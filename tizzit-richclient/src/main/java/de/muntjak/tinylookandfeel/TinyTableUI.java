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


import java.awt.Graphics;
import java.util.Enumeration;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.TableColumn;

/**
 * TinyTableUI
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class TinyTableUI extends BasicTableUI {
	
	public TinyTableUI() {
		super();
		
	}
	
	public TinyTableUI(JComponent table) {
		super();
		this.table = (JTable)table;				
				
	}
	
	public static ComponentUI createUI(JComponent table) {		
			
		return new TinyTableUI(table);
	}
	
	protected void installDefaults() {
		super.installDefaults();
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {
		Enumeration<TableColumn> columns = table.getColumnModel().getColumns();
		if(columns != null){
			TinyTableCellRenderer cellRenderer =  new TinyTableCellRenderer();
			//while(columns.hasMoreElements()){
				//columns.nextElement().setCellRenderer(cellRenderer);
			//}
		}
		super.paint(g, c);
	}
	
}
