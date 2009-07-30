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
package de.juwimm.cms.safeguard.model;

import javax.ejb.CreateException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Element;

import de.juwimm.cms.model.SequenceHbmDao;
import de.juwimm.cms.safeguard.vo.RealmSimplePwUserValue;
import de.juwimm.util.XercesHelper;

/**
 * @see de.juwimm.cms.safeguard.model.RealmSimplePwUserHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 *         company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id: RealmSimplePwUserHbmDaoImpl.java 26886 2008-04-30 10:25:41Z
 *          greivej $
 */
public class RealmSimplePwUserHbmDaoImpl extends RealmSimplePwUserHbmDaoBase {
	private static Log log = LogFactory.getLog(RealmSimplePwUserHbmDaoImpl.class);

	@Autowired
	private SequenceHbmDao sequenceHbmDao;

	@Override
	public RealmSimplePwUserHbm handleCreate(Element element, boolean newId) throws CreateException {
		RealmSimplePwUserHbm realm = new RealmSimplePwUserHbmImpl();
		if (newId) {
			try {
				Integer id = sequenceHbmDao.getNextSequenceNumber("realm_simple_pw_user.simple_pw_realm_user_id");
				realm.setSimplePwRealmUserId(id);
			} catch (Exception e) {
				log.error("Error creating primary key", e);
			}
		}
		try {
			String userName = XercesHelper.getNodeValue(element, "userName");
			String password = XercesHelper.getNodeValue(element, "password");
			realm.setUserName(userName);
			realm.setPassword(password);
		} catch (Exception e) {
			log.error("Error creating new RealmSimplePwBeanImpl: " + e.getMessage());
			throw new CreateException("Error creating new RealmSimplePwBeanImpl: " + e.getMessage());
		}
		return super.create(realm);
	}

	@Override
	public RealmSimplePwUserHbm create(RealmSimplePwUserHbm realmSimplePwUserHbm) {
		try {
			Integer id = sequenceHbmDao.getNextSequenceNumber("realm_simple_pw_user.simple_pw_realm_user_id");
			realmSimplePwUserHbm.setSimplePwRealmUserId(id);
		} catch (Exception e) {
			log.error("Error creating primary key", e);
		}
		return super.create(realmSimplePwUserHbm);
	}

	@Override
	protected void handleCreate(Integer simplePwRealmId, RealmSimplePwUserValue realmSimplePwUserValue) throws Exception {
		RealmSimplePwUserHbm realm = RealmSimplePwUserHbm.Factory.newInstance();
		try {
			Integer id = sequenceHbmDao.getNextSequenceNumber("realm_simple_pw_user.simple_pw_realm_user_id");
			realm.setSimplePwRealmUserId(id);
		} catch (Exception e) {
			log.error("Error creating primary key", e);
		}
		realm.setPassword(realmSimplePwUserValue.getPassword());
		realm.setUserName(realmSimplePwUserValue.getUserName());
		String roles = realmSimplePwUserValue.getRoles();
		if (roles != null && (roles.length() == 0 || "null".equalsIgnoreCase(roles))) {
			roles = null;
		}
		realm.setRoles(roles);
		getHibernateTemplate().save(realm);
	}

	@Override
	protected void handleCreate(Element element) throws Exception {
		RealmSimplePwUserHbm realm = RealmSimplePwUserHbm.Factory.newInstance();
		try {
			Integer id = sequenceHbmDao.getNextSequenceNumber("realm_simple_pw_user.simple_pw_realm_user_id");
			realm.setSimplePwRealmUserId(id);
		} catch (Exception e) {
			log.error("Error creating primary key", e);
		}
		String userName = XercesHelper.getNodeValue(element, "userName");
		String password = XercesHelper.getNodeValue(element, "password");
		String roles = XercesHelper.getNodeValue(element, "roles");
		realm.setUserName(userName);
		realm.setPassword(password);
		if (roles != null && (roles.length() == 0 || "null".equalsIgnoreCase(roles))) {
			roles = null;
		}
		realm.setRoles(roles);
		getHibernateTemplate().save(realm);
	}

	public java.lang.Object findByUsernamePasswordRealmId(final int transform, final java.lang.String userName, final java.lang.String password, final java.lang.Integer realmSimplePwId) {
		return this.findByUsernamePasswordRealmId(transform, "from de.juwimm.cms.safeguard.model.RealmSimplePwUserHbm as r where r.userName = ? and r.password = ? and r.simplePwRealm.simplePwRealmId = ?", userName, password, realmSimplePwId);
	}

	public java.lang.Object findByUsernameAndRealmId(final int transform, final java.lang.String userName, final java.lang.Integer realmSimplePwId) {
		return this.findByUsernameAndRealmId(transform, "from de.juwimm.cms.safeguard.model.RealmSimplePwUserHbm as r where r.userName = ? and r.simplePwRealm.simplePwRealmId = ?", userName, realmSimplePwId);
	}

}
