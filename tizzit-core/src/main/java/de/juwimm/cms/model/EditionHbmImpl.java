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

import java.util.Date;

import de.juwimm.cms.common.Constants.LiveserverDeployStatus;
import de.juwimm.cms.vo.EditionValue;

/**
 * @see de.juwimm.cms.model.EditionHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 *         company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class EditionHbmImpl extends EditionHbm {
	private static final long serialVersionUID = -1092721299580130722L;

	/**
	 * @see de.juwimm.cms.model.EditionHbm#getDao()
	 */
	@Override
	public EditionValue getDao() {
		EditionValue dao = new EditionValue();
		String deployStatus = getDeployStatus() == null ? "" : new String(getDeployStatus());
		dao.setEditionId(getEditionId());
		dao.setCreationDate(getCreationDate());
		dao.setComment(getComment());
		try {
			dao.setCreatorName(getCreator().getLastName() + ", " + getCreator().getFirstName());
		} catch (Exception exe) {
			dao.setCreatorName("");
		}
		dao.setStatus(getStatus());
		dao.setViewDocumentId(getViewDocumentId() == null ? 0 : getViewDocumentId());
		dao.setUnitId(getUnitId());
		dao.setNeedsDeploy(isNeedsDeploy());
		dao.setNeedsImport(isNeedsImport());
		dao.setStartActionTimestamp(getStartActionTimestamp() == null ? null : new Date(getStartActionTimestamp()));
		dao.setEndActionTimestamp(getEndActionTimestamp() == null ? new Date() : new Date(getEndActionTimestamp()));
		dao.setWorkServerEditionId(getWorkServerEditionId());
		dao.setDeployStatus(deployStatus);

		//split Deploy status into meanings: Deploy State, hasException, extraMessages
		String[] deployStatuses = getDeployStatus() != null ? deployStatus.split(";") : null;
		if (deployStatuses != null) {
			String extraMessagesPrefix = "";
			if (deployStatuses.length >= 1) {
				//contains exception
				if (deployStatuses[0].contains(LiveserverDeployStatus.Exception.name())) {
					dao.setException(true);
					extraMessagesPrefix += LiveserverDeployStatus.Exception.name() + ";";
					if (deployStatuses.length >= 2) {
						dao.setDeployState(deployStatuses[1]);
						extraMessagesPrefix += deployStatuses[1] + ";";
					}
				} else {
					extraMessagesPrefix = deployStatuses[0] + ";";
					dao.setDeployState(deployStatuses[0]);
					dao.setException(false);
				}
			}
			//if there are extra messages
			if (extraMessagesPrefix != "") {
				dao.setExtraMessages(new String(getDeployStatus()).replace(extraMessagesPrefix, "").split(";"));
			}
		}

		return dao;
	}

	/**
	 * Exception must have this form Exception;LastState;errorMessage
	 */
	@Override
	public void setExceptionMessage(String errorMessage) {
		String deployStatus = new String(getDeployStatus());
		String[] deployStatuses = getDeployStatus() != null ? deployStatus.split(";") : null;
		String newDeployStatus = null;
		//contains old exception 
		if (deployStatuses != null && deployStatuses.length >= 1 && deployStatuses[0].contains(LiveserverDeployStatus.Exception.name())) {
			newDeployStatus = LiveserverDeployStatus.Exception.name();
			if (deployStatuses.length >= 2) {
				newDeployStatus += ";" + deployStatuses[1];
			}
			newDeployStatus += ";" + errorMessage;
		} else {
			newDeployStatus = LiveserverDeployStatus.Exception.name();
			if (deployStatuses != null && deployStatuses.length >= 1) {
				newDeployStatus += ";" + deployStatuses[0];
			}
			newDeployStatus += ";" + errorMessage;
		}
		setDeployStatus(newDeployStatus.getBytes());
	}

}
