package org.tizzit.cocoon.generic;

import java.net.URL;
import java.net.URLClassLoader;

import org.apache.log4j.Logger;

/**
 * Factory managing the Singleton JuwiUrlClassLoader
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * , Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id:JuwiURLClassLoaderFactory.java 4371 2008-05-28 09:37:42Z kulawik $
 */
public class JuwiURLClassLoaderFactory {
	public static Logger log = Logger.getLogger(JuwiURLClassLoaderFactory.class);
	private static JuwiURLClassLoaderFactory instance;
	private static JuwiUrlClassLoader classLoader;

	private JuwiURLClassLoaderFactory() {
	}

	/**
	 * get the factory Singleton
	 * @return the one and only factory
	 */
	public static JuwiURLClassLoaderFactory getInstance() {
		if (JuwiURLClassLoaderFactory.instance == null) JuwiURLClassLoaderFactory.instance = new JuwiURLClassLoaderFactory();
		return JuwiURLClassLoaderFactory.instance;
	}

	/**
	 * get the JuwiUrlClassLoader Singleton
	 * @param urls the new urls to expand the repository
	 * @param parent the parent classloader
	 * @return the one and only JuwiUrlClassLoader
	 */
	public JuwiUrlClassLoader getJuwiUrlClassLoader(URL[] urls, ClassLoader parent) {
		if (JuwiURLClassLoaderFactory.classLoader == null) {
			JuwiURLClassLoaderFactory.classLoader = new JuwiUrlClassLoader(urls, parent);
		}
		return JuwiURLClassLoaderFactory.classLoader;
	}

	/**
	 * Special <code>URLClassLoader</code> with possibility to extend the repository for loading classes at runtime
	 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
	 * , Juwi|MacMillan Group Gmbh, Walsrode, Germany
	 * @version $Id:JuwiURLClassLoaderFactory.java 4371 2008-05-28 09:37:42Z kulawik $
	 */
	public class JuwiUrlClassLoader extends URLClassLoader {

		private JuwiUrlClassLoader(URL[] urls, ClassLoader parent) {
			super(urls, parent);
		}

		/** @see java.lang.ClassLoader#loadClass(java.lang.String) */
		@Override
		public Class< ? > loadClass(String name) throws ClassNotFoundException {
			if (log.isDebugEnabled()) log.debug("loadClass (" + name + ")"); //$NON-NLS-1$//$NON-NLS-2$
			return super.loadClass(name);
		}

		/**
		 * extend the repository for loading classes by urls
		 * @param urls
		 */
		public void appendUrls(URL[] urls) {
			if (urls != null && urls.length > 0) {
				for (int i = 0; i < urls.length; i++) {
					if (!this.isUrlAlreadyKnown(urls[i])) {
						if (log.isDebugEnabled()) log.debug("Adding url " + urls[i].toString() + " to repository"); //$NON-NLS-1$ //$NON-NLS-2$
						super.addURL(urls[i]);
					}
				}
			}
		}

		private boolean isUrlAlreadyKnown(URL url) {
			if (log.isDebugEnabled()) log.debug("Url to check: " + url.toString()); //$NON-NLS-1$
			boolean result = false;
			URL[] urls = super.getURLs();
			if (urls != null && urls.length > 0) {
				for (int i = 0; i < urls.length; i++) {
					result = url.sameFile(urls[i]);
					if (result) {
						if (log.isDebugEnabled()) log.debug("Url is already part of this repository"); //$NON-NLS-1$
						break;
					}
				}
			}

			return result;
		}
	}

}
