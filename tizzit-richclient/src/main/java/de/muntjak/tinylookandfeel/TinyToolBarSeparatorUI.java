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
import javax.swing.*;

import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicToolBarSeparatorUI;

/**
 * ToolBarSeparatorUI
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class TinyToolBarSeparatorUI extends BasicToolBarSeparatorUI {
	
	private static final int W99_SIZE 	= 6;
	private static final int YQ_SIZE 	= 7;
	private int defaultSize = YQ_SIZE;

	public static ComponentUI createUI(JComponent c) {
		return new TinyToolBarSeparatorUI();
	}

	/**
	 * Overridden to do nothing
	 */
	protected void installDefaults(JSeparator s) {
		if(Theme.derivedStyle[Theme.style] == Theme.W99_STYLE) {
			defaultSize = W99_SIZE;
		}
	}
	
	public Dimension getMinimumSize(JComponent c) {
		JToolBar.Separator sep = (JToolBar.Separator)c;
		
		if (sep.getOrientation() == JSeparator.HORIZONTAL) {
			return new Dimension(0, 1);
		}
		else {
			return new Dimension(1, 0);
		}
	}
	
	public Dimension getMaximumSize(JComponent c) {
		JToolBar.Separator sep = (JToolBar.Separator)c;		
		Dimension size = sep.getSeparatorSize();

		if(sep.getOrientation() == JSeparator.HORIZONTAL) {
			if(size != null) return new Dimension(32767, size.height);
			
			return new Dimension(32767, defaultSize);
		}
		else {
			if(size != null) return new Dimension(32767, size.width);
			
			return new Dimension(defaultSize, 32767);
		}
	}
	
	public Dimension getPreferredSize(JComponent c) {
		JToolBar.Separator sep = (JToolBar.Separator)c;
		
		Dimension size = sep.getSeparatorSize();
		
		if(size != null) return size.getSize();
		
		if (sep.getOrientation() == JSeparator.HORIZONTAL) {
			return new Dimension(0, defaultSize);
		}
		else {
			return new Dimension(defaultSize, 0);
		}
	}

	public void paint(Graphics g, JComponent c) {
		switch(Theme.derivedStyle[Theme.style]) {
			case Theme.TINY_STYLE:
				drawTinyToolBarSeparator(g, c);
				break;
			case Theme.W99_STYLE:
				drawWinToolBarSeparator(g, c);
				break;
			case Theme.YQ_STYLE:
				drawXpToolBarSeparator(g, c);
				break;
		}
	}
	
	protected void drawTinyToolBarSeparator(Graphics g, JComponent c) {
	}
	
	protected void drawWinToolBarSeparator(Graphics g, JComponent c) {
		JToolBar.Separator sep = (JToolBar.Separator)c;
		
		if (sep.getOrientation() == JSeparator.HORIZONTAL) {
			Container cont = c.getParent();
			int y = sep.getPreferredSize().height / 2 - 1;	// centered if height is even
			
			g.setColor(Theme.toolSepDarkColor[Theme.style].getColor());
			g.drawLine(1, y, cont.getWidth() - 6, y);

			g.setColor(Theme.toolSepLightColor[Theme.style].getColor());
			g.drawLine(1, y + 1, cont.getWidth() - 6, y + 1);
		}
		else {
			int x = sep.getPreferredSize().width / 2 - 1;	// centered if height is even
			
			g.setColor(Theme.toolSepDarkColor[Theme.style].getColor());
			g.drawLine(x, 0, x, sep.getHeight());

			g.setColor(Theme.toolSepLightColor[Theme.style].getColor());
			g.drawLine(x + 1, 0, x + 1, sep.getHeight());
		}
	}
	
	protected void drawXpToolBarSeparator(Graphics g, JComponent c) {
		JToolBar.Separator sep = (JToolBar.Separator)c;
		
		if(sep.getOrientation() == JSeparator.HORIZONTAL) {
			int y = sep.getHeight() / 2;	// centered if height is odd
			
			g.setColor(Theme.toolSepDarkColor[Theme.style].getColor());
			g.drawLine(0, y, sep.getWidth(), y);
		}
		else {
			int x = sep.getWidth() / 2;	// centered if width is odd

			g.setColor(Theme.toolSepDarkColor[Theme.style].getColor());
			g.drawLine(x, 0, x, sep.getHeight());
		}
	}
}
