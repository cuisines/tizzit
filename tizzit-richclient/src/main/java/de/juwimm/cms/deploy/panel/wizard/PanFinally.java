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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import de.juwimm.cms.deploy.frame.Wizard;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */

public class PanFinally extends JPanel implements WizardPanel {
	private static Logger log = Logger.getLogger(PanFinally.class);
	private JLabel lblFinallyMessage = new JLabel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private String way = "";

	public PanFinally() {
		try {
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	void jbInit() throws Exception {
		lblFinallyMessage.setFont(new java.awt.Font("Dialog", 0, 12));
		lblFinallyMessage.setBorder(BorderFactory.createLoweredBevelBorder());
		lblFinallyMessage.setText("");
		lblFinallyMessage.setVerticalAlignment(SwingConstants.TOP);
		lblFinallyMessage.setVerticalTextPosition(SwingConstants.TOP);
		this.setLayout(gridBagLayout1);
		this.add(lblFinallyMessage, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(60, 50, 60, 50), 0, 0));
	}

	public void setWizard(Wizard wizard) {
		wizard.setNextAsFinally(true);
	}

	public void setUnitId(int unitId) {
	}

	public void save() {
	}

	public void setProsa(String prosa) {
		this.lblFinallyMessage.setText(prosa);
	}

	public void setWay(String way) {
		this.way = way;
	}

	public String getWay() {
		return this.way;
	}
}