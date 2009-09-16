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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.modules.Module;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanSimpleTextfield extends JPanel {
	private Logger log = Logger.getLogger(PanSimpleTextfield.class);
	private JTextField txtTextfield = new JTextField();

	public PanSimpleTextfield() {
		try {
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization error", exe);
		}
	}

	public PanSimpleTextfield(Module module) {
		this();
	}

	void jbInit() throws Exception {
		this.setLayout(new GridBagLayout());

		txtTextfield.setMinimumSize(new Dimension(4, 22));
		txtTextfield.setPreferredSize(new Dimension(4, 22));
		this.add(txtTextfield, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 440, 0));
	}

	public void setProperties(Node prop) {
		if(prop == null) {
			this.txtTextfield.setText("");	
		} else {
			this.txtTextfield.setText(XercesHelper.getNodeValue(prop));
		}
	}
	

	public Node getProperties() {
		Element elm = ContentManager.getDomDoc().createElement("simpleTextfield");
		CDATASection txtNode = ContentManager.getDomDoc().createCDATASection(this.txtTextfield.getText());
		elm.appendChild(txtNode);
		return elm;
	}

	public boolean isModuleValid() {
		return !this.txtTextfield.getText().equals("");
	}

	public void setEnabled(boolean enabling) {
		this.txtTextfield.setEnabled(enabling);
	}

}