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

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JTextArea;

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
public class PanSimpleTextArea extends JPanel {
	private Logger log = Logger.getLogger(PanSimpleTextArea.class);
	private JTextArea txtArea = new JTextArea();

	public PanSimpleTextArea() {
		try {
			jbInit();
		} catch (Exception exe) {
			log.error("initialization problem", exe);
		}
	}

	public PanSimpleTextArea(Module module) {
		this();
	}

	void jbInit() throws Exception {
		txtArea.setDoubleBuffered(true);
		txtArea.setAutoscrolls(true);
		txtArea.setWrapStyleWord(true);
		txtArea.setLineWrap(true);
		txtArea.setEditable(false);
		txtArea.setText("");
		txtArea.setFont(new java.awt.Font("SansSerif", 0, 12));
		txtArea.setBackground(this.getBackground());
		this.setLayout(new BorderLayout());
		this.add(txtArea, BorderLayout.CENTER);
	}

	public void setProperties(Node prop) {
		if(prop == null) {
			this.txtArea.setText("");
		} else {
			this.txtArea.setText(XercesHelper.getNodeValue(prop));	
		}
	}

	public Node getProperties() {
		Element elm = null;
		elm = ContentManager.getDomDoc().createElement("SimpleTextArea");
		CDATASection txtNode = ContentManager.getDomDoc().createCDATASection(this.txtArea.getText());
		elm.appendChild(txtNode);
		return elm;
	}

	public void setEnabled(boolean enabled) {
		this.txtArea.setEditable(enabled);
		if (enabled) {
			txtArea.setBackground(Color.WHITE);
		} else {
			txtArea.setBackground(this.getBackground());
		}
	}

	public void setMonospaced(boolean isMonospaced) {
		if (isMonospaced) {
			this.txtArea.setFont(new java.awt.Font("Monospaced", 0, 12));
		} else {
			this.txtArea.setFont(new java.awt.Font("SansSerif", 0, 12));
		}
	}

}