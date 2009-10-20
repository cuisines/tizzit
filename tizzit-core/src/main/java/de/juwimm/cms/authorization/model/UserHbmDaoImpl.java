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
// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package de.juwimm.cms.authorization.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.authorization.vo.GroupValue;
import de.juwimm.cms.authorization.vo.UserLoginValue;
import de.juwimm.cms.authorization.vo.UserUnitsGroupsValue;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.model.SequenceHbmDao;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.remote.helper.AuthenticationHelper;
import de.juwimm.cms.vo.UnitSlimValue;
import de.juwimm.cms.vo.UnitValue;

/**
 * @see de.juwimm.cms.authorization.model.UserHbm
 */
public class UserHbmDaoImpl extends de.juwimm.cms.authorization.model.UserHbmDaoBase {
	private static Log log = LogFactory.getLog(UserHbmDaoImpl.class);

	@Autowired
	private SequenceHbmDao sequenceHbmDao;

	@Override
	public UserHbm load(String userId) {
		if (userId != null) {
			userId = userId.toLowerCase();
		}
		return super.load(userId);
	}

	@Override
	public UserHbm create(UserHbm userHbm) {
		if (userHbm.getUserId() == null) {
			try {
				Integer id = sequenceHbmDao.getNextSequenceNumber("user.user_id");
				userHbm.setUserId(String.valueOf(id));
			} catch (Exception e) {
				log.error("Error creating primary key", e);
			}
		} else {
			userHbm.setUserId(userHbm.getUserId().toLowerCase());
		}
		return super.create(userHbm);
	}

	/**
	 * Get all groups of this user.<br>
	 * <b>PLEASE NOTE:</b><br>
	 * This Method only returns the groups, the user belongs to regarding the
	 * active site, the calling<br>
	 * user is actually in.
	 * 
	 * @return All groups the User belongs to, dependend to the active site the
	 *         logged in user belongs.
	 */
	@Override
	protected Collection handleGetGroups4ActiveSite(UserHbm user) throws Exception {
		if (user.getActiveSite() != null) {
			Integer siteId = user.getActiveSite().getSiteId();
			Collection coll = null;
			if (user.isMasterRoot()) {
				coll = getGroupHbmDao().findAll(siteId);
			} else {
				coll = getGroupHbmDao().findByUserAndSite(user.getUserId(), siteId);
			}
			return coll;
		}
		return new ArrayList();
	}

	@Override
	protected Collection handleGetUnits4ActiveSite(UserHbm user) throws Exception {
		Integer siteId = null;
		try {
			UserHbm userMe = load(AuthenticationHelper.getUserName());
			siteId = userMe.getActiveSite().getSiteId();
		} catch (Exception exe) {
			log.error("Error while getting the actual siteid inside getRoles()", exe);
		}
		Collection coll = null;
		try {
			if (user.isMasterRoot() || isInRole(user, "siteRoot", user.getActiveSite())) {
				coll = getUnitHbmDao().findAll(siteId);
			} else {
				coll = getUnitHbmDao().findByUserAndSite(user.getUserId(), siteId);
			}
		} catch (Exception exe) {
			log.error("Unknown error catched during getGroups()");
		}
		return coll;
	}

