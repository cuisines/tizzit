package org.tizzit.cocoon.transformation;

import java.util.Date;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tizzit.util.XercesHelper;
import org.tizzit.util.xml.SAXHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import de.juwimm.cms.plugins.Constants;
import de.juwimm.cms.plugins.server.Request;
import de.juwimm.cms.plugins.server.Response;
import de.juwimm.cms.plugins.server.TizzitPlugin;
import de.juwimm.cms.vo.UnitValue;

/**
 * <p>
 * <b>Namespace: <code>http://plugins.tizzit.org/InternalLinksTransformerPlugin</code></b>
 * </p>
 *
 * @author <a href="mailto:rene.hertzfeldt@juwimm.com">Rene Hertzfeldt</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id: InternalLinksTransformerPlugin.java 759 2010-05-05 13:34:28Z rene.hertzfeldt $
 */
public class InternalLinksTransformerPlugin implements TizzitPlugin {
	private static final Log log = LogFactory.getLog(InternalLinksTransformerPlugin.class);

	public static final String PLUGIN_NAMESPACE = Constants.PLUGIN_NAMESPACE + "InternalLinksTransformerPlugin";
	private ContentHandler parent;
	private final String INTERNALLINK = "internalLink";
	private boolean inInternalLink = false;
	private final Integer viewComponentId = null;

	//private WebServiceSpring webSpringBean = null;

	/* (non-Javadoc)
	 * @see de.juwimm.cms.plugins.server.ConquestPlugin#configurePlugin(de.juwimm.cms.plugins.server.Request, de.juwimm.cms.plugins.server.Response, org.xml.sax.ContentHandler, java.lang.Integer)
	 */
	public void configurePlugin(Request req, Response resp, ContentHandler ch, Integer uniquePageId) {
		if (log.isDebugEnabled()) log.debug("configurePlugin() -> begin");
		this.parent = ch;
		//webSpringBean = (WebServiceSpring) PluginSpringHelper.getBean(objectModel, PluginSpringHelper.WEB_SERVICE_SPRING);
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
		if (localName.compareTo(INTERNALLINK) == 0) {
			if(inInternalLink){
				startInternalLinkElement( uri,  localName,  qName,  attrs);
			}
			else{
				inInternalLink = true;
				parent.startElement(uri, localName, qName, attrs);
			}
		}
		else{
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

	private void startInternalLinkElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		if(log.isDebugEnabled())log.debug("create InternalLink");
		Integer vcid = null;
		AttributesImpl newAttrs = new AttributesImpl();
		try {
			vcid = Integer.decode(attrs.getValue("viewid"));
//			String path = webSpringBean.getPath4ViewComponent(vcid);
//			String lang = webSpringBean.getViewDocument4ViewComponentId(vcid).getLanguage();
//			Integer unitId = webSpringBean.getUnitIdForViewComponent(vcid);
			
			// FIXME: strings just for testing
			String path = "1, 2, 3, 4, 5, 6";
			String lang = "de/D";
			Integer unitId = 123;

			SAXHelper.setSAXAttr(newAttrs, "viewid", vcid.toString());
			SAXHelper.setSAXAttr(newAttrs, "url", path);
			SAXHelper.setSAXAttr(newAttrs, "language", lang);
			if (unitId != null) SAXHelper.setSAXAttr(newAttrs,"unitid", unitId.toString());
		} catch (Exception exe) {
			log.info("Could not solve internalLink with vcid " + vcid + " in content of vcid " + this.viewComponentId);
		}
		parent.startElement(uri, localName, qName, newAttrs);
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (log.isDebugEnabled()) log.debug("endElement: " + localName + " in nameSpace: " + uri);
		if (localName.compareTo(INTERNALLINK) == 0) {
			if(inInternalLink){
				inInternalLink = false;
			}
		}
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
