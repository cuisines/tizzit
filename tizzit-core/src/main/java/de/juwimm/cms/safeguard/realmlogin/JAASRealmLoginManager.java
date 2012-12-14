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
package de.juwimm.cms.safeguard.realmlogin;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;

import de.juwimm.cms.authorization.SimpleCallbackHandler;
import de.juwimm.cms.safeguard.model.RealmJaasHbm;
import de.juwimm.cms.safeguard.model.RealmJaasHbmDao;

/**
 * <strong>only works properly in JBoss!!!</strong>
 * @author <a href="mailto:michael.meyer@juwimm.com">Michael Meyer</a>
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class JAASRealmLoginManager implements SafeguardLoginManager {
	private static Logger log = Logger.getLogger(JAASRealmLoginManager.class);
	private final String userName;
	private final String password;
	private final String roleNeeded;
	private final Integer realmId;
	private final RealmJaasHbmDao realmJaasHbmDao;

	public JAASRealmLoginManager(RealmJaasHbmDao realmJaasHbmDao, Integer realmId, String userName, String password, String roleNeeded) {
		this.password = password;
		this.realmId = realmId;
		this.userName = userName;
		this.roleNeeded = roleNeeded;
		this.realmJaasHbmDao = realmJaasHbmDao;
	}

	@SuppressWarnings("unchecked")
	public byte login() {
		byte login = SafeguardLoginManager.LOGIN_UNAUTHENTICATED;
		try {
			RealmJaasHbm realm = realmJaasHbmDao.load(this.realmId);
			LoginContext lc = new LoginContext(realm.getJaasPolicyName(), new SimpleCallbackHandler(this.userName, this.password));
			lc.login();
			if (this.roleNeeded != null && this.roleNeeded.length() > 0) {
				login = SafeguardLoginManager.LOGIN_UNAUTHORIZED;
				// check required role for this realm
				Principal requiredRole = new PrincipalImpl(this.roleNeeded);
				Subject user = lc.getSubject();
				if (log.isDebugEnabled()) log.debug(user.getClass().getName() + ": " + user);
				Set<Principal> principalSet = user.getPrincipals();
				Iterator<Principal> it = principalSet.iterator();
				while (login < SafeguardLoginManager.LOGIN_SUCCESSFULLY && it.hasNext()) {
					Principal principal = it.next();
					if (log.isDebugEnabled()) log.debug(principal.getClass().getName() + ": " + principal.getName() + ": " + principal);
					if (principal instanceof Group) {
						Group group = (Group) principal;
						if (group.getName().equalsIgnoreCase("roles")) {
							Enumeration e=group.members();
							while (e.hasMoreElements()) {
								Principal p = (Principal) e.nextElement();
								if(p.getName().equalsIgnoreCase(requiredRole.getName())){
									login = SafeguardLoginManager.LOGIN_SUCCESSFULLY;
								}
							}
							if (group.isMember(requiredRole)) login = SafeguardLoginManager.LOGIN_SUCCESSFULLY;
						}
						if (log.isDebugEnabled()) {
							Enumeration members = group.members();
							while (members.hasMoreElements()) {
								Principal member = (Principal) members.nextElement();
								if (log.isDebugEnabled()) log.debug(member.getClass().getName() + ": " + member.getName() + ": " + member);
							}
						}
					}
				}
			} else {
				login = SafeguardLoginManager.LOGIN_SUCCESSFULLY;
			}
			if (log.isDebugEnabled()) log.debug("user \"" + this.userName + "\" at realm " + this.realmId + " is logedin: " + login);
			lc.logout();
		} catch (LoginException e) {
			log.error("Error loging in user " + this.userName + " on JaasRealm " + this.realmId + ": " + e.getMessage(), e);
		}
		return login;
	}

	public Collection<String> getRoles() {
		HashSet<String> rolesSet = new HashSet<String>();
		try {
			RealmJaasHbm realm = realmJaasHbmDao.load(this.realmId);
			LoginContext lc = new LoginContext(realm.getJaasPolicyName(), new SimpleCallbackHandler(this.userName, this.password));
			lc.login();
			Subject user = lc.getSubject();
			Set<Principal> principalSet = user.getPrincipals();
			Iterator<Principal> it = principalSet.iterator();
			while (it.hasNext()) {
				Principal principal = it.next();
				if (principal instanceof Group) {
					Group group = (Group) principal;
					if (group.getName().equalsIgnoreCase("roles")) {
						Enumeration members = group.members();
						while (members.hasMoreElements()) {
							Principal member = (Principal) members.nextElement();
							rolesSet.add(member.getName());
						}
					}
				}
			}
			lc.logout();
		} catch (LoginException e) {
			log.error("Error getting roles: " + e.getMessage(), e);
		}
		return rolesSet;
	}

}

class PrincipalImpl implements Principal {
	private final String name;

	public PrincipalImpl(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
