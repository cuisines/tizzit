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

import static de.juwimm.cms.common.Constants.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import de.juwimm.cms.authorization.vo.GroupValue;
import de.juwimm.cms.authorization.vo.UserUnitsGroupsValue;
import de.juwimm.cms.authorization.vo.UserValue;
import de.juwimm.cms.vo.UnitSlimValue;

/**
 * @author Dirk Bogun
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * , Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class UserTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 7715989623457225719L;
	private Vector<String> columnNames = new Vector<String>(5);
	private Vector<Object> vRows = new Vector<Object>();
	private boolean editable = false;

	public UserTableModel(boolean editable) {
		super();
		this.editable = editable;
		if (editable) {
			columnNames = new Vector<String>(5);
		} else {
			columnNames = new Vector<String>(4);
		}
		columnNames.addElement(rb.getString("user.userName"));
		columnNames.addElement(rb.getString("user.firstName"));
		columnNames.addElement(rb.getString("user.lastName"));
		columnNames.addElement(rb.getString("user.eMail"));
		if (editable) {
			columnNames.addElement(rb.getString("user.groups"));
		}
	}

	public UserTableModel(UserValue[] vec) {
		this(true);
		addRows(vec);
	}

	public void addRow(UserValue dao) {
		Object[] obj = new Object[6];
		obj[0] = dao.getUserName();
		obj[1] = dao.getFirstName() != null ? dao.getFirstName() : "";
		obj[2] = dao.getLastName() != null ? dao.getLastName() : "";
		obj[3] = dao.getEmail() != null ? dao.getEmail() : "";
		obj[4] = "";
		obj[5] = dao;
		addRow(obj);
	}

	public void addRows(UserValue[] vec) {
		for (int i = 0; i < vec.length; i++) {
			UserValue dao = vec[i];
			this.addRow(dao);
		}
	}

	public UserTableModel(UserUnitsGroupsValue[] vec, boolean showGroups) {
		this(showGroups);
		addRows(vec);
	}

	public void addRow(UserUnitsGroupsValue dao) {
		Object[] obj = new Object[7];
		obj[0] = dao.getUser().getUserName();
		obj[1] = dao.getUser().getFirstName() != null ? dao.getUser().getFirstName() : "";
		obj[2] = dao.getUser().getLastName() != null ? dao.getUser().getLastName() : "";
		obj[3] = dao.getUser().getEmail() != null ? dao.getUser().getEmail() : "";
		obj[4] = this.getOrderedGroups(dao.getGroups());
		obj[5] = this.getOrderedUnits(dao.getUnits());
		obj[6] = dao.getUser();
		addRow(obj);
	}

	public void addRows(UserUnitsGroupsValue[] vec) {
		for (int i = 0; i < vec.length; i++) {
			UserUnitsGroupsValue dao = vec[i];
			this.addRow(dao);
		}
	}

	public int getColumnCount() {
		return columnNames.size();
	}

	public int getRowCount() {
		return vRows.size();
	}

	@Override
	public String getColumnName(int col) {
		if (columnNames.size() >= col) { return columnNames.elementAt(col); }
		return "";
	}

	public void setColumnName(int col, String name) {
		if (columnNames.size() == 0 || col >= columnNames.size()) {
			columnNames.addElement(name);
		} else {
			columnNames.setElementAt(name, col);
		}
	}

	public Object getValueAt(int row, int col) {
		try {
			return ((Object[]) vRows.elementAt(row))[col];
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		Object[] array = (Object[]) vRows.elementAt(row);
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
		if (editable) { return (col != 0 && col != 4); }
		return false;
	}

	public void addRow(Object[] obj) {
		vRows.addElement(obj);
		fireTableRowsInserted(getRowCount(), getRowCount());
	}

	public void insertRow(Object[] obj) {
		if (getRowCount() == 0) {
			vRows.addElement(obj);
		} else {
			vRows.insertElementAt(obj, 0);
		}
		fireTableRowsInserted(getRowCount(), getRowCount());
	}

	public void removeRow(int i) {
		vRows.removeElementAt(i);
		this.fireTableRowsDeleted(i, i);
	}

	public Object[] getRow(int i) {
		return (Object[]) vRows.elementAt(i);
	}

	private String getOrderedGroups(GroupValue[] groups) {
		StringBuffer sb = new StringBuffer();
		if (groups != null) {
			Arrays.sort(groups, new GroupNameComparator());
			for (int i = 0; i < groups.length; i++) {
				if (sb.length() > 0) sb.append(", ");
				sb.append(groups[i].getGroupName());
			}
		}
		
		return sb.toString();
	}

	private String getOrderedUnits(UnitSlimValue[] units) {
		StringBuffer sb = new StringBuffer();
		if (units != null) {
			Arrays.sort(units, new UnitNameComparator());
			for (int i = 0; i < units.length; i++) {
				if (sb.length() > 0) sb.append(", ");
				sb.append(units[i].getName());
			}
		}
		
		return sb.toString();
	}

	public class GroupNameComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			GroupValue gv1 = (GroupValue) o1;
			GroupValue gv2 = (GroupValue) o2;
			if (gv1.getGroupName() == null) return -1;
			if (gv2.getGroupName() == null) return 1;
			return gv1.getGroupName().compareTo(gv2.getGroupName());
		}

	}

	public class UnitNameComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			UnitSlimValue usv1 = (UnitSlimValue) o1;
			UnitSlimValue usv2 = (UnitSlimValue) o2;
			if (usv1.getName() == null) return -1;
			if (usv2.getName() == null) return 1;
			return usv1.getName().compareTo(usv2.getName());
		}

	}

}