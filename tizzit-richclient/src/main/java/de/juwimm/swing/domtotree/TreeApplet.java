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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;

import javax.swing.JApplet;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: JuwiMacMillanGroup</p>
 * @author Michael Meyer
 * @version 1.0
 */
public class TreeApplet extends JApplet {
	private Color click, normal;
	private TreeModel treemodel;
	private TreeAppletSelectionListener treelistener;
	private JScrollPane scrollPane = new JScrollPane();
	private JTree booktree = new JTree();
	private TreeSelectionModel treeSelectionModel1 = new javax.swing.tree.DefaultTreeSelectionModel();

	public TreeApplet() {
		try {
			jbInit();
		} catch (Exception e) {
		}
	}

	private void jbInit() {
		booktree.setFont(new java.awt.Font("SansSerif", 0, 11));
		booktree.setAlignmentX((float) 0.5);
		booktree.setSelectionModel(treeSelectionModel1);
		booktree.setToggleClickCount(3);
		booktree.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				booktreeMouseClicked(e);
			}
		});
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(booktree, null);
	}

	/**
	 * Set the treemodel for the tree.
	 *
	 * @param model The treemodel
	 * @param c Color when a node is not selected.
	 * @param n Color when the node is selected.
	 */

	public void setTreeModel(TreeModel model, Color c, Color n) {
		//    TreeRenderer r = new TreeRenderer();
		click = c;
		normal = n;
		ColorTreeRenderer r = new ColorTreeRenderer(click, normal);
		booktree.setCellRenderer(r);
		treemodel = model;
		treelistener = new TreeAppletSelectionListener();
		booktree.addTreeSelectionListener(treelistener);
		booktree.addTreeExpansionListener(treelistener);
		booktree.setModel(treemodel);
	}

	public void setNewTreeModel(String xmlfile) {

	}

	private void booktreeMouseClicked(MouseEvent e) {
		try {
			JTree tr = (JTree) e.getSource();
			int r = tr.getRowForLocation(e.getX(), e.getY());
			TreePath path = tr.getPathForRow(r);
			if (path != null) {
				MyTreeNode node = (MyTreeNode) tr.getLastSelectedPathComponent();
				//        node.setCheckBox();
				try {
					AktColorNode.getColorNode().setColor(normal);
				} catch (Exception ex) {
					System.out.println("aktnode");
				}
				node.setColor(click);
				tr.repaint();
			}
		} catch (Exception ex) {
			System.out.println("mouse :" + ex.getMessage());
		}
	}

}