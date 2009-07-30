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
package de.juwimm.cms.content.frame.tree;

import static de.juwimm.cms.client.beans.Application.*;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.util.Communication;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class ComponentsTreeModel extends DefaultTreeModel {
	private Communication comm = ((Communication) getBean(Beans.COMMUNICATION));

	public ComponentsTreeModel(ComponentNode root) {
		super(root);
	}

	/**
	 * Returns a copy of the current tree that only contains those components that are checked for visibility.
	 * 
	 * @return a copy of the current tree
	 */
	public ComponentsTreeModel getSelectedNodesModel() {
		ComponentNode rootNew = (ComponentNode) this.getRoot();
		ComponentsTreeModel model = new ComponentsTreeModel((ComponentNode) rootNew.clone());

		fillSelectedNodes((ComponentNode) this.getRoot(), (ComponentNode) model.getRoot());
		return model;
	}

	/**
	 * Builds the new tree belonging to the root {@code node}. 
	 * This tree will be a copy of the original tree belonging to the {@code rootNode}, but only contains those children nodes
	 * that were checked for being visible.
	 * 
	 * @param rootNode the original's tree root node
	 * @param node the new tree's root node
	 */
	private void fillSelectedNodes(ComponentNode rootNode, ComponentNode node) {
		ComponentNode childNode = null;
		ComponentNode childNodeClone = null;
		Object obj;

		Enumeration enume = rootNode.children(); 
		while (enume.hasMoreElements()) {
			obj = enume.nextElement();
			if (!(obj instanceof ComponentNode)) continue;

			childNode = (ComponentNode) obj;
			if (childNode.isCheckSelected()) {
				childNodeClone = (ComponentNode) childNode.clone();
				node.add(childNodeClone);
			}
			if (childNode.hasChildren()) {
				fillSelectedNodes(childNode, childNodeClone);
			}
		}
	}
	
	public ArrayList<ComponentNode> getSelectedNodes() {
		ComponentNode rootNew = (ComponentNode) this.getRoot();
		ComponentsTreeModel model = new ComponentsTreeModel((ComponentNode) rootNew.clone());

		ArrayList<ComponentNode> al = new ArrayList<ComponentNode>();
		if (this.getRoot() instanceof ComponentNode) {
			if (((ComponentNode) this.getRoot()).isCheckSelected()) {
				al.add((ComponentNode) this.getRoot());
				al.addAll(findSelectedNodes((ComponentNode) this.getRoot(), (ComponentNode) model.getRoot(),
						new ArrayList<ComponentNode>()));
				return al;
			}
		} else {
			al = findSelectedNodes((ComponentNode) this.getRoot(), (ComponentNode) model.getRoot(), new ArrayList<ComponentNode>());
		}
		return al;
	}
	private ArrayList<ComponentNode> findSelectedNodes(ComponentNode rootNode, ComponentNode node, ArrayList<ComponentNode> vec) {
		ComponentNode temp = null;
		ComponentNode tempNew = null;
		Object obj;

		for (Enumeration enume = rootNode.children(); enume.hasMoreElements();) {
			obj = enume.nextElement();

			if (!(obj instanceof ComponentNode)) continue;

			temp = (ComponentNode) obj;

			if (temp.isCheckSelected()) {
				tempNew = (ComponentNode) temp.clone();
				vec.add(tempNew);

			}

			if (temp.hasChildren()) {
				vec.addAll(findSelectedNodes(temp, tempNew, new ArrayList<ComponentNode>()));
			}
		}
		return vec;
	}

	public void expandPath4CheckedNodes(JTree tree) {
		expandPathForCheckedNodes(tree, (ComponentNode) tree.getModel().getRoot());
	}

	private ComponentNode expandPathForCheckedNodes(JTree tree, ComponentNode node) {
		ComponentNode temp = null;
		ComponentNode tempNew = null;
		Object obj;

		if (!node.hasChildren() && node.isCheckSelected()) {
			TreePath path = new TreePath(node.getPath());
			tree.expandPath(path);
			return node;
		}

		for (Enumeration enume = node.children(); enume.hasMoreElements();) {
			obj = enume.nextElement();

			if (!(obj instanceof ComponentNode)) continue;

			temp = (ComponentNode) obj;

			if (temp.isCheckSelected() && expandPathForCheckedNodes(tree, temp) == null) {
				TreePath path = new TreePath(temp.getPath());
				tree.expandPath(path);
				tempNew = temp;
			}
		}
		return tempNew;
	}
}