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
package de.juwimm.swing.domtotree;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.tizzit.util.XercesHelper;
import org.w3c.dom.*;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: JuwiMacMillanGroup</p>
 * @author Michael Meyer
 * @version 1.0
 */
public class DOMtoTreeModel {
	private Document mydoc;
	private String file;
	private DefaultTreeModel treemodel = null;
	private MyTreeNode aktRootNode = null;
	private boolean isOnlyStandard = false;
	private Color n;
	private Color cl;
	private int z = 0;

	private String title, sectshort, chapt;

	/**
	 * Constructor
	 *
	 * @param filename The XML filename
	 * @param titlename Shortage of the title tags
	 * @param sectshortage Shortage of the sect tags
	 * @param chapt Shortage of the chapt tags
	 */

	public DOMtoTreeModel(String filename, String titlename, String sectshortage, String chapt) {
		file = filename;
		title = titlename;
		this.chapt = chapt;
		sectshort = sectshortage;
	}

	/**
	 * Constructor
	 *
	 * @param filename Name of the XML File
	 * @param titlename Shortage of the title tags
	 * @param sectshortage Shortage of the sect tags
	 * @param normal Color when the node is not selected
	 * @param click Color when the node is selected
	 */

	public DOMtoTreeModel(String filename, String titlename, String sectshortage, Color normal, Color click) {
		file = filename;
		title = titlename;
		sectshort = sectshortage;
		n = normal;
		cl = click;
	}

	/**
	 * All nodes will be shown, not only nodes with the standard attribut. Call this function
	 * befor you call createTreeModel().
	 */

	public void makeAllNodeVisible() {
		isOnlyStandard = false;
	}

	/**
	 * Only Node with the Standard Attribute will be shown by the tree.
	 * This is set as default.
	 */

	public void makeOnlyStandardVisible() {
		isOnlyStandard = true;
	}

	/**
	 * Call this funtion to build up the tree from a given file.
	 */

	public void createTreeModel() {
		try {
			mydoc = file2Dom(file);
			buildTheTreeModel(mydoc);
		} catch (Exception ex) {
			System.out.println("createTreeModel :" + ex.getMessage());
		}
	}

	/**
	 * Call this function to build up the tree from a Document object.
	 * @param doc The Document object
	 */

	public void createTreeModel(Document doc) {
		try {
			buildTheTreeModel(doc);
		} catch (Exception ex) {
			System.out.println("createTreeModel with doc : " + ex.getMessage());
		}
	}

	/**
	 * Returns the created treemodel
	 *
	 * @return treemodel
	 */

	public DefaultTreeModel getTreeModel() {
		return treemodel;
	}

	private void buildTheTreeModel(Document doc) {
		Element element = doc.getDocumentElement();
		NodeList list = element.getChildNodes();
		findChapter(list);
		if (isOnlyStandard) {
			MyTreeNode rn = (MyTreeNode) treemodel.getRoot();
			removeSubStandards(rn);
		}
	}

	private void setRootNode() {
		MyTreeNode node = (MyTreeNode) treemodel.getRoot();
		String value = (mydoc.getDocumentElement()).getNodeName();
		node.setStandard(false);
		node.setUserObject(value);
	}

	private void findChapter(NodeList list) {
		//Node node = null;
		int i = 0;
		try {
			for (i = 0; i < list.getLength(); i++) {
				printChapter(list.item(i));
			}
		} catch (Exception ex) {
			System.out.println("findChapter " + ex.getMessage());
			setRootNode();
		}
		//      getActNode(node, i);
	}

	private void startParsing(NodeList list) {

		Node node = null;
		int i = 0;
		try {
			for (i = 0; i < list.getLength(); i++) {
				if (printTitles(list.item(i))) {
					node = list.item(i);
					getActNode(node, i);
					break;
				}
			}
		} catch (Exception ex) {
			System.out.println("startParsing " + ex.getMessage());
		}
	}

