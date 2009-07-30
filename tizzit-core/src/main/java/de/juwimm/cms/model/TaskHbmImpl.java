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
package de.juwimm.cms.model;

import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;

import de.juwimm.cms.vo.TaskValue;
import de.juwimm.cms.vo.ViewComponentValue;

/**
 * @see de.juwimm.cms.model.TaskHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> , Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class TaskHbmImpl extends TaskHbm {
	private static final long serialVersionUID = 8110754786857604707L;

	/**
	 * @see de.juwimm.cms.model.TaskHbm#getTaskValue()
	 */
	@Override
	public TaskValue getTaskValue() {
		TaskValue value = new TaskValue();
		try {
			value.setTaskId(getTaskId());
			value.setCreationDate(getCreationDate());
			value.setComment(getComment());
			value.setReceiverRole(getReceiverRole());
			value.setTaskType(getTaskType());
			value.setStatus(getStatus());
			if (getUnit() != null) value.setUnit(getUnit().getUnitSlimValue());
			if (getSender() != null) value.setSender(getSender().getUserValue());

			Collection coll = getViewComponents();
			ViewComponentValue[] vcs = new ViewComponentValue[coll.size()];
			int i = 0;
			for (Iterator it = coll.iterator(); it.hasNext();) {
				vcs[i++] = ((ViewComponentHbm) it.next()).getViewComponentDao();
			}
			value.setViewComponents(vcs);
		} catch (Exception e) {
			throw new EJBException(e);
		}
		return value;
	}

	/**
	 * @see de.juwimm.cms.model.TaskHbm#setTaskValue(de.juwimm.cms.vo.TaskValue)
	 */
	@Override
	public void setTaskValue(TaskValue value) {
		try {
			setCreationDate(value.getCreationDate());
			setComment(value.getComment());
			setReceiverRole(value.getReceiverRole());
			setTaskType(value.getTaskType());
			setStatus(value.getStatus());
		} catch (Exception e) {
			throw new javax.ejb.EJBException(e);
		}
	}

}
