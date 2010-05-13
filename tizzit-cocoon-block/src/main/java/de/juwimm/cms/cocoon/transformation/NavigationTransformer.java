/**
 * @author rhertzfeldt
 * @lastChange 4:24:36 PM
 */
package de.juwimm.cms.cocoon.transformation;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.avalon.excalibur.pool.Recyclable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractTransformer;
import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.tizzit.util.xml.SAXHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import de.juwimm.cms.beans.WebServiceSpring;
import de.juwimm.cms.cocoon.helper.CocoonSpringHelper;
import de.juwimm.cms.vo.UnitValue;
import de.juwimm.cms.vo.ViewComponentValue;

/**
 * @author rhertzfeldt
 *
 */
public class NavigationTransformer extends AbstractTransformer implements Recyclable {
	private static Logger log = Logger.getLogger(NavigationTransformer.class);
	private WebServiceSpring webSpringBean = null;
	private Integer viewComponentId = null;
	private ViewComponentValue viewComponentValue = null;
	private UnitValue unitValue = null;
	private boolean iAmTheLiveserver = false;
	private boolean disableNavigationAxis = false;
	private Serializable uniqueKey;
	private Request request = null;
	private Map<String, String> safeguardMap = null;

	/* (non-Javadoc)
	 * @see org.apache.cocoon.sitemap.SitemapModelComponent#setup(org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
	 */
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) throws ProcessingException, SAXException, IOException {
		if (log.isDebugEnabled()) log.debug("begin setup with src: " + src);
		try {
			webSpringBean = (WebServiceSpring) CocoonSpringHelper.getBean(objectModel, CocoonSpringHelper.WEB_SERVICE_SPRING);
		} catch (Exception exf) {
			log.error("could not load webServiceSpringBean ", exf);
		}
		try {
			viewComponentId = new Integer(par.getParameter("viewComponentId"));
			request = ObjectModelHelper.getRequest(objectModel);
			uniqueKey = viewComponentId + src + "?" + request.getQueryString();
			if (log.isDebugEnabled()) {
				log.debug("UniqueKey: " + uniqueKey);
			}
			try {
				viewComponentValue = webSpringBean.getViewComponent4Id(viewComponentId);
				unitValue = webSpringBean.getUnit4ViewComponent(viewComponentValue.getViewComponentId());
			} catch (Exception e) {
				if (log.isDebugEnabled()) {
					log.debug(e.getMessage());
				}
			}

			HttpSession session = this.request.getSession(true);
			try {
				this.safeguardMap = (Map<String, String>) session.getAttribute("safeGuardService");
				if (this.safeguardMap == null) {
					if (log.isDebugEnabled()) log.debug("no SafeguardMap");
					this.safeguardMap = new HashMap<String, String>();
					if (log.isDebugEnabled()) log.debug("created new SafeguardMap");
					session.setAttribute("safeGuardService", this.safeguardMap);
					if (log.isDebugEnabled()) log.debug("put SafeguardMap into Session");
				} else {
					if (log.isDebugEnabled()) log.debug("found SafeguardMap");
				}
			} catch (Exception cookieex) {
				log.warn("SafeGuard-Error: " + cookieex.getMessage());
			}
		} catch (Exception exe) {
			viewComponentId = null;
		}
		try {
			disableNavigationAxis = new Boolean(par.getParameter("disableNavigationAxis")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			iAmTheLiveserver = new Boolean(par.getParameter("liveserver")).booleanValue();
		} catch (Exception exe) {
		}
		if (log.isDebugEnabled()) log.debug("end setup");
	}

	@Override
	public void recycle() {
		if (log.isDebugEnabled()) log.debug("begin recycle");

		super.recycle();
		disableNavigationAxis = false;
		request = null;
		if (log.isDebugEnabled()) log.debug("end recycle");
	}

	/*
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		if (localName.equals("navigation")) {
			AttributesImpl newAtts = new AttributesImpl();
			newAtts.setAttributes(attrs);
			if (this.unitValue != null) {
				if (log.isDebugEnabled()) log.debug("found a unitValue: " + unitValue.getUnitId());
				try {
					SAXHelper.setSAXAttr(newAtts, "unitImageId", this.unitValue.getImageId().toString());
				} catch (Exception exe) {
					if (log.isDebugEnabled()) log.debug("found a unitValue - but no Image for it ");
				}
				try {
					SAXHelper.setSAXAttr(newAtts, "unitLogoId", this.unitValue.getLogoId().toString());
				} catch (Exception exe) {
					if (log.isDebugEnabled()) log.debug("found a unitValue - but no Logo for it ");
				}
			}
			if (log.isDebugEnabled()) log.debug("calling startElement with new attrs");
			super.startElement(uri, localName, qName, newAtts);
		} else {
			super.startElement(uri, localName, qName, attrs);
		}
		if (localName.equals("navigation")) {
			Document doc = XercesHelper.getNewDocument();
			if (log.isDebugEnabled()) log.debug("fillNavigation entered.");
			String navigationXml = "";

			String since = attrs.getValue("since");
			int depth = -1;
			try {
				depth = new Integer(attrs.getValue("depth")).intValue();
			} catch (Exception exe) {
				if (log.isDebugEnabled()) log.debug("value for 'depth' not found ");
			}
			try {
				viewComponentId = new Integer(attrs.getValue("viewComponentId"));
				if (viewComponentId != null) {
					viewComponentValue = this.webSpringBean.getViewComponent4Id(viewComponentId);
				}
			} catch (Exception exe) {
				if (log.isDebugEnabled()) log.debug("value for 'viewComponentId' not found ");
			}
			int ifDistanceToNavigationRoot = -1;
			try {
				ifDistanceToNavigationRoot = new Integer(attrs.getValue("ifDistanceToNavigationRoot")).intValue();
				if (log.isDebugEnabled()) log.debug("GOT ifDistanceToNavigationRoot");
			} catch (Exception exe) {
				if (log.isDebugEnabled()) log.debug("value for 'ifDistanceToNavigationRoot' not found ");
			}
			boolean showOnlyAuthorized = false;
			try {
				showOnlyAuthorized = Boolean.valueOf(attrs.getValue("showOnlyAuthorized")).booleanValue();
				if (log.isDebugEnabled()) log.debug("showOnlyAuthorized: " + showOnlyAuthorized);
			} catch (Exception e) {
				if (log.isDebugEnabled()) log.debug("value for 'showOnlyAuthorized' not found ");
			}
			try {
				if (this.unitValue != null) {
					try {
						if (log.isDebugEnabled()) log.debug("found that unitValue again: " + unitValue.getUnitId() + " - Try to get it's info...");
						Document docUnitInfoXml = XercesHelper.string2Dom(this.webSpringBean.getUnitInfoXml(this.unitValue.getUnitId()));
						if (log.isDebugEnabled() && docUnitInfoXml != null) log.debug("got the info for that unit...");
						Node page = doc.importNode(docUnitInfoXml.getDocumentElement(), true);
						SAXHelper.string2sax(XercesHelper.node2string(page), this);
						if (log.isDebugEnabled()) log.debug("attached the unit info to this node...");
					} catch (Exception e) {
						if (log.isDebugEnabled()) log.debug("Error catched while trying to get unit info from webSpringBean and attache it to the xml", e);
					}
				}
			} catch (Exception exe) {
			}
			try {
				if (ifDistanceToNavigationRoot == -1 || webSpringBean.getNavigationRootDistance4VCId(viewComponentValue.getViewComponentId()) >= ifDistanceToNavigationRoot) {
					navigationXml = webSpringBean.getNavigationXml(viewComponentId, since, depth, iAmTheLiveserver);
					if (navigationXml != null && !"".equalsIgnoreCase(navigationXml)) {
						try {
							Document docNavigationXml = XercesHelper.string2Dom(navigationXml);
							// add axis
							if (!disableNavigationAxis) {
								String viewComponentXPath = "//viewcomponent[@id=\"" + viewComponentId + "\"]";
								if (log.isDebugEnabled()) log.debug("Resolving Navigation Axis: " + viewComponentXPath);
								Node found = XercesHelper.findNode(docNavigationXml, viewComponentXPath);
								if (found != null) {
									if (log.isDebugEnabled()) log.debug("Found Axis in viewComponentId " + viewComponentId);
									this.setAxisToRootAttributes(found);
								} else {
									ViewComponentValue axisVcl = webSpringBean.getViewComponent4Id(viewComponentValue.getParentId());
									while (axisVcl != null) {
										found = XercesHelper.findNode(docNavigationXml, "//viewcomponent[@id=\"" + axisVcl.getViewComponentId() + "\"]");
										if (found != null) {
											if (log.isDebugEnabled()) log.debug("Found Axis in axisVcl " + axisVcl.getViewComponentId());
											this.setAxisToRootAttributes(found);
											break;
										}
										axisVcl = axisVcl.getParentId() == null ? null : webSpringBean.getViewComponent4Id(axisVcl.getParentId());
									}
								}
							}
							// filter safeGuard
							if (showOnlyAuthorized) {
								try {
									String allNavigationXml = XercesHelper.doc2String(docNavigationXml);
									String filteredNavigationXml = this.webSpringBean.filterNavigation(allNavigationXml, safeguardMap);
									if (log.isDebugEnabled()) {
										log.debug("allNavigationXml\n" + allNavigationXml);
										log.debug("filteredNavigationXml\n" + filteredNavigationXml);
									}
									docNavigationXml = XercesHelper.string2Dom(filteredNavigationXml);
								} catch (Exception e) {
									log.error("Error filtering navigation with SafeGuard: " + e.getMessage(), e);
								}
							}
							// Insert navigationXml -> sitemap
							Node page = doc.importNode(docNavigationXml.getFirstChild(), true);
							SAXHelper.string2sax(XercesHelper.node2string(page), this);
						} catch (Exception exe) {
							log.error("An error occured", exe);
						}
					}
				}
			} catch (Exception ex) {
				log.warn("Exception in NavigationTransformer accured: " + ex.getMessage(), ex);
			}
		}
	}

	private void setAxisToRootAttributes(Node found) {
		Node changeNode = found;
		while (changeNode != null && changeNode instanceof Element && changeNode.getNodeName().equalsIgnoreCase("viewcomponent")) {
			((Element) changeNode).setAttribute("onAxisToRoot", "true");
			changeNode = changeNode.getParentNode();
		}
	}

}
