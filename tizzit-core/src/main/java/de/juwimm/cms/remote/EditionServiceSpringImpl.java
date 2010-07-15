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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.StringReader;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import javax.ejb.CreateException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.SpringSecurityException;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.providers.rcp.RemoteAuthenticationManager;
import org.tizzit.util.XercesHelper;
import org.tizzit.util.xml.XMLWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.XMLFilter;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.authorization.remote.AuthorizationServiceSpring;
import de.juwimm.cms.beans.EditionCronService;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.Constants.LiveserverDeployStatus;
import de.juwimm.cms.components.model.AddressHbm;
import de.juwimm.cms.components.model.DepartmentHbm;
import de.juwimm.cms.components.model.PersonHbm;
import de.juwimm.cms.components.model.TalktimeHbm;
import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.model.ContentHbm;
import de.juwimm.cms.model.ContentVersionHbm;
import de.juwimm.cms.model.DocumentHbm;
import de.juwimm.cms.model.EditionHbm;
import de.juwimm.cms.model.HostHbm;
import de.juwimm.cms.model.HostHbmImpl;
import de.juwimm.cms.model.PictureHbm;
import de.juwimm.cms.model.ShortLinkHbm;
import de.juwimm.cms.model.ShortLinkHbmImpl;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.TaskHbm;
import de.juwimm.cms.model.TaskHbmImpl;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.model.ViewComponentHbm;
import de.juwimm.cms.model.ViewComponentHbmImpl;
import de.juwimm.cms.model.ViewDocumentHbm;
import de.juwimm.cms.remote.helper.AuthenticationHelper;
import de.juwimm.cms.safeguard.model.Realm2viewComponentHbm;
import de.juwimm.cms.safeguard.model.Realm2viewComponentHbmImpl;
import de.juwimm.cms.safeguard.model.RealmJaasHbm;
import de.juwimm.cms.safeguard.model.RealmJdbcHbm;
import de.juwimm.cms.safeguard.model.RealmLdapHbm;
import de.juwimm.cms.safeguard.model.RealmSimplePwHbm;
import de.juwimm.cms.safeguard.model.RealmSimplePwUserHbm;
import de.juwimm.cms.safeguard.model.RealmSimplePwUserHbmDaoImpl;
import de.juwimm.cms.util.EditionBlobContentHandler;
import de.juwimm.cms.vo.EditionValue;
import de.juwimm.cms.vo.ShortLinkValue;

