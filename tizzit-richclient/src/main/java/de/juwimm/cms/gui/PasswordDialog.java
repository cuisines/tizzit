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

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.*;

import org.apache.log4j.Logger;

import de.juwimm.cms.Messages;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;

/**
 * <p>Title: juwimm cms</p>
 * <p>Description: content management system</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: juwi macmillan group gmbh</p>
 * @author Dirk Bogun
 * @version $Id$
 */
public class PasswordDialog extends JDialog {
	private static Logger log = Logger.getLogger(PasswordDialog.class);
	private JLabel lblPasswd = new JLabel();
	private JLabel lblPasswdRepeat = new JLabel();
	private JPasswordField txtNewPassword1 = new JPasswordField();
	private JPasswordField txtNewPassword2 = new JPasswordField();
	private JButton cmdChange = new JButton();
	private JButton cmdCancel = new JButton();
	private String whom = null;
	private JLabel lblChange = new JLabel();

	public PasswordDialog(Frame frame, String title, boolean modal) {
		super(frame, title, modal);
		try {
			jbInit();
			pack();
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	public PasswordDialog(String whom) {
		this(UIConstants.getMainFrame(), "", true);
		this.whom = whom;
		int frameHeight = 190;
		int frameWidth = 305;
		this.setSize(frameWidth, frameHeight);
		this.setLocationRelativeTo(UIConstants.getMainFrame());
		this.setTitle(rb.getString("msgbox.title.changePassword"));
		//setIconImage(UIConstants.WIZARD_ICON_INSTALL.getImage());
		cmdChange.setText(rb.getString("dialog.ok"));
		cmdCancel.setText(rb.getString("dialog.cancel"));
		lblPasswdRepeat.setText(rb.getString("frame.changePasswd.newPasswd"));
		lblPasswd.setText(rb.getString("frame.changePasswd.repeatPasswd"));
		lblChange.setText(Messages.getString("frame.changePasswd.whomMessage", whom));
	}

	private void jbInit() throws Exception {
		this.getContentPane().setLayout(new GridBagLayout());
		lblPasswd.setText("Passwort wiederholen");
		lblPasswdRepeat.setText("neues Passwort");
		cmdChange.setText("Übernehmen");
		cmdChange.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdChangeActionPerformed(e);
			}
		});
		cmdCancel.setText("Abbrechen");
		cmdCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdCancelActionPerformed(e);
			}
		});
		this.setEnabled(true);
		lblChange.setText("Change password:");
		getContentPane().add(lblPasswdRepeat, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));
		getContentPane().add(lblPasswd, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		getContentPane().add(cmdChange, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		getContentPane().add(txtNewPassword1, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));
		getContentPane().add(txtNewPassword2, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));
		getContentPane().add(cmdCancel, new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		this.getContentPane().add(lblChange, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
	}

	void cmdChangeActionPerformed(ActionEvent e) {
		if (String.copyValueOf(txtNewPassword1.getPassword()).equals(String.copyValueOf(txtNewPassword2.getPassword()))) {
			try {
				((Communication) getBean(Beans.COMMUNICATION)).changePassword(whom, String.copyValueOf(txtNewPassword2.getPassword()));
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, rb.getString("frame.changePasswd.msgError") + "\n"
						+ ex.getMessage().toString(), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
				return;
			}
			UIConstants.setStatusInfo(rb.getString("frame.changePasswd.msgSucc"));
			JOptionPane.showMessageDialog(this, rb.getString("frame.changePasswd.msgSucc"),
					rb.getString("dialog.title"), JOptionPane.INFORMATION_MESSAGE);
			setVisible(false);
			dispose();
		} else {
			JOptionPane.showMessageDialog(this, rb.getString("frame.changePasswd.msgNoMatch"),
					rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
			txtNewPassword1.setText("");
			txtNewPassword2.setText("");
			txtNewPassword1.requestFocus();
		}
	}

	void cmdCancelActionPerformed(ActionEvent e) {
		setVisible(false);
		dispose();
	}

}
