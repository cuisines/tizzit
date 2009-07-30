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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.components.vo.AddressValue;
import de.juwimm.cms.content.frame.helper.IsolatedAggregationHelper;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;

/**
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik </a>
 * @version $Id$
 */
public class AddressNode extends ComponentNode {
	private AddressValue address;

	public AddressNode() {
	}

	public AddressNode(AddressValue data, IsolatedAggregationHelper aggHelper) {
		this.setAggregationHelper(aggHelper);
		setIcon(UIConstants.ICON_ADDRESS);

		this.address = data;
		try {
			setCheckSelected(this.getAggregationHelper().isInAggregation("address", Long.toString(address.getAddressId())));
			setClicks(this.getAggregationHelper().getClickHashForNode("address", Long.toString(address.getAddressId())));
		} catch (Exception ex) {
		}
	}

	public boolean hasChildren() {
		return false;
	}

	public String getToolTipText() {
		return rb.getString("address");
	}

	public String toString() {
		return this.address.getAddressType();
	}

	public AddressValue getData() {
		return this.address;
	}

	public void remove() throws Exception {
		this.removeFromParent();
		((Communication) getBean(Beans.COMMUNICATION)).removeAddress(this.address.getAddressId());
	}

	public long getId() {
		return this.address.getAddressId();
	}

	public String getDescription() {
		return new String();
	}

	public Node getXmlRepresentation(Document doc, Node node) {

		Element include = doc.createElement("include");
		node.appendChild(include);

		include.setAttribute("type", "address");
		include.setAttribute("id", Long.toString(address.getAddressId()));

		Element content = doc.createElement("content");
		include.appendChild(content);

		for (java.util.Enumeration e = getHashClicks().keys(); e.hasMoreElements();) {
			String elName = (String) e.nextElement();

			if (new Integer(1).equals(getHashClicks().get(elName))) {
				content.appendChild(doc.createElement(elName));
			}
		}
		return include;
	}

}