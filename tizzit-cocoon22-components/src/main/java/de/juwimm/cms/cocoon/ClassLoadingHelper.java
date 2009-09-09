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
/**
 * 
 */
package de.juwimm.cms.cocoon;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

import de.juwimm.cms.classloading.PluginServerClassloadingHelper;
import de.juwimm.cms.cocoon.helper.ConfigurationHelper;

/**
 * Helper for loading new classes dynamically
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * , Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class ClassLoadingHelper {
	private static Logger log = Logger.getLogger(ClassLoadingHelper.class);
	private URL[] urls = null;
	private PluginServerClassloadingHelper psc = new PluginServerClassloadingHelper();

	/**
	 * Create an new helper
	 * @param mandatorShort
	 * @param jarNames
	 */
	public ClassLoadingHelper(String mandatorShort, String[] jarNames) {
		if (log.isDebugEnabled()) log.debug("Instanciating new ClassloadingHelper for Mandantor \"" + mandatorShort + "\" with jars: " + this.getStringFromArray(jarNames));
		String dcfUrl = ConfigurationHelper.getDCFUrl(mandatorShort);
		this.urls = this.getURLArrayFromJarsURL(jarNames, dcfUrl);
	}

	/**
	 * instanciates a new instance of the desired class
	 * @param classname
	 * @return an instance of the desired class
	 */
	public Object instanciateClass(String classname) {
		return psc.loadMandatorClass(classname, this.urls);
	}

	private String getStringFromArray(String[] source) {
		StringBuilder sb = new StringBuilder();
		if (source != null && source.length > 0) {
			for (int i = 0; i < source.length; i++) {
				if (sb.length() > 0) sb.append(", ");
				sb.append(source[i]);
			}
		}
		return sb.toString();
	}

	private URL[] getURLArrayFromJarsURL(String[] jarNames, String url) {
		URL[] urla = null;
		if (jarNames != null) {
			urla = new URL[jarNames.length];
			try {
				for (int i = 0; i < jarNames.length; i++) {
					urla[i] = new URL(url + jarNames[i]);
				}
			} catch (MalformedURLException ex) {
				log.error(ex);
			}
		}
		return urla;
	}
	

	public void restoreContextClassLoader() {
		psc.restoreContextClassLoader();
	}
}
