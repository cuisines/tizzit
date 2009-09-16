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
import de.juwimm.cms.content.panel.PanStickyPad;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class StickyPadModule extends AbstractModule {
	private static Logger log = Logger.getLogger(StickyPadModule.class);
	private PanStickyPad pan = new PanStickyPad();

	public String getPaneImage() {
		try {
			return getURLEncoded("svgzitat." + getDescription());
		} catch (Exception exe) {
			log.error("Error returning pane image", exe);
			return "16_komp_zitat.gif";
		}
	}

	public String getIconImage() {
		return "16_komp_zitat.gif";
	}

	public void setCustomProperties(String methodname, Properties parameters) {
		super.setCustomProperties(methodname, parameters);
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
		return pan.getProperties();
	}

	public void setEnabled(boolean enabling) {
		pan.setEnabled(enabling);
	}
	
	public void recycle() {
		pan.setText("");
	}
}