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

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.Messages;
import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.util.UIConstants;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */

public class PanQuotation extends JPanel {
	private static Logger log = Logger.getLogger(PanQuotation.class);
	private JLabel lbl = new JLabel();
	private JTextField txt = new JTextField();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private EventListenerList listenerList = new EventListenerList();
	private String strName = "";
	private JTextField txtFootnode = new JTextField();
	private JLabel jLabel1 = new JLabel();

	public PanQuotation() {
		try {
			jbInit();
		} catch (Exception exe) {
			log.error("initialization problem", exe);
		}
	}

	void jbInit() throws Exception {
		this.setLayout(gridBagLayout1);
		this.txt.setMaximumSize(new Dimension(160, 23));
		this.txt.setPreferredSize(new Dimension(160, 23));
		this.getLabel().setFont(new java.awt.Font("Dialog", 1, 12));
		getLabel().setText(Messages.getString("PanQuotation.quote"));
		txtFootnode.setPreferredSize(new Dimension(160, 23));
		jLabel1.setText(Messages.getString("PanQuotation.footNote"));
		this.add(txt, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		this.add(getLabel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 0), 42, 0));
		this.add(txtFootnode, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		this.add(jLabel1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
	}

	public boolean isModuleValid() {
		boolean retVal = false;
		if (this.txt.equals("")) {
			JOptionPane.showMessageDialog(UIConstants.getMainFrame(), Messages.getString("PanQuotation.errorMandatory"));
		} else {
			retVal = true;
		}
		return retVal;
	}

	public void setProperties(Node prop) {
		if (prop != null) {
			try {
				prop.getFirstChild().getAttributes().getNamedItem("footnode").getNodeValue();
				prop = prop.getFirstChild();
			} catch (Exception exe) {
			}

			try {
				this.txt.setText(XercesHelper.getNodeValue(prop));
			} catch (Exception e) {
			}
			try {
				this.txtFootnode.setText(prop.getAttributes().getNamedItem("footnode").getNodeValue());
			} catch (Exception exe) {
			}
		}
	}
	
	public void clear() {
		this.txt.setText("");
		this.txtFootnode.setText("");
	}

	public Node getProperties() {
		Element root = ContentManager.getDomDoc().createElement("root");
		Element elm = ContentManager.getDomDoc().createElement("quotation");
		root.appendChild(elm);
		CDATASection txtNode = ContentManager.getDomDoc().createCDATASection(this.txt.getText());
		elm.setAttribute("footnode", this.txtFootnode.getText());
		elm.appendChild(txtNode);
		return root;
	}

	/**
	 * @param lbl The lbl to set.
	 */
	public void setLabel(JLabel lbl) {
		this.lbl = lbl;
	}

	/**
	 * @return Returns the lbl.
	 */
	public JLabel getLabel() {
		return lbl;
	}
}