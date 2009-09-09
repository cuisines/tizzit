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

import javax.servlet.http.Cookie;

import org.apache.cocoon.environment.http.HttpCookie;

import de.juwimm.cms.plugins.server.Response;

/**
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class ResponseImpl implements Response {
	private org.apache.cocoon.environment.Response delegateResponse = null;

	public ResponseImpl(org.apache.cocoon.environment.Response response) {
		this.delegateResponse = response;
	}

	public void addCookie(Cookie cookie) {
		HttpCookie cocoonCookie = new HttpCookie(cookie);
		this.delegateResponse.addCookie(cocoonCookie);
	}

}
