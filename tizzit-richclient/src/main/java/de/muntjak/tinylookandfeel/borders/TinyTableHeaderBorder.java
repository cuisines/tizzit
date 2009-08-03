/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
*	Tiny Look and Feel                                                         *
*                                                                              *
*  (C) Copyright 2003 - 2007 Hans Bickel                                       *
*                                                                              *
*   For licensing information and credits, please refer to the                 *
*   comment in file de.muntjak.tinylookandfeel.TinyLookAndFeel                 *
*                                                                              *
* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package de.muntjak.tinylookandfeel.borders;

import java.awt.*;
import java.lang.reflect.Method;

import javax.swing.CellRendererPane;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import de.muntjak.tinylookandfeel.*;
import de.muntjak.tinylookandfeel.controlpanel.*;

/**
 * TinyTableHeaderBorder is the border displayed for table headers
 * of non-sortable table models and of sortable table models if
 * the column in question is not the rollover column.
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class TinyTableHeaderBorder extends AbstractBorder implements UIResource {
	
	protected static final Insets insets98 = new Insets(1, 1, 1, 1);
	protected static final Insets insetsXP = new Insets(3, 0, 3, 2);
	protected Color color1, color2, color3, color4, color5;

	public Insets getBorderInsets(Component c) {
		if(Theme.derivedStyle[Theme.style] == Theme.YQ_STYLE) {
			return insetsXP;
		}
		
		return insets98;
	}
	
	public Insets getBorderInsets(Component c, Insets insets) {
		if(Theme.derivedStyle[Theme.style] == Theme.YQ_STYLE) {
			insets.left = insetsXP.left;
	        insets.top = insetsXP.top;
	        insets.right = insetsXP.right;
	        insets.bottom = insetsXP.bottom;
		}
		else {
	        insets.left = insets98.left;
	        insets.top = insets98.top;
	        insets.right = insets98.right;
	        insets.bottom = insets98.bottom;
		}
        
        return insets;
    }

	public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
		if(Theme.derivedStyle[Theme.style] == Theme.YQ_STYLE) {
			if(color1 == null) {
				color1 = ColorRoutines.darken(c.getBackground(), 5);
			    color2 = ColorRoutines.darken(c.getBackground(), 10);
				color3 = ColorRoutines.darken(c.getBackground(), 15);
			    color4 = Theme.tableHeaderDarkColor[Theme.style].getColor();
			    color5 = Theme.tableHeaderLightColor[Theme.style].getColor();
			}
			
			// paint 3 bottom lines
			g.setColor(color1);
	        g.drawLine(x, y + h - 3, x + w - 1, y + h - 3);
	        
	        g.setColor(color2);
	        g.drawLine(x, y + h - 2, x + w - 1, y + h - 2);
	        
	        g.setColor(color3);
	        g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
	        
	        // paint separator
	        g.setColor(color4);
	        g.drawLine(x + w - 2, y + 3, x + w - 2, y + h - 5);
	        
	        g.setColor(color5);
	        g.drawLine(x + w - 1, y + 3, x + w - 1, y + h - 5);
		}
		else {	// 98 style
			if(color4 == null) {
			    color4 = Theme.tableHeaderLightColor[Theme.style].getColor();
			    color5 = Theme.tableHeaderDarkColor[Theme.style].getColor();
			}
			
			g.setColor(color4);
			g.drawLine(x, y, x, y + h - 1);		// left
			g.drawLine(x, y, x + w - 1, y);		// top
			
			g.setColor(color5);
			g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);		// right
			g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);		// bottom
		}
	}
}
