package de.muntjak.tinylookandfeel.controlpanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import de.muntjak.tinylookandfeel.controlpanel.NonSortableTableModel.Record;
import de.muntjak.tinylookandfeel.table.SortableTableData;


/**
 * An example table model implementing SortableTableData.
 * See de.muntjak.tinylookandfeel.controlpanel.NonSortableTableModel
 * for details about the data structure.
 * 
 * @author Hans Bickel
 *
 */
public class TinyTableModel extends NonSortableTableModel implements SortableTableData {

	public TinyTableModel() {
		super(true);
	}
	
// SortableTableData implementation
	
	public boolean isColumnSortable(int column) {
		// Note: For TinyTableModel this works fine. You may
		// take another approach depending on your kind of data.
		if(data.isEmpty()) return false;
		
		// value at column #1 is of type Icon (which doesn't implement Comparable)
		return (getValueAt(0, column) instanceof Comparable);
	}
	
	public boolean supportsMultiColumnSort() {
		// We support multi column sort
		return true;
	}

	public void sortColumns(final int[] columns, final int[] sortingDirections, JTable table) {
		// Prerequisites for restoring the selection
		int[] sr = table.getSelectedRows();
		int[] sc = table.getSelectedColumns();
		int rowIndex = 0;
		
		Iterator ii = data.iterator();
		while(ii.hasNext()) {
			((Record)ii.next()).setOldRow(rowIndex ++);
		}

		// The sorting part...
		if(columns.length == 0) {
			// The natural order of our data depends on first (Integer) column
			Collections.sort(data, new Comparator() {
				public int compare(Object o1, Object o2) {
					// For our data we know that arguments are
					// non-null and are of type Record.
					Record r1 = (Record)o1;
					Record r2 = (Record)o2;
					Comparable val1 = (Comparable)r1.getValueAt(0);
					Comparable val2 = (Comparable)r2.getValueAt(0);
					
					return val1.compareTo(val2);
				}
			});
		}
		else {
			// Multi column sort
			Collections.sort(data, new Comparator() {
				public int compare(Object o1, Object o2) {
					// For our data we know that arguments are
					// non-null and are of type Record.
					Record r1 = (Record)o1;
					Record r2 = (Record)o2;
					
					for(int i = 0; i < columns.length; i++) {
						Comparable val1 = (Comparable)r1.getValueAt(columns[i]);
						Comparable val2 = (Comparable)r2.getValueAt(columns[i]);
						int result = val1.compareTo(val2);
						
						if(result != 0) {
							if(sortingDirections[i] == SORT_DESCENDING) {
								return -result;
							}
							
							return result;
						}
					}
					
					return 0;
				}
			});
		}

		// Tell our listeners that data has changed
		fireTableDataChanged();
		
		// Restore selection
		rowIndex = 0;
		
		ii = data.iterator();
		while(ii.hasNext()) {
			((Record)ii.next()).setNewRow(rowIndex ++);
		}
		
		Vector temp = (Vector)data.clone();
		Collections.sort(temp, new Comparator() {
			public int compare(Object o1, Object o2) {
				Record r1 = (Record)o1;
				Record r2 = (Record)o2;
				
				if(r1.getOldRow() > r2.getOldRow()) return 1;
				
				return -1;
			}
		});

		// Adding one row selection interval after another is
		// probably inefficient.
		for(int i = 0; i < sr.length; i++) {
			int row = ((Record)temp.get(sr[i])).getNewRow();
			table.addRowSelectionInterval(row, row);
		}

		for(int i = 0; i < sc.length; i++) {
			table.addColumnSelectionInterval(sc[i], sc[i]);
		}
	}
}
