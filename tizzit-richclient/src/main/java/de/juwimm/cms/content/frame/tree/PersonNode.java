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
import static de.juwimm.cms.common.Constants.*;

import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.components.vo.PersonValue;
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
public class PersonNode extends ComponentNode {
	private static Logger log = Logger.getLogger(PersonNode.class);
	private PersonValue person;
	private String viewType = new String();

	public void loadNodes() {
		try {
			setPerson(((Communication) getBean(Beans.COMMUNICATION)).getPerson(getPerson().getPersonId()));
		} catch (Exception exe) {
			log.error("Initialization error", exe);
		}
		fillNodes();
		this.setInit();
	}

	public boolean hasChildren() {
		return children().hasMoreElements();
		//return m_Dao.isHasChildren();
	}

	public PersonNode() {
	}

	public PersonNode(PersonValue dao, IsolatedAggregationHelper aggHelper) {
		this.setAggregationHelper(aggHelper);
		setIcon(UIConstants.ICON_PERSON);
		setPerson(dao);
		try {
			String id = Long.toString(getPerson().getPersonId());
			setCheckSelected(this.getAggregationHelper().isInAggregation("person", id));
			setClicks(this.getAggregationHelper().getClickHashForNode("person", id));
			fillNodes();

		} catch (Exception ex) {
		}
	}

	public String getToolTipText() {
		return rb.getString("person");
	}

	public String toString() {
		return getPerson().getLastname() + ", " + getPerson().getFirstname();
	}

	protected void fillNodes() {
		setSelected(true);
		if (this.isCheckSelected() && (getPerson().getAddresses() == null || getPerson().getTalktimes() == null)) {
			try {
				setPerson(((Communication) getBean(Beans.COMMUNICATION)).getPerson(getPerson().getPersonId()));
			} catch (Exception exe) {
			}
		}
		if (getPerson().getAddresses() != null)
			FillHelper.fillAddresses(this, getPerson().getAddresses(), this.getAggregationHelper());
		if (getPerson().getTalktimes() != null)
			FillHelper.fillTalkTimes(this, getPerson().getTalktimes(), this.getAggregationHelper());

		if (this.isLeaf() && getPerson().isHasChildren()) {
			CheckNode dummy = new CheckNode("dummy");
			this.add(dummy);
		}
	}

	public PersonValue getPersonValue() {
		return getPerson();
	}

	public void remove() throws Exception {
		((Communication) getBean(Beans.COMMUNICATION)).removePerson(getPerson().getPersonId());
		((ComponentNode) this.getParent()).remove(this);
	}

	public long getId() {
		if(getPerson() == null) return 0;
		return getPerson().getPersonId();
	}

	public String getDescription() {
		return getPerson().getLastname();
	}

	public Node getXmlRepresentation(Document doc, Node node) {
		Element include = doc.createElement("include");
		node.appendChild(include);

		include.setAttribute("type", "person");
		include.setAttribute("id", Long.toString(getPerson().getPersonId()));
		//include.setAttribute("linkid", "");
		include.setAttribute("listtype", this.viewType);

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

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	/**
	 * @param person The person to set.
	 */
	protected void setPerson(PersonValue person) {
		this.person = person;
	}

	/**
	 * @return Returns the person.
	 */
	protected PersonValue getPerson() {
		return person;
	}
}