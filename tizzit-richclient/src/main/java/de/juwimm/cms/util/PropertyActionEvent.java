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
package de.juwimm.cms.util;

import java.awt.event.ActionEvent;

/**
 * <b>ConQuest Enterprise Content Management</b><br/>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PropertyActionEvent extends ActionEvent {
	public static final int TYPE_GROUP = 1;
	public static final int TYPE_ITEM = 2;
	private int propertyType = 2;
	private String uniqueId;
	private String parentId;
	private String descName;
	private String descDetail;
	private String action;

	public PropertyActionEvent(Object source, int id, String command, String uniqueId, int propertyType) {
		super(source, id, command);
		this.uniqueId = uniqueId;
	}

	/**
	 * @return Returns the idName.
	 */
	public String getUniqueId() {
		return uniqueId;
	}

	/**
	 * @return Returns the actionCommand.
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param actionCommand The actionCommand to set.
	 */
	public void setAction(String actionCommand) {
		this.action = actionCommand;
	}

	/**
	 * @return Returns the descDetail.
	 */
	public String getDescDetail() {
		return descDetail;
	}

	/**
	 * @param descDetail The descDetail to set.
	 */
	public void setDescDetail(String descDetail) {
		this.descDetail = descDetail;
	}

	/**
	 * @return Returns the descName.
	 */
	public String getDescName() {
		return descName;
	}

	/**
	 * @param descName The descName to set.
	 */
	public void setDescName(String descName) {
		this.descName = descName;
	}

	/**
	 * @return Returns the parentId.
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId The parentId to set.
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return Returns the type.
	 */
	public int getPropertyType() {
		return propertyType;
	}
}