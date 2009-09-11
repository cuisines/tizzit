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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.content.panel.PanSimpleDate;
import de.juwimm.cms.gui.PanTree;
import de.juwimm.cms.gui.controls.LoadableViewComponentPanel;
import de.juwimm.cms.gui.tree.PageNode;
import de.juwimm.cms.util.ActionHub;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.Parameters;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.ViewComponentValue;
import de.juwimm.swing.DropDownHolder;

/**
 * <p>Title: ConQuest </p>
 * <p>Description: Content Management System</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanMenuentry extends JPanel implements LoadableViewComponentPanel, ActionListener {
	private JButton btnReindexPage;
	private static Logger log = Logger.getLogger(PanMenuentry.class);
	private ViewComponentValue viewComponent;
	private JLabel lblDisplayedLinkname = new JLabel();
	private JTextField txtDisplayedLinkName = new JTextField();
	private JLabel lblLinkDescription = new JLabel();
	private JTextField txtLinkDescription = new JTextField();
	private JLabel lblUrlLinkName = new JLabel();
	private JTextField txtUrlLinkName = new JTextField();
	private JPanel panStati = new JPanel();
	private JPanel panDefault = new JPanel();
	private TitledBorder borderStati;
	private JLabel lblStateDeploy = new JLabel();
	private JLabel lblStateDeployContent = new JLabel();
	private Communication communication = ((Communication) getBean(Beans.COMMUNICATION));
	private JLabel lblStateOnlineContent = new JLabel();
	private JLabel lblStateOnline = new JLabel();
	private JCheckBox chkOpenNewNavi = new JCheckBox();
	private PanSimpleDate txtOnlineStart = new PanSimpleDate();
	private PanSimpleDate txtOnlineStop = new PanSimpleDate();
	private JPanel panOnlineOfflineDates = new JPanel();
	private DefaultComboBoxModel showTypeModel = new DefaultComboBoxModel();
	private JComboBox optSelectShow = new JComboBox(showTypeModel);
	private JPanel panOptPan = new JPanel();
	private JLabel lblVcId = new JLabel();
	private JLabel lblVcIdcontent = new JLabel();
	private DropDownHolder showType0 = null;
	private DropDownHolder showType1 = null;
	private DropDownHolder showType2 = null;
	private DropDownHolder showType3 = null;
	private JCheckBox chkSetInvisible = new JCheckBox();
	private JCheckBox chkSearchIndexed = new JCheckBox();
	private JCheckBox chkXmlSearchIndexed = new JCheckBox();
	private JLabel lblLastModifiedText = new JLabel();
	private JLabel lblLastModifiedData = new JLabel();
	private JLabel lblUserLastModifiedText = new JLabel();
	private JLabel lblUserLastModifiedData = new JLabel();
	private boolean saveStatus;

	public PanMenuentry() {
		try {
			setDoubleBuffered(true);
			jbInit();
			txtDisplayedLinkName.addKeyListener(ActionHub.getContentEditKeyListener());
			txtLinkDescription.addKeyListener(ActionHub.getContentEditKeyListener());
			txtUrlLinkName.addKeyListener(ActionHub.getContentEditKeyListener());
			optSelectShow.addMouseListener(ActionHub.getContentEditMouseListener());
			chkSetInvisible.addKeyListener(ActionHub.getContentEditKeyListener());
			chkSetInvisible.addMouseListener(ActionHub.getContentEditMouseListener());
			chkSearchIndexed.addKeyListener(ActionHub.getContentEditKeyListener());
			chkSearchIndexed.addMouseListener(ActionHub.getContentEditMouseListener());
			chkXmlSearchIndexed.addKeyListener(ActionHub.getContentEditKeyListener());
			chkXmlSearchIndexed.addMouseListener(ActionHub.getContentEditMouseListener());

			borderStati = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(165, 163, 151)), rb.getString("panel.panStati"));
			lblDisplayedLinkname.setText(rb.getString("panel.panelView.lblDisplayedLinkname"));
			lblLinkDescription.setText(rb.getString("panel.panelView.lblLinkDescription"));
			lblUrlLinkName.setText(rb.getString("panel.panelView.lblUrlLinkName"));
			txtUrlLinkName.setToolTipText(rb.getString("panel.panelView.ttUrlLinkName"));
			lblStateDeploy.setText(rb.getString("panel.panelView.lblStateDeploy"));
			lblStateOnline.setText(rb.getString("panel.panelView.lblStateOnline"));
			chkOpenNewNavi.setText(rb.getString("panel.panelView.openNewNavi"));
			chkSetInvisible.setText(rb.getString("panel.panelView.content.setInvisible"));
			chkSearchIndexed.setText(rb.getString("panel.panelView.content.setSearchIndexed"));
			chkXmlSearchIndexed.setText(rb.getString("panel.panelView.content.setXmlSearchIndexed"));
			lblLastModifiedText.setText(rb.getString("panel.panelView.lastModifiedAt"));
			lblUserLastModifiedText.setText(rb.getString("panel.panelView.lastModifiedAtDisplay"));

			if (Parameters.getBooleanParameter(Parameters.PARAM_INCLUDE_XML_SEARCH_NAME)) {
				String txt = Parameters.getStringParameter(Parameters.PARAM_INCLUDE_XML_SEARCH_NAME);
				if (txt == null || txt.equals("")) txt = rb.getString("panel.panelView.content.setXmlSearchIndexed");
				chkXmlSearchIndexed.setText(txt);
			}
			if (Parameters.getBooleanParameter(Parameters.PARAM_SHOWTYPE_3)) {
				String txt = Parameters.getStringParameter(Parameters.PARAM_SHOWTYPE_3);
				if (txt == null || txt.equals("")) txt = rb.getString("panel.panelView.topAndMain");
				showType3 = new DropDownHolder(new String("3"), txt);
				this.showTypeModel.addElement(showType3);
			}
			if (Parameters.getBooleanParameter(Parameters.PARAM_SHOWTYPE_2)) {
				String txt = Parameters.getStringParameter(Parameters.PARAM_SHOWTYPE_2);
				if (txt == null || txt.equals("")) txt = rb.getString("panel.panelView.top");
				showType2 = new DropDownHolder(new String("2"), txt);
				this.showTypeModel.addElement(showType2);
			}
			if (Parameters.getBooleanParameter(Parameters.PARAM_SHOWTYPE_1)) {
				String txt = Parameters.getStringParameter(Parameters.PARAM_SHOWTYPE_1);
				if (txt == null || txt.equals("")) txt = rb.getString("panel.panelView.bottom");
				showType1 = new DropDownHolder(new String("1"), txt);
				this.showTypeModel.addElement(showType1);
			}
			if (Parameters.getBooleanParameter(Parameters.PARAM_SHOWTYPE_0)) {
				String txt = Parameters.getStringParameter(Parameters.PARAM_SHOWTYPE_0);
				if (!(txt == null || txt.equals(""))) {
					showType0 = new DropDownHolder(new String("0"), txt);
					this.showTypeModel.addElement(showType0);
				}
			}
			lblVcId.setText(rb.getString("panel.panelView.vcid"));
			txtOnlineStart.setDateButtonText(rb.getString("panel.panelView.OnlineDateButton"));
			txtOnlineStop.setDateButtonText(rb.getString("panel.panelView.OfflineDateButton"));
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	private void jbInit() throws Exception {
		GridBagConstraints gridBagConstraints9 = new GridBagConstraints(0, 5, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 5, 0, 10), 0, 0);
		gridBagConstraints9.gridwidth = 4;
		GridBagConstraints gridBagConstraints8 = new GridBagConstraints(2, 4, 2, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0);
		gridBagConstraints8.gridwidth = 1;
		GridBagConstraints gridBagConstraints7 = new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0);
		gridBagConstraints7.gridwidth = 1;
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0);
		gridBagConstraints6.gridwidth = 1;
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0);
		gridBagConstraints5.gridwidth = 1;
		GridBagConstraints gridBagConstraints41 = new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 1);
		gridBagConstraints41.gridwidth = 3;
		GridBagConstraints gridBagConstraints31 = new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 1);
		gridBagConstraints31.gridwidth = 3;
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 1);
		gridBagConstraints21.gridwidth = 3;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0);
		gridBagConstraints11.gridwidth = 3;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(4, 8, 6, 9), 0, 0);
		gridBagConstraints4.gridwidth = 2;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(4, 8, 6, 9), 0, 0);
		gridBagConstraints3.gridwidth = 2;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 6, 9), 0, 0);
		gridBagConstraints2.gridwidth = 2;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 0), 0, 4);
		GridBagConstraints gridBagConstraints = new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 4);
		gridBagConstraints.gridwidth = 2;
		borderStati = new TitledBorder(BorderFactory.createEtchedBorder(), "Stati");
		this.setLayout(new GridBagLayout());
		lblDisplayedLinkname.setPreferredSize(new Dimension(140, 15));
		lblDisplayedLinkname.setText("LINKNAME");
		lblLinkDescription.setPreferredSize(new Dimension(140, 15));
		lblLinkDescription.setText("Statuszeile");
		lblUrlLinkName.setText("interner Linkname");
		lblUrlLinkName.setPreferredSize(new Dimension(140, 15));
		txtUrlLinkName.setText("");
		chkSetInvisible.setText("This Page is invisible");
		chkSearchIndexed.setText("Include this page in search");
		chkXmlSearchIndexed.setText("Include this page in xml-search");
		panDefault.setBorder(BorderFactory.createEtchedBorder());
		panDefault.setLayout(new GridBagLayout());
		panStati.setBorder(borderStati);
		panStati.setLayout(new GridBagLayout());
		lblStateDeploy.setMaximumSize(new Dimension(60, 15));
		lblStateDeploy.setMinimumSize(new Dimension(90, 15));
		lblStateDeploy.setPreferredSize(new Dimension(90, 15));
		lblStateDeploy.setText("Deploy Status");
		lblStateDeployContent.setFont(new java.awt.Font("Dialog", 1, 11));
		lblStateDeployContent.setBorder(BorderFactory.createLoweredBevelBorder());
		lblStateDeployContent.setMaximumSize(new Dimension(38, 21));
		lblStateDeployContent.setMinimumSize(new Dimension(38, 21));
		lblStateDeployContent.setPreferredSize(new Dimension(38, 21));
		lblStateDeployContent.setIconTextGap(5);
		lblStateDeployContent.setText("DEPLOY_STATUS_EDITED");
		lblStateOnlineContent.setFont(new java.awt.Font("Dialog", 1, 11));
		lblStateOnlineContent.setBorder(BorderFactory.createLoweredBevelBorder());
		lblStateOnlineContent.setMaximumSize(new Dimension(38, 21));
		lblStateOnlineContent.setMinimumSize(new Dimension(38, 21));
		lblStateOnlineContent.setPreferredSize(new Dimension(38, 21));
		lblStateOnlineContent.setIconTextGap(5);
		lblStateOnlineContent.setText("ONLINE_STATUS_ONLINE");
		lblStateOnline.setMaximumSize(new Dimension(60, 15));
		lblStateOnline.setMinimumSize(new Dimension(60, 15));
		lblStateOnline.setPreferredSize(new Dimension(60, 15));
		lblStateOnline.setText("Online State");
		lblStateOnline.setVerticalAlignment(SwingConstants.TOP);
		lblLastModifiedText.setText("last modified at:");
		lblLastModifiedData.setText("01.01.1970 00:00");
		lblUserLastModifiedText.setText("displayed last modified at:");
		lblUserLastModifiedData.setText("01.01.1970 00:00");

		txtOnlineStop.setBounds(new Rectangle(167, 331, 10, 15));

		lblVcId.setText("ViewComponentID");
		lblVcIdcontent.setText(" ");
		panDefault.add(lblVcId, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		panDefault.add(lblVcIdcontent, gridBagConstraints11);
		panDefault.add(lblDisplayedLinkname, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		panDefault.add(txtDisplayedLinkName, gridBagConstraints21);
		panDefault.add(lblLinkDescription, gridBagConstraints5);
		panDefault.add(txtLinkDescription, gridBagConstraints31);
		panOnlineOfflineDates.setLayout(new BorderLayout());
		panOnlineOfflineDates.setMinimumSize(new Dimension(40, 30));
		panOnlineOfflineDates.setOpaque(true);
		panOnlineOfflineDates.setPreferredSize(new Dimension(40, 50));
		panOnlineOfflineDates.add(txtOnlineStart, BorderLayout.WEST);
		panOnlineOfflineDates.add(txtOnlineStop, BorderLayout.EAST);
		panDefault.add(lblUrlLinkName, gridBagConstraints6);
		panDefault.add(txtUrlLinkName, gridBagConstraints41);
		panDefault.add(panOnlineOfflineDates, gridBagConstraints9);
		panDefault.add(lblLastModifiedText, gridBagConstraints7);
		panDefault.add(lblLastModifiedData, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 1));
		if (Parameters.getBooleanParameter(Parameters.PARAM_USER_CHANGE_PAGE_MODIFIED_DATE) && communication.isUserInRole(UserRights.PAGE_UPDATE_LAST_MODIFIED_DATE)) {
			panDefault.add(lblUserLastModifiedText, gridBagConstraints8);
			panDefault.add(lblUserLastModifiedData, new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 1));
		}

		this.add(panDefault, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(10, 10, 0, 10), 0, 0));

		this.add(panOptPan, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 10, 0), 0, 0));
		panStati.add(lblStateDeploy, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		panStati.add(lblStateDeployContent, gridBagConstraints);
		panStati.add(lblStateOnline, new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 10, 0), 0, 0));
		final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
		gridBagConstraints_1.insets = new Insets(0, 5, 0, 10);
		gridBagConstraints_1.anchor = GridBagConstraints.EAST;
		gridBagConstraints_1.gridy = 1;
		gridBagConstraints_1.gridx = 2;
		if (communication.isUserInRole(UserRights.PAGE_CHANGE_SEARCH_INDEXED)) {
			panStati.add(getBtnReindexPage(), gridBagConstraints_1);
		}
		panStati.add(optSelectShow, gridBagConstraints2);
		panStati.add(chkOpenNewNavi, gridBagConstraints3);
		panStati.add(chkSetInvisible, gridBagConstraints4);
		panStati.add(chkSearchIndexed, new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(4, 8, 6, 9), 0, 0));
		panStati.add(chkXmlSearchIndexed, new GridBagConstraints(2, 5, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(4, 8, 6, 9), 0, 0));
		panStati.add(lblStateOnlineContent, gridBagConstraints1);
		this.add(panStati, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(8, 8, 0, 8), 0, 0));
	}

	protected JPanel getOptPan() {
		return panOptPan;
	}

	public boolean getSaveStatus() {
		return saveStatus;
	}

	public void save() throws Exception {
		boolean edited = false;
		saveStatus = true;
		Date onlineStart = txtOnlineStart.getDate();
		Date onlineStop = txtOnlineStop.getDate();

		if (!txtDisplayedLinkName.getText().equals(viewComponent.getDisplayLinkName()) || !txtLinkDescription.getText().equals(viewComponent.getLinkDescription()) || !txtUrlLinkName.getText().equals(viewComponent.getUrlLinkName())) {
			edited = true;
		}
		this.createUrlLinkName();
		txtUrlLinkName.setBackground(Color.white);
		String urlName = txtUrlLinkName.getText();
		if (!checkUrlLinkNameUnique(urlName)) {
			ActionHub.showMessageDialog(rb.getString("panel.panelView.dlgUniqueUrl.msg"), JOptionPane.INFORMATION_MESSAGE);
			txtUrlLinkName.setBackground(Color.red);
			txtUrlLinkName.requestFocus();
			saveStatus = false;
			return;
		}
		viewComponent.setDisplayLinkName(txtDisplayedLinkName.getText());
		viewComponent.setLinkDescription(txtLinkDescription.getText());
		viewComponent.setUrlLinkName(txtUrlLinkName.getText());
		if (viewComponent.getViewType() == Constants.VIEW_TYPE_UNIT || viewComponent.getViewType() == Constants.VIEW_TYPE_CONTENT || viewComponent.getViewType() == Constants.VIEW_TYPE_SYMLINK) {
			if (chkOpenNewNavi.isSelected()) {
				if (viewComponent.getViewIndex() != null && !viewComponent.getViewIndex().equals("2")) edited = true;
				viewComponent.setViewIndex("2");
				if ((viewComponent.getDisplaySettings() & Constants.DISPLAY_SETTING_NEW_NAVIGATION) != Constants.DISPLAY_SETTING_NEW_NAVIGATION) edited = true;
				viewComponent.setDisplaySettings((byte) (viewComponent.getDisplaySettings() | Constants.DISPLAY_SETTING_NEW_NAVIGATION));
			} else {
				if (viewComponent.getViewIndex() != null && !viewComponent.getViewIndex().equals("3")) edited = true;
				viewComponent.setViewIndex("3");
				if ((viewComponent.getDisplaySettings() & Constants.DISPLAY_SETTING_NEW_NAVIGATION) != 0) edited = true;
				viewComponent.setDisplaySettings((byte) (viewComponent.getDisplaySettings() & ~Constants.DISPLAY_SETTING_NEW_NAVIGATION));
			}
		}

		// save showType only, when user has rights to view and change
		if ((communication.isUserInRole(UserRights.PAGE_SHOWTYPE) || (communication.isUserInRole(UserRights.SITE_ROOT)))) {
			if (((DropDownHolder) showTypeModel.getSelectedItem()).getObject().toString().equalsIgnoreCase("1")) {
				if (viewComponent.getShowType() != 1) edited = true;
				viewComponent.setShowType((byte) 1);
			} else if (((DropDownHolder) showTypeModel.getSelectedItem()).getObject().toString().equalsIgnoreCase("2")) {
				if (viewComponent.getShowType() != 2) edited = true;
				viewComponent.setShowType((byte) 2);
			} else if (((DropDownHolder) showTypeModel.getSelectedItem()).getObject().toString().equalsIgnoreCase("3")) {
				if (viewComponent.getShowType() != 3) edited = true;
				viewComponent.setShowType((byte) 3);
			} else if (((DropDownHolder) showTypeModel.getSelectedItem()).getObject().toString().equalsIgnoreCase("0")) {
				if (viewComponent.getShowType() != 0) edited = true;
				viewComponent.setShowType((byte) 0);
			}
		}

		if ((onlineStart != null && onlineStart.getTime() != viewComponent.getOnlineStart()) || (viewComponent.getOnlineStart() > 0 && viewComponent.getOnlineStart() != onlineStart.getTime())) {
			edited = true;
			if (onlineStart != null) {
				viewComponent.setOnlineStart(new Long(onlineStart.getTime()));
			} else {
				viewComponent.setOnlineStart(new Long(0L));
			}
		}
		if ((onlineStop != null && onlineStop.getTime() != viewComponent.getOnlineStop()) || (viewComponent.getOnlineStop() > 0 && viewComponent.getOnlineStop() != onlineStop.getTime())) {
			edited = true;
			if (onlineStop != null) {
				viewComponent.setOnlineStop(new Long(onlineStop.getTime()));
			} else {
				viewComponent.setOnlineStop(new Long(0L));
			}
		}
		if (this.chkSetInvisible.isSelected()) {
			if (viewComponent.isVisible()) {
				edited = true;
			}
			viewComponent.setVisible(false);
		} else {
			if (!viewComponent.isVisible()) {
				edited = true;
			}
			viewComponent.setVisible(true);
		}
		if (this.chkSearchIndexed.isSelected()) {
			if (!viewComponent.isSearchIndexed()) {
				edited = true;
			}
			viewComponent.setSearchIndexed(true);
		} else {
			if (viewComponent.isSearchIndexed()) {
				edited = true;
			}
			viewComponent.setSearchIndexed(false);
		}
		if (this.chkXmlSearchIndexed.isSelected()) {
			if (!viewComponent.isXmlSearchIndexed()) {
				edited = true;
			}
			viewComponent.setXmlSearchIndexed(true);
		} else {
			if (viewComponent.isXmlSearchIndexed()) {
				edited = true;
			}
			viewComponent.setXmlSearchIndexed(false);
		}
		if (edited) {
			viewComponent.setStatus(Constants.DEPLOY_STATUS_EDITED);
		}
	}

	public void load(ViewComponentValue viewComponentValue) {
		if (log.isDebugEnabled()) {
			log.debug("load(ViewComponentDao vcd) " + viewComponentValue.getViewComponentId());
		}
		ActionHub.addActionListener(this);
		viewComponent = viewComponentValue;
		txtDisplayedLinkName.setText(viewComponent.getDisplayLinkName());
		txtLinkDescription.setText(viewComponent.getLinkDescription());
		txtUrlLinkName.setText(viewComponent.getUrlLinkName());
		txtUrlLinkName.setBackground(Color.white);

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(rb.getString("General.ShortDateTimeFormat"));
			lblLastModifiedData.setText(sdf.format(new Date(viewComponentValue.getLastModifiedDate())));
			lblUserLastModifiedData.setText(sdf.format(new Date(viewComponentValue.getUserLastModifiedDate())));
		} catch (Exception e) {
		}

		//lblLastModifiedData.setText(viewComponent.g)

		if (viewComponent.getOnlineStart() > 0) {
			txtOnlineStart.setDateTextField(viewComponent.getOnlineStart());
		} else {
			txtOnlineStart.setDateTextField((Date) null);
		}
		if (viewComponent.getOnlineStop() > 0) {
			txtOnlineStop.setDateTextField(viewComponent.getOnlineStop());
		} else {
			txtOnlineStop.setDateTextField((Date) null);
		}

		optSelectShow.setVisible(false);
		lblVcId.setVisible(false);
		lblVcIdcontent.setVisible(false);
		txtUrlLinkName.setEditable(false);
		this.txtDisplayedLinkName.setVisible(true);
		this.lblDisplayedLinkname.setVisible(true);

		if (communication.isUserInRole(UserRights.PAGE_STATUSBAR)) {
			lblLinkDescription.setVisible(true);
			txtLinkDescription.setVisible(true);
		} else {
			lblLinkDescription.setVisible(false);
			txtLinkDescription.setVisible(false);
		}

		if (communication.isUserInRole(UserRights.SITE_ROOT)) {
			lblVcId.setVisible(true);
			lblVcIdcontent.setVisible(true);
			lblVcIdcontent.setText(String.valueOf(viewComponent.getViewComponentId()));
			lblUrlLinkName.setVisible(true);
			txtUrlLinkName.setEditable(true);
		} else if (communication.isUserInRole(UserRights.PAGE_EDIT_URL_LINKNAME)) {
			lblUrlLinkName.setVisible(true);
			txtUrlLinkName.setEditable(true);
		}

		if ((communication.isUserInRole(UserRights.PAGE_SHOWTYPE) || (communication.isUserInRole(UserRights.SITE_ROOT)))) {
			if (!viewComponentValue.isRoot()) {
				optSelectShow.setVisible(true);
				switch (viewComponentValue.getShowType()) {
					case 3:
						if (showType3 == null) {
							showType3 = new DropDownHolder("3", "3-unknown");
							this.showTypeModel.addElement(showType3);
						}
						showTypeModel.setSelectedItem(showType3);
						break;
					case 2:
						if (showType2 == null) {
							showType2 = new DropDownHolder("2", "2-unknown");
							this.showTypeModel.addElement(showType2);
						}
						showTypeModel.setSelectedItem(showType2);
						break;
					case 1:
						if (showType1 == null) {
							showType1 = new DropDownHolder("1", "1-unknown");
							this.showTypeModel.addElement(showType1);
						}
						showTypeModel.setSelectedItem(showType1);
						break;
					case 0:
						if (showType0 == null) {
							showType0 = new DropDownHolder("0", "0-default");
							this.showTypeModel.addElement(showType0);
						}
						showTypeModel.setSelectedItem(showType0);
						break;
					default:
						log.info("showType " + viewComponentValue.getShowType() + " not supported - ignoring...");
						break;
				}
			}
		}

		if (viewComponent.getViewType() == Constants.VIEW_TYPE_UNIT || viewComponent.getViewType() == Constants.VIEW_TYPE_CONTENT || viewComponent.getViewType() == Constants.VIEW_TYPE_SYMLINK) {
			if (communication.isUserInRole(UserRights.PAGE_OPEN_NEW_NAVIGATION)) {
				this.chkOpenNewNavi.setVisible(true);
			}
			/*
			 if (viewComponent.getViewIndex() != null && viewComponent.getViewIndex().equals("2")) {
			 this.chkOpenNewNavi.setSelected(true);
			 } else {
			 this.chkOpenNewNavi.setSelected(false);
			 }
			 */
			if ((viewComponent.getDisplaySettings() & Constants.DISPLAY_SETTING_NEW_NAVIGATION) == Constants.DISPLAY_SETTING_NEW_NAVIGATION) {
				this.chkOpenNewNavi.setSelected(true);
			} else {
				this.chkOpenNewNavi.setSelected(false);
			}
			if (viewComponent.isFirstLevel()) {
				this.chkOpenNewNavi.setEnabled(false);
			} else {
				this.chkOpenNewNavi.setEnabled(true);
			}
		} else {
			if (communication.isUserInRole(UserRights.PAGE_OPEN_NEW_NAVIGATION)) {
				this.chkOpenNewNavi.setVisible(false);
			}
		}

		switch (viewComponent.getOnline()) {
			case Constants.ONLINE_STATUS_OFFLINE:
				lblStateOnlineContent.setText(rb.getString("actions.ONLINE_STATUS_OFFLINE"));
				lblStateOnlineContent.setIcon(UIConstants.CONTENT_OFFLINE);
				break;
			case Constants.ONLINE_STATUS_ONLINE:
				lblStateOnlineContent.setText(rb.getString("actions.ONLINE_STATUS_ONLINE"));
				lblStateOnlineContent.setIcon(UIConstants.CONTENT_DEPLOYED_LIVE);
				break;
			case Constants.ONLINE_STATUS_UNDEF:
				lblStateOnlineContent.setText(rb.getString("actions.ONLINE_STATUS_UNDEF"));
				lblStateOnlineContent.setIcon(UIConstants.CONTENT_OFFLINE);
				break;
			default:
				break;
		}
		String txt = "";
		switch (viewComponent.getStatus()) {
			case Constants.DEPLOY_STATUS_EDITED:
				txt = rb.getString("actions.DEPLOY_STATUS_EDITED");
				if (communication.isUserInRole(UserRights.SITE_ROOT)) txt = "(0) " + txt;
				lblStateDeployContent.setText(txt);
				lblStateDeployContent.setIcon(UIConstants.DEPLOYSTATUS_EDITED);
				break;
			case Constants.DEPLOY_STATUS_FOR_APPROVAL:
				txt = rb.getString("actions.DEPLOY_STATUS_FOR_APPROVAL");
				if (communication.isUserInRole(UserRights.SITE_ROOT)) txt = "(1) " + txt;
				lblStateDeployContent.setText(txt);
				lblStateDeployContent.setIcon(UIConstants.DEPLOYSTATUS_APPROVAL);
				break;
			case Constants.DEPLOY_STATUS_APPROVED:
				txt = rb.getString("actions.DEPLOY_STATUS_APPROVED");
				if (communication.isUserInRole(UserRights.SITE_ROOT)) txt = "(2) " + txt;
				lblStateDeployContent.setText(txt);
				lblStateDeployContent.setIcon(UIConstants.DEPLOYSTATUS_APPROVED);
				break;
			case Constants.DEPLOY_STATUS_FOR_DEPLOY:
				txt = rb.getString("actions.DEPLOY_STATUS_FOR_DEPLOY");
				if (communication.isUserInRole(UserRights.SITE_ROOT)) txt = "(3) " + txt;
				lblStateDeployContent.setText(txt);
				lblStateDeployContent.setIcon(UIConstants.CONTENT_DEPLOYED_LIVE);
				break;
			case Constants.DEPLOY_STATUS_DEPLOYED:
				txt = rb.getString("actions.DEPLOY_STATUS_DEPLOYED");
				if (communication.isUserInRole(UserRights.SITE_ROOT)) txt = "(4) " + txt;
				lblStateDeployContent.setText(txt);
				lblStateDeployContent.setIcon(UIConstants.CONTENT_DEPLOYED_LIVE);
				break;
			default:
				break;
		}

		if (communication.isUserInRole(UserRights.PAGE_MAKE_INVISIBLE) && !this.getViewComponent().isRoot()) {
			this.chkSetInvisible.setVisible(true);
		} else {
			this.chkSetInvisible.setVisible(false);
		}
		if (!viewComponentValue.isVisible()) {
			this.chkSetInvisible.setSelected(true);
		} else {
			this.chkSetInvisible.setSelected(false);
		}
		if (communication.isUserInRole(UserRights.PAGE_CHANGE_SEARCH_INDEXED) && !this.getViewComponent().isRoot()) {
			this.chkSearchIndexed.setVisible(true);
		} else {
			this.chkSearchIndexed.setVisible(false);
		}
		if (viewComponentValue.isSearchIndexed()) {
			this.chkSearchIndexed.setSelected(true);
		} else {
			this.chkSearchIndexed.setSelected(false);
		}

		if (communication.isUserInRole(UserRights.PAGE_CHANGE_XML_SEARCH_INDEXED) && !this.getViewComponent().isRoot()) {
			this.chkXmlSearchIndexed.setVisible(true);
		} else {
			this.chkXmlSearchIndexed.setVisible(false);
		}
		if (viewComponentValue.isXmlSearchIndexed()) {
			this.chkXmlSearchIndexed.setSelected(true);
		} else {
			this.chkXmlSearchIndexed.setSelected(false);
		}

		//at last we will disable the functions for root, it is already enabled if needed & access
		if (viewComponent.isRoot()) {
			this.chkOpenNewNavi.setVisible(false);
			this.txtDisplayedLinkName.setVisible(false);
			this.lblDisplayedLinkname.setVisible(false);
			this.txtOnlineStart.setVisible(false);
			this.txtOnlineStop.setVisible(false);
			this.lblUrlLinkName.setVisible(false);
			this.txtUrlLinkName.setVisible(false);
			this.chkSetInvisible.setVisible(false);
			this.chkSearchIndexed.setVisible(false);
			this.chkXmlSearchIndexed.setVisible(false);
		} else {
			this.txtOnlineStart.setVisible(true);
			this.txtOnlineStop.setVisible(true);
			this.txtUrlLinkName.setVisible(true);
		}
	}

	public void unload() {
		ActionHub.removeActionListener(this);
	}

	/*
	 private boolean isDateValid(String strDate) {
	 java.util.Date dte = null;
	 boolean bolRet = false;
	 try {
	 dte = DateConverter.getString2Sql(strDate);
	 } catch (Exception exe) {
	 }
	 if (dte != null) {
	 bolRet = true;
	 }
	 return bolRet;
	 }
	 */

	public void actionPerformed(ActionEvent ae) {
		String action = ae.getActionCommand();
		try {
			ViewComponentValue vc = ((PageNode) PanTree.getSelectedEntry()).getViewComponent();
			if (action.equals(Constants.ACTION_CONTENT_4APPROVAL)) {
				load(vc);
			} else if (action.equals(Constants.ACTION_CONTENT_CANCEL_APPROVAL) || action.equals(Constants.ACTION_CONTENT_EDITED)) {
				load(vc);
			} else if (action.equals(Constants.ACTION_CONTENT_APPROVE)) {
				load(vc);
			} else if (action.equals(Constants.ACTION_DEPLOY_STATUS_CHANGED)) {
				load(vc);
			}
		} catch (Exception exe) {
		}
	}

	protected JCheckBox getChkOpenNewNavi() {
		return this.chkOpenNewNavi;
	}

	public JComboBox getOptSelectShow() {
		return this.optSelectShow;
	}

	public JTextField getTxtInfo() {
		return this.txtLinkDescription;
	}

	public PanSimpleDate getTxtOnlineStart() {
		return this.txtOnlineStart;
	}

	public PanSimpleDate getTxtOnlineStop() {
		return this.txtOnlineStop;
	}

	public JTextField getTxtText() {
		return this.txtDisplayedLinkName;
	}

	public ViewComponentValue getViewComponent() {
		return this.viewComponent;
	}

	private void createUrlLinkName() {
		if ((communication.isUserInRole(UserRights.PAGE_EDIT_URL_LINKNAME) || (communication.isUserInRole(UserRights.SITE_ROOT)))) {
			// user may have changed the urlLinkName, so tidy it
			if (txtUrlLinkName.getText().length() > 0) {
				try {
					//Was: the urlLinkName has the initial value from the creation of this page (viewComponentId)
					Integer.valueOf(txtUrlLinkName.getText());
					txtUrlLinkName.setText(PanMenuentry.tidyUrl(txtDisplayedLinkName.getText()));
				} catch (NumberFormatException e) {

				}
				if (!txtUrlLinkName.getText().equals(viewComponent.getUrlLinkName())) {
					// urlLinkName has changed, tidy it
					txtUrlLinkName.setText(PanMenuentry.tidyUrl(txtUrlLinkName.getText()));
				}
			} else {
				txtUrlLinkName.setText(PanMenuentry.tidyUrl(txtDisplayedLinkName.getText()));
			}
		} else {
			// user has not the right to change the urlLinkName, so calculate it from the displayLinkName only if changed
			if (!txtDisplayedLinkName.getText().equals(viewComponent.getDisplayLinkName())) {
				txtUrlLinkName.setText(PanMenuentry.tidyUrl(txtDisplayedLinkName.getText()));
			}
		}
	}

	/**
	 * Checks for unique urlLinkName for a level
	 * @return boolean
	 */
	private boolean checkUrlLinkNameUnique(String urlName) {
		boolean flag = true;
		try {
			ViewComponentValue[] children = communication.getViewComponentChildren(viewComponent.getParentId());

			for (ViewComponentValue viewComponentValue : children) {
				if ((viewComponentValue.getUrlLinkName().equalsIgnoreCase(urlName)) && (viewComponent.getViewComponentId().intValue() != viewComponentValue.getViewComponentId().intValue())) {
					flag = false;
				}
			}
		} catch (Exception e) {
			System.out.println("error at getting children");
		}
		return flag;
	}

	public JLabel getLblUrlLinkName() {
		return lblUrlLinkName;
	}

	public JTextField getTxtUrlLinkName() {
		return txtUrlLinkName;
	}

	public JCheckBox getChkSearchIndexed() {
		return chkSearchIndexed;
	}

	public JCheckBox getChkXmlSearchIndexed() {
		return chkXmlSearchIndexed;
	}

	public JLabel getLblLinkDescription() {
		return lblLinkDescription;
	}

	public JTextField getTxtLinkDescription() {
		return txtLinkDescription;
	}

	/**
	 * Method for deleting all special-characters in a string. Allowed characters are [a-zA-Z_0-9].<br/>
	 * German umlauts will be converted.
	 * @return the converted and tidied string
	 */
	public static String tidyUrl(String url) {
		//return url.replaceAll("\\W", ""); //[a-zA-Z_0-9]

		String newUrl = url.toLowerCase();
		newUrl = newUrl.replaceAll("ä", "ae");
		newUrl = newUrl.replaceAll("ö", "oe");
		newUrl = newUrl.replaceAll("ü", "ue");
		newUrl = newUrl.replaceAll("Ä", "Ae");
		newUrl = newUrl.replaceAll("Ö", "Oe");
		newUrl = newUrl.replaceAll("Ü", "Ue");
		newUrl = newUrl.replaceAll("ß", "ss");
		return newUrl.replaceAll("[^A-Za-z_0-9\\.-]", "");
	}

	/**
	 * @return
	 */
	protected JButton getBtnReindexPage() {
		if (btnReindexPage == null) {
			btnReindexPage = new JButton();
			btnReindexPage.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					try {
						communication.reindexContent(viewComponent.getViewComponentId());
						ActionHub.showMessageDialog(rb.getString("panel.sitesAdministration.btnReindexSiteMsg"), JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception ee) {
						ActionHub.showMessageDialog("Error reindexing page!", JOptionPane.ERROR_MESSAGE);
						log.error("Error reindexing page", ee);
					}
				}
			});
			btnReindexPage.setText("Reindex Page");
		}
		return btnReindexPage;
	}

} //  @jve:decl-index=0:visual-constraint="243,118"
