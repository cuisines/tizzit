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
import javax.swing.tree.DefaultTreeModel;

import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.ViewComponentValue;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PageOtherUnitNode extends PageNode {
	public PageOtherUnitNode(ViewComponentValue value, DefaultTreeModel treeModel) {
		super(value, treeModel);
		init();
	}

	public void init() {
		if(getViewComponent().getDisplayLinkName() == null || getViewComponent().getDisplayLinkName().equals("")) {
			getViewComponent().setDisplayLinkName("[neuer Content]");
		}
		/*if(viewComponent.isUnit()) {
			viewComponent.setViewIndex("2");
		} else {
			viewComponent.setViewIndex("3");
		}*/
		getViewComponent().setViewLevel("3");
		this.removeAllChildren();
	}

	public ImageIcon getIcon() {
		return UIConstants.CONTENT_NO_ACCESS;
	}
	
	public ImageIcon getSuperIcon() {
		return super.getIcon();
	}

	public boolean loadChildren() {
	    //nothing to load, so we overwrite it here
	    return false;
	}
	
	public boolean isAppendingAllowed() {
		return false;
	}
}
