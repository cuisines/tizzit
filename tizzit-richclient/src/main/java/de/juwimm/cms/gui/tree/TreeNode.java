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
package de.juwimm.cms.gui.tree;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import de.juwimm.cms.util.UIConstants;

/**
 * This is the basic TreeNode seen in the Content Management in the left site and can represent a page,
 * a plugin or whatever.
 * <b>Tizzit Enterprise Content Management</b><br/>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class TreeNode extends DefaultMutableTreeNode {
	private DefaultTreeModel treeModel;
	private boolean init = false;

	public TreeNode() {
		super();
	}

	public TreeNode(String title) {
		super(title);
	}

	public void setInit(boolean init) {
		this.init = init;
	}

	public boolean isInit() {
		return init;
	}

	public void setModel(DefaultTreeModel treeModel) {
		setTreeModel(treeModel);
	}

	public ImageIcon getIcon() {
		return UIConstants.CMS;
	}

	/**
	 * @return Returns if the children or the current one has been reloaded
	 */
	public boolean loadChildren() {
		return false;
	}

	/**
	 * returns children of this and loads also an additional the path to a certain selection
	 * @param viewComponentId to this node will be constructed the extra path
	 * @return
	 */
	public boolean loadChildrenWithViewComponent(Integer viewComponentId) {
		return false;
	}

	public void update(Object daoObject) {

	}

	public boolean isDeleteable() {
		return false;
	}

	public boolean isMoveableToUp() {
		return false;
	}

	public boolean isMoveableToDown() {
		return false;
	}

	public boolean isMoveableToLeft() {
		return false;
	}

	public boolean isMoveableToRight() {
		return false;
	}

	public boolean isEditable() {
		return false;
	}

	public boolean isAppendingAllowed() {
		return false;
	}

	/**
	 * @param treeModel The treeModel to set.
	 */
	protected void setTreeModel(DefaultTreeModel treeModel) {
		this.treeModel = treeModel;
	}

	/**
	 * @return Returns the treeModel.
	 */
	protected DefaultTreeModel getTreeModel() {
		return treeModel;
	}
}