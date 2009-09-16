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

import static de.juwimm.cms.common.Constants.*;

import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.tizzit.util.XercesHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.event.EditpaneFiredEvent;
import de.juwimm.cms.content.panel.PanOnlyButton;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class Anchor extends AbstractModule {
	private boolean imEnabled = true;
	private String anchor = "";
	private PanOnlyButton panBtn;

	public void setCustomProperties(String methodname, Properties parameters) {
		super.setCustomProperties(methodname, parameters);
	}

	/**
	 * We haven't implemented the isValid Method here, because this Module should only be used inside a WYSIWYG.<br>
	 * Therefor we also can open a JOptionPane.
	 *
	 * @return Everytime true here
	 */
	public boolean isModuleValid() {
		if (isMandatory()) {
			boolean retVal = true;

			setValidationError("");
			if (anchor == null || anchor.equalsIgnoreCase("")) {
				appendValidationError(rb.getString("exception.AnchorRequired"));
				retVal = false;
			}
			return retVal;
		}
		return true;
	}

	public JDialog viewModalUI(boolean modal) {
		Thread t = new Thread(new ShowOptionPane(this));
		if (modal) {
			t.run();
		} else {
			t.setPriority(Thread.NORM_PRIORITY);
			t.start();
		}
		return null;
	}

	/**
	 * 
	 */
	private class ShowOptionPane implements Runnable {
		private Module module = null;

		public ShowOptionPane(Module module) {
			this.module = module;
		}

		public void run() {
			String newAnchor = JOptionPane.showInputDialog(rb.getString("content.modules.anchor.insertAnchorName"),
					anchor);
			if (newAnchor != null && !newAnchor.equals("")) {
				anchor = newAnchor;
				EditpaneFiredEvent efe = new EditpaneFiredEvent(module);
				runEditpaneFiredEvent(efe);
				setSaveable(true);
			} else if (newAnchor == null) {
				EditpaneFiredEvent efe = new EditpaneFiredEvent(module);
				runEditpaneCancelEvent(efe);
				setSaveable(false);
			}
		}
	}

	public JPanel viewPanelUI() {
		panBtn = new PanOnlyButton(this, true);
		panBtn.setEnabled(imEnabled);
		return panBtn;
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.content.modules.Module#save(org.xml.sax.ContentHandler)
	 */
	public void load() {
	}

	public Node getProperties() {
		Element root = ContentManager.getDomDoc().createElement("root");
		Element elm = ContentManager.getDomDoc().createElement("a");
		elm.setAttribute("name", getURLEncoded(anchor));
		elm.setAttribute("type", "anchor");
		setDescription(anchor);
		root.appendChild(elm);
		return root;
	}

	public void setProperties(Node node) {
		if (node != null) {
			try {
				anchor = ((Element) XercesHelper.findNode(node, "./a")).getAttribute("name");
				anchor = AbstractModule.getURLDecoded(anchor);
			} catch (Exception exe) {
			}
		}
	}

	public String getPaneImage() {
		return "16_komp_anchor.png";
	}

	public String getIconImage() {
		return "16_komp_anchor.png";
	}

	public void setEnabled(boolean enabling) {
		if (panBtn != null) panBtn.setEnabled(enabling);
		imEnabled = enabling;
	}
	
	public void recycle() {
		
	}
}