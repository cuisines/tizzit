package de.juwimm.cms.search.beans;

import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.ViewComponentHbm;
import de.juwimm.cms.model.ViewDocumentHbm;
import de.juwimm.cms.search.lucene.LuceneService;
import de.juwimm.cms.search.xmldb.XmlDb;

/**
 * Needs to avoid cyclic references
 * @author <a href="mailto:j2ee@juwimm.com">Sascha-Matthias Kulawik</a>
 * company Juwi|MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since cqcms-core 06.07.2009
 */
public class SearchengineDeleteService {
	private static Logger log = Logger.getLogger(SearchengineDeleteService.class);
	@Autowired
	private LuceneService luceneService;
	@Autowired
	private XmlDb xmlDb;

	public void deletePage(ViewComponentHbm viewComponent, boolean isLive) {
		deletePage4Xml(viewComponent);
		deletePage4Lucene(viewComponent, isLive);
	}

	public void deletePage4Xml(ViewComponentHbm viewComponent) {
		if (log.isDebugEnabled()) log.debug("XML-Index delete for VC " + viewComponent.getViewComponentId());
		ViewDocumentHbm viewDocument = viewComponent.getViewDocument();
		SiteHbm site = viewDocument.getSite();
		xmlDb.deleteXml(site.getSiteId(), viewComponent.getViewComponentId());
		if (log.isDebugEnabled()) log.debug("finished deletePage4Xml");
	}

	public void deletePage4Lucene(ViewComponentHbm viewComponent, boolean isLive) {
		if (log.isDebugEnabled()) log.debug("Lucene-Index delete for VC " + viewComponent.getViewComponentId());
		try {
			String currentUrl = getUrl(viewComponent, isLive);
			String cleanUrl = viewComponent.getViewDocument().getSite().getPageNameSearch();
			cleanUrl = currentUrl.substring(0, currentUrl.length() - cleanUrl.length());
			luceneService.removeDocument(new Term("uid", cleanUrl));
		} catch (Exception e) {
			log.warn("Error deletePage4Lucene, maybe this page is not in the index: " + e.getMessage());
			if (log.isDebugEnabled()) log.debug(e);
		} finally {
		}

		if (log.isDebugEnabled()) log.debug("finished deletePage4Lucene");
	}

	public String getUrl(ViewComponentHbm vc, boolean isLive) {
		SiteHbm site = vc.getViewDocument().getSite();
		String url = "";
		if (isLive) {
			url = site.getPreviewUrlLiveServer();
		} else {
			url = site.getPreviewUrlWorkServer();
		}
		if (url != null) {
			url += vc.getViewDocument().getLanguage() + "/" + vc.getPath() + "." + site.getPageNameSearch();
		}
		if (log.isInfoEnabled()) log.info("created url " + url + " for site " + site.getName());
		return url;
	}

	public void deleteDocument(Integer documentId, Integer siteId) {
		if (log.isDebugEnabled()) log.debug("Index delete for Document: " + documentId);
		try {
			luceneService.removeDocument(new Term("uid", documentId.toString()));
		} catch (Exception e) {
			log.warn("Error deleteDocument, maybe this document is not in the index: " + e.getMessage());
			if (log.isDebugEnabled()) log.debug(e);
		} finally {
		}
		if (log.isDebugEnabled()) log.debug("finished deleteDocument");
	}
}
