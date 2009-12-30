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
package de.juwimm.cms.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.vo.SiteValue;

public class SmallSiteConfigReader {

	private static Log log = LogFactory.getLog(SmallSiteConfigReader.class);

	public static final String CONF_NODE_DEFAULT = "config/default";
	public static final String EXTERNAL_SEARCH_TAG = "externalSearchConfig";
	public static final String EXTERNAL_SEARCH_URLS_PATH = EXTERNAL_SEARCH_TAG + "/urls/urlItem";
	public static final String EXTERNAL_SEARCH_DEPTH_PATH = EXTERNAL_SEARCH_TAG + "/depth";

	public static final String EXTERNAL_SEARCH_POSITIVE_LIST = EXTERNAL_SEARCH_TAG + "/%s/positives/positiveItem";
	public static final String EXTERNAL_SEARCH_NEGATIVE_LIST = EXTERNAL_SEARCH_TAG + "/%s/negatives/negativeItem";
	private Document confdoc = null;
	private String relativeNodePath = "//config";

	public SmallSiteConfigReader(String configfile) {
		try {
			if (configfile == null || configfile.equals("")) {
				setConfdoc(XercesHelper.getNewDocument());
			} else {
				setConfdoc(XercesHelper.string2Dom(configfile));
			}
			this.setRelativeNodePath(CONF_NODE_DEFAULT);
		} catch (Exception exe) {
		}
	}

	public SmallSiteConfigReader(SiteValue site) {
		try {
			if (site.getConfigXML() == null || site.getConfigXML().equals("")) {
				setConfdoc(XercesHelper.getNewDocument());
			} else {
				setConfdoc(XercesHelper.string2Dom(site.getConfigXML()));
			}
			this.setRelativeNodePath(CONF_NODE_DEFAULT);
		} catch (Exception exe) {
		}
	}

	static public String getNegativeListTag(String property) {
		return String.format(EXTERNAL_SEARCH_NEGATIVE_LIST, property);
	}

	static public String getPositiveListTag(String property) {
		return String.format(EXTERNAL_SEARCH_POSITIVE_LIST, property);
	}

	public SmallSiteConfigReader() {
		setConfdoc(XercesHelper.getNewDocument());
		this.setRelativeNodePath(CONF_NODE_DEFAULT);
	}

	public SmallSiteConfigReader(SiteHbm site) {
		try {
			if (site.getConfigXML() == null || site.getConfigXML().equals("")) {
				setConfdoc(XercesHelper.getNewDocument());
			} else {
				setConfdoc(XercesHelper.string2Dom(site.getConfigXML()));
			}
			this.setRelativeNodePath(CONF_NODE_DEFAULT);
		} catch (Exception exe) {
		}
	}

	public String getConfigElementValue(String strNode) {
		return XercesHelper.getNodeValue(this.getConfdoc().getFirstChild(), "//" + this.getRelativeNodePath() + "/" + strNode);
	}

	public String getConfigAttribute(String strNode, String strAttrName) {
		String retVal = "";
		try {
			Element elm = (Element) XercesHelper.findNode(this.getConfdoc().getFirstChild(), "//" + this.getRelativeNodePath() + "/" + strNode);
			retVal = elm.getAttribute(strAttrName);
		} catch (Exception exe) {
		}
		return retVal;
	}

