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

import java.util.Date;

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
	@Override
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
		value.setPreviewUrlWorkServer(this.getPreviewUrlWorkServer());
		value.setPreviewUrlLiveServer(this.getPreviewUrlLiveServer());
		value.setPageNameFull(this.getPageNameFull());
		value.setPageNameContent(this.getPageNameContent());
		value.setPageNameSearch(this.getPageNameSearch());
		value.setLastModifiedDate(this.getLastModifiedDate());
		value.setExternalSiteSearch(this.getExternalSiteSearch() == null ? false : this.getExternalSiteSearch());
		return value;
	}

	/**
	 * @see de.juwimm.cms.model.SiteHbm#setSiteValue(de.juwimm.cms.vo.SiteValue)
	 */
	@Override
	public void setSiteValue(SiteValue value) {
		this.setSiteId(value.getSiteId());
		this.setName(value.getName());
		this.setShortName(value.getShortName());
		this.setMandatorDir(value.getMandatorDir());
		this.setCacheExpire(value.getCacheExpire());
		this.setWysiwygImageUrl(value.getWysiwygImageUrl());
		this.setHelpUrl(value.getHelpUrl());
		this.setDcfUrl(value.getDcfUrl());
		this.setPreviewUrlWorkServer(value.getPreviewUrlWorkServer());
		this.setPreviewUrlLiveServer(value.getPreviewUrlLiveServer());
		this.setPageNameFull(value.getPageNameFull());
		this.setPageNameContent(value.getPageNameContent());
		this.setPageNameSearch(value.getPageNameSearch());
		this.setLastModifiedDate(new Date().getTime());
		this.setExternalSiteSearch(value.isExternalSiteSearch());
	}

	/**
	 * @see de.juwimm.cms.model.SiteHbm#toXML(int)
	 */
	/*	public String toXML(int tabdepth) {
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
		}*/

	@Override
	public String toXML(int tabdepth) {
		StringBuffer buf = new StringBuffer();
		buf.append("<site>");
		buf.append("<id>").append(this.getSiteId()).append("</id>");
		buf.append("<name>").append("<![CDATA[").append(this.getName()).append("]]></name>");
		buf.append("<shortName>").append("<![CDATA[").append(this.getShortName()).append("]]></shortName>");
		buf.append("<siteConfig>").append("<![CDATA[").append(this.getConfigXML()).append("]]>").append("</siteConfig>");
		buf.append("<mandatorDir>").append("<![CDATA[").append(this.getMandatorDir()).append("]]>").append("</mandatorDir>");
		buf.append("<rootUnitId>");
		if (this.getRootUnit() != null) {
			buf.append(this.getRootUnit().getUnitId());
		}
		buf.append("</rootUnitId>");
		buf.append("<defaultViewDocumentId>");
		if (this.getDefaultViewDocument() != null) {
			buf.append(this.getDefaultViewDocument().getViewDocumentId());
		}
		buf.append("</defaultViewDocumentId>");
		buf.append("<cacheExpire>").append("<![CDATA[").append(this.getCacheExpire()).append("]]>").append("</cacheExpire>");
		buf.append("<siteGroupId>");
		if (this.getSiteGroup() != null) {
			buf.append(this.getSiteGroup().getSiteGroupId());
		}
		buf.append("</siteGroupId>");
		buf.append("<wysiwygImageUrl>").append("<![CDATA[").append(this.getWysiwygImageUrl()).append("]]>").append("</wysiwygImageUrl>");
		buf.append("<helpUrl>").append("<![CDATA[").append(this.getHelpUrl()).append("]]>").append("</helpUrl>");
		buf.append("<dcfUrl>").append("<![CDATA[").append(this.getDcfUrl()).append("]]>").append("</dcfUrl>");
		buf.append("<previewUrlWorkServer>").append("<![CDATA[").append(this.getPreviewUrlWorkServer()).append("]]>").append("</previewUrlWorkServer>");
		buf.append("<previewUrlLiveServer>").append("<![CDATA[").append(this.getPreviewUrlLiveServer()).append("]]>").append("</previewUrlLiveServer>");
		buf.append("<pageNameFull>").append("<![CDATA[").append(this.getPageNameFull()).append("]]>").append("</pageNameFull>");
		buf.append("<pageNameContent>").append("<![CDATA[").append(this.getPageNameContent()).append("]]>").append("</pageNameContent>");
		buf.append("<pageNameSearch>").append("<![CDATA[").append(this.getPageNameSearch()).append("]]>").append("</pageNameSearch>");
		buf.append("<lastModifiedDate>").append(this.getLastModifiedDate()).append("</lastModifiedDate>");

		buf.append("</site>").append("\n");
		return buf.toString();
	}
}