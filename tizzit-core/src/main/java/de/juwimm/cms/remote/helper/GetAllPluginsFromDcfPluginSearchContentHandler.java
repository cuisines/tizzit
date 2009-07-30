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
package de.juwimm.cms.remote.helper;

import java.util.HashMap;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import de.juwimm.cms.plugins.Constants;
import de.juwimm.cms.vo.ContentPluginValue;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class GetAllPluginsFromDcfPluginSearchContentHandler extends DefaultHandler {
	private static Logger log = Logger.getLogger(GetAllPluginsFromDcfPluginSearchContentHandler.class);
	private HashMap<String, ContentPluginValue> plugins = null;
	private boolean delegate = false;
	private StringBuffer charStringBuffer = null;
	private ContentPluginValue plugin = null;
	private Integer siteId = null;

	public GetAllPluginsFromDcfPluginSearchContentHandler(HashMap<String, ContentPluginValue> plugins, Integer siteId) {
		this.plugins = plugins;
		this.siteId = siteId;
	}

	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		charStringBuffer = new StringBuffer();
		if (!delegate && uri != null && uri.startsWith(Constants.PLUGIN_NAMESPACE)) {
			delegate = true;
			plugin = new ContentPluginValue();
			plugin.setClasspath(new String[0]);
			plugin.setNamespace(uri);
			plugin.setSiteId(siteId);
		}

	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		if (delegate) {
			for (int i = start; i < start + length; i++) {
				charStringBuffer.append(ch[i]);
			}
		}
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (delegate) {
			//charStringBuffer
			if (localName.equalsIgnoreCase("classname")) {
				plugin.setModuleClassname(charStringBuffer.toString());
			} else if (localName.equalsIgnoreCase("generatorClassname")) {
				plugin.setGeneratorClassname(charStringBuffer.toString());
			} else if (localName.equalsIgnoreCase("jar")) {
				plugin.setClasspath((String[]) ArrayUtils.add(plugin.getClasspath(), charStringBuffer.toString()));
			}
		}

		if (delegate && uri.startsWith(de.juwimm.cms.plugins.Constants.PLUGIN_NAMESPACE)) {
			if (plugins.containsKey(plugin.getNamespace())) {
				log.debug("Plugin " + plugin.getNamespace() + " already in map - ignored");
			} else {
				plugins.put(plugin.getNamespace(), plugin);
			}
			plugin = null;
			delegate = false;
		}
	}
}