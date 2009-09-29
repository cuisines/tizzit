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
/*
 * Created on 20.08.2004
 */
package de.juwimm.cms.gui.admin;

import static de.juwimm.cms.client.beans.Application.getBean;
import static de.juwimm.cms.common.Constants.rb;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.event.EditpaneFiredEvent;
import de.juwimm.cms.content.event.EditpaneFiredListener;
import de.juwimm.cms.content.modules.InternalLink;
import de.juwimm.cms.content.modules.Module;
import de.juwimm.cms.util.ActionHub;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.vo.HostValue;
import de.juwimm.cms.vo.ViewComponentValue;
import de.juwimm.swing.DropDownHolder;
import de.juwimm.swing.SortingListModel;

/**
 * Dialog for creating a new host
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * @company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class CreateNewHostDialog extends JDialog implements EditpaneFiredListener {
	private static Logger log = Logger.getLogger(CreateNewHostDialog.class);
	private static final int MODE_ADD_HOST = 0;
	private static final int MODE_EDIT_HOST = 1;

	private JPanel panContentPane = null;
	private JTextField txtHost = null;
	private JButton btnAddHost = null;
	private JButton btnCancelHost = null;
	private JPanel parent = null;
	private Module chooseViewComponentDialog = null;
	private JLabel lblHeadline = new JLabel();
	private HostValue currentHost = null;
	private int mode = CreateNewHostDialog.MODE_ADD_HOST;
	private Integer startPageId = null;
	private JTextField txtStartPage = null;
	private JPanel panStartPage = null;
	private JLabel lblStartPage = null;
	private JButton btnChooseStartPage = null;
	private JButton btnDeleteStartPage = null;
	private Communication communication = ((Communication) getBean(Beans.COMMUNICATION));
	private boolean assignSiteAutomatically = false;
	private JLabel lblRedirectUrl = null;
	private JTextField txtRedirectUrl = null;
	private JLabel lblRedirectHost = null;
	private JComboBox cbRedirectHost = null;
	private JLabel lblLiveServer = null;
	private JCheckBox liveServer = null;

	/**
	 * This constructor can be used to edit an existing host 
	 * @param parent
	 * @param host
	 * @param autoAssignSite
	 */
	public CreateNewHostDialog(JPanel parent, HostValue host, boolean autoAssignSite) {
		super();
		this.setModal(true);
		this.setTitle(rb.getString("panel.admin.host.createhost"));
		initialize();
		this.chooseViewComponentDialog = new InternalLink(true);
		chooseViewComponentDialog.addEditpaneFiredListener(this);
		this.mode = CreateNewHostDialog.MODE_ADD_HOST;
		this.parent = parent;
		this.mode = CreateNewHostDialog.MODE_ADD_HOST;
		this.assignSiteAutomatically = autoAssignSite;
		if (host != null) {
			this.currentHost = host;
			this.btnAddHost.setText(rb.getString("dialog.change"));
			this.setTitle(rb.getString("panel.admin.host.edithost"));
			this.lblHeadline.setText(rb.getString("panel.admin.host.pleaseedithost") + ":");
			this.txtHost.setText(host.getHostName());
			this.txtHost.setEditable(false);
			String startPageName = "";
			String sPage = communication.getStartPage(host.getHostName());
			if ((sPage != null) && (!("".equalsIgnoreCase(sPage)))) {
				this.startPageId = Integer.valueOf(sPage);
				startPageName = getVcTitle(sPage);
			}
			this.txtStartPage.setText(startPageName);
			this.txtRedirectUrl.setText(host.getRedirectUrl());
			this.liveServer.setSelected(host.getLiveServer());
			this.mode = CreateNewHostDialog.MODE_EDIT_HOST;
			this.updateValues();
		}
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(480, 330);
		this.setResizable(false);
		this.setContentPane(getPanContentPane());
		this.getRootPane().setDefaultButton(this.btnAddHost);
	}

	/**
	 * This method initializes panContentPane	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanContentPane() {
		if (panContentPane == null) {
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints14.gridy = 3;
			gridBagConstraints14.weightx = 1.0;
			gridBagConstraints14.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints14.insets = new java.awt.Insets(5, 5, 0, 10);
			gridBagConstraints14.gridx = 1;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints8.insets = new java.awt.Insets(8, 10, 0, 0);
			gridBagConstraints8.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.gridy = 3;
			lblRedirectHost = new JLabel();
			lblRedirectHost.setText("Redirect Host:");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridy = 2;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints1.insets = new java.awt.Insets(5, 5, 0, 10);
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints.insets = new java.awt.Insets(8, 10, 0, 0);
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridy = 2;
			lblRedirectUrl = new JLabel();
			lblRedirectUrl.setText("Redirect Url:");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			javax.swing.JLabel lblHost = new JLabel();
			panContentPane = new JPanel();
			panContentPane.setLayout(new GridBagLayout());
			lblHost.setText("Host:");
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.gridy = 1;
			gridBagConstraints9.insets = new java.awt.Insets(10, 10, 0, 0);
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.gridheight = 1;
			gridBagConstraints9.gridwidth = 1;
			gridBagConstraints10.gridx = 1;
			gridBagConstraints10.gridy = 1;
			gridBagConstraints10.weightx = 1.0D;
			gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints10.insets = new java.awt.Insets(10, 5, 0, 10);
			gridBagConstraints10.gridwidth = 1;
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridy = 6;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.SOUTHWEST;
			gridBagConstraints11.insets = new java.awt.Insets(5, 10, 10, 0);
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.gridy = 6;
			gridBagConstraints12.anchor = java.awt.GridBagConstraints.SOUTHEAST;
			gridBagConstraints12.insets = new java.awt.Insets(5, 5, 10, 10);
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.gridy = 0;
			gridBagConstraints13.gridwidth = 2;
			gridBagConstraints13.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints13.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints13.insets = new java.awt.Insets(10, 10, 0, 10);
			lblHeadline.setText(rb.getString("panel.admin.host.pleaseenterhost") + ":");
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 2;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints2.insets = new java.awt.Insets(10, 5, 0, 10);
			gridBagConstraints21.gridx = 1;
			gridBagConstraints21.gridy = 2;
			gridBagConstraints21.weightx = 1.0D;
			gridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints21.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints21.insets = new java.awt.Insets(10, 5, 0, 10);
			gridBagConstraints21.gridwidth = 2;
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 5;
			gridBagConstraints3.gridwidth = 2;
			gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints3.insets = new java.awt.Insets(10, 10, 0, 10);
			lblLiveServer = new JLabel();
			lblLiveServer.setText(rb.getString("panel.admin.host.liveServer"));
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints4.insets = new java.awt.Insets(10, 10, 0, 0);
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridy = 4;
			gridBagConstraints41.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints41.gridy = 4;
			gridBagConstraints41.weightx = 1.0;
			gridBagConstraints41.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints41.insets = new java.awt.Insets(5, 5, 0, 10);
			gridBagConstraints41.gridx = 1;
			panContentPane.add(lblHost, gridBagConstraints9);
			panContentPane.add(getTxtHost(), gridBagConstraints10);
			panContentPane.add(getBtnAddHost(), gridBagConstraints11);
			panContentPane.add(getBtnCancelHost(), gridBagConstraints12);
			panContentPane.add(lblHeadline, gridBagConstraints13);
			panContentPane.add(getPanStartPage(), gridBagConstraints3);
			panContentPane.add(lblRedirectUrl, gridBagConstraints);
			panContentPane.add(getTxtRedirectUrl(), gridBagConstraints1);
			panContentPane.add(lblRedirectHost, gridBagConstraints8);
			panContentPane.add(getCbRedirectHost(), gridBagConstraints14);
			panContentPane.add(lblLiveServer, gridBagConstraints4);
			panContentPane.add(getLiveServer(), gridBagConstraints41);
		}
		return panContentPane;
	}

	private JCheckBox getLiveServer() {
		if (liveServer == null) {
			liveServer = new JCheckBox();
		}
		return liveServer;
	}

	/**
	 * This method initializes txtHost	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtHost() {
		if (txtHost == null) {
			txtHost = new JTextField();
			txtHost.setText("Hostname");
			txtHost.setPreferredSize(new java.awt.Dimension(100, 20));
			txtHost.setMinimumSize(new java.awt.Dimension(100, 20));
		}
		return txtHost;
	}

	/**
	 * This method initializes btnAddHost	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnAddHost() {
		if (btnAddHost == null) {
			btnAddHost = new JButton();
			btnAddHost.setText(rb.getString("dialog.add"));
			btnAddHost.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnAddActionPerformed(e);
				}

			});
		}
		return btnAddHost;
	}

	/**
	 * This method initializes btnCancelHost	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnCancelHost() {
		if (btnCancelHost == null) {
			btnCancelHost = new JButton();
			btnCancelHost.setText(rb.getString("dialog.cancel"));
		}
		btnCancelHost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCancelActionPerformed(e);
			}
		});
		return btnCancelHost;
	}

	private void btnCancelActionPerformed(ActionEvent e) {
		this.setVisible(false);
		this.dispose();
	}

	private void btnAddActionPerformed(ActionEvent e) {
		this.getTxtHost().setEnabled(false);
		this.getTxtStartPage().setEnabled(false);
		this.getTxtRedirectUrl().setEnabled(false);
		this.getCbRedirectHost().setEnabled(false);
		this.getBtnAddHost().setEnabled(false);
		this.getBtnCancelHost().setEnabled(false);
		this.getBtnChooseStartPage().setEnabled(false);
		this.getBtnDeleteStartPage().setEnabled(false);
		if (this.mode == CreateNewHostDialog.MODE_ADD_HOST) {
			String newHost = txtHost.getText();
			if ("".equalsIgnoreCase(newHost)) {
				ActionHub.showMessageDialog(rb.getString("panel.admin.host.pleaseenterhost"), JOptionPane.ERROR_MESSAGE);
				return;
			}
			HostValue hv = null;
			SortingListModel model = ((PanHost) parent).getHostListModel();
			boolean found = false;
			for (int i = (model.getSize() - 1); i >= 0; i--) {
				if (((HostValue) ((DropDownHolder) model.getElementAt(i)).getObject()).getHostName().equalsIgnoreCase(newHost)) {
					found = true;
					break;
				}
			}
			if (found) {
				ActionHub.showMessageDialog(rb.getString("dialog.entryAlreadyExists"), JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				hv = communication.createHost(newHost);
			} catch (Exception e1) {
				log.error(e1.getMessage());
			}
			if (this.assignSiteAutomatically && (hv != null)) {
				String currSite = communication.getSiteName();
				communication.setSite(newHost, currSite);
			} else if (hv != null) {
				((PanHost) parent).getPickListData().getLstRightModel().addElement(new DropDownHolder(hv, hv.getHostName()));
			}
			if (txtStartPage.getText().length() > 0) {
				if (this.startPageId != null) {
					communication.setStartPage(newHost, this.startPageId.toString());
				}
			}
			communication.setLiveServer(newHost, liveServer.isSelected());
			model.addElement(new DropDownHolder(hv, hv.getHostName()));
			txtHost.setText("");
			this.currentHost = hv;
		} else if (this.mode == CreateNewHostDialog.MODE_EDIT_HOST) {
			if (this.currentHost != null) {
				if (txtStartPage.getText().length() > 0) {
					communication.setStartPage(this.currentHost.getHostName(), this.startPageId.toString());
				} else {
					communication.setStartPage(this.currentHost.getHostName(), null);
				}
				if (this.assignSiteAutomatically) {
					String currSite = communication.getSiteName();
					communication.setSite(this.currentHost.getHostName(), currSite);
				}
				if (liveServer.isSelected() != this.currentHost.getLiveServer()) {
					communication.setLiveServer(this.currentHost.getHostName(), liveServer.isSelected());
				}
				((PanHost) parent).getHostListModel().fireContentsChanged();
			}
		}
		if (this.currentHost != null) {
			if (this.getTxtRedirectUrl().getText().length() > 0) {
				communication.setRedirectUrl(this.currentHost.getHostName(), this.getTxtRedirectUrl().getText());
			} else {
				communication.setRedirectUrl(this.currentHost.getHostName(), null);
			}
			if (this.getCbRedirectHost().getSelectedIndex() >= 0) {
				Object selectedItem = this.getCbRedirectHost().getSelectedItem();
				if (selectedItem != null && ((DropDownHolder) selectedItem).getObject() != null) {
					String redirectHost = ((HostValue) ((DropDownHolder) selectedItem).getObject()).getHostName();
					if (redirectHost.length() > 0) {
						communication.setRedirectHost(this.currentHost.getHostName(), redirectHost);
					} else {
						communication.setRedirectHost(this.currentHost.getHostName(), null);
					}
				} else {
					communication.setRedirectHost(this.currentHost.getHostName(), null);
				}
			}
			((PanHost) parent).reload();
		}

		this.setVisible(false);
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.content.event.EditpaneFiredListener#editpaneFiredPerformed(de.juwimm.cms.content.event.EditpaneFiredEvent)
	 */
	public void editpaneFiredPerformed(EditpaneFiredEvent ae) {
		Node prop = chooseViewComponentDialog.getProperties();
		String viewId = ((Element) prop.getFirstChild()).getAttribute("viewid");
		this.startPageId = Integer.valueOf(viewId);
		this.txtStartPage.setText(getVcTitle(viewId));
		this.setVisible(true);
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.content.event.EditpaneFiredListener#editpaneCancelPerformed(de.juwimm.cms.content.event.EditpaneFiredEvent)
	 */
	public void editpaneCancelPerformed(EditpaneFiredEvent ae) {
		this.setVisible(true);
	}

	private void chooseStartPageLink() {
		try {
			Element elmLinkRoot = ContentManager.getDomDoc().createElement("linkRoot");
			Element elmInternalLink = ContentManager.getDomDoc().createElement("internalLink");
			CDATASection txtNode = ContentManager.getDomDoc().createCDATASection("");
			elmInternalLink.appendChild(txtNode);
			if (currentHost != null) {
				String viewComponentId = communication.getStartPage(currentHost.getHostName());
				if (!("".equalsIgnoreCase(viewComponentId))) {
					elmInternalLink.setAttribute("viewid", viewComponentId);
				}
			}
			elmLinkRoot.appendChild(elmInternalLink);
			chooseViewComponentDialog.setProperties(elmLinkRoot);
			chooseViewComponentDialog.viewModalUI(true);
			chooseViewComponentDialog.load();
		} catch (Exception exe) {
			log.error("Error adding new vc-id", exe);
		}
	}

	/**
	 * This method initializes txtStartPage	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtStartPage() {
		if (txtStartPage == null) {
			txtStartPage = new JTextField();
			txtStartPage.setEditable(false);
		}
		return txtStartPage;
	}

	/**
	 * This method initializes panStartPage	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanStartPage() {
		if (panStartPage == null) {
			lblStartPage = new JLabel();
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			panStartPage = new JPanel();
			panStartPage.setLayout(new GridBagLayout());
			panStartPage.setBorder(javax.swing.BorderFactory.createTitledBorder(rb.getString("panel.admin.host.startpage")));
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.insets = new java.awt.Insets(5, 5, 0, 5);
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints4.gridwidth = 2;
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints5.insets = new java.awt.Insets(5, 5, 0, 0);
			lblStartPage.setText(rb.getString("panel.admin.host.startpage"));
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.gridy = 1;
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints6.insets = new java.awt.Insets(5, 5, 5, 0);
			gridBagConstraints6.fill = java.awt.GridBagConstraints.NONE;
			gridBagConstraints7.gridx = 2;
			gridBagConstraints7.gridy = 1;
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.NORTHEAST;
			gridBagConstraints7.insets = new java.awt.Insets(5, 5, 5, 5);
			panStartPage.add(getTxtStartPage(), gridBagConstraints4);
			panStartPage.add(lblStartPage, gridBagConstraints5);
			panStartPage.add(getBtnChooseStartPage(), gridBagConstraints6);
			panStartPage.add(getBtnDeleteStartPage(), gridBagConstraints7);
		}
		return panStartPage;
	}

	/**
	 * This method initializes btnChooseStartPage	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnChooseStartPage() {
		if (btnChooseStartPage == null) {
			btnChooseStartPage = new JButton();
			btnChooseStartPage.setText(rb.getString("panel.admin.host.choose"));
		}
		btnChooseStartPage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnChooseStartPageActionPerformed(e);
			}
		});
		return btnChooseStartPage;
	}

	/**
	 * This method initializes btnDeleteStartPage	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnDeleteStartPage() {
		if (btnDeleteStartPage == null) {
			btnDeleteStartPage = new JButton();
			btnDeleteStartPage.setText(rb.getString("dialog.delete"));
		}
		btnDeleteStartPage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDeleteStartPageActionPerformed(e);
			}
		});
		return btnDeleteStartPage;
	}

	public void btnChooseStartPageActionPerformed(ActionEvent e) {
		this.chooseStartPageLink();
	}

	public void btnDeleteStartPageActionPerformed(ActionEvent e) {
		if (txtStartPage.getText().length() > 0) {
			txtStartPage.setText("");
		}
	}

	public String getVcTitle(String vcId) {
		ViewComponentValue vcDao = null;
		try {
			vcDao = this.communication.getViewComponent(new Integer(vcId).intValue());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		String vcTitle = "";
		if (vcDao != null) {
			vcTitle = vcDao.getDisplayLinkName();
		}
		if (vcTitle.length() == 0) {
			vcTitle = vcId;
		}
		return (vcTitle);
	}

	/**
	 * This method initializes txtRedirectUrl	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtRedirectUrl() {
		if (txtRedirectUrl == null) {
			txtRedirectUrl = new JTextField();
		}
		return txtRedirectUrl;
	}

	/**
	 * This method initializes cbRedirectHost	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCbRedirectHost() {
		if (cbRedirectHost == null) {
			cbRedirectHost = new JComboBox();
			cbRedirectHost.setEditable(false);
			HostValue[] hostList = null;
			try {
				if (this.communication.getUser().isMasterRoot()) {
					if (log.isDebugEnabled()) log.debug("MasterRoot: getAllHosts()");
					hostList = this.communication.getAllHosts();
				} else {
					if (log.isDebugEnabled()) log.debug("SiteRoot: getHosts()");
					hostList = this.communication.getHosts();
				}
			} catch (Exception e) {
				log.error("Error on getting hostlist: " + e.getMessage());
			}
			((DefaultComboBoxModel) cbRedirectHost.getModel()).removeAllElements();
			if (hostList != null) {
				DropDownHolder[] ddhArray = new DropDownHolder[hostList.length + 1];
				for (int i = (hostList.length - 1); i >= 0; i--) {
					ddhArray[i] = new DropDownHolder(hostList[i], hostList[i].getHostName());
				}
				ddhArray[hostList.length] = new DropDownHolder(null, "");
				Arrays.sort(ddhArray, localeSensitiveStringComparator);
				cbRedirectHost.setModel(new DefaultComboBoxModel(ddhArray));
			}

		}
		return cbRedirectHost;
	}

	private void updateValues() {
		if (this.currentHost != null) {
			DefaultComboBoxModel model = (DefaultComboBoxModel) this.getCbRedirectHost().getModel();
			// remove current entry
			for (int i = (model.getSize() - 1); i >= 0; i--) {
				HostValue hostValue = (HostValue) ((DropDownHolder) model.getElementAt(i)).getObject();
				if (hostValue != null && hostValue.getHostName().equalsIgnoreCase(this.currentHost.getHostName())) {
					model.removeElementAt(i);
					break;
				}
			}
			// select selected one, if any
			if (this.currentHost.getRedirectHostName() != null && this.currentHost.getRedirectHostName().length() > 0) {
				for (int i = (model.getSize() - 1); i >= 0; i--) {
					HostValue hostValue = (HostValue) ((DropDownHolder) model.getElementAt(i)).getObject();
					if (hostValue != null && hostValue.getHostName().equalsIgnoreCase(this.currentHost.getRedirectHostName())) {
						model.setSelectedItem(model.getElementAt(i));
						break;
					}
				}
			}
			this.getCbRedirectHost().setModel(model);
			this.getTxtRedirectUrl().setText(this.currentHost.getRedirectUrl());
		} else {
			this.getTxtRedirectUrl().setText("");
		}
	}

	private static Comparator localeSensitiveStringComparator = new Comparator() {
		public int compare(Object o1, Object o2) {
			String str1 = o1.toString();
			String str2 = o2.toString();
			Collator collator = Collator.getInstance();
			int result = collator.compare(str1, str2);
			return result;
		}
	};

} //  @jve:decl-index=0:visual-constraint="243,258"
