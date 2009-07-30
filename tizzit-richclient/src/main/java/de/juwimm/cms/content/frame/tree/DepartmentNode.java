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
package de.juwimm.cms.content.frame.tree;

import static de.juwimm.cms.client.beans.Application.*;

import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.components.vo.DepartmentValue;
import de.juwimm.cms.content.frame.helper.FillHelper;
import de.juwimm.cms.content.frame.helper.IsolatedAggregationHelper;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class DepartmentNode extends ComponentNode {
	private static Logger log = Logger.getLogger(DepartmentNode.class);
	private DepartmentValue department;

	public boolean hasChildren() {
		return children().hasMoreElements();
		//return m_Dao.isHasChildren();
	}

	public void loadNodes() {
		try {
			department = ((Communication) getBean(Beans.COMMUNICATION)).getDepartment(department.getDepartmentId());
		} catch (Exception exe) {
			log.error("Error loading nodes", exe);
		}
		fillNodes();
		this.setInit();
	}

	public DepartmentNode() {
	}

	public DepartmentNode(DepartmentValue dao, IsolatedAggregationHelper aggHelper) {
		this.setAggregationHelper(aggHelper);
		setIcon(UIConstants.ICON_DEPARTMENT);

		department = dao;
		try {
			String id = department.getDepartmentId() + "";
			setCheckSelected(this.getAggregationHelper().isInAggregation("department", id));
			setClicks(this.getAggregationHelper().getClickHashForNode("department", id));
			fillNodes();
		} catch (Exception ex) {
		}
	}

	public String getToolTipText() {
		return "Bereich";
	}

	public String toString() {
		return department.getName();
	}

	private void fillNodes() {
		if (this.isCheckSelected()
				&& (department.getAddresses() == null || department.getTalkTimes() == null || department.getPersons() == null)) {
			try {
				department = ((Communication) getBean(Beans.COMMUNICATION)).getDepartment(department.getDepartmentId());
			} catch (Exception exe) {
			}
		}
		if (department.getAddresses() != null)
			FillHelper.fillAddresses(this, department.getAddresses(), this.getAggregationHelper());
		if (department.getTalkTimes() != null)
			FillHelper.fillTalkTimes(this, department.getTalkTimes(), this.getAggregationHelper());
		if (department.getPersons() != null)
			FillHelper.fillPersons(this, department.getPersons(), this.getAggregationHelper());

		if (this.isLeaf() && department.isHasChildren()) {
			CheckNode dummy = new CheckNode("dummy");
			this.add(dummy);
		}
	}

	public DepartmentValue getDepartmentValue() {
		return department;
	}

	public void remove() throws Exception {
		this.removeFromParent();
		((Communication) getBean(Beans.COMMUNICATION)).removeDepartment(department.getDepartmentId());
	}

	public long getId() {
		return department.getDepartmentId();
	}

	/**
	 * @todo TO DO: Implement this, if we fully implement the Department-Feature
	 * @return
	 */
	public String getDescription() {
		return "";
	}

	public Node getXmlRepresentation(Document doc, Node node) {
		Element include = doc.createElement("include");
		node.appendChild(include);

		include.setAttribute("type", "department");
		include.setAttribute("id", department.getDepartmentId() + "");

		if (getHashClicks().size() > 0) {
			Element content = doc.createElement("content");
			include.appendChild(content);

			for (java.util.Enumeration e = getHashClicks().keys(); e.hasMoreElements();) {
				String elName = (String) e.nextElement();

				if (new Integer(1).equals(getHashClicks().get(elName))) {
					content.appendChild(doc.createElement(elName));
				}
			}
		}

		Enumeration enume = children();
		ComponentNode child;

		while (enume.hasMoreElements()) {
			child = (ComponentNode) enume.nextElement();

			include.appendChild(child.getXmlRepresentation(doc, include));
		}
		return include;
	}
}