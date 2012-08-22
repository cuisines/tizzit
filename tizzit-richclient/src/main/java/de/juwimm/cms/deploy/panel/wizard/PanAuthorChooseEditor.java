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
package de.juwimm.cms.deploy.panel.wizard;

import static de.juwimm.cms.client.beans.Application.*;
import static de.juwimm.cms.common.Constants.*;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;

import org.apache.log4j.Logger;

import de.juwimm.cms.authorization.vo.GroupValue;
import de.juwimm.cms.authorization.vo.UserValue;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.deploy.frame.Wizard;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.swing.CustomComboBoxModel;
import de.juwimm.swing.DropDownHolder;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanAuthorChooseEditor extends JPanel implements WizardPanel, ItemListener {
	private static Logger log = Logger.getLogger(PanAuthorChooseEditor.class);
	private Wizard wizard;
	private int unitId = 0;
	private Communication communication = ((Communication) getBean(Beans.COMMUNICATION));
	private boolean sendMessage = false;
	private int createdTaskId = 0;
	private JLabel lblMessage = new JLabel();
	private JScrollPane jScrollPane1 = new JScrollPane();
	private JTextArea txtMessage = new JTextArea();
	private JPanel panSelectOneUser = new JPanel();
	private JComboBox cboUser = new JComboBox();
	private JLabel lblGroups = new JLabel();
	private JLabel lblUser = new JLabel();
	private JButton btnFindPerRole = new JButton();
	private JComboBox cboGroups = new JComboBox();
	private JComboBox cboRights = new JComboBox();
	private ComboBoxModel cboGroupModel = null;

	private ButtonGroup buttonGroup1 = new ButtonGroup();
	private JRadioButton optUser = new JRadioButton();
	private JRadioButton optRight = new JRadioButton();

	public PanAuthorChooseEditor() {
		try {
			jbInit();
			cboRights.setRenderer(new RightsCellRenderer());
			if (rb != null) {
				lblMessage.setText(rb.getString("wizard.author.chooseEditor.lblMessage"));
				lblUser.setText(rb.getString("wizard.author.chooseEditor.lblUser"));
				lblGroups.setText(rb.getString("wizard.author.chooseEditor.lblGroup"));
				btnFindPerRole.setText(rb.getString("wizard.author.chooseEditor.findUserPerGroup"));
				optUser.setText(rb.getString("wizard.author.chooseEditor.sendToSpecificUser"));
				optRight.setText(rb.getString("wizard.author.chooseEditor.sendToAllWithRight"));
			}
			cboUser.addItemListener(this);
			optRight.addItemListener(this);
			optUser.addItemListener(this);
			cboGroups.addItemListener(this);
			cboGroups.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent ie) {
					cboUser.setModel(new DefaultComboBoxModel());
				}
			});
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	public PanAuthorChooseEditor(boolean sendMessage) {
		this();
		this.sendMessage = sendMessage;
	}

	public void setWizard(Wizard wizard) {
		this.wizard = wizard;
		if ((cboUser.getItemCount() > 0) || (optRight.isSelected())) {
			wizard.setNextEnabled(true);
		} else {
			wizard.setNextEnabled(false);
		}
	}

	public void setUnitId(int unitId) {
		if (log.isDebugEnabled()) log.debug("setUnitId " + unitId);
		this.unitId = unitId;
		if (this.unitId > 0) {
			try {
				cboRights.removeAllItems();
				cboRights.addItem("deploy");
				cboRights.addItem("approve");
				cboRights.addItem("unitAdmin");

				cboGroups.removeAllItems();
				GroupValue[] gv = communication.getAllGroupsUsedInUnit(this.unitId);
				CustomComboBoxModel ccm = new CustomComboBoxModel(gv, "getGroupName");
				cboGroupModel = ccm;
				cboGroups.setModel(ccm);
			} catch (Exception ex) {
			}
		}
	}

	void jbInit() throws Exception {
		this.setLayout(new GridBagLayout());
		lblMessage.setText("Nachricht:");
		txtMessage.setBorder(BorderFactory.createLoweredBevelBorder());
		txtMessage.setText("");
		jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		optUser.setSelected(false);
		optUser.setText("An einen bestimmten Benutzer schicken:");
		optRight.setSelected(true);
		optRight.setText("An alle Benutzer schicken, die folgendes Recht haben:");
		panSelectOneUser.setBorder(BorderFactory.createEtchedBorder());
		panSelectOneUser.setLayout(new GridBagLayout());
		lblGroups.setHorizontalAlignment(SwingConstants.RIGHT);
		lblGroups.setHorizontalTextPosition(SwingConstants.RIGHT);
		lblGroups.setText("Groups");
		lblUser.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUser.setHorizontalTextPosition(SwingConstants.RIGHT);
		lblUser.setText("User");
		btnFindPerRole.setText("Find User");
		btnFindPerRole.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performActionBtnFindPerRole(e);
			}
		});
		this.add(jScrollPane1, new GridBagConstraints(0, 5, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 70, 20, 70), 0, 0));
		jScrollPane1.getViewport().add(txtMessage, null);
		panSelectOneUser.add(cboUser, new GridBagConstraints(1, 1, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 10), 254, 0));
		panSelectOneUser.add(btnFindPerRole, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(5, 0, 0, 10), 0, 0));
		panSelectOneUser.add(cboGroups, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 168, 0));
		panSelectOneUser.add(lblGroups, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 5), 8, 0));
		panSelectOneUser.add(lblUser, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(5, 10, 5, 5), 0, 0));
		this.add(optRight, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(10, 50, 0, 50), 0, 0));
		this.add(lblMessage, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(10, 50, 0, 50), 0, 0));
		this.add(cboRights, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 70, 0, 70), 0, 0));
		this.add(optUser, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(10, 50, 0, 50), 0, 0));
		this.add(panSelectOneUser, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 70, 0, 70), 0, 0));
		buttonGroup1.add(optRight);
		buttonGroup1.add(optUser);
		this.enableOption();
	}

	/**
	 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
	 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
	 * @version $Id$
	 */
	private class RightsCellRenderer extends DefaultListCellRenderer implements ListCellRenderer<Object> {
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			return super.getListCellRendererComponent(list, rightsView(((String) value)), index, isSelected, cellHasFocus);
		}

		private String rightsView(String role) {
			String ret = role;
			try {
				ret = rb.getString("wizard.author.chooseEditor.cboRights." + ret);
			} catch (Exception exe) {
			}
			return ret;
		}
	}

	public void save() {
		byte tt = 0;
		if (sendMessage) {
			tt = Constants.TASK_MESSAGE;
		} else {
			tt = Constants.TASK_APPROVE;
		}
		try {
			if (optRight.isSelected()) {
				String right = (String) cboRights.getSelectedItem();
				createdTaskId = communication.createTask(null, right, this.unitId, txtMessage.getText(), tt);
			} else {
				Object obj = cboUser.getModel().getElementAt(cboUser.getSelectedIndex());
				UserValue receiver = ((UserValue) ((DropDownHolder) obj).getObject());
				createdTaskId = communication.createTask(receiver.getUserName(), null, this.unitId, txtMessage.getText(), tt);
			}
			UIConstants.setStatusInfo(rb.getString("actions.TASK_FOR_EDITOR"));
		} catch (Exception exe) {
			log.error("Error during save", exe);
			JOptionPane.showMessageDialog(this, rb.getString("exception.createTask.chooseEditor"), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
		}
	}

	public int getCreatedTaskId() {
		return createdTaskId;
	}

	public void itemStateChanged(ItemEvent ie) {
		this.enableOption();
		if (cboUser.getSelectedIndex() >= 0 || optRight.isSelected()) {
			wizard.setNextEnabled(true);
		} else {
			wizard.setNextEnabled(false);
		}
	}

	private void enableOption() {
		this.cboGroups.setEnabled(!optRight.isSelected());
		this.cboUser.setEnabled(!optRight.isSelected());
		this.btnFindPerRole.setEnabled(!optRight.isSelected());
		this.cboRights.setEnabled(optRight.isSelected());
	}

	void performActionBtnFindPerRole(ActionEvent e) {
		int groupId = 0;
		try {
			groupId = ((GroupValue) ((DropDownHolder) cboGroupModel.getElementAt(cboGroups.getSelectedIndex())).getObject()).getGroupId();
		} catch (Exception exe) {
		} finally {
			UserValue[] uv = new UserValue[0];
			if (groupId > 0) {
				uv = communication.getAllUser(groupId, this.unitId);
			}
			if (uv != null) {
				cboUser.setModel(new CustomComboBoxModel(uv, "getLastName", "getFirstName"));
			} else {
				cboUser.setModel(new DefaultComboBoxModel());
			}
			itemStateChanged(null);
		}
	}

}