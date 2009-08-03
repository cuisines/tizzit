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
import java.io.*;
import java.net.URL;

import javax.swing.plaf.*;

import de.muntjak.tinylookandfeel.controlpanel.*;

/**
 * Theme
 * 
 * @version 1.3
 * @author Hans Bickel
 */
public class Theme {
	public static final String DEFAULT_THEME = "Default.theme";
	public static final String FILE_EXTENSION = ".theme";
	public static final int TINY_STYLE 		= 0;
    public static final int W99_STYLE 		= 1;
    public static final int YQ_STYLE 		= 2;
    public static final int CUSTOM_STYLE 	= 3;
    public static int style = YQ_STYLE;
    private static final int FILE_ID_1 = 0x1234;
    private static final int FILE_ID_2 = 0x2234;
    public static final int FILE_ID_3A = 0x3234;
    public static final int FILE_ID_3B = 0x3235;
    public static final int FILE_ID_3C = 0x3236;
    public static final int FILE_ID_3D = 0x3237;
    public static final int FILE_ID_3E = 0x3238;
    public static final int FILE_ID_3F = 0x3239;
    
    public static int fileID;
    public static int[] derivedStyle = new int[4];
    
// Colors
	public static ColorReference mainColor[] = new ColorReference[4];
    public static ColorReference disColor[] = new ColorReference[4];
    public static ColorReference backColor[] = new ColorReference[4];
    public static ColorReference frameColor[] = new ColorReference[4];
    public static ColorReference sub1Color[] = new ColorReference[4];
    public static ColorReference sub2Color[] = new ColorReference[4];
    public static ColorReference sub3Color[] = new ColorReference[4];
    public static ColorReference sub4Color[] = new ColorReference[4];
    public static ColorReference sub5Color[] = new ColorReference[4];
    public static ColorReference sub6Color[] = new ColorReference[4];
    public static ColorReference sub7Color[] = new ColorReference[4];
    public static ColorReference sub8Color[] = new ColorReference[4];
    
// Fonts
    public static ColoredFont[] plainFont = new ColoredFont[4];
  	public static ColoredFont[] boldFont = new ColoredFont[4];
  		
  	public static ColoredFont[] buttonFont = new ColoredFont[4];
  	public static ColorReference[] buttonFontColor = new ColorReference[4];
  	public static ColoredFont[] labelFont = new ColoredFont[4];
  	public static ColorReference[] labelFontColor = new ColorReference[4];
  	public static ColoredFont[] comboFont = new ColoredFont[4];
  	public static ColoredFont[] popupFont = new ColoredFont[4];
  	public static ColoredFont[] listFont = new ColoredFont[4];
  	public static ColoredFont[] menuFont = new ColoredFont[4];
  	public static ColorReference[] menuFontColor = new ColorReference[4];
  	public static ColoredFont[] menuItemFont = new ColoredFont[4];
  	public static ColorReference[] menuItemFontColor = new ColorReference[4];
  	public static ColoredFont[] passwordFont = new ColoredFont[4];
  	public static ColoredFont[] radioFont = new ColoredFont[4];
  	public static ColorReference[] radioFontColor = new ColorReference[4];
  	public static ColoredFont[] checkFont = new ColoredFont[4];
  	public static ColorReference[] checkFontColor = new ColorReference[4];
  	public static ColoredFont[] tableFont = new ColoredFont[4];
  	public static ColorReference[] tableFontColor = new ColorReference[4];
  	public static ColoredFont[] tableHeaderFont = new ColoredFont[4];
  	public static ColorReference[] tableHeaderFontColor = new ColorReference[4];
  	public static ColoredFont[] textAreaFont = new ColoredFont[4];
  	public static ColoredFont[] textFieldFont = new ColoredFont[4];
  	public static ColoredFont[] textPaneFont = new ColoredFont[4];
  	public static ColoredFont[] titledBorderFont = new ColoredFont[4];
  	public static ColorReference[] titledBorderFontColor = new ColorReference[4];
  	public static ColoredFont[] toolTipFont = new ColoredFont[4];
  	public static ColorReference[] toolTipFontColor = new ColorReference[4];
  	public static ColoredFont[] treeFont = new ColoredFont[4];
  	public static ColoredFont[] tabFont = new ColoredFont[4];
  	public static ColorReference[] tabFontColor = new ColorReference[4];
  	public static ColoredFont[] editorFont = new ColoredFont[4];
  	public static ColoredFont[] frameTitleFont = new ColoredFont[4];
  	public static ColoredFont[] internalFrameTitleFont = new ColoredFont[4];
  	public static ColoredFont[] internalPaletteTitleFont = new ColoredFont[4];
  	public static ColoredFont[] progressBarFont = new ColoredFont[4];
  	
// Progressbar
  	public static ColorReference[] progressColor = new ColorReference[4];
  	public static ColorReference[] progressTrackColor = new ColorReference[4];
	public static ColorReference[] progressBorderColor = new ColorReference[4];
	public static ColorReference[] progressDarkColor = new ColorReference[4];
	public static ColorReference[] progressLightColor = new ColorReference[4];
	public static ColorReference[] progressSelectForeColor = new ColorReference[4];
	public static ColorReference[] progressSelectBackColor = new ColorReference[4];
    	
// Text
	public static ColorReference[] textBgColor = new ColorReference[4];
	public static ColorReference[] textSelectedBgColor = new ColorReference[4];
	public static ColorReference[] textDisabledBgColor = new ColorReference[4];
	public static ColorReference[] textTextColor = new ColorReference[4];
	public static ColorReference[] textSelectedTextColor = new ColorReference[4];
	public static ColorReference[] textBorderColor = new ColorReference[4];
	public static ColorReference[] textBorderDarkColor = new ColorReference[4];
	public static ColorReference[] textBorderLightColor = new ColorReference[4];
	public static ColorReference[] textBorderDisabledColor = new ColorReference[4];
	public static ColorReference[] textBorderDarkDisabledColor = new ColorReference[4];
	public static ColorReference[] textBorderLightDisabledColor = new ColorReference[4];
	public static ColorReference[] textCaretColor = new ColorReference[4];
	public static ColorReference[] textPaneBgColor = new ColorReference[4];
	public static ColorReference[] editorPaneBgColor = new ColorReference[4];
	public static ColorReference[] desktopPaneBgColor = new ColorReference[4];
    	
    public static Insets[] textInsets = new Insets[4];
    
// Combo
	public static ColorReference[] comboBorderColor = new ColorReference[4];
	public static ColorReference[] comboDarkColor = new ColorReference[4];
	public static ColorReference[] comboLightColor = new ColorReference[4];
	public static ColorReference[] comboBorderDisabledColor = new ColorReference[4];
	public static ColorReference[] comboDarkDisabledColor = new ColorReference[4];
	public static ColorReference[] comboLightDisabledColor = new ColorReference[4];
	public static ColorReference[] comboSelectedBgColor = new ColorReference[4];
	public static ColorReference[] comboSelectedTextColor = new ColorReference[4];
	public static ColorReference[] comboFocusBgColor = new ColorReference[4];
	public static ColorReference[] comboArrowColor = new ColorReference[4];
	public static ColorReference[] comboArrowDisabledColor = new ColorReference[4];
	public static ColorReference[] comboButtColor = new ColorReference[4];
    public static ColorReference[] comboButtRolloverColor = new ColorReference[4];
    public static ColorReference[] comboButtPressedColor = new ColorReference[4];
    public static ColorReference[] comboButtDisabledColor = new ColorReference[4];
    public static ColorReference[] comboButtBorderColor = new ColorReference[4];
    public static ColorReference[] comboButtDarkColor = new ColorReference[4];
    public static ColorReference[] comboButtLightColor = new ColorReference[4];
    public static ColorReference[] comboButtBorderDisabledColor = new ColorReference[4];
    public static ColorReference[] comboButtDarkDisabledColor = new ColorReference[4];
    public static ColorReference[] comboButtLightDisabledColor = new ColorReference[4];
    public static ColorReference[] comboBgColor = new ColorReference[4];
	public static ColorReference[] comboTextColor = new ColorReference[4];
    
    public static int[] comboButtonWidth = new int[4];
    public static int[] comboSpreadLight = new int[4];
    public static int[] comboSpreadLightDisabled = new int[4];
    public static int[] comboSpreadDark = new int[4];
    public static int[] comboSpreadDarkDisabled = new int[4];    	
    public static Insets[] comboInsets = new Insets[4];    
    public static boolean[] comboRollover = new boolean[4];
    public static boolean[] comboFocus = new boolean[4];
    
// List
	public static ColorReference[] listBgColor = new ColorReference[4];
	public static ColorReference[] listTextColor = new ColorReference[4];
	public static ColorReference[] listSelectedBgColor = new ColorReference[4];
	public static ColorReference[] listSelectedTextColor = new ColorReference[4];

// Menu
	public static ColorReference[] menuBarColor = new ColorReference[4];
	public static ColorReference[] menuRolloverBgColor = new ColorReference[4];
	public static ColorReference[] menuRolloverFgColor = new ColorReference[4];
	public static ColorReference[] menuDisabledFgColor = new ColorReference[4];
	public static ColorReference[] menuItemRolloverColor = new ColorReference[4];
	public static ColorReference[] menuSelectedTextColor = new ColorReference[4];
	public static ColorReference[] menuBorderColor = new ColorReference[4];
	public static ColorReference[] menuDarkColor = new ColorReference[4];
	public static ColorReference[] menuLightColor = new ColorReference[4];
	public static ColorReference[] menuPopupColor = new ColorReference[4];
	public static ColorReference[] menuInnerHilightColor = new ColorReference[4];
	public static ColorReference[] menuInnerShadowColor = new ColorReference[4];
	public static ColorReference[] menuOuterHilightColor = new ColorReference[4];
	public static ColorReference[] menuOuterShadowColor = new ColorReference[4];
	public static ColorReference[] menuIconColor = new ColorReference[4];
	public static ColorReference[] menuIconRolloverColor = new ColorReference[4];
	public static ColorReference[] menuIconDisabledColor = new ColorReference[4];
	public static ColorReference[] menuIconShadowColor = new ColorReference[4];
	public static ColorReference[] menuSepDarkColor = new ColorReference[4];
	public static ColorReference[] menuSepLightColor = new ColorReference[4];
    
    public static int[] menuSeparatorHeight = new int[4];    
    public static Insets[] menuBorderInsets = new Insets[4];    	
    public static boolean[] menuRollover = new boolean[4];
    
// Toolbar
    public static ColorReference[] toolBarColor = new ColorReference[4];
	public static ColorReference[] toolBarDarkColor = new ColorReference[4];
	public static ColorReference[] toolBarLightColor = new ColorReference[4];
	public static ColorReference[] toolButtColor = new ColorReference[4];
	public static ColorReference[] toolButtSelectedColor = new ColorReference[4];
	public static ColorReference[] toolButtRolloverColor = new ColorReference[4];
	public static ColorReference[] toolButtPressedColor = new ColorReference[4];
	public static ColorReference[] toolBorderColor = new ColorReference[4];
	public static ColorReference[] toolBorderSelectedColor = new ColorReference[4];
	public static ColorReference[] toolBorderRolloverColor = new ColorReference[4];
	public static ColorReference[] toolBorderPressedColor = new ColorReference[4];
	public static ColorReference[] toolBorderDarkColor = new ColorReference[4];
	public static ColorReference[] toolBorderLightColor = new ColorReference[4];
	public static ColorReference[] toolGripDarkColor = new ColorReference[4];
	public static ColorReference[] toolGripLightColor = new ColorReference[4];
	public static ColorReference[] toolSepDarkColor = new ColorReference[4];
	public static ColorReference[] toolSepLightColor = new ColorReference[4];
	
	// new in 1.3
    public static int[] toolMarginTop = new int[4];
    public static int[] toolMarginLeft = new int[4];
    public static int[] toolMarginBottom = new int[4];
    public static int[] toolMarginRight = new int[4];
	
	public static boolean[] toolFocus = new boolean[4];
	public static boolean[] toolRollover = new boolean[4];
	
// Button
    public static ColorReference[] buttonNormalColor = new ColorReference[4];
    public static ColorReference[] buttonRolloverBgColor = new ColorReference[4];
    public static ColorReference[] buttonPressedColor = new ColorReference[4];
    public static ColorReference[] buttonDisabledColor = new ColorReference[4];
    public static ColorReference[] buttonRolloverColor = new ColorReference[4];
    public static ColorReference[] buttonDefaultColor = new ColorReference[4];
    public static ColorReference[] buttonCheckColor = new ColorReference[4];
    public static ColorReference[] buttonCheckDisabledColor = new ColorReference[4];
    public static ColorReference[] buttonBorderColor = new ColorReference[4];
    public static ColorReference[] buttonDarkColor = new ColorReference[4];
    public static ColorReference[] buttonLightColor = new ColorReference[4];
    public static ColorReference[] buttonBorderDisabledColor = new ColorReference[4];
    public static ColorReference[] buttonDarkDisabledColor = new ColorReference[4];
    public static ColorReference[] buttonLightDisabledColor = new ColorReference[4];
    public static ColorReference[] buttonDisabledFgColor = new ColorReference[4];
    public static ColorReference[] checkDisabledFgColor = new ColorReference[4];
    public static ColorReference[] radioDisabledFgColor = new ColorReference[4];

    public static boolean[] buttonRollover = new boolean[4];
    public static boolean[] buttonFocus = new boolean[4];
    public static boolean[] buttonFocusBorder = new boolean[4];
    public static boolean[] buttonEnter = new boolean[4];
    
    // new in 1.3.04
    public static boolean[] shiftButtonText = new boolean[4];
    
    public static int[] buttonMarginTop = new int[4];
    public static int[] buttonMarginLeft = new int[4];
    public static int[] buttonMarginBottom = new int[4];
    public static int[] buttonMarginRight = new int[4];
    
    public static int[] buttonSpreadLight = new int[4];
    public static int[] buttonSpreadLightDisabled = new int[4];
    public static int[] buttonSpreadDark = new int[4];
    public static int[] buttonSpreadDarkDisabled = new int[4];
    
// CheckBox
	public static Dimension[] checkSize = new Dimension[4];
	// new in 1.3
	public static int[] checkMarginTop = new int[4];
    public static int[] checkMarginLeft = new int[4];
    public static int[] checkMarginBottom = new int[4];
    public static int[] checkMarginRight = new int[4];
    
// Tabbed
	public static ColorReference[] tabPaneBorderColor = new ColorReference[4];
    public static ColorReference[] tabPaneDarkColor = new ColorReference[4];
    public static ColorReference[] tabPaneLightColor = new ColorReference[4];
    public static ColorReference[] tabNormalColor = new ColorReference[4];
    public static ColorReference[] tabSelectedColor = new ColorReference[4];
    public static ColorReference[] tabDisabledColor = new ColorReference[4];
    public static ColorReference[] tabDisabledSelectedColor = new ColorReference[4];
    public static ColorReference[] tabDisabledTextColor = new ColorReference[4];
    public static ColorReference[] tabBorderColor = new ColorReference[4];
    public static ColorReference[] tabDarkColor = new ColorReference[4];
    public static ColorReference[] tabLightColor = new ColorReference[4];
    public static ColorReference[] tabRolloverColor = new ColorReference[4];
    
    public static int[] firstTabDistance = new int[4];
    
    public static boolean[] tabRollover = new boolean[4];
    
    // new in 1.3.05
    public static boolean[] tabFocus = new boolean[4];
    public static boolean[] ignoreSelectedBg = new boolean[4];
    public static boolean[] fixedTabs = new boolean[4];

    public static Insets[] tabInsets = new Insets[4];
    public static Insets[] tabAreaInsets = new Insets[4];
    	
// Slider
	public static Dimension[] sliderVertSize = new Dimension[4];
	public static Dimension[] sliderHorzSize = new Dimension[4];
	
	public static boolean[] sliderRolloverEnabled = new boolean[4];
	
	// new in 1.3.05
	public static boolean[] sliderFocusEnabled = new boolean[4];
	
	public static ColorReference[] sliderThumbColor = new ColorReference[4];
    public static ColorReference[] sliderThumbRolloverColor = new ColorReference[4];
    public static ColorReference[] sliderThumbPressedColor = new ColorReference[4];
    public static ColorReference[] sliderThumbDisabledColor = new ColorReference[4];
    public static ColorReference[] sliderBorderColor = new ColorReference[4];
    public static ColorReference[] sliderDarkColor = new ColorReference[4];
    public static ColorReference[] sliderLightColor = new ColorReference[4];
    public static ColorReference[] sliderBorderDisabledColor = new ColorReference[4];
    public static ColorReference[] sliderDarkDisabledColor = new ColorReference[4];
    public static ColorReference[] sliderLightDisabledColor = new ColorReference[4];
    public static ColorReference[] sliderTrackColor = new ColorReference[4];
    public static ColorReference[] sliderTrackBorderColor = new ColorReference[4];
    public static ColorReference[] sliderTrackDarkColor = new ColorReference[4];
    public static ColorReference[] sliderTrackLightColor = new ColorReference[4];
    public static ColorReference[] sliderTickColor = new ColorReference[4];
    public static ColorReference[] sliderTickDisabledColor = new ColorReference[4];
    
    // new in 1.3.05
    public static ColorReference[] sliderFocusColor = new ColorReference[4];
    
// Spinner
	public static boolean[] spinnerRollover = new boolean[4];
	
	public static ColorReference[] spinnerButtColor = new ColorReference[4];
    public static ColorReference[] spinnerButtRolloverColor = new ColorReference[4];
    public static ColorReference[] spinnerButtPressedColor = new ColorReference[4];
    public static ColorReference[] spinnerButtDisabledColor = new ColorReference[4];
    public static ColorReference[] spinnerBorderColor = new ColorReference[4];
    public static ColorReference[] spinnerDarkColor = new ColorReference[4];
    public static ColorReference[] spinnerLightColor = new ColorReference[4];
    public static ColorReference[] spinnerBorderDisabledColor = new ColorReference[4];
    public static ColorReference[] spinnerDarkDisabledColor = new ColorReference[4];
    public static ColorReference[] spinnerLightDisabledColor = new ColorReference[4];
    public static ColorReference[] spinnerArrowColor = new ColorReference[4];
	public static ColorReference[] spinnerArrowDisabledColor = new ColorReference[4];
	
	public static int[] spinnerSpreadLight = new int[4];
	public static int[] spinnerSpreadLightDisabled = new int[4];
	public static int[] spinnerSpreadDark = new int[4];
	public static int[] spinnerSpreadDarkDisabled = new int[4];
    	
// Scrollbar
    public static ColorReference[] scrollTrackColor = new ColorReference[4];
    public static ColorReference[] scrollTrackDisabledColor = new ColorReference[4];
    public static ColorReference[] scrollTrackBorderColor = new ColorReference[4];
    public static ColorReference[] scrollTrackBorderDisabledColor = new ColorReference[4];
    public static ColorReference[] scrollThumbColor = new ColorReference[4];
    public static ColorReference[] scrollThumbRolloverColor = new ColorReference[4];
    public static ColorReference[] scrollThumbPressedColor = new ColorReference[4];
    public static ColorReference[] scrollThumbDisabledColor = new ColorReference[4];
    public static ColorReference[] scrollButtColor = new ColorReference[4];
    public static ColorReference[] scrollButtRolloverColor = new ColorReference[4];
    public static ColorReference[] scrollButtPressedColor = new ColorReference[4];
    public static ColorReference[] scrollButtDisabledColor = new ColorReference[4];
    public static ColorReference[] scrollArrowColor = new ColorReference[4];
    public static ColorReference[] scrollArrowDisabledColor = new ColorReference[4];
    public static ColorReference[] scrollGripLightColor = new ColorReference[4];
    public static ColorReference[] scrollGripDarkColor = new ColorReference[4];
    public static ColorReference[] scrollBorderColor = new ColorReference[4];
    public static ColorReference[] scrollDarkColor = new ColorReference[4];
    public static ColorReference[] scrollLightColor = new ColorReference[4];
    public static ColorReference[] scrollBorderDisabledColor = new ColorReference[4];
    public static ColorReference[] scrollDarkDisabledColor = new ColorReference[4];
    public static ColorReference[] scrollLightDisabledColor = new ColorReference[4];
    public static ColorReference[] scrollPaneBorderColor = new ColorReference[4];

    public static int[] scrollSpreadLight = new int[4];
    public static int[] scrollSpreadLightDisabled = new int[4];
    public static int[] scrollSpreadDark = new int[4];
    public static int[] scrollSpreadDarkDisabled = new int[4];
    
    public static boolean[] scrollRollover = new boolean[4];
    
// Tree
    public static ColorReference[] treeBgColor = new ColorReference[4];
    public static ColorReference[] treeTextColor = new ColorReference[4];
    public static ColorReference[] treeTextBgColor = new ColorReference[4];
    public static ColorReference[] treeSelectedTextColor = new ColorReference[4];
    public static ColorReference[] treeSelectedBgColor = new ColorReference[4];
    public static ColorReference[] treeLineColor = new ColorReference[4];
    
// Frame
	public static ColorReference[] frameCaptionColor = new ColorReference[4];
	public static ColorReference[] frameCaptionDisabledColor = new ColorReference[4];
	public static ColorReference[] frameBorderColor = new ColorReference[4];
	public static ColorReference[] frameDarkColor = new ColorReference[4];
	public static ColorReference[] frameLightColor = new ColorReference[4];
	public static ColorReference[] frameBorderDisabledColor = new ColorReference[4];
	public static ColorReference[] frameDarkDisabledColor = new ColorReference[4];
	public static ColorReference[] frameLightDisabledColor = new ColorReference[4];
	public static ColorReference[] frameTitleColor = new ColorReference[4];
	public static ColorReference[] frameTitleDisabledColor = new ColorReference[4];
	public static ColorReference[] frameButtColor = new ColorReference[4];
    public static ColorReference[] frameButtRolloverColor = new ColorReference[4];
    public static ColorReference[] frameButtPressedColor = new ColorReference[4];
    public static ColorReference[] frameButtDisabledColor = new ColorReference[4];
    public static ColorReference[] frameButtCloseColor = new ColorReference[4];
    public static ColorReference[] frameButtCloseRolloverColor = new ColorReference[4];
    public static ColorReference[] frameButtClosePressedColor = new ColorReference[4];
    public static ColorReference[] frameButtCloseDisabledColor = new ColorReference[4];
    public static ColorReference[] frameButtBorderColor = new ColorReference[4];
    public static ColorReference[] frameButtDarkColor = new ColorReference[4];
    public static ColorReference[] frameButtLightColor = new ColorReference[4];
    public static ColorReference[] frameButtBorderDisabledColor = new ColorReference[4];
    public static ColorReference[] frameButtDarkDisabledColor = new ColorReference[4];
    public static ColorReference[] frameButtLightDisabledColor = new ColorReference[4];
    public static int[] frameButtSpreadLight = new int[4];
    public static int[] frameButtSpreadDark = new int[4];
    public static int[] frameButtSpreadLightDisabled = new int[4];
    public static int[] frameButtSpreadDarkDisabled = new int[4];
    public static ColorReference[] frameButtCloseBorderColor = new ColorReference[4];
    public static ColorReference[] frameButtCloseDarkColor = new ColorReference[4];
    public static ColorReference[] frameButtCloseLightColor = new ColorReference[4];
    public static ColorReference[] frameButtCloseBorderDisabledColor = new ColorReference[4];
    public static ColorReference[] frameButtCloseDarkDisabledColor = new ColorReference[4];
    public static ColorReference[] frameButtCloseLightDisabledColor = new ColorReference[4];
    public static int[] frameButtCloseSpreadLight = new int[4];
    public static int[] frameButtCloseSpreadLightDisabled = new int[4];
    public static int[] frameButtCloseSpreadDark = new int[4];
    public static int[] frameButtCloseSpreadDarkDisabled = new int[4];
    public static ColorReference[] frameSymbolColor = new ColorReference[4];
    public static ColorReference[] frameSymbolPressedColor = new ColorReference[4];
    public static ColorReference[] frameSymbolDisabledColor = new ColorReference[4];
    public static ColorReference[] frameSymbolDarkColor = new ColorReference[4];
    public static ColorReference[] frameSymbolLightColor = new ColorReference[4];
    public static ColorReference[] frameSymbolCloseColor = new ColorReference[4];
    public static ColorReference[] frameSymbolClosePressedColor = new ColorReference[4];
    public static ColorReference[] frameSymbolCloseDisabledColor = new ColorReference[4];
    public static ColorReference[] frameSymbolCloseDarkColor = new ColorReference[4];
    public static ColorReference[] frameSymbolCloseLightColor = new ColorReference[4];
	
