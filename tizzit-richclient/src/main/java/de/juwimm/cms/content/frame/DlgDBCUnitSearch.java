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

import de.juwimm.cms.Messages;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.content.event.SearchEvent;
import de.juwimm.cms.content.event.SearchListener;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.UnitValue;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class DlgDBCUnitSearch extends JDialog {
	private static Logger log = Logger.getLogger(DlgDBCUnitSearch.class);
	private UnitListModel unitListModel = new UnitListModel();
	private Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private SearchListener searchListener;
	private JLabel jLabel1 = new JLabel();
	private JTextField txtName = new JTextField();
	private JLabel jLabel2 = new JLabel();
	private JList lstResult = new JList();
	private JButton cmdOk = new JButton();
	private JButton cmdCancel = new JButton();
	private JButton cmdSearch = new JButton();

	public DlgDBCUnitSearch() {
		super(UIConstants.getMainFrame(), true);
		try {
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization error", exe);
		}
		this.getRootPane().setDefaultButton(cmdSearch);
	}

	private void jbInit() throws Exception {
		jLabel1.setText(Messages.getString("DlgDBCUnitSearch.unitName"));
		jLabel1.setBounds(new Rectangle(20, 14, 123, 21));
		this.getContentPane().setLayout(null);
		txtName.setBounds(new Rectangle(20, 38, 248, 20));

		txtName.addKeyListener(new MyKeyAdapter(this));
		txtName.setText("*");

		jLabel2.setText(Messages.getString("DlgDBCUnitSearch.searchResult"));
		jLabel2.setBounds(new Rectangle(19, 79, 114, 20));

		JScrollPane scroll = new JScrollPane();
		scroll.setBounds(new Rectangle(15, 95, 374, 220));
		scroll.getViewport().add(lstResult);

		cmdOk.setBounds(new Rectangle(20, 346, 138, 26));
		cmdOk.setText(Messages.getString("dialog.choose"));
		cmdOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdOkActionPerformed(e);
			}
		});
		cmdCancel.setBounds(new Rectangle(256, 347, 138, 26));
		cmdCancel.setText(Messages.getString("dialog.cancel"));
		cmdCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdCancelActionPerformed(e);
			}
		});
		cmdSearch.setBounds(new Rectangle(281, 32, 110, 26));
		cmdSearch.setText(Messages.getString("dialog.search"));
		cmdSearch.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdSearchActionPerformed(e);
			}
		});
		this.getContentPane().add(jLabel1, null);
		this.getContentPane().add(cmdCancel, null);
		this.getContentPane().add(jLabel2, null);
		this.getContentPane().add(scroll, null);
		this.getContentPane().add(cmdOk, null);
		this.getContentPane().add(txtName, null);
		this.getContentPane().add(cmdSearch, null);

		lstResult.setModel(unitListModel);

		lstResult.addListSelectionListener(new MyListSelectionListener());
		lstResult.addMouseListener(new MyMouseListener());

		cmdOk.setEnabled(false);
	}

	public void addListener(SearchListener sl) {
		this.searchListener = sl;
	}
	
	/**
	 * 
	 */
	private class MyKeyAdapter extends KeyAdapter {
		private DlgDBCUnitSearch dlgUnitSearch;

		public MyKeyAdapter(DlgDBCUnitSearch frm) {
			dlgUnitSearch = frm;
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) dlgUnitSearch.cmdSearch.doClick();
		}
	}

	private void cmdSearchActionPerformed(ActionEvent e) {
		try {
			Vector vec = comm.getUnits4Name(txtName.getText().replace('*', '%'));
			((UnitListModel) lstResult.getModel()).removeAllElements();
			((UnitListModel) lstResult.getModel()).addElements(vec);
		} catch (Exception exe) {
			log.error("search error", exe);
		}
	}

	private void cmdCancelActionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
	
	/**
	 * 
	 */
	private class UnitListModel extends DefaultListModel {
		public void addElements(Vector vec) {
			for (int i = 0; i < vec.size(); i++) {
				this.addElement(new Unit((UnitValue) vec.elementAt(i)));
			}
		}
	}
	
	/**
	 * 
	 */
	private class Unit {
		private UnitValue unit;

		public Unit(UnitValue data) {
			unit = data;
		}

		public String toString() {
			return unit.getName();
		}

		public int getUnitId() {
			return unit.getUnitId();
		}
	}
	
	/**
	 * 
	 */
	private class MyListSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			cmdOk.setEnabled(true);
		}
	}

	/**
	 * 
	 */
	private class MyMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2 && !lstResult.isSelectionEmpty()) {
				cmdOk.doClick();
			}
		}
	}

	private void cmdOkActionPerformed(ActionEvent e) {
		try {
			int unitId = ((Unit) lstResult.getSelectedValue()).getUnitId();
			SearchEvent event = new SearchEvent(comm.getUnit(unitId));
			searchListener.searchPerformed(event);
		} catch (Exception exe) {
			log.error("Error setting ok", exe);
		}
		this.setVisible(false);
	}
}