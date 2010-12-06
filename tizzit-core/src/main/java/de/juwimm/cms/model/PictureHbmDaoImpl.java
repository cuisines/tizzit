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
		}
		pictureHbm = super.create(pictureHbm);
		pictureHbm.setTimeStamp(System.currentTimeMillis());
		return pictureHbm;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findAllPerSite(final int transform, final java.lang.Integer siteId) {
		return this.findAllPerSite(transform, "from de.juwimm.cms.model.PictureHbm as p where p.unit.site.siteId = ?", siteId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findAllPerUnit(final int transform, final java.lang.Integer unitId) {
		return this.findAllPerUnit(transform, "from de.juwimm.cms.model.PictureHbm as p where p.unit.unitId = ?", unitId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findAllPerViewComponent(final int transform, final java.lang.Integer viewComponentId) {
		return this.findAllPerViewComponent(transform, "from de.juwimm.cms.model.PictureHbm as p where p.viewComponent.viewComponentId = ?", viewComponentId);
	}

	@Override
	public java.lang.Object getIdForNameAndUnit(final int transform, final java.lang.Integer unitId, final java.lang.String name) {
		PictureHbm pic = (PictureHbm) this.getIdForNameAndUnit(transform, "from de.juwimm.cms.model.PictureHbm as pictureHbm where pictureHbm.unit.unitId = ? and pictureHbm.pictureName = ?", unitId, name);
		if (pic != null) {
			return pic.getPictureId();
		} else {
			return 0;
		}

	}

	@Override
	public java.lang.Object getIdForNameAndViewComponent(final int transform, final java.lang.String name, final java.lang.Integer viewComponentId) {
		PictureHbm pic = (PictureHbm) this.getIdForNameAndViewComponent(transform, "from de.juwimm.cms.model.PictureHbm as pictureHbm where pictureHbm.pictureName = ? and pictureHbm.viewComponent.viewComponentId = ?", name, viewComponentId);
		if (pic != null) {
			return pic.getPictureId();
		} else {
			return 0;
		}

	}

	@Override
	protected void handleDeletePictures(Integer[] ids) throws Exception {
		if (ids == null || ids.length == 0) {
			return;
		}
		for (Integer pictureId : ids) {
			remove(pictureId);
		}

	}

	@Override
	protected Integer handleClonePicture(Integer oldPictureId, UnitHbm newUnit) throws Exception {
		PictureHbm oldPicture = load(oldPictureId);
		PictureHbm newPicture = PictureHbm.Factory.newInstance();
		newPicture.setAltText(oldPicture.getAltText());
		newPicture.setHeight(oldPicture.getHeight());
		newPicture.setWidth(oldPicture.getWidth());
		newPicture.setMimeType(oldPicture.getMimeType());
		newPicture.setPicture(oldPicture.getPicture());
		newPicture.setPictureName(oldPicture.getPictureName());
		newPicture.setPreview(oldPicture.getPreview());
		newPicture.setThumbnail(oldPicture.getThumbnail());
		newPicture.setThumbnailPopup(oldPicture.isThumbnailPopup());
		/**set the new unit*/
		newPicture.setUnit(newUnit);
		/* or new ViewComponent */

		newPicture.setTitle(oldPicture.getTitle());
		newPicture.setTimeStamp(oldPicture.getTimeStamp());
		newPicture = create(newPicture);
		return newPicture.getPictureId();
	}
}
