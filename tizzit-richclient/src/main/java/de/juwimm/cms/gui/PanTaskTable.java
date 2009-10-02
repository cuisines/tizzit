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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;
import org.tizzit.util.DateConverter;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.gui.table.TaskTableModel;
import de.juwimm.cms.util.ActionHub;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.TaskValue;

/**
 * <p>Title: juwimm cms</p>
 * <p>Description: content management system</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: juwi macmillan group gmbh</p>
 * @author Dirk Bogun
 * @version $Id$
 */
public class PanTaskTable extends JPanel implements ActionListener {
	private static final Logger log = Logger.getLogger(PanTaskTable.class);
	public static final String APPROVE = rb.getString("panel.panelTaskTable.APPROVE");
	public static final String REJECT = rb.getString("panel.panelTaskTable.REJECT");
	public static final String MESSAGE = rb.getString("panel.panelTaskTable.MESSAGE");
	private Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private JScrollPane scrollPane;
	private JPanel panel = new JPanel();
	private JButton btnDelete = new JButton();
	private TaskTableModel tableModel;
	private JTable table = new JTable();
	private int viewedTaskId = -1;

	public PanTaskTable() {
		try {
			setDoubleBuffered(true);
			jbInit();
			ActionHub.addActionListener(this);
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	void jbInit() throws Exception {
		this.setLayout(new GridBagLayout());
		Dimension dim = table.getTableHeader().getPreferredSize();
		table.getTableHeader().setPreferredSize(new Dimension(dim.width, 20));
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		table.getSelectionModel().addListSelectionListener(new MyListSelectionListener());
		scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(300, 0));

		panel.setLayout(new GridBagLayout());
		btnDelete.setEnabled(false);
		btnDelete.setPreferredSize(new Dimension(30, 30));
		btnDelete.setToolTipText(rb.getString("panel.task.delete.single"));
		btnDelete.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDeleteActionPerformed(e);
			}
		});
		this.add(scrollPane, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(panel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		btnDelete.setIcon(UIConstants.MODULE_DATABASECOMPONENT_DELETE);

		panel.add(btnDelete, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(3, 0, 3, 5), 0, 0));
	}

	protected void fillTable() {
		try {
			TaskValue[] tasks = comm.getAllTasks();
			if (tasks != null) {
				tableModel = new TaskTableModel(tasks);
				table.setModel(tableModel);
			} else {
				DefaultTableModel tm = new DefaultTableModel();
				
				table.setModel(tm);
				tm.fireTableDataChanged();
			}
			setEditor();
		} catch (Exception exe) {
			log.error("Error filling Table", exe);
		}
	}

	protected void setEditor() {
		try {
			TableColumn column = table.getColumnModel().getColumn(0);
			column.setPreferredWidth(60);
			column = table.getColumnModel().getColumn(1);
			column.setPreferredWidth(120);
			column = table.getColumnModel().getColumn(2);
			column.setCellRenderer(new MyTableCellRenderer());
			column = table.getColumnModel().getColumn(3);
			column.setPreferredWidth(60);
		} catch (Exception ex) {
		}
	}
	
	/**
	 * @version $Id$
	 */
	private class MyTableCellRenderer extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			this.setHorizontalAlignment(JLabel.CENTER);
			if (value instanceof Calendar) {
				this.setText(getDateOrTime((Calendar) value));
			}

			return this;
		}

		private String getDateOrTime(Calendar timeStamp) {
			if (DateConverter.isDateToday(timeStamp)) {
				return DateConverter.getSql2TimeString(timeStamp);
			}
			return DateConverter.getSql2String(timeStamp);
		}
	}

	/**
	 * @version $Id$
	 */
	private class MyListSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			// does not fire, if the same row is selected again
			if (e.getValueIsAdjusting()) {
				return;
			}
			/*
			// one of these is the focused row(?)
			int i1 = e.getFirstIndex();
			int i2 = e.getLastIndex();
			*/
			if (table.getSelectedRow() > -1) {
				btnDelete.setEnabled(true);
				TaskValue task = (TaskValue) tableModel.getValueAt(table.getSelectedRow(), 4);
				try {
					ActionHub.fireActionPerformed(new ActionEvent(task, ActionEvent.ACTION_PERFORMED, Constants.ACTION_TASK_SELECT));
					viewedTaskId = task.getTaskId();					
					if (task.getStatus() == Constants.TASK_STATUS_NEW) {
						comm.setTaskViewed(task.getTaskId());
					}
				} catch (Exception exe) {
					log.error("Error selecting task", exe);
				}
				if (table.getSelectedRowCount() > 1) {
					btnDelete.setToolTipText(rb.getString("panel.task.delete.multiple"));
				} else if (table.getSelectedRowCount() == 1) {
					btnDelete.setToolTipText(rb.getString("panel.task.delete.single"));
				}
			}
		}
	}

	public void reload() {
		fillTable();
	}

	private void btnDeleteActionPerformed(ActionEvent e) {
		int delRowsCount = table.getSelectedRowCount();
		String confirmMessage = null;
		if (delRowsCount == 1) {
			confirmMessage = rb.getString("panel.task.delete.confirm.single");
		} else {
			confirmMessage = rb.getString("panel.task.delete.confirm.multiple");
		}
		int i = JOptionPane.showConfirmDialog(this.getParent().getParent(), confirmMessage, "CMS", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (i == JOptionPane.YES_OPTION) {
			int deleteIds[] = table.getSelectedRows();
			for (int j = (deleteIds.length - 1); j >= 0; j--) {
				int deleteId = deleteIds[j];
				TaskValue task = (TaskValue) tableModel.getValueAt(deleteId, 4);
				try {
					comm.removeTask(task.getTaskId());					
				} catch (Exception ex) {
					String errorMessage = null;
					if (delRowsCount == 1) {
						errorMessage = rb.getString("panel.task.delete.error.single");
					} else {
						errorMessage = rb.getString("panel.task.delete.error.multiple");
					}
					JOptionPane.showMessageDialog(this.getParent().getParent(), errorMessage + "\n" + ex.getMessage(), "CMS", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (task.getTaskId() == this.viewedTaskId) {
					ActionHub.fireActionPerformed(new ActionEvent(task, ActionEvent.ACTION_PERFORMED, Constants.ACTION_TASK_DESELECT));
					this.viewedTaskId = -1;
				}
			}
			this.reload();
			if (delRowsCount > 0) {
				String successMessage = null;
				if (delRowsCount == 1) {
					successMessage = rb.getString("panel.task.delete.success.single");
				} else {
					successMessage = rb.getString("panel.task.delete.success.multiple");
				}
				UIConstants.setStatusInfo(successMessage);				
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(Constants.ACTION_TASK_DESELECT)) {
			btnDelete.setEnabled(false);
			table.clearSelection();
		} else if (e.getActionCommand().equals(Constants.ACTION_TASK_DONE)) {
			btnDelete.doClick();
		} else if (e.getActionCommand().equals(Constants.ACTION_TASK_VIEW_COMPONENT_REFRESH)){
			TaskValue source = (TaskValue)e.getSource();
			for(int i=0;i<tableModel.getRowCount();i++){
				if(((TaskValue)tableModel.getValueAt(i, 4)).getTaskId().equals(source.getTaskId())){
					tableModel.setValueAt(source, i, 4);
				}
			}
		}
	}

}
