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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.Principal;
import java.security.acl.Group;
import java.sql.*;
import java.util.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.sql.DataSource;
import javax.transaction.Transaction;

import org.apache.log4j.Logger;
import org.jboss.security.SimpleGroup;
import org.jboss.tm.TransactionDemarcationSupport;

/**
 * Common login module utility methods
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision: 57430 $
 */
public class Util {
	/** Create the set of roles the user belongs to by parsing the roles.properties
	 data for username=role1,role2,... and username.XXX=role1,role2,...
	 patterns.
	 * 
	 * @param targetUser - the username to obtain roles for
	 * @param roles - the Properties containing the user=roles mappings
	 * @param roleGroupSeperator - the character that seperates a username
	 *    from a group name, e.g., targetUser[.GroupName]=roles
	 * @param aslm - the login module to use for Principal creation
	 * @return Group[] containing the sets of roles
	 */
	static Group[] getRoleSets(String targetUser, Properties roles, char roleGroupSeperator, AbstractServerLoginModule aslm) {
		Logger log = aslm.log;
		boolean trace = log.isTraceEnabled();
		Enumeration users = roles.propertyNames();
		SimpleGroup rolesGroup = new SimpleGroup("Roles");
		ArrayList groups = new ArrayList();
		groups.add(rolesGroup);
		while (users.hasMoreElements() && targetUser != null) {
			String user = (String) users.nextElement();
			String value = roles.getProperty(user);
			if (trace) log.trace("Checking user: " + user + ", roles string: " + value);
			// See if this entry is of the form targetUser[.GroupName]=roles
			// JBAS-3742 - skip potential '.' in targetUser
			int index = user.indexOf(roleGroupSeperator, targetUser.length());

			boolean isRoleGroup = false;
			boolean userMatch = false;
			if (index > 0 && targetUser.regionMatches(0, user, 0, index) == true)
				isRoleGroup = true;
			else
				userMatch = targetUser.equals(user);

			// Check for username.RoleGroup pattern
			if (isRoleGroup == true) {
				String groupName = user.substring(index + 1);
				if (groupName.equals("Roles")) {
					if (trace) log.trace("Adding to Roles: " + value);
					parseGroupMembers(rolesGroup, value, aslm);
				} else {
					if (trace) log.trace("Adding to " + groupName + ": " + value);
					SimpleGroup group = new SimpleGroup(groupName);
					parseGroupMembers(group, value, aslm);
					groups.add(group);
				}
			} else if (userMatch == true) {
				if (trace) log.trace("Adding to Roles: " + value);
				// Place these roles into the Default "Roles" group
				parseGroupMembers(rolesGroup, value, aslm);
			}
		}
		Group[] roleSets = new Group[groups.size()];
		groups.toArray(roleSets);
		return roleSets;
	}

	/** Execute the rolesQuery against the dsJndiName to obtain the roles for
	 the authenticated user.
	 
	 @return Group[] containing the sets of roles
	 */
	static Group[] getRoleSets(String username, String dsJndiName, String rolesQuery, AbstractServerLoginModule aslm) throws LoginException {
		return getRoleSets(username, dsJndiName, rolesQuery, aslm, false);
	}

