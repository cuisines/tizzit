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

import java.util.Hashtable;

import javax.swing.event.ChangeListener;

/**
 * <p>Title: juwimm cms</p>
 * <p><b>Copyright: JuwiMacMillan Group GmbH (c) 2002</b></p>
 * Interface for simulating multiple inheritance for the implementation-class AbstractTreePanel
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public interface TreePanel {
	
	/**
	 * Adds a {@link ChangeListener} that is listening on changes inside the panel.
	 * 
	 * @param cl the {@code ChangeListener} to add
	 */
	public void addChangeListener(ChangeListener cl);

	/**
	 * Removes all {@link ChangeListener}s.
	 */
	public void removeAllChangeListener();
	
	/**
	 * Read the current panel's checkboxes defining the visibility of single entries values.
	 */
	public void updateCheckHash();

	/**
	 * Returns the actual checked fields inside the panel as a {@link Hashtable}.
	 * The name of the checked fields are used as key, {@code Integer} objects with value 1 
	 * reflect that the field should be displayed.
	 * 
	 * @return a {@code Hashtable} reflecting the displayable fields
	 */
	public Hashtable getCheckHash();

	/**
	 * Sets the Hashtable for the Checkboxes inside this Panel.<br>
	 * This will be filled out from the Node and should not be done by hand.
	 * 
	 * @param ch a {@link Hashtable} carrying the field names and display hints
	 */
	public void setCheckHash(Hashtable ch);

	/**
	 * Sets all checkboxes enabled or disabled on this panel.
	 * 
	 * @param enabled a boolean indicating if the checkboxes are supposed to be enabled
	 */
	public void setAllChecksEnabled(boolean enabled);

	/**
	 * Sets whethter the fields should be editable or not.
	 * 
	 * @param editable a boolean indicating if the fields should be editable.
	 */
	public void setFieldsEditable(boolean editable);
}