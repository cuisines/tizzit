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
package de.juwimm.cms.cocoon.generation.helper;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import de.juwimm.cms.beans.PluginManagement;
import de.juwimm.cms.plugins.server.TizzitPlugin;
import de.juwimm.cms.plugins.server.Request;
import de.juwimm.cms.plugins.server.Response;

/**
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public final class PluginContentHandler implements ContentHandler {
	private static Logger log = Logger.getLogger(PluginContentHandler.class);
	private ContentHandler parent = null;
	private boolean delegate = false;
	private TizzitPlugin plugin;
	private Request request = null;
	private Integer viewComponentId = null;
	private Integer siteId = null;
	private Response response = null;
	private PluginManagement pluginManagement;

	public PluginContentHandler(PluginManagement pluginManagement, ContentHandler parent, Request request, Response response, Integer viewComponentId, Integer siteId) {
		this.parent = parent;
		this.request = request;
		this.viewComponentId = viewComponentId;
		this.pluginManagement = pluginManagement;
		this.siteId = siteId;
		this.response = response;
	}

	public void setDocumentLocator(Locator locator) {
	}

	public void startDocument() throws SAXException {
		parent.startDocument();
	}

	public void endDocument() throws SAXException {
		parent.endDocument();
	}

	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		parent.startPrefixMapping(prefix, uri);
	}

	public void endPrefixMapping(String prefix) throws SAXException {
		parent.endPrefixMapping(prefix);
	}

	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		if (!delegate && uri != null && uri.startsWith(de.juwimm.cms.plugins.Constants.PLUGIN_NAMESPACE)) {
			plugin = pluginManagement.getPlugin(uri);
			if (plugin == null) {
				if (log.isDebugEnabled()) log.debug("NO PLUGIN, WILL DELEGATE TO THE PARENT");
				delegate = false;
			} else {
				if (log.isDebugEnabled()) log.debug("CONFIGURING PLUGIN");
				plugin.configurePlugin(request, this.response, this.parent, viewComponentId);
				delegate = true;
			}
		}

		if (delegate) {
			plugin.startElement(uri, localName, qName, atts);
		} else {
			parent.startElement(uri, localName, qName, atts);
		}
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (delegate) {
			// weiterleiten
			plugin.endElement(uri, localName, qName);
		} else {
			parent.endElement(uri, localName, qName);
		}

		if (delegate && uri.startsWith(de.juwimm.cms.plugins.Constants.PLUGIN_NAMESPACE)) {
			plugin.processContent();
			plugin = null;
			delegate = false;
		}
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		if (delegate) {
			// weiterleiten
			plugin.characters(ch, start, length);
		} else {
			parent.characters(ch, start, length);
		}
	}

	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		parent.ignorableWhitespace(ch, start, length);
	}

	public void processingInstruction(String target, String data) throws SAXException {
		parent.processingInstruction(target, data);
	}

	public void skippedEntity(String name) throws SAXException {
		parent.skippedEntity(name);
	}

}
