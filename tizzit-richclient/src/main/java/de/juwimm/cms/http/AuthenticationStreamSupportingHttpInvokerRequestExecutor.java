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
package de.juwimm.cms.http;

import static de.juwimm.cms.common.Constants.rb;

import java.awt.Dialog.ModalityType;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.remoting.httpinvoker.HttpInvokerClientConfiguration;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.tizzit.util.spring.httpinvoker.StreamSupportingHttpInvokerRequestExecutor;

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.http.HttpClientWrapper;
import de.juwimm.cms.util.UIConstants;

/**
 * Adds BASIC authentication support to
 * <code>SimpleHttpInvokerRequestExecutor</code>.
 * 
 * @author Ben Alex
 * @version $Id: AuthenticationSimpleHttpInvokerRequestExecutor.java 1784
 *          2007-02-24 21:00:24Z luke_t $
 */
public class AuthenticationStreamSupportingHttpInvokerRequestExecutor extends StreamSupportingHttpInvokerRequestExecutor {
	private static final Log log = LogFactory.getLog(AuthenticationStreamSupportingHttpInvokerRequestExecutor.class);
	private static boolean isErrorMessageShown = false;

	public AuthenticationStreamSupportingHttpInvokerRequestExecutor() {
		setReadTimeout(10 * 60 * 60 * 1000);
	}

	protected PostMethod createPostMethodForStreaming(final HttpInvokerClientConfiguration config) throws IOException {
		HttpClientWrapper.getInstance().setHostConfiguration(super.getHttpClient(), new URL(config.getServiceUrl()));
		final PostMethod postMethod = new PostMethod(config.getServiceUrl());
		postMethod.setRequestHeader(HTTP_HEADER_CONTENT_TYPE, CONTENT_TYPE_SERIALIZED_OBJECT_WITH_STREAM);
		//Solution for TIZZIT-282
		//postMethod.setContentChunked(true);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if ((auth != null) && (auth.getName() != null) && (auth.getCredentials() != null)) {
			postMethod.setDoAuthentication(true);
			String base64 = auth.getName() + ":" + auth.getCredentials().toString();
			postMethod.addRequestHeader("Authorization", "Basic " + new String(Base64.encodeBase64(base64.getBytes())));

			if (log.isDebugEnabled()) {
				// log.debug("HttpInvocation now presenting via BASIC
				// authentication SecurityContextHolder-derived: " +
				// auth.toString());
				log.debug("HttpInvocation now presenting via BASIC authentication SecurityContextHolder-derived. User: " + auth.getName());
			}
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Unable to set BASIC authentication header as SecurityContext did not provide " + "valid Authentication: " + auth);
			}
		}
		return postMethod;
	}

	protected PostMethod createPostMethod(HttpInvokerClientConfiguration config) throws IOException {
		HttpClientWrapper.getInstance().setHostConfiguration(super.getHttpClient(), new URL(config.getServiceUrl()));
		PostMethod postMethod = new PostMethod(config.getServiceUrl());
		if (isAcceptGzipEncoding()) {
			postMethod.addRequestHeader(HTTP_HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if ((auth != null) && (auth.getName() != null) && (auth.getCredentials() != null)) {
			postMethod.setDoAuthentication(true);
			String base64 = auth.getName() + ":" + auth.getCredentials().toString();
			postMethod.addRequestHeader("Authorization", "Basic " + new String(Base64.encodeBase64(base64.getBytes())));

			if (log.isDebugEnabled()) {
				// log.debug("HttpInvocation now presenting via BASIC
				// authentication SecurityContextHolder-derived: " +
				// auth.toString());
				log.debug("HttpInvocation now presenting via BASIC authentication SecurityContextHolder-derived: " + auth.getName());
			}
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Unable to set BASIC authentication header as SecurityContext did not provide " + "valid Authentication: " + auth);
			}
		}
		return postMethod;
	}

	@Override
	protected void executePostMethod(HttpInvokerClientConfiguration config, HttpClient httpClient, PostMethod postMethod) throws IOException {
		HttpClientWrapper.getInstance().setHostConfiguration(super.getHttpClient(), new URL(config.getServiceUrl()));
		try {
			super.executePostMethod(config, httpClient, postMethod);
			//if call succeeds 
			isErrorMessageShown = false;
		} catch (IOException e) {
			if ((e instanceof SocketException && e.getMessage().equals("Connection reset")) || (e instanceof ConnectException && e.getMessage().equals("Connection refused"))) {
				if (!isErrorMessageShown) {
					isErrorMessageShown = true;
					JOptionPane errorOptionPane = new JOptionPane(Constants.rb.getString("exception.connectionToServerLost"), JOptionPane.INFORMATION_MESSAGE);
					JDialog errorDialogPane = errorOptionPane.createDialog(UIConstants.getMainFrame(), rb.getString("dialog.title"));
					errorDialogPane.setModalityType(ModalityType.MODELESS);
					errorDialogPane.setVisible(true);
					errorDialogPane.setEnabled(true);
					errorDialogPane.addWindowListener(new WindowAdapter() {
						public void windowDeactivated(WindowEvent e) {
							if (((JDialog) e.getSource()).isVisible() == false) {
								isErrorMessageShown = false;
							}
						}
					});

				}
				log.error("server is not reachable");
			} else {
				e.printStackTrace();
			}
		}
		System.out.println(postMethod.getResponseHeaders());
	}

	@Override
	protected RemoteInvocationResult doExecuteRequest(HttpInvokerClientConfiguration config, ByteArrayOutputStream baos) throws IOException, ClassNotFoundException {
		HttpClientWrapper.getInstance().setHostConfiguration(super.getHttpClient(), new URL(config.getServiceUrl()));
		RemoteInvocationResult r = super.doExecuteRequest(config, baos);
		return r;
	}

}
