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

import static de.juwimm.cms.common.Constants.*;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.apache.log4j.Logger;

import de.juwimm.cms.deploy.RootController;
import de.juwimm.cms.deploy.frame.Wizard;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanRootStart extends JPanel implements WizardPanel, ActionListener {
	private static Logger log = Logger.getLogger(PanRootStart.class);
	private Wizard wizard;
	private JLabel lblProsa = new JLabel();
	private JRadioButton optDeployRoot = new JRadioButton();
	private JRadioButton optDeployAll = new JRadioButton();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private ButtonGroup btnGrpOptions = new ButtonGroup();

	public PanRootStart() {
		try {
			jbInit();
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
		optDeployRoot.setText("ROOTDEPLOY");
		optDeployRoot.addActionListener(this);
		optDeployRoot.setActionCommand(new Integer(RootController.STAGE_DEPLOY_ROOT).toString());
		optDeployAll.setText("DEPLOY EVERYTHING");
		optDeployAll.addActionListener(this);
		optDeployAll.setActionCommand(new Integer(RootController.STAGE_DEPLOY_ALL_WITH_ROOT).toString());
		this.add(lblProsa, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 59, 17));
		this.add(optDeployRoot, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 50, 20, 50), 37, -6));
		this.add(optDeployAll, new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(0, 50, 0, 50), 0, 0));
		getBtnGrpOptions().add(optDeployRoot);
		getBtnGrpOptions().add(optDeployAll);
		if (rb != null) {
			lblProsa.setText(rb.getString("wizard.author.start.lblProsa"));
			optDeployRoot.setText(rb.getString("wizard.root.start.deployRoot"));
			optDeployAll.setText(rb.getString("wizard.root.start.deployAll"));
		}
	}

	public void actionPerformed(ActionEvent ae) {
		wizard.setNextEnabled(true);
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
