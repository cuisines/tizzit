package de.juwimm.cms.beans;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.juwimm.cms.beans.foreign.CqPropertiesBeanSpring;
import de.juwimm.cms.model.ContentHbm;
import de.juwimm.cms.model.ContentHbmDao;
import de.juwimm.cms.model.DocumentHbm;
import de.juwimm.cms.model.DocumentHbmDao;
import de.juwimm.cms.search.beans.SearchengineService;

@Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.DEFAULT)
public class SearchCronService {
	private static Log log = LogFactory.getLog(SearchCronService.class);
	private boolean cronIsRunning = false;

	private SearchengineService searchengineService;
	private CqPropertiesBeanSpring cqPropertiesBeanSpring;
	private ContentHbmDao contentHbmDao;
	private DocumentHbmDao documentHbmDao;

	@SuppressWarnings("unchecked")
	public void cronRunSearchIndexer() throws Exception {
		if(!cqPropertiesBeanSpring.getSearch().isIndexerEnabled()) {
			log.info("Cron SearchIndex has been invoked but indexer is disabled");
			return;
		}
		if(cronIsRunning) {
			log.info("Cron already running, ignoring crontask");
			return;
		}
		log.info("Cronjob SearchIndex has been started");
		cronIsRunning = true;

		Collection<ContentHbm> contentsToUpdate = contentHbmDao.findByUpdateSearchIndex(true);
		log.info("Found " + contentsToUpdate.size() + " Contents to update");
		for (ContentHbm content : contentsToUpdate) {
			searchengineService.indexPage(content.getContentId());
		}

		Collection<DocumentHbm> documentsToUpdate = documentHbmDao.findByUpdateSearchIndex(true);
		log.info("Found " + documentsToUpdate.size() + " Documents to update");
		for (DocumentHbm doc : documentsToUpdate) {
			searchengineService.indexDocument(doc.getDocumentId());
		}
		cronIsRunning = false;
	}
	
	public void setSearchengineService(SearchengineService searchengineService) {
		this.searchengineService = searchengineService;
	}

	public void setCqPropertiesBeanSpring(CqPropertiesBeanSpring cqPropertiesBeanSpring) {
		this.cqPropertiesBeanSpring = cqPropertiesBeanSpring;
	}

	public void setContentHbmDao(ContentHbmDao contentHbmDao) {
		this.contentHbmDao = contentHbmDao;
	}

	public void setDocumentHbmDao(DocumentHbmDao documentHbmDao) {
		this.documentHbmDao = documentHbmDao;
	}

}
