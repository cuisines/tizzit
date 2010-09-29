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
package de.juwimm.cms.gui.views.page;

import static de.juwimm.cms.client.beans.Application.getBean;
import static de.juwimm.cms.common.Constants.rb;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.gui.controls.LoadableViewComponentPanel;
import de.juwimm.cms.gui.views.safeguard.PanGrailsRealm;
import de.juwimm.cms.gui.views.safeguard.PanJaasRealm;
import de.juwimm.cms.gui.views.safeguard.PanJdbcRealm;
import de.juwimm.cms.gui.views.safeguard.PanelLDAPRealm;
import de.juwimm.cms.gui.views.safeguard.PanelSimplePwRealm;
import de.juwimm.cms.safeguard.vo.ActiveRealmValue;
import de.juwimm.cms.util.ActionHub;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.ViewComponentValue;

/**
 * @author <a href="mailto:michael.meyer@juwimm.com">Michael Meyer</a><br/>
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public final class PanelSafeGuard extends JPanel implements LoadableViewComponentPanel {
	private static final long serialVersionUID = 202512339880580071L;
	private static Logger log = Logger.getLogger(PanelSafeGuard.class);
	private final Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private int viewComponentId = -1;
	private final PanelSimplePwRealm panSimplePwRealm = new PanelSimplePwRealm();
	private final PanJdbcRealm panJdbcRealm = new PanJdbcRealm();
	private final PanelLDAPRealm panLdapRealm = new PanelLDAPRealm();
	private final PanJaasRealm panJaasRealm = new PanJaasRealm();
	private final PanGrailsRealm panGrails = new PanGrailsRealm();
	private final int REALM_NONE = -1;
	private final int REALM_SIMPLE_PW = 1;
	private final int REALM_RDBMS = 2;
	private final int REALM_JAAS = 3;
	private final int REALM_LDAP = 4;
	private final int REALM_GRAILS = 5;
	private int selectedRealm = REALM_NONE;
	private final ButtonGroup realmGroup = new ButtonGroup();
	private JRadioButton rbSimplePw = null;
	private JRadioButton rbJdbc = null;
	private JRadioButton rbJaas = null;
	private JRadioButton rbLdap = null;
	private JRadioButton rbGrails = null;
	private JRadioButton rbNoRealm = null;
	private JPanel panConfigure = null;
	private JPanel panSelection = null;
	private ActiveRealmValue activeRealm = null;
	private String loginPageId = null;

	public PanelSafeGuard() {
		super();
		initialize();
		this.initializeButtonGroup();
	}

	private void initializeButtonGroup() {
		realmGroup.add(rbSimplePw);
		realmGroup.add(rbJdbc);
		realmGroup.add(rbJaas);
		realmGroup.add(rbLdap);
		realmGroup.add(rbGrails);
		realmGroup.add(rbNoRealm);

	}

	private void setActiveRealm(int viewComponentId) {
		if (log.isDebugEnabled()) log.debug("setActiveRealm");
		try {
			this.activeRealm = comm.getActiveRealm(new Integer(viewComponentId));
			this.loginPageId = null;
			if (this.activeRealm.getLoginPageId() != null) this.loginPageId = this.activeRealm.getLoginPageId().toString();
			if (this.activeRealm.isRealmNone()) {
				this.rbNoRealm.setSelected(true);
				this.panSimplePwRealm.setRequiredRole("");
			} else if (this.activeRealm.isRealmSimplePw()) {
				this.rbSimplePw.setSelected(true);
				this.selectedRealm = REALM_SIMPLE_PW;
				this.panSimplePwRealm.fillRealmList();
				this.panSimplePwRealm.setExistingRealm(Integer.valueOf(this.activeRealm.getRealmId()));
				this.panSimplePwRealm.setLoginPageViewComponentId(this.loginPageId);
				this.panSimplePwRealm.setRequiredRole(this.activeRealm.getRoleNeeded());
			} else if (this.activeRealm.isRealmJdbc()) {
				this.rbJdbc.setSelected(true);
				this.selectedRealm = REALM_RDBMS;
				this.panJdbcRealm.fillRealmList();
				this.panJdbcRealm.setExistingRealm(Integer.valueOf(this.activeRealm.getRealmId()));
				this.panJdbcRealm.setLoginPageViewComponentId(this.loginPageId);
				this.panJdbcRealm.setRequiredRole(this.activeRealm.getRoleNeeded());
			} else if (this.activeRealm.isRealmLdap()) {
				this.rbLdap.setSelected(true);
				this.selectedRealm = REALM_LDAP;
				this.panLdapRealm.fillRealmList();
				this.panLdapRealm.setExistingRealm(Integer.valueOf(this.activeRealm.getRealmId()));
				this.panLdapRealm.setLoginPageViewComponentId(this.loginPageId);
				this.panLdapRealm.setRequiredRole(this.activeRealm.getRoleNeeded());
			} else if (this.activeRealm.isRealmJaas()) {
				this.rbJaas.setSelected(true);
				this.selectedRealm = REALM_JAAS;
				this.panJaasRealm.fillRealmList();
				this.panJaasRealm.setExistingRealm(Integer.valueOf(this.activeRealm.getRealmId()));
				this.panJaasRealm.setLoginPageViewComponentId(this.loginPageId);
				this.panJaasRealm.setRequiredRole(this.activeRealm.getRoleNeeded());
			}

			if (!this.activeRealm.isRealmNone()) {
				this.showConfigurePanel();
			} else {
				Integer protectedParentId = this.comm.getFirstProtectedParentId(Integer.valueOf(viewComponentId));
				if (log.isDebugEnabled()) log.debug("First protected Parent: " + protectedParentId);
				this.showIndirectProtectionPanel(protectedParentId);
			}

		} catch (Exception ex) {
			log.warn("Could not set active Realm for ViewComponent " + viewComponentId + ": " + ex.getMessage());
		}
	}

	private void showConfigurePanel() {
		panConfigure.removeAll();
		switch (selectedRealm) {
			case (REALM_SIMPLE_PW): {
				panConfigure.add(panSimplePwRealm, BorderLayout.NORTH);
				break;
			}
			case (REALM_RDBMS): {
				panConfigure.add(panJdbcRealm, BorderLayout.NORTH);
				break;
			}
			case (REALM_LDAP): {
				panConfigure.add(panLdapRealm, BorderLayout.NORTH);
				break;
			}
			case (REALM_JAAS): {
				panConfigure.add(panJaasRealm, BorderLayout.NORTH);
				break;
			}
			case (REALM_GRAILS): {
				panConfigure.add(panGrails, BorderLayout.NORTH);
				break;
			}
			case (REALM_NONE): {
				Integer protectedParentId = this.comm.getFirstProtectedParentId(Integer.valueOf(this.viewComponentId));
				if (log.isDebugEnabled()) log.debug("First protected Parent: " + protectedParentId);
				this.showIndirectProtectionPanel(protectedParentId);
				break;
			}
			default: {
				break;
			}
		}
		panConfigure.updateUI();
	}

	private void showIndirectProtectionPanel(final Integer viewComponentId) {
		panConfigure.removeAll();
		if (viewComponentId != null && viewComponentId.intValue() > 0) {
			JButton btnJump = new JButton(rb.getString("panel.panelView.safeguard.btnJump"));
			btnJump.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnJumpActionPerformed(viewComponentId);
				}
			});
			JPanel panProtection = new JPanel();
			panProtection.setBorder(new TitledBorder(rb.getString("panel.panelView.safeguard.protected")));
			panProtection.add(btnJump);
			panConfigure.add(panProtection, BorderLayout.NORTH);
		}
		panConfigure.updateUI();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setBackground(UIConstants.backgroundBaseColor);
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 0;
		gridBagConstraints6.ipady = 0;
		gridBagConstraints6.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints6.insets = new java.awt.Insets(10, 10, 0, 10);
		gridBagConstraints6.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints6.weightx = 1.0;
		gridBagConstraints6.weighty = 0.0;
		gridBagConstraints6.gridy = 0;
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.gridx = 0;
		gridBagConstraints5.ipadx = 0;
		gridBagConstraints5.ipady = 0;
		gridBagConstraints5.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints5.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints5.weightx = 1.0;
		gridBagConstraints5.weighty = 1.0;
		gridBagConstraints5.insets = new java.awt.Insets(10, 10, 10, 10);
		gridBagConstraints5.gridy = 1;
		this.setLayout(new GridBagLayout());
		this.setSize(new java.awt.Dimension(637, 300));
		this.add(getPanConfigure(), gridBagConstraints5);
		this.add(getPanSelection(), gridBagConstraints6);
	}

	public void save() throws Exception {
		// panel has not been loaded; just needs to be saved if user has clicked on the protection pane
		if (viewComponentId > 0) {
			if (log.isDebugEnabled()) log.debug("save");
			if (rbNoRealm.isSelected()) {
				if (log.isDebugEnabled()) log.debug("no realm selected");
				comm.deleteRealmAtVC(Integer.valueOf(this.viewComponentId));
			} else if (rbSimplePw.isSelected()) {
				if (log.isDebugEnabled()) log.debug("SimplePwRealm selected");
				int pk;
				try {
					pk = panSimplePwRealm.getSelectedRealm().intValue();
				} catch (Exception e) {
					noSelectedProtectionMessage();
					return;
				}
				String loginPage = panSimplePwRealm.getLoginPageViewComponentId();
				Integer loginPageId = null;
				try {
					loginPageId = Integer.valueOf(loginPage);
				} catch (Exception e) {
				}
				String requiredRole = panSimplePwRealm.getRequiredRole();
				comm.addSimplePwRealmToVC(new Integer(pk), new Integer(this.viewComponentId), requiredRole, loginPageId);
			} else if (rbJdbc.isSelected()) {
				if (log.isDebugEnabled()) log.debug("JdbcRealm selected");
				int pk;
				try {
					pk = panJdbcRealm.getSelectedRealm().intValue();
				} catch (Exception e) {
					noSelectedProtectionMessage();
					return;
				}
				String loginPage = panJdbcRealm.getLoginPageViewComponentId();
				Integer loginPageId = null;
				try {
					loginPageId = Integer.valueOf(loginPage);
				} catch (Exception e) {
				}
				String requiredRole = panJdbcRealm.getRequiredRole();
				comm.addSqlDbRealmToVC(new Integer(pk), new Integer(this.viewComponentId), requiredRole, loginPageId);
			} else if (rbLdap.isSelected()) {
				if (log.isDebugEnabled()) log.debug("LdapRealm selected");
				int pk;
				try {
					pk = panLdapRealm.getSelectedRealm().intValue();
				} catch (Exception e) {
					noSelectedProtectionMessage();
					return;
				}
				String loginPage = panLdapRealm.getLoginPageViewComponentId();
				Integer loginPageId = null;
				try {
					loginPageId = Integer.valueOf(loginPage);
				} catch (Exception e) {
				}
				String requiredRole = panLdapRealm.getRequiredRole();
				comm.addLdapRealmToVC(new Integer(this.viewComponentId), new Integer(pk), requiredRole, loginPageId);
			} else if (rbJaas.isSelected()) {
				if (log.isDebugEnabled()) log.debug("JaasRealm selected");
				int pk;
				try {
					pk = panJaasRealm.getSelectedRealm().intValue();
				} catch (Exception e) {
					noSelectedProtectionMessage();
					return;
				}
				String loginPage = panJaasRealm.getLoginPageViewComponentId();
				Integer loginPageId = null;
				try {
					loginPageId = Integer.valueOf(loginPage);
				} catch (Exception e) {
				}
				String requiredRole = panJaasRealm.getRequiredRole();
				comm.addJaasRealmToVC(new Integer(this.viewComponentId), new Integer(pk), requiredRole, loginPageId);
			} else if (rbGrails.isSelected()) {
				if (log.isDebugEnabled()) log.debug("Grails role selected");
				//
				//change change change
				int pk;
				try {
					pk = panGrails.getSelectedRealm().intValue();
				} catch (Exception e) {
					noSelectedProtectionMessage();
					return;
				}
				String loginPage = panJaasRealm.getLoginPageViewComponentId();
				Integer loginPageId = null;
				try {
					loginPageId = Integer.valueOf(loginPage);
				} catch (Exception e) {
				}
				String requiredRole = panJaasRealm.getRequiredRole();
				comm.addJaasRealmToVC(new Integer(this.viewComponentId), new Integer(pk), requiredRole, loginPageId);
			} else {
				if (log.isDebugEnabled()) log.debug("unknown realm selected?");
			}
			this.setActiveRealm(this.viewComponentId);
		}
	}

	public void load(ViewComponentValue viewComponent) {
		if (log.isDebugEnabled()) log.debug("load");
		if (viewComponent == null) {
			viewComponentId = -1;
		} else {
			this.viewComponentId = viewComponent.getViewComponentId();
			this.setActiveRealm(this.viewComponentId);
		}
	}

	public void unload() {
		if (log.isDebugEnabled()) log.debug("unload");
		panConfigure.removeAll();
	}

	/**
	 * This method initializes rbSimplePw	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getRbSimplePw() {
		if (rbSimplePw == null) {
			rbSimplePw = new JRadioButton();
			rbSimplePw.setText(rb.getString("panel.panelSafeguard.realm.simple"));
			rbSimplePw.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					selectedRealm = REALM_SIMPLE_PW;
					panSimplePwRealm.fillRealmList();
					if (activeRealm.isRealmSimplePw()) {
						panSimplePwRealm.setExistingRealm(Integer.valueOf(activeRealm.getRealmId()));
						panSimplePwRealm.setLoginPageViewComponentId(loginPageId);
					}
					showConfigurePanel();
				}
			});
		}
		return rbSimplePw;
	}

	/**
	 * This method initializes jRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getRbJdbc() {
		if (rbJdbc == null) {
			rbJdbc = new JRadioButton();
			rbJdbc.setText(rb.getString("panel.panelSafeguard.realm.sql"));
			rbJdbc.setEnabled(true);
			rbJdbc.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					selectedRealm = REALM_RDBMS;
					panJdbcRealm.fillRealmList();
					if (activeRealm.isRealmJdbc()) {
						panJdbcRealm.setExistingRealm(Integer.valueOf(activeRealm.getRealmId()));
						panJdbcRealm.setLoginPageViewComponentId(loginPageId);
					}
					showConfigurePanel();
				}
			});

		}
		return rbJdbc;
	}

	/**
	 * This method initializes rbJaas	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getRbGrails() {
		if (rbGrails == null) {
			rbGrails = new JRadioButton();
			rbGrails.setText(rb.getString("panel.panelSafeguard.realm.grails"));
			rbGrails.setEnabled(true);
			rbGrails.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					selectedRealm = REALM_GRAILS;
					panGrails.fillRealmList();
					//FIXME
					//					if (activeRealm.isRealmJaas()) {
					//						panJaasRealm.setExistingRealm(Integer.valueOf(activeRealm.getRealmId()));
					//						panJaasRealm.setLoginPageViewComponentId(loginPageId);
					//					}
					showConfigurePanel();
				}
			});
		}
		return rbGrails;
	}

	/**
	 * This method initializes rbJaas	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getRbJaas() {
		if (rbJaas == null) {
			rbJaas = new JRadioButton();
			rbJaas.setText(rb.getString("panel.panelSafeguard.realm.jaas"));
			rbJaas.setEnabled(true);
			rbJaas.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					selectedRealm = REALM_JAAS;
					panJaasRealm.fillRealmList();
					if (activeRealm.isRealmJaas()) {
						panJaasRealm.setExistingRealm(Integer.valueOf(activeRealm.getRealmId()));
						panJaasRealm.setLoginPageViewComponentId(loginPageId);
					}
					showConfigurePanel();
				}
			});
		}
		return rbJaas;
	}

	/**
	 * This method initializes rbLdap	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getRbLdap() {
		if (rbLdap == null) {
			rbLdap = new JRadioButton();
			rbLdap.setText(rb.getString("panel.panelSafeguard.realm.ldap"));
			rbLdap.setEnabled(true);
			rbLdap.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					selectedRealm = REALM_LDAP;
					panLdapRealm.fillRealmList();
					if (activeRealm.isRealmLdap()) {
						panLdapRealm.setExistingRealm(Integer.valueOf(activeRealm.getRealmId()));
						panLdapRealm.setLoginPageViewComponentId(loginPageId);
					}
					showConfigurePanel();
				}
			});
		}
		return rbLdap;
	}

	/**
	 * This method initializes rbNoRealm	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getRbNoRealm() {
		if (rbNoRealm == null) {
			rbNoRealm = new JRadioButton();
			rbNoRealm.setText(rb.getString("panel.panelSafeguard.realm.none"));
			rbNoRealm.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					selectedRealm = REALM_NONE;
					showConfigurePanel();
				}
			});
		}
		return rbNoRealm;
	}

	/**
	 * This method initializes panConfigure	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanConfigure() {
		if (panConfigure == null) {
			panConfigure = new JPanel();
			panConfigure.setLayout(new BorderLayout());
			panConfigure.setBackground(UIConstants.backgroundBaseColor);
		}
		return panConfigure;
	}

	/**
	 * This method initializes panSelection	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanSelection() {
		if (panSelection == null) {

			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints4.gridy = 4;
			gridBagConstraints4.insets = new java.awt.Insets(10, 10, 0, 0);
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.weighty = 1.0;
			gridBagConstraints4.gridx = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints5.gridy = 5;
			gridBagConstraints5.insets = new java.awt.Insets(10, 10, 0, 0);
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.weighty = 1.0;
			gridBagConstraints5.gridx = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints3.gridy = 3;
			gridBagConstraints3.insets = new java.awt.Insets(10, 10, 0, 0);
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.weighty = 1.0;
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints0 = new GridBagConstraints();
			gridBagConstraints0.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints0.gridy = 0;
			gridBagConstraints0.insets = new java.awt.Insets(10, 10, 0, 0);
			gridBagConstraints0.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints0.weightx = 1.0;
			gridBagConstraints0.weighty = 1.0;
			gridBagConstraints0.gridx = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.weighty = 1.0;
			gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 0);
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints2.gridy = 2;
			gridBagConstraints2.insets = new java.awt.Insets(10, 10, 0, 0);
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.gridx = 1;
			panSelection = new JPanel();
			panSelection.setBorder(new TitledBorder(rb.getString("panel.panelSafeguard.realm.choosekind")));
			panSelection.setLayout(new GridBagLayout());
			panSelection.add(getRbNoRealm(), gridBagConstraints0);
			panSelection.add(getRbSimplePw(), gridBagConstraints1);
			panSelection.add(getRbJdbc(), gridBagConstraints2);
			panSelection.add(getRbJaas(), gridBagConstraints3);
			panSelection.add(getRbLdap(), gridBagConstraints4);
			panSelection.add(getRbGrails(), gridBagConstraints5);
		}
		return panSelection;
	}

	void btnJumpActionPerformed(Integer viewComponentId) {
		ActionHub.fireActionPerformed(new ActionEvent(viewComponentId.toString(), ActionEvent.ACTION_PERFORMED, Constants.ACTION_TREE_JUMP));
	}

	protected void noSelectedProtectionMessage() {
		JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("panel.panelView.safeguard.noSelectedProtection"), rb.getString("dialog.title"), JOptionPane.INFORMATION_MESSAGE);
	}

} //  @jve:decl-index=0:visual-constraint="10,10"
