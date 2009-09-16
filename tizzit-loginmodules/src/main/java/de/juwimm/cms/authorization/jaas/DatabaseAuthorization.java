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

import java.security.acl.Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * Delegatee for fetching the roles of any ConQuest-user
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * , Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class DatabaseAuthorization {
	private static final Logger log = Logger.getLogger(DatabaseAuthorization.class);
	
	private static final String sqlUserDetail = "SELECT masterRoot FROM usr WHERE user_id = ?";
	// ################################# ROLES #################################
	private static final String sqlRoles = "SELECT DISTINCT ROLES_ID_FK FROM GROUPS2ROLES WHERE GROUPS_ID_FK IN "
			+ "(SELECT DISTINCT group_id FROM sgroup WHERE group_id IN "
			+ "(SELECT DISTINCT GROUPS_ID_FK FROM GROUPS2USERS WHERE USERS_ID_FK = ?) "
			+ "AND site_id_fk = (SELECT active_site_id_fk FROM usr WHERE user_id = ?))";
	private static final String sqlRolesAnyRoot = "SELECT role_id FROM role";
	// ######## Roles and Rights for access from web ###########################
	private static final String sqlWebRoles = "SELECT DISTINCT rg.ROLES_ID_FK, g.site_id_fk FROM GROUPS2ROLES rg "
			+ "JOIN sgroup g ON rg.GROUPS_ID_FK = g.group_id WHERE GROUPS_ID_FK IN "
			+ "(SELECT DISTINCT group_id FROM sgroup WHERE group_id IN "
			+ "(SELECT DISTINCT GROUPS_ID_FK FROM GROUPS2USERS WHERE USERS_ID_FK = ?) AND site_id_fk IN "
			+ "(SELECT DISTINCT g.site_id_fk FROM sgroup g JOIN GROUPS2USERS ug ON g.group_id = ug.GROUPS_ID_FK "
			+ "WHERE ug.USERS_ID_FK = ?))";
	private static final String sqlWebRolesSiteRoot = "SELECT DISTINCT rg.ROLES_ID_FK, g.site_id_fk FROM GROUPS2ROLES rg "
			+ "JOIN GROUPS2USERS ug ON ug.GROUPS_ID_FK = rg.GROUPS_ID_FK JOIN sgroup g ON ug.GROUPS_ID_FK = g.group_id "
			+ "WHERE ug.USERS_ID_FK = ?";
	private static final String sqlWebRolesMasterRoot = "SELECT DISTINCT rg.ROLES_ID_FK, g.site_id_fk FROM sgroup g "
			+ "JOIN GROUPS2ROLES rg ON g.group_id = rg.GROUPS_ID_FK";
	// ################################# GROUPS ################################
	private static final String sqlGroups = "SELECT DISTINCT group_id, group_name, site_id_fk FROM sgroup WHERE group_id IN "
			+ "(SELECT GROUPS_ID_FK FROM GROUPS2USERS WHERE USERS_ID_FK = ?) "
			+ "AND site_id_fk = (SELECT active_site_id_fk FROM usr WHERE user_id = ?)";
	private static final String sqlGroupsSiteRoot = "SELECT DISTINCT group_id, group_name, site_id_fk FROM sgroup WHERE site_id_fk IN "
			+ "(SELECT site_id_fk FROM SITES2USERS WHERE USERS_ID_FK = ?)";
	private static final String sqlGroupsMasterRoot = "SELECT group_id, group_name, site_id_fk FROM sgroup ";
	// ################################# UNITS #################################
	private static final String sqlUnits = "SELECT DISTINCT u.unit_id, u.name, s.site_id FROM unit u "
			+ "JOIN site s ON u.site_id_fk = s.site_id WHERE u.unit_id IN "
			+ "(SELECT units_id_fk FROM UNITS2USERS WHERE users_id_fk = ?)";
	private static final String sqlUnitsSiteRoot = "SELECT DISTINCT unit_id, name, site_id_fk FROM unit "
			+ "WHERE site_id_fk IN (SELECT g.site_id_fk FROM sgroup g JOIN GROUPS2ROLES rg ON rg.GROUPS_ID_FK = g.group_id "
			+ "JOIN GROUPS2USERS ug ON ug.GROUPS_ID_FK = g.group_id WHERE rg.ROLES_ID_FK = 'siteRoot' AND ug.USERS_ID_FK = ?) "
			+ "OR unit_id IN (SELECT units_id_fk FROM UNITS2USERS WHERE users_id_fk = ?)";
	private static final String sqlUnitsMasterRoot = "SELECT u.unit_id, u.name, s.site_id FROM unit u, site s "
			+ "WHERE u.site_id_fk = s.site_id";
	// ################################# SITES #################################
	private static final String sqlSites = "SELECT site_id, site_name, site_short FROM site s JOIN SITES2USERS us ON "
			+ "s.site_id = us.sites_id_fk WHERE us.USERS_ID_FK = ?";
	private static final String sqlSitesMasterRoot = "SELECT site_id, site_name, site_short FROM site";
	
	
	public static Group[] getRoleSets(String dsJndiName, String userName) {
		try {
			InitialContext ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup(dsJndiName);
			return getRoleSets(ds, userName.toLowerCase());
		} catch (NamingException e) {
			log.error("Error occured", e);
		}
		return new Group[0];
	}
	
	/**
	 * Fetch the roles a user belongs to
	 * 
	 * @param dsJndiName the JNDI-Name for the datasource
	 * @param userName the unique username
	 * @return all groups this user belongs to
	 */
	public static Group[] getRoleSets(DataSource ds, String userName) {
		byte userType = 0; // 0=User, 1=SiteRoot, 2=MasterRoot
		HashMap<String, Group> setsMap = new HashMap<String, Group>();
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			// ################ UserDetail #####################
			conn = ds.getConnection();
			ps = conn.prepareStatement(sqlUserDetail);
			try {
				ps.setString(1, userName);
			} catch (ArrayIndexOutOfBoundsException ignore) {
				// The query may not have any parameters so just try it
			}
			ResultSet rs = ps.executeQuery();
			rs.next();
			if (rs.getByte(1) == 1) {
				userType = 2;
			}
			rs.close();
			ps.close();
			// ################ Roles for CMS #####################
			ArrayList<String> al = new ArrayList<String>(5);
			if (userType > 0) {
				if (log.isTraceEnabled()) {
					log.trace("Examining Roles for MasterRoot User");
				}
				al.add("masterRoot");
				ps = conn.prepareStatement(sqlRolesAnyRoot);
				rs = ps.executeQuery();
				while (rs.next()) {
					al.add(rs.getString(1));
				}
				rs.close();
				ps.close();
			} else {
				if (log.isTraceEnabled()) {
					log.trace("Examining Roles for normal User");
				}
				ps = conn.prepareStatement(sqlRoles);
				try {
					ps.setString(1, userName);
					ps.setString(2, userName);
				} catch (ArrayIndexOutOfBoundsException ignore) {
					// The query may not have any parameters so just try it
				}
				rs = ps.executeQuery();
				boolean usertypeChanged = false;
				while (rs.next()) {
					String roleName = rs.getString(1);
					if (roleName.equalsIgnoreCase("siteRoot")) {
						if (log.isTraceEnabled()) {
							log.trace("Stopped Roles! Found siteRoot");
						}
						usertypeChanged = true;
						userType = 1;
						break;
					}
					al.add(roleName);
				}
				if (usertypeChanged) {
					if (log.isTraceEnabled()) {
						log.trace("Examining Roles for SiteRoot User");
					}
					al = new ArrayList<String>(5);
					rs.close();
					ps.close();
					ps = conn.prepareStatement(sqlRolesAnyRoot);
					rs = ps.executeQuery();
					while (rs.next()) {
						al.add(rs.getString(1));
					}
				}
				rs.close();
				ps.close();
			}
			Iterator it = al.iterator();
			while (it.hasNext()) {
				String roleName = (String) it.next();
				// ##############  Roles / Rigths  ################
				if (log.isTraceEnabled()) log.trace("Added Role: " + roleName);
				String groupName = "Roles";

				Group group = setsMap.get(groupName);
				if (group == null) {
					group = new SimpleGroup(groupName);
					setsMap.put(groupName, group);
				}
				group.addMember(new SimplePrincipal(roleName));
				// ################################################
			}
			// ################ Roles for Web #####################
			userType = 0;
			ps = conn.prepareStatement(sqlUserDetail);
			try {
				ps.setString(1, userName);
			} catch (ArrayIndexOutOfBoundsException ignore) {
				// The query may not have any parameters so just try it
			}
			rs = ps.executeQuery();
			rs.next();
			if (rs.getByte(1) == 1) {
				userType = 2;
			}
			rs.close();
			ps.close();
			al = new ArrayList<String>(5);
			if (userType > 0) {
				if (log.isTraceEnabled()) {
					log.trace("Examining Roles for MasterRoot User");
				}
				al.add("masterRoot");
				ps = conn.prepareStatement(sqlWebRolesMasterRoot);
				rs = ps.executeQuery();
				while (rs.next()) {
					al.add(rs.getString(1) + "_" + rs.getString(2));
				}
				rs.close();
				ps.close();
			} else {
				if (log.isTraceEnabled()) {
					log.trace("Examining Roles for normal User");
				}
				ps = conn.prepareStatement(sqlWebRoles);
				try {
					ps.setString(1, userName);
					ps.setString(2, userName);
				} catch (ArrayIndexOutOfBoundsException ignore) {
					// The query may not have any parameters so just try it
				}
				rs = ps.executeQuery();
				boolean usertypeChanged = false;
				while (rs.next()) {
					String roleName = rs.getString(1);
					if (roleName.equalsIgnoreCase("siteRoot")) {
						if (log.isTraceEnabled()) {
							log.trace("Stopped Roles! Found siteRoot");
						}
						usertypeChanged = true;
						userType = 1;
						break;
					}
					al.add(rs.getString(1) + "_" + rs.getString(2));
				}
				if (usertypeChanged) {
					if (log.isTraceEnabled()) {
						log.trace("Examining Roles for SiteRoot User");
					}
					al = new ArrayList<String>(5);
					rs.close();
					ps.close();
					ps = conn.prepareStatement(sqlWebRolesSiteRoot);
					ps.setString(1, userName);
					rs = ps.executeQuery();
					while (rs.next()) {
						al.add(rs.getString(1) + "_" + rs.getString(2));
					}
				}
				rs.close();
				ps.close();
			}
			it = al.iterator();
			while (it.hasNext()) {
				String roleName = "role_" + (String) it.next();
				// ##############  Roles / Rigths  ################
				if (log.isTraceEnabled()) log.trace("Added Role: " + roleName);
				String groupName = "Roles";

				Group group = setsMap.get(groupName);
				if (group == null) {
					group = new SimpleGroup(groupName);
					setsMap.put(groupName, group);
				}
				group.addMember(new SimplePrincipal(roleName));
				// ################################################
			}
			// ################  Groups as Roles ################
			switch (userType) {
				case 1:
					if (log.isTraceEnabled()) log.trace("Examining Group-Roles for SiteRoot User");
					ps = conn.prepareStatement(sqlGroupsSiteRoot);
					try {
						ps.setString(1, userName);
					} catch (ArrayIndexOutOfBoundsException ignore) {
						// The query may not have any parameters so just try it
					}
					break;
				case 2:
					if (log.isTraceEnabled()) {
						log.trace("Examining Group-Roles for MasterRoot User");
					}
					ps = conn.prepareStatement(sqlGroupsMasterRoot);
					break;
				default:
					if (log.isTraceEnabled()) {
						log.trace("Examining Group-Roles for normal User");
					}
					ps = conn.prepareStatement(sqlGroups);
					try {
						ps.setString(1, userName);
						ps.setString(2, userName);
					} catch (ArrayIndexOutOfBoundsException ignore) {
						// The query may not have any parameters so just try it
					}
					break;
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				String groupRole = "group_" + rs.getString(1) + "_" + Base64.encodeString(rs.getString(2)) + "_site_"
						+ rs.getString(3);
				// ################################################
				//if(log.isTraceEnabled()) log.trace("Added Group-Role: " + unitRole);
				String groupName = "Roles";

				Group group = setsMap.get(groupName);
				if (group == null) {
					group = new SimpleGroup(groupName);
					setsMap.put(groupName, group);
				}
				group.addMember(new SimplePrincipal(groupRole));
				// ################################################
			}
			rs.close();
			ps.close();
			// ################  Units as Roles ################
			switch (userType) {
				case 1:
					if (log.isTraceEnabled()) {
						log.trace("Examining Unit-Roles for SiteRoot User");
					}
					ps = conn.prepareStatement(sqlUnitsSiteRoot);
					try {
						ps.setString(1, userName);
						ps.setString(2, userName);
					} catch (ArrayIndexOutOfBoundsException ignore) {
						// The query may not have any parameters so just try it
					}
					break;
				case 2:
					if (log.isTraceEnabled()) {
						log.trace("Examining Unit-Roles for MasterRoot User");
					}
					ps = conn.prepareStatement(sqlUnitsMasterRoot);
					break;
				default:
					if (log.isTraceEnabled()) {
						log.trace("Examining Unit-Roles for normal User");
					}
					ps = conn.prepareStatement(sqlUnits);
					try {
						ps.setString(1, userName);
					} catch (ArrayIndexOutOfBoundsException ignore) {
						// The query may not have any parameters so just try it
					}
					break;
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				String unitRole = "unit_" + rs.getString(1) + "_" + Base64.encodeString(rs.getString(2)) + "_site_"
						+ rs.getShort(3);
				// ################################################
				//if(log.isTraceEnabled()) log.trace("Added Unit-Role: " + unitRole);
				String groupName = "Roles";

				Group group = setsMap.get(groupName);
				if (group == null) {
					group = new SimpleGroup(groupName);
					setsMap.put(groupName, group);
				}
				group.addMember(new SimplePrincipal(unitRole));
				// ################################################
			}
			rs.close();
			ps.close();
			// ################  Sites as Roles ################
			switch (userType) {
				case 2:
					if (log.isTraceEnabled()) {
						log.trace("Examining Site-Roles for MasterRoot User");
					}
					ps = conn.prepareStatement(sqlSitesMasterRoot);
					break;
				default:
					if (log.isTraceEnabled()) {
						log.trace("Examining Site-Roles for normal User");
					}
					ps = conn.prepareStatement(sqlSites);
					try {
						ps.setString(1, userName);
					} catch (ArrayIndexOutOfBoundsException ignore) {
						// The query may not have any parameters so just try it
					}
					break;
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				String siteDesc = "";
				try {
					siteDesc = Base64.encodeString(rs.getString(3));
				} catch (Exception exe) {}
			
				String siteRole = "site_" + rs.getString(1) + "_" + Base64.encodeString(rs.getString(2)) + "_"
						+ siteDesc;
				// ################################################
				//if(log.isTraceEnabled()) log.trace("Added Site-Role: " + siteRole);
				String groupName = "Roles";

				Group group = setsMap.get(groupName);
				if (group == null) {
					group = new SimpleGroup(groupName);
					setsMap.put(groupName, group);
				}
				group.addMember(new SimplePrincipal(siteRole));
				// ################################################
			}
			rs.close();
			ps.close();
		} catch (Exception exe) {
			log.error("Error occured", exe);
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception exe) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception exe) {
				}
			}
		}

		Group[] roleSets = new Group[setsMap.size()];
		setsMap.values().toArray(roleSets);

		return roleSets;
	}

}
