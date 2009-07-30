package de.juwimm.cms.client.view.layoutcontainer;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

import de.juwimm.cms.client.util.Constants;

public class Header extends LayoutContainer {
	public Header() {
		this.setStyleName(Constants.HEADER);
		this.setLayout(new FitLayout());
	}
}
