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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import de.juwimm.cms.components.vo.AddressValue;
import de.juwimm.cms.components.vo.PersonValue;
import de.juwimm.cms.components.vo.TalktimeValue;
import de.juwimm.cms.model.UnitHbm;

/**
 * @see de.juwimm.cms.components.model.PersonHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * , Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class PersonHbmImpl extends PersonHbm {
	private static Logger log = Logger.getLogger(PersonHbmImpl.class);
	private static final long serialVersionUID = -4369651079722360324L;

	/**
	 * @see de.juwimm.cms.components.model.PersonHbm#addAddress(de.juwimm.cms.components.model.AddressHbm)
	 */
	@Override
	public void addAddress(AddressHbm address) {
		if (getAddresses() != null) {
			getAddresses().add(address);
		} else {
			List<AddressHbm> addresses = new LinkedList<AddressHbm>();
			addresses.add(address);
			setAddresses(addresses);
		}
	}

	/**
	 * @see de.juwimm.cms.components.model.PersonHbm#removeAddress(de.juwimm.cms.components.model.AddressHbm)
	 */
	@Override
	public void removeAddress(AddressHbm address) {
		try {
			getAddresses().remove(address);
		} catch (Exception e) {
			log.error("Could not remove address from person with id " + getPersonId(), e);
		}
	}

	/**
	 * @see de.juwimm.cms.components.model.PersonHbm#addDepartment(de.juwimm.cms.components.model.DepartmentHbm)
	 */
	@Override
	public void addDepartment(DepartmentHbm department) {
		if (getDepartments() != null) {
			getDepartments().add(department);
			//			department.addPerson(this);
		} else {
			List<DepartmentHbm> departments = new LinkedList<DepartmentHbm>();
			departments.add(department);
			setDepartments(departments);
		}
	}

	/**
	 * @see de.juwimm.cms.components.model.PersonHbm#removeDepartment(de.juwimm.cms.components.model.DepartmentHbm)
	 */
	@Override
	public void removeDepartment(DepartmentHbm department) {
		try {
			getDepartments().remove(department);
		} catch (Exception e) {
			log.error("Could not remove department from person with id " + getPersonId(), e);
		}
	}

	/**
	 * @see de.juwimm.cms.components.model.PersonHbm#addTalktime(de.juwimm.cms.components.model.TalktimeHbm)
	 */
	@Override
	public void addTalktime(TalktimeHbm talktime) {
		if (getTalktimes() != null) {
			getTalktimes().add(talktime);
		} else {
			List<TalktimeHbm> talktimes = new LinkedList<TalktimeHbm>();
			talktimes.add(talktime);
			setTalktimes(talktimes);
		}
	}

	/**
	 * @see de.juwimm.cms.components.model.PersonHbm#removeTalktime(de.juwimm.cms.components.model.TalktimeHbm)
	 */
	@Override
	public void removeTalktime(TalktimeHbm talktime) {
		try {
			getTalktimes().remove(talktime);
		} catch (Exception e) {
			log.error("Could not remove talktime from person with id " + getPersonId(), e);
		}
	}

	/**
	 * @see de.juwimm.cms.components.model.PersonHbm#getDao(int)
	 */
	@Override
	public PersonValue getDao(int depth) {
		PersonValue dao = null;
		try {
			dao = new PersonValue();
			dao.setPersonId(getPersonId());
			dao.setPosition(getPosition());
			dao.setSalutation(getSalutation());
			dao.setSex(getSex());
			dao.setTitle(getTitle());
			dao.setFirstname(getFirstname());
			dao.setLastname(getLastname());
			dao.setBirthDay(getBirthDay());
			dao.setJob(getJob());
			dao.setImageId(getImageId());
			dao.setJobTitle(getJobTitle());
			dao.setCountryJob(getCountryJob());
			dao.setMedicalAssociation(getMedicalAssociation());
			dao.setLinkMedicalAssociation(getLinkMedicalAssociation());
			dao.setLastModifiedDate(this.getLastModifiedDate());
			dao.setExternalId(this.getExternalId());

			//set the first unit (at this time we have a n:1 relationship between person:unit) as UnitId
			try {
				dao.setUnitId(new Long(((UnitHbm) getUnits().iterator().next()).getUnitId().intValue()));
			} catch (Exception exe) {
			}

			Vector vec;
			Collection col;
			Iterator it;
			col = getAddresses();
			if (!col.isEmpty() && depth == 0) {
				dao.setHasChildren(true);
			} else if (!col.isEmpty()) {
				it = col.iterator();
				vec = new Vector();

				while (it.hasNext()) {
					vec.addElement(((AddressHbm) it.next()).getData());
				}
				if (vec.size() > 0) {
					dao.setAddresses((AddressValue[]) vec.toArray(new AddressValue[0]));
					dao.setHasChildren(true);
				}
			}

			col = getTalktimes();
			if (!col.isEmpty() && depth == 0) {
				dao.setHasChildren(true);
			} else if (!col.isEmpty()) {
				it = col.iterator();
				vec = new Vector();

				while (it.hasNext()) {
					vec.addElement(((TalktimeHbm) it.next()).getData());
				}
				if (vec.size() > 0) {
					dao.setTalktimes((TalktimeValue[]) vec.toArray(new TalktimeValue[0]));
					dao.setHasChildren(true);
				}
			}
			//dao.setDepartmentId(this.getDepartment().getDeparmentId());
		} catch (Exception e) {
			log.warn("GET DAO " + e.getMessage());
		}

		return dao;
	}

	/**
	 * @see de.juwimm.cms.components.model.PersonHbm#update(de.juwimm.cms.components.vo.PersonValue)
	 */
	@Override
	public void update(PersonValue personValue) {
		//@todo implement public void update(de.juwimm.cms.components.vo.PersonValue personValue)
		throw new UnsupportedOperationException("de.juwimm.cms.components.model.PersonHbm.update(de.juwimm.cms.components.vo.PersonValue personValue) Not implemented!");
	}

	/**
	 * @see de.juwimm.cms.components.model.PersonHbm#toXmlRecursive(int)
	 */
	@Override
	public String toXmlRecursive(int tabdepth) {
		StringBuffer sb = new StringBuffer();
		sb.append("<person id=\"").append(getPersonId()).append("\" imageid=\"").append(getImageId()).append("\">\n");
		sb.append("<birthDay><![CDATA[").append(this.getValidField(getBirthDay())).append("]]></birthDay>\n");
		sb.append("<countryJob><![CDATA[").append(this.getValidField(getCountryJob())).append("]]></countryJob>\n");
		sb.append("<firstname><![CDATA[").append(this.getValidField(getFirstname())).append("]]></firstname>\n");
		sb.append("<job><![CDATA[").append(this.getValidField(getJob())).append("]]></job>\n");
		sb.append("<jobTitle><![CDATA[").append(this.getValidField(getJobTitle())).append("]]></jobTitle>\n");
		sb.append("<lastname><![CDATA[").append(this.getValidField(getLastname())).append("]]></lastname>\n");
		sb.append("<linkMedicalAssociation><![CDATA[").append(this.getValidField(getLinkMedicalAssociation())).append("]]></linkMedicalAssociation>\n");
		sb.append("<medicalAssociation><![CDATA[").append(this.getValidField(getMedicalAssociation())).append("]]></medicalAssociation>\n");
		sb.append("<position>").append(getPosition()).append("</position>\n");
		sb.append("<salutation><![CDATA[").append(this.getValidField(getSalutation())).append("]]></salutation>\n");
		sb.append("<sex>").append(getSex()).append("</sex>\n");
		sb.append("<title><![CDATA[").append(this.getValidField(getTitle())).append("]]></title>\n");
		sb.append("<externalId><![CDATA[").append(this.getValidField(this.getExternalId())).append("]]></externalId>\n");
		{
			Collection<AddressHbm> addresses = getAddresses();
			Iterator<AddressHbm> it = addresses.iterator();
			while (it.hasNext()) {
				AddressHbm adr = it.next();
				sb.append(adr.toXml(tabdepth + 1));
			}
		}
		{
			Collection<TalktimeHbm> ttimes = getTalktimes();
			Iterator<TalktimeHbm> it = ttimes.iterator();
			while (it.hasNext()) {
				TalktimeHbm ttime = it.next();
				sb.append(ttime.toXml(tabdepth + 1));
			}
		}
		sb.append("</person>\n");
		return sb.toString();
	}

	private String getValidField(String field) {
		return (field == null) ? "" : field;
	}

}