	public static Dimension[] frameExternalButtonSize = new Dimension[4];
	public static Dimension[] frameInternalButtonSize = new Dimension[4];
	public static Dimension[] framePaletteButtonSize = new Dimension[4];
	
	public static int[] frameSpreadDark = new int[4];
	public static int[] frameSpreadLight = new int[4];
	public static int[] frameSpreadDarkDisabled = new int[4];
	public static int[] frameSpreadLightDisabled = new int[4];
	public static int[] frameBorderWidth = new int[4];
	public static int[] frameTitleHeight = new int[4];
	public static int[] frameInternalTitleHeight = new int[4];
	public static int[] framePaletteTitleHeight = new int[4];
	
	public static boolean[] frameIsTransparent = new boolean[4];
	
// Table
	public static ColorReference[] tableBackColor = new ColorReference[4];
	public static ColorReference[] tableHeaderBackColor = new ColorReference[4];
	public static ColorReference[] tableHeaderRolloverBackColor = new ColorReference[4];
	public static ColorReference[] tableHeaderRolloverColor = new ColorReference[4];
	public static ColorReference[] tableHeaderArrowColor = new ColorReference[4];
	public static ColorReference[] tableGridColor = new ColorReference[4];
	public static ColorReference[] tableSelectedBackColor = new ColorReference[4];
	public static ColorReference[] tableSelectedForeColor = new ColorReference[4];
	public static ColorReference[] tableBorderDarkColor = new ColorReference[4];
	public static ColorReference[] tableBorderLightColor = new ColorReference[4];
	public static ColorReference[] tableHeaderDarkColor = new ColorReference[4];
	public static ColorReference[] tableHeaderLightColor = new ColorReference[4];
	
// Icons
	public static ColorReference[] frameIconColor = new ColorReference[4];
	public static ColorReference[] treeIconColor = new ColorReference[4];
	public static ColorReference[] fileViewIconColor = new ColorReference[4];
	public static ColorReference[] fileChooserIconColor = new ColorReference[4];
	public static ColorReference[] optionPaneIconColor = new ColorReference[4];
	
	public static int[] hue = new int[4];
	public static HSBReference[][] colorizer = new HSBReference[20][4];
	
	public static boolean[][] colorize = new boolean[4][20];
	public static boolean[] colorizeFrameIcon = new boolean[4];
	public static boolean[] colorizeTreeIcon = new boolean[4];
	public static boolean[] colorizeFileViewIcon = new boolean[4];
	public static boolean[] colorizeFileChooserIcon = new boolean[4];
	public static boolean[] colorizeOptionPaneIcon = new boolean[4];
	
// Separator
	public static ColorReference[] sepDarkColor = new ColorReference[4];
	public static ColorReference[] sepLightColor = new ColorReference[4];
	
// ToolTip
	public static ColorReference[] tipBorderColor = new ColorReference[4];
	public static ColorReference[] tipBorderDis = new ColorReference[4];
	public static ColorReference[] tipBgColor = new ColorReference[4];
	public static ColorReference[] tipBgDis = new ColorReference[4];
	public static ColorReference[] tipTextColor = new ColorReference[4];
	public static ColorReference[] tipTextDis = new ColorReference[4];
	
// Misc
	public static ColorReference[] titledBorderColor = new ColorReference[4];
	
	static {
		initData();
	}
	
    public static void initData() {
    	derivedStyle[0] = TINY_STYLE;
    	derivedStyle[1] = W99_STYLE;
    	derivedStyle[2] = YQ_STYLE;
    	
// Colors
		mainColor[0] = new ColorReference(new Color(153, 153, 255), 0, 0, ColorReference.ABS_COLOR, true);
		mainColor[1] = new ColorReference(new Color(0, 106, 255), 0, 0, ColorReference.ABS_COLOR, true);
		mainColor[2] = new ColorReference(new Color(0, 106, 255), 0, 0, ColorReference.ABS_COLOR, true);
		mainColor[3] = new ColorReference(new Color(153, 153, 255), 0, 0, ColorReference.ABS_COLOR, true);
		
    	disColor[0] = new ColorReference(new Color(255, 199, 59), 0, 0, ColorReference.ABS_COLOR, true);
    	disColor[1] = new ColorReference(new Color(143, 141, 139), 0, 0, ColorReference.ABS_COLOR, true);
    	disColor[2] = new ColorReference(new Color(143, 142, 139), 0, 0, ColorReference.ABS_COLOR, true);
    	disColor[3] = new ColorReference(new Color(255, 199, 59), 0, 0, ColorReference.ABS_COLOR, true);
    	
    	backColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.ABS_COLOR, true);
    	backColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.ABS_COLOR, true);
    	backColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.ABS_COLOR, true);
    	backColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.ABS_COLOR, true);
    	
    	frameColor[0] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR, true);
    	frameColor[1] = new ColorReference(new Color(10, 36, 106), 0, 0, ColorReference.ABS_COLOR, true);
    	frameColor[2] = new ColorReference(new Color(0, 85, 255), 0, 0, ColorReference.ABS_COLOR, true);
    	frameColor[3] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR, true);
    	
    	sub1Color[0] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR);
    	sub1Color[1] = new ColorReference(new Color(10, 50, 105), -18, -55, ColorReference.MAIN_COLOR);
    	sub1Color[2] = new ColorReference(new Color(197, 213, 252), 0, 0, ColorReference.ABS_COLOR);
    	sub1Color[3] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR);
    	
    	sub2Color[0] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR);
    	sub2Color[1] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR);
    	sub2Color[2] = new ColorReference(new Color(34, 161, 34), 0, 0, ColorReference.ABS_COLOR);
    	sub2Color[3] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR);
    	
    	sub3Color[0] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR);
    	sub3Color[1] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR);
    	sub3Color[2] = new ColorReference(new Color(231, 232, 245), 0, 0, ColorReference.ABS_COLOR);
    	sub3Color[3] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR);
    	
    	sub4Color[0] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR);
    	sub4Color[1] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR);
    	sub4Color[2] = new ColorReference(new Color(227, 92, 60), 0, 0, ColorReference.ABS_COLOR);
    	sub4Color[3] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR);
    	
    	sub5Color[0] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR);
    	sub5Color[1] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR);
    	sub5Color[2] = new ColorReference(new Color(120, 123, 189), 0, 0, ColorReference.ABS_COLOR);
    	sub5Color[3] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR);
    	
    	sub6Color[0] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR);
    	sub6Color[1] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR);
    	sub6Color[2] = new ColorReference(new Color(248, 179, 48), 0, 0, ColorReference.ABS_COLOR);
    	sub6Color[3] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR);
    	
    	sub7Color[0] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR);
    	sub7Color[1] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR);
    	sub7Color[2] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR);
    	sub7Color[3] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR);
    	
    	sub8Color[0] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR);
    	sub8Color[1] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR);
    	sub8Color[2] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR);
    	sub8Color[3] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.ABS_COLOR);
    
// Font
    	plainFont[0] = new ColoredFont("sansserif", Font.PLAIN, 12);
    	plainFont[1] = new ColoredFont("sansserif", Font.PLAIN, 12);
    	plainFont[2] = new ColoredFont("Tahoma", Font.PLAIN, 11);
    	plainFont[3] = new ColoredFont("sansserif", Font.PLAIN, 12);
    	
  		boldFont[0] = new ColoredFont("sansserif", Font.BOLD, 11);
  		boldFont[1] = new ColoredFont("sansserif", Font.BOLD, 11);
  		boldFont[2] = new ColoredFont("Tahoma", Font.BOLD, 11);
		boldFont[3] = new ColoredFont("sansserif", Font.BOLD, 11);
		
  		buttonFont[0] = new ColoredFont(buttonFontColor);
  		buttonFont[1] = new ColoredFont(buttonFontColor);
  		buttonFont[2] = new ColoredFont(buttonFontColor);
		buttonFont[3] = new ColoredFont(buttonFontColor);
		
  		labelFont[0] = new ColoredFont(labelFontColor);
  		labelFont[1] = new ColoredFont(labelFontColor);
  		labelFont[2] = new ColoredFont(labelFontColor);
  		labelFont[3] = new ColoredFont(labelFontColor);
  		
  		passwordFont[0] = new ColoredFont();
  		passwordFont[1] = new ColoredFont();
  		passwordFont[2] = new ColoredFont();
  		passwordFont[3] = new ColoredFont();
  		
  		comboFont[0] = new ColoredFont();
  		comboFont[1] = new ColoredFont();
  		comboFont[2] = new ColoredFont();
  		comboFont[3] = new ColoredFont();
  		
  		// with version 1.1, popupFont disappeared
  		// but still must be initialized for 1.0
  		popupFont[0] = new ColoredFont();
  		popupFont[1] = new ColoredFont();
  		popupFont[2] = new ColoredFont();
  		popupFont[3] = new ColoredFont();
  		
  		listFont[0] = new ColoredFont();
  		listFont[1] = new ColoredFont();
  		listFont[2] = new ColoredFont();
  		listFont[3] = new ColoredFont();

  		menuFont[0] = new ColoredFont(menuFontColor);
  		menuFont[1] = new ColoredFont(menuFontColor);
  		menuFont[2] = new ColoredFont(menuFontColor);
  		menuFont[3] = new ColoredFont(menuFontColor);
  		
  		menuItemFont[0] = new ColoredFont(menuItemFontColor);
  		menuItemFont[1] = new ColoredFont(menuItemFontColor);
  		menuItemFont[2] = new ColoredFont(menuItemFontColor);
  		menuItemFont[3] = new ColoredFont(menuItemFontColor);

  		radioFont[0] = new ColoredFont(radioFontColor);
  		radioFont[1] = new ColoredFont(radioFontColor);
  		radioFont[2] = new ColoredFont(radioFontColor);
  		radioFont[3] = new ColoredFont(radioFontColor);
 
  		checkFont[0] = new ColoredFont(checkFontColor);
  		checkFont[1] = new ColoredFont(checkFontColor);
  		checkFont[2] = new ColoredFont(checkFontColor);
  		checkFont[3] = new ColoredFont(checkFontColor);
  		
  		tableFont[0] = new ColoredFont(tableFontColor);
  		tableFont[1] = new ColoredFont(tableFontColor);
  		tableFont[2] = new ColoredFont(tableFontColor);
  		tableFont[3] = new ColoredFont(tableFontColor);
  		
  		tableHeaderFont[0] = new ColoredFont(tableHeaderFontColor);
  		tableHeaderFont[1] = new ColoredFont(tableHeaderFontColor);
  		tableHeaderFont[2] = new ColoredFont(tableHeaderFontColor);
  		tableHeaderFont[3] = new ColoredFont(tableHeaderFontColor);
  		
  		textAreaFont[0] = new ColoredFont();
  		textAreaFont[1] = new ColoredFont();
  		textAreaFont[2] = new ColoredFont();
  		textAreaFont[3] = new ColoredFont();
  		
  		textFieldFont[0] = new ColoredFont();
  		textFieldFont[1] = new ColoredFont();
  		textFieldFont[2] = new ColoredFont();
  		textFieldFont[3] = new ColoredFont();
  		
  		textPaneFont[0] = new ColoredFont();
  		textPaneFont[1] = new ColoredFont();
  		textPaneFont[2] = new ColoredFont();
  		textPaneFont[3] = new ColoredFont();
  		
  		titledBorderFont[0] = new ColoredFont(titledBorderFontColor);
  		titledBorderFont[1] = new ColoredFont(titledBorderFontColor);
  		titledBorderFont[2] = new ColoredFont(titledBorderFontColor);
  		titledBorderFont[3] = new ColoredFont(titledBorderFontColor);
  		
  		// sine 1.3C toolTipFont has no color property ...
  		toolTipFont[0] = new ColoredFont();
  		toolTipFont[1] = new ColoredFont();
  		toolTipFont[2] = new ColoredFont();
  		toolTipFont[3] = new ColoredFont();
  		// ... but we must still initialize toolTipFontColor
  		toolTipFontColor[0] = new ColorReference(Color.BLACK, 0, -100, ColorReference.BACK_COLOR);
  		toolTipFontColor[1] = new ColorReference(Color.BLACK, 0, -100, ColorReference.BACK_COLOR);
  		toolTipFontColor[2] = new ColorReference(Color.BLACK, 0, -100, ColorReference.BACK_COLOR);
  		toolTipFontColor[3] = new ColorReference(Color.BLACK, 0, -100, ColorReference.BACK_COLOR);

  		treeFont[0] = new ColoredFont();
  		treeFont[1] = new ColoredFont();
  		treeFont[2] = new ColoredFont();
  		treeFont[3] = new ColoredFont();

  		tabFontColor[0] = new ColorReference(new Color(10, 50, 105));
  		tabFontColor[1] = new ColorReference(new Color(0, 0, 0));
  		tabFontColor[2] = new ColorReference(new Color(0, 0, 0));
  		tabFontColor[3] = new ColorReference(new Color(10, 50, 105));
  		tabFont[0] = new ColoredFont("sansserif", Font.BOLD, 11, tabFontColor);
  		tabFont[1] = new ColoredFont(tabFontColor);
  		tabFont[2] = new ColoredFont(tabFontColor);
  		tabFont[3] = new ColoredFont("sansserif", Font.BOLD, 11, tabFontColor);
  		tabFont[0].setBoldFont(true);
  		tabFont[1].setBoldFont(false);
  		tabFont[2].setBoldFont(false);
  		tabFont[3].setBoldFont(true);
  		
  		editorFont[0] = new ColoredFont();
  		editorFont[1] = new ColoredFont();
  		editorFont[2] = new ColoredFont();
  		editorFont[3] = new ColoredFont();

  		frameTitleFont[0] = new ColoredFont();
  		frameTitleFont[1] = new ColoredFont("Tahoma", Font.BOLD, 12);
  		frameTitleFont[2] = new ColoredFont("dialog",Font.BOLD,13);
  		frameTitleFont[3] = new ColoredFont();
  		
  		internalFrameTitleFont[0] = new ColoredFont();
  		internalFrameTitleFont[1] = new ColoredFont("Tahoma", Font.BOLD, 11);
  		internalFrameTitleFont[2] = new ColoredFont("dialog",Font.BOLD,12);
  		internalFrameTitleFont[3] = new ColoredFont();
  		
  		internalPaletteTitleFont[0] = new ColoredFont();
  		internalPaletteTitleFont[1] = new ColoredFont("Tahoma", Font.BOLD, 11);
  		internalPaletteTitleFont[2] = new ColoredFont("dialog",Font.BOLD,11);
  		internalPaletteTitleFont[3] = new ColoredFont();
  		
  		progressBarFont[0] = new ColoredFont();
  		progressBarFont[1] = new ColoredFont();
  		progressBarFont[2] = new ColoredFont();
  		progressBarFont[3] = new ColoredFont();
  		
// Progressbar
  		progressColor[0] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.ABS_COLOR);
  		progressColor[1] = new ColorReference(new Color(10, 50, 105), 0, 0, ColorReference.SUB1_COLOR);
  		progressColor[2] = new ColorReference(new Color(44, 212, 43), 43, 19, ColorReference.SUB2_COLOR);
  		progressColor[3] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.ABS_COLOR);
  		
  		progressTrackColor[0] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.ABS_COLOR);
  		progressTrackColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
  		progressTrackColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
  		progressTrackColor[3] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.ABS_COLOR);
  		
		progressBorderColor[0] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
		progressBorderColor[1] = new ColorReference(new Color(64, 64, 64), -100, -69, ColorReference.BACK_COLOR);
		progressBorderColor[2] = new ColorReference(new Color(104, 104, 104), -100, -54, ColorReference.BACK_COLOR);
		progressBorderColor[3] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
		
		progressDarkColor[0] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
		progressDarkColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
		progressDarkColor[2] = new ColorReference(new Color(190, 190, 190), -100, -16, ColorReference.BACK_COLOR);
		progressDarkColor[3] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
		
		progressLightColor[0] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
		progressLightColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		progressLightColor[2] = new ColorReference(new Color(238, 238, 238), -100, 40, ColorReference.BACK_COLOR);
		progressLightColor[3] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);

		progressSelectForeColor[0] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		progressSelectForeColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		progressSelectForeColor[2] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		progressSelectForeColor[3] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		
		progressSelectBackColor[0] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		progressSelectBackColor[1] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		progressSelectBackColor[2] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		progressSelectBackColor[3] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
// Text	
		textBgColor[0] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.BACK_COLOR);
		textBgColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		textBgColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		textBgColor[3] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.BACK_COLOR);
		
		textPaneBgColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		textPaneBgColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		textPaneBgColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		textPaneBgColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		
		editorPaneBgColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		editorPaneBgColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		editorPaneBgColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		editorPaneBgColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	
    	desktopPaneBgColor[0] = new ColorReference(new Color(212, 208, 200), 0, -10, ColorReference.BACK_COLOR);
    	desktopPaneBgColor[1] = new ColorReference(new Color(191, 187, 180), 0, -10, ColorReference.BACK_COLOR);
    	desktopPaneBgColor[2] = new ColorReference(new Color(212, 210, 194), 0, -10, ColorReference.BACK_COLOR);
    	desktopPaneBgColor[3] = new ColorReference(new Color(212, 208, 200), 0, -10, ColorReference.BACK_COLOR);
    	
    	textTextColor[0] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	textTextColor[1] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	textTextColor[2] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	textTextColor[3] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	
    	textCaretColor[0] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	textCaretColor[1] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	textCaretColor[2] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	textCaretColor[3] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	
    	textSelectedBgColor[0] = new ColorReference(new Color(51, 51, 154), 0, 0, ColorReference.BACK_COLOR);
    	textSelectedBgColor[1] = new ColorReference(new Color(10, 50, 105), 0, 0, ColorReference.SUB1_COLOR);
		textSelectedBgColor[2] = new ColorReference(new Color(43, 107, 197), -36, -6, ColorReference.MAIN_COLOR);
		textSelectedBgColor[3] = new ColorReference(new Color(51, 51, 154), 0, 0, ColorReference.BACK_COLOR);
    	
    	textSelectedTextColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	textSelectedTextColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	textSelectedTextColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	textSelectedTextColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	
    	textDisabledBgColor[0] = new ColorReference(new Color(236, 236, 236), 0, 0, ColorReference.BACK_COLOR);
		textDisabledBgColor[1] = new ColorReference(new Color(231, 229, 224), 0, 44, ColorReference.BACK_COLOR);
		textDisabledBgColor[2] = new ColorReference(new Color(244, 243, 233), 0, 44, ColorReference.BACK_COLOR);
		textDisabledBgColor[3] = new ColorReference(new Color(236, 236, 236), 0, 0, ColorReference.BACK_COLOR);
		
  		textBorderColor[0] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
  		textBorderColor[1] = new ColorReference(new Color(64, 64, 64), -100, -69, ColorReference.BACK_COLOR);
  		textBorderColor[2] = new ColorReference(new Color(128, 152, 186), -70, 23, ColorReference.MAIN_COLOR);
  		textBorderColor[3] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
    	
    	textBorderDarkColor[0] = new ColorReference(new Color(137, 137, 137), 0, 0, ColorReference.BACK_COLOR);
  		textBorderDarkColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
  		textBorderDarkColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
  		textBorderDarkColor[3] = new ColorReference(new Color(137, 137, 137), 0, 0, ColorReference.BACK_COLOR);
    	
    	textBorderLightColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
  		textBorderLightColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
  		textBorderLightColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
  		textBorderLightColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	
		textBorderDisabledColor[0] = new ColorReference(new Color(199, 199, 199), 0, 0, ColorReference.BACK_COLOR);
		textBorderDisabledColor[1] = new ColorReference(new Color(191, 187, 180), 0, -10, ColorReference.BACK_COLOR);
		textBorderDisabledColor[2] = new ColorReference(new Color(201, 198, 184), 0, -15, ColorReference.BACK_COLOR);
		textBorderDisabledColor[3] = new ColorReference(new Color(199, 199, 199), 0, 0, ColorReference.BACK_COLOR);
    	
    	textBorderDarkDisabledColor[0] = new ColorReference(new Color(210, 210, 210), 0, 0, ColorReference.BACK_COLOR);
		textBorderDarkDisabledColor[1] = new ColorReference(new Color(201, 198, 190), 0, -5, ColorReference.BACK_COLOR);
		textBorderDarkDisabledColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
		textBorderDarkDisabledColor[3] = new ColorReference(new Color(210, 210, 210), 0, 0, ColorReference.BACK_COLOR);
    	
    	textBorderLightDisabledColor[0] = new ColorReference(new Color(228, 228, 228), 0, 0, ColorReference.BACK_COLOR);
		textBorderLightDisabledColor[1] = new ColorReference(new Color(221, 217, 211), 0, 20, ColorReference.BACK_COLOR);
		textBorderLightDisabledColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
		textBorderLightDisabledColor[3] = new ColorReference(new Color(228, 228, 228), 0, 0, ColorReference.BACK_COLOR);

    	textInsets[0] = new Insets(2, 3, 2, 3);
    	textInsets[1] = new Insets(1, 3, 3, 3);
    	textInsets[2] = new Insets(2, 3, 2, 3);
    	textInsets[3] = new Insets(2, 3, 2, 3);
    	
