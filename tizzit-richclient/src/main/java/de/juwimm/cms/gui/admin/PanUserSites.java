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
package de.juwimm.cms.gui.admin;

import static de.juwimm.cms.client.beans.Application.getBean;
import static de.juwimm.cms.common.Constants.rb;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.gui.controls.ReloadablePanel;
import de.juwimm.cms.gui.table.SiteTableModel;
import de.juwimm.cms.gui.table.SiteUserTableModel;
import de.juwimm.cms.gui.table.TableSorter;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.SiteValue;

/**
 * Simple Version of PanSitesAdministration for connecting users and sites
 * 
 * <p>Title: Tizzit</p>
 * <p>Description: Enterprise Content Management</p>
 * <p>Copyright: Copyright (c) 2002, 2003, 2004, 2005, 2006</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="carsten.schalm@juwimm.com">Carsten Schalm</a>
 * @version $Id$
 */
public class PanUserSites extends JPanel implements ReloadablePanel {
	private static Logger log = Logger.getLogger(PanUserSites.class);
	private final Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private SiteTableModel tblSiteModel = new SiteTableModel();
	private SiteUserTableModel tblUserModel = null;
	private TableSorter tblSiteSorter = null;
	private TableSorter tblUserSorter = null;
	private final JPanel panDetails = new JPanel();
	private final JButton btnSaveChanges = new JButton(UIConstants.BTN_SAVE);
	private final JTextField txtSiteName = new JTextField();
	private final JTextField txtSiteShort = new JTextField();
	private final JLabel lblSiteShort = new JLabel();
	private final JScrollPane spSite = new JScrollPane();
	private final JTable tblSite = new JTable();
	private final JLabel lblSiteName1 = new JLabel();
	private final GridBagLayout gridBagLayout1 = new GridBagLayout();
	private final JPanel panConnectedUsers = new JPanel();
	private TitledBorder titledBorder2;
	private final JScrollPane spUser = new JScrollPane();
	private final JTable tblUser = new JTable();
	private final BorderLayout borderLayout1 = new BorderLayout();
	private final GridBagLayout gridBagLayout2 = new GridBagLayout();
	private final JLabel lblSiteId = new JLabel();
	private final JLabel lblSiteIdContent = new JLabel();
	private final JPanel jPanel1 = new JPanel();
	private final GridBagLayout gridBagLayout3 = new GridBagLayout();

