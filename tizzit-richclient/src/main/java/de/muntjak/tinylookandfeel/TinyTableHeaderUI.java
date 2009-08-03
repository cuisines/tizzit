/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
*	Tiny Look and Feel                                                         *
*                                                                              *
*  (C) Copyright 2003 - 2007 Hans Bickel                                       *
*                                                                              *
*   For licensing information and credits, please refer to the                 *
*   comment in file de.muntjak.tinylookandfeel.TinyLookAndFeel                 *
*                                                                              *
* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package de.muntjak.tinylookandfeel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

import de.muntjak.tinylookandfeel.controlpanel.*;
import de.muntjak.tinylookandfeel.table.SortableTableData;
import de.muntjak.tinylookandfeel.table.TinyTableHeaderRenderer;

/**
 * TinyTableHeaderUI
 * 
 * @version 1.0
 * @author Hans Bickel 
 */
public class TinyTableHeaderUI extends BasicTableHeaderUI {

	/** Client property key */
	public static final String ROLLOVER_COLUMN_KEY = "rolloverColumn";
	
	/** Client property key */
	public static final String SORTED_COLUMN_KEY = "sortedColumn";
	
	/** Client property key */
	public static final String SORTING_DIRECTION_KEY = "sortingDirection";
	
	// Action command constants
	private static final int ADD_COLUMN = 0;
	private static final int REMOVE_COLUMN = 1;
	private static final int REPLACE_COLUMN = 2;

	/** The horizontal distance the mouse must move to
	 * be recognized as a mouse drag. */
	private static final int MINIMUM_DRAG_DISTANCE = 5;
	
	private static final HashMap sortingCache = new HashMap();
	
	protected SortableTableHandler handler;
	protected TinyTableHeaderRenderer headerRenderer;

	public TinyTableHeaderUI() {
		super();
	}

	public static ComponentUI createUI(JComponent header) {
		return new TinyTableHeaderUI();
	}
	
	protected void installListeners() {
		super.installListeners();
		
		// new in 1.3.6
		// install our own renderer so we can realize rollovers
		// and arrow icons (sortable table models only)
		headerRenderer = new TinyTableHeaderRenderer();
		header.setDefaultRenderer(headerRenderer);
		
		// new in 1.3.6
		// support for sortable table models
		handler = new SortableTableHandler();
		
		header.addMouseListener(handler);
		header.addMouseMotionListener(handler);
		header.getColumnModel().addColumnModelListener(handler);
		
		SortingInformation sortingInfo = (SortingInformation)sortingCache.get(header);
		
		if(sortingInfo != null) {
			handler.restoreSortingInformation(header, sortingInfo);
		}
	}
	
	protected void uninstallListeners() {
		super.uninstallListeners();

		// Remove sorting information - even if we only
		// switch TinyLaF themes, we cannot preserve state
		handler.removeSortingInformation();
		
		header.removeMouseListener(handler);
        header.removeMouseMotionListener(handler);
        header.getColumnModel().removeColumnModelListener(handler);
        
//        if(header.getTable() != null) {
//        	TableModel model = header.getTable().getModel();
//        	
//        	if(model != null) {
//        		model.removeTableModelListener(handler);
//        	}
//        }
	}

	public Dimension getPreferredSize(JComponent c) {
		// this is for the very special case that the header value
		// of the 1st column is an empty string and no custom header
		// renderers were defined.
		// In this case, the dimension returned by super.getPreferredSize()
		// has a height of 2 but that's not what we want...
		Dimension d = super.getPreferredSize(c);
		
		d.height = Math.max(16, d.height);
		
		return d;
	}
	
