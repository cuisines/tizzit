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

import static de.juwimm.cms.client.beans.Application.getBean;
import static de.juwimm.cms.common.Constants.rb;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.gui.admin.safeguard.PanChooseLoginPage;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.vo.AccessRoleValue;
import de.juwimm.swing.PickListData;
import de.juwimm.swing.PickListPanel;

/**
 * 
 * @author <a href="mailto:rene.hertzfeldt@juwimm.com">Rene Hertzfeldt</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id: PanGrailsRealm.java 6 2009-07-30 14:05:05Z  $
 */
public final class PanGrailsRealm extends JPanel implements ConfigurationInterface {
	private static final long serialVersionUID = -6549018298391847196L;
	private static Logger log = Logger.getLogger(PanGrailsRealm.class);
	private final Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private PanChooseLoginPage panChooseLoginPage = null;
	private PickListData rolePickData = null;
	private PickListPanel panRolePick = null;

	public void setExistingRoles(String[] roles) {
		if (roles != null) {
			rolePickData.getLstLeftModel().addAll(roles);
		}
	}

	public String[] getSelectedRoles() {
		int size = rolePickData.getLstLeftModel().getSize();
		String[] roles = null;
		if (size > 0) {
			roles = new String[size];
			int i = 0;
			for (Iterator it = rolePickData.getLstLeftModel().iterator(); it.hasNext();) {
				roles[i++] = (String) it.next();
			}
		}
		return roles;
	}

	/**
	 * This method initializes 
	 * 
	 */
	public PanGrailsRealm() {
		super();
		initialize();
		this.setBorder(new TitledBorder(rb.getString("panel.panelSafeguard.realm.grails")));
	}

	public void fillRealmList() {
		try {
			AccessRoleValue[] val = comm.getAllAccessRoles();
			if (val != null) {
				for (int i = 0; i < val.length; i++) {
					this.rolePickData.getLstRightModel().addElement(val[i].getRoleId());
				}
				this.panChooseLoginPage.setEnabled(true);
				this.panRolePick.setEnabled(true);
				//delete assigned roles
				this.rolePickData.getLstLeftModel().removeAllElements();
			} else {
				this.panChooseLoginPage.setEnabled(false);
				this.panRolePick.setEnabled(false);
			}

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
		this.rolePickData = new PickListData();
		this.rolePickData.setLeftLabel(rb.getString("panel.panelSafeguard.realm.grails.roles.assigned"));
		this.rolePickData.setRightLabel(rb.getString("panel.panelSafeguard.realm.grails.roles.available"));
		this.panRolePick = new PickListPanel(this.rolePickData);
		this.panRolePick.setEnabled(false);

		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.weightx = 0.0;
		gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints1.insets = new java.awt.Insets(15, 10, 10, 0);
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridwidth = 2;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints2.insets = new java.awt.Insets(10, 10, 10, 10);
		gridBagConstraints2.gridx = 1;
		this.setLayout(new GridBagLayout());
		this.setSize(new java.awt.Dimension(398, 250));
		this.add(this.panChooseLoginPage, gridBagConstraints1);
		this.add(this.panRolePick, gridBagConstraints2);
		fillRealmList();
	}

	public String getLoginPageViewComponentId() {
		return this.panChooseLoginPage.getLoginPageViewComponentId();
	}

	public void setLoginPageViewComponentId(String loginPageId) {
		this.panChooseLoginPage.setLoginpage(loginPageId);
	}

	// just inherited by the interface 
	public Integer getSelectedRealm() throws Exception {
		return null;
	}

	// just inherited by the interface 
	public void setExistingRealm(Integer accessRoleId) {
	}
} //  @jve:decl-index=0:visual-constraint="257,279"
