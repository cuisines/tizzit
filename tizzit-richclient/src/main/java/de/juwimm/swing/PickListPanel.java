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
package de.juwimm.swing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * This panel consists of two lists between witch you can switch items.
 * The data gets into this panel using a PickListData-object.
 * 
 * @see de.juwimm.swing.PickListData()
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class PickListPanel extends JPanel implements ActionListener {
	private PickListData data = null;
	private boolean isEnabled = false;
	private boolean manuallySortable = false;
	
    private static final String ACTION_MOVE_ALL_RIGHT = "ACTION_MOVE_ALL_RIGHT";
    private static final String ACTION_MOVE_RIGHT = "ACTION_MOVE_RIGHT";
    private static final String ACTION_MOVE_ALL_LEFT = "ACTION_MOVE_ALL_LEFT";
    private static final String ACTION_MOVE_LEFT = "ACTION_MOVE_LEFT";
    private static final String ACTION_MOVE_UP = "ACTION_MOVE_UP";
    private static final String ACTION_MOVE_DOWN = "ACTION_MOVE_DOWN";

	private JPanel panExchangeButtons = null;
	private JButton btnMoveLeft = null;
	private JButton btnMoveAllLeft = null;
	private JButton btnMoveRight = null;
	private JButton btnMoveAllRight = null;
	private JButton btnUp = null;
	private JButton btnDown = null;
	private JLabel lblLeftArea = null;
	private JLabel lblRightArea = null;
	private JList lstLeft = null;
	private JList lstRight = null;
	private JScrollPane spLeft = null;
	private JScrollPane spRight = null;

	private PickListPanel() {
		super();
		initialize();
	}
	
	public PickListPanel(PickListData pickListData) {
		this(pickListData, false);
	}
	
	public PickListPanel(PickListData pickListData, boolean manuallySortable) {
		super();
		this.data = pickListData;
		this.manuallySortable = manuallySortable;
		initialize();
	}
	
	private void initialize() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints panConstraints = new GridBagConstraints();
		GridBagConstraints lbAssignedConstraints = new GridBagConstraints();
		GridBagConstraints lbAvailableConstraints = new GridBagConstraints();
		GridBagConstraints lstAvailConstraints = new GridBagConstraints();
		GridBagConstraints lstAssignConstraints = new GridBagConstraints();
		GridBagConstraints panExchangeConstraints = new GridBagConstraints();
		lblLeftArea = new JLabel();
		lblRightArea = new JLabel();
		panConstraints.weightx = 1.0;
		panConstraints.weighty = 1.0;
		panConstraints.fill = java.awt.GridBagConstraints.BOTH;
		lbAssignedConstraints.gridx = 0;
		lbAssignedConstraints.gridy = 0;
		lblLeftArea.setText(this.data.getLeftLabel());
		lbAvailableConstraints.gridx = 2;
		lbAvailableConstraints.gridy = 0;
		lblRightArea.setText(this.data.getRightLabel());
		lbAssignedConstraints.anchor = java.awt.GridBagConstraints.CENTER;
		lbAssignedConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
		lbAvailableConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
		lstAvailConstraints.gridx = 2;
		lstAvailConstraints.gridy = 1;
		lstAvailConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		lstAvailConstraints.fill = java.awt.GridBagConstraints.BOTH;
		lstAvailConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		lstAvailConstraints.weightx = 1.0D;
		lstAvailConstraints.weighty = 1.0D;
		lstAssignConstraints.gridx = 0;
		lstAssignConstraints.gridy = 1;
		lstAssignConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		lstAssignConstraints.fill = java.awt.GridBagConstraints.BOTH;
		lstAssignConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
		lstAssignConstraints.weightx = 1.0D;
		lstAssignConstraints.weighty = 1.0D;
		panExchangeConstraints.gridx = 1;
		panExchangeConstraints.gridy = 1;
		panExchangeConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		panExchangeConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
		panExchangeConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
		panExchangeConstraints.weightx = 0.0D;
		panExchangeConstraints.weighty = 1.0D;
		this.setMinimumSize(new java.awt.Dimension(400, 200));
		this.setPreferredSize(new java.awt.Dimension(400, 200));
		this.setSize(490, 200);
		this.add(lblLeftArea, lbAssignedConstraints);
		this.add(getSpLeft(), lstAssignConstraints);
		this.add(getPanExchangeButtons(), panExchangeConstraints);
		this.add(lblRightArea, lbAvailableConstraints);
		this.add(getSpRight(), lstAvailConstraints);
	}
	
	private JPanel getPanExchangeButtons() {
		if (panExchangeButtons == null) {
			java.awt.GridBagConstraints btnRemoveAllConstraints = new GridBagConstraints();
			java.awt.GridBagConstraints btnRemoveConstraints = new GridBagConstraints();
			java.awt.GridBagConstraints btnAssociateAllConstraints = new GridBagConstraints();
			java.awt.GridBagConstraints btnAssociateConstraints = new GridBagConstraints();

			panExchangeButtons = new JPanel();
			panExchangeButtons.setLayout(new GridBagLayout());
			btnAssociateConstraints.gridx = 0;
			btnAssociateConstraints.gridy = 0;
			btnAssociateConstraints.insets = new Insets(5, 5, 0, 5);
			btnAssociateConstraints.anchor = GridBagConstraints.NORTHWEST;
			btnAssociateConstraints.fill = GridBagConstraints.HORIZONTAL;
			btnAssociateConstraints.weighty = 0.0D;
			btnAssociateAllConstraints.gridx = 0;
			btnAssociateAllConstraints.gridy = 2;
			btnAssociateAllConstraints.anchor = GridBagConstraints.NORTHWEST;
			btnAssociateAllConstraints.insets = new Insets(5, 5, 0, 5);
			btnAssociateAllConstraints.weighty = 0.0D;
			btnRemoveConstraints.gridx = 0;
			btnRemoveConstraints.gridy = 1;
			btnRemoveConstraints.anchor = GridBagConstraints.NORTHWEST;
			btnRemoveConstraints.insets = new Insets(5, 5, 0, 5);
			btnRemoveConstraints.fill = GridBagConstraints.HORIZONTAL;
			btnRemoveAllConstraints.gridx = 0;
			btnRemoveAllConstraints.gridy = 3;
			btnRemoveAllConstraints.anchor = GridBagConstraints.NORTHWEST;
			btnRemoveAllConstraints.fill = GridBagConstraints.HORIZONTAL;
			btnRemoveAllConstraints.insets = new Insets(5, 5, 0, 5);
			panExchangeButtons.add(getBtnMoveLeft(), btnAssociateConstraints);
			panExchangeButtons.add(getBtnMoveAllLeft(), btnAssociateAllConstraints);
			panExchangeButtons.add(getBtnMoveRight(), btnRemoveConstraints);
			panExchangeButtons.add(getBtnMoveAllRight(), btnRemoveAllConstraints);
			if (this.manuallySortable) {
				JPanel sortingPanel = new JPanel();
				sortingPanel.setOpaque(false);
				sortingPanel.setLayout(new GridBagLayout());
				GridBagConstraints sortingPanelConstraints = new GridBagConstraints();
				sortingPanelConstraints.gridx = 0;
				sortingPanelConstraints.gridy = 4;
				sortingPanelConstraints.anchor = GridBagConstraints.NORTHWEST;
				sortingPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
				sortingPanelConstraints.insets = new Insets(5, 5, 0, 5);
				
				GridBagConstraints btnUpConstraints = new GridBagConstraints();
				GridBagConstraints btnDownConstraints = new GridBagConstraints();
				btnUpConstraints.gridx = 0;
				btnUpConstraints.gridy = 0;
				btnUpConstraints.anchor = GridBagConstraints.CENTER;
				btnUpConstraints.fill = GridBagConstraints.NONE;
				btnUpConstraints.insets = new Insets(0, 0, 0, 5);
				btnDownConstraints.gridx = 1;
				btnDownConstraints.gridy = 0;
				btnDownConstraints.anchor = GridBagConstraints.CENTER;
				btnDownConstraints.fill = GridBagConstraints.NONE;
				sortingPanel.add(getBtnUp(), btnUpConstraints);
				sortingPanel.add(getBtnDown(), btnDownConstraints);
				panExchangeButtons.add(sortingPanel, sortingPanelConstraints);
			}
		}
		return panExchangeButtons;
	}
	
	private JButton getBtnMoveLeft() {
		if (btnMoveLeft == null) {
			btnMoveLeft = new JButton();
			btnMoveLeft.setText("<");
			btnMoveLeft.setEnabled(false);
			btnMoveLeft.addActionListener(this);
			btnMoveLeft.setActionCommand(PickListPanel.ACTION_MOVE_LEFT);
		}
		return btnMoveLeft;
	}
	
	private JButton getBtnMoveAllLeft() {
		if (btnMoveAllLeft == null) {
			btnMoveAllLeft = new JButton();
			btnMoveAllLeft.setText("<<");
			btnMoveAllLeft.setEnabled(false);
			btnMoveAllLeft.addActionListener(this);
			btnMoveAllLeft.setActionCommand(PickListPanel.ACTION_MOVE_ALL_LEFT);
		}
		return btnMoveAllLeft;
	}
	
	private JButton getBtnMoveRight() {
		if (btnMoveRight == null) {
			btnMoveRight = new JButton();
			btnMoveRight.setText(">");
			btnMoveRight.setEnabled(false);
			btnMoveRight.addActionListener(this);
			btnMoveRight.setActionCommand(PickListPanel.ACTION_MOVE_RIGHT);
		}
		return btnMoveRight;
	}
	private JButton getBtnMoveAllRight() {
		if (btnMoveAllRight == null) {
			btnMoveAllRight = new JButton();
			btnMoveAllRight.setText(">>");
			btnMoveAllRight.setEnabled(false);
			btnMoveAllRight.addActionListener(this);
			btnMoveAllRight.setActionCommand(PickListPanel.ACTION_MOVE_ALL_RIGHT);
		}
		return btnMoveAllRight;
	}
	
	private JButton getBtnUp() {
		if (this.btnUp == null) {
			this.btnUp = new JButton();
			this.btnUp.setIcon(new ImageIcon(getClass().getResource("/images/content_up.png")));
			this.btnUp.setPreferredSize(new Dimension(20, 20));
			this.btnUp.setMaximumSize(new Dimension(20, 20));
			this.btnUp.setSize(new Dimension(20, 20));
			this.btnUp.setBorderPainted(false);
			this.btnUp.setOpaque(false);
			this.btnUp.setEnabled(false);
			this.btnUp.setToolTipText(SwingMessages.getString("tooltip.moveitem.up"));
			this.btnUp.setActionCommand(PickListPanel.ACTION_MOVE_UP);
			this.btnUp.addActionListener(this);
		}
		return this.btnUp;
	}
	
	private JButton getBtnDown() {
		if (this.btnDown == null) {
			this.btnDown = new JButton();
			this.btnDown.setIcon(new ImageIcon(getClass().getResource("/images/content_down.png")));
			this.btnDown.setPreferredSize(new Dimension(20, 20));
			this.btnDown.setMaximumSize(new Dimension(20, 20));
			this.btnDown.setSize(new Dimension(20, 20));
			this.btnDown.setBorderPainted(false);
			this.btnDown.setOpaque(false);
			this.btnDown.setEnabled(false);
			this.btnDown.setToolTipText(SwingMessages.getString("tooltip.moveitem.down"));
			this.btnDown.setActionCommand(PickListPanel.ACTION_MOVE_DOWN);
			this.btnDown.addActionListener(this);
		}
		return this.btnDown;
	}

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
    	String cmd = e.getActionCommand();
    	if (PickListPanel.ACTION_MOVE_LEFT.equalsIgnoreCase(cmd)) {
    		// add new item
    		Object[] selectedItems = lstRight.getSelectedValues();
    		for (int i = (selectedItems.length - 1); i >= 0; i--) {
    			this.data.getLstLeftModel().addElement(selectedItems[i]);
    			this.data.getLstRightModel().removeElement(selectedItems[i]);
    		}
    		this.disableButtons();
    		this.data.setModified(true);
    	} else if (PickListPanel.ACTION_MOVE_ALL_LEFT.equalsIgnoreCase(cmd)) {
    		// add all items
    		for (int i = (this.data.getLstRightModel().getSize() - 1); i >= 0; i--) {
    			Object current = this.data.getLstRightModel().getElementAt(i);
    			this.data.getLstLeftModel().addElement(current);
    			this.data.getLstRightModel().removeElement(current);
    		}
    		this.disableButtons();
    		this.data.setModified(true);
    	} else if (PickListPanel.ACTION_MOVE_RIGHT.equalsIgnoreCase(cmd)) {
    		// remove item
    		Object[] selectedItems = lstLeft.getSelectedValues();
    		for (int i = (selectedItems.length - 1); i >= 0; i--) {
    			this.data.getLstRightModel().addElement(selectedItems[i]);
    			this.data.getLstLeftModel().removeElement(selectedItems[i]);
    		}
    		this.disableButtons();
    		this.data.setModified(true);
    	} else if (PickListPanel.ACTION_MOVE_ALL_RIGHT.equalsIgnoreCase(cmd)) {
    		// remove all items
    		for (int i = (this.data.getLstLeftModel().getSize() - 1); i >= 0; i--) {
    			Object current = this.data.getLstLeftModel().getElementAt(i);
    			this.data.getLstRightModel().addElement(current);
    			this.data.getLstLeftModel().removeElement(current);
    		}
    		this.disableButtons();
    		this.data.setModified(true);
    	} else if (PickListPanel.ACTION_MOVE_UP.equalsIgnoreCase(cmd)) {
    		if (this.manuallySortable) {
    			if (this.lstLeft.isSelectionEmpty()) {
    				if (!this.lstRight.isSelectionEmpty()) {
    					moveItemUp(this.lstRight);
    				}
    			} else if (this.lstRight.isSelectionEmpty()) {
    				if (!this.lstLeft.isSelectionEmpty()) {
    					moveItemUp(this.lstLeft);
    				}
    			}
    		}
    	} else if (PickListPanel.ACTION_MOVE_DOWN.equalsIgnoreCase(cmd)) {
			if (this.manuallySortable) {
				if (this.lstLeft.isSelectionEmpty()) {
					if (!this.lstRight.isSelectionEmpty()) {
						moveItemDown(this.lstRight);
					}
				} else if (this.lstRight.isSelectionEmpty()) {
					if (!this.lstLeft.isSelectionEmpty()) {
						moveItemDown(this.lstLeft);
					}
				}
			}    		
    	}
    }
    
    private void moveItemDown(JList list) {
    	if (list.getSelectedIndices().length > 1) {
    		list.setSelectedIndex(list.getSelectedIndex());
    		return;
    	}
    	AbstractPickListModel listModel = (AbstractPickListModel) list.getModel();
		// erster Index, falls mehrere Items selektiert waren
		int selectedId = list.getSelectedIndex();
		Object lower = listModel.getElementAt(selectedId + 1);
		listModel.setElementAt(listModel.getElementAt(selectedId), selectedId + 1);
		listModel.setElementAt(lower, selectedId);
		list.setSelectedIndex(selectedId + 1);
    }
    
    private void moveItemUp(JList list) {
    	if (list.getSelectedIndices().length > 1) {
    		list.setSelectedIndex(list.getSelectedIndex());
    		return;
    	}
    	AbstractPickListModel listModel = (AbstractPickListModel) list.getModel();
    	// erster Index, falls mehrere Items selektiert waren
    	int selectedIndex = list.getSelectedIndex();
    	Object upper = listModel.getElementAt(selectedIndex - 1);
    	listModel.setElementAt(listModel.getElementAt(selectedIndex), selectedIndex - 1);
    	listModel.setElementAt(upper, selectedIndex);
    	list.setSelectedIndex(selectedIndex - 1);
    }
    
	private JList getLstLeft() {
		if (lstLeft == null) {
			lstLeft = new JList();
			lstLeft.setModel(this.data.getLstLeftModel());
			lstLeft.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (isEnabled) {
						if (manuallySortable) {
							repaintButtons(lstLeft);
						}
						if (!lstLeft.isSelectionEmpty()) {
							lstRight.clearSelection();
							btnMoveLeft.setEnabled(false);
							btnMoveAllLeft.setEnabled(false);
							btnMoveRight.setEnabled(true);
							btnMoveAllRight.setEnabled(true);
						}
					}
				}
			});
		}
		return lstLeft;
	}
	
	private JList getLstRight() {
		if (lstRight == null) {
			lstRight = new JList();
			lstRight.setModel(this.data.getLstRightModel());
			lstRight.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (isEnabled) {
						if (manuallySortable) {
							repaintButtons(lstRight);
						}
						if (!lstRight.isSelectionEmpty()) {
							lstLeft.clearSelection();
							btnMoveLeft.setEnabled(true);
							btnMoveAllLeft.setEnabled(true);
							btnMoveRight.setEnabled(false);
							btnMoveAllRight.setEnabled(false);
						}
					}
				}
			});
		}
		return lstRight;
	}
	private void repaintButtons(JList list) {
		if (isEnabled) {
			this.btnUp.setEnabled(true);
			this.btnDown.setEnabled(true);

			// First Element
			if (list.isSelectionEmpty() || list.getSelectedValue().equals(((AbstractPickListModel) list.getModel()).firstElement())) {
				this.btnUp.setEnabled(false);
			} else {
				this.btnUp.setEnabled(true);
			}
			// Last Element
			if (list.isSelectionEmpty() || list.getSelectedValue().equals(((AbstractPickListModel) list.getModel()).lastElement())) {
				this.btnDown.setEnabled(false);
			} else {
				this.btnDown.setEnabled(true);
			}
		} else {
			this.btnUp.setEnabled(false);
			this.btnDown.setEnabled(false);
		}
	}

	private void disableButtons() {
		this.lstLeft.clearSelection();
		this.lstRight.clearSelection();
		this.btnMoveLeft.setEnabled(false);
		this.btnMoveAllLeft.setEnabled(false);
		this.btnMoveRight.setEnabled(false);
		this.btnMoveAllRight.setEnabled(false);
	}
	
	/**
	 * Enable or disable the PickListPanel
	 * @see java.awt.Component#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabling) {
		this.isEnabled = enabling;
		this.disableButtons();
		lstLeft.clearSelection();
		lstLeft.setEnabled(enabling);
		lstRight.clearSelection();
		lstRight.setEnabled(enabling);
	}

	private JScrollPane getSpLeft() {
		if (spLeft == null) {
			spLeft = new NoResizeScrollPane(getLstLeft());
		}
		return this.spLeft;
	}

	private JScrollPane getSpRight() {
		if (spRight == null) {
			spRight = new NoResizeScrollPane(getLstRight());
		}
		return this.spRight;
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"