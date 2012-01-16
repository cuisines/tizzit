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

import static de.juwimm.cms.client.beans.Application.getBean;
import static de.juwimm.cms.common.Constants.rb;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.deploy.EditorController;
import de.juwimm.cms.deploy.frame.Wizard;
import de.juwimm.cms.util.Communication;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanEditorStart extends JPanel implements WizardPanel, ActionListener {
	private static Logger log = Logger.getLogger(PanEditorStart.class);
	private Wizard wizard;
	private final JLabel lblProsa = new JLabel();
	private final JRadioButton optApprove = new JRadioButton();
	private final JRadioButton optDeleteOldEditions = new JRadioButton();
	private final JRadioButton optSendMessage = new JRadioButton();
	private final GridBagLayout gridBagLayout1 = new GridBagLayout();
	private ButtonGroup btnGrpOptions = new ButtonGroup();

	public PanEditorStart() {
		try {
			jbInit();
			deployAllow(null);
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	public PanEditorStart(Integer unitId) {
		try {
			jbInit();
			deployAllow(unitId);
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	public void setWizard(Wizard wizard) {
		this.wizard = wizard;
	}

	public void save() {
	}

	public void setUnitId(int unitId) {
	}

	void jbInit() throws Exception {
		lblProsa.setForeground(Color.black);
		lblProsa.setHorizontalAlignment(SwingConstants.LEFT);
		lblProsa.setHorizontalTextPosition(SwingConstants.LEFT);
		lblProsa.setText("<html>Bitte beschreiben Sie, was f�r eine Aufgabe Sie erledigen m�chten.<br></html>");
		lblProsa.setVerticalAlignment(SwingConstants.TOP);
		lblProsa.setVerticalTextPosition(SwingConstants.TOP);
		this.setLayout(gridBagLayout1);
		optApprove.setHorizontalTextPosition(SwingConstants.TRAILING);
		optApprove.setText("<html>Bisher freigegebene Seiten deployen</html>");
		optApprove.setActionCommand(new Integer(EditorController.STAGE_APPROVE_COMMENT).toString());
		optApprove.setVerticalAlignment(SwingConstants.CENTER);
		optApprove.addActionListener(this);
		optDeleteOldEditions.setText("<html>Alte Editionen l�schen oder online stellen</html>");
		optDeleteOldEditions.addActionListener(this);
		optDeleteOldEditions.setActionCommand(new Integer(EditorController.STAGE_OLDEDITIONSDELETE_SHOWLIST).toString());
		optSendMessage.setText("<html>Eine Nachricht an einen Autor senden</html>");
		optSendMessage.addActionListener(this);
		optSendMessage.setActionCommand(new Integer(EditorController.STAGE_SEND_COMMENT).toString());
		this.add(lblProsa, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 59, 17));
		this.add(optApprove, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 50, 20, 50), 1, -5));
//		this.add(optDeleteOldEditions, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 50, 20, 50), 37, -6));
		this.add(optSendMessage, new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 50, 0, 50), 0, 0));
		getBtnGrpOptions().add(optApprove);
		getBtnGrpOptions().add(optDeleteOldEditions);
		getBtnGrpOptions().add(optSendMessage);
		if (rb != null) {
			lblProsa.setText(rb.getString("wizard.author.start.lblProsa"));
			optApprove.setText(rb.getString("wizard.editor.start.approve"));
			optDeleteOldEditions.setText(rb.getString("wizard.editor.start.deleteOldEditions"));
			optSendMessage.setText(rb.getString("wizard.editor.start.sendMessage"));
		}
	}

	public void actionPerformed(ActionEvent ae) {
		wizard.setNextEnabled(true);
	}

	private final void deployAllow(Integer unitId) {
		Communication com = ((Communication) getBean(Beans.COMMUNICATION));
		boolean deployPossible = true;
		if (unitId != null) {
			try {
				deployPossible = com.isViewComponentPublishable(com.getViewComponent4Unit(unitId).getViewComponentId());
			} catch (Exception e) {

			}
		}

		if (!(com.isUserInRole(UserRights.DEPLOY) && deployPossible)) {
			try {
				this.removeAll();
				this.setLayout(new BorderLayout());
				JLabel lab = new JLabel();
				lab.setText(rb.getString("exception.deployCurrentlyBlocked"));
				this.add(lab, BorderLayout.CENTER);
			} catch (Exception ex) {
				log.error(ex.getMessage());
			}
		}
	}

	/**
	 * @param btnGrpOptions The btnGrpOptions to set.
	 */
	public void setBtnGrpOptions(ButtonGroup btnGrpOptions) {
		this.btnGrpOptions = btnGrpOptions;
	}

	/**
	 * @return Returns the btnGrpOptions.
	 */
	public ButtonGroup getBtnGrpOptions() {
		return btnGrpOptions;
	}
}
