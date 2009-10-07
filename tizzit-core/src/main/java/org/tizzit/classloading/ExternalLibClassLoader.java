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
package org.tizzit.classloading;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

// TODO: Class description
/**
 *
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since tizzit-core 07.10.2009
 */
public class ExternalLibClassLoader extends URLClassLoader {

	/**
	 * @param urls
	 * @param parent
	 * @param factory
	 */
	public ExternalLibClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
		super(urls, parent, factory);
	}

	/**
	 * @param urls
	 * @param parent
	 */
	public ExternalLibClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	/**
	 * @param urls
	 */
	public ExternalLibClassLoader(URL[] urls) {
		super(urls);
	}

}
