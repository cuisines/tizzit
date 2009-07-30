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
package de.juwimm.cms.content.panel;

import static de.juwimm.cms.common.Constants.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import de.juwimm.cms.Messages;
import de.juwimm.cms.content.event.EditpaneFiredEvent;
import de.juwimm.cms.content.event.EditpaneFiredListener;
import de.juwimm.cms.content.modules.Documents;
import de.juwimm.cms.content.modules.ExternalLink;
import de.juwimm.cms.content.modules.InternalLink;
import de.juwimm.cms.content.modules.Module;

/**
 * Panel for displaying internal and external links in panelmode
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class PanLinkButton extends JPanel implements EditpaneFiredListener {
	private static Logger log = Logger.getLogger(PanLinkButton.class);
	private JButton btnOpen = new JButton();
	private JButton btnDeleteConnection = new JButton();
	private JPanel panLinkData = null;
	private JLabel lblLinkName = null;
	private JLabel lblLinkTarget = null;
	private Module module;

	protected PanLinkButton() {
		try {
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization problem", exe);
		}
	}

	public PanLinkButton(Module module, boolean showDeleteButton) {
		this();
		this.module = module;
		this.module.addEditpaneFiredListener(this);
		btnOpen.setText(Messages.getString("panel.panOnlyButton.openButton", module.getLabel()));
		if (showDeleteButton) {
			btnDeleteConnection.setText(Messages.getString("panel.panOnlyButton.deleteConnection", module.getLabel()));
			btnDeleteConnection.setVisible(true);
		} else {
			btnDeleteConnection.setVisible(false);
		}
	}

	protected void jbInit() throws Exception {
		btnOpen.setText("open Module");
		btnOpen.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnOpenActionPerformed(e);
			}
		});
		this.setLayout(new GridBagLayout());
		btnDeleteConnection.setText("Delete Connection");
		btnDeleteConnection.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDeleteConnectionActionPerformed(e);
			}
		});
		this.add(btnOpen, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 10, 2, 0), 0, 0));
		this.add(btnDeleteConnection, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
		this.add(this.getPanLinkData(), new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 10, 2, 0), 0, 0));
	}

	void btnOpenActionPerformed(ActionEvent e) {
		btnOpen.setEnabled(false);
		btnDeleteConnection.setEnabled(false);
		this.module.viewModalUI(true);
	}

	public void editpaneFiredPerformed(EditpaneFiredEvent efe) {
		btnOpen.setEnabled(true);
		btnDeleteConnection.setEnabled(true);
		this.updateLinks();
	}

	public void editpaneCancelPerformed(EditpaneFiredEvent efe) {
		btnOpen.setEnabled(true);
		btnDeleteConnection.setEnabled(true);
	}

	public void setEnabled(boolean enabling) {
		btnOpen.setEnabled(enabling);
		btnDeleteConnection.setEnabled(enabling);
	}

	void btnDeleteConnectionActionPerformed(ActionEvent e) {
		this.module.setProperties(null);
	}
	
	private JPanel getPanLinkData() {
		if (this.panLinkData == null) {
			this.panLinkData = new JPanel();
			this.lblLinkName = new JLabel("");
			//lblLinkName.setText(rb.getString("panel.panelView.lblLinkname"));
			this.lblLinkTarget = new JLabel("");
			this.panLinkData.setLayout(new GridBagLayout());
			this.panLinkData.add(new JLabel(rb.getString("panel.panelView.lblLinkname") + ": "), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(2, 10, 2, 0), 0, 0));
			this.panLinkData.add(this.lblLinkName, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(2, 10, 2, 0), 0, 0));
			this.panLinkData.add(new JLabel(rb.getString("content.modules.externalLink.linkAddress") + ": "), new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(2, 10, 2, 0), 0, 0));
			this.panLinkData.add(this.lblLinkTarget, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(2, 10, 2, 0), 0, 0));
		}
		return this.panLinkData;
	}
	
	public void updateLinks() {
		if (this.module instanceof InternalLink) {
			this.updateLinks((InternalLink) this.module);
		} else if (this.module instanceof ExternalLink) {
			this.updateLinks((ExternalLink) this.module);
		} else if (this.module instanceof Documents) {
			this.updateLinks((Documents) this.module);
		}
	}
	
	private void updateLinks(InternalLink il) {
		this.lblLinkName.setText(il.getLinkName());
		this.lblLinkTarget.setText(il.getLinkTarget());
	}

	private void updateLinks(ExternalLink el) {
		this.lblLinkName.setText(el.getLinkName());
		this.lblLinkTarget.setText(el.getLinkTarget());
	}
	
	private void updateLinks(Documents doc) {
		this.lblLinkName.setText(doc.getDocumentDescription());
		this.lblLinkTarget.setText(doc.getDocumentName());
	}
	
	public void addDeleteSettingsActionListener(ActionListener al) {
		if (this.btnDeleteConnection != null)
			this.btnDeleteConnection.addActionListener(al);
	}

}