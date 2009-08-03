/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
*	Tiny Look and Feel                                                         *
*                                                                              *
*  (C) Copyright 2003 - 2007 Hans Bickel                                       *
*                                                                              *
*   For licensing information and credits, please refer to the                 *
*   comment in file de.muntjak.tinylookandfeel.TinyLookAndFeel                 *
*                                                                              *
* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package de.muntjak.tinylookandfeel;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

import de.muntjak.tinylookandfeel.controlpanel.*;

/**
 * TinyDefaultTheme
 *
 * @version 1.1
 * @author Hans Bickel
 */
public class TinyDefaultTheme extends DefaultMetalTheme {

	/**
		* Secondary Color 3, used for the following:
		*  Canvas color (that is, normal background color), inactive title bar.
		*  Background for noneditable text fields.
		*/
	public static ColorUIResource secondary3 = Theme.backColor[Theme.style].getColor();
	
	private final ColorUIResource lightBackground = new ColorUIResource(252, 252, 254);

	public static final ColorUIResource darkControl = new ColorUIResource(161, 161, 148);

	/**
	 * Primary Color 1, used for the following:
	 *  Active internal window borders.
	 *  Shadows of activated items.
	 *  System text (for example, labels).
	 */
	private final ColorUIResource primary1 = new ColorUIResource(0, 0, 0);

	/**
	 * Primary Color 2, used for the following:
	 *  Highlighting to indicate activation (for example, of menu titles and menu
	 *  items); indication of keyboard focus.
	 *  Shadows (color).
	 *  Scrollbars.
	 */
	private final ColorUIResource primary2 = new ColorUIResource(213, 211, 209);

	/**
	 * Primary Color 3, used for the following:
	 *  Large colored areas (for example, the active title bar).
	 *  Text selection.
	 *  Tooltips background.
	 *  InternalFrame TitleBar.
	 */
	private final ColorUIResource primary3 = new ColorUIResource(213, 211, 209);

	/**
	 * Secondary Color 1, used for the following:
	 *  Dark border for flush 3D style.
	 */
	private final ColorUIResource secondary1 = new ColorUIResource(167, 165, 163);

	/**
	 * Secondary Color 2, used for the following:
	 *  Inactive internal window borders; dimmed button borders.
	 *  Shadows; highlighting of toolbar buttons upon mouse button down.
	 *  Dimmed text (for example, inactive menu items or labels).
	 */
	private final ColorUIResource secondary2 = new ColorUIResource(167, 165, 163);

	/**
	 * The background color of a pressed button.
	 */
	private final ColorUIResource secondary4 = new ColorUIResource(190, 188, 186);

	/**
	 * The upper gradient color for components like JButton, JMenuBar and JProgressBar.
	 */
	private final Color gradientReflection = new Color(255, 255, 255, 86);

	/**
	 * The lower gradient color for components like JButton, JMenuBar and
	 * JProgressBar.
	 */
	private final Color gradientShadow = new Color(188, 186, 184, 100);

	/**
	 * The transluscent variation of the upper gradient color for components
	 * like JButton, JMenuBar and JProgressBar.
	 */
	private final Color gradientTranslucentReflection = new Color(gradientReflection.getRGB() & 0x00FFFFFF, true);

	/**
	 * The transluscent variation of the lower gradient color for components
	 * like JButton, JMenuBar and JProgressBar.
	 */
	private final Color gradientTranslucentShadow = new Color(gradientShadow.getRGB() & 0x00FFFFFF, true);

	/**
	 * Gets the upper gradient color for components like JButton, JMenuBar and
	 * JProgressBar.
	 *
	 * @return The gradient reflection color.
	 */
	public Color getGradientReflection() {
		return gradientReflection;
	}

	/**
	 * Gets the lower gradient color for components like JButton, JMenuBar and
	 * JProgressBar.
	 *
	 * @return The gradient shadow color.
	 */
	public Color getGradientShadow() {
		return gradientShadow;
	}

	/**
	 * Gets the transluscent variation of the upper gradient color for components
	 * like JButton, JMenuBar and JProgressBar.
	 *
	 * @return The transluscent gradient reflection color.
	 */
	public Color getGradientTranslucentReflection() {
		return gradientTranslucentReflection;
	}

