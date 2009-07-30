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

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.*;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.log4j.Logger;

/**
 * Action for destroying Safeguard-Map of authorized Realms for current User
 * <p><h5>Configuration:</h5>
 * <pre>
 * &lt;map:action name="safeguard.logout" src="de.juwimm.cms.cocoon.acting.SafeguardLogoutAction"/&gt;
 * </pre>
 * <p><h5>Usage:</h5>
 * <pre>
 * &lt;map:match pattern="** /logout"&gt;
 *     &lt;map:act type="safeguard.logout"/&gt;
 *     &lt;map:redirect-to uri="/"/&gt;
 * &lt;/map:match&gt;
 * </pre>
 * </p>
 * <p><h5>Result:</h5>The action returns an empty map as parameters to the sitemap:</br>
 * </p>
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 * @since ConQuest 2.4.4
 */
public class SafeguardLogoutAction extends AbstractAction {
	private Logger log = Logger.getLogger(SafeguardLogoutAction.class);

	public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters parameters) throws Exception {
		if (log.isDebugEnabled()) log.debug("start acting");
		Map<String, String> sitemapParams = new HashMap<String, String>();

		Request request = ObjectModelHelper.getRequest(objectModel);
		Session session = request.getSession(true);

		Map safeguardMap = new HashMap();

		try {
			safeguardMap = (Map) session.getAttribute("safeGuardService");
			if (safeguardMap == null) {
				if (log.isDebugEnabled()) log.debug("no SafeguardMap");
				safeguardMap = new HashMap();
				if (log.isDebugEnabled()) log.debug("created new SafeguardMap");
				session.setAttribute("safeGuardService", safeguardMap);
				if (log.isDebugEnabled()) log.debug("put SafeguardMap into Session");
			} else {
				if (log.isDebugEnabled()) log.debug("found SafeguardMap");
				safeguardMap.clear();
				if (log.isDebugEnabled()) log.debug("cleared SafeguardMap");
			}
		} catch (Exception cookieex) {
			log.warn("SafeGuard-Error: " + cookieex.getMessage());
		}

		return sitemapParams;
	}

}
