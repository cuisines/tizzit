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
package de.juwimm.cms.util;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.log4j.Logger;
import org.tizzit.util.Base64;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;


/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class EditionBlobContentHandler implements ContentHandler {
	private static Logger log = Logger.getLogger(EditionBlobContentHandler.class);
	private ContentHandler parent = null;
	private File directory = null;
	private String previousElement = "";
	private String currentElement = "";
	private String lastFoundId = "";
	private String blobElementFileId = "";

	private Base64.OutputStream out = null;

	public EditionBlobContentHandler(ContentHandler parent, File directory) {
		this.parent = parent;
		this.directory = directory;
	}

	public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		String id = attrs.getValue("id");
		if (id != null) {
			lastFoundId = id;
		}

		previousElement = currentElement;
		currentElement = qName;

		if (previousElement.equals("picture") && currentElement.equals("thumbnail")) {
			blobElementFileId = "t" + lastFoundId;
		} else if (previousElement.equals("thumbnail") && currentElement.equals("file")) {
			blobElementFileId = "f" + lastFoundId;
		} else if (previousElement.equals("file") && currentElement.equals("preview")) {
			blobElementFileId = "p" + lastFoundId;
		} else if (previousElement.equals("document") && currentElement.equals("file")) {
			blobElementFileId = "d" + lastFoundId;
		} else {
			blobElementFileId = "";
		}

		if (!blobElementFileId.equalsIgnoreCase("")) {
			try {
				String fileName = directory.getParent() + File.separator + blobElementFileId;
				if (log.isDebugEnabled()) log.debug("File writing to: " + fileName);
				out = new Base64.OutputStream(new FileOutputStream(fileName, false), false);
			} catch (Exception exe) {
				log.error("Error occured", exe);
			}
		}
		parent.startElement(uri, localName, qName, attrs);
	}

	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (!blobElementFileId.equalsIgnoreCase("")) {
			if (log.isDebugEnabled()) log.debug("File successfully written!");
			try {
				out.flush();
				out.close();
				out = null;
			} catch (Exception exe) {
				log.error("Error occured", exe);
			}
			blobElementFileId = "";
		}
		parent.endElement(namespaceURI, localName, qName);
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		if (!blobElementFileId.equalsIgnoreCase("")) {
			try {
				for (int i = start; i < start + length; i++) {
					out.write((int) ch[i]);
				}
			} catch (Exception exe) {
				log.error("Error occured", exe);
			}
		} else {
			parent.characters(ch, start, length);
		}
	}

	public void startDocument() throws SAXException {
		if (log.isDebugEnabled()) log.debug("startDocument");
		parent.startDocument();
	}

	public void endDocument() throws SAXException {
		if (log.isDebugEnabled()) log.debug("endDocument");
		parent.endDocument();
	}

	//unused ------------------------------------------------------------------------------------------
	public void setDocumentLocator(Locator locator) {
		parent.setDocumentLocator(locator);
	}

	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		parent.startPrefixMapping(prefix, uri);
	}

	public void endPrefixMapping(String prefix) throws SAXException {
		parent.endPrefixMapping(prefix);
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