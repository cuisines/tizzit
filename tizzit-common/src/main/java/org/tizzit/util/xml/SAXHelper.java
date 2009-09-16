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

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * <p>Title: ConQuest</p>
 * <p>Description: Enterprise Content Management</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Revision: 1.32 $
 */
public final class SAXHelper {
	private SAXHelper() {
	}

	public static void addElement(ContentHandler handler, String elementName, String elementValue, Attributes attr)
			throws SAXException {
		if(elementName != null) {
			handler.startElement("", elementName, elementName, attr);
			String saveVal = (elementValue == null) ? "" : elementValue;
			char[] ca = saveVal.toCharArray();
			handler.characters(ca, 0, ca.length);
			handler.endElement("", elementName, elementName);
		}
	}

	public static void addElement(ContentHandler handler, String elementName, String elementValue) throws SAXException {
		addElement(handler, elementName, elementValue, new AttributesImpl());
	}
	
	public static void addElement(ContentHandler handler, String elementName, Integer elementValue) throws SAXException {
		String saveVal = (elementValue == null) ? "" : elementValue.toString();
		addElement(handler, elementName, saveVal, new AttributesImpl());
	}
	
	public static void addElement(ContentHandler handler, String elementName, Boolean elementValue) throws SAXException {
		String saveVal = (elementValue == null) ? "" : elementValue.toString();
		addElement(handler, elementName, saveVal, new AttributesImpl());
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
		if (attrName != null) {
			int idx = attr.getIndex(attrName);
			String saveVal = (attrValue == null) ? "" : attrValue;
			if (idx >= 0) {
				String dd = attr.getType(attrName);
				attr.setAttribute(idx, "", attrName, attrName, dd, saveVal);
			} else {
				attr.addAttribute("", attrName, attrName, "CDATA", saveVal);
			}
		}
	}

}