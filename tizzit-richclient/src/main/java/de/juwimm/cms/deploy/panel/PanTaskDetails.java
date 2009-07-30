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
package de.juwimm.cms.deploy.panel;

import static de.juwimm.cms.client.beans.Application.*;
import static de.juwimm.cms.common.Constants.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import de.juwimm.cms.Messages;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.deploy.frame.FrmRejectMessage;
import de.juwimm.cms.deploy.panel.table.EditorTableModel;
import de.juwimm.cms.gui.table.TableSorter;
import de.juwimm.cms.util.ActionHub;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.TaskValue;
import de.juwimm.cms.vo.ViewComponentValue;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanTaskDetails extends JPanel implements ActionListener {
	private static Logger log = Logger.getLogger(PanTaskDetails.class);
	private AbstractTableModel tableModel;
	private JLabel lblPath = new JLabel();
	private JButton cmdNone = new JButton();
	private JButton cmdAll = new JButton();
	private JScrollPane scrollPane;
	private JTable table = new JTable();
	private JTextField txtPath = new JTextField();
	private Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private BorderLayout borderLayout1 = new BorderLayout();
	private JPanel panButtons = new JPanel();
	private JPanel panFat = new JPanel();
	private JButton cmdSave = new JButton();
	private JButton cmdCancel = new JButton();
	private JButton btnPreview = new JButton();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private FrmRejectMessage frm = null;
	private TaskValue task = null;
	private JPanel panMessage = new JPanel();
	private Border border1;
	private TitledBorder titledBorder1;
	private GridBagLayout gridBagLayout3 = new GridBagLayout();
	private JScrollPane jScrollPane1 = new JScrollPane();
	private JTextArea txtMessage = new JTextArea();

	public PanTaskDetails() {
		try {
			jbInit();
			ActionHub.addActionListener(this);
			if (rb != null) {
				titledBorder1 = new TitledBorder(border1, rb.getString("panel.panTaskDetails.message"));
				jScrollPane1.setBorder(titledBorder1);
				cmdSave.setText(rb.getString("dialog.ok"));
				cmdCancel.setText(rb.getString("dialog.cancel"));
				btnPreview.setText(rb.getString("dialog.preview"));
				cmdAll.setText(rb.getString("dialog.selectAll"));
				cmdNone.setText(rb.getString("dialog.selectNone"));
				lblPath.setText(rb.getString("panel.panTaskDetails.path"));
			}
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	void jbInit() throws Exception {
		border1 = BorderFactory.createEtchedBorder(Color.white, new Color(165, 163, 151));
		this.setLayout(borderLayout1);

		panMessage.setBorder(null);
		panMessage.setMinimumSize(new Dimension(150, 150));
		panMessage.setPreferredSize(new Dimension(150, 150));
		panMessage.setLayout(gridBagLayout3);

		txtPath.setBackground(UIManager.getColor("Panel.background"));
		txtPath.setEditable(false);
		txtMessage.setBackground(UIManager.getColor("Panel.background"));
		txtMessage.setFont(new java.awt.Font("Dialog", 1, 12));
		txtMessage.setLineWrap(true);
		jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(panButtons, BorderLayout.SOUTH);

		cmdSave.setText("�bernehmen");
		cmdSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdSaveActionPerformed(e);
			}
		});
		cmdCancel.setText("Abbrechen");
		cmdCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdCancelActionPerformed(e);
			}
		});
		panButtons.setLayout(gridBagLayout2);
		btnPreview.setEnabled(false);
		btnPreview.setText("Vorschau");
		btnPreview.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPreviewActionPerformed(e);
			}
		});

		panButtons.add(cmdSave, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 30, 0));
		panButtons.add(cmdCancel, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 30, 0));
		panButtons.add(btnPreview, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 43, 0));

		this.add(panFat, BorderLayout.CENTER);

		table.getTableHeader().setFont(new Font("SansSerif", 0, 13));
		Dimension dim = table.getTableHeader().getPreferredSize();
		table.getTableHeader().setPreferredSize(new Dimension(dim.width, 22));
		table.setRowHeight(22);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollPane = new JScrollPane(table);

		scrollPane.setToolTipText("");

		cmdAll.setText("Alle");
		cmdAll.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdAllActionPerformed(e);
			}
		});
		cmdAll.setMinimumSize(new Dimension(101, 28));
		cmdAll.setPreferredSize(new Dimension(101, 25));
		cmdNone.setText("Keinen");
		cmdNone.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdNoneActionPerformed(e);
			}
		});
		cmdNone.setMaximumSize(new Dimension(77, 25));
		cmdNone.setMinimumSize(new Dimension(101, 28));
		cmdNone.setPreferredSize(new Dimension(101, 25));
		lblPath.setText("Pfad");
		panFat.setLayout(gridBagLayout1);
		panFat.add(scrollPane, new GridBagConstraints(0, 0, 2, 2, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 10, 0, 0), 49, 0));
		panFat.add(cmdAll, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
		panFat.add(cmdNone, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.NONE, new Insets(5, 10, 0, 10), 0, 0));
		panFat.add(txtPath, new GridBagConstraints(0, 3, 3, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 10, 10, 10), 501, 9));
		panFat.add(lblPath, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 108, 7));
		this.add(panMessage, BorderLayout.NORTH);
		panMessage.add(jScrollPane1, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		jScrollPane1.getViewport().add(txtMessage, null);
	}

	private void cmdAllActionPerformed(ActionEvent e) {
		int c = tableModel.getRowCount();

		for (int r = 0; r < c; r++) {
			tableModel.setValueAt(new Boolean(true), r, 4);
		}
	}

	private void cmdNoneActionPerformed(ActionEvent e) {
		int c = tableModel.getRowCount();

		for (int r = 0; r < c; r++) {
			tableModel.setValueAt(new Boolean(false), r, 3);
			tableModel.setValueAt(new Boolean(false), r, 4);
		}
	}

	private void cmdSaveActionPerformed(ActionEvent e) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				save();
			}
		});
		t.setPriority(Thread.NORM_PRIORITY);
		t.start();
	}

	private void cmdCancelActionPerformed(ActionEvent e) {
		btnPreview.setEnabled(false);
		ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
				Constants.ACTION_TASK_DESELECT));
	}

	private void btnPreviewActionPerformed(ActionEvent e) {
		ViewComponentValue view = getSelectedView();
		comm.showBrowserWindow(view.getViewComponentId(), true);
	}

	public void load(String message) {
		this.txtMessage.setText(message);
		this.panFat.setVisible(false);
		this.panButtons.setVisible(false);
		this.task = null;
	}

	public void load(TaskValue taskValue) {
		this.panFat.setVisible(true);
		this.panButtons.setVisible(true);
		this.task = taskValue;
		this.fillTable(task.getStatus());
		this.txtMessage.setText(task.getComment());
		this.btnPreview.setEnabled(false);
	}

	public void save() {
		ArrayList<Integer> al = new ArrayList<Integer>();
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		int count = tableModel.getRowCount();
		ViewComponentValue view;
		for (int i = 0; i < count; i++) {
			if (((Boolean) tableModel.getValueAt(i, 4)).booleanValue()) {
				try {
					view = (ViewComponentValue) tableModel.getValueAt(i, 6);
					if (view.getDeployCommand() == Constants.DEPLOY_COMMAND_DELETE
							|| view.getDeployCommand() == Constants.DEPLOY_COMMAND_REMOVE) {
						comm.removeViewComponent(view.getViewComponentId(), view.getDisplayLinkName(),
								Constants.ONLINE_STATUS_OFFLINE);
						// remove from task
						al.add(new Integer(view.getViewComponentId()));
					} else {
						view.setStatus(Constants.DEPLOY_STATUS_APPROVED);
						comm.updateStatus4ViewComponent(view);
						comm.setStatus4ViewComponentId(view.getViewComponentId(), Constants.DEPLOY_STATUS_APPROVED);
						// remove from task
						al.add(new Integer(view.getViewComponentId()));
					}
				} catch (Exception exe) {
					log.error("cant change content status", exe);
					JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb
							.getString("exception.cantChangeContentStatus")
							+ "\n" + exe.getMessage(), "CMS", JOptionPane.ERROR_MESSAGE);
				}
			} else if (((Boolean) tableModel.getValueAt(i, 3)).booleanValue()) {
				try {
					view = (ViewComponentValue) tableModel.getValueAt(i, 6);
					frm = new FrmRejectMessage(view);
					Thread t = new Thread(new Runnable() {
						public void run() {
							while (frm.getPressedButton() == FrmRejectMessage.BUTTON_NOACTION) {
								try {
									Thread.sleep(100);
								} catch (Exception exe) {
								}
							}
						}
					});
					t.setPriority(Thread.NORM_PRIORITY);
					t.start();
					t.join();

					if (frm.getPressedButton() == FrmRejectMessage.BUTTON_REJECT) {
						String msg = Messages.getString("panel.panTaskDetails.rejectMessage", view.getDisplayLinkName());
						msg += "\n" + frm.getMessage();
						comm.createTask(task.getSender().getUserName(), null, task.getUnit().getUnitId(), msg,
								Constants.TASK_REJECTED);

						view.setStatus(Constants.DEPLOY_STATUS_EDITED);
						comm.updateStatus4ViewComponent(view);
						comm.setStatus4ViewComponentId(view.getViewComponentId(), Constants.DEPLOY_STATUS_EDITED);
						// remove from task
						al.add(new Integer(view.getViewComponentId()));
					}
					frm.dispose();
				} catch (Exception exe) {
					log.error("cant change content status", exe);
					JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb
							.getString("exception.cantChangeContentStatus")
							+ "\n" + exe.getMessage(), "CMS", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		try {
			comm.removeViewComponentsFromTask(task.getTaskId(), al.toArray(new Integer[0]));
			TaskValue[] tv = comm.getAllTasks();
			if (tv != null) {
				for (int i = 0; i < tv.length; i++) {
					if (tv[i].getTaskId() == task.getTaskId()) {
						load(tv[i]);
					}
				}
			}
			fillTable(Constants.DEPLOY_STATUS_FOR_APPROVAL);
		} catch (Exception exe) {
			log.error("error removing vcs from task", exe);
		}
		if (tableModel.getRowCount() == 0) {
			ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
					Constants.ACTION_TASK_DONE));
		}
		this.setCursor(Cursor.getDefaultCursor());
	}

	private void fillTable(int status) {
		try {
			if (task != null) {
				ViewComponentValue[] viewComponents = task.getViewComponents();
				tableModel = new EditorTableModel(viewComponents);
				TableSorter sorter = new TableSorter(tableModel, table.getTableHeader());
				table.setModel(sorter);
				//m_Sorter.addMouseListenerToHeaderInTable(m_Table);
				table.getSelectionModel().addListSelectionListener(new ApproveListSelectionListener());
				if (table.getColumnModel().getColumnCount() == 5) {
					TableColumn column = table.getColumnModel().getColumn(0);
					column.setPreferredWidth(40);
					ImageCellRenderer cellrend = new ImageCellRenderer();
					column.setCellRenderer(cellrend);
					column.setResizable(false);
					column = table.getColumnModel().getColumn(1);
					column.setPreferredWidth(40);
					PageTypeCellRenderer ptypeCell = new PageTypeCellRenderer();
					column.setCellRenderer(ptypeCell);
					column.setResizable(false);
					column = table.getColumnModel().getColumn(2);
					column.setPreferredWidth(290);
					column = table.getColumnModel().getColumn(3);
					column.setPreferredWidth(100);
				}
			} else {
				tableModel = new DefaultTableModel();
			}
		} catch (Exception ex) {
		}
	}

	public ViewComponentValue getSelectedView() {
		return (ViewComponentValue) tableModel.getValueAt(table.getSelectedRow(), 6);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(Constants.ACTION_TASK_VIEW_SELECTED)) {
			btnPreview.setEnabled(true);
		}
	}
	
	/**
	 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
	 */
	private class ApproveListSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting()) return;
			if (table.getSelectedRow() > -1) {
				Thread t = new Thread(new Runnable() {
					public void run() {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								txtPath.setText((String) tableModel.getValueAt(table.getSelectedRow(), 5));
							}
						});
					}
				});
				t.setPriority(Thread.NORM_PRIORITY);
				t.start();
				ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
						Constants.ACTION_TASK_VIEW_SELECTED));
			}
		}
	}
	
	/**
	 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
	 */
	private class ImageCellRenderer extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			JPanel pan = new JPanel();
			JLabel lbl = new JLabel();
			pan.add(lbl);
			byte ival = ((Integer) value).byteValue();
			switch (ival) {
				case Constants.DEPLOY_COMMAND_ADD:
					lbl.setIcon(UIConstants.MODULE_ITERATION_CONTENT_ADD);
					break;
				case Constants.DEPLOY_COMMAND_DELETE:
				case Constants.DEPLOY_COMMAND_REMOVE:
					lbl.setIcon(UIConstants.CONTENT_DELETE_LIVE);
					break;
				case Constants.DEPLOY_COMMAND_MODIFY:
					lbl.setIcon(UIConstants.CONTENT_EDITED_LIVE);
					break;
				default:
			}
			return pan;
		}
	}
	
	/**
	 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
	 */
	private class PageTypeCellRenderer extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			JPanel pan = new JPanel();
			pan.setBackground(Color.WHITE);
			JLabel lbl = new JLabel();
			pan.add(lbl);
			byte ival = ((Integer) value).byteValue();
			switch (ival) {
				case Constants.VIEW_TYPE_CONTENT:
				case Constants.VIEW_TYPE_UNIT:
					lbl.setIcon(UIConstants.CONTENT_DEPLOYED_LIVE);
					pan.setToolTipText(rb.getString("panel.task.tttContent"));
					break;
				case Constants.VIEW_TYPE_INTERNAL_LINK:
					lbl.setIcon(UIConstants.INTERNALLINK_DEPLOYED_LIVE);
					pan.setToolTipText(rb.getString("panel.task.tttInternalLink"));
					break;
				case Constants.VIEW_TYPE_EXTERNAL_LINK:
					lbl.setIcon(UIConstants.EXTERNALLINK_DEPLOYED_LIVE);
					pan.setToolTipText(rb.getString("panel.task.tttExternalLink"));
					break;
				case Constants.VIEW_TYPE_SEPARATOR:
					lbl.setIcon(UIConstants.SEPARATOR_DEPLOYED_LIVE);
					pan.setToolTipText(rb.getString("panel.task.tttSeparator"));
					break;
				case Constants.VIEW_TYPE_SYMLINK:
					lbl.setIcon(UIConstants.SYMLINK_DEPLOYED_LIVE);
					pan.setToolTipText(rb.getString("panel.task.tttSymlink"));
					break;
				default:
			}
			return pan;
		}
	}
}