	/**
	 * Gets the transluscent variation of the lower gradient color for components
	 * like JButton, JMenuBar and JProgressBar.
	 *
	 * @return The transluscent gradient shadow color.
	 */
	public Color getGradientTranslucentShadow() {
		return gradientTranslucentShadow;
	}

	/**
	 * Gets the Font of Labels in many cases.
	 *
	 * @return The Font of Labels in many cases.
	 */
	public FontUIResource getControlTextFont() {
		return Theme.plainFont[Theme.style].getFont();
	}

	/**
	 * Gets the Font of Menus and MenuItems.
	 *
	 * @return The Font of Menus and MenuItems.
	 */
	public FontUIResource getMenuTextFont() {
		return Theme.plainFont[Theme.style].getFont();
	}

	/**
	 * Gets the Font of Nodes in JTrees.
	 *
	 * @return The Font of Nodes in JTrees.
	 */
	public FontUIResource getSystemTextFont() {
		return Theme.plainFont[Theme.style].getFont();
	}

	/**
	 * Gets the Font in TextFields, EditorPanes, etc.
	 *
	 * @return The Font in TextFields, EditorPanes, etc.
	 */
	public FontUIResource getUserTextFont() {
		return Theme.plainFont[Theme.style].getFont();
	}

	/**
	 * Gets the Font of the Title of JInternalFrames.
	 *
	 * @return The Font of the Title of JInternalFrames.
	 */
	public FontUIResource getWindowTitleFont() {
		return Theme.boldFont[Theme.style].getFont();
	}

