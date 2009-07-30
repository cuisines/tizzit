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
package de.juwimm.cms.content;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import de.juwimm.cms.content.modules.AbstractModule;
import de.juwimm.cms.content.modules.Module;
import de.juwimm.cms.content.modules.ModuleFactory;
import de.juwimm.util.XercesHelper;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class GetContentHandler extends DefaultHandler implements ErrorHandler {
	private static Logger log = Logger.getLogger(GetContentHandler.class);
	private StringBuffer content;
	private String lastStartElement = "";
	private boolean startOrEnd = false;
	private String imInsideThisDCFElement = "";
	private boolean imInsideTitle = false;
	private boolean titleAlreadyWritten = false;
	private String dcfname = "";
	private ModuleFactory mf;
	private String gcontent = "";
	private String title = "";
	private boolean doNotCallModule = false;
	private boolean imInsideDCFInitial = false;
	private boolean imInsideIterationElements = false;

	public GetContentHandler(ModuleFactory moduleFactory, String strTitle) {
		super();
		doNotCallModule = false;
		this.mf = moduleFactory;
		this.title = strTitle;
	}

	/**
	 * This Constructor does NOT call a module for properties!<br>
	 * It is only used for creating an empty content out of a DCF for saving the first time.
	 * @param strTitle Title to be parsed inside the document
	 */
	public GetContentHandler(String strTitle) {
		super();
		doNotCallModule = true;
		this.title = AbstractModule.getURLEncoded(strTitle);
	}

	public void startDocument() {
		if (log.isDebugEnabled()) log.debug("Start document");
		content = new StringBuffer();
		startOrEnd = false;
		gcontent = "";
	}

	public void startElement(String uri, String na2me, String qName, Attributes atts) {
		if (imInsideThisDCFElement.equals("") || imInsideDCFInitial) {
			if (qName.equals("title")) {
				imInsideTitle = true;
			}
			String attributes = "";
			for (int i = 0; i < atts.getLength(); i++) {
				attributes += " " + atts.getQName(i) + "=\"" + XercesHelper.getHexEncoded(atts.getValue(i)) + "\"";
				if (atts.getQName(i).equals("dcfname") && imInsideThisDCFElement.equals("")) {
					imInsideThisDCFElement = qName;
					dcfname = atts.getValue(i);
				}
			}
			lastStartElement = qName;
			if (startOrEnd) content.append(">");
//			 --- URI ---
			content.append("<");
			if(!uri.equalsIgnoreCase("")) {
				content.append(uri).append(':');
			}
			content.append(qName).append(attributes);
//			 --- URI ---
			startOrEnd = true;
		} else if (doNotCallModule && !imInsideDCFInitial && !imInsideIterationElements && qName.equals("dcfInitial")) {
			startOrEnd = true;
			imInsideDCFInitial = true;
		} else if (!imInsideIterationElements && qName.equals("iterationElements")) {
			imInsideIterationElements = true;
			log.debug("SET ITERATION ELEMENTS");
		}
	}

	public void characters(char[] ch, int start, int length) {
		if (imInsideThisDCFElement.equals("") || imInsideDCFInitial) {
			if (imInsideTitle && !titleAlreadyWritten) {
				content.append("><![CDATA[" + title + "]]>");
				lastStartElement = "";
				titleAlreadyWritten = true;
				startOrEnd = true;
			} else {
				StringBuffer tmp = new StringBuffer();
				for (int i = start; i < start + length; i++) {
					tmp.append(ch[i]);
				}
				if (startOrEnd) {
					content.append(">\n");
				}
				content.append(XercesHelper.getHexEncoded(tmp.toString()));
				lastStartElement = "";
				startOrEnd = false;
			}
		}
	}

	public void endElement(String uri, String na2me, String qName) throws SAXException {
		if (imInsideThisDCFElement.equals("") || (imInsideDCFInitial && !qName.equals("dcfInitial"))) {
			if (lastStartElement.equals(qName)) {
				content.append("/>\n");
			} else {
				// --- URI ---
				content.append("</");
				if(!uri.equalsIgnoreCase("")) {
					content.append(uri).append(':');
				}
				content.append(qName).append(">\n");
				// --- URI ---
			}
		} else if (imInsideThisDCFElement.equals(qName)) {
			imInsideThisDCFElement = "";
			if (!doNotCallModule) {
				Module module = mf.getModuleByDCFName(dcfname);
				Node node = module.getProperties();
				String validationError = module.getValidationError();
				if (!validationError.equalsIgnoreCase("")) { throw new SAXException(validationError); }
				String nde = "";
				if (node != null) nde = XercesHelper.nodeList2string(node.getChildNodes());
				log.debug("Added in endElement: " + nde);
				// --- URI ---
				content.append(">").append(nde).append("</");
				if(!uri.equalsIgnoreCase("")) {
					content.append(uri).append(':');
				}
				content.append(qName).append(">\n");
				// --- URI ---
			} else {
				if (lastStartElement.equals(qName)) content.append(">");
				// --- URI ---
				content.append("</");
				if(!uri.equalsIgnoreCase("")) {
					content.append(uri).append(':');
				}
				content.append(qName).append(">\n");
				// --- URI ---
			}
			dcfname = "";
		} else if (imInsideDCFInitial && qName.equals("dcfInitial")) {
			imInsideDCFInitial = false;
		} else if (imInsideIterationElements && qName.equals("iterationElements")) {
			imInsideIterationElements = false;
			log.debug("GOT OUT OF ITERATION ELEMENTS");
		}

		if (qName.equals("title")) imInsideTitle = false;
		startOrEnd = false;
	}

	public void endDocument() {
		if (log.isDebugEnabled()) log.debug("End document ");
		gcontent = content.toString();
	}

	public String getContent() throws Exception {
		validateContent(gcontent);
		return gcontent;
	}

	public static void validateContent(String cnt) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(true);
		dbf.setNamespaceAware(false);

		DocumentBuilder docBuilder = dbf.newDocumentBuilder();
		docBuilder.setErrorHandler(new ValidationErrorHandler());
		String xmlGrammar = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		String document = xmlGrammar + cnt;
		if (log.isDebugEnabled()) log.debug("Content to VALIDATE: " + document);
		InputSource in = new InputSource(new StringReader(document));
		Document doc = docBuilder.parse(in);
		if (log.isDebugEnabled()) {
			if (doc == null) {
				log.debug("DOC IS NULL");
			} else {
				log.debug("ALLES QL");
			}
		}
	}

	/**
	 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
	 * @version $Id$
	 */
	private static class ValidationErrorHandler implements ErrorHandler {
		public void warning(SAXParseException exception) throws SAXException {
			//log.debug("ValidationErrorHandler warning " + exception.getMessage());
		}

		public void error(SAXParseException exception) throws SAXException {
			//log.debug("ValidationErrorHandler error " + exception.getMessage());
		}

		public void fatalError(SAXParseException exception) throws SAXException {
			log.debug("ValidationErrorHandler fatalError ", exception);
		}
	}
}