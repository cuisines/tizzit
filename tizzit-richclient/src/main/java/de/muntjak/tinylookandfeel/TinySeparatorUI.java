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

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSeparatorUI;

/**
 * TinySeparatorUI
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class TinySeparatorUI extends BasicSeparatorUI {
	
	protected static final Dimension vertDimension = new Dimension(0, 2);
	protected static final Dimension horzDimension = new Dimension(2, 0);

	public static ComponentUI createUI(JComponent c) {
		return new TinySeparatorUI();
	}

	protected void installDefaults(JSeparator s) {
		LookAndFeel.installColors(s, "Separator.background", "Separator.foreground");
	}

	public void paint(Graphics g, JComponent c) {
		switch(Theme.derivedStyle[Theme.style]) {
			case Theme.TINY_STYLE:
				drawTinySeparator(g, c);
				break;
			case Theme.W99_STYLE:
				drawWinSeparator(g, c);
				break;
			case Theme.YQ_STYLE:
				drawXpSeparator(g, c);
				break;
		}
	}
	
	protected void drawTinySeparator(Graphics g, JComponent c) {
	}
	
	protected void drawWinSeparator(Graphics g, JComponent c) {
		Dimension s = c.getSize();

		if (((JSeparator)c).getOrientation() == JSeparator.VERTICAL) {
			g.setColor(Theme.sepDarkColor[Theme.style].getColor());
			g.drawLine(0, 0, 0, s.height);

			g.setColor(Theme.sepLightColor[Theme.style].getColor());
			g.drawLine(1, 0, 1, s.height);
		}
		else { // HORIZONTAL
			g.setColor(Theme.sepDarkColor[Theme.style].getColor());
			g.drawLine(0, 0, s.width, 0);

			g.setColor(Theme.sepLightColor[Theme.style].getColor());
			g.drawLine(0, 1, s.width, 1);
		}
	}
	
	protected void drawXpSeparator(Graphics g, JComponent c) {
		Dimension s = c.getSize();
		g.setColor(c.getBackground());

		if (((JSeparator)c).getOrientation() == JSeparator.VERTICAL) {
			g.drawLine(0, 0, 0, s.height);
		}
		else { // HORIZONTAL
			g.drawLine(0, 0, s.width, 0);
		}
	}

	public Dimension getPreferredSize(JComponent c) {
		if (((JSeparator)c).getOrientation() == JSeparator.VERTICAL) {
			return horzDimension;
		}			
		else {
			return vertDimension;
		}			
	}
	
	public Dimension getMinimumSize(JComponent c) {
		return getPreferredSize(c);
	}
}
