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

import static de.juwimm.cms.common.Constants.*;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.*;

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
public class FrmProgressDialog extends JFrame {
	private static Logger log = Logger.getLogger(FrmProgressDialog.class);
	private JProgressBar jProgressBar1 = new JProgressBar();
	private JLabel lblTask = new JLabel();
	private JButton btnCancel = new JButton();
	private JButton btnClose = new JButton();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JTextArea txtDetail = new JTextArea();

	public FrmProgressDialog(String taskName, String startingDetail, int maxProgress) {
		try {
			jbInit();
			if (rb != null) {
				btnCancel.setText(rb.getString("dialog.cancel"));
				btnClose.setText(rb.getString("dialog.close"));
				this.setTitle(rb.getString("dialog.title"));
			}
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
		this.lblTask.setText(taskName);
		this.jProgressBar1.setValue(0);
		this.jProgressBar1.setMaximum(maxProgress);
		this.txtDetail.setText(startingDetail);

		int width = 450;
		int height = 220;
		int midHeight = UIConstants.getMainFrame().getY() + (UIConstants.getMainFrame().getHeight() / 2);
		int midWidth = UIConstants.getMainFrame().getX() + (UIConstants.getMainFrame().getWidth() / 2);
		this.setSize(width, height);
		this.setIconImage(UIConstants.CMS.getImage());
		this.setLocation(midWidth - width / 2, midHeight - height / 2);
		this.setResizable(false);
		this.btnCancel.setVisible(false);
		this.setVisible(true);
	}

	private void jbInit() throws Exception {
		this.getContentPane().setLayout(gridBagLayout1);
		jProgressBar1.setValue(50);
		lblTask.setFont(new java.awt.Font("Dialog", 1, 11));
		lblTask.setText("Deploying all Units");
		btnCancel.setText("Cancel");
		btnClose.setEnabled(false);
		btnClose.setText("Close");
		btnClose.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCloseActionPerformed(e);
			}
		});
		txtDetail.setBackground(UIManager.getColor("Label.background"));
		txtDetail.setFont(new java.awt.Font("SansSerif", 0, 12));
		txtDetail.setForeground(Color.black);
		txtDetail.setBorder(BorderFactory.createLoweredBevelBorder());
		txtDetail.setDoubleBuffered(true);
		txtDetail.setEditable(false);
		txtDetail.setLineWrap(true);
		txtDetail.setWrapStyleWord(true);
		this.getContentPane().add(
				lblTask,
				new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
						new Insets(10, 10, 0, 10), 313, 6));
		this.getContentPane().add(
				jProgressBar1,
				new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 10, 0, 10), 257, 5));
		this.getContentPane().add(
				btnCancel,
				new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
						new Insets(15, 10, 10, 0), 0, 0));
		this.getContentPane().add(
				btnClose,
				new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
						new Insets(15, 0, 10, 10), 0, 0));
		this.getContentPane().add(
				txtDetail,
				new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 10, 10, 10), 0, 0));
	}

	public void setProgress(String strDetail, int progressValue) {
		this.txtDetail.setText(strDetail);
		this.jProgressBar1.setValue(progressValue);
		this.repaint();
		this.validate();
		this.getContentPane().repaint();
		this.getContentPane().validate();
		this.jProgressBar1.revalidate();
		this.txtDetail.revalidate();
	}

	public void setProgress(String strDetail) {
		setProgress(strDetail, this.jProgressBar1.getValue() + 1);
	}

	public void addToMaximum(int maxval) {
		this.jProgressBar1.setMaximum(this.jProgressBar1.getMaximum() + maxval);
	}

	public void setProgress(String strDetail, int maxval, int progressValue) {
		this.jProgressBar1.setMaximum(maxval);
		setProgress(strDetail, progressValue);
	}

	public int getValue() {
		return this.jProgressBar1.getValue();
	}

	public int getMaximum() {
		return this.jProgressBar1.getMaximum();
	}

	public void enableClose(boolean val) {
		this.btnClose.setEnabled(val);
	}

	void btnCloseActionPerformed(ActionEvent e) {
		setVisible(false);
		dispose();
	}
}