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
package de.juwimm.cms.deploy.panel.wizard;

import static de.juwimm.cms.client.beans.Application.*;
import static de.juwimm.cms.common.Constants.*;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.deploy.frame.Wizard;
import de.juwimm.cms.exceptions.EditionXMLIsNotValid;
import de.juwimm.cms.exceptions.ParentUnitNeverDeployed;
import de.juwimm.cms.exceptions.PreviousUnitNeverDeployed;
import de.juwimm.cms.exceptions.UnitWasNeverDeployed;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanEditorApproveMessage extends JPanel implements WizardPanel {
	private static Logger log = Logger.getLogger(PanEditorApproveMessage.class);
	private int unitId;
	private JLabel lblMessage = new JLabel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JScrollPane jScrollPane1 = new JScrollPane();
	private JTextArea txtMessage = new JTextArea();

	public PanEditorApproveMessage() {
		try {
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	public void setWizard(Wizard wizard) {
	}

	public void save() {
		// CREATE EDITION AND SAVE THE COMMENT....
		Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
		try {
			comm.createEdition(this.txtMessage.getText(), comm.getViewComponent4Unit(this.unitId).getViewComponentId(),
					true, true);
			JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb
					.getString("wizard.deploy.editionSucessfulTransmitted"), rb.getString("dialog.title"),
					JOptionPane.INFORMATION_MESSAGE);
		} catch (ParentUnitNeverDeployed pd) {
			JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb
					.getString("SYSTEMMESSAGE_ERROR.ParentUnitNeverDeployed"), rb.getString("dialog.title"),
					JOptionPane.ERROR_MESSAGE);
		} catch (PreviousUnitNeverDeployed pu) {
			JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb
					.getString("SYSTEMMESSAGE_ERROR.PreviousUnitNeverDeployed"), rb.getString("dialog.title"),
					JOptionPane.ERROR_MESSAGE);
		} catch (UnitWasNeverDeployed ud) {
			JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb
					.getString("SYSTEMMESSAGE_ERROR.UnitWasNeverDeployed"), rb.getString("dialog.title"),
					JOptionPane.ERROR_MESSAGE);
		} catch (EditionXMLIsNotValid ex) {
			JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb
					.getString("SYSTEMMESSAGE_ERROR.EditionXMLIsNotValid"), rb.getString("dialog.title"),
					JOptionPane.ERROR_MESSAGE);
		} catch (Exception exe) {
			JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("exception.UnknownError")
					+ exe.getMessage(), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
			log.error("Error during save and create edition", exe);
		}
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	void jbInit() throws Exception {
		lblMessage.setText("Kurzbeschreibung");
		this.setLayout(gridBagLayout1);
		this.setMaximumSize(new Dimension(32767, 32767));
		txtMessage.setBorder(BorderFactory.createLoweredBevelBorder());
		txtMessage.setText("");
		txtMessage.setLineWrap(true);
		jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(lblMessage, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(20, 50, 0, 50), 0, 0));
		this.add(jScrollPane1, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 50, 90, 50), 0, 0));
		jScrollPane1.getViewport().add(txtMessage, null);
		if (rb != null) {
			lblMessage.setText(rb.getString("wizard.editor.approveMessage.lblMessage"));
		}
	}
}
