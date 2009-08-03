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
import de.muntjak.tinylookandfeel.controlpanel.*;

/**
 * TinyTextFieldBorder
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class TinyTextFieldBorder extends AbstractBorder implements UIResource {

    private Insets insets;

    public TinyTextFieldBorder() {
        insets = Theme.textInsets[Theme.style];
    }

    public TinyTextFieldBorder(Insets insets) {
        this.insets = insets;
    }

	/**
	 * Gets the border insets for a given component.
	 *
	 * @param mainColor The component to get its border insets.
	 * @return Always returns the same insets as defined in <code>insets</code>.
	 */
	public Insets getBorderInsets(Component c) {
		return insets;
	}

	/**
	 * Use the skin to paint the border
	 * @see javax.swing.border.Border#paintBorder(Component, Graphics, int, int, int, int)
	 */
	public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
		switch(Theme.derivedStyle[Theme.style]) {
			case Theme.TINY_STYLE:
				drawTinyBorder(c, g, x, y, w, h);
				break;
			case Theme.W99_STYLE:
				drawWinBorder(c, g, x, y, w, h);
				break;
			case Theme.YQ_STYLE:
				drawXpBorder(c, g, x, y, w, h);
				break;
		}
	}
	
	private void drawTinyBorder(Component c, Graphics g, int x, int y, int w, int h) {
	}
		
	private void drawWinBorder(Component c, Graphics g, int x, int y, int w, int h) {
		if (!c.isEnabled()) {
			g.setColor(Theme.textBorderDisabledColor[Theme.style].getColor());
		}
		else {
			g.setColor(Theme.textBorderColor[Theme.style].getColor());
		}		
		g.drawLine(x + 1, y + 1, x + w - 3, y + 1);
		g.drawLine(x + 1, y + 2, x + 1, y + h - 3);
		
		if (!c.isEnabled()) {
			g.setColor(Theme.textBorderDarkDisabledColor[Theme.style].getColor());
		}
		else {
			g.setColor(Theme.textBorderDarkColor[Theme.style].getColor());
		}
		g.drawLine(x, y, x + w - 2, y);
		g.drawLine(x, y + 1, x, y + h - 2);
		
		g.setColor(Theme.backColor[Theme.style].getColor());
		g.drawLine(x + 1, y + h - 2, x + w - 2, y + h - 2);
		g.drawLine(x + w - 2, y + 1, x + w - 2, y + h - 2);
		
		if (!c.isEnabled()) {
			g.setColor(Theme.textBorderLightDisabledColor[Theme.style].getColor());
		}
		else {
			g.setColor(Theme.textBorderLightColor[Theme.style].getColor());
		}
		g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
		g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);
	}
	
	private void drawXpBorder(Component c, Graphics g, int x, int y, int w, int h) {
		if (!c.isEnabled()) {
			DrawRoutines.drawBorder(
				g, Theme.textBorderDisabledColor[Theme.style].getColor(), x, y, w, h);
		}
		else {
			DrawRoutines.drawBorder(
				g, Theme.textBorderColor[Theme.style].getColor(), x, y, w, h);
		}
	}
}