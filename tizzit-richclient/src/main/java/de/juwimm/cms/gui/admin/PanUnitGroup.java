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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Collator;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import de.juwimm.cms.authorization.vo.GroupValue;
import de.juwimm.cms.authorization.vo.RoleValue;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.deploy.actions.ExportPersonDataThread;
import de.juwimm.cms.exceptions.UnitnameIsAlreadyUsedException;
import de.juwimm.cms.gui.controls.ReloadablePanel;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.UnitValue;
import de.juwimm.swing.DropDownHolder;
import de.juwimm.swing.NoResizeScrollPane;

/**
 * <p>Title: juwimm cms</p>
 * <p>Description: content management system</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: juwi macmillan group gmbh</p>
 * @author Dirk Bogun
 * @version $Id$
 */
public class PanUnitGroup extends JPanel implements ReloadablePanel {
	private static Logger log = Logger.getLogger(PanUnitGroup.class);
	private Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private DefaultListModel dlmUnits = new DefaultListModel();
	private DefaultListModel dlmRoles = new DefaultListModel();
	private DefaultListModel dlmGroups = new DefaultListModel();
	private JPanel panGroups = new JPanel();
	private JPanel panUnits = new JPanel();
	private JList lstUnits = null;
	private JList lstRoles = new JList();
	private JList lstGroups = new JList();
	private TitledBorder borderGroups;
	private TitledBorder borderUnits;
	private JScrollPane scrollGroups = new NoResizeScrollPane();
	private JScrollPane scrollRoles = new NoResizeScrollPane();
	private JButton btnUnitAdd = new JButton();
	private JButton btnUnitDelete = new JButton();
	private JButton btnUnitEdit = new JButton();
	private JButton btnAddGroup = new JButton();
	private JButton btnDelGroup = new JButton();
	private JButton btnSave = new JButton();
	private JButton btnExportPersonData = new JButton();

