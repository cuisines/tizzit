package de.juwimm.cms.beans;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.SiteHbmDao;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
@Transactional
public class ExternalSitesCronService {
	private static Logger log = Logger.getLogger(ExternalSitesCronService.class);

	SiteHbmDao siteHbmDao;

	WebCrawlerService webCrawler;

	boolean cronIsRunning = false;

	public WebCrawlerService getWebCrawler() {
		return webCrawler;
	}

	public void setWebCrawler(WebCrawlerService webCrawler) {
		this.webCrawler = webCrawler;
	}

	public SiteHbmDao getSiteHbmDao() {
		return siteHbmDao;
	}

	public void setSiteHbmDao(SiteHbmDao siteHbmDao) {
		this.siteHbmDao = siteHbmDao;
	}

	public void indexSites() {
		if (cronIsRunning) {
			log.info("ExternalSitesCronService: cron is running");
			return;
		}
		cronIsRunning = true;
		log.info("ExternalSitesCronService: starting index external sites");

		List<SiteHbm> externalSites = getExternalSites();

		if (externalSites == null || externalSites.size() == 0) {
			cronIsRunning = false;
			return;
		}

		for (SiteHbm externalSite : externalSites) {
			webCrawler.indexSite(externalSite);
			externalSite.setUpdateSiteIndex(false);
		}
		cronIsRunning = false;
		log.info("ExternalSitesCronService: end index end sites");
	}

	private List<SiteHbm> getExternalSites() {
		List<SiteHbm> sites = (List<SiteHbm>) siteHbmDao.findAll();
		List<SiteHbm> externalSites = new ArrayList<SiteHbm>();

		if (sites == null || sites.size() == 0) {
			return externalSites;
		}

		for (SiteHbm site : sites) {
			//check if it is an external site and if it is scheduled for indexing
			if (site.getExternalSiteSearch() != null && site.getExternalSiteSearch() && site.getUpdateSiteIndex() != null && site.getUpdateSiteIndex()) {
				externalSites.add(site);
			}
		}

		return externalSites;
	}
}
