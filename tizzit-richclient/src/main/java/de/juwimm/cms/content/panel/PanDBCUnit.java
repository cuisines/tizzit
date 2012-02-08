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

import org.apache.log4j.Logger;

import de.juwimm.cms.Messages;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.content.frame.DlgModalPicture;
import de.juwimm.cms.content.frame.tree.ComponentNode;
import de.juwimm.cms.content.frame.tree.UnitNode;
import de.juwimm.cms.content.panel.util.VisibilityCheckBox;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.DBCDao;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.UnitValue;

/**
 * A panel implementation that displays a unit and all of its values for the PersonDB.
 * 
 * <p>Title: juwimm cms</p>
 * <p>Description: content management system</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: juwi macmillan group gmbh</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanDBCUnit extends AbstractTreePanel implements DBCDao {
	
	private static Logger log = Logger.getLogger(PanDBCUnit.class);
	private UnitNode unitNode = null;
	
	private DlgModalPicture dlgModalPicture = null;
	private JLabel lblCaptionVisibility = new JLabel(UIConstants.DBC_VISIBILTY);
	private JLabel lblCaptionDB = new JLabel(rb.getString("PanDBC.component"));
	private JLabel lblName = new JLabel();
	private JLabel lblColour = new JLabel();
	private VisibilityCheckBox vcbName =  new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbImage = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbLogo = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbColour =  new VisibilityCheckBox(getCheckActionListener());
	private JTextField txtUnitName = new JTextField();
	private JLabel lblImage = new JLabel();
	private JTextField txtImage = new JTextField();
	private JButton btnChooseImage = new JButton();
	private JButton btnDeleteImage = new JButton();
	private JLabel lblLogo = new JLabel();
	private JTextField txtLogo = new JTextField();
	private JTextField txtUnitColour = new JTextField();
	private JButton btnChooseLogo = new JButton();
	private JButton btnDeleteLogo = new JButton();

	/**
	 * The default constructor initializes the instance.
	 */
	public PanDBCUnit() {
		try {
			setLayout(new GridBagLayout());
			this.lblName.setText(Messages.getString("PanDBCUnit.unitName"));
			this.txtUnitName.setDisabledTextColor(UIManager.getColor("TextArea.selectionBackground"));
			this.txtUnitName.setEditable(false);

			this.lblColour.setText(Messages.getString("PanDBCUnit.unitColour"));
			this.txtUnitColour.setDisabledTextColor(UIManager.getColor("TextArea.selectionBackground"));

			this.lblImage.setText(Messages.getString("PanDBCUnit.picture"));
			this.txtImage.setMinimumSize(new Dimension(80, 19));
			this.txtImage.setPreferredSize(new Dimension(80, 19));
			this.txtImage.setDisabledTextColor(UIManager.getColor("TextArea.selectionBackground"));
			this.txtImage.setEditable(false);
			this.btnChooseImage.setText(Messages.getString("dialog.choose"));
			this.btnChooseImage.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cmdImageActionPerformed(e);
				}
			});
			this.setMaximumSize(new Dimension(2147483647, 2147483647));
			this.setPreferredSize(new Dimension(300, 350));
			this.btnDeleteImage.setText(Messages.getString("dialog.delete"));
			this.btnDeleteImage.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnDeleteImageActionPerformed(e);
				}
			});
			this.lblLogo.setToolTipText("");
			this.lblLogo.setText(Messages.getString("PanDBCUnit.logo"));
			this.txtLogo.getDocument().addDocumentListener(getChangedDocumentListener());
			this.txtLogo.setDisabledTextColor(UIManager.getColor("TextArea.selectionBackground"));
			this.txtLogo.setEditable(false);
			this.txtLogo.setDisabledTextColor(UIManager.getColor("TextArea.selectionBackground"));
			this.txtLogo.setPreferredSize(new Dimension(80, 19));
			this.txtLogo.setMinimumSize(new Dimension(80, 19));
			this.btnChooseLogo.setText(Messages.getString("dialog.choose"));
			this.btnChooseLogo.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cmdLogoActionPerformed(e);
				}
			});
			this.btnDeleteLogo.setText(Messages.getString("dialog.delete"));
			this.btnDeleteLogo.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnDeleteLogoActionPerformed(e);
				}
			});
			this.txtImage.getDocument().addDocumentListener(getChangedDocumentListener());
			this.txtImage.setDisabledTextColor(UIManager.getColor("TextArea.selectionBackground"));
			this.txtImage.getDocument().addDocumentListener(getChangedDocumentListener());
			this.txtLogo.getDocument().addDocumentListener(getChangedDocumentListener());
			
			add(this.lblCaptionVisibility, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblCaptionDB, new GridBagConstraints(1, 0, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			JPanel horizontalRuler = new JPanel();
			horizontalRuler.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
			add(horizontalRuler, new GridBagConstraints(0, 1, 5, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
			
			add(this.vcbName, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblName, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 1));
			add(this.txtUnitName, new GridBagConstraints(1, 3, 3, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 7, 0, 24), 338, 4));
			
			add(this.vcbImage, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblImage, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.txtImage, new GridBagConstraints(1, 5, 1, 1, 0.5, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 4));
			add(this.btnChooseImage, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
			add(this.btnDeleteImage, new GridBagConstraints(3, 5, 1, 1, 0.5, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
			
			add(this.vcbLogo, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblLogo, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			add(this.txtLogo, new GridBagConstraints(1, 7, 1, 1, 0.5, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 4));
			add(this.btnChooseLogo, new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
			add(this.btnDeleteLogo, new GridBagConstraints(3, 7, 1, 1, 0.5, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
			
			add(this.vcbColour, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblColour, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 1));
			add(this.txtUnitColour, new GridBagConstraints(1, 9, 3, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 7, 0, 24), 338, 4));

		} catch (Exception exception) {
			log.error("Initialization problem", exception);
		}
	}

	/** @see de.juwimm.cms.content.panel.AbstractTreePanel#updateCheckHash() */
	public void updateCheckHash() {
		Hashtable<String, Integer> ht = new Hashtable<String, Integer>();
		ht.put("name", this.vcbName.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("image", this.vcbImage.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("logo", this.vcbLogo.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("colour", this.vcbColour.isSelected() ? new Integer(1) : new Integer(0));
		setCheckHash(ht);
	}

	/** @see de.juwimm.cms.content.panel.AbstractTreePanel#setCheckHash(java.util.Hashtable) */
	public void setCheckHash(Hashtable ht) {
		super.setCheckHash(ht);
		this.unitNode.setClicks(ht);
		this.vcbName.setSelected(getCheckValueForName("name"));
		this.vcbImage.setSelected(getCheckValueForName("image"));
		this.vcbLogo.setSelected(getCheckValueForName("logo"));
		this.vcbColour.setSelected(getCheckValueForName("colour"));
		if (hasClicks()) {
			this.setAllChecksEnabled(true);
		}
	}

	/** @see de.juwimm.cms.content.panel.AbstractTreePanel#setFieldsEditable(boolean) */
	public void setFieldsEditable(boolean editable) {
		this.btnChooseImage.setEnabled(editable);
		this.txtUnitColour.setEnabled(editable);
	}

	/**
	 * Handles the event user choosing the image.
	 * 
	 * @param e the {@link ActionEvent} that led to this routine 
	 */
	private void cmdImageActionPerformed(ActionEvent e) {
		try {
			dlgModalPicture = new DlgModalPicture(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					txtImage.setText(dlgModalPicture.getRootPanel().getPictureId().toString());
					fireChangeListener();
				}
			});
			if (!this.txtImage.getText().equals("") && !this.txtImage.getText().equals("0")) {
				int picid = new Integer(this.txtImage.getText()).intValue();
				dlgModalPicture.getRootPanel().setPictureId(picid);
			}
		} catch (Exception exe) {
		}
		dlgModalPicture.getRootPanel().hidePictureMessage();
		dlgModalPicture.setVisible(true);
	}

	/**
	 * Handles the event user deleting the image.
	 * 
	 * @param e  the {@link ActionEvent} that led to this routine
	 */
	private void btnDeleteImageActionPerformed(ActionEvent e) {
		this.txtImage.setText(new String());
		fireChangeListener();
	}

	/**
	 * Handles the event user choosing the logo.
	 * 
	 * @param e the {@link ActionEvent} that led to this routine
	 */
	private void cmdLogoActionPerformed(ActionEvent e) {
		try {
			dlgModalPicture = new DlgModalPicture(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					txtLogo.setText(dlgModalPicture.getRootPanel().getPictureId().toString());
					fireChangeListener();
				}
			});
			if (!this.txtLogo.getText().equals("") && !this.txtLogo.getText().equals("0")) {
				int picid = new Integer(this.txtLogo.getText()).intValue();
				dlgModalPicture.getRootPanel().setPictureId(picid);
			}
		} catch (Exception exe) {
		}
		dlgModalPicture.getRootPanel().hidePictureMessage();
		dlgModalPicture.setVisible(true);
	}

	/**
	 * Handles the event the user deleting the logo.
	 * 
	 * @param e the {@link ActionEvent} that led to this routine
	 */
	private void btnDeleteLogoActionPerformed(ActionEvent e) {
		this.txtLogo.setText("");
		fireChangeListener();
	}

	/** @see de.juwimm.cms.util.DBCDao#load(de.juwimm.cms.content.frame.tree.ComponentNode) */
	public void load(ComponentNode unitComponent) {
		if (!(unitComponent instanceof UnitNode)) return;
		this.unitNode = (UnitNode) unitComponent;
		UnitValue unitValue = this.unitNode.getUnitValue();
		setCheckHash(this.unitNode.getClicks());

		this.txtUnitName.setText(unitValue.getName());
		if (unitValue.getImageId() == null) {
			this.txtImage.setText(new String());
		} else {
			this.txtImage.setText(Integer.toString(unitValue.getImageId()));
		}
		if (unitValue.getLogoId() == null) {
			this.txtLogo.setText(new String());
		} else {
			this.txtLogo.setText(Integer.toString(unitValue.getLogoId()));
		}
		if (unitValue.getColour() == null) {
			this.txtUnitColour.setText(new String());
		} else {
			this.txtUnitColour.setText(unitValue.getColour());
		}
	}

	/** @see de.juwimm.cms.util.DBCDao#save() */
	public void save() {
		this.unitNode.setClicks(getCheckHash());
		UnitValue unitValue = this.unitNode.getUnitValue();
		boolean changed = false;
		
		String content = this.txtUnitName.getText();
		if (content == null || content.equals("")) {
			if (unitValue.getName() != null) {
				changed = true;
				unitValue.setName(null);
			}
		} else {
			if (!content.equals(unitValue.getName())) {
				changed = true;
				unitValue.setName(content);
			}
		}
		content = this.txtImage.getText();
		if (content == null || content.equals("")) {
			if (unitValue.getImageId() != null) {
				changed = true;
				unitValue.setImageId(null);
			}
		} else {
			Integer imageId = new Integer(content);
			if (unitValue.getImageId() == null || !(unitValue.getImageId().equals(imageId))) {
				changed = true;
				unitValue.setImageId(imageId);
			} 
		}
		content = this.txtLogo.getText();
		if (content == null || content.equals("")) {
			if (unitValue.getLogoId() != null) {
				changed = true;
				unitValue.setLogoId(null);
			}
		} else {
			Integer logoId = new Integer(content);
			if (unitValue.getLogoId() == null || !(unitValue.getLogoId().equals(logoId ))) {
				changed = true;
				unitValue.setLogoId(logoId);
			}
		}
		content = this.txtUnitColour.getText();
		if (content == null || content.equals("")) {
			if (unitValue.getColour() != null) {
				changed = true;
				unitValue.setColour(null);
			}
		} else {
			if (!content.equals(unitValue.getColour())) {
				changed = true;
				unitValue.setColour(content);
			}
		}

		if (changed) {
			try {
				Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
				comm.updateUnit(unitValue);
			} catch (Exception exe) {
				JOptionPane.showMessageDialog(this.getParent().getParent().getParent(), Messages.getString("PanDBCUnit.errorSaving") + exe.getMessage(),
						"CMS",
						JOptionPane.ERROR_MESSAGE);
				log.error(Messages.getString("PanDBCUnit.errorSaving"), exe);
			}
		}
	}

	/** @see de.juwimm.cms.util.DBCDao#validateNode() */
	public String validateNode() {
		return null;
	}

}
