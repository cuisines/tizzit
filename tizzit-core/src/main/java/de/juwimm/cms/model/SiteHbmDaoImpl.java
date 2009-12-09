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
package de.juwimm.cms.model;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @see de.juwimm.cms.model.SiteHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 *         company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class SiteHbmDaoImpl extends SiteHbmDaoBase {
	private static final Logger log = Logger.getLogger(SiteHbmDaoImpl.class);

	@Autowired
	private SequenceHbmDao sequenceHbmDao;

	@Override
	public SiteHbm create(SiteHbm siteHbm) {
		try {
			UnitHbm unitHbm = null;
			if (siteHbm.getSiteId() == null || siteHbm.getSiteId().intValue() == 0) {
				Integer id = sequenceHbmDao.getNextSequenceNumber("site.site_id");
				siteHbm.setSiteId(id);
			}
			if (siteHbm.getRootUnit() == null) {
				unitHbm = UnitHbm.Factory.newInstance();
				unitHbm.setName("rootUnit " + siteHbm.getShortName());
				unitHbm = getUnitHbmDao().create(unitHbm);
				siteHbm.setRootUnit(unitHbm);
				siteHbm.setLastModifiedDate(System.currentTimeMillis());
			}
			if (siteHbm.getLastModifiedDate() == 0) {
				siteHbm.setLastModifiedDate(System.currentTimeMillis());
			}
			siteHbm = super.create(siteHbm);
			if (siteHbm.getRootUnit().getSite() == null && unitHbm != null) {
				unitHbm.setSite(siteHbm);
				getUnitHbmDao().update(unitHbm);
			}
			return siteHbm;
		} catch (Exception e) {
			log.error("Error creating site: " + e.getMessage(), e);
		}
		return null;
	}

	@Override
	protected void handleResetDocumentUseCount(Integer siteId) throws Exception {
		try {
			Session session = getSessionFactory().getCurrentSession();
			Query query = session.createQuery("UPDATE de.juwimm.cms.model.DocumentHbm d SET d.useCountPublishVersion = 0, d.useCountLastVersion = 0 " + "WHERE d.unit.unitId IN (SELECT u.unitId FROM de.juwimm.cms.model.UnitHbm u WHERE u.site.siteId = ?)");
			query.setInteger(0, siteId.intValue());
			query.executeUpdate();
		} catch (Exception e) {
			log.error("Error reseting document use-count for site " + siteId + ": " + e.getMessage(), e);
		}
	}

	@Override
	protected long handleGetMaxLastModifiedDate() throws Exception {
		try {
			Session session = getSessionFactory().getCurrentSession();
			Query query = session.createQuery("SELECT MAX(s.lastModifiedDate) FROM de.juwimm.cms.model.SiteHbm s");
			return ((Long) query.uniqueResult()).longValue();
		} catch (Exception e) {
			log.error("Error getting max(lastModifiedDate) for all sites : " + e.getMessage(), e);
		}
		return 0L;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findAll(final int transform) {
		return this.findAll(transform, "from de.juwimm.cms.model.SiteHbm as siteHbm");
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findAll4User(final int transform, final java.lang.String userId) {
		return this.findAll4User(transform, "select s from de.juwimm.cms.model.SiteHbm s inner join s.users u where u.userId = ?", userId);
	}

	@Override
	public java.lang.Object findByName(final int transform, final java.lang.String name) {
		return this.findByName(transform, "from de.juwimm.cms.model.SiteHbm as s where s.name = ?", name);
	}

	@Override
	public java.lang.Object findByShort(final int transform, final java.lang.String shortName) {
		return this.findByShort(transform, "from de.juwimm.cms.model.SiteHbm s where s.shortName = ?", shortName);
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findAllNotAssigned2SiteGroups(final int transform) {
		return this.findAllNotAssigned2SiteGroups(transform, "from de.juwimm.cms.model.SiteHbm as s where s.siteGroup is null");
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findAll4SiteGroup(final int transform, final java.lang.Integer siteGroupId) {
		return this.findAll4SiteGroup(transform, "from de.juwimm.cms.model.SiteHbm as s where s.siteGroup.siteGroupId = ?", siteGroupId);
	}

}
