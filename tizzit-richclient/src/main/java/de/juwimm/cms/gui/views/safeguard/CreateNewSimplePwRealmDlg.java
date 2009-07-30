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

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;

import org.apache.log4j.Logger;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.modules.InternalLink;
import de.juwimm.cms.content.modules.Module;
import de.juwimm.cms.util.Communication;

public final class CreateNewSimplePwRealmDlg extends JDialog {
	private static final long serialVersionUID = 1659106293285607496L;
	private static Logger logger = Logger.getLogger(CreateNewSimplePwRealmDlg.class);
	private Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private boolean addrealm = false;
	private String realmname = null;
	private String loginPageViewComponentId = null;
	private JPanel jContentPane = null;
	private JPanel jPanelSouth = null;
	private JButton jButtonOK = null;
	private JButton jButtonCancel = null;
	private JPanel jPanelCenter = null;
	private JLabel jLabel = null;
	private JTextField jTfRealmName = null;
	private JLabel jLabelLoginPage = null;
	private JButton jButtonChooseLoginPage = null;
	private JLabel lblLoginPagePath = null;
	private JLabel lblLoginPagePathText = null;

	/**
	 * This is the default constructor
	 */
	public CreateNewSimplePwRealmDlg() {
		super();
		initialize();
	}

	public String getLoginpage() {
		return this.loginPageViewComponentId;
	}

	public boolean addRealm() {
		return this.addrealm;
	}

	public String getRealmName() {
		return this.realmname;
	}

	private void setLoginpage(String page) {
		this.loginPageViewComponentId = page;
		String loginPagePath = comm.getPathForViewComponentId(Integer.valueOf(page).intValue());
		this.lblLoginPagePath.setText("/" + loginPagePath);
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setSize(362, 183);
		this.setTitle(rb.getString("panel.panelSafeguard.realm.createnew"));
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanelSouth(), java.awt.BorderLayout.SOUTH);
			jContentPane.add(getJPanelCenter(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanelSouth	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelSouth() {
		if (jPanelSouth == null) {
			jPanelSouth = new JPanel();
			jPanelSouth.add(getJButtonOK(), null);
			jPanelSouth.add(getJButtonCancel(), null);
		}
		return jPanelSouth;
	}

	/**
	 * This method initializes jButtonOK	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new JButton();
			jButtonOK.setText(rb.getString("dialog.ok"));
			jButtonOK.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					realmname = jTfRealmName.getText();
					if (realmname.trim().length() > 1) {
						addrealm = true;
						dispose();
					} else {
						JOptionPane.showMessageDialog(null, rb.getString("panel.panelSafeguard.realm.nameplease"));
					}
				}
			});
		}
		return jButtonOK;
	}

	/**
	 * This method initializes jButtonCancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setText(rb.getString("dialog.cancel"));
			jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return jButtonCancel;
	}

	/**
	 * This method initializes jPanelCenter	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelCenter() {
		if (jPanelCenter == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints12.insets = new java.awt.Insets(10,10,10,0);
			gridBagConstraints12.gridy = 2;
			lblLoginPagePathText = new JLabel();
			lblLoginPagePathText.setText(rb.getString("panel.panelSafeguard.pagepath"));
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.insets = new java.awt.Insets(10,10,10,10);
			gridBagConstraints11.gridy = 2;
			lblLoginPagePath = new JLabel();
			lblLoginPagePath.setText("");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.insets = new java.awt.Insets(10,10,0,10);
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints3.gridy = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.insets = new java.awt.Insets(15,10,0,10);
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints2.gridy = 1;
			jLabelLoginPage = new JLabel();
			jLabelLoginPage.setText(rb.getString("panel.panelSafeguard.pagelogin"));
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.insets = new java.awt.Insets(10,10,0,10);
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new java.awt.Insets(15,10,10,10);
			gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints.gridy = 0;
			jLabel = new JLabel();
			jLabel.setText(rb.getString("panel.panelSafeguard.realm.name"));
			jPanelCenter = new JPanel();
			jPanelCenter.setLayout(new GridBagLayout());
			jPanelCenter.add(jLabel, gridBagConstraints);
			jPanelCenter.add(getJTfRealmName(), gridBagConstraints1);
			jPanelCenter.add(jLabelLoginPage, gridBagConstraints2);
			jPanelCenter.add(getJButtonChooseLoginPage(), gridBagConstraints3);
			jPanelCenter.add(lblLoginPagePath, gridBagConstraints11);
			jPanelCenter.add(lblLoginPagePathText, gridBagConstraints12);
		}
		return jPanelCenter;
	}

	/**
	 * This method initializes jTfRealmName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTfRealmName() {
		if (jTfRealmName == null) {
			jTfRealmName = new JTextField();
			jTfRealmName.setPreferredSize(new java.awt.Dimension(200, 23));
		}
		return jTfRealmName;
	}

	/**
	 * This method initializes jButtonChooseLoginPage	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonChooseLoginPage() {
		if (jButtonChooseLoginPage == null) {
			jButtonChooseLoginPage = new JButton();
			jButtonChooseLoginPage.setText(rb.getString("panel.panelSafeguard.btn.pagelogin"));
			jButtonChooseLoginPage.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						Module chooseViewComponentDialog = new InternalLink(true);
						Element elmLinkRoot = ContentManager.getDomDoc().createElement("linkRoot");
						Element elmInternalLink = ContentManager.getDomDoc().createElement("internalLink");
						CDATASection txtNode = ContentManager.getDomDoc().createCDATASection("");
						if (loginPageViewComponentId != null & !"".equalsIgnoreCase(loginPageViewComponentId)) {
							elmInternalLink.setAttribute("viewid", loginPageViewComponentId);
						}
						elmInternalLink.appendChild(txtNode);

						elmLinkRoot.appendChild(elmInternalLink);
						chooseViewComponentDialog.setProperties(elmLinkRoot);
						chooseViewComponentDialog.viewModalUI(true);
						chooseViewComponentDialog.load();
						if (chooseViewComponentDialog.isSaveable()) {
							Node prop = chooseViewComponentDialog.getProperties();
							String viewId = ((Element) prop.getFirstChild()).getAttribute("viewid");
							setLoginpage(viewId);
						}
					} catch (Exception ex) {
						logger.warn("CANNOT SHOW INTERNAL LINKS " + ex.getMessage());
					}
				}
			});
		}
		return jButtonChooseLoginPage;
	}

}  //  @jve:decl-index=0:visual-constraint="268,232"
