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
package de.juwimm.cms.gui.views.safeguard;

import static de.juwimm.cms.common.Constants.rb;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class PanelSelectAuthorizationRole4Realm extends JPanel {
	private static final long serialVersionUID = -557342000323844250L;
	private JLabel lblRequiredRole = null;
	private JTextField txtRequiredRole = null;

	/**
	 * This is the default constructor
	 */
	public PanelSelectAuthorizationRole4Realm() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.weightx = 6.0;
		gridBagConstraints1.gridwidth = 2;
		gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 0);
		gridBagConstraints1.gridx = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridy = 0;
		lblRequiredRole = new JLabel();
		lblRequiredRole.setPreferredSize(new Dimension(90, 22));
		lblRequiredRole.setMinimumSize(new Dimension(90, 22));
		lblRequiredRole.setText(rb.getString("panel.panelSafeguard.requiredRole"));
		this.setLayout(new GridBagLayout());
		this.add(lblRequiredRole, gridBagConstraints);
		this.add(getTxtRequiredRole(), gridBagConstraints1);
	}

	/**
	 * This method initializes txtRequiredRole	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtRequiredRole() {
		if (txtRequiredRole == null) {
			txtRequiredRole = new JTextField();
			txtRequiredRole.setToolTipText(rb.getString("panel.panelSafeguard.requiredRole.tt"));
		}
		return txtRequiredRole;
	}

	public String getRequiredRole() {
		return this.getTxtRequiredRole().getText();
	}

	public void setRequiredRole(String requiredRole) {
		this.getTxtRequiredRole().setText(requiredRole);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		this.getTxtRequiredRole().setEnabled(enabled);
	}

}
