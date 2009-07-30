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

import static de.juwimm.cms.common.Constants.*;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import de.juwimm.cms.Messages;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanDeployStartStop extends JPanel {
	private static Logger log = Logger.getLogger(PanDeployStartStop.class);
	private JLabel lblBegin = new JLabel();
	private JPanel panBegin = new JPanel();
	private BorderLayout borderLayout1 = new BorderLayout();
	private BorderLayout borderLayout2 = new BorderLayout();
	private JPanel panEnd = new JPanel();
	private JLabel lblEnd = new JLabel();
	private BorderLayout borderLayout3 = new BorderLayout();

	public PanDeployStartStop() {
		try {
			jbInit();
			lblBegin.setText(rb.getString("panel.panDeployStartStop.begin"));
			lblEnd.setText(rb.getString("panel.panDeployStartStop.end"));
		} catch (Exception exe) {
			log.error("Initialization error", exe);
		}
	}

	public void setBeginPanel(JPanel pan) {
		panBegin.add(pan, BorderLayout.CENTER);
	}

	public void setEndPanel(JPanel pan) {
		panEnd.add(pan, BorderLayout.CENTER);
	}

	void jbInit() throws Exception {
		this.setLayout(borderLayout1);
		lblBegin.setText(Messages.getString("PanDeployStartStop.lblBegin"));
		panBegin.setLayout(borderLayout2);
		lblEnd.setText(Messages.getString("PanDeployStartStop.lblEnd"));
		panEnd.setLayout(borderLayout3);
		this.add(panEnd, BorderLayout.EAST);
		panEnd.add(lblEnd, BorderLayout.NORTH);
		this.add(panBegin, BorderLayout.WEST);
		panBegin.add(lblBegin, BorderLayout.NORTH);
	}

}