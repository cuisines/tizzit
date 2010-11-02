package de.juwimm.cms.util;

import static de.juwimm.cms.common.Constants.rb;
import de.juwimm.cms.content.panel.PanLine;

/**
 * This Enum holds possible values for the Width Unit property.
 * 
 * <br/><br/><b>Usage:</b> {@link PanLine}
 * @author nnitu
 *
 */
public enum EnumWidthUnit {
	PERCENT(rb.getString("panel.content.panLine.comboPercent"), "%"), PIXEL(rb.getString("panel.content.panLine.comboPx"), "px");
	private String displayValue;
	private String xmlValue;

	private EnumWidthUnit(String outputValue, String xmlValue) {
		this.displayValue = outputValue;
		this.xmlValue = xmlValue;
	}

	public String getDisplayValue() {
		return displayValue;
	}

	public String getXmlValue() {
		return xmlValue;
	}

	@Override
	public String toString() {
		return displayValue;
	}

	public static EnumWidthUnit findByXmlValue(String xmlValue) {
		for (EnumWidthUnit enumAlign : EnumWidthUnit.values()) {
			if (enumAlign.getXmlValue().equals(xmlValue)) {
				return enumAlign;
			}
		}
		throw new RuntimeException("EnumAlign not found for input value of: '" + xmlValue + "'");
	}
}
