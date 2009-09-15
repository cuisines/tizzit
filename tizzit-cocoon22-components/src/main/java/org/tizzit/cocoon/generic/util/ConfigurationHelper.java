package org.tizzit.cocoon.generic.util;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.log4j.Logger;

/**
 * 
 * @author toerberj
 *
 * read the configuration of the Action from the sitemap
 * @version $Id:ConfigurationHelper.java 4371 2008-05-28 09:37:42Z kulawik $
 */
public class ConfigurationHelper {
	public static final String JUWIMM_COCOON_COMPONENT_LIBRARIES_PARENT = "juwimmCocoonComponentLibrariesParent"; //$NON-NLS-1$
	private String PROPERTY_FILE = "juwimm-cocoon.properties";

	protected static Logger log = Logger.getLogger(ConfigurationHelper.class);

	/**
	 * Only use the public parameter to pass necessary parameters
	 */
	@SuppressWarnings("unused")
	private ConfigurationHelper() {
	}

	public ConfigurationHelper(String propertyFile) {
		this.PROPERTY_FILE = propertyFile;
	}

	protected Properties getProperties() {
		Properties props = new Properties();
		try {
			InputStream in = getClass().getClassLoader().getResourceAsStream(PROPERTY_FILE);
			props.load(in);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return props;
	}

	public String getUrl(String siteShort) {
		String path = getProperties().getProperty(ConfigurationHelper.JUWIMM_COCOON_COMPONENT_LIBRARIES_PARENT);
		if (!path.endsWith("/")) {
			path = path + "/";
		}
		return path + siteShort + File.separatorChar;
	}

	public static String getSiteShort(Configuration config) throws ConfigurationException {
		String siteShort = new String();

		try {
			siteShort = config.getChild("siteShort").getValue(); //$NON-NLS-1$
		} catch (ConfigurationException e) {
			log.error(e);
		}
		if (siteShort.length() == 0) throw new ConfigurationException("siteSort in sitemap not found or empty"); //$NON-NLS-1$
		return siteShort;
	}

	public static String getDsJndiName(Configuration config) throws ConfigurationException {
		String dsJndiName = new String();

		try {
			dsJndiName = config.getChild("dsJndiName").getValue(); //$NON-NLS-1$
		} catch (ConfigurationException e) {
			log.error(e);
		}
		if (dsJndiName.length() == 0) throw new ConfigurationException("dsJndiName in sitemap not found or empty"); //$NON-NLS-1$

		return dsJndiName;
	}

	public static String getClassName(Configuration config) throws ConfigurationException {
		String classname = new String();

		try {
			classname = config.getChild("classname").getValue(); //$NON-NLS-1$
		} catch (ConfigurationException e) {
			log.error(e);
		}

		if (classname.length() == 0) throw new ConfigurationException("classname in sitemap not found or empty"); //$NON-NLS-1$

		return classname;
	}

	public static String[] getJarNames(Configuration config) throws ConfigurationException {
		String jarNames[] = null;
		Configuration cfg = config.getChild("classpath"); //$NON-NLS-1$

		if (cfg != null) {
			Configuration[] cfgJarNames = cfg.getChildren("jar"); //$NON-NLS-1$

			jarNames = new String[cfgJarNames.length];
			for (int i = 0; i < cfgJarNames.length; i++) {
				try {
					jarNames[i] = cfgJarNames[i].getValue();
				} catch (ConfigurationException e) {
					log.error("jarNames[" + i + "] ", e); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}

		if (jarNames.length == 0) throw new ConfigurationException("no jarNames in Configuration found"); //$NON-NLS-1$

		return jarNames;
	}
}