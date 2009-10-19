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
package de.juwimm.cms.gui.event;

import de.juwimm.cms.gui.tree.PageNode;

/**
 * <b>Tizzit Enterprise Content Management</b><br/>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public interface ChooseTemplateListener {

	/**
	 * Will be called if the user has pressed the "ok" Button in ChooseTemplateDialog.
	 *
	 * @param unitId The unitId the User has selected, if one. Otherwise it is null.
	 * @param parentEntry The parentEntry if this dialog is used to add a new component
	 * @param template The selected template
	 * @param position The position (BEFORE, AFTER, UNDERNEATH) if this should add a new component
	 */
	public void chooseTemplatePerformed(int unitId, PageNode parentEntry, String template, int position);
}