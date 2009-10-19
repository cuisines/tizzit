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

import static de.juwimm.cms.client.beans.Application.getBean;
import static de.juwimm.cms.common.Constants.rb;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.gui.event.ChooseTemplateListener;
import de.juwimm.cms.gui.tree.PageNode;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.UnitValue;
import de.juwimm.swing.CustomComboBoxModel;
import de.juwimm.swing.DropDownHolder;

/**
 * <b>Tizzit Enterprise Content Management</b><br/>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class ChooseTemplateDialog extends JDialog {
	private static Logger log = Logger.getLogger(ChooseTemplateDialog.class);
	private final Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private ChooseTemplateListener ctl = null;
	private PageNode pageNode = null;
	private int position;
	private final JButton cmdCancel = new JButton();
	private final JButton cmdOK = new JButton();
	private final JComboBox cbxTemplates = new JComboBox();
	private final JLabel lblSelectTemplate = new JLabel();
	private final JComboBox cbxUnits = new JComboBox();
	private final JCheckBox cbCreateUnit = new JCheckBox();

	/**
	 * Use this, if you only want to select the template, nothing more.
	 * @param ctl The Listener to inform
	 */
	public ChooseTemplateDialog(ChooseTemplateListener ctl) {
		this(ctl, null, -1);
		this.cbCreateUnit.setVisible(false);
		this.cbxUnits.setVisible(false);
	}

	public ChooseTemplateDialog(ChooseTemplateListener ctl, PageNode pgNode, int pos) {
		super(UIConstants.getMainFrame(), rb.getString("panel.chooseTemplate.title"), true);
		try {
			jbInit();
			this.cmdCancel.setText(rb.getString("dialog.cancel"));
			this.cmdOK.setText(rb.getString("dialog.ok"));
			this.lblSelectTemplate.setText(rb.getString("panel.chooseTemplate.selectTemplate"));
			this.cbCreateUnit.setText(rb.getString("panel.chooseTemplate.createUnit"));
			Iterator it = Constants.CMS_AVAILABLE_DCF.getKeyOrder().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				HashMap val = (HashMap) Constants.CMS_AVAILABLE_DCF.get(key);
				if (val == null) {
					JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("exception.TEMPLATE_NOT_FOUND"), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
				}
				String role = "";
				try {
					role = (String) val.get("role");
				} catch (NullPointerException exe) {
				}
				if (role.equals("") || comm.isUserInRole(role)) {
					this.cbxTemplates.addItem(new DropDownHolder(key, (String) val.get("description")));
				} else if (role.indexOf(',') > 0) {
					// several roles for the current template
					StringTokenizer st = new StringTokenizer(role, ",");
					while (st.hasMoreTokens()) {
						String currRole = st.nextToken();
						if (comm.isUserInRole(currRole)) {
							cbxTemplates.addItem(new DropDownHolder(key, (String) val.get("description")));
							break;
						}
					}
				}
			}
			this.cbxTemplates.setSelectedIndex(0);

			if (comm.isUserInRole(UserRights.SITE_ROOT)) {
				cbxUnits.setModel(new CustomComboBoxModel(comm.getNotReferencedUnits(comm.getViewDocument()), "getName"));
				if (cbxUnits.getModel().getSize() <= 0) {
					this.cbCreateUnit.setEnabled(false);
				}
			} else {
				this.cbCreateUnit.setVisible(false);
				this.cbxUnits.setVisible(false);
			}

			this.ctl = ctl;
			this.pageNode = pgNode;
			this.position = pos;

			this.setSize(350, 200);
			this.setLocationRelativeTo(UIConstants.getMainFrame());
			this.setVisible(true);
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	void jbInit() throws Exception {
		this.getContentPane().setLayout(new GridBagLayout());
		cmdCancel.setSelectedIcon(null);
		cmdCancel.setText("Cancel");
		cmdCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdCancelActionPerformed(e);
			}
		});
		cmdOK.setText("OK");
		cmdOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdOkActionPerformed(e);
			}
		});
		lblSelectTemplate.setText("Bitte wählen Sie ein Template aus.");
		cbxUnits.setEnabled(false);
		cbCreateUnit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cbCreateUnitActionPerformed(e);
			}
		});
		this.getContentPane().add(cbxUnits, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));
		this.getContentPane().add(cbCreateUnit, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));
		this.getContentPane().add(cmdCancel, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		this.getContentPane().add(cmdOK, new GridBagConstraints(0, 5, 1, 1, 0.0, 1.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(10, 10, 10, 0), 0, 0));
		this.getContentPane().add(lblSelectTemplate, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));
		this.getContentPane().add(cbxTemplates, new GridBagConstraints(0, 4, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));
	}

	private void close() {
		this.setVisible(false);
		this.dispose();
	}

	private void cmdCancelActionPerformed(ActionEvent e) {
		close();
	}

	private void cmdOkActionPerformed(ActionEvent e) {
		{
			// disable dialog when ok-button is pressed
			this.cmdOK.setEnabled(false);
			this.cmdCancel.setEnabled(false);
			this.cbCreateUnit.setEnabled(false);
			this.cbxTemplates.setEnabled(false);
			this.cbxUnits.setEnabled(false);
		}
		int unitId = 0;
		if (cbCreateUnit.isSelected() && cbxUnits.getSelectedIndex() >= 0) {
			unitId = ((UnitValue) ((DropDownHolder) cbxUnits.getSelectedItem()).getObject()).getUnitId();
		}
		ctl.chooseTemplatePerformed(unitId, pageNode, ((DropDownHolder) cbxTemplates.getSelectedItem()).getObject().toString(), position);
		close();
	}

	private void cbCreateUnitActionPerformed(ActionEvent e) {
		if (cbCreateUnit.isSelected()) {
			cbxUnits.setEnabled(true);
		} else {
			cbxUnits.setEnabled(false);
		}
	}

}
