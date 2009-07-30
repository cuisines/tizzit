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
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.deploy.frame.Wizard;
import de.juwimm.cms.deploy.panel.table.PagesTableModel;
import de.juwimm.cms.gui.table.TableSorter;
import de.juwimm.cms.util.ActionHub;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.vo.ViewComponentValue;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanAuthorShowList extends JPanel implements WizardPanel {
	private static Logger log = Logger.getLogger(PanAuthorShowList.class);
	private Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private Wizard wizard;
	private PagesTableModel tblModel;
	private int internalUnitId = 0;
	private int taskId = 0;

	private JTable tblChange = new JTable();
	private JScrollPane scpChange = new JScrollPane();
	private JLabel lblChanged = new JLabel();
	private GridBagLayout gridBagLayout = new GridBagLayout();
	private JButton btnPreview = new JButton();
	private JTextField txtPath = new JTextField();

	public PanAuthorShowList() {
		try {
			jbInit();
			getTblChange().setRowHeight(22);
			getTblChange().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			getTblChange().getSelectionModel().addListSelectionListener(new MyListSelectionListener());
			getBtnPreview().setEnabled(true);
			getLblChanged().setText(rb.getString("wizard.author.getApproval.lblChanged"));
			getBtnPreview().setText(rb.getString("wizard.author.getApproval.btnPreview"));
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	public void setWizard(Wizard wizard) {
		this.wizard = wizard;
		if (this.getTblModel().getRowCount() == 0) {
			wizard.setNextEnabled(false);
		} else {
			wizard.setNextEnabled(true);
		}
	}

	public void setUnitId(int unitId) {
		this.setInternalUnitId(unitId);
		fillTable(Constants.DEPLOY_STATUS_EDITED, this.getTblChange(), new MyTableCellRenderer());
	}

	void jbInit() throws Exception {
		this.setLayout(getGridBagLayout());
		getLblChanged().setText("PAGES");
		getBtnPreview().setText("SELECT");
		getBtnPreview().addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPreviewActionPerformed(e);
			}
		});
		txtPath.setEditable(false);
		txtPath.setText("");
		this.add(getScpChange(), new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(5, 10, 0, 10), 230, 0));
		this.add(getLblChanged(), new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 0), 0, 0));
		this.add(getBtnPreview(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(5, 0, 10, 10), 0, 0));
		this.add(txtPath, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 10, 10, 10), 136, 0));
		getScpChange().getViewport().add(getTblChange());
	}

	protected void fillTable(int status, JTable tbl, DefaultTableCellRenderer cellRenderer) {
		try {
			Vector viewComponents;
			if (getInternalUnitId() <= 0) {
				viewComponents = comm.getAllViewComponents4Status(status);
			} else {
				viewComponents = comm.getViewComponents4Status(getInternalUnitId(), status);
			}
			setTblModel(new PagesTableModel(viewComponents));
			getTblModel().addTableModelListener(new TableModelListener() {
				public void tableChanged(TableModelEvent e) {
					boolean retVal = false;
					for (int i = 0; i < getTblChange().getRowCount(); i++) {
						boolean sel = ((Boolean) getTblChange().getValueAt(i, 2)).booleanValue();
						if (sel) {
							retVal = true;
						}
					}
					if (retVal) {
						wizard.setNextEnabled(true);
					} else {
						wizard.setNextEnabled(false);
					}
				}
			});

			TableSorter tblSorter = new TableSorter(getTblModel(), getTblChange().getTableHeader());
			getTblChange().setModel(tblSorter);
			//m_Sorter.addMouseListenerToHeaderInTable(tbl);

			TableColumn column = tbl.getColumnModel().getColumn(0);
			column.setPreferredWidth(290);
			column = tbl.getColumnModel().getColumn(1);
			column.setPreferredWidth(100);
			column.setCellRenderer(cellRenderer);

		} catch (Exception exe) {
			log.error("Error", exe);
		}
	}

	/**
	 * @version $Id$
	 */
	protected class MyTableCellRenderer extends DefaultTableCellRenderer {
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

	/**
	 * @version $Id$
	 */
	protected class MyListSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			txtPath.setText((String) getTblModel().getValueAt(getTblChange().getSelectedRow(), 3));
			getBtnPreview().setEnabled(true);
		}
	}

	private void btnPreviewActionPerformed(ActionEvent e) {
		ViewComponentValue view = (ViewComponentValue) getTblModel().getValueAt(getTblChange().getSelectedRow(), 4);
		ActionHub.fireActionPerformed(new ActionEvent(view.getViewComponentId() + "", ActionEvent.ACTION_PERFORMED,
				Constants.ACTION_TREE_JUMP));
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public void save() {
		int count = getTblModel().getRowCount();
		ViewComponentValue view;
		String command;

		Vector<Integer> selectedVcs = new Vector<Integer>();

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
					selectedVcs.add(new Integer(view.getViewComponentId()));
					view.setStatus(Constants.DEPLOY_STATUS_FOR_APPROVAL);
					comm.updateStatus4ViewComponent(view);
					try {
						comm.setStatus4ViewComponentId(view.getViewComponentId(), Constants.DEPLOY_STATUS_FOR_APPROVAL);
					} catch (Exception exe) {
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this, "Der Status des Contents kann nicht ge�ndert werden\n"
							+ ex.getMessage(), "CMS", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		try {
			comm.addViewComponentsToTask(taskId, selectedVcs.toArray(new Integer[0]));
		} catch (Exception exe) {
			log.error("Error adding vc to task " + taskId, exe);
		}
	}

	/**
	 * @param tblModel The tblModel to set.
	 */
	protected void setTblModel(PagesTableModel tblModel) {
		this.tblModel = tblModel;
	}

	/**
	 * @return Returns the tblModel.
	 */
	protected PagesTableModel getTblModel() {
		return tblModel;
	}

	/**
	 * @param tblChange The tblChange to set.
	 */
	void setTblChange(JTable tblChange) {
		this.tblChange = tblChange;
	}

	/**
	 * @return Returns the tblChange.
	 */
	JTable getTblChange() {
		return tblChange;
	}

	/**
	 * @param scpChange The scpChange to set.
	 */
	void setScpChange(JScrollPane scpChange) {
		this.scpChange = scpChange;
	}

	/**
	 * @return Returns the scpChange.
	 */
	JScrollPane getScpChange() {
		return scpChange;
	}

	/**
	 * @param lblChanged The lblChanged to set.
	 */
	void setLblChanged(JLabel lblChanged) {
		this.lblChanged = lblChanged;
	}

	/**
	 * @return Returns the lblChanged.
	 */
	JLabel getLblChanged() {
		return lblChanged;
	}

	/**
	 * @param gridBagLayout The gridBagLayout to set.
	 */
	void setGridBagLayout(GridBagLayout gridBagLayout) {
		this.gridBagLayout = gridBagLayout;
	}

	/**
	 * @return Returns the gridBagLayout.
	 */
	GridBagLayout getGridBagLayout() {
		return gridBagLayout;
	}

	/**
	 * @param btnPreview The btnPreview to set.
	 */
	void setBtnPreview(JButton btnPreview) {
		this.btnPreview = btnPreview;
	}

	/**
	 * @return Returns the btnPreview.
	 */
	JButton getBtnPreview() {
		return btnPreview;
	}

	/**
	 * @param internalUnitId The internalUnitId to set.
	 */
	protected void setInternalUnitId(int internalUnitId) {
		this.internalUnitId = internalUnitId;
	}

	/**
	 * @return Returns the internalUnitId.
	 */
	protected int getInternalUnitId() {
		return internalUnitId;
	}
}