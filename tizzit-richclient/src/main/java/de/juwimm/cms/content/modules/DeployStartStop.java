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

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.frame.DlgModalModule;
import de.juwimm.cms.content.panel.PanDeployStartStop;
import de.juwimm.util.XercesHelper;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class DeployStartStop extends AbstractModule {
	private SimpleDate start = new SimpleDate();
	private SimpleDate stop = new SimpleDate();
	private PanDeployStartStop pan = new PanDeployStartStop();

	public DeployStartStop() {
		pan.setBeginPanel(start.viewPanelUI());
		pan.setEndPanel(stop.viewPanelUI());
		pan.updateUI();
	}

	public void setMandatory(boolean mandatory) {
		start.setMandatory(mandatory);
		stop.setMandatory(mandatory);
		super.setMandatory(mandatory);
	}

	public void setCustomProperties(String methodname, Properties parameters) {
		super.setCustomProperties(methodname, parameters);
	}

	public boolean isModuleValid() {
		setValidationError("");
		if (isMandatory()) {
			boolean retVal = true;
			if (!start.isModuleValid()) {
				appendValidationError(start.getValidationError());
				retVal = false;
			}
			if (!stop.isModuleValid()) {
				appendValidationError(stop.getValidationError());
				retVal = false;
			}
			return retVal;
		}
		return true;
	}

	public JPanel viewPanelUI() {
		return this.pan;
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.content.modules.Module#save(org.xml.sax.ContentHandler)
	 */
	public void load() {
	}

	public Node getProperties() {
		Element root = ContentManager.getDomDoc().createElement("deploy-start-stop");
		Node begin = this.start.getProperties();
		Node end = this.stop.getProperties();
		begin = XercesHelper.renameNode(begin, "start");
		root.appendChild(begin);
		end = XercesHelper.renameNode(end, "stop");
		root.appendChild(end);
		return root;
	}

	public void setProperties(Node node) {
		try {
			Node begin = XercesHelper.findNode(node, "./start");
			Node end = XercesHelper.findNode(node, "./stop");
			this.start.setProperties(begin);
			this.stop.setProperties(end);
		} catch (Exception exe) {
		}
	}

	public JDialog viewModalUI(boolean modal) {
		DlgModalModule frm = new DlgModalModule(this, pan, 250, 600, modal);
		frm.setVisible(true);
		return frm;
	}

	public String getPaneImage() {
		return "16_datum.gif";
	}

	public String getIconImage() {
		return "16_datum.gif";
	}

	public void setEnabled(boolean enabling) {
		start.setEnabled(enabling);
		stop.setEnabled(enabling);
	}
	
	public void recycle() {

	}
}