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
package de.juwimm.cms.gui.admin.safeguard;

import static de.juwimm.cms.client.beans.Application.*;
import static de.juwimm.cms.common.Constants.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.swing.NoResizeScrollPane;
import de.juwimm.swing.SortingListModel;

/**
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public abstract class AbstractSafeguardPanel extends JPanel {
	protected Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	protected JList lstRealms = null;
	protected JPanel panActions = null;
	protected JButton btnAddRealm = null;
	protected JButton btnDeleteRealm = null;
	protected JButton btnSaveRealm = null;

	/**
	 * This is the default constructor
	 */
	public AbstractSafeguardPanel() {
		super();
		initialize();
	}

	protected abstract void deleteRealm();

	protected abstract void addRealm();

	protected abstract void saveRealm();

	/**
	 * wird aufgerufen, wenn die Liste selektiert wird.
	 *
	 */
	protected abstract void listSelected();

	public abstract void fillRealmList();

	protected final GridBagConstraints getTopGridBagConstraints() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gbc.insets = new java.awt.Insets(5, 5, 5, 5);
		gbc.weightx = 1.0D;
		gbc.gridy = 0;

		return gbc;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	protected final void initialize() {
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.insets = new java.awt.Insets(25, 5, 5, 5);
		gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints1.gridy = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		gridBagConstraints.gridheight = 2;
		gridBagConstraints.gridx = 0;
		this.setLayout(new GridBagLayout());
		this.setSize(396, 259);
		this.add(new NoResizeScrollPane(getLstRealms()), gridBagConstraints);
		this.add(getPanActions(), gridBagConstraints1);
	}

	/**
	 * This method initializes lstRealms	
	 * 	
	 * @return javax.swing.JList	
	 */
	protected final JList getLstRealms() {
		if (lstRealms == null) {
			lstRealms = new JList();
			lstRealms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			lstRealms.setModel(new SortingListModel());
			lstRealms.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
				public void valueChanged(javax.swing.event.ListSelectionEvent e) {
					listSelected();
				}
			});
		}
		return lstRealms;
	}

	/**
	 * This method initializes panActions	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	protected final JPanel getPanActions() {
		if (panActions == null) {
			panActions = new JPanel();
			panActions.add(getBtnAddRealm());
			panActions.add(getBtnSaveRealm());
			panActions.add(getBtnDeleteRealm());
		}
		return panActions;
	}

	/**
	 * This method initializes btnAddRealm	
	 * 	
	 * @return javax.swing.JButton	
	 */
	protected final JButton getBtnAddRealm() {
		if (btnAddRealm == null) {
			btnAddRealm = new JButton();
			btnAddRealm.setText(rb.getString("panel.admin.tab.safeguard.panel.add"));
			btnAddRealm.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					addRealm();
				}
			});
		}
		return btnAddRealm;
	}

	/**
	 * This method initializes btnDeleteRealm	
	 * 	
	 * @return javax.swing.JButton	
	 */
	protected final JButton getBtnDeleteRealm() {
		if (btnDeleteRealm == null) {
			btnDeleteRealm = new JButton();
			btnDeleteRealm.setText(rb.getString("panel.admin.tab.safeguard.panel.delete"));
			btnDeleteRealm.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int ret = JOptionPane.showConfirmDialog(UIConstants.getMainFrame(), rb.getString("panel.panelSafeguard.realm.reallydelete"), rb.getString("panel.panelSafeguard.realm.reallydelete"), JOptionPane.YES_NO_OPTION);
					if (ret == JOptionPane.OK_OPTION) {
						deleteRealm();
					}
				}
			});
		}
		return btnDeleteRealm;
	}

	/**
	 * This method initializes btnSaveRealm	
	 * 	
	 * @return javax.swing.JButton	
	 */
	protected final JButton getBtnSaveRealm() {
		if (btnSaveRealm == null) {
			btnSaveRealm = new JButton();
			btnSaveRealm.setText(rb.getString("panel.admin.tab.safeguard.panel.save"));
			btnSaveRealm.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					saveRealm();
				}
			});
		}
		return btnSaveRealm;
	}

} //  @jve:decl-index=0:visual-constraint="10,10"
