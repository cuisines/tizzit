package de.juwimm.cms.gui.tree;

import static de.juwimm.cms.client.beans.Application.getBean;

import java.awt.Insets;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.Autoscroll;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.vo.ViewComponentValue;

public class DnDTree extends JTree implements Autoscroll {

	/**	 */
	private static final long serialVersionUID = 1L;
	private final Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private static Logger log = Logger.getLogger(DnDTree.class);

	private Insets insets;

	private int top = 0, bottom = 0, topRow = 0, bottomRow = 0;

	public DnDTree() {
		DragSource dragSource = DragSource.getDefaultDragSource();
		dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, new TreeDragGestureListener());
		DropTarget dropTarget = new DropTarget(this, new TreeDropTargetListener());
	}

	public DnDTree(TreeModel model) {
		super(model);
		DragSource dragSource = DragSource.getDefaultDragSource();
		dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, new TreeDragGestureListener());
		DropTarget dropTarget = new DropTarget(this, new TreeDropTargetListener());
	}

	public Insets getAutoscrollInsets() {
		return insets;
	}

	public void autoscroll(Point p) {
		// Only support up/down scrolling
		top = Math.abs(getLocation().y) + 10;
		bottom = top + getParent().getHeight() - 20;
		int next;
		if (p.y < top) {
			next = topRow--;
			bottomRow++;
			scrollRowToVisible(next);
		} else if (p.y > bottom) {
			next = bottomRow++;
			topRow--;
			scrollRowToVisible(next);
		}
	}

	private static class TreeDragGestureListener implements DragGestureListener {
		public void dragGestureRecognized(DragGestureEvent dragGestureEvent) {
			// Can only drag leafs
			JTree tree = (JTree) dragGestureEvent.getComponent();
			TreePath path = tree.getSelectionPath();
			if (path != null) {
				TreeNode selection = (TreeNode) tree.getLastSelectedPathComponent();
				PageNode entry = (PageNode) tree.getLastSelectedPathComponent();
				Integer viewComponentId = entry.getViewComponent().getViewComponentId();
				if (viewComponentId != null) {
					TransferableDataNode node = new TransferableDataNode(viewComponentId);
					dragGestureEvent.startDrag(DragSource.DefaultCopyDrop, node, new MyDragSourceListener());
				}
			}
		}
	}

	private class TreeDropTargetListener implements DropTargetListener {

		public void dragEnter(DropTargetDragEvent dropTargetDragEvent) {
			// Setup positioning info for auto-scrolling
			top = Math.abs(getLocation().y);
			bottom = top + getParent().getHeight();
			topRow = getClosestRowForLocation(0, top);
			bottomRow = getClosestRowForLocation(0, bottom);
			insets = new Insets(top + 10, 0, bottom - 10, getWidth());
		}

		public void dragExit(DropTargetEvent dropTargetEvent) {
		}

		public void dragOver(DropTargetDragEvent dropTargetDragEvent) {
		}

		public void dropActionChanged(DropTargetDragEvent dropTargetDragEvent) {
		}

		public synchronized void drop(DropTargetDropEvent dropTargetDropEvent) {
			Point location = dropTargetDropEvent.getLocation();
			TreePath path = getPathForLocation(location.x, location.y);
			Object node = path.getLastPathComponent();

			if ((node != null) && (node instanceof TreeNode) && (node instanceof PageNode)) {
				try {
					Transferable tr = dropTargetDropEvent.getTransferable();
					if (tr != null) {
						dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
						Object viewcomponentId = tr.getTransferData(DataFlavor.stringFlavor);
						/**Drop the element to parent*/
						dropNodeToParent((PageNode) node, Integer.parseInt(viewcomponentId.toString()));
						dropTargetDropEvent.dropComplete(true);
						System.out.println("Node transfered " + viewcomponentId);
					} else {
						System.err.println("Rejected");
						dropTargetDropEvent.rejectDrop();
					}
				} catch (IOException io) {
					io.printStackTrace();
					dropTargetDropEvent.rejectDrop();
				} catch (UnsupportedFlavorException ufe) {
					ufe.printStackTrace();
					dropTargetDropEvent.rejectDrop();
				}
			} else {
				System.out.println("Can't drop here");
				dropTargetDropEvent.rejectDrop();
			}
		}

		/**
		 * Drop the node to parent and refresh the tree
		 * @param parent
		 * @param viewComponentId
		 */
		private void dropNodeToParent(PageNode entry, Integer viewComponentId) {
			/**copy to new parent and delete from old one*/
			//ViewComponentValue[] viewComponentArray = comm.copyViewComponentToParent(entry.getViewComponent().getViewComponentId(), new Integer[] {viewComponentId}, ViewComponentEvent.MOVE_LEFT);
			ViewComponentValue[] viewComponentArray = null;
			ViewComponentValue viewComponentValue = null;
			try {
				viewComponentValue = comm.getViewComponent(viewComponentId);
				viewComponentArray = new ViewComponentValue[] {viewComponentValue};
			} catch (Exception e) {
				if (log.isDebugEnabled()) {
					log.debug("Error in dropNodeToParent ", e);
				}
			}

			//comm.getViewComponent4Unit(treeSelectionEventData.getUnitId(), -1);
			CmsTreeModel treeModel = new CmsTreeModel(new PageContentNode(viewComponentValue));
			DnDTree.this.setModel(treeModel);

			for (ViewComponentValue viewComponent : viewComponentArray) {
				PageNode temp = null;
				switch (viewComponent.getViewType()) {
					case Constants.VIEW_TYPE_EXTERNAL_LINK:
						temp = new PageExternallinkNode(viewComponent, treeModel);
						break;
					case Constants.VIEW_TYPE_INTERNAL_LINK:
						temp = new PageInternallinkNode(viewComponent, treeModel);
						break;
					case Constants.VIEW_TYPE_SYMLINK:
						temp = new PageSymlinkNode(viewComponent, treeModel);
						break;
					case Constants.VIEW_TYPE_SEPARATOR:
						temp = new PageSeparatorNode(viewComponent, treeModel);
						break;
					default:
						temp = new PageContentNode(viewComponent, treeModel);
						break;
				}
				entry.insert(temp, 0);
			}
			treeModel.nodeStructureChanged(entry);
		}
		//private void addElement(TreePath path, Object element) {
		/** add new subnode to drop_node, update viewComponent */
		//PageNode entry = (PageNode)DnDTree.this.getLastSelectedPathComponent();
		///TreeNode node = new TreeNode();
		//System.out.println("Added: " + node + " to " + parent);
		//TreeModel model = (TreeModel) (DnDTree.this.getModel());
		//((DefaultTreeModel) model).insertNodeInto(node, parent, parent.getChildCount()); ddd
		//}
	}

	private static class MyDragSourceListener implements DragSourceListener {
		public void dragDropEnd(DragSourceDropEvent dragSourceDropEvent) {
			if (dragSourceDropEvent.getDropSuccess()) {
				int dropAction = dragSourceDropEvent.getDropAction();
				if (dropAction == DnDConstants.ACTION_MOVE) {
					System.out.println("MOVE: remove node");
				}
			}
		}

		public void dragEnter(DragSourceDragEvent dragSourceDragEvent) {
			DragSourceContext context = dragSourceDragEvent.getDragSourceContext();
			int dropAction = dragSourceDragEvent.getDropAction();
			if ((dropAction & DnDConstants.ACTION_COPY) != 0) {
				context.setCursor(DragSource.DefaultCopyDrop);
			} else if ((dropAction & DnDConstants.ACTION_MOVE) != 0) {
				context.setCursor(DragSource.DefaultMoveDrop);
			} else {
				context.setCursor(DragSource.DefaultCopyNoDrop);
			}
		}

		public void dragExit(DragSourceEvent dragSourceEvent) {
		}

		public void dragOver(DragSourceDragEvent dragSourceDragEvent) {
		}

		public void dropActionChanged(DragSourceDragEvent dragSourceDragEvent) {
		}
	}

}

class TransferableDataNode implements Transferable {

	private Integer data;

	public TransferableDataNode(Integer data) {
		this.data = data;
	}

	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		Object returnObject = null;
		if (flavor.equals(DataFlavor.stringFlavor)) {
			Object userObject = data;
			if (userObject == null) {
				returnObject = data.toString();
			} else {
				returnObject = userObject.toString();
			}
		}
		return returnObject;
	}

	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] {DataFlavor.stringFlavor};
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		if (flavor.equals(DataFlavor.stringFlavor)) {
			return true;
		}
		return false;
	}
}