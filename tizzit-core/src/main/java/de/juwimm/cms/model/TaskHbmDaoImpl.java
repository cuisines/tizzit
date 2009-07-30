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
package de.juwimm.cms.model;

import org.apache.log4j.Logger;

/**
 * @see de.juwimm.cms.model.TaskHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> ,
 *         Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class TaskHbmDaoImpl extends TaskHbmDaoBase {
	private static Logger log = Logger.getLogger(TaskHbmDaoImpl.class);

	@Override
	public TaskHbm create(TaskHbm taskHbm) {
		return super.create(taskHbm);
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findAll(final int transform, final java.lang.Integer siteId) {
		return this.findAll(transform, "from de.juwimm.cms.model.TaskHbm as t where t.unit.site.siteId = ?", siteId);
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findBySender(final int transform, final java.lang.String userName) {
		return this.findBySender(transform, "from de.juwimm.cms.model.TaskHbm as t where t.sender.userId = ?", userName);
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findByReceiver(final int transform, final java.lang.String userName) {
		return this.findByReceiver(transform, "from de.juwimm.cms.model.TaskHbm as t where t.receiver.userId = ?", userName);
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findAll4Type(final int transform, final java.lang.Byte taskType, final java.lang.Integer siteId) {
		return this.findAll4Type(transform, "from de.juwimm.cms.model.TaskHbm as t where t.taskType = ? and (t.unit is null or t.unit.site.siteId = ?)", taskType, siteId);
	}
}
