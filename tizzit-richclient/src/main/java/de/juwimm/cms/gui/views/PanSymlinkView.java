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

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

import javax.swing.*;

import org.apache.log4j.Logger;

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.gui.controls.LoadableViewComponentPanel;
import de.juwimm.cms.gui.views.menuentry.PanMenuentryInternallink;
import de.juwimm.cms.util.ActionHub;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.ViewComponentValue;

/**
 * <p>Title: ConQuest</p>
 * <p>Description: Content Management System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanSymlinkView extends JPanel implements LoadableViewComponentPanel, ActionListener {
	private static Logger log = Logger.getLogger(PanContentView.class);
	private ResourceBundle rb = Constants.rb;
	private BorderLayout borderLayout1 = new BorderLayout();
	private JTabbedPane tab = new JTabbedPane();
	private JPanel jPanel1 = new JPanel();
	private PanMenuentryInternallink viewJump = new PanMenuentryInternallink(true);
	private JButton btnSave = new JButton(UIConstants.BTN_SAVE);
	private JButton btnCancel = new JButton(UIConstants.BTN_CLOSE);
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	public PanSymlinkView() {
		try {
			jbInit();
			this.btnCancel.setText(rb.getString("dialog.cancel"));
			this.btnSave.setText(rb.getString("dialog.save"));
			tab.add("Symlink", viewJump);

			ComponentInputMap im = new ComponentInputMap(this);
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK), "saveContent");
			this.setInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW, im);
			this.getActionMap().put("saveContent", new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					try {
						save();
					} catch (Exception exe) {
						log.error("Save error", exe);
					}
				}
			});
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	void jbInit() throws Exception {
		this.setLayout(borderLayout1);

		btnSave.setText("Speichern");
		btnSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					save();
				} catch (Exception exe) {
				}
			}
		});
		btnCancel.setText("Abbrechen");
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdCancelActionPerformed(e);
			}
		});
		jPanel1.setLayout(gridBagLayout1);
		this.add(tab, BorderLayout.CENTER);
		this.add(jPanel1, BorderLayout.SOUTH);
		jPanel1.add(btnSave, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 30, 0));
		jPanel1.add(btnCancel, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 30, 0));
	}

	public void save() throws Exception {
		Constants.EDIT_CONTENT = false;
		viewJump.save();
	}

	public void load(ViewComponentValue viewComponent) {
		ActionHub.addActionListener(this);
		viewJump.load(viewComponent);
	}

	public void unload() {
		ActionHub.removeActionListener(this);
		viewJump.unload();
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
				log.error("Error saving symlink", exe);
			}
		}
	}
}