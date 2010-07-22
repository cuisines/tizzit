package org.tizzit.plugins.server.transformer;

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

import de.juwimm.cms.beans.WebServiceSpring;
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
public class LanguageVersionTransformerPlugin implements ManagedTizzitPlugin {
	private static final Log log = LogFactory.getLog(LanguageVersionTransformerPlugin.class);

	private ContentHandler parent;
	private final Integer viewComponentId = null;
	private boolean hasData = false;
	private boolean iAmTheLiveserver = true;
	private Integer siteId = null;
	private String language = null;

	private WebServiceSpring webSpringBean = null;

	private ContentHandler manager;
	private String nameSpace;
	
	
	public void setup(ContentHandler pluginManager, String nameSpace) {
		this.manager = pluginManager;
		this.nameSpace = nameSpace;
	}
	
	/* (non-Javadoc)
	 * @see de.juwimm.cms.plugins.server.ConquestPlugin#configurePlugin(de.juwimm.cms.plugins.server.Request, de.juwimm.cms.plugins.server.Response, org.xml.sax.ContentHandler, java.lang.Integer)
	 */
	public void configurePlugin(Request req, Response resp, ContentHandler ch, Integer uniquePageId) {
		if (log.isDebugEnabled()) log.debug("configurePlugin() -> begin");
		this.parent = ch;
		this.siteId = uniquePageId;
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
		if(!uri.equalsIgnoreCase(nameSpace)){
			manager.startElement(uri, localName, qName, attrs);
		}
		if(qName.equalsIgnoreCase("language")){
			hasData = true;
		}
		parent.startElement(uri, localName, qName, attrs);


	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (log.isDebugEnabled()) log.debug("characters: " + length + " long");
		parent.characters(ch, start, length);

	}

	/**
	 * creates langName and langUrl Elements under languageVersions Element
	 */
	private void fillLanguageVersions() {
		try {
			ViewDocumentValue[] vdd = webSpringBean.getViewDocuments4Site(this.siteId);
			for (int i = 0; i < vdd.length; i++) {
				String lang = vdd[i].getLanguage();
				if (!lang.equals(language)) { // give me only those vc's from other languages
					try {
						Integer unitId = webSpringBean.getUnit4ViewComponent(viewComponentId).getUnitId();
						Integer viewDocumentId = vdd[i].getViewDocumentId();
						ViewComponentValue vcl = webSpringBean.getViewComponent4Unit(unitId, viewDocumentId);
						if (vcl != null) {
							// Maybe for this language there is no page for this unit
							if (webSpringBean.isVisibleForLanguageVersion(vcl, iAmTheLiveserver)) {
								String langpath = webSpringBean.getPath4ViewComponent(vcl.getViewComponentId());
								StringBuffer langua = new StringBuffer("<language>");
								langua.append("<langName>").append(lang).append("</langName>");
								langua.append("<langUrl>").append(langpath).append("</langUrl>");
								langua.append("/language");
								SAXHelper.string2sax(langua.toString(), parent);
							}
						}
					} catch (Exception exe) { //if the vcl wont be found. thats ok :)
						if (log.isDebugEnabled()) {
							log.warn("An error occured", exe);
						}
					}
				}
			}
		} catch (Exception exe) {
			log.warn("An error occured", exe);
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (log.isDebugEnabled()) log.debug("endElement: " + localName + " in nameSpace: " + uri);
		if(localName.equalsIgnoreCase("languageVersions") && !hasData){
			fillLanguageVersions();
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
