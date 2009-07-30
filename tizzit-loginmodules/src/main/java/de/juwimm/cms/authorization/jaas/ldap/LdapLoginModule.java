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
import java.security.Principal;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.InitialLdapContext;
import javax.security.auth.login.LoginException;
import javax.management.ObjectName;

import org.jboss.security.SimpleGroup;

/**
 * An implementation of LoginModule that authenticates against an LDAP server
 * using JNDI, based on the configuration properties.
 * <p>
 * The LoginModule options include whatever options your LDAP JNDI provider
 * supports. Examples of standard property names are:
 * <ul>
 * <li><code>Context.INITIAL_CONTEXT_FACTORY = "java.naming.factory.initial"</code>
 * <li><code>Context.SECURITY_PROTOCOL = "java.naming.security.protocol"</code>
 * <li><code>Context.PROVIDER_URL = "java.naming.provider.url"</code>
 * <li><code>Context.SECURITY_AUTHENTICATION = "java.naming.security.authentication"</code>
 * </ul>
 * <p>
 * The Context.SECURITY_PRINCIPAL is set to the distinguished name of the user
 * as obtained by the callback handler and the Context.SECURITY_CREDENTIALS
 * property is either set to the String password or Object credential depending
 * on the useObjectCredential option.
 * <p>
 * Additional module properties include:
 * <ul>
 * <li>principalDNPrefix, principalDNSuffix : A prefix and suffix to add to the
 * username when forming the user distiguished name. This is useful if you
 * prompt a user for a username and you don't want them to have to enter the
 * fully distinguished name. Using this property and principalDNSuffix the
 * userDN will be formed as:
 * <pre>
 *    String userDN = principalDNPrefix + username + principalDNSuffix;
 * </pre>
 * <li>useObjectCredential : indicates that the credential should be obtained as
 * an opaque Object using the <code>org.jboss.security.plugins.ObjectCallback</code> type
 * of Callback rather than as a char[] password using a JAAS PasswordCallback.
 * <li>rolesCtxDN : The fixed distinguished name to the context to search for user roles.
 * <li>userRolesCtxDNAttributeName : The name of an attribute in the user
 * object that contains the distinguished name to the context to search for
 * user roles. This differs from rolesCtxDN in that the context to search for a
 * user's roles can be unique for each user.
 * <li>uidAttributeID : The name of the attribute that in the object containing
 * the user roles that corresponds to the userid. This is used to locate the
 * user roles.
 * <li>matchOnUserDN : A flag indicating if the search for user roles should match
 * on the user's fully distinguished name. If false just the username is used
 * as the match value. If true, the userDN is used as the match value.
 * <li>allowEmptyPasswords : A flag indicating if empty(length==0) passwords
 * should be passed to the ldap server. An empty password is treated as an
 * anonymous login by some ldap servers and this may not be a desirable
 * feature. Set this to false to reject empty passwords, true to have the ldap
 * server validate the empty password. The default is true.
 *
 * <li>roleAttributeIsDN : A flag indicating whether the user's role attribute
 * contains the fully distinguished name of a role object, or the users's role
 * attribute contains the role name. If false, the role name is taken from the
 * value of the user's role attribute. If true, the role attribute represents
 * the distinguished name of a role object.  The role name is taken from the
 * value of the `roleNameAttributeId` attribute of the corresponding object.  In
 * certain directory schemas (e.g., Microsoft Active Directory), role (group)
 * attributes in the user object are stored as DNs to role objects instead of
 * as simple names, in which case, this property should be set to true.
 * The default value of this property is false.
 * <li>roleNameAttributeID : The name of the attribute of the role object which
 * corresponds to the name of the role.  If the `roleAttributeIsDN` property is
 * set to true, this property is used to find the role object's name attribute.
 * If the `roleAttributeIsDN` property is set to false, this property is ignored.
 * <li>java.naming.security.principal (4.0.3+): This standard JNDI property if
 * specified in the login configuration, it is used to rebind to the ldap server
 * after user authentication for the role searches. This may be necessar if the
 * user does not have permission to perform these queres. If specified, the
 * java.naming.security.credentials provides the rebind credentials.
 * </li>
 * <li>java.naming.security.credentials (4.0.3+): This standard JNDI property
 * if specified in the login configuration, it is used to rebind to the ldap
 * server after user authentication for the role searches along with the
 * java.naming.security.principal value. This can be encrypted using the
 * jaasSecurityDomain.
 * <li>jaasSecurityDomain (4.0.3+): The JMX ObjectName of the JaasSecurityDomain
 * to use to decrypt the java.naming.security.principal. The encrypted form
 * of the password is that returned by the JaasSecurityDomain#encrypt64(byte[])
 * method. The org.jboss.security.plugins.PBEUtils can also be used to generate
 * the encrypted form.
 * </ul>
 * A sample login config:
 * <p>
 <pre>
 testLdap {
 org.jboss.security.auth.spi.LdapLoginModule required
 java.naming.factory.initial=com.sun.jndi.ldap.LdapCtxFactory
 java.naming.provider.url="ldap://ldaphost.jboss.org:1389/"
 java.naming.security.authentication=simple
 principalDNPrefix=uid=
 uidAttributeID=userid
 roleAttributeID=roleName
 principalDNSuffix=,ou=People,o=jboss.org
 rolesCtxDN=cn=JBossSX Tests,ou=Roles,o=jboss.org
 };

 testLdap2 {
 org.jboss.security.auth.spi.LdapLoginModule required
 java.naming.factory.initial=com.sun.jndi.ldap.LdapCtxFactory
 java.naming.provider.url="ldap://ldaphost.jboss.org:1389/"
 java.naming.security.authentication=simple
 principalDNPrefix=uid=
 uidAttributeID=userid
 roleAttributeID=roleName
 principalDNSuffix=,ou=People,o=jboss.org
 userRolesCtxDNAttributeName=ou=Roles,dc=user1,dc=com
 };

 testLdapToActiveDirectory {
 org.jboss.security.auth.spi.LdapLoginModule required
 java.naming.factory.initial=com.sun.jndi.ldap.LdapCtxFactory
 java.naming.provider.url="ldap://ldaphost.jboss.org:1389/"
 java.naming.security.authentication=simple
 rolesCtxDN=cn=Users,dc=ldaphost,dc=jboss,dc=org
 uidAttributeID=userPrincipalName
 roleAttributeID=memberOf
 roleAttributeIsDN=true
 roleNameAttributeID=name
 };
 </pre>
 *
 * @author Scott.Stark@jboss.org
 * @version $Revision: 57203 $
 */