// Button    	
    	buttonRollover[0] = false;
    	buttonRollover[1] = false;
    	buttonRollover[2] = true;
    	buttonRollover[3] = false;
    	
    	buttonFocus[0] = true;
    	buttonFocus[1] = true;
    	buttonFocus[2] = true;
    	buttonFocus[3] = true;
    	
    	buttonFocusBorder[0] = false;
    	buttonFocusBorder[1] = false;
    	buttonFocusBorder[2] = false;
    	buttonFocusBorder[3] = false;
    	
    	buttonEnter[0] = false;
    	buttonEnter[1] = false;
    	buttonEnter[2] = false;
    	buttonEnter[3] = false;
    	
    	shiftButtonText[0] = true;
    	shiftButtonText[1] = true;
    	shiftButtonText[2] = true;
    	shiftButtonText[3] = true;
    	
    	buttonNormalColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	buttonNormalColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	buttonNormalColor[2] = new ColorReference(new Color(231, 232, 245), 0, 0, ColorReference.SUB3_COLOR);
    	buttonNormalColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	buttonRolloverBgColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	buttonRolloverBgColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	buttonRolloverBgColor[2] = new ColorReference(new Color(239, 240, 248), 0, 33, ColorReference.SUB3_COLOR);
    	buttonRolloverBgColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	buttonPressedColor[0] = new ColorReference(new Color(126, 126, 126), 0, 0, ColorReference.BACK_COLOR);
    	buttonPressedColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	buttonPressedColor[2] = new ColorReference(new Color(217, 218, 230), 0, -6, ColorReference.SUB3_COLOR);
    	buttonPressedColor[3] = new ColorReference(new Color(126, 126, 126), 0, 0, ColorReference.BACK_COLOR);

		buttonDisabledColor[0] = new ColorReference(new Color(229, 227, 222), 0, 0, ColorReference.BACK_COLOR);
		buttonDisabledColor[1] = new ColorReference(new Color(225, 222, 217), 0, 30, ColorReference.BACK_COLOR);
		buttonDisabledColor[2] = new ColorReference(new Color(245, 244, 235), 0, 48, ColorReference.BACK_COLOR);
		buttonDisabledColor[3] = new ColorReference(new Color(229, 227, 222), 0, 0, ColorReference.BACK_COLOR);
 
    	buttonBorderColor[0] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
    	buttonBorderColor[1] = new ColorReference(new Color(64, 64, 64), -100, -69, ColorReference.BACK_COLOR);
    	buttonBorderColor[2] = new ColorReference(new Color(21, 61, 117), -30, -46, ColorReference.MAIN_COLOR);
    	buttonBorderColor[3] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
    	
    	buttonDarkColor[0] = new ColorReference(new Color(119, 119, 119), 0, 0, ColorReference.BACK_COLOR);
    	buttonDarkColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
    	buttonDarkColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
    	buttonDarkColor[3] = new ColorReference(new Color(119, 119, 119), 0, 0, ColorReference.BACK_COLOR);
    	
    	buttonLightColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	buttonLightColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	buttonLightColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
    	buttonLightColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	
    	buttonBorderDisabledColor[0] = new ColorReference(new Color(201, 198, 184), 0, 0, ColorReference.BACK_COLOR);
    	buttonBorderDisabledColor[1] = new ColorReference(new Color(180, 177, 170), 0, -15, ColorReference.BACK_COLOR);
    	buttonBorderDisabledColor[2] = new ColorReference(new Color(201, 198, 184), 0, -15, ColorReference.BACK_COLOR);
    	buttonBorderDisabledColor[3] = new ColorReference(new Color(201, 198, 184), 0, 0, ColorReference.BACK_COLOR);
    	
    	buttonDarkDisabledColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	buttonDarkDisabledColor[1] = new ColorReference(new Color(180, 177, 170), 0, -15, ColorReference.BACK_COLOR);
    	buttonDarkDisabledColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
    	buttonDarkDisabledColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	buttonLightDisabledColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	buttonLightDisabledColor[1] = new ColorReference(new Color(240, 239, 236), 0, 66, ColorReference.BACK_COLOR);
    	buttonLightDisabledColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
    	buttonLightDisabledColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    
    	buttonDisabledFgColor[0] = new ColorReference(new Color(143, 141, 139), 0, 0, ColorReference.DIS_COLOR);
    	buttonDisabledFgColor[1] = new ColorReference(new Color(143, 141, 139), 0, 0, ColorReference.DIS_COLOR);
    	buttonDisabledFgColor[2] = new ColorReference(new Color(143, 142, 139), 0, 0, ColorReference.DIS_COLOR);
    	buttonDisabledFgColor[3] = new ColorReference(new Color(143, 141, 139), 0, 0, ColorReference.DIS_COLOR);
    	
    	checkDisabledFgColor[0] = new ColorReference(new Color(143, 141, 139), 0, 0, ColorReference.DIS_COLOR);
    	checkDisabledFgColor[1] = new ColorReference(new Color(143, 141, 139), 0, 0, ColorReference.DIS_COLOR);
    	checkDisabledFgColor[2] = new ColorReference(new Color(143, 142, 139), 0, 0, ColorReference.DIS_COLOR);
    	checkDisabledFgColor[3] = new ColorReference(new Color(143, 141, 139), 0, 0, ColorReference.DIS_COLOR);
    	
    	radioDisabledFgColor[0] = new ColorReference(new Color(143, 141, 139), 0, 0, ColorReference.DIS_COLOR);
    	radioDisabledFgColor[1] = new ColorReference(new Color(143, 141, 139), 0, 0, ColorReference.DIS_COLOR);
    	radioDisabledFgColor[2] = new ColorReference(new Color(143, 142, 139), 0, 0, ColorReference.DIS_COLOR);
    	radioDisabledFgColor[3] = new ColorReference(new Color(143, 141, 139), 0, 0, ColorReference.DIS_COLOR);
    	
    	for(int i = 0; i < 4; i++) {
	    	buttonMarginTop[i] = 2;
	    	buttonMarginLeft[i] = 12;
	    	buttonMarginBottom[i] = 2;
	    	buttonMarginRight[i] = 12;
    	}

    	buttonRolloverColor[0] = new ColorReference(new Color(255, 199, 59), 0, 0, ColorReference.BACK_COLOR);
    	buttonRolloverColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	buttonRolloverColor[2] = new ColorReference(new Color(248, 179, 48), 0, 0, ColorReference.SUB6_COLOR);
    	buttonRolloverColor[3] = new ColorReference(new Color(255, 199, 59), 0, 0, ColorReference.BACK_COLOR);
    	
    	buttonDefaultColor[0] = new ColorReference(new Color(255, 199, 59), 0, 0, ColorReference.BACK_COLOR);
    	buttonDefaultColor[1] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	buttonDefaultColor[2] = new ColorReference(new Color(160, 182, 235), 38, -12, ColorReference.SUB1_COLOR);
    	buttonDefaultColor[3] = new ColorReference(new Color(255, 199, 59), 0, 0, ColorReference.BACK_COLOR);
    	
    	buttonCheckColor[0] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	buttonCheckColor[1] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	buttonCheckColor[2] = new ColorReference(new Color(34, 161, 34), 0, 0, ColorReference.SUB2_COLOR);
    	buttonCheckColor[3] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	
    	buttonCheckDisabledColor[0] = new ColorReference(new Color(187, 183, 176), 0, -12, ColorReference.BACK_COLOR);
    	buttonCheckDisabledColor[1] = new ColorReference(new Color(187, 183, 176), 0, -12, ColorReference.BACK_COLOR);
    	buttonCheckDisabledColor[2] = new ColorReference(new Color(208, 205, 190), 0, -12, ColorReference.BACK_COLOR);
    	buttonCheckDisabledColor[3] = new ColorReference(new Color(187, 183, 176), 0, -12, ColorReference.BACK_COLOR);
    	
    	checkSize[0] = new Dimension(14, 12);
    	checkSize[1] = new Dimension(13, 13);
    	checkSize[2] = new Dimension(13, 13);
    	checkSize[3] = new Dimension(14, 12);
    	
    	for(int i = 0; i < 4; i++) {
    		checkMarginTop[i] = 2;
	    	checkMarginLeft[i] = 2;
	    	checkMarginBottom[i] = 2;
	    	checkMarginRight[i] = 2;
    	}
    	
    	buttonSpreadLight[0] = 20;
		buttonSpreadLight[1] = 0;
		buttonSpreadLight[2] = 20;
		buttonSpreadLight[3] = 20;
		
		buttonSpreadDark[0] = 3;
		buttonSpreadDark[1] = 0;
		buttonSpreadDark[2] = 3;
		buttonSpreadDark[3] = 3;
		
		buttonSpreadLightDisabled[0] = 20;
		buttonSpreadLightDisabled[1] = 0;
		buttonSpreadLightDisabled[2] = 20;
		buttonSpreadLightDisabled[3] = 20;
		
		buttonSpreadDarkDisabled[0] = 1;
		buttonSpreadDarkDisabled[1] = 0;
		buttonSpreadDarkDisabled[2] = 1;
		buttonSpreadDarkDisabled[3] = 1;
    	
// Scrollbar
    	scrollRollover[0] = false;
    	scrollRollover[1] = false;
    	scrollRollover[2] = true;
    	scrollRollover[3] = false;

    	// Track
    	scrollTrackColor[0] = new ColorReference(new Color(170, 170, 170), 0, 0, ColorReference.BACK_COLOR);
    	scrollTrackColor[1] = new ColorReference(new Color(233, 231, 227), -15, 49, ColorReference.BACK_COLOR);
    	scrollTrackColor[2] = new ColorReference(new Color(249, 249, 247), -50, 76, ColorReference.BACK_COLOR);
    	scrollTrackColor[3] = new ColorReference(new Color(170, 170, 170), 0, 0, ColorReference.BACK_COLOR);
    	
    	scrollTrackDisabledColor[0] = new ColorReference(new Color(238, 238, 238), 0, 0, ColorReference.BACK_COLOR);
    	scrollTrackDisabledColor[1] = new ColorReference(new Color(233, 231, 227), -15, 49, ColorReference.BACK_COLOR);
    	scrollTrackDisabledColor[2] = new ColorReference(new Color(249, 249, 247), -50, 76, ColorReference.BACK_COLOR);
    	scrollTrackDisabledColor[3] = new ColorReference(new Color(238, 238, 238), 0, 0, ColorReference.BACK_COLOR);
    	
    	scrollTrackBorderColor[0] = new ColorReference(new Color(136, 136, 136), 0, 0, ColorReference.BACK_COLOR);
    	scrollTrackBorderColor[1] = new ColorReference(new Color(233, 231, 227), -15, 49, ColorReference.BACK_COLOR);
    	scrollTrackBorderColor[2] = new ColorReference(new Color(234, 231, 218), -23, 0, ColorReference.BACK_COLOR);
    	scrollTrackBorderColor[3] = new ColorReference(new Color(136, 136, 136), 0, 0, ColorReference.BACK_COLOR);
    	
    	scrollTrackBorderDisabledColor[0] = new ColorReference(new Color(238, 238, 238), 0, 0, ColorReference.BACK_COLOR);
    	scrollTrackBorderDisabledColor[1] = new ColorReference(new Color(233, 231, 227), -15, 49, ColorReference.BACK_COLOR);
    	scrollTrackBorderDisabledColor[2] = new ColorReference(new Color(234, 231, 218), -23, 0, ColorReference.BACK_COLOR);
    	scrollTrackBorderDisabledColor[3] = new ColorReference(new Color(238, 238, 238), 0, 0, ColorReference.BACK_COLOR);
    	
    	// Thumb
    	scrollThumbColor[0] = new ColorReference(new Color(153, 153, 255), 0, 0, ColorReference.BACK_COLOR);
    	scrollThumbColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	scrollThumbColor[2] = new ColorReference(new Color(197, 213, 252), 0, 0, ColorReference.SUB1_COLOR);
    	scrollThumbColor[3] = new ColorReference(new Color(153, 153, 255), 0, 0, ColorReference.BACK_COLOR);
    	
    	scrollThumbRolloverColor[0] = new ColorReference(new Color(175, 175, 255), 0, 0, ColorReference.BACK_COLOR);
    	scrollThumbRolloverColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	scrollThumbRolloverColor[2] = new ColorReference(new Color(226, 234, 254), 0, 50, ColorReference.SUB1_COLOR);
    	scrollThumbRolloverColor[3] = new ColorReference(new Color(175, 175, 255), 0, 0, ColorReference.BACK_COLOR);
    	
    	scrollThumbPressedColor[0] = new ColorReference(new Color(122, 122, 204), 0, 0, ColorReference.BACK_COLOR);
    	scrollThumbPressedColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	scrollThumbPressedColor[2] = new ColorReference(new Color(187, 202, 239), 0, -5, ColorReference.SUB1_COLOR);
    	scrollThumbPressedColor[3] = new ColorReference(new Color(122, 122, 204), 0, 0, ColorReference.BACK_COLOR);
    	
    	scrollThumbDisabledColor[0] = new ColorReference(new Color(214, 214, 255), 0, 0, ColorReference.BACK_COLOR);
    	scrollThumbDisabledColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	scrollThumbDisabledColor[2] = new ColorReference(new Color(238, 238, 231), 0, -3, ColorReference.SUB1_COLOR);
    	scrollThumbDisabledColor[3] = new ColorReference(new Color(214, 214, 255), 0, 0, ColorReference.BACK_COLOR);
    	
    	// Grip
    	scrollGripLightColor[0] = new ColorReference(new Color(204, 204, 255), 0, 0, ColorReference.BACK_COLOR);
    	scrollGripLightColor[1] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
    	scrollGripLightColor[2] = new ColorReference(new Color(238, 243, 254), 0, 71, ColorReference.SUB1_COLOR);
    	scrollGripLightColor[3] = new ColorReference(new Color(204, 204, 255), 0, 0, ColorReference.BACK_COLOR);
    	
    	scrollGripDarkColor[0] = new ColorReference(new Color(51, 51, 153), 0, 0, ColorReference.BACK_COLOR);
    	scrollGripDarkColor[1] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
    	scrollGripDarkColor[2] = new ColorReference(new Color(171, 185, 219), 0, -13, ColorReference.SUB1_COLOR);
    	scrollGripDarkColor[3] = new ColorReference(new Color(51, 51, 153), 0, 0, ColorReference.BACK_COLOR);
    	
    	// Buttons
    	scrollButtColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	scrollButtColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	scrollButtColor[2] = new ColorReference(new Color(197, 213, 252), 0, 0, ColorReference.SUB1_COLOR);
    	scrollButtColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	scrollButtRolloverColor[0] = new ColorReference(new Color(243, 243, 243), 0, 0, ColorReference.BACK_COLOR);
    	scrollButtRolloverColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	scrollButtRolloverColor[2] = new ColorReference(new Color(226, 234, 254), 0, 50, ColorReference.SUB1_COLOR);
    	scrollButtRolloverColor[3] = new ColorReference(new Color(243, 243, 243), 0, 0, ColorReference.BACK_COLOR);
    	
    	scrollButtPressedColor[0] = new ColorReference(new Color(126, 126, 126), 0, 0, ColorReference.BACK_COLOR);
    	scrollButtPressedColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	scrollButtPressedColor[2] = new ColorReference(new Color(187, 202, 239), 0, -5, ColorReference.SUB1_COLOR);
    	scrollButtPressedColor[3] = new ColorReference(new Color(126, 126, 126), 0, 0, ColorReference.BACK_COLOR);
    	
    	scrollButtDisabledColor[0] = new ColorReference(new Color(238, 238, 238), 0, 0, ColorReference.BACK_COLOR);
    	scrollButtDisabledColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	scrollButtDisabledColor[2] = new ColorReference(new Color(238, 237, 231), -48, 29, ColorReference.BACK_COLOR);
    	scrollButtDisabledColor[3] = new ColorReference(new Color(238, 238, 238), 0, 0, ColorReference.BACK_COLOR);
    	
    	scrollSpreadLight[0] = 20;
		scrollSpreadLight[1] = 0;
		scrollSpreadLight[2] = 20;
		scrollSpreadLight[3] = 20;
		
		scrollSpreadDark[0] = 2;
		scrollSpreadDark[1] = 0;
		scrollSpreadDark[2] = 2;
		scrollSpreadDark[3] = 2;

		scrollSpreadLightDisabled[0] = 20;
		scrollSpreadLightDisabled[1] = 0;
		scrollSpreadLightDisabled[2] = 20;
		scrollSpreadLightDisabled[3] = 20;
		
		scrollSpreadDarkDisabled[0] = 1;
		scrollSpreadDarkDisabled[1] = 0;
		scrollSpreadDarkDisabled[2] = 1;
		scrollSpreadDarkDisabled[3] = 1;
    	
    	// Arrow
    	scrollArrowColor[0] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	scrollArrowColor[1] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	scrollArrowColor[2] = new ColorReference(new Color(77, 100, 132), -74, -18, ColorReference.MAIN_COLOR);
    	scrollArrowColor[3] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	
    	scrollArrowDisabledColor[0] = new ColorReference(new Color(136, 136, 136), 0, 0, ColorReference.BACK_COLOR);
    	scrollArrowDisabledColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
    	scrollArrowDisabledColor[2] = new ColorReference(new Color(193, 193, 193), -100, -15, ColorReference.BACK_COLOR);
		scrollArrowDisabledColor[3] = new ColorReference(new Color(136, 136, 136), 0, 0, ColorReference.BACK_COLOR);
		
    	// Border
    	scrollBorderColor[0] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
    	scrollBorderColor[1] = new ColorReference(new Color(64, 64, 64), -100, -69, ColorReference.BACK_COLOR);
    	scrollBorderColor[2] = new ColorReference(new Color(212, 210, 194), 0, -10, ColorReference.SUB1_COLOR);
    	scrollBorderColor[3] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
    	
    	scrollDarkColor[0] = new ColorReference(new Color(119, 119, 119), 0, 0, ColorReference.BACK_COLOR);
    	scrollDarkColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
    	scrollDarkColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
    	scrollDarkColor[3] = new ColorReference(new Color(119, 119, 119), 0, 0, ColorReference.BACK_COLOR);
    	
    	scrollLightColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	scrollLightColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	scrollLightColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.SUB1_COLOR);
    	scrollLightColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	
    	scrollBorderDisabledColor[0] = new ColorReference(new Color(201, 198, 184), 0, 0, ColorReference.BACK_COLOR);
    	scrollBorderDisabledColor[1] = new ColorReference(new Color(180, 177, 170), 0, -15, ColorReference.BACK_COLOR);
    	scrollBorderDisabledColor[2] = new ColorReference(new Color(232, 230, 220), -41, 0, ColorReference.BACK_COLOR);
    	scrollBorderDisabledColor[3] = new ColorReference(new Color(201, 198, 184), 0, 0, ColorReference.BACK_COLOR);
    	
    	scrollDarkDisabledColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	scrollDarkDisabledColor[1] = new ColorReference(new Color(180, 177, 170), 0, -15, ColorReference.BACK_COLOR);
    	scrollDarkDisabledColor[2] = new ColorReference(new Color(232, 230, 220), -41, 0, ColorReference.BACK_COLOR);
    	scrollDarkDisabledColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	scrollLightDisabledColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	scrollLightDisabledColor[1] = new ColorReference(new Color(240, 239, 236), 0, 66, ColorReference.BACK_COLOR);
    	scrollLightDisabledColor[2] = new ColorReference(new Color(232, 230, 220), -41, 0, ColorReference.BACK_COLOR);
    	scrollLightDisabledColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	// ScrollPane border
    	scrollPaneBorderColor[0] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
    	scrollPaneBorderColor[1] = new ColorReference(new Color(191, 187, 188), 0, -10, ColorReference.BACK_COLOR);
    	scrollPaneBorderColor[2] = new ColorReference(new Color(201, 198, 184), 0, -15, ColorReference.BACK_COLOR);
    	scrollPaneBorderColor[3] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
    	
// Tabbed
    	tabPaneBorderColor[0] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
		tabPaneBorderColor[1] = new ColorReference(new Color(64, 64, 64), -100, -69, ColorReference.BACK_COLOR);
		tabPaneBorderColor[2] = new ColorReference(new Color(143, 160, 183), -78, 28, ColorReference.MAIN_COLOR);
		tabPaneBorderColor[3] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
		
		tabPaneDarkColor[0] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
		tabPaneDarkColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
		tabPaneDarkColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
		tabPaneDarkColor[3] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
		
		tabPaneLightColor[0] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
		tabPaneLightColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		tabPaneLightColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
		tabPaneLightColor[3] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
		
    	tabNormalColor[0] = new ColorReference(new Color(242, 240, 238), 0, 0, ColorReference.BACK_COLOR);
    	tabNormalColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	tabNormalColor[2] = new ColorReference(new Color(242, 240, 238), 0, 69, ColorReference.BACK_COLOR);
    	tabNormalColor[3] = new ColorReference(new Color(242, 240, 238), 0, 0, ColorReference.BACK_COLOR);
    	
    	tabSelectedColor[0] = new ColorReference(new Color(251, 251, 250), 0, 0, ColorReference.BACK_COLOR);
    	tabSelectedColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	tabSelectedColor[2] = new ColorReference(new Color(251, 251, 250), 0, 91, ColorReference.BACK_COLOR);
    	tabSelectedColor[3] = new ColorReference(new Color(251, 251, 250), 0, 0, ColorReference.BACK_COLOR);
    	
    	// since 1.3
    	tabDisabledColor[0] = new ColorReference(new Color(242, 240, 238), 0, 0, ColorReference.BACK_COLOR);
    	tabDisabledColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	tabDisabledColor[2] = new ColorReference(new Color(244, 242, 232), 0, 40, ColorReference.BACK_COLOR);
    	tabDisabledColor[3] = new ColorReference(new Color(242, 240, 238), 0, 0, ColorReference.BACK_COLOR);

    	tabDisabledSelectedColor[0] = new ColorReference(new Color(251, 251, 250), 0, 0, ColorReference.BACK_COLOR);
    	tabDisabledSelectedColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	tabDisabledSelectedColor[2] = new ColorReference(new Color(251, 251, 247), 0, 80, ColorReference.BACK_COLOR);
    	tabDisabledSelectedColor[3] = new ColorReference(new Color(251, 251, 250), 0, 0, ColorReference.BACK_COLOR);
    	
    	tabDisabledTextColor[0] = new ColorReference(new Color(242, 240, 238), 0, 0, ColorReference.BACK_COLOR);
    	tabDisabledTextColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	tabDisabledTextColor[2] = new ColorReference(new Color(188, 187, 185), 0, 40, ColorReference.DIS_COLOR);
    	tabDisabledTextColor[3] = new ColorReference(new Color(242, 240, 238), 0, 0, ColorReference.BACK_COLOR);
    	// end since 1.3
    	
    	tabBorderColor[0] = new ColorReference(new Color(143, 160, 183), 0, 0, ColorReference.BACK_COLOR);
    	tabBorderColor[1] = new ColorReference(new Color(64, 64, 64), -100, -69, ColorReference.BACK_COLOR);
    	tabBorderColor[2] = new ColorReference(new Color(143, 160, 183), -78, 28, ColorReference.MAIN_COLOR);
    	tabBorderColor[3] = new ColorReference(new Color(143, 160, 183), 0, 0, ColorReference.BACK_COLOR);
    	
    	tabDarkColor[0] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
		tabDarkColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
		tabDarkColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
		tabDarkColor[3] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
		
		tabLightColor[0] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
		tabLightColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		tabLightColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
		tabLightColor[3] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
    	
    	tabRolloverColor[0] = new ColorReference(new Color(255, 199, 59), 0, 0, ColorReference.BACK_COLOR);
    	tabRolloverColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	tabRolloverColor[2] = new ColorReference(new Color(255, 199, 59), 0, 0, ColorReference.SUB6_COLOR);
    	tabRolloverColor[3] = new ColorReference(new Color(255, 199, 59), 0, 0, ColorReference.BACK_COLOR);
    
    	firstTabDistance[0] = 2;
    	firstTabDistance[1] = 2;
    	firstTabDistance[2] = 2;
    	firstTabDistance[3] = 2;
    
    	tabRollover[0] = true;
    	tabRollover[1] = false;
    	tabRollover[2] = true;
    	tabRollover[3] = true;
    	
    	// since 1.3.05
    	tabFocus[0] = true;
    	tabFocus[1] = true;
    	tabFocus[2] = true;
    	tabFocus[3] = true;

    	ignoreSelectedBg[0] = false;
    	ignoreSelectedBg[1] = false;
    	ignoreSelectedBg[2] = false;
    	ignoreSelectedBg[3] = false;
    	
    	fixedTabs[0] = true;
    	fixedTabs[1] = true;
    	fixedTabs[2] = true;
    	fixedTabs[3] = true;

    	tabInsets[0] = new Insets(1, 6, 4, 6);
    	tabInsets[1] = new Insets(1, 6, 4, 6);
    	tabInsets[2] = new Insets(1, 6, 4, 6);
    	tabInsets[3] = new Insets(1, 6, 4, 6);
    	
    	tabAreaInsets[0] = new Insets(4, 2, 0, 0);
    	tabAreaInsets[1] = new Insets(4, 2, 0, 0);
    	tabAreaInsets[2] = new Insets(4, 2, 0, 0);
    	tabAreaInsets[3] = new Insets(4, 2, 0, 0);
    	    	
