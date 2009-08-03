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
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.plaf.ComponentUI;

import de.muntjak.tinylookandfeel.borders.TinyFrameBorder;
import de.muntjak.tinylookandfeel.borders.TinyInternalFrameBorder;
import de.muntjak.tinylookandfeel.controlpanel.*;

/**
 * TinyWindowButtonUI
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class TinyWindowButtonUI extends TinyButtonUI {
    
    private int type;
    /** An icon to indicate that this button closes the windows */
    public final static int CLOSE = 0;
    /** An icon to indicate that this button maximizes the windows */
    public final static int MAXIMIZE = 1;
    /** An icon to indicate that this button minmizes / iconfies the windows */
    public final static int MINIMIZE = 2;

	public static ComponentUI createUI(JComponent c) {
        throw new IllegalStateException("Must not be used this way.");
	}
    
    TinyWindowButtonUI(int type) {
        this.type = type;   
    }
    
    public void installDefaults(AbstractButton button) {
		super.installDefaults(button);
		button.setBorder(null);
		button.setFocusable(false);
	}

    protected void paintFocus(Graphics g, AbstractButton b,
    	Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {}
	
    public void paint(Graphics g, JComponent c) { 	
        AbstractButton button = (AbstractButton) c;

        boolean frameSelected = false;
        boolean frameMaximized = false;
        
        if(c.getParent() instanceof TinyInternalFrameTitlePane) {
        	frameSelected = ((TinyInternalFrameTitlePane)c.getParent()).isFrameSelected();
        	frameMaximized = ((TinyInternalFrameTitlePane)c.getParent()).isFrameMaximized();
        }
        else if(c.getParent() instanceof TinyTitlePane) {
        	frameSelected = ((TinyTitlePane)c.getParent()).isSelected();
        	frameMaximized = ((TinyTitlePane)c.getParent()).isFrameMaximized();
        }

        int w = button.getWidth();
		int h = button.getHeight();
			
		// content area
		Color col = null;
		if(!frameSelected) {
			if(button.isEnabled()) {
				if(button.getModel().isPressed()) {
					if(type == CLOSE) {
						col = Theme.frameButtClosePressedColor[Theme.style].getColor();
					}
					else {
						col = Theme.frameButtPressedColor[Theme.style].getColor();
					}
				}
				else {
					if(type == CLOSE) {
						col = Theme.frameButtCloseColor[Theme.style].getColor();
					}
					else {
						col = Theme.frameButtColor[Theme.style].getColor();
					}
				}
			}
			else {
				if(type == CLOSE) {
					col = Theme.frameButtCloseDisabledColor[Theme.style].getColor();
				}
				else {
					col = Theme.frameButtDisabledColor[Theme.style].getColor();
				}
			}
		}
		else if(button.getModel().isPressed()) {
			if(button.getModel().isRollover()) {
				if(type == CLOSE) {
					col = Theme.frameButtClosePressedColor[Theme.style].getColor();
				}
				else {
					col = Theme.frameButtPressedColor[Theme.style].getColor();
				}
			}
			else {
				if(type == CLOSE) {
					col = Theme.frameButtCloseColor[Theme.style].getColor();
				}
				else {
					col = Theme.frameButtColor[Theme.style].getColor();
				}
			}
		}
		else if(button.getModel().isRollover()) {
			if(type == CLOSE) {
				col = Theme.frameButtCloseRolloverColor[Theme.style].getColor();
			}
			else {
				col = Theme.frameButtRolloverColor[Theme.style].getColor();
			}
		}
		else {
			if(type == CLOSE) {
				col = Theme.frameButtCloseColor[Theme.style].getColor();
			}
			else {
				col = Theme.frameButtColor[Theme.style].getColor();
			}
		}
		g.setColor(col);
		
		switch(Theme.derivedStyle[Theme.style]) {
			case Theme.TINY_STYLE:
				drawTinyButton(g, button, col, w, h, frameSelected);
				break;
			case Theme.W99_STYLE:
				drawWinButton(g, button, col, w, h, frameSelected);
				break;
			case Theme.YQ_STYLE:
				drawXpButton(g, button, col, w, h, frameSelected);
				break;
		}

		// draw symbol
		if(!button.isEnabled()) {
			if(type == CLOSE) {
				g.setColor(Theme.frameSymbolCloseDisabledColor[Theme.style].getColor());
			}
			else {
				g.setColor(Theme.frameSymbolDisabledColor[Theme.style].getColor());
			}
		}
		else {
			if(type == CLOSE) {
				g.setColor(Theme.frameSymbolCloseColor[Theme.style].getColor());
			}
			else {
				g.setColor(Theme.frameSymbolColor[Theme.style].getColor());
			}
		}
		
		switch(Theme.derivedStyle[Theme.style]) {
			case Theme.TINY_STYLE:
				drawTinySymbol(g, button, col, w, h, frameSelected, frameMaximized);
				break;
			case Theme.W99_STYLE:
				drawWinSymbol(g, button, col, w, h, frameSelected, frameMaximized);
				break;
			case Theme.YQ_STYLE:
				drawXpSymbol(g, button, col, w, h, frameSelected, frameMaximized);
				break;
		}
    }
    
    private void drawTinyButton(
    	Graphics g, AbstractButton button, Color c, int w, int h, boolean frameSelected)
    {
    	g.fillRect(1, 1, w - 2, h - 2);
    }
    
    private void drawWinButton(
    	Graphics g, AbstractButton button, Color c, int w, int h, boolean frameSelected)
    {
    	g.fillRect(1, 1, w - 2, h - 2);
    	
    	// Border
    	if((button.getModel().isPressed() && button.getModel().isRollover()) ||
    		(button.getModel().isPressed() && !frameSelected))
    	{
    		g.setColor(Theme.frameButtBorderColor[Theme.style].getColor());
    	}
    	else {
    		g.setColor(Theme.frameButtLightColor[Theme.style].getColor());
    	}
    	g.drawLine(0, 0, w - 2, 0);
    	g.drawLine(0, 1, 0, h - 2);
    	
    	g.setColor(Theme.frameButtDarkColor[Theme.style].getColor());
    	if((button.getModel().isPressed() && button.getModel().isRollover()) ||
    		(button.getModel().isPressed() && !frameSelected))
    	{
    		g.drawLine(1, 1, w - 2, 1);
    		g.drawLine(1, 1, 1, h - 3);
    	}
    	else {
    		g.drawLine(1, h - 2, w - 2, h - 2);
    		g.drawLine(w - 2, 1, w - 2, h - 3);
    	}
    	
    	if((button.getModel().isPressed() && button.getModel().isRollover()) ||
    		(button.getModel().isPressed() && !frameSelected))
    	{
    		g.setColor(Theme.frameButtLightColor[Theme.style].getColor());
    	}
    	else {
    		g.setColor(Theme.frameButtBorderColor[Theme.style].getColor());
    	}
    	g.drawLine(0, h - 1, w - 1, h - 1);
    	g.drawLine(w - 1, 0, w - 1, h - 2);
    }
    
    // Button for internal frames and dialogs
    private void drawXpButton(Graphics g, AbstractButton button,
    	Color c, int w, int h, boolean frameSelected)
    {
    	if(button.getClientProperty("externalFrameButton") == Boolean.TRUE) {
    		drawXpLargeButton(g, button, c, w, h, frameSelected);
    		return;
    	}

    	g.fillRect(1, 1, w - 2, h - 2);
    	
    	boolean isPalette = false;
    	
    	if(button.getParent() instanceof TinyInternalFrameTitlePane) {
			if(((TinyInternalFrameTitlePane)button.getParent()).isPalette()) {
				isPalette = true;
			}
		}
			
		if(frameSelected) {
			g.setColor(TinyInternalFrameBorder.frameUpperColor);
			g.drawLine(0, 0, w - 1, 0);
			g.drawLine(0, 1, 0, 1);				// ol
			g.drawLine(w - 1, 1, w - 1, 1);		// or
			g.setColor(TinyInternalFrameBorder.frameLowerColor);
			g.drawLine(0, h - 1, w - 1, h - 1);
			g.drawLine(0, h - 2, 0, h - 2);				// ul
			g.drawLine(w - 1, h - 2, w - 1, h - 2);		// ur
		}
		else {
			g.setColor(TinyInternalFrameBorder.disabledUpperColor);
			g.drawLine(0, 0, w - 1, 0);
			g.drawLine(0, 1, 0, 1);				// ol
			g.drawLine(w - 1, 1, w - 1, 1);		// or
			g.setColor(TinyInternalFrameBorder.disabledLowerColor);
			g.drawLine(0, h - 1, w - 1, h - 1);
			g.drawLine(0, h - 2, 0, h - 2);				// ul
			g.drawLine(w - 1, h - 2, w - 1, h - 2);		// ur
		}   	

		Color col = null;
    	if(!button.isEnabled()) {
    		if(type == CLOSE) {
    			col = Theme.frameButtCloseBorderDisabledColor[Theme.style].getColor();
    		}
    		else {
    			col = Theme.frameButtBorderDisabledColor[Theme.style].getColor();
    		}
		}
		else {
			if(type == CLOSE) {
    			col = Theme.frameButtCloseBorderColor[Theme.style].getColor();
    		}
    		else {
    			col = Theme.frameButtBorderColor[Theme.style].getColor();
    		}
		}

		DrawRoutines.drawRoundedBorder(g, col, 0, 0, w, h);
		
		if(!button.isEnabled()) {
    		if(type == CLOSE) {
    			col = Theme.frameButtCloseLightDisabledColor[Theme.style].getColor();
    		}
    		else {
    			col = Theme.frameButtLightDisabledColor[Theme.style].getColor();
    		}
		}
		else {
			if(type == CLOSE) {
    			col = Theme.frameButtCloseLightColor[Theme.style].getColor();
    		}
    		else {
    			col = Theme.frameButtLightColor[Theme.style].getColor();
    		}
		}

		g.setColor(col);
		g.drawLine(2, 1, w - 3, 1);
		g.drawLine(1, 2, 1, h - 3);
		
		if(!button.isEnabled()) {
    		if(type == CLOSE) {
    			col = Theme.frameButtCloseDarkDisabledColor[Theme.style].getColor();
    		}
    		else {
    			col = Theme.frameButtDarkDisabledColor[Theme.style].getColor();
    		}
		}
		else {
			if(type == CLOSE) {
    			col = Theme.frameButtCloseDarkColor[Theme.style].getColor();
    		}
    		else {
    			col = Theme.frameButtDarkColor[Theme.style].getColor();
    		}
		}
		g.setColor(col);
		g.drawLine(w - 2, 2, w - 2, h - 3);
		g.drawLine(2, h - 2, w - 3, h - 2);
    }
    
    private void drawXpLargeButton(Graphics g, AbstractButton b,
    	Color c, int w, int h, boolean frameSelected)
    {
    	g.setColor(TinyFrameBorder.buttonUpperColor);
    	g.drawLine(0, 0, w - 1, 0);
    	
    	g.setColor(TinyFrameBorder.buttonLowerColor);
    	g.drawLine(0, h - 1, w - 1, h - 1);

		int spread1 = Theme.frameButtSpreadLight[Theme.style];
		int spread2 = Theme.frameButtSpreadDark[Theme.style];

		if(!b.isEnabled()) {
			if(type == CLOSE) {
				spread1 = Theme.frameButtCloseSpreadLightDisabled[Theme.style];
				spread2 = Theme.frameButtCloseSpreadDarkDisabled[Theme.style];
			}
			else {
				spread1 = Theme.frameButtSpreadLightDisabled[Theme.style];
				spread2 = Theme.frameButtSpreadDarkDisabled[Theme.style];
			}
		}
		else if(type == CLOSE) {
			spread1 = Theme.frameButtCloseSpreadLight[Theme.style];
			spread2 = Theme.frameButtCloseSpreadDark[Theme.style];
		}
		
		float spreadStep1 = 10.0f * spread1 / (h - 3);
		float spreadStep2 = 10.0f * spread2 / (h - 3);
		int halfY = h / 2;
		int yd;

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
			
			g.drawLine(2, y, w - 3, y);
			
			if(y == 1 && b.isEnabled()) {
				// left vertical line
				g.drawLine(1, 2, 1, h - 3);	
			}
			else if(y == h - 2) {
				// right vertical line
				g.drawLine(w - 2, 2, w - 2, h - 3);
			}
		}

		if(!b.isEnabled()) {
			if(type == CLOSE) {
				DrawRoutines.drawRoundedBorder(g,
					Theme.frameButtCloseBorderDisabledColor[Theme.style].getColor(), 0, 0, w, h);
			}
			else {
				DrawRoutines.drawRoundedBorder(g,
					Theme.frameButtBorderDisabledColor[Theme.style].getColor(), 0, 0, w, h);
			}
		}
		else {
			if(type == CLOSE) {
				DrawRoutines.drawRoundedBorder(g,
					Theme.frameButtCloseBorderColor[Theme.style].getColor(), 0, 0, w, h);
			}
			else {
				DrawRoutines.drawRoundedBorder(g,
					Theme.frameButtBorderColor[Theme.style].getColor(), 0, 0, w, h);
			}
		}
    }
    
    private void drawTinySymbol(Graphics g, AbstractButton button, Color c,
    	int w, int h, boolean frameSelected, boolean frameMaximized)
    {
    	
    }
    
    private void drawWinSymbol(Graphics g, AbstractButton button, Color c,
    	int w, int h, boolean frameSelected, boolean frameMaximized)
    {
    	int x = 3; int y = 2;

    	if((button.getModel().isPressed() && button.getModel().isRollover()) ||
    		(button.getModel().isPressed() && !frameSelected))
    	{
    		x = 4; y = 3;
    	}

    	switch(type) {
			case CLOSE:
				g.drawLine(x + 1, y + 1, x + 2, y + 1);
				g.drawLine(x + 7, y + 1, x + 8, y + 1);
				g.drawLine(x + 2, y + 2, x + 3, y + 2);
				g.drawLine(x + 6, y + 2, x + 7, y + 2);
				g.drawLine(x + 3, y + 3, x + 6, y + 3);
				g.drawLine(x + 4, y + 4, x + 5, y + 4);
				g.drawLine(x + 3, y + 5, x + 6, y + 5);
				g.drawLine(x + 2, y + 6, x + 3, y + 6);
				g.drawLine(x + 6, y + 6, x + 7, y + 6);
				g.drawLine(x + 1, y + 7, x + 2, y + 7);
				g.drawLine(x + 7, y + 7, x + 8, y + 7);
				break;
			case MAXIMIZE:
				if(frameMaximized) {
					g.fillRect(x + 2, y, 6, 2);
					g.drawLine(x + 2, y + 2, x + 2, y + 2);
					g.drawLine(x + 6, y + 5, x + 7, y + 5);
					g.drawLine(x + 7, y + 2, x + 7, y + 4);
					
					g.drawLine(x, y + 3, x + 5, y + 3);
					g.drawRect(x, y + 4, 5, 4);
				}
				else {
					g.drawRect(x, y, 8, 8);
					g.drawLine(x + 1, y + 1, x + 7, y + 1);
				}
				break;
			case MINIMIZE:
				g.drawRect(x + 1, y + 7, 6, 1);
				break;
		}
    }
    
    private void drawXpSymbol(Graphics g, AbstractButton button, Color c,
    	int w, int h, boolean frameSelected, boolean frameMaximized)
    {
    	if(button.getClientProperty("externalFrameButton") == Boolean.TRUE) {
    		drawXpLargeSymbol(g, button, c, w, h, frameSelected, frameMaximized);
    		return;
    	}
    	
    	if(button.getParent() instanceof TinyInternalFrameTitlePane) {
			if(((TinyInternalFrameTitlePane)button.getParent()).isPalette()) {
				drawXpSmallSymbol(g, button, c, w, h, frameSelected, frameMaximized);
				return;
			}
		}
    	
    	if(!frameSelected) {
    		if(button.isEnabled() && !button.getModel().isPressed()) {
    			if(type == CLOSE) {
    				g.setColor(Theme.frameSymbolCloseColor[Theme.style].getColor());
    			}
    			else {
    				g.setColor(Theme.frameSymbolColor[Theme.style].getColor());
    			}
    		}
    		else {
    			if(type == CLOSE) {
    				if(button.getModel().isPressed()) {
    					g.setColor(Theme.frameSymbolClosePressedColor[Theme.style].getColor());
    				}
    				else {
						g.setColor(Theme.frameSymbolCloseDisabledColor[Theme.style].getColor());
    				}
				}
				else {
    				if(button.getModel().isPressed()) {
    					g.setColor(Theme.frameSymbolPressedColor[Theme.style].getColor());
    				}
    				else {
						g.setColor(Theme.frameSymbolDisabledColor[Theme.style].getColor());
    				}
				}
    		}
    	}
    	else {
    		if(button.getModel().isPressed() && button.getModel().isRollover()) {
    			if(type == CLOSE) {
					g.setColor(Theme.frameSymbolClosePressedColor[Theme.style].getColor());
				}
				else {
    				g.setColor(Theme.frameSymbolPressedColor[Theme.style].getColor());
				}
    		}
    		else {
    			if(type == CLOSE) {
    				g.setColor(Theme.frameSymbolCloseColor[Theme.style].getColor());
    			}
    			else {
    				g.setColor(Theme.frameSymbolColor[Theme.style].getColor());
    			}
    		}
    	}
    	
    	int x = 0; int y = 0;
    	
    	switch(type) {
			case CLOSE:
				g.drawLine(x + 4, y + 3, x + 4, y + 3);
				g.drawLine(x + 12, y + 3, x + 12, y + 3);
				g.drawLine(x + 3, y + 4, x + 5, y + 4);
				g.drawLine(x + 11, y + 4, x + 13, y + 4);
				g.drawLine(x + 4, y + 5, x + 6, y + 5);
				g.drawLine(x + 10, y + 5, x + 12, y + 5);
				g.drawLine(x + 5, y + 6, x + 7, y + 6);
				g.drawLine(x + 9, y + 6, x + 11, y + 6);
				g.drawLine(x + 6, y + 7, x + 10, y + 7);
				g.drawLine(x + 7, y + 8, x + 9, y + 8);
				g.drawLine(x + 4, y + 13, x + 4, y + 13);
				g.drawLine(x + 12, y + 13, x + 12, y + 13);
				g.drawLine(x + 3, y + 12, x + 5, y + 12);
				g.drawLine(x + 11, y + 12, x + 13, y + 12);
				g.drawLine(x + 4, y + 11, x + 6, y + 11);
				g.drawLine(x + 10, y + 11, x + 12, y + 11);
				g.drawLine(x + 5, y + 10, x + 7, y + 10);
				g.drawLine(x + 9, y + 10, x + 11, y + 10);
				g.drawLine(x + 6, y + 9, x + 10, y + 9);
				break;
			case MAXIMIZE:
				if(frameMaximized) {
					g.fillRect(x + 6, y + 3, 8, 2);
					g.drawLine(x + 6, y + 5, x + 6, y + 6);
					g.drawLine(x + 11, y + 9, x + 13, y + 9);
					g.drawLine(x + 13, y + 5, x + 13, y + 8);
					
					g.drawLine(x + 3, y + 7, x + 10, y + 7);
					g.drawRect(x + 3, y + 8, 7, 5);
				}
				else {
					g.fillRect(x + 3, y + 3, 11, 2);
					g.drawRect(x + 3, y + 5, 10, 8);
				}
				break;
			case MINIMIZE:
				g.fillRect(x + 3, y + 11, 7, 3);
				break;
		}
    }
    
    private void drawXpSmallSymbol(Graphics g, AbstractButton button, Color c,
    	int w, int h, boolean frameSelected, boolean frameMaximized)
	{
		if(!frameSelected) {
    		if(button.isEnabled() && !button.getModel().isPressed()) {
    			if(type == CLOSE) {
    				g.setColor(Theme.frameSymbolCloseColor[Theme.style].getColor());
    			}
    			else {
    				g.setColor(Theme.frameSymbolColor[Theme.style].getColor());
    			}
    		}
    		else {
    			if(type == CLOSE) {
    				if(button.getModel().isPressed()) {
    					g.setColor(Theme.frameSymbolClosePressedColor[Theme.style].getColor());
    				}
    				else {
						g.setColor(Theme.frameSymbolCloseDisabledColor[Theme.style].getColor());
    				}
				}
				else {
    				if(button.getModel().isPressed()) {
    					g.setColor(Theme.frameSymbolPressedColor[Theme.style].getColor());
    				}
    				else {
						g.setColor(Theme.frameSymbolDisabledColor[Theme.style].getColor());
    				}
				}
    		}
    	}
    	else {
    		if(button.getModel().isPressed() && button.getModel().isRollover()) {
    			if(type == CLOSE) {
					g.setColor(Theme.frameSymbolClosePressedColor[Theme.style].getColor());
				}
				else {
    				g.setColor(Theme.frameSymbolPressedColor[Theme.style].getColor());
				}
    		}
    		else {
    			if(type == CLOSE) {
    				g.setColor(Theme.frameSymbolCloseColor[Theme.style].getColor());
    			}
    			else {
    				g.setColor(Theme.frameSymbolColor[Theme.style].getColor());
    			}
    		}
    	}
    	
    	int x = 0; int y = 0;
    	
    	switch(type) {
			case CLOSE:
				g.drawLine(x + 3, y + 2, x + 3, y + 2);
				g.drawLine(x + 9, y + 2, x + 9, y + 2);
				g.drawLine(x + 2, y + 3, x + 4, y + 3);
				g.drawLine(x + 8, y + 3, x + 10, y + 3);
				g.drawLine(x + 3, y + 4, x + 5, y + 4);
				g.drawLine(x + 7, y + 4, x + 9, y + 4);
				g.drawLine(x + 4, y + 5, x + 8, y + 5);
				g.drawLine(x + 5, y + 6, x + 7, y + 6);			
				g.drawLine(x + 4, y + 7, x + 8, y + 7);
				g.drawLine(x + 3, y + 8, x + 5, y + 8);
				g.drawLine(x + 7, y + 8, x + 9, y + 8);
				g.drawLine(x + 2, y + 9, x + 4, y + 9);
				g.drawLine(x + 8, y + 9, x + 10, y + 9);
				g.drawLine(x + 3, y + 10, x + 3, y + 10);
				g.drawLine(x + 9, y + 10, x + 9, y + 10);
				break;
			case MAXIMIZE:
				if(frameMaximized) {
					g.drawRect(x + 2, y + 6, 6, 4);
					g.drawLine(x + 2, y + 5, x + 8, y + 5);
					
					g.fillRect(x + 4, y + 2, x + 7, y + 2);
					g.drawLine(x + 4, y + 4, x + 4, y + 4);					
					g.drawLine(x + 9, y + 7, x + 9, y + 7);
					g.drawLine(x + 10, y + 4, x + 10, y + 7);
				}
				else {
					g.drawLine(x + 2, y + 2, x + 10, y + 2);
					g.drawRect(x + 2, y + 3, 8, 7);
				}
				break;
			case MINIMIZE:
				g.fillRect(x + 2, y + 8, 6, 3);
				break;
		}
	}
    
    private void drawXpLargeSymbol(Graphics g, AbstractButton b, Color c,
    	int w, int h, boolean frameSelected, boolean frameMaximized)
	{
		if(!frameSelected) {
    		if(b.isEnabled() && !b.getModel().isPressed()) {
    			if(type == CLOSE) {
    				g.setColor(Theme.frameSymbolCloseColor[Theme.style].getColor());
    			}
    			else {
    				g.setColor(Theme.frameSymbolColor[Theme.style].getColor());
    			}
    		}
    		else {
    			if(type == CLOSE) {
    				if(b.getModel().isPressed()) {
    					g.setColor(Theme.frameSymbolClosePressedColor[Theme.style].getColor());
    				}
    				else {
						g.setColor(Theme.frameSymbolCloseDisabledColor[Theme.style].getColor());
    				}
				}
				else {
    				if(b.getModel().isPressed()) {
    					g.setColor(Theme.frameSymbolPressedColor[Theme.style].getColor());
    				}
    				else {
						g.setColor(Theme.frameSymbolDisabledColor[Theme.style].getColor());
    				}
				}
    		}
    	}
    	else {
    		if(b.getModel().isPressed() && b.getModel().isRollover()) {
    			if(type == CLOSE) {
					g.setColor(Theme.frameSymbolClosePressedColor[Theme.style].getColor());
				}
				else {
    				g.setColor(Theme.frameSymbolPressedColor[Theme.style].getColor());
				}
    		}
    		else {
    			if(type == CLOSE) {
    				g.setColor(Theme.frameSymbolCloseColor[Theme.style].getColor());
    			}
    			else {
    				g.setColor(Theme.frameSymbolColor[Theme.style].getColor());
    			}
    		}
    	}
    	
    	int x = 0; int y = 0;
    	
    	switch(type) {
			case CLOSE:
				g.drawLine(x + 5, y + 5, x + 6, y + 5);
				g.drawLine(x + 14, y + 5, x + 15, y + 5);
				g.drawLine(x + 5, y + 6, x + 7, y + 6);
				g.drawLine(x + 13, y + 6, x + 15, y + 6);
				g.drawLine(x + 6, y + 7, x + 8, y + 7);
				g.drawLine(x + 12, y + 7, x + 14, y + 7);
				g.drawLine(x + 7, y + 8, x + 9, y + 8);
				g.drawLine(x + 11, y + 8, x + 13, y + 8);
				g.drawLine(x + 8, y + 9, x + 12, y + 9);
				g.drawLine(x + 9, y + 10, x + 11, y + 10);
				g.drawLine(x + 5, y + 15, x + 6, y + 15);
				g.drawLine(x + 14, y + 15, x + 15, y + 15);
				g.drawLine(x + 5, y + 14, x + 7, y + 14);
				g.drawLine(x + 13, y + 14, x + 15, y + 14);
				g.drawLine(x + 6, y + 13, x + 8, y + 13);
				g.drawLine(x + 12, y + 13, x + 14, y + 13);
				g.drawLine(x + 7, y + 12, x + 9, y + 12);
				g.drawLine(x + 11, y + 12, x + 13, y + 12);
				g.drawLine(x + 8, y + 11, x + 12, y + 11);
				break;
			case MAXIMIZE:
				if(frameMaximized) {
					g.fillRect(x + 8, y + 4, 8, 2);
					g.fillRect(x + 4, y + 9, 8, 2);
				}
				else {
					g.fillRect(x + 5, y + 6, 10, 2);
				}
				break;
			case MINIMIZE:
				g.fillRect(x + 5, y + 14, 6, 2);
				break;
		}
		
		Color col = null;
		if(type == CLOSE) {
			col = Theme.frameSymbolCloseLightColor[Theme.style].getColor();
		}
		else {
			col = Theme.frameSymbolLightColor[Theme.style].getColor();
		}
		
		if(!frameSelected) {
			col = ColorRoutines.getAverage(col, c);
		}
		g.setColor(col);
		
		switch(type) {
			case CLOSE:
				// nothing
				break;
			case MAXIMIZE:
				if(frameMaximized) {
					g.drawLine(x + 7, y + 4, x + 7, y + 7);
					g.drawLine(x + 9, y + 6, x + 14, y + 6);
					g.drawLine(x + 15, y + 6, x + 15, y + 10);
					g.drawLine(x + 13, y + 12, x + 16, y + 12);
					
					g.drawLine(x + 3, y + 9, x + 3, y + 16);
					g.drawLine(x + 3, y + 17, x + 12, y + 17);
					g.drawLine(x + 5, y + 11, x + 10, y + 11);
					g.drawLine(x + 11, y + 11, x + 11, y + 15);
				}
				else {
					g.drawLine(x + 4, y + 6, x + 4, y + 15);
					g.drawLine(x + 4, y + 16, x + 15, y + 16);
					g.drawLine(x + 6, y + 8, x + 13, y + 8);
					g.drawLine(x + 14, y + 8, x + 14, y + 14);
				}
				break;
			case MINIMIZE:
				g.drawLine(x + 4, y + 13, x + 4, y + 16);
				g.drawLine(x + 5, y + 16, x + 11, y + 16);
				break;
		}
		
		if(type == CLOSE) {
			col = Theme.frameSymbolCloseDarkColor[Theme.style].getColor();
		}
		else {
			col = Theme.frameSymbolDarkColor[Theme.style].getColor();
		}
		
		if(!frameSelected) {
			col = ColorRoutines.getAverage(col, c);
		}
		g.setColor(col);
		
		switch(type) {
			case CLOSE:
				g.drawLine(x + 5, y + 4, x + 6, y + 4);
				g.drawLine(x + 14, y + 4, x + 15, y + 4);
				g.drawLine(x + 7, y + 5, x + 7, y + 5);
				g.drawLine(x + 13, y + 5, x + 13, y + 5);
				g.drawLine(x + 8, y + 6, x + 8, y + 6);
				g.drawLine(x + 12, y + 6, x + 12, y + 6);
				g.drawLine(x + 9, y + 7, x + 9, y + 7);
				g.drawLine(x + 11, y + 7, x + 11, y + 7);
				g.drawLine(x + 10, y + 8, x + 10, y + 8);
				g.drawLine(x + 8, y + 10, x + 8, y + 10);
				g.drawLine(x + 12, y + 10, x + 12, y + 10);
				g.drawLine(x + 7, y + 11, x + 7, y + 11);
				g.drawLine(x + 13, y + 11, x + 13, y + 11);
				g.drawLine(x + 6, y + 12, x + 6, y + 12);
				g.drawLine(x + 14, y + 12, x + 14, y + 12);
				g.drawLine(x + 5, y + 13, x + 5, y + 13);
				g.drawLine(x + 15, y + 13, x + 15, y + 13);
				g.drawLine(x + 4, y + 14, x + 4, y + 14);
				g.drawLine(x + 16, y + 14, x + 16, y + 14);
				break;
			case MAXIMIZE:
				if(frameMaximized) {
					g.drawLine(x + 8, y + 3, x + 15, y + 3);
					g.drawLine(x + 16, y + 3, x + 16, y + 11);
					g.drawLine(x + 13, y + 11, x + 15, y + 11);
					g.drawLine(x + 8, y + 6, x + 8, y + 7);
					
					g.drawLine(x + 4, y + 8, x + 11, y + 8);
					g.drawLine(x + 12, y + 8, x + 12, y + 16);
					g.drawLine(x + 4, y + 16, x + 11, y + 16);
					g.drawLine(x + 4, y + 11, x + 4, y + 15);
				}
				else {
					g.drawLine(x + 5, y + 5, x + 14, y + 5);
					g.drawLine(x + 15, y + 5, x + 15, y + 15);
					g.drawLine(x + 5, y + 15, x + 14, y + 15);
					g.drawLine(x + 5, y + 8, x + 5, y + 14);
				}
				break;
			case MINIMIZE:
				g.drawLine(x + 5, y + 13, x + 10, y + 13);
				g.drawLine(x + 11, y + 13, x + 11, y + 15);
				break;
		}
	}
    
    /**
     * Creates a new Window Button UI for the specified type
     * @param type one of MINIMIZE, MAXIMIZE, CLOSE
     * @return TinyWindowButtonUI
     */
	public static TinyWindowButtonUI createButtonUIForType(int type) {
        return new TinyWindowButtonUI(type);
	}
	
	/**
	 * @see javax.swing.plaf.basic.BasicButtonUI#getPreferredSize(javax.swing.JComponent)
	 */
	public Dimension getPreferredSize(JComponent c) {
		if(((AbstractButton)c).getClientProperty("externalFrameButton") == Boolean.TRUE) {
			return Theme.frameExternalButtonSize[Theme.derivedStyle[Theme.style]];
		}
		else {
			if(c.getParent() instanceof TinyInternalFrameTitlePane) {
				if(((TinyInternalFrameTitlePane)c.getParent()).isPalette()) {
					return Theme.framePaletteButtonSize[Theme.derivedStyle[Theme.style]];
				}
			}
			
			return Theme.frameInternalButtonSize[Theme.derivedStyle[Theme.style]];
		}
	}
}
