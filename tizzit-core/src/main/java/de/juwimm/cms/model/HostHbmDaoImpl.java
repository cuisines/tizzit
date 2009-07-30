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

import org.apache.log4j.Logger;

import de.juwimm.cms.vo.HostValue;

/**
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> , Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class HostHbmDaoImpl extends HostHbmDaoBase {
	private static Logger log = Logger.getLogger(HostHbmDaoImpl.class);

	@Override
	public HostHbm create(HostHbm hostHbm) {
		return super.create(hostHbm);
	}

	@Override
	protected void handleSetHostValue(HostValue value, HostHbm host) throws Exception {
		if (value.getStartPageId() != null) {
			try {
				host.setStartPage(getViewComponentHbmDao().load(value.getStartPageId()));
			} catch (Exception e) {
				log.error("Error assigning host " + value.getHostName() + " to startpage " + value.getStartPageId() + ": " + e.getMessage(), e);
			}
		}
		if (value.getUnitId() != null) {
			try {
				host.setUnit(getUnitHbmDao().load(value.getUnitId()));
			} catch (Exception e) {
				log.error("Error assigning host " + value.getHostName() + " to unit " + value.getUnitId() + ": " + e.getMessage(), e);
			}
		}
		if (value.getRedirectHostName() != null) {
			try {
				host.setRedirectHostName(load(value.getRedirectHostName()));
			} catch (Exception e) {
				log.error("Error assigning redirect-host " + value.getRedirectHostName() + " to host " + value.getHostName() + ": " + e.getMessage(), e);
			}
		}
		if (value.getRedirectUrl() != null) {
			try {
				host.setRedirectUrl(value.getRedirectUrl());
			} catch (Exception e) {
				log.error("Error assigning redirect-url to host " + value.getHostName() + ": " + e.getMessage(), e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findAll(final int transform) {
		return this.findAll(transform, "from de.juwimm.cms.model.HostHbm as hostHbm");
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findAll(final int transform, final java.lang.Integer siteId) {
		return this.findAll(transform, "from de.juwimm.cms.model.HostHbm as h where h.site.siteId = ?", siteId);
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findAllUnassigned(final int transform) {
		return this.findAllUnassigned(transform, "from de.juwimm.cms.model.HostHbm as h where h.site is null");
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findAllWithStartPage4Site(final int transform, final java.lang.Integer siteId) {
		return this.findAllWithStartPage4Site(transform, "from de.juwimm.cms.model.HostHbm as h where h.site.siteId = ? and h.startPage is not null", siteId);
	}
}
