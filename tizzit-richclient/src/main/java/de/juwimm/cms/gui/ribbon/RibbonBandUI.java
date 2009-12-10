package de.juwimm.cms.gui.ribbon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import org.jvnet.flamingo.ribbon.ui.BasicRibbonBandUI;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class RibbonBandUI extends BasicRibbonBandUI {

	/* (non-Javadoc)
	 * @see org.jvnet.flamingo.ribbon.ui.BasicRibbonBandUI#paintBandTitle(java.awt.Graphics, java.awt.Rectangle, java.lang.String)
	 */
	@Override
	protected void paintBandTitle(Graphics g, Rectangle titleRectangle, String title) {
		if (this.isUnderMouse) {
			this.ribbonBand.setForeground(Color.black);
		} else {
			this.ribbonBand.setForeground(Color.black);
		}
		super.paintBandTitle(g, titleRectangle, title);
	}

}
