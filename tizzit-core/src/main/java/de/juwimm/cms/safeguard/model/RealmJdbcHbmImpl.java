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

import de.juwimm.cms.safeguard.vo.RealmJdbcValue;
import de.juwimm.cms.util.ToXmlHelper;

/**
 * @see de.juwimm.cms.safeguard.model.RealmJdbcHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 *         company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class RealmJdbcHbmImpl extends RealmJdbcHbm {

	/**
	 * The serial version UID of this class. Needed for serialization.
	 */
	private static final long serialVersionUID = 8995923002238698309L;

	/**
	 * @see de.juwimm.cms.safeguard.model.RealmJdbcHbm#getRealmJdbcValue()
	 */
	@Override
	public RealmJdbcValue getRealmJdbcValue() {
		RealmJdbcValue value = new RealmJdbcValue();
		value.setJndiName(this.getJndiName());
		value.setJdbcRealmId(this.getJdbcRealmId());
		value.setStatementRolePerUser(this.getStatementRolePerUser());
		value.setStatementUser(this.getStatementUser());
		value.setLoginPageId(this.getLoginPageId());
		value.setRealmName(this.getRealmName());

		return value;
	}

	/**
	 * @see de.juwimm.cms.safeguard.model.RealmJdbcHbm#setRealmJdbcValue(de.juwimm.cms.safeguard.vo.RealmJdbcValue)
	 */
	@Override
	public void setRealmJdbcValue(RealmJdbcValue realmJdbcValue) {
		this.setJndiName(realmJdbcValue.getJndiName());
		this.setRealmName(realmJdbcValue.getRealmName());
		this.setStatementRolePerUser(realmJdbcValue.getStatementRolePerUser());
		this.setStatementUser(realmJdbcValue.getStatementUser());
		this.setLoginPageId(realmJdbcValue.getLoginPageId());
	}

	/**
	 * @see de.juwimm.cms.safeguard.model.RealmJdbcHbm#toXml()
	 */
	@Override
	public String toXml() {
		ToXmlHelper helper = new ToXmlHelper();
		StringBuffer sb = new StringBuffer("<realmJdbc>");
		sb.append(helper.getXMLNode("jdbcRealmId", this.getJdbcRealmId().toString()));
		sb.append("<realmName><![CDATA[").append(this.getRealmName()).append("]]></realmName>\n");
		if (this.getLoginPageId() != null && this.getLoginPageId().length() > 0) sb.append(helper.getXMLNode("loginPageId", this.getLoginPageId()));
		sb.append(helper.getXMLNode("jndiName", this.getJndiName()));
		sb.append("<statementUser><![CDATA[").append(this.getStatementUser()).append("]]></statementUser>\n");
		sb.append("<statementRolePerUser><![CDATA[").append(this.getStatementRolePerUser()).append("]]></statementRolePerUser>\n");
		sb.append("</realmJdbc>");

		return sb.toString();
	}

}
