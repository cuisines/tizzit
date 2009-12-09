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
package de.juwimm.cms.components.remote;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.components.model.AddressHbm;
import de.juwimm.cms.components.model.AddressHbmImpl;
import de.juwimm.cms.components.model.DepartmentHbm;
import de.juwimm.cms.components.model.DepartmentHbmImpl;
import de.juwimm.cms.components.model.PersonHbm;
import de.juwimm.cms.components.model.PersonHbmImpl;
import de.juwimm.cms.components.model.TalktimeHbm;
import de.juwimm.cms.components.model.TalktimeHbmImpl;
import de.juwimm.cms.components.vo.AddressValue;
import de.juwimm.cms.components.vo.DepartmentValue;
import de.juwimm.cms.components.vo.PersonValue;
import de.juwimm.cms.components.vo.TalktimeValue;
import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.remote.helper.AuthenticationHelper;
import de.juwimm.cms.vo.UnitValue;

/**
 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> ,
 *         Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id: ComponentsServiceSpringImpl.java 26864 2008-04-29 14:21:24Z
 *          greivej $
 */
public class ComponentsServiceSpringImpl extends ComponentsServiceSpringBase {
	private static Log log = LogFactory.getLog(ComponentsServiceSpringImpl.class);

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#getPerson(java.lang.Long)
	 */
	@Override
	protected PersonValue handleGetPerson(Long personId) throws Exception {
		PersonHbm person = getPersonHbmDao().load(personId);
		if (person != null) {
			return person.getDao(1);
		}
		return null;
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpringBase#handleGetPersonByExternalId(java.lang.String)
	 */
	@Override
	protected PersonValue handleGetPersonByExternalId(String personId) throws Exception {
		PersonHbm person = getPersonHbmDao().findByExternalId(personId);
		return person.getDao(1);
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#getPerson4Name(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	protected PersonValue[] handleGetPerson4Name(String firstName, String lastName) throws Exception {
		PersonValue[] personValues = new PersonValue[0];
		try {
			UserHbm user = getUserHbmDao().load(AuthenticationHelper.getUserName());
			Collection<PersonHbm> temp = getPersonHbmDao().findByName(user.getActiveSite().getSiteId(), firstName, lastName);
			Iterator<PersonHbm> it = temp.iterator();
			Vector<PersonValue> vec = new Vector<PersonValue>();
			while (it.hasNext()) {
				vec.add(it.next().getDao(0));
			}
			personValues = vec.toArray(new PersonValue[0]);
		} catch (Exception e) {
			log.error("Could not get persons by name (" + firstName + " " + lastName + "): " + e.getMessage(), e);
		}
		return personValues;
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#getUnits4Name(java.lang.String)
	 */
	@Override
	protected UnitValue[] handleGetUnits4Name(String name) throws Exception {
		try {
			Vector<UnitValue> vec = new Vector<UnitValue>();
			UserHbm user = getUserHbmDao().load(AuthenticationHelper.getUserName());
			Iterator it = getUnitHbmDao().findByName(user.getActiveSite().getSiteId(), name).iterator();
			while (it.hasNext()) {
				UnitHbm unit = (UnitHbm) it.next();
				vec.addElement(getUnitHbmDao().getDao(unit));
			}
			return vec.toArray(new UnitValue[0]);
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#getUnit(java.lang.Integer)
	 */
	@Override
	protected UnitValue handleGetUnit(Integer unitId) throws Exception {
		UnitHbm unit = getUnitHbmDao().load(unitId);
		if (unit != null) {
			return getUnitHbmDao().getDao(unit);
		}
		return null;
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#getDepartments4Name(java.lang.String)
	 */
	@Override
	protected DepartmentValue[] handleGetDepartments4Name(String name) throws Exception {
		UserHbm user = null;
		Iterator<DepartmentHbm> it = null;
		try {
			user = getUserHbmDao().load(AuthenticationHelper.getUserName());
			it = getDepartmentHbmDao().findByName(user.getActiveSite().getSiteId(), name).iterator();
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
		Vector<DepartmentValue> vec = new Vector<DepartmentValue>();
		while (it.hasNext()) {
			vec.addElement(it.next().getDao(0));
		}
		return vec.toArray(new DepartmentValue[0]);
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#getDepartment(java.lang.Long)
	 */
	@Override
	protected DepartmentValue handleGetDepartment(Long departmentId) throws Exception {
		DepartmentHbm department = getDepartmentHbmDao().load(departmentId);
		if (department != null) {
			return department.getDao(-1);
		}
		return null;
	}

	private PersonHbm createPersonHbmFromValue(PersonValue value) {
		PersonHbm personHbm = null;
		if (value != null) {
			if (value.getPersonId() != null) {
				personHbm = getPersonHbmDao().load(value.getPersonId());
			} else {
				personHbm = new PersonHbmImpl();
			}
			personHbm.setBirthDay(value.getBirthDay());
			personHbm.setCountryJob(value.getCountryJob());
			if (value.getDepartmentId() != null) {
				HashSet<DepartmentHbm> departments = new HashSet<DepartmentHbm>();
				departments.add(getDepartmentHbmDao().load(value.getDepartmentId()));
				personHbm.setDepartments(departments);
			}
			if (value.getUnitId() != null) {
				personHbm.getUnits().add(getUnitHbmDao().load(value.getUnitId().intValue()));
			}
			personHbm.setFirstname(value.getFirstname());
			personHbm.setImageId(value.getImageId());
			personHbm.setJob(value.getJob());
			personHbm.setJobTitle(value.getJobTitle());
			personHbm.setLastname(value.getLastname());
			personHbm.setLinkMedicalAssociation(value.getLinkMedicalAssociation());
			personHbm.setMedicalAssociation(value.getMedicalAssociation());
			personHbm.setPosition(value.getPosition());
			personHbm.setSalutation(value.getSalutation());
			personHbm.setSex(value.getSex());
			personHbm.setTitle(value.getTitle());
		}
		return personHbm;
	}

	private AddressHbm createAddressHbmFromValue(AddressValue value) {
		AddressHbm address = null;
		if (value != null) {
			if (value.getAddressId() != null) {
				address = getAddressHbmDao().load(value.getAddressId());
			} else {
				address = new AddressHbmImpl();
			}
			address.setAddressType(value.getAddressType());
			address.setBuildingLevel(value.getBuildingLevel());
			address.setBuildingNr(value.getBuildingNr());
			address.setCity(value.getCity());
			address.setCountry(value.getCountry());
			address.setCountryCode(value.getCountryCode());
			address.setEmail(value.getEmail());
			address.setFax(value.getFax());
			address.setHomepage(value.getHomepage());
			address.setMisc(value.getMisc());
			address.setMobilePhone(value.getMobilePhone());
			address.setPhone1(value.getPhone1());
			address.setPhone2(value.getPhone2());
			address.setPostOfficeBox(value.getPostOfficeBox());
			address.setRoomNr(value.getRoomNr());
			address.setStreet(value.getStreet());
			address.setStreetNr(value.getStreetNr());
			address.setZipCode(value.getZipCode());
		}
		return address;
	}

	private DepartmentHbm createDepartmentHbmFromValue(DepartmentValue value) {
		DepartmentHbm department = null;
		if (value != null) {
			if (value.getDepartmentId() != null) {
				department = getDepartmentHbmDao().load(value.getDepartmentId());
			} else {
				department = new DepartmentHbmImpl();
			}
			department.setName(value.getName());
		}
		return department;
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#createPerson(de.juwimm.cms.components.vo.PersonValue)
	 */
	@Override
	protected Long handleCreatePerson(PersonValue value) throws Exception {
		PersonHbm personHbm = this.createPersonHbmFromValue(value);
		personHbm = getPersonHbmDao().create(personHbm);
		return personHbm.getPersonId();
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#createAddress(de.juwimm.cms.components.vo.AddressValue)
	 */
	@Override
	protected Long handleCreateAddress(AddressValue value) throws Exception {
		AddressHbm address = this.createAddressHbmFromValue(value);
		address = getAddressHbmDao().create(address);
		return address.getAddressId();
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#createDepartment(java.lang.String,
	 *      java.lang.Integer)
	 */
	@Override
	protected Long handleCreateDepartment(String name, Integer unitId) throws Exception {
		DepartmentHbm department = new DepartmentHbmImpl();
		department.setName(name);
		department.setUnit(getUnitHbmDao().load(unitId));
		department = getDepartmentHbmDao().create(department);
		return department.getDepartmentId();
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#addAddress2Person(java.lang.Long,
	 *      java.lang.Long)
	 */
	@Override
	protected void handleAddAddress2Person(Long personId, Long addressId) throws Exception {
		PersonHbm person = getPersonHbmDao().load(personId);
		AddressHbm address = getAddressHbmDao().load(addressId);
		person.addAddress(address);
		getPersonHbmDao().update(person);
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#addAddress2Department(java.lang.Long,
	 *      java.lang.Long)
	 */
	@Override
	protected void handleAddAddress2Department(Long departmentId, Long addressId) throws Exception {
		DepartmentHbm department = getDepartmentHbmDao().load(departmentId);
		AddressHbm address = getAddressHbmDao().load(addressId);
		department.addAddress(address);
		getDepartmentHbmDao().update(department);
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#addAddress2Unit(java.lang.Integer,
	 *      java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void handleAddAddress2Unit(Integer unitId, Long addressId) throws Exception {
		UnitHbm unit = getUnitHbmDao().load(unitId);
		AddressHbm address = getAddressHbmDao().load(addressId);
		unit.getAddresses().add(address);
		getUnitHbmDao().update(unit);
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#removePerson(java.lang.Long)
	 */
	@Override
	protected void handleRemovePerson(Long personId) throws Exception {
		getPersonHbmDao().remove(personId);
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#createTalktime(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	protected Long handleCreateTalktime(String talkTimeType, String talkTimes) throws Exception {
		TalktimeHbm talktime = TalktimeHbm.Factory.newInstance();
		talktime.setTalkTimeType(talkTimeType);
		talktime.setTalkTimes(talkTimes);
		talktime = getTalktimeHbmDao().create(talktime);
		return talktime.getTalkTimeId();
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#addTalktime2Person(java.lang.Long,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	protected Long handleAddTalktime2Person(Long personId, String talkTimeType, String talkTimes) throws Exception {
		TalktimeHbm talktime = new TalktimeHbmImpl();
		talktime.setTalkTimes(talkTimes);
		talktime.setTalkTimeType(talkTimeType);
		talktime = getTalktimeHbmDao().create(talktime);
		PersonHbm person = getPersonHbmDao().load(personId);
		person.addTalktime(talktime);
		getPersonHbmDao().update(person);
		return talktime.getTalkTimeId();
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#addTalktime2Unit(java.lang.Integer,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	protected Long handleAddTalktime2Unit(Integer unitId, String talkTimeType, String talkTimes) throws Exception {
		TalktimeHbm talktime = new TalktimeHbmImpl();
		talktime.setTalkTimes(talkTimes);
		talktime.setTalkTimeType(talkTimeType);
		talktime = getTalktimeHbmDao().create(talktime);
		UnitHbm unit = getUnitHbmDao().load(unitId);
		talktime.setUnit(unit);
		getUnitHbmDao().update(unit);
		getTalktimeHbmDao().update(talktime);
		return talktime.getTalkTimeId();
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#addTalktime2Department(java.lang.Long,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	protected Long handleAddTalktime2Department(Long departmentId, String talkTimeType, String talkTimes) throws Exception {
		TalktimeHbm talktime = new TalktimeHbmImpl();
		talktime.setTalkTimes(talkTimes);
		talktime.setTalkTimeType(talkTimeType);
		talktime = getTalktimeHbmDao().create(talktime);
		DepartmentHbm department = getDepartmentHbmDao().load(departmentId);
		department.addTalktime(talktime);
		getDepartmentHbmDao().update(department);
		return talktime.getTalkTimeId();
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#removeTalktime(java.lang.Long)
	 */
	@Override
	protected void handleRemoveTalktime(Long talktimeId) throws Exception {
		getTalktimeHbmDao().remove(talktimeId);
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#removeUnit(java.lang.Integer)
	 */
	@Override
	protected void handleRemoveUnit(Integer unitId) throws Exception {
		getUnitHbmDao().remove(unitId);
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#removeDepartment(java.lang.Long)
	 */
	@Override
	protected void handleRemoveDepartment(Long departmentId) throws Exception {
		getDepartmentHbmDao().remove(departmentId);
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#removeAddress(java.lang.Long)
	 */
	@Override
	protected void handleRemoveAddress(Long addressId) throws Exception {
		getAddressHbmDao().remove(addressId);
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#getDepartments4Unit(java.lang.Integer)
	 */
	@Override
	protected DepartmentValue[] handleGetDepartments4Unit(Integer unitId) throws Exception {
		UnitHbm unit = getUnitHbmDao().load(unitId);
		Collection<DepartmentHbm> departments = unit.getDepartments();
		DepartmentValue[] departmentValues = new DepartmentValue[departments.size()];
		int i = 0;
		for (DepartmentHbm department : departments) {
			departmentValues[i++] = department.getDao(-1);
		}
		return departmentValues;
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#getPersons4Unit(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected PersonValue[] handleGetPersons4Unit(Integer unitId) throws Exception {
		Collection<PersonHbm> persons = getPersonHbmDao().findByUnit(unitId);
		PersonValue[] personValues = new PersonValue[persons.size()];
		int i = 0;
		for (PersonHbm person : persons) {
			personValues[i++] = person.getDao(-1);
		}
		return personValues;
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#getPersons4Department(java.lang.Long)
	 */
	@Override
	protected PersonValue[] handleGetPersons4Department(Long departmentId) throws Exception {
		DepartmentHbm department = getDepartmentHbmDao().load(departmentId);
		Collection<PersonHbm> persons = department.getPersons();
		PersonValue[] personValues = new PersonValue[persons.size()];
		int i = 0;
		for (PersonHbm person : persons) {
			personValues[i++] = person.getDao(-1);
		}
		return personValues;
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#getTalktimes4Person(java.lang.Long)
	 */
	@Override
	protected TalktimeValue[] handleGetTalktimes4Person(Long personId) throws Exception {
		PersonHbm person = getPersonHbmDao().load(personId);
		Collection<TalktimeHbm> talktimes = person.getTalktimes();
		TalktimeValue[] talktimeValues = new TalktimeValue[talktimes.size()];
		int i = 0;
		for (TalktimeHbm talktime : talktimes) {
			talktimeValues[i++] = talktime.getData();
		}
		return talktimeValues;
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#getTalktimes4Department(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected TalktimeValue[] handleGetTalktimes4Department(Long departmentId) throws Exception {
		DepartmentHbm department = getDepartmentHbmDao().load(departmentId);
		Collection<TalktimeHbm> talktimes = department.getTalktimes();
		TalktimeValue[] talktimeValues = new TalktimeValue[talktimes.size()];
		int i = 0;
		for (TalktimeHbm talktime : talktimes) {
			talktimeValues[i++] = talktime.getData();
		}
		return talktimeValues;
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#getTalktimes4Unit(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected TalktimeValue[] handleGetTalktimes4Unit(Integer unitId) throws Exception {
		Collection<TalktimeHbm> talktimes = getTalktimeHbmDao().findByUnit(unitId);
		TalktimeValue[] talktimeValues = new TalktimeValue[talktimes.size()];
		int i = 0;
		for (TalktimeHbm talktime : talktimes) {
			talktimeValues[i++] = talktime.getData();
		}
		return talktimeValues;
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#getTalktime(java.lang.Long)
	 */
	@Override
	protected TalktimeValue handleGetTalktime(Long talktimeId) throws Exception {
		TalktimeHbm talktime = getTalktimeHbmDao().load(talktimeId);
		if (talktime != null) {
			return talktime.getData();
		}
		return null;
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#getAddress(java.lang.Long)
	 */
	@Override
	protected AddressValue handleGetAddress(Long addressId) throws Exception {
		AddressHbm address = getAddressHbmDao().load(addressId);
		if (address != null) {
			return address.getData();
		}
		return null;
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#updatePerson(de.juwimm.cms.components.vo.PersonValue)
	 */
	@Override
	protected void handleUpdatePerson(PersonValue personValue) throws Exception {
		PersonHbm personHbm = this.createPersonHbmFromValue(personValue);
		getPersonHbmDao().update(personHbm);
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#updateDepartment(de.juwimm.cms.components.vo.DepartmentValue)
	 */
	@Override
	protected void handleUpdateDepartment(DepartmentValue departmentValue) throws Exception {
		DepartmentHbm department = this.createDepartmentHbmFromValue(departmentValue);
		getDepartmentHbmDao().update(department);
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#updateTalktime(de.juwimm.cms.components.vo.TalktimeValue)
	 */
	@Override
	protected void handleUpdateTalktime(TalktimeValue talktimeValue) throws Exception {
		TalktimeHbm talktime = null;
		if (talktimeValue.getTalkTimeId() != null) {
			talktime = getTalktimeHbmDao().load(talktimeValue.getTalkTimeId());
		} else {
			talktime = new TalktimeHbmImpl();
		}
		talktime.setTalkTimes(talktimeValue.getTalkTimes());
		talktime.setTalkTimeType(talktimeValue.getTalkTimeType());
		getTalktimeHbmDao().update(talktime);
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#updateAddressData(de.juwimm.cms.components.vo.AddressValue)
	 */
	@Override
	protected void handleUpdateAddressData(AddressValue addressValue) throws Exception {
		AddressHbm address = this.createAddressHbmFromValue(addressValue);
		getAddressHbmDao().update(address);
	}

	/**
	 * @see de.juwimm.cms.components.remote.ComponentsServiceSpring#setPicture4Person(java.lang.Long,
	 *      java.lang.Integer)
	 */
	@Override
	protected void handleSetPicture4Person(Long personId, Integer pictureId) throws Exception {
		PersonHbm person = getPersonHbmDao().load(personId);
		person.setImageId(pictureId);
		getPersonHbmDao().update(person);
	}

	@Override
	protected long handleCreateTalkTime(String talkTimes, String talkTimeType) throws Exception {
		long id = getTalktimeHbmDao().create(talkTimeType, talkTimes);
		return id;
	}

}
