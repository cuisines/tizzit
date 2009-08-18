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

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.*;

import org.apache.log4j.Logger;

import com.Ostermiller.util.Browser;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.deploy.actions.ExportFullThread;
import de.juwimm.cms.deploy.actions.ImportFullThread;
import de.juwimm.cms.gui.event.FinishedActionListener;
import de.juwimm.cms.gui.event.ViewComponentEvent;
import de.juwimm.cms.gui.tree.PageNode;
import de.juwimm.cms.gui.tree.TreeNode;
import de.juwimm.cms.util.ActionHub;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.util.UserConfig;

/**
 * <p>Title: ConQuest</p>
 * <p>Description: Enterprise Content Management<</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanMenubar extends JMenuBar implements ActionListener, FinishedActionListener {
	private static Logger log = Logger.getLogger(PanMenubar.class);
	private ButtonGroup buttonGroup;
	private Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	/** File Menu */
	private JMenu mnuFile = new JMenu(rb.getString("menubar.file"));
	private JMenu mnuFileNew = new JMenu(rb.getString("menubar.file.new"));
	private JMenuItem mnuFileNewContentAfter = new JMenuItem(rb.getString("actions.ACTION_TREE_NODE_AFTER"));
	private JMenuItem mnuFileNewContentUnderneath = new JMenuItem(rb.getString("actions.ACTION_TREE_NODE_APPEND"));
	private JMenuItem mnuFileNewSymlinkAfter = new JMenuItem(rb.getString("actions.ACTION_TREE_SYMLINK_AFTER"));
	private JMenuItem mnuFileNewSymlinkUnderneath = new JMenuItem(rb.getString("actions.ACTION_TREE_SYMLINK_ADD"));
	private JMenuItem mnuFileNewInternalLinkAfter = new JMenuItem(rb.getString("actions.ACTION_TREE_JUMP_AFTER"));
	private JMenuItem mnuFileNewInternalLinkUnderneath = new JMenuItem(rb.getString("actions.ACTION_TREE_JUMP_ADD"));
	private JMenuItem mnuFileNewExternalLinkAfter = new JMenuItem(rb.getString("actions.ACTION_TREE_LINK_AFTER"));
	private JMenuItem mnuFileNewExternalLinkUnderneath = new JMenuItem(rb.getString("actions.ACTION_TREE_LINK_ADD"));
	private JMenu mnuFileMove = new JMenu(rb.getString("menubar.file.move"));
	private JMenuItem mnuFileMoveLeft = new JMenuItem(rb.getString("actions.MOVE_LEFT"), UIConstants.MNU_MOVE_LEFT);
	private JMenuItem mnuFileMoveRight = new JMenuItem(rb.getString("actions.MOVE_RIGHT"), UIConstants.MNU_MOVE_RIGHT);
	private JMenuItem mnuFileMoveUp = new JMenuItem(rb.getString("actions.MOVE_UP"), UIConstants.MNU_MOVE_UP);
	private JMenuItem mnuFileMoveDown = new JMenuItem(rb.getString("actions.MOVE_DOWN"), UIConstants.MNU_MOVE_DOWN);
	private JMenuItem mnuFileCheckout = new JMenuItem(rb.getString("actions.ACTION_CHECKOUT"), UIConstants.ACTION_CHECKOUT);
	private JMenuItem mnuFileCheckin = new JMenuItem(rb.getString("actions.ACTION_CHECKIN"), UIConstants.ACTION_CHECKIN);
	private JMenuItem mnuFileSave = new JMenuItem(rb.getString("menubar.file.save"), UIConstants.MNU_FILE_SAVE);
	private JMenuItem mnuFileDelete = new JMenuItem(rb.getString("menubar.file.delete"), UIConstants.MODULE_ITERATION_CONTENT_DELETE);
	private JMenuItem mnuFilePreview = new JMenuItem(rb.getString("menubar.file.preview"), UIConstants.MNU_EMPTY);
	private JMenuItem mnuFileLogoff = new JMenuItem(rb.getString("menubar.file.logoff"), UIConstants.MNU_EMPTY);
	private JMenuItem mnuFileQuit = new JMenuItem(rb.getString("menubar.file.quit"), UIConstants.MNU_EMPTY);
	/** Edit Menu */
	private JMenu mnuEdit = new JMenu(rb.getString("menubar.edit"));
	private JMenuItem mnuEditUndo = new JMenuItem(rb.getString("menubar.edit.undo"), UIConstants.MNU_FILE_EDIT_UNDO);
	private JMenuItem mnuEditRedo = new JMenuItem(rb.getString("menubar.edit.redo"), UIConstants.MNU_FILE_EDIT_REDO);
	private JMenuItem mnuEditCut = new JMenuItem(rb.getString("menubar.edit.cut"), UIConstants.MNU_FILE_EDIT_CUT);
	private JMenuItem mnuEditCopy = new JMenuItem(rb.getString("menubar.edit.copy"), UIConstants.MNU_FILE_EDIT_COPY);
	private JMenuItem mnuEditPaste = new JMenuItem(rb.getString("menubar.edit.paste"), UIConstants.MNU_FILE_EDIT_PASTE);
	private JMenuItem mnuEditDelete = new JMenuItem(rb.getString("menubar.edit.delete"), UIConstants.MNU_EMPTY);
	/** View Menu */
	private JMenu mnuView = new JMenu(rb.getString("menubar.view"));
	private JRadioButtonMenuItem mnuViewEditor = new JRadioButtonMenuItem(rb.getString("menubar.view.editor"), UIConstants.ICON_USER);
	private JRadioButtonMenuItem mnuViewTask = new JRadioButtonMenuItem(rb.getString("menubar.view.task"), UIConstants.ICON_USER);
	private JRadioButtonMenuItem mnuViewAdmin = new JRadioButtonMenuItem(rb.getString("menubar.view.admin"), UIConstants.ICON_USER);

	/** Publish Menu */
	private JMenu mnuPublish = new JMenu(rb.getString("menubar.publish"));
	private JMenuItem mnuPublishLetRelease = new JMenuItem(rb.getString("menubar.publish.letRelease"));
	private JMenuItem mnuPublishRevise = new JMenuItem(rb.getString("menubar.publish.revise")); // zurückziehen
	private JMenuItem mnuPublishRelease = new JMenuItem(rb.getString("menubar.publish.release")); // freigeben
	private JMenuItem mnuPublishDeploy = new JMenuItem();
	/** Extras Menu */
	private JMenu mnuExtras = new JMenu(rb.getString("menubar.extras"));
	private JMenuItem mnuExtrasChangePassword = new JMenuItem(rb.getString("menubar.extras.changePassword"));
	private JMenu mnuExtrasOptions = new JMenu(rb.getString("menubar.extras.options"));
	private JMenu mnuExtrasOptionsChooseLanguage = new JMenu(rb.getString("menubar.extras.options.chooseLanguage"));
	/*private JMenu mnuExtrasOptionsLookAndFeel = new JMenu(rb.getString("menubar.extras.options.lookAndFeel"));
	 private JMenuItem mnuExtrasOptionsLookAndFeelLinux = new JMenuItem(rb.getString(
	 "menubar.extras.options.lookAndFeel.fromLinuxWindowmanager"));
	 private JMenuItem mnuExtrasOptionsLookAndFeelWindows = new JMenuItem(rb.getString(
	 "menubar.extras.options.lookAndFeel.fromWindows"));*/
	private JMenuItem mnuExtrasClearCache = new JMenuItem(rb.getString("menubar.extras.clearcache"));
	private JMenuItem mnuExtrasReloadDcf = new JMenuItem(rb.getString("menubar.extras.reloadDcf"));
	/** Question Mark Menu */
	private JMenu mnuQuestionMark = new JMenu(rb.getString("menubar.questionMark"));
	private JMenuItem mnuQuestionMarkHelp = new JMenuItem(rb.getString("menubar.questionMark.help"));
	private JMenuItem mnuQuestionMarkContextHelp = new JMenuItem(rb.getString("menubar.questionMark.contextHelp"));
	private JMenuItem mnuQuestionMarkAbout = new JMenuItem(rb.getString("menubar.questionMark.about"));

	public static final String CMS_CLEAR_CACHE = "cmsclearcachenow!";
	public static final String CMS_RELOAD_DCF = "cmsreloaddcf";
	public static final String CMS_LANG_DE = "cmslanguagegerman";
	public static final String CMS_LANG_EN = "cmslanguageenglish";
	public static final String CMS_EXPORT_ALL = "cmsexportallthefuck";
	public static final String CMS_IMPORT_ALL = "cmsimportallthisshittystuffassoonaspossibletogetitrunning";

	public PanMenubar() {
		try {
			setDoubleBuffered(true);
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
		mnuQuestionMarkContextHelp.setEnabled(false);
	}

	public void addActionListener(ActionListener actionListener) {
		mnuViewEditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mnuView.setEnabled(false);
				ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Constants.ACTION_VIEW_EDITOR));
			}
		});

		mnuViewTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mnuView.setEnabled(false);
				ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Constants.ACTION_SHOW_TASK));
			}
		});

		mnuViewAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mnuView.setEnabled(false);
				ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, mnuViewAdmin.getActionCommand()));
			}
		});
		mnuQuestionMarkAbout.addActionListener(actionListener);
		mnuFileLogoff.addActionListener(actionListener);
		mnuFileQuit.addActionListener(actionListener);
		mnuExtrasChangePassword.addActionListener(actionListener);
		mnuPublishLetRelease.addActionListener(actionListener);
		mnuPublishRevise.addActionListener(actionListener);
		mnuPublishRelease.addActionListener(actionListener);

		mnuPublishDeploy.addActionListener(actionListener);
		mnuFileSave.addActionListener(actionListener);
		mnuFileDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionHub.fireViewComponentPerformed(new ViewComponentEvent(ViewComponentEvent.DELETE));
			}
		});
		mnuFileNewContentAfter.addActionListener(actionListener);
		mnuFileNewContentUnderneath.addActionListener(actionListener);
		mnuFileNewExternalLinkAfter.addActionListener(actionListener);
		mnuFileNewExternalLinkUnderneath.addActionListener(actionListener);
		mnuFileNewInternalLinkAfter.addActionListener(actionListener);
		mnuFileNewInternalLinkUnderneath.addActionListener(actionListener);
		mnuFileNewSymlinkAfter.addActionListener(actionListener);
		mnuFileNewSymlinkUnderneath.addActionListener(actionListener);
		mnuFilePreview.addActionListener(actionListener);
		mnuFileCheckin.addActionListener(actionListener);
		mnuFileCheckin.setActionCommand(Constants.ACTION_CHECKIN);
		mnuFileCheckout.addActionListener(actionListener);
		mnuFileCheckout.setActionCommand(Constants.ACTION_CHECKOUT);
	}

	void jbInit() throws Exception {
		buttonGroup = new ButtonGroup();
		buttonGroup.add(mnuViewEditor);
		buttonGroup.add(mnuViewTask);
		buttonGroup.add(mnuViewAdmin);

		this.add(mnuFile);
		mnuFile.add(mnuFileNew);
		mnuFileNew.setIcon(UIConstants.MNU_FILE_NEW);
		mnuFileNew.add(mnuFileNewContentAfter);
		mnuFileNewContentAfter.setActionCommand(Constants.ACTION_TREE_NODE_AFTER);
		mnuFileNew.add(mnuFileNewContentUnderneath);
		mnuFileNewContentUnderneath.setActionCommand(Constants.ACTION_TREE_NODE_APPEND);
		if (comm.isUserInRole(UserRights.SITE_ROOT)) {
			mnuFileNew.add(mnuFileNewSymlinkAfter);
			mnuFileNewSymlinkAfter.setActionCommand(Constants.ACTION_TREE_SYMLINK_AFTER);
			mnuFileNew.add(mnuFileNewSymlinkUnderneath);
			mnuFileNewSymlinkUnderneath.setActionCommand(Constants.ACTION_TREE_SYMLINK_ADD);
		}
		mnuFileNew.add(mnuFileNewInternalLinkAfter);
		mnuFileNewInternalLinkAfter.setActionCommand(Constants.ACTION_TREE_JUMP_AFTER);
		mnuFileNew.add(mnuFileNewInternalLinkUnderneath);
		mnuFileNewInternalLinkUnderneath.setActionCommand(Constants.ACTION_TREE_JUMP_ADD);
		mnuFileNew.add(mnuFileNewExternalLinkAfter);
		mnuFileNewExternalLinkAfter.setActionCommand(Constants.ACTION_TREE_LINK_AFTER);
		mnuFileNew.add(mnuFileNewExternalLinkUnderneath);
		mnuFileNewExternalLinkUnderneath.setActionCommand(Constants.ACTION_TREE_LINK_ADD);
		mnuFile.addSeparator();
		mnuFile.add(mnuFileMove);
		mnuFileMove.setIcon(UIConstants.MNU_EMPTY);
		mnuFileMove.add(mnuFileMoveLeft);
		mnuFileMoveLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionHub.fireViewComponentPerformed(new ViewComponentEvent(ViewComponentEvent.MOVE_LEFT));
			}
		});
		mnuFileMove.add(mnuFileMoveRight);
		mnuFileMoveRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionHub.fireViewComponentPerformed(new ViewComponentEvent(ViewComponentEvent.MOVE_RIGHT));
			}
		});
		mnuFileMove.add(mnuFileMoveUp);
		mnuFileMoveUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionHub.fireViewComponentPerformed(new ViewComponentEvent(ViewComponentEvent.MOVE_UP));
			}
		});
		mnuFileMove.add(mnuFileMoveDown);
		mnuFileMoveDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionHub.fireViewComponentPerformed(new ViewComponentEvent(ViewComponentEvent.MOVE_DOWN));
			}
		});
		mnuFile.addSeparator();
		mnuFile.add(mnuFileCheckout);
		mnuFile.add(mnuFileCheckin);
		mnuFile.add(mnuFileSave);
		mnuFileSave.setActionCommand(Constants.ACTION_SAVE);

		mnuFile.addSeparator();
		mnuFile.add(mnuFileDelete);
		mnuFile.addSeparator();
		mnuFile.add(mnuFilePreview);
		mnuFile.addSeparator();
		mnuFile.add(mnuFileLogoff);
		mnuFileLogoff.setActionCommand(Constants.ACTION_LOGOFF);
		mnuFile.add(mnuFileQuit);
		mnuFileQuit.setActionCommand(Constants.ACTION_EXIT);

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

		this.add(mnuView);
		mnuView.add(mnuViewEditor);
		mnuView.add(mnuViewTask);
		mnuView.add(mnuViewAdmin);

		this.add(mnuPublish);
		//mnuPublish.add(mnuPublishLetRelease);
		//mnuPublishLetRelease.setActionCommand(Constants.ACTION_CONTENT_4APPROVAL);
		mnuPublish.add(mnuPublishRevise);
		mnuPublishRevise.setActionCommand(Constants.ACTION_CONTENT_CANCEL_APPROVAL);
		mnuPublish.add(mnuPublishRelease);
		mnuPublishRelease.setActionCommand(Constants.ACTION_CONTENT_APPROVE);
		mnuPublish.addSeparator();
		mnuPublish.add(mnuPublishDeploy);
		if (comm.isUserInRole(UserRights.SITE_ROOT)) {
			mnuPublishDeploy.setText(rb.getString("menubar.publish.wizardRoot"));
		} else if (comm.isUserInRole(UserRights.DEPLOY)) {
			mnuPublishDeploy.setText(rb.getString("menubar.publish.wizardEditor"));
		} else {
			mnuPublishDeploy.setText(rb.getString("menubar.publish.wizardAuthor"));
		}
		mnuPublishDeploy.setActionCommand(Constants.ACTION_DEPLOY);

		this.add(mnuExtras);
		mnuExtras.add(mnuExtrasChangePassword);
		mnuExtrasChangePassword.setActionCommand(Constants.ACTION_CHANGE_PASSWORD);
		mnuExtras.addSeparator();
		mnuExtras.add(mnuExtrasOptions);
		mnuExtrasOptions.add(mnuExtrasOptionsChooseLanguage);

		/*java.util.Iterator it = LookAndFeel.ALL_SKINS.keySet().iterator();
		 while(it.hasNext()) {
		 JMenuItem mit = new JMenuItem((String) it.next());
		 mit.addActionListener(this);
		 mit.setActionCommand(PanMenubar.CMS_SKIN_MENU);
		 mnuExtrasOptionsLookAndFeel.add(mit);
		 }
		 if(Constants.isClientOS(Constants.OS_LINUX)) {
		 mnuExtrasOptionsLookAndFeelLinux.addActionListener(this);
		 mnuExtrasOptionsLookAndFeelLinux.setActionCommand(PanMenubar.CMS_SKIN_MENU_NATIVE);
		 mnuExtrasOptionsLookAndFeel.add(mnuExtrasOptionsLookAndFeelLinux);
		 }
		 if(Constants.isClientOS(Constants.OS_WINDOWS)) {
		 mnuExtrasOptionsLookAndFeelWindows.addActionListener(this);
		 mnuExtrasOptionsLookAndFeelWindows.setActionCommand(PanMenubar.CMS_SKIN_MENU_NATIVE);
		 mnuExtrasOptionsLookAndFeel.add(mnuExtrasOptionsLookAndFeelWindows);
		 }
		 if(mnuExtrasOptionsLookAndFeel.getItemCount()>0) {
		 // Apple Macintosh has no Skins, therefor this menu will not be shown
		 mnuExtrasOptions.add(mnuExtrasOptionsLookAndFeel);
		 }*/

		if (comm.isUserInRole(UserRights.SITE_ROOT)) {
			JMenuItem mnuExtrasExportAll = new JMenuItem("Export complete Site");
			mnuExtrasExportAll.setActionCommand(PanMenubar.CMS_EXPORT_ALL);
			mnuExtrasExportAll.addActionListener(this);
			mnuExtras.add(mnuExtrasExportAll);
			JMenuItem mnuExtrasImportAll = new JMenuItem("Import complete Site");
			mnuExtrasImportAll.setActionCommand(PanMenubar.CMS_IMPORT_ALL);
			mnuExtrasImportAll.addActionListener(this);
			mnuExtras.add(mnuExtrasImportAll);
		}

		mnuExtras.addSeparator();
		mnuExtras.add(mnuExtrasClearCache);
		mnuExtrasClearCache.addActionListener(this);
		mnuExtrasClearCache.setActionCommand(CMS_CLEAR_CACHE);
		mnuExtras.add(mnuExtrasReloadDcf);
		mnuExtrasReloadDcf.addActionListener(this);
		mnuExtrasReloadDcf.setActionCommand(CMS_RELOAD_DCF);

		this.add(mnuQuestionMark);
		mnuQuestionMark.add(mnuQuestionMarkHelp);
		mnuQuestionMark.add(mnuQuestionMarkContextHelp);
		mnuQuestionMark.addSeparator();
		mnuQuestionMark.add(mnuQuestionMarkAbout);

		mnuViewAdmin.setEnabled(false);
		if (comm.isUserInRole(UserRights.SITE_ROOT)) {
			mnuViewAdmin.setActionCommand(Constants.ACTION_VIEW_ROOT);
		} else {
			mnuViewAdmin.setActionCommand(Constants.ACTION_VIEW_ADMIN);
		}
		mnuViewAdmin.addActionListener(this);
		if (comm.isUserInRole(UserRights.UNIT_ADMIN)) {
			mnuViewAdmin.setEnabled(true);
		}
		/*
		 //MenuItem for undo in the Edit menu.
		 mnuEditUndo.addActionListener(new ActionListener() {
		 public void actionPerformed(ActionEvent evt) {
		 if(Textfield.getInstance() != null) {
		 Textfield.getInstance().doActionForUndo();
		 }
		 }
		 });
		 //MenuItem for redo in the Edit menu
		 mnuEditRedo.addActionListener(new ActionListener() {
		 public void actionPerformed(ActionEvent evt) {
		 if(Textfield.getInstance() != null) {
		 Textfield.getInstance().doActionForRedo();
		 }
		 }
		 });

		 //MenuItem for delete in the Edit menu
		 mnuEditDelete.addActionListener(new ActionListener() {
		 public void actionPerformed(ActionEvent evt) {
		 if(Textfield.getInstance() != null) {
		 Textfield.getInstance().doActionForDelete();
		 }
		 }
		 });
		 */
		mnuQuestionMarkHelp.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuItemHelpActionPerformed(e);
			}
		});

		mnuQuestionMarkAbout.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mnuItemVersionActionPerformed(e);
			}
		});

		JMenuItem mi = new JMenuItem("Deutsch");
		mi.addActionListener(this);
		mi.setActionCommand(CMS_LANG_DE);
		mnuExtrasOptionsChooseLanguage.add(mi);
		mi = new JMenuItem("English");
		mi.addActionListener(this);
		mi.setActionCommand(CMS_LANG_EN);
		mnuExtrasOptionsChooseLanguage.add(mi);

		mnuFileDelete.setEnabled(false);
		mnuFileNewContentAfter.setEnabled(false);
		mnuFileNewContentUnderneath.setEnabled(false);
		mnuFileNewExternalLinkUnderneath.setEnabled(false);
		mnuFileNewExternalLinkAfter.setEnabled(false);
		mnuFileNewInternalLinkAfter.setEnabled(false);
		mnuFileNewInternalLinkUnderneath.setEnabled(false);
		mnuFileCheckin.setEnabled(false);
		mnuFileCheckout.setEnabled(false);
		mnuFileMoveDown.setEnabled(false);
		mnuFileMoveUp.setEnabled(false);
		mnuFileMoveLeft.setEnabled(false);
		mnuFileMoveRight.setEnabled(false);
		mnuFileSave.setEnabled(false);
		mnuFilePreview.setEnabled(false);
	}

	public void setView(boolean showContent) {
		mnuPublish.setEnabled(showContent);
		mnuViewAdmin.setSelected(!showContent);
		mnuViewEditor.setSelected(showContent);
		this.validate();
		this.repaint();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(Constants.ACTION_CREATE_UNIT)) {
			mnuViewAdmin.doClick();
		} else if (e.getActionCommand().equals(Constants.ACTION_SHOW_TASK)) {
			mnuPublishLetRelease.setEnabled(false);
			mnuPublishRelease.setEnabled(false);
			mnuPublishRevise.setEnabled(false);
			mnuViewTask.setSelected(true);
		} else if (e.getActionCommand().equals(Constants.ACTION_TREE_SELECT) || e.getActionCommand().equals(Constants.ACTION_DEPLOY_STATUS_CHANGED)) {
			setMenuOnEntryProperties((TreeNode) e.getSource());
		} else if (e.getActionCommand().equals(CMS_CLEAR_CACHE)) {
			comm.clearSvgCache();
			try {
				if (comm.getDbHelper().clearMyCache()) {
					JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("panel.cmsMenubar.cacheCleared"), 
							rb.getString("dialog.title"), JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("exception.cantClearCache"), 
							rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception exe) {
				if (log.isDebugEnabled()) {
					log.debug(exe.getMessage());
				}
			}
		} else if (e.getActionCommand().equals(CMS_RELOAD_DCF)) {
			PanLogin.loadTemplates(false);
			ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Constants.ACTION_VIEW_EDITOR));
		}  else if (e.getActionCommand().equals(CMS_EXPORT_ALL)) {
			new ExportFullThread().start();
			//EXPORT ALL
		} else if (e.getActionCommand().equals(CMS_IMPORT_ALL)) {
			new ImportFullThread().start();
			//IMPORT ALL
		} else if (e.getActionCommand().equals(Constants.ENABLE_CHECKIN)) {
			mnuFileCheckin.setEnabled(true);
			mnuFileCheckout.setEnabled(false);
			mnuPublishLetRelease.setEnabled(false);
			mnuPublishRelease.setEnabled(false);
			mnuPublishRevise.setEnabled(false);
		} else if (e.getActionCommand().equals(Constants.ENABLE_CHECKOUT)) {
			mnuFileCheckin.setEnabled(false);
			mnuFileCheckout.setEnabled(true);
			setMenuOnEntryProperties(PanTree.getSelectedEntry());
		} else if (e.getActionCommand().equals(Constants.ACTION_CONTENT_SELECT) || e.getActionCommand().equals(Constants.ACTION_CONTENT_EDITED)) {
			mnuFileCheckin.setEnabled(false);
			mnuFileCheckout.setEnabled(false);
		} else if (e.getActionCommand().equals(Constants.ACTION_CONTENT_DESELECT)) {
			mnuFileCheckin.setEnabled(false);
			mnuFileCheckout.setEnabled(false);
		} else if (e.getActionCommand().equals(Constants.ACTION_TREE_DESELECT)) {
			mnuFileDelete.setEnabled(false);
			mnuFileNewContentAfter.setEnabled(false);
			mnuFileNewContentUnderneath.setEnabled(false);
			mnuFileNewExternalLinkUnderneath.setEnabled(false);
			mnuFileNewExternalLinkAfter.setEnabled(false);
			mnuFileNewInternalLinkAfter.setEnabled(false);
			mnuFileNewInternalLinkUnderneath.setEnabled(false);
			mnuFileNewSymlinkAfter.setEnabled(false);
			mnuFileNewSymlinkUnderneath.setEnabled(false);
			mnuFileCheckin.setEnabled(false);
			mnuFileCheckout.setEnabled(false);
		}
	}

	public void actionFinished() {
		mnuView.setEnabled(true);
	}

	private void setMenuOnEntryProperties(TreeNode currentEntry) {
		if (currentEntry == null) return;
		// NEW --------------------------------------------------------
		mnuFileMoveLeft.setEnabled(currentEntry.isMoveableToLeft());
		mnuFileMoveUp.setEnabled(currentEntry.isMoveableToUp());
		mnuFileMoveDown.setEnabled(currentEntry.isMoveableToDown());
		mnuFileMoveRight.setEnabled(currentEntry.isMoveableToRight());
		mnuFileDelete.setEnabled(currentEntry.isDeleteable());

		boolean append = currentEntry.isAppendingAllowed();
		mnuFileNewContentUnderneath.setEnabled(append);
		mnuFileNewSymlinkUnderneath.setEnabled(append);
		mnuFileNewInternalLinkUnderneath.setEnabled(append);
		mnuFileNewExternalLinkUnderneath.setEnabled(append);

		mnuFileNewContentAfter.setEnabled(false);
		mnuFileNewInternalLinkAfter.setEnabled(false);
		mnuFileNewSymlinkAfter.setEnabled(false);
		mnuFileNewExternalLinkAfter.setEnabled(false);
		mnuPublishLetRelease.setEnabled(false);
		mnuPublishRelease.setEnabled(false);
		mnuPublishRevise.setEnabled(false);

		if (currentEntry instanceof PageNode) {
			if (!currentEntry.isRoot()) {
				mnuFileNewContentAfter.setEnabled(true);
				mnuFileNewInternalLinkAfter.setEnabled(true);
				mnuFileNewSymlinkAfter.setEnabled(true);
				mnuFileNewExternalLinkAfter.setEnabled(true);
			}
			switch (((PageNode) currentEntry).getStatus()) {
				case Constants.DEPLOY_STATUS_EDITED:
					if (!comm.isUserInRole(UserRights.APPROVE)) {
						mnuPublishRelease.setEnabled(false);
					} else {
						mnuPublishRelease.setEnabled(true);
					}
					mnuPublishLetRelease.setEnabled(true);
					mnuPublishRevise.setEnabled(false);
					break;
				case Constants.DEPLOY_STATUS_FOR_APPROVAL:
					mnuPublishLetRelease.setEnabled(false);
					mnuPublishRevise.setEnabled(true);

					if (!comm.isUserInRole(UserRights.APPROVE)) {
						mnuPublishRelease.setEnabled(false);
					} else {
						mnuPublishRelease.setEnabled(true);
					}
					break;
				default:
					mnuPublishLetRelease.setEnabled(false);
					mnuPublishRelease.setEnabled(false);
					mnuPublishRevise.setEnabled(false);
					break;
			}
		}
	}

	void menuItemHelpActionPerformed(ActionEvent e) {
		try {
			this.getParent().setCursor(new Cursor(Cursor.WAIT_CURSOR));
			Browser.displayURL(Constants.CMS_PATH_HELP);
			this.getParent().setCursor(Cursor.getDefaultCursor());
		} catch (IOException exe) {
			log.error("HELPError", exe);
		}
	}

	void mnuItemVersionActionPerformed(ActionEvent e) {
		FrmVersion dialog = new FrmVersion(Constants.CMS_VERSION);
		int height = 300;
		int width = 450;
		int midHeight = UIConstants.getMainFrame().getY() + (UIConstants.getMainFrame().getHeight() / 2);
		int midWidth = UIConstants.getMainFrame().getX() + (UIConstants.getMainFrame().getWidth() / 2);
		dialog.setSize(width, height);
		dialog.setLocation(midWidth - width / 2, midHeight - height / 2);
		dialog.setVisible(true);
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