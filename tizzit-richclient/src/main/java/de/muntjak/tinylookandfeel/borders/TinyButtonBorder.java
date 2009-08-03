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
 * TinyButtonBorder is the border for JButton, JToggleButton and JSpinner buttons.
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class TinyButtonBorder extends AbstractBorder implements UIResource {
	
	protected final Insets borderInsets = new Insets(2, 2, 2, 2);

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
		boolean isDefault = (c instanceof JButton) && (((JButton)c).isDefaultButton());
		
		if(b.getModel().isPressed() ||
			((b instanceof JToggleButton) && b.isSelected()))
		{
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, w - 1, h - 1);
			
			g.setColor(Theme.buttonDarkColor[Theme.style].getColor());
			g.drawRect(1, 1, w - 3, h - 3);
			return;
		}
		else if(isDefault && b.isEnabled()) {
			g.setColor(Theme.buttonDefaultColor[Theme.style].getColor());
			g.drawRect(0, 0, w - 1, h - 1);
			x += 1; y += 1; w -= 1; h -= 1;
		}
		
		if(!b.isEnabled()) {
			g.setColor(Theme.buttonLightDisabledColor[Theme.style].getColor());
		}
		else {
			g.setColor(Theme.buttonLightColor[Theme.style].getColor());
		}
		g.drawLine(x, y, w - 2, y);
		g.drawLine(x, y + 1, x, h - 2);
		
		if(!b.isEnabled()) {
			g.setColor(Theme.buttonBorderDisabledColor[Theme.style].getColor());
		}
		else {
			g.setColor(Theme.buttonBorderColor[Theme.style].getColor());
		}
		g.drawLine(x, h - 1, w - 1, h - 1);
		g.drawLine(w - 1, y, w - 1, h - 1);
		
		if(!b.isEnabled()) {
			g.setColor(Theme.buttonDarkDisabledColor[Theme.style].getColor());
		}
		else {
			g.setColor(Theme.buttonDarkColor[Theme.style].getColor());
		}
		g.drawLine(x + 1, h - 2, w - 2, h - 2);
		g.drawLine(w - 2, y + 1, w - 2, h - 2);
	}

	private void drawXpBorder(Component c, Graphics g, int x, int y, int w, int h) {
		AbstractButton b = (AbstractButton)c;
		boolean isDefault = (c instanceof JButton) && (((JButton)c).isDefaultButton());
		boolean isComboBoxButton =
			Boolean.TRUE.equals(b.getClientProperty("isComboBoxButton"));

		if(isComboBoxButton) {
			if (!b.isEnabled()) {
				DrawRoutines.drawRoundedBorder(
					g, Theme.comboBorderDisabledColor[Theme.style].getColor(), x, y, w, h);
			}
			else {	
				DrawRoutines.drawRoundedBorder(
					g, Theme.comboBorderColor[Theme.style].getColor(), x, y, w, h);
	
				if(b.getModel().isPressed()) return;

				if(b.getModel().isRollover() && Theme.comboRollover[Theme.style]) {
					DrawRoutines.drawRolloverBorder(
						g, Theme.buttonRolloverColor[Theme.style].getColor(), x, y, w, h);
				}
			}
		}
		else {	// it's a normal JButton or a JSpinner button
			boolean isSpinnerButton =
				Boolean.TRUE.equals(b.getClientProperty("isSpinnerButton"));
			boolean paintRollover =
				(isSpinnerButton && Theme.spinnerRollover[Theme.style]) ||
				(!isSpinnerButton && Theme.buttonRollover[Theme.style]);
			
			if (!b.isEnabled()) {
				DrawRoutines.drawRoundedBorder(
					g, Theme.buttonBorderDisabledColor[Theme.style].getColor(), x, y, w, h);
			}
			else {	
				DrawRoutines.drawRoundedBorder(
					g, Theme.buttonBorderColor[Theme.style].getColor(), x, y, w, h);
	
				if(b.getModel().isPressed()) return;
				
				if(b.getModel().isRollover() && paintRollover) {
					DrawRoutines.drawRolloverBorder(
						g, Theme.buttonRolloverColor[Theme.style].getColor(), x, y, w, h);
				}
				else if(isDefault ||
					(Theme.buttonFocusBorder[Theme.style] && b.isFocusOwner()))
				{
					DrawRoutines.drawRolloverBorder(
						g, Theme.buttonDefaultColor[Theme.style].getColor(), x, y, w, h);
				}
			}
		}
	}

	/**
	 * Gets the border insets for a given component.
	 *
	 * @param c The component to get its border insets.
	 * @return Always returns the same insets as defined in <code>insets</code>.
	 */
	public Insets getBorderInsets(Component c) {
		return borderInsets;
	}
}