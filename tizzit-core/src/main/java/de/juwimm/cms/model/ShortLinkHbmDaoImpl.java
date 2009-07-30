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
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.vo.ShortLinkValue;

/**
 * @see de.juwimm.cms.model.ShortLinkHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 *         company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class ShortLinkHbmDaoImpl extends ShortLinkHbmDaoBase {
	private static Logger log = Logger.getLogger(ShortLinkHbmDaoImpl.class);

	@Autowired
	private SequenceHbmDao sequenceHbmDao;
	
	@Override
	public ShortLinkHbm create(ShortLinkHbm shortLinkHbm) {
		if (shortLinkHbm.getShortLinkId() == null || shortLinkHbm.getShortLinkId().intValue() == 0) {
			try {
				Integer id = sequenceHbmDao.getNextSequenceNumber("shortlink.short_link_id");
				shortLinkHbm.setShortLinkId(id);
			} catch (Exception e) {
				log.error("Error creating primary key", e);
			}
		}
		return super.create(shortLinkHbm);
	}

	@Override
	protected void handleSetShortLinkValue(ShortLinkValue shortLinkValue, ShortLinkHbm shortLink) throws Exception {
		shortLink.setShortLink(shortLinkValue.getShortLink());
		shortLink.setRedirectUrl(shortLinkValue.getRedirectUrl());
		try {
			if (shortLinkValue.getSiteId() != null) {
				SiteHbm site = getSiteHbmDao().load(shortLinkValue.getSiteId());
				shortLink.setSite(site);
			}
			if (shortLinkValue.getViewDocumentId() != null) {
				ViewDocumentHbm viewDocument = getViewDocumentHbmDao().load(shortLinkValue.getViewDocumentId());
				shortLink.setViewDocument(viewDocument);
			}
		} catch (Exception e) {
			log.error("Error setting relations in setShortLinkValue: " + e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findAll(final int transform, final java.lang.Integer siteId) {
		return this.findAll(transform, "from de.juwimm.cms.model.ShortLinkHbm as s where s.site.siteId = ?", siteId);
	}

	public java.lang.Object findByShortLink(final int transform, final java.lang.String shortLink, final java.lang.Integer siteId) {
		return this.findByShortLink(transform, "from de.juwimm.cms.model.ShortLinkHbmImpl as sl where sl.shortLink = ? and sl.site.siteId = ?", shortLink, siteId);
	}
}
