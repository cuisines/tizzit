/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
*	Tiny Look and Feel                                                         *
*                                                                              *
*  (C) Copyright 2003 - 2007 Hans Bickel                                       *
*                                                                              *
*   For licensing information and credits, please refer to the                 *
*   comment in file de.muntjak.tinylookandfeel.TinyLookAndFeel                 *
*                                                                              *
* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package de.muntjak.tinylookandfeel.controlpanel;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * ToolButtonIcon
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class ColorIcon implements Icon {

	private Dimension iconSize;
	private Color color;
	private static int hue = 0;
	
	public ColorIcon(Dimension iconSize) {
		this.iconSize = iconSize;
		color = Color.getHSBColor((float)(hue / 360.0), 0.5f, 0.9f);
		hue += 360 / 15;
	}
	
	public int getIconHeight() {
		return iconSize.height;
	}

	public int getIconWidth() {
		return iconSize.width;
	}
		
	public void paintIcon(Component comp, Graphics g, int x, int y) {
		g.setColor(color);
		g.fillRect(x + 1, y + 1, getIconWidth() - 2, getIconHeight() - 2);
		
		g.setColor(Color.BLACK);
		g.drawRect(x, y, getIconWidth() - 1, getIconHeight() - 1);
	}
}
