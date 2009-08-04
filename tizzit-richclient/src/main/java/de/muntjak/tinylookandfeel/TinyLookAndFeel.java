/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
*	Tiny Look and Feel														   *
*															                   *
*  (C) Copyright 2003 - 2005 by Hans Bickel									   *
*                                                                              *
*                                                                              *
* The starting point for Tiny Look and Feel was the XP Look and Feel written   *
* by Stefan Krause.  														   *
* The original header of this file was:                                        *
** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
*	XP Look and Feel														   *
*															                   *
*  (C) Copyright 2002, by Stefan Krause, Taufik Romdhane and Contributors      *
*                                                                              *
*                                                                              *
* The XP Look and Feel started as as extension to the Metouia Look and Feel.   *
* The original header of this file was:                                        *
** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
*        Metouia Look And Feel: a free pluggable look and feel for java        *
*                         http://mlf.sourceforge.net                           *
*          (C) Copyright 2002, by Taoufik Romdhane and Contributors.           *
*                                                                              *
*   This library is free software; you can redistribute it and/or modify it    *
*   under the terms of the GNU Lesser General Public License as published by   *
*   the Free Software Foundation; either version 2.1 of the License, or (at    *
*   your option) any later version.                                            *
*                                                                              *
*   This library is distributed in the hope that it will be useful,            *
*   but WITHOUT ANY WARRANTY; without even the implied warranty of             *
*   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                       *
*   See the GNU Lesser General Public License for more details.                *
*                                                                              *
*   You should have received a copy of the GNU General Public License along    *
*   with this program; if not, write to the Free Software Foundation, Inc.,    *
*   59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.                    *
*                                                                              *
*   Original Author:  Taoufik Romdhane                                         *
* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package de.muntjak.tinylookandfeel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;

import de.muntjak.tinylookandfeel.borders.*;

