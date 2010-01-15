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

import static de.juwimm.cms.common.Constants.rb;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import de.juwimm.cms.safeguard.vo.RealmJdbcValue;
import de.juwimm.swing.DropDownHolder;
import de.juwimm.swing.SortingListModel;

/**
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public final class PanSqlDbRealm extends AbstractSafeguardPanel {
	private static final long serialVersionUID = -4595155805594463415L;
	private static Logger log = Logger.getLogger(PanSqlDbRealm.class);
	private JPanel panData = null;
	private JLabel lblRealmId = null;
	private JLabel lblRealmIdDisplay = null;
	private JLabel lblRealmName = null;
	private JTextField txtRealmName = null;
	private JLabel lblJndiName = null;
	private JTextField txtJndiName = null;
	private JLabel lblSqlUser = null;
	private JTextField txtSqlUser = null;
	private JLabel lblSqlRole = null;
	private JTextField txtSqlRole = null;
	private PanChooseLoginPage panChooseLoginPage = null;
	private final Color backgroundTextFieldError = new Color(0xed4044);

	public PanSqlDbRealm() {
		super();
		this.init();
		this.setActive(false);
	}

	/**
	 * This method initializes panData	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanData() {
		if (panData == null) {
			this.panChooseLoginPage = new PanChooseLoginPage();
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 3;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints11.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints11.gridy = 5;
			gridBagConstraints11.gridwidth = 3;
			gridBagConstraints11.weightx = 1.0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = java.awt.GridBagConstraints.NONE;
			gridBagConstraints4.gridy = 2;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints4.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints4.gridx = 4;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 3;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints3.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints3.gridy = 2;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = java.awt.GridBagConstraints.NONE;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints2.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints2.gridx = 4;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 3;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.fill = java.awt.GridBagConstraints.NONE;
			gridBagConstraints20.gridy = 0;
			gridBagConstraints20.weightx = 1.0;
			gridBagConstraints20.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints20.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints20.gridx = 4;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 3;
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints10.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints10.gridy = 0;

			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = java.awt.GridBagConstraints.NONE;
			gridBagConstraints6.gridy = 3;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints6.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints6.gridx = 4;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 3;
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints5.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints5.gridy = 3;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = java.awt.GridBagConstraints.NONE;
			gridBagConstraints8.gridy = 4;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints8.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints8.gridx = 4;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 3;
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints7.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints7.gridy = 4;
			lblRealmId = new JLabel(rb.getString("panel.admin.tab.safeguard.panel.realmId"));
			lblRealmIdDisplay = new JLabel("");
			lblRealmName = new JLabel(rb.getString("panel.admin.tab.safeguard.panel.realmName"));
			lblJndiName = new JLabel(rb.getString("panel.admin.tab.safeguard.panel.jdbc.jndiName"));
			lblSqlUser = new JLabel(rb.getString("panel.admin.tab.safeguard.panel.jdbc.statementUser"));
			lblSqlRole = new JLabel(rb.getString("panel.admin.tab.safeguard.panel.jdbc.statementRoles"));
			panData = new JPanel();
			panData.setLayout(new GridBagLayout());
			panData.add(lblRealmId, gridBagConstraints10);
			panData.add(lblRealmIdDisplay, gridBagConstraints20);
			panData.add(lblRealmName, gridBagConstraints1);
			panData.add(getTxtRealmName(), gridBagConstraints2);
			panData.add(lblJndiName, gridBagConstraints3);
			panData.add(getTxtJndiName(), gridBagConstraints4);
			panData.add(lblSqlUser, gridBagConstraints5);
			panData.add(getTxtSqlUser(), gridBagConstraints6);
			panData.add(lblSqlRole, gridBagConstraints7);
			panData.add(getTxtSqlRole(), gridBagConstraints8);
			panData.add(panChooseLoginPage, gridBagConstraints11);
		}
		return panData;
	}

	private void init() {
		this.add(getPanData(), this.getTopGridBagConstraints());
	}

	@Override
	protected void deleteRealm() {
		if (!this.lstRealms.isSelectionEmpty()) {
			RealmJdbcValue currentRealm = (RealmJdbcValue) ((DropDownHolder) this.lstRealms.getSelectedValue()).getObject();
			comm.deleteSqlDbRealm(currentRealm.getJdbcRealmId());
			((SortingListModel) this.lstRealms.getModel()).removeElementAt(this.lstRealms.getSelectedIndex());
		}
		this.getTxtRealmName().setText("");
		this.getTxtJndiName().setText("");
		this.getTxtSqlUser().setText("");
		this.getTxtSqlRole().setText("");
		this.panChooseLoginPage.setLoginpage(null);
		this.panChooseLoginPage.setLoginPageViewComponentId(null);
		this.lblRealmIdDisplay.setText("");
		this.lstRealms.clearSelection();
		this.setActive(false);
	}

	@Override
	protected void addRealm() {
		this.getTxtRealmName().setText("[new Realm]");
		this.getTxtJndiName().setText("");
		this.getTxtSqlUser().setText("");
		this.getTxtSqlRole().setText("");
		this.panChooseLoginPage.setLoginpage(null);
		this.panChooseLoginPage.setLoginPageViewComponentId(null);
		this.lblRealmIdDisplay.setText("");
		this.lstRealms.clearSelection();
		this.setActive(true);
	}

	@Override
	protected void saveRealm() {
		RealmJdbcValue value = new RealmJdbcValue();
		if (this.lblRealmIdDisplay.getText().equalsIgnoreCase("")) {
			value.setJdbcRealmId(new Integer(-1));
		} else {
			value.setJdbcRealmId(Integer.valueOf(this.lblRealmIdDisplay.getText()));
		}
		value.setRealmName(this.getTxtRealmName().getText());
		value.setJndiName(this.getTxtJndiName().getText());
		value.setStatementUser(this.getTxtSqlUser().getText());
		value.setStatementRolePerUser(this.getTxtSqlRole().getText());
		value.setLoginPageId(this.panChooseLoginPage.getLoginPageViewComponentId());
		if (validateSaveJdbc(value)) {
			if (value.getJdbcRealmId().intValue() == -1) {
				value.setJdbcRealmId(comm.addSqlDbRealmToSite(Integer.valueOf(comm.getSiteId()), value));
				((SortingListModel) this.lstRealms.getModel()).addElement(new DropDownHolder(value, value.getRealmName()));
			} else {
				comm.editSqlDbRealm(value);
				((SortingListModel) this.lstRealms.getModel()).removeElementAt(this.lstRealms.getSelectedIndex());
				((SortingListModel) this.lstRealms.getModel()).addElement(new DropDownHolder(value, value.getRealmName()));
			}
		}
	}

	private boolean validateSaveJdbc(RealmJdbcValue jdbcValue) {
		boolean isValid = true;
		FocusListener focusListener = new FocusListener() {
			public void focusGained(FocusEvent e) {
				if (e.getSource() instanceof JTextField) {
					JTextField source = (JTextField) e.getSource();
					resetInputHighlight(source);
				}

			}

			public void focusLost(FocusEvent e) {
			}

		};

		if (jdbcValue.getRealmName() == null || jdbcValue.getRealmName().isEmpty()) {
			txtRealmName.setBackground(backgroundTextFieldError);
			txtRealmName.addFocusListener(focusListener);
			isValid = false;
		}

		return isValid;
	}

	private void resetInputHighlight(JTextField source) {
		if (source.getBackground().equals(backgroundTextFieldError)) {
			source.setBackground(Color.WHITE);
		}
	}

	private void resetInputsHighlight() {
		resetInputHighlight(txtRealmName);
	}

	@Override
	protected void listSelected() {
		if (lstRealms.getSelectedIndex() != -1) {
			RealmJdbcValue val = (RealmJdbcValue) ((DropDownHolder) lstRealms.getSelectedValue()).getObject();
			getTxtRealmName().setText(val.getRealmName());
			getTxtJndiName().setText(val.getJndiName());
			getTxtSqlUser().setText(val.getStatementUser());
			getTxtSqlRole().setText(val.getStatementRolePerUser());
			this.lblRealmIdDisplay.setText(val.getJdbcRealmId().toString());
			this.panChooseLoginPage.setLoginpage(val.getLoginPageId());
			this.setActive(true);
			resetInputsHighlight();
		}
	}

	@Override
	public void fillRealmList() {
		try {
			RealmJdbcValue[] val = comm.getSqlDbRealmsForSite(Integer.valueOf(comm.getSiteId()));
			SortingListModel listmodel = new SortingListModel();
			if (val != null) {
				for (int i = 0; i < val.length; i++) {
					listmodel.addElement(new DropDownHolder(val[i], val[i].getRealmName()));
				}
			}
			this.lstRealms.setModel(listmodel);
			this.getTxtRealmName().setText("");
			this.getTxtJndiName().setText("");
			this.getTxtSqlUser().setText("");
			this.getTxtSqlRole().setText("");
			this.panChooseLoginPage.setLoginpage(null);
			this.panChooseLoginPage.setLoginPageViewComponentId(null);
			this.lblRealmIdDisplay.setText("");
			this.lstRealms.clearSelection();
			this.setActive(false);
		} catch (Exception ex) {
			log.warn("Error filling list of all JdbcRealms: " + ex.getMessage());
		}
	}

	/**
	 * This method initializes txtRealmName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtRealmName() {
		if (txtRealmName == null) {
			txtRealmName = new JTextField();
			txtRealmName.setPreferredSize(new java.awt.Dimension(200, 20));
		}
		return txtRealmName;
	}

	/**
	 * This method initializes txtJndiName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtJndiName() {
		if (txtJndiName == null) {
			txtJndiName = new JTextField();
			txtJndiName.setPreferredSize(new java.awt.Dimension(200, 20));
		}
		return txtJndiName;
	}

	/**
	 * This method initializes txtSqlUser	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtSqlUser() {
		if (txtSqlUser == null) {
			txtSqlUser = new JTextField();
			txtSqlUser.setPreferredSize(new java.awt.Dimension(200, 20));
		}
		return txtSqlUser;
	}

	/**
	 * This method initializes txtSqlRole	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtSqlRole() {
		if (txtSqlRole == null) {
			txtSqlRole = new JTextField();
			txtSqlRole.setPreferredSize(new java.awt.Dimension(200, 20));
		}
		return txtSqlRole;
	}

	private void setActive(boolean enabled) {
		this.panChooseLoginPage.setEnabled(enabled);
		this.getTxtJndiName().setEnabled(enabled);
		this.getTxtRealmName().setEnabled(enabled);
		this.getTxtSqlRole().setEnabled(enabled);
		this.getTxtSqlUser().setEnabled(enabled);
		super.getBtnSaveRealm().setEnabled(enabled);
		super.getBtnDeleteRealm().setEnabled(enabled);
	}

} //  @jve:decl-index=0:visual-constraint="126,67"
