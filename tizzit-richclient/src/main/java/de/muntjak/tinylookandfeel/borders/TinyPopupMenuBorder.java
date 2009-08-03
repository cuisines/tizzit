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

import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;

import de.muntjak.tinylookandfeel.*;

/**
 * TinyPopupMenuBorder
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class TinyPopupMenuBorder extends AbstractBorder implements UIResource {

	/**
	 * Draws a simple 3d border for the given component.
	 *
	 * @param mainColor The component to draw its border.
	 * @param g The graphics context.
	 * @param x The x coordinate of the top left corner.
	 * @param y The y coordinate of the top left corner.
	 * @param w The width.
	 * @param h The height.
	 */
	public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
		switch (Theme.derivedStyle[Theme.style]) {
			case Theme.TINY_STYLE :
				drawTinyBorder(g, x, y, w, h);
				break;
			case Theme.W99_STYLE :
				drawWinBorder(g, x, y, w, h);
				break;
			case Theme.YQ_STYLE :
				drawXpBorder(g, x, y, w, h);
				break;
		}
	}

	private void drawTinyBorder(Graphics g, int x, int y, int w, int h) {

	}

	private void drawWinBorder(Graphics g, int x, int y, int w, int h) {
		g.translate(x, y);
		// Inner highlight:
		g.setColor(Theme.menuInnerHilightColor[Theme.style].getColor());
		g.drawLine(1, 1, w - 3, 1);
		g.drawLine(1, 1, 1, h - 3);

		// Inner shadow:
		g.setColor(Theme.menuInnerShadowColor[Theme.style].getColor());
		g.drawLine(w - 2, 1, w - 2, h - 2);
		g.drawLine(1, h - 2, w - 2, h - 2);

		// Outer highlight:
		g.setColor(Theme.menuOuterHilightColor[Theme.style].getColor());
		g.drawLine(0, 0, w - 2, 0);
		g.drawLine(0, 0, 0, h - 1);

		// Outer shadow:
		g.setColor(Theme.menuOuterShadowColor[Theme.style].getColor());
		g.drawLine(w - 1, 0, w - 1, h - 1);
		g.drawLine(0, h - 1, w - 1, h - 1);
		g.translate(-x, -y);
	}

	private void drawXpBorder(Graphics g, int x, int y, int w, int h) {
		g.translate(x, y);
		// Inner highlight:
		g.setColor(Theme.menuInnerHilightColor[Theme.style].getColor());
		g.drawLine(1, 1, w - 3, 1);
		g.drawLine(1, 1, 1, h - 3);

		// Inner shadow:
		g.setColor(Theme.menuInnerShadowColor[Theme.style].getColor());
		g.drawLine(w - 2, 1, w - 2, h - 2);
		g.drawLine(1, h - 2, w - 2, h - 2);

		// Outer highlight:
		g.setColor(Theme.menuOuterHilightColor[Theme.style].getColor());
		g.drawLine(0, 0, w - 2, 0);
		g.drawLine(0, 0, 0, h - 1);

		// Outer shadow:
		g.setColor(Theme.menuOuterShadowColor[Theme.style].getColor());
		g.drawLine(w - 1, 0, w - 1, h - 1);
		g.drawLine(0, h - 1, w - 1, h - 1);
		g.translate(-x, -y);
	}

	/**
	 * Gets the border insets for a given component.
	 *
	 * @param mainColor The component to get its border insets.
	 * @return Always returns the same insets as defined in <code>insets</code>.
	 */
	public Insets getBorderInsets(Component c) {
		return Theme.menuBorderInsets[Theme.style];
	}
}