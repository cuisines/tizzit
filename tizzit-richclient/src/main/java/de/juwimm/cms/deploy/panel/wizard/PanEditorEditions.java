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

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.deploy.frame.Wizard;
import de.juwimm.cms.deploy.panel.table.EditionModel;
import de.juwimm.cms.gui.table.TableSorter;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.EditionValue;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanEditorEditions extends JPanel implements WizardPanel {
	private static Logger log = Logger.getLogger(PanEditorEditions.class);
	private EditionModel tblModel;
	private Communication communication = ((Communication) getBean(Beans.COMMUNICATION));
	private int unitId;
	private JScrollPane scp = new JScrollPane();
	private JTextArea txtEditionMessage = new JTextArea();
	private JLabel lblEditions = new JLabel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JTable tbl = new JTable();

	public PanEditorEditions() {
		try {
			tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tbl.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent me) {
					Thread t = new Thread(new SelectionRunner());
					t.setPriority(Thread.NORM_PRIORITY);
					t.start();
				}
			});
			tbl.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					Thread t = new Thread(new SelectionRunner());
					t.setPriority(Thread.NORM_PRIORITY);
					t.start();
				}
			});
			jbInit();
		} catch (Exception exe) {
			log.error("Inititialization error", exe);
		}
	}

	void jbInit() throws Exception {
		this.setLayout(gridBagLayout1);
		txtEditionMessage.setBackground(UIManager.getColor("Panel.background"));
		txtEditionMessage.setFont(new java.awt.Font("Dialog", 0, 12));
		txtEditionMessage.setBorder(BorderFactory.createEtchedBorder());
		txtEditionMessage.setText("");
		txtEditionMessage.setLineWrap(true);
		lblEditions.setText("Editionen");
		this.add(scp, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 10, 0, 10), 0, 0));
		this.add(txtEditionMessage, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(5, 10, 10, 10), 0, 56));
		this.add(lblEditions, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 10), 0, 0));
		scp.getViewport().add(tbl, null);
	}

	public void setWizard(Wizard wizard) {
	}

	public void save() {
		for (int i = 0; i < tblModel.getRowCount(); i++) {
			String val = (String) this.tblModel.getValueAt(i, 0);
			EditionValue edao = (EditionValue) tblModel.getValueAt(i, 5);
			if (val.equals("true")) {
				log.info("Setting Edition online: " + edao.getEditionId());
				try {
					communication.setActiveEdition(edao.getEditionId());
					JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb
							.getString("wizard.deploy.editionSucessfulTransmitted"), rb.getString("dialog.title"),
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception exe) {
					log.error("Error saving edition", exe);
				}
			}
			Boolean delete = (Boolean) tblModel.getValueAt(i, 4);
			if (delete.booleanValue()) {
				log.info("Deleting Edition: " + edao.getEditionId());
				try {
					communication.removeEdition(edao.getEditionId());
				} catch (Exception exe) {
					log.error("Error deleting edition", exe);
				}
			}
		}
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
		fillTable();
	}

	protected void fillTable() {
		try {
			Vector editions;
			editions = communication.getEditions(unitId);

			tblModel = new EditionModel(editions);

			TableSorter tableSorter = new TableSorter(tblModel, tbl.getTableHeader());
			tbl.setModel(tableSorter);
			//m_Sorter.addMouseListenerToHeaderInTable(tbl);

			//ONLINE OFFLINE
			TableColumn column = tbl.getColumnModel().getColumn(0);
			column.setPreferredWidth(20);
			column.setCellRenderer(new MyTableCellRenderer());
			//NAME
			column = tbl.getColumnModel().getColumn(1);
			column.setPreferredWidth(100);
			//DELETE
			column = tbl.getColumnModel().getColumn(4);
			column.setPreferredWidth(20);
		} catch (Exception exe) {
			log.error("Error filling Table", exe);
		}
	}

	/**
	 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
	 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
	 * @version $Id$
	 */
	public static class MyTableCellRenderer extends JPanel implements TableCellRenderer {
		private JRadioButton radio1 = new JRadioButton();

		public MyTableCellRenderer() {
			radio1.setHorizontalAlignment(SwingConstants.CENTER);
			this.setBackground(Color.WHITE);
			setLayout(new BorderLayout());

		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			String s = value.toString();
			if (s.equals("true")) {
				removeAll();
				add(radio1, BorderLayout.CENTER);
				radio1.setSelected(true);
			} else if (s.equals("false")) {
				removeAll();
				add(radio1, BorderLayout.CENTER);
				radio1.setSelected(false);
			}
			return this;
		}
	}

	/**
	 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
	 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
	 * @version $Id$
	 */
	protected class SelectionRunner implements Runnable {
		public void run() {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					if (tbl.getSelectedColumn() == 0) {
						String old = tblModel.getValueAt(tbl.getSelectedRow(), 0).toString();
						if (old.equals("false")) {
							tblModel.setValueAt("true", tbl.getSelectedRow(), 0);
							for (int i = 0; i < tbl.getRowCount(); i++) {
								if (i != tbl.getSelectedRow()) {
									tblModel.setValueAt("false", i, 0);
								}
							}
						}
						String delete = tblModel.getValueAt(tbl.getSelectedRow(), 4).toString();
						if (delete.equals("true")) {
							tblModel.setValueAt(new Boolean(false), tbl.getSelectedRow(), 4);
						}
					} else if (tbl.getSelectedColumn() == 4) {
						String online = tblModel.getValueAt(tbl.getSelectedRow(), 0).toString();
						if (online.equals("true")) {
							tblModel.setValueAt(new Boolean(false), tbl.getSelectedRow(), 4);
							tblModel.fireTableDataChanged();
						} else {
							Boolean currentState = (Boolean) tblModel.getValueAt(tbl.getSelectedRow(), 4);
							Boolean newState = new Boolean(!currentState.booleanValue());
							tblModel.setValueAt(newState, tbl.getSelectedRow(), 4);
						}
					}
					try {
						EditionValue dao = (EditionValue) tblModel.getValueAt(tbl.getSelectedRow(), 5);
						txtEditionMessage.setText(dao.getComment());
					} catch (Exception exe) {
					}
				}
			});
		}
	}
}