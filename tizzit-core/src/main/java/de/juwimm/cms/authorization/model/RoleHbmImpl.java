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
// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package de.juwimm.cms.authorization.model;

import de.juwimm.cms.authorization.vo.RoleValue;

/**
 * @see de.juwimm.cms.authorization.model.RoleHbm
 */
public class RoleHbmImpl extends de.juwimm.cms.authorization.model.RoleHbm {
	/**
	 * The serial version UID of this class. Needed for serialization.
	 */
	private static final long serialVersionUID = -643620350171372005L;

	/**
	 * @see de.juwimm.cms.authorization.model.RoleHbm#getRoleValue()
	 */
	public de.juwimm.cms.authorization.vo.RoleValue getRoleValue() {
		RoleValue value = new RoleValue();
		try {
			value.setRoleId(getRoleId());
		} catch (Exception e) {
			throw new javax.ejb.EJBException(e);
		}
		return value;
	}

	/**
	 * @see de.juwimm.cms.authorization.model.RoleHbm#setRoleValue(de.juwimm.cms.authorization.vo.RoleValue)
	 */
	public void setRoleValue(de.juwimm.cms.authorization.vo.RoleValue value) {
	}

}