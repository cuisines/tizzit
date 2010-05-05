/**
 * @author rhertzfeldt
 * @lastChange 4:24:36 PM
 */
package de.juwimm.cms.cocoon.transformation;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.xml.parsers.SAXParser;

import org.apache.avalon.excalibur.pool.Recyclable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.components.source.SourceUtil;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Response;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractTransformer;
import org.apache.cocoon.webapps.session.ContextManager;
import org.apache.cocoon.webapps.session.context.SessionContext;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.tizzit.util.xml.SAXHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import de.juwimm.cms.beans.PluginManagement;
import de.juwimm.cms.beans.WebServiceSpring;
import de.juwimm.cms.beans.cocoon.ModifiedDateContentHandler;
import de.juwimm.cms.cocoon.generation.CmsContentGenerator;
import de.juwimm.cms.cocoon.helper.CocoonSpringHelper;
import de.juwimm.cms.search.beans.SearchengineService;
import de.juwimm.cms.vo.SiteValue;
import de.juwimm.cms.vo.UnitValue;
import de.juwimm.cms.vo.ViewComponentValue;
import de.juwimm.cms.vo.ViewDocumentValue;

/**
 * @author rhertzfeldt
 *
 */
public class NavigationTransformer extends AbstractTransformer implements Recyclable {
	private static Logger log = Logger.getLogger(NavigationTransformer.class);
	private static Logger cacheLogger = Logger.getLogger(CmsContentGenerator.class.getName() + "-CacheLogger");
	private static final DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	public static final int ADDRESSTYPE_OFFICE = 1;
	public static final int ADDRESSTYPE_SECRETARIAT = 2;
	public static final int ADDRESSTYPE_POSTADDRESS = 3;
	public static final int ADDRESSTYPE_OTHERS = 4;
	public static final int ADDRESSTYPE_PRIVATE = 5;
	private Source inputSource = null;
	private SearchengineService searchengineService = null;
	private WebServiceSpring webSpringBean = null;
	private Integer viewComponentId = null;
	private ViewComponentValue viewComponentValue = null;
	private ModifiedDateContentHandler mdch = null;
	private SiteValue siteValue = null;
	private UnitValue unitValue = null;
	private final Integer depth = null;
	private Parameters par = null;
	private SessionContext sessContext = null;
	private final ServiceManager serviceManager = null;
	private ContextManager cm = null;
	private boolean iAmTheLiveserver = false;

	private boolean parameters = false;
	private boolean disableMeta = false;
	private boolean disableHeadLine = false;
	private boolean disableMembersList = false;
	private boolean disableUnitList = false;
	private boolean disableUnitInformation = false;
	private boolean disableFulltext = false;
	private boolean disableNavigation = false;
	private boolean disableNavigationAxis = false;
	private boolean disableNavigationBackward = false;
	private boolean disableAggregations = false;
	private boolean disableInternalLinks = false;
	private boolean disableLanguageVersions = true; // we need only in menu.xsl, UKD
	private boolean disableContentInclude = true;
	private boolean disableTeaserInclude = true;

	private Serializable uniqueKey;
	private Request request = null;
	private Response response = null;
	private final String webSearchquery = null;
	private long chgDate = 0;
	private final SAXParser parser = null;
	private PluginManagement pluginManagement = null;
	private String requestUrl = null;
	private Map<String, String> safeguardMap = null;
	private final Map<Integer, String> path4ViewComponentCacheMap = new HashMap<Integer, String>();

