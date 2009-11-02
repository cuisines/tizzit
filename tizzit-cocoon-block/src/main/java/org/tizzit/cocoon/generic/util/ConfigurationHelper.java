package org.tizzit.cocoon.generic.util;

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
	protected static Logger log = Logger.getLogger(ConfigurationHelper.class);

	/**
	 * Only use the public parameter to pass necessary parameters
	 */
	@SuppressWarnings("unused")
	private ConfigurationHelper() {
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
}