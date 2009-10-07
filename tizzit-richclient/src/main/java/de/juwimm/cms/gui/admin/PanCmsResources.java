package de.juwimm.cms.gui.admin;

import static de.juwimm.cms.common.Constants.rb;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

import de.juwimm.cms.gui.controls.ReloadablePanel;
import de.juwimm.cms.gui.controls.UnloadablePanel;
import de.juwimm.cms.gui.tree.CmsResourcesTreeModel;
import de.juwimm.cms.gui.tree.TreeNode;
import de.juwimm.cms.gui.tree.CmsResourcesTreeModel.CmsResourcesCellRenderer;
import de.juwimm.cms.gui.tree.CmsResourcesTreeModel.CmsResourcesTreeNode;
import de.juwimm.cms.gui.tree.CmsResourcesTreeModel.DocumentTreeNode;
import de.juwimm.cms.gui.tree.CmsResourcesTreeModel.PictureTreeNode;
import de.juwimm.cms.gui.tree.CmsResourcesTreeModel.SiteTreeNode;
import de.juwimm.cms.gui.tree.CmsResourcesTreeModel.UnitTreeNode;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.DocumentSlimValue;
import de.juwimm.cms.vo.PictureSlimstValue;
import de.juwimm.cms.vo.SiteValue;
import de.juwimm.cms.vo.UnitValue;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class PanCmsResources extends JPanel implements UnloadablePanel,ReloadablePanel{
	
	private JTree treeResources;
	private JPanel detailsPane;
	private JSplitPane splitPane;
	private CmsResourcesTreeModel treeModel;
	private Communication communication;
	private JPanel resourcePanel;
	private JLabel nameLabel;
	private JLabel typeLabel;
	private JLabel createdLabel;
	private JLabel sizeLabel;
	
	private JLabel nameValueLabel;
	private JLabel typeValueLabel;
	private JLabel createdValueLabel;
	private JLabel sizeValueLabel;
	
	private JPanel treeControlPanel;
	private JButton deleteResource;
	
	public static final String TitleKey = "panCmsResources.title";
	
	private static final long serialVersionUID = 1L;
	
	PanCmsResources(Communication communication){
		splitPane = new JSplitPane();
		treeResources = new JTree();
		detailsPane = new JPanel();		
		resourcePanel = new JPanel();
		nameLabel = new JLabel();
		typeLabel = new JLabel();
		createdLabel = new JLabel();
		sizeLabel = new JLabel();		
		nameValueLabel = new JLabel();
		typeValueLabel = new JLabel();
		createdValueLabel = new JLabel();
		sizeValueLabel = new JLabel();		
		treeControlPanel = new JPanel();
		deleteResource = new JButton();
		
		this.communication = communication;
		initLayout();
		initListeners();
	}

	
	private void initListeners() {
		treeResources.addTreeWillExpandListener(new TreeWillExpandListener(){

			public void treeWillCollapse(TreeExpansionEvent event)throws ExpandVetoException {				
				
			}

			public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
				TreePath path = event.getPath();
				Object entry = path.getLastPathComponent();				
				if(entry instanceof UnitTreeNode){
					UIConstants.setWorker(true);
					splitPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					UnitTreeNode unitNode = (UnitTreeNode)entry;
					unitNode.removeAllChildren();
					List unsusedResources = communication.getUnsusedResources4Unit(unitNode.getId());
					if(unsusedResources != null && unsusedResources.size() > 0){
						for(Object resource:unsusedResources){
							if(resource instanceof DocumentSlimValue){
								unitNode.add(new DocumentTreeNode((DocumentSlimValue)resource));
							}else{
								unitNode.add(new PictureTreeNode((PictureSlimstValue)resource));							
							}
						}
						treeModel.nodeStructureChanged(unitNode);
					}
					UIConstants.setWorker(false);
					splitPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}			
		});
		treeResources.addTreeSelectionListener(new TreeSelectionListener(){

			public void valueChanged(TreeSelectionEvent e) {
				TreePath path = e.getPath();
				Object entry = path.getLastPathComponent();
				if(entry instanceof DocumentTreeNode){
					updateDocumentDetails((DocumentTreeNode)entry);					
				}else if(entry instanceof PictureTreeNode){
					updatePictureDetails((PictureTreeNode)entry);
				}else{
					emptyDetails();
				}
				
			}			
		});
		
		treeResources.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				  	TreePath path = treeResources.getPathForLocation(e.getX(), e.getY()); 
			        if(path==null) 
			            return;
			        Object node = path.getLastPathComponent();
			        DocumentTreeNode documentNode = null;
			        PictureTreeNode pictureNode = null;
			        
			        if (node instanceof DocumentTreeNode){
			        	documentNode = (DocumentTreeNode)node ;
			        }else if(node instanceof PictureTreeNode) {
			        	pictureNode = (PictureTreeNode)node ;
			        }else {
			        	return;
			        }
			        if(e.getX()>treeResources.getPathBounds(path).x+new JCheckBox().getPreferredSize().width){ 
			        	return;
			        }
		        	if(documentNode!= null){	        	
		        		if(documentNode.toogleCheck()){
		        			treeModel.addResourceToDelete(documentNode);
		        		}else{
		        			treeModel.removeResourceFromDelete(documentNode);
		        		}
		        	}else{
		        		if(pictureNode.toogleCheck()){
		        			treeModel.addResourceToDelete(pictureNode);
		        		}else{
		        			treeModel.removeResourceFromDelete(pictureNode);
		        		}
		        		
		        	}
		            treeResources.treeDidChange();
			        }
			});
		deleteResource.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UIConstants.setWorker(true);
						communication.removeResources(treeModel.getPicturesToDelete(),treeModel.getDocumentsToDelete());
						treeModel.deleteResourcesFromTree();
						treeResources.treeDidChange();
						UIConstants.setWorker(false);
					}
				});
			}
			
		});
	}

	public void updateDocumentDetails(DocumentTreeNode entry){
		DocumentSlimValue value = entry.getValue();
		nameValueLabel.setText(value.getDocumentName());
		typeValueLabel.setText(value.getMimeType());
		createdValueLabel.setText(new SimpleDateFormat(rb.getString("General.ShortDateTimeFormat")).format(new Date(value.getTimeStamp())));
	}
	
	public void updatePictureDetails(PictureTreeNode entry){
		PictureSlimstValue value = entry.getValue();
		nameValueLabel.setText(value.getPictureName());
		typeValueLabel.setText(value.getMimeType());
		createdValueLabel.setText(new SimpleDateFormat(rb.getString("General.ShortDateTimeFormat")).format(new Date(value.getTimeStamp())));		
	}
	
	public void emptyDetails(){		
		nameValueLabel.setText("");
		typeValueLabel.setText("");
		createdValueLabel.setText("");		
	}
	
	private void initLayout(){
		this.setLayout(new BorderLayout());
		this.setSize(636, 271);
		treeResources.setAutoscrolls(true);
		detailsPane.setLayout(new BorderLayout());
		deleteResource.setIcon(UIConstants.ACTION_TREE_NODE_DELETE);
		deleteResource.setToolTipText(rb.getString("panCmsResources.delete"));
		treeControlPanel.add(deleteResource);		
		resourcePanel.setBorder(new TitledBorder(rb.getString("panCmsResources.resourceDetails")));		
		detailsPane.add(resourcePanel,BorderLayout.NORTH);
		resourcePanel.setLayout(new GridBagLayout());
		
		resourcePanel.add(nameLabel,new GridBagConstraints(0, 0, 1, 1, 0.5, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0), 0, 0));
		resourcePanel.add(nameValueLabel,new GridBagConstraints(1, 0, 1, 1, 0.5, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0), 0, 0));
		
