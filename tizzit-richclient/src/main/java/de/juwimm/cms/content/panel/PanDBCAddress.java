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

import static de.juwimm.cms.client.beans.Application.*;
import static de.juwimm.cms.common.Constants.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.border.MatteBorder;

import org.apache.log4j.Logger;

import de.juwimm.cms.Messages;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.components.vo.AddressValue;
import de.juwimm.cms.content.frame.tree.AddressNode;
import de.juwimm.cms.content.frame.tree.ComponentNode;
import de.juwimm.cms.content.panel.util.VisibilityCheckBox;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.DBCDao;
import de.juwimm.cms.util.UIConstants;

/**
 * <p>Title: juwimm cms</p>
 * <p>Description: content management system</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: juwi macmillan group gmbh</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanDBCAddress extends AbstractTreePanel implements DBCDao {
	private static Logger log = Logger.getLogger(PanDBCAddress.class);
	
	private AddressNode addressNode = null;
	
	private JLabel lblCaptionVisibility = new JLabel(UIConstants.DBC_VISIBILTY);
	private JLabel lblCaptionDB = new JLabel(rb.getString("PanDBC.component"));
	private JLabel lblCity = new JLabel();
	private JLabel lblRoomNo = new JLabel();
	private JLabel lblEmail = new JLabel();
	private JLabel lblCountryCode = new JLabel();
	private JLabel lblPhone2 = new JLabel();
	private JLabel lblFax = new JLabel();
	private JLabel lblMobilePhone = new JLabel();
	private JLabel lblPhone1 = new JLabel();
	private JTextField txtCity = new JTextField();
	private JTextField txtEmail = new JTextField();
	private JTextField txtPhone1 = new JTextField();
	private JTextField txtMobilePhone = new JTextField();
	private JTextField txtFax = new JTextField();
	private JTextField txtPhone2 = new JTextField();
	private VisibilityCheckBox vcbRoomNr = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbCity = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbPostOfficeBox = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbCountryCode = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbPhone1 = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbPhone2 = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbFax = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbMobilePhone = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbEmail = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbBuildingLevel = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbBuildingNr = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbStreet = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbStreetNr = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbZipcode = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbCountry = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbHomepage = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbMiscInfo = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbAddressType = new VisibilityCheckBox(getCheckActionListener());
	
	private JTextField txtRoomNr = new JTextField();
	private JLabel lblLevel = new JLabel();
	private JLabel lblBuildingNo = new JLabel();
	private JTextField txtBuildingNr = new JTextField();
	private JTextField txtBuildingLevel = new JTextField();
	private JTextField txtStreet = new JTextField();
	private JLabel lblHouseNo = new JLabel();
	private JTextField txtStreetNr = new JTextField();
	private JTextField txtZipcode = new JTextField();
	private JLabel lblZipcode = new JLabel();
	private JTextField txtPostOfficeBox = new JTextField();
	private JTextField txtCountryCode = new JTextField();
	private JTextField txtCountry = new JTextField();
	private JLabel lblCountry = new JLabel();
	private JLabel lblHomepage = new JLabel();
	private JTextField txtHomepage = new JTextField();
	private JLabel lblMiscInfo = new JLabel();
	private JRadioButton radioStreet = new JRadioButton();
	private JRadioButton radioPostOfficeBox = new JRadioButton();
	private JLabel lblAddressType = new JLabel();
	private JComboBox cbxAddressType = new JComboBox();
	private JScrollPane scrollPaneMiscInfo = new JScrollPane();
	private JTextArea txtMisc = new JTextArea();

	public PanDBCAddress() {
		try {
			setLayout(new GridBagLayout());
			setPreferredSize(new Dimension(330, 900));
			this.lblCity.setText(Messages.getString("PanDBCAddress.city"));
			this.lblRoomNo.setText(Messages.getString("PanDBCAddress.rooomNo"));
			this.lblEmail.setText(Messages.getString("PanDBCAddress.emailAddress"));
			this.lblCountryCode.setText(Messages.getString("PanDBCAddress.countryCode"));
			this.lblPhone2.setText(Messages.getString("PanDBCAddress.phone2"));
			this.lblFax.setText(Messages.getString("PanDBCAddress.fax"));
			this.lblMobilePhone.setText(Messages.getString("PanDBCAddress.mobile"));
			this.lblPhone1.setText(Messages.getString("PanDBCAddress.phone1"));
			this.txtCity.getDocument().addDocumentListener(getChangedDocumentListener());
			this.txtEmail.getDocument().addDocumentListener(getChangedDocumentListener());
			this.txtPhone1.getDocument().addDocumentListener(getChangedDocumentListener());
			this.txtMobilePhone.getDocument().addDocumentListener(getChangedDocumentListener());
			this.txtFax.getDocument().addDocumentListener(getChangedDocumentListener());
			this.txtPhone2.getDocument().addDocumentListener(getChangedDocumentListener());
			this.txtRoomNr.getDocument().addDocumentListener(getChangedDocumentListener());
			this.lblLevel.setText(Messages.getString("PanDBCAddress.buildingLevel"));
			this.lblBuildingNo.setText(Messages.getString("PanDBCAddress.buildingNo"));
			this.txtBuildingNr.getDocument().addDocumentListener(getChangedDocumentListener());
			this.txtBuildingLevel.getDocument().addDocumentListener(getChangedDocumentListener());
			this.txtStreet.getDocument().addDocumentListener(getChangedDocumentListener());
			this.lblHouseNo.setText(Messages.getString("PanDBCAddress.houseNo"));
			this.txtStreetNr.getDocument().addDocumentListener(getChangedDocumentListener());
			this.txtZipcode.getDocument().addDocumentListener(getChangedDocumentListener());
			this.lblZipcode.setText(Messages.getString("PanDBCAddress.zip"));
			this.txtPostOfficeBox.getDocument().addDocumentListener(getChangedDocumentListener());
			this.txtCountryCode.getDocument().addDocumentListener(getChangedDocumentListener());
			this.txtCountry.getDocument().addDocumentListener(getChangedDocumentListener());
			this.lblCountry.setText(Messages.getString("PanDBCAddress.country"));
			this.lblHomepage.setText(Messages.getString("PanDBCAddress.homepage"));
			this.txtHomepage.getDocument().addDocumentListener(getChangedDocumentListener());
			this.lblMiscInfo.setText(Messages.getString("PanDBCAddress.information"));
			this.txtMisc.setLineWrap(true);
			this.txtMisc.getDocument().addDocumentListener(getChangedDocumentListener());
			this.radioStreet.setSelected(true);
			this.radioStreet.setText(Messages.getString("PanDBCAddress.street"));
			this.radioStreet.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					txtPostOfficeBox.setText("");
					vcbPostOfficeBox.setSelected(false);
					txtPostOfficeBox.setEnabled(false);
					vcbPostOfficeBox.setEnabled(false);
					txtStreet.setEnabled(true);
					txtStreetNr.setEnabled(true);
					vcbStreet.setEnabled(true);
					vcbStreetNr.setEnabled(true);
					validate();
					repaint();
				}
			});

			this.cbxAddressType.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fireChangeListener();
				}
			});
			this.radioPostOfficeBox.setText(Messages.getString("PanDBCAddress.postOfficeBox"));
			this.radioPostOfficeBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					txtPostOfficeBox.setEnabled(true);
					vcbPostOfficeBox.setEnabled(true);
					txtStreet.setEnabled(false);
					txtStreetNr.setEnabled(false);
					vcbStreet.setEnabled(false);
					vcbStreetNr.setEnabled(false);
					txtStreet.setText("");
					txtStreetNr.setText("");
					vcbStreet.setSelected(false);
					vcbStreetNr.setSelected(false);
					validate();
					repaint();
				}
			});
			this.lblAddressType.setText(Messages.getString("PanDBCAddress.addressType"));
			this.scrollPaneMiscInfo.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			this.scrollPaneMiscInfo.setBorder(BorderFactory.createLoweredBevelBorder());
			
			add(this.lblCaptionVisibility, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblCaptionDB, new GridBagConstraints(1, 0, 7, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			JPanel horizontalRuler = new JPanel();
			horizontalRuler.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
			add(horizontalRuler, new GridBagConstraints(0, 1, 8, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));

			add(this.vcbAddressType, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblAddressType, new GridBagConstraints(1, 2, 5, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.cbxAddressType, new GridBagConstraints(1, 3, 5, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 5, 0, 0), 10, 0));
			
			add(this.vcbRoomNr, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblRoomNo, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.vcbBuildingLevel, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));
			add(this.lblLevel, new GridBagConstraints(4, 4, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.vcbBuildingNr, new GridBagConstraints(6, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));
			add(this.lblBuildingNo, new GridBagConstraints(7, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 1));

			add(this.txtRoomNr, new GridBagConstraints(1, 6, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 4));
			add(this.txtBuildingLevel, new GridBagConstraints(4, 6, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 4));
			add(this.txtBuildingNr, new GridBagConstraints(7, 6, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 4));
			
			add(this.vcbStreet, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.radioStreet, new GridBagConstraints(1, 7, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.vcbStreetNr, new GridBagConstraints(6, 7, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));
			add(this.lblHouseNo, new GridBagConstraints(7, 7, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 1));
			add(this.txtStreet, new GridBagConstraints(1, 8, 5, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 4));
			add(this.txtStreetNr, new GridBagConstraints(7, 8, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 70, 4));

			add(this.vcbPostOfficeBox, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.radioPostOfficeBox, new GridBagConstraints(1, 9, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.txtPostOfficeBox, new GridBagConstraints(1, 10, 5, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 4));
			
			add(this.vcbZipcode, new GridBagConstraints(0, 11, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblZipcode, new GridBagConstraints(1, 11, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.vcbCity, new GridBagConstraints(2, 11, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));
			add(this.lblCity, new GridBagConstraints(4, 11, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 68, 1));
			add(this.txtZipcode, new GridBagConstraints(1, 12, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 4));
			add(this.txtCity, new GridBagConstraints(4, 12, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 4));
			
			add(this.vcbCountryCode, new GridBagConstraints(0, 13, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblCountryCode, new GridBagConstraints(1, 13, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.vcbCountry, new GridBagConstraints(2, 13, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));
			add(this.lblCountry, new GridBagConstraints(5, 13, 3, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.txtCountryCode, new GridBagConstraints(1, 14, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 72, 4));
			add(this.txtCountry, new GridBagConstraints(5, 14, 3, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 4));
			
			add(this.vcbPhone1, new GridBagConstraints(0, 15, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblPhone1, new GridBagConstraints(1, 15, 7, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.txtPhone1, new GridBagConstraints(1, 16, 7, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 4));
			
			add(this.vcbPhone2, new GridBagConstraints(0, 17, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblPhone2, new GridBagConstraints(1, 17, 7, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.txtPhone2, new GridBagConstraints(1, 18, 7, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 4));
			
			add(this.vcbFax, new GridBagConstraints(0, 19, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblFax, new GridBagConstraints(1, 19, 7, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 211, 1));
			add(this.txtFax, new GridBagConstraints(1, 20, 7, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 4));
			
			add(this.vcbMobilePhone, new GridBagConstraints(0, 21, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblMobilePhone, new GridBagConstraints(1, 21, 7, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.txtMobilePhone, new GridBagConstraints(1, 22, 7, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 4));

			add(this.vcbEmail, new GridBagConstraints(0, 23, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblEmail, new GridBagConstraints(1, 23, 7, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.txtEmail, new GridBagConstraints(1, 24, 7, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 4));
			
			add(this.vcbHomepage, new GridBagConstraints(0, 25, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblHomepage, new GridBagConstraints(1, 25, 7, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.txtHomepage, new GridBagConstraints(1, 26, 7, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 4));
			
			add(this.vcbMiscInfo, new GridBagConstraints(0, 27, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblMiscInfo, new GridBagConstraints(1, 27, 7, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.scrollPaneMiscInfo, new GridBagConstraints(1, 28, 7, 5, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 60));

			this.scrollPaneMiscInfo.getViewport().add(this.txtMisc, null);
			
			ButtonGroup bgrp = new ButtonGroup();
			bgrp.add(this.radioStreet);
			bgrp.add(this.radioPostOfficeBox);

			cbxAddressType.addItem(Messages.getString("PanDBCAddress.type.office"));
			cbxAddressType.addItem(Messages.getString("PanDBCAddress.type.private"));
			cbxAddressType.addItem(Messages.getString("PanDBCAddress.type.seketary"));
			cbxAddressType.addItem(Messages.getString("PanDBCAddress.type.postAddress"));
			cbxAddressType.addItem(Messages.getString("PanDBCAddress.type.other"));
			setAllChecksEnabled(false);
		} catch (Exception exe) {
			log.error("Initialization problem", exe);
		}
	}

	/** @see de.juwimm.cms.content.panel.AbstractTreePanel#updateCheckHash() */
	public void updateCheckHash() {
		Hashtable<String, Integer> ht = new Hashtable<String, Integer>();
		ht.put("buildingLevel", vcbBuildingLevel.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("buildingNr", vcbBuildingNr.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("city", vcbCity.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("country", vcbCountry.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("countryCode", vcbCountryCode.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("email", vcbEmail.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("fax", vcbFax.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("homepage", vcbHomepage.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("misc", vcbMiscInfo.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("mobilePhone", vcbMobilePhone.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("phone1", vcbPhone1.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("phone2", vcbPhone2.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("postOfficeBox", vcbPostOfficeBox.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("roomNr", vcbRoomNr.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("street", vcbStreet.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("streetNr", vcbStreetNr.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("zipcode", vcbZipcode.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("addressType", vcbAddressType.isSelected() ? new Integer(1) : new Integer(0));
		setCheckHash(ht);
	}

	/** @see de.juwimm.cms.content.panel.AbstractTreePanel#setCheckHash(java.util.Hashtable) */
	public void setCheckHash(Hashtable ht) {
		super.setCheckHash(ht);
		this.vcbBuildingLevel.setSelected(getCheckValueForName("buildingLevel"));
		this.vcbBuildingNr.setSelected(getCheckValueForName("buildingNr"));
		this.vcbCity.setSelected(getCheckValueForName("city"));
		this.vcbCountry.setSelected(getCheckValueForName("country"));
		this.vcbCountryCode.setSelected(getCheckValueForName("countryCode"));
		this.vcbEmail.setSelected(getCheckValueForName("email"));
		this.vcbFax.setSelected(getCheckValueForName("fax"));
		this.vcbHomepage.setSelected(getCheckValueForName("homepage"));
		this.vcbMiscInfo.setSelected(getCheckValueForName("misc"));
		this.vcbMobilePhone.setSelected(getCheckValueForName("mobilePhone"));
		this.vcbPhone1.setSelected(getCheckValueForName("phone1"));
		this.vcbPhone2.setSelected(getCheckValueForName("phone2"));
		this.vcbPostOfficeBox.setSelected(getCheckValueForName("postOfficeBox"));
		this.vcbRoomNr.setSelected(getCheckValueForName("roomNr"));
		this.vcbStreet.setSelected(getCheckValueForName("street"));
		this.vcbStreetNr.setSelected(getCheckValueForName("streetNr"));
		this.vcbZipcode.setSelected(getCheckValueForName("zipcode"));
		this.vcbAddressType.setSelected(getCheckValueForName("addressType"));
		if (hasClicks()) {
			this.setAllChecksEnabled(true);
		}
		this.addressNode.setClicks(ht);
	}

	/** @see de.juwimm.cms.content.panel.AbstractTreePanel#setFieldsEditable(boolean) */
	public void setFieldsEditable(boolean editable) {
		txtRoomNr.setEditable(editable);
		txtBuildingLevel.setEditable(editable);
		txtBuildingNr.setEditable(editable);
		txtStreet.setEditable(editable);
		txtStreetNr.setEditable(editable);
		txtPostOfficeBox.setEditable(editable);
		txtCountryCode.setEditable(editable);
		txtCountry.setEditable(editable);
		txtZipcode.setEditable(editable);
		txtCity.setEditable(editable);
		txtPhone1.setEditable(editable);
		txtPhone2.setEditable(editable);
		txtMobilePhone.setEditable(editable);
		txtEmail.setEditable(editable);
		txtHomepage.setEditable(editable);
		txtMisc.setEditable(editable);
		txtFax.setEditable(editable);
		cbxAddressType.setEnabled(editable);
		txtPostOfficeBox.setEditable(editable);
		radioPostOfficeBox.setEnabled(editable);
		radioStreet.setEnabled(editable);
	}

	public void load(ComponentNode addressComponent) {
		if (!(addressComponent instanceof AddressNode)) return;
		this.addressNode = (AddressNode) addressComponent;
		AddressValue addressValue = this.addressNode.getData();
		setCheckHash(this.addressNode.getClicks());

		this.txtRoomNr.setText(addressValue.getRoomNr());
		this.txtBuildingLevel.setText(addressValue.getBuildingLevel());
		this.txtBuildingNr.setText(addressValue.getBuildingNr());
		this.txtStreet.setText(addressValue.getStreet());
		this.txtStreetNr.setText(addressValue.getStreetNr());
		this.txtPostOfficeBox.setText(addressValue.getPostOfficeBox());
		this.txtCountryCode.setText(addressValue.getCountryCode());
		this.txtCountry.setText(addressValue.getCountry());
		this.txtZipcode.setText(addressValue.getZipCode());
		this.txtCity.setText(addressValue.getCity());
		this.txtPhone1.setText(addressValue.getPhone1());
		this.txtPhone2.setText(addressValue.getPhone2());
		this.txtMobilePhone.setText(addressValue.getMobilePhone());
		this.txtEmail.setText(addressValue.getEmail());
		this.txtHomepage.setText(addressValue.getHomepage());
		this.txtMisc.setText(addressValue.getMisc());
		this.txtFax.setText(addressValue.getFax());

		this.cbxAddressType.setSelectedItem(addressValue.getAddressType());

		if (this.txtPostOfficeBox.getText().equals("")) {
			this.radioStreet.setSelected(true);
		} else {
			this.radioPostOfficeBox.setSelected(true);
		}
	}

	/** @see de.juwimm.cms.util.DBCDao#save() */
	public void save() {
		this.addressNode.setClicks(getCheckHash());
		AddressValue addressValue = this.addressNode.getData();

		Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
		addressValue.setRoomNr(this.txtRoomNr.getText());
		addressValue.setBuildingLevel(this.txtBuildingLevel.getText());
		addressValue.setBuildingNr(this.txtBuildingNr.getText());
		addressValue.setStreet(this.txtStreet.getText());
		addressValue.setStreetNr(this.txtStreetNr.getText());
		addressValue.setPostOfficeBox(this.txtPostOfficeBox.getText());
		addressValue.setCountryCode(this.txtCountryCode.getText());
		addressValue.setCountry(this.txtCountry.getText());
		addressValue.setZipCode(this.txtZipcode.getText());
		addressValue.setCity(this.txtCity.getText());
		addressValue.setPhone1(this.txtPhone1.getText());
		addressValue.setPhone2(this.txtPhone2.getText());
		addressValue.setMobilePhone(this.txtMobilePhone.getText());
		addressValue.setEmail(this.txtEmail.getText());
		addressValue.setHomepage(this.txtHomepage.getText());
		addressValue.setMisc(this.txtMisc.getText());
		addressValue.setFax(this.txtFax.getText());
		addressValue.setAddressType((String) cbxAddressType.getSelectedItem());
		try {
			comm.updateAddress(addressValue);
		} catch (Exception exe) {
			JOptionPane.showMessageDialog(this.getParent().getParent().getParent(), Messages.getString("PanDBCAddress.errorSaving") + exe.getMessage(), "CMS", JOptionPane.ERROR_MESSAGE);
			log.error(Messages.getString("PanDBCAddress.errorSaving") + addressValue.getAddressId(), exe);
		}
	}

	/** @see de.juwimm.cms.util.DBCDao#validateNode() */
	public String validateNode() {
		StringBuilder errorMessageBuilder = new StringBuilder();
		if (this.txtZipcode.getText() != null && this.txtZipcode.getText().length() > 50) {
			errorMessageBuilder.append(rb.getString("PanDBCAddress.error.zip.length"));
		}
		if (this.txtCountryCode.getText() != null && this.txtCountryCode.getText().length() > 3) {
			errorMessageBuilder.append(rb.getString("PanDBCAddress.error.countrycode.length"));
		}
		if (errorMessageBuilder.length() == 0) {
			return null;
		}
		return errorMessageBuilder.toString();
	}

}