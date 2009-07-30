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
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import org.w3c.dom.Element;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class MyTreeNode extends DefaultMutableTreeNode {
	private boolean selected;
	private Color color;
	private boolean standard;
	private Element node;

	public MyTreeNode(String v, boolean leaf) {
		super(v, leaf);
		selected = true;
	}

	public MyTreeNode(String v, Color c, boolean leaf, boolean standard) {
		super(v);
		this.setAllowsChildren(true);
		color = c;
		this.standard = standard;
	}

	/**
	 * Set the Domnode for this MyTreeNode object
	 * @param n
	 */
	public void setDomNode(Element n) {
		node = n;
	}

	public Element getDomeNode() {
		return node;
	}

	public String getElementTitle() {
		return this.userObject.toString();
	}

	/**
	 * Call this function to change the color when the Node was selected in the tree.
	 * @param c Color of selected Nodes
	 */

	public void setColor(Color c) {
		color = c;
		AktColorNode.setColorNode(this);
	}

	public void changeColorToNormal(Color c) {
		color = c;
	}

	public Color getColor() {
		return color;
	}

	/**
	 * Returns information about the standardvalue
	 * @return
	 */

	public boolean getStandard() {
		return standard;
	}

	/**
	 * Set this MyTreeNode to a standard Node
	 * @param st
	 */

	public void setStandard(boolean st) {
		standard = st;
	}

	public boolean setCheckBox() {
		if (selected) {
			selected = false;
			setChildrenCheckBox(this, selected);
			return false;
		} else {
			selected = true;
			setParentCheckBox(this);
			setChildrenCheckBox(this, selected);
		}
		return true;
	}

	public boolean getCheckBox() {
		return selected;
	}

	public void setCheckBox(boolean v) {
		selected = v;
	}

	private void setChildrenCheckBox(MyTreeNode node, boolean v) {
		try {
			Vector vc = node.children;
			for (int i = 0; i < vc.size(); i++) {
				MyTreeNode n = (MyTreeNode) vc.elementAt(i);
				n.setCheckBox(v);
				if (n.getChildCount() > 0) {
					setChildrenCheckBox(n, v);
				}
			}
		} catch (Exception ex) {
		}
	}

	private void setParentCheckBox(MyTreeNode node) {
		try {
			MyTreeNode parent = (MyTreeNode) node.getParent();
			if (!parent.getCheckBox()) {
				parent.setCheckBox();
				setParentCheckBox(parent);
			}
		} catch (Exception ex) {
		}
	}

}