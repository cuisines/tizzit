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

import static de.juwimm.cms.client.beans.Application.*;
import static de.juwimm.cms.common.Constants.*;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.gui.controls.ToolbarButton;
import de.juwimm.cms.gui.event.ViewComponentEvent;
import de.juwimm.cms.gui.tree.PageNode;
import de.juwimm.cms.gui.tree.TreeNode;
import de.juwimm.cms.util.*;

/**
 * <p>Title: ConQuest</p>
 * <p>Description: Enterprise Content Management<</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanToolbar extends JToolBar implements ActionListener {
	private static Logger log = Logger.getLogger(PanToolbar.class);
	private Communication communication;

	private ToolbarButton cmdMoveViewUp = new ToolbarButton();
	private ToolbarButton cmdMoveViewDown = new ToolbarButton();
	private ToolbarButton cmdMoveViewLeft = new ToolbarButton();
	private ToolbarButton cmdMoveViewRight = new ToolbarButton();

	private ToolbarButton cmdContent = new ToolbarButton();
	private JPopupMenu cmdContentPopup = new JPopupMenu();
	private JMenuItem cmdAddContentBefore = new JMenuItem(rb.getString("actions.ACTION_TREE_NODE_BEFORE"));
	private JMenuItem cmdAddContentAfter = new JMenuItem(rb.getString("actions.ACTION_TREE_NODE_AFTER"));
	private JMenuItem cmdAppendContent = new JMenuItem(rb.getString("actions.ACTION_TREE_NODE_APPEND"));

	private ToolbarButton cmdJump = new ToolbarButton();
	private JPopupMenu cmdJumpPopup = new JPopupMenu();
	private JMenuItem cmdAddJumpBefore = new JMenuItem(rb.getString("actions.ACTION_TREE_JUMP_BEFORE"));
	private JMenuItem cmdAddJumpAfter = new JMenuItem(rb.getString("actions.ACTION_TREE_JUMP_AFTER"));
	private JMenuItem cmdAddJumpAdd = new JMenuItem(rb.getString("actions.ACTION_TREE_JUMP_ADD"));

	private ToolbarButton cmdSymlink = new ToolbarButton();
	private JPopupMenu cmdSymlinkPopup = new JPopupMenu();
	private JMenuItem cmdAddSymlinkBefore = new JMenuItem(rb.getString("actions.ACTION_TREE_SYMLINK_BEFORE"));
	private JMenuItem cmdAddSymlinkAfter = new JMenuItem(rb.getString("actions.ACTION_TREE_SYMLINK_AFTER"));
	private JMenuItem cmdAddSymlinkAdd = new JMenuItem(rb.getString("actions.ACTION_TREE_SYMLINK_ADD"));

	private ToolbarButton cmdLink = new ToolbarButton();
	private JPopupMenu cmdLinkPopup = new JPopupMenu();
	private JMenuItem cmdAddLinkBefore = new JMenuItem(rb.getString("actions.ACTION_TREE_LINK_BEFORE"));
	private JMenuItem cmdAddLinkAfter = new JMenuItem(rb.getString("actions.ACTION_TREE_LINK_AFTER"));
	private JMenuItem cmdAddLinkAdd = new JMenuItem(rb.getString("actions.ACTION_TREE_LINK_ADD"));

	private ToolbarButton cmdSeparator = new ToolbarButton();
	private JPopupMenu cmdSeparatorPopup = new JPopupMenu();
	private JMenuItem cmdAddSeparatorBefore = new JMenuItem(rb.getString("actions.ACTION_TREE_SEPARATOR_BEFORE"));
	private JMenuItem cmdAddSeparatorAfter = new JMenuItem(rb.getString("actions.ACTION_TREE_SEPARATOR_AFTER"));
	private JMenuItem cmdAddSeparatorAdd = new JMenuItem(rb.getString("actions.ACTION_TREE_SEPARATOR_ADD"));

	private ToolbarButton cmdDeleteNode = new ToolbarButton();
	private ToolbarButton cmdRefreshTree = new ToolbarButton();

	private ToolbarButton cmdDeploy = new ToolbarButton();
	private ToolbarButton cmdSend2Editor = new ToolbarButton();

	public PanToolbar(Communication comm) {
		communication = comm;
		try {
			this.setDoubleBuffered(true);
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	void jbInit() throws Exception {
		loadImages();
		this.setFloatable(false);
	}

	private void loadImages() {
		// move ViewCompont (CmsEntry)
		cmdMoveViewLeft.setIcon(UIConstants.MOVE_LEFT);
		cmdMoveViewLeft.setToolTipText(rb.getString("actions.MOVE_LEFT"));
		cmdMoveViewLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setMoveButtonsEnabled(false);
				ActionHub.fireViewComponentPerformed(new ViewComponentEvent(ViewComponentEvent.MOVE_LEFT));
			}
		});
		cmdMoveViewRight.setIcon(UIConstants.MOVE_RIGHT);
		cmdMoveViewRight.setToolTipText(rb.getString("actions.MOVE_RIGHT"));
		cmdMoveViewRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setMoveButtonsEnabled(false);
				ActionHub.fireViewComponentPerformed(new ViewComponentEvent(ViewComponentEvent.MOVE_RIGHT));
			}
		});
		cmdMoveViewUp.setIcon(UIConstants.MOVE_UP);
		cmdMoveViewUp.setToolTipText(rb.getString("actions.MOVE_UP"));
		cmdMoveViewUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setMoveButtonsEnabled(false);
				ActionHub.fireViewComponentPerformed(new ViewComponentEvent(ViewComponentEvent.MOVE_UP));
			}
		});

		cmdMoveViewDown.setIcon(UIConstants.MOVE_DOWN);
		cmdMoveViewDown.setToolTipText(rb.getString("actions.MOVE_DOWN"));
		cmdMoveViewDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setMoveButtonsEnabled(false);
				ActionHub.fireViewComponentPerformed(new ViewComponentEvent(ViewComponentEvent.MOVE_DOWN));
			}
		});

		// content stati
		cmdContent.setMaximumSize(new Dimension(38, 32));
		cmdContent.setPreferredSize(new Dimension(38, 32));
		cmdContent.setIcon(UIConstants.TOOLBAR_CONTENT);
		cmdContent.setToolTipText(rb.getString("actions.TOOLBAR_CONTENT"));
		cmdContent.setActionCommand("CONTENTSUBMENU");
		cmdContent.addActionListener(this);
		cmdContentPopup.add(cmdAddContentBefore, null);
		cmdContentPopup.add(cmdAddContentAfter, null);
		cmdContentPopup.add(cmdAppendContent, null);

		cmdAddContentBefore.setIcon(UIConstants.ACTION_TREE_NODE_BEFORE);
		cmdAddContentBefore.setActionCommand(Constants.ACTION_TREE_NODE_BEFORE);
		cmdAddContentBefore.addActionListener(communication);
		cmdAddContentAfter.setIcon(UIConstants.ACTION_TREE_NODE_AFTER);
		cmdAddContentAfter.setActionCommand(Constants.ACTION_TREE_NODE_AFTER);
		cmdAddContentAfter.addActionListener(communication);
		cmdAppendContent.setIcon(UIConstants.ACTION_TREE_NODE_APPEND);
		cmdAppendContent.setActionCommand(Constants.ACTION_TREE_NODE_APPEND);
		cmdAppendContent.addActionListener(communication);

		// Link stati
		cmdLink.setMaximumSize(new Dimension(38, 32));
		cmdLink.setPreferredSize(new Dimension(38, 32));
		cmdLink.setIcon(UIConstants.TOOLBAR_LINK);
		cmdLink.setToolTipText(rb.getString("actions.TOOLBAR_LINK"));
		cmdLink.setActionCommand("LINKSUBMENU");
		cmdLink.addActionListener(this);
		cmdLinkPopup.add(cmdAddLinkBefore, null);
		cmdLinkPopup.add(cmdAddLinkAfter, null);
		cmdLinkPopup.add(cmdAddLinkAdd, null);

		cmdAddLinkBefore.setIcon(UIConstants.ACTION_TREE_LINK_BEFORE);
		cmdAddLinkBefore.setActionCommand(Constants.ACTION_TREE_LINK_BEFORE);
		cmdAddLinkBefore.addActionListener(communication);
		cmdAddLinkAfter.setIcon(UIConstants.ACTION_TREE_LINK_AFTER);
		cmdAddLinkAfter.setActionCommand(Constants.ACTION_TREE_LINK_AFTER);
		cmdAddLinkAfter.addActionListener(communication);
		cmdAddLinkAdd.setIcon(UIConstants.ACTION_TREE_LINK_ADD);
		cmdAddLinkAdd.setActionCommand(Constants.ACTION_TREE_LINK_ADD);
		cmdAddLinkAdd.addActionListener(communication);

		// Separator stati
		cmdSeparator.setMaximumSize(new Dimension(38, 32));
		cmdSeparator.setPreferredSize(new Dimension(38, 32));
		cmdSeparator.setIcon(UIConstants.TOOLBAR_SEPARATOR);
		cmdSeparator.setToolTipText(rb.getString("actions.TOOLBAR_SEPARATOR"));
		cmdSeparator.setActionCommand("SEPARATORSUBMENU");
		cmdSeparator.addActionListener(this);
		cmdSeparatorPopup.add(cmdAddSeparatorBefore, null);
		cmdSeparatorPopup.add(cmdAddSeparatorAfter, null);
		cmdSeparatorPopup.add(cmdAddSeparatorAdd, null);

		cmdAddSeparatorBefore.setIcon(UIConstants.ACTION_TREE_SEPARATOR_BEFORE);
		cmdAddSeparatorBefore.setActionCommand(Constants.ACTION_TREE_SEPARATOR_BEFORE);
		cmdAddSeparatorBefore.addActionListener(communication);
		cmdAddSeparatorAfter.setIcon(UIConstants.ACTION_TREE_SEPARATOR_AFTER);
		cmdAddSeparatorAfter.setActionCommand(Constants.ACTION_TREE_SEPARATOR_AFTER);
		cmdAddSeparatorAfter.addActionListener(communication);
		cmdAddSeparatorAdd.setIcon(UIConstants.ACTION_TREE_SEPARATOR_ADD);
		cmdAddSeparatorAdd.setActionCommand(Constants.ACTION_TREE_SEPARATOR_ADD);
		cmdAddSeparatorAdd.addActionListener(communication);

		// jump stati
		cmdJump.setMaximumSize(new Dimension(38, 32));
		cmdJump.setPreferredSize(new Dimension(38, 32));
		cmdJump.setIcon(UIConstants.TOOLBAR_JUMP);
		cmdJump.setToolTipText(rb.getString("actions.TOOLBAR_JUMP"));
		cmdJump.setActionCommand("JUMPSUBMENU");
		cmdJump.addActionListener(this);
		cmdJumpPopup.add(cmdAddJumpBefore, null);
		cmdJumpPopup.add(cmdAddJumpAfter, null);
		cmdJumpPopup.add(cmdAddJumpAdd, null);

		cmdAddJumpBefore.setIcon(UIConstants.ACTION_TREE_JUMP_BEFORE);
		cmdAddJumpBefore.setActionCommand(Constants.ACTION_TREE_JUMP_BEFORE);
		cmdAddJumpBefore.addActionListener(communication);
		cmdAddJumpAfter.setIcon(UIConstants.ACTION_TREE_JUMP_AFTER);
		cmdAddJumpAfter.setActionCommand(Constants.ACTION_TREE_JUMP_AFTER);
		cmdAddJumpAfter.addActionListener(communication);
		cmdAddJumpAdd.setIcon(UIConstants.ACTION_TREE_JUMP_ADD);
		cmdAddJumpAdd.setActionCommand(Constants.ACTION_TREE_JUMP_ADD);
		cmdAddJumpAdd.addActionListener(communication);

		// Symlink stati
		cmdSymlink.setMaximumSize(new Dimension(38, 32));
		cmdSymlink.setPreferredSize(new Dimension(38, 32));
		cmdSymlink.setIcon(UIConstants.TOOLBAR_SYMLINK);
		cmdSymlink.setToolTipText(rb.getString("actions.TOOLBAR_SYMLINK"));
		cmdSymlink.setActionCommand("SYMLINKSUBMENU");
		cmdSymlink.addActionListener(this);
		cmdSymlinkPopup.add(cmdAddSymlinkBefore, null);
		cmdSymlinkPopup.add(cmdAddSymlinkAfter, null);
		cmdSymlinkPopup.add(cmdAddSymlinkAdd, null);

		cmdAddSymlinkBefore.setIcon(UIConstants.ACTION_TREE_SYMLINK_BEFORE);
		cmdAddSymlinkBefore.setActionCommand(Constants.ACTION_TREE_SYMLINK_BEFORE);
		cmdAddSymlinkBefore.addActionListener(communication);
		cmdAddSymlinkAfter.setIcon(UIConstants.ACTION_TREE_SYMLINK_AFTER);
		cmdAddSymlinkAfter.setActionCommand(Constants.ACTION_TREE_SYMLINK_AFTER);
		cmdAddSymlinkAfter.addActionListener(communication);
		cmdAddSymlinkAdd.setIcon(UIConstants.ACTION_TREE_SYMLINK_ADD);
		cmdAddSymlinkAdd.setActionCommand(Constants.ACTION_TREE_SYMLINK_ADD);
		cmdAddSymlinkAdd.addActionListener(communication);

		// delete node
		cmdDeleteNode.setIcon(UIConstants.ACTION_TREE_NODE_DELETE);
		cmdDeleteNode.setActionCommand(Constants.ACTION_TREE_NODE_DELETE);
		cmdDeleteNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionHub.fireViewComponentPerformed(new ViewComponentEvent(ViewComponentEvent.DELETE));
			}
		});
		cmdDeleteNode.setToolTipText(rb.getString("actions.ACTION_TREE_NODE_DELETE"));

		// refresh tree
		cmdRefreshTree.setIcon(UIConstants.ACTION_TREE_REFRESH);
		cmdRefreshTree.setActionCommand(Constants.ACTION_TREE_REFRESH);
		cmdRefreshTree.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionHub.fireActionPerformed(new ActionEvent(cmdRefreshTree, ActionEvent.ACTION_PERFORMED, Constants.ACTION_TREE_REFRESH));
			}
		});
		cmdRefreshTree.setToolTipText(rb.getString("actions.ACTION_TREE_REFRESH"));

		/*
		 cmdCut.setIcon(UIConstants.ACTION_CUT);
		 cmdCut.setToolTipText(rb.getString("actions.ACTION_CUT"));
		 cmdCopy.setIcon(UIConstants.ACTION_COPY);
		 cmdCopy.setToolTipText(rb.getString("actions.ACTION_COPY"));
		 cmdPaste.setIcon(UIConstants.ACTION_PASTE);
		 cmdPaste.setToolTipText(rb.getString("actions.ACTION_COPY"));
		 */

		cmdDeploy.setIcon(UIConstants.ACTION_DEPLOY);
		cmdDeploy.setToolTipText(rb.getString("actions.ACTION_DEPLOY"));
		cmdDeploy.setActionCommand(Constants.ACTION_DEPLOY);
		cmdDeploy.addActionListener(communication);

		cmdSend2Editor.setIcon(UIConstants.ACTION_DEPLOY);
		cmdSend2Editor.setToolTipText(rb.getString("actions.ACTION_SEND2EDITOR"));
		cmdSend2Editor.setActionCommand(Constants.ACTION_SEND2EDITOR);
		cmdSend2Editor.addActionListener(communication);
	}

	public void setView(boolean showContent) {
		this.removeAll();
		if (showContent) {
			showEditor();
		}
		this.validate();
		this.repaint();
	}

	private void showEditor() {
		cmdDeploy.setEnabled(true);
		cmdAddContentBefore.setEnabled(false);
		cmdAddContentAfter.setEnabled(false);
		cmdAppendContent.setEnabled(false);
		cmdAddLinkBefore.setEnabled(false);
		cmdAddLinkAfter.setEnabled(false);
		cmdAddLinkAdd.setEnabled(false);
		cmdAddSeparatorBefore.setEnabled(false);
		cmdAddSeparatorAfter.setEnabled(false);
		cmdAddSeparatorAdd.setEnabled(false);
		cmdAddJumpBefore.setEnabled(false);
		cmdAddJumpAfter.setEnabled(false);
		cmdAddJumpAdd.setEnabled(false);
		cmdAddSymlinkBefore.setEnabled(false);
		cmdAddSymlinkAfter.setEnabled(false);
		cmdAddSymlinkAdd.setEnabled(false);

		this.add(cmdMoveViewLeft, null);
		cmdMoveViewLeft.setEnabled(false);
		this.add(cmdMoveViewUp, null);
		cmdMoveViewUp.setEnabled(false);
		this.add(cmdMoveViewDown, null);
		cmdMoveViewDown.setEnabled(false);
		this.add(cmdMoveViewRight, null);
		cmdMoveViewRight.setEnabled(false);

		this.addSeparator(new Dimension(20, 0));
		this.add(cmdDeleteNode, null);
		this.add(cmdRefreshTree, null);
		cmdDeleteNode.setEnabled(false);

		this.addSeparator(new Dimension(20, 0));
		this.add(cmdContent);
		if (communication.isUserInRole(UserRights.SYMLINK)) {
			this.add(cmdSymlink);
		}
		this.add(cmdJump);
		this.add(cmdLink);
		if (communication.isUserInRole(UserRights.SEPARATOR)) {
			this.add(cmdSeparator);
		}

		this.addSeparator(new Dimension(20, 0));
		this.add(cmdDeploy, null);

		this.addSeparator(new Dimension(20, 0));
	}

	private HashMap<String, ToolbarButton> htPropButtons = new HashMap<String, ToolbarButton>();

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(Constants.PROPERTY_PROPAGATION) || e.getActionCommand().equals(Constants.PROPERTY_DEPROPAGATION) || e.getActionCommand().equals(Constants.PROPERTY_CONFIGURATION)) {
			// this action will be send if one of the components want to configure other
			// application parts
			if (e instanceof PropertyActionEvent) {
				PropertyActionEvent pae = (PropertyActionEvent) e;
				if (e.getActionCommand().equals(Constants.PROPERTY_PROPAGATION)) {
					ToolbarButton tb = null;
					if (htPropButtons.containsKey(pae.getUniqueId())) {
						tb = (ToolbarButton) htPropButtons.get(pae.getUniqueId());
					} else {
						tb = new ToolbarButton();
						tb.setToolTipText(pae.getDescDetail());
						tb.setActionCommand(pae.getAction());
						tb.addActionListener(((Communication) getBean(Beans.COMMUNICATION)));
						try {
							tb.setIcon(UIConstants.getPropImage(pae.getUniqueId(), UIConstants.IMAGE_SIZE_20));
						} catch (Exception exe) {
						}
						htPropButtons.put(pae.getUniqueId(), tb);
					}
					this.add(tb);
				} else if (e.getActionCommand().equals(Constants.PROPERTY_DEPROPAGATION)) {
					// DEPROPAGATION
					if (htPropButtons.containsKey(pae.getUniqueId())) {
						this.remove((Component) htPropButtons.get(pae.getUniqueId()));
					}
				}
			} else if (e.getActionCommand().equals(Constants.PROPERTY_CONFIGURATION) && e instanceof PropertyConfigurationEvent && htPropButtons.containsKey(((PropertyConfigurationEvent) e).getUniqueId())) {
				// CONFIGURATION
				PropertyConfigurationEvent pce = (PropertyConfigurationEvent) e;
				if (pce.getKey().equalsIgnoreCase(PropertyConfigurationEvent.PROP_ENABLE)) {
					ToolbarButton tb = (ToolbarButton) htPropButtons.get(pce.getUniqueId());
					tb.setEnabled(Boolean.valueOf(pce.getValue()).booleanValue());
				}
			}
		} else if (e.getActionCommand().equals("CONTENTSUBMENU")) {
			cmdContentPopup.show(this, cmdContent.getX(), cmdContent.getY() + cmdContent.getHeight());
		} else if (e.getActionCommand().equals("LINKSUBMENU")) {
			cmdLinkPopup.show(this, cmdLink.getX(), cmdLink.getY() + cmdContent.getHeight());
		} else if (e.getActionCommand().equals("SEPARATORSUBMENU")) {
			cmdSeparatorPopup.show(this, cmdSeparator.getX(), cmdSeparator.getY() + cmdContent.getHeight());
		} else if (e.getActionCommand().equals("JUMPSUBMENU")) {
			cmdJumpPopup.show(this, cmdJump.getX(), cmdJump.getY() + cmdContent.getHeight());
		} else if (e.getActionCommand().equals("SYMLINKSUBMENU")) {
			cmdSymlinkPopup.show(this, cmdSymlink.getX(), cmdSymlink.getY() + cmdSymlink.getHeight());
			//} else if(e.getActionCommand().equals(Constants.ACTION_CONTENT_FINISHED_LOADING)) {
			// nothing to do
		} else if (e.getActionCommand().equals(Constants.ACTION_SHOW_CONTENT)) {
			setView(true);
		} else if (e.getActionCommand().equals(Constants.ACTION_SHOW_TASK)) {
			this.removeAll();
		} else if (e.getActionCommand().equals(Constants.ACTION_TREE_DESELECT)) {
			cmdDeleteNode.setEnabled(false);
			cmdAddContentBefore.setEnabled(false);
			cmdAddContentAfter.setEnabled(false);
			cmdAppendContent.setEnabled(false);

			cmdAddLinkBefore.setEnabled(false);
			cmdAddLinkAfter.setEnabled(false);
			cmdAddLinkAdd.setEnabled(false);

			cmdAddSeparatorBefore.setEnabled(false);
			cmdAddSeparatorAfter.setEnabled(false);
			cmdAddSeparatorAdd.setEnabled(false);

			cmdAddJumpBefore.setEnabled(false);
			cmdAddJumpAfter.setEnabled(false);
			cmdAddJumpAdd.setEnabled(false);

			cmdAddSymlinkBefore.setEnabled(false);
			cmdAddSymlinkAfter.setEnabled(false);
			cmdAddSymlinkAdd.setEnabled(false);
		} else if (e.getActionCommand().equals(Constants.ACTION_TREE_SELECT)) {
			TreeNode entry = (TreeNode) e.getSource();
			// NEW --------------------------------------------------------
			cmdMoveViewLeft.setEnabled(entry.isMoveableToLeft());
			cmdMoveViewUp.setEnabled(entry.isMoveableToUp());
			cmdMoveViewDown.setEnabled(entry.isMoveableToDown());
			cmdMoveViewRight.setEnabled(entry.isMoveableToRight());

			cmdDeleteNode.setEnabled(entry.isDeleteable());

			boolean append = entry.isAppendingAllowed();
			cmdAppendContent.setEnabled(append);
			cmdAddLinkAdd.setEnabled(append);
			cmdAddSeparatorAdd.setEnabled(append);
			cmdAddJumpAdd.setEnabled(append);
			cmdAddSymlinkAdd.setEnabled(append);

			cmdAddContentBefore.setEnabled(false);
			cmdAddContentAfter.setEnabled(false);
			cmdAddLinkBefore.setEnabled(false);
			cmdAddLinkAfter.setEnabled(false);
			cmdAddSeparatorBefore.setEnabled(false);
			cmdAddSeparatorAfter.setEnabled(false);

			cmdAddJumpBefore.setEnabled(false);
			cmdAddJumpAfter.setEnabled(false);
			cmdAddSymlinkBefore.setEnabled(false);
			cmdAddSymlinkAfter.setEnabled(false);

			if (!entry.isRoot() && entry instanceof PageNode) {
				cmdAddContentBefore.setEnabled(true);
				cmdAddContentAfter.setEnabled(true);
				cmdAddLinkBefore.setEnabled(true);
				cmdAddLinkAfter.setEnabled(true);
				cmdAddSeparatorBefore.setEnabled(true);
				cmdAddSeparatorAfter.setEnabled(true);
				cmdAddJumpBefore.setEnabled(true);
				cmdAddJumpAfter.setEnabled(true);
				cmdAddSymlinkBefore.setEnabled(true);
				cmdAddSymlinkAfter.setEnabled(true);
			}
			// NEW END ----------------------------------------------------
		}
	}

	private void setMoveButtonsEnabled(boolean enabled) {
		cmdMoveViewLeft.setEnabled(enabled);
		cmdMoveViewRight.setEnabled(enabled);
		cmdMoveViewUp.setEnabled(enabled);
		cmdMoveViewDown.setEnabled(enabled);
	}

}
