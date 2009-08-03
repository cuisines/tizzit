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
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.plaf.ComponentUI;

import de.muntjak.tinylookandfeel.controlpanel.*;

/**
 * TinySpinnerButtonUI
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class TinySpinnerButtonUI extends TinyButtonUI {
    
    private int orientation;
    
    protected static Dimension winSize = new Dimension(18, 8);
    protected static Dimension xpSize = new Dimension(15, 8);
    
	public static ComponentUI createUI(JComponent c) {
        throw new IllegalStateException("Must not be used this way.");
	}
    
    /**
     * Creates a new Spinner Button. Use either SwingConstants.SOUTH or SwingConstants.NORTH
     * for a SpinnerButton of Type up or a down.
     * @param type
     */
    TinySpinnerButtonUI(int type) {
        orientation = type;   
    }

    public void paint(Graphics g, JComponent c) {
        AbstractButton button = (AbstractButton) c;
        
        if(!button.isEnabled()) {
			g.setColor(Theme.spinnerButtDisabledColor[Theme.style].getColor());
		}
		else if(button.getModel().isPressed()) {
			g.setColor(Theme.spinnerButtPressedColor[Theme.style].getColor());
		}
		else if(button.getModel().isRollover()) {
			g.setColor(Theme.spinnerButtRolloverColor[Theme.style].getColor());
		}
		else {
			g.setColor(Theme.spinnerButtColor[Theme.style].getColor());
		}
		
		switch(Theme.derivedStyle[Theme.style]) {
			case Theme.TINY_STYLE:
				drawTinyButton(g, button);
				break;
			case Theme.W99_STYLE:
				drawWinButton(g, button);
				break;
			case Theme.YQ_STYLE:
				drawXpButton(g, button);
				break;
		}
		
		if(!button.isEnabled()) {
			g.setColor(Theme.spinnerArrowDisabledColor[Theme.style].getColor());
		}
		else {
			g.setColor(Theme.spinnerArrowColor[Theme.style].getColor());
		}
		
		switch(Theme.derivedStyle[Theme.style]) {
			case Theme.TINY_STYLE:
				drawTinyArrow(g, button);
				break;
			case Theme.W99_STYLE:
				drawWinArrow(g, button);
				break;
			case Theme.YQ_STYLE:
				drawXpArrow(g, button);
				break;
		}
    }
    
    private void drawTinyButton(Graphics g, AbstractButton b) {
    	
    }
    
    private void drawWinButton(Graphics g, AbstractButton b) {
    	g.fillRect(0, 0, b.getWidth(), b.getHeight());
    	
		int x2 = b.getSize().width - 3;
		int y2 = (orientation == SwingConstants.NORTH ? b.getSize().height - 1 : b.getSize().height - 3);
		int y1 = (orientation == SwingConstants.NORTH ? 2 : 0);
		
		if(b.getModel().isPressed()) {
			g.setColor(Theme.spinnerDarkColor[Theme.style].getColor());
			g.drawRect(0, y1, x2, y2 - y1);
			return;
		}

		if(!b.isEnabled()) {
			g.setColor(Theme.spinnerLightDisabledColor[Theme.style].getColor());
		}
		else {
			g.setColor(Theme.spinnerLightColor[Theme.style].getColor());
		}
		
		g.drawLine(1, y1 + 1, x2 - 2, y1 + 1);
		g.drawLine(1, y1 + 2, 1, y2 - 2);
		
		if(!b.isEnabled()) {
			g.setColor(Theme.spinnerDarkDisabledColor[Theme.style].getColor());
		}
		else {
			g.setColor(Theme.spinnerDarkColor[Theme.style].getColor());
		}
		
		g.drawLine(1, y2 - 1, x2 - 1, y2 - 1);
		g.drawLine(x2 - 1, y1 + 1, x2 - 1, y2 - 2);
		
		if(!b.isEnabled()) {
			g.setColor(Theme.spinnerBorderDisabledColor[Theme.style].getColor());
		}
		else {
			g.setColor(Theme.spinnerBorderColor[Theme.style].getColor());
		}
		
		g.drawLine(0, y2, x2, y2);
		g.drawLine(x2, y1, x2, y2);
    }
    
    private void drawXpButton(Graphics g, AbstractButton b) {
//    	g.setColor(Color.RED);
//    	g.fillRect(0, 0, b.getWidth(), b.getHeight());
//    	if(true) return;
		int x2 = b.getSize().width - 1;
		int y2 = b.getSize().height - 1;
		int h = b.getSize().height;
		
		int spread1 = Theme.spinnerSpreadLight[Theme.style];
		int spread2 = Theme.spinnerSpreadDark[Theme.style];
		if(!b.isEnabled()) {
			spread1 = Theme.spinnerSpreadLightDisabled[Theme.style];
			spread2 = Theme.spinnerSpreadDarkDisabled[Theme.style];
		}
		
		float spreadStep1 = 10.0f * spread1 / (h - 2);
		float spreadStep2 = 10.0f * spread2 / (h - 2);
		int halfY = h / 2;
		int yd;
		Color c = g.getColor();

		for(int y = 1; y < h - 1; y++) {
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
			
			g.drawLine(1, y, x2, y);
		}
		
		if(!b.isEnabled()) {
			g.setColor(Theme.spinnerBorderDisabledColor[Theme.style].getColor());
		}
		else {
			g.setColor(Theme.spinnerBorderColor[Theme.style].getColor());
		}
		g.drawRect(0, 0, x2, y2);
    }
    
    private void drawTinyArrow(Graphics g, AbstractButton b) {
    	
    }
    
    private void drawWinArrow(Graphics g, AbstractButton b) {
    	int x = 6;
		int y = (b.getSize().height - 2) / 2;
		if(orientation == SwingConstants.NORTH) y += 1;
		else y -= 1;
		
    	if(b.getModel().isPressed()) {
    		y += 1;
    		x += 1;
    	}
    	
    	switch (orientation) {
			case SwingConstants.NORTH: 
				g.drawLine(x + 1, y, x + 1, y);
				g.drawLine(x, y + 1, x + 2, y + 1);
				break; 
			case SwingConstants.SOUTH:
				g.drawLine(x + 1, y + 1, x + 1, y + 1);
				g.drawLine(x, y, x + 2, y);
				break;
		}
    }
    
    private void drawXpArrow(Graphics g, AbstractButton b) {
    	int y = (b.getSize().height - 6) / 2;
    	
    	switch (orientation) {
			case SwingConstants.NORTH:
				y --;
				g.drawLine(7, y + 2, 7, y + 2);
				g.drawLine(6, y + 3, 8, y + 3);
				g.drawLine(5, y + 4, 9, y + 4);
				g.drawLine(4, y + 5, 6, y + 5);
				g.drawLine(8, y + 5, 10, y + 5);
				break; 
			case SwingConstants.SOUTH:
				g.drawLine(4, y + 2, 6, y + 2);
				g.drawLine(8, y + 2, 10, y + 2);
				g.drawLine(5, y + 3, 9, y + 3);
				g.drawLine(6, y + 4, 8, y + 4);
				g.drawLine(7, y + 5, 7, y + 5);
				break;
		}
    }
    
	/**
	 * @see javax.swing.plaf.basic.BasicButtonUI#getPreferredSize(javax.swing.JComponent)
	 */
	public Dimension getPreferredSize(JComponent c) {
		switch(Theme.derivedStyle[Theme.style]) {
			case Theme.TINY_STYLE:
				return xpSize;
			case Theme.W99_STYLE:
				return winSize;
			default:
				return xpSize;
		}
	}
}
