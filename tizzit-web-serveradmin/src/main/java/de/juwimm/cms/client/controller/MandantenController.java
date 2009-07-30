package de.juwimm.cms.client.controller;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;

import de.juwimm.cms.client.util.AppEvents;
import de.juwimm.cms.client.view.MandantenView;

public class MandantenController extends Controller {
	private MandantenView view;

	public MandantenController() {
		Log.debug(MandantenController.class.getName() + "-AppEvents.MANDATENSERVICE_INIT");
		registerEventTypes(AppEvents.MANDATENSERVICE_INIT);
	}

	@Override
	public void handleEvent(AppEvent< ? > event) {
		switch (event.type) {
			case AppEvents.MANDATENSERVICE_INIT:
				if (view == null) {
					view = new MandantenView(this);
				}
				forwardToView(view, event);
		}
	}

}
