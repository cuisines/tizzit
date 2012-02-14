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
package de.juwimm.cms.gui.views.menuentry;

import static de.juwimm.cms.client.beans.Application.getBean;
import static de.juwimm.cms.common.Constants.rb;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.event.EditpaneFiredEvent;
import de.juwimm.cms.content.event.EditpaneFiredListener;
import de.juwimm.cms.content.modules.AbstractModule;
import de.juwimm.cms.content.modules.InternalLink;
import de.juwimm.cms.content.modules.Module;
import de.juwimm.cms.exceptions.ViewComponentLinkNameAlreadyExisting;
import de.juwimm.cms.exceptions.ViewComponentLinkNameIsEmptyException;
import de.juwimm.cms.exceptions.ViewComponentNotFound;
import de.juwimm.cms.util.ActionHub;
import de.juwimm.cms.util.Communication;
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
public class PanMenuentryInternallink extends PanMenuentry implements EditpaneFiredListener {
	private static Logger log = Logger.getLogger(PanMenuentryInternallink.class);
	private final JButton cmdJump = new JButton();
	private final Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private final JCheckBox cbNewWindow = new JCheckBox();
	private final Module intLink = new InternalLink();
	private final JPanel panLinkProperties = new JPanel();
	private final JLabel lblPath = new JLabel();
	private final JButton btnChange = new JButton();
	private final JLabel lblAnchorDesc = new JLabel();
	private final JLabel lblAnchor = new JLabel();
	private boolean isSymlink = false;
	private boolean edited = false;
	private final JCheckBox cbReferrToCompleteDir = new JCheckBox();
	private Component component1;

	public PanMenuentryInternallink(boolean iAmASymlink) {
		super();
		isSymlink = iAmASymlink;
		if (isSymlink) {
			Properties prop = new Properties();
			prop.put("visible", Boolean.FALSE);
			intLink.setCustomProperties("anchor", prop);
		}
		intLink.addEditpaneFiredListener(this);
		try {
			jbInit();
			cbNewWindow.addMouseListener(ActionHub.getContentEditMouseListener());
			cbReferrToCompleteDir.addMouseListener(ActionHub.getContentEditMouseListener());

			cmdJump.setText(rb.getString("panel.panelView.jump.btnJump"));
			cbNewWindow.setText(rb.getString("panel.panelView.jump.newWindow"));
			cbReferrToCompleteDir.setText(rb.getString("panel.panelView.jump.symDir"));
			btnChange.setText(rb.getString("panel.panelView.jump.btnChange"));
			lblAnchorDesc.setText(rb.getString("panel.panelView.jump.anchorDesc"));
		} catch (Exception ex) {
		}

		//FIXME: ???
		this.cbReferrToCompleteDir.setVisible(false); // CURRENTLY NOT IMPLEMENTED
	}

