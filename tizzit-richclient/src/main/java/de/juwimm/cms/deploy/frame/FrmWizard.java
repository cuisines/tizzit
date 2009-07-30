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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

import de.juwimm.cms.deploy.panel.wizard.PanAuthorStart;
import de.juwimm.cms.deploy.panel.wizard.WizardPanel;
import de.juwimm.cms.util.UIConstants;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class FrmWizard extends JFrame implements Wizard {
	private static Logger log = Logger.getLogger(FrmWizard.class);
	private JPanel panButtons = new JPanel();
	private JPanel panDesc = new JPanel();
	private JLabel lblIcon = new JLabel();
	private JLabel lblWizardTitle = new JLabel();
	private JLabel lblWizardDescription = new JLabel();
	private JButton btnNext = new JButton();
	private JButton btnCancel = new JButton();
	private JButton btnBack = new JButton();
	private JPanel panLineSeparator = new JPanel();
	private PanAuthorStart pas = new PanAuthorStart();
	private JPanel pan = null;
	private EventListenerList listenerList = new EventListenerList();
	public static final int ACTION_WIZARD_NEXT = 1;
	public static final int ACTION_WIZARD_PREVIOUS = 2;
	public static final int ACTION_WIZARD_CANCEL = 3;

	public FrmWizard(ActionListener wizardActionListener, Icon iconFiftyThree, String strWizardTitle, String strWizardDescription, JPanel firstPanel) {
		this();
		addWizardListener(wizardActionListener);
		this.lblWizardTitle.setText(strWizardTitle);
		this.setPanel(firstPanel, strWizardDescription);
		//		this.deployAllow(firstPanel);
		this.lblIcon.setIcon(iconFiftyThree);
		this.setBackEnabled(false);
	}

	public FrmWizard() {
		try {
			jbInit();
			this.btnBack.setText(rb.getString("frame.wizard.back"));
			this.btnCancel.setText(rb.getString("frame.wizard.cancel"));
			this.btnNext.setText(rb.getString("frame.wizard.next"));
			this.setTitle(rb.getString("frame.wizard.title"));
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	public void showWizard() {
		int width = 520;
		int height = 390;
		int midHeight = UIConstants.getMainFrame().getY() + (UIConstants.getMainFrame().getHeight() / 2);
		int midWidth = UIConstants.getMainFrame().getX() + (UIConstants.getMainFrame().getWidth() / 2);
		this.setSize(width, height);
		this.setLocation(midWidth - width / 2, midHeight - height / 2);
		this.setIconImage(UIConstants.WIZARD_ICON_INSTALL.getImage());
		this.setResizable(false);
		this.setVisible(true);
	}

	private void jbInit() throws Exception {
		this.getContentPane().setLayout(new BorderLayout());
		panButtons.setDebugGraphicsOptions(0);
		panButtons.setPreferredSize(new Dimension(45, 45));
		panButtons.setLayout(new GridBagLayout());
		panDesc.setBackground(Color.white);
		panDesc.setMinimumSize(new Dimension(60, 60));
		panDesc.setPreferredSize(new Dimension(60, 60));
		panDesc.setLayout(new GridBagLayout());
		lblIcon.setBackground(Color.lightGray);
		lblIcon.setBorder(null);
		lblIcon.setDebugGraphicsOptions(0);
		lblIcon.setMaximumSize(new Dimension(53, 53));
		lblIcon.setMinimumSize(new Dimension(53, 53));
		lblIcon.setPreferredSize(new Dimension(53, 53));
		lblIcon.setIcon(null);
		lblWizardTitle.setFont(new Font("Dialog", Font.BOLD, 11));
		lblWizardTitle.setText("Author Deployment Wizard");
		lblWizardDescription.setHorizontalAlignment(SwingConstants.LEADING);
		lblWizardDescription.setHorizontalTextPosition(SwingConstants.LEADING);
		lblWizardDescription.setText("<html>With this Wizard you can deploy, revoke and<br>delete pages " + "or send messages to your editor.</html>");
		lblWizardDescription.setVerticalAlignment(SwingConstants.TOP);
		lblWizardDescription.setVerticalTextPosition(SwingConstants.TOP);
		btnNext.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnNext.setText(" Next > ");
		btnNext.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNextActionPerformed(e);
			}
		});
		btnCancel.setText(" Cancel ");
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCancelActionPerformed(e);
			}
		});
		btnBack.setPreferredSize(new Dimension(80, 25));
		btnBack.setText(" < Back ");
		btnBack.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnBackActionPerformed(e);
			}
		});
		panLineSeparator.setBorder(BorderFactory.createEtchedBorder());
		panLineSeparator.setMaximumSize(new Dimension(32767, 2));
		panLineSeparator.setMinimumSize(new Dimension(1, 2));
		panLineSeparator.setPreferredSize(new Dimension(540, 2));
		this.setTitle("CMS Deployment Wizard");
		panButtons.add(panLineSeparator, new GridBagConstraints(0, 0, 3, GridBagConstraints.REMAINDER, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
		panButtons.add(btnBack, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.VERTICAL, new Insets(10, 0, 5, 0), 0, 0));
		panButtons.add(btnNext, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.VERTICAL, new Insets(10, 5, 5, 0), 0, 0));
		panButtons.add(btnCancel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(10, 5, 5, 5), 0, 0));
		this.getContentPane().add(panDesc, BorderLayout.NORTH);
		panDesc.add(lblIcon, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
		panDesc.add(lblWizardTitle, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3, 5, 0, 10), 0, 4));
		panDesc.add(lblWizardDescription, new GridBagConstraints(1, 1, 1, 1, 0.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(1, 5, 0, 10), 0, 0));
		this.getContentPane().add(panButtons, BorderLayout.SOUTH);
		this.getContentPane().add(pas, BorderLayout.CENTER);
	}

	void btnBackActionPerformed(ActionEvent e) {
		ActionEvent ae = new ActionEvent(this, FrmWizard.ACTION_WIZARD_PREVIOUS, "WHATZZUP");
		runWizardFiredEvent(ae);
	}

	void btnNextActionPerformed(ActionEvent e) {
		ActionEvent ae = new ActionEvent(this, FrmWizard.ACTION_WIZARD_NEXT, "WHATZZUP");
		runWizardFiredEvent(ae);
	}

	void btnCancelActionPerformed(ActionEvent e) {
		ActionEvent ae = new ActionEvent(this, FrmWizard.ACTION_WIZARD_CANCEL, "WHATZZUP");
		runWizardFiredEvent(ae);
	}

	public final void addWizardListener(ActionListener al) {
		this.listenerList.add(ActionListener.class, al);
	}

	public final void removeWizardListener(ActionListener al) {
		this.listenerList.remove(ActionListener.class, al);
	}

	public final void runWizardFiredEvent(ActionEvent e) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			((ActionListener) listeners[i + 1]).actionPerformed(e);
		}
	}

	public void setPanel(JPanel pan, String strWizardDescription) {
		this.lblWizardDescription.setText(strWizardDescription);
		if (this.pan != null) {
			this.getContentPane().remove(this.pan);
		}
		this.pan = pan;
		this.getContentPane().add(pan, BorderLayout.CENTER);
		WizardPanel wp = (WizardPanel) pan;
		wp.setWizard(this);
		this.getContentPane().repaint();
		this.getContentPane().validate();
	}

	public JPanel getPanel() {
		return this.pan;
	}

	public void setBackEnabled(boolean back) {
		this.btnBack.setEnabled(back);
	}

	public void setNextEnabled(boolean next) {
		this.btnNext.setEnabled(next);
	}

	public void setNextAsFinally(boolean fin) {
		if (fin) {
			this.btnNext.setText(rb.getString("frame.wizard.finally"));
			btnNext.setFont(new Font("Dialog", Font.BOLD, 11));
		} else {
			this.btnNext.setText(rb.getString("frame.wizard.next"));
			btnNext.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
	}

	public void setCancelEnabled(boolean cancel) {
		this.btnCancel.setEnabled(cancel);
	}
}
