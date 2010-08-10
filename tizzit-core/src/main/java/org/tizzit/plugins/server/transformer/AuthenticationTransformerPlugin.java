package org.tizzit.plugins.server.transformer;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import de.juwimm.cms.beans.WebServiceSpring;
import de.juwimm.cms.plugins.server.Request;
import de.juwimm.cms.plugins.server.Response;
import de.juwimm.cms.vo.ViewComponentValue;

/**
 * <p>
 * <b>Namespace: <code>http://plugins.tizzit.org/AuthenticationTransformerPlugin</code></b>
 * </p>
 * 
 * @author <a href="mailto:rene.hertzfeldt@juwimm.com">Rene Hertzfeldt</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id: AuthenticationTransformerPlugin.java 759 2010-05-05 13:34:28Z rene.hertzfeldt $
 */
public class AuthenticationTransformerPlugin implements ManagedTizzitPlugin {
	private static final Log log = LogFactory.getLog(AuthenticationTransformerPlugin.class);

	private ContentHandler parent;
	private boolean iAmTheLiveserver = true;

	private WebServiceSpring webSpringBean = null;
	private final Integer viewComponentId = null;
	private final Integer unitId = null;

	private ContentHandler manager;
	private String nameSpace;

	private ViewComponentValue viewComponentValue;

	//	private SessionContext sessContext = null;

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

		//		XercesHelper.findNode(doc, "//source/head");
		//		if (head != null) {
		//			if (sessContext == null) {
		//				if (log.isDebugEnabled()) log.debug("No Logged-in User");
		//			} 
		//		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (log.isDebugEnabled()) log.debug("characters: " + length + " long");

		parent.characters(ch, start, length);

	}

	private void fillAuthentication() throws Exception {
		//		StringBuffer auth = new StringBuffer("<authentication>");
		//		try {
		//			Node idN = sessContext.getXML("/authentication/ID");
		//			Node id = doc.createElement("id");
		//			id.appendChild(doc.importNode(idN, true));
		//			authentication.appendChild(id);
		//		} catch (Exception exe) {
		//			log.error("An error occured", exe);
		//		}
		//		
		//		
		//		DocumentFragment df = sessContext.getXML("/authentication/roles");
		//		if (df != null) {
		//			NodeList nl = df.getChildNodes();
		//			for (int i = 0; i < nl.getLength(); i++) {
		//				Node role = nl.item(i);
		//				if (XercesHelper.getNodeValue(role).startsWith("unit_")) {
		//					Element unitInformation = doc.createElement("unitInformation");
		//					StringTokenizer st = new StringTokenizer(XercesHelper.getNodeValue(role), "_");
		//					if (st.hasMoreTokens()) {
		//						// skip "unit"
		//						st.nextToken();
		//						if (st.hasMoreTokens()) {
		//							String unitId = st.nextToken();
		//							unitInformation.setAttribute("id", unitId);
		//						}
		//						if (st.hasMoreTokens()) {
		//							String unitName = st.nextToken();
		//							Element unitNameElem = doc.createElement("unitname");
		//							CDATASection cData = doc.createCDATASection(Base64.decodeToString(unitName));
		//							unitNameElem.appendChild(cData);
		//							unitInformation.appendChild(unitNameElem);
		//						}
		//						if (st.hasMoreTokens()) {
		//							// skip "site"
		//							st.nextToken();
		//							if (st.hasMoreTokens()) {
		//								String siteId = st.nextToken();
		//								unitInformation.setAttribute("siteId", siteId);
		//							}
		//						}
		//					}
		//					authentication.appendChild(unitInformation);
		//				} else if (XercesHelper.getNodeValue(role).startsWith("group_")) {
		//					Element groupInformation = doc.createElement("groupInformation");
		//					StringTokenizer st = new StringTokenizer(XercesHelper.getNodeValue(role), "_");
		//					if (st.hasMoreTokens()) {
		//						// skip "group"
		//						st.nextToken();
		//						if (st.hasMoreTokens()) {
		//							String groupId = st.nextToken();
		//							groupInformation.setAttribute("id", groupId);
		//						}
		//						if (st.hasMoreTokens()) {
		//							String groupName = st.nextToken();
		//							Element groupNameElem = doc.createElement("groupname");
		//							CDATASection cData = doc.createCDATASection(Base64.decodeToString(groupName));
		//							groupNameElem.appendChild(cData);
		//							groupInformation.appendChild(groupNameElem);
		//						}
		//						if (st.hasMoreTokens()) {
		//							// skip "site"
		//							st.nextToken();
		//							if (st.hasMoreTokens()) {
		//								String siteId = st.nextToken();
		//								groupInformation.setAttribute("siteId", siteId);
		//							}
		//						}
		//					}
		//					authentication.appendChild(groupInformation);
		//				} else if (XercesHelper.getNodeValue(role).startsWith("site_")) {
		//					Element siteInformation = doc.createElement("siteInformation");
		//					StringTokenizer st = new StringTokenizer(XercesHelper.getNodeValue(role), "_");
		//					if (st.hasMoreTokens()) {
		//						// skip "site"
		//						st.nextToken();
		//						if (st.hasMoreTokens()) {
		//							String siteId = st.nextToken();
		//							siteInformation.setAttribute("id", siteId);
		//						}
		//						if (st.hasMoreTokens()) {
		//							String siteName = st.nextToken();
		//							Element siteNameElem = doc.createElement("sitename");
		//							CDATASection cData = doc.createCDATASection(Base64.decodeToString(siteName));
		//							siteNameElem.appendChild(cData);
		//							siteInformation.appendChild(siteNameElem);
		//						}
		//						if (st.hasMoreTokens()) {
		//							String siteShort = st.nextToken();
		//							Element siteShortElem = doc.createElement("siteshort");
		//							CDATASection cData = doc.createCDATASection(Base64.decodeToString(siteShort));
		//							siteShortElem.appendChild(cData);
		//							siteInformation.appendChild(siteShortElem);
		//						}
		//					}
		//					authentication.appendChild(siteInformation);
		//				} else if (XercesHelper.getNodeValue(role).startsWith("role_")) {
		//					Element roleInformation = doc.createElement("roleInformation");
		//					StringTokenizer st = new StringTokenizer(XercesHelper.getNodeValue(role), "_");
		//					if (st.hasMoreTokens()) {
		//						// skip "role"
		//						st.nextToken();
		//						if (st.hasMoreTokens()) {
		//							String roleId = st.nextToken();
		//							roleInformation.setAttribute("id", roleId);
		//						}
		//						if (st.hasMoreTokens()) {
		//							// skip "site"
		//							// trash = st.nextToken();
		//							if (st.hasMoreTokens()) {
		//								String siteId = st.nextToken();
		//								roleInformation.setAttribute("siteId", siteId);
		//							}
		//						}
		//					}
		//					authentication.appendChild(roleInformation);
		//				} else {
		//					role = doc.importNode(role, true);
		//					authentication.appendChild(role);
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
