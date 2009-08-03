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

import javax.swing.*;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.metal.*;
import java.awt.*;
import java.io.Serializable;

/**
 * MenuItemIconFactory
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class MenuItemIconFactory implements Serializable {

	private static Icon checkBoxMenuItemIcon;
	private static Icon radioButtonMenuItemIcon;
	private static Icon menuArrowIcon;
	static private final Dimension menuCheckIconSize = new Dimension(10, 10);
	static private final Dimension menuArrowIconSize = new Dimension(4, 8);

	public static Icon getCheckBoxMenuItemIcon() {
		if (checkBoxMenuItemIcon == null) {
			checkBoxMenuItemIcon = new CheckBoxMenuItemIcon();
		}

		return checkBoxMenuItemIcon;
	}

	public static Icon getRadioButtonMenuItemIcon() {
		if (radioButtonMenuItemIcon == null) {
			radioButtonMenuItemIcon = new RadioButtonMenuItemIcon();
		}

		return radioButtonMenuItemIcon;
	}

	public static Icon getMenuArrowIcon() {
		if (menuArrowIcon == null) {
			menuArrowIcon = new MenuArrowIcon();
		}

		return menuArrowIcon;
	}

	private static class CheckBoxMenuItemIcon implements Icon, UIResource, Serializable {

		public void paintIcon(Component c, Graphics g, int x, int y) {
			JMenuItem item = (JMenuItem)c;
			ButtonModel model = item.getModel();

			boolean isSelected = model.isSelected();
			if (!isSelected)
				return;

			boolean isEnabled = model.isEnabled();
			boolean isPressed = model.isPressed();
			boolean isArmed = model.isArmed();

			g.translate(x, y);

			if (isEnabled) {
				if (model.isArmed() || (c instanceof JMenu && model.isSelected())) {
					// rollover
					g.setColor(Theme.menuIconRolloverColor[Theme.style].getColor());
				} else {
					// !rollover
					g.setColor(Theme.menuIconColor[Theme.style].getColor());
				}
			} else {
				// disabled
				g.setColor(Theme.menuIconDisabledColor[Theme.style].getColor());
			}

			// paint arrow
			g.drawLine(2, 4, 2, 6);
			g.drawLine(3, 5, 3, 7);
			g.drawLine(4, 6, 4, 8);
			g.drawLine(5, 5, 5, 7);
			g.drawLine(6, 4, 6, 6);
			g.drawLine(7, 3, 7, 5);
			g.drawLine(8, 2, 8, 4);

			if (!isEnabled && Theme.derivedStyle[Theme.style] == Theme.W99_STYLE) {
				// white shadow
				g.setColor(Theme.menuIconShadowColor[Theme.style].getColor());
				g.drawLine(9, 3, 9, 5);
				g.drawLine(8, 5, 8, 6);
				g.drawLine(7, 6, 7, 7);
				g.drawLine(6, 7, 6, 8);
				g.drawLine(5, 8, 5, 9);
			}

			g.translate(-x, -y);
		}

		public int getIconWidth() {
			return menuCheckIconSize.width;
		}

		public int getIconHeight() {
			return menuCheckIconSize.height;
		}
	}

	private static class RadioButtonMenuItemIcon implements Icon, UIResource, Serializable {

		public void paintIcon(Component c, Graphics g, int x, int y) {
			JMenuItem b = (JMenuItem)c;
			ButtonModel model = b.getModel();

			boolean isSelected = model.isSelected();
			boolean isEnabled = model.isEnabled();
			boolean isPressed = model.isPressed();
			boolean isArmed = model.isArmed();

			g.translate(x, y);

			if (isEnabled) {
				if (isPressed || isArmed) {
					// rollover
					g.setColor(Theme.menuIconRolloverColor[Theme.style].getColor());
				} else {
					// !rollover
					g.setColor(Theme.menuIconColor[Theme.style].getColor());
				}
			} else {
				// disabled
				g.setColor(Theme.menuIconDisabledColor[Theme.style].getColor());
			}

			// draw circle
			g.drawLine(3, 0, 6, 0);
			g.drawLine(9, 3, 9, 6);
			g.drawLine(3, 9, 6, 9);
			g.drawLine(0, 3, 0, 6);
			g.drawLine(1, 1, 2, 1);
			g.drawLine(7, 1, 8, 1);
			g.drawLine(1, 8, 2, 8);
			g.drawLine(7, 8, 8, 8);
			g.drawLine(1, 2, 1, 2);
			g.drawLine(8, 2, 8, 2);
			g.drawLine(1, 7, 1, 7);
			g.drawLine(8, 7, 8, 7);

			if (isSelected) {
				// draw check
				g.drawLine(4, 3, 5, 3);
				g.drawLine(3, 4, 6, 4);
				g.drawLine(3, 5, 6, 5);
				g.drawLine(4, 6, 5, 6);
			}

			g.translate(-x, -y);
		}

		public int getIconWidth() {
			return menuCheckIconSize.width;
		}

		public int getIconHeight() {
			return menuCheckIconSize.height;
		}
	}

	private static class MenuArrowIcon implements Icon, UIResource, Serializable {
		
		public void paintIcon(Component c, Graphics g, int x, int y) {
			JMenuItem b = (JMenuItem)c;
			ButtonModel model = b.getModel();

			g.translate(x, y);

			if (!model.isEnabled()) {
				g.setColor(Theme.menuDisabledFgColor[Theme.style].getColor());
			} else {
				if (model.isArmed() || (c instanceof JMenu && model.isSelected())) {
					g.setColor(Theme.menuSelectedTextColor[Theme.style].getColor());
				} else {
					g.setColor(b.getForeground());
				}
			}
			if (c.getComponentOrientation().isLeftToRight()) {
				g.drawLine(0, 0, 0, 7);
				g.drawLine(1, 1, 1, 6);
				g.drawLine(2, 2, 2, 5);
				g.drawLine(3, 3, 3, 4);
			} else {
				g.drawLine(4, 0, 4, 7);
				g.drawLine(3, 1, 3, 6);
				g.drawLine(2, 2, 2, 5);
				g.drawLine(1, 3, 1, 4);
			}

			g.translate(-x, -y);
		}

		public int getIconWidth() {
			return menuArrowIconSize.width;
		}

		public int getIconHeight() {
			return menuArrowIconSize.height;
		}
	}
}
