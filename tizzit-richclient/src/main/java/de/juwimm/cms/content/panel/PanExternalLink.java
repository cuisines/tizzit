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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.util.Parameters;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanExternalLink extends JPanel {
	private static Logger log = Logger.getLogger(PanExternalLink.class);
	private JTextField txtLinkname = new JTextField();
	private JTextField txtLinkaddress = new JTextField();
	private JLabel lblLinkName = new JLabel();
	private JLabel lblLinkAddress = new JLabel();
	private JCheckBox chkHeader = new JCheckBox();
	private JCheckBox chkNewWindow = new JCheckBox();
	private String errMsg = "";
	private boolean showLinkname = true;
	private JCheckBox cbxDisplayTypeInline = new JCheckBox();
	private JCheckBox cbxPopup = new JCheckBox();
	private PanPopupDetails panPopupDetails = null;

	public PanExternalLink(boolean showLinkname) {
		this.showLinkname = showLinkname;
		try {
			jbInit();

			lblLinkName.setVisible(showLinkname);
			getLinknameField().setVisible(showLinkname);

			if (rb != null) {
				lblLinkName.setText(rb.getString("panel.panelView.lblLinkname"));
				lblLinkAddress.setText(rb.getString("content.modules.externalLink.linkAddress"));
				chkHeader.setText(rb.getString("content.modules.externalLink.openInFrame"));
				if (Parameters.getBooleanParameter(Parameters.PARAM_EXTLINK_OPENWITHSTYLE)) {
					chkHeader.setVisible(true);
				} else {
					chkHeader.setVisible(false);
				}
				chkNewWindow.setText(rb.getString("panel.panelView.jump.newWindow"));
				cbxDisplayTypeInline.setText(rb.getString("content.modules.externalLink.displayTypeInline"));
				cbxPopup.setText(rb.getString("PanPopupDetails.showInPopup"));
			}
		} catch (Exception exe) {
			log.error("Initialization problem", exe);
		}
	}

	private void jbInit() throws Exception {
		this.setLayout(new GridBagLayout());
		lblLinkName.setText("Linkname");
		lblLinkAddress.setText("Linkadresse");
		this.getLinkaddressField().setMaximumSize(new Dimension(160, 23));
		this.getLinkaddressField().setPreferredSize(new Dimension(160, 23));
		getLinkaddressField().setText(rb.getString("PanExternalLink.linkAddressPrefill"));
		this.getLinknameField().setMaximumSize(new Dimension(160, 23));
		this.getLinknameField().setPreferredSize(new Dimension(160, 23));

		chkHeader.setText(rb.getString("PanExternalLink.lblWithHeader"));
		chkNewWindow.setText("In einem neuen Fenster öffnen");
		chkNewWindow.setSelected(true); // default like in former (better?) times
		this.add(lblLinkName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(13, 4, 0, 10), 13, 0));
		this.add(getLinknameField(), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 6), 0, 0));
		this.add(lblLinkAddress, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(13, 4, 0, 0), 9, 0));
		this.add(getLinkaddressField(), new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 6), 0, 0));
		this.add(chkHeader, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(5, 0, 0, 6), 0, 0));
		this.add(chkNewWindow, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(5, 0, 0, 6), 0, 0));
		this.add(cbxDisplayTypeInline, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(5, 0, 0, 6), 0, 0));
		this.add(cbxPopup, new GridBagConstraints(1, 5, 1, 1, 0.0, 1.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(5, 0, 0, 6), 0, 0));
		
		this.cbxPopup.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				cbxPopupStateChanged(e);
			}
		});
	}
	
	private void cbxPopupStateChanged(ItemEvent e) {
		this.showPopupPanel(e.getStateChange() == ItemEvent.SELECTED);
	}
	
	private void showPopupPanel(boolean show) {
		if (show) {
			this.add(this.getPanPopupDetails(), new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
					GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		} else {
			this.remove(this.getPanPopupDetails());
		}
		this.revalidate();
		this.repaint();
	}
	
	private PanPopupDetails getPanPopupDetails() {
		if (this.panPopupDetails == null) {
			this.panPopupDetails = new PanPopupDetails();
		}
		return this.panPopupDetails;
	}

	public boolean isModuleValid() {
		errMsg = "";
		if (this.getLinknameField().getText().equals("") && showLinkname) {
			errMsg = rb.getString("exception.LinknameRequired");
		}
		try {
			//Verify der Felder
			new URL(this.getLinkaddressField().getText());
			if (this.getLinkaddressField().getText().equals(rb.getString("PanExternalLink.linkAddressPrefill"))) {
				if (!errMsg.equals(""))
						errMsg += "\n";
				errMsg += rb.getString("exception.LinkURLnotValid") + "\n" +
						rb.getString("exception.LinkURLnotValidExplain");
			}
		} catch (java.net.MalformedURLException me) {
			if (!errMsg.equals(""))
					errMsg += "\n";
			errMsg += rb.getString("exception.LinkURLnotValid");
		}
		if (this.cbxPopup.isVisible()) {
			return (errMsg.equals("")) && this.getPanPopupDetails().isModuleValid();
		}
		return (errMsg.equals(""));
	}

	public String getValidationError() {
		if (this.cbxPopup.isVisible()) {
			if (this.errMsg.length() > 0 && this.getPanPopupDetails().getValidationError().length() > 0) {
				this.errMsg += "\n";
			}
			this.errMsg += this.getPanPopupDetails().getValidationError();
		}
		return errMsg;
	}

	public Node getProperties() {
		Element root = ContentManager.getDomDoc().createElement("root");
		Element elm = ContentManager.getDomDoc().createElement("a");
		CDATASection txtNode = ContentManager.getDomDoc().createCDATASection(this.getLinknameField().getText());
		elm.appendChild(txtNode);
		if (chkHeader.isSelected()) {
			elm.setAttribute("header", "true");
		}
		elm.setAttribute("href", this.getLinkaddressField().getText());
		if (chkNewWindow.isSelected()) {
			elm.setAttribute("target", "_blank");
		} else {
			elm.setAttribute("target", "_self");
		}
		if (cbxDisplayTypeInline.isSelected()) {
			elm.setAttribute("displayType", "inline");
		} else {
			elm.setAttribute("displayType", "block");
		}
		root.appendChild(elm);
		if (this.cbxPopup.isVisible() && this.cbxPopup.isSelected()) {
			root.appendChild(this.getPanPopupDetails().getProperties());
		}
		return root;
	}
	
	public void clear() {
		this.getLinkaddressField().setText("");
		this.getLinknameField().setText("");
		if (this.panPopupDetails != null) {
			this.remove(this.panPopupDetails);	
		}
		this.panPopupDetails = null;
	}

	public void setProperties(Node prop) {
		if (prop != null && prop.hasChildNodes()) {
			try {
				Node nde = XercesHelper.findNode(prop, "./popup");
				if (nde != null) {
					this.getPanPopupDetails().setProperties(nde);
					this.cbxPopup.setSelected(true);
				} else {
					if (this.panPopupDetails != null) {
						this.remove(this.panPopupDetails);	
					}
					this.panPopupDetails = null;
					this.cbxPopup.setSelected(false);
				}
				this.showPopupPanel(this.cbxPopup.isSelected());
			} catch (Exception exe) {
			}
			try {
				Node nde = XercesHelper.findNode(prop, "./a");
				if (nde != null) {
					prop = nde;
				}
			} catch (Exception exe) {
			}

			this.getLinkaddressField().setText(((Element) prop).getAttribute("href").replaceAll("&#38;", "&"));
			this.getLinknameField().setText(XercesHelper.getNodeValue(prop).trim());
			try {
				if (((Element) prop).getAttribute("header").equals("true"))
					this.chkHeader.setSelected(true);
			} catch (Exception exe) {
			}
			try {
				if (((Element) prop).getAttribute("target").equals("_self")) {
					this.chkNewWindow.setSelected(false);
				} else {
					this.chkNewWindow.setSelected(true);
				}
			} catch (Exception exe) {
			}
			try {
				String displayType = ((Element) prop).getAttribute("displayType");
				this.cbxDisplayTypeInline.setSelected("inline".equalsIgnoreCase(displayType));
			} catch (Exception exe) {
			}
		}
	}

	/**
	 * @param txtLinkname The txtLinkname to set.
	 */
	protected void setLinknameField(JTextField txtLinkname) {
		this.txtLinkname = txtLinkname;
	}

	/**
	 * @return Returns the txtLinkname.
	 */
	public JTextField getLinknameField() {
		return txtLinkname;
	}

	/**
	 * @param txtLinkaddress The txtLinkaddress to set.
	 */
	protected void setLinkaddressField(JTextField txtLinkaddress) {
		this.txtLinkaddress = txtLinkaddress;
	}

	/**
	 * @return Returns the txtLinkaddress.
	 */
	public JTextField getLinkaddressField() {
		return txtLinkaddress;
	}

	public void setDisplayTypeEditable(boolean editable) {
		this.cbxDisplayTypeInline.setVisible(editable);
	}

	public void setPopupAvailable(boolean available) {
		this.cbxPopup.setVisible(available);
	}

}