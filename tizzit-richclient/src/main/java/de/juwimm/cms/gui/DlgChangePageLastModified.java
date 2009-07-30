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

import static de.juwimm.cms.common.Constants.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

import org.apache.log4j.Logger;

import de.juwimm.cms.content.frame.DlgSimpleDateTime;
import de.juwimm.cms.gui.table.ChangePageLastModifiedTableModel;
import de.juwimm.cms.gui.table.TableSorter;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.ViewComponentValue;

/**
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class DlgChangePageLastModified extends JDialog {
	private static Logger log = Logger.getLogger(DlgChangePageLastModified.class);
	private SimpleDateFormat sdf = new SimpleDateFormat(rb.getString("General.ShortDateTimeFormat"));
	private JPanel jContentPane = null;
	private JPanel panButtons = null;
	private JButton btnOk = null;
	private JButton btnCancel = null;
	private JTextArea taMessage = null;
	private JScrollPane spPages = null;
	private JTable tblPages = null;
	private TableSorter tblPagesSorter = null;
	private ChangePageLastModifiedTableModel tblPagesModel = null;
	private int buttonState = JOptionPane.CANCEL_OPTION;

	/**
	 * This is the default constructor
	 */
	public DlgChangePageLastModified(ArrayList<ViewComponentValue> pageList) {
		super(UIConstants.getMainFrame(), true);
		this.initialize();
		this.tblPagesModel = new ChangePageLastModifiedTableModel(pageList);
		this.tblPagesSorter = new TableSorter(this.tblPagesModel, this.tblPages.getTableHeader());
		this.tblPages.getSelectionModel().clearSelection();
		this.tblPages.setModel(this.tblPagesSorter);
		this.tblPages.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setLocationRelativeTo(UIConstants.getMainFrame());
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(800, 200);
		this.setTitle(rb.getString("DlgChangePageLastModified.title"));
		this.setContentPane(this.getJContentPane());
		this.getRootPane().setDefaultButton(this.getBtnOk());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (this.jContentPane == null) {
			this.jContentPane = new JPanel();
			this.jContentPane.setLayout(new BorderLayout());
			this.jContentPane.add(this.getPanButtons(), java.awt.BorderLayout.SOUTH);
			this.jContentPane.add(this.getTaMessage(), java.awt.BorderLayout.NORTH);
			this.jContentPane.add(this.getSpPages(), java.awt.BorderLayout.CENTER);
		}
		return this.jContentPane;
	}

	/**
	 * This method initializes panButtons	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanButtons() {
		if (this.panButtons == null) {
			this.panButtons = new JPanel();
			this.panButtons.setLayout(new BorderLayout());
			this.panButtons.add(this.getBtnCancel(), java.awt.BorderLayout.EAST);
			this.panButtons.add(this.getBtnOk(), java.awt.BorderLayout.WEST);
		}
		return this.panButtons;
	}

	/**
	 * This method initializes btnOk	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnOk() {
		if (this.btnOk == null) {
			this.btnOk = new JButton();
			this.btnOk.setText(rb.getString("dialog.ok"));
			this.btnOk.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					buttonState = JOptionPane.OK_OPTION;
					setVisible(false);
				}
				
			});
		}
		return this.btnOk;
	}

	/**
	 * This method initializes btnCancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnCancel() {
		if (this.btnCancel == null) {
			this.btnCancel = new JButton();
			this.btnCancel.setText(rb.getString("dialog.cancel"));
			this.btnCancel.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					buttonState = JOptionPane.CANCEL_OPTION;
					setVisible(false);
				}
				
			});
		}
		return this.btnCancel;
	}

	/**
	 * This method initializes taMessage	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getTaMessage() {
		if (this.taMessage == null) {
			this.taMessage = new JTextArea();
			this.taMessage.setLineWrap(true);
			this.taMessage.setWrapStyleWord(true);
			this.taMessage.setEditable(false);
			this.taMessage.setMargin(new Insets(20, 20, 20, 20));
			this.taMessage.setText(rb.getString("DlgChangePageLastModified.introText"));
			this.taMessage.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 13));
		}
		return this.taMessage;
	}

	/**
	 * This method initializes spPages	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getSpPages() {
		if (this.spPages == null) {
			this.spPages = new JScrollPane();
			this.spPages.setViewportView(this.getTblPages());
		}
		return this.spPages;
	}

	/**
	 * This method initializes tblPages	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getTblPages() {
		if (this.tblPages == null) {
			this.tblPages = new JTable();
			this.tblPages.setDefaultRenderer(Date.class, new DateRenderer());
			this.tblPages.setDefaultEditor(Date.class, new DateTableCellEditor());
		}
		return this.tblPages;
	}

	public int showDialog() {
		this.setVisible(true);
		return this.buttonState;
	}

	public Collection<ViewComponentValue> getSelectedPages() {
		return this.tblPagesModel.getSelectedPages();
	}

	private class DateRenderer extends DefaultTableCellRenderer {
	    public DateRenderer() { super(); }

	    public void setValue(Object value) {
	        super.setText((value == null) ? "" : sdf.format((Date) value));
	    }
	}

	private class DateTableCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
		private JLabel label = new JLabel();
		private Date date = null;
		private DlgSimpleDateTime pan = new DlgSimpleDateTime();
		
		public DateTableCellEditor() {
			this.pan.setDatePurgeable(false);
			this.pan.addOkListner(this);
			this.label.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					// the user has clicked the cell, so bring up the dialog.
		            label.setText((date == null) ? "" : sdf.format(date));
		            pan.setDate(date);
		            pan.setLocationRelativeTo(UIConstants.getMainFrame());
		            pan.setVisible(true);

		            // make the renderer reappear.
		            fireEditingStopped();
				}
				
			});
		}
		
		public Object getCellEditorValue() {
			return this.date;
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			this.date = (Date) value;
			this.label.setText((value == null) ? "" : sdf.format((Date) value));
			return this.label;
		}

		public void actionPerformed(ActionEvent e) {
			// user pressed dialog's "OK" button
			this.date = this.pan.getDate();
			if (log.isDebugEnabled()) log.debug("new DateTime: " + date);
		}
		
	}

} //  @jve:decl-index=0:visual-constraint="284,266"
