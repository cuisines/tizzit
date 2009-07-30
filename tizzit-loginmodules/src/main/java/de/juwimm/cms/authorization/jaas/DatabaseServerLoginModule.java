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
package de.juwimm.cms.authorization.jaas;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.log4j.Logger;

/**
 * <p>Title: ConQuest</p>
 * <p>Description: Enterprise Content Management</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: JuwiMacMillan Group</p>
 * @author <a href="s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * , Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class DatabaseServerLoginModule implements LoginModule {
	private static final Logger log = Logger.getLogger(DatabaseServerLoginModule.class);
	protected Subject subject;
	protected CallbackHandler callbackHandler; 
	protected Principal identity;
	private String dsJndiName;

	/**
	 * Initialize the login module. This stores the subject, callbackHandler
	 * and sharedState and options for the login session. Subclasses should override
	 * if they need to process their own options. A call to super.initialize(...)
	 * must be made in the case of an override.
	 * 
	 * @param subject the Subject to update after a successful login.
	 * @param callbackHandler the CallbackHandler that will be used to obtain the
	 *    the user identity and credentials.
	 * @param sharedState a Map shared between all configured login module instances
	 * @param options the parameters passed to the login module.
	 */
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
		if (log.isTraceEnabled()) {
			log.trace("initialize");
		}
		this.subject = subject;
		this.callbackHandler = callbackHandler; 
		Object username = sharedState.get("javax.security.auth.login.name");
		if (username instanceof Principal) {
			if (log.isTraceEnabled()) {
				log.trace("found principal object");
			}
			this.identity = (Principal) username;
		} else {
			if (log.isTraceEnabled()) {
				log.trace("creating principal object");
			}
			String name = username.toString();
			this.identity = new SimplePrincipal(name);
		}
		
		this.dsJndiName = (String) options.get("dsJndiName");
		if (this.dsJndiName == null) {
			this.dsJndiName = "java:/ConQuestDS";
		}
		if (log.isTraceEnabled()) {
			log.trace("login " + this.identity.getName());
			log.trace("initialize, instance=@" + System.identityHashCode(this));
		}
	}

	/** Perform the authentication of the username and password.
	 */
	public boolean login() throws LoginException {
		if (log.isTraceEnabled()) {
			log.trace("login");
		}
		return true;
	}

	/** Method to commit the authentication process (phase 2). If the login
	 method completed successfully as indicated by loginOk == true, this
	 method adds the getIdentity() value to the subject getPrincipals() Set.
	 It also adds the members of each Group returned by getRoleSets()
	 to the subject getPrincipals() Set.
	 @see javax.security.auth.Subject
	 @see java.security.acl.Group
	 @return true always.
	 */
	public boolean commit() throws LoginException {
		if (log.isTraceEnabled()) {
			log.trace("commit");
		}
		Set principals = this.subject.getPrincipals();
		principals.add(this.identity);
		Group[] roleSets = this.getRoleSets();
		for (int g = 0; g < roleSets.length; g++) {
			Group group = roleSets[g];
			String name = group.getName();
			Group subjectGroup = this.createGroup(name, principals);
			Enumeration members = group.members();
			while (members.hasMoreElements()) {
				Principal role = (Principal) members.nextElement();
				try {
					subjectGroup.addMember(role);
				} catch (IllegalArgumentException e) {
					SimpleGroup tmp = new SimpleGroup("Roles");
					subjectGroup.addMember(tmp);
					subjectGroup = tmp;
					subjectGroup.addMember(role);
				}
			}
		}
		return true;
	}

	private Group[] getRoleSets() {
		return DatabaseAuthorization.getRoleSets(this.dsJndiName, this.identity.getName());
	}

	/** Method to abort the authentication process (phase 2).
	 @return true alaways
	 */
	public boolean abort() throws LoginException {
		if (log.isTraceEnabled()) {
			log.trace("abort");
		}
		return true;
	}

	/** Remove the user identity and roles added to the Subject during commit.
	 @return true always.
	 */
	public boolean logout() throws LoginException {
		if (log.isTraceEnabled()) log.trace("logout");
		// Remove the user identity
		Set principals = this.subject.getPrincipals();
		principals.remove(this.identity);
		// Remove any added Groups...
		return true;
	}

	/** Find or create a Group with the given name. Subclasses should use this
	 method to locate the 'Roles' group or create additional types of groups.
	 @return A named Group from the principals set.
	 */
	private Group createGroup(String name, Set principals) {
		Group roles = null;
		Iterator iter = principals.iterator();
		while (iter.hasNext()) {
			Object next = iter.next();
			if (!(next instanceof Group)) {
				continue;
			}
			Group grp = (Group) next;
			if (grp.getName().equals(name)) {
				roles = grp;
				break;
			}
		}
		// If we did not find a group create one
		if (roles == null) {
			roles = new SimpleGroup(name);
			principals.add(roles);
		}
		return roles;
	}

}
