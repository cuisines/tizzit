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

import javax.swing.*;

import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;

import de.muntjak.tinylookandfeel.*;
import de.muntjak.tinylookandfeel.controlpanel.DrawRoutines;

/**
 * TinyProgressBarBorder
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class TinyProgressBarBorder extends AbstractBorder implements UIResource {
	
	protected static final Insets INSETS_YQ = new Insets(3, 3, 3, 3);
	protected static final Insets INSETS_99 = new Insets(2, 2, 2, 2);
	
	/**
	 * Draws the button border for the given component.
	 *
	 * @param mainColor The component to draw its border.
	 * @param g The graphics context.
	 * @param x The x coordinate of the top left corner.
	 * @param y The y coordinate of the top left corner.
	 * @param w The width.
	 * @param h The height.
	 */
	public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
		switch(Theme.derivedStyle[Theme.style]) {
			case Theme.TINY_STYLE :
				drawTinyBorder(c, g, x, y, w, h);
				break;
			case Theme.W99_STYLE :
				drawWinBorder(c, g, x, y, w, h);
				break;
			case Theme.YQ_STYLE :
				drawXpBorder(c, g, x, y, w, h);
				break;
		}
	}
	
	private void drawTinyBorder(Component c, Graphics g, int x, int y, int w, int h) {
	}

	private void drawWinBorder(Component c, Graphics g, int x, int y, int w, int h) {
		g.setColor(Theme.progressDarkColor[Theme.style].getColor());
		g.drawLine(x, y, x + w - 1, y);
		g.drawLine(x, 1, x, y + h - 1);

		g.setColor(Theme.progressLightColor[Theme.style].getColor());
		g.drawLine(x + w - 1, y + 1, x + w - 1, y + h - 1);
		g.drawLine(x + 1, y + h - 1, x + w - 1, y + h - 1);
	}

	private void drawXpBorder(Component c, Graphics g, int x, int y, int w, int h) {
		DrawRoutines.drawProgressBarBorder(g,
			Theme.progressBorderColor[Theme.style].getColor(), x, y, w, h);

		DrawRoutines.drawProgressBarBorder(g,
			Theme.progressDarkColor[Theme.style].getColor(), x + 1, y + 1, w - 2, h - 2);


		w -= 4; h -= 4;
		x += 2; y += 2;
		g.setColor(Theme.progressLightColor[Theme.style].getColor());
		// rect
		g.drawLine(x + 1, y, x + w - 2, y);
		g.drawLine(x, y + 1, x, y + h - 2);

		// track
		g.setColor(Theme.progressTrackColor[Theme.style].getColor());
		g.drawLine(x + 1, y + h - 1, x + w - 2, y + h - 1);
		g.drawLine(x + w - 1, y + 1, x + w - 1, y + h - 2);
	}
	
	/**
	 * Gets the border insets for a given component.
	 *
	 * @param c The component to get its border insets.
	 * @return Always returns the same insets as defined in <code>insets</code>.
	 */
	public Insets getBorderInsets(Component c) {
		if(Theme.derivedStyle[Theme.style] == Theme.YQ_STYLE) {
    		return INSETS_YQ;
    	}
    	
		return INSETS_99;
	}

}
