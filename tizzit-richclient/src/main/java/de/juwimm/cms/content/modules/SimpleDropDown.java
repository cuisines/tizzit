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

import java.awt.BorderLayout;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.frame.DlgModalModule;
import de.juwimm.swing.DropDownHolder;
import de.juwimm.util.XercesHelper;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class SimpleDropDown extends AbstractModule {
	private static Logger log = Logger.getLogger(SimpleDropDown.class);
	private JComboBox cboBox;
	private Vector<DropDownHolder> vec = new Vector<DropDownHolder>();
	private JPanel pan = new JPanel();
	private boolean imEnabled = true;

	public SimpleDropDown() {
		pan.setLayout(new BorderLayout());
		pan.setDoubleBuffered(true);
	}

	public Object clone() {
		SimpleDropDown module = (SimpleDropDown) super.clone(false);
		return module;
	}

	public JDialog viewModalUI(boolean modal) {
		DlgModalModule frm = new DlgModalModule(this, pan, 250, 250, modal);
		frm.setVisible(true);
		return frm;
	}

	public JPanel viewPanelUI() {
		return pan;
	}

	public void setCustomProperties(String methodname, Properties parameters) {
		super.setCustomProperties(methodname, parameters);
		if (methodname.equals("dropdownValues")) {
			DropDownHolder ddh = new DropDownHolder(parameters.getProperty("value"), parameters.getProperty("name"));
			vec.add(ddh);
		} else if (methodname.equals("CustomConfigurationReady")) {
			cboBox = new JComboBox(vec);
			setEnabled(imEnabled);
			pan.add(cboBox, BorderLayout.CENTER);
		}
	}

	/**
	 * Is always true, because there is always one element selected.
	 * @return true
	 */
	public boolean isModuleValid() {
		return true;
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.content.modules.Module#save(org.xml.sax.ContentHandler)
	 */
	public void load() {
	}

	public Node getProperties() {
		if (log.isDebugEnabled()) log.debug("getProperties");
		Node root = ContentManager.getDomDoc().createElement("dropdown");
		DropDownHolder ddh = (DropDownHolder) this.cboBox.getSelectedItem();
		Node value = ContentManager.getDomDoc().createElement("value");
		Node txt = ContentManager.getDomDoc().createCDATASection((String) ddh.getObject());
		value.appendChild(txt);
		root.appendChild(value);
		if (log.isDebugEnabled()) log.debug("RETURNED: " + (String) ddh.getObject());
		return root;
	}

	public void setProperties(Node node) {
		if (log.isDebugEnabled()) log.debug("setProperties");
		String value = XercesHelper.getNodeValue(node, "./value");
		Iterator it = vec.iterator();
		while (it.hasNext()) {
			DropDownHolder ddh = (DropDownHolder) it.next();
			if (log.isDebugEnabled()) log.debug("value " + ddh.getObject());
			if (((String) ddh.getObject()).equals(value)) {
				if (log.isDebugEnabled()) log.debug("SELECTED! " + value);
				this.cboBox.setSelectedItem(ddh);
				break;
			}
		}
	}

	public String getPaneImage() {
		return "16_datum.gif";
	}

	public String getIconImage() {
		return "16_datum.gif";
	}

	public void setEnabled(boolean enabling) {
		if (cboBox != null) cboBox.setEnabled(enabling);
		imEnabled = enabling;
	}
	
	public void recycle() {
		
	}
}