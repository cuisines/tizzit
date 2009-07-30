package de.juwimm.cms.client.view;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.ui.RootPanel;

import de.juwimm.cms.client.util.AppEvents;
import de.juwimm.cms.client.util.Constants;
import de.juwimm.cms.client.view.layoutcontainer.Footer;
import de.juwimm.cms.client.view.layoutcontainer.Header;
import de.juwimm.cms.client.view.layoutcontainer.MainContentTabPanel;
import de.juwimm.cms.client.view.layoutcontainer.ServicesListMenu;

public class ConquestAdministrationMainView extends View {
	private Viewport conquestAdministrationMainPanel;
	private Header header;
	private Footer footer;
	private ServicesListMenu servicesMenu;
	private MainContentTabPanel mainContentPanel;

	public ConquestAdministrationMainView(Controller controller) {
		super(controller);
	}

	@Override
	protected void handleEvent(AppEvent< ? > event) {
		switch (event.type) {
			case AppEvents.Init:
				break;
		}
	}

	@Override
	protected void initialize() {
		RootPanel root = RootPanel.get(Constants.MAINPANEL);
		conquestAdministrationMainPanel = new Viewport();
		conquestAdministrationMainPanel.setStyleName(Constants.CONQUESTADMINISTRATIONMAINPANEL);

		BorderLayout borderLayout = new BorderLayout();
		conquestAdministrationMainPanel.setLayout(borderLayout);

		createHeader();
		createFooter();
		createServiceMenu();
		createContentCenter();

		conquestAdministrationMainPanel.setMonitorWindowResize(true);
		root.add(conquestAdministrationMainPanel);
	}

	private void createFooter() {
		footer = new Footer();
		footer.setBorders(true);
		BorderLayoutData headerData = new BorderLayoutData(LayoutRegion.SOUTH,40);
		conquestAdministrationMainPanel.add(footer, headerData);
	}

	private void createContentCenter() {
		mainContentPanel = MainContentTabPanel.getInstance();
		mainContentPanel.setBorders(true);
		BorderLayoutData headerData = new BorderLayoutData(LayoutRegion.CENTER);
		conquestAdministrationMainPanel.add(mainContentPanel, headerData);
	}

	private void createServiceMenu() {
		servicesMenu = new ServicesListMenu();
		servicesMenu.setBorders(true);
		BorderLayoutData headerData = new BorderLayoutData(LayoutRegion.WEST);
		conquestAdministrationMainPanel.add(servicesMenu, headerData);
	}

	private void createHeader() {
		header = new Header();
		header.setBorders(true);
		BorderLayoutData headerData = new BorderLayoutData(LayoutRegion.NORTH,60);
		header.setHeight(50);
		conquestAdministrationMainPanel.add(header, headerData);
	}
}
