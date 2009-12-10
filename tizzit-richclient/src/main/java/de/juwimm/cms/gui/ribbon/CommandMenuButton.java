package de.juwimm.cms.gui.ribbon;

import javax.swing.UIManager;

import org.jvnet.flamingo.common.JCommandMenuButton;
import org.jvnet.flamingo.common.icon.ResizableIcon;
import org.jvnet.flamingo.common.ui.CommandButtonUI;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class CommandMenuButton extends JCommandMenuButton {

	private static final long serialVersionUID = 3579993692983889772L;

	public CommandMenuButton(String title, ResizableIcon icon) {
		super(title, icon);
	}

	@Override
	public void updateUI() {
		if (UIManager.get(getUIClassID()) != null) {
			setUI((CommandButtonUI) UIManager.getUI(this));
		} else {
			setUI(CommandMenuButtonUI.createUI(this));
		}
	}

}