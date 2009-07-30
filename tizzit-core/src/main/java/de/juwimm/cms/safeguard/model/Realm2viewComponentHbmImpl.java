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

import de.juwimm.cms.util.ToXmlHelper;

/**
 * @see de.juwimm.cms.safeguard.model.Realm2viewComponentHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 *         company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id: Realm2viewComponentHbmImpl.java 26886 2008-04-30 10:25:41Z
 *          greivej $
 */
public class Realm2viewComponentHbmImpl extends Realm2viewComponentHbm {

	/**
	 * The serial version UID of this class. Needed for serialization.
	 */
	private static final long serialVersionUID = -1273524386202541807L;

	/**
	 * @see de.juwimm.cms.safeguard.model.Realm2viewComponentHbm#toXml()
	 */
	@Override
	public String toXml() {
		ToXmlHelper helper = new ToXmlHelper();

		StringBuffer sb = new StringBuffer("<realm2viewComponent>");
		sb.append(helper.getXMLNode("realm2viewComponentId", this.getRealm2viewComponentId().toString()));
		if (this.getRoleNeeded() != null) {
			sb.append("<roleNeeded><![CDATA[").append(this.getRoleNeeded()).append("]]></roleNeeded>\n");
		} else {
			sb.append("<roleNeeded/>\n");
		}
		if (this.getLoginPage() != null) sb.append(helper.getXMLNode("loginPageId", this.getLoginPage().getViewComponentId().toString()));

		RealmSimplePwHbm simplerealm = this.getSimplePwRealm();
		if (simplerealm != null) {
			Integer id = simplerealm.getSimplePwRealmId();
			sb.append(helper.getXMLNode("simplePwRealmId", id.toString()));
		} else {
			RealmJdbcHbm sqlrealm = this.getJdbcRealm();
			if (sqlrealm != null) {
				Integer id = sqlrealm.getJdbcRealmId();
				sb.append(helper.getXMLNode("jdbcRealmId", id.toString()));
			} else {
				RealmLdapHbm ldapRealm = this.getLdapRealm();
				if (ldapRealm != null) {
					Integer id = ldapRealm.getLdapRealmId();
					sb.append(helper.getXMLNode("ldapRealmId", id.toString()));
				} else {
					RealmJaasHbm jaasRealm = this.getJaasRealm();
					if (jaasRealm != null) {
						Integer id = jaasRealm.getJaasRealmId();
						sb.append(helper.getXMLNode("jaasRealmId", id.toString()));
					}
				}
			}
		}
		sb.append("</realm2viewComponent>");

		return sb.toString();
	}

}
