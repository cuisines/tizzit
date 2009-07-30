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
package de.juwimm.cms.components.model;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Element;

import de.juwimm.cms.model.SequenceHbmDao;
import de.juwimm.util.XercesHelper;

/**
 * @see de.juwimm.cms.components.model.TalktimeHbm
 */
public class TalktimeHbmDaoImpl extends de.juwimm.cms.components.model.TalktimeHbmDaoBase {
	private static Logger log = Logger.getLogger(TalktimeHbmDaoImpl.class);
	
	@Autowired
	private SequenceHbmDao sequenceHbmDao;

	@Override
	public TalktimeHbm create(TalktimeHbm talktimeHbm) {
		Integer id = null;
		if (talktimeHbm.getTalkTimeId() == null) {
			try {
				id = sequenceHbmDao.getNextSequenceNumber("talktime.talk_time_id");
				talktimeHbm.setTalkTimeId(new Long(id));
			} catch (Exception e) {
				log.error("Error creating primary key", e);
			}
		}
		return super.create(talktimeHbm);
	}

	/**
	 * @see de.juwimm.cms.components.model.TalktimeHbmDao#create()
	 */
	protected java.lang.Long handleCreate() {
		TalktimeHbm talktime = TalktimeHbm.Factory.newInstance();
		return this.create(talktime).getTalkTimeId();
	}

	/**
	 * @see de.juwimm.cms.components.model.TalktimeHbmDao#create(java.lang.String,
	 *      java.lang.String)
	 */
	protected java.lang.Long handleCreate(java.lang.String talkTimeType, java.lang.String talkTimes) {
		TalktimeHbm talktime = TalktimeHbm.Factory.newInstance();
		talktime.setTalkTimes(talkTimes);
		talktime.setTalkTimeType(talkTimeType);
		return this.create(talktime).getTalkTimeId();
	}

	/**
	 * @see de.juwimm.cms.components.model.TalktimeHbmDao#create(org.w3c.dom.Element,
	 *      boolean, java.util.Map)
	 */
	protected java.lang.Long handleCreate(org.w3c.dom.Element ael, boolean useNewID, java.util.Map mappingTalktime) {
		TalktimeHbm talktime = TalktimeHbm.Factory.newInstance();
		if (useNewID) {
			try {
				Integer id = sequenceHbmDao.getNextSequenceNumber("talktime.talk_time_id");
				talktime.setTalkTimeId(new Long(id));
				mappingTalktime.put(new Long(ael.getAttribute("id")), talktime.getTalkTimeId());
			} catch (Exception e) {

			}
		} else {
			talktime.setTalkTimeId(new Long(ael.getAttribute("id")));
		}

		try {
			talktime.setTalkTimes(XercesHelper.nodeList2string(XercesHelper.findNode(ael, "./talkTimes").getChildNodes()));
		} catch (Exception exe) {
			log.error("An error occurred: " + exe.getMessage(), exe);
		}
		talktime.setTalkTimeType(getNVal(ael, "talkTimeType"));
		return talktime.getTalkTimeId();
	}

	private String getNVal(Element ael, String nodeName) {
		String tmp = XercesHelper.getNodeValue(ael, "./" + nodeName);
		if (tmp == null || tmp.equals("null") || tmp.equals("")) { return null; }
		return tmp;
	}

	@SuppressWarnings("unchecked")
	public java.util.Collection findByUnit(final int transform, final java.lang.Integer unitId) {
		return this.findByUnit(transform, "from de.juwimm.cms.components.model.TalktimeHbm t where t.unit.unitId = ?", unitId);
	}
}