//		resourcePanel.add(sizeLabel,new GridBagConstraints(0, 1, 1, 1, 0.5, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0), 0, 0));
//		resourcePanel.add(sizeValueLabel,new GridBagConstraints(1, 1, 1, 1, 0.5, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0), 0, 0));
		
		resourcePanel.add(typeLabel,new GridBagConstraints(0, 2, 1, 1, 0.5, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0), 0, 0));
		resourcePanel.add(typeValueLabel,new GridBagConstraints(1, 2, 1, 1, 0.5, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0), 0, 0));
		
		resourcePanel.add(createdLabel,new GridBagConstraints(0, 3, 1, 1, 0.5, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0), 0, 0));
		resourcePanel.add(createdValueLabel,new GridBagConstraints(1, 3, 1, 1, 0.5, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0), 0, 0));
		
		resourcePanel.add(new JPanel(),new GridBagConstraints(0, 4, 2, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0), 0, 0));		
		
		nameLabel.setText(rb.getString("panCmsResources.details.name"));
		typeLabel.setText(rb.getString("panCmsResources.details.type"));
		sizeLabel.setText(rb.getString("panCmsResources.details.size"));
		createdLabel.setText(rb.getString("panCmsResources.details.created"));
		
		JPanel treeContainer = new JPanel(new BorderLayout());
		treeContainer.add(treeResources,BorderLayout.CENTER);
		treeContainer.add(treeControlPanel,BorderLayout.SOUTH);
		splitPane.setLeftComponent(treeContainer);
		splitPane.setRightComponent(detailsPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerSize(3);
		splitPane.setDividerLocation(230);		
		GridBagConstraints splitPaneConstraints = new GridBagConstraints();
		splitPaneConstraints.insets = new Insets(20,10,10,10);
		this.add(splitPane);				
		
	}
	
	public void unload() {
		
	}

	public void reload() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UIConstants.setWorker(true);
				loadTree();			
				UIConstants.setWorker(false);
			}
		});
	}
	
	private void loadTree(){
		if(treeModel == null){
			treeModel = new CmsResourcesTreeModel(new DefaultMutableTreeNode(rb.getString("panCmsResources.tree.rootNode")));
			treeResources.setModel(treeModel);
			treeResources.setCellRenderer(new CmsResourcesCellRenderer());
		}else{
			treeModel.getRoot().removeAllChildren();
		}
		
		SiteValue[] sites = null;
		if(communication.getUser().isMasterRoot()){
			//getAllSites
			sites = communication.getAllSites();
		}else{
		    //getActiveSite
			sites = communication.getAllSites4CurrentUser();
		}
		
		if(sites !=null){
			for(SiteValue site:sites){
				CmsResourcesTreeNode siteNode = new SiteTreeNode(site);
				treeModel.getRoot().add(siteNode);
				loadUnit(siteNode);
			}
		}
		treeModel.nodeStructureChanged(treeModel.getRoot());
	}
	
	private void loadUnit(CmsResourcesTreeNode siteNode) {
		UnitValue[] units = communication.getAllUnits4Site(siteNode.getId());
		if(units != null){
			for(UnitValue unit:units){
				UnitTreeNode unitNode = new UnitTreeNode(unit);
				unitNode.add(new TreeNode("empty"));
				siteNode.add(unitNode);
			}
		}
	}

	public void save() {
		
	}

}
