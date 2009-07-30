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

import de.juwimm.cms.safeguard.vo.RealmJaasValue;
import de.juwimm.cms.util.ToXmlHelper;

/**
 * @see de.juwimm.cms.safeguard.model.RealmJaasHbm
 */
public class RealmJaasHbmImpl extends RealmJaasHbm {

	/**
	 * The serial version UID of this class. Needed for serialization.
	 */
	private static final long serialVersionUID = -2889297493891345598L;

	/**
	 * @see de.juwimm.cms.safeguard.model.RealmJaasHbm#getRealmJaasValue()
	 */
	@Override
	public RealmJaasValue getRealmJaasValue() {
		RealmJaasValue value = new RealmJaasValue();
		value.setJaasRealmId(this.getJaasRealmId());
		value.setRealmName(this.getRealmName());
		value.setLoginPageId(this.getLoginPageId());
		value.setJaasPolicyName(this.getJaasPolicyName());

		return value;
	}

	/**
	 * @see de.juwimm.cms.safeguard.model.RealmJaasHbm#setRealmJaasValue(de.juwimm.cms.safeguard.vo.RealmJaasValue)
	 */
	@Override
	public void setRealmJaasValue(RealmJaasValue realmJaasValue) {
		this.setRealmName(realmJaasValue.getRealmName());
		this.setLoginPageId(realmJaasValue.getLoginPageId());
		this.setJaasPolicyName(realmJaasValue.getJaasPolicyName());
	}

	/**
	 * @see de.juwimm.cms.safeguard.model.RealmJaasHbm#toXml()
	 */
	@Override
	public String toXml() {
		ToXmlHelper helper = new ToXmlHelper();
		StringBuffer sb = new StringBuffer("<realmJaas>");
		sb.append(helper.getXMLNode("jaasRealmId", this.getJaasRealmId().toString()));
		sb.append("<realmName><![CDATA[").append(this.getRealmName()).append("]]></realmName>\n");
		if (this.getLoginPageId() != null && this.getLoginPageId().length() > 0) sb.append(helper.getXMLNode("loginPageId", this.getLoginPageId()));
		sb.append("<jaasPolicyName><![CDATA[").append(this.getJaasPolicyName()).append("]]></jaasPolicyName>\n");
		sb.append("</realmJaas>");

		return sb.toString();
	}

}