	/* (non-Javadoc)
	 * @see org.apache.cocoon.sitemap.SitemapModelComponent#setup(org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
	 */
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) throws ProcessingException, SAXException, IOException {
		if (log.isDebugEnabled()) log.debug("begin setup with src: " + src);
		this.requestUrl = src;
		this.par = par;

		try {
			pluginManagement = (PluginManagement) CocoonSpringHelper.getBean(objectModel, CocoonSpringHelper.PLUGIN_MANAGEMENT);
		} catch (Exception exf) {
			log.error("could not load pluginManagement ", exf);
		}
		try {
			webSpringBean = (WebServiceSpring) CocoonSpringHelper.getBean(objectModel, CocoonSpringHelper.WEB_SERVICE_SPRING);
		} catch (Exception exf) {
			log.error("could not load webServiceSpringBean ", exf);
		}
		try {
			searchengineService = (SearchengineService) CocoonSpringHelper.getBean(objectModel, CocoonSpringHelper.SEARCHENGINE_SERVICE_SPRING);
		} catch (Exception exf) {
			log.error("could not load searchengineService ", exf);
		}
		try {
			mdch = (ModifiedDateContentHandler) CocoonSpringHelper.getBean(objectModel, CocoonSpringHelper.MODIFIED_DATE_CONTENT_HANDLER);
		} catch (Exception exf) {
			log.error("could not load ModifiedDateContentHandler ", exf);
		}

		try {
			if (src != null) {
				try {
					this.inputSource = resolver.resolveURI(src);
				} catch (SourceException se) {
					throw SourceUtil.handle("Error during resolving of '" + src + "'.", se);
				}
			} else {
				this.inputSource = null;
			}
			viewComponentId = new Integer(par.getParameter("viewComponentId"));
			request = ObjectModelHelper.getRequest(objectModel);
			response = ObjectModelHelper.getResponse(objectModel);
			uniqueKey = viewComponentId + src + "?" + request.getQueryString();
			if (log.isDebugEnabled()) {
				log.debug("UniqueKey: " + uniqueKey);
			}
			try {
				viewComponentValue = webSpringBean.getViewComponent4Id(viewComponentId);
				siteValue = webSpringBean.getSite4VCId(viewComponentId);
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
			cm = (ContextManager) this.serviceManager.lookup(ContextManager.ROLE);
			sessContext = cm.getContext("authentication");
		} catch (Exception exe) {
			viewComponentId = null;
		}
		try {
			parameters = new Boolean(par.getParameter("parameters")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableMeta = new Boolean(par.getParameter("disableMeta")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableHeadLine = new Boolean(par.getParameter("disableHeadLine")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableAggregations = new Boolean(par.getParameter("disableAggregations")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableMembersList = new Boolean(par.getParameter("disableMembersList")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableUnitList = new Boolean(par.getParameter("disableUnitList")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableInternalLinks = new Boolean(par.getParameter("disableInternalLinks")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableUnitInformation = new Boolean(par.getParameter("disableUnitInformation")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableFulltext = new Boolean(par.getParameter("disableFulltext")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableNavigation = new Boolean(par.getParameter("disableNavigation")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableNavigationAxis = new Boolean(par.getParameter("disableNavigationAxis")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableNavigationBackward = new Boolean(par.getParameter("disableNavigationBackward")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableLanguageVersions = new Boolean(par.getParameter("disableLanguageVersions")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableContentInclude = new Boolean(par.getParameter("disableContentInclude")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableTeaserInclude = new Boolean(par.getParameter("disableTeaserInclude")).booleanValue();
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
		//		if (null != this.inputSource) {
		//			resolver.release(this.inputSource);
		//			this.inputSource = null;
		//		}
		super.recycle();
		parameters = true;
		disableNavigation = false;
		disableNavigationBackward = false;
		disableNavigationAxis = false;
		disableAggregations = false;
		disableInternalLinks = false;
		disableUnitInformation = false;
		disableFulltext = false;
		disableMeta = false;
		disableHeadLine = false;
		disableUnitList = false;
		disableMembersList = false;
		disableContentInclude = false;
		disableTeaserInclude = false;
		disableLanguageVersions = false;
		request = null;
		response = null;
		chgDate = 0;
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

	/*
	 * if (log.isDebugEnabled()) log.debug("begin transform for navigation element");
			String safeguardUsername = null;
			try {
				viewComponentId = new Integer(attrs.getValue("viewComponentId"));
			} catch (Exception exe) {
			}
			if (log.isDebugEnabled()) {
				if (viewComponentId != null) {
					log.debug("found viewComponentId: " + viewComponentId);
				} else {
					log.debug("Could not find viewComponentId.");
				}
			}
			try {
				depth = new Integer(attrs.getValue("depth"));
			} catch (Exception exe) {
			}
			if (log.isDebugEnabled()) {
				if (depth != null) {
					log.debug("found depth: " + depth);
				} else {
					log.debug("Could not find depth.");
				}
			}
			try {
				iAmTheLiveserver = Boolean.getBoolean(attrs.getValue("isLiveServer"));
			} catch (Exception exe) {
			}
			try {
				safeguardUsername = attrs.getValue("safeguardUsername");
			} catch (Exception exe) {
			}
			try {
				attrs.getValue("since");
			} catch (Exception exe) {
			}
			try {
				attrs.getValue("languageCode");
			} catch (Exception exe) {
			}
			try {
				Document doc = this.generate();
				String docString = documentToString(doc);
				SAXHelper.string2sax(docString, this);
			} catch (ProcessingException e) {
				if (log.isDebugEnabled()) log.debug("Error while generating output ", e);
			}
	 */

	//	public Document generate() throws SAXException, ProcessingException {
	//		if (log.isDebugEnabled()) log.debug("begin generate");
	//		if (viewComponentId == null) {
	//			throw new ResourceNotFoundException("Could not find " + request.getRequestURI());
	//		}
	//
	//		Document doc = null;
	//		InputSource in = null;
	//
	//		if (this.inputSource != null) {
	//			if (log.isDebugEnabled()) log.debug("loading xml from file");
	//			try {
	//				in = new InputSource(this.inputSource.getInputStream());
	//			} catch (Exception exe) {
	//			}
	//		} else {
	//			if (log.isDebugEnabled()) log.debug("loading xml from database");
	//			try {
	//				//	String content = webService.getContent(viewComponentId, iAmTheLiveserver);
	//				if (cacheLogger.isDebugEnabled()) {
	//					cacheLogger.debug("generating content at " + sdf.format(new Date()));
	//				}
	//				String content = webSpringBean.getContent(viewComponentId, iAmTheLiveserver);
	//				//	enterlogger.debug(content);
	//				in = new InputSource(new StringReader(content));
	//			} catch (Exception exe) {
	//				log.warn("Error getting content for \"" + this.requestUrl + "\": " + exe.getMessage(), exe);
	//			}
	//		}
	//		if (in == null) {
	//			throw new ResourceNotFoundException("Could not find resource with ");
	//		}
	//		try {
	//			doc = XercesHelper.inputSource2Dom(in);
	//		} catch (Exception exe) {
	//			throw new ProcessingException("Error parsing the content", exe);
	//		}
	//
	//		try {
	//			//			if (!disableContentInclude) {
	//			//				this.fillContentInclude(doc);
	//			//				this.fillTeaserInclude(doc);
	//			//			}
	//			//			if (!disableUnitInformation) // has to be before fulltext, because fulltext will fill its own
	//			//				this.fillUnitInformation(doc, null);
	//			//			if (!disableFulltext) // has to be at the beginning, because it can contain other tags as well
	//			//				this.fillFulltext(doc);
	//			//			if (!disableMeta) this.fillMeta(doc);
	//			//			if (!disableHeadLine) this.fillHeadLine(doc);
	//			//			if (!disableAggregations) this.fillAggregations(doc);
	//			//			if (!disableMembersList) this.fillMembersList(doc);
	//			//			if (!disableUnitList) // im filling also unitInformations
	//			//				this.fillUnitList(doc);
	//			//			if (!disableInternalLinks) this.solveInternalLinks(doc);
	//			if (!disableNavigation) this.fillNavigation(doc);
	//			if (!disableNavigationBackward) this.fillNavigationBackward(doc);
	//			if (!disableLanguageVersions) this.fillLanguageVersions(doc);
	//		} catch (Exception e) {
	//			String errMsg = "An error occured while processing the content";
	//			log.error(errMsg, e);
	//			throw new ProcessingException(errMsg, e);
	//		}
	//		//		if (doc != null && contentHandler != null) {
	//		//			if (log.isDebugEnabled()) log.debug("start streaming to sax");
	//		//			try {
	//		//				ContentHandler contentHandlerWrapper = new PluginContentHandler(pluginManagement, contentHandler, new RequestImpl(request), new ResponseImpl(response), viewComponentId, siteValue.getSiteId());
	//		//				contentHandlerWrapper.startDocument();
	//		//				DOMStreamer ds = new DOMStreamer(contentHandlerWrapper);
	//		//				ds.stream(doc.getDocumentElement());
	//		//				contentHandlerWrapper.endDocument();
	//		//			} catch (Exception exe) {
	//		//				log.error("An error occured", exe);
	//		//			}
	//		//		}
	//		if (log.isDebugEnabled()) log.debug("end generate");
	//		return doc;
	//	}
	/**
	 * creates langName and langUrl Elements under languageVersions Element
	 */
	private void fillLanguageVersions(Document doc) {
		try {
			String language = par.getParameter("language");
			ViewDocumentValue[] vdd = webSpringBean.getViewDocuments4Site(siteValue.getSiteId());
			for (int i = 0; i < vdd.length; i++) {
				String lang = vdd[i].getLanguage();
				if (!lang.equals(language)) { // give me only those vc's from other languages
					try {
						Integer unitId = webSpringBean.getUnit4ViewComponent(viewComponentValue.getViewComponentId()).getUnitId();
						Integer viewDocumentId = vdd[i].getViewDocumentId();
						ViewComponentValue vcl = webSpringBean.getViewComponent4Unit(unitId, viewDocumentId);
						if (vcl != null) {
							// Maybe for this language there is no page for this unit
							if (webSpringBean.isVisibleForLanguageVersion(vcl, iAmTheLiveserver)) {
								String langpath = webSpringBean.getPath4ViewComponent(vcl.getViewComponentId());
								Iterator it = XercesHelper.findNodes(doc, "//languageVersions");
								while (it.hasNext()) {
									Node nde = (Node) it.next();
									Node ndeLanguage = doc.createElement("language");
									nde.appendChild(ndeLanguage);
									Node langName = doc.createElement("langName");
									Node langNameText = doc.createTextNode(lang);
									langName.appendChild(langNameText);
									ndeLanguage.appendChild(langName);
									Node langUrl = doc.createElement("langUrl");
									Node langUrlText = doc.createTextNode(langpath);
									langUrl.appendChild(langUrlText);
									ndeLanguage.appendChild(langUrl);
								}
							}
						}
					} catch (Exception exe) { //if the vcl wont be found. thats ok :)
						if (log.isDebugEnabled()) {
							log.debug("An error occured", exe);
						}
					}
				}
			}
		} catch (Exception exe) {
			log.error("An error occured", exe);
		}
	}

	private void fillMeta(Document doc) throws Exception {
		Node head = XercesHelper.findNode(doc, "//source/head");
		if (head != null) {
			Element metaDesc = doc.createElement("meta");
			Element metaKeyw = doc.createElement("meta");
			Element metaLastEdited = doc.createElement("meta");
			metaDesc.setAttribute("name", "description");
			metaKeyw.setAttribute("name", "keywords");
			metaLastEdited.setAttribute("name", "LastUpdated");
			String desc = "";
			String keyw = "";
			String lastEdited = "";
			try {
				desc = viewComponentValue.getMetaDescription();
				keyw = viewComponentValue.getMetaData();
				lastEdited = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.US).format(webSpringBean.getModifiedDate4Info(viewComponentId));
			} catch (Exception exe) {
			}
			if (desc != null) metaDesc.setAttribute("content", desc);
			if (keyw != null) metaKeyw.setAttribute("content", keyw);
			if (lastEdited != null) metaLastEdited.setAttribute("content", lastEdited);
			head.appendChild(metaDesc);
			head.appendChild(metaKeyw);
			head.appendChild(metaLastEdited);
		}
	}

	//	<navigationBackward since="lastNavigationRoot" dontShowFirst="2"/>
	private void fillNavigationBackward(Document doc) throws Exception {
		Iterator it = XercesHelper.findNodes(doc, "//navigationBackward");
		String sm = "";

		while (it.hasNext()) {
			if (log.isDebugEnabled()) log.debug("found navigationBackward");
			Element navigation = (Element) it.next();
			String since = navigation.getAttribute("since");
			int dontShowFirst = 0;
			try {
				dontShowFirst = new Integer(navigation.getAttribute("dontShowFirst")).intValue();
			} catch (Exception exe) {
			}

			if (sm.equals("")) {
				sm = "<navigationBackward>" + webSpringBean.getNavigationBackwardXml(viewComponentId, since, dontShowFirst, iAmTheLiveserver) + "</navigationBackward>";
			}
			try {
				Document smdoc = XercesHelper.string2Dom(sm);
				Node page = doc.importNode(smdoc.getFirstChild(), true);
				Node parent = navigation.getParentNode();
				parent.replaceChild(page, navigation);
			} catch (Exception exe) {
				log.error("An error occured", exe);
			}
		}
	}

	private void fillNavigation(Document doc) throws Exception {
		if (log.isDebugEnabled()) log.debug("fillNavigation entered.");
		doc.createElement("//navigation");
		String navigationXml = "";
		Element navigation = doc.getElementById("//navigation");
		if (log.isDebugEnabled() && navigation != null) log.debug("found navigation element");
		String since = navigation.getAttribute("since");
		int depth = -1;
		try {
			depth = new Integer(navigation.getAttribute("depth")).intValue();
		} catch (Exception exe) {
		}
		int ifDistanceToNavigationRoot = -1;
		try {
			ifDistanceToNavigationRoot = new Integer(navigation.getAttribute("ifDistanceToNavigationRoot")).intValue();
			if (log.isDebugEnabled()) log.debug("GOT ifDistanceToNavigationRoot");
		} catch (Exception exe) {
		}
		boolean showOnlyAuthorized = false;
		try {
			showOnlyAuthorized = Boolean.valueOf(navigation.getAttribute("showOnlyAuthorized")).booleanValue();
			if (log.isDebugEnabled()) log.debug("showOnlyAuthorized: " + showOnlyAuthorized);
		} catch (Exception e) {
		}

		try {
			if (this.unitValue != null) {
				try {
					navigation.setAttribute("unitImageId", this.unitValue.getImageId().toString());
				} catch (Exception exe) {
				}
				try {
					navigation.setAttribute("unitLogoId", this.unitValue.getLogoId().toString());
				} catch (Exception exe) {
				}
				try {
					Document docUnitInfoXml = XercesHelper.string2Dom(this.webSpringBean.getUnitInfoXml(this.unitValue.getUnitId()));
					Node page = doc.importNode(docUnitInfoXml.getDocumentElement(), true);
					navigation.appendChild(page);
				} catch (Exception e) {
					if (log.isDebugEnabled()) log.debug(e.getMessage(), e);
				}
			}
		} catch (Exception exe) {
		}

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
					navigation.appendChild(page);
				} catch (Exception exe) {
					log.error("An error occured", exe);
				}
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
