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
package de.juwimm.cms.search.xmldb;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.xindice.client.xmldb.resources.XMLResourceImpl;
import org.apache.xindice.client.xmldb.services.MetaService;
import org.apache.xindice.core.meta.MetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;

import de.juwimm.cms.beans.foreign.TizzitPropertiesBeanSpring;
import de.juwimm.cms.search.vo.XmlSearchValue;

/**
 * tizzitXindiceHost and tizzitXindicePort must be set up in tizzit.properties!
 * @author Michael Frankfurter (michael.frankfurter@juwimm.com)
 * @version $Id$
 */
public class XindiceXmlDbImpl implements XmlDb {
	private static Logger log = Logger.getLogger(XindiceXmlDbImpl.class);
	public static final String XMLDB_COLLECTION_NAME = "viewComponentContent";
	private TizzitPropertiesBeanSpring tizzitPropertiesBeanSpring;

	public TizzitPropertiesBeanSpring getTizzitPropertiesBeanSpring() {
		return tizzitPropertiesBeanSpring;
	}

	@Autowired
	public void setTizzitPropertiesBeanSpring(TizzitPropertiesBeanSpring tizzitPropertiesBeanSpring) {
		// TODO: Test if autowire is working
		this.tizzitPropertiesBeanSpring = tizzitPropertiesBeanSpring;
	}

	public XmlSearchValue[] searchXml(Integer siteId, String xpathQuery) {
		if (log.isDebugEnabled()) log.debug("searchXml(...) -> begin at " + sdf.format(new Date()));

		if (xpathQuery == null || xpathQuery.length() == 0) {
			log.warn("XPath-Query is null or empty, no search possible and sensible!");
			return new XmlSearchValue[0];
		}

		XmlSearchValue[] retArray = null;
		org.xmldb.api.base.Collection col = null;

		try {
			String serverHost = tizzitPropertiesBeanSpring.getSearch().getXindiceHost();
			String port = tizzitPropertiesBeanSpring.getSearch().getXindicePort();

			col = XindiceHelper.getCollection(getTizzitPropertiesBeanSpring(), "xmldb:xindice://" + serverHost + ":" + port + "/db/", "viewComponentContent", siteId);

			//index erstellen
			if (xpathQuery.indexOf("[") < 0 && xpathQuery.indexOf("'") < 0 && xpathQuery.startsWith("//")) {
				String idxName = xpathQuery.substring(2);
				XindiceHelper.addIndex(col, "idx_" + idxName, idxName);
			}

			//xindice execute query
			XPathQueryService service = XindiceHelper.getXPathQueryService(col);
			MetaService metaService = XindiceHelper.getMetaService(col);
			if (log.isDebugEnabled()) log.debug("searchXml(...) -> starting searchXML with siteId " + siteId + " and query " + xpathQuery);

			ResourceSet resultSet = service.query(xpathQuery);
			if (resultSet.getSize() > 0) {
				retArray = new XmlSearchValue[(int) resultSet.getSize()];
				int i = 0;
				ResourceIterator results = resultSet.getIterator();
				while (results.hasMoreResources()) {
					XMLResourceImpl res = (XMLResourceImpl) results.nextResource();
					try {
						// test! perhaps if one entry is corrupt nothing is returned? para-stellenagebote + news
						if (log.isDebugEnabled()) log.debug("searchXml(...) -> found object with ID: " + res.getDocumentId());
						retArray[i] = new XmlSearchValue();
						retArray[i].setContent((String) res.getContent());

						MetaData metaData = metaService.getMetaData(res.getDocumentId());
						retArray[i].setInfoText((String) metaData.getAttribute("infoText"));
						retArray[i].setText((String) metaData.getAttribute("text"));
						retArray[i].setUnitId(new Integer((String) metaData.getAttribute("unitId")));
						retArray[i].setViewComponentId(new Integer(res.getDocumentId()));
					} catch (Exception e) {
						log.error("searchXml(...) -> error iterating results for search for: siteId " + siteId + " query " + xpathQuery, e);
					}
					i++;
				}
			} else {
				if (log.isDebugEnabled()) log.debug("searchXml(...) -> no result for query " + xpathQuery + " on site " + siteId);
			}

		} catch (Exception exe) {
			log.error("searchXml(...) -> error occured during search ", exe);
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (XMLDBException e) {
					log.warn("Error closing Xindice-Collection: " + e.getMessage(), e);
				}
			}
		}