/**
 * TinyLookAndFeel 
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class TinyLookAndFeel extends MetalLookAndFeel {
	
	/** Will be set to true in ControlPanel.main */
	public static boolean controlPanelInstantiated = false;
	
	// this constant is the minimum width for TitlePane.TitlePaneLayout,
	// it is from interest only if a frame or dialog is decorated.
	static final int MINIMUM_FRAME_WIDTH = 104;
	
	// the minimum width for internal frames and palettes
	static final int MINIMUM_INTERNAL_FRAME_WIDTH = 32;
	
	// -1 = unspecified
	// 0  = JRE v1.4
	// 1  = JRE v1.5 or higher
	private static int is1dot4 = -1;
	
	public static final String VERSION_STRING = "1.3.8";
	public static final String DATE_STRING = "2007-6-17";

	protected static TinyDefaultTheme defaultTheme;

	/**
	 * The installation state of the TinyLaF Look and Feel.
	 */
	private static boolean isInstalled = false;

	/**
	 * The installation state of the TinyLaF Theme.
	 */
	private static boolean themeHasBeenSet = false;

	/**
     * UIManager.setLookAndFeel calls this method before the first
     * call (and typically the only call) to getDefaults(). Subclasses
     * should do any one-time setup they need here, rather than 
     * in a static initializer, because look and feel class objects
     * may be loaded just to discover that isSupportedLookAndFeel()
     * returns false.
     *
     * @see #uninitialize
     * @see UIManager#setLookAndFeel
     */
	public void initialize() {
		super.initialize();
		
		if(!isInstalled) {
			isInstalled = true;
			
			// Note: We must know (for TinyMouseHandler) on
			// which JRE version we are running. Because we
			// might be an applet, we execute a privileged block.
			if(is1dot4 == -1) {
				String version = (String)AccessController.doPrivileged(
					new PrivilegedAction() {
			            public Object run() {
			                return System.getProperty("java.version");
			            }
					}
				);

				if(version != null) {
					is1dot4 =
						version.startsWith("1.0") ||
						version.startsWith("1.1") ||
						version.startsWith("1.2") ||
						version.startsWith("1.3") ||
						version.startsWith("1.4") ? 0 : 1;
				}
				else {
					// give up - assume we are 1.5 or higher
					is1dot4 = 1;
				}
			}

			searchDefaultTheme();
			UIManager.installLookAndFeel(new UIManager.LookAndFeelInfo(
				"TinyLookAndFeel", "de.muntjak.tinylookandfeel.TinyLookAndFeel"));
		}
		
		// Execute this one even if isInstalled is true
		// New in 1.3.6
		if(!"false".equals(System.getProperty("altClosesMenu"))) {
			KeyboardFocusManager.getCurrentKeyboardFocusManager().
            	addKeyEventPostProcessor(TinyMenuUI.altProcessor);
		}
	}
	
	/**
	 * 
	 * @return true if we are running Java 1.4 or lower,
	 * false otherwise
	 */
	public static boolean is1dot4() {
		return (is1dot4 == 0);
	}
	
	public void uninitialize() {
		super.uninitialize();

		KeyboardFocusManager.getCurrentKeyboardFocusManager().
        	removeKeyEventPostProcessor(TinyMenuUI.altProcessor);
	}
	
	private void searchDefaultTheme() {
		// only if running without the control panel
		if(controlPanelInstantiated) return;

		String loadedFrom = null;
		URL defaultURL = TinyLookAndFeel.class.getResource("/" + Theme.DEFAULT_THEME);
		
		if(Theme.loadTheme(defaultURL, Theme.CUSTOM_STYLE)) {
			Theme.style = Theme.CUSTOM_STYLE;
			loadedFrom = defaultURL.toExternalForm();
		}
		else {
			defaultURL = Thread.currentThread().getContextClassLoader().getResource(Theme.DEFAULT_THEME);

			
			if(Theme.loadTheme(defaultURL, Theme.CUSTOM_STYLE)) {
				Theme.style = Theme.CUSTOM_STYLE;
				loadedFrom = defaultURL.toExternalForm();
			}
			else {
				try {
					defaultURL = new File(System.getProperty("user.home"), Theme.DEFAULT_THEME).toURI().toURL();
					
					if(Theme.loadTheme(defaultURL, Theme.CUSTOM_STYLE)) {
						Theme.style = Theme.CUSTOM_STYLE;
						loadedFrom = defaultURL.toExternalForm();
					}
					else {
						defaultURL = new File(System.getProperty("user.dir"), Theme.DEFAULT_THEME).toURI().toURL();
					
						if(Theme.loadTheme(defaultURL, Theme.CUSTOM_STYLE)) {
							Theme.style = Theme.CUSTOM_STYLE;
							loadedFrom = defaultURL.toExternalForm();
						}
						// else we give up
					}
				}
				catch (MalformedURLException ignore) {}
				
				// AccessControlException is thrown when running
				// with Java Web Start
				catch(AccessControlException ignore) {}
			}
		}
		
		String info = "TinyLaF v" + VERSION_STRING + "\n";
		if(loadedFrom == null) {
			System.out.println(info +
			"'Default.theme' not found - using YQ default theme.");
		}
		else {
			System.out.println(info + "Theme: " + loadedFrom);
		}
	}

	/**
	 * Return a string that identifies this look and feel.  This string
	 * will be used by applications/services that want to recognize
	 * well known look and feel implementations.  Presently
	 * the well known names are "Motif", "Windows", "Mac", "Metal".  Note
	 * that a LookAndFeel derived from a well known superclass
	 * that doesn't make any fundamental changes to the look or feel
	 * shouldn't override this method.
	 *
	 * @return The TinyLaF Look and Feel identifier.
	 */
	public String getID() {
		return "TinyLookAndFeel";
	}

	/**
	 * Return a short string that identifies this look and feel, e.g.
	 * "CDE/Motif".  This string should be appropriate for a menu item.
	 * Distinct look and feels should have different names, e.g.
	 * a subclass of MotifLookAndFeel that changes the way a few components
	 * are rendered should be called "CDE/Motif My Way"; something
	 * that would be useful to a user trying to select a L&F from a list
	 * of names.
	 *
	 * @return The look and feel short name.
	 */
	public String getName() {
		return "TinyLookAndFeel";
	}

	/**
	 * Return a one line description of this look and feel implementation,
	 * e.g. "The CDE/Motif Look and Feel".   This string is intended for
	 * the user, e.g. in the title of a window or in a ToolTip message.
	 *
	 * @return The look and feel short description.
	 */
	public String getDescription() {
		return "TinyLookAndFeel";
	}

	/**
	 * If the underlying platform has a "native" look and feel, and this
	 * is an implementation of it, return true.  For example a CDE/Motif
	 * look and implementation would return true when the underlying
	 * platform was Solaris.
	 */
	public boolean isNativeLookAndFeel() {
		return false;
	}

	/**
	 * Return true if the underlying platform supports and or permits
	 * this look and feel.  This method returns false if the look
	 * and feel depends on special resources or legal agreements that
	 * aren't defined for the current platform.
	 */
	public final boolean isSupportedLookAndFeel() {
		return true;
	}

	public boolean getSupportsWindowDecorations() {
		return true;
	}

	/**
	 * Initializes the uiClassID to BasicComponentUI mapping.
	 * The JComponent classes define their own uiClassID constants. This table
	 * must map those constants to a BasicComponentUI class of the appropriate
	 * type.
	 *
	 * @param table The ui defaults table.
	 */
	protected void initClassDefaults(UIDefaults table) {
		super.initClassDefaults(table);

		table.putDefaults(new Object[] {
			"ButtonUI", "de.muntjak.tinylookandfeel.TinyButtonUI",
			"CheckBoxUI", "de.muntjak.tinylookandfeel.TinyCheckBoxUI",
			"TextFieldUI", "de.muntjak.tinylookandfeel.TinyTextFieldUI",
			"TextAreaUI", "de.muntjak.tinylookandfeel.TinyTextAreaUI",
			/* New in 1.3.6: Removed entry for FormattedTextFieldUI */
			"PasswordFieldUI", "de.muntjak.tinylookandfeel.TinyPasswordFieldUI",
			"EditorPaneUI", "de.muntjak.tinylookandfeel.TinyEditorPaneUI",
			"TextPaneUI", "de.muntjak.tinylookandfeel.TinyTextPaneUI",
			"SliderUI", "de.muntjak.tinylookandfeel.TinySliderUI",
			"SpinnerUI", "de.muntjak.tinylookandfeel.TinySpinnerUI",
			"ToolBarUI", "de.muntjak.tinylookandfeel.TinyToolBarUI",			
			"ToolBarSeparatorUI", "de.muntjak.tinylookandfeel.TinyToolBarSeparatorUI",
			"MenuBarUI", "de.muntjak.tinylookandfeel.TinyMenuBarUI",
			"MenuUI", "de.muntjak.tinylookandfeel.TinyMenuUI",
			"MenuItemUI", "de.muntjak.tinylookandfeel.TinyMenuItemUI",
			"CheckBoxMenuItemUI", "de.muntjak.tinylookandfeel.TinyCheckBoxMenuItemUI",
			"RadioButtonMenuItemUI", "de.muntjak.tinylookandfeel.TinyRadioButtonMenuItemUI",
			"ScrollBarUI", "de.muntjak.tinylookandfeel.TinyScrollBarUI",
			"TabbedPaneUI", "de.muntjak.tinylookandfeel.TinyTabbedPaneUI",
			"ToggleButtonUI", "de.muntjak.tinylookandfeel.TinyButtonUI",
			"ScrollPaneUI", "de.muntjak.tinylookandfeel.TinyScrollPaneUI",
			"ProgressBarUI", "de.muntjak.tinylookandfeel.TinyProgressBarUI",
			"InternalFrameUI", "de.muntjak.tinylookandfeel.TinyInternalFrameUI",
			"RadioButtonUI", "de.muntjak.tinylookandfeel.TinyRadioButtonUI",
			"ComboBoxUI", "de.muntjak.tinylookandfeel.TinyComboBoxUI",
			"PopupMenuSeparatorUI", "de.muntjak.tinylookandfeel.TinyPopupMenuSeparatorUI",
			"SeparatorUI", "de.muntjak.tinylookandfeel.TinySeparatorUI",
			"SplitPaneUI", "de.muntjak.tinylookandfeel.TinySplitPaneUI",
			"FileChooserUI", "de.muntjak.tinylookandfeel.TinyFileChooserUI",
			"ListUI", "de.muntjak.tinylookandfeel.TinyListUI",
			"TreeUI", "de.muntjak.tinylookandfeel.TinyTreeUI",
			"LabelUI", "de.muntjak.tinylookandfeel.TinyLabelUI",
			"TableUI", "de.muntjak.tinylookandfeel.TinyTableUI",
			"TableHeaderUI", "de.muntjak.tinylookandfeel.TinyTableHeaderUI",
			"ToolTipUI", "de.muntjak.tinylookandfeel.TinyToolTipUI",
			"RootPaneUI", "de.muntjak.tinylookandfeel.TinyRootPaneUI",
			"DesktopPaneUI", "de.muntjak.tinylookandfeel.TinyDesktopPaneUI"
			});
	}

	/**
	 * Creates the default theme and installs it.
	 * The TinyDefaultTheme is used as default.
	 */
	protected void createDefaultTheme() {
//		if(JFrame.isDefaultLookAndFeelDecorated()) {
//			System.setProperty("sun.awt.noerasebackground", "true");
//		}
		
		// Note: up to 1.3.02, the if-clause below prevented
		// the theme from being re-loaded when refreshing an Applet page
			
//		if(!themeHasBeenSet) {
			defaultTheme = new TinyDefaultTheme();
			setCurrentTheme(defaultTheme);
//		}
	}

	/**
	 * Sets the current color theme.
	 * Warning: The theme must be an instance of TinyDefaultTheme!
	 *
	 * @param theme the theme to install.
	 */
	public static void setCurrentTheme(MetalTheme theme) {
		MetalLookAndFeel.setCurrentTheme(theme);
		themeHasBeenSet = true;
	}

	/**
	 * Initializes the default values for many ui widgets and puts them in the
	 * given ui defaults table.
	 * Here is the place where borders can be changed.
	 *
	 * @param table The ui defaults table.
	 */
	protected void initComponentDefaults(UIDefaults table) {
		super.initComponentDefaults(table);

		// Replace Metal borders:
		Border border = new EmptyBorder(0, 0, 0, 0);
		table.put("Button.border",
			new BorderUIResource.CompoundBorderUIResource(
				new TinyButtonBorder(),
				new BasicBorders.MarginBorder()));
		table.put("FormattedTextField.border", new TinyTextFieldBorder());
		table.put("TextField.border", new TinyTextFieldBorder());
		table.put("PasswordField.border", new TinyTextFieldBorder());
		table.put("ComboBox.border", border);
		table.put("Table.scrollPaneBorder", new TinyTableScrollPaneBorder());
		table.put("TableHeader.cellBorder", new TinyTableHeaderBorder());
		table.put("TableHeader.cellRolloverBorder", new TinyTableHeaderRolloverBorder());
		table.put("Spinner.border", new TinyTextFieldBorder(new Insets(2, 2, 2, 2)));
		table.put("ProgressBar.border", new TinyProgressBarBorder());
		table.put("ToolBar.border", new TinyToolBarBorder());
		table.put("ToolTip.border", new BorderUIResource(new TinyToolTipBorder(true)));
		table.put("ToolTip.borderInactive", new BorderUIResource(new TinyToolTipBorder(false)));

		border = new TinyInternalFrameBorder();		
		table.put("InternalFrame.border", border);
		table.put("InternalFrame.paletteBorder", border);
		table.put("InternalFrame.optionDialogBorder", border);

		border = new EmptyBorder(2, 4, 2, 4);
		table.put("Menu.border", border);
		table.put("MenuItem.border", border);
		table.put("CheckBoxMenuItem.border", border);
		table.put("RadioButtonMenuItem.border", border);

		table.put("PopupMenu.border", new TinyPopupMenuBorder());
		table.put("ScrollPane.border", new TinyScrollPaneBorder());
		table.put("Slider.trackWidth", new Integer(4));
		
		// Note: Margins correspond to borders. In TinyLaF 1.2 checkboxes
		// and radio buttons had the Metal border which itself adds insets
		// of (2, 2, 2, 2) - so a 1.3 checkbox/radio button has the same
		// visible margin as a 1.2 checkbox/radio button because margins
		// in 1.2 where (0, 0, 0, 0)
		table.put("CheckBox.border", new BasicBorders.MarginBorder());
		table.put("RadioButton.border", new BasicBorders.MarginBorder());
		table.put("RadioButton.margin", new InsetsUIResource(2, 2, 2, 2));
		table.put("CheckBox.margin", new InsetsUIResource(2, 2, 2, 2));

		// Tweak some subtle values:
		table.put("SplitPane.dividerSize", new Integer(7));
		table.put("InternalFrame.normalTitleFont", new Font("dialog", Font.BOLD, 13));
		
		// New in 1.3.7 - value is evaluated at
		// javax.swing.plaf.basic.BasicFileChooserUI.installDefaults(JFileChooser)
		if(System.getProperty("os.name").toLowerCase().startsWith("linux")) {
			table.put("FileChooser.readOnly", Boolean.TRUE);
		}

		table.put("TabbedPane.tabInsets", new Insets(1, 6, 4, 6));
		table.put("TabbedPane.selectedTabPadInsets", new Insets(2, 2, 1, 2));
		table.put("TabbedPane.unselected", new ColorUIResource(0, 0, 0));
		table.put("TabbedPane.tabAreaInsets", new Insets(6, 2, 0, 0));
		table.put("TabbedPane.contentBorderInsets", new Insets(1, 1, 3, 3));

		table.put("PopupMenu.foreground", new Color(255, 0, 0));

		table.put("RootPane.colorChooserDialogBorder", TinyFrameBorder.getInstance());
		table.put("RootPane.errorDialogBorder", TinyFrameBorder.getInstance());
		table.put("RootPane.fileChooserDialogBorder", TinyFrameBorder.getInstance());
		table.put("RootPane.frameBorder", TinyFrameBorder.getInstance());
		table.put("RootPane.informationDialogBorder", TinyFrameBorder.getInstance());
		table.put("RootPane.plainDialogBorder", TinyFrameBorder.getInstance());
		table.put("RootPane.questionDialogBorder", TinyFrameBorder.getInstance());
		table.put("RootPane.warningDialogBorder", TinyFrameBorder.getInstance());

		table.put("CheckBoxMenuItem.checkIcon", MenuItemIconFactory.getCheckBoxMenuItemIcon());
		table.put("RadioButtonMenuItem.checkIcon", MenuItemIconFactory.getRadioButtonMenuItemIcon());
		table.put("Menu.arrowIcon", MenuItemIconFactory.getMenuArrowIcon());
		
		table.put("InternalFrame.frameTitleHeight", new Integer(25));
		table.put("InternalFrame.paletteTitleHeight", new Integer(16));
		table.put("InternalFrame.icon", loadIcon("InternalFrameIcon.png", this));

		table.put("Tree.expandedIcon", loadIcon("TreeMinusIcon.png", this));
		table.put("Tree.collapsedIcon", loadIcon("TreePlusIcon.png", this));
		table.put("Tree.openIcon", loadIcon("TreeFolderOpenedIcon.png", this));
		table.put("Tree.closedIcon", loadIcon("TreeFolderClosedIcon.png", this));
		table.put("Tree.leafIcon", loadIcon("TreeLeafIcon.png", this));
		
		table.put("FileView.directoryIcon", loadIcon("DirectoryIcon.png", this));
		table.put("FileView.computerIcon", loadIcon("ComputerIcon.png", this));
		table.put("FileView.fileIcon", loadIcon("FileIcon.png", this));
		table.put("FileView.floppyDriveIcon", loadIcon("FloppyIcon.png", this));
		table.put("FileView.hardDriveIcon", loadIcon("HarddiskIcon.png", this));
		
		table.put("FileChooser.detailsViewIcon", loadIcon("FileDetailsIcon.png", this));
		table.put("FileChooser.homeFolderIcon", loadIcon("HomeFolderIcon.png", this));
		table.put("FileChooser.listViewIcon", loadIcon("FileListIcon.png", this));
		table.put("FileChooser.newFolderIcon", loadIcon("NewFolderIcon.png", this));
		table.put("FileChooser.upFolderIcon", loadIcon("ParentDirectoryIcon.png", this));
	
		table.put("OptionPane.errorIcon", loadIcon("ErrorIcon.png", this));
		table.put("OptionPane.informationIcon", loadIcon("InformationIcon.png", this));
		table.put("OptionPane.warningIcon", loadIcon("WarningIcon.png", this));
		table.put("OptionPane.questionIcon", loadIcon("QuestionIcon.png", this));
	}

	public static Icon getUncolorizedSystemIcon(int index) {
		switch(index) {
			case 0:
			return loadIcon("InternalFrameIcon.png", null);
			case 1:
			return loadIcon("TreeFolderClosedIcon.png", null);
			case 2:
			return loadIcon("TreeFolderOpenedIcon.png", null);
			case 3:
			return loadIcon("TreeLeafIcon.png", null);
			case 4:
			return loadIcon("TreeMinusIcon.png", null);
			case 5:
			return loadIcon("TreePlusIcon.png", null);
			case 6:
			return loadIcon("ComputerIcon.png", null);
			case 7:
			return loadIcon("FloppyIcon.png", null);
			case 8:
			return loadIcon("HarddiskIcon.png", null);
			case 9:
			return loadIcon("DirectoryIcon.png", null);
			case 10:
			return loadIcon("FileIcon.png", null);
			case 11:
			return loadIcon("ParentDirectoryIcon.png", null);
			case 12:
			return loadIcon("HomeFolderIcon.png", null);
			case 13:
			return loadIcon("NewFolderIcon.png", null);
			case 14:
			return loadIcon("FileListIcon.png", null);
			case 15:
			return loadIcon("FileDetailsIcon.png", null);
			case 16:
			return loadIcon("InformationIcon.png", null);
			case 17:
			return loadIcon("QuestionIcon.png", null);
			case 18:
			return loadIcon("WarningIcon.png", null);
			default:
			return loadIcon("ErrorIcon.png", null);
		}
	}

	public static String getSystemIconName(int index) {
		switch(index) {
			case 0:
			return "InternalFrame.icon";
			case 1:
			return "Tree.closedIcon";
			case 2:
			return "Tree.openIcon";
			case 3:
			return "Tree.leafIcon";
			case 4:
			return "Tree.expandedIcon";
			case 5:
			return "Tree.collapsedIcon";
			case 6:
			return "FileView.computerIcon";
			case 7:
			return "FileView.floppyDriveIcon";
			case 8:
			return "FileView.hardDriveIcon";
			case 9:
			return "FileView.directoryIcon";
			case 10:
			return "FileView.fileIcon";
			case 11:
			return "FileChooser.upFolderIcon";
			case 12:
			return "FileChooser.homeFolderIcon";
			case 13:
			return "FileChooser.newFolderIcon";
			case 14:
			return "FileChooser.listViewIcon";
			case 15:
			return "FileChooser.detailsViewIcon";
			case 16:
			return "OptionPane.informationIcon";
			case 17:
			return "OptionPane.questionIcon";
			case 18:
			return "OptionPane.warningIcon";
			default:
			return "OptionPane.errorIcon";
		}
	}

	/**
	 * Loads an image icon.
	 *
	 * @param file The image file name.
	 * @param invoker The refence of the invoking class, whose classloader will be
	 *                used for loading the image.
	 */
	public static ImageIcon loadIcon(final String fileName, final Object invoker) {
		// This should work for both applications and applets
		URL url = Thread.currentThread().getContextClassLoader().getResource(
			"tinylookandfeel/icons/" + fileName);
		if(url == null) {
			// Another try
			url = TinyLookAndFeel.class.getResource(
				"/tinylookandfeel/icons/" + fileName);
			
			if(url == null) {
				System.err.println("TinyLaF: Icon directory could not be resolved.");
				return null;
			}
		}
		
		return new ImageIcon(url);
	}
}