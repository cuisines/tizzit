package org.tizzit.plugins.server.transformer;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import de.juwimm.cms.plugins.Constants;
import de.juwimm.cms.plugins.server.Request;
import de.juwimm.cms.plugins.server.Response;
import de.juwimm.cms.plugins.server.TizzitPlugin;
import de.juwimm.cms.vo.ViewComponentValue;

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
 * @version $Id: ConfluenceContentPlugin.java 543 2009-10-23 11:33:02Z eduard.siebert@online.de $
 * @since tizzit-plugin-sample 15.10.2009
 */
public class UnitInformationTransformerPlugin implements TizzitPlugin {
	private static final Log log = LogFactory.getLog(UnitInformationTransformerPlugin.class);

	public static final String PLUGIN_NAMESPACE = Constants.PLUGIN_NAMESPACE + "UnitInformationTransformerPlugin";
	private ContentHandler parent;
	private final String UNITINFORMATION = "unitInformation";
	private boolean inUnitInformation = false;
	//private final WebServiceSpring webSpringBean = null;
	private final Integer viewComponentId = null;

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
			inUnitInformation = true;
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

	private void fillUnitInformation(Node nde, ViewComponentValue vcl) throws Exception {
		ViewComponentValue myVcl = null;
		//		if (vcl != null) {
		//			myVcl = vcl;
		//		} else {
		//			myVcl = viewComponentValue;
		//		}
		//		Iterator it = XercesHelper.findNodes(nde, ".//unitInformation");
		//
		//		while (it.hasNext()) {
		//			Element el = (Element) it.next();
		//			String strUn = el.getAttribute("unitId");
		//
		//			UnitValue uv = null;
		//			if (strUn != null && !strUn.equals("")) {
		//				uv = webSpringBean.getUnit(new Integer(strUn));
		//			} else {
		//				uv = webSpringBean.getUnit4ViewComponent(myVcl.getViewComponentId());
		//				el.setAttribute("unitId", uv.getUnitId().toString());
		//			}
		//
		//			Integer viewDocumentId = webSpringBean.getViewDocument4ViewComponentId(myVcl.getViewComponentId()).getViewDocumentId();
		//			try {
		//				String unitPath = webSpringBean.getPath4Unit(uv.getUnitId(), viewDocumentId);
		//				el.setAttribute("url", unitPath);
		//			} catch (Exception exe) {
		//			}
		//
		//			el.setAttribute("unitName", uv.getName());
		//		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (log.isDebugEnabled()) log.debug("endElement: " + localName + " in nameSpace: " + uri);
		if (localName.compareTo(UNITINFORMATION) == 0) {
			inUnitInformation = false;
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
