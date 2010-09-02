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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.gui.controls.ReloadablePanel;
import de.juwimm.cms.gui.table.SiteTableModel;
import de.juwimm.cms.gui.table.TableSorter;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.HostValue;
import de.juwimm.cms.vo.ShortLinkValue;
import de.juwimm.cms.vo.SiteValue;
import de.juwimm.cms.vo.UnitValue;
import de.juwimm.cms.vo.ViewDocumentValue;
import de.juwimm.swing.DropDownHolder;
import de.juwimm.swing.NoResizeScrollPane;
import de.juwimm.swing.PickListData;
import de.juwimm.swing.PickListPanel;
import de.juwimm.swing.SortingListModel;

/**
 * Panel for managing hosts
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * @company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class PanHost extends JPanel implements ReloadablePanel {
	private static Logger log = Logger.getLogger(PanHost.class);
	private JButton btnAddHost = null;
	private JButton btnDeleteHost = null;
	private final SortingListModel hostListModel = new SortingListModel();
	private JPanel self = null;
	private JTable tblSite = null;
	private JScrollPane jScrollPane = null;
	private JList lstHosts = null;
	private JScrollPane lstHostsScrollPane = null;
	private JPanel panManageHosts = null;
	private JPanel panAssignHosts = null;
	private final PickListData pickListData = new PickListData();
	private PickListPanel pickListPanel = null;
	private JButton btnSaveHosts = null;
	private SiteTableModel tblSiteModel = new SiteTableModel();
	private TableSorter tblSiteSorter = null;
	private JButton btnEditHost = null;
	private final Communication communication = ((Communication) getBean(Beans.COMMUNICATION));
	private boolean isMasterRoot = false;

	private JPanel panManageShortLinks = null;
	private JScrollPane spSites = null;
	private JTable tblSitesShortLinks = null;
	private SiteTableModel tblSitesShortLinksModel = new SiteTableModel();
	private TableSorter tblSitesShortLinksSorter = null;
	private JList lstHostsShortLinks = null;
	private JScrollPane spHostsShortLinks = null;
	private final SortingListModel hostsShortLinksListModel = new SortingListModel();
	private JList lstShortLinks = null;
	private JScrollPane spShortLinks = null;
	private final SortingListModel shortLinksListModel = new SortingListModel();
	private JPanel panHostUnit = null;
	private JPanel panShortLink = null;
	private JComboBox cbxUnits = null;
	private JButton btnSaveHostUnit = null;
	private JButton btnSaveShortLink = null;
	private JButton btnNewShortLink = null;
	private JButton btnDeleteShortLink = null;
	private JComboBox cbxViewDocuments = null;
	private JTextField txtShortLink = null;
	private JTextField txtRedirectUrl = null;
	private JButton btnHelp = null;

	/**
	 * This is the default constructor
	 */
	public PanHost() {
		super();
		initialize();
		this.self = this;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new GridBagLayout());
		this.setSize(636, 271);
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.gridheight = 1;
		gridBagConstraints2.insets = new java.awt.Insets(10, 10, 0, 10);
		gridBagConstraints2.weighty = 1.0D;
		gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints2.weightx = 1.0D;
		gridBagConstraints2.gridwidth = 2;
		gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
		this.setPreferredSize(new java.awt.Dimension(636, 271));
		this.setMinimumSize(new java.awt.Dimension(636, 271));
		if (communication.getUser().isMasterRoot()) {
			JTabbedPane panTab = new JTabbedPane();
			this.pickListData.setLeftLabel(rb.getString("panel.admin.host.assignedhosts"));
			this.pickListData.setRightLabel(rb.getString("panel.admin.host.availablehosts"));
			this.pickListPanel = new PickListPanel(this.pickListData);
			this.isMasterRoot = true;
			this.getTblSite().getSelectionModel().addListSelectionListener(new SiteListSelectionListener());
			this.getTblSite().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			this.getTblSitesShortLinks().getSelectionModel().addListSelectionListener(new SitesShortLinksListSelectionListener());
			this.getTblSitesShortLinks().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			panTab.add(getPanManageHosts(), rb.getString("panel.admin.host.managehosts"));
			panTab.add(getPanAssignHosts(), rb.getString("panel.admin.host.assignhosts"));
			panTab.add(getPanManageShortLinks(), rb.getString("panel.admin.host.manageShortLinks"));
			this.add(panTab, gridBagConstraints2);
		} else {
			this.add(getPanManageHosts(), gridBagConstraints2);
		}
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
			btnAddHost.setPreferredSize(new java.awt.Dimension(103, 26));
			btnAddHost.setMaximumSize(new java.awt.Dimension(103, 26));
			btnAddHost.setMinimumSize(new java.awt.Dimension(103, 26));
		}
		btnAddHost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreateNewHostDialog hd = new CreateNewHostDialog(self, null, (!isMasterRoot));
				hd.setLocationRelativeTo(UIConstants.getMainFrame());
				hd.setVisible(true);
			}
		});
		return btnAddHost;
	}

	/**
	 * This method initializes btnDeleteHost	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnDeleteHost() {
		if (btnDeleteHost == null) {
			btnDeleteHost = new JButton();
			btnDeleteHost.setText(rb.getString("dialog.delete"));
			btnDeleteHost.setEnabled(false);
			btnDeleteHost.setMaximumSize(new java.awt.Dimension(103, 26));
			btnDeleteHost.setMinimumSize(new java.awt.Dimension(103, 26));
			btnDeleteHost.setPreferredSize(new java.awt.Dimension(103, 26));
			btnDeleteHost.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int[] count = lstHosts.getSelectedIndices();
					for (int i = count.length - 1; i > -1; i--) {
						HostValue currentHost = (HostValue) ((DropDownHolder) hostListModel.getElementAt(count[i])).getObject();
						hostListModel.removeElementAt(count[i]);
						communication.removeHost(currentHost.getHostName());
						if (getTblSite().getSelectedRow() != -1) {
							SiteValue vo = (SiteValue) tblSiteSorter.getValueAt(getTblSite().getSelectedRow(), 2);
							updateValues(vo);
						} else {
							updateValues(null);
						}
					}
				}
			});
		}
		return btnDeleteHost;
	}

	/**
	 * This method initializes lstHosts	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getLstHosts() {
		if (lstHosts == null) {
			lstHosts = new JList(this.hostListModel);
			lstHosts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			lstHosts.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (lstHosts.isSelectionEmpty()) {
						btnDeleteHost.setEnabled(false);
						btnEditHost.setEnabled(false);
					} else {
						btnDeleteHost.setEnabled(true);
						btnEditHost.setEnabled(true);
					}
				}
			});
		}
		return lstHosts;
	}

	/**
	 * @return Returns the hostListModel.
	 */
	public SortingListModel getHostListModel() {
		return (this.hostListModel);
	}

	/**
	 * This method initializes panManageHosts	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanManageHosts() {
		if (panManageHosts == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			panManageHosts = new JPanel();
			panManageHosts.setLayout(new GridBagLayout());
			gridBagConstraints3.gridx = 3;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.gridheight = 1;
			gridBagConstraints3.insets = new java.awt.Insets(5, 5, 0, 5);
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.weightx = 0.0D;
			gridBagConstraints4.gridx = 3;
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.gridheight = 1;
			gridBagConstraints4.insets = new java.awt.Insets(5, 5, 0, 5);
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.weightx = 0.0D;
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 3;
			gridBagConstraints5.gridheight = 1;
			gridBagConstraints5.insets = new java.awt.Insets(5, 5, 0, 0);
			gridBagConstraints5.weighty = 0.0D;
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints5.weightx = 0.0D;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.gridheight = 3;
			gridBagConstraints6.weightx = 2.0D;
			gridBagConstraints6.weighty = 2.0D;
			gridBagConstraints6.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints6.insets = new java.awt.Insets(5, 5, 5, 0);
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints6.gridwidth = 3;
			gridBagConstraints1.gridx = 3;
			gridBagConstraints1.gridy = 2;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
			panManageHosts.setBorder(javax.swing.BorderFactory.createTitledBorder(rb.getString("panel.admin.host.managehosts")));
			panManageHosts.setPreferredSize(new java.awt.Dimension(400, 150));
			panManageHosts.setMaximumSize(new java.awt.Dimension(400, 150));
			panManageHosts.setMinimumSize(new java.awt.Dimension(400, 150));
			panManageHosts.add(getBtnAddHost(), gridBagConstraints3);
			panManageHosts.add(getBtnDeleteHost(), gridBagConstraints4);
			panManageHosts.add(getBtnEditHost(), gridBagConstraints1);
			panManageHosts.add(getLstHostsScrollPane(), gridBagConstraints6);
			//refreshHostList();
		}
		return panManageHosts;
	}

	/**
	 * This method initializes panAssignHosts	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanAssignHosts() {
		if (panAssignHosts == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			panAssignHosts = new JPanel();
			panAssignHosts.setLayout(new GridBagLayout());
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.gridy = 0;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.weighty = 1.0;
			gridBagConstraints9.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints9.insets = new java.awt.Insets(5, 5, 5, 0);
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.gridy = 0;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints11.insets = new java.awt.Insets(5, 5, 0, 5);
			gridBagConstraints11.weightx = 6.0D;
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.gridy = 1;
			gridBagConstraints13.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints13.insets = new java.awt.Insets(5, 5, 5, 0);
			panAssignHosts.setBorder(javax.swing.BorderFactory.createTitledBorder(rb.getString("panel.admin.host.assignhosts")));
			panAssignHosts.add(getJScrollPane(), gridBagConstraints9);
			panAssignHosts.add(this.pickListPanel, gridBagConstraints11);
			panAssignHosts.add(getBtnSaveHosts(), gridBagConstraints13);
			this.pickListPanel.setEnabled(true);
		}
		return panAssignHosts;
	}

	public void reload() {
		if (log.isDebugEnabled()) log.debug("reload ...");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					setCursor(new Cursor(Cursor.WAIT_CURSOR));
					if (communication.getUser().isMasterRoot()) {
						reloadSites();
					}
					refreshHostList();
				} catch (Exception exe) {
					log.error("Reloading Error", exe);
				}
				setCursor(Cursor.getDefaultCursor());
			}
		});
	}

	private void reloadSites() {
		updateValues(null);
		tblSiteModel = new SiteTableModel();
		tblSiteSorter = new TableSorter(tblSiteModel, this.getTblSite().getTableHeader());
		SiteValue[] sites = communication.getAllSites();
		tblSiteModel.addRows(sites);
		this.getTblSite().getSelectionModel().clearSelection();
		this.getTblSite().setModel(tblSiteSorter);
		tblSitesShortLinksModel = new SiteTableModel();
		tblSitesShortLinksSorter = new TableSorter(tblSitesShortLinksModel, this.getTblSitesShortLinks().getTableHeader());
		tblSitesShortLinksModel.addRows(sites);
		this.getTblSitesShortLinks().getSelectionModel().clearSelection();
		this.getTblSitesShortLinks().setModel(tblSitesShortLinksSorter);
		siteSelected(false);
	}

	private void updateValues(SiteValue vo) {
		this.pickListData.getLstLeftModel().removeAllElements();
		this.pickListData.getLstRightModel().removeAllElements();
		if (vo != null) {
			HostValue[] assignedHosts = null;
			assignedHosts = this.communication.getHostsForSite(vo.getSiteId());
			if (assignedHosts != null) {
				for (int i = (assignedHosts.length - 1); i >= 0; i--) {
					this.pickListData.getLstLeftModel().addElement(new DropDownHolder(assignedHosts[i], assignedHosts[i].getHostName()));
				}
			}
		}
		HostValue[] allUnassignedHosts = null;
		allUnassignedHosts = this.communication.getAllUnassignedHosts();
		if (allUnassignedHosts != null) {
			for (int i = (allUnassignedHosts.length - 1); i >= 0; i--) {
				this.pickListData.getLstRightModel().addElement(new DropDownHolder(allUnassignedHosts[i], allUnassignedHosts[i].getHostName()));
			}
		}
	}

	private void siteSelected(boolean val) {
		this.pickListPanel.setEnabled(val);
	}

	/**
	 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
	 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
	 * @version $Id$
	 */
	private class SiteListSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting()) {
				return;
			}
			if (getTblSite().getSelectedRow() >= 0) {
				if (pickListData.isModified()) {
					int i = JOptionPane.showConfirmDialog(self, rb.getString("dialog.wantToSave"), rb.getString("dialog.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (i == JOptionPane.YES_OPTION) {
						setCursor(new Cursor(Cursor.WAIT_CURSOR));
						save(e.getLastIndex());
					}
				}
				siteSelected(false);
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				SiteValue siteValue = (SiteValue) tblSiteSorter.getValueAt(getTblSite().getSelectedRow(), 2);
				updateValues(siteValue);
				siteSelected(true);
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			} else {
				siteSelected(false);
			}
			pickListData.setModified(false);
		}
	}

	/**
	 * This method initializes btnSaveHosts, the button on the page for associate hosts to a site
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnSaveHosts() {
		if (btnSaveHosts == null) {
			btnSaveHosts = new JButton();
			btnSaveHosts.setText(rb.getString("dialog.save"));
			btnSaveHosts.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					save();
				}
			});
		}
		return btnSaveHosts;
	}

	private void refreshHostList() {
		HostValue[] hv = null;
		try {
			if (communication.getUser().isMasterRoot()) {
				if (log.isDebugEnabled()) log.debug("MasterRoot: getAllHosts()");
				hv = this.communication.getAllHosts();
			} else {
				if (log.isDebugEnabled()) log.debug("SiteRoot: getHosts()");
				hv = this.communication.getHosts();
			}
		} catch (Exception e) {
			log.error("Error on getting hostlist: " + e.getMessage());
		}
		this.hostListModel.removeAllElements();
		if (hv != null) {
			for (int i = (hv.length - 1); i >= 0; i--) {
				this.hostListModel.addElement(new DropDownHolder(hv[i], hv[i].getHostName()));
			}
		}
	}

	/**
	 * @return Returns the lstHostsScrollPane.
	 */
	public JScrollPane getLstHostsScrollPane() {
		if (lstHostsScrollPane == null) {
			lstHostsScrollPane = new NoResizeScrollPane(getLstHosts());
		}
		return this.lstHostsScrollPane;
	}

	/**
	 * This method initializes btnEditHost	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnEditHost() {
		if (btnEditHost == null) {
			btnEditHost = new JButton();
			btnEditHost.setText(rb.getString("dialog.change"));
			btnEditHost.setEnabled(false);
			btnEditHost.setPreferredSize(new java.awt.Dimension(103, 26));
			btnEditHost.setMaximumSize(new java.awt.Dimension(103, 26));
			btnEditHost.setMinimumSize(new java.awt.Dimension(103, 26));
		}
		btnEditHost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!(lstHosts.isSelectionEmpty())) {
					HostValue currentHost = ((HostValue) ((DropDownHolder) lstHosts.getSelectedValue()).getObject());
					CreateNewHostDialog hd = new CreateNewHostDialog(self, currentHost, (!isMasterRoot));
					hd.setLocationRelativeTo(UIConstants.getMainFrame());
					hd.setVisible(true);
				}
			}
		});
		return btnEditHost;
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.gui.controls.ReloadablePanel#save()
	 */
	public void save() {
		SwingUtilities.invokeLater(new SaveRunner(-1));
	}

	public void save(int row) {
		SwingUtilities.invokeLater(new SaveRunner(row));
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.gui.controls.UnloadablePanel#unload()
	 */
	public void unload() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					setCursor(new Cursor(Cursor.WAIT_CURSOR));
				} catch (Exception exe) {
					log.error("Reloading Error", exe);
				}
				setCursor(Cursor.getDefaultCursor());
			}
		});
	}

	/**
	 * This method initializes tblSite	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getTblSite() {
		if (tblSite == null) {
			tblSite = new JTable();
		}
		return tblSite;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getTblSite());
			jScrollPane.setPreferredSize(new java.awt.Dimension(200, 419));
			jScrollPane.setMinimumSize(new java.awt.Dimension(200, 419));
		}
		return jScrollPane;
	}

	public PickListData getPickListData() {
		return this.pickListData;
	}

	/**
	 * 
	 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
	 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
	 * @version $Id$
	 */
	private class SaveRunner implements Runnable {
		private int rowToSave = -1;

		public SaveRunner(int row) {
			this.rowToSave = row;
		}

		public void run() {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			if (pickListData.isModified()) {
				if (this.rowToSave == -1) {
					this.rowToSave = getTblSite().getSelectedRow();
				}
				SiteValue selSite = (SiteValue) tblSiteSorter.getValueAt(this.rowToSave, 2);
				Iterator it = pickListData.getLstLeftModel().iterator();
				while (it.hasNext()) {
					HostValue currHost = ((HostValue) ((DropDownHolder) it.next()).getObject());
					communication.setSite(currHost.getHostName(), selSite.getSiteId());
				}
				it = pickListData.getLstRightModel().iterator();
				while (it.hasNext()) {
					HostValue currHost = ((HostValue) ((DropDownHolder) it.next()).getObject());
					communication.removeSiteFromHost(currHost.getHostName());
				}
			}
			pickListData.setModified(false);
			setCursor(Cursor.getDefaultCursor());
		}
	}

	/**
	 * This method initializes panManageShortLinks	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanManageShortLinks() {
		if (panManageShortLinks == null) {
			GridBagConstraints sitesTableConstraint = new GridBagConstraints();
			sitesTableConstraint.gridx = 0;
			sitesTableConstraint.gridy = 0;
			sitesTableConstraint.gridheight = 2;
			sitesTableConstraint.weightx = 1.0;
			sitesTableConstraint.weighty = 1.0;
			sitesTableConstraint.fill = java.awt.GridBagConstraints.BOTH;
			sitesTableConstraint.anchor = java.awt.GridBagConstraints.NORTHWEST;
			sitesTableConstraint.insets = new java.awt.Insets(5, 5, 5, 0);
			GridBagConstraints unitConstraint = new GridBagConstraints();
			unitConstraint.gridx = 1;
			unitConstraint.gridy = 0;
			unitConstraint.weightx = 2.0;
			unitConstraint.weighty = 1.0;
			unitConstraint.fill = java.awt.GridBagConstraints.BOTH;
			unitConstraint.anchor = java.awt.GridBagConstraints.NORTHWEST;
			unitConstraint.insets = new java.awt.Insets(5, 5, 0, 5);
			GridBagConstraints shortLinkConstraint = new GridBagConstraints();
			shortLinkConstraint.gridx = 1;
			shortLinkConstraint.gridy = 1;
			shortLinkConstraint.weightx = 2.0;
			shortLinkConstraint.weighty = 1.0;
			shortLinkConstraint.fill = java.awt.GridBagConstraints.BOTH;
			shortLinkConstraint.anchor = java.awt.GridBagConstraints.NORTHWEST;
			shortLinkConstraint.insets = new java.awt.Insets(5, 5, 5, 5);
			panManageShortLinks = new JPanel();
			panManageShortLinks.setLayout(new GridBagLayout());
			panManageShortLinks.setBorder(javax.swing.BorderFactory.createTitledBorder(rb.getString("panel.admin.host.manageShortLinks")));
			panManageShortLinks.add(getSpSites(), sitesTableConstraint);
			panManageShortLinks.add(getPanHostUnit(), unitConstraint);
			panManageShortLinks.add(getPanShortLink(), shortLinkConstraint);
		}
		return panManageShortLinks;
	}

	/**
	 * This method initializes spSites	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getSpSites() {
		if (spSites == null) {
			spSites = new JScrollPane();
			spSites.setViewportView(getTblSitesShortLinks());
			spSites.setPreferredSize(new java.awt.Dimension(200, 419));
			spSites.setMinimumSize(new java.awt.Dimension(200, 419));
		}
		return spSites;
	}

	/**
	 * This method initializes tblSitesShortLinks	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getTblSitesShortLinks() {
		if (tblSitesShortLinks == null) {
			tblSitesShortLinks = new JTable();
		}
		return tblSitesShortLinks;
	}

	/**
	 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
	 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
	 * @version $Id$
	 */
	private class SitesShortLinksListSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting()) {
				return;
			}
			if (getTblSitesShortLinks().getSelectedRow() >= 0) {
				siteShortLinksSelected(false);
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				SiteValue siteValue = (SiteValue) tblSitesShortLinksSorter.getValueAt(getTblSitesShortLinks().getSelectedRow(), 2);
				updateShortLinkValues(siteValue);
				siteShortLinksSelected(true);
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			} else {
				siteShortLinksSelected(false);
				getBtnNewShortLink().setEnabled(false);
			}
		}
	}

	/**
	 * This method initializes lstHostsShortLinks	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getLstHostsShortLinks() {
		if (this.lstHostsShortLinks == null) {
			this.lstHostsShortLinks = new JList(this.hostsShortLinksListModel);
			this.lstHostsShortLinks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			this.lstHostsShortLinks.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (lstHostsShortLinks.isSelectionEmpty()) {
						getCbUnits().setSelectedIndex(0);
					} else {
						HostValue selectedHost = (HostValue) ((DropDownHolder) lstHostsShortLinks.getSelectedValue()).getObject();
						if (selectedHost.getUnitId() != null) {
							// search for unit in combobox and select it
							DefaultComboBoxModel model = (DefaultComboBoxModel) getCbUnits().getModel();
							for (int i = (model.getSize() - 1); i >= 0; i--) {
								UnitValue unit = (UnitValue) ((DropDownHolder) model.getElementAt(i)).getObject();
								if (unit != null && unit.getUnitId() == selectedHost.getUnitId().intValue()) {
									model.setSelectedItem(model.getElementAt(i));
									break;
								}
							}
						} else {
							getCbUnits().setSelectedIndex(0);
						}
					}
					getCbUnits().setEnabled(!lstHostsShortLinks.isSelectionEmpty());
					getBtnSaveHostUnit().setEnabled(!lstHostsShortLinks.isSelectionEmpty());
				}
			});
			this.lstHostsShortLinks.setEnabled(false);
		}
		return this.lstHostsShortLinks;
	}

	/**
	 * This method initializes lstShortLinks	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getLstShortLinks() {
		if (this.lstShortLinks == null) {
			this.lstShortLinks = new JList(this.shortLinksListModel);
			this.lstShortLinks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			this.lstShortLinks.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (lstShortLinks.isSelectionEmpty()) {
						getCbViewDocuments().setSelectedIndex(0);
						getTxtShortLink().setText("");
						getTxtRedirectUrl().setText("");
					} else {
						ShortLinkValue selectedShortLink = (ShortLinkValue) ((DropDownHolder) lstShortLinks.getSelectedValue()).getObject();
						if (selectedShortLink.getViewDocumentId() != null) {
							// search for viewDocument in combobox and select it
							DefaultComboBoxModel model = (DefaultComboBoxModel) getCbViewDocuments().getModel();
							for (int i = (model.getSize() - 1); i >= 0; i--) {
								ViewDocumentValue viewDocument = (ViewDocumentValue) ((DropDownHolder) model.getElementAt(i)).getObject();
								if (viewDocument != null && viewDocument.getViewDocumentId() == selectedShortLink.getViewDocumentId()) {
									model.setSelectedItem(model.getElementAt(i));
									break;
								}
							}
						} else {
							getCbViewDocuments().setSelectedIndex(0);
						}
						getTxtShortLink().setText(selectedShortLink.getShortLink());
						getTxtRedirectUrl().setText(selectedShortLink.getRedirectUrl());
					}
					getCbViewDocuments().setEnabled(!lstShortLinks.isSelectionEmpty());
					getTxtShortLink().setEnabled(!lstShortLinks.isSelectionEmpty());
					getTxtRedirectUrl().setEnabled(!lstShortLinks.isSelectionEmpty());
					getBtnSaveShortLink().setEnabled(!lstShortLinks.isSelectionEmpty());
					getBtnDeleteShortLink().setEnabled(!lstShortLinks.isSelectionEmpty());
				}
			});
			this.lstShortLinks.setEnabled(false);
		}
		return this.lstShortLinks;
	}

	/**
	 * @return Returns the spHostsShortLinks.
	 */
	public JScrollPane getSpHostsShortLinks() {
		if (spHostsShortLinks == null) {
			spHostsShortLinks = new NoResizeScrollPane(getLstHostsShortLinks());
		}
		return this.spHostsShortLinks;
	}

	/**
	 * @return Returns the spShortLinks.
	 */
	public JScrollPane getSpShortLinks() {
		if (spShortLinks == null) {
			spShortLinks = new NoResizeScrollPane(getLstShortLinks());
		}
		return this.spShortLinks;
	}

	/**
	 * This method initializes panHostUnit	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanHostUnit() {
		if (panHostUnit == null) {
			GridBagConstraints hostListConstraint = new GridBagConstraints();
			hostListConstraint.gridx = 0;
			hostListConstraint.gridy = 0;
			hostListConstraint.weightx = 1.0;
			hostListConstraint.weighty = 1.0;
			hostListConstraint.gridwidth = 2;
			hostListConstraint.fill = java.awt.GridBagConstraints.BOTH;
			hostListConstraint.anchor = java.awt.GridBagConstraints.NORTHWEST;
			hostListConstraint.insets = new java.awt.Insets(5, 5, 0, 5);
			GridBagConstraints lblUnitConstraint = new GridBagConstraints();
			lblUnitConstraint.gridx = 0;
			lblUnitConstraint.gridy = 1;
			lblUnitConstraint.weightx = 0.0;
			lblUnitConstraint.weighty = 0.0;
			lblUnitConstraint.fill = java.awt.GridBagConstraints.HORIZONTAL;
			lblUnitConstraint.anchor = java.awt.GridBagConstraints.NORTHWEST;
			lblUnitConstraint.insets = new java.awt.Insets(7, 7, 0, 5);
			GridBagConstraints unitComboConstraint = new GridBagConstraints();
			unitComboConstraint.gridx = 1;
			unitComboConstraint.gridy = 1;
			unitComboConstraint.weightx = 1.0;
			unitComboConstraint.weighty = 0.0;
			unitComboConstraint.fill = java.awt.GridBagConstraints.HORIZONTAL;
			unitComboConstraint.anchor = java.awt.GridBagConstraints.NORTHWEST;
			unitComboConstraint.insets = new java.awt.Insets(5, 5, 0, 5);
			GridBagConstraints btnSaveConstraint = new GridBagConstraints();
			btnSaveConstraint.gridx = 0;
			btnSaveConstraint.gridy = 2;
			btnSaveConstraint.weightx = 0.0;
			btnSaveConstraint.weighty = 0.0;
			btnSaveConstraint.fill = java.awt.GridBagConstraints.NONE;
			btnSaveConstraint.anchor = java.awt.GridBagConstraints.NORTHWEST;
			btnSaveConstraint.insets = new java.awt.Insets(5, 5, 5, 5);
			panHostUnit = new JPanel();
			panHostUnit.setLayout(new GridBagLayout());
			panHostUnit.setBorder(javax.swing.BorderFactory.createTitledBorder(rb.getString("panel.admin.host.assignUnitToHost")));
			panHostUnit.add(getSpHostsShortLinks(), hostListConstraint);
			panHostUnit.add(new JLabel("Unit: "), lblUnitConstraint);
			panHostUnit.add(getCbUnits(), unitComboConstraint);
			panHostUnit.add(getBtnSaveHostUnit(), btnSaveConstraint);
		}
		return panHostUnit;
	}

	/**
	 * This method initializes panShortLink	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanShortLink() {
		if (panShortLink == null) {
			GridBagConstraints btnHelpConstraint = new GridBagConstraints();
			btnHelpConstraint.gridx = 0;
			btnHelpConstraint.gridy = 0;
			btnHelpConstraint.gridwidth = 1;
			btnHelpConstraint.fill = java.awt.GridBagConstraints.NONE;
			btnHelpConstraint.anchor = java.awt.GridBagConstraints.NORTHWEST;
			btnHelpConstraint.insets = new java.awt.Insets(5, 5, 5, 5);
			GridBagConstraints shortLinkListConstraint = new GridBagConstraints();
			shortLinkListConstraint.gridx = 0;
			shortLinkListConstraint.gridy = 1;
			shortLinkListConstraint.weightx = 1.0;
			shortLinkListConstraint.weighty = 1.0;
			shortLinkListConstraint.gridwidth = 3;
			shortLinkListConstraint.fill = java.awt.GridBagConstraints.BOTH;
			shortLinkListConstraint.anchor = java.awt.GridBagConstraints.NORTHWEST;
			shortLinkListConstraint.insets = new java.awt.Insets(5, 5, 5, 5);
			GridBagConstraints lblViewDocumentConstraint = new GridBagConstraints();
			lblViewDocumentConstraint.gridx = 0;
			lblViewDocumentConstraint.gridy = 2;
			lblViewDocumentConstraint.weightx = 0.0;
			lblViewDocumentConstraint.weighty = 0.0;
			lblViewDocumentConstraint.fill = java.awt.GridBagConstraints.HORIZONTAL;
			lblViewDocumentConstraint.anchor = java.awt.GridBagConstraints.NORTHWEST;
			lblViewDocumentConstraint.insets = new java.awt.Insets(7, 7, 0, 5);
			GridBagConstraints viewDocumentComboConstraint = new GridBagConstraints();
			viewDocumentComboConstraint.gridx = 1;
			viewDocumentComboConstraint.gridy = 2;
			viewDocumentComboConstraint.weightx = 1.0;
			viewDocumentComboConstraint.weighty = 0.0;
			viewDocumentComboConstraint.gridwidth = 2;
			viewDocumentComboConstraint.fill = java.awt.GridBagConstraints.HORIZONTAL;
			viewDocumentComboConstraint.anchor = java.awt.GridBagConstraints.NORTHWEST;
			viewDocumentComboConstraint.insets = new java.awt.Insets(5, 5, 0, 5);
			GridBagConstraints btnSaveConstraint = new GridBagConstraints();
			btnSaveConstraint.gridx = 0;
			btnSaveConstraint.gridy = 5;
			btnSaveConstraint.weightx = 0.0;
			btnSaveConstraint.weighty = 0.0;
			btnSaveConstraint.fill = java.awt.GridBagConstraints.NONE;
			btnSaveConstraint.anchor = java.awt.GridBagConstraints.NORTHWEST;
			btnSaveConstraint.insets = new java.awt.Insets(5, 5, 5, 5);
			GridBagConstraints btnNewConstraint = new GridBagConstraints();
			btnNewConstraint.gridx = 1;
			btnNewConstraint.gridy = 5;
			btnNewConstraint.weightx = 0.0;
			btnNewConstraint.weighty = 0.0;
			btnNewConstraint.fill = java.awt.GridBagConstraints.NONE;
			btnNewConstraint.anchor = java.awt.GridBagConstraints.NORTHWEST;
			btnNewConstraint.insets = new java.awt.Insets(5, 5, 5, 5);
			GridBagConstraints btnDeleteConstraint = new GridBagConstraints();
			btnDeleteConstraint.gridx = 2;
			btnDeleteConstraint.gridy = 5;
			btnDeleteConstraint.weightx = 0.0;
			btnDeleteConstraint.weighty = 0.0;
			btnDeleteConstraint.fill = java.awt.GridBagConstraints.NONE;
			btnDeleteConstraint.anchor = java.awt.GridBagConstraints.NORTHWEST;
			btnDeleteConstraint.insets = new java.awt.Insets(5, 5, 5, 5);
			GridBagConstraints lblShortLinkConstraint = new GridBagConstraints();
			lblShortLinkConstraint.gridx = 0;
			lblShortLinkConstraint.gridy = 3;
			lblShortLinkConstraint.weightx = 0.0;
			lblShortLinkConstraint.weighty = 0.0;
			lblShortLinkConstraint.fill = java.awt.GridBagConstraints.NONE;
			lblShortLinkConstraint.anchor = java.awt.GridBagConstraints.NORTHWEST;
			lblShortLinkConstraint.insets = new java.awt.Insets(7, 7, 0, 0);
			GridBagConstraints txtShortLinkConstraint = new GridBagConstraints();
			txtShortLinkConstraint.gridx = 1;
			txtShortLinkConstraint.gridy = 3;
			txtShortLinkConstraint.weightx = 1.0;
			txtShortLinkConstraint.weighty = 0.0;
			txtShortLinkConstraint.gridwidth = 2;
			txtShortLinkConstraint.fill = java.awt.GridBagConstraints.HORIZONTAL;
			txtShortLinkConstraint.anchor = java.awt.GridBagConstraints.NORTHWEST;
			txtShortLinkConstraint.insets = new java.awt.Insets(5, 5, 0, 5);
			GridBagConstraints lblRedirectUrlConstraint = new GridBagConstraints();
			lblRedirectUrlConstraint.gridx = 0;
			lblRedirectUrlConstraint.gridy = 4;
			lblRedirectUrlConstraint.weightx = 0.0;
			lblRedirectUrlConstraint.weighty = 0.0;
			lblRedirectUrlConstraint.fill = java.awt.GridBagConstraints.NONE;
			lblRedirectUrlConstraint.anchor = java.awt.GridBagConstraints.NORTHWEST;
			lblRedirectUrlConstraint.insets = new java.awt.Insets(7, 7, 0, 0);
			GridBagConstraints txtRedirectUrlConstraint = new GridBagConstraints();
			txtRedirectUrlConstraint.gridx = 1;
			txtRedirectUrlConstraint.gridy = 4;
			txtRedirectUrlConstraint.weightx = 1.0;
			txtRedirectUrlConstraint.weighty = 0.0;
			txtRedirectUrlConstraint.gridwidth = 2;
			txtRedirectUrlConstraint.fill = java.awt.GridBagConstraints.HORIZONTAL;
			txtRedirectUrlConstraint.anchor = java.awt.GridBagConstraints.NORTHWEST;
			txtRedirectUrlConstraint.insets = new java.awt.Insets(5, 5, 0, 5);
			panShortLink = new JPanel();
			panShortLink.setLayout(new GridBagLayout());
			panShortLink.setBorder(javax.swing.BorderFactory.createTitledBorder(rb.getString("panel.admin.host.editShortLink")));
			panShortLink.add(getBtnHelp(), btnHelpConstraint);
			panShortLink.add(getSpShortLinks(), shortLinkListConstraint);
			panShortLink.add(new JLabel(rb.getString("panel.admin.host.language")), lblViewDocumentConstraint);
			panShortLink.add(getCbViewDocuments(), viewDocumentComboConstraint);
			panShortLink.add(new JLabel("ShortLink: "), lblShortLinkConstraint);
			panShortLink.add(getTxtShortLink(), txtShortLinkConstraint);
			panShortLink.add(new JLabel("RedirectUrl: "), lblRedirectUrlConstraint);
			panShortLink.add(getTxtRedirectUrl(), txtRedirectUrlConstraint);
			panShortLink.add(getBtnSaveShortLink(), btnSaveConstraint);
			panShortLink.add(getBtnNewShortLink(), btnNewConstraint);
			panShortLink.add(getBtnDeleteShortLink(), btnDeleteConstraint);
		}
		return panShortLink;
	}

	private void siteShortLinksSelected(boolean val) {
		this.getLstHostsShortLinks().setEnabled(val);
		this.getLstShortLinks().setEnabled(val);
		this.getLstHostsShortLinks().clearSelection();
		this.getLstShortLinks().clearSelection();
		this.getCbUnits().setEnabled(false);
		this.getBtnSaveHostUnit().setEnabled(false);
		this.getCbViewDocuments().setEnabled(false);
		this.getBtnSaveShortLink().setEnabled(false);
		this.getBtnDeleteShortLink().setEnabled(false);
		this.getBtnNewShortLink().setEnabled(true);
	}

	private void updateShortLinkValues(SiteValue vo) {
		this.hostsShortLinksListModel.removeAllElements();
		this.shortLinksListModel.removeAllElements();
		if (vo != null) {
			HostValue[] assignedHosts = null;
			assignedHosts = this.communication.getHostsForSite(vo.getSiteId());
			if (assignedHosts != null) {
				for (int i = (assignedHosts.length - 1); i >= 0; i--) {
					this.hostsShortLinksListModel.addElement(new DropDownHolder(assignedHosts[i], assignedHosts[i].getHostName()));
				}
			}
			ShortLinkValue[] shortLinks = null;
			shortLinks = this.communication.getAllShortLinks4Site(vo.getSiteId());
			if (shortLinks != null) {
				for (int i = (shortLinks.length - 1); i >= 0; i--) {
					this.shortLinksListModel.addElement(new DropDownHolder(shortLinks[i], shortLinks[i].getShortLink()));
				}
			}
			UnitValue[] units4SelectedSite = this.communication.getAllUnits4Site(vo.getSiteId());
			((DefaultComboBoxModel) this.getCbUnits().getModel()).removeAllElements();
			if (units4SelectedSite != null) {
				DropDownHolder[] ddhArray = new DropDownHolder[units4SelectedSite.length + 1];
				for (int i = (units4SelectedSite.length - 1); i >= 0; i--) {
					ddhArray[i] = new DropDownHolder(units4SelectedSite[i], units4SelectedSite[i].getName());
				}
				ddhArray[units4SelectedSite.length] = new DropDownHolder(null, "");
				Arrays.sort(ddhArray, localeSensitiveStringComparator);
				this.getCbUnits().setModel(new DefaultComboBoxModel(ddhArray));
			}
			ViewDocumentValue[] viewDocuments4SelectedSite = this.communication.getAllViewDocuments4Site(vo.getSiteId());
			((DefaultComboBoxModel) this.getCbViewDocuments().getModel()).removeAllElements();
			if (viewDocuments4SelectedSite != null) {
				DropDownHolder[] ddhArray = new DropDownHolder[viewDocuments4SelectedSite.length];
				for (int i = (viewDocuments4SelectedSite.length - 1); i >= 0; i--) {
					ddhArray[i] = new DropDownHolder(viewDocuments4SelectedSite[i], viewDocuments4SelectedSite[i].getViewType() + ": " + viewDocuments4SelectedSite[i].getLanguage());
				}
				Arrays.sort(ddhArray, localeSensitiveStringComparator);
				this.getCbViewDocuments().setModel(new DefaultComboBoxModel(ddhArray));
			}
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

	private JComboBox getCbUnits() {
		if (this.cbxUnits == null) {
			this.cbxUnits = new JComboBox(new DefaultComboBoxModel());
			this.cbxUnits.setEditable(false);
			this.cbxUnits.setEnabled(false);
		}
		return this.cbxUnits;
	}

	/**
	 * This method initializes btnSaveHostUnit
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnSaveHostUnit() {
		if (this.btnSaveHostUnit == null) {
			this.btnSaveHostUnit = new JButton();
			this.btnSaveHostUnit.setText(rb.getString("dialog.save"));
			this.btnSaveHostUnit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					saveHostUnit();
				}
			});
			this.btnSaveHostUnit.setEnabled(false);
		}
		return this.btnSaveHostUnit;
	}

	/**
	 * This method initializes btnSaveShortLink
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnSaveShortLink() {
		if (this.btnSaveShortLink == null) {
			this.btnSaveShortLink = new JButton();
			this.btnSaveShortLink.setText(rb.getString("dialog.save"));
			this.btnSaveShortLink.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					validateAndSaveShortLink();
				}
			});
			this.btnSaveShortLink.setEnabled(false);
		}
		return this.btnSaveShortLink;
	}

	private JButton getBtnNewShortLink() {
		if (this.btnNewShortLink == null) {
			this.btnNewShortLink = new JButton();
			this.btnNewShortLink.setText(rb.getString("dialog.new"));
			this.btnNewShortLink.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					newShortLink();
				}
			});
			this.btnNewShortLink.setEnabled(false);
		}
		return this.btnNewShortLink;
	}

	private JButton getBtnDeleteShortLink() {
		if (this.btnDeleteShortLink == null) {
			this.btnDeleteShortLink = new JButton();
			this.btnDeleteShortLink.setText(rb.getString("dialog.delete"));
			this.btnDeleteShortLink.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int ret = JOptionPane.showConfirmDialog(UIConstants.getMainFrame(), rb.getString("comm.removevc.header_offline"), rb.getString("dialog.general.askDelete"), JOptionPane.YES_NO_OPTION);
					if (ret == JOptionPane.OK_OPTION) {
						deleteShortLink();
					}
				}
			});
			this.btnDeleteShortLink.setEnabled(false);
		}
		return this.btnDeleteShortLink;
	}

	private void saveHostUnit() {
		if (!this.getLstHostsShortLinks().isSelectionEmpty()) {
			HostValue selectedHost = (HostValue) ((DropDownHolder) lstHostsShortLinks.getSelectedValue()).getObject();
			UnitValue unit = (UnitValue) ((DropDownHolder) getCbUnits().getSelectedItem()).getObject();
			if (unit == null) {
				selectedHost.setUnitId(null);
			} else {
				selectedHost.setUnitId(unit.getUnitId());
			}
			selectedHost = this.communication.saveHost(selectedHost);
		}
	}

	private void validateAndSaveShortLink() {
		boolean hasErrors = false;
		if (!getTxtShortLink().getText().matches("\\w+")) {
			getTxtShortLink().setBackground(Color.red);
			hasErrors = true;
		} else {
			getTxtShortLink().setBackground(Color.white);
		}
		if (!hasErrors) {
			this.saveShortLink();
		}
	}

	private void saveShortLink() {
		if (!this.getLstShortLinks().isSelectionEmpty()) {
			DropDownHolder ddh = (DropDownHolder) lstShortLinks.getSelectedValue();
			ShortLinkValue selectedShortLink = (ShortLinkValue) ddh.getObject();
			ViewDocumentValue viewDocument = (ViewDocumentValue) ((DropDownHolder) getCbViewDocuments().getSelectedItem()).getObject();
			if (viewDocument == null) {
				selectedShortLink.setViewDocumentId(null);
			} else {
				selectedShortLink.setViewDocumentId(viewDocument.getViewDocumentId());
			}
			selectedShortLink.setShortLink(getTxtShortLink().getText());
			selectedShortLink.setRedirectUrl(getTxtRedirectUrl().getText());
			if (selectedShortLink.getShortLinkId() > 0) {
				selectedShortLink = this.communication.saveShortLink(selectedShortLink);
			} else {
				selectedShortLink = this.communication.createShortLink(selectedShortLink);
			}
			((SortingListModel) this.lstShortLinks.getModel()).removeElementAt(this.lstShortLinks.getSelectedIndex());
			((SortingListModel) this.lstShortLinks.getModel()).addElement(new DropDownHolder(selectedShortLink, selectedShortLink.getShortLink()));
		}
	}

	private void newShortLink() {
		if (!this.getTblSitesShortLinks().getSelectionModel().isSelectionEmpty()) {
			SiteValue siteValue = (SiteValue) tblSitesShortLinksSorter.getValueAt(getTblSitesShortLinks().getSelectedRow(), 2);
			ShortLinkValue newShortLink = new ShortLinkValue();
			newShortLink.setShortLink("<new ShortLink>");
			newShortLink.setSiteId(siteValue.getSiteId());
			newShortLink.setRedirectUrl("");
			newShortLink.setShortLinkId(-1);
			DropDownHolder ddh = new DropDownHolder(newShortLink, newShortLink.getShortLink());
			this.shortLinksListModel.addElement(ddh);
			this.getLstShortLinks().setSelectedValue(ddh, true);
			this.getCbViewDocuments().setSelectedIndex(0);
			this.getTxtShortLink().setText(newShortLink.getShortLink());
			this.getTxtRedirectUrl().setText(newShortLink.getRedirectUrl());
		}
	}

	private void deleteShortLink() {
		if (!this.getLstShortLinks().isSelectionEmpty()) {
			ShortLinkValue selectedShortLink = (ShortLinkValue) ((DropDownHolder) lstShortLinks.getSelectedValue()).getObject();
			this.communication.deleteShortLink(selectedShortLink.getShortLinkId());
			this.shortLinksListModel.removeElement(lstShortLinks.getSelectedValue());
			this.getLstShortLinks().clearSelection();
		}
	}

	private JComboBox getCbViewDocuments() {
		if (this.cbxViewDocuments == null) {
			this.cbxViewDocuments = new JComboBox(new DefaultComboBoxModel());
			this.cbxViewDocuments.setEditable(false);
			this.cbxViewDocuments.setEnabled(false);
		}
		return this.cbxViewDocuments;
	}

	private JTextField getTxtShortLink() {
		if (this.txtShortLink == null) {
			this.txtShortLink = new JTextField();
			this.txtShortLink.setEnabled(false);
		}
		return this.txtShortLink;
	}

	private JTextField getTxtRedirectUrl() {
		if (this.txtRedirectUrl == null) {
			this.txtRedirectUrl = new JTextField();
			this.txtRedirectUrl.setEnabled(false);
		}
		return this.txtRedirectUrl;
	}

	private JButton getBtnHelp() {
		if (this.btnHelp == null) {
			this.btnHelp = new JButton(rb.getString("ribbon.help"));
			this.btnHelp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					help();
				}
			});
		}
		return this.btnHelp;
	}

	private void help() {
		JOptionPane.showMessageDialog(UIConstants.getMainFrame(), new JLabel(rb.getString("panel.admin.host.help")), rb.getString("ribbon.help"), JOptionPane.INFORMATION_MESSAGE);
	}

} //  @jve:decl-index=0:visual-constraint="10,10"