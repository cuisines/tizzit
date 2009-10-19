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

import java.util.Enumeration;

import javax.swing.tree.DefaultTreeModel;

import org.apache.log4j.Logger;

/**
 * <b>Tizzit Enterprise Content Management</b><br/>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class CmsTreeModel extends DefaultTreeModel {
	private static Logger log = Logger.getLogger(CmsTreeModel.class);

	public CmsTreeModel(TreeNode treeNode) {
		super(treeNode);
		treeNode.setModel(this);
		treeNode.loadChildren();
	}

	public PageNode findEntry4Id(TreeNode treeNode, int viewComponentId) {
		if (treeNode instanceof PageNode && ((PageNode) treeNode).getViewId() == viewComponentId) { return (PageNode) treeNode; }
		if (!treeNode.isLeaf()) {
			Enumeration enume = treeNode.children();
			PageNode temp;

			while (enume.hasMoreElements()) {
				try {
					temp = findEntry4Id((TreeNode) enume.nextElement(), viewComponentId);
				} catch (ClassCastException ex) {
					break;
				}
				if (temp != null) { return temp; }
			}
		}
		return null;
	}
}
