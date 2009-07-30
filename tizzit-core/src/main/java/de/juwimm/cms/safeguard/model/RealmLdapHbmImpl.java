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
package de.juwimm.cms.safeguard.model;

import de.juwimm.cms.safeguard.vo.RealmLdapValue;
import de.juwimm.cms.util.ToXmlHelper;

/**
 * @see de.juwimm.cms.safeguard.model.RealmLdapHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 *         company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class RealmLdapHbmImpl extends RealmLdapHbm {
	/**
	 * The serial version UID of this class. Needed for serialization.
	 */
	private static final long serialVersionUID = -2285174318033128743L;

	/**
	 * @see de.juwimm.cms.safeguard.model.RealmLdapHbm#getRealmLdapValue()
	 */
	@Override
	public RealmLdapValue getRealmLdapValue() {
		RealmLdapValue value = new RealmLdapValue();

		value.setLdapAuthenticationType(this.getLdapAuthenticationType());
		value.setLdapPrefix(this.getLdapPrefix());
		value.setLdapRealmId(this.getLdapRealmId());
		value.setLdapSuffix(this.getLdapSuffix());
		value.setLdapUrl(this.getLdapUrl());
		value.setRealmName(this.getRealmName());
		value.setLoginPageId(this.getLoginPageId());

		return value;
	}

	/**
	 * @see de.juwimm.cms.safeguard.model.RealmLdapHbm#setRealmLdapValue(de.juwimm.cms.safeguard.vo.RealmLdapValue)
	 */
	@Override
	public void setRealmLdapValue(RealmLdapValue realmLdapValue) {
		this.setLdapAuthenticationType(realmLdapValue.getLdapAuthenticationType());
		this.setRealmName(realmLdapValue.getRealmName());
		this.setLdapPrefix(realmLdapValue.getLdapPrefix());
		this.setLdapSuffix(realmLdapValue.getLdapSuffix());
		this.setLdapUrl(realmLdapValue.getLdapUrl());
		this.setLoginPageId(realmLdapValue.getLoginPageId());
	}

	/**
	 * @see de.juwimm.cms.safeguard.model.RealmLdapHbm#toXml()
	 */
	@Override
	public String toXml() {
		ToXmlHelper helper = new ToXmlHelper();
		StringBuffer sb = new StringBuffer("<realmLdap>");
		sb.append(helper.getXMLNode("ldapRealmId", this.getLdapRealmId().toString()));
		if (this.getLoginPageId() != null && this.getLoginPageId().length() > 0) sb.append(helper.getXMLNode("loginPageId", this.getLoginPageId()));
		sb.append("<realmName><![CDATA[").append(this.getRealmName()).append("]]></realmName>\n");
		sb.append(helper.getXMLNode("ldapPrefix", this.getLdapPrefix()));
		sb.append(helper.getXMLNode("ldapSuffix", this.getLdapSuffix()));
		sb.append(helper.getXMLNode("ldapUrl", this.getLdapUrl()));
		sb.append(helper.getXMLNode("ldapAuthenticationType", this.getLdapAuthenticationType()));
		sb.append("</realmLdap>");

		return sb.toString();
	}

}
