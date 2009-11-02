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

import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.Composable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.AbstractGenerator;
import org.apache.cocoon.webapps.session.ContextManager;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * <p>Title: ConQuest</p>
 * <p>Description: Enterprise Content Management</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PortalJaasSecurityGenerator extends AbstractGenerator implements Composable {
	private static Logger log = Logger.getLogger(PortalJaasSecurityGenerator.class);
	private String userid = "";
	private String password = "";
	private String jaasRealm = "juwimm-cms-security-domain";
	private ComponentManager manager = null;

	public void compose(ComponentManager manager) {
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
			if (log.isDebugEnabled()) log.debug("trying to login as " + userid + " on the webpage");
		} catch (Exception ex) {
			new ProcessingException(ex.getMessage());
		}
		if (log.isDebugEnabled()) log.debug("end setup");
	}

	public void addTextNode(String nodeName, String text) throws SAXException {
		contentHandler.startElement("", nodeName, nodeName, new AttributesImpl());
		contentHandler.characters(text.toCharArray(), 0, text.length());
		contentHandler.endElement("", nodeName, nodeName);
	}

	public void generate() throws SAXException, ProcessingException {
		if (log.isDebugEnabled()) log.debug("begin generate");
		contentHandler.startElement("", "authentication", "authentication", new AttributesImpl());

		try {
			LoginContext lc = new LoginContext(jaasRealm, new InternalCallbackHandler());
			lc.login();
			Subject s = lc.getSubject();
			if (log.isDebugEnabled()) log.debug("Subject is: " + s.getPrincipals().toString());
			String principal = "";
			ArrayList<String> roles = new ArrayList<String>();

			Iterator it = s.getPrincipals(java.security.Principal.class).iterator();
			while (it.hasNext()) {
				Principal prp = (Principal) it.next();
				if (prp.getName().equalsIgnoreCase("Roles")) {
					Group grp = (Group) prp;
					Enumeration enume = grp.members();
					while (enume.hasMoreElements()) {
						Principal sg = (Principal) enume.nextElement();
						roles.add(sg.getName());
					}
				} else {
					principal = prp.getName();
				}
			}
			lc.logout();

			addTextNode("ID", principal);
			it = roles.iterator();
			while (it.hasNext()) {
				String role = (String) it.next();
				addTextNode("role", role);
			}
			contentHandler.startElement("", "data", "data", new AttributesImpl());
			addTextNode("user", principal);
			contentHandler.endElement("", "data", "data");
		} catch (Exception exe) {
			log.warn("Could not login user \"" + userid + "\"");
		} finally {
			contentHandler.endElement("", "authentication", "authentication");
			if (log.isDebugEnabled()) log.debug("end generate");
		}
	}

	/**
	 * Callback Handler
	 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
	 * @version $Id$
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
