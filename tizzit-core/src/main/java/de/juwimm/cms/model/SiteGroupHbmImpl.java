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

import de.juwimm.cms.vo.SiteGroupValue;
import de.juwimm.cms.vo.SiteValue;

/**
 * @see de.juwimm.cms.model.SiteGroupHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class SiteGroupHbmImpl extends SiteGroupHbm {
	private static final long serialVersionUID = -4184883353227629194L;

	/**
	 * @see de.juwimm.cms.model.SiteGroupHbm#getSiteGroupValue()
	 */
	@Override
	public SiteGroupValue getSiteGroupValue() {
		SiteGroupValue value = new SiteGroupValue();
		value.setSiteGroupId(this.getSiteGroupId());
		value.setSiteGroupName(this.getSiteGroupName());
		ArrayList siteList = new ArrayList();
		Iterator it = this.getSites().iterator();
		while (it.hasNext()) {
			SiteHbm site = (SiteHbm) it.next();
			siteList.add(site.getSiteValue());
		}
		value.setSiteValues((SiteValue[]) siteList.toArray(new SiteValue[0]));

		return value;
	}

}
