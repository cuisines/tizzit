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

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.metal.MetalInternalFrameTitlePane;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 * TinyInternalFrameTitlePane is not an UI-delegate but a JComponent.
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class TinyInternalFrameTitlePane extends BasicInternalFrameTitlePane
	implements LayoutManager
{
	
	protected boolean isPalette = false;

	/**
	 * The buttons width, calculated at runtime.
	 */
	private int buttonsWidth;

	/**
	 * Installs some default values.
	 * Reads the internalframe title height from the ui defaults table.
	 */
	protected void installDefaults() {
		super.installDefaults();
		frame.setFrameIcon(UIManager.getDefaults().getIcon("InternalFrame.icon"));
	}
	
	protected PropertyChangeListener createPropertyChangeListener() {
        return new TinyPropertyChangeHandler();
    }

	/**
	 * This constructor creates a title pane for the given internal frame
	 * instance.
	 *
	 * @param frame The internal frame that needs a title pane.
	 */
	public TinyInternalFrameTitlePane(JInternalFrame frame) {
		super(frame);
	}

	protected void paintTitleBackground(Graphics g) {
	}

	public boolean isFrameSelected() {
		return frame.isSelected();
	}

	public boolean isFrameMaximized() {
		return frame.isMaximum();
	}

	/**
		* Paints this component.
		*
		* @param g The graphics context to use.
		*/
	public void paintComponent(Graphics g) {
		if(Theme.frameIsTransparent[Theme.derivedStyle[Theme.style]]) {
			frame.setOpaque(false);
		}

		boolean leftToRight = frame.getComponentOrientation().isLeftToRight();
		boolean isSelected = frame.isSelected();

		int width = getWidth();
		int height = getHeight();

		Color foreground = MetalLookAndFeel.getWindowTitleInactiveForeground();
		int titleLength = 0;
		int xOffset = leftToRight ? 2 : width - 2;
		String frameTitle = frame.getTitle();

		Icon icon = frame.getFrameIcon();

		if(icon != null) {
			int iconY = Math.round((height - icon.getIconHeight()) / 2.0f);

			if(!leftToRight) {
				xOffset -= icon.getIconWidth();
			}

			if(Theme.derivedStyle[Theme.style] == Theme.W99_STYLE) {
				iconY += 1;
			}

			icon.paintIcon(frame, g, xOffset, iconY);
			xOffset += leftToRight ? icon.getIconWidth() + 2 : -2;
		}

		if(frameTitle != null) {
			Font f = getFont();
			g.setFont(f);
			FontMetrics fm = g.getFontMetrics();
			titleLength = fm.stringWidth(frameTitle);

			int yOffset = ((height - fm.getHeight()) / 2) + fm.getAscent() + 1;
			if(!leftToRight)
				xOffset -= titleLength;

			if(isSelected) {
				g.setColor(Theme.frameTitleColor[Theme.style].getColor());
				g.drawString(frameTitle, xOffset, yOffset);

				xOffset += leftToRight ? titleLength + 2 : -2;
			} else {
				// for an inactive window
				g.setColor(Theme.frameTitleDisabledColor[Theme.style].getColor());

				g.drawString(frameTitle, xOffset, yOffset);

				xOffset += leftToRight ? titleLength + 2 : -2;
			}
		}
	}

	/**
	 * Creates the layout manager for the title pane.
	 *
	 * @return The layout manager for the title pane.
	 */
	protected LayoutManager createLayout() {
		return this;
	}
	
	protected void addSubComponents() {
		super.addSubComponents();
		
//		if(menuBar != null) {
//			menuBar.setOpaque(false);
//		}
	}

	protected void setButtonIcons() {
	}

	/**
	 * This listener is added to the maximize, minimize and close button to 
	 * manage the rollover status of the buttons
	 * 
	 */
	class RolloverListener implements MouseListener {
		JButton button;
		Action action;

		public RolloverListener(JButton b, Action a) {
			button = b;
			action = a;
		}

		public void mouseClicked(MouseEvent e) {
			action.actionPerformed(new ActionEvent(this, Event.ACTION_EVENT, button.getText()));
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
			button.getModel().setRollover(true);

			if(!button.isEnabled()) {
				button.setEnabled(true);
			}
			button.repaint();
		}

		public void mouseExited(MouseEvent e) {
			button.getModel().setRollover(false);
			if(!frame.isSelected()) {
				button.setEnabled(false);
			}
			button.repaint();
		}
	}

	static TinyWindowButtonUI iconButtonUI;
	static TinyWindowButtonUI maxButtonUI;
	static TinyWindowButtonUI closeButtonUI;

	/**
	 * Creates the buttons of the title pane and initilizes their actions.
	 */
	protected void createButtons() {
		if(iconButtonUI == null) {
			iconButtonUI = TinyWindowButtonUI.createButtonUIForType(TinyWindowButtonUI.MINIMIZE);
			maxButtonUI = TinyWindowButtonUI.createButtonUIForType(TinyWindowButtonUI.MAXIMIZE);
			closeButtonUI = TinyWindowButtonUI.createButtonUIForType(TinyWindowButtonUI.CLOSE);
		}

		iconButton = new SpecialUIButton(iconButtonUI);
		iconButton.addActionListener(iconifyAction);
		iconButton.setRolloverEnabled(true);
		iconButton.addMouseListener(new RolloverListener(iconButton, iconifyAction));

		maxButton = new SpecialUIButton(maxButtonUI);
		maxButton.addActionListener(maximizeAction);
		maxButton.setRolloverEnabled(true);
		maxButton.addMouseListener(new RolloverListener(maxButton, maximizeAction));

		closeButton = new SpecialUIButton(closeButtonUI);
		closeButton.addActionListener(closeAction);
		closeButton.setRolloverEnabled(true);
		closeButton.addMouseListener(new RolloverListener(closeButton, closeAction));

		iconButton.putClientProperty("externalFrameButton", Boolean.FALSE);
		maxButton.putClientProperty("externalFrameButton", Boolean.FALSE);
		closeButton.putClientProperty("externalFrameButton", Boolean.FALSE);

		iconButton.getAccessibleContext().setAccessibleName(UIManager.getString("InternalFrameTitlePane.iconifyButtonAccessibleName"));

		maxButton.getAccessibleContext().setAccessibleName(UIManager.getString("InternalFrameTitlePane.maximizeButtonAccessibleName"));

		closeButton.getAccessibleContext().setAccessibleName(UIManager.getString("InternalFrameTitlePane.closeButtonAccessibleName"));

		if(frame.isSelected()) {
			activate();
		} else {
			deactivate();
		}
	}

	/**
	 * Paints the title pane for a palette.
	 *
	 * @param g The graphics context to use.
	 */
	public void paintPalette(Graphics g) {
	}

	/**
	 * Adds the specified component with the specified name to the layout.
	 *
	 * @param name the component name
	 * @param mainColor the component to be added
	 */
	public void addLayoutComponent(String name, Component c) {
	}

	/**
	 * Removes the specified component from the layout.
	 *
	 * @param mainColor the component to be removed
	 */
	public void removeLayoutComponent(Component c) {
	}

	/**
	 * Calculates the preferred size dimensions for the specified
	 * panel given the components in the specified parent container.
	 *
	 * @param mainColor the component to be laid out
	 */
	public Dimension preferredLayoutSize(Container c) {
		return getPreferredSize(c);
	}

	/**
	 * Gets the preferred size of the given container.
	 *
	 * @return The preferred size of the given container.
	 */
	public Dimension getPreferredSize(Container c) {
		isPalette = (frame.getClientProperty("isPalette") == Boolean.TRUE);
		// width.
        int width = 22;
 
        if(frame.isClosable()) {
            width += 19;
        }
        if(frame.isMaximizable()) {
            width += 19;
        }
        if(frame.isIconifiable()) {
            width += 19;
        }

        FontMetrics fm = getFontMetrics(getFont());
        String frameTitle = frame.getTitle();
        int title_w = frameTitle != null ? fm.stringWidth(frameTitle) : 0;
        int title_length = frameTitle != null ? frameTitle.length() : 0;

        // Leave room for three characters in the title.
        if(title_length > 3) {
            int subtitle_w =
                fm.stringWidth(frameTitle.substring(0, 3) + "...");
            width += (title_w < subtitle_w) ? title_w : subtitle_w;
        } else {
            width += title_w;
        }

        // height
		int height = (isPalette ?
			Theme.framePaletteTitleHeight[Theme.derivedStyle[Theme.style]] :
			Theme.frameInternalTitleHeight[Theme.derivedStyle[Theme.style]]);

        Dimension dim = new Dimension(width, height);

        // Take into account the border insets if any.
        if(getBorder() != null) {
            Insets insets = getBorder().getBorderInsets(c);
            dim.height += insets.top + insets.bottom;
            dim.width += insets.left + insets.right;
        }
        
        return dim;
	}

	/**
	 * The minimum size of the frame.
	 * This is used, for example, during resizing to
	 * find the minimum allowable size.
	 * Providing at least some minimum size fixes a bug
	 * which breaks horizontal resizing.
	 * <b>Note</b>: the Motif plaf allows for a 0,0 min size,
	 * but we provide a reasonable minimum here.
	 * <b>Future</b>: calculate min size based upon contents.
	 */
	public Dimension getMinimumSize() {
		isPalette = (frame.getClientProperty("isPalette") == Boolean.TRUE);
		int height = (isPalette ?
			Theme.framePaletteTitleHeight[Theme.derivedStyle[Theme.style]] :
			Theme.frameInternalTitleHeight[Theme.derivedStyle[Theme.style]]);
			
		return new Dimension(TinyLookAndFeel.MINIMUM_INTERNAL_FRAME_WIDTH, height);
	}

	/**
	 * Calculates the minimum size dimensions for the specified
	 * panel given the components in the specified parent container.
	 */
	public Dimension minimumLayoutSize(Container c) {
		return preferredLayoutSize(c);
	}
	
	public void setPalette(boolean b) {
        isPalette = b;
	}
	
	public boolean isPalette() {
        return isPalette;
	}

	/**
	 * Lays out the container in the specified panel.
	 *
	 * @param c the component which needs to be laid out
	 */
	public void layoutContainer(Container c) {
		isPalette = (frame.getClientProperty("isPalette") == Boolean.TRUE);
		boolean leftToRight = frame.getComponentOrientation().isLeftToRight();
		int buttonHeight = closeButton.getPreferredSize().height;

		int w = getWidth();
		int x = leftToRight ? w : 0;
		int y = (getHeight() - buttonHeight) / 2 + 1;
		int spacing;

		if(Theme.derivedStyle[Theme.style] == Theme.W99_STYLE) {
			y += 1;
		}

		int buttonWidth = 0;
		
		if(isPalette) {
			buttonWidth = Theme.framePaletteButtonSize[Theme.derivedStyle[Theme.style]].width;
		}
		else {
			buttonWidth = Theme.frameInternalButtonSize[Theme.derivedStyle[Theme.style]].width;
		}

		if(frame.isClosable()) {
			spacing = 2;
			x += leftToRight ? -spacing - buttonWidth : spacing;
			closeButton.setBounds(x, y, buttonWidth, buttonHeight);
			if(!leftToRight) x += (buttonWidth);
		}

		if(frame.isMaximizable()) {
			spacing = 2;
			x += leftToRight ? -spacing - buttonWidth : spacing;
			maxButton.setBounds(x, y, buttonWidth, buttonHeight);
			if(!leftToRight)
				x += buttonWidth;
		}

		if(frame.isIconifiable()) {
			spacing = (frame.isMaximizable() && Theme.style == Theme.W99_STYLE) ? 0 : 2;
			x += leftToRight ? -spacing - buttonWidth : spacing;
			iconButton.setBounds(x, y, buttonWidth, buttonHeight);
			if(!leftToRight)
				x += buttonWidth;
		}

		buttonsWidth = leftToRight ? w - x : x;
	}

	public void activate() {
		closeButton.setEnabled(true);
		iconButton.setEnabled(true);
		maxButton.setEnabled(true);
	}

	public void deactivate() {
		closeButton.setEnabled(false);
		iconButton.setEnabled(false);
		maxButton.setEnabled(false);
	}

	/**
	* @see java.awt.Component#getFont()
	*/
	public Font getFont() {
		Font f = null;
		
		if(isPalette) {
			f = UIManager.getFont("InternalFrame.paletteTitleFont");
		}
		else {
			f = UIManager.getFont("InternalFrame.normalTitleFont");
		}
		
		if(f == null) {
			f = new Font("SansSerife", Font.BOLD, 12);
		}
		
		return f;
	}
	
	class TinyPropertyChangeHandler
        extends BasicInternalFrameTitlePane.PropertyChangeHandler
    {
        public void propertyChange(PropertyChangeEvent evt) {
	    	String prop = (String)evt.getPropertyName();
	    	
            if( prop.equals(JInternalFrame.IS_SELECTED_PROPERTY) ) {
                Boolean b = (Boolean)evt.getNewValue();
                
                iconButton.putClientProperty("paintActive", b);
                closeButton.putClientProperty("paintActive", b);
                maxButton.putClientProperty("paintActive", b);
            }
//            else if("JInternalFrame.messageType".equals(prop)) {
//                updateOptionPaneState();
//                frame.repaint();
//            }
            
            super.propertyChange(evt);
        }
    }
}
