/**
 * Copyright (c) 2009 Juwi MacMillan Group GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.juwimm.cms.servlets;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Properties;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.log4j.Logger;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id:ProxyJNLPDownloadServlet.java 18392 2007-04-02 15:32:06Z kulawik $
 */
public class ProxyJNLPDownloadServlet extends HttpServlet implements javax.servlet.Filter {
	private static final long serialVersionUID = 5961639019230634706L;
	private static final String PROPERTIES_FILENAME = "tizzit.properties";
	private static final String PLACEHOLDER_PROXY_HOST = "$$fake80codebase";
	private static final String PLACEHOLDER_LOG4J_PROPERTIES = "$$fakeClientMailAppenderProperties";

	//Mail appender key properties
	private static final String log4jPrefix = "log4j.appender.email.";
	private static final String tizzitPropetiesMailAppenderPrefix = "tizzitPropertiesBeanSpring.clientMailAppenderConfig.";
	private static final String SMTPHostKey = "tizzitPropertiesBeanSpring.clientMailAppenderConfig.SMTPHost";
	private static final String fromKey = "tizzitPropertiesBeanSpring.clientMailAppenderConfig.from";
	private static final String toKey = "tizzitPropertiesBeanSpring.clientMailAppenderConfig.to";

	private static Logger log = Logger.getLogger(ProxyJNLPDownloadServlet.class);
	private static final String MAX_HEAP = "max-heap-size=\"";
	private static final String INITIAL_HEAP = "initial-heap-size=\"";
	private Properties props = null;
	private String clientMailAppenderProperties = "clientMailAppenderProperties=\"";
	private String serverHost = null;
	private String serverPort = null;

