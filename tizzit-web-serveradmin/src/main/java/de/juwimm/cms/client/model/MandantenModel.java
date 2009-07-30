package de.juwimm.cms.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;

public class MandantenModel extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String SITE_ID = "site_id";
	public static String SITE_SHORT = "site_short";
	public static String SITE_NAME = "site_Name";
	public static String START_SEARCH = "startSearch";
	

	public MandantenModel(int siteId, String siteShort, String siteName) {
		set(SITE_ID, siteId);
		set(SITE_SHORT, siteShort);
		set(SITE_NAME, siteName);
		set(START_SEARCH, false);
	}
}
