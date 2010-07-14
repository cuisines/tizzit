package org.tizzit.cocoon.transformation;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tizzit.util.XercesHelper;
import org.tizzit.util.xml.SAXHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

/**
 * <p>
 * <b>Namespace: <code>http://plugins.tizzit.org/NavigationTransformerPlugin</code></b>
 * </p>
 *
 * @author <a href="mailto:rene.hertzfeldt@juwimm.com">Rene Hertzfeldt</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id: NavigationTransformerPlugin.java 759 2010-05-05 13:34:28Z rene.hertzfeldt $
 */
public class NavigationTransformerPlugin implements TizzitPlugin {
	private static final Log log = LogFactory.getLog(NavigationTransformerPlugin.class);

	public static final String PLUGIN_NAMESPACE = Constants.PLUGIN_NAMESPACE + "NavigationTransformerPlugin";
	private ContentHandler parent;
	private final String NAVIGATION = "navigation";
	private Integer viewComponentId = null;
	private final boolean iAmTheLiveserver = true;
	
	private UnitValue unitValue;

	//private WebServiceSpring webSpringBean = null;

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
		if (localName.compareTo(NAVIGATION) == 0) {
			startNavigationElement(uri, localName, qName, attrs);
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

	/*
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startNavigationElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		AttributesImpl newAttrs = (AttributesImpl) attrs;
		String since = attrs.getValue("since");
		int depth = -1;
		try {
			depth = Integer.decode(attrs.getValue("depth")).intValue();
		} catch (Exception exe) {
		}
		int ifDistanceToNavigationRoot = -1;
		try {
			ifDistanceToNavigationRoot = Integer.decode(attrs.getValue("ifDistanceToNavigationRoot")).intValue();
			if (log.isDebugEnabled()) log.debug("GOT ifDistanceToNavigationRoot");
		} catch (Exception exe) {
		}
		boolean showOnlyAuthorized = false;
		try {
			showOnlyAuthorized = Boolean.valueOf(attrs.getValue("showOnlyAuthorized")).booleanValue();
			if (log.isDebugEnabled()) log.debug("showOnlyAuthorized: " + showOnlyAuthorized);
		} catch (Exception e) {
		}

		try {
			if (this.unitValue != null) {
				try {
					SAXHelper.setSAXAttr(newAttrs, "unitImageId", this.unitValue.getImageId().toString());
				} catch (Exception exe) {
				}
				try {
					SAXHelper.setSAXAttr(newAttrs,"unitLogoId", this.unitValue.getLogoId().toString());
				} catch (Exception exe) {
				}
				parent.startElement(uri, localName, qName, newAttrs);
				try {
					//String unitInfo = this.webSpringBean.getUnitInfoXml(this.unitValue.getUnitId());
					//FIXME: string for test only
					String unitInfo = "<unit id=\"5\" siteId=\"4\"><unitName>rootUnit tizzit</unitName><lastModified>09.11.2009 15:01:53</lastModified><unitImage/><unitLogo/></unit>";
					SAXHelper.string2sax(unitInfo, parent);
				} catch (Exception e) {
					if (log.isDebugEnabled()) log.debug(e.getMessage(), e);
				}
			}
		} catch (Exception exe) {
		}

//		if (ifDistanceToNavigationRoot == -1 || webSpringBean.getNavigationRootDistance4VCId(viewComponentValue.getViewComponentId()) >= ifDistanceToNavigationRoot) {
//			navigationXml = webSpringBean.getNavigationXml(viewComponentId, since, depth, iAmTheLiveserver);
//			if (navigationXml != null && !"".equalsIgnoreCase(navigationXml)) {
//				try {
//					Document docNavigationXml = XercesHelper.string2Dom(navigationXml);
//					// add axis
//					if (!disableNavigationAxis) {
//						String viewComponentXPath = "//viewcomponent[@id=\"" + viewComponentId + "\"]";
//						if (log.isDebugEnabled()) log.debug("Resolving Navigation Axis: " + viewComponentXPath);
//						Node found = XercesHelper.findNode(docNavigationXml, viewComponentXPath);
//						if (found != null) {
//							if (log.isDebugEnabled()) log.debug("Found Axis in viewComponentId " + viewComponentId);
//							this.setAxisToRootAttributes(found);
//						} else {
//							ViewComponentValue axisVcl = webSpringBean.getViewComponent4Id(viewComponentValue.getParentId());
//							while (axisVcl != null) {
//								found = XercesHelper.findNode(docNavigationXml, "//viewcomponent[@id=\"" + axisVcl.getViewComponentId() + "\"]");
//								if (found != null) {
//									if (log.isDebugEnabled()) log.debug("Found Axis in axisVcl " + axisVcl.getViewComponentId());
//									this.setAxisToRootAttributes(found);
//									break;
//								}
//								axisVcl = axisVcl.getParentId() == null ? null : webSpringBean.getViewComponent4Id(axisVcl.getParentId());
//							}
//						}
//					}
//					// filter safeGuard
//					if (showOnlyAuthorized) {
//						try {
//							String allNavigationXml = XercesHelper.doc2String(docNavigationXml);
//							String filteredNavigationXml = this.webSpringBean.filterNavigation(allNavigationXml, safeguardMap);
//							if (log.isDebugEnabled()) {
//								log.debug("allNavigationXml\n" + allNavigationXml);
//								log.debug("filteredNavigationXml\n" + filteredNavigationXml);
//							}
//							docNavigationXml = XercesHelper.string2Dom(filteredNavigationXml);
//						} catch (Exception e) {
//							log.error("Error filtering navigation with SafeGuard: " + e.getMessage(), e);
//						}
//					}
//					// Insert navigationXml -> sitemap
//					Node page = doc.importNode(docNavigationXml.getFirstChild(), true);
//					navigation.appendChild(page);
//				} catch (Exception exe) {
//					log.error("An error occured", exe);
//				}
//			}
//		}

	}

	private void setAxisToRootAttributes(Node found) {
		Node changeNode = found;
		while (changeNode != null && changeNode instanceof Element && changeNode.getNodeName().equalsIgnoreCase("viewcomponent")) {
			((Element) changeNode).setAttribute("onAxisToRoot", "true");
			changeNode = changeNode.getParentNode();
		}
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
