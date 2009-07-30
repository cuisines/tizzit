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
/**
 * 
 */
package de.juwimm.cms.util;

import de.juwimm.cms.content.frame.tree.ComponentNode;

/**
 * @author <a href="mailto:christiane.hausleiter@juwimm.com">Christiane Hausleiter</a>
 */
public interface DBCDao {
	
	/**
	 * Loads the specified {@link ComponentNode}'s values.
	 * 
	 * @param valueObject
	 */
	void load(ComponentNode node);
	
	/**
	 * Saves the current {@link ComponentNode}'s values.
	 */
	void save();
	
	/**
	 * Validates the current {@link ComponentNode}'s values.
	 * 
	 * @return An error message, if the {@code valueObject} is not valid, null otherwise.
	 */
	String validateNode();

}
