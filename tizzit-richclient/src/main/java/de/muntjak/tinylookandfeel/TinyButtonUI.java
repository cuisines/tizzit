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
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalButtonUI;

import de.muntjak.tinylookandfeel.controlpanel.*;

/**
 * TinyButtonUI. The UI delegate for JButton, JToggleButton and ArrowButtons for JSpinner.
 *
 * @version 1.3.04
 * @author Hans Bickel
 */
public class TinyButtonUI extends MetalButtonUI {

	// if a button has not the defined background, it will
	// be darkened resp. lightened by BG_CHANGE amount if
	// pressed or rollover
	public static final int BG_CHANGE_AMOUNT = 10;

	/**
	 * The Cached UI delegate.
	 */
	private static final TinyButtonUI buttonUI = new TinyButtonUI();

	/* the only instance of the stroke for the focus */
	private static final BasicStroke focusStroke =
		new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, new float[] { 1.0f, 1.0f }, 0.0f);

	private boolean graphicsTranslated;
	private boolean isToolBarButton, isFileChooserButton;
	private boolean isDefault;

	public TinyButtonUI() {}
	
	public void installUI(JComponent c) {
		super.installUI(c);
        
        if(!Theme.buttonEnter[Theme.style]) return;
        if(!c.isFocusable()) return;

        InputMap km = (InputMap)UIManager.get(getPropertyPrefix() + "focusInputMap");

        if(km != null) {
        	// replace SPACE with ENTER (but SPACE will still work, don't know why)
        	km.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "pressed");
        	km.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "released");
        }
    }

	public void installDefaults(AbstractButton button) {
		super.installDefaults(button);
		button.setRolloverEnabled(true);
	}

	protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {
		if(isFileChooserButton ||
			(isToolBarButton && !Theme.toolFocus[Theme.style]) ||
			!Theme.buttonFocus[Theme.style])
		{
			return;
		}

		Graphics2D g2d = (Graphics2D)g;
		Rectangle focusRect = b.getBounds();

		g.setColor(Color.black);
		g2d.setStroke(focusStroke);

		int x1 = 2;
		int y1 = 2;
		int x2 = x1 + focusRect.width - 5;
		int y2 = y1 + focusRect.height - 5;

		if(!isToolBarButton) {
			x1++;
			y1++;
			x2--;
			y2--;
		}

		if(graphicsTranslated) {
			g.translate(-1, -1);
		}

		g2d.drawLine(x1, y1, x2, y1);
		g2d.drawLine(x1, y1, x1, y2);
		g2d.drawLine(x1, y2, x2, y2);
		g2d.drawLine(x2, y1, x2, y2);
	}

	/**
	 * Creates the UI delegate for the given component.
	 *
	 * @param mainColor The component to create its UI delegate.
	 * @return The UI delegate for the given component.
	 */
	public static ComponentUI createUI(final JComponent c) {
		return buttonUI;
	}

	protected void paintButtonPressed(Graphics g, AbstractButton button) {
		if(isToolBarButton || isFileChooserButton) return;

		Color col = null;
		if(!button.getBackground().equals(Theme.buttonNormalColor[Theme.style].getColor())) {
			col = ColorRoutines.darken(button.getBackground(), BG_CHANGE_AMOUNT);
		}
		else {
			col = Theme.buttonPressedColor[Theme.style].getColor();
		}

		g.setColor(col);

		switch (Theme.derivedStyle[Theme.style]) {
			case Theme.TINY_STYLE :
				drawTinyButton(g, button, col);
				break;
			case Theme.W99_STYLE :
				drawWinButton(g, button, col);
				break;
			case Theme.YQ_STYLE :
				drawXpButton(g, button, col, false);
				break;
		}

		if(!(button instanceof JToggleButton)) {
			// Changed in 1.3.04: If button is icon-only
			// then don't shift
			if(Theme.shiftButtonText[Theme.style] &&
				button.getText() != null &&
				!"".equals(button.getText()))
			{
				g.translate(1, 1);
				graphicsTranslated = true;
			}
		}
	}

	public void paintToolBarButton(Graphics g, AbstractButton b) {
		Color col = null;
		
		// New in 1.3.7
		boolean isRollover = b.getModel().isRollover() || b.getModel().isArmed();
		Color toolButtColor = null;
		
		if(isFileChooserButton) {
			toolButtColor = b.getParent().getBackground();
		}
		else {
			toolButtColor = Theme.toolButtColor[Theme.style].getColor();
		}

		if(b.getModel().isPressed()) {
			if(isRollover) {
				col = Theme.toolButtPressedColor[Theme.style].getColor();
			}
			else {
				if(b.isSelected()) {
					col = Theme.toolButtSelectedColor[Theme.style].getColor();
				}
				else {
					col = toolButtColor;
				}
			}
		}
		else if(isRollover && Theme.derivedStyle[Theme.style] == Theme.YQ_STYLE) {
			if(b.isSelected()) {
				col = Theme.toolButtSelectedColor[Theme.style].getColor();
			}
			else {
				col = Theme.toolButtRolloverColor[Theme.style].getColor();
			}
		}
		else if(b.isSelected()) {
			col = Theme.toolButtSelectedColor[Theme.style].getColor();
		}
		else {
			col = toolButtColor;
		}

		g.setColor(col);

		switch (Theme.derivedStyle[Theme.style]) {
			case Theme.TINY_STYLE :
				drawTinyToolBarButton(g, b, col, false);
				break;
			case Theme.W99_STYLE :
				drawWinToolBarButton(g, b, col, false);
				break;
			case Theme.YQ_STYLE :
				drawXpToolBarButton(g, b, col, false);
				break;
		}
	}

	public void paint(Graphics g, JComponent c) {
		AbstractButton button = (AbstractButton)c;

		if(isToolBarButton || isFileChooserButton) {
			paintToolBarButton(g, button);
			
			// the base class may paint text and/or icons
			super.paint(g, c);

			return;
		}

		if((button instanceof JToggleButton) && button.isSelected()) {
			paintButtonPressed(g, button);

			// the base class may paint text and/or icons
			super.paint(g, c);
			return;
		}

		isDefault = (c instanceof JButton) && (((JButton)c).isDefaultButton());
		boolean isRollover = button.getModel().isRollover() &&
			Theme.buttonRollover[Theme.derivedStyle[Theme.style]];
		boolean isDefinedBackground = c.getBackground().equals(
			Theme.buttonNormalColor[Theme.style].getColor());
		Color col = null;

		if(!button.isEnabled()) {		
			col = Theme.buttonDisabledColor[Theme.style].getColor();
		}
		else if(button.getModel().isPressed()) {
			if(isRollover) {
				if(isDefinedBackground) {
					col = Theme.buttonPressedColor[Theme.style].getColor();
				}
				else {
					col = ColorRoutines.darken(c.getBackground(), BG_CHANGE_AMOUNT);
				}
			}
			else {
				// button pressed but mouse exited
				col = c.getBackground();
			}
		}
		else if(isRollover) {
			if(isDefinedBackground) {
				col = Theme.buttonRolloverBgColor[Theme.style].getColor();
			}
			else {
				col = ColorRoutines.lighten(c.getBackground(), BG_CHANGE_AMOUNT);
			}
		}
		else {
			col = c.getBackground();
		}

		g.setColor(col);

		switch(Theme.derivedStyle[Theme.style]) {
			case Theme.TINY_STYLE :
				drawTinyButton(g, button, col);
				break;
			case Theme.W99_STYLE :
				drawWinButton(g, button, col);
				break;
			case Theme.YQ_STYLE :
				drawXpButton(g, button, col, isRollover);
				break;
		}

		// the base class may paint text and/or icons
		super.paint(g, c);
	}

	// this overrides BasicButtonUI.paintIcon(...)
	protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {
		if(c instanceof JToggleButton) {
			paintToggleButtonIcon(g, c, iconRect);
		}
		else {
			super.paintIcon(g, c, iconRect);
		}
	}

	protected void paintToggleButtonIcon(Graphics g, JComponent c, Rectangle iconRect) {
		AbstractButton b = (AbstractButton)c;
		ButtonModel model = b.getModel();
		Icon icon = null;

		if(!model.isEnabled()) {
			if(model.isSelected()) {
				icon = (Icon)b.getDisabledSelectedIcon();
			}
			else {
				icon = (Icon)b.getDisabledIcon();
			}
		}
		else if(model.isPressed() && model.isArmed()) {
			icon = (Icon)b.getPressedIcon();
			if(icon == null) {
				// Use selected icon
				icon = (Icon)b.getSelectedIcon();
			}
		}
		else if(model.isSelected()) {
			if(b.isRolloverEnabled() && model.isRollover()) {
				icon = (Icon)b.getRolloverSelectedIcon();
				if(icon == null) {
					icon = (Icon)b.getSelectedIcon();
				}
			}
			else {
				icon = (Icon)b.getSelectedIcon();
			}
		}
		else if(model.isRollover() && Theme.derivedStyle[Theme.style] == Theme.YQ_STYLE) {
			icon = (Icon)b.getRolloverIcon();
		}

		if(icon == null) {
			icon = (Icon)b.getIcon();
		}

		icon.paintIcon(b, g, iconRect.x, iconRect.y);
	}

	public void update(Graphics g, JComponent c) {
		isToolBarButton = Boolean.TRUE.equals(
			c.getClientProperty(TinyToolBarUI.IS_TOOL_BAR_BUTTON_KEY));
		isFileChooserButton = Boolean.TRUE.equals(
			c.getClientProperty(TinyFileChooserUI.IS_FILE_CHOOSER_BUTTON_KEY));
		paint(g, c);
		graphicsTranslated = false;
	}

	private void drawTinyButton(Graphics g, AbstractButton b, Color c) {
	}

	private void drawWinButton(Graphics g, AbstractButton b, Color c) {
		int w = b.getWidth();
		int h = b.getHeight();

		if(b.isContentAreaFilled() && b.isOpaque()) {
			g.fillRect(1, 1, w - 2, h - 2);
		}
	}

	private void drawXpButton(Graphics g, AbstractButton b, Color c, boolean isRollover) {
		if(!b.isContentAreaFilled()) return;
		if(!b.isOpaque()) return;

		int w = b.getWidth();
		int h = b.getHeight();
		
		// paint border background
		Color bg = b.getParent().getBackground();
		g.setColor(bg);
		g.drawRect(0, 0, w - 1, h - 1);

		int spread1 = Theme.buttonSpreadLight[Theme.style];
		int spread2 = Theme.buttonSpreadDark[Theme.style];
		if(!b.isEnabled()) {
			spread1 = Theme.buttonSpreadLightDisabled[Theme.style];
			spread2 = Theme.buttonSpreadDarkDisabled[Theme.style];
		}

		float spreadStep1 = 10.0f * spread1 / (h - 3);
		float spreadStep2 = 10.0f * spread2 / (h - 3);
		int halfY = h / 2;
		int yd;

		for (int y = 1; y < h - 1; y++) {
			if(y < halfY) {
				yd = halfY - y;
				g.setColor(ColorRoutines.lighten(c, (int)(yd * spreadStep1)));
			}
			else if(y == halfY) {
				g.setColor(c);
			}
			else {
				yd = y - halfY;
				g.setColor(ColorRoutines.darken(c, (int)(yd * spreadStep2)));
			}

			g.drawLine(2, y, w - 3, y);

			if(y == 1) {
				// left vertical line
				g.drawLine(1, 1, 1, h - 2);
				
				if(isRollover || isDefault) {
					// right vertical line
					g.drawLine(w - 2, 1, w - 2, h - 2);
				}
			}
			else if(y == h - 2 && !isRollover && !isDefault) {
				// right vertical line
				g.drawLine(w - 2, 1, w - 2, h - 2);
			}
		}
		
		// 1 pixel away from each corner
		if(isRollover) {
			g.setColor(Theme.buttonRolloverColor[Theme.style].getColor());
			g.drawLine(1, h - 2, 1, h - 2);
			g.drawLine(w - 2, h - 2, w - 2, h - 2);
		}
		else if(isDefault) {
			g.setColor(Theme.buttonDefaultColor[Theme.style].getColor());
			g.drawLine(1, h - 2, 1, h - 2);
			g.drawLine(w - 2, h - 2, w - 2, h - 2);
		}
	}

	private void drawTinyToolBarButton(Graphics g, AbstractButton b, Color c, boolean isPressed) {
	}

	private void drawWinToolBarButton(Graphics g, AbstractButton b, Color c, boolean isPressed) {
		int w = b.getWidth();
		int h = b.getHeight();

		if(b.isContentAreaFilled()) {
			g.fillRect(1, 1, w - 2, h - 2);
		}
	}

	private void drawXpToolBarButton(Graphics g, AbstractButton b, Color c, boolean isPressed) {
		int w = b.getWidth();
		int h = b.getHeight();

		if(b.isContentAreaFilled()) {
			g.fillRect(1, 1, w - 2, h - 2);
		}
		
		// paint border background
		Color bg = b.getParent().getBackground();
		g.setColor(bg);
		g.drawRect(0, 0, w - 1, h - 1);
	}
}