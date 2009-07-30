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

import javax.swing.*;

/**
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class CreateNewSiteGroupDialog extends JDialog {
	private JPanel jContentPane = null;
	private JLabel lblIntroText = null;
	private JLabel lblGroupName = null;
	private JTextField txtSiteGroupName = null;
	private JPanel panButtons = null;
	private JButton btnOk = null;
	private JButton btnCancel = null;
	private JPanel panMain = null;
	private String siteGroupName = null;
	private boolean btnOkKlicked = false;

	/**
	 * This is the default constructor
	 */
	public CreateNewSiteGroupDialog() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(400, 200);
		this.setResizable(false);
		this.setContentPane(getJContentPane());
		this.getRootPane().setDefaultButton(this.btnOk);
		this.setTitle(rb.getString("panel.panelSiteGroups.group.createnew"));
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
	 * This method initializes txtSiteGroupName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtSiteGroupName() {
		if (txtSiteGroupName == null) {
			txtSiteGroupName = new JTextField();
		}
		return txtSiteGroupName;
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
					siteGroupName = getTxtSiteGroupName().getText();
					if (siteGroupName.trim().length() > 1) {
						btnOkKlicked = true;
						dispose();
					} else {
						JOptionPane.showMessageDialog(null, rb.getString("panel.panelSiteGroups.group.nameplease"));
					}
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
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridwidth = 2;
			gridBagConstraints3.gridy = 2;
			gridBagConstraints3.ipadx = 171;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints3.insets = new java.awt.Insets(10,10,10,10);
			gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints3.gridx = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.ipadx = 179;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints2.insets = new java.awt.Insets(10,10,0,10);
			gridBagConstraints2.gridx = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.ipady = 6;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints1.insets = new java.awt.Insets(10,10,0,0);
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints.insets = new java.awt.Insets(10,10,0,10);
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridx = 0;
			this.panMain = new JPanel();
			this.panMain.setLayout(new GridBagLayout());
			lblGroupName = new JLabel();
			lblGroupName.setText(rb.getString("panel.panelSiteGroups.group.name"));
			lblIntroText = new JLabel();
			lblIntroText.setText(rb.getString("panel.panelSiteGroups.group.nameplease"));
			panMain.add(lblIntroText, gridBagConstraints);
			panMain.add(lblGroupName, gridBagConstraints1);
			panMain.add(getTxtSiteGroupName(), gridBagConstraints2);
			panMain.add(getPanButtons(), gridBagConstraints3);
		}
		return this.panMain;
	}

	public String getSiteGroupName() {
		return this.getTxtSiteGroupName().getText();
	}

	public boolean isBtnOkKlicked() {
		return this.btnOkKlicked;
	}

}
