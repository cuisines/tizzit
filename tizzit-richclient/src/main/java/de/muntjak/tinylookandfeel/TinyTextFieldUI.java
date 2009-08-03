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
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextUI;
import javax.swing.plaf.metal.MetalTextFieldUI;
import javax.swing.text.JTextComponent;

import de.muntjak.tinylookandfeel.controlpanel.*;

/**
 * TinyTextFieldUI
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class TinyTextFieldUI extends MetalTextFieldUI {

	/**
	 * Method createUI.
	 * @param mainColor
	 * @return ComponentUI
	 */
	public static ComponentUI createUI(JComponent c) {
		return new TinyTextFieldUI();
	}

	protected void paintBackground(Graphics g) {
		JTextComponent editor = getComponent();
		// We will only be here if editor is opaque, so we don't have to test

		if(editor.isEnabled()) {
			if(editor.isEditable()) {
				g.setColor(editor.getBackground());
			}
			else {
				// not editable
				if(editor.getBackground().equals(Theme.textBgColor[Theme.style].getColor())) {
					// set default panel background
					g.setColor(Theme.backColor[Theme.style].getColor());
				}
				else {
					// color changed by user - set textfield background
					g.setColor(editor.getBackground());
				}
			}

			g.fillRect(0, 0, editor.getWidth(), editor.getHeight());
		}
		else {
			if(editor.getBackground().equals(Theme.textBgColor[Theme.style].getColor())) {
				g.setColor(Theme.textDisabledBgColor[Theme.style].getColor());
			}
			else {
				// color changed by user - set textfield background
				g.setColor(editor.getBackground());
			}
			
			g.fillRect(0, 0, editor.getWidth(), editor.getHeight());
			
			if(Theme.style != Theme.YQ_STYLE) return;
			
			g.setColor(Theme.backColor[Theme.style].getColor());
			g.drawRect(1, 1, editor.getWidth() - 3, editor.getHeight() - 3);
			g.drawRect(2, 2, editor.getWidth() - 5, editor.getHeight() - 5);
		}
	}
}