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
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.login.FailedLoginException;
import javax.transaction.Transaction;

import org.jboss.tm.TransactionDemarcationSupport;

/**
 * A JDBC based login module that supports authentication and role mapping.
 * It is based on two logical tables:
 * <ul>
 * <li>Principals(PrincipalID text, Password text)
 * <li>Roles(PrincipalID text, Role text, RoleGroup text)
 * </ul>
 * <p>
 * LoginModule options:
 * <ul>
 * <li><em>dsJndiName</em>: The name of the DataSource of the database
 * containing the Principals, Roles tables
 * <li><em>principalsQuery</em>: The prepared statement query, equivalent to:
 * <pre>
 *    "select Password from Principals where PrincipalID=?"
 * </pre>
 * <li><em>rolesQuery</em>: The prepared statement query, equivalent to:
 * <pre>
 *    "select Role, RoleGroup from Roles where PrincipalID=?"
 * </pre>
 * </ul>
 *
 * @author <a href="mailto:on@ibis.odessa.ua">Oleg Nitz</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision: 57203 $
 */
public class DatabaseServerLoginModule extends UsernamePasswordLoginModule {
	/** The JNDI name of the DataSource to use */
	protected String dsJndiName;
	/** The sql query to obtain the user password */
	protected String principalsQuery = "select Password from Principals where PrincipalID=?";
	/** The sql query to obtain the user roles */
	protected String rolesQuery = "select Role, RoleGroup from Roles where PrincipalID=?";
	/** Whether to suspend resume transactions during database operations */
	protected boolean suspendResume = true;

	/**
	 * Initialize this LoginModule.
	 * 
	 * @param options -
	 * dsJndiName: The name of the DataSource of the database containing the
	 *    Principals, Roles tables
	 * principalsQuery: The prepared statement query, equivalent to:
	 *    "select Password from Principals where PrincipalID=?"
	 * rolesQuery: The prepared statement query, equivalent to:
	 *    "select Role, RoleGroup from Roles where PrincipalID=?"
	 */
	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
		super.initialize(subject, callbackHandler, sharedState, options);
		dsJndiName = (String) options.get("dsJndiName");
		if (dsJndiName == null) dsJndiName = "java:/DefaultDS";
		Object tmp = options.get("principalsQuery");
		if (tmp != null) principalsQuery = tmp.toString();
		tmp = options.get("rolesQuery");
		if (tmp != null) rolesQuery = tmp.toString();
		tmp = options.get("suspendResume");
		if (tmp != null) suspendResume = Boolean.valueOf(tmp.toString()).booleanValue();
		if (log.isTraceEnabled()) {
			log.trace("DatabaseServerLoginModule, dsJndiName=" + dsJndiName);
			log.trace("principalsQuery=" + principalsQuery);
			log.trace("rolesQuery=" + rolesQuery);
			log.trace("suspendResume=" + suspendResume);
		}
	}

	/** Get the expected password for the current username available via
	 * the getUsername() method. This is called from within the login()
	 * method after the CallbackHandler has returned the username and
	 * candidate password.
	 * @return the valid password String
	 */
	@Override
	protected String getUsersPassword() throws LoginException {
		boolean trace = log.isTraceEnabled();
		String username = getUsername();
		String password = null;
		Connection conn = null;
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
			// Get the password
			if (trace) log.trace("Excuting query: " + principalsQuery + ", with username: " + username);
			ps = conn.prepareStatement(principalsQuery);
			ps.setString(1, username);
			rs = ps.executeQuery();
			if (rs.next() == false) {
				if (trace) log.trace("Query returned no matches from db");
				throw new FailedLoginException("No matching username found in Principals");
			}

			password = rs.getString(1);
			password = convertRawPassword(password);
			if (trace) log.trace("Obtained user password");
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
				} catch (SQLException ex) {
				}
			}
			if (suspendResume) {
				TransactionDemarcationSupport.resumeAnyTransaction(tx);
				if (log.isTraceEnabled()) log.trace("resumeAnyTransaction");
			}
		}
		return password;
	}

	/** Execute the rolesQuery against the dsJndiName to obtain the roles for
	 the authenticated user.
	 
	 @return Group[] containing the sets of roles
	 */
	@Override
	protected Group[] getRoleSets() throws LoginException {
		String username = getUsername();
		if (log.isTraceEnabled()) log.trace("getRoleSets using rolesQuery: " + rolesQuery + ", username: " + username);
		Group[] roleSets = Util.getRoleSets(username, dsJndiName, rolesQuery, this, suspendResume);
		return roleSets;
	}

	/** A hook to allow subclasses to convert a password from the database
	 into a plain text string or whatever form is used for matching against
	 the user input. It is called from within the getUsersPassword() method.
	 @param rawPassword - the password as obtained from the database
	 @return the argument rawPassword
	 */
	protected String convertRawPassword(String rawPassword) {
		return rawPassword;
	}

}
