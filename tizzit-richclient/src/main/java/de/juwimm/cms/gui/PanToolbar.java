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

import static de.juwimm.cms.client.beans.Application.*;
import static de.juwimm.cms.common.Constants.*;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.gui.controls.ToolbarButton;
import de.juwimm.cms.gui.event.ViewComponentEvent;
import de.juwimm.cms.gui.tree.PageNode;
import de.juwimm.cms.gui.tree.TreeNode;
import de.juwimm.cms.util.*;

/**
 * <p>Title: ConQuest</p>
 * <p>Description: Enterprise Content Management<</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanToolbar extends JToolBar implements ActionListener {
	private static Logger log = Logger.getLogger(PanToolbar.class);
	private Communication communication;

	private ToolbarButton cmdSend2Editor = new ToolbarButton();

	public PanToolbar(Communication comm) {
		communication = comm;
		try {
			this.setDoubleBuffered(true);
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	void jbInit() throws Exception {
		loadImages();
		this.setFloatable(false);
	}

	private void loadImages() {
		cmdSend2Editor.setIcon(UIConstants.ACTION_DEPLOY);
		cmdSend2Editor.setToolTipText(rb.getString("actions.ACTION_SEND2EDITOR"));
		cmdSend2Editor.setActionCommand(Constants.ACTION_SEND2EDITOR);
		cmdSend2Editor.addActionListener(communication);
	}

	private HashMap<String, ToolbarButton> htPropButtons = new HashMap<String, ToolbarButton>();

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(Constants.PROPERTY_PROPAGATION) || e.getActionCommand().equals(Constants.PROPERTY_DEPROPAGATION) || e.getActionCommand().equals(Constants.PROPERTY_CONFIGURATION)) {
			// this action will be send if one of the components want to configure other
			// application parts
			if (e instanceof PropertyActionEvent) {
				PropertyActionEvent pae = (PropertyActionEvent) e;
				if (e.getActionCommand().equals(Constants.PROPERTY_PROPAGATION)) {
					ToolbarButton tb = null;
					if (htPropButtons.containsKey(pae.getUniqueId())) {
						tb = (ToolbarButton) htPropButtons.get(pae.getUniqueId());
					} else {
						tb = new ToolbarButton();
						tb.setToolTipText(pae.getDescDetail());
						tb.setActionCommand(pae.getAction());
						tb.addActionListener(((Communication) getBean(Beans.COMMUNICATION)));
						try {
							tb.setIcon(UIConstants.getPropImage(pae.getUniqueId(), UIConstants.IMAGE_SIZE_20));
						} catch (Exception exe) {
						}
						htPropButtons.put(pae.getUniqueId(), tb);
					}
					this.add(tb);
				} else if (e.getActionCommand().equals(Constants.PROPERTY_DEPROPAGATION)) {
					// DEPROPAGATION
					if (htPropButtons.containsKey(pae.getUniqueId())) {
						this.remove((Component) htPropButtons.get(pae.getUniqueId()));
					}
				}
			} else if (e.getActionCommand().equals(Constants.PROPERTY_CONFIGURATION) && e instanceof PropertyConfigurationEvent && htPropButtons.containsKey(((PropertyConfigurationEvent) e).getUniqueId())) {
				// CONFIGURATION
				PropertyConfigurationEvent pce = (PropertyConfigurationEvent) e;
				if (pce.getKey().equalsIgnoreCase(PropertyConfigurationEvent.PROP_ENABLE)) {
					ToolbarButton tb = (ToolbarButton) htPropButtons.get(pce.getUniqueId());
					tb.setEnabled(Boolean.valueOf(pce.getValue()).booleanValue());
				}
			}
		} 
	}

}
