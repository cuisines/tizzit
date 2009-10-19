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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ComponentInputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;
import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.common.icon.ImageWrapperResizableIcon;

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.gui.controls.LoadableViewComponentPanel;
import de.juwimm.cms.gui.ribbon.CommandButton;
import de.juwimm.cms.gui.views.menuentry.PanMenuentry;
import de.juwimm.cms.gui.views.menuentry.PanMenuentryInternallink;
import de.juwimm.cms.util.ActionHub;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.ViewComponentValue;

/**
 * <p>Title: Tizzit </p>
 * <p>Description: Content Management System</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanInternallinkView extends JPanel implements LoadableViewComponentPanel, ActionListener {
	private static Logger log = Logger.getLogger(PanInternallinkView.class);
	private final BorderLayout borderLayout1 = new BorderLayout();
	private final JTabbedPane tab = new JTabbedPane();
	private final PanMenuentryInternallink panViewJump = new PanMenuentryInternallink(false);
	private final JPanel jPanel1 = new JPanel();
	private JCommandButton cmdCancel;
	private JCommandButton cmdSave;

	public PanInternallinkView() {
		try {
			jbInit();
			tab.add(panViewJump, Constants.rb.getString("panel.toolJump"));

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
		this.cmdCancel = new CommandButton(Constants.rb.getString("dialog.cancel"), ImageWrapperResizableIcon.getIcon(UIConstants.RIBBON_CLOSE.getImage(), new Dimension(32, 32)));
		this.cmdSave = new CommandButton(Constants.rb.getString("dialog.save"), ImageWrapperResizableIcon.getIcon(UIConstants.RIBBON_SAVE.getImage(), new Dimension(32, 32)));
		cmdCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdCancelActionPerformed(e);
			}
		});
		cmdSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdSaveActionPerformed(e);
			}
		});
		jPanel1.setLayout(new GridBagLayout());
		this.add(tab, BorderLayout.CENTER);
		this.add(jPanel1, BorderLayout.SOUTH);
		jPanel1.add(cmdCancel, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 30, 0));
		jPanel1.add(cmdSave, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 30, 0));
	}

	public void setPanel(PanMenuentry panMenuentry) {
	}

	public void save() throws Exception {
		panViewJump.save();
	}

	public void load(ViewComponentValue viewComponent) {
		ActionHub.addActionListener(this);
		panViewJump.load(viewComponent);
	}

	public void unload() {
		ActionHub.removeActionListener(this);
		panViewJump.unload();
	}

	void cmdSaveActionPerformed(ActionEvent e) {
		Constants.EDIT_CONTENT = false;
		try {
			save();
		} catch (Exception exe) {
			log.error("Save Error", exe);
		}
	}

	void cmdCancelActionPerformed(ActionEvent e) {
		Constants.EDIT_CONTENT = false;
		ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Constants.ACTION_TREE_DESELECT));
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
