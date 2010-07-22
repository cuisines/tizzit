/**
 * @author rhertzfeldt
 * @lastChange 10:21:38 AM
 */
package org.tizzit.plugins.server.transformer;

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

import java.util.Stack;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import de.juwimm.cms.beans.PluginManagement;
import de.juwimm.cms.plugins.server.Request;
import de.juwimm.cms.plugins.server.Response;
import de.juwimm.cms.plugins.server.TizzitPlugin;

/**
 *
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id: PluginContentHandler.java 543 2009-10-23 11:33:02Z eduard.siebert@online.de $
 */
public final class BaseContentHandler implements ContentHandler {
	private static Logger log = Logger.getLogger(BaseContentHandler.class);
	
	private ContentHandler parent = null;
	private Request request = null;
	private Integer viewComponentId = null;
	private Integer siteId = null;
	private Response response = null;
	private PluginManagement pluginManagement;
	private Stack<PluginMapping> pluginStack = null;

	//private String startLocalName = "";

	public BaseContentHandler(PluginManagement pluginManagement, ContentHandler parent, Request request, Response response, Integer viewComponentId, Integer siteId) {
		this.parent = parent;
		this.request = request;
		this.viewComponentId = viewComponentId;
		this.pluginManagement = pluginManagement;
		this.siteId = siteId;
		this.response = response;
		this.pluginStack = new Stack<PluginMapping>();
		this.pluginStack.push(new PluginMapping("", this.parent, null));
	}
	
	public BaseContentHandler(PluginManagement pluginManagement, ContentHandler parent) {
		this.parent = parent;
		this.pluginManagement = pluginManagement;
		this.pluginStack = new Stack<PluginMapping>();
		this.pluginStack.push(new PluginMapping("", this.parent, null));
	}

	public void setDocumentLocator(Locator locator) {
	}

	public void startDocument() throws SAXException {
		this.parent.startDocument();
	}

	public void endDocument() throws SAXException {
		this.parent.endDocument();
	}

	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		this.parent.startPrefixMapping(prefix, uri);
	}

	public void endPrefixMapping(String prefix) throws SAXException {
		this.parent.endPrefixMapping(prefix);
	}

	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		if (uri != null && uri.startsWith(de.juwimm.cms.plugins.Constants.PLUGIN_NAMESPACE)) {
			if (!this.pluginStack.peek().getNamespaceUri().equals(uri)) {
				// new plugin
				ManagedTizzitPlugin plugin = this.getPlugin(uri);
				if (plugin != null) {
					plugin.configurePlugin(this.request, this.response, this.parent, this.viewComponentId);
					plugin.setup(this, uri);
					this.pluginStack.push(new PluginMapping(uri, plugin, localName));
				}
			} else if (this.pluginStack.peek().getNamespaceUri().equals(uri) && this.pluginStack.peek().getElementName().equals(localName)) {
				// PluginA in PluginA
				ManagedTizzitPlugin plugin = this.getPlugin(uri);
				if (plugin != null) {
					plugin.configurePlugin(this.request, this.response, this.parent, this.viewComponentId);
					plugin.setup(this, uri);
					this.pluginStack.push(new PluginMapping(uri, plugin, localName));
				}
			}
		}
		this.pluginStack.peek().getContentHandler().startElement(uri, localName, qName, atts);
		
	}
	
	private ManagedTizzitPlugin getPlugin(String name){
		String pre = "org.tizzit.plugins.server.transformer.";
		String mid = name.substring(name.lastIndexOf("/")+1);
		String suf = "Plugin";
		String className = pre+mid+suf;
		log.info("gimme plugin for: "+className);
		ManagedTizzitPlugin retval = null;
		try{
			retval = (ManagedTizzitPlugin)Class.forName(className).newInstance();
		}catch(Exception e){	
			log.warn("could not instanciate class: " +className);
		}
		return retval;
	}
	

	public void endElement(String uri, String localName, String qName) throws SAXException {
		this.pluginStack.peek().getContentHandler().endElement(uri, localName, qName);

		if (uri.startsWith(de.juwimm.cms.plugins.Constants.PLUGIN_NAMESPACE)) {
			if (localName.equals(this.pluginStack.peek().getElementName())) {
				try {
					((TizzitPlugin) this.pluginStack.pop().getContentHandler()).processContent();
				} catch (Exception exe) {
				}
			}
		}
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		this.pluginStack.peek().getContentHandler().characters(ch, start, length);
	}

	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		this.parent.ignorableWhitespace(ch, start, length);
	}

	public void processingInstruction(String target, String data) throws SAXException {
		this.parent.processingInstruction(target, data);
	}

	public void skippedEntity(String name) throws SAXException {
		this.parent.skippedEntity(name);
	}

	private class PluginMapping {
		String namespaceUri = null;
		ContentHandler contentHandler = null;
		String elementName = null;

		public PluginMapping(String namespaceUri, ContentHandler contentHandler, String elementName) {
			this.namespaceUri = namespaceUri;
			this.contentHandler = contentHandler;
			this.elementName = elementName;
		}

		/**
		 * @param namespaceUri the namespaceUri to set
		 */
		public void setNamespaceUri(String namespaceUri) {
			this.namespaceUri = namespaceUri;
		}

		/**
		 * @param contentHandler the contentHandler to set
		 */
		public void setContentHandler(ContentHandler contentHandler) {
			this.contentHandler = contentHandler;
		}

		/**
		 * @return the namespaceUri
		 */
		public String getNamespaceUri() {
			return namespaceUri;
		}

		/**
		 * @return the contentHandler
		 */
		public ContentHandler getContentHandler() {
			return contentHandler;
		}

		/**
		 * @return the elementName
		 */
		public String getElementName() {
			return elementName;
		}

		/**
		 * @param elementName the elementName to set
		 */
		public void setElementName(String elementName) {
			this.elementName = elementName;
		}
	}
}