public class LdapLoginModule extends UsernamePasswordLoginModule {
	private static final String PRINCIPAL_DN_PREFIX_OPT = "principalDNPrefix";
	private static final String PRINCIPAL_DN_SUFFIX_OPT = "principalDNSuffix";
	private static final String ROLES_CTX_DN_OPT = "rolesCtxDN";
	private static final String USER_ROLES_CTX_DN_ATTRIBUTE_ID_OPT = "userRolesCtxDNAttributeName";
	private static final String UID_ATTRIBUTE_ID_OPT = "uidAttributeID";
	private static final String ROLE_ATTRIBUTE_ID_OPT = "roleAttributeID";
	private static final String MATCH_ON_USER_DN_OPT = "matchOnUserDN";
	private static final String ROLE_ATTRIBUTE_IS_DN_OPT = "roleAttributeIsDN";
	private static final String ROLE_NAME_ATTRIBUTE_ID_OPT = "roleNameAttributeID";
	private static final String SEARCH_TIME_LIMIT_OPT = "searchTimeLimit";
	private static final String SEARCH_SCOPE_OPT = "searchScope";
	private static final String SECURITY_DOMAIN_OPT = "jaasSecurityDomain";

	public LdapLoginModule() {
	}

	private transient SimpleGroup userRoles = new SimpleGroup("Roles");

	/** Overriden to return an empty password string as typically one cannot
	 obtain a user's password. We also override the validatePassword so
	 this is ok.
	 @return and empty password String
	 */
	@Override
	protected String getUsersPassword() throws LoginException {
		return "";
	}

	/** Overriden by subclasses to return the Groups that correspond to the
	 to the role sets assigned to the user. Subclasses should create at
	 least a Group named "Roles" that contains the roles assigned to the user.
	 A second common group is "CallerPrincipal" that provides the application
	 identity of the user rather than the security domain identity.
	 @return Group[] containing the sets of roles 
	 */
	@Override
	protected Group[] getRoleSets() throws LoginException {
		Group[] roleSets = {userRoles};
		return roleSets;
	}

	/** Validate the inputPassword by creating a ldap InitialContext with the
	 SECURITY_CREDENTIALS set to the password.

	 @param inputPassword the password to validate.
	 @param expectedPassword ignored
	 */
	@Override
	protected boolean validatePassword(String inputPassword, String expectedPassword) {
		boolean isValid = false;
		if (inputPassword != null) {
			// See if this is an empty password that should be disallowed
			if (inputPassword.length() == 0) {
				// Check for an allowEmptyPasswords option
				boolean allowEmptyPasswords = true;
				String flag = (String) options.get("allowEmptyPasswords");
				if (flag != null) allowEmptyPasswords = Boolean.valueOf(flag).booleanValue();
				if (allowEmptyPasswords == false) {
					super.log.trace("Rejecting empty password due to allowEmptyPasswords");
					return false;
				}
			}

			try {
				// Validate the password by trying to create an initial context
				String username = getUsername();
				createLdapInitContext(username, inputPassword);
				isValid = true;
			} catch (Throwable e) {
				super.setValidateError(e);
			}
		}
		return isValid;
	}

