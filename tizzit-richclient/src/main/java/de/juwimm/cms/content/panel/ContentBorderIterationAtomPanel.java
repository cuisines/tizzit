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

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version $Id$
 */
public class ContentBorderIterationAtomPanel extends JPanel implements ContentBorder {
	private static Logger log = Logger.getLogger(ContentBorderIterationAtomPanel.class);
	private BorderLayout borderLayout1 = new BorderLayout();
	private Border border1;
	private TitledBorder titledBorder1;
	private JLabel jLabel1 = new JLabel();

	public ContentBorderIterationAtomPanel() {
		try {
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization problem", exe);
		}
	}

	void jbInit() throws Exception {
		border1 = BorderFactory.createEtchedBorder(Color.white, new Color(165, 163, 151));
		titledBorder1 = new TitledBorder(border1, "Aggregation");
		jLabel1.setText("jLabel1");
		this.setBorder(titledBorder1);
		this.setLayout(borderLayout1);
	}

	public void setContentModulePanel(JPanel panel) {
		this.add(panel, BorderLayout.CENTER);
	}

	public void setLabel(String strUserDescription) {
		titledBorder1 = new TitledBorder(border1, strUserDescription);
		this.setBorder(titledBorder1);
		this.revalidate();
		this.repaint();
	}
}