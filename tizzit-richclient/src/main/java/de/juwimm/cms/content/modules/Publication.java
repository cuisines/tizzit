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
import de.juwimm.cms.content.panel.PanPublication;
import de.juwimm.util.XercesHelper;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class Publication extends AbstractModule {
	private PanPublication pan = new PanPublication();

	public void setCustomProperties(String methodname, Properties parameters) {
		super.setCustomProperties(methodname, parameters);
	}

	public boolean isModuleValid() {
		setValidationError("");
		if (isMandatory()) {
			String valMsg = pan.getValidationError();
			if (valMsg.equals("")) {
				return true;
			}
			setValidationError(valMsg);
			return false;
		}
		return true;
	}

	public JPanel viewPanelUI() {
		return pan;
	}

	public JDialog viewModalUI(boolean modal) {
		DlgModalModule frm = new DlgModalModule(this, pan, 430, 430, modal);
		frm.setVisible(true);
		return frm;
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.content.modules.Module#save(org.xml.sax.ContentHandler)
	 */
	public void load() {
	}

	public Node getProperties() {
		Node node = pan.getProperties();
		setDescription(XercesHelper.getNodeValue(node, "./pubhead"));
		return node;
	}

	public void setProperties(Node node) {
		setDescription(XercesHelper.getNodeValue(node, "./pubhead"));
		this.pan.setProperties(node);
	}

	public String getPaneImage() {
		try {
			return getURLEncodedISO("svgpublikation." + getDescription());
		} catch (Exception exe) {
			return "16_komp_publikation.gif";
		}
	}

	public String getIconImage() {
		return "16_komp_publikation.gif";
	}

	public void setEnabled(boolean enabling) {
	}
	
	public void recycle() {
		
	}
}