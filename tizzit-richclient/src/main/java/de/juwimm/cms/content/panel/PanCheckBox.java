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

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.content.ContentManager;
import de.juwimm.util.XercesHelper;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Sabarinath Ayyappan
 * @version $Id$
 */
public class PanCheckBox extends JPanel {
	private static Logger log = Logger.getLogger(PanCheckBox.class);
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private ArrayList<CheckBoxesDetail> checkBoxesList = new ArrayList<CheckBoxesDetail>();

	public PanCheckBox() {
		try {
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization error", exe);
		}
	}

	void jbInit() throws Exception {
		this.setLayout(gridBagLayout1);
	}
	
	public void clear() {
		disableAllCheckBoxes();
	}
	
	public void setProperties(Node prop) {
		if (prop != null && prop.hasChildNodes()) {
			disableAllCheckBoxes();
			Iterator itCheckboxes = checkBoxesList.iterator();
			while (itCheckboxes.hasNext()) {
				CheckBoxesDetail checkBoxesDetail = (CheckBoxesDetail) itCheckboxes.next();
				Node foundNode = null;
				try {
					foundNode = XercesHelper.findNode(prop, "./" + checkBoxesDetail.getElementName());
				} catch (Exception exe) {
				}
				checkBoxesDetail.getCheckBox().setSelected(false);
				if (foundNode != null && foundNode.hasChildNodes()) {
					checkBoxesDetail.getCheckBox().setSelected(true);
				}
			}
		} else {
			disableAllCheckBoxes();
		}
	}

	public Node getProperties() {
		Element elm = ContentManager.getDomDoc().createElement("checkbox");
		for (int i = 0; i < checkBoxesList.size(); i++) {
			try {
				CheckBoxesDetail chkBoxesDetail = (CheckBoxesDetail) checkBoxesList.get(i);
				Node cbox = ContentManager.getDomDoc().createElement(chkBoxesDetail.getElementName());
				if (chkBoxesDetail.getCheckBox().isSelected()) {
					//Text txtNode = ContentSingleton.getDomDoc().createTextNode(chkBoxesDetail.getProperties());
					//cbox.appendChild(txtNode);
					Node cbContent = null;
					Document doc = null;
					String cbData = chkBoxesDetail.getProperties();
					if ((cbData.startsWith("<")) || (cbData.startsWith("&lt;"))) {
						try {
							doc = XercesHelper.string2Dom(chkBoxesDetail.getProperties());
						} catch (Throwable e) {
						}
					} else {
						cbContent = ContentManager.getDomDoc().createCDATASection(cbData);
					}
					if (doc != null) {
						cbContent = doc.getDocumentElement();
					}
					Node newProp = ContentManager.getDomDoc().importNode(cbContent, true);
					cbox.appendChild(newProp);
				}
				elm.appendChild(cbox);
			} catch (Exception exe) {
				log.error("Error getting properties", exe);
			}
		}
		return elm;
	}

	/**
	 * Funtion for deselecting all the checkboxes in the list
	 * @param prop
	 */
	public void disableAllCheckBoxes() {
		CheckBoxesDetail chkBoxesDetail = null;
		for (int i = 0; i < checkBoxesList.size(); i++) {
			chkBoxesDetail = (CheckBoxesDetail) checkBoxesList.get(i);
			chkBoxesDetail.getCheckBox().setSelected(false);
		}
	}

	public void setCustomProperties(String propmodule, Properties prop) {
		if (propmodule.equals("checkboxes")) {
			//Added a new check box
			JCheckBox checkbox = new JCheckBox();
			//To set the label.
			checkbox.setText(prop.get("label").toString());
			this.add(checkbox);
			//To have the list of checkboxes added to the panel
			CheckBoxesDetail chkBoxesDetails = new CheckBoxesDetail(checkbox, prop.get("label").toString(),
					prop.get("elementName").toString(), prop.get("properties").toString());
			checkBoxesList.add(chkBoxesDetails);
		}

	}

	public void setEnabled(boolean enabling) {
		Iterator it = checkBoxesList.iterator();
		while (it.hasNext()) {
			((CheckBoxesDetail) it.next()).getCheckBox().setEnabled(enabling);
		}
	}

	/**
	 * Static class which holds the details of the checkbox instances
	 */
	static class CheckBoxesDetail {
		private JCheckBox checkbox = null;
		private String checkBoxName = "";
		private String elementName = "";
		private String properties = "";

		public CheckBoxesDetail(JCheckBox chkBox, String chkBoxName, String elementName, String properties) {
			this.checkbox = chkBox;
			this.checkBoxName = chkBoxName;
			this.elementName = elementName;
			//<properties>...</properties>
			this.properties = properties.substring(12, properties.length() - 13);
		}

		//To get the checkBox instance
		public JCheckBox getCheckBox() {
			return checkbox;
		}

		//To get the checkBox name
		public String getCheckBoxName() {
			return checkBoxName;
		}

		//To get the tag or value to be inserted into the XML
		public String getElementName() {
			return elementName;
		}

		//To get the tag or value to be inserted into the XML
		public String getProperties() {
			return properties;
		}
	}
}