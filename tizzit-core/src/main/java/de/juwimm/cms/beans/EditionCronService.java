package de.juwimm.cms.beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tizzit.util.XercesHelper;
import org.tizzit.util.xml.XMLWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.beans.foreign.TizzitPropertiesBeanSpring;
import de.juwimm.cms.common.Constants.LiveserverDeployStatus;
import de.juwimm.cms.model.EditionHbm;
import de.juwimm.cms.model.EditionHbmDao;
import de.juwimm.cms.remote.ContentServiceSpring;
import de.juwimm.cms.remote.EditionServiceSpring;
import de.juwimm.cms.util.EditionBlobContentHandler;

public class EditionCronService {
	private static Logger log = Logger.getLogger(EditionCronService.class);
	private boolean cronEditionImportIsRunning = false;
	private boolean cronEditionDeployIsRunning = false;
	private boolean cronCreateEditionFromDeployFileIsRunning = false;
	private boolean updateDeployStatusIsRunning = false;
	private Document editionDoc = null;

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
					if (creator == null) {
						Element elm = null;
						if (editionDoc == null) {
							elm = getElementFromXmlInFile(edFile.getAbsolutePath(), "/edition/site");
						} else {
							elm = (Element) XercesHelper.findNode(editionDoc, "/edition/site");
						}
						String config = getNVal(elm, "siteConfig");
						Document doc = XercesHelper.string2Dom(config);
						String user = XercesHelper.getNodeValue(doc, "/config/default/liveServer/username");
						String pass = XercesHelper.getNodeValue(doc, "/config/default/liveServer/password");
						SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, pass));
					} else {
						SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(creator.getUserId(), creator.getPasswd()));
					}
					//null for viewcomponentID for a complete Import?
					//					getEditionServiceSpring().importEdition(edition.getSiteId(), edition.getEditionId(), edition.getEditionFileName(), edition.getViewComponentId(), false);
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

	private Element getElementFromXmlInFile(String file, String path) {
		try {
			if (log.isInfoEnabled()) log.info("Finished writing Edition to File, starting to import it as GZIP-InputStream...");
			//			XMLFilter filter = new XmlElementPositivFilter(XMLReaderFactory.createXMLReader(), "/edition/edition");
			XMLFilter filter = new XMLFilterImpl(XMLReaderFactory.createXMLReader());
			File preparsedXMLfile = File.createTempFile("edition_import_preparsed_", ".xml");
			if (log.isDebugEnabled()) log.debug("preparsedXMLfile: " + preparsedXMLfile.getAbsolutePath());
			XMLWriter xmlWriter = new XMLWriter(new OutputStreamWriter(new FileOutputStream(preparsedXMLfile)));
			filter.setContentHandler(new EditionBlobContentHandler(xmlWriter, preparsedXMLfile));
			InputSource saxIn = null;
			try {
				try {
					saxIn = new InputSource(new GZIPInputStream(new FileInputStream(file)));
				} catch (Exception exe) {
					saxIn = new InputSource(new BufferedReader(new FileReader(file)));
				}
			} catch (FileNotFoundException exe) {
				log.error("Edition file isnt available anymore. Edition needs to be deleted!");
			}
			filter.parse(saxIn);
			xmlWriter.flush();
			xmlWriter = null;
			filter = null;
			System.gc();
			if (log.isInfoEnabled()) log.info("Finished cutting BLOBs, starting to open XML Document...");
			InputSource domIn = new InputSource(new FileInputStream(preparsedXMLfile));
			editionDoc = XercesHelper.inputSource2Dom(domIn);
			return (Element) XercesHelper.findNode(editionDoc, path);
		} catch (IOException ioe) {
			log.warn("Error while parsing EditionFile." + ioe.getStackTrace());
		} catch (SAXException e) {
			log.warn("Error while parsing EditionFile." + e.getStackTrace());
		} catch (Exception e) {
			log.warn("Error while parsing EditionFile." + e.getStackTrace());
		}
		return null;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void cronCreateEditionFromDeployFile() {
		if (!tizzitProperties.isLiveserver()) {
			return;
		}
		if (cronCreateEditionFromDeployFileIsRunning) {
			if (log.isInfoEnabled()) log.info("Cron already running, ignoring crontask");
			return;
		}
		if (log.isInfoEnabled()) log.info("start cronCreateEditionFromDeployFile");
		cronCreateEditionFromDeployFileIsRunning = true;
		File deployDir = new File(tizzitProperties.getDeployDir());
		File[] files = null;
		if (deployDir != null && deployDir.exists() && deployDir.isDirectory()) {
			files = deployDir.listFiles();
		} else {
			log.warn("Error trying to open deployDir - " + deployDir);
		}
		if (files == null || files.length == 0) {
			if (log.isInfoEnabled()) log.info("No deploys to import found.");
			cronCreateEditionFromDeployFileIsRunning = false;
			return;
		}
		Collection<EditionHbm> editionsToImport = getEditionHbmDao().findByNeedsImport(true);
		for (int i = 0; i < files.length; i++) {
			boolean found = false;
			for (EditionHbm edition : editionsToImport) {
				if (edition.getEditionFileName().compareTo(files[i].getAbsolutePath()) == 0) {
					found = true;
					break;
				}
			}
			if (!found) {
				createEditionFromDeployFile(files[i].getAbsolutePath());
			}
		}
		cronCreateEditionFromDeployFileIsRunning = false;
		if (log.isInfoEnabled()) log.info("finished cronCreateEditionFromDeployFile");
	}

	private EditionHbm createEditionFromDeployFile(String editionFileName) {
		EditionHbm edition = null;
		Element node = getElementFromXmlInFile(editionFileName, "/edition/edition");

		if (node != null) {
			edition = EditionHbm.Factory.newInstance();
			edition.setComment(getNVal(node, "comment"));
			edition.setCreationDate(new Long(getNVal(node, "creationDate")).longValue());
			//			edition.setCreator(new Integer(getNVal(node, "creatorId")));
			edition.setEditionFileName(editionFileName);
			edition.setNeedsImport(true);
			edition.setSiteId(new Integer(getNVal(node, "siteId")));
			edition.setUnitId(new Integer(getNVal(node, "unitId")));
			edition.setUseNewIds(true);
			edition.setViewDocumentId(new Integer(getNVal(node, "viewDocumentId")));
			edition.setViewComponentId(new Integer(getNVal(node, "viewComponentId")));
			edition.setWorkServerEditionId(new Integer(getNVal(node, "id")));
			String user = tizzitProperties.getDeployUser();
			String pass = tizzitProperties.getDeployPassword();
			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, pass));
			getEditionHbmDao().create(edition);
		} else {
			log.warn("Could not find edition Data in file " + editionFileName);
		}

		return edition;
	}

	private String getNVal(Element ael, String nodeName) {
		String tmp = XercesHelper.getNodeValue(ael, "./" + nodeName);
		if (tmp.equals("null") || tmp.equals("")) {
			return null;
		}
		return tmp;
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
		File edFile = null;
		try {
			Collection<EditionHbm> editionsToDeploy = getEditionHbmDao().findByNeedsDeploy(true);
			if (log.isInfoEnabled()) log.info("Found " + editionsToDeploy.size() + " Deploy-Editions to publish");
			for (EditionHbm edition : editionsToDeploy) {
				String fileName = edition.getEditionFileName();
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
			if (edFile != null && edFile.exists()) {
				edFile.delete();
			}
			throw new RuntimeException(e);
		} finally {
			cronEditionDeployIsRunning = false;
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_UNCOMMITTED)
	public void logEditionStatusException(Integer editionId, String errorMessage) {
		EditionHbm edition = getEditionHbmDao().load(editionId);
		edition.setExceptionMessage(errorMessage);
		edition.setEndActionTimestamp(System.currentTimeMillis());
		getEditionHbmDao().update(edition);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_UNCOMMITTED)
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
			List<EditionHbm> editions = getEditionHbmDao().findDeployed();
			try {
				getEditionServiceSpring().updateDeployStatus(editions);
			} catch (de.juwimm.cms.exceptions.UserException e) {
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