	private void createLdapInitContext(String username, Object credential) throws Exception {
		boolean trace = log.isTraceEnabled();
		Properties env = new Properties();
		// Map all option into the JNDI InitialLdapContext env
		Iterator iter = options.entrySet().iterator();
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			env.put(entry.getKey(), entry.getValue());
		}

		// Set defaults for key values if they are missing
		String factoryName = env.getProperty(Context.INITIAL_CONTEXT_FACTORY);
		if (factoryName == null) {
			factoryName = "com.sun.jndi.ldap.LdapCtxFactory";
			env.setProperty(Context.INITIAL_CONTEXT_FACTORY, factoryName);
		}
		String authType = env.getProperty(Context.SECURITY_AUTHENTICATION);
		if (authType == null) env.setProperty(Context.SECURITY_AUTHENTICATION, "simple");
		String protocol = env.getProperty(Context.SECURITY_PROTOCOL);
		String providerURL = (String) options.get(Context.PROVIDER_URL);
		if (providerURL == null) providerURL = "ldap://localhost:" + ((protocol != null && protocol.equals("ssl")) ? "636" : "389");

		String bindDN = (String) options.get(Context.SECURITY_PRINCIPAL);
		String bindCredential = (String) options.get(Context.SECURITY_CREDENTIALS);
		String securityDomain = (String) options.get(SECURITY_DOMAIN_OPT);
		if (securityDomain != null) {
			ObjectName serviceName = new ObjectName(securityDomain);
			char[] tmp = DecodeAction.decode(bindCredential, serviceName);
			bindCredential = new String(tmp);
		}

		String principalDNPrefix = (String) options.get(PRINCIPAL_DN_PREFIX_OPT);
		if (principalDNPrefix == null) principalDNPrefix = "";
		String principalDNSuffix = (String) options.get(PRINCIPAL_DN_SUFFIX_OPT);
		if (principalDNSuffix == null) principalDNSuffix = "";
		String matchType = (String) options.get(MATCH_ON_USER_DN_OPT);
		boolean matchOnUserDN = Boolean.valueOf(matchType).booleanValue();
		String userDN = principalDNPrefix + username + principalDNSuffix;
		env.setProperty(Context.PROVIDER_URL, providerURL);
		env.setProperty(Context.SECURITY_PRINCIPAL, userDN);
		env.put(Context.SECURITY_CREDENTIALS, credential);
		if (trace) {
			Properties tmp = new Properties();
			tmp.putAll(env);
			tmp.setProperty(Context.SECURITY_CREDENTIALS, "***");
			log.trace("Logging into LDAP server, env=" + tmp.toString());
		}
		InitialLdapContext ctx = new InitialLdapContext(env, null);
		if (trace) log.trace("Logged into LDAP server, " + ctx);

		if (bindDN != null) {
			// Rebind the ctx to the bind dn/credentials for the roles searches
			if (trace) log.trace("Rebind SECURITY_PRINCIPAL to: " + bindDN);
			env.setProperty(Context.SECURITY_PRINCIPAL, bindDN);
			env.put(Context.SECURITY_CREDENTIALS, bindCredential);
			ctx = new InitialLdapContext(env, null);
		}

		/* If a userRolesCtxDNAttributeName was speocified, see if there is a
		 user specific roles DN. If there is not, the default rolesCtxDN will
		 be used.
		 */
		String rolesCtxDN = (String) options.get(ROLES_CTX_DN_OPT);
		String userRolesCtxDNAttributeName = (String) options.get(USER_ROLES_CTX_DN_ATTRIBUTE_ID_OPT);
		if (userRolesCtxDNAttributeName != null) {
			// Query the indicated attribute for the roles ctx DN to use
			String[] returnAttribute = {userRolesCtxDNAttributeName};
			try {
				Attributes result = ctx.getAttributes(userDN, returnAttribute);
				if (result.get(userRolesCtxDNAttributeName) != null) {
					rolesCtxDN = result.get(userRolesCtxDNAttributeName).get().toString();
					super.log.trace("Found user roles context DN: " + rolesCtxDN);
				}
			} catch (NamingException e) {
				super.log.debug("Failed to query userRolesCtxDNAttributeName", e);
			}
		}

