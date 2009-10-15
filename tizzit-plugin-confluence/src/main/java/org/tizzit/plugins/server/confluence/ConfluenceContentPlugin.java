package org.tizzit.plugins.server.confluence;

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlrpc.XmlRpcClient;
import org.tizzit.plugins.server.confluence.data.ConfluencePage;
import org.tizzit.util.xml.SAXHelper;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import de.juwimm.cms.plugins.Constants;
import de.juwimm.cms.plugins.server.Request;
import de.juwimm.cms.plugins.server.Response;
import de.juwimm.cms.plugins.server.TizzitPlugin;

/**
 * <h5>Usage / Configuration:</h5>
 * <p>
 * <b>tizzit-plugins.conf:</b><br/>
 * <code>http://plugins.tizzit.org/ConfluenceContentPlugin=org.tizzit.plugins.server.confluence.ConfluenceContentPlugin</code>
 * </p>
 * <b>XML:</b>
 * <pre> &lt;getPage xmlns="http://plugins.tizzit.org/ConfluenceContentPlugin"&gt;
 *   &lt;pageId dcfname="pageId" label="Page ID"&gt;557129&lt;/pageId&gt;
 *   &lt;confluenceURLs dcfname="confluenceURL" label="Confluence URLs"&gt;
 *     &lt;value&gt;http://wiki.tizzit.org&lt;/value&gt;
 *   &lt;/confluenceURLs&gt;
 *   &lt;xmlRpcMethodsPrefix dcfname="xmlRpcMethodsPrefix" label="XML-RPC methods prefix"&gt;confluence1&lt;/xmlRpcMethodsPrefix&gt;
 *   &lt;useStyles dcfname="useStyles" label="Render content with styles?"&gt;
 *     &lt;value&gt;true&lt;/value&gt;
 *   &lt;/useStyles&gt;
 * &lt;/getPage&gt;</pre>
 *
 * <h5>Element description:</h5>
 * <b>mandatory:</b>
 * <ul>
 * 	<li><b>pageId</b> - The id of the Confluence page.</li>
 * 	<li><b>confluenceURLs/value</b> - The base URL to Confluence (<code>http://&lt;&lt;confluence-install&gt;&gt;</code>). This URL will be appended with {@link org.tizzit.plugins.server.confluence.DEFAULT_RPC_PATH}</li>
 * </ul>
 * <b>optional:</b>
 * <ul>
 * 	<li><b>xmlRpcMethodsPrefix</b> - default: <code>confluence1</code> - XML-RPC methods prefix.</li>
 * 	<li><b>useStyles/value</b> - default: <code>false</code> - Renders the Confluence content with styles if <code>true</code>.</li>
 * </ul>
 *
 * <h5>Output:</h5>
 * <pre> &lt;confluencePage contentStatus="current" created="1246447786000" creator="ckulawik"
 *                 current="true" homePage="false" id="557129" modified="1255086937000"
 *                 modifier="msimon" parentId="557083" permissions="0" space="userdoc" title="Menu bar"
 *                 url="http://wiki.tizzit.org/display/userdoc/Menu+bar" version="12"&gt;
 *   &lt;renderedContent&gt;
 *     &lt;div id="ConfluenceContent"&gt;
 *       &lt;h1&gt;&lt;a name="Menubar-MenuBar" /&gt;Menu Bar&lt;/h1&gt;
 *
 *       &lt;p&gt;The menu bar is one of the tree main functional areas in the tizzit &lt;a href="/display/userdoc/User+Interface" title="User Interface"&gt;user interface&lt;/a&gt;.&lt;br /&gt; It provides buttons/icons and drop down menus to manage your tizzit website.&lt;br /&gt; The buttons are grouped by their functions:&lt;/p&gt;
 *       &lt;ul&gt;
 *         &lt;li&gt;
 *           &lt;a href="#Menubar-Edit"&gt;Edit&lt;/a&gt;
 *         &lt;/li&gt;
 *       &lt;/ul&gt;
 *       ...
 *     &lt;/div&gt;
 *   &lt;/renderedContent&gt;
 * &lt;/confluencePage&gt;</pre>
 *
 * <p>
 * <b>Namespace: <code>http://plugins.tizzit.org/ConfluenceContentPlugin</code></b>
 * </p>
 *
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since tizzit-plugin-sample 15.10.2009
 */
public class ConfluenceContentPlugin implements TizzitPlugin {
	private static final Log log = LogFactory.getLog(ConfluenceContentPlugin.class);

	public static final String PLUGIN_NAMESPACE = Constants.PLUGIN_NAMESPACE + "ConfluenceContentPlugin";

