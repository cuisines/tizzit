package org.tizzit.cocoon.transformation;

import java.util.Date;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tizzit.util.XercesHelper;
import org.tizzit.util.xml.SAXHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
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
import de.juwimm.cms.vo.ViewComponentValue;
import de.juwimm.cms.vo.ViewDocumentValue;

/**
 * <p>
 * <b>Namespace: <code>http://plugins.tizzit.org/LanguageVersionTransformerPlugin</code></b>
 * </p>
 *
 * @author <a href="mailto:rene.hertzfeldt@juwimm.com">Rene Hertzfeldt</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id: LanguageVersionTransformerPlugin.java 759 2010-05-05 13:34:28Z rene.hertzfeldt $
 */
public class LanguageVersionTransformerPlugin implements TizzitPlugin {
	private static final Log log = LogFactory.getLog(LanguageVersionTransformerPlugin.class);

	public static final String PLUGIN_NAMESPACE = Constants.PLUGIN_NAMESPACE + "LanguageVersionTransformerPlugin";
	private ContentHandler parent;
	private final String UNITINFORMATION = "unitInformation";
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
		if (localName.compareTo(UNITINFORMATION) == 0) {
			
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

	// FIXME: debug strings only
	// needs to be called for start element
	private void fillLanguageVersions(Document doc) {
//		try {
//			String language = par.getParameter("language");
//			ViewDocumentValue[] vdd = webSpringBean.getViewDocuments4Site(siteValue.getSiteId());
//			for (int i = 0; i < vdd.length; i++) {
//				String lang = vdd[i].getLanguage();
//				if (!lang.equals(language)) { // give me only those vc's from other languages
//					try {
//						Integer unitId = webSpringBean.getUnit4ViewComponent(viewComponentValue.getViewComponentId()).getUnitId();
//						Integer viewDocumentId = vdd[i].getViewDocumentId();
//						ViewComponentValue vcl = webSpringBean.getViewComponent4Unit(unitId, viewDocumentId);
//						if (vcl != null) {
//							// Maybe for this language there is no page for this unit
//							if (webSpringBean.isVisibleForLanguageVersion(vcl, iAmTheLiveserver)) {
//								String langpath = webSpringBean.getPath4ViewComponent(vcl.getViewComponentId());
//								Iterator it = XercesHelper.findNodes(doc, "//languageVersions");
//								while (it.hasNext()) {
//									Node nde = (Node) it.next();
//									Node ndeLanguage = doc.createElement("language");
//									nde.appendChild(ndeLanguage);
//									Node langName = doc.createElement("langName");
//									Node langNameText = doc.createTextNode(lang);
//									langName.appendChild(langNameText);
//									ndeLanguage.appendChild(langName);
//									Node langUrl = doc.createElement("langUrl");
//									Node langUrlText = doc.createTextNode(langpath);
//									langUrl.appendChild(langUrlText);
//									ndeLanguage.appendChild(langUrl);
//								}
//							}
//						}
//					} catch (Exception exe) { //if the vcl wont be found. thats ok :)
//						if (log.isDebugEnabled()) {
//							log.debug("An error occured", exe);
//						}
//					}
//				}
//			}
//		} catch (Exception exe) {
//			log.error("An error occured", exe);
//		}
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
