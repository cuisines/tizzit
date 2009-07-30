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

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Element;

import de.juwimm.cms.components.vo.PersonValue;
import de.juwimm.cms.model.SequenceHbmDao;
import de.juwimm.util.XercesHelper;

/**
 * @see de.juwimm.cms.components.model.PersonHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> ,
 *         Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class PersonHbmDaoImpl extends PersonHbmDaoBase {
	private static Logger log = Logger.getLogger(PersonHbmDaoImpl.class);
	
	@Autowired
	private SequenceHbmDao sequenceHbmDao;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.juwimm.cms.components.model.PersonHbmDaoBase#create(de.juwimm.cms.components.model.PersonHbm)
	 */
	@Override
	public PersonHbm create(PersonHbm personHbm) {
		if (personHbm.getPersonId() == null) {
			try {
				personHbm.setPersonId(new Long(sequenceHbmDao.getNextSequenceNumber("person.person_id").longValue()));		
			} catch (Exception e) {
				log.error("Error creating primary key", e);
			}
		}
		personHbm.setLastModifiedDate(new Date().getTime());
		return super.create(personHbm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.juwimm.cms.components.model.PersonHbmDaoBase#update(de.juwimm.cms.components.model.PersonHbm)
	 */
	@Override
	public void update(PersonHbm personHbm) {
		personHbm.setLastModifiedDate(new Date().getTime());
		super.update(personHbm);
	}

	@Override
	protected Long handleCreate() throws Exception {
		PersonHbm person = PersonHbm.Factory.newInstance();
		return this.create(person).getPersonId();
	}

	@Override
	protected Long handleCreate(PersonValue value) throws Exception {
		PersonHbm p = PersonHbm.Factory.newInstance();
		p.setAddresses(Arrays.asList(value.getAddresses()));
		p.setBirthDay(value.getBirthDay());
		p.setCountryJob(value.getCountryJob());
		if (value.getDepartmentId() != null) {
			// TODO - AddressHbm <-> DepartmentDao
			// setDepartments
		}
		p.setExternalId(value.getExternalId());
		p.setFirstname(value.getFirstname());
		p.setImageId(value.getImageId());
		p.setJob(value.getJob());
		p.setJobTitle(value.getJobTitle());
		p.setLastModifiedDate(value.getLastModifiedDate());
		p.setLastname(value.getLastname());
		p.setLinkMedicalAssociation(value.getLinkMedicalAssociation());
		p.setMedicalAssociation(value.getMedicalAssociation());
		if (value.getPersonId() != null) {
			p.setPersonId(value.getPersonId());
		} else {
			try {
				Integer id = sequenceHbmDao.getNextSequenceNumber("person.person_id");
				p.setPersonId(new Long(id));
			} catch (Exception e) {
				log.error("Error creating primary key", e);
			}
		}
		p.setPosition(value.getPosition());
		p.setSalutation(value.getSalutation());
		p.setSex(value.getSex());
		p.setTalktimes(Arrays.asList(value.getTalktimes()));
		p.setTitle(value.getTitle());
		// TODO AddressHbm <-> UnitDao
		// p.setUnits(value.getUnitId())
		getHibernateTemplate().save(p);
		return p.getPersonId();
	}

	@Override
	protected Long handleCreate(Element ael, boolean useNewId, Map mappingPersons, Map mappingAddresses, Map mappingTalktime) throws Exception {
		Long pid = new Long(ael.getAttribute("id"));
		PersonHbm person = PersonHbm.Factory.newInstance();
		if (useNewId) {
			try {
				Integer id = sequenceHbmDao.getNextSequenceNumber("person.person_id");
				person.setPersonId(new Long(id));
			} catch (Exception e) {
				log.error("Error creating primary key", e);
			}
			mappingPersons.put(pid, person.getPersonId());
		} else {
			try {
				PersonHbm pl = super.load(pid);
				log.warn("Removing existing Person while creating a new one with Name " + pl.getLastname());
				super.remove(pid);
			} catch (Exception exe) {
				log.error("An error occurred: " + exe.getMessage(), exe);
			} finally {
				person.setPersonId(pid);
			}
		}
		try {
			person.setImageId(new Integer(ael.getAttribute("imageid")));
		} catch (Exception exe) {
		}
		try {
			person.setBirthDay(getNVal(ael, "birthDay"));
		} catch (Exception exe) {
			log.error("An error occurred: " + exe.getMessage(), exe);
		}
		try {
			person.setCountryJob(getNVal(ael, "countryJob"));
			person.setFirstname(getNVal(ael, "firstname"));
			person.setJob(getNVal(ael, "job"));
			person.setJobTitle(getNVal(ael, "jobTitle"));
			person.setLastname(getNVal(ael, "lastname"));
			person.setLinkMedicalAssociation(getNVal(ael, "linkMedicalAssociation"));
			person.setMedicalAssociation(getNVal(ael, "medicalAssociation"));
			person.setPosition(new Byte(getNVal(ael, "position")).byteValue());
			person.setSalutation(getNVal(ael, "salutation"));
			person.setSex(new Byte(getNVal(ael, "sex")).byteValue());
			person.setTitle(getNVal(ael, "title"));
			person.setExternalId(getNVal(ael, "externalId"));
			person.setLastModifiedDate(new Date().getTime());
		} catch (Exception exe) {
			log.warn("Error setting values: ", exe);
		}
		return person.getPersonId();
	}

	private String getNVal(Element ael, String nodeName) {
		String tmp = XercesHelper.getNodeValue(ael, "./" + nodeName);
		if (tmp.equals("null") || tmp.equals("")) { return null; }
		return tmp;
	}

	@Override
	protected Long handleCreate(Element ael, boolean useNewId, Map mappingPersons, Map mappingAddresses, Map mappingTalktime, Map mappingPics) throws Exception {
		Long pid = new Long(ael.getAttribute("id"));
		PersonHbm person = PersonHbm.Factory.newInstance();
		if (useNewId) {
			try {
				Integer id = sequenceHbmDao.getNextSequenceNumber("person.person_id");
				person.setPersonId(new Long(id));
			} catch (Exception e) {
				log.error("Error creating primary key", e);
			}
			mappingPersons.put(pid, person.getPersonId());
		} else {
			try {
				PersonHbm pl = load(pid);
				log.warn("Removing existing Person while creating a new one with Name " + pl.getLastname());
				remove(pid);
			} catch (Exception exe) {
			} finally {
				person.setPersonId(pid);
			}
		}
		Integer oldPicId = null;
		try {
			oldPicId = new Integer(ael.getAttribute("imageid"));
		} catch (Exception e) {
		}
		if (!useNewId) {
			try {
				person.setImageId(oldPicId);
			} catch (Exception exe) {
				log.error("An error occurred: " + exe.getMessage(), exe);
			}
		} else {
			Object o = null;
			if (oldPicId != null) o = mappingPics.get(oldPicId);
			if (o != null) {
				Integer newPicId = (Integer) o;
				person.setImageId(newPicId);
			} else {
				log.warn("Person " + person.getPersonId() + ": New ImageId for oldId " + oldPicId + " not found! Perhaps Image is located in a different Unit?");
			}
		}
		try {
			person.setBirthDay(getNVal(ael, "birthDay"));
		} catch (Exception exe) {
		}
		try {
			person.setCountryJob(getNVal(ael, "countryJob"));
			person.setFirstname(getNVal(ael, "firstname"));
			person.setJob(getNVal(ael, "job"));
			person.setJobTitle(getNVal(ael, "jobTitle"));
			person.setLastname(getNVal(ael, "lastname"));
			person.setLinkMedicalAssociation(getNVal(ael, "linkMedicalAssociation"));
			person.setMedicalAssociation(getNVal(ael, "medicalAssociation"));
			person.setPosition(new Byte(getNVal(ael, "position")).byteValue());
			person.setSalutation(getNVal(ael, "salutation"));
			person.setSex(new Byte(getNVal(ael, "sex")).byteValue());
			person.setTitle(getNVal(ael, "title"));
			person.setExternalId(getNVal(ael, "externalId"));
			person.setLastModifiedDate(new Date().getTime());
		} catch (Exception exe) {
			log.warn("Error setting values: ", exe);
		}
		return person.getPersonId();
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findAll(final int transform) {
		return this.findAll(transform, "from de.juwimm.cms.components.model.PersonHbm as personHbm");
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findAll(final int transform, final java.lang.Integer siteId) {
		return this.findAll(transform, "select p from de.juwimm.cms.components.model.PersonHbm p inner join fetch p.units u where u.site.siteId = ?", siteId);
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findByNameAndUnit(final int transform, final java.lang.Integer unitId, final java.lang.String firstName, final java.lang.String lastName) {
		return this.findByNameAndUnit(transform, "select p from de.juwimm.cms.components.model.PersonHbm p inner join fetch p.units u where u.unitId = ? and p.firstname like ? and p.lastname like ?", unitId, firstName, lastName);
	}
	
	@SuppressWarnings("unchecked")
	public java.util.Collection findByName(final int transform, final java.lang.Integer siteId, final java.lang.String firstName, final java.lang.String lastName) {
		return this.findByName(transform, "select p from de.juwimm.cms.components.model.PersonHbm p inner join fetch p.units u where u.site.siteId = ? and p.firstname like ? and p.lastname like ?", siteId, firstName, lastName);
	}

	public java.lang.Object findByExternalId(final int transform, final java.lang.String externalId) {
		return this.findByExternalId(transform, "from de.juwimm.cms.components.model.PersonHbm p where p.externalId = ?", externalId);
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findByUnit(final int transform, final java.lang.Integer unitId) {
		return this.findByUnit(transform, "select p from de.juwimm.cms.components.model.PersonHbm p inner join p.units n where n.unitId = ?", unitId);
	}
}
