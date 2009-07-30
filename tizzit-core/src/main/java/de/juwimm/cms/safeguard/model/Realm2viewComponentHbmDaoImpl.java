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
import org.w3c.dom.Element;

import de.juwimm.cms.model.SequenceHbmDao;
import de.juwimm.cms.model.ViewComponentHbm;
import de.juwimm.cms.safeguard.vo.RealmJaasValue;
import de.juwimm.cms.safeguard.vo.RealmJdbcValue;
import de.juwimm.cms.safeguard.vo.RealmLdapValue;
import de.juwimm.cms.safeguard.vo.RealmSimplePwValue;
import de.juwimm.util.XercesHelper;

/**
 * @see de.juwimm.cms.safeguard.model.Realm2viewComponentHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class Realm2viewComponentHbmDaoImpl extends Realm2viewComponentHbmDaoBase {
	private static Logger log = Logger.getLogger(Realm2viewComponentHbmDaoImpl.class);

	@Autowired
	private SequenceHbmDao sequenceHbmDao;
	
	@Override
	public Realm2viewComponentHbm create(Realm2viewComponentHbm realm2viewComponentHbm) {
		try {
			Integer id = sequenceHbmDao.getNextSequenceNumber("realm2view_component.realm2view_component_id");
			realm2viewComponentHbm.setRealm2viewComponentId(id);
		} catch (Exception e) {
			log.error("Error creating primary key", e);
		}
		return super.create(realm2viewComponentHbm);
	}

	@Override
	protected void handleCreate(Element element, Integer viewComponentId) throws Exception {
		Realm2viewComponentHbm realm = Realm2viewComponentHbm.Factory.newInstance();
		String neededrole = XercesHelper.getNodeValue(element, "roleNeeded");
		Integer id = new Integer(XercesHelper.getNodeValue(element, "realm2viewComponentId"));
		realm.setRealm2viewComponentId(id);
		if (neededrole != null && (neededrole.length() == 0 || "null".equalsIgnoreCase(neededrole))) {
			neededrole = null;
		}
		realm.setRoleNeeded(neededrole);
		getHibernateTemplate().save(realm);
	}

	@Override
	protected void handleCreate(RealmSimplePwValue realmSimplePwValue, Integer viewComponentId, String roleNeeded) throws Exception {
		Realm2viewComponentHbm realm = Realm2viewComponentHbm.Factory.newInstance();
		ViewComponentHbm vc = getViewComponentHbmDao().load(viewComponentId);
		realm.setViewComponent(vc);
		RealmSimplePwHbm simpleRealm = getRealmSimplePwHbmDao().load(realmSimplePwValue.getSimplePwRealmId());
		realm.setSimplePwRealm(simpleRealm);
		if (roleNeeded != null && (roleNeeded.length() == 0 || "null".equalsIgnoreCase(roleNeeded))) {
			roleNeeded = null;
		}
		realm.setRoleNeeded(roleNeeded);
		this.create(realm);
	}

	@Override
	protected void handleCreate(RealmJdbcValue realmJdbcValue, Integer viewComponentId, String roleNeeded) throws Exception {
		Realm2viewComponentHbm realm = Realm2viewComponentHbm.Factory.newInstance();
		ViewComponentHbm vc = getViewComponentHbmDao().load(viewComponentId);
		realm.setViewComponent(vc);
		if (roleNeeded != null && (roleNeeded.length() == 0 || "null".equalsIgnoreCase(roleNeeded))) {
			roleNeeded = null;
		}
		RealmJdbcHbm jdbcRealm = getRealmJdbcHbmDao().load(realmJdbcValue.getJdbcRealmId());
		realm.setJdbcRealm(jdbcRealm);
		this.create(realm);
	}

	@Override
	protected void handleCreate(RealmLdapValue realmLdapValue, Integer viewComponentId, String roleNeeded) throws Exception {
		Realm2viewComponentHbm realm = Realm2viewComponentHbm.Factory.newInstance();
		ViewComponentHbm vc = getViewComponentHbmDao().load(viewComponentId);
		realm.setViewComponent(vc);
		if (roleNeeded != null && (roleNeeded.length() == 0 || "null".equalsIgnoreCase(roleNeeded))) {
			roleNeeded = null;
		}
		RealmLdapHbm ldapRealm = getRealmLdapHbmDao().load(realmLdapValue.getLdapRealmId());
		realm.setLdapRealm(ldapRealm);
		this.create(realm);
	}

	@Override
	protected void handleCreate(RealmJaasValue realmJaasValue, Integer viewComponentId, String roleNeeded) throws Exception {
		Realm2viewComponentHbm realm = Realm2viewComponentHbm.Factory.newInstance();
		ViewComponentHbm vc = getViewComponentHbmDao().load(viewComponentId);
		realm.setViewComponent(vc);
		if (roleNeeded != null && (roleNeeded.length() == 0 || "null".equalsIgnoreCase(roleNeeded))) {
			roleNeeded = null;
		}
		RealmJaasHbm jaasRealm = getRealmJaasHbmDao().load(realmJaasValue.getJaasRealmId());
		realm.setJaasRealm(jaasRealm);
		this.create(realm);
	}

	public java.lang.Object findByViewComponent(final int transform, final java.lang.Integer viewComponentId)
    {
        return this.findByViewComponent(transform, "from de.juwimm.cms.safeguard.model.Realm2viewComponentHbm as r where r.viewComponent.viewComponentId = ?", viewComponentId);
    }
	
	@SuppressWarnings("unchecked")
	public java.util.Collection findByLoginPage(final int transform, final java.lang.Integer viewComponentId)
    {
        return this.findByLoginPage(transform, "from de.juwimm.cms.safeguard.model.Realm2viewComponentHbm as r where r.loginPage.viewComponentId = ?", viewComponentId);
    }
	
	
}