// Slider
    	sliderRolloverEnabled[0] = false;
    	sliderRolloverEnabled[1] = false;
    	sliderRolloverEnabled[2] = true;
    	sliderRolloverEnabled[3] = false;
    	
    	// since 1.3.05
    	sliderFocusEnabled[0] = true;
    	sliderFocusEnabled[1] = true;
    	sliderFocusEnabled[2] = true;
    	sliderFocusEnabled[3] = true;
    	
    	sliderVertSize[0] = new Dimension(16, 13);
		sliderHorzSize[0] = new Dimension(13, 16);
		sliderVertSize[1] = new Dimension(22, 11);
		sliderHorzSize[1] = new Dimension(11, 21);
		sliderVertSize[2] = new Dimension(22, 11);
		sliderHorzSize[2] = new Dimension(11, 22);
		sliderVertSize[3] = new Dimension(16, 13);
		sliderHorzSize[3] = new Dimension(13, 16);
		
		// Thumb
    	sliderThumbColor[0] = new ColorReference(new Color(153, 153, 255), 0, 0, ColorReference.BACK_COLOR);
    	sliderThumbColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	sliderThumbColor[2] = new ColorReference(new Color(244, 243, 239), -50, 50, ColorReference.BACK_COLOR);
    	sliderThumbColor[3] = new ColorReference(new Color(153, 153, 255), 0, 0, ColorReference.BACK_COLOR);
    	
    	sliderThumbRolloverColor[0] = new ColorReference(new Color(175, 175, 255), 0, 0, ColorReference.BACK_COLOR);
    	sliderThumbRolloverColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	sliderThumbRolloverColor[2] = new ColorReference(new Color(233, 166, 0), 100, -26, ColorReference.SUB6_COLOR);
    	sliderThumbRolloverColor[3] = new ColorReference(new Color(175, 175, 255), 0, 0, ColorReference.BACK_COLOR);
    	
    	sliderThumbPressedColor[0] = new ColorReference(new Color(122, 122, 204), 0, 0, ColorReference.BACK_COLOR);
    	sliderThumbPressedColor[1] = new ColorReference(new Color(234, 232, 228), 0, 50, ColorReference.BACK_COLOR);
    	sliderThumbPressedColor[2] = new ColorReference(new Color(244, 243, 239), -50, 50, ColorReference.BACK_COLOR);
    	sliderThumbPressedColor[3] = new ColorReference(new Color(122, 122, 204), 0, 0, ColorReference.BACK_COLOR);
    	
    	sliderThumbDisabledColor[0] = new ColorReference(new Color(212, 212, 212), 0, 0, ColorReference.BACK_COLOR);
    	sliderThumbDisabledColor[1] = new ColorReference(new Color(225, 222, 217), 0, 30, ColorReference.BACK_COLOR);
    	sliderThumbDisabledColor[2] = new ColorReference(new Color(244, 243, 233), 0, 44, ColorReference.BACK_COLOR);
    	sliderThumbDisabledColor[3] = new ColorReference(new Color(212, 212, 212), 0, 0, ColorReference.BACK_COLOR);
    	
    	// Border
    	sliderBorderColor[0] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
    	sliderBorderColor[1] = new ColorReference(new Color(64, 64, 64), -100, -69, ColorReference.BACK_COLOR);
    	sliderBorderColor[2] = new ColorReference(new Color(176, 189, 207), -76, 50, ColorReference.MAIN_COLOR);
    	sliderBorderColor[3] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
    	
    	sliderDarkColor[0] = new ColorReference(new Color(119, 119, 119), 0, 0, ColorReference.BACK_COLOR);
    	sliderDarkColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
    	sliderDarkColor[2] = new ColorReference(new Color(119, 130, 146), -89, 4, ColorReference.MAIN_COLOR);
    	sliderDarkColor[3] = new ColorReference(new Color(119, 119, 119), 0, 0, ColorReference.BACK_COLOR);
    	
    	sliderLightColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	sliderLightColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	sliderLightColor[2] = new ColorReference(new Color(27, 155, 27), 16, -7, ColorReference.SUB2_COLOR);
    	sliderLightColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	
    	sliderBorderDisabledColor[0] = new ColorReference(new Color(201, 198, 184), 0, 0, ColorReference.BACK_COLOR);
    	sliderBorderDisabledColor[1] = new ColorReference(new Color(180, 177, 170), 0, -15, ColorReference.BACK_COLOR);
    	sliderBorderDisabledColor[2] = new ColorReference(new Color(215, 212, 197), 0, -9, ColorReference.BACK_COLOR);
    	sliderBorderDisabledColor[3] = new ColorReference(new Color(201, 198, 184), 0, 0, ColorReference.BACK_COLOR);
    	
    	sliderDarkDisabledColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	sliderDarkDisabledColor[1] = new ColorReference(new Color(180, 177, 170), 0, -15, ColorReference.BACK_COLOR);
    	sliderDarkDisabledColor[2] = new ColorReference(new Color(38, 84, 149), -41, -27, ColorReference.BACK_COLOR);
    	sliderDarkDisabledColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	sliderLightDisabledColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	sliderLightDisabledColor[1] = new ColorReference(new Color(240, 239, 236), 0, 66, ColorReference.BACK_COLOR);
    	sliderLightDisabledColor[2] = new ColorReference(new Color(38, 84, 149), -41, -27, ColorReference.BACK_COLOR);
    	sliderLightDisabledColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	// Track
    	sliderTrackColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	sliderTrackColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	sliderTrackColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
    	sliderTrackColor[3] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
    	
    	sliderTrackBorderColor[0] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
    	sliderTrackBorderColor[1] = new ColorReference(new Color(64, 64, 64), -100, -69, ColorReference.BACK_COLOR);
    	sliderTrackBorderColor[2] = new ColorReference(new Color(157, 156, 150), -53, -32, ColorReference.BACK_COLOR);
    	sliderTrackBorderColor[3] = new ColorReference(new Color(157, 156, 150), -53, -32, ColorReference.BACK_COLOR);
    	
    	sliderTrackDarkColor[0] = new ColorReference(new Color(119, 119, 119), 0, 0, ColorReference.BACK_COLOR);
    	sliderTrackDarkColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
    	sliderTrackDarkColor[2] = new ColorReference(new Color(198, 196, 181), 0, -16, ColorReference.BACK_COLOR);
    	sliderTrackDarkColor[3] = new ColorReference(new Color(198, 196, 181), 0, -16, ColorReference.BACK_COLOR);
    	
    	sliderTrackLightColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	sliderTrackLightColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	sliderTrackLightColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	sliderTrackLightColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);

		// Ticks
    	sliderTickColor[0] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
    	sliderTickColor[1] = new ColorReference(new Color(106, 104, 100), 0, -50, ColorReference.BACK_COLOR);
    	sliderTickColor[2] = new ColorReference(new Color(118, 117, 108), 0, -50, ColorReference.BACK_COLOR);
    	sliderTickColor[3] = new ColorReference(new Color(157, 156, 150), -53, -32, ColorReference.BACK_COLOR);
    	
    	sliderTickDisabledColor[0] = new ColorReference(new Color(119, 119, 119), 0, 0, ColorReference.BACK_COLOR);
    	sliderTickDisabledColor[1] = new ColorReference(new Color(162, 160, 159), 0, 17, ColorReference.DIS_COLOR);
    	sliderTickDisabledColor[2] = new ColorReference(new Color(174, 174, 171), 0, 28, ColorReference.DIS_COLOR);
    	sliderTickDisabledColor[3] = new ColorReference(new Color(198, 196, 181), 0, -16, ColorReference.BACK_COLOR);
    	
    	// since 1.3.05
    	sliderFocusColor[0] = new ColorReference(new Color(113, 112, 104), 0, -52, ColorReference.BACK_COLOR);
    	sliderFocusColor[1] = new ColorReference(new Color(113, 112, 104), 0, -52, ColorReference.BACK_COLOR);
    	sliderFocusColor[2] = new ColorReference(new Color(113, 112, 104), 0, -52, ColorReference.BACK_COLOR);
    	sliderFocusColor[3] = new ColorReference(new Color(113, 112, 104), 0, -52, ColorReference.BACK_COLOR);
    	    	
// Spinner
    	spinnerRollover[0] = false;
    	spinnerRollover[1] = false;
    	spinnerRollover[2] = false;
    	spinnerRollover[3] = false;

		// Button
    	spinnerButtColor[0] = new ColorReference(new Color(153, 153, 255), 0, 0, ColorReference.BACK_COLOR);
    	spinnerButtColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	spinnerButtColor[2] = new ColorReference(new Color(198, 213, 250), 0, 0, ColorReference.SUB1_COLOR);
    	spinnerButtColor[3] = new ColorReference(new Color(153, 153, 255), 0, 0, ColorReference.BACK_COLOR);
    	
    	spinnerButtRolloverColor[0] = new ColorReference(new Color(175, 175, 255), 0, 0, ColorReference.BACK_COLOR);
    	spinnerButtRolloverColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	spinnerButtRolloverColor[2] = new ColorReference(new Color(232, 238, 254), 0, 60, ColorReference.SUB1_COLOR);
    	spinnerButtRolloverColor[3] = new ColorReference(new Color(175, 175, 255), 0, 0, ColorReference.BACK_COLOR);
    	
    	spinnerButtPressedColor[0] = new ColorReference(new Color(122, 122, 204), 0, 0, ColorReference.BACK_COLOR);
    	spinnerButtPressedColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	spinnerButtPressedColor[2] = new ColorReference(new Color(175, 190, 224), 0, -11, ColorReference.SUB1_COLOR);
    	spinnerButtPressedColor[3] = new ColorReference(new Color(122, 122, 204), 0, 0, ColorReference.BACK_COLOR);
    	
    	spinnerButtDisabledColor[0] = new ColorReference(new Color(212, 212, 212), 0, 0, ColorReference.BACK_COLOR);
    	spinnerButtDisabledColor[1] = new ColorReference(new Color(225, 222, 217), 0, 30, ColorReference.BACK_COLOR);
    	spinnerButtDisabledColor[2] = new ColorReference(new Color(242, 240, 228), 0, 30, ColorReference.BACK_COLOR);
    	spinnerButtDisabledColor[3] = new ColorReference(new Color(212, 212, 212), 0, 0, ColorReference.BACK_COLOR);
    	
    	spinnerSpreadLight[0] = 20;
		spinnerSpreadLight[1] = 0;
		spinnerSpreadLight[2] = 20;
		spinnerSpreadLight[3] = 20;
		
		spinnerSpreadDark[0] = 3;
		spinnerSpreadDark[1] = 0;
		spinnerSpreadDark[2] = 3;
		spinnerSpreadDark[3] = 3;
		
		spinnerSpreadLightDisabled[0] = 20;
		spinnerSpreadLightDisabled[1] = 0;
		spinnerSpreadLightDisabled[2] = 20;
		spinnerSpreadLightDisabled[3] = 20;
		
		spinnerSpreadDarkDisabled[0] = 1;
		spinnerSpreadDarkDisabled[1] = 0;
		spinnerSpreadDarkDisabled[2] = 1;
		spinnerSpreadDarkDisabled[3] = 1;
    	
    	spinnerBorderColor[0] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
    	spinnerBorderColor[1] = new ColorReference(new Color(64, 64, 64), -100, -69, ColorReference.BACK_COLOR);
    	spinnerBorderColor[2] = new ColorReference(new Color(150, 173, 227), 36, -16, ColorReference.SUB1_COLOR);
    	spinnerBorderColor[3] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
    	
    	spinnerDarkColor[0] = new ColorReference(new Color(119, 119, 119), 0, 0, ColorReference.BACK_COLOR);
    	spinnerDarkColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
    	spinnerDarkColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
    	spinnerDarkColor[3] = new ColorReference(new Color(119, 119, 119), 0, 0, ColorReference.BACK_COLOR);
    	
    	spinnerLightColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	spinnerLightColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	spinnerLightColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
    	spinnerLightColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	
    	spinnerBorderDisabledColor[0] = new ColorReference(new Color(201, 198, 184), 0, 0, ColorReference.BACK_COLOR);
    	spinnerBorderDisabledColor[1] = new ColorReference(new Color(180, 177, 170), 0, -15, ColorReference.BACK_COLOR);
    	spinnerBorderDisabledColor[2] = new ColorReference(new Color(215, 212, 197), 0, -9, ColorReference.BACK_COLOR);
    	spinnerBorderDisabledColor[3] = new ColorReference(new Color(201, 198, 184), 0, 0, ColorReference.BACK_COLOR);
    	
    	spinnerDarkDisabledColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	spinnerDarkDisabledColor[1] = new ColorReference(new Color(180, 177, 170), 0, -15, ColorReference.BACK_COLOR);
    	spinnerDarkDisabledColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
    	spinnerDarkDisabledColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	spinnerLightDisabledColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	spinnerLightDisabledColor[1] = new ColorReference(new Color(240, 239, 236), 0, 66, ColorReference.BACK_COLOR);
    	spinnerLightDisabledColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
    	spinnerLightDisabledColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	spinnerArrowColor[0] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	spinnerArrowColor[1] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	spinnerArrowColor[2] = new ColorReference(new Color(77, 100, 132), -74, -18, ColorReference.MAIN_COLOR);
    	spinnerArrowColor[3] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	
    	spinnerArrowDisabledColor[0] = new ColorReference(new Color(136, 136, 136), 0, 0, ColorReference.BACK_COLOR);
    	spinnerArrowDisabledColor[1] = new ColorReference(new Color(195, 191, 184), 0, -8, ColorReference.BACK_COLOR);
    	spinnerArrowDisabledColor[2] = new ColorReference(new Color(212, 210, 194), 0, -10, ColorReference.BACK_COLOR);
    	spinnerArrowDisabledColor[3] = new ColorReference(new Color(136, 136, 136), 0, 0, ColorReference.BACK_COLOR);
    	
// Combo
    	comboBorderColor[0] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
    	comboBorderColor[1] = new ColorReference(new Color(64, 64, 64), -100, -69, ColorReference.BACK_COLOR);
    	comboBorderColor[2] = new ColorReference(new Color(128, 152, 186), -70, 23, ColorReference.MAIN_COLOR);
    	comboBorderColor[3] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);

    	comboDarkColor[0] = new ColorReference(new Color(137, 137, 137), 0, 0, ColorReference.BACK_COLOR);
  		comboDarkColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
  		comboDarkColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
  		comboDarkColor[3] = new ColorReference(new Color(137, 137, 137), 0, 0, ColorReference.BACK_COLOR);
    	
    	comboLightColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
  		comboLightColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
  		comboLightColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
  		comboLightColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	
		comboBorderDisabledColor[0] = new ColorReference(new Color(200, 199, 193), 0, 0, ColorReference.BACK_COLOR);
		comboBorderDisabledColor[1] = new ColorReference(new Color(188, 186, 183), -60, -13, ColorReference.BACK_COLOR);
		comboBorderDisabledColor[2] = new ColorReference(new Color(201, 198, 184), 0, -15, ColorReference.BACK_COLOR);
		comboBorderDisabledColor[3] = new ColorReference(new Color(200, 199, 193), 0, 0, ColorReference.BACK_COLOR);
    	
    	comboDarkDisabledColor[0] = new ColorReference(new Color(210, 210, 210), 0, 0, ColorReference.BACK_COLOR);
		comboDarkDisabledColor[1] = new ColorReference(new Color(208, 207, 204), -60, 0, ColorReference.BACK_COLOR);
		comboDarkDisabledColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
		comboDarkDisabledColor[3] = new ColorReference(new Color(210, 210, 210), 0, 0, ColorReference.BACK_COLOR);
    	
    	comboLightDisabledColor[0] = new ColorReference(new Color(228, 228, 228), 0, 0, ColorReference.BACK_COLOR);
		comboLightDisabledColor[1] = new ColorReference(new Color(208, 207, 204), -60, 0, ColorReference.BACK_COLOR);
		comboLightDisabledColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
		comboLightDisabledColor[3] = new ColorReference(new Color(228, 228, 228), 0, 0, ColorReference.BACK_COLOR);
		
		comboSelectedBgColor[0] = new ColorReference(new Color(51, 51, 154), 0, 0, ColorReference.BACK_COLOR);
		comboSelectedBgColor[1] = new ColorReference(new Color(10, 50, 105), 0, 0, ColorReference.SUB1_COLOR);
		comboSelectedBgColor[2] = new ColorReference(new Color(43, 107, 197), -36, -6, ColorReference.MAIN_COLOR);
		comboSelectedBgColor[3] = new ColorReference(new Color(51, 51, 154), 0, 0, ColorReference.BACK_COLOR);
		
		comboSelectedTextColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		comboSelectedTextColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		comboSelectedTextColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		comboSelectedTextColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		
		comboFocusBgColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.ABS_COLOR);
		comboFocusBgColor[1] = new ColorReference(new Color(10, 50, 105), 0, 0, ColorReference.SUB1_COLOR);
		comboFocusBgColor[2] = new ColorReference(new Color(43, 107, 197), 0, 0, ColorReference.ABS_COLOR);
		comboFocusBgColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.ABS_COLOR);
		
		comboBgColor[0] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.BACK_COLOR);
		comboBgColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		comboBgColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		comboBgColor[3] = new ColorReference(new Color(255, 255, 255), 0, 0, ColorReference.BACK_COLOR);

    	comboTextColor[0] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	comboTextColor[1] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	comboTextColor[2] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	comboTextColor[3] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		
		// Button
		comboButtColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	comboButtColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	comboButtColor[2] = new ColorReference(new Color(197, 213, 252), 0, 0, ColorReference.SUB1_COLOR);
    	comboButtColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	comboButtRolloverColor[0] = new ColorReference(new Color(243, 243, 243), 0, 0, ColorReference.BACK_COLOR);
    	comboButtRolloverColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	comboButtRolloverColor[2] = new ColorReference(new Color(226, 234, 254), 0, 50, ColorReference.SUB1_COLOR);
    	comboButtRolloverColor[3] = new ColorReference(new Color(243, 243, 243), 0, 0, ColorReference.BACK_COLOR);
    	
    	comboButtPressedColor[0] = new ColorReference(new Color(126, 126, 126), 0, 0, ColorReference.BACK_COLOR);
    	comboButtPressedColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	comboButtPressedColor[2] = new ColorReference(new Color(175, 190, 224), 0, -11, ColorReference.SUB1_COLOR);
    	comboButtPressedColor[3] = new ColorReference(new Color(126, 126, 126), 0, 0, ColorReference.BACK_COLOR);
    	
    	comboButtDisabledColor[0] = new ColorReference(new Color(238, 238, 238), 0, 0, ColorReference.BACK_COLOR);
    	comboButtDisabledColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	comboButtDisabledColor[2] = new ColorReference(new Color(238, 237, 231), -48, 29, ColorReference.BACK_COLOR);
    	comboButtDisabledColor[3] = new ColorReference(new Color(238, 238, 238), 0, 0, ColorReference.BACK_COLOR);
    	
    	comboSpreadLight[0] = 20;
		comboSpreadLight[1] = 0;
		comboSpreadLight[2] = 20;
		comboSpreadLight[3] = 20;
		
		comboSpreadDark[0] = 3;
		comboSpreadDark[1] = 0;
		comboSpreadDark[2] = 3;
		comboSpreadDark[3] = 3;
		
		comboSpreadLightDisabled[0] = 20;
		comboSpreadLightDisabled[1] = 0;
		comboSpreadLightDisabled[2] = 20;
		comboSpreadLightDisabled[3] = 20;
		
		comboSpreadDarkDisabled[0] = 1;
		comboSpreadDarkDisabled[1] = 0;
		comboSpreadDarkDisabled[2] = 1;
		comboSpreadDarkDisabled[3] = 1;

    	// Button Border
    	comboButtBorderColor[0] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
    	comboButtBorderColor[1] = new ColorReference(new Color(64, 64, 64), -100, -69, ColorReference.BACK_COLOR);
    	comboButtBorderColor[2] = new ColorReference(new Color(212, 210, 194), 0, -10, ColorReference.SUB1_COLOR);
    	comboButtBorderColor[3] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
    	
    	comboButtDarkColor[0] = new ColorReference(new Color(119, 119, 119), 0, 0, ColorReference.BACK_COLOR);
    	comboButtDarkColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
    	comboButtDarkColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
    	comboButtDarkColor[3] = new ColorReference(new Color(119, 119, 119), 0, 0, ColorReference.BACK_COLOR);
    	
    	comboButtLightColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	comboButtLightColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	comboButtLightColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
    	comboButtLightColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	
    	comboButtBorderDisabledColor[0] = new ColorReference(new Color(201, 198, 184), 0, 0, ColorReference.BACK_COLOR);
    	comboButtBorderDisabledColor[1] = new ColorReference(new Color(180, 177, 170), 0, -15, ColorReference.BACK_COLOR);
    	comboButtBorderDisabledColor[2] = new ColorReference(new Color(232, 230, 220), -41, 0, ColorReference.BACK_COLOR);
    	comboButtBorderDisabledColor[3] = new ColorReference(new Color(201, 198, 184), 0, 0, ColorReference.BACK_COLOR);
    	
    	comboButtDarkDisabledColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	comboButtDarkDisabledColor[1] = new ColorReference(new Color(180, 177, 170), 0, -15, ColorReference.BACK_COLOR);
    	comboButtDarkDisabledColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
    	comboButtDarkDisabledColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	comboButtLightDisabledColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	comboButtLightDisabledColor[1] = new ColorReference(new Color(240, 239, 236), 0, 66, ColorReference.BACK_COLOR);
    	comboButtLightDisabledColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
    	comboButtLightDisabledColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
		
		// Arrow
		comboArrowColor[0] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	comboArrowColor[1] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	comboArrowColor[2] = new ColorReference(new Color(77, 100, 132), -74, -18, ColorReference.MAIN_COLOR);
    	comboArrowColor[3] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	
    	comboArrowDisabledColor[0] = new ColorReference(new Color(136, 136, 136), 0, 0, ColorReference.BACK_COLOR);
    	comboArrowDisabledColor[1] = new ColorReference(new Color(182, 179, 172), 0, -14, ColorReference.BACK_COLOR);
    	comboArrowDisabledColor[2] = new ColorReference(new Color(203, 200, 186), 0, -14, ColorReference.BACK_COLOR);
    	comboArrowDisabledColor[3] = new ColorReference(new Color(136, 136, 136), 0, 0, ColorReference.BACK_COLOR);
		
		comboButtonWidth[0] = 22;
		comboButtonWidth[1] = 18;
		comboButtonWidth[2] = 18;
		comboButtonWidth[3] = 22;

    	comboInsets[0] = new Insets(2, 2, 2, 2);
    	comboInsets[1] = new Insets(2, 2, 2, 2);
    	comboInsets[2] = new Insets(2, 2, 2, 2);
    	comboInsets[3] = new Insets(2, 2, 2, 2);
    
    	comboRollover[0] = false;
    	comboRollover[1] = false;
    	comboRollover[2] = false;
    	comboRollover[3] = false;
    	
    	comboFocus[0] = false;
    	comboFocus[1] = false;
    	comboFocus[2] = false;
    	comboFocus[3] = false;
    	
