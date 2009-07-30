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

import de.juwimm.cms.components.vo.TalktimeValue;

/**
 * @see de.juwimm.cms.components.model.TalktimeHbm
 */
public class TalktimeHbmImpl extends de.juwimm.cms.components.model.TalktimeHbm {
	/**
	 * The serial version UID of this class. Needed for serialization.
	 */
	private static final long serialVersionUID = -32371519287784951L;

	/**
	 * @see de.juwimm.cms.components.model.TalktimeHbm#getData()
	 */
	public de.juwimm.cms.components.vo.TalktimeValue getData() {
		TalktimeValue dataHolder = null;
		dataHolder = new TalktimeValue();
		dataHolder.setTalkTimeId(getTalkTimeId());
		dataHolder.setTalkTimeType(getTalkTimeType());
		dataHolder.setTalkTimes(getTalkTimes());

		return dataHolder;
	}

	/**
	 * @see de.juwimm.cms.components.model.TalktimeHbm#update(de.juwimm.cms.components.vo.TalktimeValue)
	 */
	public void update(de.juwimm.cms.components.vo.TalktimeValue talktimeValue) {
		setTalkTimeType(talktimeValue.getTalkTimeType());
		setTalkTimes(talktimeValue.getTalkTimes());
	}

	/**
	 * @see de.juwimm.cms.components.model.TalktimeHbm#toXml(int)
	 */
	public java.lang.String toXml(int tabdepth) {
		StringBuffer sb = new StringBuffer();
		sb.append("<talktime id=\"").append(getTalkTimeId()).append("\">\n");
		sb.append("<talkTimes>").append(this.getValidField(getTalkTimes())).append("</talkTimes>\n");
		sb.append("<talkTimeType><![CDATA[").append(this.getValidField(getTalkTimeType())).append("]]></talkTimeType>\n");
		sb.append("</talktime>\n");
		return sb.toString();
	}

	private String getValidField(String field) {
		return (field == null) ? "" : field;
	}

}