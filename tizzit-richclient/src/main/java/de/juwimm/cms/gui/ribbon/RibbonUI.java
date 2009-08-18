package de.juwimm.cms.gui.ribbon;

import java.awt.Graphics;

import org.jvnet.flamingo.ribbon.ui.BasicRibbonUI;


/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class RibbonUI extends BasicRibbonUI {
	@Override
	public int getTaskbarHeight() {
		return 0;
	}
	@Override
	public int getTaskToggleButtonHeight(){
		return 0;
		
	}	
	
	@Override
	protected void paintTaskArea(Graphics g, int x, int y, int width, int height) {
		//do not need to paint task area
	}
	
}
