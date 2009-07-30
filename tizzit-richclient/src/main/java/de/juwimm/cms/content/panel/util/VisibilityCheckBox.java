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
package de.juwimm.cms.content.panel.util;

import static de.juwimm.cms.common.Constants.*;

import javax.swing.Icon;
import javax.swing.JCheckBox;

import de.juwimm.cms.content.panel.AbstractTreePanel;
import de.juwimm.cms.content.panel.AbstractTreePanel.CheckActionListener;

/**
 * A CheckBox implementation for taking the visible state of components. 
 * This checkboxautomatically notifies its {@link AbstractTreePanel} when checked or de-checked
 * 
 * @author <a href="mailto:christiane.hausleiter@juwimm.com">Christiane Hausleiter</a>
 *
 */
public class VisibilityCheckBox extends JCheckBox {
	

	/**
	 * The default constructor initializes the instance.
	 */
	public VisibilityCheckBox(CheckActionListener caListener) {
		this(caListener, null, null, false);
	}

	/**
	 * @param icon
	 */
	public VisibilityCheckBox(CheckActionListener caListener, Icon icon) {
		this(caListener, null, icon, false);
	}

	/**
	 * @param text
	 */
	public VisibilityCheckBox(CheckActionListener caListener, String text) {
		this(caListener, text, null, false);
	}

	/**
	 * @param icon
	 * @param selected
	 */
	public VisibilityCheckBox(CheckActionListener caListener, Icon icon, boolean selected) {
		this(caListener, null, icon, selected);
	}

	/**
	 * @param text
	 * @param selected
	 */
	public VisibilityCheckBox(CheckActionListener caListener, String text, boolean selected) {
		this(caListener, text, null, selected);
	}

	/**
	 * @param text
	 * @param icon
	 */
	public VisibilityCheckBox(CheckActionListener caListener, String text, Icon icon) {
		this(caListener, text, icon, false);
	}

	/**
	 * @param text
	 * @param icon
	 * @param selected
	 */
	public VisibilityCheckBox(CheckActionListener caListener, String text, Icon icon, boolean selected) {
		super(text, icon, selected);
		setToolTipText(rb.getString("PanDBC.visibility"));
		addActionListener(caListener);
	}

}
