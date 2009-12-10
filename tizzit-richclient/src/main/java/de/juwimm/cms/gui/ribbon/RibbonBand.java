package de.juwimm.cms.gui.ribbon;

import java.awt.event.ActionListener;

import javax.swing.UIManager;

import org.jvnet.flamingo.common.icon.ResizableIcon;
import org.jvnet.flamingo.ribbon.JRibbonBand;

import de.juwimm.cms.util.UIConstants;

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
		this(title, icon, null);
	}

	/**
	 * Necessary to change the color of the RibbonBand
	 * @param title
	 * @param icon
	 * @param expandActionListener
	 */
	public RibbonBand(String title, ResizableIcon icon, ActionListener expandActionListener) {
		super(title, icon, expandActionListener);
		this.controlPanel.setBackground(UIConstants.backgroundBaseColor);

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
