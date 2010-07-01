package org.tizzit.plugins.server.transformer;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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

		//			AttributesImpl newAtts = new AttributesImpl();
		//			newAtts.setAttributes(attrs);
		//			if (this.unitValue != null) {
		//				if (log.isDebugEnabled()) log.debug("found a unitValue: " + unitValue.getUnitId());
		//				try {
		//					SAXHelper.setSAXAttr(newAtts, "unitImageId", this.unitValue.getImageId().toString());
		//				} catch (Exception exe) {
		//					if (log.isDebugEnabled()) log.debug("found a unitValue - but no Image for it ");
		//				}
		//				try {
		//					SAXHelper.setSAXAttr(newAtts, "unitLogoId", this.unitValue.getLogoId().toString());
		//				} catch (Exception exe) {
		//					if (log.isDebugEnabled()) log.debug("found a unitValue - but no Logo for it ");
		//				}
		//			}
		//			if (log.isDebugEnabled()) log.debug("calling startElement with new attrs");
		//			super.startElement(uri, localName, qName, newAtts);

		//		Document doc = XercesHelper.getNewDocument();
		//		if (log.isDebugEnabled()) log.debug("fillNavigation entered.");
		//		String navigationXml = "";
		//
		//		String since = attrs.getValue("since");
		//		int depth = -1;
		//		try {
		//			depth = new Integer(attrs.getValue("depth")).intValue();
		//		} catch (Exception exe) {
		//			if (log.isDebugEnabled()) log.debug("value for 'depth' not found ");
		//		}
		//		try {
		//			viewComponentId = new Integer(attrs.getValue("viewComponentId"));
		//			if (viewComponentId != null) {
		//				viewComponentValue = this.webSpringBean.getViewComponent4Id(viewComponentId);
		//			}
		//		} catch (Exception exe) {
		//			if (log.isDebugEnabled()) log.debug("value for 'viewComponentId' not found ");
		//		}
		//		int ifDistanceToNavigationRoot = -1;
		//		try {
		//			ifDistanceToNavigationRoot = new Integer(attrs.getValue("ifDistanceToNavigationRoot")).intValue();
		//			if (log.isDebugEnabled()) log.debug("GOT ifDistanceToNavigationRoot");
		//		} catch (Exception exe) {
		//			if (log.isDebugEnabled()) log.debug("value for 'ifDistanceToNavigationRoot' not found ");
		//		}
		//		boolean showOnlyAuthorized = false;
		//		try {
		//			showOnlyAuthorized = Boolean.valueOf(attrs.getValue("showOnlyAuthorized")).booleanValue();
		//			if (log.isDebugEnabled()) log.debug("showOnlyAuthorized: " + showOnlyAuthorized);
		//		} catch (Exception e) {
		//			if (log.isDebugEnabled()) log.debug("value for 'showOnlyAuthorized' not found ");
		//		}
		//		try {
		//			if (this.unitValue != null) {
		//				try {
		//					if (log.isDebugEnabled()) log.debug("found that unitValue again: " + unitValue.getUnitId() + " - Try to get it's info...");
		//					Document docUnitInfoXml = XercesHelper.string2Dom(this.webSpringBean.getUnitInfoXml(this.unitValue.getUnitId()));
		//					if (log.isDebugEnabled() && docUnitInfoXml != null) log.debug("got the info for that unit...");
		//					Node page = doc.importNode(docUnitInfoXml.getDocumentElement(), true);
		//					SAXHelper.string2sax(XercesHelper.node2string(page), this);
		//					if (log.isDebugEnabled()) log.debug("attached the unit info to this node...");
		//				} catch (Exception e) {
		//					if (log.isDebugEnabled()) log.debug("Error catched while trying to get unit info from webSpringBean and attache it to the xml", e);
		//				}
		//			}
		//		} catch (Exception exe) {
		//		}
		//		try {
		//			if (ifDistanceToNavigationRoot == -1 || webSpringBean.getNavigationRootDistance4VCId(viewComponentValue.getViewComponentId()) >= ifDistanceToNavigationRoot) {
		//				navigationXml = webSpringBean.getNavigationXml(viewComponentId, since, depth, iAmTheLiveserver);
		//				if (navigationXml != null && !"".equalsIgnoreCase(navigationXml)) {
		//					try {
		//						Document docNavigationXml = XercesHelper.string2Dom(navigationXml);
		//						// add axis
		//						if (!disableNavigationAxis) {
		//							String viewComponentXPath = "//viewcomponent[@id=\"" + viewComponentId + "\"]";
		//							if (log.isDebugEnabled()) log.debug("Resolving Navigation Axis: " + viewComponentXPath);
		//							Node found = XercesHelper.findNode(docNavigationXml, viewComponentXPath);
		//							if (found != null) {
		//								if (log.isDebugEnabled()) log.debug("Found Axis in viewComponentId " + viewComponentId);
		//								this.setAxisToRootAttributes(found);
		//							} else {
		//								ViewComponentValue axisVcl = webSpringBean.getViewComponent4Id(viewComponentValue.getParentId());
		//								while (axisVcl != null) {
		//									found = XercesHelper.findNode(docNavigationXml, "//viewcomponent[@id=\"" + axisVcl.getViewComponentId() + "\"]");
		//									if (found != null) {
		//										if (log.isDebugEnabled()) log.debug("Found Axis in axisVcl " + axisVcl.getViewComponentId());
		//										this.setAxisToRootAttributes(found);
		//										break;
		//									}
		//									axisVcl = axisVcl.getParentId() == null ? null : webSpringBean.getViewComponent4Id(axisVcl.getParentId());
		//								}
		//							}
		//						}
		//						// filter safeGuard
		//						if (showOnlyAuthorized) {
		//							try {
		//								String allNavigationXml = XercesHelper.doc2String(docNavigationXml);
		//								String filteredNavigationXml = this.webSpringBean.filterNavigation(allNavigationXml, safeguardMap);
		//								if (log.isDebugEnabled()) {
		//									log.debug("allNavigationXml\n" + allNavigationXml);
		//									log.debug("filteredNavigationXml\n" + filteredNavigationXml);
		//								}
		//								docNavigationXml = XercesHelper.string2Dom(filteredNavigationXml);
		//							} catch (Exception e) {
		//								log.error("Error filtering navigation with SafeGuard: " + e.getMessage(), e);
		//							}
		//						}
		//						// Insert navigationXml -> sitemap
		//						Node page = doc.importNode(docNavigationXml.getFirstChild(), true);
		//						SAXHelper.string2sax(XercesHelper.node2string(page), this);
		//					} catch (Exception exe) {
		//						log.error("An error occured", exe);
		//					}
		//				}
		//			}
		//		} catch (Exception ex) {
		//			log.warn("Exception in NavigationTransformer accured: " + ex.getMessage(), ex);
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
