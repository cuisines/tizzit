package de.juwimm.cms.gui.ribbon;

import java.awt.Component;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jvnet.flamingo.ribbon.JRibbon;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class Ribbon extends JRibbon {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see org.jvnet.flamingo.ribbon.JRibbon#updateUI()
	 */
	@Override
	public void updateUI() {
		if (UIManager.get(getUIClassID()) != null) {
			setUI(UIManager.getUI(this));
		} else {
			setUI(new RibbonUI());
		}
		for (Component comp : this.getTaskbarComponents()) {
			SwingUtilities.updateComponentTreeUI(comp);
		}
	}
}
