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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import de.juwimm.cms.safeguard.vo.RealmSimplePwUserValue;
import de.juwimm.cms.util.ToXmlHelper;

/**
 * @see de.juwimm.cms.safeguard.model.RealmSimplePwUserHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 *         company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class RealmSimplePwUserHbmImpl extends RealmSimplePwUserHbm {
	private static Logger log = Logger.getLogger(RealmSimplePwUserHbmImpl.class);

	/**
	 * The serial version UID of this class. Needed for serialization.
	 */
	private static final long serialVersionUID = -253410637462103491L;

	/**
	 * @see de.juwimm.cms.safeguard.model.RealmSimplePwUserHbm#getRealmSimplePwUserValue()
	 */
	@Override
	public RealmSimplePwUserValue getRealmSimplePwUserValue() {
		RealmSimplePwUserValue value = new RealmSimplePwUserValue();
		value.setPassword(this.getPassword());
		value.setSimplePwRealmUserId(this.getSimplePwRealmUserId());
		value.setUserName(this.getUserName());
		value.setRoles(this.getRolesAsString(this.getRolesAsStringArray(this.getRoles())));
		return value;
	}

	/**
	 * @see de.juwimm.cms.safeguard.model.RealmSimplePwUserHbm#toXml()
	 */
	@Override
	public String toXml() {
		ToXmlHelper helper = new ToXmlHelper();
		StringBuffer buf = new StringBuffer("<realmSimplePwUser>");
		buf.append(helper.getXMLNode("userId", this.getSimplePwRealmUserId().toString()));
		buf.append("<userName><![CDATA[").append(this.getUserName()).append("]]></userName>\n");
		buf.append("<password><![CDATA[").append(this.getPassword()).append("]]></password>\n");
		if (this.getRoles() != null) {
			buf.append("<roles><![CDATA[").append(this.getRoles()).append("]]></roles>\n");
		} else {
			buf.append("<roles/>\n");
		}
		buf.append("</realmSimplePwUser>");

		return buf.toString();
	}

	private String[] getRolesAsStringArray(String roles) {
		String[] rolesArray = null;
		try {
			if (roles != null && roles.length() > 0) {
				ArrayList<String> rolesList = new ArrayList<String>();
				StringTokenizer st = new StringTokenizer(roles, ",");
				while (st.hasMoreTokens()) {
					String role = st.nextToken();
					if (role.length() > 0) rolesList.add(role.trim().toLowerCase());
				}
				rolesArray = rolesList.toArray(new String[0]);
			}
		} catch (Exception e) {
			log.error("Error converting roles to String[]: " + e.getMessage(), e);
		}
		return rolesArray;
	}

	private String getRolesAsString(String[] rolesArray) {
		String roles = "";
		try {
			if (rolesArray != null && rolesArray.length > 0) {
				TreeSet rolesSet = new TreeSet<String>();
				for (int i = (rolesArray.length - 1); i >= 0; i--) {
					String role = rolesArray[i];
					if (role.length() > 0) rolesSet.add(role.trim().toLowerCase());
				}
				StringBuffer sb = new StringBuffer();
				Iterator<String> it = rolesSet.iterator();
				while (it.hasNext()) {
					String role = it.next();
					if (sb.length() > 0) sb.append(",");
					sb.append(role);
				}
				roles = sb.toString();
			}
		} catch (Exception e) {
			log.error("Error converting rolesArray to String: " + e.getMessage(), e);
		}
		return roles;
	}

}
