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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import de.juwimm.cms.safeguard.vo.RealmLdapValue;
import de.juwimm.swing.DropDownHolder;
import de.juwimm.swing.SortingListModel;

/**
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public final class PanLDAPRealm extends AbstractSafeguardPanel {
	private static Logger log = Logger.getLogger(PanLDAPRealm.class);
	private JPanel panData = null;
	private JLabel lblRealmId = null;
	private JLabel lblRealmIdDisplay = null;
	private JLabel lblRealmName = null;
	private JTextField txtRealmName = null;
	private JLabel lblLdapUrl = null;
	private JLabel lblAuthenticationType = null;
	private JComboBox cboAuthenticationType = null;
	private JTextField txtLdapUrl = null;
	private JLabel lblLdapPrefix = null;
	private JTextField txtLdapPrefix = null;
	private JLabel lblLdapSuffix = null;
	private JTextField txtLdapSuffix = null;
	private PanChooseLoginPage panChooseLoginPage = null;
	private final Color backgroundTextFieldError = new Color(0xed4044);

	public PanLDAPRealm() {
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
			gridBagConstraints11.gridy = 6;
			gridBagConstraints11.gridwidth = 3;
			gridBagConstraints11.weightx = 1.0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = java.awt.GridBagConstraints.NONE;
			gridBagConstraints4.gridy = 3;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints4.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints4.gridx = 4;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 3;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints3.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints3.gridy = 3;
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
			gridBagConstraints6.gridy = 4;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints6.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints6.gridx = 4;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 3;
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints5.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints5.gridy = 4;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = java.awt.GridBagConstraints.NONE;
			gridBagConstraints8.gridy = 5;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints8.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints8.gridx = 4;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 3;
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints7.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints7.gridy = 5;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = java.awt.GridBagConstraints.NONE;
			gridBagConstraints12.gridy = 2;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints12.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints12.gridx = 4;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 3;
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints9.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints9.gridy = 2;
			lblRealmId = new JLabel(rb.getString("panel.admin.tab.safeguard.panel.realmId"));
			lblRealmIdDisplay = new JLabel("");
			lblRealmName = new JLabel(rb.getString("panel.admin.tab.safeguard.panel.realmName"));
			lblLdapUrl = new JLabel(rb.getString("panel.admin.tab.safeguard.panel.ldap.url"));
			lblLdapPrefix = new JLabel(rb.getString("panel.admin.tab.safeguard.panel.ldap.prefix"));
			lblLdapSuffix = new JLabel(rb.getString("panel.admin.tab.safeguard.panel.ldap.suffix"));
			lblAuthenticationType = new JLabel(rb.getString("panel.admin.tab.safeguard.panel.ldap.authenticationType"));
			panData = new JPanel();
			panData.setLayout(new GridBagLayout());
			panData.add(lblRealmId, gridBagConstraints10);
			panData.add(lblRealmIdDisplay, gridBagConstraints20);
			panData.add(lblRealmName, gridBagConstraints1);
			panData.add(getTxtRealmName(), gridBagConstraints2);
			panData.add(lblAuthenticationType, gridBagConstraints9);
			panData.add(getCboAuthenticationType(), gridBagConstraints12);
			panData.add(lblLdapUrl, gridBagConstraints3);
			panData.add(getTxtLdapUrl(), gridBagConstraints4);
			panData.add(lblLdapPrefix, gridBagConstraints5);
			panData.add(getTxtLdapPrefix(), gridBagConstraints6);
			panData.add(lblLdapSuffix, gridBagConstraints7);
			panData.add(getTxtLdapSuffix(), gridBagConstraints8);
			panData.add(panChooseLoginPage, gridBagConstraints11);
		}
		return panData;
	}

	private void init() {
		this.add(getPanData(), this.getTopGridBagConstraints());
	}

	@Override
	protected void deleteRealm() {
		if (this.lstRealms.isSelectionEmpty()) return;
		RealmLdapValue currentRealm = (RealmLdapValue) ((DropDownHolder) this.lstRealms.getSelectedValue()).getObject();
		comm.deleteLdapRealm(Integer.valueOf(currentRealm.getLdapRealmId()));
		((SortingListModel) this.lstRealms.getModel()).removeElementAt(this.lstRealms.getSelectedIndex());
		this.getTxtRealmName().setText("");
		this.getTxtLdapUrl().setText("");
		this.getTxtLdapPrefix().setText("");
		this.getTxtLdapSuffix().setText("");
		this.panChooseLoginPage.setLoginpage(null);
		this.panChooseLoginPage.setLoginPageViewComponentId(null);
		this.lblRealmIdDisplay.setText("");
		this.lstRealms.clearSelection();
		this.setActive(false);
	}

	@Override
	protected void addRealm() {
		this.getTxtRealmName().setText("[new Realm]");
		this.getTxtLdapUrl().setText("");
		this.getTxtLdapPrefix().setText("");
		this.getTxtLdapSuffix().setText("");
		this.getCboAuthenticationType().setSelectedIndex(0);
		this.panChooseLoginPage.setLoginpage(null);
		this.panChooseLoginPage.setLoginPageViewComponentId(null);
		this.lblRealmIdDisplay.setText("");
		this.lstRealms.clearSelection();
		this.setActive(true);
	}

	@Override
	protected void saveRealm() {
		RealmLdapValue value = new RealmLdapValue();
		if (this.lblRealmIdDisplay.getText().equalsIgnoreCase("")) {
			value.setLdapRealmId(-1);
		} else {
			value.setLdapRealmId(Integer.parseInt(this.lblRealmIdDisplay.getText()));
		}
		value.setRealmName(this.getTxtRealmName().getText());
		value.setLdapUrl(this.getTxtLdapUrl().getText());
		value.setLdapPrefix(this.getTxtLdapPrefix().getText());
		value.setLdapSuffix(this.getTxtLdapSuffix().getText());
		value.setLdapAuthenticationType(this.getCboAuthenticationType().getSelectedItem().toString());
		value.setLoginPageId(this.panChooseLoginPage.getLoginPageViewComponentId());
		if (validateSaveLdap(value)) {
			if (value.getLdapRealmId() == -1) {
				value.setLdapRealmId(comm.addLdapRealmToSite(Integer.valueOf(comm.getSiteId()), value).intValue());
				if (value.getLdapRealmId() != -1) {
					((SortingListModel) this.lstRealms.getModel()).addElement(new DropDownHolder(value, value.getRealmName()));
				}
			} else {
				comm.editLdapRealm(value);
				((SortingListModel) this.lstRealms.getModel()).removeElementAt(this.lstRealms.getSelectedIndex());
				((SortingListModel) this.lstRealms.getModel()).addElement(new DropDownHolder(value, value.getRealmName()));
			}
		}
	}

	private boolean validateSaveLdap(RealmLdapValue ldapValue) {
		boolean isValid = true;
		FocusListener focusListener = new FocusListener() {
			public void focusGained(FocusEvent e) {
				if (e.getSource() instanceof JTextField) {
					JTextField source = (JTextField) e.getSource();
					resetInputHighlight(source);
				}

				if (e.getSource() instanceof JComboBox) {
					JComboBox source = (JComboBox) e.getSource();
					resetInputHighlight(source);
				}
			}

			public void focusLost(FocusEvent e) {
			}

		};

		if (ldapValue.getLdapAuthenticationType() == null || ldapValue.getLdapAuthenticationType().isEmpty()) {
			this.cboAuthenticationType.setBackground(backgroundTextFieldError);
			this.cboAuthenticationType.addFocusListener(focusListener);
			isValid = false;
		}

		if (ldapValue.getLdapPrefix() == null || ldapValue.getLdapPrefix().isEmpty()) {
			this.txtLdapPrefix.setBackground(backgroundTextFieldError);
			txtLdapPrefix.addFocusListener(focusListener);
			isValid = false;
		}

		if (ldapValue.getLdapSuffix() == null || ldapValue.getLdapSuffix().isEmpty()) {
			this.txtLdapSuffix.setBackground(backgroundTextFieldError);
			txtLdapSuffix.addFocusListener(focusListener);
			isValid = false;
		}

		if (ldapValue.getLdapUrl() == null || ldapValue.getLdapUrl().isEmpty()) {
			txtLdapUrl.setBackground(backgroundTextFieldError);
			txtLdapUrl.addFocusListener(focusListener);
			isValid = false;
		}

		if (ldapValue.getRealmName() == null || ldapValue.getRealmName().isEmpty()) {
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

	private void resetInputHighlight(JComboBox source) {
		if (source.getBackground().equals(backgroundTextFieldError)) {
			source.setBackground(Color.WHITE);
		}
	}

	private void resetInputsHighlight() {
		resetInputHighlight(cboAuthenticationType);
		resetInputHighlight(txtLdapPrefix);
		resetInputHighlight(txtLdapSuffix);
		resetInputHighlight(txtLdapUrl);
		resetInputHighlight(txtRealmName);
	}

	@Override
	protected void listSelected() {
		if (lstRealms.getSelectedIndex() != -1) {
			RealmLdapValue val = (RealmLdapValue) ((DropDownHolder) lstRealms.getSelectedValue()).getObject();
			getTxtRealmName().setText(val.getRealmName());
			getTxtLdapUrl().setText(val.getLdapUrl());
			getTxtLdapPrefix().setText(val.getLdapPrefix());
			getTxtLdapSuffix().setText(val.getLdapSuffix());
			getCboAuthenticationType().setSelectedItem(val.getLdapAuthenticationType());
			this.lblRealmIdDisplay.setText(Integer.toString(val.getLdapRealmId()));
			this.panChooseLoginPage.setLoginpage(val.getLoginPageId());
			this.setActive(true);
			resetInputsHighlight();
		}
	}

	@Override
	public void fillRealmList() {
		try {
			RealmLdapValue[] val = comm.getLdapRealmsForSite(comm.getSiteId());
			SortingListModel listmodel = new SortingListModel();
			if (val != null) {
				for (int i = 0; i < val.length; i++) {
					listmodel.addElement(new DropDownHolder(val[i], val[i].getRealmName()));
				}
			}
			this.lstRealms.setModel(listmodel);
			this.getTxtRealmName().setText("");
			this.getTxtLdapUrl().setText("");
			this.getTxtLdapPrefix().setText("");
			this.getTxtLdapSuffix().setText("");
			this.getCboAuthenticationType().setSelectedIndex(0);
			this.panChooseLoginPage.setLoginpage(null);
			this.panChooseLoginPage.setLoginPageViewComponentId(null);
			this.lblRealmIdDisplay.setText("");
			this.lstRealms.clearSelection();
			this.setActive(false);
		} catch (Exception ex) {
			log.warn("Error filling list of all LdapRealms: " + ex.getMessage());
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
	 * This method initializes txtLdapUrl	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtLdapUrl() {
		if (txtLdapUrl == null) {
			txtLdapUrl = new JTextField();
			txtLdapUrl.setPreferredSize(new java.awt.Dimension(200, 20));
		}
		return txtLdapUrl;
	}

	/**
	 * This method initializes txtLdapPrefix	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtLdapPrefix() {
		if (txtLdapPrefix == null) {
			txtLdapPrefix = new JTextField();
			txtLdapPrefix.setPreferredSize(new java.awt.Dimension(200, 20));
		}
		return txtLdapPrefix;
	}

	/**
	 * This method initializes txtLdapSuffix	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtLdapSuffix() {
		if (txtLdapSuffix == null) {
			txtLdapSuffix = new JTextField();
			txtLdapSuffix.setPreferredSize(new java.awt.Dimension(200, 20));
		}
		return txtLdapSuffix;
	}

	private JComboBox getCboAuthenticationType() {
		if (this.cboAuthenticationType == null) {
			this.cboAuthenticationType = new JComboBox();
			DefaultComboBoxModel cboModel = new DefaultComboBoxModel();
			cboModel.addElement("");
			cboModel.addElement("simple");
			this.cboAuthenticationType.setModel(cboModel);
		}
		return this.cboAuthenticationType;
	}

	private void setActive(boolean enabled) {
		this.panChooseLoginPage.setEnabled(enabled);
		this.getTxtRealmName().setEnabled(enabled);
		this.getTxtLdapUrl().setEnabled(enabled);
		this.getTxtLdapPrefix().setEnabled(enabled);
		this.getTxtLdapSuffix().setEnabled(enabled);
		this.getCboAuthenticationType().setEnabled(enabled);
		super.getBtnSaveRealm().setEnabled(enabled);
		super.getBtnDeleteRealm().setEnabled(enabled);
	}

} //  @jve:decl-index=0:visual-constraint="10,10"
