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

import static de.juwimm.cms.common.Constants.rb;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.SwingWorker.StateValue;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import org.apache.log4j.Logger;

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.exceptions.ViewComponentLinkNameAlreadyExisting;
import de.juwimm.cms.exceptions.ViewComponentLinkNameIsEmptyException;
import de.juwimm.cms.exceptions.ViewComponentNotFound;
import de.juwimm.cms.util.ActionHub;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.util.UrlValidator;
import de.juwimm.cms.vo.ViewComponentValue;

/**
 * <p>Title: Tizzit </p>
 * <p>Description: Content Management System</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanMenuentryExternallink extends PanMenuentry {
	private static Logger log = Logger.getLogger(PanMenuentryExternallink.class);
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final JPanel panNewWindow = new JPanel();
	private final JPanel panLinkSuggestion = new JPanel();
	private final JRadioButton rbBlank = new JRadioButton();
	private final JRadioButton rbHeader = new JRadioButton();
	private final JLabel lblLinkAddress = new JLabel();
	private final JLabel lblResultAddress = new JLabel();
	private final JTextField txtLinkUrl = new JTextField();
	private final Communication comm;
	private final JPanel panLinkProperties = new JPanel();
	private Component spacer;
	private final JCheckBox cbNewWindow = new JCheckBox();
	private final JButton buttonUseSuggested=new JButton();
	private String suggestedLink=null;
	
	@SuppressWarnings("unchecked")
	public PanMenuentryExternallink(Communication comm) {
		super();
		this.comm = comm;
		try {
			jbInit();
			txtLinkUrl.addKeyListener(ActionHub.getContentEditKeyListener());
			txtLinkUrl.addCaretListener(new CaretListener() {
				SwingWorker worker;

				private SwingWorker createNewWorker() {
					SwingWorker worker = new SwingWorker() {
						@Override
						protected Object doInBackground() throws Exception {
							return UrlValidator.validate(txtLinkUrl.getText());
						}

						@Override
						protected void done() {
							try {
								if (!isCancelled()) {
									suggestedLink=(String) get();
									lblResultAddress.setText(rb.getString("de.juwimm.cms.util.urlValidator.foundAdress")+suggestedLink);
									if(!txtLinkUrl.getText().equals(suggestedLink) && !suggestedLink.contains(rb.getString("exception.invalidURL"))){
										buttonUseSuggested.setVisible(true);
									} else {
										buttonUseSuggested.setVisible(false);
									}
								}
							} catch (InterruptedException e) {
								log.error(e);
							} catch (ExecutionException e) {
								log.error(e);
							} catch (CancellationException e) {
								log.error(e);
							}
						}
					};
					return worker;
				}

				public void caretUpdate(CaretEvent e) {
					lblResultAddress.setText(rb.getString("de.juwimm.cms.gui.views.menuentry.PanMenuentryExternallink.Searching"));
					buttonUseSuggested.setVisible(false);
					if (worker != null && worker.getState().equals(StateValue.STARTED)) {
						worker.cancel(true);
					}
					worker = createNewWorker();
					worker.execute();
				}
			});
			buttonUseSuggested.setText(rb.getString("de.juwimm.cms.gui.views.menuentry.PanMenuentryExternallink.UseSuggestion"));
			buttonUseSuggested.setVisible(false);
			buttonUseSuggested.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					txtLinkUrl.setText(suggestedLink);
					buttonUseSuggested.setVisible(false);
				}
			});
			rbBlank.addMouseListener(ActionHub.getContentEditMouseListener());
			rbBlank.addKeyListener(ActionHub.getContentEditKeyListener());
			rbHeader.addMouseListener(ActionHub.getContentEditMouseListener());
			rbHeader.addKeyListener(ActionHub.getContentEditKeyListener());

			lblLinkAddress.setText(rb.getString("panel.panelView.link.linkUrl"));
			rbBlank.setText(rb.getString("panel.panelView.link.withoutHeader"));
			rbHeader.setText(rb.getString("panel.panelView.link.withHeader"));
			cbNewWindow.addMouseListener(ActionHub.getContentEditMouseListener());
			cbNewWindow.setText(rb.getString("panel.panelView.jump.newWindow"));
		} catch (Exception ex) {
			log.error(ex);
		}
	}


	void jbInit() throws Exception {
		spacer = Box.createVerticalStrut(8);
		lblLinkAddress.setMaximumSize(new Dimension(90, 15));
		lblLinkAddress.setMinimumSize(new Dimension(90, 15));
		lblLinkAddress.setPreferredSize(new Dimension(90, 15));
		lblLinkAddress.setText("Link");
		panNewWindow.setLayout(new GridBagLayout());
		rbBlank.setActionCommand("ohne Überschrift");
		rbBlank.setText("ohne Überschrift");
		rbHeader.setText("mit Überschrift");
		cbNewWindow.setText(" im neuen Fenster öffnen");
		panLinkProperties.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(165, 163, 151)), rb.getString("panel.panelView.link.linkProperties")));
		panLinkProperties.setLayout(new GridBagLayout());
		getOptPan().setLayout(new GridBagLayout());
		panNewWindow.add(rbBlank, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 10), 0, 0));
		panNewWindow.add(rbHeader, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 10), 0, 0));
		panLinkSuggestion.setLayout(new GridBagLayout());
		panLinkSuggestion.add(lblResultAddress, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		panLinkSuggestion.add(buttonUseSuggested, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 4));
		getOptPan().add(spacer, new GridBagConstraints(0, 2, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		getOptPan().add(panLinkProperties, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 8, 0, 8), 0, -11));
		panLinkProperties.add(lblLinkAddress, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 4));
		panLinkProperties.add(txtLinkUrl, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 4));
		panLinkProperties.add(panLinkSuggestion, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 4));
		panLinkProperties.add(cbNewWindow, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 4));
		panLinkProperties.add(panNewWindow, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 10, 10), 0, 4));
		buttonGroup.add(rbBlank);
		buttonGroup.add(rbHeader);
	}

	@Override
	public void save() throws Exception {
		String vindexSave = this.getViewComponent().getViewIndex();
		super.save();
		boolean edited = false;
		if (!this.getViewComponent().getReference().equalsIgnoreCase(txtLinkUrl.getText())) edited = true;
		this.getViewComponent().setReference(txtLinkUrl.getText());

		if (rbHeader.isSelected()) {
			if (!"4".equalsIgnoreCase(this.getViewComponent().getViewIndex())) edited = true;
			this.getViewComponent().setViewIndex("4");
			if (!"7".equalsIgnoreCase(this.getViewComponent().getViewLevel())) edited = true;
			this.getViewComponent().setViewLevel("7");
			if ((this.getViewComponent().getDisplaySettings() & Constants.DISPLAY_SETTING_SHOW_HEADER) != Constants.DISPLAY_SETTING_SHOW_HEADER) edited = true;
			this.getViewComponent().setDisplaySettings((byte) (this.getViewComponent().getDisplaySettings() | Constants.DISPLAY_SETTING_SHOW_HEADER));
		} else {
			if (!"direct".equalsIgnoreCase(vindexSave)) edited = true;
			this.getViewComponent().setViewIndex("direct");
			if (!"7".equalsIgnoreCase(this.getViewComponent().getViewLevel())) edited = true;
			this.getViewComponent().setViewLevel("7");
			if ((this.getViewComponent().getDisplaySettings() & Constants.DISPLAY_SETTING_SHOW_HEADER) != 0) edited = true;
			this.getViewComponent().setDisplaySettings((byte) (this.getViewComponent().getDisplaySettings() & ~Constants.DISPLAY_SETTING_SHOW_HEADER));
		}
		if (this.cbNewWindow.isSelected()) {
			if ((this.getViewComponent().getDisplaySettings() & Constants.DISPLAY_SETTING_NEW_WINDOW) != 1) edited = true;
			this.getViewComponent().setDisplaySettings((byte) (this.getViewComponent().getDisplaySettings() | Constants.DISPLAY_SETTING_NEW_WINDOW));
		} else {
			if ((this.getViewComponent().getDisplaySettings() & Constants.DISPLAY_SETTING_NEW_WINDOW) != 0) edited = true;
			this.getViewComponent().setDisplaySettings((byte) (this.getViewComponent().getDisplaySettings() & ~Constants.DISPLAY_SETTING_NEW_WINDOW));
		}

		if (edited) {
			this.getViewComponent().setStatus(Constants.DEPLOY_STATUS_EDITED);
			// If you checkIn a NEW Contentversion, your "REMOVE-COMMAND" will also be removed
			if (this.getViewComponent().getDeployCommand() == Constants.DEPLOY_COMMAND_DELETE || this.getViewComponent().getDeployCommand() == Constants.DEPLOY_COMMAND_REMOVE) {
				this.getViewComponent().setDeployCommand(Constants.DEPLOY_COMMAND_MODIFY);
			}
			log.debug("updating status for (external) link");
			comm.updateStatus4ViewComponent(this.getViewComponent());
		}

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

	@Override
	public void load(ViewComponentValue vcd) {
		super.load(vcd);
		txtLinkUrl.setText(this.getViewComponent().getReference());
		/*
		String level = this.getViewComponent().getViewLevel();
		String index = this.getViewComponent().getViewIndex();
		try {
			if (level.equals("7") && index.equals("4")) {
				rbHeader.setSelected(true);
			} else {
				rbBlank.setSelected(true);
			}
		} catch (Exception ex) {
			rbBlank.setSelected(true);
		}
		*/
		getLblUrlLinkName().setVisible(false);
		getTxtUrlLinkName().setVisible(false);
		getChkSearchIndexed().setVisible(false);
		getChkXmlSearchIndexed().setVisible(false);
		this.cbNewWindow.setSelected((this.getViewComponent().getDisplaySettings() & Constants.DISPLAY_SETTING_NEW_WINDOW) == Constants.DISPLAY_SETTING_NEW_WINDOW);
		if ((this.getViewComponent().getDisplaySettings() & Constants.DISPLAY_SETTING_SHOW_HEADER) == Constants.DISPLAY_SETTING_SHOW_HEADER) {
			this.rbHeader.setSelected(true);
		} else {
			this.rbBlank.setSelected(true);
		}
	}

}