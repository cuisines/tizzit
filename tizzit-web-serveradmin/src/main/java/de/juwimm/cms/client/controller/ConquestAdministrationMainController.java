package de.juwimm.cms.client.controller;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.google.gwt.user.client.Window;

import de.juwimm.cms.client.rpc.RemoteAdministrationService;
import de.juwimm.cms.client.rpc.asyncallbacks.AllSitesAsyncCallback;
import de.juwimm.cms.client.rpc.asyncallbacks.CallbackRevenge;
import de.juwimm.cms.client.util.AppEvents;
import de.juwimm.cms.client.view.ConquestAdministrationMainView;
import de.juwimm.cms.vo.SiteValue;

public class ConquestAdministrationMainController extends Controller {
	private ConquestAdministrationMainView view;
	private MandantenController mandantenController;

	public ConquestAdministrationMainController() {
		registerEventTypes(AppEvents.Init);
		registerEventTypes(AppEvents.MANDATENSERVICE_INIT);
	}

	@Override
	public void handleEvent(AppEvent< ? > event) {
		switch (event.type) {
			case AppEvents.Init:
				Log.debug(ConquestAdministrationMainController.class.getName() + "-AppEvents.Init");
				if (view == null) {
					view = new ConquestAdministrationMainView(this);
				}
				CallbackRevenge<SiteValue[]> callbackRevenge = new CallbackRevenge<SiteValue[]>() {

					public void callback(SiteValue[] result, String clazzName) {
						Window.alert("In CallbackRevenge");
					}

				};
				Log.debug("Starting RemoteService Call");

				RemoteAdministrationService.Util.getInstance().getAllSites(new AllSitesAsyncCallback(callbackRevenge));
				forwardToView(view, event);
				break;
			case AppEvents.MANDATENSERVICE_INIT:
				if (mandantenController == null) {
					mandantenController = new MandantenController();
					this.addChild(mandantenController);
				}
				forwardToChild(event);
				break;
		}
	}

}
