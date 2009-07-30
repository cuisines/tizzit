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
/*
 * Created on 13.02.2006
 */
package de.juwimm.cms.content.panel;

import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 * Panel for choosing view for selecting a document or picture
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class PanViewSelect extends JPanel {
	private ButtonGroup bgrp = null;
	
	public PanViewSelect() {
		super();
		this.bgrp = new ButtonGroup();
		this.setLayout(new FlowLayout());
	}
	
	public void addTextButton(String buttonLabel) {
		JToggleButton btn = new JToggleButton(buttonLabel, true);
		this.add(btn);
		this.bgrp.add(btn);
		this.validate();
		this.repaint();
	}

	public void addIconButton(String pathToImage) {
		ImageIcon icon = new ImageIcon(pathToImage);
		JToggleButton btn = new JToggleButton(icon, true);
		this.add(btn);
		this.bgrp.add(btn);
		this.validate();
		this.repaint();
	}

	public void addButton(JToggleButton btn) {
		this.add(btn);
		this.bgrp.add(btn);
		this.validate();
		this.repaint();
	}

}
