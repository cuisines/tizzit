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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.StringTokenizer;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import de.juwimm.cms.safeguard.model.RealmSimplePwHbmDao;
import de.juwimm.cms.safeguard.model.RealmSimplePwUserHbm;
import de.juwimm.cms.safeguard.model.RealmSimplePwUserHbmDao;

/**
 * @author <a href="mailto:michael.meyer@juwimm.com">Michael Meyer</a>
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public final class SimplePwRealmLoginManager implements SafeguardLoginManager {
	private static Logger log = Logger.getLogger(SimplePwRealmLoginManager.class);
	private String username;
	private String password;
	private String roleNeeded;
	private Integer realmId;
	private RealmSimplePwUserHbmDao realmSimplePwUserHbmDao;
	
	public SimplePwRealmLoginManager(RealmSimplePwUserHbmDao realmSimplePwUserHbmDao, Integer realmId, String userName, String password, String roleNeeded) {
		this.password = password;
		this.username = userName;
		this.realmId = realmId;
		this.roleNeeded = roleNeeded;
		this.realmSimplePwUserHbmDao = realmSimplePwUserHbmDao;
	}

	private RealmSimplePwUserHbm getUser() {
		RealmSimplePwUserHbm user = null;
		if (log.isDebugEnabled()) log.debug("searching user \"" + this.username + "\" at realm " + this.realmId);
		try {
			user = realmSimplePwUserHbmDao.findByUsernamePasswordRealmId(this.username, this.password, this.realmId);
			if (log.isDebugEnabled()) log.debug("user does exist");
		} catch (Exception ex) {
			log.error("Safeguard: " + ex.getMessage(), ex);
		}
		return user;
	}

	public byte login() {
		byte login = SafeguardLoginManager.LOGIN_UNAUTHENTICATED;
		if (log.isDebugEnabled()) log.debug("searching user \"" + this.username + "\" at realm " + this.realmId);
		try {
			RealmSimplePwUserHbm user = this.getUser();
			if (user != null) {
				if (log.isDebugEnabled()) log.debug("user does exist");
				if (this.roleNeeded != null && this.roleNeeded.length() > 0) {
					login = SafeguardLoginManager.LOGIN_UNAUTHORIZED;
					// check required role for this realm
					if (log.isDebugEnabled()) log.debug("role \"" + this.roleNeeded + "\" is required");
					if (this.getRoles4User(user).contains(this.roleNeeded.trim().toLowerCase())) login = SafeguardLoginManager.LOGIN_SUCCESSFULLY;
				} else {
					if (log.isDebugEnabled()) log.debug("no roles required");
					login = SafeguardLoginManager.LOGIN_SUCCESSFULLY;
				}
				if (log.isDebugEnabled()) log.debug("user \"" + this.username + "\" at realm " + this.realmId + " is logedin: " + login);
			}
		} catch (Exception ex) {
			log.error("Safeguard: " + ex.getMessage());
		}
		return login;
	}

	private HashSet<String> getRoles4User(RealmSimplePwUserHbm user) {
		HashSet<String> rolesSet = new HashSet<String>();
		try {
			String roles = user.getRoles();
			if (roles != null && roles.length() > 0) {
				StringTokenizer st = new StringTokenizer(roles, ",");
				while (st.hasMoreTokens()) {
					String role = st.nextToken();
					if (role.length() > 0) rolesSet.add(role.trim().toLowerCase());
				}
			}
		} catch (Exception e) {
			log.error("Error converting roles to HashSet<String>: " + e.getMessage(), e);
		}
		return rolesSet;
	}

	public Collection<String> getRoles() {
		try {
			RealmSimplePwUserHbm user = this.getUser();
			if (user != null) { return this.getRoles4User(user); }
		} catch (Exception e) {
			log.error("Error getting roles: " + e.getMessage(), e);
		}
		return new ArrayList<String>(0);
	}

}
