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
package de.juwimm.cms.content.modules;

import static de.juwimm.cms.client.beans.Application.getBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.Messages;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.content.panel.ContentBorder;
import de.juwimm.cms.content.panel.ContentBorderModulePanel;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.vo.SiteValue;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class ModuleFactoryStandardImpl implements ModuleFactory {
	private static Logger log = Logger.getLogger(ModuleFactoryStandardImpl.class);
	/** Contains all the Modules used in one Template key DcfName value Module */
	private Hashtable<String, Module> htModules = new Hashtable<String, Module>();
	/** Contains the initial content from the DCF for every dcfName */
	private Hashtable<String, Node> htInitialContent = new Hashtable<String, Node>();

	public ModuleFactoryStandardImpl() {
		htModules = new Hashtable<String, Module>();
	}

	public Module getModuleInstanceUnconfigured(String classname, List<String> additionalJarFiles) {
		Module module = null;

		if (!additionalJarFiles.isEmpty()) {
			module = loadPlugins(classname, additionalJarFiles);
		} else {
			try {
				Class handlerClass = Class.forName(classname);
				module = (Module) handlerClass.newInstance();
			} catch (ClassNotFoundException exe) {
				log.error("ClassNotFoundException", exe);
			} catch (IllegalAccessException exe) {
				log.error("IllegalAccessException", exe);
			} catch (InstantiationException exe) {
				log.error("InstantiationException", exe);
			}
		}
		return module;
	}

	public Module loadPlugins(String classname, List<String> additionalJarFiles) {
		Module module = null;
		Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
		SiteValue site = comm.getCurrentSite();
		String urlPath = site.getDcfUrl();
		String siteName = site.getShortName();
		final String userHome = System.getProperty("user.home");
		final String fileSeparator = System.getProperty("file.separator");

		siteName = siteName.replaceAll(" ", "");

		StringBuffer pluginCachePath = new StringBuffer(userHome);
		pluginCachePath.append(fileSeparator);
		pluginCachePath.append(".conquestCache");
		pluginCachePath.append(fileSeparator);
		pluginCachePath.append("plugins");
		pluginCachePath.append(fileSeparator);
		pluginCachePath.append(Constants.SERVER_HOST);
		pluginCachePath.append(fileSeparator);
		pluginCachePath.append(siteName);
		pluginCachePath.append(fileSeparator);

		final String pluginPath = pluginCachePath.toString();

		final int addSize = additionalJarFiles.size();

		/* enthaelt alle Jar files die sich nicht auf dem lokalen Rechner befinden */
		ArrayList<String> httpLoad = new ArrayList<String>();

		for (int i = 0; i < addSize; i++) {
			String filePath = pluginPath + additionalJarFiles.get(i);
			File tempFile = new File(filePath);
			if (!tempFile.exists()) {
				httpLoad.add(additionalJarFiles.get(i));
			}
		}

		if (httpLoad.size() > 0) {
			File dir = new File(pluginPath);
			if (!dir.exists()) {
				log.debug("Going to create plugin directory...");
				boolean ret = dir.mkdirs();
				if (!ret) {
					log.warn("Could not create plugin directory");
				}
			}

			/* laedt die Jarfiles auf den lokalen Rechner herunter */
			HttpClient httpclient = new HttpClient();
			for (int i = 0; i < httpLoad.size(); i++) {
				String url = urlPath + httpLoad.get(i);
				log.debug("Plugin URL " + url);
				HttpMethod method = new GetMethod(url);
				try {
					int status = httpclient.executeMethod(method);
					if (status == HttpStatus.SC_OK) {
						File file = new File(pluginPath + httpLoad.get(i));
						byte[] data = method.getResponseBody();
						log.debug("Received " + data.length + " bytes of data");
						FileOutputStream output = new FileOutputStream(file);
						output.write(data);
						output.close();
					} else {
						log.warn("No OK received");
					}
				} catch (HttpException htex) {
					log.warn("HTTP exception " + htex.getMessage());
				} catch (IOException ioe) {
					log.warn("IO exception " + ioe.getMessage());
				}
				method.releaseConnection();
			}
		}

		try {
			log.debug("Creating URL");

			URL[] url = new URL[addSize];
			for (int i = 0; i < addSize; i++) {
				String jarModule = additionalJarFiles.get(i);
				String jarPath = "file:///" + pluginPath + jarModule;
				log.debug("Jar path " + jarPath);
				url[i] = new URL(jarPath);
			}
			URLClassLoader cl = this.getURLClassLoader(url);
			//URLClassLoader cl = new URLClassLoader(url, this.getClass().getClassLoader());
			if (log.isDebugEnabled()) log.debug("Created URL classloader");
			Class c = cl.loadClass(classname);
			if (log.isDebugEnabled()) log.debug("Created class");
			module = (Module) c.newInstance();
			if (log.isDebugEnabled()) log.debug("Got the module");
		} catch (Exception loadex) {
			log.warn(loadex.getClass().toString());
			log.error("Cannot load from URL " + loadex.getMessage(), loadex);
		}
		return module;
	}

	public Iterator<Module> getAllModules() {
		return htModules.values().iterator();
	}

	/**
	 * Creates a {@link Module} and configures it with all information taken from the mandator's dcf.
	 * 
	 * @param dcfelement the randomly named element containing the {@code dcfconfig} element
	 * @param contentdata XML content for the module (the way CONQUEST saves it)
	 * 
	 * @see de.juwimm.cms.content.modules.ModuleFactory#getModuleInstance(org.w3c.dom.Element, org.w3c.dom.Node) */
	public Module getModuleInstance(Element dcfElement, Node contentdata) {
		Module module = null;
		String classname = "";
		List<String> jarClassPath = new ArrayList<String>();
		try {
			classname = XercesHelper.getNodeValue(dcfElement, "./dcfConfig/classname");
			Iterator it = XercesHelper.findNodes(dcfElement, "./dcfConfig/classpath/jar");
			while (it.hasNext()) {
				Node node = (Node) it.next();
				jarClassPath.add(XercesHelper.getNodeValue(node));
			}
			module = getModuleInstanceUnconfigured(classname, jarClassPath);

			String label = dcfElement.getAttribute("label");
			String description = dcfElement.getAttribute("description");
			String dcfname = dcfElement.getAttribute("dcfname");
			String mandatory = XercesHelper.getNodeValue(dcfElement, "./dcfConfig/mandatory");
			module.setLabel(label);
			module.setDescription(description);
			module.setName(dcfname);
			if (mandatory.equals("true")) {
				module.setMandatory(true);
			} else {
				module.setMandatory(false);
			}

			// Fill the custom properties for this dcfmodule explicit for this DCF
			Iterator ni = XercesHelper.findNodes(dcfElement, "./dcfConfig/property");

			setCustomProperties(module, ni);

			if (module instanceof Iteration) {
				Node itEl = XercesHelper.findNode(dcfElement, "./dcfConfig/iterationElements");
				((Iteration) module).setIterationElements(itEl);
			}

			//Load initial config if there is no current content
			Node ndeInitialContent = XercesHelper.findNode(dcfElement, "./dcfInitial");
			if (ndeInitialContent != null) {
				htInitialContent.put(module.getName(), ndeInitialContent);
			}
			if (contentdata == null) {
				contentdata = ndeInitialContent;
			}
			if (contentdata != null) {
				module.setProperties(contentdata);
			}
		} catch (Exception exe) {
			log.error("Error getting Module instance", exe);
		}
		this.htModules.put(module.getName(), module);
		return module;
	}

	/** 
	 * Parses the specified {@code propertyNodes} and prepares the information, 
	 * calls {@see Module#setCustomProperties(String, Properties)} 
	 * 
	 * @see ModuleFactory#setCustomProperties(Module, java.util.Iterator) */
	public void setCustomProperties(Module module, Iterator propertyNodes) {
		try {
			Element property;
			while (propertyNodes.hasNext()) {
				property = (Element) propertyNodes.next();
				String propertyname = property.getAttribute("name");
				Properties prop = new Properties();

				Iterator pi = XercesHelper.findNodes(property, "./*");
				Node propParameter;
				while (pi.hasNext()) {
					propParameter = (Element) pi.next();
					String propVal = "";
					if (propParameter.getNodeName().equalsIgnoreCase("properties")) {
						propVal = XercesHelper.node2string(propParameter);
					} else {
						propVal = XercesHelper.getNodeValue(propParameter);
					}
					prop.setProperty(propParameter.getNodeName(), propVal);
				}
				module.setCustomProperties(propertyname, prop);
			}
			module.setCustomProperties("CustomConfigurationReady", new Properties());

		} catch (Exception exe) {
			log.error("Error setting custom properties", exe);
		}
		if (log.isDebugEnabled()) log.debug("SetModuleParameters end " + module.getName());
	}

	public Module getModuleByDCFName(String dcfname) {
		return this.htModules.get(dcfname);
	}

	public void reconfigureModules(Hashtable<String, Element> htModuleDcfNameDcfElement) {
		Collection coll = this.htModules.values();
		Iterator it = coll.iterator();
		while (it.hasNext()) {
			Module module = (Module) it.next();
			Thread t = new Thread(Thread.currentThread().getThreadGroup(), new ReconfigureModuleRunnable(module, htModuleDcfNameDcfElement, htInitialContent));
			t.setPriority(Thread.NORM_PRIORITY);
			t.setName("ReconfigureModuleRunnable");
			t.start();
		}
	}

	/**
	 * Thread calling <code>setProperties(Node)</code> on the given Module with the Node from the DCF
	 */
	public class ReconfigureModuleRunnable implements Runnable {
		private Hashtable<String, Element> htModuleDcfNameDcfElement = null;
		private Hashtable<String, Node> htInitialContent = null;
		private Module module = null;

		public ReconfigureModuleRunnable(Module module, Hashtable<String, Element> htModuleDcfNameDcfElement, Hashtable<String, Node> htInitialContent) {
			this.module = module;
			this.htModuleDcfNameDcfElement = htModuleDcfNameDcfElement;
			this.htInitialContent = htInitialContent;
		}

		public void run() {
			Node content = (Node) htModuleDcfNameDcfElement.get(module.getName());
			if (content != null && content.hasChildNodes()) {
				module.setProperties(content);
			} else {
				module.setProperties((Node) htInitialContent.get(module.getName()));
			}
		}
	}

	public JPanel getPanelForModule(Module module) {
		ContentBorder cb = new ContentBorderModulePanel();
		cb.setContentModulePanel(module.viewPanelUI());
		cb.setLabel(module.getLabel());
		return (JPanel) cb;
	}

	public String isModuleValid() {
		String retVal = "";
		Iterator it = this.htModules.values().iterator();
		while (it.hasNext()) {
			Module mod = (Module) it.next();
			log.info("VALIDATION: FOUND MOD " + mod.getName());
			if (!mod.isModuleValid()) {
				if (!retVal.equals("")) retVal += "<br><hr>";
				String prepend = Messages.getString("content.moduleFactory.validationPrepend", mod.getLabel());
				String errMsg = mod.getValidationError();
				errMsg = errMsg.replaceAll("[\n]", "<br>");
				retVal += "<font face=\"Arial, Helvetica, sans-serif\"><b>" + prepend + "</b><br>" + errMsg + "</font>";
			}
		}
		return retVal;
	}

	public void setEnabled(boolean enable) {
		Iterator it = this.htModules.values().iterator();
		while (it.hasNext()) {
			Module mod = (Module) it.next();
			SwingUtilities.invokeLater(new SetEnabled(mod, enable));
		}
	}

	/**
	 * 
	 */
	private class SetEnabled implements Runnable {
		private Module mod;
		private boolean enable;

		public SetEnabled(Module mod, boolean enable) {
			this.mod = mod;
			this.enable = enable;
		}

		public void run() {
			try {
				mod.setEnabled(enable);
			} catch (Exception exe) {
				log.error("Error setting enabled in setEnabled thread", exe);
			}
		}
	}

	private URLClassLoader getURLClassLoader(URL[] url) {
		ClassLoader previousClassLoader = Thread.currentThread().getContextClassLoader();
		URLClassLoader cl = new PluginURLClassLoader(url, previousClassLoader);
		Thread.currentThread().setContextClassLoader(cl);

		return cl;
	}

	/**
	 * Special URLClassLoader granting all permissions
	 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
	 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
	 * @version $Id$
	 */
	public class PluginURLClassLoader extends URLClassLoader {

		public PluginURLClassLoader(URL[] url, ClassLoader parent) {
			super(url, parent);
		}

		protected PermissionCollection getPermissions(CodeSource codesource) {
			PermissionCollection perms = super.getPermissions(codesource);
			perms.add(new AllPermission());
			return perms;
		}

	}

}