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

import java.io.File;
import java.net.URL;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.beans.SpringPluginServerClassloadingHelper;
import de.juwimm.cms.beans.foreign.CqPropertiesBeanSpring;
import de.juwimm.cms.model.SiteHbmDao;
import de.juwimm.cms.plugins.server.ConquestPlugin;
import de.juwimm.cms.vo.ContentPluginValue;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PluginCacheAccessor {
	private static Logger log = Logger.getLogger(PluginCacheAccessor.class);
	private HashMap<PluginIdentifier, ConquestPlugin> plugins = new HashMap<PluginIdentifier, ConquestPlugin>();
	@Autowired
	private SpringPluginServerClassloadingHelper springPluginServerClassloadingHelper = null;
	@Autowired
	private CqPropertiesBeanSpring cqPropertiesBeanSpring;
	@Autowired
	private SiteHbmDao siteHbmDao;
	@Autowired
	private PluginCache pluginCache = null;

	public ConquestPlugin getPlugin(PluginIdentifier pluginIdent) {
		if (log.isDebugEnabled()) log.debug("getPlugin for identifier");
		ConquestPlugin plugin = plugins.get(pluginIdent);
		if (plugin == null) {
			if (log.isDebugEnabled()) log.debug("Plugin not existing in cache - instanciating!");
			ContentPluginValue pluginValue = pluginCache.getPluginValue(pluginIdent);

			try {
				URL[] url = new URL[pluginValue.getClasspath().length];
				for (int j = 0; j < pluginValue.getClasspath().length; j++) {
					String siteShort = siteHbmDao.load(pluginIdent.getSiteId()).getMandatorDir();
					String myurl = cqPropertiesBeanSpring.getMandatorParent() + siteShort + "dcf" + File.separatorChar + pluginValue.getClasspath()[j];

					if (log.isDebugEnabled()) {
						log.debug("cqMandatorParent " + cqPropertiesBeanSpring.getMandatorParent());
						log.debug("SITE SHORT " + siteShort);
						log.debug("CLASSPATH " + pluginValue.getClasspath()[j]);
						log.debug("URL " + myurl);
					}
					url[j] = new URL(myurl);
					
					ConquestPlugin cp = (ConquestPlugin) springPluginServerClassloadingHelper.loadMandatorClass(pluginValue.getGeneratorClassname(), url);
					plugins.put(pluginIdent, cp);
					return cp;
				}
			} catch (Exception e) {
				log.error("error retrieving plugin!", e);
			}
		}
		log.debug("LEAVING GET PLUGIN");
		return plugins.get(pluginIdent);
	}
}
