package org.tizzit.plugins.server.transformer;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tizzit.util.xml.SAXHelper;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import de.juwimm.cms.plugins.Constants;
import de.juwimm.cms.plugins.server.Request;
import de.juwimm.cms.plugins.server.Response;
import de.juwimm.cms.plugins.server.TizzitPlugin;

/**
 * <p>
 * <b>Namespace: <code>http://plugins.tizzit.org/TeaserTransformerPlugin</code></b>
 * </p>
 * 
 * @author <a href="mailto:rene.hertzfeldt@juwimm.com">Rene Hertzfeldt</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id: TeaserTransformerPlugin.java 759 2010-05-05 13:34:28Z rene.hertzfeldt $
 */
public class FullTextTransformerPlugin implements TizzitPlugin {
	private static final Log log = LogFactory.getLog(FullTextTransformerPlugin.class);

	public static final String PLUGIN_NAMESPACE = Constants.PLUGIN_NAMESPACE + "FullTextTransformerPlugin";
	private ContentHandler parent;
	private final boolean inContentInclude = false;
	private final boolean inSearchByUnit = false;
	private final boolean inSearchByViewComponent = false;
	private final String contentSearchBy = null;
	private final boolean iAmTheLiveserver = true;

	//private final WebServiceSpring webSpringBean = null;
	private final Integer viewComponentId = null;
	private final Integer unitId = null;

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
		parent.startElement(uri, localName, qName, attrs);

	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (log.isDebugEnabled()) log.debug("characters: " + length + " long");
		parent.characters(ch, start, length);

	}

	/*
	 * BUG 3452
	 * <contentInclude >
	 * 	<byUnit></byUnit>
	 * 	<!--
	 * 		unit kann sein: root
	 *      this
	 *      parent
	 *      entweder BYUNIT ODER BYVIEWCOMPONENT
	 *  -->
	 *  <byViewComponent>234342</byViewComponent>
	 *  <xpathElement>//</xpathElement><!-- Wenn nicht angegeben, immer der ganze Content -->
	 * </contentInclude>
	 */
	private void charactersFillContentInclude(char[] ch, int start, int length) throws Exception {
		log.debug("charactersFillContentInclude: " + length + " long");
		String value = new String(ch, start, length).trim();
		if (log.isDebugEnabled() && value != null) log.debug("value is : " + value);
		String xPathQuery = null;
		try {
			log.debug("viewComponentId is : " + viewComponentId);
			//String result = webSpringBean.getIncludeContent(viewComponentId, contentSearchBy.contains("unit"), value, iAmTheLiveserver, xPathQuery);
			String result = "<tvViewComponent><showyType>123</showyType><viewType>321</viewType><visible>true</visible><searchIndexed>true</searchIndexed><statusInfo>Standard</statusInfo><linkName>test und test</linkName><urlLinkName>test-und-test-1</urlLinkName><viewLevel>3</viewLevel><viewIndex>3</viewIndex><displaySettings>0</displaySettings><viewDocumentId>8</viewDocumentId><viewDocumentViewType>browser</viewDocumentViewType><language>de</language><userModifiedDate>1272987064423</userModifiedDate><url>test-und-test-1</url><template>standard</template></tvViewComponent>";
			SAXHelper.string2sax(result, this.parent);
		} catch (Exception e) {
			log.warn("Error getting includeContent: " + e.getMessage(), e);
		}
	}

	/**
	 * new for database fulltext searching in DCF file, defines something like : <fulltextsearch nodename="joboffer"
	 * searchOnlyInThisUnit="false"/>
	 * <ul>
	 * <li>String[x][0] contains the content.</li>
	 * <li>String[x][1] contains the infoText</li>
	 * <li>String[x][2] contains the text</li>
	 * <li>String[x][3] contains the unitId</li>
	 * </ul>
	 */
	private void fillFulltext(Document doc) throws Exception {
		//		boolean ifOnlyUnit = true;
		//		Integer myUnitId = null;
		//		Iterator itFulltext = XercesHelper.findNodes(doc, "//fulltextsearch");
		//		while (itFulltext.hasNext()) {
		//			Element fulltextsearch = (Element) itFulltext.next();
		//			ifOnlyUnit = Boolean.valueOf(fulltextsearch.getAttribute("searchOnlyInThisUnit")).booleanValue();
		//			if (ifOnlyUnit && myUnitId == null) {
		//				myUnitId = this.webSpringBean.getUnit4ViewComponent(viewComponentValue.getViewComponentId()).getUnitId();
		//			}
		//			String xpath = "//" + fulltextsearch.getAttribute("nodename").trim();
		//			if (log.isDebugEnabled()) log.debug("STARTING FULLTEXT with XPATH: " + xpath);
		//
		//			XmlSearchValue[] foundArr = searchengineService.searchXML(siteValue.getSiteId(), xpath);
		//
		//			if (foundArr != null) {
		//				if (log.isDebugEnabled()) log.debug("GOT FULLTEXT RETURN WITH " + foundArr.length + " ITEMS");
		//				for (int i = 0; i < foundArr.length; i++) {
		//					Integer foundUnitId = Integer.valueOf(0);
		//					try {
		//						foundUnitId = foundArr[i].getUnitId();
		//					} catch (Exception exe) {
		//						log.debug("Cannot catch unitId: " + foundArr[i].getUnitId());
		//					}
		//					if ((ifOnlyUnit && foundUnitId.equals(myUnitId)) || !ifOnlyUnit) {
		//						String foundContent = foundArr[i].getContent();
		//						//String foundInfoText = foundArr[i][1];
		//						//String foundText = foundArr[i][2];
		//
		//						if (foundContent != null && !foundContent.equalsIgnoreCase("")) {
		//							Document docContent = XercesHelper.string2Dom(foundContent);
		//							Node newNode = doc.importNode(docContent.getFirstChild(), true);
		//							fulltextsearch.appendChild(newNode);
		//
		//							Integer foundVcId = null;
		//							try {
		//								foundVcId = foundArr[i].getViewComponentId();
		//							} catch (Exception exe) {
		//								log.warn("fillFulltext: Could not find vcId: " + foundArr[i].getViewComponentId());
		//							}
		//							if (foundVcId != null) {
		//								ViewComponentValue foundVc = null;
		//								try {
		//									foundVc = webSpringBean.getViewComponent4Id(foundVcId);
		//								} catch (Exception e) {
		//									if (log.isDebugEnabled()) log.debug("Can't find viewComponentId " + foundVcId + "!\n" + e.getMessage());
		//								}
		//								if (foundVc != null) this.fillUnitInformation(newNode, foundVc);
		//							}
		//						}
		//					}
		//				}
		//			}
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
