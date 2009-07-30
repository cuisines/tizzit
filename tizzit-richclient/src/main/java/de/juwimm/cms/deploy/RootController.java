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

import static de.juwimm.cms.client.beans.Application.*;
import static de.juwimm.cms.common.Constants.*;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import de.juwimm.cms.Messages;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.deploy.frame.FrmWizard;
import de.juwimm.cms.deploy.panel.wizard.PanFinally;
import de.juwimm.cms.deploy.panel.wizard.PanRootStart;
import de.juwimm.cms.deploy.panel.wizard.WizardPanel;
import de.juwimm.cms.exceptions.*;
import de.juwimm.cms.gui.FrmProgressDialog;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.compound.ViewIdAndUnitIdValue;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class RootController implements ActionListener {
	private static Logger log = Logger.getLogger(RootController.class);
	public static final int STAGE_START = 0;
	public static final int STAGE_DEPLOY_ROOT = 11;
	public static final int STAGE_DEPLOY_ALL_WITH_ROOT = 21;
	public static final int STAGE_FINALLY = 99;

	private FrmWizard wiz = null;
	private PanRootStart aPanStart = null;
	private PanFinally panFinally = null;

	private int stage = 0;
	private int unitId;
	private int latestStageEver = 0;
	private Communication comm = ((Communication) getBean(Beans.COMMUNICATION));

	public RootController(int unitId) {
		this.unitId = unitId;
		aPanStart = new PanRootStart();
		wiz = new FrmWizard(this, UIConstants.WIZARD_ICON_INSTALL, Constants.rb.getString("wizard.root.title"),
				Constants.rb.getString("wizard.root.start.introMessage"), aPanStart);
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
					case RootController.STAGE_START:
						String acomm = aPanStart.getBtnGrpOptions().getSelection().getActionCommand();

						this.stage = RootController.STAGE_FINALLY;
						paintPanelForStage(stage);
						panFinally.setWay(new Integer(acomm).toString());
						break;
					case RootController.STAGE_FINALLY:
						if (panFinally.getWay().equals("11")) {
							log.info("STAGE_DEPLOY_ROOT");
							try {
								comm.createEdition("ROOT EDITION", comm.getViewDocument().getViewId(), true, true);
								JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb
										.getString("wizard.deploy.editionSucessfulTransmitted"), rb
										.getString("dialog.title"), JOptionPane.INFORMATION_MESSAGE);
							} catch (Exception exe) {
								log.error("Creating edition error", exe);
							}
						} else if (panFinally.getWay().equals("21")) {
							log.info("STAGE_DEPLOY_ALL_WITH_ROOT");
							Thread t = new Thread(new Runnable() {
								public void run() {
									createAllEditionsAndDeploy(comm.getViewDocumentId());
								}
							});
							t.setPriority(Thread.NORM_PRIORITY);
							t.start();
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
					latestStageEver = stage;
				}
				break;
			case FrmWizard.ACTION_WIZARD_PREVIOUS:
				switch (stage) {
					case RootController.STAGE_FINALLY:
						this.stage = RootController.STAGE_START;
						paintPanelForStage(stage);
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
			case RootController.STAGE_START:
				if (aPanStart == null) aPanStart = new PanRootStart();
				wpan = aPanStart;
				wpan.setUnitId(unitId);
				wiz.setPanel(aPanStart, rb.getString("wizard.root.start.introMessage"));
				wiz.setBackEnabled(false);
				break;
			case RootController.STAGE_FINALLY:
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

	public void createAllEditionsAndDeploy(int viewDocumentId) {
		int vdvcid = comm.getViewDocument().getViewId();
		FrmProgressDialog prog = new FrmProgressDialog(rb.getString("wizard.root.deployAll.progressdialog.task"),
				"Root-Edition", 1);

		boolean gotException = false;
		try {
			comm.createEdition("ROOT EDITION", vdvcid, true, true);
			recursiveDeploy(prog, vdvcid);
		} catch (Exception exe) {
			String errmsg = Messages.getString("exception.UnknownError", exe.getMessage());
			JOptionPane.showMessageDialog(prog, errmsg, rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
			log.error("Creating edition and deploy error", exe);
			gotException = true;
		} finally {
			if (gotException) {
				prog.enableClose(true);
				return;
			}
			prog.setProgress(rb.getString("wizard.root.deployAll.finished"), prog.getMaximum());
			JOptionPane.showMessageDialog(prog, rb.getString("wizard.deploy.editionSucessfulTransmitted"),
					rb.getString("dialog.title"), JOptionPane.INFORMATION_MESSAGE);
		}
		prog.enableClose(true);
	}

	private void recursiveDeploy(FrmProgressDialog prog, int vdvcid) throws ParentUnitNeverDeployed,
			PreviousUnitNeverDeployed, UnitWasNeverDeployed, EditionXMLIsNotValid, UserException {
		ViewIdAndUnitIdValue[] vcarr = comm.getAllViewComponentsWithUnits(vdvcid);
		prog.addToMaximum(vcarr.length);
		if (vcarr != null) {
			for (int i = 0; i < vcarr.length; i++) {
				if (log.isDebugEnabled())
					log.debug("VCL: " + vcarr[i].getViewComponentId() + " UNIT " + vcarr[i].getUnitId() + " UNITNAME "
							+ vcarr[i].getUnitName());

				String msg = Messages.getString("wizard.root.deployAll.progressdialog.createEdition",
						vcarr[i].getUnitName());
				prog.setProgress(msg);
				comm.createEdition("ROOT-CREATED-FULL-DEPLOY", vcarr[i].getViewComponentId(), true, false);
				recursiveDeploy(prog, vcarr[i].getViewComponentId());
			}
		}
	}

}