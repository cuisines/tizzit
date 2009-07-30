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
package de.juwimm.cms.classloading;

import java.net.URL;

import org.apache.log4j.Logger;

/**
 * 
 * @author <a href="mailto:j2ee@juwimm.com">Sascha-Matthias Kulawik</a>
 * company Juwi|MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since cqcms-core 27.03.2009
 */
public class PluginServerClassloadingHelper {
	private static Logger log = Logger.getLogger(PluginServerClassloadingHelper.class);
	private ClassLoader previousClassLoader = null;

	public void restoreContextClassLoader() {
		if (previousClassLoader != null && Thread.currentThread().getContextClassLoader() instanceof PluginServerClassLoader) {
			Thread.currentThread().setContextClassLoader(previousClassLoader);
			previousClassLoader = null;
		}
	}

	private String getThreadInfo() {
		return "Thread " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\": ";
	}

	public Object loadMandatorClass(String clazzName, URL[] urls) {
		/*
		try {
			if (log.isDebugEnabled()) log.debug("Trying to load class " + clazzName);
			clazz = Class.forName(clazzName).newInstance();
			if (log.isDebugEnabled()) log.debug("Successfully found class in my own classpath!");
		} catch (NullPointerException nex) {
			log.warn("no valid classname given: " + clazzName);
		} catch (InstantiationException exe) {
			log.error("an unknown error occured", exe);
		} catch (IllegalAccessException exe) {
			log.error("an unknown error occured", exe);
		} catch (ClassNotFoundException exe) {
			if (log.isDebugEnabled()) log.debug("did not found class in own classpath - resolving through plugin classpath");
			try {
				URLClassLoader cl = new URLClassLoader(classpath, this.getClass().getClassLoader());
				clazz = cl.loadClass(clazzName).newInstance();
			} catch (Exception ine) {
				log.warn("Could not load plugin from plugin classpath " + ine.getMessage(), ine);
			}
		}
		return clazz;*/
		Object clazz = null;
		try {
			if (log.isDebugEnabled()) log.debug("Trying to load class " + clazzName);
			clazz = Class.forName(clazzName).newInstance();
			if (log.isDebugEnabled()) log.debug("Successfully found class in my own classpath!");
		} catch (NullPointerException nex) {
			log.warn("no valid classname given: " + clazzName);
		} catch (InstantiationException exe) {
			log.error("an unknown error occured", exe);
		} catch (IllegalAccessException exe) {
			log.error("an unknown error occured", exe);
		} catch (ClassNotFoundException exe) {
			if (log.isDebugEnabled()) log.debug("did not found class in own classpath - resolving through plugin classpath");
			PluginServerClassLoader cl = null;
			ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
			if (threadClassLoader instanceof PluginServerClassLoader) {
				if (log.isDebugEnabled()) log.debug(this.getThreadInfo() + "extending repository for existing classloader");
				cl = (PluginServerClassLoader) threadClassLoader;
				cl.appendUrls(urls);
			} else {
				if (log.isDebugEnabled()) log.debug(this.getThreadInfo() + "wrapping classloader in extended classloader");
				cl = new PluginServerClassLoader(urls, threadClassLoader);
				previousClassLoader = threadClassLoader;
				Thread.currentThread().setContextClassLoader(cl);
			}
			try {
				Class c = cl.loadClass(clazzName);
				clazz = c.newInstance();
			} catch (ClassNotFoundException ex) {
				log.error(clazzName + " not found ", ex);
			} catch (InstantiationException e) {
				log.error("Error instantiating " + clazzName + ": " + e.getMessage(), e);
			} catch (Exception ex) {
				log.error(ex);
			}
		}
		return clazz;
	}
}
