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

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.ViewComponentValue;

/**
 * <b>Tizzit Enterprise Content Management</b><br/>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PageContentNode extends PageNode {
	public PageContentNode(ViewComponentValue value) {
		super(value);
		init();
	}

	public PageContentNode(ViewComponentValue value, DefaultTreeModel treeModel) {
		super(value, treeModel);
		init();
	}

	public void init() {
		if (getViewComponent().getDisplayLinkName() == null || getViewComponent().getDisplayLinkName().equals("")) {
			getViewComponent().setDisplayLinkName("[neuer Content]");
		}
		if (getViewComponent().getViewLevel() == null || getViewComponent().getViewLevel().equalsIgnoreCase("")) {
			getViewComponent().setViewLevel("3");
		}
	}

	@Override
	public ImageIcon getIcon() {
		switch (getViewComponent().getStatus()) {
			case Constants.DEPLOY_STATUS_EDITED:
				if (getViewComponent().getDeployCommand() == Constants.DEPLOY_COMMAND_DELETE || getViewComponent().getDeployCommand() == Constants.DEPLOY_COMMAND_REMOVE) {
					if (getViewComponent().getOnline() == Constants.ONLINE_STATUS_ONLINE) {
						return UIConstants.CONTENT_DELETE_LIVE;
					}
					return UIConstants.CONTENT_DELETE;
				}
				if (getViewComponent().getOnline() == Constants.ONLINE_STATUS_ONLINE) {
					return UIConstants.CONTENT_EDITED_LIVE;
				}
				return UIConstants.CONTENT_EDITED;
			case Constants.DEPLOY_STATUS_FOR_APPROVAL:
				if (getViewComponent().getDeployCommand() == Constants.DEPLOY_COMMAND_DELETE || getViewComponent().getDeployCommand() == Constants.DEPLOY_COMMAND_REMOVE) {
					if (getViewComponent().getOnline() == Constants.ONLINE_STATUS_ONLINE) {
						return UIConstants.CONTENT_APPROVAL_DELETE_LIVE;
					}
					return UIConstants.CONTENT_DELETE;
				}
				if (getViewComponent().getOnline() == Constants.ONLINE_STATUS_ONLINE) {
					return UIConstants.CONTENT_APPROVAL_LIVE;
				}
				return UIConstants.CONTENT_APPROVAL;
			case Constants.DEPLOY_STATUS_APPROVED:
				if (getViewComponent().getOnline() == Constants.ONLINE_STATUS_ONLINE) {
					return UIConstants.CONTENT_APPROVED_LIVE;
				}
				return UIConstants.CONTENT_APPROVED;
			default:
				return UIConstants.CONTENT_DEPLOYED_LIVE;
		}
	}

	@Override
	public boolean isAppendingAllowed() {
		return true;
	}

}
