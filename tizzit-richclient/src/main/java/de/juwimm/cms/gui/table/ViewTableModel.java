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

import java.math.BigDecimal;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.ViewDocumentValue;

/**
 * <p>Title: juwimm cms</p>
 * <p>Description: content management system</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: juwi macmillan group gmbh</p>
 * @author Dirk Bogun
 * @version $Id$
 */
public class ViewTableModel extends AbstractTableModel {
	private static ResourceBundle rb = Constants.rb;
	private Vector<String> columnNames = new Vector<String>(3);
	private Vector vRows = new Vector();

	public ViewTableModel() {
		super();
		columnNames.addElement(rb.getString("panel.panelCmsViews.viewType"));
		columnNames.addElement(rb.getString("panel.panelCmsViews.viewLanguage"));
		columnNames.addElement(rb.getString("panel.panelCmsViews.default"));
	}

	public ViewTableModel(String column) {
		this();
		StringTokenizer st = new StringTokenizer(column, ";");
		while (st.hasMoreTokens()) {
			columnNames.addElement(st.nextToken());
		}
	}

	public ViewTableModel(ViewDocumentValue[] vec) {
		this();
		addRows(vec);
	}

	public void addRows(ViewDocumentValue[] vec) {
		if (vec != null) {
			for (int i = 0; i < vec.length; i++) {
				ViewDocumentValue dao = (ViewDocumentValue) vec[i];

				Object[] obj = new Object[4];
				obj[0] = dao.getViewType();
				obj[1] = dao.getLanguage();
				if (dao.isIsVdDefault()) {
					obj[2] = "true";
				} else {
					obj[2] = "false";
				}
				obj[3] = new Long(dao.getViewDocumentId());

				addRow(obj);
			}
		}
	}

	public void changeViewId(Long id, int newId) {
		for (int i = 0; i < this.getRowCount(); i++) {
			if (this.getValueAt(i, 3).equals(id)) {
				setValueAt(new Long(newId), i, 3);
			}
		}
	}

	public Long getDefault() {
		for (int i = 0; i < this.getRowCount(); i++) {
			if (this.getValueAt(i, 2).equals("true")) { return (Long) this.getValueAt(i, 3); }
		}
		return null;
	}

	public int getColumnCount() {
		return columnNames.size();
	}

	public int getRowCount() {
		return vRows.size();
	}

	public String getColumnName(int col) {

		if (columnNames.size() >= col) { return (String) columnNames.elementAt(col); }
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
				JOptionPane.showMessageDialog(UIConstants.getMainFrame(), "The column \"" + getColumnName(col) + "\" accepts only numbers.");
			}
		} else if (col == 2) {
			array[2] = value;
			fireTableCellUpdated(row, 2);
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
	 @SuppressWarnings("unchecked")
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
		return false;
	}

	public void addRow(Object[] obj) {
		vRows.addElement(obj);
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
