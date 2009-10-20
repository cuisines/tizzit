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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.model.ContentHbm;
import de.juwimm.cms.model.ContentVersionHbm;
import de.juwimm.cms.model.DocumentHbm;
import de.juwimm.cms.model.PictureHbm;
import de.juwimm.cms.model.SequenceHbmDao;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.model.ViewComponentHbm;
import de.juwimm.cms.model.ViewDocumentHbm;
import de.juwimm.cms.remote.helper.AuthenticationHelper;
import de.juwimm.cms.search.beans.SearchengineService;
import de.juwimm.cms.search.vo.XmlSearchValue;
import de.juwimm.cms.vo.SiteValue;
import de.juwimm.cms.vo.ViewComponentValue;
import de.juwimm.cms.vo.ViewDocumentValue;
import de.juwimm.cms.vo.compound.ViewIdAndInfoTextValue;
import de.juwimm.cms.vo.compound.ViewIdAndUnitIdValue;

/**
 * @see de.juwimm.cms.remote.ViewServiceSpring
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class ViewServiceSpringImpl extends ViewServiceSpringBase {
	private static Logger log = Logger.getLogger(ViewServiceSpringImpl.class);

	@Autowired
	private SearchengineService searchengineService;
	@Autowired
	private SequenceHbmDao sequenceHbmDao;

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#removeViewComponent(java.lang.Integer, boolean)
	 */
	@Override
	protected void handleRemoveViewComponent(Integer viewComponentId, boolean force) throws Exception {
		if (log.isDebugEnabled()) log.debug("begin removeViewComponent");
		// Check usage only if the force is not with us
		if (!force) {
			boolean isInUse = false;
			try {
				Collection vcrefcoll = super.getViewComponentHbmDao().findByReferencedViewComponent(viewComponentId.toString());
				if (vcrefcoll != null && vcrefcoll.size() > 0) {
					if (log.isDebugEnabled()) log.debug("Found VCrefs though Symlink or InternalLink");
					isInUse = true;
				}
			} catch (Exception e) {
				throw new UserException(e.getMessage());
			}
			if (!isInUse) {
				try {
					ViewComponentHbm vcl = super.getViewComponentHbmDao().load(viewComponentId);
					SiteHbm site = vcl.getViewDocument().getSite();
					String xpathQuery = "//internalLink[@viewid='" + viewComponentId + "']";

					XmlSearchValue[] searchresult = searchengineService.searchXML(site.getSiteId(), xpathQuery);
					if (searchresult != null && searchresult.length > 0) {
						if (log.isDebugEnabled()) log.debug("Found VCrefs though InternalLink in Content");
						isInUse = true;
					}
				} catch (Exception e) {
					throw new UserException(e.getMessage());
				}
			}
			if (isInUse) { throw new UserException("VCINUSE"); }
		}
		// try to remove
		try {
			ViewComponentHbm view = super.getViewComponentHbmDao().load(viewComponentId);
			ViewComponentHbm prevNode, nextNode;
			if (view.getPrevNode() == null && view.getNextNode() == null) {
				// only child in this chain
				view.getParent().setFirstChild(null);
			} else if (view.getPrevNode() == null) {
				// first child with next
				view.getParent().setFirstChild(view.getNextNode());
				view.getNextNode().setPrevNode(null);
			} else if (view.getNextNode() != null) {
				// between two nodes
				nextNode = view.getNextNode();
				prevNode = view.getPrevNode();
				nextNode.setPrevNode(prevNode);
				prevNode.setNextNode(nextNode);
			} else {
				// last child in chain
				view.getPrevNode().setNextNode(null);
			}
			super.getViewComponentHbmDao().remove(view.getViewComponentId());
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
		if (log.isDebugEnabled()) log.debug("end removeViewComponent");
	}

	/**
	 * Returns an array containing all ViewComponents, which will contain a reference to the given ViewComponent or which are itself a SymlinkViewComponent or InternalLinkViewComponent.<br>
	 * Because of FAST FAST FAST I used the MetaData Attribute for UnitName.
	 * 
	 * @return ViewComponentDao[] Array, containing the UnitName in the MetaData Attribute
	 * 
	 * @throws UserException
	 * @see de.juwimm.cms.remote.ViewServiceSpring#getViewComponentsWithReferenceToViewComponentId(java.lang.Integer)
	 */
	@Override
	protected ViewComponentValue[] handleGetViewComponentsWithReferenceToViewComponentId(Integer viewComponentId) throws UserException {
		ArrayList<ViewComponentValue> viewComponentValues = new ArrayList<ViewComponentValue>();
		Date start = new Date();
		Collection viewComponentReferenceCollection = super.getViewComponentHbmDao().findByReferencedViewComponent(viewComponentId.toString());
		if (viewComponentReferenceCollection != null) {
			Iterator it = viewComponentReferenceCollection.iterator();
			ViewComponentHbm viewComponentHbm = null;
			ViewComponentValue viewComponentValue = null;
			UnitHbm unit = null;
			while (it.hasNext()) {
				viewComponentHbm = (ViewComponentHbm) it.next();
				viewComponentValue = viewComponentHbm.getDao();
				unit = super.getUnitHbmDao().load(viewComponentHbm.getUnit4ViewComponent());
				if (unit != null) {
					if (viewComponentValue != null) {
						viewComponentValue.setMetaData(unit.getName());
					}
				}
				viewComponentValues.add(viewComponentValue);
			}
		}
		Date end = new Date();
		if (log.isDebugEnabled()) {
			log.debug("duration getViewComponentsWithReferenceToViewComponentId: " + (end.getTime() - start.getTime()) + " milliseconds");
		}
		ViewComponentHbm viewComponent = super.getViewComponentHbmDao().load(viewComponentId);
		SiteHbm site = viewComponent.getViewDocument().getSite();
		String xpathQuery = "//internalLink[@viewid='" + viewComponentId + "']";
		start = new Date();
		XmlSearchValue[] searchresult = null;
		try {
			searchresult = searchengineService.searchXML(site.getSiteId(), xpathQuery);
		} catch (Exception e) {
			log.error("Error", e);
		}
		end = new Date();
		if (log.isDebugEnabled()) {
			log.debug("duration searchXML: " + (end.getTime() - start.getTime()) + " milliseconds");
		}
		if (searchresult != null) {
			for (int i = 0; i < searchresult.length; i++) {
				XmlSearchValue searchrow = searchresult[i];
				ViewComponentValue vcd = new ViewComponentValue();
				// generally set viewType to content... everything else will be found though finder
				vcd.setViewType(Constants.VIEW_TYPE_CONTENT);
				vcd.setLinkDescription(searchrow.getInfoText());
				vcd.setDisplayLinkName(searchrow.getText());
				vcd.setMetaData("");
				Integer unitId = searchrow.getUnitId();
				vcd.setUnitId(unitId);
				UnitHbm unit = super.getUnitHbmDao().load(unitId);
				vcd.setMetaData(unit.getName());
				vcd.setViewComponentId(searchrow.getViewComponentId());
				viewComponentValues.add(vcd);
			}
		}
		return viewComponentValues.toArray(new ViewComponentValue[0]);
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#getRootIdFromViewDocument(java.lang.Integer)
	 */
	@Override
	protected Integer handleGetRootIdFromViewDocument(Integer viewDocumentId) throws Exception {
		ViewDocumentHbm doc;
		try {
			if (log.isDebugEnabled()) log.debug("begin removeViewDocument");
			doc = super.getViewDocumentHbmDao().load(viewDocumentId);
			if (log.isDebugEnabled()) log.debug("end   removeViewDocument");
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
		return doc.getViewComponent().getViewComponentId();
	}

	/**
	 * Insert a ViewComponent before or after another ViewComponent.
	 * 
	 * @see de.juwimm.cms.remote.ViewServiceSpring#insertViewComponent(java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	protected ViewComponentValue handleInsertViewComponent(Integer childId, Integer viewDocumentId, String strReference, String strDisplayLinkName, String strInfo, int intPos) throws Exception {
		try {
			if (log.isDebugEnabled()) log.debug("begin insertViewComponent");
			ViewComponentHbm tmpNode;
			ViewComponentHbm node = super.getViewComponentHbmDao().load(childId);
			ViewDocumentHbm vd = super.getViewDocumentHbmDao().load(viewDocumentId);
			ViewComponentHbm newNode = getViewComponentHbmDao().create(vd, strReference, strDisplayLinkName, strInfo, null);

			node.getParent().addChild(newNode);
			newNode.setParentViewComponent(node.getParent());
			newNode = super.getViewComponentHbmDao().create(newNode);
			switch (intPos) {
				case Constants.ADD_BEFORE:
					if ((tmpNode = node.getPrevNode()) != null) {
						tmpNode.setNextNode(newNode);
						newNode.setPrevNode(tmpNode);
					} else {
						ViewComponentHbm parent = node.getParent();
						parent.setFirstChild(newNode);
					}
					newNode.setNextNode(node);
					node.setPrevNode(newNode);
					break;

				default:
					if ((tmpNode = node.getNextNode()) != null) {
						tmpNode.setPrevNode(newNode);
						newNode.setNextNode(tmpNode);
					}
					newNode.setPrevNode(node);
					node.setNextNode(newNode);
					break;
			}

			if (log.isDebugEnabled()) log.debug("end insertViewComponent");
			return newNode.getDao(-1);
		} catch (Exception e) {
			log.error("Could not insert viewComponent: " + e.getMessage(), e);
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#addFirstViewComponent(java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected ViewComponentValue handleAddFirstViewComponent(Integer parentId, Integer viewDocumentId, String strReference, String strText, String strInfo) throws Exception {
		try {
			if (log.isDebugEnabled()) log.debug("begin addFirstViewComponent");
			ViewComponentHbm node = super.getViewComponentHbmDao().load(parentId);
			if (!node.isLeaf()) { throw new UserException("node is not a leaf."); }
			ViewDocumentHbm vd = super.getViewDocumentHbmDao().load(viewDocumentId);
			ViewComponentHbm newNode = super.getViewComponentHbmDao().create(getViewComponentHbmDao().create(vd, strReference, strText, strInfo, null));
			newNode.setParentViewComponent(node);
			node.addChild(newNode);
			node.setFirstChild(newNode);
			if (log.isDebugEnabled()) log.debug("end addFirstViewComponent");
			return newNode.getDao(-1);
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#getViewComponentChildren(java.lang.Integer)
	 */
	@Override
	protected ViewComponentValue[] handleGetViewComponentChildren(Integer parentId) throws Exception {
		try {
			if (log.isDebugEnabled()) log.debug("begin getViewComponentChildren");
			ViewComponentHbm view = super.getViewComponentHbmDao().load(parentId);
			if (view.isLeaf()) { throw new UserException("node is a leaf."); }
			Vector<ViewComponentValue> vec = new Vector<ViewComponentValue>();
			for (Iterator i = view.getChildren().iterator(); i.hasNext();) {
				ViewComponentHbm vcHbm = (ViewComponentHbm) i.next();
				ViewComponentValue vc = vcHbm.getDao(-1);				
				vec.addElement(vc);
			}
			if (log.isDebugEnabled()) log.debug("end getViewComponentChildren");
			return vec.toArray(new ViewComponentValue[0]);
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#getUnit4ViewComponent(java.lang.Integer)
	 */
	@Override
	protected Integer handleGetUnit4ViewComponent(Integer viewComponentId) throws Exception {
		try {
			ViewComponentHbm view = super.getViewComponentHbmDao().load(viewComponentId);
			if (view.getAssignedUnit() != null) { return view.getAssignedUnit().getUnitId(); }
			return view.getUnit4ViewComponent();
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#cancelApproval(java.lang.Integer)
	 */
	@Override
	protected void handleCancelApproval(Integer viewComponentId) throws Exception {
		try {
			ViewComponentHbm view = super.getViewComponentHbmDao().load(viewComponentId);
			view.setStatus(Constants.DEPLOY_STATUS_EDITED);
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#getViewComponent4UnitViewComponent(java.lang.Integer)
	 */
	@Override
	protected Integer handleGetViewComponent4UnitViewComponent(Integer viewComponentId) throws Exception {
		try {
			ViewComponentHbm view = super.getViewComponentHbmDao().load(viewComponentId);
			if (view.getAssignedUnit() != null) { return view.getViewComponentId(); }
			while (view.getAssignedUnit() == null && !view.isRoot()) {
				view = view.getParent();
				if (view.getAssignedUnit() != null) { return view.getViewComponentId(); }
			}
			return null;
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#getViewComponent(java.lang.Integer)
	 */
	@Override
	protected ViewComponentValue handleGetViewComponent(Integer vcId) throws Exception {
		try {
			ViewComponentHbm vcl = super.getViewComponentHbmDao().load(vcId);
			return vcl.getViewComponentDao();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#getPathForViewComponentId(java.lang.Integer)
	 */
	@Override
	protected String handleGetPathForViewComponentId(Integer id) throws Exception {
		if (log.isDebugEnabled()) log.debug("GET PATH FOR VIEW COMPONENT ID");
		try {
			ViewComponentHbm vcl = super.getViewComponentHbmDao().load(id);
			return vcl.getPath();
		} catch (Exception e) {
			log.warn("GET PATH FOR VIEW COMPONENT ID " + e.getMessage());
			return "";
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#getViewComponentId4PathWithViewTypeAndLanguage(java.lang.String, java.lang.String, java.lang.String, java.lang.Integer)
	 */
	@Override
	protected Integer handleGetViewComponentId4PathWithViewTypeAndLanguage(String path, String viewType, String language, Integer siteId) throws Exception {
		if (log.isDebugEnabled()) {

			log.debug("GET VIEW COMPONENT ID 4 PATH WITH VIEW TYPE AND LANGUAGE");
			log.debug("Path: " + path + ", ViewType: " + viewType + ", Language: " + language + ", SiteId: " + siteId);
		}
		try {
			ViewDocumentHbm vdl = super.getViewDocumentHbmDao().findByViewTypeAndLanguage(viewType, language, siteId);

			Integer rootVCid = vdl.getViewComponent().getViewComponentId();
			return getViewComponentId4Path(path, rootVCid);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#getUnitForPath(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected Integer handleGetUnitForPath(Integer siteId, String vdLang, String vdType, String path) throws Exception {
		Integer unitId = null;
		try {
			if (vdLang.equalsIgnoreCase("img") && path.startsWith("/ejbimage")) {
				int start = path.indexOf("id=");
				char[] ca = path.substring(start + 3).toCharArray();
				String id = "";
				for (int i = 0; i < ca.length; i++) {
					if (ca[i] >= 48 && ca[i] <= 57) {
						id += ca[i];
					} else {
						break;
					}
				}
				try {
					PictureHbm pic = super.getPictureHbmDao().load(new Integer(id));
					unitId = pic.getUnit().getUnitId();
				} catch (Exception exe) {
				}
			} else if (vdLang.equalsIgnoreCase("img") && path.startsWith("/ejbfile")) {
				int start = path.indexOf("id=");
				char[] ca = path.substring(start + 3).toCharArray();
				String id = "";
				for (int i = 0; i < ca.length; i++) {
					if (ca[i] >= 48 && ca[i] <= 57) {
						id += ca[i];
					} else {
						break;
					}
				}
				try {
					DocumentHbm doc = super.getDocumentHbmDao().load(new Integer(id));
					unitId = doc.getUnit().getUnitId();
				} catch (Exception exe) {
				}
			} else {
				ViewDocumentHbm vd = null;
				try {
					vd = super.getViewDocumentHbmDao().findByViewTypeAndLanguage(vdType, vdLang, siteId);
				} catch (Exception exe) {
					return unitId;
				}
				if (vd == null) return unitId;
				ViewComponentHbm vcl = this.getViewComponentId4PathResolver(path, vd.getViewComponent().getViewComponentId(), true);
				if (log.isDebugEnabled()) {
					if (vcl != null) {
						if (log.isDebugEnabled()) log.debug("Found ViewComponent: " + vcl.getDisplayLinkName() + " for path " + path);
					} else {
						if (log.isDebugEnabled()) log.debug("Found no ViewComponent for path " + path);
					}
				}
				if (vcl != null) {
					unitId = vcl.getUnit4ViewComponent();
				}
			}
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
		return unitId;
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#getViewComponentId4Path(java.lang.String, java.lang.Integer)
	 */
	@Override
	protected Integer handleGetViewComponentId4Path(String path, Integer rootViewComponentId) throws Exception {
		if (log.isDebugEnabled()) log.debug("GET VIEW COMPONENT ID 4 PATH");
		try {
			ViewComponentHbm vcl = this.getViewComponentId4PathResolver(path, rootViewComponentId, false);
			// createPathCache(vcl.getViewComponentId(), rootViewComponentId, path);
			return vcl.getViewComponentId();
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#getViewComponentWithDepth(java.lang.Integer, int)
	 */
	@Override
	protected ViewComponentValue handleGetViewComponentWithDepth(Integer viewComponentId, int depth) throws Exception {
		try {
			ViewComponentHbm view = super.getViewComponentHbmDao().load(viewComponentId);
			ViewComponentValue viewComponentValue = view.getDao(depth);
			getOnlineStatus(view,viewComponentValue);
			return viewComponentValue;
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	private void getOnlineStatus(ViewComponentHbm viewComponentHbm,ViewComponentValue viewComponentValue){
		viewComponentValue.setHasPublishContentVersion(getViewComponentHbmDao().hasPublishContentVersion(viewComponentHbm));
		if(viewComponentValue.getChildren()!=null){
			for(ViewComponentValue vc:viewComponentValue.getChildren()){
				getOnlineStatus(getViewComponentHbmDao().load(vc.getViewComponentId()),vc);
			}
		}
	}
	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#getViewComponent4Unit(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	protected ViewComponentValue handleGetViewComponent4Unit(Integer unitId, Integer viewDocumentId) throws Exception {
		ViewComponentValue vcd = null;
		try {
			ViewComponentHbm view = super.getViewComponentHbmDao().find4Unit(unitId, viewDocumentId);
			vcd = view.getDao(-1);			
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
		return vcd;
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#getViewDocuments()
	 */
	@Override
	protected ViewDocumentValue[] handleGetViewDocuments() throws Exception {
		try {
			Integer siteId = super.getUserHbmDao().load(AuthenticationHelper.getUserName()).getActiveSite().getSiteId();
			return getViewDocuments4Site(siteId);
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#getViewDocuments4Site(java.lang.Integer)
	 */
	@Override
	protected ViewDocumentValue[] handleGetViewDocuments4Site(Integer siteId) throws Exception {
		try {
			Vector<ViewDocumentValue> vec = new Vector<ViewDocumentValue>();
			Iterator it = super.getViewDocumentHbmDao().findAll(siteId).iterator();
			while (it.hasNext()) {
				vec.addElement(((ViewDocumentHbm) it.next()).getDao());
			}
			return vec.toArray(new ViewDocumentValue[0]);
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#getViewComponentForLanguageOrShortlink(java.lang.String, java.lang.String, java.lang.Integer)
	 */
	@Override
	protected String[] handleGetViewComponentForLanguageOrShortlink(String viewType, String lang, Integer siteId) throws Exception {
		if (log.isDebugEnabled()) log.debug("GET VIEW COMPONET FOR LANGUAGE OR SHORTLINK");
		try {
			String[] retArr = new String[4];
			ViewDocumentHbm vd = null;
			ViewComponentHbm vcl = null;
			try {
				vd = super.getViewDocumentHbmDao().findByViewTypeAndLanguage(viewType, lang, siteId);
				vcl = vd.getViewComponent();
			} catch (Exception exe) {
				// maybe an Shortlink
				try {
					SiteHbm site = super.getSiteHbmDao().load(siteId);
					vd = site.getDefaultViewDocument();
					vcl = this.getViewComponentId4PathResolver(lang, vd.getViewComponent().getViewComponentId(), false);
				} catch (Exception exee) {
				}
			}
			if (vd == null || vcl == null) {
				log.warn("Could not find viewComponent/shortlink for viewType: " + viewType + " language: " + lang + " siteId: " + siteId);
			} else {
				retArr[0] = vcl.getViewComponentId().toString();
				retArr[1] = getPathForViewComponentId(vcl.getViewComponentId());
				retArr[2] = vd.getLanguage();
				retArr[3] = Byte.toString(vcl.getViewType());
			}
			return retArr;
		} catch (Exception e) {
			return null;
		}
	}

	private ViewComponentHbm getViewComponentId4PathResolver(String path, Integer rootViewComponentId, boolean ret) throws Exception {
		if (log.isDebugEnabled()) log.debug("GET VIEW COMPONENT ID 4 PATH RESOLVER");

		try {
			ViewComponentHbm vS = super.getViewComponentHbmDao().load(rootViewComponentId);
			Collection<ViewComponentHbm> children = super.getViewComponentHbmDao().findByParent(vS.getViewComponentId());

			// List children = getViewComponentLocalHome().findByParent(vS.getViewComponentId());
			ViewComponentHbm vcl = null;
			if (ret) {
				path = java.net.URLDecoder.decode(path, "ISO-8859-1");
				if (path != null && !path.equalsIgnoreCase("")) {
					vcl = this.getIdFromTreePath(path, children, ret, vS);
				}
			} else {
				vcl = this.getIdFromTreePath(path, children, ret, vS);
				if (vcl == null) {
					String iePath = java.net.URLEncoder.encode(path, "ISO-8859-1");
					path = java.net.URLDecoder.decode(iePath, "UTF-8");
					vcl = this.getIdFromTreePath(path, children, ret, vS);
				}
			}
			if (vcl != null && vcl.getViewType() == Constants.VIEW_TYPE_INTERNAL_LINK) {
				Integer refId = new Integer(vcl.getReference());
				vcl = super.getViewComponentHbmDao().load(refId);
			}

			return vcl;
		} catch (Exception e) {
			log.error("GET VIEW COMPONENT ID 4 PATH RESOLVER " + e.getMessage());
			throw new Exception(e.getMessage());
		}

	}

	private ViewComponentHbm getIdFromTreePath(String path, Collection<ViewComponentHbm> vcdarr, boolean ret, ViewComponentHbm prevOne) {
		if (log.isDebugEnabled()) log.debug("GET ID FROM TREE PATH");
		StringTokenizer st = new StringTokenizer(path, "/");
		String firstElement = st.nextToken();
		Iterator<ViewComponentHbm> it = vcdarr.iterator();
		while (it.hasNext()) {
			ViewComponentHbm vcl = it.next();
			String urlLinkName = vcl.getUrlLinkName();
			// urlLinkName may be null (externalLink)
			if (urlLinkName == null) continue;
			if (vcl.getUrlLinkName().equalsIgnoreCase(firstElement)) {
				if (st.hasMoreTokens()) {
					String newpath = "";
					while (st.hasMoreTokens()) {
						if (newpath.equals("")) {
							newpath = st.nextToken();
						} else {
							newpath = newpath + "/" + st.nextToken();
						}
					}
					return this.getIdFromTreePath(newpath, vcl.getChildren(), ret, vcl);
				}
				return vcl;
			}
		}
		if (log.isDebugEnabled()) log.debug("LEAVING GET ID FROM TREEPATH");
		if (ret) { return prevOne; }
		return null;
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#getViewDocument4ViewTypeAndLanguage(java.lang.String, java.lang.String)
	 */
	@Override
	protected ViewDocumentValue handleGetViewDocument4ViewTypeAndLanguage(String viewType, String strLanguage) throws Exception {
		try {
			UserHbm user = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			ViewDocumentHbm vd = super.getViewDocumentHbmDao().findByViewTypeAndLanguage(viewType, strLanguage, user.getActiveSite().getSiteId());
			return vd.getDao();
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#getViewDocument4ViewComponent(java.lang.Integer)
	 */
	@Override
	protected ViewDocumentValue handleGetViewDocument4ViewComponent(Integer viewComponentId) throws Exception {
		try {
			return super.getViewComponentHbmDao().load(viewComponentId).getViewDocument().getDao();
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#getDefaultViewDocument4Site(java.lang.Integer)
	 */
	@Override
	protected ViewDocumentValue handleGetDefaultViewDocument4Site(Integer siteId) throws Exception {
		if (log.isDebugEnabled()) log.debug("GET DEFAULT VIEW DOCUMENT 4 SITE");
		ViewDocumentValue val = new ViewDocumentValue();
		try {
			SiteHbm site = super.getSiteHbmDao().load(siteId);
			ViewDocumentHbm view = site.getDefaultViewDocument();
			val = view.getDao();
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}

		return val;
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#createViewDocument(de.juwimm.cms.vo.ViewDocumentValue)
	 */
	@Override
	protected ViewDocumentValue handleCreateViewDocument(ViewDocumentValue value) throws Exception {
		ViewDocumentHbm vd = null;
		Integer vdid = getViewDocumentHbmDao().create(value.getLanguage(), value.getViewType());
		vd = getViewDocumentHbmDao().load(vdid);
		return vd.getDao();
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#removeViewDocument(java.lang.Integer)
	 */
	@Override
	protected void handleRemoveViewDocument(Integer viewDocumentId) throws Exception {
		try {
			if (log.isDebugEnabled()) log.debug("begin removeViewDocument");
			super.getViewDocumentHbmDao().remove(viewDocumentId);
			if (log.isDebugEnabled()) log.debug("end removeViewDocument");
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#setDefaultViewDocument(java.lang.Integer)
	 */
	@Override
	protected void handleSetDefaultViewDocument(Integer viewDocumentId) throws Exception {
		try {
			if (log.isDebugEnabled()) log.debug("begin setDefaultViewDocument");
			ViewDocumentHbm doc = super.getViewDocumentHbmDao().load(viewDocumentId);
			SiteHbm site = super.getUserHbmDao().load(AuthenticationHelper.getUserName()).getActiveSite();
			site.setDefaultViewDocument(doc);
			site.setLastModifiedDate(new Date().getTime());
			if (log.isDebugEnabled()) log.debug("end setDefaultViewDocument");
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#setUnit4ViewComponent(java.lang.Integer, de.juwimm.cms.vo.ViewDocumentValue, java.lang.Integer)
	 */
	@Override
	protected void handleSetUnit4ViewComponent(Integer unitId, ViewDocumentValue viewDocumentValue, Integer viewComponentId) throws Exception {
		try {
			ViewComponentHbm vc = super.getViewComponentHbmDao().load(viewComponentId);
			UnitHbm unit = super.getUnitHbmDao().load(unitId);
			vc.setAssignedUnit(unit);
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#moveViewComponentUp(java.lang.Integer)
	 */
	@Override
	protected ViewComponentValue handleMoveViewComponentUp(Integer viewComponentId) throws Exception {
		return moveComponentUp(viewComponentId);
	}

	/**
	 * 
	 * @param viewComponentId
	 * @return ViewComponentValue
	 */
	private ViewComponentValue moveComponentUp(Integer viewComponentId) throws UserException {
		try {
			ViewComponentHbm view = super.getViewComponentHbmDao().load(viewComponentId);
			if (view.getPrevNode() == null) { throw new UserException("Node is already first child."); }
			ViewComponentHbm prev = view.getPrevNode();
			ViewComponentHbm next = view.getNextNode();
			ViewComponentHbm prevPrev = prev.getPrevNode();
			ViewComponentHbm parent = view.getParent();
			if (log.isDebugEnabled()) {
				log.debug("moving up: prev " + prev + " next " + next + " prevPrev " + prevPrev + " parent " + parent);
			}
			if (prevPrev != null) {
				prevPrev.setNextNode(view);
				view.setPrevNode(prevPrev);
				view.setNextNode(prev);
				prev.setPrevNode(view);
				prev.setNextNode(next);
				if (log.isDebugEnabled()) {
					log.debug("next6 " + next + " prevgetNextNode " + prev.getNextNode());
				}
				if (next != null) {
					next.setPrevNode(prev);
				}
			} else {
				parent.setFirstChild(view);
				view.setPrevNode(null);
				view.setNextNode(prev);
				prev.setPrevNode(view);
				prev.setNextNode(next);
				if (next != null) {
					next.setPrevNode(prev);
				}
			}
			view.setLastModifiedDate(System.currentTimeMillis());
			return view.getDao();
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#moveViewComponentDown(java.lang.Integer)
	 */
	@Override
	protected ViewComponentValue handleMoveViewComponentDown(Integer viewComponentId) throws Exception {
		return moveComponentDown(viewComponentId);
	}

	/**
	 * 
	 * @param viewComponentId
	 * @return ViewComponentValue
	 * @throws Exception
	 */
	private ViewComponentValue moveComponentDown(Integer viewComponentId) throws UserException {
		try {
			ViewComponentHbm view = super.getViewComponentHbmDao().load(viewComponentId);
			if (view.getNextNode() == null) { throw new UserException("Node is already last child."); }
			ViewComponentHbm prev, next, nextNext, parent;
			prev = view.getPrevNode();
			next = view.getNextNode();
			nextNext = next.getNextNode();
			if (nextNext != null) {
				if (prev != null) {
					prev.setNextNode(next);
				} else {
					parent = view.getParent();
					parent.setFirstChild(next);
				}
				next.setPrevNode(prev);
				next.setNextNode(view);
				view.setPrevNode(next);
				view.setNextNode(nextNext);
				nextNext.setPrevNode(view);
			} else {
				if (prev != null) {
					prev.setNextNode(next);
				} else {
					parent = view.getParent();
					parent.setFirstChild(next);
				}
				next.setPrevNode(prev);
				next.setNextNode(view);
				view.setPrevNode(next);
				view.setNextNode(null);
			}
			view.setLastModifiedDate(System.currentTimeMillis());
			return view.getDao();
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#moveViewComponentLeft(java.lang.Integer)
	 */
	@Override
	protected ViewComponentValue handleMoveViewComponentLeft(Integer viewComponentId) throws Exception {
		return moveComponentLeft(viewComponentId);
	}

	/**
	 * 
	 * @param viewComponentId
	 * @return ViewComponentValue
	 * @throws Exception
	 */
	private ViewComponentValue moveComponentLeft(Integer viewComponentId) throws UserException {
		try {
			ViewComponentHbm thisNode = getViewComponentHbmDao().load(viewComponentId);
			if (thisNode.getParent().getParent().getViewType() == 0) {
				// I'm moving a Unit to a root-entry !
				thisNode.setShowType((byte) 3);
				thisNode.setViewIndex("2");
			}
			ViewComponentHbm prev, next, parent;
			prev = thisNode.getPrevNode();
			next = thisNode.getNextNode();
			parent = thisNode.getParent();
			if (prev != null) {
				prev.setNextNode(next);
			} else {
				parent.setFirstChild(next);
			}
			if (next != null) {
				next.setPrevNode(prev);
			}
			thisNode.setPrevNode(parent);
			thisNode.setNextNode(parent.getNextNode());
			if (thisNode.getNextNode() != null) {
				thisNode.getNextNode().setPrevNode(thisNode);
			}
			parent.setNextNode(thisNode);
			thisNode.setParent(parent.getParent());
			parent.removeChild(thisNode);
			thisNode.getParent().addChild(thisNode);
			long modDate = (System.currentTimeMillis());
			thisNode.setLastModifiedDate(modDate);
			parent.setLastModifiedDate(modDate);
			// check for same urlLinkName on this level
			if (thisNode.hasSiblingsWithLinkName(thisNode.getUrlLinkName())) {
				thisNode.setUrlLinkName(thisNode.getUrlLinkName() + "-0");
			}
			return thisNode.getDao();
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#moveViewComponentRight(java.lang.Integer)
	 */
	@Override
	protected ViewComponentValue handleMoveViewComponentRight(Integer viewComponentId) throws Exception {
		return moveComponentRight(viewComponentId);
	}

	/**
	 * 
	 * @param viewComponentId
	 * @return ViewComponentValue
	 * @throws Exception
	 */
	private ViewComponentValue moveComponentRight(Integer viewComponentId) throws UserException {
		try {
			ViewComponentHbm view = super.getViewComponentHbmDao().load(viewComponentId);
			ViewComponentHbm prev, next, firstChild, parent;
			if (view.getParent().getViewType() == 0) {
				// I'm moving a Unit that has been under root to a "under-unit" - so we have to reset showtype! (because it
				// is 3)
				view.setShowType((byte) 0);
			}
			prev = view.getPrevNode();
			next = view.getNextNode();
			firstChild = prev.getFirstChild();
			parent = view.getParent();
			if (prev == null) { throw new UserException("previous node cannot be null."); }
			prev.setNextNode(next);
			if (next != null) {
				next.setPrevNode(prev);
			}
			if (prev.isLeaf()) {
				prev.setFirstChild(view);
				view.setParentViewComponent(prev);

				view.setParentViewComponent(prev);
				view.setPrevNode(null);
				view.setNextNode(null);
			} else {
				firstChild.setPrevNode(view);
				view.setPrevNode(null);
				view.setNextNode(firstChild);

				prev.setFirstChild(view);
				view.setParentViewComponent(prev);
			}
			parent.removeChild(view);
			prev.addChild(view);
			view.setLastModifiedDate(System.currentTimeMillis());
			// check for same urlLinkName on this level
			if (view.hasSiblingsWithLinkName(view.getUrlLinkName())) {
				int id = 0;
				String tempText = "";
				boolean foundAnEmptyName = false;
				while (!foundAnEmptyName) {
					id++;
					tempText = view.getUrlLinkName() + "_" + id;
					if (!view.hasSiblingsWithLinkName(tempText)) {
						foundAnEmptyName = true;
					}
				}
				view.setUrlLinkName(tempText);
			}
			return view.getDao();
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#updateStatus4ViewComponent(de.juwimm.cms.vo.ViewComponentValue)
	 */
	@Override
	protected void handleUpdateStatus4ViewComponent(ViewComponentValue dao) throws Exception {
		try {
			ViewComponentHbm vc = super.getViewComponentHbmDao().load(dao.getViewComponentId());
			if (log.isDebugEnabled()) {
				log.debug("STATUS old " + vc.getStatus() + " new " + dao.getStatus());
			}
			vc.setDeployCommand(dao.getDeployCommand());
			vc.setStatus(dao.getStatus());
			vc.setLastModifiedDate(System.currentTimeMillis());
			vc.setUserLastModifiedDate(dao.getUserLastModifiedDate());
			if (vc.getStatus() == Constants.DEPLOY_STATUS_APPROVED && (vc.getViewType() == Constants.VIEW_TYPE_CONTENT || vc.getViewType() == Constants.VIEW_TYPE_UNIT)) {
				super.getContentHbmDao().setLatestContentVersionAsPublishVersion(new Integer(vc.getReference()));
			}
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * Checks if the ViewComponent is online and a link<br/> (needed for saving the old online-linkname on editing the linkname)
	 * 
	 * @param viewComponentValue
	 * @return true if viewComponentValue is a already published link
	 */
	private boolean isLinkAndOnline(ViewComponentValue viewComponentValue) {
		if (viewComponentValue.getOnline() != 1) return false;
		byte viewType = viewComponentValue.getViewType();
		return (viewType == Constants.VIEW_TYPE_EXTERNAL_LINK || viewType == Constants.VIEW_TYPE_INTERNAL_LINK || viewType == Constants.VIEW_TYPE_SYMLINK);
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#saveViewComponent(de.juwimm.cms.vo.ViewComponentValue)
	 */
	@Override
	protected ViewComponentValue handleSaveViewComponent(ViewComponentValue viewComponentValue) throws Exception {
		ViewComponentHbm viewComponent = null;
		try {
			try {
				viewComponent = super.getViewComponentHbmDao().load(viewComponentValue.getViewComponentId());
			} catch (Exception e) {
				throw new UserException(e.getMessage());
			}
			if (viewComponentValue.getDisplayLinkName().trim().equals("")) {
				throw new UserException("ViewComponentLinkNameIsEmpty");
			} else if (viewComponent.hasSiblingsWithLinkName(viewComponentValue.getUrlLinkName())) {
				int id = 0;
				String tempText = "";
				boolean foundAnEmptyName = false;
				while (!foundAnEmptyName) {
					id++;
					tempText = viewComponentValue.getUrlLinkName() + "_" + id;
					if (!viewComponent.hasSiblingsWithLinkName(tempText)) {
						foundAnEmptyName = true;
					}
				}
				viewComponentValue.setUrlLinkName(tempText);
			}
			if (viewComponentValue.getViewIndex() != null) {
				viewComponent.setViewIndex(viewComponentValue.getViewIndex());
			}
			if (viewComponentValue.getViewLevel() != null) {
				viewComponent.setViewLevel(viewComponentValue.getViewLevel());
			}
			viewComponent.setDisplaySettings(viewComponentValue.getDisplaySettings());
			if (this.isLinkAndOnline(viewComponentValue)) {
				// did the linkName change?
				if (!viewComponent.getDisplayLinkName().equals(viewComponentValue.getDisplayLinkName())) {
					// did the name change before now but after the last approval?
					if (viewComponent.getApprovedLinkName() == null) {
						viewComponent.setApprovedLinkName(viewComponent.getDisplayLinkName());
					}
					// old name is saved yet
					viewComponent.setDisplayLinkName(viewComponentValue.getDisplayLinkName());
				}
			} else {
				viewComponent.setDisplayLinkName(viewComponentValue.getDisplayLinkName());
			}
			viewComponent.setLinkDescription(viewComponentValue.getLinkDescription());
			viewComponent.setUrlLinkName(viewComponentValue.getUrlLinkName());
			viewComponent.setOnlineStart(viewComponentValue.getOnlineStart());
			viewComponent.setOnlineStop(viewComponentValue.getOnlineStop());
			viewComponent.setReference(viewComponentValue.getReference());
			viewComponent.setShowType(viewComponentValue.getShowType());
			viewComponent.setMetaData(viewComponentValue.getMetaData());
			viewComponent.setMetaDescription(viewComponentValue.getMetaDescription());
			if (viewComponentValue.getViewType() != 0) {
				viewComponent.setViewType(viewComponentValue.getViewType());
			}
			viewComponent.setVisible(viewComponentValue.isVisible());
			viewComponent.setSearchIndexed(viewComponentValue.isSearchIndexed());
			viewComponent.setXmlSearchIndexed(viewComponentValue.isXmlSearchIndexed());
			viewComponent.setLastModifiedDate(System.currentTimeMillis());
			// -- Indexing through Messaging
			try {
				if (log.isDebugEnabled()) log.debug("STARTING INDEXING AT: saveViewComponent");
				Integer contentId = null;
				if (viewComponent.getViewType() == Constants.VIEW_TYPE_SYMLINK) {
					ViewComponentHbm vclRef = super.getViewComponentHbmDao().load(new Integer(viewComponent.getReference()));
					contentId = new Integer(vclRef.getReference());
				} else if (viewComponent.getViewType() == Constants.VIEW_TYPE_CONTENT || viewComponent.getViewType() == Constants.VIEW_TYPE_UNIT) {
					contentId = new Integer(viewComponent.getReference());
				}
				if (contentId != null) {
					ContentHbm content = super.getContentHbmDao().load(contentId);
					content.setUpdateSearchIndex(true);
				}
			} catch (Exception exe) {
				log.error("Error occured", exe);
			}
			// -- Indexing
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
		return viewComponent.getDao();
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#getAllViewComponents4Status(java.lang.Integer, int)
	 */
	@Override
	protected ViewComponentValue[] handleGetAllViewComponents4Status(Integer viewDocumentId, int status) throws Exception {
		try {
			Vector<ViewComponentValue> vec = new Vector<ViewComponentValue>();
			Collection<ViewComponentHbm> coll = super.getViewComponentHbmDao().findByStatus(viewDocumentId, status);
			Iterator<ViewComponentHbm> it = coll.iterator();
			ViewComponentValue viewDao;
			ViewComponentHbm view;
			while (it.hasNext()) {
				view = it.next();
				if (!view.isRoot()) {
					viewDao = view.getDeployDao();
					viewDao.setPath2Unit(this.getParents4View(view));
					vec.addElement(viewDao);
				}
			}
			return vec.toArray(new ViewComponentValue[0]);
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * Get parents to a viewComponent unit, excluding the viewCcomponent, which has a unit.
	 * 
	 * @param viewComponent
	 * @return String with path to this viewComponent
	 */
	private String getParents4View(ViewComponentHbm viewComponent) {
		try {
			if (viewComponent.getParent().isRoot()) { return "\\"; }
		} catch (Exception ex) {
			return "\\";
		}
		Vector<ViewComponentHbm> vec = new Vector<ViewComponentHbm>();
		ViewComponentHbm parentView = viewComponent.getParent();

		while (parentView.getAssignedUnit() == null) {
			vec.addElement(parentView);
			parentView = parentView.getParent();
			try {
				if (parentView.isRoot()) {
					break;
				}
			} catch (Exception ex) {
				break;
			}
		}
		if (parentView.getAssignedUnit() != null) {
			vec.addElement(parentView);
		}
		StringBuffer sb = new StringBuffer("\\");

		for (int i = vec.size() - 1; i > -1; i--) {
			sb.append((vec.elementAt(i)).getUrlLinkName());
			if (i != 0) {
				sb.append("\\");
			}
		}
		sb.append("\\").append(viewComponent.getUrlLinkName());
		return sb.toString();
	}

	/**
	 * Returns all children of a ViewComponent, that contains a unit.
	 * 
	 * @param viewComponentId
	 *            The viewComponentId
	 * @return Array of ViewIdAndUnitIdValue
	 *         <ul>
	 *         <li>[i][0] contains ViewComponentId</li>
	 *         <li>[i][1] contains UnitId</li>
	 *         <li>[i][2] contains UnitName</li>
	 *         </ul>
	 * 
	 * @see de.juwimm.cms.remote.ViewServiceSpring#getAllViewComponentsWithUnits(java.lang.Integer)
	 */
	@Override
	protected ViewIdAndUnitIdValue[] handleGetAllViewComponentsWithUnits(Integer viewComponentId) throws Exception {
		try {
			ViewIdAndUnitIdValue[] retarr = null;
			ViewComponentHbm root = super.getViewComponentHbmDao().load(viewComponentId);
			Vector<ViewComponentHbm> vec = new Vector<ViewComponentHbm>(root.getAllChildrenWithUnits());
			Iterator it = vec.iterator();
			retarr = new ViewIdAndUnitIdValue[vec.size()];
			int i = 0;
			while (it.hasNext()) {
				ViewComponentHbm vcl = (ViewComponentHbm) it.next();
				retarr[i] = new ViewIdAndUnitIdValue();
				retarr[i].setViewComponentId(vcl.getViewComponentId());
				retarr[i].setUnitId(vcl.getAssignedUnit().getUnitId());
				retarr[i].setUnitName(vcl.getAssignedUnit().getName());
				i++;
			}
			return retarr;
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#getAllViewComponents4UnitAndStatus(java.lang.Integer, java.lang.Integer, int)
	 */
	@Override
	protected ViewComponentValue[] handleGetAllViewComponents4UnitAndStatus(Integer unitId, Integer viewDocumentId, int intStatus) throws Exception {
		try {
			Vector<ViewComponentValue> vec = new Vector<ViewComponentValue>();
			ViewComponentHbm view = super.getViewComponentHbmDao().find4Unit(unitId, viewDocumentId);
			this.getAllViewComponentsChildren4Status(view, vec, intStatus, unitId);
			return vec.toArray(new ViewComponentValue[0]);
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	private void getAllViewComponentsChildren4Status(ViewComponentHbm view, Vector<ViewComponentValue> vec, int status, Integer unitId) throws Exception {
		if (view.getStatus() == status && !view.isRoot()) {
			ViewComponentValue viewDao = view.getDeployDao();
			viewDao.setPath2Unit(this.getParents4View(view));
			vec.addElement(viewDao);
		}
		Iterator it = view.getChildren().iterator();
		while (it.hasNext()) {
			ViewComponentHbm vcl = (ViewComponentHbm) it.next();
			if (vcl.getAssignedUnit() == null || vcl.getAssignedUnit().getUnitId().equals(unitId)) {
				this.getAllViewComponentsChildren4Status(vcl, vec, status, unitId);
			}
		}
	}

	/**
	 * Returns all Names of ViewComponents with Units.<br>
	 * The returned name is the name as seen in the tree of the application.<br>
	 * [1] -> InfoText<br>
	 * [2] -> ViewComponentsId
	 * 
	 * @see de.juwimm.cms.remote.ViewServiceSpring#getAllChildrenNamesWithUnit(java.lang.Integer)
	 */
	@Override
	protected ViewIdAndInfoTextValue[] handleGetAllChildrenNamesWithUnit(Integer viewComponentId) throws Exception {
		try {
			ViewIdAndInfoTextValue[] retarr = null;
			ViewComponentHbm vcl = super.getViewComponentHbmDao().load(viewComponentId);
			Vector<ViewComponentHbm> vec = new Vector<ViewComponentHbm>(vcl.getAllChildrenWithUnits());
			retarr = new ViewIdAndInfoTextValue[vec.size()];
			for (int i = 0; i < vec.size(); i++) {
				retarr[i] = new ViewIdAndInfoTextValue();
				retarr[i].setInfoText(vec.get(i).getLinkDescription());
				retarr[i].setViewComponentId(vec.get(i).getViewComponentId());
			}
			return retarr;
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#getParents4ViewComponent(java.lang.Integer)
	 */
	@Override
	protected String[] handleGetParents4ViewComponent(Integer viewComponentId) throws Exception {
		try {
			ViewComponentHbm view = super.getViewComponentHbmDao().load(viewComponentId);
			Vector<Integer> vec = new Vector<Integer>();
			Vector<String> topDown = new Vector<String>();
			ViewComponentHbm parentView = view;
			while ((parentView = parentView.getParent()) != null) {
				vec.addElement(parentView.getViewComponentId());
			}
			for (int i = vec.size() - 1; i > -1; i--) {
				topDown.addElement(vec.elementAt(i).toString());
			}
			return topDown.toArray(new String[0]);
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * Returns all Units<br>
	 * This Method is been used by the JMX MBean Logfile for the logfile processing.
	 * 
	 * @todo This should refactored into a session bean for JMX only. It is NOT inside MDA model
	 * 
	 * @see de.juwimm.cms.remote.ViewServiceSpring#getAllUnits()
	 */
	@Override
	protected Collection handleGetAllUnits() throws UserException {
		try {
			return super.getUnitHbmDao().findAll();
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * Checks if the viewcomponent is a unit and gets moved left in tree from one unit into another
	 * 
	 * @return true if the viewcomponent is a unit and gets moved left in tree from one unit into another
	 * @throws UserException
	 *             if an error occurs (viewComponentId not valid)
	 * 
	 * @see de.juwimm.cms.remote.ViewServiceSpring#isUnitAndChangesParentUnitLeft(java.lang.Integer)
	 */
	@Override
	protected Boolean handleIsUnitAndChangesParentUnitLeft(Integer viewComponentId) throws Exception {
		try {
			ViewComponentHbm current = super.getViewComponentHbmDao().load(viewComponentId);
			if ((current == null) || (current.isRoot()) || (current.getAssignedUnit() == null)) { return Boolean.FALSE; }
			ViewComponentHbm parent = current.getParent();
			if (parent.isRoot()) { return Boolean.FALSE; }
			int parentUnitId = parent.getUnit4ViewComponent().intValue();
			int grandfatherUnitId = parent.getParent().getUnit4ViewComponent().intValue();
			return (new Boolean(grandfatherUnitId == parentUnitId));
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * Checks if the viewcomponent is a unit and gets moved right in tree from one unit into another
	 * 
	 * @return true if the viewcomponent is a unit and gets moved right in tree from one unit into another
	 * @throws UserException
	 *             if an error occurs (viewComponentId not valid)
	 * 
	 * @see de.juwimm.cms.remote.ViewServiceSpring#isUnitAndChangesParentUnitRight(java.lang.Integer)
	 */
	@Override
	protected Boolean handleIsUnitAndChangesParentUnitRight(Integer viewComponentId) throws Exception {
		try {
			ViewComponentHbm current = super.getViewComponentHbmDao().load(viewComponentId);
			if ((current == null) || (current.isRoot()) || (current.getAssignedUnit() == null)) { return Boolean.FALSE; }
			ViewComponentHbm parent = current.getParent();
			ViewComponentHbm previous = current.getPrevNode();
			if (previous == null) { return Boolean.FALSE; }
			int parentUnitId = parent.getUnit4ViewComponent().intValue();
			int previousUnitId = previous.getUnit4ViewComponent().intValue();
			return (new Boolean(parentUnitId == previousUnitId));
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	@Override
	protected SiteValue[] handleGetAllRelatedSites(Integer siteId) throws Exception {
		try {
			return getUserServiceSpring().getAllSites4CurrentUserWithRole(UserRights.SITE_ROOT);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new UserException(e.getMessage());
		}
	}

	/** @see de.juwimm.cms.remote.ViewServiceSpringBase#handleSearchXmlByUnit(java.lang.Integer, java.lang.Integer, java.lang.String, boolean) */
	@Override
	protected XmlSearchValue[] handleSearchXmlByUnit(Integer unitId, Integer viewDocumentId, String xpathQuery, boolean parentSearch) throws Exception {
		try {
			return searchengineService.searchXmlByUnit(unitId, viewDocumentId, xpathQuery, parentSearch);
		} catch (Exception e) {
			log.error("Error calling the SearchengineServiceHome / searchXmlByUnit", e);
			throw new UserException(e.getMessage(), e);
		}
	}

	/** @see de.juwimm.cms.remote.ViewServiceSpringBase#handleSearchXml(java.lang.Integer, java.lang.String) */
	@Override
	protected XmlSearchValue[] handleSearchXml(Integer siteId, String xpathQuery) throws Exception {
		try {
			return searchengineService.searchXML(siteId, xpathQuery);
		} catch (Exception e) {
			log.error("Error calling the SearchengineServiceHome / searchXml", e);
			throw new UserException(e.getMessage(), e);
		}
	}

	/**
	 * It sets to the site specified in the viewDocument.siteId this viewDocument as default view document for the site 
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected ViewDocumentValue handleSetDefaultViewDocument(String viewType, String language, Integer siteId) throws Exception {
		SiteHbm site = getSiteHbmDao().load(siteId);
		ViewDocumentHbm viewDocumentDefault = null;
		if (site.getDefaultViewDocument() != null && checkEquals(site.getDefaultViewDocument(), language, viewType)) {
			viewDocumentDefault = site.getDefaultViewDocument();
		} else {
			List<ViewDocumentHbm> viewDocuments = (List<ViewDocumentHbm>) getViewDocumentHbmDao().findAll(siteId);
			for (ViewDocumentHbm viewDocument : viewDocuments) {
				if (checkEquals(viewDocument, language, viewType)) {
					viewDocumentDefault = viewDocument;
					break;
				}
			}
			if (viewDocumentDefault == null) {
				//create
				viewDocumentDefault = ViewDocumentHbm.Factory.newInstance();
				viewDocumentDefault.setLanguage(language);
				viewDocumentDefault.setViewType(viewType);
				viewDocumentDefault.setSite(site);
				viewDocumentDefault = getViewDocumentHbmDao().create(viewDocumentDefault);
				getViewDocumentHbmDao().update(viewDocumentDefault);
			}
			site.setDefaultViewDocument(viewDocumentDefault);
			getSiteHbmDao().update(site);
		}
		return viewDocumentDefault.getDao();
	}

	private boolean checkEquals(ViewDocumentHbm viewDocument, String language, String viewType) {
		return viewDocument.getLanguage().equals(language) && viewDocument.getViewType().equals(viewType);
	}

	@Override
	protected void handleRemoveViewDocuments(Collection viewDocuments) throws Exception {
		for (Object o : viewDocuments) {
			ViewDocumentHbm vdh = (ViewDocumentHbm) o;
			getViewComponentHbmDao().remove(vdh.getViewComponent());
		}
		getViewDocumentHbmDao().remove(viewDocuments);
	}

	@Override
	protected ViewComponentValue[] handleMoveViewComponentsDown(Integer[] viewComponentsId) throws Exception {
		ViewComponentValue[] localValues = new ViewComponentValue[viewComponentsId.length];
		for (int i = 0; i < viewComponentsId.length; i++) {
			localValues[i] = moveComponentDown(viewComponentsId[i]);
		}
		return localValues;
	}

	@Override
	protected ViewComponentValue[] handleMoveViewComponentsLeft(Integer[] viewComponentsId) throws Exception {
		ViewComponentValue[] localValues = new ViewComponentValue[viewComponentsId.length];
		for (int i = 0; i < viewComponentsId.length; i++) {
			localValues[i] = moveComponentLeft(viewComponentsId[i]);
		}
		return localValues;
	}

	@Override
	protected ViewComponentValue[] handleMoveViewComponentsRight(Integer[] viewComponentsId) throws Exception {
		ViewComponentValue[] localValues = new ViewComponentValue[viewComponentsId.length];
		for (int i = 0; i < viewComponentsId.length; i++) {
			localValues[i] = moveComponentRight(viewComponentsId[i]);
		}
		return localValues;
	}

	@Override
	protected ViewComponentValue[] handleMoveViewComponentsUp(Integer[] viewComponentsId) throws Exception {
		ViewComponentValue[] localValues = new ViewComponentValue[viewComponentsId.length];
		for (int i = 0; i < viewComponentsId.length; i++) {
			localValues[i] = moveComponentUp(viewComponentsId[i]);
		}
		return localValues;
	}

	@Override
	protected Integer handleGetViewComponentChildrenNumber(Integer[] viewComponentsIds) throws Exception {
		return getNumberOfChildren(viewComponentsIds);
	}

	/**
	 * Return the number of children of the selected item + itself
	 * @param viewComponentsIds
	 * @return
	 */
	private Integer getNumberOfChildren(Integer[] viewComponentsIds) {
		int number = 0;
		for (Integer parentId : viewComponentsIds) {
			ViewComponentValue[] childrenVec = null;
			try {
				childrenVec = getViewComponentChildren(parentId);
			} catch (Exception e) {
				number++;
			}
			if (childrenVec != null) {
				Integer[] childrenIds = new Integer[childrenVec.length];

				for (int i = 0; i < childrenVec.length; i++) {
					childrenIds[i] = childrenVec[i].getViewComponentId();
				}
				number = number + getNumberOfChildren(childrenIds) + 1;
			}

		}

		return number;
	}

	@Override
	protected ViewComponentValue[] handleCopyViewComponentsToParent(Integer parentId, Integer[] viewComponentsIds, Integer position) throws Exception {

		ViewComponentValue[] values = new ViewComponentValue[viewComponentsIds.length];
		try {
			ViewComponentHbm parent = getViewComponentHbmDao().load(parentId);
			ViewComponentHbm firstChild;
			for (int i = 0; i < viewComponentsIds.length; i++) {
				firstChild = parent.getFirstChild();
				ViewComponentHbm oldViewComponent = getViewComponentHbmDao().load(viewComponentsIds[i]);
				ViewComponentHbm viewComponent = getViewComponentHbmDao().cloneViewComponent(oldViewComponent);
				parent.addChild(viewComponent);
				viewComponent.setParent(parent);

				if (firstChild != null) {
					viewComponent.setNextNode(firstChild);
					firstChild.setPrevNode(viewComponent);
					parent.setFirstChild(viewComponent);
				} else {
					parent.setFirstChild(viewComponent);
				}
				values[i] = viewComponent.getViewComponentDao();
			}

		} catch (Exception e) {
			log.error("Error at Copy view components" + e.getMessage());
			throw new UserException(e.getMessage());
		}
		return values;

	}

	@Override
	protected String handleGetViewComponentXmlComplete(Integer viewComponentId, String hostUrl, boolean withMedia) throws Exception {
		String retVal = "";
		//		File fle = File.createTempFile("edition_unit_export", ".xml.gz");
		//		FileOutputStream fout = new FileOutputStream(fle);
		//		GZIPOutputStream gzoudt = new GZIPOutputStream(fout);
		//		PrintStream out = new PrintStream(gzoudt, true, "UTF-8");
		//		EditionHbm edition = super.getEditionHbmDao().create("RETURNEDITION", rootViewComponentId, out, true);
		//		super.getEditionHbmDao().remove(edition);
		//		out.flush();
		//		out.close();
		//		out = null;
		//		return new FileInputStream(fle);
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(byteOut, true, "UTF-8");
		out.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		out.print("<site>\n");
		out.print("<hostUrl>" + hostUrl + "</hostUrl>\n");
		getViewComponentHbmDao().toXmlComplete(viewComponentId, true, null, true, 1, true, false, out);
		if (withMedia) {
			ViewComponentHbm viewComponent = getViewComponentHbmDao().load(viewComponentId);
			ContentHbm content = getContentHbmDao().load(Integer.parseInt(viewComponent.getReference()));
			ContentVersionHbm contentVersion = content.getLastContentVersion();
			String contentVersionText = contentVersion.getText();
			if (contentVersionText != null) {
				Document doc = XercesHelper.string2Dom(contentVersionText);
				getMediaXML(doc, out, "picture", "description");
				getMediaXML(doc, out, "document", "src");
			}
		}
		out.print("</site>");
		retVal = byteOut.toString("UTF-8");
		return retVal;
	}

	/**
	 * 
	 * @param doc
	 * @param out
	 * @param tagString can be picture or document
	 * @param attribute description for picture or src for document
	 * @throws Exception
	 */
	private void getMediaXML(Document doc, PrintStream out, String tagString, String attribute) throws Exception {
		try {
			Iterator it = XercesHelper.findNodes(doc, "//" + tagString);
			while (it.hasNext()) {
				Node node = (Node) it.next();
				int itemId = Integer.parseInt(node.getAttributes().getNamedItem(attribute).getNodeValue());
				if (itemId != 0) {
					if (tagString.equals("picture")) {
						PictureHbm picture = getPictureHbmDao().load(itemId);
						out.print(picture.toXml(0));
					} else if (tagString.equals("document")) {
						out.print(getDocumentHbmDao().toXml(itemId, 0));
					}
				}
			}

		} catch (Exception e) {
			if (log.isDebugEnabled()) log.debug("Error at getting " + tagString + " xml");
			throw new UserException(e.getMessage());
		}
	}

	@Override
	protected ViewComponentValue handleCopyViewComponentToParentFromXml(Integer parentId, String xmlString, boolean withMedia, boolean withChildren, Integer unitId) throws Exception {

		ViewComponentHbm parent = getViewComponentHbmDao().load(parentId);
		ViewComponentHbm firstChild = parent.getFirstChild();
		ViewComponentHbm viewComponent = getViewComponentHbmDao().createFromXml(parentId, xmlString, withChildren);
		parent.addChild(viewComponent);
		viewComponent.setParent(parent);

		if (firstChild != null) {
			viewComponent.setNextNode(firstChild);
			firstChild.setPrevNode(viewComponent);
			parent.setFirstChild(viewComponent);
		} else {
			parent.setFirstChild(viewComponent);
		}

		return viewComponent.getDao();
	}

	@Override
	protected InputStream handleExportViewComponent(Integer viewComponentId) throws Exception {
		//		try {
		//			File fle = File.createTempFile("edition_unit_export", ".xml.gz");
		//			FileOutputStream fout = new FileOutputStream(fle);
		//			GZIPOutputStream gzoudt = new GZIPOutputStream(fout);
		//			PrintStream out = new PrintStream(gzoudt, true, "UTF-8");
		//			EditionHbm edition = super.getEditionHbmDao().create("RETURNEDITION", rootViewComponentId, out, true);
		//			super.getEditionHbmDao().remove(edition);
		//			out.flush();
		//			out.close();
		//			out = null;
		//			return new FileInputStream(fle);
		//		} catch (Exception e) {
		//			log.error("Could not export edition unit", e);
		//			throw new UserException(e.getMessage());
		//		}
		File fle = File.createTempFile("view_component_export", ".xml.gz");
		FileOutputStream fout = new FileOutputStream(fle);
		GZIPOutputStream gzoudt = new GZIPOutputStream(fout);
		PrintStream out = new PrintStream(gzoudt, true, "UTF-8");
		out.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		out.print("<site>\n");
		out.print("<hostUrl>" + Constants.URL_HOST + "</hostUrl>\n");
		getViewComponentHbmDao().toXmlComplete(viewComponentId, true, null, true, 1, true, false, out);
		ViewComponentHbm viewComponent = getViewComponentHbmDao().load(viewComponentId);
		ContentHbm content = getContentHbmDao().load(Integer.parseInt(viewComponent.getReference()));
		ContentVersionHbm contentVersion = content.getLastContentVersion();
		String contentVersionText = contentVersion.getText();
		if (contentVersionText != null) {
			Document doc = XercesHelper.string2Dom(contentVersionText);
			getMediaXML(doc, out, "picture", "description");
			getMediaXML(doc, out, "document", "src");
		}
		out.print("</site>");
		out.flush();
		out.close();
		out = null;
		return new FileInputStream(fle);
	}

}