	/**
	 * Adds some custom values to the defaults table.
	 *
	 * @param table The UI defaults table.
	 */
	public void addCustomEntriesToTable(UIDefaults table) {
		super.addCustomEntriesToTable(table);
		
		table.put("Button.margin", new InsetsUIResource(
			Theme.buttonMarginTop[Theme.style],
			Theme.buttonMarginLeft[Theme.style],
			Theme.buttonMarginBottom[Theme.style],
			Theme.buttonMarginRight[Theme.style]));
		table.put("CheckBox.margin", new InsetsUIResource(
			Theme.checkMarginTop[Theme.style],
			Theme.checkMarginLeft[Theme.style],
			Theme.checkMarginBottom[Theme.style],
			Theme.checkMarginRight[Theme.style]));
		table.put("RadioButton.margin", new InsetsUIResource(
			Theme.checkMarginTop[Theme.style],
			Theme.checkMarginLeft[Theme.style],
			Theme.checkMarginBottom[Theme.style],
			Theme.checkMarginRight[Theme.style]));
		table.put("Button.background", Theme.buttonNormalColor[Theme.style].getColor());
		table.put("Button.font", Theme.buttonFont[Theme.style].getFont());
		table.put("CheckBox.font", Theme.checkFont[Theme.style].getFont());
		table.put("CheckBoxMenuItem.font", Theme.menuItemFont[Theme.style].getFont());
		table.put("ComboBox.font", Theme.comboFont[Theme.style].getFont());
		table.put("Label.font", Theme.labelFont[Theme.style].getFont());
		table.put("List.font", Theme.listFont[Theme.style].getFont());
		table.put("Menu.font", Theme.menuFont[Theme.style].getFont());
		table.put("MenuItem.font", Theme.menuItemFont[Theme.style].getFont());
		table.put("ProgressBar.font", Theme.progressBarFont[Theme.style].getFont());
		table.put("RadioButton.font", Theme.radioFont[Theme.style].getFont());
		table.put("RadioButtonMenuItem.font", Theme.menuItemFont[Theme.style].getFont());
		table.put("Table.font", Theme.tableFont[Theme.style].getFont());
		table.put("TableHeader.font", Theme.tableHeaderFont[Theme.style].getFont());
		table.put("TitledBorder.font", Theme.titledBorderFont[Theme.style].getFont());
		table.put("ToolTip.font", Theme.toolTipFont[Theme.style].getFont());
		table.put("Tree.font", Theme.treeFont[Theme.style].getFont());
		table.put("PasswordField.font", Theme.passwordFont[Theme.style].getFont());
		table.put("TextArea.font", Theme.textAreaFont[Theme.style].getFont());
		table.put("TextField.font", Theme.textFieldFont[Theme.style].getFont());
		table.put("FormattedTextField.font", Theme.textFieldFont[Theme.style].getFont());
		table.put("TextPane.font", Theme.textPaneFont[Theme.style].getFont());
		table.put("EditorPane.font", Theme.editorFont[Theme.style].getFont());
		table.put("InternalFrame.font", Theme.editorFont[Theme.style].getFont());
		// font for internal frames and palettes
		table.put("InternalFrame.normalTitleFont", Theme.internalFrameTitleFont[Theme.style].getFont());
		table.put("InternalFrame.paletteTitleFont", Theme.internalPaletteTitleFont[Theme.style].getFont());
		// font for (decorized) frame
		table.put("InternalFrame.titleFont", Theme.frameTitleFont[Theme.style].getFont());
		
		table.put("TabbedPane.font", Theme.tabFont[Theme.style].getFont());

		table.put("Button.foreground", Theme.buttonFontColor[Theme.style].getColor());
		table.put("CheckBox.foreground", Theme.checkFontColor[Theme.style].getColor());
		table.put("Menu.foreground", Theme.menuFontColor[Theme.style].getColor());
		table.put("MenuItem.foreground", Theme.menuItemFontColor[Theme.style].getColor());
		table.put("CheckBoxMenuItem.foreground", Theme.menuItemFontColor[Theme.style].getColor());
		table.put("RadioButtonMenuItem.foreground", Theme.menuItemFontColor[Theme.style].getColor());
		table.put("RadioButton.foreground", Theme.radioFontColor[Theme.style].getColor());
		table.put("TabbedPane.foreground", Theme.tabFontColor[Theme.style].getColor());
		table.put("TitledBorder.titleColor", Theme.titledBorderFontColor[Theme.style].getColor());
		table.put("Label.foreground", Theme.labelFontColor[Theme.style].getColor());
		table.put("TableHeader.foreground", Theme.tableHeaderFontColor[Theme.style].getColor());
		table.put("TableHeader.background", Theme.tableHeaderBackColor[Theme.style].getColor());
		table.put("Table.foreground", Theme.tableFontColor[Theme.style].getColor());
		table.put("Table.background", Theme.tableBackColor[Theme.style].getColor());
		table.put("Table.selectionForeground", Theme.tableSelectedForeColor[Theme.style].getColor());
		table.put("Table.selectionBackground", Theme.tableSelectedBackColor[Theme.style].getColor());
		table.put("Table.gridColor", Theme.tableGridColor[Theme.style].getColor());
		table.put("ProgressBar.foreground", Theme.progressColor[Theme.style].getColor());
		table.put("ProgressBar.background", Theme.progressTrackColor[Theme.style].getColor());
		table.put("ProgressBar.selectionForeground", Theme.progressSelectForeColor[Theme.style].getColor());
		table.put("ProgressBar.selectionBackground", Theme.progressSelectBackColor[Theme.style].getColor());
		table.put("PopupMenu.background", Theme.menuPopupColor[Theme.style]);

		table.put("TabbedPane.background", Theme.tabNormalColor[Theme.style].getColor());
		table.put("TabbedPane.tabAreaInsets", Theme.tabAreaInsets[Theme.style]);
		table.put("TabbedPane.tabInsets", Theme.tabInsets[Theme.style]);

		table.put("MenuBar.background", Theme.menuBarColor[Theme.style].getColor());
		table.put("ToolBar.background", Theme.toolBarColor[Theme.style].getColor());

		table.put("EditorPane.caretForeground", Theme.textCaretColor[Theme.style].getColor());
		table.put("PasswordField.caretForeground", Theme.textCaretColor[Theme.style].getColor());
		table.put("TextArea.caretForeground", Theme.textCaretColor[Theme.style].getColor());
		table.put("TextField.caretForeground", Theme.textCaretColor[Theme.style].getColor());
		table.put("FormattedTextField.caretForeground", Theme.textCaretColor[Theme.style].getColor());

		table.put("List.foreground", Theme.listTextColor[Theme.style].getColor());
		table.put("List.background", Theme.listBgColor[Theme.style].getColor());
		table.put("ComboBox.foreground", Theme.comboTextColor[Theme.style].getColor());
		table.put("ComboBox.background", Theme.comboBgColor[Theme.style].getColor());
		table.put("ComboBox.disabledBackground", Theme.textDisabledBgColor[Theme.style].getColor());
		table.put("EditorPane.background", Theme.textBgColor[Theme.style].getColor());
		table.put("EditorPane.foreground", Theme.textTextColor[Theme.style].getColor());
		table.put("PasswordField.background", Theme.textBgColor[Theme.style].getColor());
		table.put("PasswordField.foreground", Theme.textTextColor[Theme.style].getColor());
		table.put("PasswordField.inactiveBackground", Theme.textDisabledBgColor[Theme.style].getColor());
		table.put("TextArea.background", Theme.textBgColor[Theme.style].getColor());
		table.put("TextArea.foreground", Theme.textTextColor[Theme.style].getColor());
		table.put("TextArea.inactiveBackground", Theme.textDisabledBgColor[Theme.style].getColor());
		table.put("TextField.background", Theme.textBgColor[Theme.style].getColor());
		table.put("TextField.foreground", Theme.textTextColor[Theme.style].getColor());		
		table.put("TextField.inactiveBackground", Theme.textDisabledBgColor[Theme.style].getColor());
		table.put("FormattedTextField.background", Theme.textBgColor[Theme.style].getColor());
		table.put("FormattedTextField.foreground", Theme.textTextColor[Theme.style].getColor());
		table.put("FormattedTextField.inactiveBackground", Theme.textDisabledBgColor[Theme.style].getColor());
		table.put("TextPane.background", Theme.textPaneBgColor[Theme.style].getColor());
		table.put("EditorPane.background", Theme.editorPaneBgColor[Theme.style].getColor());
		table.put("OptionPane.messageForeground", Theme.textTextColor[Theme.style].getColor());
		
		table.put("PasswordField.selectionBackground", Theme.textSelectedBgColor[Theme.style].getColor());
		table.put("PasswordField.selectionForeground", Theme.textSelectedTextColor[Theme.style].getColor());
		table.put("TextField.selectionBackground", Theme.textSelectedBgColor[Theme.style].getColor());
		table.put("TextField.selectionForeground", Theme.textSelectedTextColor[Theme.style].getColor());
		table.put("FormattedTextField.selectionBackground", Theme.textSelectedBgColor[Theme.style].getColor());
		table.put("FormattedTextField.selectionForeground", Theme.textSelectedTextColor[Theme.style].getColor());
		table.put("TextArea.selectionBackground", Theme.textSelectedBgColor[Theme.style].getColor());
		table.put("TextArea.selectionForeground", Theme.textSelectedTextColor[Theme.style].getColor());
		table.put("TextPane.selectionBackground", Theme.textSelectedBgColor[Theme.style].getColor());
		table.put("TextPane.selectionForeground", Theme.textSelectedTextColor[Theme.style].getColor());

		table.put("ComboBox.selectionBackground", Theme.comboSelectedBgColor[Theme.style].getColor());
		table.put("ComboBox.selectionForeground", Theme.comboSelectedTextColor[Theme.style].getColor());
		table.put("ComboBox.focusBackground", Theme.comboSelectedBgColor[Theme.style].getColor());

		table.put("List.selectionForeground", Theme.listSelectedTextColor[Theme.style].getColor());
		table.put("List.selectionBackground", Theme.listSelectedBgColor[Theme.style].getColor());

		table.put("Tree.background", Theme.treeBgColor[Theme.style].getColor());
		table.put("Tree.textBackground", Theme.treeTextBgColor[Theme.style].getColor());
		table.put("Tree.textForeground", Theme.treeTextColor[Theme.style].getColor());
		table.put("Tree.selectionBackground", Theme.treeSelectedBgColor[Theme.style].getColor());
		table.put("Tree.selectionForeground", Theme.treeSelectedTextColor[Theme.style].getColor());
		table.put("Tree.hash", Theme.treeLineColor[Theme.style].getColor());
		table.put("Tree.line", Theme.treeLineColor[Theme.style].getColor());

		table.put("Button.disabledText", Theme.buttonDisabledFgColor[Theme.style].getColor());
		table.put("CheckBox.disabledText", Theme.checkDisabledFgColor[Theme.style].getColor());
		table.put("RadioButton.disabledText", Theme.radioDisabledFgColor[Theme.style].getColor());
		table.put("ToggleButton.disabledText", Theme.disColor[Theme.style].getColor());
		table.put("ToggleButton.disabledSelectedText", Theme.disColor[Theme.style].getColor());
		table.put("TextArea.inactiveForeground", Theme.disColor[Theme.style].getColor());
		table.put("TextField.inactiveForeground", Theme.disColor[Theme.style].getColor());
		table.put("FormattedTextField.inactiveForeground", Theme.disColor[Theme.style].getColor());
		table.put("TextPane.inactiveForeground", Theme.disColor[Theme.style].getColor());
		table.put("PasswordField.inactiveForeground", Theme.disColor[Theme.style].getColor());
		table.put("ComboBox.disabledForeground", Theme.disColor[Theme.style].getColor());
		table.put("Label.disabledForeground", Theme.disColor[Theme.style].getColor());
		table.put("textInactiveText", Theme.disColor[Theme.style].getColor());

		table.put("Desktop.background", Theme.desktopPaneBgColor[Theme.style].getColor());
		table.put("Separator.background", Theme.sepDarkColor[Theme.style].getColor());
		table.put("Separator.foreground", Theme.sepLightColor[Theme.style].getColor());

		table.put("TitledBorder.border", new LineBorder(
			Theme.titledBorderColor[Theme.style].getColor()));

		table.put("ToolTip.background", Theme.tipBgColor[Theme.style].getColor());
		table.put("ToolTip.backgroundInactive", Theme.tipBgDis[Theme.style].getColor());
		table.put("ToolTip.foreground", Theme.tipTextColor[Theme.style].getColor());
		table.put("ToolTip.foregroundInactive", Theme.tipTextDis[Theme.style].getColor());

		table.put("Panel.background", Theme.backColor[Theme.style].getColor());

		secondary3 = new ColorUIResource(Theme.backColor[Theme.style].getColor());

		// set default icons and colorize selected icons
		Icon icon = null;
		
		for(int i = 0; i < 20; i++) {
			if(Theme.colorize[Theme.style][i]) {
				icon = TinyLookAndFeel.getUncolorizedSystemIcon(i);
				
				if (icon != null && (icon instanceof ImageIcon)) {
					HSBReference ref = Theme.colorizer[i][Theme.style];
					
					table.put(TinyLookAndFeel.getSystemIconName(i),
						DrawRoutines.colorize(((ImageIcon)icon).getImage(),
							ref.getHue(), ref.getSaturation(),
							ref.getBrightness(), ref.isPreserveGrey()));
				}
				else {
					table.put(TinyLookAndFeel.getSystemIconName(i), icon);
				}
			}
		}
	}

