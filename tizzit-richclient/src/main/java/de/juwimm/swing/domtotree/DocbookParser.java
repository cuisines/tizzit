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
/*
 * Created on 23.06.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package de.juwimm.swing.domtotree;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.tizzit.util.XercesHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * @author Michael Meyer
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class DocbookParser {
	private Document mydoc;
	private String filename;
	private DefaultTreeModel treemodel;
	private final String titletag;
	private String attrname;
	private String attrvalue;

	private final Color n;

	public DocbookParser(String file, String titletag, Color normal) {
		filename = new String(file);
		this.titletag = titletag;
		n = normal;
	}

	public DocbookParser(Document doc, String titletag, Color normal) {
		mydoc = doc;
		this.titletag = titletag;
		n = normal;
	}

	/**
	 * Call this function after creating the object
	 * @return
	 */

	public boolean convertFileToDom() {
		mydoc = parseFile(filename);
		if (mydoc == null) {
			return false;
		}
		return true;
	}

	/**
	 * The parser needs to know the name and the value of the standard tag.
	 * Calls this function befor you call the start function.
	 *
	 * @param attrname The name of the attribute (e.g. role)
	 * @param attrvalue The value of the attribute (e.g. standard)
	 */

	public void setStandardTag(String attrname, String attrvalue) {
		this.attrname = attrname;
		this.attrvalue = attrvalue;
	}

	/**
	 * Call this function to start parsing.
	 *
	 */

	public void start() {
		DefaultMutableTreeNode akt = null;
		Element el = mydoc.getDocumentElement();
		akt = this.searchTitles(el, false);
		treemodel = new DefaultTreeModel(akt, true);
		NodeList list = el.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			try {
				Element e = (Element) list.item(i);
				nextParsing(e, akt);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Call this function, when you only want to show the node of a tree until one of its node
	 * ownes the standard tag.
	 */

	public void removeAllStandards() {
		MyTreeNode tr = (MyTreeNode) treemodel.getRoot();
		this.removeSubStandards(tr);
	}

	private void nextParsing(Element e, DefaultMutableTreeNode root) {
		DefaultMutableTreeNode akt = null;
		boolean st = isStandard(e);
		for (int i = 0; i < e.getChildNodes().getLength(); i++) {
			try {
				Element elm = (Element) e.getChildNodes().item(i);
				if (elm.getTagName().equals(titletag)) {
					akt = printOnMyScreen(elm, st);
					insertNewTreeNode(akt, root);
				}
				if (elm.hasChildNodes()) {
					if (akt != null)
						nextParsing(elm, root);
					else {
						nextParsing(elm, akt);
					}
				}
			} catch (Exception ex) {
			}
		}
	}

	private DefaultMutableTreeNode searchTitles(Element e, boolean st) {
		DefaultMutableTreeNode node = null;
		boolean ok = false;
		for (int i = 0; i < e.getChildNodes().getLength(); i++) {
			try {
				Element el = (Element) (e.getChildNodes().item(i));
				if (el.getLocalName().equals(titletag)) {
					node = printOnMyScreen(el, st);
					if (node != null) ok = true;
					break;
				}
			} catch (Exception ex) {
			}
		}
		if (!ok) {
			node = printOnMyScreen(e, st, e.getLocalName());
		}
		return node;
	}

	private DefaultMutableTreeNode printOnMyScreen(Element e, boolean st, String v) {
		DefaultMutableTreeNode node = getTreeNode(v, e, st);
		return node;
	}

	private DefaultMutableTreeNode printOnMyScreen(Element e, boolean st) {
		NodeList list = e.getChildNodes();
		DefaultMutableTreeNode node = null;
		for (int i = 0; i < list.getLength(); i++) {
			try {
				Text t = (Text) list.item(i);
				node = getTreeNode(t.getData(), e, st);
				break;
			} catch (Exception ex) {
			}
		}
		return node;
	}

	private void insertNewTreeNode(DefaultMutableTreeNode akt, DefaultMutableTreeNode root) {
		try {
			treemodel.insertNodeInto(akt, root, root.getChildCount());
		} catch (Exception ex) {
		}
	}

	private boolean isStandard(Element el) {
		NamedNodeMap map = el.getAttributes();
		for (int i = 0; i < map.getLength(); i++) {
			Node node = map.item(i);
			if ((node.getLocalName()).equals(attrname) && (node.getNodeValue()).equals(attrvalue)) {
				return true;
			}
		}

		return false;
	}

	private MyTreeNode getTreeNode(String value, Element el, boolean st) {
		MyTreeNode mynode = new MyTreeNode(value, n, true, st);
		Element set;
		try {
			set = (Element) el.getParentNode();
		} catch (Exception ex) {
			set = el;
		}
		mynode.setDomNode(set);
		return mynode;
	}

	private void removeSubStandards(MyTreeNode rn) {
		for (int i = 0; i < rn.getChildCount(); i++) {
			MyTreeNode mynode = (MyTreeNode) rn.getChildAt(i);
			if (mynode.getStandard()) {
				mynode.removeAllChildren();
			} else {
				if (!mynode.isLeaf()) {
					removeSubStandards(mynode);
				}
			}
		}
	}

	private void setMyTreeLeafs(MyTreeNode node) {
		for (int i = 0; i < node.getChildCount(); i++) {
			MyTreeNode nde = (MyTreeNode) node.getChildAt(i);
			if (!nde.isLeaf())
				setMyTreeLeafs(nde);
			else {
				nde.setAllowsChildren(false);
			}
		}
	}

	public DefaultTreeModel getTreeModel() {
		MyTreeNode root = (MyTreeNode) treemodel.getRoot();
		setMyTreeLeafs(root);
		return treemodel;
	}

	/*************** Helper ****************/

	private Document parseFile(String filename) {
		try {
			FileInputStream inStream = new FileInputStream(new File(filename));
			return XercesHelper.inputstream2Dom(inStream);
		} catch (Exception ex) {
			return null;
		}
	}
}