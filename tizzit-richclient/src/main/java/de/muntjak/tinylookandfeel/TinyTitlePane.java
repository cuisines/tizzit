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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;

import de.muntjak.tinylookandfeel.controlpanel.ControlPanel;

/*
 * @(#)TinyTitlePane.java	1.10 01/12/03
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Class that manages a JLF awt.Window-descendant class's title bar.  
 * <p>
 * This class assumes it will be created with a particular window
 * decoration style, and that if the style changes, a new one will
 * be created.
 *
 * @version 1.10 12/03/01
 * @author Terry Kellerman
 * @since 1.4
 */

/**
 * TinyTitlePane
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class TinyTitlePane extends JComponent {
	
    private static final int IMAGE_HEIGHT = 16;
    private static final int IMAGE_WIDTH = 16;
    
    private static TinyWindowButtonUI iconButtonUI;
    private static TinyWindowButtonUI maxButtonUI;
    private static TinyWindowButtonUI closeButtonUI;

    /**
     * PropertyChangeListener added to the JRootPane.
     */
    private PropertyChangeListener propertyChangeListener;

    /**
     * JMenuBar, typically renders the system menu items.
     */
    private JMenuBar menuBar;
    /**
     * Action used to close the Window.
     */
    private Action closeAction;

    /**
     * Action used to iconify the Frame.
     */
    private Action iconifyAction;

    /**
     * Action to restore the Frame size.
     */
    private Action restoreAction;

    /**
     * Action to restore the Frame size.
     */
    private Action maximizeAction;

    /**
     * Button used to maximize or restore the Frame.
     */
    private JButton toggleButton;

    /**
     * Button used to maximize or restore the Frame.
     */
    private JButton iconifyButton;

    /**
     * Button used to maximize or restore the Frame.
     */
    private JButton closeButton;

    /**
     * Listens for changes in the state of the Window listener to update
     * the state of the widgets.
     */
    private WindowListener windowListener;
    
    private ComponentListener windowMoveListener;

    /**
     * Window we're currently in.
     */
    private Window window;

    /**
     * JRootPane rendering for.
     */
    private JRootPane rootPane;

    /**
     * Room remaining in title for bumps.
     */
    private int buttonsWidth;

    /**
     * Buffered Frame.state property. As state isn't bound, this is kept
     * to determine when to avoid updating widgets.
     */
    private int state;

    /**
     * RootPaneUI that created us.
     */
    private TinyRootPaneUI rootPaneUI;
    

    public TinyTitlePane(JRootPane root, TinyRootPaneUI ui) {
        rootPane = root;
        rootPaneUI = ui;

        state = -1;

        installSubcomponents();
        installDefaults();

        setLayout(createLayout());
    }

    /**
     * Uninstalls the necessary state.
     */
    private void uninstall() {
        uninstallListeners();
        window = null;
        removeAll();
    }

    /**
     * Installs the necessary listeners.
     */
    private void installListeners() {
        if(window != null) {
            windowListener = createWindowListener();
            window.addWindowListener(windowListener);
            propertyChangeListener = createWindowPropertyChangeListener();
            window.addPropertyChangeListener(propertyChangeListener);
            windowMoveListener = new WindowMoveListener();
            window.addComponentListener(windowMoveListener);
        }
    }

    /**
     * Uninstalls the necessary listeners.
     */
    private void uninstallListeners() {
        if(window != null) {
            window.removeWindowListener(windowListener);
            window.removePropertyChangeListener(propertyChangeListener);
            window.removeComponentListener(windowMoveListener);
        }
    }

    /**
     * Returns the <code>WindowListener</code> to add to the
     * <code>Window</code>.
     */
    private WindowListener createWindowListener() {
        return new WindowHandler();
    }

    /**
     * Returns the <code>PropertyChangeListener</code> to install on
     * the <code>Window</code>.
     */
    private PropertyChangeListener createWindowPropertyChangeListener() {
        return new PropertyChangeHandler();
    }

    /**
     * Returns the <code>JRootPane</code> this was created for.
     */
    public JRootPane getRootPane() {
        return rootPane;
    }

    /**
     * Returns the decoration style of the <code>JRootPane</code>.
     */
    private int getWindowDecorationStyle() {
        return getRootPane().getWindowDecorationStyle();
    }

    public void addNotify() {
        super.addNotify();

        uninstallListeners();

        window = SwingUtilities.getWindowAncestor(this);
        if(window != null) {
            if(window instanceof Frame) {
                setState(((Frame)window).getExtendedState());
            }
            else {
                setState(0);
            }
            setActive(window.isActive());
            installListeners();
        }
    }

    public void removeNotify() {
        super.removeNotify();

        uninstallListeners();
        window = null;
    }

    /**
     * Adds any sub-Components contained in the <code>TinyTitlePane</code>.
     */
    private void installSubcomponents() {
        if(getWindowDecorationStyle() == JRootPane.FRAME) {
            createActions();
            menuBar = createMenuBar();
            add(menuBar);
            createButtons();
            add(iconifyButton);
            add(toggleButton);
            add(closeButton);
            iconifyButton.putClientProperty("externalFrameButton", Boolean.TRUE);
            toggleButton.putClientProperty("externalFrameButton", Boolean.TRUE);
            closeButton.putClientProperty("externalFrameButton", Boolean.TRUE);
        }
        else if(getWindowDecorationStyle() != JRootPane.NONE) {
        	createActions();
        	createButtons();
        	add(closeButton);
        	closeButton.putClientProperty("externalFrameButton", Boolean.FALSE);
        }
    }

    /**
     * Installs the fonts and necessary properties on the TinyTitlePane.
     */
    private void installDefaults() {
        setFont(UIManager.getFont("InternalFrame.titleFont", getLocale()));
    }
    
    /**
     * Uninstalls any previously installed UI values.
     */
    private void uninstallDefaults() {
    }

    /**
     * Returns the <code>JMenuBar</code> displaying the appropriate 
     * system menu items.
     */
    protected JMenuBar createMenuBar() {
        menuBar = new SystemMenuBar();
        menuBar.setFocusable(false);
        menuBar.setBorderPainted(true);
        menuBar.add(createMenu());
        return menuBar;
    }

    /**
     * Closes the Window.
     */
    private void close() {
        Window window = getWindow();

        if(window != null) {
            window.dispatchEvent(new WindowEvent(
                                 window, WindowEvent.WINDOW_CLOSING));
        }
    }

    /**
     * Iconifies the Frame.
     */
    private void iconify() {
        Frame frame = getFrame();

        if(frame != null) {
            frame.setExtendedState(frame.getExtendedState() | Frame.ICONIFIED);
        }
    }

    /**
     * Maximizes the Frame.
     */
    private void maximize() {
        Frame frame = getFrame();

        if(frame != null) {
        	setMaximizeBounds(frame);
            frame.setExtendedState(frame.getExtendedState() | Frame.MAXIMIZED_BOTH);
        }
    }

    protected void setMaximizeBounds(Frame frame) {
    	if(frame.getMaximizedBounds() != null) return;
    	
    	Insets screenInsets =
    		Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	
    	// spare any Systemmenus or Taskbars or ??...
    	int x = screenInsets.top;
    	int y = screenInsets.left;
    	int w = screenSize.width - x - screenInsets.right;
    	int h = screenSize.height - y - screenInsets.bottom;
    	Rectangle maxBounds = new Rectangle(x, y, w, h);
    	frame.setMaximizedBounds(maxBounds);
    }

    /**
     * Restores the Frame size.
     */
    private void restore() {
        Frame frame = getFrame();

        if(frame == null) {
            return;
        }

        if((frame.getExtendedState() & Frame.ICONIFIED) == Frame.ICONIFIED) {
            frame.setExtendedState(state & ~Frame.ICONIFIED);
        } else {
            frame.setExtendedState(state & ~Frame.MAXIMIZED_BOTH);
        }
    }

    /**
     * Create the <code>Action</code>s that get associated with the
     * buttons and menu items.
     */
    private void createActions() {
        closeAction = new CloseAction();
        iconifyAction = new IconifyAction();
        restoreAction = new RestoreAction();
        maximizeAction = new MaximizeAction();
    }

    /**
     * Returns the <code>JMenu</code> displaying the appropriate menu items
     * for manipulating the Frame.
     */
    private JMenu createMenu() {
        JMenu menu = new JMenu("");
        if(getWindowDecorationStyle() == JRootPane.FRAME) {
            addMenuItems(menu);
            // we use this property to prevent the Menu from drawing rollovers
            menu.putClientProperty("isSystemMenu", Boolean.TRUE);
        }
        
        return menu;
    }

    /**
     * Adds the necessary <code>JMenuItem</code>s to the passed in menu.
     */
    private void addMenuItems(JMenu menu) {
        Locale locale = getRootPane().getLocale();
        JMenuItem mi = menu.add(restoreAction);
        int mnemonic = getInt("MetalTitlePane.restoreMnemonic", -1);

        if(mnemonic != -1) {
            mi.setMnemonic(mnemonic);
        }

        mi = menu.add(iconifyAction);
        mnemonic = getInt("MetalTitlePane.iconifyMnemonic", -1);
        if(mnemonic != -1) {
            mi.setMnemonic(mnemonic);
        }

        if(Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_BOTH)) {
            mi = menu.add(maximizeAction);
            mnemonic =
                getInt("MetalTitlePane.maximizeMnemonic", -1);
            if(mnemonic != -1) {
                mi.setMnemonic(mnemonic);
            }
        }

        menu.addSeparator();

        mi = menu.add(closeAction);
        mnemonic = getInt("MetalTitlePane.closeMnemonic", -1);
        if(mnemonic != -1) {
            mi.setMnemonic(mnemonic);
        }
    }

  /**
   * Creates the buttons of the title pane and initializes their actions.
   */
  protected void createButtons() {
    if(iconButtonUI == null) {
        iconButtonUI = TinyWindowButtonUI.createButtonUIForType(TinyWindowButtonUI.MINIMIZE);
        maxButtonUI = TinyWindowButtonUI.createButtonUIForType(TinyWindowButtonUI.MAXIMIZE);
        closeButtonUI = TinyWindowButtonUI.createButtonUIForType(TinyWindowButtonUI.CLOSE);
    }
    
    iconifyButton = new SpecialUIButton(iconButtonUI);
    iconifyButton.setAction(iconifyAction);
    iconifyButton.setRolloverEnabled(true);

    toggleButton = new SpecialUIButton(maxButtonUI);
    toggleButton.setAction(maximizeAction);
    toggleButton.setRolloverEnabled(true);
    

    closeButton = new SpecialUIButton(closeButtonUI);
    closeButton.setAction(closeAction);
    closeButton.setRolloverEnabled(true);

    closeButton.getAccessibleContext().setAccessibleName("Close");
    iconifyButton.getAccessibleContext().setAccessibleName("Iconify");
    toggleButton.getAccessibleContext().setAccessibleName("Maximize");
    
    if(TinyLookAndFeel.controlPanelInstantiated) {
    	ControlPanel.setWindowButtons(new JButton[] {iconifyButton, toggleButton, closeButton});
    }
  }

    /**
     * Returns the <code>LayoutManager</code> that should be installed on
     * the <code>TinyTitlePane</code>.
     */
    private LayoutManager createLayout() {
        return new TitlePaneLayout();
    }

    /**
     * Updates state dependant upon the Window's active state.
     */
    private void setActive(boolean isActive) {
        if(getWindowDecorationStyle() == JRootPane.FRAME) {
            Boolean activeB = isActive ? Boolean.TRUE : Boolean.FALSE;

            iconifyButton.putClientProperty("paintActive", activeB);
            closeButton.putClientProperty("paintActive", activeB);
            toggleButton.putClientProperty("paintActive", activeB);
            
            iconifyButton.setEnabled(isActive);
            closeButton.setEnabled(isActive);
            toggleButton.setEnabled(isActive);
        }
        // Repaint the whole thing as the Borders that are used have
        // different colors for active vs inactive
        getRootPane().repaint();
    }

    /**
     * Sets the state of the Window.
     */
    private void setState(int state) {
        setState(state, false);
    }

    /**
     * Sets the state of the window. If <code>updateRegardless</code> is
     * true and the state has not changed, this will update anyway.
     */
    private void setState(int state, boolean updateRegardless) {
        Window w = getWindow();

        if(w != null && getWindowDecorationStyle() == JRootPane.FRAME) {
            if(this.state == state && !updateRegardless) {
                return;
            }
            Frame frame = getFrame();

            if(frame != null) {
                JRootPane rootPane = getRootPane();

                if((state & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH &&
                              (rootPane.getBorder() == null ||
                              (rootPane.getBorder() instanceof UIResource)) &&
                              frame.isShowing()) {
                    //rootPane.setBorder(null);
                }
                else if((state & Frame.MAXIMIZED_BOTH) !=
                        Frame.MAXIMIZED_BOTH) {
                    // This is a croak, if state becomes bound, this can
                    // be nuked.
                    //rootPaneUI.installBorder(rootPane);
                }
                if(frame.isResizable()) {
                    if((state & Frame.MAXIMIZED_VERT) == Frame.MAXIMIZED_VERT ||
                            (state & Frame.MAXIMIZED_HORIZ) == Frame.MAXIMIZED_HORIZ)
                    {
                        updateToggleButton(restoreAction);
                        maximizeAction.setEnabled(false);
                        restoreAction.setEnabled(true);
                    }
                    else {
                        updateToggleButton(maximizeAction);
                        maximizeAction.setEnabled(true);
                        restoreAction.setEnabled(false);
                    }
                    if(toggleButton.getParent() == null ||
                        iconifyButton.getParent() == null) {
                        add(toggleButton);
                        add(iconifyButton);
                        revalidate();
                        repaint();
                    }
                    toggleButton.setText(null);
                }
                else {
                    maximizeAction.setEnabled(false);
                    restoreAction.setEnabled(false);
                    if(toggleButton.getParent() != null) {
                        remove(toggleButton);
                        revalidate();
                        repaint();
                    }
                }
            }
            else {
                // Not contained in a Frame
                maximizeAction.setEnabled(false);
                restoreAction.setEnabled(false);
                iconifyAction.setEnabled(false);
                remove(toggleButton);
                remove(iconifyButton);
                revalidate();
                repaint();
            }
            
            closeAction.setEnabled(true);
            this.state = state;
        }
    }

    /**
     * Updates the toggle button to contain the Icon <code>icon</code>, and
     * Action <code>action</code>.
     */
    private void updateToggleButton(Action action) {
        toggleButton.setAction(action);
        toggleButton.setText(null);
    }

    /**
     * Returns the Frame rendering in. This will return null if the
     * <code>JRootPane</code> is not contained in a <code>Frame</code>.
     */
    private Frame getFrame() {
        Window window = getWindow();

        if(window instanceof Frame) {        	
            return (Frame)window;
        }
        
        return null;
    }

    /**
     * Returns the <code>Window</code> the <code>JRootPane</code> is
     * contained in. This will return null if there is no parent ancestor
     * of the <code>JRootPane</code>.
     */
    private Window getWindow() {
        return window;
    }

    /**
     * Returns the String to display as the title.
     */
    private String getTitle() {
        Window w = getWindow();

        if(w instanceof Frame) {
            return ((Frame)w).getTitle();
        }
        else if(w instanceof Dialog) {
            return ((Dialog)w).getTitle();
        }
        
        return null;
    }
    
    public boolean isSelected() {
    	Window window = getWindow();
    	return (window == null) ? true : window.isActive();
    }
    
    public boolean isFrameMaximized() {
    	Frame frame = getFrame();
    	
        if(frame != null) {
            return ((frame.getExtendedState() &
            	Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH);
        }
        
    	return false;
    }

    /**
     * Renders the TitlePane ( => paints the title).
     */
    public void paintComponent(Graphics g)  {
        // As state isn't bound, we need a convenience place to check
        // if it has changed.
        if(getFrame() != null) {
            setState(getFrame().getExtendedState());
        }

        Window window = getWindow();
        boolean leftToRight = (window == null) ?
                               getRootPane().getComponentOrientation().isLeftToRight() :
                               window.getComponentOrientation().isLeftToRight();
        boolean isSelected = (window == null) ? true : window.isActive();
        int width = getWidth();
        int height = getHeight();
        int xOffset = leftToRight ? 5 : width - 5;

        if(getWindowDecorationStyle() == JRootPane.FRAME) {
            xOffset += leftToRight ? IMAGE_WIDTH + 5 : - IMAGE_WIDTH - 5;
        }
        
        String theTitle = getTitle();
        
        if(theTitle != null) {
            Font f = UIManager.getFont("InternalFrame.normalTitleFont");
            FontMetrics fm = g.getFontMetrics();
            int yOffset = ( (height - fm.getHeight() ) / 2 ) + fm.getAscent();
            Rectangle rect = new Rectangle(0, 0, 0, 0);
            
            if(iconifyButton != null && iconifyButton.getParent() != null) {
                rect = iconifyButton.getBounds();
            }
            int titleW;

            if( leftToRight ) {
                if(rect.x == 0) {
                    rect.x = window.getWidth() - window.getInsets().right-2;
                }
                
                titleW = rect.x - xOffset - 4;
                theTitle = clippedText(theTitle, fm, titleW);
            } else {
                titleW = xOffset - rect.x - rect.width - 4;
                theTitle = clippedText(theTitle, fm, titleW);
                xOffset -= SwingUtilities.computeStringWidth(fm, theTitle);
            }
            
            int titleLength = SwingUtilities.computeStringWidth(fm, theTitle);
            
            if(isSelected) {
	      		g.setColor(Theme.frameTitleColor[Theme.style].getColor());
	      		g.drawString(theTitle, xOffset, yOffset);
            }
            else {
            	// for an inactive window
				g.setColor(Theme.frameTitleDisabledColor[Theme.style].getColor());
				g.drawString(theTitle, xOffset, yOffset);
            }
            
            xOffset += leftToRight ? titleLength + 5  : -5;
        }
    }

    /**
     * Convenience method to clip the passed in text to the specified
     * size.
     */
    private String clippedText(String text, FontMetrics fm,
                                 int availTextWidth) {
        if((text == null) || (text.equals("")))  {
            return "";
        }
        int textWidth = SwingUtilities.computeStringWidth(fm, text);
        String clipString = "...";
        if(textWidth > availTextWidth) {
            int totalWidth = SwingUtilities.computeStringWidth(fm, clipString);
            int nChars;
            for(nChars = 0; nChars < text.length(); nChars++) {
                totalWidth += fm.charWidth(text.charAt(nChars));
                if(totalWidth > availTextWidth) {
                    break;
                }
            }
            text = text.substring(0, nChars) + clipString;
        }
        return text;
    }
    
    private int getInt(Object key, int defaultValue) {
        Object value = UIManager.get(key);

        if(value instanceof Integer) {
            return ((Integer)value).intValue();
        }
        if(value instanceof String) {
            try {
                return Integer.parseInt((String)value);
            } catch (NumberFormatException nfe) {}
        }
        return defaultValue;
    }



    /**
     * Actions used to <code>close</code> the <code>Window</code>.
     */
    private class CloseAction extends AbstractAction {
        public CloseAction() {
            super(UIManager.getString("MetalTitlePane.closeTitle",
                                      getLocale()));
        }

        public void actionPerformed(ActionEvent e) {
            close();
        }      
    }


    /**
     * Actions used to <code>iconfiy</code> the <code>Frame</code>.
     */
    private class IconifyAction extends AbstractAction {
        public IconifyAction() {
            super(UIManager.getString("MetalTitlePane.iconifyTitle",
                                      getLocale()));
        }

        public void actionPerformed(ActionEvent e) {
            iconify();
        }
    } 


    /**
     * Actions used to <code>restore</code> the <code>Frame</code>.
     */
    private class RestoreAction extends AbstractAction {
        public RestoreAction() {
            super(UIManager.getString
                  ("MetalTitlePane.restoreTitle", getLocale()));
        }

        public void actionPerformed(ActionEvent e) {
            restore();
        }
    }


    /**
     * Actions used to <code>restore</code> the <code>Frame</code>.
     */
    private class MaximizeAction extends AbstractAction {
        public MaximizeAction() {
            super(UIManager.getString(
				"MetalTitlePane.maximizeTitle", getLocale()));
        }

        public void actionPerformed(ActionEvent e) {
            maximize();
        }
    }


    /**
     * Class responsible for drawing the system menu. Looks up the
     * image to draw from the Frame associated with the
     * <code>JRootPane</code>.
     */
    private class SystemMenuBar extends JMenuBar {
    	
        public void paint(Graphics g) {
            Frame frame = getFrame();
            Image image = (frame != null) ? frame.getIconImage() : null;

            if(image != null) {
                g.drawImage(image, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, null);
            } else {
                Icon icon = UIManager.getIcon("InternalFrame.icon");

                if(icon != null) {
      				icon.paintIcon(this, g, 0, 0);
                }
            }
        }
 
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }
        
        public Dimension getPreferredSize() {
        	Icon icon = UIManager.getIcon("InternalFrame.icon");

            if(icon != null) {
            	return new Dimension(icon.getIconWidth(), icon.getIconHeight());
            }
                	
            Dimension size = super.getPreferredSize();

            return new Dimension(Math.max(IMAGE_WIDTH, size.width),
                                 Math.max(size.height, IMAGE_HEIGHT));
        }
    }

    /**
     * This inner class is marked &quot;public&quot; due to a compiler bug.
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of <Foo>.
     */
    private class TitlePaneLayout implements LayoutManager {
    	
        public void addLayoutComponent(String name, Component c) {}
        public void removeLayoutComponent(Component c) {}
        
        public Dimension preferredLayoutSize(Container c)  {
            return new Dimension(TinyLookAndFeel.MINIMUM_FRAME_WIDTH, computeHeight());
        }
        
        public Dimension minimumLayoutSize(Container c) {
            return preferredLayoutSize(c);
        } 
    
        private int computeHeight() {
        	if(getFrame() instanceof JFrame) {
        		setMaximizeBounds(getFrame());
        		
            	return Theme.frameTitleHeight[Theme.derivedStyle[Theme.style]];
        	}
        	else {
        		return Theme.frameInternalTitleHeight[Theme.derivedStyle[Theme.style]];
        	}
        }    
                    
        public void layoutContainer(Container c) {
            if(getWindowDecorationStyle() == JRootPane.NONE) {
                buttonsWidth = 0;
                return;
            }
            
            boolean leftToRight = (window == null) ?
                               getRootPane().getComponentOrientation().isLeftToRight() :
                               window.getComponentOrientation().isLeftToRight();

            int w = getWidth();
            int x; 
            int spacing;
            int buttonHeight; 
            int buttonWidth;
            
            if(closeButton != null) {
                buttonHeight = closeButton.getPreferredSize().height;
                buttonWidth = closeButton.getPreferredSize().width;
            }
            else {
                buttonHeight = IMAGE_HEIGHT;
                buttonWidth = IMAGE_WIDTH;
            }
            
            int y = (getHeight() - buttonHeight) / 2 + 1;
    
    		if(Theme.derivedStyle[Theme.style] == Theme.W99_STYLE) {
        		y += 1;
     		}

            // assumes all buttons have the same dimensions,
            // these dimensions include the borders

            x = leftToRight ? w : 0;

            spacing = 5;
            x = leftToRight ? spacing : w - buttonWidth - spacing;
            if(menuBar != null) {
            	// this is a JFrame
            	menuBar.setBounds(x, y, buttonWidth, buttonHeight);
            }

            x = leftToRight ? w : 0;
            spacing = 2;
            x += leftToRight ? -spacing -buttonWidth : spacing;
            if(closeButton != null) {
                closeButton.setBounds(x, y, buttonWidth, buttonHeight);
            }

            if( !leftToRight ) x += buttonWidth;

            if(Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_BOTH)) {
                if(toggleButton.getParent() != null) {
                    x += leftToRight ? -spacing -buttonWidth : spacing;
                    toggleButton.setBounds(x, y, buttonWidth, buttonHeight);
                    if(!leftToRight) {
                        x += buttonWidth;
                    }
                }
            }

            if(iconifyButton != null && iconifyButton.getParent() != null) {
                x += leftToRight ? -spacing -buttonWidth : spacing;
                iconifyButton.setBounds(x, y, buttonWidth, buttonHeight);
                if(!leftToRight) {
                    x += buttonWidth;
                }
            }
            
            buttonsWidth = leftToRight ? w - x : x;
        }
    }



    /**
     * PropertyChangeListener installed on the Window. Updates the necessary
     * state as the state of the Window changes.
     */
    private class PropertyChangeHandler implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent pce) {
            String name = pce.getPropertyName();

            // Frame.state isn't currently bound.
            if("resizable".equals(name) || "state".equals(name)) {
                Frame frame = getFrame();

                if(frame != null) {
                    setState(frame.getExtendedState(), true);
                }
                
                if("resizable".equals(name)) {
                    getRootPane().repaint();
                }
            }
            else if("title".equals(name)) {
                repaint();
            }
            else if("componentOrientation".equals(name)) {
                revalidate();
                repaint();
            }
        }
    }


    /**
     * WindowListener installed on the Window, updates the state as necessary.
     */
    private class WindowHandler extends WindowAdapter {
        public void windowActivated(WindowEvent ev) {
            setActive(true);
        }

        public void windowDeactivated(WindowEvent ev) {
            setActive(false);
        }
    }
    
    class WindowMoveListener extends ComponentAdapter {

		public void componentMoved(ComponentEvent e) {
			if(getWindowDecorationStyle() == JRootPane.NONE) return;
			
			// paint the non-opaque upper edges
			Window w = getWindow();
			
			if(!w.isShowing()) return;

			w.repaint(0, 0, w.getWidth(), 5);
		}

		public void componentResized(ComponentEvent e) {
			if(getWindowDecorationStyle() == JRootPane.NONE) return;
			
			// paint the non-opaque upper edges
			Window w = getWindow();
			
			if(!w.isShowing()) return;
			
			w.repaint(0, 0, w.getWidth(), 5);
		}
    }
}  
