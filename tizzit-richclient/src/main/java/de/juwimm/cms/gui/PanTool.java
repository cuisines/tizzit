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
package de.juwimm.cms.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.apache.log4j.Logger;

import de.juwimm.cms.Main;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.content.event.TreeSelectionEventData;
import de.juwimm.cms.deploy.panel.PanTaskDetails;
import de.juwimm.cms.gui.controls.LoadableViewComponentPanel;
import de.juwimm.cms.gui.controls.UnloadablePanel;
import de.juwimm.cms.gui.tree.PageNode;
import de.juwimm.cms.gui.tree.PageOtherUnitNode;
import de.juwimm.cms.gui.tree.TreeNode;
import de.juwimm.cms.gui.views.PanContentView;
import de.juwimm.cms.gui.views.PanExternallinkView;
import de.juwimm.cms.gui.views.PanInitView;
import de.juwimm.cms.gui.views.PanInternallinkView;
import de.juwimm.cms.gui.views.PanSeparatorView;
import de.juwimm.cms.gui.views.PanSymlinkView;
import de.juwimm.cms.util.ActionHub;
import de.juwimm.cms.vo.TaskValue;
import de.juwimm.cms.vo.ViewComponentValue;
import de.juwimm.swing.DropDownHolder;

/**
 * <p>Title: juwimm cms</p>
 * <p>Description: content management system</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: juwi macmillan group gmbh</p>
 * @author Dirk Bogun
 * @version $Id$
 */
public final class PanTool extends JPanel implements UnloadablePanel, ActionListener {
	private static Logger log = Logger.getLogger(PanTool.class);
	private static PanTree panTree;
	private JSplitPane splitPane = new JSplitPane();
	private JPanel rightPan = new JPanel(new BorderLayout());
	private PanTaskDetails panTaskDetails;
	private PanInitView panInit;
	private PanExternallinkView panExternalLink;
	private PanSeparatorView panSeparator;
	private PanInternallinkView panInternalLink;
	private PanSymlinkView panSymlink;
	private PanContentView panContent;
	private PanTaskTable panTask;
	private JPanel pnlLeft = new JPanel();

	private static PanTool instance = null;

	/**
	 * This is the method to call if you want to get an PanelCmsTool Instance.
	 *
	 * @return
	 */
	public static PanTool getInstance() {
		if (instance == null) {
			instance = new PanTool();
		}
		return instance;
	}