		if (log.isDebugEnabled()) log.debug("searchXml(...) -> end at " + sdf.format(new Date()));
		return retArray;
	}

	public boolean saveXml(Integer siteId, Integer viewComponentId, String contentText, Map<String, String> metaAttributes) {
		if (log.isDebugEnabled()) log.debug("saveXml(...) -> begin");

		boolean retVal = false;

		String serverHost = tizzitPropertiesBeanSpring.getSearch().getXindiceHost();
		String port = tizzitPropertiesBeanSpring.getSearch().getXindicePort();

		try {
			org.xmldb.api.base.Collection col = XindiceHelper.getCollection(getTizzitPropertiesBeanSpring(), "xmldb:xindice://" + serverHost + ":" + port + "/db/", XMLDB_COLLECTION_NAME, siteId.intValue());

			XMLResource doc = (XMLResource) col.createResource(viewComponentId.toString(), "XMLResource");
			doc.setContent(contentText);
			col.storeResource(doc);

			MetaService metaService = XindiceHelper.getMetaService(col);
			MetaData metaData = metaService.getMetaData(viewComponentId.toString());
			if (!(metaAttributes.get("infoText") == null || "".equals(metaAttributes.get("infoText")))) {
				metaData.setAttribute("infoText", metaAttributes.get("infoText"));
			}
			if (!(metaAttributes.get("text") == null || "".equals(metaAttributes.get("text")))) {
				metaData.setAttribute("text", metaAttributes.get("text"));
			}
			if (!(metaAttributes.get("unitId") == null || "".equals(metaAttributes.get("unitId")))) {
				metaData.setAttribute("unitId", metaAttributes.get("unitId"));
			}

			metaService.setMetaData(viewComponentId.toString(), metaData);

			retVal = true;
		} catch (XMLDBException e) {
			log.error("saveXml(...) -> could not write xindice resource for viewComponent " + viewComponentId + "\n" + e.getMessage());
		}

		if (log.isDebugEnabled()) log.debug("saveXml(...) -> end");
		return retVal;
	}

	public void deleteXml(Integer siteId, Integer viewComponentId) {
		if (log.isDebugEnabled()) log.debug("deleteXml(...) -> begin");

		String serverHost = System.getProperty("tizzitXindiceHost", "localhost");
		String port = System.getProperty("tizzitXindicePort", "8080");

		try {
			org.xmldb.api.base.Collection col = XindiceHelper.getCollection(getTizzitPropertiesBeanSpring(), "xmldb:xindice://" + serverHost + ":" + port + "/db/", XMLDB_COLLECTION_NAME, siteId.intValue());
			org.xmldb.api.base.Resource doc = col.getResource(viewComponentId.toString());
			if (doc != null) {
				col.removeResource(doc);
			}
		} catch (XMLDBException e) {
			log.error("deleteXml(...) -> could not delete xindice resource for viewComponent " + viewComponentId + "\n" + e.getMessage());
		}

		if (log.isDebugEnabled()) log.debug("deleteXml(...) -> end");
	}

	/** @see de.juwimm.cms.search.xmldb.XmlDb#searchXmlByUnit(java.lang.Integer, java.lang.String) */
	public XmlSearchValue[] searchXmlByUnit(Integer unitId, Integer viewDocumentId, String xpathQuery) {
		if (log.isDebugEnabled()) log.debug("searchXmlByUnit(...) -> begin at " + sdf.format(new Date()));

		if (xpathQuery == null || xpathQuery.length() == 0) {
			log.warn("XPath-Query is null or empty, no search possible and sensible!");
			return new XmlSearchValue[0];
		}

		ArrayList<XmlSearchValue> result = new ArrayList<XmlSearchValue>();
		/*
		org.xmldb.api.base.Collection col = null;
		try {
			Integer siteId = UnitUtil.getLocalHome().findByPrimaryKey(unitId).getSite().getSiteId();

			String serverHost = System.getProperty("tizzitXindiceHost", "localhost");
			String port = System.getProperty("tizzitXindicePort", "8080");

			col = XindiceHelper.getCollection("xmldb:xindice://" + serverHost + ":" + port + "/db/", SearchUpdateMessageListener.XMLDB_COLLECTION_NAME, siteId);

			//index erstellen
			if (xpathQuery.indexOf("[") < 0 && xpathQuery.indexOf("'") < 0 && xpathQuery.startsWith("//")) {
				String idxName = xpathQuery.substring(2);
				XindiceHelper.addIndex(col, "idx_" + idxName, idxName);
			}

			//xindice execute query
			XPathQueryService service = XindiceHelper.getXPathQueryService(col);
			MetaService metaService = XindiceHelper.getMetaService(col);
			if (log.isDebugEnabled()) log.debug("searchXmlByUnit(...) -> starting searchXML with siteId " + siteId + " and query " + xpathQuery);

			ResourceSet resultSet = service.query(xpathQuery);
			if (resultSet.getSize() > 0) {
				ResourceIterator results = resultSet.getIterator();
				while (results.hasMoreResources()) {
					XMLResourceImpl res = (XMLResourceImpl) results.nextResource();
					try {
						// test! perhaps if one entry is corrupt nothing is returned?
						if (log.isDebugEnabled()) {
							log.debug("searchXmlByUnit(...) -> found object with ID: " + res.getDocumentId());
						}
						MetaData metaData = metaService.getMetaData(res.getDocumentId());
						Integer foundUnit = new Integer((String) metaData.getAttribute("unitId"));
						if (foundUnit.equals(unitId)) {
							XmlSearchValue xmlSearchValue = new XmlSearchValue();
							xmlSearchValue.setContent((String) res.getContent());
							xmlSearchValue.setInfoText((String) metaData.getAttribute("infoText"));
							xmlSearchValue.setText((String) metaData.getAttribute("text"));
							xmlSearchValue.setUnitId(new Integer((String) metaData.getAttribute("unitId")));
							xmlSearchValue.setViewComponentId(new Integer((String) res.getDocumentId()));
							result.add(xmlSearchValue);
						}
					} catch (Exception e) {
						log.error("searchXmlByUnit(...) -> error iterating results for search for: siteId " + siteId + " query " + xpathQuery, e);
					}
				}
			} else {
				if (log.isDebugEnabled()) log.debug("searchXmlByUnit(...) -> no result for query " + xpathQuery + " on site " + siteId);
			}

		} catch (FinderException exception) {
			log.error("searchXmlByUnit(...) -> could not find site for unit " + unitId, exception);
		} catch (Exception exception) {
			log.error("searchXmlByUnit(...) -> error occured during search ", exception);
		} finally {
			if (col != null) {
				try {
					col.close();
				} catch (XMLDBException e) {
					log.warn("Error closing Xindice-Collection: " + e.getMessage(), e);
				}
			}
		}
		*/
		if (log.isDebugEnabled()) log.debug("searchXmlByUnit(...) -> end at " + sdf.format(new Date()));
		return result.toArray(new XmlSearchValue[result.size()]);
	}

}
