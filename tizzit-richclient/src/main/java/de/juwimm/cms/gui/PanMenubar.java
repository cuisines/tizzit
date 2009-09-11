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
package de.juwimm.cms.gui;

import static de.juwimm.cms.client.beans.Application.*;
import static de.juwimm.cms.common.Constants.*;

import java.awt.event.ActionListener;

import javax.swing.*;

import org.apache.log4j.Logger;


import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.gui.event.FinishedActionListener;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;

/**
 * <p>Title: ConQuest</p>
 * <p>Description: Enterprise Content Management<</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanMenubar extends JMenuBar {
	private static Logger log = Logger.getLogger(PanMenubar.class);
	
	/** Edit Menu */
	private JMenu mnuEdit = new JMenu(rb.getString("menubar.edit"));
	private JMenuItem mnuEditUndo = new JMenuItem(rb.getString("menubar.edit.undo"), UIConstants.MNU_FILE_EDIT_UNDO);
	private JMenuItem mnuEditRedo = new JMenuItem(rb.getString("menubar.edit.redo"), UIConstants.MNU_FILE_EDIT_REDO);
	private JMenuItem mnuEditCut = new JMenuItem(rb.getString("menubar.edit.cut"), UIConstants.MNU_FILE_EDIT_CUT);
	private JMenuItem mnuEditCopy = new JMenuItem(rb.getString("menubar.edit.copy"), UIConstants.MNU_FILE_EDIT_COPY);
	private JMenuItem mnuEditPaste = new JMenuItem(rb.getString("menubar.edit.paste"), UIConstants.MNU_FILE_EDIT_PASTE);
	private JMenuItem mnuEditDelete = new JMenuItem(rb.getString("menubar.edit.delete"), UIConstants.MNU_EMPTY);
	
	/** Publish Menu */
	private JMenu mnuPublish = new JMenu(rb.getString("ribbon.publish"));
	private JMenuItem mnuPublishLetRelease = new JMenuItem(rb.getString("menubar.publish.letRelease"));
	


	public PanMenubar() {
		try {
			setDoubleBuffered(true);
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	public void addActionListener(ActionListener actionListener) {		
		mnuPublishLetRelease.addActionListener(actionListener);
	}

	void jbInit() throws Exception {


		this.add(mnuEdit);
		mnuEdit.add(mnuEditUndo);
		mnuEdit.add(mnuEditRedo);
		mnuEdit.addSeparator();
		mnuEdit.add(mnuEditCut);
		mnuEdit.add(mnuEditCopy);
		mnuEdit.add(mnuEditPaste);
		mnuEdit.addSeparator();
		mnuEdit.add(mnuEditDelete);
		enableEditMenu(false);


		this.add(mnuPublish);
		//mnuPublish.add(mnuPublishLetRelease);
		//mnuPublishLetRelease.setActionCommand(Constants.ACTION_CONTENT_4APPROVAL);
		mnuPublish.addSeparator();

	}

	
		
	public void setActionForCutCopyPaste(Action cutAction, Action copyAction, Action pasteAction) {
		mnuEditCut.setAction(cutAction);
		mnuEditCut.setText(rb.getString("menubar.edit.cut"));
		mnuEditCut.setIcon(UIConstants.MNU_FILE_EDIT_CUT);
		mnuEditCopy.setAction(copyAction);
		mnuEditCopy.setText(rb.getString("menubar.edit.copy"));
		mnuEditCopy.setIcon(UIConstants.MNU_FILE_EDIT_COPY);
		mnuEditPaste.setAction(pasteAction);
		mnuEditPaste.setText(rb.getString("menubar.edit.paste"));
		mnuEditPaste.setIcon(UIConstants.MNU_FILE_EDIT_PASTE);
	}

	public void enableEditMenu(boolean enable) {
		mnuEdit.setEnabled(enable);
		mnuEditCut.setEnabled(enable);
		mnuEditCopy.setEnabled(enable);
		mnuEditPaste.setEnabled(enable);
		mnuEditUndo.setEnabled(enable);
		mnuEditRedo.setEnabled(enable);
		mnuEditDelete.setEnabled(enable);
	}

}