/**
 * @see de.juwimm.cms.remote.EditionServiceSpring
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class EditionServiceSpringImpl extends EditionServiceSpringBase {
	private static Log log = LogFactory.getLog(EditionServiceSpringImpl.class);
	@Autowired
	EditionCronService editionCronService;
	/*
	 * These Hashtables are needed for the FullEdition-Import only
	 */
	private Hashtable<Integer, Integer> mappingUnits = null;
	private Hashtable<Integer, Integer> mappingUnitsReverse = null;
	private Hashtable<Integer, Integer> mappingVDs = null;
	private Hashtable<Integer, Integer> mappingVCs = null;
	private Hashtable<Integer, Integer> mappingPics = null;
	private Hashtable<Integer, Integer> mappingDocs = null;
	private Hashtable<Integer, Long> mappingAddresses = null;
	private Hashtable<Integer, Long> mappingDepartments = null;
	private Hashtable<Integer, Long> mappingPersons = null;
	private Hashtable<Integer, Long> mappingTalktime = null;
	private Hashtable<String, Integer> startPageBackup = null;
	/**
	 * Key: RealmId, Value: old LoginPageId
	 */
	private Hashtable<Integer, Integer> loginPagesSimplePwRealmBackup = null;
	private Hashtable<Integer, Integer> loginPagesJdbcRealmBackup = null;
	private Hashtable<Integer, Integer> loginPagesJaasRealmBackup = null;
	private Hashtable<Integer, Integer> loginPagesLdapRealmBackup = null;
	private Hashtable<Integer, Integer> loginPagesRealm2viewComponentBackup = null;
	private Hashtable<Integer, Integer> mappingRealmsSimplePw = null;
	private Hashtable<Integer, Integer> mappingRealmsJdbc = null;
	private Hashtable<Integer, Integer> mappingRealmsLdap = null;
	private Hashtable<Integer, Integer> mappingRealmsJaas = null;
	/**
	 * Key: old SimplePwRealmId Value: old LoginPageId
	 */
	private Hashtable<Integer, Integer> loginPagesRealmsSimplePw = null;
	private Hashtable<Integer, Integer> loginPagesRealmsJdbc = null;
	private Hashtable<Integer, Integer> loginPagesRealmsLdap = null;
	private Hashtable<Integer, Integer> loginPagesRealmsJaas = null;
	/**
	 * Key: old protected ViewComponentId Value: old LoginPageId
	 */
	private Hashtable<Integer, Integer> loginPagesRealm2vc = null;

	@Override
	public void handleRemoveEdition(Integer editionId) {
		EditionHbm edition = getEditionHbmDao().load(editionId);
		if (log.isInfoEnabled()) log.info("Deleting edition and attachments: " + edition.getEditionId());
		if (edition.getEditionFileName() != null) {
			File f = new File(edition.getEditionFileName());
			try {
				f.delete();
			} catch (SecurityException se) {
				log.warn("Could not delete file: " + f.getAbsolutePath() + " " + se.getStackTrace());
			}
		}
		//getEditionHbmDao().remove(edition);
	}

	private Document getDocumentFromFile(File preparsedXMLfile, String editionFileName) throws Exception {
		if (log.isInfoEnabled()) log.info("Finished writing Edition to File, starting to import it as GZIP-InputStream...");
		XMLFilter filter = new XMLFilterImpl(XMLReaderFactory.createXMLReader());
		preparsedXMLfile = File.createTempFile("edition_import_preparsed_", ".xml");
		if (log.isDebugEnabled()) log.debug("preparsedXMLfile: " + preparsedXMLfile.getAbsolutePath());
		XMLWriter xmlWriter = new XMLWriter(new OutputStreamWriter(new FileOutputStream(preparsedXMLfile)));
		filter.setContentHandler(new EditionBlobContentHandler(xmlWriter, preparsedXMLfile));
		InputSource saxIn = null;
		try {
			try {
				saxIn = new InputSource(new GZIPInputStream(new FileInputStream(editionFileName)));
			} catch (Exception exe) {
				saxIn = new InputSource(new BufferedReader(new FileReader(editionFileName)));
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
		// BufferedReader br = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
		// InputSource in = new InputSource(br);
		InputSource domIn = new InputSource(new FileInputStream(preparsedXMLfile));
		return XercesHelper.inputSource2Dom(domIn);
	}

	@Override
	public void handleImportEdition(Integer siteId, Integer editionId, String editionFileName, Integer rootVcId, boolean useNewIds) throws Exception {
		this.editionCronService.logEditionStatusInfo(LiveserverDeployStatus.ImportStarted, editionId);
		mappingUnits = new Hashtable<Integer, Integer>();
		mappingUnitsReverse = new Hashtable<Integer, Integer>();
		mappingVDs = new Hashtable<Integer, Integer>();
		mappingVCs = new Hashtable<Integer, Integer>();
		mappingPics = new Hashtable<Integer, Integer>();
		mappingDocs = new Hashtable<Integer, Integer>();
		mappingAddresses = new Hashtable<Integer, Long>();
		mappingDepartments = new Hashtable<Integer, Long>();
		mappingPersons = new Hashtable<Integer, Long>();
		mappingTalktime = new Hashtable<Integer, Long>();
		startPageBackup = new Hashtable<String, Integer>();
		mappingRealmsSimplePw = new Hashtable<Integer, Integer>();
		mappingRealmsJdbc = new Hashtable<Integer, Integer>();
		mappingRealmsLdap = new Hashtable<Integer, Integer>();
		mappingRealmsJaas = new Hashtable<Integer, Integer>();
		loginPagesRealmsSimplePw = new Hashtable<Integer, Integer>();
		loginPagesRealmsJdbc = new Hashtable<Integer, Integer>();
		loginPagesRealmsLdap = new Hashtable<Integer, Integer>();
		loginPagesRealmsJaas = new Hashtable<Integer, Integer>();
		loginPagesRealm2vc = new Hashtable<Integer, Integer>();
		File preparsedXMLfile = null;
		EditionHbm edition = null;
		try {
			if (log.isInfoEnabled()) {
				if (rootVcId == null) {
					log.info("processFileImport for Site: " + siteId);
				} else {
					log.info("processFileImport for Site: " + siteId + " in the RootVCId: " + rootVcId);
				}
			}
			edition = getEditionHbmDao().load(editionId);
			UnitHbm rootUnit = null;
			Integer rootUnitId = null;
			ViewComponentHbm viewComponent = null;
			if (edition.getDeployType().compareTo(Constants.DEPLOY_TYPE_PAGE) == 0 || edition.getDeployType().compareTo(Constants.DEPLOY_TYPE_UNIT) == 0) {
				try {
					log.info("try to load ViewComponent: " + rootVcId);
					viewComponent = getViewComponentHbmDao().load(rootVcId);
					rootUnit = viewComponent.getAssignedUnit();
					//					if (viewComponent.isRoot()) {
					//						rootVcId = null;
					//						// context.setRollbackOnly();
					//						//						throw new RuntimeException("You can't import an Unit-Deploy as Root-Unit at this moment");
					//					}
				} catch (Exception e) {
					if (log.isInfoEnabled()) log.info("The given rootVcId " + rootVcId + " does not belong to any viewcomponent");
				}
			}

			if (log.isInfoEnabled()) log.info("Finished writing Edition to File, starting to import it as GZIP-InputStream...");
			preparsedXMLfile = File.createTempFile("edition_import_preparsed_", ".xml");
			Document doc = getDocumentFromFile(preparsedXMLfile, editionFileName);
			/*
			 * DOMParser parser = new DOMParser(); parser.parse(domIn); org.w3c.dom.Document doc = parser.getDocument();
			 */

			/*
			 * #################################################################################### 
			 * S T E P 0.5 create/update Site 
			 * ####################################################################################
			 */
			SiteHbm importSite = null;
			try{
				importSite = getSiteHbmDao().load(siteId);
			} catch(Exception e){
				if(log.isDebugEnabled()) log.debug("Could not find site with Id: " + siteId + " will create it now"); 
			}
			boolean siteIsNew = false;
			if (importSite == null) {
				importSite = SiteHbm.Factory.newInstance();
				siteIsNew = true;
				importSite.setSiteId(siteId);
			} else {
				if (log.isDebugEnabled()) log.debug("Site with id: " + siteId + " found - updating it now");
			}
			try {
				Element node = (Element) XercesHelper.findNode(doc, "/edition/site");
				importSite.setName(getNValNotNull(node, "name"));
				importSite.setShortName(getNValNotNull(node, "shortName"));
				importSite.setConfigXML(getNValNotNull(node, "siteConfig"));
				importSite.setMandatorDir(getNValNotNull(node, "mandatorDir"));
				importSite.setCacheExpire(new Integer(getNVal(node, "cacheExpire")));
				importSite.setWysiwygImageUrl(getNValNotNull(node, "wysiwygImageUrl"));
				importSite.setHelpUrl(getNValNotNull(node, "helpUrl"));
				importSite.setDcfUrl(getNValNotNull(node, "dcfUrl"));
				importSite.setPreviewUrl(getNValNotNull(node, "previewUrl"));
				importSite.setPageNameFull(getNValNotNull(node, "pageNameFull"));
				importSite.setPageNameContent(getNValNotNull(node, "pageNameContent"));
				importSite.setPageNameSearch(getNValNotNull(node, "pageNameSearch"));
				importSite.setLastModifiedDate(new Long(getNVal(node, "lastModifiedDate")));

				String str = getNVal(node, "rootUnitId");
				Integer defaultViewDocumentId_import = null;
				Integer siteGroupId_import = null;
				if (str != null) {
					rootUnitId = Integer.decode(str);
				}
				str = getNVal(node, "defaultViewDocumentId");
				if (str != null) {
					defaultViewDocumentId_import = Integer.decode(str);
				}
				str = getNVal(node, "siteGroupId");
				if (str != null) {
					siteGroupId_import = new Integer(str);
				}
			} catch (Exception e) {
				log.error("Error in Import while creating/updating site: " + siteId, e);
				throw new UserException("Error in Import while creating/updating site: " + siteId);
			}
			if (siteIsNew) {
				getSiteHbmDao().create(importSite);
			}
			// set this site as active site for user 
			// in case the site was new it could not be done at login
			getUserHbmDao().load(AuthenticationHelper.getUserName()).setActiveSite(importSite);
			if (rootUnit != null) {
				// if provided a unit import, we will fillup the mapping for this unit
				Integer newUnitId = rootUnit.getUnitId();
				Element unitNode = (Element) XercesHelper.findNode(doc, "/edition/units/unit");
				Integer oldUnitId = new Integer(unitNode.getAttribute("id"));
				if (log.isDebugEnabled()) log.debug("mapping for unit-import: OLD:" + oldUnitId + " NEW:" + newUnitId);
				mappingUnits.put(oldUnitId, newUnitId);
				mappingUnitsReverse.put(newUnitId, oldUnitId);
			}

			/*
			 * #################################################################################### 
			 * S T E P 1 Clean the complete Database
			 * ####################################################################################
			 */
			//TODO: on UnitDeploy delete just unit relevant stuff, for rootImport site stuff too
			this.editionCronService.logEditionStatusInfo(LiveserverDeployStatus.ImportCleanDatabase, editionId);
			if (log.isDebugEnabled()) log.debug("Starting with STEP 1/6");

			if (edition.getDeployType().compareTo(Constants.DEPLOY_TYPE_ROOT) == 0 || edition.getDeployType().compareTo(Constants.DEPLOY_TYPE_FULL) == 0) {
				Collection vdocs = getViewDocumentHbmDao().findAll(siteId);
				Iterator vdocsIt = vdocs.iterator();
				while (vdocsIt.hasNext()) {
					ViewDocumentHbm viewDocument = (ViewDocumentHbm) vdocsIt.next();
					if (log.isDebugEnabled()) log.debug("Found VDocument to remove: " + viewDocument.getLanguage() + " " + viewDocument.getViewType());
					ViewComponentHbm rootViewComponent = viewDocument.getViewComponent();
					if (log.isDebugEnabled()) log.debug("Removing rootVC: " + rootViewComponent.getViewComponentId());
					getViewComponentHbmDao().remove(rootViewComponent);
					getViewDocumentHbmDao().remove(viewDocument);
					if (log.isDebugEnabled()) log.debug("Removing SUCC!");
				}
				Collection units = getUnitHbmDao().findAll(siteId);
				getUnitHbmDao().remove(units);
			}

			if (edition.getDeployType().compareTo(Constants.DEPLOY_TYPE_UNIT) == 0 || edition.getDeployType().compareTo(Constants.DEPLOY_TYPE_FULL) == 0 || edition.getDeployType().compareTo(Constants.DEPLOY_TYPE_ROOT) == 0) {
				ViewComponentHbm rootViewComponent = getViewComponentHbmDao().find4Unit(edition.getUnitId(), edition.getViewDocumentId());
				if (rootViewComponent != null) {
					Collection vComps = rootViewComponent.getChildren();
					while (vComps.iterator().hasNext()) {
						ViewComponentHbm vc = (ViewComponentHbm) vComps.iterator().next();
						if (vc.getAssignedUnit().equals(rootUnit)) {
							Realm2viewComponentHbm r2vc = getRealm2viewComponentHbmDao().findByViewComponent(vc.getViewComponentId());
							if (r2vc != null) {
								if (r2vc.getJaasRealm() != null) getRealmJaasHbmDao().remove(r2vc.getJaasRealm());
								if (r2vc.getJdbcRealm() != null) getRealmJdbcHbmDao().remove(r2vc.getJdbcRealm());
								if (r2vc.getLdapRealm() != null) getRealmLdapHbmDao().remove(r2vc.getLdapRealm());
								if (r2vc.getSimplePwRealm() != null) getRealmSimplePwHbmDao().remove(r2vc.getSimplePwRealm());
							}
						}
						//vc.getChildren();
					}
					if (log.isDebugEnabled()) log.debug("Removing rootVC: " + rootViewComponent.getViewComponentId());
					//getViewComponentHbmDao().remove(rootViewComponent);
					if (log.isDebugEnabled()) log.debug("Removing SUCC!");
				} else {
					if (log.isDebugEnabled()) log.debug("RootVC: " + edition.getViewDocumentId() + " not found - first deploy?");

				}

			}

			if (edition.getDeployType().compareTo(Constants.DEPLOY_TYPE_PAGE) == 0) {
				ViewComponentHbm vc = getViewComponentHbmDao().load(edition.getViewComponentId());
				Realm2viewComponentHbm r2vc = getRealm2viewComponentHbmDao().findByViewComponent(vc.getViewComponentId());
				if (r2vc != null) {
					if (r2vc.getJaasRealm() != null) getRealmJaasHbmDao().remove(r2vc.getJaasRealm());
					if (r2vc.getJdbcRealm() != null) getRealmJdbcHbmDao().remove(r2vc.getJdbcRealm());
					if (r2vc.getLdapRealm() != null) getRealmLdapHbmDao().remove(r2vc.getLdapRealm());
					if (r2vc.getSimplePwRealm() != null) getRealmSimplePwHbmDao().remove(r2vc.getSimplePwRealm());
				}

				if (log.isDebugEnabled()) log.debug("Removing vC: " + vc.getViewComponentId());
				getViewComponentHbmDao().remove(vc);
				if (log.isDebugEnabled()) log.debug("Removing SUCC!");
			}

			/*
			 * #################################################################################### 
			 * S T E P 2 Import Units WITHOUT IMAGEID, ViewDocuments WITHOUT rootVC, Hosts WITHOUT vcId
			 * ####################################################################################
			 */
			this.editionCronService.logEditionStatusInfo(LiveserverDeployStatus.ImportUnits, editionId);
			if (log.isDebugEnabled()) log.debug("Starting with STEP 2/6");
			if (edition.getDeployType().compareTo(Constants.DEPLOY_TYPE_FULL) == 0 || edition.getDeployType().compareTo(Constants.DEPLOY_TYPE_ROOT) == 0) {
				importUnitsAndViewDocumentsAndRealms(doc, siteId, useNewIds);
			} else {
				// perhaps protected pages should stay protected after an import?
				importRealms(doc, siteId, useNewIds);
			}
			/*
			 * #################################################################################### 
			 * S T E P 3 Import Pic / Docs, UPDATE Units
			 * ####################################################################################
			 */
			this.editionCronService.logEditionStatusInfo(LiveserverDeployStatus.ImportResources, editionId);
			if (log.isDebugEnabled()) log.debug("Starting with STEP 3/6");
			importDocumentsAndPictures(doc, rootUnit, useNewIds, preparsedXMLfile);
			// pics and docs are in full, unit and root
			if (edition.getDeployType().compareTo(Constants.DEPLOY_TYPE_PAGE) != 0) {
				Collection units = getUnitHbmDao().findAll(siteId);
				Iterator unitsIt = units.iterator();
				while (unitsIt.hasNext()) {
					UnitHbm unit = (UnitHbm) unitsIt.next();
					Integer newId = unit.getUnitId();
					if (edition.getDeployType().compareTo(Constants.DEPLOY_TYPE_UNIT) == 0 && rootUnit != null && newId.compareTo(rootUnit.getUnitId()) != 0) continue;
					Element unitNode = null;
					if (useNewIds) {
						unitNode = (Element) XercesHelper.findNode(doc, "/edition/units/unit[@id='" + mappingUnitsReverse.get(newId) + "']");
						if (log.isDebugEnabled()) log.debug("newId " + newId + " mappingUnitsReverse.get(newId)" + mappingUnitsReverse.get(newId) + " unitNode " + unitNode);
					} else {
						unitNode = (Element) XercesHelper.findNode(doc, "/edition/units/unit[@id='" + newId + "']");
						if (log.isDebugEnabled()) log.debug("newId " + newId + " mappingUnitsReverse.get(newId)" + newId + " unitNode " + unitNode);

					}
					String imageIdstr = unitNode.getAttribute("imageId");
					if (imageIdstr != null && !imageIdstr.equalsIgnoreCase("") && !imageIdstr.equalsIgnoreCase("null")) {
						Integer newImageId = new Integer(imageIdstr);
						if (useNewIds) {
							newImageId = mappingPics.get(newImageId);
						}
						unit.setImageId(newImageId);
					}
					String logoIdstr = unitNode.getAttribute("logoId");
					if (logoIdstr != null && !logoIdstr.equalsIgnoreCase("") && !logoIdstr.equalsIgnoreCase("null")) {
						Integer newLogoId = new Integer(logoIdstr);
						if (useNewIds) {
							newLogoId = mappingPics.get(newLogoId);
						}
						unit.setLogoId(newLogoId);
					}
				}
			}
			/*
			 * #################################################################################### 
			 * S T E P 3 U Import Database Components
			 * ####################################################################################
			 */
			this.editionCronService.logEditionStatusInfo(LiveserverDeployStatus.ImportDatabaseComponents, editionId);
			Iterator it = XercesHelper.findNodes(doc, "/edition/units/unit");
			while (it.hasNext()) {
				Element unitNode = (Element) it.next();
				Integer newUnitId = new Integer(unitNode.getAttribute("id"));
				if (useNewIds) {
					newUnitId = mappingUnits.get(newUnitId);
				}
				if (log.isDebugEnabled()) log.debug("Import Database Components for UnitId: " + newUnitId + " (old was:" + unitNode.getAttribute("id") + ")");
				UnitHbm unit = getUnitHbmDao().load(newUnitId);
				importDatabaseComponents(unitNode, unit, useNewIds);
			}
			/*
			 * #################################################################################### 
			 * S T E P 4+5 Import ViewComponents. DONT UPDATE InternalLink IDs Update ViewDocument rootVCIDs
			 * ####################################################################################
			 */
			this.editionCronService.logEditionStatusInfo(LiveserverDeployStatus.ImportViewComponents, editionId);
			if (log.isDebugEnabled()) log.debug("Starting with STEP 4+5(6)/6");
			if (XercesHelper.findNode(doc, "/edition/viewcomponent") != null) {
				//if (edition.getDeployType().compareTo(Constants.DEPLOY_TYPE_FULL) == 0) {
				if (viewComponent == null) {
					Iterator vcNodeIt = XercesHelper.findNodes(doc, "/edition/viewcomponent");
					while (vcNodeIt.hasNext()) {
						Element vcNode = (Element) vcNodeIt.next();
						String oldVcId = vcNode.getAttribute("id");
						String oldVdId = null;
						try {
							oldVdId = ((Element) XercesHelper.findNode(doc, "/edition/viewDocuments/viewDocument[@rootViewComponentId='" + oldVcId + "']")).getAttribute("id");
						} catch (Exception e) {
							oldVdId = XercesHelper.getNodeValue(vcNode, "./viewDocumentId");
							new Integer(oldVdId);
							//							Integer tempParent = Integer.decode(vcNode.getAttribute("parent"));
							//							oldVdId = getViewComponentHbmDao().load(tempParent).getViewDocument().getViewDocumentId().toString();
						}
						Integer newVdlId = new Integer(oldVdId);
						if (useNewIds) {
							newVdlId = mappingVDs.get(newVdlId);
						}
						if (log.isDebugEnabled()) log.debug("Importing one of the Root-ViewComponents with oldVcId:" + oldVcId + " oldVdId:" + oldVdId + " newVdlId:" + newVdlId);
						ViewDocumentHbm viewDocument = getViewDocumentHbmDao().load(newVdlId);
						ViewComponentHbm parentVc = null;
						ViewComponentHbm prevVc = null;
						Integer id = null;

						if ((id = getIntegerAttributeValue(vcNode, "parent")) != null) {
							parentVc = getViewComponentHbmDao().load(id);
						}
						if ((id = getIntegerAttributeValue(vcNode, "prev")) != null) {
							prevVc = getViewComponentHbmDao().load(id);
						}

						ViewComponentHbm rootViewComponent = createViewComponent(null, viewDocument, null, vcNode, parentVc, prevVc, false, 1, useNewIds);

						//getViewComponentHbmDao().remove(viewDocument.getViewComponent());
						//viewDocument.setViewComponent(rootViewComponent);
					}
				} else {
					// Unit-Import or Unit-Deploy!
					{
						// backing-up hosts with startpage
						Collection hosts = getHostHbmDao().findAllWithStartPage4Site(siteId);
						Iterator<HostHbm> hostIt = hosts.iterator();
						while (hostIt.hasNext()) {
							HostHbm currHost = hostIt.next();
							this.startPageBackup.put(currHost.getHostName(), currHost.getStartPage().getViewComponentId());
						}
					}
					Element vcNode = (Element) XercesHelper.findNode(doc, "/edition/viewcomponent");

					ViewComponentHbm prev = null;
					ViewComponentHbm next = null;
					ViewComponentHbm parent = null;

					ViewDocumentHbm viewDocument = null;

					boolean wasFirstChild = false;
					// After removing we need this information to localize the ViewComponent to reimport
					prev = viewComponent.getPrevNode();
					next = viewComponent.getNextNode();
					parent = viewComponent.getParent();
					viewDocument = viewComponent.getViewDocument();

					if (parent != null && parent.getFirstChild().getViewComponentId().equals(viewComponent.getViewComponentId())) wasFirstChild = true;
					// REMOVE
					if (log.isDebugEnabled()) {
						log.debug("Removing ViewComponent for clean Import: " + viewComponent.getLinkDescription() + " PREV" + viewComponent.getPrevNode() + " NEXT" + viewComponent.getNextNode() + " PARENT" + viewComponent.getParent());
					}
					ViewComponentHbm temp, temp2;
					if (viewComponent.getPrevNode() == null && viewComponent.getNextNode() == null) {
						viewComponent.getParent().setFirstChild(null);
					} else if (viewComponent.getPrevNode() == null) {
						viewComponent.getParent().setFirstChild(viewComponent.getNextNode());
						viewComponent.getNextNode().setPrevNode(null);
					} else if (viewComponent.getNextNode() != null) {
						temp2 = viewComponent.getNextNode();
						temp = viewComponent.getPrevNode();
						temp2.setPrevNode(temp);
						temp.setNextNode(viewComponent.getNextNode());
					} else {
						viewComponent.getPrevNode().setNextNode(null);
					}
					if (log.isDebugEnabled()) log.debug("Trying to remove Unit-ViewComponent " + viewComponent.getViewComponentId());
					getViewComponentHbmDao().remove(viewComponent);
					if (log.isDebugEnabled()) log.debug("SUCC remove!");
					Integer myOldUnitId = new Integer(vcNode.getAttribute("unitId"));
					//FIXME useNewIds or not
					ViewComponentHbm rootview = createViewComponent(myOldUnitId, viewDocument, null, vcNode, parent, prev, false, 2, useNewIds);
					if (log.isDebugEnabled()) log.debug("rootview " + rootview + " next " + next);
					// Linkname check begin
					try {
						String linkname = rootview.getUrlLinkName();
						rootview.setUrlLinkName(UUID.randomUUID().toString());
						if (rootview.hasSiblingsWithLinkName(linkname)) {
							int id = 0;
							String tempText = "";
							while (true) {
								id++;
								tempText = linkname + id;
								if (!rootview.hasSiblingsWithLinkName(tempText)) {
									rootview.setUrlLinkName(tempText);
									break;
								}
							}
						} else {
							rootview.setUrlLinkName(linkname);
						}
					} catch (Exception exe) {
						log.error("Error occured", exe);
					}
					// Linkname check end
					rootview.setNextNode(next);
					if (next != null) {
						next.setPrevNode(rootview);
					}
					rootview.setAssignedUnit(rootUnit);
					if (wasFirstChild) {
						parent.setFirstChild(rootview);
					}
					if (log.isDebugEnabled()) log.debug("Starting with STEP 6/6 for Unit-Import");
					reparseViewComponent(rootview);
				}
			}
			/*
			 * #################################################################################### 
			 * S T E P 6 Reparse ViewComponents / Content for Internal Links, Pics, Docs Must be done in a second
			 * 'while', because there could be Internal Links from other languages/viewtypes as well
			 * 
			 * Importing Hosts, there is no other dependency on them 
			 * ####################################################################################
			 */
			this.editionCronService.logEditionStatusInfo(LiveserverDeployStatus.ImportHosts, editionId);
			if (log.isDebugEnabled()) log.debug("Starting with STEP 6/6");
			// hosts are only depoyed with full and root
			if (edition.getDeployType().compareTo(Constants.DEPLOY_TYPE_FULL) == 0 || edition.getDeployType().compareTo(Constants.DEPLOY_TYPE_ROOT) == 0) {
				Collection vdocs = getViewDocumentHbmDao().findAll(siteId);
				Iterator vdocsIt = vdocs.iterator();
				while (vdocsIt.hasNext()) {
					ViewDocumentHbm vdl = (ViewDocumentHbm) vdocsIt.next();
					reparseViewComponent(vdl.getViewComponent());
				}
				if (log.isDebugEnabled()) log.debug("Root-Deploy, importing Hosts");
				importHosts(doc, siteId);
			} else {
				if (log.isDebugEnabled()) log.debug("Unit-Import, restoring Hosts");
				{
					Enumeration<String> hosts = this.startPageBackup.keys();
					while (hosts.hasMoreElements()) {
						try {
							String hostName = hosts.nextElement();
							Integer startPageId = this.startPageBackup.get(hostName);
							HostHbm currHost = getHostHbmDao().load(hostName);
							ViewComponentHbm currStartPage = getViewComponentHbmDao().load(startPageId);
							currHost.setStartPage(currStartPage);
						} catch (Exception e) {
							log.warn("Error restoring startpage ", e);
						}
					}
				}
			}
			this.restoreSafeguardLoginPages(true);
		} catch (Exception exe) {
			// context.setRollbackOnly();
			if (exe.getMessage() != null) {
				editionCronService.logEditionStatusException(editionId, exe.getMessage());
			} else {
				editionCronService.logEditionStatusException(editionId, exe.getClass().toString());
			}
			log.error("Error occured processFileImport", exe);
			throw exe;
		} finally {
			//			try {
			//				new File(editionFileName).delete();
			//			} catch (Exception exe) {
			//			}
			//			try {
			//				preparsedXMLfile.delete();
			//			} catch (Exception exe) {
			//			}
			System.gc();
		}
		this.editionCronService.logEditionStatusInfo(LiveserverDeployStatus.ImportSuccessful, editionId);
		if (log.isInfoEnabled()) log.info("End processFileImport");
	}

	/**
	 * @see de.juwimm.cms.remote.EditionServiceSpring#importUnitsAndViewDocumentsAndRealms(org.w3c.dom.Document, Integer, boolean)
	 */
	@Override
	protected void handleImportUnitsAndViewDocumentsAndRealms(org.w3c.dom.Document doc, Integer siteId, boolean useNewIds) throws Exception {
		if (log.isDebugEnabled()) log.debug("begin importUnitsAndViewDocumentsAndRealms with useNewIDs=" + useNewIds);
		// if (!context.getRollbackOnly()) {
		this.importUnits(doc, useNewIds);
		// }
		// if (!context.getRollbackOnly()) {
		this.importViewDocuments(doc, useNewIds);
		// }
		// if (!context.getRollbackOnly()) {
		this.importRealms(doc, siteId, useNewIds);
		// }
		if (log.isDebugEnabled()) log.debug("end importUnitsAndViewDocumentsAndRealms");
	}

	private void importUnits(org.w3c.dom.Document doc, boolean useNewIDs) {
		if (log.isDebugEnabled()) log.debug("start importUnits with useNewIDs=" + useNewIDs);
		try {
			if (XercesHelper.findNode(doc, "/edition/units") != null) {
				Iterator itUnits = XercesHelper.findNodes(doc, "/edition/units/unit");
				while (itUnits.hasNext()) {
					Element elmUnit = (Element) itUnits.next();
					String unitName = "";
					try {
						unitName = XercesHelper.getNodeValue(elmUnit).trim();
					} catch (Exception exe) {
					}
					Integer unitId = new Integer(elmUnit.getAttribute("id"));
					UnitHbm unit = null;
					if (useNewIDs) {
						unit = getUnitHbmDao().create(createUnitHbm(unitName, null));
						mappingUnits.put(unitId, unit.getUnitId()); // mapping OLD-ID to NEW-ID
						mappingUnitsReverse.put(unit.getUnitId(), unitId); // mapping NEW-ID to OLD-ID
					} else {
						try {
							if (log.isDebugEnabled()) log.debug("Searching for unit " + unitId);
							unit = getUnitHbmDao().load(unitId);
							unit.setName(unitName);
						} catch (Exception exe) {
							if (log.isDebugEnabled()) log.debug("Creating unit " + unitId + " (" + unitName + ")");
							unit = createUnitHbm(unitName, null);
							unit.setUnitId(unitId);
							unit = getUnitHbmDao().create(unit);
						}
						Integer imageId = null;
						try {
							imageId = new Integer(elmUnit.getAttribute("imageId"));
						} catch (Exception exe) {
						}
						unit.setImageId(imageId);
						Integer logoId = null;
						try {
							logoId = new Integer(elmUnit.getAttribute("logoId"));
						} catch (Exception exe) {
						}
						unit.setLogoId(logoId);
					}
					boolean isRootUnitForThisSite = false;
					try {
						isRootUnitForThisSite = Boolean.valueOf(elmUnit.getAttribute("isRootUnit")).booleanValue();
					} catch (Exception exe) {
					}
					if (log.isDebugEnabled()) log.debug("The Unit " + unit.getUnitId() + " (" + unit.getName().trim() + ")" + " isRootUnitForThisSite " + isRootUnitForThisSite);
					if (isRootUnitForThisSite) {
						unit.getSite().setRootUnit(unit);
					}
				}
			} else {
				if (log.isDebugEnabled()) log.debug("importUnits reports: There are no /edition/units nodes, so nothing to do");
			}
		} catch (Exception exe) { // here are also only finder exceptions thrown
			// context.setRollbackOnly();
			log.error("Error occured importUnits: " + exe.getMessage(), exe);
		}
	}

	private UnitHbm createUnitHbm(String unitName, Integer unitId) {
		UnitHbm unit;
		if (unitId == null) {
			unit = UnitHbm.Factory.newInstance();
		} else {
			unit = getUnitHbmDao().load(unitId);
		}
		unit.setName(unitName);
		return unit;
	}

	private void importViewDocuments(org.w3c.dom.Document doc, boolean useNewIDs) {
		if (log.isDebugEnabled()) log.debug("start importViewDocuments with useNewIDs=" + useNewIDs);
		try {
			if (XercesHelper.findNode(doc, "/edition/viewDocuments") != null) {
				Iterator itViewDocuments = XercesHelper.findNodes(doc, "/edition/viewDocuments/viewDocument");
				while (itViewDocuments.hasNext()) {
					if (log.isDebugEnabled()) log.debug("Found ViewDocument to import...");
					Element emlViewDocument = (Element) itViewDocuments.next();
					Integer vdId = new Integer(emlViewDocument.getAttribute("id"));
					Integer rootViewComponentId = new Integer(emlViewDocument.getAttribute("rootViewComponentId"));
					String lang = emlViewDocument.getAttribute("language");
					String viewType = emlViewDocument.getAttribute("viewType");
					if (log.isDebugEnabled()) log.debug("VD " + vdId + " rootVcId " + rootViewComponentId + " language " + lang + " viewType " + viewType);

					ViewDocumentHbm vdoc = null;
					if (useNewIDs) {
						vdoc = getViewDocumentHbmDao().create(createViewDocumentHbm(lang, viewType, null, null));
						mappingVDs.put(vdId, vdoc.getViewDocumentId()); // mapping OLD-ID to NEW-ID
					} else {
						// if (log.isDebugEnabled()) log.debug("...creating...");
						try {
							if (log.isDebugEnabled()) log.debug("searching ViewDocument: " + vdId);
							vdoc = getViewDocumentHbmDao().load(vdId);
							//nur um null abzufangen...
							vdoc.getViewDocumentId();
						} catch (Exception exe) {
							if (log.isDebugEnabled()) log.debug("creating ViewDocument: " + vdId);
							//							vdoc = createViewDocumentHbm(lang, viewType, null, rootViewComponentId);
							//							vdoc.setViewDocumentId(vdId);
							vdoc = getViewDocumentHbmDao().load(getViewDocumentHbmDao().create(lang, viewType, vdId, rootViewComponentId));
						}
						// only if we are doing a live-deploy, we will create the rootViewComponent as well,
						// if it is not existing
						Element rootVE = (Element) XercesHelper.findNode(doc, "/edition/viewcomponent");
						UnitHbm unit = getUnitHbmDao().load(new Integer(rootVE.getAttribute("unitId")));
						vdoc.getViewComponent().setAssignedUnit(unit);
					}
					boolean isDefaultViewDocumentForThisSite = false;
					try {
						isDefaultViewDocumentForThisSite = Boolean.valueOf(emlViewDocument.getAttribute("default")).booleanValue();
					} catch (Exception exe) {
					}
					if (log.isDebugEnabled()) log.debug("The ViewDocument " + vdoc.getViewDocumentId() + " (" + vdoc.getViewType() + ", " + vdoc.getLanguage() + ")" + " isDefaultViewDocumentForThisSite " + isDefaultViewDocumentForThisSite);
					if (isDefaultViewDocumentForThisSite) {
						vdoc.getSite().setDefaultViewDocument(vdoc);
						vdoc.getSite().setLastModifiedDate(new Date().getTime());
					}
				}
			} else {
				if (log.isDebugEnabled()) log.debug("importViewDocuments reports: There are no /edition/viewDocuments nodes, so nothing to do");
			}
		} catch (Exception exe) { // here are also only finder exceptions thrown
			// context.setRollbackOnly();
			log.error("Error occured importViewDocuments: " + exe.getMessage(), exe);
		}
	}

	private ViewDocumentHbm createViewDocumentHbm(String lang, String viewType, Integer vdId, Integer rootViewComponentId) {
		ViewDocumentHbm viewDocument = ViewDocumentHbm.Factory.newInstance();
		if (lang != null) {
			viewDocument.setLanguage(lang);
		}
		if (viewType != null) {
			viewDocument.setViewType(viewType);
		}
		if (vdId != null) {
			viewDocument.setViewDocumentId(vdId);
		}
		if (rootViewComponentId != null) {
			viewDocument.setViewComponent(getViewComponentHbmDao().load(rootViewComponentId));
		}
		return viewDocument;
	}

	/**
	 * This Method imports the database components for the actual unitid given in the edition. <br>
	 * Will be called from liveserver ONLY. (currently)
	 * 
	 * @throws UserException
	 * 
	 * @see de.juwimm.cms.remote.EditionServiceSpring#importDatabaseComponents(org.w3c.dom.Element, de.juwimm.cms.model.UnitHbm, boolean)
	 */
	protected void importDatabaseComponents(org.w3c.dom.Element unitElm, de.juwimm.cms.model.UnitHbm unit, boolean useNewIds) throws Exception {
		if (log.isInfoEnabled()) log.info("begin importDatabaseComponents for unit: " + unit.getName());
		// if (!context.getRollbackOnly()) {
		try {
			if (unitElm != null) {
				Integer unitIdFromEdition = new Integer(unitElm.getAttribute("id"));
				// old IDs and same unitId
				if (!useNewIds && unitIdFromEdition.equals(unit.getUnitId())) {
					/*
					 * If no new IDs, this will be a deploy. In other cases we already have deleted the complete site and setted the unitLogo
					 */
					try {
						unit.setImageId(new Integer(unitElm.getAttribute("imageId")));
					} catch (Exception exe) {
						if (log.isDebugEnabled()) log.debug("Unit " + unit.getName() + " has no Image: " + exe.getMessage());
					}
					try {
						unit.setLogoId(new Integer(unitElm.getAttribute("logoId")));
					} catch (Exception exe) {
						if (log.isDebugEnabled()) log.debug("Unit " + unit.getName() + " has no Logo: " + exe.getMessage());
					}

					if (log.isDebugEnabled()) log.debug("DELETING CURRENT ADDRESSES");
					Collection cAddresses = unit.getAddresses();
					getAddressHbmDao().remove(cAddresses);

					if (log.isDebugEnabled()) log.debug("DELETING CURRENT PERSONS");
					Collection<PersonHbm> persons = getPersonHbmDao().findByUnit(unit.getUnitId());
					getPersonHbmDao().remove(persons);

					if (log.isDebugEnabled()) log.debug("DELETING CURRENT DEPTS");
					Collection cDepartments = unit.getDepartments();
					getDepartmentHbmDao().remove(cDepartments);

					if (log.isDebugEnabled()) log.debug("DELETING CURRENT TTIMES");
					Collection<TalktimeHbm> talktimes = getTalktimeHbmDao().findByUnit(unit.getUnitId());
					getTalktimeHbmDao().remove(talktimes);
				}
				// old IDs and same unitID or new IDs and id == oldId from mapping
				if ((!useNewIds && unitIdFromEdition.equals(unit.getUnitId())) || (useNewIds && unitIdFromEdition.equals(mappingUnitsReverse.get(unit.getUnitId())))) {
					if (log.isDebugEnabled()) log.debug("adding current ADDRESSES");
					Iterator itAdr = XercesHelper.findNodes(unitElm, "./address");
					while (itAdr.hasNext()) {
						Element adrEl = (Element) itAdr.next();
						AddressHbm address = createAddressHbm(adrEl, useNewIds, mappingAddresses);
						unit.getAddresses().add(address);
					}
					if (log.isDebugEnabled()) log.debug("adding current PERSONS");
					Iterator<Element> itPers = XercesHelper.findNodes(unitElm, "./person");
					while (itPers.hasNext()) {
						if (log.isDebugEnabled()) log.debug("found person to import");
						Element elm = itPers.next();
						createPersonHbm(unit, elm, useNewIds, mappingPersons, mappingAddresses, mappingTalktime);
					}
					if (log.isDebugEnabled()) log.debug("adding current DEPTS");
					Iterator itDepts = XercesHelper.findNodes(unitElm, "./department");
					while (itDepts.hasNext()) {
						Element elm = (Element) itDepts.next();
						DepartmentHbm department = createDepartmentHbm(elm, useNewIds, mappingDepartments, mappingPersons, mappingAddresses, mappingTalktime);
						department.setUnit(unit);
						unit.getDepartments().add(department);
					}
					if (log.isDebugEnabled()) log.debug("adding current TTIMES");
					Iterator<Element> itTTimes = XercesHelper.findNodes(unitElm, "./talktime");
					while (itTTimes.hasNext()) {
						Element elm = itTTimes.next();
						TalktimeHbm talktime = createTalktimeHbm(elm, useNewIds, mappingTalktime);
						talktime.setUnit(unit);
					}
				}
			} else {
				if (log.isDebugEnabled()) log.debug("importDatabaseComponents reports: There are no /edition/units/unit nodes, so nothing to do");
			}
		} catch (Exception exe) {
			// context.setRollbackOnly();
			log.error("Error occured importDatabaseComponents", exe);
		}
		// }
		if (log.isInfoEnabled()) log.info("end importDatabaseComponents for unit: " + unit.getName());

	}

	private TalktimeHbm createTalktimeHbm(Element ael, boolean useNewIds, Hashtable<Integer, Long> mappingTalktime2) {
		TalktimeHbm talktime = TalktimeHbm.Factory.newInstance();
		try {
			talktime.setTalkTimes(XercesHelper.nodeList2string(XercesHelper.findNode(ael, "./talkTimes").getChildNodes()));
		} catch (Exception exe) {
		}
		talktime.setTalkTimeType(getNVal(ael, "talkTimeType"));
		if (!useNewIds) {
			talktime.setTalkTimeId(Integer.getInteger(getNVal(ael, "id")).longValue());
		}
		talktime = getTalktimeHbmDao().create(talktime);
		return talktime;
	}

	private DepartmentHbm createDepartmentHbm(Element ael, boolean useNewIds, Hashtable<Integer, Long> mappingDepartments2, Hashtable<Integer, Long> mappingPersons2, Hashtable<Integer, Long> mappingAddresses2, Hashtable<Integer, Long> mappingTalktime2) {
		DepartmentHbm department = DepartmentHbm.Factory.newInstance();
		if (!useNewIds) {
			department.setDepartmentId(Integer.getInteger(getNVal(ael, "id")).longValue());
		}
		department.setName(ael.getAttribute("name"));
		department = getDepartmentHbmDao().create(department);
		return department;
	}

	private PersonHbm createPersonHbm(UnitHbm unit, Element ael, boolean useNewIds, Hashtable<Integer, Long> mappingPersons2, Hashtable<Integer, Long> mappingAddresses2, Hashtable<Integer, Long> mappingTalktime2) throws Exception {
		PersonHbm person = PersonHbm.Factory.newInstance();
		try {
			person.setImageId(Integer.getInteger(ael.getAttribute("imageid")));
		} catch (Exception exe) {
		}
		try {
			person.setBirthDay(getNVal(ael, "birthDay"));
		} catch (Exception exe) {
		}
		try {
			person.setCountryJob(getNVal(ael, "countryJob"));
			person.setFirstname(getNVal(ael, "firstname"));
			person.setJob(getNVal(ael, "job"));
			person.setJobTitle(getNVal(ael, "jobTitle"));
			person.setLastname(getNVal(ael, "lastname"));
			person.setLinkMedicalAssociation(getNVal(ael, "linkMedicalAssociation"));
			person.setMedicalAssociation(getNVal(ael, "medicalAssociation"));
			person.setPosition(new Byte(getNVal(ael, "position")).byteValue());
			person.setSalutation(getNVal(ael, "salutation"));
			person.setSex(new Byte(getNVal(ael, "sex")).byteValue());
			person.setTitle(getNVal(ael, "title"));
			if (!useNewIds) {
				person.setPersonId(new Integer(ael.getAttribute("id")).longValue());
			}
			person.getUnits().add(unit);
			person = getPersonHbmDao().create(person);
		} catch (Exception exe) {
			log.warn("Error setting values: ", exe);
		}
		try {
			if (log.isDebugEnabled()) log.debug("looking for addresses to import for person " + person.getPersonId()); //$NON-NLS-1$
			Iterator itAdr = XercesHelper.findNodes(ael, "./address"); //$NON-NLS-1$
			while (itAdr.hasNext()) {
				Element adrEl = (Element) itAdr.next();
				if (log.isDebugEnabled()) log.debug("found address to import"); //$NON-NLS-1$
				AddressHbm local = createAddressHbm(adrEl, useNewIds, mappingAddresses);
				person.addAddress(local);
			}
		} catch (Exception exe) {
			log.warn("Error importing addresses: ", exe); //$NON-NLS-1$
			throw new CreateException(exe.getMessage());
		}
		try {
			if (log.isDebugEnabled()) log.debug("looking for talktimes to import for person " + person.getPersonId()); //$NON-NLS-1$
			Iterator itTTimes = XercesHelper.findNodes(ael, "./talktime"); //$NON-NLS-1$
			while (itTTimes.hasNext()) {
				Element elm = (Element) itTTimes.next();
				if (log.isDebugEnabled()) log.debug("found talktime to import"); //$NON-NLS-1$
				TalktimeHbm local = createTalktimeHbm(elm, useNewIds, mappingTalktime);
				person.addTalktime(local);
			}
		} catch (Exception exe) {
			log.warn("Error importing talktimes: ", exe); //$NON-NLS-1$
			throw new Exception(exe.getMessage());
		}
		return person;
	}

	private AddressHbm createAddressHbm(Element ael, boolean useNewIds, Hashtable<Integer, Long> mappingAddresses2) {
		AddressHbm address = AddressHbm.Factory.newInstance();
		address.setAddressType(getNVal(ael, "addressType"));
		address.setBuildingLevel(getNVal(ael, "buildingLevel"));
		address.setBuildingNr(getNVal(ael, "buildingNr"));
		address.setCity(getNVal(ael, "city"));
		address.setCountry(getNVal(ael, "country"));
		address.setCountryCode(getNVal(ael, "countryCode"));
		address.setEmail(getNVal(ael, "email"));
		address.setFax(getNVal(ael, "fax"));
		address.setHomepage(getNVal(ael, "homepage"));
		address.setMisc(getNVal(ael, "misc"));
		address.setMobilePhone(getNVal(ael, "mobilePhone"));
		address.setPhone1(getNVal(ael, "phone1"));
		address.setPhone2(getNVal(ael, "phone2"));
		address.setPostOfficeBox(getNVal(ael, "postOfficeBox"));
		address.setRoomNr(getNVal(ael, "roomNr"));
		address.setStreet(getNVal(ael, "street"));
		address.setStreetNr(getNVal(ael, "streetNr"));
		address.setZipCode(getNVal(ael, "zipCode"));
		if (!useNewIds) {
			address.setAddressId(Integer.getInteger(ael.getAttribute("id")).longValue());
			log.info("AddressID: " + address.getAddressId());
		}
		address = getAddressHbmDao().create(address);
		return address;
	}

	private String getNVal(Element ael, String nodeName) {
		String tmp = XercesHelper.getNodeValue(ael, "./" + nodeName);
		if (tmp.equals("null") || tmp.equals("")) {
			return null;
		}
		return tmp;
	}

	private String getNValNotNull(Element ael, String nodeName) {
		String tmp = XercesHelper.getNodeValue(ael, "./" + nodeName);
		if (tmp.equals("null")) {
			return null;
		}
		return tmp;
	}

	private Integer getIntegerAttributeValue(Element ael, String attributeName) {
		String attribute = ael.getAttribute(attributeName);
		Integer retValue = null;
		if (attribute != null && !attribute.isEmpty()) {
			try {
				retValue = Integer.decode(attribute);
			} catch (Exception e) {
				if (log.isInfoEnabled()) log.info("getIntegerAttributeValue could not find value for key: " + attributeName);
			}
		}
		return retValue;
	}

	/**
	 * @see de.juwimm.cms.remote.EditionServiceSpring#importDocumentsAndPictures(org.w3c.dom.Document, de.juwimm.cms.model.UnitHbm, boolean, java.io.File)
	 */
	protected void importDocumentsAndPictures(org.w3c.dom.Document doc, UnitHbm ul, boolean useNewIds, File directory) throws Exception {
		if (log.isInfoEnabled()) log.info("begin importDocumentsAndPictures");
		// if (!context.getRollbackOnly()) {
		try {
			SiteHbm site = getUserHbmDao().load(AuthenticationHelper.getUserName()).getActiveSite();
			Iterator itdocs = XercesHelper.findNodes(doc, "/edition/documents/document");
			Iterator itpics = XercesHelper.findNodes(doc, "/edition/pictures/picture");
			// end of parsing the document
			if ((itdocs != null && itdocs.hasNext()) || useNewIds) {
				if (log.isDebugEnabled()) log.debug("DELETING DOCS");
				Collection docs = null;
				if (ul == null) {
					docs = getDocumentHbmDao().findAllPerSite(site.getSiteId());
				} else {
					docs = getDocumentHbmDao().findAll(ul.getUnitId());
				}
				Iterator it = docs.iterator();
				while (it.hasNext()) {
					de.juwimm.cms.model.DocumentHbm docl = (de.juwimm.cms.model.DocumentHbm) it.next();
					getDocumentHbmDao().remove(docl);
				}
				if (log.isDebugEnabled()) log.debug("IMPORTING DOCS");
				while (itdocs.hasNext()) {
					Element el = (Element) itdocs.next();
					Integer id = new Integer(el.getAttribute("id"));
					String strDocName = XercesHelper.getNodeValue(el, "./name");
					String strMimeType = el.getAttribute("mimeType");
					int unitId = Integer.decode(el.getAttribute("unitId"));
					// byte[] file = Base64.decode(XercesHelper.getNodeValue(el, "./file"));
					File fle = new File(directory.getParent() + File.separator + "d" + id);
					byte[] file = new byte[(int) fle.length()];
					new FileInputStream(fle).read(file);
					fle.delete();

					if (useNewIds) {
						de.juwimm.cms.model.DocumentHbm document = null;
						if (ul == null) {
							Integer oldUnitId = new Integer(el.getAttribute("unitId"));
							Integer newUnitId = mappingUnits.get(oldUnitId);
							UnitHbm unitForPic = getUnitHbmDao().load(newUnitId);

							document = getDocumentHbmDao().create(createDocumentHbm(file, strDocName, strMimeType, null, unitForPic));
						} else {
							document = getDocumentHbmDao().create(createDocumentHbm(file, strDocName, strMimeType, null, ul));
						}
						mappingDocs.put(id, document.getDocumentId());
					} else {
						if (ul == null) {
							ul = getUnitHbmDao().load(unitId);
						}
						getDocumentHbmDao().create(createDocumentHbm(file, strDocName, strMimeType, id, ul));
					}
				}
			}
			System.gc();
			if ((itpics != null && itpics.hasNext()) || useNewIds) {
				if (log.isDebugEnabled()) log.debug("DELETING PICS");
				Collection<PictureHbm> pics = null;
				if (ul == null) {
					pics = getPictureHbmDao().findAllPerSite(site.getSiteId());
				} else {
					pics = getPictureHbmDao().findAllPerUnit(ul.getUnitId());
				}
				getPictureHbmDao().remove(pics);

				if (log.isDebugEnabled()) log.debug("IMPORTING PICS");
				while (itpics.hasNext()) {
					Element el = (Element) itpics.next();
					Integer id = new Integer(el.getAttribute("id"));
					String strMimeType = el.getAttribute("mimeType");
					String strPictureName = XercesHelper.getNodeValue(el, "./pictureName");
					String strAltText = XercesHelper.getNodeValue(el, "./altText");
					String title = XercesHelper.getNodeValue(el, "./title");

					File fle = new File(directory.getParent() + File.separator + "f" + id);
					byte[] file = new byte[(int) fle.length()];
					new FileInputStream(fle).read(file);
					fle.delete();

					File fleThumb = new File(directory.getParent() + File.separator + "t" + id);
					byte[] thumbnail = new byte[(int) fleThumb.length()];
					new FileInputStream(fleThumb).read(thumbnail);
					fleThumb.delete();

					byte[] preview = null;
					File flePreview = new File(directory.getParent() + File.separator + "p" + id);
					// not every picture may have a custom preview-image
					if (flePreview.exists() && flePreview.canRead()) {
						preview = new byte[(int) flePreview.length()];
						new FileInputStream(flePreview).read(preview);
						flePreview.delete();
					}

					if (useNewIds) {
						PictureHbm pic = null;
						if (ul == null) {
							Integer oldUnitId = new Integer(el.getAttribute("unitId"));
							Integer newUnitId = mappingUnits.get(oldUnitId);
							UnitHbm unitForPic = getUnitHbmDao().load(newUnitId);
							pic = getPictureHbmDao().create(createPictureHbm(thumbnail, file, preview, strMimeType, strAltText, title, strPictureName, null, unitForPic));
						} else {
							pic = getPictureHbmDao().create(createPictureHbm(thumbnail, file, preview, strMimeType, strAltText, title, strPictureName, null, ul));
						}
						if (log.isDebugEnabled()) log.debug("mappingPics OLD " + id + " NEW " + pic.getPictureId());
						mappingPics.put(id, pic.getPictureId());
					} else {
						createPictureHbm(thumbnail, file, preview, strMimeType, strAltText, title, strPictureName, id, ul);
					}
				}
			}
		} catch (Exception exe) {
			// here are only finder-exceptions thrown. Should not occure, so what to do with it?
			// context.setRollbackOnly();
			log.error("Error occured importDocumentsAndPictures", exe);
		}
		// }
		if (log.isInfoEnabled()) log.info("end importDocumentsAndPictures");
	}

	private PictureHbm createPictureHbm(byte[] thumbnail, byte[] file, byte[] preview, String strMimeType, String strAltText, String title, String strPictureName, Integer id, UnitHbm unit) {
		PictureHbm picture;
		boolean newPicture = (id != null && id == 0) ? true : false;
		if (id == null) {
			picture = PictureHbm.Factory.newInstance();
		} else {
			picture = getPictureHbmDao().load(id);
			if (picture == null) {
				newPicture = true;
				picture = PictureHbm.Factory.newInstance();
				picture.setPictureId(id);
			}
		}
		picture.setThumbnail(thumbnail);
		picture.setPicture(file);
		picture.setPreview(preview);
		picture.setMimeType(strMimeType);
		picture.setAltText(strAltText);
		picture.setTitle(title);
		picture.setPictureName(strPictureName);
		picture.setUnit(unit);

		if (newPicture) {
			picture = getPictureHbmDao().create(picture);
		}
		return picture;
	}

	private DocumentHbm createDocumentHbm(byte[] file, String strDocName, String strMimeType, Integer id, UnitHbm unit) {
		DocumentHbm document;
		if (id == null) {
			document = DocumentHbm.Factory.newInstance();
		} else {
			document = getDocumentHbmDao().load(id);
			if (document == null) {
				document = getDocumentHbmDao().load(getDocumentHbmDao().create(file, strDocName, strMimeType, unit, id));
			}
		}

		document.setDocumentName(strDocName);
		document.setMimeType(strMimeType);
		document.setUnit(unit);

		if (id == null) {
			document = getDocumentHbmDao().create(document);
		}

		try {
			Blob b = Hibernate.createBlob(file);
			document.setDocument(b);
		} catch (Exception e) {
			log.error("Exception copying document to database", e);
		}
		return document;
	}

	/**
	 * Imports an XML File as Edition for one Unit. <br>
	 * Important: THIS IS USED FOR IMPORTING EDITIONS AND DEPLOYMENTS AS WELL.
	 * 
	 * @param doc
	 * @param ul
	 * @param viewDocumentId
	 * @param liveDeploy
	 * @throws UserException
	 * @see de.juwimm.cms.remote.EditionServiceSpring#importViewComponentFile(org.w3c.dom.Document, de.juwimm.cms.model.UnitHbm, int, boolean)
	 */
	protected void importViewComponentFile(org.w3c.dom.Document doc, UnitHbm ul, int viewDocumentId, boolean liveDeploy) throws Exception {
		if (log.isDebugEnabled()) log.debug("begin importViewComponentFile");
		// if (!context.getRollbackOnly()) {
		try {
			Node vcompNode = XercesHelper.findNode(doc, "/edition/viewcomponent");
			if (vcompNode == null) return;
			Element nde = (Element) vcompNode;

			ViewDocumentHbm vdl = getViewDocumentHbmDao().load(new Integer(viewDocumentId));

			ViewComponentHbm prev = null;
			ViewComponentHbm next = null;
			ViewComponentHbm parent = null;
			ArrayList<ViewComponentHbm> savedUnits = new ArrayList<ViewComponentHbm>();
			boolean wasFirstChild = false;
			try {
				if (log.isInfoEnabled()) log.info("Starting import of ViewComponent File: VDid:" + viewDocumentId + " Unit:" + ul.getUnitId() + " (" + ul.getName().trim() + ")");
				ViewComponentHbm view = getViewComponentHbmDao().find4Unit(ul.getUnitId(), new Integer(viewDocumentId));
				if (log.isDebugEnabled()) log.debug("Found the VC by UnitId and ViewDocumentId");
				boolean isRoot = false;
				try {
					isRoot = (view.getViewComponentId().intValue() == vdl.getViewComponent().getViewComponentId().intValue());
				} catch (Exception e) {
				}
				if (isRoot) {
					try {
						if (log.isInfoEnabled()) log.info("Started ROOT-Deploy");
						savedUnits = moveContainedUnitsAway(view);
						getViewComponentHbmDao().remove(view);
						ViewComponentHbm rootview = createViewComponent(ul.getUnitId(), vdl, savedUnits, nde, null, null, liveDeploy, 0, false);
						rootview.setAssignedUnit(ul);
						vdl.setViewComponent(rootview);
					} catch (Exception exe) {
						// context.setRollbackOnly();
						log.error("Error occured importViewComponentFile ROOT", exe);
					}
					return;
				}
				// After removing we need this information to localize the ViewComponent to reimport
				prev = view.getPrevNode();
				next = view.getNextNode();
				parent = view.getParent();
				if (log.isDebugEnabled()) if (parent != null) log.debug("parent: " + parent.getViewComponentId());
				if (log.isDebugEnabled()) if (parent.getFirstChild() != null) log.debug("parent.getFirstChild(): " + parent.getFirstChild().getViewComponentId());
				if (parent != null && parent.getFirstChild().getViewComponentId().equals(view.getViewComponentId())) wasFirstChild = true;

				savedUnits = moveContainedUnitsAway(view);
				this.startPageBackup = new Hashtable<String, Integer>();
				try {
					if (log.isDebugEnabled()) log.debug("backing-up hosts with startpage");
					Collection hosts = getHostHbmDao().findAllWithStartPage4Site(view.getViewDocument().getSite().getSiteId());
					Iterator hostIt = hosts.iterator();
					while (hostIt.hasNext()) {
						HostHbm currHost = (HostHbm) hostIt.next();
						this.startPageBackup.put(currHost.getHostName(), currHost.getStartPage().getViewComponentId());
					}
				} catch (Exception exe) {
					log.error("Error occured backing up host entries!", exe);
				}
				{
					// backup of loginPageIds if pages from other units use a page from this unit for login
					this.loginPagesRealm2viewComponentBackup = new Hashtable<Integer, Integer>();
					this.loginPagesSimplePwRealmBackup = new Hashtable<Integer, Integer>();
					try {
						if (log.isDebugEnabled()) log.debug("backing-up loginPages for SimplePwRealms");
						Iterator it = getRealmSimplePwHbmDao().findBySiteId(ul.getSite().getSiteId()).iterator();
						while (it.hasNext()) {
							RealmSimplePwHbm realmSimplePW = (RealmSimplePwHbm) it.next();
							if (realmSimplePW.getLoginPageId() != null && realmSimplePW.getLoginPageId().length() > 0) {
								try {
									ViewComponentHbm loginPage = getViewComponentHbmDao().load(Integer.valueOf(realmSimplePW.getLoginPageId()));
									if (loginPage.getUnit4ViewComponent().intValue() == ul.getUnitId().intValue()) this.loginPagesSimplePwRealmBackup.put(realmSimplePW.getSimplePwRealmId(), loginPage.getViewComponentId());
								} catch (Exception e) {
									// skip faulty loginpage and continue with the rest
								}
							}
							this.backupLoginPages4realm2viewComponents(realmSimplePW.getRealm2viewComponent().iterator(), ul.getUnitId());
						}
					} catch (Exception e) {
						log.error("Error occured backing up loginPages for SimplePwRealms!", e);
					}
					this.loginPagesJdbcRealmBackup = new Hashtable<Integer, Integer>();
					try {
						if (log.isDebugEnabled()) log.debug("backing-up loginPages for JdbcRealms");
						Iterator it = getRealmJdbcHbmDao().findBySiteId(ul.getSite().getSiteId()).iterator();
						while (it.hasNext()) {
							RealmJdbcHbm realmJdbc = (RealmJdbcHbm) it.next();
							if (realmJdbc.getLoginPageId() != null && realmJdbc.getLoginPageId().length() > 0) {
								try {
									ViewComponentHbm loginPage = getViewComponentHbmDao().load(Integer.valueOf(realmJdbc.getLoginPageId()));
									if (loginPage.getUnit4ViewComponent().intValue() == ul.getUnitId().intValue()) this.loginPagesJdbcRealmBackup.put(realmJdbc.getJdbcRealmId(), view.getViewComponentId());
								} catch (Exception e) {
									// skip faulty loginpage and continue with the rest
								}
							}
							this.backupLoginPages4realm2viewComponents(realmJdbc.getRealm2viewComponent().iterator(), ul.getUnitId());
						}
					} catch (Exception e) {
						log.error("Error occured backing up loginPages for JdbcRealms!", e);
					}
					this.loginPagesJaasRealmBackup = new Hashtable<Integer, Integer>();
					try {
						if (log.isDebugEnabled()) log.debug("backing-up loginPages for JaasRealms");
						Iterator it = getRealmJaasHbmDao().findBySiteId(ul.getSite().getSiteId()).iterator();
						while (it.hasNext()) {
							RealmJaasHbm realmJaas = (RealmJaasHbm) it.next();
							if (realmJaas.getLoginPageId() != null && realmJaas.getLoginPageId().length() > 0) {
								try {
									ViewComponentHbm loginPage = getViewComponentHbmDao().load(Integer.valueOf(realmJaas.getLoginPageId()));
									if (loginPage.getUnit4ViewComponent().intValue() == ul.getUnitId().intValue()) this.loginPagesJaasRealmBackup.put(realmJaas.getJaasRealmId(), view.getViewComponentId());
								} catch (Exception e) {
									// skip faulty loginpage and continue with the rest
								}
							}
							this.backupLoginPages4realm2viewComponents(realmJaas.getRealm2viewComponent().iterator(), ul.getUnitId());
						}
					} catch (Exception e) {
						log.error("Error occured backing up loginPages for JaasRealms!", e);
					}
					this.loginPagesLdapRealmBackup = new Hashtable<Integer, Integer>();
					try {
						if (log.isDebugEnabled()) log.debug("backing-up loginPages for LdapRealms");
						Iterator it = getRealmLdapHbmDao().findBySiteId(ul.getSite().getSiteId()).iterator();
						while (it.hasNext()) {
							RealmLdapHbm realmLdap = (RealmLdapHbm) it.next();
							if (realmLdap.getLoginPageId() != null && realmLdap.getLoginPageId().length() > 0) {
								try {
									ViewComponentHbm loginPage = getViewComponentHbmDao().load(Integer.valueOf(realmLdap.getLoginPageId()));
									if (loginPage.getUnit4ViewComponent().intValue() == ul.getUnitId().intValue()) this.loginPagesLdapRealmBackup.put(realmLdap.getLdapRealmId(), view.getViewComponentId());
								} catch (Exception e) {
									log.error("Error occured backing up loginPages for JaasRealms!", e);
									// skip faulty loginpage and continue with the rest
								}
							}
							this.backupLoginPages4realm2viewComponents(realmLdap.getRealm2viewComponent().iterator(), ul.getUnitId());
						}
					} catch (Exception e) {
						log.error("Error occured backing up loginPages for LdapRealms!", e);
					}

					try {
						if (log.isDebugEnabled()) log.debug("backing-up loginPages for Realm2viewComponents");
						// Iterator it = view.getRealm4login().iterator();
						Iterator it = getRealm2viewComponentHbmDao().findByLoginPage(view.getViewComponentId()).iterator();
						while (it.hasNext()) {
							Realm2viewComponentHbm realm2viewComponent = (Realm2viewComponentHbm) it.next();
							this.loginPagesRealm2viewComponentBackup.put(realm2viewComponent.getRealm2viewComponentId(), view.getViewComponentId());
						}
					} catch (Exception e) {
						log.error("Error occured backing up loginPages for Realm2viewComponents!", e);
					}
				}

				// REMOVE
				if (log.isDebugEnabled()) log.debug("Removing ViewComponent for clean Import: " + view.getViewComponentId() + " (" + view.getLinkDescription() + ")");
				if (view.getPrevNode() == null && view.getNextNode() == null) {
					view.getParent().setFirstChild(null);
				} else if (view.getPrevNode() == null) {
					view.getParent().setFirstChild(view.getNextNode());
					view.getNextNode().setPrevNode(null);
				} else if (view.getNextNode() != null) {
					ViewComponentHbm temp, temp2;
					temp2 = view.getNextNode();
					temp = view.getPrevNode();

					temp2.setPrevNode(temp);
					temp.setNextNode(view.getNextNode());
				} else {
					view.getPrevNode().setNextNode(null);
				}
				if (log.isDebugEnabled()) log.debug("Trying to remove Unit-ViewComponent " + view.getViewComponentId());
				getViewComponentHbmDao().remove(view);
				if (log.isDebugEnabled()) log.debug("SUCC remove!");
				// REMOVE END
			} catch (Exception exe) {
				// This is an initial load. Therefor the MOTHER (parent) MUST exist !
				// Maybe this is also a dummy-entry. This can't be inserted with UNITID, therefor we have
				// to search for the VCID.
				if (log.isDebugEnabled()) log.debug("Got Exception");
				try {
					Integer vcid = new Integer(nde.getAttribute("id"));
					ViewComponentHbm view = getViewComponentHbmDao().load(vcid);
					if (log.isDebugEnabled()) log.debug("Found VC");
					if (view.getReference().equals("DUMMY")) {
						if (log.isDebugEnabled()) log.debug("VC is DUMMY");
						getViewComponentHbmDao().remove(view);
					} else {
						if (log.isDebugEnabled()) log.debug("VC is no DUMMY!");
						// This can also be an interrupted import! So maybe ignore the stuff and import?
						// context.setRollbackOnly();
						log.error("ViewComponentFoundButHasNoUnitRelationshipAndIsNotADummy " + vcid + exe);
						throw new UserException("ViewComponentFoundButHasNoUnitRelationshipAndIsNotADummy " + vcid);
					}
				} catch (Exception ex) {
					if (log.isDebugEnabled()) log.debug("Got Exception during findByPrimaryKey " + ex.getMessage());
					// Occures also, if this VC does not exist. Therefor we do not throw an exception
				}

				if (nde.getAttribute("parent") != null && !nde.getAttribute("parent").equals("")) {
					Integer vcid = new Integer(nde.getAttribute("parent"));
					try {
						parent = getViewComponentHbmDao().load(vcid);
					} catch (Exception ee) {
						// context.setRollbackOnly();
						log.warn("ParentUnitNeverDeployed " + vcid);
						throw new UserException("ParentUnitNeverDeployed " + ee.getMessage() + vcid);
					}
					if (nde.getAttribute("next") != null && !nde.getAttribute("next").equals("")) {
						vcid = new Integer(nde.getAttribute("next"));
						try {
							next = getViewComponentHbmDao().load(vcid);
						} catch (Exception ee) {
							ViewComponentHbm viewComponent = createViewComponentHbm(vdl, "DUMMY", "DUMMY", "DUMMY", vcid);
							next = getViewComponentHbmDao().create(viewComponent);
							next.setViewType(Constants.VIEW_TYPE_CONTENT);
							next.setVisible(false);
						}
					}
					if (nde.getAttribute("prev") != null && !nde.getAttribute("prev").equals("")) {
						vcid = new Integer(nde.getAttribute("prev"));
						try {
							prev = getViewComponentHbmDao().load(vcid);
						} catch (Exception ee) {
							// context.setRollbackOnly();
							log.warn("PreviousUnitNeverDeployed");
							throw new UserException("PreviousUnitNeverDeployed " + ee.getMessage() + vcid);
						}
					} else {
						wasFirstChild = true;
					}
				} else {
					// For initial load we expect, that there is the parent component.
					// So currently we also need the ROOT-VC!
					// context.setRollbackOnly();
					log.warn("ParentUnitNeverDeployed of vc " + nde.getAttribute("id"));
					throw new UserException("ParentUnitNeverDeployed " + exe.getMessage());
				}
			}
			// IMPORT
			ViewComponentHbm rootview = createViewComponent(ul.getUnitId(), vdl, savedUnits, nde, parent, prev, liveDeploy, 1, true);
			rootview.setNextNode(next);
			if (next != null) {
				next.setPrevNode(rootview);
			}
			rootview.setAssignedUnit(ul);
			if (wasFirstChild) {
				parent.setFirstChild(rootview);
			}
			// Now we have to catch all childunits, who aren't existent in this Edition. It is possible, that
			// an Editor will deploy an old Edition where some of his child-units aren't created.
			// Therefor we have to catch them, and put them into another unit (or leave them down under the root unit)
			/*
			 * Iterator it = savedUnits.iterator(); while(it.hasNext()) { ViewComponentLocal savedvc = (ViewComponentLocal) it.next(); }
			 */
			// We are doing nothing with them. If they are now under another Unit and this Unit will be deployed,
			// we will stack them back - so no problem with it.
			// If a previous Edition has been deployed, we won't see this unit anymore in the context... So it
			// is also needed to deploy the unit who should reference thisone.
			// There is currently NO WAY to get this "invisible" unit back, without reimporting the newest edition.
			// Maybe we have to find a way for ROOT's, to put them back again.
			if (log.isDebugEnabled()) log.debug("Unit-Import, restoring Hosts");
			{
				Enumeration hosts = this.startPageBackup.keys();
				while (hosts.hasMoreElements()) {
					try {
						String hostName = (String) hosts.nextElement();
						Integer startPageId = this.startPageBackup.get(hostName);
						HostHbm currHost = getHostHbmDao().load(hostName);
						ViewComponentHbm currStartPage = getViewComponentHbmDao().load(startPageId);
						currHost.setStartPage(currStartPage);
					} catch (Exception e) {
						log.warn("Error restoring startpage ", e);
					}
				}
			}
		} catch (Exception exe) {
			// context.setRollbackOnly();
			log.error("Error occured importViewComponentFile", exe);
			throw new UserException("UnknownErrorOccuredWhileImportingEdition" + exe.getMessage());
		}
		// }
		if (log.isDebugEnabled()) log.debug("end importViewComponentFile");
	}

	/**
	 * This create method takes only mandatory (non-nullable) parameters.
	 * 
	 * When the client invokes a create method, the EJB container invokes the ejbCreate method. Typically, an ejbCreate method in an entity bean performs the following tasks:
	 * <UL>
	 * <LI>Inserts the entity state into the database.</LI>
	 * <LI>Initializes the instance variables.</LI>
	 * <LI>Returns the primary key.</LI>
	 * </UL>
	 * 
	 * @param viewDocument
	 *            a_ViewDocument
	 * @param reference
	 *            mandatory CMR field
	 * @param displayLinkName
	 *            mandatory CMR field
	 * @param linkDescription
	 *            mandatory CMR field
	 * @param viewComponentId
	 *            mandatory CMR field
	 * @return A ViewComponentHbm instance
	 * 
	 */
	private ViewComponentHbm createViewComponentHbm(ViewDocumentHbm viewDocument, String reference, String displayLinkName, String linkDescription, Integer viewComponentId) {
		ViewComponentHbm view = null;
		if (viewComponentId == null) {
			view = ViewComponentHbm.Factory.newInstance();
		} else {
			view = getViewComponentHbmDao().load(viewComponentId);
		}
		view.setDisplayLinkName(displayLinkName);
		view.setLinkDescription(linkDescription);
		// ?? view.setUrlLinkName(view.getViewComponentId().toString());

		if (reference.equals("root")) {
			view.setViewType(new Byte("1").byteValue());
		} else if (reference.startsWith("content:")) {
			view.setReference(reference.substring(8));
			view.setViewType(new Byte("1").byteValue());
		} else if (reference.startsWith("jump:")) {
			view.setReference(reference.substring(5));
			view.setViewType(new Byte("2").byteValue());
		} else if (reference.equalsIgnoreCase("SEPARATOR")) {
			view.setViewType(new Byte("7").byteValue());
		} else if (reference.startsWith("link:")) {
			view.setReference(reference.substring(5));
			view.setViewType(new Byte("3").byteValue());
		} else if (reference.startsWith("symlink:")) {
			view.setReference(reference.substring(8));
			view.setViewType(new Byte("6").byteValue());
		} else {
			view.setReference(reference);
		}
		view.setStatus(0);
		view.setOnline((byte) 0);
		view.setVisible(true);
		view.setSearchIndexed(true);
		view.setXmlSearchIndexed(true);
		return view;
	}

	private ArrayList<ViewComponentHbm> moveContainedUnitsAway(ViewComponentHbm view) {
		if (log.isDebugEnabled()) log.debug("begin moveContainedUnitsAway");
		ArrayList<ViewComponentHbm> vec = new ArrayList<ViewComponentHbm>(view.getAllChildrenWithUnits());
		for (ViewComponentHbm vcl : vec) {
			if (log.isDebugEnabled()) log.debug("MOVING OUT: " + vcl.getViewComponentId() + " " + vcl.getLinkDescription());
			ViewComponentHbm prev, next, parent;
			prev = vcl.getPrevNode();
			next = vcl.getNextNode();
			parent = vcl.getParent();

			if (prev == null && next == null) {
				parent.setFirstChild(null);
			} else if (prev == null && next != null) {
				parent.setFirstChild(next);
				next.setPrevNode(null);
			} else if (prev != null && next == null) {
				prev.setNextNode(null);
			} else {
				prev.setNextNode(next);
				next.setPrevNode(prev);
			}
			vcl.setPrevNode(null);
			vcl.setNextNode(null);
			vcl.setParentViewComponent(null); // ROOTUNITID maybe
		}
		if (log.isDebugEnabled()) log.debug("end moveContainedUnitsAway");
		return vec;
	}

	/**
	 * backup Realm2viewComponents and their LoginPages if the LoginPage is located in the given Unit
	 */
	private void backupLoginPages4realm2viewComponents(Iterator realm2viewComponentIterator, Integer unitId) {
		try {
			if (log.isDebugEnabled()) log.debug("backing-up loginPages for Realm2viewComponents");
			while (realm2viewComponentIterator.hasNext()) {
				Realm2viewComponentHbm r = (Realm2viewComponentHbm) realm2viewComponentIterator.next();
				if (r.getLoginPage() != null) {
					if (r.getLoginPage().getUnit4ViewComponent().intValue() == unitId.intValue()) this.loginPagesRealm2viewComponentBackup.put(r.getRealm2viewComponentId(), r.getLoginPage().getViewComponentId());
				}
			}
		} catch (Exception e) {
			log.error("Error occured backing up loginPages for Realm2viewComponents!", e);
		}
	}

	/**
	 * @see de.juwimm.cms.remote.EditionServiceSpring#importHosts(org.w3c.dom.Document, java.lang.Integer)
	 */
	@Override
	protected void handleImportHosts(org.w3c.dom.Document doc, Integer siteId) throws Exception {
		try {
			if (log.isInfoEnabled()) log.info("begin importHosts");
			Iterator itHosts = XercesHelper.findNodes(doc, "/edition/hosts/host");
			if (itHosts.hasNext()) {
				HashMap<String, String> redirectHostsMap = new HashMap<String, String>();
				if (log.isDebugEnabled()) log.debug("Trying to delete Hosts");
				getHostHbmDao().remove(getHostHbmDao().findAll(siteId));
				if (log.isDebugEnabled()) log.debug("Trying to add new Hosts");
				while (itHosts.hasNext()) {
					if (log.isDebugEnabled()) log.debug("Found Host to import...");
					Element elHost = (Element) itHosts.next();
					String hostName = XercesHelper.getNodeValue(elHost, "./hostName");
					String startPageId = XercesHelper.getNodeValue(elHost, "./startPageId");
					String unitId = XercesHelper.getNodeValue(elHost, "./unitId");
					String redirectUrl = XercesHelper.getNodeValue(elHost, "./redirectUrl");
					String redirectHostName = XercesHelper.getNodeValue(elHost, "./redirectHostName");

					if (getHostHbmDao().load(hostName) != null) {
						if (log.isInfoEnabled()) log.info("wont import host " + hostName + " - it is already assigned to another site");
					} else {
						if (log.isDebugEnabled()) log.debug("Importing Host-Entry: " + hostName + " startPageId " + startPageId + " for site " + siteId + " and unitId " + unitId);

						HostHbm host = new HostHbmImpl();
						host.setHostName(hostName.trim());
						host = getHostHbmDao().create(host);
						// set the site for the current host
						if (host != null) {
							try {
								SiteHbm site = getSiteHbmDao().load(siteId);
								if (site != null) host.setSite(site);
							} catch (Exception e) {
								log.warn("Could not find siteId " + siteId + " or set the site to the current host!");
							}
						}

						if (startPageId != null && !startPageId.equalsIgnoreCase("")) {
							try {
								ViewComponentHbm viewComponent = getViewComponentHbmDao().load(Integer.valueOf(startPageId));
								host.setStartPage(viewComponent);
							} catch (Exception exe) {
								log.warn("Could not find ViewComponent with Id " + startPageId + " thus no shortlink has been set for host " + hostName);
							}
						} else {
							if (log.isDebugEnabled()) log.debug("haven't found any startPageId for host " + host.getHostName());
						}
						if (unitId != null && !unitId.equalsIgnoreCase("")) {
							try {
								UnitHbm unit = getUnitHbmDao().load(Integer.valueOf(unitId));
								host.setUnit(unit);
							} catch (Exception exe) {
								log.warn("Could not find Unit with Id " + unitId + " thus no unit has been set for host " + hostName);
							}
						} else {
							if (log.isDebugEnabled()) log.debug("haven't found any unitId for host " + host.getHostName());
						}
						if (redirectUrl != null && redirectUrl.length() > 0) {
							try {
								host.setRedirectUrl(redirectUrl);
							} catch (Exception e) {
								log.warn("Error setting redirectUrl for host " + host.getHostName());
							}
						}
						// keep in mind the host with redirectHost, these may not be created yet
						if (redirectHostName != null && redirectHostName.length() > 0) {
							redirectHostsMap.put(hostName, redirectHostName);
						}
					}
				}
				// restore redirectHosts
				Iterator<String> it = redirectHostsMap.keySet().iterator();
				while (it.hasNext()) {
					String hostName = it.next();
					String redirectHostName = redirectHostsMap.get(hostName);
					try {
						HostHbm host = getHostHbmDao().load(hostName);
						HostHbm redirectHost = getHostHbmDao().load(redirectHostName);
						host.setRedirectHostName(redirectHost);
					} catch (Exception e) {
						log.error("Error restoring redirectHost " + redirectHostName + " for " + hostName + ": " + e.getMessage(), e);
					}
				}
			}
			if (log.isInfoEnabled()) log.info("end importHosts");
		} catch (Exception exe) {
			// context.setRollbackOnly();
			throw new UserException("Error recreating the Hosts " + exe.getMessage());
		}
	}

	/**
	 * Publishs the Edition to the named Liveserver IP Address. <br>
	 * 
	 * @see de.juwimm.cms.remote.EditionServiceSpring#publishEditionToLiveserver(java.lang.Integer)
	 */
	@Override
	protected void handlePublishEditionToLiveserver(Integer editionId) throws Exception {
		String info = "";
		editionCronService.logEditionStatusInfo(LiveserverDeployStatus.TransmitDeployFile, editionId);
		if(log.isDebugEnabled()) log.debug("start publishing to liveserver process");
		try {
			String liveServerIP = "";
			String liveUserName = "";
			String livePassword = "";
			SiteHbm site = null;
			org.w3c.dom.Document doc = null;
			try {
				site = getUserHbmDao().load(AuthenticationHelper.getUserName()).getActiveSite();
				if(log.isDebugEnabled()) log.debug("siteId: "+site.getSiteId());
				doc = XercesHelper.string2Dom(site.getConfigXML());
				liveServerIP = XercesHelper.getNodeValue(doc, "/config/default/liveServer/url");
				if(log.isDebugEnabled()) log.debug("liveserverIp: "+liveServerIP);
				liveUserName = XercesHelper.getNodeValue(doc, "/config/default/liveServer/username");
				if(log.isDebugEnabled()) log.debug("liveUserName: "+liveUserName);
				livePassword = XercesHelper.getNodeValue(doc, "/config/default/liveServer/password");
				if(log.isDebugEnabled()) log.debug("livePassword: "+livePassword);
			} catch (Exception exe) {
				log.error("Error occured reading siteConfig: ", exe);
			} finally {
				doc = null;
			}
			if (liveServerIP == null || liveServerIP.equals("") || "".equalsIgnoreCase(liveUserName)) {
				log.error("LiveServerIP is not correct: " + liveServerIP);
				throw new UserException("LiveServerIP is not correct: " + liveServerIP);
			}

			try {
				if (log.isInfoEnabled()) log.info("publishEditionToLiveserver - using URL: " + "http://" + liveServerIP);
				EditionHbm edition = getEditionHbmDao().load(editionId);

				int unitId = edition.getUnitId();
				int viewDocumentId = edition.getViewDocumentId();

				System.setProperty("tizzit-liveserver.remoteServer", "http://" + liveServerIP);
				ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext-deploy.xml");
				if(log.isDebugEnabled()) log.debug("ApplicationContext: "+ctx);
				try {
					SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_GLOBAL);
					RemoteAuthenticationManager remoteAuthenticationService = (RemoteAuthenticationManager) ctx.getBean("remoteRemoteAuthenticationManagerServiceDeploy");
					//RemoteAuthenticationManager remoteAuthenticationService = RemoteServiceLocator.instance().getRemoteAuthenticationService();
					GrantedAuthority[] authorities = remoteAuthenticationService.attemptAuthentication(liveUserName, String.valueOf(livePassword));
					SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(liveUserName, String.valueOf(livePassword), authorities));
					log.debug(SecurityContextHolder.getContext().getAuthentication());
				} catch (SpringSecurityException e) {
					log.info("authentication failed: " + e.getMessage());
					throw new UserException(e.getMessage());
				}

				AuthorizationServiceSpring autoSpring = (AuthorizationServiceSpring) ctx.getBean("authorizationServiceDeploySpring");
				if (log.isInfoEnabled()) log.info("Logging in on Liveserver...");
				// using remoteLogin in order to not have the site created before the deploy on a site
				autoSpring.remoteLoginLive(liveUserName, livePassword);
				if (log.isInfoEnabled()) log.info("Successfully logged in!");

				if (log.isDebugEnabled()) log.debug("Adding Attachment to Message Call");

				InputStream fis = new BufferedInputStream(new FileInputStream(edition.getEditionFileName()));

				if (log.isInfoEnabled()) log.info("Starting transfer to Liveserver - " + info);

				ClientServiceSpring clientServiceSpring = (ClientServiceSpring) ctx.getBean("clientServiceDeploySpring");
				clientServiceSpring.importEditionFromImport(fis, edition.getViewComponentId(), false, editionId);

				if (log.isInfoEnabled()) log.info("Liveserver has finished deploy - " + info);

				if (log.isInfoEnabled()) log.info("Setting the ViewComponents on Work-Server to \"Online\" - " + info);
				setViewComponentsOnline(edition);
				edition.setNeedsDeploy(false);
			} catch (Exception exe) {
				if (log.isDebugEnabled()) log.debug("Rolling back because of error on Liveserver");
				throw new UserException(exe.getMessage());
			}
			if (log.isInfoEnabled()) log.info("Finished Live-Deployment successfully - " + info);

		} catch (Exception e) {
			editionCronService.logEditionStatusException(editionId, e.getMessage());
			throw new UserException(e.getMessage());
		}
		//editionCronService.logEditionStatusInfo(LiveserverDeployStatus.FileDeployedOnLiveServer, editionId);
	}

	@SuppressWarnings("unchecked")
	private void setViewComponentsOnline(EditionHbm edition) {
		// fullDeploy all
		// unitDeploy just unit
		// pageDeploy just VC
		// rootDeploy non
		ViewComponentHbm vc = getViewComponentHbmDao().load(edition.getViewComponentId());

		if (edition.getDeployType() == Constants.DEPLOY_TYPE_FULL) {
			Collection units = getUnitHbmDao().findAll(edition.getSiteId());
			Collection vds = getViewDocumentHbmDao().findAll(edition.getSiteId());
			for (int unitc = 0; unitc < units.size(); unitc++) {
				for (int vdc = 0; vdc < vds.size(); vdc++) {
					ViewComponentHbm unitRoot = getViewComponentHbmDao().find4Unit(unitc, vdc);
					if (unitRoot != null) {
						unitRoot.setUnitOnline();
					}
				}
			}
		}
		if (edition.getDeployType() == Constants.DEPLOY_TYPE_UNIT) {
			vc.setUnitOnline();
		}
		if (edition.getDeployType() == Constants.DEPLOY_TYPE_PAGE) {
			vc.setStatus(Constants.DEPLOY_STATUS_DEPLOYED);
			vc.setOnline(Constants.ONLINE_STATUS_ONLINE);
		}
	}

	/**
	 * @see de.juwimm.cms.remote.EditionServiceSpring#createLiveDeploy(java.lang.String, java.lang.String, java.lang.Integer, boolean, boolean)
	 */
	@Override
	protected void handleCreateLiveDeploy(String userName, String comment, Integer rootViewComponentId, boolean deploy, boolean showMessage) throws Exception {
		if (rootViewComponentId != null) {
			UserHbm user = null;
			EditionHbm edition = null;
			String unitName = "";
			try {
				unitName = getViewComponentHbmDao().load(rootViewComponentId).getAssignedUnit().getName();
				if (unitName != null) {
					unitName = "(" + unitName.trim() + ")";
				} else {
					unitName = "";
				}
			} catch (Exception e) {
				log.warn("Unit not found for ViewComponent " + rootViewComponentId + ": " + e.getMessage(), e);
			}
			try {
				if (log.isInfoEnabled()) log.info("Start creating Edition for ViewComponent " + rootViewComponentId + " " + unitName);
				EditionHbm editionTemp = this.createEditionHbm(comment, rootViewComponentId, null, !deploy);
				edition = getEditionHbmDao().create(editionTemp);
				if (log.isInfoEnabled()) log.info("Finished creating Edition for ViewComponent " + rootViewComponentId + " " + unitName);
				Collection coll = getEditionHbmDao().findByUnitAndOnline(new Integer(edition.getUnitId()));
				if (log.isDebugEnabled()) log.debug("Finished findByUnitAndOnline");
				Iterator it = coll.iterator();
				while (it.hasNext()) {
					if (log.isDebugEnabled()) log.debug("Starting to disable old Edition");
					EditionHbm eded = (EditionHbm) it.next();
					eded.setStatus((byte) 0);
					if (log.isDebugEnabled()) log.debug("setStatus " + eded.getEditionId());
				}

				if (log.isDebugEnabled()) log.debug("setting status = 1");
				edition.setStatus((byte) 1);
				if (log.isDebugEnabled()) log.debug("finished setting status = 1");
			} catch (Exception exe) {
				if (user == null) {
					log.error("USER HAS NOT BEEN FOUND WHILE CREATING THE EDITION");
				} else if (edition == null) {
					log.error("Unknown Error while creating the Edition");
				} else {
					createUserTask(userName, exe.toString(), rootViewComponentId, Constants.TASK_SYSTEMMESSAGE_ERROR, true);
				}
				log.error("Error occured in createLiveDeploy: ", exe);
				return;
			}
			if (deploy) {
				try {
					if (log.isDebugEnabled()) log.debug("Starting calling publishEditionToLiveserver");
					this.publishEditionToLiveserver(edition.getEditionId());
					if (log.isDebugEnabled()) log.debug("Publish to liveServer sucessfully called!");
					createUserTask(userName, "CreateEditionAndDeploy", rootViewComponentId, Constants.TASK_SYSTEMMESSAGE_INFORMATION, showMessage);
				} catch (Exception re) {
					log.error("Error occured in createLiveDeploy: ", re);
					String errMsg = "";
					errMsg = re.getMessage();
					createUserTask(userName, errMsg, rootViewComponentId, Constants.TASK_SYSTEMMESSAGE_ERROR, true);
					// TODO without the rollback the task is stored and delivered...
					// context.setRollbackOnly();
				}
			} else {
				createUserTask(userName, "CreateEdition", rootViewComponentId, Constants.TASK_SYSTEMMESSAGE_INFORMATION, showMessage);
			}
		} else {
			log.error("RootViewComponentId must not be Null for creating an Edition!");
		}
	}

	private EditionHbm createEditionHbm(String comment, Integer rootViewComponentId, PrintStream out, boolean includeUnused) {
		EditionHbm edition = EditionHbm.Factory.newInstance();
		edition.setStatus((byte) 0);
		edition.setCreationDate((System.currentTimeMillis()));
		edition.setComment(comment);
		return edition;
	}

	/**
	 * @see de.juwimm.cms.remote.EditionServiceSpring#createUserTask(java.lang.String, java.lang.String, java.lang.Integer, byte, boolean)
	 */
	@Override
	protected void handleCreateUserTask(String user, String why, Integer rootViewComponentId, byte taskType, boolean showMessage) throws Exception {
		if (log.isDebugEnabled()) log.debug("createTask: User " + user + " Message " + why + " rootViewComponent " + rootViewComponentId + " taskType " + taskType + " showMessage " + Boolean.toString(showMessage));
		if (showMessage) {
			if (user != null && !user.equals("")) {
				try {
					Integer unitId = this.getUnit4ViewComponent(rootViewComponentId);
					this.createTask(user, null, unitId, why, taskType);
					if (log.isDebugEnabled()) log.debug("Send the information-task to the user " + user);
				} catch (Exception exe) {
					log.error("Error occured creating task for user " + user, exe);
				}
			}
		}
	}

	private Integer createTask(String receiverUserId, String receiverRole, Integer unitId, String comment, byte taskType) throws UserException {
		TaskHbm task = null;
		TaskHbm temp = null;
		try {
			if (receiverUserId != null) {
				UserHbm receiver = getUserHbmDao().load(receiverUserId);
				temp = createTaskHbm(receiver, null, unitId, comment, taskType);
				task = getTaskHbmDao().create(temp);
			} else {
				temp = createTaskHbm(null, receiverRole, unitId, comment, taskType);
				task = getTaskHbmDao().create(temp);
			}
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
		return task.getTaskId();
	}

	private TaskHbm createTaskHbm(UserHbm receiver, String receiverRole, Integer unitId, String comment, byte taskType) {
		TaskHbm task = new TaskHbmImpl();
		task.setReceiver(receiver);
		task.setReceiverRole(receiverRole);
		task.setUnit(getUnitHbmDao().load(unitId));
		task.setComment(comment);
		task.setTaskType(taskType);
		return task;
	}

	private Integer getUnit4ViewComponent(Integer viewComponentId) throws UserException {
		try {
			ViewComponentHbm view = getViewComponentHbmDao().load(viewComponentId);
			if (view.getAssignedUnit() != null) {
				return view.getAssignedUnit().getUnitId();
			}
			return view.getUnit4ViewComponent();
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	@Override
	protected void handleRestoreSafeguardLoginPages(boolean useNewIds) throws Exception {
		if (log.isDebugEnabled()) log.debug("start restoreSafeguard with useNewIDs=" + useNewIds);
		if (log.isInfoEnabled()) log.info("begin restoreSafeguardLoginPages");
		try {
			if (this.loginPagesRealmsSimplePw != null) {
				Iterator it = this.loginPagesRealmsSimplePw.keySet().iterator();
				while (it.hasNext()) {
					Integer simplePwRealmId = (Integer) it.next();
					Integer loginPageId = this.loginPagesRealmsSimplePw.get(simplePwRealmId);
					if (useNewIds) {
						simplePwRealmId = this.mappingRealmsSimplePw.get(simplePwRealmId);
						loginPageId = this.mappingVCs.get(loginPageId);
					}
					if (simplePwRealmId != null && loginPageId != null) {
						RealmSimplePwHbm realm = getRealmSimplePwHbmDao().load(simplePwRealmId);
						realm.setLoginPageId(loginPageId.toString());
					}
				}
			}
			if (this.loginPagesRealmsJdbc != null) {
				Iterator it = this.loginPagesRealmsJdbc.keySet().iterator();
				while (it.hasNext()) {
					Integer jdbcRealmId = (Integer) it.next();
					Integer loginPageId = this.loginPagesRealmsJdbc.get(jdbcRealmId);
					if (useNewIds) {
						jdbcRealmId = this.mappingRealmsJdbc.get(jdbcRealmId);
						loginPageId = this.mappingVCs.get(loginPageId);
					}
					if (jdbcRealmId != null && loginPageId != null) {
						RealmJdbcHbm realm = getRealmJdbcHbmDao().load(jdbcRealmId);
						realm.setLoginPageId(loginPageId.toString());
					}
				}
			}
			if (this.loginPagesRealmsLdap != null) {
				Iterator it = this.loginPagesRealmsLdap.keySet().iterator();
				while (it.hasNext()) {
					Integer ldapRealmId = (Integer) it.next();
					Integer loginPageId = this.loginPagesRealmsLdap.get(ldapRealmId);
					if (useNewIds) {
						ldapRealmId = this.mappingRealmsLdap.get(ldapRealmId);
						loginPageId = this.mappingVCs.get(loginPageId);
					}
					if (ldapRealmId != null && loginPageId != null) {
						RealmLdapHbm realm = getRealmLdapHbmDao().load(ldapRealmId);
						realm.setLoginPageId(loginPageId.toString());
					}
				}
			}
			if (this.loginPagesRealmsJaas != null) {
				Iterator it = this.loginPagesRealmsJaas.keySet().iterator();
				while (it.hasNext()) {
					Integer jaasRealmId = (Integer) it.next();
					Integer loginPageId = this.loginPagesRealmsJaas.get(jaasRealmId);
					if (useNewIds) {
						jaasRealmId = this.mappingRealmsJaas.get(jaasRealmId);
						loginPageId = this.mappingVCs.get(loginPageId);
					}
					if (jaasRealmId != null && loginPageId != null) {
						RealmJaasHbm realm = getRealmJaasHbmDao().load(jaasRealmId);
						realm.setLoginPageId(loginPageId.toString());
					}
				}
			}
			if (this.loginPagesRealm2vc != null) {
				Iterator it = this.loginPagesRealm2vc.keySet().iterator();
				while (it.hasNext()) {
					Integer viewComponentId = (Integer) it.next();
					Integer loginPageId = this.loginPagesRealm2vc.get(viewComponentId);
					if (useNewIds) {
						viewComponentId = this.mappingVCs.get(viewComponentId);
						loginPageId = this.mappingVCs.get(loginPageId);
					}
					if (viewComponentId != null && loginPageId != null) {
						ViewComponentHbm protectedViewComponent = getViewComponentHbmDao().load(viewComponentId);
						ViewComponentHbm loginPage = getViewComponentHbmDao().load(loginPageId);
						protectedViewComponent.getRealm2vc().setLoginPage(loginPage);
					}
				}
			}
			if (!useNewIds) {
				// only after deploy restore loginpages for realm used in other units
				if (this.loginPagesSimplePwRealmBackup != null) {
					Iterator it = this.loginPagesSimplePwRealmBackup.keySet().iterator();
					while (it.hasNext()) {
						Integer realmId = (Integer) it.next();
						Integer loginPageId = this.loginPagesSimplePwRealmBackup.get(realmId);
						RealmSimplePwHbm realm = null;
						try {
							realm = getRealmSimplePwHbmDao().load(realmId);
						} catch (Exception e) {
							if (log.isDebugEnabled()) log.debug("SimplePwRealm " + realmId + " does not exist any more");
							continue;
						}
						ViewComponentHbm loginPage = null;
						try {
							loginPage = getViewComponentHbmDao().load(loginPageId);
						} catch (Exception e) {
							if (log.isDebugEnabled()) log.debug("LoginPage " + loginPageId + " does not exist any more");
							continue;
						}
						if (realm != null && loginPage != null) {
							realm.setLoginPageId(loginPageId.toString());
						}
					}
				}
				if (this.loginPagesJdbcRealmBackup != null) {
					Iterator it = this.loginPagesJdbcRealmBackup.keySet().iterator();
					while (it.hasNext()) {
						Integer realmId = (Integer) it.next();
						Integer loginPageId = this.loginPagesJdbcRealmBackup.get(realmId);
						RealmJdbcHbm realm = null;
						try {
							realm = getRealmJdbcHbmDao().load(realmId);
						} catch (Exception e) {
							if (log.isDebugEnabled()) log.debug("JdbcRealm " + realmId + " does not exist any more");
							continue;
						}
						ViewComponentHbm loginPage = null;
						try {
							loginPage = getViewComponentHbmDao().load(loginPageId);
						} catch (Exception e) {
							if (log.isDebugEnabled()) log.debug("LoginPage " + loginPageId + " does not exist any more");
							continue;
						}
						if (realm != null && loginPage != null) {
							realm.setLoginPageId(loginPageId.toString());
						}
					}
				}
				if (this.loginPagesJaasRealmBackup != null) {
					Iterator it = this.loginPagesJaasRealmBackup.keySet().iterator();
					while (it.hasNext()) {
						Integer realmId = (Integer) it.next();
						Integer loginPageId = this.loginPagesJaasRealmBackup.get(realmId);
						RealmJaasHbm realm = null;
						try {
							realm = getRealmJaasHbmDao().load(realmId);
						} catch (Exception e) {
							if (log.isDebugEnabled()) log.debug("JaasRealm " + realmId + " does not exist any more");
							continue;
						}
						ViewComponentHbm loginPage = null;
						try {
							loginPage = getViewComponentHbmDao().load(loginPageId);
						} catch (Exception e) {
							if (log.isDebugEnabled()) log.debug("LoginPage " + loginPageId + " does not exist any more");
							continue;
						}
						if (realm != null && loginPage != null) {
							realm.setLoginPageId(loginPageId.toString());
						}
					}
				}
				if (this.loginPagesLdapRealmBackup != null) {
					Iterator it = this.loginPagesLdapRealmBackup.keySet().iterator();
					while (it.hasNext()) {
						Integer realmId = (Integer) it.next();
						Integer loginPageId = this.loginPagesLdapRealmBackup.get(realmId);
						RealmLdapHbm realm = null;
						try {
							realm = getRealmLdapHbmDao().load(realmId);
						} catch (Exception e) {
							if (log.isDebugEnabled()) log.debug("LdapRealm " + realmId + " does not exist any more");
							continue;
						}
						ViewComponentHbm loginPage = null;
						try {
							loginPage = getViewComponentHbmDao().load(loginPageId);
						} catch (Exception e) {
							if (log.isDebugEnabled()) log.debug("LoginPage " + loginPageId + " does not exist any more");
							continue;
						}
						if (realm != null && loginPage != null) {
							realm.setLoginPageId(loginPageId.toString());
						}
					}
				}
				if (this.loginPagesRealm2viewComponentBackup != null) {
					Iterator it = this.loginPagesRealm2viewComponentBackup.keySet().iterator();
					while (it.hasNext()) {
						Integer realmId = (Integer) it.next();
						Integer loginPageId = this.loginPagesRealm2viewComponentBackup.get(realmId);
						Realm2viewComponentHbm realm = null;
						try {
							realm = getRealm2viewComponentHbmDao().load(realmId);
						} catch (Exception e) {
							if (log.isDebugEnabled()) log.debug("Realm2viewComponent " + realmId + " does not exist any more");
							continue;
						}
						ViewComponentHbm loginPage = null;
						try {
							loginPage = getViewComponentHbmDao().load(loginPageId);
						} catch (Exception e) {
							if (log.isDebugEnabled()) log.debug("LoginPage " + loginPageId + " does not exist any more");
							continue;
						}
						if (realm != null && loginPage != null) {
							realm.setLoginPage(loginPage);
							loginPage.getRealm4login().add(realm);
						}
					}
				}
			}
		} catch (Exception e) {
			log.warn("Error occured restoring LoginPages: " + e.getMessage(), e);
		}
		if (log.isInfoEnabled()) log.info("end restoreSafeguardLoginPages");
	}

	/**
	 * #################################################################################### S T E P 6 Reparse ViewComponents / Content for Internal Links, Pics, Docs
	 * ####################################################################################
	 */
	private void reparseViewComponent(ViewComponentHbm vcl) throws Exception {
		// if (!context.getRollbackOnly()) {
		try {
			String ref = vcl.getReference();
			if (log.isDebugEnabled()) log.debug("Reparsing VC: " + vcl.getDisplayLinkName());
			switch (vcl.getViewType()) {
				case Constants.VIEW_TYPE_EXTERNAL_LINK: // dont do anythink at the external link
				case Constants.VIEW_TYPE_SEPARATOR:
					break;
				case Constants.VIEW_TYPE_INTERNAL_LINK:
				case Constants.VIEW_TYPE_SYMLINK:
					String newRefId = ref;
					try {
						newRefId = mappingVCs.get(new Integer(ref)).toString();
					} catch (Exception exe) {
						log.warn("Error while changing ref: " + ref + " to " + newRefId + " : " + exe.getMessage());
					}
					vcl.setReference(newRefId);
					break;
				case Constants.VIEW_TYPE_CONTENT:
				case Constants.VIEW_TYPE_UNIT:
				default:
					if (log.isDebugEnabled()) log.debug("Reparsing Content of VC to Ref: " + ref);
					if (ref != null && !ref.equalsIgnoreCase("") && !ref.equalsIgnoreCase("root")) {
						ContentHbm content = null;
						try {
							content = getContentHbmDao().load(new Integer(ref));
						} catch (Exception e) {
							log.warn("Cannot find referenced content " + ref + " - continue import");
						}
						if (content != null) {
							Collection cvlColl = content.getContentVersions();
							Iterator cvlIt = cvlColl.iterator();
							while (cvlIt.hasNext()) {
								ContentVersionHbm cvl = (ContentVersionHbm) cvlIt.next();
								String text = cvl.getText();
								if (text != null && !text.equalsIgnoreCase("")) {
									org.w3c.dom.Document doc = null;
									try {
										InputSource in = new InputSource(new StringReader(text));
										doc = XercesHelper.inputSource2Dom(in);
									} catch (Exception exe) {
									}
									if (doc != null) {
										if (log.isDebugEnabled()) log.debug("Found ContentVersion: " + cvl.getContentVersionId() + " with DOC");
										Iterator itILinks = XercesHelper.findNodes(doc, "//internalLink/internalLink");
										while (itILinks.hasNext()) {
											Element elm = (Element) itILinks.next();
											String oldId = elm.getAttribute("viewid");
											try {
												System.out.println("oldId " + oldId);
												String newId = mappingVCs.get(new Integer(oldId)).toString();
												elm.setAttribute("viewid", newId);
											} catch (Exception exe) {
											}
										}
										Iterator itPics = XercesHelper.findNodes(doc, "//image[@src]");
										while (itPics.hasNext()) {
											Element elm = (Element) itPics.next();
											String oldId = elm.getAttribute("src");
											try {
												String newId = mappingPics.get(new Integer(oldId)).toString();
												if (log.isDebugEnabled()) log.debug("newId " + newId);
												elm.setAttribute("src", newId);
												((Element) elm.getParentNode()).setAttribute("description", newId);
											} catch (Exception exe) {
											}
										}
										Iterator itDocs = XercesHelper.findNodes(doc, "//documents/document");
										while (itDocs.hasNext()) {
											Element elm = (Element) itDocs.next();
											String oldId = elm.getAttribute("src");
											try {
												String newId = mappingDocs.get(new Integer(oldId)).toString();
												elm.setAttribute("src", newId);
												content.setUpdateSearchIndex(true);
											} catch (Exception exe) {
											}
										}
										/*
										 * Here the reparsing of the database components has to be called
										 */
										Iterator itAggregation = XercesHelper.findNodes(doc, "//aggregation//include");
										while (itAggregation.hasNext()) {
											try {
												Element ndeInclude = (Element) itAggregation.next();
												String type = ndeInclude.getAttribute("type");
												Long id = new Long(ndeInclude.getAttribute("id"));

												if (type.equals("person")) {
													if (mappingPersons.containsKey(id)) {
														Long newId = mappingPersons.get(id);
														ndeInclude.setAttribute("id", newId.toString());
													}
												} else if (type.equals("address")) {
													if (mappingAddresses.containsKey(id)) {
														Long newId = mappingAddresses.get(id);
														ndeInclude.setAttribute("id", newId.toString());
													}
												} else if (type.equals("unit")) {
													// Unit hat Integer Primary Keys
													if (mappingUnits.containsKey(new Integer(id.intValue()))) {
														Integer newId = mappingUnits.get(new Integer(id.intValue()));
														ndeInclude.setAttribute("id", newId.toString());
													}
												} else if (type.equals("department")) {
													if (mappingDepartments.containsKey(id)) {
														Long newId = mappingDepartments.get(id);
														ndeInclude.setAttribute("id", newId.toString());
													}
												} else if (type.equals("talkTime")) {
													if (mappingTalktime.containsKey(id)) {
														Long newId = mappingTalktime.get(id);
														ndeInclude.setAttribute("id", newId.toString());
													}
												}
											} catch (Exception exe) {
												log.error("Error during the replacement of database components:", exe);
											}
										}
										// READY
										text = XercesHelper.doc2String(doc);
										cvl.setText(text);
									}
								}
							}
						}
					}
					// children
					if (vcl.getFirstChild() != null) {
						reparseViewComponent(vcl.getFirstChild());
					}
					break;
			}
			// next
			if (vcl.getNextNode() != null) {
				reparseViewComponent(vcl.getNextNode());
			}
		} catch (Exception exe) {
			// context.setRollbackOnly();
			throw exe;
		}
		// }
	}

	private Realm2viewComponentHbm createTempRealm(ViewComponentHbm viewComponent, String neededRole, RealmJdbcHbm jdbcRealm, RealmSimplePwHbm simplePWRealm, RealmLdapHbm ldapRealm, RealmJaasHbm jaasRealm) {
		Realm2viewComponentHbm tempRealm = new Realm2viewComponentHbmImpl();
		tempRealm.setViewComponent(viewComponent);
		tempRealm.setRoleNeeded(neededRole);
		if (jdbcRealm != null) {
			tempRealm.setJdbcRealm(getRealmJdbcHbmDao().load(jdbcRealm.getJdbcRealmId()));
		}
		if (simplePWRealm != null) {
			tempRealm.setSimplePwRealm(getRealmSimplePwHbmDao().load(simplePWRealm.getSimplePwRealmId()));
		}
		if (ldapRealm != null) {
			tempRealm.setLdapRealm(getRealmLdapHbmDao().load(ldapRealm.getLdapRealmId()));
		}
		if (jaasRealm != null) {
			tempRealm.setJaasRealm(getRealmJaasHbmDao().load(jaasRealm.getJaasRealmId()));
		}
		return tempRealm;
	}

	/**
	 * @param fulldeploy
	 *            means, if this is an import of an Full-Edition, Unit-Edition or Live-Deploy
	 *            <ul>
	 *            <li>0 - means LiveDeploy</li>
	 *            <li>1 - means Fulldeploy of an FullEdition</li>
	 *            <li>2 - means Fulldeploy of an UnitEdition</li>
	 *            </ul>
	 */
	private ViewComponentHbm createViewComponent(Integer unitId, ViewDocumentHbm viewDocument, ArrayList<ViewComponentHbm> savedUnits, Element nde, ViewComponentHbm parent, ViewComponentHbm prev, boolean livedeploy, int fulldeploy, boolean useNewIds) throws Exception {
		boolean reusePrimaryKey = (fulldeploy == 0);
		ViewComponentHbm viewComponent = null;
		try {
			Integer myUnitId = new Integer(nde.getAttribute("unitId"));
			Integer vcId = new Integer(nde.getAttribute("id"));
			if (fulldeploy == 2 && !myUnitId.equals(unitId)) {
				return null;
			} else if (fulldeploy > 0 || myUnitId.equals(unitId)) {
				String reference = XercesHelper.getNodeValue(nde, "./reference");
				String linkName = XercesHelper.getNodeValue(nde, "./linkName");
				String approvedLinkName = XercesHelper.getNodeValue(nde, "./approvedLinkName");
				String statusInfo = XercesHelper.getNodeValue(nde, "./statusInfo");
				String urlLinkName = XercesHelper.getNodeValue(nde, "./urlLinkName");

				byte viewType = new Byte(XercesHelper.getNodeValue(nde, "./viewType")).byteValue();
				boolean visible = Boolean.valueOf(XercesHelper.getNodeValue(nde, "./visible")).booleanValue();
				if (livedeploy && (viewType == Constants.VIEW_TYPE_CONTENT || viewType == Constants.VIEW_TYPE_UNIT)) {
					Node publsh = XercesHelper.findNode(nde, "./content/contentVersion/version[string()='PUBLS']");

					if (log.isDebugEnabled()) log.debug("viewType " + viewType);
					if (publsh == null) {
						if (log.isDebugEnabled()) log.debug("Haven't found a Cool-Online-Version (PUBLS), does not create VC " + vcId);
						return null;
					}
				}
				if (fulldeploy > 0) {
					if (useNewIds) {
						viewComponent = getViewComponentHbmDao().create(viewDocument, reference, linkName, statusInfo, urlLinkName, null);
						viewComponent.setApprovedLinkName(approvedLinkName);
						mappingVCs.put(vcId, viewComponent.getViewComponentId());
					} else {
						// viewDocs can only be crated with viewComps 
						if (viewDocument.getViewComponent().getViewComponentId().compareTo(vcId) == 0) {
							viewComponent = viewDocument.getViewComponent();
						} else {
							viewComponent = getViewComponentHbmDao().load(vcId);
						}
						if (viewComponent == null) {
							viewComponent = getViewComponentHbmDao().create(viewDocument, reference, linkName, statusInfo, urlLinkName, vcId);
						} else {
							viewComponent.setDisplayLinkName(linkName);
							viewComponent.setViewDocument(viewDocument);
							viewComponent.setReference(reference);
							viewComponent.setUrlLinkName(urlLinkName);
							viewComponent.setLinkDescription(statusInfo);
						}
						viewComponent.setApprovedLinkName(approvedLinkName);
						mappingVCs.put(vcId, vcId);
					}
					if (fulldeploy == 1) {
						Integer newUnitId = mappingUnits.get(myUnitId);
						if (newUnitId == null) {
							newUnitId = myUnitId;
						}
						if (unitId == null || !newUnitId.equals(unitId)) {
							if (log.isDebugEnabled()) log.debug("newUnitId " + newUnitId + " myUnitId " + myUnitId);
							UnitHbm unit = getUnitHbmDao().load(newUnitId);
							viewComponent.setAssignedUnit(unit);
							unitId = unit.getUnitId();
						}
					}
				} else {
					try {
						if (log.isDebugEnabled()) log.debug("searching ViewComponent: " + vcId);
						viewComponent = getViewComponentHbmDao().load(vcId);
						viewComponent.setViewDocument(viewDocument);
						viewComponent.setReference(reference);
						viewComponent.setDisplayLinkName(linkName);
						viewComponent.setLinkDescription(statusInfo);
						viewComponent.setUrlLinkName(urlLinkName);
						viewComponent.setApprovedLinkName(approvedLinkName);
					} catch (Exception e) {
						if (log.isDebugEnabled()) log.debug("error, creating ViewComponent: " + vcId);
						ViewComponentHbm tempVC = new ViewComponentHbmImpl();
						tempVC.setViewDocument(viewDocument);
						tempVC.setReference(reference);
						tempVC.setLinkDescription(linkName);
						tempVC.setStatus(Integer.parseInt(statusInfo));
						tempVC.setUrlLinkName(urlLinkName);
						tempVC.setViewComponentId(vcId);
						viewComponent = getViewComponentHbmDao().create(tempVC);
						viewComponent.setApprovedLinkName(approvedLinkName);
					}
					viewComponent.setOnline((byte) 1);
				}
				viewComponent.setPrevNode(prev);
				if (prev != null) prev.setNextNode(viewComponent);
				viewComponent.setParentViewComponent(parent);
				viewComponent.setMetaData(XercesHelper.getNodeValue(nde, "./metaKeywords"));
				viewComponent.setMetaDescription(XercesHelper.getNodeValue(nde, "./metaDescription"));
				String onlineStart = XercesHelper.getNodeValue(nde, "./onlineStart");
				if (!onlineStart.equals("")) {
					if (log.isDebugEnabled()) log.debug("OnlineStart: " + onlineStart);
					viewComponent.setOnlineStart(Long.parseLong(onlineStart));
				}
				String onlineStop = XercesHelper.getNodeValue(nde, "./onlineStop");
				if (!onlineStop.equals("")) {
					if (log.isDebugEnabled()) log.debug("OnlineStop: " + onlineStop);
					viewComponent.setOnlineStop(Long.parseLong(onlineStop));
				}
				viewComponent.setViewLevel(XercesHelper.getNodeValue(nde, "./viewLevel"));
				viewComponent.setViewIndex(XercesHelper.getNodeValue(nde, "./viewIndex"));
				viewComponent.setDisplaySettings(Byte.parseByte(XercesHelper.getNodeValue(nde, "./displaySettings")));
				viewComponent.setShowType(Byte.parseByte(XercesHelper.getNodeValue(nde, "./showType")));
				viewComponent.setViewType(viewType);
				viewComponent.setVisible(visible);
				viewComponent.setSearchIndexed(Boolean.valueOf(XercesHelper.getNodeValue(nde, "./searchIndexed")).booleanValue());
				viewComponent.setXmlSearchIndexed(Boolean.valueOf(XercesHelper.getNodeValue(nde, "./xmlSearchIndexed")).booleanValue());
				byte status = -1;
				try {
					status = new Byte(XercesHelper.getNodeValue(nde, "./status")).byteValue();
				} catch (Exception e) {
					if (log.isDebugEnabled()) log.debug("Status was not set...");
				}
				// on import set deploy-state to "edited"
				if (!livedeploy) status = Constants.DEPLOY_STATUS_EDITED;
				viewComponent.setStatus(status);
				byte onlineState = -1;
				try {
					onlineState = new Byte(XercesHelper.getNodeValue(nde, "./online")).byteValue();
				} catch (Exception e) {
					if (log.isDebugEnabled()) log.debug("OnlineState was not set...");
				}
				// on import set online-state to offline
				if (!livedeploy) onlineState = Constants.ONLINE_STATUS_OFFLINE;
				viewComponent.setOnline(onlineState);
				if (fulldeploy == 0 && viewComponent.getStatus() == Constants.DEPLOY_STATUS_APPROVED) {
					// this only occures on new pages that are deployed for the first time
					// (they dont have the state online on workserver because of different transactions)
					if (log.isDebugEnabled()) log.debug("setting online: " + viewComponent.getDisplayLinkName());
					viewComponent.setOnline((byte) 1);
				}
				{
					// realm2viewComponent
					Node nodeRealm = XercesHelper.findNode(nde, "realm2viewComponent");
					if (nodeRealm != null) {
						// this ViewComponent is protected by a realm
						if (fulldeploy > 0) {
							// use new ids
							String neededrole = XercesHelper.getNodeValue(nodeRealm, "roleNeeded");
							String loginPage = XercesHelper.getNodeValue(nodeRealm, "loginPageId");
							if (loginPage != null && !"".equalsIgnoreCase(loginPage)) {
								Integer loginPageId = null;
								try {
									loginPageId = Integer.valueOf(loginPage);
								} catch (Exception e) {
								}
								if (loginPageId != null) loginPagesRealm2vc.put(vcId, loginPageId);
							}
							Node relNode = XercesHelper.findNode(nodeRealm, "jdbcRealmId");
							if (relNode != null) {
								Integer id = new Integer(XercesHelper.getNodeValue(relNode));
								RealmJdbcHbm sqlrealm = null;
								if (useNewIds) {
									sqlrealm = getRealmJdbcHbmDao().load(mappingRealmsJdbc.get(id));
								} else {
									sqlrealm = getRealmJdbcHbmDao().load(id);
								}
								Realm2viewComponentHbm tempRealm = createTempRealm(viewComponent, neededrole, sqlrealm, null, null, null);
								Realm2viewComponentHbm r = getRealm2viewComponentHbmDao().create(tempRealm);
								viewComponent.setRealm2vc(r);
							} else {
								relNode = XercesHelper.findNode(nodeRealm, "simplePwRealmId");
								if (relNode != null) {
									Integer id = new Integer(XercesHelper.getNodeValue(relNode));
									RealmSimplePwHbm realm = null;
									if (useNewIds) {
										realm = getRealmSimplePwHbmDao().load(mappingRealmsSimplePw.get(id));
									} else {
										realm = getRealmSimplePwHbmDao().load(id);
									}
									Realm2viewComponentHbm tempRealm = createTempRealm(viewComponent, neededrole, null, realm, null, null);
									Realm2viewComponentHbm r = getRealm2viewComponentHbmDao().create(tempRealm);
									viewComponent.setRealm2vc(r);
								} else {
									relNode = XercesHelper.findNode(nodeRealm, "ldapRealmId");
									if (relNode != null) {
										Integer id = new Integer(XercesHelper.getNodeValue(relNode));
										RealmLdapHbm realm = null;
										if (useNewIds) {
											realm = getRealmLdapHbmDao().load(mappingRealmsLdap.get(id));
										} else {
											realm = getRealmLdapHbmDao().load(id);
										}
										Realm2viewComponentHbm tempRealm = createTempRealm(viewComponent, neededrole, null, null, realm, null);
										Realm2viewComponentHbm r = getRealm2viewComponentHbmDao().create(tempRealm);
										viewComponent.setRealm2vc(r);
									} else {
										relNode = XercesHelper.findNode(nodeRealm, "jaasRealmId");
										if (relNode != null) {
											Integer id = new Integer(XercesHelper.getNodeValue(relNode));
											RealmJaasHbm realm = null;
											if (useNewIds) {
												realm = getRealmJaasHbmDao().load(mappingRealmsJaas.get(id));
											} else {
												realm = getRealmJaasHbmDao().load(id);
											}
											Realm2viewComponentHbm tempRealm = createTempRealm(viewComponent, neededrole, null, null, null, realm);
											Realm2viewComponentHbm r = getRealm2viewComponentHbmDao().create(tempRealm);
											viewComponent.setRealm2vc(r);
										}
									}
								}
							}
						} else {
							Realm2viewComponentHbm tempRealm = new Realm2viewComponentHbmImpl();
							tempRealm.setViewComponent(viewComponent);
							Realm2viewComponentHbm r = getRealm2viewComponentHbmDao().create(tempRealm);
							viewComponent.setRealm2vc(r);
							String loginPage = XercesHelper.getNodeValue(nodeRealm, "loginPageId");
							if (loginPage != null && !"".equalsIgnoreCase(loginPage)) {
								Integer loginPageId = null;
								try {
									loginPageId = Integer.valueOf(loginPage);
								} catch (Exception e) {
								}
								if (loginPageId != null) loginPagesRealm2vc.put(vcId, loginPageId);
							}
						}
					}
				}
				// CONTENT
				Element cnde = (Element) XercesHelper.findNode(nde, "./content");
				if (cnde != null) {
					ContentHbm content = getContentHbmDao().createFromXml(cnde, reusePrimaryKey, livedeploy, null, null, null, null);
					viewComponent.setReference(content.getContentId().toString());
				}
				// CHILDREN
				Iterator it = XercesHelper.findNodes(nde, "./viewcomponent");
				ViewComponentHbm childprev = null;
				ViewComponentHbm tmp = null;
				while (it.hasNext()) {
					Element childnode = (Element) it.next();
					tmp = createViewComponent(unitId, viewDocument, savedUnits, childnode, viewComponent, childprev, livedeploy, fulldeploy, useNewIds);
					if (childprev != null) {
						childprev.setNextNode(tmp);
					} else {
						viewComponent.setFirstChild(tmp);
					}
					if (tmp != null) childprev = tmp;
				}
			} else {
				// Im a ViewComponent from another Unit. This must be inside the Array... HEY ARRAY !!!
				boolean found = false;
				Iterator it = savedUnits.iterator();
				while (it.hasNext()) {
					ViewComponentHbm savedvc = (ViewComponentHbm) it.next();
					if (myUnitId.equals(savedvc.getAssignedUnit().getUnitId())) {
						viewComponent = savedvc;
						viewComponent.setPrevNode(prev);
						byte showtype = viewComponent.getShowType();
						viewComponent.setParentViewComponent(parent);
						viewComponent.setShowType(showtype);
						savedUnits.remove(savedvc);
						found = true;
						if (log.isDebugEnabled()) log.debug("MOVING BACK: " + viewComponent.getDisplayLinkName());
						break;
					}
				}
				if (!found) {
					// This means: We are importing a Edition, where we suggest, that there is already this
					// Unit deployed, what we have as child-Unit.
					// It is possible, that we are deploying a unit, where this child-Unis haven't deployed yet -
					// f.e. initial-deploy or this child-unit has been new created and never deployed at this special
					// moment.
					// Therefor we will create a DUMMY entry with a "DUMMY"-Reference, so we can map the child-unit to
					// this place, if the child-Unit Editor wants to deploy his unit.
					try {
						// FIRST we will take a look, if there is already this Unit - but wasn't found here.
						// This could be possible, if this unit was been "outtaked" and this is an old Edition,
						// we will find this Unit anywhere else.
						viewComponent = getViewComponentHbmDao().find4Unit(myUnitId, viewDocument.getViewDocumentId());
					} catch (Exception exe) {
						try {
							UnitHbm unit = getUnitHbmDao().load(myUnitId);
							ViewComponentHbm tempVC = new ViewComponentHbmImpl();
							tempVC.setViewDocument(viewDocument);
							tempVC.setReference("DUMMY");
							tempVC.setDisplayLinkName("SUB_ENTRY_NOT_DEPLOYED");
							tempVC.setLinkDescription("SUB_ENTRY_NOT_DEPLOYED");
							tempVC.setViewComponentId(vcId);
							viewComponent = getViewComponentHbmDao().create(tempVC);
							viewComponent.setViewType(Constants.VIEW_TYPE_UNIT);
							viewComponent.setVisible(false);
							viewComponent.setAssignedUnit(unit);
						} catch (Exception ex) {
							// Jens - ??
							// context.setRollbackOnly();
							throw new UserException("UnitWasNeverDeployed");
						}
					}
					viewComponent.setPrevNode(prev);
					viewComponent.setParentViewComponent(parent);
				}
			}
		} catch (Exception exe) {
			// Jens - ??
			// context.setRollbackOnly();
			log.error("Error occured", exe);
			throw exe;
		}
		// IMPORT END
		return viewComponent;
	}

	private void importRealms(org.w3c.dom.Document doc, Integer siteId, boolean useNewIDs) {
		if (log.isDebugEnabled()) log.debug("start importRealms with useNewIDs=" + useNewIDs);
		mappingRealmsSimplePw = new Hashtable<Integer, Integer>();
		mappingRealmsJdbc = new Hashtable<Integer, Integer>();
		mappingRealmsLdap = new Hashtable<Integer, Integer>();
		mappingRealmsJaas = new Hashtable<Integer, Integer>();
		loginPagesRealmsSimplePw = new Hashtable<Integer, Integer>();
		loginPagesRealmsJdbc = new Hashtable<Integer, Integer>();
		loginPagesRealmsLdap = new Hashtable<Integer, Integer>();
		loginPagesRealmsJaas = new Hashtable<Integer, Integer>();
		loginPagesRealm2vc = new Hashtable<Integer, Integer>();
		try {
			if (XercesHelper.findNode(doc, "/edition/realms") != null) {
				SiteHbm site = getSiteHbmDao().load(siteId);
				Iterator itRealms = XercesHelper.findNodes(doc, "/edition/realms/realmsSimplePw/realmSimplePw");
				while (itRealms.hasNext()) {
					if (log.isDebugEnabled()) log.debug("Found RealmSimplePw to import...");
					Element elmRealm = (Element) itRealms.next();
					Integer id = new Integer(XercesHelper.getNodeValue(elmRealm, "simplePwRealmId"));
					RealmSimplePwHbm realm = null;
					if (useNewIDs) {
						realm = getRealmSimplePwHbmDao().create(elmRealm, true);
						realm.setSite(site);
						mappingRealmsSimplePw.put(id, realm.getSimplePwRealmId()); // mapping OLD-ID to NEW-ID
					} else {
						try {
							if (log.isDebugEnabled()) log.debug("searching RealmSimplePw: " + id);
							realm = getRealmSimplePwHbmDao().load(id);
							String realmName = XercesHelper.getNodeValue(elmRealm, "realmName");
							if (realmName != null && realmName.length() > 0) {
								realm.setRealmName(realmName);
							} else {
								realm.setRealmName(null);
							}
							realm.setSite(site);
							{
								// import users, first delete all existing ones for this realm
								Collection toDelete = new ArrayList();
								toDelete.addAll(realm.getSimplePwRealmUsers());
								Iterator it = toDelete.iterator();
								while (it.hasNext()) {
									getRealmSimplePwUserHbmDao().remove(((RealmSimplePwUserHbm) it.next()));
								}
								toDelete.clear();
								realm.getSimplePwRealmUsers().clear();
								Iterator itUsers = XercesHelper.findNodes(elmRealm, "simplePwRealmUsers/realmSimplePwUser");
								while (itUsers.hasNext()) {
									Element elmUser = (Element) itUsers.next();
									RealmSimplePwUserHbm user = ((RealmSimplePwUserHbmDaoImpl) getRealmSimplePwUserHbmDao()).create(elmUser, false);
									user.setSimplePwRealm(realm);
									realm.getSimplePwRealmUsers().add(user);
								}
							}
						} catch (Exception exe) {
							if (log.isDebugEnabled()) log.debug("creating RealmSimplePw: " + id);
							realm = getRealmSimplePwHbmDao().create(elmRealm, false);
							realm.setSite(site);
						}
					}
					String loginPageId = XercesHelper.getNodeValue(elmRealm, "loginPageId");
					if (loginPageId != null && loginPageId.length() > 0) loginPagesRealmsSimplePw.put(id, new Integer(loginPageId));
				}
				itRealms = XercesHelper.findNodes(doc, "/edition/realms/realmsLdap/realmLdap");
				while (itRealms.hasNext()) {
					if (log.isDebugEnabled()) log.debug("Found RealmLdap to import...");
					Element elmRealm = (Element) itRealms.next();
					Integer id = new Integer(XercesHelper.getNodeValue(elmRealm, "ldapRealmId"));
					RealmLdapHbm realm = null;
					if (useNewIDs) {
						realm = getRealmLdapHbmDao().create(elmRealm, true);
						realm.setSite(site);
						mappingRealmsLdap.put(id, realm.getLdapRealmId()); // mapping OLD-ID to NEW-ID
					} else {
						try {
							if (log.isDebugEnabled()) log.debug("searching RealmLdap: " + id);
							realm = getRealmLdapHbmDao().load(id);
							String realmName = XercesHelper.getNodeValue(elmRealm, "realmName");
							if (realmName != null && realmName.length() > 0) {
								realm.setRealmName(realmName);
							} else {
								realm.setRealmName(null);
							}
							String ldapPrefix = XercesHelper.getNodeValue(elmRealm, "ldapPrefix");
							realm.setLdapPrefix(ldapPrefix);
							String ldapSuffix = XercesHelper.getNodeValue(elmRealm, "ldapSuffix");
							realm.setLdapSuffix(ldapSuffix);
							String ldapUrl = XercesHelper.getNodeValue(elmRealm, "ldapUrl");
							realm.setLdapUrl(ldapUrl);
							String ldapAuthenticationType = XercesHelper.getNodeValue(elmRealm, "ldapAuthenticationType");
							realm.setLdapAuthenticationType(ldapAuthenticationType);
							realm.setSite(site);
						} catch (Exception exe) {
							if (log.isDebugEnabled()) log.debug("creating RealmLdap: " + id);
							realm = getRealmLdapHbmDao().create(elmRealm, false);
							realm.setSite(site);
						}
					}
					String loginPageId = XercesHelper.getNodeValue(elmRealm, "loginPageId");
					if (loginPageId != null && loginPageId.length() > 0) loginPagesRealmsLdap.put(id, new Integer(loginPageId));
				}
				itRealms = XercesHelper.findNodes(doc, "/edition/realms/realmsJdbc/realmJdbc");
				while (itRealms.hasNext()) {
					if (log.isDebugEnabled()) log.debug("Found RealmJdbc to import...");
					Element elmRealm = (Element) itRealms.next();
					Integer id = new Integer(XercesHelper.getNodeValue(elmRealm, "jdbcRealmId"));
					RealmJdbcHbm realm = null;
					if (useNewIDs) {
						realm = getRealmJdbcHbmDao().create(elmRealm, true);
						realm.setSite(site);
						mappingRealmsJdbc.put(id, realm.getJdbcRealmId()); // mapping OLD-ID to NEW-ID
					} else {
						try {
							if (log.isDebugEnabled()) log.debug("searching RealmJdbc: " + id);
							realm = getRealmJdbcHbmDao().load(id);
							String realmName = XercesHelper.getNodeValue(elmRealm, "realmName");
							if (realmName != null && realmName.length() > 0) {
								realm.setRealmName(realmName);
							} else {
								realm.setRealmName(null);
							}
							String jndiName = XercesHelper.getNodeValue(elmRealm, "jndiName");
							realm.setJndiName(jndiName);
							String statementUser = XercesHelper.getNodeValue(elmRealm, "statementUser");
							realm.setStatementUser(statementUser);
							String statementRolePerUser = XercesHelper.getNodeValue(elmRealm, "statementRolePerUser");
							realm.setStatementRolePerUser(statementRolePerUser);
							realm.setSite(site);
						} catch (Exception exe) {
							if (log.isDebugEnabled()) log.debug("creating RealmJdbc: " + id);
							realm = getRealmJdbcHbmDao().create(elmRealm, false);
							realm.setSite(site);
						}
					}
					String loginPageId = XercesHelper.getNodeValue(elmRealm, "loginPageId");
					if (loginPageId != null && loginPageId.length() > 0) loginPagesRealmsJdbc.put(id, new Integer(loginPageId));
				}
				itRealms = XercesHelper.findNodes(doc, "/edition/realms/realmsJaas/realmJaas");
				while (itRealms.hasNext()) {
					if (log.isDebugEnabled()) log.debug("Found RealmJaas to import...");
					Element elmRealm = (Element) itRealms.next();
					Integer id = new Integer(XercesHelper.getNodeValue(elmRealm, "jaasRealmId"));
					RealmJaasHbm realm = null;
					if (useNewIDs) {
						realm = getRealmJaasHbmDao().create(elmRealm, true);
						realm.setSite(site);
						mappingRealmsJaas.put(id, realm.getJaasRealmId()); // mapping OLD-ID to NEW-ID
					} else {
						try {
							if (log.isDebugEnabled()) log.debug("searching RealmJaas: " + id);
							realm = getRealmJaasHbmDao().load(id);
							String realmName = XercesHelper.getNodeValue(elmRealm, "realmName");
							if (realmName != null && realmName.length() > 0) {
								realm.setRealmName(realmName);
							} else {
								realm.setRealmName(null);
							}
							String jaasPolicyName = XercesHelper.getNodeValue(elmRealm, "jaasPolicyName");
							realm.setJaasPolicyName(jaasPolicyName);
							realm.setSite(site);
						} catch (Exception exe) {
							if (log.isDebugEnabled()) log.debug("creating RealmJaas: " + id);
							realm = getRealmJaasHbmDao().create(elmRealm, false);
							realm.setSite(site);
						}
					}
					String loginPageId = XercesHelper.getNodeValue(elmRealm, "loginPageId");
					if (loginPageId != null && loginPageId.length() > 0) loginPagesRealmsJaas.put(id, new Integer(loginPageId));
				}
			} else {
				if (log.isDebugEnabled()) log.debug("importRealms reports: There are no /edition/realms nodes, so nothing to do");
			}
		} catch (Exception exe) {
			log.error("Error occured importRealms: " + exe.getMessage(), exe);
		}
	}

	@Override
	protected void handleImportShortLinks(org.w3c.dom.Document doc, Integer siteId, boolean useNewIds) throws Exception {
		try {
			if (log.isInfoEnabled()) log.info("begin importShortLinks");
			Iterator itShortLinks = XercesHelper.findNodes(doc, "/edition/shortLinks/shortLink");
			if (itShortLinks.hasNext()) {
				if (log.isDebugEnabled()) log.debug("Trying to delete ShortLinks");
				// delete I
				getShortLinkHbmDao().findAll(siteId).clear();
				// delete II
				Collection<ShortLinkHbm> shortLinks = new ArrayList<ShortLinkHbm>();
				shortLinks.addAll(getShortLinkHbmDao().findAll(siteId));
				Iterator hi = shortLinks.iterator();
				ShortLinkHbm tempLink;
				while (hi.hasNext()) {
					tempLink = ((ShortLinkHbm) hi.next());
					getShortLinkHbmDao().remove(tempLink);
				}
				if (log.isDebugEnabled()) log.debug("Trying to add new ShortLinks");
				while (itShortLinks.hasNext()) {
					if (log.isDebugEnabled()) log.debug("Found ShortLink to import...");
					Element elShortLink = (Element) itShortLinks.next();
					String shortLinkId = XercesHelper.getNodeValue(elShortLink, "./shortLinkId").trim();
					String shortLinkName = XercesHelper.getNodeValue(elShortLink, "./shortLinkName").trim();
					String redirectUrl = XercesHelper.getNodeValue(elShortLink, "./redirectUrl").trim();
					String viewDocumentId = XercesHelper.getNodeValue(elShortLink, "./viewDocumentId").trim();

					ShortLinkValue shortLinkValue = new ShortLinkValue();
					if (useNewIds) {
						shortLinkValue.setShortLinkId(new Integer(-1));
						shortLinkValue.setViewDocumentId(mappingVDs.get(Integer.valueOf(viewDocumentId)));
					} else {
						shortLinkValue.setShortLinkId(Integer.valueOf(shortLinkId));
						shortLinkValue.setViewDocumentId(Integer.valueOf(viewDocumentId));
					}
					shortLinkValue.setRedirectUrl(redirectUrl);
					shortLinkValue.setShortLink(shortLinkName);
					shortLinkValue.setSiteId(siteId);

					if (log.isDebugEnabled()) log.debug("Importing ShortLink: " + shortLinkId + " shortLinkName \"" + shortLinkName + "\" redirectUrl \"" + redirectUrl + "\" for site " + siteId + " and viewDocument " + shortLinkValue.getViewDocumentId());

					try {
						ShortLinkHbm shortLink = createShortLinkHbm(shortLinkValue);
						getShortLinkHbmDao().setShortLinkValue(shortLinkValue, shortLink);
						//						shortLink.setShortLinkValue(shortLinkValue);
						getShortLinkHbmDao().create(shortLink);
					} catch (Exception e) {
						log.error("Error creating ShortLink: " + e.getMessage(), e);
					}
				}
			}
			if (log.isInfoEnabled()) log.info("end importShortLinks");
		} catch (Exception exe) {
			// context.setRollbackOnly();
			throw new UserException("Error recreating the ShortLinks " + exe.getMessage());
		}
	}

	private ShortLinkHbm createShortLinkHbm(ShortLinkValue shortLinkValue) {
		ShortLinkHbm shortLink = new ShortLinkHbmImpl();
		shortLink.setShortLink(shortLinkValue.getShortLink());
		shortLink.setRedirectUrl(shortLinkValue.getRedirectUrl());
		try {
			if (shortLinkValue.getSiteId() != null) {
				SiteHbm site = getSiteHbmDao().load(shortLinkValue.getSiteId());
				shortLink.setSite(site);
			}
			if (shortLinkValue.getViewDocumentId() != null) {
				ViewDocumentHbm viewDocument = getViewDocumentHbmDao().load(shortLinkValue.getViewDocumentId());
				shortLink.setViewDocument(viewDocument);
			}
		} catch (Exception e) {
			log.error("Error setting relations in setShortLinkValue: " + e.getMessage(), e);
		}
		return shortLink;
	}

	/**
	 * It runs on the workserver only
	 */
	@Override
	protected void handleUpdateDeployStatus(List editions) throws Exception {
		if (editions != null && editions.size() > 0) {
			//group editions by liveserver
			Map<LiveServerCredentials, List<EditionHbm>> editionsByLiveServers = new HashMap<LiveServerCredentials, List<EditionHbm>>();
			for (EditionHbm edition : (List<EditionHbm>) editions) {
				// edition is deployed - no new status expected
				if (edition.getEndActionTimestamp() != null) {
					continue;
				}
				SiteHbm site = getSiteHbmDao().load(edition.getSiteId());
				LiveServerCredentials credentials = new LiveServerCredentials();
				org.w3c.dom.Document doc = null;
				try {
					doc = XercesHelper.string2Dom(site.getConfigXML());
					credentials.liveServerIP = XercesHelper.getNodeValue(doc, "/config/default/liveServer/url");
					credentials.liveServerUserName = XercesHelper.getNodeValue(doc, "/config/default/liveServer/username");
					credentials.liveServerPass = XercesHelper.getNodeValue(doc, "/config/default/liveServer/password");
				} catch (Exception exe) {
					log.error("Error occured reading siteConfig: ", exe);
				} finally {
					doc = null;
				}
				if (!editionsByLiveServers.containsKey(credentials)) {
					List<EditionHbm> editionsOnLiveServer = new ArrayList<EditionHbm>();
					editionsOnLiveServer.add(edition);
					editionsByLiveServers.put(credentials, editionsOnLiveServer);
				} else {
					editionsByLiveServers.get(credentials).add(edition);
				}

			}

			for (Entry<LiveServerCredentials, List<EditionHbm>> editionsByLiveServer : editionsByLiveServers.entrySet()) {
				List<EditionHbm> editionStatusesRequest = editionsByLiveServer.getValue();
				List<EditionValue> editionStatusesResponse = getDeployStatusFromLiveServer(editionsByLiveServer.getValue(), editionsByLiveServer.getKey());
				for (EditionHbm editionRequest : editionStatusesRequest) {
					boolean foundEditionInResponse = false;
					EditionValue editionValueRequest = editionRequest.getDao();
					for (EditionValue editionResponse : editionStatusesResponse) {
						if (editionResponse.getWorkServerEditionId().equals(editionValueRequest.getEditionId())) {
							//update deploy status
							if (editionResponse.getDeployStatus() == null) {
								editionRequest.setDeployStatus(new byte[0]);
							} else {
								editionRequest.setDeployStatus(editionResponse.getDeployStatus().getBytes());
								String message = new String(editionResponse.getDeployStatus().getBytes());
								if (message.compareToIgnoreCase("ImportSuccessful") == 0 && editionRequest.getEndActionTimestamp() == null) {
									createDeployFinishedTask(message, editionRequest);
									setViewComponentsOnline(editionRequest);
								}
								if (message.contains("Exception") && editionRequest.getEndActionTimestamp() == null) {
									createDeployFinishedTask(message, editionRequest);
								}
							}
							editionRequest.setStartActionTimestamp(editionResponse.getStartActionTimestamp() == null ? null : editionResponse.getStartActionTimestamp().getTime());
							editionRequest.setEndActionTimestamp(editionResponse.getEndActionTimestamp() == null ? null : editionResponse.getEndActionTimestamp().getTime());
							getEditionHbmDao().update(editionRequest);
							foundEditionInResponse = true;
							break;
						}
					}

					if (!foundEditionInResponse) {
						//deploy finished
						//getEditionHbmDao().remove(editionRequest.getEditionId());
					}
				}
			}

		}
	}

	private void createDeployFinishedTask(String message, EditionHbm edition) {
		if (log.isDebugEnabled()) log.debug("start createDeployFinishedTask");
		if (log.isDebugEnabled() && message != null) log.debug("parameter message: " + message);
		if (log.isDebugEnabled() && edition != null) log.debug("parameter edition(id): " + edition.getEditionId());
		TaskHbm finish = TaskHbm.Factory.newInstance();
		finish.setComment(message);
		finish.setCreationDate(new Date().getTime());
		finish.setReceiverRole("deploy");
		finish.setSender(edition.getCreator());
		finish.setUnit(getUnitHbmDao().load(edition.getUnitId()));
		finish.setTaskType(Constants.TASK_SYSTEMMESSAGE_INFORMATION);
		if (message.compareToIgnoreCase("ImportSuccessful") == 0) {
			message = "Edition " + edition.getComment() + " (" + edition.getEditionId() + ") has been deployed successfully.";
		} else {
			message = "Edition " + edition.getComment() + " (" + edition.getEditionId() + ") could not be deployed. Please contact your administrator.";
		}
		getTaskHbmDao().create(finish);
		edition.setDeployStatus("Finished".getBytes());
		getEditionHbmDao().update(edition);
		if (log.isDebugEnabled()) log.debug("end createDeployFinishedTask");
	}

	private List<EditionValue> getDeployStatusFromLiveServer(List<EditionHbm> editions, LiveServerCredentials credentials) throws UserException {
		List<EditionValue> editionsStatus = new ArrayList<EditionValue>();
		if (editions != null && editions.size() > 0) {
			try {
				EditionHbm edition = editions.get(0);

				int unitId = edition.getUnitId();
				int viewDocumentId = edition.getViewDocumentId();

				System.setProperty("tizzit-liveserver.remoteServer", "http://" + credentials.liveServerIP);
				ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext-deploy.xml");

				try {
					SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_GLOBAL);
					RemoteAuthenticationManager remoteAuthenticationService = (RemoteAuthenticationManager) ctx.getBean("remoteRemoteAuthenticationManagerServiceDeploy");
					GrantedAuthority[] authorities = remoteAuthenticationService.attemptAuthentication(credentials.liveServerUserName, String.valueOf(credentials.liveServerPass));
					SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(credentials.liveServerUserName, String.valueOf(credentials.liveServerPass), authorities));
					log.debug(SecurityContextHolder.getContext().getAuthentication());
				} catch (SpringSecurityException e) {
					log.info("authentication failed: " + e.getMessage());
					throw new UserException(e.getMessage());
				}

				AuthorizationServiceSpring autoSpring = (AuthorizationServiceSpring) ctx.getBean("authorizationServiceDeploySpring");
				if (log.isInfoEnabled()) log.info("Logging in on Liveserver...");
				autoSpring.remoteLoginLive(credentials.liveServerUserName, credentials.liveServerPass);
				if (log.isInfoEnabled()) log.info("Successfully logged in!");

				ClientServiceSpring clientServiceSpring = (ClientServiceSpring) ctx.getBean("clientServiceDeploySpring");
				editionsStatus = clientServiceSpring.getDeployStatus(editions);

			} catch (Exception exe) {
				if (log.isDebugEnabled()) log.debug("Rolling back because of error on Liveserver");
				//if error connecting to server don't update statuses for requesting edition
				for (EditionHbm edition : editions) {
					edition.setWorkServerEditionId(edition.getEditionId());
					edition.setExceptionMessage(exe.getMessage());
					edition.setEndActionTimestamp(System.currentTimeMillis());
					editionsStatus.add(edition.getDao());
				}
			}
		}
		return editionsStatus;
	}

	@Override
	protected List handleGetDeployStatus(List editions) throws Exception {
		List<EditionValue> editionsResult = new ArrayList<EditionValue>();
		if (editions != null && editions.size() > 0) {
			for (EditionHbm edition : (List<EditionHbm>) editions) {
				EditionHbm liveServerEdition = getEditionHbmDao().findByWorkServerEdition(edition.getEditionId());
				if (liveServerEdition != null) {
					editionsResult.add(liveServerEdition.getDao());
				}
			}
		}
		return editionsResult;
	}

	private class LiveServerCredentials {
		private String liveServerIP;
		private String liveServerUserName;
		private String liveServerPass;

		@Override
		public int hashCode() {
			return (liveServerIP + liveServerUserName + liveServerPass).hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof LiveServerCredentials) {
				LiveServerCredentials credentials = (LiveServerCredentials) obj;
				return liveServerIP.equals(credentials.liveServerIP) && liveServerPass.equals(credentials.liveServerPass) && liveServerUserName.equals(credentials.liveServerUserName);
			}
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.remote.EditionServiceSpringBase#handleImportUnit(java.lang.Integer, java.lang.String)
	 */
	@Override
	protected void handleImportUnit(Integer editionId, String editionFileName) throws Exception {
		// TODO Auto-generated method stub

	}
}
