package de.juwimm.cms.client.util;

import com.google.gwt.core.client.GWT;

import de.juwimm.cms.client.I18NConstants;
import de.juwimm.cms.client.I18NMessages;

public final class Constants {

	/**
	 *  GWT in Action p. 503: referencing constants in a Resource file
	 *  MyConstants constants = (MyConstants) GWT.create(MyConstants.class)
	 *  Label newLabel = new Label(constants.labelText());
	 *  
	 *  to this only once per client, do it here
	 */
	public static final I18NConstants I18N_CONSTANTS = ((I18NConstants) GWT.create(I18NConstants.class));

	public static final I18NMessages I18N_MESSAGES = ((I18NMessages) GWT.create(I18NMessages.class));

	public static final String MAINPANEL = "mainPanel";
	
	public static final String STYLE_NAME_SERVICELIST = "serviceList";
	public static final String STYLE_NAME_MANDANTENSERVICE_ITEM = "mandantenSeviceListItem";
	public static final String CONQUESTADMINISTRATIONMAINPANEL="conquestAdministrationMainPanel";
	public static final String CONQUESTADMINISTRATIONMAINCONTENTPANEL="conquestAdministrationMainContentPanel";
	public static final String FOOTER="footer";
	public static final String HEADER="header";
}