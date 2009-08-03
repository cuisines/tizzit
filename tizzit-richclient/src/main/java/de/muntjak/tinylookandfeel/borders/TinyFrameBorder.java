/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
*	Tiny Look and Feel                                                         *
*                                                                              *
*  (C) Copyright 2003 - 2007 Hans Bickel                                       *
*                                                                              *
*   For licensing information and credits, please refer to the                 *
*   comment in file de.muntjak.tinylookandfeel.TinyLookAndFeel                 *
*                                                                              *
* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package de.muntjak.tinylookandfeel.borders;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;

import de.muntjak.tinylookandfeel.Theme;
import de.muntjak.tinylookandfeel.TinyLookAndFeel;
import de.muntjak.tinylookandfeel.controlpanel.ColorRoutines;
import de.muntjak.tinylookandfeel.controlpanel.SBChooser;

/**
 * TinyFrameBorder
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class TinyFrameBorder extends AbstractBorder implements UIResource {

	// cache for already painted captions
	private static HashMap cache = new HashMap();
	
	public static Color buttonUpperColor, buttonLowerColor;
	private static TinyFrameBorder onlyInstance;
	private static Robot robot;
	private static boolean robotsSupported = true;
	private Window window;
	private int titleHeight;
    private boolean isActive;
    
    public static TinyFrameBorder getInstance() {
    	if(onlyInstance == null) {
    		onlyInstance = new TinyFrameBorder();
    		
    		if(robot == null && robotsSupported) {
    			try {
					robot = new Robot();
				} catch(Exception ex) {
					robotsSupported = false;
				}
    		}
    	}
    	
    	return onlyInstance;
    }

	public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
		window = SwingUtilities.getWindowAncestor(c);
		isActive = window.isActive();
		
		if(window instanceof JFrame) {
			titleHeight = Theme.frameTitleHeight[Theme.derivedStyle[Theme.style]];
		}
		else {
			titleHeight = Theme.frameInternalTitleHeight[Theme.derivedStyle[Theme.style]];
		}

		if(isActive) {
    		g.setColor(Theme.frameBorderColor[Theme.style].getColor());
    	}
    	else {
    		g.setColor(Theme.frameBorderDisabledColor[Theme.style].getColor());
    	}
    	
    	switch(Theme.derivedStyle[Theme.style]) {
			case Theme.TINY_STYLE:
				drawTinyBorder(g, x, y, w, h);
				break;
			case Theme.W99_STYLE:
				drawWinBorder(g, x, y, w, h);
				break;
			case Theme.YQ_STYLE:
				drawXpBorder(g, x, y, w, h);
				break;
		}
		
		Color col = null;
		if(isActive) {
    		col = Theme.frameCaptionColor[Theme.style].getColor();
    	}
    	else {
    		col = Theme.frameCaptionDisabledColor[Theme.style].getColor();
    	}
    	g.setColor(col);
    	
    	switch(Theme.derivedStyle[Theme.style]) {
			case Theme.TINY_STYLE:
				drawTinyCaption(g, x, y, w, h, col);
				break;
			case Theme.W99_STYLE:
				drawWinCaption(g, x, y, w, h, col);
				break;
			case Theme.YQ_STYLE:
				if(TinyLookAndFeel.controlPanelInstantiated) {
					drawXpCaptionNoCache(g, x, y, w, h, col);
				}
				else {
					drawXpCaption(g, x, y, w, h, col);
				}
				break;
		}
	}
	
	private void drawTinyBorder(Graphics g, int x, int y, int w, int h) {
		
	}
	
	private void drawWinBorder(Graphics g, int x, int y, int w, int h) {
		// Border color
		g.drawLine(x, y, x + w - 2, y);
		g.drawLine(x, y + 1, x, y + h - 2);
		g.drawRect(x + 2, y + 2, w - 5, h - 5);
		g.drawRect(x + 3, y + 3, w - 7, h - 7);
		
		g.setColor(Color.WHITE);
		g.drawLine(x + 1, y + 1, x + w - 3, y + 1);
		g.drawLine(x + 1, y + 2, x + 1, y + h - 3);
		
		g.setColor(Theme.frameDarkColor[Theme.style].getColor());
		g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
		g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);
		
		g.setColor(Theme.frameLightColor[Theme.style].getColor());
		g.drawLine(x + 1, y + h - 2, x + w - 2, y + h - 2);
		g.drawLine(w - 2, y + 1, x + w - 2, y + h - 2);
	}
	
	private void drawXpBorder(Graphics g, int x, int y, int w, int h) {
		// left
		g.drawLine(x, y + 6, x, y + h - 1);
		g.drawLine(x + 2, y + titleHeight, x + 2, y + h - 3);
		// right
		g.drawLine(x + w - 1, y + 6, x + w - 1, y + h - 1);
		g.drawLine(x + w - 3, y + titleHeight, x + w - 3, y + h - 3);
		// bottom
		g.drawLine(x + 2, y + h - 3, x + w - 3, y + h - 3);
		g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);

		if(robot != null) {
			int wx = window.getLocationOnScreen().x - 4;
			int wy = window.getLocationOnScreen().y;

			Rectangle screenRect = new Rectangle(wx, wy, 4, 4);
			g.drawImage(robot.createScreenCapture(screenRect), x, y, null);
			
			wx = window.getLocationOnScreen().x + window.getWidth() + 1;
			screenRect = new Rectangle(wx, wy, 4, 4);
			g.drawImage(robot.createScreenCapture(screenRect), x + w - 4, y, null);
		}
		else {
			g.setColor(Theme.backColor[Theme.style].getColor());
			g.fillRect(0, 0, w, 3);
		}
		
		if(isActive) {
    		g.setColor(Theme.frameCaptionColor[Theme.style].getColor());
    	}
    	else {
    		g.setColor(Theme.frameCaptionDisabledColor[Theme.style].getColor());
    	}
    	// left
		g.drawLine(x + 1, y + titleHeight, x + 1, y + h - 2);
		// right
		g.drawLine(x + w - 2, y + titleHeight, x + w - 2, y + h - 2);
		// bottom
		g.drawLine(x + 1, y + h - 2, x + w - 2, y + h - 2);
		
		Color c = null;
		if(isActive) {
    		c = Theme.frameBorderColor[Theme.style].getColor();
    	}
    	else {
    		c = Theme.frameBorderDisabledColor[Theme.style].getColor();
    	}
    	
    	g.setColor(ColorRoutines.getAlphaColor(c, 82));
    	g.drawLine(x, y + 3, x, y + 3);
    	g.drawLine(x + w - 1, y + 3, x + w - 1, y + 3);
    	g.setColor(ColorRoutines.getAlphaColor(c, 156));
    	g.drawLine(x, y + 4, x, y + 4);
    	g.drawLine(x + w - 1, y + 4, x + w - 1, y + 4);
    	g.setColor(ColorRoutines.getAlphaColor(c, 215));
    	g.drawLine(x, y + 5, x, y + 5);
    	g.drawLine(x + w - 1, y + 5, x + w - 1, y + 5);
	}
	
	private void drawTinyCaption(Graphics g, int x, int y, int w, int h, Color c) {
		
	}
	
	private void drawWinCaption(Graphics g, int x, int y, int w, int h, Color c) {
		int y2 = y + titleHeight - 2;
		int tw = w - 8;
		int maxSat = 56, maxBri = 78;
		if(!isActive) {
			maxSat = 0; maxBri = 50;
		}
		
		int sat = 0, bri = 0;
		int memSat = 0, memBri = 0;
		Color col = c;
		for(int i = 0; i < tw; i++) {	// horizontal
			g.setColor(col);
			g.drawLine(x + 4 + i, y + 4, x + 4 + i, y2);
			
			sat = maxSat * i / tw;
			bri = maxBri * i / tw;
			if((sat != memSat) || (bri != memBri)) {
				memSat = sat;
				memBri = bri;
				col = SBChooser.getAdjustedColor(c, sat, bri);
			}
		}
		
		
		g.setColor(Theme.frameBorderColor[Theme.style].getColor());
		g.drawLine(x + 4, y + titleHeight - 1,
			x + w - 5, y + titleHeight - 1);
	}
	
	private void drawXpCaption(Graphics g, int x, int y, int w, int h, Color c) {
		if(titleHeight == Theme.frameInternalTitleHeight[Theme.derivedStyle[Theme.style]]) {
			drawXpInternalCaption(g, x, y, w, h, c);
			return;
		}
		
		int spread1 = Theme.frameSpreadDarkDisabled[Theme.style];
		int spread2 = Theme.frameSpreadLightDisabled[Theme.style];
		int y2 = y;
		Color borderColor = null;

		if(isActive) {
			borderColor = Theme.frameBorderColor[Theme.style].getColor();
    		spread1 = Theme.frameSpreadDark[Theme.style];
    		spread2 = Theme.frameSpreadLight[Theme.style];
    	}
    	else {
    		borderColor = Theme.frameBorderDisabledColor[Theme.style].getColor();
    	}
		
		// always paint the semi-transparent parts
// 1
		// blend
		g.setColor(ColorRoutines.getAlphaColor(borderColor, 82));
		g.drawLine(x + 3, y2, x + 3, y2);
		g.drawLine(x + w - 4, y2, x + w - 4, y2);
		g.setColor(ColorRoutines.getAlphaColor(borderColor, 156));
		g.drawLine(x + 4, y2, x + 4, y2);
		g.drawLine(x + w - 5, y2, x + w - 5, y2);
		g.setColor(ColorRoutines.getAlphaColor(borderColor, 215));
		g.drawLine(x + 5, y2, x + 5, y2);
		g.drawLine(x + w - 6, y2, x + w - 6, y2);		
		y2 ++;
// 2
		Color c2 = ColorRoutines.darken(c, 4 * spread1);
		g.setColor(c2);
		g.drawLine(x + 3, y2, x + 5, y2);	// left
		g.drawLine(x + w - 6, y2, x + w - 4, y2);	// right
		// blend
		g.setColor(ColorRoutines.getAlphaColor(c2, 139));
		g.drawLine(x + 2, y2, x + 2, y2);
		g.drawLine(x + w - 3, y2, x + w - 3, y2);
		g.setColor(ColorRoutines.getAlphaColor(c2, 23));
		g.drawLine(x + 1, y2, x + 1, y2);
		g.drawLine(x + w - 2, y2, x + w - 2, y2);
		y2 ++;
// 3
		g.setColor(ColorRoutines.lighten(c, 10 * spread2));
		g.drawLine(x + 4, y2, x + 5, y2);	// left
		g.drawLine(x + w - 6, y2, x + w - 5, y2);	// right
		// darker border
		g.setColor(c);
		g.drawLine(x + 3, y2, x + 3, y2);
		g.drawLine(x + w - 4, y2, x + w - 4, y2);		
		c2 = ColorRoutines.darken(c, 6 * spread1);
		g.setColor(c2);
		g.drawLine(x + 2, y2, x + 2, y2);
		g.drawLine(x + w - 3, y2, x + w - 3, y2);
		// blend		
		g.setColor(ColorRoutines.getAlphaColor(c2, 139));
		g.drawLine(x + 1, y2, x + 1, y2);
		g.drawLine(x + w - 2, y2, x + w - 2, y2);
		y2 ++;
// 4
		// darker border
		g.setColor(c);
		g.drawLine(x + 2, y2, x + 2, y2);
		g.drawLine(x + w - 3, y2, x + w - 3, y2);		
		g.setColor(ColorRoutines.darken(c, 6 * spread1));
		g.drawLine(x + 1, y2, x + 1, y2);
		g.drawLine(x + w - 2, y2, x + w - 2, y2);
		// blend from lightest color
		g.setColor(ColorRoutines.lighten(c, 10 * spread2));
		g.drawLine(x + 3, y2, x + 3, y2);
		g.drawLine(x + w - 4, y2, x + w - 4, y2);
		g.setColor(ColorRoutines.lighten(c, 7 * spread2));
		g.drawLine(x + 4, y2, x + 4, y2);
		g.drawLine(x + w - 5, y2, x + w - 5, y2);
		g.setColor(ColorRoutines.lighten(c, 3 * spread2));
		g.drawLine(x + 5, y2, x + 5, y2);
		g.drawLine(x + w - 6, y2, x + w - 6, y2);
		g.setColor(c);
		g.drawLine(x + 6, y2, x + 6, y2);
		g.drawLine(x + w - 7, y2, x + w - 7, y2);
		y2 ++;
// 5
		g.setColor(ColorRoutines.darken(c, 2 * spread1));
		g.drawLine(x + 5, y2, x + 6, y2);	// left
		g.drawLine(x + x + w - 7, y2, x + w - 6, y2);	// right
		// darker border
		g.setColor(ColorRoutines.darken(c, 6 * spread1));
		g.drawLine(x + 1, y2, x + 1, y2);
		g.drawLine(x + w - 2, y2, x + w - 2, y2);
		// blend from lightest color
		g.setColor(ColorRoutines.lighten(c, 10 * spread2));
		g.drawLine(x + 2, y2, x + 2, y2);
		g.drawLine(x + w - 3, y2, x + w - 3, y2);
		g.setColor(ColorRoutines.lighten(c, 5 * spread2));
		g.drawLine(x + 3, y2, x + 3, y2);
		g.drawLine(x + w - 4, y2, x + w - 4, y2);
		g.setColor(c);
		g.drawLine(x + 4, y2, x + 4, y2);
		g.drawLine(x + w - 5, y2, x + w - 5, y2);
		y2 ++;
// 6
		// lighten little
		g.setColor(ColorRoutines.darken(c, 4 * spread1));
		g.drawLine(x + 1, y2, x + 1, y2);
		g.drawLine(x + w - 2, y2, x + w - 2, y2);
		
		// now either paint from cache or create cached image
		CaptionKey key = new CaptionKey(isActive, titleHeight);
		Object value = cache.get(key);
		
		if(value != null) {
			// image is cached - paint and return
			g.drawImage((Image)value,
				x + 6, y, x + w - 6, y + 5,
				0, 0, 1, 5,
				window);
			g.drawImage((Image)value,
				x + 1, y + 5, x + w - 1, y + titleHeight,
				0, 5, 1, titleHeight,
				window);
				
			// store button colors
			buttonUpperColor = ColorRoutines.darken(c, 4 * spread1);
			buttonLowerColor = ColorRoutines.lighten(c, 10 * spread2);
			
			return;
		}

		Image img = new BufferedImage(1, titleHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics imgGraphics = img.getGraphics();
// 1
		imgGraphics.setColor(borderColor);
		imgGraphics.drawLine(0, 0, 1, 0);
// 2
		imgGraphics.setColor(ColorRoutines.darken(c, 4 * spread1));
		imgGraphics.drawLine(0, 1, 1, 1);
// 3
		imgGraphics.setColor(ColorRoutines.lighten(c, 10 * spread2));
		imgGraphics.drawLine(0, 2, 1, 2);
// 4
		imgGraphics.setColor(c);
		imgGraphics.drawLine(0, 3, 1, 3);
// 5
		imgGraphics.setColor(ColorRoutines.darken(c, 2 * spread1));
		imgGraphics.drawLine(0, 4, 1, 4);
// 6
		buttonUpperColor = ColorRoutines.darken(c, 4 * spread1);
		imgGraphics.setColor(buttonUpperColor);
		imgGraphics.drawLine(0, 5, 1, 5);
// 7 - 8
		imgGraphics.setColor(ColorRoutines.darken(c, 4 * spread1));
		imgGraphics.drawLine(0, 6, 1, 6);
		imgGraphics.drawLine(0, 7, 1, 7);
// 9 - 12
		imgGraphics.setColor(ColorRoutines.darken(c, 3 * spread1));
		imgGraphics.drawLine(0, 8, 1, 8);
		imgGraphics.drawLine(0, 9, 1, 9);
		imgGraphics.drawLine(0, 10, 1, 10);
		imgGraphics.drawLine(0, 11, 1, 11);
// 13 - 15
		imgGraphics.setColor(ColorRoutines.darken(c, 2 * spread1));
		imgGraphics.drawLine(0, 12, 1, 12);
		imgGraphics.drawLine(0, 13, 1, 13);
		imgGraphics.drawLine(0, 14, 1, 14);
// 16 - 17
		imgGraphics.setColor(ColorRoutines.darken(c, spread1));
		imgGraphics.drawLine(0, 15, 1, 15);
		imgGraphics.drawLine(0, 16, 1, 16);
// 18 - 19
		imgGraphics.setColor(c);
		imgGraphics.drawLine(0, 17, 1, 17);
		imgGraphics.drawLine(0, 18, 1, 18);
// 20...
		imgGraphics.setColor(ColorRoutines.lighten(c, 2 * spread2));
		imgGraphics.drawLine(0, 19, 1, 19);
		imgGraphics.setColor(ColorRoutines.lighten(c, 4 * spread2));
		imgGraphics.drawLine(0, 20, 1, 20);
		imgGraphics.setColor(ColorRoutines.lighten(c, 5 * spread2));
		imgGraphics.drawLine(0, 21, 1, 21);
		imgGraphics.setColor(ColorRoutines.lighten(c, 6 * spread2));
		imgGraphics.drawLine(0, 22, 1, 22);
		imgGraphics.setColor(ColorRoutines.lighten(c, 8 * spread2));
		imgGraphics.drawLine(0, 23, 1, 23);
		imgGraphics.setColor(ColorRoutines.lighten(c, 9 * spread2));
		imgGraphics.drawLine(0, 24, 1, 24);
		imgGraphics.setColor(ColorRoutines.lighten(c, 10 * spread2));
		imgGraphics.drawLine(0, 25, 1, 25);
// 27
		imgGraphics.setColor(ColorRoutines.lighten(c, 4 * spread2));
		imgGraphics.drawLine(0, 26, 1, 26);
// 28
		imgGraphics.setColor(ColorRoutines.darken(c, 2 * spread1));
		imgGraphics.drawLine(0, 27, 1, 27);
// 29		
		if(isActive) {
    		imgGraphics.setColor(Theme.frameLightColor[Theme.style].getColor());
    	}
    	else {
    		imgGraphics.setColor(Theme.frameLightDisabledColor[Theme.style].getColor());
    	}
		imgGraphics.drawLine(0, 28, 1, 28);

		// dispose of image graphics
		imgGraphics.dispose();
		
		// paint image
		g.drawImage(img,
			x + 6, y, x + w - 6, y + 5,
			0, 0, 1, 5,
			window);
		g.drawImage(img,
			x + 1, y + 5, x + w - 1, y + titleHeight,
			0, 5, 1, titleHeight,
			window);
		
		// add the image to the cache
		cache.put(key, img);
	}
	
	private void drawXpCaptionNoCache(Graphics g, int x, int y, int w, int h, Color c) {
		if(titleHeight == Theme.frameInternalTitleHeight[Theme.derivedStyle[Theme.style]]) {
			drawXpInternalCaptionNoCache(g, x, y, w, h, c);
			return;
		}

		int y2 = y;
		int spread1 = Theme.frameSpreadDarkDisabled[Theme.style];
		int spread2 = Theme.frameSpreadLightDisabled[Theme.style];
		Color borderColor = null;
		
		if(isActive) {
    		borderColor = Theme.frameBorderColor[Theme.style].getColor();
    		spread1 = Theme.frameSpreadDark[Theme.style];
    		spread2 = Theme.frameSpreadLight[Theme.style];
    	}
    	else {
    		borderColor = Theme.frameBorderDisabledColor[Theme.style].getColor();
    	}
// 1
		g.setColor(borderColor);
		g.drawLine(x + 6, y2, x + w - 7, y2);
		// blend
		g.setColor(ColorRoutines.getAlphaColor(borderColor, 82));
		g.drawLine(x + 3, y2, x + 3, y2);
		g.drawLine(x + w - 4, y2, x + w - 4, y2);
		g.setColor(ColorRoutines.getAlphaColor(borderColor, 156));
		g.drawLine(x + 4, y2, x + 4, y2);
		g.drawLine(x + w - 5, y2, x + w - 5, y2);
		g.setColor(ColorRoutines.getAlphaColor(borderColor, 215));
		g.drawLine(x + 5, y2, x + 5, y2);
		g.drawLine(x + w - 6, y2, x + w - 6, y2);		
		y2 ++;
// 2
		Color c2 = ColorRoutines.darken(c, 4 * spread1);
		g.setColor(c2);
		g.drawLine(x + 3, y2, x + w - 4, y2);
		// blend
		g.setColor(ColorRoutines.getAlphaColor(c2, 139));
		g.drawLine(x + 2, y2, x + 2, y2);
		g.drawLine(x + w - 3, y2, x + w - 3, y2);
		g.setColor(ColorRoutines.getAlphaColor(c2, 23));
		g.drawLine(x + 1, y2, x + 1, y2);
		g.drawLine(x + w - 2, y2, x + w - 2, y2);
		y2 ++;
// 3
		g.setColor(ColorRoutines.lighten(c, 10 * spread2));
		g.drawLine(x + 4, y2, x + w - 5, y2);
		// darker border
		g.setColor(c);
		g.drawLine(x + 3, y2, x + 3, y2);
		g.drawLine(x + w - 4, y2, x + w - 4, y2);		
		c2 = ColorRoutines.darken(c, 6 * spread1);
		g.setColor(c2);
		g.drawLine(x + 2, y2, x + 2, y2);
		g.drawLine(x + w - 3, y2, x + w - 3, y2);
		// blend		
		g.setColor(ColorRoutines.getAlphaColor(c2, 139));
		g.drawLine(x + 1, y2, x + 1, y2);
		g.drawLine(x + w - 2, y2, x + w - 2, y2);
		y2 ++;
// 4
		g.setColor(c);
		g.drawLine(x + 7, y2, x + w - 8, y2);
		// darker border
		g.setColor(c);
		g.drawLine(x + 2, y2, x + 2, y2);
		g.drawLine(x + w - 3, y2, x + w - 3, y2);		
		g.setColor(ColorRoutines.darken(c, 6 * spread1));
		g.drawLine(x + 1, y2, x + 1, y2);
		g.drawLine(x + w - 2, y2, x + w - 2, y2);
		// blend from lightest color
		g.setColor(ColorRoutines.lighten(c, 10 * spread2));
		g.drawLine(x + 3, y2, x + 3, y2);
		g.drawLine(x + w - 4, y2, x + w - 4, y2);
		g.setColor(ColorRoutines.lighten(c, 7 * spread2));
		g.drawLine(x + 4, y2, x + 4, y2);
		g.drawLine(x + w - 5, y2, x + w - 5, y2);
		g.setColor(ColorRoutines.lighten(c, 3 * spread2));
		g.drawLine(x + 5, y2, x + 5, y2);
		g.drawLine(x + w - 6, y2, x + w - 6, y2);
		g.setColor(c);
		g.drawLine(x + 6, y2, x + 6, y2);
		g.drawLine(x + w - 7, y2, x + w - 7, y2);
		y2 ++;
// 5
		g.setColor(ColorRoutines.darken(c, 2 * spread1));
		g.drawLine(x + 5, y2, x + w - 6, y2);
		// darker border
		g.setColor(ColorRoutines.darken(c, 6 * spread1));
		g.drawLine(x + 1, y2, x + 1, y2);
		g.drawLine(x + w - 2, y2, x + w - 2, y2);
		// blend from lightest color
		g.setColor(ColorRoutines.lighten(c, 10 * spread2));
		g.drawLine(x + 2, y2, x + 2, y2);
		g.drawLine(x + w - 3, y2, x + w - 3, y2);
		g.setColor(ColorRoutines.lighten(c, 5 * spread2));
		g.drawLine(x + 3, y2, x + 3, y2);
		g.drawLine(x + w - 4, y2, x + w - 4, y2);
		g.setColor(c);
		g.drawLine(x + 4, y2, x + 4, y2);
		g.drawLine(x + w - 5, y2, x + w - 5, y2);
		y2 ++;
// 6
		buttonUpperColor = ColorRoutines.darken(c, 4 * spread1);
		g.setColor(buttonUpperColor);
		g.drawLine(x + 2, y2, x + w - 3, y2);
		// lighten little
		g.setColor(ColorRoutines.darken(c, 4 * spread1));
		g.drawLine(x + 1, y2, x + 1, y2);
		g.drawLine(x + w - 2, y2, x + w - 2, y2);
		y2 ++;
// 7 - 8
		g.setColor(ColorRoutines.darken(c, 4 * spread1));
		g.fillRect(x + 1, y2, w - 2, 2);
		y2 += 2;
// 9 - 12
		g.setColor(ColorRoutines.darken(c, 3 * spread1));
		g.fillRect(x + 1, y2, w - 2, 4);
		y2 += 4;
// 13 - 15
		g.setColor(ColorRoutines.darken(c, 2 * spread1));
		g.fillRect(x + 1, y2, w - 2, 3);
		y2 += 3;
// 16 - 17
		g.setColor(ColorRoutines.darken(c, 1 * spread1));
		g.fillRect(x + 1, y2, w - 2, 2);
		y2 += 2;
// 18 - 19
		g.setColor(c);
		g.fillRect(x + 1, y2, w - 2, 2);
		y2 += 2;
// 20...
		g.setColor(ColorRoutines.lighten(c, 2 * spread2));
		g.drawLine(x + 1, y2, x + w - 2, y2);
		y2 ++;
		g.setColor(ColorRoutines.lighten(c, 4 * spread2));
		g.drawLine(x + 1, y2, x + w - 2, y2);
		y2 ++;
		g.setColor(ColorRoutines.lighten(c, 5 * spread2));
		g.drawLine(x + 1, y2, x + w - 2, y2);
		y2 ++;
		g.setColor(ColorRoutines.lighten(c, 6 * spread2));
		g.drawLine(x + 1, y2, x + w - 2, y2);
		y2 ++;
		g.setColor(ColorRoutines.lighten(c, 8 * spread2));
		g.drawLine(x + 1, y2, x + w - 2, y2);
		y2 ++;
		g.setColor(ColorRoutines.lighten(c, 9 * spread2));
		g.drawLine(x + 1, y2, x + w - 2, y2);
		y2 ++;
		buttonLowerColor = ColorRoutines.lighten(c, 10 * spread2);
		g.setColor(buttonLowerColor);
		g.drawLine(x + 1, y2, x + w - 2, y2);
		y2 ++;
// 27
		g.setColor(ColorRoutines.lighten(c, 4 * spread2));
		g.drawLine(x + 1, y2, x + w - 2, y2);
		y2 ++;
// 28
		g.setColor(ColorRoutines.darken(c, 2 * spread1));
		g.drawLine(x + 1, y2, x + w - 2, y2);
		y2 ++;
// 29		
		if(isActive) {
    		g.setColor(Theme.frameLightColor[Theme.style].getColor());
    	}
    	else {
    		g.setColor(Theme.frameLightDisabledColor[Theme.style].getColor());
    	}
		g.drawLine(x + 1, y2, x + w - 2, y2);
	}
	
	private void drawXpInternalCaption(Graphics g, int x, int y, int w, int h, Color c) {
		int spread1 = Theme.frameSpreadDarkDisabled[Theme.style];
		int spread2 = Theme.frameSpreadLightDisabled[Theme.style];
		int y2 = y;
		Color borderColor = null;

		if(isActive) {
			borderColor = Theme.frameBorderColor[Theme.style].getColor();
    		spread1 = Theme.frameSpreadDark[Theme.style];
    		spread2 = Theme.frameSpreadLight[Theme.style];
    	}
    	else {
    		borderColor = Theme.frameBorderDisabledColor[Theme.style].getColor();
    	}
    	
    	// always paint the semi-transparent parts
// 1
		// blend
		g.setColor(ColorRoutines.getAlphaColor(borderColor, 82));
		g.drawLine(x + 3, y2, x + 3, y2);
		g.drawLine(x + w - 4, y2, x + w - 4, y2);
		g.setColor(ColorRoutines.getAlphaColor(borderColor, 156));
		g.drawLine(x + 4, y2, x + 4, y2);
		g.drawLine(x + w - 5, y2, x + w - 5, y2);
		g.setColor(ColorRoutines.getAlphaColor(borderColor, 215));
		g.drawLine(x + 5, y2, x + 5, y2);
		g.drawLine(x + w - 6, y2, x + w - 6, y2);		
		y2 ++;
// 2
		Color c2 = ColorRoutines.darken(c, 4 * spread1);
		g.setColor(c2);
		g.drawLine(x + 3, y2, x + 5, y2);	// left
		g.drawLine(x + w - 6, y2, x + w - 4, y2);	// right
		// blend
		g.setColor(ColorRoutines.getAlphaColor(c2, 139));
		g.drawLine(x + 2, y2, x + 2, y2);
		g.drawLine(x + w - 3, y2, x + w - 3, y2);
		g.setColor(ColorRoutines.getAlphaColor(c2, 23));
		g.drawLine(x + 1, y2, x + 1, y2);
		g.drawLine(x + w - 2, y2, x + w - 2, y2);
		y2 ++;
// 3
		g.setColor(ColorRoutines.lighten(c, 10 * spread2));
		g.drawLine(x + 4, y2, x + 5, y2);	// left
		g.drawLine(x + w - 6, y2, x + w - 5, y2);	// right
		// darker border
		g.setColor(c);
		g.drawLine(x + 3, y2, x + 3, y2);
		g.drawLine(x + w - 4, y2, x + w - 4, y2);		
		c2 = ColorRoutines.darken(c, 6 * spread1);
		g.setColor(c2);
		g.drawLine(x + 2, y2, x + 2, y2);
		g.drawLine(x + w - 3, y2, x + w - 3, y2);
		// blend		
		g.setColor(ColorRoutines.getAlphaColor(c2, 139));
		g.drawLine(x + 1, y2, x + 1, y2);
		g.drawLine(x + w - 2, y2, x + w - 2, y2);
		y2 ++;
// 4
		// darker border
		g.setColor(c);
		g.drawLine(x + 2, y2, x + 2, y2);
		g.drawLine(x + w - 3, y2, x + w - 3, y2);		
		g.setColor(ColorRoutines.darken(c, 6 * spread1));
		g.drawLine(x + 1, y2, x + 1, y2);
		g.drawLine(x + w - 2, y2, x + w - 2, y2);
		// blend from lightest color
		g.setColor(ColorRoutines.lighten(c, 10 * spread2));
		g.drawLine(x + 3, y2, x + 3, y2);
		g.drawLine(x + w - 4, y2, x + w - 4, y2);
		g.setColor(ColorRoutines.lighten(c, 7 * spread2));
		g.drawLine(x + 4, y2, x + 4, y2);
		g.drawLine(x + w - 5, y2, x + w - 5, y2);
		g.setColor(ColorRoutines.lighten(c, 3 * spread2));
		g.drawLine(x + 5, y2, x + 5, y2);
		g.drawLine(x + w - 6, y2, x + w - 6, y2);
		g.setColor(c);
		g.drawLine(x + 6, y2, x + 6, y2);
		g.drawLine(x + w - 7, y2, x + w - 7, y2);
		y2 ++;
// 5
		g.setColor(ColorRoutines.darken(c, 2 * spread1));
		g.drawLine(x + 5, y2, x + 6, y2);	// left
		g.drawLine(x + x + w - 7, y2, x + w - 6, y2);	// right
		// darker border
		g.setColor(ColorRoutines.darken(c, 6 * spread1));
		g.drawLine(x + 1, y2, x + 1, y2);
		g.drawLine(x + w - 2, y2, x + w - 2, y2);
		// blend from lightest color
		g.setColor(ColorRoutines.lighten(c, 10 * spread2));
		g.drawLine(x + 2, y2, x + 2, y2);
		g.drawLine(x + w - 3, y2, x + w - 3, y2);
		g.setColor(ColorRoutines.lighten(c, 5 * spread2));
		g.drawLine(x + 3, y2, x + 3, y2);
		g.drawLine(x + w - 4, y2, x + w - 4, y2);
		g.setColor(c);
		g.drawLine(x + 4, y2, x + 4, y2);
		g.drawLine(x + w - 5, y2, x + w - 5, y2);
		y2 ++;
// 6
		// lighten little
		g.setColor(ColorRoutines.darken(c, 4 * spread1));
		g.drawLine(x + 1, y2, x + 1, y2);
		g.drawLine(x + w - 2, y2, x + w - 2, y2);
		
		// now either paint from cache or create cached image
		CaptionKey key = new CaptionKey(isActive, titleHeight);
		Object value = cache.get(key);
		
		if(value != null) {
			// image is cached - paint and return
			g.drawImage((Image)value,
				x + 6, y, x + w - 6, y + 5,
				0, 0, 1, 5,
				window);
			g.drawImage((Image)value,
				x + 1, y + 5, x + w - 1, y + titleHeight,
				0, 5, 1, titleHeight,
				window);
				
			// store button colors
			buttonUpperColor = ColorRoutines.darken(c, 4 * spread1);
			buttonLowerColor = ColorRoutines.lighten(c, 10 * spread2);

			return;
		}

		Image img = new BufferedImage(1, titleHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics imgGraphics = img.getGraphics();
		
// 1
		imgGraphics.setColor(borderColor);
		imgGraphics.drawLine(0, 0, 1, 0);
// 2
		imgGraphics.setColor(ColorRoutines.darken(c, 4 * spread1));
		imgGraphics.drawLine(0, 1, 1, 1);
// 3
		imgGraphics.setColor(ColorRoutines.lighten(c, 10 * spread2));
		imgGraphics.drawLine(0, 2, 1, 2);
// 4
		imgGraphics.setColor(c);
		imgGraphics.drawLine(0, 3, 1, 3);
// 5
		imgGraphics.setColor(ColorRoutines.darken(c, 2 * spread1));
		imgGraphics.drawLine(0, 4, 1, 4);
// 6
		imgGraphics.setColor(ColorRoutines.darken(c, 4 * spread1));
		imgGraphics.drawLine(0, 5, 1, 5);
// 7
		imgGraphics.setColor(ColorRoutines.darken(c, 4 * spread1));
		imgGraphics.drawLine(0, 6, 1, 6);
// 8 - 10
		imgGraphics.setColor(ColorRoutines.darken(c, 3 * spread1));
		imgGraphics.drawLine(0, 7, 1, 7);
		imgGraphics.drawLine(0, 8, 1, 8);
		imgGraphics.drawLine(0, 9, 1, 9);
// 11 - 12
		imgGraphics.setColor(ColorRoutines.darken(c, 2 * spread1));
		imgGraphics.drawLine(0, 10, 1, 10);
		imgGraphics.drawLine(0, 11, 1, 11);
// 13
		imgGraphics.setColor(ColorRoutines.darken(c, spread1));
		imgGraphics.drawLine(0, 12, 1, 12);
// 14 - 15
		imgGraphics.setColor(c);
		imgGraphics.drawLine(0, 13, 1, 13);
		imgGraphics.drawLine(0, 14, 1, 14);
// 16...
		imgGraphics.setColor(ColorRoutines.lighten(c, 2 * spread2));
		imgGraphics.drawLine(0, 15, 1, 15);
		imgGraphics.setColor(ColorRoutines.lighten(c, 4 * spread2));
		imgGraphics.drawLine(0, 16, 1, 16);
		imgGraphics.setColor(ColorRoutines.lighten(c, 5 * spread2));
		imgGraphics.drawLine(0, 17, 1, 17);
		imgGraphics.setColor(ColorRoutines.lighten(c, 6 * spread2));
		imgGraphics.drawLine(0, 18, 1, 18);
		imgGraphics.setColor(ColorRoutines.lighten(c, 8 * spread2));
		imgGraphics.drawLine(0, 19, 1, 19);
		imgGraphics.setColor(ColorRoutines.lighten(c, 9 * spread2));
		imgGraphics.drawLine(0, 20, 1, 20);
		imgGraphics.setColor(ColorRoutines.lighten(c, 10 * spread2));
		imgGraphics.drawLine(0, 21, 1, 21);
// 23
		imgGraphics.setColor(ColorRoutines.lighten(c, 4 * spread2));
		imgGraphics.drawLine(0, 22, 1, 22);
// 24
		imgGraphics.setColor(ColorRoutines.darken(c, 2 * spread1));
		imgGraphics.drawLine(0, 23, 1, 23);
// 25		
		if(isActive) {
    		imgGraphics.setColor(Theme.frameLightColor[Theme.style].getColor());
    	}
    	else {
    		imgGraphics.setColor(Theme.frameLightDisabledColor[Theme.style].getColor());
    	}
		imgGraphics.drawLine(0, 24, 1, 24);
		
		// dispose of image graphics
		imgGraphics.dispose();
		
		// paint image
		g.drawImage(img,
			x + 6, y, x + w - 6, y + 5,
			0, 0, 1, 5,
			window);
		g.drawImage(img,
			x + 1, y + 5, x + w - 1, y + titleHeight,
			0, 5, 1, titleHeight,
			window);
		
		// add the image to the cache
		cache.put(key, img);
	}
	
	private void drawXpInternalCaptionNoCache(Graphics g, int x, int y, int w, int h, Color c) {	
		int y2 = y;
		int spread1 = Theme.frameSpreadDarkDisabled[Theme.style];
		int spread2 = Theme.frameSpreadLightDisabled[Theme.style];
		Color borderColor = null;
		
		if(isActive) {
    		borderColor = Theme.frameBorderColor[Theme.style].getColor();
    		spread1 = Theme.frameSpreadDark[Theme.style];
    		spread2 = Theme.frameSpreadLight[Theme.style];
    	}
    	else {
    		borderColor = Theme.frameBorderDisabledColor[Theme.style].getColor();
    	}
// 1
		g.setColor(borderColor);
		g.drawLine(x + 6, y2, x + w - 7, y2);
		// blend
		g.setColor(ColorRoutines.getAlphaColor(borderColor, 82));
		g.drawLine(x + 3, y2, x + 3, y2);
		g.drawLine(x + w - 4, y2, x + w - 4, y2);
		g.setColor(ColorRoutines.getAlphaColor(borderColor, 156));
		g.drawLine(x + 4, y2, x + 4, y2);
		g.drawLine(x + w - 5, y2, x + w - 5, y2);
		g.setColor(ColorRoutines.getAlphaColor(borderColor, 215));
		g.drawLine(x + 5, y2, x + 5, y2);
		g.drawLine(x + w - 6, y2, x + w - 6, y2);		
		y2 ++;
// 2
		Color c2 = ColorRoutines.darken(c, 4 * spread1);
		g.setColor(c2);
		g.drawLine(x + 3, y2, x + w - 4, y2);
		// blend
		g.setColor(ColorRoutines.getAlphaColor(c2, 139));
		g.drawLine(x + 2, y2, x + 2, y2);
		g.drawLine(x + w - 3, y2, x + w - 3, y2);
		g.setColor(ColorRoutines.getAlphaColor(c2, 23));
		g.drawLine(x + 1, y2, x + 1, y2);
		g.drawLine(x + w - 2, y2, x + w - 2, y2);
		y2 ++;
// 3
		g.setColor(ColorRoutines.lighten(c, 10 * spread2));
		g.drawLine(x + 4, y2, x + w - 5, y2);
		// darker border
		g.setColor(c);
		g.drawLine(x + 3, y2, x + 3, y2);
		g.drawLine(x + w - 4, y2, x + w - 4, y2);		
		c2 = ColorRoutines.darken(c, 6 * spread1);
		g.setColor(c2);
		g.drawLine(x + 2, y2, x + 2, y2);
		g.drawLine(x + w - 3, y2, x + w - 3, y2);
		// blend		
		g.setColor(ColorRoutines.getAlphaColor(c2, 139));
		g.drawLine(x + 1, y2, x + 1, y2);
		g.drawLine(x + w - 2, y2, x + w - 2, y2);
		y2 ++;
// 4
		g.setColor(c);
		g.drawLine(x + 7, y2, x + w - 8, y2);
		// darker border
		g.setColor(c);
		g.drawLine(x + 2, y2, x + 2, y2);
		g.drawLine(x + w - 3, y2, x + w - 3, y2);		
		g.setColor(ColorRoutines.darken(c, 6 * spread1));
		g.drawLine(x + 1, y2, x + 1, y2);
		g.drawLine(x + w - 2, y2, x + w - 2, y2);
		// blend from lightest color
		g.setColor(ColorRoutines.lighten(c, 10 * spread2));
		g.drawLine(x + 3, y2, x + 3, y2);
		g.drawLine(x + w - 4, y2, x + w - 4, y2);
		g.setColor(ColorRoutines.lighten(c, 7 * spread2));
		g.drawLine(x + 4, y2, x + 4, y2);
		g.drawLine(x + w - 5, y2, x + w - 5, y2);
		g.setColor(ColorRoutines.lighten(c, 3 * spread2));
		g.drawLine(x + 5, y2, x + 5, y2);
		g.drawLine(x + w - 6, y2, x + w - 6, y2);
		g.setColor(c);
		g.drawLine(x + 6, y2, x + 6, y2);
		g.drawLine(x + w - 7, y2, x + w - 7, y2);
		y2 ++;
// 5
		g.setColor(ColorRoutines.darken(c, 2 * spread1));
		g.drawLine(x + 5, y2, x + w - 6, y2);
		// darker border
		g.setColor(ColorRoutines.darken(c, 6 * spread1));
		g.drawLine(x + 1, y2, x + 1, y2);
		g.drawLine(x + w - 2, y2, x + w - 2, y2);
		// blend from lightest color
		g.setColor(ColorRoutines.lighten(c, 10 * spread2));
		g.drawLine(x + 2, y2, x + 2, y2);
		g.drawLine(x + w - 3, y2, x + w - 3, y2);
		g.setColor(ColorRoutines.lighten(c, 5 * spread2));
		g.drawLine(x + 3, y2, x + 3, y2);
		g.drawLine(x + w - 4, y2, x + w - 4, y2);
		g.setColor(c);
		g.drawLine(x + 4, y2, x + 4, y2);
		g.drawLine(x + w - 5, y2, x + w - 5, y2);
		y2 ++;
// 6
		g.setColor(ColorRoutines.darken(c, 4 * spread1));
		g.drawLine(x + 2, y2, x + w - 3, y2);
		// lighten little
		g.setColor(ColorRoutines.darken(c, 4 * spread1));
		g.drawLine(x + 1, y2, x + 1, y2);
		g.drawLine(x + w - 2, y2, x + w - 2, y2);
		y2 ++;
// 7
		g.setColor(ColorRoutines.darken(c, 4 * spread1));
		g.fillRect(x + 1, y2, w - 2, 1);
		y2 += 1;
// 8 - 10
		g.setColor(ColorRoutines.darken(c, 3 * spread1));
		g.fillRect(x + 1, y2, w - 2, 3);
		y2 += 3;
// 11 - 12
		g.setColor(ColorRoutines.darken(c, 2 * spread1));
		g.fillRect(x + 1, y2, w - 2, 2);
		y2 += 2;
// 13
		g.setColor(ColorRoutines.darken(c, 1 * spread1));
		g.fillRect(x + 1, y2, w - 2, 1);
		y2 += 1;
// 14 - 15
		g.setColor(c);
		g.fillRect(x + 1, y2, w - 2, 2);
		y2 += 2;
// 16...
		g.setColor(ColorRoutines.lighten(c, 2 * spread2));
		g.drawLine(x + 1, y2, x + w - 2, y2);
		y2 ++;
		g.setColor(ColorRoutines.lighten(c, 4 * spread2));
		g.drawLine(x + 1, y2, x + w - 2, y2);
		y2 ++;
		g.setColor(ColorRoutines.lighten(c, 5 * spread2));
		g.drawLine(x + 1, y2, x + w - 2, y2);
		y2 ++;
		g.setColor(ColorRoutines.lighten(c, 6 * spread2));
		g.drawLine(x + 1, y2, x + w - 2, y2);
		y2 ++;
		g.setColor(ColorRoutines.lighten(c, 8 * spread2));
		g.drawLine(x + 1, y2, x + w - 2, y2);
		y2 ++;
		g.setColor(ColorRoutines.lighten(c, 9 * spread2));
		g.drawLine(x + 1, y2, x + w - 2, y2);
		y2 ++;
		g.setColor(ColorRoutines.lighten(c, 10 * spread2));
		g.drawLine(x + 1, y2, x + w - 2, y2);
		y2 ++;
// 23
		g.setColor(ColorRoutines.lighten(c, 4 * spread2));
		g.drawLine(x + 1, y2, x + w - 2, y2);
		y2 ++;
// 24
		g.setColor(ColorRoutines.darken(c, 2 * spread1));
		g.drawLine(x + 1, y2, x + w - 2, y2);
		y2 ++;
// 25		
		if(isActive) {
    		g.setColor(Theme.frameLightColor[Theme.style].getColor());
    	}
    	else {
    		g.setColor(Theme.frameLightDisabledColor[Theme.style].getColor());
    	}
		g.drawLine(x + 1, y2, x + w - 2, y2);
	}

    /**
     * 
     * @see javax.swing.border.Border#getBorderInsets(Component)
     */
	public Insets getBorderInsets(Component c) {
		Window w = SwingUtilities.getWindowAncestor(c);
		
		if(w != null && (w instanceof Frame)) {
			Frame f = (Frame)w;

			// if the frame is maximized, the border should not be visible
			if(f.getExtendedState() == (f.getExtendedState() | Frame.MAXIMIZED_BOTH)) {
				return new Insets(0, 0, 0, 0);
			}
		}
		
		return new Insets(0,
			Theme.frameBorderWidth[Theme.derivedStyle[Theme.style]],
			Theme.frameBorderWidth[Theme.derivedStyle[Theme.style]],
			Theme.frameBorderWidth[Theme.derivedStyle[Theme.style]]);
	}
	
	/**
	 * CaptionKey is used as key in the cache HashMap.
	 * Overrides equals() and hashCode().
	 */
	static class CaptionKey {

		private boolean isActive;
		private int titleHeight;

		CaptionKey(boolean isActive, int titleHeight) {
			this.isActive = isActive;
			this.titleHeight = titleHeight;
		}
		
		public boolean equals(Object o) {
			if(o == null) return false;
			if(!(o instanceof CaptionKey)) return false;

			CaptionKey other = (CaptionKey)o;
			
			return isActive == other.isActive &&
				titleHeight == other.titleHeight;
		}
		
		public int hashCode() {
			return (isActive ? 1 : 2) * titleHeight;
		}
	}
}
