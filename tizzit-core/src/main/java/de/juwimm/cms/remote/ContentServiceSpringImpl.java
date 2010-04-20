/**
 * Copyright (c) 2009 Juwi MacMillan Group GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package de.juwimm.cms.remote;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Blob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.tizzit.util.DateConverter;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.beans.EditionCronService;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.Constants.LiveserverDeployStatus;
import de.juwimm.cms.common.Constants.ResourceUsageState;
import de.juwimm.cms.exceptions.AlreadyCheckedOutException;
import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.model.ContentHbm;
import de.juwimm.cms.model.ContentVersionHbm;
import de.juwimm.cms.model.DocumentHbm;
import de.juwimm.cms.model.EditionHbm;
import de.juwimm.cms.model.LockHbm;
import de.juwimm.cms.model.PictureHbm;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.model.ViewComponentHbm;
import de.juwimm.cms.model.ViewDocumentHbm;
import de.juwimm.cms.remote.helper.AuthenticationHelper;
import de.juwimm.cms.vo.ContentValue;
import de.juwimm.cms.vo.ContentVersionValue;
import de.juwimm.cms.vo.DocumentSlimValue;
import de.juwimm.cms.vo.EditionValue;
import de.juwimm.cms.vo.PictureSlimValue;
import de.juwimm.cms.vo.PictureSlimstValue;
import de.juwimm.cms.vo.UnitValue;
import de.juwimm.cms.vo.ViewComponentValue;
import de.juwimm.cms.vo.ViewDocumentValue;

/**
 * @see de.juwimm.cms.remote.ContentServiceSpring
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class ContentServiceSpringImpl extends ContentServiceSpringBase {
	private static Log log = LogFactory.getLog(ContentServiceSpringImpl.class);
	public static final byte MAX_NO_OF_CONTENT_VERSIONS_PER_PAGE = 10;
	@Autowired
	EditionCronService editionCronService;

	public class DocumentCountWrapper {
		private HashMap<Integer, Integer> deltaDocuments = null;

		public DocumentCountWrapper(HashMap<Integer, Integer> startMap) {
			this.deltaDocuments = startMap;
		}

		public void addDocument(Integer docId, Integer docDelta) {
			Integer docCount = this.deltaDocuments.get(docId);
			if (docCount == null) docCount = new Integer(0);
			docCount = new Integer(docCount.intValue() + docDelta.intValue());
			this.deltaDocuments.put(docId, docCount);
		}

		public HashMap<Integer, Integer> getDeltaDocuments() {
			return deltaDocuments;
		}

	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#addPicture2Unit(java.lang.Integer, byte[], byte[], java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected Integer handleAddPicture2Unit(Integer unitId, byte[] thumbnail, byte[] picture, String mimeType, String altText, String pictureName) throws Exception {
		PictureHbm pictureHbm = PictureHbm.Factory.newInstance(thumbnail, picture, null, mimeType, null, altText, pictureName, null, null, false, null);
		UnitHbm unit = super.getUnitHbmDao().load(unitId);
		pictureHbm.setUnit(unit);
		pictureHbm = getPictureHbmDao().create(pictureHbm);
		return pictureHbm.getPictureId();
	}

	@Override
	protected Integer handleAddPictureWithPreview2Unit(Integer unitId, byte[] thumbnail, byte[] picture, byte[] preview, String mimeType, String altText, String pictureName) throws Exception {
		UnitHbm unit = super.getUnitHbmDao().load(unitId);
		PictureHbm pictureHbm = PictureHbm.Factory.newInstance(thumbnail, picture, preview, mimeType, null, altText, pictureName, null, null, false, null);
		pictureHbm.setUnit(unit);
		pictureHbm = getPictureHbmDao().create(pictureHbm);
		return pictureHbm.getPictureId();
	}

	/**
	 * Save content and create new contentVersion if heading or content is different from saved state
	 * @see de.juwimm.cms.remote.ContentServiceSpring#checkIn(de.juwimm.cms.vo.ContentValue)
	 */
	@Override
	protected void handleCheckIn(ContentValue contentValue) throws Exception {
		if (log.isDebugEnabled()) log.debug("begin checkIn ContentValue");
		try {
			ContentHbm content = super.getContentHbmDao().load(contentValue.getContentId());
			ContentVersionHbm latest = content.getLastContentVersion();
			boolean headingEqual = latest.getHeading() != null && latest.getHeading().equals(contentValue.getHeading());
			boolean textEqual = latest.getText() != null && latest.getText().equals(contentValue.getContentText());
			if (headingEqual && textEqual) {
				contentValue.setCreateNewVersion(false);
				//				this.updateDocumentUseCountLastVersion(latest.getText(), contentValue.getContentText());
			}
			contentValue.setVersion(latest.getVersion());

			LockHbm lock = latest.getLock();
			if (lock != null) {
				if (AuthenticationHelper.getUserName().equalsIgnoreCase(lock.getOwner().getUserId())) {
					if (contentValue.isCreateNewVersion()) {
						try {
							ContentVersionHbm newContentVersion = ContentVersionHbm.Factory.newInstance();
							newContentVersion.setCreateDate(System.currentTimeMillis());
							newContentVersion.setText(contentValue.getContentText());
							newContentVersion.setHeading(contentValue.getHeading());

							Integer version = Integer.parseInt(latest.getVersion());
							if (version == null) {
								version = 1;
							} else {
								version++;
							}
							newContentVersion.setVersion(version.toString());
							newContentVersion = super.getContentVersionHbmDao().create(newContentVersion);
							content.getContentVersions().add(newContentVersion);
						} catch (Exception exe) {
							throw new UserException(exe.getMessage());
						}
					} else {
						latest.setText(contentValue.getContentText());
						latest.setHeading(contentValue.getHeading());
						latest.setCreateDate(System.currentTimeMillis());
						latest.setCreator(AuthenticationHelper.getUserName());
					}
					// -- Indexing through No-Messaging
					if (contentValue.getContentText() != null) {
						content.setUpdateSearchIndex(true);
					}
					try {
						if (log.isDebugEnabled()) {
							log.debug("Removing Lock");
						}
						latest.setLock(null);
						getLockHbmDao().remove(lock);
					} catch (Exception exe) {
						throw new UserException(exe.getMessage());
					}
				} else {
					throw new UserException("checked out by another User: " + lock.getOwner().getUserId());
				}
			}
			if (log.isDebugEnabled()) log.debug("end checkIn()");
			//FIXME FIX THIS this.removeSpareContentVersions(contentValue.getContentId());
			/**
			 * Caused by: org.hibernate.ObjectDeletedException: deleted object would be re-saved by cascade (remove deleted object from associations): [de.juwimm.cms.model.ContentVersionHbmImpl#993]

			 */
		} catch (Exception e) {
			log.error("Error checking in: " + e.getMessage(), e);
			throw new UserException("Error checking in: " + e);
		}
		if (log.isDebugEnabled()) log.debug("end checkIn ContentValue");
	}

	/*
	private void updateDocumentCounters(ContentVersionHbm formerContentVersion, ContentVersionHbm newContentVersion) {
		if (newContentVersion.getVersion().equals("PUBLS")) {
			this.updateDocumentUseCountPublishVersion(null, newContentVersion.getText());
		} else {
			String oldLastContentText = (formerContentVersion == null || formerContentVersion.getText() == null) ? null : formerContentVersion.getText();
			this.updateDocumentUseCountLastVersion(oldLastContentText, newContentVersion.getText());
		}
	}
	*/

	/**
	 * Checkin only without creating a specialized version <br/>
	 * This is used for checking in all remaining pages while exiting the app<br/>
	 * Just remove any lock
	 * 
	 * @throws UserException
	 * 
	 * @see de.juwimm.cms.remote.ContentServiceSpring#checkIn4ContentId(java.lang.Integer)
	 */
	@Override
	protected void handleCheckIn4ContentId(Integer contentId) throws Exception {
		try {
			ContentHbm content = getContentHbmDao().load(contentId);
			ContentVersionHbm latest = content.getLastContentVersion();
			LockHbm lock = latest.getLock();
			if (lock != null) {
				latest.setLock(null);
				super.getLockHbmDao().remove(lock);
			}
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * Lock this content-page exclusively for the current user
	 * @see de.juwimm.cms.remote.ContentServiceSpring#checkOut(java.lang.Integer, boolean)
	 */
	@Override
	protected ContentValue handleCheckOut(Integer contentId, boolean force) throws Exception {
		try {
			String caller = AuthenticationHelper.getUserName();
			ContentHbm content = getContentHbmDao().load(contentId);
			ContentVersionHbm contentVersion = content.getLastContentVersion();
			if (contentVersion == null) {
				//if does not have a content version
				contentVersion = ContentVersionHbm.Factory.newInstance();
				contentVersion.setVersion("1");
				contentVersion = getContentVersionHbmDao().create(contentVersion);
				content.getContentVersions().add(contentVersion);
			}
			LockHbm lock = content.getLastContentVersion().getLock();
			UserHbm user = getUserHbmDao().load(caller);
			if (lock == null || force || lock.getOwner() == null) {
				this.checkOut(content, force, user);
				return content.getDao();
			}
			String lockOwner = lock.getOwner().getUserId();
			if (caller.equals(lockOwner)) {
				if (log.isDebugEnabled()) log.debug("AlreadyCheckedOut by mysqlf - " + caller);
				throw new AlreadyCheckedOutException("");
			}
			if (log.isDebugEnabled()) {
				log.debug("AlreadyCheckedOut by - " + lockOwner);
			}
			throw new AlreadyCheckedOutException(lockOwner);
		} catch (AlreadyCheckedOutException acoe) {
			log.warn("Content with id " + contentId + " already checked out.", acoe);
			throw new AlreadyCheckedOutException(acoe.getMessage());
		} catch (Exception e) {
			log.error("Exception: " + e.getMessage(), e);
			throw new UserException(e.getMessage());
		}
	}

	private void checkOut(ContentHbm content, boolean force, UserHbm user) throws UserException, AlreadyCheckedOutException {
		if (log.isDebugEnabled()) log.debug("begin checkOut()");
		ContentVersionHbm latest = content.getLastContentVersion();
		LockHbm lock = latest.getLock();

		if (lock != null && lock.getOwner() != null) {
			if (!force) {
				if (user.getUserId().equals(lock.getOwner().getUserId())) {
					if (log.isDebugEnabled()) {
						log.debug("AlreadyCheckedOut by mysqlf - " + user.getUserId());
					}
					throw new AlreadyCheckedOutException("");
				}
				if (log.isDebugEnabled()) {
					log.debug("AlreadyCheckedOut by - " + lock.getOwner().getUserId());
				}
				throw new AlreadyCheckedOutException(lock.getOwner().getUserId());
			}
			try {
				super.getLockHbmDao().remove(lock);
				LockHbm newLock = LockHbm.Factory.newInstance();
				newLock.setOwner(user);
				newLock.setCreateDate(System.currentTimeMillis());
				newLock = getLockHbmDao().create(newLock);
				latest.setLock(newLock);
			} catch (Exception exe) {
				log.error("Error occured", exe);
			}
		} else {
			try {
				LockHbm newLock = LockHbm.Factory.newInstance();
				newLock.setOwner(user);
				newLock.setCreateDate(System.currentTimeMillis());
				newLock = getLockHbmDao().create(newLock);
				latest.setLock(newLock);
				if (log.isDebugEnabled()) {
					log.debug("Setting lock for checkout " + user.getUserId());
				}
			} catch (Exception exe) {
				log.error("Error occured", exe);
				throw new UserException(exe.getMessage());
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("end checkOut()");
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#createContent(de.juwimm.cms.vo.ContentValue)
	 */
	@Override
	protected ContentValue handleCreateContent(ContentValue contentValue) throws Exception {
		ContentHbm contentHbm = getContentHbmDao().create(contentValue, AuthenticationHelper.getUserName());

		ContentVersionHbm cv = ContentVersionHbm.Factory.newInstance();
		cv.setVersion("1");
		cv.setText(contentValue.getContentText());
		cv.setHeading(contentValue.getHeading());
		cv = getContentVersionHbmDao().create(cv);
		contentHbm.getContentVersions().add(cv);
		return contentHbm.getDao();
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#createEdition(java.lang.String, java.lang.Integer, boolean, boolean)
	 */
	@Override
	protected void handleCreateEdition(String commentText, Integer rootViewComponentId, boolean deploy, boolean succMessage, Integer deployType) throws Exception {
		if (log.isInfoEnabled()) log.info("Enqueue createEdition-Event " + AuthenticationHelper.getUserName() + " rootVCID " + rootViewComponentId);
		SiteHbm site = null;
		ViewComponentHbm vc = null;
		try {
			site = getUserHbmDao().load(AuthenticationHelper.getUserName()).getActiveSite();
			vc = getViewComponentHbmDao().load(rootViewComponentId);
		} catch (Exception exe) {
			log.error("Havent found either site or viewcomponent: (rootVc)" + rootViewComponentId + " " + exe.getMessage());
		}
		if (site != null) {
			if (log.isInfoEnabled()) log.info("Enqueue createEdition-Event for viewComponentId: " + rootViewComponentId);
			try {
				boolean needsDeploy = true;
				//TODO Edition
				// site, vc, vd, unit in one value - difference makes deploy type
				// type = 3 rootDeploy
				// type = 0 fulldeploy - value is site; 1 unitdeploy - value is unit, 2 pagedeploy - value is vc, rest for export...
				EditionHbm newEdition = getEditionHbmDao().create(AuthenticationHelper.getUserName(), commentText, rootViewComponentId, vc.getUnit4ViewComponent(), site.getDefaultViewDocument().getViewDocumentId(), site.getSiteId(), needsDeploy);
				newEdition.setDeployType(deployType);
				newEdition.setStartActionTimestamp(System.currentTimeMillis());
				newEdition.setDeployStatus(LiveserverDeployStatus.EditionCreated.name().getBytes());
				if (log.isDebugEnabled()) log.debug("Finished createEdtion Task on Queue");
			} catch (Exception e) {
				throw new UserException(e.getMessage());
			}
		} else {
			throw new UserException("User was not loggedin to the site he is willing to deploy. Deploy has been CANCELED.");
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#createPicture(byte[], byte[], java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected Integer handleCreatePicture(byte[] thumbnail, byte[] picture, String mimeType, String altText, String pictureName) throws Exception {
		PictureHbm pictureHbm = PictureHbm.Factory.newInstance(thumbnail, picture, null, mimeType, null, altText, pictureName, null, null, false, null);
		pictureHbm = getPictureHbmDao().create(pictureHbm);
		return pictureHbm.getPictureId();
	}

	/**
	 * Creates a new FULL-Edition for the active site and returns it as SOAP-Attachment.
	 * 
	 * @see de.juwimm.cms.remote.ContentServiceSpring#exportEditionFull()
	 */
	@Override
	protected InputStream handleExportEditionFull() throws Exception {
		try {
			if (log.isInfoEnabled()) log.info("createEditionForExport " + AuthenticationHelper.getUserName());
			File fle = File.createTempFile("edition_full_export", ".xml.gz");
			FileOutputStream fout = new FileOutputStream(fle);
			GZIPOutputStream gzoudt = new GZIPOutputStream(fout);
			PrintStream out = new PrintStream(gzoudt, true, "UTF-8");

			UserHbm invoker = getUserHbmDao().load(AuthenticationHelper.getUserName());
			SiteHbm site = invoker.getActiveSite();
			if (log.isDebugEnabled()) log.debug("Invoker is: " + invoker.getUserId() + " within Site " + site.getName());
			EditionHbm edition = getEditionHbmDao().create("INTERIMEDITION", null, null, true);
			if (log.isDebugEnabled()) log.debug("Dummy-Editon create");
			out.println("<edition>");
			if (log.isDebugEnabled()) log.debug("picturesToXmlRecursive");
			getEditionHbmDao().picturesToXmlRecursive(null, site.getSiteId(), out, edition);
			System.gc();
			if (log.isDebugEnabled()) log.debug("documentsToXmlRecursive");
			getEditionHbmDao().documentsToXmlRecursive(null, site.getSiteId(), out, true, edition);
			System.gc();
			if (log.isDebugEnabled()) log.debug("unitsToXmlRecursive");
			getEditionHbmDao().unitsToXmlRecursive(site.getSiteId(), out, edition);
			System.gc();
			if (log.isDebugEnabled()) log.debug("hostsToXmlRecursive");
			getEditionHbmDao().hostsToXmlRecursive(site.getSiteId(), out, edition);
			if (log.isDebugEnabled()) log.debug("viewdocumentsToXmlRecursive");
			getEditionHbmDao().viewdocumentsToXmlRecursive(site.getSiteId(), out, edition);
			if (log.isDebugEnabled()) log.debug("realmsToXmlRecursive");
			getEditionHbmDao().realmsToXmlRecursive(site.getSiteId(), out, edition);
			System.gc();
			if (log.isDebugEnabled()) log.debug("Creating ViewComponent Data");
			Iterator vdIt = getViewDocumentHbmDao().findAll(site.getSiteId()).iterator();
			while (vdIt.hasNext()) {
				ViewDocumentHbm vdl = (ViewDocumentHbm) vdIt.next();
				// vdl.getViewComponent().toXml(null, 0, true, false, 1, false, false, out);
				getViewComponentHbmDao().toXml(vdl.getViewComponent(), null, true, false, -1, false, false, out);

			}
			if (log.isDebugEnabled()) log.debug("Finished creating ViewComponent Data");
			out.println("</edition>");
			getEditionHbmDao().remove(edition);
			out.flush();
			out.close();
			out = null;
			return new FileInputStream(fle);
		} catch (Exception e) {
			throw new UserException(e.getMessage(), e);
		}
	}

	/**
	 * Creates a new Unit-Edition for the active site and returns it as SOAP-Attachment.
	 * 
	 * @throws UserException
	 * 
	 * @see de.juwimm.cms.remote.ContentServiceSpring#exportEditionUnit(java.lang.Integer)
	 */
	@Override
	protected InputStream handleExportEditionUnit(Integer rootViewComponentId) throws Exception {
		try {
			File fle = File.createTempFile("edition_unit_export", ".xml.gz");
			FileOutputStream fout = new FileOutputStream(fle);
			GZIPOutputStream gzoudt = new GZIPOutputStream(fout);
			PrintStream out = new PrintStream(gzoudt, true, "UTF-8");
			EditionHbm edition = super.getEditionHbmDao().create("RETURNEDITION", rootViewComponentId, out, true);
			super.getEditionHbmDao().remove(edition);
			out.flush();
			out.close();
			out = null;
			return new FileInputStream(fle);
		} catch (Exception e) {
			log.error("Could not export edition unit", e);
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#getAllContentVersions(java.lang.Integer)
	 */
	@Override
	protected ContentVersionValue[] handleGetAllContentVersions(Integer contentId) throws UserException {
		try {
			ContentHbm content = super.getContentHbmDao().load(contentId);
			Collection<ContentVersionHbm> coll = content.getContentVersions();
			Iterator<ContentVersionHbm> it = coll.iterator();
			TreeMap<Integer, ContentVersionHbm> tm = new TreeMap<Integer, ContentVersionHbm>();
			while (it.hasNext()) {
				ContentVersionHbm cvd = it.next();
				if (!cvd.getVersion().equals("PUBLS")) {
					tm.put(new Integer(cvd.getVersion()), cvd);
				}
			}

			it = tm.values().iterator();
			int siz = tm.values().size();
			ContentVersionValue[] arr = new ContentVersionValue[siz];
			for (int i = 0; i < siz; i++) {
				ContentVersionHbm cv = it.next();
				arr[i] = getContentVersionHbmDao().getDao(cv);
				if (i == (siz - 1)) {
					arr[i].setVersionComment(arr[i].getCreator() + " (" + DateConverter.getSql2String(new Date(arr[i].getCreateDate())) + ")");
				} else {
					arr[i].setVersionComment(arr[i].getVersion() + " - " + arr[i].getCreator() + " (" + DateConverter.getSql2String(new Date(arr[i].getCreateDate())) + ")");
				}
				arr[i].setText(null);
			}
			return arr;
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#getAllContentVersionsId(java.lang.Integer)
	 */
	@Override
	protected Integer[] handleGetAllContentVersionsId(Integer contentId) throws Exception {
		try {
			ContentHbm content = super.getContentHbmDao().load(contentId);
			Collection coll = content.getContentVersions();
			Iterator it = coll.iterator();
			TreeMap<Integer, ContentVersionHbm> tm = new TreeMap<Integer, ContentVersionHbm>();
			while (it.hasNext()) {
				ContentVersionHbm cvd = (ContentVersionHbm) it.next();
				if (!cvd.getVersion().equals("PUBLS")) {
					tm.put(new Integer(cvd.getVersion()), cvd);
				}
			}

			it = tm.values().iterator();
			int siz = tm.values().size();
			Integer[] arr = new Integer[siz];
			for (int i = 0; i < siz; i++) {
				arr[i] = ((ContentVersionHbm) it.next()).getContentVersionId();
			}
			return arr;
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#getAllDocuments4Unit(java.lang.Integer)
	 */
	@Override
	protected Integer[] handleGetAllDocuments4Unit(Integer unitId) throws Exception {
		try {
			Collection coll = super.getDocumentHbmDao().findAll(unitId);
			Iterator it = coll.iterator();
			int siz = coll.size();
			Integer[] itarr = new Integer[siz];
			for (int i = 0; i < siz; i++) {
				itarr[i] = ((de.juwimm.cms.model.DocumentHbm) it.next()).getDocumentId();
			}
			return itarr;
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#getAllPictures4Unit(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Integer[] handleGetAllPictures4Unit(Integer unitId) throws Exception {
		try {
			Collection<PictureHbm> coll = getPictureHbmDao().findAllPerUnit(unitId);
			int i = 0;
			Integer[] itarr = new Integer[coll.size()];
			for (PictureHbm pic : coll) {
				itarr[i++] = pic.getPictureId();
			}
			return itarr;
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#getAllSlimDocuments4Unit(java.lang.Integer)
	 */
	@Override
	protected DocumentSlimValue[] handleGetAllSlimDocuments4Unit(Integer unitId) throws Exception {
		DocumentSlimValue[] dvArr = null;
		try {
			Collection<DocumentHbm> coll = getDocumentHbmDao().findAll(unitId);
			dvArr = new DocumentSlimValue[coll.size()];
			int i = 0;
			for (DocumentHbm doc : coll) {
				dvArr[i++] = doc.getSlimValue();
			}
			return dvArr;
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#getAllSlimPictures4Unit(java.lang.Integer)
	 */
	@Override
	protected PictureSlimstValue[] handleGetAllSlimPictures4Unit(Integer unitId) throws Exception {
		PictureSlimstValue[] pvArr = null;
		try {
			Collection coll = super.getPictureHbmDao().findAllPerUnit(unitId);
			Iterator it = coll.iterator();
			int siz = coll.size();
			pvArr = new PictureSlimstValue[siz];
			for (int i = 0; i < siz; i++) {
				PictureHbm pic = (PictureHbm) it.next();
				pvArr[i] = pic.getSlimstValue();
			}
			return pvArr;
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#getAnchors(java.lang.Integer)
	 */
	@Override
	protected String[] handleGetAnchors(Integer contentId) throws Exception {
		try {
			String[] anchArr = new String[0];
			ContentHbm content = super.getContentHbmDao().load(contentId);
			ContentVersionHbm cvl = content.getLastContentVersion();
			String contentText = cvl.getText();
			org.w3c.dom.Document doc = XercesHelper.string2Dom(contentText);
			Iterator nit = XercesHelper.findNodes(doc, "//anchor/a");
			Vector<String> vec = new Vector<String>();
			while (nit.hasNext()) {
				Element elm = (Element) nit.next();
				try {
					String anchor = elm.getAttribute("name");
					vec.add(anchor);
				} catch (Exception exe) {
				}
			}
			anchArr = vec.toArray(new String[0]);
			return anchArr;
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#getContent(java.lang.Integer)
	 */
	@Override
	protected ContentValue handleGetContent(Integer contentId) throws Exception {
		try {
			ContentHbm content = super.getContentHbmDao().load(contentId);
			return content.getDao();
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#getContentTemplateName(java.lang.Integer)
	 */
	@Override
	protected String handleGetContentTemplateName(Integer viewComponentId) throws Exception {
		ContentHbm content = null;
		ViewComponentHbm view = null;
		try {
			view = super.getViewComponentHbmDao().load(viewComponentId);
			if (view.getViewType() == Constants.VIEW_TYPE_INTERNAL_LINK || view.getViewType() == Constants.VIEW_TYPE_SYMLINK) {
				view = super.getViewComponentHbmDao().load(new Integer(view.getReference()));
				content = super.getContentHbmDao().load(new Integer(view.getReference()));
			} else {
				content = super.getContentHbmDao().load(new Integer(view.getReference()));
			}
			if (log.isDebugEnabled()) {
				log.debug("content: " + content + " vcId: " + viewComponentId);
				if (content != null) {
					log.debug("content: " + content.getTemplate());
				}
			}
			if (content != null) return content.getTemplate();
			return null;
		} catch (Exception e) {
			throw new UserException("Could not find referenced ContentVersion with Id: " + view.getReference() + " vcid:" + viewComponentId + "\n" + e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#getContentVersion(java.lang.Integer)
	 */
	@Override
	protected ContentVersionValue handleGetContentVersion(Integer contentVersionId) throws Exception {
		try {
			ContentVersionHbm contentVersion = super.getContentVersionHbmDao().load(contentVersionId);
			return getContentVersionHbmDao().getDao(contentVersion);
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#getContentWithPUBLSVersion(java.lang.Integer)
	 */
	@Override
	protected ContentValue handleGetContentWithPUBLSVersion(Integer contentId) throws Exception {
		try {
			ContentHbm content = super.getContentHbmDao().load(contentId);
			return content.getDaoWithPUBLSVersion();
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#getDocument(java.lang.Integer)
	 */
	@Override
	protected byte[] handleGetDocument(Integer documentId) throws Exception {
		byte[] retArr = null;
		try {
			if (log.isDebugEnabled()) log.debug("LOOKING FOR DOCUMENT");
			//TODO: @TODO: Just a hack for the moment
			retArr = getDocumentHbmDao().getDocumentContent(documentId);
			if (log.isDebugEnabled()) {
				try {
					if (log.isDebugEnabled()) {
						log.debug("GOT THE DOCUMENT");

						log.debug("DOCUMENT SIZE " + retArr.length);
					}
				} catch (Exception inew) {
					if (log.isDebugEnabled()) log.debug(inew.getMessage());
				}
			}
		} catch (Exception e) {
			if (log.isDebugEnabled()) log.debug("CANNOT GET DOCUMENT " + e.getMessage());
			throw new UserException(e.getMessage());
		}
		return retArr;
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#getDocumentName(java.lang.Integer)
	 */
	@Override
	protected String handleGetDocumentName(Integer documentId) throws Exception {
		try {
			DocumentHbm document = super.getDocumentHbmDao().load(documentId);
			return document.getDocumentName();
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#getEchoDAO(java.lang.String)
	 */
	@Override
	protected ContentValue handleGetEchoDAO(String echoString) throws Exception {
		try {
			System.out.println("GOT AN ECHO REQUEST " + echoString);
			ContentValue cdao = new ContentValue();
			cdao.setHeading(echoString);
			return cdao;
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#getEchoString(java.lang.String)
	 */
	@Override
	protected String handleGetEchoString(String echoString) throws Exception {
		try {
			ContentVersionHbm cvl = super.getContentVersionHbmDao().load(new Integer(999999));
			System.out.println("GOT AN ECHO REQUEST cvl.getHeading() " + cvl.getHeading());
			return cvl.getHeading();
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#getEditions(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	protected EditionValue[] handleGetEditions(Integer unitId, Integer viewDocumentId) throws Exception {
		try {
			Collection<EditionHbm> editions = super.getEditionHbmDao().findByUnitAndViewDocument(unitId, viewDocumentId);
			EditionValue[] editionValues = new EditionValue[editions.size()];
			int i = 0;
			for (EditionHbm edition : editions) {
				editionValues[i++] = edition.getDao();
			}
			return editionValues;
		} catch (Exception e) {
			log.error("Could not get edition values", e);
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#getMimeType4Document(java.lang.Integer)
	 */
	@Override
	protected String handleGetMimeType4Document(Integer pictureId) throws Exception {
		DocumentHbm document = super.getDocumentHbmDao().load(pictureId);
		try {
			return document.getMimeType();
		} catch (Exception e) {
			log.error("Could not get mime type for document with id: " + pictureId, e);
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * Get mime type for document
	 * 
	 * @see de.juwimm.cms.remote.ContentServiceSpring#getMimeType4Picture(java.lang.Integer)
	 */
	@Override
	protected String handleGetMimeType4Picture(Integer pictureId) throws Exception {
		PictureHbm picture = super.getPictureHbmDao().load(pictureId);
		try {
			return picture.getMimeType();
		} catch (Exception e) {
			log.error("Could not get mime type for picture with id: " + pictureId, e);
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#getNotReferencedUnits(de.juwimm.cms.vo.ViewDocumentValue)
	 */
	@Override
	protected UnitValue[] handleGetNotReferencedUnits(ViewDocumentValue viewDocument) throws Exception {
		if (log.isInfoEnabled()) log.info("starting getNotReferencedUnits for Site " + viewDocument.getSiteId() + " and Language " + viewDocument.getLanguage());
		try {
			Collection coll = super.getViewComponentHbmDao().findAllWithUnit(viewDocument.getViewDocumentId());
			Collection u = super.getUnitHbmDao().findAll(viewDocument.getSiteId());
			Vector<Integer> units = new Vector<Integer>();

			Iterator itUnits = u.iterator();
			while (itUnits.hasNext()) {
				UnitHbm unit = (UnitHbm) itUnits.next();
				units.add(unit.getUnitId());
			}

			Iterator it = coll.iterator();
			while (it.hasNext()) {
				ViewComponentHbm vcl = (ViewComponentHbm) it.next();
				try {
					units.remove(vcl.getAssignedUnit().getUnitId());
				} catch (Exception e) {
					if (log.isDebugEnabled()) {
						log.debug("Could not remove assigned unit: " + e.getMessage());
					}
				}
			}

			UnitValue[] unitdaos = new UnitValue[units.size()];
			it = units.iterator();
			int i = 0;
			while (it.hasNext()) {
				Integer unitId = (Integer) it.next();
				UnitHbm ul = super.getUnitHbmDao().load(unitId);
				unitdaos[i++] = getUnitHbmDao().getDao(ul);
			}
			if (log.isDebugEnabled()) log.debug("end getNotReferencedUnits for Site " + viewDocument.getSiteId() + " and Language " + viewDocument.getLanguage());
			return unitdaos;
		} catch (Exception e) {
			log.warn("Error getNotReferencedUnits for Site " + viewDocument.getSiteId() + " and Language " + viewDocument.getLanguage() + ": " + e.getMessage(), e);
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#getPicture(java.lang.Integer)
	 */
	@Override
	protected PictureSlimValue handleGetPicture(Integer pictureId) throws Exception {
		PictureHbm picture = super.getPictureHbmDao().load(pictureId);
		return picture.getSlimValue();
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#getPictureAltText(java.lang.Integer)
	 */
	@Override
	protected String handleGetPictureAltText(Integer pictureId) throws Exception {
		PictureHbm picture = super.getPictureHbmDao().load(pictureId);
		return picture.getAltText();
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#getPictureData(java.lang.Integer)
	 */
	@Override
	protected byte[] handleGetPictureData(Integer pictureId) throws Exception {
		PictureHbm picture = super.getPictureHbmDao().load(pictureId);
		try {
			return picture.getPicture();
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("Could not get data for picture with id: " + pictureId, e);
			}
			throw new UserException(e.getMessage());
		}

	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#getPictureFileName(java.lang.Integer)
	 */
	@Override
	protected String handleGetPictureFileName(Integer pictureId) throws Exception {
		PictureHbm picture = super.getPictureHbmDao().load(pictureId);
		return picture.getPictureName();
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#getThumbnail(java.lang.Integer)
	 */
	@Override
	protected byte[] handleGetThumbnail(Integer pictureId) throws Exception {
		PictureHbm picture = super.getPictureHbmDao().load(pictureId);
		if (picture != null) {
			return picture.getThumbnail();
		}
		return new byte[0];
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#getTimestamp4Document(java.lang.Integer)
	 */
	@Override
	protected Long handleGetTimestamp4Document(Integer pictureId) throws Exception {
		DocumentHbm document = super.getDocumentHbmDao().load(pictureId);
		try {
			return document.getTimeStamp();
		} catch (Exception e) {
			log.error("Could not get timestamp for document with id: " + pictureId, e);
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#getTimestamp4Picture(java.lang.Integer)
	 */
	@Override
	protected Long handleGetTimestamp4Picture(Integer pictureId) throws Exception {
		PictureHbm picture = super.getPictureHbmDao().load(pictureId);
		try {
			return picture.getTimeStamp();
		} catch (Exception e) {
			log.error("Could not get timestamp for picture with id: " + pictureId, e);
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#importDocument(java.lang.Integer, java.lang.String, java.lang.String, java.io.InputStream)
	 */
	@Override
	protected Integer handleAddOrUpdateDocument(Integer unitId, String documentName, String mimeType, InputStream documentData, Integer documentId) throws Exception {
		try {
			if (log.isDebugEnabled()) log.debug("addOrUpdateDocument for user " + AuthenticationHelper.getUserName());

			UnitHbm unit = getUnitHbmDao().load(unitId);
			DocumentHbm doc = null;

			File tmp = this.storeTempFile(documentData, documentName);
			FileInputStream in = new FileInputStream(tmp);
			Blob b = Hibernate.createBlob(in);

			if (documentId == null) {
				doc = DocumentHbm.Factory.newInstance();
				doc.setDocumentName(documentName);
				doc.setMimeType(mimeType);
				doc.setUnit(unit);
				doc = getDocumentHbmDao().create(doc);
				doc.setDocument(b);
			} else {
				doc = getDocumentHbmDao().load(documentId);
				doc.setTimeStamp(System.currentTimeMillis());
				doc.setDocumentName(documentName);
				doc.setMimeType(mimeType);
				getDocumentHbmDao().setDocumentContent(doc.getDocumentId(), IOUtils.toByteArray(in));
			}
			return doc.getDocumentId();
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * Imports an Edition though an <b>.xml.gz </b> Upload. <br>
	 * It will be expected as a SOAP Attachment.
	 * 
	 * @see de.juwimm.cms.remote.ContentServiceSpring#importEdition(java.lang.Integer, java.io.InputStream)
	 */
	@Override
	protected void handleImportEdition(Integer viewComponentId, InputStream in, boolean useNewIds, Integer workServerEditionId) throws Exception {
		try {
			if (log.isInfoEnabled()) log.info("importEdition " + AuthenticationHelper.getUserName());
			String tmpFileName = "";
			try {
				tmpFileName = this.storeEditionFile(in);
			} catch (IOException e) {
				log.warn("Unable to copy received inputstream: " + e.getMessage(), e);
			}
			if (log.isInfoEnabled()) log.info("end importEdition - please wait for cronjob to pick up!");
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}

	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#removeAllOldContentVersions(java.lang.Integer)
	 */
	@Override
	protected void handleRemoveAllOldContentVersions(Integer contentId) throws Exception {
		try {
			Integer[] allContentVersionIds = getAllContentVersionsId(contentId);
			ContentHbm content = super.getContentHbmDao().load(contentId);
			ContentVersionHbm lastContentVersion = content.getLastContentVersion();
			Integer lastContentVersionId = lastContentVersion.getContentVersionId();
			for (int i = 0; i < allContentVersionIds.length; i++) {
				if (!lastContentVersionId.equals(allContentVersionIds[i])) {
					removeContentVersion(allContentVersionIds[i]);
				}
			}
			lastContentVersion.setVersion("1");
		} catch (Exception e) {
			log.error("Could not remove all old content versions from content with id " + contentId, e);
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#removeContentVersion(java.lang.Integer)
	 */
	@Override
	protected void handleRemoveContentVersion(Integer contentVersionId) throws Exception {
		super.getContentVersionHbmDao().remove(contentVersionId);
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#removeDocument(java.lang.Integer)
	 */
	@Override
	protected void handleRemoveDocument(Integer documentId) throws Exception {
		Set viewComponents = getDocumentUsage(documentId);
		if (viewComponents.size() > 0) {
			throw new UserException("validation exception");
		}
		super.getDocumentHbmDao().remove(documentId);
	}

	/**
	 * Deletes an Edition
	 * 
	 * @see de.juwimm.cms.remote.ContentServiceSpring#removeEdition(java.lang.Integer)
	 */
	@Override
	protected void handleRemoveEdition(Integer editionId) throws Exception {
		try {
			super.getEditionHbmDao().remove(editionId);
		} catch (Exception e) {
			log.error("Could not remove edition with id: " + editionId, e);
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#removePicture(java.lang.Integer)
	 */
	@Override
	protected void handleRemovePicture(Integer pictureId) throws Exception {
		Set viewComponents = getPictureUsage(pictureId);
		if (viewComponents.size() > 0) {
			throw new UserException("validation exception");
		}
		super.getPictureHbmDao().remove(pictureId);

	}

	/**
	 * 
	 * @see de.juwimm.cms.remote.ContentServiceSpring#restoreEdition(java.lang.Integer)
	 */
	@Override
	protected void handleRestoreEdition(Integer editionId) throws Exception {
		throw new UnsupportedOperationException("Method importEdition(Integer editionId) not yet implemented.");
		/*
		 * try { if(log.isDebugEnabled()) log.debug("importEdition"); EditionLocal edition = getEditionLocalHome().findByPrimaryKey(editionId); int unitId = edition.getUnitId(); int viewDocumentId =
		 * edition.getViewDocumentId(); // because of this transaction won't be completely rolledback, we try to parse this document at first ByteArrayInputStream barr = new
		 * ByteArrayInputStream(edition.getEditionText()); GZIPInputStream gzIn = new GZIPInputStream(barr); InputSource in = new InputSource(new InputStreamReader(gzIn, "UTF-8")); DOMParser parser =
		 * new DOMParser(); parser.parse(in); Document doc = parser.getDocument(); // end of parsing the document Viewde.juwimm.cms.model.Document vdl = ViewgetDocumentLocalHome().findByPrimaryKey(new
		 * Integer(viewDocumentId)); UnitLocal unit = getUnitLocalHome().findByPrimaryKey(new Integer(unitId)); importViewComponentFile(doc, unit, viewDocumentId, false); }catch(AxisFault ae) { throw
		 * ae; }catch(Exception exe) { AxisFault af = new AxisFault(); af.setFaultDetailString(exe.getMessage()); throw af; }
		 */
	}

	/**
	 * Saves a content directly. ONLY for migration use! <br/>
	 * Is currently used to implement the XML-Lasche.
	 * 
	 * @see de.juwimm.cms.remote.ContentServiceSpring#saveContent(java.lang.Integer, java.lang.String)
	 */
	@Override
	protected void handleSaveContent(Integer contentId, String contentText) throws Exception {
		try {
			ContentHbm content = getContentHbmDao().load(contentId);
			getContentHbmDao().setContent(content, contentText);
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * Sets the ActiveEdition and deploy it to liveServer
	 * 
	 * @see de.juwimm.cms.remote.ContentServiceSpring#setActiveEdition(java.lang.Integer, boolean)
	 */
	@Override
	protected void handleSetActiveEdition(Integer editionId, boolean deploy) throws Exception {
		try {
			EditionHbm edac = super.getEditionHbmDao().load(editionId);
			Collection coll = super.getEditionHbmDao().findByUnitAndViewDocument(new Integer(edac.getUnitId()), new Integer(edac.getViewDocumentId()));
			Iterator it = coll.iterator();
			EditionHbm edition = null;
			while (it.hasNext()) {
				edition = (EditionHbm) it.next();
				if (edition.getEditionId().equals(editionId)) {
					if (edition.getStatus() == 0) {
						if (log.isDebugEnabled()) log.debug("CALLING importEdition AND/OR publishToLiveserver");
						if (deploy) {
							edition = super.getEditionHbmDao().create(edition);
							super.getEditionServiceSpring().publishEditionToLiveserver(edition.getEditionId());
						}
						edition.setStatus((byte) 1);
					}
				} else {
					edition.setStatus((byte) 0);
				}
			}
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#setPicture4Unit(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	protected void handleSetPicture4Unit(Integer unitId, Integer pictureId) throws Exception {
		UnitHbm unit = super.getUnitHbmDao().load(unitId);
		unit.setImageId(pictureId);
	}

	/**
	 * @see de.juwimm.cms.remote.ContentServiceSpring#updatePictureAltText(java.lang.Integer, java.lang.String)
	 */
	@Override
	protected void handleUpdatePictureAltText(Integer pictureId, String altText) throws Exception {
		PictureHbm picture = super.getPictureHbmDao().load(pictureId);
		picture.setAltText(altText);
	}

	/**
	 * Changes the TemplateName. This should be used carefully by the siteRoot, because it could corrupt the content after a second "save". <br>
	 * In general this should be used only to change templates for pages, that can't be deleted - for example the root-page.
	 * 
	 * @param viewComponentId
	 *            The ViewComponent to which the templateName should be updated in the content bean
	 * @param templateName
	 *            The template Name
	 * 
	 * @see de.juwimm.cms.remote.ContentServiceSpring#updateTemplate(java.lang.Integer, java.lang.String)
	 */
	@Override
	protected void handleUpdateTemplate(Integer viewComponentId, String templateName) throws Exception {
		ViewComponentHbm viewComponent = super.getViewComponentHbmDao().load(viewComponentId);
		ContentHbm content = super.getContentHbmDao().load(new Integer(viewComponent.getReference()));
		content.setTemplate(templateName);
	}

	/**
	 * If this page has more than ContentServiceSpringImpl.MAX_NO_OF_CONTENT_VERSIONS_PER_PAGE contentVersions, the oldest ones are deleted, the rest gets renumbered. An existing PUBLS-Version is
	 * conserved.
	 */
	private void removeSpareContentVersions(Integer contentId) {
		try {
			Collection allContentVersions = super.getContentHbmDao().load(contentId).getContentVersions();
			if (allContentVersions != null && allContentVersions.size() > ContentServiceSpringImpl.MAX_NO_OF_CONTENT_VERSIONS_PER_PAGE) {
				Iterator<ContentVersionHbm> it = allContentVersions.iterator();
				TreeMap<Integer, ContentVersionHbm> tm = new TreeMap<Integer, ContentVersionHbm>();
				while (it.hasNext()) {
					ContentVersionHbm cvd = it.next();
					if (!cvd.getVersion().equals("PUBLS")) {
						tm.put(new Integer(cvd.getVersion()), cvd);
					}
				}
				List<Integer> cvList2Delete = new ArrayList<Integer>();
				List<ContentVersionHbm> cvList = new ArrayList<ContentVersionHbm>();
				it = tm.values().iterator();
				while (it.hasNext()) {
					cvList.add(it.next());
				}
				int firstCoolIndex = tm.values().size() - ContentServiceSpringImpl.MAX_NO_OF_CONTENT_VERSIONS_PER_PAGE;
				int currentIndex = 1;
				for (int i = 0; i < cvList.size(); i++) {
					ContentVersionHbm current = cvList.get(i);
					if (i < firstCoolIndex) {
						cvList2Delete.add(current.getContentVersionId());
					} else {
						current.setVersion(Integer.toString(currentIndex++));
					}
				}
				Iterator<Integer> delIt = cvList2Delete.iterator();
				while (delIt.hasNext()) {
					Integer currContentVersionId = delIt.next();
					if (log.isDebugEnabled()) log.debug("Content: " + contentId + " Contentversion to delete: " + currContentVersionId);
					super.getContentVersionHbmDao().remove(currContentVersionId);
				}
			}
		} catch (Exception e) {
			log.error("Error removing spare ContentVersions: " + e.getMessage(), e);
		}
	}

	private String storeEditionFile(InputStream in) throws IOException {
		String dir = getTizzitPropertiesBeanSpring().getDeployDir();
		File fDir = new File(dir);
		fDir.mkdirs();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String date = dateFormat.format(new Date());
		File storedEditionFile = new File(fDir.getAbsolutePath() + File.separatorChar + "edition_import_" + date + ".xml.gz");
		//File.createTempFile("edition_import_" + date, ".xml.gz", fDir);
		FileOutputStream out = new FileOutputStream(storedEditionFile);
		IOUtils.copyLarge(in, out);
		IOUtils.closeQuietly(out);
		IOUtils.closeQuietly(in);
		return storedEditionFile.getAbsolutePath();
	}

	private File storeTempFile(InputStream in, String name) throws IOException {
		String dir = getTizzitPropertiesBeanSpring().getDatadir() + File.separatorChar + "tmp";
		File fDir = new File(dir);
		fDir.mkdirs();
		File storedEditionFile = File.createTempFile(name, "bak", fDir);
		FileOutputStream out = new FileOutputStream(storedEditionFile);
		IOUtils.copyLarge(in, out);
		IOUtils.closeQuietly(out);
		IOUtils.closeQuietly(in);
		if (log.isDebugEnabled()) {
			log.debug("Stored document file stream temporarily in: " + storedEditionFile.getAbsolutePath());
		}
		return storedEditionFile;
	}

	//		/**
	//		 * @param oldContentText
	//		 * @param newContentText
	//		 */
	//		private void updateDocumentUseCountLastVersion(String oldContentText, String newContentText) {
	//			/*  
	//			HashMap<Integer, Integer> deltaMap = this.getDeltaDocumentCounts(oldContentText, newContentText);
	//			this.updateDocumentUseCounts(deltaMap, true);
	//			*/
	//		}
	//
	//	private void updateDocumentUseCountPublishVersion(String oldContentText, String newContentText) {
	//		/*  
	//		HashMap<Integer, Integer> deltaMap = this.getDeltaDocumentCounts(oldContentText, newContentText);
	//		this.updateDocumentUseCounts(deltaMap, false);
	//		*/
	//	}
	//
	//	private void updateDocumentUseCounts(HashMap<Integer, Integer> deltaMap, boolean isLastVersion) {
	//		Iterator<Integer> it = deltaMap.keySet().iterator();
	//		while (it.hasNext()) {
	//			Integer docId = it.next();
	//			Integer docCountDelta = deltaMap.get(docId);
	//			if (docCountDelta != null && docCountDelta.intValue() != 0) {
	//				try {
	//					DocumentHbm doc = super.getDocumentHbmDao().load(docId);
	//					if (isLastVersion) {
	//						int newCount = doc.getUseCountLastVersion() + docCountDelta.intValue();
	//						if (newCount < 0) newCount = 0;
	//						doc.setUseCountLastVersion(newCount);
	//					} else {
	//						int newCount = doc.getUseCountPublishVersion() + docCountDelta.intValue();
	//						if (newCount < 0) newCount = 0;
	//						doc.setUseCountPublishVersion(newCount);
	//					}
	//				} catch (Exception e) {
	//					log.warn("Error updating documentCount for document " + docId + ": " + e.getMessage());
	//				}
	//			}
	//		}
	//	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.remote.ContentServiceSpringBase#handleUpdatePictureData(java.lang.Integer, byte[], byte[])
	 */
	@Override
	protected void handleUpdatePictureData(Integer pictureId, byte[] picture, String mimeType, byte[] thumbnail) throws Exception {
		PictureHbm pic = getPictureHbmDao().load(pictureId);
		pic.setMimeType(mimeType);
		pic.setPicture(picture);
		pic.setThumbnail(thumbnail);
	}

	@Override
	protected Integer handleGetPictureIdForUnitAndName(Integer unitId, String name) throws Exception {
		return getPictureHbmDao().getIdForNameAndUnit(unitId, name);
	}

	@Override
	protected Integer handleGetDocumentIdForNameAndUnit(String name, Integer unitId) throws Exception {
		return getDocumentHbmDao().getIdForNameAndUnit(name, unitId);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map handleGetResources4Unit(Integer unitId, boolean usedDoc, boolean unusedDoc, boolean usedPict, boolean unusedPict) throws Exception {
		Map<Object, ResourceUsageState> result = new HashMap<Object, ResourceUsageState>();
		Map<Integer, ResourceUsageState> documents = new HashMap<Integer, ResourceUsageState>();
		Map<Integer, ResourceUsageState> pictures = new HashMap<Integer, ResourceUsageState>();

		DocumentSlimValue[] dbDocuments = this.getAllSlimDocuments4Unit(unitId);
		PictureSlimstValue[] dbPictures = this.getAllSlimPictures4Unit(unitId);

		Map<ViewComponentHbm, List<ContentVersionHbm>> contentVersions = getAllContentVersions4Unit(unitId);
		getUsedResourcesFromContentVersions(contentVersions, documents, pictures);

		result.putAll(new DocumentResourceComparer().extract(dbDocuments, documents, usedDoc, unusedDoc));
		result.putAll(new PictureResourceComparer().extract(dbPictures, pictures, usedPict, unusedPict));

		return result;
	}

	private Map<ViewComponentHbm, List<ContentVersionHbm>> getAllContentVersions4Unit(Integer unitId) {
		Map<ViewComponentHbm, List<ContentVersionHbm>> contentVersions = new HashMap<ViewComponentHbm, List<ContentVersionHbm>>();
		List<ViewComponentHbm> rootViewDocuments = (List<ViewComponentHbm>) getViewComponentHbmDao().findRootViewComponents4Unit(unitId);

		if (rootViewDocuments != null && rootViewDocuments.size() > 0) {
			for (ViewComponentHbm root : rootViewDocuments) {
				contentVersions.putAll(getAllContentVersionsRecursive(root));
			}
		}
		return contentVersions;
	}

	private Map<ViewComponentHbm, List<ContentVersionHbm>> getAllContentVersionsRecursive(ViewComponentHbm root) {

		Map<ViewComponentHbm, List<ContentVersionHbm>> contentVersions = new HashMap<ViewComponentHbm, List<ContentVersionHbm>>();
		contentVersions.put(root, (List<ContentVersionHbm>) getContentVersionHbmDao().findContentVersionsByViewComponent(root.getViewComponentId()));
		Collection children = root.getChildren();
		if (children != null && children.size() > 0) {
			for (Object child : children) {
				contentVersions.putAll(getAllContentVersionsRecursive((ViewComponentHbm) child));
			}
		}

		return contentVersions;
	}

	/**
	 * Used to compare list of resources from database (image/pictures) with used resources in contents
	 * to extract resources that are not used
	 * @param <T> can be PictureSlimstValue or DocumentValue
	 */
	private static abstract class UnusedResourceComparer<T, K> {
		abstract Integer getId(T resource);

		abstract K load(Integer id);

		abstract UnitHbm getUnit(K resource);

		abstract K createResource(Integer id);

		public Map<T, ResourceUsageState> extract(T[] resources, Map<Integer, ResourceUsageState> used, boolean usedRes, boolean unusedRes) {
			Map<T, ResourceUsageState> resourceResult = new HashMap<T, ResourceUsageState>();
			if (resources != null && resources.length > 0) {
				boolean emptyUsedResources = false;
				if (used == null || used.size() == 0) {
					emptyUsedResources = true;
				}
				for (T resource : resources) {
					if (emptyUsedResources && unusedRes) {
						resourceResult.put(resource, ResourceUsageState.Unsused);
						continue;
					}
					Integer resourceId = this.getId(resource);
					if (!used.containsKey(resourceId)) {
						if (unusedRes) {
							resourceResult.put(resource, ResourceUsageState.Unsused);
						}
					} else {
						if (usedRes) {
							resourceResult.put(resource, used.get(resourceId));
						}
					}
				}
			}
			return resourceResult;
		}

		public Map<UnitHbm, List<K>> groupResourcesByUnit(Integer[] resourceIds) {
			Map<UnitHbm, List<K>> ressourcesPerUnit = new HashMap<UnitHbm, List<K>>();
			if (resourceIds != null && resourceIds.length > 0) {
				for (Integer resourceId : resourceIds) {
					K resource = load(resourceId);
					UnitHbm unit = getUnit(resource);
					if (ressourcesPerUnit.containsKey(unit)) {
						ressourcesPerUnit.get(unit).add(resource);
					} else {
						List<K> rsources = new ArrayList<K>();
						rsources.add(resource);
						ressourcesPerUnit.put(unit, rsources);
					}
				}
			}
			return ressourcesPerUnit;
		}

		public boolean checkResourcesInContentText(Node contentVersionNode, ResourceUsageState state, Boolean withHistory, List<K> resources, String type, String attributeName) throws Exception {
			Iterator it = XercesHelper.findNodes(contentVersionNode, "//" + type);
			boolean keepVersionForDelete = false;
			while (it.hasNext()) {
				Node node = (Node) it.next();
				Node attributeNode = node.getAttributes().getNamedItem(attributeName);
				if (attributeNode == null) {
					continue;
				}
				Integer resourceId = null;
				try {
					resourceId = Integer.parseInt(attributeNode.getNodeValue());
				} catch (NumberFormatException ex) {
					continue;
				}
				K resourceTemp = createResource(resourceId);
				if (resources.contains(resourceTemp)) {
					//if user does not want to delete history revisions also then error
					if (!withHistory) {
						if (state != ResourceUsageState.Unsused) {
							throw new Exception("validation exception");
						}
					} else {
						if (state != ResourceUsageState.Used) {
							//select history version for delete
							keepVersionForDelete = true;
						} else if (state == ResourceUsageState.Used) {
							throw new Exception("validation exception");
						}
					}
				}
			}
			return keepVersionForDelete;
		}
	}

	private class DocumentResourceComparer extends UnusedResourceComparer<DocumentSlimValue, DocumentHbm> {

		@Override
		Integer getId(DocumentSlimValue resource) {
			return resource.getDocumentId();
		}

		@Override
		DocumentHbm load(Integer id) {
			return getDocumentHbmDao().load(id);
		}

		@Override
		UnitHbm getUnit(DocumentHbm resource) {
			return resource.getUnit();
		}

		@Override
		DocumentHbm createResource(Integer id) {
			DocumentHbm resource = DocumentHbm.Factory.newInstance();
			resource.setDocumentId(id);
			return resource;
		}

	}

	private class PictureResourceComparer extends UnusedResourceComparer<PictureSlimstValue, PictureHbm> {

		@Override
		Integer getId(PictureSlimstValue resource) {
			return resource.getPictureId();
		}

		@Override
		PictureHbm load(Integer id) {
			return getPictureHbmDao().load(id);
		}

		@Override
		UnitHbm getUnit(PictureHbm resource) {
			return resource.getUnit();
		}

		@Override
		PictureHbm createResource(Integer id) {
			PictureHbm resource = PictureHbm.Factory.newInstance();
			resource.setPictureId(id);
			return resource;
		}

	}

	/**
	 * Gets used documents and pictures
	 * @param contentVersions - content versions where to find
	 * @param withHistory - if true selects the history version also
	 * @param documents - list of used documents
	 * @param pictures - list of used pictures
	 */
	private void getUsedResourcesFromContentVersions(Map<ViewComponentHbm, List<ContentVersionHbm>> contentVersions, Map<Integer, ResourceUsageState> documents, Map<Integer, ResourceUsageState> pictures) {
		if (contentVersions == null || contentVersions.size() == 0) {
			return;
		}

		for (Entry<ViewComponentHbm, List<ContentVersionHbm>> contentVersionGroup : contentVersions.entrySet()) {
			Integer maxVersion = getMaxVersion(contentVersionGroup.getValue());
			for (ContentVersionHbm contentVersion : contentVersionGroup.getValue()) {
				String content = contentVersion.getText();
				ResourceUsageState state = null;
				Integer currentVersion = Integer.decode(contentVersion.getVersion());
				if (currentVersion < maxVersion) {
					state = ResourceUsageState.UsedInOlderVersions;
				} else if (currentVersion == maxVersion) {
					state = ResourceUsageState.Used;
				}
				if (content != null) {
					try {
						Document domDocument = XercesHelper.string2Dom(content);
						//tags from content that represent documents and pictures
						getResourcesFromContentVersion(domDocument, state, documents, "document", "src");
						getResourcesFromContentVersion(domDocument, state, pictures, "picture", "description");
						getResourcesFromContentVersion(domDocument, state, pictures, "image", "src");
					} catch (Exception e) {
						log.info("could not parse used ressources: " + e.getMessage());
						if (log.isDebugEnabled()) log.debug("Parsing Error", e);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param contentVersions
	 * @param resourceId
	 * @param type
	 * @param attributeNameId
	 * @return
	 */
	private List<ContentVersionHbm> getContentVersionUsingResource(List<ContentVersionHbm> contentVersions, Integer resourceId, String type, String attributeNameId) {
		List<ContentVersionHbm> usedContentVersion = new ArrayList<ContentVersionHbm>();
		if (contentVersions == null || contentVersions.size() == 0) {
			return usedContentVersion;
		}
		for (ContentVersionHbm contentVersion : contentVersions) {
			String content = contentVersion.getText();
			if (content != null) {
				try {
					Document document = XercesHelper.string2Dom(content);
					if (isResourceInContentVersion(document, resourceId, type, attributeNameId)) {
						usedContentVersion.add(contentVersion);
					}
				} catch (Exception e) {
					log.info("could not parse used ressources: " + e.getMessage());
					if (log.isDebugEnabled()) log.debug("Parsing Error", e);
				}
			}
		}
		return usedContentVersion;
	}

	/**
	 * 
	 * @param contentVersionNode xml node
	 * @param state 
	 * @param resources used resources list ids
	 * @param type tag name of the resource
	 * @param attributeName attribute that indicates the id of the resource
	 */
	private void getResourcesFromContentVersion(Node contentVersionNode, ResourceUsageState state, Map<Integer, ResourceUsageState> resources, String type, String attributeName) {
		Iterator it = XercesHelper.findNodes(contentVersionNode, "//" + type);
		while (it.hasNext()) {
			Node node = (Node) it.next();
			Node attributeNode = node.getAttributes().getNamedItem(attributeName);
			if (attributeNode == null) {
				continue;
			}
			Integer resourceId = null;
			try {
				resourceId = Integer.parseInt(attributeNode.getNodeValue());
			} catch (NumberFormatException e) {
				continue;
			}
			if (resources.containsKey(resourceId)) {
				//if state is Used I don't care if they are used in older version too
				//State Used is powerfull than UsedInOlderVersions
				if (resources.get(resourceId) == ResourceUsageState.Used) {
					continue;
				}
			}
			resources.put(resourceId, state);
		}
	}

	private boolean isResourceInContentVersion(Node contentVersionNode, Integer resourceId, String type, String attributeName) {
		Iterator it = XercesHelper.findNodes(contentVersionNode, "//" + type + "[@" + attributeName + "=" + resourceId.toString() + "]");
		return it.hasNext();
	}

	private PrintStream createEditionOutputStream(EditionHbm edition) throws IOException {
		//create dir for deploys
		String dir = getTizzitPropertiesBeanSpring().getDatadir() + File.separatorChar + "deploys";
		File fDir = new File(dir);
		fDir.mkdirs();

		String fileName = fDir.getAbsolutePath() + File.separatorChar + "deploy_file_edition_" + edition.getEditionId() + "_byUser_" + edition.getCreator().getUserId() + "_" + edition.getCreationDate() + ".xml.gz";
		File fle = new File(fileName);
		edition.setEditionFileName(fle.getAbsolutePath());
		FileOutputStream fout = new FileOutputStream(fle);
		GZIPOutputStream gzoudt = new GZIPOutputStream(fout);
		PrintStream out = new PrintStream(gzoudt, true, "UTF-8");

		//if (log.isDebugEnabled()) log.debug("Invoker is: " + edition.getCreator() + " within Site " + site.getName());
		if (log.isDebugEnabled()) log.debug("Dummy-Editon create");
		out.println("<edition>");
		if (log.isDebugEnabled()) log.debug("editionToXml");
		out.println(edition.toXml());
		System.gc();
		return out;
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.remote.ContentServiceSpringBase#handleDeployEdition(de.juwimm.cms.model.EditionHbm)
	 */
	@Override
	protected void handleDeployEdition(Integer editionId) throws Exception {
		try {
			editionCronService.logEditionStatusInfo(LiveserverDeployStatus.CreateDeployFileForExport, editionId);
			//if (log.isInfoEnabled())log.info("createDeployFile " + AuthenticationHelper.getUserName());
			EditionHbm edition = getEditionHbmDao().load(editionId);

			PrintStream out = createEditionOutputStream(edition);
			//
			if (log.isDebugEnabled()) log.debug("siteToXml");
			getEditionHbmDao().siteToXml(edition.getSiteId(), out, edition);
			System.gc();
			if (log.isDebugEnabled()) log.debug("picturesToXmlRecursive");
			getEditionHbmDao().picturesToXmlRecursive(null, edition.getSiteId(), out, edition);
			System.gc();
			if (log.isDebugEnabled()) log.debug("documentsToXmlRecursive");
			getEditionHbmDao().documentsToXmlRecursive(null, edition.getSiteId(), out, true, edition);
			System.gc();
			if (log.isDebugEnabled()) log.debug("unitsToXmlRecursive");
			getEditionHbmDao().unitsToXmlRecursive(edition.getSiteId(), out, edition);
			System.gc();
			if (log.isDebugEnabled()) log.debug("hostsToXmlRecursive");
			getEditionHbmDao().hostsToXmlRecursive(edition.getSiteId(), out, edition);
			if (log.isDebugEnabled()) log.debug("viewdocumentsToXmlRecursive");
			getEditionHbmDao().viewdocumentsToXmlRecursive(edition.getSiteId(), out, edition);
			if (log.isDebugEnabled()) log.debug("realmsToXmlRecursive");
			getEditionHbmDao().realmsToXmlRecursive(edition.getSiteId(), out, edition);
			System.gc();
			if (log.isDebugEnabled()) log.debug("Creating ViewComponent Data");
			Iterator vdIt = getViewDocumentHbmDao().findAll(edition.getSiteId()).iterator();
			while (vdIt.hasNext()) {
				ViewDocumentHbm vdl = (ViewDocumentHbm) vdIt.next();
				getViewComponentHbmDao().toXml(vdl.getViewComponent(), null, true, true, false, false, -1, false, false, out);
			}
			if (log.isDebugEnabled()) log.debug("Finished creating ViewComponent Data");
			out.println("</edition>");

			out.flush();
			out.close();
			out = null;
		} catch (Exception e) {
			editionCronService.logEditionStatusException(editionId, e.getMessage());
			throw new UserException(e.getMessage(), e);
		}
	}

	@Override
	protected void handleDeployUnitEdition(Integer editionId, Integer unitId) throws Exception {
		try {
			editionCronService.logEditionStatusInfo(LiveserverDeployStatus.CreateDeployFileForExport, editionId);
			//if (log.isInfoEnabled())log.info("createDeployFile " + AuthenticationHelper.getUserName());
			EditionHbm edition = getEditionHbmDao().load(editionId);

			PrintStream out = createEditionOutputStream(edition);
			// site info is needed to connect to the live server
			if (log.isDebugEnabled()) log.debug("siteToXml");
			getEditionHbmDao().siteToXml(edition.getSiteId(), out, edition);
			System.gc();
			// Alle ..toXML + unitID to reuse them in unitDeploy
			if (log.isDebugEnabled()) log.debug("picturesToXmlRecursive");
			getEditionHbmDao().picturesToXmlRecursive(unitId, edition.getSiteId(), out, edition);
			System.gc();
			if (log.isDebugEnabled()) log.debug("documentsToXmlRecursive");
			getEditionHbmDao().documentsToXmlRecursive(unitId, edition.getSiteId(), out, true, edition);
			System.gc();
			if (log.isDebugEnabled()) log.debug("unitToXml");
			getEditionHbmDao().unitToXml(unitId, out, edition);
			System.gc();
			//			if (log.isDebugEnabled()) log.debug("viewdocumentsToXmlRecursive");
			//			getEditionHbmDao().viewdocumentsToXmlRecursive(edition.getSiteId(), out, edition);
			if (log.isDebugEnabled()) log.debug("realmsToXmlUsed: unit - " + unitId);
			getEditionHbmDao().realmsToXmlUsed(unitId, out, edition);
			System.gc();
			if (log.isDebugEnabled()) log.debug("Creating ViewComponent Data");
			Iterator vdIt = getViewDocumentHbmDao().findAll(edition.getSiteId()).iterator();
			while (vdIt.hasNext()) {
				ViewDocumentHbm vdl = (ViewDocumentHbm) vdIt.next();
				ViewComponentHbm vch = getViewComponentHbmDao().find4Unit(unitId, vdl.getViewDocumentId());
				if (vch != null) getViewComponentHbmDao().toXml(vch, unitId, true, true, true, false, -1, false, false, out);
			}
			if (log.isDebugEnabled()) log.debug("Finished creating ViewComponent Data");
			out.println("</edition>");

			out.flush();
			out.close();
			out = null;
		} catch (Exception e) {
			editionCronService.logEditionStatusException(editionId, e.getMessage());
			throw new UserException(e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleRemoveResources(Integer[] pictureIds, Integer[] documentsIds, boolean forceDeleteHistory) throws Exception {
		Map<UnitHbm, List<PictureHbm>> picturesPerUnits = new PictureResourceComparer().groupResourcesByUnit(pictureIds);
		Map<UnitHbm, List<DocumentHbm>> documentsPerUnits = new DocumentResourceComparer().groupResourcesByUnit(documentsIds);

		Map<UnitHbm, List<List>> resourcesPerUnits = new HashMap<UnitHbm, List<List>>();

		//compact unit per resources
		//conventionon <UnitHbm,List<List>>: first object in list are the pictures, second the documents
		for (Entry<UnitHbm, List<PictureHbm>> picturePerUnit : picturesPerUnits.entrySet()) {
			List<List> resourcesList = new ArrayList<List>();
			resourcesList.add(0, picturePerUnit.getValue());
			if (documentsPerUnits.containsKey(picturePerUnit.getKey())) {
				resourcesList.add(1, documentsPerUnits.get(picturePerUnit.getKey()));
				documentsPerUnits.remove(picturePerUnit.getKey());
			} else {
				resourcesList.add(1, new ArrayList());
			}
			resourcesPerUnits.put(picturePerUnit.getKey(), resourcesList);
		}

		for (Entry<UnitHbm, List<DocumentHbm>> documentsPerUnit : documentsPerUnits.entrySet()) {
			List<List> resourcesList = new ArrayList<List>();
			resourcesList.add(0, new ArrayList());
			resourcesList.add(1, documentsPerUnit.getValue());
			resourcesPerUnits.put(documentsPerUnit.getKey(), resourcesList);
		}

		//Validate resources are used or not
		List<ContentVersionHbm> historyContentVersion = new ArrayList<ContentVersionHbm>();
		for (Entry<UnitHbm, List<List>> resourcesPerUnit : resourcesPerUnits.entrySet()) {
			Map<ViewComponentHbm, List<ContentVersionHbm>> contentVersions = getAllContentVersions4Unit(resourcesPerUnit.getKey().getUnitId());
			historyContentVersion.addAll(checkResourcesInContentVersions(contentVersions, forceDeleteHistory, resourcesPerUnit.getValue().get(0), resourcesPerUnit.getValue().get(1)));
		}

		if (forceDeleteHistory) {
			getContentVersionHbmDao().remove(historyContentVersion);
		}

		getDocumentHbmDao().deleteDocuments(documentsIds);
		getPictureHbmDao().deletePictures(pictureIds);
	}

	private List<ContentVersionHbm> checkResourcesInContentVersions(Map<ViewComponentHbm, List<ContentVersionHbm>> contentVersions, boolean withHistory, List<PictureHbm> pictures, List<DocumentHbm> documents) throws Exception {
		DocumentResourceComparer documentComparer = new DocumentResourceComparer();
		PictureResourceComparer pictureComparer = new PictureResourceComparer();
		List<ContentVersionHbm> contentsToDelete = new ArrayList<ContentVersionHbm>();
		if (contentVersions == null || contentVersions.size() == 0) {
			return contentsToDelete;
		}

		for (Entry<ViewComponentHbm, List<ContentVersionHbm>> contentVersionGroup : contentVersions.entrySet()) {
			Integer maxVersion = getMaxVersion(contentVersionGroup.getValue());
			for (ContentVersionHbm contentVersion : contentVersionGroup.getValue()) {
				String content = contentVersion.getText();
				ResourceUsageState state = null;
				Integer currentVersion = Integer.decode(contentVersion.getVersion());
				if (currentVersion < maxVersion) {
					state = ResourceUsageState.UsedInOlderVersions;
				} else if (currentVersion == maxVersion) {
					state = ResourceUsageState.Used;
				}
				if (content != null) {
					Document domDocument = null;
					try {
						domDocument = XercesHelper.string2Dom(content);
					} catch (Exception e) {
						log.info("could not parse used ressources: " + e.getMessage());
						if (log.isDebugEnabled()) log.debug("Parsing Error", e);
					}
					if (domDocument != null) {
						boolean deleteContent = false;
						deleteContent = deleteContent || documentComparer.checkResourcesInContentText(domDocument, state, withHistory, documents, "document", "src");
						deleteContent = deleteContent || pictureComparer.checkResourcesInContentText(domDocument, state, withHistory, pictures, "picture", "description");
						deleteContent = deleteContent || pictureComparer.checkResourcesInContentText(domDocument, state, withHistory, pictures, "image", "src");
						if (deleteContent) {
							contentsToDelete.add(contentVersion);
						}
					}

				}
			}
		}
		return contentsToDelete;
	}

	/**
	 * Also in the contentVersions it replaces the PUBLS version with the max version  
	 * @param contentVersions - The list of contents should be all the contents from a view
	 * @return
	 */
	private Integer getMaxVersion(List<ContentVersionHbm> contentVersions) {
		Integer maxVersion = 0;
		ContentVersionHbm publishContentVersion = null;
		//search max revision
		for (ContentVersionHbm contentVersion : contentVersions) {
			if (contentVersion.getVersion().equals(Constants.PUBLISH_VERSION)) {
				publishContentVersion = contentVersion;
				break;
			}
			int versionNumber = Integer.decode(contentVersion.getVersion());
			if (versionNumber > maxVersion) {
				maxVersion = versionNumber;
			}
		}

		if (publishContentVersion != null) {
			publishContentVersion.setVersion(Integer.toString(++maxVersion));
		}
		return maxVersion;
	}

	@Override
	protected void handleMakeContentOffline(Integer viewComponentId) throws Exception {
		ViewComponentHbm viewComponent = getViewComponentHbmDao().load(viewComponentId);
		ContentHbm content = getContentHbmDao().load(Integer.decode(viewComponent.getReference()));
		ContentVersionHbm publishContentVersion = content.getContentVersionForPublish();
		//update online status
		viewComponent.setOnline((byte) 0);
		getViewComponentHbmDao().update(viewComponent);

		if (publishContentVersion == null) {
			return;
		}
		//remove publish content version
		content.getContentVersions().remove(publishContentVersion);
		getContentHbmDao().update(content);
		removeContentVersion(publishContentVersion.getContentVersionId());
	}

	@Override
	protected boolean handleGetPictureThumbnailPopup(Integer pictureId) throws Exception {
		PictureHbm picture = super.getPictureHbmDao().load(pictureId);
		return picture.isThumbnailPopup();
	}

	@Override
	protected void handleUpdatePictureThumbnailPopup(boolean withThumbnailPopup, Integer pictureId) throws Exception {
		PictureHbm picture = super.getPictureHbmDao().load(pictureId);
		picture.setThumbnailPopup(withThumbnailPopup);

	}

	@Override
	protected Set handleGetDocumentUsage(Integer documentId) throws Exception {
		DocumentHbm document = super.getDocumentHbmDao().load(documentId);
		Set<ViewComponentValue> result = new HashSet<ViewComponentValue>();
		Map<ViewComponentHbm, List<ContentVersionHbm>> contentVersions = getAllContentVersions4Unit(document.getUnit().getUnitId());
		for (Entry<ViewComponentHbm, List<ContentVersionHbm>> contentVersion : contentVersions.entrySet()) {
			List<ContentVersionHbm> usedContentVersions = getContentVersionUsingResource(contentVersion.getValue(), documentId, "document", "src");
			if (usedContentVersions != null && usedContentVersions.size() > 0) {
				result.add(contentVersion.getKey().getDao());
			}
		}
		return result;
	}

	@Override
	protected Set handleGetPictureUsage(Integer pictureId) throws Exception {
		PictureHbm picture = getPictureHbmDao().load(pictureId);
		Set<ViewComponentValue> result = new HashSet<ViewComponentValue>();
		Map<ViewComponentHbm, List<ContentVersionHbm>> contentVersions = getAllContentVersions4Unit(picture.getUnit().getUnitId());
		for (Entry<ViewComponentHbm, List<ContentVersionHbm>> contentVersion : contentVersions.entrySet()) {
			List<ContentVersionHbm> usedContentVersions = getContentVersionUsingResource(contentVersion.getValue(), pictureId, "picture", "description");
			if (usedContentVersions != null && usedContentVersions.size() > 0) {
				result.add(contentVersion.getKey().getDao());
				continue;
			}
			usedContentVersions = getContentVersionUsingResource(contentVersion.getValue(), pictureId, "image", "src");
			if (usedContentVersions != null && usedContentVersions.size() > 0) {
				result.add(contentVersion.getKey().getDao());
			}
		}
		return result;
	}

	@Override
	protected List handleGetEditions() throws Exception {
		List<EditionHbm> editions = (List<EditionHbm>) getEditionHbmDao().loadAll();
		List<EditionValue> editionValues = new ArrayList<EditionValue>();
		if (editions.size() > 0) {
			for (EditionHbm edition : editions) {
				editionValues.add(edition.getDao());
			}
		}
		return editionValues;
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.remote.ContentServiceSpringBase#handleDeployRootUnitEdition(java.lang.Integer)
	 */
	@Override
	protected void handleDeployRootUnitEdition(Integer editionId) throws Exception {
		try {
			editionCronService.logEditionStatusInfo(LiveserverDeployStatus.CreateDeployFileForExport, editionId);
			//if (log.isInfoEnabled())log.info("createDeployFile " + AuthenticationHelper.getUserName());
			EditionHbm edition = getEditionHbmDao().load(editionId);

			PrintStream out = createEditionOutputStream(edition);
			// site info is needed to connect to the live server
			if (log.isDebugEnabled()) log.debug("siteToXml");
			getEditionHbmDao().siteToXml(edition.getSiteId(), out, edition);
			System.gc();
			// Alle ..toXML + unitID to reuse them in unitDeploy
			if (log.isDebugEnabled()) log.debug("picturesToXmlRecursive");
			//TODO: change -1 to null and check edition.type later .... - for the moment -1 = rootdeploy
			getEditionHbmDao().picturesToXmlRecursive(-1, edition.getSiteId(), out, edition);
			System.gc();
			if (log.isDebugEnabled()) log.debug("documentsToXmlRecursive");
			//TODO: change -1 to null and check edition.type later .... - for the moment -1 = rootdeploy
			getEditionHbmDao().documentsToXmlRecursive(-1, edition.getSiteId(), out, true, edition);
			System.gc();

			if (log.isDebugEnabled()) log.debug("unitsToXmlRecursive");
			getEditionHbmDao().unitsToXmlRecursive(edition.getSiteId(), out, edition);
			System.gc();
			//			if (log.isDebugEnabled()) log.debug("viewdocumentsToXmlRecursive");
			//			getEditionHbmDao().viewdocumentsToXmlRecursive(edition.getSiteId(), out, edition);
			if (log.isDebugEnabled()) log.debug("hostsToXmlRecursive");
			getEditionHbmDao().hostsToXmlRecursive(edition.getSiteId(), out, edition);

			if (log.isDebugEnabled()) log.debug("viewdocumentsToXmlRecursive");
			getEditionHbmDao().viewdocumentsToXmlRecursive(edition.getSiteId(), out, edition);
			if (log.isDebugEnabled()) log.debug("realmsToXmlRecursive");
			getEditionHbmDao().realmsToXmlUsed(edition.getUnitId(), out, edition);
			System.gc();
			if (log.isDebugEnabled()) log.debug("Creating ViewComponent Data");
			Iterator vdIt = getViewDocumentHbmDao().findAll(edition.getSiteId()).iterator();
			while (vdIt.hasNext()) {
				ViewDocumentHbm vdl = (ViewDocumentHbm) vdIt.next();
				//getViewComponentHbmDao().toXml(vdl.getViewComponent(), edition.getUnitId(), true, false, -1, false, false, out);
				getViewComponentHbmDao().toXml(vdl.getViewComponent(), edition.getUnitId(), true, true, true, false, -1, false, false, out);
				// get all VC's with UnitID in this VD and deploy them too
				Iterator vcIt = getViewComponentHbmDao().findAllWithUnit(vdl.getViewDocumentId()).iterator();
				while (vcIt.hasNext()) {
					ViewComponentHbm vc = (ViewComponentHbm) vcIt.next();
					if (vc.getAssignedUnit().getUnitId().compareTo(edition.getUnitId()) != 0) {
						getViewComponentHbmDao().toXml(vc, null, false, false, 0, false, false, out);
					}
				}
			}

			out.println("</edition>");

			out.flush();
			out.close();
			out = null;
		} catch (Exception e) {
			editionCronService.logEditionStatusException(editionId, e.getMessage());
			throw new UserException(e.getMessage(), e);
		}
	}
}
