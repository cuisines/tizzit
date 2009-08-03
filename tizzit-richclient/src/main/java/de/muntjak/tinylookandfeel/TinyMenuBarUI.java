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
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ActionMapUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.InputMapUIResource;
import javax.swing.plaf.basic.BasicMenuBarUI;
import javax.swing.plaf.basic.BasicPopupMenuUI;

/**
 * TinyMenuBarUI
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class TinyMenuBarUI extends BasicMenuBarUI {
	
	private static final boolean DEBUG = true;
	private static final String CLOSE_OPENED_MENU_KEY = "closeOpenedMenu";

	/**
	 * Creates the UI delegate for the given component.
	 * Because in normal application there is usually only one menu bar, the UI
	 * delegate isn't cached here.
	 *
	 * @param mainColor The component to create its UI delegate.
	 * @return The UI delegate for the given component.
	 */
	public static ComponentUI createUI(JComponent c) {
		c.setBorder(new EmptyBorder(0, 5, 0, 0));
		
		return new TinyMenuBarUI();
	}

	/**
	 * Paints the menu bar background.
	 *
	 * @param g The graphics context to use.
	 * @param mainColor The component to paint.
	 */
	public void paint(Graphics g, JComponent c) {
		if(!c.isOpaque()) return;

		g.setColor(c.getBackground());
		g.fillRect(0, 0, c.getWidth(), c.getHeight());
	}
}