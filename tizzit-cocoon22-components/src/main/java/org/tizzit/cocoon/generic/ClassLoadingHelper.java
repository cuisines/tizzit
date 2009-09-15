package org.tizzit.cocoon.generic;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.tizzit.cocoon.generic.JuwiURLClassLoaderFactory.JuwiUrlClassLoader;
import org.tizzit.cocoon.generic.util.ConfigurationHelper;

/**
 * Helper for loading new classes dynamically
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * , Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id:ClassLoadingHelper.java 4371 2008-05-28 09:37:42Z kulawik $
 */
public class ClassLoadingHelper {
	private static Logger log = Logger.getLogger(ClassLoadingHelper.class);
	private URL[] urls = null;

	/**
	 * Create an new helper
	 * @param mandatorShort
	 * @param jarNames
	 */
	public ClassLoadingHelper(String mandatorShort, String[] jarNames) {
		if (log.isDebugEnabled()) log.debug(this.getThreadInfo() + "Instanciating new ClassLoadingHelper for Mandantor \"" + mandatorShort + "\" with jars: " + this.getStringFromArray(jarNames)); //$NON-NLS-1$ //$NON-NLS-2$
		ConfigurationHelper ch = new ConfigurationHelper("juwimm-cocoon.properties");
		String dcfUrl = ch.getUrl(mandatorShort);
		this.urls = this.getURLArrayFromJarsURL(jarNames, dcfUrl);
	}

	/**
	 * instanciates a new instance of the desired class
	 * @param classname
	 * @return an instance of the desired class
	 */
	@SuppressWarnings("unchecked")
	public Object instanciateClass(String classname) {
		if (log.isDebugEnabled()) log.debug(this.getThreadInfo() + "Instanciating class " + classname); //$NON-NLS-1$
		JuwiUrlClassLoader cl = null;
		ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
		if (threadClassLoader instanceof JuwiUrlClassLoader) {
			if (log.isDebugEnabled()) log.debug(this.getThreadInfo() + "extending repository for existing classloader"); //$NON-NLS-1$
			cl = (JuwiUrlClassLoader) threadClassLoader;
		} else {
			if (log.isDebugEnabled()) log.debug(this.getThreadInfo() + "wrapping classloader in extended classloader"); //$NON-NLS-1$
			JuwiURLClassLoaderFactory factory = JuwiURLClassLoaderFactory.getInstance();
			cl = factory.getJuwiUrlClassLoader(this.urls, threadClassLoader);
			Thread.currentThread().setContextClassLoader(cl);
		}
		cl.appendUrls(this.urls);
		try {
			Class c = cl.loadClass(classname);
			return c.newInstance();
		} catch (ClassNotFoundException ex) {
			log.error(classname + " not found ", ex); //$NON-NLS-1$
		} catch (InstantiationException e) {
			log.error("Error instantiating " + classname + ": " + e.getMessage(), e); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (Exception ex) {
			log.error(ex);
		}
		return null;
	}

	private String getStringFromArray(String[] source) {
		StringBuilder sb = new StringBuilder();
		if (source != null && source.length > 0) {
			for (int i = 0; i < source.length; i++) {
				if (sb.length() > 0) sb.append(", "); //$NON-NLS-1$
				sb.append(source[i]);
			}
		}
		return sb.toString();
	}

	private URL[] getURLArrayFromJarsURL(String[] jarNames, String url) {
		URL[] urla = null;
		if (jarNames != null) {
			urla = new URL[jarNames.length];
			try {
				for (int i = 0; i < jarNames.length; i++) {
					urla[i] = new URL(url + jarNames[i]);
				}
			} catch (MalformedURLException ex) {
				log.error(ex);
			}
		}

		return urla;
	}

	private String getThreadInfo() {
		return "Thread " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\": "; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

}