	/** Execute the rolesQuery against the dsJndiName to obtain the roles for
	 the authenticated user.
	 
	 @return Group[] containing the sets of roles
	 */
	static Group[] getRoleSets(String username, String dsJndiName, String rolesQuery, AbstractServerLoginModule aslm, boolean suspendResume) throws LoginException {
		Logger log = aslm.log;
		boolean trace = log.isTraceEnabled();
		Connection conn = null;
		HashMap setsMap = new HashMap();
		PreparedStatement ps = null;
		ResultSet rs = null;

		Transaction tx = null;
		if (suspendResume) {
			tx = TransactionDemarcationSupport.suspendAnyTransaction();
			if (trace) log.trace("suspendAnyTransaction");
		}

		try {
			InitialContext ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup(dsJndiName);
			conn = ds.getConnection();
			// Get the user role names
			if (trace) log.trace("Excuting query: " + rolesQuery + ", with username: " + username);
			ps = conn.prepareStatement(rolesQuery);
			try {
				ps.setString(1, username);
			} catch (ArrayIndexOutOfBoundsException ignore) {
				// The query may not have any parameters so just try it
			}
			rs = ps.executeQuery();
			if (rs.next() == false) {
				if (trace) log.trace("No roles found");
				if (aslm.getUnauthenticatedIdentity() == null) throw new FailedLoginException("No matching username found in Roles");
				/* We are running with an unauthenticatedIdentity so create an
				 empty Roles set and return.
				 */
				Group[] roleSets = {new SimpleGroup("Roles")};
				return roleSets;
			}

			do {
				String name = rs.getString(1);
				String groupName = rs.getString(2);
				if (groupName == null || groupName.length() == 0) groupName = "Roles";
				Group group = (Group) setsMap.get(groupName);
				if (group == null) {
					group = new SimpleGroup(groupName);
					setsMap.put(groupName, group);
				}

				try {
					Principal p = aslm.createIdentity(name);
					if (trace) log.trace("Assign user to role " + name);
					group.addMember(p);
				} catch (Exception e) {
					log.debug("Failed to create principal: " + name, e);
				}
			} while (rs.next());
		} catch (NamingException ex) {
			LoginException le = new LoginException("Error looking up DataSource from: " + dsJndiName);
			le.initCause(ex);
			throw le;
		} catch (SQLException ex) {
			LoginException le = new LoginException("Query failed");
			le.initCause(ex);
			throw le;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception ex) {
				}
			}
			if (suspendResume) {
				TransactionDemarcationSupport.resumeAnyTransaction(tx);
				if (trace) log.trace("resumeAnyTransaction");
			}
		}

		Group[] roleSets = new Group[setsMap.size()];
		setsMap.values().toArray(roleSets);
		return roleSets;
	}

	/** Utility method which loads the given properties file and returns a
	 * Properties object containing the key,value pairs in that file.
	 * The properties files should be in the class path as this method looks
	 * to the thread context class loader (TCL) to locate the resource. If the
	 * TCL is a URLClassLoader the findResource(String) method is first tried.
	 * If this fails or the TCL is not a URLClassLoader getResource(String) is
	 * tried.
	 * @param defaultsName - the name of the default properties file resource
	 *    that will be used as the default Properties to the ctor of the
	 *    propertiesName Properties instance.
	 * @param propertiesName - the name of the properties file resource
	 * @param log - the logger used for trace level messages
	 * @return the loaded properties file if found
	 * @exception java.io.IOException thrown if the properties file cannot be found
	 *    or loaded 
	 */
	static Properties loadProperties(String defaultsName, String propertiesName, Logger log) throws IOException {
		Properties bundle = null;
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL defaultUrl = null;
		URL url = null;
		// First check for local visibility via a URLClassLoader.findResource
		if (loader instanceof URLClassLoader) {
			URLClassLoader ucl = (URLClassLoader) loader;
			defaultUrl = ucl.findResource(defaultsName);
			url = ucl.findResource(propertiesName);
			log.trace("findResource: " + url);
		}
		// Do a general resource search
		if (defaultUrl == null) defaultUrl = loader.getResource(defaultsName);
		if (url == null) url = loader.getResource(propertiesName);
		if (url == null && defaultUrl == null) {
			String msg = "No properties file: " + propertiesName + " or defaults: " + defaultsName + " found";
			throw new IOException(msg);
		}

		log.trace("Properties file=" + url + ", defaults=" + defaultUrl);
		Properties defaults = new Properties();
		if (defaultUrl != null) {
			try {
				InputStream is = defaultUrl.openStream();
				defaults.load(is);
				is.close();
				log.debug("Loaded defaults, users=" + defaults.keySet());
			} catch (Throwable e) {
				log.debug("Failed to load defaults", e);
			}
		}

		bundle = new Properties(defaults);
		if (url != null) {
			InputStream is = url.openStream();
			if (is != null) {
				bundle.load(is);
				is.close();
			} else {
				throw new IOException("Properties file " + propertiesName + " not avilable");
			}
			log.debug("Loaded properties, users=" + bundle.keySet());
		}

		return bundle;
	}

	/** Utility method which loads the given properties file and returns a
	 * Properties object containing the key,value pairs in that file.
	 * The properties files should be in the class path as this method looks
	 * to the thread context class loader (TCL) to locate the resource. If the
	 * TCL is a URLClassLoader the findResource(String) method is first tried.
	 * If this fails or the TCL is not a URLClassLoader getResource(String) is
	 * tried. If not, an absolute path is tried.
	 * @param propertiesName - the name of the properties file resource
	 * @param log - the logger used for trace level messages
	 * @return the loaded properties file if found
	 * @exception java.io.IOException thrown if the properties file cannot be found
	 *    or loaded 
	 */
	static Properties loadProperties(String propertiesName, Logger log) throws IOException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL url = null;
		// First check for local visibility via a URLClassLoader.findResource
		if (loader instanceof URLClassLoader) {
			URLClassLoader ucl = (URLClassLoader) loader;
			url = ucl.findResource(propertiesName);
			log.trace("findResource: " + url);
		}
		if (url == null) url = loader.getResource(propertiesName);
		if (url == null) {
			url = new URL(propertiesName);
		}

		log.trace("Properties file=" + url);

		Properties bundle = new Properties();
		if (url != null) {
			InputStream is = url.openStream();
			if (is != null) {
				bundle.load(is);
				is.close();
			} else {
				throw new IOException("Properties file " + propertiesName + " not avilable");
			}
			log.debug("Loaded properties, users=" + bundle.keySet());
		}

		return bundle;
	}

	/** Parse the comma delimited roles names given by value and add them to
	 * group. The type of Principal created for each name is determined by
	 * the createIdentity method.
	 *
	 * @see AbstractServerLoginModule#createIdentity(String)
	 * 
	 * @param group - the Group to add the roles to.
	 * @param roles - the comma delimited role names.
	 */
	static void parseGroupMembers(Group group, String roles, AbstractServerLoginModule aslm) {
		StringTokenizer tokenizer = new StringTokenizer(roles, ",");
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			try {
				Principal p = aslm.createIdentity(token);
				group.addMember(p);
			} catch (Exception e) {
				aslm.log.warn("Failed to create principal for: " + token, e);
			}
		}
	}
}
