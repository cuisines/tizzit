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
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;

import org.apache.log4j.Logger;

import de.juwimm.cms.Messages;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.components.vo.DepartmentValue;
import de.juwimm.cms.content.frame.tree.ComponentNode;
import de.juwimm.cms.content.frame.tree.DepartmentNode;
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
public class PanDBCDepartment extends AbstractTreePanel implements DBCDao {
	private static Logger log = Logger.getLogger(PanDBCDepartment.class);
	
	private DepartmentNode departmentNode;
	private JLabel lblCaptionVisibility = new JLabel(UIConstants.DBC_VISIBILTY);
	private JLabel lblCaptionDB = new JLabel(rb.getString("PanDBC.component"));
	private JLabel lblDepartmentName = new JLabel();
	private VisibilityCheckBox vcbName = new VisibilityCheckBox(getCheckActionListener());
	private JTextField txtName = new JTextField();

	public PanDBCDepartment() {
		try {
			setLayout(new GridBagLayout());
			setPreferredSize(new Dimension(300, 350));
			
			this.lblDepartmentName.setText(Messages.getString("PanDBCDepartment.departmentName"));
			this.txtName.getDocument().addDocumentListener(getChangedDocumentListener());
			
			add(this.lblCaptionVisibility, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblCaptionDB, new GridBagConstraints(1, 0, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			JPanel horizontalRuler = new JPanel();
			horizontalRuler.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
			add(horizontalRuler, new GridBagConstraints(0, 1, 5, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
			
			add(this.vcbName, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 13, 0, 0), -3, -2));
			add(this.lblDepartmentName, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(20, 9, 0, 78), 54, 1));
			add(this.txtName, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(7, 10, 20, 10), 274, 4));
			setAllChecksEnabled(false);
		} catch (Exception exe) {
			log.error("Initialization problem", exe);
		}
	}

	/** @see de.juwimm.cms.content.panel.AbstractTreePanel#updateCheckHash() */
	public void updateCheckHash() {
		Hashtable<String, Integer> ht = new Hashtable<String, Integer>();
		ht.put("name", vcbName.isSelected() ? new Integer(1) : new Integer(0));
		setCheckHash(ht);
	}

	/** @see de.juwimm.cms.content.panel.AbstractTreePanel#setCheckHash(java.util.Hashtable) */
	public void setCheckHash(Hashtable ht) {
		super.setCheckHash(ht);
		this.vcbName.setSelected(getCheckValueForName("name"));
		if (hasClicks()) {
			this.setAllChecksEnabled(true);
		}
	}

	/** @see de.juwimm.cms.content.panel.AbstractTreePanel#setFieldsEditable(boolean) */
	public void setFieldsEditable(boolean editable) {
		txtName.setEditable(editable);
	}
	
	/** @see de.juwimm.cms.util.DBCDao#load(de.juwimm.cms.content.frame.tree.ComponentNode) */
	public void load(ComponentNode departmentComponent) {
		if (! (departmentComponent instanceof DepartmentNode)) return;
		this.departmentNode = (DepartmentNode) departmentComponent;
		DepartmentValue departmentValue = this.departmentNode.getDepartmentValue();
		
		txtName.setText(departmentValue.getName());
	}

	/** @see de.juwimm.cms.util.DBCDao#save() */
	public void save() {
		this.departmentNode.setClicks(getCheckHash());
		DepartmentValue departmentValue = this.departmentNode.getDepartmentValue();
		setCheckHash(this.departmentNode.getClicks());
		
		Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
		departmentValue.setName(this.txtName.getText());
		try {
			comm.updateDepartment(departmentValue);
		} catch (Exception exe) {
			JOptionPane.showMessageDialog(this.getParent().getParent().getParent(), Messages
					.getString("PanDBCDepartment.errorSaving") + exe.getMessage(),
					"CMS",
					JOptionPane.ERROR_MESSAGE);
			log.error(Messages.getString("PanDBCDepartment.errorSaving"), exe);
		}
	}

	/** @see de.juwimm.cms.util.DBCDao#validateNode() */
	public String validateNode() {
		return null;
	}
	
	
}