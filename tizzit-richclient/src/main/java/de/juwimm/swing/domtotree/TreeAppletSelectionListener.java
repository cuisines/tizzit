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

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;

import org.tizzit.util.XercesHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * <p>Title: DomToTree</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: JuwiMacMillan</p>
 * @author Michael Meyer
 * @version 1.0
 */
public class TreeAppletSelectionListener implements TreeSelectionListener, TreeExpansionListener {

	public TreeAppletSelectionListener() {
	}

	/* belongs to TreeSelectionListener */

	public void valueChanged(TreeSelectionEvent e) {
		try {
			Object ob = e.getSource();
			JTree tree = (JTree) ob;
			MyTreeNode node = (MyTreeNode) tree.getLastSelectedPathComponent();
			Element el = node.getDomeNode();
			String t = node.getElementTitle();
			Document doc = getDocumentFromElement(t, el);
			DocbookParser p = new DocbookParser(doc, "title", Color.white);
			p.setStandardTag("role", "standard");
			p.start();
			p.removeAllStandards();
			DefaultTreeModel model = p.getTreeModel();
			tree.setModel(model);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	private Document getDocumentFromElement(String t, Element el) {
		Document doc = XercesHelper.getNewDocument();
		//Element root = doc.createElement("root");
		try {
			Node newEl = doc.importNode(el, true);
			doc.appendChild(newEl);
		} catch (Exception ex) {
		}
		return doc;
	}

	/* belongs to TreeExpansionListener */

	public void treeExpanded(TreeExpansionEvent event) {

	}

	public void treeCollapsed(TreeExpansionEvent event) {

	}

}