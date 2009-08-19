package de.muntjak.tinylookandfeel;

import java.awt.Component;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class TinyTableCellRenderer extends DefaultTableCellRenderer{

	private static final long serialVersionUID = -813337799034191985L;

	public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected, boolean hasFocus,int row, int column) {
	    JLabel cellLabel = (JLabel)  super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);	 
	    cellLabel.setBorder(new CompoundBorder(new EmptyBorder(new Insets(1,4,1,4)),cellLabel.getBorder()));
		return cellLabel;
	}
}
