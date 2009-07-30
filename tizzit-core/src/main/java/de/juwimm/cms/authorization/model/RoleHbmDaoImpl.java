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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.model.SequenceHbmDao;

/**
 * @see de.juwimm.cms.authorization.model.RoleHbm
 */
public class RoleHbmDaoImpl extends de.juwimm.cms.authorization.model.RoleHbmDaoBase {
	private static Logger log = Logger.getLogger(RoleHbmDaoImpl.class);

	@Autowired
	private SequenceHbmDao sequenceHbmDao;
	
	@Override
	public RoleHbm create(RoleHbm roleHbm) {
		if (roleHbm.getRoleId() == null) {
			try {
				Integer id = sequenceHbmDao.getNextSequenceNumber("role.role_id");
				roleHbm.setRoleId(String.valueOf(id));
			} catch (Exception e) {
				log.error("Error creating primary key", e);
			}
		}
		return super.create(roleHbm);
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findAll(final int transform) {
		return this.findAll(transform, "from de.juwimm.cms.authorization.model.RoleHbm as roleHbm");
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findAll(final int transform, final java.lang.Integer siteId) {
		return this.findAll(transform, "select r from de.juwimm.cms.authorization.model.RoleHbm r inner join r.groups g where g.site.siteId = ?", siteId);
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findByUserAndSite(final int transform, final java.lang.String userId, final java.lang.Integer siteId) {
		return this.findByUserAndSite(transform, "select r from de.juwimm.cms.authorization.model.RoleHbm as r inner join r.groups g inner join g.users u where u.userId = ? and g.site.siteId = ?", userId, siteId);
	}
}