	@Override
	protected void handleAddGroup(GroupHbm group, String principal, String username) throws Exception {
		try {
			UserHbm userPrincipal = load(principal);
			UserHbm userToAdd = load(username);
			if (group.getSite().equals(userPrincipal.getActiveSite())) {
				userToAdd.getGroups().add(group);
				group.getUsers().add(userToAdd);
			} else {
				throw new EJBException(principal + " has tried to add a Group to a user that does not belong to the activeSite");
			}
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	@Override
	protected UserLoginValue handleGetUserLoginValue(UserHbm user) throws Exception {
		UserLoginValue value = new UserLoginValue();
		try {
			value.setUser(user.getUserValue());
			value.setSiteConfigXML(user.getConfigXML());
			value.setSiteName(user.getActiveSite().getName());
			Collection<UnitHbm> units = getUnits4ActiveSite(user);
			if (units != null) {
				UnitValue[] uv = new UnitValue[units.size()];
				int i = 0;
				for (UnitHbm unit : units) {
					uv[i++] = getUnitHbmDao().getDao(unit);
				}
				value.setUnits(uv);
			}
		} catch (Exception e) {
			throw new EJBException(e);
		}
		return value;
	}

	/**
	 * Get all roles of this user.<br>
	 * <b>PLEASE NOTE:</b><br>
	 * This Method only returns the roles, the user belongs to regarding the
	 * active site, the calling<br>
	 * user is actually in.
	 * 
	 * @return All Roles the User belongs to, dependend to the active site the
	 *         logged in user belongs.
	 */
	@Override
	protected Collection handleGetRoles(UserHbm user, SiteHbm site) throws Exception {
		Collection coll = null;
		if (site != null) {
			Integer siteId = site.getSiteId();
			try {
				if (user.isMasterRoot()) {
					coll = getRoleHbmDao().findAll(siteId);
				} else {
					coll = getRoleHbmDao().findByUserAndSite(user.getUserId(), siteId);
				}
			} catch (Exception exe) {
				log.error("Unknown error catched during getRoles()", exe);
			}
		}
		return coll;
	}

	@Override
	protected boolean handleIsInRole(UserHbm user, String role, SiteHbm site) throws Exception {
		if (user.isMasterRoot()) { return true; }
		Iterator it = getRoles(user, site).iterator();
		while (it.hasNext()) {
			RoleHbm roleHbm = (RoleHbm) it.next();
			if (roleHbm.getRoleId().equals(role)) { return true; }
		}
		return false;
	}

	@Override
	protected UserUnitsGroupsValue handleGetUserUnitsGroupsValue(UserHbm user) throws Exception {
		UserUnitsGroupsValue uugv = new UserUnitsGroupsValue();
		uugv.setUser(user.getUserValue());
		Collection groupsList = getGroups4ActiveSite(user);
		GroupValue[] gva = new GroupValue[groupsList.size()];
		int i = 0;
		Iterator it = groupsList.iterator();
		while (it.hasNext()) {
			GroupHbm group = (GroupHbm) it.next();
			gva[i++] = group.getGroupValue();
		}
		uugv.setGroups(gva);
		Collection unitsList = getUnits4ActiveSite(user);
		UnitSlimValue[] usva = new UnitSlimValue[unitsList.size()];
		i = 0;
		it = unitsList.iterator();
		while (it.hasNext()) {
			UnitHbm unit = (UnitHbm) it.next();
			usva[i++] = unit.getUnitSlimValue();
		}
		uugv.setUnits(usva);
		return uugv;
	}

	@Override
	protected boolean handleIsInUnit(Integer unitId, UserHbm user) throws Exception {
		if (user.isMasterRoot()) { return true; }
		boolean retVal = false;
		if (isInRole(user, UserRights.SITE_ROOT, user.getActiveSite())) {
			retVal = true;
		} else {
			Iterator<UnitHbm> it = user.getUnits().iterator();
			while (it.hasNext()) {
				UnitHbm ul = it.next();
				if (ul.getUnitId().equals(unitId)) {
					retVal = true;
					break;
				}
			}
		}
		return retVal;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findAll(final int transform, final java.lang.Integer siteId) {
		return this.findAll(transform, "select u from de.juwimm.cms.authorization.model.UserHbm u inner join u.sites s where s.siteId = ?", siteId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findAll(final int transform) {
		return this.findAll(transform, "from de.juwimm.cms.authorization.model.UserHbm as userHbm");
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findAll4Group(final int transform, final java.lang.Integer groupId) {
		return this.findAll4Group(transform, "select u from de.juwimm.cms.authorization.model.UserHbm as u inner join u.groups g where g.groupId = ?", groupId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findAll4Unit(final int transform, final java.lang.Integer unitId) {
		return this.findAll4Unit(transform, "select u from de.juwimm.cms.authorization.model.UserHbm as u inner join u.units n where n.unitId = ?", unitId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findAll4UnitAndGroup(final int transform, final java.lang.Integer unitId, final java.lang.Integer groupId) {
		return this.findAll4UnitAndGroup(transform, "select u from de.juwimm.cms.authorization.model.UserHbm as u inner join u.units n inner join u.groups g where n.unitId = ? and g.groupId = ?", unitId, groupId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void remove(de.juwimm.cms.authorization.model.UserHbm userHbm) {
		if (userHbm == null) { throw new IllegalArgumentException("UserHbm.remove - 'userHbm' can not be null"); }
		// TODO could that be made different?
		Collection<de.juwimm.cms.authorization.model.GroupHbm> groups = userHbm.getGroups();
		int[] groupIds = new int[groups.size()];
		int i = 0;
		for (GroupHbm group : groups) {
			groupIds[i++] = group.getGroupId();
		}
		for (i = 0; i < groupIds.length; i++) {
			userHbm.dropGroup(getGroupHbmDao().load(groupIds[i]));
		}

		//        Collection<de.juwimm.cms.model.SiteHbm> sites = userHbm.getSites();
		//        int[] siteIds = new int[sites.size()];
		//        i=0;
		//        for(SiteHbm site : sites)
		//        {
		//        	siteIds[i++] = site.getSiteId();
		//        }
		//        for(i=0; i<siteIds.length;i++)
		//        {
		//        	//userHbm.drop
		//        }
		super.remove(userHbm);
	}
}
