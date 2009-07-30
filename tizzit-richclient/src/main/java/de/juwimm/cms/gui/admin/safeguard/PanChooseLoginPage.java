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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.modules.InternalLink;
import de.juwimm.cms.content.modules.Module;
import de.juwimm.cms.util.Communication;

/**
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class PanChooseLoginPage extends JPanel {
	private static Logger log = Logger.getLogger(PanChooseLoginPage.class);
	private Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private JLabel lblLoginPage = null;
	private JButton btnChooseLoginPage = null;
	private String loginPageViewComponentId = null;
	private JLabel lblLoginPagePathText = null;

	public PanChooseLoginPage() {
		super();
		this.initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	protected final void initialize() {
		this.setLayout(new GridBagLayout());
		lblLoginPage = new JLabel();
		lblLoginPage.setText(rb.getString("panel.panelSafeguard.pagepath"));
		lblLoginPagePathText = new JLabel();
		lblLoginPagePathText.setText("");

		GridBagConstraints gbcLblLoginPage = new GridBagConstraints();
		gbcLblLoginPage.gridx = 0;
		gbcLblLoginPage.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gbcLblLoginPage.insets = new java.awt.Insets(0, 0, 0, 5);
		gbcLblLoginPage.gridy = 0;
		GridBagConstraints gbcLblLoginPagePathText = new GridBagConstraints();
		gbcLblLoginPagePathText.gridx = 1;
		gbcLblLoginPagePathText.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gbcLblLoginPagePathText.insets = new java.awt.Insets(0, 0, 0, 5);
		gbcLblLoginPagePathText.gridy = 0;
		gbcLblLoginPagePathText.weightx = 1.0;
		GridBagConstraints gbcBtnChooseLoginPage = new GridBagConstraints();
		gbcBtnChooseLoginPage.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gbcBtnChooseLoginPage.gridx = 0;
		gbcBtnChooseLoginPage.insets = new java.awt.Insets(5, 0, 0, 0);
		gbcBtnChooseLoginPage.gridy = 1;
		gbcBtnChooseLoginPage.gridwidth = 2;

		this.add(lblLoginPage, gbcLblLoginPage);
		this.add(lblLoginPagePathText, gbcLblLoginPagePathText);
		this.add(getBtnChooseLoginPage(), gbcBtnChooseLoginPage);
	}

	/**
	 * This method initializes jButtonChooseLoginPage	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnChooseLoginPage() {
		if (btnChooseLoginPage == null) {
			btnChooseLoginPage = new JButton();
			btnChooseLoginPage.setText(rb.getString("panel.panelSafeguard.btn.pagelogin"));
			btnChooseLoginPage.addActionListener(new java.awt.event.ActionListener() {
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
						log.warn("CANNOT SHOW INTERNAL LINKS " + ex.getMessage());
					}
				}
			});
		}
		return btnChooseLoginPage;
	}

	public void setLoginpage(String page) {
		this.loginPageViewComponentId = page;
		if (page != null) {
			String loginPagePath = comm.getPathForViewComponentId(Integer.valueOf(page).intValue());
			this.lblLoginPagePathText.setText("/" + loginPagePath);
		} else {
			this.lblLoginPagePathText.setText("");
		}
	}

	public String getLoginPageViewComponentId() {
		return loginPageViewComponentId;
	}

	public void setLoginPageViewComponentId(String loginPageViewComponentId) {
		this.loginPageViewComponentId = loginPageViewComponentId;
	}
	
	public void setEnabled(boolean enabling) {
		this.btnChooseLoginPage.setEnabled(enabling);
	}

} //  @jve:decl-index=0:visual-constraint="126,67"
