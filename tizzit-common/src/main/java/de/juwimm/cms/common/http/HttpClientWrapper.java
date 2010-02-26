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
package de.juwimm.cms.common.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

import javax.net.ssl.SSLPeerUnverifiedException;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.auth.InvalidCredentialsException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.SSLProtocolSocketFactory;
import org.apache.log4j.Logger;

import de.juwimm.cms.common.ClientDesEncrypter;
import de.juwimm.cms.common.HttpMessages;

/**
 * <p>
 * Title: Tizzit
 * </p>
 * <p>
 * Description: Enterprise Content Management
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Revision: 1.1 $
 */

public class HttpClientWrapper {

	private static final String CQ_HTTP_PPROXY_PASSWORD_ENCRYPTED = "cqHttpPproxyPasswordEncrypted";
	public static final AuthScope AUTHSCOPE_ANY = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
	private static Logger log = Logger.getLogger(HttpClientWrapper.class);
	private static HttpClientWrapper instance = null;
	private MultiThreadedHttpConnectionManager connectionManager = null;
	private String httpProxyHost = null;
	private String httpProxyPort = null;
	private static String httpProxyUser = null;
	private static String httpProxyPassword = null;
	private static boolean useNTproxy = false;
	private Properties fileProp = null;
	private final File propFile = new File(System.getProperty("user.home") + System.getProperty("file.separator") + ".tizzit");
	private final HashMap<String, HostConfiguration> hostMap = new HashMap<String, HostConfiguration>();

	public static HttpClientWrapper getInstance() {
		if (instance == null) {
			instance = new HttpClientWrapper();
		}
		return instance;
	}

	protected HttpClientWrapper() {
		// Protocol.registerProtocol("https", new Protocol("https", new
		// EasySSLProtocolSocketFactory(), 443));
		Protocol.registerProtocol("https", new Protocol("https", (ProtocolSocketFactory) new SSLProtocolSocketFactory(), 443));
		System.setProperty("java.protocol.handler.pkgs", "org.apache.commons.httpclient");

		connectionManager = new MultiThreadedHttpConnectionManager();
		connectionManager.getParams().setDefaultMaxConnectionsPerHost(20);
		connectionManager.getParams().setMaxTotalConnections(40);
		connectionManager.getParams().setStaleCheckingEnabled(true);

		setHttpProxyHost(System.getProperty("https.proxyHost"));
		setHttpProxyPort(System.getProperty("https.proxyPort"));
		if (propFile.exists()) {
			try {
				FileInputStream fis = new FileInputStream(propFile);
				fileProp = new Properties();
				fileProp.load(fis);
				fis.close();
				setHttpProxyUser(fileProp.getProperty("http.proxyUser", "").toString());
				// setHttpProxyPassword(fileProp.getProperty("http.proxyPassword",
				// "").toString());
				setHttpProxyPassword(fileProp.getProperty(HttpClientWrapper.CQ_HTTP_PPROXY_PASSWORD_ENCRYPTED, "no").equalsIgnoreCase("no") ? fileProp.getProperty("http.proxyPassword").toString() : new ClientDesEncrypter().decrypt(fileProp.getProperty("http.proxyPassword")));
			} catch (Exception exe) {
				log.error("Could not update the localuser properties from the .tizzit file", exe);
			}
		}

	}
	
	public boolean isUsingProxy() {
		return (getHttpProxyHost() == null || getHttpProxyHost().equalsIgnoreCase("")) ? false : true;
	}

	public String getProxyServer() {
		return getHttpProxyHost();
	}

	/**
	 * save in ${user.home}\.conquest (e.g. %USERPROFILE%\.tizzit in windows)
	 * encrypt password always decrypt only if cqHttpPproxyPasswordEncrypted=yes
	 * in ${user.home}\.tizzit
	 * 
	 */
	protected void saveProperties(boolean save) {
		if (save == true) {
			if (fileProp == null) {
				fileProp = new Properties();
			}
			fileProp.setProperty("http.proxyUser", getHttpProxyUser());
			fileProp.setProperty("http.proxyPassword", new ClientDesEncrypter().encrypt(getHttpProxyPassword()));
			fileProp.setProperty(HttpClientWrapper.CQ_HTTP_PPROXY_PASSWORD_ENCRYPTED, "yes");
			try {
				if (propFile.exists()) {
					propFile.delete();
				}
				propFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(propFile, false);
				fileProp.store(fos, "Local Tizzit Properties");
				fos.close();
			} catch (Exception exe) {
				log.error("Error saving to .tizzit File: ", exe);
			}
		}
	}

