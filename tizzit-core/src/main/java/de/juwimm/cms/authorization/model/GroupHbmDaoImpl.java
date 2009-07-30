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
 * @see de.juwimm.cms.authorization.model.GroupHbm
 */
public class GroupHbmDaoImpl extends de.juwimm.cms.authorization.model.GroupHbmDaoBase {
	private static Logger log = Logger.getLogger(GroupHbmDaoImpl.class);
	
	@Autowired
	private SequenceHbmDao sequenceHbmDao;
	
	@Override
	public GroupHbm create(GroupHbm groupHbm) {
		if (groupHbm.getGroupId() == null || groupHbm.getGroupId().intValue() == 0) {
			try {
				Integer id = sequenceHbmDao.getNextSequenceNumber("group.group_id");
				groupHbm.setGroupId(id);
			} catch (Exception e) {
				log.error("Error creating primary key: " + e.getMessage(), e);
			}
		}
		return super.create(groupHbm);
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findAll(final int transform, final java.lang.Integer siteId) {
		return this.findAll(transform, "from de.juwimm.cms.authorization.model.GroupHbm as g where g.site.siteId = ?", siteId);
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findByUserAndSite(final int transform, final java.lang.String userId, final java.lang.Integer siteId) {
		return this.findByUserAndSite(transform, "select g from de.juwimm.cms.authorization.model.GroupHbm g inner join g.users u where u.userId = ? and g.site.siteId = ?", userId, siteId);
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findByRoleAndSite(final int transform, final java.lang.String roleId, final java.lang.Integer siteId) {
		return this.findByRoleAndSite(transform, "SELECT g FROM de.juwimm.cms.authorization.model.GroupHbm g inner join g.roles r where r.roleId = ? and g.site.siteId = ?", roleId, siteId);
	}
}