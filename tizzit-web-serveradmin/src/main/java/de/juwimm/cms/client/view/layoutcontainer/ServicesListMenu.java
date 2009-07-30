package de.juwimm.cms.client.view.layoutcontainer;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.DataList;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;

import de.juwimm.cms.client.model.ServiceDataListItem;
import de.juwimm.cms.client.util.AppEvents;
import de.juwimm.cms.client.util.Constants;

public class ServicesListMenu extends ContentPanel {
	private DataList servicesList;
	private ServiceDataListItem mandantenService;

	public ServicesListMenu() {
		initializeList();
	}

	private void initializeList() {
		this.setStyleName(ServicesListMenu.class.getName());
		this.setLayout(new FlowLayout());
		this.setHeading(Constants.I18N_CONSTANTS.serviceListMenu_head());
		servicesList = new DataList();
		servicesList.setStyleName(Constants.STYLE_NAME_SERVICELIST);

		initializeDataListItems();

		servicesList.addListener(Events.SelectionChange, new Listener<BaseEvent>() {

			public void handleEvent(BaseEvent be) {
				((ServiceDataListItem) servicesList.getSelectedItem()).fireSelectionEvent();
			}

		});

		this.add(servicesList);
	}

	private void initializeDataListItems() {
		mandantenService = new ServiceDataListItem(Constants.I18N_CONSTANTS.mandantenService());
		mandantenService.setStyleName(Constants.STYLE_NAME_MANDANTENSERVICE_ITEM);
		AppEvent<Void> event = new AppEvent<Void>(AppEvents.MANDATENSERVICE_INIT);
		mandantenService.setSelectionEvent(event);
		servicesList.add(mandantenService);
	}
}
