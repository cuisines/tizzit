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

import de.juwimm.cms.vo.LockValue;

/**
 * @see de.juwimm.cms.model.LockHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * , Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class LockHbmImpl extends LockHbm {
	private static final long serialVersionUID = -1117351836146485820L;

	/**
	 * @see de.juwimm.cms.model.LockHbm#getDao()
	 */
	@Override
	public LockValue getDao() {
		LockValue data = new LockValue();
		data.setLockId(getLockId());
		if (getOwner() != null) {
			data.setOwner(getOwner().getUserId());
		} else {
			data.setOwner("User does not exist anymore!");
		}
		data.setCreateDate(getCreateDate()); // darf nicht NULL in Datenbank sein
		return data;
	}

}