	public PanUnitGroup() {
		try {
			setDoubleBuffered(true);
			lstRoles.setCellRenderer(new RolesCellRenderer());
			lstRoles.setModel(dlmRoles);
			lstGroups.setModel(dlmGroups);
			lstGroups.addListSelectionListener(new GroupListSelectionListener());
			jbInit();
			reload();
			btnUnitAdd.setText(rb.getString("dialog.add"));
			btnUnitDelete.setText(rb.getString("dialog.delete"));
			btnUnitEdit.setText(rb.getString("dialog.change"));
			btnAddGroup.setText(rb.getString("dialog.add"));
			btnDelGroup.setText(rb.getString("dialog.delete"));
			btnSave.setText(rb.getString("dialog.save"));
			btnExportPersonData.setText(rb.getString("panel.panelCmsUnit.exportPersonData"));
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	void jbInit() throws Exception {
		borderGroups = new TitledBorder(rb.getString("panel.panelCmsUnit.groups"));
		borderUnits = new TitledBorder(rb.getString("panel.panelCmsUnit.units"));
		lstGroups.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		btnAddGroup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAddGroupActionPerformed(e);
			}
		});
		btnDelGroup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDelGroupActionPerformed(e);
			}
		});
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		scrollGroups.getViewport().add(lstGroups, null);
		scrollRoles.getViewport().add(lstRoles, null);
		this.setLayout(new GridBagLayout());
		//btnUnitAdd.setText("Hinzufügen");
		btnUnitAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addUnitActionPerformed(e);
			}
		});
		//btnUnitDelete.setText("Löschen");
		btnUnitDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteUnitActionPerformed(e);
			}
		});
		btnUnitEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editUnitActionPerformed(e);
			}
		});
		//btnUnitEdit.setText("Ändern");
		btnExportPersonData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnExportPersonDataActionPerformed(e);
			}
		});

		JScrollPane scrollPane = new NoResizeScrollPane(this.getLstUnits());

		panGroups.setBorder(borderGroups);
		panGroups.setLayout(new GridBagLayout());
		panUnits.setBorder(borderUnits);
		panUnits.setLayout(new GridBagLayout());
		/*
		btnAddGroup.setText("Hinzuf�gen");
		btnDelGroup.setText("L�schen");
		btnSave.setText("Speichern");
		*/
		this.add(panGroups, new GridBagConstraints(1, 0, 1, 1, 0.6, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
		this.add(panUnits, new GridBagConstraints(0, 0, 1, 1, 0.4, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 10, 10, 0), 0, 0));
		panUnits.add(btnUnitAdd, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 10, 10), 0, 0));
		panUnits.add(btnUnitEdit, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 10, 10), 0, 0));
		panUnits.add(btnUnitDelete, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 10, 10), 0, 0));
		panUnits.add(scrollPane, new GridBagConstraints(0, 0, 1, 3, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 10, 10, 10), 0, 0));
		if (comm.isUserInRole(UserRights.SITE_ROOT)) {
			panUnits.add(btnExportPersonData, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
					GridBagConstraints.HORIZONTAL, new Insets(0, 10, 10, 10), 0, 0));
		}
		panGroups.add(scrollRoles, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.6, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 10, 10, 0), 0, 0));
		panGroups.add(scrollGroups, new GridBagConstraints(0, 0, 2, 2, 1.0, 0.4, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 10, 10, 0), 0, 0));
		panGroups.add(btnAddGroup, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 10, 10, 10), 4, 0));
		panGroups.add(btnDelGroup, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(0, 10, 10, 10), 0, 0));
		panGroups.add(btnSave, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));
	}

	public void reload() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setButtonsEnabled(false);
				fillUnits();
				fillRoles();
				fillGroups();
				setButtonsEnabled(true);
			}
		});
	}

	private void setButtonsEnabled(boolean enabled) {
		if (!enabled) {
			// disable all buttons
			btnUnitAdd.setEnabled(enabled);
			btnUnitDelete.setEnabled(enabled);
			btnUnitEdit.setEnabled(enabled);
			btnAddGroup.setEnabled(enabled);
			btnDelGroup.setEnabled(enabled);
			btnSave.setEnabled(enabled);
			btnExportPersonData.setEnabled(enabled);
			scrollRoles.setEnabled(enabled);
		} else {
			// enable only some
			btnUnitAdd.setEnabled(enabled);
			btnAddGroup.setEnabled(enabled);
			btnSave.setEnabled(enabled);
			btnExportPersonData.setEnabled(enabled);
		}
	}

	public void unload() {

	}

	private void fillUnits() {
		try {
			this.getLstUnits().clearSelection();
			dlmUnits.removeAllElements();
			UnitValue[] uda = comm.getAllUnits();
			if (uda != null) {
				for (int i = 0; i < uda.length; i++) {
					UnitValue ud = uda[i];
					dlmUnits.addElement(new DropDownHolder(ud, ud.getName()));
				}
			}
		} catch (Exception exe) {
			log.error("Error filling units", exe);
		}
	}

	private void fillGroups() {
		dlmGroups.removeAllElements();
		lstRoles.clearSelection();
		GroupValue[] gv = comm.getAllGroups();
		if (gv != null) {
			for (int i = 0; i < gv.length; i++) {
				dlmGroups.addElement(new DropDownHolder(gv[i], gv[i].getGroupName()));
			}
		}
	}

	private void fillRoles() {
		try {
			dlmRoles.removeAllElements();
			RoleValue[] rva = null;
			if (comm.isUserInRole(UserRights.SITE_ROOT)) {
				rva = comm.getAllRoles();
			} else {
				rva = comm.getRolesMe();
			}
			sortRoles(rva);
			for (int i = 0; i < rva.length; i++) {
				dlmRoles.addElement(rva[i]);
			}
		} catch (Exception exe) {
			log.error("Error filling roles", exe);
		}
	}

	private void sortRoles(RoleValue[] rva) { //bubble bubble :)
		Collator collator = Collator.getInstance(Constants.CMS_LOCALE);
		RoleValue tmp;
		if (rva.length == 1) { return; }
		for (int i = 0; i < rva.length; i++) {
			for (int j = i + 1; j < rva.length; j++) {
				if (collator.compare(roleView(rva[i].getRoleId()), roleView(rva[j].getRoleId())) > 0) {
					tmp = rva[i];
					rva[i] = rva[j];
					rva[j] = tmp;
				}
			}
		}
	}

	private String roleView(String role) {
		String ret = role;
		try {
			ret = rb.getString("ROLE." + ret);
		} catch (Exception exe) {
		}
		return ret;
	}
	
	/**
	 * 
	 */
	private class GroupListSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting()) return;
			if (lstGroups.isSelectionEmpty()) {
				btnDelGroup.setEnabled(false);
				btnSave.setEnabled(false);
				scrollRoles.setEnabled(false);
				lstRoles.clearSelection();
			} else {
				btnDelGroup.setEnabled(true);
				scrollRoles.setEnabled(true);
				btnSave.setEnabled(true);
				try {
					GroupValue gv = (GroupValue) ((DropDownHolder) lstGroups.getSelectedValue()).getObject();
					RoleValue[] rv = gv.getRoles();
					if (rv != null) {
						int[] selIdx = new int[rv.length];
						for (int i = 0; i < rv.length; i++) {
							for(int li = 0; li<dlmRoles.size();li++)
							{
								RoleValue r = (RoleValue)dlmRoles.get(li);
								if(r.getRoleId().equalsIgnoreCase(rv[i].getRoleId())){
									selIdx[i]=li;
									break;
								}
								else{
									selIdx[i]=-1;
								}
							}
						}
						lstRoles.setSelectedIndices(selIdx);
					} else {
						// no roles => no selection
						lstRoles.clearSelection();
					}
				} catch (Exception exe) {
				}
			}
		}
	}

	/**
	 * 
	 */
	private class RolesCellRenderer extends DefaultListCellRenderer implements ListCellRenderer {
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			return super.getListCellRendererComponent(list, roleView(((RoleValue) value).getRoleId()), index, isSelected, cellHasFocus);
		}
	}

	private void addUnitActionPerformed(ActionEvent e) {
		String unitName;
		unitName = JOptionPane.showInputDialog(UIConstants.getMainFrame(), 
				rb.getString("panel.panelCmsUnit.addNewUnitMessage"), rb.getString("dialog.title"),
				JOptionPane.PLAIN_MESSAGE);

		if (unitName != null) {
			if (unitName.trim().equals("")) {
				JOptionPane.showMessageDialog(UIConstants.getMainFrame(), 
						rb.getString("panel.panelCmsUnit.addNewUnitEmptyMessage"), rb.getString("dialog.title"),
						JOptionPane.WARNING_MESSAGE);
			} else {
				try {
					comm.createUnit(unitName.trim());
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							fillUnits();
						}
					});
				} catch (UnitnameIsAlreadyUsedException uia) {
					JOptionPane.showMessageDialog(UIConstants.getMainFrame(), 
							rb.getString("exception.UnitnameIsAlreadyUsed"), rb.getString("dialog.title"),
							JOptionPane.ERROR_MESSAGE);
				} catch (Exception exe) {
					log.error("Error adding unit", exe);
				}
			}
		}
	}

	private void deleteUnitActionPerformed(ActionEvent e) {
		try {
			UnitValue unit = (UnitValue) ((DropDownHolder) this.getLstUnits().getSelectedValue()).getObject();
			comm.removeUnit(unit);

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					fillUnits();
				}
			});

		} catch (Exception exe) {
			log.error("Error delete unit", exe);
		}
	}

	private void editUnitActionPerformed(ActionEvent e) {
		try {
			String oldName = ((DropDownHolder) this.getLstUnits().getSelectedValue()).toString();
			String unitName = null;
			unitName = JOptionPane.showInputDialog(UIConstants.getMainFrame(),
					rb.getString("panel.panelCmsUnit.addNewUnitMessage"),
					((DropDownHolder) this.getLstUnits().getSelectedValue()).toString());

			if (unitName != null && !unitName.trim().equalsIgnoreCase("") && !unitName.trim().equals(oldName)) {
				UnitValue unit = (UnitValue) ((DropDownHolder) this.getLstUnits().getSelectedValue()).getObject();
				unit.setName(unitName.trim());
				comm.updateUnit(unit);
	
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						fillUnits();
					}
				});
			}
		} catch (Exception exe) {
			log.error("Error editing unit", exe);
		}

	}

	private void btnAddGroupActionPerformed(ActionEvent e) {
		String groupName = JOptionPane.showInputDialog(UIConstants.getMainFrame(), 
				rb.getString("panel.panelCmsUnit.addNewGroupMessage"), rb.getString("dialog.title"), JOptionPane.PLAIN_MESSAGE);
		if (groupName != null && !groupName.equalsIgnoreCase("")) {
			try {
				GroupValue gv = comm.createGroup(groupName);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						fillGroups();
					}
				});
				int id = dlmGroups.indexOf(gv);
				lstGroups.setSelectedIndex(id);
			} catch (Exception exe) {
				JOptionPane.showMessageDialog(UIConstants.getMainFrame(), exe.getMessage(), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	void btnDelGroupActionPerformed(ActionEvent e) {
		Object selObj = lstGroups.getSelectedValue();
		if (selObj != null) {
			try {
				GroupValue gv = (GroupValue) ((DropDownHolder) selObj).getObject();
				comm.removeGroup(gv.getGroupId());
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						fillGroups();
					}
				});
			} catch (Exception exe) {
				JOptionPane.showMessageDialog(UIConstants.getMainFrame(), exe.getMessage(), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void save() {
		Object selObj = lstGroups.getSelectedValue();
		if (selObj != null) {
			try {
				GroupValue gv = (GroupValue) ((DropDownHolder) selObj).getObject();
				Object[] obarr = lstRoles.getSelectedValues();
				/*RoleValue[] rvold = gv.getRoles();
				 ArrayList alNew = new ArrayList(obarr.length);
				 ArrayList alDel = new ArrayList(obarr.length);

				 for(int i=0;i<obarr.length;i++) { //find out new one
				 RoleValue rv = (RoleValue) obarr[i];
				 boolean found = false;
				 for(int j=0;j<rvold.length;j++) {
				 if(rvold[j].equals(rv)) {
				 found = true;
				 break;
				 }
				 }
				 if(!found) { //new one
				 alNew.add(rv);
				 }
				 }
				 for(int i=0;i<rvold.length;i++) { //find out deleted one
				 boolean found = false;
				 for(int j=0;j<obarr.length;j++) {
				 RoleValue rv = (RoleValue) obarr[j];
				 if(rvold[i].equals(rv)) {
				 found = true;
				 break;
				 }
				 }
				 if(!found) { //new one
				 alDel.add(rvold[i]);
				 }
				 }

				 gv.setAddedRoles((RoleValue[]) alNew.toArray(new RoleValue[alNew.size()]));
				 gv.setRemovedRoles((RoleValue[]) alDel.toArray(new RoleValue[alDel.size()]));
				 */
				RoleValue[] rva = new RoleValue[obarr.length];
				for (int i = 0; i < obarr.length; i++) {
					rva[i] = (RoleValue) obarr[i];
				}
				gv.setRoles(rva);
				comm.updateGroup(gv);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						fillGroups();
					}
				});
			} catch (Exception exe) {
				log.error("Error filling groups", exe);
				JOptionPane.showMessageDialog(UIConstants.getMainFrame(), exe.getMessage(), rb.getString("dialog.title"),
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void btnExportPersonDataActionPerformed(ActionEvent e) {
		new ExportPersonDataThread().start();
	}

	private JList getLstUnits() {
		if (lstUnits == null) {
			lstUnits = new JList(this.dlmUnits);
			lstUnits.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			lstUnits.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (e.getValueIsAdjusting()) return;
					if (lstUnits.isSelectionEmpty()) {
						btnUnitEdit.setEnabled(false);
						btnUnitDelete.setEnabled(false);
					} else {
						btnUnitEdit.setEnabled(true);
						btnUnitDelete.setEnabled(true);
					}
				}
			});
		}
		return lstUnits;
	}

}