	/**
	 * Gets the background color of a selected menu item.
	 * Pending!
	 *
	 * @return The background color of a selected menu item.
	 */
	public ColorUIResource getMenuSelectedBackground() {
		return new ColorUIResource(200, 200, 255);
	}

	/**
	 * Gets the foreground color of a separator (in menues etc.).
	 *
	 * @return The foreground color of a separator == secondary3 == Theme.backColor
	 */
	public ColorUIResource getSeparatorForeground() {
		return getSecondary3();
	}

	/**
	 * Gets the name of this theme.
	 *
	 * @return A string describing this theme.
	 */
	public String getName() {
		return "TinyLaF Default Theme";
	}

	/**
	 * Gets the first primary color.
	 *
	 * @return The first primary color. See field declaration for more details.
	 */
	protected ColorUIResource getPrimary1() {
		return primary1;
	}

	/**
	 * Gets the second primary color.
	 *
	 * @return The second primary color. See field declaration for more details.
	 */
	protected ColorUIResource getPrimary2() {
		return primary2;
	}

	/**
	 * Gets the third primary color.
	 *
	 * @return The third primary color. See field declaration for more details.
	 */
	protected ColorUIResource getPrimary3() {
		return primary3;
	}

	/**
	 * Gets the first secondary color.
	 *
	 * @return The first secondary color. See field declaration for more details.
	 */
	protected ColorUIResource getSecondary1() {
		return secondary1;
	}

	/**
	 * Gets the second secondary color.
	 *
	 * @return The second secondary color. See field declaration for more details.
	 */
	protected ColorUIResource getSecondary2() {
		return secondary2;
	}

	/**
	 * Gets the third secondary color.
	 *
	 * @return The third secondary color. See field declaration for more details.
	 */
	protected ColorUIResource getSecondary3() {
		return secondary3;
	}

	/**
	 * Returns the ligthBackground.
	 * @return ColorUIResource
	 */
	public ColorUIResource getLigthBackground() {
		return lightBackground;
	}

	/**
	 * Returns the ligthBackground.
	 * @return ColorUIResource
	 */
	public ColorUIResource getDarkControl() {
		return darkControl;
	}
}