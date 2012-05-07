package de.juwimm.cms.search.beans;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.compass.core.Compass;
import org.compass.core.CompassHit;
import org.compass.core.CompassHits;
import org.compass.core.CompassQuery;
import org.compass.core.CompassQueryBuilder.CompassBooleanQueryBuilder;
import org.compass.core.CompassSession;
import org.compass.core.CompassTransaction;
import org.compass.core.Property;
import org.compass.core.Resource;
import org.compass.core.engine.spellcheck.SearchEngineSpellCheckManager;
import org.compass.core.engine.spellcheck.SearchEngineSpellCheckSuggestBuilder;
import org.compass.core.engine.spellcheck.SearchEngineSpellSuggestions;
import org.compass.core.lucene.util.LuceneHelper;
import org.compass.core.support.search.CompassSearchCommand;
import org.compass.core.support.search.CompassSearchHelper;
import org.compass.core.support.search.CompassSearchResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tizzit.util.XercesHelper;

import de.juwimm.cms.beans.foreign.TizzitPropertiesBeanSpring;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.model.ContentHbm;
import de.juwimm.cms.model.ContentHbmDao;
import de.juwimm.cms.model.ContentVersionHbm;
import de.juwimm.cms.model.DocumentHbm;
import de.juwimm.cms.model.DocumentHbmDao;
import de.juwimm.cms.model.HostHbm;
import de.juwimm.cms.model.HostHbmDao;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.SiteHbmDao;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.model.UnitHbmDao;
import de.juwimm.cms.model.ViewComponentHbm;
import de.juwimm.cms.model.ViewComponentHbmDao;
import de.juwimm.cms.model.ViewDocumentHbm;
import de.juwimm.cms.model.ViewDocumentHbmDao;
import de.juwimm.cms.safeguard.remote.SafeguardServiceSpring;
import de.juwimm.cms.search.res.DocumentResourceLocatorFactory;
import de.juwimm.cms.search.res.HtmlResourceLocator;
import de.juwimm.cms.search.vo.LinkDataValue;
import de.juwimm.cms.search.vo.SearchResultValue;
import de.juwimm.cms.search.vo.XmlSearchValue;
import de.juwimm.cms.search.xmldb.XmlDb;

/**
 * 
 * @author <a href="mailto:j2ee@juwimm.com">Sascha-Matthias Kulawik</a>
 * company Juwi|MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since cqcms-core 03.07.2009
 */
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
public class SearchengineService {
	private static Logger log = Logger.getLogger(SearchengineService.class);
	private static final String LUCENE_ESCAPE_CHARS = "[\\\\!\\(\\)\\:\\^\\]\\{\\}\\~]";
	private static final Pattern LUCENE_PATTERN = Pattern.compile(LUCENE_ESCAPE_CHARS);
	private static final String REPLACEMENT_STRING = "\\\\$0";
	private static final int NUMBER_OF_SUGGESTIONS=5;
	@Autowired
	private Compass compass;
	@Autowired
	private XmlDb xmlDb;
	@Autowired
	private HtmlResourceLocator htmlResourceLocator;
	@Autowired
	private DocumentResourceLocatorFactory documentResourceLocatorFactory;
	@Autowired
	private SearchengineDeleteService searchengineDeleteService;
	@Autowired
	private SafeguardServiceSpring safeguardServiceSpring;
	@Autowired
	private TizzitPropertiesBeanSpring tizzitPropertiesBeanSpring;

	private ViewComponentHbmDao viewComponentHbmDao;
	private SiteHbmDao siteHbmDao;
	private HostHbmDao hostHbmDao;
	private DocumentHbmDao documentHbmDao;
	private ContentHbmDao contentHbmDao;
	private ViewDocumentHbmDao viewDocumentHbmDao;
	private UnitHbmDao unitHbmDao;

	private final HttpClient client = new HttpClient();

	public SafeguardServiceSpring getSafeguardServiceSpring() {
		return safeguardServiceSpring;
	}

	public void setSafeguardServiceSpring(SafeguardServiceSpring safeguardServiceSpring) {
		this.safeguardServiceSpring = safeguardServiceSpring;
	}

	public void setViewComponentHbmDao(ViewComponentHbmDao viewComponentHbmDao) {
		this.viewComponentHbmDao = viewComponentHbmDao;
	}

	public void setSiteHbmDao(SiteHbmDao siteHbmDao) {
		this.siteHbmDao = siteHbmDao;
	}

	public void setHostHbmDao(HostHbmDao hostHbmDao) {
		this.hostHbmDao = hostHbmDao;
	}

	public void setDocumentHbmDao(DocumentHbmDao documentHbmDao) {
		this.documentHbmDao = documentHbmDao;
	}

	public void setContentHbmDao(ContentHbmDao contentHbmDao) {
		this.contentHbmDao = contentHbmDao;
	}

