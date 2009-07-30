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
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.safeguard.vo.RealmJaasValue;
import de.juwimm.util.XercesHelper;

/**
 * @see de.juwimm.cms.safeguard.model.RealmJaasHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 *         company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class RealmJaasHbmDaoImpl extends RealmJaasHbmDaoBase {
	private static Logger log = Logger.getLogger(RealmJaasHbmDaoImpl.class);

	@Autowired
	private SequenceHbmDao sequenceHbmDao;
	
	@Override
	public RealmJaasHbm create(RealmJaasHbm realmJaasHbm) {
		try {
			Integer id = sequenceHbmDao.getNextSequenceNumber("realm_jaas.jaas_realm_id");
			realmJaasHbm.setJaasRealmId(id);
		} catch (Exception e) {
			log.error("Error creating primary key", e);
		}
		return super.create(realmJaasHbm);
	}

	@Override
	protected void handleCreate(Element element) throws Exception {
		RealmJaasHbm realm = RealmJaasHbm.Factory.newInstance();
		String name = XercesHelper.getNodeValue(element, "realmName");
		realm.setRealmName(name);
		String loginPageId = XercesHelper.getNodeValue(element, "loginPageId");
		realm.setLoginPageId(loginPageId);
		String jaasPolicyName = XercesHelper.getNodeValue(element, "jaasPolicyName");
		realm.setJaasPolicyName(jaasPolicyName);
		this.create(realm);
	}

	@Override
	protected RealmJaasHbm handleCreate(Element element, boolean newId) throws Exception {
		RealmJaasHbm realm = RealmJaasHbm.Factory.newInstance();
		if (newId) {
			try {
				Integer id = sequenceHbmDao.getNextSequenceNumber("realm_jaas.jaas_realm_id");
				realm.setJaasRealmId(id);
			} catch (Exception e) {
				log.error("Error creating primary key", e);
			}
		} else {
			realm.setJaasRealmId(Integer.valueOf(XercesHelper.getNodeValue(element, "jaasRealmId")));
		}
		String name = XercesHelper.getNodeValue(element, "realmName");
		realm.setRealmName(name);
		String loginPageId = XercesHelper.getNodeValue(element, "loginPageId");
		realm.setLoginPageId(loginPageId);
		String jaasPolicyName = XercesHelper.getNodeValue(element, "jaasPolicyName");
		realm.setJaasPolicyName(jaasPolicyName);
		return load((Integer) getHibernateTemplate().save(realm));
	}

	@Override
	protected void handleCreate(Integer siteId, RealmJaasValue realmJaasValue) throws Exception {
		RealmJaasHbm realm = RealmJaasHbm.Factory.newInstance();
		try {
			SiteHbm site = getSiteHbmDao().load(siteId);
			realm.setSite(site);
		} catch (Exception e) {
			log.error("Error creating new RealmJaasHbm: " + e.getMessage());		
		}
		this.create(realm);
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findBySiteId(final int transform, final java.lang.Integer siteId) {
		return this.findBySiteId(transform, "from de.juwimm.cms.safeguard.model.RealmJaasHbm as r where r.site.siteId = ?", siteId);
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findByLoginPage(final int transform, final java.lang.String loginPageId) {
		return this.findByLoginPage(transform, "from de.juwimm.cms.safeguard.model.RealmJaasHbm as r where r.loginPageId = ?", loginPageId);
	}
}