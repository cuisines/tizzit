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
package de.juwimm.cms.gui.views;

import static de.juwimm.cms.client.beans.Application.*;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.gui.controls.LoadableViewComponentPanel;
import de.juwimm.cms.gui.views.menuentry.PanMenuentryExternallink;
import de.juwimm.cms.util.ActionHub;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.ViewComponentValue;

/**
 * <p>Title: ConQuest </p>
 * <p>Description: Content Management System</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanExternallinkView extends JPanel implements LoadableViewComponentPanel, ActionListener {
	private static Logger log = Logger.getLogger(PanExternallinkView.class);
	private BorderLayout borderLayout1 = new BorderLayout();
	private JTabbedPane tab = new JTabbedPane();
	private PanMenuentryExternallink panExternalLink;
	private Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private JPanel jPanel1 = new JPanel();
	private JButton cmdSave = new JButton(UIConstants.BTN_SAVE);
	private JButton cmdCancel = new JButton(UIConstants.BTN_CLOSE);

	public PanExternallinkView() {
		try {
			jbInit();
			tab.add(panExternalLink, Constants.rb.getString("panel.toolLink"));

			ComponentInputMap im = new ComponentInputMap(this);
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK), "saveContent");
			this.setInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW, im);
			this.getActionMap().put("saveContent", new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					try {
						save();
					} catch (Exception exe) {
						log.error("Save Error", exe);
					}
				}
			});
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	void jbInit() throws Exception {
		this.setLayout(borderLayout1);
		panExternalLink = new PanMenuentryExternallink(comm);
		cmdSave.setText(Constants.rb.getString("dialog.save"));
		cmdSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdSaveActionPerformed(e);
			}
		});
		cmdCancel.setText(Constants.rb.getString("dialog.cancel"));
		cmdCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdCancelActionPerformed(e);
			}
		});
		jPanel1.setLayout(new GridBagLayout());
		this.add(tab, BorderLayout.CENTER);
		this.add(jPanel1, BorderLayout.SOUTH);
		jPanel1.add(cmdSave, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 30, 0));
		jPanel1.add(cmdCancel, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 30, 0));
	}

	public void save() throws Exception {
		panExternalLink.save();
	}

	public void load(ViewComponentValue value) {
		ActionHub.addActionListener(this);
		panExternalLink.load(value);
	}

	public void unload() {
		ActionHub.removeActionListener(this);
		panExternalLink.unload();
	}

	private void cmdSaveActionPerformed(ActionEvent e) {
		Constants.EDIT_CONTENT = false;
		try {
			save();
		} catch (Exception exe) {
			log.error("Save Error", exe);
		}
	}

	private void cmdCancelActionPerformed(ActionEvent e) {
		Constants.EDIT_CONTENT = false;
		ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
				Constants.ACTION_TREE_DESELECT));
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals(Constants.ACTION_SAVE)) {
			try {
				save();
			} catch (Exception exe) {
				log.error("Save Error", exe);
			}
		}
	}
}
