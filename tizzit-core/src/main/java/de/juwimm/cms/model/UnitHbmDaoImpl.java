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
package de.juwimm.cms.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.components.model.AddressHbm;
import de.juwimm.cms.components.model.DepartmentHbm;
import de.juwimm.cms.components.model.PersonHbm;
import de.juwimm.cms.components.model.TalktimeHbm;
import de.juwimm.cms.components.vo.AddressValue;
import de.juwimm.cms.components.vo.DepartmentValue;
import de.juwimm.cms.components.vo.PersonValue;
import de.juwimm.cms.components.vo.TalktimeValue;
import de.juwimm.cms.remote.helper.AuthenticationHelper;
import de.juwimm.cms.vo.UnitValue;

/**
 * @see de.juwimm.cms.model.UnitHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 *         company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class UnitHbmDaoImpl extends UnitHbmDaoBase {
	private static final Logger log = Logger.getLogger(UnitHbmDaoImpl.class);

	@Autowired
	private SequenceHbmDao sequenceHbmDao;

	@Override
	public UnitHbm create(UnitHbm unitHbm) {
		if (unitHbm.getUnitId() == null || unitHbm.getUnitId().intValue() == 0) {
			try {
				Integer id = sequenceHbmDao.getNextSequenceNumber("unit.unit_id");
				unitHbm.setUnitId(id);
			} catch (Exception e) {
				log.error("Error creating primary key", e);
			}
		}
		if (unitHbm.getSite() == null) {
			unitHbm.setSite(getUserHbmDao().load(AuthenticationHelper.getUserName()).getActiveSite());
		}
		unitHbm.setLastModifiedDate(System.currentTimeMillis());
		return super.create(unitHbm);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected String handleToXmlRecursive(int tabdepth, UnitHbm unit) throws Exception {
		boolean isRootUnit = false;
		if (unit.getSite().getRootUnit().equals(unit)) {
			isRootUnit = true;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<unit id=\"" + unit.getUnitId() + "\" ");
		sb.append("imageId=\"" + unit.getImageId() + "\" logoId=\"" + unit.getLogoId() + "\" ");
		sb.append("isRootUnit=\"" + Boolean.toString(isRootUnit) + "\">");
		sb.append("<![CDATA[").append(unit.getName().trim()).append("]]>\n");
		{
			Collection addr = unit.getAddresses();
			Iterator it = addr.iterator();
			while (it.hasNext()) {
				AddressHbm adr = (AddressHbm) it.next();
				sb.append(adr.toXml(tabdepth + 1));
			}
		}
		{
			Collection<PersonHbm> persons = unit.getPersons();
			for (PersonHbm person : persons) {
				sb.append(person.toXmlRecursive(tabdepth + 1));
			}
		}
		{
			Collection deps = unit.getDepartments();
			Iterator it = deps.iterator();
			while (it.hasNext()) {
				DepartmentHbm dep = (DepartmentHbm) it.next();
				sb.append(dep.toXmlRecursive(tabdepth + 1));
			}
		}
		{
			Collection<TalktimeHbm> talktimes = getTalktimeHbmDao().findByUnit(unit.getUnitId());
			for (TalktimeHbm talktime : talktimes) {
				sb.append(talktime.toXml(tabdepth + 1));
			}
		}
		sb.append("</unit>\n");
		return sb.toString();
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findAll(final int transform) {
		return this.findAll(transform, "from de.juwimm.cms.model.UnitHbm as unitHbm");
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findAll(final int transform, final java.lang.Integer siteId) {
		return this.findAll(transform, "from de.juwimm.cms.model.UnitHbm as unit where unit.site.siteId = ? order by unit.name", siteId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findBySite(final int transform, final java.lang.Integer siteId) {
		return this.findBySite(transform, "from de.juwimm.cms.model.UnitHbm as unit where unit.site.siteId = ?", siteId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findByName(final int transform, final java.lang.Integer siteId, final java.lang.String name) {
		return this.findByName(transform, "from de.juwimm.cms.model.UnitHbm as u where u.site.siteId = ? and u.name like ?", siteId, name);
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.Collection findByUserAndSite(final int transform, final java.lang.String userId, final java.lang.Integer siteId) {
		return this.findByUserAndSite(transform, "select u from de.juwimm.cms.model.UnitHbm as u inner join u.users s where s.userId = ? and u.site.siteId = ?", userId, siteId);
	}

	@Override
	public UnitValue handleGetDao(UnitHbm unit) {
		UnitValue value = new UnitValue();
		value.setUnitId(unit.getUnitId());
		value.setImageId(unit.getImageId());
		value.setLastModifiedDate(unit.getLastModifiedDate());
		value.setLogoId(unit.getLogoId());
		value.setName(unit.getName());

		try {
			ArrayList vec = new ArrayList<AddressValue>();
			Iterator it = unit.getAddresses().iterator();
			while (it.hasNext()) {
				vec.add(((AddressHbm) it.next()).getData());
			}
			if (vec.size() > 0) {
				value.setAddresses((AddressValue[]) vec.toArray(new AddressValue[0]));
				value.setHasChildren(true);
			}
		} catch (Exception ex) {
			log.warn("GETDAO CANNOT GET ADRESSES " + ex.getMessage());
		}

		try {
			ArrayList vec = new ArrayList();
			Collection<TalktimeHbm> talktimes = getTalktimeHbmDao().findByUnit(unit.getUnitId());
			for (TalktimeHbm talktimeHbm : talktimes) {
				vec.add(talktimeHbm.getData());
			}
			if (vec.size() > 0) {
				value.setTalkTimes((TalktimeValue[]) vec.toArray(new TalktimeValue[0]));
				value.setHasChildren(true);
			}
		} catch (Exception ex) {
			log.warn("GETDAO CANNOT GET TALKTIMES " + ex.getMessage());
		}

		try {
			ArrayList vec = new ArrayList();
			Collection<PersonHbm> persons = unit.getPersons();
			for (PersonHbm person : persons) {
				vec.add(person.getDao(0));
			}
			if (vec.size() > 0) {
				value.setPersons((PersonValue[]) vec.toArray(new PersonValue[0]));
				value.setHasChildren(true);
			}
		} catch (Exception ex) {
			log.warn("GETDAO CANNOT GET PERSONS", ex);
		}

		try {
			ArrayList vec = new ArrayList();
			Iterator it = unit.getDepartments().iterator();
			while (it.hasNext()) {
				vec.add(((DepartmentHbm) it.next()).getDao(0));
			}

			if (vec.size() > 0) {
				value.setDepartments((DepartmentValue[]) vec.toArray(new DepartmentValue[0]));
				value.setHasChildren(true);
			}
		} catch (Exception ex) {
			log.warn("GETDAO CANNOT GET DEPARTMENTS " + ex.getMessage());
		}

		return value;
	}
}
