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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalSeparatorUI;

/**
 * TinyPopupMenuSeparatorUI
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class TinyPopupMenuSeparatorUI extends MetalSeparatorUI {
	
    public static ComponentUI createUI( JComponent c )
    {
        return new TinyPopupMenuSeparatorUI();
    }

    public void paint(Graphics g, JComponent c ) {
        switch(Theme.derivedStyle[Theme.style]) {
			case Theme.TINY_STYLE:
				drawTinySeparator(g, c.getSize());
				break;
			case Theme.W99_STYLE:
				drawWinSeparator(g, c.getSize());
				break;
			case Theme.YQ_STYLE:
				drawXpSeparator(g, c.getSize());
				break;
		}
    }
    
    private void drawTinySeparator(Graphics g, Dimension s) {
    	
    }
    
    private void drawWinSeparator(Graphics g, Dimension s) {
    	g.setColor(Theme.menuPopupColor[Theme.style].getColor());
        g.fillRect(0, 0, s.width, s.height);
		
		g.setColor(Theme.menuSepDarkColor[Theme.style].getColor());
		g.drawLine(2, 1, s.width - 3, 1);
		
		g.setColor(Theme.menuSepLightColor[Theme.style].getColor());
		g.drawLine(2, 2, s.width - 3, 2);
    }
    
    private void drawXpSeparator(Graphics g, Dimension s) {
    	g.setColor(Theme.menuPopupColor[Theme.style].getColor());
        g.fillRect(0, 0, s.width, s.height);
		
		g.setColor(Theme.menuSepDarkColor[Theme.style].getColor());
		g.drawLine(2, 1, s.width - 3, 1);
    }

    public Dimension getPreferredSize( JComponent c )
    { 
        return new Dimension(0, Theme.menuSeparatorHeight[Theme.derivedStyle[Theme.style]]);
    }
}