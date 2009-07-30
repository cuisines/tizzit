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

import java.sql.Blob;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.lob.BlobImpl;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.search.beans.SearchengineDeleteService;

/**
 * @see de.juwimm.cms.model.DocumentHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> ,
 *         Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class DocumentHbmDaoImpl extends de.juwimm.cms.model.DocumentHbmDaoBase {
	private static Log log = LogFactory.getLog(DocumentHbmDaoImpl.class);

	@Autowired
	private SearchengineDeleteService searchengineDeleteService;
	
	@Autowired
	private SequenceHbmDao sequenceHbmDao;
	
	@Override
	public DocumentHbm create(DocumentHbm documentHbm) {
		if (documentHbm.getDocumentId() == null || documentHbm.getDocumentId().intValue() == 0) {
			try {
				Integer id = sequenceHbmDao.getNextSequenceNumber("document.document_id");
				documentHbm.setDocumentId(id);
			} catch (Exception e) {
				log.error("Error creating primary key", e);
			}
		}
		documentHbm.setUpdateSearchIndex(true);
		documentHbm.setTimeStamp(System.currentTimeMillis());
		return super.create(documentHbm);
	}

	@Override
	protected Integer handleCreate() throws Exception {
		DocumentHbm documentHbm = DocumentHbm.Factory.newInstance();
		return create(documentHbm).getDocumentId();
	}

	@Override
	protected Integer handleCreate(byte[] document, String documentName, String mimeType) throws Exception {
		DocumentHbm documentHbm = DocumentHbm.Factory.newInstance();
		Blob data = new BlobImpl(document);
		documentHbm.setDocument(data);
		documentHbm.setDocumentName(documentName);
		documentHbm.setMimeType(mimeType);
		documentHbm.setUpdateSearchIndex(true);
		return this.create(documentHbm).getDocumentId();
	}

	@Override
	protected Integer handleCreate(byte[] document, String documentName, String mimeType, Integer id) throws Exception {
		DocumentHbm documentHbm = DocumentHbm.Factory.newInstance();
		Blob data = new BlobImpl(document);
		documentHbm.setDocument(data);
		documentHbm.setDocumentName(documentName);
		documentHbm.setMimeType(mimeType);
		documentHbm.setDocumentId(id);
		documentHbm.setUpdateSearchIndex(true);
		return this.create(documentHbm).getDocumentId();
	}

	@Override
	public void remove(DocumentHbm documentHbm) {
		searchengineDeleteService.deleteDocument(documentHbm.getDocumentId(), documentHbm.getUnit().getSite().getSiteId());
		super.remove(documentHbm);
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findAll(final int transform, final java.lang.Integer unitId) {
		return this.findAll(transform, "from de.juwimm.cms.model.DocumentHbm as d where d.unit.unitId = ?", unitId);
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findAllPerSite(final int transform, final java.lang.Integer siteId) {
		return this.findAllPerSite(transform, "from de.juwimm.cms.model.DocumentHbm as d where d.unit.site.siteId = ?", siteId);
	}
	
	@SuppressWarnings("unchecked")
	public java.util.Collection findAllPerUnit(final int transform, final java.lang.Integer unitId) {
		return this.findAllPerUnit(transform, "from de.juwimm.cms.model.DocumentHbm as d where d.unit.unitId = ?", unitId);
	}
}
