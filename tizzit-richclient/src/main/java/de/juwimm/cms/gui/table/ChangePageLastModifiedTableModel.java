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

import java.io.Serializable;
import java.util.*;

import javax.swing.table.AbstractTableModel;

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.vo.ViewComponentValue;

/**
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class ChangePageLastModifiedTableModel extends AbstractTableModel implements Serializable {
	private Vector<String> columnNames = new Vector<String>(5);
	protected Vector dataVector = new Vector();

	public ChangePageLastModifiedTableModel(Collection<ViewComponentValue> data) {
		super();
		columnNames.addElement(rb.getString("panel.panelView.lblLinkname"));
		columnNames.addElement(rb.getString("panel.panelView.lblStateOnline"));
		columnNames.addElement(rb.getString("ChangePageLastModifiedTableModel.existingModifiedDate"));
		columnNames.addElement(rb.getString("ChangePageLastModifiedTableModel.newModifiedDate"));
		columnNames.addElement(rb.getString("ChangePageLastModifiedTableModel.changeModifiedDate"));
		this.addRows(data);
	}

	private void addRows(Collection<ViewComponentValue> data) {
		Iterator it = data.iterator();
		while (it.hasNext()) {
			ViewComponentValue value = (ViewComponentValue) it.next();
			Object[] row = new Object[6];
			row[0] = value.getDisplayLinkName();
			row[1] = this.getOnlineStateText(value);
			row[2] = new Date(value.getUserLastModifiedDate());
			row[3] = new Date(value.getLastModifiedDate());
			row[4] = new Boolean(true);
			row[5] = value;
			this.addRow(row);
		}
	}
	
	private String getOnlineStateText(ViewComponentValue viewComponent) {
		switch (viewComponent.getOnline()) {
			case Constants.ONLINE_STATUS_OFFLINE:
				return rb.getString("actions.ONLINE_STATUS_OFFLINE");
			case Constants.ONLINE_STATUS_ONLINE:
				return rb.getString("actions.ONLINE_STATUS_ONLINE");
			default:
				return rb.getString("actions.ONLINE_STATUS_UNDEF");
		}
	}

	private void addRow(Object[] row) {
		dataVector.addElement(row);
		fireTableRowsInserted(this.getRowCount(), this.getRowCount());
	}

	public int getRowCount() {
		return this.dataVector.size();
	}

	public int getColumnCount() {
		return this.columnNames.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return ((Object[]) this.dataVector.elementAt(rowIndex))[columnIndex];
	}

	public Collection<ViewComponentValue> getSelectedPages() {
		Collection<ViewComponentValue> result = new ArrayList<ViewComponentValue>();
		Iterator it = this.dataVector.iterator();
		while (it.hasNext()) {
			Object[] value = (Object[]) it.next();
			if (((Boolean) value[4]).booleanValue()) {
				Date date = (Date) value[3];
				ViewComponentValue vcValue = (ViewComponentValue) value[5];
				vcValue.setUserLastModifiedDate(date.getTime());
				result.add(vcValue);
			}
		}

		return result;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch(columnIndex) {
			case 0: return String.class;
			case 1: return String.class;
			case 2: return Date.class;
			case 3: return Date.class;
			case 4: return Boolean.class;
			default: return Object.class;
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		return this.columnNames.elementAt(column);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// only the checkbox and the userDate should be editable
		return ((columnIndex == 4) || (columnIndex == 3));
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
	 */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Object[] row = (Object[]) dataVector.elementAt(rowIndex);
		row[columnIndex] = aValue;
		super.fireTableCellUpdated(rowIndex, columnIndex);
	}

}