	private PanTool() {
		ActionHub.addActionListener(this);
		try {
			setDoubleBuffered(true);
			if (panTree == null) {
				panTree = new PanTree();
				ActionHub.addActionListener(panTree);
			}
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	void jbInit() throws Exception {
		this.setLayout(new BorderLayout());
		panInit = PanInitView.getInstance();
		panExternalLink = new PanExternallinkView();
		panInternalLink = new PanInternallinkView();
		panSeparator = new PanSeparatorView();
		panContent = PanContentView.getInstance();
		panSymlink = new PanSymlinkView();

		pnlLeft.setLayout(new BorderLayout());
		pnlLeft.add(panTree.getParametersPanel(), BorderLayout.NORTH);
		pnlLeft.add(panTree, BorderLayout.CENTER);

		splitPane.setBorder(null);
		splitPane.setDividerSize(3);
		splitPane.setDividerLocation(300);
		splitPane.setOneTouchExpandable(true);
		splitPane.setLeftComponent(pnlLeft);
		splitPane.setRightComponent(rightPan);
		this.add(splitPane, BorderLayout.CENTER);
	}

	public void reload(boolean withSelection, Object source) throws Exception {
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		try {
			panContent.reload();
			if (!withSelection) {
				panTree.reload();
			} else {
				panTree.reloadWithSelection((TreeSelectionEventData) source);
			}
			panTree.setCursor(Cursor.getDefaultCursor());
			panTree.getParametersPanel().setCursor(Cursor.getDefaultCursor());
			panContent.setCursor(Cursor.getDefaultCursor());
		} catch (Exception e) {
			this.setCursor(Cursor.getDefaultCursor());
			panTree.getParametersPanel().setCursor(Cursor.getDefaultCursor());
			panContent.setCursor(Cursor.getDefaultCursor());
			throw e;
		}
	}

	public void unload() {
		//instance = null;
	}

	public void setTreeToEmpty() {
		panTree.setTreeToEmpty();
	}

	private LoadableViewComponentPanel oldRight = null;

	private void updateRightComponent(Component right) {
		rightPan.removeAll();
		rightPan.add(right, BorderLayout.CENTER);
		if (right instanceof LoadableViewComponentPanel) {
			if (oldRight != null) {
				oldRight.unload();
			}
			oldRight = (LoadableViewComponentPanel) right;
		}
	}

	public void actionPerformed(ActionEvent e) {
		if ((e.getActionCommand().equals(Constants.ACTION_TREE_SELECT)) || (e.getActionCommand().equals(Constants.ACTION_TREE_SELECT_SAVE))) {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			TreeNode treeNode = (TreeNode) e.getSource();
			if (treeNode instanceof PageNode) {
				ViewComponentValue vc = ((PageNode) e.getSource()).getViewComponent();
				if (e.getActionCommand().equals(Constants.ACTION_TREE_SELECT_SAVE)) {
					try {
						log.debug("actionPerformed::ACTION_TREE_SELECT_SAVE");
						Main.getInstance().freezeInput(true);
						panContent.save();
					} catch (Exception ex) {
						log.error(ex.getMessage());
					}
				}
				if (treeNode instanceof PageOtherUnitNode) {
					updateRightComponent(panInit);
				} else if (vc.getViewType() == Constants.VIEW_TYPE_CONTENT || vc.getViewType() == Constants.VIEW_TYPE_UNIT) {
					updateRightComponent(panContent);
					panContent.load(vc);
				} else if (vc.getViewType() == Constants.VIEW_TYPE_EXTERNAL_LINK) {
					updateRightComponent(panExternalLink);
					panExternalLink.load(vc);
				} else if (vc.getViewType() == Constants.VIEW_TYPE_SEPARATOR) {
					updateRightComponent(panSeparator);
					panSeparator.load(vc);
				} else if (vc.getViewType() == Constants.VIEW_TYPE_INTERNAL_LINK) {
					updateRightComponent(panInternalLink);
					panInternalLink.load(vc);
				} else if (vc.getViewType() == Constants.VIEW_TYPE_SYMLINK) {
					updateRightComponent(panSymlink);
					panSymlink.load(vc);
				}
			} else {
				updateRightComponent(panInit);
			}
			setCursor(Cursor.getDefaultCursor());
		} else if (e.getActionCommand().equals(Constants.ACTION_TREE_DESELECT)) {
			updateRightComponent(panInit);
		} else if (e.getActionCommand().equals(Constants.ACTION_TASK_SELECT)) {
			updateRightComponent(panInit);
			if (panTaskDetails == null) {
				panTaskDetails = new PanTaskDetails();
			}
			TaskValue task = (TaskValue) e.getSource();
			if (task.getTaskType() == Constants.TASK_MESSAGE || task.getTaskType() == Constants.TASK_REJECTED) {
				panTaskDetails.load(task.getComment());
			} else {
				panTaskDetails.load(task);
			}
			updateRightComponent(panTaskDetails);
		} else if (e.getActionCommand().equals(Constants.ACTION_TASK_DESELECT)) {
			updateRightComponent(panInit);
		} else if (e.getActionCommand().equals(Constants.ACTION_TASK_DONE)) {
			updateRightComponent(panInit);
		} else if (e.getActionCommand().equals(Constants.ACTION_SHOW_TASK)) {
			try {
				pnlLeft.remove(panTree.getParametersPanel());
				pnlLeft.remove(panTree);
			} catch (Exception exe) {
			}
			if (panTask == null) {
				panTask = new PanTaskTable();
			}
			panTask.reload();
			pnlLeft.add(panTask, BorderLayout.CENTER);
			updateRightComponent(panInit);
		} else if (e.getActionCommand().equals(Constants.ACTION_SHOW_CONTENT) || e.getActionCommand().equals(Constants.ACTION_VIEW_EDITOR)) {
			try {
				pnlLeft.remove(panTask);
			} catch (Exception exe) {
			}
			pnlLeft.add(panTree.getParametersPanel(), BorderLayout.NORTH);
			pnlLeft.add(panTree, BorderLayout.CENTER);
		}
	}

	public void selectDefaultViewDocument() {
		panTree.selectDefaultViewDocument();
	}

	public DropDownHolder getSelectedViewDocument() {
		return ((DropDownHolder) PanTool.panTree.getSelectedViewDocument());
	}
}
