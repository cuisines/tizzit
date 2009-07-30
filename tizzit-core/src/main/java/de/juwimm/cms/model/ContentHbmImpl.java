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

import org.apache.log4j.Logger;

import de.juwimm.cms.vo.ContentValue;

/**
 * @see de.juwimm.cms.model.ContentHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * , Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class ContentHbmImpl extends ContentHbm {
	private static Logger log = Logger.getLogger(ContentHbmImpl.class);
	private static final long serialVersionUID = -5240525056882338680L;

	/**
	 * @see de.juwimm.cms.model.ContentHbm#getLastContentVersion()
	 */
	@Override
	public ContentVersionHbm getLastContentVersion() {
		Collection contentset = this.getContentVersions();
		Iterator it = contentset.iterator();
		ContentVersionHbm contentVersion = null;
		ContentVersionHbm latest = null;

		while (it.hasNext()) {
			contentVersion = (ContentVersionHbm) it.next();
			if (!contentVersion.getVersion().equals("PUBLS")) {
				if (latest == null) {
					latest = contentVersion;
				} else {
					int newid = new Integer(contentVersion.getVersion()).intValue();
					int currid = new Integer(latest.getVersion()).intValue();
					if (newid > currid) {
						latest = contentVersion;
					}
				}
			}
		}

		return latest;
	}

	/**
	 * @see de.juwimm.cms.model.ContentHbm#getContentVersionForPublish()
	 */
	@Override
	public ContentVersionHbm getContentVersionForPublish() {
		ContentVersionHbm cv = null;
		Collection contentset = this.getContentVersions();
		Iterator it = contentset.iterator();
		while (it.hasNext()) {
			ContentVersionHbm contentVersionHbm = (ContentVersionHbm) it.next();
			if (contentVersionHbm.getVersion().equals("PUBLS")) {
				cv = contentVersionHbm;
				break;
			}
		}
		return cv;
	}

	/**
	 * @see de.juwimm.cms.model.ContentHbm#getDao()
	 */
	@Override
	public ContentValue getDao() {
		ContentVersionHbm cv = this.getLastContentVersion();
		return toValue(cv);
	}

	/**
	 * @see de.juwimm.cms.model.ContentHbm#getDaoWithPUBLSVersion()
	 */
	@Override
	public ContentValue getDaoWithPUBLSVersion() {
		ContentVersionHbm contentVersion = this.getContentVersionForPublish();
		return toValue(contentVersion);
	}

	private ContentValue toValue(ContentVersionHbm contentVersion) {
		ContentValue data = new ContentValue();
		if (contentVersion != null) {
			data.setContentText(contentVersion.getText());
			data.setCreateDate(contentVersion.getCreateDate());
			data.setCreator(contentVersion.getCreator());
			data.setVersion(contentVersion.getVersion()); 
			data.setHeading(contentVersion.getHeading());
			data.setTemplate(this.getTemplate());
			data.setContentId(this.getContentId());
			if (contentVersion.getLock() != null) {
				try {
					data.setLock(contentVersion.getLock().getDao());
				} catch (Exception exe) {
					contentVersion.setLock(null);
				}
			}
		}
		return data;
	}

}
