package de.juwimm.cms.client.model;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.DataListItem;

public class ServiceDataListItem extends DataListItem {
	AppEvent< ? > event;

	public ServiceDataListItem(String mandantenService) {
		super(mandantenService);
	}

	public void setSelectionEvent(AppEvent< ? > event) {
		this.event = event;
	}

	public void fireSelectionEvent() {
		if (event != null) {
			Log.debug(ServiceDataListItem.class.getName() + "-fireSelectionEvent()");

			Dispatcher.forwardEvent(event);
		}
	}

}
