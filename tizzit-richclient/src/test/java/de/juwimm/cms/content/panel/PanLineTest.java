package de.juwimm.cms.content.panel;

import java.awt.Color;
import java.util.ResourceBundle;

import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import de.juwimm.cms.client.beans.Application;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.util.EnumAlign;
import de.juwimm.cms.util.EnumWidthUnit;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
//@PrepareForTest( {PanLine.class})
public class PanLineTest {

	PanLine panLine;

	@Before
	public void setUp() {
		Application.initializeContext();
		Constants.rb = ResourceBundle.getBundle("CMS", Constants.CMS_LOCALE);
		panLine = new PanLine();
	}

	@Test
	public void testGetAlign() {
		JComboBox comboBox = new JComboBox();
		for (EnumAlign enumAlign : EnumAlign.values()) {
			comboBox.addItem(enumAlign);
		}
		comboBox.setSelectedIndex(1);
		Whitebox.setInternalState(panLine, "comboBoxAlign", comboBox);
		String align = panLine.getAlign();
		String expectedAlign = EnumAlign.values()[1].getXmlValue();
		assertEquals(expectedAlign, align);
	}

	@Test
	public void testSetAlign() {
		JComboBox comboBox = new JComboBox();
		for (EnumAlign enumAlign : EnumAlign.values()) {
			comboBox.addItem(enumAlign);
		}
		comboBox.setSelectedIndex(0);
		Whitebox.setInternalState(panLine, "comboBoxAlign", comboBox);
		panLine.setAlign(EnumAlign.CENTER);
		String actual = ((EnumAlign) comboBox.getSelectedItem()).getXmlValue();
		String expectedAlign = EnumAlign.CENTER.getXmlValue();
		assertEquals(expectedAlign, actual);
		assertEquals(1, comboBox.getSelectedIndex());
	}

	@Test
	public void testGetThickness() {
		JTextField textField = new JTextField();
		textField.setText("5 ");
		Whitebox.setInternalState(panLine, "textFieldSize", textField);
		String actual = panLine.getThickness();
		String expected = "5";
		assertEquals(expected, actual);
	}

	@Test
	public void testSetThickness() {
		JTextField textField = new JTextField();
		textField.setText("5 ");
		Whitebox.setInternalState(panLine, "textFieldSize", textField);
		panLine.setThickness("7");
		String expected = "7";
		String actual = textField.getText();
		assertEquals(expected, actual);
	}

	@Test
	public void testGetCustomWidth() {
		JTextField textField = new JTextField();
		textField.setText("5 ");
		Whitebox.setInternalState(panLine, "textFieldWidth", textField);
		String actual = panLine.getCustomWidth();
		String expected = "5";
		assertEquals(expected, actual);
	}

	@Test
	public void testGetCustomWidthEmpty() {
		JTextField textField = new JTextField();
		textField.setText(" ");
		Whitebox.setInternalState(panLine, "textFieldWidth", textField);
		String actual = panLine.getCustomWidth();
		assertNull(actual);
	}

	@Test
	public void testSetCustomWidth() {
		JTextField textField = new JTextField();
		textField.setText("5 ");
		Whitebox.setInternalState(panLine, "textFieldWidth", textField);
		panLine.setCustomWidth("7");
		String expected = "7";
		String actual = textField.getText();
		assertEquals(expected, actual);
	}

	@Test
	public void testGetCustomWidthUnit() {
		JComboBox comboBox = new JComboBox();
		for (EnumWidthUnit enumWidthUnit : EnumWidthUnit.values()) {
			comboBox.addItem(enumWidthUnit);
		}
		comboBox.setSelectedIndex(1);
		Whitebox.setInternalState(panLine, "comboBoxWidthUnit", comboBox);
		String actual = panLine.getCustomWidthUnit();
		String expected = EnumWidthUnit.values()[1].getXmlValue();
		assertEquals(expected, actual);
	}

	@Test
	public void testSetCustomWidthUnit() {
		JComboBox comboBox = new JComboBox();
		for (EnumWidthUnit enumWidthUnit : EnumWidthUnit.values()) {
			comboBox.addItem(enumWidthUnit);
		}
		comboBox.setSelectedIndex(0);
		Whitebox.setInternalState(panLine, "comboBoxWidthUnit", comboBox);
		panLine.setCustomWidthUnit(EnumWidthUnit.PIXEL);
		String actual = ((EnumWidthUnit) comboBox.getSelectedItem()).getXmlValue();
		String expected = EnumWidthUnit.PIXEL.getXmlValue();
		assertEquals(expected, actual);
		assertEquals(1, comboBox.getSelectedIndex());
	}

	@Test
	public void testGetColor() {
		JColorChooser colorChooser = new JColorChooser();
		colorChooser.setColor(Color.BLACK);
		Whitebox.setInternalState(panLine, "colorChooser", colorChooser);
		String actual = panLine.getColor();
		String expected = "000000";
		assertEquals(expected, actual);
	}

	@Test
	public void testSetColor() {
		JColorChooser colorChooser = new JColorChooser();
		colorChooser.setColor(Color.BLACK);
		Whitebox.setInternalState(panLine, "colorChooser", colorChooser);
		panLine.setColor("FFFFFF");
		Color expected = Color.WHITE;
		Color actual = colorChooser.getColor();
		assertEquals(expected, actual);
	}

}
