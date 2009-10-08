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
package de.juwimm.cms.beans;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.tizzit.core.classloading.ClassloadingHelper;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import de.juwimm.cms.plugins.server.Request;
import de.juwimm.cms.plugins.server.Response;
import de.juwimm.cms.plugins.server.TizzitPlugin;

/**
 * 
 * @author <a href="mailto:j2ee@juwimm.com">Sascha-Matthias Kulawik</a>
 * company Juwi|MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since tizzit-core 08.10.2009
 */
public class PluginManagement {
	private Logger log = Logger.getLogger(PluginManagement.class);
	private Properties props = null;

	public PluginManagement() {		
		try {
			URL cp = this.getClass().getResource("/tizzit-plugins.properties");
			InputStream is = new FileInputStream(cp.getFile());
			props = new Properties();
			is = new FileInputStream(cp.getFile());
			props.load(is);
		} catch (FileNotFoundException e) {
			log.error("Could not find tizzit-plugins.properties - no plugins will be available!");
		} catch (Exception e) {
			log.error("Could not find tizzit-plugins.properties - no plugins will be available!");
		}
	}

	public TizzitPlugin getPlugin(String namespace) {
		TizzitPlugin plugin = null;
		String clazzName = props.getProperty(namespace);
		if(clazzName == null) {
			log.warn("Could not find Plugin for Namespace: " + namespace + "!");
			plugin = new NullPlugin();
		} else {
			try {
				plugin = (TizzitPlugin) ClassloadingHelper.getInstance(clazzName);
			} catch(Exception exe) {
				log.warn("Could not resolve TizzitPlugin with classname " + clazzName + " " + exe.getMessage());
				plugin = new NullPlugin();
			}
		}
		return plugin;
	}
	
	public class NullPlugin implements TizzitPlugin {
		ContentHandler parent = null;
		
		public void configurePlugin(Request request, Response response, ContentHandler parent, Integer uniquePageId) {
			this.parent = parent;
		}

		public Date getLastModifiedDate() {
			return new Date(0L);
		}

		public boolean isCacheable() {
			return false;
		}

		public void processContent() {
		}

		public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
			parent.characters(arg0, arg1, arg2);
		}

		public void endDocument() throws SAXException {
			parent.endDocument();
		}

		public void endElement(String arg0, String arg1, String arg2) throws SAXException {
			parent.endElement(arg0, arg1, arg2);
		}

		public void endPrefixMapping(String arg0) throws SAXException {
			parent.endPrefixMapping(arg0);
		}

		public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {
			parent.ignorableWhitespace(arg0, arg1, arg2);
		}

		public void processingInstruction(String arg0, String arg1) throws SAXException {
			parent.processingInstruction(arg0, arg1);
		}

		public void setDocumentLocator(Locator arg0) {
			parent.setDocumentLocator(arg0);
		}

		public void skippedEntity(String arg0) throws SAXException {
			parent.skippedEntity(arg0);
		}

		public void startDocument() throws SAXException {
			parent.startDocument();
		}

		public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {
			parent.startElement(arg0, arg1, arg2, arg3);
		}

		public void startPrefixMapping(String arg0, String arg1) throws SAXException {
			parent.startPrefixMapping(arg0, arg1);
		}
	}
}
