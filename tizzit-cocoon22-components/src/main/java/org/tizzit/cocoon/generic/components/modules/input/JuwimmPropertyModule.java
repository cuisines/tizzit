package org.tizzit.cocoon.generic.components.modules.input;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.components.modules.input.AbstractJXPathModule;
import org.apache.cocoon.components.modules.input.InputModule;
import org.apache.log4j.Logger;

/**
 * Provides the {juwimm-cocoon-property:xyz} Namespace in the sitemap.
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id:JuwimmPropertyModule.java 4371 2008-05-28 09:37:42Z kulawik $
 */
public class JuwimmPropertyModule extends AbstractJXPathModule implements InputModule, ThreadSafe {
	private Logger log = Logger.getLogger(JuwimmPropertyModule.class);
	private static final String PROPERTIES_FILENAME = "juwimm-cocoon.properties"; //$NON-NLS-1$
	private Properties prop;
	//private WebServiceSpring webSpringBean = null;
	private long lastLoadTime = -1L;

	@SuppressWarnings("unchecked")
	@Override
	protected Object getContextObject(Configuration modeConf, Map objectModel) throws ConfigurationException {
		if (this.prop == null) {
			try {
				//this.webSpringBean = (WebServiceSpring) CocoonSpringHelper.getBean(objectModel, CocoonSpringHelper.WEB_SERVICE_SPRING);
			} catch (Exception exf) {
				log.error("Could not load webservicespringbean!", exf); //$NON-NLS-1$
			}
			this.load();
		}
		return this.prop;
	}

	/*
	 * @see org.apache.cocoon.components.modules.input.AbstractJXPathModule#getAttribute(java.lang.String, org.apache.avalon.framework.configuration.Configuration, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getAttribute(String name, Configuration modeConf, Map objectModel) throws ConfigurationException {
		Object result = super.getAttribute(name, modeConf, objectModel);
		if (result == null) {
			if (log.isDebugEnabled()) log.debug("Attribute \"" + name + "\" not found, reloading..."); //$NON-NLS-1$ //$NON-NLS-2$
			this.load();
			result = super.getAttribute(name, modeConf, objectModel);
		}

		return result;
	}

	private void load() {
		if (log.isDebugEnabled()) log.debug("start load JuwimmPropertyModule"); //$NON-NLS-1$
		long siteLastModifiedTime = 0L;
		try {
			//siteLastModifiedTime = this.webSpringBean.getMaxSiteLastModiefiedDate();
		} catch (Exception e) {
		}
		if (siteLastModifiedTime > this.lastLoadTime) {
			// at least one site has changed since last load

			try {
				this.prop = new Properties();
				InputStream is = this.getClass().getClassLoader().getResourceAsStream(JuwimmPropertyModule.PROPERTIES_FILENAME);
				if (is == null) {
					super.getLogger().warn("Unable to load \"" + JuwimmPropertyModule.PROPERTIES_FILENAME + "\" for JuwimmPropertyModule!"); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					prop.load(is);
				}
				/*
				Collection sites = this.webSpringBean.getAllSites();
				for (Iterator it = sites.iterator(); it.hasNext();) {
					SiteValue site = (SiteValue) it.next();
					String defaultLaguage = this.webSpringBean.getDefaultLanguage(site.getSiteId());
					this.prop.put("mandator-id-" + site.getShortName(), site.getSiteId()); //$NON-NLS-1$
					this.prop.put("default-language-" + site.getShortName(), defaultLaguage); //$NON-NLS-1$
					if (System.getProperty("cqLiveserver", "false").equalsIgnoreCase("false")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						this.prop.put("expires-" + site.getShortName(), "0"); //$NON-NLS-1$ //$NON-NLS-2$
					} else {
						this.prop.put("expires-" + site.getShortName(), site.getCacheExpire()); //$NON-NLS-1$
					}
				}*/
			} catch (Exception ex) {
				log.error("an unknown Exception ", ex); //$NON-NLS-1$
			}

			this.lastLoadTime = System.currentTimeMillis();
		}

		if (log.isDebugEnabled()) log.debug("end load"); //$NON-NLS-1$
	}

}