// Menu
    	menuBarColor[0] = new ColorReference(new Color(189, 208, 234), 0, 0, ColorReference.BACK_COLOR);
    	menuBarColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	menuBarColor[2] = new ColorReference(new Color(238, 237, 230), -43, 28, ColorReference.BACK_COLOR);
    	menuBarColor[3] = new ColorReference(new Color(189, 208, 234), 0, 0, ColorReference.BACK_COLOR);
    	
    	menuSelectedTextColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	menuSelectedTextColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	menuSelectedTextColor[2] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	menuSelectedTextColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	
    	menuPopupColor[0] = new ColorReference(new Color(189, 208, 234), 0, 0, ColorReference.BACK_COLOR);
    	menuPopupColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	menuPopupColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	menuPopupColor[3] = new ColorReference(new Color(189, 208, 234), 0, 0, ColorReference.BACK_COLOR);
    
    	menuRolloverBgColor[0] = new ColorReference(new Color(189, 208, 234), -50, 66, ColorReference.MAIN_COLOR);
    	menuRolloverBgColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	menuRolloverBgColor[2] = new ColorReference(new Color(189, 208, 234), -50, 66, ColorReference.MAIN_COLOR);
    	menuRolloverBgColor[3] = new ColorReference(new Color(189, 208, 234), -50, 66, ColorReference.MAIN_COLOR);
    	
    	menuRolloverFgColor[0] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	menuRolloverFgColor[1] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	menuRolloverFgColor[2] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	menuRolloverFgColor[3] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	
    	menuDisabledFgColor[0] = new ColorReference(new Color(143, 141, 139), 0, 0, ColorReference.DIS_COLOR);
    	menuDisabledFgColor[1] = new ColorReference(new Color(143, 141, 139), 0, 0, ColorReference.DIS_COLOR);
    	menuDisabledFgColor[2] = new ColorReference(new Color(143, 142, 139), 0, 0, ColorReference.DIS_COLOR);
    	menuDisabledFgColor[3] = new ColorReference(new Color(143, 141, 139), 0, 0, ColorReference.DIS_COLOR);
    	
    	menuItemRolloverColor[0] = new ColorReference(new Color(189, 208, 234), 0, 0, ColorReference.BACK_COLOR);
    	menuItemRolloverColor[1] = new ColorReference(new Color(10, 50, 105), 0, 0, ColorReference.SUB1_COLOR);
    	menuItemRolloverColor[2] = new ColorReference(new Color(189, 208, 234), -50, 66, ColorReference.MAIN_COLOR);
    	menuItemRolloverColor[3] = new ColorReference(new Color(189, 208, 234), 0, 0, ColorReference.BACK_COLOR);
		
		menuBorderColor[0] = new ColorReference(new Color(167, 167, 163), 0, 0, ColorReference.BACK_COLOR);
		menuBorderColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
		menuBorderColor[2] = new ColorReference(new Color(173, 170, 153), 4, -28, ColorReference.BACK_COLOR);
		menuBorderColor[3] = new ColorReference(new Color(167, 167, 163), 0, 0, ColorReference.BACK_COLOR);
		
		menuDarkColor[0] = new ColorReference(new Color(167, 167, 163), 0, 0, ColorReference.BACK_COLOR);
		menuDarkColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
		menuDarkColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
		menuDarkColor[3] = new ColorReference(new Color(167, 167, 163), 0, 0, ColorReference.BACK_COLOR);
		
		menuLightColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		menuLightColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		menuLightColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
		menuLightColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		
		menuInnerHilightColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		menuInnerHilightColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		menuInnerHilightColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		menuInnerHilightColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		
		menuInnerShadowColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		menuInnerShadowColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
		menuInnerShadowColor[2] = new ColorReference(new Color(213, 212, 207), -70, -7, ColorReference.BACK_COLOR);
		menuInnerShadowColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		
		menuOuterHilightColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		menuOuterHilightColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
		menuOuterHilightColor[2] = new ColorReference(new Color(173, 170, 153), 4, -28, ColorReference.BACK_COLOR);
		menuOuterHilightColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		
		menuOuterShadowColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		menuOuterShadowColor[1] = new ColorReference(new Color(64, 64, 64), -100, -69, ColorReference.BACK_COLOR);
		menuOuterShadowColor[2] = new ColorReference(new Color(173, 170, 153), 4, -28, ColorReference.BACK_COLOR);
		menuOuterShadowColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		
		menuIconColor[0] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		menuIconColor[1] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		menuIconColor[2] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		menuIconColor[3] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		
		menuIconRolloverColor[0] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		menuIconRolloverColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		menuIconRolloverColor[2] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		menuIconRolloverColor[3] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		
		menuIconDisabledColor[0] = new ColorReference(new Color(128, 128, 128), 0, 50, ColorReference.BACK_COLOR);
		menuIconDisabledColor[1] = new ColorReference(new Color(165, 162, 156), 0, -22, ColorReference.BACK_COLOR);
		menuIconDisabledColor[2] = new ColorReference(new Color(165, 163, 151), 0, -30, ColorReference.BACK_COLOR);
		menuIconDisabledColor[3] = new ColorReference(new Color(128, 128, 128), 0, 50, ColorReference.BACK_COLOR);
		
		menuIconShadowColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		menuIconShadowColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		menuIconShadowColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
		menuIconShadowColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		
		menuSepDarkColor[0] = new ColorReference(new Color(167, 167, 163), 0, 0, ColorReference.BACK_COLOR);
		menuSepDarkColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
		menuSepDarkColor[2] = new ColorReference(new Color(173, 170, 153), 4, -28, ColorReference.BACK_COLOR);
		menuSepDarkColor[3] = new ColorReference(new Color(167, 167, 163), 0, 0, ColorReference.BACK_COLOR);
		
		menuSepLightColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		menuSepLightColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		menuSepLightColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
		menuSepLightColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	
    	menuSeparatorHeight[0] = 3;
    	menuSeparatorHeight[1] = 4;
    	menuSeparatorHeight[2] = 3;
    	menuSeparatorHeight[3] = 3;

    	menuBorderInsets[0] = new Insets(2, 2, 2, 2);
    	menuBorderInsets[1] = new Insets(3, 3, 3, 3);
    	menuBorderInsets[2] = new Insets(2, 2, 2, 2);
    	menuBorderInsets[3] = new Insets(2, 2, 2, 2);
    	
    	menuRollover[0] = true;
    	menuRollover[1] = true;
    	menuRollover[2] = true;
    	menuRollover[3] = true;
    	
// Toolbar
		toolBarColor[0] = new ColorReference(new Color(189, 208, 234), 0, 0, ColorReference.BACK_COLOR);
    	toolBarColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	toolBarColor[2] = new ColorReference(new Color(239, 237, 229), -35, 28, ColorReference.BACK_COLOR);
    	toolBarColor[3] = new ColorReference(new Color(189, 208, 234), 0, 0, ColorReference.BACK_COLOR);
    	
    	toolBarLightColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	toolBarLightColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	toolBarLightColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	toolBarLightColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	
    	toolBarDarkColor[0] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	toolBarDarkColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
    	toolBarDarkColor[2] = new ColorReference(new Color(214, 210, 187), 10, -11, ColorReference.BACK_COLOR);
    	toolBarDarkColor[3] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);

		toolButtColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	toolButtColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	toolButtColor[2] = new ColorReference(new Color(239, 237, 229), -35, 28, ColorReference.BACK_COLOR);
    	toolButtColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
		toolButtSelectedColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	toolButtSelectedColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	toolButtSelectedColor[2] = new ColorReference(new Color(243, 242, 239), -51, 52, ColorReference.BACK_COLOR);
    	toolButtSelectedColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	toolButtRolloverColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	toolButtRolloverColor[1] = new ColorReference(new Color(234, 232, 228), 0, 50, ColorReference.BACK_COLOR);
    	toolButtRolloverColor[2] = new ColorReference(new Color(251, 251, 248), -30, 81, ColorReference.BACK_COLOR);
    	toolButtRolloverColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	toolButtPressedColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	toolButtPressedColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	toolButtPressedColor[2] = new ColorReference(new Color(225, 224, 218), -58, -2, ColorReference.BACK_COLOR);
    	toolButtPressedColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	toolBorderDarkColor[0] = new ColorReference(new Color(167, 167, 163), 0, 0, ColorReference.BACK_COLOR);
		toolBorderDarkColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
		toolBorderDarkColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
		toolBorderDarkColor[3] = new ColorReference(new Color(167, 167, 163), 0, 0, ColorReference.BACK_COLOR);
		
		toolBorderLightColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	toolBorderLightColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	toolBorderLightColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
    	toolBorderLightColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	
    	toolGripDarkColor[0] = new ColorReference(new Color(167, 167, 163), 0, 0, ColorReference.BACK_COLOR);
		toolGripDarkColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
		toolGripDarkColor[2] = new ColorReference(new Color(167, 167, 163), -70, -27, ColorReference.BACK_COLOR);
		toolGripDarkColor[3] = new ColorReference(new Color(167, 167, 163), 0, 0, ColorReference.BACK_COLOR);
		
		toolGripLightColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	toolGripLightColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	toolGripLightColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	toolGripLightColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	
    	toolSepDarkColor[0] = new ColorReference(new Color(167, 167, 163), 0, 0, ColorReference.BACK_COLOR);
		toolSepDarkColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
		toolSepDarkColor[2] = new ColorReference(new Color(167, 167, 163), -70, -27, ColorReference.BACK_COLOR);
		toolSepDarkColor[3] = new ColorReference(new Color(167, 167, 163), 0, 0, ColorReference.BACK_COLOR);
		
		toolSepLightColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	toolSepLightColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	toolSepLightColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
    	toolSepLightColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	
    	toolBorderColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	toolBorderColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	toolBorderColor[2] = new ColorReference(new Color(239, 237, 229), -35, 28, ColorReference.BACK_COLOR);
    	toolBorderColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);

    	toolBorderPressedColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	toolBorderPressedColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	toolBorderPressedColor[2] = new ColorReference(new Color(122, 144, 174), -76, 16, ColorReference.MAIN_COLOR);
    	toolBorderPressedColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	
    	toolBorderRolloverColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	toolBorderRolloverColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	toolBorderRolloverColor[2] = new ColorReference(new Color(122, 144, 174), -76, 16, ColorReference.MAIN_COLOR);
    	toolBorderRolloverColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	
    	toolBorderSelectedColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	toolBorderSelectedColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	toolBorderSelectedColor[2] = new ColorReference(new Color(122, 144, 174), -76, 16, ColorReference.MAIN_COLOR);
    	toolBorderSelectedColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	
    	toolMarginTop[0] = 5;
    	toolMarginLeft[0] = 5;
    	toolMarginBottom[0] = 5;
    	toolMarginRight[0] = 5;
    	
    	toolMarginTop[1] = 5;
    	toolMarginLeft[1] = 5;
    	toolMarginBottom[1] = 5;
    	toolMarginRight[1] = 5;
    	
    	toolMarginTop[2] = 5;
    	toolMarginLeft[2] = 5;
    	toolMarginBottom[2] = 5;
    	toolMarginRight[2] = 5;
    	
    	toolMarginTop[3] = 5;
    	toolMarginLeft[3] = 5;
    	toolMarginBottom[3] = 5;
    	toolMarginRight[3] = 5;
		
		toolFocus[0] = false;
		toolFocus[1] = false;
		toolFocus[2] = false;
		toolFocus[3] = false;
		
		// (!) not adjustable
		toolRollover[0] = true;
		toolRollover[1] = false;
		toolRollover[2] = true;
		toolRollover[3] = true;
    	
// List
		listBgColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	listBgColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	listBgColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	listBgColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	
		listTextColor[0] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		listTextColor[1] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		listTextColor[2] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		listTextColor[3] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		
    	listSelectedBgColor[0] = new ColorReference(new Color(51, 51, 154), 0, 0, ColorReference.BACK_COLOR);
    	listSelectedBgColor[1] = new ColorReference(new Color(10, 50, 105), 0, 0, ColorReference.SUB1_COLOR);
    	listSelectedBgColor[2] = new ColorReference(new Color(43, 107, 197), -36, -6, ColorReference.MAIN_COLOR);
    	listSelectedBgColor[3] = new ColorReference(new Color(51, 51, 154), 0, 0, ColorReference.BACK_COLOR);
    	
		listSelectedTextColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		listSelectedTextColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		listSelectedTextColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		listSelectedTextColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
	
// Tree
    	treeBgColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	treeBgColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	treeBgColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	treeBgColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);

    	treeTextColor[0] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	treeTextColor[1] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	treeTextColor[2] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	treeTextColor[3] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	
    	treeTextBgColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	treeTextBgColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	treeTextBgColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	treeTextBgColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	
    	treeSelectedTextColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	treeSelectedTextColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	treeSelectedTextColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	treeSelectedTextColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);

    	treeSelectedBgColor[0] = new ColorReference(new Color(51, 51, 154), 0, 0, ColorReference.BACK_COLOR);
    	treeSelectedBgColor[1] = new ColorReference(new Color(10, 50, 105), 0, 0, ColorReference.SUB1_COLOR);
    	treeSelectedBgColor[2] = new ColorReference(new Color(43, 107, 197), -36, -6, ColorReference.MAIN_COLOR);
    	treeSelectedBgColor[3] = new ColorReference(new Color(51, 51, 154), 0, 0, ColorReference.BACK_COLOR);
    	
    	treeLineColor[0] = new ColorReference(new Color(208, 205, 190), 0, -12, ColorReference.BACK_COLOR);
    	treeLineColor[1] = new ColorReference(new Color(187, 183, 176), 0, -12, ColorReference.BACK_COLOR);
    	treeLineColor[2] = new ColorReference(new Color(208, 205, 190), 0, -12, ColorReference.BACK_COLOR);
    	treeLineColor[3] = new ColorReference(new Color(208, 205, 190), 0, -12, ColorReference.BACK_COLOR);

