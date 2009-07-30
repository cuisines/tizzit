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

import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.apache.xindice.client.xmldb.services.CollectionManager;
import org.apache.xindice.client.xmldb.services.MetaService;
import org.w3c.dom.Document;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;

import de.juwimm.cms.beans.foreign.CqPropertiesBeanSpring;
import de.juwimm.util.XercesHelper;

/**
 * Helperclass for performing operations on the Xindice XML Database.
 * <p>Most of the code should be abstract from the Database behind - so it should
 * possible to switch to a different XML Provider like eXist or Tamino if needed.</p>
 * 
 * <p>Title: ConQuest</p>
 * <p>Description: Enterprise Content Management</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author <a href="s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public final class XindiceHelper {
	private static Logger log = Logger.getLogger(XindiceHelper.class);
	private static Hashtable<Collection, CollectionManager> htCollectionToCollectionManager = new Hashtable<Collection, CollectionManager>();
	private static Hashtable<Collection, XPathQueryService> htCollectionToXPathQueryService = new Hashtable<Collection, XPathQueryService>();
	private static Hashtable<Collection, MetaService> htCollectionToMetaService = new Hashtable<Collection, MetaService>();
	private static boolean initiated = false;

	private XindiceHelper() {
	}

	public static CollectionManager getCollectionManager(Collection coll) throws XMLDBException {
		if (coll == null) return null;
		if (htCollectionToCollectionManager.contains(coll)) { return (CollectionManager) htCollectionToCollectionManager.get(coll); }
		CollectionManager collectionManager = (CollectionManager) coll.getService("CollectionManager", "1.0");
		htCollectionToCollectionManager.put(coll, collectionManager);
		return collectionManager;
	}

	public static XPathQueryService getXPathQueryService(Collection coll) throws XMLDBException {
		if (coll == null) return null;
		if (htCollectionToXPathQueryService.contains(coll)) { return (XPathQueryService) htCollectionToXPathQueryService.get(coll); }
		XPathQueryService service = (XPathQueryService) coll.getService("XPathQueryService", "1.0");
		htCollectionToXPathQueryService.put(coll, service);
		return service;
	}

	public static MetaService getMetaService(Collection coll) throws XMLDBException {
		if (coll == null) return null;
		if (htCollectionToMetaService.contains(coll)) { return (MetaService) htCollectionToMetaService.get(coll); }
		MetaService metaService = (MetaService) coll.getService("MetaService", "1.0");
		htCollectionToMetaService.put(coll, metaService);
		return metaService;
	}

	/**
	 * Xindice only method for adding indexes to a Collection.
	 * Pattern uses the following format:
	 *         Pattern           Description
	 *         =========== ====================================================
	 *          elem       The value of the named element
	 *          elem@attr  The value of the attribute for the named element
	 *          *          The value for all elements
	 *          *@attr     The value of the named attribute for all elements
	 *          elem@*     The value of all attributes for the named element
	 *          *@*        The value of all attributes for all elements
	 */
	public static void addIndex(Collection collection, String name, String pattern) throws XMLDBException {
		CollectionManager collectionManager = getCollectionManager(collection);
		boolean createIdx = true;
		String[] idxe = collectionManager.listIndexers();
		for (int i = 0; i < idxe.length; i++) {
			if (idxe[i].equalsIgnoreCase(name)) {
				createIdx = false;
				break;
			}
		}
		if (createIdx) {
			try {
				DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				org.w3c.dom.Document document = documentBuilder.newDocument();
				org.w3c.dom.Element element = document.createElement("index");
				document.appendChild(element);

				element.setAttribute("class", "org.apache.xindice.core.indexer.ValueIndexer");
				element.setAttribute("name", name);
				element.setAttribute("pattern", pattern);

				collectionManager.createIndexer(document);

				if (log.isDebugEnabled()) {
					log.debug("Listing indexes:");
					String[] idx = collectionManager.listIndexers();
					for (int i = 0; i < idx.length; i++) {
						log.debug("initializeXMLDB - " + i + " - " + idx[i]);
					}
					log.debug("initializeXMLDB - Total Indexes: " + idx.length);
				}
			} catch (Throwable exe) {
				log.error("Error occured", exe);
			}
		}
	}

	public void deleteDocument(Collection coll, String documentId) throws XMLDBException {
		CollectionManager collman = getCollectionManager(coll);
		collman.dropXMLObject(documentId);
	}

	public static synchronized Collection getCollection(CqPropertiesBeanSpring cqProps, String uri, String collectionName, Integer siteId) throws XMLDBException {
		collectionName += "_" + siteId.toString();
		if (log.isDebugEnabled()) log.debug("start getCollection: " + collectionName);

		if (!initiated) {
			try {
				//System.setProperty("xindice.xmlrpc.user", "");
				//System.setProperty("xindice.xmlrpc.password", "");

				String driver = "org.apache.xindice.client.xmldb.DatabaseImpl";
				Class c = Class.forName(driver);
				Database database = (Database) c.newInstance();

				DatabaseManager.registerDatabase(database);
				initiated = true;
			} catch (Exception exe) {
				String errMsg = "Error during the registering of the Xindice Database Driver";
				log.error(errMsg);
				throw new XMLDBException(ErrorCodes.VENDOR_ERROR, -1, errMsg, exe);
			}
		}

		Collection col = DatabaseManager.getCollection(uri + collectionName);
		if (col == null) {
			log.info("The given XMLDB Collection " + collectionName + " was not found at " + uri + " and will be created...");
			String collectionConfig = "<collection compressed=\"true\" name=\"" + collectionName + "\">" + "<filer class=\"org.apache.xindice.core.filer.BTreeFiler\" gzip=\"true\"/>" + "</collection>";
			Document collectionConfigDom = null;
			try {
				collectionConfigDom = XercesHelper.string2Dom(collectionConfig);
			} catch (Exception exe) {
				String errMsg = "Error during the converting of the Collection-String to XML-DOM";
				log.error(errMsg);
				throw new XMLDBException(ErrorCodes.VENDOR_ERROR, -1, errMsg, exe);
			}
			try {
				col = DatabaseManager.getCollection(uri);
				CollectionManager collman = getCollectionManager(col);
				collman.createCollection(collectionName, collectionConfigDom);
				col = DatabaseManager.getCollection(uri + collectionName);
				addIndex(col, "idx_document", "document@*");
				addIndex(col, "idx_internalLink", "internalLink@*");
				addIndex(col, "idx_news", "news@*");
				addIndex(col, "idx_jobs", "jobs@*");
			} catch (Exception e) {
				String errMsg = "Error during create of new Collection: " + e.getMessage();
				log.error(errMsg);
				throw new XMLDBException(ErrorCodes.VENDOR_ERROR, -1, errMsg, e);
			}
			if (col == null) {
				String errMsg = "An unknown error has occured - I created the Collection you wanted but I haven't got it!";
				log.error(errMsg);
				throw new XMLDBException(ErrorCodes.VENDOR_ERROR, -1, errMsg);
			}

		}
		if (log.isDebugEnabled()) log.debug("end  getCollection");
		return col;
	}

}