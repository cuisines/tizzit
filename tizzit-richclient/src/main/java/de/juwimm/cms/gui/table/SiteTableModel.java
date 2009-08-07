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

import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.vo.SiteValue;
import de.juwimm.cms.vo.ViewDocumentValue;

/**
 * <p>Title: ConQuest</p>
 * <p>Description: Enterprise Content Management</p>
 * <p>Copyright: Copyright (c) 2002, 2003</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class SiteTableModel extends DefaultTableModel {
	private ResourceBundle rb = Constants.rb;
	private Vector<String> columnNames = new Vector<String>(2);

	//Vector dataVector = new Vector();

	public SiteTableModel() {
		super();
		columnNames.addElement(rb.getString("panel.sitesAdministration.lblSiteName"));
		columnNames.addElement(rb.getString("panel.sitesAdministration.lblSiteShort"));
	}

	public void addRow(SiteValue vo,ViewDocumentValue defaultViewDocumentValue) {
		// Vector is natively used by DefaultTableModel
		Vector vec = new Vector();
		vec.add(vo.getName());
		vec.add(vo.getShortName());
		vec.add(vo);
		vec.add(defaultViewDocumentValue);
		addRow(vec);
	}

	public void addRow(Vector obj) {
		dataVector.addElement(obj);
		fireTableRowsInserted(getRowCount(), getRowCount());
	}

	public void addRows(SiteValue[] vos) {
		for (int i = 0; i < vos.length; i++) {
			addRow(vos[i],null);
		}
	}

	public int getColumnCount() {
		return columnNames.size();
	}

	public int getRowCount() {
		return dataVector.size();
	}

	public int getRowForSite(int siteId) {
		int retVal = -1;
		for (int i = 0; i < dataVector.size(); i++) {
			Vector vec = (Vector) dataVector.elementAt(i);
			if (((SiteValue) vec.get(2)).getSiteId() == siteId) {
				retVal = i;
				break;
			}
		}
		return retVal;
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
}
