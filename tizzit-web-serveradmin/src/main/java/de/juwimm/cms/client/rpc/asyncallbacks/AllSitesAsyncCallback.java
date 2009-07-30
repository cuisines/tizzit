package de.juwimm.cms.client.rpc.asyncallbacks;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.Popup;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.juwimm.cms.client.util.AppEvents;
import de.juwimm.cms.vo.SiteValue;

/**
 * 
 * the intention is to seperate the AsyncCallback from the code, where it is used
 * 
 */
public class AllSitesAsyncCallback implements AsyncCallback<SiteValue[]> {
	boolean failure = false;
	private CallbackRevenge<SiteValue[]> callbackRevenge;

	public AllSitesAsyncCallback(CallbackRevenge<SiteValue[]> callbackRevenge) {
		this.callbackRevenge = callbackRevenge;
	}

	public boolean isFailure() {
		return failure;
	}

	public void onFailure(Throwable failure) {
		Log.error("Could not resolve Sites");
		Popup errorPopup = new Popup();
//		HTMLPanel
//		failure.printStackTrace();
		//			Window.alert(Constants.I18N_MESSAGES.error_budgeting()); 

		Dispatcher.forwardEvent(AppEvents.ENABLE_WIDGETS);
	}

	public void onSuccess(SiteValue[] siteValues) {
		Log.info(siteValues.length + " Found sites.");
		if (siteValues != null) {
			if (callbackRevenge != null) {
				callbackRevenge.callback(siteValues, this.getClass().getName());
			}
		}
		//		Dispatcher.forwardEvent(AppEvents.ENABLE_WIDGETS);
	}
}