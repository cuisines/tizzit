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

import java.util.EventListener;

/**
 * Interface for the Listener-Class who wants to receive the Informations about a save-operation from a EditpaneHandler.
 * <p><b>Copyright: JuwiMacMillan Group GmbH (c) 2002</b></p>
 * @author <a href="mailto:d.bogun@juwimm.com">Dirk Bogun</a>
 * @version $Id$
 */
public interface ExitListener extends EventListener {

	/**
	 * 
	 * @param ae
	 * @return true = exit, false = cancel
	 */
	public boolean exitPerformed(ExitEvent ae);

}