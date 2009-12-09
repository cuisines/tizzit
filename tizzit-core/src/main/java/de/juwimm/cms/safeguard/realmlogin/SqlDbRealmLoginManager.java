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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import de.juwimm.cms.safeguard.model.RealmJdbcHbm;
import de.juwimm.cms.safeguard.model.RealmJdbcHbmDao;

/**
 * @author <a href="mailto:michael.meyer@juwimm.com">Michael Meyer</a>
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public final class SqlDbRealmLoginManager implements SafeguardLoginManager {
	private static Logger log = Logger.getLogger(SqlDbRealmLoginManager.class);
	private final String username;
	private final String password;
	private final String roleNeeded;
	private final Integer realmId;
	private final RealmJdbcHbmDao realmJdbcHbmDao;

	public SqlDbRealmLoginManager(RealmJdbcHbmDao realmJdbcHbmDao, Integer realmId, String userName, String password, String roleNeeded) {
		this.password = password;
		this.realmId = realmId;
		this.username = userName;
		this.roleNeeded = roleNeeded;
		this.realmJdbcHbmDao = realmJdbcHbmDao;
	}

	public byte login() {
		byte login = SafeguardLoginManager.LOGIN_UNAUTHENTICATED;
		try {
			RealmJdbcHbm sqlrealm = realmJdbcHbmDao.load(this.realmId);
			String jndiName = sqlrealm.getJndiName();
			String sqlUser = sqlrealm.getStatementUser();
			String sqlRoles = sqlrealm.getStatementRolePerUser();

			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet result = null;
			try {
				boolean userExits = false;
				Context ctx = new InitialContext();
				DataSource ds = (DataSource) ctx.lookup(jndiName);
				conn = ds.getConnection();
				if (log.isDebugEnabled()) log.debug("lookup db-connection successfully");
				ctx.close();

				pstmt = conn.prepareStatement(sqlUser);
				pstmt.setString(1, this.username);
				pstmt.setString(2, this.password);

				int count = 0;
				result = pstmt.executeQuery();
				if (log.isDebugEnabled()) log.debug("executed " + sqlUser);
				while (result.next()) {
					count++;
				}
				if (count == 1) {
					userExits = true;
				} else if (count > 1) {
					userExits = true;
					log.warn("ambiguous UserId \"" + this.username + "\", existing " + count + " times!");
				}
				if (log.isDebugEnabled()) log.debug("user \"" + this.username + "\" at realm " + this.realmId + " exists: " + userExits);
				if (!userExits) return login;
				if (this.roleNeeded != null && this.roleNeeded.length() > 0) {
					login = SafeguardLoginManager.LOGIN_UNAUTHORIZED;
					// check required role for this realm
					try {
						if (result != null) result.close();
					} catch (Exception ine) {
						log.warn("Error closing ResultSet?", ine);
					}
					try {
						if (pstmt != null) pstmt.close();
					} catch (Exception ine) {
						log.warn("Error closing PreparedStatement?", ine);
					}
					pstmt = conn.prepareStatement(sqlRoles);
					pstmt.setString(1, this.username);
					result = pstmt.executeQuery();
					while (result.next()) {
						String role = result.getString(1);
						if (role != null && role.length() > 0) role = role.trim();
						if (role != null && role.equalsIgnoreCase(this.roleNeeded.trim())) {
							login = SafeguardLoginManager.LOGIN_SUCCESSFULLY;
							break;
						}
					}
				} else {
					login = SafeguardLoginManager.LOGIN_SUCCESSFULLY;
				}
				if (log.isDebugEnabled()) log.debug("user \"" + this.username + "\" at realm " + this.realmId + " is logedin: " + login);
			} catch (Exception ex) {
				log.error("Error executing statements: " + ex.getMessage(), ex);
			} finally {
				try {
					if (result != null) result.close();
				} catch (Exception ine) {
					log.warn("Error closing ResultSet?", ine);
				}
				try {
					if (pstmt != null) pstmt.close();
				} catch (Exception ine) {
					log.warn("Error closing PreparedStatement?", ine);
				}
				try {
					if (conn != null) conn.close();
				} catch (Exception ine) {
					log.warn("Error closing Connection?", ine);
				}
			}
		} catch (Exception ex) {
			log.error("JDBC-Realm " + this.realmId + " not found: " + ex.getMessage(), ex);
		}
		return login;
	}

	public Collection<String> getRoles() {
		HashSet<String> rolesSet = new HashSet<String>();
		try {
			RealmJdbcHbm sqlrealm = realmJdbcHbmDao.load(this.realmId);
			String jndiName = sqlrealm.getJndiName();
			String sqlRoles = sqlrealm.getStatementRolePerUser();

			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet result = null;
			try {
				Context ctx = new InitialContext();
				DataSource ds = (DataSource) ctx.lookup(jndiName);
				conn = ds.getConnection();
				if (log.isDebugEnabled()) log.debug("lookup db-connection successfully");
				ctx.close();

				pstmt = conn.prepareStatement(sqlRoles);
				pstmt.setString(1, this.username);
				result = pstmt.executeQuery();
				while (result.next()) {
					String role = result.getString(1);
					if (role != null && role.length() > 0) role = role.trim().toLowerCase();
					rolesSet.add(role);
				}
			} catch (Exception ex) {
				log.error("Error executing statements: " + ex.getMessage(), ex);
			} finally {
				try {
					if (result != null) result.close();
				} catch (Exception ine) {
					log.warn("Error closing ResultSet?", ine);
				}
				try {
					if (pstmt != null) pstmt.close();
				} catch (Exception ine) {
					log.warn("Error closing PreparedStatement?", ine);
				}
				try {
					if (conn != null) conn.close();
				} catch (Exception ine) {
					log.warn("Error closing Connection?", ine);
				}
			}
		} catch (Exception ex) {
			log.error("JDBC-Realm " + this.realmId + " not found: " + ex.getMessage(), ex);
		}
		return rolesSet;
	}

}
