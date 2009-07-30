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

import java.awt.Rectangle;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.components.vo.DepartmentValue;
import de.juwimm.cms.content.event.SearchEvent;
import de.juwimm.cms.content.event.SearchListener;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;

/**
 * <p>Title: juwimm cms</p>
 * <p>Description: content management system</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: juwi macmillan group gmbh</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class DlgDBCDepartmentSearch extends JDialog {
	private static Logger log = Logger.getLogger(DlgDBCDepartmentSearch.class);
	private DepartmentListModel departmentsListModel = new DepartmentListModel();
	private Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private SearchListener searchListener;
	private JList lstResult = new JList();
	private JButton btnCancel = new JButton();
	private JTextField txtName = new JTextField();
	private JButton btnOk = new JButton();
	private JLabel jLabel2 = new JLabel();
	private JLabel jLabel1 = new JLabel();
	private JButton cmdSearch = new JButton();

	public DlgDBCDepartmentSearch() {
		super(UIConstants.getMainFrame(), true);
		try {
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization error", exe);
		}
		this.getRootPane().setDefaultButton(btnOk);
	}

	private void jbInit() throws Exception {
		txtName.addKeyListener(new MyKeyAdapter());
		txtName.setText("*");

		jLabel1.setBounds(new Rectangle(17, 13, 123, 21));
		jLabel1.setText("Bereichsname");
		jLabel2.setBounds(new Rectangle(15, 68, 114, 20));
		jLabel2.setText("Suchergebnis");
		btnOk.setText("Übernehmen");
		btnOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdOkActionPerformed(e);
			}
		});
		btnOk.setBounds(new Rectangle(16, 335, 138, 26));
		txtName.setBounds(new Rectangle(17, 37, 261, 20));
		btnCancel.setText("Abbrechen");
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdCancelActionPerformed(e);
			}
		});
		btnCancel.setBounds(new Rectangle(252, 336, 138, 26));

		JScrollPane scroll = new JScrollPane();
		scroll.setBounds(new Rectangle(15, 95, 374, 220));
		scroll.getViewport().add(lstResult);

		this.getContentPane().setLayout(null);
		cmdSearch.setBounds(new Rectangle(291, 31, 97, 26));
		cmdSearch.setText("Suchen");
		cmdSearch.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdSearchActionPerformed(e);
			}
		});
		this.getContentPane().add(scroll, null);
		this.getContentPane().add(jLabel2, null);
		this.getContentPane().add(btnOk, null);
		this.getContentPane().add(btnCancel, null);
		this.getContentPane().add(txtName, null);
		this.getContentPane().add(cmdSearch, null);
		this.getContentPane().add(jLabel1, null);

		lstResult.setModel(departmentsListModel);

		lstResult.addListSelectionListener(new MyListSelectionListener());
		lstResult.addMouseListener(new MyMouseListener());

		btnOk.setEnabled(false);
	}

	public void addListener(SearchListener sl) {
		this.searchListener = sl;
	}

	/**
	 * 
	 */
	private class DepartmentListModel extends DefaultListModel {
		public void addElements(Vector vec) {
			for (int i = 0; i < vec.size(); i++) {
				this.addElement(new Department((DepartmentValue) vec.elementAt(i)));
			}
		}
	}

	/**
	 * 
	 */
	private class Department {
		private DepartmentValue department;

		public Department(DepartmentValue data) {
			department = data;
		}

		public String toString() {
			return department.getName();
		}

		public long getDepartmentId() {
			return department.getDepartmentId();
		}
	}
	
	/**
	 * 
	 */
	private class MyKeyAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) cmdSearch.doClick();
		}
	}
	
	/**
	 * 
	 */
	private class MyListSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			btnOk.setEnabled(true);
		}
	}
	
	/**
	 * 
	 */
	private class MyMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2 && !lstResult.isSelectionEmpty()) {
				btnOk.doClick();
			}
		}
	}

	private void cmdOkActionPerformed(ActionEvent e) {
		try {
			long departmentId = ((Department) lstResult.getSelectedValue()).getDepartmentId();
			SearchEvent event = new SearchEvent(comm.getDepartment(departmentId));
			searchListener.searchPerformed(event);
		} catch (Exception exe) {
			log.error("Error drung search event for department", exe);
		}
		this.setVisible(false);
	}

	void cmdCancelActionPerformed(ActionEvent e) {
		setVisible(false);
	}

	void cmdSearchActionPerformed(ActionEvent e) {
		try {
			Vector vec = comm.getDepartments4Name(txtName.getText().replace('*', '%'));
			((DepartmentListModel) lstResult.getModel()).removeAllElements();
			((DepartmentListModel) lstResult.getModel()).addElements(vec);
		} catch (Exception ex) {
		}
	}
}