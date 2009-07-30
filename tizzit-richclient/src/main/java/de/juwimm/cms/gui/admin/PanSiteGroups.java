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

import static de.juwimm.cms.client.beans.Application.*;
import static de.juwimm.cms.common.Constants.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.*;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.gui.controls.ReloadablePanel;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.SiteGroupValue;
import de.juwimm.cms.vo.SiteValue;
import de.juwimm.swing.DropDownHolder;
import de.juwimm.swing.PickListData;

/**
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public final class PanSiteGroups extends JPanel implements ReloadablePanel {
	private static Logger log = Logger.getLogger(PanSiteGroups.class);
	private Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private JComboBox comboSiteGroups = null;
	private JButton btnAddSiteGroup = null;
	private JButton btnManageSiteGroups = null;
	private JButton btnDeleteSiteGroup = null;
	private JLabel lblChooseSiteGroup = null;
	private JButton btnSaveSiteGroups = null;
	private JLabel lblIntroText = null;
	private PickListData pickListData = null;
	private HashSet<SiteGroupValue> deletedSiteGroupsList = new HashSet<SiteGroupValue>();
	
	/**
	 * This is the default constructor
	 */
	public PanSiteGroups() {
		super();
		initialize();
		//this.fillSiteGroupList();
	}
	
	private void fillSiteGroupList() {
		SiteGroupValue[] vals = comm.getAllSiteGroups();
		DefaultComboBoxModel model = null;
		if (vals != null && vals.length > 0) {
			Vector<DropDownHolder> vec = new Vector<DropDownHolder>();
			for (int i = (vals.length - 1); i >= 0; i--) {
				vec.add(new DropDownHolder(vals[i], vals[i].getSiteGroupName()));
			}
			model = new DefaultComboBoxModel(vec);
		} else {
			model = new DefaultComboBoxModel();
		}
		this.comboSiteGroups.setModel(model);
	}
	
	private PickListData getPickListData(SiteGroupValue value) {
		if (this.pickListData == null) {
			this.pickListData = new PickListData();
			this.pickListData.setLeftLabel(rb.getString("panel.panelSiteGroups.sites.assigned"));
			this.pickListData.setRightLabel(rb.getString("panel.panelSiteGroups.sites.available"));
		} else {
			this.pickListData.getLstLeftModel().removeAllElements();
			this.pickListData.getLstRightModel().removeAllElements();
		}
		HashSet<Integer> assignedSites = new HashSet<Integer>();
		SiteValue[] allUnassignedSites = comm.getAllNotAssignedSites();
		if (value.getSiteValues() != null && value.getSiteValues() != null) {
			for (int i = value.getSiteValues().length - 1; i >= 0; i--) {
				DropDownHolder ddh = new DropDownHolder(value.getSiteValues()[i], value.getSiteValues()[i].getName());
				this.pickListData.getLstLeftModel().addElement(ddh);
				assignedSites.add(Integer.valueOf(value.getSiteValues()[i].getSiteId()));
			}
			if (allUnassignedSites != null) {
				for (int i = allUnassignedSites.length - 1; i >= 0; i--) {
					if (!assignedSites.contains(Integer.valueOf(allUnassignedSites[i].getSiteId()))) {
						DropDownHolder ddh = new DropDownHolder(allUnassignedSites[i], allUnassignedSites[i].getName());
						this.pickListData.getLstRightModel().addElement(ddh);
					}
				}
			}
		} else {
			if (allUnassignedSites != null) {
				for (int i = allUnassignedSites.length - 1; i >= 0; i--) {
					DropDownHolder ddh = new DropDownHolder(allUnassignedSites[i], allUnassignedSites[i].getName());
					this.pickListData.getLstRightModel().addElement(ddh);
				}
			}
		}
		
		return pickListData;
	}

	public void unload() {
		if (log.isDebugEnabled()) log.debug("unload ...");
	}

	public void reload() {
		if (log.isDebugEnabled()) log.debug("reload ...");
		this.fillSiteGroupList();
	}

	public void save() {
		if (log.isDebugEnabled()) log.debug("save ...");
		for (int i = this.getComboSiteGroups().getModel().getSize() -1; i >= 0; i--) {
			DropDownHolder ddh = (DropDownHolder) this.getComboSiteGroups().getModel().getElementAt(i);
			SiteGroupValue siteGroupValue = (SiteGroupValue) ddh.getObject();
			if (siteGroupValue.getSiteGroupId() == null || siteGroupValue.getSiteGroupId() <= 0) {
				// new group
				siteGroupValue = comm.createSiteGroup(siteGroupValue);
				ddh = new DropDownHolder(siteGroupValue, siteGroupValue.getSiteGroupName());
				((DefaultComboBoxModel) this.getComboSiteGroups().getModel()).removeElementAt(i);
				((DefaultComboBoxModel) this.getComboSiteGroups().getModel()).addElement(ddh);
			} else {
				// existing group
				comm.updateSiteGroup(siteGroupValue);
			}
		}
		Iterator it = this.deletedSiteGroupsList.iterator();
		while (it.hasNext()) {
			SiteGroupValue value = (SiteGroupValue) it.next();
			comm.removeSiteGroup(value);
		}
		this.deletedSiteGroupsList.clear();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 2;
		gridBagConstraints21.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints21.insets = new java.awt.Insets(10,10,10,10);
		gridBagConstraints21.fill = java.awt.GridBagConstraints.NONE;
		gridBagConstraints21.gridy = 3;
		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.gridx = 2;
		gridBagConstraints12.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints12.insets = new java.awt.Insets(10,10,10,10);
		gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints12.gridwidth = 3;
		gridBagConstraints12.gridy = 0;
		lblIntroText = new JLabel();
		lblIntroText.setText(rb.getString("panel.panelSiteGroups.introText"));
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
		gridBagConstraints.insets = new java.awt.Insets(15,10,0,0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
		gridBagConstraints.gridy = 1;
		lblChooseSiteGroup = new JLabel();
		lblChooseSiteGroup.setText(rb.getString("panel.panelSiteGroups.group.choose"));
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 3;
		gridBagConstraints11.insets = new java.awt.Insets(10,10,0,10);
		gridBagConstraints11.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints11.weightx = 1.0;
		gridBagConstraints11.weighty = 1.0;
		gridBagConstraints11.gridy = 2;
		GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
		gridBagConstraints10.gridx = 8;
		gridBagConstraints10.insets = new java.awt.Insets(25, 10, 10, 10);
		gridBagConstraints10.gridy = 3;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 3;
		gridBagConstraints4.insets = new java.awt.Insets(10,10,0,10);
		gridBagConstraints4.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints4.weightx = 1.0;
		gridBagConstraints4.weighty = 1.0;
		gridBagConstraints4.gridy = 1;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.insets = new java.awt.Insets(10,10,0,0);
		gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints2.gridwidth = 2;
		gridBagConstraints2.weighty = 1.0;
		gridBagConstraints2.gridx = 1;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.gridheight = 1;
		gridBagConstraints1.insets = new java.awt.Insets(10,10,10,0);
		gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.weighty = 1.0;
		gridBagConstraints1.gridwidth = 2;
		gridBagConstraints1.gridy = 2;
		this.setLayout(new GridBagLayout());
		this.setSize(new java.awt.Dimension(569, 297));
		this.add(lblChooseSiteGroup, gridBagConstraints);
		this.add(getComboSiteGroups(), gridBagConstraints2);
		this.add(getBtnManageSiteGroups(), gridBagConstraints4);
		this.add(getBtnDeleteSiteGroup(), gridBagConstraints11);
		this.add(getBtnAddSiteGroup(), gridBagConstraints1);
		this.add(lblIntroText, gridBagConstraints12);
		this.add(getBtnSaveSiteGroups(), gridBagConstraints21);
		this.setSize(new java.awt.Dimension(440, 223));
	}

	/**
	 * This method initializes btnAddSiteGroup	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnAddSiteGroup() {
		if (btnAddSiteGroup == null) {
			btnAddSiteGroup = new JButton();
			btnAddSiteGroup.setText(rb.getString("panel.panelSiteGroups.group.createnew"));
			btnAddSiteGroup.setPreferredSize(new java.awt.Dimension(120,23));
			btnAddSiteGroup.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					CreateNewSiteGroupDialog dlg = new CreateNewSiteGroupDialog();
					dlg.setLocationRelativeTo(UIConstants.getMainFrame());
					dlg.setModal(true);
					dlg.setVisible(true);
					String name = dlg.getSiteGroupName();
					if (dlg.isBtnOkKlicked()) {
						DefaultComboBoxModel model = (DefaultComboBoxModel) comboSiteGroups.getModel();
						SiteGroupValue val = new SiteGroupValue();
						val.setSiteGroupId(-1);
						val.setSiteGroupName(name);
						model.addElement(new DropDownHolder(val, val.getSiteGroupName()));
						comboSiteGroups.updateUI();
					}
				}
			});
		}
		return btnAddSiteGroup;
	}

	/**
	 * This method initializes comboSiteGroups	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getComboSiteGroups() {
		if (comboSiteGroups == null) {
			comboSiteGroups = new JComboBox();
			comboSiteGroups.setMaximumSize(new java.awt.Dimension(200, 20));
			comboSiteGroups.setPreferredSize(new java.awt.Dimension(100,23));
			comboSiteGroups.setEditable(false);
		}
		return comboSiteGroups;
	}

	/**
	 * This method initializes btnManageSiteGroups	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnManageSiteGroups() {
		if (btnManageSiteGroups == null) {
			btnManageSiteGroups = new JButton();
			btnManageSiteGroups.setText(rb.getString("panel.panelSiteGroups.group.manage"));
			btnManageSiteGroups.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					DropDownHolder currentElement = (DropDownHolder) comboSiteGroups.getSelectedItem();
					if (currentElement != null) {
						SiteGroupValue val = (SiteGroupValue) currentElement.getObject();
						ManageSiteGroupDialog dlg = new ManageSiteGroupDialog(getPickListData(val));
						dlg.setTitle(rb.getString("panel.panelSiteGroups.group.edit") + val.getSiteGroupName());
						dlg.setModal(true);
						dlg.setLocationRelativeTo(UIConstants.getMainFrame());
						dlg.setVisible(true);
						if (dlg.isBtnOkKlicked()) {
							SiteValue[] sitesList = new SiteValue[dlg.getPickListData().getLstLeftModel().getSize()];
							int i = 0;
							Iterator it = dlg.getPickListData().getLstLeftModel().iterator();
							while (it.hasNext()) {
								DropDownHolder ddh = (DropDownHolder) it.next();
								SiteValue siteValue = (SiteValue) ddh.getObject();
								sitesList[i++] = siteValue;
							}
							if (val.getSiteValues() == null) {
								val.setSiteValues(new SiteValue[0]);
							}
							val.setSiteValues(sitesList);
						}
					}
				}
			});
		}
		return btnManageSiteGroups;
	}

	/**
	 * This method initializes btnDeleteSiteGroup	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnDeleteSiteGroup() {
		if (btnDeleteSiteGroup == null) {
			btnDeleteSiteGroup = new JButton();
			btnDeleteSiteGroup.setText(rb.getString("panel.panelSiteGroups.btn.deleteGroup"));
			btnDeleteSiteGroup.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					DropDownHolder currentElement = (DropDownHolder) comboSiteGroups.getSelectedItem();
					if (currentElement != null) {
						SiteGroupValue val = (SiteGroupValue) currentElement.getObject();
						String name = val.getSiteGroupName();
						int ret = JOptionPane.showConfirmDialog(UIConstants.getMainFrame(), rb.getString("panel.panelSiteGroups.group.reallydelete") + " " + name, rb.getString("panel.panelSiteGroups.group.reallydelete"), JOptionPane.YES_NO_OPTION);
						if (ret == JOptionPane.YES_OPTION) {
							deletedSiteGroupsList.add(val);
							DefaultComboBoxModel model = (DefaultComboBoxModel) comboSiteGroups.getModel();
							model.removeElement(currentElement);
						}
					}
				}
			});
		}
		return btnDeleteSiteGroup;
	}

	/**
	 * This method initializes btnSaveSiteGroups
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnSaveSiteGroups() {
		if (btnSaveSiteGroups == null) {
			btnSaveSiteGroups = new JButton();
			btnSaveSiteGroups.setText(rb.getString("dialog.save"));
			btnSaveSiteGroups.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					save();
				}
			});
		}
		return btnSaveSiteGroups;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
