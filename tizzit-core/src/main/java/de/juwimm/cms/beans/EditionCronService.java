package de.juwimm.cms.beans;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.juwimm.cms.authorization.model.UserHbmDao;
import de.juwimm.cms.beans.foreign.TizzitPropertiesBeanSpring;
import de.juwimm.cms.common.Constants.LiveserverDeployStatus;
import de.juwimm.cms.model.EditionHbm;
import de.juwimm.cms.model.EditionHbmDao;
import de.juwimm.cms.remote.ContentServiceSpring;
import de.juwimm.cms.remote.EditionServiceSpring;

public class EditionCronService {
	private static Logger log = Logger.getLogger(EditionCronService.class);

	private EditionHbmDao editionHbmDao;
	private EditionServiceSpring editionServiceSpring;
	private ContentServiceSpring contentServiceSpring;
	private UserHbmDao userHbmDao;
	@Autowired
	TizzitPropertiesBeanSpring tizzitProperties;

	

	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_UNCOMMITTED)
	public void logEditionStatusException(Integer editionId, String errorMessage) {
		EditionHbm edition = getEditionHbmDao().load(editionId);
		edition.setExceptionMessage(errorMessage);
		edition.setEndActionTimestamp(System.currentTimeMillis());
		getEditionHbmDao().update(edition);
	}

	//	possible values for status
	//	EditionCreated, 
	//	CreateDeployFileForExport, TransmitDeployFile, 
	//	FileDeployedOnLiveServer, ImportStarted, 
	//	ImportCleanDatabase, ImportUnits, ImportResources, 
	//	ImportDatabaseComponents, ImportViewComponents, 
	//	ImportHosts, ImportSuccessful, Exception;

	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_UNCOMMITTED)
	public void logEditionStatusInfo(LiveserverDeployStatus status, Integer editionId) {
		String statusValue = status.name();
		EditionHbm edition = getEditionHbmDao().load(editionId);
		edition.setDeployStatus(statusValue.getBytes());
		edition.setStartActionTimestamp(System.currentTimeMillis());
		edition.setEndActionTimestamp(null);
		getEditionHbmDao().update(edition);
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

	public UserHbmDao getUserHbmDao() {
		return userHbmDao;
	}

	public void setUserHbmDao(UserHbmDao userHbmDao) {
		this.userHbmDao = userHbmDao;
	}
}
