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
package de.juwimm.cms.gui.views.menuentry;

import static de.juwimm.cms.common.Constants.*;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.exceptions.ViewComponentLinkNameAlreadyExisting;
import de.juwimm.cms.exceptions.ViewComponentLinkNameIsEmptyException;
import de.juwimm.cms.exceptions.ViewComponentNotFound;
import de.juwimm.cms.util.ActionHub;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.ViewComponentValue;

/**
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class PanMenuentrySeparator extends PanMenuentry {
	private static Logger log = Logger.getLogger(PanMenuentrySeparator.class);
	private Communication comm;

	public PanMenuentrySeparator(Communication comm) {
		super();
		this.comm = comm;
		try {
			jbInit();
		} catch (Exception ex) {
		}
	}

	void jbInit() throws Exception {
	}

	public void save() throws Exception {
		super.save();
		try {
			comm.saveViewComponent(this.getViewComponent());
			ActionHub.fireActionPerformed(new ActionEvent(this.getViewComponent(), ActionEvent.ACTION_PERFORMED, Constants.ACTION_TREE_ENTRY_NAME));
		} catch (ViewComponentLinkNameAlreadyExisting vc) {
			JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("exception.ViewComponentLinkNameAlreadyExisting"), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
		} catch (ViewComponentNotFound vn) {
			JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("exception.ViewComponentNotFound"), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
		} catch (ViewComponentLinkNameIsEmptyException ve) {
			JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("exception.ViewComponentLinkNameIsEmpty"), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
		}
	}

	public void load(ViewComponentValue vcd) {
		super.load(vcd);
		getLblUrlLinkName().setVisible(false);
		getTxtUrlLinkName().setVisible(false);
		getChkSearchIndexed().setVisible(false);
		getChkXmlSearchIndexed().setVisible(false);
		getLblLinkDescription().setVisible(false);
		getTxtLinkDescription().setVisible(false);
	}

}