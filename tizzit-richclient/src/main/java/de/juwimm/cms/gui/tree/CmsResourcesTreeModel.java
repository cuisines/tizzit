package de.juwimm.cms.gui.tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.DocumentSlimValue;
import de.juwimm.cms.vo.PictureSlimstValue;
import de.juwimm.cms.vo.SiteValue;
import de.juwimm.cms.vo.UnitValue;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class CmsResourcesTreeModel extends DefaultTreeModel {
	
	private static final long serialVersionUID = 7201877101394641381L;
	private Map<Integer,DocumentTreeNode> documentsToDelete;
	private Map<Integer,PictureTreeNode> picturesToDelete;
	
	public CmsResourcesTreeModel(DefaultMutableTreeNode root) {
		super(root);
		documentsToDelete = new HashMap<Integer,DocumentTreeNode>();
		picturesToDelete = new HashMap<Integer,PictureTreeNode>();		
	}
	
	public void addResourceToDelete(DocumentTreeNode documentNode){
		documentsToDelete.put(documentNode.getId(),documentNode);
	}
	
	public void addResourceToDelete(PictureTreeNode pictureTreeNode){
		picturesToDelete.put(pictureTreeNode.getId(),pictureTreeNode);
	}
	
	public void removeResourceFromDelete(DocumentTreeNode documentNode){
		documentsToDelete.remove(documentNode.getId());
	}
	
	public void removeResourceFromDelete(PictureTreeNode pictureTreeNode){
		picturesToDelete.remove(pictureTreeNode.getId());
	}
	
	public Integer[] getPicturesToDelete(){
		return picturesToDelete.keySet().toArray(new Integer[0]);
	}
	public Integer[] getDocumentsToDelete(){
		return documentsToDelete.keySet().toArray(new Integer[0]);
	}
	
	public void deleteResourcesFromTree(){
		for(PictureTreeNode pictureNode :picturesToDelete.values()){
			this.removeNodeFromParent(pictureNode);
			picturesToDelete.remove(pictureNode.getId());
		}
		
		for(DocumentTreeNode documentNode :documentsToDelete.values()){
			this.removeNodeFromParent(documentNode);
			documentsToDelete.remove(documentNode.getId());
		}
		
	}
	
	@Override
	public DefaultMutableTreeNode getRoot() {
		return (DefaultMutableTreeNode)super.getRoot();
	}
	
	public static abstract class CmsResourcesTreeNode<T> extends TreeNode{
		private static final long serialVersionUID = 1L;
		private boolean isChecked = false;
		T value;
		public CmsResourcesTreeNode(String name){
			super(name);			
		}
		public T getValue(){
			return value;
		}
		public abstract Integer getId();
		public boolean toogleCheck(){
			isChecked = !isChecked;
			return isChecked;
		}
		public boolean isChecked(){
			return isChecked;
		}
	}
	
	public static class SiteTreeNode extends CmsResourcesTreeNode<SiteValue>{
		private static final long serialVersionUID = -7236858154971687599L;
		
		public SiteTreeNode(SiteValue site){
			super(site.getName());
			this.value = site;
		}
		@Override
		public ImageIcon getIcon() {
			return UIConstants.ICON_MANDANT;
		}

		@Override
		public Integer getId() {
			return value.getSiteId();
		}
	}
	
	public static class UnitTreeNode extends CmsResourcesTreeNode<UnitValue>{
		private static final long serialVersionUID = 3099538424767131542L;
		
		public UnitTreeNode(UnitValue unit){
			super(unit.getName());
			value = unit;
		}
		
		@Override
		public ImageIcon getIcon() {
			return UIConstants.ICON_UNIT;
		}

		@Override
		public Integer getId() {
			return value.getUnitId();
		}

	}
	public static class DocumentTreeNode extends CmsResourcesTreeNode<DocumentSlimValue>{
		private static final long serialVersionUID = 3777971038493127103L;
		
		public DocumentTreeNode(DocumentSlimValue document) {
			super(document.getDocumentName());
			this.value = document;		
		}
		
		@Override
		public ImageIcon getIcon() {		
			return UIConstants.ICON_CALENDAR;
		}

		@Override
		public boolean isEditable() {
			return true;
		}
		
		@Override
		public Integer getId() {
			return value.getDocumentId();
		}
		
	}
	
	public static class PictureTreeNode extends CmsResourcesTreeNode<PictureSlimstValue>{
		private static final long serialVersionUID = 3777971038493127103L;
		
		public PictureTreeNode(PictureSlimstValue picture) {
			super(picture.getPictureName());
			this.value = picture;		
		}
		
		@Override
		public boolean isEditable() {
			return true;
		}
		
		@Override
		public ImageIcon getIcon() {		
			return UIConstants.ICON_DECRYPTED;
		}

		@Override
		public Integer getId() {
			return value.getPictureId();
		}
		
	}
	
	public static class CmsResourcesCellRenderer extends DefaultTreeCellRenderer{
		private static final long serialVersionUID = 1L;
		JCheckBox checkBox = new JCheckBox();
		JPanel panel = new JPanel();
		
		
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus) {		
			super.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, hasFocus);
			if (value instanceof TreeNode) {
				TreeNode tn = (TreeNode) value;
				setIcon(tn.getIcon());							
			}
			if(value instanceof DocumentTreeNode || value instanceof PictureTreeNode){								
				panel.setBackground(Color.white);
				checkBox.setBackground(Color.white);
				checkBox.setSelected(((CmsResourcesTreeNode)value).isChecked());
				panel.setOpaque(false);
				this.setOpaque(false);
				panel.setLayout(new BorderLayout());
				panel.add(checkBox, BorderLayout.WEST);
				panel.add(this, BorderLayout.CENTER);				
				return panel;
			}
			
			return this;
			
		}
	}	
}
