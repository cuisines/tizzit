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
package de.juwimm.cms.gui;

import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.util.UIConstants;

/**
 * The LookAndFeel class can inform the application about existing look and feels, as well as 
 * switching to one of these.<br>
 * On MacOS currently it does not return any skin - there should not be any option to change the skin.
 * <p>Description: Content Management System</p>
 * <p>Copyright: Copyright (c) 2003, 2004</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public final class LookAndFeel {
	private static Logger log = Logger.getLogger(LookAndFeel.class);
	public static final String NATIVE = "NATIVE";
	private static boolean updateUI = true;

	/*
		static {
			UIManager.put("Label.font", new Font("SansSerif", Font.PLAIN, 11));
			UIManager.put("Button.font", new Font("SansSerif", Font.PLAIN, 11));
			UIManager.put("Component.font", new Font("SansSerif", Font.PLAIN, 11));
			UIManager.put("CheckBox.font", new Font("SansSerif", Font.PLAIN, 11));
			UIManager.put("OptionPane.font", new Font("SansSerif", Font.PLAIN, 11));
			UIManager.put("RadioButton.font", new Font("SansSerif", Font.PLAIN, 11));
			UIManager.put("TextField.font", new Font("SansSerif", Font.PLAIN, 11));
			UIManager.put("ComboBox.font", new Font("SansSerif", Font.PLAIN, 11));
			UIManager.put("MenuItem.font", new Font("SansSerif", Font.PLAIN, 11));
			UIManager.put("Menu.font", new Font("SansSerif", Font.PLAIN, 11));
			UIManager.put("RadioButtonMenuItem.font", new Font("SansSerif", Font.PLAIN, 11));
			UIManager.put("TitledBorder.font", new Font("SansSerif", Font.PLAIN, 11));
			UIManager.put("TabbedPane.font", new Font("SansSerif", Font.PLAIN, 11));
			UIManager.put("List.font", new Font("SansSerif", Font.PLAIN, 11));
		}*/

	private LookAndFeel() {
	}

	public static boolean switchTo(String lookAndFeel, boolean updateUIv) {
		updateUI = updateUIv;
		boolean retVal = switchTo(lookAndFeel);
		updateUI = true;
		return retVal;
	}

	/**
	 * Ignores any statement under MacOS
	 * @param lookAndFeel
	 * @return
	 */
	public static boolean switchTo(String lookAndFeel) {
		/*if (!Constants.isClientOS(Constants.OS_WINDOWS)) {
			if (log.isDebugEnabled()) log.debug("Setting Fonts for all Operating Systems except Windows");
			UIManager.put("Label.font", new Font("SansSerif", Font.PLAIN, 11));
			UIManager.put("Button.font", new Font("SansSerif", Font.PLAIN, 11));
			UIManager.put("Component.font", new Font("SansSerif", Font.PLAIN, 11));
			UIManager.put("CheckBox.font", new Font("SansSerif", Font.PLAIN, 11));
			UIManager.put("OptionPane.font", new Font("SansSerif", Font.PLAIN, 11));
			UIManager.put("RadioButton.font", new Font("SansSerif", Font.PLAIN, 11));
			UIManager.put("TextField.font", new Font("SansSerif", Font.PLAIN, 11));
			UIManager.put("ComboBox.font", new Font("SansSerif", Font.PLAIN, 11));
			UIManager.put("MenuItem.font", new Font("SansSerif", Font.PLAIN, 11));
			UIManager.put("Menu.font", new Font("SansSerif", Font.PLAIN, 11));
			UIManager.put("RadioButtonMenuItem.font", new Font("SansSerif", Font.PLAIN, 11));
			UIManager.put("TitledBorder.font", new Font("SansSerif", Font.PLAIN, 11));
			UIManager.put("TabbedPane.font", new Font("SansSerif", Font.PLAIN, 11));
			UIManager.put("List.font", new Font("SansSerif", Font.PLAIN, 11));
		}
*/
		try {
			//PlasticLookAndFeel.setPlasticTheme(new com.jgoodies.looks.plastic.theme.ExperienceRoyale());
			//UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
			lookAndFeel.getClass().getResource("YQSilver.theme");
			UIManager.setLookAndFeel("de.muntjak.tinylookandfeel.TinyLookAndFeel");
			//Thread.currentThread().getContextClassLoader().getResource("YQSilver.theme");

			// continuous layout on frame resize
			Toolkit.getDefaultToolkit().setDynamicLayout(true);
			// no flickering on resize
			System.setProperty("sun.awt.noerasebackground", "true");
			SwingUtilities.updateComponentTreeUI(UIConstants.getMainFrame());
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug(e.getMessage(), e);
			}
		}

		/*boolean retVal = false;
		if (Constants.isClientOS(Constants.OS_MACOSX)) { return true; }
		try {
			if (lookAndFeel != null) {
				if (lookAndFeel.equals(NATIVE)) {
					if (Constants.isClientOS(Constants.OS_LINUX)) {
						try {
							log.info("Trying GTK+ Theme");
							UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
							log.info("SUCC GTK+!");
						} catch (Exception exe) {
							log.error("It wasn't possible to switch to the GTK+ Theme");
							UIManager.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
						}
					} else {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					}
					SwingUtilities.updateComponentTreeUI(UIConstants.getMainFrame());
					retVal = true;
				} else {
					UIManager.setLookAndFeel(lookAndFeel);
					SwingUtilities.updateComponentTreeUI(UIConstants.getMainFrame());
					retVal = true;
				}
			}
		} catch (Exception exe) {
			if (log.isDebugEnabled()) {
				log.debug(exe.getMessage(), exe);
			}
		}*/
		return true;
	}

	public static String determineLookAndFeel() {
		String lookAndFeel = "";

		if (Constants.isClientOS(Constants.OS_MACOSX)) {
			lookAndFeel = UIManager.getSystemLookAndFeelClassName();
		} else if (Constants.isClientOS(Constants.OS_WINDOWS)) {
			lookAndFeel = UIManager.getSystemLookAndFeelClassName();
		} else if (Constants.isClientOS(Constants.OS_LINUX)) {
			lookAndFeel = LookAndFeel.NATIVE;
		}
		return (lookAndFeel);
	}
}