	void jbInit() throws Exception {
		component1 = Box.createVerticalStrut(8);
		cmdJump.setText("springe zum referenzierten Content");
		cmdJump.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdJumpActionPerformed(e);
			}
		});
		cbNewWindow.setText(" im neuen Fenster öffnen");

		panLinkProperties.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(165, 163, 151)), rb.getString("panel.panelView.link.linkProperties")));
		panLinkProperties.setDebugGraphicsOptions(0);
		panLinkProperties.setBounds(new Rectangle(13, 279, 424, 124));
		panLinkProperties.setLayout(new GridBagLayout());
		lblPath.setBorder(BorderFactory.createLoweredBevelBorder());
		lblPath.setText("/Unternehmen/Verwaltung/Stabsstellen/Stabsstelle 1");
		btnChange.setForeground(Color.black);
		btnChange.setText("Verknüpfung ändern");
		btnChange.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnChangeActionPerformed(e);
			}
		});
		cbReferrToCompleteDir.setActionCommand("jCheckBox1");
		cbReferrToCompleteDir.setText("Verzeichnis verlinken (Symdir)");
		lblAnchor.setBorder(BorderFactory.createLoweredBevelBorder());
		lblAnchor.setHorizontalTextPosition(SwingConstants.CENTER);

		getOptPan().setLayout(new GridBagLayout());
		//row 1
		panLinkProperties.add(lblPath, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(4, 10, 0, 10), 0, 0));
		panLinkProperties.add(lblAnchorDesc, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 0, 0, 0), 0, 0));
		panLinkProperties.add(lblAnchor, new GridBagConstraints(3, 0, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(4, 5, 0, 10), 0, 0));
		//row 2
		panLinkProperties.add(cbNewWindow, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(4, 10, 4, 10), 0, 0));
		panLinkProperties.add(cmdJump, new GridBagConstraints(2, 1, 1, 1, 0.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(4, 10, 4, 10), 0, 0));
		panLinkProperties.add(btnChange, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(4, 10, 4, 10), 0, 0));
		//row 3
		panLinkProperties.add(cbReferrToCompleteDir, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 4, 10), 0, 0));

		getOptPan().add(component1, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(8, 0, 0, 0), 0, 0));
		getOptPan().add(panLinkProperties, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(8, 8, 4, 8), 0, 0));
	}

	@Override
	public void save() throws Exception {
		super.save();

		String prevVLevel = this.getViewComponent().getViewLevel();
		if (prevVLevel == null) prevVLevel = "";
		String prevVIndex = this.getViewComponent().getViewIndex();
		if (prevVIndex == null) prevVIndex = "";

		if (!isSymlink) {
			int vcid = new Integer(this.getViewComponent().getReference()).intValue();
			ViewComponentValue referencedView = comm.getViewComponent(vcid);
			int referencedLevel = 0;

			try {
				referencedLevel = new Integer(referencedView.getViewLevel()).intValue();
			} catch (Exception ex) {
				referencedLevel = 6;
			}

			if (cbNewWindow.isSelected()) {
				if (referencedLevel <= 3) {
					String newLevel = new Integer(referencedLevel + 3).toString();
					this.getViewComponent().setViewLevel(newLevel);
				} else {
					String newLevel = new Integer(referencedLevel).toString();
					this.getViewComponent().setViewLevel(newLevel);
				}
				this.getViewComponent().setViewIndex("1");
				this.getViewComponent().setDisplaySettings((byte) (this.getViewComponent().getDisplaySettings() | Constants.DISPLAY_SETTING_NEW_WINDOW));
			} else {
				this.getViewComponent().setViewIndex(referencedView.getViewIndex());
				this.getViewComponent().setViewLevel(referencedView.getViewLevel());
				this.getViewComponent().setDisplaySettings((byte) (this.getViewComponent().getDisplaySettings() & ~Constants.DISPLAY_SETTING_NEW_WINDOW));
			}
		} else {
			this.getViewComponent().setViewLevel("3");
			this.getViewComponent().setReference(((InternalLink) intLink).getLinkTargetViewComponentId().toString());
			//this.getViewComponent().setViewIndex("3");
		}

		if (!prevVLevel.equalsIgnoreCase(this.getViewComponent().getViewLevel()) || !prevVIndex.equalsIgnoreCase(this.getViewComponent().getViewIndex()) || edited) {
			this.getViewComponent().setStatus(Constants.DEPLOY_STATUS_EDITED);
			// If you checkIn a NEW Contentversion, your "REMOVE-COMMAND" will also be removed
			if (this.getViewComponent().getDeployCommand() == Constants.DEPLOY_COMMAND_DELETE || this.getViewComponent().getDeployCommand() == Constants.DEPLOY_COMMAND_REMOVE) {
				this.getViewComponent().setDeployCommand(Constants.DEPLOY_COMMAND_MODIFY);
			}
			log.debug("updating status for jump (internal link / symlink)");
			comm.updateStatus4ViewComponent(this.getViewComponent());
		}

		try {
			comm.saveViewComponent(this.getViewComponent());
			ActionHub.fireActionPerformed(new ActionEvent(this.getViewComponent(), ActionEvent.ACTION_PERFORMED, Constants.ACTION_TREE_ENTRY_NAME));
		} catch (ViewComponentLinkNameAlreadyExisting vc) {
			JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("exception.ViewComponentLinkNameAlreadyExisting"), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
		} catch (ViewComponentNotFound vn) {
			JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("exception.ViewComponentNotFound"), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
		} catch (ViewComponentLinkNameIsEmptyException ve) {
			JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("exception.ViewComponentLinkNameIsEmpty"), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void load(ViewComponentValue vcd) {
		btnChange.setEnabled(true);
		edited = false;
		super.load(vcd);

		lblPath.setText(" ");
		lblPath.validate();
		String path = comm.getPathForViewComponentId(new Integer(this.getViewComponent().getReference()).intValue());
		if (!"".equalsIgnoreCase(this.getViewComponent().getMetaData())) {
			lblAnchor.setText(this.getViewComponent().getMetaData());
			lblAnchor.setVisible(true);
			lblAnchorDesc.setVisible(true);
		} else {
			lblAnchor.setText("");
			lblAnchor.setVisible(false);
			lblAnchorDesc.setVisible(false);
		}
		lblPath.setText("/" + AbstractModule.getURLDecoded(path));
		lblPath.validate();

		if (!comm.isUserInRole(UserRights.SITE_ROOT)) {
			cmdJump.setEnabled(false);
		} else {
			cmdJump.setEnabled(true);
		}
		/*
		try {
			int index = new Integer(this.getViewComponent().getViewIndex()).intValue();

			if (index == 1) {
				cbNewWindow.setSelected(true);
			} else {
				cbNewWindow.setSelected(false);
			}
		} catch (NumberFormatException ex) {
			cbNewWindow.setSelected(false);
		}
		*/
		cbNewWindow.setSelected((this.getViewComponent().getDisplaySettings() & Constants.DISPLAY_SETTING_NEW_WINDOW) == Constants.DISPLAY_SETTING_NEW_WINDOW);
		if (this.getViewComponent().getViewType() == Constants.VIEW_TYPE_SYMLINK) {
			cbNewWindow.setVisible(false);
		} else {
			cbNewWindow.setVisible(true);
		}
	}

	void cmdJumpActionPerformed(ActionEvent e) {
		ActionHub.fireActionPerformed(new ActionEvent(this.getViewComponent().getReference(), ActionEvent.ACTION_PERFORMED, Constants.ACTION_TREE_JUMP));
	}

	void btnChangeActionPerformed(ActionEvent e) {
		btnChange.setEnabled(false);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		Element root = ContentManager.getDomDoc().createElement("linkRoot");
		Element elm = ContentManager.getDomDoc().createElement("internalLink");
		CDATASection txtNode = ContentManager.getDomDoc().createCDATASection(this.getTxtInfo().getText());
		elm.appendChild(txtNode);
		elm.setAttribute("viewid", this.getViewComponent().getReference());
		elm.setAttribute("anchor", this.getViewComponent().getMetaData());
		root.appendChild(elm);

		((InternalLink) intLink).setIsSymlink(this.getViewComponent().getViewType() == Constants.VIEW_TYPE_SYMLINK);
		intLink.setProperties(root);
		intLink.viewModalUI(false);
		intLink.load();
		this.setCursor(Cursor.getDefaultCursor());
	}

	public void editpaneCancelPerformed(EditpaneFiredEvent efe) {
		btnChange.setEnabled(true);
	}

	public void editpaneFiredPerformed(EditpaneFiredEvent efe) {
		btnChange.setEnabled(true);
		// got a change event from the good internalLink
		Node prop = intLink.getProperties();
		String viewid = ((Element) prop.getFirstChild()).getAttribute("viewid");
		String anchor = ((Element) prop.getFirstChild()).getAttribute("anchor");
		this.getViewComponent().setReference(viewid);
		this.getViewComponent().setMetaData(anchor);

		load(this.getViewComponent());
		edited = true;
		Constants.EDIT_CONTENT = true;
	}

}