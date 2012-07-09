package de.juwimm.cms.beans;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.search.lucene.LuceneService;
import de.juwimm.cms.search.res.HtmlDocumentLocator;
import de.juwimm.cms.search.res.PDFDocumentLocator;
import de.juwimm.cms.search.res.RTFDocumentLocator;
import de.juwimm.cms.search.res.WordDocumentLocator;
import de.juwimm.cms.util.AbstractCrawlUrlStrategy.FilterCrawlUrlStrategy;
import de.juwimm.cms.util.AbstractCrawlUrlStrategy.ProtocolCrawlUrlStrategy;
import de.juwimm.cms.util.SmallSiteConfigReader;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class WebCrawlerService {

	private static Logger log = Logger.getLogger(WebCrawlerService.class);
	private final HttpClient httpClient = new HttpClient();
	private static String HTML_CONTENT_TYPE = "text/html";

	@Autowired
	private HtmlDocumentLocator htmlDocumentLocator;
	@Autowired
	private PDFDocumentLocator pdfResourceLocator;
	@Autowired
	private RTFDocumentLocator rtfResourceLocator;
	@Autowired
	private WordDocumentLocator wordResourceLocator;
	private Map<Integer, Set<String>> alreadyIndexedPages;
	/**
	 * Used for broken links and non html pages
	 */
	private Set<String> alreadyIndexedNonHtmlPages;
	private FilterCrawlUrlStrategy filtersStrategy;
	private ProtocolCrawlUrlStrategy protocolsStrategy;
	private int baseDepth;
	
	@Autowired
	private LuceneService luceneService;

	public void indexSite(SiteHbm site) {
		log.info("Crawl and index on site" + site.getSiteId() + "started");
		SmallSiteConfigReader configReader = new SmallSiteConfigReader(site);
		List<String> urls = configReader.readValues(SmallSiteConfigReader.EXTERNAL_SEARCH_URLS_PATH);
		if (urls == null || urls.size() == 0) {
			return;
		}
		List<String> positiveProtocols = configReader.readValues(SmallSiteConfigReader.getPositiveListTag("protocols"));
		List<String> negativeProtocols = configReader.readValues(SmallSiteConfigReader.getNegativeListTag("protocols"));
		List<String> positiveFilters = configReader.readValues(SmallSiteConfigReader.getPositiveListTag("filters"));
		List<String> negativeFilters = configReader.readValues(SmallSiteConfigReader.getNegativeListTag("filters"));

		alreadyIndexedPages = new HashMap<Integer, Set<String>>();
		alreadyIndexedNonHtmlPages = new HashSet<String>();
		filtersStrategy = new FilterCrawlUrlStrategy(positiveFilters, negativeFilters);
		protocolsStrategy = new ProtocolCrawlUrlStrategy(positiveProtocols, negativeProtocols);
		baseDepth = Integer.valueOf(configReader.readValue(SmallSiteConfigReader.EXTERNAL_SEARCH_DEPTH_PATH));
		for (String url : urls) {
			try {
				indexUrl(url, baseDepth, url);
			} catch (Exception e) {
				if (log.isDebugEnabled()) {
					log.debug("failed indexing url:" + url + " - " + e.getMessage());
				}
				continue;
			}
		}
		log.info("Crawl and index on site" + site.getSiteId() + " ended");
	}

	@SuppressWarnings("deprecation")
	private void indexUrl(String url, int depth, String baseUrl) {
		//pages per depth must be indexed only once
		if (!alreadyIndexedPages.containsKey(depth)) {
			alreadyIndexedPages.put(depth, new HashSet<String>());
		} else if (alreadyIndexedPages.get(depth).contains(url) || alreadyIndexedNonHtmlPages.contains(url)) {
			return;
		}

		if (baseDepth != depth) {//no check for first level url
			//check url validity
			if (!filtersStrategy.isUrlValid(url)) {
				//link does not respect the search constraints
				return;
			}
		}
		//configure http request
		httpClient.getParams().setConnectionManagerTimeout(5000);
		HttpMethod httpMethod = null;
		try {
			httpMethod = new GetMethod(escapeUrl(url));
		} catch (IllegalStateException ex) {
			if (log.isDebugEnabled()) {
				log.debug("failed to create http method on url " + url + " with error:" + ex.getMessage());
			}
			if (!url.startsWith(baseUrl)) {
				indexUrl(baseUrl + (url.startsWith("/") ? "" : "/") + url, depth, baseUrl);
			}
			return;
		} catch (IllegalArgumentException ex) {
			if (log.isDebugEnabled()) {
				log.debug("failed to create http method on url " + url + " with error:" + ex.getMessage());
			}
			if (!url.startsWith(baseUrl)) {
				indexUrl(baseUrl + (url.startsWith("/") ? "" : "/") + url, depth, baseUrl);
			}
			return;
		}

		if (httpMethod.getHostConfiguration().getHost() == null) {
			if (!url.startsWith(baseUrl)) {
				indexUrl(baseUrl + (url.startsWith("/") ? "" : "/") + url, depth, baseUrl);
			}
			return;
		}
		if (baseDepth != depth) {//no check for first level url
			//protocol constraints check
			if (!protocolsStrategy.isUrlValid(url)) {
				return;
			}
		}

		httpMethod.setFollowRedirects(true);
		httpMethod.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, true);
		log.info("indexUrl : url " + url + " depth: " + depth);

		try {
			httpClient.executeMethod(httpMethod);
			//index content
			indexContent(httpMethod, url, httpMethod.getHostConfiguration().getHostURL(), depth);
		} catch (IOException e) {
			if (log.isDebugEnabled()) {
				log.debug("request failed to url:" + url + " - " + e.getMessage());
			}
			//not to come second time at this link, if the link is broken
			alreadyIndexedNonHtmlPages.add(url);
			return;
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("request failed to url:" + url + " - " + e.getMessage());
			}
			//not to come second time at this link, if the link is broken
			alreadyIndexedNonHtmlPages.add(url);
			return;
		}

	}

	private String escapeUrl(String url) {
		return url.replaceAll(" ", "%20");
	}

	private void indexContent(HttpMethod httpMethod, String url, String baseUrl, int depth) {
		String contentType = httpMethod.getResponseHeader("Content-Type").getValue();
		//index content
		if (contentType.contains(HTML_CONTENT_TYPE)) {
			indexHtmlResource(httpMethod, url, baseUrl, depth);
		} else {
			indexNonHtmlResource(httpMethod, url, contentType);
		}
	}

	private void indexNonHtmlResource(HttpMethod httpMethod, String url, String contentType) {
		InputStream in;
		try {
			in = httpMethod.getResponseBodyAsStream();

			Document resource = null;
			if (contentType.contains(PDFDocumentLocator.MIME_TYPE)) {
				resource = pdfResourceLocator.getExternalResource( url, in);
			} else if (contentType.contains(RTFDocumentLocator.MIME_TYPE)) {
				resource = rtfResourceLocator.getExternalResource( url, in);
			} else if (contentType.contains(WordDocumentLocator.MIME_TYPE)) {
				resource = wordResourceLocator.getExternalResource( url, in);
			}

			if (resource != null) {
				luceneService.addToIndex(resource);
			}
		} catch (IOException e) {
			if (log.isDebugEnabled()) {
				log.debug("request failed to url:" + url + " - " + e.getMessage());
			}
			return;
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("Error index url " + url + " : " + e.getMessage());
			}
		} finally {
			httpMethod.releaseConnection();
			alreadyIndexedNonHtmlPages.add(url);
		}

	}

	/**
	 * Also this method goes to the next links
	 * @param httpMethod
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	private void indexHtmlResource(HttpMethod httpMethod, String url, String baseUrl, int depth) {
		StringWriter out = new StringWriter();
		InputStream in;
		try {
			in = httpMethod.getResponseBodyAsStream();
		} catch (IOException e) {
			if (log.isDebugEnabled()) {
				log.debug("request failed to url:" + url + " - " + e.getMessage());
			}
			return;
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("request failed to url:" + url + " - " + e.getMessage());
			}
			return;
		}

		try {
			IOUtils.copy(in, out);
			in.close();
		} catch (IOException e) {
			log.debug("failed to copy content of url :" + url + " - " + e.getMessage());
			return;
		}
		StringReader parseHtmlReader = new StringReader(out.toString());
		StringReader findLinksReader = new StringReader(out.toString());

		//index
		try {
			Document resource = htmlDocumentLocator.getExternalResource(url, parseHtmlReader);
			if (resource != null) {
				luceneService.addToIndex(resource);
			}
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("Error index url " + url + " : " + e.getMessage());
			}
		} finally {
		}
		httpMethod.releaseConnection();
		alreadyIndexedPages.get(depth).add(url);
		parseHtmlReader.close();

		if (depth == 0) {
			findLinksReader.close();
			return;
		}
		//find new links
		final List<String> childUrls = extractLinks(findLinksReader, url);

		if (childUrls == null || childUrls.size() == 0) {
			return;
		}
		findLinksReader.close();

		//index deeper
		for (String childUrl : childUrls) {
			//childUrl.replace(" ", "%20");
			indexUrl(childUrl, depth - 1, baseUrl);
		}
	}

	/**
	 * Input reader should be a html content
	 * @param findLinksReader
	 * @param url
	 * @return
	 */
	private final List<String> extractLinks(Reader findLinksReader, String url) {
		final List<String> childUrls = new ArrayList<String>();
		try {
			new ParserDelegator().parse(findLinksReader, new HTMLEditorKit.ParserCallback() {

				@Override
				public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
					if (t == HTML.Tag.A && a.getAttribute(HTML.Attribute.HREF) != null) {
						childUrls.add(a.getAttribute(HTML.Attribute.HREF).toString());
					}
				}

				@Override
				public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {
					if (t == HTML.Tag.A && a.getAttribute(HTML.Attribute.HREF) != null) {
						childUrls.add(a.getAttribute(HTML.Attribute.HREF).toString());
					}
				}
			}, true);
		} catch (IOException e) {
			if (log.isDebugEnabled()) {
				log.debug("error on parsing url: " + url + " trying to get links");
			}
		}
		return childUrls;
	}

}
