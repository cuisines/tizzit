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

import org.apache.avalon.framework.component.Component;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.Composable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.thread.SingleThreaded;
import org.apache.cocoon.ResourceNotFoundException;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.webapps.authentication.AuthenticationManager;
import org.apache.cocoon.webapps.authentication.user.RequestState;
import org.apache.log4j.Logger;

import de.juwimm.cms.beans.WebServiceSpring;
import de.juwimm.cms.cocoon.helper.CocoonSpringHelper;
import de.juwimm.cms.exceptions.UserException;

/**
 *
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha Kulawik</a>
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class CmsTemplateAction extends AbstractAction implements SingleThreaded, Composable {
	private Logger log = Logger.getLogger(CmsTemplateAction.class);
	private ComponentManager manager = null;
	private WebServiceSpring webSpringBean = null;

	public CmsTemplateAction() {

	}

	public void compose(ComponentManager manager) {
		this.manager = manager;
	}

	public Map act(Redirector redirector, SourceResolver parm2, Map objectModel, String parm4, Parameters par) throws Exception {
		if (log.isDebugEnabled()) log.debug("start act");
		try {
			webSpringBean = (WebServiceSpring) CocoonSpringHelper.getBean(objectModel, CocoonSpringHelper.WEB_SERVICE_SPRING);
		} catch (Exception exe) {
			log.error(exe);
		}
		Map<String, String> sitemapParams = new HashMap<String, String>();
		Map<String, String> sitemapInputParams = new HashMap<String, String>();

		// ------------------------------ PARAMETERS ------------------------------
		Integer siteId = null;
		try {
			siteId = new Integer(par.getParameter("siteId"));
		} catch (Exception exe) {
			log.error("No siteId found!");
		}
		String path = par.getParameter("path");
		String language = par.getParameter("language");
		String viewType = par.getParameter("viewType");
		Integer viewComponentId = null;
		try {
			viewComponentId = new Integer(par.getParameter("viewComponentId"));
		} catch (Exception e) {
			if (log.isDebugEnabled()) log.debug("no viewComponentId found");
		}

		String securityHandler = "juwimm-cms-user";
		try {
			securityHandler = par.getParameter("handler");
			if (securityHandler == null || "".equals(securityHandler)) {
				securityHandler = "juwimm-cms-user";
			}
		} catch (Exception e) {
		}

		// ------------------------------ AUTHENTICATION ------------------------------
		try {
			AuthenticationManager authManager = null;
			try {
				authManager = (AuthenticationManager) this.manager.lookup(AuthenticationManager.ROLE);
				// do authentication
				if (!authManager.checkAuthentication(null, securityHandler, null)) {
					if (log.isDebugEnabled()) log.debug("NOAUTH");
				} else {
					if (log.isDebugEnabled()) log.debug("AUTH");
					RequestState state = authManager.getState();
					sitemapParams = state.getHandler().getContext().getContextInfo();
				}
			} finally {
				this.manager.release((Component) authManager);
			}
		} catch (Exception exe) {
		}

		Request request = ObjectModelHelper.getRequest(objectModel);
		HttpSession session = request.getSession(true);

		Map<String, String> safeguardMap = null;

		try {
			safeguardMap = (Map<String, String>) session.getAttribute("safeGuardService");
			if (safeguardMap == null) {
				if (log.isDebugEnabled()) log.debug("no SafeguardMap");
				safeguardMap = new HashMap<String, String>();
				if (log.isDebugEnabled()) log.debug("created new SafeguardMap");
				session.setAttribute("safeGuardService", safeguardMap);
				if (log.isDebugEnabled()) log.debug("put SafeguardMap into Session");
			} else {
				if (log.isDebugEnabled()) log.debug("found SafeguardMap");
			}
		} catch (Exception cookieex) {
			log.warn("SafeGuard-Error: " + cookieex.getMessage());
		}

		sitemapInputParams.put("viewComponentId", viewComponentId == null ? null : viewComponentId.toString());
		sitemapInputParams.put("siteId", siteId == null ? null : siteId.toString());
		sitemapInputParams.put("language", language);
		sitemapInputParams.put("path", path);
		sitemapInputParams.put("viewType", viewType);

		this.prepareSafeguard(request, sitemapInputParams);
		final String keySafeGuardUserName = "safeguardUsername";
		if (sitemapInputParams.containsKey(keySafeGuardUserName)) {
			safeguardMap.put(keySafeGuardUserName, sitemapInputParams.get(keySafeGuardUserName));
		}
		if (safeguardMap.containsKey(keySafeGuardUserName)) {
			sitemapParams.put(keySafeGuardUserName, safeguardMap.get(keySafeGuardUserName));
		}

		// ------------------------------ SOLVE LINK ------------------------------
		try {
			sitemapParams.putAll(this.webSpringBean.getSitemapParameters(sitemapInputParams, safeguardMap));
			sitemapParams.put("safeguardlogedin", String.valueOf(safeguardMap.size() > 0));
		} catch (Exception exe) {
			throw new ResourceNotFoundException("File " + path + " not found");
		}
		if (log.isDebugEnabled()) log.debug("finished act");
		return sitemapParams;
	}

	/*
	 * If the Request contains Username and Password for SafeGuard, these values are put into the HashMap of all SiteMapParameters
	 */
	private void prepareSafeguard(Request request, Map cookiemap) throws Exception {
		if (log.isDebugEnabled()) log.debug("trying to filter SafeGuard-Authentification-Data from request: " + request.getRequestURI());
		String username = request.getParameter("cqusername");
		if (username != null) {
			if (log.isDebugEnabled()) log.debug("cqusername found!");
			String password = request.getParameter("cqpassword");
			cookiemap.put("safeguardUsername", username);
			cookiemap.put("safeguardPassword", password != null ? password : "");
		} else {
			if (log.isDebugEnabled()) log.debug("no cqusername found in request");
		}
	}

}
