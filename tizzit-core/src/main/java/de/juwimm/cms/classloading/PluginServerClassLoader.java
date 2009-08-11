/**
 * Copyright (c) 2009 Juwi MacMillan Group GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.juwimm.cms.classloading;

import java.net.URL;
import java.net.URLClassLoader;

import org.apache.log4j.Logger;

/**
 * Special <code>URLClassLoader</code> with possibility to extend the repository for loading classes at runtime
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * , Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class PluginServerClassLoader extends URLClassLoader {
	public static Logger log = Logger.getLogger(PluginServerClassLoader.class);
	
	public PluginServerClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}
	
	/** @see java.lang.ClassLoader#loadClass(java.lang.String) */
	@Override
	public Class< ? > loadClass(String name) throws ClassNotFoundException {
		if (log.isDebugEnabled()){
			log.debug("loadClass (" + name + ")");
			log.debug("Thread " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\": ");
		}
		return super.loadClass(name);
	}

	/**
	 * extend the repository for loading classes by urls
	 * @param urls
	 */
	public void appendUrls(URL[] urls) {
		if (urls != null && urls.length > 0) {
			for (int i = 0; i < urls.length; i++) {
				if (!this.isUrlAlreadyKnown(urls[i])) {
					if (log.isDebugEnabled()) log.debug("Adding url " + urls[i].toString() + " to repository");
					super.addURL(urls[i]);
				}
			}
		}
	}

	private boolean isUrlAlreadyKnown(URL url) {
		if (log.isDebugEnabled()) log.debug("Url to check: " + url.toString());
		boolean result = false;
		URL[] urls = super.getURLs();
		if (urls != null && urls.length > 0) {
			for (int i = 0; i < urls.length; i++) {
				result = url.sameFile(urls[i]);
				if (result) {
					if (log.isDebugEnabled()) log.debug("Url is already part of this repository");
					break;
				}
			}
		}

		return result;
	}
}