	public PanUserSites() {
		try {
			jbInit();
			tblSite.getSelectionModel().addListSelectionListener(new SiteListSelectionListener());
			tblSite.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tblUser.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			titledBorder2.setTitle(rb.getString("panel.sitesAdministration.frmConnectedUsers"));
			btnSaveChanges.setText(rb.getString("dialog.save"));
			lblSiteShort.setText(rb.getString("panel.sitesAdministration.lblSiteShort"));
			lblSiteName1.setText(rb.getString("panel.sitesAdministration.lblSiteName"));
			lblSiteId.setText(rb.getString("panel.sitesAdministration.lblSiteId"));
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	void jbInit() throws Exception {
		titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(165, 163, 151)), "Connected Users");
		this.setLayout(gridBagLayout1);
		panDetails.setBorder(BorderFactory.createEtchedBorder());
		panDetails.setDebugGraphicsOptions(0);
		panDetails.setLayout(gridBagLayout2);
		btnSaveChanges.setText("Save");
		btnSaveChanges.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		txtSiteShort.setSelectionStart(11);
		lblSiteShort.setText("Site Short");
		lblSiteName1.setText("Site Name");
		panConnectedUsers.setBorder(titledBorder2);
		panConnectedUsers.setLayout(borderLayout1);
		lblSiteId.setText("SiteId");
		lblSiteIdContent.setText(" ");
		jPanel1.setLayout(gridBagLayout3);
		this.setSize(711, 494);
		panDetails.add(txtSiteName, new GridBagConstraints(1, 1, 3, 1, 0.6, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		panDetails.add(txtSiteShort, new GridBagConstraints(1, 2, 2, 1, 0.6, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		this.add(spSite, new GridBagConstraints(0, 0, 2, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(10, 10, 0, 0), 200, 0));
		this.add(panDetails, new GridBagConstraints(2, 0, 1, 2, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
		spSite.getViewport().add(tblSite, null);
		panDetails.add(lblSiteName1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		panDetails.add(lblSiteShort, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		panDetails.add(panConnectedUsers, new GridBagConstraints(4, 0, 1, 17, 0.4, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 10, 10, 10), 150, 0));
		panConnectedUsers.add(spUser, BorderLayout.CENTER);
		spUser.getViewport().add(tblUser, null);
		panDetails.add(btnSaveChanges, new GridBagConstraints(0, 16, 3, 1, 0.0, 1.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 10, 10, 10), 0, 0));
		panDetails.add(lblSiteId, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		panDetails.add(lblSiteIdContent, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		panDetails.add(jPanel1, new GridBagConstraints(1, 7, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	}

	public void reload() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					setCursor(new Cursor(Cursor.WAIT_CURSOR));
					setButtonsEnabled(false);
					reloadUsers();
					reloadSites();
				} catch (Exception exe) {
					log.error("Reloading Error", exe);
				}
				setButtonsEnabled(true);
				setCursor(Cursor.getDefaultCursor());
			}
		});
	}

	public void unload() {

	}

	private void setButtonsEnabled(boolean enabled) {
		btnSaveChanges.setEnabled(enabled);
	}

	private void reloadUsers() {
		tblUserModel = new SiteUserTableModel();
		tblUserSorter = new TableSorter(tblUserModel, tblUser.getTableHeader());
		tblUserModel.setTableSorter(tblUserSorter);
		tblUserModel.addRows(comm.getAllUsers4OwnSites());
		tblUser.getSelectionModel().clearSelection();
		tblUser.setModel(tblUserSorter);
	}

	private void reloadSites() {
		setValues(new SiteValue());
		tblSiteModel = new SiteTableModel();
		tblSiteSorter = new TableSorter(tblSiteModel, tblSite.getTableHeader());
		tblSiteModel.addRows(comm.getAllSites4CurrentUser());
		tblSite.getSelectionModel().clearSelection();
		tblSite.setModel(tblSiteSorter);
		tblUserModel.setSelectedUsers(new String[0]);
		siteSelected(false);
	}

	private void selectSite(int siteId) {
		int row = tblSiteModel.getRowForSite(siteId);
		if (row >= 0) {
			tblSite.getSelectionModel().setSelectionInterval(row, row);
		}
	}

	public void save() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				setButtonsEnabled(false);
				SiteValue vo = (SiteValue) tblSiteSorter.getValueAt(tblSite.getSelectedRow(), 2);
				int siteToSelect = vo.getSiteId();
				comm.setConnectedUsersForSite(siteToSelect, tblUserModel.getSelectedUsers());
				reloadSites();
				selectSite(siteToSelect);
				setButtonsEnabled(true);
				setCursor(Cursor.getDefaultCursor());
			}
		});
	}

	/**
	 * 
	 */
	private class SiteListSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting()) { return; }
			if (tblSite.getSelectedRow() >= 0) {
				siteSelected(false);
				setButtonsEnabled(false);
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				SiteValue vo = (SiteValue) tblSiteSorter.getValueAt(tblSite.getSelectedRow(), 2);
				setValues(vo);
				if (vo.getSiteId() > 0) {
					String[] connUsers = comm.getConnectedUsersForSite(vo.getSiteId());
					tblUserModel.setSelectedUsers(connUsers);
				}
				siteSelected(true);
				setButtonsEnabled(true);
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			} else {
				setButtonsEnabled(false);
				siteSelected(false);
			}
		}
	}

	private void siteSelected(boolean val) {
		panDetails.setEnabled(false);
		tblUser.setEnabled(val);
		txtSiteName.setEnabled(false);
		txtSiteShort.setEnabled(false);
	}

	private void setValues(SiteValue vo) {
		txtSiteName.setText(vo.getName());
		txtSiteShort.setText(vo.getShortName());
		if (vo.getSiteId() != null) {
			lblSiteIdContent.setText(vo.getSiteId() + "");
		} else {
			lblSiteIdContent.setText(" ");
		}
	}

} //  @jve:decl-index=0:visual-constraint="10,10"