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
 * Created on 14.09.2004
 */
package de.juwimm.cms.content.modules;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * A class implements the Explodeable interface to indicate that is has a JPanel
 * to explode in an extra window. Therefor it needs a panel for the button to invoke
 * the explode.
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public interface Explodeable {

	/**
	 * This method returns the panel to explode.
	 * @return The panel to explode
	 */
	public JPanel getPanContent();

	/**
	 * This method returns the title for the exploded window 
	 * @return The title for the exploded window
	 */
	public String getExplodeWindowTitle();

	/**
	 * This method adds the button to the panel. 
	 * @param button The button to add
	 */
	public void addExplodeButton(JButton button);
}