	public static final String DEFAULT_RPC_PATH = "/rpc/xmlrpc";
	public static final String DEFAULT_PLUGIN_ACCESS_KEY = "confluence1";
	public static final boolean DEFAULT_USE_STYLES = false;

	private ContentHandler parent;

	private static final int PROCESS_NO_PROCESS = -1;
	private static final int PROCESS_PAGE_ID = 10;
	private static final int PROCESS_CONFLUENCE_URL = 11;
	private static final int PROCESS_CONFLUENCE_URL_VALUE = 12;
	private static final int PROCESS_USE_STYLES = 13;
	private static final int PROCESS_USE_STYLES_VALUE = 14;

	private static final int PROCESS_ACCESS_KEY = 15;

	private int processing = PROCESS_NO_PROCESS;

	private String pageId = null;
	private String confluenceURL = null;
	private boolean useStyles = false;
	private String xmlRpcMethodsPrefix = "confluence1";

	private XmlRpcClient rpcClient;

	/* (non-Javadoc)
	 * @see de.juwimm.cms.plugins.server.ConquestPlugin#configurePlugin(de.juwimm.cms.plugins.server.Request, de.juwimm.cms.plugins.server.Response, org.xml.sax.ContentHandler, java.lang.Integer)
	 */
	public void configurePlugin(Request req, Response resp, ContentHandler ch, Integer uniquePageId) {
		if (log.isDebugEnabled()) log.debug("configurePlugin() -> begin");
		this.parent = ch;
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
		try {
			if (StringUtils.isNotBlank(this.confluenceURL)) {
				String xmlRpcUri = this.confluenceURL + DEFAULT_RPC_PATH;
				if (log.isDebugEnabled()) log.debug("Creating XML-RPC-Client for URI '" + xmlRpcUri + "'.");

				this.rpcClient = new XmlRpcClient(xmlRpcUri);

				// TODO: provide key / value configuration and implement login
				// Vector loginParams = new Vector(2);
				// loginParams.add(USER_NAME);
				// loginParams.add(PASSWORD);
				// String loginToken = (String) rpcClient.execute("confluence1.login", loginParams);

				ConfluencePage cp = this.getPage(null);

				this.parent.startElement("", "confluencePage", "confluencePage", this.createSAXAttributes(cp));

				String renderedContent = this.getRenderedContent(cp, null);
				/*
				 * Print Confluence content into the SAX stream
				 */
				this.parent.startElement("", "renderedContent", "renderedContent", new AttributesImpl());
				SAXHelper.string2sax(renderedContent, this.parent);
				this.parent.endElement("", "renderedContent", "renderedContent");

				this.parent.endElement("", "confluencePage", "confluencePage");
			} else {
				log.error("Confluence URL not found!");
			}
		} catch (Exception exe) {
			log.error(exe.getMessage(), exe);
		}
		if (log.isDebugEnabled()) log.debug("processContent() -> end");
	}

	@SuppressWarnings("unchecked")
	private ConfluencePage getPage(Vector<Object> loginToken) throws Exception {
		if (log.isDebugEnabled()) log.debug("getPage() -> begin");

		String method = this.getFullMethodName("getPage");

		Vector<Object> pageVector = new Vector<Object>();
		pageVector.add(loginToken == null ? "" : loginToken); //LOGIN
		pageVector.add(this.pageId);

		if (log.isDebugEnabled()) log.debug("Executing RPC method '" + method + "'.");
		Hashtable<Object, Object> page = (Hashtable<Object, Object>) this.rpcClient.execute(method, pageVector);
		ConfluencePage cp = new ConfluencePage();
		cp.transform(page);
		if (log.isDebugEnabled()) log.debug("getPage() -> end");
		return cp;
	}

	private String getRenderedContent(ConfluencePage cp, Vector<Object> loginToken) throws Exception {
		if (log.isDebugEnabled()) log.debug("getRenderedContent() -> begin");
		String method = this.getFullMethodName("renderContent");
		Vector<Object> renderContentVector = new Vector<Object>();
		renderContentVector.add(loginToken == null ? "" : loginToken); //LOGIN
		renderContentVector.add(cp.getSpace());
		renderContentVector.add(cp.getId());
		renderContentVector.add(cp.getContent());

		if (!this.useStyles) {
			Hashtable<String, String> renderPars = new Hashtable<String, String>();
			renderPars.put("style", "clean");
			renderContentVector.add(renderPars);
		}
		if (log.isDebugEnabled()) log.debug("Executing RPC method '" + method + "'.");
		if (log.isDebugEnabled()) log.debug("getRenderedContent() -> end");
		return (String) this.rpcClient.execute(method, renderContentVector);
	}

