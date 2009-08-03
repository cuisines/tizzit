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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.metal.MetalComboBoxEditor;

import de.muntjak.tinylookandfeel.controlpanel.*;

/**
 * TinyComboBoxEditor
 * 
 * @version 1.1
 * @author Hans Bickel
 */
public class TinyComboBoxEditor extends MetalComboBoxEditor {

	public TinyComboBoxEditor() {
		super();
		
		editor = new JTextField("", 9) {
			// workaround for 4530952
			public void setText(String s) {
				if (getText().equals(s)) {
					return;
				}
				
				super.setText(s);
			}
			
			// Note: The following code was introduced with Java 1.5 in
			// class javax.swing.plaf.metal.MetalComboBoxEditor.
			// With TinyLaF this isn't a good idea, so we create our
			// own editor.
			
			// The preferred and minimum sizes are overriden and padded by
			// 4 to keep the size as it previously was.  Refer to bugs
			// 4775789 and 4517214 for details.
//			public Dimension getPreferredSize() {
//				Dimension pref = super.getPreferredSize();
//				pref.height += 4;
//				return pref;
//			}
//			public Dimension getMinimumSize() {
//				Dimension min = super.getMinimumSize();
//				min.height += 4;
//				return min;
//			}
		};
		
		editor.setBorder(new EditorBorder());
	}
	
	class EditorBorder extends AbstractBorder {
		
		/**
		 * @see javax.swing.border.Border#getBorderInsets(java.awt.Component)
		 */
		public Insets getBorderInsets(Component c) {
			// Note: I just adjusted insets until
			// editable and non-editable combo boxes look equal
			return new Insets(
				1,
				Theme.comboInsets[Theme.style].left + 1,
				1,
				0);
		}
		
		/**
		 * @see javax.swing.border.Border#paintBorder(java.awt.Component, java.awt.Graphics, int, int, int, int)
		 */
		public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
			JComponent combo = (JComponent)editor.getParent();

			if(combo.getBorder() == null) return;
			
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
	}
	
	private void drawTinyBorder(Component c, Graphics g, int x, int y, int w, int h) {
		if(!c.isEnabled()) {
			g.setColor(Theme.textBorderDisabledColor[Theme.style].getColor());
		}
		else {
			g.setColor(Theme.textBorderColor[Theme.style].getColor());
		}		
		g.drawRect(x + 1, y + 1, w - 3, h - 3);
		
		if(!c.isEnabled()) {
			g.setColor(Theme.textBorderDarkDisabledColor[Theme.style].getColor());
		}
		else {
			g.setColor(Theme.textBorderDarkColor[Theme.style].getColor());
		}
		g.drawLine(x, y, x + w - 2, y);
		g.drawLine(x, y + 1, x, y + h - 2);
		
		if(!c.isEnabled()) {
			g.setColor(Theme.textBorderLightDisabledColor[Theme.style].getColor());
		}
		else {
			g.setColor(Theme.textBorderLightColor[Theme.style].getColor());
		}
		g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
		g.drawLine(x + w - 1, y, x + w - 1, y + h - 2);
	}
	
	private void drawWinBorder(Component c, Graphics g, int x, int y, int w, int h) {
		if(!c.isEnabled()) {
			g.setColor(Theme.textBorderDisabledColor[Theme.style].getColor());
		}
		else {
			g.setColor(Theme.textBorderColor[Theme.style].getColor());
		}		
		g.drawLine(x + 1, y + 1, x + w - 1, y + 1);
		g.drawLine(x + 1, y + 2, x + 1, y + h - 3);
		
		if(!c.isEnabled()) {
			g.setColor(Theme.textBorderDarkDisabledColor[Theme.style].getColor());
		}
		else {
			g.setColor(Theme.textBorderDarkColor[Theme.style].getColor());
		}
		g.drawLine(x, y, x + w - 1, y);
		g.drawLine(x, y + 1, x, y + h - 2);
		
		g.setColor(Theme.backColor[Theme.style].getColor());
		g.drawLine(x + 1, y + h - 2, x + w - 1, y + h - 2);
		
		if(!c.isEnabled()) {
			g.setColor(Theme.textBorderLightDisabledColor[Theme.style].getColor());
		}
		else {
			g.setColor(Theme.textBorderLightColor[Theme.style].getColor());
		}
		g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
	}
	
	private void drawXpBorder(Component c, Graphics g, int x, int y, int w, int h) {
		// paint border background - next parent is combo box
		Color bg = c.getParent().getParent().getBackground();
		g.setColor(bg);
		g.drawLine(x, y, x + w - 1, y);					// top
		g.drawLine(x, y, x, y + h - 1);					// left
		g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);	// bottom
		
		if(!c.isEnabled()) {
			DrawRoutines.drawEditableComboBorder(
				g, Theme.comboBorderDisabledColor[Theme.style].getColor(), 0, 0, w, h);
		}
		else {
			DrawRoutines.drawEditableComboBorder(
				g, Theme.comboBorderColor[Theme.style].getColor(), 0, 0, w, h);
		}
	}
	
	
	/**
	 * A subclass of BasicComboBoxEditor that implements UIResource.
	 * BasicComboBoxEditor doesn't implement UIResource
	 * directly so that applications can safely override the
	 * cellRenderer property with BasicListCellRenderer subclasses.
	 * <p>
	 * <strong>Warning:</strong>
	 * Serialized objects of this class will not be compatible with
	 * future Swing releases. The current serialization support is
	 * appropriate for short term storage or RMI between applications running
	 * the same version of Swing.  As of 1.4, support for long term storage
	 * of all JavaBeans<sup><font size="-2">TM</font></sup>
	 * has been added to the <code>java.beans</code> package.
	 * Please see {@link java.beans.XMLEncoder}.
	 */
	public static class UIResource extends TinyComboBoxEditor
	implements javax.swing.plaf.UIResource
	{
	}
}


