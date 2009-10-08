/*
 * Copyright (c) 2002-2009 Juwi MacMillan Group GmbH (JuwiMM)
 * Bockhorn 1, 29664 Walsrode, Germany
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of JuwiMM
 * ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with JuwiMM.
 */
package org.tizzit.classloading;

import java.net.URL;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.juwimm.cms.beans.foreign.TizzitPropertiesBeanSpring;

/**
 *
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since tizzit-core 07.10.2009
 */
public class ReloadingClassloaderManager {
	private static final Log log = LogFactory.getLog(ReloadingClassloaderManager.class);

	private static ClassLoader reloadingClassloader = null;

	private ReloadingClassloaderManager() {
		// only allow static usage
	}

	public static synchronized ClassLoader getClassLoader(TizzitPropertiesBeanSpring tizzitPropertiesBeanSpring) {
		// if there is no classloader, create one
		if (ReloadingClassloaderManager.reloadingClassloader == null) {

			// create the URL classloader
			final ClassLoader urlClassloader = createExternalLibClassLoader(tizzitPropertiesBeanSpring);
			// check, if the reloading classloader should be used
			if (isReloadingClassloaderEnabled(tizzitPropertiesBeanSpring)) {
				log.warn("Reloading is not yet implemented!");
				ReloadingClassloaderManager.reloadingClassloader = urlClassloader;
			} else {
				ReloadingClassloaderManager.reloadingClassloader = urlClassloader;
			}
		}
		return ReloadingClassloaderManager.reloadingClassloader;
	}

	protected static ClassLoader createExternalLibClassLoader(TizzitPropertiesBeanSpring tizzitPropertiesBeanSpring) {
		String externalLibPath = tizzitPropertiesBeanSpring.getExternalLib().getPath();
		try {
			URL[] urls = null;

			List<URL> urlList = tizzitPropertiesBeanSpring.getExternalLib().getURLList();
			if (urlList != null && urlList.size() > 0) {
				urls = new URL[urlList.size()];
				urlList.toArray(urls);
			}

			//return new URLClassLoader(urls, ReloadingClassloaderManager.class.getClassLoader());
			return new ExternalLibClassLoader(urls, ReloadingClassloaderManager.class.getClassLoader());
		} catch (Exception e) {
			throw new ReloadingClassloaderCreationException("Error while creating the URLClassLoader from externalLib with path: '" + externalLibPath + "'.", e);
		}
	}

	protected static boolean isReloadingClassloaderEnabled(TizzitPropertiesBeanSpring tizzitPropertiesBeanSpring) {
		return tizzitPropertiesBeanSpring.getExternalLib().isReloadingEnabled();
	}
}
