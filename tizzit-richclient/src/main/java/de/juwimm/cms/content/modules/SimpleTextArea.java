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

import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Node;

import de.juwimm.cms.content.frame.DlgModalModule;
import de.juwimm.cms.content.panel.PanSimpleTextArea;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class SimpleTextArea extends AbstractModule {
	private static Logger log = Logger.getLogger(SimpleTextArea.class);
	protected PanSimpleTextArea pan = new PanSimpleTextArea(this);
	protected boolean isEditable = false;

	public void setCustomProperties(String methodname, Properties parameters) {
		super.setCustomProperties(methodname, parameters);
		if (methodname != null && "viewOption".equalsIgnoreCase(methodname)) {
			if (parameters != null) {
				this.isEditable = Boolean.valueOf(parameters.getProperty("editable", "false")).booleanValue();
			}
		}
	}

	/**
	 * This module is always valid. Therefor it will return true.
	 * @return true
	 */
	public boolean isModuleValid() {
		return true;
	}

	public JPanel viewPanelUI() {
		return this.pan;
	}

	public JDialog viewModalUI(boolean modal) {
		// if shown as component inside wysiwyg set enabled
		this.isEditable = true;
		this.setEnabled(true);
		DlgModalModule frm = new DlgModalModule(this, this.pan, 150, 450, modal);
		frm.setVisible(true);
		return frm;
	}

	public void load() {
	}

	public void setProperties(Node node) {
		setDescription(XercesHelper.getNodeValue(node));
		this.pan.setProperties(node);
	}

	public Node getProperties() {
		return this.pan.getProperties();
	}

	public String getPaneImage() {
		try {
			return getURLEncodedISO("svgsubline.Text");
		} catch (Exception exe) {
			log.error("Error returning pane image", exe);
			return "16_komp_monospaced.gif";
		}
	}

	public String getIconImage() {
		return "16_komp_monospaced.gif";
	}

	public void setEnabled(boolean enabling) {
		this.pan.setEnabled(this.isEditable && enabling);
	}
	
	public void recycle() {
		pan.setProperties(null);
	}
}