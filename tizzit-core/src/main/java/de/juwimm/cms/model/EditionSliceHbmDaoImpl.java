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
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @see de.juwimm.cms.model.EditionSliceHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> ,
 *         Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class EditionSliceHbmDaoImpl extends EditionSliceHbmDaoBase {
	private static Logger log = Logger.getLogger(EditionSliceHbmDaoImpl.class);

	@Autowired
	private SequenceHbmDao sequenceHbmDao;
	
	@Override
	public EditionSliceHbm create(EditionSliceHbm editionSliceHbm) {
		if (editionSliceHbm.getEditionSliceId() == null || editionSliceHbm.getEditionSliceId().intValue() == 0) {
			try {
				Integer id = sequenceHbmDao.getNextSequenceNumber("editionslice.edition_slice_id");
				editionSliceHbm.setEditionSliceId(id);
			} catch (Exception e) {
				log.error("Error creating primary key", e);
			}
		}
		return super.create(editionSliceHbm);
	}

	public java.lang.Object findByEditionAndSlice(final int transform, final int editionId, final int sliceId) {
		return this.findByEditionAndSlice(transform, "from de.juwimm.cms.model.EditionSliceHbm as e where e.editionId = ? and e.sliceNr = ?", editionId, sliceId);
	}
}
