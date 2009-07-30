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

/**
 * @see de.juwimm.cms.model.PictureHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> ,
 *         Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class PictureHbmDaoImpl extends PictureHbmDaoBase {
	private static Logger log = Logger.getLogger(PictureHbmDaoImpl.class);

	@Autowired
	private SequenceHbmDao sequenceHbmDao;
	
	@Override
	public PictureHbm create(PictureHbm pictureHbm) {
		if (pictureHbm.getPictureId() == null || pictureHbm.getPictureId().intValue() == 0) {
			try {
				Integer id = sequenceHbmDao.getNextSequenceNumber("picture.picture_id");
				pictureHbm.setPictureId(id);
			} catch (Exception e) {
				log.error("Error creating primary key", e);
			}
			pictureHbm = super.create(pictureHbm);
			pictureHbm.setTimeStamp(System.currentTimeMillis());
		}
		return pictureHbm;
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findAllPerSite(final int transform, final java.lang.Integer siteId) {
		return this.findAllPerSite(transform, "from de.juwimm.cms.model.PictureHbm as p where p.unit.site.siteId = ?", siteId);
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findAllPerUnit(final int transform, final java.lang.Integer unitId) {
		return this.findAllPerUnit(transform, "from de.juwimm.cms.model.PictureHbm as p where p.unit.unitId = ?", unitId);
	}
}
