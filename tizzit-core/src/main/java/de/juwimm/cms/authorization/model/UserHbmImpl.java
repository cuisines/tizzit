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

import java.security.MessageDigest;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tizzit.util.Base64;

import de.juwimm.cms.authorization.vo.UserValue;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.UnitHbm;

/**
 * @see de.juwimm.cms.authorization.model.UserHbm
 */
public class UserHbmImpl extends de.juwimm.cms.authorization.model.UserHbm {
	/**
	 * The serial version UID of this class. Needed for serialization.
	 */
	private static final long serialVersionUID = -4618667299327020506L;
	private static Log log = LogFactory.getLog(UserHbmImpl.class);

	/**
	 * @see de.juwimm.cms.authorization.model.UserHbm#addUnit(de.juwimm.cms.model.UnitHbm)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void addUnit(de.juwimm.cms.model.UnitHbm unit) {
		unit.getUsers().add(this);
		getUnits().add(unit);
	}

	/**
	 * @see de.juwimm.cms.authorization.model.UserHbm#dropUnit(de.juwimm.cms.model.UnitHbm)
	 */
	@Override
	public void dropUnit(de.juwimm.cms.model.UnitHbm unit) {
		unit.getUsers().remove(this);
		getUnits().remove(unit);
	}

	/**
	 * @see de.juwimm.cms.authorization.model.UserHbm#isInGroup(java.lang.Integer)
	 */
	@Override
	public boolean isInGroup(java.lang.Integer groupId) { //TODO inefficent! solve per HBM query
		Collection c = getGroups();
		Iterator it = c.iterator();
		GroupHbm g = null;
		while (it.hasNext()) {
			g = (GroupHbm) it.next();
			if (g.getGroupId().equals(groupId)) { return true; }
		}
		return false;
	}

	/**
	 * @see de.juwimm.cms.authorization.model.UserHbm#dropGroup(de.juwimm.cms.authorization.model.GroupHbm)
	 */
	@Override
	public void dropGroup(de.juwimm.cms.authorization.model.GroupHbm group) {
		try {
			getGroups().remove(group);
			group.getUsers().remove(this);
		} catch (Exception ex) {
			throw new EJBException(ex.getMessage());
		}
	}

	/**
	 * @return UserValue Object
	 * @see de.juwimm.cms.authorization.model.UserHbm#getUserValue()
	 */
	@Override
	public de.juwimm.cms.authorization.vo.UserValue getUserValue() {
		UserValue value = new UserValue();
		try {
			value.setUserName(getUserId());
			value.setFirstName(getFirstName());
			value.setLastName(getLastName());
			value.setMasterRoot(isMasterRoot());
			value.setEmail(getEmail());
			value.setConfigXML(getConfigXML());
			value.setLoginDate(getLoginDate());
		} catch (Exception e) {
			throw new EJBException(e);
		}
		return value;
	}

	/**
	 * @see de.juwimm.cms.authorization.model.UserHbm#setUserValue(de.juwimm.cms.authorization.vo.UserValue)
	 */
	@Override
	public void setUserValue(de.juwimm.cms.authorization.vo.UserValue value) {
		try {
			setFirstName(value.getFirstName());
			setLastName(value.getLastName());
			setMasterRoot(value.isMasterRoot());
			setEmail(value.getEmail());
			setConfigXML(value.getConfigXML());
			setLoginDate(value.getLoginDate());
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	/**
	 * @see de.juwimm.cms.authorization.model.UserHbm#getUserLoginValue()
	 */

	public de.juwimm.cms.authorization.vo.UserLoginValue getUserLoginValue() {
		return null;
	}

	/**
	 * @see de.juwimm.cms.authorization.model.UserHbm#setUserLoginValue(de.juwimm.cms.authorization.vo.UserLoginValue)
	 */
	@Override
	public void setUserLoginValue(de.juwimm.cms.authorization.vo.UserLoginValue value) {
		setFirstName(value.getUser().getFirstName());
		setLastName(value.getUser().getLastName());
		setMasterRoot(value.getUser().isMasterRoot());
		setEmail(value.getUser().getEmail());
		setLoginDate(value.getUser().getLoginDate());
	}

	@Override
	public String encrypt(String x) {
		String passwd = null;
		try {
			byte[] hash = MessageDigest.getInstance("SHA-1").digest(x.getBytes());
			passwd = Base64.encodeBytes(hash);
		} catch (Exception exe) {
			log.error("encryption failed", exe);
		}
		return passwd;
	}

	@Override
	public boolean isInUnit(Integer unitId) {
		Collection units = getUnits();
		Iterator it = units.iterator();
		while (it.hasNext()) {
			int uId = ((UnitHbm) it.next()).getUnitId();
			if (uId == unitId) { return true; }
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.authorization.model.UserHbm#dropSite(de.juwimm.cms.model.SiteHbm)
	 */
	@Override
	public void dropSite(SiteHbm site) {
		this.getSites().remove(site);
	}
}