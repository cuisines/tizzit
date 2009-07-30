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

import static de.juwimm.cms.client.beans.Application.*;
import static de.juwimm.cms.common.Constants.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.gui.admin.safeguard.PanChooseLoginPage;
import de.juwimm.cms.safeguard.vo.RealmLdapValue;
import de.juwimm.cms.util.Communication;
import de.juwimm.swing.DropDownHolder;

public final class PanelLDAPRealm extends JPanel implements ConfigurationInterface {
	private static Logger log = Logger.getLogger(PanelLDAPRealm.class);
	private static final long serialVersionUID = 3289399687428294715L;
	private Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private JComboBox jComboBoxRealms = null;
	private JLabel lblChooseRealm = null;
	private PanChooseLoginPage panChooseLoginPage = null;
	private PanelSelectAuthorizationRole4Realm panRequiredRole = null;

	public Integer getSelectedRealm() throws Exception {
		RealmLdapValue val = (RealmLdapValue) ((DropDownHolder) jComboBoxRealms.getSelectedItem()).getObject();
		return val.getLdapRealmId();
	}

	public void setExistingRealm(Integer realmId) {
		for (int i = 0; i < jComboBoxRealms.getItemCount(); i++) {
			DropDownHolder current = (DropDownHolder) jComboBoxRealms.getItemAt(i);
			RealmLdapValue val = (RealmLdapValue) current.getObject();
			if (val.getLdapRealmId() == realmId) {
				jComboBoxRealms.setSelectedItem(current);
				break;
			}
		}
	}

	/**
	 * This method initializes 
	 * 
	 */
	public PanelLDAPRealm() {
		super();
		initialize();
		this.setBorder(new TitledBorder(rb.getString("panel.panelSafeguard.realm.ldap")));
	}

	public void fillRealmList() {
		try {
			RealmLdapValue[] val = comm.getLdapRealmsForSite(Integer.valueOf(comm.getSiteId()));
			DefaultComboBoxModel listmodel = new DefaultComboBoxModel();
			if (val != null) {
				for (int i = 0; i < val.length; i++) {
					listmodel.addElement(new DropDownHolder(val[i], val[i].getRealmName()));
				}
				this.panChooseLoginPage.setEnabled(true);
				this.panRequiredRole.setEnabled(true);
			} else {
				this.panChooseLoginPage.setEnabled(false);
				this.panRequiredRole.setEnabled(false);
			}
			this.jComboBoxRealms.setModel(listmodel);
		} catch (Exception ex) {
			log.warn("CANNOT FILL LIST " + ex.getMessage());
		}
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.panChooseLoginPage = new PanChooseLoginPage();
		this.panChooseLoginPage.setEnabled(false);
		this.panRequiredRole = new PanelSelectAuthorizationRole4Realm();
		this.panRequiredRole.setEnabled(false);
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints3.gridy = 0;
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints3.insets = new java.awt.Insets(10, 10, 10, 10);
		gridBagConstraints3.gridx = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(15, 10, 10, 0);
		gridBagConstraints.gridx = 0;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints2.gridy = 2;
		gridBagConstraints2.weightx = 0.0;
		gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints2.insets = new java.awt.Insets(15, 10, 10, 0);
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridwidth = 2;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.gridy = 1;
		gridBagConstraints1.weightx = 0.0;
		gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 0);
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridwidth = 2;
		this.setLayout(new GridBagLayout());
		this.setSize(new java.awt.Dimension(398, 251));
		this.add(getJLabel(), gridBagConstraints);
		this.add(getJComboBoxRealms(), gridBagConstraints3);
		this.add(this.panChooseLoginPage, gridBagConstraints2);
		this.add(this.panRequiredRole, gridBagConstraints1);
	}

	/**
	 * This method initializes jComboBoxRealms	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBoxRealms() {
		if (jComboBoxRealms == null) {
			jComboBoxRealms = new JComboBox();
			jComboBoxRealms.setPreferredSize(new java.awt.Dimension(100, 23));
			jComboBoxRealms.setEditable(false);
		}
		return jComboBoxRealms;
	}

	private JLabel getJLabel() {
		if (lblChooseRealm == null) {
			lblChooseRealm = new JLabel();
			lblChooseRealm.setText(rb.getString("panel.panelSafeguard.realm.choose"));
		}
		return lblChooseRealm;
	}

	public String getLoginPageViewComponentId() {
		return this.panChooseLoginPage.getLoginPageViewComponentId();
	}

	public void setLoginPageViewComponentId(String loginPageId) {
		this.panChooseLoginPage.setLoginpage(loginPageId);
	}

	public String getRequiredRole() {
		return this.panRequiredRole.getRequiredRole();
	}

	public void setRequiredRole(String requiredRole) {
		this.panRequiredRole.setRequiredRole(requiredRole);
	}

}
