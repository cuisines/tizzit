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
package de.juwimm.cms.components.model;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.model.SequenceHbmDao;

/**
 * @see de.juwimm.cms.components.model.DepartmentHbm
 */
public class DepartmentHbmDaoImpl extends de.juwimm.cms.components.model.DepartmentHbmDaoBase {
	private static Logger log = Logger.getLogger(DepartmentHbmDaoImpl.class);

	@Autowired
	private SequenceHbmDao sequenceHbmDao;

	@Override
	public DepartmentHbm create(DepartmentHbm departmentHbm) {
		if (departmentHbm.getDepartmentId() == null) {
			try {
				Integer id = sequenceHbmDao.getNextSequenceNumber("department.department_id");
				departmentHbm.setDepartmentId(new Long(id.longValue()));
			} catch (Exception e) {
				log.error("Error creating primary key", e);
			}
		}
		return super.create(departmentHbm);
	}

	/**
	 * @see de.juwimm.cms.components.model.DepartmentHbm#create()
	 */
	@Override
	public java.lang.Long handleCreate() {
		Long id = null;
		DepartmentHbm department = DepartmentHbm.Factory.newInstance();
		try {
			id = new Long(sequenceHbmDao.getNextSequenceNumber("department.department_id").longValue());
			department.setDepartmentId(id);
		} catch (Exception e) {
			logger.error("Error creating primary key: ", e);
		}
		getHibernateTemplate().save(department);
		return department.getDepartmentId();
	}

	/**
	 * @see de.juwimm.cms.components.model.DepartmentHbm#create(java.lang.String,
	 *      de.juwimm.cms.model.UnitHbm)
	 */
	@Override
	public java.lang.Long handleCreate(java.lang.String name, de.juwimm.cms.model.UnitHbm unit) {
		Long id = null;
		DepartmentHbm department = DepartmentHbm.Factory.newInstance();
		try {
			id = new Long(sequenceHbmDao.getNextSequenceNumber("department.department_id").longValue());
			department.setDepartmentId(id);
		} catch (Exception e) {
			logger.error("Error creating primary key: ", e);
		}
		department.setName(name);
		department.setUnit(unit);
		getHibernateTemplate().save(department);
		return department.getDepartmentId();
	}

	/**
	 * @see de.juwimm.cms.components.model.DepartmentHbm#create(org.w3c.dom.Element,
	 *      boolean, java.util.Map, java.util.Map, java.util.Map, java.util.Map)
	 */
	@Override
	public java.lang.Long handleCreate(org.w3c.dom.Element ael, boolean useNewId, java.util.Map mappingDepartments, java.util.Map mappingPersons, java.util.Map mappingAddresses, java.util.Map mappingTalktime) {
		DepartmentHbm department = DepartmentHbm.Factory.newInstance();
		if (useNewId) {
			this.create();
			mappingDepartments.put(new Long(ael.getAttribute("id")), department.getDepartmentId());
		} else {
			department.setDepartmentId(new Long(ael.getAttribute("id")));
		}
		department.setName(ael.getAttribute("name"));
		getHibernateTemplate().save(department);
		return department.getDepartmentId();
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findByName(final int transform, final java.lang.Integer siteId, final java.lang.String name) {
		return this.findByName(transform, "from de.juwimm.cms.components.model.DepartmentHbm as d where d.unit.site.siteId = ? and d.name LIKE ?", siteId, name);
	}
}