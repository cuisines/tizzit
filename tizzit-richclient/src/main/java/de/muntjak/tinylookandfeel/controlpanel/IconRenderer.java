package de.muntjak.tinylookandfeel.controlpanel;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;


/**
 * Renders the icon column.
 */
public class IconRenderer extends DefaultTableCellRenderer {
	
	public IconRenderer() {
		setHorizontalAlignment(SwingConstants.CENTER);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
		boolean isSelected, boolean hasFocus, int row, int column)
	{
		// needs no foreground
		if (isSelected) {
		   super.setBackground(table.getSelectionBackground());
		}
		else {
		    super.setBackground(table.getBackground());
		}
		
		if(value == null || !(value instanceof Icon)) {
			setIcon(null);
		}
		else {
			setIcon((Icon)value);
		}

        return this;
    }
}