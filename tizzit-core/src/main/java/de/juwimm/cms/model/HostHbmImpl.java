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

import de.juwimm.cms.vo.HostValue;

/**
 * @see de.juwimm.cms.model.HostHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 *         company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class HostHbmImpl extends HostHbm {
	/**
	 * The serial version UID of this class. Needed for serialization.
	 */
	private static final long serialVersionUID = 5790565437415130530L;

	/**
	 * @see de.juwimm.cms.model.HostHbm#getHostValue()
	 */
	public HostValue getHostValue() {
		HostValue value = new HostValue();
		value.setHostName(this.getHostName());
		value.setRedirectUrl(this.getRedirectUrl());
		if(this.getRedirectHostName() != null){
			value.setRedirectHostName(this.getRedirectHostName().getHostName());
		}
		if (this.getStartPage() != null) value.setStartPageId(this.getStartPage().getViewComponentId());
		if (this.getUnit() != null) value.setUnitId(this.getUnit().getUnitId());
		return value;
	}

	/**
	 * @see de.juwimm.cms.model.HostHbm#toXml(int)
	 */
	public String toXml(int tabdepth) {
		StringBuffer sb = new StringBuffer();
		sb.append("<host>\n");
		sb.append("<hostName><![CDATA[");
		sb.append(this.getHostName().trim());
		sb.append("]]></hostName>\n");
		if (this.getStartPage() == null) {
			sb.append("<startPageId/>\n");
		} else {
			sb.append("<startPageId>");
			sb.append(this.getStartPage().getViewComponentId());
			sb.append("</startPageId>\n");
		}
		if (this.getUnit() == null) {
			sb.append("<unitId/>\n");
		} else {
			sb.append("<unitId>");
			sb.append(this.getUnit().getUnitId());
			sb.append("</unitId>\n");
		}
		if (this.getRedirectHostName() == null) {
			sb.append("<redirectHostName/>\n");
		} else {
			sb.append("<redirectHostName><![CDATA[");
			sb.append(this.getRedirectHostName().getHostName());
			sb.append("]]></redirectHostName>\n");
		}
		if (this.getRedirectUrl() == null) {
			sb.append("<redirectUrl/>\n");
		} else {
			sb.append("<redirectUrl><![CDATA[");
			sb.append(this.getRedirectUrl());
			sb.append("]]></redirectUrl>\n");
		}
		sb.append("</host>\n");
		return sb.toString();
	}

}