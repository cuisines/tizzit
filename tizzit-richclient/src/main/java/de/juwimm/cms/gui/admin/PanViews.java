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
package de.juwimm.cms.gui.admin;

import static de.juwimm.cms.client.beans.Application.getBean;
import static de.juwimm.cms.common.Constants.rb;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.deploy.panel.wizard.PanEditorEditions;
import de.juwimm.cms.gui.controls.ReloadablePanel;
import de.juwimm.cms.gui.table.ViewTableModel;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.ViewDocumentValue;

/**
 * <p>Title: juwimm cms</p>
 * <p>Description: content management system</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: juwi macmillan group gmbh</p>
 * @author Dirk Bogun
 * @version $Id$
 */
public class PanViews extends JPanel implements ReloadablePanel {
	private static Logger log = Logger.getLogger(PanViews.class);
	private ViewTableModel tblModel = null;
	private Hashtable<Long, Long> viewsDeleted;
	private Hashtable<Long, Long> viewsNew;
	private final JButton cmdSave = new JButton();
	private JScrollPane jScrollPane1;
	private final JTable tblView = new JTable();
	private final Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private final JButton cmdAdd = new JButton();
	private final JLabel jLabel3 = new JLabel();
	private final JComboBox cbxViewType = new JComboBox();
	private final JComboBox cbxLanguage = new JComboBox();
	private final JLabel jLabel4 = new JLabel();
	private final JButton cmdDelete = new JButton();
	private final GridBagLayout gridBagLayout1 = new GridBagLayout();
	private final JPanel panAddEntry = new JPanel();
	private final GridBagLayout gridBagLayout2 = new GridBagLayout();