	public String readValue(String xmlPath) {
		try {
			Document document = this.getConfdoc();
			Iterator iterator = XercesHelper.findNodes(document, "//" + xmlPath);

			while (iterator.hasNext()) {
				Node node = (Node) iterator.next();
				return node.getFirstChild().getNodeValue();
			}
			return null;
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("Could not read from site config path:" + xmlPath);
			}
			return null;
		}
	}

	public List<String> readValues(String xmlPath) {
		List<String> result = new ArrayList<String>();
		try {
			Document document = this.getConfdoc();
			Iterator iterator = XercesHelper.findNodes(document, "//" + xmlPath);

			while (iterator.hasNext()) {
				Node node = (Node) iterator.next();
				result.add(node.getFirstChild().getNodeValue());
			}
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("Could not read from site config path:" + xmlPath);
			}
		}

		return result;
	}

	public void saveValues(String xmlPath, List<String> values) {
		String[] pathComponents = xmlPath.split("/");
		String itemTag = pathComponents[pathComponents.length - 1];
		Node parentNode = buildXmlPathToLeaf(xmlPath);
		//remove old values
		int oldLength = parentNode.getChildNodes().getLength();
		for (int i = 0; i < oldLength; i++) {
			parentNode.removeChild(parentNode.getChildNodes().item(0));
		}

		if (parentNode == null) {
			return;
		}

		if (values == null || values.size() == 0) {
			return;
		}

		//save new values
		Document document = this.getConfdoc();
		for (String value : values) {
			Node valueNode = document.createElement(itemTag);
			valueNode.appendChild(document.createTextNode(value));
			parentNode.appendChild(valueNode);
		}
	}

	public void saveValue(String xmlPath, String value) {
		String[] pathComponents = xmlPath.split("/");
		String itemTag = pathComponents[pathComponents.length - 1];
		Node parentNode = buildXmlPathToLeaf(xmlPath);
		if (parentNode == null) {
			return;
		}

		if (value == null) {
			return;
		}

		//delete old value
		try {
			Node oldNode = XercesHelper.findNode(parentNode, "//" + itemTag);
			parentNode.removeChild(oldNode);
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("SmalSiteConfigReader: old value at path " + xmlPath + " does not exist");
			}
		}
		//create new value
		Document document = this.getConfdoc();
		Node valueNode = document.createElement(itemTag);
		valueNode.appendChild(document.createTextNode(value));
		parentNode.appendChild(valueNode);

	}

	public Node buildXmlPathToLeaf(String xmlPath) {
		String fullXmlPath = getRelativeNodePath() + "/" + xmlPath;
		String[] pathComponents = fullXmlPath.split("/");
		try {
			Document document = this.getConfdoc();
			Node prevNode = document;
			Node nextNode;
			for (int i = 0; i < pathComponents.length - 1; i++) {
				nextNode = XercesHelper.findNode(prevNode, pathComponents[i]);
				if (nextNode == null) {
					nextNode = document.createElement(pathComponents[i]);
					prevNode.appendChild(nextNode);
				}
				prevNode = nextNode;
			}

			return prevNode;
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("Could not read from site config path:" + fullXmlPath);
			}
			return null;
		}
	}

	/**
	 * @param confdoc The confdoc to set.
	 */

	public void setConfdoc(SiteValue site) {
		try {
			this.confdoc = XercesHelper.string2Dom(site.getConfigXML());
		} catch (Exception e) {
			log.debug("SmallSiteConfigReader: can not parse config xml");
		}
	}

	protected void setConfdoc(Document confdoc) {
		this.confdoc = confdoc;
	}

	/**
	 * @return Returns the confdoc.
	 */
	protected Document getConfdoc() {
		return confdoc;
	}

	/**
	 * @param relativeNodePath The relativeNodePath to set.
	 */
	protected void setRelativeNodePath(String relativeNodePath) {
		this.relativeNodePath = relativeNodePath;
	}

	/**
	 * @return Returns the relativeNodePath.
	 */
	protected String getRelativeNodePath() {
		return relativeNodePath;
	}

	public void updateConfigXml(SiteValue site) {
		site.setConfigXML(XercesHelper.doc2String(this.getConfdoc()));
	}

	public void updateConfigXml(SiteHbm site) {
		site.setConfigXML(XercesHelper.doc2String(this.getConfdoc()));
	}

	public String getXmlString() {
		return XercesHelper.doc2String(this.getConfdoc());
	}
}
