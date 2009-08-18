package de.juwimm.cms.gui.ribbon;

import javax.swing.UIManager;

import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.common.icon.ResizableIcon;


/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class CommandButton extends JCommandButton{

	private static final long serialVersionUID = 1750079748505618927L;

	public CommandButton(String title, ResizableIcon icon) {
		super(title, icon);
	}
	
	@Override
	public void updateUI() {
		if (UIManager.get(getUIClassID()) != null) {
			setUI((CommandButtonUI) UIManager.getUI(this));
		} else {
			setUI(CommandButtonUI.createUI(this));
		}
	}
	
}