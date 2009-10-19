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
public class PageExternallinkNode extends PageNode {
	public PageExternallinkNode(ViewComponentValue value, DefaultTreeModel treeModel) {
		super(value, treeModel);

		if (getViewComponent().getDisplayLinkName() == null || getViewComponent().getDisplayLinkName().equals("")) {
			getViewComponent().setDisplayLinkName("[neuer Link]");
		}
		this.getViewComponent().setDisplaySettings((byte) (this.getViewComponent().getDisplaySettings() | Constants.DISPLAY_SETTING_NEW_WINDOW));
	}

	@Override
	public ImageIcon getIcon() {
		switch (getViewComponent().getStatus()) {
			case Constants.DEPLOY_STATUS_EDITED:
				if (getViewComponent().getOnline() == Constants.ONLINE_STATUS_ONLINE) { return UIConstants.EXTERNALLINK_EDITED_LIVE; }
				return UIConstants.EXTERNALLINK_EDITED;
			case Constants.DEPLOY_STATUS_FOR_APPROVAL:
				if (getViewComponent().getDeployCommand() == Constants.DEPLOY_COMMAND_ADD) {
					if (getViewComponent().getOnline() == Constants.ONLINE_STATUS_ONLINE) { return UIConstants.EXTERNALLINK_APPROVAL_LIVE; }
					return UIConstants.EXTERNALLINK_APPROVAL;
				}
				if (getViewComponent().getOnline() == Constants.ONLINE_STATUS_ONLINE) { return UIConstants.EXTERNALLINK_DELETE_LIVE; }
				return UIConstants.EXTERNALLINK_DELETE;
			case Constants.DEPLOY_STATUS_APPROVED:
				if (getViewComponent().getOnline() == Constants.ONLINE_STATUS_ONLINE) { return UIConstants.EXTERNALLINK_APPROVED_LIVE; }
				return UIConstants.EXTERNALLINK_APPROVED;
			default:
				return UIConstants.EXTERNALLINK_DEPLOYED_LIVE;
		}
	}

	@Override
	public boolean isAppendingAllowed() {
		return false;
	}

}
