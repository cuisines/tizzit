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
package de.juwimm.cms.content.frame.table;

import static de.juwimm.cms.common.Constants.*;

import java.math.BigDecimal;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import de.juwimm.cms.components.vo.PersonValue;
import de.juwimm.cms.util.UIConstants;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PersonTableModel extends AbstractTableModel {
	private Vector<String> columnNames = new Vector<String>(5);
	private Vector<Object> vRows = new Vector<Object>();

	public PersonTableModel() {
		super();
		columnNames.addElement(rb.getString("user.lastName"));
		columnNames.addElement(rb.getString("user.firstName"));
		columnNames.addElement(rb.getString("PanDBCPerson.birthDate"));
		columnNames.addElement(rb.getString("PanDBCPerson.jobTitle"));
	}

	public PersonTableModel(String column) {
		this();
		StringTokenizer st = new StringTokenizer(column, ";");
		while (st.hasMoreTokens()) {
			columnNames.addElement(st.nextToken());
		}
	}

	public PersonTableModel(Vector vec) {
		this();
		addRows(vec);
	}

	public void addRows(Vector vec) {
		for (int i = 0; i < vec.size(); i++) {
			PersonValue dao = (PersonValue) vec.elementAt(i);
			Object[] obj = new Object[5];
			String s;

			if ((s = dao.getLastname()) == null) {
				obj[0] = "";
			} else {
				obj[0] = s;
			}
			if ((s = dao.getFirstname()) == null) {
				obj[1] = "";
			} else {
				obj[1] = s;
			}
			
			if(dao.getBirthDay() == null) {
				obj[2] = "";
			} else {
				obj[2] = dao.getBirthDay();
			}

			if ((s = dao.getJobTitle()) == null) {
				obj[3] = "";
			} else {
				obj[3] = s;
			}
			obj[4] = dao;
			addRow(obj);
		}
	}

	public void addRows(Object[] obja) {
		for (int i = 0; i < obja.length; i++) {
			PersonValue dao = (PersonValue) obja[i];
			Object[] obj = new Object[5];
			String s;

			if ((s = dao.getLastname()) == null) {
				obj[0] = "";
			} else {
				obj[0] = s;
			}
			if ((s = dao.getFirstname()) == null) {
				obj[1] = "";
			} else {
				obj[1] = s;
			}

			if(dao.getBirthDay() == null) {
				obj[2] = "";
			} else {
				obj[2] = dao.getBirthDay();
			}
			
			if ((s = dao.getJobTitle()) == null) {
				obj[3] = "";
			} else {
				obj[3] = s;
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

	public PersonValue getPersonAtRow(int i) {
		return (PersonValue) this.getValueAt(i, 4);
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

		if (array[col] instanceof BigDecimal) {
			try {
				String s = (String) value;
				if (s.indexOf(".") == -1 && s.indexOf(",") == -1) {
					s = s + ",0";
				}
				BigDecimal big = new BigDecimal(s.replace(',', '.'));

				array[col] = big;
				fireTableCellUpdated(row, col);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(UIConstants.getMainFrame(), "Die Spalte \"" + getColumnName(col)
						+ "\" akzeptiert nur Zahlen.");
			}
		} else {

			array[col] = value;
			fireTableCellUpdated(row, col);
		}
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

	/*
	 * Don't need to implement this method unless your table's
	 * editable.
	 */
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	public void addRow(Object[] obj) {
		vRows.addElement(obj);
		//this.fireTableDataChanged();
		fireTableRowsInserted(getRowCount(), getRowCount());
	}

	public void removeRows() {
		vRows.removeAllElements();
	}

	public void removeRow(int i) {
		vRows.removeElementAt(i);
		this.fireTableRowsDeleted(i, i);
	}

	public Object[] getRow(int i) {
		return (Object[]) vRows.elementAt(i);
	}

}