// Frame
		frameCaptionColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	frameCaptionColor[1] = new ColorReference(new Color(10, 36, 106), 0, 0, ColorReference.FRAME_COLOR);
    	frameCaptionColor[2] = new ColorReference(new Color(38, 111, 255), 0, 15, ColorReference.FRAME_COLOR);
    	frameCaptionColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);

    	frameCaptionDisabledColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	frameCaptionDisabledColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
    	frameCaptionDisabledColor[2] = new ColorReference(new Color(122, 159, 223), -25, 41, ColorReference.FRAME_COLOR);
    	frameCaptionDisabledColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	frameBorderColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	frameBorderColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	frameBorderColor[2] = new ColorReference(new Color(0, 60, 161), 0, -30, ColorReference.FRAME_COLOR);
    	frameBorderColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	frameDarkColor[0] = new ColorReference(new Color(119, 119, 119), 0, 0, ColorReference.BACK_COLOR);
    	frameDarkColor[1] = new ColorReference(new Color(64, 64, 64), -100, -69, ColorReference.BACK_COLOR);
    	frameDarkColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
    	frameDarkColor[3] = new ColorReference(new Color(119, 119, 119), 0, 0, ColorReference.BACK_COLOR);
    	
    	frameLightColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	frameLightColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
    	frameLightColor[2] = new ColorReference(new Color(0, 68, 184), 0, -20, ColorReference.FRAME_COLOR);
    	frameLightColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);

    	frameBorderDisabledColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	frameBorderDisabledColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	frameBorderDisabledColor[2] = new ColorReference(new Color(74, 125, 212), -25, 20, ColorReference.FRAME_COLOR);
    	frameBorderDisabledColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	frameDarkDisabledColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	frameDarkDisabledColor[1] = new ColorReference(new Color(64, 64, 64), -100, -69, ColorReference.BACK_COLOR);
    	frameDarkDisabledColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
    	frameDarkDisabledColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	frameLightDisabledColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	frameLightDisabledColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
    	frameLightDisabledColor[2] = new ColorReference(new Color(99, 144, 233), -25, 30, ColorReference.FRAME_COLOR);
    	frameLightDisabledColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	frameTitleColor[0] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.FRAME_COLOR);
  		frameTitleColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
  		frameTitleColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.FRAME_COLOR);
  		frameTitleColor[3] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.FRAME_COLOR);
  		
  		frameTitleDisabledColor[0] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.FRAME_COLOR);
  		frameTitleDisabledColor[1] = new ColorReference(new Color(234, 232, 228), 0, 50, ColorReference.BACK_COLOR);
  		frameTitleDisabledColor[2] = new ColorReference(new Color(191, 213, 249), 0, 75, ColorReference.FRAME_COLOR);
  		frameTitleDisabledColor[3] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.FRAME_COLOR);
  		
    	// Button
		frameButtColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	frameButtColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	frameButtColor[2] = new ColorReference(new Color(48, 120, 244), 42, 22, ColorReference.FRAME_COLOR);
    	frameButtColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	frameButtRolloverColor[0] = new ColorReference(new Color(243, 243, 243), 0, 0, ColorReference.BACK_COLOR);
    	frameButtRolloverColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	frameButtRolloverColor[2] = new ColorReference(new Color(84, 148, 255), 100, 39, ColorReference.FRAME_COLOR);
    	frameButtRolloverColor[3] = new ColorReference(new Color(243, 243, 243), 0, 0, ColorReference.BACK_COLOR);
    	
    	frameButtPressedColor[0] = new ColorReference(new Color(126, 126, 126), 0, 0, ColorReference.BACK_COLOR);
    	frameButtPressedColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	frameButtPressedColor[2] = new ColorReference(new Color(0, 77, 230), 0, -10, ColorReference.FRAME_COLOR);
    	frameButtPressedColor[3] = new ColorReference(new Color(126, 126, 126), 0, 0, ColorReference.BACK_COLOR);
    	
    	frameButtDisabledColor[0] = new ColorReference(new Color(238, 238, 238), 0, 0, ColorReference.BACK_COLOR);
    	frameButtDisabledColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	frameButtDisabledColor[2] = new ColorReference(new Color(87, 139, 228), -13, 30, ColorReference.FRAME_COLOR);
    	frameButtDisabledColor[3] = new ColorReference(new Color(238, 238, 238), 0, 0, ColorReference.BACK_COLOR);
    	
    	frameButtSpreadLight[0] = 12;
		frameButtSpreadLight[1] = 0;
		frameButtSpreadLight[2] = 12;
		frameButtSpreadLight[3] = 12;
		
		frameButtSpreadDark[0] = 6;
		frameButtSpreadDark[1] = 0;
		frameButtSpreadDark[2] = 6;
		frameButtSpreadDark[3] = 6;
		
		frameButtSpreadLightDisabled[0] = 6;
		frameButtSpreadLightDisabled[1] = 0;
		frameButtSpreadLightDisabled[2] = 6;
		frameButtSpreadLightDisabled[3] = 6;
		
		frameButtSpreadDarkDisabled[0] = 2;
		frameButtSpreadDarkDisabled[1] = 0;
		frameButtSpreadDarkDisabled[2] = 2;
		frameButtSpreadDarkDisabled[3] = 2;
    	
    	frameButtCloseColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	frameButtCloseColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	frameButtCloseColor[2] = new ColorReference(new Color(227, 92, 60), 0, 0, ColorReference.SUB4_COLOR);
    	frameButtCloseColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	frameButtCloseRolloverColor[0] = new ColorReference(new Color(243, 243, 243), 0, 0, ColorReference.BACK_COLOR);
    	frameButtCloseRolloverColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	frameButtCloseRolloverColor[2] = new ColorReference(new Color(255, 117, 81), 100, 22, ColorReference.SUB4_COLOR);
    	frameButtCloseRolloverColor[3] = new ColorReference(new Color(243, 243, 243), 0, 0, ColorReference.BACK_COLOR);
    	
    	frameButtClosePressedColor[0] = new ColorReference(new Color(126, 126, 126), 0, 0, ColorReference.BACK_COLOR);
    	frameButtClosePressedColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	frameButtClosePressedColor[2] = new ColorReference(new Color(188, 59, 27), 39, -25, ColorReference.SUB4_COLOR);
    	frameButtClosePressedColor[3] = new ColorReference(new Color(126, 126, 126), 0, 0, ColorReference.BACK_COLOR);
    	
    	frameButtCloseDisabledColor[0] = new ColorReference(new Color(238, 238, 238), 0, 0, ColorReference.BACK_COLOR);
    	frameButtCloseDisabledColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	frameButtCloseDisabledColor[2] = new ColorReference(new Color(120, 123, 189), 0, 0, ColorReference.SUB5_COLOR);
    	frameButtCloseDisabledColor[3] = new ColorReference(new Color(238, 238, 238), 0, 0, ColorReference.BACK_COLOR);
    	
    	frameButtCloseSpreadLight[0] = 12;
		frameButtCloseSpreadLight[1] = 0;
		frameButtCloseSpreadLight[2] = 12;
		frameButtCloseSpreadLight[3] = 12;
		
		frameButtCloseSpreadDark[0] = 6;
		frameButtCloseSpreadDark[1] = 0;
		frameButtCloseSpreadDark[2] = 6;
		frameButtCloseSpreadDark[3] = 6;
		
		frameButtCloseSpreadLightDisabled[0] = 6;
		frameButtCloseSpreadLightDisabled[1] = 0;
		frameButtCloseSpreadLightDisabled[2] = 6;
		frameButtCloseSpreadLightDisabled[3] = 6;
		
		frameButtCloseSpreadDarkDisabled[0] = 2;
		frameButtCloseSpreadDarkDisabled[1] = 0;
		frameButtCloseSpreadDarkDisabled[2] = 2;
		frameButtCloseSpreadDarkDisabled[3] = 2;

    	// Button Border
    	frameButtBorderColor[0] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
    	frameButtBorderColor[1] = new ColorReference(new Color(64, 64, 64), -100, -69, ColorReference.BACK_COLOR);
    	frameButtBorderColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.FRAME_COLOR);
    	frameButtBorderColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.FRAME_COLOR);
    	
    	frameButtDarkColor[0] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
    	frameButtDarkColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
    	frameButtDarkColor[2] = new ColorReference(new Color(30, 93, 200), -26, 0, ColorReference.FRAME_COLOR);
    	frameButtDarkColor[3] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
    	
    	frameButtLightColor[0] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
    	frameButtLightColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	frameButtLightColor[2] = new ColorReference(new Color(63, 125, 232), -5, 23, ColorReference.FRAME_COLOR);
    	frameButtLightColor[3] = new ColorReference(new Color(0, 0, 0), 0, 0, ColorReference.BACK_COLOR);
    	
    	frameButtBorderDisabledColor[0] = new ColorReference(new Color(201, 198, 184), 0, 0, ColorReference.BACK_COLOR);
    	frameButtBorderDisabledColor[1] = new ColorReference(new Color(64, 64, 64), -100, -69, ColorReference.BACK_COLOR);
    	frameButtBorderDisabledColor[2] = new ColorReference(new Color(190, 206, 238), -42, 68, ColorReference.FRAME_COLOR);
    	frameButtBorderDisabledColor[3] = new ColorReference(new Color(190, 206, 238), -42, 68, ColorReference.FRAME_COLOR);
    	
    	frameButtDarkDisabledColor[0] = new ColorReference(new Color(201, 198, 184), 0, 0, ColorReference.BACK_COLOR);
    	frameButtDarkDisabledColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
    	frameButtDarkDisabledColor[2] = new ColorReference(new Color(74, 125, 212), -25, 20, ColorReference.FRAME_COLOR);
    	frameButtDarkDisabledColor[3] = new ColorReference(new Color(201, 198, 184), 0, 0, ColorReference.BACK_COLOR);
    	
    	frameButtLightDisabledColor[0] = new ColorReference(new Color(201, 198, 184), 0, 0, ColorReference.BACK_COLOR);
    	frameButtLightDisabledColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	frameButtLightDisabledColor[2] = new ColorReference(new Color(107, 151, 226), -18, 37, ColorReference.FRAME_COLOR);
    	frameButtLightDisabledColor[3] = new ColorReference(new Color(201, 198, 184), 0, 0, ColorReference.BACK_COLOR);
    	
    	// Symbol
    	frameSymbolColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	frameSymbolColor[1] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	frameSymbolColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.FRAME_COLOR);
    	frameSymbolColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	
    	frameSymbolPressedColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	frameSymbolPressedColor[1] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	frameSymbolPressedColor[2] = new ColorReference(new Color(183, 201, 232), -37, 66, ColorReference.FRAME_COLOR);
    	frameSymbolPressedColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	
    	frameSymbolDisabledColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	frameSymbolDisabledColor[1] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	frameSymbolDisabledColor[2] = new ColorReference(new Color(157, 178, 214), -50, 50, ColorReference.FRAME_COLOR);
    	frameSymbolDisabledColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	
    	frameSymbolDarkColor[0] = new ColorReference(new Color(64, 64, 64), -100, -69, ColorReference.FRAME_COLOR);
    	frameSymbolDarkColor[1] = new ColorReference(new Color(64, 64, 64), -100, -69, ColorReference.FRAME_COLOR);
    	frameSymbolDarkColor[2] = new ColorReference(new Color(64, 64, 64), -100, -69, ColorReference.FRAME_COLOR);
    	frameSymbolDarkColor[3] = new ColorReference(new Color(64, 64, 64), -100, -69, ColorReference.FRAME_COLOR);
    	
    	frameSymbolLightColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.FRAME_COLOR);
    	frameSymbolLightColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.FRAME_COLOR);
    	frameSymbolLightColor[2] = new ColorReference(new Color(128, 170, 255), 0, 50, ColorReference.FRAME_COLOR);
    	frameSymbolLightColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.FRAME_COLOR);
    	
    	// Close Button
    	frameButtCloseBorderColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	frameButtCloseBorderColor[1] = new ColorReference(new Color(64, 64, 64), -100, -69, ColorReference.BACK_COLOR);
    	frameButtCloseBorderColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.FRAME_COLOR);
    	frameButtCloseBorderColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.FRAME_COLOR);
    	
    	frameButtCloseDarkColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	frameButtCloseDarkColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
    	frameButtCloseDarkColor[2] = new ColorReference(new Color(174, 51, 20), 50, -32, ColorReference.SUB4_COLOR);
    	frameButtCloseDarkColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	frameButtCloseLightColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	frameButtCloseLightColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	frameButtCloseLightColor[2] = new ColorReference(new Color(226, 88, 55), 11, -2, ColorReference.SUB4_COLOR);
    	frameButtCloseLightColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	frameButtCloseBorderDisabledColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	frameButtCloseBorderDisabledColor[1] = new ColorReference(new Color(64, 64, 64), -100, -69, ColorReference.BACK_COLOR);
    	frameButtCloseBorderDisabledColor[2] = new ColorReference(new Color(190, 206, 238), -42, 68, ColorReference.FRAME_COLOR);
    	frameButtCloseBorderDisabledColor[3] = new ColorReference(new Color(190, 206, 238), -42, 68, ColorReference.FRAME_COLOR);
    	
    	frameButtCloseDarkDisabledColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	frameButtCloseDarkDisabledColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
    	frameButtCloseDarkDisabledColor[2] = new ColorReference(new Color(111, 114, 185), 6, -4, ColorReference.SUB5_COLOR);
    	frameButtCloseDarkDisabledColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	frameButtCloseLightDisabledColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	frameButtCloseLightDisabledColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	frameButtCloseLightDisabledColor[2] = new ColorReference(new Color(114, 117, 185), 3, -3, ColorReference.SUB5_COLOR);
    	frameButtCloseLightDisabledColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	// Close Symbol
    	frameSymbolCloseColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	frameSymbolCloseColor[1] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	frameSymbolCloseColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.FRAME_COLOR);
    	frameSymbolCloseColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	
    	frameSymbolClosePressedColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	frameSymbolClosePressedColor[1] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	frameSymbolClosePressedColor[2] = new ColorReference(new Color(222, 152, 136), -24, 32, ColorReference.SUB4_COLOR);
    	frameSymbolClosePressedColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	
    	frameSymbolCloseDisabledColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	frameSymbolCloseDisabledColor[1] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
    	frameSymbolCloseDisabledColor[2] = new ColorReference(new Color(150, 152, 204), 0, 22, ColorReference.SUB5_COLOR);
    	frameSymbolCloseDisabledColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	
    	frameSymbolCloseDarkColor[0] = new ColorReference(new Color(64, 64, 64), -100, -69, ColorReference.FRAME_COLOR);
    	frameSymbolCloseDarkColor[1] = new ColorReference(new Color(64, 64, 64), -100, -69, ColorReference.FRAME_COLOR);
    	frameSymbolCloseDarkColor[2] = new ColorReference(new Color(64, 64, 64), -100, -69, ColorReference.FRAME_COLOR);
    	frameSymbolCloseDarkColor[3] = new ColorReference(new Color(64, 64, 64), -100, -69, ColorReference.FRAME_COLOR);
    	
    	frameSymbolCloseLightColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.FRAME_COLOR);
    	frameSymbolCloseLightColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.FRAME_COLOR);
    	frameSymbolCloseLightColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.FRAME_COLOR);
    	frameSymbolCloseLightColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.FRAME_COLOR);

		frameExternalButtonSize[0] = new Dimension(17, 17);
		frameExternalButtonSize[1] = new Dimension(16, 14);
		frameExternalButtonSize[2] = new Dimension(21, 21);
		frameExternalButtonSize[3] = new Dimension(17, 17);
		
		
		frameInternalButtonSize[0] = new Dimension(17, 17);
		frameInternalButtonSize[1] = new Dimension(16, 14);
		frameInternalButtonSize[2] = new Dimension(17, 17);
		frameInternalButtonSize[3] = new Dimension(17, 17);
		
		framePaletteButtonSize[0] = new Dimension(17, 17);
		framePaletteButtonSize[1] = new Dimension(16, 14);
		framePaletteButtonSize[2] = new Dimension(13, 13);
		framePaletteButtonSize[3] = new Dimension(17, 17);
		
		frameBorderWidth[0] = 4;
		frameBorderWidth[1] = 4;
		frameBorderWidth[2] = 3;
		frameBorderWidth[3] = 4;
		
		frameTitleHeight[0] = 25;
		frameTitleHeight[1] = 25;
		frameTitleHeight[2] = 29;
		frameTitleHeight[3] = 25;		
		
		frameInternalTitleHeight[0] = 21;
		frameInternalTitleHeight[1] = 23;
		frameInternalTitleHeight[2] = 25;
		frameInternalTitleHeight[3] = 21;
		
		framePaletteTitleHeight[0] = 21;
		framePaletteTitleHeight[1] = 21;
		framePaletteTitleHeight[2] = 21;
		framePaletteTitleHeight[3] = 21;
		
		frameSpreadDark[0] = 6;
		frameSpreadDark[1] = 0;
		frameSpreadDark[2] = 6;
		frameSpreadDark[3] = 6;
		
		frameSpreadLight[0] = 6;
		frameSpreadLight[1] = 0;
		frameSpreadLight[2] = 2;
		frameSpreadLight[3] = 6;
		
		frameSpreadDarkDisabled[0] = 2;
		frameSpreadDarkDisabled[1] = 0;
		frameSpreadDarkDisabled[2] = 2;
		frameSpreadDarkDisabled[3] = 2;
		
		frameSpreadLightDisabled[0] = 2;
		frameSpreadLightDisabled[1] = 0;
		frameSpreadLightDisabled[2] = 2;
		frameSpreadLightDisabled[3] = 2;
		
		frameIsTransparent[0] = true;
		frameIsTransparent[1] = false;
		frameIsTransparent[2] = true;
		frameIsTransparent[3] = true;

// Table
		tableBackColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		tableBackColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		tableBackColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		tableBackColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);

		tableHeaderBackColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
		tableHeaderBackColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
		tableHeaderBackColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
		tableHeaderBackColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
		
		tableHeaderRolloverBackColor[0] = new ColorReference(new Color(249, 248, 243), 0, 70, ColorReference.BACK_COLOR);
		tableHeaderRolloverBackColor[1] = new ColorReference(new Color(249, 248, 243), 0, 70, ColorReference.BACK_COLOR);
		tableHeaderRolloverBackColor[2] = new ColorReference(new Color(249, 248, 243), 0, 70, ColorReference.BACK_COLOR);
		tableHeaderRolloverBackColor[3] = new ColorReference(new Color(249, 248, 243), 0, 70, ColorReference.BACK_COLOR);
		
		tableHeaderRolloverColor[0] = new ColorReference(new Color(248, 179, 48), 0, 0, ColorReference.SUB6_COLOR);
		tableHeaderRolloverColor[1] = new ColorReference(new Color(248, 179, 48), 0, 0, ColorReference.SUB6_COLOR);
		tableHeaderRolloverColor[2] = new ColorReference(new Color(248, 179, 48), 0, 0, ColorReference.SUB6_COLOR);
		tableHeaderRolloverColor[3] = new ColorReference(new Color(248, 179, 48), 0, 0, ColorReference.SUB6_COLOR);
		
		tableGridColor[0] = new ColorReference(new Color(167, 166, 160), -50, -28, ColorReference.BACK_COLOR);
		tableGridColor[1] = new ColorReference(new Color(168, 166, 163), -50, -20, ColorReference.BACK_COLOR);
		tableGridColor[2] = new ColorReference(new Color(167, 166, 160), -50, -28, ColorReference.BACK_COLOR);
		tableGridColor[3] = new ColorReference(new Color(167, 166, 160), -50, -28, ColorReference.BACK_COLOR);
		
		tableHeaderArrowColor[0] = new ColorReference(new Color(167, 166, 160), -50, -28, ColorReference.BACK_COLOR);
		tableHeaderArrowColor[1] = new ColorReference(new Color(168, 166, 163), -50, -20, ColorReference.BACK_COLOR);
		tableHeaderArrowColor[2] = new ColorReference(new Color(167, 166, 160), -50, -28, ColorReference.BACK_COLOR);
		tableHeaderArrowColor[3] = new ColorReference(new Color(167, 166, 160), -50, -28, ColorReference.BACK_COLOR);
		
		tableSelectedBackColor[0] = new ColorReference(new Color(167, 166, 160), -50, -28, ColorReference.BACK_COLOR);
		tableSelectedBackColor[1] = new ColorReference(new Color(10, 50, 105), 0, 0, ColorReference.SUB1_COLOR);
		tableSelectedBackColor[2] = new ColorReference(new Color(213, 211, 204), -50, -8, ColorReference.BACK_COLOR);
		tableSelectedBackColor[3] = new ColorReference(new Color(167, 166, 160), -50, -28, ColorReference.BACK_COLOR);
		
		tableSelectedForeColor[0] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		tableSelectedForeColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		tableSelectedForeColor[2] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		tableSelectedForeColor[3] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		
		tableBorderDarkColor[0] = new ColorReference(new Color(168, 166, 163), -50, -20, ColorReference.BACK_COLOR);
		tableBorderDarkColor[1] = new ColorReference(new Color(168, 166, 163), -50, -20, ColorReference.BACK_COLOR);
		tableBorderDarkColor[2] = new ColorReference(new Color(167, 166, 160), -50, -28, ColorReference.BACK_COLOR);
		tableBorderDarkColor[3] = new ColorReference(new Color(167, 166, 160), -50, -28, ColorReference.BACK_COLOR);
		
		tableBorderLightColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		tableBorderLightColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		tableBorderLightColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		tableBorderLightColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		
		tableHeaderDarkColor[0] = new ColorReference(new Color(168, 166, 163), -50, -20, ColorReference.BACK_COLOR);
		tableHeaderDarkColor[1] = new ColorReference(new Color(168, 166, 163), -50, -20, ColorReference.BACK_COLOR);
		tableHeaderDarkColor[2] = new ColorReference(new Color(189, 186, 173), 0, -20, ColorReference.BACK_COLOR);
		tableHeaderDarkColor[3] = new ColorReference(new Color(167, 166, 160), -50, -28, ColorReference.BACK_COLOR);
		
		tableHeaderLightColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		tableHeaderLightColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		tableHeaderLightColor[2] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		tableHeaderLightColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		
// Icons
		frameIconColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.FRAME_COLOR);
    	frameIconColor[1] = new ColorReference(new Color(10, 36, 106), 0, 0, ColorReference.FRAME_COLOR);
    	frameIconColor[2] = new ColorReference(new Color(0, 85, 230), 0, 0, ColorReference.FRAME_COLOR);
    	frameIconColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.FRAME_COLOR);

    	treeIconColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	treeIconColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	treeIconColor[2] = new ColorReference(new Color(255, 239, 151), 100, -10, ColorReference.BACK_COLOR);
    	treeIconColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	fileViewIconColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	fileViewIconColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	fileViewIconColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
    	fileViewIconColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	fileChooserIconColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	fileChooserIconColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	fileChooserIconColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
    	fileChooserIconColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	
    	optionPaneIconColor[0] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);
    	optionPaneIconColor[1] = new ColorReference(new Color(212, 208, 200), 0, 0, ColorReference.BACK_COLOR);
    	optionPaneIconColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
    	optionPaneIconColor[3] = new ColorReference(new Color(221, 221, 221), 0, 0, ColorReference.BACK_COLOR);

		for(int st = 0; st < 4; st++) {
			for(int i = 0; i < 15; i++) {
				colorize[st][i] = true;
			}
		}
		colorizeFrameIcon[0] = false;
		colorizeFrameIcon[1] = false;
		colorizeFrameIcon[2] = false;
		colorizeFrameIcon[3] = false;
		
		colorizeTreeIcon[0] = true;
		colorizeTreeIcon[1] = true;
		colorizeTreeIcon[2] = true;
		colorizeTreeIcon[3] = true;
		
		colorizeFileViewIcon[0] = false;
		colorizeFileViewIcon[1] = false;
		colorizeFileViewIcon[2] = false;
		colorizeFileViewIcon[3] = false;
		
		colorizeFileChooserIcon[0] = false;
		colorizeFileChooserIcon[1] = false;
		colorizeFileChooserIcon[2] = false;
		colorizeFileChooserIcon[3] = false;
		
		colorizeOptionPaneIcon[0] = false;
		colorizeOptionPaneIcon[1] = false;
		colorizeOptionPaneIcon[2] = false;
		colorizeOptionPaneIcon[3] = false;
		
		hue[0] = 40;
		hue[1] = 40;
		hue[2] = 51;
		hue[3] = 40;
		
		for(int st = 0; st < 4; st++) {
			for(int i = 0; i < 20; i++) {
				colorizer[i][st] = new HSBReference(hue[st], 25, 0, HSBReference.BACK_COLOR);
				colorize[st][i] = false;
			}
		}
		
