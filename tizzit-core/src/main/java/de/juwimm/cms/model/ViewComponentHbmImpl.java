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

import java.util.*;

import org.apache.log4j.Logger;

import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.vo.ViewComponentValue;

/**
 * @see de.juwimm.cms.model.ViewComponentHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> ,
 *         Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class ViewComponentHbmImpl extends ViewComponentHbm {
	private static Logger log = Logger.getLogger(ViewComponentHbmImpl.class);
	private static final long serialVersionUID = 4842362882873491911L;

	/**
	 * @see de.juwimm.cms.model.ViewComponentHbm#addChild(de.juwimm.cms.model.ViewComponentHbm)
	 */
	@Override
	public void addChild(ViewComponentHbm child) {
		try {
			Collection children = getChildren();
			children.add(child);
		} catch (Exception e) {
			log.error("Could not add child to viewcomponent with id: " + getViewComponentId(), e);
		}
	}

	/**
	 * @see de.juwimm.cms.model.ViewComponentHbm#removeChild(de.juwimm.cms.model.ViewComponentHbm)
	 */
	@Override
	public void removeChild(ViewComponentHbm child) {
		try {
			Collection children = getChildren();
			children.remove(child);
		} catch (Exception e) {
			log.error("Could not remove child from viewcomponent with id: " + getViewComponentId());
		}
	}

	/**
	 * 
	 * @see de.juwimm.cms.model.ViewComponentHbm#getUnit4ViewComponent()
	 */
	@Override
	public Integer getUnit4ViewComponent() {
		try {
			return this.getUnitViewComponent(this).getAssignedUnit().getUnitId();
		} catch (Exception exe) {
			log.error("get Unit 4 View Component Error occured" + exe.getMessage());
			return null;
		}

	}

	/**
	 * 
	 * @see de.juwimm.cms.model.ViewComponentHbm#getViewComponentUnit()
	 */
	@Override
	public ViewComponentHbm getViewComponentUnit() {
		try {
			return this.getUnitViewComponent(this);
		} catch (Exception exe) {
			log.error("Error occured in getViewComponentUnit()", exe);
			return null;
		}
	}

	/**
	 * @see de.juwimm.cms.model.ViewComponentHbm#getNavigationModifiedDate(int,
	 *      boolean)
	 */
	@Override
	public Date getNavigationModifiedDate(int depth, boolean liveServer) {
		Date retVal = new Date(getLastModifiedDate());
		// getRevision().getModifiedDate());
		if (depth != 1) { // 1 is only THIS ViewComponent
			try {
				for (Iterator it = getChildren().iterator(); it.hasNext();) {
					ViewComponentHbm vclChild = (ViewComponentHbm) it.next();
					// if (vclChild.shouldBeVisible(liveServer)) {
					if (vclChild.isVisible()) {
						int destDepth = (depth == 0) ? 0 : depth - 1;
						Date childDate = vclChild.getNavigationModifiedDate(destDepth, liveServer);
						if (retVal.compareTo(childDate) <= 0) {
							retVal = childDate;
						}
					}
				}
			} catch (Exception exe) {
				log.error("Error occured", exe);
			}
		}
		return retVal;
	}

	/**
	 * @see de.juwimm.cms.model.ViewComponentHbm#getDeployDao()
	 */
	@Override
	public ViewComponentValue getDeployDao() {
		ViewComponentValue dao = new ViewComponentValue();
		dao.setViewComponentId(this.getViewComponentId());
		dao.setViewType(this.getViewType());
		dao.setDeployCommand(this.getDeployCommand());
		dao.setOnline(this.getOnline());
		dao.setDisplayLinkName(this.getDisplayLinkName());
		return dao;
	}

	/**
	 * @see de.juwimm.cms.model.ViewComponentHbm#getDao()
	 */
	@Override
	public ViewComponentValue getDao() throws UserException {
		// ViewComponentValue val = new ViewComponentValue();
		// val.setCreateDate(this.getCreateDate());
		// val.setDeployCommand(this.getDeployCommand());
		// val.setDisplayLinkName(this.getDisplayLinkName());
		// val.setLastModifiedDate(this.getLastModifiedDate());
		// val.setLeaf(this.isLeaf());
		// val.setLinkDescription(this.getLinkDescription());
		// val.setMetaData(this.getMetaData());
		// val.setMetaDescription(this.getMetaDescription());
		// val.setOnline(this.getOnline());
		// val.setOnlineStart(this.getOnlineStart());
		// val.setOnlineStop(this.getOnlineStop());
		// val.setReference(this.getReference());
		// val.setShowType(this.getShowType());
		// val.setStatus(this.getStatus());
		// val.setViewComponentId(this.getViewComponentId());
		// val.setViewIndex(this.getViewIndex());
		// val.setViewLevel(this.getViewLevel());
		// val.setViewType(this.getViewType());
		// val.setVisible(this.isVisible());
		// val.setSearchIndexed(this.isSearchIndexed());
		// try {
		// val.setParentId(this.getParent().getViewComponentId());
		// } catch (Exception ex) {
		// }
		// byte displaySettings = 0;
		// try {
		// // may result errors during update and development
		// displaySettings = this.getDisplaySettings();
		// } catch (Exception e) {
		// }
		// val.setDisplaySettings(displaySettings);
		// try {
		// ViewComponentHbm firstChild = this.getFirstChild();
		// if (firstChild != null && firstChild.getViewComponentId() != null)
		// val.setFirstChildId(firstChild.getViewComponentId());
		// } catch (Exception e) {
		// }
		// return val;
		return getDao(-1);
	}

	/**
	 * @see de.juwimm.cms.model.ViewComponentHbm#getViewComponentDao()
	 */
	@Override
	public ViewComponentValue getViewComponentDao() throws UserException {
		return getDao();
	}

	/**
	 * @see de.juwimm.cms.model.ViewComponentHbm#getDao(int)
	 */
	@Override
	public ViewComponentValue getDao(int depth) throws UserException {
		log.debug("begin getDao() id=" + this.getViewComponentId().toString() + " depth=" + depth);
		ViewComponentHbm temp;
		UnitHbm unit;

		ViewComponentValue data = new ViewComponentValue();
		data.setLinkDescription(this.getLinkDescription());
		data.setDisplayLinkName(this.getDisplayLinkName());
		data.setUrlLinkName(this.getUrlLinkName());
		data.setOnlineStart(this.getOnlineStart());
		data.setOnlineStop(this.getOnlineStop());
		data.setReference(this.getReference());
		data.setViewType(this.getViewType());
		data.setViewIndex(this.getViewIndex());
		data.setViewLevel(this.getViewLevel());
		data.setViewComponentId(this.getViewComponentId());
		data.setShowType(this.getShowType());
		data.setStatus(this.getStatus());
		data.setOnline(this.getOnline());
		data.setDeployCommand(this.getDeployCommand());
		data.setMetaData(this.getMetaData());
		data.setMetaDescription(this.getMetaDescription());
		data.setFirstLevel(this.isIAmInTheFirstLevel());
		data.setVisible(this.isVisible());
		data.setSearchIndexed(this.isSearchIndexed());
		data.setXmlSearchIndexed(this.isXmlSearchIndexed());
		byte displaySettings = 0;
		try {
			// may result errors during update and development
			displaySettings = this.getDisplaySettings();
		} catch (Exception e) {
		}
		data.setDisplaySettings(displaySettings);

		if ((unit = this.getAssignedUnit()) != null) {
			data.setUnitId(unit.getUnitId());
		}
		data.setLastModifiedDate(getLastModifiedDate());
		data.setUserLastModifiedDate(this.getUserLastModifiedDate());
		data.setCreateDate(getCreateDate());
		// data.setRevision(this.getRevision().getDao());

		if ((temp = this.getFirstChild()) != null) {
			data.setFirstChildId(temp.getViewComponentId());
			if (depth > -1) {
				try {
					Vector<ViewComponentValue> vec = new Vector<ViewComponentValue>(this.getAllChildrenOrderedAsDao(depth));
					if (vec != null) {
						ViewComponentValue[] vcdarr = vec.toArray(new ViewComponentValue[0]);
						data.setChildren(vcdarr);
					}
				} catch (Exception exe) {
					throw new UserException(exe.getMessage());
				}
			}

		}

		if ((temp = this.getNextNode()) != null) {
			data.setNextId(temp.getViewComponentId());
		}

		if ((temp = this.getPrevNode()) != null) {
			data.setPrevId(temp.getViewComponentId());
		}

		if ((temp = this.getParent()) != null) {
			data.setParentId(temp.getViewComponentId());
		}

		Collection colChildren = this.getChildren();
		if (colChildren != null) {
			data.setLeaf(colChildren.size() == 0);
		} else {
			data.setLeaf(true);
		}
		data.setRoot(getParent() == null);
		data.setUnit(getAssignedUnit() != null);
		log.debug("end getDao");
		return data;
	}

	/**
	 * @see de.juwimm.cms.model.ViewComponentHbm#getAllChildrenOrderedAsDao(int)
	 */
	@Override
	public Collection getAllChildrenOrderedAsDao(int depth) throws UserException {
		int currentDepth = depth - 1;
		log.debug("begin getViewComponentChildrenOrdered() depth " + depth);
		if (isLeaf()) { throw new UserException("node is a leaf."); }
		Vector<ViewComponentValue> vec = new Vector<ViewComponentValue>();
		ViewComponentHbm temp = getFirstChild();
		if (currentDepth >= 0) {
			try {
				vec.addElement(temp.getDao(currentDepth));
			} catch (Exception exe) {
				throw new UserException(exe.getMessage());
			}
		}
		while ((temp = temp.getNextNode()) != null) {
			if(log.isTraceEnabled()) log.trace("TRACE getAllChildrenOrderedAsDao: nextNode: " + temp.getViewComponentId());
			if (currentDepth >= 0) {
				try {
					vec.addElement(temp.getDao(currentDepth));
				} catch (Exception exe) {
					throw new UserException(exe.getMessage());
				}
			}
		}
		log.debug("end getViewComponentChildrenOrdered()");
		return vec;
	}

	/**
	 * 
	 * @see de.juwimm.cms.model.ViewComponentHbm#getChildrenOrdered()
	 */
	@Override
	public Collection getChildrenOrdered() {
		if (log.isDebugEnabled()) log.debug("getChildrenOrdered start");

		ArrayList vec = null;
		if (!isLeaf()) {
			try {
				Set<ViewComponentHbm> children = (Set<ViewComponentHbm>) getChildren();
				ArrayList<ViewComponentHbm> coll = new ArrayList<ViewComponentHbm>(children);
				vec = new ArrayList(coll.size());
				ViewComponentHbm prevNode = null;
				for (int i = 0; i < children.size(); i++) {
					Iterator it = coll.iterator();
					while (it.hasNext()) {
						ViewComponentHbm foundOne = (ViewComponentHbm) it.next();
						if ((foundOne.getPrevNode() == null && prevNode == null) || (prevNode != null && foundOne.getPrevNode() != null && foundOne.getPrevNode().getViewComponentId().equals(prevNode.getViewComponentId()))) {
							vec.add(foundOne);
							prevNode = foundOne;
							coll.remove(foundOne);
							break;
						}
					}
				}
			} catch (Exception exe) {
				log.error("an unknown error occured", exe);
			}
		}
		if (vec == null) vec = new ArrayList();
		if (log.isDebugEnabled()) log.debug("getChildrenOrdered end");
		return vec;
	}

	/**
	 * @see de.juwimm.cms.model.ViewComponentHbm#setUnitOnline()
	 */
	@Override
	public void setUnitOnline() {
		// @todo implement public void setUnitOnline()
		throw new java.lang.UnsupportedOperationException("de.juwimm.cms.model.ViewComponent.setUnitOnline() Not implemented!");
	}

	/**
	 * @see de.juwimm.cms.model.ViewComponentHbm#getDepth()
	 */
	@Override
	public int getDepth() {
		// @todo implement public int getDepth()
		return 0;
	}

	/**
	 * @see de.juwimm.cms.model.ViewComponentHbm#hasSiblingsWithLinkName(java.lang.String)
	 */
	@Override
	public boolean hasSiblingsWithLinkName(String testName) {
		// @todo implement public boolean
		// hasSiblingsWithLinkName(java.lang.String testName)
		return false;
	}

	/**
	 * @see de.juwimm.cms.model.ViewComponentHbm#getNavigationRoot()
	 */
	@Override
	public ViewComponentHbm getNavigationRoot() {
		if (log.isDebugEnabled()) log.debug("getNavigationRoot start");
		if ((getViewIndex() != null && "2".equals(getViewIndex())) || isRoot()) {
			return this;
		}
		ViewComponentHbm parent = null;
		parent = getParent();
		if (parent != null && !parent.isRoot()) {
			while (!(getViewIndex() != null && "2".equals(parent.getViewIndex())) && !parent.isRoot()) {
				parent = parent.getParent();
				if (parent == null) { return null; }
			}
		}
		log.debug("getNavigationRoot end");
		return parent;
	}

	/**
	 * @see de.juwimm.cms.model.ViewComponentHbm#getNavigationRootDistance()
	 */
	@Override
	public int getNavigationRootDistance() {
		if ((getViewIndex() != null && "2".equals(getViewIndex())) || isRoot()) { return 0; }
		ViewComponentHbm parent = getParent();
		int parentCount = 1;
		if (parent != null && !parent.isRoot()) {
			while (!(getViewIndex() != null && "2".equals(parent.getViewIndex())) && !parent.isRoot()) {
				parent = parent.getParent();
				if (parent == null) { return -1; }
				parentCount++;
			}
		}
		return parentCount;
	}

	/**
	 * @see de.juwimm.cms.model.ViewComponentHbm#getPath()
	 */
	@Override
	public String getPath() {
		try {
			return this.buildTreePath(this, "");
		} catch (Exception exe) {
			return "";
		}
	}

	/**
	 * @see de.juwimm.cms.model.ViewComponentHbm#isRoot()
	 */
	@Override
	public boolean isRoot() {
		return (this.getParent() == null);
	}

	/**
	 * References a ViewComponent a unit?
	 * 
	 * @return boolean
	 * @see de.juwimm.cms.model.ViewComponentHbm#isUnit()
	 */
	@Override
	public boolean isUnit() {
		return (this.getAssignedUnit() != null);
	}

	/**
	 * @see de.juwimm.cms.model.ViewComponentHbm#getAllChildrenWithUnits()
	 */
	@Override
	public Collection getAllChildrenWithUnits() {
		return getAllChildrenWithUnits(this);
	}

	private Vector<ViewComponentHbm> getAllChildrenWithUnits(ViewComponentHbm mother) {
		Vector<ViewComponentHbm> retVec = new Vector<ViewComponentHbm>();
		ViewComponentHbm vcl = mother.getFirstChild();
		while (vcl != null) {
			if (vcl.isUnit()) {
				retVec.add(vcl);
			} else {
				retVec.addAll(getAllChildrenWithUnits(vcl));
			}
			vcl = vcl.getNextNode();
		}
		return retVec;
	}

	/**
	 * @see de.juwimm.cms.model.ViewComponentHbm#isLeaf()
	 */
	@Override
	public boolean isLeaf() {
		return (getFirstChild() == null);
	}

	/**
	 * @see de.juwimm.cms.model.ViewComponentHbm#setParentViewComponent(de.juwimm.cms.model.ViewComponentHbm)
	 */
	@Override
	public void setParentViewComponent(ViewComponentHbm parent) {
		if (parent != null && parent.getParent() == null) {
			setShowType((byte) 1);
		}
		setParent(parent);
	}

	/**
	 * @see de.juwimm.cms.model.ViewComponentHbm#isIAmInTheFirstLevel()
	 */
	@Override
	public boolean isIAmInTheFirstLevel() {
		// @todo implement public boolean isIAmInTheFirstLevel()
		return false;
	}

	/**
	 * @see de.juwimm.cms.model.ViewComponentHbm#getPageModifiedDate()
	 */
	@Override
	public long getPageModifiedDate() {
		throw new UnsupportedOperationException("Use classifier instead!");
	}

	/**
	 * 
	 * @param viewComponent
	 * @param concPath
	 * @return the path to this viewComponent
	 */
	private String buildTreePath(ViewComponentHbm viewComponent, String concPath) {
		// if (log.isTraceEnabled()) log.trace("buildTreePath start");
		if (viewComponent.isRoot()) { return concPath; }
		String newPath = "";
		newPath = viewComponent.getUrlLinkName();

		if (concPath.equals("")) {
			concPath = newPath;
		} else {
			concPath = newPath + "/" + concPath;
		}
		return this.buildTreePath(viewComponent.getParent(), concPath);
	}

	/**
	 * @param vcl
	 * @return the first viewComponent in root-direction beeing a unit
	 */
	private ViewComponentHbm getUnitViewComponent(ViewComponentHbm vcl) {
		if (vcl.getAssignedUnit() != null) {
			return vcl;
		} else if (vcl.isRoot()) {
			return vcl;
		} else {
			ViewComponentHbm vcp = vcl.getParent();
			return this.getUnitViewComponent(vcp);
		}
	}

}
