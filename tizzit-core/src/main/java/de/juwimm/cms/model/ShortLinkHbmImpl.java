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

import de.juwimm.cms.vo.ShortLinkValue;

/**
 * @see de.juwimm.cms.model.ShortLinkHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 *         company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class ShortLinkHbmImpl extends ShortLinkHbm {

	/**
	 * The serial version UID of this class. Needed for serialization.
	 */
	private static final long serialVersionUID = 8183276639795495958L;

	/**
	 * @see de.juwimm.cms.model.ShortLinkHbm#getShortLinkValue()
	 */
	public ShortLinkValue getShortLinkValue() {
		ShortLinkValue value = new ShortLinkValue();
		value.setShortLinkId(this.getShortLinkId());
		value.setShortLink(this.getShortLink());
		value.setRedirectUrl(this.getRedirectUrl());
		if (this.getSite() != null) value.setSiteId(this.getSite().getSiteId());
		if (this.getViewDocument() != null) {
			value.setViewDocumentId(this.getViewDocument().getViewDocumentId());
		}
		return value;
	}

	/**
	 * @see de.juwimm.cms.model.ShortLinkHbm#toXml(int)
	 */
	public String toXml(int tabdepth) {
		StringBuffer sb = new StringBuffer();
		sb.append("<shortLink>\n");
		sb.append("<shortLinkId>").append(this.getShortLinkId().toString()).append("</shortLinkId>\n");
		sb.append("<viewDocumentId>").append(this.getViewDocument().getViewDocumentId().toString()).append("</viewDocumentId>\n");
		if (this.getShortLink() == null) {
			sb.append("<shortLinkName/>\n");
		} else {
			sb.append("<shortLinkName><![CDATA[");
			sb.append(this.getShortLink());
			sb.append("]]></shortLinkName>\n");
		}
		if (this.getRedirectUrl() == null) {
			sb.append("<redirectUrl/>\n");
		} else {
			sb.append("<redirectUrl><![CDATA[");
			sb.append(this.getRedirectUrl());
			sb.append("]]></redirectUrl>\n");
		}
		sb.append("</shortLink>\n");
		return sb.toString();
	}

}