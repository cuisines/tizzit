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
package de.juwimm.cms.content.frame;

import static de.juwimm.cms.common.Constants.*;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import de.juwimm.cms.util.UIConstants;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class FrmValidationError extends JDialog {
	private static Logger log = Logger.getLogger(FrmValidationError.class);
	private JPanel jPanel1 = new JPanel();
	private TitledBorder titledBorder1;
	private BorderLayout borderLayout1 = new BorderLayout();
	private JLabel jLabel1 = new JLabel();
	private Border border1;
	private JScrollPane jScrollPane1 = new JScrollPane();
	private JButton btnClose = new JButton();
	private JEditorPane jEditorPane1 = new JEditorPane();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	public FrmValidationError() {
		super(new JFrame());
		try {
			jbInit();
			jEditorPane1.setDoubleBuffered(true);
			jEditorPane1.setContentType("text/html");
			jEditorPane1.setEditable(false);
			jEditorPane1.repaint();
			this.setModal(true);
	
			if (rb != null) {
				this.btnClose.setText(rb.getString("dialog.close"));
				this.jLabel1.setText(rb.getString("content.contentSingleton.validationErrorOccuredPrepend"));
			}
			this.getRootPane().setDefaultButton(btnClose);
		} catch (Exception exe) {
			log.error("Initialization error", exe);
		}
	}

	public FrmValidationError(String errorMessage) {
		this();
		int width = 450;
		int height = 440;
		int midHeight = UIConstants.getMainFrame().getY() + (UIConstants.getMainFrame().getHeight() / 2);
		int midWidth = UIConstants.getMainFrame().getX() + (UIConstants.getMainFrame().getWidth() / 2);
		this.setSize(width, height);
		this.setLocation(midWidth - width / 2, midHeight - height / 2);
		((JFrame) this.getParent()).setIconImage(UIConstants.CMS.getImage());
		jEditorPane1.setText("<html><font face=\"Arial, Helvetica, sans-serif\">" + errorMessage + "</font></html>");
		this.setVisible(true);
		this.setTitle(rb.getString("dialog.title"));
	}

	private void jbInit() throws Exception {
		titledBorder1 = new TitledBorder("");
		border1 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		this.getContentPane().setLayout(gridBagLayout1);
		jPanel1.setBorder(BorderFactory.createEtchedBorder());
		jPanel1.setLayout(borderLayout1);
		jLabel1.setBorder(border1);
		jLabel1.setHorizontalAlignment(SwingConstants.LEADING);
		jLabel1.setVerticalAlignment(SwingConstants.TOP);
		jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		btnClose.setContentAreaFilled(true);
		btnClose.setMnemonic('0');
		btnClose.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCloseActionPerformed(e);
			}
		});

		this.getContentPane().add(
				jPanel1,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(5, 5, 0, 5), 0, 0));
		jPanel1.add(jLabel1, BorderLayout.CENTER);
		this.getContentPane().add(
				jScrollPane1,
				new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(5, 5, 0, 5), 0, 0));
		jScrollPane1.getViewport().add(jEditorPane1, null);
		this.getContentPane().add(
				btnClose,
				new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 0), 0, 0));
	}

	void btnCloseActionPerformed(ActionEvent e) {
		this.setVisible(false);
		dispose();
	}
}