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

import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.w3c.dom.Node;

import de.juwimm.cms.content.frame.DlgModalModule;
import de.juwimm.cms.content.panel.PanCheckBox;
import de.juwimm.util.XercesHelper;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Sabarinath Ayyappan
 * @version $Id$
 */
public class CheckBoxModule extends AbstractModule {
	private PanCheckBox pan = new PanCheckBox();
	private ArrayList<Properties> vec = new ArrayList<Properties>();
	
	public String getPaneImage() {
		return "";
	}

	public String getIconImage() {
		return "";
	}

	public Object clone() {
		CheckBoxModule module = (CheckBoxModule) super.clone(false);
		return module;
	}

	public void setCustomProperties(String methodname, Properties parameters) {
		super.setCustomProperties(methodname, parameters);
		pan.setCustomProperties(methodname, parameters);
		vec.add(parameters);
	}

	public boolean isModuleValid() {
		return true;
	}

	public JPanel viewPanelUI() {
		return pan;
	}

	public JDialog viewModalUI(boolean modal) {
		DlgModalModule frm = new DlgModalModule(this, pan, 150, 450, modal);
		frm.setVisible(true);
		return frm;
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.content.modules.Module#save(org.xml.sax.ContentHandler)
	 */
	public void load() {
	}

	public void setProperties(Node node) {
		setDescription(XercesHelper.getNodeValue(node.getFirstChild()));
		pan.setProperties(node);
	}

	public Node getProperties() {
		Node node = pan.getProperties();
		try {
			setDescription(XercesHelper.getNodeValue(node.getFirstChild()));
		} catch (Exception exe) {
		}
		return node;
	}

	/**
	 * Function which enables the toolbar button
	 * @param enabling
	 */
	public void setEnabled(boolean enabling) {
		pan.setEnabled(enabling);
	}
	
	public void recycle() {
		pan.clear();
	}
}