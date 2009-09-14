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

import static de.juwimm.cms.client.beans.Application.getBean;
import static de.juwimm.cms.common.Constants.rb;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.exceptions.NeededFieldsMissingException;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.UnitValue;
import de.juwimm.swing.DropDownHolder;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class CreateNewUserDialog extends JFrame {
	private static Logger log = Logger.getLogger(CreateNewUserDialog.class);
	private Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private PanUser panUser = null;
	private JPanel panMain = new JPanel();
	private JButton btnOk = new JButton();
	private JButton btnCancel = new JButton();
	private JLabel lblUserName = new JLabel();
	private JTextField txtUsername = new JTextField();
	private JLabel lblFirstName = new JLabel();
	private JLabel lblLastName = new JLabel();
	private JLabel lblEMail = new JLabel();
	private JLabel lblPassword = new JLabel();
	private JLabel lblPasswordRepeat = new JLabel();
	private JLabel lblUnits = new JLabel();
	private JTextField txtFirstName = new JTextField();
	private JTextField txtLastName = new JTextField();
	private JTextField txtEmail = new JTextField();
	private JPasswordField pwdOne = new JPasswordField();
	private JPasswordField pwdTwo = new JPasswordField();
	private JComboBox cmbUnits = new JComboBox();
	private DropDownHolder[] allUnits;
	private UnitValue[] units;

	public CreateNewUserDialog(PanUser panUser) {
		this.panUser = panUser;
		try {
			jbInit();
			pack();
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
		int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int frameHeight = 280;
		int frameWidth = 400;
		setSize(frameWidth, frameHeight);
		setLocation((screenWidth / 2) - (frameWidth / 2), (screenHeight / 2) - (frameHeight / 2));
		setTitle(rb.getString("frame.createUser.title"));
		setResizable(false);

		setIconImage(UIConstants.MODULE_DATABASECOMPONENT_ADD.getImage());
		btnOk.setText(rb.getString("dialog.add"));
		btnCancel.setText(rb.getString("dialog.cancel"));
		lblUserName.setText(rb.getString("user.userName"));
		lblFirstName.setText(rb.getString("user.firstName"));
		lblLastName.setText(rb.getString("user.lastName"));
		lblEMail.setText(rb.getString("user.eMail"));
		lblPassword.setText(rb.getString("frame.changePasswd.newPasswd"));
		lblPasswordRepeat.setText(rb.getString("frame.changePasswd.repeatPasswd"));
	}

	private void jbInit() throws Exception {
		panMain.setLayout(new GridBagLayout());
		btnOk.setText("Hinzufügen");
		btnOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnOkActionPerformed(e);
			}
		});
		btnCancel.setText("Abbrechen");
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCancelActionPerformed(e);
			}
		});
		lblUserName.setText("Benutzername");
		lblFirstName.setText("Vorname");
		lblLastName.setText("Nachname");
		lblEMail.setText("eMail");
		lblPassword.setText("Passwort");
		lblPasswordRepeat.setText("Passwort wiederholen");
		lblUnits.setText("Einrichtungen");

		units = comm.getUnits();
		if (units != null) {
			for (int i = 0; i < units.length; i++) {
				cmbUnits.addItem(units[i].getName());
			}
		}

		getContentPane().add(panMain);
		panMain.add(btnOk, new GridBagConstraints(0, 7, 1, 1, 0.0, 1.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		panMain.add(btnCancel, new GridBagConstraints(1, 7, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		panMain.add(lblUserName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 21), 0, 0));
		panMain.add(txtUsername, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));
		panMain.add(lblFirstName, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		panMain.add(lblLastName, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));
		panMain.add(lblEMail, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));
		panMain.add(lblPassword, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));
		panMain.add(lblPasswordRepeat, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));
		panMain.add(lblUnits, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));
		panMain.add(txtFirstName, new GridBagConstraints(1, 1, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));
		panMain.add(txtLastName, new GridBagConstraints(1, 2, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));
		panMain.add(txtEmail, new GridBagConstraints(1, 3, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));
		panMain.add(pwdOne, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 80), 0, 0));
		panMain.add(pwdTwo, new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 80), 0, 0));
		panMain.add(cmbUnits, new GridBagConstraints(1, 6, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 80), 0, 0));
	}

	void btnCancelActionPerformed(ActionEvent e) {
		this.setVisible(false);
		this.dispose();
	}

	void btnOkActionPerformed(ActionEvent e) {
		this.setEnabled(false);
		if (String.copyValueOf(pwdOne.getPassword()).equals(String.copyValueOf(pwdTwo.getPassword()))) {
			Pattern p = Pattern.compile("^[A-Za-z_0-9\\.-]+@[A-Za-z_0-9\\.-]+\\.[a-zA-Z]+");
			Matcher m = p.matcher(this.txtEmail.getText());
			int selectedUnit = cmbUnits.getSelectedIndex();
			if (m.matches() || this.txtEmail.getText().length() == 0) {
				try {
					((Communication) getBean(Beans.COMMUNICATION)).createUser(this.txtUsername.getText(), String.copyValueOf(pwdOne.getPassword()), this.txtFirstName.getText(), this.txtLastName.getText(), this.txtEmail.getText(), units[selectedUnit].getUnitId());
				} catch (NeededFieldsMissingException nfme) {
					String msg = rb.getString("exception.NeededFieldsMissingException");
					msg = msg + nfme.getMissingFieldsLocaleString();
					JOptionPane.showMessageDialog(this, msg, rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
					this.setEnabled(true);
					this.setVisible(true);
					return;
				} catch (Exception excep) {
					String msg = excep.getMessage();
					if (msg.startsWith("de.juwimm.cms.exceptions.UserException: Entity with primary key")) msg = rb.getString("exception.UsernameAlreadyInUse");
					if (msg.startsWith("java.sql.BatchUpdateException: ")) msg = rb.getString("exception.UsernameAlreadyInUse");
					if (msg.contains("ConstraintViolationException ")) msg = rb.getString("exception.UsernameAlreadyInUse");
					JOptionPane.showMessageDialog(this, msg, rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
					this.setEnabled(true);
					this.setVisible(true);
					return;
				}
				this.setVisible(false);
				panUser.reload();
				this.dispose();
			} else {
				String msg = rb.getString("exception.EmailIsNotValid");
				JOptionPane.showMessageDialog(UIConstants.getMainFrame(), msg, rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
				this.setVisible(true);
				this.txtEmail.requestFocus();
			}

		} else {
			JOptionPane.showMessageDialog(this, rb.getString("frame.changePasswd.msgNoMatch"), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
			pwdOne.setText("");
			pwdTwo.setText("");
			pwdOne.requestFocus();
		}
		this.setEnabled(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		this.btnCancel.setEnabled(b);
		this.btnOk.setEnabled(b);
		this.txtUsername.setEnabled(b);
		this.txtFirstName.setEnabled(b);
		this.txtLastName.setEnabled(b);
		this.txtEmail.setEnabled(b);
		this.pwdOne.setEnabled(b);
		this.pwdTwo.setEnabled(b);
	}

}
