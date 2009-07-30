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
package de.juwimm.cms.http;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.*;

import de.juwimm.cms.Messages;

/**
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class DlgUsernamePassword extends JDialog {
	private javax.swing.JPanel jContentPane = null; //  @jve:decl-index=0:visual-constraint="10,10"
	private JTextField txtUsername = null;
	private JPasswordField txtPassword = null;
	private JTextField txtNTDomain = null;
	private JCheckBox cboSave = null;
	private JButton btnOk = null;
	private JButton btnCancel = null;
	private JLabel lblNTDomain = new JLabel();
	private boolean isCanceled = true;

	/**
	 * This is the default constructor
	 */
	public DlgUsernamePassword() {
		super();
		initialize();
		this.setModal(true);
		this.setAlwaysOnTop(true);
		this.txtNTDomain.setVisible(false);
		this.lblNTDomain.setVisible(false);
		
		int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		this.setLocation((screenWidth / 2) - (getWidth() / 2), (screenHeight / 2) - (getHeight() / 2));
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setTitle(Messages.getString("FrmUsernamePassword.title"));
		this.setSize(422, 220);
		this.setContentPane(getJContentPane());
		this.getRootPane().setDefaultButton(getBtnOk());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {
			java.awt.GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints7 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			javax.swing.JLabel lblPassword = new JLabel();
			java.awt.GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			javax.swing.JLabel lblUsername = new JLabel();
			java.awt.GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			javax.swing.JLabel lblProsa = new JLabel();
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new GridBagLayout());
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridwidth = 2;
			gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
			lblProsa.setText(Messages.getString("FrmUsernamePassword.prosa"));
			gridBagConstraints8.gridx = 1;
			gridBagConstraints8.gridy = 6;
			gridBagConstraints8.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints8.weightx = 1.0D;
			gridBagConstraints8.weighty = 1.0D;
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.gridy = 7;
			gridBagConstraints9.insets = new java.awt.Insets(0, 5, 5, 0);
			gridBagConstraints10.gridx = 1;
			gridBagConstraints10.gridy = 7;
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.SOUTHWEST;
			gridBagConstraints10.insets = new java.awt.Insets(0, 0, 5, 5);
			
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 3;
			gridBagConstraints2.insets = new java.awt.Insets(0, 5, 5, 5);
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
			lblUsername.setText(Messages.getString("FrmUsernamePassword.username"));
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 4;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints3.insets = new java.awt.Insets(0, 5, 5, 0);
			lblPassword.setText(Messages.getString("FrmUsernamePassword.password"));
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.gridy = 3;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.insets = new java.awt.Insets(0, 0, 5, 5);
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.gridy = 4;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.insets = new java.awt.Insets(0, 0, 5, 5);
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.gridy = 5;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.insets = new java.awt.Insets(0, 0, 5, 5);
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridy = 5;
			gridBagConstraints7.insets = new java.awt.Insets(0, 5, 5, 0);
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.NORTHWEST;
			lblNTDomain.setText(Messages.getString("FrmUsernamePassword.ntDomain"));
			jContentPane.add(lblProsa, gridBagConstraints1);
			jContentPane.add(lblUsername, gridBagConstraints2);
			jContentPane.add(lblPassword, gridBagConstraints3);
			jContentPane.add(getTxtUsername(), gridBagConstraints4);
			jContentPane.add(getTxtPassword(), gridBagConstraints5);
			jContentPane.add(getTxtNTDomain(), gridBagConstraints6);
			jContentPane.add(lblNTDomain, gridBagConstraints7);
			jContentPane.add(getCboSave(), gridBagConstraints8);
			jContentPane.add(getBtnOk(), gridBagConstraints9);
			jContentPane.add(getBtnCancel(), gridBagConstraints10);
			jContentPane.setSize(476, 201);
		}
		return jContentPane;
	}

	/**
	 * This method initializes txtUsername	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	protected JTextField getTxtUsername() {
		if (txtUsername == null) {
			txtUsername = new JTextField();
		}
		return txtUsername;
	}

	/**
	 * This method initializes txtPassword	
	 * 	
	 * @return javax.swing.JPasswordField	
	 */
	protected JPasswordField getTxtPassword() {
		if (txtPassword == null) {
			txtPassword = new JPasswordField();
		}
		return txtPassword;
	}

	/**
	 * This method initializes txtNTDomain	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	protected JTextField getTxtNTDomain() {
		if (txtNTDomain == null) {
			txtNTDomain = new JTextField();
		}
		return txtNTDomain;
	}

	/**
	 * This method initializes cboSave	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	protected JCheckBox getCboSave() {
		if (cboSave == null) {
			cboSave = new JCheckBox();
			cboSave.setText(Messages.getString("FrmUsernamePassword.saveUsernameAndPassword"));
			cboSave.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			cboSave.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		}
		return cboSave;
	}

	/**
	 * This method initializes btnOk	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnOk() {
		if (btnOk == null) {
			btnOk = new JButton();
			btnOk.setText(Messages.getString("dialog.ok"));
			btnOk.setMnemonic(java.awt.event.KeyEvent.VK_ENTER);
			btnOk.setSelected(false);
			btnOk.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					isCanceled = false;
					setVisible(false);
				}
			});
		}
		return btnOk;
	}

	/**
	 * This method initializes btnCancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton();
			btnCancel.setText(Messages.getString("dialog.cancel"));
			btnCancel.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {   
					isCanceled = true;
					setVisible(false);
				}
			});
		}
		return btnCancel;
	}
	/**
	 * @return Returns the isCanceled.
	 */
	protected boolean isCanceled() {
		return this.isCanceled;
	}
} //  @jve:decl-index=0:visual-constraint="18,12"
