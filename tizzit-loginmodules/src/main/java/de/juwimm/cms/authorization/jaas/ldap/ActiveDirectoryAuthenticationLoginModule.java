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
package de.juwimm.cms.authorization.jaas.ldap;

import java.security.acl.Group;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.log4j.Logger;

import de.juwimm.cms.authorization.jaas.DatabaseAuthorization;

/**
 * This <code>LoginModule</code> wraps two LoginModules, a <code>LdapLoginModule</code> and a <code>DatabaseServerLoginModule</code>.<br/>
 * For authentication it delegates to one LoginModule and if the login fails, it delegates to the other one.<br/>
 * If the login succeeds the roles for this user are fetched the usual way from the ConQuest-database.
 * <p>
 * This LoginModule needs a combination of options of both LdapLoginModule and DatabaseServerLoginModule.<br/>
 * <h5>Example:</h5>
 * <pre>
 * &lt;!-- ConQuest --&gt;
 * &lt;application-policy name="juwimm-cms-security-domain"&gt;
 *	 &lt;authentication&gt;
 *		 &lt;!-- EJB J2EE Security --&gt;
 *		 &lt;login-module code="org.jboss.security.ClientLoginModule" flag="required"/&gt;
 *		 &lt;!-- Authentification (Check Username / Password) --&gt;
 *		 &lt;login-module code="de.juwimm.cms.authorization.jaas.ldap.ActiveDirectoryAuthenticationLoginModule" flag="required"&gt;
 *			 &lt;module-option name="java.naming.factory.initial"&gt;com.sun.jndi.ldap.LdapCtxFactory&lt;/module-option&gt;
 *			 &lt;&lt;module-option name="java.naming.provider.url"&gt;ldap://bsnads.ADSTEST.bsn:389&lt;/module-option&gt;
 *			 &lt;module-option name="java.naming.security.authentication"&gt;simple&lt;/module-option&gt;
 *			 &lt;module-option name="principalDNPrefix"&gt;CN=&lt;/module-option&gt;
 *			 &lt;module-option name="uidAttributeID"&gt;sAMAccountName&lt;/module-option&gt;
 *			 &lt;module-option name="principalDNSuffix"&gt;,OU=JuwiMM,DC=adstest,DC=bsn&lt;/module-option&gt;
 *			 &lt;module-option name="dsJndiName"&gt;java:/ConQuestDS&lt;/module-option&gt;
 *			 &lt;module-option name="password-stacking"&gt;useFirstPass&lt;/module-option&gt;
 *			 &lt;module-option name="principalsQuery"&gt;SELECT passwd FROM usr WHERE user_id = ?&lt;/module-option&gt;
 *			 &lt;module-option name="rolesQuery"&gt;SELECT user_id, 'Roles' FROM usr WHERE user_id = '' AND user_id = ?&lt;/module-option&gt;
 *			 &lt;module-option name="hashAlgorithm"&gt;SHA-1&lt;/module-option&gt;
 *			 &lt;module-option name="hashEncoding"&gt;base64&lt;/module-option&gt;
 *			 &lt;module-option name="unauthenticatedIdentity"&gt;nobody&lt;/module-option&gt;
 *		 &lt;/login-module&gt;
 *	 &lt;/authentication&gt;
 * &lt;/application-policy&gt;
 * </pre>
 * </p>
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 * @since 2.4.14
 */
public class ActiveDirectoryAuthenticationLoginModule implements LoginModule {
	private static final Logger log = Logger.getLogger(ActiveDirectoryAuthenticationLoginModule.class);
	private LdapLoginModule ldapLoginModule = new AdLdapLoginModule();
	private DatabaseServerLoginModule databaseLoginModule = new AdDatabaseServerLoginModule();
	/** The JNDI name of the DataSource to use */
	protected String dsJndiName;
	private UsernamePasswordLoginModule lastSuccessfullyModule = null;

	public ActiveDirectoryAuthenticationLoginModule() {
	}

