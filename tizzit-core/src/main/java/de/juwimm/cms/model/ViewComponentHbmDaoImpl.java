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
package de.juwimm.cms.model;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.tizzit.util.DateConverter;

import de.juwimm.cms.beans.foreign.TizzitPropertiesBeanSpring;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.remote.helper.AuthenticationHelper;
import de.juwimm.cms.safeguard.model.Realm2viewComponentHbm;
import de.juwimm.cms.search.beans.SearchengineDeleteService;
import de.juwimm.cms.vo.ContentValue;
import de.juwimm.cms.vo.ViewDocumentValue;

/**
 * @see de.juwimm.cms.model.ViewComponentHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 *         company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class ViewComponentHbmDaoImpl extends ViewComponentHbmDaoBase {
	private static Logger log = Logger.getLogger(ViewComponentHbmDaoImpl.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
	private TizzitPropertiesBeanSpring tizzitPropertiesBeanSpring;

	@Autowired
	private SearchengineDeleteService searchengineDeleteService;

	@Autowired
	private SequenceHbmDao sequenceHbmDao;

	public TizzitPropertiesBeanSpring getTizzitPropertiesBeanSpring() {
		return tizzitPropertiesBeanSpring;
	}

	@Autowired
	public void setTizzitPropertiesBeanSpring(TizzitPropertiesBeanSpring tizzitPropertiesBeanSpring) {
		this.tizzitPropertiesBeanSpring = tizzitPropertiesBeanSpring;
	}

	@Override
	protected boolean handleShouldBeVisible(ViewComponentHbm current, boolean isLiveserver) {
		//		if (isLiveserver && current.getOnline() != 1) return false;
		boolean retVal = current.isVisible();

		if (retVal && (current.getViewType() == Constants.VIEW_TYPE_CONTENT || current.getViewType() == Constants.VIEW_TYPE_UNIT) && isLiveserver && !hasPublishContentVersion(current)) {
			retVal = false;
		}
		if (retVal && (current.getOnlineStart() > 0 && new Date(current.getOnlineStart()).after(new Date(System.currentTimeMillis())))) retVal = false;

		if (retVal && (current.getOnlineStop() > 0 && new Date(current.getOnlineStop()).before(new Date(System.currentTimeMillis())))) retVal = false;

		return retVal;
	}

	@Override
	/**
	 * @param current
	 * @param onlyThisUnitId
	 * @param withContent
	 * @param depth max level of recursion, 0 for this node only, -1 for infinity
	 */
	protected void handleToXml(ViewComponentHbm current, Integer onlyThisUnitId, boolean withContent, boolean withUrl, int depth, boolean liveServer, boolean returnOnlyVisibleOne, PrintStream out) {
		if (log.isDebugEnabled()) log.debug("toXml " + withContent + " WITH URL " + withUrl);

		out.print("<viewcomponent id=\"");
		out.print(current.getViewComponentId());
		out.print("\" unitId=\"");
		out.print(current.getUnit4ViewComponent());

		if (withUrl) {
			out.print("\" hasChild=\"");
			out.print(hasVisibleChild(current, liveServer));
		}
		// This is only needed for the FIRST VC in the Edition.
		// If there is no existent VC like this (initial deploy), we will take
		// this settings.
		if (current.getPrevNode() != null) {
			out.print("\" prev=\"");
			out.print(current.getPrevNode().getViewComponentId());
		}
		if (current.getNextNode() != null) {
			out.print("\" next=\"");
			out.print(current.getNextNode().getViewComponentId());
		}
		if (current.getParent() != null) {
			out.print("\" parent=\"");
			out.print(current.getParent().getViewComponentId());
		}

		out.print("\">\n");
		ViewDocumentValue viewDocumentValue = null;
		try {
			viewDocumentValue = current.getViewDocument().getDao();
		} catch (Exception e) {
		}
		out.print("<showType>" + current.getShowType() + "</showType>\n");
		out.print("<viewType>" + current.getViewType() + "</viewType>\n");
		out.print("<visible>" + current.isVisible() + "</visible>\n");
		out.print("<searchIndexed>" + current.isSearchIndexed() + "</searchIndexed>\n");
		out.print("<statusInfo><![CDATA[" + current.getLinkDescription() + "]]></statusInfo>\n");
		if (liveServer) {
			byte viewType = current.getViewType();
			if (viewType == Constants.VIEW_TYPE_EXTERNAL_LINK || viewType == Constants.VIEW_TYPE_INTERNAL_LINK || viewType == Constants.VIEW_TYPE_SYMLINK) {
				if (current.getStatus() != Constants.DEPLOY_STATUS_APPROVED) {
					if (current.getApprovedLinkName() != null && !"null".equalsIgnoreCase(current.getApprovedLinkName())) {
						out.print("<linkName><![CDATA[" + current.getApprovedLinkName() + "]]></linkName>\n");
					} else {
						out.print("<linkName><![CDATA[" + current.getDisplayLinkName() + "]]></linkName>\n");
					}
				} else {
					out.print("<linkName><![CDATA[" + current.getDisplayLinkName() + "]]></linkName>\n");
				}
			} else {
				out.print("<linkName><![CDATA[" + current.getDisplayLinkName() + "]]></linkName>\n");
			}
		} else {
			out.print("<linkName><![CDATA[" + current.getDisplayLinkName() + "]]></linkName>\n");
		}
		out.print("<urlLinkName><![CDATA[" + current.getUrlLinkName() + "]]></urlLinkName>\n");
		out.print("<viewLevel>" + current.getViewLevel() + "</viewLevel>\n");
		out.print("<viewIndex>" + current.getViewIndex() + "</viewIndex>\n");
		out.print("<displaySettings>" + current.getDisplaySettings() + "</displaySettings>\n");
		out.print("<viewDocumentViewType>" + (viewDocumentValue != null ? viewDocumentValue.getViewType() : "browser") + "</viewDocumentViewType>\n");
		out.print("<language>" + (viewDocumentValue != null ? viewDocumentValue.getLanguage() : "deutsch") + "</language>\n");
		out.print("<userModifiedDate>" + current.getUserLastModifiedDate() + "</userModifiedDate>\n");

		// this is for the edition
		if (withContent) {
			out.print("<status>" + current.getStatus() + "</status>\n");
			out.print("<onlineStart>" + current.getOnlineStart() + "</onlineStart>\n");
			out.print("<onlineStop>" + current.getOnlineStop() + "</onlineStop>\n");
			out.print("<online>" + current.getOnline() + "</online>\n");
			out.print("<reference><![CDATA[" + current.getReference() + "]]></reference>\n");
			out.print("<metaKeywords><![CDATA[" + current.getMetaData() + "]]></metaKeywords>\n");
			out.print("<metaDescription><![CDATA[" + current.getMetaDescription() + "]]></metaDescription>\n");
			try {
				out.print("<modifiedDate>" + DateConverter.getSql2String(new Date(current.getLastModifiedDate())) + "</modifiedDate>\n");
				out.print("<createDate>" + DateConverter.getSql2String(new Date(current.getCreateDate())) + "</createDate>\n");
				if (current.getViewType() == Constants.VIEW_TYPE_CONTENT || current.getViewType() == Constants.VIEW_TYPE_UNIT) {
					if (log.isDebugEnabled()) log.debug("GETTING CONTENT");
					ContentHbm cl = getContentHbmDao().load(new Integer(current.getReference()));
					out.print(getContentHbmDao().toXml(cl));
				}
			} catch (Exception exe) {
				log.warn("Error occured ViewComponentHbmImpl to XML " + exe.getMessage());
			}
			/* Safeguard RealmAtVC */
			/*
			 * try { RealmAtVC realmatvc = current.getRealmAtVC(); if (realmatvc !=
			 * null) { out.print("" + realmatvc.toXml() + "\n"); } } catch
			 * (Exception ine) { log.error("CANNOT APPEND REALM AT VC " +
			 * ine.getMessage()); }
			 */
		}
		// this is for navigation, f.e.
		if (withUrl) {
			if (current.getViewType() == Constants.VIEW_TYPE_EXTERNAL_LINK) {
				out.print("<extUrl><![CDATA[" + current.getReference() + "]]></extUrl>\n");
			} else if (current.getViewType() == Constants.VIEW_TYPE_SEPARATOR) {
				out.print("<separator><![CDATA[" + current.getReference() + "]]></separator>\n");
			} else if (current.getViewType() == Constants.VIEW_TYPE_INTERNAL_LINK) {
				try {
					ViewComponentHbm vclJump = (ViewComponentHbm) getSessionFactory().getCurrentSession().load(ViewComponentHbmImpl.class, new Integer(current.getReference()));
					out.print("<url");
					if (current.getMetaData() != null && !current.getMetaData().equals("")) {
						out.print(" anchor=\"" + current.getMetaData() + "\"><![CDATA[" + vclJump.getPath() + "]]></url>\n");
					} else {
						out.print("><![CDATA[" + vclJump.getPath() + "]]></url>\n");
					}
				} catch (Exception exe) {
					out.print("/>\n");
					log.warn("Error getting path for referenced viewComponent " + current.getReference() + " by internalLink " + current.getViewComponentId() + ": " + exe.getMessage());
				}
			} else {
				out.print("<url><![CDATA[" + current.getPath() + "]]></url>\n");
				try {
					if (current.getViewType() == Constants.VIEW_TYPE_SYMLINK) {
						try {
							ViewComponentHbm vclSym = (ViewComponentHbm) getSessionFactory().getCurrentSession().load(ViewComponentHbmImpl.class, new Integer(current.getReference()));
							String reference = vclSym.getReference();
							ContentHbm content = (ContentHbm) getSessionFactory().getCurrentSession().load(ContentHbmImpl.class, new Integer(reference));
							out.print("<template>" + content.getTemplate() + "</template>\n");
						} catch (Exception symEx) {
							log.warn("ViewComponent " + current.getViewComponentId() + " is a SymLink, maybe the LinkTarget " + current.getReference() + " does not exist (anymore)? -> " + symEx.getMessage());
						}
					} else {
						String reference = current.getReference();
						ContentHbm content = (ContentHbm) getSessionFactory().getCurrentSession().load(ContentHbmImpl.class, new Integer(reference));
						out.print("<template>" + content.getTemplate() + "</template>\n");
						// out.print("<template>" +
						// getContentLocalHome().findByPrimaryKey(new
						// Integer(getReference())).getTemplate() +
						// "</template>\n");
					}
				} catch (Exception exe) {
					log.warn("Error getting url or template for viewComponent " + current.getViewComponentId() + ": " + exe.getMessage());
				}
			}
		}
		if (depth != 0) { // 1 is only THIS ViewComponent
			try {
				Collection coll = current.getChildrenOrdered();
				Iterator it = coll.iterator();
				while (it.hasNext()) {
					ViewComponentHbm vcl = (ViewComponentHbm) it.next();
					if (onlyThisUnitId == null || onlyThisUnitId.equals(vcl.getUnit4ViewComponent())) {
						if (!returnOnlyVisibleOne || this.shouldBeVisible(vcl, liveServer)) {
							int destDepth = depth - 1;
							if (depth == -1) destDepth = -1;
							this.toXml(vcl, onlyThisUnitId, withContent, withUrl, destDepth, liveServer, returnOnlyVisibleOne, out);
						}
					} else {
						// This is outside the specified unit. Therefor append
						// this shitty VC and then leave.
						this.toXml(vcl, onlyThisUnitId, false, withUrl, 1, liveServer, returnOnlyVisibleOne, out);
					}
				}
			} catch (Exception exe) {
				log.error("Error occured calling children.toXml: " + exe.getMessage(), exe);
			}
		}
		out.println("</viewcomponent>");
		if (log.isDebugEnabled()) log.debug("toXml end");
	}

	/**
	 * 
	 * @param liveServer
	 * @return
	 */
	private boolean hasVisibleChild(ViewComponentHbm me, boolean liveServer) {
		if (log.isDebugEnabled()) log.debug("hasVisibleChild start");
		boolean result = false;
		try {
			Iterator it = me.getChildren().iterator();
			while (it.hasNext()) {
				ViewComponentHbm current = (ViewComponentHbm) it.next();
				result = this.shouldBeVisible(current, liveServer);
				if (result) {
					break;
				}
			}
		} catch (Exception exe) {
			log.error("hasVisibleChild for VCID " + me.getViewComponentId() + " an unknown error occured: " + exe.getMessage(), exe);
		}
		if (log.isDebugEnabled()) log.debug("hasVisibleChild end");
		return result;
	}

	@Override
	protected long handleGetPageModifiedDate(ViewComponentHbm me) {
		if (log.isDebugEnabled()) log.debug("getPageModifiedDate start");
		long result = new Date().getTime();

		try {
			byte viewType = me.getViewType();

			switch (viewType) {
				case Constants.VIEW_TYPE_CONTENT:
				case Constants.VIEW_TYPE_UNIT: {
					String reference = me.getReference();
					if (reference != null) {
						Integer refId = null;
						refId = new Integer(reference);
						if (refId != null) {
							ContentVersionHbm contentVersion = null;
							ContentHbm vc = (ContentHbm) getSessionFactory().getCurrentSession().load(ContentHbmImpl.class, refId);
							if (getTizzitPropertiesBeanSpring().isLiveserver()) {
								contentVersion = vc.getContentVersionForPublish();
							} else {
								contentVersion = vc.getLastContentVersion();
							}
							if (contentVersion != null) {
								result = contentVersion.getCreateDate();
							}
						}
					}
					break;
				}
				case Constants.VIEW_TYPE_SYMLINK: {
					String reference = me.getReference();
					if (reference != null) {
						Integer refId = null;
						refId = new Integer(reference);
						if (refId != null) {
							ViewComponentHbm vc = (ViewComponentHbm) getSessionFactory().getCurrentSession().load(ViewComponentHbmImpl.class, refId);
							result = this.getPageModifiedDate(vc);
						}
					}
					break;
				}
				case Constants.VIEW_TYPE_INTERNAL_LINK:
				case Constants.VIEW_TYPE_SEPARATOR:
				case Constants.VIEW_TYPE_EXTERNAL_LINK: {
					result = me.getCreateDate();
					break;
				}
				default:
					break;
			}
		} catch (Exception e) {
			log.warn("Error getting pageModifiedDate, setting to \"now\"", e);
		}
		if (log.isDebugEnabled()) log.debug("LEAVING GET PAGE MODIFIED DATE");
		return result;
	}

	@Override
	protected boolean handleHasPublishContentVersion(ViewComponentHbm me) {
		boolean retVal = false;
		ContentHbm cl = null;
		ContentVersionHbm cvl = null;

		try {
			if (me.getViewType() == Constants.VIEW_TYPE_CONTENT || me.getViewType() == Constants.VIEW_TYPE_UNIT) {
				String ref = me.getReference();
				cl = (ContentHbm) getSessionFactory().getCurrentSession().load(ContentHbmImpl.class, new Integer(ref));
				cvl = cl.getContentVersionForPublish();
				if (cvl != null) {
					retVal = true;
				}
			}
		} catch (Exception exe) {
			log.error("hasPublishContentVersion: This should not occure, basically no ContentVersion found or wrong Reference: " + me.getReference() + " Content:" + cl + " cvl:" + cvl + " vcid:" + me.getViewComponentId());
		}
		return retVal;
	}

	@Override
	protected Date handleGetNavigationAge(Integer refVcId, String since, int depth, boolean getPUBLSVersion) throws Exception {
		try {
			if (log.isDebugEnabled()) log.debug("begin getNavigationAge");
			Date retVal = null;
			try {
				Integer viewDocumentId = this.load(refVcId).getViewDocument().getViewDocumentId();
				Query q = getSessionFactory().getCurrentSession().createQuery("SELECT MAX(vc.lastModifiedDate) FROM de.juwimm.cms.model.ViewComponentHbm vc WHERE vc.viewDocument.viewDocumentId = ?");
				q.setInteger(0, viewDocumentId.intValue());
				Long date = (Long) q.uniqueResult();
				if (log.isDebugEnabled()) log.debug("getNavigationAge got md from SQL Command");
				retVal = new Date(date.longValue());
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			if (retVal == null) {
				retVal = new Date();
			}

			if (log.isDebugEnabled()) log.debug("end getNavigationAge " + retVal);
			return retVal;
		} catch (Exception e) {
			log.error("Could not get navigation age", e);
			throw new UserException("Could not get navigation age: " + e.getMessage());
		}
	}

	@Override
	protected String handleGetLastModifiedPages(Integer viewDocumentId, Integer unitId, int numberOfPages, boolean getPublsVersion) throws Exception {
		if (log.isDebugEnabled()) log.debug("begin getLastModifiedPages");
		StringBuffer sb = new StringBuffer("<lastModifiedPages>");
		try {
			try {
				Query q = null;
				// TODO implement search in unit. right now I can't find all
				// pages belonging to one unit
				// if (unitId == null) {
				// get newest pages for complete site
				q = getSessionFactory().getCurrentSession().createQuery("SELECT vc.viewComponentId FROM de.juwimm.cms.model.ViewComponentHbm vc WHERE vc.viewDocument.viewDocumentId = ? ORDER BY vc.userLastModifiedDate DESC");
				q.setInteger(0, viewDocumentId.intValue());
				// } else {
				// get newest pages for one unit
				// q =
				// getSessionFactory().getCurrentSession().createQuery("SELECT
				// vc.viewComponentId FROM de.juwimm.cms.model.ViewComponentHbm
				// vc WHERE vc.viewDocument.site.siteId = ? ORDER BY
				// vc.userLastModifiedDate DESC");
				// q.setInteger(0, unitId.intValue());
				// }
				q.setMaxResults(numberOfPages);
				ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
				PrintStream out = new PrintStream(byteOut, true, "UTF-8");
				Iterator it = q.list().iterator();
				while (it.hasNext()) {
					Integer vcId = (Integer) it.next();
					ViewComponentHbm viewComponent = this.load(vcId);
					this.toXml(viewComponent, null, false, true, 1, getPublsVersion, true, out);
					sb.append(byteOut.toString("UTF-8"));
					byteOut.reset();
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		} catch (Exception e) {
			log.error("Error getting " + numberOfPages + " last-modified pages for viewDocument " + viewDocumentId + " and unit " + unitId + ": " + e.getMessage(), e);
			throw new UserException("Error getting " + numberOfPages + " last-modified pages for viewDocument " + viewDocumentId + " and unit " + unitId + ": " + e.getMessage());
		}
		sb.append("</lastModifiedPages>");

		return sb.toString();
	}

	@Override
	protected ViewComponentHbm handleCreate(ViewDocumentHbm viewDocument, String reference, String displayLinkName, String linkDescription, Integer viewComponentId) throws Exception {
		return handleCreate(viewDocument, reference, displayLinkName, linkDescription, null, viewComponentId);
	}

	@Override
	protected ViewComponentHbm handleCreate(ViewDocumentHbm vd, String reference, String displayLinkName, String linkDescription, String urlLinkName, Integer viewComponentId) throws Exception {
		ViewComponentHbm vc = ViewComponentHbm.Factory.newInstance();
		try {
			if (viewComponentId == null) {
				Integer id = sequenceHbmDao.getNextSequenceNumber("viewcomponent.view_component_id");
				vc.setViewComponentId(id);
			} else {
				vc.setViewComponentId(viewComponentId);
			}
		} catch (Exception e) {
			log.error("Error creating/setting primary key", e);
		}
		vc.setDisplayLinkName(displayLinkName);
		vc.setLinkDescription(linkDescription);
		if (urlLinkName != null && !"".equalsIgnoreCase(urlLinkName)) {
			vc.setUrlLinkName(urlLinkName);
		} else {
			vc.setUrlLinkName(vc.getViewComponentId().toString());
		}

		if (reference.equals("root")) {
			vc.setViewType(new Byte("1").byteValue());
		} else if (reference.startsWith("content:")) {
			vc.setReference(reference.substring(8));
			vc.setViewType(new Byte("1").byteValue());
		} else if (reference.startsWith("jump:")) {
			vc.setReference(reference.substring(5));
			vc.setViewType(new Byte("2").byteValue());
		} else if (reference.equalsIgnoreCase("SEPARATOR")) {
			vc.setViewType(new Byte("7").byteValue());
		} else if (reference.startsWith("link:")) {
			vc.setReference(reference.substring(5));
			vc.setViewType(new Byte("3").byteValue());
		} else if (reference.startsWith("symlink:")) {
			vc.setReference(reference.substring(8));
			vc.setViewType(new Byte("6").byteValue());
		} else {
			vc.setReference(reference);
		}
		vc.setStatus(0);
		vc.setOnline((byte) 0);
		vc.setVisible(true);
		vc.setSearchIndexed(true);
		vc.setXmlSearchIndexed(true);
		vc.setViewDocument(vd);

		long ts = System.currentTimeMillis();
		vc.setLastModifiedDate(ts);
		vc.setUserLastModifiedDate(ts);
		vc.setCreateDate(ts);
		if (reference.equals("root")) {
			ContentValue cdao = new ContentValue();
			cdao.setTemplate("standard");
			cdao.setHeading("Initial Page");
			cdao.setVersion("1");
			cdao.setContentText("<source></source>");
			try {
				ContentHbm content = getContentHbmDao().createWithContentVersion(cdao, AuthenticationHelper.getUserName());
				vc.setReference(content.getContentId().toString());
			} catch (Throwable exe) {
				log.warn("RootContent could not be created because of duplicate key. This should only occure on liveserver.");
			}
			vc.setViewType(new Byte("1").byteValue());
		} else if (vc.getReference() != null && vc.getReference().startsWith("symlink:")) {
			try {
				ViewComponentHbm vclRef = super.load(new Integer(vc.getReference()));
				ContentHbm c = getContentHbmDao().load(new Integer(vclRef.getReference()));
				c.setUpdateSearchIndex(true);
			} catch (Exception exe) {
				log.warn("Error occured during creation of symlink", exe);
			}
		}
		getHibernateTemplate().save(vc);
		return vc;
	}

	@Override
	public void remove(ViewComponentHbm viewComponentHbm) {
		// delete all children
		if (!viewComponentHbm.isLeaf()) {
			ViewComponentHbm currentNode, nextNode;
			currentNode = viewComponentHbm.getFirstChild();
			nextNode = currentNode.getNextNode();
			remove(currentNode);
			while (nextNode != null) {
				currentNode = nextNode;
				nextNode = nextNode.getNextNode();
				remove(currentNode);
			}
		}
		// delete referenced content
		byte viewType = viewComponentHbm.getViewType();
		if (viewType == Constants.VIEW_TYPE_CONTENT || viewType == Constants.VIEW_TYPE_UNIT) {
			try {
				String reference = viewComponentHbm.getReference();
				if (reference != null && !reference.equals("") && !reference.equals("DUMMY") && !reference.equals("root")) {
					ContentHbm content = getContentHbmDao().load(new Integer(reference));
					getContentHbmDao().remove(content);
				}
			} catch (Exception exe) {
				log.warn("Error deleting referenced content for viewComponent " + viewComponentHbm.getViewComponentId() + "\n" + exe.getMessage());
			}
		}
		// sending deleted-message to searchEngine
		searchengineDeleteService.deletePage(viewComponentHbm);

		// if this page was protected remove protection
		Realm2viewComponentHbm realm = viewComponentHbm.getRealm2vc();
		if (realm != null) getRealm2viewComponentHbmDao().remove(realm);
		// if this page is a login-page, update realms that use this page

		Iterator it = viewComponentHbm.getRealm4login().iterator();
		ArrayList<Realm2viewComponentHbm> al = new ArrayList<Realm2viewComponentHbm>();
		while (it.hasNext()) {
			Realm2viewComponentHbm r = (Realm2viewComponentHbm) it.next();
			al.add(r);
		}
		it = al.iterator();
		while (it.hasNext()) {
			Realm2viewComponentHbm r = (Realm2viewComponentHbm) it.next();
			r.setLoginPage(null);
		}
		super.remove(viewComponentHbm);
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findByStatus(final int transform, final java.lang.Integer viewDocumentId, final int status) {
		return this.findByStatus(transform, "from de.juwimm.cms.model.ViewComponentHbm as v where v.viewDocument.viewDocumentId = ? and v.status = ?", viewDocumentId, status);
	}

	@Override
	public java.lang.Object find4Unit(final int transform, final java.lang.Integer unitId, final java.lang.Integer viewDocumentId) {
		return this.find4Unit(transform, "from de.juwimm.cms.model.ViewComponentHbm v where v.assignedUnit.unitId = ? and v.viewDocument.viewDocumentId = ?", unitId, viewDocumentId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findByReferencedContent(final int transform, final java.lang.String reference) {
		return this.findByReferencedContent(transform, "from de.juwimm.cms.model.ViewComponentHbm as v where v.reference = ? and v.viewType in (0, 1, 4, 5)", reference);
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findByReferencedViewComponent(final int transform, final java.lang.String reference) {
		return this.findByReferencedViewComponent(transform, "from de.juwimm.cms.model.ViewComponentHbm as v where v.reference = ? and v.viewType in (6, 2)", reference);
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findAllWithUnit(final int transform, final java.lang.Integer viewDocumentId) {
		return this.findAllWithUnit(transform, "from de.juwimm.cms.model.ViewComponentHbm as v where v.assignedUnit is not null and v.viewDocument.viewDocumentId = ?", viewDocumentId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findByParent(final int transform, final java.lang.Integer vcId) {
		return this.findByParent(transform, "from de.juwimm.cms.model.ViewComponentHbm v WHERE v.parent.viewComponentId = ?", vcId);
	}

	@Override
	protected java.util.Collection handleFindRootViewComponents4Unit(Integer unitId) throws Exception {
		Query query = getSession().createQuery("from de.juwimm.cms.model.ViewComponentHbm v where v.assignedUnit.unitId = :unitId");
		query.setParameter("unitId", unitId);
		return query.list();
	}

	@Override
	protected ViewComponentHbm handleCloneViewComponent(ViewComponentHbm oldViewComponent) throws Exception {
		ViewComponentHbm viewComponentHbm = ViewComponentHbm.Factory.newInstance();
		try {
			Integer id = sequenceHbmDao.getNextSequenceNumber("viewcomponent.view_component_id");
			viewComponentHbm.setViewComponentId(id);
		} catch (Exception e) {
			log.error("Error creating/setting primary key", e);
		}
		viewComponentHbm.setApprovedLinkName(oldViewComponent.getApprovedLinkName());
		if (oldViewComponent.getAssignedUnit() != null) {
			viewComponentHbm.setAssignedUnit(oldViewComponent.getAssignedUnit());
		}
		viewComponentHbm.setCreateDate(System.currentTimeMillis());
		viewComponentHbm.setDeployCommand(oldViewComponent.getDeployCommand());
		viewComponentHbm.setDisplayLinkName("copy_" + oldViewComponent.getDisplayLinkName());
		viewComponentHbm.setDisplaySettings(oldViewComponent.getDisplaySettings());
		viewComponentHbm.setLinkDescription(oldViewComponent.getLinkDescription());
		viewComponentHbm.setMetaData(oldViewComponent.getMetaData());
		viewComponentHbm.setMetaDescription(oldViewComponent.getMetaDescription());
		viewComponentHbm.setOnline(oldViewComponent.getOnline());
		viewComponentHbm.setOnlineStart(oldViewComponent.getOnlineStart());
		viewComponentHbm.setOnlineStop(oldViewComponent.getOnlineStop());
		viewComponentHbm.setRealm2vc(oldViewComponent.getRealm2vc());
		//viewComponentHbm.setRealm4login(oldViewComponent.getRealm4login());
		viewComponentHbm.setSearchIndexed(oldViewComponent.isSearchIndexed());
		viewComponentHbm.setShowType(oldViewComponent.getShowType());
		viewComponentHbm.setStatus(oldViewComponent.getStatus());
		viewComponentHbm.setUrlLinkName("copy_" + oldViewComponent.getUrlLinkName());
		viewComponentHbm.setViewDocument(oldViewComponent.getViewDocument());
		viewComponentHbm.setViewIndex(oldViewComponent.getViewIndex());
		viewComponentHbm.setViewLevel(oldViewComponent.getViewLevel());
		viewComponentHbm.setViewType(oldViewComponent.getViewType());
		viewComponentHbm.setVisible(oldViewComponent.isVisible());
		viewComponentHbm.setXmlSearchIndexed(oldViewComponent.isXmlSearchIndexed());

		ContentHbm oldContent = getContentHbmDao().load(Integer.valueOf(oldViewComponent.getReference()));
		ContentHbm newContent = getContentHbmDao().cloneContent(oldContent);
		viewComponentHbm.setReference(String.valueOf(newContent.getContentId()));
		return create(viewComponentHbm);
	}

}
