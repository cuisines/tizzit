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
package de.juwimm.cms.deploy.panel.table;

import static de.juwimm.cms.common.Constants.*;

import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import de.juwimm.cms.vo.ViewComponentValue;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PagesTableModel extends AbstractTableModel {
	private boolean[] columnEditable = {false, false, true};
	private Vector<String> columnNames = new Vector<String>(3);
	private Vector<Object> vRows = new Vector<Object>();

	public PagesTableModel() {
		super();
		columnNames.addElement(rb.getString("wizard.author.treeheader.name"));
		columnNames.addElement(rb.getString("wizard.author.treeheader.action"));
		columnNames.addElement(rb.getString("wizard.author.treeheader.selection"));
	}

	public PagesTableModel(String column) {
		this();
		StringTokenizer st = new StringTokenizer(column, ";");
		while (st.hasMoreTokens()) {
			columnNames.addElement(st.nextToken());
		}
	}

	public PagesTableModel(Vector vec) {
		this();
		addRows(vec);
	}

	public void addRows(Vector vec) {
		String s;

		for (int i = 0; i < vec.size(); i++) {
			ViewComponentValue dao = (ViewComponentValue) vec.elementAt(i);
			Object[] obj = new Object[5];

			if ((s = dao.getDisplayLinkName()) != null) {
				obj[0] = s;
			} else {
				obj[0] = "";
			}

			obj[1] = new Integer(dao.getDeployCommand());
			obj[2] = new Boolean(false);

			if ((s = dao.getPath2Unit()) != null) {
				obj[3] = s;
			} else {
				obj[3] = "";
			}

			obj[4] = dao;
			addRow(obj);
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
			return columnNames.elementAt(col);
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
	@SuppressWarnings(value = "unchecked")
	public Class getColumnClass(int c) {
		try {
			return getValueAt(0, c).getClass();
		} catch (Exception ex) {
			return null;
		}
	}

	public boolean isCellEditable(int row, int col) {
		return columnEditable[col];
	}

	public void setColumnEditable(int i, boolean bool) {
		columnEditable[i] = bool;
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
