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
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class PageSeparatorNode extends PageNode {

	public PageSeparatorNode(ViewComponentValue value, DefaultTreeModel treeModel) {
		super(value, treeModel);
	}

	public ImageIcon getIcon() {
		switch (getViewComponent().getStatus()) {
			case Constants.DEPLOY_STATUS_EDITED:
				if (getViewComponent().getOnline() == Constants.ONLINE_STATUS_ONLINE) {
					return UIConstants.SEPARATOR_EDITED_LIVE;
				}
				return UIConstants.SEPARATOR_EDITED;
			case Constants.DEPLOY_STATUS_FOR_APPROVAL:
				if (getViewComponent().getDeployCommand() == Constants.DEPLOY_COMMAND_ADD) {
					if (getViewComponent().getOnline() == Constants.ONLINE_STATUS_ONLINE) {
						return UIConstants.SEPARATOR_APPROVAL_LIVE;
					}
					return UIConstants.SEPARATOR_APPROVAL;
				}
				if (getViewComponent().getOnline() == Constants.ONLINE_STATUS_ONLINE) {
					return UIConstants.SEPARATOR_DELETE_LIVE;
				}
				return UIConstants.SEPARATOR_DELETE;
			case Constants.DEPLOY_STATUS_APPROVED:
				if (getViewComponent().getOnline() == Constants.ONLINE_STATUS_ONLINE) {
					return UIConstants.SEPARATOR_APPROVED_LIVE;
				}
				return UIConstants.SEPARATOR_APPROVED;
			default:
				return UIConstants.SEPARATOR_DEPLOYED_LIVE;
		}
	}

	public boolean isAppendingAllowed() {
		return false;
	}

}
