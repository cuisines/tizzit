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
package de.juwimm.cms.model;

import java.io.InputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

import javax.ejb.CreateException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.tizzit.util.XercesHelper;

import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.remote.helper.AuthenticationHelper;
import de.juwimm.cms.safeguard.model.RealmJaasHbm;
import de.juwimm.cms.safeguard.model.RealmJdbcHbm;
import de.juwimm.cms.safeguard.model.RealmLdapHbm;
import de.juwimm.cms.safeguard.model.RealmSimplePwHbm;
import de.juwimm.cms.util.EditionSliceInputStream;
import de.juwimm.cms.util.EditionSliceOutputStream;

/**
 * @see de.juwimm.cms.model.EditionHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 *         company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class EditionHbmDaoImpl extends EditionHbmDaoBase {
	private static Log log = LogFactory.getLog(EditionHbmDaoImpl.class);
	private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	@Autowired
	private SequenceHbmDao sequenceHbmDao;

	@Override
	protected EditionHbm handleCreate(String comment, Integer rootViewComponentId, PrintStream out, boolean includeUnused) throws Exception {
		EditionHbm newEdition = new EditionHbmImpl();
		// Use XDoclet's GUID generator. Only works for String fields
		// This requires <utilobject includeGUID="true"/> in ejbdoclet.
		newEdition.setStatus((byte) 0);
		newEdition.setCreationDate((System.currentTimeMillis()));
		newEdition.setComment(comment);
		newEdition = postCreate(newEdition, comment, rootViewComponentId, out, includeUnused);
		return super.create(newEdition);
	}

	private EditionHbm postCreate(EditionHbm edition, String comment, Integer rootViewComponentId, PrintStream out, boolean includeUnused) throws CreateException {
		if (log.isDebugEnabled()) log.debug("Postcreating Edition");
		if (rootViewComponentId != null) {
			EditionSliceOutputStream byteOut = null;
			if (out == null) { //if no Stream was submitted, create out own and store in DB at least.
				try {
					byteOut = new EditionSliceOutputStream(edition, null);
					GZIPOutputStream gzOut = new GZIPOutputStream(byteOut);
					out = new PrintStream(gzOut, true, "UTF-8");
				} catch (Exception exe) {
					log.error("Could not create GZIPOutputStream because of: " + exe.getMessage());
					throw new javax.ejb.CreateException(exe.getMessage());
				}
			}
			try {
				UserHbm creator = getUserHbmDao().load(AuthenticationHelper.getUserName());
				edition.setCreator(creator);
			} catch (Exception exe) {
				log.warn("There went something wrong during ejbPostcreate and finding the right user", exe);
			}
			try {
				ViewComponentHbm vc = getViewComponentHbmDao().load(rootViewComponentId);

				Integer unitId = vc.getUnit4ViewComponent();
				Integer siteId = vc.getViewDocument().getSite().getSiteId();

				out.println("<edition>");

				if (log.isDebugEnabled()) log.debug("picturesToXmlRecursive");
				this.picturesToXmlRecursive(unitId, siteId, out, edition);
				System.gc();
				if (log.isDebugEnabled()) log.debug("documentsToXmlRecursive");
				this.documentsToXmlRecursive(unitId, siteId, out, includeUnused, edition);
				System.gc();
				if (vc.isRoot()) { // ROOT Deploy can only be done by a ROOT-User (and this must be automatically invoked!)
					if (log.isDebugEnabled()) log.debug("ROOT Deploy");
					this.hostsToXmlRecursive(siteId, out, edition);
					this.shortLinksToXmlRecursive(siteId, out, edition);
					this.unitsToXmlRecursive(siteId, out, edition);
					this.viewdocumentsToXmlRecursive(siteId, out, edition);
					this.realmsToXmlRecursive(siteId, out, edition);
				} else {
					UnitHbm unit = getUnitHbmDao().load(unitId);
					if (log.isDebugEnabled()) log.debug("Unit Export/Deploy " + unit.getUnitId() + "(" + unit.getName().trim() + ")");
					out.println("\t<units>");
					if (log.isDebugEnabled()) log.debug("unit.toXmlRecursive");
					this.unitsToXmlRecursive(siteId, out, edition);
					//				out.print(unit.toXmlRecursive(2));
					out.println("\t</units>");
					if (log.isDebugEnabled()) log.debug("realmsToXmlUsed");
					this.realmsToXmlUsed(unitId, out, edition);
				}
				System.gc();
				if (log.isDebugEnabled()) log.debug("Creating ViewComponent Data");
				this.viewdocumentsToXmlRecursive(siteId, out, edition);
				//			vc.toXml(vc.getUnit4ViewComponent(), true, false, 0, 0, false, false, out);
				if (log.isDebugEnabled()) log.debug("Finished creating ViewComponent Data");
				out.println("</edition>");

				edition.setUnitId(vc.getAssignedUnit().getUnitId().intValue());
				edition.setViewDocumentId(vc.getViewDocument().getViewDocumentId().intValue());

				out.flush();
				out.close();
				out = null;
				String siteConfig = vc.getViewDocument().getSite().getConfigXML();
				org.w3c.dom.Document doc = XercesHelper.string2Dom(siteConfig);
				String isEditionLimited = XercesHelper.getNodeValue(doc, "/config/default/parameters/maxEditionStack_1");
				if (isEditionLimited != null && !"".equalsIgnoreCase(isEditionLimited) && Boolean.valueOf(isEditionLimited).booleanValue()) {
					String maxEditionStack = XercesHelper.getNodeValue(doc, "/config/default/parameters/maxEditionStack_2");
					if (maxEditionStack != null && !"".equalsIgnoreCase(maxEditionStack)) {
						int max = Integer.valueOf(maxEditionStack);
						// max must be > 0, otherwise the created edition would be deleted before the deploy
						if (max > 0) {
							if (log.isDebugEnabled()) log.debug("Site: " + siteId + " maxEditionStack: " + max);
							Collection editions = findByUnitAndViewDocument(vc.getAssignedUnit().getUnitId(), vc.getViewDocument().getViewDocumentId());
							while (editions.size() > max) {
								// get oldest edition
								EditionHbm oldestEdition = null;
								Iterator edIt = editions.iterator();
								while (edIt.hasNext()) {
									EditionHbm currentEdition = (EditionHbm) edIt.next();
									if ((oldestEdition == null) || (currentEdition.getCreationDate() < oldestEdition.getCreationDate())) {
										oldestEdition = currentEdition;
									}
								}
								if (oldestEdition != null) {
									// delete oldest one
									Date oldestCreateDate = new Date(oldestEdition.getCreationDate());
									if (log.isDebugEnabled()) log.debug("Deleting edition " + oldestEdition.getEditionId() + " of unit \"" + vc.getAssignedUnit().getName().trim() + "\" (" + unitId + ") from " + sdf.format(oldestCreateDate) + " for language \"" + vc.getViewDocument().getLanguage().trim() + "\"");
									this.remove(oldestEdition);
								}
								editions = this.findByUnitAndViewDocument(vc.getAssignedUnit().getUnitId(), vc.getViewDocument().getViewDocumentId());
							}
						}
					}
				}
			} catch (Exception exe) {
				log.error("Error occured", exe);
			}
		}
		log.debug("Finished Postcreating Edition");
		return edition;
	}

	/*
	 * POSTCREATES!!!!
	 * 
	 * 
	 * public void ejbPostCreate(String comment, Integer rootViewComponentId,
	 * PrintStream out, boolean includeUnused) throws CreateException { if
	 * (log.isDebugEnabled()) log.debug("Postcreating Edition");
	 * if (rootViewComponentId != null) { EditionSliceOutputStream byteOut =
	 * null; if (out == null) { //if no Stream was submitted, create out own and
	 * store in DB at least. try { byteOut = new
	 * EditionSliceOutputStream((Edition) ctx.getEJBLocalObject());
	 * GZIPOutputStream gzOut = new GZIPOutputStream(byteOut); out = new
	 * PrintStream(gzOut, true, "UTF-8"); } catch (Exception exe) {
	 * log.error("Could not create GZIPOutputStream because of: " +
	 * exe.getMessage()); throw new
	 * javax.ejb.CreateException(exe.getMessage()); } } try { User creator =
	 * getUserLocalHome().findByPrimaryKey(ctx.getCallerPrincipal().getName());
	 * setCreator(creator); } catch (Exception exe) { log.warn("There went
	 * something wrong during ejbPostcreate and finding the right user", exe);
	 * } try { ViewComponent vc =
	 * getViewComponentLocalHome().findByPrimaryKey(rootViewComponentId);
	 * 
	 * Integer unitId = vc.getUnit4ViewComponent(); Integer siteId =
	 * vc.getViewDocument().getSite().getSiteId();
	 * 
	 * out.println("<edition>");
	 * 
	 * if (log.isDebugEnabled()) log.debug("picturesToXmlRecursive");
	 * this.picturesToXmlRecursive(unitId, siteId, out);
	 * System.gc(); if (log.isDebugEnabled())
	 * log.debug("documentsToXmlRecursive");
	 * this.documentsToXmlRecursive(unitId, siteId, out, includeUnused);
	 * System.gc(); if (vc.isRoot()) { // ROOT Deploy can only be done by a
	 * ROOT-User (and this must be automatically invoked!) if
	 * (log.isDebugEnabled()) log.debug("ROOT Deploy");
	 * this.hostsToXmlRecursive(siteId, out);
	 * this.shortLinksToXmlRecursive(siteId, out);
	 * this.unitsToXmlRecursive(siteId, out);
	 * this.viewdocumentsToXmlRecursive(siteId, out);
	 * this.realmsToXmlRecursive(siteId, out); } else { Unit unit =
	 * getUnitLocalHome().findByPrimaryKey(unitId); if (log.isDebugEnabled())
	 * log.debug("Unit Export/Deploy " + unit.getUnitId() + "(" +
	 * unit.getName().trim() + ")");
	 * out.println("\t<units>"); if (log.isDebugEnabled())
	 * log.debug("unit.toXmlRecursive");
	 * out.print(unit.toXmlRecursive(2)); out.println("\t</units>");
	 * if (log.isDebugEnabled()) log.debug("realmsToXmlUsed");
	 * this.realmsToXmlUsed(unitId, out); } System.gc(); if
	 * (log.isDebugEnabled()) log.debug("Creating ViewComponent Data");
	 * vc.toXml(vc.getUnit4ViewComponent(), true, false, 0, 0,
	 * false, false, out); if (log.isDebugEnabled()) log.debug("Finished
	 * creating ViewComponent Data"); out.println("</edition>");
	 *
	 * 
	 * setUnitId(vc.getAssignedUnit().getUnitId().intValue());
	 * setViewDocumentId(vc.getViewDocument().getViewDocumentId().intValue());
	 * 
	 * out.flush(); out.close(); out = null; String siteConfig =
	 * vc.getViewDocument().getSite().getConfigXML(); org.w3c.dom.Document doc =
	 * XercesHelper.string2Dom(siteConfig); String isEditionLimited =
	 * XercesHelper .getNodeValue(doc,
	 * "/config/default/parameters/maxEditionStack_1"); if
	 * (isEditionLimited != null && !"".equalsIgnoreCase(isEditionLimited)
	 * && Boolean.valueOf(isEditionLimited).booleanValue()) {
	 * String maxEditionStack = XercesHelper.getNodeValue(doc,
	 * "/config/default/parameters/maxEditionStack_2"); if
	 * (maxEditionStack != null && !"".equalsIgnoreCase(maxEditionStack)) {
	 * int max = Integer.valueOf(maxEditionStack); // max must be >
	 * 0, otherwise the created edition would be deleted before the deploy if
	 * (max > 0) { if (log.isDebugEnabled()) log.debug("Site: " + siteId + "
	 * maxEditionStack: " + max); Collection
	 * editions = ((EditionLocalHome) this.ctx.getEJBLocalHome())
	 * .findByUnitAndViewDocument(vc.getAssignedUnit().getUnitId(),
	 * vc.getViewDocument() .getViewDocumentId()); while (editions.size() > max) { //
	 * get oldest edition Edition oldestEdition = null; Iterator edIt =
	 * editions.iterator(); while (edIt.hasNext()) { Edition currentEdition =
	 * (Edition) edIt.next(); if ((oldestEdition == null) ||
	 * (currentEdition.getCreationDate() < oldestEdition.getCreationDate())) {
	 * oldestEdition = currentEdition; } } if (oldestEdition != null) { //
	 * delete oldest one Date oldestCreateDate = new
	 * Date(oldestEdition.getCreationDate()); if (log.isDebugEnabled())
	 * log.debug("Deleting edition " + oldestEdition.getEditionId() + " of unit
	 * \"" + vc.getAssignedUnit().getName().trim() +
	 * "\" (" + unitId + ") from " +
	 * sdf.format(oldestCreateDate) + " for language \"" +
	 * vc.getViewDocument().getLanguage().trim() + "\"");
	 * oldestEdition.remove(); } editions = ((EditionLocalHome)
	 * this.ctx.getEJBLocalHome()).findByUnitAndViewDocument(vc
	 * .getAssignedUnit().getUnitId(),
	 * vc.getViewDocument().getViewDocumentId()); } } } } } catch (Exception
	 * exe) { log.error("Error occured", exe); } }
	 * log.debug("Finished Postcreating Edition"); }
	 * 
	 * private void getUsedRealmsForUnitAndViewDocument(Integer unitId, Integer
	 * viewDocumentId, HashMap ldapMap, HashMap jdbcMap, HashMap simplePwMap,
	 * HashMap jaasMap) throws Exception { ViewComponent viewRoot =
	 * getViewComponentLocalHome().find4Unit(unitId, viewDocumentId);
	 * this.getRealmsForViewComponent(viewRoot, ldapMap, jdbcMap, simplePwMap,
	 * jaasMap); }
	 * 
	 * private void getRealmsForViewComponent(ViewComponent viewComponent,
	 * HashMap ldapMap, HashMap jdbcMap, HashMap simplePwMap, HashMap jaasMap)
	 * throws Exception { Realm2viewComponent realm =
	 * viewComponent.getRealm2vc(); if (realm != null) { if
	 * (realm.getLdapRealm() != null && realm.getLdapRealm().getLdapRealmId() !=
	 * null) { if(log.isDebugEnabled()) log.debug("Adding Ldap Realm " +
	 * realm.getLdapRealm().getLdapRealmId() + " used by VC " +
	 * viewComponent.getViewComponentId());
	 * ldapMap.put(realm.getLdapRealm().getLdapRealmId(),
	 * realm.getLdapRealm().getLdapRealmId()); } else if
	 * (realm.getSimplePwRealm() != null &&
	 * realm.getSimplePwRealm().getSimplePwRealmId() != null) {
	 * if(log.isDebugEnabled()) log.debug("Adding SimplePwRealm Realm " +
	 * realm.getSimplePwRealm().getSimplePwRealmId() + " used by VC " +
	 * viewComponent.getViewComponentId());
	 * simplePwMap.put(realm.getSimplePwRealm().getSimplePwRealmId(),
	 * realm.getSimplePwRealm().getSimplePwRealmId()); } else if
	 * (realm.getJdbcRealm() != null && realm.getJdbcRealm().getJdbcRealmId() !=
	 * null) { if(log.isDebugEnabled()) log.debug("Adding Jdbc Realm " +
	 * realm.getJdbcRealm().getJdbcRealmId() + " used by VC " +
	 * viewComponent.getViewComponentId());
	 * ldapMap.put(realm.getJdbcRealm().getJdbcRealmId(),
	 * realm.getJdbcRealm().getJdbcRealmId()); } else if (realm.getJaasRealm() !=
	 * null && realm.getJaasRealm().getJaasRealmId() != null) {
	 * if(log.isDebugEnabled()) log.debug("Adding Jaas Realm " +
	 * realm.getJaasRealm().getJaasRealmId() + " used by VC " +
	 * viewComponent.getViewComponentId());
	 * jaasMap.put(realm.getJaasRealm().getJaasRealmId(),
	 * realm.getJaasRealm().getJaasRealmId()); } else { log.warn("There is a
	 * Realm protecting ViewComponent " + viewComponent.getViewComponentId() + "
	 * but I can't identify it!"); } } Collection
	 * children = viewComponent.getChildren(); if (children != null) { Iterator
	 * it = children.iterator(); while (it.hasNext()) { ViewComponent child =
	 * (ViewComponent) it.next(); if (!child.isUnit()) {
	 * this.getRealmsForViewComponent(child, ldapMap, jdbcMap, simplePwMap,
	 * jaasMap); } } } }
	 * 
	 */

	@Override
	public EditionHbm create(EditionHbm editionHbm) {
		if (editionHbm.getEditionId() == null || editionHbm.getEditionId().intValue() == 0) {
			try {
				Integer id = sequenceHbmDao.getNextSequenceNumber("edition.edition_id");
				editionHbm.setEditionId(id);
			} catch (Exception e) {
				log.error("Error creating primary key", e);
			}
		}
		//	editionHbm = this.postCreate(editionHbm);
		return super.create(editionHbm);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleDocumentsToXmlRecursive(Integer unitId, Integer siteId, PrintStream out, boolean includeUnused, EditionHbm edition) throws Exception {
		// TODO: if searchIndex is reliable only linked documents should be included in edition
		includeUnused = true;
		out.println("\t<documents>");
		try {
			if (unitId == null) {
				Collection<UnitHbm> units = getUnitHbmDao().findAll(siteId);
				for (UnitHbm unit : units) {
					Collection<DocumentHbm> docs = getDocumentHbmDao().findAllPerUnit(unit.getUnitId());
					for (DocumentHbm doc : docs) {
						if (!includeUnused && doc.getUseCountPublishVersion() == 0) continue;
						out.print(getDocumentHbmDao().toXml(doc.getDocumentId(), 2));
					}
				}
			} else {
				Collection<DocumentHbm> docs = getDocumentHbmDao().findAllPerUnit(unitId);
				for (DocumentHbm doc : docs) {
					if (!includeUnused && doc.getUseCountPublishVersion() == 0) continue;
					out.print(getDocumentHbmDao().toXml(doc.getDocumentId(), 2));
				}
			}
		} catch (Exception exe) {
			log.error("Error occured", exe);
		}
		out.println("\t</documents>");
	}

	@Override
	protected InputStream handleGetEditionDataRaw(EditionHbm edition) throws Exception {
		return new EditionSliceInputStream(edition.getEditionId(), null);
	}

	@Override
	protected void handleHostsToXmlRecursive(Integer siteId, PrintStream out, EditionHbm edition) throws Exception {
		out.println("\t<hosts>");
		try {
			Collection hostList = getHostHbmDao().findAll(siteId);
			Iterator it = hostList.iterator();
			while (it.hasNext()) {
				HostHbm host = (HostHbm) it.next();
				out.print(host.toXml(2));
			}
		} catch (Exception exe) {
			log.error("Error occured", exe);
		}
		out.println("\t</hosts>");

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handlePicturesToXmlRecursive(Integer unitId, Integer siteId, PrintStream out, EditionHbm edition) throws Exception {
		out.println("\t<pictures>");
		try {
			if (unitId == null) {
				Collection<UnitHbm> units = getUnitHbmDao().findAll(siteId);
				for (UnitHbm unit : units) {
					Collection<PictureHbm> pictures = getPictureHbmDao().findAllPerUnit(unit.getUnitId());
					for (PictureHbm picture : pictures) {
						out.print(picture.toXml(2));
					}
				}
			} else {
				Collection<PictureHbm> pictures = getPictureHbmDao().findAllPerUnit(unitId);
				for (PictureHbm pic : pictures) {
					out.print(pic.toXml(2));
				}
			}
		} catch (Exception exe) {
			log.error("Error occured", exe);
		}
		out.println("\t</pictures>");
	}

	@Override
	protected void handleRealmsToXmlRecursive(Integer siteId, PrintStream out, EditionHbm edition) throws Exception {
		out.println("<realms>");
		try {
			SiteHbm site = getSiteHbmDao().load(siteId);
			{
				Collection simplepwrealms = site.getRealmSimplePwHbms();
				Iterator it = simplepwrealms.iterator();
				out.println("<realmsSimplePw>");
				while (it.hasNext()) {
					RealmSimplePwHbm realm = (RealmSimplePwHbm) it.next();
					out.println(realm.toXml());
				}
				out.println("</realmsSimplePw>");
			}
			{
				Collection sqldbrealms = site.getRealmJdbcHbms();
				Iterator it = sqldbrealms.iterator();
				out.println("<realmsJdbc>");
				while (it.hasNext()) {
					RealmJdbcHbm realm = (RealmJdbcHbm) it.next();
					out.println(realm.toXml());
				}
				out.println("</realmsJdbc>");
			}
			{
				Collection realmsLdap = site.getRealmLdapHbms();
				Iterator it = realmsLdap.iterator();
				out.println("<realmsLdap>");
				while (it.hasNext()) {
					RealmLdapHbm realm = (RealmLdapHbm) it.next();
					out.println(realm.toXml());
				}
				out.println("</realmsLdap>");
			}
			{
				Collection realmsJaas = site.getRealmJaasHbms();
				Iterator it = realmsJaas.iterator();
				out.println("<realmsJaas>");
				while (it.hasNext()) {
					RealmJaasHbm realm = (RealmJaasHbm) it.next();
					out.println(realm.toXml());
				}
				out.println("</realmsJaas>");
			}
		} catch (Exception exe) {
			log.error("Error occured", exe);
		}
		out.println("</realms>");

	}

	@Override
	protected void handleRealmsToXmlUsed(Integer unitId, PrintStream out, EditionHbm edition) throws Exception {
		HashMap ldapMap = new HashMap();
		HashMap jdbcMap = new HashMap();
		HashMap simplePwMap = new HashMap();
		HashMap jaasMap = new HashMap();

		try {
			UnitHbm unit = getUnitHbmDao().load(unitId);
			Collection viewDocumentsList = getViewDocumentHbmDao().findAll(unit.getSite().getSiteId());
			Iterator it = viewDocumentsList.iterator();
			while (it.hasNext()) {
				ViewDocumentHbm viewDocument = (ViewDocumentHbm) it.next();
				try {
					// TODO - Methode gibt es nicht mehr??
					// this.getUsedRealmsForUnitAndViewDocument(unitId,
					// viewDocument.getViewDocumentId(), ldapMap, jdbcMap,
					// simplePwMap, jaasMap);
				} catch (Exception ex) {
					log.error("Error getting Realms in use for Unit " + unitId + " and ViewDocument " + viewDocument.getLanguage() + ": " + ex.getMessage());
				}
			}

			out.println("\t<realms>");
			{
				out.println("\t<realmsLdap>");
				Set keyset = ldapMap.keySet();
				Iterator keyit = keyset.iterator();
				while (keyit.hasNext()) {
					Integer key = (Integer) keyit.next();
					if (log.isDebugEnabled()) log.debug("realmsLdap add " + key);
					RealmLdapHbm ldapRealm = getRealmLdapHbmDao().load(key);
					String content = ldapRealm.toXml();
					out.println(content);
				}

				out.println("\t</realmsLdap>");
			}
			{
				out.println("\t<realmsJdbc>");
				Set keyset = jdbcMap.keySet();
				Iterator keyit = keyset.iterator();
				while (keyit.hasNext()) {
					Integer key = (Integer) keyit.next();
					if (log.isDebugEnabled()) log.debug("realmsJdbc add " + key);
					RealmJdbcHbm sqlRealm = getRealmJdbcHbmDao().load(key);
					String content = sqlRealm.toXml();
					out.println(content);
				}
				out.println("\t</realmsJdbc>");
			}
			{
				out.println("\t<realmsSimplePw>");

				Set keyset = simplePwMap.keySet();
				Iterator keyit = keyset.iterator();
				while (keyit.hasNext()) {
					Integer key = (Integer) keyit.next();
					if (log.isDebugEnabled()) log.debug("realmsSimplePw add " + key);
					RealmSimplePwHbm simpleRealm = getRealmSimplePwHbmDao().load(key);
					String content = simpleRealm.toXml();
					out.println(content);
				}
				out.println("\t</realmsSimplePw>");
			}
			{
				out.println("\t<realmsJaas>");

				Set keyset = jaasMap.keySet();
				Iterator keyit = keyset.iterator();
				while (keyit.hasNext()) {
					Integer key = (Integer) keyit.next();
					if (log.isDebugEnabled()) log.debug("realmsJaas add " + key);
					RealmJaasHbm jaasRealm = getRealmJaasHbmDao().load(key);
					String content = jaasRealm.toXml();
					out.println(content);
				}
				out.println("\t</realmsJaas>");
			}

			out.println("\t</realms>");
		} catch (Exception e) {
			log.error("Error getting all used Realms: " + e.getMessage(), e);
		}

	}

	@Override
	protected void handleShortLinksToXmlRecursive(Integer siteId, PrintStream out, EditionHbm edition) throws Exception {
		out.println("\t<shortLinks>");
		try {
			Collection shortLinkList = getShortLinkHbmDao().findAll(siteId);
			Iterator it = shortLinkList.iterator();
			while (it.hasNext()) {
				ShortLinkHbm shortLink = (ShortLinkHbm) it.next();
				out.print(shortLink.toXml(2));
			}
		} catch (Exception exe) {
			log.error("Error occured", exe);
		}
		out.println("\t</shortLinks>");

	}

	@Override
	protected void handleUnitsToXmlRecursive(Integer siteId, PrintStream out, EditionHbm edition) throws Exception {
		out.println("\t<units>");
		try {
			Collection units = getUnitHbmDao().findAll(siteId);
			Iterator it = units.iterator();
			while (it.hasNext()) {
				UnitHbm unit = (UnitHbm) it.next();
				out.print(getUnitHbmDao().toXmlRecursive(2, unit));
			}
		} catch (Exception exe) {
			log.error("Error occured", exe);
		}
		out.println("\t</units>");

	}

	@Override
	protected void handleViewdocumentsToXmlRecursive(Integer siteId, PrintStream out, EditionHbm edition) throws Exception {
		out.println("\t<viewDocuments>");
		try {
			Collection vdocs = getViewDocumentHbmDao().findAll(siteId);
			Iterator it = vdocs.iterator();
			while (it.hasNext()) {
				ViewDocumentHbm vdoc = (ViewDocumentHbm) it.next();
				out.print(vdoc.toXml(2));
			}
		} catch (Exception exe) {
			log.error("Error occured", exe);
		}
		out.println("\t</viewDocuments>");
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findByUnitAndViewDocument(final int transform, final java.lang.Integer unitId, final java.lang.Integer viewdocumentId) {
		return this.findByUnitAndViewDocument(transform, "from de.juwimm.cms.model.EditionHbm as e where e.unitId = ? and e.viewDocumentId = ?", unitId, viewdocumentId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findByUnitAndOnline(final int transform, final java.lang.Integer unitId) {
		return this.findByUnitAndOnline(transform, "from de.juwimm.cms.model.EditionHbm as e where e.unitId = ? and e.status = 1", unitId);
	}

	@Override
	protected EditionHbm handleCreate(String creator, String comment, Integer viewComponentId, Integer unitId, Integer viewDocumentId, Integer siteId, boolean needsDeploy) throws Exception {
		EditionHbm newEdition = new EditionHbmImpl();

		newEdition.setStatus((byte) 0);
		newEdition.setCreationDate((System.currentTimeMillis()));
		newEdition.setCreator(getUserHbmDao().load(creator));
		newEdition.setViewComponentId(viewComponentId);
		newEdition.setViewDocumentId(viewDocumentId);
		newEdition.setUnitId(unitId);
		newEdition.setSiteId(siteId);
		newEdition.setEditionFileName(null);
		newEdition.setNeedsDeploy(needsDeploy);
		newEdition.setComment(comment);
		return super.create(newEdition);
	}

	@Override
	protected EditionHbm handleFindByWorkServerEdition(Integer workServerEditionId) throws Exception {
		Query query = getSession().createQuery("from de.juwimm.cms.model.EditionHbm where workServerEditionId = :workServerEditionId");
		query.setParameter("workServerEditionId", workServerEditionId);
		return (EditionHbm) query.uniqueResult();
	}

	@Override
	protected List handleFindDeployed() throws Exception {
		Query query = getSession().createQuery("from de.juwimm.cms.model.EditionHbm where needsDeploy = false and needsImport = false");
		return query.list();
	}
}
