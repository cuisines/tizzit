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
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;

import org.apache.log4j.Logger;

import de.juwimm.cms.Messages;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.components.vo.PersonValue;
import de.juwimm.cms.content.frame.DlgModalPicture;
import de.juwimm.cms.content.frame.tree.ComponentNode;
import de.juwimm.cms.content.frame.tree.PersonNode;
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
public class PanDBCPerson extends AbstractTreePanel implements DBCDao {
	private static Logger log = Logger.getLogger(PanDBCPerson.class);
	private static final String LIST_TYPE = "list";
	private static final String NORMAL_TYPE = "normal";
	private static final String FLOW_TYPE = "flow";
	private PersonNode personNode = null;
	
	private DlgModalPicture frame = new DlgModalPicture(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
			txtImage.setText(frame.getRootPanel().getPictureId().toString());
		}
	});
	private JLabel lblCaptionVisibility = new JLabel(UIConstants.DBC_VISIBILTY);
	private JLabel lblCaptionDB = new JLabel(rb.getString("PanDBC.component"));
	private JLabel lblFirstName = new JLabel();
	private JLabel lblLastName = new JLabel();
	private JLabel lblOrderPosition = new JLabel();
	private JLabel lblSalutation = new JLabel();
	private JLabel lblImage = new JLabel();
	private JLabel lblJobTitle = new JLabel();
	private JLabel lblCountryJob = new JLabel();
	private JLabel lblMedicalAssociation = new JLabel();
	private JLabel lblLinkMedicalAssociation = new JLabel();
	private JLabel lpbPosition = new JLabel();
	private JTextField txtFirstName = new JTextField();
	private JTextField txtLastName = new JTextField();
	private PanSimpleDate txtBirthDay = new PanSimpleDate();
	private JTextField txtImage = new JTextField();
	private JComboBox cbxSalutation = new JComboBox();
	private JTextField txtJobTitle = new JTextField();
	private JTextField txtPosition = new JTextField();
	private JTextField txtLinkMedicalAssociation = new JTextField();
	private JTextField txtMedicalAssociation = new JTextField();
	private JTextField txtCountryJob = new JTextField();
	private JSlider sliderOrderPosition = new JSlider();
	private VisibilityCheckBox vcbSalutation = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbTitle = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbFirstName = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbLastName = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbBirthDay = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbJobTitle = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbPosition = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbCountryJob = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbMedicalAssociation = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbLinkMedicalAssociation = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbImage = new VisibilityCheckBox(getCheckActionListener());
	private JTextField txtTitle = new JTextField();
	
	private JLabel lblTitle = new JLabel();
	private JLabel lblViewType = new JLabel();
	private JComboBox cbxViewType = new JComboBox();
	private JButton cmdImage = new JButton();
	private JLabel lblImportant = new JLabel();
	private JLabel lblLessImportant = new JLabel();
	private JLabel lblBirthday = new JLabel();
	private JButton btnDeleteImage = new JButton();
	
	/**
	 * The default constructor initializes the instance.
	 */
	public PanDBCPerson() {
		try {
			this.setLayout(new GridBagLayout());
			this.lblFirstName.setText(Messages.getString("PanDBCPerson.firstname"));
			this.lblLastName.setText(Messages.getString("PanDBCPerson.lastname"));
			this.lblOrderPosition.setText(Messages.getString("PanDBCPerson.orderPosition"));
			this.lblSalutation.setText(Messages.getString("PanDBCPerson.salutation"));
			this.lblImage.setText(Messages.getString("PanDBCPerson.picture"));
			this.lblJobTitle.setText(Messages.getString("PanDBCPerson.jobTitle"));
			this.lblCountryJob.setText(Messages.getString("PanDBCPerson.countryJob"));
			this.lblMedicalAssociation.setText(Messages.getString("PanDBCPerson.medicalAssociation"));
			this.lblLinkMedicalAssociation.setText(Messages.getString("PanDBCPerson.linkMedicalAssociation"));
			this.lpbPosition.setText(Messages.getString("PanDBCPerson.position"));
			this.txtFirstName.getDocument().addDocumentListener(getChangedDocumentListener());
			this.txtLastName.getDocument().addDocumentListener(getChangedDocumentListener());
			//txtBirthDay.getDocument().addDocumentListener(getChangedDocumentListener());
			this.txtBirthDay.setPreferredSize(new Dimension(250, 53));
			this.txtBirthDay.setDateButtonText(Messages.getString("PanDBCPerson.dateButton"));
			this.txtBirthDay.addDocumentListener(getChangedDocumentListener());
			this.txtImage.getDocument().addDocumentListener(getChangedDocumentListener());
			this.txtTitle.getDocument().addDocumentListener(getChangedDocumentListener());
			this.cbxSalutation.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fireChangeListener();
				}
			});

			this.cbxViewType.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (personNode != null) {
						personNode.setViewType(getViewType());
					}
				}
			});
			this.txtJobTitle.getDocument().addDocumentListener(getChangedDocumentListener());
			this.txtPosition.getDocument().addDocumentListener(getChangedDocumentListener());
			this.txtLinkMedicalAssociation.getDocument().addDocumentListener(getChangedDocumentListener());
			this.txtMedicalAssociation.getDocument().addDocumentListener(getChangedDocumentListener());
			this.sliderOrderPosition.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					fireChangeListener();
				}
			});
			this.lblTitle.setText(Messages.getString("PanDBCPerson.title"));
			this.lblViewType.setText(Messages.getString("PanDBCPerson.viewType"));
			this.txtImage.setMinimumSize(new Dimension(80, 19));
			this.txtImage.setPreferredSize(new Dimension(80, 19));
			this.txtImage.setDisabledTextColor(UIManager.getColor("TextArea.selectionBackground"));
			this.txtImage.setEditable(false);
			this.cmdImage.setText(Messages.getString("dialog.choose"));
			this.cmdImage.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cmdImageActionPerformed(e);
				}
			});
			this.lblImportant.setFont(new java.awt.Font("Dialog", 1, 12));
			this.lblImportant.setText(Messages.getString("PanDBCPerson.important"));
			this.lblLessImportant.setFont(new java.awt.Font("Dialog", 1, 12));
			this.lblLessImportant.setText(Messages.getString("PanDBCPerson.lessImportant"));
			this.lblBirthday.setText(Messages.getString("PanDBCPerson.birthDate"));
			this.cbxViewType.setAlignmentX((float) 4.0);
			this.btnDeleteImage.setText(Messages.getString("dialog.delete"));
			this.btnDeleteImage.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnDeleteImageActionPerformed(e);
				}
			});

			this.cbxSalutation.addItem(Messages.getString("PanDBCPerson.saluationMs"));
			this.cbxSalutation.addItem(Messages.getString("PanDBCPerson.salutationMr"));

			add(this.lblCaptionVisibility, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblCaptionDB, new GridBagConstraints(1, 0, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			JPanel horizontalRuler = new JPanel();
			horizontalRuler.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
			add(horizontalRuler, new GridBagConstraints(0, 1, 5, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
			
			add(this.lblViewType, new GridBagConstraints(1, 2, 3, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 3, 0), 0, 0));
			add(this.cbxViewType, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 20, 3));

			add(this.vcbSalutation, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblSalutation, new GridBagConstraints(1, 4, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.cbxSalutation, new GridBagConstraints(1, 5, 4, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 20, 3));

			add(this.vcbTitle, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblTitle, new GridBagConstraints(1, 6, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.txtTitle, new GridBagConstraints(1, 7, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 25), 0, 4));
			
			add(this.vcbLastName, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblLastName, new GridBagConstraints(1, 8, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.txtLastName, new GridBagConstraints(1, 9, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 25), 0, 4));
			
			add(this.vcbFirstName, new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblFirstName, new GridBagConstraints(1, 10, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.txtFirstName, new GridBagConstraints(1, 11, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 25), 0, 4));
			
			add(this.vcbBirthDay, new GridBagConstraints(0, 12, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblBirthday, new GridBagConstraints(1, 12, 3, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.txtBirthDay, new GridBagConstraints(1, 13, 3, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
			
			add(this.vcbPosition, new GridBagConstraints(0, 14, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lpbPosition, new GridBagConstraints(1, 14, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.txtPosition, new GridBagConstraints(1, 15, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 25), 0, 4));
			
			add(this.vcbJobTitle, new GridBagConstraints(0, 16, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblJobTitle, new GridBagConstraints(1, 16, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.txtJobTitle, new GridBagConstraints(1, 17, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 25), 0, 4));
			
			add(this.vcbCountryJob, new GridBagConstraints(0, 18, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblCountryJob, new GridBagConstraints(1, 18, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.txtCountryJob, new GridBagConstraints(1, 19, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 25), 0, 4));
			
			add(this.vcbMedicalAssociation, new GridBagConstraints(0, 20, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblMedicalAssociation, new GridBagConstraints(1, 20, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.txtMedicalAssociation, new GridBagConstraints(1, 21, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 25), 0, 4));
			
			add(this.vcbLinkMedicalAssociation, new GridBagConstraints(0, 22, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblLinkMedicalAssociation, new GridBagConstraints(1, 22, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.txtLinkMedicalAssociation, new GridBagConstraints(1, 23, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 25), 0, 4));
			
			add(this.vcbImage, new GridBagConstraints(0, 24, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblImage, new GridBagConstraints(1, 24, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.txtImage, new GridBagConstraints(1, 25, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 20, 4));
			add(this.cmdImage, new GridBagConstraints(2, 25, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
			add(this.btnDeleteImage, new GridBagConstraints(3, 25, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
			
			add(this.lblOrderPosition, new GridBagConstraints(1, 26, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 3, 0), 0, 0));
			add(this.sliderOrderPosition, new GridBagConstraints(1, 27, 4, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 25), 0, 0));
			add(this.lblImportant, new GridBagConstraints(1, 28, 1, 1, 0.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 10, 0), 0, 0));
			add(this.lblLessImportant, new GridBagConstraints(2, 28, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 10, 25), 0, 0));
			
			this.sliderOrderPosition.setSnapToTicks(true);
			this.sliderOrderPosition.setPaintTrack(true);
			this.sliderOrderPosition.setMinimum(1);
			this.sliderOrderPosition.setMaximum(10);
			this.sliderOrderPosition.setMajorTickSpacing(1);
			this.sliderOrderPosition.setMinorTickSpacing(1);
			this.sliderOrderPosition.setPaintTicks(true);
			this.sliderOrderPosition.setPaintLabels(true);
			
			this.cbxViewType.addItem(Messages.getString("PanDBCPerson.viewType.list"));
			this.cbxViewType.addItem(Messages.getString("PanDBCPerson.viewType.normal"));
			this.cbxViewType.addItem(Messages.getString("PanDBCPerson.viewType.flow"));
			this.cbxViewType.setSelectedIndex(1);
			setAllChecksEnabled(false);
		} catch (Exception exe) {
			log.error("Initialization problem", exe);
		}
	}


	/** @see de.juwimm.cms.content.panel.AbstractTreePanel#updateCheckHash() */
	public void updateCheckHash() {
		Hashtable<String, Integer> ht = new Hashtable<String, Integer>();
		ht.put("birthDay", this.vcbBirthDay.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("countryJob", this.vcbCountryJob.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("firstname", this.vcbFirstName.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("image", this.vcbImage.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("job", this.vcbPosition.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("jobTitle", this.vcbJobTitle.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("lastname", this.vcbLastName.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("linkMedicalAssociation", this.vcbLinkMedicalAssociation.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("medicalAssociation", this.vcbMedicalAssociation.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("salutation", this.vcbSalutation.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("title", this.vcbTitle.isSelected() ? new Integer(1) : new Integer(0));
		setCheckHash(ht);
	}

	/** @see de.juwimm.cms.content.panel.AbstractTreePanel#setCheckHash(java.util.Hashtable) */
	public void setCheckHash(Hashtable ht) {
		super.setCheckHash(ht);
		this.vcbBirthDay.setSelected(getCheckValueForName("birthDay"));
		this.vcbCountryJob.setSelected(getCheckValueForName("countryJob"));
		this.vcbFirstName.setSelected(getCheckValueForName("firstname"));
		this.vcbImage.setSelected(getCheckValueForName("image"));
		this.vcbPosition.setSelected(getCheckValueForName("job"));
		this.vcbJobTitle.setSelected(getCheckValueForName("jobTitle"));
		this.vcbTitle.setSelected(getCheckValueForName("title"));
		this.vcbLastName.setSelected(getCheckValueForName("lastname"));
		this.vcbLinkMedicalAssociation.setSelected(getCheckValueForName("linkMedicalAssociation"));
		this.vcbMedicalAssociation.setSelected(getCheckValueForName("medicalAssociation"));
		this.vcbSalutation.setSelected(getCheckValueForName("salutation"));
		if (hasClicks()) {
			this.setAllChecksEnabled(true);
		} else {
			this.setAllChecksEnabled(false);
		}
		this.personNode.setClicks(ht);
	}

	/** @see de.juwimm.cms.content.panel.AbstractTreePanel#setFieldsEditable(boolean) */
	public void setFieldsEditable(boolean editable) {
		cbxSalutation.setEnabled(editable);
		txtFirstName.setEditable(editable);
		txtTitle.setEditable(editable);
		txtLastName.setEditable(editable);
		sliderOrderPosition.setEnabled(editable);
		txtBirthDay.setDateButtonEnabled(editable);
		txtCountryJob.setEditable(editable);
		txtPosition.setEditable(editable);
		txtJobTitle.setEditable(editable);
		txtMedicalAssociation.setEditable(editable);
		txtLinkMedicalAssociation.setEditable(editable);
		this.cmdImage.setEnabled(editable);
	}

	/**
	 * Sets the combobox to the specified listtype.
	 * 
	 * @param listtype the list type to set
	 */
	public void setViewType(String listtype) {
		if (listtype.equals(LIST_TYPE)) {
			this.cbxViewType.setSelectedIndex(0);
		} else if (listtype.equals(NORMAL_TYPE)) {
			this.cbxViewType.setSelectedIndex(1);
		} else if (listtype.equals(FLOW_TYPE)) {
			this.cbxViewType.setSelectedIndex(2);
		}
		// Fallback: old algortithm:
		else if (listtype.equals("Listenform")) {
			this.cbxViewType.setSelectedIndex(0);
		} else if (listtype.equals("Normal")) {
			this.cbxViewType.setSelectedIndex(1);
		} else if (listtype.equals("Fliesstext")) {
			this.cbxViewType.setSelectedIndex(2);
		}
		// default:
		else {
			this.cbxViewType.setSelectedIndex(1);
		}
	}

	/**
	 * Returns the list type the user chose from the combobx.
	 * 
	 * @return the list type
	 */
	public String getViewType() {
		switch (this.cbxViewType.getSelectedIndex()) {
			case 0: return LIST_TYPE;
			case 1: return NORMAL_TYPE;
			case 2: return FLOW_TYPE;
			default: return NORMAL_TYPE;
		}
//		return ((String) this.cbxViewType.getSelectedItem());
	}

	/**
	 * Handles the event user choosing the image.
	 * 
	 * @param e the {@link ActionEvent} that led to this routine 
	 */
	void cmdImageActionPerformed(ActionEvent e) {
		try {
			if (!this.txtImage.getText().equals("") && !this.txtImage.getText().equals("0")) {
				int picid = new Integer(this.txtImage.getText()).intValue();
				frame.getRootPanel().setPictureId(picid);
			}
		} catch (Exception exe) {
		}
		frame.getRootPanel().hidePictureMessage();
		frame.setVisible(true);
	}

	/**
	 * Handles the event user deleting the image
	 * 
	 * @param e the {@link ActionEvent} that led to this routine 
	 */
	void btnDeleteImageActionPerformed(ActionEvent e) {
		this.txtImage.setText(new String());
	}

	/** @see de.juwimm.cms.util.DBCDao#load(de.juwimm.cms.content.frame.tree.ComponentNode) */
	public void load(ComponentNode personComponent) {
		if (!(personComponent instanceof PersonNode)) return;
		this.personNode = (PersonNode) personComponent;
		PersonValue personValue = personNode.getPersonValue();
		setCheckHash(this.personNode.getClicks());
		setViewType(this.personNode.getViewType());

		cbxSalutation.setSelectedItem(personValue.getSalutation());
		txtFirstName.setText(personValue.getFirstname());
		txtTitle.setText(personValue.getTitle());
		txtLastName.setText(personValue.getLastname());
		sliderOrderPosition.setValue(personValue.getPosition());
		if (personValue.getBirthDay() != null) {
			txtBirthDay.setDateTextField(personValue.getBirthDay());
		} else {
			txtBirthDay.setDateTextField(new String());
		}
		txtCountryJob.setText(personValue.getCountryJob());
		txtPosition.setText(personValue.getJob());
		txtJobTitle.setText(personValue.getJobTitle());
		txtMedicalAssociation.setText(personValue.getMedicalAssociation());
		txtLinkMedicalAssociation.setText(personValue.getLinkMedicalAssociation());

		if (personValue.getImageId() == null) {
			txtImage.setText("");
		} else {
			txtImage.setText(Integer.toString(personValue.getImageId()));
		}
	}

	/** @see de.juwimm.cms.util.DBCDao#save() */
	public void save() {
		this.personNode.setViewType(getViewType());
		this.personNode.setClicks(getCheckHash());
		PersonValue personValue = this.personNode.getPersonValue();
		boolean changed = false;
		
		String content = (String) this.cbxSalutation.getSelectedItem();
		if (!personValue.getSalutation().equals(content)) {
			changed = true;
			personValue.setSalutation(content);	
		}
		content = this.txtTitle.getText();
		if (content == null || content.trim().equals("")) {
			if (personValue.getTitle() != null) {
				changed = true;
				personValue.setTitle(content);
			}
		} else {
			if (personValue.getTitle() == null || !(personValue.getTitle().equals(content))) {
				changed = true;
				personValue.setTitle(content);
			}
		}
		content = this.txtFirstName.getText();
		if (content == null || content.trim().equals("")) {
			if (personValue.getFirstname() != null) {
				changed = true;
				personValue.setFirstname(null);
			}
		} else {
			if (personValue.getFirstname() == null || !(personValue.getFirstname().equals(content))) {
				changed = true;
				personValue.setFirstname(content);
			}
		}
		content = this.txtLastName.getText();
		if (content == null || content.trim().equals("")) {
			if (personValue.getLastname() != null) {
				changed = true;
				personValue.setLastname(null);
			}
		} else {
			if (personValue.getLastname() == null || !(personValue.getLastname().equals(content))) {
				changed = true;
				personValue.setLastname(content);
			}
		}
		content = this.txtJobTitle.getText();
		if (content == null || content.trim().equals("")) {
			if (personValue.getJobTitle() != null) {
				changed = true;
				personValue.setJobTitle(null);
			}
		} else {
			if (personValue.getJobTitle() == null || !(personValue.getJobTitle().equals(content))) {
				changed = true;
				personValue.setJobTitle(content);
			}
		}
		int intValue = this.sliderOrderPosition.getValue();
		if (intValue != personValue.getPosition()) {
			changed = true;
			personValue.setPosition(intValue);
		}
		content = this.txtBirthDay.getDateTextField();
		if (content == null || content.trim().equals("")) {
			if (personValue.getBirthDay() != null) {
				changed = true;
				personValue.setBirthDay(null);
			}
		} else {
			if (personValue.getBirthDay() == null || !(personValue.getBirthDay().equals(content))) {
				changed = true;
				personValue.setBirthDay(content);
			}
		}
		content = this.txtPosition.getText();
		if (content == null || content.trim().equals("")) {
			if (personValue.getJob() != null) {
				changed = true;
				personValue.setJob(null);
			}
		} else {
			if (personValue.getJob() == null || !(personValue.getJob().equals(content))) {
				changed = true;
				personValue.setJob(content);
			}
		}
		content = this.txtImage.getText();
		if (content == null || content.trim().equals("")) {
			if (personValue.getImageId() != null && personValue.getImageId() != 0) {
				changed = true;
				personValue.setImageId(0);
			}
		} else {
			intValue = Integer.parseInt(content);
			if (personValue.getImageId() != intValue) {
				changed = true;
				personValue.setImageId(intValue);
			}
		}
		// ******************************************************************************
		content = this.txtCountryJob.getText();
		if (content == null || content.trim().equals("")) {
			if (personValue.getCountryJob() != null) {
				changed = true;
				personValue.setCountryJob(null);
			}
		} else {
			if (personValue.getCountryJob() == null || !(personValue.getCountryJob().equals(content))) {
				changed = true;
				personValue.setCountryJob(content);
			}
		}
		content = this.txtMedicalAssociation.getText();
		if (content == null || content.trim().equals("")) {
			if (personValue.getMedicalAssociation() != null) {
				changed = true;
				personValue.setMedicalAssociation(null);
			}
		} else {
			if (personValue.getMedicalAssociation() == null || !(personValue.getMedicalAssociation().equals(content))) {
				changed = true;
				personValue.setMedicalAssociation(content);
			}
		}
		content = this.txtLinkMedicalAssociation.getText();
		if (content == null || content.trim().equals("")) {
			if (personValue.getLinkMedicalAssociation() != null) {
				changed = true;
				personValue.setLinkMedicalAssociation(null);
			}
		} else {
			if (personValue.getLinkMedicalAssociation() == null || !(personValue.getLinkMedicalAssociation().equals(content))) {
				changed = true;
				personValue.setLinkMedicalAssociation(content);
			}
		}
		// ******************************************************************************
		
		if (changed) {
			Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
			try {
				comm.updatePerson(personValue);
			} catch (Exception exe) {
				JOptionPane.showMessageDialog(this.getParent().getParent().getParent(), Messages.getString("PanDBCPerson.errorSaving") + exe.getMessage(),
						Messages.getString("dialog.title"),
						JOptionPane.ERROR_MESSAGE);
				log.error(Messages.getString("PanDBCPerson.errorSaving") + personValue.getLastname(), exe);
			}
		}
	}

	/** @see de.juwimm.cms.util.DBCDao#validateNode() */
	public String validateNode() {
		StringBuilder errorMessageBuilder = new StringBuilder();
		if (this.txtPosition.getText().length() > 100) {
			errorMessageBuilder.append(rb.getString("PanDBCPerson.error.job.length"));
		}
		if (errorMessageBuilder.length() == 0) {
			return null;
		}
		return errorMessageBuilder.toString();
	}

}