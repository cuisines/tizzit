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
package de.juwimm.cms.gui.views.safeguard;

import static de.juwimm.cms.client.beans.Application.*;
import static de.juwimm.cms.common.Constants.*;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.gui.admin.safeguard.PanChooseLoginPage;
import de.juwimm.cms.safeguard.vo.RealmSimplePwUserValue;
import de.juwimm.cms.safeguard.vo.RealmSimplePwValue;
import de.juwimm.cms.util.Communication;
import de.juwimm.swing.DropDownHolder;
import de.juwimm.swing.SortingListModel;

/**
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class EditSimplePwRealmDlg extends JDialog {
	private static final long serialVersionUID = 6152670363520934372L;
	private Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private JPanel jContentPane = null;
	private JList lstUser = null;
	private JButton btnUserNew = null;
	private JButton btnUserSave = null;
	private JButton btnUserDelete = null;
	private JLabel lblUserId = null;
	private JLabel lblUserIdDisplay = null;
	private JLabel lblUserName = null;
	private JLabel lblPassword = null;
	private JLabel lblRoles = null;
	private JTextField txtUserName = null;
	private JTextField txtPassword = null;
	private JTextField txtRoles = null;
	private JButton btnOk = null;
	private JPanel panButtons = null;
	private PanChooseLoginPage panChooseLoginPage = null;
	private RealmSimplePwValue realmValue = null;

	/**
	 * This is the default constructor
	 */
	public EditSimplePwRealmDlg(RealmSimplePwValue realmValue) {
		super();
		this.realmValue = realmValue;
		initialize();
		if (this.realmValue.getLoginPageId() != null && this.realmValue.getLoginPageId().length() > 0) {
			this.panChooseLoginPage.setLoginpage(this.realmValue.getLoginPageId());
		}
		this.setActive(false);
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setSize(549, 350);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridx = 2;
			gridBagConstraints11.gridy = 3;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints11.insets = new java.awt.Insets(10,10,0,10);
			gridBagConstraints11.weightx = 1.0;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
			gridBagConstraints12.insets = new java.awt.Insets(12, 10, 0, 0);
			this.panChooseLoginPage = new PanChooseLoginPage();
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
			gridBagConstraints8.fill = java.awt.GridBagConstraints.NONE;
			gridBagConstraints8.insets = new java.awt.Insets(20, 0, 10, 10);
			gridBagConstraints8.gridy = 6;
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.NORTHEAST;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints(2, 3, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
			gridBagConstraints7.gridx = 1;
			gridBagConstraints7.gridwidth = 2;
			gridBagConstraints7.weighty = 0.0;
			gridBagConstraints7.insets = new java.awt.Insets(20, 10, 0, 10);
			gridBagConstraints7.gridy = 4;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints(2, 2, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
			gridBagConstraints6.insets = new java.awt.Insets(10, 10, 0, 10);
			gridBagConstraints6.weighty = 0.0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints(2, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
			gridBagConstraints5.insets = new java.awt.Insets(10, 10, 0, 10);
			gridBagConstraints5.weighty = 0.0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
			gridBagConstraints4.insets = new java.awt.Insets(12, 10, 0, 0);
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
			gridBagConstraints3.insets = new java.awt.Insets(12, 10, 0, 0);
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
			gridBagConstraints2.insets = new java.awt.Insets(20, 12, 0, 10);
			gridBagConstraints2.weighty = 0.0;
			gridBagConstraints2.weightx = 0.0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
			gridBagConstraints1.insets = new java.awt.Insets(20, 10, 0, 0);
			GridBagConstraints gridBagConstraints = new GridBagConstraints(0, 0, 1, 4, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
			gridBagConstraints.weightx = 3.0D;
			gridBagConstraints.gridheight = 5;
			gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints(0, 5, 4, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0);
			lblPassword = new JLabel();
			lblPassword.setText(rb.getString("panel.login.password") + ":");
			lblUserName = new JLabel();
			lblUserName.setText(rb.getString("panel.login.username") + ":");
			lblRoles = new JLabel();
			lblRoles.setText(rb.getString("panel.panelSafeguard.grantedRoles"));
			lblUserIdDisplay = new JLabel();
			lblUserIdDisplay.setText("");
			lblUserId = new JLabel();
			lblUserId.setText("User ID:");
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(new JScrollPane(getLstUser()), gridBagConstraints);
			jContentPane.add(lblUserId, gridBagConstraints1);
			jContentPane.add(lblUserIdDisplay, gridBagConstraints2);
			jContentPane.add(lblUserName, gridBagConstraints3);
			jContentPane.add(lblPassword, gridBagConstraints4);
			jContentPane.add(lblRoles, gridBagConstraints12);
			jContentPane.add(getTxtUserName(), gridBagConstraints5);
			jContentPane.add(getTxtPassword(), gridBagConstraints6);
			jContentPane.add(getTxtRoles(), gridBagConstraints11);
			jContentPane.add(getBtnOk(), gridBagConstraints8);
			jContentPane.add(getPanButtons(), gridBagConstraints7);
			jContentPane.add(this.panChooseLoginPage, gridBagConstraints9);
		}
		return jContentPane;
	}

	/**
	 * This method initializes lstUser	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getLstUser() {
		if (lstUser == null) {
			lstUser = new JList();
			lstUser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			SortingListModel listModel = new SortingListModel();
			if (this.realmValue != null) {
				RealmSimplePwUserValue[] val = comm.getUserForSimplePwRealm(Integer.valueOf(this.realmValue.getSimplePwRealmId()));
				if (val != null) {
					for (int i = 0; i < val.length; i++) {
						listModel.addElement(new DropDownHolder(val[i], val[i].getUserName()));
					}
				}
			}
			lstUser.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
				public void valueChanged(javax.swing.event.ListSelectionEvent e) {
					listSelected();
				}
			});
			lstUser.setModel(listModel);
		}
		return lstUser;
	}

	/**
	 * This method initializes btnUserNew	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnUserNew() {
		if (btnUserNew == null) {
			btnUserNew = new JButton();
			btnUserNew.setText(rb.getString("panel.panelSafeguard.btn.createuser"));
			btnUserNew.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					lstUser.clearSelection();
					lblUserIdDisplay.setText("");
					txtUserName.setText("[new User]");
					txtPassword.setText("");
					setActive(true);
				}
			});
		}
		return btnUserNew;
	}

	/**
	 * This method initializes btnUserSave	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnUserSave() {
		if (btnUserSave == null) {
			btnUserSave = new JButton();
			btnUserSave.setText(rb.getString("panel.panelSafeguard.btn.saveuser"));
			btnUserSave.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					RealmSimplePwUserValue value = new RealmSimplePwUserValue();
					if (lblUserIdDisplay.getText().equalsIgnoreCase("")) {
						value.setSimplePwRealmUserId(-1);
					} else {
						value.setSimplePwRealmUserId(Integer.parseInt(lblUserIdDisplay.getText()));
					}
					value.setUserName(getTxtUserName().getText());
					value.setPassword(getTxtPassword().getText());
					value.setRoles(getTxtRoles().getText());
					if (value.getSimplePwRealmUserId() == -1) {
						value.setSimplePwRealmUserId(comm.addUserToSimpleRealm(Integer.valueOf(realmValue.getSimplePwRealmId()), value));
						((SortingListModel) lstUser.getModel()).addElement(new DropDownHolder(value, value.getUserName()));
					} else {
						comm.deleteSimplePwRealmUser(Integer.valueOf(value.getSimplePwRealmUserId()));
						((SortingListModel) lstUser.getModel()).removeElementAt(lstUser.getSelectedIndex());
						value.setSimplePwRealmUserId(comm.addUserToSimpleRealm(Integer.valueOf(realmValue.getSimplePwRealmId()), value));
						lblUserIdDisplay.setText(String.valueOf(value.getSimplePwRealmUserId()));
						((SortingListModel) lstUser.getModel()).addElement(new DropDownHolder(value, value.getUserName()));
					}
				}
			});
		}
		return btnUserSave;
	}

	/**
	 * This method initializes btnUserDelete	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnUserDelete() {
		if (btnUserDelete == null) {
			btnUserDelete = new JButton();
			btnUserDelete.setText(rb.getString("panel.panelSafeguard.btn.deleteuser"));
			btnUserDelete.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					deleteUser();
				}
			});
		}
		return btnUserDelete;
	}

	/**
	 * This method initializes txtUserName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtUserName() {
		if (txtUserName == null) {
			txtUserName = new JTextField();
		}
		return txtUserName;
	}

	/**
	 * This method initializes txtPassword	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtPassword() {
		if (txtPassword == null) {
			txtPassword = new JTextField();
		}
		return txtPassword;
	}

	/**
	 * This method initializes txtRoles	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtRoles() {
		if (txtRoles == null) {
			txtRoles = new JTextField();
			txtRoles.setToolTipText(rb.getString("panel.panelSafeguard.grantedRoles.tt"));
		}
		return txtRoles;
	}

	/**
	 * This method initializes btnOk	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnOk() {
		if (btnOk == null) {
			btnOk = new JButton();
			btnOk.setText(rb.getString("dialog.ok"));
			btnOk.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return btnOk;
	}

	/**
	 * This method initializes panButtons	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanButtons() {
		if (panButtons == null) {
			panButtons = new JPanel();
			panButtons.setLayout(new FlowLayout());
			panButtons.add(getBtnUserNew(), null);
			panButtons.add(getBtnUserSave(), null);
			panButtons.add(getBtnUserDelete(), null);
		}
		return panButtons;
	}

	private void listSelected() {
		if (this.getLstUser().getSelectedIndex() != -1) {
			RealmSimplePwUserValue val = (RealmSimplePwUserValue) ((DropDownHolder) this.getLstUser().getSelectedValue()).getObject();
			this.lblUserIdDisplay.setText(String.valueOf(val.getSimplePwRealmUserId()));
			this.getTxtUserName().setText(val.getUserName());
			this.getTxtPassword().setText(val.getPassword());
			this.getTxtRoles().setText(val.getRoles());
			this.setActive(true);
		}
	}

	private void deleteUser() {
		int ret = JOptionPane.showConfirmDialog(this, rb.getString("panel.panelSafeguard.user.reallydelete"), rb.getString("panel.panelSafeguard.user.reallydelete"), JOptionPane.YES_NO_OPTION);
		if (ret == JOptionPane.OK_OPTION) {
			if (this.getLstUser().isSelectionEmpty()) return;
			RealmSimplePwUserValue val = (RealmSimplePwUserValue) ((DropDownHolder) this.getLstUser().getSelectedValue()).getObject();
			comm.deleteSimplePwRealmUser(Integer.valueOf(val.getSimplePwRealmUserId()));
			((SortingListModel) this.getLstUser().getModel()).removeElementAt(this.getLstUser().getSelectedIndex());
			this.lblUserIdDisplay.setText("");
			this.getTxtUserName().setText("");
			this.getTxtPassword().setText("");
			this.getTxtRoles().setText("");
			this.lstUser.clearSelection();
			this.setActive(false);
		}
	}

	private void setActive(boolean enabled) {
		this.getTxtUserName().setEnabled(enabled);
		this.getTxtPassword().setEnabled(enabled);
		this.getTxtRoles().setEnabled(enabled);
		this.getBtnUserSave().setEnabled(enabled);
		this.getBtnUserDelete().setEnabled(enabled);
	}

	public String getLoginPage() {
		return this.panChooseLoginPage.getLoginPageViewComponentId();
	}

} //  @jve:decl-index=0:visual-constraint="10,10"
