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
package de.juwimm.cms.content.modules;

import static de.juwimm.cms.common.Constants.*;

import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.frame.DlgModalModule;
import de.juwimm.cms.content.panel.PanDocuments;
import de.juwimm.cms.content.panel.PanLinkButton;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class Documents extends AbstractModule {
	private static Logger log = Logger.getLogger(Documents.class);
	private PanDocuments pan = new PanDocuments();
	private PanLinkButton panBtn;
	private boolean imEnabled = true;
	private boolean displayTypeEditable = false;

	public void setCustomProperties(String methodname, Properties parameters) {
		super.setCustomProperties(methodname, parameters);
		if ("DisplayType".equalsIgnoreCase(methodname)) {
			try {
				this.displayTypeEditable = Boolean.parseBoolean(parameters.get("editable").toString());
			} catch (Exception e) {
				// parameter may not be present - that's ok
			}
		}
	}

	public JDialog viewModalUI(boolean modal) {
		int frameHeight = 500;
		int frameWidth = 500;
		DlgModalModule frm = new DlgModalModule(this, pan, frameHeight, frameWidth, modal);
		pan.setDisplayTypeEditable(this.displayTypeEditable);
		if (this.displayTypeEditable) frameHeight += 20;
		pan.regionSelected();
		frm.setVisible(true);
		return frm;
	}

	public JPanel viewPanelUI() {
		if (this.panBtn == null) this.panBtn = new PanLinkButton(this, true);
		panBtn.setEnabled(imEnabled);
		return panBtn;
	}

	public boolean isModuleValid() {
		if (isMandatory()) {
			boolean retVal = true;
			setValidationError("");
			if (pan.getDocumentId() == null || pan.getDocumentId().intValue() <= 0) {
				appendValidationError(rb.getString("exception.DocumentRequired"));
				retVal = false;
			}
			if (pan.getDocumentDescription().equals("")) {
				appendValidationError(rb.getString("exception.LinknameRequired"));
				retVal = false;
			}
			return retVal;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see de.juwimm.cms.content.modules.Module#save(org.xml.sax.ContentHandler)
	 */
	public void load() {
		/*
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pan.regionSelected();
			}
		});
		*/
	}

	public Node getProperties() {
		Element root = ContentManager.getDomDoc().createElement("root");
		if (pan.getDocumentId() != null) {
			Element elm = ContentManager.getDomDoc().createElement("document");
			if (pan.getDocumentId().intValue() > 0) elm.setAttribute("src", pan.getDocumentId() + "");
			elm.setAttribute("documentName", pan.getDocumentName());
			if (pan.getCbxDisplayTypeInline().isSelected()) {
				elm.setAttribute("displayType", "inline");
			} else {
				elm.setAttribute("displayType", "block");
			}
			setDescription(pan.getDocumentDescription());
			CDATASection txtNode = ContentManager.getDomDoc().createCDATASection(pan.getDocumentDescription());
			elm.appendChild(txtNode);
			root.appendChild(elm);
		}
		return root;
	}

	public void setProperties(Node node) {
		if (node != null) {
			try {
				Element docEl = (Element) XercesHelper.findNode(node, "./document");
				if (docEl.getAttribute("src") != null) {
					node = docEl;
				}
			} catch (Exception exe) {
			}
			try {
				pan = new PanDocuments();
				try {
					setDescription(XercesHelper.getNodeValue(node));
				} catch (Exception exe) {
				}
				int inti = new Integer(((Element) node).getAttribute("src")).intValue();
				pan.setDocumentId(new Integer(inti));
				pan.setDocumentDescription(XercesHelper.getNodeValue(node));
				try {
					String displayType = ((Element) node).getAttribute("displayType");
					this.pan.getCbxDisplayTypeInline().setSelected("inline".equalsIgnoreCase(displayType));
				} catch (Exception exe) {
				}
			} catch (Exception e) {
			}
		} else {
			pan = new PanDocuments();
			pan.setDocumentId(null);
			pan.setDocumentDescription("");
		}
		if (this.panBtn == null) this.panBtn = new PanLinkButton(this, true);
		this.panBtn.updateLinks();
	}

	public String getPaneImage() {
		try {
			return getURLEncodedISO("svgdocument." + getDescription());
		} catch (Exception exe) {
			log.error("Error getting pane image", exe);
			return "16_komp_importdoc.gif";
		}
	}

	public String getIconImage() {
		return "16_komp_importdoc.gif";
	}

	public void setEnabled(boolean enabling) {
		if (panBtn != null) panBtn.setEnabled(enabling);
		imEnabled = enabling;
	}
	
	public void recycle() {
		pan.setDocumentId(null);
		pan.setDocumentDescription("");
	}

	public void addDeleteSettingsActionListener(ActionListener al) {
		if (this.panBtn == null) this.panBtn = new PanLinkButton(this, true);
		this.panBtn.addDeleteSettingsActionListener(al);
	}

	public String getDocumentName() {
		return this.pan.getDocumentName();
	}

	public String getDocumentDescription() {
		return this.pan.getDocumentDescription();
	}

}