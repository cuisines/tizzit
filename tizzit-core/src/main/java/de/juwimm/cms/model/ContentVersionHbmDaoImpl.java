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
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.tizzit.util.Base64;
import org.tizzit.util.DateConverter;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.remote.helper.AuthenticationHelper;
import de.juwimm.cms.vo.ContentVersionValue;

/**
 * @see de.juwimm.cms.model.ContentVersionHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> ,
 *         Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class ContentVersionHbmDaoImpl extends ContentVersionHbmDaoBase {
	private static Log log = LogFactory.getLog(ContentVersionHbmDaoImpl.class);

	@Autowired
	private SequenceHbmDao sequenceHbmDao;

	@Override
	public ContentVersionHbm create(ContentVersionHbm contentVersionHbm) {
		if (contentVersionHbm.getContentVersionId() == null || contentVersionHbm.getContentVersionId().intValue() == 0) {
			try {
				Integer id = sequenceHbmDao.getNextSequenceNumber("contentversion.content_version_id");
				contentVersionHbm.setContentVersionId(id);
			} catch (Exception e) {
				log.error("Error creating primary key", e);
			}
		}
		if (contentVersionHbm.getVersion() == null) {
			contentVersionHbm.setVersion("1");
		}
		contentVersionHbm.setCreateDate(System.currentTimeMillis());
		contentVersionHbm.setCreator(AuthenticationHelper.getUserName());
		return super.create(contentVersionHbm);
	}

	/**
	 * @see de.juwimm.cms.model.ContentVersionHbm#toXml(int)
	 */
	@Override
	public String handleToXml(ContentVersionHbm contentVersion) {
		StringBuilder sb = new StringBuilder();
		sb.append("<contentVersion id=\"");
		sb.append(contentVersion.getContentVersionId());
		sb.append("\">\n");
		sb.append("<heading><![CDATA[").append(contentVersion.getHeading()).append("]]></heading>\n");
		sb.append("<creator><userName>").append(contentVersion.getCreator()).append("</userName>").append("</creator>\n");
		sb.append("<createDate>").append(DateConverter.getSql2String(new Date(contentVersion.getCreateDate()))).append("</createDate>\n");
		/* KICK the XML Banner out */
		String xmlbanner = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
		String txt = contentVersion.getText();
		if (txt != null) {
			txt = txt.trim();
		} else {
			txt = ""; //$NON-NLS-1$
		}
		if (txt.length() >= xmlbanner.length() && txt.substring(0, xmlbanner.length()).equals(xmlbanner)) {
			txt = txt.substring(xmlbanner.length());
		}
		txt = Base64.encodeString(txt, false);
		sb.append("<text>").append(txt).append("</text>\n");
		sb.append("<version>").append(contentVersion.getVersion()).append("</version>\n");
		sb.append("</contentVersion>\n");
		return sb.toString();
	}

	/**
	 * @see de.juwimm.cms.model.ContentVersionHbm#getDao()
	 */
	@Override
	public ContentVersionValue handleGetDao(ContentVersionHbm contentVersion) {
		ContentVersionValue dao = new ContentVersionValue();
		dao.setContentVersionId(contentVersion.getContentVersionId());
		dao.setHeading(contentVersion.getHeading());
		dao.setCreateDate(contentVersion.getCreateDate());
		dao.setCreator(contentVersion.getCreator());
		dao.setText(contentVersion.getText());
		dao.setVersion(contentVersion.getVersion());
		return dao;
	}

	@Override
	protected ContentVersionHbm handleCreateFromXml(Element cvnde, boolean reusePrimaryKey, boolean liveDeploy) throws Exception {
		ContentVersionHbm contentVersion = ContentVersionHbm.Factory.newInstance();
		if (reusePrimaryKey) {
			Integer id = new Integer(cvnde.getAttribute("id"));
			if (log.isDebugEnabled()) log.debug("creating ContentVersion with existing id " + id);
			contentVersion.setContentVersionId(id);
		}
		contentVersion.setCreateDate(DateConverter.getString2Sql(XercesHelper.getNodeValue(cvnde, "./createDate")).getTime());
		contentVersion.setHeading(XercesHelper.getNodeValue(cvnde, "./heading"));
		contentVersion.setVersion(XercesHelper.getNodeValue(cvnde, "./version"));
		contentVersion.setCreator(XercesHelper.getNodeValue(cvnde, "./creator/userName"));
		String ttext = null;
		try {
			Node ttextnode = XercesHelper.findNode(cvnde, "./text");
			ttext = XercesHelper.nodeList2string(ttextnode.getChildNodes());
			if (ttext != null) ttext = Base64.decodeToString(ttext);
		} catch (Exception e) {
		}
		if (ttext == null || ttext.equals("<text/>")) {
			ttext = "";
		}
		contentVersion.setText(ttext);
		contentVersion = create(contentVersion);

		return contentVersion;
	}

	protected Collection handleFindContentVersionsByViewComponent(Integer viewComponentId) throws Exception {
		Query query = getSession().createQuery("select content.contentVersions from de.juwimm.cms.model.ContentHbm content" + " ,de.juwimm.cms.model.ViewComponentHbm viewComponent" + " where content.contentId = viewComponent.reference and viewComponent.viewComponentId = :viewComponentId");
		query.setParameter("viewComponentId", viewComponentId);
		return query.list();
	}

	@Override
	protected ContentVersionHbm handleCreateFromXmlWIthMedia(Element cvnde, boolean reusePrimaryKey, boolean liveServer, Map pictureIds, Map documentIds, Map personsIds, Integer newUnitId) throws Exception {
		ContentVersionHbm contentVersion = ContentVersionHbm.Factory.newInstance();
		if (reusePrimaryKey) {
			Integer id = new Integer(cvnde.getAttribute("id"));
			if (log.isDebugEnabled()) log.debug("creating ContentVersion with existing id " + id);
			contentVersion.setContentVersionId(id);
		} else {
			Integer id = sequenceHbmDao.getNextSequenceNumber("contentversion.content_version_id");
			contentVersion.setContentVersionId(id);
		}
		contentVersion.setCreateDate(DateConverter.getString2Sql(XercesHelper.getNodeValue(cvnde, "./createDate")).getTime());
		contentVersion.setHeading(XercesHelper.getNodeValue(cvnde, "./heading"));
		contentVersion.setVersion(XercesHelper.getNodeValue(cvnde, "./version"));
		contentVersion.setCreator(XercesHelper.getNodeValue(cvnde, "./creator/userName"));
		String ttext = null;
		try {
			Node ttextnode = XercesHelper.findNode(cvnde, "./text");
			ttext = XercesHelper.nodeList2string(ttextnode.getChildNodes());
			if (ttext != null) {
				ttext = Base64.decodeToString(ttext);
				Document content = XercesHelper.string2Dom(ttext);
				Iterator cvIt = XercesHelper.findNodes(content, "//picture");
				while (cvIt.hasNext()) {
					Element el = (Element) cvIt.next();
					Integer oldId = new Integer(el.getAttribute("description"));
					Integer newId = (Integer) pictureIds.get(oldId);
					el.setAttribute("description", newId.toString());
				}
				Iterator imgIt = XercesHelper.findNodes(content, "//image");
				while (imgIt.hasNext()) {
					Element el = (Element) imgIt.next();
					Integer oldId = new Integer(el.getAttribute("src"));
					Integer newId = (Integer) pictureIds.get(oldId);
					el.setAttribute("src", newId.toString());
				}
				Iterator docIt = XercesHelper.findNodes(content, "//document");
				while (docIt.hasNext()) {
					Element el = (Element) docIt.next();
					Integer oldId = new Integer(el.getAttribute("src"));
					Integer newId = (Integer) documentIds.get(oldId);
					el.setAttribute("src", newId.toString());
				}
				Iterator docAggregation = XercesHelper.findNodes(content, "//aggregation");
				if (personsIds != null) {
					while (docAggregation.hasNext()) {
						Element elAggregation = (Element) docAggregation.next();
						Iterator itInclude = XercesHelper.findNodes(elAggregation, "./include");
						while (itInclude.hasNext()) {
							Node nodeInclude = (Node) itInclude.next();
							String type = nodeInclude.getAttributes().getNamedItem("type").getNodeValue();
							if (type.equals("unit")) {
								if (newUnitId != null) {
									nodeInclude.getAttributes().getNamedItem("id").setNodeValue(newUnitId.toString());
								}
							}
							Iterator itIncludePerson = XercesHelper.findNodes(nodeInclude, "./include");
							while (itIncludePerson.hasNext()) {
								Node nodePerson = (Node) itIncludePerson.next();
								String typePerson = nodePerson.getAttributes().getNamedItem("type").getNodeValue();
								if (typePerson.equals("person")) {
									Long personId = Long.parseLong(nodePerson.getAttributes().getNamedItem("id").getNodeValue());
									if (personsIds != null) {
										Long newId = (Long) personsIds.get(personId);
										nodePerson.getAttributes().getNamedItem("id").setNodeValue(newId.toString());
									}
								}
							}
						}
					}
				}
				ttext = XercesHelper.doc2String(content);
			}
		} catch (Exception e) {
		}
		if (ttext == null || ttext.equals("<text/>")) {
			ttext = "";
		}
		contentVersion.setText(ttext);
		contentVersion = create(contentVersion);

		return contentVersion;
	}

	@Override
	protected ContentVersionHbm handleCloneContentVersion(ContentVersionHbm oldContentVersion, Map picturesIds, Map documentsIds) throws Exception {
		ContentVersionHbm contentVersionHbm = ContentVersionHbm.Factory.newInstance();;
		try {
			Integer id = sequenceHbmDao.getNextSequenceNumber("contentversion.content_version_id");
			contentVersionHbm.setContentVersionId(id);
		} catch (Exception e) {
			log.error("Error creating primary key for contentVersion", e);
		}
		/**replace pictures and documents only if not null*/
		if ((picturesIds != null) || (documentsIds != null)) {
			String text = oldContentVersion.getText();
			Document content = XercesHelper.string2Dom(text);
			if (picturesIds != null) {
				Iterator cvIt = XercesHelper.findNodes(content, "//picture");
				while (cvIt.hasNext()) {
					Element el = (Element) cvIt.next();
					Integer oldId = new Integer(el.getAttribute("description"));
					Integer newId = (Integer) picturesIds.get(oldId);
					el.setAttribute("description", newId.toString());
				}
				Iterator imgIt = XercesHelper.findNodes(content, "//image");
				while (imgIt.hasNext()) {
					Element el = (Element) imgIt.next();
					Integer oldId = new Integer(el.getAttribute("src"));
					Integer newId = (Integer) picturesIds.get(oldId);
					el.setAttribute("src", newId.toString());
				}
			}
			if (documentsIds != null) {
				Iterator docIt = XercesHelper.findNodes(content, "//document");
				while (docIt.hasNext()) {
					Element el = (Element) docIt.next();
					Integer oldId = new Integer(el.getAttribute("src"));
					Integer newId = (Integer) documentsIds.get(oldId);
					el.setAttribute("src", newId.toString());
				}
			}
			text = XercesHelper.doc2String(content);
			contentVersionHbm.setText(text);
		} else {
			contentVersionHbm.setText(oldContentVersion.getText());
		}
		contentVersionHbm.setVersion("1");
		contentVersionHbm.setHeading(oldContentVersion.getHeading());
		contentVersionHbm.setCreateDate(System.currentTimeMillis());
		contentVersionHbm.setCreator(AuthenticationHelper.getUserName());
		return super.create(contentVersionHbm);
	}

}
