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

import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import de.juwimm.cms.content.frame.DlgModalModule;
import de.juwimm.cms.content.panel.PanExternalLink;
import de.juwimm.cms.content.panel.PanLinkButton;
import de.juwimm.util.XercesHelper;

/**
 * Module for external links (links to pages outside this CMS).<br/>
 * <p>
 * Can be parameterized in the DCF:<br/>
 * &lt;externalLink label="Externer Link DisplayType" dcfname="externalLink"&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&lt;dcfConfig&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;classname&gt;de.juwimm.cms.content.modules.ExternalLink&lt;/classname&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;mandatory&gt;false&lt;/mandatory&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="DisplayType"&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;editable&gt;true&lt;/editable&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;popupAvailable&gt;true&lt;/popupAvailable&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;/property&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&lt;/dcfConfig&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&lt;dcfInitial /&gt;<br/>
 * &lt;/externalLink&gt;<br/>
 * </p>
 * <p>
 * If the value of &quot;editable&quot; can be converted to &quot;true&quot; an additional checkbox is shown on the panel<br/>
 * for specifying if the link should be shown inline or as block on the webpage.<br/>
 * Depending on the state of this checkbox the attribute &quot;displayType&quot; has the value &quot;inline&quot; or &quot;block&quot;.<br/>
 * </p>
 * <p>
 * If the value of &quot;popupAvailable&quot; can be converted to &quot;true&quot; an additional checkbox is shown on the panel<br/>
 * for specifying if the link-target should be shown in a separate popup-window. If this checkbox is selected, some additional<br/>
 * input-fields appear for the title of the window, it's size, if it should be resizeable and if it may have scrollbars.<br/>
 * </p>
 * <p>
 * The output from the Generator looks like this:<br/>
 * &lt;externalLink dcfname="externalLink" label="Externer Link DisplayType"&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&lt;a displayType="inline" href="http://www.Linkadresse.de" target="_blank"&gt;Linkname externer Link&lt;/a&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&lt;popup resizeable="true" scrollbars="true"&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;windowTitle&gt;&lt;![CDATA[title]]&gt;&lt;/windowTitle&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;width&gt;width&lt;/width&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;height&gt;height&lt;/height&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&lt;/popup&gt;<br/>
 * &lt;/externalLink&gt;<br/>
 * </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class ExternalLink extends AbstractModule {
	private static Logger log = Logger.getLogger(ExternalLink.class);
	public static final String CLASS_NAME = "de.juwimm.cms.content.modules.ExternalLink";
	private PanExternalLink pan = null;
	private PanLinkButton panBtn;
	private boolean imEnabled = false;
	private boolean linknameVisible = true;
	private boolean displayTypeEditable = false;
	private boolean popupAvailable = false;

	public void setCustomProperties(String methodname, Properties parameters) {
		super.setCustomProperties(methodname, parameters);
		if ("DisplayType".equalsIgnoreCase(methodname)) {
			try {
				this.displayTypeEditable = Boolean.parseBoolean(parameters.get("editable").toString());
			} catch (Exception e) {
				// parameter may not be present - that's ok
			}
			try {
				this.popupAvailable = Boolean.parseBoolean(parameters.get("popupAvailable").toString());
			} catch (Exception e) {
				// parameter may not be present - that's ok
			}
		}
	}

	public void setLinknameVisible(boolean linknameVisible) {
		this.linknameVisible = linknameVisible;
	}

	public JDialog viewModalUI(boolean modal) {
		if (pan == null) pan = new PanExternalLink(linknameVisible);
		pan.setDisplayTypeEditable(this.displayTypeEditable);
		pan.setPopupAvailable(this.popupAvailable);
		int frameHeight = 180;
		if (this.displayTypeEditable) frameHeight += 20;
		if (this.popupAvailable) frameHeight += 180;
		int frameWidth = 470;
		DlgModalModule frm = new DlgModalModule(this, pan, frameHeight, frameWidth, modal);
		frm.setVisible(true);
		return frm;
	}

	public JPanel viewPanelUI() {
		if (pan == null) pan = new PanExternalLink(linknameVisible);
		if (this.panBtn == null) this.panBtn = new PanLinkButton(this, true);
		panBtn.setEnabled(imEnabled);
		return panBtn;
	}

	public boolean isModuleValid() {
		if (isMandatory()) {
			boolean retVal = this.pan.isModuleValid();
			setValidationError(this.pan.getValidationError());
			return retVal;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.content.modules.Module#save(org.xml.sax.ContentHandler)
	 */
	public void load() {
	}

	public Node getProperties() {
		Node prop = pan.getProperties();
		setDescription(XercesHelper.getNodeValue(prop.getFirstChild()));
		return prop;
	}

	public void setProperties(Node node) {
		pan = new PanExternalLink(linknameVisible);
		if (node != null) {
			setDescription(XercesHelper.getNodeValue(node.getFirstChild()));
			try {
				Node nde = XercesHelper.findNode(node, "./popup");
				if (nde != null) {
					this.popupAvailable = true;
				}
			} catch (Exception e) {}
		} else {
			setDescription("");
		}
		pan.setProperties(node);
		if (this.panBtn == null) this.panBtn = new PanLinkButton(this, true);
		this.panBtn.updateLinks();
	}

	public String getPaneImage() {
		try {
			return getURLEncodedISO("svgexternallink." + getDescription());
		} catch (Exception exe) {
			log.error("Error getting pane image", exe);
			return "tra_link.png";
		}
	}

	public String getIconImage() {
		return "tra_link.png";
	}

	public void setEnabled(boolean enabling) {
		if (panBtn != null) panBtn.setEnabled(enabling);
		imEnabled = enabling;
	}
	
	public void recycle() {
		pan.clear();
	}

	public String getLinkName() {
		return this.pan.getLinknameField().getText();
	}

	public String getLinkTarget() {
		return this.pan.getLinkaddressField().getText();
	}

	public void addDeleteSettingsActionListener(ActionListener al) {
		if (this.panBtn == null) this.panBtn = new PanLinkButton(this, true);
		this.panBtn.addDeleteSettingsActionListener(al);
	}

}