	private void getActNode(Node node, int listItem) {
		int type;

		try {
			type = node.getNodeType();
			if (type == Node.ELEMENT_NODE) {
				if ((node.getLocalName().startsWith(title))) {

					Node parent = node.getParentNode();
					NodeList list = parent.getChildNodes();
					Node test = null;
					for (int i = listItem; i < list.getLength(); i++) {
						test = list.item(i);
						if (test.getNodeType() == Node.ELEMENT_NODE && test.getLocalName().startsWith(sectshort)) {
							if (hasMoreChildren(test)) {
								startParsing(test.getChildNodes());
								break;
							} else {
								parseLastLine(test);
								break;
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			System.out.println("getActNode : " + ex.getMessage());
		}
	}

	private boolean hasMoreChildren(Node node) {

		NodeList childs = node.getChildNodes();
		Node check;
		for (int i = 0; i < childs.getLength(); i++) {
			check = childs.item(i);
			if (check.getNodeType() == Node.ELEMENT_NODE && check.getLocalName().startsWith(sectshort)) { return true; }
		}
		return false;
	}

	private boolean isMyStandard(Node node) {
		Node parent = node.getParentNode();
		NamedNodeMap map = parent.getAttributes();
		boolean st = true;
		if (map.getLength() <= 0) {
			st = false;
		}
		return st;
	}

	private void parseLastLine(Node node) {
		NodeList list = node.getChildNodes();

		for (int i = 0; i < list.getLength(); i++) {
			Node nde = list.item(i);
			if (nde.getNodeType() == Node.ELEMENT_NODE && nde.getLocalName().equals(title)) {
				Text value = (Text) nde.getFirstChild();
				DefaultMutableTreeNode dmtn = this.getTreeNode(nde, value.getData(), true, isMyStandard(nde));
				treemodel.insertNodeInto(dmtn, aktRootNode, aktRootNode.getChildCount());
				Node parent = nde.getParentNode();
				getNextNode(parent);
			}
		}
	}

	private void getNextNode(Node node) {
		Node next, parent;
		try {
			next = node.getNextSibling();
			next = next.getNextSibling();
			parseLastLine(next);
		} catch (Exception ex) {
			aktRootNode = (MyTreeNode) aktRootNode.getParent();
			try {
				parent = node.getParentNode();
				next = parent.getNextSibling();
				next = next.getNextSibling();
				startParsing(next.getChildNodes());
			} catch (Exception e) {
				parent = node.getParentNode();
				getNextNode(parent);
			}
		}
	}

	private void printChapter(Node node) {
		if (node.getNodeName().startsWith(chapt)) {
			Text value = (Text) node.getFirstChild();
			DefaultMutableTreeNode nde = getTreeNode(node, value.getData(), true, isMyStandard(node));
			if (treemodel == null) {
				treemodel = new DefaultTreeModel(nde);
				aktRootNode = (MyTreeNode) nde;
			} else {
				treemodel.insertNodeInto(nde, aktRootNode, aktRootNode.getChildCount());
				aktRootNode = (MyTreeNode) nde;
			}
			startParsing(node.getChildNodes());
		}
	}

	private boolean printTitles(Node node) {
		if (node.getNodeName().equals(title)) {
			Text value = (Text) node.getFirstChild();
			DefaultMutableTreeNode nde = getTreeNode(node, value.getData(), true, isMyStandard(node));
			if (treemodel == null) {
				treemodel = new DefaultTreeModel(nde);
				aktRootNode = (MyTreeNode) nde;
			}
			treemodel.insertNodeInto(nde, aktRootNode, aktRootNode.getChildCount());
			aktRootNode = (MyTreeNode) nde;

			return true;
		}
		return false;
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

	private MyTreeNode getTreeNode(Node node, String value, boolean colornode, boolean st) {
		MyTreeNode mynode = new MyTreeNode(value, n, true, st);
		//    mynode.SetDomNode(node);
		return mynode;
	}

	/////////////////////// nicht mein Zeug ////////////////////////////////////////

	public static Document file2Dom(String filename) throws Exception {
		FileInputStream inStream = new FileInputStream(new File(filename));
		return XercesHelper.inputstream2Dom(inStream);
	}
}