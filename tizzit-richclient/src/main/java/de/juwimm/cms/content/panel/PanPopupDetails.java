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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.content.ContentManager;
import de.juwimm.util.XercesHelper;
/**
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class PanPopupDetails extends JPanel {
	private String errMsg = "";
	private JCheckBox cbxScrollbars = null;
	private JCheckBox cbxResize = null;
	private JTextField txtWidth = null;
	private JTextField txtHeight = null;
	private JTextField txtWindowTitle = null;
	private JLabel lblWidth = null;
	private JLabel lblHeight = null;
	private JLabel lblWindowTitle = null;
	
	public PanPopupDetails() {
		super();
		this.init();
	}
	
	private void init() {
		this.setMinimumSize(new Dimension(300, 145));
		this.setBorder(BorderFactory.createEtchedBorder());
		this.setLayout(new GridBagLayout());
		this.cbxScrollbars = new JCheckBox(rb.getString("PanPopupDetails.scrollbars"));
		this.cbxResize = new JCheckBox(rb.getString("PanPopupDetails.resizeable"));
		this.lblHeight = new JLabel(rb.getString("PanPopupDetails.height"));
		this.lblWidth = new JLabel(rb.getString("PanPopupDetails.width"));
		this.lblWindowTitle = new JLabel(rb.getString("PanPopupDetails.windowTitle"));
		this.txtHeight = new JTextField();
		this.txtWidth = new JTextField();
		this.txtWindowTitle = new JTextField();
		this.add(cbxScrollbars, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 5), 0, 0));
		this.add(cbxResize, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
		this.add(lblWindowTitle, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 0), 0, 0));
		this.add(txtWindowTitle, new GridBagConstraints(1, 2, 1, 1, 3.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 10), 0, 0));
		this.add(lblWidth, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 0), 0, 0));
		this.add(txtWidth, new GridBagConstraints(1, 3, 1, 1, 3.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 10), 0, 0));
		this.add(lblHeight, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 0), 0, 0));
		this.add(txtHeight, new GridBagConstraints(1, 4, 1, 1, 3.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 10, 10), 0, 0));
	}

	public JCheckBox getCbxResize() {
		return cbxResize;
	}

	public JCheckBox getCbxScrollbars() {
		return cbxScrollbars;
	}

	public JTextField getTxtHeight() {
		return txtHeight;
	}

	public JTextField getTxtWidth() {
		return txtWidth;
	}

	public JTextField getTxtWindowTitle() {
		return txtWindowTitle;
	}

	public void setProperties(Node prop) {
		if (prop != null && prop.hasChildNodes()) {
			try {
				String scrollbars = ((Element) prop).getAttribute("scrollbars");
				this.cbxScrollbars.setSelected("true".equalsIgnoreCase(scrollbars));
			} catch (Exception exe) {
			}
			try {
				String resizeable = ((Element) prop).getAttribute("resizeable");
				this.cbxResize.setSelected("true".equalsIgnoreCase(resizeable));
			} catch (Exception exe) {
			}
			try {
				Node windowTitle = XercesHelper.findNode(prop, "./windowTitle");
				if (windowTitle != null) {
					this.getTxtWindowTitle().setText(XercesHelper.getNodeValue(windowTitle).trim());
				}
			} catch (Exception exe) {
			}
			try {
				Node width = XercesHelper.findNode(prop, "./width");
				if (width != null) {
					this.getTxtWidth().setText(XercesHelper.getNodeValue(width).trim());
				}
			} catch (Exception exe) {
			}
			try {
				Node height = XercesHelper.findNode(prop, "./height");
				if (height != null) {
					this.getTxtHeight().setText(XercesHelper.getNodeValue(height).trim());
				}
			} catch (Exception exe) {
			}
		}
	}
	
	public Node getProperties() {
		Element popup = ContentManager.getDomDoc().createElement("popup");
		popup.setAttribute("scrollbars", String.valueOf(cbxScrollbars.isSelected()));
		popup.setAttribute("resizeable", String.valueOf(cbxResize.isSelected()));
		// TODO: validate user-input (numbers)
		{
			Element elmWindowTitle = ContentManager.getDomDoc().createElement("windowTitle");
			CDATASection txtWindowTitle = ContentManager.getDomDoc().createCDATASection(this.getTxtWindowTitle().getText());
			elmWindowTitle.appendChild(txtWindowTitle);
			popup.appendChild(elmWindowTitle);
		}
		{
			Element elmWidth = ContentManager.getDomDoc().createElement("width");
			CDATASection txtWidth = ContentManager.getDomDoc().createCDATASection(this.getTxtWidth().getText());
			elmWidth.appendChild(txtWidth);
			popup.appendChild(elmWidth);
		}
		{
			Element elmHeight = ContentManager.getDomDoc().createElement("height");
			CDATASection txtHeight = ContentManager.getDomDoc().createCDATASection(this.getTxtHeight().getText());
			elmHeight.appendChild(txtHeight);
			popup.appendChild(elmHeight);
		}
		return popup;
	}

	public boolean isModuleValid() {
		boolean valid = true;
		String width = this.getTxtWidth().getText();
		String height = this.getTxtHeight().getText();
		if (width != null && width.length() > 0) {
			try {
				int w = Integer.parseInt(width);
				if (w <= 0)
					errMsg = rb.getString("exception.integerNotValid");
			} catch (Exception e) {
				errMsg = rb.getString("exception.integerNotValid");
				valid = false;
			}
		}
		if (valid && height != null && height.length() > 0) {
			try {
				int h = Integer.parseInt(height);
				if (h <= 0)
					errMsg = rb.getString("exception.integerNotValid");
			} catch (Exception e) {
				errMsg = rb.getString("exception.integerNotValid");
				valid = false;
			}
		}
		return valid;
	}

	public String getValidationError() {
		return errMsg;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