	public PanViews() {
		try {
			setDoubleBuffered(true);
			jbInit();
			//this ones are hard coded... :(
			cbxLanguage.addItem("en");
			cbxLanguage.addItem("de");
			cbxLanguage.addItem("es");
			cbxLanguage.addItem("fr");
			cbxLanguage.addItem("it");
			cbxLanguage.addItem("ru");
			cbxLanguage.addItem("tr");
			cbxLanguage.addItem("nl"); //Holland
			cbxLanguage.addItem("dk"); //Dänemark
			cbxLanguage.addItem("se"); //Schweden
			cbxLanguage.addItem("sa"); //Saudi-Arabien
			cbxLanguage.addItem("pl"); //Polen
			cbxLanguage.addItem("uae"); // Vereinigte Arabische Emirate

			cbxViewType.addItem("browser");
			cbxViewType.addItem("WAP");

			tblView.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent me) {
					itemSelected();
				}
			});
			tblView.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					itemSelected();
				}
			});
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	void jbInit() throws Exception {
		tblView.getTableHeader().setFont(new Font("SansSerif", 0, 13));
		Dimension dim = tblView.getTableHeader().getPreferredSize();
		tblView.getTableHeader().setPreferredSize(new Dimension(dim.width, 22));
		tblView.setRowHeight(22);
		tblView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		jScrollPane1 = new JScrollPane(tblView);

		this.setLayout(gridBagLayout1);
		cmdSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultCellEditor ed = (DefaultCellEditor) tblView.getCellEditor();
				if (ed != null) {
					int r = tblView.getEditingRow();
					int c = tblView.getEditingColumn();
					tblModel.setValueAt(ed.getCellEditorValue(), r, c);
					ed.stopCellEditing();
				}
				tblView.getSelectionModel().clearSelection();
				save();
			}
		});
		cmdAdd.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdAddActionPerformed(e);
			}
		});
		cmdSave.setText(rb.getString("dialog.save"));
		cmdAdd.setText(rb.getString("dialog.add"));
		cmdDelete.setText(rb.getString("dialog.delete"));
		jLabel3.setText(rb.getString("panel.panelCmsViews.viewType"));
		jLabel4.setText(rb.getString("panel.panelCmsViews.viewLanguage"));

		cmdDelete.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdDeleteActionPerformed(e);
			}
		});

		panAddEntry.setBorder(BorderFactory.createEtchedBorder());
		panAddEntry.setLayout(gridBagLayout2);
		this.add(jScrollPane1, new GridBagConstraints(0, 0, 2, 2, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 0, 0), -69, -181));
		this.add(cmdSave, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 10, 10, 10), 0, 0));
		this.add(cmdDelete, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));
		this.add(panAddEntry, new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 0), 0, 0));
		panAddEntry.add(jLabel3, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		panAddEntry.add(cbxViewType, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));
		panAddEntry.add(jLabel4, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		panAddEntry.add(cmdAdd, new GridBagConstraints(1, 3, 1, 1, 0.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		panAddEntry.add(cbxLanguage, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));

		viewsDeleted = new Hashtable<Long, Long>(3);
		viewsNew = new Hashtable<Long, Long>(3);
	}

	public void reload() {
		try {
			fillTable();
		} catch (Exception exe) {
			log.error("Reload Error", exe);
		}
	}

	public void unload() {
		try {
			if (viewsDeleted.size() != 0 || viewsNew.size() != 0) {
				int i = JOptionPane.showConfirmDialog(this, rb.getString("dialog.wantToSave"), rb.getString("dialog.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

				if (i == JOptionPane.YES_OPTION) {
					save();
				} else {
					viewsDeleted = new Hashtable<Long, Long>(3);
					viewsNew = new Hashtable<Long, Long>(3);
				}
			}
		} catch (Exception ex) {
		}
	}

	private void fillTable() {
		try {
			ViewDocumentValue[] views = comm.getViewDocuments();
			tblModel = new ViewTableModel(views);
			tblView.setModel(tblModel);

			TableColumn column = tblView.getColumnModel().getColumn(2);
			column.setPreferredWidth(20);
			column.setCellRenderer(new PanEditorEditions.MyTableCellRenderer());
		} catch (Exception exe) {
			log.error("Error filling table", exe);
		}
	}

	private void itemSelected() {
		if (tblView.getSelectedColumn() == 2) {
			String old = tblModel.getValueAt(tblView.getSelectedRow(), 2).toString();
			if (old.equals("false")) {
				tblModel.setValueAt("true", tblView.getSelectedRow(), 2);
				for (int i = 0; i < tblView.getRowCount(); i++) {
					if (i != tblView.getSelectedRow()) {
						tblModel.setValueAt("false", i, 2);
					}
				}
			}
		}
	}

	public void save() {
		ViewDocumentValue vcd = null;
		if (viewsDeleted.size() > 0) {
			for (Enumeration enume = viewsDeleted.elements(); enume.hasMoreElements();) {
				Long viewDocumentId = (Long) enume.nextElement();

				try {
					comm.removeViewDocument(viewDocumentId.intValue());
					viewsDeleted.remove(viewDocumentId);
					UIConstants.setStatusInfo(rb.getString("panel.panelCmsViews.succDeleted"));
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(UIConstants.getMainFrame(), ex.getMessage(), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		if (viewsNew.size() > 0) {
			for (Enumeration enume = viewsNew.elements(); enume.hasMoreElements();) {
				Long id = (Long) enume.nextElement();
				vcd = getDao4Id(id);

				try {
					tblModel.changeViewId(id, comm.createViewDocument(vcd).getViewDocumentId());
					viewsNew.remove(id);
					UIConstants.setStatusInfo(rb.getString("panel.panelCmsViews.succAdded"));
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(UIConstants.getMainFrame(), ex.getMessage(), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		try {
			Long vdDefault = tblModel.getDefault();
			comm.setDefaultViewDocument(vdDefault.intValue());
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(UIConstants.getMainFrame(), ex.getMessage(), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
		}
	}

	private ViewDocumentValue getDao4Id(Long viewDocumentId) {
		ViewDocumentValue vd = new ViewDocumentValue();

		int len = tblModel.getRowCount();
		Object[] obj = null;

		for (int i = 0; i < len; i++) {
			if (viewDocumentId.equals(tblModel.getValueAt(i, 3))) {
				obj = tblModel.getRow(i);
				break;
			}
		}
		vd.setLanguage((String) obj[1]);
		vd.setViewType((String) obj[0]);
		return vd;
	}

	void cmdAddActionPerformed(ActionEvent e) {
		String language = (String) cbxLanguage.getSelectedItem();
		String viewType = (String) cbxViewType.getSelectedItem();

		for (int i = 0; i < tblView.getRowCount(); i++) {
			if (((String) tblModel.getValueAt(i, 0)).equals(viewType) && ((String) tblModel.getValueAt(i, 1)).equals(language)) {
				JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("panel.panelCmsViews.combAlreadyAvailable"), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		Object[] obj = new Object[4];
		Long id = new Long(System.currentTimeMillis());
		obj[3] = id;
		obj[2] = "false";
		obj[1] = language;
		obj[0] = viewType;
		viewsNew.put(id, id);
		tblModel.addRow(obj);
		int i = tblView.getModel().getRowCount() - 1;
		tblView.getSelectionModel().setSelectionInterval(i, i);
		tblView.requestFocus();
	}

	void cmdDeleteActionPerformed(ActionEvent e) {
		int r = tblView.getSelectedRow();
		Long id = (Long) tblModel.getValueAt(r, 3);
		Object std = tblModel.getValueAt(r, 2);
		if (r >= 0) {
			if (viewsNew.get(id) != null) {
				viewsNew.remove(id);
				tblModel.removeRow(r);
				return;
			}
			viewsDeleted.put(id, id);
			tblModel.removeRow(r);
		}
		if (std.toString().equalsIgnoreCase("true") && tblModel.getRowCount() > 0) {
			tblModel.setValueAt(std, 0, 2);
		}
	}
}