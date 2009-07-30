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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import de.juwimm.cms.gui.PanTaskTable;
import de.juwimm.cms.vo.TaskValue;

/**
 * <p>Title: juwimm cms</p>
 * <p>Description: content management system</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: juwi macmillan group gmbh</p>
 * @author Dirk Bogun
 * @version $Id$
 */
public class TaskTableModel extends AbstractTableModel {
	private SimpleDateFormat sdf = new SimpleDateFormat(rb.getString("General.ShortDateTimeFormat"));
	private Vector columnNames = new Vector(3);
	private Vector vRows = new Vector();

	public TaskTableModel() {
		super();
		columnNames.addElement(rb.getString("TaskTableModel.kind"));
		columnNames.addElement(rb.getString("TaskTableModel.unit"));
		columnNames.addElement(rb.getString("TaskTableModel.from"));
		columnNames.addElement(rb.getString("TaskTableModel.createDate"));
	}

	public TaskTableModel(String column) {
		this();
		StringTokenizer st = new StringTokenizer(column, ";");
		while (st.hasMoreTokens()) {
			columnNames.addElement(st.nextToken());
		}
	}

	public TaskTableModel(TaskValue[] tdao) {
		this();
		addRows(tdao);
	}

	public void addRows(TaskValue[] tdao) {
		for (int i = 0; i < tdao.length; i++) {
			TaskValue dao = tdao[i];
			Object[] obj = new Object[5];
			obj[0] = getName4Type(dao.getTaskType());
			if (dao.getUnit() != null) {
				obj[1] = dao.getUnit().getName();
			} else {
				obj[1] = "";
			}
			if (dao.getSender() != null) {
				obj[2] = dao.getSender().getUserName();
			} else {
				obj[2] = "";
			}
			obj[3] = this.sdf.format(new Date(dao.getCreationDate()));
			obj[4] = dao;
			addRow(obj);
		}
	}

	private String getName4Type(byte b) {
		switch (b) {
			case 0:
				return PanTaskTable.APPROVE;
			case 1:
				return PanTaskTable.MESSAGE;
			case 2:
				return PanTaskTable.REJECT;
			default:
				return "unknown";
		}
	}

	public int getColumnCount() {
		return columnNames.size();
	}

	public int getRowCount() {
		return vRows.size();
	}

	public String getColumnName(int col) {
		if (columnNames.size() >= col) {
			return (String) columnNames.elementAt(col);
		}
		return null;
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
	public boolean isCellEditable(int row, int col) {
		//Note that the data/cell address is constant,
		//no matter where the cell appears onscreen.

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
}
