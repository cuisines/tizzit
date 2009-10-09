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
package de.juwimm.cms.content.panel;

import static de.juwimm.cms.client.beans.Application.getBean;
import static de.juwimm.cms.common.Constants.rb;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.Messages;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.modules.AbstractModule;
import de.juwimm.cms.content.modules.Module;
import de.juwimm.cms.gui.tree.CmsTreeRenderer;
import de.juwimm.cms.gui.tree.InternalLinkCache;
import de.juwimm.cms.gui.tree.InternallinkTreeModel;
import de.juwimm.cms.gui.tree.PageNode;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.SiteValue;
import de.juwimm.cms.vo.ViewComponentValue;
import de.juwimm.cms.vo.ViewDocumentValue;
import de.juwimm.swing.DropDownHolder;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanInternalLink extends JPanel {
	private static Logger log = Logger.getLogger(PanInternalLink.class);
	private final Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private final int rootViewId = ((ContentManager) getBean(Beans.CONTENT_MANAGER)).getRootUnitId();
	private InternallinkTreeModel currentTreeModel = null;
	private final JScrollPane jScrollPane1 = new JScrollPane();
	private final JTree tree = new JTree();
	private final JLabel lblLinkName = new JLabel();
	private final JTextField txtLinkname = new JTextField();
	private final JButton btnSearchAnker = new JButton();
	private final JComboBox cboAnker = new JComboBox();
	private Module module;
	private String errMsg = "";
	private boolean treeLink = false;
	private int targetViewId = 0;
	private String targetViewLevel = null;
	private String anchor = null;
	private boolean loading = false;
	private final JComboBox cbxViewDocuments = new JComboBox();
	private static InternalLinkCache linkCache = new InternalLinkCache();
	private final JComboBox cbxRelatedSites = new JComboBox();
	private boolean isSymlink = false;
	private final JCheckBox cbxDisplayTypeInline = new JCheckBox();
	private final JCheckBox cbxPopup = new JCheckBox();
	private PanPopupDetails panPopupDetails = null;

	public PanInternalLink(Module module, boolean treeLink, boolean isSymlink) {
		this(treeLink, isSymlink);
		this.module = module;
		this.isSymlink = isSymlink;
	}

	public PanInternalLink(boolean treeLink, boolean isSymlink) {
		this.treeLink = treeLink;
		this.isSymlink = isSymlink;
		try {
			jbInit();
			this.txtLinkname.setVisible(!treeLink);
			this.lblLinkName.setVisible(!treeLink);
			if (rb != null) {
				lblLinkName.setText(rb.getString("panel.panelView.lblLinkname"));
				btnSearchAnker.setText(rb.getString("content.modules.internalLink.btnSearchAnchor"));
				cbxDisplayTypeInline.setText(rb.getString("content.modules.externalLink.displayTypeInline"));
				cbxPopup.setText(rb.getString("PanPopupDetails.showInPopup"));
			}
			btnSearchAnker.setEnabled(false);
			TreeWillExpandListener tl = new TreeWillExpandListener() {
				public void treeWillCollapse(TreeExpansionEvent event) {
				}

				public void treeWillExpand(TreeExpansionEvent event) {
					try {
						TreePath path = event.getPath();
						PageNode entry = (PageNode) path.getLastPathComponent();

						if (!entry.isInit() && !entry.isLeaf()) {
							treeClick(event.getPath());
						}
					} catch (Exception exe) {
						log.error("Error expanding the tree " + event.getPath(), exe);
					}
				}
			};

			MouseListener ml = new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					try {
						int selRow = tree.getRowForLocation(e.getX(), e.getY());
						TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
						btnSearchAnker.setEnabled(true);
						if (e.getClickCount() == 2) {
							if (tree.isExpanded(selPath)) tree.collapsePath(selPath);
						} else if (selRow != -1) {
							treeClick(selPath);
						}
					} catch (Exception exe) {
						log.error("Error during mousePressed", exe);
					}
				}
			};
			tree.addMouseListener(ml);
			tree.addTreeWillExpandListener(tl);

			/*			if (currentTreeModel == null || rootViewId != ((PageNode) currentTreeModel.getRoot()).getViewId()) {
							log.info("Loading TreeModel for Internal Link... Please wait :)");
							currentTreeModel = new InternallinkTreeModel(new PageNode(comm.getViewComponent4Unit(rootViewId, -1)));
						}
						tree.setModel(currentTreeModel);
						tree.setCellRenderer(new CmsTreeRenderer());
						tree.validate();
						tree.repaint();
						validate();
						repaint();
			*/
		} catch (Exception exe) {
			log.error("Initialization problem", exe);
		}
	}

	void jbInit() throws Exception {
		this.setLayout(new GridBagLayout());

		lblLinkName.setText(Messages.getString("PanInternalLink.lblLinkname"));
		btnSearchAnker.setText(Messages.getString("PanInternalLink.btnSearchAnchor"));
		cboAnker.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent e) {
				if (!loading) {
					if (cboAnker.getSelectedIndex() > 0) {
						anchor = (String) cboAnker.getSelectedItem();
					} else {
						anchor = "";
					}
				}
			}
		});
		btnSearchAnker.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSearchAnkerActionPerformed(e);
			}
		});

		jScrollPane1.getViewport().setBackground(Color.white);
		this.add(lblLinkName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(5, 5, 0, 5), 6, 0));
		this.add(txtLinkname, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 296, 0));
		this.add(cbxRelatedSites, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 0, 5), 0, 2));
		this.add(cbxViewDocuments, new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 0, 5), 0, 2));
		this.add(jScrollPane1, new GridBagConstraints(0, 3, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 0, 5), 418, 336));
		this.add(btnSearchAnker, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 33, 0));
		this.add(cboAnker, new GridBagConstraints(2, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 178, 2));
		this.add(cbxDisplayTypeInline, new GridBagConstraints(0, 5, 3, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 33, 0));
		jScrollPane1.getViewport().add(tree, null);
		this.cbxRelatedSites.setVisible(this.isSymlink);
		this.add(cbxPopup, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

		this.cbxPopup.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				cbxPopupStateChanged(e);
			}
		});
	}

	private void cbxPopupStateChanged(ItemEvent e) {
		this.showPopupPanel(e.getStateChange() == ItemEvent.SELECTED);
	}

	private void showPopupPanel(boolean show) {
		if (show) {
			this.add(this.getPanPopupDetails(), new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		} else {
			this.remove(this.getPanPopupDetails());
		}
		this.revalidate();
		this.repaint();
	}

	private PanPopupDetails getPanPopupDetails() {
		if (this.panPopupDetails == null) {
			this.panPopupDetails = new PanPopupDetails();
		}
		return this.panPopupDetails;
	}

	private void treeClick(TreePath treePath) throws Exception {
		PageNode pageNode = (PageNode) treePath.getLastPathComponent();
		pageNode.loadChildren();

		if (pageNode.isRoot()) {
			TreePath path = new TreePath(currentTreeModel.getPathToRoot(pageNode));
			tree.setSelectionPath(path);
		} else if (!pageNode.isLeaf()) {
			TreePath path = new TreePath(currentTreeModel.getPathToRoot(pageNode));
			if (tree.isCollapsed(path)) {
				tree.expandPath(path);
				tree.setSelectionPath(path);
			}
		}
		targetViewId = pageNode.getViewId();
		targetViewLevel = pageNode.getViewComponent().getViewLevel();
	}

	void btnSearchAnkerActionPerformed(ActionEvent e) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				searchAnchor();
				setCursor(Cursor.getDefaultCursor());
			}
		});
		t.setPriority(Thread.NORM_PRIORITY);
		t.start();
	}

	public void setAnchorVisible(boolean vis) {
		btnSearchAnker.setVisible(vis);
		cboAnker.setVisible(vis);
	}

	public void setDisplayTypeEditable(boolean editable) {
		this.cbxDisplayTypeInline.setVisible(editable);
	}

	private void searchAnchor() {
		loading = true;
		TreePath tp = this.tree.getSelectionPath();
		if (tp != null) {
			PageNode pageNode = (PageNode) tp.getLastPathComponent();
			ViewComponentValue vcd = pageNode.getViewComponent();
			if (vcd.getViewType() == Constants.VIEW_TYPE_CONTENT || vcd.getViewType() == Constants.VIEW_TYPE_UNIT) {
				String[] anchArr = comm.getAnchors(new Integer(vcd.getReference()).intValue());
				this.cboAnker.removeAllItems();
				this.cboAnker.addItem(rb.getString("content.modules.internalLink.noAnchor"));
				if (anchArr != null) {
					for (int i = 0; i < anchArr.length; i++) {
						this.cboAnker.addItem(AbstractModule.getURLDecoded(anchArr[i]));
					}
				}
			}
		}
		loading = false;
	}

	public boolean isModuleValid() {
		errMsg = "";

		PageNode pageNode = null;
		try {
			pageNode = (PageNode) tree.getSelectionPath().getLastPathComponent();
		} catch (Exception exe) {
		}
		if (pageNode == null) {
			errMsg = rb.getString("exception.LinkRequired");
		} else if (pageNode.getViewComponent().getViewType() == Constants.VIEW_TYPE_INTERNAL_LINK || pageNode.getViewComponent().getViewType() == Constants.VIEW_TYPE_EXTERNAL_LINK) {
			errMsg = rb.getString("exception.ValidLinkRequired");
		}
		if (!treeLink) {
			if (txtLinkname.getText().equals("")) {
				if (errMsg.equals("")) {
					errMsg = rb.getString("exception.LinknameRequired");
				} else {
					errMsg = errMsg + "\n" + rb.getString("exception.LinknameRequired");
				}
			}
		}
		if (this.cbxPopup.isVisible()) { return (errMsg.equals("")) && this.panPopupDetails.isModuleValid(); }
		return (errMsg.equals(""));
	}

	public String getValidationError() {
		if (this.cbxPopup.isVisible()) {
			if (this.errMsg.length() > 0 && this.panPopupDetails.getValidationError().length() > 0) {
				this.errMsg += "\n";
			}
			this.errMsg += this.panPopupDetails.getValidationError();
		}
		return errMsg;
	}

	public Node getProperties() {
		Element root = ContentManager.getDomDoc().createElement("linkRoot");
		this.module.setDescription(this.txtLinkname.getText());

		if (targetViewId > 0) {
			Element elm = ContentManager.getDomDoc().createElement("internalLink");
			CDATASection txtNode = ContentManager.getDomDoc().createCDATASection(this.txtLinkname.getText());
			elm.appendChild(txtNode);
			elm.setAttribute("viewid", targetViewId + "");
			elm.setAttribute("level", targetViewLevel);
			if (anchor != null && !"".equalsIgnoreCase(anchor)) {
				/*
				PageNode pageNode = (PageNode) tree.getSelectionPath().getLastPathComponent();
				ViewComponentValue vcd = pageNode.getViewComponent();
				String[] availableAnchors = this.comm.getAnchors(new Integer(vcd.getReference()).intValue());
				ArrayList<String> avAnchorsList = new ArrayList<String>(Arrays.asList(availableAnchors));
				HashSet<String> avAnchors = new HashSet<String>(avAnchorsList);
				if (avAnchors.contains(anchor)) {
				*/
				elm.setAttribute("anchor", AbstractModule.getURLEncoded(anchor));
				/*
				} else {
				if (log.isDebugEnabled()) log.debug("Anchor " + anchor + " does not exist and is NOT saved");
				}
				*/
			}
			if (cbxDisplayTypeInline.isSelected()) {
				elm.setAttribute("displayType", "inline");
			} else {
				elm.setAttribute("displayType", "block");
			}
			root.appendChild(elm);
			if (this.cbxPopup.isVisible() && this.cbxPopup.isSelected()) {
				root.appendChild(this.getPanPopupDetails().getProperties());
			}
		}
		return root;
	}

	public void clear() {
		targetViewId = -1;
		this.txtLinkname.setText("");
		this.module.setDescription("");
		if (this.panPopupDetails != null) {
			this.remove(this.panPopupDetails);
		}
		this.panPopupDetails = null;
	}

	public void setProperties(Node prop) {
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		try {
			ItemListener[] sil = cbxRelatedSites.getItemListeners();
			for (int i = 0; i < sil.length; i++) {
				cbxViewDocuments.removeItemListener(sil[i]);
			}
			cbxRelatedSites.removeAllItems();
			Iterator<DropDownHolder> sit = linkCache.getSites().iterator();
			while (sit.hasNext()) {
				DropDownHolder ddh = sit.next();
				cbxRelatedSites.addItem(ddh);
			}
			cbxRelatedSites.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						DropDownHolder currentSite = (DropDownHolder) cbxRelatedSites.getSelectedItem();
						switchSitesCombo(currentSite);
					}
				}
			});
			// initially select current site and default viewdocument
			DropDownHolder currentSite = linkCache.getSiteDropDownHolder(comm.getSiteId());
			this.switchSitesCombo(currentSite);
		} catch (Exception e) {
			log.error("Error on filling sites and viewdocuments-dropdown: " + e.getMessage(), e);
		}
		try {
			Node nde = XercesHelper.findNode(prop, "./popup");
			if (nde != null) {
				this.getPanPopupDetails().setProperties(nde);
				this.cbxPopup.setSelected(true);
			} else {
				if (this.panPopupDetails != null) {
					this.remove(this.panPopupDetails);
				}
				this.panPopupDetails = null;
				this.cbxPopup.setSelected(false);
			}
			this.showPopupPanel(this.cbxPopup.isSelected());
		} catch (Exception exe) {
		}
		try {
			// if this will go, it is the new syntax. after that take prop.getFirstChild();
			Node nde = XercesHelper.findNode(prop, "./internalLink");
			if (nde != null) {
				// this statement has no effect!
				// ((Element) nde).getAttribute("viewid");
				prop = nde;
			}
		} catch (Exception exe) {
		}

		String strId = "";
		try {
			strId = ((Element) prop).getAttribute("viewid");
		} catch (Exception exe) {
		}

		try {
			String displayType = ((Element) prop).getAttribute("displayType");
			this.cbxDisplayTypeInline.setSelected("inline".equalsIgnoreCase(displayType));
		} catch (Exception exe) {
		}

		if (strId != null && !strId.equalsIgnoreCase("")) {
			try {
				String lnkDesc = XercesHelper.getNodeValue(prop);
				this.txtLinkname.setText(lnkDesc);
				this.module.setDescription(lnkDesc);
			} catch (Exception exe) {
			}

			targetViewId = rootViewId;
			try {
				if (Integer.parseInt(strId) > 0) {
					targetViewId = new Integer(strId).intValue();
					if (log.isDebugEnabled()) log.debug("loading targetViewId " + targetViewId);
				}
			} catch (Exception exe) {
			}

			anchor = ((Element) prop).getAttribute("anchor");

			btnSearchAnker.setEnabled(true);

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					try {
						PageNode pageNode = currentTreeModel.findEntry4Id((PageNode) currentTreeModel.getRoot(), targetViewId);
						if (pageNode == null) {
							pageNode = loadTree2View(targetViewId);
						}
						if (pageNode != null) {
							TreePath tp = new TreePath(currentTreeModel.getPathToRoot(pageNode));
							if (log.isDebugEnabled()) log.debug("expandit");
							tree.expandPath(tp);
							if (log.isDebugEnabled()) log.debug("setselectionpath");
							tree.setSelectionPath(tp);
							tree.scrollPathToVisible(tp);
						} else {
							if (log.isDebugEnabled()) log.debug("Could not find ViewComponent " + targetViewId);
						}
					} catch (Exception exe) {
						log.error("Error setting properties in treemodel thread", exe);
					}

					if (anchor != null && !anchor.equals("")) {
						synchronized (anchor) {
							searchAnchor();
							cboAnker.setSelectedItem(AbstractModule.getURLDecoded(anchor));
						}
					} else {
						cboAnker.removeAllItems();
					}
					setCursor(Cursor.getDefaultCursor());
					if (log.isDebugEnabled()) log.debug("finished runner");
				}
			});
		} else {
			targetViewId = 0;
			this.txtLinkname.setText("");
			anchor = null;
			setCursor(Cursor.getDefaultCursor());
			if (log.isDebugEnabled()) log.debug("Got an invalid internal link - won't resolve it");
		}
	}

	private PageNode loadTree2View(int targetVId) {
		if (log.isDebugEnabled()) log.debug("loadTree2View " + targetVId);
		try {
			String[] vec = comm.getParents4ViewComponent(targetVId);
			String viewId;
			PageNode pageNode = null;
			for (int i = 0; i < vec.length; i++) {
				viewId = vec[i];
				pageNode = currentTreeModel.findEntry4Id((PageNode) currentTreeModel.getRoot(), new Integer(viewId).intValue());
				if (pageNode == null) {
					// node may be located in another site?
					ViewDocumentValue vdValue = this.comm.getViewDocument4ViewComponent(new Integer(viewId).intValue());
					if (vdValue != null) {
						DropDownHolder site = linkCache.getSiteDropDownHolder(vdValue.getSiteId());
						DropDownHolder viewDocument = linkCache.getViewDocumentDropDownHolder(vdValue.getSiteId(), vdValue.getViewDocumentId());
						if (site != null && viewDocument != null) {
							this.switchTree(site, viewDocument);
						} else {
							log.warn("The current user \"" + this.comm.getUser().getUserName() + "\" must not access site " + vdValue.getSiteId() + " where the target of this link is located!");
							JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("content.modules.internalLink.target.unavailable.warning"), rb.getString("content.modules.internalLink.target.unavailable.title"), JOptionPane.WARNING_MESSAGE);
						}
					}
				} else if (!pageNode.isInit()) {
					pageNode.loadChildren();
					if (log.isDebugEnabled()) log.debug("loaded " + i);

				}
			}
			pageNode = currentTreeModel.findEntry4Id((PageNode) currentTreeModel.getRoot(), targetViewId);
			return pageNode;
		} catch (Exception exe) {
			log.error("problem loading the tree to this view " + targetVId, exe);
		}
		return null;
	}

	private void switchTree(DropDownHolder currentSite, DropDownHolder currentViewDocument) {
		currentTreeModel = linkCache.getModel(((SiteValue) currentSite.getObject()).getSiteId(), currentViewDocument);
		tree.setModel(currentTreeModel);
		tree.setCellRenderer(new CmsTreeRenderer());
		tree.validate();
		tree.repaint();
		validate();
		repaint();
		cbxRelatedSites.setSelectedItem(currentSite);
		cbxViewDocuments.setSelectedItem(currentViewDocument);
	}

	public static InternalLinkCache getLinkCache() {
		return linkCache;
	}

	private void switchSitesCombo(DropDownHolder selectedSite) {
		DropDownHolder defaultViewDocument = null;
		ItemListener[] il = cbxViewDocuments.getItemListeners();
		for (int i = 0; i < il.length; i++) {
			cbxViewDocuments.removeItemListener(il[i]);
		}
		cbxViewDocuments.removeAllItems();
		Iterator<DropDownHolder> it = linkCache.getViewDocuments(((SiteValue) selectedSite.getObject()).getSiteId()).iterator();
		while (it.hasNext()) {
			DropDownHolder ddh = it.next();
			cbxViewDocuments.addItem(ddh);
			if (((ViewDocumentValue) ddh.getObject()).isIsVdDefault()) defaultViewDocument = ddh;
		}
		cbxViewDocuments.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					DropDownHolder currentSite = (DropDownHolder) cbxRelatedSites.getSelectedItem();
					DropDownHolder currentViewDocument = (DropDownHolder) cbxViewDocuments.getSelectedItem();
					switchTree(currentSite, currentViewDocument);
				}
			}
		});
		if (selectedSite != null && defaultViewDocument != null) switchTree(selectedSite, defaultViewDocument);
	}

	public int getLinkTarget() {
		return this.targetViewId;
	}

	public String getLinkName() {
		return this.txtLinkname.getText();
	}

	public void setPopupAvailable(boolean available) {
		this.cbxPopup.setVisible(available);
	}

}