	public void init(FilterConfig filterConfig) {
		if (log.isDebugEnabled()) log.debug("Init");

		URL cp = this.getClass().getClassLoader().getResource(PROPERTIES_FILENAME);
		try {
			InputStream is = new FileInputStream(cp.getFile());
			props = new Properties();
			try {
				is = new FileInputStream(cp.getFile());
			} catch (FileNotFoundException e) {
				log.error("Could not find " + PROPERTIES_FILENAME);
			}
			props.load(is);

		} catch (FileNotFoundException e) {
			log.error("Could not find " + PROPERTIES_FILENAME);
		} catch (IOException e) {
			log.warn("Unable to load jnlpHost / jnlpPort props from \"" + PROPERTIES_FILENAME + "\"!");
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
		try {
			long startTime = System.currentTimeMillis();
			HttpServletRequest req = (HttpServletRequest) request;
			if (log.isDebugEnabled()) log.debug("doFilter start for: " + req.getRequestURL().toString());
			URL serverName = new URL(req.getRequestURL().toString());

			if (serverName.getPath().endsWith(".jnlp")) {
				if (props == null) {
					log.error("Unable to load \"" + PROPERTIES_FILENAME + "\"! You MUST restart the server!");
				} else {
					if (serverHost == null) {
						serverHost = props.getProperty("tizzitPropertiesBeanSpring.jnlpHost", "");
						if (serverHost.equalsIgnoreCase("")) {
							serverHost = serverName.getHost();
						}

						serverPort = props.getProperty("tizzitPropertiesBeanSpring.jnlpPort", "");
						if (serverPort.equalsIgnoreCase("")) {
							if (serverName.getProtocol().equalsIgnoreCase("https")) {
								serverPort = "443";
							} else {
								serverPort = "80";
							}
						}
					}
				}

				String server80Name = serverName.getProtocol() + "://" + serverHost + ":" + serverPort + serverName.getPath().substring(0, serverName.getPath().lastIndexOf("/"));
				if (log.isDebugEnabled()) log.debug("$$fake80codebase will be replaced by: " + server80Name);

				final HttpServletResponse resp = (HttpServletResponse) response;
				final ByteArrayPrintWriter pw = new ByteArrayPrintWriter();
				HttpServletResponse wrappedResp = new HttpServletResponseWrapper(resp) {
					@Override
					public PrintWriter getWriter() {
						return pw.getWriter();
					}

					@Override
					public ServletOutputStream getOutputStream() {
						return pw.getStream();
					}
				};
				chain.doFilter(request, wrappedResp);

				byte[] bytes = pw.toByteArray();
				String newJnlp = new String(bytes);
				//server argument
				newJnlp = newJnlp.replace(PLACEHOLDER_PROXY_HOST, server80Name);

				//mail appender argument
				//should be log4jProperties="k1=v1;k2=v2"			
				addLog4jPropertyArgument(SMTPHostKey);
				addLog4jPropertyArgument(fromKey);
				addLog4jPropertyArgument(toKey);
				clientMailAppenderProperties += "\"";
				newJnlp = newJnlp.replace(PLACEHOLDER_LOG4J_PROPERTIES, clientMailAppenderProperties);

				if (isMacClient(req)) {
					newJnlp = this.cutMemorySettings(newJnlp);
				}
				if (log.isDebugEnabled()) log.debug("newJnlp new: " + newJnlp);
				resp.setContentLength(newJnlp.length());
				resp.getOutputStream().print(newJnlp);
				resp.getOutputStream().close();
			} else {
				chain.doFilter(request, response);
			}
			long stopTime = System.currentTimeMillis();
			if (log.isDebugEnabled()) log.debug("Time to execute request: " + (stopTime - startTime) + " milliseconds");
		} catch (ServletException sx) {
			log.error(sx.getMessage());
		} catch (IOException iox) {
			log.error(iox.getMessage());
		}
	}

	private void addLog4jPropertyArgument(String propertyKey) {
		String propertyValue = props.getProperty(propertyKey);
		if (propertyValue == null || "".equals(propertyValue)) {
			log.error("Property " + propertyKey + "is missing from tizzit.properties");
			return;
		}
		//properties of the email appender e.g. log4j.appender.email.BufferSize=1
		propertyKey = propertyKey.replace(tizzitPropetiesMailAppenderPrefix, log4jPrefix);
		this.clientMailAppenderProperties += propertyKey + "=" + propertyValue + ";";
	}

	@Override
	public void destroy() {
		if (log.isDebugEnabled()) log.debug("destroy");
	}

	/**
	 * 
	 */
	private static class ByteArrayServletStream extends ServletOutputStream {
		private final ByteArrayOutputStream baos;

		ByteArrayServletStream(ByteArrayOutputStream baos) {
			this.baos = baos;
		}

		@Override
		public void write(int param) throws java.io.IOException {
			baos.write(param);
		}
	}

	/**
	 * 
	 */
	private static class ByteArrayPrintWriter {
		private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		private final PrintWriter pw = new PrintWriter(baos);
		private final ServletOutputStream sos = new ByteArrayServletStream(baos);

		public PrintWriter getWriter() {
			return pw;
		}

		public ServletOutputStream getStream() {
			return sos;
		}

		byte[] toByteArray() {
			return baos.toByteArray();
		}
	}

	private boolean isMacClient(HttpServletRequest request) {
		String userAgent = request.getHeader("user-agent");
		if (log.isDebugEnabled()) log.debug("User-Agent: " + userAgent);
		return ((userAgent != null) && (userAgent.indexOf("Mac") >= 0));
	}

	private String cutMemorySettings(String oldJnlp) {
		StringBuilder sb = new StringBuilder();
		// cut max-heap-size
		int maxHeapIndex = oldJnlp.indexOf(ProxyJNLPDownloadServlet.MAX_HEAP);
		if (maxHeapIndex > -1) {
			int endMaxHeapIndex = oldJnlp.indexOf("\"", maxHeapIndex + ProxyJNLPDownloadServlet.MAX_HEAP.length());
			sb.append(oldJnlp.substring(0, maxHeapIndex));
			sb.append(oldJnlp.substring(endMaxHeapIndex + 1, oldJnlp.length()));
		}
		// cut initial-heap-size
		String tmp = sb.toString();
		int initialHeapIndex = tmp.indexOf(ProxyJNLPDownloadServlet.INITIAL_HEAP);
		if (initialHeapIndex > -1) {
			sb = new StringBuilder();
			int endInitialHeapIndex = tmp.indexOf("\"", initialHeapIndex + ProxyJNLPDownloadServlet.INITIAL_HEAP.length());
			sb.append(tmp.substring(0, initialHeapIndex));
			sb.append(tmp.substring(endInitialHeapIndex + 1, tmp.length()));
		}
		return sb.toString();
	}

}