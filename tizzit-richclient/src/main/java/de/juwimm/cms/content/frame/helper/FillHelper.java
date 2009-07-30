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
package de.juwimm.cms.content.frame.helper;

import java.util.Iterator;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;

import de.juwimm.cms.components.vo.AddressValue;
import de.juwimm.cms.components.vo.DepartmentValue;
import de.juwimm.cms.components.vo.PersonValue;
import de.juwimm.cms.components.vo.TalktimeValue;
import de.juwimm.cms.content.frame.tree.AddressNode;
import de.juwimm.cms.content.frame.tree.DepartmentNode;
import de.juwimm.cms.content.frame.tree.PersonNode;
import de.juwimm.cms.content.frame.tree.TalkTimeNode;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public final class FillHelper {
	private static Logger log = Logger.getLogger(FillHelper.class);

	private FillHelper() {
	}

	public static void fillPersons(DefaultMutableTreeNode node, Vector persons, IsolatedAggregationHelper aggHelper) {
		if (persons == null) return;

		PersonNode person;
		Iterator it = persons.iterator();
		while (it.hasNext()) {
			person = new PersonNode((PersonValue) it.next(), aggHelper);
			node.add(person);
		}
	}

	public static void fillPersons(DefaultMutableTreeNode node, PersonValue[] persons, IsolatedAggregationHelper aggHelper) {
		if (persons == null) return;

		PersonNode person;
		for (int i = 0; i < persons.length; i++) {
			PersonValue pdao = persons[i]; //(PersonDao)it.next();
			person = new PersonNode(pdao, aggHelper);
			person.setViewType(aggHelper.lookupPersonViewType(String.valueOf(pdao.getPersonId())));
			node.add(person);
		}
	}

	public static void fillDepartments(DefaultMutableTreeNode node, Vector departments, IsolatedAggregationHelper aggHelper) {
		if (departments == null) return;

		DepartmentNode department;
		Iterator it = departments.iterator();
		while (it.hasNext()) {
			department = new DepartmentNode((DepartmentValue) it.next(), aggHelper);
			node.add(department);
		}
	}

	public static void fillDepartments(DefaultMutableTreeNode node, DepartmentValue[] departments, IsolatedAggregationHelper aggHelper) {
		if (departments == null) return;

		DepartmentNode department;
		for (int i = 0; i < departments.length; i++) {
			department = new DepartmentNode(departments[i], aggHelper);
			node.add(department);
		}
	}

	public static void fillAddresses(DefaultMutableTreeNode node, Vector addresses, IsolatedAggregationHelper aggHelper) {
		if (addresses == null) return;

		AddressNode address;
		Iterator it = addresses.iterator();

		while (it.hasNext()) {
			address = new AddressNode((AddressValue) it.next(), aggHelper);
			node.add(address);
		}
	}

	public static void fillAddresses(DefaultMutableTreeNode node, AddressValue[] addresses, IsolatedAggregationHelper aggHelper) {
		if (addresses == null) return;

		AddressNode address;
		for (int i = 0; i < addresses.length; i++) {
			address = new AddressNode(addresses[i], aggHelper);
			node.add(address);
		}
	}

	public static void fillTalkTimes(DefaultMutableTreeNode node, Vector talkTimes, IsolatedAggregationHelper aggHelper) {
		if (talkTimes == null) return;

		TalkTimeNode talkTime;
		Iterator it = talkTimes.iterator();
		while (it.hasNext()) {
			talkTime = new TalkTimeNode((TalktimeValue) it.next(), aggHelper);
			node.add(talkTime);
		}
	}

	public static void fillTalkTimes(DefaultMutableTreeNode node, TalktimeValue[] talkTimes, IsolatedAggregationHelper aggHelper) {
		if (talkTimes == null) return;

		TalkTimeNode talkTime;
		for (int i = 0; i < talkTimes.length; i++) {
			talkTime = new TalkTimeNode(talkTimes[i], aggHelper);
			node.add(talkTime);
		}
	}

}