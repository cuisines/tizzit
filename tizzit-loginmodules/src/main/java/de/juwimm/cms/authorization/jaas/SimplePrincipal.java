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
 * A simple String based implementation of Principal. Typically a
 * SimplePrincipal is created given a userID which is used as the Principal name
 * 
 * @author <a href="s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @author Scott.Stark@jboss.org
 * @version 1.0
 */
public class SimplePrincipal implements Principal, java.io.Serializable {
	private static final long serialVersionUID = -2924697608567468581L;

	private String name;

	public SimplePrincipal(String name) {
		this.name = name;
	}

	/**
	 * Compare this SimplePrincipal's name against another Principal
	 * 
	 * @return true if name equals another.getName();
	 */
	@Override
	public boolean equals(Object another) {
		if (!(another instanceof Principal)) {
			return false;
		}
		String anotherName = ((Principal) another).getName();
		boolean equals = false;
		if (name == null) {
			equals = anotherName == null;
		} else {
			equals = name.equals(anotherName);
		}
		return equals;
	}

	@Override
	public int hashCode() {
		return (name == null ? 0 : name.hashCode());
	}

	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

}
