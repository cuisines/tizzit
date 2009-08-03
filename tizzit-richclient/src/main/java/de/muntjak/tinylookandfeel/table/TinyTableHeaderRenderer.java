package de.muntjak.tinylookandfeel.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import de.muntjak.tinylookandfeel.Theme;
import de.muntjak.tinylookandfeel.TinyTableHeaderUI;
import de.muntjak.tinylookandfeel.controlpanel.ColorRoutines;

/**
 * This TableCellRenderer is installed on the table header
 * in TinyTableHeaderUI.installDefaults() so we can change the
 * border on rollover and show an arrow icon for sorted columns.
 * @author Hans Bickel
 *
 */
public class TinyTableHeaderRenderer extends DefaultTableCellRenderer implements UIResource {

	private static final Icon arrowNo = new Arrow(true, -1);
	
	// arrows array will be lazily filled
	private static final Icon[][] arrows = new Icon[2][4];
	
	private int[] horizontalAlignments;
	
	public TinyTableHeaderRenderer() {
		setHorizontalAlignment(CENTER);
		setHorizontalTextPosition(LEFT);
	}
	
	/**
	 * Sets horizontal alignments of renderers where an index
	 * in the argument array corresponds to a column index.
	 * @param alignments array of the following constants
     *           defined in <code>SwingConstants</code>:
     *           <code>LEFT</code>,
     *           <code>CENTER</code>,
     *           <code>RIGHT</code>,
     *           <code>LEADING</code> (the default for text-only labels) or
     *           <code>TRAILING</code>.
	 */
	public void setHorizontalAlignments(int[] alignments) {
		horizontalAlignments = alignments;
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column)
	{
		LookAndFeel laf = UIManager.getLookAndFeel();

		if(laf == null || !"TinyLookAndFeel".equals(laf.getName())) {
			if(table != null) {
				JTableHeader header = table.getTableHeader();
				
				if(header != null) {
					setBackground(header.getBackground());
				    setForeground(header.getForeground());
				    setFont(header.getFont());
				}
			}
			
			setIcon(null);
			setText((value == null) ? "" : value.toString());
			setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			
			return this;
		}
		
		boolean isRolloverColumn = false;
		Icon icon = arrowNo;
		int sortingDirection = SortableTableData.SORT_ASCENDING;
		int priority = -1;
		boolean paintArrow = false;

		if(table != null) {
			JTableHeader header = table.getTableHeader();
			
			if(header != null) {
				Object o = header.getClientProperty(TinyTableHeaderUI.ROLLOVER_COLUMN_KEY);
				
				if(o != null) {
					int rolloverColumn = ((Integer)o).intValue();
					
					if(rolloverColumn == column) {
						isRolloverColumn = true;
					}
				}
				
				o = header.getClientProperty(TinyTableHeaderUI.SORTED_COLUMN_KEY);
				
				if(o != null) {
					int sc[] = (int[])o;
					priority = -1;
					
					for(int i = 0; i < sc.length; i++) {
						if(sc[i] == column) {
							priority = i;
						}
					}
					
					if(priority > -1) {
						paintArrow = true;
						o = header.getClientProperty(TinyTableHeaderUI.SORTING_DIRECTION_KEY);
						
						if(o != null) {
							int[] sd = (int[])o;
							sortingDirection = sd[priority];
						}
					}
				}

			    if(isRolloverColumn) {
			    	setBackground(Theme.tableHeaderRolloverBackColor[Theme.style].getColor());
			    }
			    else {
			    	setBackground(header.getBackground());
			    }
			    
			    setForeground(header.getForeground());
			    setFont(header.getFont());
			}
			
			TableModel model = table.getModel();
			
			if(model instanceof SortableTableData) {
				if(paintArrow) {
					int pri = Math.min(3, priority);
					
					if(sortingDirection == SortableTableData.SORT_ASCENDING) {
						if(arrows[0][pri] == null) {
							arrows[0][pri] = new Arrow(false, priority);
						}
						
						icon = arrows[0][pri];
					}
					else {
						if(arrows[1][pri] == null) {
							arrows[1][pri] = new Arrow(true, priority);
						}
						
						icon = arrows[1][pri];
					}
				}
				else {
					int modelColumn = table.getColumnModel().getColumn(column).getModelIndex();
					
					if(!((SortableTableData)model).isColumnSortable(modelColumn)) {
						icon = null;
					}
				}
			}
			else {
				icon = null;
				setToolTipText(null);
			}
		}

		setIcon(icon);
		setText((value == null) ? "" : value.toString());

		if(isRolloverColumn) {
			setBorder(UIManager.getBorder("TableHeader.cellRolloverBorder"));
	    }
	    else {
	    	setBorder(UIManager.getBorder("TableHeader.cellBorder"));
	    }
		
		int modelColumn = table.getColumnModel().getColumn(column).getModelIndex();
		
		if(horizontalAlignments != null && horizontalAlignments.length > modelColumn) {
			setHorizontalAlignment(horizontalAlignments[modelColumn]);
		}
		
		return this;
	}

	private static class Arrow implements Icon {

		private static final int height = 11;
		private boolean descending;
		private int priority;

		public Arrow(boolean descending, int priority) {
			this.descending = descending;
			this.priority = Math.min(3, priority);
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			if(priority == -1) return;	// empty icon
			
			// In a compound sort, make each successive triangle
			// smaller than the previous one
			int dx = priority;
			int dy = (height - 5 + priority) / 2;

			g.translate(x + dx, y + dy);
			g.setColor(Theme.tableHeaderArrowColor[Theme.style].getColor());
			
			if(descending) {
				switch(priority) {
					case 0:
						g.drawLine(4, 4, 4, 4);
						g.drawLine(3, 3, 5, 3);
						g.drawLine(2, 2, 6, 2);
						g.drawLine(1, 1, 7, 1);
						g.drawLine(0, 0, 8, 0);
						break;
					case 1:
						g.drawLine(3, 3, 3, 3);
						g.drawLine(2, 2, 4, 2);
						g.drawLine(1, 1, 5, 1);
						g.drawLine(0, 0, 6, 0);
						break;
					case 2:
						g.drawLine(2, 2, 2, 2);
						g.drawLine(1, 1, 3, 1);
						g.drawLine(0, 0, 4, 0);
						break;
					case 3:
					default:
						g.drawLine(1, 1, 1, 1);
						g.drawLine(0, 0, 2, 0);
						break;
				}
			}
			else {
				switch(priority) {
					case 0:
						g.drawLine(4, 0, 4, 0);
						g.drawLine(3, 1, 5, 1);
						g.drawLine(2, 2, 6, 2);
						g.drawLine(1, 3, 7, 3);
						g.drawLine(0, 4, 8, 4);
						break;
					case 1:
						g.drawLine(3, 0, 3, 0);
						g.drawLine(2, 1, 4, 1);
						g.drawLine(1, 2, 5, 2);
						g.drawLine(0, 3, 6, 3);
						break;
					case 2:
						g.drawLine(2, 0, 2, 0);
						g.drawLine(1, 1, 3, 1);
						g.drawLine(0, 2, 4, 2);
						break;
					case 3:
					default:
						g.drawLine(1, 0, 1, 0);
						g.drawLine(0, 1, 2, 1);
						break;
				}
			}
			
			g.translate(-(x + dx), -(y + dy));
		}

		public int getIconWidth() {
			return 9;
		}

		public int getIconHeight() {
			return height;
		}
	}
}
