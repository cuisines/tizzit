package de.juwimm.util.xml.ns;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * 
 * @author <a href="mailto:j2ee@juwimm.com">Sascha-Matthias Kulawik</a>
 * company Juwi|MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since juwimm-linde-cocoon22 28.05.2008
 */
public final class SAXHelper {
	private SAXHelper() {
	}

	public static void addElement(ContentHandler handler, String namespace, String elementName, String elementValue, Attributes attr) throws SAXException {
		if (elementName != null) {
			handler.startElement(namespace, elementName, elementName, attr);
			String saveVal = (elementValue == null) ? "" : elementValue;
			char[] ca = saveVal.toCharArray();
			handler.characters(ca, 0, ca.length);
			handler.endElement(namespace, elementName, elementName);
		}
	}

	public static void addElement(ContentHandler handler, String namespace, String elementName, String elementValue) throws SAXException {
		addElement(handler, namespace, elementName, elementValue, new AttributesImpl());
	}

	public static void addElement(ContentHandler handler, String namespace, String elementName, Integer elementValue) throws SAXException {
		String saveVal = (elementValue == null) ? "" : elementValue.toString();
		addElement(handler, namespace, elementName, saveVal, new AttributesImpl());
	}

	public static void addElement(ContentHandler handler, String namespace, String elementName, Boolean elementValue) throws SAXException {
		String saveVal = (elementValue == null) ? "" : elementValue.toString();
		addElement(handler, namespace, elementName, saveVal, new AttributesImpl());
	}

	/**
	 * Sets an attribute of an SAX AttributesImpl to a specific value.<br/>
	 * If it exists, the value will be overwritten. 
	 * 
	 * @param attr The AttributesImpl
	 * @param attrName The name
	 * @param attrValue The (new) value
	 */
	public static void setSAXAttr(AttributesImpl attr, String namespace, String attrName, String attrValue) {
		if (attrName != null) {
			int idx = attr.getIndex(attrName);
			String saveVal = (attrValue == null) ? "" : attrValue;
			if (idx >= 0) {
				String dd = attr.getType(attrName);
				attr.setAttribute(idx, namespace, attrName, attrName, dd, saveVal);
			} else {
				attr.addAttribute(namespace, attrName, attrName, "CDATA", saveVal);
			}
		}
	}

}