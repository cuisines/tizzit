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
package de.juwimm.cms.deploy;

import static de.juwimm.cms.common.Constants.*;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.deploy.frame.FrmWizard;
import de.juwimm.cms.deploy.panel.wizard.*;
import de.juwimm.cms.util.UIConstants;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class AuthorController implements ActionListener {
	private static Logger log = Logger.getLogger(AuthorController.class);
	private FrmWizard wiz = null;
	private PanAuthorStart aPanStart = null;
	private PanAuthorShowList bPanList = null;
	private PanAuthorChooseEditor cPanEditor = null;
	private PanRevokeShowList dPanList = null;
	private PanAuthorChooseEditor ePanEditor = null;
	private PanFinally panFinally = null;
	private int stage = 0;
	private int unitId;
	private int latestStageEver = 0;

	public static final int STAGE_START = 0;
	public static final int STAGE_APPROVAL_SHOW_LIST = 11;
	public static final int STAGE_APPROVAL_SELECT_EDITOR = 12;
	public static final int STAGE_REVOKE_SHOW_LIST = 21;
	public static final int STAGE_REVOKE_SEND_COMMENT = 22;
	public static final int STAGE_SEND_COMMENT = 31;
	public static final int STAGE_FINALLY = 99;

	public AuthorController(int unitId) {
		this.unitId = unitId;
		aPanStart = new PanAuthorStart();
		wiz = new FrmWizard(this, UIConstants.WIZARD_ICON_INSTALL, rb.getString("wizard.author.title"),
				Constants.rb.getString("wizard.author.introMessage"), aPanStart);
		wiz.showWizard();
		wiz.setNextEnabled(false);
	}

	/**
	 * Here we will get the SwingMessages from the Wizard
	 * @param ae ActionEvent
	 */
	public void actionPerformed(ActionEvent ae) {
		switch (ae.getID()) {
			case FrmWizard.ACTION_WIZARD_CANCEL:
				wiz.setVisible(false);
				wiz.dispose();
				break;
			case FrmWizard.ACTION_WIZARD_NEXT:
				wiz.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				switch (stage) {
					case AuthorController.STAGE_START:
						String acomm = aPanStart.getBtnGrpOptions().getSelection().getActionCommand();
						this.stage = new Integer(acomm).intValue();
						paintPanelForStage(stage);
						break;
					case AuthorController.STAGE_APPROVAL_SHOW_LIST:
						this.stage = AuthorController.STAGE_APPROVAL_SELECT_EDITOR;
						paintPanelForStage(stage);
						break;
					case AuthorController.STAGE_APPROVAL_SELECT_EDITOR:
						this.stage = AuthorController.STAGE_FINALLY;
						paintPanelForStage(stage);
						panFinally.setWay("APPROVAL");
						break;
					case AuthorController.STAGE_REVOKE_SHOW_LIST:
						this.stage = AuthorController.STAGE_REVOKE_SEND_COMMENT;
						paintPanelForStage(stage);
						break;
					case AuthorController.STAGE_REVOKE_SEND_COMMENT:
						this.stage = AuthorController.STAGE_FINALLY;
						paintPanelForStage(stage);
						panFinally.setWay("REVOKE");
						wiz.setBackEnabled(false);
						break;
					case AuthorController.STAGE_SEND_COMMENT:
						this.stage = AuthorController.STAGE_FINALLY;
						paintPanelForStage(stage);
						panFinally.setWay("SENDMAIL");
						wiz.setBackEnabled(false);
						break;
					case AuthorController.STAGE_FINALLY:
						log.info("WRITING COLLECTED INFORMATIONS");
						if (panFinally.getWay().equals("APPROVAL")) {
							cPanEditor.save();
							bPanList.setTaskId(cPanEditor.getCreatedTaskId());
							bPanList.save();
							Thread t = new Thread(new Runnable() {
								public void run() {
									JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb
											.getString("wizard.author.completeMessage"), rb.getString("dialog.title"),
											JOptionPane.INFORMATION_MESSAGE);
								}
							});
							t.setPriority(Thread.NORM_PRIORITY);
							t.start();
						} else if (panFinally.getWay().equals("REVOKE")) {
							dPanList.save();
							ePanEditor.save();
						} else if (panFinally.getWay().equals("SENDMAIL")) {
							cPanEditor.save();
						}
						wiz.setCursor(Cursor.getDefaultCursor());
						wiz.setVisible(false);
						wiz.dispose();
						break;
					default:
				}
				wiz.setCursor(Cursor.getDefaultCursor());
				if (latestStageEver > stage) {
					wiz.setNextEnabled(true);
				} else {
					//wiz.setNextEnabled(false);
					latestStageEver = stage;
				}
				break;
			case FrmWizard.ACTION_WIZARD_PREVIOUS:
				switch (stage) {
					case AuthorController.STAGE_APPROVAL_SHOW_LIST:
						paintPanelForStage(AuthorController.STAGE_START);
						stage = AuthorController.STAGE_START;
						break;
					case AuthorController.STAGE_APPROVAL_SELECT_EDITOR:
						paintPanelForStage(AuthorController.STAGE_APPROVAL_SHOW_LIST);
						stage = AuthorController.STAGE_APPROVAL_SHOW_LIST;
						break;
					case AuthorController.STAGE_FINALLY:
						paintPanelForStage(AuthorController.STAGE_APPROVAL_SELECT_EDITOR);
						stage = AuthorController.STAGE_APPROVAL_SELECT_EDITOR;
						wiz.setNextAsFinally(false);
						break;
					case AuthorController.STAGE_REVOKE_SHOW_LIST:
						this.stage = AuthorController.STAGE_START;
						paintPanelForStage(stage);
						break;
					case AuthorController.STAGE_REVOKE_SEND_COMMENT:
						this.stage = AuthorController.STAGE_REVOKE_SHOW_LIST;
						paintPanelForStage(stage);
						break;
					case AuthorController.STAGE_SEND_COMMENT:
						this.stage = AuthorController.STAGE_START;
						paintPanelForStage(stage);
						break;
					default:
				}
				break;
			default:
		}
	}

	private void paintPanelForStage(int selStage) {
		WizardPanel wpan = null;
		switch (selStage) {
			case AuthorController.STAGE_START:
				if (aPanStart == null) {
					aPanStart = new PanAuthorStart();
				}
				wpan = aPanStart;
				wpan.setUnitId(unitId);
				wiz.setPanel(aPanStart, rb.getString("wizard.author.introMessage"));
				wiz.setBackEnabled(false);
				break;
			case AuthorController.STAGE_APPROVAL_SHOW_LIST:
				if (bPanList == null) {
					bPanList = new PanAuthorShowList();
				}
				wpan = bPanList;
				wpan.setUnitId(unitId);
				wiz.setPanel(bPanList, rb.getString("wizard.author.getApproval.prosa"));
				wiz.setBackEnabled(true);
				wiz.setNextEnabled(false);
				break;
			case AuthorController.STAGE_APPROVAL_SELECT_EDITOR:
				if (cPanEditor == null) {
					cPanEditor = new PanAuthorChooseEditor();
				}
				wpan = cPanEditor;
				wpan.setUnitId(unitId);
				wiz.setPanel(cPanEditor, rb.getString("wizard.author.chooseEditor.prosa"));
				wiz.setBackEnabled(true);
				break;
			case AuthorController.STAGE_REVOKE_SHOW_LIST:
				if (dPanList == null) {
					dPanList = new PanRevokeShowList();
				}
				wpan = dPanList;
				wpan.setUnitId(unitId);
				wiz.setPanel(dPanList, rb.getString("wizard.author.revoke.prosa"));
				wiz.setBackEnabled(true);
				wiz.setNextEnabled(false);
				break;
			case AuthorController.STAGE_REVOKE_SEND_COMMENT:
				if (ePanEditor == null) {
					ePanEditor = new PanAuthorChooseEditor();
				}
				wpan = ePanEditor;
				wpan.setUnitId(unitId);
				wiz.setPanel(ePanEditor, rb.getString("wizard.author.revoke.chooseEditor.prosa"));
				wiz.setBackEnabled(true);
				break;
			case AuthorController.STAGE_SEND_COMMENT:
				if (cPanEditor == null) {
					cPanEditor = new PanAuthorChooseEditor(true);
				}
				wpan = cPanEditor;
				wpan.setUnitId(unitId);
				wiz.setPanel(cPanEditor, rb.getString("wizard.author.chooseEditor.prosa"));
				wiz.setBackEnabled(true);
				break;
			case AuthorController.STAGE_FINALLY:
				if (panFinally == null) {
					panFinally = new PanFinally();
				}
				wpan = panFinally;
				panFinally.setProsa(rb.getString("wizard.author.getApproval.finallyprosa"));
				wpan.setUnitId(unitId);
				wiz.setPanel(panFinally, rb.getString("wizard.author.finally.prosa"));
				wiz.setBackEnabled(true);
				break;
			default:
		}
	}
}
