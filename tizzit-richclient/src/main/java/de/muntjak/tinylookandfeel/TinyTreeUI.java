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
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalTreeUI;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import de.muntjak.tinylookandfeel.controlpanel.*;

/**
 * TinyTreeUI
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class TinyTreeUI extends MetalTreeUI {
	
	public static ComponentUI createUI(JComponent x) {
		return new TinyTreeUI();
    }
    
	protected void installDefaults() {
		super.installDefaults();

		if(tree.getCellRenderer() instanceof DefaultTreeCellRenderer) {
			DefaultTreeCellRenderer r = (DefaultTreeCellRenderer)tree.getCellRenderer();
			r.setBackgroundNonSelectionColor(Theme.treeTextBgColor[Theme.style].getColor());
			r.setBackgroundSelectionColor(Theme.treeSelectedBgColor[Theme.style].getColor());
			r.setTextNonSelectionColor(Theme.treeTextColor[Theme.style].getColor());
			r.setTextSelectionColor(Theme.treeSelectedTextColor[Theme.style].getColor());
			UIDefaults defaults = UIManager.getDefaults();
			r.setClosedIcon(defaults.getIcon("Tree.closedIcon"));
			r.setOpenIcon(defaults.getIcon("Tree.openIcon"));
			r.setLeafIcon(defaults.getIcon("Tree.leafIcon"));
		}
	}
}
