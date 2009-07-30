package de.juwimm.cms.client.controller;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.util.Theme;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.i18n.client.LocaleInfo;

import de.juwimm.cms.client.util.AppEvents;


public class ConquestAdministrationMain implements EntryPoint {
	
	private Dispatcher dispatcher;

	public void onModuleLoad() {
		Log.debug(ConquestAdministrationMain.class.getName() + "-onModuleLoad");

		// Theme has to be set before any windgets elements are initiated otherwise the gxt-default-blue-theme is set
		GXT.setDefaultTheme(Theme.GRAY, true);
		Log.setCurrentLogLevel(Log.LOG_LEVEL_DEBUG);
		Log.info("LocaleInfo: " + LocaleInfo.getCurrentLocale().getLocaleName());
		/*
		// "de" and "default"
		String[] availableLocaleNames = LocaleInfo.getCurrentLocale().getAvailableLocaleNames();
		for (int i=0; i< availableLocaleNames.length; i++) {
			Log.info("" + availableLocaleNames[i]);
		}
		*/
		startDispatcher();
	}

	private void startDispatcher() {
		Log.debug(ConquestAdministrationMain.class.getName() + "-startDispatcher");
		// Theme bla =  new Theme("novwopadmin", "novwopadmin", "NovartisWopAdministrationView.css");
		dispatcher = Dispatcher.get();
		dispatcher.addController(new ConquestAdministrationMainController());
		Dispatcher.forwardEvent(AppEvents.Init);
	}
}