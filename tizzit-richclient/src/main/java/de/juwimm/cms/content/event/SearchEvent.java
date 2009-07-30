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
package de.juwimm.cms.content.event;

import org.w3c.dom.Node;

/**
 * The Event-token for sending the Information about a succeeded EditpaneHandler save-operation.
 * <p><b>Copyright: JuwiMacMillan Group GmbH (c) 2002</b></p>
 * @author <a href="mailto:d.bogun@juwimm.com">Dirk Bogun</a>
 * @version $Id$
 */
public class SearchEvent extends java.util.EventObject {

	private Node prop;
	private String id = "";
	private int roleType = 0;

	public SearchEvent(Object source) {
		super(source);
	}

	public void setProperties(Node prop) {
		this.prop = prop;
	}

	public Node getProperties() {
		return this.prop;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getRoleType() {
		return this.roleType;
	}

	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}
}