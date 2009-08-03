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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.*;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

import de.muntjak.tinylookandfeel.controlpanel.*;

/**
 * TinyScrollBarUI
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class TinyScrollBarUI extends BasicScrollBarUI {
	
	static final int alpha = 92;	// 255 is full opaque

	/** true if thumb is in rollover state */
	protected boolean isRollover=false;
	/** true if thumb was in rollover state */
	protected boolean wasRollover=false;

	/**
	 * The free standing property of this scrollbar UI delegate.
	 */
	private boolean freeStanding = false;

	private int scrollBarWidth;
		
	public TinyScrollBarUI() {}

	/**
	 * Installs some default values.
	 */
	protected void installDefaults() {
		scrollBarWidth = TinyScrollButton.size[Theme.derivedStyle[Theme.style]].width;
		super.installDefaults();
		scrollbar.setBorder(null);
		minimumThumbSize = new Dimension(17, 17);
	}
	
	protected Dimension getMaximumThumbSize()	{ 
		return maximumThumbSize;
    }

	/**
	 * Creates the UI delegate for the given component.
	 *
	 * @param mainColor The component to create its UI delegate.
	 * @return The UI delegate for the given component.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new TinyScrollBarUI();
	}

	/**
	 * Creates the decrease button of the scrollbar.
	 *
	 * @param orientation The button's orientation.
	 * @return The created button.
	 */
	protected JButton createDecreaseButton(int orientation) {
		return new TinyScrollButton(orientation,  this);
	}

	/**
	 * Creates the increase button of the scrollbar.
	 *
	 * @param orientation The button's orientation.
	 * @return The created button.
	 */
	protected JButton createIncreaseButton(int orientation) {
		return new TinyScrollButton(orientation,  this);
	}

	/// From MetalUI
	public Dimension getPreferredSize(JComponent c) {
		if(scrollbar.getOrientation() == JScrollBar.VERTICAL) {
			return new Dimension(scrollBarWidth, scrollBarWidth * 3 + 10);
		} else // Horizontal
		{
			return new Dimension(scrollBarWidth * 3 + 10, scrollBarWidth);
		}

	}
	
	public void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {	
		// borders depend on the scrollbar's style
		switch(Theme.derivedStyle[Theme.style]) {
			case Theme.TINY_STYLE:
				drawTinyTrack(g, trackBounds);
				break;
			case Theme.W99_STYLE:
				drawWinTrack(g, trackBounds);
				break;
			case Theme.YQ_STYLE:
				drawXpTrack(g, trackBounds);
				break;
		}
	}
	
	private void drawTinyTrack(Graphics g, Rectangle t) {
		if(isThumbVisible()) {
			g.setColor(Theme.scrollTrackColor[Theme.style].getColor());
		}
		else {
			g.setColor(Theme.scrollTrackDisabledColor[Theme.style].getColor());
		}
		
		g.fillRect(t.x, t.y,  t.width, t.height);
		
		g.setColor(Color.BLACK);
		if(scrollbar.getOrientation() == JScrollBar.VERTICAL) {
			// links
			g.drawLine(t.x, t.y, t.x, t.y + t.height - 1);
		}
		else {
			// oben
			g.drawLine(t.x, t.y, t.x + t.width - 1, t.y);
		}
			
		if(!isThumbVisible()) return;
		
		if(scrollbar.getOrientation() == JScrollBar.VERTICAL) {
			g.setColor(ColorRoutines.darken(Theme.scrollTrackColor[Theme.style].getColor(), 30));
			g.drawLine(t.x + 1, t.y + 1, t.x + t.width - 3, t.y + 1);
			g.drawLine(t.x + 1, t.y + 1, t.x + 1, t.y + t.height - 1);
		
			g.setColor(ColorRoutines.darken(Theme.scrollTrackColor[Theme.style].getColor(), 20));
			g.drawLine(t.x + 2, t.y + 2, t.x + t.width - 4, t.y + 2);
			g.drawLine(t.x + 2, t.y + 2, t.x + 2, t.y + t.height - 1);
		
			g.setColor(ColorRoutines.lighten(Theme.scrollTrackColor[Theme.style].getColor(), 40));
			g.drawLine(t.x + t.width - 2, t.y + 1, t.x + t.width - 2, t.y + t.height - 1);
		
			g.setColor(ColorRoutines.lighten(Theme.scrollTrackColor[Theme.style].getColor(), 20));
			g.drawLine(t.x + t.width - 3, t.y + 2, t.x + t.width - 3, t.y + t.height - 1);
		}
		else {
			g.setColor(ColorRoutines.darken(Theme.scrollTrackColor[Theme.style].getColor(), 30));
			g.drawLine(t.x + 1, t.y + 1, t.x + t.width - 1, t.y + 1);
			g.drawLine(t.x + 1, t.y + 1, t.x + 1, t.y + t.height - 3);
		
			g.setColor(ColorRoutines.darken(Theme.scrollTrackColor[Theme.style].getColor(), 20));
			g.drawLine(t.x + 2, t.y + 2, t.x + t.width - 1, t.y + 2);
			g.drawLine(t.x + 2, t.y + 2, t.x + 2, t.y + t.height - 4);
		
			g.setColor(ColorRoutines.lighten(Theme.scrollTrackColor[Theme.style].getColor(), 40));
			g.drawLine(t.x + 1, t.y + t.height - 2, t.x + t.width - 1, t.y + t.height - 2);
		
			g.setColor(ColorRoutines.lighten(Theme.scrollTrackColor[Theme.style].getColor(), 20));
			g.drawLine(t.x + 2, t.y + t.height - 3, t.x + t.width - 1, t.y + t.height - 3);
		}
		
	}
	
	private void drawWinTrack(Graphics g, Rectangle trackBounds) {
		if(isThumbVisible()) {
			g.setColor(Theme.scrollTrackColor[Theme.style].getColor());
		}
		else {
			g.setColor(Theme.scrollTrackDisabledColor[Theme.style].getColor());
		}
		
		g.fillRect(trackBounds.x, trackBounds.y,  trackBounds.width, trackBounds.height);
		
		// no border
	}
	
	private void drawXpTrack(Graphics g, Rectangle t) {
		if(isThumbVisible()) {
			g.setColor(Theme.scrollTrackColor[Theme.style].getColor());
		}
		else {
			g.setColor(Theme.scrollTrackDisabledColor[Theme.style].getColor());
		}
		
		g.fillRect(t.x, t.y,  t.width, t.height);
		
		if(isThumbVisible()) {
			g.setColor(Theme.scrollTrackBorderColor[Theme.style].getColor());
		}
		else {
			g.setColor(Theme.scrollTrackBorderDisabledColor[Theme.style].getColor());
		}

		if(scrollbar.getOrientation() == JScrollBar.VERTICAL) {
			g.drawLine(t.x, t.y, t.x, t.y + t.height - 1);
			g.drawLine(t.x + t.width - 1, t.y, t.x + t.width - 1, t.y + t.height - 1);
		}
		else {
			g.drawLine(t.x, t.y, t.x + t.width - 1, t.y);
			g.drawLine(t.x, t.y + t.height - 1, t.x + t.width - 1, t.y + t.height - 1);
		}
	}
	
	public void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
		switch(Theme.derivedStyle[Theme.style]) {
			case Theme.TINY_STYLE:
				drawTinyThumb(g, thumbBounds);
				break;
			case Theme.W99_STYLE:
				drawWinThumb(g, thumbBounds);
				break;
			case Theme.YQ_STYLE:
				drawXpThumb(g, thumbBounds);
				break;
		}
	}
	
	private void drawTinyThumb(Graphics g, Rectangle t) {
	}
	
	private void drawWinThumb(Graphics g, Rectangle t) {
		if(isDragging)
			g.setColor(Theme.scrollThumbPressedColor[Theme.style].getColor());
		else if(isRollover && Theme.scrollRollover[Theme.style])
			g.setColor(Theme.scrollThumbRolloverColor[Theme.style].getColor());
		else
			g.setColor(Theme.scrollThumbColor[Theme.style].getColor());
			
		int x2 = t.x + t.width;
		int y2 = t.y + t.height;
			
		g.fillRect(t.x , t.y,  t.width - 1, t.height - 1);
		
		g.setColor(Theme.scrollLightColor[Theme.style].getColor());
		g.drawLine(t.x + 1, t.y + 1,  x2 - 3, t.y + 1);
		g.drawLine(t.x + 1, t.y + 1,  t.x + 1, y2 - 3);
		
		g.setColor(Theme.scrollDarkColor[Theme.style].getColor());
		g.drawLine(x2 - 2, t.y + 1, x2 - 2, y2 - 3);
		g.drawLine(t.x + 1, y2 - 2, x2 - 2, y2 - 2);
		
		g.setColor(Theme.scrollBorderColor[Theme.style].getColor());
		g.drawLine(x2 - 1, t.y, x2 - 1, y2 - 2);
		g.drawLine(t.x, y2 - 1, x2 - 1, y2 - 1);
	}
	
	private void drawXpThumb(Graphics g, Rectangle t) {
		Color c = null;
		if(isDragging && isRollover) {
			c = Theme.scrollThumbPressedColor[Theme.style].getColor();
		}
		else if(isRollover && Theme.scrollRollover[Theme.style]) {
			c = Theme.scrollThumbRolloverColor[Theme.style].getColor();
		}
		else {
			c = Theme.scrollThumbColor[Theme.style].getColor();
		}

		g.setColor(c);

		int x2 = t.x + t.width - 1;
		int y2 = t.y + t.height - 1;
		
		int spread1 = Theme.scrollSpreadLight[Theme.style];
		int spread2 = Theme.scrollSpreadDark[Theme.style];
		
		int h = 15;
		float spreadStep1 = 10.0f * spread1 / 10;
		float spreadStep2 = 10.0f * spread2 / 10;
		int halfY = h / 2;
		int yd;

		switch (scrollbar.getOrientation()) {
			case JScrollBar.VERTICAL:
				for(int y = 1; y < h; y++) {
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
			
					g.drawLine(t.x + y, t.y + 2, t.x + y, y2 - 1);
				}

				g.setColor(Theme.scrollLightColor[Theme.style].getColor());
				g.drawLine(t.x + 3, t.y + 1, t.x + 14, t.y + 1);
				g.drawLine(t.x + 15, t.y + 2, t.x + 15, y2 - 2);
				
				g.setColor(Theme.scrollBorderColor[Theme.style].getColor());
				g.drawRect(t.x + 1, t.y, 15, y2 - t.y);

				// edges - blend borderColor with lightColor
				Color a = Theme.scrollBorderColor[Theme.style].getColor();
				g.setColor(new Color(a.getRed(), a.getGreen(), a.getBlue(), alpha));				
				g.drawLine(t.x + 2, t.y + 1, t.x + 2, t.y + 1);
				g.drawLine(t.x + 15, t.y + 1, t.x + 15, t.y + 1);
				g.drawLine(t.x + 2, y2 - 1, t.x + 2, y2 - 1);
				g.drawLine(t.x + 15, y2 - 1, t.x + 15, y2 - 1);
				
				// blend lightColor with borderColor
				a = Theme.scrollLightColor[Theme.style].getColor();
				g.setColor(new Color(a.getRed(), a.getGreen(), a.getBlue(), alpha));
				g.drawLine(t.x + 1, t.y, t.x + 1, t.y);
				g.drawLine(t.x + 16, t.y, t.x + 16, t.y);
				g.drawLine(t.x + 1, y2, t.x + 1, y2);
				g.drawLine(t.x + 16, y2, t.x + 16, y2);
				break;
			case JScrollBar.HORIZONTAL:
				for(int y = 1; y < h; y++) {
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
			
					g.drawLine(t.x + 1, t.y + y, x2 - 2, t.y + y);
				}
				
				g.setColor(Theme.scrollLightColor[Theme.style].getColor());
				g.drawLine(t.x + 2, t.y + 15, x2 - 2, t.y + 15);
				g.drawLine(x2 - 1, t.y + 3, x2 - 1, t.y + 14);
				
				g.setColor(Theme.scrollBorderColor[Theme.style].getColor());
				g.drawRect(t.x, t.y + 1, x2 - t.x, 15);
				
				g.setColor(Theme.scrollTrackBorderColor[Theme.style].getColor());
				g.drawLine(t.x, t.y, x2, t.y);
				
				// edges - blend borderColor with lightColor
				a = Theme.scrollBorderColor[Theme.style].getColor();
				g.setColor(new Color(a.getRed(), a.getGreen(), a.getBlue(), alpha));				
				g.drawLine(t.x + 1, t.y + 2, t.x + 1, t.y + 2);
				g.drawLine(t.x + 1, t.y + 15, t.x + 1, t.y + 15);
				g.drawLine(x2 - 1, t.y + 2, x2 - 1, t.y + 2);
				g.drawLine(x2 - 1, t.y + 15, x2 - 1, t.y + 15);
				
				// blend lightColor with borderColor
				a = Theme.scrollLightColor[Theme.style].getColor();
				g.setColor(new Color(a.getRed(), a.getGreen(), a.getBlue(), alpha));
				g.drawLine(t.x, t.y + 1, t.x, t.y + 1);
				g.drawLine(t.x, t.y + 16, t.x, t.y + 16);
				g.drawLine(x2, t.y + 1, x2, t.y + 1);
				g.drawLine(x2, t.y + 16, x2, t.y + 16);
				break;
		}
		
		// draw Grip
		if(t.height < 11) return;
		
		if(scrollbar.getOrientation() == JScrollBar.VERTICAL) {
			int y1 = t.y + (t.height) / 2 - 4;
			y2 = Math.min(y1 + 8, t.y + t.height - 5);
			
			int y = y1;
			// we take only saturation & brightness and apply them
			// to the background color (normal/rollover/pressed)
			g.setColor(SBChooser.getAdjustedColor(c,
				Theme.scrollGripLightColor[Theme.style].getSaturation(),
				Theme.scrollGripLightColor[Theme.style].getBrightness()));
			while(y < y2) {
				g.drawLine(5, y, 11, y);
				y += 2;
			}
			
			y = y1 + 1;
			g.setColor(SBChooser.getAdjustedColor(c,
				Theme.scrollGripDarkColor[Theme.style].getSaturation(),
				Theme.scrollGripDarkColor[Theme.style].getBrightness()));
			while(y < y2) {
				g.drawLine(6, y, 12, y);
				y += 2;
			}
		}
		else {
			int x1 = t.x + (t.width) / 2 - 4;
			x2 = Math.min(x1 + 8, t.x + t.width - 5);
			
			int x = x1 + 1;
			// we take only saturation & brightness and apply them
			// to the background color (normal/rollover/pressed)
			g.setColor(SBChooser.getAdjustedColor(c,
				Theme.scrollGripLightColor[Theme.style].getSaturation(),
				Theme.scrollGripLightColor[Theme.style].getBrightness()));
			while(x < x2) {
				g.drawLine(x, 5, x, 11);
				x += 2;
			}
			
			x = x1;
			g.setColor(SBChooser.getAdjustedColor(c,
				Theme.scrollGripDarkColor[Theme.style].getSaturation(),
				Theme.scrollGripDarkColor[Theme.style].getBrightness()));
			while(x < x2) {
				g.drawLine(x, 6, x, 12);
				x += 2;
			}
		}
	}

	public boolean isThumbVisible() {
		if(scrollbar.getOrientation() == JScrollBar.VERTICAL) {
			return getThumbBounds().height > 0;
		} else {
			return getThumbBounds().width > 0;
		}
	}

	// From BasicUI
    protected TrackListener createTrackListener(){
		return new MyTrackListener();
    }

	/**
	 * Basically does BasicScrollBarUI.TrackListener the right job, it just needs
	 * an additional repaint and rollover management
	 */
	protected class MyTrackListener extends BasicScrollBarUI.TrackListener {
		public void mouseReleased(MouseEvent e) {
			super.mouseReleased(e);
			scrollbar.repaint();
		}

		public void mousePressed(MouseEvent e) {
			super.mousePressed(e);
			scrollbar.repaint();
		}
		public void mouseEntered(MouseEvent e) {
			isRollover=false;
			wasRollover=false;
		    if(getThumbBounds().contains(e.getPoint())) {
		    	isRollover = true;
		    	wasRollover = isRollover;
		    	scrollbar.repaint();
		    }
		}
		public void mouseExited(MouseEvent e) {
			isRollover=false;
	    	if (isRollover != wasRollover) {
		    	wasRollover = isRollover;
		    	scrollbar.repaint();
	    	}
		}
		public void mouseDragged(MouseEvent e) {
		    if(getThumbBounds().contains(e.getPoint())) {
		    	isDragging=true;
		    }
			super.mouseDragged(e);
		}
		public void mouseMoved(MouseEvent e) {
		    if(getThumbBounds().contains(e.getPoint())) {
		    	isRollover=true;
		    	if (isRollover != wasRollover) {
			    	scrollbar.repaint();
			    	wasRollover = isRollover;
		    	}
		    } else {
		    	isRollover=false;
		    	if (isRollover != wasRollover) {
			    	scrollbar.repaint();
			    	wasRollover = isRollover;
		    	}
		    }
		}
	}

	protected class OrientationChangeListener implements PropertyChangeListener {
		
		public void propertyChange(PropertyChangeEvent e) {
			Integer orient = (Integer)e.getNewValue();

            if (scrollbar.getComponentOrientation().isLeftToRight()) { 
                if (incrButton instanceof TinyScrollButton) {
                    ((TinyScrollButton)incrButton).setDirection(orient.intValue() == HORIZONTAL?
                                                            EAST : SOUTH);
                }
                if (decrButton instanceof TinyScrollButton) {
                    ((TinyScrollButton)decrButton).setDirection(orient.intValue() == HORIZONTAL?
                                                            WEST : NORTH);
                }
            } else {
                if (incrButton instanceof TinyScrollButton) {
                    ((TinyScrollButton)incrButton).setDirection(orient.intValue() == HORIZONTAL?
                                                            WEST : SOUTH);
                }
                if (decrButton instanceof TinyScrollButton) {
                    ((TinyScrollButton)decrButton).setDirection(orient.intValue() == HORIZONTAL?
                                                            EAST : NORTH);
                }
            }
		}
	}
}