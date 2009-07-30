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

import static de.juwimm.cms.common.Constants.*;

import java.awt.BorderLayout;
import java.awt.Cursor;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import de.juwimm.cms.gui.controls.ReloadablePanel;
import de.juwimm.cms.gui.controls.UnloadablePanel;
import de.juwimm.cms.util.ActionHub;

/**
 * <p>Title: juwimm cms</p>
 * <p>Description: content management system</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: juwi macmillan group gmbh</p>
 * @author Dirk Bogun
 * @version $Id$
 */
public class PanAdministrationAdmin extends JPanel implements UnloadablePanel {
	private static Logger log = Logger.getLogger(PanAdministrationAdmin.class);
	private JTabbedPane panTab = new JTabbedPane();
	private PanUser panUser;
	private PanUnitGroupPerUser panUserUnits = null;

	public PanAdministrationAdmin() {
		try {
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	void jbInit() throws Exception {
		this.setLayout(new BorderLayout());

		panUser = new PanUser();
		if (panUserUnits != null) {
			ActionHub.removeActionListener(panUserUnits);
		}
		panUserUnits = new PanUnitGroupPerUser();
		ActionHub.addActionListener(panUserUnits);

		panTab.add(panUser, rb.getString("panel.admin.tab.user"));
		panTab.add(panUserUnits, rb.getString("panel.admin.tab.unitUser"));

		panTab.setSelectedIndex(panTab.indexOfTab(rb.getString("panel.admin.tab.user")));

		/* Change the Tab in panel */
		panTab.addChangeListener(new ChangeListener() {
			//int lastIndex = 0;
			private int lastIndex = panTab.indexOfTab(rb.getString("panel.admin.tab.user"));

			public void stateChanged(ChangeEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						setCursor(new Cursor(Cursor.WAIT_CURSOR));
						String strTabName = panTab.getTitleAt(panTab.getSelectedIndex());
						if (strTabName.equals(rb.getString("panel.admin.tab.user"))) {
							if (lastIndex == panTab.indexOfTab(rb.getString("panel.admin.tab.unitUser"))) {
								panUserUnits.unload();
							}
							panUser.reload();
							lastIndex = panTab.indexOfTab(rb.getString("panel.admin.tab.user"));
						} else if (strTabName.equals(rb.getString("panel.admin.tab.unitUser"))) {
							if (lastIndex == panTab.indexOfTab(rb.getString("panel.admin.tab.user"))) {
								panUser.unload();
							}
							panUserUnits.reload();
							lastIndex = panTab.indexOfTab(rb.getString("panel.admin.tab.unitUser"));
						}
						setCursor(Cursor.getDefaultCursor());
					}
				});
			}
		});
		this.add(panTab, BorderLayout.CENTER);
	}

	public void reload() throws Exception {
		ReloadablePanel rpan = (ReloadablePanel) panTab.getSelectedComponent();
		rpan.reload();
	}

	public void unload() {
		panUser.unload();
		panUserUnits.unload();
	}

}