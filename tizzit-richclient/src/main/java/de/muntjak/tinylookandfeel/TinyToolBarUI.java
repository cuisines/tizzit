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
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.metal.MetalToolBarUI;

import de.muntjak.tinylookandfeel.borders.*;

/**
 * TinyToolBarUI
 * 
 * @version 1.1
 * @author Hans Bickel
 */
public class TinyToolBarUI extends MetalToolBarUI {

	public static final String IS_TOOL_BAR_BUTTON_KEY = "JToolBar.isToolbarButton";
	public static final int floatableGripSize = 8;

	/**
	 * The Border used for buttons in a toolbar
	 */
	private static Border toolButtonBorder = new TinyToolButtonBorder();

	/**
	 * Creates the UI delegate for the given component.
	 *
	 * @param c The component to create its UI delegate.
	 * @return The UI delegate for the given component.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new TinyToolBarUI();
	}

	/**
	 * Installs some default values for the given toolbar.
	 * The gets a rollover property.
	 *
	 * @param mainColor The reference of the toolbar to install its default values.
	 */
	public void installUI(JComponent c) {
		super.installUI(c);
		c.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
	}
	
	/**
	 * Overrides BasicToolBarUI.createFloatingWindow() to return a
	 * simple dialog (which works with TinyLaF).
     * Creates a window which contains the toolbar after it has been
     * dragged out from its container
     * @return a <code>RootPaneContainer</code> object, containing the toolbar.
     */
    protected RootPaneContainer createFloatingWindow(JToolBar toolbar) {
		JDialog dialog;
		Window window = SwingUtilities.getWindowAncestor(toolbar);
		
		if(window instanceof Frame) {
		    dialog = new JDialog((Frame)window, toolbar.getName(), false);
		}
		else if(window instanceof Dialog) {
		    dialog = new JDialog((Dialog)window, toolbar.getName(), false);
		}
		else {
		    dialog = new JDialog((Frame)null, toolbar.getName(), false);
		}
	
		dialog.setTitle(toolbar.getName());
		dialog.setResizable(false);
		WindowListener wl = createFrameListener();
		dialog.addWindowListener(wl);
		
        return dialog;
    }

	/**
	 * Paints the given component.
	 *
	 * @param g The graphics context to use.
	 * @param c The component to paint.
	 */
	public void paint(Graphics g, JComponent c) {
		if(c.getBackground() instanceof ColorUIResource) {
			g.setColor(Theme.toolBarColor[Theme.style].getColor());
		}
		else {
			g.setColor(c.getBackground());
		}

		g.fillRect(0, 0, c.getWidth(), c.getHeight());
	}

	/**
	 * Rewritten in 1.3. Now the border is defined
	 * through button margin.
	 */
	protected void setBorderToRollover(Component c) {
		setBorderToNormal(c);
	}

	protected void setBorderToNormal(Component c) {
		if(!(c instanceof AbstractButton)) return;
		if(c instanceof JCheckBox) return;
		if(c instanceof JRadioButton) return;
		
		AbstractButton b = (AbstractButton)c;
		
		b.setRolloverEnabled(true);
		b.putClientProperty(IS_TOOL_BAR_BUTTON_KEY, Boolean.TRUE);

		if(!(b.getBorder() instanceof UIResource) &&
			!(b.getBorder() instanceof TinyToolButtonBorder))
		{
			// user has installed her own border
			return;
		}
		
		b.setBorder(toolButtonBorder);
	}
}