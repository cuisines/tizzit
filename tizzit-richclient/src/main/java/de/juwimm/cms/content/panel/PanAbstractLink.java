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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.modules.Documents;
import de.juwimm.cms.content.modules.ExternalLink;
import de.juwimm.cms.content.modules.InternalLink;
import de.juwimm.cms.content.modules.Module;
import de.juwimm.swing.DropDownHolder;

/**
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class PanAbstractLink extends JPanel implements ActionListener {
	private static Logger log = Logger.getLogger(PanAbstractLink.class);
	private DefaultComboBoxModel modelLinkType = null;
	private JComboBox cboLinkType = null;
	private JButton btnCreateLink = null;
	private Module selectedLinkType = null;
	private boolean enabledState = false;
	private HashMap<String, Properties> customPropertiesMap = new HashMap<String, Properties>();
	
	public PanAbstractLink(HashMap<String, Properties> customPropertiesMap) {
		super();
		this.customPropertiesMap = customPropertiesMap;
		this.initialize();
	}

	private void initialize() {
		this.add(this.getCboLinkType());
		this.add(this.getBtnCreateLink());
	}

	private JComboBox getCboLinkType() {
		if (this.cboLinkType == null) {
			this.cboLinkType = new JComboBox();
			this.cboLinkType.setEditable(false);
			this.modelLinkType = new DefaultComboBoxModel();
			this.modelLinkType.addElement(new DropDownHolder("el", rb.getString("panel.toolLink")));
			this.modelLinkType.addElement(new DropDownHolder("il", rb.getString("panel.toolJump")));
			this.modelLinkType.addElement(new DropDownHolder("doc", rb.getString("panel.document")));
			this.cboLinkType.setModel(this.modelLinkType);
		}
		return this.cboLinkType;
	}

	private JButton getBtnCreateLink() {
		if (this.btnCreateLink == null) {
			this.btnCreateLink = new JButton(rb.getString("content.modules.link.create"));
			this.btnCreateLink.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnCreateLinkActionPerformed(e);
				}
			});
		}
		return this.btnCreateLink;
	}

	private void btnCreateLinkActionPerformed(ActionEvent e) {
		Object selectedItem = this.cboLinkType.getSelectedItem();
		if (selectedItem != null) {
			DropDownHolder ddh = (DropDownHolder) selectedItem;
			try {
				if ("el".equalsIgnoreCase(ddh.getObject().toString())) {
					this.selectedLinkType = new ExternalLink();
					this.setCustomProperties();
					this.selectedLinkType.setLabel(rb.getString("panel.toolLink"));
					((ExternalLink) this.selectedLinkType).addDeleteSettingsActionListener(this);
				} else if ("il".equalsIgnoreCase(ddh.getObject().toString())) {
					this.selectedLinkType = new InternalLink();
					this.setCustomProperties();
					this.selectedLinkType.setLabel(rb.getString("panel.toolJump"));
					((InternalLink) this.selectedLinkType).addDeleteSettingsActionListener(this);
				} else if ("doc".equalsIgnoreCase(ddh.getObject().toString())) {
					this.selectedLinkType = new Documents();
					this.setCustomProperties();
					this.selectedLinkType.setLabel(rb.getString("panel.document"));
					((Documents) this.selectedLinkType).addDeleteSettingsActionListener(this);
				}
				this.selectedLinkType.setProperties(null);
				this.selectedLinkType.setEnabled(this.enabledState);
				this.removeAll();
				this.add(this.selectedLinkType.viewPanelUI());
			} catch (Exception ex) {
				log.error("Error creating new " + ddh + ": " + ex.getMessage(), ex);
				this.initialize();
				this.selectedLinkType = null;
			}
			this.revalidate();
		}
	}

	public Module getSelectedLinkType() {
		return this.selectedLinkType;
	}

	public Node getProperties() {
		Element root = ContentManager.getDomDoc().createElement("abstractLink");
		if (this.selectedLinkType != null) {
			Node node = ContentManager.getDomDoc().importNode(this.selectedLinkType.getProperties(), true);
			if (this.selectedLinkType instanceof ExternalLink) {
				Node extLink = ContentManager.getDomDoc().createElement("externalLink");
				try {
					Node nde = XercesHelper.findNode(node, "./a");
					if (nde != null) {
						extLink.appendChild(nde);
					}
					nde = XercesHelper.findNode(node, "./popup");
					if (nde != null) {
						extLink.appendChild(nde);
					}
				} catch (Exception e) {}
				root.appendChild(extLink);
			} else if (this.selectedLinkType instanceof InternalLink) {
				Node intLink = ContentManager.getDomDoc().createElement("internalLink");
				try {
					Node nde = XercesHelper.findNode(node, "./internalLink");
					if (nde != null) {
						intLink.appendChild(nde);
					}
					nde = XercesHelper.findNode(node, "./popup");
					if (nde != null) {
						intLink.appendChild(nde);
					}
				} catch (Exception e) {}
				root.appendChild(intLink);
			} else if (this.selectedLinkType instanceof Documents) {
				Node document = ContentManager.getDomDoc().createElement("documents");
				try {
					Node nde = XercesHelper.findNode(node, "./document");
					if (nde != null) {
						document.appendChild(nde);
					}
				} catch (Exception e) {}
				root.appendChild(document);
			} else {
				root.appendChild(node);
			}
		}
		return root;
	}

	public void setProperties(Node prop) {
		this.selectedLinkType = null;
		this.removeAll();
		if (prop != null && prop.hasChildNodes()) {
			try {
				Node nde = XercesHelper.findNode(prop, "externalLink");
				if (nde != null) {
					this.selectedLinkType = new ExternalLink();
					this.setCustomProperties();
					this.selectedLinkType.setLabel(rb.getString("panel.toolLink"));
					((ExternalLink) this.selectedLinkType).addDeleteSettingsActionListener(this);
					this.selectedLinkType.setProperties(prop.getFirstChild());
					this.add(this.selectedLinkType.viewPanelUI());
					this.selectedLinkType.setEnabled(this.enabledState);
				} else {
					nde = XercesHelper.findNode(prop, "internalLink");
					if (nde != null) {
						this.selectedLinkType = new InternalLink();
						this.setCustomProperties();
						this.selectedLinkType.setLabel(rb.getString("panel.toolJump"));
						((InternalLink) this.selectedLinkType).addDeleteSettingsActionListener(this);
						this.selectedLinkType.setProperties(prop.getFirstChild());
						this.add(this.selectedLinkType.viewPanelUI());
						this.selectedLinkType.setEnabled(this.enabledState);
					} else {
						nde = XercesHelper.findNode(prop, "documents");
						if (nde != null) {
							this.selectedLinkType = new Documents();
							this.setCustomProperties();
							this.selectedLinkType.setLabel(rb.getString("panel.document"));
							((Documents) this.selectedLinkType).addDeleteSettingsActionListener(this);
							this.selectedLinkType.setProperties(prop.getFirstChild());
							this.add(this.selectedLinkType.viewPanelUI());
							this.selectedLinkType.setEnabled(this.enabledState);
						} else {
							// no child is found
							this.selectedLinkType = null;
							this.removeAll();
							this.initialize();
						}
					}
				}
			} catch (Exception e) {
				log.error("Error setting properties for AbstractLink!", e);
				this.selectedLinkType = null;
			}
		} else {
			this.initialize();
		}
		this.revalidate();
	}

	public void setEnabled(boolean enabling) {
		this.enabledState = enabling;
		this.getCboLinkType().setEnabled(enabling);
		this.getBtnCreateLink().setEnabled(enabling);
		if (this.selectedLinkType != null) this.selectedLinkType.setEnabled(enabling);
	}

	public void actionPerformed(ActionEvent e) {
		this.setProperties(null);
	}
	
	private void setCustomProperties() {
		if (this.selectedLinkType != null) {
			if (this.customPropertiesMap != null) {
				Iterator<String> it = this.customPropertiesMap.keySet().iterator();
				while (it.hasNext()) {
					String methodName = it.next();
					Properties parameters = this.customPropertiesMap.get(methodName);
					this.selectedLinkType.setCustomProperties(methodName, parameters);
				}
			}
		}
	}

}
