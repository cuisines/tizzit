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
package de.juwimm.cms.cocoon.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.log4j.Logger;

/**
 *
 * @author toerberj
 *
 * read the configuration of the Action from the sitemap
 * @version $Id$
 */
public class ConfigurationHelper extends org.tizzit.cocoon.generic.helper.ConfigurationHelper {
	private static Logger log = Logger.getLogger(ConfigurationHelper.class);
	private static Properties props = null;

	public ConfigurationHelper() {
		super();
	}

	public static String getDCFUrl(String siteShort) {
		if (props == null) {
			URL cp = ConfigurationHelper.class.getResource("/tizzit.properties");
			try {
				InputStream is = new FileInputStream(cp.getFile());
				props = new Properties();
				is = new FileInputStream(cp.getFile());
				props.load(is);
			} catch (FileNotFoundException e) {
				log.error("Could not find tizzit.properties");
			} catch (IOException e) {
				log.warn("Unable to load jnlpHost / jnlpPort props from \"tizzit.properties\"!");
			}
		}
		String parent = props.getProperty("tizzitPropertiesBeanSpring.cocoon.componentLibrariesParent", "");
		return parent + siteShort + File.separatorChar;
	}

	public static String getSiteShort(Configuration config) throws ConfigurationException {
		String siteShort = "";

		try {
			siteShort = config.getChild("siteShort").getValue();
		} catch (ConfigurationException e) {
			log.error(e);
		}
		if (siteShort.length() == 0) throw new ConfigurationException("siteSort in sitemap not found or empty");
		return siteShort;
	}

}