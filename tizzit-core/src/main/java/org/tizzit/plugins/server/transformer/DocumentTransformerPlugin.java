package org.tizzit.plugins.server.transformer;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tizzit.util.xml.SAXHelper;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import de.juwimm.cms.beans.WebServiceSpring;
import de.juwimm.cms.plugins.server.Request;
import de.juwimm.cms.plugins.server.Response;
import de.juwimm.cms.vo.ViewComponentValue;

/**
 * <p>
 * <b>Namespace: <code>http://plugins.tizzit.org/DocumentTransformerPlugin</code></b>
 * </p>
 *
 * @author <a href="mailto:rene.hertzfeldt@juwimm.com">Rene Hertzfeldt</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id: DocumentTransformerPlugin.java 759 2010-05-05 13:34:28Z rene.hertzfeldt $
 */
public class DocumentTransformerPlugin implements ManagedTizzitPlugin {
	private static final Log log = LogFactory.getLog(DocumentTransformerPlugin.class);

	private ContentHandler parent;
	private final String IMAGE = "image";
	private final String DOCUMENT = "document";
	private final String MIMETYPE = "mimeType";
	private boolean iAmTheLiveserver = true;

	private WebServiceSpring webSpringBean = null;
	private ViewComponentValue viewComponentValue = null;
	private Integer viewComponentId = null;

	private ContentHandler manager;
	private String nameSpace;

	public void setup(ContentHandler pluginManager, String nameSpace, WebServiceSpring wss, ViewComponentValue viewComponent, boolean liveServer) {
		this.manager = pluginManager;
		this.nameSpace = nameSpace;
		this.webSpringBean = wss;
		this.viewComponentValue = viewComponent;
		this.iAmTheLiveserver = liveServer;
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.plugins.server.ConquestPlugin#configurePlugin(de.juwimm.cms.plugins.server.Request, de.juwimm.cms.plugins.server.Response, org.xml.sax.ContentHandler, java.lang.Integer)
	 */
	public void configurePlugin(Request req, Response resp, ContentHandler ch, Integer uniquePageId) {
		if (log.isDebugEnabled()) log.debug("configurePlugin() -> begin");
		this.parent = ch;
		this.viewComponentId = uniquePageId;
		if (log.isDebugEnabled()) log.debug("configurePlugin() -> end");
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.plugins.server.ConquestPlugin#getLastModifiedDate()
	 */
	public Date getLastModifiedDate() {
		return new Date();
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.plugins.server.ConquestPlugin#isCacheable()
	 */
	public boolean isCacheable() {
		return false;
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.plugins.server.ConquestPlugin#processContent()
	 */
	public void processContent() {
		if (log.isDebugEnabled()) log.debug("processContent() -> begin");

		if (log.isDebugEnabled()) log.debug("processContent() -> end");
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		if (log.isDebugEnabled()) log.debug("startElement: " + localName + " in nameSpace: " + uri + " found " + attrs.getLength() + " attributes");

		if (localName.equalsIgnoreCase(IMAGE)) {
			String mime = attrs.getValue(MIMETYPE);
			AttributesImpl newAttrs = new AttributesImpl(attrs);
			if (mime == null || mime.isEmpty()) {
				try {
					Integer picId = Integer.decode(attrs.getValue("src"));
					mime = webSpringBean.getPictureMimeType(picId);
					SAXHelper.setSAXAttr(newAttrs, MIMETYPE, mime);
				} catch (NumberFormatException e) {
					log.warn("Cound not get int value from: " + attrs.getValue("src"), e);
				} catch (Exception e) {
					log.warn("Error while getting mime type for picture: " + attrs.getValue("src"), e);
				}
			}
			parent.startElement(null, localName, qName, newAttrs);
		} else if (localName.equalsIgnoreCase(DOCUMENT)) {
			String mime = attrs.getValue(MIMETYPE);
			AttributesImpl newAttrs = new AttributesImpl(attrs);
			if (mime == null || mime.isEmpty()) {
				try {
					Integer docId = Integer.decode(attrs.getValue("src"));
					mime = webSpringBean.getDocumentMimeType(docId);
					SAXHelper.setSAXAttr(newAttrs, MIMETYPE, mime);
				} catch (NumberFormatException e) {
					log.warn("Cound not get int value from: " + attrs.getValue("src"), e);
				} catch (Exception e) {
					log.warn("Error while getting mime type for document: " + attrs.getValue("src"), e);
				}
			}
			parent.startElement(uri, localName, qName, newAttrs);
		} else {
			parent.startElement(uri, localName, qName, attrs);
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (log.isDebugEnabled()) log.debug("characters: " + length + " long");
		parent.characters(ch, start, length);

	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (log.isDebugEnabled()) log.debug("endElement: " + localName + " in nameSpace: " + uri);
		parent.endElement(uri, localName, qName);
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	public void endDocument() throws SAXException {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
	 */
	public void endPrefixMapping(String prefix) throws SAXException {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
	 */
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
	 */
	public void processingInstruction(String target, String data) throws SAXException {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	public void setDocumentLocator(Locator locator) {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
	 */
	public void skippedEntity(String name) throws SAXException {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	public void startDocument() throws SAXException {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
	 */
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		// do nothing
	}
}
