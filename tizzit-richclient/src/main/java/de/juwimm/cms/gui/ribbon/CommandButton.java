package de.juwimm.cms.gui.ribbon;

import javax.swing.UIManager;

import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.common.RichToolTipManager;
import org.jvnet.flamingo.common.RichTooltip;
import org.jvnet.flamingo.common.icon.ResizableIcon;


/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class CommandButton extends JCommandButton{	
	private static final long serialVersionUID = 1750079748505618927L;
	/**
	 * Stores the tooltip for when the button is enabled
	 */
	private RichTooltip enabledToolTip = null; 

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
	
	
	@Override
	public void setEnabled(boolean state) {
		if(enabledToolTip != null){
			if(state == false){				
				super.setActionRichTooltip(null);
			}else{
				this.setActionRichTooltip(enabledToolTip);
			}
		}
		super.setEnabled(state);
	}
	
	
	
	@Override
	public void setActionRichTooltip(RichTooltip richTooltip) {
		enabledToolTip = richTooltip;
		super.setActionRichTooltip(richTooltip);
	}
	
}