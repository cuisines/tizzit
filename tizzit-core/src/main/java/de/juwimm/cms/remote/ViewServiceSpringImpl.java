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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.ejb.CreateException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.tizzit.util.XercesHelper;
import org.tizzit.util.xml.XMLWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.XMLFilter;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.components.model.AddressHbm;
import de.juwimm.cms.components.model.AddressHbmImpl;
import de.juwimm.cms.components.model.PersonHbm;
import de.juwimm.cms.components.model.PersonHbmImpl;
import de.juwimm.cms.components.model.TalktimeHbm;
import de.juwimm.cms.components.model.TalktimeHbmImpl;
import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.model.AccessRoleHbm;
import de.juwimm.cms.model.AccessRoles2ViewComponentsHbm;
import de.juwimm.cms.model.ContentHbm;
import de.juwimm.cms.model.ContentVersionHbm;
import de.juwimm.cms.model.DocumentHbm;
import de.juwimm.cms.model.DocumentHbmImpl;
import de.juwimm.cms.model.PictureHbm;
import de.juwimm.cms.model.PictureHbmImpl;
import de.juwimm.cms.model.SequenceHbmDao;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.model.ViewComponentHbm;
import de.juwimm.cms.model.ViewComponentHbmDao;
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
import de.juwimm.cms.search.beans.SearchengineService;
import de.juwimm.cms.search.vo.XmlSearchValue;
import de.juwimm.cms.util.SmallSiteConfigReader;
import de.juwimm.cms.vo.AccessRoleValue;
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
	private static Log log = LogFactory.getLog(ViewServiceSpringImpl.class);

	@Autowired
	private SearchengineService searchengineService;
	@Autowired
	private SequenceHbmDao sequenceHbmDao;

	private Hashtable<Integer, Integer> loginPagesRealmsSimplePw = null;
	private Hashtable<Integer, Integer> loginPagesRealmsJdbc = null;
	private Hashtable<Integer, Integer> loginPagesRealmsLdap = null;
	private Hashtable<Integer, Integer> loginPagesRealmsJaas = null;

	private Hashtable<Integer, Integer> mappingRealmsSimplePw = null;
	private Hashtable<Integer, Integer> mappingRealmsJdbc = null;
	private Hashtable<Integer, Integer> mappingRealmsLdap = null;
	private Hashtable<Integer, Integer> mappingRealmsJaas = null;
	private Hashtable<Integer, Integer> loginPagesRealm2vc = null;

	private Hashtable<Long, Long> mappingPersons = null;

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
			if (isInUse) {
				throw new UserException("VCINUSE");
			}
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
			if (!node.isLeaf()) {
				throw new UserException("node is not a leaf.");
			}
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
			if (view.isLeaf()) {
				throw new UserException("node is a leaf.");
			}
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
			if (view.getAssignedUnit() != null) {
				return view.getAssignedUnit().getUnitId();
			}
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
			if (view.getAssignedUnit() != null) {
				return view.getViewComponentId();
			}
			while (view.getAssignedUnit() == null && !view.isRoot()) {
				view = view.getParent();
				if (view.getAssignedUnit() != null) {
					return view.getViewComponentId();
				}
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
			return viewComponentValue;
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.ViewServiceSpring#getViewComponent4Unit(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	protected ViewComponentValue handleGetViewComponent4Unit(Integer unitId, Integer viewDocumentId) throws Exception {
		ViewComponentValue vcd = null;
		try {
			ViewComponentHbm view = getViewComponentHbmDao().find4Unit(unitId, viewDocumentId);
			if (view != null) vcd = view.getDao(-1);
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
		if (ret) {
			return prevOne;
		}
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
			if (view.getPrevNode() == null) {
				throw new UserException("Node is already first child.");
			}
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
			if (view.getNextNode() == null) {
				throw new UserException("Node is already last child.");
			}
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
			if (prev == null) {
				throw new UserException("previous node cannot be null.");
			}
			firstChild = prev.getFirstChild();
			parent = view.getParent();
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
			vc.setOnline(dao.getOnline());
			vc.setOnlineStart(dao.getOnlineStart());
			vc.setStatus(dao.getStatus());
			vc.setLastModifiedDate(System.currentTimeMillis());
			vc.setUserLastModifiedDate(dao.getUserLastModifiedDate());
			SiteHbm site = vc.getViewDocument().getSite();
			boolean liveDeploy = false;
			try {
				SmallSiteConfigReader cfg = new SmallSiteConfigReader(site);
				if (cfg != null) {
					liveDeploy = cfg.getConfigElementValue("liveServer/liveDeploymentActive").equalsIgnoreCase("1");
				}
			} catch (Exception ex) {
				log.warn("could not read siteConfig of site: " + site.getName(), ex);
			}
			if (liveDeploy && vc.getStatus() == Constants.DEPLOY_STATUS_FOR_DEPLOY && (vc.getViewType() == Constants.VIEW_TYPE_CONTENT || vc.getViewType() == Constants.VIEW_TYPE_UNIT)) {
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
			if (viewComponent.getParent().isRoot()) {
				return "\\";
			}
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
	protected Integer[] handleGetParents4ViewComponent(Integer viewComponentId) throws Exception {
		try {
			ViewComponentHbm view = super.getViewComponentHbmDao().load(viewComponentId);
			Vector<Integer> vec = new Vector<Integer>();
			Vector<Integer> topDown = new Vector<Integer>();
			ViewComponentHbm parentView = view;
			while ((parentView = parentView.getParent()) != null) {
				vec.addElement(parentView.getViewComponentId());
			}
			for (int i = vec.size() - 1; i > -1; i--) {
				topDown.addElement(vec.elementAt(i));
			}
			return topDown.toArray(new Integer[0]);
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
			if ((current == null) || (current.isRoot()) || (current.getAssignedUnit() == null)) {
				return Boolean.FALSE;
			}
			ViewComponentHbm parent = current.getParent();
			if (parent.isRoot()) {
				return Boolean.FALSE;
			}
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
			if ((current == null) || (current.isRoot()) || (current.getAssignedUnit() == null)) {
				return Boolean.FALSE;
			}
			ViewComponentHbm parent = current.getParent();
			ViewComponentHbm previous = current.getPrevNode();
			if (previous == null) {
				return Boolean.FALSE;
			}
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
		viewComponentsIds = removeSelectedChildrenForCount(viewComponentsIds);
		return getNumberOfChildren(viewComponentsIds);
	}

	/**
	 * For accurate counting of pages in selected nodes,
	 * removes from the array the selected children of the selected nodes
	 * @param viewComponentsIds
	 * @return
	 */
	private Integer[] removeSelectedChildrenForCount(Integer[] viewComponentsIds) {
		Hashtable<Integer, Boolean> isValidForCount = new Hashtable<Integer, Boolean>();
		for (Integer id : viewComponentsIds) {
			try {
				ArrayList<Integer> children = getAllChildren(new Integer[] {id});
				for (Integer val : viewComponentsIds) {
					if (children.contains(val) && ((val.intValue()) != (id.intValue()))) {
						isValidForCount.put(val, false);
					}
				}
			} catch (Exception e) {
				if (log.isWarnEnabled()) log.warn("Error in removeSelectedChildrenForCount");
			}
		}
		ArrayList<Integer> values = new ArrayList<Integer>();
		for (Integer id : viewComponentsIds) {
			if (isValidForCount.get(id) == null) {
				values.add(id);
			}
		}
		Integer[] a = new Integer[values.size()];
		a = values.toArray(a);
		return a;
	}

	private ArrayList<Integer> getAllChildren(Integer[] viewComponentsIds) {
		ArrayList<Integer> children = new ArrayList<Integer>();
		for (Integer parentId : viewComponentsIds) {
			ViewComponentValue[] childrenVec = null;
			try {
				childrenVec = getViewComponentChildren(parentId);
			} catch (Exception e) {
				children.add(parentId);
			}
			if (childrenVec != null) {
				Integer[] childrenIds = new Integer[childrenVec.length];

				for (int i = 0; i < childrenVec.length; i++) {
					childrenIds[i] = childrenVec[i].getViewComponentId();
					children.add(childrenVec[i].getViewComponentId());
				}
				ArrayList<Integer> list = getAllChildren(childrenIds);
				for (Integer tempVal : list) {
					if (!children.contains(tempVal)) {
						children.add(tempVal);
					}
				}
			}
		}
		return children;
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
			int parentUnitId = getUnitForViewComponent(parentId);
			ViewComponentHbm firstChild;
			for (int i = 0; i < viewComponentsIds.length; i++) {
				firstChild = parent.getFirstChild();
				ViewComponentHbm oldViewComponent = getViewComponentHbmDao().load(viewComponentsIds[i]);
				ContentHbm content = getContentHbmDao().load(Integer.parseInt(oldViewComponent.getReference()));
				ContentVersionHbm lastContentVersion = content.getLastContentVersion();
				int childUnitId = getUnitForViewComponent(oldViewComponent.getViewComponentId());
				Hashtable<Integer, Integer> picIds = null;
				Hashtable<Integer, Integer> docIds = null;
				Hashtable<Long, Long> personIds = null;
				UnitHbm newUnit = getUnitHbmDao().load(parentUnitId);
				if (parentUnitId != childUnitId) {
					picIds = clonePictures(newUnit, lastContentVersion);
					docIds = cloneDocuments(newUnit, lastContentVersion);
					personIds = clonePersons(newUnit, lastContentVersion, picIds);

				}
				ViewComponentHbm viewComponent = getViewComponentHbmDao().cloneViewComponent(oldViewComponent, picIds, docIds, personIds, parentUnitId);
				viewComponent.setStatus(Constants.DEPLOY_STATUS_EDITED);
				viewComponent.setOnline((byte) 0);
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

	private Hashtable<Long, Long> clonePersons(UnitHbm newUnit, ContentVersionHbm lastContentVersion, Hashtable<Integer, Integer> picIds) {
		Hashtable<Long, Long> personsIds = new Hashtable<Long, Long>();
		String text = lastContentVersion.getText();
		try {
			Document doc = XercesHelper.string2Dom(text);
			Iterator it = XercesHelper.findNodes(doc, "//aggregation");
			while (it.hasNext()) {
				Node node = (Node) it.next();
				Iterator itInclude = XercesHelper.findNodes(node, "./include");
				while (itInclude.hasNext()) {
					Node nodeInclude = (Node) itInclude.next();
					Iterator itIncludePerson = XercesHelper.findNodes(nodeInclude, "./include");
					while (itIncludePerson.hasNext()) {
						Node nodePerson = (Node) itIncludePerson.next();
						String type = nodePerson.getAttributes().getNamedItem("type").getNodeValue();
						if (type.equals("person")) {
							Long personId = Long.parseLong(nodePerson.getAttributes().getNamedItem("id").getNodeValue());
							PersonHbm person = getPersonHbmDao().load(personId);
							Integer pictureId = null;
							if (person.getImageId() != null) {
								pictureId = picIds.get(person.getImageId());
							}
							PersonHbm newPerson = getPersonHbmDao().clonePerson(person, newUnit, pictureId);
							personsIds.put(personId, newPerson.getPersonId());
						}
					}
				}
			}
		} catch (Exception e) {
			if (log.isDebugEnabled()) log.debug("Error at cloning pictures");
		}
		return personsIds;
	}

	/**
	 * 
	 * @param unitId
	 * @param lastContentVersion
	 * @return
	 */
	private Hashtable<Integer, Integer> clonePictures(UnitHbm newUnit, ContentVersionHbm lastContentVersion) {
		Hashtable<Integer, Integer> pictureIds = new Hashtable<Integer, Integer>();
		String text = lastContentVersion.getText();
		try {
			Document content = XercesHelper.string2Dom(text);
			Iterator cvIt = XercesHelper.findNodes(content, "//picture");
			while (cvIt.hasNext()) {
				Element el = (Element) cvIt.next();
				String value = el.getAttribute("description");
				Integer oldId = Integer.parseInt(value);
				Integer newId = getPictureHbmDao().clonePicture(oldId, newUnit);
				pictureIds.put(oldId, newId);
			}
		} catch (Exception e) {
			if (log.isDebugEnabled()) log.debug("Error at cloning pictures");
		}
		return pictureIds;
	}

	/**
	 * Clones the documents and sets the unit sent as parameter
	 * @param unitId
	 * @param lastContentVersion
	 * @return
	 */
	private Hashtable<Integer, Integer> cloneDocuments(UnitHbm newUnit, ContentVersionHbm lastContentVersion) {
		Hashtable<Integer, Integer> documentsIds = new Hashtable<Integer, Integer>();
		String text = lastContentVersion.getText();
		try {
			Document content = XercesHelper.string2Dom(text);
			Iterator cvIt = XercesHelper.findNodes(content, "//document");
			while (cvIt.hasNext()) {
				Element el = (Element) cvIt.next();
				Integer oldId = Integer.parseInt(el.getAttribute("src"));
				Integer newId = getDocumentHbmDao().cloneDocument(oldId, newUnit);
				documentsIds.put(oldId, newId);
			}
		} catch (Exception e) {
			if (log.isDebugEnabled()) log.debug("Error at cloning documents");
		}
		return documentsIds;
	}

	@Override
	protected String handleGetViewComponentXmlComplete(Integer viewComponentId, String hostUrl, boolean withMedia) throws Exception {
		String retVal = "";
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(byteOut, true, "UTF-8");
		out.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		out.print("<site>\n");
		out.print("<hostUrl>" + hostUrl + "</hostUrl>\n");
		ViewComponentHbm viewComponent = getViewComponentHbmDao().load(viewComponentId);
		// TODO: depth 0 or 1 ???
		getViewComponentHbmDao().toXml(viewComponent, null, true, true, true, true, 0, true, false, out);
		//		getViewComponentHbmDao().toXmlComplete(viewComponentId, true, null, true, 1, true, false, out);
		if (withMedia) {
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
	protected InputStream handleExportViewComponent(Integer viewComponentId) throws Exception {
		File fle = File.createTempFile("view_component_export", ".xml.gz");
		FileOutputStream fout = new FileOutputStream(fle);
		GZIPOutputStream gzoudt = new GZIPOutputStream(fout);
		PrintStream out = new PrintStream(gzoudt, true, "UTF-8");
		out.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		out.print("<site>\n");
		out.print("<hostUrl>" + Constants.URL_HOST + "</hostUrl>\n");
		ViewComponentHbm viewComponent = getViewComponentHbmDao().load(viewComponentId);
		// TODO: depth 0 or 1 ???
		getViewComponentHbmDao().toXml(viewComponent, null, true, true, true, true, 0, true, false, out);

		ContentHbm content = getContentHbmDao().load(Integer.parseInt(viewComponent.getReference()));
		ContentVersionHbm contentVersion = content.getLastContentVersion();
		String contentVersionText = contentVersion.getText();
		if (contentVersionText != null) {
			Document doc = XercesHelper.string2Dom(contentVersionText);
			getMediaXML(doc, out, "picture", "description");
			getMediaXML(doc, out, "document", "src");
			getAggregationXML(doc, out);
		}
		out.print("</site>");
		out.flush();
		out.close();
		out = null;
		return new FileInputStream(fle);
	}

	/**
	 * 
	 * @param doc
	 * @param out
	 */
	private void getAggregationXML(Document doc, PrintStream out) throws Exception {
		try {
			Iterator it = XercesHelper.findNodes(doc, "//aggregation");
			while (it.hasNext()) {
				Node node = (Node) it.next();
				Iterator itInclude = XercesHelper.findNodes(node, "./include");
				while (itInclude.hasNext()) {
					Node nodeInclude = (Node) itInclude.next();
					Iterator itIncludePerson = XercesHelper.findNodes(nodeInclude, "./include");
					while (itIncludePerson.hasNext()) {
						Node nodePerson = (Node) itIncludePerson.next();
						String type = nodePerson.getAttributes().getNamedItem("type").getNodeValue();
						if (type.equals("person")) {
							Long personId = Long.parseLong(nodePerson.getAttributes().getNamedItem("id").getNodeValue());
							PersonHbm person = getPersonHbmDao().load(personId);
							out.println(person.toXmlRecursive(1));
							if (person.getImageId() != null) {
								PictureHbm pic = getPictureHbmDao().load(person.getImageId());
								out.println(pic.toXml(0));
							}
						}
					}
				}

			}

		} catch (Exception e) {
			if (log.isDebugEnabled()) log.debug("Error at getting  aggregation xml");
			throw new UserException(e.getMessage());
		}
	}

	@Override
	protected ViewComponentValue handleImportViewComponent(Integer parentId, InputStream xmlFile, boolean withMedia, boolean withChildren, Integer unitId, boolean useNewIds, Integer siteId, Integer fulldeploy) throws Exception {
		String tmpFileName = "";
		try {
			tmpFileName = this.storeSiteFile(xmlFile);
			if (log.isInfoEnabled()) log.info("-------------->importFile saved.");
		} catch (IOException e) {
			log.warn("Unable to copy received inputstream: " + e.getMessage(), e);
		}
		ViewComponentHbm parent = getViewComponentHbmDao().load(parentId);
		ViewComponentHbm firstChild = parent.getFirstChild();
		File preparsedXMLfile = null;
		XMLFilter filter = new XMLFilterImpl(XMLReaderFactory.createXMLReader());
		preparsedXMLfile = File.createTempFile("edition_import_preparsed_", ".xml");
		if (log.isDebugEnabled()) log.debug("preparsedXMLfile: " + preparsedXMLfile.getAbsolutePath());
		XMLWriter xmlWriter = new XMLWriter(new OutputStreamWriter(new FileOutputStream(preparsedXMLfile)));
		filter.setContentHandler(new de.juwimm.cms.util.EditionBlobContentHandler(xmlWriter, preparsedXMLfile));
		InputSource saxIn = null;
		try {
			try {
				saxIn = new InputSource(new GZIPInputStream(new FileInputStream(tmpFileName)));
			} catch (Exception exe) {
				saxIn = new InputSource(new BufferedReader(new FileReader(tmpFileName)));
			}
		} catch (FileNotFoundException exe) {
			if (log.isDebugEnabled()) log.error("Error at creating InputSource in paste");
		}
		filter.parse(saxIn);
		xmlWriter.flush();
		xmlWriter = null;
		filter = null;
		System.gc();
		InputSource domIn = new InputSource(new BufferedInputStream(new FileInputStream(preparsedXMLfile)));
		org.w3c.dom.Document doc = XercesHelper.inputSource2Dom(domIn);
		Hashtable<Integer, Integer> picIds = null;
		Hashtable<Integer, Integer> docIds = null;
		if (withMedia) {
			picIds = importPictures(unitId, doc, preparsedXMLfile, useNewIds);
			docIds = importDocuments(unitId, doc, preparsedXMLfile, useNewIds);
		}
		/**import persons */
		mappingPersons = new Hashtable<Long, Long>();

		Iterator it = XercesHelper.findNodes(doc, "//viewcomponent");
		while (it.hasNext()) {
			Node nodeViewComponent = (Node) it.next();
			Integer oldunitId = new Integer(((Element) nodeViewComponent).getAttribute("unitId"));
			if (oldunitId.intValue() != unitId.intValue()) {
				importPersons(unitId, doc, useNewIds, picIds);
				/**import only if not in the same unit*/
			}
		}
		ViewComponentHbm viewComponent = createViewComponentFromXml(parentId, doc, withChildren, picIds, docIds, useNewIds, siteId, fulldeploy, unitId);
		//importRealmsForViewComponent(doc, viewComponent, 1);
		/**import realms*/

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

	private Hashtable<Integer, Integer> importPersons(Integer unitId, Document doc, boolean useNewIds, Hashtable<Integer, Integer> picIds) {
		UnitHbm unit = getUnitHbmDao().load(unitId);
		Iterator it = XercesHelper.findNodes(doc, "//person");
		while (it.hasNext()) {
			try {
				Element elPerson = (Element) it.next();

				createPersonHbm(unit, elPerson, useNewIds, picIds);
			} catch (Exception e) {
				if (log.isWarnEnabled()) log.warn("Person not created correctly at import site");
			}
		}
		return null;
	}

	private TalktimeHbm createTalktimeHbm(Element ael, boolean useNewIds) {
		TalktimeHbm talktime = new TalktimeHbmImpl();
		try {
			talktime.setTalkTimes(XercesHelper.nodeList2string(XercesHelper.findNode(ael, "./talkTimes").getChildNodes()));
		} catch (Exception exe) {
		}
		talktime.setTalkTimeType(getNVal(ael, "talkTimeType"));
		if (!useNewIds) {
			Long id = Long.parseLong(ael.getAttribute("id"));
			talktime.setTalkTimeId(id);
		}
		talktime = getTalktimeHbmDao().create(talktime);
		return talktime;
	}

	private PersonHbm createPersonHbm(UnitHbm unit, Element ael, boolean useNewIds, Hashtable<Integer, Integer> picIds) throws Exception {
		PersonHbm person = new PersonHbmImpl();
		try {
			Integer imageId = Integer.parseInt(ael.getAttribute("imageid"));
			if (picIds != null) {
				Integer newPicId = picIds.get(imageId);
				person.setImageId(newPicId);
			} else {
				person.setImageId(imageId);
			}
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
			person.getUnits().add(unit);
			person = getPersonHbmDao().create(person);
		} catch (Exception exe) {
			log.warn("Error setting values: ", exe);
		}
		try {
			if (log.isDebugEnabled()) log.debug("looking for addresses to import for person " + person.getPersonId());
			Iterator itAdr = XercesHelper.findNodes(ael, "./address");
			while (itAdr.hasNext()) {
				Element adrEl = (Element) itAdr.next();
				if (log.isDebugEnabled()) log.debug("found address to import");
				AddressHbm local = createAddressHbm(adrEl, useNewIds);
				person.addAddress(local);
			}
		} catch (Exception exe) {
			if (log.isWarnEnabled()) log.warn("Error importing addresses: ", exe);
			throw new CreateException(exe.getMessage());
		}
		try {
			if (log.isDebugEnabled()) log.debug("looking for talktimes to import for person " + person.getPersonId());
			Iterator itTTimes = XercesHelper.findNodes(ael, "./talktime");
			while (itTTimes.hasNext()) {
				Element elm = (Element) itTTimes.next();
				if (log.isDebugEnabled()) log.debug("found talktime to import");
				TalktimeHbm local = createTalktimeHbm(elm, useNewIds);
				person.addTalktime(local);
			}
		} catch (Exception exe) {
			if (log.isWarnEnabled()) log.warn("Error importing talktimes: ", exe);
			throw new Exception(exe.getMessage());
		}
		mappingPersons.put(Long.parseLong(ael.getAttribute("id")), person.getPersonId());
		return person;
	}

	private String getNVal(Element ael, String nodeName) {
		String tmp = XercesHelper.getNodeValue(ael, "./" + nodeName);
		if (tmp.equals("null") || tmp.equals("")) {
			return null;
		}
		return tmp;
	}

	private AddressHbm createAddressHbm(Element ael, boolean useNewIds) {
		AddressHbm address = new AddressHbmImpl();
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
		address = getAddressHbmDao().create(address);
		return address;
	}

	private String storeSiteFile(InputStream in) throws IOException {
		String dir = getTizzitPropertiesBeanSpring().getDatadir() + File.separatorChar + "editions";
		File fDir = new File(dir);
		fDir.mkdirs();
		File storedEditionFile = File.createTempFile("edition_import_", ".xml.gz", fDir);
		FileOutputStream out = new FileOutputStream(storedEditionFile);
		IOUtils.copyLarge(in, out);
		IOUtils.closeQuietly(out);
		IOUtils.closeQuietly(in);
		return storedEditionFile.getAbsolutePath();
	}

	/**
	 * Used for import single view component
	 * @param parentId
	 * @param doc
	 * @param withChildren
	 * @param picIds
	 * @param docIds
	 * @return 
	 */
	private ViewComponentHbm createViewComponentFromXml(Integer parentId, Document doc, boolean withChildren, Hashtable<Integer, Integer> picIds, Hashtable<Integer, Integer> docIds, boolean useNewIds, Integer siteId, Integer fulldeploy, Integer newUnitId) {
		ViewComponentHbm viewComponent = ViewComponentHbm.Factory.newInstance();
		ViewComponentHbm parent = getViewComponentHbmDao().load(parentId);

		try {
			Integer id = sequenceHbmDao.getNextSequenceNumber("viewcomponent.view_component_id");
			viewComponent.setViewComponentId(id);
		} catch (Exception e) {
			log.error("Error creating/setting primary key", e);
		}
		Iterator it = XercesHelper.findNodes(doc, "//viewcomponent");
		try {
			while (it.hasNext()) {
				Node nodeViewComponent = (Node) it.next();
				Integer vcId = new Integer(((Element) nodeViewComponent).getAttribute("id"));
				Node nodeRealm = XercesHelper.findNode(nodeViewComponent, "realm2viewComponent");
				/**import realms*/
				importRealms(nodeViewComponent, siteId, useNewIds);
				importViewComponentPictures(nodeViewComponent,viewComponent);
				importViewComponentDocuments(nodeViewComponent,viewComponent);
				String linkName = XercesHelper.getNodeValue(nodeViewComponent, "//linkName");
				String approvedLinkName = XercesHelper.getNodeValue(nodeViewComponent, "//approvedLinkName");
				String statusInfo = XercesHelper.getNodeValue(nodeViewComponent, "//statusInfo");
				String urlLinkName = XercesHelper.getNodeValue(nodeViewComponent, "//urlLinkName");
				viewComponent.setDisplayLinkName(linkName);
				viewComponent.setApprovedLinkName(approvedLinkName);
				viewComponent.setLinkDescription(statusInfo);
				viewComponent.setUrlLinkName(urlLinkName);
				byte viewType = new Byte(XercesHelper.getNodeValue(nodeViewComponent, "//viewType")).byteValue();
				boolean visible = Boolean.valueOf(XercesHelper.getNodeValue(nodeViewComponent, "//visible")).booleanValue();
				viewComponent.setMetaData(XercesHelper.getNodeValue(nodeViewComponent, "./metaKeywords"));
				viewComponent.setMetaDescription(XercesHelper.getNodeValue(nodeViewComponent, "./metaDescription"));
				String onlineStart = XercesHelper.getNodeValue(nodeViewComponent, "./onlineStart");
				if (!onlineStart.equals("")) {
					if (log.isDebugEnabled()) log.debug("OnlineStart: " + onlineStart);
					viewComponent.setOnlineStart(Long.parseLong(onlineStart));
				}
				String onlineStop = XercesHelper.getNodeValue(nodeViewComponent, "./onlineStop");
				if (!onlineStop.equals("")) {
					if (log.isDebugEnabled()) log.debug("OnlineStop: " + onlineStop);
					viewComponent.setOnlineStop(Long.parseLong(onlineStop));
				}
				viewComponent.setViewLevel(XercesHelper.getNodeValue(nodeViewComponent, "./viewLevel"));
				viewComponent.setViewIndex(XercesHelper.getNodeValue(nodeViewComponent, "./viewIndex"));
				viewComponent.setDisplaySettings(Byte.parseByte(XercesHelper.getNodeValue(nodeViewComponent, "./displaySettings")));
				viewComponent.setShowType(Byte.parseByte(XercesHelper.getNodeValue(nodeViewComponent, "./showType")));
				viewComponent.setViewType(viewType);
				viewComponent.setVisible(visible);
				viewComponent.setSearchIndexed(Boolean.valueOf(XercesHelper.getNodeValue(nodeViewComponent, "./searchIndexed")).booleanValue());
				viewComponent.setXmlSearchIndexed(Boolean.valueOf(XercesHelper.getNodeValue(nodeViewComponent, "./xmlSearchIndexed")).booleanValue());
				byte status = Constants.DEPLOY_STATUS_EDITED;
				viewComponent.setStatus(status);
				viewComponent.setOnline((byte) 0);
				Element cnde = (Element) XercesHelper.findNode(nodeViewComponent, "//content");
				if (cnde != null) {
					ContentHbm content = getContentHbmDao().createFromXml(cnde, false, false, picIds, docIds, mappingPersons, newUnitId);
					viewComponent.setReference(content.getContentId().toString());
				}

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
						RealmJdbcHbm sqlrealm = super.getRealmJdbcHbmDao().load(mappingRealmsJdbc.get(id));
						Realm2viewComponentHbm tempRealm = createTempRealm(viewComponent, neededrole, sqlrealm, null, null, null);
						Realm2viewComponentHbm r = super.getRealm2viewComponentHbmDao().create(tempRealm);
						viewComponent.setRealm2vc(r);
					} else {
						relNode = XercesHelper.findNode(nodeRealm, "simplePwRealmId");
						if (relNode != null) {
							Integer id = new Integer(XercesHelper.getNodeValue(relNode));
							RealmSimplePwHbm realm = super.getRealmSimplePwHbmDao().load(mappingRealmsSimplePw.get(id));
							Realm2viewComponentHbm tempRealm = createTempRealm(viewComponent, neededrole, null, realm, null, null);
							Realm2viewComponentHbm r = super.getRealm2viewComponentHbmDao().create(tempRealm);
							viewComponent.setRealm2vc(r);
						} else {
							relNode = XercesHelper.findNode(nodeRealm, "ldapRealmId");
							if (relNode != null) {
								Integer id = new Integer(XercesHelper.getNodeValue(relNode));
								RealmLdapHbm realm = super.getRealmLdapHbmDao().load(mappingRealmsLdap.get(id));
								Realm2viewComponentHbm tempRealm = createTempRealm(viewComponent, neededrole, null, null, realm, null);
								Realm2viewComponentHbm r = super.getRealm2viewComponentHbmDao().create(tempRealm);
								viewComponent.setRealm2vc(r);
							} else {
								relNode = XercesHelper.findNode(nodeRealm, "jaasRealmId");
								if (relNode != null) {
									Integer id = new Integer(XercesHelper.getNodeValue(relNode));
									RealmJaasHbm realm = super.getRealmJaasHbmDao().load(mappingRealmsJaas.get(id));
									Realm2viewComponentHbm tempRealm = createTempRealm(viewComponent, neededrole, null, null, null, realm);
									Realm2viewComponentHbm r = super.getRealm2viewComponentHbmDao().create(tempRealm);
									viewComponent.setRealm2vc(r);
								}
							}
						}
					}
				} else {
					Realm2viewComponentHbm tempRealm = new Realm2viewComponentHbmImpl();
					tempRealm.setViewComponent(viewComponent);
					Realm2viewComponentHbm r = super.getRealm2viewComponentHbmDao().create(tempRealm);
					viewComponent.setRealm2vc(r);
					String loginPage = XercesHelper.getNodeValue(nodeRealm, "loginPageId");
					if (loginPage != null && !"".equalsIgnoreCase(loginPage)) {
						Integer loginPageId = null;
						try {
							loginPageId = Integer.valueOf(loginPage);
						} catch (Exception e) {
						}
					}
				}

			}
		} catch (Exception e) {
			if (log.isDebugEnabled()) log.debug("Error ar import viewcomponent");
		}
		viewComponent.setChildren(null);
		viewComponent.setViewDocument(parent.getViewDocument());
		return getViewComponentHbmDao().create(viewComponent);
	}

	private void importViewComponentDocuments(Node nodeViewComponent, ViewComponentHbm viewComponent) {
		File tempFile=null;
		try {
			tempFile = File.createTempFile("temp_", ".xml");
		} catch (IOException e1) {
			log.error(e1);
		}

		Iterator itDocs = XercesHelper.findNodes(nodeViewComponent, ".//document");
		while (itDocs.hasNext()) {
			try {
				Element el = (Element) itDocs.next();
				Integer id = new Integer(el.getAttribute("id"));
				String strDocName = XercesHelper.getNodeValue(el, "./name");
				String strMimeType = el.getAttribute("mimeType");
				File fle = new File(tempFile.getParent() + File.separator + "d" + id);
				byte[] file = new byte[(int) fle.length()];
				new FileInputStream(fle).read(file);
				fle.delete();
				DocumentHbm document = new DocumentHbmImpl();
				try {
					Blob b = Hibernate.createBlob(file);
					document.setDocument(b);
				} catch (Exception e) {
					if (log.isWarnEnabled()) log.warn("Exception copying document to database", e);
				}
				document.setDocumentName(strDocName);
				document.setMimeType(strMimeType);
				document.setViewComponent(viewComponent);
				document = super.getDocumentHbmDao().create(document);
			} catch (Exception e) {
				if (log.isWarnEnabled()) log.warn("Error at importing documents");
			}
		}
	}

	private Hashtable<Integer, Integer> importViewComponentPictures(Node nodeViewComponent, ViewComponentHbm viewComponent) {
		File tempFile=null;
		try {
			tempFile = File.createTempFile("temp_", ".xml");
		} catch (IOException e1) {
			log.error(e1);
		}
		Hashtable<Integer, Integer> pictureIds = new Hashtable<Integer, Integer>();
		Iterator<Element> iterator=XercesHelper.findNodes(nodeViewComponent, ".//picture");
		while (iterator.hasNext()) {
			Element el = (Element) iterator.next();
			try {
				Integer id = new Integer(el.getAttribute("id"));
				String strMimeType = el.getAttribute("mimeType");
				String strPictureName = XercesHelper.getNodeValue(el, "./pictureName");
				String strAltText = XercesHelper.getNodeValue(el, "./altText");
				File fle = new File(tempFile.getParent() + File.separator + "f" + id);
				byte[] file = new byte[(int) fle.length()];
				new FileInputStream(fle).read(file);
				fle.delete();

				File fleThumb = new File(tempFile.getParent() + File.separator + "t" + id);
				byte[] thumbnail = new byte[(int) fleThumb.length()];
				new FileInputStream(fleThumb).read(thumbnail);
				fleThumb.delete();

				byte[] preview = null;
				File flePreview = new File(tempFile.getParent() + File.separator + "p" + id);
				if (flePreview.exists() && flePreview.canRead()) {
					preview = new byte[(int) flePreview.length()];
					new FileInputStream(flePreview).read(preview);
					flePreview.delete();
				}
				PictureHbm picture = new PictureHbmImpl();
				picture.setThumbnail(thumbnail);
				picture.setPicture(file);
				picture.setPreview(preview);
				picture.setMimeType(strMimeType);
				picture.setAltText(strAltText);
				picture.setPictureName(strPictureName);
				picture.setViewComponent(viewComponent);
				picture = getPictureHbmDao().create(picture);
				pictureIds.put(id, picture.getPictureId());
			} catch (Exception e) {
				if (log.isWarnEnabled()) log.warn("Error at importing pictures");
			}
		}
		tempFile.delete();
		return pictureIds;	
	}

	private Realm2viewComponentHbm createTempRealm(ViewComponentHbm viewComponent, String neededRole, RealmJdbcHbm jdbcRealm, RealmSimplePwHbm simplePWRealm, RealmLdapHbm ldapRealm, RealmJaasHbm jaasRealm) {
		Realm2viewComponentHbm tempRealm = new Realm2viewComponentHbmImpl();
		tempRealm.setViewComponent(viewComponent);
		tempRealm.setRoleNeeded(neededRole);
		if (jdbcRealm != null) {
			tempRealm.setJdbcRealm(super.getRealmJdbcHbmDao().load(jdbcRealm.getJdbcRealmId()));
		}
		if (simplePWRealm != null) {
			tempRealm.setSimplePwRealm(super.getRealmSimplePwHbmDao().load(simplePWRealm.getSimplePwRealmId()));
		}
		if (ldapRealm != null) {
			tempRealm.setLdapRealm(super.getRealmLdapHbmDao().load(ldapRealm.getLdapRealmId()));
		}
		if (jaasRealm != null) {
			tempRealm.setJaasRealm(super.getRealmJaasHbmDao().load(jaasRealm.getJaasRealmId()));
		}
		return tempRealm;
	}

	/**
	 * Import the realms used for a view component
	 * @param doc
	 * @param siteId
	 * @param useNewIDs
	 */
	private void importRealms(org.w3c.dom.Node doc, Integer siteId, boolean useNewIDs) {
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
			SiteHbm site = super.getSiteHbmDao().load(siteId);
			UserHbm userLogged = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			Iterator itRealms = XercesHelper.findNodes(doc, "//realmSimplePw");
			while (itRealms.hasNext()) {
				if (log.isDebugEnabled()) log.debug("Found RealmSimplePw to import...");
				Element elmRealm = (Element) itRealms.next();
				Integer id = new Integer(XercesHelper.getNodeValue(elmRealm, "simplePwRealmId"));
				RealmSimplePwHbm realm = null;
				if (useNewIDs) {
					realm = getRealmSimplePwHbmDao().create(elmRealm, true);
					realm.setSite(site);
					//realm.setOwner(userLogged);
					mappingRealmsSimplePw.put(id, realm.getSimplePwRealmId()); // mapping OLD-ID to NEW-ID
				} else {
					try {
						if (log.isDebugEnabled()) log.debug("searching RealmSimplePw: " + id);
						realm = super.getRealmSimplePwHbmDao().load(id);
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
								super.getRealmSimplePwUserHbmDao().remove(((RealmSimplePwUserHbm) it.next()));
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
						realm = super.getRealmSimplePwHbmDao().create(elmRealm, false);
						realm.setSite(site);
					}
				}
				String loginPageId = XercesHelper.getNodeValue(elmRealm, "loginPageId");
				if (loginPageId != null && loginPageId.length() > 0) loginPagesRealmsSimplePw.put(id, new Integer(loginPageId));
			}
			itRealms = XercesHelper.findNodes(doc, "//realmLdap");
			while (itRealms.hasNext()) {
				if (log.isDebugEnabled()) log.debug("Found RealmLdap to import...");
				Element elmRealm = (Element) itRealms.next();
				Integer id = new Integer(XercesHelper.getNodeValue(elmRealm, "ldapRealmId"));
				RealmLdapHbm realm = null;
				if (useNewIDs) {
					realm = super.getRealmLdapHbmDao().create(elmRealm, true);
					realm.setSite(site);
					mappingRealmsLdap.put(id, realm.getLdapRealmId()); // mapping OLD-ID to NEW-ID
				} else {
					try {
						if (log.isDebugEnabled()) log.debug("searching RealmLdap: " + id);
						realm = super.getRealmLdapHbmDao().load(id);
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
						realm = super.getRealmLdapHbmDao().create(elmRealm, false);
						realm.setSite(site);
					}
				}
				String loginPageId = XercesHelper.getNodeValue(elmRealm, "loginPageId");
				if (loginPageId != null && loginPageId.length() > 0) loginPagesRealmsLdap.put(id, new Integer(loginPageId));
			}
			itRealms = XercesHelper.findNodes(doc, "//realmJdbc");
			while (itRealms.hasNext()) {
				if (log.isDebugEnabled()) log.debug("Found RealmJdbc to import...");
				Element elmRealm = (Element) itRealms.next();
				Integer id = new Integer(XercesHelper.getNodeValue(elmRealm, "jdbcRealmId"));
				RealmJdbcHbm realm = null;
				if (useNewIDs) {
					realm = super.getRealmJdbcHbmDao().create(elmRealm, true);
					realm.setSite(site);
					mappingRealmsJdbc.put(id, realm.getJdbcRealmId()); // mapping OLD-ID to NEW-ID
				} else {
					try {
						if (log.isDebugEnabled()) log.debug("searching RealmJdbc: " + id);
						realm = super.getRealmJdbcHbmDao().load(id);
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
						realm = super.getRealmJdbcHbmDao().create(elmRealm, false);
						realm.setSite(site);
					}
				}
				String loginPageId = XercesHelper.getNodeValue(elmRealm, "loginPageId");
				if (loginPageId != null && loginPageId.length() > 0) loginPagesRealmsJdbc.put(id, new Integer(loginPageId));
			}
			itRealms = XercesHelper.findNodes(doc, "//realmJaas");
			while (itRealms.hasNext()) {
				if (log.isDebugEnabled()) log.debug("Found RealmJaas to import...");
				Element elmRealm = (Element) itRealms.next();
				Integer id = new Integer(XercesHelper.getNodeValue(elmRealm, "jaasRealmId"));
				RealmJaasHbm realm = null;
				if (useNewIDs) {
					realm = super.getRealmJaasHbmDao().create(elmRealm, true);
					realm.setSite(site);
					mappingRealmsJaas.put(id, realm.getJaasRealmId()); // mapping OLD-ID to NEW-ID
				} else {
					try {
						if (log.isDebugEnabled()) log.debug("searching RealmJaas: " + id);
						realm = super.getRealmJaasHbmDao().load(id);
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
						realm = super.getRealmJaasHbmDao().create(elmRealm, false);
						realm.setSite(site);
					}
				}
				String loginPageId = XercesHelper.getNodeValue(elmRealm, "loginPageId");
				if (loginPageId != null && loginPageId.length() > 0) loginPagesRealmsJaas.put(id, new Integer(loginPageId));
			}

		} catch (Exception exe) {
			log.error("Error occured importRealms: " + exe.getMessage(), exe);
		}

	}

	/**
	 * Import pictures used in the content of a vc
	 * @param unitId
	 * @param doc
	 * @return
	 */
	private Hashtable<Integer, Integer> importPictures(Integer unitId, Document doc, File directory, boolean useNewIds) {
		Iterator itPictures = XercesHelper.findNodes(doc, "//picture");
		Hashtable<Integer, Integer> pictureIds = new Hashtable<Integer, Integer>();
		while (itPictures.hasNext()) {
			Element el = (Element) itPictures.next();
			try {
				String unitIdAttribute = el.getAttribute("unitId");
				if(unitIdAttribute==null || unitIdAttribute.isEmpty()){continue;}
				Integer id = new Integer(el.getAttribute("id"));
				String strMimeType = el.getAttribute("mimeType");
				String strPictureName = XercesHelper.getNodeValue(el, "./pictureName");
				String strAltText = XercesHelper.getNodeValue(el, "./altText");
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
				if (flePreview.exists() && flePreview.canRead()) {
					preview = new byte[(int) flePreview.length()];
					new FileInputStream(flePreview).read(preview);
					flePreview.delete();
				}
				UnitHbm unit = getUnitHbmDao().load(unitId);
				PictureHbm picture = new PictureHbmImpl();
				if (!useNewIds) {
					picture.setPictureId(id);
				}
				picture.setThumbnail(thumbnail);
				picture.setPicture(file);
				picture.setPreview(preview);
				picture.setMimeType(strMimeType);
				picture.setAltText(strAltText);
				picture.setPictureName(strPictureName);
				picture.setUnit(unit);
				picture = getPictureHbmDao().create(picture);
				pictureIds.put(id, picture.getPictureId());
			} catch (Exception e) {
				if (log.isWarnEnabled()) log.warn("Error at importing pictures");
			}
		}
		return pictureIds;
	}

	/**
	 * Import documents used in the content of a vc
	 * @param unitId
	 * @param doc
	 * @return
	 */
	private Hashtable<Integer, Integer> importDocuments(Integer unitId, Document doc, File directory, boolean useNewIds) {
		Iterator itDocs = XercesHelper.findNodes(doc, "//document");
		Hashtable<Integer, Integer> docIds = new Hashtable<Integer, Integer>();
		while (itDocs.hasNext()) {
			try {
				Element el = (Element) itDocs.next();
				String unitIdAttribute = el.getAttribute("unitId");
				if(unitIdAttribute==null || unitIdAttribute.isEmpty()){
					continue;
				}
				UnitHbm unit = getUnitHbmDao().load(unitId);
				Integer id = new Integer(el.getAttribute("id"));
				String strDocName = XercesHelper.getNodeValue(el, "./name");
				String strMimeType = el.getAttribute("mimeType");
				File fle = new File(directory.getParent() + File.separator + "d" + id);
				byte[] file = new byte[(int) fle.length()];
				new FileInputStream(fle).read(file);
				fle.delete();
				DocumentHbm document = new DocumentHbmImpl();
				try {
					Blob b = Hibernate.createBlob(file);
					document.setDocument(b);
				} catch (Exception e) {
					if (log.isWarnEnabled()) log.warn("Exception copying document to database", e);
				}
				if (!useNewIds) {
					document.setDocumentId(id);
				}
				document.setDocumentName(strDocName);
				document.setMimeType(strMimeType);
				document.setUnit(unit);
				document = super.getDocumentHbmDao().create(document);
				docIds.put(id, document.getDocumentId());
			} catch (Exception e) {
				if (log.isWarnEnabled()) log.warn("Error at importing documents");
			}
		}
		return docIds;
	}

	@Override
	protected Integer handleGetUnitForViewComponent(Integer viewComponentId) throws Exception {
		return getViewComponentUnit(viewComponentId);
	}

	/**
	 * Search for the not null unit in current viewcomponent or parent until finding one
	 * @param viewComponentId
	 * @return
	 */
	private Integer getViewComponentUnit(Integer viewComponentId) {
		Integer value;
		ViewComponentHbm viewComp = getViewComponentHbmDao().load(viewComponentId);
		UnitHbm unit = viewComp.getAssignedUnit();
		if (unit == null) {
			value = getViewComponentUnit(viewComp.getParent().getViewComponentId());
		} else {
			value = unit.getUnitId();
		}
		return value;

	}

	@Override
	protected String handleCheckForUniqueUrlLinkName(Integer viewComponentId, Integer parentId, String urlLinkName) throws Exception {
		if (parentId != null) {
			ViewComponentValue[] children = getViewComponentChildren(parentId);
			ArrayList<Integer> existingNumber = new ArrayList<Integer>();
			boolean flag = false;
			for (ViewComponentValue viewComponentValue : children) {
				if ((viewComponentValue.getUrlLinkName().startsWith(urlLinkName)) && (viewComponentId.intValue() != viewComponentValue.getViewComponentId().intValue())) {
					String existingUrlLinkname = viewComponentValue.getUrlLinkName();
					if (existingUrlLinkname.equalsIgnoreCase(urlLinkName)) {
						flag = true;
					}
					if (existingUrlLinkname.startsWith(urlLinkName + "-")) {
						Integer version = Integer.parseInt(existingUrlLinkname.substring(urlLinkName.length() + 1, existingUrlLinkname.length()));
						existingNumber.add(version);
					}
				}
			}
			if ((flag) && (existingNumber.size() == 0)) {
				urlLinkName = urlLinkName + "-1";
			}
			if ((existingNumber.size() > 0) && (flag)) {
				Integer max = 0;
				for (Integer value : existingNumber) {
					if (value.intValue() > max.intValue()) {
						max = value;
					}
				}
				max++;
				urlLinkName = urlLinkName + "-" + max;
			}

		}
		return urlLinkName;
	}

	@Override
	protected boolean handleIsViewComponentPublishable(Integer viewComponentId) throws Exception {
		Integer[] vcArray = getParents4ViewComponent(viewComponentId);
		ViewComponentHbmDao vcDao = getViewComponentHbmDao();
		for (int i = 0; i < vcArray.length; i++) {
			ViewComponentHbm vc = vcDao.load(viewComponentId);
			if ((vc.getStatus() < Constants.DEPLOY_STATUS_FOR_DEPLOY) && (vc.getOnline() == 0)) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected void handleSetViewComponentOnline(Integer viewComponentId) throws Exception {
		ViewComponentHbm viewComponentHbm = getViewComponentHbmDao().load(viewComponentId);
		viewComponentHbm.setOnline((byte) 1);
		getViewComponentHbmDao().update(viewComponentHbm);
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.remote.ViewServiceSpringBase#handleGetAllAccessRoles()
	 */
	@Override
	protected AccessRoleValue[] handleGetAllAccessRoles() throws Exception {
		Collection coll = getAccessRoleHbmDao().findAll();
		AccessRoleValue[] values = new AccessRoleValue[coll.size()];
		int i = 0;
		for (Iterator it = coll.iterator(); it.hasNext();) {
			values[i++] = getAccessRoleHbmDao().toAccessRoleValue((AccessRoleHbm) it.next());
		}
		return values;
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.remote.ViewServiceSpringBase#handleAddAccessRole(java.lang.String)
	 */
	@Override
	protected void handleAddAccessRole(String roleId) throws Exception {
		AccessRoleHbm accessRoleHbm = AccessRoleHbm.Factory.newInstance();
		accessRoleHbm.setRoleId(roleId);
		getAccessRoleHbmDao().create(accessRoleHbm);
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.remote.ViewServiceSpringBase#handleRemoveAccessRole(java.lang.String)
	 */
	@Override
	protected void handleRemoveAccessRole(String roleId) throws Exception {
		getAccessRoleHbmDao().remove(roleId);
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.remote.ViewServiceSpringBase#handleAddAccessRoleToViewComponent(java.lang.Integer, java.lang.String, java.lang.Integer)
	 */
	@Override
	protected void handleAddAccessRoleToViewComponent(Integer viewComponentId, String accessRole, Integer loginPageId) throws Exception {
		ViewComponentHbm vc = getViewComponentHbmDao().load(viewComponentId);
		AccessRoleHbm ar = getAccessRoleHbmDao().load(accessRole);
		ViewComponentHbm login = getViewComponentHbmDao().load(loginPageId);

		AccessRoles2ViewComponentsHbm ar2vc = AccessRoles2ViewComponentsHbm.Factory.newInstance(login, vc, ar);
		getAccessRoles2ViewComponentsHbmDao().create(ar2vc);
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.remote.ViewServiceSpringBase#handleAddAccessRolesToViewComponent(java.lang.Integer, java.lang.String[], java.lang.Integer)
	 */
	@Override
	protected void handleAddAccessRolesToViewComponent(Integer viewComponentId, String[] accessRoles, Integer loginPageId) throws Exception {
		ViewComponentHbm vc = getViewComponentHbmDao().load(viewComponentId);
		ViewComponentHbm login = getViewComponentHbmDao().load(loginPageId);
		for (String accessRole : accessRoles) {
			AccessRoleHbm ar = getAccessRoleHbmDao().load(accessRole);
			AccessRoles2ViewComponentsHbm ar2vc = AccessRoles2ViewComponentsHbm.Factory.newInstance(login, vc, ar);
			getAccessRoles2ViewComponentsHbmDao().create(ar2vc);
		}
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.remote.ViewServiceSpringBase#handleRemoveAccessRoleFromViewComponent(java.lang.Integer, java.lang.String)
	 */
	@Override
	protected void handleRemoveAccessRole2ViewComponent(Integer ar2vcId) throws Exception {
		getAccessRoles2ViewComponentsHbmDao().remove(ar2vcId);
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.remote.ViewServiceSpringBase#handleHandleGetViewComponentsForSearch(java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	@Override
	protected List handleGetViewComponentsForSearch(Integer unitId, Integer viewDocumentId, String searchValue) throws Exception {
		List<ViewComponentValue> results = new ArrayList<ViewComponentValue>();
		List<ViewComponentHbm> entities = getViewComponentHbmDao().getViewComponentsForSearch(unitId, viewDocumentId, searchValue);
		for (ViewComponentHbm entity : entities) {
			Integer vcUnitId = getViewComponentUnit(entity.getViewComponentId());
			if (vcUnitId.intValue() == unitId.intValue()) {
				results.add(entity.getDao());
			}
		}
		return results;
	}
}
