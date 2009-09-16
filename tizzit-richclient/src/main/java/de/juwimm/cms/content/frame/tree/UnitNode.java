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

import javax.swing.Icon;

import org.tizzit.util.ArraySorter;
import org.tizzit.util.Comparer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.components.vo.PersonValue;
import de.juwimm.cms.content.frame.helper.FillHelper;
import de.juwimm.cms.content.frame.helper.IsolatedAggregationHelper;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.UnitValue;

/**
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik </a>
 * @version $Id$
 */
public class UnitNode extends ComponentNode {
	private UnitValue unit;

	/**
	 * The value constructor initializes the instance.
	 * 
	 * @param data the {@link UnitValue} this node bases upon
	 * @param aggHelper the {@link IsolatedAggregationHelper} this instance may use for serializing
	 */
	public UnitNode(UnitValue data, IsolatedAggregationHelper aggHelper) {
		setAggregationHelper(aggHelper);
		setIcon(UIConstants.ICON_UNIT);

		this.unit = data;
		try {
			setClicks(this.getAggregationHelper().getClickHashForNode("unit", Integer.toString(unit.getUnitId())));
			fillNodes();
		} catch (Exception ex) {
		}
	}

	/** @see de.juwimm.cms.content.frame.tree.CheckNode#getToolTipText() */
	public String getToolTipText() {
		return rb.getString("unit");
	}

	/** @see javax.swing.tree.DefaultMutableTreeNode#toString() */
	public String toString() {
		return rb.getString("unit") + ": " + unit.getName();
	}

	/** @see de.juwimm.cms.content.frame.tree.ComponentNode#hasChildren() */
	public boolean hasChildren() {
		return children().hasMoreElements();
	}
	
	/**
	 * 
	 */
	private void fillNodes() {
		if (unit.getAddresses() != null)
			FillHelper.fillAddresses(this, unit.getAddresses(), this.getAggregationHelper());
		if (unit.getTalkTimes() != null)
			FillHelper.fillTalkTimes(this, unit.getTalkTimes(), this.getAggregationHelper());
		if (unit.getPersons() != null) {
			PersonValue[] pdaoarr = unit.getPersons();
			ArraySorter.sort(pdaoarr, new PersonComparer());
			FillHelper.fillPersons(this, pdaoarr, this.getAggregationHelper());
		}
		if (unit.getDepartments() != null)
			FillHelper.fillDepartments(this, unit.getDepartments(), this.getAggregationHelper());
	}

	/**
	 * Returns the {@link UnitValue} this node represents.
	 * 
	 * @return the {@code UnitValue} this node represents
	 */
	public UnitValue getUnitValue() {
		return unit;
	}

	/** @see de.juwimm.cms.content.frame.tree.CheckNode#getIcon() */
	public Icon getIcon() {
		return UIConstants.ICON_UNIT;
	}

	/** @see de.juwimm.cms.content.frame.tree.ComponentNode#remove() */
	public void remove() throws Exception {
		this.removeFromParent();
		((Communication) getBean(Beans.COMMUNICATION)).removeUnit(unit.getUnitId());
	}

	/** @see de.juwimm.cms.content.frame.tree.ComponentNode#getId() */
	public long getId() {
		return unit.getUnitId();
	}

	/** @see de.juwimm.cms.content.frame.tree.ComponentNode#getDescription() */
	public String getDescription() {
		String description = new String();
		Enumeration enume = children();
		ComponentNode child;

		while (enume.hasMoreElements()) {
			child = (ComponentNode) enume.nextElement();
			if (!description.equals("")) description += ", ";
			description += child.getDescription();
		}
		if (description.equals("")) {
			description = unit.getName();
		}
		return description;
	}

	/** @see de.juwimm.cms.content.frame.tree.ComponentNode#getXmlRepresentation(org.w3c.dom.Document, org.w3c.dom.Node) */
	public Node getXmlRepresentation(Document doc, Node node) {
		Element elmInclude = doc.createElement("include");
		node.appendChild(elmInclude);

		elmInclude.setAttribute("type", "unit");
		elmInclude.setAttribute("id", Integer.toString(unit.getUnitId()));

		if (getHashClicks().size() > 0) {
			Element elmContent = doc.createElement("content");
			elmInclude.appendChild(elmContent);

			java.util.Enumeration e = getHashClicks().keys(); 
			while (e.hasMoreElements()) {
				String elName = (String) e.nextElement();
				if (new Integer(1).equals(getHashClicks().get(elName))) {
					elmContent.appendChild(doc.createElement(elName));
				}
			}
		}

		Enumeration enume = children();
		ComponentNode child;

		while (enume.hasMoreElements()) {
			child = (ComponentNode) enume.nextElement();

			elmInclude.appendChild(child.getXmlRepresentation(doc, elmInclude));
		}
		return elmInclude;
	}
	
	/**
	 * A {@link Comparer} implementation for comparing {@link PersonValue}s 
	 * with each other, based on the persons' last names. 
	 */
	private static class PersonComparer implements Comparer {
		
		/** @see org.tizzit.util.Comparer#compare(java.lang.Object, java.lang.Object) */
		public int compare(Object objA, Object objB) {
			PersonValue pda = (PersonValue) objA;
			PersonValue pdb = (PersonValue) objB;
			if (pda == null || pda.getLastname() == null) return -1;
			if (pdb == null || pdb.getLastname() == null) return 1;
			return pda.getLastname().toLowerCase().compareTo(pdb.getLastname().toLowerCase());
		}
	}
}