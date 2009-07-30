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
package de.juwimm.cms.cocoon;

import java.net.*;

import org.apache.log4j.Logger;

import de.juwimm.cms.cocoon.helper.ConfigurationHelper;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 * @deprecated replaced by de.juwimm.cms.cocoon.ClassloadingHelper
 */
public class CformHelper {
	private static Logger log = Logger.getLogger(CformHelper.class);
	private static int globalId = 0;
	private static int closedCount = 0;
	private int localId = 0;
	// see also JBoss 4.0 official guide (integrating third party servlet containers, page 424ff)
	// we change the ContextClassloader and we have to set it back so we store it here
	private ClassLoader prevContextClassloader = null;
	private URLClassLoader cl = null;
	private boolean isAlive = true;

	/**
	 * Creates the Borderline for all Classes instanciated by this helper.
	 * Please note - the Borderline HAS TO BE retired BEFORE the execution of the cform will go on.
	 */
	public CformHelper(String mandatorShort, String[] jarNames) {
		this.localId = ++globalId;
		if (log.isDebugEnabled()) log.debug("Thread " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\" " + this.localId + " Instanciating new CformHelper for Mandantor " + mandatorShort + " with jars: " + this.getStringFromArray(jarNames));
		String dcfUrl = ConfigurationHelper.getDCFUrl(mandatorShort);
		this.cl = this.getURLClassLoader(this.getURLArrayFromJarsURL(jarNames, dcfUrl));
	}

	/**
	 * Instanciate concrete class with extended classloader
	 * @return an instance of the desired class
	 */
	public Object instanciateClass(String classname) {
		if (log.isDebugEnabled()) log.debug("Thread " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\" " + this.localId + " Instanciating class " + classname);
		if (this.isAlive) {
			try {
				Class c = this.cl.loadClass(classname);
				return c.newInstance();
			} catch (ClassNotFoundException ex) {
				log.error(classname + " not found ", ex);
			} catch (InstantiationException e) {
				log.error("COULD NOT INSTANTIATE " + classname + " ", e);
			} catch (Exception ex) {
				log.error(ex);
			}
			return null;
		}
		throw new IllegalStateException(this.localId + " CformHelper has already been retired");
	}

	public void reuseClassloader() {
		if (log.isDebugEnabled()) log.debug("Thread " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\" " + this.localId + " reuseClassloader");
		Thread.currentThread().setContextClassLoader(this.cl);
		this.isAlive = true;
	}

	public void retireBorderline() {
		if (log.isDebugEnabled()) log.debug("Thread " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\" " + this.localId + " retireBorderline Stats: " + ++closedCount + " closed of " + globalId);
		Thread.currentThread().setContextClassLoader(this.prevContextClassloader);
		this.isAlive = false;
	}

	/**
	 * 
	 * @param jarNames	array of jar-files
	 * @param url		url-location of the jar-files 
	 * @return			null if error, URL[] otherwise
	 */
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

	/**
	 * creates a new URLClassloader using the thread's former classloader as parent and appends the urls for loading the desired classes
	 * @param url
	 * @return an classloader with extended repository for loading classes
	 */
	private URLClassLoader getURLClassLoader(URL[] url) {
		if (log.isDebugEnabled()) log.debug("Thread " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\" " + this.localId + " getURLClassLoader");
		this.prevContextClassloader = Thread.currentThread().getContextClassLoader();
		URLClassLoader cl = new URLClassLoader(url, this.prevContextClassloader);
		Thread.currentThread().setContextClassLoader(cl);

		return cl;
	}

	private String getStringFromArray(String[] source) {
		StringBuilder sb = new StringBuilder();
		if (source != null && source.length > 0) {
			for (int i = 0; i < source.length; i++) {
				if (sb.length() > 0) sb.append(", ");
				sb.append(source[i]);
			}
		}
		return sb.toString();
	}

}
