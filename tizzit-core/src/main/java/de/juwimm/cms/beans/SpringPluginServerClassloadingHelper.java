package de.juwimm.cms.beans;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.beans.foreign.TizzitPropertiesBeanSpring;
import de.juwimm.cms.classloading.PluginServerClassloadingHelper;

public class SpringPluginServerClassloadingHelper extends PluginServerClassloadingHelper {
	private static Logger log = Logger.getLogger(SpringPluginServerClassloadingHelper.class);
	@Autowired
	private TizzitPropertiesBeanSpring tizzitPropertiesBeanSpring;

	private URL[] urls = null;

	/**
	 * This is the loader for all extension points
	 * @param clazzName
	 * @return
	 */
	public Object loadServerClass(String clazzName) {
		if (log.isDebugEnabled()) {
			log.debug("loadServerClass - with parameter: " + clazzName);
			log.debug("Thread " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\": ");
		}
		try {
			if (urls == null) {
				String parentDir = tizzitPropertiesBeanSpring.getCocoon().getComponentLibrariesParent() + File.separatorChar + "server" + File.separatorChar;
				log.info("The Tizzit server starts caching the plugin extensions from " + parentDir);
				File dir = new File(parentDir.substring(7));
				File[] files = dir.listFiles();
				urls = new URL[files.length];
				int j = 0;
				for (File file : files) {
					String myurl = parentDir + file.getName();
					urls[j++] = new URL(myurl);
					log.info("Appending " + myurl + " to server classpath for plugins");
				}
			}
			return loadMandatorClass(clazzName, urls);
		} catch (Exception ine) {
			log.warn("Could not load plugin from plugin classpath " + ine.getMessage(), ine);
		}
		return null;
	}
}
