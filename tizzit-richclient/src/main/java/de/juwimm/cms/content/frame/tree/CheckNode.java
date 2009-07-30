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

import java.util.Enumeration;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class CheckNode extends DefaultMutableTreeNode {
	public static final int SINGLE_SELECTION = 0;
	public static final int DIG_IN_SELECTION = 4;
	private int selectionMode;
	private boolean isSelected;
	private boolean isCheckSelected;
	private boolean tmpCheckSel;
	private Icon icon;
	private String strTooltiptext = "";

	public CheckNode() {
		this(null);
	}

	public CheckNode(Object userObject) {
		this(userObject, true, false);
	}

	public CheckNode(Object userObject, boolean allowsChildren, boolean isSelected) {
		super(userObject, allowsChildren);
		this.setSelected(isSelected);
		setSelectionMode(SINGLE_SELECTION);
	}

	public void setSelectionMode(int mode) {
		selectionMode = mode;
	}

	public int getSelectionMode() {
		return selectionMode;
	}

	public void setToolTipText(String tool) {
		this.strTooltiptext = tool;
	}

	public String getToolTipText() {
		return this.strTooltiptext;
	}

	public void setCheckSelected(boolean isCheckSelected) {
		if (this.isRoot() && !isCheckSelected) return;

		if (isCheckSelected) {
			setParentsCheckedUp2Root(this);
			this.isCheckSelected = isCheckSelected;
		} else {
			if (!isOneChildSelected(this)) this.isCheckSelected = isCheckSelected;

			checkParentsUp2Root(this);
		}

		if ((selectionMode == DIG_IN_SELECTION) && (children != null)) {
			Enumeration enume = children.elements();
			while (enume.hasMoreElements()) {
				CheckNode node = (CheckNode) enume.nextElement();
				node.setCheckSelected(isSelected());
			}
		}
	}

	/**
	 * select checked for all parents up to root
	 * @param node
	 */
	private void setParentsCheckedUp2Root(CheckNode node) {
		CheckNode cn;
		if ((cn = (CheckNode) node.getParent()) == null) return;
		cn.setCheckSelected(true);
		setParentsCheckedUp2Root(cn);
	}

	/**
	 * if the node is unchecked and no more node is check, then uncheck the
	 * parent. This function goes recursive up to the root parent.
	 */
	private void checkParentsUp2Root(CheckNode node) {
		CheckNode cn;
		if ((cn = (CheckNode) node.getParent()) == null) return;
		if (cn.isCheckSelected() && !isOneChildSelected(cn)) cn.setCheckSelected(false);
		checkParentsUp2Root(cn);
	}

	private boolean isOneChildSelected(CheckNode parentNode) {
		ComponentNode temp;
		for (Enumeration enume = parentNode.children(); enume.hasMoreElements();) {
			try {
				temp = (ComponentNode) enume.nextElement();
			} catch (Exception ex) {
				continue;
			}
			if (temp.isCheckSelected()) return true;
			if (temp.getChildCount() > 0) isOneChildSelected(temp);
		}
		return false;
	}

	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}

	public Icon getIcon() {
		return this.icon;
	}

	public boolean isCheckSelected() {
		return isCheckSelected;
	}

	/**
	 * @param isSelected The isSelected to set.
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	/**
	 * @return Returns the isSelected.
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * @param tmpCheckSel The tmpCheckSel to set.
	 */
	protected void setTmpCheckSel(boolean tmpCheckSel) {
		this.tmpCheckSel = tmpCheckSel;
	}

	/**
	 * @return Returns the tmpCheckSel.
	 */
	protected boolean isTmpCheckSel() {
		return tmpCheckSel;
	}


}