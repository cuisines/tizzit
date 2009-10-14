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
package de.juwimm.cms.gui.views;

import static de.juwimm.cms.client.beans.Application.getBean;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.ComponentInputMap;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.common.icon.ImageWrapperResizableIcon;

import de.juwimm.cms.Messages;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.exceptions.ViewComponentLinkNameAlreadyExisting;
import de.juwimm.cms.exceptions.ViewComponentLinkNameIsEmptyException;
import de.juwimm.cms.exceptions.ViewComponentNotFound;
import de.juwimm.cms.gui.controls.LoadableViewComponentPanel;
import de.juwimm.cms.gui.ribbon.CommandButton;
import de.juwimm.cms.gui.views.menuentry.PanMenuentryContent;
import de.juwimm.cms.gui.views.page.PanelContent;
import de.juwimm.cms.gui.views.page.PanelMetaData;
import de.juwimm.cms.gui.views.page.PanelSafeGuard;
import de.juwimm.cms.util.ActionHub;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.Parameters;
import de.juwimm.cms.util.PropertyConfigurationEvent;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.ViewComponentValue;
import de.juwimm.swing.NoResizeScrollPane;

/**
 * <p>Title: ConQuest </p>
 * <p>Description: Content Management System</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public final class PanContentView extends JPanel implements LoadableViewComponentPanel, ActionListener {
	private static final long serialVersionUID = 1043431871885717071L;
	private static Logger log = Logger.getLogger(PanContentView.class);
	private Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private ResourceBundle rb = Constants.rb;
	private ViewComponentValue viewComponent;
	private JTabbedPane panTab = new JTabbedPane();
	private PanMenuentryContent panMenuentry;
	private PanelContent panContent;
	private PanelMetaData panMetaData;
	private JPanel panXmlData = new JPanel();
	private PanelSafeGuard panSafeGuard;
	private JPanel panBottom = new JPanel();
	private JCommandButton btnSave;
	private JCommandButton btnCancel;
	private JCommandButton btnPreview;
	private JScrollPane spXmlText = new NoResizeScrollPane();
	private JEditorPane txtEditor = new JEditorPane(); // for "XML"-Tab
	private JRadioButton radioPreviewFrameset = new JRadioButton();
	private JRadioButton radioPreviewWithoutFrame = new JRadioButton();
	private ButtonGroup bGrp = new ButtonGroup();
	private static PanContentView instance = null;
	private boolean saveStatus = true;
	private TreePath treeNode;
	private boolean isFromSaveButton = false;

	public static PanContentView getInstance() {
		if (instance == null) {
			instance = new PanContentView();
		}
		return instance;
	}

	private PanContentView() {
		try {
			panMenuentry = new PanMenuentryContent();
			panContent = new PanelContent(this);
			panMetaData = new PanelMetaData();
			panSafeGuard = new PanelSafeGuard();
			this.btnCancel = new CommandButton(rb.getString("dialog.cancel"), ImageWrapperResizableIcon.getIcon(UIConstants.RIBBON_CLOSE.getImage(), new Dimension(32, 32)));
			this.btnCancel.setPreferredSize(new Dimension(55, 55));
			this.btnSave = new CommandButton(rb.getString("dialog.save"), ImageWrapperResizableIcon.getIcon(UIConstants.RIBBON_SAVE.getImage(), new Dimension(32, 32)));
			this.btnSave.setPreferredSize(new Dimension(55, 55));
			this.btnPreview = new CommandButton(rb.getString("dialog.preview"), ImageWrapperResizableIcon.getIcon(UIConstants.RIBBON_PREVIEW.getImage(), new Dimension(32, 32)));
			this.btnPreview.setPreferredSize(new Dimension(55, 55));
			jbInit();
			this.radioPreviewFrameset.setText(rb.getString("panel.toolContent.previewInFrameset"));
			this.radioPreviewWithoutFrame.setText(rb.getString("panel.toolContent.previewWithoutFrameset"));
			//btnSave.setMnemonic(KeyEvent.VK_S);
			//btnSave.setDisplayedMnemonicIndex(0);

			ComponentInputMap im = new ComponentInputMap(this);
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK), "saveContent");
			this.setInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW, im);
			this.getActionMap().put("saveContent", new AbstractAction() {
				private static final long serialVersionUID = -8417253327858948035L;

				public void actionPerformed(ActionEvent e) {
					save();
				}
			});

		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	public synchronized void reload() {
		panTab.removeAll();
		panTab.add(panMenuentry, rb.getString("panel.toolContent.tab.menuentry"));
		panTab.add(panContent, rb.getString("panel.toolContent.tab.content"));
		if (comm.isUserInRole(UserRights.PAGE_VIEW_METADATA)) {
			panTab.add(panMetaData, rb.getString("panel.toolContent.tab.metadata"));
		}
		if (comm.isUserInRole(UserRights.SITE_ROOT)) {
			panXmlData.setLayout(new BorderLayout());
			panXmlData.add(spXmlText, BorderLayout.CENTER);
			spXmlText.getViewport().add(txtEditor, null);
			panTab.add(panXmlData, rb.getString("panel.toolContent.tab.xml"));
			this.setXmlEditListener();
		}
		if ((comm.isUserInRole(UserRights.MANAGE_SAFEGUARD))
		// || (comm.isUserInRole(UserRights.SITE_ROOT)) at present no customer may see this tab !!!
				|| (comm.getUser().isMasterRoot())) {
			panTab.add(this.panSafeGuard, rb.getString("panel.toolContent.tab.safeguard"));
		}
	}

	private void jbInit() throws Exception {
		this.setLayout(new BorderLayout());
		ActionHub.addActionListener(this);

		btnSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (log.isDebugEnabled()) log.debug("btnSave actionPerformed");
				btnSave.setEnabled(false);
				Constants.EDIT_CONTENT = false;
				Constants.SHOW_CONTENT_FROM_DROPDOWN = true;
				isFromSaveButton = true;
				save();
				btnSave.setEnabled(true);
			}
		});

		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdCancelActionPerformed(e);
			}
		});
		panBottom.setLayout(new GridBagLayout());
		btnPreview.setEnabled(true);

		btnPreview.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPreviewActionPerformed(e);
			}
		});

		radioPreviewFrameset.setText("Preview in Frameset");
		radioPreviewWithoutFrame.setSelected(true);
		radioPreviewWithoutFrame.setText("Preview without Frame");
		panBottom.setMinimumSize(new Dimension(540, 40));

		this.add(panTab, BorderLayout.CENTER);

		this.add(panBottom, BorderLayout.SOUTH);
		panBottom.add(btnSave, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 2), 0, 0));
		panBottom.add(btnCancel, new GridBagConstraints(4, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		panBottom.add(btnPreview, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 0, 0));
		panBottom.add(radioPreviewFrameset, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		panBottom.add(radioPreviewWithoutFrame, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		panTab.addChangeListener(new MyChangeListener(this));
		bGrp.add(radioPreviewFrameset);
		bGrp.add(radioPreviewWithoutFrame);
	}

	private final void setXmlEditListener() {
		try {
			txtEditor.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent ke) {
					if (!Constants.EDIT_CONTENT) {
						Constants.EDIT_CONTENT = true;
					}
				}
			});
		} catch (Exception ex) {
		}
	}

	/**
	 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
	 * @version $Id$
	 */
	private class MyChangeListener implements ChangeListener {
		private int lastIndex = -1;

		public MyChangeListener(PanContentView panContentView) {
		}

		public void stateChanged(ChangeEvent e) {
			if (Constants.EDIT_CONTENT && lastIndex == 1) {
				int i = JOptionPane.showConfirmDialog(panTab, rb.getString("dialog.wantToSave"), rb.getString("dialog.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (i == JOptionPane.YES_OPTION) {
					panContent.save();
					if (!getSaveStatus()) {

					return; }
				}
				Constants.EDIT_CONTENT = false;
			}

			if (lastIndex == 1) {
				panContent.unload();
			} /*else if (lastIndex == 0) {
																																																																																																																											 panMenuentry.unload();
																																																																																																																											 } else if (lastIndex == 2) {
																																																																																																																											 panMetaData.unload();
																																																																																																																											 }*/

			String strTabName = "";
			try {
				strTabName = panTab.getTitleAt(panTab.getSelectedIndex());
			} catch (Exception ex) {
				// sometimes an ArrayIndexOutOfBoundsException occurs
			}
			if (panTab.getSelectedIndex() == 1) {
				//m_Pnl.setCursor(new Cursor(Cursor.WAIT_CURSOR));

				panContent.setEditorPane(txtEditor);
				panContent.load(viewComponent, false);

				//m_Pnl.setCursor(Cursor.getDefaultCursor());
			} else if ((panTab.getSelectedIndex() == 4) || strTabName.equalsIgnoreCase(rb.getString("panel.toolContent.tab.safeguard"))) {
				panSafeGuard.load(viewComponent);
			} else {
				ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Constants.ACTION_CONTENT_DESELECT));
				btnSave.setEnabled(true);
			}

			lastIndex = panTab.getSelectedIndex();
		}
	}

	public boolean getSaveStatus() {
		return saveStatus;
	}

	public synchronized void save() {
		UIConstants.setActionStatus(rb.getString("statusinfo.content.save"));
		btnSave.setEnabled(false);
		ActionHub.configureProperty(PanelContent.PROP_CHECKIN, PropertyConfigurationEvent.PROP_ENABLE, "false");
		ActionHub.configureProperty(PanelContent.PROP_CHECKOUT, PropertyConfigurationEvent.PROP_ENABLE, "false");

		Constants.EDIT_CONTENT = false;
		try {
			// Menu entry
			panMenuentry.save();
			viewComponent = panMenuentry.getViewComponent();
			saveStatus = true;
			if (!panMenuentry.getSaveStatus()) {
				saveStatus = false;
				if (!isFromSaveButton) {
					ActionHub.fireActionPerformed(new ActionEvent(treeNode, ActionEvent.ACTION_PERFORMED, Constants.ACTION_TREE_CLICK_NODE));
				}
				return;
			}
			// Metadata
			if (comm.isUserInRole(UserRights.PAGE_VIEW_METADATA)) {
				panMetaData.save();
			}

			if (panTab.getSelectedIndex() == 0) {
				int maxLinkNameLength = 255;
				try {
					maxLinkNameLength = Integer.parseInt(Parameters.getStringParameter(Parameters.PARAM_MAX_DISPLAY_LINK_NAME_LENGTH));
				} catch (Exception e) {
				}
				if (panMenuentry.getTxtText().getText().length() > maxLinkNameLength) {
					JOptionPane.showMessageDialog(UIConstants.getMainFrame(), Messages.getString("panel.panelView.content.linkNameTooLongWarning", new String[] {Integer.toString(maxLinkNameLength)}), rb.getString("dialog.title"), JOptionPane.WARNING_MESSAGE);
				}
			}
			if (panTab.getSelectedIndex() == 3) {
				panContent.setContent(txtEditor.getText());
			} else if (panTab.getSelectedIndex() == 1) {
				panContent.save();
			}
			if (viewComponent.getStatus() == Constants.DEPLOY_STATUS_EDITED) {
				// If you checkIn a NEW Contentversion, your "REMOVE-COMMAND" will also be removed
				if (viewComponent.getDeployCommand() == Constants.DEPLOY_COMMAND_DELETE || viewComponent.getDeployCommand() == Constants.DEPLOY_COMMAND_REMOVE) {
					viewComponent.setDeployCommand(Constants.DEPLOY_COMMAND_MODIFY);
				}
				log.debug("updating status");
				comm.updateStatus4ViewComponent(viewComponent);
			}
			try {
				viewComponent = comm.saveViewComponent(viewComponent);
				ActionHub.fireActionPerformed(new ActionEvent(viewComponent, ActionEvent.ACTION_PERFORMED, Constants.ACTION_TREE_ENTRY_NAME));
			} catch (ViewComponentLinkNameAlreadyExisting vc) {
				JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("exception.ViewComponentLinkNameAlreadyExisting"), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
			} catch (ViewComponentNotFound vn) {
				JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("exception.ViewComponentNotFound"), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
			} catch (ViewComponentLinkNameIsEmptyException ve) {
				JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("exception.ViewComponentLinkNameIsEmpty"), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
			}
			panSafeGuard.save();
		} catch (Exception exe) {
			log.error("Save Error", exe);
		}
		btnSave.setEnabled(true);
		ActionHub.configureProperty(PanelContent.PROP_CHECKIN, PropertyConfigurationEvent.PROP_ENABLE, "true");
		UIConstants.setActionStatus("");
	}

	public synchronized void load(ViewComponentValue value) {
		if (getSaveStatus()) {
			boolean showFrameset = Parameters.getBooleanParameter(Parameters.PARAM_SHOW_PREVIEW_FRAMESET);
			radioPreviewFrameset.setVisible(showFrameset);
			radioPreviewWithoutFrame.setVisible(showFrameset);

			panTab.setSelectedIndex(0);

			btnSave.setEnabled(false);
			viewComponent = value;
			if (panTab.getSelectedIndex() == 1) {
				panContent.setEditorPane(txtEditor);
				panContent.load(value, false);
			} else {
				btnSave.setEnabled(true);
			}
			panMenuentry.load(value);
			if (panMenuentry.shouldBeEdtiable()) {
				panMenuentry.setMenuentryEnabled(true);
				panTab.setEnabledAt(1, true); //Content
				if (comm.isUserInRole(UserRights.PAGE_VIEW_METADATA)) {
					panTab.setEnabledAt(2, true); //metadata
				}
			} else {
				panTab.setSelectedIndex(0);
				panMenuentry.setMenuentryEnabled(false);
				// the button for changing the template is invisible if user doesn't have the right to change
				panMenuentry.setTemplateButtonEnabled(true);
				this.btnSave.setEnabled(false);
				panTab.setEnabledAt(1, false); //Content
				if (comm.isUserInRole(UserRights.PAGE_VIEW_METADATA)) {
					panTab.setEnabledAt(2, false); //metadata
				}
			}
			if (comm.isUserInRole(UserRights.PAGE_VIEW_METADATA)) {
				panMetaData.load(value);
			}
			if ((comm.isUserInRole(UserRights.MANAGE_SAFEGUARD) || (comm.getUser().isMasterRoot())) && (panTab.getSelectedIndex() == 4)) {
				// || (comm.isUserInRole(UserRights.SITE_ROOT)) at present no customer may see this tab !!!
				panSafeGuard.load(value);
			} else {
				panSafeGuard.load(null); // reset panel (needed that the panel wont be saved for a wrong vc)
			}
		} else {
			btnSave.setEnabled(true);
		}
	}

	public void unload() {
		//comm.fireActionPerformed(new ActionEvent(null, ActionEvent.ACTION_PERFORMED, Constants.ACTION_CONTENT_DESELECT));
		//comm.removeActionListener(this);
		panMenuentry.unload();
		panContent.unload();
		panMetaData.unload();
		panSafeGuard.unload();
	}

	public void unloadAll() {
		this.unload();
		this.panContent.unloadAll();
	}

	private void cmdCancelActionPerformed(ActionEvent e) {
		Constants.EDIT_CONTENT = false;
		if (panTab.getSelectedIndex() == 1) {
			panContent.checkIn();
		}
		unload();
		ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Constants.ACTION_TREE_DESELECT));
	}

	private void btnPreviewActionPerformed(ActionEvent e) {
		boolean showInFrame = false;

		if (this.radioPreviewFrameset.isSelected()) {
			showInFrame = true;

		}
		int id = viewComponent.getViewComponentId().intValue();
		comm.showBrowserWindow(id, showInFrame);
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals(Constants.ENABLE_CHECKIN) || ae.getActionCommand().equals(Constants.ACTION_CONTENT_DESELECT)) {
			btnSave.setEnabled(true);
		} else if (ae.getActionCommand().equals(Constants.ENABLE_CHECKOUT)) {
			btnSave.setEnabled(false);
		} else if (ae.getActionCommand().equals(Constants.ACTION_SAVE)) {
			if (log.isDebugEnabled()) log.debug("actionPerformed::ACTION_SAVE");
			isFromSaveButton = false;
			save();
		} else if (ae.getActionCommand().equals(Constants.ACTION_TREE_SET_NODE)) {
			treeNode = (TreePath) ae.getSource();
		} else if (ae.getActionCommand().equals(Constants.ACTION_TREE_RESET_CONSTANTS_CONTENT_VIEW)) {
			saveStatus = true;
		}

	}

}
