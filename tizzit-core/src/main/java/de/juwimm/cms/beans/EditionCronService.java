package de.juwimm.cms.beans;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.beans.foreign.TizzitPropertiesBeanSpring;
import de.juwimm.cms.common.Constants.LiveserverDeployStatus;
import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.model.EditionHbm;
import de.juwimm.cms.model.EditionHbmDao;
import de.juwimm.cms.remote.ContentServiceSpring;
import de.juwimm.cms.remote.EditionServiceSpring;

public class EditionCronService {
	private static Logger log = Logger.getLogger(EditionCronService.class);
	private boolean cronEditionImportIsRunning = false;
	private boolean cronEditionDeployIsRunning = false;
	private boolean updateDeployStatusIsRunning = false;

	private EditionHbmDao editionHbmDao;
	private EditionServiceSpring editionServiceSpring;
	private ContentServiceSpring contentServiceSpring;
	@Autowired
	TizzitPropertiesBeanSpring tizzitProperties;

	/**
	 * @see de.juwimm.cms.remote.EditionServiceSpring#processFileImport(java.lang.Integer, java.lang.String, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public void cronEditionImport() throws Exception {
		if (cronEditionImportIsRunning) {
			if (log.isInfoEnabled()) log.info("Cron already running, ignoring crontask");
			return;
		}
		if (log.isInfoEnabled()) log.info("start cronEditionImport");
		cronEditionImportIsRunning = true;
		try {
			Collection<EditionHbm> editionsToImport = getEditionHbmDao().findByNeedsImport(true);
			if (log.isInfoEnabled()) log.info("Found " + editionsToImport.size() + " Editions to import");
			for (EditionHbm edition : editionsToImport) {
				File edFile = new File(edition.getEditionFileName());
				if (!edFile.exists()) {
					log.warn("Edition " + edition.getEditionId() + " will be deleted because the editionfile isnt available anymore " + edition.getEditionFileName());
				} else {
					UserHbm creator = edition.getCreator();
					SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(creator.getUserId(), creator.getPasswd()));
					//null for viewcomponentID for a complete Import?
					//getEditionServiceSpring().importEdition(edition.getSiteId(), edition.getEditionFileName(), edition.getViewComponentId(), false);
					getEditionServiceSpring().importEdition(edition.getSiteId(), edition.getEditionId(), edition.getEditionFileName(), null, false);
				}
				getEditionServiceSpring().removeEdition(edition.getEditionId());
			}
		} catch (Exception e) {
			log.error("Error importing Edition during crontask", e);
			throw new RuntimeException(e);
		} finally {
			cronEditionImportIsRunning = false;
		}
	}

	/**
	 * @see de.juwimm.cms.remote.EditionServiceSpring#processFileImport(java.lang.Integer, java.lang.String, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public void cronEditionDeploy() throws Exception {
		if (cronEditionDeployIsRunning) {
			if (log.isInfoEnabled()) log.info("Cron already running, ignoring crontask");
			return;
		}
		if (log.isInfoEnabled()) log.info("start cronEditionDeploy");
		cronEditionDeployIsRunning = true;
		try {
			Collection<EditionHbm> editionsToDeploy = getEditionHbmDao().findByNeedsDeploy(true);
			if (log.isInfoEnabled()) log.info("Found " + editionsToDeploy.size() + " Deploy-Editions to publish");
			for (EditionHbm edition : editionsToDeploy) {
				String fileName = edition.getEditionFileName();
				File edFile = null;
				if (fileName != null) {
					edFile = new File(fileName);
				}
				if (edFile == null || !edFile.exists()) {
					//create deploy than
					getContentServiceSpring().deployEdition(edition.getEditionId());
				}
				UserHbm creator = edition.getCreator();
				SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(creator.getUserId(), creator.getPasswd()));
				getEditionServiceSpring().publishEditionToLiveserver(edition.getEditionId());
				if (edFile != null) {
					edFile.delete();
				}
			}
			editionsToDeploy.clear();
		} catch (Exception e) {
			log.error("Error deploying Edition during crontask", e);
			throw new RuntimeException(e);
		} finally {
			cronEditionDeployIsRunning = false;
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	public void logEditionStatusException(Integer editionId, String errorMessage) {
		EditionHbm edition = getEditionHbmDao().load(editionId);
		edition.setExceptionMessage(errorMessage);
		edition.setEndActionTimestamp(System.currentTimeMillis());
		getEditionHbmDao().update(edition);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	public void logEditionStatusInfo(LiveserverDeployStatus status, Integer editionId) {
		String statusValue = status.name();
		EditionHbm edition = getEditionHbmDao().load(editionId);
		if (status == LiveserverDeployStatus.FileDeployedOnLiveServer) {
			edition.setNeedsDeploy(false);
		}
		edition.setDeployStatus(statusValue.getBytes());
		edition.setStartActionTimestamp(System.currentTimeMillis());
		edition.setEndActionTimestamp(null);
		getEditionHbmDao().update(edition);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void cronUpdateDeployStatus() {
		if (!updateDeployStatusIsRunning && !tizzitProperties.isLiveserver()) {
			log.info("start cron update deploy statuses");
			updateDeployStatusIsRunning = true;
			List<EditionHbm> editions = (List<EditionHbm>) getEditionHbmDao().findDeployed();
			try {
				getEditionServiceSpring().updateDeployStatus(editions);
			} catch (UserException e) {
				log.warn("update deploy status from liveserver crashed");
				if (log.isDebugEnabled()) {
					log.debug(e);
				}
			} finally {
				editions.clear();
				updateDeployStatusIsRunning = false;
			}
		}
	}

	public void setEditionHbmDao(EditionHbmDao editionHbmDao) {
		this.editionHbmDao = editionHbmDao;
	}

	public EditionHbmDao getEditionHbmDao() {
		return editionHbmDao;
	}

	public void setEditionServiceSpring(EditionServiceSpring editionServiceSpring) {
		this.editionServiceSpring = editionServiceSpring;
	}

	public EditionServiceSpring getEditionServiceSpring() {
		return editionServiceSpring;
	}

	public void setContentServiceSpring(ContentServiceSpring contentServiceSpring) {
		this.contentServiceSpring = contentServiceSpring;
	}

	public ContentServiceSpring getContentServiceSpring() {
		return contentServiceSpring;
	}

}