	public synchronized String getString(URL destUrl) throws IOException {
		return getString(destUrl, null, null);
	}

	public synchronized String getString(URL destUrl, String userName, String password) throws IOException {
		HttpMethodBase method = invoke(destUrl, userName, password);
		String retString = method.getResponseBodyAsString();
		method.releaseConnection();
		return retString;
	}

	public synchronized String getString(String destUrl) throws IOException {
		return getString(destUrl, null, null);
	}

	public synchronized String getString(String destUrl, String userName, String password) throws IOException {
		HttpMethodBase method = invoke(destUrl, userName, password);
		String retString = method.getResponseBodyAsString();
		method.releaseConnection();
		return retString;
	}

	public synchronized byte[] getByte(String destUrl) throws IOException {
		return getByte(destUrl, null, null);
	}

	public synchronized byte[] getByte(String destUrl, String userName, String password) throws IOException {
		HttpMethodBase method = invoke(destUrl, userName, password);
		byte[] retArr;
		retArr = method.getResponseBody();
		method.releaseConnection();
		return retArr;
	}

	protected HttpMethodBase invoke(String destUrl, String userName, String password) throws URIException {
		// HttpMethodBase method = null;
		URL targetURL = null;
		try {
			targetURL = new URL(destUrl);
		} catch (MalformedURLException exe) {
			log.error("this url is not valid: " + destUrl, exe);
		}
		return (targetURL != null) ? invoke(targetURL, userName, password) : null;
	}

	protected HttpMethodBase invoke(URL targetURL, String userName, String password) throws URIException {
		HttpMethodBase method = null;
		if (log.isDebugEnabled()) log.debug(targetURL.toExternalForm());
		method = new GetMethod(targetURL.toExternalForm());
		HttpClient httpClient = getNewHttpClient();
		if (userName != null) {
			// Credentials for destination URL
			Credentials cred = new UsernamePasswordCredentials(userName, password);
			httpClient.getParams().setAuthenticationPreemptive(true);
			httpClient.getState().setCredentials(AUTHSCOPE_ANY, cred);
		}
		setHostConfiguration(httpClient, targetURL);
		String returnMessage = null;
		int returnCode = 500;
		try {
			returnCode = httpClient.executeMethod(method);
		} catch (InvalidCredentialsException exe) {
			if (log.isInfoEnabled()) log.info("Invalid credentials trying to authenticate: " + exe.getMessage());
		} catch (HttpException exe) {
			log.error("an unknown error occured (HttpException): " + exe.getMessage());
		} catch (SSLPeerUnverifiedException exe) {
			returnCode = 516;
			returnMessage = exe.getMessage();
		} catch (IOException exe) {
			log.error("an unknown error occured (IOException): " + exe.getMessage());
		} catch (Exception exe) {
			log.error("an unknown error occured: " + exe.getMessage());
		}

		if ((returnCode > 199) && (returnCode < 300)) {
			// return is OK - so fall through
			if (log.isDebugEnabled()) log.debug("good return code: " + returnCode);
		} else if (returnCode == 401) {
			returnMessage = HttpMessages.getString("HttpClientWrapper.401_authRequired");
		} else if (returnCode == 404) {
			returnMessage = HttpMessages.getString("HttpClientWrapper.404_notFound");
		} else if (returnCode == 407) {
			returnMessage = HttpMessages.getString("HttpClientWrapper.407_proxyAuthRequired");
		} else if (returnCode == 403) {
			returnMessage = HttpMessages.getString("HttpClientWrapper.403_Forbidden");
		} else if (returnCode == 503) {
			returnMessage = HttpMessages.getString("HttpClientWrapper.503_ServiceUnavailable");
		} else if (returnCode == 504) {
			returnMessage = HttpMessages.getString("HttpClientWrapper.504_ProxyTimeout");
		} else if (returnCode == 516) {
			returnMessage = HttpMessages.getString("HttpClientWrapper.516_SSLPeerUnverified", returnMessage);
		} else {
			returnMessage = "Unknown error with return code " + returnCode;
		}
		if (returnMessage != null) {
			throw new URIException(returnCode, returnMessage);
		}
		return method;
	}

