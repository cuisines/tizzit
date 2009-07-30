/**
 * Copyright (c) 2009 Juwi MacMillan Group GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.juwimm.cms.cocoon.generation;

import java.io.Serializable;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.avalon.excalibur.pool.Poolable;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.Composable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.ResourceNotFoundException;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.components.source.SourceUtil;
import org.apache.cocoon.environment.*;
import org.apache.cocoon.generation.AbstractGenerator;
import org.apache.cocoon.webapps.session.ContextManager;
import org.apache.cocoon.webapps.session.context.SessionContext;
import org.apache.cocoon.xml.dom.DOMStreamer;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.TimeStampValidity;
import org.apache.log4j.Logger;
import org.springframework.aop.target.CommonsPoolTargetSource;
import org.w3c.dom.*;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.juwimm.cms.beans.WebServiceSpring;
import de.juwimm.cms.beans.cocoon.ModifiedDateContentHandler;
import de.juwimm.cms.beans.cocoon.PluginCacheAccessor;
import de.juwimm.cms.cocoon.generation.helper.PluginContentHandler;
import de.juwimm.cms.cocoon.generation.helper.RequestImpl;
import de.juwimm.cms.cocoon.generation.helper.ResponseImpl;
import de.juwimm.cms.cocoon.helper.CocoonSpringHelper;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.components.vo.*;
import de.juwimm.cms.search.beans.SearchengineService;
import de.juwimm.cms.search.vo.SearchResultValue;
import de.juwimm.cms.search.vo.WebSearchValue;
import de.juwimm.cms.search.vo.XmlSearchValue;
import de.juwimm.cms.vo.SiteValue;
import de.juwimm.cms.vo.UnitValue;
import de.juwimm.cms.vo.ViewComponentValue;
import de.juwimm.cms.vo.ViewDocumentValue;
import de.juwimm.util.Base64;
import de.juwimm.util.XercesHelper;

/**
 * The ContentGenerator is the main interface between the core content management system and the Cocoon presentation layer.<br/>
 * Actually it has a lot of functionality like parsing the content after returning it from the database.
 * <p><h5>Configuration:</h5>
 * <pre>
 * &lt;map:generator name="CmsContent" pool-grow="2" pool-max="128" pool-min="4" src="de.juwimm.cms.cocoon.generation.CmsContentGenerator"/&gt;
 * </pre>
 * </p>
 * <p><h5>Usage:</h5>
 * Just put code like this in your pipeline:
 * <pre>
 * &lt;map:generate type="CmsContent"&gt;
 *    &lt;map:parameter name="parameters" value="true" /&gt;
 *    &lt;map:parameter name="viewComponentId" value="{viewComponentId}" /&gt;
 *    &lt;map:parameter name="liveserver" value="{system-property:cqLiveserver}" /&gt;
 *    &lt;map:parameter name="disableMeta" value="false" /&gt;
 *    &lt;map:parameter name="disableAuthentication" value="false" /&gt;
 *    &lt;map:parameter name="disableHeadLine" value="false" /&gt;
 *    &lt;map:parameter name="disableAggregations" value="false" /&gt;
 *    &lt;map:parameter name="disableMembersList" value="true" /&gt;
 *    &lt;map:parameter name="disableUnitList" value="true" /&gt;
 *    &lt;map:parameter name="disableInternalLinks" value="false" /&gt;
 *    &lt;map:parameter name="disableUnitInformation" value="true" /&gt;
 *    &lt;map:parameter name="disableFulltext" value="true" /&gt;
 *    &lt;map:parameter name="disableNavigation" value="true" /&gt;
 *    &lt;map:parameter name="disableNavigationAxis" value="true" /&gt;
 *    &lt;map:parameter name="disableNavigationBackward" value="true" /&gt;
 *    &lt;map:parameter name="disableLanguageVersions" value="true" /&gt;
 *    &lt;map:parameter name="disableUsedDocumentsSearch" value="true" /&gt;
 *    &lt;map:parameter name="disableContentInclude" value="false" /&gt;
 *    &lt;map:parameter name="disableHtmlSearch" value="false" /&gt;
 *    &lt;map:parameter name="disableLastModifiedPages" value="true" /&gt;
 * &lt;/map:generate&gt;
 * </pre>
 * </p>
 * <p><h5>Parameters:</h5>
 * This generator can be widely configured to specify the amount of output. Some options are quite expensive, hence an appropriate configuration is very important!<br/>&nbsp;<br/>
 * <ul>
 * <li><strong>viewComponentId</strong> - the primary-key of the page to be fetched and returned, determined from the request by the CmsTemplateAction</li>
 * <li><strong>liveserver</strong> - default: false (Workserver-Preview), switch the ContentVersion of a page, if true the published version is returned, if false the newest ContentVersion</li>
 * <li><strong>disableMeta</strong> - default: false, appends &quot;description&quot;, &quot;keywords&quot; and &quot;LastUpdated&quot; to the page-data</li>
 * <li><strong>disableAuthentication</strong> - default: false, appends authentication-data from the current session (ID, roles, units, sites) to the page-data</li>
 * <li><strong>disableHeadLine</strong> - default: false, appends the headline from the ContentVersion to the page-data</li>
 * <li><strong>disableAggregations</strong> - default: false, appends embedded components (persons, addresses, units, departments, talktimes) to the page-data</li>
 * <li><strong>disableMembersList</strong> - default: false<p>if the content of the page contains the tags &quot;unitMembersList&quot; or &quot;memberslist&quot;,<br/>
 * some persons are added depending on three attributes of the tag: unitId, surname and lastname. unitId limits the persons returned to member of this unit, surname and lastname search for persons (in this unit) matching the patterns given.<br/>
 * If unitId = &quot;all&quot; all persons of the whole site are added (depending on the search-patterns).</p></li>
 * <li><strong>disableUnitList</strong> - default: false, appends a list of all units belonging to the current site to the page-data</li>
 * <li><strong>disableInternalLinks</strong> - default: false, controls if InternalLinks in the content of this page should be resolved, i.e. are expanded with their url and language to create a hyperlink on the webpage</li>
 * <li><strong>disableUnitInformation</strong> - default: false, if the content of the page contains the tag &quot;unitInformation&quot; the generator appends the name and url for the current unit or a given unitId-attribute</li>
 * <li><strong>disableFulltext</strong> - default: false<p>
 * if the content of the page contains the tag &quot;fulltextsearch&quot; the generator performs a xpath-search in the xml-content of all pages for this 
 * site containing a node with the name of the attribute &quot;nodename&quot; and appends the whole content of the pages found to the page-data. The search can be limited to the current unit by setting the attribute &quot;searchOnlyInThisUnit&quot; to true</p></li>
 * <li><strong>disableNavigation</strong> - default: false, </li>
 * <li><strong>disableNavigationAxis</strong> - default: false, </li>
 * <li><strong>disableNavigationBackward</strong> - default: false, </li>
 * <li><strong>disableLanguageVersions</strong> - default: true, </li>
 * <li><strong>disableUsedDocumentsSearch</strong> - default: true, search only returns documents that are linked on any webpage</li>
 * <li><strong>disableContentInclude</strong> - default: true, </li>
 * <li><strong>disableHtmlSearch</strong> - default: false, if conquest-searchquery or cqWebSearchQuery exist in request, a html-search is performed, <a href="#disableHtmlSearch">explanation</a></li>
 * <li><strong>disableLastModifiedPages</strong> - default: true, appends a list of the last-modified pages, <a href="#disableLastModifiedPages">explanation</a></li>
 * </ul>
 * </p>
 * <p>
 * <h5>Explanation of Generator-Methods:</h5>
 * <p><a name="disableLastModifiedPages"/><strong><u>disableLastModifiedPages</u></strong><br/>If the content contains the tag &quot;lastModifiedPages&quot;</p>
 * </p>
 * <p><a name="disableHtmlSearch"/><strong><u>disableHtmlSearch</u></strong><br/>If the request contains the parameter &quot;cqWebSearchQuery&quot;</p>
 * </p>
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @author <a href="carsten.schalm@juwimm.com">Carsten Schalm</a>
 * Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class CmsContentGenerator extends AbstractGenerator implements CacheableProcessingComponent, Poolable, Composable {
	private static Logger log = Logger.getLogger(CmsContentGenerator.class);
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
	private Parameters par = null;
	private SessionContext sessContext = null;
	private ComponentManager manager = null;
	private ContextManager cm = null;
	private boolean iAmTheLiveserver = false;
	private boolean disableMeta = false;
	private boolean disableAuthentication = false;
	private boolean disableHeadLine = false;
	private boolean disableAggregations = false;
	private boolean disableMembersList = false;
	private boolean disableUnitList = false;
	private boolean disableInternalLinks = false;
	private boolean disableUnitInformation = false;
	private boolean disableFulltext = false;
	private boolean disableNavigation = false;
	private boolean disableNavigationAxis = false;
	private boolean disableNavigationBackward = false;
	private boolean disableLanguageVersions = true; // we need only in menu.xsl, UKD
	private boolean disableUsedDocumentsSearch = true;
	private boolean disableContentInclude = true;
	private boolean disableHtmlSearch = false;
	private boolean disableLastModifiedPages = true;
	private Serializable uniqueKey;
	private Request request = null;
	private Response response = null; 
	private String webSearchquery = null;
	private long chgDate = 0;
	private SAXParser parser = null;
	private PluginCacheAccessor pluginCache = null;
	private String requestUrl = null;
	private Map<String, String> safeguardMap = null;
	private Map<Integer, String> path4ViewComponentCacheMap = new HashMap<Integer, String>();

	public void compose(ComponentManager componentManager) {
		if (log.isDebugEnabled()) log.debug("begin compose");
		this.manager = componentManager;

		if (log.isDebugEnabled()) log.debug("instanciating new SAX Parser");
		try {
			SAXParserFactory fac = SAXParserFactory.newInstance();
			fac.setNamespaceAware(true);
			parser = fac.newSAXParser();
		} catch (ParserConfigurationException exe) {
			log.error("an unknown error occured", exe);
		} catch (SAXException exe) {
			log.error("an unknown error occured", exe);
		}
		if (log.isDebugEnabled()) log.debug("end compose");
	}

	@Override
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters parameters) {
		if (log.isDebugEnabled()) log.debug("begin setup with src: " + src);
		this.requestUrl = src;
		this.par = parameters;

		pluginCache = (PluginCacheAccessor) CocoonSpringHelper.getBean(objectModel, CocoonSpringHelper.PLUGIN_CACHE_ACCESSOR);
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
			super.setup(resolver, objectModel, src, parameters);
			if (src != null) {
				try {
					this.inputSource = super.resolver.resolveURI(src);
				} catch (SourceException se) {
					throw SourceUtil.handle("Error during resolving of '" + src + "'.", se);
				}
			} else {
				this.inputSource = null;
			}
			viewComponentId = new Integer(parameters.getParameter("viewComponentId"));
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
			
			Session session = this.request.getSession(true);
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
			try {
				webSearchquery = request.getParameter("conquest-searchquery");
				if(webSearchquery == null)
					webSearchquery = this.request.getParameter("cqWebSearchQuery");
			} catch (Exception exe) {
			}

			cm = (ContextManager) this.manager.lookup(ContextManager.ROLE);
			sessContext = cm.getContext("authentication");
		} catch (Exception exe) {
			viewComponentId = null;
		}
		try {
			disableMeta = new Boolean(parameters.getParameter("disableMeta")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableAuthentication = new Boolean(parameters.getParameter("disableAuthentication")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableHeadLine = new Boolean(parameters.getParameter("disableHeadLine")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableAggregations = new Boolean(parameters.getParameter("disableAggregations")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableMembersList = new Boolean(parameters.getParameter("disableMembersList")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableUnitList = new Boolean(parameters.getParameter("disableUnitList")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableInternalLinks = new Boolean(parameters.getParameter("disableInternalLinks")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableUnitInformation = new Boolean(parameters.getParameter("disableUnitInformation")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableFulltext = new Boolean(parameters.getParameter("disableFulltext")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableNavigation = new Boolean(parameters.getParameter("disableNavigation")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableNavigationAxis = new Boolean(parameters.getParameter("disableNavigationAxis")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableNavigationBackward = new Boolean(parameters.getParameter("disableNavigationBackward")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableLanguageVersions = new Boolean(parameters.getParameter("disableLanguageVersions")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableUsedDocumentsSearch = new Boolean(parameters.getParameter("disableUsedDocumentsSearch")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableContentInclude = new Boolean(parameters.getParameter("disableContentInclude")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableHtmlSearch = new Boolean(parameters.getParameter("disableHtmlSearch")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			disableLastModifiedPages = new Boolean(parameters.getParameter("disableLastModifiedPages")).booleanValue();
		} catch (Exception exe) {
		}
		try {
			iAmTheLiveserver = new Boolean(parameters.getParameter("liveserver")).booleanValue();
		} catch (Exception exe) {
		}
		if (log.isDebugEnabled()) log.debug("end setup");
	}

	@Override
	public void recycle() {
		if (log.isDebugEnabled()) log.debug("begin recycle");
		if (null != this.inputSource) {
			super.resolver.release(this.inputSource);
			this.inputSource = null;
		}
		super.recycle();
		disableMeta = false;
		disableHeadLine = false;
		disableAggregations = false;
		disableMembersList = false;
		disableUnitList = false;
		disableInternalLinks = false;
		disableUnitInformation = false;
		disableFulltext = false;
		disableNavigation = false;
		disableNavigationAxis = false;
		disableNavigationBackward = false;
		disableLanguageVersions = true;
		disableAuthentication = false;
		disableUsedDocumentsSearch = true;
		disableContentInclude = true;
		disableHtmlSearch = false;
		disableLastModifiedPages = true; 
		webSearchquery = null;
		request = null;
		response = null;
		chgDate = 0;
		if (log.isDebugEnabled()) log.debug("end recycle");
	}

	public void generate() throws SAXException, ProcessingException {
		if (log.isDebugEnabled()) log.debug("begin generate");
		if (viewComponentId == null) { throw new ResourceNotFoundException("Could not find " + request.getRequestURI()); }

		Document doc = null;
		InputSource in = null;

		if (this.inputSource != null) {
			if (log.isDebugEnabled()) log.debug("loading xml from file");
			try {
				in = new InputSource(this.inputSource.getInputStream());
			} catch (Exception exe) {
			}
		} else {
			if (log.isDebugEnabled()) log.debug("loading xml from database");
			try {
				//	String content = webService.getContent(viewComponentId, iAmTheLiveserver);
				if (cacheLogger.isDebugEnabled()) {
					cacheLogger.debug("generating content at " + sdf.format(new Date()));
				}
				String content = webSpringBean.getContent(viewComponentId, iAmTheLiveserver);
				//	enterlogger.debug(content);
				in = new InputSource(new StringReader(content));
			} catch (Exception exe) {
				log.warn("Error getting content for \"" + this.requestUrl + "\": " + exe.getMessage(), exe);
			}
		}
		if (in == null) { throw new ResourceNotFoundException("Could not find resource with "); }
		try {
			doc = XercesHelper.inputSource2Dom(in);
		} catch (Exception exe) {
			throw new ProcessingException("Error parsing the content", exe);
		}

		try {
			if (!disableContentInclude) {
				this.fillContentInclude(doc);
				this.fillTeaserInclude(doc);
			}
			if (!disableUnitInformation) // has to be before fulltext, because fulltext will fill its own
				this.fillUnitInformation(doc, null);
			if (!disableFulltext) // has to be at the beginning, because it can contain other tags as well
				this.fillFulltext(doc);
			if (!disableMeta) this.fillMeta(doc);
			if (!disableAuthentication) // hast to be after unitInformation, because it will put in some units maybe
				this.fillAuthentication(doc);
			if (!disableHeadLine) this.fillHeadLine(doc);
			if (!disableAggregations) this.fillAggregations(doc);
			if (!disableMembersList) this.fillMembersList(doc);
			if (!disableUnitList) // im filling also unitInformations
				this.fillUnitList(doc);
			if (!disableInternalLinks) this.solveInternalLinks(doc);
			if (!disableNavigation) this.fillNavigation(doc);
			if (!disableLastModifiedPages) this.fillLastModifiedPages(doc);
			if (!disableNavigationBackward) this.fillNavigationBackward(doc);
			if (!disableLanguageVersions) this.fillLanguageVersions(doc);
			if (!disableHtmlSearch && this.webSearchquery != null && this.webSearchquery.length() > 0) {
				this.fillWebSearch(doc);
			}
		} catch (Exception e) {
			String errMsg = "An error occured while processing the content";
			log.error(errMsg, e);
			throw new ProcessingException(errMsg, e);
		}
		if (doc != null && contentHandler != null) {
			if (log.isDebugEnabled()) log.debug("start streaming to sax");
			try {
				ContentHandler contentHandlerWrapper = new PluginContentHandler(pluginCache, contentHandler, new RequestImpl(request), new ResponseImpl(response), viewComponentId, siteValue.getSiteId());
				contentHandlerWrapper.startDocument();
				DOMStreamer ds = new DOMStreamer(contentHandlerWrapper);
				ds.stream(doc.getDocumentElement());
				contentHandlerWrapper.endDocument();
				try {
					((CommonsPoolTargetSource) CocoonSpringHelper.getBean(objectModel, "pluginCacheAccessorPool")).releaseTarget(pluginCache);
				} catch (Exception exe) {
					log.error("an unknown error occured", exe);
				}
			} catch (Exception exe) {
				log.error("An error occured", exe);
			}
		}
		/* just for debugging...
		try {
			log.info(CmsContentGenerator.sdf.format(webSpringBean.getNavigationAge(viewComponentId, "root", 12, iAmTheLiveserver)));
		} catch (UserException e) {
			log.error(e.getMessage(), e);
		}
		*/
		if (log.isDebugEnabled()) log.debug("end generate");
	}
	
	private void fillWebSearch(Document doc) throws Exception {
		Node head = XercesHelper.findNode(doc, "//source/head");
		if (head != null) {
			Integer pageSize = null;
			try {
				pageSize = Integer.valueOf(this.request.getParameter("cqSearchPageSize"));
			} catch (Exception exe) {
				if (log.isDebugEnabled()) log.debug("No cqSearchPageSize? " + exe.getMessage());
			}
			Integer pageNumber = null;
			try {
				pageNumber = Integer.valueOf(this.request.getParameter("cqSearchPageNumber"));
			} catch (Exception exe) {
				if (log.isDebugEnabled()) log.debug("No cqSearchPageNumber? " + exe.getMessage());
			}
			String urlSearch = null;
			try {
				urlSearch = this.request.getParameter("cqUrlSearch");
			} catch (Exception exe) {
			}

			ArrayList<SearchResultValue> resultList = new ArrayList<SearchResultValue>();
			SearchResultValue[] results = null;
			if(urlSearch == null) {
				results = searchengineService.searchWeb(this.siteValue.getSiteId(), this.webSearchquery, pageSize, pageNumber);
			} else {
				results = searchengineService.searchWeb(this.siteValue.getSiteId(), this.webSearchquery, pageSize, pageNumber, urlSearch);
			}
			
			if (results != null) {
				resultList.addAll(Arrays.asList(results));
				Integer totalHits = null;
				pageSize = null;
				Integer pageAmount = null;
				pageNumber = null;
				Long duration = null;
				if (resultList.size() > 0) {
					SearchResultValue first = resultList.get(0);
					totalHits = first.getTotalHits();
					pageSize = first.getPageSize();
					pageAmount = first.getPageAmount();
					pageNumber = first.getPageNumber();
					duration = first.getDuration();
				}
				if (log.isDebugEnabled()) log.debug("got " + totalHits + " total search-results, returning " + resultList.size());
				Element searchresults = doc.createElement("searchresults");
				if (totalHits == null) totalHits = new Integer(0);
				searchresults.setAttribute("totalCount", totalHits.toString());
				if (pageAmount == null) pageAmount = new Integer(0);
				searchresults.setAttribute("pageAmount", pageAmount.toString());
				if (pageSize == null) pageSize = new Integer(0);
				searchresults.setAttribute("pageSize", pageSize.toString());
				if (pageNumber == null) pageNumber = new Integer(0);
				searchresults.setAttribute("pageNumber", pageNumber.toString());
				if (duration == null) duration = new Long(0L);
				searchresults.setAttribute("duration", duration.toString());
				searchresults.setAttribute("count", Integer.toString(resultList.size()));
				head.appendChild(searchresults);
				Iterator<SearchResultValue> it = resultList.iterator();
				while (it.hasNext()) {
					Element result = doc.createElement("result");
					this.appendWebSearchXml(doc, result, it.next());
					searchresults.appendChild(result);
				}
			} else {
				if (log.isDebugEnabled()) log.debug("got NO search-results!");
			}
		}
		if (log.isDebugEnabled()) log.debug("finished fillWebSearch");
	}

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
		boolean ifOnlyUnit = true;
		Integer myUnitId = null;
		Iterator itFulltext = XercesHelper.findNodes(doc, "//fulltextsearch");
		while (itFulltext.hasNext()) {
			Element fulltextsearch = (Element) itFulltext.next();
			ifOnlyUnit = Boolean.valueOf(fulltextsearch.getAttribute("searchOnlyInThisUnit")).booleanValue();
			if (ifOnlyUnit && myUnitId == null) {
				myUnitId = viewComponentValue.getUnitId();
			}
			String xpath = "//" + fulltextsearch.getAttribute("nodename").trim();
			if (log.isDebugEnabled()) log.debug("STARTING FULLTEXT with XPATH: " + xpath);

			XmlSearchValue[] foundArr = searchengineService.searchXML(siteValue.getSiteId(), xpath);

			if (foundArr != null) {
				if (log.isDebugEnabled()) log.debug("GOT FULLTEXT RETURN WITH " + foundArr.length + " ITEMS");
				for (int i = 0; i < foundArr.length; i++) {
					Integer foundUnitId = new Integer(0);
					try {
						foundUnitId = foundArr[i].getUnitId();
					} catch (Exception exe) {
						log.debug("Cannot catch unitId: " + foundArr[i].getUnitId());
					}
					if ((ifOnlyUnit && foundUnitId.equals(myUnitId)) || !ifOnlyUnit) {
						String foundContent = foundArr[i].getContent();
						//String foundInfoText = foundArr[i][1];
						//String foundText = foundArr[i][2];

						if (foundContent != null && !foundContent.equalsIgnoreCase("")) {
							Document docContent = XercesHelper.string2Dom(foundContent);
							Node newNode = doc.importNode(docContent.getFirstChild(), true);
							fulltextsearch.appendChild(newNode);

							Integer foundVcId = null;
							try {
								foundVcId = foundArr[i].getViewComponentId();
							} catch (Exception exe) {
								log.warn("fillFulltext: Could not find vcId: " + foundArr[i].getViewComponentId());
							}
							if (foundVcId != null) {
								ViewComponentValue foundVc = null;
								try {
									foundVc = webSpringBean.getViewComponent4Id(foundVcId);
								} catch (Exception e) {
									if (log.isDebugEnabled()) log.debug("Can't find viewComponentId " + foundVcId + "!\n" + e.getMessage());
								}
								if (foundVc != null) this.fillUnitInformation(newNode, foundVc);
							}
						}
					}
				}
			}
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

	private void fillAuthentication(Document doc) throws Exception {
		Node head = XercesHelper.findNode(doc, "//source/head");
		if (head != null) {
			// If no context then user has not logged on
			if (sessContext == null) {
				if (log.isDebugEnabled()) log.debug("No Logged-in User");
			} else {
				Node authentication = doc.createElement("authentication");
				try {
					Node idN = sessContext.getXML("/authentication/ID");
					Node id = doc.createElement("id");
					id.appendChild(doc.importNode(idN, true));
					authentication.appendChild(id);
				} catch (Exception exe) {
					log.error("An error occured", exe);
				}
				DocumentFragment df = sessContext.getXML("/authentication/roles");
				if (df != null) {
					NodeList nl = df.getChildNodes();
					for (int i = 0; i < nl.getLength(); i++) {
						Node role = nl.item(i);
						if (XercesHelper.getNodeValue(role).startsWith("unit_")) {
							Element unitInformation = doc.createElement("unitInformation");
							StringTokenizer st = new StringTokenizer(XercesHelper.getNodeValue(role), "_");
							if (st.hasMoreTokens()) {
								// skip "unit"
								st.nextToken();
								if (st.hasMoreTokens()) {
									String unitId = st.nextToken();
									unitInformation.setAttribute("id", unitId);
								}
								if (st.hasMoreTokens()) {
									String unitName = st.nextToken();
									Element unitNameElem = doc.createElement("unitname");
									CDATASection cData = doc.createCDATASection(Base64.decodeToString(unitName));
									unitNameElem.appendChild(cData);
									unitInformation.appendChild(unitNameElem);
								}
								if (st.hasMoreTokens()) {
									// skip "site"
									st.nextToken();
									if (st.hasMoreTokens()) {
										String siteId = st.nextToken();
										unitInformation.setAttribute("siteId", siteId);
									}
								}
							}
							authentication.appendChild(unitInformation);
						} else if (XercesHelper.getNodeValue(role).startsWith("group_")) {
							Element groupInformation = doc.createElement("groupInformation");
							StringTokenizer st = new StringTokenizer(XercesHelper.getNodeValue(role), "_");
							if (st.hasMoreTokens()) {
								// skip "group"
								st.nextToken();
								if (st.hasMoreTokens()) {
									String groupId = st.nextToken();
									groupInformation.setAttribute("id", groupId);
								}
								if (st.hasMoreTokens()) {
									String groupName = st.nextToken();
									Element groupNameElem = doc.createElement("groupname");
									CDATASection cData = doc.createCDATASection(Base64.decodeToString(groupName));
									groupNameElem.appendChild(cData);
									groupInformation.appendChild(groupNameElem);
								}
								if (st.hasMoreTokens()) {
									// skip "site"
									st.nextToken();
									if (st.hasMoreTokens()) {
										String siteId = st.nextToken();
										groupInformation.setAttribute("siteId", siteId);
									}
								}
							}
							authentication.appendChild(groupInformation);
						} else if (XercesHelper.getNodeValue(role).startsWith("site_")) {
							Element siteInformation = doc.createElement("siteInformation");
							StringTokenizer st = new StringTokenizer(XercesHelper.getNodeValue(role), "_");
							if (st.hasMoreTokens()) {
								// skip "site"
								st.nextToken();
								if (st.hasMoreTokens()) {
									String siteId = st.nextToken();
									siteInformation.setAttribute("id", siteId);
								}
								if (st.hasMoreTokens()) {
									String siteName = st.nextToken();
									Element siteNameElem = doc.createElement("sitename");
									CDATASection cData = doc.createCDATASection(Base64.decodeToString(siteName));
									siteNameElem.appendChild(cData);
									siteInformation.appendChild(siteNameElem);
								}
								if (st.hasMoreTokens()) {
									String siteShort = st.nextToken();
									Element siteShortElem = doc.createElement("siteshort");
									CDATASection cData = doc.createCDATASection(Base64.decodeToString(siteShort));
									siteShortElem.appendChild(cData);
									siteInformation.appendChild(siteShortElem);
								}
							}
							authentication.appendChild(siteInformation);
						} else if (XercesHelper.getNodeValue(role).startsWith("role_")) {
							Element roleInformation = doc.createElement("roleInformation");
							StringTokenizer st = new StringTokenizer(XercesHelper.getNodeValue(role), "_");
							if (st.hasMoreTokens()) {
								// skip "role"
								st.nextToken();
								if (st.hasMoreTokens()) {
									String roleId = st.nextToken();
									roleInformation.setAttribute("id", roleId);
								}
								if (st.hasMoreTokens()) {
									// skip "site"
									// trash = st.nextToken();
									if (st.hasMoreTokens()) {
										String siteId = st.nextToken();
										roleInformation.setAttribute("siteId", siteId);
									}
								}
							}
							authentication.appendChild(roleInformation);
						} else {
							role = doc.importNode(role, true);
							authentication.appendChild(role);
						}
					}
				}
				head.appendChild(authentication);
			}
		}
	}

	private void fillHeadLine(Document doc) throws Exception {
		Iterator it = XercesHelper.findNodes(doc, "//headLine");
		Integer contentId = null;

		while (it.hasNext()) {
			Element headLine = (Element) it.next();
			try {
				if (contentId == null) {
					if (viewComponentValue.getViewType() == Constants.VIEW_TYPE_SYMLINK) {
						ViewComponentValue vclSym = webSpringBean.getViewComponent4Id(new Integer(viewComponentValue.getReference()));
						contentId = new Integer(vclSym.getReference());
					} else {
						contentId = new Integer(viewComponentValue.getReference());
					}
				}
				try {
					Node txtNde = doc.createTextNode(webSpringBean.getHeading(contentId, iAmTheLiveserver));
					headLine.appendChild(txtNde);
				} catch (Exception exe) {
				}
			} catch (NumberFormatException nfe) {
			}
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
		Iterator it = XercesHelper.findNodes(doc, "//navigation");
		String navigationXml = "";

		while (it.hasNext()) {
			if (log.isDebugEnabled()) log.debug("found Navigation");
			Element navigation = (Element) it.next();
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
	}

	private void fillLastModifiedPages(Document doc) throws Exception {
		Iterator it = XercesHelper.findNodes(doc, "//lastModifiedPages");
		String lastModifiedPagesXml = "";

		while (it.hasNext()) {
			if (log.isDebugEnabled()) log.debug("found lastModifiedPages");
			Element lastModifiedPages = (Element) it.next();
			int number = 10;
			try {
				number = new Integer(lastModifiedPages.getAttribute("number")).intValue();
			} catch (Exception e) {
			}
			boolean showOnlyAuthorized = false;
			try {
				showOnlyAuthorized = Boolean.valueOf(lastModifiedPages.getAttribute("showOnlyAuthorized")).booleanValue();
				if (log.isDebugEnabled()) log.debug("showOnlyAuthorized: " + showOnlyAuthorized);
			} catch (Exception e) {
			}
			Integer unitId = null;
			try {
				unitId = new Integer(lastModifiedPages.getAttribute("unitId"));
				if (log.isDebugEnabled()) log.debug("unitId: " + unitId);
			} catch (Exception e) {
			}
			/*
			try {
				if (this.unitValue != null) {
					try {
						lastModifiedPages.setAttribute("unitImageId", this.unitValue.getImageId().toString());
					} catch (Exception exe) {
					}
					try {
						lastModifiedPages.setAttribute("unitLogoId", this.unitValue.getLogoId().toString());
					} catch (Exception exe) {
					}
					try {
						Document docUnitInfoXml = XercesHelper.string2Dom(this.webSpringBean.getUnitInfoXml(this.unitValue.getUnitId()));
						Node page = doc.importNode(docUnitInfoXml.getDocumentElement(), true);
						lastModifiedPages.appendChild(page);
					} catch (Exception e) {
						if (log.isDebugEnabled()) log.debug(e.getMessage(), e);
					}
				}
			} catch (Exception exe) {
			}
			*/
			lastModifiedPagesXml = webSpringBean.getLastModifiedPages(this.viewComponentId, unitId, number, showOnlyAuthorized);
			if (lastModifiedPagesXml != null && !"".equalsIgnoreCase(lastModifiedPagesXml)) {
				try {
					Document docLastModifiedPagesXml = XercesHelper.string2Dom(lastModifiedPagesXml);
					// filter safeGuard
					if (showOnlyAuthorized) {
						try {
							String filteredLastModifiedPagesXml = this.webSpringBean.filterNavigation(lastModifiedPagesXml, safeguardMap);
							if (log.isDebugEnabled()) {
								log.debug("allLastModifiedPagesXml\n" + lastModifiedPagesXml);
								log.debug("filteredLastModifiedPagesXml\n" + filteredLastModifiedPagesXml);
							}
							docLastModifiedPagesXml = XercesHelper.string2Dom(filteredLastModifiedPagesXml);
						} catch (Exception e) {
							log.error("Error filtering LastModifiedPages with SafeGuard: " + e.getMessage(), e);
						}
					}
					// Insert lastModifiedPagesXml -> sitemap
					Iterator vcIt = XercesHelper.findNodes(docLastModifiedPagesXml, "//viewcomponent");
					while (vcIt.hasNext()) {
						Node vcNode = (Node) vcIt.next();
						Node page = doc.importNode(vcNode, true);
						lastModifiedPages.appendChild(page);
					}
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

	private void fillUnitInformation(Node nde, ViewComponentValue vcl) throws Exception {
		ViewComponentValue myVcl = null;
		if (vcl != null) {
			myVcl = vcl;
		} else {
			myVcl = viewComponentValue;
		}
		Iterator it = XercesHelper.findNodes(nde, ".//unitInformation");

		while (it.hasNext()) {
			Element el = (Element) it.next();
			String strUn = el.getAttribute("unitId");

			UnitValue uv = null;
			if (strUn != null && !strUn.equals("")) {
				uv = webSpringBean.getUnit(new Integer(strUn));
			} else {
				uv = webSpringBean.getUnit4ViewComponent(myVcl.getViewComponentId());
				el.setAttribute("unitId", uv.getUnitId().toString());
			}

			Integer viewDocumentId = webSpringBean.getViewDocument4ViewComponentId(myVcl.getViewComponentId()).getViewDocumentId();
			try {
				String unitPath = webSpringBean.getPath4Unit(uv.getUnitId(), viewDocumentId);
				el.setAttribute("url", unitPath);
			} catch (Exception exe) {
			}

			el.setAttribute("unitName", uv.getName());
		}
	}

	private void fillAggregations(Document doc) throws Exception {
		Iterator it = XercesHelper.findNodes(doc, "//aggregation/include");
		while (it.hasNext()) {
			Node node = (Node) it.next();
			try {
				this.fillComponent(doc, node);
			} catch (Exception exe) {
				log.warn("Error filling Aggregations: " + exe.getMessage() + "\nin: " + XercesHelper.node2string(node) + " (\"" + this.getPath4CurrentRequest() + "\")");
			}
		}
	}

	private void fillComponent(Document doc, Node ndeInclude) throws Exception {
		String type = ndeInclude.getAttributes().getNamedItem("type").getNodeValue();
		Long id = new Long(ndeInclude.getAttributes().getNamedItem("id").getNodeValue());

		if (type.equals("person")) {
			this.fillPerson(id, doc, ndeInclude);
		} else if (type.equals("address")) {
			this.fillAddress(id, doc, ndeInclude);
		} else if (type.equals("unit")) {
			this.fillUnit(new Integer(id.toString()), doc, ndeInclude);
		} else if (type.equals("department")) {
			this.fillDepartment(id, doc, ndeInclude);
		} else if (type.equals("talkTime")) {
			this.fillTalkTime(id, doc, ndeInclude);
		}
	}

	private void fillPerson(Long id, Document doc, Node ndeInclude) {
		PersonValue person = null;
		try {
			person = webSpringBean.getPerson(id);
		} catch (Exception exe) {
		}

		if (person != null) {
			try {
				((Element) ndeInclude).setAttribute("sortOrder", Integer.toString(person.getPosition()));
			} catch (Exception e) {
				log.warn("Error getting sortOrder for person " + person.getPersonId());
			}
			Iterator it = XercesHelper.findNodes(ndeInclude, "./content/*");
			while (it.hasNext()) {
				try {
					Node ndeAttribute = (Node) it.next();
					if (ndeAttribute.getNodeName().equals("firstname")) {
						if (person.getFirstname() != null) ndeAttribute.appendChild(doc.createTextNode(person.getFirstname()));
					} else if (ndeAttribute.getNodeName().equals("lastname")) {
						if (person.getLastname() != null) ndeAttribute.appendChild(doc.createTextNode(person.getLastname()));
					} else if (ndeAttribute.getNodeName().equals("birthDay")) {
						if (person.getBirthDay() != null) ndeAttribute.appendChild(doc.createTextNode(person.getBirthDay()));
					} else if (ndeAttribute.getNodeName().equals("salutation")) {
						if (person.getSalutation() != null) ndeAttribute.appendChild(doc.createTextNode(person.getSalutation()));
					} else if (ndeAttribute.getNodeName().equals("title")) {
						if (person.getTitle() != null) ndeAttribute.appendChild(doc.createTextNode(person.getTitle()));
					} else if (ndeAttribute.getNodeName().equals("job")) {
						if (person.getJob() != null) ndeAttribute.appendChild(doc.createTextNode(person.getJob()));
					} else if (ndeAttribute.getNodeName().equals("jobTitle")) {
						if (person.getJobTitle() != null) ndeAttribute.appendChild(doc.createTextNode(person.getJobTitle()));
					} else if (ndeAttribute.getNodeName().equals("medicalAssociation")) {
						if (person.getMedicalAssociation() != null) ndeAttribute.appendChild(doc.createTextNode(person.getMedicalAssociation()));
					} else if (ndeAttribute.getNodeName().equals("linkMedicalAssociation")) {
						if (person.getLinkMedicalAssociation() != null) ndeAttribute.appendChild(doc.createTextNode(person.getLinkMedicalAssociation()));
					} else if (ndeAttribute.getNodeName().equals("countryJob")) {
						if (person.getCountryJob() != null) ndeAttribute.appendChild(doc.createTextNode(person.getCountryJob()));
					} else if (ndeAttribute.getNodeName().equals("image")) {
						try {
							ndeAttribute.appendChild(doc.createTextNode(person.getImageId().toString()));
						} catch (Exception exe) {
						}
					} else if (ndeAttribute.getNodeName().equals("sex")) {
						ndeAttribute.appendChild(doc.createTextNode(new Byte(person.getSex()).toString()));
					}
				} catch (Exception exe) {
					log.error("An error occured", exe);
				}
			}
			it = XercesHelper.findNodes(ndeInclude, "./include");
			while (it.hasNext()) {
				Node ndeAttribute = (Node) it.next();
				try {
					this.fillComponent(doc, ndeAttribute);
				} catch (Exception exe) {
				}
			}
		} else {
			log.info("Person with Id " + id + " not found for page " + this.getPath4CurrentRequest());
		}
	}

	private void fillAddress(Long id, Document doc, Node ndeInclude) throws Exception {
		AddressValue address = webSpringBean.getAddress(id);

		Iterator it = XercesHelper.findNodes(ndeInclude, "./content/*");
		while (it.hasNext()) {
			Node ndeAttribute = (Node) it.next();

			if (ndeAttribute.getNodeName().equals("roomNr")) {
				if (address.getRoomNr() != null) ndeAttribute.appendChild(doc.createTextNode(address.getRoomNr()));
			} else if (ndeAttribute.getNodeName().equals("buildingLevel")) {
				if (address.getBuildingLevel() != null) ndeAttribute.appendChild(doc.createTextNode(address.getBuildingLevel()));
			} else if (ndeAttribute.getNodeName().equals("buildingNr")) {
				if (address.getBuildingNr() != null) ndeAttribute.appendChild(doc.createTextNode(address.getBuildingNr()));
			} else if (ndeAttribute.getNodeName().equals("street")) {
				if (address.getStreet() != null) ndeAttribute.appendChild(doc.createTextNode(address.getStreet()));
			} else if (ndeAttribute.getNodeName().equals("streetNr")) {
				if (address.getStreetNr() != null) ndeAttribute.appendChild(doc.createTextNode(address.getStreetNr()));
			} else if (ndeAttribute.getNodeName().equals("zipcode")) {
				if (address.getZipCode() != null) ndeAttribute.appendChild(doc.createTextNode(address.getZipCode()));
			} else if (ndeAttribute.getNodeName().equals("country")) {
				if (address.getCountry() != null) ndeAttribute.appendChild(doc.createTextNode(address.getCountry()));
			} else if (ndeAttribute.getNodeName().equals("countryCode")) {
				if (address.getCountryCode() != null) ndeAttribute.appendChild(doc.createTextNode(address.getCountryCode()));
			} else if (ndeAttribute.getNodeName().equals("city")) {
				if (address.getCity() != null) ndeAttribute.appendChild(doc.createTextNode(address.getCity()));
			} else if (ndeAttribute.getNodeName().equals("postOfficeBox")) {
				if (address.getPostOfficeBox() != null) ndeAttribute.appendChild(doc.createTextNode(address.getPostOfficeBox()));
			} else if (ndeAttribute.getNodeName().equals("phone1")) {
				if (address.getPhone1() != null) ndeAttribute.appendChild(doc.createTextNode(address.getPhone1()));
			} else if (ndeAttribute.getNodeName().equals("phone2")) {
				if (address.getPhone2() != null) ndeAttribute.appendChild(doc.createTextNode(address.getPhone2()));
			} else if (ndeAttribute.getNodeName().equals("fax")) {
				if (address.getFax() != null) ndeAttribute.appendChild(doc.createTextNode(address.getFax()));
			} else if (ndeAttribute.getNodeName().equals("homepage")) {
				if (address.getHomepage() != null) ndeAttribute.appendChild(doc.createTextNode(address.getHomepage()));
			} else if (ndeAttribute.getNodeName().equals("misc")) {
				if (address.getMisc() != null) ndeAttribute.appendChild(doc.createTextNode(address.getMisc()));
			} else if (ndeAttribute.getNodeName().equals("mobilePhone")) {
				if (address.getMobilePhone() != null) ndeAttribute.appendChild(doc.createTextNode(address.getMobilePhone()));
			} else if (ndeAttribute.getNodeName().equals("email")) {
				if (address.getEmail() != null) ndeAttribute.appendChild(doc.createTextNode(address.getEmail()));
			}
		}
	}

	private void fillDepartment(Long id, Document doc, Node ndeInclude) throws Exception {
		DepartmentValue department = webSpringBean.getDepartment(id);

		Iterator it = XercesHelper.findNodes(ndeInclude, "./content/*");
		while (it.hasNext()) {
			Node node = (Node) it.next();
			if (node.getNodeName().equals("name")) {
				node.appendChild(doc.createTextNode(department.getName()));
			} else if (node.getNodeName().equals("include")) {
				this.fillComponent(doc, node);
			}
		}

		it = XercesHelper.findNodes(ndeInclude, "./include");
		while (it.hasNext()) {
			Node node = (Node) it.next();
			this.fillComponent(doc, node);
		}
	}

	private void fillUnit(Integer id, Document doc, Node ndeInclude) throws Exception {
		UnitValue unit = webSpringBean.getUnit(id);

		Iterator it = XercesHelper.findNodes(ndeInclude, "./content/*");
		while (it.hasNext()) {
			Node node = (Node) it.next();
			if (node.getNodeName().equals("name")) {
				node.appendChild(doc.createTextNode(unit.getName()));
			}
		}

		it = XercesHelper.findNodes(ndeInclude, "./include");
		while (it.hasNext()) {
			Node node = (Node) it.next();
			this.fillComponent(doc, node);
		}
	}

	private void fillTalkTime(Long id, Document document, Node ndeInclude) throws Exception {
		TalktimeValue talktimeDao = webSpringBean.getTalktime(id);

		Iterator it = XercesHelper.findNodes(ndeInclude, "./content/*");
		while (it.hasNext()) {
			Node node = (Node) it.next();
			if (node.getNodeName().equals("talkTimeType")) {
				node.appendChild(document.createTextNode(talktimeDao.getTalkTimeType()));
			} else if (node.getNodeName().equals("talkTimes")) {
				Node root = document.createElement("times");
				try {
					Document doc = XercesHelper.string2Dom(talktimeDao.getTalkTimes());
					Iterator it2 = XercesHelper.findNodes(doc, "//time");
					while (it2.hasNext()) {
						Node foundNode = (Node) it2.next();
						Node time = document.createElement("time");
						Node txtnde = document.createTextNode(XercesHelper.getNodeValue(foundNode));
						time.appendChild(txtnde);
						root.appendChild(time);
					}
				} catch (Exception exe) {
					log.error("An error occured", exe);
				}
				node.appendChild(root);
			}
		}
	}

	/**
	 * Method to create "Contact-Lists". <br/>This Contactlist will show all persons in one unit at the given Node
	 * "unitMembersList" inside the content-xml. <br/>Will be called after generating the aggregations.
	 *
	 * @param document Document-Node of the content
	 */
	private void fillMembersList(Document document) {
		try {
			Integer siteId = siteValue.getSiteId();
			Iterator it = XercesHelper.findNodes(document, "//membersList | //unitMembersList");
			while (it.hasNext()) {
				Element membersList = (Element) it.next();
				if (log.isDebugEnabled()) log.debug("Found membersList");
				/*
				 * now we habe three modes: - no attributes, get actual unit -
				 * unit attribute, get all from this unitId - surname attribute - lastname attribute
				 */
				Integer unitId = null;
				String unitAttr = this.getAttribute(membersList, "unitId");
				if (unitAttr == null || unitAttr.equalsIgnoreCase("")) {
					unitId = unitValue.getUnitId();
				} else if (unitAttr.equalsIgnoreCase("all")) {
					unitId = null;
				} else {
					unitId = new Integer(unitAttr);
				}
				if (new Integer(0).equals(unitId)) { return; }

				String surname = this.getAttribute(membersList, "surname");
				if (surname == null || "".equalsIgnoreCase(surname)) {
					surname = "*";
				}
				surname = surname.replaceAll("[*]", "%");

				String lastname = this.getAttribute(membersList, "lastname");
				if (lastname == null || "".equalsIgnoreCase(lastname)) {
					lastname = "*";
				}
				lastname = lastname.replaceAll("[*]", "%");

				String ml = webSpringBean.getMembersList(siteId, unitId, surname, lastname);

				if (log.isDebugEnabled()) log.debug("Converting MembersList to XML");
				try {
					Document pdoc = XercesHelper.string2Dom(ml); //ml contains all
					Node mlnde = document.importNode(pdoc.getFirstChild(), true);
					membersList.getParentNode().replaceChild(mlnde, membersList);
				} catch (Exception exe) {
				}

				if (log.isDebugEnabled()) log.debug("Finished MembersList");
			} //end while
		} catch (Exception exe) {
			log.error("An error occured", exe);
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
	private void fillContentInclude(Document document) throws Exception {
		Iterator it = XercesHelper.findNodes(document, "//contentInclude");
		while (it.hasNext()) {
			String searchBy = null;
			String xPathQuery = null;
			boolean includeUnit = false;
			Element elm = (Element) it.next();
			Node ndeChild = elm.getFirstChild();
			while (ndeChild != null) {
				if (ndeChild.getNodeType() == Node.ELEMENT_NODE) {
					if ("byUnit".equalsIgnoreCase(ndeChild.getNodeName())) {
						includeUnit = true;
						searchBy = ndeChild.getFirstChild().getNodeValue();
					} else if ("byViewComponent".equalsIgnoreCase(ndeChild.getNodeName())) {
						includeUnit = false;
						searchBy = ndeChild.getFirstChild().getNodeValue();
					}
					Node optional = ndeChild.getNextSibling();
					while (optional != null) {
						if (optional.getNodeType() == Node.ELEMENT_NODE) {
							if ("xpathElement".equalsIgnoreCase(optional.getNodeName())) {
								xPathQuery = optional.getFirstChild().getNodeValue();
							}
						}
						optional = optional.getNextSibling();
					}
				}
				ndeChild = ndeChild.getNextSibling();
			}
			// remove all children
			Node node = elm.getFirstChild();
			while (node != null) {
				elm.removeChild(node);
				node = elm.getFirstChild();
			}
			try {
				Document unitXmlDoc = XercesHelper.string2Dom(webSpringBean.getIncludeContent(viewComponentId, includeUnit, searchBy, iAmTheLiveserver, xPathQuery));
				Node contentInclude = XercesHelper.findNode(unitXmlDoc, "//contentInclude/contentInclude");
				if (contentInclude == null) {
					contentInclude = XercesHelper.findNode(unitXmlDoc, "//contentInclude");
				}
				if (contentInclude != null) {
					Node xd = document.importNode(contentInclude, true);
					elm.appendChild(xd);
				}
			} catch (Exception e) {
				log.warn("Error getting includeContent: " + e.getMessage(), e);
			}
		}
	}

	/*
	 * BUG 3451
	 * Input:
	 * <teaserInclude dcfname="teaserInclude" label="Teaser">
	 * 	<teaserRandomized>
	 * 		<count>3</count>
	 * 		<unit>this</unit>
	 * 	</teaserRandomized>
	 * 	<teaserRandomized>
	 * 		<count>1</count>
	 * 		<unit>root</unit>
	 * 	</teaserRandomized>
	 * </teaserInclude>
	 * 
	 * <teaserInclude dcfname="teaserInclude" label="Teaser">
	 * 	<teaserRef viewComponentId="23" teaserIdentifier="1234" xpathTeaserElement="//teaser" xpathTeaserIdentifier="@id"/>
	 * 	<teaserRef viewComponentId="23" teaserIdentifier="3434343" xpathTeaserElement="//teaser" xpathTeaserIdentifier="@id"/>
	 * </teaserInclude>
	 * 
	 * <teaserInclude dcfname="teaserInclude" label="Teaser">
	 * 	<teaserRef viewComponentId="23" xpathTeaserElement="//teaser"/>
	 * 	<teaserRef viewComponentId="34" xpathTeaserElement="//teaser"/>
	 * </teaserInclude>
	 * 
	 * Result:
	 * <teaserInclude dcfname="teserInclude" label="Teaser">
	 * 	<teaser>
	 * 		xmlcontent der viewcomponentseite
	 * 		dfsdf sd sd sdfsdfsdf dsf yd xydfsad
	 * 	</teaser>
	 * 	<teaser>
	 * 		dfgdfg dfg df df df sdasd asd asd asd asd
	 * 	</teaser>
	 * </teaserInclude>
	 */
	private void fillTeaserInclude(Document document) throws Exception {
		Iterator it = XercesHelper.findNodes(document, "//teaserInclude");
		while (it.hasNext()) {
			Element elm = (Element) it.next();
			try {
				Document teaserDoc = XercesHelper.string2Dom(webSpringBean.getIncludeTeaser(viewComponentId, iAmTheLiveserver, XercesHelper.node2string(elm)));
				Node teaserInclude = XercesHelper.findNode(teaserDoc, "//teaserInclude/teaserInclude");
				if (teaserInclude == null) {
					teaserInclude = XercesHelper.findNode(teaserDoc, "//teaserInclude");
				}
				if (teaserInclude != null) {
					Node xd = document.importNode(teaserInclude, true);
					// remove all children
					Node node = elm.getFirstChild();
					while (node != null) {
						elm.removeChild(node);
						node = elm.getFirstChild();
					}
					elm.appendChild(xd);
				}
			} catch (Exception e) {
				log.warn("Error getting teaserInclude: " + e.getMessage(), e);
			}
		}
	}

	private void fillUnitList(Document document) throws Exception {
		Iterator it = XercesHelper.findNodes(document, "//unitList");
		while (it.hasNext()) {
			Element elm = (Element) it.next();
			Document unitXmlDoc = XercesHelper.string2Dom(webSpringBean.getAllUnitsXml(siteValue.getSiteId()));
			Iterator ui = XercesHelper.findNodes(unitXmlDoc, "//unit");
			while (ui.hasNext()) {
				Element unit = (Element) ui.next();
				Node xd = document.importNode(unit, true);
				elm.appendChild(xd);
			}
		}
	}

	private void solveInternalLinks(Document document) {
		Iterator it = XercesHelper.findNodes(document, "//internalLink/internalLink");
		while (it.hasNext()) {
			Element elm = (Element) it.next();
			Integer vcid = null;
			try {
				vcid = new Integer(elm.getAttribute("viewid"));
				String path = webSpringBean.getPath4ViewComponent(vcid);
				String lang = webSpringBean.getViewDocument4ViewComponentId(vcid).getLanguage();
				Integer unitId = webSpringBean.getUnitIdForViewComponent(vcid);

				elm.setAttribute("url", path);
				elm.setAttribute("language", lang);
				if (unitId != null)
					elm.setAttribute("unitid", unitId.toString());
			} catch (Exception exe) {
				log.info("Could not solve internalLink with vcid " + vcid + " in content of vcid " + this.viewComponentId + " (\"" + this.getPath4CurrentRequest() + "\")");
			}
		}
	}

	/**
	 * Resolves Attributes from Nodenames. As required, it is possible to overwrite every Parameter with
	 * request-parameters called: <br>
	 * conquest-{nodename}-{attributename}
	 *
	 * @param node
	 * @param attributeName
	 * @return The Value
	 */
	private String getAttribute(Element node, String attributeName) {
		String param = request.getParameter("conquest_" + node.getNodeName() + "_" + attributeName);
		if (param == null || "".equalsIgnoreCase(param)) {
			param = node.getAttribute(attributeName);
		}
		return param;
	}

	/**
	 * Generate the unique key. This key must be unique inside the space of this component.
	 *
	 * @return The generated key hashes the src
	 */
	public Serializable getKey() {
		return this.uniqueKey;
	}

	/**
	 * Generate the validity object.
	 *
	 * @return The generated validity object or <code>null</code> if the component is currently not cacheable.
	 */
	public SourceValidity getValidity() {
		chgDate = 0;
		try {
			chgDate = this.getModifiedDate().getTime();
		} catch (Exception exe) {
			log.error("An error occured", exe);
		}
		if (chgDate != 0) {
			SourceValidity sv = new TimeStampValidity(chgDate);
			return sv;
		}
		return null;
	}

	/**
	 * Returns the last modified Date of this ViewComponent. <br>
	 * It will also check if there are some contained DatabaseComponents or other dynamic content, who have to be
	 * checked.
	 */
	private Date getModifiedDate() {
		if (log.isDebugEnabled()) log.debug("start getModifiedDate for " + this.requestUrl);
		if (viewComponentId == null) { return new Date(System.currentTimeMillis()); }

		try {
			if (this.webSearchquery != null) {
				if (log.isDebugEnabled()) log.debug("Found searchquery - do not validate at all, user will get fresh copy of this page");
				return new Date(System.currentTimeMillis());
			}
			if (cm.existsContext("authentication")) {
				if (log.isDebugEnabled()) log.debug("Found validation-context - do not validate at all, user will get fresh copy of this page");
				return new Date(System.currentTimeMillis());
			}
		} catch (Exception exe) {
		}

		mdch.setLiveserver(iAmTheLiveserver);
		mdch.setSiteId(siteValue.getSiteId());
		mdch.setViewComponentId(viewComponentId);
		try {
			InputSource in = null;
			if (this.inputSource == null) {
				mdch.setModifiedDate(webSpringBean.getModifiedDate4Cache(viewComponentId));
				String content = webSpringBean.getContent(viewComponentId, iAmTheLiveserver);
				in = new InputSource(new StringReader(content));
			} else {
				// Input was a file instead of a view component id
				in = new InputSource(this.inputSource.getInputStream());
				mdch.setModifiedDate(new Date(this.inputSource.getLastModified())); //there we start as well :)
			}

			parser.parse(in, mdch);
			//DOMStreamer ds = new DOMStreamer(mdch);
			//ds.stream(XercesHelper.inputSource2Dom(in).getDocumentElement());
		} catch (SAXException se) {
			if (log.isTraceEnabled()) log.trace("Error getting last modified date for \"" + this.requestUrl + "\": " + se.getMessage(), se);
		} catch (Exception exe) {
			log.warn("Error getting last modified date for \"" + this.requestUrl + "\": " + exe.getMessage());
			if (log.isDebugEnabled()) log.debug("Error getting last modified date for \"" + this.requestUrl + "\": " + exe.getMessage(), exe);
		}

		if (log.isDebugEnabled()) log.debug("end getModifiedDate for " + this.requestUrl + " with " + mdch.getModifiedDate());
		return mdch.getModifiedDate();
	}

	private final class WebSearchResultComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			WebSearchValue v1 = (WebSearchValue) o1;
			WebSearchValue v2 = (WebSearchValue) o2;
			if (v1 == null || v1.getScore() == null) return -1;
			if (v2 == null || v2.getScore() == null) return 1;
			return v1.getScore().compareTo(v2.getScore());
		}

	}

	private void appendWebSearchXml(Document doc, Element parent, SearchResultValue value) {
		if (value.getDocumentId() != null) {
			// document
			parent.setAttribute("resultType", "document");
			Node documentId = doc.createElement("documentId");
			Node documentIdText = doc.createCDATASection(value.getDocumentId().toString());
			documentId.appendChild(documentIdText);
			parent.appendChild(documentId);
			Node documentName = doc.createElement("documentName");
			if (value.getDocumentName() != null) {
				Node titleTxt = doc.createCDATASection(value.getDocumentName());
				documentName.appendChild(titleTxt);
			}
			parent.appendChild(documentName);
			Node mimeType = doc.createElement("mimeType");
			if (value.getMimeType() != null) {
				Node languageTxt = doc.createCDATASection(value.getMimeType());
				mimeType.appendChild(languageTxt);
			}
			parent.appendChild(mimeType);
			Node timeStamp = doc.createElement("timeStamp");
			if (value.getTimeStamp() != null) {
				Long dateTime = Long.valueOf(value.getTimeStamp());
				Node viewtypeTxt = doc.createCDATASection(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.US).format(new Date(dateTime.longValue())));
				timeStamp.appendChild(viewtypeTxt);
			}
			parent.appendChild(timeStamp);
		} else {
			// page
			parent.setAttribute("resultType", "page");
			Node url = doc.createElement("url");
			Node urlTxt = doc.createCDATASection(value.getUrl());
			url.appendChild(urlTxt);
			parent.appendChild(url);
			Node title = doc.createElement("title");
			if (value.getTitle() != null) {
				Node titleTxt = doc.createCDATASection(value.getTitle());
				title.appendChild(titleTxt);
			}
			parent.appendChild(title);
			Node language = doc.createElement("language");
			if (value.getLanguage() != null) {
				Node languageTxt = doc.createCDATASection(value.getLanguage());
				language.appendChild(languageTxt);
			}
			parent.appendChild(language);
			Node viewtype = doc.createElement("viewtype");
			if (value.getViewType() != null) {
				Node viewtypeTxt = doc.createCDATASection(value.getViewType());
				viewtype.appendChild(viewtypeTxt);
			}
			parent.appendChild(viewtype);
			Node template = doc.createElement("template");
			if (value.getTemplate() != null) {
				Node templateTxt = doc.createCDATASection(value.getTemplate());
				template.appendChild(templateTxt);
			}
			parent.appendChild(template);
		}
		// common attributes
		Node score = doc.createElement("score");
		Node scoreTxt = doc.createCDATASection(value.getScore() + "%");
		score.appendChild(scoreTxt);
		parent.appendChild(score);
		
		String summary = "";
		try {
			summary = "<root><summary>" + value.getSummary().replaceAll("&", "&#38;") + "</summary></root>";
			Node node = XercesHelper.findNode(XercesHelper.string2Dom(summary), "//summary");
			Node importNode = doc.importNode(node, true);
			parent.appendChild(importNode); 
		} catch (Exception exe) {
			log.warn("Searchresult could not be parsed as XML. Returning as CDATA. Search:" + webSearchquery + " Summary:" + summary, exe);
			Node s = doc.createElement("summary");
			if (value.getSummary() != null) {
				Node summaryTxt = doc.createCDATASection(value.getSummary());
				s.appendChild(summaryTxt);
			}
			parent.appendChild(s);
		}
		Node unitId = doc.createElement("unitId");
		if (value.getUnitId() != null) {
			Node unitIdTxt = doc.createTextNode(value.getUnitId().toString());
			unitId.appendChild(unitIdTxt);
		}
		parent.appendChild(unitId);
		Node unitName = doc.createElement("unitName");
		if (value.getUnitName() != null) {
			Node unitNameTxt = doc.createCDATASection(value.getUnitName());
			unitName.appendChild(unitNameTxt);
		}
		parent.appendChild(unitName);
	}

	private String getPath4CurrentRequest() {
		String result = this.path4ViewComponentCacheMap.get(this.viewComponentId);
		if (result == null) {
			String requestPath = "";
			String requestLang = "";
			try {
				requestLang = webSpringBean.getViewDocument4ViewComponentId(this.viewComponentId).getLanguage();
			} catch (Exception e) {
			}
			try {
				requestPath = webSpringBean.getPath4ViewComponent(this.viewComponentId);
			} catch (Exception e) {
			}
			result = this.siteValue.getShortName() + "/" + requestLang + "/" + requestPath;
			this.path4ViewComponentCacheMap.put(this.viewComponentId, result);
		}
		
		return result;
	}

}
