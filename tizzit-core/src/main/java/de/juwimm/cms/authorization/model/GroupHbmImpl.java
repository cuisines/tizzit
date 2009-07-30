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

import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;

import de.juwimm.cms.authorization.vo.GroupValue;
import de.juwimm.cms.authorization.vo.RoleValue;

/**
 * @see de.juwimm.cms.authorization.model.GroupHbm
 */
public class GroupHbmImpl extends de.juwimm.cms.authorization.model.GroupHbm {
	/**
	 * The serial version UID of this class. Needed for serialization.
	 */
	private static final long serialVersionUID = 414544076657984838L;

	/**
	 * @see de.juwimm.cms.authorization.model.GroupHbm#getGroupValue()
	 */
	public de.juwimm.cms.authorization.vo.GroupValue getGroupValue() {
		GroupValue value = new GroupValue();
		try {
			value.setGroupId(getGroupId());
			value.setGroupName(getGroupName());
			Collection cRole = getRoles();
			RoleValue[] roles = new RoleValue[cRole.size()];
			int i = 0;
			for (Iterator it = cRole.iterator(); it.hasNext();) {
				roles[i++] = ((RoleHbm) it.next()).getRoleValue();
			}
			value.setRoles(roles);
		} catch (Exception e) {
			throw new EJBException(e);
		}
		return value;
	}

	/**
	 * @see de.juwimm.cms.authorization.model.GroupHbm#setGroupValue(de.juwimm.cms.authorization.vo.GroupValue)
	 */
	public void setGroupValue(de.juwimm.cms.authorization.vo.GroupValue value) {
		try {
			setGroupName(value.getGroupName());
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

}