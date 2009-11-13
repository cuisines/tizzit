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
package de.juwimm.cms.gui.views.menuentry;

import static de.juwimm.cms.client.beans.Application.getBean;
import static de.juwimm.cms.common.Constants.rb;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import de.juwimm.cms.Messages;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.gui.ChooseTemplateDialog;
import de.juwimm.cms.gui.PanTree;
import de.juwimm.cms.gui.event.ChooseTemplateListener;
import de.juwimm.cms.gui.tree.PageNode;
import de.juwimm.cms.gui.tree.TreeNode;
import de.juwimm.cms.util.ActionHub;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.ViewComponentValue;

/**
 * <p>Title: Tizzit </p>
 * <p>Description: Content Management System</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanMenuentryContent extends PanMenuentry implements ChooseTemplateListener {
	private static Logger log = Logger.getLogger(PanMenuentryContent.class);
	private final Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private final JLabel lblTemplate = new JLabel();
	private final JLabel lblTemplateText = new JLabel();
	private boolean shouldBeEditable = false; //will be set through the "load" Method
	private final JPanel panTemplateUnit = new JPanel();
	private final JLabel lblUnitText = new JLabel();
	private final JLabel lblUnit = new JLabel();
	private Component component1;
	private final JButton btnChangeTemplate = new JButton();
	private final JButton btnChangeUnit = new JButton();

	public PanMenuentryContent() {
		super();
		try {
			jbInit();
			this.getChkOpenNewNavi().addKeyListener(ActionHub.getContentEditKeyListener());
			this.getChkOpenNewNavi().addMouseListener(ActionHub.getContentEditMouseListener());
			lblTemplate.setText(rb.getString("panel.panelView.content.template"));
			lblUnit.setText(rb.getString("panel.panelView.content.unit"));
			btnChangeTemplate.setText(rb.getString("dialog.change"));
			btnChangeUnit.setText(rb.getString("dialog.change"));
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	void jbInit() throws Exception {
		component1 = Box.createVerticalStrut(8);
		lblTemplate.setText("Verwendetes Template");
		lblTemplateText.setFont(new java.awt.Font("Dialog", 1, 11));
		lblTemplateText.setBorder(BorderFactory.createLoweredBevelBorder());
		lblTemplateText.setText("5563535");
		panTemplateUnit.setBorder(BorderFactory.createEtchedBorder());
		panTemplateUnit.setLayout(new GridBagLayout());
		lblUnitText.setFont(new java.awt.Font("Dialog", 1, 11));
		lblUnitText.setBorder(BorderFactory.createLoweredBevelBorder());
		lblUnitText.setText("This Page is a Unit");
		lblUnit.setText("Unit");

		getOptPan().setLayout(new GridBagLayout());
		btnChangeTemplate.setText("Change");
		btnChangeTemplate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				btnChangeTemplateActionPerformed(ae);
			}
		});
		btnChangeUnit.setText("Change");
		btnChangeUnit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				btnChangeUnitActionPerformed(ae);
			}
		});
		panTemplateUnit.add(lblTemplate, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 10, 0), 0, 5));
		panTemplateUnit.add(lblTemplateText, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 5));
		panTemplateUnit.add(btnChangeTemplate, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(10, 0, 10, 10), 0, 0));
		panTemplateUnit.add(lblUnit, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 10, 0), 0, 0));
		panTemplateUnit.add(lblUnitText, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 10, 10), 0, 5));
		panTemplateUnit.add(btnChangeUnit, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 10, 10), 0, 0));
		getOptPan().add(component1, new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
		getOptPan().add(panTemplateUnit, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(8, 10, 10, 10), 0, 0));
	}

	@Override
	public void unload() {
		super.unload();

	}

	@Override
	public void load(ViewComponentValue viewComponentValue) {
		super.load(viewComponentValue);

		try {
			if (comm.isUserInRole(UserRights.SITE_ROOT)) {
				this.btnChangeTemplate.setVisible(true);
				//this.btnChangeUnit.setVisible(true);
			} else {
				this.btnChangeTemplate.setVisible(false);
				//this.btnChangeUnit.setVisible(false);
			}
			this.btnChangeUnit.setVisible(false);
			String strTplName = comm.getContentTemplateName(viewComponentValue.getViewComponentId());

			HashMap hm = (HashMap) Constants.CMS_AVAILABLE_DCF.get(strTplName);
			if (hm == null) {
				// Template not found
				setMenuentryEnabled(false);
				shouldBeEditable = false;
				String msg = Messages.getString("exception.TemplateNotFound", strTplName);
				JOptionPane.showMessageDialog(UIConstants.getMainFrame(), msg, rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
				log.error("Template '" + strTplName + "' on Server " + Constants.CMS_PATH_DCF + " not found!");
				lblTemplateText.setText(strTplName);
				this.refreshUnitPanel();
				return;
			}
			String editableBy = (String) hm.get("editableBy");
			if (!editableBy.equals("") && !comm.isUserInRole(editableBy)) {
				setMenuentryEnabled(false);
				shouldBeEditable = false;
			} else {
				setMenuentryEnabled(true);
				shouldBeEditable = true;
			}
			lblTemplateText.setText((String) hm.get("description"));
			this.refreshUnitPanel();
		} catch (Exception exe) {
			log.error("Load Error", exe);
		}
	}

	private void refreshUnitPanel() {
		if (this.getViewComponent().getUnitId() != null) {
			lblUnitText.setText(rb.getString("panel.panelView.content.isAUnit"));
			Thread t = new Thread(new Runnable() {
				public void run() {
					try {
						String unitDetails = null;
						Integer unitId = getViewComponent().getUnitId();
						if (log.isDebugEnabled()) {
							unitDetails = unitId.toString() + ": " + comm.getUnit(unitId.intValue()).getName();
						} else {
							unitDetails = comm.getUnit(unitId.intValue()).getName();
						}
						lblUnitText.setText(unitDetails);
						lblUnitText.updateUI();
					} catch (Exception exe) {
					}
				}
			});
			t.setPriority(Thread.NORM_PRIORITY);
			t.start();
		} else {
			lblUnitText.setText(" ");
		}
	}

	private void btnChangeTemplateActionPerformed(ActionEvent e) {
		int retType = JOptionPane.showConfirmDialog(UIConstants.getMainFrame(), rb.getString("panel.panelView.content.changeTemplateWarning"), rb.getString("dialog.title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (retType == JOptionPane.YES_OPTION) {
			new ChooseTemplateDialog(this);
		}
	}

	private void btnChangeUnitActionPerformed(ActionEvent e) {
		int retType = JOptionPane.showConfirmDialog(UIConstants.getMainFrame(), rb.getString("panel.panelView.content.changeTemplateWarning"), rb.getString("dialog.title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (retType == JOptionPane.YES_OPTION) {
			new ChooseTemplateDialog(this);
		}
	}

	public void chooseTemplatePerformed(int unitId, PageNode parentEntry, String template, int position) {
		log.debug("Selected template: " + template);
		try {
			comm.updateTemplate(this.getViewComponent().getViewComponentId(), template);
			TreeNode entry = PanTree.getSelectedEntry();
			ActionHub.fireActionPerformed(new ActionEvent(entry, ActionEvent.ACTION_PERFORMED, Constants.ACTION_TREE_SELECT));
			JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("panel.panelView.content.changeTemplateWarningCache"), rb.getString("dialog.title"), JOptionPane.WARNING_MESSAGE);
		} catch (Exception exe) {
			log.error("Choosing Templates Error", exe);
		}
	}

	public boolean shouldBeEdtiable() {
		return shouldBeEditable;
	}

	public void setMenuentryEnabled(boolean enabled) {
		log.debug("setMenuentryEnabled" + enabled);
		/*	this.btnChangeTemplate.setEnabled(enabled);
			this.getTxtOnlineStart().setDateButtonEnabled(enabled);
			this.getTxtOnlineStop().setDateButtonEnabled(enabled);
			this.getTxtText().setEnabled(enabled);
			this.getTxtInfo().setEnabled(enabled);
			this.getChkOpenNewNavi().setEnabled(enabled);
			this.getOptSelectShow().setEnabled(enabled);
			{
				if (this.comm.isUserInRole(UserRights.SITE_ROOT) || this.comm.isUserInRole(UserRights.PAGE_EDIT_URL_LINKNAME)) {
					this.getTxtUrlLinkName().setEditable(enabled);
				} else {
					this.getTxtUrlLinkName().setEditable(false);
				}
			}*/
	}

	public void setTemplateButtonEnabled(boolean enabled) {
		this.btnChangeTemplate.setEnabled(enabled);
	}

}