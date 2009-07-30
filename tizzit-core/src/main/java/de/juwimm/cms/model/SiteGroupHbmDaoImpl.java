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

import java.util.HashSet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.vo.SiteGroupValue;

/**
 * @see de.juwimm.cms.model.SiteGroupHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 *         company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class SiteGroupHbmDaoImpl extends SiteGroupHbmDaoBase {
	private static Logger log = Logger.getLogger(SiteGroupHbmDaoImpl.class);

	@Autowired
	private SequenceHbmDao sequenceHbmDao;
	
	@Override
	public SiteGroupHbm create(SiteGroupHbm siteGroupHbm) {
		if (siteGroupHbm.getSiteGroupId() == null || siteGroupHbm.getSiteGroupId().intValue() == 0) {
			try {
				Integer id = sequenceHbmDao.getNextSequenceNumber("sitegroup.site_group_id");
				siteGroupHbm.setSiteGroupId(id);
			} catch (Exception e) {
				log.error("Error creating primary key", e);
			}
		}
		return super.create(siteGroupHbm);
	}

	@Override
	protected void handleSetSiteGroupValue(SiteGroupValue value, SiteGroupHbm siteGroup) throws Exception {
		if (value != null) {
			siteGroup.setSiteGroupName(value.getSiteGroupName());
			if (value.getSiteValues() != null) {
				siteGroup.setSites(new HashSet());
				for (int i = value.getSiteValues().length - 1; i >= 0; i--) {
					try {
						SiteHbm site = getSiteHbmDao().load(value.getSiteValues()[i].getSiteId());
						siteGroup.getSites().add(site);
					} catch (Exception e) {
						log.warn("Error finding site: " + e.getMessage(), e);
					}
				}
			} else {
				siteGroup.setSites(new HashSet());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findAll(final int transform) {
		return this.findAll(transform, "from de.juwimm.cms.model.SiteGroupHbm as siteGroupHbm");
	}

}
