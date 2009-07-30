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

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import de.juwimm.cms.safeguard.model.RealmJaasHbm;
import de.juwimm.cms.safeguard.model.RealmJdbcHbm;
import de.juwimm.cms.safeguard.model.RealmLdapHbm;
import de.juwimm.cms.safeguard.model.RealmSimplePwHbm;
import de.juwimm.cms.util.ToXmlHelper;
import de.juwimm.cms.vo.SiteValue;

/**
 * @see de.juwimm.cms.model.SiteHbm
 */
public class SiteHbmImpl extends SiteHbm {

	/**
	 * The serial version UID of this class. Needed for serialization.
	 */
	private static final long serialVersionUID = 1185511211582526878L;

	/**
	 * @see de.juwimm.cms.model.SiteHbm#getSiteValue()
	 */
	public SiteValue getSiteValue() {
		SiteValue value = new SiteValue();
		value.setMandatorDir(this.getMandatorDir());
		value.setCacheExpire(this.getCacheExpire());
		value.setConfigXML(this.getConfigXML());
		value.setName(this.getName());
		value.setShortName(this.getShortName());
		value.setSiteId(this.getSiteId());
		value.setWysiwygImageUrl(this.getWysiwygImageUrl());
		value.setHelpUrl(this.getHelpUrl());
		value.setDcfUrl(this.getDcfUrl());
		value.setPreviewUrl(this.getPreviewUrl());
		value.setPageNameFull(this.getPageNameFull());
		value.setPageNameContent(this.getPageNameContent());
		value.setPageNameSearch(this.getPageNameSearch());
		value.setLastModifiedDate(this.getLastModifiedDate());
		return value;
	}

	/**
	 * @see de.juwimm.cms.model.SiteHbm#setSiteValue(de.juwimm.cms.vo.SiteValue)
	 */
	public void setSiteValue(SiteValue value) {
		this.setSiteId(value.getSiteId());
		this.setName(value.getName());
		this.setShortName(value.getShortName());
		this.setMandatorDir(value.getMandatorDir());
		this.setCacheExpire(value.getCacheExpire());
		this.setWysiwygImageUrl(value.getWysiwygImageUrl());
		this.setHelpUrl(value.getHelpUrl());
		this.setDcfUrl(value.getDcfUrl());
		this.setPreviewUrl(value.getPreviewUrl());
		this.setPageNameFull(value.getPageNameFull());
		this.setPageNameContent(value.getPageNameContent());
		this.setPageNameSearch(value.getPageNameSearch());
		this.setLastModifiedDate(new Date().getTime());
	}

	/**
	 * @see de.juwimm.cms.model.SiteHbm#toXML(int)
	 */
	public String toXML(int tabdepth) {
		ToXmlHelper helper = new ToXmlHelper();

		StringBuffer buf = new StringBuffer("<site>");
		buf.append(helper.getXMLNode("siteid", this.getSiteId().toString()));
		{
			Collection simplepwrealms = this.getRealmSimplePwHbms();
			Iterator it = simplepwrealms.iterator();
			buf.append("<realmsSimplePw>");
			while (it.hasNext()) {
				RealmSimplePwHbm realm = (RealmSimplePwHbm) it.next();
				buf.append(realm.toXml());
			}
			buf.append("</realmsSimplePw>");
		}
		{
			Collection sqldbrealms = this.getRealmJdbcHbms();
			Iterator it = sqldbrealms.iterator();
			buf.append("<realmsJdbc>");
			while (it.hasNext()) {
				RealmJdbcHbm realm = (RealmJdbcHbm) it.next();
				buf.append(realm.toXml());
			}
			buf.append("</realmsJdbc>");
		}
		{
			Collection realmsLdap = this.getRealmLdapHbms();
			Iterator it = realmsLdap.iterator();
			buf.append("<realmsLdap>");
			while (it.hasNext()) {
				RealmLdapHbm realm = (RealmLdapHbm) it.next();
				buf.append(realm.toXml());
			}
			buf.append("</realmsLdap>");
		}
		{
			Collection realmsJaas = this.getRealmJaasHbms();
			Iterator it = realmsJaas.iterator();
			buf.append("<realmsJaas>");
			while (it.hasNext()) {
				RealmJaasHbm realm = (RealmJaasHbm) it.next();
				buf.append(realm.toXml());
			}
			buf.append("</realmsJaas>");
		}
		{
			buf.append("<hosts>");
			Collection hostcollection = this.getHost();
			Iterator it = hostcollection.iterator();
			while (it.hasNext()) {
				HostHbm host = (HostHbm) it.next();
				buf.append(host.toXml(tabdepth));
			}
			buf.append("</hosts>");
		}
		buf.append("</site>");

		return buf.toString();
	}

}