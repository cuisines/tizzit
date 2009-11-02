package org.tizzit.util.xml;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.jaxen.BaseXPath;
import org.jaxen.JaxenException;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Helperclass for XML/XPath Functionality.
 * <p>Copyright: Copyright JuwiMacMillan Group GmbH (c) 2008</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since tizzit-common 02.11.2009
 */
public final class DOMHelper {
	private static Logger log = Logger.getLogger(DOMHelper.class);
	private static DocumentBuilder docBuilder = null;
	private static DocumentBuilderFactory dbf = null;
	private static HashMap<String, BaseXPath> xpathCache = new HashMap<String, BaseXPath>();

	/**
	 * Static saving of xpath queries reduces the compile time of the query up to 90%.
	 * A complete XPath Query on a more then 100 line XML document will be up to 10 times faster, 0.1 ms instead
	 * of 1 ms (core2duo)
	 * @param xpath
	 * @param namespaces
	 * @return
	 * @throws JaxenException
	 */
	public static BaseXPath getXpath(String xpath, Map<String, String> namespaces) throws JaxenException {
		if (xpathCache.containsKey(xpath + namespaces.hashCode())) {
			return xpathCache.get(xpath + namespaces.hashCode());
		} else {
			final BaseXPath expression = new DOMXPath(xpath);
			Set<Entry<String, String>> nsKeys = namespaces.entrySet();
			for (Entry<String, String> entry : nsKeys) {
				expression.addNamespace(entry.getKey(), entry.getValue());
			}
			if (xpathCache.size() > 20000) {
				log.info("Size of XPath Cache exceeded size of 20000, starting with new cache.");
				xpathCache = new HashMap<String, BaseXPath>();
			}
			xpathCache.put(xpath + namespaces.hashCode(), expression);
			return expression;
		}
	}

	static {
		try {
			dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(false);
			dbf.setNamespaceAware(true);
			dbf.setFeature("http://xml.org/sax/features/namespaces", true);
			docBuilder = dbf.newDocumentBuilder();
		} catch (Exception exe) {
			log.error("Fatal Error occured while getting the DocumentBuilderFactory", exe);
		}
	}

	private DOMHelper() {
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
			t.setOutputProperty(OutputKeys.STANDALONE, "no");
			t.setOutputProperty(OutputKeys.METHOD, "xml");
			t.transform(new DOMSource(doc), new StreamResult(stringOut));
		} catch (Exception exe) {
			log.error("unknown error occured", exe);
		}
		return stringOut.toString();
	}

	public static String node2String(Node node) {
		StringWriter stringOut = new StringWriter();
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.setOutputProperty(OutputKeys.STANDALONE, "no");
			t.setOutputProperty(OutputKeys.METHOD, "xml");
			t.transform(new DOMSource(node), new StreamResult(stringOut));
		} catch (Exception exe) {
			log.error("unknown error occured", exe);
		}
		return stringOut.toString();
	}

	public static void node2sax(Node node, ContentHandler handler) {
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.setOutputProperty(OutputKeys.STANDALONE, "no");
			t.setOutputProperty(OutputKeys.METHOD, "xml");
			DOMSource source = new DOMSource(node);
			SAXResult result = new SAXResult(handler);
			t.transform(source, result);
		} catch (Exception exe) {
			log.error("unknown error occured", exe);
		}
	}

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

	public static Node findNode(Node node, String xpathquery, HashMap<String, String> namespaces) throws Exception {
		Iterator<Node> it = findNodes(node, xpathquery, namespaces);
		if (it.hasNext()) { return it.next(); }
		return null;
	}

	@SuppressWarnings("unchecked")
	public static Iterator<Node> findNodes(Node node, String xpathquery, Map<String, String> namespaces) {
		Iterator<Node> it = null;
		try {
			BaseXPath expression = getXpath(xpathquery, namespaces);
			it = expression.selectNodes(node).iterator();
		} catch (Exception exe) {
		}
		if (it == null) {
			it = new Vector().iterator();
		}
		return it;
	}

	public static String getNodeValue(Node node, String xpath, HashMap<String, String> namespaces) {
		String retString = "";
		try {
			Node lNode = findNode(node, xpath, namespaces);
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
		return docBuilder.parse(in); //this will be always validated in xerces. Why?
	}

	public static synchronized Document inputSource2Dom(InputSource in) throws Exception {
		return docBuilder.parse(in);
	}

	public static synchronized Document string2Dom(String strXML) throws Exception {
		InputSource in = new InputSource(new StringReader(strXML));
		return docBuilder.parse(in);
	}

	/**
	 * This ContentHandler is to remove the xml Header. The OMIT_XML_DECLARATION does not work actually.
	 * @author <a href="mailto:j2ee@juwimm.com">Sascha-Matthias Kulawik</a>
	 * company Juwi MacMillan Group GmbH, Walsrode, Germany
	 * @version $Id$
	 * @since tizzit-common 02.11.2009
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