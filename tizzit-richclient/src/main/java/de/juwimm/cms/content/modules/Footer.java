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

import org.w3c.dom.Node;

import de.juwimm.cms.content.frame.DlgModalModule;
import de.juwimm.cms.content.panel.PanFooter;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class Footer extends AbstractModule {
	private PanFooter pan = new PanFooter();

	public void setCustomProperties(String methodname, Properties parameters) {
		super.setCustomProperties(methodname, parameters);
	}

	/**
	 * The Footer is always true
	 * @return true
	 */
	public boolean isModuleValid() {
		return true;
	}

	public JPanel viewPanelUI() {
		return pan;
	}

	public JDialog viewModalUI(boolean modal) {
		DlgModalModule frm = new DlgModalModule(this, pan, 190, 380, modal);
		frm.setVisible(true);
		return frm;
	}

	public void load() {
	}

	public Node getProperties() {
		return pan.getProperties();
	}

	public void setProperties(Node node) {
		pan.setProperties(node);
	}

	public String getIconImage() {
		return "";
	}

	public String getPaneImage() {
		return "";
	}

	public void setEnabled(boolean enabling) {
		pan.setEnabled(enabling);
	}
	
	public void recycle() {
		
	}
}