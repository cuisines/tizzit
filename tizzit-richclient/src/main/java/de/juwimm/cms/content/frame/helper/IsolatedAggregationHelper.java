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
 * New AggregationHelper without static w3c.dom.Document for all aggregations. This one must be used<br/>
 * if multiple aggregations may occur on one page.<br/>
 * Substitutes AggregationHelper.java
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author <a href="mailto:sascha-matthias.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * @version $Id$
 */
public class IsolatedAggregationHelper {
	private Document docAggregation;

	public IsolatedAggregationHelper() {
	}

	public void setAggregationXml(Node ndeAggregation) {
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
		Element elemAgg = doc.createElement("aggregation");
		elemAgg.setAttribute("description", rootNode.getDescription());
		elemAgg.appendChild(rootNode.getXmlRepresentation(doc, elemAgg));
		return elemAgg;
	}

	public Hashtable<String, Integer> getClickHashForNode(String type, String id) {
		Hashtable<String, Integer> hash = new Hashtable<String, Integer>(20);
		try {
			Iterator iterator = XercesHelper.findNodes(docAggregation.getFirstChild(), "//include[@id=\"" + id + "\" and not(@linkId) and @type=\"" + type + "\"]/content/*");
			while (iterator.hasNext()) {
				Node node = (Node) iterator.next();
				hash.put(node.getNodeName(), new Integer(1));
			}
		} catch (Exception ex) {
		}
		return hash;
	}

	public Hashtable<String, Integer> getClickHashForNode(String type, String id, Integer linkId) {
		Hashtable<String, Integer> hash = new Hashtable<String, Integer>(20);
		try {
			Iterator iterator = XercesHelper.findNodes(docAggregation.getFirstChild(), "//include[@id=\"" + id + "\" and @linkId=\"" + linkId + "\" and @type=\"" + type + "\"]/content/*");
			while (iterator.hasNext()) {
				Node node = (Node) iterator.next();
				hash.put(node.getNodeName(), new Integer(1));
			}
		} catch (Exception ex) {
		}
		return hash;
	}

	public boolean isInAggregation(String type, String id) {
		Iterator iterator = XercesHelper.findNodes(docAggregation.getFirstChild(), "//include[@id=\"" + id + "\" and not(@linkId) and @type=\"" + type + "\"]");
		return iterator.hasNext();
	}

	public boolean isInAggregation(String type, String id, Integer linkId) {
		Iterator iterator = XercesHelper.findNodes(docAggregation.getFirstChild(), "//include[@id=\"" + id + "\" and @linkId=\"" + linkId + "\" and @type=\"" + type + "\"]");
		return iterator.hasNext();
	}

	public static Node findNode(Node node, String path) throws Exception {
		return XercesHelper.findNode(node, path);
	}

	public static String getNodeValue(Node node, String path) throws Exception {
		return XercesHelper.getNodeValue(node, path);
	}

	/**
	 * Returns the attribute "listtype"'s value - or an empty string, if the attribute could not be found.
	 * 
	 * @param personid the person's id
	 * @return a string representing the person's list type
	 */
	public String lookupPersonViewType(String personid) {
		String strRet = new String();
		try {
			Element elm = (Element) findNode(docAggregation.getFirstChild(), "//include[@id=\"" + personid + "\" and @type=\"person\"]");
			strRet = elm.getAttribute("listtype");
		} catch (Exception ex) {
		}
		return strRet;
	}
}