	/**
	 * Call this method to programmatically initiate sorting on (sortable)
	 * table models. Especially if your data is sorted by default, you
	 * should call this method before the table is displayed the first
	 * time.
	 * @param columns array of column indices sorted by priority (highest
	 * priority first)
	 * @param sortingDirections array containing the sorting direction for
	 * each sorted column. Values are either
	 * <ul>
	 * <li><code>de.muntjak.tinylookandfeel.table.SortableTableData.SORT_ASCENDING</code> or
	 * <li><code>de.muntjak.tinylookandfeel.table.SortableTableData.SORT_DESCENDING</code>
	 * </ul>
	 * 
	 * @param table the table displaying the data
	 * @throws IllegalArgumentException If any of the arguments is
	 * <code>null</code> or arguments <code>colums</code> and
	 * <code>sortingDirections</code> are of different length
	 */
	public void sortColumns(int[] columns, int[] sortingDirections, JTable table) {
		if(handler == null) return;
		
		handler.sortColumns(columns, sortingDirections, table);
	}
	
	/**
	 * Sets horizontal alignments of table header renderers where an index
	 * in the argument array corresponds to a column index.
	 * <br>
	 * Note: If the length of the argument array is less than the number
	 * of columns, unspecified columns default to <code>CENTER</code> alignment.
	 * If the length of the argument array is greater than the number
	 * of columns, surplus information will be ignored.
	 * @param alignments array of the following constants
     *           defined in <code>SwingConstants</code>:
     *           <code>LEFT</code>,
     *           <code>CENTER</code>,
     *           <code>RIGHT</code>,
     *           <code>LEADING</code> or
     *           <code>TRAILING</code>.
	 */
	public void setHorizontalAlignments(int[] alignments) {
		if(headerRenderer == null) return;
		
		headerRenderer.setHorizontalAlignments(alignments);
	}
	
	/**
	 * A data container used to store sorting information
	 * between instantiations (LAF change)
	 * @author Hans Bickel
	 *
	 */
	private class SortingInformation {
		private Vector sortedViewColumns;
		private Vector sortedModelColumns;
		private Vector sortingDirections;
		
		SortingInformation(
			Vector sortedViewColumns,
			Vector sortedModelColumns,
			Vector sortingDirections)
		{
			this.sortedViewColumns = sortedViewColumns;
			this.sortedModelColumns = sortedModelColumns;
			this.sortingDirections = sortingDirections;
		}
	}

