package de.juwimm.cms.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.juwimm.cms.beans.vo.LogfileValue;
import de.juwimm.cms.vo.SiteValue;

/**
 * client-Interface for GWT-RPC
 * 
 * client-/server-Interface:	de.juwimm.novartis.wop.client.RemoteClientService
 * 
 * server-impl:		de.juwimm.novartis.wop.client.RemoteClientServiceImpl
 * 
 * GWT-Designer:
 * start here to declare the Method; GWT-Designer then creates the appropriate Method in RemoteClientServiceAsync
 */
public interface RemoteAdministrationServiceAsync {
	/**
	* 
	*/
	public void getSiteList(AsyncCallback<String> callback);

	/**
	 * 
	 */
	public void setLogfileValue(LogfileValue lv, AsyncCallback<Void> callback);

	/**
	 * 
	 */
	public void getLogfileValue(AsyncCallback<LogfileValue> callback);

	/**
	 * 
	 */
	public void startLogfileParsing(AsyncCallback<Void> callback);

	/**
	 * 
	 */
	public void getAllSites(AsyncCallback<SiteValue[]> callback);

	/**
	 * 
	 */
	public void createSite(SiteValue siteValue, AsyncCallback<SiteValue> callback);

	/**
	 * 
	 */
	public void updateSite(SiteValue siteValue, AsyncCallback<Void> callback);

	/**
	 * 
	 */
	public void deleteSite(SiteValue siteValue, AsyncCallback<Void> callback);

	/**
	 * 
	 */
	public void getSiteConfig(SiteValue siteValue, AsyncCallback<String> callback);

	/**
	 * 
	 */
	public void setSiteConfig(SiteValue siteValue, java.lang.String config, AsyncCallback<Void> callback);

	/**
	 * 
	 */
	public void startSearchIndexer(AsyncCallback<Void> callback);

	/**
	 * 
	 */
	public void startSearchIndexer(java.lang.Integer siteId, AsyncCallback<Void> callback);

	/**
	 * 
	 */
	public void startUpdateDocumentUseCount(AsyncCallback<Void> callback);

	/**
	 * 
	 */
	public void startUpdateDocumentUseCount(java.lang.Integer siteId, AsyncCallback<Void> callback);

	/**
	 * 
	 */
	public void startTreeRepair(java.lang.Integer parentId, AsyncCallback<Void> callback);

	/**
	 * 
	 */
	public void importActiveDirectoryPersonData(java.util.Map< ? , ? > properties, AsyncCallback<Void> callback);
}