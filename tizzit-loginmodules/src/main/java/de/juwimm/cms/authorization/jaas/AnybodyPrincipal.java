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
package de.juwimm.cms.authorization.jaas;

import java.security.Principal;

/**
 * An implementation of Principal and Comparable that represents any role. Any
 * Principal or name of a Principal when compared to an AnybodyPrincipal using
 * {@link #equals(Object) equals} or {@link #compareTo(Object) compareTo} will
 * always be found equals to the AnybodyPrincipal. Note that this class is not
 * likely to operate correctly in a collection since the hashCode() and equals()
 * methods are not correlated.
 * <p>
 * Title: ConQuest
 * </p>
 * <p>
 * Description: Enterprise Content Management
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: JuwiMacMillan Group
 * </p>
 * 
 * @author <a href="s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @author Scott.Stark@jboss.org
 * @version $$Id$$
 */
public class AnybodyPrincipal implements Comparable, Principal {
	public static final String ANYBODY = "<ANYBODY>";

	public static final AnybodyPrincipal ANYBODY_PRINCIPAL = new AnybodyPrincipal();

	@Override
	public int hashCode() {
		return ANYBODY.hashCode();
	}

	/**
	 * @return "<ANYBODY>"
	 */
	public String getName() {
		return ANYBODY;
	}

	@Override
	public String toString() {
		return ANYBODY;
	}

	/**
	 * This method always returns 0 to indicate equality for any argument. This
	 * is only meaningful when comparing against other Principal objects or
	 * names of Principals.
	 * 
	 * @return true to indicate equality for any argument.
	 */
	@Override
	public boolean equals(Object another) {
		return true;
	}

	/**
	 * This method always returns 0 to indicate equality for any argument. This
	 * is only meaningful when comparing against other Principal objects or
	 * names of Principals.
	 * 
	 * @return 0 to indicate equality for any argument.
	 */
	public int compareTo(Object o) {
		return 0;
	}

}