	public void setViewDocumentHbmDao(ViewDocumentHbmDao viewDocumentHbmDao) {
		this.viewDocumentHbmDao = viewDocumentHbmDao;
	}

	public void setUnitHbmDao(UnitHbmDao unitHbmDao) {
		this.unitHbmDao = unitHbmDao;
	}

	public ViewComponentHbmDao getViewComponentHbmDao() {
		return viewComponentHbmDao;
	}

	public SiteHbmDao getSiteHbmDao() {
		return siteHbmDao;
	}

	public HostHbmDao getHostHbmDao() {
		return hostHbmDao;
	}

	public DocumentHbmDao getDocumentHbmDao() {
		return documentHbmDao;
	}

	public ContentHbmDao getContentHbmDao() {
		return contentHbmDao;
	}

	public ViewDocumentHbmDao getViewDocumentHbmDao() {
		return viewDocumentHbmDao;
	}

	public UnitHbmDao getUnitHbmDao() {
		return unitHbmDao;
	}

	/**
	 * @see de.juwimm.cms.search.remote.SearchengineServiceSpring#searchXML(java.lang.Integer, java.lang.String)
	 */
	@Transactional(readOnly = true)
	public XmlSearchValue[] searchXML(Integer siteId, String xpathQuery) throws Exception {
		XmlSearchValue[] retString = null;
		retString = xmlDb.searchXml(siteId, xpathQuery);
		return retString;
	}