		// Search for any roles associated with the user
		if (rolesCtxDN != null) {
			String uidAttrName = (String) options.get(UID_ATTRIBUTE_ID_OPT);
			if (uidAttrName == null) uidAttrName = "uid";
			String roleAttrName = (String) options.get(ROLE_ATTRIBUTE_ID_OPT);
			if (roleAttrName == null) roleAttrName = "roles";
			StringBuffer roleFilter = new StringBuffer("(");
			roleFilter.append(uidAttrName);
			roleFilter.append("={0})");
			String userToMatch = username;
			if (matchOnUserDN == true) userToMatch = userDN;

			String[] roleAttr = {roleAttrName};
			// Is user's role attribute a DN or the role name
			String roleAttributeIsDNOption = (String) options.get(ROLE_ATTRIBUTE_IS_DN_OPT);
			boolean roleAttributeIsDN = Boolean.valueOf(roleAttributeIsDNOption).booleanValue();

			// If user's role attribute is a DN, what is the role's name attribute
			// Default to 'name' (Group name attribute in Active Directory)
			String roleNameAttributeID = (String) options.get(ROLE_NAME_ATTRIBUTE_ID_OPT);
			if (roleNameAttributeID == null) roleNameAttributeID = "name";

			int searchScope = SearchControls.SUBTREE_SCOPE;
			int searchTimeLimit = 10000;
			String timeLimit = (String) options.get(SEARCH_TIME_LIMIT_OPT);
			if (timeLimit != null) {
				try {
					searchTimeLimit = Integer.parseInt(timeLimit);
				} catch (NumberFormatException e) {
					log.trace("Failed to parse: " + timeLimit + ", using searchTimeLimit=" + searchTimeLimit);
				}
			}
			String scope = (String) options.get(SEARCH_SCOPE_OPT);
			if ("OBJECT_SCOPE".equalsIgnoreCase(scope))
				searchScope = SearchControls.OBJECT_SCOPE;
			else if ("ONELEVEL_SCOPE".equalsIgnoreCase(scope)) searchScope = SearchControls.ONELEVEL_SCOPE;
			if ("SUBTREE_SCOPE".equalsIgnoreCase(scope)) searchScope = SearchControls.SUBTREE_SCOPE;

			try {
				SearchControls controls = new SearchControls();
				controls.setSearchScope(searchScope);
				controls.setReturningAttributes(roleAttr);
				controls.setTimeLimit(searchTimeLimit);
				Object[] filterArgs = {userToMatch};
				if (trace) {
					log.trace("searching rolesCtxDN=" + rolesCtxDN + ", roleFilter=" + roleFilter + ", filterArgs=" + userToMatch + ", roleAttr=" + roleAttr + ", searchScope=" + searchScope + ", searchTimeLimit=" + searchTimeLimit);
				}
				NamingEnumeration answer = ctx.search(rolesCtxDN, roleFilter.toString(), filterArgs, controls);
				while (answer.hasMore()) {
					SearchResult sr = (SearchResult) answer.next();
					if (trace) {
						log.trace("Checking answer: " + sr.getName());
					}
					Attributes attrs = sr.getAttributes();
					Attribute roles = attrs.get(roleAttrName);
					for (int r = 0; r < roles.size(); r++) {
						Object value = roles.get(r);
						String roleName = null;
						if (roleAttributeIsDN == true) {
							// Query the roleDN location for the value of roleNameAttributeID
							String roleDN = value.toString();
							String[] returnAttribute = {roleNameAttributeID};
							if (trace) log.trace("Following roleDN: " + roleDN);
							try {
								Attributes result2 = ctx.getAttributes(roleDN, returnAttribute);
								Attribute roles2 = result2.get(roleNameAttributeID);
								if (roles2 != null) {
									for (int m = 0; m < roles2.size(); m++) {
										roleName = (String) roles2.get(m);
										addRole(roleName);
									}
								}
							} catch (NamingException e) {
								log.trace("Failed to query roleNameAttrName", e);
							}
						} else {
							// The role attribute value is the role name
							roleName = value.toString();
							addRole(roleName);
						}
					}
				}
				answer.close();
			} catch (NamingException e) {
				if (trace) log.trace("Failed to locate roles", e);
			}
		}
		// Close the context to release the connection
		ctx.close();
	}

	private void addRole(String roleName) {
		if (roleName != null) {
			try {
				Principal p = super.createIdentity(roleName);
				log.trace("Assign user to role " + roleName);
				userRoles.addMember(p);
			} catch (Exception e) {
				log.debug("Failed to create principal: " + roleName, e);
			}
		}
	}
}
