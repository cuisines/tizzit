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

import java.awt.BorderLayout;
import java.awt.Cursor;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.gui.controls.ReloadablePanel;
import de.juwimm.cms.gui.controls.UnloadablePanel;
import de.juwimm.cms.util.Communication;

/**
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanAdministrationRoot extends JPanel implements UnloadablePanel {
	private static Logger log = Logger.getLogger(PanAdministrationRoot.class);
	private final JTabbedPane panTab = new JTabbedPane();
	private PanUser panUser;
	private PanUnitGroupPerUser panUnitUser;
	private PanViews panView;
	private PanUnitGroup panUnit;
	private PanSitesAdministration panSitesAdmin;
	private PanHost panHost;
	private PanUserSites panUserSites;
	private PanSafeguardRealmManager panSafeguard;
	private PanSiteGroups panSiteGroups = null;
	private PanCmsResources panCmsResources;

	private final Communication comm = ((Communication) getBean(Beans.COMMUNICATION));

	public PanAdministrationRoot() {
		try {
			setDoubleBuffered(true);
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	void jbInit() throws Exception {
		this.setLayout(new BorderLayout());

		panUser = new PanUser();
		panTab.add(panUser, rb.getString("panel.admin.tab.user"));

		panUnitUser = new PanUnitGroupPerUser();
		panTab.add(panUnitUser, rb.getString("panel.admin.tab.unitUser"));

		panView = new PanViews();
		panTab.add(panView, rb.getString("panel.admin.tab.view"));

		panUnit = new PanUnitGroup();
		panTab.add(panUnit, rb.getString("panel.admin.tab.unit"));

		if ((comm.isUserInRole(UserRights.MANAGE_HOSTS)) || (comm.isUserInRole(UserRights.SITE_ROOT)) || (comm.getUser().isMasterRoot())) {
			panHost = new PanHost();
			panTab.add(panHost, rb.getString("panel.admin.tab.hosts"));
		}

		if (comm.getUser().isMasterRoot()) {
			panSitesAdmin = new PanSitesAdministration();
			panTab.add(panSitesAdmin, rb.getString("panel.admin.tab.sites"));
		} else if (comm.isUserInRole(UserRights.SITE_ROOT)) {
			panUserSites = new PanUserSites();
			panTab.add(panUserSites, rb.getString("panel.admin.tab.user.sites"));
		}

		if ((comm.isUserInRole(UserRights.MANAGE_SAFEGUARD))
		// || (comm.isUserInRole(UserRights.SITE_ROOT)) at present no customer may see this tab !!!
				|| (comm.getUser().isMasterRoot())) {
			panSafeguard = new PanSafeguardRealmManager();
			panTab.add(panSafeguard, rb.getString("panel.admin.tab.safeguard"));
		}

		if (comm.getUser().isMasterRoot()) {
			panSiteGroups = new PanSiteGroups();
			panTab.add(panSiteGroups, rb.getString("panel.admin.tab.sitegroups"));
		}

		panCmsResources = new PanCmsResources(comm);
		panTab.add(panCmsResources, rb.getString(PanCmsResources.TitleKey));

		panTab.setSelectedIndex(0);

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
								panUnitUser.unload();
							}
							panUser.reload();
							lastIndex = panTab.indexOfTab(rb.getString("panel.admin.tab.user"));
						} else if (strTabName.equals(rb.getString("panel.admin.tab.unitUser"))) {
							if (lastIndex == panTab.indexOfTab(rb.getString("panel.admin.tab.user"))) {
								panUser.unload();
							}
							panUnitUser.reload();
							lastIndex = panTab.indexOfTab(rb.getString("panel.admin.tab.unitUser"));
						} else if (strTabName.equals(rb.getString("panel.admin.tab.unit"))) {
							if (lastIndex == panTab.indexOfTab(rb.getString("panel.admin.tab.user"))) {
								panUser.unload();
							}
							panUnit.reload();
							lastIndex = panTab.indexOfTab(rb.getString("panel.admin.tab.unit"));
						} else if (strTabName.equals(rb.getString("panel.admin.tab.view"))) {
							if (lastIndex == panTab.indexOfTab(rb.getString("panel.admin.tab.user"))) {
								panUser.unload();
							}
							panView.reload();
							lastIndex = panTab.indexOfTab(rb.getString("panel.admin.tab.view"));
						} else if (strTabName.equalsIgnoreCase(rb.getString("panel.admin.tab.sites"))) {
							if (lastIndex == panTab.indexOfTab(rb.getString("panel.admin.tab.user"))) {
								panUser.unload();
							}
							panSitesAdmin.reload();
							lastIndex = panTab.indexOfTab(rb.getString("panel.admin.tab.sites"));
						} else if (strTabName.equalsIgnoreCase(rb.getString("panel.admin.tab.user.sites"))) {
							if (lastIndex == panTab.indexOfTab(rb.getString("panel.admin.tab.user"))) {
								panUser.unload();
							}
							panUserSites.reload();
							lastIndex = panTab.indexOfTab(rb.getString("panel.admin.tab.user.sites"));
						} else if (strTabName.equalsIgnoreCase(rb.getString("panel.admin.tab.hosts"))) {
							if (lastIndex == panTab.indexOfTab(rb.getString("panel.admin.tab.user"))) {
								panUser.unload();
							}
							panHost.reload();
							lastIndex = panTab.indexOfTab(rb.getString("panel.admin.tab.hosts"));
						} else if (strTabName.equalsIgnoreCase(rb.getString("panel.admin.tab.safeguard"))) {
							if (lastIndex == panTab.indexOfTab(rb.getString("panel.admin.tab.user"))) {
								panUser.unload();
							}
							panSafeguard.reload();
							lastIndex = panTab.indexOfTab(rb.getString("panel.admin.tab.safeguard"));
						} else if (strTabName.equalsIgnoreCase(rb.getString("panel.admin.tab.sitegroups"))) {
							if (lastIndex == panTab.indexOfTab(rb.getString("panel.admin.tab.user"))) {
								panUser.unload();
							}
							panSiteGroups.reload();
							lastIndex = panTab.indexOfTab(rb.getString("panel.admin.tab.sitegroups"));
						} else if (strTabName.equalsIgnoreCase(rb.getString(PanCmsResources.TitleKey))) {
							if (lastIndex == panTab.indexOfTab(rb.getString("panel.admin.tab.user"))) {
								panUser.unload();
							}
							panCmsResources.reload();
							lastIndex = panTab.indexOfTab(rb.getString(PanCmsResources.TitleKey));
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
		panUnitUser.unload();
		panView.unload();
	}

}