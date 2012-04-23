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
package org.tizzit.core.classloading;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since tizzit-core 07.10.2009
 */
public class ExternalLibClassLoaderManager {
	private static final Log log = LogFactory.getLog(ExternalLibClassLoaderManager.class);
	private static final String PROPERTY_FILE = "tizzit.properties";

	private static ClassLoader externalLibClassLoader = null;

	private ExternalLibClassLoaderManager() {
		// only allow static usage
	}

	public static ClassLoader getClassLoader() {
		if (externalLibClassLoader == null) { throw new ExternalLibClassLoaderCreationException("'ExternalLibClassLoader' was not initialized on start up!"); }
		return ExternalLibClassLoaderManager.externalLibClassLoader;
	}

	protected static synchronized ClassLoader createExternalLibClassLoader(ServletContext servletContext) {
		if (log.isDebugEnabled()) log.debug("createExternalLibClassLoader() -> begin");
		// if there is no classloader, create one
		if (ExternalLibClassLoaderManager.externalLibClassLoader == null) {
			if (log.isDebugEnabled()) log.debug("Creating singelton of 'ExternalLibClassLoader'!");
			// create the URL classloader

			Properties props = ExternalLibClassLoaderManager.getProperties();

			String externalLibPath = props.getProperty("tizzitPropertiesBeanSpring.externalLib.path");
			if (log.isDebugEnabled()) log.debug("externalLibPath: " + externalLibPath);
			try {
				URL[] urls = null;

				List<URL> urlList = getURLList(externalLibPath);
				if (urlList != null && urlList.size() > 0) {
					urls = new URL[urlList.size()];
					urlList.toArray(urls);
				}

				//return new URLClassLoader(urls, ReloadingClassloaderManager.class.getClassLoader());
				final ClassLoader classLoader = new ExternalLibClassLoader(urls, ExternalLibClassLoaderManager.class.getClassLoader());
				ExternalLibClassLoaderManager.externalLibClassLoader = classLoader;
			} catch (Exception e) {
				throw new ExternalLibClassLoaderCreationException("Error while creating the ExternalLibClassLoader with path: '" + externalLibPath + "'.", e);
			}
		}
		if (log.isDebugEnabled()) log.debug("createExternalLibClassLoader() -> end");
		return ExternalLibClassLoaderManager.externalLibClassLoader;
	}

	public static List<File> getFileList(String path) throws Exception {
		File startFileOrDir = new File(new URI(path));
		validateDirectory(startFileOrDir);
		List<File> result = getFileList(startFileOrDir);
		return result;
	}

	private static List<File> getFileList(File startDir) throws Exception {
		List<File> result = new ArrayList<File>();
		File[] filesAndDirs = startDir.listFiles();
		List<File> filesDirs = Arrays.asList(filesAndDirs);
		for (File file : filesDirs) {
			if (file.isDirectory()) {
				//recursive call!
				if(!file.getName().startsWith("."))
				result.addAll(getFileList(file));
			} else {
				if (log.isDebugEnabled()) log.debug("Adding URL '" + file.getAbsolutePath() + "'.");
				result.add(file);
			}
		}
		return result;
	}

	public static List<URL> getURLList(String path) throws Exception {
		List<URL> retVal = null;

		List<File> files = getFileList(path);
		if (files != null && files.size() > 0) {
			retVal = new ArrayList<URL>(files.size());
			for (File file : files) {
				retVal.add(file.toURL());
			}
		}
		return retVal;
	}

	private static void validateDirectory(File directory) throws FileNotFoundException {
		if (log.isDebugEnabled()) log.debug("validateDirectory() -> begin");
		if (directory == null) { throw new IllegalArgumentException("Directory should not be null."); }
		if (!directory.exists()) { throw new FileNotFoundException("Directory does not exist: " + directory); }
		if (!directory.isDirectory()) { throw new IllegalArgumentException("Is not a directory: " + directory); }
		if (!directory.canRead()) { throw new IllegalArgumentException("Directory cannot be read: " + directory); }
		if (log.isDebugEnabled()) log.debug("validateDirectory() -> end");
	}

	private static Properties getProperties() {
		Properties props = new Properties();
		InputStream in = null;
		try {
			in = ExternalLibClassLoaderManager.class.getClassLoader().getResourceAsStream(PROPERTY_FILE);
			props.load(in);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(in);
			in = null;
		}
		return props;
	}
}
