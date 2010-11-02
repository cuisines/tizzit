package de.juwimm.cms.util;

import static de.juwimm.cms.common.Constants.rb;
import de.juwimm.cms.content.panel.PanLine;

/**
 * This Enum holds possible values for the Align property.
 * 
 * <br/><br/><b>Usage:</b> {@link PanLine}
 * @author nnitu
 *
 */
public enum EnumAlign {
	RIGTH(rb.getString("panel.content.panLine.comboAlign.right"), "right"), CENTER(rb.getString("panel.content.panLine.comboAlign.center"), "center"), LEFT(rb.getString("panel.content.panLine.comboAlign.left"), "left");
	private String displayValue;
	private String xmlValue;

	private EnumAlign(String outputValue, String xmlValue) {
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

	public static EnumAlign findByXmlValue(String xmlValue) {
		for (EnumAlign enumAlign : EnumAlign.values()) {
			if (enumAlign.getXmlValue().equals(xmlValue)) {
				return enumAlign;
			}
		}
		throw new RuntimeException("EnumAlign not found for input value of: '" + xmlValue + "'");
	}
}
