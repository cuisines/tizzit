package org.tizzit.plugins.server.transformer;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tizzit.util.xml.SAXHelper;
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
 * <b>Namespace: <code>http://plugins.tizzit.org/MemeberListTransformerPlugin</code></b>
 * </p>
 *
 * @author <a href="mailto:rene.hertzfeldt@juwimm.com">Rene Hertzfeldt</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id: MemeberListTransformerPlugin.java 759 2010-05-05 13:34:28Z rene.hertzfeldt $
 */
public class MemberListTransformerPlugin implements ManagedTizzitPlugin {
	private static final Log log = LogFactory.getLog(MemberListTransformerPlugin.class);

	private final String MEMBERLIST = "memberList";
	private final String UNITMEMBERLIST = "unitMemberList";
	private ContentHandler parent;
	private Integer viewComponentId = null;

	private WebServiceSpring webSpringBean = null;

	private ContentHandler manager;
	private String nameSpace;

	public void setup(ContentHandler pluginManager, String nameSpace, WebServiceSpring wss, ViewComponentValue viewComponent, boolean liveServer) {
		this.manager = pluginManager;
		this.nameSpace = nameSpace;
		this.webSpringBean = wss;
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.plugins.server.ConquestPlugin#configurePlugin(de.juwimm.cms.plugins.server.Request, de.juwimm.cms.plugins.server.Response, org.xml.sax.ContentHandler, java.lang.Integer)
	 */
	public void configurePlugin(Request req, Response resp, ContentHandler ch, Integer uniquePageId) {
		if (log.isDebugEnabled()) log.debug("configurePlugin() -> begin");
		this.parent = ch;
		this.viewComponentId = uniquePageId;
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
		if (localName.compareTo(MEMBERLIST) == 0 || localName.compareTo(UNITMEMBERLIST) == 0) {
			Integer unitId = null;
			String unitAttr = attrs.getValue("unitId");
			if (unitAttr == null || unitAttr.trim().isEmpty()) {
				//FIXME: muss wieder rein
				//unitId = unitValue.getUnitId();
			} else if (unitAttr.equalsIgnoreCase("all")) {
				unitId = null;
			} else {
				unitId = new Integer(unitAttr);
			}
			if (Integer.valueOf(0).equals(unitId)) {
				return;
			}

			String firstname = attrs.getValue("firstname");
			if (firstname == null || firstname.trim().isEmpty()) {
				firstname = "*";
			}
			firstname = firstname.replaceAll("[*]", "%");

			String lastname = attrs.getValue("lastname");
			if (lastname == null || lastname.trim().isEmpty()) {
				lastname = "*";
			}
			lastname = lastname.replaceAll("[*]", "%");
			//FIXME: string just for testing
			//String ml = webSpringBean.getMembersList(siteId, unitId, firstname, lastname);
			String ml = "<memberList>viele viele Members</memberList>";
			SAXHelper.string2sax(ml, parent);
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
		if (localName.compareTo(MEMBERLIST) != 0 && localName.compareTo(UNITMEMBERLIST) != 0) {
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
