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
package de.juwimm.cms.beans.cocoon;

import java.util.HashMap;

import org.apache.log4j.Logger;

import de.juwimm.cms.beans.WebServiceSpring;
import de.juwimm.cms.vo.ContentPluginValue;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PluginCache {
	private static Logger log = Logger.getLogger(PluginCache.class);
	private HashMap<PluginIdentifier, ContentPluginValue> plugins = new HashMap<PluginIdentifier, ContentPluginValue>();
	private WebServiceSpring webServiceSpringBean = null;
	private boolean isRenewing = false;

	public WebServiceSpring getWebServiceSpringBean() {
		return this.webServiceSpringBean;
	}

	public void setWebServiceSpringBean(WebServiceSpring webServiceSpringBean) {
		this.webServiceSpringBean = webServiceSpringBean;
	}

	/**
	 * @TODO @todo: renewCacheFromDcf
	 *
	 */
	public void renewCacheFromDcf() {
		if (!isRenewing) {
			isRenewing = true;
			new Thread(new Runnable() {
				public void run() {
					log.info("starting renewing plugin cache... please be patient");
					try {
						ContentPluginValue[] pluginss = getWebServiceSpringBean().getAllPluginsFromDcf();
						plugins = new HashMap<PluginIdentifier, ContentPluginValue>();
						for (int i = 0; i < pluginss.length; i++) {
							plugins.put(new PluginIdentifier(pluginss[i].getSiteId(), pluginss[i].getNamespace()), pluginss[i]);
						}
					} catch (Exception exe) {
						log.error("an unknown error occured", exe);
					}
					log.info("finished loading plugin cache!");
					isRenewing = false;
				}
			}, "PluginCacheRenewer").start();
		}
	}

	public ContentPluginValue getPluginValue(PluginIdentifier pluginIdent) {
		return plugins.get(pluginIdent);
	}

}
