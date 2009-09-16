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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Element;

import de.juwimm.cms.model.SequenceHbmDao;
import de.juwimm.cms.safeguard.vo.RealmJdbcValue;

/**
 * @see de.juwimm.cms.safeguard.model.RealmJdbcHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class RealmJdbcHbmDaoImpl extends RealmJdbcHbmDaoBase {
	private static Logger log = Logger.getLogger(RealmJdbcHbmDaoImpl.class);

	@Autowired
	private SequenceHbmDao sequenceHbmDao;
	
	@Override
	public RealmJdbcHbm create(RealmJdbcHbm realmJdbcHbm) {
		try {
			Integer id = sequenceHbmDao.getNextSequenceNumber("realm_jdbc.jdbc_realm_id");
			realmJdbcHbm.setJdbcRealmId(id);
		} catch (Exception e) {
			log.error("Error creating primary key", e);
		}
		return super.create(realmJdbcHbm);
	}

	@Override
	protected void handleCreate(Element element) throws Exception {
		RealmJdbcHbm realm = RealmJdbcHbm.Factory.newInstance();
		String name = XercesHelper.getNodeValue(element, "realmName");
		realm.setRealmName(name);
		String loginPageId = XercesHelper.getNodeValue(element, "loginPageId");
		realm.setLoginPageId(loginPageId);
		String jndiName = XercesHelper.getNodeValue(element, "jndiName");
		realm.setJndiName(jndiName);
		String statementUser = XercesHelper.getNodeValue(element, "statementUser");
		realm.setStatementUser(statementUser);
		String statementRolePerUser = XercesHelper.getNodeValue(element, "statementRolePerUser");
		realm.setStatementRolePerUser(statementRolePerUser);
		this.create(realm);
	}

	@Override
	protected RealmJdbcHbm handleCreate(Element element, boolean newId) throws Exception {
		RealmJdbcHbm realm = RealmJdbcHbm.Factory.newInstance();
		String name = XercesHelper.getNodeValue(element, "realmName");
		realm.setRealmName(name);
		String loginPageId = XercesHelper.getNodeValue(element, "loginPageId");
		realm.setLoginPageId(loginPageId);
		String jndiName = XercesHelper.getNodeValue(element, "jndiName");
		realm.setJndiName(jndiName);
		String statementUser = XercesHelper.getNodeValue(element, "statementUser");
		realm.setStatementUser(statementUser);
		String statementRolePerUser = XercesHelper.getNodeValue(element, "statementRolePerUser");
		realm.setStatementRolePerUser(statementRolePerUser);
		if (newId) {
			realm = this.create(realm);
		} else {
			realm.setJdbcRealmId(Integer.valueOf(XercesHelper.getNodeValue(element, "jdbcRealmId")));
			realm = load((Integer) getHibernateTemplate().save(realm));
		}
		return realm;
	}

	@Override
	protected void handleCreate(Integer siteId, RealmJdbcValue realmJdbcValue) throws Exception {
		RealmJdbcHbm realm = RealmJdbcHbm.Factory.newInstance();
		realm.setJndiName(realmJdbcValue.getJndiName());
		realm.setStatementRolePerUser(realmJdbcValue.getStatementRolePerUser());
		realm.setStatementUser(realmJdbcValue.getStatementUser());
		realm.setRealmName(realmJdbcValue.getRealmName());
		realm.setLoginPageId(realmJdbcValue.getLoginPageId());
		this.create(realm);
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findBySiteId(final int transform, final java.lang.Integer siteId) {
		return this.findBySiteId(transform, "from de.juwimm.cms.safeguard.model.RealmJdbcHbm as r where r.site.siteId = ?", siteId);
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findByLoginPage(final int transform, final java.lang.String loginPageId) {
		return this.findByLoginPage(transform, "from de.juwimm.cms.safeguard.model.RealmJdbcHbm as r where r.loginPageId = ?", loginPageId);
	}

}