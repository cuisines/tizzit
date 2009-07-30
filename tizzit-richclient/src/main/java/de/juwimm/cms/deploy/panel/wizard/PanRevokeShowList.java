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

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.ViewComponentValue;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanRevokeShowList extends PanAuthorShowList {
	private static Logger log = Logger.getLogger(PanRevokeShowList.class);
	private Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	
	public PanRevokeShowList() {
		try {
			jbInit();
			getTblChange().setRowHeight(22);
			getTblChange().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			getTblChange().getSelectionModel().addListSelectionListener(new MyListSelectionListener());
			getBtnPreview().setEnabled(true);
			getLblChanged().setText(rb.getString("wizard.author.revoke.lblChanged"));
			getBtnPreview().setText(rb.getString("wizard.author.getApproval.btnPreview"));
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	public void setUnitId(int unitId) {
		setInternalUnitId(unitId);
		fillTable(Constants.DEPLOY_STATUS_FOR_APPROVAL, this.getTblChange(), new MyTableCellRenderer());
	}
	
	/**
	 * @version $Id$
	 */
	private class MyTableCellRenderer extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			String s = value.toString();
			if (s.equals("0")) {
				setText(rb.getString("wizard.author.getApproval.ADD"));
			} else if (s.equals("1") || s.equals("2")) {
				setText(rb.getString("wizard.author.getApproval.DELETE"));
			} else if (s.equals("3")) {
				setText(rb.getString("wizard.author.getApproval.MODIFY"));
			}
			return this;
		}
	}

	public void save() {
		//REVOKE SAVE
		int count = getTblModel().getRowCount();
		ViewComponentValue view;
		String command;

		for (int i = 0; i < count; i++) {
			if (((Boolean) getTblModel().getValueAt(i, 2)).booleanValue()) {
				try {
					view = (ViewComponentValue) getTblModel().getValueAt(i, 4);
					try {
						command = (String) getTblModel().getValueAt(i, 1);
						if (command.equals(rb.getString("wizard.author.getApproval.ADD"))) {
							view.setDeployCommand((byte) 0);
						} else if (command.equals(rb.getString("wizard.author.getApproval.DELETE"))) {
							view.setDeployCommand((byte) 1);
						} else if (command.equals(rb.getString("wizard.author.getApproval.MODIFY"))) {
							view.setDeployCommand((byte) 3);
						}
					} catch (Exception ex) {
					}

					view.setStatus(Constants.DEPLOY_STATUS_EDITED);
					comm.updateStatus4ViewComponent(view);
					try {
						comm.setStatus4ViewComponentId(view.getViewComponentId(),
								Constants.DEPLOY_STATUS_EDITED);
					} catch (Exception exe) {
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(UIConstants.getMainFrame(),
							"Der Status des Contents kann nicht ge�ndert werden\n" + ex.getMessage(), "CMS",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

}
