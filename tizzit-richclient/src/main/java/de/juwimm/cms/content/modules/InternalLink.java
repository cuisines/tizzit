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

import static de.juwimm.cms.client.beans.Application.*;

import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Node;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.content.frame.DlgModalModule;
import de.juwimm.cms.content.panel.PanInternalLink;
import de.juwimm.cms.content.panel.PanLinkButton;
import de.juwimm.cms.util.Communication;

/**
 * Module for internal links (links to other pages inside this CMS).<br/>
 * <p>
 * Can be parameterized in the DCF:<br/>
 * &lt;internalLink label="Interner Link" dcfname="internalLink"&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&lt;dcfConfig&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;classname&gt;de.juwimm.cms.content.modules.InternalLink&lt;/classname&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;mandatory&gt;false&lt;/mandatory&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="anchor"&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;visible&gt;true&lt;/editable&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;/property&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="DisplayType"&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;editable&gt;true&lt;/editable&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;popupAvailable&gt;true&lt;/popupAvailable&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;/property&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&lt;/dcfConfig&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&lt;dcfInitial /&gt;<br/>
 * &lt;/internalLink&gt;<br/>
 * </p>
 * <p>
 * By giving the value of &quot;visible&quot; in the &quot;anchor&quot;-section you can control in the DCF if the user may search for and select<br/>
 * an anchor on the target-page.<br/>
 * The default-value for this option is &quot;true&quot;, if nothing is set in the DCF the user may search and select an anchor.<br/>
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
 * &lt;internalLink dcfname="internalLink" label="Interner Link"&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&lt;internalLink displayType="inline" language="deutsch" level="" url="DualPicture" viewid="532"&gt;Linkname interner Link&lt;/internalLink&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&lt;popup resizeable="true" scrollbars="true"&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;windowTitle&gt;&lt;![CDATA[title]]&gt;&lt;/windowTitle&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;width&gt;width&lt;/width&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;height&gt;height&lt;/height&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&lt;/popup&gt;<br/>
 * &lt;/internalLink&gt;<br/>
 * </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class InternalLink extends AbstractModule {
	private static Logger log = Logger.getLogger(InternalLink.class);
	public static final String CLASS_NAME = "de.juwimm.cms.content.modules.InternalLink";
	private Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private PanInternalLink pan;
	private PanLinkButton panBtn;
	private boolean imEnabled = false;
	private boolean treeLink = false;
	private boolean anchorVisible = true;
	private boolean isSymlink = false;
	private boolean displayTypeEditable = false;
	private boolean popupAvailable = false;

	public InternalLink() {
	}

	public InternalLink(boolean treeLink) {
		this.treeLink = treeLink;
	}

	public void setLinknameVisible(boolean linknameVisible) {
		this.treeLink = !linknameVisible;
	}

	/**
	 * Allowed Methods are: 
	 * <ul>
	 *   <li>anchor</li>
	 * 	 <ul>
	 * 	   <li>visible - boolean object of <strong>true</strong> or <strong>false</strong></li>
	 *   </ul>
	 * </ul>
	 */
	public void setCustomProperties(String methodname, Properties parameters) {
		super.setCustomProperties(methodname, parameters);
		if ("anchor".equalsIgnoreCase(methodname)) {
			try {
				this.anchorVisible = Boolean.parseBoolean(parameters.get("visible").toString());
			} catch (Exception e) {
				// parameter may not be present - that's ok
			}
		} else if ("DisplayType".equalsIgnoreCase(methodname)) {
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

	public boolean isModuleValid() {
		if (isMandatory()) {
			boolean retVal = pan.isModuleValid();
			setValidationError(pan.getValidationError());
			return retVal;
		}
		return true;
	}

	public JPanel viewPanelUI() {
		if (this.panBtn == null) this.panBtn = new PanLinkButton(this, true);
		panBtn.setEnabled(imEnabled);
		return panBtn;
	}

	public JDialog viewModalUI(boolean modal) {
		if (pan == null) pan = new PanInternalLink(this, treeLink, isSymlink);
		pan.setAnchorVisible(anchorVisible);
		pan.setDisplayTypeEditable(this.displayTypeEditable);
		pan.setPopupAvailable(this.popupAvailable);

		int frameHeight = 520;
		if (this.displayTypeEditable) frameHeight += 20;
		if (this.popupAvailable) frameHeight += 180;
		int frameWidth = 444;
		DlgModalModule frm = new DlgModalModule(this, pan, frameHeight, frameWidth, modal);
		frm.setVisible(true);
		return frm;
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.content.modules.Module#save(org.xml.sax.ContentHandler)
	 */
	public void load() {
		//setLoaded(true);
	}

	public Node getProperties() {
		//waitWhileIsntLoaded();
		if (pan == null) pan = new PanInternalLink(this, treeLink, isSymlink);
		return pan.getProperties();
	}

	private Node node = null;

	public synchronized void setProperties(Node node) {
		this.node = node;
		if (node != null) {
			try {
				Node nde = XercesHelper.findNode(node, "./popup");
				if (nde != null) {
					this.popupAvailable = true;
				}
			} catch (Exception e) {}
		}
		if (pan == null) pan = new PanInternalLink(this, treeLink, isSymlink);
		pan.setProperties(this.node);
		if (this.panBtn == null) this.panBtn = new PanLinkButton(this, true);
		this.panBtn.updateLinks();
	}

	public String getIconImage() {
		return "tra_jump.png";
	}

	public String getPaneImage() {
		try {
			return getURLEncodedISO("svginternallink." + getDescription());
		} catch (Exception exe) {
			log.error("Error returning the pane image for desc " + getDescription(), exe);
			return "tra_jump.png";
		}
	}

	public void setEnabled(boolean enabling) {
		if (panBtn != null) panBtn.setEnabled(enabling);
		imEnabled = enabling;
	}
	
	public void recycle() {
		pan.clear();
	}

	public void setIsSymlink(boolean isSymlink) {
		this.isSymlink = isSymlink;
	}

	public String getLinkName() {
		return this.pan.getLinkName();
	}

	public String getLinkTarget() {
		int targetViewComponentId = this.pan.getLinkTarget();
		if (targetViewComponentId > 0)
			return "/" + comm.getPathForViewComponentId(targetViewComponentId);
		return "";
	}

	public void addDeleteSettingsActionListener(ActionListener al) {
		if (this.panBtn == null) this.panBtn = new PanLinkButton(this, true);
		this.panBtn.addDeleteSettingsActionListener(al);
	}

}