	/**
	 * This handler is for table headers whose table model
	 * implements SortableTableData. It is attached to the header
	 * in createUI(JComponent) and keeps track of the sorting
	 * direction and the sorted column.
	 * 
	 * @author Hans Bickel
	 * @since 1.3.6
	 *
	 */
	private class SortableTableHandler implements MouseListener,
		MouseMotionListener, TableColumnModelListener
	{
		// -1 means that no column is currently a rollover candidate,
		// else it is the index of the rollover column
		private int rolloverColumn = -1;
		private int pressedColumn = -1;
		private Vector sortedViewColumns = new Vector();
		private Vector sortedModelColumns = new Vector();
		private Vector sortingDirections = new Vector();
		private boolean mouseInside = false;
		private boolean mouseDragged = false;
		private boolean inDrag = false;
		private Point pressedPoint;

		void sortColumns(int[] columns, int[] directions, JTable table) {
			if(columns == null) {
				throw new IllegalArgumentException("columns argument may not be null");
			}
			
			if(directions == null) {
				throw new IllegalArgumentException("directions argument may not be null");
			}
			
			if(columns.length != directions.length) {
				throw new IllegalArgumentException("columns argument and directions argument must be of equal length");
			}
			
			if(columns.length > table.getColumnCount()) {
				throw new IllegalArgumentException("Length of columns argument is greater than number of table columns");
			}
			
			JTableHeader header = table.getTableHeader();
			SortableTableData model = getTableModel(header);
			
			if(model == null) return;
			
			sortedViewColumns.clear();
			sortedModelColumns.clear();
			sortingDirections.clear();
			
			for(int i = 0; i < columns.length; i++) {
				sortedViewColumns.add(new Integer(columns[i]));
				sortedModelColumns.add(new Integer(getModelColumn(header, columns[i])));
				sortingDirections.add(new Integer(directions[i]));
			}

			header.putClientProperty(SORTED_COLUMN_KEY, vectorToIntArray(sortedViewColumns));
			header.putClientProperty(SORTING_DIRECTION_KEY, vectorToIntArray(sortingDirections));
			
			model.sortColumns(
				vectorToIntArray(sortedModelColumns),
				vectorToIntArray(sortingDirections),
				table);
			header.repaint();
		}

// MouseListener implementation
		
		public void mouseEntered(MouseEvent e) {
			mouseInside = true;
			
			if(mouseDragged) return;
			
			SortableTableData model = getTableModel(e.getSource());
			
			if(model == null) return;

			JTableHeader header = (JTableHeader)e.getSource();
			int viewColumn = header.columnAtPoint(e.getPoint());
			int modelColumn = getModelColumnAt(e);
			
			if(!model.isColumnSortable(modelColumn)) {
				if(rolloverColumn != -1) {
					rolloverColumn = -1;
					header.putClientProperty(ROLLOVER_COLUMN_KEY, null);
				}
			}
			else {
				rolloverColumn = viewColumn;
				header.putClientProperty(ROLLOVER_COLUMN_KEY, new Integer(rolloverColumn));
			}
			
			header.repaint();
		}

		public void mouseExited(MouseEvent e) {
			mouseInside = false;
			JTableHeader header = (JTableHeader)e.getSource();
			
			if(inDrag && header.getReorderingAllowed()) return;
			
			SortableTableData model = getTableModel(e.getSource());
			
			if(model == null) return;

			if(rolloverColumn != -1) {
				rolloverColumn = -1;
				header.putClientProperty(ROLLOVER_COLUMN_KEY, null);
				header.repaint();
			}
		}

		public void mouseReleased(MouseEvent e) {
			inDrag = false;
			
			if(e.isPopupTrigger()) {
//				showHeaderPopup(e);
				return;
			}
			
			if(!mouseInside) {
				mouseDragged = false;
				return;
			}
			
			SortableTableData model = getTableModel(e.getSource());
			
			if(model == null) {
				mouseDragged = false;
				return;
			}

			JTableHeader header = (JTableHeader)e.getSource();
			int viewColumn = header.columnAtPoint(e.getPoint());
			
			if(viewColumn == -1) {
				mouseDragged = false;
				return;
			}

			int modelColumn = getModelColumnAt(e);

			if(!model.isColumnSortable(modelColumn)) {
				if(rolloverColumn != -1) {
					rolloverColumn = -1;
					header.putClientProperty(ROLLOVER_COLUMN_KEY, null);
				}
			}
			else {
				rolloverColumn = viewColumn;
				header.putClientProperty(ROLLOVER_COLUMN_KEY, new Integer(rolloverColumn));
			}

			if(mouseDragged) {
				mouseDragged = false;

				return;
			}
			
			if(!model.isColumnSortable(modelColumn)) return;
			if(pressedColumn != viewColumn) return;

			// for header renderer, sorted column must be viewColumn
			Integer vc = new Integer(viewColumn);

			if(sortedViewColumns.contains(vc)) {
				int index = sortedViewColumns.indexOf(vc);
				
				if(e.isAltDown()) {
					// remove clicked column from sorted columns
					sortedViewColumns.remove(index);
					sortedModelColumns.remove(index);
					sortingDirections.remove(index);
				}
				else if((e.isControlDown() && model.supportsMultiColumnSort()) ||
					sortedModelColumns.size() == 1)
				{
					// change sorting direction of clicked column
					int sortingDirection = ((Integer)sortingDirections.get(index)).intValue();
	
					if(sortingDirection != SortableTableData.SORT_DESCENDING) {
						sortingDirection = SortableTableData.SORT_DESCENDING;
					}
					else {
						sortingDirection = SortableTableData.SORT_ASCENDING;
					}
					
					sortingDirections.remove(index);
					sortingDirections.add(index, new Integer(sortingDirection));
				}
				else {
					// change sorting direction of clicked column
					int sortingDirection = ((Integer)sortingDirections.get(index)).intValue();
	
					if(sortingDirection != SortableTableData.SORT_DESCENDING) {
						sortingDirection = SortableTableData.SORT_DESCENDING;
					}
					else {
						sortingDirection = SortableTableData.SORT_ASCENDING;
					}
					
					// remove all sorted columns and initialize
					// sorted columns with clicked column
					sortedViewColumns.clear();
					sortedModelColumns.clear();
					sortingDirections.clear();
					sortedViewColumns.add(vc);
					sortedModelColumns.add(new Integer(getModelColumn(e, viewColumn)));
					sortingDirections.add(new Integer(sortingDirection));
				}
			}
			else {
				if(e.isAltDown()) {
					// ignore
					return;
				}
				else if(e.isControlDown() && model.supportsMultiColumnSort()) {
					// add clicked column to sorted columns
					sortedViewColumns.add(vc);
					sortedModelColumns.add(new Integer(getModelColumn(e, viewColumn)));
					sortingDirections.add(new Integer(SortableTableData.SORT_ASCENDING));
				}
				else {
					// initialize sorted columns with clicked column
					sortedViewColumns.clear();
					sortedModelColumns.clear();
					sortingDirections.clear();
					sortedViewColumns.add(vc);
					sortedModelColumns.add(new Integer(getModelColumn(e, viewColumn)));
					sortingDirections.add(new Integer(SortableTableData.SORT_ASCENDING));
				}
			}
			
			header.putClientProperty(SORTED_COLUMN_KEY, vectorToIntArray(sortedViewColumns));
			header.putClientProperty(SORTING_DIRECTION_KEY, vectorToIntArray(sortingDirections));
			
			model.sortColumns(
				vectorToIntArray(sortedModelColumns),
				vectorToIntArray(sortingDirections),
				header.getTable());
			header.repaint();
		}
		
		private int[] vectorToIntArray(Vector v) {
			int[] ret = new int[v.size()];
			
			for(int i = 0; i < ret.length; i++) {
				ret[i] = ((Integer)v.get(i)).intValue();
			}
			
			return ret;
		}

		public void mousePressed(MouseEvent e) {
			if(e.isPopupTrigger()) {
//				showHeaderPopup(e);
				return;
			}
			
			JTableHeader header = (JTableHeader)e.getSource();
			pressedPoint = e.getPoint();
			pressedColumn = header.columnAtPoint(pressedPoint);
			mouseDragged = false;
		}
		
		public void mouseClicked(MouseEvent e) {}

// MouseMotionListener implementation
		
		public void mouseDragged(MouseEvent e) {
			SortableTableData model = getTableModel(e.getSource());
			
			if(model == null) return;
			
			inDrag = true;
			JTableHeader header = (JTableHeader)e.getSource();

			if(header.getResizingColumn() != null) {
				// It's a resize, not a column move
				if(!mouseDragged) mouseDragged = true;
			}

			if(!header.getReorderingAllowed()) {
				int modelColumn = getModelColumnAt(e);
	
				if(!model.isColumnSortable(modelColumn)) {
					header.putClientProperty(ROLLOVER_COLUMN_KEY, null);
					header.repaint();
					
					return;
				}
			}

			if(!mouseDragged && isMouseDragged(e.getPoint(), pressedPoint)) {
				mouseDragged = true;
			}

			if(!mouseInside) {
				// ! don't set rolloverColumn to -1
				header.putClientProperty(ROLLOVER_COLUMN_KEY, null);
			}
			else {
				if(!header.getReorderingAllowed()) {
					int viewColumn = header.columnAtPoint(e.getPoint());
					
					if(viewColumn != rolloverColumn) {
						rolloverColumn = viewColumn;
					}
				}
				
				if(rolloverColumn != -1) {
					header.putClientProperty(ROLLOVER_COLUMN_KEY, new Integer(rolloverColumn));
				}
			}
			
			header.repaint();
		}

		public void mouseMoved(MouseEvent e) {
			if(!mouseInside) return;

			JTableHeader header = (JTableHeader)e.getSource();
			int viewColumn = header.columnAtPoint(e.getPoint());
			
			if(viewColumn == -1) return;
			
			SortableTableData model = getTableModel(e.getSource());
			
			if(model == null) return;

			int modelColumn = getModelColumnAt(e);
			
			if(!model.isColumnSortable(modelColumn)) {
				if(rolloverColumn != -1) {
					rolloverColumn = -1;
					header.putClientProperty(ROLLOVER_COLUMN_KEY, null);
					header.repaint();
				}
				
				return;
			}

			// rolloverColumn must be viewColumn, not modelColumn
			if(viewColumn != rolloverColumn) {
				rolloverColumn = viewColumn;
				header.putClientProperty(ROLLOVER_COLUMN_KEY, new Integer(rolloverColumn));
				header.repaint();
			}
		}

// TableColumnModelListener implementation

		public void columnAdded(TableColumnModelEvent e) {
//			System.out.println("columnAdded at " + e.getToIndex());
			removeSorting();
		}

		public void columnMoved(TableColumnModelEvent e) {
			if(e.getFromIndex() == e.getToIndex()) return;
			if(header == null) return;

			// update rolloverColumn
			if(rolloverColumn == e.getFromIndex()) {
				rolloverColumn = e.getToIndex();
				
				if(mouseInside) {
					header.putClientProperty(ROLLOVER_COLUMN_KEY, new Integer(rolloverColumn));
				}
			}
			
			// update sorted column(s)
			int[] sc = vectorToIntArray(sortedViewColumns);
			boolean changed = false;

			// Note: With multiple sorted columns it might easily
			// be that both the column at FromIndex and the column
			// at ToIndex are sortable
			for(int i = 0; i < sc.length; i++) {
				if(sc[i] == e.getFromIndex()) {
					sc[i] = e.getToIndex();
					changed = true;
				}
				else if(sc[i] == e.getToIndex()) {
					sc[i] = e.getFromIndex();
					changed = true;
				}
			}
			
			if(changed) {
				sortedViewColumns.clear();
				
				for(int i = 0; i < sc.length; i++) {
					sortedViewColumns.add(new Integer(sc[i]));
				}

				header.putClientProperty(SORTED_COLUMN_KEY, vectorToIntArray(sortedViewColumns));
			}
		}

		public void columnRemoved(TableColumnModelEvent e) {
//			System.out.println("columnRemoved at " + e.getFromIndex());
			removeSorting();
		}
		
		public void columnMarginChanged(ChangeEvent e) {}
		public void columnSelectionChanged(ListSelectionEvent e) {}

// Helper methods
		
		private void removeSorting() {
			if(header == null) return;
			
			// remove rolloverColumn
			if(rolloverColumn != -1) {
				rolloverColumn = -1;
				header.putClientProperty(ROLLOVER_COLUMN_KEY, new Integer(rolloverColumn));
			}
			
			sortedModelColumns.clear();
			sortedViewColumns.clear();
			sortingDirections.clear();
			header.putClientProperty(SORTING_DIRECTION_KEY, null);
			header.putClientProperty(SORTED_COLUMN_KEY, null);
			header.repaint();
		}
		
		void removeSortingInformation() {
			if(header == null) return;
			
			SortableTableData model = getTableModel(header);
			
			if(model == null) return;
			
			// cache sorting information
			sortingCache.put(header,
				new SortingInformation(
					sortedViewColumns,
					sortedModelColumns,
					sortingDirections));
			
			// We don't have to call removeSorting()
			model.sortColumns(
				new int[] {},
				new int[] {},
				header.getTable());
			header.repaint();
		}
		
		void restoreSortingInformation(JTableHeader header, SortingInformation sortingInfo) {
			if(header == null) return;
			
			SortableTableData model = getTableModel(header);

			if(model == null) return;

			sortedViewColumns = sortingInfo.sortedViewColumns;
			sortedModelColumns = sortingInfo.sortedModelColumns;
			sortingDirections = sortingInfo.sortingDirections;
			
			model.sortColumns(
				vectorToIntArray(sortedModelColumns),
				vectorToIntArray(sortingDirections),
				header.getTable());
			header.repaint();
		}

		/**
		 * 
		 * @param source the table header
		 * @return the TableModel to work on or <code>null</code> if
		 * either table is <code>null</code>, table model is <code>null</code>
		 * or table model doesn't implement <code>SortableTableData</code>
		 */
		private SortableTableData getTableModel(Object source) {
			return getTableModel((JTableHeader)source);
		}
		
		private SortableTableData getTableModel(JTableHeader header) {
			JTable table = header.getTable();
			
			if(table == null) return null;

			TableModel model = table.getModel();

			if(!(model instanceof SortableTableData)) return null;

//			if(!tableModelListenerInstalled) {
//				tableModelListenerInstalled = true;
//				model.addTableModelListener(handler);
//			}

			return (SortableTableData)model;
		}
		
		/**
		 * 
		 * @param e
		 * @return the logical column index
		 */
		private int getModelColumnAt(MouseEvent e) {
			JTableHeader header = (JTableHeader)e.getSource();
			int viewColumn = header.columnAtPoint(e.getPoint());
			
			if(viewColumn == -1) return -1;

			return header.getColumnModel().getColumn(viewColumn).getModelIndex();
		}
		
		/**
		 * 
		 * @param e
		 * @param viewColumn
		 * @return the model column index corresponding to the
		 * view column index
		 */
		private int getModelColumn(MouseEvent e, int viewColumn) {
			if(viewColumn == -1) return -1;
			
			JTableHeader header = (JTableHeader)e.getSource();

			return getModelColumn(header, viewColumn);
		}
		
		private int getModelColumn(JTableHeader header, int viewColumn) {
			return header.getColumnModel().getColumn(viewColumn).getModelIndex();
		}
		
		private boolean isMouseDragged(Point p1, Point p2) {
			if(Math.abs(p1.x - p2.x) >= MINIMUM_DRAG_DISTANCE) return true;
			
			return false;
		}
		
		private void showHeaderPopup(MouseEvent e) {
			final SortableTableData model = getTableModel(e.getSource());
			
			if(model == null) return;
			
			JTableHeader header = (JTableHeader)e.getSource();
			final int viewColumn = header.columnAtPoint(e.getPoint());
			
			JPopupMenu menu = new JPopupMenu();
			JMenu sub = new JMenu("Add");
			JMenuItem item = new JMenuItem("Double Column");
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					((TinyTableModel)model).addColumn(Double.class, viewColumn);
				}
			});
			sub.add(item);
			
			item = new JMenuItem("Icon Column");
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					((TinyTableModel)model).addColumn(Icon.class, viewColumn);
				}
			});
			sub.add(item);
			
			item = new JMenuItem("Integer Column");
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					((TinyTableModel)model).addColumn(Integer.class, viewColumn);
				}
			});
			sub.add(item);
			
			item = new JMenuItem("String Column");
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					((TinyTableModel)model).addColumn(String.class, viewColumn);
				}
			});
			sub.add(item);
			
			menu.add(sub);
			menu.addSeparator();
			
			item = new JMenuItem("Remove Column");
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					((TinyTableModel)model).removeColumn(viewColumn);
				}
			});
			if(((TinyTableModel)model).getColumnCount() < 2) {
				item.setEnabled(false);
			}
			menu.add(item);
			
			menu.addSeparator();
			
			item = new JMenuItem("Remove all Rows");
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					((TinyTableModel)model).removeAllRows();
				}
			});
			if(((TinyTableModel)model).getRowCount() == 0) {
				item.setEnabled(false);
			}
			menu.add(item);
			
			menu.addSeparator();
			
			item = new JMenuItem("Create new Data");
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					((TinyTableModel)model).createNewData();
				}
			});
			if(((TinyTableModel)model).getRowCount() > 0) {
				item.setEnabled(false);
			}
			menu.add(item);
			
			menu.show(header, e.getX(), 0);
		}
	}
}
