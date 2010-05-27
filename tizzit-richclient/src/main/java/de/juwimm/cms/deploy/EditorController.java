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

import static de.juwimm.cms.common.Constants.rb;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.log4j.Logger;

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.deploy.frame.FrmWizard;
import de.juwimm.cms.deploy.panel.wizard.PanAuthorChooseEditor;
import de.juwimm.cms.deploy.panel.wizard.PanEditorApproveMessage;
import de.juwimm.cms.deploy.panel.wizard.PanEditorEditions;
import de.juwimm.cms.deploy.panel.wizard.PanEditorStart;
import de.juwimm.cms.deploy.panel.wizard.PanFinally;
import de.juwimm.cms.deploy.panel.wizard.WizardPanel;
import de.juwimm.cms.util.UIConstants;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class EditorController implements ActionListener {
	private static Logger log = Logger.getLogger(EditorController.class);
	public static final int STAGE_START = 0;
	public static final int STAGE_APPROVE_COMMENT = 11;
	public static final int STAGE_OLDEDITIONSDELETE_SHOWLIST = 21;
	public static final int STAGE_SEND_COMMENT = 31;
	public static final int STAGE_FINALLY = 99;

	private FrmWizard wiz = null;
	private PanEditorStart aPanStart = null;
	private PanEditorApproveMessage bPanApproveMessage = null;
	private PanEditorEditions cPanEditions = null;
	private PanAuthorChooseEditor dPanComment = null;
	private PanFinally panFinally = null;

	private int stage = 0;
	private final int unitId;
	private int latestStageEver = 0;

	public EditorController(Integer unitId) {
		this.unitId = unitId.intValue();
		aPanStart = new PanEditorStart(unitId);
		wiz = new FrmWizard(this, UIConstants.WIZARD_ICON_INSTALL, Constants.rb.getString("wizard.editor.start.title"), Constants.rb.getString("wizard.editor.start.introMessage"), aPanStart);
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
					case EditorController.STAGE_START:
						String acomm = aPanStart.getBtnGrpOptions().getSelection().getActionCommand();
						this.stage = new Integer(acomm).intValue();
						paintPanelForStage(stage);
						wiz.setCursor(Cursor.getDefaultCursor());
						break;
					case EditorController.STAGE_APPROVE_COMMENT:
						this.stage = EditorController.STAGE_FINALLY;
						paintPanelForStage(stage);
						panFinally.setWay("APPROVED");
						wiz.setCursor(Cursor.getDefaultCursor());
						break;
					case EditorController.STAGE_OLDEDITIONSDELETE_SHOWLIST:
						this.stage = EditorController.STAGE_FINALLY;
						paintPanelForStage(stage);
						panFinally.setWay("OLDEDITIONS");
						wiz.setCursor(Cursor.getDefaultCursor());
						break;
					case EditorController.STAGE_SEND_COMMENT:
						this.stage = EditorController.STAGE_FINALLY;
						paintPanelForStage(stage);
						panFinally.setWay("COMMENT");
						wiz.setCursor(Cursor.getDefaultCursor());
						break;
					case EditorController.STAGE_FINALLY:
						log.info("WRITING COLLECTED INFORMATIONS");
						Thread t = new Thread(new Runnable() {
							public void run() {
								wiz.setCancelEnabled(false);
								wiz.setNextEnabled(false);
								wiz.setBackEnabled(false);
								wiz.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
								if (panFinally.getWay().equals("APPROVED")) {
									bPanApproveMessage.save();
								} else if (panFinally.getWay().equals("OLDEDITIONS")) {
									cPanEditions.save();
								} else if (panFinally.getWay().equals("COMMENT")) {
									dPanComment.save();
								}
								wiz.setCursor(Cursor.getDefaultCursor());
								wiz.setVisible(false);
								wiz.dispose();
							}
						});
						t.setPriority(Thread.NORM_PRIORITY);
						t.start();
						break;
					default:
				}
				if (latestStageEver > stage) {
					wiz.setNextEnabled(true);
				} else {
					latestStageEver = stage;
				}
				break;
			case FrmWizard.ACTION_WIZARD_PREVIOUS:
				switch (stage) {
					case EditorController.STAGE_APPROVE_COMMENT:
					case EditorController.STAGE_OLDEDITIONSDELETE_SHOWLIST:
					case EditorController.STAGE_SEND_COMMENT:
						this.stage = EditorController.STAGE_START;
						paintPanelForStage(stage);
						break;
					case EditorController.STAGE_FINALLY:
						if (panFinally.getWay().equals("APPROVED")) {
							this.stage = EditorController.STAGE_APPROVE_COMMENT;
						} else if (panFinally.getWay().equals("OLDEDITIONS")) {
							this.stage = EditorController.STAGE_OLDEDITIONSDELETE_SHOWLIST;
						} else if (panFinally.getWay().equals("COMMENT")) {
							this.stage = EditorController.STAGE_SEND_COMMENT;
						}
						wiz.setNextAsFinally(false);
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
			case EditorController.STAGE_START:
				if (aPanStart == null) aPanStart = new PanEditorStart();
				wpan = aPanStart;
				wpan.setUnitId(unitId);
				wiz.setPanel(aPanStart, rb.getString("wizard.editor.start.introMessage"));
				wiz.setBackEnabled(false);
				break;
			case EditorController.STAGE_APPROVE_COMMENT:
				if (bPanApproveMessage == null) bPanApproveMessage = new PanEditorApproveMessage();
				wpan = bPanApproveMessage;
				wpan.setUnitId(unitId);
				wiz.setPanel(bPanApproveMessage, rb.getString("wizard.editor.approveMessage.introMessage"));
				wiz.setBackEnabled(true);
				break;
			case EditorController.STAGE_OLDEDITIONSDELETE_SHOWLIST:
				if (cPanEditions == null) cPanEditions = new PanEditorEditions();
				wpan = cPanEditions;
				wpan.setUnitId(unitId);
				wiz.setPanel(cPanEditions, rb.getString("wizard.editor.edition.introMessage"));
				wiz.setBackEnabled(true);
				break;
			case EditorController.STAGE_SEND_COMMENT:
				if (dPanComment == null) dPanComment = new PanAuthorChooseEditor(true);
				wpan = dPanComment;
				wpan.setUnitId(unitId);
				wiz.setPanel(dPanComment, rb.getString("wizard.author.chooseEditor.prosa"));
				wiz.setBackEnabled(true);
				break;
			case EditorController.STAGE_FINALLY:
				if (panFinally == null) panFinally = new PanFinally();
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
