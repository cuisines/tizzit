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
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.metal.MetalScrollButton;

import de.muntjak.tinylookandfeel.controlpanel.*;

/**
 * TinyScrollButton
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class TinyScrollButton extends BasicArrowButton {
	
	// cache for already drawn icons - speeds up drawing by a factor
	// of 2.2 if there are several scroll buttons or one scroll button
	// is painted several times
	static HashMap cache = new HashMap();

	private boolean isRollover;
	
	private TinyScrollBarUI scrollbarUI;
	
	protected static Dimension[] size = new Dimension[3];
	
	static {
		size[0] = new Dimension(15, 15);
		size[1] = new Dimension(16, 16);
		size[2] = new Dimension(17, 17);
	}

	/**
	 * Create a new ScrollButton.
	 * @see javax.swing.plaf.metal.MetalScrollButton#MetalScrollButton(int, int, boolean)
	 */
	public TinyScrollButton(int direction,TinyScrollBarUI scrollbarUI) {
		super(direction);

		this.scrollbarUI = scrollbarUI;
		setBorder(null);
		setRolloverEnabled(true);
		setMargin(new Insets(0, 0, 0, 0));
		setSize(size[Theme.derivedStyle[Theme.style]]);
	}

	/**
	 * Paints the button
	 * @see java.awt.Component#paint(Graphics)
	 */
	public void paint(Graphics g) {
		isRollover = false;
		Color c = null;
		
		if(!scrollbarUI.isThumbVisible()) {
			c = Theme.scrollButtDisabledColor[Theme.style].getColor();
		}
		else if(getModel().isPressed()) {
			c = Theme.scrollButtPressedColor[Theme.style].getColor();
		}
		else if(getModel().isRollover() && Theme.scrollRollover[Theme.style]) {
			c = Theme.scrollButtRolloverColor[Theme.style].getColor();
			isRollover = true;
		}
		else {
			c = Theme.scrollButtColor[Theme.style].getColor();
		}
		
		g.setColor(c);
		
		switch(Theme.derivedStyle[Theme.style]) {
			case Theme.TINY_STYLE:
				drawTinyButton(g, getSize());
				break;
			case Theme.W99_STYLE:
				drawWinButton(g, getSize());
				break;
			case Theme.YQ_STYLE:
				if(TinyLookAndFeel.controlPanelInstantiated) {
					drawXpButtonNoCache(g, getSize(), c);
				}
				else {
					drawXpButton(g, getSize(), c);
				}
				break;
		}
		
		// arrows depend on scrollbar's style
		if(!scrollbarUI.isThumbVisible()) {
			g.setColor(Theme.scrollArrowDisabledColor[Theme.style].getColor());
		}
		else {
			g.setColor(Theme.scrollArrowColor[Theme.style].getColor());
		}
		
		switch(Theme.derivedStyle[Theme.style]) {
			case Theme.TINY_STYLE:
				drawTinyArrow(g, getSize());
				break;
			case Theme.W99_STYLE:
				drawWinArrow(g, getSize());
				break;
			case Theme.YQ_STYLE:
				drawXpArrow(g, getSize());
				break;
		}
	}
	
	private void drawTinyButton(Graphics g, Dimension size) {
	}
	
	private void drawWinButton(Graphics g, Dimension size) {
		g.fillRect(1, 1,  size.width - 2, size.height - 2);
		
		g.drawLine(0, 0,  size.width - 1, 0);
		g.drawLine(0, 0,  0, size.height - 1);
		
		if(getModel().isPressed() && scrollbarUI.isThumbVisible()) {
			g.setColor(Theme.scrollBorderColor[Theme.style].getColor());
			g.drawRect(0, 0,  size.width - 1, size.height - 1);
		}
		else {
			g.setColor(Theme.scrollLightColor[Theme.style].getColor());
			g.drawLine(1, 1,  size.width - 3, 1);
			g.drawLine(1, 1,  1, size.height - 3);
		
			g.setColor(Theme.scrollDarkColor[Theme.style].getColor());
			g.drawLine(size.width - 2, 1, size.width - 2, size.height - 3);
			g.drawLine(1, size.height - 2, size.width - 2, size.height - 2);
		
			g.setColor(Theme.scrollBorderColor[Theme.style].getColor());
			g.drawLine(size.width - 1, 0, size.width - 1, size.height - 2);
			g.drawLine(0, size.height - 1, size.width - 1, size.height - 1);
		}
	}
	
	private void drawXpButton(Graphics g, Dimension size, Color c) {
		boolean enabled = scrollbarUI.isThumbVisible();
		boolean pressed = getModel().isPressed();
		boolean rollover = getModel().isRollover() && Theme.scrollRollover[Theme.style];
		ScrollButtonKey key = new ScrollButtonKey(
			(direction == NORTH || direction == SOUTH),
			c, pressed, enabled, rollover);
		
		Object value = cache.get(key);
		
		if(value != null) {
			// image was cached - paint image and return
			g.drawImage((Image)value, 0, 0, this);
			return;
		}
		
		Image img = new BufferedImage(17, 17, BufferedImage.TYPE_INT_ARGB);
		Graphics imgGraphics = img.getGraphics();
		
		// size is 17x17, independent of orientation
		int spread1 = Theme.scrollSpreadLight[Theme.style];
		int spread2 = Theme.scrollSpreadDark[Theme.style];
		
		if(!scrollbarUI.isThumbVisible()) {
			spread1 = Theme.scrollSpreadLightDisabled[Theme.style];
			spread2 = Theme.scrollSpreadDarkDisabled[Theme.style];
		}
			
		switch (direction) {
			case NORTH:
			case SOUTH:
				int h = 17;
				float spreadStep1 = 10.0f * spread1 / 11;
				float spreadStep2 = 10.0f * spread2 / 11;
				int halfY = 6;
				int yd;
				
				for(int y = 1; y < h - 1; y++) {
					if(y < halfY) {
						yd = halfY - y;
						imgGraphics.setColor(ColorRoutines.lighten(
							c, (int)(yd * spreadStep1)));
					}
					else if(y == halfY) {
						imgGraphics.setColor(c);
					}
					else {
						yd = y - halfY;
						imgGraphics.setColor(ColorRoutines.darken(c, (int)(yd * spreadStep2)));
					}
			
					imgGraphics.drawLine(3, y, 14, y);
				}

				imgGraphics.setColor(Theme.scrollTrackBorderColor[Theme.style].getColor());
				imgGraphics.drawLine(0, 0, 0, 16);
				
				if(!scrollbarUI.isThumbVisible()) {
					c = Theme.scrollLightDisabledColor[Theme.style].getColor();
				}
				else {
					c = Theme.scrollLightColor[Theme.style].getColor();
				}
				imgGraphics.setColor(c);
				imgGraphics.drawLine(2, 1, 2, 15);
				imgGraphics.drawLine(15, 1, 15, 15);
				Color lightAlpha = new Color(c.getRed(), c.getGreen(), c.getBlue(), TinyScrollBarUI.alpha);
				
				if(!scrollbarUI.isThumbVisible()) {
					c = Theme.scrollBorderDisabledColor[Theme.style].getColor();
				}
				else {
					c = Theme.scrollBorderColor[Theme.style].getColor();
				}
				imgGraphics.setColor(c);
				imgGraphics.drawRect(1, 0, 15, 16);
				
				// edges - blend borderColor with lightColor
				imgGraphics.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), TinyScrollBarUI.alpha));				
				imgGraphics.drawLine(2, 1, 2, 1);
				imgGraphics.drawLine(15, 1, 15, 1);
				imgGraphics.drawLine(2, 15, 2, 15);
				imgGraphics.drawLine(15, 15, 15, 15);
				
				// blend lightColor with borderColor
				imgGraphics.setColor(lightAlpha);				
				imgGraphics.drawLine(1, 0, 1, 0);
				imgGraphics.drawLine(1, 16, 1, 16);
				imgGraphics.drawLine(16, 0, 16, 0);
				imgGraphics.drawLine(16, 16, 16, 16);
				break;
			case EAST:
			case WEST:
				spreadStep1 = 10.0f * spread1 / 10;
				spreadStep2 = 10.0f * spread2 / 10;
				halfY = 6;
				
				for(int y = 1; y < 15; y++) {
					if(y < halfY) {
						yd = halfY - y;
						imgGraphics.setColor(ColorRoutines.lighten(c, (int)(yd * spreadStep1)));
					}
					else if(y == halfY) {
						imgGraphics.setColor(c);
					}
					else {
						yd = y - halfY;
						imgGraphics.setColor(ColorRoutines.darken(c, (int)(yd * spreadStep2)));
					}
			
					imgGraphics.drawLine(2, y + 1, 14, y + 1);
				}
				
				imgGraphics.setColor(Theme.scrollTrackBorderColor[Theme.style].getColor());
				imgGraphics.drawLine(0, 0, 16, 0);
				
				if(!scrollbarUI.isThumbVisible()) {
					c = Theme.scrollLightDisabledColor[Theme.style].getColor();
				}
				else {
					c = Theme.scrollLightColor[Theme.style].getColor();
				}
				imgGraphics.setColor(c);
				imgGraphics.drawLine(1, 2, 1, 15);
				imgGraphics.drawLine(15, 2, 15, 15);
				lightAlpha = new Color(c.getRed(), c.getGreen(), c.getBlue(), TinyScrollBarUI.alpha);
				
				if(!scrollbarUI.isThumbVisible()) {
					c = Theme.scrollBorderDisabledColor[Theme.style].getColor();
				}
				else {
					c = Theme.scrollBorderColor[Theme.style].getColor();
				}
				imgGraphics.setColor(c);
				imgGraphics.drawRect(0, 1, 16, 15);
				
				// edges - blend borderColor with lightColor
				imgGraphics.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), TinyScrollBarUI.alpha));				
				imgGraphics.drawLine(1, 2, 1, 2);
				imgGraphics.drawLine(15, 2, 15, 2);
				imgGraphics.drawLine(1, 15, 1, 15);
				imgGraphics.drawLine(15, 15, 15, 15);
				
				// blend lightColor with borderColor
				imgGraphics.setColor(lightAlpha);				
				imgGraphics.drawLine(0, 1, 0, 1);
				imgGraphics.drawLine(16, 1, 16, 1);
				imgGraphics.drawLine(0, 16, 0, 16);
				imgGraphics.drawLine(16, 16, 16, 16);
				break;
		}
		
		// dispose of image graphics
		imgGraphics.dispose();
		
		// draw the image
		g.drawImage(img, 0, 0, this);
		
		// add the image to the cache
		cache.put(key, img);
	}
	
	private void drawXpButtonNoCache(Graphics g, Dimension size, Color c) {
		// size is 17x17, independent of orientation
		int spread1 = Theme.scrollSpreadLight[Theme.style];
		int spread2 = Theme.scrollSpreadDark[Theme.style];
		
		if(!scrollbarUI.isThumbVisible()) {
			spread1 = Theme.scrollSpreadLightDisabled[Theme.style];
			spread2 = Theme.scrollSpreadDarkDisabled[Theme.style];
		}
			
		switch (direction) {
			case SwingConstants.NORTH:
			case SwingConstants.SOUTH:
				int h = 17;
				float spreadStep1 = 10.0f * spread1 / 11;
				float spreadStep2 = 10.0f * spread2 / 11;
				int halfY = 6;
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
			
					g.drawLine(3, y, 14, y);
				}

				g.setColor(Theme.scrollTrackBorderColor[Theme.style].getColor());
				g.drawLine(0, 0, 0, 16);
				
				if(!scrollbarUI.isThumbVisible()) {
					c = Theme.scrollLightDisabledColor[Theme.style].getColor();
				}
				else {
					c = Theme.scrollLightColor[Theme.style].getColor();
				}
				g.setColor(c);
				g.drawLine(2, 1, 2, 15);
				g.drawLine(15, 1, 15, 15);
				Color lightAlpha = new Color(c.getRed(), c.getGreen(), c.getBlue(), TinyScrollBarUI.alpha);
				
				if(!scrollbarUI.isThumbVisible()) {
					c = Theme.scrollBorderDisabledColor[Theme.style].getColor();
				}
				else {
					c = Theme.scrollBorderColor[Theme.style].getColor();
				}
				g.setColor(c);
				g.drawRect(1, 0, 15, 16);
				
				// edges - blend borderColor with lightColor
				g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), TinyScrollBarUI.alpha));				
				g.drawLine(2, 1, 2, 1);
				g.drawLine(15, 1, 15, 1);
				g.drawLine(2, 15, 2, 15);
				g.drawLine(15, 15, 15, 15);
				
				// blend lightColor with borderColor
				g.setColor(lightAlpha);				
				g.drawLine(1, 0, 1, 0);
				g.drawLine(1, 16, 1, 16);
				g.drawLine(16, 0, 16, 0);
				g.drawLine(16, 16, 16, 16);
				break;
			case SwingConstants.EAST:
			case SwingConstants.WEST:
				spreadStep1 = 10.0f * spread1 / 10;
				spreadStep2 = 10.0f * spread2 / 10;
				halfY = 6;
				
				for(int y = 1; y < 15; y++) {
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
			
					g.drawLine(2, y + 1, 14, y + 1);
				}
				
				g.setColor(Theme.scrollTrackBorderColor[Theme.style].getColor());
				g.drawLine(0, 0, 16, 0);
				
				if(!scrollbarUI.isThumbVisible()) {
					c = Theme.scrollLightDisabledColor[Theme.style].getColor();
				}
				else {
					c = Theme.scrollLightColor[Theme.style].getColor();
				}
				g.setColor(c);
				g.drawLine(1, 2, 1, 15);
				g.drawLine(15, 2, 15, 15);
				lightAlpha = new Color(c.getRed(), c.getGreen(), c.getBlue(), TinyScrollBarUI.alpha);
				
				if(!scrollbarUI.isThumbVisible()) {
					c = Theme.scrollBorderDisabledColor[Theme.style].getColor();
				}
				else {
					c = Theme.scrollBorderColor[Theme.style].getColor();
				}
				g.setColor(c);
				g.drawRect(0, 1, 16, 15);
				
				// edges - blend borderColor with lightColor
				g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), TinyScrollBarUI.alpha));				
				g.drawLine(1, 2, 1, 2);
				g.drawLine(15, 2, 15, 2);
				g.drawLine(1, 15, 1, 15);
				g.drawLine(15, 15, 15, 15);
				
				// blend lightColor with borderColor
				g.setColor(lightAlpha);				
				g.drawLine(0, 1, 0, 1);
				g.drawLine(16, 1, 16, 1);
				g.drawLine(0, 16, 0, 16);
				g.drawLine(16, 16, 16, 16);
				break;
		}
	}
	
	private void drawTinyArrow(Graphics g, Dimension size) {
	}
	
	private void drawWinArrow(Graphics g, Dimension size) {
		int x = 4;
		
		if(getModel().isPressed() && scrollbarUI.isThumbVisible()) {
			x = 5;
		}

		switch (direction)
		{
			case SwingConstants.NORTH: 
				g.drawLine(x + 3, x + 2, x + 3, x + 2);
				g.drawLine(x + 2, x + 3, x + 4, x + 3);
				g.drawLine(x + 1, x + 4, x + 5, x + 4);
				g.drawLine(x, x + 5, x + 6, x + 5);
				
				if(!scrollbarUI.isThumbVisible()) {
					g.setColor(Color.WHITE);
					g.drawLine(x + 1, x + 6, x + 7, x + 6);
				}
				break; 
			case SwingConstants.SOUTH:
				g.drawLine(x, x + 2, x + 6, x + 2);
				g.drawLine(x + 1, x + 3, x + 5, x + 3);
				g.drawLine(x + 2, x + 4, x + 4, x + 4);
				g.drawLine(x + 3, x + 5, x + 3, x + 5);
				if(!scrollbarUI.isThumbVisible()) {
					g.setColor(Color.WHITE);
					g.drawLine(x + 4, x + 6, x + 4, x + 6);
					g.drawLine(x + 4, x + 5, x + 5, x + 5);
					g.drawLine(x + 5, x + 4, x + 6, x + 4);
					g.drawLine(x + 6, x + 3, x + 7, x + 3);
				}
				break;
			case SwingConstants.EAST:
				g.drawLine(x + 5, x + 3, x + 5, x + 3);
				g.drawLine(x + 4, x + 2, x + 4, x + 4);
				g.drawLine(x + 3, x + 1, x + 3, x + 5);
				g.drawLine(x + 2, x, x + 2, x + 6);
				
				if(!scrollbarUI.isThumbVisible()) {
					g.setColor(Color.WHITE);
					g.drawLine(x + 3, x + 7, x + 3, x + 7);
					g.drawLine(x + 3, x + 6, x + 4, x + 6);
					g.drawLine(x + 4, x + 5, x + 5, x + 5);
					g.drawLine(x + 5, x + 4, x + 6, x + 4);
				}
				break;
			case SwingConstants.WEST:
				g.drawLine(x + 1, x + 3, x + 1, x + 3);
				g.drawLine(x + 2, x + 2, x + 2, x + 4);
				g.drawLine(x + 3, x + 1, x + 3, x + 5);
				g.drawLine(x + 4, x, x + 4, x + 6);
				
				if(!scrollbarUI.isThumbVisible()) {
					g.setColor(Color.WHITE);
					g.drawLine(x + 5, x + 1, x + 5, x + 7);
				}
				break;
		}
	}
	
	private void drawXpArrow(Graphics g, Dimension size) {
		switch (direction)
		{
			case SwingConstants.NORTH:
				g.drawLine(8, 5, 8, 5);
				g.drawLine(7, 6, 9, 6);
				g.drawLine(6, 7, 10, 7);
				g.drawLine(5, 8, 7, 8);
				g.drawLine(9, 8, 11, 8);
				g.drawLine(4, 9, 6, 9);
				g.drawLine(10, 9, 12, 9);
				g.drawLine(5, 10, 5, 10);
				g.drawLine(11, 10, 11, 10);
				break; 
			case SwingConstants.SOUTH:
				g.drawLine(5, 6, 5, 6);
				g.drawLine(11, 6, 11, 6);
				g.drawLine(4, 7, 6, 7);
				g.drawLine(10, 7, 12, 7);
				g.drawLine(5, 8, 7, 8);
				g.drawLine(9, 8, 11, 8);
				g.drawLine(6, 9, 10, 9);
				g.drawLine(7, 10, 9, 10);
				g.drawLine(8, 11, 8, 11);
				break;
			case SwingConstants.EAST:
				g.drawLine(6, 5, 6, 5);
				g.drawLine(6, 11, 6, 11);
				g.drawLine(7, 4, 7, 6);
				g.drawLine(7, 10, 7, 12);
				g.drawLine(8, 5, 8, 7);
				g.drawLine(8, 9, 8, 11);
				g.drawLine(9, 6, 9, 10);
				g.drawLine(10, 7, 10, 9);
				g.drawLine(11, 8, 11, 8);
				break;
			case SwingConstants.WEST:
				g.drawLine(4, 8, 4, 8);
				g.drawLine(5, 7, 5, 9);
				g.drawLine(6, 6, 6, 10);
				g.drawLine(7, 5, 7, 7);
				g.drawLine(7, 9, 7, 11);
				g.drawLine(8, 4, 8, 6);
				g.drawLine(8, 10, 8, 12);
				g.drawLine(9, 5, 9, 5);
				g.drawLine(9, 11, 9, 11);
				break;
		}
	}

	/**
	 * Returns the preferred size of the component wich is the size of the skin
	 * @see java.awt.Component#getPreferredSize()
	 */
	public Dimension getPreferredSize() {
		return size[Theme.derivedStyle[Theme.style]];
	}
	
	/*
	 * ScrollButtonKey is used as key in the cache HashMap.
	 * Overrides equals() and hashCode().
	 */
	static class ScrollButtonKey {
		
		private boolean vertical;
		private Color c;
		private boolean pressed;
		private boolean enabled;
		private boolean rollover;
		
		ScrollButtonKey(boolean vertical, Color c,
			boolean pressed, boolean enabled, boolean rollover)
		{
			this.vertical = vertical;
			this.c = c;
			this.pressed = pressed;
			this.enabled = enabled;
			this.rollover = rollover;
		}
		
		public boolean equals(Object o) {
			if(o == null) return false;
			if(!(o instanceof ScrollButtonKey)) return false;

			ScrollButtonKey other = (ScrollButtonKey)o;
			
			return vertical == other.vertical &&
				pressed == other.pressed &&
				enabled == other.enabled &&
				rollover == other.rollover &&
				c.equals(other.c);
		}
		
		public int hashCode() {
			return c.hashCode() *
				(pressed ? 1 : 2) *
				(enabled ? 4 : 8) *
				(rollover ? 16 : 32) *
				(vertical ? 64 : 128);
		}
	}
}