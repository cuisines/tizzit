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
package de.juwimm.cms.gui.admin;

import static de.juwimm.cms.common.Constants.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.juwimm.swing.PickListData;
import de.juwimm.swing.PickListPanel;

/**
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class ManageSiteGroupDialog extends JDialog {
	private JPanel jContentPane = null;
	private JLabel lblIntroText = null;
	private JPanel panButtons = null;
	private JButton btnOk = null;
	private JButton btnCancel = null;
	private JPanel panMain = null;
	private boolean btnOkKlicked = false;
	private PickListData pickListData = null;
	private PickListPanel pickListPanel = null;

	/**
	 * This is the default constructor
	 */
	public ManageSiteGroupDialog(PickListData pickListData) {
		super();
		this.pickListData = pickListData;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(800, 600);
		this.setResizable(false);
		this.setContentPane(getJContentPane());
		this.getRootPane().setDefaultButton(this.btnOk);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = getPanMain();
		}
		return jContentPane;
	}

	/**
	 * This method initializes panButtons	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanButtons() {
		if (panButtons == null) {
			panButtons = new JPanel();
			panButtons.add(getBtnOk(), null);
			panButtons.add(getBtnCancel(), null);
		}
		return panButtons;
	}

	/**
	 * This method initializes jButtonOK	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnOk() {
		if (btnOk == null) {
			btnOk = new JButton();
			btnOk.setText(rb.getString("dialog.ok"));
			btnOk.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					btnOkKlicked = true;
					dispose();
				}
			});
		}
		return btnOk;
	}

	/**
	 * This method initializes jButtonCancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton();
			btnCancel.setText(rb.getString("dialog.cancel"));
			btnCancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					btnOkKlicked = false;
					dispose();
				}
			});
		}
		return btnCancel;
	}

	private JPanel getPanMain() {
		if (this.panMain == null) {
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 0;
			gridBagConstraints31.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints31.insets = new java.awt.Insets(10,10,10,10);
			gridBagConstraints31.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints31.gridwidth = 2;
			gridBagConstraints31.gridheight = 2;
			gridBagConstraints31.weightx = 1.0;
			gridBagConstraints31.weighty = 1.0;
			gridBagConstraints31.gridy = 2;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridwidth = 2;
			gridBagConstraints3.gridy = 4;
			gridBagConstraints3.ipadx = 171;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.weighty = 1.0;
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints3.gridx = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints.insets = new java.awt.Insets(10,10,0,10);
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridx = 0;
			this.panMain = new JPanel();
			this.panMain.setLayout(new GridBagLayout());
			lblIntroText = new JLabel();
			lblIntroText.setText(rb.getString("panel.panelSiteGroups.warning"));
			panMain.add(lblIntroText, gridBagConstraints);
			panMain.add(getPanButtons(), gridBagConstraints3);
			panMain.add(getPickListPanel(), gridBagConstraints31);
		}
		return this.panMain;
	}

	public boolean isBtnOkKlicked() {
		return this.btnOkKlicked;
	}

	public PickListData getPickListData() {
		return pickListData;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private PickListPanel getPickListPanel() {
		if (pickListPanel == null) {
			pickListPanel = new PickListPanel(this.getPickListData());
			pickListPanel.setEnabled(true);
		}
		return pickListPanel;
	}

}
