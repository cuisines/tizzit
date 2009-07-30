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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.components.model.AddressHbm;
import de.juwimm.cms.components.model.DepartmentHbm;
import de.juwimm.cms.components.model.PersonHbm;
import de.juwimm.cms.components.model.TalktimeHbm;
import de.juwimm.cms.components.vo.AddressValue;
import de.juwimm.cms.components.vo.DepartmentValue;
import de.juwimm.cms.components.vo.PersonValue;
import de.juwimm.cms.components.vo.TalktimeValue;
import de.juwimm.cms.vo.UnitSlimValue;
import de.juwimm.cms.vo.UnitValue;

/**
 * @see de.juwimm.cms.model.UnitHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 *         company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class UnitHbmImpl extends UnitHbm {
	private static Logger log = Logger.getLogger(UnitHbmImpl.class);
	/**
	 * The serial version UID of this class. Needed for serialization.
	 */
	private static final long serialVersionUID = 9021931885682469670L;

	/**
	 * @see de.juwimm.cms.model.UnitHbm#update(de.juwimm.cms.vo.UnitValue)
	 */
	public void update(UnitValue unit) {
		try {
			setName(unit.getName());
			setImageId(unit.getImageId());
			setLogoId(unit.getLogoId());
			long ts = System.currentTimeMillis();
			setLastModifiedDate(ts);
		} catch (Exception e) {
			log.error("Error up9-dating unit: " + e.getMessage(), e);
		}
	}

	/**
	 * @see de.juwimm.cms.model.UnitHbm#toXml(int)
	 */
	public String toXml(int tabdepth) {
		boolean isRootUnit = false;
		if (getSite().getRootUnit().getUnitId().intValue() == this.getUnitId().intValue()) {
			isRootUnit = true;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<unit id=\"").append(getUnitId()).append("\" imageId=\"").append(getImageId());
		sb.append("\" logoId=\"").append(getLogoId()).append("\" siteid=\"").append(getSite().getSiteId());
		sb.append("\" isRootUnit=\"").append(Boolean.toString(isRootUnit)).append("\">");
		sb.append("<![CDATA[").append(getName()).append("]]>");
		sb.append("</unit>\n");
		return sb.toString();
	}

	/**
	 * @see de.juwimm.cms.model.UnitHbm#getUnitSlimValue()
	 */
	public UnitSlimValue getUnitSlimValue() {
		UnitSlimValue value = new UnitSlimValue();
		try {
			value.setUnitId(getUnitId());
			value.setImageId(getImageId());
			value.setLogoId(getLogoId());
			value.setName(getName());
		} catch (Exception e) {
			throw new EJBException(e);
		}
		return value;
	}

	/**
	 * @see de.juwimm.cms.model.UnitHbm#setUnitSlimValue(de.juwimm.cms.vo.UnitSlimValue)
	 */
	public void setUnitSlimValue(UnitSlimValue value) {
		setImageId(value.getImageId());
		setLogoId(value.getLogoId());
		setName(value.getName());
	}
}
