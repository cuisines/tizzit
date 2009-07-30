package de.juwimm.cms.server.rpc;

import java.util.Map;

import org.andromda.spring.RemoteServiceLocator;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.juwimm.cms.beans.vo.LogfileValue;
import de.juwimm.cms.client.rpc.RemoteAdministrationService;
import de.juwimm.cms.management.remote.AdminServiceSpring;
import de.juwimm.cms.vo.SiteValue;

/**
 * 
 */
public class RemoteAdministrationServiceImpl extends RemoteServiceServlet implements RemoteAdministrationService {

	private static final long serialVersionUID = 1L;

	public AdminServiceSpring getBean() {
		// in hosted mode webApplicationContext is null
		org.springframework.web.context.WebApplicationContext webApplicationContext = org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		if (webApplicationContext != null) {
			AdminServiceSpring adminService = (AdminServiceSpring) webApplicationContext.getBean("adminServiceSpring");
			Log.info("AdminService ist : "+adminService);
			return adminService;
		} else {
			// for e.g. hosted mode
			RemoteServiceLocator remoteServiceLocator = RemoteServiceLocator.instance();
			AdminServiceSpring adminService = remoteServiceLocator.getAdminServiceSpring();
			Log.info("AdminService ist : "+adminService);
			return adminService;
		}

	}

	public SiteValue createSite(SiteValue siteValue) {
		return getBean().createSite(siteValue);
	}

	public void deleteSite(SiteValue siteValue) {
		getBean().deleteSite(siteValue);
	}

	public SiteValue[] getAllSites() {
		Log.info("Getting All SITES");
		return getBean().getAllSites();
	}

	public LogfileValue getLogfileValue() {
		return getBean().getLogfileValue();
	}

	public String getSiteConfig(SiteValue siteValue) {
		return getBean().getSiteConfig(siteValue);
	}

	public String getSiteList() {
		return getBean().getSiteList();
	}


	@SuppressWarnings("unchecked")
	public void importActiveDirectoryPersonData(Map properties) {
		getBean().importActiveDirectoryPersonData(properties);
	}

	public void setLogfileValue(LogfileValue lv) {
		getBean().setLogfileValue(lv);
	}

	public void setSiteConfig(SiteValue siteValue, String config) {
		getBean().setSiteConfig(siteValue, config);
	}

	public void startLogfileParsing() {
		getBean().startLogfileParsing();
	}

	public void startSearchIndexer() {
		getBean().startSearchIndexer();
	}

	public void startSearchIndexer(Integer siteId) {
		getBean().startSearchIndexer(siteId);
	}

	public void startTreeRepair(Integer parentId) {
		getBean().startTreeRepair(parentId);
	}

	public void startUpdateDocumentUseCount() {
		getBean().startUpdateDocumentUseCount();
	}

	public void startUpdateDocumentUseCount(Integer siteId) {
		getBean().startUpdateDocumentUseCount(siteId);
	}

	public void updateSite(SiteValue siteValue) {
		getBean().updateSite(siteValue);
	}

}