package de.juwimm.cms.gui.ribbon;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import org.jvnet.flamingo.common.ui.BasicCommandButtonUI;
import org.jvnet.flamingo.utils.FlamingoUtilities;


/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class CommandButtonUI extends BasicCommandButtonUI{	
	@Override
	protected Color getForegroundColor(boolean isTextPaintedEnabled) {
		if (isTextPaintedEnabled) {
			return FlamingoUtilities.getColor(Color.black, "Button.border");
		} else {
			return FlamingoUtilities.getColor(Color.gray,
					"Label.disabledForeground");
		}
	}
	
	public static ComponentUI createUI(JComponent c) {
		return new CommandButtonUI();
	}
}
