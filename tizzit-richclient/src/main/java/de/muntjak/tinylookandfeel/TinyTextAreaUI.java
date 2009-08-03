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
import javax.swing.plaf.basic.BasicTextAreaUI;
import javax.swing.text.JTextComponent;

import de.muntjak.tinylookandfeel.controlpanel.*;

/**
 * TinyTextAreaUI
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class TinyTextAreaUI extends BasicTextAreaUI {
	/**
	 * Method createUI.
	 * @param mainColor
	 * @return ComponentUI
	 */
	public static ComponentUI createUI(JComponent c) {
		return new TinyTextAreaUI();
	}
}
