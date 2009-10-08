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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

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

/**
 * Adds BASIC authentication support to
 * <code>SimpleHttpInvokerRequestExecutor</code>.
 * 
 * @author Ben Alex
 * @version $Id: AuthenticationSimpleHttpInvokerRequestExecutor.java 1784
 *          2007-02-24 21:00:24Z luke_t $
 */
public class Server2ServerAuthenticationStreamSupportingHttpInvokerRequestExecutor extends StreamSupportingHttpInvokerRequestExecutor {
	private static final Log log = LogFactory.getLog(Server2ServerAuthenticationStreamSupportingHttpInvokerRequestExecutor.class);

	public Server2ServerAuthenticationStreamSupportingHttpInvokerRequestExecutor() {
		setReadTimeout(10 * 60 * 60 * 1000);
	}

	protected PostMethod createPostMethodForStreaming(final HttpInvokerClientConfiguration config) throws IOException {
		HttpClientWrapper.getInstance().setHostConfiguration(super.getHttpClient(), new URL(config.getServiceUrl()));
		final PostMethod postMethod = new PostMethod(config.getServiceUrl());
		postMethod.setRequestHeader(HTTP_HEADER_CONTENT_TYPE, CONTENT_TYPE_SERIALIZED_OBJECT_WITH_STREAM);
		postMethod.setContentChunked(true);

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
		super.executePostMethod(config, httpClient, postMethod);
	}

	@Override
	protected RemoteInvocationResult doExecuteRequest(HttpInvokerClientConfiguration config, ByteArrayOutputStream baos) throws IOException, ClassNotFoundException {
		HttpClientWrapper.getInstance().setHostConfiguration(super.getHttpClient(), new URL(config.getServiceUrl()));
		RemoteInvocationResult r = super.doExecuteRequest(config, baos);
		return r;
	}

}
