/*
 * Created on 12.05.2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package de.muntjak.tinylookandfeel;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPasswordFieldUI;

import de.muntjak.tinylookandfeel.controlpanel.*;

/**
 * TinyPasswordFieldUI
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class TinyPasswordFieldUI extends BasicPasswordFieldUI {
	
	JComponent editor;

	public static ComponentUI createUI(JComponent c) {
        return new TinyPasswordFieldUI();
    }
    
    public void installUI(JComponent c) {
        super.installUI(c);
        editor = c;
    }
    
    protected void paintBackground(Graphics g) {
    	if(editor.isEnabled()) {
    		g.setColor(editor.getBackground());
    	}
    	else {
    		g.setColor(Theme.textDisabledBgColor[Theme.style].getColor());
    	}
        
        g.fillRect(0, 0, editor.getWidth(), editor.getHeight());
    }
}