	private Attributes createSAXAttributes(ConfluencePage cp) {
		AttributesImpl attribs = new AttributesImpl();
		SAXHelper.setSAXAttr(attribs, "space", cp.getSpace());
		SAXHelper.setSAXAttr(attribs, "url", cp.getUrl());
		SAXHelper.setSAXAttr(attribs, "id", cp.getId());
		SAXHelper.setSAXAttr(attribs, "homePage", Boolean.toString(cp.isHomePage()));
		SAXHelper.setSAXAttr(attribs, "creator", cp.getCreator());
		SAXHelper.setSAXAttr(attribs, "modifier", cp.getModifier());
		SAXHelper.setSAXAttr(attribs, "contentStatus", cp.getContentStatus());
		SAXHelper.setSAXAttr(attribs, "created", Long.toString(cp.getCreated().getTime()));
		SAXHelper.setSAXAttr(attribs, "parentId", cp.getParentId());
		SAXHelper.setSAXAttr(attribs, "current", Boolean.toString(cp.isCurrent()));
		SAXHelper.setSAXAttr(attribs, "version", Integer.toString(cp.getVersion()));
		SAXHelper.setSAXAttr(attribs, "permissions", Integer.toString(cp.getPermissions()));
		SAXHelper.setSAXAttr(attribs, "title", cp.getTitle());
		SAXHelper.setSAXAttr(attribs, "modified", Long.toString(cp.getModified().getTime()));
		return attribs;
	}

	private String getFullMethodName(String methodName) {
		return this.xmlRpcMethodsPrefix + "." + methodName;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String name, Attributes atts) throws SAXException {
		if (PLUGIN_NAMESPACE.equals(uri)) {
			if ("getPage".equals(localName)) {

			} else if ("pageId".equals(localName)) {
				if (log.isDebugEnabled()) log.debug("Processing element '" + localName + "'.");
				processing = PROCESS_PAGE_ID;
			} else if ("confluenceURLs".equals(localName)) {
				if (log.isDebugEnabled()) log.debug("Processing element '" + localName + "'.");
				this.processing = PROCESS_CONFLUENCE_URL_VALUE;
			} else if ("useStyles".equals(localName)) {
				if (log.isDebugEnabled()) log.debug("Processing element '" + localName + "'.");
				this.processing = PROCESS_USE_STYLES_VALUE;
			} else if ("xmlRpcMethodsPrefix".equals(localName)) {
				if (log.isDebugEnabled()) log.debug("Processing element '" + localName + "'.");
				this.processing = PROCESS_ACCESS_KEY;
			} else if ("value".equals(localName)) {
				if (log.isDebugEnabled()) log.debug("Processing element '" + localName + "'.");
				switch (this.processing) {
					case PROCESS_USE_STYLES_VALUE:
						this.processing = PROCESS_USE_STYLES;
						break;
					case PROCESS_CONFLUENCE_URL_VALUE:
						this.processing = PROCESS_CONFLUENCE_URL;
						break;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		switch (this.processing) {
			case PROCESS_PAGE_ID:
				this.pageId = StringUtils.trimToNull(new String(ch, start, length));
				if (log.isDebugEnabled()) log.debug("Setting pageId to '" + this.pageId + "'.");
				this.processing = PROCESS_NO_PROCESS;
				break;
			case PROCESS_CONFLUENCE_URL:
				this.confluenceURL = StringUtils.trimToNull(new String(ch, start, length));
				//				if (this.confluenceURL != null && this.confluenceURL.endsWith("/")) {
				//					this.confluenceURL = this.confluenceURL.substring(0, this.confluenceURL.lastIndexOf("/"));
				//				}
				if (log.isDebugEnabled()) log.debug("Setting confluenceURL to '" + this.confluenceURL + "'.");
				this.processing = PROCESS_NO_PROCESS;
				break;
			case PROCESS_USE_STYLES:
				this.useStyles = Boolean.parseBoolean(new String(ch, start, length).trim());
				if (log.isDebugEnabled()) log.debug("Setting useStyles to '" + this.useStyles + "'.");
				this.processing = PROCESS_NO_PROCESS;
				break;
			case PROCESS_ACCESS_KEY:
				String xmlRpcMethodsPrefix = StringUtils.trimToNull(new String(ch, start, length));
				if (xmlRpcMethodsPrefix != null) {
					this.xmlRpcMethodsPrefix = xmlRpcMethodsPrefix;
				}
				if (log.isDebugEnabled()) log.debug("Setting xmlRpcMethodsPrefix to '" + this.xmlRpcMethodsPrefix + "'.");
				this.processing = PROCESS_NO_PROCESS;
				break;
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String name) throws SAXException {
		// do nothing
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
