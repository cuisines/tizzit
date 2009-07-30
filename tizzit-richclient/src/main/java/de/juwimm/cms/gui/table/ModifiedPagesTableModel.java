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

import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.table.DefaultTableModel;

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.vo.ContentValue;

/**
 * TableModel for list of all modified pages in this sesssion
 * 
 * @author <a href="carsten.schalm@juwimm.com">Carsten Schalm</a>
 * @version $Id$
 */
public class ModifiedPagesTableModel extends DefaultTableModel {
	private ResourceBundle rb = Constants.rb;
	private Vector<String> columnNames = new Vector<String>(4);
	private SimpleDateFormat sdf = new SimpleDateFormat(rb.getString("General.ShortDateTimeFormat"));

	public ModifiedPagesTableModel() {
		super();
		columnNames.addElement(rb.getString("ModifiedPagesTableModel.checkin"));
		columnNames.addElement(rb.getString("wizard.author.treeheader.name"));
		columnNames.addElement(rb.getString("panel.panelView.content.template"));
		columnNames.addElement(rb.getString("PanDocument.document.changedAt"));
	}

	public void addRow(ContentValue vo) {
		Object[] obj = new Object[5];
		obj[0] = new Boolean(true);
		obj[1] = vo.getHeading() == null ? "" : vo.getHeading();
		obj[2] = (String) ((HashMap) Constants.CMS_AVAILABLE_DCF.get(vo.getTemplate())).get("description");
		obj[3] = this.sdf.format(new Date(vo.getCreateDate()));
		obj[4] = vo;
		addRow(obj);
	}

	public void addRow(Vector obj) {
		dataVector.addElement(obj);
		fireTableRowsInserted(getRowCount(), getRowCount());
	}

	public void addRows(ContentValue[] vos) {
		for (int i = 0; i < vos.length; i++) {
			addRow(vos[i]);
		}
	}

	public void addRows(ArrayList<ContentValue> pageList) {
		Iterator it = pageList.iterator();
		while (it.hasNext()) {
			addRow((ContentValue) it.next());
		}
	}

	public int getColumnCount() {
		return columnNames.size();
	}

	public int getRowCount() {
		return dataVector.size();
	}

	public int getRowForContent(Integer contentId) {
		int retVal = -1;
		for (int i = 0; i < dataVector.size(); i++) {
			Object[] obj = (Object[]) dataVector.elementAt(i);
			if (((ContentValue) obj[3]).getContentId() == (contentId.intValue())) {
				retVal = i;
				break;
			}
		}
		return retVal;
	}

	public Integer[] getSelectedPages() {
		ArrayList<Integer> al = new ArrayList<Integer>(dataVector.size());
		for (int i = 0; i < dataVector.size(); i++) {
			if (((Boolean) ((Object[]) dataVector.elementAt(i))[0]).booleanValue()) {
				al.add(((ContentValue) ((Object[]) dataVector.elementAt(i))[4]).getContentId());
			}
		}
		return (Integer[]) al.toArray(new Integer[0]);
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
			return ((Object[]) dataVector.elementAt(row))[col];
		} catch (Exception ex) {
			return null;
		}
	}

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
		return (col == 0);
	}

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

	public void removeRow(int i) {
		dataVector.removeElementAt(i);
		this.fireTableRowsDeleted(i, i);
	}

	public Object[] getRow(int i) {
		return (Object[]) dataVector.elementAt(i);
	}

}
