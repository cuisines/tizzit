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
package de.juwimm.cms.gui.tree;

import static de.juwimm.cms.client.beans.Application.getBean;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.vo.ViewComponentValue;

/**
 * <b>Tizzit Enterprise Content Management</b><br/>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PageNode extends TreeNode implements Transferable {
	private static ResourceBundle rb = Constants.rb;
	private static Logger log = Logger.getLogger(PageNode.class);
	private ViewComponentValue viewComponent;
	private final Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	public static final DataFlavor DATA_FLAVOUR_PAGE_NODE = new DataFlavor(PageNode.class, "PageNode");
	public static final DataFlavor DATA_FLAVOUR_TEXT_PLAIN = new DataFlavor("text/plain", "Text");
	public static final DataFlavor[] FLAVORS = {DATA_FLAVOUR_PAGE_NODE, DATA_FLAVOUR_TEXT_PLAIN};

	private PageNode() {
	}

	public PageNode(ViewComponentValue vcDao) {
		if (!vcDao.isLeaf()) {
			add(new DefaultMutableTreeNode(Constants.rb.getString("panel.tree.loadingSubelements")));
		}
		setViewComponent(vcDao);
	}

	public PageNode(ViewComponentValue vcDao, DefaultTreeModel treeModel) {
		this(vcDao);
		setModel(treeModel);
	}

	public boolean isRootView() {
		return getViewComponent().isRoot();
	}

	public int getViewId() {
		return getViewComponent().getViewComponentId();
	}

	@Override
	public String toString() {
		if (getViewComponent() == null || getViewComponent().isRoot()) { return rb.getString("panel.tree.rootHeading"); }
		return getViewComponent().getDisplayLinkName();
	}

	public boolean isChildADao() {
		return getViewComponent().isLeaf();
	}

	public void setStatus(int status) {
		getViewComponent().setStatus(status);
		getTreeModel().nodeChanged(this);
	}

	public int getStatus() {
		return getViewComponent().getStatus();
	}

	@Override
	public void update(Object daoObject) {
		if (daoObject instanceof ViewComponentValue) {
			update((ViewComponentValue) daoObject);
		}
	}

	public void update(ViewComponentValue vcDao) {
		boolean isEdited = false;

		if (getViewComponent().getStatus() != vcDao.getStatus()) {
			isEdited = true;
			getViewComponent().setStatus(vcDao.getStatus());
		}
		if (!getViewComponent().getDisplayLinkName().equals(vcDao.getDisplayLinkName())) {
			isEdited = true;
			getViewComponent().setDisplayLinkName(vcDao.getDisplayLinkName());
		}
		if (getViewComponent().getOnline() != vcDao.getOnline()) {
			isEdited = true;
			getViewComponent().setOnline(vcDao.getOnline());
		}
		if (isEdited) {
			getTreeModel().nodeChanged(this);
		}
	}

	public byte getOnline() {
		return getViewComponent().getOnline();
	}

	// --------- Transferable --------------

	public boolean isDataFlavorSupported(DataFlavor df) {
		return df.equals(DATA_FLAVOUR_PAGE_NODE);
	}

	/** implements Transferable interface */
	public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException, IOException {
		if (df.equals(DATA_FLAVOUR_PAGE_NODE)) { return this; }
		throw new UnsupportedFlavorException(df);
	}

	/** implements Transferable interface */
	public DataFlavor[] getTransferDataFlavors() {
		return FLAVORS;
	}

	// ----------- Centralization of needed parameters ---------
	@Override
	public boolean isDeleteable() {
		boolean retVal = false;
		if (isRoot()) return false;
		if (this instanceof PageOtherUnitNode) {
			retVal = false;
		} else {
			retVal = true;
		}
		return retVal;
	}

	@Override
	public boolean isMoveableToUp() {
		boolean retVal = false;
		if (isRoot()) return false;
		if (getParent().getIndex(this) != 0) {
			retVal = true;
		} else {
			retVal = false;
		}
		return retVal;
	}

	@Override
	public boolean isMoveableToDown() {
		boolean retVal = false;
		if (isRoot()) return false;
		if (getParent().getIndex(this) != getParent().getChildCount() - 1) {
			retVal = true;
		} else {
			retVal = false;
		}
		return retVal;
	}

	@Override
	public boolean isMoveableToLeft() {
		boolean retVal = false;
		if (isRoot()) return false;
		if (getParent().getParent() == null) {
			retVal = false;
		} else {
			retVal = true;
		}
		return retVal;
	}

	@Override
	public boolean isMoveableToRight() {
		boolean retVal = false;
		if (isRoot()) return false;
		DefaultMutableTreeNode prevNode = getPreviousSibling();
		//normally getIndex(this)==0 and prevNode==null must be the same...
		if (getParent().getIndex(this) != 0 && prevNode != null && prevNode instanceof TreeNode && ((TreeNode) prevNode).isAppendingAllowed()) {
			retVal = true;
		} else {
			retVal = false;
		}
		return retVal;
	}

	@Override
	public boolean isEditable() {
		boolean retVal = false;
		return retVal;
	}

	private void buildNodes(PageNode pageNode, ViewComponentValue vcDao) {
		try {
			TreeNode entry = null;
			switch (vcDao.getViewType()) {
				case Constants.VIEW_TYPE_INTERNAL_LINK:
					entry = new PageInternallinkNode(vcDao, getTreeModel());
					break;

				case Constants.VIEW_TYPE_EXTERNAL_LINK:
					entry = new PageExternallinkNode(vcDao, getTreeModel());
					break;

				case Constants.VIEW_TYPE_SEPARATOR:
					entry = new PageSeparatorNode(vcDao, getTreeModel());
					break;

				case Constants.VIEW_TYPE_CONTENT:
				case Constants.VIEW_TYPE_UNIT:
					entry = new PageContentNode(vcDao, getTreeModel());
					if (vcDao.isUnit() && !(getTreeModel() instanceof InternallinkTreeModel)) {
						if (!comm.isUserInUnit(vcDao.getUnitId().intValue())) {
							//if(a_View.isUnit() && !m_Communication.isUserInRole(UserRights.SITE_ROOT)
							//  && !a_View.getUnitId().equals(m_Communication.getSelectedUnitId())) {
							entry = new PageOtherUnitNode(vcDao, getTreeModel());
						}
					}
					break;

				case Constants.VIEW_TYPE_SYMLINK:
					entry = new PageSymlinkNode(vcDao, getTreeModel());
					break;

				default:
					log.warn("Something is wrong here");
					//entry = new PageNode();
			}
			if (entry != null) {
				pageNode.add(entry);
			}
		} catch (Exception exe) {
			log.error("Error building nodes", exe);
		}
	}

	@Override
	public boolean loadChildren() {
		boolean retVal = false;
		if (!isInit()) {
			if (log.isDebugEnabled()) log.debug("loadChildren::l_Entry.isInit() == false ViewId: " + getViewId());
			try {
				removeAllChildren();
				/*if (getViewComponent().isUnit()) {
				 //add(new TreeNode("Einrichtung"));
				 //add(new TreeNode("Mediendatenbank"));
				 }*/
				setViewComponent(comm.getViewComponent(getViewId(), 1));
				try {
					ViewComponentValue[] vcdarr = getViewComponent().getChildren();
					for (int i = 0; i < vcdarr.length; i++) {
						buildNodes(this, vcdarr[i]);
					}
				} catch (Exception ex) {
					if (log.isDebugEnabled()) log.debug("probably no children, ignoring.");
				}
			} catch (Exception ex) {
			}
			log.debug("SETTING INIT");
			setInit(true);
			retVal = true;
		} else {
			if (log.isDebugEnabled()) log.debug("loadChildren::reloadChildren");
			try {
				PageNode parent = (PageNode) getParent();
				if (parent == null) {
					parent = this;
				}
				ViewComponentValue parentDao = comm.getViewComponent(parent.getViewId(), 1);
				try {
					if (parentDao.getChildren() != null) {
						ViewComponentValue[] childrenServerDao = parentDao.getChildren();
						Enumeration childrenTreeDao = parent.children();

						if (childrenServerDao != null && childrenTreeDao != null && childrenTreeDao.hasMoreElements()) {
							for (int i = 0; i < childrenServerDao.length; i++) {
								ViewComponentValue childDao = childrenServerDao[i];
								if (childrenTreeDao.hasMoreElements()) {
									TreeNode child = (TreeNode) childrenTreeDao.nextElement();
									if (child instanceof PageNode) {
										if (((PageNode) child).getViewId() != childDao.getViewComponentId()) {
											parent.removeAllChildren();
											parent.setInit(false);
											parent.loadChildren();
											retVal = true;
											log.info("Check check - Treestructure changed!");
										} else {
											child.update(childDao);
										}
									}
								} else {
									// hier muss auch irgendwas nicht stimmen...
									parent.removeAllChildren();
									parent.setInit(false);
									parent.loadChildren();
									retVal = true;
									log.info("Treestructure changed!");
									/*parent.setView(childDao);
									 buildNodes(parent, childDao);*/
								}
							}
						}
					}
				} catch (Exception exe) {
					log.error("Error reloading children", exe);
				}
			} catch (Exception exe) {
				log.error("Error", exe);
			}
		}
		if (getTreeModel() != null && retVal) {
			getTreeModel().nodeStructureChanged(this); // THIS FUCKED ME UP TO 4 HOURES OF WORK.... OH MY GODNESS
		} else if (getTreeModel() == null) {
			log.warn("TreeModel was null!");
		}
		return retVal;
	}

	/**
	 * @param viewComponent The viewComponent to set.
	 */
	public void setViewComponent(ViewComponentValue viewComponent) {
		this.viewComponent = viewComponent;
	}

	/**
	 * @return Returns the viewComponent.
	 */
	public ViewComponentValue getViewComponent() {
		return viewComponent;
	}

}