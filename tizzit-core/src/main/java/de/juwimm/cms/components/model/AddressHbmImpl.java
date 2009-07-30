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
package de.juwimm.cms.components.model;

import java.util.Date;

import org.apache.log4j.Logger;

import de.juwimm.cms.components.vo.AddressValue;

/**
 * @see de.juwimm.cms.components.model.AddressHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> ,
 *         Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class AddressHbmImpl extends de.juwimm.cms.components.model.AddressHbm {

	private static Logger log = Logger.getLogger(AddressHbmImpl.class);
	/**
	 * The serial version UID of this class. Needed for serialization.
	 */
	private static final long serialVersionUID = 6828838381487663943L;

	/**
	 * @see de.juwimm.cms.components.model.AddressHbm#getData()
	 */
	public de.juwimm.cms.components.vo.AddressValue getData() {
		AddressValue dataHolder = new AddressValue();
		dataHolder.setAddressId(getAddressId());
		dataHolder.setRoomNr(getRoomNr());
		dataHolder.setBuildingLevel(getBuildingLevel());
		dataHolder.setBuildingNr(getBuildingNr());
		dataHolder.setStreet(getStreet());
		dataHolder.setStreetNr(getStreetNr());
		dataHolder.setPostOfficeBox(getPostOfficeBox());
		dataHolder.setCountryCode(getCountryCode());
		dataHolder.setCountry(getCountry());
		dataHolder.setCity(getCity());
		dataHolder.setZipCode(getZipCode());
		dataHolder.setPhone1(getPhone1());
		dataHolder.setPhone2(getPhone2());
		dataHolder.setFax(getFax());
		dataHolder.setMobilePhone(getMobilePhone());
		dataHolder.setEmail(getEmail());
		dataHolder.setHomepage(getHomepage());
		dataHolder.setMisc(getMisc());
		dataHolder.setAddressType(getAddressType());
		dataHolder.setLastModifiedDate(this.getLastModifiedDate());
		dataHolder.setExternalId(this.getExternalId());
		return dataHolder;
	}

	/**
	 * @see de.juwimm.cms.components.model.AddressHbm#update(de.juwimm.cms.components.vo.AddressValue)
	 */
	public void update(de.juwimm.cms.components.vo.AddressValue value) {
		try {
			setRoomNr(value.getRoomNr());
			setBuildingLevel(value.getBuildingLevel());
			setBuildingNr(value.getBuildingNr());
			setStreet(value.getStreet());
			setStreetNr(value.getStreetNr());
			setPostOfficeBox(value.getPostOfficeBox());
			setCountryCode(value.getCountryCode());
			setCountry(value.getCountry());
			setCity(value.getCity());
			setZipCode(value.getZipCode());
			setPhone1(value.getPhone1());
			setPhone2(value.getPhone2());
			setFax(value.getFax());
			setMobilePhone(value.getMobilePhone());
			setEmail(value.getEmail());
			setHomepage(value.getHomepage());
			setMisc(value.getMisc());
			setAddressType(value.getAddressType());
			this.setExternalId(value.getExternalId());
			this.setLastModifiedDate(new Date().getTime());
		} catch (Exception e) {
			log.error("Could not update address value: ", e);
		}
	}

	/**
	 * @see de.juwimm.cms.components.model.AddressHbm#toXml(int)
	 */
	public java.lang.String toXml(int tabdepth) {
		StringBuffer sb = new StringBuffer();
		sb.append("<address id=\"").append(getAddressId()).append("\">\n");
		sb.append("<addressType><![CDATA[").append(this.getValidField(getAddressType())).append("]]></addressType>\n");
		sb.append("<buildingLevel><![CDATA[").append(this.getValidField(getBuildingLevel())).append("]]></buildingLevel>\n");
		sb.append("<buildingNr><![CDATA[").append(this.getValidField(getBuildingNr())).append("]]></buildingNr>\n");
		sb.append("<city><![CDATA[").append(this.getValidField(getCity())).append("]]></city>\n");
		sb.append("<country><![CDATA[").append(this.getValidField(getCountry())).append("]]></country>\n");
		sb.append("<countryCode><![CDATA[").append(this.getValidField(getCountryCode())).append("]]></countryCode>\n");
		sb.append("<email><![CDATA[").append(this.getValidField(getEmail())).append("]]></email>\n");
		sb.append("<fax><![CDATA[").append(this.getValidField(getFax())).append("]]></fax>\n");
		sb.append("<homepage><![CDATA[").append(this.getValidField(getHomepage())).append("]]></homepage>\n");
		sb.append("<misc><![CDATA[").append(this.getValidField(getMisc())).append("]]></misc>\n");
		sb.append("<mobilePhone><![CDATA[").append(this.getValidField(getMobilePhone())).append("]]></mobilePhone>\n");
		sb.append("<phone1><![CDATA[").append(this.getValidField(getPhone1())).append("]]></phone1>\n");
		sb.append("<phone2><![CDATA[").append(this.getValidField(getPhone2())).append("]]></phone2>\n");
		sb.append("<postOfficeBox><![CDATA[").append(this.getValidField(getPostOfficeBox())).append("]]></postOfficeBox>\n");
		sb.append("<roomNr><![CDATA[").append(this.getValidField(getRoomNr())).append("]]></roomNr>\n");
		sb.append("<street><![CDATA[").append(this.getValidField(getStreet())).append("]]></street>\n");
		sb.append("<streetNr><![CDATA[").append(this.getValidField(getStreetNr())).append("]]></streetNr>\n");
		sb.append("<zipCode><![CDATA[").append(this.getValidField(getZipCode())).append("]]></zipCode>\n");
		sb.append("<externalId><![CDATA[").append(this.getValidField(this.getExternalId())).append("]]></externalId>\n");
		sb.append("</address>\n");
		return sb.toString();
	}

	private String getValidField(String field) {
		return (field == null) ? "" : field;
	}

}