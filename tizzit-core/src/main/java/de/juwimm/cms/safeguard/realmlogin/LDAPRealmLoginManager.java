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
import java.util.Properties;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.log4j.Logger;

import de.juwimm.cms.safeguard.model.RealmLdapHbm;
import de.juwimm.cms.safeguard.model.RealmLdapHbmDao;

/**
 * @author <a href="mailto:michael.meyer@juwimm.com">Michael Meyer</a>
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public final class LDAPRealmLoginManager implements SafeguardLoginManager {
	private static Logger log = Logger.getLogger(LDAPRealmLoginManager.class);
	private final String username;
	private final String password;
	private final String roleNeeded;
	private final Integer realmId;
	private final RealmLdapHbmDao realmLdapHbmDao;

	public LDAPRealmLoginManager(RealmLdapHbmDao realmLdapHbmDao, Integer realmId, String userName, String password, String roleNeeded) {
		this.username = userName;
		this.password = password;
		this.realmId = realmId;
		this.roleNeeded = roleNeeded;
		this.realmLdapHbmDao = realmLdapHbmDao;
	}

	public byte login() {
		byte login = SafeguardLoginManager.LOGIN_UNAUTHENTICATED;
		try {
			RealmLdapHbm realm = realmLdapHbmDao.load(this.realmId);

			String secPrinc = realm.getLdapPrefix() + this.username + realm.getLdapSuffix();
			String ldapUrl = realm.getLdapUrl();
			String authType = realm.getLdapAuthenticationType();

			Properties env = new Properties();
			env.put(DirContext.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(DirContext.PROVIDER_URL, ldapUrl);
			env.put(DirContext.SECURITY_AUTHENTICATION, authType);
			env.put(DirContext.SECURITY_PRINCIPAL, secPrinc);
			env.put(DirContext.SECURITY_CREDENTIALS, this.password);

			DirContext dc = new InitialDirContext(env);
			if (this.roleNeeded != null && this.roleNeeded.length() > 0) {
				login = SafeguardLoginManager.LOGIN_UNAUTHORIZED;
			}
			login = SafeguardLoginManager.LOGIN_SUCCESSFULLY;
			dc.close();
		} catch (NamingException nex) {
			if (log.isDebugEnabled()) log.debug("Naming Exception: " + nex.getMessage(), nex);
		} catch (Exception ex) {
			log.error("Error loging in user " + this.username + " on LdapRealm " + this.realmId + ": " + ex.getMessage(), ex);
		}
		if (log.isDebugEnabled()) log.debug("user \"" + this.username + "\" at realm " + this.realmId + " is logedin: " + login);
		return login;
	}

	public Collection<String> getRoles() {
		return new ArrayList<String>(0);
	}

}