	public HttpClient getNewHttpClient() {
		HttpClient client = new HttpClient(connectionManager);
		return client;
	}

	public void setHostConfiguration(HttpClient client, URL targetURL) {
		int port = targetURL.getPort();
		String host = targetURL.getHost();
		HostConfiguration config = hostMap.get(host + ":" + port);
		if (config == null) {
			config = new HostConfiguration();
			if (port == -1) {
				if (targetURL.getProtocol().equalsIgnoreCase("https")) {
					port = 443;
				} else {
					port = 80;
				}
			}
			config.setHost(host, port, targetURL.getProtocol());
		}
		// in the meantime HttpProxyUser and HttpProxyPasword might have changed
		// (DlgUsernamePassword) or now trying NTLM instead of BASE
		// authentication
		if (getHttpProxyHost() != null && getHttpProxyPort() != null && getHttpProxyHost().length() > 0 && getHttpProxyPort().length() > 0) {
			client.getParams().setAuthenticationPreemptive(true);
			int proxyPort = new Integer(getHttpProxyPort()).intValue();
			config.setProxy(getHttpProxyHost(), proxyPort);
			if (getHttpProxyUser() != null && getHttpProxyUser().length() > 0) {
				Credentials proxyCred = null;
				if (isUseNTproxy()) {
					proxyCred = new NTCredentials(getHttpProxyUser(), getHttpProxyPassword(), getHttpProxyHost(), "");
				} else {
					proxyCred = new UsernamePasswordCredentials(getHttpProxyUser(), getHttpProxyPassword());
				}
				client.getState().setProxyCredentials(AUTHSCOPE_ANY, proxyCred);

			}
		}
		hostMap.put(host + ":" + port, config);
		client.setHostConfiguration(config);

	}

	/**
	 * @param httpProxyHost
	 *                The httpProxyHost to set.
	 */
	protected void setHttpProxyHost(String httpProxyHost) {
		this.httpProxyHost = httpProxyHost;
	}

	/**
	 * @return Returns the httpProxyHost.
	 */
	protected String getHttpProxyHost() {
		return httpProxyHost;
	}

	/**
	 * @param httpProxyPort
	 *                The httpProxyPort to set.
	 */
	protected void setHttpProxyPort(String httpProxyPort) {
		this.httpProxyPort = httpProxyPort;
	}

	/**
	 * @return Returns the httpProxyPort.
	 */
	protected String getHttpProxyPort() {
		return httpProxyPort;
	}

	/**
	 * @param httpProxyUser
	 *                The httpProxyUser to set.
	 */
	protected void setHttpProxyUser(String httpProxyUser) {
		HttpClientWrapper.httpProxyUser = httpProxyUser;
	}

	/**
	 * @return Returns the httpProxyUser.
	 */
	protected String getHttpProxyUser() {
		return httpProxyUser;
	}

	/**
	 * @param httpProxyPassword
	 *                The httpProxyPassword to set.
	 */
	protected void setHttpProxyPassword(String httpProxyPassword) {
		HttpClientWrapper.httpProxyPassword = httpProxyPassword;
	}

	/**
	 * @return Returns the httpProxyPassword.
	 */
	protected String getHttpProxyPassword() {
		return httpProxyPassword;
	}

	/**
	 * @param useNTproxy
	 *                The useNTproxy to set.
	 */
	protected void setUseNTproxy(boolean useNTproxy) {
		this.useNTproxy = useNTproxy;
	}

	/**
	 * @return Returns the useNTproxy.
	 */
	protected boolean isUseNTproxy() {
		return useNTproxy;
	}
}