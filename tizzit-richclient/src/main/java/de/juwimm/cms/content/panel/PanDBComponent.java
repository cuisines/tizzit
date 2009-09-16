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

import static de.juwimm.cms.client.beans.Application.*;
import static de.juwimm.cms.common.Constants.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Node;

import de.juwimm.cms.Messages;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.components.vo.AddressValue;
import de.juwimm.cms.components.vo.DepartmentValue;
import de.juwimm.cms.components.vo.PersonValue;
import de.juwimm.cms.components.vo.TalktimeValue;
import de.juwimm.cms.content.event.SearchEvent;
import de.juwimm.cms.content.event.SearchListener;
import de.juwimm.cms.content.frame.DlgDBCPersonSearch;
import de.juwimm.cms.content.frame.DlgDBCUnitSearch;
import de.juwimm.cms.content.frame.DlgModalModule;
import de.juwimm.cms.content.frame.helper.IsolatedAggregationHelper;
import de.juwimm.cms.content.frame.tree.*;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.UnitValue;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanDBComponent extends JPanel implements SearchListener, ChangeListener, ActionListener {
	private static final String ACTION_SEARCH_POPUP = "searchpopup";
	private static final String ACTION_SEARCH_UNIT = "searchunit";
	private static final String ACTION_SEARCH_PERSON = "searchperson";
	private static final String ACTION_ADD_POPUP = "addpopup";
	private static final String ACTION_ADD_PERSON = "addperson"; 
	private static final String ACTION_ADD_TALKTIME = "addtalktime";
	private static final String ACTION_ADD_ADDRESS = "addaddress";

	private static Logger log = Logger.getLogger(PanDBComponent.class);

	private IsolatedAggregationHelper aggHelper = null;
	private Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private Thread treeClick = null;
	private JPanel panToolbar = new JPanel();
	//	private JToolBar tbGeneral = new JToolBar();
	private JButton cmdSearchPopup = new JButton();
	private JButton cmdSave = new JButton();
	private JToolBar tbActions = new JToolBar();
	private JButton cmdDelete = new JButton();
	private JLabel lblGeneral = new JLabel();
	private JLabel lblSpecificOptions = new JLabel();

	private boolean isTreeEditable = true;

	//	private PanDBCPerson panPerson = new PanDBCPerson();
	//	private PanDBCAddress panAddress = new PanDBCAddress();
	//	private PanDBCTalkTime panTalkTime = new PanDBCTalkTime();
	//	private PanDBCUnit panUnit = new PanDBCUnit();
	//	private PanDBCDepartment panDepartment = new PanDBCDepartment();
	private AbstractTreePanel currentComponentPanel;

	private DlgModalModule frm = null;

	/** all components with children */
	private ComponentsTreeModel modelAllTree;
	/** only selected components with selected children */
	private ComponentsTreeModel modelTree;
	private JTree mtree;

	private JSplitPane jSplitPane = new JSplitPane();
	private JPanel leftPanel = new JPanel();
	private JScrollPane scrollpanelMTree = new JScrollPane();
	private JComboBox comboBox = new JComboBox();
	private JPanel rightPanel = new JPanel();
	private JScrollPane scrollpanelComponent = new JScrollPane();
	private JPopupMenu popupSearch = new JPopupMenu();
	private JMenuItem cmdSearchUnit = new JMenuItem(rb.getString("content.modules.dbc.search4Unit"));
	private JMenuItem cmdSearchPerson = new JMenuItem(rb.getString("content.modules.dbc.search4Person"));
	private JButton cmdAddPopup = new JButton();
	private JPopupMenu popupAdd = new JPopupMenu(); 
	private JMenuItem cmdAddPerson = new JMenuItem(rb.getString("content.modules.dbc.addPerson"));
	private JMenuItem cmdAddTalktime = new JMenuItem(rb.getString("content.modules.dbc.addTalktime"));
	private JMenuItem cmdAddAddress = new JMenuItem(rb.getString("content.modules.dbc.addAddress"));

	public PanDBComponent() {
		try {
			jbInit();
			//SEARCH POPUP
			cmdSearchPopup.setIcon(UIConstants.DBC_SEARCH_POPUP);
			cmdSearchPopup.setActionCommand(ACTION_SEARCH_POPUP);
			cmdSearchPopup.addActionListener(this);
			cmdSearchUnit.setIcon(UIConstants.ICON_UNIT);
			cmdSearchUnit.setActionCommand(ACTION_SEARCH_UNIT);
			cmdSearchUnit.addActionListener(this);
			cmdSearchPerson.setIcon(UIConstants.ICON_PERSON);
			cmdSearchPerson.setActionCommand(ACTION_SEARCH_PERSON);
			cmdSearchPerson.addActionListener(this);
			popupSearch.add(cmdSearchUnit);
			popupSearch.add(cmdSearchPerson);
			//ADD POPUP
			cmdAddPopup.setIcon(UIConstants.DBC_ADD);
			cmdAddPopup.setActionCommand(ACTION_ADD_POPUP);
			cmdAddPopup.addActionListener(this);
			cmdAddPerson.setIcon(UIConstants.ICON_PERSON);
			cmdAddPerson.setActionCommand(ACTION_ADD_PERSON);
			cmdAddPerson.addActionListener(this); 
			cmdAddTalktime.setIcon(UIConstants.ICON_TALKTIME);
			cmdAddTalktime.setActionCommand(ACTION_ADD_TALKTIME);
			cmdAddTalktime.addActionListener(this);
			cmdAddAddress.setIcon(UIConstants.ICON_ADDRESS);
			cmdAddAddress.setActionCommand(ACTION_ADD_ADDRESS);
			cmdAddAddress.addActionListener(this);
			popupAdd.add(cmdAddPerson); 
			popupAdd.add(cmdAddTalktime);
			popupAdd.add(cmdAddAddress);

			comboBox.addItem(rb.getString("content.modules.dbc.showAllComponents"));
			comboBox.addItem(rb.getString("content.modules.dbc.showSelectedComponents"));
			comboBox.addItemListener(new MyItemListener());
			setButtonEnabled(false);
		} catch (Exception exe) {
			log.error("Initialization problem", exe);
		}
	}

	public void actionPerformed(ActionEvent e) {
		int screenHeight;
		int screenWidth;
		int frameWidth;
		int frameHeight;

		if (e.getActionCommand().equals(ACTION_SEARCH_POPUP)) {
			popupSearch.show(this, cmdSearchPopup.getX(), cmdSearchPopup.getY() + cmdSearchPopup.getHeight());
		} else if (e.getActionCommand().equals(ACTION_ADD_POPUP)) {
			popupAdd.show(this, cmdAddPopup.getX(), cmdAddPopup.getY() + cmdAddPopup.getHeight());
		} else if (e.getActionCommand().equals(ACTION_ADD_ADDRESS)) {
			ComponentNode node = (ComponentNode) mtree.getLastSelectedPathComponent();
			addAddressNode(node);
		} else if (e.getActionCommand().equals(ACTION_ADD_PERSON)) {
			ComponentNode node = (ComponentNode) mtree.getLastSelectedPathComponent();
			addPersonNode(node);
		} else if (e.getActionCommand().equals(ACTION_ADD_TALKTIME)) {
			ComponentNode node = (ComponentNode) mtree.getLastSelectedPathComponent();
			addTalktimeNode(node);
		} else if (e.getActionCommand().equals(ACTION_SEARCH_UNIT)) {
			DlgDBCUnitSearch frmu = new DlgDBCUnitSearch();
			screenHeight = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
			screenWidth = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
			frameWidth = 420;
			frameHeight = 412;
			frmu.setSize(frameWidth, frameHeight);
			frmu.setLocation((screenWidth / 2) - (frameWidth / 2), (screenHeight / 2) - (frameHeight / 2));
			frmu.setTitle(rb.getString("dialog.title"));
			frmu.addListener(this);
			frmu.setVisible(true);
		} else if (e.getActionCommand().equals(ACTION_SEARCH_PERSON)) {
			DlgDBCPersonSearch frmp = new DlgDBCPersonSearch();
			screenHeight = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
			screenWidth = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
			frameWidth = 480;
			frameHeight = 395;
			frmp.setSize(frameWidth, frameHeight);
			frmp.setLocation((screenWidth / 2) - (frameWidth / 2), (screenHeight / 2) - (frameHeight / 2));
			frmp.setTitle(rb.getString("dialog.title"));
			frmp.setSearchListener(this);
			frmp.setVisible(true);
		}
	}

	public void setFrmModalModule(DlgModalModule frm) {
		this.frm = frm;
	}

	private void jbInit() throws Exception {
		this.setLayout(new GridBagLayout());
		leftPanel.setLayout(new BorderLayout());
		scrollpanelMTree.getViewport().setBackground(Color.white);
		rightPanel.setLayout(new BorderLayout());
		scrollpanelComponent.setBorder(BorderFactory.createLoweredBevelBorder());
		jSplitPane.setDividerSize(3);

		comboBox.setPreferredSize(new Dimension(21, 21));
		panToolbar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		cmdSave.setIcon(UIConstants.MODULE_DATABASECOMPONENT_SAVE);
		cmdSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdSaveActionPerformed(e);
			}
		});
		cmdSave.setToolTipText(rb.getString("content.modules.dbc.saveTooltip"));
		cmdAddPopup.setToolTipText(rb.getString("content.modules.dbc.addTooltip"));
		cmdAddPopup.setIcon(UIConstants.DBC_ADD_POPUP);
		cmdAddPopup.setEnabled(true);

		cmdDelete.setToolTipText(rb.getString("content.modules.dbc.deleteTooltip"));
		cmdDelete.setIcon(UIConstants.MODULE_DATABASECOMPONENT_DELETE);
		cmdDelete.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdDeleteActionPerformed(e);
			}
		});

		lblGeneral.setAlignmentX((float) 0.0);
		lblGeneral.setText(rb.getString("content.modules.dbc.ToolbarGeneral") + " ");
		lblSpecificOptions.setText(rb.getString("content.modules.dbc.ToolbarActualComponent") + " ");
		leftPanel.add(scrollpanelMTree, BorderLayout.CENTER);
		leftPanel.add(comboBox, BorderLayout.NORTH);
		rightPanel.add(scrollpanelComponent, BorderLayout.CENTER);
		this.add(panToolbar, new GridBagConstraints(0, 0, 4, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jSplitPane.add(leftPanel, JSplitPane.LEFT);
		this.add(jSplitPane, new GridBagConstraints(1, 2, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 10, 10, 10), 253, 309));
		jSplitPane.add(rightPanel, JSplitPane.RIGHT);
		tbActions.setFloatable(false);
		panToolbar.add(tbActions, null);
		tbActions.add(lblSpecificOptions, null);
		tbActions.add(cmdAddPopup, null);
		tbActions.add(cmdDelete, null);
		tbActions.add(cmdSave, null);
		tbActions.add(Box.createRigidArea(new Dimension(10, 0)));
		tbActions.add(lblGeneral, null);
		tbActions.add(cmdSearchPopup, null);
		jSplitPane.setDividerLocation(183);
	}

	/**
	 * Will be called from within the Panels after changing the values typed in.<br>
	 * This is Implemented from the ChangeListener Class.
	 * @param ce
	 */
	public void stateChanged(ChangeEvent ce) {
		this.cmdSave.setEnabled(true);
		frm.setOkButtonEnabled(false);
		this.cmdSave.validate();
		this.cmdSave.repaint();
	}

	/**
	 * This will be called from the outpopping Search-Windows and Implements the SearchListener.
	 * @param e
	 */
	public void searchPerformed(SearchEvent e) {
		mtree = new JTree();
		TreeWillExpandListener tl = new TreeWillExpandListener() {
			public void treeWillCollapse(TreeExpansionEvent event) {
			}

			public void treeWillExpand(TreeExpansionEvent event) {
				try {
					TreePath path = event.getPath();
					ComponentNode node = (ComponentNode) path.getLastPathComponent();

					if (!node.isInit() && !node.isLeaf()) {
						treeClick(event.getPath());
					}
				} catch (Exception exe) {
					log.error("Error expanding the tree", exe);
				}
			}
		};

		MouseListener ml = new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				try {
					int selRow = mtree.getRowForLocation(me.getX(), me.getY());
					TreePath selPath = mtree.getPathForLocation(me.getX(), me.getY());

					if (me.getClickCount() == 2) {
						if (mtree.isExpanded(selPath)) {
							mtree.collapsePath(selPath);
						}
					} else if (selRow != -1) {
						treeClick(selPath);
					}
				} catch (Exception exe) {
					log.error("Error in mousePressed event", exe);
				}
			}
		};

		mtree.addTreeWillExpandListener(tl);
		mtree.addMouseListener(ml);

		Object obj = e.getSource();

		if (obj instanceof PersonValue) {
			modelAllTree = new ComponentsTreeModel(new PersonNode((PersonValue) obj, this.aggHelper));
		} else if (obj instanceof UnitValue) {
			modelAllTree = new ComponentsTreeModel(new UnitNode((UnitValue) obj, this.aggHelper));
		} else if (obj instanceof DepartmentValue) {
			modelAllTree = new ComponentsTreeModel(new DepartmentNode((DepartmentValue) obj, this.aggHelper));
		}

		ComponentNode root = (ComponentNode) modelAllTree.getRoot();
		root.setInit();
		root.setCheckSelected(true);

		mtree.setModel(modelAllTree);

		CheckRenderer renderer = new CheckRenderer();
		mtree.setCellRenderer(renderer);

		mtree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		mtree.addMouseListener(new NodeSelectionListener(mtree));

		scrollpanelMTree.getViewport().add(mtree, null);

		setButtonEnabled(false);
	}

	/**
	 * 
	 */
	private class NodeSelectionListener extends MouseAdapter {
		private JTree tree;

		NodeSelectionListener(JTree tree) {
			this.tree = tree;
		}

		public void mouseEntered(MouseEvent e) {
			tree.setToolTipText(new String());
		}

		public void mouseClicked(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			int row = tree.getRowForLocation(x, y);
			TreePath path = tree.getPathForRow(row);

			if (path != null) {
				CheckNode node = (CheckNode) path.getLastPathComponent();
				TreePanel currentPanel = (TreePanel) scrollpanelComponent.getViewport().getComponent(0);

				int startx = node.getLevel() * 20;
				int endx = startx + 15;
				if (x > startx && x < endx) {
					// Checkbox is clicked: new state: 
					boolean isCheckSelected = !(node.isCheckSelected());

					if (isTreeEditable) {
						ComponentNode cnode = (ComponentNode) mtree.getSelectionPath().getLastPathComponent();
						node.setCheckSelected(isCheckSelected);
						if (!node.isCheckSelected()) {
							// set everything to invisible
							currentPanel.setCheckHash(new Hashtable());
						} else {
							// Default visibility values for each component
							Hashtable<String, Integer> ht = new Hashtable<String, Integer>();

							if (cnode instanceof PersonNode) {
								ht.put("title", new Integer(1));
								ht.put("firstname", new Integer(1));
								ht.put("lastname", new Integer(1));
							}
							currentPanel.setCheckHash(ht);
						}
						if (mtree.getSelectionCount() > 0) frm.setOkButtonEnabled(true);
					}
				}
				currentPanel.setAllChecksEnabled(node.isCheckSelected());

				((ComponentsTreeModel) tree.getModel()).nodeChanged(node);
				// I need revalidate if node is root.  but why?
				if (row == 0) {
					tree.revalidate();
					tree.repaint();
				}
			}
		}
	}

	/**
	 * Makes sure there is always only one thread busy with refreshing the panel after choosing a node inside the tree panel.
	 * 
	 * @param treePath
	 * @throws Exception
	 */
	private void treeClick(TreePath treePath) throws Exception {
		if (treeClick != null) {
			treeClick.interrupt();
			treeClick = null;
		}
		treeClick = new Thread(new TreeClickRunnable(treePath, this), "TreeClickRunnable");
		treeClick.setPriority(Thread.NORM_PRIORITY);
		treeClick.start();
		treeClick.join();
		treeClick = null;
	}

	/**
	 * 
	 */
	private class TreeClickRunnable implements Runnable {
		private TreePath treePath = null;
		private PanDBComponent panDBComponent = null;

		public TreeClickRunnable(TreePath treePath, PanDBComponent myPan) {
			this.treePath = treePath;
			this.panDBComponent = myPan;
		}

		public void run() {
			ComponentNode nde = (ComponentNode) treePath.getLastPathComponent();
			if (nde.hasChildren() && !nde.isInit()) {
				nde.removeAllChildren();
				nde.loadNodes();
			}

			int selectedUnitId = 0;
			//			TreePanel treePanel = null;

			if (nde instanceof PersonNode) {
				if (scrollpanelComponent.getViewport().getComponentCount() > 0) {
					scrollpanelComponent.getViewport().remove(0);
				}
				PersonNode personNode = (PersonNode) nde;
				PanDBCPerson panPerson = new PanDBCPerson();

				personNode.setViewType(panDBComponent.aggHelper.lookupPersonViewType(String.valueOf(personNode.getPersonValue().getPersonId())));
				panPerson.load(personNode);
				panPerson.addChangeListener(panDBComponent);
				cmdSave.setEnabled(false);
				cmdSave.repaint();
				scrollpanelComponent.getViewport().add(panPerson);
				currentComponentPanel = panPerson;

				if (isTreeEditable) {
					cmdAddPopup.setEnabled(true);
					cmdAddAddress.setEnabled(true);
					cmdAddTalktime.setEnabled(true);
					cmdAddPerson.setEnabled(false); 
					cmdDelete.setEnabled(true);
				}
				try {
					selectedUnitId = (int) personNode.getPersonValue().getUnitId().intValue();
				} catch (Exception exe) {
					log.error("Unit ID not found for person ID " + personNode.getId());
				}
			} else if (nde instanceof AddressNode) {
				if (scrollpanelComponent.getViewport().getComponentCount() > 0) {
					scrollpanelComponent.getViewport().remove(0);
				}
				AddressNode addressNode = (AddressNode) nde;
				PanDBCAddress panAddress = new PanDBCAddress();
				panAddress.load(addressNode);
				panAddress.addChangeListener(panDBComponent);
				cmdSave.setEnabled(false);
				cmdSave.repaint();
				scrollpanelComponent.getViewport().add(panAddress);
				currentComponentPanel = panAddress;

				if (isTreeEditable) {
					cmdAddPopup.setEnabled(false);
					cmdAddAddress.setEnabled(false);
					cmdAddTalktime.setEnabled(false);
					cmdAddPerson.setEnabled(false); 
					cmdDelete.setEnabled(true);
				}
				if (addressNode.getParent() instanceof PersonNode) {
					selectedUnitId = (int) ((PersonNode) addressNode.getParent()).getPersonValue().getUnitId().intValue();
				} else if (addressNode.getParent() instanceof DepartmentNode) {
					selectedUnitId = ((DepartmentNode) addressNode.getParent()).getDepartmentValue().getUnitId();
				} else if (addressNode.getParent() instanceof UnitNode) {
					selectedUnitId = ((UnitNode) addressNode.getParent()).getUnitValue().getUnitId();
				}
			} else if (nde instanceof TalkTimeNode) {
				if (scrollpanelComponent.getViewport().getComponentCount() > 0) {
					scrollpanelComponent.getViewport().remove(0);
				}
				TalkTimeNode talktimeNode = (TalkTimeNode) nde;
				PanDBCTalkTime panTalkTime = new PanDBCTalkTime();
				panTalkTime.load(talktimeNode);
				panTalkTime.addChangeListener(panDBComponent);
				cmdSave.setEnabled(false);
				cmdSave.repaint();
				scrollpanelComponent.getViewport().add(panTalkTime);
				currentComponentPanel = panTalkTime;

				if (isTreeEditable) {
					cmdAddPopup.setEnabled(false);
					cmdAddAddress.setEnabled(false);
					cmdAddTalktime.setEnabled(false);
					cmdAddPerson.setEnabled(false); 
					cmdDelete.setEnabled(true);
				}
				if (talktimeNode.getParent() instanceof PersonNode) {
					selectedUnitId = (int) ((PersonNode) talktimeNode.getParent()).getPersonValue().getUnitId().intValue();
				} else if (talktimeNode.getParent() instanceof DepartmentNode) {
					selectedUnitId = ((DepartmentNode) talktimeNode.getParent()).getDepartmentValue().getUnitId();
				} else if (talktimeNode.getParent() instanceof UnitNode) {
					selectedUnitId = ((UnitNode) talktimeNode.getParent()).getUnitValue().getUnitId();
				}
			} else if (nde instanceof DepartmentNode) {
				if (scrollpanelComponent.getViewport().getComponentCount() > 0) {
					scrollpanelComponent.getViewport().remove(0);
				}
				DepartmentNode departmentNode = (DepartmentNode) nde;
				PanDBCDepartment panDepartment = new PanDBCDepartment();
				panDepartment.load(departmentNode);
				panDepartment.addChangeListener(panDBComponent);
				cmdSave.setEnabled(false);
				cmdSave.repaint();
				scrollpanelComponent.getViewport().add(panDepartment);
				currentComponentPanel = panDepartment;

				if (isTreeEditable) {
					cmdAddPopup.setEnabled(true);
					cmdAddAddress.setEnabled(true);
					cmdAddTalktime.setEnabled(true);
					cmdAddPerson.setEnabled(true); 
					cmdDelete.setEnabled(true);
				}
				selectedUnitId = ((DepartmentNode) nde).getDepartmentValue().getUnitId();
			} else if (nde instanceof UnitNode) {
				if (scrollpanelComponent.getViewport().getComponentCount() > 0) {
					scrollpanelComponent.getViewport().remove(0);
				}
				UnitNode unitNode = (UnitNode) nde;
				PanDBCUnit panUnit = new PanDBCUnit();
				panUnit.load(unitNode);
				panUnit.addChangeListener(panDBComponent);
				cmdSave.setEnabled(false);
				cmdSave.repaint();
				scrollpanelComponent.getViewport().add(panUnit);
				currentComponentPanel = panUnit;

				if (isTreeEditable) {
					cmdAddPopup.setEnabled(true);
					cmdAddAddress.setEnabled(true);
					cmdAddTalktime.setEnabled(true);
					cmdAddPerson.setEnabled(true); 
					cmdDelete.setEnabled(false);
				}
				selectedUnitId = unitNode.getUnitValue().getUnitId();
			} else if (!nde.isLeaf()) {
				TreePath path = new TreePath(modelAllTree.getPathToRoot(nde));

				mtree.expandPath(path);
				mtree.setSelectionPath(path);
			}
			if (selectedUnitId > 0 && !comm.isUserInUnit(selectedUnitId)) {
				// I am a not-priveleged User.... so I can't edit this person
				cmdAddPopup.setEnabled(false);
				cmdAddAddress.setEnabled(false);
				cmdAddTalktime.setEnabled(false);
				cmdAddPerson.setEnabled(false); 
				cmdDelete.setEnabled(false);
				currentComponentPanel.setFieldsEditable(false);
			} else {
				currentComponentPanel.setFieldsEditable(true);
			}
		}
	}

	/**
	 * 
	 */
	private class MyItemListener implements ItemListener {
		public MyItemListener() {
		}

		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				try {
					if (comboBox.getSelectedIndex() == 0) {
						mtree.setModel(modelAllTree);
						modelAllTree.expandPath4CheckedNodes(mtree);
						isTreeEditable = true;
					} else {
						modelTree = modelAllTree.getSelectedNodesModel();
						mtree.setModel(modelTree);
						modelTree.expandPath4CheckedNodes(mtree);
						isTreeEditable = false;
						setButtonEnabled(false);
					}
					scrollpanelComponent.getViewport().remove(0);
					scrollpanelComponent.validate();
					scrollpanelComponent.repaint();
				} catch (Exception ex) {
				}
			}
		}
	}

	private void setButtonEnabled(boolean bool) {
		cmdAddPopup.setEnabled(bool);
		cmdDelete.setEnabled(bool);
		cmdSave.setEnabled(bool);
	}

	/*
	private void addDepartmentNode(ComponentNode node) {
		DepartmentValue dao = new DepartmentValue();
		DepartmentNode departmentNode = new DepartmentNode(dao, this.aggHelper);
		node.add(departmentNode);
		try {
			dao.setDepartmentId(comm.createDepartment(SwingMessages.getString("PanDBComponent.newDepartmentPrefill"),
					(int) node.getId()));
			dao.setName(SwingMessages.getString("PanDBComponent.newDepartmentPrefill"));
		} catch (Exception ex) {
		}

		((ComponentsTreeModel) mtree.getModel()).nodeStructureChanged(node);
		TreePath path = new TreePath(departmentNode.getPath());

		try {
			treeClick(path);
			mtree.setSelectionPath(path);
		} catch (Exception ex) {
		}
		departmentNode.setInit();
	}
	*/

	private void addPersonNode(ComponentNode node) {
		PersonValue dao = new PersonValue();
		PersonNode personNode = new PersonNode(dao, this.aggHelper);
		personNode.setInit();
		node.add(personNode);

		dao.setBirthDay(null);
		dao.setSalutation(Messages.getString("PanDBComponent.saluationPrefill"));
		dao.setPosition(10);
		dao.setSalutation(Messages.getString("PanDBComponent.saluationPrefill"));
		if (node instanceof DepartmentNode) {
			dao.setDepartmentId(node.getId());
		} else {
			dao.setUnitId(node.getId());
		}
		try {
			dao.setPersonId(comm.createPerson(dao));
		} catch (Exception e) {
			log.error("error creating person", e);
		}
		
		((ComponentsTreeModel) mtree.getModel()).nodeStructureChanged(node);
		TreePath path = new TreePath(personNode.getPath());

		try {
			treeClick(path);
			mtree.setSelectionPath(path);
		} catch (Exception ex) {
		}
	}

	private void addAddressNode(ComponentNode node) {
		if (log.isDebugEnabled()) log.debug("CALLING addAddressNode:AddressData");
		AddressValue data = new AddressValue();
		AddressNode addressNode = new AddressNode(data, this.aggHelper);
		addressNode.setInit();
		node.add(addressNode);

		try {
			long id = comm.createAddress(data); 
			if (node instanceof PersonNode) {
				comm.addAddress2Person(node.getId(), id);
			} else if (node instanceof DepartmentNode) {
				comm.addAddress2Department(node.getId(), id);
			} else {
				comm.addAddress2Unit((int) node.getId(), id);
			}
			data.setAddressId(id);
		} catch (Exception ex) {
			log.error("Error creating new Address: " + ex.getMessage(), ex);
		}

		((ComponentsTreeModel) mtree.getModel()).nodeStructureChanged(node);
		TreePath path = new TreePath(addressNode.getPath());

		try {
			treeClick(path);
			mtree.setSelectionPath(path);
		} catch (Exception ex) {
		}
	}

	private void addTalktimeNode(ComponentNode node) {
		TalktimeValue data = new TalktimeValue();
		TalkTimeNode talktimeNode = new TalkTimeNode(data, this.aggHelper);
		talktimeNode.setInit();
		node.add(talktimeNode);

		try {
			if (node instanceof DepartmentNode) {
				data.setTalkTimeId(comm.addTalktime2Department(node.getId(), new String(), new String()));
			} else if (node instanceof PersonNode) {
				data.setTalkTimeId(comm.addTalktime2Person(node.getId(), new String(), new String()));
			} else if (node instanceof UnitNode) {
				data.setTalkTimeId(comm.addTalktime2Unit((int) node.getId(), new String(), new String()));
			}
		} catch (Exception exe) {
			log.error("Error adding talktime to " + node, exe);
		}

		((ComponentsTreeModel) mtree.getModel()).nodeStructureChanged(node);
		TreePath path = new TreePath(talktimeNode.getPath());

		try {
			treeClick(path);
			mtree.setSelectionPath(path);
		} catch (Exception ex) {
		}
	}

	void cmdDeleteActionPerformed(ActionEvent e) {
		ComponentNode node = (ComponentNode) mtree.getLastSelectedPathComponent();
		String message = "";
		message = Messages.getString("PanDBComponent.askDeleteComponent");

		if (JOptionPane.showConfirmDialog(this.getParent(), message, "CMS-Komponenten", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
			ComponentNode root = (ComponentNode) node.getParent();
			try {
				node.remove();
				if (modelAllTree.getRoot() == null) {
					modelAllTree = null;
				}
			} catch (Exception exe) {
				log.error("Error deleting the node " + root, exe);
			}

			if (root != null) {
				((ComponentsTreeModel) mtree.getModel()).nodeStructureChanged(root);
				setButtonEnabled(false);
			} else {
				scrollpanelMTree.getViewport().remove(mtree);
				scrollpanelMTree.validate();
				scrollpanelMTree.repaint();
				setButtonEnabled(false);
				cmdAddPopup.setEnabled(true);
			}
			scrollpanelComponent.getViewport().remove(0);
			scrollpanelComponent.validate();
			scrollpanelComponent.repaint();
		}
	}

	/**
	 * This Command saves the ACTUAL PANEL to the Database. <br>
	 * It catches the from the ACTUAL NODE the DAO Object and lets the <br>
	 * actual Panel save it with the content.<br>
	 * It does also save the Checkboxes for displaying the content inside the Webpage.
	 * @param e ActionEvent that does nothing here.
	 */
	private void cmdSaveActionPerformed(ActionEvent e) {
		try {
			Component comp = scrollpanelComponent.getViewport().getComponent(0);
			TreePath tp = this.mtree.getSelectionPath();
			boolean savingDone = false;
			if (tp != null) {
				ComponentNode node = (ComponentNode) tp.getLastPathComponent();
				if (comp instanceof PanDBCPerson) {
					PanDBCPerson pnlPerson = (PanDBCPerson) comp;
					if (pnlPerson.validateNode() == null) {
						pnlPerson.save();
						savingDone = true;
					} else {
						JOptionPane.showMessageDialog(this, pnlPerson.validateNode(), new String(), JOptionPane.ERROR_MESSAGE);
					}
				} else if (comp instanceof PanDBCAddress) {
					PanDBCAddress pnlAddress = (PanDBCAddress) comp;
					if (pnlAddress.validateNode() == null) {
						pnlAddress.save();
						savingDone = true;
					} else {
						JOptionPane.showMessageDialog(this, pnlAddress.validateNode(), new String(), JOptionPane.ERROR_MESSAGE);
					}
				} else if (comp instanceof PanDBCDepartment) {
					PanDBCDepartment pnlDepartment = (PanDBCDepartment) comp;
					if (pnlDepartment.validateNode() == null) {
						pnlDepartment.save();
						savingDone = true;
					} else {
						JOptionPane.showMessageDialog(this, pnlDepartment.validateNode(), new String(), JOptionPane.ERROR_MESSAGE);
					}
				} else if (comp instanceof PanDBCTalkTime) {
					PanDBCTalkTime pnlTalktime = (PanDBCTalkTime) comp;
					if (pnlTalktime.validateNode() == null) {
						pnlTalktime.save();
						savingDone = true;
					} else {
						JOptionPane.showMessageDialog(this, pnlTalktime.validateNode(), new String(), JOptionPane.ERROR_MESSAGE);
					}
				} else if (comp instanceof PanDBCUnit) {
					PanDBCUnit pnlUnit = (PanDBCUnit) comp;
					if (pnlUnit.validateNode() == null) {
						pnlUnit.save();
						savingDone = true;
					} else {
						JOptionPane.showMessageDialog(this, pnlUnit.validateNode(), new String(), JOptionPane.ERROR_MESSAGE);
					}
				}
				if (savingDone) {
					this.cmdSave.setEnabled(false);
					frm.setOkButtonEnabled(true);
					this.cmdSave.validate();
					this.cmdSave.repaint();
				}
				modelAllTree.nodeChanged(node);
			} else {
				JOptionPane.showMessageDialog(this, rb.getString("exception.NothingSelected"), rb.getString("dialog.title"), JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (Exception exe) {
			log.error("Save error", exe);
		}
	}

	public void setAggregationXml(Node ndeAgg) {
		this.aggHelper = new IsolatedAggregationHelper();
		if (log.isDebugEnabled()) log.debug("Aggregation XML to set:\n" + XercesHelper.node2string(ndeAgg));
		this.aggHelper.setAggregationXml(ndeAgg);
		if (ndeAgg != null) {
			try {
				Node nde = IsolatedAggregationHelper.findNode(ndeAgg, "./include");
				if (nde != null) {
					String type = nde.getAttributes().getNamedItem("type").getNodeValue();
					int id = new Integer(nde.getAttributes().getNamedItem("id").getNodeValue()).intValue();
					if (type.equals("unit")) {
						UnitValue udao = comm.getUnit(id);
						SearchEvent se = new SearchEvent(udao);
						this.searchPerformed(se);
					} else if (type.equals("person")) {
						PersonValue pdao = comm.getPerson(id);
						SearchEvent se = new SearchEvent(pdao);
						this.searchPerformed(se);
					} else if (type.equals("department")) {
						DepartmentValue ddao = comm.getDepartment(id);
						SearchEvent se = new SearchEvent(ddao);
						this.searchPerformed(se);
					}
					modelTree.expandPath4CheckedNodes(mtree);
				}
			} catch (Exception exe) {
			}
		}
	}

	public Node getAggregationXml() {
		try {
			//			ComponentsTreeModel selectedTreeModel = modelAllTree.getSelectedNodesModel();
			//			ComponentNode selectedRootNode = (ComponentNode) selectedTreeModel.getRoot();
			//			Node result = new IsolatedAggregationHelper().getAggregationXml(selectedRootNode);
			//			log.debug("\n" + XercesHelper.node2string(result));
			//			return result;
			return new IsolatedAggregationHelper().getAggregationXml((ComponentNode) modelAllTree.getSelectedNodesModel().getRoot());
		} catch (Exception exception) {
			log.error("Error building XML node for tree: " + exception);
			return null;
		}
	}
}