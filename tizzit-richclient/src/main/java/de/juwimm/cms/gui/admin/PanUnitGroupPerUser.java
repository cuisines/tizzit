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

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import de.juwimm.cms.authorization.vo.GroupValue;
import de.juwimm.cms.authorization.vo.UserValue;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.gui.controls.ReloadablePanel;
import de.juwimm.cms.gui.event.ExitEvent;
import de.juwimm.cms.gui.event.ExitListener;
import de.juwimm.cms.gui.table.TableSorter;
import de.juwimm.cms.gui.table.UserTableModel;
import de.juwimm.cms.util.ActionHub;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.vo.UnitValue;
import de.juwimm.swing.DropDownHolder;
import de.juwimm.swing.NoResizeScrollPane;
import de.juwimm.swing.PickListData;
import de.juwimm.swing.PickListPanel;

/**
 * <p>
 * Title: juwimm cms
 * </p>
 * <p>
 * Description: content management system
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: juwi macmillan group gmbh
 * </p>
 * 
 * @author Dirk Bogun
 * @version $Id: PanUnitGroupPerUser.java 6 2009-07-30 14:05:05Z
 *          skulawik@gmail.com $
 */
public class PanUnitGroupPerUser extends JPanel implements ExitListener,
		ActionListener, ReloadablePanel {
	private static Logger log = Logger.getLogger(PanUnitGroupPerUser.class);
	private UserTableModel userTableModel = new UserTableModel(false);
	private Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private int currentSelected = -1;
	private DropDownHolder[] allUnits;
	private DropDownHolder[] allGroupsOfCaller;
	private JTable tblUser;
	private JButton btnSave = new JButton();
	private JLabel lblUser = new JLabel();

	private PickListData unitPickData = null;
	private PickListData groupPickData = null;
	private PickListPanel panUnitPick = null;
	private PickListPanel panGroupPick = null;

	public PanUnitGroupPerUser() {
		ActionHub.addExitListener(this);
		try {
			setDoubleBuffered(true);
			jbInit();

			tblUser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tblUser.getSelectionModel().addListSelectionListener(
					new UserListSelectionListener(this));

		} catch (Exception exe) {
			log.error("Initialization Error: ", exe);
		}
	}

	private UserValue getSelectedUser(int row) {
		if (row >= 0) {
			return (UserValue) tblUser.getModel().getValueAt(row, 5);
		}
		return null;
	}

	void jbInit() throws Exception {
		this.setLayout(new GridBagLayout());

		this.unitPickData = new PickListData();
		this.unitPickData.setLeftLabel(rb
				.getString("panel.admin.tab.units.assigned"));
		this.unitPickData.setRightLabel(rb
				.getString("panel.admin.tab.units.all"));
		this.groupPickData = new PickListData();
		this.groupPickData.setLeftLabel(rb
				.getString("panel.admin.tab.groups.assigned"));
		this.groupPickData.setRightLabel(rb
				.getString("panel.admin.tab.groups.all"));
		this.panUnitPick = new PickListPanel(this.unitPickData);
		this.panGroupPick = new PickListPanel(this.groupPickData);
		this.btnSave.setText(rb.getString("dialog.save"));
		this.lblUser.setText(rb.getString("panel.login.username"));

		this.btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});

		this.tblUser = new JTable();
		JScrollPane scrollUser = new NoResizeScrollPane(tblUser);

		this.add(lblUser, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10, 5, 0), 0, 0));
		this.add(scrollUser, new GridBagConstraints(0, 1, 1, 2, 1.0, 1.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(0, 10, 0, 0), 0, 0));
		this.add(btnSave, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
				new Insets(10, 10, 10, 0), 0, 0));
		this.add(this.panUnitPick, new GridBagConstraints(1, 0, 1, 2, 2.0, 1.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(0, 10, 0, 10), 0, 0));
		this.add(this.panGroupPick, new GridBagConstraints(1, 2, 1, 1, 2.0,
				1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(0, 10, 0, 10), 0, 0));
	}

	/**
	 * Return all Groups the Caller is in that are not in the Array given
	 * 
	 * @param gvDiff
	 *            the Array to check
	 * @return Array of all of my Groups that are not in the Array given
	 */
	private DropDownHolder[] getDiffGroups(GroupValue[] gvDiff) {
		if (allGroupsOfCaller == null)
			return new DropDownHolder[0];
		ArrayList<DropDownHolder> diffGroupList = new ArrayList<DropDownHolder>();
		for (int i = 0; i < allGroupsOfCaller.length; i++) {
			if (!contains(gvDiff, allGroupsOfCaller[i])) {
				diffGroupList.add(allGroupsOfCaller[i]);
			}
		}
		return diffGroupList.toArray(new DropDownHolder[0]);
	}

	private boolean contains(GroupValue[] gv, DropDownHolder groupDao) {
		for (int i = 0; i < gv.length; i++) {
			if (gv[i].getGroupId().equals(
					((GroupValue) groupDao.getObject()).getGroupId())) {
				return true;
			}
		}
		return false;
	}

	public void unload() {
		if (unitPickData.isModified() || groupPickData.isModified()) {
			int i = JOptionPane.showConfirmDialog(this, rb
					.getString("dialog.wantToSave"), "CMS",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (i == JOptionPane.YES_OPTION) {
				saveChanges(currentSelected);
			}
			unitPickData.setModified(false);
			groupPickData.setModified(false);
		}
	}

	private void setPickerEnabled(boolean enabled) {
		this.panGroupPick.setEnabled(enabled);
		this.panUnitPick.setEnabled(enabled);
	}

	public void reload() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				try {
					setPickerEnabled(false);
					tblUser.setEnabled(false);
					userTableModel = new UserTableModel(false);
					TableSorter tblSorter = new TableSorter(userTableModel,
							tblUser.getTableHeader());

					GroupValue[] groups = null;
					if (!comm.isUserInRole(UserRights.SITE_ROOT)) {
						groups = comm.getGroups();
					} else {
						groups = comm.getAllGroups();
					}
					if (groups != null) {
						allGroupsOfCaller = new DropDownHolder[groups.length];
						for (int i = groups.length - 1; i >= 0; i--) {
							allGroupsOfCaller[i] = new DropDownHolder(
									groups[i], groups[i].getGroupName());
						}
					}
					UnitValue[] units = comm.getUnits();
					if (units != null) {
						allUnits = new DropDownHolder[units.length];
						for (int i = units.length - 1; i >= 0; i--) {
							allUnits[i] = new DropDownHolder(units[i], units[i]
									.getName());
						}
					}

					UserValue[] uv = comm.getAllUser();
					if (uv != null) {
						if (comm.isUserInRole(UserRights.SITE_ROOT)) {
							userTableModel.addRows(uv);
						} else {
							UserValue me = comm.getUser();
							for (int i = 0; i < uv.length; i++) {
								boolean isSiteRoot = comm.isUserInRole(uv[i],
										UserRights.SITE_ROOT);
								boolean isMe = uv[i].getUserName()
										.equalsIgnoreCase(me.getUserName());
								if (!isSiteRoot) {
									userTableModel.addRow(uv[i]);
								}
							}
						}
					}

					tblUser.setModel(tblSorter);
					unitPickData.getLstLeftModel().clear();
					unitPickData.getLstRightModel().clear();
					groupPickData.getLstLeftModel().clear();
					groupPickData.getLstRightModel().clear();
				} catch (Exception exe) {
					log.error("Error reloading tables: ", exe);
				}
				tblUser.setEnabled(true);
				setCursor(Cursor.getDefaultCursor());
			}
		});
	}

	public void save() {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setPickerEnabled(false);
		btnSave.setEnabled(false);
		if (currentSelected >= 0)
			saveChanges(currentSelected);
		setPickerEnabled(true);
		btnSave.setEnabled(true);
		setCursor(Cursor.getDefaultCursor());
	}

	private synchronized void saveChanges(int row) {
		if (this.unitPickData.isModified() || this.groupPickData.isModified()) {
			try {
				tblUser.setEnabled(false);
				UserValue user = getSelectedUser(row);

				GroupValue[] groups = comm.getGroups4User(user.getUserName());
				DropDownHolder[] groupList = null;
				if (groups != null) {
					groupList = new DropDownHolder[groups.length];
					for (int i = groups.length - 1; i >= 0; i--) {
						groupList[i] = new DropDownHolder(groups[i], groups[i]
								.getGroupName());
					}
				}
				UnitValue[] units = comm.getUnits4User(user.getUserName());
				DropDownHolder[] unitList = null;
				if (units != null) {
					unitList = new DropDownHolder[units.length];
					for (int i = units.length - 1; i >= 0; i--) {
						unitList[i] = new DropDownHolder(units[i], units[i]
								.getName());
					}
				}

				for (int i = 0; i < groupList.length; i++) {
					if (!this.groupPickData.getLstLeftModel().contains(
							groupList[i])) {
						try {
							comm.removeUserFromGroup(((GroupValue) groupList[i]
									.getObject()), user.getUserName());
						} catch (Exception exe) {
							log.error("Error removing user from group: ", exe);
						}
					}
				}
				for (int i = 0; i < this.groupPickData.getLstLeftModel()
						.getSize(); i++) {
					GroupValue group = (GroupValue) ((DropDownHolder) this.groupPickData
							.getLstLeftModel().getElementAt(i)).getObject();
					if (!ArrayUtils.contains(groups, group)) {
						try {
							comm.addUserToGroup(group, user.getUserName());
						} catch (Exception exe) {
							log.error("Error adding user to group: ", exe);
						}
					}
				}

				if (!comm.isUserInRole(user, UserRights.SITE_ROOT)) {
					for (int i = 0; i < unitList.length; i++) {
						if (!this.unitPickData.getLstLeftModel().contains(
								unitList[i])) {
							try {
								comm.removeUserFromUnit(
										((UnitValue) unitList[i].getObject()),
										user.getUserName());
							} catch (Exception exe) {
								log.error("Error removing unser from unit: ",
										exe);
							}
						}
					}
					for (int i = 0; i < this.unitPickData.getLstLeftModel()
							.getSize(); i++) {
						UnitValue unit = (UnitValue) ((DropDownHolder) this.unitPickData
								.getLstLeftModel().getElementAt(i)).getObject();
						if (!ArrayUtils.contains(units, unit)) {
							try {
								comm.addUser2Unit(user, unit);
							} catch (Exception exe) {
								log.error("Error adding user to unit: ", exe);
							}
						}
					}
				}
			} catch (Exception exe) {
				log.error(exe);
			}
		}
		this.unitPickData.setModified(false);
		this.groupPickData.setModified(false);
		tblUser.setEnabled(true);
	}

	/**
	 * 
	 */
	private class UserListSelectionListener implements ListSelectionListener {
		private PanUnitGroupPerUser caller;

		public UserListSelectionListener(PanUnitGroupPerUser aCaller) {
			caller = aCaller;
		}

		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting()) {
				return;
			}
			boolean isSiteRoot = false;
			if (tblUser.getSelectedRow() >= 0) {
				if (unitPickData.isModified() || groupPickData.isModified()) {
					int i = JOptionPane.showConfirmDialog(caller, rb
							.getString("dialog.wantToSave"), rb
							.getString("dialog.title"),
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);

					if (i == JOptionPane.YES_OPTION) {
						setCursor(new Cursor(Cursor.WAIT_CURSOR));
						saveChanges(currentSelected);
					} else {
						unitPickData.setModified(false);
						groupPickData.setModified(false);
					}
				}
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				setPickerEnabled(false);
				currentSelected = tblUser.getSelectedRow();

				UserValue user = getSelectedUser(currentSelected);
				try {
					if (comm.isUserInRole(user, UserRights.SITE_ROOT)) {
						unitPickData.getLstLeftModel().removeAllElements();
						unitPickData.getLstLeftModel().addAll(allUnits);
						unitPickData.getLstRightModel().removeAllElements();
						isSiteRoot = true;
					} else {
						UnitValue[] vec = comm
								.getUnits4User(user.getUserName());
						unitPickData.getLstRightModel().removeAllElements();
						unitPickData.getLstLeftModel().removeAllElements();
						for (int i = 0; i < vec.length; i++) {
							DropDownHolder ddh = new DropDownHolder(vec[i],
									vec[i].getName());
							unitPickData.getLstLeftModel().addElement(ddh);
						}
						for (int i = 0; i < allUnits.length; i++) {
							if (!unitPickData.getLstLeftModel().contains(
									allUnits[i])) {
								unitPickData.getLstRightModel().addElement(
										allUnits[i]);
							}
						}
					}
					GroupValue[] grpHave = comm.getGroups4User(user
							.getUserName());
					groupPickData.getLstRightModel().clear();
					groupPickData.getLstLeftModel().clear();
					groupPickData.getLstRightModel().addAll(
							getDiffGroups(grpHave));
					if (grpHave != null) {
						DropDownHolder[] groups = new DropDownHolder[grpHave.length];
						for (int i = grpHave.length - 1; i >= 0; i--) {
							groups[i] = new DropDownHolder(grpHave[i],
									grpHave[i].getGroupName());
						}
						groupPickData.getLstLeftModel().addAll(groups);
					}
				} catch (Exception exe) {
					log.error(exe);
				}
			}
			panUnitPick.setEnabled(!isSiteRoot);
			panGroupPick.setEnabled(true);
			setCursor(Cursor.getDefaultCursor());
		}
	}

	public boolean exitPerformed(ExitEvent e) {
		try {
			if (unitPickData.isModified() || groupPickData.isModified()) {
				int i = JOptionPane.showConfirmDialog(this, rb
						.getString("dialog.wantToSave"), rb
						.getString("dialog.title"),
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (i == JOptionPane.YES_OPTION) {
					saveChanges(currentSelected);
				} else if (i == JOptionPane.CANCEL_OPTION) {
					return false;
				}
			}
		} catch (Exception ex) {
		}
		return true;
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals(Constants.ACTION_CHANGE_USERACCOUNTS)) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					reload();
				}
			});
			t.setPriority(Thread.NORM_PRIORITY);
			t.start();
		}
	}

}