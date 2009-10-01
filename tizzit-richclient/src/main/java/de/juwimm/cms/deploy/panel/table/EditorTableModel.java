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

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.vo.ViewComponentValue;

/**
 * <p>Title: ConQuest</p>
 * <p>Description: Enterprise Content Management<</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class EditorTableModel extends AbstractTableModel {
	private Vector<String> columnNames = new Vector<String>(5);
	private Vector<Object> vRows = new Vector<Object>();

	public EditorTableModel() {
		super();
		columnNames.addElement(rb.getString("wizard.author.treeheader.action"));
		columnNames.addElement(rb.getString("panel.task.treeheader.type"));
		columnNames.addElement(rb.getString("wizard.author.treeheader.name"));
		columnNames.addElement(rb.getString("panel.task.treeheader.reject"));
		columnNames.addElement(rb.getString("panel.task.treeheader.approve"));
	}

	public EditorTableModel(String column) {
		this();
		StringTokenizer st = new StringTokenizer(column, ";");
		while (st.hasMoreTokens()) {
			columnNames.addElement(st.nextToken());
		}
	}

	public EditorTableModel(ViewComponentValue[] vec) {
		this();
		addRows(vec);
	}

	public void setNewData(ViewComponentValue[] vec){	
		this.vRows.removeAllElements();
		addRows(vec);
		fireTableDataChanged();
	}
		
	
	public void addRows(ViewComponentValue[] vec) {
		String s = "";
		for (int i = 0; i < vec.length; i++) {
			ViewComponentValue dao = vec[i];
			Object[] obj = new Object[7];

			if (dao.getDeployCommand() == Constants.DEPLOY_COMMAND_ADD
					&& dao.getOnline() == Constants.ONLINE_STATUS_ONLINE) {
				// for buggy Deploy Commands. In this case the Deploy-Command should be MODIFY
				obj[0] = new Integer(Constants.DEPLOY_COMMAND_MODIFY);
			} else {
				obj[0] = new Integer(dao.getDeployCommand());
			}

			obj[1] = new Integer(dao.getViewType());
			obj[2] = s;
			if (dao.getDisplayLinkName() != null) {
				obj[2] = dao.getDisplayLinkName();
			}
			obj[3] = new Boolean(false);
			obj[4] = new Boolean(false);
			obj[5] = dao.getUrlLinkName();
			obj[6] = dao;
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

		if (col == 3 && ((Boolean) value).booleanValue() && ((Boolean) array[4]).booleanValue()) {
			array[4] = new Boolean(false);
			fireTableCellUpdated(row, 4);
		} else if (col == 4 && ((Boolean) value).booleanValue() && ((Boolean) array[3]).booleanValue()) {
			array[3] = new Boolean(false);
			fireTableCellUpdated(row, 3);
		}
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

	/*
	 * Don't need to implement this method unless your table's
	 * editable.
	 */
	public boolean isCellEditable(int row, int col) {
		//Note that the data/cell address is constant,
		//no matter where the cell appears onscreen.
		return (!(col == 0 || col == 1));
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