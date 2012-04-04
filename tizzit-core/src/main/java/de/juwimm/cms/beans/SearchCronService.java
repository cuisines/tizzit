package de.juwimm.cms.beans;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.juwimm.cms.beans.foreign.TizzitPropertiesBeanSpring;
import de.juwimm.cms.model.ContentHbm;
import de.juwimm.cms.model.ContentHbmDao;
import de.juwimm.cms.model.DocumentHbm;
import de.juwimm.cms.model.DocumentHbmDao;
import de.juwimm.cms.search.beans.SearchengineService;

@Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.DEFAULT)
public class SearchCronService {
	private static Logger log = Logger.getLogger(SearchCronService.class);
	private boolean cronIsRunning = false;

	private SearchengineService searchengineService;
	private TizzitPropertiesBeanSpring tizzitPropertiesBeanSpring;
	private ContentHbmDao contentHbmDao;
	private DocumentHbmDao documentHbmDao;

	@SuppressWarnings("unchecked")
	public void cronRunSearchIndexer() throws Exception {
		if (!tizzitPropertiesBeanSpring.getSearch().isIndexerEnabled()) {
			if (log.isInfoEnabled()) log.info("Cron SearchIndex has been invoked but indexer is disabled");
			return;
		}
		if (cronIsRunning) {
			if (log.isInfoEnabled()) log.info("Cron already running, ignoring crontask");
			return;
		}
		if (log.isInfoEnabled()) log.info("Cronjob SearchIndex has been started");
		cronIsRunning = true;

		try {
			Collection<ContentHbm> contentsToUpdate = contentHbmDao.findByUpdateSearchIndex(true);
			if (log.isInfoEnabled()) log.info("Found " + contentsToUpdate.size() + " Contents to update");
			for (ContentHbm content : contentsToUpdate) {
				try {
					searchengineService.indexPage(content.getContentId());
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					if (log.isInfoEnabled()) log.info("Error indexing Content " + content.getContentId() + ": " + e.getMessage());
				}
			}

			Collection<DocumentHbm> documentsToUpdate = documentHbmDao.findByUpdateSearchIndex(true);
			if (log.isInfoEnabled()) log.info("Found " + documentsToUpdate.size() + " Documents to update");
			for (DocumentHbm doc : documentsToUpdate) {
				try {
					searchengineService.indexDocument(doc.getDocumentId());
				} catch (Exception e) {
					if (log.isInfoEnabled()) log.info("Error indexing Document " + doc.getDocumentId() + ": " + e.getMessage());
				}
			}
		} catch (Exception exe) {
			throw exe;
		} finally {
			cronIsRunning = false;
		}
	}

	public void setSearchengineService(SearchengineService searchengineService) {
		this.searchengineService = searchengineService;
	}

	public void setTizzitPropertiesBeanSpring(TizzitPropertiesBeanSpring tizzitPropertiesBeanSpring) {
		this.tizzitPropertiesBeanSpring = tizzitPropertiesBeanSpring;
	}

	public void setContentHbmDao(ContentHbmDao contentHbmDao) {
		this.contentHbmDao = contentHbmDao;
	}

	public void setDocumentHbmDao(DocumentHbmDao documentHbmDao) {
		this.documentHbmDao = documentHbmDao;
	}

}
