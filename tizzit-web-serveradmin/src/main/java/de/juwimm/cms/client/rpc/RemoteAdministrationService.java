package de.juwimm.cms.client.rpc;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import de.juwimm.cms.management.remote.AdminServiceSpring;

/**
 */
public interface RemoteAdministrationService extends RemoteService, AdminServiceSpring{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		static final String REMOTECLIENTSERVICE = "RemoteAdministrationService";
		private static RemoteAdministrationServiceAsync instance = null;

		public static RemoteAdministrationServiceAsync getInstance() {
			if (Util.instance == null) {
				Util.instance =  (RemoteAdministrationServiceAsync) GWT.create(RemoteAdministrationService.class);
				ServiceDefTarget target = (ServiceDefTarget) Util.instance;
				boolean isClient = GWT.isClient();
				// hosted mode => isScript = false; see GWT in Action page 369
				boolean isScript = GWT.isScript();
				String serviceEntryPoint = GWT.getModuleBaseURL() + Util.REMOTECLIENTSERVICE;

				Log.debug("isClient: " + isClient);
				Log.debug("isScript: " + isScript);
				Log.debug("serviceEntryPoint: " + serviceEntryPoint);
				/*
				if (!isScript) {
					serviceEntryPoint = "/services/" + Util.REMOTECLIENTSERVICE +".rpc";
				}
				*/
				target.setServiceEntryPoint(serviceEntryPoint);
			}

			return Util.instance;
		}
	}
}