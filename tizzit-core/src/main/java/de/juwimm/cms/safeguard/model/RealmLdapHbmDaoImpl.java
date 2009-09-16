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
import de.juwimm.cms.safeguard.vo.RealmLdapValue;

/**
 * @see de.juwimm.cms.safeguard.model.RealmLdapHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class RealmLdapHbmDaoImpl extends RealmLdapHbmDaoBase {
	private static Logger log = Logger.getLogger(RealmLdapHbmDaoImpl.class);

	@Autowired
	private SequenceHbmDao sequenceHbmDao;
	
	@Override
	public RealmLdapHbm create(RealmLdapHbm realmLdapHbm) {
		try {
			Integer id = sequenceHbmDao.getNextSequenceNumber("realmldap.ldap_realm_id");
			realmLdapHbm.setLdapRealmId(id);
		} catch (Exception e) {
			log.error("Error creating primary key", e);
		}
		return super.create(realmLdapHbm);
	}

	@Override
	protected void handleCreate(Element element) throws Exception {
		RealmLdapHbm realm = RealmLdapHbm.Factory.newInstance();
		String name = XercesHelper.getNodeValue(element, "realmName");
		realm.setRealmName(name);
		String loginPageId = XercesHelper.getNodeValue(element, "loginPageId");
		realm.setLoginPageId(loginPageId);
		String ldapPrefix = XercesHelper.getNodeValue(element, "ldapPrefix");
		realm.setLdapPrefix(ldapPrefix);
		String ldapSuffix = XercesHelper.getNodeValue(element, "ldapSuffix");
		realm.setLdapSuffix(ldapSuffix);
		String ldapUrl = XercesHelper.getNodeValue(element, "ldapUrl");
		realm.setLdapUrl(ldapUrl);
		String ldapAuthenticationType = XercesHelper.getNodeValue(element, "ldapAuthenticationType");
		realm.setLdapAuthenticationType(ldapAuthenticationType);
		this.create(realm);
	}

	@Override
	protected RealmLdapHbm handleCreate(Element element, boolean newId) throws Exception {
		RealmLdapHbm realm = RealmLdapHbm.Factory.newInstance();
		String name = XercesHelper.getNodeValue(element, "realmName");
		realm.setRealmName(name);
		String loginPageId = XercesHelper.getNodeValue(element, "loginPageId");
		realm.setLoginPageId(loginPageId);
		String ldapPrefix = XercesHelper.getNodeValue(element, "ldapPrefix");
		realm.setLdapPrefix(ldapPrefix);
		String ldapSuffix = XercesHelper.getNodeValue(element, "ldapSuffix");
		realm.setLdapSuffix(ldapSuffix);
		String ldapUrl = XercesHelper.getNodeValue(element, "ldapUrl");
		realm.setLdapUrl(ldapUrl);
		String ldapAuthenticationType = XercesHelper.getNodeValue(element, "ldapAuthenticationType");
		realm.setLdapAuthenticationType(ldapAuthenticationType);
		if (newId) {
			realm = this.create(realm);
		} else {
			realm.setLdapRealmId(Integer.valueOf(XercesHelper.getNodeValue(element, "ldapRealmId")));
			realm = load((Integer) getHibernateTemplate().save(realm));
		}
		return realm;
	}

	@Override
	protected void handleCreate(Integer siteId, RealmLdapValue realmLdapValue) throws Exception {
		RealmLdapHbm realm = RealmLdapHbm.Factory.newInstance();
		realm.setLdapAuthenticationType(realmLdapValue.getLdapAuthenticationType());
		realm.setRealmName(realmLdapValue.getRealmName());
		realm.setLdapPrefix(realmLdapValue.getLdapPrefix());
		realm.setLdapSuffix(realmLdapValue.getLdapSuffix());
		realm.setLdapUrl(realmLdapValue.getLdapUrl());
		realm.setLoginPageId(realmLdapValue.getLoginPageId());
		this.create(realm);
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findBySiteId(final int transform, final java.lang.Integer siteId) {
		return this.findBySiteId(transform, "from de.juwimm.cms.safeguard.model.RealmLdapHbm as r where r.site.siteId = ?", siteId);
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findByLoginPage(final int transform, final java.lang.String loginPageId) {
		return this.findByLoginPage(transform, "from de.juwimm.cms.safeguard.model.RealmLdapHbm as r where r.loginPageId = ?", loginPageId);
	}

}