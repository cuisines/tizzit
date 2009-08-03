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
 * TinyToolButtonBorder is the border for JButton, JToggleButton and JSpinner buttons.
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class TinyToolButtonBorder extends AbstractBorder {
	
	protected final Insets insets = new Insets(1, 1, 1, 1);

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
		switch (Theme.derivedStyle[Theme.style]) {
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
		AbstractButton b = (AbstractButton)c;
		
		g.translate(-x, -y);

		g.setColor(Theme.toolBorderLightColor[Theme.style].getColor());
		if(b.isSelected() || (b.getModel().isPressed() && b.getModel().isRollover())) {
			g.drawLine(0, h - 1, w - 1, h - 1);
			g.drawLine(w - 1, 1, w - 1, h - 1);
		}
		else {
			g.drawLine(0, 0, w - 1, 0);
			g.drawLine(0, 1, 0, h - 1);
		}

		g.setColor(Theme.toolBorderDarkColor[Theme.style].getColor());
		if(b.isSelected() || (b.getModel().isPressed() && b.getModel().isRollover())) {
			g.drawLine(0, 0, w - 1, 0);
			g.drawLine(0, 1, 0, h - 1);
		}
		else {
			g.drawLine(0, h - 1, w - 1, h - 1);
			g.drawLine(w - 1, 1, w - 1, h - 1);
		}
		
		g.translate(x, y);
	}

	private void drawXpBorder(Component c, Graphics g, int x, int y, int w, int h) {
		AbstractButton b = (AbstractButton)c;		
		Color col = null;
		boolean isFileChooserButton = Boolean.TRUE.equals(
			b.getClientProperty(TinyFileChooserUI.IS_FILE_CHOOSER_BUTTON_KEY));
		
		// New in 1.3.7 (previously only b.getModel().isRollover() evaluated)
		boolean isRollover =  b.getModel().isRollover() || b.getModel().isArmed();
		
		if(b.getModel().isPressed()) {
			if(isRollover) {
				col = Theme.toolBorderPressedColor[Theme.style].getColor();
			}
			else {
				if(b.isSelected()) {
					col = Theme.toolBorderSelectedColor[Theme.style].getColor();
				}
				else {
					if(isFileChooserButton) return;	// no border painted
					
					col = Theme.toolBorderColor[Theme.style].getColor();
				}
			}
		}
		else if(isRollover) {
			if(b.isSelected()) {
				col = Theme.toolBorderSelectedColor[Theme.style].getColor();
			}
			else {
				col = Theme.toolBorderRolloverColor[Theme.style].getColor();
			}
		}
		else if(b.isSelected()) {
			col = Theme.toolBorderSelectedColor[Theme.style].getColor();
		}
		else {
			if(isFileChooserButton) return;	// no border painted
			
			col = Theme.toolBorderColor[Theme.style].getColor();
		}

		DrawRoutines.drawRoundedBorder(g, col, x, y, w, h);
	}

	/**
	 * Gets the border insets for a given component.
	 * 
	 * @return some insets...
	 */
	public Insets getBorderInsets(Component c) {
		if(!(c instanceof AbstractButton)) return insets;
		
		AbstractButton b = (AbstractButton)c;
		
		if(b.getMargin() == null || (b.getMargin() instanceof UIResource)) {
			return new Insets(
				Theme.toolMarginTop[Theme.style],
				Theme.toolMarginLeft[Theme.style],
				Theme.toolMarginBottom[Theme.style],
				Theme.toolMarginRight[Theme.style]);
		}
		else {
			Insets margin = b.getMargin();
			
			return new Insets(
				margin.top + 1,
				margin.left + 1,
				margin.bottom + 1,
				margin.right + 1);
		}
	}
}