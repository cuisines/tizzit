package org.tizzit.cocoon.transformation;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tizzit.util.xml.SAXHelper;
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
 * <b>Namespace: <code>http://plugins.tizzit.org/ContentTransformerPlugin</code></b>
 * </p>
 * 
 * @author <a href="mailto:rene.hertzfeldt@juwimm.com">Rene Hertzfeldt</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id: ContentTransformerPlugin.java 759 2010-05-05 13:34:28Z rene.hertzfeldt $
 */
public class ContentTransformerPlugin implements TizzitPlugin {
	private static final Log log = LogFactory.getLog(ContentTransformerPlugin.class);

	public static final String PLUGIN_NAMESPACE = Constants.PLUGIN_NAMESPACE + "ContentTransformerPlugin";
	private static final String BYUNIT = "byUnit";
	private static final String CONTENTINCLUDE = "contentInclude";
	private static final String BYVIEWCOMPONENT = "byViewComponent";
	private ContentHandler parent;
	private boolean inContentInclude = false;
	private boolean inSearchByUnit = false;
	private boolean inSearchByViewComponent = false;
	private String contentSearchBy = null;
	private final boolean iAmTheLiveserver = true;

	//private final WebServiceSpring webSpringBean = null;
	private Integer viewComponentId = null;
	private Integer unitId = null;

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
		if (localName.equals("contentInclude")) {
			inContentInclude = true;
			parent.startElement(uri, localName, qName, attrs);
		} else if (localName.equals(BYVIEWCOMPONENT) && inContentInclude) {
			inSearchByViewComponent = true;
		} else if (localName.equals(BYUNIT) && inContentInclude) {
			inSearchByUnit = true;
		} else {
			parent.startElement(uri, localName, qName, attrs);
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (log.isDebugEnabled()) log.debug("characters: " + length + " long");
		if (inContentInclude && contentSearchBy != null && !(inSearchByUnit || inSearchByViewComponent)) {
			try {
				charactersFillContentInclude(ch, start, length);
			} catch (Exception e) {
				if (log.isDebugEnabled()) log.debug("error in charactersFillContentInclude ", e);
			}
		} else if (inContentInclude && (inSearchByViewComponent || inSearchByUnit)) {
			contentSearchBy = (inSearchByViewComponent) ? BYVIEWCOMPONENT : BYUNIT;
			if (ch.length != 0 && !(new String(ch, start, length).trim().isEmpty())) {
				try {
					if (inSearchByViewComponent) viewComponentId = Integer.parseInt(new String(ch, start, length).trim());
					if (inSearchByUnit) unitId = Integer.parseInt((new String(ch, start, length).trim()));
				} catch (NumberFormatException nfe) {
					if (log.isDebugEnabled()) log.debug(new String(ch, start, length) + " could not be formed to an Integer ", nfe);
				}
			}
		} else {
			parent.characters(ch, start, length);
		}
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
			//FIXME: string just for testing
			//String result = webSpringBean.getIncludeContent(viewComponentId, contentSearchBy.contains("unit"), value, iAmTheLiveserver, xPathQuery);
			String result = "<tvViewComponent><showyType>123</showyType><viewType>321</viewType><visible>true</visible><searchIndexed>true</searchIndexed><statusInfo>Standard</statusInfo><linkName>test und test</linkName><urlLinkName>test-und-test-1</urlLinkName><viewLevel>3</viewLevel><viewIndex>3</viewIndex><displaySettings>0</displaySettings><viewDocumentId>8</viewDocumentId><viewDocumentViewType>browser</viewDocumentViewType><language>de</language><userModifiedDate>1272987064423</userModifiedDate><url>test-und-test-1</url><template>standard</template></tvViewComponent>";
			SAXHelper.string2sax(result, this.parent);
		} catch (Exception e) {
			log.warn("Error getting includeContent: " + e.getMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (log.isDebugEnabled()) log.debug("endElement: " + localName + " in nameSpace: " + uri);
		if (localName.equals("contentInclude")) {
			inContentInclude = false;
			parent.endElement(uri, localName, qName);
		} else if (localName.compareToIgnoreCase(BYUNIT) == 0 && inContentInclude) {
			inSearchByUnit = false;
		} else if (localName.compareToIgnoreCase(BYVIEWCOMPONENT) == 0 && inContentInclude) {
			inSearchByViewComponent = false;
		} else {
			parent.endElement(uri, localName, qName);
		}
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
