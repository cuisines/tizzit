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
package de.juwimm.cms.gui.table;

import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import de.juwimm.cms.authorization.vo.UserValue;
import de.juwimm.cms.common.Constants;

/**
 * <p>Title: Tizzit</p>
 * <p>Description: Enterprise Content Management</p>
 * <p>Copyright: Copyright (c) 2002, 2003</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class SiteUserTableModel extends DefaultTableModel {
	private final ResourceBundle rb = Constants.rb;
	private TableSorter tblUserSorter = null;
	private final Vector<String> columnNames = new Vector<String>(4);

	//Vector dataVector = new Vector(); // implemented in super-class

	public SiteUserTableModel() {
		super();
		columnNames.addElement(rb.getString("panel.sitesAdministration.tblUserModel.connected"));
		columnNames.addElement(rb.getString("user.userName"));
		columnNames.addElement(rb.getString("user.firstName"));
		columnNames.addElement(rb.getString("user.lastName"));
	}

	public void setTableSorter(TableSorter tblUserSorter) {
		this.tblUserSorter = tblUserSorter;
	}

	public void addRow(UserValue vo) {
		Object[] obj = new Object[5];
		obj[0] = new Boolean(false);
		obj[1] = vo.getUserName();
		obj[2] = vo.getFirstName();
		obj[3] = vo.getLastName();
		obj[4] = vo;
		addRow(obj);
	}

	public void addRows(UserValue[] vos) {
		if (vos != null) {
			for (int i = 0; i < vos.length; i++) {
				addRow(vos[i]);
			}
		}
	}

	public void setSelectedUsers(String[] selUsers) {
		for (int i = 0; i < dataVector.size(); i++) {
			UserValue vo = (UserValue) tblUserSorter.getValueAt(i, 4);
			boolean foundVal = false;
			if (selUsers != null) {
				for (int u = 0; u < selUsers.length; u++) {
					if (selUsers[u].equals(vo.getUserName())) {
						foundVal = true;
						break;
					}
				}
			}
			tblUserSorter.setValueAt(new Boolean(foundVal), i, 0);
		}
	}

	public String[] getSelectedUsers() {
		ArrayList<String> al = new ArrayList<String>(dataVector.size());
		for (int i = 0; i < dataVector.size(); i++) {
			if (((Boolean) ((Object[]) dataVector.elementAt(i))[0]).booleanValue()) {
				al.add(((UserValue) ((Object[]) dataVector.elementAt(i))[4]).getUserName());
			}
		}
		return al.toArray(new String[0]);
	}

	@Override
	public int getColumnCount() {
		return columnNames.size();
	}

	@Override
	public int getRowCount() {
		return dataVector.size();
	}

	@Override
	public String getColumnName(int col) {
		if (columnNames.size() >= col) { return columnNames.elementAt(col); }
		return null;
	}

	public void setColumnName(int col, String name) {
		if (columnNames.size() == 0 || col >= columnNames.size()) {
			columnNames.addElement(name);
		} else {
			columnNames.setElementAt(name, col);
		}
	}

	@Override
	public Object getValueAt(int row, int col) {
		try {
			return ((Object[]) dataVector.elementAt(row))[col];
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		Object[] array = (Object[]) dataVector.elementAt(row);
		array[col] = value;
		fireTableCellUpdated(row, col);
	}

	/*
	 * JTable uses this method to determine the default renderer/
	 * editor for each cell.  If we didn't implement this method,
	 * then the last column would contain text ("true"/"false"),
	 * rather than a check box.
	 */
	@Override
	public Class getColumnClass(int c) {
		try {
			return getValueAt(0, c).getClass();
		} catch (Exception ex) {
			return null;
		}
	}

	/*
	 * Don't need to implement this method unless your table's
	 * editable.
	 */
	@Override
	public boolean isCellEditable(int row, int col) {
		//Note that the data/cell address is constant,
		//no matter where the cell appears onscreen.
		return (col == 0);
	}

	@Override
	public void addRow(Object[] obj) {
		dataVector.addElement(obj);
		fireTableRowsInserted(getRowCount(), getRowCount());
	}

	public void insertRow(Object[] obj) {
		if (getRowCount() == 0) {
			dataVector.addElement(obj);
		} else {
			dataVector.insertElementAt(obj, 0);
		}
		fireTableRowsInserted(getRowCount(), getRowCount());
	}

	@Override
	public void removeRow(int i) {
		dataVector.removeElementAt(i);
		this.fireTableRowsDeleted(i, i);
	}

	public Object[] getRow(int i) {
		return (Object[]) dataVector.elementAt(i);
	}

}
