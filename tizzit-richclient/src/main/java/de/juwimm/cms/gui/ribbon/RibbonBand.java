package de.juwimm.cms.gui.ribbon;

import javax.swing.UIManager;

import org.jvnet.flamingo.common.icon.ResizableIcon;
import org.jvnet.flamingo.ribbon.JRibbonBand;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class RibbonBand extends JRibbonBand {

	private static final long serialVersionUID = -6028483220418832347L;

	/**
	 * @param title
	 * @param icon
	 */
	public RibbonBand(String title, ResizableIcon icon) {
		super(title, icon);		
	}
	
	/* (non-Javadoc)
	 * @see org.jvnet.flamingo.ribbon.AbstractRibbonBand#updateUI()
	 */
	@Override
	public void updateUI() {
		if (UIManager.get(getUIClassID()) != null) {
			setUI((RibbonBandUI) UIManager.getUI(this));
		} else {
			setUI(new RibbonBandUI());
		}
	}

}