// Separator
		sepDarkColor[0] = new ColorReference(new Color(167, 167, 163), 0, 0, ColorReference.BACK_COLOR);
		sepDarkColor[1] = new ColorReference(new Color(128, 128, 128), -100, -38, ColorReference.BACK_COLOR);
		sepDarkColor[2] = new ColorReference(new Color(167, 167, 163), -70, -27, ColorReference.BACK_COLOR);
		sepDarkColor[3] = new ColorReference(new Color(167, 167, 163), 0, 0, ColorReference.BACK_COLOR);
		
		sepLightColor[0] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	sepLightColor[1] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
    	sepLightColor[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
    	sepLightColor[3] = new ColorReference(new Color(255, 255, 255), 0, 100, ColorReference.BACK_COLOR);
		
		
// ToolTip
		tipBorderColor[0] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		tipBorderColor[1] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		tipBorderColor[2] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		tipBorderColor[3] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		
		tipBorderDis[0] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		tipBorderDis[1] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		tipBorderDis[2] = new ColorReference(new Color(143, 141, 139), 0, 0, ColorReference.DIS_COLOR);
		tipBorderDis[3] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		
		tipBgColor[0] = new ColorReference(new Color(255, 255, 225), 0, 0, ColorReference.ABS_COLOR);
		tipBgColor[1] = new ColorReference(new Color(255, 255, 225), 0, 0, ColorReference.ABS_COLOR);
		tipBgColor[2] = new ColorReference(new Color(255, 255, 225), 0, 0, ColorReference.ABS_COLOR);
		tipBgColor[3] = new ColorReference(new Color(255, 255, 225), 0, 0, ColorReference.ABS_COLOR);
		
		tipBgDis[0] = new ColorReference(new Color(255, 255, 225), 0, 0, ColorReference.ABS_COLOR);
		tipBgDis[1] = new ColorReference(new Color(255, 255, 225), 0, 0, ColorReference.ABS_COLOR);
		tipBgDis[2] = new ColorReference(new Color(236, 233, 216), 0, 0, ColorReference.BACK_COLOR);
		tipBgDis[3] = new ColorReference(new Color(255, 255, 225), 0, 0, ColorReference.ABS_COLOR);
		
		tipTextColor[0] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		tipTextColor[1] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		tipTextColor[2] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		tipTextColor[3] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		
		tipTextDis[0] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		tipTextDis[1] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
		tipTextDis[2] = new ColorReference(new Color(143, 141, 139), 0, 0, ColorReference.DIS_COLOR);
		tipTextDis[3] = new ColorReference(new Color(0, 0, 0), 0, -100, ColorReference.BACK_COLOR);
// Misc
		titledBorderColor[0] = new ColorReference(new Color(221, 221, 221), 0, -50, ColorReference.BACK_COLOR);
		titledBorderColor[1] = new ColorReference(new Color(167, 164, 158), 0, -21, ColorReference.BACK_COLOR);
		titledBorderColor[2] = new ColorReference(new Color(165, 163, 151), 0, -30, ColorReference.BACK_COLOR);
		titledBorderColor[3] = new ColorReference(new Color(221, 221, 221), 0, -50, ColorReference.BACK_COLOR);
    }

    /**
     * Load theme from file.
     * @param f
     * @param style
     * @return
     */
    public static boolean loadTheme(File f, int style) {
    	if(!f.exists()) return false;
    	
    	try {
			return loadTheme(new FileInputStream(f), style);
		} catch (FileNotFoundException e) {
			return false;
		}
    }
    
    /**
     * Load theme from url.
     * @param url
     * @param style
     * @return
     */
    public static boolean loadTheme(URL url, int style) {
    	if(url == null) return false;
    	
    	try {
			return loadTheme(url.openStream(), style);
		}
    	catch(IOException ex) {
			return false;
		}
    }
    
    private static boolean loadTheme(InputStream istream, int style) {
  		try {
			DataInputStream in = new DataInputStream(
				new BufferedInputStream(istream));
			
			fileID = in.readInt();

			if(fileID != FILE_ID_3A &&
				fileID != FILE_ID_3B &&
				fileID != FILE_ID_3C &&
				fileID != FILE_ID_3D &&
				fileID != FILE_ID_3E &&
				fileID != FILE_ID_3F &&
				fileID != FILE_ID_2 &&
				fileID != FILE_ID_1) return false;
			
			derivedStyle[CUSTOM_STYLE] = in.readInt();
			
// Colors
			mainColor[style].load(in);
			disColor[style].load(in);
    		backColor[style].load(in);
			frameColor[style].load(in);
			sub1Color[style].load(in);
			sub2Color[style].load(in);
			sub3Color[style].load(in);
			sub4Color[style].load(in);
			sub5Color[style].load(in);
			sub6Color[style].load(in);
			sub7Color[style].load(in);
			sub8Color[style].load(in);
    
// Font
  			plainFont[style].load(in);
			boldFont[style].load(in);
			buttonFont[style].load(in);
			passwordFont[style].load(in);
			labelFont[style].load(in);
			comboFont[style].load(in);
			if(fileID == FILE_ID_1) {	// 1.0
				popupFont[style].load(in);
			}
			listFont[style].load(in);
			menuFont[style].load(in);
			menuItemFont[style].load(in);
			radioFont[style].load(in);
			checkFont[style].load(in);
			tableFont[style].load(in);
			tableHeaderFont[style].load(in);
			textAreaFont[style].load(in);
			textFieldFont[style].load(in);
  			textPaneFont[style].load(in);
			titledBorderFont[style].load(in);
			toolTipFont[style].load(in);
			treeFont[style].load(in);
			tabFont[style].load(in);
			editorFont[style].load(in);
			frameTitleFont[style].load(in);
			
			if(fileID != FILE_ID_1) {	// not for 1.0
				progressBarFont[style].load(in);
			}
			
			if(fileID >= FILE_ID_3A) {
				internalFrameTitleFont[style].load(in);
				internalPaletteTitleFont[style].load(in);
			}
			else {
				internalFrameTitleFont[style] = internalFrameTitleFont[derivedStyle[CUSTOM_STYLE]];
				internalPaletteTitleFont[style] = internalPaletteTitleFont[derivedStyle[CUSTOM_STYLE]];
			}

  			buttonFontColor[style].load(in);
  			buttonFont[style].setColorReference(buttonFontColor);
			labelFontColor[style].load(in);
			labelFont[style].setColorReference(labelFontColor);
			menuFontColor[style].load(in);
			menuFont[style].setColorReference(menuFontColor);
			menuItemFontColor[style].load(in);
			menuItemFont[style].setColorReference(menuItemFontColor);
			radioFontColor[style].load(in);
			radioFont[style].setColorReference(radioFontColor);
			checkFontColor[style].load(in);
			checkFont[style].setColorReference(checkFontColor);
			tableFontColor[style].load(in);
			tableFont[style].setColorReference(tableFontColor);
			tableHeaderFontColor[style].load(in);
			tableHeaderFont[style].setColorReference(tableHeaderFontColor);
			tabFontColor[style].load(in);
			tabFont[style].setColorReference(tabFontColor);
			titledBorderFontColor[style].load(in);
			titledBorderFont[style].setColorReference(titledBorderFontColor);
			
			if(fileID < FILE_ID_3C) {
				toolTipFontColor[style].load(in);
				//toolTipFont[style].setColorReference(toolTipFontColor);
			}
  		
// Progressbar
			progressColor[style].load(in);
			progressTrackColor[style].load(in);
			progressBorderColor[style].load(in);
			progressDarkColor[style].load(in);
			progressLightColor[style].load(in);
			
			if(fileID != FILE_ID_1) {	// not 1.0
				progressSelectForeColor[style].load(in);
				progressSelectBackColor[style].load(in);
			}
  		
// Text	
  			textBgColor[style].load(in);
			textTextColor[style].load(in);
			
			if(fileID >= FILE_ID_3A) {
				textCaretColor[style].load(in);
				editorPaneBgColor[style].load(in);
				textPaneBgColor[style].load(in);
				desktopPaneBgColor[style].load(in);
			}
			else {
				textCaretColor[style] = new ColorReference(textCaretColor[derivedStyle[CUSTOM_STYLE]]);
				editorPaneBgColor[style] = new ColorReference(editorPaneBgColor[derivedStyle[CUSTOM_STYLE]]);
				textPaneBgColor[style] = new ColorReference(textPaneBgColor[derivedStyle[CUSTOM_STYLE]]);
				desktopPaneBgColor[style] = new ColorReference(desktopPaneBgColor[derivedStyle[CUSTOM_STYLE]]);
			}
			
			textSelectedBgColor[style].load(in);
			textSelectedTextColor[style].load(in);
			textDisabledBgColor[style].load(in);
			textBorderColor[style].load(in);
    		textBorderDarkColor[style].load(in);
			textBorderLightColor[style].load(in);
			textBorderDisabledColor[style].load(in);
			textBorderDarkDisabledColor[style].load(in);
			textBorderLightDisabledColor[style].load(in);
			textInsets[style] = new Insets(
				in.readInt(),
				in.readInt(),
				in.readInt(),
				in.readInt());
    	
// Button    	
			buttonRollover[style] = in.readBoolean();
			buttonFocus[style] = in.readBoolean();
			
			if(fileID >= FILE_ID_3A) {
				buttonFocusBorder[style] = in.readBoolean();
				buttonEnter[style] = in.readBoolean();
			}
			
			if(fileID >= FILE_ID_3D) {
				shiftButtonText[style] = in.readBoolean();
			}
			
    		buttonNormalColor[style].load(in);
			buttonRolloverBgColor[style].load(in);
			buttonPressedColor[style].load(in);
			buttonDisabledColor[style].load(in);
			buttonBorderColor[style].load(in);
			buttonDarkColor[style].load(in);
			buttonLightColor[style].load(in);
			buttonBorderDisabledColor[style].load(in);
			buttonDarkDisabledColor[style].load(in);
			buttonLightDisabledColor[style].load(in);
			buttonMarginTop[style] = in.readInt();
			buttonMarginLeft[style] = in.readInt();
			buttonMarginBottom[style] = in.readInt();
			buttonMarginRight[style] = in.readInt();
			
			if(fileID >= FILE_ID_3B) {
				checkMarginTop[style] = in.readInt();
				checkMarginLeft[style] = in.readInt();
				checkMarginBottom[style] = in.readInt();
				checkMarginRight[style] = in.readInt();
			}
			else {
				checkMarginTop[style] = checkMarginTop[0];
				checkMarginLeft[style] = checkMarginLeft[0];
				checkMarginBottom[style] = checkMarginBottom[0];
				checkMarginRight[style] = checkMarginRight[0];
			}
			
			buttonRolloverColor[style].load(in);
			buttonDefaultColor[style].load(in);
			buttonCheckColor[style].load(in);
			buttonCheckDisabledColor[style].load(in);
			buttonDisabledFgColor[style].load(in);
			checkDisabledFgColor[style].load(in);
			radioDisabledFgColor[style].load(in);
			buttonSpreadLight[style] = in.readInt();
			buttonSpreadDark[style] = in.readInt();
			buttonSpreadLightDisabled[style] = in.readInt();
			buttonSpreadDarkDisabled[style] = in.readInt();
			
			if(fileID < FILE_ID_3A) {
				// because I added (2, 2, 2, 2) insets for the border,
				// subtract it here
				buttonMarginTop[style] = Math.max(0, buttonMarginTop[style] - 2);
				buttonMarginLeft[style] = Math.max(0, buttonMarginLeft[style] - 2);
				buttonMarginBottom[style] = Math.max(0, buttonMarginBottom[style] - 2);
				buttonMarginRight[style] = Math.max(0, buttonMarginRight[style] - 2);
			}
    	
// Scrollbar
    		scrollRollover[style] = in.readBoolean();
			scrollTrackColor[style].load(in);
			scrollTrackDisabledColor[style].load(in);
			scrollTrackBorderColor[style].load(in);
			scrollTrackBorderDisabledColor[style].load(in);

    		// Thumb
    		scrollThumbColor[style].load(in);
			scrollThumbRolloverColor[style].load(in);
			scrollThumbPressedColor[style].load(in);
			scrollThumbDisabledColor[style].load(in);
		
    		// Grip
    		scrollGripLightColor[style].load(in);
			scrollGripDarkColor[style].load(in);
		
    		// Buttons
			scrollButtColor[style].load(in);
			scrollButtRolloverColor[style].load(in);
			scrollButtPressedColor[style].load(in);
			scrollButtDisabledColor[style].load(in);
			scrollSpreadLight[style] = in.readInt();
			scrollSpreadDark[style] = in.readInt();
			scrollSpreadLightDisabled[style] = in.readInt();
			scrollSpreadDarkDisabled[style] = in.readInt();
		
    		// Arrow
			scrollArrowColor[style].load(in);
			scrollArrowDisabledColor[style].load(in);
    	
    		// Border
    		scrollBorderColor[style].load(in);
			scrollDarkColor[style].load(in);
			scrollLightColor[style].load(in);
			scrollBorderDisabledColor[style].load(in);
			scrollDarkDisabledColor[style].load(in);
			scrollLightDisabledColor[style].load(in);
		
    		// ScrollPane border
    		scrollPaneBorderColor[style].load(in);
    	
// Tabbed
			tabPaneBorderColor[style].load(in);
    		tabPaneDarkColor[style].load(in);
			tabPaneLightColor[style].load(in);
			tabNormalColor[style].load(in);
    		tabSelectedColor[style].load(in);
    		
    		if(fileID >= FILE_ID_3A) {
				tabDisabledColor[style].load(in);
				tabDisabledSelectedColor[style].load(in);
				tabDisabledTextColor[style].load(in);
			}
			else {
				tabDisabledColor[style] = new ColorReference(tabDisabledColor[derivedStyle[CUSTOM_STYLE]]);
				tabDisabledSelectedColor[style] = new ColorReference(tabDisabledSelectedColor[derivedStyle[CUSTOM_STYLE]]);
				tabDisabledTextColor[style] = new ColorReference(tabDisabledTextColor[derivedStyle[CUSTOM_STYLE]]);
			}
    		
			tabBorderColor[style].load(in);
			tabDarkColor[style].load(in);
			tabLightColor[style].load(in);
			tabRolloverColor[style].load(in);

			int leftInset = -1;
			if(fileID < FILE_ID_3A) {
				leftInset = in.readInt();	// was firstTabDistance
			}
			
			tabRollover[style] = in.readBoolean();
			
			if(fileID >= FILE_ID_3E) {
				tabFocus[style] = in.readBoolean();
			}
			// else is true
			
			ignoreSelectedBg[style] = in.readBoolean(); // was tabFocus
			
			if(fileID >= FILE_ID_3C) {
				fixedTabs[style] = in.readBoolean();
			}
			
			if(fileID < FILE_ID_3A) {
				in.readInt();	// was tabContentBorderInsets
				in.readInt();
				in.readInt();
				in.readInt();
			}
				
			if(fileID >= FILE_ID_3A) {
				tabInsets[style] = new Insets(
					in.readInt(),
					in.readInt(),
					in.readInt(),
					in.readInt());
				tabAreaInsets[style] = new Insets(
					in.readInt(),
					in.readInt(),
					in.readInt(),
					in.readInt());
					
				if(leftInset > -1) {
					tabAreaInsets[style].left = leftInset;
				}
			}
    	    	
// Slider
    		sliderRolloverEnabled[style] = in.readBoolean();
    		
    		if(fileID >= FILE_ID_3E) {
    			sliderFocusEnabled[style] = in.readBoolean();
			}
			// else is true

			// Thumb
			sliderThumbColor[style].load(in);
			sliderThumbRolloverColor[style].load(in);
			sliderThumbPressedColor[style].load(in);
			sliderThumbDisabledColor[style].load(in);
			sliderBorderColor[style].load(in);
			sliderDarkColor[style].load(in);
			sliderLightColor[style].load(in);
			
			if(derivedStyle[style] == YQ_STYLE) {
				if(fileID < FILE_ID_3A) {
					sliderLightColor[style] = new ColorReference(buttonCheckColor[derivedStyle[CUSTOM_STYLE]]);
				}
			}
			
			sliderBorderDisabledColor[style].load(in);
			sliderDarkDisabledColor[style].load(in);
			sliderLightDisabledColor[style].load(in);
			sliderTrackColor[style].load(in);
			sliderTrackBorderColor[style].load(in);
			sliderTrackDarkColor[style].load(in);
			sliderTrackLightColor[style].load(in);
			sliderTickColor[style].load(in);
			sliderTickDisabledColor[style].load(in);
			
			if(fileID >= FILE_ID_3E) {
				sliderFocusColor[style].load(in);
			}
		
// Spinner
    		spinnerRollover[style] = in.readBoolean();

			// Button
			spinnerButtColor[style].load(in);
			spinnerButtRolloverColor[style].load(in);
			spinnerButtPressedColor[style].load(in);
    		spinnerButtDisabledColor[style].load(in);
    		spinnerSpreadLight[style] = in.readInt();
			spinnerSpreadDark[style] = in.readInt();
			spinnerSpreadLightDisabled[style] = in.readInt();
			spinnerSpreadDarkDisabled[style] = in.readInt();
			spinnerBorderColor[style].load(in);
			spinnerDarkColor[style].load(in);
			spinnerLightColor[style].load(in);
			spinnerBorderDisabledColor[style].load(in);
			spinnerDarkDisabledColor[style].load(in);
			spinnerLightDisabledColor[style].load(in);
		
    		// Arrow
    		spinnerArrowColor[style].load(in);
			spinnerArrowDisabledColor[style].load(in);
		
// Combo
    		comboBorderColor[style].load(in);
			comboDarkColor[style].load(in);
			comboLightColor[style].load(in);
			comboBorderDisabledColor[style].load(in);
			comboDarkDisabledColor[style].load(in);
			comboLightDisabledColor[style].load(in);
			comboSelectedBgColor[style].load(in);
			comboSelectedTextColor[style].load(in);
			comboFocusBgColor[style].load(in);
			
			if(fileID >= FILE_ID_3A) {
				comboBgColor[style].load(in);
				comboTextColor[style].load(in);
			}
			else {
				comboBgColor[style] = new ColorReference(textBgColor[style]);
				comboTextColor[style] = new ColorReference(textTextColor[style]);
			}
		
			// Button
    		comboButtColor[style].load(in);
			comboButtRolloverColor[style].load(in);
			comboButtPressedColor[style].load(in);
			comboButtDisabledColor[style].load(in);
			comboSpreadLight[style] = in.readInt();
			comboSpreadDark[style] = in.readInt();
			comboSpreadLightDisabled[style] = in.readInt();
			comboSpreadDarkDisabled[style] = in.readInt();

    		// Button Border
    		comboButtBorderColor[style].load(in);
			comboButtDarkColor[style].load(in);
			comboButtLightColor[style].load(in);
			comboButtBorderDisabledColor[style].load(in);
			comboButtDarkDisabledColor[style].load(in);
			comboButtLightDisabledColor[style].load(in);
		
			// Arrow
    		comboArrowColor[style].load(in);
			comboArrowDisabledColor[style].load(in);
			comboInsets[style] = new Insets(
				in.readInt(),
				in.readInt(),
				in.readInt(),
				in.readInt());
			comboRollover[style] = in.readBoolean();
			comboFocus[style] = in.readBoolean();
		
// Menu
    		menuBarColor[style].load(in);
			menuSelectedTextColor[style].load(in);
			menuPopupColor[style].load(in);
			menuRolloverBgColor[style].load(in);
			menuItemRolloverColor[style].load(in);
			menuBorderColor[style].load(in);
			menuDarkColor[style].load(in);
			menuLightColor[style].load(in);
			menuIconColor[style].load(in);
			menuIconRolloverColor[style].load(in);
			menuIconDisabledColor[style].load(in);
			menuIconShadowColor[style].load(in);
			menuSepDarkColor[style].load(in);
			menuSepLightColor[style].load(in);
			menuBorderInsets[style] = new Insets(
				in.readInt(),
				in.readInt(),
				in.readInt(),
				in.readInt());
			menuRollover[style] = in.readBoolean();
			
			if(fileID >= FILE_ID_3A) {
				menuInnerHilightColor[style].load(in);
				menuInnerShadowColor[style].load(in);
				menuOuterHilightColor[style].load(in);
				menuOuterShadowColor[style].load(in);
				menuRolloverFgColor[style].load(in);
				menuDisabledFgColor[style].load(in);
			}
			else {
				menuInnerHilightColor[style] = new ColorReference(menuInnerHilightColor[derivedStyle[CUSTOM_STYLE]]);
				menuInnerShadowColor[style] = new ColorReference(menuInnerShadowColor[derivedStyle[CUSTOM_STYLE]]);
				menuOuterHilightColor[style] = new ColorReference(menuOuterHilightColor[derivedStyle[CUSTOM_STYLE]]);
				menuOuterShadowColor[style] = new ColorReference(menuOuterShadowColor[derivedStyle[CUSTOM_STYLE]]);
				menuRolloverFgColor[style] = new ColorReference(menuFont[style].getColorReference()[style]);
				menuDisabledFgColor[style] = new ColorReference(buttonDisabledFgColor[style]);
			}
    	
// Toolbar
    		toolBarColor[style].load(in);
			toolBarLightColor[style].load(in);
			toolBarDarkColor[style].load(in);
			
			if(fileID >= FILE_ID_3A) {
				toolButtColor[style].load(in);
				toolButtRolloverColor[style].load(in);
				toolButtPressedColor[style].load(in);
				toolButtSelectedColor[style].load(in);
			}
			else {
				toolButtColor[style] = new ColorReference(toolButtSelectedColor[style]);
				toolButtSelectedColor[style].load(in);	// Note:
				toolButtRolloverColor[style].load(in);	// order differs
				toolButtPressedColor[style].load(in);	// from 1.3
			}
			
			toolBorderDarkColor[style].load(in);
			toolBorderLightColor[style].load(in);
			toolBorderColor[style].load(in);
			if(fileID >= FILE_ID_3A) {
				toolBorderRolloverColor[style].load(in);
			}
			else {
				toolBorderRolloverColor[style] = new ColorReference(toolBorderColor[style]);
			}
			toolBorderPressedColor[style].load(in);
			toolBorderSelectedColor[style].load(in);
			toolRollover[style] = in.readBoolean();
			toolFocus[style] = in.readBoolean();
			
			if(fileID >= FILE_ID_3A) {
				toolGripDarkColor[style].load(in);
				toolGripLightColor[style].load(in);
				toolSepDarkColor[style].load(in);
				toolSepLightColor[style].load(in);

				toolMarginTop[style] = in.readInt();
				toolMarginLeft[style] = in.readInt();
				toolMarginBottom[style] = in.readInt();
				toolMarginRight[style] = in.readInt();
			}
			else {
				toolGripDarkColor[style] = new ColorReference(toolGripDarkColor[derivedStyle[CUSTOM_STYLE]]);
				toolGripLightColor[style] = new ColorReference(toolGripLightColor[derivedStyle[CUSTOM_STYLE]]);
				toolSepDarkColor[style] = new ColorReference(toolSepDarkColor[derivedStyle[CUSTOM_STYLE]]);
				toolSepLightColor[style] = new ColorReference(toolSepLightColor[derivedStyle[CUSTOM_STYLE]]);

				toolMarginTop[style] = 4;
				toolMarginLeft[style] = 4;
				toolMarginBottom[style] = 4;
				toolMarginRight[style] = 4;
			}
    	
// List
			listSelectedBgColor[style].load(in);
			listSelectedTextColor[style].load(in);
			if(fileID >= FILE_ID_3A) {
				listBgColor[style].load(in);
				listTextColor[style].load(in);
			}
			else {
				listBgColor[style] = new ColorReference(listBgColor[derivedStyle[CUSTOM_STYLE]]);
				listTextColor[style] = new ColorReference(listTextColor[derivedStyle[CUSTOM_STYLE]]);
			}
		
// Tree
			treeBgColor[style].load(in);
			treeTextColor[style].load(in);
			treeTextBgColor[style].load(in);
			treeSelectedTextColor[style].load(in);
			treeSelectedBgColor[style].load(in);
			if(fileID >= FILE_ID_3A) {
				treeLineColor[style].load(in);
			}
			else {
				treeLineColor[style] = new ColorReference(treeLineColor[derivedStyle[CUSTOM_STYLE]]);
			}
		
// Frame
			frameCaptionColor[style].load(in);
    		frameCaptionDisabledColor[style].load(in);
			frameBorderColor[style].load(in);
			frameDarkColor[style].load(in);
			frameLightColor[style].load(in);
			frameBorderDisabledColor[style].load(in);
			frameDarkDisabledColor[style].load(in);
			frameLightDisabledColor[style].load(in);
			frameTitleColor[style].load(in);
			frameTitleDisabledColor[style].load(in);
		
    		// Button
			frameButtColor[style].load(in);
			frameButtRolloverColor[style].load(in);
			frameButtPressedColor[style].load(in);
			frameButtDisabledColor[style].load(in);
			frameButtSpreadDark[style] = in.readInt();
			frameButtSpreadLight[style] = in.readInt();
			frameButtSpreadDarkDisabled[style] = in.readInt();
			frameButtSpreadLightDisabled[style] = in.readInt();
			frameButtCloseColor[style].load(in);
			frameButtCloseRolloverColor[style].load(in);
			frameButtClosePressedColor[style].load(in);
			frameButtCloseDisabledColor[style].load(in);
			frameButtCloseSpreadDark[style] = in.readInt();
			frameButtCloseSpreadLight[style] = in.readInt();
			frameButtCloseSpreadDarkDisabled[style] = in.readInt();
			frameButtCloseSpreadLightDisabled[style] = in.readInt();
		
    		// Button Border
    		frameButtBorderColor[style].load(in);
    		frameButtDarkColor[style].load(in);
			frameButtLightColor[style].load(in);
			frameButtBorderDisabledColor[style].load(in);
			frameButtDarkDisabledColor[style].load(in);
			frameButtLightDisabledColor[style].load(in);
			
			// Symbol
			frameSymbolColor[style].load(in);
			frameSymbolPressedColor[style].load(in);
			frameSymbolDisabledColor[style].load(in);
			frameSymbolDarkColor[style].load(in);
			frameSymbolLightColor[style].load(in);
		
    		// Close Button
			frameButtCloseBorderColor[style].load(in);
    		frameButtCloseDarkColor[style].load(in);
			frameButtCloseLightColor[style].load(in);
			frameButtCloseBorderDisabledColor[style].load(in);
			frameButtCloseDarkDisabledColor[style].load(in);
			frameButtCloseLightDisabledColor[style].load(in);
			
			// Close Symbol
			frameSymbolCloseColor[style].load(in);
			frameSymbolClosePressedColor[style].load(in);
			frameSymbolCloseDisabledColor[style].load(in);
			frameSymbolCloseDarkColor[style].load(in);
			frameSymbolCloseLightColor[style].load(in);
			frameSpreadDark[style] = in.readInt();
			frameSpreadLight[style] = in.readInt();
			frameSpreadDarkDisabled[style] = in.readInt();
			frameSpreadLightDisabled[style] = in.readInt();

// Table
			tableBackColor[style].load(in);
			tableHeaderBackColor[style].load(in);
			
			if(fileID >= FILE_ID_3F) {
				tableHeaderArrowColor[style].load(in);
				tableHeaderRolloverBackColor[style].load(in);
				tableHeaderRolloverColor[style].load(in);
			}
			else {
				tableHeaderArrowColor[style] = new ColorReference(tableHeaderArrowColor[derivedStyle[CUSTOM_STYLE]]);
				tableHeaderRolloverBackColor[style] = new ColorReference(tableHeaderRolloverBackColor[derivedStyle[CUSTOM_STYLE]]);
				tableHeaderRolloverColor[style] = new ColorReference(tableHeaderRolloverColor[derivedStyle[CUSTOM_STYLE]]);
			}
			
			tableGridColor[style].load(in);
			tableSelectedBackColor[style].load(in);
			tableSelectedForeColor[style].load(in);
			
			if(fileID >= FILE_ID_3A) {
				tableBorderDarkColor[style].load(in);
				tableBorderLightColor[style].load(in);
				tableHeaderDarkColor[style].load(in);
				tableHeaderLightColor[style].load(in);
			}
			else {
				tableBorderDarkColor[style] = new ColorReference(tableBorderDarkColor[derivedStyle[CUSTOM_STYLE]]);
				tableBorderLightColor[style] = new ColorReference(tableBorderLightColor[derivedStyle[CUSTOM_STYLE]]);
				tableHeaderDarkColor[style] = new ColorReference(tableHeaderDarkColor[derivedStyle[CUSTOM_STYLE]]);
				tableHeaderLightColor[style] = new ColorReference(tableHeaderLightColor[derivedStyle[CUSTOM_STYLE]]);
			}
			
// Icons
			if(fileID >= FILE_ID_3A) {
				for(int i = 0; i < 20; i++) {
					colorizer[i][style].load(in);
					colorize[style][i] = in.readBoolean();
				}
			}
			else {
				frameIconColor[style].load(in);
	    		treeIconColor[style].load(in);
	    		fileViewIconColor[style].load(in);
	    		fileChooserIconColor[style].load(in);
	    		optionPaneIconColor[style].load(in);
	    		
	    		colorizeFrameIcon[style] = in.readBoolean();
				colorizeTreeIcon[style] = in.readBoolean();
				colorizeFileViewIcon[style] = in.readBoolean();
				colorizeFileChooserIcon[style] = in.readBoolean();
				colorizeOptionPaneIcon[style] = in.readBoolean();
	
				for(int i = 0; i < 15; i++) {
					colorize[style][i] = in.readBoolean();
				}
				
				// no icons colorized
				for(int i = 0; i < 20; i++) {
					colorize[style][i] = false;
				}
			}
			
// Separator
			if(fileID >= FILE_ID_3A) {
				sepDarkColor[style].load(in);
				sepLightColor[style].load(in);
			}
			else {
				sepDarkColor[style] = new ColorReference(sepDarkColor[derivedStyle[CUSTOM_STYLE]]);
				sepLightColor[style] = new ColorReference(sepLightColor[derivedStyle[CUSTOM_STYLE]]);
			}
			
// ToolTip
			tipBorderColor[style].load(in);
			tipBgColor[style].load(in);
			
			if(fileID >= FILE_ID_3C) {
				tipBorderDis[style].load(in);
				tipBgDis[style].load(in);
				tipTextColor[style].load(in);
				tipTextDis[style].load(in);
			}
			else {
				tipTextColor[style] = new ColorReference(toolTipFontColor[style]);
				tipTextDis[style] = new ColorReference(disColor[style], ColorReference.DIS_COLOR);
				tipBgDis[style] = new ColorReference(backColor[style], ColorReference.BACK_COLOR);
				tipBorderDis[style] = new ColorReference(disColor[style], ColorReference.DIS_COLOR);
			}
			
// Misc
			titledBorderColor[style].load(in);
			
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
		
		return true;
  	}
    
    public static void saveTheme(String fn) {
    	saveTheme(new File(fn));
    }
    
    public static void saveTheme(File f) {
  		if(f.exists()) {
  			f.delete();
  		}
  		
  		try {
			DataOutputStream out = new DataOutputStream(
				new FileOutputStream(f));
				
			out.writeInt(FILE_ID_3F);
			out.writeInt(derivedStyle[style]);
// Colors
			mainColor[style].save(out);
			disColor[style].save(out);
    		backColor[style].save(out);
			frameColor[style].save(out);
			sub1Color[style].save(out);
			sub2Color[style].save(out);
			sub3Color[style].save(out);
			sub4Color[style].save(out);
			sub5Color[style].save(out);
			sub6Color[style].save(out);
			sub7Color[style].save(out);
			sub8Color[style].save(out);
    
// Font
  			plainFont[style].save(out);
			boldFont[style].save(out);
			buttonFont[style].save(out);
			passwordFont[style].save(out);
			labelFont[style].save(out);
			comboFont[style].save(out);
			//popupFont[style].save(out); // with version 1.1 popupFont disappeared
			listFont[style].save(out);
			menuFont[style].save(out);
			menuItemFont[style].save(out);
			radioFont[style].save(out);
			checkFont[style].save(out);
			tableFont[style].save(out);
			tableHeaderFont[style].save(out);
			textAreaFont[style].save(out);
			textFieldFont[style].save(out);
  			textPaneFont[style].save(out);
			titledBorderFont[style].save(out);
			toolTipFont[style].save(out);
			treeFont[style].save(out);
			tabFont[style].save(out);
			editorFont[style].save(out);
			frameTitleFont[style].save(out);
			// since 1.3
			internalFrameTitleFont[style].save(out);
			// since 1.3
			internalPaletteTitleFont[style].save(out);
			progressBarFont[style].save(out);

  			buttonFontColor[style].save(out);
			labelFontColor[style].save(out);
			menuFontColor[style].save(out);
			menuItemFontColor[style].save(out);
			radioFontColor[style].save(out);
			checkFontColor[style].save(out);
			tableFontColor[style].save(out);
			tableHeaderFontColor[style].save(out);
			tabFontColor[style].save(out);
			titledBorderFontColor[style].save(out);

			// disappeared with 1.3C
			//toolTipFontColor[style].save(out);
  		
// Progressbar
			progressColor[style].save(out);
			progressTrackColor[style].save(out);
			progressBorderColor[style].save(out);
			progressDarkColor[style].save(out);
			progressLightColor[style].save(out);
			// since 1.1
			progressSelectForeColor[style].save(out);
			progressSelectBackColor[style].save(out);
  		
// Text	
  			textBgColor[style].save(out);
			textTextColor[style].save(out);
			// since 1.3
			textCaretColor[style].save(out);
			editorPaneBgColor[style].save(out);
			textPaneBgColor[style].save(out);
			desktopPaneBgColor[style].save(out);
			
			textSelectedBgColor[style].save(out);
			textSelectedTextColor[style].save(out);
			textDisabledBgColor[style].save(out);
			textBorderColor[style].save(out);
    		textBorderDarkColor[style].save(out);
			textBorderLightColor[style].save(out);
			textBorderDisabledColor[style].save(out);
			textBorderDarkDisabledColor[style].save(out);
			textBorderLightDisabledColor[style].save(out);
			out.writeInt(textInsets[style].top);
			out.writeInt(textInsets[style].left);
			out.writeInt(textInsets[style].bottom);
			out.writeInt(textInsets[style].right);
    	
// Button    	
			out.writeBoolean(buttonRollover[style]);
			out.writeBoolean(buttonFocus[style]);
			out.writeBoolean(buttonFocusBorder[style]);	// new in 1.3
			out.writeBoolean(buttonEnter[style]);		// new in 1.3
			out.writeBoolean(shiftButtonText[style]);	// new in 1.3.04
    		buttonNormalColor[style].save(out);
			buttonRolloverBgColor[style].save(out);
			buttonPressedColor[style].save(out);
			buttonDisabledColor[style].save(out);
			buttonBorderColor[style].save(out);
			buttonDarkColor[style].save(out);
			buttonLightColor[style].save(out);
			buttonBorderDisabledColor[style].save(out);
			buttonDarkDisabledColor[style].save(out);
			buttonLightDisabledColor[style].save(out);
			out.writeInt(buttonMarginTop[style]);
			out.writeInt(buttonMarginLeft[style]);
			out.writeInt(buttonMarginBottom[style]);
			out.writeInt(buttonMarginRight[style]);
			// since 1.3
			out.writeInt(checkMarginTop[style]);
			out.writeInt(checkMarginLeft[style]);
			out.writeInt(checkMarginBottom[style]);
			out.writeInt(checkMarginRight[style]);
			
			buttonRolloverColor[style].save(out);
			buttonDefaultColor[style].save(out);
			buttonCheckColor[style].save(out);
			buttonCheckDisabledColor[style].save(out);
			buttonDisabledFgColor[style].save(out);
			checkDisabledFgColor[style].save(out);
			radioDisabledFgColor[style].save(out);
			out.writeInt(buttonSpreadLight[style]);
			out.writeInt(buttonSpreadDark[style]);
			out.writeInt(buttonSpreadLightDisabled[style]);
			out.writeInt(buttonSpreadDarkDisabled[style]);
    	
// Scrollbar
    		out.writeBoolean(scrollRollover[style]);
			scrollTrackColor[style].save(out);
			scrollTrackDisabledColor[style].save(out);
			scrollTrackBorderColor[style].save(out);
			scrollTrackBorderDisabledColor[style].save(out);

    		// Thumb
    		scrollThumbColor[style].save(out);
			scrollThumbRolloverColor[style].save(out);
			scrollThumbPressedColor[style].save(out);
			scrollThumbDisabledColor[style].save(out);
		
    		// Grip
    		scrollGripLightColor[style].save(out);
			scrollGripDarkColor[style].save(out);
		
    		// Buttons
			scrollButtColor[style].save(out);
			scrollButtRolloverColor[style].save(out);
			scrollButtPressedColor[style].save(out);
			scrollButtDisabledColor[style].save(out);
			out.writeInt(scrollSpreadLight[style]);
			out.writeInt(scrollSpreadDark[style]);
			out.writeInt(scrollSpreadLightDisabled[style]);
			out.writeInt(scrollSpreadDarkDisabled[style]);
		
    		// Arrow
			scrollArrowColor[style].save(out);
			scrollArrowDisabledColor[style].save(out);
    	
    		// Border
    		scrollBorderColor[style].save(out);
			scrollDarkColor[style].save(out);
			scrollLightColor[style].save(out);
			scrollBorderDisabledColor[style].save(out);
			scrollDarkDisabledColor[style].save(out);
			scrollLightDisabledColor[style].save(out);
		
    		// ScrollPane border
    		scrollPaneBorderColor[style].save(out);
    	
// Tabbed
    		tabPaneBorderColor[style].save(out);
    		tabPaneDarkColor[style].save(out);
			tabPaneLightColor[style].save(out);
			tabNormalColor[style].save(out);
    		tabSelectedColor[style].save(out);
    		
    		// since 1.3
    		tabDisabledColor[style].save(out);
    		tabDisabledSelectedColor[style].save(out);
    		tabDisabledTextColor[style].save(out);
    		
			tabBorderColor[style].save(out);
			tabDarkColor[style].save(out);
			tabLightColor[style].save(out);
			tabRolloverColor[style].save(out);
			
			out.writeBoolean(tabRollover[style]);
			
			// since 1.3.05
			out.writeBoolean(tabFocus[style]);
			
			out.writeBoolean(ignoreSelectedBg[style]);
			
			// since 1.3
			out.writeBoolean(fixedTabs[style]);
			
			// since 1.3
			out.writeInt(tabInsets[style].top);
			out.writeInt(tabInsets[style].left);
			out.writeInt(tabInsets[style].bottom);
			out.writeInt(tabInsets[style].right);
			
			out.writeInt(tabAreaInsets[style].top);
			out.writeInt(tabAreaInsets[style].left);
			out.writeInt(tabAreaInsets[style].bottom);
			out.writeInt(tabAreaInsets[style].right);
    	    	
// Slider
    		out.writeBoolean(sliderRolloverEnabled[style]);
    		
    		// since 1.3.05
    		out.writeBoolean(sliderFocusEnabled[style]);
		
			// Thumb
			sliderThumbColor[style].save(out);
			sliderThumbRolloverColor[style].save(out);
			sliderThumbPressedColor[style].save(out);
			sliderThumbDisabledColor[style].save(out);
			sliderBorderColor[style].save(out);
			sliderDarkColor[style].save(out);
			sliderLightColor[style].save(out);
			sliderBorderDisabledColor[style].save(out);
			sliderDarkDisabledColor[style].save(out);
			sliderLightDisabledColor[style].save(out);
			sliderTrackColor[style].save(out);
			sliderTrackBorderColor[style].save(out);
			sliderTrackDarkColor[style].save(out);
			sliderTrackLightColor[style].save(out);
			sliderTickColor[style].save(out);
			sliderTickDisabledColor[style].save(out);
			
			// since 1.3.05
			sliderFocusColor[style].save(out);
		
// Spinner
    		out.writeBoolean(spinnerRollover[style]);

		// Button
			spinnerButtColor[style].save(out);
			spinnerButtRolloverColor[style].save(out);
			spinnerButtPressedColor[style].save(out);
    		spinnerButtDisabledColor[style].save(out);
    		out.writeInt(spinnerSpreadLight[style]);
			out.writeInt(spinnerSpreadDark[style]);
			out.writeInt(spinnerSpreadLightDisabled[style]);
			out.writeInt(spinnerSpreadDarkDisabled[style]);
			spinnerBorderColor[style].save(out);
			spinnerDarkColor[style].save(out);
			spinnerLightColor[style].save(out);
			spinnerBorderDisabledColor[style].save(out);
			spinnerDarkDisabledColor[style].save(out);
			spinnerLightDisabledColor[style].save(out);
		
    		// Arrow
    		spinnerArrowColor[style].save(out);
			spinnerArrowDisabledColor[style].save(out);
		
// Combo
    		comboBorderColor[style].save(out);
			comboDarkColor[style].save(out);
			comboLightColor[style].save(out);
			comboBorderDisabledColor[style].save(out);
			comboDarkDisabledColor[style].save(out);
			comboLightDisabledColor[style].save(out);
			comboSelectedBgColor[style].save(out);
			comboSelectedTextColor[style].save(out);
			comboFocusBgColor[style].save(out);
			comboBgColor[style].save(out);
			comboTextColor[style].save(out);
		
			// Button
    		comboButtColor[style].save(out);
			comboButtRolloverColor[style].save(out);
			comboButtPressedColor[style].save(out);
			comboButtDisabledColor[style].save(out);
			out.writeInt(comboSpreadLight[style]);
			out.writeInt(comboSpreadDark[style]);
			out.writeInt(comboSpreadLightDisabled[style]);
			out.writeInt(comboSpreadDarkDisabled[style]);

    		// Button Border
    		comboButtBorderColor[style].save(out);
			comboButtDarkColor[style].save(out);
			comboButtLightColor[style].save(out);
			comboButtBorderDisabledColor[style].save(out);
			comboButtDarkDisabledColor[style].save(out);
			comboButtLightDisabledColor[style].save(out);
		
			// Arrow
    		comboArrowColor[style].save(out);
			comboArrowDisabledColor[style].save(out);
			out.writeInt(comboInsets[style].top);
			out.writeInt(comboInsets[style].left);
			out.writeInt(comboInsets[style].bottom);
			out.writeInt(comboInsets[style].right);
			out.writeBoolean(comboRollover[style]);
			out.writeBoolean(comboFocus[style]);
		
// Menu
    		menuBarColor[style].save(out);
			menuSelectedTextColor[style].save(out);
			menuPopupColor[style].save(out);
			menuRolloverBgColor[style].save(out);
			menuItemRolloverColor[style].save(out);
			menuBorderColor[style].save(out);
			menuDarkColor[style].save(out);
			menuLightColor[style].save(out);
			menuIconColor[style].save(out);
			menuIconRolloverColor[style].save(out);
			menuIconDisabledColor[style].save(out);
			menuIconShadowColor[style].save(out);
			menuSepDarkColor[style].save(out);
			menuSepLightColor[style].save(out);
			out.writeInt(menuBorderInsets[style].top);
			out.writeInt(menuBorderInsets[style].left);
			out.writeInt(menuBorderInsets[style].bottom);
			out.writeInt(menuBorderInsets[style].right);
			out.writeBoolean(menuRollover[style]);
			// since 1.3
			menuInnerHilightColor[style].save(out);
			menuInnerShadowColor[style].save(out);
			menuOuterHilightColor[style].save(out);
			menuOuterShadowColor[style].save(out);
			menuRolloverFgColor[style].save(out);
			menuDisabledFgColor[style].save(out);
    	
// Toolbar
    		toolBarColor[style].save(out);
			toolBarLightColor[style].save(out);
			toolBarDarkColor[style].save(out);
			toolButtColor[style].save(out);	// since 1.3
			toolButtRolloverColor[style].save(out);
			toolButtPressedColor[style].save(out);
			toolButtSelectedColor[style].save(out);
			toolBorderDarkColor[style].save(out);
			toolBorderLightColor[style].save(out);
			toolBorderColor[style].save(out);
			toolBorderRolloverColor[style].save(out);	// since 1.3
			toolBorderPressedColor[style].save(out);
			toolBorderSelectedColor[style].save(out);
			out.writeBoolean(toolRollover[style]);
			out.writeBoolean(toolFocus[style]);
			
			// since 1.3
			toolGripDarkColor[style].save(out);
			toolGripLightColor[style].save(out);
			toolSepDarkColor[style].save(out);
			toolSepLightColor[style].save(out);
			out.writeInt(toolMarginTop[style]);
			out.writeInt(toolMarginLeft[style]);
			out.writeInt(toolMarginBottom[style]);
			out.writeInt(toolMarginRight[style]);
    	
// List
			listSelectedBgColor[style].save(out);
			listSelectedTextColor[style].save(out);
			// since 1.3
			listBgColor[style].save(out);
			listTextColor[style].save(out);
		
// Tree
			treeBgColor[style].save(out);
			treeTextColor[style].save(out);
			treeTextBgColor[style].save(out);
			treeSelectedTextColor[style].save(out);
			treeSelectedBgColor[style].save(out);
			treeLineColor[style].save(out);
		
// Frame
			frameCaptionColor[style].save(out);
    		frameCaptionDisabledColor[style].save(out);
			frameBorderColor[style].save(out);
			frameDarkColor[style].save(out);
			frameLightColor[style].save(out);
			frameBorderDisabledColor[style].save(out);
			frameDarkDisabledColor[style].save(out);
			frameLightDisabledColor[style].save(out);
			frameTitleColor[style].save(out);
			frameTitleDisabledColor[style].save(out);
		
    		// Button
			frameButtColor[style].save(out);
			frameButtRolloverColor[style].save(out);
			frameButtPressedColor[style].save(out);
			frameButtDisabledColor[style].save(out);
			out.writeInt(frameButtSpreadDark[style]);
			out.writeInt(frameButtSpreadLight[style]);
			out.writeInt(frameButtSpreadDarkDisabled[style]);
			out.writeInt(frameButtSpreadLightDisabled[style]);
			frameButtCloseColor[style].save(out);
			frameButtCloseRolloverColor[style].save(out);
			frameButtClosePressedColor[style].save(out);
			frameButtCloseDisabledColor[style].save(out);
			out.writeInt(frameButtCloseSpreadDark[style]);
			out.writeInt(frameButtCloseSpreadLight[style]);
			out.writeInt(frameButtCloseSpreadDarkDisabled[style]);
			out.writeInt(frameButtCloseSpreadLightDisabled[style]);
		
    		// Button Border
    		frameButtBorderColor[style].save(out);
    		frameButtDarkColor[style].save(out);
			frameButtLightColor[style].save(out);
			frameButtBorderDisabledColor[style].save(out);
			frameButtDarkDisabledColor[style].save(out);
			frameButtLightDisabledColor[style].save(out);
			frameSymbolColor[style].save(out);
			frameSymbolPressedColor[style].save(out);
			frameSymbolDisabledColor[style].save(out);
			frameSymbolDarkColor[style].save(out);
			frameSymbolLightColor[style].save(out);
		
    	// Close Button
			frameButtCloseBorderColor[style].save(out);
    		frameButtCloseDarkColor[style].save(out);
			frameButtCloseLightColor[style].save(out);
			frameButtCloseBorderDisabledColor[style].save(out);
			frameButtCloseDarkDisabledColor[style].save(out);
			frameButtCloseLightDisabledColor[style].save(out);
			frameSymbolCloseColor[style].save(out);
			frameSymbolClosePressedColor[style].save(out);
			frameSymbolCloseDisabledColor[style].save(out);
			frameSymbolCloseDarkColor[style].save(out);
			frameSymbolCloseLightColor[style].save(out);
			out.writeInt(frameSpreadDark[style]);
			out.writeInt(frameSpreadLight[style]);
			out.writeInt(frameSpreadDarkDisabled[style]);
			out.writeInt(frameSpreadLightDisabled[style]);

// Table
			tableBackColor[style].save(out);
			tableHeaderBackColor[style].save(out);
			// since 1.3.6
			tableHeaderArrowColor[style].save(out);
			tableHeaderRolloverBackColor[style].save(out);
			tableHeaderRolloverColor[style].save(out);
			// end since 1.3.6
			tableGridColor[style].save(out);
			tableSelectedBackColor[style].save(out);
			tableSelectedForeColor[style].save(out);
			tableBorderDarkColor[style].save(out);
			tableBorderLightColor[style].save(out);
			tableHeaderDarkColor[style].save(out);
			tableHeaderLightColor[style].save(out);		
			
// Icons
			for(int i = 0; i < 20; i++) {
				colorizer[i][style].save(out);
				out.writeBoolean(colorize[style][i]);
			}
			
// Separator - since 1.3
			sepDarkColor[style].save(out);
			sepLightColor[style].save(out);
			
// ToolTip
			tipBorderColor[style].save(out);
			tipBgColor[style].save(out);
			
			// since 1.3C
			tipBorderDis[style].save(out);
			tipBgDis[style].save(out);
			tipTextColor[style].save(out);
			tipTextDis[style].save(out);
			
// Misc
			titledBorderColor[style].save(out);
			
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
    }
}