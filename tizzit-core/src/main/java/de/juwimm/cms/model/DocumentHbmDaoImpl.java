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
import org.tizzit.util.Base64;

import com.ibm.icu.text.SimpleDateFormat;

import de.juwimm.cms.beans.BlobJdbcDao;
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

	@Autowired
	private BlobJdbcDao blobJdbcDao;

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
	protected Integer handleCreate(byte[] document, String documentName, String mimeType, UnitHbm unit) throws Exception {
		DocumentHbm documentHbm = DocumentHbm.Factory.newInstance();
		Blob data = new BlobImpl(document);
		documentHbm.setDocument(data);
		documentHbm.setDocumentName(documentName);
		documentHbm.setMimeType(mimeType);
		documentHbm.setUnit(unit);
		documentHbm.setUpdateSearchIndex(true);
		return this.create(documentHbm).getDocumentId();
	}

	@Override
	protected Integer handleCreate(byte[] document, String documentName, String mimeType, UnitHbm unit, Integer id) throws Exception {
		DocumentHbm documentHbm = DocumentHbm.Factory.newInstance();
		Blob data = new BlobImpl(document);
		documentHbm.setDocument(data);
		documentHbm.setDocumentName(documentName);
		documentHbm.setMimeType(mimeType);
		documentHbm.setUnit(unit);
		documentHbm.setDocumentId(id);
		documentHbm.setUpdateSearchIndex(true);
		return this.create(documentHbm).getDocumentId();
	}

	@Override
	public void remove(DocumentHbm documentHbm) {
		super.remove(documentHbm);
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findAll(final int transform, final java.lang.Integer unitId) {
		return this.findAll(transform, "from de.juwimm.cms.model.DocumentHbm as d where d.unit.unitId = ?", unitId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findAllPerSite(final int transform, final java.lang.Integer siteId) {
		return this.findAllPerSite(transform, "from de.juwimm.cms.model.DocumentHbm as d where d.unit.site.siteId = ?", siteId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findAllPerUnit(final int transform, final java.lang.Integer unitId) {
		return this.findAllPerUnit(transform, "from de.juwimm.cms.model.DocumentHbm as d where d.unit.unitId = ?", unitId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findAllPerViewComponent(final int transform, final java.lang.Integer viewComponentId) {
		return this.findAllPerViewComponent(transform, "from de.juwimm.cms.model.DocumentHbm as d where d.viewComponent.viewComponentId = ?", viewComponentId);
	}

	@Override
	protected byte[] handleGetDocumentContent(Integer documentId) throws Exception {
		return blobJdbcDao.getDocumentContent(documentId);
	}

	@Override
	public java.lang.Object getIdForNameAndUnit(final int transform, final java.lang.String name, final java.lang.Integer unitId) {
		DocumentHbm docHbm = (DocumentHbm) this.getIdForNameAndUnit(transform, "from de.juwimm.cms.model.DocumentHbm as documentHbm where documentHbm.documentName = ? and documentHbm.unit.unitId = ?", name, unitId);
		if (docHbm != null) {
			return docHbm.getDocumentId();
		} else {
			return 0;
		}
	}

	@Override
	public java.lang.Object getIdForNameAndViewComponent(final int transform, final java.lang.String name, final java.lang.Integer viewComponentIdId) {
		DocumentHbm docHbm = (DocumentHbm) this.getIdForNameAndUnit(transform, "from de.juwimm.cms.model.DocumentHbm as documentHbm where documentHbm.documentName = ? and documentHbm.viewComponent.viewComponentId = ?", name, viewComponentIdId);
		if (docHbm != null) {
			return docHbm.getDocumentId();
		} else {
			return 0;
		}
	}

	@Override
	protected boolean handleSetDocumentContent(Integer idDocument, byte[] blob) throws Exception {
		try {
			blobJdbcDao.setDocumentContent(idDocument, blob);
			return true;
		} catch (Exception e) {
			log.error("Error setting document content");
			return false;
		}

	}

	@Override
	protected void handleDeleteDocuments(Integer[] ids) throws Exception {
		if (ids == null || ids.length == 0) { return; }
		for (Integer documentId : ids) {
			remove(documentId);
		}
	}

	@Override
	protected Integer handleCloneDocument(Integer oldDocumentId, UnitHbm newUnit) throws Exception {
		DocumentHbm oldDocument = load(oldDocumentId);
		DocumentHbm newDocument = new DocumentHbmImpl();
		newDocument.setDocumentName(oldDocument.getDocumentName());
		newDocument.setMimeType(oldDocument.getMimeType());
		newDocument.setTimeStamp(oldDocument.getTimeStamp());
		newDocument.setUnit(newUnit);
		newDocument = create(newDocument);
		byte[] blob = getDocumentContent(oldDocumentId);
		setDocumentContent(newDocument.getDocumentId(), blob);
		return newDocument.getDocumentId();
	}

	/**
	 * document name, document id, document last modified and mime type.
	 * - label (text) 
 	 * - description (litte text field) 
	 * - checkbox (show document in search? yes/no)
	 */
	@Override
	public String handleToXml(Integer documentId, int tabdepth) {
		DocumentHbm document = this.load(documentId);
		StringBuffer sb = new StringBuffer();
		sb.append("<document id=\"");
		sb.append(document.getDocumentId());
		sb.append("\" mimeType=\"");
		sb.append(document.getMimeType());
		if (document.getUnit() == null) {
			sb.append("\" viewComponentId=\"");
			sb.append(document.getViewComponent().getViewComponentId());
		} else {
			sb.append("\" unitId=\"");
			sb.append(document.getUnit().getUnitId());
		}
		sb.append("\" label=\"");
		sb.append(document.getLabel());
		sb.append("\" description=\"");
		sb.append(document.getDescription());
		sb.append("\" searchable=\"");
		sb.append(document.isSearchable());
		sb.append("\" lastModified=\"");
		Date date =new Date(document.getTimeStamp());
		sb.append(new SimpleDateFormat().format(date));
		sb.append("\" documentName=\"");
		sb.append(document.getDocumentName());

		sb.append("\">\n");
		byte[] data = blobJdbcDao.getDocumentContent(document.getDocumentId());

		if (data == null || data.length == 0) {
			sb.append("\t<file></file>\n");
		} else {
			sb.append("\t<file>").append(Base64.encodeBytes(data)).append("</file>\n");
		}

		sb.append("\t<name><![CDATA[" + document.getDocumentName() + "]]></name>\n");
		sb.append("</document>\n");
		return sb.toString();
	}


}
