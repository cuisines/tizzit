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
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import de.juwimm.cms.util.UIConstants;


/**
 * Modal dialogbox with two buttons
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * @version $Id$
 */
public class DlgModal extends JDialog {
	private static Logger log = Logger.getLogger(DlgModal.class);
	private BorderLayout borderLayout1 = new BorderLayout();
	private JPanel panButtons = new JPanel();
	private JButton btnOk = new JButton();
	private JPanel rootPanel;

	public DlgModal(boolean modal) {
		super(UIConstants.getMainFrame(), modal);
		try {
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization error", exe);
		}
	}

	public DlgModal(JPanel rootPanel, int height, int width, String title) {
		this(true);
		this.rootPanel = rootPanel;
		this.getContentPane().add(this.rootPanel, BorderLayout.CENTER);
		this.setSize(width, height);
		this.setLocationRelativeTo(UIConstants.getMainFrame());
		this.setTitle(title);
		this.getRootPane().setDefaultButton(btnOk);
	}

	private void jbInit() throws Exception {
		this.getContentPane().setLayout(borderLayout1);
		btnOk.setText(rb.getString("dialog.ok"));
		panButtons.setLayout(new GridBagLayout());
		this.getContentPane().add(panButtons, BorderLayout.SOUTH);
		panButtons.add(btnOk, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
	}

	public void addOkListener(ActionListener okListener) {
		btnOk.addActionListener(okListener);
	}

}