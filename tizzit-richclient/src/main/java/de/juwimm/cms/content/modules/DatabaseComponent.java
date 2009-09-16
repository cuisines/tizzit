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

import static de.juwimm.cms.client.beans.Application.*;
import static de.juwimm.cms.common.Constants.*;

import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.event.EditpaneFiredEvent;
import de.juwimm.cms.content.event.EditpaneFiredListener;
import de.juwimm.cms.content.frame.DlgModalModule;
import de.juwimm.cms.content.panel.PanDBComponent;
import de.juwimm.cms.content.panel.PanOnlyButton;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class DatabaseComponent extends AbstractModule implements EditpaneFiredListener {
	private static Logger log = Logger.getLogger(DatabaseComponent.class);
	private PanDBComponent pan;
	private PanOnlyButton panBtn;
	private boolean setPropertiesCalled = false;
	private boolean imEnabled = true;

	public void setCustomProperties(String methodname, Properties parameters) {
		super.setCustomProperties(methodname, parameters);
	}

	public JDialog viewModalUI(boolean modal) {
		if (!setPropertiesCalled) { // we will show the actual unit inside the DB Components
			createEmptyContent();
		}
		if (pan == null) pan = new PanDBComponent();
		int frameWidth = 600;
		int frameHeight = 530;
		addEditpaneFiredListener(this);
		DlgModalModule frm = new DlgModalModule(this, pan, frameHeight, frameWidth, modal);
		pan.setFrmModalModule(frm);
		frm.setOkButtonText(rb.getString("dialog.saveToContent"));
		frm.setVisible(true);
		return frm;
	}

	private void createEmptyContent() {
		pan = new PanDBComponent();
		Element node = ContentManager.getDomDoc().createElement("root");
		Element inclElement = ContentManager.getDomDoc().createElement("include");
		node.appendChild(inclElement);
		inclElement.setAttribute("type", "unit");
		String unitId = Integer.toString(((ContentManager) getBean(Beans.CONTENT_MANAGER)).getActUnitId());
		inclElement.setAttribute("id", unitId);
		this.pan.setAggregationXml(node);
	}

	public JPanel viewPanelUI() {
		panBtn = new PanOnlyButton(this, false);
		panBtn.setEnabled(imEnabled);
		return panBtn;
	}

	public boolean isModuleValid() {
		setValidationError("");
		if (isMandatory() && this.pan.getAggregationXml() == null) {
			setValidationError(rb.getString("exception.DatabaseComponentRequired"));
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.content.modules.Module#save(org.xml.sax.ContentHandler)
	 */
	public void load() {
	}

	public Node getProperties() {
		if (pan != null) {
			Node node = this.pan.getAggregationXml();
			if (node != null)
				setDescription(node.getAttributes().getNamedItem("description").getNodeValue());
			else {
				node = ContentManager.getDomDoc().createElement("aggregation");
			}
			return node;
		}
		return null;
	}

	public void setProperties(Node node) {
		if (node != null) {
			setPropertiesCalled = true;
			if (node.getFirstChild() != null) {
				pan = new PanDBComponent();
				this.pan.setAggregationXml(node);
			} else {
				createEmptyContent();
			}
		}
	}

	public String getPaneImage() {
		try {
			return getURLEncodedISO("svgaggregation." + getDescription());
		} catch (Exception exe) {
			log.error("Error returning pane image", exe);
			return "16_aggregation.gif";
		}
	}

	public String getIconImage() {
		return "16_aggregation.gif";
	}

	/**
	 * Enables or disables the toolbar button.
	 * @param enabling a boolean describing the enabling state
	 */
	public void setEnabled(boolean enabling) {
		if (panBtn != null) panBtn.setEnabled(enabling);
		imEnabled = enabling;
	}
	
	public void recycle() {	}

	/** @see de.juwimm.cms.content.event.EditpaneFiredListener#editpaneCancelPerformed(de.juwimm.cms.content.event.EditpaneFiredEvent) */
	public void editpaneCancelPerformed(EditpaneFiredEvent ae) {
		// nothing to do
	}

	/** @see de.juwimm.cms.content.event.EditpaneFiredListener#editpaneFiredPerformed(de.juwimm.cms.content.event.EditpaneFiredEvent) */
	public void editpaneFiredPerformed(EditpaneFiredEvent ae) {
		if (this.pan != null) {
			Node nodeCurrentState = this.pan.getAggregationXml();
			log.debug("Current XML for PersonDB: \n" + XercesHelper.node2string(nodeCurrentState));
			setProperties(nodeCurrentState);
		}
	}

}