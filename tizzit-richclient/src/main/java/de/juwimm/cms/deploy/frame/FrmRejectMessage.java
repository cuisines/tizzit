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
package de.juwimm.cms.deploy.frame;

import static de.juwimm.cms.common.Constants.*;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import de.juwimm.cms.Messages;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.ViewComponentValue;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class FrmRejectMessage extends JFrame {
	private static Logger log = Logger.getLogger(FrmRejectMessage.class);
	private int pid = 0;
	public static final int BUTTON_REJECT = 2;
	public static final int BUTTON_CANCEL = 1;
	public static final int BUTTON_NOACTION = 0;
	private JPanel panTop = new JPanel();
	private JScrollPane spTextArea = new JScrollPane();
	private JTextArea txtArea = new JTextArea();
	private JButton btnReject = new JButton();
	private JButton btnCancel = new JButton();
	private JLabel lblExplain = new JLabel();

	public FrmRejectMessage() {
		try {
			pid = 0;
			jbInit();
			if (rb != null) {
				btnReject.setText(rb.getString("panel.frmRejectMessage.btnReject"));
				btnCancel.setText(rb.getString("dialog.cancel"));
				this.setTitle(rb.getString("dialog.title"));
			}
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	public FrmRejectMessage(ViewComponentValue dao) {
		this();
		lblExplain.setText(Messages.getString("panel.frmRejectMessage.prosa", dao.getDisplayLinkName()));

		int width = 360;
		int height = 240;
		int midHeight = UIConstants.getMainFrame().getY() + (UIConstants.getMainFrame().getHeight() / 2);
		int midWidth = UIConstants.getMainFrame().getX() + (UIConstants.getMainFrame().getWidth() / 2);
		this.setSize(width, height);
		this.setLocation(midWidth - width / 2, midHeight - height / 2);
		this.setIconImage(UIConstants.CMS.getImage());
		this.setResizable(false);
		this.setVisible(true);
	}

	private void jbInit() throws Exception {
		this.setLocale(java.util.Locale.getDefault());
		this.getContentPane().setLayout(new GridBagLayout());
		panTop.setBorder(new TitledBorder(""));
		panTop.setLayout(new BorderLayout());
		txtArea.setText("");
		txtArea.setLineWrap(true);
		btnReject.setText("Zurückweisen");
		btnReject.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnRejectActionPerformed(e);
			}
		});
		btnCancel.setText("Abbrechen");
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCancelActionPerformed(e);
			}
		});
		lblExplain.setToolTipText("");
		lblExplain.setText("<html>Sie haben sich entschieden, die Seite *** zurückzuweisen.<br>Bitte " + "geben Sie jetzt noch einen Grund an, damit der Autor diese Seite " + "erneut bearbeiten kann.</html>");
		lblExplain.setVerticalAlignment(SwingConstants.TOP);
		spTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		spTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.getContentPane().add(panTop, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 0, 5), 0, 0));
		this.getContentPane().add(spTextArea, new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 0, 5), 0, 0));
		this.getContentPane().add(btnReject, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
		this.getContentPane().add(btnCancel, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		spTextArea.getViewport().add(txtArea, null);
		panTop.add(lblExplain, BorderLayout.CENTER);
	}

	void btnRejectActionPerformed(ActionEvent e) {
		if (!txtArea.getText().equals("")) {
			setVisible(false);
			pid = 2;
		} else {
			JOptionPane.showMessageDialog(this, rb.getString("panel.frmRejectMessage.enterMessageWarning"), rb.getString("dialog.title"), JOptionPane.WARNING_MESSAGE);
		}
	}

	void btnCancelActionPerformed(ActionEvent e) {
		setVisible(false);
		pid = 1;
	}

	public int getPressedButton() {
		return pid;
	}

	public String getMessage() {
		return this.txtArea.getText();
	}
}