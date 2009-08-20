/**
 * @author rhertzfeldt
 * @lastChange 10:24:04 PM
 */
package de.juwimm.cms.content.panel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.media.jai.PlanarImage;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import com.sun.media.jai.widget.DisplayJAI;

/**
 * @author rhertzfeldt
 *
 */
public class PanDisplayJAIScrollable extends DisplayJAI implements Scrollable, MouseMotionListener{

	private static final long serialVersionUID = 5910342592700583388L;
	private int maxUnitIncrement = 1;
    /**
	 * @param picture
	 * @param width
	 */
	public PanDisplayJAIScrollable(PlanarImage picture, int width) {
		super(picture);
		maxUnitIncrement = width;
		
		setAutoscrolls(true);
        addMouseMotionListener(this);
        addMouseListener(this);
	}

	/**
	 * @return the maxUnitIncrement
	 */
	public int getMaxUnitIncrement() {
		return maxUnitIncrement;
	}

	public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect,
                                          int orientation,
                                          int direction) {
        //Get the current position.
        int currentPosition = 0;
        if (orientation == SwingConstants.HORIZONTAL) {
            currentPosition = visibleRect.x;
        } else {
            currentPosition = visibleRect.y;
        }

        //Return the number of pixels between currentPosition
        //and the nearest tick mark in the indicated direction.
        if (direction < 0) {
            int newPosition = currentPosition -
                             (currentPosition / maxUnitIncrement)
                              * maxUnitIncrement;
            return (newPosition == 0) ? maxUnitIncrement : newPosition;
        } else {
            return ((currentPosition / maxUnitIncrement) + 1)
                   * maxUnitIncrement
                   - currentPosition;
        }
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect,
                                           int orientation,
                                           int direction) {
        if (orientation == SwingConstants.HORIZONTAL) {
            return visibleRect.width - maxUnitIncrement;
        } else {
            return visibleRect.height - maxUnitIncrement;
        }
    }

    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    public void setMaxUnitIncrement(int pixels) {
        maxUnitIncrement = pixels;
    }
	
	public void mouseDragged(MouseEvent e) {
        Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
        scrollRectToVisible(r);
    }
	
	public void mouseMoved(MouseEvent e) {
	}
}
