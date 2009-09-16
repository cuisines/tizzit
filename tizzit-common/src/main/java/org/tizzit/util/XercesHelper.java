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
package org.tizzit.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jaxen.BaseXPath;
import org.jaxen.dom.DOMXPath;
import org.tizzit.util.tidy.Configuration;
import org.tizzit.util.tidy.Tidy;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;


/**
 * Helperclass for XML/XPath Functionality.
 * <p>Copyright: Copyright JuwiMacMillan Group GmbH (c) 2003</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public final class XercesHelper {
	private static Log log = LogFactory.getLog(XercesHelper.class);
	private static DocumentBuilder docBuilder = null;
	private static DocumentBuilderFactory dbf = null;

	private static final String ENTITIES_RESOURCE = "/HTMLEntities.res";
	private static Hashtable byChar;
	private static Hashtable byName;

	static {
		initialize();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(false);
			dbf.setNamespaceAware(false);
			//dbf.setExpandEntityReferences(false);
			//dbf.setAttribute("http://xml.org/sax/features/validation", "false");
			//dbf.setAttribute("http://apache.org/xml/features/nonvalidating/load-external-dtd", "false");
			docBuilder = dbf.newDocumentBuilder();
		} catch (Exception exe) {
			log.error("Fatal Error occured while getting the DocumentBuilderFactory", exe);
		}
	}

	private XercesHelper() {
	}

	private static void initialize() {
		InputStream is = null;
		BufferedReader reader = null;
		int index;
		String name;
		String value;
		int code;
		String line;

		// Make sure not to initialize twice.
		if (byName != null) return;
		try {
			byName = new Hashtable();
			byChar = new Hashtable();
			is = XercesHelper.class.getResourceAsStream(ENTITIES_RESOURCE);
			if (is == null)
					throw new RuntimeException("SER003 The resource [" + ENTITIES_RESOURCE + "] could not be found.\n"
							+ ENTITIES_RESOURCE);
			reader = new BufferedReader(new InputStreamReader(is));
			line = reader.readLine();
			while (line != null) {
				if (line.length() == 0 || line.charAt(0) == '#') {
					line = reader.readLine();
					continue;
				}
				index = line.indexOf(' ');
				if (index > 1) {
					name = line.substring(0, index);
					++index;
					if (index < line.length()) {
						value = line.substring(index);
						index = value.indexOf(' ');
						if (index > 0) value = value.substring(0, index);
						code = Integer.parseInt(value);
						defineEntity(name, (char) code);
					}
				}
				line = reader.readLine();
			}
			is.close();
		} catch (Exception except) {
			throw new RuntimeException("SER003 The resource [" + ENTITIES_RESOURCE + "] could not load: "
					+ except.toString() + "\n" + ENTITIES_RESOURCE + "\t" + except.toString());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception except) {
				}
			}
		}
	}

	private static void defineEntity(String name, char value) {
		if (byName.get(name) == null) {
			byName.put(name, new Integer(value));
			byChar.put(new Integer(value), name);
		}
	}

	public static synchronized Document getNewDocument() {
		Document doc = null;
		try {
			doc = docBuilder.newDocument();
		} catch (Exception exe) {
			log.error("unknown error occured", exe);
		}
		return doc;
	}

	public static String doc2String(Document doc) {
		StringWriter stringOut = new StringWriter();
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.setOutputProperty(OutputKeys.METHOD, "xml");
			t.transform(new DOMSource(doc), new StreamResult(stringOut));
			
			/*OutputFormat format = new OutputFormat(doc, "ISO-8859-1", true);
			format.setOmitXMLDeclaration(true);
			format.setOmitDocumentType(true);
			stringOut = new StringWriter();
			XMLSerializer serial = new XMLSerializer(stringOut, format);
			serial.asDOMSerializer();
			serial.serialize(doc);*/
		} catch (Exception exe) {
			log.error("unknown error occured", exe);
		}
		return stringOut.toString();
	}

	public static String node2Html(Node node) {
		Document doc = getNewDocument();
		Node newnde = doc.importNode(node, true);
		doc.appendChild(newnde);

		StringWriter stringOut = new StringWriter();
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			// for "XHTML" serialization, use the output method "xml" and set publicId as shown

			/*t.setOutputProperty(OutputKeys.METHOD, "xml");
			 t.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//W3C//DTD XHTML 1.0 Transitional//EN");
			 t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
			 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");*/

			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.setOutputProperty(OutputKeys.METHOD, "xml");

			t.transform(new DOMSource(doc), new StreamResult(stringOut));
		} catch (Exception exe) {
			log.error("unknown error occured", exe);
		}
		return stringOut.toString();
	}

	public static Node findNode(Node node, String xpathquery) throws Exception {
		Iterator it = findNodes(node, xpathquery);
		if (it.hasNext()) {
			return (Node) it.next();
		}
		return null;
	}

	public static Iterator findNodes(Node node, String xpathquery) {
		Iterator it = null;
		try {
			//XPath expression = DocumentHelper.createXPath(xpathquery);
			BaseXPath expression = new DOMXPath(xpathquery);
			//Node productNode = XPathAPI.selectSingleNode(doc,xPath);

			/* THIS IS A DOM3 TEST
			 XPathEvaluator xpe = (XPathEvaluator) node;
			 Document response = null;
			 xpe.evaluate(xpathquery, response, null, XPathResult.UNORDERED_NODE_ITERATOR_TYPE, null);
			 
			 NodeList nl = response.getChildNodes();
			 ArrayList al = new ArrayList(nl.getLength());
			 for(int i=0;i<nl.getLength();i++) {
			 al.add(nl.item(i));
			 }
			 return al.iterator();
			 */
			it = expression.selectNodes(node).iterator();
		} catch (Exception exe) {
		}
		if (it == null) {
			it = new Vector().iterator();
		}
		return it;
	}

	public static String getNodeValue(Node node, String path) {
		String retString = "";
		try {
			Node lNode = findNode(node, path);
			retString = lNode.getFirstChild().getNodeValue();
		} catch (Exception e) {
		}
		return retString;
	}

	public static String getNodeValue(Node nde) {
		String retString = "";
		try {
			retString = nde.getFirstChild().getNodeValue();
		} catch (Exception e) {
		}
		return retString;
	}

	public static Node renameNode(Node nde, String strName) {
		if (!strName.equals(nde.getNodeName())) {
			Document xdoc = nde.getOwnerDocument();
			Element retnode = xdoc.createElement(strName);
			NodeList nl = nde.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				retnode.appendChild(nl.item(i).cloneNode(true));
			}
			NamedNodeMap al = nde.getAttributes();
			for (int i = 0; i < al.getLength(); i++) {
				Attr attr = (Attr) al.item(i);
				retnode.setAttribute(attr.getName(), attr.getValue());
			}
			return retnode;
		}
		return nde;
	}

	/**
	 * Easily creates a new Node, containing Text and returns the new created Node.
	 * @param doc The Node to append to
	 * @param elementName The Name of the new Node
	 * @param elementText The Text to insert into the Node
	 * @return The created Node
	 */
	public static Element createTextNode(Node doc, String elementName, String elementText) {
		Element elm = doc.getOwnerDocument().createElement(elementName);
		Text elmTxt = doc.getOwnerDocument().createTextNode(elementText);
		elm.appendChild(elmTxt);
		doc.appendChild(elm);
		return elm;
	}

	public static synchronized Document file2Dom(File file) throws Exception {
		return docBuilder.parse(file);
	}

	public static synchronized Document inputstream2Dom(InputStream in) throws Exception {
		/*DOMParser parser = new DOMParser();
		parser.setFeature("http://xml.org/sax/features/validation", false);
		parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		parser.parse(new InputSource(in));
		return parser.getDocument();*/
		return docBuilder.parse(in); //this will be always validated in xerces. Why?
	}
	
	public static synchronized Document inputSource2Dom(InputSource in) throws Exception {
		return docBuilder.parse(in); 
	}

	public static synchronized Document string2Dom(String strXML) throws Exception {
		InputSource in = new InputSource(new StringReader(strXML));
		return docBuilder.parse(in);
		/*
		InputSource in = new InputSource(new java.io.StringReader(strXML));
		DOMParser parser = new DOMParser();

		parser.setFeature("http://xml.org/sax/features/validation", false);
		parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		parser.parse(in);

		return parser.getDocument();
		*/	
	}

	public static String node2string(Node nde) {
		StringBuffer sb = new StringBuffer();
		String attributes = "";
		if (nde.hasAttributes()) {
			NamedNodeMap attr = nde.getAttributes();
			for (int j = 0; j < attr.getLength(); j++) {
				attributes += " " + attr.item(j).getNodeName() + "=\"" + getHexEncoded(attr.item(j).getNodeValue())
						+ "\"";
			}
		}
		sb.append("<" + nde.getNodeName() + attributes);
		if (nde.hasChildNodes()) {
			sb.append(">" + nodeList2string(nde.getChildNodes()) + "</" + nde.getNodeName() + ">");
		} else {
			sb.append("/>");
		}
		return sb.toString();
	}

	public static String nodeList2string(NodeList nl) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < nl.getLength(); i++) {
			Node nde = nl.item(i);
			String attributes = "";
			if (nde.getNodeType() == Node.TEXT_NODE) {
				sb.append(getHexEncoded(nde.getNodeValue()));
				if (nde.hasChildNodes()) {
					sb.append(nodeList2string(nde.getChildNodes()));
				}
			} else if (nde.getNodeType() == Node.CDATA_SECTION_NODE) {
				sb.append("<![CDATA[" + nde.getNodeValue() + "]]>");
			} else if (nde.getNodeType() == Node.COMMENT_NODE) {
				sb.append("<!-- -->");
			} else {
				if (nde.hasAttributes()) {
					NamedNodeMap attr = nde.getAttributes();
					for (int j = 0; j < attr.getLength(); j++) {
						attributes += " " + attr.item(j).getNodeName() + "=\""
								+ getHexEncoded(attr.item(j).getNodeValue()) + "\"";
					}
				}
				sb.append("<" + nde.getNodeName() + attributes);
				if (nde.hasChildNodes()) {
					sb.append(">" + nodeList2string(nde.getChildNodes()) + "</" + nde.getNodeName() + ">");
				} else {
					sb.append("/>");
				}
			}
		}
		return sb.toString();
	}

	public static String getHexEncoded(String utf8String) {
		if (utf8String != null) {
			boolean changed = false;
			StringBuffer sbRet = new StringBuffer();
			for (int i = 0; i < utf8String.length(); i++) {
				int val = (int) utf8String.charAt(i);
				if (val > 128) {
					changed = true;
					sbRet.append("&#").append(val).append(";");
				} else if (val == 38) { // &
					sbRet.append("&#38;");
				} else if (val == 60) { // <
					sbRet.append("&#60;");
				} else if (val == 62) { // >
					sbRet.append("&#62;");
				} else if (val == 34) { // "
					sbRet.append("&#34;");
				} else {
					sbRet.append(utf8String.charAt(i));
				}
			}
			if (log.isDebugEnabled() && changed) {
				log.debug("changed string from: " + utf8String + " to " + sbRet.toString());
			}
			return sbRet.toString();
		}
		return null;
	}

	public static String getHexDecoded(String hexString) {
		if (hexString != null) {
			boolean changed = false;

			StringBuffer sbRet = new StringBuffer();
			for (int i = 0; i < hexString.length(); i++) {
				if (hexString.charAt(i) == '&' && hexString.charAt(i + 1) == '#') {
					// we've found one
					try {
						int endAt = hexString.indexOf((int) ';', i);
						int charCode = new Integer(hexString.substring(i + 2, endAt)).intValue();
						sbRet.append((char) charCode);
						i = endAt;
					} catch (Exception exe) {
						log.warn("uncorrectly escaped string, returning original value");
						sbRet.append(hexString.charAt(i));
					}
				} else {
					sbRet.append(hexString.charAt(i));
				}
			}
			if (log.isDebugEnabled() && changed) {
				log.debug("changed string from: " + hexString + " to " + sbRet.toString());
			}
			return sbRet.toString();
		}
		return null;
	}

	public static Node html2node(String html) {
		//Import HTML and convert it to XHTML
		Tidy myTidy = new Tidy();
		myTidy.setQuoteAmpersand(true);
		myTidy.setQuoteNbsp(true);
		//myTidy.setQuoteMarks(true);
		myTidy.setXmlOut(true);
		//myTidy.setXmlTags(true);
		myTidy.setCharEncoding(Configuration.ISO2022);
		myTidy.setShowWarnings(false);
		myTidy.setRawOut(false);
		myTidy.setQuiet(true);
		myTidy.setNumEntities(false);
		InputStream in = new ByteArrayInputStream(html.getBytes());
		OutputStream outStream = new ByteArrayOutputStream();
		myTidy.parseDOM(in, outStream);
		String strOut = outStream.toString();

		Node nde = null;
		try {
			Document htmlDoc = XercesHelper.string2Dom(strOut);
			nde = XercesHelper.findNode(htmlDoc, "//body");
		} catch (Exception exe) {
		}
		return nde;
	}
		
	public static String html2nodeUTF8(String html) throws Exception {
		if(log.isDebugEnabled()){
			log.debug("html2nodeUTF8 - with param: " + html.substring(0, (html.length()>20)?20:html.length()-1));
			log.debug("Thread " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\": ");
		}
        Tidy myTidy = new Tidy();
        myTidy.setQuoteNbsp(false);
        myTidy.setXmlOut(true);
        myTidy.setCharEncoding(Configuration.UTF8);
        myTidy.setShowWarnings(false);
        myTidy.setRawOut(true);
        myTidy.setQuoteAmpersand(false);
        myTidy.setQuiet(true);
        myTidy.setNumEntities(false);
        InputStream in = new ByteArrayInputStream(html.getBytes("UTF-8"));
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        myTidy.parseDOM(in, outStream);
        String strOut = outStream.toString("UTF-8");

		Node nde = null;
		try {
			org.w3c.dom.Document htmlDoc = XercesHelper.string2Dom(strOut);
			nde = XercesHelper.findNode(htmlDoc, "//body");
		} catch (Exception exe) {
		}
		String text = "";
		if (nde != null) {
			text = XercesHelper.node2string(nde);
			text = text.replaceAll("<body>", "");
			text = text.replaceAll("</body>", "");
		}
		return text;
    }

	
/*	public static String html2nodeUTF8(String html) {
		//Import HTML and convert it to XHTML
		Tidy myTidy = new Tidy();
		myTidy.setQuoteAmpersand(false);
		myTidy.setQuoteNbsp(true);
		//myTidy.setQuoteMarks(true);
		myTidy.setXmlOut(true);
		//myTidy.setXmlTags(true);
		
		myTidy.setCharEncoding(Configuration.UTF8);
		myTidy.setShowWarnings(false);
		myTidy.setRawOut(true);		
		myTidy.setQuiet(true);
		myTidy.setNumEntities(false);
		InputStream in = new ByteArrayInputStream(html.getBytes());
		OutputStream outStream = new ByteArrayOutputStream();
		myTidy.parseDOM(in, outStream);
		return outStream.toString();
	}*/
	
	/**
	 * 
	 * @param html
	 * @return
	 */
	public static String html2utf8string(String html) {
		Iterator it = byName.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			html = html.replaceAll("&" + key + ";", (char) ((Integer) byName.get(key)).intValue() + "");
		}
		return html;
	}
}