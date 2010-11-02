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
package de.juwimm.cms.content.panel;

import static de.juwimm.cms.client.beans.Application.getBean;
import static de.juwimm.cms.common.Constants.rb;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.content.modules.Line;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.EnumAlign;
import de.juwimm.cms.util.EnumWidthUnit;

/**
 * This class defines the panel for all cobnfiguration needed for a {@link Line} object in the WYSIWYG editor.
 * 
 * @author <a href="mailto:nitun@juwimm.com">Nicolaie Nitu </a>
 * @version $Id: PanLine.java$
 */
public class PanLine extends JPanel {
	private static final long serialVersionUID = -3349397781166994441L;
	private static Logger log = Logger.getLogger(PanLine.class);
	protected Communication comm = ((Communication) getBean(Beans.COMMUNICATION));

	private JLabel labelAlign = new JLabel(rb.getString("panel.content.panLine.labelAlign"));
	private JLabel labelSize = new JLabel(rb.getString("panel.content.panLine.labelSize"));
	private JLabel labelWidth = new JLabel(rb.getString("panel.content.panLine.labelWidth"));
	private JLabel labelColor = new JLabel(rb.getString("panel.content.panLine.labelColor"));
	private JLabel labelPx = new JLabel(rb.getString("panel.content.panLine.labelPx"));
	private JComboBox comboBoxAlign = new JComboBox();
	private JComboBox comboBoxWidthUnit = new JComboBox();
	private JTextField textFieldSize = new JTextField();
	private JTextField textFieldWidth = new JTextField();
	private JLabel labelColourPreview = new JLabel("  ");
	private JColorChooser colorChooser = new JColorChooser();

	private Color selectedColor;

	public PanLine() {
		for (EnumAlign enumAlign : EnumAlign.values()) {
			comboBoxAlign.addItem(enumAlign);
		}
		for (EnumWidthUnit enumWidthUnit : EnumWidthUnit.values()) {
			comboBoxWidthUnit.addItem(enumWidthUnit);
		}
		labelColourPreview.setOpaque(true);
		colorChooser.getChooserPanels()[0] = null;
		colorChooser.getChooserPanels()[2] = null;
		colorChooser.setPreviewPanel(labelColourPreview);
		colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				selectedColor = colorChooser.getColor();
				labelColourPreview.setBackground(selectedColor);
			}
		});
		this.setLayout(new GridBagLayout());
		this.add(labelAlign, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(17, 15, 0, 13), 0, 0));
		this.add(comboBoxAlign, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(17, 15, 0, 13), 0, 0));
		this.add(labelSize, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(17, 15, 0, 13), 0, 0));
		this.add(textFieldSize, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(17, 15, 0, 13), 0, 0));
		this.add(labelPx, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(17, 15, 0, 13), 0, 0));
		this.add(labelWidth, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(17, 15, 0, 13), 0, 0));
		this.add(textFieldWidth, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(17, 15, 0, 13), 0, 0));
		this.add(comboBoxWidthUnit, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(17, 15, 0, 13), 0, 0));
		this.add(labelColor, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(17, 15, 0, 13), 0, 0));
		this.add(labelColourPreview, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(17, 15, 0, 13), 0, 0));
		this.add(colorChooser, new GridBagConstraints(0, 4, 3, 1, 2.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(17, 15, 0, 13), 0, 0));
	}

	/**
	 * Public method used to retrieve the <b>align</b> property from the configuration panel.
	 * @return the align selected in the comboBox (One of: "right","center","left")
	 */
	public String getAlign() {
		EnumAlign enumAlign = (EnumAlign) comboBoxAlign.getSelectedItem();
		return enumAlign.getXmlValue();
	}

	/**
	 * Public method used to retrieve the <b>thickness</b> property from the configuration panel.
	 * @return the thickness inserted in the textField (Format: "<i>value</i> px", where value is the value inserted in the textField)
	 */
	public String getThickness() {
		return textFieldSize.getText().trim();
	}

	/**
	 * Public method used to retrieve the <b>width</b> property from the configuration panel.
	 * @return the width of the Line
	 */
	public String getCustomWidth() {
		String value = textFieldWidth.getText().trim();
		if (value == null || value.length() == 0) {
			return null;
		}
		return value;
	}

	/**
	 * Public method used to retrieve the <b>width unit</b> property from the configuration panel.
	 * @return the width unit of the Line (One of: "px", "%")
	 */
	public String getCustomWidthUnit() {
		EnumWidthUnit enumWidthUnit = (EnumWidthUnit) comboBoxWidthUnit.getSelectedItem();
		return enumWidthUnit.getXmlValue();
	}

	/**
	 * Public method used to retrieve the <b>color</b> property from the configuration panel.
	 * @return the color of the Line(Format: "<i>value</i>", where value is the RGB value calculated from the color selected in the colorChooser)
	 */
	public String getColor() {
		Color color = colorChooser.getColor();
		String red = Integer.toHexString(color.getRed());
		if(red.length()==1)red="0"+red;
		String green = Integer.toHexString(color.getGreen());
		if(green.length()==1)green="0"+green;
		String blue = Integer.toHexString(color.getBlue());
		if(blue.length()==1)blue="0"+blue;
		String rgbHexValue = red + green + blue;
		return rgbHexValue;
	}

	/**
	 * See {@link PanLine.getAlign()}
	 */
	public void setAlign(EnumAlign enumAlign) {
		comboBoxAlign.setSelectedItem(enumAlign);
	}

	/**
	 * See {@link PanLine.getThickness()}
	 */
	public void setThickness(String thickness) {
		textFieldSize.setText(thickness);
	}

	/**
	 * See {@link PanLine.getWidth()}
	 */
	public void setCustomWidth(String width) {
		textFieldWidth.setText(width);
	}

	/**
	 * See {@link PanLine.getWidthUnit()}
	 */
	public void setCustomWidthUnit(EnumWidthUnit enumWidthUnit) {
		comboBoxWidthUnit.setSelectedItem(enumWidthUnit);
	}

	/**
	 * See {@link PanLine.getColor()}
	 */
	public void setColor(String hexValue) {
		if (hexValue.length() != 6) {
			throw new RuntimeException("Invalid hex color for panel");
		}
		Integer red = Integer.parseInt(hexValue.substring(0, 2),16);
		Integer green = Integer.parseInt(hexValue.substring(2, 4),16);
		Integer blue = Integer.parseInt(hexValue.substring(4, 6),16);
		Color color = new Color(red, green, blue);

		colorChooser.setColor(color);
	}
}