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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import de.juwimm.cms.Messages;
import de.juwimm.cms.content.event.EditpaneFiredEvent;
import de.juwimm.cms.content.modules.Module;
import de.juwimm.cms.util.UIConstants;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class DlgModalModule extends JDialog {
	private static Logger log = Logger.getLogger(DlgModalModule.class);
	private final JPanel panButtons = new JPanel();
	private final JButton btnOk = new JButton();
	private final JButton btnCancel = new JButton();
	private Module module;
	private JPanel rootPanel;

	public DlgModalModule(boolean modal) {
		super(UIConstants.getMainFrame(), modal);
		try {
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization error", exe);
		}
	}

	public DlgModalModule(Module module, JPanel rootPanel, int height, int width, boolean modal) {
		this(modal);
		this.module = module;
		this.rootPanel = rootPanel;
		this.getContentPane().add(this.rootPanel, BorderLayout.CENTER);

		int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		this.setSize(width, height);
		this.setMinimumSize(new Dimension(440, 440));
		this.setPreferredSize(new Dimension(width, height));
		this.setLocation((screenWidth / 2) - (width / 2), (screenHeight / 2) - (height / 2));
		this.setTitle(Messages.getString("panel.content.DlgModalModule", module.getLabel(), module.getDescription()));
		this.getRootPane().setDefaultButton(btnOk);
	}

	public void setOkButtonText(String txt) {
		btnOk.setText(txt);
	}

	public void setOkButtonEnabled(boolean enabled) {
		btnOk.setEnabled(enabled);
	}

	private void jbInit() throws Exception {
		this.getContentPane().setLayout(new BorderLayout());
		btnOk.setText(Messages.getString("dialog.ok"));
		btnOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnOkActionPerformed(e);
			}
		});
		btnCancel.setText(Messages.getString("dialog.cancel"));
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCancelActionPerformed(e);
			}
		});
		panButtons.setLayout(new GridBagLayout());
		this.getContentPane().add(panButtons, BorderLayout.SOUTH);
		panButtons.add(btnOk, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
		panButtons.add(btnCancel, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 1, 0));
	}

	void btnOkActionPerformed(ActionEvent e) {
		if (this.module.isModuleValid()) {
			if (this.module.hasEditpaneFiredListener()) {
				EditpaneFiredEvent efe = new EditpaneFiredEvent(this.module);
				this.module.runEditpaneFiredEvent(efe);
			}
			this.module.setSaveable(true);
			this.setVisible(false);
			this.dispose();
		} else {
			Thread t = new Thread(new Runnable() {
				public void run() {
					JOptionPane.showMessageDialog(getContentPane(), module.getValidationError(), Messages.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
				}
			});
			t.setPriority(Thread.NORM_PRIORITY);
			t.start();
		}
	}

	void btnCancelActionPerformed(ActionEvent e) {
		EditpaneFiredEvent efe = new EditpaneFiredEvent(this.module);
		this.module.runEditpaneCancelEvent(efe);
		this.module.setSaveable(false);
		this.setVisible(false);
		this.dispose();
	}
}