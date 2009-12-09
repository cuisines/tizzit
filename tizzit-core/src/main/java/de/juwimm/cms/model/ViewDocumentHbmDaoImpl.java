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

import de.juwimm.cms.remote.helper.AuthenticationHelper;

/**
 * @see de.juwimm.cms.model.ViewDocumentHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> ,
 *         Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class ViewDocumentHbmDaoImpl extends ViewDocumentHbmDaoBase {
	private static Logger log = Logger.getLogger(ViewDocumentHbmDaoImpl.class);

	@Autowired
	private SequenceHbmDao sequenceHbmDao;

	@Override
	public ViewDocumentHbm create(ViewDocumentHbm viewDocumentHbm) {
		if (viewDocumentHbm.getViewDocumentId() == null || viewDocumentHbm.getViewDocumentId().intValue() == 0) {
			try {
				Integer id = sequenceHbmDao.getNextSequenceNumber("viewdocument.view_document_id");
				viewDocumentHbm.setViewDocumentId(id);
			} catch (Exception e) {
				log.error("Error creating primary key", e);
			}
		}
		if (viewDocumentHbm.getSite() == null) {
			viewDocumentHbm.setSite(getUserHbmDao().load(AuthenticationHelper.getUserName()).getActiveSite());
		}
		if (viewDocumentHbm.getViewComponent() == null) {
			ViewComponentHbm viewComponent = getViewComponentHbmDao().create(viewDocumentHbm, "root", "root", "root", null);
			viewDocumentHbm.setViewComponent(viewComponent);
		}
		try {
			viewDocumentHbm.getViewComponent().setAssignedUnit(viewDocumentHbm.getSite().getRootUnit());
		} catch (Exception e) {
			log.error("An unexpected error occurred while creating a ViewDocument object", e);
		}
		return super.create(viewDocumentHbm);
	}

	@Override
	protected Integer handleCreate(String language, String viewType) throws Exception {
		return create(language, viewType, null, null);
	}

	@Override
	protected Integer handleCreate(String language, String viewType, Integer id, Integer rootViewComponentId) throws Exception {
		ViewDocumentHbm viewDocumentHbm = ViewDocumentHbm.Factory.newInstance();
		if (id == null) {
			try {
				Integer s = sequenceHbmDao.getNextSequenceNumber("viewdocument.view_document_id");
				viewDocumentHbm.setViewDocumentId(s);
			} catch (Exception e) {
				log.error("Error creating primary key", e);
			}
		} else {
			viewDocumentHbm.setViewDocumentId(id);
		}

		viewDocumentHbm.setLanguage(language);
		viewDocumentHbm.setViewType(viewType);

		if (rootViewComponentId != null) {
			ViewComponentHbm viewComponent = getViewComponentHbmDao().load(rootViewComponentId);
			viewDocumentHbm.setViewComponent(viewComponent);
		} else {
			ViewComponentHbm viewComponent = getViewComponentHbmDao().create(viewDocumentHbm, "root", "root", "root", null);
			viewDocumentHbm.setViewComponent(viewComponent);
		}
		viewDocumentHbm.setSite(getUserHbmDao().load(AuthenticationHelper.getUserName()).getActiveSite());
		try {
			viewDocumentHbm.getViewComponent().setAssignedUnit(viewDocumentHbm.getSite().getRootUnit());
		} catch (Exception e) {
			log.error("An unexpected error occurred while creating a ViewDocument object", e);
		}

		getHibernateTemplate().save(viewDocumentHbm);
		return viewDocumentHbm.getViewDocumentId();
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findAll(final int transform, final java.lang.Integer siteId) {
		return this.findAll(transform, "from de.juwimm.cms.model.ViewDocumentHbm as o where o.site.siteId = ?", siteId);
	}

	@Override
	public java.lang.Object findByViewTypeAndLanguage(final int transform, final java.lang.String viewType, final java.lang.String language, final java.lang.Integer siteId) {
		return this.findByViewTypeAndLanguage(transform, "from de.juwimm.cms.model.ViewDocumentHbm vc where vc.viewType = ? and vc.language = ? and vc.site.siteId = ?", viewType, language, siteId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findByViewType(final int transform, final java.lang.String viewType, final java.lang.Integer siteId) {
		return this.findByViewType(transform, "from de.juwimm.cms.model.ViewDocumentHbm as v where v.viewType = ? and v.site.siteId = ?", viewType, siteId);
	}

}