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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Element;

import de.juwimm.cms.remote.helper.AuthenticationHelper;
import de.juwimm.cms.vo.ContentValue;

/**
 * @see de.juwimm.cms.model.ContentHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> ,
 *         Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class ContentHbmDaoImpl extends ContentHbmDaoBase {
	private static Logger log = Logger.getLogger(ContentHbmDaoImpl.class);

	@Autowired
	private SequenceHbmDao sequenceHbmDao;

	@Override
	protected ContentHbm handleCreate(ContentValue content, String creator) throws Exception {
		ContentHbm contentHbm = ContentHbm.Factory.newInstance();
		if (contentHbm.getContentId() == null || contentHbm.getContentId().intValue() == 0) {
			contentHbm.setContentId(sequenceHbmDao.getNextSequenceNumber("content.content_id"));
		} else {
			contentHbm.setContentId(content.getContentId());
		}
		contentHbm.setStatus(content.getStatus());
		contentHbm.setTemplate(content.getTemplate());

		//   removeSpareContentVersions();
		return this.create(contentHbm);
	}

	/**
	 * @see de.juwimm.cms.model.ContentHbm#setContent(java.lang.String)
	 */
	@Override
	public void handleSetContent(ContentHbm content, String ct) {
		ContentVersionHbm contentVersion = content.getLastContentVersion();
		if (contentVersion != null) {
			updateDocumentUseCountLastVersion(contentVersion.getText(), ct);
			contentVersion.setText(ct);
		}
		// -- Indexing through No-Messaging
		content.setUpdateSearchIndex(true);
	}

	/**
	 * @see de.juwimm.cms.model.ContentHbmDao#updateDocumentUseCountLastVersion(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	protected void handleUpdateDocumentUseCountLastVersion(String oldContentText, String newContentText) {
		HashMap<Integer, Integer> deltaMap = this.getDeltaDocumentCounts(oldContentText, newContentText);
		this.updateDocumentUseCounts(deltaMap, true);
	}

	/**
	 * @see de.juwimm.cms.model.ContentHbmDao#updateDocumentUseCountPublishVersion(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	protected void handleUpdateDocumentUseCountPublishVersion(String oldContentText, String newContentText) {
		HashMap<Integer, Integer> deltaMap = this.getDeltaDocumentCounts(oldContentText, newContentText);
		this.updateDocumentUseCounts(deltaMap, false);
	}

	private HashMap<Integer, Integer> getDeltaDocumentCounts(String oldContentText, String newContentText) {
		HashMap<Integer, Integer> deltaMap = new HashMap<Integer, Integer>();

		if (oldContentText != null && !"".equalsIgnoreCase(oldContentText)) {
			deltaMap = this.getDocumentCountWrapper(deltaMap, oldContentText, true).getDeltaDocuments();
		}
		if (newContentText != null && !"".equalsIgnoreCase(newContentText)) {
			deltaMap = this.getDocumentCountWrapper(deltaMap, newContentText, false).getDeltaDocuments();
		}

		return deltaMap;
	}

	private DocumentCountWrapper getDocumentCountWrapper(HashMap<Integer, Integer> startMap, String contentText, boolean isOld) {
		DocumentCountWrapper dcw = new DocumentCountWrapper(startMap);

		org.w3c.dom.Document contentNode = null;
		try {
			contentNode = XercesHelper.string2Dom(contentText);
		} catch (Exception e) {
			if (log.isDebugEnabled()) log.debug("Content is not valid XML!");
		}
		if (contentNode != null) {
			Iterator it = XercesHelper.findNodes(contentNode, "//document");
			while (it.hasNext()) {
				Element doc = (Element) it.next();
				Integer docId = null;
				try {
					docId = Integer.valueOf(doc.getAttribute("src"));
				} catch (NumberFormatException nfe) {
					if (log.isDebugEnabled()) log.debug(doc.getAttribute("src") + " is not a valid Number (DocumentId)!");
				}
				if (docId != null) {
					dcw.addDocument(docId, new Integer(isOld ? -1 : +1));
				}
			}
		}

		return dcw;
	}

	public class DocumentCountWrapper {
		private HashMap<Integer, Integer> deltaDocuments = null;

		public DocumentCountWrapper(HashMap<Integer, Integer> startMap) {
			this.deltaDocuments = startMap;
		}

		public HashMap<Integer, Integer> getDeltaDocuments() {
			return deltaDocuments;
		}

		public void addDocument(Integer docId, Integer docDelta) {
			Integer docCount = this.deltaDocuments.get(docId);
			if (docCount == null) docCount = new Integer(0);
			docCount = new Integer(docCount.intValue() + docDelta.intValue());
			this.deltaDocuments.put(docId, docCount);
		}
	}

	private void updateDocumentUseCounts(HashMap<Integer, Integer> deltaMap, boolean isLastVersion) {
		Iterator<Integer> it = deltaMap.keySet().iterator();
		while (it.hasNext()) {
			Integer docId = it.next();
			Integer docCountDelta = deltaMap.get(docId);
			if (docCountDelta != null && docCountDelta.intValue() != 0) {
				try {
					DocumentHbm doc = getDocumentHbmDao().load(docId);
					if (isLastVersion) {
						int newCount = doc.getUseCountLastVersion() + docCountDelta.intValue();
						if (newCount < 0) newCount = 0;
						doc.setUseCountLastVersion(newCount);
					} else {
						int newCount = doc.getUseCountPublishVersion() + docCountDelta.intValue();
						if (newCount < 0) newCount = 0;
						doc.setUseCountPublishVersion(newCount);
					}
				} catch (Exception e) {
					log.error("Error updating documentCount: " + e.getMessage(), e);
				}
			}
		}
	}

	@Override
	protected void handleSetLatestContentVersionAsPublishVersion(Integer contentId) throws Exception {
		ContentHbm content = super.load(contentId);
		ContentVersionHbm latestVersion = content.getLastContentVersion();
		ContentVersionHbm publsVersion = content.getContentVersionForPublish();
		if (publsVersion != null) {
			String oldPublishContentText = publsVersion.getText() == null ? null : publsVersion.getText();
			this.updateDocumentUseCountPublishVersion(oldPublishContentText, latestVersion.getText());
			publsVersion.setText(latestVersion.getText());
			publsVersion.setHeading(latestVersion.getHeading());
			publsVersion.setCreateDate(System.currentTimeMillis());
			publsVersion.setCreator(AuthenticationHelper.getUserName());
		} else {
			publsVersion = ContentVersionHbm.Factory.newInstance();
			publsVersion.setCreateDate(System.currentTimeMillis());
			publsVersion.setText(latestVersion.getText());
			publsVersion.setHeading(latestVersion.getHeading());
			publsVersion.setCreator(AuthenticationHelper.getUserName());
			publsVersion.setVersion("PUBLS");
			publsVersion = super.getContentVersionHbmDao().create(publsVersion);
			content.getContentVersions().add(publsVersion);
		}
		// -- Indexing through Messaging
		if (publsVersion.getText() != null) {
			content.setUpdateSearchIndex(true);
			// -- Indexing
		}
	}

	/**
	 * @see de.juwimm.cms.model.ContentHbm#toXml(int)
	 */
	@Override
	public String handleToXml(ContentHbm content) {
		StringBuilder sb = new StringBuilder();
		sb.append("<content id=\"");
		sb.append(content.getContentId());
		sb.append("\">\n");
		sb.append("\t<template>").append(content.getTemplate()).append("</template>\n");
		sb.append("\t<status>").append(content.getStatus()).append("</status>\n");

		Collection coll = content.getContentVersions();
		Iterator it = coll.iterator();
		while (it.hasNext()) {
			ContentVersionHbm cvl = (ContentVersionHbm) it.next(); // getContentVersionForPublish();
			if (cvl != null) {
				sb.append(getContentVersionHbmDao().toXml(cvl));
			}
		}

		sb.append("</content>\n");
		return sb.toString();
	}

	@Override
	protected ContentHbm handleCreateWithContentVersion(ContentValue contentValue, String creator) throws Exception {
		ContentHbm contentHbm = ContentHbm.Factory.newInstance();
		if (contentHbm.getContentId() == null || contentHbm.getContentId().intValue() == 0) {
			contentHbm.setContentId(sequenceHbmDao.getNextSequenceNumber("content.content_id"));
		} else {
			contentHbm.setContentId(contentValue.getContentId());
		}
		contentHbm.setStatus(contentValue.getStatus());
		contentHbm.setTemplate(contentValue.getTemplate());
		ContentHbm content = this.create(contentHbm);

		ContentVersionHbm cv = ContentVersionHbm.Factory.newInstance();
		cv.setVersion("1");
		cv.setText(contentValue.getContentText());
		cv.setHeading(contentValue.getHeading());
		cv = getContentVersionHbmDao().create(cv);
		content.getContentVersions().add(cv);

		return content;
	}

	@Override
	protected ContentHbm handleCloneContent(ContentHbm oldContent, Map picturesIds, Map documentsIds, Map personsIds, Integer unitId) throws Exception {
		ContentHbm contentHbm = ContentHbm.Factory.newInstance();
		try {
			contentHbm.setContentId(sequenceHbmDao.getNextSequenceNumber("content.content_id"));
		} catch (Exception e) {
			log.error("error at creating primary key for content");
		}
		contentHbm.setStatus(oldContent.getStatus());
		contentHbm.setTemplate(oldContent.getTemplate());
		contentHbm.setUpdateSearchIndex(oldContent.isUpdateSearchIndex());
		ContentVersionHbm contentVersion = getContentVersionHbmDao().cloneContentVersion(oldContent.getLastContentVersion(), picturesIds, documentsIds, personsIds, unitId);
		contentHbm = this.create(contentHbm);
		contentHbm.getContentVersions().add(contentVersion);
		return contentHbm;
	}

	@Override
	protected String handleToXmlWithLastContentVersion(ContentHbm content) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("<content id=\"");
		sb.append(content.getContentId());
		sb.append("\">\n");
		sb.append("\t<template>").append(content.getTemplate()).append("</template>\n");
		sb.append("\t<status>").append(content.getStatus()).append("</status>\n");

		ContentVersionHbm lastContentVersion = content.getLastContentVersion();

		sb.append(getContentVersionHbmDao().toXml(lastContentVersion));
		sb.append("</content>\n");

		return sb.toString();

	}

	@Override
	protected ContentHbm handleCreateFromXml(Element cnde, boolean reusePrimaryKey, boolean liveDeploy, Map pictureIds, Map documentIds, Map personsIds, Integer newUniId) throws Exception {
		ContentHbm content = ContentHbm.Factory.newInstance();
		if (reusePrimaryKey) {
			Integer id = new Integer(cnde.getAttribute("id"));
			if (log.isDebugEnabled()) log.debug("creating Content with existing id " + id);
			content.setContentId(id);
		} else {
			content.setContentId(sequenceHbmDao.getNextSequenceNumber("content.content_id"));
		}
		content.setStatus(Integer.parseInt(XercesHelper.getNodeValue(cnde, "./status")));
		content.setUpdateSearchIndex(true);
		content.setTemplate(XercesHelper.getNodeValue(cnde, "./template"));
		content = create(content);

		Iterator cvit = XercesHelper.findNodes(cnde, "./contentVersion");
		while (cvit.hasNext()) {
			Element cvnde = (Element) cvit.next();
			String version = XercesHelper.getNodeValue(cvnde, "./version");
			// import contentVersion: PUBLS only on livedeploy, all but PUBLS on import
			if ((liveDeploy && version.equalsIgnoreCase("PUBLS")) || (!liveDeploy && !version.equalsIgnoreCase("PUBLS"))) {
				ContentVersionHbm contentVersion;
				if ((pictureIds == null) && (documentIds == null)) {
					contentVersion = getContentVersionHbmDao().createFromXml(cvnde, reusePrimaryKey, liveDeploy);
				} else {
					contentVersion = getContentVersionHbmDao().createFromXmlWIthMedia(cvnde, reusePrimaryKey, liveDeploy, pictureIds, documentIds, personsIds, newUniId);
				}
				content.getContentVersions().add(contentVersion);
			}
		}

		return content;
	}
}
