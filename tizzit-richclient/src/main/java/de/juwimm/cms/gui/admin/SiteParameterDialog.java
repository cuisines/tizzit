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
package de.juwimm.cms.gui.admin;

import static de.juwimm.cms.common.Constants.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import de.juwimm.cms.util.ConfigReader;
import de.juwimm.cms.util.Parameter;
import de.juwimm.cms.util.Parameters;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.util.XercesHelper;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class SiteParameterDialog extends JDialog {
	private static Logger log = Logger.getLogger(SiteParameterDialog.class);
	private JScrollPane jScrollPane1 = new JScrollPane();
	private JList lstParameter = new JList();
	private JButton btnSave = new JButton(UIConstants.BTN_OK);
	private JButton btnCancel = new JButton(UIConstants.BTN_CLOSE);
	private JPanel panOptions = new JPanel();
	private JCheckBox chkSelectFeature = new JCheckBox();
	private JTextField txtDescribeFeature = new JTextField();
	private JLabel lblProsa = new JLabel();
	private DefaultListModel lstModel = new DefaultListModel();
	private ListItem previousSelected = null;

	public SiteParameterDialog() {
		super(UIConstants.getMainFrame(), true);
		try {
			jbInit();
			btnSave.setText(rb.getString("dialog.ok"));
			btnCancel.setText(rb.getString("dialog.cancel"));
			lblProsa.setText(rb.getString("panel.sitesAdministration.prosa"));
			chkSelectFeature.setText(rb.getString("panel.sitesAdministration.chkEnabled"));
			lstParameter.setModel(lstModel);
			lstParameter.setCellRenderer(new ParameterCellRenderer());
			lstParameter.addListSelectionListener(new ParameterSelectListener());
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	private void jbInit() throws Exception {
		this.getContentPane().setLayout(new GridBagLayout());
		btnSave.setText("Save");
		btnSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSaveActionPerformed(e);
			}
		});
		btnCancel.setText("Cancel");
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCancelActionPerformed(e);
			}
		});
		panOptions.setBorder(BorderFactory.createEtchedBorder());
		panOptions.setLayout(new GridBagLayout());
		chkSelectFeature.setText("select feature");
		lblProsa.setText("Parametrize Site functionality");
		txtDescribeFeature.setText("");
		this.getContentPane().add(jScrollPane1, new GridBagConstraints(0, 1, 2, 1, 0.6, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 10, 0, 10), 0, 0));
		this.getContentPane().add(btnSave, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		this.getContentPane().add(btnCancel, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		this.getContentPane().add(panOptions, new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 0, 10), 0, 0));
		panOptions.add(txtDescribeFeature, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		panOptions.add(chkSelectFeature, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		this.getContentPane().add(lblProsa, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		jScrollPane1.getViewport().add(lstParameter);
		super.getRootPane().setDefaultButton(btnSave);
	}

	public void load(ConfigReader cfgReader) {
		lstModel.clear();
		reset();
		Parameter[] params = Parameters.getAvailableSiteParameter();
		for (int i = 0; i < params.length; i++) {
			int[] avParamTypes = params[i].getParameterTypes();
			Hashtable<Integer, Object> customValues = new Hashtable<Integer, Object>();
			for (int j = 0; j < avParamTypes.length; j++) {
				Object defaultVal = null;
				if (cfgReader != null) {
					try {
						defaultVal = cfgReader.getConfigNodeValueWithNull("parameters/" + params[i].getId() + "_" + avParamTypes[j]);
					} catch (Exception exe) {
					}
				}
				if (defaultVal == null) {
					defaultVal = params[i].getDefaultValue(avParamTypes[j]);
				}
				customValues.put(new Integer(avParamTypes[j]), defaultVal);
			}
			lstModel.addElement(new ListItem(params[i], customValues));
		}
	}

	public void save(Element node) {
		Enumeration enume = lstModel.elements();
		while (enume.hasMoreElements()) {
			ListItem li = (ListItem) enume.nextElement();
			Iterator itCustVal = li.getCustomValues().keySet().iterator();
			while (itCustVal.hasNext()) {
				Integer paramType = (Integer) itCustVal.next();
				Object val = li.getCustomValues().get(paramType);
				XercesHelper.createTextNode(node, li.getParam().getId() + "_" + paramType, val.toString());
			}
		}
	}

	public void reset() {
		chkSelectFeature.setEnabled(false);
		chkSelectFeature.setSelected(false);
		txtDescribeFeature.setEnabled(false);
		txtDescribeFeature.setText("");
	}

	/**
	 * 
	 */
	private class ListItem {
		private Parameter param;
		private Hashtable<Integer, Object> customValues;

		public ListItem(Parameter param, Hashtable<Integer, Object> customValues) {
			this.setParam(param);
			this.setCustomValues(customValues);
		}

		/**
		 * @param param The param to set.
		 */
		public void setParam(Parameter param) {
			this.param = param;
		}

		/**
		 * @return Returns the param.
		 */
		public Parameter getParam() {
			return param;
		}

		/**
		 * @param customValues The customValues to set.
		 */
		public void setCustomValues(Hashtable<Integer, Object> customValues) {
			this.customValues = customValues;
		}

		/**
		 * @return Returns the customValues.
		 */
		public Hashtable<Integer, Object> getCustomValues() {
			return customValues;
		}
	}

	/**
	 * 
	 */
	private class ParameterCellRenderer extends JPanel implements ListCellRenderer {
		private JLabel lblIcon = new JLabel();
		private JLabel lblTxt = new JLabel();

		public ParameterCellRenderer() {
			setLayout(new FlowLayout(FlowLayout.LEFT));
			this.add(lblIcon);
			this.add(lblTxt);
			setOpaque(true);
		}

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			ListItem li = ((ListItem) value);
			lblIcon.setIcon(li.getParam().getIcon());
			lblTxt.setText(li.getParam().getName());
			setBackground(isSelected ? lstParameter.getSelectionBackground() : Color.white);
			lblTxt.setForeground(isSelected ? Color.white : Color.black);
			return this;
		}
	}

	/**
	 * 
	 */
	private class ParameterSelectListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting()) return;
			if (lstParameter.getSelectedIndex() > -1 && !e.getValueIsAdjusting()) {
				ListItem li = ((ListItem) lstParameter.getSelectedValue());
				if (previousSelected != null) {
					saveVisibleTo(previousSelected);
				}
				reset();
				//now we're appending the values, that are changed for this site
				Iterator itCustomValues = li.getCustomValues().keySet().iterator();
				while (itCustomValues.hasNext()) {
					Integer customValueParameterType = (Integer) itCustomValues.next();
					if (customValueParameterType.intValue() == Parameter.PARAMETER_TYPE_BOOLEAN) {
						chkSelectFeature.setEnabled(true);
						chkSelectFeature.setSelected(Boolean.valueOf(li.getCustomValues().get(customValueParameterType).toString()).booleanValue());
					}
					if (customValueParameterType.intValue() == Parameter.PARAMETER_TYPE_STRING) {
						txtDescribeFeature.setEnabled(true);
						txtDescribeFeature.setText(li.getCustomValues().get(customValueParameterType).toString());
					}
				}
				previousSelected = li;
			}
		}
	}

	private void saveVisibleTo(ListItem li) {
		Iterator itCustomValues = li.getCustomValues().keySet().iterator();
		while (itCustomValues.hasNext()) {
			Integer customValueParameterType = (Integer) itCustomValues.next();
			if (customValueParameterType.intValue() == Parameter.PARAMETER_TYPE_BOOLEAN) {
				li.getCustomValues().put(customValueParameterType, Boolean.valueOf(chkSelectFeature.isSelected()));
			}
			if (customValueParameterType.intValue() == Parameter.PARAMETER_TYPE_STRING) {
				li.getCustomValues().put(customValueParameterType, txtDescribeFeature.getText());
			}
		}
	}

	void btnCancelActionPerformed(ActionEvent e) {
		//		load();
		this.setVisible(false);
	}

	void btnSaveActionPerformed(ActionEvent e) {
		ListItem li = ((ListItem) lstParameter.getSelectedValue());
		saveVisibleTo(li);
		this.setVisible(false);
	}

}
