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
package de.juwimm.cms.content.frame.helper;

import java.util.Hashtable;
import java.util.Iterator;

import org.tizzit.util.XercesHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.frame.tree.ComponentNode;

/**
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik </a>
 * @version $Id$
 * @deprecated substituted by IsolatedAggregationHelper.java
 */
public class AggregationHelper {
	private static Document docAggregation;

	public AggregationHelper() {
	}

	public static void setAggregationXml(Node ndeAggregation) {
		docAggregation = XercesHelper.getNewDocument();
		if (ndeAggregation != null) {
			Node appendednode = docAggregation.importNode(ndeAggregation, true);
			docAggregation.appendChild(appendednode);
		} else {
			docAggregation.appendChild(docAggregation.createElement("aggregation"));
		}
	}

	public Node getAggregationXml(ComponentNode rootNode) {
		Document doc = ContentManager.getDomDoc();
		Element node = doc.createElement("aggregation");
		node.setAttribute("description", rootNode.getDescription());
		node.appendChild(rootNode.getXmlRepresentation(doc, node));
		return node;
	}

	public static Hashtable<String, Integer> getClickHashForNode(String type, String id) {
		Hashtable<String, Integer> hash = new Hashtable<String, Integer>(20);
		try {
			Iterator iterator = XercesHelper.findNodes(docAggregation.getFirstChild(), "//include[@id=\"" + id
					+ "\" and not(@linkId) and @type=\"" + type + "\"]/content/*");
			while (iterator.hasNext()) {
				Node node = (Node) iterator.next();
				hash.put(node.getNodeName(), new Integer(1));
			}
		} catch (Exception ex) {
		}
		return hash;
	}

	public static Hashtable<String, Integer> getClickHashForNode(String type, String id, int linkId) {
		Hashtable<String, Integer> hash = new Hashtable<String, Integer>(20);
		try {
			Iterator iterator = XercesHelper.findNodes(docAggregation.getFirstChild(), "//include[@id=\"" + id
					+ "\" and @linkId=\"" + linkId + "\" and @type=\"" + type + "\"]/content/*");
			while (iterator.hasNext()) {
				Node node = (Node) iterator.next();
				hash.put(node.getNodeName(), new Integer(1));
			}
		} catch (Exception ex) {
		}
		return hash;
	}

	public static boolean isInAggregation(String type, String id) {
		try {
			Iterator iterator = XercesHelper.findNodes(docAggregation.getFirstChild(), "//include[@id=\"" + id
					+ "\" and not(@linkId) and @type=\"" + type + "\"]");
			Node node;
			if (iterator.hasNext()) {
				node = (Node) iterator.next();
			} else {
				node = null;
			}
			if (node != null) { return true; }

		} catch (Exception ex) {
		}
		return false;
	}

	public static boolean isInAggregation(String type, String id, int linkId) {
		try {
			Iterator iterator = XercesHelper.findNodes(docAggregation.getFirstChild(), "//include[@id=\"" + id
					+ "\" and @linkId=\"" + linkId + "\" and @type=\"" + type + "\"]");
			Node node;
			if (iterator.hasNext()) {
				node = (Node) iterator.next();
			} else {
				node = null;
			}
			if (node != null) { return true; }

		} catch (Exception ex) {
		}
		return false;
	}

	public static Node findNode(Node node, String path) throws Exception {
		return XercesHelper.findNode(node, path);
	}

	public static String getNodeValue(Node node, String path) throws Exception {
		return XercesHelper.getNodeValue(node, path);
	}

	public static String lookupPersonViewType(int personid) {
		String strRet = "";
		try {
			Element elm = (Element) findNode(docAggregation.getFirstChild(), "//include[@id=\"" + personid
					+ "\" and @type=\"person\"]");
			strRet = elm.getAttribute("listtype");
		} catch (Exception ex) {
		}
		return strRet;
	}
}