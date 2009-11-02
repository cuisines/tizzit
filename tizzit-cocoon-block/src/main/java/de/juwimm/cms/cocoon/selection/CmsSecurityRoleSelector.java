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
package de.juwimm.cms.cocoon.selection;

import java.util.*;

import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.selection.Selector;
import org.apache.cocoon.webapps.authentication.components.DefaultAuthenticationManager;
import org.apache.cocoon.webapps.authentication.user.UserState;
import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;


/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class CmsSecurityRoleSelector extends AbstractLogEnabled implements ThreadSafe, Selector {
	private static Logger log = Logger.getLogger(CmsSecurityRoleSelector.class);
	private ArrayList<String> roles;

	public boolean select(String expression, Map objectModel, Parameters parameters) {
		String handlerName = parameters.getParameter("handler", null);
		String strRoles = expression;
		if (log.isDebugEnabled()) log.debug("Testing: " + strRoles + " in Handler " + handlerName);
		if (strRoles != null) {
			roles = new ArrayList<String>();
			StringTokenizer st = new StringTokenizer(strRoles, ",");
			while (st.hasMoreTokens()) {
				roles.add(st.nextToken());
			}
		} else {
			getLogger().warn("No attribute name given -- failing.");
			return false;
		}

		boolean retVal = true;
		Object value = ObjectModelHelper.getRequest(objectModel).getSession().getAttribute(DefaultAuthenticationManager.SESSION_ATTRIBUTE_USER_STATUS);
		UserState userState = (UserState) value;
		try {
			DocumentFragment doc = userState.getHandler(handlerName).getContext().getXML("/authentication/roles");

			Iterator it = roles.iterator();
			while (it.hasNext()) {
				String role = (String) it.next();
				String xpath = "";
				if (role.startsWith("unit_") || role.startsWith("site_") || role.startsWith("group_")
						|| role.startsWith("role_")) {
					xpath = "//role[starts-with(text(), '" + role + "')]";
				} else if (role.toUpperCase().startsWith("ROLEISSUBSTRINGOF:")) {
					// this does not work with Jaxen... 
					String searchRole = role.substring(18);
					xpath = "//role[starts-with('" + searchRole + "', text())]";
					if (log.isDebugEnabled()) log.debug("xpathquery: " + xpath);
					if (log.isDebugEnabled()) log.debug(XercesHelper.node2string(doc));
				} else {
					xpath = "//role[text()='" + role + "']";
				}

				Node nde = XercesHelper.findNode(doc, xpath);
				// Node nde = org.apache.xpath.XPathAPI.selectSingleNode(doc, xpath);
				if (nde == null) {
					if (log.isDebugEnabled()) log.debug("Could not find role: " + role);
					retVal = false;
					break;
				}
			}
		} catch (Exception exe) {
			retVal = false;
		}
		return retVal;
	}

}