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
import org.compass.core.Compass;
import org.compass.core.CompassSession;
import org.compass.core.CompassTransaction;
import org.compass.core.Resource;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.search.res.HtmlResourceLocator;
import de.juwimm.cms.search.res.PDFResourceLocator;
import de.juwimm.cms.search.res.RTFResourceLocator;
import de.juwimm.cms.search.res.WordResourceLocator;
import de.juwimm.cms.util.SmallSiteConfigReader;
import de.juwimm.cms.util.AbstractCrawlUrlStrategy.FilterCrawlUrlStrategy;
import de.juwimm.cms.util.AbstractCrawlUrlStrategy.ProtocolCrawlUrlStrategy;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class WebCrawlerService {

	private static Logger log = Logger.getLogger(WebCrawlerService.class);
	private final HttpClient httpClient = new HttpClient();
	private static String HTML_CONTENT_TYPE = "text/html";

	@Autowired
	private HtmlResourceLocator htmlResourceLocator;
	@Autowired
	private PDFResourceLocator pdfResourceLocator;
	@Autowired
	private RTFResourceLocator rtfResourceLocator;
	@Autowired
	private WordResourceLocator wordResourceLocator;
	private Map<Integer, Set<String>> alreadyIndexedPages;
	/**
	 * Used for broken links and non html pages
	 */
	private Set<String> alreadyIndexedNonHtmlPages;
	private FilterCrawlUrlStrategy filtersStrategy;
	private ProtocolCrawlUrlStrategy protocolsStrategy;
	private int baseDepth;

	@Autowired
	private Compass compass;

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
		CompassSession session = null;
		CompassTransaction tx = null;
		session = compass.openSession();
		InputStream in;
		try {
			in = httpMethod.getResponseBodyAsStream();

			Resource resource = null;
			if (contentType.contains(PDFResourceLocator.MIME_TYPE)) {
				resource = pdfResourceLocator.getExternalResource(session, url, in);
			} else if (contentType.contains(RTFResourceLocator.MIME_TYPE)) {
				resource = rtfResourceLocator.getExternalResource(session, url, in);
			} else if (contentType.contains(WordResourceLocator.MIME_TYPE)) {
				resource = wordResourceLocator.getExternalResource(session, url, in);
			}

			if (resource != null) {
				tx = session.beginTransaction();
				session.save(resource);
				tx.commit();
				session.close();
				session = null;
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
			if (tx != null) tx.rollback();
		} finally {
			if (session != null) session.close();
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
		CompassSession session = null;
		CompassTransaction tx = null;
		try {
			session = compass.openSession();
			Resource resource = htmlResourceLocator.getExternalResource(session, url, parseHtmlReader);
			if (resource != null) {
				tx = session.beginTransaction();
				session.save(resource);
				tx.commit();
				session.close();
				session = null;
			}
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("Error index url " + url + " : " + e.getMessage());
			}
			if (tx != null) tx.rollback();
		} finally {
			if (session != null) session.close();
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
			//TODO http://www.juwimm.net/img/ejbfile/Share%20of%20Voice.pdf?id=12142 does not work. 
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
					if (t == HTML.Tag.A && a.getAttribute(HTML.Attribute.HREF) != null
					//TODO && isLinkValid(a.getAttribute(HTML.Attribute.HREF).toString())
					) {
						childUrls.add(a.getAttribute(HTML.Attribute.HREF).toString());
					}
				}

				@Override
				public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {
					if (t == HTML.Tag.A && a.getAttribute(HTML.Attribute.HREF) != null
					//TODO && isLinkValid(a.getAttribute(HTML.Attribute.HREF).toString())
					) {
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
