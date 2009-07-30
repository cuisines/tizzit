package de.juwimm.cms.client.view.layoutcontainer;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;

import de.juwimm.cms.client.util.Constants;

public class Footer extends LayoutContainer {
	
	public Footer(){
		this.setStyleName(Constants.FOOTER);
		this.setLayout(new FlowLayout());
	}
}
