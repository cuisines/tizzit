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
package de.juwimm.cms.gui.views;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import de.juwimm.cms.gui.controls.UnloadablePanel;

/**
 * <p>Title: ConQuest </p>
 * <p>Description: Content Management System</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public final class PanInitView extends JPanel implements UnloadablePanel {
	private static Logger log = Logger.getLogger(PanInitView.class);
	private JLabel lblIcon = new JLabel(new ImageIcon(getClass().getResource("/images/cms_450x121.png")));
	private static PanInitView pan = new PanInitView();

	private PanInitView() {
		try {
			setDoubleBuffered(true);
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	public static PanInitView getInstance() {
		return pan;
	}

	void jbInit() throws Exception {
		this.setLayout(new BorderLayout());
		this.add(lblIcon, BorderLayout.CENTER);
	}

	public void unload() {

	}
}
