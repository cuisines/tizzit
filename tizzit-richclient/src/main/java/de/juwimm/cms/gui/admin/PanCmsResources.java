package de.juwimm.cms.gui.admin;

import static de.juwimm.cms.common.Constants.rb;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.jvnet.flamingo.common.CommandButtonDisplayState;
import org.jvnet.flamingo.common.JCommandMenuButton;
import org.jvnet.flamingo.common.icon.ImageWrapperResizableIcon;

import de.juwimm.cms.Messages;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.Constants.ResourceUsageState;
import de.juwimm.cms.content.event.TreeSelectionEventData;
import de.juwimm.cms.content.frame.helper.Utils;
import de.juwimm.cms.gui.controls.ReloadablePanel;
import de.juwimm.cms.gui.ribbon.CommandMenuButton;
import de.juwimm.cms.gui.tree.CmsResourcesTreeModel;
import de.juwimm.cms.gui.tree.TreeNode;
import de.juwimm.cms.gui.tree.CmsResourcesTreeModel.CmsResourcesCellRenderer;
import de.juwimm.cms.gui.tree.CmsResourcesTreeModel.CmsResourcesTreeNode;
import de.juwimm.cms.gui.tree.CmsResourcesTreeModel.CmsStateResourceTreeNode;
import de.juwimm.cms.gui.tree.CmsResourcesTreeModel.DocumentTreeNode;
import de.juwimm.cms.gui.tree.CmsResourcesTreeModel.PictureTreeNode;
import de.juwimm.cms.gui.tree.CmsResourcesTreeModel.SiteTreeNode;
import de.juwimm.cms.gui.tree.CmsResourcesTreeModel.UnitTreeNode;
import de.juwimm.cms.util.ActionHub;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.DocumentSlimValue;
import de.juwimm.cms.vo.PictureSlimstValue;
import de.juwimm.cms.vo.SiteValue;
import de.juwimm.cms.vo.UnitValue;
import de.juwimm.cms.vo.ViewComponentValue;
import de.juwimm.cms.vo.ViewDocumentValue;
import de.juwimm.swing.NoResizeScrollPane;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class PanCmsResources extends JPanel implements ReloadablePanel {
	private static Logger log = Logger.getLogger(PanCmsResources.class);
	private JTree treeResources;
	private JPanel detailsPane;
	private JSplitPane splitPane;
	private CmsResourcesTreeModel treeModel;
	private Communication communication;
	private JPanel resourcePanel;
	private JLabel nameLabel;
	private JLabel typeLabel;
	private JLabel createdLabel;
	private JLabel resourceStateLabel;
	JButton resourcePreview;

	private JLabel nameValueLabel;
	private JLabel typeValueLabel;
	private JLabel createdValueLabel;
	private JLabel resourceStateValueLabel;
	private JTable viewComponentsTable;

	private JPanel treeControlPanel;
	private JPanel resourcePreviewPanel;
	private JButton deleteResource;
	private MultiComboBox filterMultiComboBox;
	private ViewComponentsTableModel viewComponentsTableModel = new ViewComponentsTableModel();

	private boolean isLoading = false;

	public static final String TitleKey = "panCmsResources.title";

	private static final long serialVersionUID = 1L;

	PanCmsResources(Communication communication) {
		splitPane = new JSplitPane();
		treeResources = new JTree();
		resourceStateValueLabel = new JLabel();
		resourceStateLabel = new JLabel(rb.getString("panCmsResources.resourceState"));
		detailsPane = new JPanel();
		resourcePanel = new JPanel();
		nameLabel = new JLabel();
		typeLabel = new JLabel();
		createdLabel = new JLabel();
		nameValueLabel = new JLabel();
		typeValueLabel = new JLabel();
		createdValueLabel = new JLabel();
		treeControlPanel = new JPanel();
		deleteResource = new JButton();
		viewComponentsTable = new JTable();
		resourcePreviewPanel = new JPanel();
		resourcePreview = new JButton();
		this.communication = communication;

		initLayout();
		initListeners();
	}

	private void initListeners() {
		treeResources.addTreeWillExpandListener(new TreeWillExpandListener() {

			public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {

			}

			public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
				TreePath path = event.getPath();
				refreshNode(path);
			}
		});
		treeResources.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent e) {
				if (isLoading) {
					return;
				}
				TreePath path = e.getPath();
				Object entry = path.getLastPathComponent();
				if (entry instanceof CmsStateResourceTreeNode) {
					resourcePreviewPanel.setVisible(true);
					if (entry instanceof DocumentTreeNode) {
						updateDocumentDetails((DocumentTreeNode) entry);
					} else if (entry instanceof PictureTreeNode) {
						updatePictureDetails((PictureTreeNode) entry);
					}
				} else {
					emptyDetails();
				}

			}
		});

		treeResources.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TreePath path = treeResources.getPathForLocation(e.getX(), e.getY());
				if (path == null) return;
				Object node = path.getLastPathComponent();
				DocumentTreeNode documentNode = null;
				PictureTreeNode pictureNode = null;

				if (node instanceof CmsStateResourceTreeNode && ((CmsStateResourceTreeNode) node).getState() == ResourceUsageState.Used) {
					return;
				}

				if (node instanceof DocumentTreeNode) {
					documentNode = (DocumentTreeNode) node;
				} else if (node instanceof PictureTreeNode) {
					pictureNode = (PictureTreeNode) node;
				} else {
					return;
				}
				if (e.getX() > treeResources.getPathBounds(path).x + new JCheckBox().getPreferredSize().width) {
					return;
				}
				if (documentNode != null) {
					if (documentNode.toogleCheck()) {
						treeModel.addResourceToDelete(documentNode);
					} else {
						treeModel.removeResourceFromDelete(documentNode);
					}
				} else {
					if (pictureNode.toogleCheck()) {
						treeModel.addResourceToDelete(pictureNode);
					} else {
						treeModel.removeResourceFromDelete(pictureNode);
					}

				}
				treeResources.treeDidChange();
			}
		});
		deleteResource.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						//if resources with state UsedInOlderVersions are selcted ask the user if he wants to delete history content versions
						boolean forceDeleteHistory = false;
						if (treeModel.areResourcesUsedInHistory()) {
							//ask user if he wants to delete also history versions where resources appear
							if (JOptionPane.showConfirmDialog(splitPane, rb.getString("panCmsResources.deleteHistory.question"), rb.getString("panCmsResources.deleteHistory.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
								forceDeleteHistory = true;
							} else {
								return;
							}
						}
						UIConstants.setWorker(true);
						try {
							communication.removeResources(treeModel.getPicturesToDelete(), treeModel.getDocumentsToDelete(), forceDeleteHistory);
							treeModel.deleteResourcesFromTree();
							treeResources.treeDidChange();
						} catch (Exception e) {
							if (e.getMessage().contains("validation exception")) {
								if (log.isDebugEnabled()) {
									log.debug("validation exception on delete resources");
								}
								JOptionPane.showConfirmDialog(splitPane, rb.getString("panCmsResources.delete.validationException"), rb.getString("dialog.title"), JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
								refreshTree();
							} else {
								log.warn("unknown error on delete resources");
								if (log.isDebugEnabled()) {
									log.debug(e);
								}
							}
						} finally {
							UIConstants.setWorker(false);
						}
					}
				});
			}

		});
	}

	private void refreshNode(TreePath path) {
		try {

			Object entry = path.getLastPathComponent();
			if (entry instanceof UnitTreeNode) {
				UIConstants.setWorker(true);
				splitPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				UnitTreeNode unitNode = (UnitTreeNode) entry;
				unitNode.removeAllChildren();
				List<Boolean> values = filterMultiComboBox.getValues();
				Map<Object, ResourceUsageState> resources = (Map<Object, ResourceUsageState>) communication.getResources4Unit(unitNode.getId(), values.get(0), values.get(1), values.get(2), values.get(3));
				if (resources != null && resources.size() > 0) {
					for (Entry<Object, ResourceUsageState> resource : resources.entrySet()) {
						if (resource.getKey() instanceof DocumentSlimValue) {
							unitNode.add(new DocumentTreeNode((DocumentSlimValue) resource.getKey(), resource.getValue()));
						} else {
							unitNode.add(new PictureTreeNode((PictureSlimstValue) resource.getKey(), resource.getValue()));
						}
					}
				}
				treeModel.clearResourcesSelection();
				treeModel.nodeStructureChanged(unitNode);
				treeResources.treeDidChange();
			}
		} finally {
			UIConstants.setWorker(false);
			splitPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	public void updateDocumentDetails(DocumentTreeNode entry) {
		DocumentSlimValue value = entry.getValue();
		nameValueLabel.setText(value.getDocumentName());
		typeValueLabel.setText(value.getMimeType());
		createdValueLabel.setText(new SimpleDateFormat(rb.getString("General.ShortDateTimeFormat")).format(new Date(value.getTimeStamp())));
		resourceStateValueLabel.setText(rb.getString("panCmsResources.state." + entry.getState().getKey()));

		if (entry.getState() != ResourceUsageState.Unsused) {
			Set<ViewComponentValue> viewComponentValues = (Set<ViewComponentValue>) communication.getDocumentUsage(value.getDocumentId());
			this.viewComponentsTableModel.setRows(viewComponentValues);
			//this.viewComponentsTable.setModel(viewComponentsTableModel);
			this.viewComponentsTable.setVisible(true);
		} else {
			this.viewComponentsTable.setVisible(false);
		}
		ImageIcon ico = Utils.getIcon4Extension(Utils.getExtension(value.getDocumentName()));
		resourcePreview.setIcon(ico);
		resourcePreview.setVisible(true);
	}

	public void updatePictureDetails(final PictureTreeNode entry) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UIConstants.setWorker(true);
				PictureSlimstValue value = entry.getValue();
				nameValueLabel.setText(value.getPictureName());
				typeValueLabel.setText(value.getMimeType());
				createdValueLabel.setText(new SimpleDateFormat(rb.getString("General.ShortDateTimeFormat")).format(new Date(value.getTimeStamp())));
				resourceStateValueLabel.setText(rb.getString("panCmsResources.state." + entry.getState().getKey()));
				if (entry.getState() != ResourceUsageState.Unsused) {
					Set<ViewComponentValue> viewComponentValues = (Set<ViewComponentValue>) communication.getPictureUsage(value.getPictureId());
					viewComponentsTableModel.setRows(viewComponentValues);
					//viewComponentsTable.setModel(new TableSorter(viewComponentsTableModel, viewComponentsTable.getTableHeader()));
					viewComponentsTable.setVisible(true);
				} else {
					viewComponentsTable.setVisible(false);
				}

				try {

					Icon ico;
					ico = new ImageIcon(communication.getThumbnail(value.getPictureId()));
					resourcePreview.setIcon(ico);
					resourcePreview.setVisible(true);

				} catch (Exception e) {
					if (log.isDebugEnabled()) {
						log.debug("PanCmsResources: image preview could not be loaded");
					}
					resourcePreview.setVisible(false);
				} finally {
					UIConstants.setWorker(false);
				}
			}
		});
	}

	public void emptyDetails() {
		nameValueLabel.setText("");
		typeValueLabel.setText("");
		createdValueLabel.setText("");
		resourceStateValueLabel.setText("");
		viewComponentsTableModel.removeData();
		viewComponentsTable.setVisible(false);
		resourcePreviewPanel.setVisible(false);
		//resourcePreview.setVisible(false);
		//resourcePanel.setVisible(false);
		viewComponentsTable.repaint();
	}

	private void initLayout() {
		this.setLayout(new BorderLayout());
		this.setSize(636, 271);

		viewComponentsTable.setModel(viewComponentsTableModel);

		viewComponentsTable.addMouseListener(new CellListener(viewComponentsTable, 1));
		viewComponentsTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, final Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				this.setText((String) value);
				this.setForeground(Color.blue);
				return this;
			}

		});
		JScrollPane tableScrollPane = new JScrollPane(viewComponentsTable);
		viewComponentsTable.setFillsViewportHeight(true);

		treeResources.setAutoscrolls(true);
		detailsPane.setLayout(new BorderLayout());
		detailsPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 3, 3));
		treeResources.setBorder(BorderFactory.createEmptyBorder(5, 5, 3, 3));
		deleteResource.setIcon(UIConstants.ACTION_TREE_NODE_DELETE);
		deleteResource.setToolTipText(rb.getString("panCmsResources.delete"));
		treeControlPanel.add(deleteResource);
		resourcePanel.setBorder(new TitledBorder(rb.getString("panCmsResources.resourceDetails")));
		detailsPane.add(resourcePanel, BorderLayout.NORTH);
		resourcePanel.setLayout(new GridBagLayout());
		resourcePreview.setPreferredSize(new Dimension(100, 100));
		resourcePreview.setVisible(false);
		resourcePreviewPanel.add(resourcePreview);

		resourcePanel.add(nameLabel, new GridBagConstraints(0, 0, 1, 1, 0.5, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0), 0, 0));
		resourcePanel.add(nameValueLabel, new GridBagConstraints(1, 0, 1, 1, 0.5, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0), 0, 0));
		resourcePanel.add(resourcePreviewPanel, new GridBagConstraints(2, 0, 1, 5, 0.5, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0), 0, 0));

		resourcePanel.add(resourceStateLabel, new GridBagConstraints(0, 1, 1, 1, 0.5, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0), 0, 0));
		resourcePanel.add(resourceStateValueLabel, new GridBagConstraints(1, 1, 1, 1, 0.5, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0), 0, 0));

		resourcePanel.add(typeLabel, new GridBagConstraints(0, 2, 1, 1, 0.5, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0), 0, 0));
		resourcePanel.add(typeValueLabel, new GridBagConstraints(1, 2, 1, 1, 0.5, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0), 0, 0));

		resourcePanel.add(createdLabel, new GridBagConstraints(0, 3, 1, 1, 0.5, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0), 0, 0));
		resourcePanel.add(createdValueLabel, new GridBagConstraints(1, 3, 1, 1, 0.5, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0), 0, 0));
		resourcePanel.add(new JLabel(rb.getString("panCmsResources.usingViewComponents.label")), new GridBagConstraints(0, 4, 1, 1, 0.5, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0), 0, 0));
		resourcePanel.add(tableScrollPane, new GridBagConstraints(0, 5, 3, 1, 0.5, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0), 0, 0));

		nameLabel.setText(rb.getString("panCmsResources.details.name"));
		typeLabel.setText(rb.getString("panCmsResources.details.type"));
		createdLabel.setText(rb.getString("panCmsResources.details.created"));

		NoResizeScrollPane treeScrolable = new NoResizeScrollPane(treeResources);
		Dimension treeSize = new Dimension(300, 600);
		treeScrolable.setPreferredSize(treeSize);
		treeScrolable.setSize(treeSize);
		treeScrolable.setMaximumSize(treeSize);
		treeScrolable.setMinimumSize(new Dimension(150, 600));
		treeScrolable.setVerifyInputWhenFocusTarget(true);

		JPanel treeContainer = new JPanel(new BorderLayout());
		filterMultiComboBox = new MultiComboBox(this);
		filterMultiComboBox.addSeparator(rb.getString("panCmsResources.documents"));
		filterMultiComboBox.addItem(rb.getString("panCmsResources.used"), true);
		filterMultiComboBox.addItem(rb.getString("panCmsResources.unused"), true);
		filterMultiComboBox.addSeparator(rb.getString("panCmsResources.pictures"));
		filterMultiComboBox.addItem(rb.getString("panCmsResources.used"), true);
		filterMultiComboBox.addItem(rb.getString("panCmsResources.unused"), true);

		treeContainer.add(filterMultiComboBox, BorderLayout.NORTH);
		treeContainer.add(treeScrolable, BorderLayout.CENTER);
		treeContainer.add(treeControlPanel, BorderLayout.SOUTH);
		splitPane.setLeftComponent(treeContainer);
		splitPane.setRightComponent(detailsPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerSize(3);
		splitPane.setDividerLocation(230);

		this.add(splitPane);

	}
	private static class ViewComponentsTableModel extends DefaultTableModel {

		private static final long serialVersionUID = -2736913744647681753L;

		@SuppressWarnings("unchecked")
		public ViewComponentsTableModel() {
			super();
			columnIdentifiers.add(rb.getString("panCmsResources.viewComponent.viewComponentId"));
			columnIdentifiers.add(rb.getString("panCmsResources.viewComponent.urlLinkName"));
			columnIdentifiers.add(rb.getString("panCmsResources.viewComponent.displayLinkName"));
		}

		@SuppressWarnings("unchecked")
		private void addRow(ViewComponentValue value) {
			Vector rowDate = new Vector();
			rowDate.add(value.getViewComponentId());
			rowDate.add(value.getUrlLinkName());
			rowDate.add(value.getDisplayLinkName());
			//in this column it will be a link 
			rowDate.add("");
			super.addRow(rowDate);
		}

		public void setRows(Set<ViewComponentValue> views) {
			this.dataVector.removeAllElements();
			if (views != null) {
				for (ViewComponentValue viewComponent : views) {
					this.addRow(viewComponent);
				}
			}
			fireTableRowsInserted(getRowCount(), getRowCount());
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

		public void removeData() {
			this.dataVector.removeAllElements();
			fireTableRowsInserted(getRowCount(), getRowCount());
		}
	}

	public void unload() {

	}

	public void reload() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UIConstants.setWorker(true);
				isLoading = true;
				loadTree();
				emptyDetails();
				isLoading = false;
				UIConstants.setWorker(false);
			}
		});
	}

	private void loadTree() {
		if (treeModel == null) {
			treeModel = new CmsResourcesTreeModel(new DefaultMutableTreeNode(rb.getString("panCmsResources.tree.rootNode")));
			treeResources.setModel(treeModel);
			treeResources.setCellRenderer(new CmsResourcesCellRenderer());
		} else {
			treeModel.getRoot().removeAllChildren();
		}

		SiteValue[] sites = null;
		if (communication.getUser().isMasterRoot()) {
			//getAllSites
			sites = communication.getAllSites();
		} else {
			//getActiveSite
			sites = communication.getAllSites4CurrentUser();
		}

		if (sites != null) {
			for (SiteValue site : sites) {
				CmsResourcesTreeNode siteNode = new SiteTreeNode(site);
				treeModel.getRoot().add(siteNode);
				loadUnit(siteNode);
			}
		}
		treeModel.nodeStructureChanged(treeModel.getRoot());
	}

	private void loadUnit(CmsResourcesTreeNode siteNode) {
		UnitValue[] units = communication.getAllUnits4Site(siteNode.getId());
		if (units != null) {
			for (UnitValue unit : units) {
				UnitTreeNode unitNode = new UnitTreeNode(unit);
				unitNode.add(new TreeNode("empty"));
				siteNode.add(unitNode);
			}
		}
	}

	public void refreshTree() {
		Enumeration<TreePath> paths = treeResources.getExpandedDescendants(new TreePath(treeResources.getModel().getRoot()));
		while (paths.hasMoreElements()) {
			TreePath currentPath = paths.nextElement();
			if (currentPath.getPathCount() == 3) {
				try {
					treeResources.fireTreeWillExpand(currentPath);
				} catch (ExpandVetoException e) {
					log.info("filter tree error");
				}
			}
		}
	}

	public void save() {

	}

	private static class MultiComboBox extends JPanel {
		private static final long serialVersionUID = 1L;
		private JButton dropButton;
		private List<String> items;
		private List<JCheckBox> values;
		private JPopupMenu popup;
		private PanCmsResources panelResources;
		private int itemIndex = 0;
		private MouseAdapter exitMouseAdapter;

		public MultiComboBox(PanCmsResources panel) {
			this.panelResources = panel;
			dropButton = new JButton(rb.getString("panCmsResources.filter"));
			items = new ArrayList<String>();
			values = new ArrayList<JCheckBox>();
			popup = new JPopupMenu();
			popup.setLayout(new GridBagLayout());
			exitMouseAdapter = new MouseAdapter() {
				@Override
				public void mouseExited(MouseEvent e) {
					Point p = e.getPoint();
					p.x += e.getComponent().getParent().getX();
					p.y += e.getComponent().getParent().getY();
					if (!popup.contains(p)) {
						setVisiblePopup(false, e);
						panelResources.refreshTree();
					}
				}
			};
			dropButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					setVisiblePopup(!popup.isVisible(), e);
					if (!popup.isVisible()) {
						panelResources.refreshTree();
					}
				}
			});

			popup.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseExited(MouseEvent e) {
					Point p = e.getPoint();
					if (!popup.contains(p)) {
						setVisiblePopup(false, e);
						panelResources.refreshTree();
					}
				}
			});

			dropButton.addFocusListener(new FocusListener() {

				public void focusGained(FocusEvent e) {
				}

				public void focusLost(FocusEvent e) {
					//					if(popup.isVisible()){
					//						refreshTree();
					//					}
					setVisiblePopup(false, null);
				}

			});
			this.add(dropButton);
			this.add(popup);

		}

		private void setVisiblePopup(boolean visible, MouseEvent e) {
			if (visible) {
				Point popupPoint = e.getLocationOnScreen();

				popupPoint.y += (dropButton.getHeight() - e.getPoint().getY());
				popupPoint.x -= e.getPoint().getX();
				popup.setLocation(popupPoint);
			}
			this.popup.setVisible(visible);
		}

		private void addItem(String item, boolean state) {
			items.add(item);
			JCheckBox checkBox = new JCheckBox();
			checkBox.addMouseListener(exitMouseAdapter);
			values.add(checkBox);
			checkBox.setSelected(state);
			GridBagConstraints gridBagConstraint = new GridBagConstraints();
			gridBagConstraint.gridy = itemIndex;
			gridBagConstraint.anchor = GridBagConstraints.WEST;
			gridBagConstraint.insets = new Insets(1, 18, 1, 40);
			JPanel itemPanel = new JPanel(new BorderLayout());
			itemPanel.add(checkBox, BorderLayout.WEST);
			itemPanel.add(new JLabel(item), BorderLayout.CENTER);
			popup.add(itemPanel, gridBagConstraint);

			itemIndex++;
		}

		private void addItem(String item) {
			addItem(item, false);
		}

		private void addSeparator(String item) {
			GridBagConstraints gridBagConstraint = new GridBagConstraints();
			gridBagConstraint.gridy = itemIndex;
			gridBagConstraint.insets = new Insets(1, 3, 1, 40);
			gridBagConstraint.anchor = GridBagConstraints.WEST;
			JPanel itemPanel = new JPanel(new BorderLayout());
			itemPanel.add(new JLabel(item), BorderLayout.CENTER);
			popup.add(itemPanel, gridBagConstraint);
			itemIndex++;
		}

		public List<Boolean> getValues() {
			List<Boolean> booleanValues = new ArrayList<Boolean>();
			if (values.size() > 0) {
				for (JCheckBox checkBox : values) {
					booleanValues.add(checkBox.isSelected());
				}
			}
			return booleanValues;
		}

		private JCommandMenuButton createOption(String txt) {
			JCommandMenuButton item = new CommandMenuButton(txt, ImageWrapperResizableIcon.getIcon(UIConstants.EMPTY.getImage(), new Dimension(16, 16)));
			item.setDisplayState(CommandButtonDisplayState.MEDIUM);
			item.setHorizontalAlignment(SwingUtilities.LEFT);
			return item;
		}
	}

	private class CellListener extends MouseAdapter {
		private JTable table;
		private int columnIndexToListen;

		public CellListener(JTable table, int index) {
			this.table = table;
			columnIndexToListen = index;
		}

		public void forwardEvent(MouseEvent e) {
			TableColumnModel columnModel = table.getColumnModel();
			int column = columnModel.getColumnIndexAtX(e.getX());
			int row = e.getY() / table.getRowHeight();

			if (row >= table.getRowCount() || row < 0 || column >= table.getColumnCount() || column < 0 || column != columnIndexToListen) {
				return;
			}
			Integer viewComponentId = (Integer) table.getModel().getValueAt(row, 0);
			ViewDocumentValue viewDocument = communication.getViewDocument4ViewComponent(viewComponentId);
			//selected view compoment is not contained in the active site 
			if (!viewDocument.getSiteId().equals(communication.getSiteId())) {
				JOptionPane.showConfirmDialog(splitPane, Messages.getString("panCmsResources.goto.error", communication.getSiteName()), rb.getString("dialog.title"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			UnitTreeNode unitNode = (UnitTreeNode) treeResources.getSelectionModel().getLeadSelectionPath().getParentPath().getLastPathComponent();
			TreeSelectionEventData treeSelectionData = new TreeSelectionEventData();
			treeSelectionData.setViewDocument(viewDocument);
			treeSelectionData.setViewComponentId(viewComponentId);
			treeSelectionData.setUnitId(unitNode.getValue().getUnitId());

			ActionHub.fireActionPerformed(new ActionEvent(treeSelectionData, ActionEvent.ACTION_PERFORMED, Constants.ACTION_VIEW_EDITOR_WITH_SELECTION));
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			forwardEvent(e);
		}
	}
}
