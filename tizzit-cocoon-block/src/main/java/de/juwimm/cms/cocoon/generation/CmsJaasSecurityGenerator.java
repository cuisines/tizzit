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
package de.juwimm.cms.cocoon.generation;

import java.security.Principal;
import java.security.acl.Group;
import java.util.*;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginContext;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.*;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.AbstractGenerator;
import org.apache.cocoon.webapps.session.ContextManager;
import org.apache.cocoon.xml.dom.DOMStreamer;
import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.*;
import org.xml.sax.SAXException;


/**
 * <p>Title: juwimm cms</p>
 * <p>Description: content management system</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: juwi macmillan group gmbh</p>
 * @author <a href="mailto:s.kulawik@juwimm.c">Sascha Kulawik</a>
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class CmsJaasSecurityGenerator extends AbstractGenerator implements Serviceable {
	private static Logger log = Logger.getLogger(CmsJaasSecurityGenerator.class);
	private String userid = "";
	private String password = "";
	private String jaasRealm = "juwimm-cms-security-domain";
	private ServiceManager manager = null;

	/* (non-Javadoc)
	 * @see org.apache.avalon.framework.service.Serviceable#service(org.apache.avalon.framework.service.ServiceManager)
	 */
	public void service(ServiceManager manager) throws ServiceException {
		this.manager = manager;
	}

	@Override
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) {
		if (log.isDebugEnabled()) log.debug("begin setup");
		try {
			super.setup(resolver, objectModel, src, par);
			ContextManager cm = (ContextManager) this.manager.lookup(ContextManager.ROLE);
			try {
				if (cm.hasSessionContext()) {
					cm.deleteContext("authentication");
				}
			} catch (Exception exe) {
			}
			userid = par.getParameter("username", null);
			password = par.getParameter("password", null);
			try {
				String jaasRealmTmp = par.getParameter("jaasRealm", null);
				if (jaasRealmTmp != null && !jaasRealmTmp.equalsIgnoreCase("")) {
					jaasRealm = jaasRealmTmp;
				}
			} catch (Exception se) {
			}
			try {
				String toUpper = par.getParameter("toUpperCase", null);
				if (toUpper != null && !toUpper.equalsIgnoreCase("true")) {
					userid = userid.toUpperCase();
				}
			} catch (Exception se) {
			}
			if (log.isDebugEnabled()) log.debug("trying to login as " + userid + " on the webpage");
		} catch (Exception ex) {
			new ProcessingException(ex.getMessage());
		}
		if (log.isDebugEnabled()) log.debug("end setup");
	}

	public void generate() throws SAXException, ProcessingException {
		if (log.isDebugEnabled()) log.debug("begin generate");
		contentHandler.startDocument();
		Document doc = XercesHelper.getNewDocument();
		Element root = doc.createElement("authentication");
		doc.appendChild(root);
		try {
			LoginContext lc = new LoginContext(jaasRealm, new InternalCallbackHandler());
			lc.login();
			Subject s = lc.getSubject();
			if (log.isDebugEnabled()) log.debug("Subject is: " + s.getPrincipals().toString());
			Element idElement = doc.createElement("ID");
			root.appendChild(idElement);

			Iterator it = s.getPrincipals(java.security.Principal.class).iterator();
			while (it.hasNext()) {
				Principal prp = (Principal) it.next();
				if (prp.getName().equalsIgnoreCase("Roles")) {
					Element roles = doc.createElement("roles");
					root.appendChild(roles);
					Group grp = (Group) prp;
					Enumeration member = grp.members();
					while (member.hasMoreElements()) {
						Principal sg = (Principal) member.nextElement();
						Element role = doc.createElement("role");
						roles.appendChild(role);
						Text txt = doc.createTextNode(sg.getName());
						role.appendChild(txt);
					}
				} else {
					Node nde = doc.createTextNode(prp.getName());
					idElement.appendChild(nde);
				}
			}
			lc.logout();
		} catch (Exception exe) {
			log.warn("Could not login user \"" + userid + "\"");
		} finally {
			try {
				DOMStreamer ds = new DOMStreamer(contentHandler);
				ds.stream(doc.getDocumentElement());
				contentHandler.endDocument();
			} catch (Exception exe) {
				log.error("Error streaming to dom", exe);
			}
			if (log.isDebugEnabled()) log.debug("end generate");
		}
	}

	/**
	 * 
	 */
	private class InternalCallbackHandler implements CallbackHandler {
		public void handle(Callback[] callbacks) throws UnsupportedCallbackException {

			for (int i = 0; i < callbacks.length; i++) {
				if (callbacks[i] instanceof NameCallback) {
					// prompt the user for a username
					NameCallback nc = (NameCallback) callbacks[i];
					nc.setName(userid);
				} else if (callbacks[i] instanceof PasswordCallback) {
					PasswordCallback pc = (PasswordCallback) callbacks[i];
					pc.setPassword(password.toCharArray());
				}
			}
		}
	}

}