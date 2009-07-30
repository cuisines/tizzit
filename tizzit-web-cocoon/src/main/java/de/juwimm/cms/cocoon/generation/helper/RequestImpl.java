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
package de.juwimm.cms.cocoon.generation.helper;

import java.util.Enumeration;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;

import de.juwimm.cms.plugins.server.Request;

/**
 * This is the CocoonRequest implementation for conquest plugins
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class RequestImpl implements Request {
	private static Logger log = Logger.getLogger(RequestImpl.class);
	private org.apache.cocoon.environment.Request delegateRequest = null;

	public RequestImpl(org.apache.cocoon.environment.Request delegateRequest) {
		this.delegateRequest = delegateRequest;
	}

	public String getParameter(String parameter) {
		return delegateRequest.getParameter(parameter);
	}

	@SuppressWarnings("unchecked")
	public Enumeration<String> getParameterNames() {
		return delegateRequest.getParameterNames();
	}

	public Cookie[] getCookies() {
		if (log.isDebugEnabled()) log.debug("ENTERING GET COOKIES");
		org.apache.cocoon.environment.Cookie[] cookies = this.delegateRequest.getCookies();
		Cookie[] realCookies = new Cookie[cookies.length];
		if (log.isDebugEnabled()) log.debug("FOUND " + cookies.length + " COOKIES");
		for (int i = 0; i < cookies.length; i++) {
			String name = cookies[i].getName();
			String value = cookies[i].getValue();
			Cookie cook = new javax.servlet.http.Cookie(name, value);
			realCookies[i] = cook;
		}
		if (log.isDebugEnabled()) log.debug("LEAVING GET COOKIES");
		return realCookies;
	}

}
