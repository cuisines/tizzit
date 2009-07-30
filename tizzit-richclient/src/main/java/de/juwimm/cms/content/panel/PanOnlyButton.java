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
package de.juwimm.cms.content.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import de.juwimm.cms.Messages;
import de.juwimm.cms.content.event.EditpaneFiredEvent;
import de.juwimm.cms.content.event.EditpaneFiredListener;
import de.juwimm.cms.content.modules.Module;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanOnlyButton extends JPanel implements EditpaneFiredListener {
	private static Logger log = Logger.getLogger(PanOnlyButton.class);
	private JButton btnOpen = new JButton();
	private JButton btnDeleteConnection = new JButton();
	private Module module;

	protected PanOnlyButton() {
		try {
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization problem", exe);
		}
	}

	public PanOnlyButton(Module module, boolean showDeleteButton) {
		this();
		this.module = module;
		this.module.addEditpaneFiredListener(this);
		btnOpen.setText(Messages.getString("panel.panOnlyButton.openButton", module.getLabel()));
		if (showDeleteButton) {
			btnDeleteConnection.setText(Messages.getString("panel.panOnlyButton.deleteConnection", module.getLabel()));
			btnDeleteConnection.setVisible(true);
		} else {
			btnDeleteConnection.setVisible(false);
		}
	}

	protected void jbInit() throws Exception {
		btnOpen.setText("open Module");
		btnOpen.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnOpenActionPerformed(e);
			}
		});
		this.setLayout(new GridBagLayout());
		btnDeleteConnection.setText("Delete Connection");
		btnDeleteConnection.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDeleteConnectionActionPerformed(e);
			}
		});
		this.add(btnOpen, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 10, 2, 0), 0, 0));
		this.add(btnDeleteConnection, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
	}

	void btnOpenActionPerformed(ActionEvent e) {
		btnOpen.setEnabled(false);
		btnDeleteConnection.setEnabled(false);
		this.module.viewModalUI(true);
	}

	public void editpaneFiredPerformed(EditpaneFiredEvent efe) {
		btnOpen.setEnabled(true);
		btnDeleteConnection.setEnabled(true);
	}

	public void editpaneCancelPerformed(EditpaneFiredEvent efe) {
		btnOpen.setEnabled(true);
		btnDeleteConnection.setEnabled(true);
	}

	public void setEnabled(boolean enabling) {
		btnOpen.setEnabled(enabling);
		btnDeleteConnection.setEnabled(enabling);
	}

	void btnDeleteConnectionActionPerformed(ActionEvent e) {
		this.module.setProperties(null);
	}

	public void addDeleteSettingsActionListener(ActionListener al) {
		if (this.btnDeleteConnection != null)
			this.btnDeleteConnection.addActionListener(al);
	}

}