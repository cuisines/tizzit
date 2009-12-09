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
package de.juwimm.cms.cocoon.acting;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.log4j.Logger;

import de.juwimm.cms.cocoon.helper.CocoonSpringHelper;
import de.juwimm.cms.safeguard.remote.SafeguardServiceSpring;

/**
 *
 * @author <a href="mailto:michael.meyer@juwimm.com">Michael Meyer</a><br/>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public final class SafeguardAuthenticationAction extends AbstractAction {
	private static Logger log = Logger.getLogger(SafeguardAuthenticationAction.class);

	public SafeguardAuthenticationAction() {
	}

	@SuppressWarnings("unchecked")
	public Map act(Redirector arg0, SourceResolver arg1, Map objectModel, String arg3, Parameters arg4) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			Request request = ObjectModelHelper.getRequest(objectModel);

			String username = request.getParameter("username");
			String password = request.getParameter("password");
			Integer viewComponentId = new Integer(request.getParameter("viewComponentId"));

			String path = request.getParameter("path");
			String language = request.getParameter("language");

			if (log.isDebugEnabled()) {
				log.debug("USERNAME " + username);
				log.debug("PASSWORD ********");
				log.debug("VIEWCOMPONENTID " + viewComponentId);
				log.debug("PATH " + path);
				log.debug("LANGUAGE " + language);
			}

			SafeguardServiceSpring safeguard = (SafeguardServiceSpring) CocoonSpringHelper.getBean(objectModel, CocoonSpringHelper.SAFEGUARD_SERVICE_SPRING);
			byte login = safeguard.login(password, username, viewComponentId);
			if (log.isDebugEnabled()) log.debug("USER LOGGED IN " + login);
			if (login == 0) {
				String realmkey = safeguard.getRealmIdAndType(viewComponentId);
				HttpSession session = request.getSession(true);

				Map cookiemap = (Map) session.getAttribute("safeguard");
				cookiemap.put(realmkey, new Boolean(true));
				if (log.isDebugEnabled()) log.debug("SET COOKIE FOR USER");
				map.put("viewComponentId", viewComponentId.toString());
			}

			map.put("login", Byte.toString(login));
			map.put("path", path);
			map.put("language", language);

		} catch (Exception ex) {
			log.error("SAFEGUARD " + ex.getMessage());
		}

		return map;
	}

}
