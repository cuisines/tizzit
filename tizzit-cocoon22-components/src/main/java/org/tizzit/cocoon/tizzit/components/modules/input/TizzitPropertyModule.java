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
package org.tizzit.cocoon.tizzit.components.modules.input;

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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.juwimm.cms.beans.WebServiceSpring;
import de.juwimm.cms.cocoon.helper.CocoonSpringHelper;
import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.vo.SiteValue;

// TODO: Class description
// TODO: verify behavior
// TODO: verify class comment
/**
 * @Deprecated TODO After switching to Cocoon 2.2 this should be implemented by resolving TizzitPropertiesBeanSpring
 * Provides the tizzit.properties functionality for Tizzit 2.2 systems
 * with a synthetic Cocoon deployment.
 *
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since tizzit-web-cocoon22 02.09.2009
 */
public class TizzitPropertyModule extends AbstractJXPathModule implements InputModule, ThreadSafe {
	private static final Log log = LogFactory.getLog(TizzitPropertyModule.class);

	private static final String PROPERTIES_FILENAME = "tizzit.properties";
	private Properties prop;
	private long lastLoadTime = -1L;
	private WebServiceSpring webSpringBean = null;
	private long lastLoadTimeTizzitProperties = -1L;

	@SuppressWarnings("unchecked")
	@Override
	protected Object getContextObject(Configuration modeConf, Map objectModel) throws ConfigurationException {
		if (log.isDebugEnabled()) log.debug("getContextObject() -> begin");
		if (this.prop == null) {
			this.instantiateWebServiceSpringBean(objectModel);
			this.load();
		}
		if (log.isDebugEnabled()) log.debug("getContextObject() -> end");
		return this.prop;
	}

	/*
	 * @see org.apache.cocoon.components.modules.input.AbstractJXPathModule#getAttribute(java.lang.String, org.apache.avalon.framework.configuration.Configuration, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getAttribute(String name, Configuration modeConf, Map objectModel) throws ConfigurationException {
		if (log.isDebugEnabled()) log.debug("getAttribute() -> begin");
		Object result;
		if ("liveserver".equals(name)) {
			this.instantiateWebServiceSpringBean(objectModel);
			Request request = ObjectModelHelper.getRequest(objectModel);
			String host = request.getHeader("Host");
			int portPosition = host.lastIndexOf(":");
			if (portPosition > 0) {
				host = host.substring(0, portPosition);
			}
			result = webSpringBean.getLiveserver(host).toString();
		} else {
			result = null;
			try {
				result = super.getAttribute(name, modeConf, objectModel);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (result == null) {
				if (log.isDebugEnabled()) log.debug("Attribute \"" + name + "\" not found, reloading...");
				this.instantiateWebServiceSpringBean(objectModel);
				this.load();
				result = super.getAttribute(name, modeConf, objectModel);
			}
		}
		if (log.isDebugEnabled()) log.debug("getAttribute() -> end");
		return result;
	}

	private void load() {
		if (log.isDebugEnabled()) log.debug("load() -> begin");
		long siteLastModifiedTime = 0L;
		try {
			siteLastModifiedTime = this.webSpringBean.getMaxSiteLastModifiedDate();
		} catch (Exception e) {
		}

		// load site specific variables
		if (siteLastModifiedTime > this.lastLoadTime) {
			// at least one site has changed since last load
			try {
				this.prop = new Properties();
				InputStream is = this.getClass().getClassLoader().getResourceAsStream(TizzitPropertyModule.PROPERTIES_FILENAME);
				if (is == null) {
					super.getLogger().warn("Unable to load \"" + TizzitPropertyModule.PROPERTIES_FILENAME + "\" for TizzitPropertyModule!");
				} else {
					this.prop.load(is);
				}
				this.loadTizzitProperties();
				this.loadSiteProperties();
			} catch (Exception ex) {
				log.error("an unknown Exception ", ex);
			}

			this.lastLoadTime = System.currentTimeMillis();
		} else {
			// load variables from tizzit.properties
			URL url = this.getClass().getResource("/" + PROPERTIES_FILENAME);
			File f;
			try {
				f = new File(url.toURI());
				if (lastLoadTimeTizzitProperties < f.lastModified()) {
					lastLoadTimeTizzitProperties = f.lastModified();
					loadTizzitProperties();
				}
			} catch (URISyntaxException e) {
				log.error("URI Syntax for tizzit.properties file is wrong", e);
			}
		}

		if (log.isDebugEnabled()) log.debug("load() -> end");
	}

	@SuppressWarnings("unchecked")
	private void loadSiteProperties() {
		if (log.isDebugEnabled()) log.debug("loadSiteProperties() -> begin");
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
		if (log.isDebugEnabled()) log.debug("loadSiteProperties() -> end");
	}

	private void loadTizzitProperties() {
		if (log.isDebugEnabled()) log.debug("loadTizzitProperties() -> begin");
		InputStream is = this.getClass().getResourceAsStream("/" + PROPERTIES_FILENAME);
		try {
			prop.load(is);
			prop.setProperty("cmsTemplatesPath", prop.get("tizzitPropertiesBeanSpring.cmsTemplatesPath").toString());
			prop.setProperty("externalLibPath", prop.get("tizzitPropertiesBeanSpring.externalLib.path").toString());
			
		} catch (Exception exe) {
			log.warn("Unable to load props from \"" + PROPERTIES_FILENAME + "\"!");
		}
		if (log.isDebugEnabled()) log.debug("loadTizzitProperties() -> end");
	}

	@SuppressWarnings("unchecked")
	private void instantiateWebServiceSpringBean(Map objectModel) {
		if (this.webSpringBean == null) {
			try {
				this.webSpringBean = (WebServiceSpring) CocoonSpringHelper.getBean(objectModel, CocoonSpringHelper.WEB_SERVICE_SPRING);
			} catch (Exception exf) {
				log.error("Could not load webservicespringbean!", exf);
			}
		}
	}
}
