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
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.Messages;
import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.modules.Module;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanSimpleHTMLArea extends JPanel {
	private static Logger log = Logger.getLogger(PanSimpleHTMLArea.class);
	private JEditorPane txtHTMLArea = new JEditorPane();
	private JScrollPane areaScrollPane = new JScrollPane(txtHTMLArea);
	private Module module;
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private String url = "";
	private String text = "";

	public PanSimpleHTMLArea() {
		try {
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization error", exe);
		}
	}

	public PanSimpleHTMLArea(Module module) {
		this();
		this.module = module;
	}

	void jbInit() throws Exception {
		txtHTMLArea.setMaximumSize(new Dimension(2147483647, 2147483647));
		txtHTMLArea.setEditable(false);
		areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setPreferredSize(new Dimension(100, 100));
		areaScrollPane.setBorder(BorderFactory.createCompoundBorder(
								BorderFactory.createCompoundBorder(
								BorderFactory.createTitledBorder(Messages.getString("PanSimpleHTMLArea.borderName")),  
								BorderFactory.createEmptyBorder(5, 5, 5, 5)), areaScrollPane.getBorder()));
		areaScrollPane.setMaximumSize(new Dimension(2147483647, 2147483647));
		this.setLayout(gridBagLayout1);
		/*
		 txtTextArea.setFont(new Font("Arial", Font.ITALIC, 14));
		 txtTextArea.setText("");
		 txtTextArea.setLineWrap(true);
		 txtTextArea.setTabSize(4);
		 txtTextArea.setWrapStyleWord(true);
		 txtTextArea.setAutoscrolls(true);
		 txtTextArea.setDoubleBuffered(true);
		 this.setBorder(BorderFactory.createEtchedBorder());
		 */
		this.setMaximumSize(new Dimension(2147483647, 2147483647));
		this.add(areaScrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 4, 5), 440, 0));
	}

	public void setProperties(Node prop) {
		//this.txtHTMLArea.setText(XercesHelper.getNodeValue(prop));
		String strTemp = XercesHelper.getNodeValue(prop);
		if (strTemp.indexOf("http") == -1) {
			this.url = "";
			this.text = strTemp;
			txtHTMLArea.setContentType("text/html");
			txtHTMLArea.setText(this.text);
		} else {
			this.text = "";
			this.url = strTemp;
			try {
				txtHTMLArea.setPage(this.url);
			} catch (IOException e) {
				System.err.println("Attempted to read a bad URL: " + url);
			}
		}
	}
	
	public void clean() {
		this.url = "";
		this.text = "";
		txtHTMLArea.setContentType("text/html");
		txtHTMLArea.setText(this.text);
	}

	public Node getProperties() {
		Element elm = null;
		CDATASection txtNode = null;
		elm = ContentManager.getDomDoc().createElement("simpleTextArea");
		if (this.url.equals("")) {
			txtNode = ContentManager.getDomDoc().createCDATASection(this.text);
		} else {
			txtNode = ContentManager.getDomDoc().createCDATASection(this.url);
		}
		elm.appendChild(txtNode);
		return elm;
	}

	public void setEnabled(boolean enable) {
		txtHTMLArea.setEditable(enable);
	}
}