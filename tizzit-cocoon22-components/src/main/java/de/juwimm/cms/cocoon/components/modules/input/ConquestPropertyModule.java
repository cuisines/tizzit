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
package de.juwimm.cms.cocoon.components.modules.input;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.components.modules.input.AbstractJXPathModule;
import org.apache.cocoon.components.modules.input.InputModule;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.log4j.Logger;

import de.juwimm.cms.beans.WebServiceSpring;
import de.juwimm.cms.cocoon.helper.CocoonSpringHelper;
import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.vo.SiteValue;

/**
 * @Deprecated TODO After switching to Cocoon 2.2 this should be implemented by resolving tizzitPropertiesBeanSpring
 * Provides the tizzit.properties functionality for ConQuest 2.2 systems
 * with a synthetic Cocoon deployment.
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class ConquestPropertyModule extends AbstractJXPathModule implements InputModule, ThreadSafe {
	private final Logger log = Logger.getLogger(ConquestPropertyModule.class);
	private static final String PROPERTIES_FILENAME = "tizzit.properties";
	private Properties prop;
	private WebServiceSpring webSpringBean = null;
	private long lastLoadTime = -1L;
	private long lastLoadTimeConquestProperties = -1L;

	@Override
	protected Object getContextObject(Configuration modeConf, Map objectModel) throws ConfigurationException {
		if (this.webSpringBean == null) {
			try {
				this.webSpringBean = (WebServiceSpring) CocoonSpringHelper.getBean(objectModel, CocoonSpringHelper.WEB_SERVICE_SPRING);
			} catch (Exception exf) {
				log.error("Could not load webservicespringbean!", exf);
			}
		}
		return this.prop;
	}

	/*
	 * @see org.apache.cocoon.components.modules.input.AbstractJXPathModule#getAttribute(java.lang.String, org.apache.avalon.framework.configuration.Configuration, java.util.Map)
	 */
	@Override
	public Object getAttribute(String name, Configuration modeConf, Map objectModel) throws ConfigurationException {
		String result;
		if (this.webSpringBean == null) {
			try {
				this.webSpringBean = (WebServiceSpring) CocoonSpringHelper.getBean(objectModel, CocoonSpringHelper.WEB_SERVICE_SPRING);
			} catch (Exception exf) {
				log.error("Could not load webservicespringbean!", exf);
			}
		}
		if ("liveserver".equals(name)) {
			Request request = ObjectModelHelper.getRequest(objectModel);
			String host = request.getHeader("Host");
			int portPosition = host.lastIndexOf(":");
			if (portPosition > 0) {
				host = host.substring(0, portPosition);
			}
			result = webSpringBean.getLiveserver(host).toString();
		} else {
			reload();
			result = prop.get(name).toString();
		}
		return result;
	}

	private void reload() {
		if (log.isDebugEnabled()) log.debug("start load");
		long siteLastModifiedTime = 0L;
		try {
			siteLastModifiedTime = this.webSpringBean.getMaxSiteLastModifiedDate();
		} catch (Exception e) {
		}
		// load site specific variables
		if (siteLastModifiedTime > this.lastLoadTime) {
			this.lastLoadTime = siteLastModifiedTime;
			prop = new Properties();
			loadConquestProperties();
			loadSiteProperties();
		} else {
			// load variables from tizzit.properties
			URL url = this.getClass().getResource("/" + PROPERTIES_FILENAME);
			File f;
			try {
				f = new File(url.toURI());
				if (lastLoadTimeConquestProperties < f.lastModified()) {
					lastLoadTimeConquestProperties = f.lastModified();
					loadConquestProperties();
				}
			} catch (URISyntaxException e) {
				log.error("URI Syntax for tizzit.properties file is wrong", e);
			}
		}
		if (log.isDebugEnabled()) log.debug("end load");
	}

	private void loadSiteProperties() {
		// at least one site has changed since last load
		try {
			Collection sites = this.webSpringBean.getAllSites();
			for (Iterator it = sites.iterator(); it.hasNext();) {
				SiteValue site = (SiteValue) it.next();
				String defaultLaguage = this.webSpringBean.getDefaultLanguage(site.getSiteId());
				prop.put("mandator-id-" + site.getShortName(), site.getSiteId());
				prop.put("default-language-" + site.getShortName(), defaultLaguage);
				if (!Boolean.parseBoolean(prop.get("tizzitPropertiesBeanSpring.liveserver").toString())) {
					prop.put("expires-" + site.getShortName(), "0");
				} else {
					prop.put("expires-" + site.getShortName(), site.getCacheExpire());
				}
			}
		} catch (UserException exe) {
			log.error("an unknown UserException occured", exe);
		} catch (Exception ex) {
			log.error("an unknown Exception ", ex);
		}
	}

	private void loadConquestProperties() {
		InputStream is = this.getClass().getResourceAsStream("/" + PROPERTIES_FILENAME);
		try {
			prop.load(is);
			System.setProperty("tizzitCmsTemplatesPath", prop.get("tizzitPropertiesBeanSpring.cmsTemplatesPath").toString());
			System.setProperty("tizzitLiveserver", prop.get("tizzitPropertiesBeanSpring.liveserver").toString());
		} catch (Exception exe) {
			log.warn("Unable to load props from \"" + PROPERTIES_FILENAME + "\"!");
		}
	}
}
