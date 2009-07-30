package de.juwimm.cms.client.view.layoutcontainer;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

import de.juwimm.cms.client.util.Constants;

public class MainContentTabPanel extends TabPanel {
	private static MainContentTabPanel instance;

	public static MainContentTabPanel getInstance() {
		if (instance == null) {
			instance = new MainContentTabPanel();
		}
		return instance;
	}

	private MainContentTabPanel() {
		this.setStyleName(Constants.CONQUESTADMINISTRATIONMAINCONTENTPANEL);
		this.setLayout(new FitLayout());
		this.layoutOnChange=true;
	}

	public void addTab(LayoutContainer item, String displayName) {
		TabItem newTab = new TabItem(displayName);
		newTab.add(item);
		newTab.setLayout(new FitLayout());
		this.add(newTab);
		
	}
}
