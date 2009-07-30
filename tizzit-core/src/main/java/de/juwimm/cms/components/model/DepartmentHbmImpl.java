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

import java.util.*;

import org.apache.log4j.Logger;

import de.juwimm.cms.components.vo.AddressValue;
import de.juwimm.cms.components.vo.DepartmentValue;
import de.juwimm.cms.components.vo.PersonValue;
import de.juwimm.cms.components.vo.TalktimeValue;

/**
 * @see de.juwimm.cms.components.model.DepartmentHbm
 */
public class DepartmentHbmImpl extends de.juwimm.cms.components.model.DepartmentHbm {
	private static Logger logger = Logger.getLogger(DepartmentHbmImpl.class);

	/**
	 * The serial version UID of this class. Needed for serialization.
	 */
	private static final long serialVersionUID = -3440811444450701784L;

	/**
	 * @see de.juwimm.cms.components.model.DepartmentHbm#addAddress(de.juwimm.cms.components.model.AddressHbm)
	 */
	public void addAddress(de.juwimm.cms.components.model.AddressHbm address) {
		if (getAddresses() != null) {
			getAddresses().add(address);
		} else {
			List<AddressHbm> addresses = new LinkedList<AddressHbm>();
			addresses.add(address);
			setAddresses(addresses);
		}
	}

	/**
	 * @see de.juwimm.cms.components.model.DepartmentHbm#removeAddress(de.juwimm.cms.components.model.AddressHbm)
	 */
	public void removeAddress(de.juwimm.cms.components.model.AddressHbm address) {
		try {
			getAddresses().remove(address);
		} catch (Exception e) {
			logger.error("Could not remove address from department with id " + getDepartmentId(), e);
		}
	}

	/**
	 * @see de.juwimm.cms.components.model.DepartmentHbm#addPerson(de.juwimm.cms.components.model.PersonHbm)
	 */
	public void addPerson(de.juwimm.cms.components.model.PersonHbm person) {
		if (getPersons() != null) {
			getPersons().add(person);
			//			person.addDepartment(this);
		} else {
			List<PersonHbm> persons = new LinkedList<PersonHbm>();
			persons.add(person);
			setPersons(persons);
		}
	}

	/**
	 * @see de.juwimm.cms.components.model.DepartmentHbm#removePerson(de.juwimm.cms.components.model.PersonHbm)
	 */
	public void removePerson(de.juwimm.cms.components.model.PersonHbm person) {
		try {
			getPersons().remove(person);
			//			person.removeDepartment(this);
		} catch (Exception e) {
			logger.error("Could not remove person from department with id " + getDepartmentId(), e);
		}
	}

	/**
	 * @see de.juwimm.cms.components.model.DepartmentHbm#addTalktime(de.juwimm.cms.components.model.TalktimeHbm)
	 */
	public void addTalktime(de.juwimm.cms.components.model.TalktimeHbm talktime) {
		if (getTalktimes() != null) {
			getTalktimes().add(talktime);
		} else {
			List<TalktimeHbm> talktimes = new LinkedList<TalktimeHbm>();
			talktimes.add(talktime);
			setTalktimes(talktimes);
		}
	}

	/**
	 * @see de.juwimm.cms.components.model.DepartmentHbm#removeTalktime(de.juwimm.cms.components.model.TalktimeHbm)
	 */
	public void removeTalktime(de.juwimm.cms.components.model.TalktimeHbm talktime) {
		try {
			getTalktimes().remove(talktime);
		} catch (Exception e) {
			logger.error("Could not remove talktime from department with id " + getDepartmentId(), e);
		}
	}

	/**
	 * @see de.juwimm.cms.components.model.DepartmentHbm#getDao(int)
	 */
	public de.juwimm.cms.components.vo.DepartmentValue getDao(int depth) {
		DepartmentValue dao = null;
		try {
			dao = new DepartmentValue();
			dao.setDepartmentId(getDepartmentId());
			dao.setName(getName());
			try {
				dao.setUnitId(this.getUnit().getUnitId());
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
				vec = new Vector();
				it = col.iterator();
				while (it.hasNext()) {
					vec.addElement(((TalktimeHbm) it.next()).getData());
				}
				if (vec.size() > 0) {
					dao.setTalkTimes((TalktimeValue[]) vec.toArray(new TalktimeValue[0]));
					dao.setHasChildren(true);
				}
			}

			col = getPersons();
			if (!col.isEmpty() && depth == 0) {
				dao.setHasChildren(true);
			} else if (!col.isEmpty()) {
				vec = new Vector();
				it = col.iterator();
				while (it.hasNext()) {
					vec.addElement(((PersonHbm) it.next()).getDao(0));
				}
				if (vec.size() > 0) {
					dao.setPersons((PersonValue[]) vec.toArray(new PersonValue[0]));
					dao.setHasChildren(true);
				}
			}
		} catch (RuntimeException exe) {
			logger.warn("GET DAO " + exe.getMessage());

		}

		return dao;
	}

	/**
	 * @see de.juwimm.cms.components.model.DepartmentHbm#update(de.juwimm.cms.components.vo.DepartmentValue)
	 */
	@Override
	public void update(de.juwimm.cms.components.vo.DepartmentValue departmentValue) {
		setName(departmentValue.getName());
	}

	/**
	 * @see de.juwimm.cms.components.model.DepartmentHbm#toXmlRecursive(int)
	 */
	public java.lang.String toXmlRecursive(int tabdepth) {
		StringBuffer sb = new StringBuffer();
		sb.append("<department id=\"" + getDepartmentId() + "\" name=\"" + getName() + "\">\n");

		Collection addr = getAddresses();
		Iterator it = addr.iterator();
		while (it.hasNext()) {
			AddressHbm adr = (AddressHbm) it.next();
			sb.append(adr.toXml(tabdepth + 1));
		}
		Collection pers = getPersons();
		it = pers.iterator();
		while (it.hasNext()) {
			PersonHbm per = (PersonHbm) it.next();
			sb.append(per.toXmlRecursive(tabdepth + 1));
		}

		Collection ttimes = getTalktimes();
		it = ttimes.iterator();
		while (it.hasNext()) {
			TalktimeHbm ttime = (TalktimeHbm) it.next();
			sb.append(ttime.toXml(tabdepth + 1));
		}

		sb.append("</department>\n");
		return sb.toString();
	}

}