	/**
	 * Fetch additional options
	 * @see javax.security.auth.spi.LoginModule#initialize(javax.security.auth.Subject, javax.security.auth.callback.CallbackHandler, java.util.Map, java.util.Map)
	 */
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ? > sharedState, Map<String, ? > options) {
		this.dsJndiName = (String) options.get("dsJndiName");
		if (this.dsJndiName == null) this.dsJndiName = "java:/ConQuestDS";
		this.databaseLoginModule.initialize(subject, callbackHandler, sharedState, options);
		Map newOptions = new HashMap(options);
		if (newOptions.containsKey("hashAlgorithm")) {
			// remove hashing to authenticate at Active Directory with plain text password
			newOptions.remove("hashAlgorithm");
		}
		this.ldapLoginModule.initialize(subject, callbackHandler, sharedState, newOptions);
	}

	/**
	 * Fetch roles from ConQuest-database
	 */
	protected Group[] getRoleSets() throws LoginException {
		try {
			return DatabaseAuthorization.getRoleSets(this.dsJndiName, this.lastSuccessfullyModule.getIdentity().getName());
		} catch (Exception e) {
			log.error("Error fetching roles: " + e.getMessage(), e);
			LoginException le = new LoginException("Error fetching roles: " + e.getMessage());
			throw le;
		}
	}

	/**
	 * @see javax.security.auth.spi.LoginModule#login()
	 */
	public boolean login() throws LoginException {
		boolean loginSuccessfully = false;
		if (this.lastSuccessfullyModule == null) this.lastSuccessfullyModule = this.ldapLoginModule;
		try {
			loginSuccessfully = this.lastSuccessfullyModule.login();
		} catch (LoginException le) {
			if (log.isDebugEnabled()) log.debug("Login via " + this.lastSuccessfullyModule.getClass().getName() + " failed: " + le.getMessage());
		}
		if (!loginSuccessfully) {
			this.switchLoginModule();
			try {
				loginSuccessfully = this.lastSuccessfullyModule.login();
			} catch (LoginException le) {
				if (log.isDebugEnabled()) log.debug("Login via " + this.lastSuccessfullyModule.getClass().getName() + " failed: " + le.getMessage());
			}
		}
		return loginSuccessfully;
	}

	private void switchLoginModule() {
		if (this.lastSuccessfullyModule == null) {
			this.lastSuccessfullyModule = this.ldapLoginModule;
		} else if (this.lastSuccessfullyModule instanceof AdLdapLoginModule) {
			this.lastSuccessfullyModule = this.databaseLoginModule;
		} else {
			this.lastSuccessfullyModule = this.ldapLoginModule;
		}
	}

	/**
	 * @see javax.security.auth.spi.LoginModule#commit()
	 */
	public boolean commit() throws LoginException {
		if (this.lastSuccessfullyModule == null) this.lastSuccessfullyModule = this.ldapLoginModule;
		return this.lastSuccessfullyModule.commit();
	}

	/**
	 * @see javax.security.auth.spi.LoginModule#abort()
	 */
	public boolean abort() throws LoginException {
		if (this.lastSuccessfullyModule == null) this.lastSuccessfullyModule = this.ldapLoginModule;
		return this.lastSuccessfullyModule.abort();
	}

	/**
	 * @see javax.security.auth.spi.LoginModule#logout()
	 */
	public boolean logout() throws LoginException {
		if (this.lastSuccessfullyModule == null) this.lastSuccessfullyModule = this.ldapLoginModule;
		return this.lastSuccessfullyModule.logout();
	}

	/**
	 * 
	 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
	 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
	 * @version $Id$
	 */
	public class AdLdapLoginModule extends LdapLoginModule {
		
		public AdLdapLoginModule() {
			super();
		}

		/**
		 * @see de.juwimm.cms.authorization.jaas.ldap.LdapLoginModule#getRoleSets()
		 */
		@Override
		protected Group[] getRoleSets() throws LoginException {
			try {
				return ActiveDirectoryAuthenticationLoginModule.this.getRoleSets();
			} catch (Exception e) {
				LoginException le = new LoginException(e.getMessage());
				throw le;
			}
		}

	}

	/**
	 * 
	 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
	 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
	 * @version $Id$
	 */
	public class AdDatabaseServerLoginModule extends DatabaseServerLoginModule {
		
		public AdDatabaseServerLoginModule() {
			super();
		}

		/**
		 * @see de.juwimm.cms.authorization.jaas.ldap.AbstractServerLoginModule#getRoleSets()
		 */
		@Override
		protected Group[] getRoleSets() throws LoginException {
			try {
				return ActiveDirectoryAuthenticationLoginModule.this.getRoleSets();
			} catch (Exception e) {
				LoginException le = new LoginException(e.getMessage());
				throw le;
			}
		}

	}

}
