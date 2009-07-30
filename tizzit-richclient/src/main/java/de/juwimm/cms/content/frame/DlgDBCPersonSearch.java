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
package de.juwimm.cms.content.frame;

import static de.juwimm.cms.client.beans.Application.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import de.juwimm.cms.Messages;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.components.vo.PersonValue;
import de.juwimm.cms.content.event.SearchEvent;
import de.juwimm.cms.content.event.SearchListener;
import de.juwimm.cms.content.frame.table.PersonTableModel;
import de.juwimm.cms.gui.table.TableSorter;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class DlgDBCPersonSearch extends JDialog {
	private static Logger log = Logger.getLogger(DlgDBCPersonSearch.class);
	private Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private SearchListener searchListener;
	private PersonTableModel personTableModel = new PersonTableModel();
	private JTextField txtLastName = new JTextField();
	private JButton btnSearch = new JButton();
	private JTextField txtFirstName = new JTextField();
	private JLabel jLabel3 = new JLabel();
	private JLabel jLabel2 = new JLabel();
	private JButton btnOk = new JButton();
	private JButton btnCancel = new JButton();
	private JScrollPane jScrollPane1 = new JScrollPane();
	private JLabel jLabel1 = new JLabel();
	private JTable table = new JTable();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JLabel lblRoleType = new JLabel(); 
	private boolean anyRoleTypesFree = false;

	public DlgDBCPersonSearch() {
		super(UIConstants.getMainFrame(), true);
		try {
			jbInit(); 
			this.lblRoleType.setVisible(false);
		} catch (Exception exe) {
			log.error("Initialization error", exe);
		}
		this.getRootPane().setDefaultButton(btnOk);
	}

	/**
	 * This Constructor should be used to add a link to a exisiting component, not to search
	 */
	public DlgDBCPersonSearch(int unitId) {
		super(UIConstants.getMainFrame(), true);
		try {
			jbInit(); 
		} catch (Exception exe) {
			log.error("Initialization error", exe);
		}
	}

	public boolean isAnyRoleTypeFree() {
		return anyRoleTypesFree;
	}

	private void jbInit() throws Exception {
		this.getContentPane().setLayout(gridBagLayout1);
		btnSearch.setText(Messages.getString("dialog.search"));
		btnSearch.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSearchActionPerformed(e);
			}
		});
		jLabel3.setText(Messages.getString("DlgDBCPersonSearch.firstname"));
		jLabel2.setText(Messages.getString("DlgDBCPersonSearch.lastname"));
		btnOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnOkActionPerformed(e);
			}
		});
		btnOk.setText(Messages.getString("dialog.choose"));
		btnCancel.setText(Messages.getString("dialog.cancel"));
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCancelActionPerformed(e);
			}
		});
		txtFirstName.setText("*");
		txtFirstName.addKeyListener(new MyKeyAdapter());
		txtLastName.setText("*");
		txtLastName.addKeyListener(new MyKeyAdapter());
		jScrollPane1.getViewport().setBackground(Color.white);
		jLabel1.setText(Messages.getString("DlgDBCPersonSearch.searchResult"));
		this.setResizable(true);
		this.setTitle(Messages.getString("DlgDBCPersonSearch.persons"));

		lblRoleType.setText(Messages.getString("DlgDBCPersonSearch.insertPersonAs"));
		this.getContentPane().add(
				txtFirstName,
				new GridBagConstraints(1, 0, 3, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
						new Insets(10, 0, 0, 0), 256, 1));
		this.getContentPane().add(
				txtLastName,
				new GridBagConstraints(1, 1, 3, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
						new Insets(5, 0, 0, 0), 256, 1));
		this.getContentPane().add(
				btnSearch,
				new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(10, 5, 0, 10), 23, 1));
		this.getContentPane().add(
				btnOk,
				new GridBagConstraints(0, 6, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(10, 10, 10, 0), 34, 1));
		this.getContentPane().add(
				btnCancel,
				new GridBagConstraints(2, 5, 3, 2, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(10, 0, 10, 10), 40, 1));
		this.getContentPane().add(
				jLabel1,
				new GridBagConstraints(0, 2, 5, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
						new Insets(5, 10, 0, 6), 56, 10));
		this.getContentPane().add(
				jScrollPane1,
				new GridBagConstraints(0, 3, 5, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 10, 0, 10), 0, 0));
		this.getContentPane().add(
				jLabel3,
				new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(10, 10, 0, 16), 12, 7));
		this.getContentPane().add(
				jLabel2,
				new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(5, 10, 0, 0), 21, 7));
		this.getContentPane().add(
				lblRoleType,
				new GridBagConstraints(0, 4, 2, 2, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(0, 10, 0, 10), 0, 0));
		jScrollPane1.getViewport().add(table, null);

		table.getTableHeader().setFont(new Font("SansSerif", 0, 13));
		Dimension dim = table.getTableHeader().getPreferredSize();
		table.getTableHeader().setPreferredSize(new Dimension(dim.width, 22));
		table.setRowHeight(22);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		TableSorter tableSorter = new TableSorter(personTableModel, table.getTableHeader());
		table.setModel(tableSorter);

		//tableSorter.addMouseListenerToHeaderInTable(table);

		table.getSelectionModel().addListSelectionListener(new MyListSelectionListener(this));

		table.addMouseListener(new MyMouseAdapter(this));

		btnOk.setEnabled(false);
	}

	void btnSearchActionPerformed(ActionEvent e) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				String firstName = txtFirstName.getText().replace('*', '%');
				String lastName = txtLastName.getText().replace('*', '%');
				try {
					setCursor(new Cursor(Cursor.WAIT_CURSOR));
					personTableModel.removeRows();
					personTableModel.fireTableDataChanged();
					personTableModel.addRows(comm.getPerson4Name(firstName, lastName));
				} catch (Exception ex) {
				} finally {
					setCursor(Cursor.getDefaultCursor());
				}
			}
		});
		t.setPriority(Thread.NORM_PRIORITY);
		t.start();
	}

	void btnOkActionPerformed(ActionEvent e) {
		try {
			PersonValue dao = personTableModel.getPersonAtRow(table.getSelectedRow());
			SearchEvent event = new SearchEvent(comm.getPerson(dao.getPersonId()));
			searchListener.searchPerformed(event);
		} catch (Exception exe) {
			log.error("Error getting person", exe);
		}
		this.setVisible(false);
	}

	void btnCancelActionPerformed(ActionEvent e) {
		this.setVisible(false);
	}

	public void setSearchListener(SearchListener searchListener) {
		this.searchListener = searchListener;
	}

	/**
	 *
	 */
	private class MyMouseAdapter extends MouseAdapter {
		private DlgDBCPersonSearch dlgPersonSearch;

		public MyMouseAdapter(DlgDBCPersonSearch frm) {
			dlgPersonSearch = frm;
		}

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2 && !table.getSelectionModel().isSelectionEmpty()) {
				btnOk.doClick();
			}
		}
	}

	/**
	 * 
	 */
	private class MyKeyAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (txtFirstName.getText().equals("")) {
					txtFirstName.requestFocus();
				} else if (txtLastName.getText().equals("")) {
					txtLastName.requestFocus();
				} else {
					btnSearch.doClick();
				}
			}
		}
	}

	/**
	 * 
	 */
	private class MyListSelectionListener implements ListSelectionListener {
		private DlgDBCPersonSearch dlgPersonSearch;

		public MyListSelectionListener(DlgDBCPersonSearch frm) {
			dlgPersonSearch = frm;
		}

		public void valueChanged(ListSelectionEvent e) {
			btnOk.setEnabled(true);
		}
	}
}