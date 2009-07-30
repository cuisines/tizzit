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
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.*;

import org.apache.log4j.Logger;

import de.juwimm.cms.gui.table.ModifiedPagesTableModel;
import de.juwimm.cms.gui.table.TableSorter;
import de.juwimm.cms.vo.ContentValue;

/**
 * Panel with list of all not checked-in pages on exiting client
 * 
 * @author <a href="carsten.schalm@juwimm.com">Carsten Schalm</a>
 * @company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class PanCheckInPages extends JPanel {
	private static Logger log = Logger.getLogger(PanCheckInPages.class);
	private JScrollPane scrollPane = new JScrollPane();
	private JTextArea taMessage = new JTextArea();
	private JTable tblPages = new JTable();
	private ModifiedPagesTableModel tblPagesModel = null;
	private TableSorter tblPagesSorter = null;

	public PanCheckInPages(ArrayList<ContentValue> pageList) {
		try {
			tblPagesModel = new ModifiedPagesTableModel();
			tblPagesSorter = new TableSorter(tblPagesModel, tblPages.getTableHeader());
			tblPagesModel.addRows(pageList);
			tblPages.getSelectionModel().clearSelection();
			tblPages.setModel(tblPagesSorter);
			jbInit();
			tblPages.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			taMessage.setText(rb.getString("PanCheckInPages.explaination"));
		} catch (Exception exe) {
			log.error("Initialization error", exe);
		}
	}

	private void jbInit() throws Exception {
		taMessage.setLineWrap(true);
		taMessage.setWrapStyleWord(true);
		taMessage.setEditable(false);
		taMessage.setMargin(new Insets(20, 20, 20, 20));
		this.setLayout(new BorderLayout());
		this.scrollPane.getViewport().add(this.tblPages);
		this.add(taMessage, BorderLayout.NORTH);
		this.add(scrollPane, BorderLayout.CENTER);
	}

	public ModifiedPagesTableModel getModel() {
		return tblPagesModel;
	}

}
