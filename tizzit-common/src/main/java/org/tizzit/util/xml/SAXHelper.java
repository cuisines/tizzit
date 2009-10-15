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
package org.tizzit.util.xml;

import java.io.StringReader;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <p>Title: ConQuest</p>
 * <p>Description: Enterprise Content Management</p>
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <b>Namespace support since 14.10.2009</b>
 *
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * @version $Revision: 1.32 $
 */
public final class SAXHelper {
	private static final Log log = LogFactory.getLog(SAXHelper.class);

	private SAXHelper() {
	}

	/**
	 * @since tizzit-common 15.10.2009
	 */
	public static void addElement(ContentHandler handler, String namespaceUri, String elementName, String elementValue, Attributes attr) throws SAXException {
		if (elementName != null) {
			handler.startElement(namespaceUri, elementName, elementName, attr);
			String saveVal = (elementValue == null) ? "" : elementValue;
			char[] ca = saveVal.toCharArray();
			handler.characters(ca, 0, ca.length);
			handler.endElement(namespaceUri, elementName, elementName);
		}
	}

	public static void addElement(ContentHandler handler, String elementName, String elementValue) throws SAXException {
		addElement(handler, "", elementName, elementValue);
	}

	public static void addElement(ContentHandler handler, String elementName, Integer elementValue) throws SAXException {
		addElement(handler, "", elementName, elementValue);
	}

	public static void addElement(ContentHandler handler, String elementName, Boolean elementValue) throws SAXException {
		addElement(handler, "", elementName, elementValue);
	}

	/**
	 * @since tizzit-common 15.10.2009
	 */
	public static void addElement(ContentHandler handler, String namespaceUri, String elementName, String elementValue) throws SAXException {
		addElement(handler, namespaceUri, elementName, elementValue, new AttributesImpl());
	}

	/**
	 * @since tizzit-common 15.10.2009
	 */
	public static void addElement(ContentHandler handler, String namespaceUri, String elementName, Integer elementValue) throws SAXException {
		String saveVal = (elementValue == null) ? "" : elementValue.toString();
		addElement(handler, namespaceUri, elementName, saveVal, new AttributesImpl());
	}

	/**
	 * @since tizzit-common 15.10.2009
	 */
	public static void addElement(ContentHandler handler, String namespaceUri, String elementName, Boolean elementValue) throws SAXException {
		String saveVal = (elementValue == null) ? "" : elementValue.toString();
		addElement(handler, namespaceUri, elementName, saveVal, new AttributesImpl());
	}

	/**
	 * Sets an attribute of an SAX AttributesImpl to a specific value.<br/>
	 * If it exists, the value will be overwritten.
	 *
	 * @param attr The AttributesImpl
	 * @param attrName The name
	 * @param attrValue The (new) value
	 */
	public static void setSAXAttr(AttributesImpl attr, String attrName, String attrValue) {
		setSAXAttr(attr, "", attrName, attrValue);
	}

	/**
	 * Sets an attribute of an SAX AttributesImpl to a specific value (namespace supported).<br/>
	 * If it exists, the value will be overwritten.
	 *
	 * @param attr The AttributesImpl
	 * @param namespaceUri
	 * @param attrName The name
	 * @param attrValue The (new) value
	 * @since tizzit-common 15.10.2009
	 */
	public static void setSAXAttr(AttributesImpl attr, String namespaceUri, String attrName, String attrValue) {
		if (attrName != null) {
			int idx = attr.getIndex(attrName);
			String saveVal = (attrValue == null) ? "" : attrValue;
			if (idx >= 0) {
				String dd = attr.getType(attrName);
				attr.setAttribute(idx, namespaceUri, attrName, attrName, dd, saveVal);
			} else {
				attr.addAttribute(namespaceUri, attrName, attrName, "CDATA", saveVal);
			}
		}
	}

	/**
	 * @param node the string to stream.
	 * @param handler the content handler.
	 * @since tizzit-common 15.10.2009
	 */
	public static void string2sax(String node, ContentHandler handler) {
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.setOutputProperty(OutputKeys.STANDALONE, "no");
			t.setOutputProperty(OutputKeys.METHOD, "xml");
			StreamSource source = new StreamSource(new StringReader(node));
			SAXResult result = new SAXResult(new OmitXmlDeclarationContentHandler(handler));
			t.transform(source, result);
		} catch (Exception exe) {
			log.error("unknown error occured", exe);
		}
	}

	/**
	 * @param node the string to stream.
	 * @param handler the content handler.
	 * @since tizzit-common 15.10.2009
	 */
	public static void string2sax(String node, ContentHandler handler, String encoding) {
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.setOutputProperty(OutputKeys.STANDALONE, "no");
			t.setOutputProperty(OutputKeys.METHOD, "xml");
			if (!"".equals(encoding) && encoding != null) {
				t.setOutputProperty(OutputKeys.ENCODING, encoding);
			}
			StreamSource source = new StreamSource(new StringReader(node));
			SAXResult result = new SAXResult(new OmitXmlDeclarationContentHandler(handler));
			t.transform(source, result);
		} catch (Exception exe) {
			log.error("unknown error occured", exe);
		}
	}

	/**
	 * This ContentHandler is to remove the xml Header. The OMIT_XML_DECLARATION does not work actually.
	 *
	 * @author <a href="mailto:j2ee@juwimm.com">Sascha-Matthias Kulawik</a>
	 * company Juwi|MacMillan Group GmbH, Walsrode, Germany
	 * @version $Id$
	 * @since tizzit-common 15.10.2009
	 */
	public static class OmitXmlDeclarationContentHandler extends DefaultHandler implements ContentHandler {
		private ContentHandler parent;

		public OmitXmlDeclarationContentHandler(ContentHandler parent) {
			this.parent = parent;
		}

		@Override
		public void endDocument() throws SAXException {
		}

		@Override
		public void startDocument() throws SAXException {
		}

		@Override
		public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
			parent.characters(arg0, arg1, arg2);
		}

		@Override
		public void endElement(String arg0, String arg1, String arg2) throws SAXException {
			parent.endElement(arg0, arg1, arg2);
		}

		@Override
		public void endPrefixMapping(String arg0) throws SAXException {
			parent.endPrefixMapping(arg0);
		}

		@Override
		public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {
			parent.ignorableWhitespace(arg0, arg1, arg2);
		}

		@Override
		public void processingInstruction(String arg0, String arg1) throws SAXException {
			parent.processingInstruction(arg0, arg1);
		}

		@Override
		public void skippedEntity(String arg0) throws SAXException {
			parent.skippedEntity(arg0);
		}

		@Override
		public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {
			parent.startElement(arg0, arg1, arg2, arg3);
		}

		@Override
		public void startPrefixMapping(String arg0, String arg1) throws SAXException {
			parent.startPrefixMapping(arg0, arg1);
		}

	}
}