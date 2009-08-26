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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import de.juwimm.cms.Messages;
import de.juwimm.cms.authorization.vo.GroupValue;
import de.juwimm.cms.authorization.vo.UserUnitsGroupsValue;
import de.juwimm.cms.authorization.vo.UserValue;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.exceptions.NeededFieldsMissingException;
import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.exceptions.UserHasNoUnitsException;
import de.juwimm.cms.gui.PasswordDialog;
import de.juwimm.cms.gui.controls.ReloadablePanel;
import de.juwimm.cms.gui.controls.UnloadablePanel;
import de.juwimm.cms.gui.event.ExitEvent;
import de.juwimm.cms.gui.event.ExitListener;
import de.juwimm.cms.gui.table.TableSorter;
import de.juwimm.cms.gui.table.UserTableModel;
import de.juwimm.cms.remote.ClientServiceSpringException;
import de.juwimm.cms.util.ActionHub;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.UnitValue;
import de.juwimm.swing.CustomComboBoxModel;
import de.juwimm.swing.DropDownHolder;

/**
 * <p>Title: ConQuest</p>
 * <p>Description: Enterprise Content Management<</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanUser extends JPanel implements UnloadablePanel, ExitListener, ReloadablePanel {
	private static final long serialVersionUID = 7552673537490612921L;
	private static Logger log = Logger.getLogger(PanUser.class);
	private UnitValue lastSelectedUnit = null;
	private GroupValue lastSelectedGroup = null;
	private UserTableModel tblModel = null;
	private TableSorter tblSorter = null;
	private HashSet<String> userDeleted;
	private HashSet<String> userEdited;
	private final JTable tblUser = new JTable();
	private final JLabel lblUsers = new JLabel();
	private final JButton cmdDelete = new JButton();
	private final JButton cmdAdd = new JButton();
	private final JButton cmdSave = new JButton();
	private final Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private final JComboBox cbxUnits = new JComboBox();
	private final JComboBox cbxGroups = new JComboBox();
	private final JLabel lblUnitFilter = new JLabel();
	private final JLabel lblGroupFilter = new JLabel();
	private final JButton cmdExport = new JButton();
	private final JButton cmdPasswd = new JButton();
	private final boolean mayUserSeeGroups = comm.isUserInRole(UserRights.SITE_ROOT);

	public PanUser() {
		ActionHub.addExitListener(this);
		try {
			jbInit();
			setDoubleBuffered(true);
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	void jbInit() throws Exception {
		this.setLayout(new GridBagLayout());
		tblUser.getTableHeader().setFont(new Font("SansSerif", 0, 13));
		Dimension dim = tblUser.getTableHeader().getPreferredSize();
		tblUser.getTableHeader().setPreferredSize(new Dimension(dim.width, 22));
		tblUser.setRowHeight(22);
		tblUser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		lblUsers.setText(rb.getString("panel.panelCmsUser.user"));
		cmdDelete.setToolTipText("");
		cmdDelete.setText(rb.getString("dialog.delete"));
		cmdDelete.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdDeleteActionPerformed(e);
			}
		});
		cmdAdd.setText(rb.getString("dialog.add"));
		cmdAdd.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdAddActionPerformed(e);
			}
		});
		cmdSave.setText(rb.getString("dialog.save"));
		cmdSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		lblUnitFilter.setText(rb.getString("panel.panelCmsUser.filterUnits"));
		lblGroupFilter.setText(rb.getString("panel.panelCmsUser.filterGroups"));
		cmdExport.setRolloverEnabled(false);
		cmdExport.setText(rb.getString("panel.panelCmsUser.export"));
		cmdExport.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdExportActionPerformed(e);
			}
		});
		cmdPasswd.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdPasswdActionPerformed(e);
			}
		});
		cmdPasswd.setText(rb.getString("panel.panelCmsUser.setPassword"));
		cmdPasswd.setToolTipText("");
		this.add(cmdAdd, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));
		this.add(lblUsers, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 0), 0, 0));
		this.add(new JScrollPane(tblUser), new GridBagConstraints(0, 1, 2, 3, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 10, 0, 0), 0, 0));
		this.add(cmdDelete, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));
		this.add(cmdPasswd, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));
		this.add(lblUnitFilter, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 0), 0, 0));
		this.add(cbxUnits, new GridBagConstraints(0, 5, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));
		if (this.mayUserSeeGroups) {
			this.add(lblGroupFilter, new GridBagConstraints(0, 6, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 0), 0, 0));
			this.add(cbxGroups, new GridBagConstraints(0, 7, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));
		}
		this.add(cmdSave, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		this.add(cmdExport, new GridBagConstraints(3, 8, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));

		cbxUnits.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					selectFilterAction();
				}
			}
		});
		cbxGroups.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					selectFilterAction();
				}
			}
		});

		userDeleted = new HashSet<String>(3);
		userEdited = new HashSet<String>(3);
	}

	private void selectFilterAction() {
		try {
			if (userDeleted.size() != 0 || userEdited.size() != 0) {
				int i = JOptionPane.showConfirmDialog(UIConstants.getMainFrame(), rb.getString("dialog.wantToSave"), rb.getString("dialog.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (i == JOptionPane.YES_OPTION) {
					saveChanges();
				} else {
					userDeleted = new HashSet<String>(3);
					userEdited = new HashSet<String>(3);
				}
			}
		} catch (Exception ex) {
		}
		lastSelectedUnit = (UnitValue) ((DropDownHolder) cbxUnits.getSelectedItem()).getObject();
		if (this.mayUserSeeGroups) {
			lastSelectedGroup = (GroupValue) ((DropDownHolder) cbxGroups.getSelectedItem()).getObject();
		}
		setEnabled(false);
		fillTable(lastSelectedUnit.getUnitId(), (lastSelectedGroup == null ? -1 : lastSelectedGroup.getGroupId()));
		setEnabled(true);
		if (lastSelectedUnit.getName().equalsIgnoreCase(rb.getString("panel.panelCmsUser.showAll")) && !comm.isUserInRole(UserRights.SITE_ROOT)) {
			cmdAdd.setEnabled(false);
			cmdAdd.setToolTipText(rb.getString("panel.panelCmsUser.btnAddToolTip"));
		} else {
			cmdAdd.setEnabled(true);
			cmdAdd.setToolTipText("");
		}
	}

	public int getLastSelectedUnit() {
		if (lastSelectedUnit == null) return 0;
		return lastSelectedUnit.getUnitId();
	}

	public void reload() {
		this.setEnabled(false);
		try {
			fillUnits();
			if (this.mayUserSeeGroups) {
				fillGroups();
			}
		} catch (UserHasNoUnitsException ex) {
			log.error("here was a UserHasNoUnitsException");
		} catch (Exception ex) {
		}
		try {
			selectFilterAction();
		} catch (Exception ex) {
			log.error("Error in selectFilterAction: " + ex.getMessage());
		}
	}

	private void setButtonsEnabled(boolean enabled) {
		cmdAdd.setEnabled(enabled);
		cmdDelete.setEnabled(enabled);
		cmdExport.setEnabled(enabled);
		cmdPasswd.setEnabled(enabled);
		cmdSave.setEnabled(enabled);
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (!enabled) {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		} else {
			setCursor(Cursor.getDefaultCursor());
		}
		this.setButtonsEnabled(enabled);
		this.tblUser.setEnabled(enabled);
		this.cbxUnits.setEnabled(enabled);
		this.cbxGroups.setEnabled(enabled);
	}

	public void unload() {
		setButtonsEnabled(false);
		this.tblUser.setEnabled(false);
		this.cbxUnits.setEnabled(false);
		this.cbxGroups.setEnabled(false);
		try {
			if (userDeleted.size() != 0 || userEdited.size() != 0) {
				int i = JOptionPane.showConfirmDialog(UIConstants.getMainFrame(), rb.getString("dialog.wantToSave"), rb.getString("dialog.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (i == JOptionPane.YES_OPTION) {
					saveChanges();
				} else {
					userDeleted = new HashSet<String>(3);
					userEdited = new HashSet<String>(3);
					this.reload();
				}
			}
		} catch (Exception ex) {
		}
		this.cbxGroups.setEnabled(true);
		this.cbxUnits.setEnabled(true);
		this.tblUser.setEnabled(true);
		setButtonsEnabled(true);
	}

	private void fillUnits() throws Exception {
		UnitValue[] units = null;
		if (comm.isUserInRole(UserRights.SITE_ROOT)) {
			units = comm.getAllUnits();
		} else { //UNIT ADMIN
			units = comm.getUnits();
			if (units.length == 0) { throw new UserHasNoUnitsException(rb.getString("exception.UserHasNoUnitsException")); }
		}
		UnitValue[] unitss = new UnitValue[units.length + 1];
		unitss[0] = new UnitValue();
		unitss[0].setUnitId(0);
		unitss[0].setName(rb.getString("panel.panelCmsUser.showAll"));

		for (int i = 0; i < units.length; i++) {
			unitss[i + 1] = units[i];
		}
		units = unitss;

		cbxUnits.setModel(new CustomComboBoxModel(units, "getName"));
		/* BUG 3951, always start with list with all users
		if (!comm.isUserInRole(UserRights.SITE_ROOT) && unitss.length >= 2) {
			// BUG 739, preselect the first Unit of this Admin
			cbxUnits.setSelectedIndex(1);
		}
		*/
	}

	private void fillGroups() throws Exception {
		GroupValue[] allGroups = null;
		allGroups = comm.getAllGroups();
		if (allGroups == null) allGroups = new GroupValue[0];
		GroupValue[] allGroupsPlusOne = new GroupValue[allGroups.length + 1];
		allGroupsPlusOne[0] = new GroupValue();
		allGroupsPlusOne[0].setGroupId(0);
		allGroupsPlusOne[0].setGroupName(rb.getString("panel.panelCmsUser.showAll"));

		for (int i = 0; i < allGroups.length; i++) {
			allGroupsPlusOne[i + 1] = allGroups[i];
		}
		allGroups = allGroupsPlusOne;

		cbxGroups.setModel(new CustomComboBoxModel(allGroups, "getGroupName"));
	}

	private void fillTable(int unitId, int groupId) {
		try {
			UserUnitsGroupsValue[] user = comm.getUserUnitsGroups4UnitAndGroup(unitId, groupId);
			if (user != null) {
				tblModel = new UserTableModel(user, this.mayUserSeeGroups);
			} else {
				tblModel = new UserTableModel(false);
			}
			tblModel.addTableModelListener(new MyTableModelListener());

			tblSorter = new TableSorter(tblModel, tblUser.getTableHeader());
			tblUser.setModel(tblSorter);
			setEditor();
		} catch (Exception exe) {
			log.error("Error filling table", exe);
		}
	}

	private void setEditor() {
		try {
			/*
			DefaultCellEditor ed0 = new DefaultCellEditor(new JTextField());
			ed0.setClickCountToStart(1);
			DefaultCellEditor ed1 = new DefaultCellEditor(new JTextField());
			ed1.setClickCountToStart(1);
			DefaultCellEditor ed2 = new DefaultCellEditor(new JTextField());
			ed2.setClickCountToStart(1);
			DefaultCellEditor ed3 = new DefaultCellEditor(new JTextField());
			ed3.setClickCountToStart(1);
			*/

			/*
			tblUser.setDefaultEditor(tblUser.getColumnClass(0), ed0);
			tblUser.setDefaultEditor(tblUser.getColumnClass(1), ed1);
			tblUser.setDefaultEditor(tblUser.getColumnClass(2), ed2);
			tblUser.setDefaultEditor(tblUser.getColumnClass(3), ed3);
			*/

			TableColumn column = tblUser.getColumnModel().getColumn(0);
			column.setPreferredWidth(50);
			column = tblUser.getColumnModel().getColumn(1);
			column.setPreferredWidth(50);
			column = tblUser.getColumnModel().getColumn(2);
			column.setPreferredWidth(50);
		} catch (Exception ex) {
		}
	}

	/**
	 * 
	 */
	class MyTableModelListener implements TableModelListener {

		public void tableChanged(TableModelEvent e) {
			if (e.getType() == TableModelEvent.UPDATE) {
				int row = e.getFirstRow();
				int col = e.getColumn();
				String cellContent = (String) tblModel.getValueAt(row, col);
				ArrayList<String> assertFields = new ArrayList<String>();
				if ((tblSorter.getColumnName(e.getColumn())).equals(rb.getString("user.eMail"))) {
					try {
						if (!checkEmail(cellContent, true)) {
							String msg = rb.getString("exception.EmailIsNotValid");
							JOptionPane.showMessageDialog(UIConstants.getMainFrame(), msg, rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
							String oldValue = ((UserValue) tblModel.getValueAt(row, 6)).getEmail();
							if (checkEmail(oldValue, true)) {
								tblModel.setValueAt(oldValue, row, col);
							} else {
								tblModel.setValueAt("", row, col);
							}
							return;
						}
					} catch (Exception ex) {
					}
				} else if ((tblSorter.getColumnName(e.getColumn())).equals(rb.getString("user.firstName"))) {
					if (cellContent == null || "".equalsIgnoreCase(cellContent)) {
						assertFields.add("user.firstName");
						String oldValue = ((UserValue) tblModel.getValueAt(row, 6)).getFirstName();
						if (oldValue != null && oldValue.length() > 0) {
							tblModel.setValueAt(oldValue, row, col);
						} else {
							tblModel.setValueAt("<>", row, col);
						}
					}
				} else if ((tblSorter.getColumnName(e.getColumn())).equals(rb.getString("user.lastName"))) {
					if (cellContent == null || "".equalsIgnoreCase(cellContent)) {
						assertFields.add("user.lastName");
						String oldValue = ((UserValue) tblModel.getValueAt(row, 6)).getLastName();
						if (oldValue != null && oldValue.length() > 0) {
							tblModel.setValueAt(oldValue, row, col);
						} else {
							tblModel.setValueAt("<>", row, col);
						}
					}
				}
				if (assertFields.size() > 0) {
					StringBuffer list = new StringBuffer();
					for (int i = 0; i < assertFields.size(); i++) {
						if (i != 0) {
							list.append(",");
						}
						list.append(assertFields.get(i));
					}
					String msg = rb.getString("exception.NeededFieldsMissingException");
					NeededFieldsMissingException nfme = new NeededFieldsMissingException();
					nfme.setMissingFields(list.toString());
					msg = msg + nfme.getMissingFieldsLocaleString();
					JOptionPane.showMessageDialog(UIConstants.getMainFrame(), msg, rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
					return;
				}
				String id = (String) tblModel.getValueAt(e.getFirstRow(), 0);
				userEdited.add(id);
			}
		}

		private final boolean checkEmail(String mail, boolean mayBeEmpty) {
			if (mayBeEmpty && mail != null && mail.length() == 0) return true;
			Pattern p = Pattern.compile("^[A-Za-z_0-9\\.-]+@[A-Za-z_0-9\\.-]+\\.[a-zA-Z]+");
			Matcher m = p.matcher(mail);
			return m.matches();
		}
	}

	private void saveChanges() {
		UserValue user = null;
		Iterator<String> it = null;

		it = userEdited.iterator();
		while (it.hasNext()) {
			int tmr = this.getTableModelRow4Username(it.next());
			user = ((UserValue) tblModel.getValueAt(tmr, 6));
			user.setFirstName((String) tblModel.getValueAt(tmr, 1));
			user.setLastName((String) tblModel.getValueAt(tmr, 2));
			user.setEmail((String) tblModel.getValueAt(tmr, 3));
			if (user.getConfigXML() == null) user.setConfigXML("");
			try {
				comm.updateUser(user);
				it.remove();
				userEdited.remove(user.getUserName());
			} catch (Exception ex) {
				if (ex instanceof ClientServiceSpringException || ex.getCause() instanceof UserException) {
					String persmissionMessage = Messages.getString("user.noUpdatePermision.exception", user.getUserName());
					JOptionPane.showMessageDialog(UIConstants.getMainFrame(), persmissionMessage, rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(UIConstants.getMainFrame(), ex.getMessage(), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		it = userDeleted.iterator();
		while (it.hasNext()) {
			String userNameId = it.next();
			try {
				comm.deleteUser(userNameId);
				it.remove();
				userDeleted.remove(userNameId);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(UIConstants.getMainFrame(), ex.getMessage(), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
			}
		}
		ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Constants.ACTION_CHANGE_USERACCOUNTS));
	}

	private int getTableModelRow4Username(String userNameId) {
		int len = tblModel.getRowCount();
		for (int i = 0; i < len; i++) {
			if (userNameId.equals(tblSorter.getValueAtInModel(i, 0))) { return i; }
		}
		return -1;
	}

	void cmdAddActionPerformed(ActionEvent e) {
		CreateNewUserDialog dialog = new CreateNewUserDialog(this);
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
	}

	void cmdDeleteActionPerformed(ActionEvent e) {
		String id = (String) tblSorter.getValueAt(tblUser.getSelectedRow(), 0);

		if (userEdited.contains(id)) {
			userEdited.remove(id);
		}
		if (id.equals(comm.getUser().getUserName())) {
			JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("exception.cantDeleteMyself"), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
		} else {
			userDeleted.add(id);
			int tmr = getTableModelRow4Username(id);
			tblModel.removeRow(tmr);
		}
	}

	public void save() {
		DefaultCellEditor ed = (DefaultCellEditor) tblUser.getCellEditor();
		if (ed != null) {
			int r = tblUser.getEditingRow();
			int c = tblUser.getEditingColumn();

			tblSorter.setValueAt(ed.getCellEditorValue(), r, c);
			ed.stopCellEditing();
		}
		tblUser.getSelectionModel().clearSelection();
		saveChanges();
	}

	public boolean exitPerformed(ExitEvent e) {
		try {
			if (userDeleted.size() != 0 || userEdited.size() != 0) {
				int i = JOptionPane.showConfirmDialog(UIConstants.getMainFrame(), rb.getString("dialog.wantToSave"), rb.getString("dialog.title"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

				if (i == JOptionPane.YES_OPTION) {
					saveChanges();
				} else if (i == JOptionPane.CANCEL_OPTION) { return false; }
			}
		} catch (Exception ex) {
		}
		return true;
	}

	void cmdExportActionPerformed(ActionEvent e) {
		JFileChooser choose = new JFileChooser();
		FileFilter filter = choose.getFileFilter();
		choose.removeChoosableFileFilter(filter);
		choose.setFileFilter(new CsvFileFilter());
		int ret = choose.showSaveDialog(this);
		if (ret == JFileChooser.APPROVE_OPTION) {
			File file = choose.getSelectedFile();
			ExportToCsv ex = new ExportToCsv(file);
			Thread t = new Thread(ex);
			t.setPriority(Thread.NORM_PRIORITY);
			t.start();
		}
	}

	/**
	 * 
	 */
	private final class ExportToCsv implements Runnable {
		private File file;

		public ExportToCsv(File f) {
			file = f;
		}

		public void run() {
			int vorname = tblSorter.findColumn(rb.getString("user.firstName"));
			int nachname = tblSorter.findColumn(rb.getString("user.lastName"));
			int mail = tblSorter.findColumn(rb.getString("user.eMail"));
			int roles = tblSorter.findColumn(rb.getString("user.groups"));

			String[][] values = new String[tblSorter.getRowCount()][5];

			for (int i = 0; i < tblSorter.getRowCount(); i++) {
				Object current = tblSorter.getValueAt(i, nachname);
				values[i][0] = current != null ? current.toString() : "";
				current = tblSorter.getValueAt(i, vorname);
				values[i][1] = current != null ? current.toString() : "";
				current = tblSorter.getValueAt(i, mail);
				values[i][2] = current != null ? current.toString() : "";
				current = tblSorter.getValueAt(i, roles);
				values[i][3] = current != null ? current.toString().replaceAll(",", "") : "";
				current = tblSorter.getValueAt(i, 5);
				values[i][4] = current != null ? current.toString().replaceAll(",", "") : "";
			}

			try {
				Properties prop = System.getProperties();
				String end = prop.getProperty("line.separator");
				if (file.getAbsolutePath().lastIndexOf(".csv") == -1) {
					String path = file.getAbsolutePath();
					path += ".csv";
					file = new File(path);
				}
				FileWriter fw = new FileWriter(file);
				StringBuffer buf = new StringBuffer();
				int z, i;
				for (i = 0; i < values.length; i++) {
					for (z = 0; z < 5; z++) {
						buf.append(values[i][z]);
						if (z != 4) {
							buf.append(",");
						} else {
							buf.append(end);
						}
					}
				}
				fw.write(buf.toString());
				fw.close();
			} catch (IOException ex) {
				log.error(ex.getMessage());
			}
		}
	}

	/**
	 * 
	 */
	private final class CsvFileFilter extends FileFilter {
		public CsvFileFilter() {
		}

		@Override
		public boolean accept(File f) {
			if (f.isDirectory()) { return true; }
			if (f.getName().lastIndexOf(".csv") == -1) { return false; }
			return true;
		}

		@Override
		public String getDescription() {
			return ".csv";
		}
	}

	void cmdPasswdActionPerformed(ActionEvent e) {
		String userName = (String) tblSorter.getValueAt(tblUser.getSelectedRow(), 0);
		PasswordDialog dialog = new PasswordDialog(userName);
		dialog.setVisible(true);
	}

}