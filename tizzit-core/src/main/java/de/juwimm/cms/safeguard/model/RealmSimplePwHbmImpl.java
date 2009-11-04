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

import java.util.Collection;
import java.util.Iterator;

import de.juwimm.cms.safeguard.vo.RealmSimplePwValue;
import de.juwimm.cms.util.ToXmlHelper;

/**
 * @see de.juwimm.cms.safeguard.model.RealmSimplePwHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 *         company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class RealmSimplePwHbmImpl extends RealmSimplePwHbm {

	/**
	 * The serial version UID of this class. Needed for serialization.
	 */
	private static final long serialVersionUID = 5940540296006699961L;

	/**
	 * @see de.juwimm.cms.safeguard.model.RealmSimplePwHbm#getRealmSimplePwValue()
	 */
	@Override
	public RealmSimplePwValue getRealmSimplePwValue() {
		RealmSimplePwValue value = new RealmSimplePwValue();
		value.setRealmName(this.getRealmName());
		value.setSimplePwRealmId(this.getSimplePwRealmId());
		value.setLoginPageId(this.getLoginPageId());
		return value;
	}

	@Override
	public void setRealmSimplePwValue(RealmSimplePwValue realmSimplePwValue) {
		this.setRealmName(realmSimplePwValue.getRealmName());
		this.setLoginPageId(realmSimplePwValue.getLoginPageId());
	}

	/**
	 * @see de.juwimm.cms.safeguard.model.RealmSimplePwHbm#toXml()
	 */
	@Override
	public String toXml() {
		ToXmlHelper helper = new ToXmlHelper();
		StringBuffer sb = new StringBuffer("<realmSimplePw>");
		sb.append(helper.getXMLNode("simplePwRealmId", this.getSimplePwRealmId().toString()));
		sb.append("<realmName><![CDATA[").append(this.getRealmName()).append("]]></realmName>\n");
		if (this.getLoginPageId() != null && this.getLoginPageId().length() > 0) sb.append(helper.getXMLNode("loginPageId", this.getLoginPageId()));
		//String ownerId = this.getOwner() != null ? this.getOwner().getUserId() : "";
		//sb.append(helper.getXMLNode("ownerId", ownerId));
		{
			sb.append("<simplePwRealmUsers>");
			Collection realmUserList = this.getSimplePwRealmUsers();
			Iterator it = realmUserList.iterator();
			while (it.hasNext()) {
				RealmSimplePwUserHbm simpleuser = (RealmSimplePwUserHbm) it.next();
				sb.append(simpleuser.toXml());
			}
			sb.append("</simplePwRealmUsers>");
		}
		sb.append("</realmSimplePw>");

		return sb.toString();
	}

}