	/**
	 * @see de.juwimm.cms.search.remote.SearchengineServiceSpring#startIndexer()
	 */
	public void startIndexer() throws Exception {
		try {
			Date start = new Date();
			Collection sites = getSiteHbmDao().findAll();
			Iterator itSites = sites.iterator();
			while (itSites.hasNext()) {
				SiteHbm site = (SiteHbm) itSites.next();
				if (log.isDebugEnabled()) log.debug("Starting with Site " + site.getName() + " (" + site.getSiteId() + ")");
				reindexSite(site.getSiteId());
			}
			Date end = new Date();
			if (log.isInfoEnabled()) log.info(end.getTime() - start.getTime() + " total milliseconds");
		} catch (Exception e) {
			log.error("Caught a " + e.getClass() + "\n with message: " + e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.search.remote.SearchengineServiceSpring#reindexSite(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	public void reindexSite(Integer siteId) throws Exception {
		if (log.isDebugEnabled()) log.debug("Starting reindexing site " + siteId);
		SiteHbm site = getSiteHbmDao().load(siteId);
		//if site with external site search then schedule it to the  ExternalSitesCronService for indexing
		if (site.getExternalSiteSearch() != null && site.getExternalSiteSearch()) {
			site.setUpdateSiteIndex(true);
			return;
		}
		CompassSession session = compass.openSession();
		CompassQuery query = session.queryBuilder().term("siteId", siteId);
		if (log.isDebugEnabled()) log.debug("delete sites with siteId: " + siteId + " with query: " + query.toString());
		session.delete(query);
		session.close();
		Date start = new Date();
		try {
			Collection vdocs = getViewDocumentHbmDao().findAll(siteId);
			Iterator itVdocs = vdocs.iterator();
			while (itVdocs.hasNext()) {
				ViewDocumentHbm vdl = (ViewDocumentHbm) itVdocs.next();
				if (log.isDebugEnabled()) log.debug("- Starting ViewDocument: " + vdl.getLanguage() + " " + vdl.getViewType());
				ViewComponentHbm rootvc = vdl.getViewComponent();
				if(tizzitPropertiesBeanSpring.getSearch().isUseOracleBatchUpdate()){
					setUpdateSearchIndex4AllVCsUsingQuery(rootvc);
				} else {
					setUpdateSearchIndex4AllVCs(rootvc);
				}
			}
		} catch (Exception e) {
			log.error("Caught a " + e.getClass() + "\n with message: " + e.getMessage());
		}
		try {
			Collection<UnitHbm> units = getUnitHbmDao().findBySite(siteId);
			for (UnitHbm unit : units) {
				Collection<DocumentHbm> docs = getDocumentHbmDao().findAllPerUnit(unit.getUnitId());
				for (DocumentHbm doc : docs) {
					// -- Indexing through No-Messaging
					doc.setUpdateSearchIndex(true);
				}
				
				Collection<ViewComponentHbm> allViewComponentsForUnit=new ArrayList<ViewComponentHbm>();
				Collection<ViewComponentHbm> rootViewComponents=getViewComponentHbmDao().find4Unit(unit.getUnitId());
				for (ViewComponentHbm viewComponentHbm : rootViewComponents) {
					allViewComponentsForUnit.addAll(viewComponentHbm.getAllChildrenOfUnit());
				}
				docs=new ArrayList<DocumentHbm>();
				for (ViewComponentHbm viewComponentHbm : allViewComponentsForUnit) {
					docs.addAll(getDocumentHbmDao().findAllPerViewComponent(viewComponentHbm.getViewComponentId()));
				}
				for (DocumentHbm doc : docs) {
					// -- Indexing through No-Messaging
					doc.setUpdateSearchIndex(true);
				}
			}
		} catch (Exception e) {
			log.error("Caught a " + e.getClass() + "\n with message: " + e.getMessage());
		}
		Date end = new Date();
		if (log.isInfoEnabled()) log.info(end.getTime() - start.getTime() + " total milliseconds for site " + siteId);
		if (log.isDebugEnabled()) log.debug("finished index for site " + siteId);
	}

	private void setUpdateSearchIndex4AllVCs(ViewComponentHbm viewComponent) {
		if (!"DUMMY".equals(viewComponent.getReference())) {
			if (viewComponent.getViewType() == Constants.VIEW_TYPE_CONTENT || viewComponent.getViewType() == Constants.VIEW_TYPE_UNIT) {
				ContentHbm content = null;
				try {
					content = getContentHbmDao().load(new Integer(viewComponent.getReference()));
					// -- Indexing through No-Messaging
					content.setUpdateSearchIndex(true);
				} catch (Exception exe) {
					log.warn("Could not resolve Content with Id: " + viewComponent.getReference(), exe);
				}
			}
			if (!viewComponent.isLeaf()) {
				Collection children = viewComponent.getChildren();
				Iterator itChildren = children.iterator();
				while (itChildren.hasNext()) {
					ViewComponentHbm child = (ViewComponentHbm) itChildren.next();
					setUpdateSearchIndex4AllVCs(child);
				}
			}
		}
	}
	private void setUpdateSearchIndex4AllVCsUsingQuery(ViewComponentHbm viewComponent) {
		log.debug("Using oracle batch update for view component id "+viewComponent.getViewComponentId());
		getViewComponentHbmDao().bulkUpdateForSearchengine(viewComponent.getViewComponentId());
	}

	private LinkDataValue[] getLinkData(Integer siteId, DocumentHbm doc) {
		Collection<LinkDataValue> linkDataList = new ArrayList<LinkDataValue>();
		try {
			XmlSearchValue[] xmlData = this.searchXML(siteId, "//document[@src=\"" + doc.getDocumentId().toString() + "\"]");
			if (xmlData != null && xmlData.length > 0) {
				for (int i = (xmlData.length - 1); i >= 0; i--) {
					LinkDataValue ldv = new LinkDataValue();
					ldv.setUnitId(xmlData[i].getUnitId());
					ldv.setViewComponentId(xmlData[i].getViewComponentId());
					try {
						UnitHbm unit = getUnitHbmDao().load(ldv.getUnitId());
						ldv.setUnitName(unit.getName());
					} catch (Exception e) {
						log.error("Error loading Unit " + ldv.getUnitId() + ": Perhaps SearchIndex is corrupt? " + e.getMessage(), e);
					}
					try {
						ViewComponentHbm viewComponent = getViewComponentHbmDao().load(ldv.getViewComponentId());
						ldv.setViewComponentPath(viewComponent.getPath());
						ViewDocumentHbm viewDocument = viewComponent.getViewDocument();
						ldv.setLanguage(viewDocument.getLanguage());
						ldv.setViewType(viewDocument.getViewType());
					} catch (Exception e) {
						log.error("Error loading ViewComponent " + ldv.getViewComponentId() + ": Perhaps SearchIndex is corrupt? " + e.getMessage(), e);
					}
					linkDataList.add(ldv);
				}
			}
		} catch (Exception e) {
			log.error("Error loading link-data for document " + doc.getDocumentId() + ": " + e.getMessage(), e);
		}
		return linkDataList.toArray(new LinkDataValue[0]);
	}

	@Transactional(readOnly = true)
	public XmlSearchValue[] searchXmlByUnit(Integer unitId, Integer viewDocumentId, String xpathQuery, boolean parentSearch) throws Exception {
		XmlSearchValue[] retString = null;

		if (parentSearch) {
			try {
				ViewComponentHbm vc = getViewComponentHbmDao().find4Unit(unitId, viewDocumentId);
				if (vc != null && !vc.isRoot()) {
					unitId = vc.getParent().getUnit4ViewComponent();
				}
			} catch (Exception e) {
				log.error("Error finding VC by unit " + unitId + " and viewDocument " + viewDocumentId + " in searchXmlByUnit!", e);
			}
		}

		retString = xmlDb.searchXmlByUnit(unitId, viewDocumentId, xpathQuery);
		return retString;
	}

	@Transactional(readOnly = true)
	public SearchResultValue[] searchWeb(Integer siteId,Integer unitId, final String searchItem, Integer pageSize, Integer pageNumber, Map safeGuardCookieMap, String searchUrl, boolean isLiveServer) throws Exception {
		if (pageSize != null && pageSize.intValue() <= 1) pageSize = new Integer(20);
		if (pageNumber != null && pageNumber.intValue() < 0) pageNumber = new Integer(0); // first page
		SearchResultValue[] staticRetArr = null;
		Vector<SearchResultValue> retArr = new Vector<SearchResultValue>();

		if (log.isDebugEnabled()) log.debug("starting compass-search");

		try {
			if (log.isDebugEnabled()) log.debug("searchurl is: " + searchUrl);
			if (log.isDebugEnabled()) log.debug("search for: \"" + searchItem + "\"");
			if (log.isDebugEnabled()) log.debug("SonderZeicheVergleich (ae, oe, ue, ss): ä ö ü ß");

			//TODO: find calls of searchWeb and ADD exception handling 
			//special chars with a meaning in Lucene have to be escaped - there is a mechanism for
			//that in Lucene - QueryParser.escape but I don't want to replace '*','?','+','-' in searchItem 
			String searchItemEsc = LUCENE_PATTERN.matcher(searchItem).replaceAll(REPLACEMENT_STRING);
			String searchUrlEsc = null;
			if (searchUrl != null) {
				searchUrlEsc = LUCENE_PATTERN.matcher(searchUrl).replaceAll(REPLACEMENT_STRING);
			}

			if (log.isDebugEnabled() && !searchItem.equalsIgnoreCase(searchItemEsc)) {
				log.debug("search for(escaped form): \"" + searchItemEsc + "\"");
			}

			CompassSession session = compass.openSession();

			//per default searchItems get connected by AND (compare CompassSettings.java)
			CompassQuery query = buildRatedWildcardQuery(session, siteId,unitId, searchItemEsc, searchUrlEsc, safeGuardCookieMap,isLiveServer);
			if (log.isDebugEnabled()) log.debug("search for query: " + query.toString());

			CompassSearchHelper searchHelper = new CompassSearchHelper(compass, pageSize) {
				@Override
				protected void doProcessBeforeDetach(CompassSearchCommand searchCommand, CompassSession session, CompassHits hits, int from, int size) {
					for (int i = 0; i < hits.length(); i++) {
						hits.highlighter(i).fragment("contents");
					}
				}
			};

			CompassSearchCommand command = null;

			if (pageSize == null) {
				command = new CompassSearchCommand(query);
			} else {
				command = new CompassSearchCommand(query, pageNumber);
			}

			CompassSearchResults results = searchHelper.search(command);
			if (log.isDebugEnabled()) log.debug("search lasted " + results.getSearchTime() + " milliseconds");
			CompassHit[] hits = results.getHits();
			if (log.isDebugEnabled()) log.debug(hits.length + " results found");
			for (int i = 0; i < hits.length; i++) {
				String alias = hits[i].getAlias();
				Resource resource = hits[i].getResource();
				SearchResultValue retVal = new SearchResultValue();
				retVal.setScore((int) (hits[i].getScore() * 100.0f));
				//CompassHighlightedText text = hits[i].getHighlightedText();
				retVal.setSummary(stripNonValidXMLCharacters(hits[i].getHighlightedText().getHighlightedText("contents")));
				retVal.setUnitId(new Integer(resource.getProperty("unitId").getStringValue()));
				retVal.setUnitName(resource.getProperty("unitName").getStringValue());
				retVal.setPageSize(pageSize);
				retVal.setPageNumber(pageNumber);
				retVal.setDuration(Long.valueOf(results.getSearchTime()));
				retVal.setPageAmount(results.getPages()!=null?results.getPages().length:null);
				retVal.setTotalHits(results.getTotalHits());
				if ("HtmlSearchValue".equalsIgnoreCase(alias)) {
					String url = resource.getProperty("url").getStringValue();
					if (url != null) {
						retVal.setUrl(url);
						retVal.setTitle(resource.getProperty("title").getStringValue());
						retVal.setLanguage(resource.getProperty("language").getStringValue());
						retVal.setViewType(resource.getProperty("viewtype").getStringValue());
						retVal.setIsLiveContent(Boolean.parseBoolean(resource.getProperty("isLiveContent").getStringValue()));
						Property template = resource.getProperty("template");
						retVal.setTemplate(template != null ? template.getStringValue() : "standard");
					}
				} else {
					String documentId = resource.getProperty("documentId").getStringValue();
					Integer docId = null;
					try {
						docId = Integer.valueOf(documentId);
					} catch (Exception e) {
						log.warn("Error converting documentId: " + e.getMessage());
						if (log.isDebugEnabled()) log.debug(e);
					}
					if (docId != null) {
						retVal.setDocumentId(docId);
						retVal.setDocumentName(resource.getProperty("documentName").getStringValue());
						retVal.setMimeType(resource.getProperty("mimeType").getStringValue());
						retVal.setTimeStamp(resource.getProperty("timeStamp").getStringValue());
					}
				}
				retArr.add(retVal);
			}
		} catch (BooleanQuery.TooManyClauses tmc) {
			StringBuffer sb = new StringBuffer(256);
			sb.append("BooleanQuery.TooManyClauses Exception. ");
			sb.append("This typically happens if a PrefixQuery, FuzzyQuery, WildcardQuery, or RangeQuery is expanded to many terms during search. ");
			sb.append("Possible solution: BooleanQuery.setMaxClauseCount() > 1024(default)");
			sb.append(" or reform the search querry with more letters and less wildcards (*).");
			log.error(sb.toString(), tmc);
			//Exception gets thrown again to allow the caller to react
			throw tmc;
		} catch (Exception e) {
			log.error("Error performing search with compass: " + e.getMessage(), e);
		}
		if (log.isDebugEnabled()) log.debug("finished compass-search");
		staticRetArr = new SearchResultValue[retArr.size()];
		retArr.toArray(staticRetArr);
		return staticRetArr;
	}
	
	/**
	 * This method provides search suggestions for the <b>searchItem</b> input string. 
	 * The search suggestions are limited to the site mentioned in the <b>siteId</b> 
	 * parameter and for the authorization tokens provided in the <b>safeGuardCookieMap</b>
	 * 
	 * @param siteId
	 * @param searchItem
	 * @param safeGuardCookieMap
	 * @return a {@link String} matrix of maximum 10 lines and exactly 2 columns. Each line contains a search suggestion, the first column contains the suggestion string and the second column the number of hits for the suggestion in the first column. 
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public String[][] searchWebSuggestions(Integer siteId,Integer unitId, final String searchItem, Map safeGuardCookieMap) throws Exception {
		CompassSession session = compass.openSession();
		SearchEngineSpellCheckManager spellCheckManager= compass.getSpellCheckManager();
		SearchEngineSpellCheckSuggestBuilder suggestBuilder=spellCheckManager.suggestBuilder(searchItem);
		suggestBuilder.numberOfSuggestions(20);
		suggestBuilder.accuracy(0.5f);
		SearchEngineSpellSuggestions spellSuggestions =suggestBuilder.suggest();
		String[] results=spellSuggestions.getSuggestions();
		log.info(results.toString());
		String[][] resultValue=new String[NUMBER_OF_SUGGESTIONS][2];
		int max=0;
		for (int i = 0; i < results.length; i++) {
			CompassQuery query = buildRatedWildcardQuery(session, siteId,unitId, results[i], null, safeGuardCookieMap,false);
			CompassHits hits = query.hits();
			if(hits.getLength()>0){
				resultValue[max][0]=results[i];
				resultValue[max][1]=String.valueOf(hits.getLength());
				max++;
			}
			if(max>=NUMBER_OF_SUGGESTIONS){
				break;
			}
		}
		return resultValue;
	}

	public String stripNonValidXMLCharacters(String in) {
		if (in == null) return "";
		String stripped = in.replaceAll("[^\\u0009\\u000a\\u000d\\u0020-\\ud7ff\\e0000-\\ufffd]", "");
		return stripped;
	}

	@SuppressWarnings("unchecked")
	private CompassQuery buildRatedWildcardQuery(CompassSession session, Integer siteId,Integer unitId, String searchItem, String searchUrl, Map safeGuard, boolean isLiveServer) throws IOException, ParseException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Analyzer analyzer = null;
		CompassBooleanQueryBuilder queryBuilder = session.queryBuilder().bool();
		IndexReader ir = LuceneHelper.getLuceneInternalSearch(session).getReader();
		Query query;
		QueryParser parser;
		try {
			String analyzerClass = session.getSettings().getSetting("compass.engine.analyzer.search.type");
			Constructor<Analyzer> analyzerConstructor = (Constructor<Analyzer>) (Class.forName(analyzerClass)).getConstructor();
			analyzer = analyzerConstructor.newInstance();
			if (log.isInfoEnabled()) log.info("Created search analyzer from compass settings - class is: " + analyzer.getClass().getName());
		} catch (Exception e) {
			log.error("Error while instantiating search analyzer from compass settings - going on with StandardAnalyzer");
			analyzer = new StandardAnalyzer();
		}
		if (searchUrl != null) {
			if (log.isDebugEnabled()) log.debug("to search all hosts and subpages attaching * at start and end of urlString...");
			// search on this side and all sub sites on all hosts
			searchUrl = "*" + searchUrl + "*";
			if (log.isDebugEnabled()) log.debug("urlString before parsing: " + searchUrl);
			parser = new QueryParser("url", analyzer);
			parser.setAllowLeadingWildcard(true);
			query = parser.parse(searchUrl).rewrite(ir);
			queryBuilder.addMust(LuceneHelper.createCompassQuery(session, query));
		}
		
		if(unitId!=null){
			query = new QueryParser("unitId", analyzer).parse(unitId.toString());
			queryBuilder.addMust(LuceneHelper.createCompassQuery(session, query));
		} else {
			query = new QueryParser("siteId", analyzer).parse(siteId.toString());
			queryBuilder.addMust(LuceneHelper.createCompassQuery(session, query));
		}
		
		if(isLiveServer){
			query = new QueryParser("isLiveContent", analyzer).parse("true");
			queryBuilder.addMust(LuceneHelper.createCompassQuery(session, query));
		}

		CompassBooleanQueryBuilder subQueryBuilder = session.queryBuilder().bool();
		String searchFields[] = {"metadata", "url", "title", "contents"};
		for (int i = 0; i < searchFields.length; i++) {
			if (i == (searchFields.length - 1) && searchUrl != null) {
				searchItem = searchItem + " " + searchUrl;
			}
			if (log.isDebugEnabled()) {
				log.debug("wildcart query string: " + searchItem);
			}
			parser = new QueryParser(searchFields[i], analyzer);
			parser.setAllowLeadingWildcard(true);
			query = parser.parse(searchItem);
			if (log.isDebugEnabled()) log.debug("wildcart query part: " + query.toString());
			query = query.rewrite(ir);
			query.setBoost(searchFields.length - i);
			if (log.isDebugEnabled()) log.debug("wildcart query part - rewritten: " + query.toString());

			subQueryBuilder.addShould(LuceneHelper.createCompassQuery(session, query));
		}
		queryBuilder.addMust(subQueryBuilder.toQuery());
		subQueryBuilder = session.queryBuilder().bool();
		if (safeGuard != null && !safeGuard.keySet().isEmpty()) {
			if (log.isDebugEnabled()) log.debug("Safeguard found - adding realms to query.");
			Iterator it = safeGuard.keySet().iterator();
			while (it.hasNext()) {
				String key = new String(((String) it.next()).getBytes("ISO-8859-1"));
				if (key.trim().equalsIgnoreCase("")) continue;
				if (log.isDebugEnabled()) log.debug("adding realm: " + key);
				parser = new QueryParser("realm", analyzer);
				query = parser.parse(key);
				subQueryBuilder.addShould(LuceneHelper.createCompassQuery(session, query));
			}
			// adding null representation to find pages without security
			parser = new QueryParser("realm", analyzer);
			query = parser.parse(Constants.SEARCH_INDEX_NULL);
			subQueryBuilder.addShould(LuceneHelper.createCompassQuery(session, query));
			if (subQueryBuilder.toQuery() != null) queryBuilder.addMust(subQueryBuilder.toQuery());
		}
		return queryBuilder.toQuery();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void indexPage(Integer contentId) {
		ContentHbm content = getContentHbmDao().load(contentId);
		ContentVersionHbm contentVersion = content.getLastContentVersion();
		ContentVersionHbm contentLiveVersion = content.getContentVersionForPublish();
		if (contentVersion == null && contentLiveVersion == null) {
			log.error("ContentVersion not existing for content: " + content.getContentId());
			return;
		}
		if((contentVersion!=null && contentVersion.getLock()!=null) || (contentLiveVersion!=null && contentLiveVersion.getLock()!=null) ){
			log.error("Skipping index to avoid deadlock. ContentVersion is locked: " + content.getContentId());
			return;
		}
		String contentText = null;
		String contentLiveText = null;
		if (contentVersion != null) contentText = contentVersion.getText();
		if (contentLiveVersion != null) contentLiveText = contentLiveVersion.getText();

		Collection vclColl = getViewComponentHbmDao().findByReferencedContent(content.getContentId().toString());
		Iterator it = vclColl.iterator();
		while (it.hasNext()) {
			ViewComponentHbm viewComponent = (ViewComponentHbm) it.next();
			log.info("indexing content for referenced vc id "+viewComponent.getViewComponentId()+"("+viewComponent.getDisplayLinkName()+")");
			if (log.isDebugEnabled()) log.debug("Updating Indexes for VCID " + viewComponent.getDisplayLinkName());
			boolean hasLivePreview = (getLiveUrl(viewComponent) != null);
//			boolean hasLivePreview = (viewComponent.getViewDocument().getSite().getPreviewUrlLiveServer() != null);
			boolean hasWorkPreview = (viewComponent.getViewDocument().getSite().getPreviewUrlWorkServer() != null);
			if (viewComponent.isSearchIndexed()) {
				if (hasWorkPreview && contentText != null) this.indexPage4Lucene(viewComponent, contentText, false);
				if (hasLivePreview && contentLiveText != null) this.indexPage4Lucene(viewComponent, contentLiveText, true);
			} else {
				if (hasLivePreview) searchengineDeleteService.deletePage4Lucene(viewComponent, true);
				if (hasWorkPreview) searchengineDeleteService.deletePage4Lucene(viewComponent, false);
			}
			if (viewComponent.isXmlSearchIndexed()) {
				if (contentText != null)
					this.indexPage4Xml(viewComponent, contentText);
				else
					this.indexPage4Xml(viewComponent, contentLiveText);
			} else {
				searchengineDeleteService.deletePage4Xml(viewComponent);
			}

			Collection referencingViewComponents = getViewComponentHbmDao().findByReferencedViewComponent(viewComponent.getViewComponentId().toString());
			Iterator itRef = referencingViewComponents.iterator();
			while (itRef.hasNext()) {
				ViewComponentHbm refViewComponent = (ViewComponentHbm) itRef.next();
				log.info("indexing content for referencing vc id "+refViewComponent.getViewComponentId()+"("+refViewComponent.getDisplayLinkName()+")");
				hasLivePreview = (getLiveUrl(refViewComponent) != null);
//				hasWorkPreview = (refViewComponent.getViewDocument().getSite().getPreviewUrlLiveServer() != null);
				hasWorkPreview = (refViewComponent.getViewDocument().getSite().getPreviewUrlWorkServer() != null);

				if (refViewComponent.getViewType() == Constants.VIEW_TYPE_SYMLINK) {
					// acts as normal content
					if (log.isDebugEnabled()) log.debug("trying to index symLink " + refViewComponent.getDisplayLinkName());
					if (refViewComponent.isSearchIndexed()) {
						if (hasLivePreview && contentLiveText != null) this.indexPage4Lucene(refViewComponent, contentText, true);
						if (hasWorkPreview && contentText != null) this.indexPage4Lucene(refViewComponent, contentLiveText, false);
					} else {
						if (hasLivePreview) searchengineDeleteService.deletePage4Lucene(refViewComponent, true);
						if (hasWorkPreview) searchengineDeleteService.deletePage4Lucene(refViewComponent, false);
					}
					if (refViewComponent.isXmlSearchIndexed()) {
						if (contentText != null)
							this.indexPage4Xml(refViewComponent, contentText);
						else
							this.indexPage4Xml(refViewComponent, contentLiveText);
					} else {
						searchengineDeleteService.deletePage4Xml(refViewComponent);
					}
				}

				/* Die Referenzen im Tree KÖNNEN nur über "findByReferencedViewComponent"
				   gefunden werden, und nicht über die XML Suchmaschine.
				*/
			}
		}
		content.setUpdateSearchIndex(false);
	}

	private void indexPage4Lucene(ViewComponentHbm viewComponent, String contentText, boolean isLive) {
		if (log.isDebugEnabled()) log.debug("Lucene-Index create / update for VC " + viewComponent.getViewComponentId());
		ViewDocumentHbm vdl = viewComponent.getViewDocument();
		CompassSession session = null;
		CompassTransaction tx = null;
		File file = null;
		try {
			String currentUrl =null;
			if(isLive){
				currentUrl=getLiveUrl(viewComponent);
			} else {
				currentUrl= searchengineDeleteService.getUrl(viewComponent, isLive);
			}
			file = this.downloadFile(currentUrl);
			if (file != null) {
				String cleanUrl = viewComponent.getViewDocument().getSite().getPageNameSearch();
				// cut of host name too - since it could be an other one
				//String cleanUrl = viewComponent.getViewDocument().getLanguage() + "/" + viewComponent.getPath();

				cleanUrl = currentUrl.substring(0, currentUrl.length() - cleanUrl.length());
				session = compass.openSession();
				Resource resource = htmlResourceLocator.getResource(session, file, cleanUrl, new Date(System.currentTimeMillis()), viewComponent, vdl);
				resource.addProperty("isLiveContent", isLive);
				tx = session.beginTransaction();
				session.save(resource);
				tx.commit();
				session.close();
				session = null;
			} else {
				log.warn("Critical Error during indexPage4Lucene - cound not find Ressource: " + currentUrl);
			}

		} catch (Exception e) {
			log.warn("Error indexPage4Lucene, VCID " + viewComponent.getViewComponentId().toString() + ": " + e.getMessage(), e);
			if (tx != null) tx.rollback();
		} finally {
			if (session != null) session.close();
			//delete temp file		
			if (file != null) {
				file.delete();
			}
		}
		if (log.isDebugEnabled()) log.debug("finished indexPage4Lucene");
	}

	private void indexPage4Xml(ViewComponentHbm viewComponent, String contentText) {
		if (log.isDebugEnabled()) log.debug("XML-Index create / update for VC " + viewComponent.getViewComponentId());
		ViewDocumentHbm vdl = viewComponent.getViewDocument();
		SiteHbm site = vdl.getSite();
		org.w3c.dom.Document wdoc = null;
		try {
			wdoc = XercesHelper.string2Dom(contentText);
		} catch (Throwable t) {
		}
		if (wdoc != null) {

			HashMap<String, String> metaAttributes = new HashMap<String, String>();

			if (log.isDebugEnabled()) {
				log.debug("SearchUpdateMessageBeanImpl.indexVC(...) -> infoText = " + viewComponent.getLinkDescription());
				log.debug("SearchUpdateMessageBeanImpl.indexVC(...) -> text = " + viewComponent.getDisplayLinkName());
				log.debug("SearchUpdateMessageBeanImpl.indexVC(...) -> unitId = " + viewComponent.getUnit4ViewComponent());
			}

			metaAttributes.put("infoText", viewComponent.getLinkDescription());
			metaAttributes.put("text", viewComponent.getDisplayLinkName());
			metaAttributes.put("unitId", (viewComponent.getUnit4ViewComponent() == null ? "" : viewComponent.getUnit4ViewComponent().toString()));

			xmlDb.saveXml(site.getSiteId(), viewComponent.getViewComponentId(), contentText, metaAttributes);
		}
		if (log.isDebugEnabled()) log.debug("finished indexPage4Xml");
	}

	private File downloadFile(String strUrl) {
		try {
			File file = File.createTempFile("indexingTempFile", "html");
			file.deleteOnExit();

			client.getParams().setConnectionManagerTimeout(20000);

			HttpMethod method = new GetMethod(strUrl);
			method.setFollowRedirects(true);
			method.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, true);
			for (int i = 0; i <= 3; i++) {
				if (i == 3) {
					log.error("Trying to fetch the URL " + strUrl + " three times with no success - page could not be loaded in searchengine!");
					return null;
				}
				if (log.isDebugEnabled()) log.debug("Trying " + i + " to catch the URL");
				if (this.downloadToFile(method, file, strUrl)) {
					if (log.isDebugEnabled()) log.debug("got it!");
					break;
				}
			}
			if (log.isDebugEnabled()) log.debug("downloadFile: FINISHED");
			return file;
		} catch (Exception exe) {
			return null;
		}
	}

	private boolean downloadToFile(HttpMethod method, File file, String strUrl) {
		boolean retVal = false;
		try {
			client.executeMethod(method);
			String responseBody=method.getResponseBodyAsString();
			OutputStream out = new FileOutputStream(file);
			Closeable streamOut = out;
			
			try {
				out.write(responseBody.getBytes());
			} finally {
				if (streamOut != null) {
					try {
						streamOut.close();
					} catch (IOException e) {
					}
				}
			}
			retVal = true;
		} catch (HttpException he) {
			log.error("Http error connecting to '" + strUrl + "'");
			log.error(he.getMessage());
		} catch (IOException ioe) {
			log.error("Unable to connect to '" + strUrl + "'");
		}
		//clean up the connection resources
		method.releaseConnection();
		return retVal;
	}

	public void indexDocument(Integer documentId) {
		if (log.isDebugEnabled()) {
			log.debug("Index create / update for Document: " + documentId);
		}
		DocumentHbm document = getDocumentHbmDao().load(documentId);
		if (log.isDebugEnabled()) {
			log.debug("Document " + document.getDocumentId() + " \"" + document.getDocumentName() + "\" \"" + document.getMimeType());
		}
		if (!documentResourceLocatorFactory.isSupportedFileFormat(document.getMimeType())) {
			if (log.isInfoEnabled()) log.info("Document " + document.getDocumentId() + " \"" + document.getDocumentName() + "\" \"" + document.getMimeType() + "\" is not supported, skipping...");
			document.setUpdateSearchIndex(false);
			return;
		}
		CompassSession session = null;
		CompassTransaction tx = null;
		try {
			session = compass.openSession();
			Resource resource = documentResourceLocatorFactory.getResource(session, document);
			tx = session.beginTransaction();
			session.save(resource);
			tx.commit();
			session.close();
			session = null;
		} catch (Exception e) {
			log.warn("Error indexDocument " + document.getDocumentId().toString() + ": " + e.getMessage());
			if (tx != null) tx.rollback();
		} finally {
			document.setUpdateSearchIndex(false);
			if (session != null) session.close();
		}
		if (log.isInfoEnabled()) log.info("finished indexDocument " + document.getDocumentId() + " \"" + document.getDocumentName() + "\"");
	}
	
	public String getLiveUrl(ViewComponentHbm viewComponent) {
		SiteHbm siteHbm=viewComponent.getViewDocument().getSite();
		Collection<HostHbm> hostHbmCollection=getHostHbmDao().findAll(siteHbm.getSiteId());
		String url = "";
		for (HostHbm hostHbm : hostHbmCollection) {
			if(hostHbm.isLiveserver()){
				url+="http://"+hostHbm.getHostName()+"/";
				break;
			}
		}
		if (url.isEmpty()) {
			return null;
		} else {
			url += viewComponent.getViewDocument().getLanguage() + "/" + viewComponent.getPath() + "." + siteHbm.getPageNameSearch();
			if (log.isInfoEnabled()) log.info("created url " + url + " for site " + siteHbm.getName());
			return url;
		}
	}

}
