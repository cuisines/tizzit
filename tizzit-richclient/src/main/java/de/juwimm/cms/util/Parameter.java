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
package de.juwimm.cms.util;

import javax.swing.ImageIcon;

/**
 * <p>Title: ConQuest</p>
 * <p>Description: Enterprise Content Management</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Revision: 2564 $
 */
public interface Parameter {
	public static int PARAMETER_TYPE_BOOLEAN = 1;
	public static int PARAMETER_TYPE_STRING = 2;

	/**
	 * The unique Id of this Parameter
	 * @return The unique Id
	 */
	public String getId();

	/**
	 * The name of the Parameter.
	 * @return The Name
	 */
	public String getName();

	/**
	 * The Parameter Types.
	 * @return an int Array containing every parameter type this parameter contains
	 */
	public int[] getParameterTypes();

	public Object getDefaultValue(int parameterType);

	public ImageIcon getIcon();
}