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
package org.tizzit.cocoon.generic.sax;

import java.util.HashSet;

import org.apache.log4j.Logger;
import org.tizzit.util.xml.SAXHelper;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 *
 * @author <a href="carsten.schalm@juwimm.com">Carsten Schalm</a>
 * @version $Id$
 */
public class SVGMarkContentHandler implements ContentHandler {
	private static Logger log = Logger.getLogger(SVGMarkContentHandler.class);
	private static final String MARK_COLOR = "red";
	private ContentHandler parent = null;
	@SuppressWarnings("unchecked")
	private HashSet polygons = null;

	@SuppressWarnings("unchecked")
	public SVGMarkContentHandler(ContentHandler parent, HashSet polygons) {
		this.parent = parent;
		this.polygons = polygons;
	}

	public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		String id = attrs.getValue("id");
		AttributesImpl newAttr = new AttributesImpl(attrs);
		if (this.polygons.contains(id)) {
			SAXHelper.setSAXAttr(newAttr, "fill", SVGMarkContentHandler.MARK_COLOR);
		}
		parent.startElement(uri, localName, qName, newAttr);
	}

	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		this.parent.endElement(namespaceURI, localName, qName);
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		this.parent.characters(ch, start, length);
	}

	public void startDocument() throws SAXException {
		if (log.isDebugEnabled()) log.debug("startDocument");
		this.parent.startDocument();
	}

	public void endDocument() throws SAXException {
		if (log.isDebugEnabled()) log.debug("endDocument");
		this.parent.endDocument();
	}

	//unused ------------------------------------------------------------------------------------------
	public void setDocumentLocator(Locator locator) {
		this.parent.setDocumentLocator(locator);
	}

	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		this.parent.startPrefixMapping(prefix, uri);
	}

	public void endPrefixMapping(String prefix) throws SAXException {
		this.parent.endPrefixMapping(prefix);
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

}
