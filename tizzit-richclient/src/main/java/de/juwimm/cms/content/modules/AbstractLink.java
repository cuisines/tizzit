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

import java.util.HashMap;
import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import de.juwimm.cms.content.frame.DlgModalModule;
import de.juwimm.cms.content.panel.PanAbstractLink;

/**
 * AbstractLink is a placeholder for internal or external links or documents on a page outside of a textfield.<br/>
 * <p>
 * At runtime the user can insert one concrete link instead of this abstract one.<br/>
 * </p>
 * <p>
 * This module can be parameterized like internal or external links in the DCF.<br/>
 * These additional parameters from the DCF are passed to the concrete link after it is dedicated.<br/>
 * </p> 
 * @see de.juwimm.cms.content.modules.InternalLink
 * @see de.juwimm.cms.content.modules.ExternalLink
 * <br/>
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class AbstractLink extends AbstractModule {
	private static Logger log = Logger.getLogger(AbstractLink.class);
	private PanAbstractLink pan = null;
	private boolean imEnabled = false;
	private HashMap<String, Properties> customPropertiesMap = new HashMap<String, Properties>();

	public void setCustomProperties(String methodname, Properties parameters) {
		super.setCustomProperties(methodname, parameters);
		this.customPropertiesMap.put(methodname, parameters);
	}

	public JDialog viewModalUI(boolean modal) {
		if (this.getPan().getSelectedLinkType() != null) { return this.getPan().getSelectedLinkType().viewModalUI(modal); }
		int frameHeight = 520;
		int frameWidth = 444;
		DlgModalModule frm = new DlgModalModule(this, this.getPan(), frameHeight, frameWidth, modal);
		frm.setVisible(true);
		return frm;
	}

	public JPanel viewPanelUI() {
		if (this.getPan().getSelectedLinkType() != null) {
			this.getPan().getSelectedLinkType().setEnabled(imEnabled);
		}
		this.getPan().setEnabled(imEnabled);
		return this.getPan();
	}

	public boolean isModuleValid() {
		if (isMandatory()) {
			if (this.getPan().getSelectedLinkType() == null) {
				setValidationError(rb.getString("exception.LinkRequired"));
				return false;
			}
			this.getPan().getSelectedLinkType().setMandatory(true);
			boolean retVal = true; // this.getPan().isModuleValid();
			setValidationError(this.getPan().getSelectedLinkType().getValidationError());
			return retVal;
		}
		return true;
	}

	public void load() {
	}

	public Node getProperties() {
		return this.getPan().getProperties();
	}

	public void setProperties(Node node) {
		this.getPan().setProperties(node);
	}

	public String getPaneImage() {
		try {
			return getURLEncodedISO("svgexternallink." + getDescription());
		} catch (Exception exe) {
			log.error("Error getting pane image", exe);
			return "16_komp_externallink.gif";
		}
	}

	public String getIconImage() {
		return "16_komp_externallink.gif";
	}

	public void setEnabled(boolean enabling) {
		if (this.getPan().getSelectedLinkType() != null) {
			this.getPan().setEnabled(enabling);
			this.getPan().getSelectedLinkType().setEnabled(enabling);
		} else {
			this.getPan().setEnabled(enabling);
		}
		imEnabled = enabling;
	}

	public void recycle() {
		if (this.getPan().getSelectedLinkType() != null) {
			this.getPan().getSelectedLinkType().recycle();
		}
	}

	private PanAbstractLink getPan() {
		if (this.pan == null) {
			this.pan = new PanAbstractLink(this.customPropertiesMap);
		}
		return this.pan;
	}

}