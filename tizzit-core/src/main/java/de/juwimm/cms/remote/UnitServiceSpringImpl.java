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
package de.juwimm.cms.remote;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.model.UnitHbmDao;
import de.juwimm.cms.model.UnitHbmImpl;
import de.juwimm.cms.remote.helper.AuthenticationHelper;
import de.juwimm.cms.vo.UnitValue;

/**
 * @see de.juwimm.cms.remote.UnitServiceSpring
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class UnitServiceSpringImpl extends de.juwimm.cms.remote.UnitServiceSpringBase {
	private static Log log = LogFactory.getLog(UnitServiceSpringImpl.class);

	/**
	 * @see de.juwimm.cms.remote.UnitServiceSpring#createUnit(java.lang.String)
	 */
	@Override
	protected Integer handleCreateUnit(String unitName) throws Exception {
		UnitHbm unit = null;
		try {
			UserHbm user = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			Collection units = super.getUnitHbmDao().findByName(user.getActiveSite().getSiteId(), unitName);
			if (units.size() > 0) { throw new UserException("UnitnameIsAlreadyUsedException"); }
			UnitHbm unitHbm = new UnitHbmImpl();
			unitHbm.setSite(user.getActiveSite());
			unitHbm.setName(unitName);
			unit = super.getUnitHbmDao().create(unitHbm);
		} catch (Exception e) {
			log.error("UnitnameIsAlreadyUsedException\n" + e.getMessage());
			throw new UserException("UnitnameIsAlreadyUsedException\n" + e.getMessage());
		}
		return unit.getUnitId();
	}

	/**
	 * @see de.juwimm.cms.remote.UnitServiceSpring#removeUnit(de.juwimm.cms.vo.UnitValue)
	 */
	@Override
	protected void handleRemoveUnit(UnitValue unitValue) throws Exception {
		try {
			super.getUnitHbmDao().remove(unitValue.getUnitId());
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.UnitServiceSpring#updateUnit(de.juwimm.cms.vo.UnitValue)
	 */
	@Override
	protected void handleUpdateUnit(UnitValue unitValue) throws Exception {
		try {
			super.getUnitHbmDao().load(unitValue.getUnitId()).update(unitValue);
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.UnitServiceSpring#getAllUnits()
	 */
	@Override
	protected UnitValue[] handleGetAllUnits() throws Exception {
		UnitValue[] udarr = null;
		try {
			UserHbm user = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			Collection coll = super.getUnitHbmDao().findAll(user.getActiveSite().getSiteId());
			//			Collection coll = super.getUnitHbmDao().findAll();
			udarr = new UnitValue[coll.size()];
			Iterator it = coll.iterator();
			int i = 0;
			while (it.hasNext()) {
				UnitHbm ul = (UnitHbm) it.next();
				udarr[i++] = getUnitHbmDao().getDao(ul);
			}
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
		return (udarr);
	}

	/**
	 * @see de.juwimm.cms.remote.UnitServiceSpring#addUser2Unit(de.juwimm.cms.vo.UnitValue, java.lang.String)
	 */
	@Override
	protected void handleAddUser2Unit(UnitValue unitValue, String userNameId) throws Exception {
		try {
			UserHbm user = super.getUserHbmDao().load(userNameId);
			UnitHbm unit = super.getUnitHbmDao().load(unitValue.getUnitId());
			user.addUnit(unit);
			unit.getUsers().add(user);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("Could not add user with id " + userNameId + " to unit with id " + unitValue.getUnitId(), e);
			}
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.UnitServiceSpring#removeUserFromUnit(de.juwimm.cms.vo.UnitValue, java.lang.String)
	 */
	@Override
	protected void handleRemoveUserFromUnit(UnitValue unitValue, String userNameId) throws Exception {
		try {
			UserHbm user = super.getUserHbmDao().load(userNameId);
			UnitHbm unit = super.getUnitHbmDao().load(unitValue.getUnitId());
			user.dropUnit(unit);
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.remote.UnitServiceSpringBase#handleRemoveUnits(de.juwimm.cms.vo.UnitValue[])
	 */
	@Override
	protected void handleRemoveUnits(UnitValue[] units) throws Exception {
		UnitHbmDao uhd = getUnitHbmDao();
		for (int i = 0; i < units.length; i++) {
			uhd.remove(units[i].getUnitId());
		}

	}
}
