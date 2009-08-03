/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
*	Tiny Look and Feel                                                         *
*                                                                              *
*  (C) Copyright 2003 - 2007 Hans Bickel                                       *
*                                                                              *
*   For licensing information and credits, please refer to the                 *
*   comment in file de.muntjak.tinylookandfeel.TinyLookAndFeel                 *
*                                                                              *
* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package de.muntjak.tinylookandfeel.controlpanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.MenuElement;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.TableHeaderUI;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Position;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import de.muntjak.tinylookandfeel.Theme;
import de.muntjak.tinylookandfeel.TinyDefaultTheme;
import de.muntjak.tinylookandfeel.TinyLookAndFeel;
import de.muntjak.tinylookandfeel.TinyTableHeaderUI;
import de.muntjak.tinylookandfeel.TinyTitlePane;
import de.muntjak.tinylookandfeel.table.SortableTableData;

/**
 * ControlPanel
 * 
 * 28.4.06 Added additional font sizes.
 * 
 * @version 1.1
 * @author Hans Bickel
 */
public class ControlPanel {

	private JFrame theFrame;
	private static final String WINDOW_TITLE = "TinyLaF " +
		TinyLookAndFeel.VERSION_STRING + " Controlpanel";
	
	private static final int PLAIN_FONT = 	1;
	private static final int BOLD_FONT = 	2;
	private static final int SPECIAL_FONT =	3;
	
	// control modes for SpreadPanel
	static final int CONTROLS_BUTTON 					= 1;
	static final int CONTROLS_WINDOW_BUTTON 			= 2;
	static final int CONTROLS_COMBO 					= 3;
	static final int CONTROLS_SCROLLBAR 				= 4;
	static final int CONTROLS_SPINNER 					= 5;
	static final int CONTROLS_ACTIVE_FRAME_CAPTION 		= 6;
	static final int CONTROLS_INACTIVE_FRAME_CAPTION 	= 7;
	
	private static String directoryPath = System.getProperty("user.dir");
	private static final Border sbFieldBorder = new LineBorder(Color.DARK_GRAY, 1);
	private static final Color infoColor = new Color(208, 239, 255);
    private static final Border infoBorder = BorderFactory.createCompoundBorder(
		BorderFactory.createLineBorder(new Color(108, 108, 147)),
		BorderFactory.createEmptyBorder(3, 3, 3, 3));
    
    private static final int menuShortcutKeyMask =
		Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    
    // different targets to repaint
    private static Component[] windowButtons;
    private Component[] buttons, combos, frames, scrollBars, spinners;
    
	private Icon icon99;
	private Image image99;
	
	private String currentFileName;
	private ActionListener selectThemeAction = new SelectThemeAction();
	private ActionListener checkAction = new CheckAction();
	private ChangeListener updateAction = new UpdateAction();
	private ChangeListener spinnerUpdateAction = new SpinnerUpdateAction();
	
	private boolean resistUpdate = false;
	
	private JPanel widgetInfo;
	private ExamplePanel.DesktopPane desktopPane;
	private JTree tree1, tree2;
	private JScrollPane sp1, sp2;
	private PopupTrigger trigger;
	private JToolBar theToolBar;
	private JMenu themesMenu;
	private JTabbedPane mainTab, compTab;
	private JButton updateThemeButton;
	private JCheckBoxMenuItem customStyle;
	private FontPanel plainFontPanel, boldFontPanel, specialFontPanel;
	private JComboBox fontCombo;
	private JRadioButton isPlainFont, isBoldFont;
	private ColoredFont[] selectedFont;
	private ExamplePanel examplePanel;
	private JButton exampleButton, exampleDisabledButton;
	private JToggleButton exampleToggleButton;
	private Icon buttonIcon;
	private JPopupMenu thePopup;
	private JInternalFrame internalFrame, palette;
	private JPopupMenu hsbPopup, sbPopup;
	private ButtonsCP buttonsCP;
	private ScrollBarCP scrollsCP;
	private SeparatorCP separatorCP;
	private TabbedPaneCP tabsCP;
	private ComboCP comboCP;
	private MenuCP menuCP;
	private ListCP listCP;
	private SliderCP sliderCP;
	private SpinnerCP spinnerCP;
	private ProgressCP progressCP;
	private TextCP textCP;
	private TreeCP treeCP;
	private ToolBarCP toolCP;
	private TableCP tableCP;
	private JTable exampleTable;
	private FrameCP frameCP;
	private static JCheckBox decoratedFramesCheck;
	private IconCP iconCP;
	private ToolTipCP tipCP;
	private MiscCP miscCP;
	private JSlider vertSlider, horzSlider;	
	private SBField selectedSBField;
	private SBField mainField, rollField, backField, frameField,
		sub1Field, sub2Field, sub3Field, sub4Field,
		sub5Field, sub6Field, sub7Field, sub8Field;
	private HSBField selectedHSBField;
	
// buttonCP
	private SBField buttonNormalBg, buttonRolloverBg, buttonPressedBg, buttonDisabledBg;
	private SBField buttonBorder, buttonDark, buttonLight;
	private SBField buttonRollover, buttonDefault, buttonCheck, buttonCheckDisabled;
	private SBField buttonDisabledBorder, buttonDisabledDark, buttonDisabledLight;
    private SBField buttonDisabledFg, checkDisabledFg, radioDisabledFg;
	
	SpreadControl buttonSpreadLight, buttonSpreadLightDisabled;
	SpreadControl buttonSpreadDark, buttonSpreadDarkDisabled;

// textCP
	private SBField textBg, textSelectedBg, textDisabledBg;
    private SBField textBorder, textBorderDisabled, textCaret;
    private SBField textDark, textDisabledDark, textLight, textDisabledLight;
    private SBField textText, textSelectedText;
    
// comboCP
	private SBField comboBg, comboText;
    private SBField comboBorder, comboBorderDisabled, comboSelectedBg;
    private SBField comboDark, comboDisabledDark, comboLight, comboDisabledLight;
    private SBField comboArrowField, comboArrowDisabled;
    private SBField comboButt, comboButtRollover, comboButtPressed, comboButtDisabled;
    private SBField comboButtBorder, comboButtDark, comboButtLight;
    private SBField comboButtBorderDisabled, comboButtDarkDisabled, comboButtLightDisabled;
    private SBField comboSelectedText;
    
    SpreadControl comboSpreadLight, comboSpreadLightDisabled;
    SpreadControl comboSpreadDark, comboSpreadDarkDisabled;

// menuCP
	private SBField menuRolloverBg, menuSepDark, menuSepLight;
	private SBField menuRolloverFg, menuDisabledFg;
	private SBField menuBar, menuItemRollover, menuPopup;
	private SBField menuBorder, menuDark, menuLight;
	private SBField menuInnerHilight, menuInnerShadow, menuOuterHilight, menuOuterShadow;
	private SBField menuIcon, menuIconRollover, menuIconDisabled, menuIconShadow;	
	private SBField menuSelectedText;
	
// listCP
	private SBField listBg, listText;
	private SBField listSelectedBg, listSelectedText;
		
// tabsCP
	private SBField tabNormalBg, tabSelectedBg, tabRoll;
    private SBField tabDisabled, tabDisabledSelected, tabDisabledText;
	private SBField tabBorder, tabDark, tabLight;
	private SBField tabPaneBorder, tabPaneDark, tabPaneLight;
	
// scrollsCP
	private SBField scrollThumbField, scrollButtField, scrollArrowField, trackField,
		scrollThumbRolloverBg, scrollThumbPressedBg, scrollThumbDisabledBg,
		scrollButtRolloverBg, scrollButtPressedBg, scrollButtDisabledBg,
		trackDisabled, trackBorder, trackBorderDisabled, scrollArrowDisabled,
		scrollGripDark, scrollGripLight, scrollPane,
		scrollBorder, scrollDark, scrollLight,
		scrollBorderDisabled, scrollDarkDisabled, scrollLightDisabled;
		
	SpreadControl scrollSpreadLight, scrollSpreadLightDisabled;
	SpreadControl scrollSpreadDark, scrollSpreadDarkDisabled;
    
// sliderCP
    private SBField sliderThumbRolloverBg, sliderThumbPressedBg, sliderThumbDisabledBg;
    private SBField sliderBorder, sliderDark, sliderLight, sliderThumbField;
	private SBField sliderDisabledBorder, sliderDisabledDark, sliderDisabledLight;
	private SBField sliderTrack, sliderTrackBorder, sliderTrackDark, sliderTrackLight;
	private SBField sliderTick, sliderTickDisabled, sliderFocusColor;
    
// spinnerCP
	private SBField spinnerButtField, spinnerArrowField;
    private SBField spinnerButtRolloverBg, spinnerButtPressedBg, spinnerButtDisabledBg;
    private SBField spinnerBorder, spinnerDark, spinnerLight, spinnerArrowDisabled;
	private SBField spinnerDisabledBorder, spinnerDisabledDark, spinnerDisabledLight;
	
	SpreadControl spinnerSpreadLight, spinnerSpreadLightDisabled;
	SpreadControl spinnerSpreadDark, spinnerSpreadDarkDisabled;
    
// progressCP
    private javax.swing.Timer progressTimer;
	private JProgressBar horzProgressBar, vertProgressBar;
	private SBField progressField, progressTrack;
	private SBField progressBorder, progressDark, progressLight;
	private SBField progressSelectFore, progressSelectBack;
    
// treeCP
    private SBField treeBg, treeTextBg, treeSelectedBg, treeText;
    private SBField treeSelectedText, treeLine;
    
// toolCP
	private SBField toolBar, toolBarDark, toolBarLight;
	private SBField toolButt, toolButtRollover,
		toolButtPressed, toolButtSelected;
	private SBField toolBorder, toolBorderPressed,
		toolBorderRollover, toolBorderSelected;
	private SBField toolBorderDark, toolBorderLight;
	private SBField toolGripDark, toolGripLight;
	private SBField toolSepDark, toolSepLight;
	
// frameCP
    private SBField frameCaption, frameCaptionDisabled;
    private SBField frameBorder, frameDark, frameLight;
    private SBField frameBorderDisabled, frameDarkDisabled, frameLightDisabled;
    private SBField frameTitle, frameTitleDisabled;
    private SBField frameButt, frameButtRollover, frameButtPressed, frameButtDisabled;
    SpreadControl frameButtSpreadLight, frameButtSpreadLightDisabled;
	SpreadControl frameButtSpreadDark, frameButtSpreadDarkDisabled;
    private SBField frameButtClose, frameButtCloseRollover, frameButtClosePressed, frameButtCloseDisabled;
    SpreadControl frameButtCloseSpreadLight, frameButtCloseSpreadLightDisabled;
	SpreadControl frameButtCloseSpreadDark, frameButtCloseSpreadDarkDisabled;
    private SBField frameButtBorder, frameButtDark, frameButtLight;
    private SBField frameButtBorderDisabled, frameButtDarkDisabled, frameButtLightDisabled;
    private SBField frameButtCloseBorder, frameButtCloseDark, frameButtCloseLight;
    private SBField frameButtCloseBorderDisabled, frameButtCloseDarkDisabled, frameButtCloseLightDisabled;
    private SBField frameSymbol, frameSymbolPressed, frameSymbolDisabled;
    private SBField frameSymbolDark, frameSymbolLight;
    private SBField frameSymbolClose, frameSymbolClosePressed, frameSymbolCloseDisabled;
    private SBField frameSymbolCloseDark, frameSymbolCloseLight;
    
    SpreadControl frameSpreadDark, frameSpreadLight, frameSpreadDarkDisabled, frameSpreadLightDisabled;
    
// iconCP
	private CheckedIcon[] iconChecks = new CheckedIcon[20];
    private HSBField[] hsb = new HSBField[20];
    
// tableCP
	private SBField tableBack, tableGrid;
	private SBField tableHeaderBack, tableHeaderRolloverBack,
		tableHeaderRollover, tableHeaderArrow;
	private SBField tableSelectedBack, tableSelectedFore;
	private SBField tableBorderDark, tableBorderLight;
	private SBField tableHeaderDark, tableHeaderLight;
	
// separatorCP
	private SBField sepDark, sepLight;
	
// tipCP
	private SBField tipBg, tipBorder, tipBgDis, tipBorderDis,
		tipText, tipTextDis;
	
// miscCP
	private SBField titledBorderColor, textPaneBg, editorPaneBg, desktopPaneBg;

	public ControlPanel() {
		icon99 = TinyLookAndFeel.loadIcon("icon99.gif", null);
		image99 = ((ImageIcon)icon99).getImage();
		
		createFrame();

		//showUIVariables("FileChooser");
		//showUIValues("167");
		//showSystemProperties();
	}

	private void createFrame() {
		if(decoratedFramesCheck != null && decoratedFramesCheck.isSelected()) {
			Toolkit.getDefaultToolkit().setDynamicLayout(true);
			System.setProperty("sun.awt.noerasebackground", "true");
			JFrame.setDefaultLookAndFeelDecorated(true);
		}
		else {
			Toolkit.getDefaultToolkit().setDynamicLayout(false);
			System.setProperty("sun.awt.noerasebackground", "false");
			JFrame.setDefaultLookAndFeelDecorated(false);
		}

		JDialog.setDefaultLookAndFeelDecorated(true);

		theFrame = new JFrame(WINDOW_TITLE);
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		boolean isCustomEnabled = false, isCustomSelected = false;
		
		if(customStyle != null) {
			isCustomEnabled = customStyle.isEnabled();
			isCustomSelected = customStyle.isSelected();
		}
		
		setupUI();
		
		customStyle.setEnabled(isCustomEnabled);
		customStyle.setSelected(isCustomSelected);
		createHSBPopup();
		createSBPopup();
		initColors();
		initPanels();
		updateThemeButton.setEnabled(false);
		startProgressTimer();
		
		try {
			internalFrame.setSelected(true);
		}
		catch (PropertyVetoException ignore) {}
	}
	
	private void startProgressTimer() {
		if(progressTimer == null) {
			progressTimer = new javax.swing.Timer(500, new ProgressAction());
		}

		vertProgressBar.setIndeterminate(true);
		progressTimer.start();
	}
	
	private void stopProgressTimer() {
		if(progressTimer == null) return;

		progressTimer.stop();
		horzProgressBar.setIndeterminate(false);
		vertProgressBar.setIndeterminate(false);
	}
	
	private void showUIVariables() {
    	UIDefaults defaults = UIManager.getDefaults();

    	String key;
    	int c = 0;
    	TreeMap map = new TreeMap();
    
    	Enumeration e = defaults.keys();
    	while(e.hasMoreElements()) {
      		key = e.nextElement().toString();
      		map.put(key, defaults.get(key));
    	}
    
    	Iterator ii = map.keySet().iterator();
    	while(ii.hasNext()) {
      		key = ii.next().toString();
        	System.out.print("#" + (c++) + " : " + key);
        	System.out.println(" = " + map.get(key));
    	}
    	
    	System.out.println();
	}
	
	void showUIVariables(String inString) {
    	UIDefaults defaults = UIManager.getDefaults();

    	String key;
    	int c = 0;
    	TreeMap map = new TreeMap();
    
    	Enumeration e = defaults.keys();
    	while(e.hasMoreElements()) {
      		key = e.nextElement().toString();
      		if(inString == null || key.indexOf(inString) != -1) {
      			map.put(key, defaults.get(key));
      		}
    	}
    
    	Object val;
    	
    	Iterator ii = map.keySet().iterator();
    	while(ii.hasNext()) {
      		key = ii.next().toString();
      		val = map.get(key);
      		
        	System.out.print("#" + (c++) + " : " + key);
        	System.out.println(" = " + map.get(key));
    	}
	}
	
	private void showUIValues(String val) {
    	UIDefaults defaults = UIManager.getDefaults();

    	String key;
    	int c = 0;
    	TreeMap map = new TreeMap();
    
    	Enumeration e = defaults.keys();
    	while(e.hasMoreElements()) {
      		key = e.nextElement().toString();
      		map.put(key, defaults.get(key));
    	}
    
    	Object value;
    	Iterator ii = map.keySet().iterator();
    	while(ii.hasNext()) {
      		key = ii.next().toString();
      		value = map.get(key);
      		if(value != null && value.toString().indexOf(val) != -1) {
        		System.out.print("#" + (c++) + " : " + key);
        		System.out.println(" = " + value);
      		}
    	}
    	
    	System.out.println();
	}
	
	private void showInsets() {
    	UIDefaults defaults = UIManager.getDefaults();

		Object val;
    	String key;
    	int c = 0;
    	TreeMap map = new TreeMap();
    
    	Enumeration e = defaults.keys();
    	while(e.hasMoreElements()) {
      		key = e.nextElement().toString();
      		val = defaults.get(key);
      		if(val instanceof Insets) {
      			map.put(key, val);
      		}
    	}

    	Iterator ii = map.keySet().iterator();
    	while(ii.hasNext()) {
      		key = ii.next().toString();
        	System.out.print("#" + (c++) + " : " + key);
        	System.out.println(" = " + map.get(key));
    	}
    	
    	System.out.println();
	}
		
	private void showSystemProperties() {
		Object key, value;
		Enumeration e = System.getProperties().keys();
		while(e.hasMoreElements()) {
			key = e.nextElement();
			value = System.getProperty(key.toString());
			System.out.println(key.toString() + " : " + value.toString());
		}
	}
	
	private void showMessageDialog() {
		JOptionPane.showMessageDialog(theFrame, "No messages today.");
	}
	
	private void showConfirmationDialog() {
		JOptionPane.showConfirmDialog(theFrame, "Do you really have a choice?");
	}
	
	private void showWarningDialog() {
		JOptionPane.showMessageDialog(theFrame,
			"You have been warned!", "Warning", JOptionPane.WARNING_MESSAGE);
	}
	
	private void showErrorDialog() {
		JOptionPane.showMessageDialog(theFrame,
			"Unknown software error. Panic!", "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	private void showInternalWarningDialog(String msg) {
		JOptionPane.showMessageDialog(theFrame,
			msg, "Warning", JOptionPane.WARNING_MESSAGE);
	}
	
	private void setupUI() {
		// menu bar
		JMenuBar menuBar = new JMenuBar();
    	menuBar.add(createFileMenu());
    	menuBar.add(createThemeMenu());
    	menuBar.add(createStyleMenu());
    	menuBar.add(createDisabledMenu());
    	menuBar.add(createTestMenu());
    	menuBar.add(createHelpMenu());

    	theFrame.setJMenuBar(menuBar);
    	
    	JPanel p0 = new JPanel(new BorderLayout());
    	JPanel p1 = new JPanel(new BorderLayout());
    	
    	p1.add(createToolBar(), BorderLayout.NORTH);
    	
    	// Colors/Fonts/Dekoration
    	mainTab = new JTabbedPane(JTabbedPane.LEFT);
    	mainTab.add("Colors", createColorPanel());
    	mainTab.add("Fonts", createFontPanel());
    	mainTab.add("Decoration", createDecorationPanel());
    	
    	mainTab.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(mainTab.getSelectedIndex() == 2) {
					((CardLayout)widgetInfo.getLayout()).show(widgetInfo, "show");
				}
				else {
					((CardLayout)widgetInfo.getLayout()).show(widgetInfo, "hide");
				}
			}    	
    	});

    	p1.add(mainTab, BorderLayout.CENTER);
    	
    	p0.add(p1, BorderLayout.NORTH);
    	
    	// Apply/Init Settings
    	p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 92, 4));
    	JPanel p2 = new JPanel(new BorderLayout(3, 0));
    	p2.setBackground(infoColor);
    	p2.setBorder(infoBorder);
			
		JLabel l = new JLabel("<html><b>Note: </b>Properties marked with");
		l.setForeground(Color.BLACK);
    	p2.add(l, BorderLayout.WEST);
    	p2.add(new JLabel(icon99), BorderLayout.CENTER);
    	l = new JLabel("apply to 99 Style only.");
		l.setForeground(Color.BLACK);
    	p2.add(l, BorderLayout.EAST);
    	
    	widgetInfo = new JPanel(new CardLayout());
    	widgetInfo.add(new JLabel(), "hide");
    	widgetInfo.add(p2, "show");
    	p1.add(widgetInfo);
    	
      	updateThemeButton = new JButton("Apply Settings");
      	buttons[14] = updateThemeButton;
      	theFrame.getRootPane().setDefaultButton(updateThemeButton);
      	updateThemeButton.addActionListener(new SetThemeAction());
      	p1.add(updateThemeButton);

      	p0.add(p1, BorderLayout.SOUTH);
    	theFrame.getContentPane().add(p0, BorderLayout.NORTH);
    	
    	examplePanel = new ExamplePanel();
    	p0 = new JPanel(new BorderLayout());
		p0.setBorder(new TitledBorder("Examples"));
		p0.add(examplePanel, BorderLayout.CENTER);
    	theFrame.getContentPane().add(p0, BorderLayout.CENTER);

    	Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
 
      	theFrame.pack();
		theFrame.setLocation((d.width - theFrame.getWidth()) / 2,
			(d.height - theFrame.getHeight()) / 3);
		theFrame.setVisible(true);
	}
	
	private void decorateFrame(boolean b) {
		theFrame.dispose();

		createFrame();
		mainTab.setSelectedComponent(compTab);
		compTab.setSelectedComponent(frameCP);
	}
	
	private JPanel createColorPanel() {
		JPanel p0 = new JPanel(new BorderLayout());		
		JPanel p1 = new JPanel(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.anchor = GridBagConstraints.NORTHWEST;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.insets = new Insets(2, 4, 0, 4);

		
		p1.add(new JLabel("Main Color"), gc);
		gc.gridx ++;
		
		gc.insets = new Insets(2, 8, 0, 4);
		p1.add(new JLabel("Background Color"), gc);
		gc.gridx ++;
		
		p1.add(new JLabel("Disabled Color"), gc);
		gc.gridx ++;
		
		p1.add(new JLabel("Frame Color"), gc);
		
		gc.gridx = 0;
		gc.gridy ++;
		gc.insets = new Insets(2, 4, 8, 4);

		mainField = new SBField(Theme.mainColor, 24);
		p1.add(mainField, gc);
		gc.gridx ++;
		
		gc.insets = new Insets(2, 8, 8, 4);
		backField = new SBField(Theme.backColor, 24);
		p1.add(backField, gc);
		gc.gridx ++;
		
		rollField = new SBField(Theme.disColor, 24);
		p1.add(rollField, gc);
		gc.gridx ++;
		
		frameField = new SBField(Theme.frameColor, 24);
		frameField.setName("ff");
		p1.add(frameField, gc);
		
		gc.gridx = 0;
		gc.gridy ++;
		gc.insets = new Insets(2, 4, 0, 4);
		p1.add(new JLabel("Sub1 Color"), gc);
		gc.gridx ++;
		
		gc.insets = new Insets(2, 8, 0, 4);
		p1.add(new JLabel("Sub2 Color"), gc);
		gc.gridx ++;
		
		p1.add(new JLabel("Sub3 Color"), gc);
		gc.gridx ++;
		
		p1.add(new JLabel("Sub4 Color"), gc);

		gc.gridx = 0;
		gc.gridy ++;
		gc.insets = new Insets(2, 4, 8, 4);
		sub1Field = new SBField(Theme.sub1Color, true);
		p1.add(sub1Field, gc);
		gc.gridx ++;
		
		gc.insets = new Insets(2, 8, 8, 4);
		sub2Field = new SBField(Theme.sub2Color, true);
		p1.add(sub2Field, gc);
		gc.gridx ++;
		
		sub3Field = new SBField(Theme.sub3Color, true);
		p1.add(sub3Field, gc);
		gc.gridx ++;
		
		sub4Field = new SBField(Theme.sub4Color, true);
		p1.add(sub4Field, gc);
		
		gc.gridx = 0;
		gc.gridy ++;
		gc.insets = new Insets(2, 4, 0, 4);
		p1.add(new JLabel("Sub5 Color"), gc);
		gc.gridx ++;
		
		gc.insets = new Insets(2, 8, 0, 4);
		p1.add(new JLabel("Sub6 Color"), gc);
		gc.gridx ++;
		
		p1.add(new JLabel("Sub7 Color"), gc);
		gc.gridx ++;
		
		p1.add(new JLabel("Sub8 Color"), gc);

		gc.gridx = 0;
		gc.gridy ++;
		gc.insets = new Insets(2, 4, 8, 4);
		sub5Field = new SBField(Theme.sub5Color, true);
		p1.add(sub5Field, gc);
		gc.gridx ++;
		
		gc.insets = new Insets(2, 8, 8, 4);
		sub6Field = new SBField(Theme.sub6Color, true);
		p1.add(sub6Field, gc);
		gc.gridx ++;
		
		sub7Field = new SBField(Theme.sub7Color, true);
		p1.add(sub7Field, gc);
		gc.gridx ++;
		
		sub8Field = new SBField(Theme.sub8Color, true);
		p1.add(sub8Field, gc);

		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 12));
		p2.add(p1);
		
		p0.add(p2, BorderLayout.NORTH);
		
		return p0;
	}
	
	private JToolBar createToolBar() {
		theToolBar = new JToolBar();
		
		ButtonGroup group = new ButtonGroup();
		JToggleButton b = null;
		Dimension iconSize = new Dimension(16, 18);
		
		for(int i = 0; i < 6; i++) {
			b = new JToggleButton("", new ColorIcon(iconSize));
			group.add(b);
			theToolBar.add(b);
		}
		
		theToolBar.addSeparator();
		
		for(int i = 0; i < 5; i++) {
			b = new JToggleButton("", new ColorIcon(iconSize));
			group.add(b);
			theToolBar.add(b);
		}
		
		theToolBar.addSeparator();
		
		for(int i = 0; i < 4; i++) {
			b = new JToggleButton("", new ColorIcon(iconSize));
			group.add(b);
			theToolBar.add(b);
		}
		
		b = new JToggleButton("TB_Button");
		theToolBar.add(b);

		return theToolBar;
	}
	
	private StyledDocument createStyledDocument() {
		StyledDocument doc = new DefaultStyledDocument();
		Style defaultStyle = StyleContext.getDefaultStyleContext().
			getStyle(StyleContext.DEFAULT_STYLE);
		
		Style regular = doc.addStyle("regular", defaultStyle);
		StyleConstants.setFontFamily(regular, "SansSerif");
		StyleConstants.setFontSize(regular, 12);
		StyleConstants.setForeground(regular, Color.BLACK);
		StyleConstants.setUnderline(regular, false);
		StyleConstants.setBold(regular, false);
		StyleConstants.setItalic(regular, false);
		
		doc.setLogicalStyle(0, regular);

		try {
			doc.insertString(0, "         JTextPane with\n", regular);
		} catch (BadLocationException ignore) {}

		int position = 24;
		Color red = new Color(132, 0, 0);
		Style s = doc.addStyle("red24", regular);
		StyleConstants.setFontSize(s, 24);
		StyleConstants.setUnderline(s, true);
		StyleConstants.setForeground(s, red);
		
		try {
			doc.insertString(position++, "S", s);
		} catch (BadLocationException ignore) {}
		
		s = doc.addStyle("red22", s);
		StyleConstants.setFontSize(s, 22);
		StyleConstants.setUnderline(s, true);
		StyleConstants.setForeground(s, red);
		
		try {
			doc.insertString(position++, "t", s);
		} catch (BadLocationException ignore) {}
		
		s = doc.addStyle("red20", s);
		StyleConstants.setFontSize(s, 20);
		StyleConstants.setUnderline(s, true);
		StyleConstants.setForeground(s, red);
		
		try {
			doc.insertString(position++, "y", s);
		} catch (BadLocationException ignore) {}
		
		s = doc.addStyle("red18", s);
		StyleConstants.setFontSize(s, 18);
		StyleConstants.setUnderline(s, true);
		StyleConstants.setForeground(s, red);
		
		try {
			doc.insertString(position++, "l", s);
		} catch (BadLocationException ignore) {}
		
		s = doc.addStyle("red16", s);
		StyleConstants.setFontSize(s, 16);
		StyleConstants.setUnderline(s, true);
		StyleConstants.setForeground(s, red);
		
		try {
			doc.insertString(position++, "e", s);
		} catch (BadLocationException ignore) {}
		
		s = doc.addStyle("red14", s);
		StyleConstants.setFontSize(s, 14);
		StyleConstants.setUnderline(s, true);
		StyleConstants.setForeground(s, red);
		
		try {
			doc.insertString(position++, "d ", s);
		} catch (BadLocationException ignore) {}
		
		position++;
		Color green = new Color(0, 130, 132);
		s = doc.addStyle("green12", s);
		StyleConstants.setFontSize(s, 12);
		StyleConstants.setUnderline(s, true);
		StyleConstants.setForeground(s, green);
		
		try {
			doc.insertString(position++, "D", s);
		} catch (BadLocationException ignore) {}
		
		s = doc.addStyle("green13", s);
		StyleConstants.setFontSize(s, 13);
		StyleConstants.setUnderline(s, true);
		StyleConstants.setForeground(s, green);
		
		try {
			doc.insertString(position++, "o", s);
		} catch (BadLocationException ignore) {}
		
		s = doc.addStyle("green14", s);
		StyleConstants.setFontSize(s, 14);
		StyleConstants.setUnderline(s, true);
		StyleConstants.setForeground(s, green);
		
		try {
			doc.insertString(position++, "c", s);
		} catch (BadLocationException ignore) {}
		
		s = doc.addStyle("green16", s);
		StyleConstants.setFontSize(s, 16);
		StyleConstants.setUnderline(s, true);
		StyleConstants.setForeground(s, green);
		
		try {
			doc.insertString(position++, "u", s);
		} catch (BadLocationException ignore) {}
		
		s = doc.addStyle("green18", s);
		StyleConstants.setFontSize(s, 18);
		StyleConstants.setUnderline(s, true);
		StyleConstants.setForeground(s, green);
		
		try {
			doc.insertString(position++, "m", s);
		} catch (BadLocationException ignore) {}
		
		s = doc.addStyle("green20", s);
		StyleConstants.setFontSize(s, 20);
		StyleConstants.setUnderline(s, true);
		StyleConstants.setForeground(s, green);
		
		try {
			doc.insertString(position++, "e", s);
		} catch (BadLocationException ignore) {}
		
		s = doc.addStyle("green22", s);
		StyleConstants.setFontSize(s, 22);
		StyleConstants.setUnderline(s, true);
		StyleConstants.setForeground(s, green);
		
		try {
			doc.insertString(position++, "n", s);
		} catch (BadLocationException ignore) {}
		
		s = doc.addStyle("green24", s);
		StyleConstants.setFontSize(s, 24);
		StyleConstants.setUnderline(s, true);
		StyleConstants.setForeground(s, green);
		
		try {
			doc.insertString(position++, "t", s);
		} catch (BadLocationException ignore) {}
		
		return doc;
	}
	
	private JPopupMenu createSBPopup() {
		if(sbPopup != null) return sbPopup;
		
		ActionListener hsbPopupAction = new SBPopupAction();
		sbPopup = new JPopupMenu();
		
		JMenuItem item = new JMenuItem("Absolute Color");
		item.setActionCommand("1");
		item.addActionListener(hsbPopupAction);
		sbPopup.add(item);

		sbPopup.addSeparator();
		
		item = new JMenuItem("Derive from Main Color");
		item.setActionCommand("2");
		item.addActionListener(hsbPopupAction);
		sbPopup.add(item);
		
		item = new JMenuItem("Derive from Back Color");
		item.setActionCommand("3");
		item.addActionListener(hsbPopupAction);
		sbPopup.add(item);
		
		item = new JMenuItem("Derive from Disabled Color");
		item.setActionCommand("4");
		item.addActionListener(hsbPopupAction);
		sbPopup.add(item);
		
		item = new JMenuItem("Derive from Frame Color");
		item.setActionCommand("5");
		item.addActionListener(hsbPopupAction);
		sbPopup.add(item);
		
		item = new JMenuItem("Derive from Sub1 Color");
		item.setActionCommand("6");
		item.addActionListener(hsbPopupAction);
		sbPopup.add(item);
		
		item = new JMenuItem("Derive from Sub2 Color");
		item.setActionCommand("7");
		item.addActionListener(hsbPopupAction);
		sbPopup.add(item);
		
		item = new JMenuItem("Derive from Sub3 Color");
		item.setActionCommand("8");
		item.addActionListener(hsbPopupAction);
		sbPopup.add(item);
		
		item = new JMenuItem("Derive from Sub4 Color");
		item.setActionCommand("9");
		item.addActionListener(hsbPopupAction);
		sbPopup.add(item);
		
		item = new JMenuItem("Derive from Sub5 Color");
		item.setActionCommand("10");
		item.addActionListener(hsbPopupAction);
		sbPopup.add(item);
		
		item = new JMenuItem("Derive from Sub6 Color");
		item.setActionCommand("11");
		item.addActionListener(hsbPopupAction);
		sbPopup.add(item);
		
		item = new JMenuItem("Derive from Sub7 Color");
		item.setActionCommand("12");
		item.addActionListener(hsbPopupAction);
		sbPopup.add(item);
		
		item = new JMenuItem("Derive from Sub8 Color");
		item.setActionCommand("13");
		item.addActionListener(hsbPopupAction);
		sbPopup.add(item);
		
		return sbPopup;
	}
	
	private void updateSBPopupIcons() {
		MenuElement[] me = sbPopup.getSubElements();
		
		((JMenuItem)me[0]).setIcon(ColorReference.getAbsoluteIcon());
		((JMenuItem)me[1]).setIcon(Theme.mainColor[Theme.style].getIcon());
		((JMenuItem)me[2]).setIcon(Theme.backColor[Theme.style].getIcon());
		((JMenuItem)me[3]).setIcon(Theme.disColor[Theme.style].getIcon());
		((JMenuItem)me[4]).setIcon(Theme.frameColor[Theme.style].getIcon());
		((JMenuItem)me[5]).setIcon(Theme.sub1Color[Theme.style].getIcon());
		((JMenuItem)me[6]).setIcon(Theme.sub2Color[Theme.style].getIcon());
		((JMenuItem)me[7]).setIcon(Theme.sub3Color[Theme.style].getIcon());
		((JMenuItem)me[8]).setIcon(Theme.sub4Color[Theme.style].getIcon());
		((JMenuItem)me[9]).setIcon(Theme.sub5Color[Theme.style].getIcon());
		((JMenuItem)me[10]).setIcon(Theme.sub6Color[Theme.style].getIcon());
		((JMenuItem)me[11]).setIcon(Theme.sub7Color[Theme.style].getIcon());
		((JMenuItem)me[12]).setIcon(Theme.sub8Color[Theme.style].getIcon());

		for(int i = 0; i < 13; i++) {
			((JMenuItem)me[i]).setSelected(false);
		}
		
		for(int i = 5; i < 13; i++) {
			((JMenuItem)me[i]).setEnabled(true);
		}
	}
	
	private void showSBPopup(SBField cf) {
		updateSBPopupIcons();
		
		selectedSBField = cf;
		int index = cf.getColorReference().getReference() - 1;
		MenuElement[] me = sbPopup.getSubElements();
		
		((JMenuItem)me[index]).setSelected(true);
		
		if(cf.equals(sub1Field)) {
			((JMenuItem)me[5]).setEnabled(false);
		}
		else if(cf.equals(sub2Field)) {
			((JMenuItem)me[6]).setEnabled(false);
		}
		else if(cf.equals(sub3Field)) {
			((JMenuItem)me[7]).setEnabled(false);
		}
		else if(cf.equals(sub4Field)) {
			((JMenuItem)me[8]).setEnabled(false);
		}
		else if(cf.equals(sub5Field)) {
			((JMenuItem)me[9]).setEnabled(false);
		}
		else if(cf.equals(sub6Field)) {
			((JMenuItem)me[10]).setEnabled(false);
		}
		else if(cf.equals(sub7Field)) {
			((JMenuItem)me[11]).setEnabled(false);
		}
		else if(cf.equals(sub8Field)) {
			((JMenuItem)me[12]).setEnabled(false);
		}
		
		sbPopup.show(cf, 0, cf.getHeight() + 2);
	}
	
	private JPopupMenu createHSBPopup() {
		if(hsbPopup != null) return hsbPopup;
		
		ActionListener colorizePopupAction = new HSBPopupAction();
		hsbPopup = new JPopupMenu();
		
		JMenuItem item = new JMenuItem("Derive from Main Color");
		item.setActionCommand("2");
		item.addActionListener(colorizePopupAction);
		hsbPopup.add(item);
		
		item = new JMenuItem("Derive from Back Color");
		item.setActionCommand("3");
		item.addActionListener(colorizePopupAction);
		hsbPopup.add(item);
		
		item = new JMenuItem("Derive from Disabled Color");
		item.setActionCommand("4");
		item.addActionListener(colorizePopupAction);
		hsbPopup.add(item);
		
		item = new JMenuItem("Derive from Frame Color");
		item.setActionCommand("5");
		item.addActionListener(colorizePopupAction);
		hsbPopup.add(item);
		
		item = new JMenuItem("Derive from Sub1 Color");
		item.setActionCommand("6");
		item.addActionListener(colorizePopupAction);
		hsbPopup.add(item);
		
		item = new JMenuItem("Derive from Sub2 Color");
		item.setActionCommand("7");
		item.addActionListener(colorizePopupAction);
		hsbPopup.add(item);
		
		item = new JMenuItem("Derive from Sub3 Color");
		item.setActionCommand("8");
		item.addActionListener(colorizePopupAction);
		hsbPopup.add(item);
		
		item = new JMenuItem("Derive from Sub4 Color");
		item.setActionCommand("9");
		item.addActionListener(colorizePopupAction);
		hsbPopup.add(item);
		
		item = new JMenuItem("Derive from Sub5 Color");
		item.setActionCommand("10");
		item.addActionListener(colorizePopupAction);
		hsbPopup.add(item);
		
		item = new JMenuItem("Derive from Sub6 Color");
		item.setActionCommand("11");
		item.addActionListener(colorizePopupAction);
		hsbPopup.add(item);
		
		item = new JMenuItem("Derive from Sub7 Color");
		item.setActionCommand("12");
		item.addActionListener(colorizePopupAction);
		hsbPopup.add(item);
		
		item = new JMenuItem("Derive from Sub8 Color");
		item.setActionCommand("13");
		item.addActionListener(colorizePopupAction);
		hsbPopup.add(item);
		
		return hsbPopup;
	}
	
	private void updateHSBPopupIcons() {
		MenuElement[] me = hsbPopup.getSubElements();
		
		((JMenuItem)me[0]).setIcon(Theme.mainColor[Theme.style].getIcon());
		((JMenuItem)me[1]).setIcon(Theme.backColor[Theme.style].getIcon());
		((JMenuItem)me[2]).setIcon(Theme.disColor[Theme.style].getIcon());
		((JMenuItem)me[3]).setIcon(Theme.frameColor[Theme.style].getIcon());
		((JMenuItem)me[4]).setIcon(Theme.sub1Color[Theme.style].getIcon());
		((JMenuItem)me[5]).setIcon(Theme.sub2Color[Theme.style].getIcon());
		((JMenuItem)me[6]).setIcon(Theme.sub3Color[Theme.style].getIcon());
		((JMenuItem)me[7]).setIcon(Theme.sub4Color[Theme.style].getIcon());
		((JMenuItem)me[8]).setIcon(Theme.sub5Color[Theme.style].getIcon());
		((JMenuItem)me[9]).setIcon(Theme.sub6Color[Theme.style].getIcon());
		((JMenuItem)me[10]).setIcon(Theme.sub7Color[Theme.style].getIcon());
		((JMenuItem)me[11]).setIcon(Theme.sub8Color[Theme.style].getIcon());

		for(int i = 0; i < 12; i++) {
			((JMenuItem)me[i]).setSelected(false);
		}
		
		for(int i = 4; i < 12; i++) {
			((JMenuItem)me[i]).setEnabled(true);
		}
	}
	
	private void showHSBPopup(HSBField cf) {
		updateHSBPopupIcons();
		
		selectedHSBField = cf;
		int index = cf.getReference() - 2;
		MenuElement[] me = hsbPopup.getSubElements();
		
		((JMenuItem)me[index]).setSelected(true);
		
		if(cf.equals(sub1Field)) {
			((JMenuItem)me[4]).setEnabled(false);
		}
		else if(cf.equals(sub2Field)) {
			((JMenuItem)me[5]).setEnabled(false);
		}
		else if(cf.equals(sub3Field)) {
			((JMenuItem)me[6]).setEnabled(false);
		}
		else if(cf.equals(sub4Field)) {
			((JMenuItem)me[7]).setEnabled(false);
		}
		else if(cf.equals(sub5Field)) {
			((JMenuItem)me[8]).setEnabled(false);
		}
		else if(cf.equals(sub6Field)) {
			((JMenuItem)me[9]).setEnabled(false);
		}
		else if(cf.equals(sub7Field)) {
			((JMenuItem)me[10]).setEnabled(false);
		}
		else if(cf.equals(sub8Field)) {
			((JMenuItem)me[11]).setEnabled(false);
		}
		
		hsbPopup.show(cf, 0, cf.getHeight() + 2);
	}
	
	private JPanel createFontPanel() {
		JPanel p1 = new JPanel(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.anchor = GridBagConstraints.NORTHWEST;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.insets = new Insets(4, 2, 4, 2);
		
		plainFontPanel = new FontPanel(PLAIN_FONT);
      	p1.add(plainFontPanel, gc);
      	gc.gridy ++;
      	
      	boldFontPanel = new FontPanel(BOLD_FONT);
      	p1.add(boldFontPanel, gc);
      	gc.gridy ++;

		gc.insets = new Insets(11, 2, 0, 2);
		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
      	p2.add(createFontCombo());
      	
      	p2.add(new JLabel("    "));
      	isPlainFont = new JRadioButton("is Plain Font");
      	isPlainFont.addActionListener(new DerivedFontAction());
      	p2.add(isPlainFont);
      	
      	p2.add(new JLabel("    "));
      	isBoldFont = new JRadioButton("is Bold Font");
      	isBoldFont.addActionListener(new DerivedFontAction());
      	p2.add(isBoldFont);
      	
      	p1.add(p2, gc);
      	gc.gridy ++;
      	
      	gc.insets = new Insets(2, 2, 0, 2);      	
      	specialFontPanel = new FontPanel(SPECIAL_FONT);
      	specialFontPanel.init(selectedFont[Theme.style]);
      	p1.add(specialFontPanel, gc);

		return p1;
	}
	
	private JComboBox createFontCombo() {
		Vector items = new Vector();

		items.add("Button Font");
		items.add("CheckBox Font");
		items.add("ComboBox Font");
		items.add("EditorPane Font");
		items.add("FrameTitle Font");
		items.add("InternalFrameTitle Font");
		items.add("InternalPaletteTitle Font");
		items.add("Label Font");
		items.add("List Font");
		items.add("Menu Font");
		items.add("MenuItem Font");
		items.add("Password Font");
		items.add("ProgressBar Font");
		items.add("RadioButton Font");
		items.add("Table Font");
		items.add("TableHeader Font");
		items.add("TextArea Font");
		items.add("TextField Font");
		items.add("TextPane Font");
		items.add("TitledBorder Font");
		items.add("ToolTip Font");
		items.add("Tree Font");
		items.add("TabbedPane Font");

		Collections.sort(items);
		
		fontCombo = new JComboBox(items);
		fontCombo.addActionListener(new SelectSpecialFontAction());
		
		selectedFont = Theme.buttonFont;
		
		return fontCombo;
	}
	
	private JTabbedPane createDecorationPanel() {
		compTab = new JTabbedPane();
		
		buttonsCP = new ButtonsCP();
		compTab.add("Button", buttonsCP);
		compTab.setMnemonicAt(0, KeyEvent.VK_B);
		compTab.setToolTipTextAt(0,
			"<html>JButton<br>" +
			"JToggleButton<br>" +
			"JRadioButton<br>" +
			"JCheckBox");
		
		comboCP = new ComboCP();
		compTab.add("ComboBox", comboCP);
		compTab.setMnemonicAt(1, KeyEvent.VK_C);
		
		frameCP = new FrameCP();
		compTab.add("Frame", frameCP);
		compTab.setMnemonicAt(2, KeyEvent.VK_F);
		compTab.setToolTipTextAt(2,
			"<html>JFrame<br>" +
			"JInternalFrame<br>" +
			"JDialog<br>" +
			"JOptionPane");
		
		iconCP = new IconCP();
		compTab.add("Icons", iconCP);
		compTab.setMnemonicAt(3, KeyEvent.VK_I);
		
		listCP = new ListCP();
		compTab.add("List", listCP);
		compTab.setMnemonicAt(4, KeyEvent.VK_L);

		menuCP = new MenuCP();
		compTab.add("Menu", menuCP);
		compTab.setMnemonicAt(5, KeyEvent.VK_M);
		compTab.setToolTipTextAt(5,
			"<html>JMenu<br>" +
			"JMenuItem<br>" +
			"JCheckBoxMenuItem<br>" +
			"JRadioButtonMenuItem");
		
		miscCP = new MiscCP();
		compTab.add("Miscellaneous", miscCP);
		
		progressCP = new ProgressCP();
		compTab.add("ProgressBar", progressCP);
		compTab.setMnemonicAt(7, KeyEvent.VK_P);
		
		scrollsCP = new ScrollBarCP();
		compTab.add("ScrollBar", scrollsCP);
		compTab.setMnemonicAt(8, KeyEvent.VK_S);
		compTab.setToolTipTextAt(8,
			"<html>JScrollPane<br>" +
			"JScrollBar");
		
		separatorCP = new SeparatorCP();
		compTab.add("Separator", separatorCP);
		
		sliderCP = new SliderCP();
		compTab.add("Slider", sliderCP);
		
		spinnerCP = new SpinnerCP();
		compTab.add("Spinner", spinnerCP);
		
		tabsCP = new TabbedPaneCP();
		compTab.add("TabbedPane", tabsCP);
		
		tableCP = new TableCP();
		compTab.add("Table", tableCP);
		
		textCP = new TextCP();
		compTab.add("Text", textCP);
		compTab.setToolTipTextAt(14,
			"<html>JTextField<br>" +
			"JFormattedTextField<br>" +
			"JTextArea<br>" +
			"JPasswordField<br>" +
			"JSpinner.Editor<br>" +
			"JComboBox.Editor");
		
		toolCP = new ToolBarCP();
		compTab.add("ToolBar", toolCP);
		compTab.setToolTipTextAt(15,
			"<html>JToolBar<br>" +
			"ToolBar Button<br>" +
			"JToolBar.Separator");
		
		tipCP = new ToolTipCP();
		compTab.add("ToolTip", tipCP);
		
		treeCP = new TreeCP();
		compTab.add("Tree", treeCP);
		
		return compTab;
	}
	
	private JMenu createFileMenu() {
    	JMenu menu = new JMenu("File");
    	menu.setMnemonic(KeyEvent.VK_F);

    	JMenuItem item = new JMenuItem("Open Theme...");
    	item.addActionListener(new ActionListener() {
      		public void actionPerformed(ActionEvent e) {
        		openTheme();
      		}
    	});
		item.setMnemonic(KeyEvent.VK_O);
    	item.setAccelerator(
      		KeyStroke.getKeyStroke(KeyEvent.VK_O, menuShortcutKeyMask));
    	menu.add(item);
    	
    	menu.addSeparator();
    	
    	item = new JMenuItem("Save");
    	item.addActionListener(new ActionListener() {
      		public void actionPerformed(ActionEvent e) {
        		saveTheme(false);
      		}
    	});
		item.setMnemonic(KeyEvent.VK_S);
    	item.setAccelerator(
      		KeyStroke.getKeyStroke(KeyEvent.VK_S, menuShortcutKeyMask));
    	menu.add(item);
    	
    	item = new JMenuItem("Save as Default");
    	item.addActionListener(new ActionListener() {
      		public void actionPerformed(ActionEvent e) {
        		saveDefaults();
      		}
    	});
    	item.setMnemonic(KeyEvent.VK_D);
    	item.setAccelerator(
      		KeyStroke.getKeyStroke(KeyEvent.VK_D, menuShortcutKeyMask));
    	menu.add(item);
    	
    	item = new JMenuItem("Save as...");
    	item.addActionListener(new ActionListener() {
      		public void actionPerformed(ActionEvent e) {
        		saveTheme(true);
      		}
    	});
		item.setMnemonic(KeyEvent.VK_A);
		item.setDisplayedMnemonicIndex(5);
    	item.setAccelerator(
      		KeyStroke.getKeyStroke(KeyEvent.VK_S,
      			menuShortcutKeyMask | ActionEvent.SHIFT_MASK));
    	menu.add(item);

    	menu.addSeparator();

    	item = new JMenuItem("Quit");
    	item.addActionListener(new ActionListener() {
      		public void actionPerformed(ActionEvent e) {
        		System.exit(0);
      		}
    	});
    	item.setMnemonic(KeyEvent.VK_Q);
    	item.setAccelerator(
      		KeyStroke.getKeyStroke(KeyEvent.VK_Q, menuShortcutKeyMask));
    	menu.add(item);

    	return menu;
  	}
  	
  	private Vector loadThemes() {
  		Vector names = new Vector();
  		
  		File f = new File(System.getProperty("user.dir"));
  		File[] files = f.listFiles();
  		
  		for(int i = 0; i < files.length; i++) {
  			if(files[i].getName().endsWith(Theme.FILE_EXTENSION)) {
  				names.add(files[i].getName());
  			}
  		}
  		
  		return names;
  	}
  	
  	private JMenu createThemeMenu() {
    	themesMenu = new JMenu("Themes");
    	themesMenu.setMnemonic(KeyEvent.VK_T);

    	JMenuItem item = null;
    	String fn = null;
    	
    	Iterator ii = loadThemes().iterator();
    	while(ii.hasNext()) {
    		fn = (String)ii.next();
    		item = new JMenuItem(fn.substring(0, fn.lastIndexOf(".")));
    		item.addActionListener(selectThemeAction);
    		themesMenu.add(item);
    	}

    	return themesMenu;
  	}
  	
  	private void updateThemeMenu() {
    	themesMenu.removeAll();

    	JMenuItem item = null;
    	String fn = null;
    	Iterator ii = loadThemes().iterator();
    	while(ii.hasNext()) {
    		fn = (String)ii.next();
    		item = new JMenuItem(fn.substring(0, fn.lastIndexOf(".")));
    		item.addActionListener(selectThemeAction);
    		themesMenu.add(item);
    	}
  	}
  	
  	private JMenu createStyleMenu() {
    	JMenu menu = new JMenu("Style");
    	menu.setMnemonic(KeyEvent.VK_S);

		ButtonGroup group = new ButtonGroup();
    	JCheckBoxMenuItem item = new JCheckBoxMenuItem("Tiny",
    		(Theme.style == Theme.TINY_STYLE));
    	group.add(item);
    	item.setEnabled(false);
    	item.addActionListener(new ActionListener() {
      		public void actionPerformed(ActionEvent e) {
        		updateStyle(Theme.TINY_STYLE);
      		}
    	});
    	//menu.add(item);
    	
    	item = new JCheckBoxMenuItem("99 Style", (Theme.style == Theme.W99_STYLE));
    	group.add(item);
    	item.addActionListener(new ActionListener() {
      		public void actionPerformed(ActionEvent e) {
        		updateStyle(Theme.W99_STYLE);
      		}
    	});
    	menu.add(item);
    	
    	item = new JCheckBoxMenuItem("YQ Style", (Theme.style == Theme.YQ_STYLE));
    	group.add(item);
    	item.addActionListener(new ActionListener() {
      		public void actionPerformed(ActionEvent e) {
        		updateStyle(Theme.YQ_STYLE);
      		}
    	});
    	menu.add(item);
    	
    	customStyle = new JCheckBoxMenuItem("Custom", false);
    	customStyle.setEnabled(false);
    	group.add(customStyle);
    	customStyle.addActionListener(new ActionListener() {
      		public void actionPerformed(ActionEvent e) {
      			updateStyle(Theme.CUSTOM_STYLE);
      		}
    	});
    	menu.add(customStyle);

    	return menu;
  	}
  	
  	private JMenu createDisabledMenu() {
    	JMenu menu = new JMenu("DisabledMenu");
    	
		menu.setEnabled(false);
 
    	return menu;
  	}
  	
  	private JMenu createTestMenu() {
    	JMenu menu = new JMenu("Test Menu");
    	menu.setMnemonic(KeyEvent.VK_M);
    	
		JMenuItem item = new JMenuItem("Open Dialog...");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new TestDialog(theFrame);
			}
		});
		menu.add(item);
		
		menu.addSeparator();
		
		item = new JMenuItem("<html><b>Note: </b>For JMenuItems displaying HTML text<br>" +
			"<font color=\"#0000ff\">Decoration | Menu | Selected Foreground<br>" +
			"</font><font color=\"#000000\">will have </font>" +
			"<font color=\"#ff0000\">no</font><font color=\"#000000\"> effect.");
		menu.add(item);
		
		item = new JCheckBoxMenuItem("Selected CheckBoxMenuItem", true);
		menu.add(item);
		item = new JCheckBoxMenuItem("Deselected CheckBoxMenuItem", false);
		menu.add(item);
		item = new JCheckBoxMenuItem("Disabled Selected CheckBoxMenuItem", true);
		item.setEnabled(false);
		menu.add(item);
		item = new JCheckBoxMenuItem("Disabled CheckBoxMenuItem", false);
		item.setEnabled(false);
		menu.add(item);
		
		menu.addSeparator();
		
		ButtonGroup group = new ButtonGroup();
		item = new JRadioButtonMenuItem("Selected RadioButtonMenuItem", true);
		group.add(item);
		menu.add(item);
		item = new JRadioButtonMenuItem("Deselected RadioButtonMenuItem");
		group.add(item);
		menu.add(item);
		item = new JRadioButtonMenuItem("Disabled Selected Item", true);
		item.setEnabled(false);
		menu.add(item);
		item = new JRadioButtonMenuItem("Disabled Item", false);
		item.setEnabled(false);
		menu.add(item);
		
		menu.addSeparator();
		
		item = new JMenuItem("Java version: " + System.getProperty("java.version"));
		menu.add(item);
		
		item = new JMenuItem("Disabled MenuItem");
		item.setEnabled(false);
		menu.add(item);
		
		menu.addSeparator();
		
		JMenu sub1 = new JMenu("Sub-menu 1");
		sub1.setMnemonic(KeyEvent.VK_1);
		item = new JMenuItem("Item 1");
		item.setMnemonic(KeyEvent.VK_1);
		sub1.add(item);
		item = new JMenuItem("Item 2");
		item.setMnemonic(KeyEvent.VK_2);
		sub1.add(item);
		
		sub1.addSeparator();
		
		JMenu sub2 = new JMenu("Disabled Submenu");
		sub2.setEnabled(false);
		item = new JMenuItem("SubmenuItem");
		sub2.add(item);
		sub1.add(sub2);
		
		menu.add(sub1);
 
    	return menu;
  	}
  	
  	private JMenu createHelpMenu() {
    	JMenu menu = new JMenu("Help");
    	menu.setMnemonic(KeyEvent.VK_H);
    	
		JMenuItem item = new JMenuItem("About TinyLaF...");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				String msg = "TinyLaF v" + TinyLookAndFeel.VERSION_STRING +
//					" (" + TinyLookAndFeel.DATE_STRING + ")" +
//					"\nAuthor: Hans Bickel" +
//					"\nTinyLaF Home: www.muntjak.de/hans/java/tinylaf/\n ";
//
//				JOptionPane.showMessageDialog(theFrame, msg,
//					"About TinyLaF", JOptionPane.PLAIN_MESSAGE);
				new AboutDialog();
			}
		});
		item.setMnemonic(KeyEvent.VK_A);
		menu.add(item);
		
		menu.addSeparator();
		
		item = new JMenuItem("Check for Updates...");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CheckForUpdatesDialog.showDialog(theFrame);
			}
		});
		item.setMnemonic(KeyEvent.VK_C);
		menu.add(item);

    	return menu;
  	}

  	private void addButtonIcons(boolean b) {
  		if(b && exampleButton.getIcon() == null) {
  			if(buttonIcon == null) {
  				buttonIcon = new ImageIcon(
					ClassLoader.getSystemResource(
					"de/muntjak/tinylookandfeel/icons/theIcon.gif"));
  			}
  			exampleButton.setIcon(buttonIcon);
  			exampleDisabledButton.setIcon(buttonIcon);
  			exampleToggleButton.setIcon(buttonIcon);
  		}
  		else if(!b && exampleButton.getIcon() != null) {
  			exampleButton.setIcon((Icon)null);
  			exampleDisabledButton.setIcon((Icon)null);
  			exampleToggleButton.setIcon((Icon)null);
  		}
  	}

  	private void updateFont(int type) {
  		if(type == PLAIN_FONT) {
  			Theme.plainFont[Theme.style].setFont(plainFontPanel.getCurrentFont());
  		}
  		else if(type == BOLD_FONT) {
  			Theme.boldFont[Theme.style].setFont(boldFontPanel.getCurrentFont());
  		}
  		else {
  			selectedFont[Theme.style].setFont(specialFontPanel.getCurrentFont());
  		}

  		examplePanel.update(true);
  	}
  	
  	/*
  	 * Alphabetical ordering!
  	 */
  	private void updateSpecialFont() {
  		int index = fontCombo.getSelectedIndex();
			
		switch(index) {
			case 0:
				selectedFont = Theme.buttonFont;
				break;
			case 1:
				selectedFont = Theme.checkFont;
				break;
			case 2:
				selectedFont = Theme.comboFont;
				break;
			case 3:
				selectedFont = Theme.editorFont;
				break;
			case 4:
				selectedFont = Theme.frameTitleFont;
				break;
			case 5:
				selectedFont = Theme.internalFrameTitleFont;
				break;
			case 6:
				selectedFont = Theme.internalPaletteTitleFont;
				break;
			case 7:
				selectedFont = Theme.labelFont;
				break;
			case 8:
				selectedFont = Theme.listFont;
				break;
			case 9:
				selectedFont = Theme.menuFont;
				break;
			case 10:
				selectedFont = Theme.menuItemFont;
				break;
			case 11:
				selectedFont = Theme.passwordFont;
				break;
			case 12:
				selectedFont = Theme.progressBarFont;
				break;
			case 13:
				selectedFont = Theme.radioFont;
				break;
			case 14:
				selectedFont = Theme.tabFont;
				break;
			case 15:
				selectedFont = Theme.tableFont;
				break;
			case 16:
				selectedFont = Theme.tableHeaderFont;
				break;
			case 17:
				selectedFont = Theme.textAreaFont;
				break;
			case 18:
				selectedFont = Theme.textFieldFont;
				break;
			case 19:
				selectedFont = Theme.textPaneFont;
				break;
			case 20:
				selectedFont = Theme.titledBorderFont;
				break;
			case 21:
				selectedFont = Theme.toolTipFont;
				break;
			case 22:
				selectedFont = Theme.treeFont;
				break;
		}
			
		specialFontPanel.init(selectedFont[Theme.style]);
		
		// update all font colors
		Theme.buttonFontColor[Theme.style].update();
		Theme.labelFontColor[Theme.style].update();
		Theme.menuFontColor[Theme.style].update();
		Theme.menuItemFontColor[Theme.style].update();
		Theme.radioFontColor[Theme.style].update();
		Theme.checkFontColor[Theme.style].update();
		Theme.tableFontColor[Theme.style].update();
		Theme.tableHeaderFontColor[Theme.style].update();
		Theme.titledBorderFontColor[Theme.style].update();
		Theme.tabFontColor[Theme.style].update();
		
		if(Theme.toolTipFontColor[Theme.style] != null) {
			Theme.toolTipFontColor[Theme.style].update();
		}
  	}
  	
  	private void setTheme() {
  		updateTheme();
  		
  		LookAndFeel currentLookAndFeel = UIManager.getLookAndFeel();
  		
  		try {
			UIManager.setLookAndFeel(currentLookAndFeel);
		}
  		catch(Exception e) {
			System.err.println(e.toString());
		}

  		SwingUtilities.updateComponentTreeUI(theFrame);
  		
  		if(sbPopup != null) {
  			SwingUtilities.updateComponentTreeUI(sbPopup);
  		}
  		
  		if(hsbPopup != null) {
  			SwingUtilities.updateComponentTreeUI(hsbPopup);
  		}
  		
  		updateThemeButton.setEnabled(false);
  		iconCP.init(true);
  		trigger.updateColors();
  		sp1.setViewportBorder(BorderFactory.createLineBorder(
  			Theme.treeBgColor[Theme.style].getColor(), 2));
  		sp2.setViewportBorder(BorderFactory.createLineBorder(
  			Theme.treeBgColor[Theme.style].getColor(), 2));
  		
  		theFrame.pack();
  		
  		PSColorChooser.deleteInstance();
        SBChooser.deleteInstance();
        HSBChooser.deleteInstance();
  	}
  	
  	private void updateTheme() {
//  		Theme.mainColor[Theme.style].setColor(mainField.getBackground());
//  		Theme.disColor[Theme.style].setColor(rollField.getBackground());
//  		Theme.backColor[Theme.style].setColor(backField.getBackground());
//  		Theme.frameColor[Theme.style].setColor(frameField.getBackground());
  		TinyDefaultTheme.secondary3 =
  			Theme.backColor[Theme.style].getColor();

  		updatePanels();
  		updateSpecialFont();
  		
//  		UIDefaults table = UIManager.getDefaults();
//  		table.put("Button.margin", buttonsCP.getButtonMargin());  		
//  		table.put("TabbedPane.tabAreaInsets", Theme.tabAreaInsets[Theme.style]);
  	}
  	
  	private void updatePanels() {
  		buttonsCP.updateTheme();
  		scrollsCP.updateTheme();
  		separatorCP.updateTheme();
  		tabsCP.updateTheme();
  		comboCP.updateTheme();
  		listCP.updateTheme();
  		sliderCP.updateTheme();
  		spinnerCP.updateTheme();
  		progressCP.updateTheme();
  		menuCP.updateTheme();
  		textCP.updateTheme();
  		treeCP.updateTheme();
  		toolCP.updateTheme();
  		tableCP.updateTheme();
  		frameCP.updateTheme();
  		iconCP.updateTheme();
  		tipCP.updateTheme();
  		miscCP.updateTheme();
  	}
  	
  	private void updateStyle(int newStyle) {
  		stopProgressTimer();
  		Theme.style = newStyle;
 
  		initColors();
  		initPanels();
        setTheme();
        startProgressTimer();
  	}
  	
  	private String getDescription() {
  		if(Theme.style == Theme.CUSTOM_STYLE) {
  			return currentFileName;
  		}
  		else if(Theme.style == Theme.W99_STYLE) {
  			return "99 Style";
  		}
  		else if(Theme.style == Theme.YQ_STYLE) {
  			return "YQ Style";
  		}
  		else if(Theme.style == Theme.TINY_STYLE) {
  			return "Tiny Style";
  		}
  		
  		return "";
  	}

	private void initPanels() {
  		resistUpdate = true;
  		
  		//initColors();

  		buttonsCP.init(true);
  		scrollsCP.init(true);
  		separatorCP.init(true);
  		tabsCP.init(true);
  		comboCP.init(true);
  		menuCP.init(true);
  		listCP.init(true);
  		sliderCP.init(true);
  		spinnerCP.init(true);
  		progressCP.init(true);
  		textCP.init(true);
  		treeCP.init(true);
  		toolCP.init(true);
  		tableCP.init(true);
  		frameCP.init(true);
  		iconCP.init(true);
  		tipCP.init(true);
  		miscCP.init(true);
  		
  		initFonts();
  		
  		resistUpdate = false;

  		theFrame.setTitle(WINDOW_TITLE + " - " + getDescription());
  	}
  	
  	private void initColors() {
  		mainField.setBackground(Theme.mainColor[Theme.style].getColor());
  		rollField.setBackground(Theme.disColor[Theme.style].getColor());
  		backField.setBackground(Theme.backColor[Theme.style].getColor());
  		frameField.setBackground(Theme.frameColor[Theme.style].getColor());
  		
  		// bug before 1.3.7: Tooltip text not updated
  		mainField.update();
  		rollField.update();
  		backField.update();
  		frameField.update();
  		// end bug
  		
  		sub1Field.update();
  		sub2Field.update();
  		sub3Field.update();
  		sub4Field.update();
  		sub5Field.update();
  		sub6Field.update();
  		sub7Field.update();
  		sub8Field.update();
  	}
  	
  	private void initFonts() {
  		plainFontPanel.init(Theme.plainFont[Theme.style]);
  		boldFontPanel.init(Theme.boldFont[Theme.style]);
  		updateSpecialFont();
  	}
  	
  	private void repaintTargets(Component[] targets) {
  		if(targets == null) return;
  		
  		for(int i = 0; i < targets.length; i++) {
  			targets[i].repaint();
  		}
  	}
  	
  	public static void setWindowButtons(JButton[] buttons) {
  		windowButtons = buttons;
  	}
  	
  	private void openTheme() {
  		JFileChooser ch = new JFileChooser(directoryPath);
  		ch.setFileFilter(new ThemeFileFilter());
  		int answer = ch.showOpenDialog(theFrame);
  		
  		if(answer != JFileChooser.APPROVE_OPTION) return;
  		
  		File f = ch.getSelectedFile();
  		
  		if(f == null) return;

  		if(!Theme.loadTheme(f, Theme.CUSTOM_STYLE)) {
  			JOptionPane.showMessageDialog(theFrame,
  				"This file is no valid TinyLaF theme.",
  				"Error loading file",
  				JOptionPane.ERROR_MESSAGE);

  			return;
  		}
  		
  		currentFileName = f.getAbsolutePath();
  		
  		if(f.getParent() != null) {
  			directoryPath = f.getParent();
  		}
  		
  		customStyle.setEnabled(true);
  		customStyle.setSelected(true);
  		updateStyle(Theme.CUSTOM_STYLE);
  	}
  	
  	/**
  	 * Called if user selects theme from Themes menu.
  	 * @param fn
  	 */
  	private void openTheme(String fn) {
  		File f = new File(fn);
  		
  		if(!Theme.loadTheme(f, Theme.CUSTOM_STYLE)) {
  			JOptionPane.showMessageDialog(theFrame,
  				"This file is no valid TinyLaF theme.",
  				"Error loading file",
  				JOptionPane.ERROR_MESSAGE);

  			return;
  		}
  		
  		currentFileName = fn;
  		
  		customStyle.setEnabled(true);
  		customStyle.setSelected(true);
  		updateStyle(Theme.CUSTOM_STYLE);
  	}
  	
  	private void saveTheme(boolean showFileChooser) {
  		if(currentFileName != null && !showFileChooser && Theme.style == Theme.CUSTOM_STYLE) {
  			Theme.saveTheme(currentFileName);
  			updateThemeMenu();
  			return;
  		}
  		
  		JFileChooser ch = new JFileChooser(System.getProperty("user.dir"));
  		ch.setFileFilter(new ThemeFileFilter());
  		ch.setSelectedFile(new File(
  			System.getProperty("user.dir") + File.separator + "Untitled.theme"));
  			
  		int answer = ch.showSaveDialog(theFrame);
  		
  		if(answer == JFileChooser.CANCEL_OPTION) return;
  		
  		File f = ch.getSelectedFile();
  		
  		if(f == null) return;

		currentFileName = createFileExtension(f, Theme.FILE_EXTENSION);
  		Theme.saveTheme(currentFileName);
  		updateThemeMenu();
  		
  		if(Theme.style != Theme.CUSTOM_STYLE) {
  			// load the previously saved file to make it custom
  			openTheme(currentFileName);
  		}
  		theFrame.setTitle(WINDOW_TITLE + " - " + getDescription());
  	}
  	
  	private String createFileExtension(File f, String ext) {
  		String fn = f.getAbsolutePath();
  		
  		if(fn.endsWith(ext)) return fn;
  		
  		if(fn.lastIndexOf(".") < fn.lastIndexOf(File.separator)) {
  			return fn + ext;
  		}
  		
  		return fn.substring(0, fn.lastIndexOf(".")) + ext;
  	}

  	private void saveDefaults() {
  		Theme.saveTheme(Theme.DEFAULT_THEME);
  		updateThemeMenu();
  	}
	
	public static void main(String[] args) {
		TinyLookAndFeel.controlPanelInstantiated = true;

		// $JAVA_HOME/jre/lib/swing.properties:
		// swing.defaultlaf = de.muntjak.tinylookandfeel.TinyLookAndFeel
		// System.property: swing.defaultlaf
		
		// the following also works:
		//System.setProperty("swing.defaultlaf", "de.muntjak.tinylookandfeel.TinyLookAndFeel");

		try {
			UIManager.setLookAndFeel("de.muntjak.tinylookandfeel.TinyLookAndFeel");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		        new ControlPanel();
		    }
		});
	}

	public class SBField extends JPanel {
		
		private boolean forceUpdate = false;
		private boolean only99 = false;
		private Dimension size = new Dimension(64, 18);
		private ColorReference[] ref;

		SBField(ColorReference[] ref) {
			this(ref, false);
		}
		
		SBField(ColorReference[] ref, boolean forceUpdate) {
			this.ref = ref;
			this.forceUpdate = forceUpdate;
			
			setBorder(sbFieldBorder);
			
			if(ref == null) return;

			update();
			addMouseListener(new Mousey());
		}
		
		SBField(ColorReference[] ref, boolean forceUpdate, boolean only99) {
			this.ref = ref;
			this.forceUpdate = forceUpdate;
			this.only99 = only99;
			
			setBorder(sbFieldBorder);
			
			if(ref == null) return;
			
			update();
			addMouseListener(new Mousey());
		}
		
		SBField(ColorReference[] ref, int height) {
			this.ref = ref;
			forceUpdate = true;
			size.height = height;
			
			setBorder(sbFieldBorder);
			
			if(ref == null) return;

			update();
			addMouseListener(new Mousey());
		}
		
		public ColorReference getColorReference() {
			return ref[Theme.style];
		}
		
		public void resetReference() {
			if(ref == null) return;
			
			ref[Theme.style].reset();
		}
		
		public Color getColor() {
			return ref[Theme.style].getColor();
		}
		
		public void setBackground(Color bg) {
			if(ref == null) {
				super.setBackground(bg);
			}
			else {
				super.setBackground(ref[Theme.style].getColor());
			}
		}
		
		public void update() {
			if(ref != null) {
				setBackground(ref[Theme.style].update());
			}
			
			repaint();
			updateTTT();
		}

		private void updateTTT() {
			if(ref == null) {
				setToolTipText(null);
				return;
			} 
			
			Color c = ref[Theme.style].getColor();
			StringBuffer buff = new StringBuffer();
			
			if(ref[Theme.style].isAbsoluteColor()) {
				buff.append("R:" + c.getRed());
				buff.append(" G:" + c.getGreen());
				buff.append(" B:" + c.getBlue());
			}
			else {
				buff.append("S:" + ref[Theme.style].getSaturation());
				buff.append(" B:" + ref[Theme.style].getBrightness());
				buff.append(" (" + ref[Theme.style].getReferenceString() + ")");
				buff.append(" R:" + c.getRed());
				buff.append(" G:" + c.getGreen());
				buff.append(" B:" + c.getBlue());
			}

			setToolTipText(buff.toString());
		}
		
		public void setColorReference(ColorReference[] ref) {
			this.ref = ref;
			update();
		}
		
		public Dimension getPreferredSize() {
			return size;
		}
		
		public void paint(Graphics g) {
			if(ref == null) {
				g.setColor(Theme.backColor[Theme.style].getColor());
				g.fillRect(0, 0, getWidth(), getHeight());
				return;
			}
			
			super.paint(g);
			
			if(ref[Theme.style].isLocked()) return;
			
			if(only99) {
				g.drawImage(image99, 1, 1, this);
			}

			if(ref[Theme.style].isAbsoluteColor()) {
				int x = getWidth() - 19;
				float hue = 0.0f;
			
				g.setColor(Color.BLACK);
				g.drawLine(x - 1, 1, x - 1, getHeight() - 2);
			
				for(int i = 0; i < 18; i++) {
					g.setColor(Color.getHSBColor(hue, 0.5f, 1.0f));
					g.drawLine(x + i, 1, x + i, getHeight() - 2);
					hue += 1.0 / 19.0;
				}
			}
			else {
				int x = getWidth() - 19;
				int grey = 255;
			
				g.setColor(Color.BLACK);
				g.drawLine(x - 1, 1, x - 1, getHeight() - 2);
			
				for(int i = 0; i < 18; i++) {
					g.setColor(new Color(grey, grey, grey));
					g.drawLine(x + i, 1, x + i, getHeight() - 2);
					grey -= 255 / 18;
				}
			}
		}

		class Mousey extends MouseAdapter {

			public void mouseReleased(MouseEvent e) {
				if(e.isPopupTrigger() && !ref[Theme.style].isLocked()) {
					showSBPopup((SBField)e.getSource());
				}
			}
			
			public void mousePressed(MouseEvent e) {
				if(ref == null) return;
				
				SBField cf = (SBField)e.getSource();
				cf.requestFocus();
				
				if(e.isPopupTrigger() && !ref[Theme.style].isLocked()) {
					showSBPopup(cf);
					return;
				}
				
				if(e.getX() > cf.getWidth() - 19 && !ref[Theme.style].isLocked()) {
					showSBPopup(cf);
					return;
				}
				
				if(e.getButton() != MouseEvent.BUTTON1) return;

				Color newColor = null;
				
				if(ref[Theme.style].isAbsoluteColor()) {
					newColor =
						PSColorChooser.showColorChooser(theFrame, cf.getColor());
						
					if(newColor == null) return;	// cancelled
					
					cf.getColorReference().setColor(newColor);
					cf.setBackground(newColor);
				}
				else {
					newColor = SBChooser.showSBChooser(theFrame, cf);
				
					if(newColor == null) return;	// cancelled
				
					cf.getColorReference().setColor(SBChooser.getSat(), SBChooser.getBri());
					cf.setBackground(newColor);
				}
				
				updateTTT();
				initPanels();
				examplePanel.update(forceUpdate);
			}
		}
	}

	class HSBField extends JPanel {
		
		private ActionListener action;
		private int hue, bri, sat = 25, reference;
		private boolean preserveGrey;
		private boolean forceUpdate = false;
		private Dimension size = new Dimension(33, 16);
		private HSBReference[] ref;
		private int index;

		HSBField(HSBReference[] ref, ActionListener action, int index) {
			this.ref = ref;
			this.action = action;
			this.index = index;
			hue = ref[Theme.style].getHue();
			sat = ref[Theme.style].getSaturation();
			bri = ref[Theme.style].getBrightness();
			preserveGrey = ref[Theme.style].isPreserveGrey();
			reference = ref[Theme.style].getReference();
			forceUpdate = true;
			
			setBorder(new LineBorder(Color.DARK_GRAY, 1));
			
			if(ref == null) return;
			
			update();
			addMouseListener(new Mousey());
		}

		public HSBReference getColorReference() {
			return ref[Theme.style];
		}
		
		public int getHue() {
			return hue;
		}
		
		public int getSaturation() {
			return sat;
		}
	
		public int getBrightness() {
			return bri;
		}
		
		public boolean isPreserveGrey() {
			return preserveGrey;
		}

		public void setPreserveGrey(boolean b) {
			preserveGrey = b;
		}
		
		public int getReference() {
			return reference;
		}
		
		public void setReference(int newReference, boolean updateHue) {
			reference = newReference;
			
			if(updateHue) {
				hue = ColorRoutines.calculateHue(ColorReference.getReferenceColor(reference));
			}
			
			update();
			
			if(iconChecks[index].isSelected()) {
				ActionEvent ae = new ActionEvent(this, Event.ACTION_EVENT, "");
				getAction().actionPerformed(ae);
			}
		}

		public void setHue(int newHue) {
			hue = newHue;
		}
		
		public void setSaturation(int newSat) {
			sat = newSat;
		}
	
		public void setBrightness(int newBri) {
			bri = newBri;
		}

		public void update() {
			if(ref != null) {
				setBackground(calculateBackground(ColorReference.getReferenceColor(reference)));
			}
			repaint();
			updateTTT();
		}
		
		private Color calculateBackground(Color c) {
			float[] f = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
			f[0] = (float)((float)hue / 360.0);
			
			return Color.getHSBColor(f[0], f[1], f[2]);
		}
		
		public void calculateHue() {
			Color c = ref[Theme.style].getReferenceColor();
			hue = ColorRoutines.calculateHue(c);
		}
		
		private void updateTTT() {
			if(ref == null) {
				setToolTipText(null);
				return;
			} 
			
			StringBuffer buff = new StringBuffer();

			buff.append("H:" + hue);
			buff.append(" S:" + sat);
			buff.append(" B:" + bri);
			buff.append(" (" + ref[Theme.style].getReferenceString() + ")");
			
			setToolTipText(buff.toString());
		}

		public Dimension getPreferredSize() {
			return size;
		}
		
		public void paint(Graphics g) {
			if(ref == null) {
				g.setColor(Theme.backColor[Theme.style].getColor());
				g.fillRect(0, 0, getWidth(), getHeight());
				return;
			}

			super.paint(g);

			int x = getWidth() - 17;
			int grey = 255;
		
			g.setColor(Color.BLACK);
			g.drawLine(x - 1, 1, x - 1, getHeight() - 2);
		
			for(int i = 0; i < 16; i++) {
				g.setColor(new Color(grey, grey, grey));
				g.drawLine(x + i, 1, x + i, getHeight() - 2);
				grey -= 255 / 16;
			}
		}
		
		public ActionListener getAction() {
			return action;
		}

		class Mousey extends MouseAdapter {

			public void mouseReleased(MouseEvent e) {
				if(e.isPopupTrigger() && !ref[Theme.style].isLocked()) {
					showSBPopup((SBField)e.getSource());
				}
			}
			
			public void mousePressed(MouseEvent e) {
				if(ref == null) return;
				
				HSBField cf = (HSBField)e.getSource();
				cf.requestFocus();
				
				if(e.isPopupTrigger() && !ref[Theme.style].isLocked()) {
					showHSBPopup(cf);
					return;
				}
				else if(e.getX() > cf.getWidth() - 19 && !ref[Theme.style].isLocked()) {
					showHSBPopup(cf);
					return;
				}
				
				if(e.getButton() != MouseEvent.BUTTON1) return;
				
				if(!iconChecks[cf.index].isSelected()) {
					iconChecks[cf.index].setSelected(true);
				}
				
				int memHue = cf.hue;
				int memSat = cf.sat;
				int memBri = cf.bri;
				boolean memPreserveGrey = cf.preserveGrey;

				if(!HSBChooser.showColorizeDialog(theFrame, cf)) {	// cancelled
					cf.hue = memHue;
					cf.sat = memSat;
					cf.bri = memBri;
					cf.preserveGrey = memPreserveGrey;
					
					iconCP.colorizeIcon(cf, true);
					return;
				}
				
				hue = HSBChooser.getHue();
				sat = HSBChooser.getSaturation();
				bri = HSBChooser.getBrightness();
				preserveGrey = HSBChooser.isPreserveGrey();

				update();
				updateTTT();
				updateThemeButton.setEnabled(true);
			}
		}
	}
	
	class SelectSpecialFontAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			updateSpecialFont();
		}
	}
	
	class DerivedFontAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if(resistUpdate) return;
			
			if(e.getSource().equals(isPlainFont)) {
				if(isPlainFont.isSelected()) {
					isBoldFont.setSelected(false);
					selectedFont[Theme.style].setPlainFont(true);
				}
				else {
					selectedFont[Theme.style].setPlainFont(false);
				}
			}
			else if(e.getSource().equals(isBoldFont)) {
				if(isBoldFont.isSelected()) {
					isPlainFont.setSelected(false);
					selectedFont[Theme.style].setBoldFont(true);
				}
				else {
					selectedFont[Theme.style].setBoldFont(false);
				}
			}
			
			specialFontPanel.init(selectedFont[Theme.style]);
			updateFont(SPECIAL_FONT);
		}
	}
	
	class SBPopupAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			int ref = Integer.parseInt(e.getActionCommand());
			
			selectedSBField.getColorReference().setReference(ref);
			selectedSBField.resetReference();
			selectedSBField.update();
			initPanels();
			examplePanel.update(selectedSBField.forceUpdate);
		}
	}
	
	class HSBPopupAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			int ref = Integer.parseInt(e.getActionCommand());
			
			if(!iconChecks[selectedHSBField.index].isSelected()) {
				iconChecks[selectedHSBField.index].setSelected(true);
			}
			
			selectedHSBField.setReference(ref, true);
			updateThemeButton.setEnabled(true);
		}
	}
	
	/*
	 * Action for "Apply Settings" button
	 */
	class SetThemeAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			setTheme();
		}
	}
	
	class FontPanel extends JPanel {
		
		private int type;
		private JComboBox fontFamilyCombo, fontSizeCombo;
		private JCheckBox bold;
		private SBField colorField;
		
		FontPanel(int type) {
			this.type = type;
			
			setupUI();
		}
		
		private void setupUI() {
			ActionListener updateFontAction = new UpdateFontAction();
			
			Font theFont = null;			
			if(type == PLAIN_FONT) {
				theFont = Theme.plainFont[Theme.style].getFont();
			}
			else if(type == BOLD_FONT) {
				theFont = Theme.boldFont[Theme.style].getFont();
			}
			else {
				theFont = selectedFont[Theme.style].getFont();
			}
			
			setLayout(new FlowLayout(FlowLayout.LEFT, 3, 1));
			if(type == PLAIN_FONT) {
				setBorder(new TitledBorder("Plain Font"));
			}
			else if(type == BOLD_FONT) {
				setBorder(new TitledBorder("Bold Font"));
			}
			else {
				setBorder(new TitledBorder("Special Font"));
			}
			
			add(new JLabel("Family"));
			fontFamilyCombo = createSchriftarten(theFont);
			fontFamilyCombo.addActionListener(updateFontAction);
			add(fontFamilyCombo);
			
			add(new JLabel("  Size"));
			fontSizeCombo = createSchriftgroessen(theFont);
			fontSizeCombo.addActionListener(updateFontAction);
			add(fontSizeCombo);
			
			add(new JLabel("    "));
			bold = new JCheckBox("Bold", theFont.isBold());
			bold.addActionListener(updateFontAction);
			add(bold);
			
			if(type == SPECIAL_FONT) {
				colorField = new SBField(selectedFont[Theme.style].getColorReference(), true);
				add(colorField);
			}
		}
		
		public String getFontFamily() {
			return (String)fontFamilyCombo.getSelectedItem();
		}
		
		public int getFontSize() {
			return Integer.parseInt(
				(String)fontSizeCombo.getSelectedItem());
		}
		
		public int getFontType() {
			if(bold.isSelected()) {
				return Font.BOLD;
			}
			
			return Font.PLAIN;
		}
		
		public FontUIResource getCurrentFont() {
			return new FontUIResource(getFontFamily(), getFontType(), getFontSize());
				
		}
		
		public void init(ColoredFont f) {
			resistUpdate = true;
			fontSizeCombo.setSelectedItem(String.valueOf(f.getFont().getSize()));
			fontFamilyCombo.setSelectedItem(f.getFont().getFamily(Locale.GERMANY));
			bold.setSelected(f.getFont().isBold());
			resistUpdate = false;

			if(colorField == null) return;

			resistUpdate = true;
			colorField.setColorReference(f.getColorReference());
			isPlainFont.setSelected(f.isPlainFont());
			isBoldFont.setSelected(f.isBoldFont());
			resistUpdate = false;
		}
		
		private JComboBox createSchriftarten(Font font) {
  			Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
  			TreeSet family = new TreeSet();
  	
  			for(int i = 0; i < fonts.length; i++) {
  				family.add(fonts[i].getFamily(Locale.GERMANY));
  			}
  	
  			JComboBox box = new JComboBox(new Vector(family));
  			
  			for(int i = 0; i < box.getItemCount(); i++) {
  				if(box.getItemAt(i).equals(font.getFamily(Locale.GERMANY))) {
  					box.setSelectedIndex(i);
  					break;
  				}
  			}

  			return box;
  		}
  
  		private JComboBox createSchriftgroessen(Font font) {
  			String[] groessen = new String[10];
            groessen[0] = "10";
            groessen[1] = "11";
            groessen[2] = "12";
            groessen[3] = "13";
            groessen[4] = "14";
            groessen[5] = "16";
            groessen[6] = "18";
            groessen[7] = "20";
            groessen[8] = "22";
            groessen[9] = "24";
            JComboBox box = new JComboBox(groessen);

            switch (font.getSize()) {
            case 10:
                box.setSelectedIndex(0);
                break;
            case 11:
                box.setSelectedIndex(1);
                break;
            case 12:
                box.setSelectedIndex(2);
                break;
            case 13:
                box.setSelectedIndex(3);
                break;
            case 14:
                box.setSelectedIndex(4);
                break;
            case 16:
                box.setSelectedIndex(5);
                break;
            case 18:
                box.setSelectedIndex(6);
                break;
            case 20:
                box.setSelectedIndex(7);
                break;
            case 22:
                box.setSelectedIndex(8);
                break;
            case 24:
                box.setSelectedIndex(9);
                break;
            }

            box.setMaximumRowCount(10);
  			return box;
  		}
  		
  		class UpdateFontAction implements ActionListener {
  			
			public void actionPerformed(ActionEvent e) {
				if(resistUpdate) return;
				
				if(type == SPECIAL_FONT) {
					selectedFont[Theme.style].setPlainFont(false);
					selectedFont[Theme.style].setBoldFont(false);
				}
				
				updateFont(type);
				
				specialFontPanel.init(selectedFont[Theme.style]);
			}
  		}
	}
	
	class ExamplePanel extends JPanel {
		
		private JTabbedPane exampleTb;

		ExamplePanel() {
			setupUI();
		}
		
		private void setupUI() {
			setLayout(new BorderLayout());
			JPanel p0 = new JPanel(new BorderLayout(4, 0));
			JPanel p1 = new JPanel(new GridLayout(2, 2, 4, 4));
			JPanel p2 = new JPanel(new BorderLayout(4, 4));
			
			scrollBars = new Component[7];
			// Scrollables
			SizedPanel sizey = new SizedPanel(70, 130);
			JScrollPane sp = new JScrollPane(sizey,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			sp.setPreferredSize(new Dimension(96, 96));
			sp.getVerticalScrollBar().setUnitIncrement(4);
			sp.getHorizontalScrollBar().setUnitIncrement(4);
			p1.add(sp);
			scrollBars[0] = sp;

			sizey = new SizedPanel(130, 130);
			sp = new JScrollPane(sizey,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			sp.setPreferredSize(new Dimension(96, 96));
			sp.getVerticalScrollBar().setUnitIncrement(4);
			sp.getHorizontalScrollBar().setUnitIncrement(4);
			p1.add(sp);
			scrollBars[1] = sp;
			
			// List
			JList list = createList();
			list.setSelectedIndex(1);
			list.setVisibleRowCount(4);
			sp = new JScrollPane(list,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			p1.add(sp);
			scrollBars[2] = sp;
			
			// TextAreas
			JPanel p5 = new JPanel(new GridLayout(3, 1));
			JTextArea ta = new JTextArea("  JTextArea\n  enabled");
			p5.add(ta);
			
			ta = new JTextArea("  JTextArea\n  non-editable");
			ta.setEditable(false);
			p5.add(ta);
			
			ta = new JTextArea("  JTextArea\n  disabled");
			ta.setEnabled(false);
			p5.add(ta);
			
			sp = new JScrollPane(p5,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			p1.add(sp);
			scrollBars[3] = sp;
					
			p2.add(p1, BorderLayout.CENTER);
			
			// TextPane
			JTextPane textPane = new JTextPane(createStyledDocument());
			textPane.setEditable(false);
			p5 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 3));
			p5.add(textPane);
			
			p2.add(p5, BorderLayout.SOUTH);
			
			p0.add(p2, BorderLayout.WEST);
			
			// Buttons
			p1 = new JPanel(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.anchor = GridBagConstraints.WEST;
			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets = new Insets(0, 2, 4, 2);

			exampleButton = new JButton("JButton");
//			exampleButton.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					TableHeaderUI headerUI = exampleTable.getTableHeader().getUI();
//					
//					if(headerUI instanceof TinyTableHeaderUI) {
//						// remove sorting
//						((TinyTableHeaderUI)headerUI).sortColumns(
//							new int[] {},
//							new int[] {},
//							exampleTable);
//					}
//				}
//			});
			exampleButton.setToolTipText("Enabled JButton");
			buttons[0] = exampleButton;
			p1.add(exampleButton, gc);
			gc.gridx ++;
			exampleDisabledButton = new JButton("Disabled");
			exampleDisabledButton.setToolTipText("Disabled JButton");
			buttons[1] = exampleDisabledButton;
			exampleDisabledButton.setEnabled(false);
			p1.add(exampleDisabledButton, gc);
			
			// ToggleButton
			gc.gridx = 0;
			gc.gridy ++;
			exampleToggleButton = new JToggleButton("JToggleButton");
			buttons[2] = exampleToggleButton;
			p1.add(exampleToggleButton, gc);
			gc.gridx ++;
			JCheckBox ch = new JCheckBox("Buttons w/icon", false);
			buttons[3] = ch;
			ch.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addButtonIcons(((AbstractButton)e.getSource()).isSelected());
				}
			});
			p1.add(ch, gc);

			// CheckBox
			gc.gridx = 0;
			gc.gridy ++;
			gc.insets = new Insets(0, 2, 0, 2);
			ch = new JCheckBox("JCheckBox", false);
			buttons[4] = ch;
			p1.add(ch, gc);
			gc.gridx ++;
			ch = new JCheckBox("Disabled", true);
			buttons[5] = ch;
			ch.setEnabled(false);
			p1.add(ch, gc);
			
			// Radio
			gc.gridx = 0;
			gc.gridy ++;
			JRadioButton rb = new JRadioButton("JRadioButton");
			buttons[6] = rb;
			p1.add(rb, gc);
			gc.gridx ++;
			rb = new JRadioButton("Disabled", true);
			buttons[7] = rb;
			rb.setEnabled(false);
			p1.add(rb, gc);
			
			// Separators
			gc.fill = gc.HORIZONTAL;
			gc.gridx = 0;
			gc.gridy ++;
			gc.gridwidth = 2;
			gc.insets = new Insets(4, 2, 4, 2);
			p1.add(new JSeparator(), gc);
			gc.gridwidth = 1;
			gc.fill = gc.NONE;
			
			// Combos
			combos = new Component[4];
			gc.gridx = 0;
			gc.gridy ++;
			gc.insets = new Insets(0, 2, 4, 2);
			JComboBox cb = createCombo("JComboBox");
			combos[0] = cb;
			p1.add(cb, gc);
			gc.gridx ++;
			cb = createCombo("Disabled Combo");
			combos[1] = cb;
			cb.setEnabled(false);
			p1.add(cb, gc);
			
			gc.gridx = 0;
			gc.gridy ++;
			gc.insets = new Insets(0, 2, 1, 2);
			cb = createCombo("Editable JComboBox");
			combos[2] = cb;
			cb.setEditable(true);
			p1.add(cb, gc);
			gc.gridx ++;
			cb = createCombo("Disabled Editable");
			combos[3] = cb;
			cb.setEditable(true);
			cb.setEnabled(false);
			p1.add(cb, gc);
			
			// Separators
			gc.fill = gc.HORIZONTAL;
			gc.gridx = 0;
			gc.gridy ++;
			gc.gridwidth = 2;
			gc.insets = new Insets(4, 2, 4, 2);
			p1.add(new JSeparator(), gc);
			gc.gridwidth = 1;
			gc.fill = gc.NONE;
			
			// Text
			gc.gridx = 0;
			gc.gridy ++;
			gc.insets = new Insets(0, 2, 4, 2);
			JTextField tf = new JTextField("JTextField");
			p1.add(tf, gc);
			gc.gridx ++;
			tf = new JTextField("Disabled");
			tf.setEnabled(false);
			p1.add(tf, gc);
			gc.gridx = 0;
			
			gc.gridy ++;
			tf = new JTextField("Non-editable Textfield");
			tf.setEditable(false);
			p1.add(tf, gc);
			gc.gridx ++;
			tf = new JTextField("Disabled non-editable");
			tf.setEditable(false);
			tf.setEnabled(false);
			p1.add(tf, gc);
			gc.gridx = 0;
			
			gc.gridy ++;
			tf = new JFormattedTextField("JFormattedTextField");
			p1.add(tf, gc);
			gc.gridx ++;
			tf = new JFormattedTextField("Disabled");
			tf.setEditable(false);
			tf.setEnabled(false);
			p1.add(tf, gc);
			gc.gridx = 0;
			
			gc.gridy ++;
			gc.insets = new Insets(0, 2, 1, 2);
			tf = new JPasswordField("JPasswordField");
			p1.add(tf, gc);
			gc.gridx ++;
			tf = new JPasswordField("Disabled");
			tf.setEnabled(false);
			p1.add(tf, gc);
			
			// Separators
			gc.fill = gc.HORIZONTAL;
			gc.gridx = 0;
			gc.gridy ++;
			gc.gridwidth = 2;
			gc.insets = new Insets(4, 2, 4, 2);
			p1.add(new JSeparator(), gc);
			gc.gridwidth = 1;
			gc.fill = gc.NONE;
			
			// Spinners
			spinners = new Component[4];
			gc.gridx = 0;
			gc.gridy ++;
			gc.insets = new Insets(0, 2, 2, 2);
			p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
			JSpinner spinner = new JSpinner(new SpinnerNumberModel(42, 0, 99, 1));
			spinners[0] = spinner;
			p2.add(spinner);
			p2.add(new JLabel(" "));
			spinner = new JSpinner(new SpinnerDateModel());
			spinners[1] = spinner;
			p2.add(spinner);
			p1.add(p2, gc);
			
			gc.gridx ++;
			p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
			spinner = new JSpinner(new SpinnerNumberModel(42, 0, 42, 1));
			spinners[2] = spinner;
			spinner.setEnabled(false);
			p2.add(spinner);
			p2.add(new JLabel(" "));
			spinner = new JSpinner(new SpinnerDateModel(
				new Date(), null, null, Calendar.DAY_OF_WEEK));
			spinners[3] = spinner;
			spinner.setEnabled(false);
			p2.add(spinner);
			p1.add(p2, gc);
			p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
			p2.add(p1);
			
			p0.add(p2, BorderLayout.CENTER);
			
			// Tree
			JPanel p3 = new JPanel(new BorderLayout());
			p2 = new JPanel(new GridLayout(1, 2));
			tree1 = new JTree();
			tree1.setCellRenderer(new SwitchTreeIcons(true));
			tree1.setEditable(true);
			tree1.expandPath(tree1.getNextMatch("colors", 0, Position.Bias.Forward));
			tree1.expandPath(tree1.getNextMatch("food", 0, Position.Bias.Forward));
			tree1.setVisibleRowCount(10);
			sp1 = new JScrollPane(tree1,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			sp1.setViewportBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
			p2.add(sp1);
			scrollBars[4] = sp1;
			
			tree2 = new JTree();
			tree2.setCellRenderer(new SwitchTreeIcons(true));
			DefaultMutableTreeNode root = (DefaultMutableTreeNode)tree2.getModel().getRoot();
			root.setUserObject("JTree disabled");
			tree2.getModel().valueForPathChanged(new TreePath(root.getPath()), "JTree disabled");
			tree2.expandPath(tree2.getNextMatch("sports", 0, Position.Bias.Forward));
			tree2.setEnabled(false);
			tree2.setVisibleRowCount(10);
			sp2 = new JScrollPane(tree2,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			sp2.setViewportBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
			p2.add(sp2);
			scrollBars[5] = sp2;
			
			JPanel p4 = new JPanel(new BorderLayout(3, 0));
			p4.add(p2, BorderLayout.CENTER);
			
			// Popup trigger
			p5 = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 3));
			JCheckBox check = new JCheckBox("Show Tree Icons", true);
			check.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SwitchTreeIcons renderer =
						(SwitchTreeIcons)tree1.getCellRenderer();
					renderer.setShowIcons(((AbstractButton)e.getSource()).isSelected());
					
					renderer =
						(SwitchTreeIcons)tree2.getCellRenderer();
					renderer.setShowIcons(((AbstractButton)e.getSource()).isSelected());
					
					tree1.revalidate();
					tree2.revalidate();
					repaint();
				}			
			});
			p5.add(check);
			
			trigger = new PopupTrigger();
			p5.add(trigger);
			p4.add(p5, BorderLayout.NORTH);	
			
			// Separators
			p4.add(new JSeparator(SwingConstants.VERTICAL), BorderLayout.WEST);			
			
			// EditorPane
			URL page = getClass().getResource(
				"/de/muntjak/tinylookandfeel/html/default.html");
			JEditorPane editorPane = null;
			try {
				editorPane = new JEditorPane(page);
				editorPane.setEditable(false);
				editorPane.setPreferredSize(new Dimension(150, 70));
			} catch (IOException e) {
				editorPane = new JEditorPane("text", "Plain Document");
			}

			p5 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 3));
			p5.add(editorPane);
			
			p4.add(p5, BorderLayout.SOUTH);	
			
			p3.add(p4, BorderLayout.CENTER);
			
			// Progressbar/Slider vert
			p1 = new JPanel(new BorderLayout(8, 0));
			p2 = new JPanel(new GridBagLayout());
			gc.anchor = GridBagConstraints.WEST;
			gc.fill = GridBagConstraints.VERTICAL;
			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets = new Insets(0, 12, 0, 2);
			
			vertProgressBar = new JProgressBar(JProgressBar.VERTICAL, 0, 20);
			vertProgressBar.setValue(0);
			vertProgressBar.setStringPainted(true);
			vertProgressBar.addMouseListener(new ProgressBarAction());
			vertProgressBar.setToolTipText("Click to start/stop");
			p2.add(vertProgressBar, gc);
			gc.gridx ++;
			
			vertSlider = new JSlider(JSlider.VERTICAL, 0, 100, 40);
			vertSlider.setMajorTickSpacing(20);
			vertSlider.setMinorTickSpacing(10);
			vertSlider.setPaintTicks(true);
			vertSlider.setPaintLabels(true);
			Dimension d = vertSlider.getPreferredSize();
			d.height = 183;
			vertSlider.setPreferredSize(d);
			p2.add(vertSlider, gc);
			p1.add(p2, BorderLayout.WEST);
			
			// Progressbar/Slider horz
			p2 = new JPanel(new GridBagLayout());
			gc.anchor = GridBagConstraints.WEST;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets = new Insets(2, 0, 2, 0);
			
			horzProgressBar = new JProgressBar(0, 20);
			horzProgressBar.setValue(0);
			horzProgressBar.setStringPainted(true);
			horzProgressBar.addMouseListener(new ProgressBarAction());
			horzProgressBar.setToolTipText("Click to start/stop");
			p2.add(horzProgressBar, gc);
			gc.gridy ++;
			
			horzSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 40);
			d = horzSlider.getPreferredSize();
			horzSlider.setPreferredSize(d);
			d.width = 183;
			p2.add(horzSlider, gc);
			p1.add(p2, BorderLayout.NORTH);
			
			// OptionPane Buttons			
			p2 = new JPanel(new GridBagLayout());
			gc = new GridBagConstraints();
			gc.anchor = GridBagConstraints.WEST;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets = new Insets(0, 2, 0, 0);
			
			check = new JCheckBox("stringPainted", true);
			buttons[22] = check;
			check.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					vertProgressBar.setStringPainted(
						((AbstractButton)e.getSource()).isSelected());
					horzProgressBar.setStringPainted(
						((AbstractButton)e.getSource()).isSelected());
				}
			});
			p2.add(check, gc);
			gc.gridy ++;
			
			check = new JCheckBox("Sliders Enabled", true);
			buttons[23] = check;
			check.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(horzSlider.isEnabled()) {
						horzSlider.setEnabled(false);
						vertSlider.setEnabled(false);
					}
					else {
						horzSlider.setEnabled(true);
						vertSlider.setEnabled(true);
					}
				}
			});
			p2.add(check, gc);
			gc.gridy ++;
			
			// dialog buttons
			gc.insets = new Insets(6, 2, 0, 0);
			JButton b = new JButton("MessageDialog");
			buttons[8] = b;
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showMessageDialog();
				}
			});
			p2.add(b, gc);
			gc.gridy ++;
			b = new JButton("ConfirmDialog");
			buttons[9] = b;
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showConfirmationDialog();
				}
			});
			p2.add(b, gc);
			gc.gridy ++;
			b = new JButton("WarningDialog");
			buttons[10] = b;
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showWarningDialog();
				}
			});
			p2.add(b, gc);
			gc.gridy ++;
			b = new JButton("ErrorDialog");
			buttons[11] = b;
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showErrorDialog();
				}
			});
			p2.add(b, gc);
			p1.add(p2, BorderLayout.CENTER);
			
			p3.add(p1, BorderLayout.EAST);

			p0.add(p3, BorderLayout.EAST);
			
			p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
			p2.setBorder(new EtchedBorder());
			p2.add(p0);			
			add(p2, BorderLayout.NORTH);
			
			desktopPane = new DesktopPane();
			add(desktopPane, BorderLayout.CENTER);
		}
		
		private JList createList() {
			String[] items = new String[7];
			items[0] = "A JList";
			items[1] = "can have";
			items[2] = "zero to";
			items[3] = "many items";
			items[4] = "and can be";
			items[5] = "scrolled";
			items[6] = "(or not)";
			
			return new JList(items);
		}
		
		private JComboBox createCombo(String s) {
			return new JComboBox(new String[] {
				s, "can have", "zero to", "many items",
				"and can be", "triggered", "many times"
			});
		}
		
		public void update(boolean forceUpdate) {
			updateTheme();
			
			if(forceUpdate) {
				updateThemeButton.setEnabled(true);
			}
			
			repaint(0);
		}
		
		public void paint(Graphics g) {
			super.paint(g);
		}
		
		class SwitchTreeIcons extends DefaultTreeCellRenderer {
			
			private boolean showIcons;
			
			SwitchTreeIcons(boolean showIcons) {
				this.showIcons = showIcons;
			}
			
			void setShowIcons(boolean b) {
				showIcons = b;
			}
			
			public Icon getClosedIcon() {
				if(showIcons) {
					return super.getClosedIcon();
				}
				
				return null;
			}
			
			public Icon getOpenIcon() {
				if(showIcons) {
					return super.getOpenIcon();
				}
				
				return null;
			}
			
			public Icon getLeafIcon() {
				if(showIcons) {
					return super.getLeafIcon();
				}
				
				return null;
			}
			
			public Icon getDisabledIcon() {
				if(showIcons) {
					return super.getDisabledIcon();
				}
				
				return null;
			}
		}
		
		class ContentLabel extends JLabel {
			
			ContentLabel() {
				super("Content");
				setOpaque(true);
				setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));
				setBackground(new Color(211, 225, 255));
			}
		}
		
		class DesktopPane extends JDesktopPane {
			
			Dimension preferredSize = new Dimension(780, 160);
			
			DesktopPane() {
				setupUI();
			}
			
			public Dimension getPreferredSize() {
				return preferredSize;
			}
			
			private void setupUI() {
				JPanel p0 = new JPanel();
				p0.setBounds(0, 0, preferredSize.width, preferredSize.height);
				
				// Table
				exampleTable = new JTable(new TinyTableModel());
				exampleTable.setRowSelectionAllowed(true);
				exampleTable.setColumnSelectionAllowed(true);
				exampleTable.setColumnSelectionInterval(2, 2);
				exampleTable.setRowSelectionInterval(0, 2);
				exampleTable.setDefaultRenderer(
					TinyTableModel.TableColorIcon.class, new IconRenderer());

				JScrollPane sp = new JScrollPane(exampleTable);
				sp.setBounds(10, 10, 192, 132);
				add(sp, JDesktopPane.DEFAULT_LAYER);
				scrollBars[6] = sp;

				// Disabled TabbedPane
				exampleTb = new JTabbedPane();
				exampleTb.add("Disabled", new ContentLabel());
				exampleTb.add("Tabbed", new ContentLabel());
				exampleTb.add("Pane", new ContentLabel());
				exampleTb.setEnabled(false);
				exampleTb.setPreferredSize(new Dimension(180, 60));
				exampleTb.setBounds(210, 40, 180, 60);
				add(exampleTb, JDesktopPane.DEFAULT_LAYER);
				
				// Internal Frame
				frames = new Component[2];
				internalFrame = new JInternalFrame("InternalFrame", true, true, true, true);
				frames[0] = internalFrame;
				internalFrame.updateUI();
				internalFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			
				internalFrame.addInternalFrameListener(new InternalFrameAdapter() {				
					public void internalFrameClosing(InternalFrameEvent e) {
						String msg = "This internal frame cannot be closed.";
						JOptionPane.showInternalMessageDialog(
							internalFrame, msg);
						e.getInternalFrame().show();
					}
				});
				internalFrame.getContentPane().add(new SizedPanel(200, 100));
				internalFrame.pack();
				Dimension frameSize = internalFrame.getPreferredSize();
				internalFrame.setBounds(400, 10, frameSize.width, frameSize.height);
				internalFrame.show();
				add(internalFrame, JDesktopPane.PALETTE_LAYER);
			
				// Palette
				palette = new JInternalFrame("Palette", false, true, true, true);
				frames[1] = palette;
				palette.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
				palette.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			
				palette.addInternalFrameListener(new InternalFrameAdapter() {				
					public void internalFrameClosing(InternalFrameEvent e) {
						String msg = "This internal palette cannot be closed.";
						JOptionPane.showInternalMessageDialog(
							palette, msg);
						e.getInternalFrame().show();
					}
				});
				
				JPanel p2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
				JButton defaultButton = new JButton("InternalMessageDialog");
				buttons[12] = defaultButton;
				defaultButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JOptionPane.showInternalMessageDialog(
							palette, "Life is a while(true) loop.");
					}
				});
				palette.getRootPane().setDefaultButton(defaultButton);
				
				JButton b = new JButton("InternalConfirmDialog ");
				buttons[13] = b;
				b.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {						
						JOptionPane.showInternalConfirmDialog(
							palette, "Is programming art?");
					}
				});
				
				JPanel p3 = new JPanel(new GridLayout(4, 1));
				p3.add(new JLabel());
				p3.add(defaultButton);
				p3.add(new JLabel());
				p3.add(b);
				p2.add(p3);
				palette.getContentPane().add(p2);
				palette.setBounds(400 + internalFrame.getWidth() + 12, 10, 180, 140);
				palette.show();
				add(palette, JDesktopPane.PALETTE_LAYER);
			}
		}
	}
	
	class ScrollBarCP extends JPanel {
		
		private JCheckBox rolloverEnabled;
		private boolean inited = false;
		
		ScrollBarCP() {
			setupUI();
		}
		
		private void setupUI() {
			setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
			JPanel p1 = new JPanel(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.anchor = GridBagConstraints.NORTHWEST;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets = new Insets(2, 4, 0, 4);
			
			// Thumb
			p1.add(new JLabel("Thumb Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			scrollThumbField = new SBField(Theme.scrollThumbColor);
			p1.add(scrollThumbField, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Rollover Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			scrollThumbRolloverBg = new SBField(Theme.scrollThumbRolloverColor);
			p1.add(scrollThumbRolloverBg, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Presssed Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			scrollThumbPressedBg = new SBField(Theme.scrollThumbPressedColor);
			p1.add(scrollThumbPressedBg, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Disabled Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			scrollThumbDisabledBg = new SBField(Theme.scrollThumbDisabledColor);
			p1.add(scrollThumbDisabledBg, gc);
			
			// Grip
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			JLabel l = new JLabel("<html>Grip Dark Color <b>*");
			l.setIconTextGap(2);
			l.setHorizontalTextPosition(JLabel.LEADING);
			l.setVerticalTextPosition(JLabel.TOP);
			p1.add(l, gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			scrollGripDark = new SBField(Theme.scrollGripDarkColor);
			p1.add(scrollGripDark, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			l = new JLabel("<html>Grip Light Col <b>*");
			l.setIconTextGap(2);
			l.setHorizontalTextPosition(JLabel.LEADING);
			l.setVerticalTextPosition(JLabel.TOP);
			p1.add(l, gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			scrollGripLight = new SBField(Theme.scrollGripLightColor);
			p1.add(scrollGripLight, gc);
			
			gc.gridy ++;
			gc.gridheight = 3;
			gc.insets = new Insets(6, 8, 0, 4);
			l = new JLabel("<html><b>*</b> Only saturation<br>" +
				"and lightness<br>are considered.");
			l.setVerticalTextPosition(JLabel.TOP);
			l.setBackground(infoColor);
			l.setForeground(Color.BLACK);
			l.setOpaque(true);
			l.setIconTextGap(2);
			l.setBorder(infoBorder);
			p1.add(l, gc);
			
			// Button
			gc.gridx ++;
			gc.gridy = 0;
			gc.gridheight = 1;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Button Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			scrollButtField = new SBField(Theme.scrollButtColor);
			p1.add(scrollButtField, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Rollover Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			scrollButtRolloverBg = new SBField(Theme.scrollButtRolloverColor);
			p1.add(scrollButtRolloverBg, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Presssed Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			scrollButtPressedBg = new SBField(Theme.scrollButtPressedColor);
			p1.add(scrollButtPressedBg, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			scrollButtDisabledBg = new SBField(Theme.scrollButtDisabledColor);
			p1.add(scrollButtDisabledBg, gc);
			
			// Spread
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Spread Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			scrollSpreadLight = new SpreadControl(
				Theme.scrollSpreadLight, 20, CONTROLS_SCROLLBAR);
			p1.add(scrollSpreadLight, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Spread Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			scrollSpreadDark = new SpreadControl(
				Theme.scrollSpreadDark, 20, CONTROLS_SCROLLBAR);
			p1.add(scrollSpreadDark, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled S. Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			scrollSpreadLightDisabled = new SpreadControl(
				Theme.scrollSpreadLightDisabled, 20, CONTROLS_SCROLLBAR);
			p1.add(scrollSpreadLightDisabled, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled S. Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			scrollSpreadDarkDisabled = new SpreadControl(
				Theme.scrollSpreadDarkDisabled, 20, CONTROLS_SCROLLBAR);
			p1.add(scrollSpreadDarkDisabled, gc);
			
			// Border
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Border Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			scrollBorder = new SBField(Theme.scrollBorderColor);
			p1.add(scrollBorder, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Border Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			scrollDark = new SBField(Theme.scrollDarkColor, false, true);
			p1.add(scrollDark, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Border Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			scrollLight = new SBField(Theme.scrollLightColor);
			p1.add(scrollLight, gc);
			
			gc.gridy = 7;
			gc.insets = new Insets(0, 8, 0, 4);
			rolloverEnabled = new JCheckBox("Paint Rollover", false);
			rolloverEnabled.addActionListener(checkAction);
			p1.add(rolloverEnabled, gc);

			// Border disabled
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Disabled Border"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			scrollBorderDisabled = new SBField(Theme.scrollBorderDisabledColor);
			p1.add(scrollBorderDisabled, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			scrollDarkDisabled = new SBField(Theme.scrollDarkDisabledColor, false, true);
			p1.add(scrollDarkDisabled, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			scrollLightDisabled = new SBField(Theme.scrollLightDisabledColor);
			p1.add(scrollLightDisabled, gc);

			// Track
			gc.gridx ++;
			gc.gridy = 0;
			gc.gridheight = 1;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Track Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			trackField = new SBField(Theme.scrollTrackColor);
			p1.add(trackField, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Track Disabled"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			trackDisabled = new SBField(Theme.scrollTrackDisabledColor);
			p1.add(trackDisabled, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Track Border"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			trackBorder = new SBField(Theme.scrollTrackBorderColor);
			p1.add(trackBorder, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Track Border Disabled"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			trackBorderDisabled = new SBField(Theme.scrollTrackBorderDisabledColor);
			p1.add(trackBorderDisabled, gc);
			
			// Arrow
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Arrow Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			scrollArrowField = new SBField(Theme.scrollArrowColor);
			p1.add(scrollArrowField, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Arrow Disabled Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			scrollArrowDisabled = new SBField(Theme.scrollArrowDisabledColor);
			p1.add(scrollArrowDisabled, gc);
			gc.gridy += 3;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("ScrollPane Border Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			scrollPane = new SBField(Theme.scrollPaneBorderColor);
			p1.add(scrollPane, gc);
			
			add(p1);
		}
		
		void init(boolean always) {
			if(inited && !always) return;

			rolloverEnabled.setSelected(Theme.scrollRollover[Theme.style]);
			
			scrollThumbField.update();
			scrollButtField.update();
			scrollArrowField.update();
			trackField.update();
			scrollThumbRolloverBg.update();
			scrollThumbPressedBg.update();
			scrollThumbDisabledBg.update();
			trackBorder.update();
			scrollButtRolloverBg.update();
			scrollButtPressedBg.update();
			scrollButtDisabledBg.update();
			trackDisabled.update();
			trackBorderDisabled.update();
			scrollArrowDisabled.update();
			scrollGripDark.update();
			scrollGripLight.update();
			scrollBorder.update();
			scrollDark.update();
			scrollLight.update();
			scrollBorderDisabled.update();
			scrollDarkDisabled.update();
			scrollLightDisabled.update();
			scrollPane.update();
			scrollSpreadDark.init();
			scrollSpreadLight.init();
			scrollSpreadDarkDisabled.init();
			scrollSpreadLightDisabled.init();

			inited = true;
		}
		
		void updateTheme() {	
			if(!inited || resistUpdate) return;
			
			Theme.scrollRollover[Theme.style] = rolloverEnabled.isSelected();
		}
	}
	
	class SliderCP extends JPanel {
		
		private JCheckBox rolloverEnabled;
		private JCheckBox focusEnabled;
		private boolean inited = false;
		
		SliderCP() {
			setupUI();
		}
		
		private void setupUI() {
			setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
			JPanel p1 = new JPanel(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.anchor = GridBagConstraints.NORTHWEST;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets = new Insets(2, 4, 0, 4);
			
			// Thumb
			p1.add(new JLabel("Thumb Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			sliderThumbField = new SBField(Theme.sliderThumbColor);
			p1.add(sliderThumbField, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Rollover Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			sliderThumbRolloverBg = new SBField(Theme.sliderThumbRolloverColor);
			p1.add(sliderThumbRolloverBg, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Presssed Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			sliderThumbPressedBg = new SBField(Theme.sliderThumbPressedColor);
			p1.add(sliderThumbPressedBg, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Disabled Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			sliderThumbDisabledBg = new SBField(Theme.sliderThumbDisabledColor);
			p1.add(sliderThumbDisabledBg, gc);
			
			// border
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Border Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			sliderBorder = new SBField(Theme.sliderBorderColor);
			p1.add(sliderBorder, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Dark Border"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			sliderDark = new SBField(Theme.sliderDarkColor);
			p1.add(sliderDark, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Light Border"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			sliderLight = new SBField(Theme.sliderLightColor);
			p1.add(sliderLight, gc);
			
			// disabled border
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Disabled Border"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			sliderDisabledBorder = new SBField(Theme.sliderBorderDisabledColor);
			p1.add(sliderDisabledBorder, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Dark Disabled"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			sliderDisabledDark = new SBField(Theme.sliderDarkDisabledColor);
			p1.add(sliderDisabledDark, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Light Disabled"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			sliderDisabledLight = new SBField(Theme.sliderLightDisabledColor);
			p1.add(sliderDisabledLight, gc);
			gc.gridy += 2;
			
			gc.insets = new Insets(0, 8, 0, 4);
			rolloverEnabled = new JCheckBox("Paint Rollover", true);
			rolloverEnabled.addActionListener(checkAction);
			p1.add(rolloverEnabled, gc);
			
			// Track
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Track Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			sliderTrack = new SBField(Theme.sliderTrackColor);
			p1.add(sliderTrack, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Track Border Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			sliderTrackBorder = new SBField(Theme.sliderTrackBorderColor);
			p1.add(sliderTrackBorder, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Track Border Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			sliderTrackDark = new SBField(Theme.sliderTrackDarkColor);
			p1.add(sliderTrackDark, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Track Border Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			sliderTrackLight = new SBField(Theme.sliderTrackLightColor);
			p1.add(sliderTrackLight, gc);
			
			// Ticks
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Ticks Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			sliderTick = new SBField(Theme.sliderTickColor);
			p1.add(sliderTick, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Ticks Disabled Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			sliderTickDisabled = new SBField(Theme.sliderTickDisabledColor);
			p1.add(sliderTickDisabled, gc);
			
			// Focus
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Focus Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			sliderFocusColor = new SBField(Theme.sliderFocusColor);
			p1.add(sliderFocusColor, gc);
			
			gc.gridy = 3;
			gc.insets = new Insets(1, 8, 0, 4);
			focusEnabled = new JCheckBox("Paint Focus", true);
			focusEnabled.addActionListener(checkAction);
			p1.add(focusEnabled, gc);
			
			add(p1);
		}
		
		void init(boolean always) {
			if(inited && !always) return;

			rolloverEnabled.setSelected(Theme.sliderRolloverEnabled[Theme.style]);
			focusEnabled.setSelected(Theme.sliderFocusEnabled[Theme.style]);
			
			sliderThumbField.update();
			sliderThumbRolloverBg.update();
			sliderThumbPressedBg.update();
			sliderThumbDisabledBg.update();
			sliderBorder.update();
			sliderDark.update();
			sliderLight.update();
			sliderDisabledBorder.update();
			sliderDisabledDark.update();
			sliderDisabledLight.update();
			sliderTrack.update();
			sliderTrackBorder.update();
			sliderTrackDark.update();
			sliderTrackLight.update();
			sliderTick.update();
			sliderTickDisabled.update();
			sliderFocusColor.update();

			inited = true;
		}
		
		void updateTheme() {	
			if(!inited || resistUpdate) return;

			Theme.sliderRolloverEnabled[Theme.style] = rolloverEnabled.isSelected();
			Theme.sliderFocusEnabled[Theme.style] = focusEnabled.isSelected();
		}
	}
	
	class ToolBarCP extends JPanel {
		
		private JCheckBox focusEnabled;
		private JSpinner mTop, mLeft, mBottom, mRight;
		private boolean inited = false;
		
		ToolBarCP() {
			setupUI();
		}
		
		private void setupUI() {
			setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
			JPanel p1 = new JPanel(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.anchor = GridBagConstraints.NORTHWEST;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets = new Insets(2, 4, 0, 4);
			
			// ToolBar
			p1.add(new JLabel("ToolBar Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			toolBar = new SBField(Theme.toolBarColor, true);
			p1.add(toolBar, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("ToolBar Light Border"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			toolBarLight = new SBField(Theme.toolBarLightColor, true);
			p1.add(toolBarLight, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("ToolBar Dark Border"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			toolBarDark = new SBField(Theme.toolBarDarkColor, true);
			p1.add(toolBarDark, gc);
			
			// Button
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Button Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			toolButt = new SBField(Theme.toolButtColor, true);
			p1.add(toolButt, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Button Rollover Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			toolButtRollover = new SBField(Theme.toolButtRolloverColor);
			p1.add(toolButtRollover, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Button Pressed Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			toolButtPressed = new SBField(Theme.toolButtPressedColor);
			p1.add(toolButtPressed, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Button Selected Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			toolButtSelected = new SBField(Theme.toolButtSelectedColor, true);
			p1.add(toolButtSelected, gc);
			
			// YQ Button Border
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Button Border Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			toolBorder = new SBField(Theme.toolBorderColor, true);
			p1.add(toolBorder, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Border Rollover Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			toolBorderRollover = new SBField(Theme.toolBorderRolloverColor);
			p1.add(toolBorderRollover, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Border Pressed Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			toolBorderPressed = new SBField(Theme.toolBorderPressedColor);
			p1.add(toolBorderPressed, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Border Selected Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			toolBorderSelected = new SBField(Theme.toolBorderSelectedColor, true);
			p1.add(toolBorderSelected, gc);

			// 99 Button Border
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Border Dark Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			toolBorderDark = new SBField(Theme.toolBorderDarkColor, true, true);
			p1.add(toolBorderDark, gc);			
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Border Light Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			toolBorderLight = new SBField(Theme.toolBorderLightColor, true, true);
			p1.add(toolBorderLight, gc);
			gc.gridy += 2;
			
			gc.insets = new Insets(0, 8, 0, 4);
			gc.gridheight = 2;
			focusEnabled = new JCheckBox("Paint Focus", true);
			focusEnabled.addActionListener(checkAction);
			p1.add(focusEnabled, gc);
			
			// grip
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Grip Dark Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			toolGripDark = new SBField(Theme.toolGripDarkColor, true);
			p1.add(toolGripDark, gc);			
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Grip Light Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			toolGripLight = new SBField(Theme.toolGripLightColor, true);
			p1.add(toolGripLight, gc);
			
			// separator
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Separator Dark Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			toolSepDark = new SBField(Theme.toolSepDarkColor, true);
			p1.add(toolSepDark, gc);			
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Separator Light Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			toolSepLight = new SBField(Theme.toolSepLightColor, true, true);
			p1.add(toolSepLight, gc);
			
			// Margin
			gc.gridx ++;
			gc.gridy = 1;
			gc.gridheight = 7;
			gc.gridwidth = 1;
			gc.insets = new Insets(0, 16, 0, 4);
			
			JPanel p2 = new JPanel(new GridBagLayout());
			GridBagConstraints gc2 = new GridBagConstraints();
			
			
			gc2.anchor = GridBagConstraints.CENTER;
			gc2.fill = GridBagConstraints.NONE;
			gc2.gridwidth = 3;
			gc2.gridx = 0;
			gc2.gridy = 0;
			gc2.insets = new Insets(0, 0, 4, 0);
			p2.add(new JLabel("Button Margin"), gc2);
			
			gc2.anchor = GridBagConstraints.NORTHWEST;
			gc2.fill = GridBagConstraints.HORIZONTAL;
			gc2.gridwidth = 1;
			gc2.gridy = 2;
			gc2.insets = new Insets(0, 2, 0, 2);
			
			mLeft = new JSpinner(new SpinnerNumberModel(4, 1, 99, 1));
      		mLeft.addChangeListener(spinnerUpdateAction);
			p2.add(mLeft, gc2);
			
			gc2.gridx ++;
			gc2.gridy = 1;
			mTop = new JSpinner(new SpinnerNumberModel(4, 1, 99, 1));
      		mTop.addChangeListener(spinnerUpdateAction);
			p2.add(mTop, gc2);
			
			gc2.gridy ++;
			p2.add(new JLabel("Margin"), gc2);
			
			gc2.gridy ++;
			mBottom = new JSpinner(new SpinnerNumberModel(4, 1, 99, 1));
      		mBottom.addChangeListener(spinnerUpdateAction);
			p2.add(mBottom, gc2);
			
			gc2.gridx ++;
			gc2.gridy = 2;
			mRight = new JSpinner(new SpinnerNumberModel(4, 1, 99, 1));
      		mRight.addChangeListener(spinnerUpdateAction);
			p2.add(mRight, gc2);
			
			p1.add(p2, gc);
			
			add(p1);
		}
		
		void init(boolean always) {
			if(inited && !always) return;

			focusEnabled.setSelected(Theme.toolFocus[Theme.style]);
			
			toolBar.update();
			toolBarDark.update();
			toolBarLight.update();
			toolButt.update();
			toolButtRollover.update();
			toolButtPressed.update();
			toolButtSelected.update();
			toolBorder.update();
			toolBorderRollover.update();
			toolBorderPressed.update();
			toolBorderSelected.update();
			toolBorderDark.update();
			toolBorderLight.update();
			toolGripDark.update();
			toolGripLight.update();
			toolSepDark.update();
			toolSepLight.update();
			
			mTop.setValue(new Integer(Theme.toolMarginTop[Theme.style]));
    		mLeft.setValue(new Integer(Theme.toolMarginLeft[Theme.style]));
    		mBottom.setValue(new Integer(Theme.toolMarginBottom[Theme.style]));
    		mRight.setValue(new Integer(Theme.toolMarginRight[Theme.style]));

			inited = true;
		}
		
		void updateTheme() {
			if(!inited || resistUpdate) return;

    		Theme.toolFocus[Theme.style] = focusEnabled.isSelected();
    		
    		Theme.toolMarginTop[Theme.style] = ((Integer)mTop.getValue()).intValue();
    		Theme.toolMarginLeft[Theme.style] = ((Integer)mLeft.getValue()).intValue();
    		Theme.toolMarginBottom[Theme.style] = ((Integer)mBottom.getValue()).intValue();
    		Theme.toolMarginRight[Theme.style] = ((Integer)mRight.getValue()).intValue();
		}
	}
	
	class TableCP extends JPanel {
		
		private JCheckBox focusEnabled;
		private boolean inited = false;
		
		TableCP() {
			setupUI();
		}
		
		private void setupUI() {
			setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
			JPanel p1 = new JPanel(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.anchor = GridBagConstraints.NORTHWEST;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets = new Insets(2, 4, 0, 4);
			
			// Back
			p1.add(new JLabel("Background Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			tableBack = new SBField(Theme.tableBackColor, true);
			p1.add(tableBack, gc);
			gc.gridy ++;

			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Grid Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			tableGrid = new SBField(Theme.tableGridColor, true);
			p1.add(tableGrid, gc);
			
			// Border
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Border Dark Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			tableBorderDark = new SBField(Theme.tableBorderDarkColor);
			p1.add(tableBorderDark, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Border Light Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			tableBorderLight = new SBField(Theme.tableBorderLightColor);
			p1.add(tableBorderLight, gc);
			
			// Header Colors
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Header Background"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			tableHeaderBack = new SBField(Theme.tableHeaderBackColor, true);
			p1.add(tableHeaderBack, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("<html>H. Rollover Background <b>*</b>"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			tableHeaderRolloverBack = new SBField(Theme.tableHeaderRolloverBackColor);
			p1.add(tableHeaderRolloverBack, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("<html>Header Rollover Color <b>*</b>"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			tableHeaderRollover = new SBField(Theme.tableHeaderRolloverColor, true);
			p1.add(tableHeaderRollover, gc);
			gc.gridy ++;
			
			JLabel info = new JLabel("<html>" +
				"<b>*</b> Considered only with tables implementing" +
				"<br>de.muntjak.tinylookandfeel.table.SortableTableData");
			info.setOpaque(true);
			info.setBackground(infoColor);
			info.setForeground(Color.BLACK);
			info.setBorder(infoBorder);
			gc.fill = GridBagConstraints.NONE;
			gc.gridwidth = 3;
			gc.insets = new Insets(3, 8, 0, 4);
			p1.add(info, gc);
			
			// Header Border
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.gridwidth = 1;
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Header Border Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			tableHeaderDark = new SBField(Theme.tableHeaderDarkColor, true);
			p1.add(tableHeaderDark, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Header Border Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			tableHeaderLight = new SBField(Theme.tableHeaderLightColor, true);
			p1.add(tableHeaderLight, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("<html>Header Arrow Color <b>*</b>"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			tableHeaderArrow = new SBField(Theme.tableHeaderArrowColor);
			p1.add(tableHeaderArrow, gc);
			
			// Selected
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Selected Cell Background"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			tableSelectedBack = new SBField(Theme.tableSelectedBackColor, true);
			p1.add(tableSelectedBack, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Selected Cell Foreground"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			tableSelectedFore = new SBField(Theme.tableSelectedForeColor, true);
			p1.add(tableSelectedFore, gc);
			
			// Table model radios
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 16, 0, 4);
			gc.anchor = GridBagConstraints.NORTHWEST;
			gc.gridheight = 6;
			JPanel p = new JPanel(new GridLayout(3, 1));
			p.setBorder(new TitledBorder("Not saved"));
			JCheckBox check = new JCheckBox("Sortable table model", true);
			check.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JCheckBox check = (JCheckBox)e.getSource();
					
					if(check.isSelected()) {
						exampleTable.setModel(new TinyTableModel());
						exampleTable.setColumnSelectionInterval(2, 2);
						exampleTable.setRowSelectionInterval(0, 2);
					}
					else {
						exampleTable.setModel(new NonSortableTableModel());
						exampleTable.setColumnSelectionInterval(2, 2);
						exampleTable.setRowSelectionInterval(0, 3);
					}
				}
			});
			p.add(check);

			check = new JCheckBox("Column reordering allowed", true);
			check.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JCheckBox check = (JCheckBox)e.getSource();
					exampleTable.getTableHeader().setReorderingAllowed(check.isSelected());
				}
			});
			p.add(check);

			check = new JCheckBox("Column resizing allowed", true);
			check.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JCheckBox check = (JCheckBox)e.getSource();
					exampleTable.getTableHeader().setResizingAllowed(check.isSelected());
				}
			});
			p.add(check);
			p1.add(p, gc);
			
			add(p1);
		}
		
		void init(boolean always) {
			if(inited && !always) return;

			tableBack.update();
			tableHeaderBack.update();
			tableHeaderRolloverBack.update();
			tableHeaderRollover.update();
			tableHeaderArrow.update();
			tableGrid.update();
			tableSelectedBack.update();
			tableSelectedFore.update();
			tableBorderDark.update();
			tableBorderLight.update();
			tableHeaderDark.update();
			tableHeaderLight.update();

			inited = true;
		}
		
		void updateTheme() {}
	}
	
	class SpinnerCP extends JPanel {
		
		private JCheckBox rolloverEnabled;
		private boolean inited = false;
		
		SpinnerCP() {
			setupUI();
		}
		
		private void setupUI() {
			setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
			JPanel p1 = new JPanel(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.anchor = GridBagConstraints.NORTHWEST;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets = new Insets(2, 4, 0, 4);
			
			// Button
			p1.add(new JLabel("Button Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			spinnerButtField = new SBField(Theme.spinnerButtColor);
			p1.add(spinnerButtField, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Rollover Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			spinnerButtRolloverBg = new SBField(Theme.spinnerButtRolloverColor);
			p1.add(spinnerButtRolloverBg, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Presssed Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			spinnerButtPressedBg = new SBField(Theme.spinnerButtPressedColor);
			p1.add(spinnerButtPressedBg, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Disabled Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			spinnerButtDisabledBg = new SBField(Theme.spinnerButtDisabledColor);
			p1.add(spinnerButtDisabledBg, gc);
			
			// Spread
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Spread Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			spinnerSpreadLight = new SpreadControl(
				Theme.spinnerSpreadLight, 20, CONTROLS_SPINNER);
			p1.add(spinnerSpreadLight, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Spread Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			spinnerSpreadDark = new SpreadControl(
				Theme.spinnerSpreadDark, 20, CONTROLS_SPINNER);
			p1.add(spinnerSpreadDark, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled S. Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			spinnerSpreadLightDisabled = new SpreadControl(
				Theme.spinnerSpreadLightDisabled, 20, CONTROLS_SPINNER);
			p1.add(spinnerSpreadLightDisabled, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled S. Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			spinnerSpreadDarkDisabled = new SpreadControl(
				Theme.spinnerSpreadDarkDisabled, 20, CONTROLS_SPINNER);
			p1.add(spinnerSpreadDarkDisabled, gc);
			
			// border
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Border Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			spinnerBorder = new SBField(Theme.spinnerBorderColor);
			p1.add(spinnerBorder, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Dark Border"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			spinnerDark = new SBField(Theme.spinnerDarkColor, false, true);
			p1.add(spinnerDark, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Light Border"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			spinnerLight = new SBField(Theme.spinnerLightColor, false, true);
			p1.add(spinnerLight, gc);
			
			// disabled border
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Disabled Border"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			spinnerDisabledBorder = new SBField(Theme.spinnerBorderDisabledColor);
			p1.add(spinnerDisabledBorder, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			spinnerDisabledDark = new SBField(Theme.spinnerDarkDisabledColor, false, true);
			p1.add(spinnerDisabledDark, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			spinnerDisabledLight = new SBField(Theme.spinnerLightDisabledColor, false, true);
			p1.add(spinnerDisabledLight, gc);

			// arrow
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Arrow Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			spinnerArrowField = new SBField(Theme.spinnerArrowColor);
			p1.add(spinnerArrowField, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled Arrow"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			spinnerArrowDisabled = new SBField(Theme.spinnerArrowDisabledColor);
			p1.add(spinnerArrowDisabled, gc);
			gc.gridy += 2;
			
			gc.gridheight = 2;
			gc.insets = new Insets(0, 8, 0, 4);
			rolloverEnabled = new JCheckBox("Paint Rollover Border", true);
			rolloverEnabled.addActionListener(checkAction);
			p1.add(rolloverEnabled, gc);
			
			add(p1);
		}
		
		void init(boolean always) {
			if(inited && !always) return;

			rolloverEnabled.setSelected(Theme.spinnerRollover[Theme.style]);
			
			spinnerButtField.update();
			spinnerArrowField.update();
			spinnerButtRolloverBg.update();
			spinnerButtPressedBg.update();
			spinnerButtDisabledBg.update();
			spinnerBorder.update();
			spinnerDark.update();
			spinnerLight.update();
			spinnerDisabledBorder.update();
			spinnerDisabledDark.update();
			spinnerDisabledLight.update();
			spinnerArrowDisabled.update();
			spinnerSpreadDark.init();
			spinnerSpreadLight.init();
			spinnerSpreadDarkDisabled.init();
			spinnerSpreadLightDisabled.init();

			inited = true;
		}
		
		void updateTheme() {	
			if(!inited || resistUpdate) return;
			
			Theme.spinnerRollover[Theme.style] = rolloverEnabled.isSelected();
		}
	}
	
	class MenuCP extends JPanel {
		
		private JCheckBox rolloverEnabled;
		private boolean inited = false;
		
		MenuCP() {
			setupUI();
		}
		
		private void setupUI() {	
			setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
			JPanel p1 = new JPanel(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.anchor = GridBagConstraints.NORTHWEST;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets = new Insets(2, 4, 0, 4);

			p1.add(new JLabel("MenuBar Background"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			menuBar = new SBField(Theme.menuBarColor, true);
			p1.add(menuBar, gc);
			gc.gridy ++;

			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Popup Background"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			menuPopup = new SBField(Theme.menuPopupColor);
			p1.add(menuPopup, gc);
			gc.gridy += 2;
			
			gc.insets = new Insets(0, 4, 0, 4);
			gc.gridheight = 2;
			rolloverEnabled = new JCheckBox("Paint Rollover", true);
			rolloverEnabled.addActionListener(checkAction);
			p1.add(rolloverEnabled, gc);

			gc.gridx ++;
			gc.gridy = 0;
			gc.gridheight = 1;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Popup Inner Hilight"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			menuInnerHilight = new SBField(Theme.menuInnerHilightColor);
			p1.add(menuInnerHilight, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Popup Inner Shadow"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			menuInnerShadow = new SBField(Theme.menuInnerShadowColor);
			p1.add(menuInnerShadow, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Popup Outer Hilight"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			menuOuterHilight = new SBField(Theme.menuOuterHilightColor);
			p1.add(menuOuterHilight, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Popup Outer Shadow"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			menuOuterShadow = new SBField(Theme.menuOuterShadowColor);
			p1.add(menuOuterShadow, gc);
			
			gc.gridx ++;
			gc.gridy = 0;
			gc.gridheight = 1;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Rollover Back Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			menuRolloverBg = new SBField(Theme.menuRolloverBgColor);
			p1.add(menuRolloverBg, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Rollover Fore Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			menuRolloverFg = new SBField(Theme.menuRolloverFgColor);
			p1.add(menuRolloverFg, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled Fore Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			menuDisabledFg = new SBField(Theme.menuDisabledFgColor, true);
			p1.add(menuDisabledFg, gc);
			
			// Menu border
			gc.gridx ++;
			gc.gridy = 0;
			gc.gridheight = 1;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Menu Border Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			menuBorder = new SBField(Theme.menuBorderColor);
			p1.add(menuBorder, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Menu Border Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			menuDark = new SBField(Theme.menuDarkColor, false, true);
			p1.add(menuDark, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Menu Border Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			menuLight = new SBField(Theme.menuLightColor, false, true);
			p1.add(menuLight, gc);

			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Selected Background"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			menuItemRollover = new SBField(Theme.menuItemRolloverColor);
			p1.add(menuItemRollover, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Selected Foreground"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			menuSelectedText = new SBField(Theme.menuSelectedTextColor);
			p1.add(menuSelectedText, gc);
			
			// Icon
			gc.gridx ++;
			gc.gridy = 0;
			gc.gridheight = 1;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Icon Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			menuIcon = new SBField(Theme.menuIconColor);
			p1.add(menuIcon, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Icon Rollover Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			menuIconRollover = new SBField(Theme.menuIconRolloverColor);
			p1.add(menuIconRollover, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Icon Disabled Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			menuIconDisabled = new SBField(Theme.menuIconDisabledColor);
			p1.add(menuIconDisabled, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Icon Disabled Shadow Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			menuIconShadow = new SBField(Theme.menuIconShadowColor, false, true);
			p1.add(menuIconShadow, gc);
			
			// Separator
			gc.gridx ++;
			gc.gridy = 0;
			gc.gridheight = 1;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Separator Dark Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			menuSepDark = new SBField(Theme.menuSepDarkColor);
			p1.add(menuSepDark, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Separator Light Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			menuSepLight = new SBField(Theme.menuSepLightColor, false, true);
			p1.add(menuSepLight, gc);

			add(p1);
		}
		
		void init(boolean always) {
			if(inited && !always) return;
			
			menuSelectedText.update();
			menuRolloverBg.update();
			menuRolloverFg.update();
			menuDisabledFg.update();
    		menuBar.update();
    		menuBorder.update();
    		menuDark.update();
    		menuLight.update();
    		menuPopup.update();
    		menuItemRollover.update();
    		menuInnerHilight.update();
    		menuInnerShadow.update();
    		menuOuterHilight.update();
    		menuOuterShadow.update();
    		menuIcon.update();
    		menuIconRollover.update();
    		menuIconDisabled.update();
    		menuIconShadow.update();
    		menuSepDark.update();
    		menuSepLight.update();

			rolloverEnabled.setSelected(Theme.menuRollover[Theme.style]);
					
			inited = true;
		}
		
		void updateTheme() {
			if(!inited || resistUpdate) return;
			
			Theme.menuRollover[Theme.style] = rolloverEnabled.isSelected();
		}
	}
	
	class TreeCP extends JPanel {
		
		private boolean inited = false;
		
		TreeCP() {
			setupUI();
		}
		
		private void setupUI() {
			setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
			JPanel p1 = new JPanel(new GridBagLayout());
			//p1.setBorder(new EtchedBorder());
			GridBagConstraints gc = new GridBagConstraints();
			gc.anchor = GridBagConstraints.NORTHWEST;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets = new Insets(2, 4, 0, 4);

			p1.add(new JLabel("Tree Background"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			treeBg = new SBField(Theme.treeBgColor, true);
			p1.add(treeBg, gc);
			
			gc.gridx ++;
			gc.gridy = 0;
			gc.gridheight = 1;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Text Background"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			treeTextBg = new SBField(Theme.treeTextBgColor, true);
			p1.add(treeTextBg, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Text Foreground"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			treeText = new SBField(Theme.treeTextColor, true);
			p1.add(treeText, gc);
			
			gc.gridx ++;
			gc.gridy = 0;
			
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Selected Text Bg"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			treeSelectedBg = new SBField(Theme.treeSelectedBgColor, true);
			p1.add(treeSelectedBg, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Selected Foreground"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			treeSelectedText = new SBField(Theme.treeSelectedTextColor, true);
			p1.add(treeSelectedText, gc);
			
			gc.gridx ++;
			gc.gridy = 0;
			
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Line Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			treeLine = new SBField(Theme.treeLineColor, true);
			p1.add(treeLine, gc);

			add(p1);
		}
		
		void init(boolean always) {
			if(inited && !always) return;
			
    		treeText.update();
    		treeSelectedText.update();   		
    		treeBg.update();
    		treeTextBg.update();
    		treeSelectedBg.update();
    		treeLine.update();

			inited = true;
		}
		
		void updateTheme() {}
	}
	
	class TabbedPaneCP extends JPanel {
		
		private JCheckBox rolloverEnabled, focusEnabled, ignoreSelectedBg, fixedTabs;
		private JSpinner tabTop, tabLeft, tabBottom, tabRight;
		private JSpinner areaTop, areaLeft, areaBottom, areaRight;
		private boolean inited = false;
		
		TabbedPaneCP() {
			setupUI();
		}
		
		private void setupUI() {
			setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
			JPanel p1 = new JPanel(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.anchor = GridBagConstraints.NORTHWEST;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets = new Insets(2, 4, 0, 4);

			// Pane Border
			p1.add(new JLabel("Pane Border Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			tabPaneBorder = new SBField(Theme.tabPaneBorderColor, true);
			p1.add(tabPaneBorder, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);			
			p1.add(new JLabel("Pane Border Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			tabPaneDark = new SBField(Theme.tabPaneDarkColor, true, true);
			p1.add(tabPaneDark, gc);			
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);			
			p1.add(new JLabel("Pane Border Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			tabPaneLight = new SBField(Theme.tabPaneLightColor, true, true);
			p1.add(tabPaneLight, gc);
			
			// Tab
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Unselected Bg"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			tabNormalBg = new SBField(Theme.tabNormalColor, true);
			p1.add(tabNormalBg, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Selected Bg"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			tabSelectedBg = new SBField(Theme.tabSelectedColor, true);
			p1.add(tabSelectedBg, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);			
			p1.add(new JLabel("Rollover Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			tabRoll = new SBField(Theme.tabRolloverColor, true);
			p1.add(tabRoll, gc);
			
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Disabled Bg"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			tabDisabled = new SBField(Theme.tabDisabledColor);
			p1.add(tabDisabled, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled Selected Bg"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			tabDisabledSelected = new SBField(Theme.tabDisabledSelectedColor);
			p1.add(tabDisabledSelected, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);			
			p1.add(new JLabel("Disabled Text Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			tabDisabledText = new SBField(Theme.tabDisabledTextColor);
			p1.add(tabDisabledText, gc);
			
			// Tab Border
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Tab Border Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			tabBorder = new SBField(Theme.tabBorderColor, true);
			p1.add(tabBorder, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);			
			p1.add(new JLabel("Tab Border Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			tabDark = new SBField(Theme.tabDarkColor, true, true);
			p1.add(tabDark, gc);			
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);			
			p1.add(new JLabel("Tab Border Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			tabLight = new SBField(Theme.tabLightColor, true, true);
			p1.add(tabLight, gc);

			// Tab Insets
			gc.gridx ++;
			gc.gridy = 0;
			gc.gridheight = 1;
			gc.fill = GridBagConstraints.NONE;
			gc.anchor = GridBagConstraints.NORTH;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Tab Insets"), gc);
			
			gc.gridy = 1;
			gc.gridheight = 7;
			gc.insets = new Insets(4, 8, 0, 4);
			
			JPanel p2 = new JPanel(new GridBagLayout());
			GridBagConstraints gc2 = new GridBagConstraints();
			gc2.anchor = GridBagConstraints.NORTHWEST;
			gc2.fill = GridBagConstraints.HORIZONTAL;
			gc2.gridx = 0;
			gc2.gridy = 1;
			gc2.insets = new Insets(0, 2, 0, 2);
			
			tabLeft = new JSpinner(new SpinnerNumberModel(6, 0, 99, 1));
      		tabLeft.addChangeListener(spinnerUpdateAction);
			p2.add(tabLeft, gc2);
			
			gc2.gridx ++;
			gc2.gridy = 0;
			tabTop = new JSpinner(new SpinnerNumberModel(1, 0, 99, 1));
      		tabTop.addChangeListener(spinnerUpdateAction);
			p2.add(tabTop, gc2);
			gc2.gridy += 2;
			gc2.gridy ++;
			tabBottom = new JSpinner(new SpinnerNumberModel(4, 0, 99, 1));
      		tabBottom.addChangeListener(spinnerUpdateAction);
			p2.add(tabBottom, gc2);
			
			gc2.gridx ++;
			gc2.gridy = 1;
			tabRight = new JSpinner(new SpinnerNumberModel(6, 0, 99, 1));
      		tabRight.addChangeListener(spinnerUpdateAction);
			p2.add(tabRight, gc2);
			
			p1.add(p2, gc);
			
			// Tab Area Insets
			gc.gridx ++;
			gc.gridy = 0;
			gc.gridheight = 1;
			gc.fill = GridBagConstraints.NONE;
			gc.anchor = GridBagConstraints.NORTH;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Tab Area Insets"), gc);
			
			gc.gridy = 1;
			gc.gridheight = 7;
			gc.insets = new Insets(4, 8, 0, 4);
			
			p2 = new JPanel(new GridBagLayout());
			gc2 = new GridBagConstraints();
			gc2.anchor = GridBagConstraints.NORTHWEST;
			gc2.fill = GridBagConstraints.HORIZONTAL;
			gc2.gridx = 0;
			gc2.gridy = 1;
			gc2.insets = new Insets(0, 2, 0, 2);
			
			areaLeft = new JSpinner(new SpinnerNumberModel(2, 0, 99, 1));
      		areaLeft.addChangeListener(spinnerUpdateAction);
			p2.add(areaLeft, gc2);
			
			gc2.gridx ++;
			gc2.gridy = 0;
			areaTop = new JSpinner(new SpinnerNumberModel(6, 2, 99, 1));
      		areaTop.addChangeListener(spinnerUpdateAction);
			p2.add(areaTop, gc2);
			gc2.gridy += 2;
			gc2.gridy ++;
			areaBottom = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
      		areaBottom.setEnabled(false);
			p2.add(areaBottom, gc2);
			
			gc2.gridx ++;
			gc2.gridy = 1;
			areaRight = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
      		areaRight.addChangeListener(spinnerUpdateAction);
			p2.add(areaRight, gc2);
			
			p1.add(p2, gc);
			
			// Flags
			gc.gridx = 0;
			gc.gridy = 8;
			gc.insets = new Insets(8, 4, 0, 4);
			gc.anchor = GridBagConstraints.WEST;
			gc.fill = GridBagConstraints.NONE;
			gc.gridwidth = 5;		
			p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
			rolloverEnabled = new JCheckBox("Paint Rollover", true);
			rolloverEnabled.addActionListener(checkAction);
			p2.add(rolloverEnabled);
			
			focusEnabled = new JCheckBox("Paint Focus", true);
			focusEnabled.addActionListener(checkAction);
			p2.add(focusEnabled);

			ignoreSelectedBg = new JCheckBox("Ignore Selected Bg", true);
			ignoreSelectedBg.addActionListener(new CheckUpdateAction());
			p2.add(ignoreSelectedBg, BorderLayout.CENTER);
			
			fixedTabs = new JCheckBox("Fixed Tab Positions", true);
			fixedTabs.addActionListener(new CheckUpdateAction());
			p2.add(fixedTabs);

			p1.add(p2, gc);

			add(p1);
		}
		
		void init(boolean always) {
			if(inited && !always) return;

			rolloverEnabled.setSelected(Theme.tabRollover[Theme.style]);
			focusEnabled.setSelected(Theme.tabFocus[Theme.style]);
			ignoreSelectedBg.setSelected(Theme.ignoreSelectedBg[Theme.style]);
			fixedTabs.setSelected(Theme.fixedTabs[Theme.style]);

			tabPaneBorder.update();
			tabPaneDark.update();
			tabPaneLight.update();
			tabNormalBg.update();
			tabSelectedBg.update();
			tabDisabled.update();
			tabDisabledSelected.update();
			tabDisabledText.update();
			tabBorder.update();
			tabDark.update();
			tabLight.update();
			tabRoll.update();
			
			tabTop.setValue(new Integer(Theme.tabInsets[Theme.style].top));
    		tabLeft.setValue(new Integer(Theme.tabInsets[Theme.style].left));
    		tabBottom.setValue(new Integer(Theme.tabInsets[Theme.style].bottom));
    		tabRight.setValue(new Integer(Theme.tabInsets[Theme.style].right));
    		
    		areaTop.setValue(new Integer(Theme.tabAreaInsets[Theme.style].top));
    		areaLeft.setValue(new Integer(Theme.tabAreaInsets[Theme.style].left));
    		areaBottom.setValue(new Integer(Theme.tabAreaInsets[Theme.style].bottom));
    		areaRight.setValue(new Integer(Theme.tabAreaInsets[Theme.style].right));
			
			inited = true;
		}
		
		void updateTheme() {
			if(!inited || resistUpdate) return;

			Theme.tabRollover[Theme.style] = rolloverEnabled.isSelected();
			Theme.tabFocus[Theme.style] = focusEnabled.isSelected();
    		Theme.ignoreSelectedBg[Theme.style] = ignoreSelectedBg.isSelected();
    		Theme.fixedTabs[Theme.style] = fixedTabs.isSelected();
    		
    		Theme.tabInsets[Theme.style] = new Insets(
    			((Integer)tabTop.getValue()).intValue(),
    			((Integer)tabLeft.getValue()).intValue(),
    			((Integer)tabBottom.getValue()).intValue(),
    			((Integer)tabRight.getValue()).intValue());

			Theme.tabAreaInsets[Theme.style] = new Insets(
    			((Integer)areaTop.getValue()).intValue(),
    			((Integer)areaLeft.getValue()).intValue(),
    			((Integer)areaBottom.getValue()).intValue(),
    			((Integer)areaRight.getValue()).intValue());
		}
		
		int getFirstTabDistance() {
			return 2;
		}
	}
	
	class TextCP extends JPanel {
		private JSpinner mTop, mLeft, mBottom, mRight;
		private boolean inited = false;
		
		TextCP() {
			setupUI();
		}
		
		private void setupUI() {
			setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
			JPanel p1 = new JPanel(new GridBagLayout());
			//p1.setBorder(new EtchedBorder());
			GridBagConstraints gc = new GridBagConstraints();
			gc.anchor = GridBagConstraints.WEST;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets = new Insets(2, 4, 0, 4);

			// background
			p1.add(new JLabel("Text Background"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			textBg = new SBField(Theme.textBgColor, true);
			p1.add(textBg, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Text Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			textText = new SBField(Theme.textTextColor, true);
			p1.add(textText, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Caret Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			textCaret = new SBField(Theme.textCaretColor, true);
			p1.add(textCaret, gc);

			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Selected Bg"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			textSelectedBg = new SBField(Theme.textSelectedBgColor, true);
			p1.add(textSelectedBg, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Selected Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			textSelectedText = new SBField(Theme.textSelectedTextColor, true);
			p1.add(textSelectedText, gc);
			
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Disabled Bg"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			textDisabledBg = new SBField(Theme.textDisabledBgColor);
			p1.add(textDisabledBg, gc);

			// Borders
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Border Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			textBorder = new SBField(Theme.textBorderColor);
			p1.add(textBorder, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Border Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			textDark = new SBField(Theme.textBorderDarkColor, false, true);
			p1.add(textDark, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Border Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			textLight = new SBField(Theme.textBorderLightColor, false, true);
			p1.add(textLight, gc);
			
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Disabled Border"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			textBorderDisabled = new SBField(Theme.textBorderDisabledColor);
			p1.add(textBorderDisabled, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			textDisabledDark = new SBField(Theme.textBorderDarkDisabledColor, false, true);
			p1.add(textDisabledDark, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			textDisabledLight = new SBField(Theme.textBorderLightDisabledColor, false, true);
			p1.add(textDisabledLight, gc);
			
			// Insets
			gc.gridx ++;
			gc.gridy = 0;
			gc.gridheight = 6;
			gc.insets = new Insets(2, 16, 0, 4);
			
			JPanel p2 = new JPanel(new GridBagLayout());
			GridBagConstraints gc2 = new GridBagConstraints();
			gc2.anchor = GridBagConstraints.NORTHWEST;
			gc2.fill = GridBagConstraints.HORIZONTAL;
			gc2.gridx = 0;
			gc2.gridy = 1;
			gc2.insets = new Insets(0, 2, 0, 2);
			
			mLeft = new JSpinner(new SpinnerNumberModel(16, 2, 24, 1));
      		mLeft.addChangeListener(spinnerUpdateAction);
			p2.add(mLeft, gc2);
			
			gc2.gridx ++;
			gc2.gridy = 0;
			mTop = new JSpinner(new SpinnerNumberModel(2, 1, 8, 1));
      		mTop.addChangeListener(spinnerUpdateAction);
			p2.add(mTop, gc2);
			gc2.gridy ++;
			p2.add(new JLabel("Insets"), gc2);
			gc2.gridy ++;
			mBottom = new JSpinner(new SpinnerNumberModel(3, 1, 8, 1));
      		mBottom.addChangeListener(spinnerUpdateAction);
			p2.add(mBottom, gc2);
			
			gc2.gridx ++;
			gc2.gridy = 1;
			mRight = new JSpinner(new SpinnerNumberModel(16, 2, 24, 1));
      		mRight.addChangeListener(spinnerUpdateAction);
			p2.add(mRight, gc2);
			
			p1.add(p2, gc);
			
			add(p1);
		}
		
		void init(boolean always) {
			if(inited && !always) return;

    		mTop.setValue(new Integer(Theme.textInsets[Theme.style].top));
    		mLeft.setValue(new Integer(Theme.textInsets[Theme.style].left));
    		mBottom.setValue(new Integer(Theme.textInsets[Theme.style].bottom));
    		mRight.setValue(new Integer(Theme.textInsets[Theme.style].right));
    		
    		textText.update();
			textCaret.update();
			textSelectedText.update();
    		textBg.update();
    		textSelectedBg.update();
    		textDisabledBg.update();
    		textBorder.update();
    		textDark.update();
    		textLight.update();
    		textDisabledDark.update();
    		textDisabledLight.update();
    		textBorderDisabled.update();
					
			inited = true;
		}
		
		void updateTheme() {
			if(!inited || resistUpdate) return;

    		Theme.textInsets[Theme.style] = new Insets(
    			((Integer)mTop.getValue()).intValue(),
    			((Integer)mLeft.getValue()).intValue(),
    			((Integer)mBottom.getValue()).intValue(),
    			((Integer)mRight.getValue()).intValue());
		}
	}
	
	class ListCP extends JPanel {
		private boolean inited = false;
		
		ListCP() {
			setupUI();
		}
		
		private void setupUI() {
			setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
			JPanel p1 = new JPanel(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.anchor = GridBagConstraints.WEST;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets = new Insets(2, 4, 0, 4);
			
			p1.add(new JLabel("Background"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			listBg = new SBField(Theme.listBgColor, true);
			p1.add(listBg, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Foreground"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			listText = new SBField(Theme.listTextColor, true);
			p1.add(listText, gc);
			gc.gridx ++;

			gc.gridy = 0;
			p1.add(new JLabel("Selected Background"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			listSelectedBg = new SBField(Theme.listSelectedBgColor, true);
			p1.add(listSelectedBg, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Selected Foreground"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			listSelectedText = new SBField(Theme.listSelectedTextColor, true);
			p1.add(listSelectedText, gc);
			
			add(p1);
		}
		
		void init(boolean always) {
			if(inited && !always) return;
			
			listText.update();
			listBg.update();
			listSelectedText.update();
			listSelectedBg.update();

			inited = true;
		}
		
		void updateTheme() {}
	}
	
	class MiscCP extends JPanel {
		private boolean inited = false;
		
		MiscCP() {
			setupUI();
		}
		
		private void setupUI() {
			setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
			JPanel p1 = new JPanel(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.anchor = GridBagConstraints.WEST;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets = new Insets(2, 4, 0, 4);

			p1.add(new JLabel("TitledBorder Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			titledBorderColor = new SBField(Theme.titledBorderColor, true);
			p1.add(titledBorderColor, gc);
			
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 4, 0, 4);
			p1.add(new JLabel("EditorPane Bg Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			editorPaneBg = new SBField(Theme.editorPaneBgColor, true);
			p1.add(editorPaneBg, gc);
			
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 4, 0, 4);
			p1.add(new JLabel("TextPane Bg Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			textPaneBg = new SBField(Theme.textPaneBgColor, true);
			p1.add(textPaneBg, gc);
			
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 4, 0, 4);
			p1.add(new JLabel("DesktopPane Bg Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			desktopPaneBg = new SBField(Theme.desktopPaneBgColor, true);
			p1.add(desktopPaneBg, gc);
			
			add(p1);
		}
		
		void init(boolean always) {
			if(inited && !always) return;
			
			titledBorderColor.update();
			editorPaneBg.update();
			textPaneBg.update();
			desktopPaneBg.update();

			inited = true;
		}
		
		void updateTheme() {}
	}
	
	class ToolTipCP extends JPanel {
		private boolean inited = false;
		
		ToolTipCP() {
			setupUI();
		}
		
		private void setupUI() {
			setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
			JPanel p1 = new JPanel(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.anchor = GridBagConstraints.WEST;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets = new Insets(2, 4, 0, 4);

			p1.add(new JLabel("Border Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			tipBorder = new SBField(Theme.tipBorderColor, true);
			p1.add(tipBorder, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Disabled Border"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			tipBorderDis = new SBField(Theme.tipBorderDis, true);
			p1.add(tipBorderDis, gc);
			
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 4, 0, 4);
			p1.add(new JLabel("Background Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			tipBg = new SBField(Theme.tipBgColor, true);
			p1.add(tipBg, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Disabled Background"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			tipBgDis = new SBField(Theme.tipBgDis, true);
			p1.add(tipBgDis, gc);
			
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 4, 0, 4);
			p1.add(new JLabel("Text Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			tipText = new SBField(Theme.tipTextColor, true);
			p1.add(tipText, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Disabled Text"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			tipTextDis = new SBField(Theme.tipTextDis, true);
			p1.add(tipTextDis, gc);
			
			// Test labels
			Border b = BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.DARK_GRAY),
				BorderFactory.createEmptyBorder(2, 4, 2, 4));
			JPanel p2 = new JPanel(new BorderLayout(12, 0));
			JLabel l1 = new JLabel("Display Enabled TT");
			l1.setFont(l1.getFont().deriveFont(Font.BOLD));
			l1.setToolTipText("Enabled ToolTip");
			l1.setBorder(b);
			p2.add(l1, BorderLayout.CENTER);
			
			JLabel l2 = new JLabel("Display Disabled TT");
			l2.setFont(l1.getFont().deriveFont(Font.BOLD));
			l2.setToolTipText("Disabled ToolTip");
			l2.setBorder(b);
			l2.setEnabled(false);
			p2.add(l2, BorderLayout.EAST);
			
			gc.gridx = 0;
			gc.gridy ++;
			gc.insets = new Insets(12, 4, 0, 4);
			gc.gridwidth = 3;
			gc.fill = GridBagConstraints.NONE;
			gc.anchor = GridBagConstraints.CENTER;
			p1.add(p2, gc);
			
			
			add(p1);
		}
		
		void init(boolean always) {
			if(inited && !always) return;
			
			tipBg.update();
			tipBgDis.update();
			tipBorder.update();
			tipBorderDis.update();
			tipText.update();
			tipTextDis.update();

			inited = true;
		}
		
		void updateTheme() {}
	}
	
	class SeparatorCP extends JPanel {
		private boolean inited = false;
		
		SeparatorCP() {
			setupUI();
		}
		
		private void setupUI() {
			setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
			JPanel p1 = new JPanel(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.anchor = GridBagConstraints.WEST;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets = new Insets(2, 4, 0, 4);

			p1.add(new JLabel("Separator Dark Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			sepDark = new SBField(Theme.sepDarkColor, true);
			p1.add(sepDark, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Separator Light Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			sepLight = new SBField(Theme.sepLightColor, true, true);
			p1.add(sepLight, gc);
			
			add(p1);
		}
		
		void init(boolean always) {
			if(inited && !always) return;
			
			sepDark.update();
			sepLight.update();

			inited = true;
		}
		
		void updateTheme() {}
	}
	
	class IconCP extends JPanel {
		
		private boolean inited = false;
		
		IconCP() {
			setupUI();
		}
		
		private void setupUI() {
			ActionListener colorizeAction = new ColorizeAction();
			
			for(int i = 0; i < 20; i++) {
				hsb[i] = new HSBField(Theme.colorizer[i], colorizeAction, i);
				iconChecks[i] = new CheckedIcon(
					Theme.colorize[Theme.style][i], hsb[i], TinyLookAndFeel.getSystemIconName(i));
			}
			
			setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
			JPanel p = new JPanel(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.anchor = gc.WEST;
			gc.gridx = 0;
			gc.gridy = 0;
			
			JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
			p1.add(new JLabel("Tree "));
			for(int i = 1; i < 6; i++) {
				p1.add(new CombiPanel(hsb[i], iconChecks[i]));
			}			
			p.add(p1, gc);
			gc.gridy ++;
						
			p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
			p1.add(new JLabel("FileView "));
			for(int i = 6; i < 11; i++) {
				p1.add(new CombiPanel(hsb[i], iconChecks[i]));
			}			
			p.add(p1, gc);
			gc.gridy ++;
						
			p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
			p1.add(new JLabel("FileChooser "));
			for(int i = 11; i < 16; i++) {
				p1.add(new CombiPanel(hsb[i], iconChecks[i]));
			}			
			p.add(p1, gc);
			gc.gridy ++;
						
			p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
			p1.add(new JLabel("OptionPane "));
			for(int i = 16; i < 20; i++) {
				p1.add(new CombiPanel(hsb[i], iconChecks[i]));
			}
			
			p1.add(new JLabel("   InternalFrame "));
			p1.add(new CombiPanel(hsb[0], iconChecks[0]));
			
			p.add(p1, gc);
			
			add(p);
		}
		
		void init(boolean always) {
			if(inited && !always) return;
			
			for(int i = 0; i < 20; i++) {
				iconChecks[i].setIcon(TinyLookAndFeel.getUncolorizedSystemIcon(i));
				iconChecks[i].setSelected(Theme.colorize[Theme.style][i]);
				hsb[i].setHue(Theme.colorizer[i][Theme.style].getHue());
				hsb[i].setSaturation(Theme.colorizer[i][Theme.style].getSaturation());
				hsb[i].setBrightness(Theme.colorizer[i][Theme.style].getBrightness());
				hsb[i].setPreserveGrey(Theme.colorizer[i][Theme.style].isPreserveGrey());
				hsb[i].setReference(Theme.colorizer[i][Theme.style].getReference(), false);
				
				hsb[i].update();
			}

			inited = true;
		}
		
		void updateTheme() {
			if(!inited || resistUpdate) return;

			for(int i = 0; i < 20; i++) {
				Theme.colorize[Theme.style][i] = iconChecks[i].isSelected();
				Theme.colorizer[i][Theme.style].setHue(hsb[i].getHue());
				Theme.colorizer[i][Theme.style].setSaturation(hsb[i].getSaturation());
				Theme.colorizer[i][Theme.style].setBrightness(hsb[i].getBrightness());
				Theme.colorizer[i][Theme.style].setPreserveGrey(hsb[i].isPreserveGrey());
				Theme.colorizer[i][Theme.style].setReference(hsb[i].getReference());
			}
		}
		
		public void colorizeIcon(HSBField field, boolean doColorize) {
			for(int i = 0; i < 20; i++) {
				if(field.equals(hsb[i])) {
					if(doColorize) {
						Icon icon = DrawRoutines.colorize(
							((ImageIcon)TinyLookAndFeel.getUncolorizedSystemIcon(i)).getImage(),
							field.hue, field.sat, field.bri, field.preserveGrey);
						iconChecks[i].setIcon(icon);
					}
					else {
						iconChecks[i].setIcon(TinyLookAndFeel.getUncolorizedSystemIcon(i));
					}
				}				
			}
		}
		
		class ColorizeAction implements ActionListener {

			public void actionPerformed(ActionEvent e) {
				colorizeIcon((HSBField)e.getSource(), true);
			}
		}
		
		class CombiPanel extends JPanel {
			
			CombiPanel(HSBField field, CheckedIcon check) {
				setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
				add(field);
				add(check);
			}
		}
	}
	
	class ProgressCP extends JPanel {
		private boolean inited = false;
		
		ProgressCP() {
			setupUI();
		}
		
		private void setupUI() {
			setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
			JPanel p1 = new JPanel(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.anchor = GridBagConstraints.WEST;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets = new Insets(2, 4, 0, 4);

			p1.add(new JLabel("Track Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			progressTrack = new SBField(Theme.progressTrackColor, true);
			p1.add(progressTrack, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Display Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			progressField = new SBField(Theme.progressColor, true);
			p1.add(progressField, gc);
			
			// Border
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Border Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			progressBorder = new SBField(Theme.progressBorderColor);
			p1.add(progressBorder, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Dark Border"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			progressDark = new SBField(Theme.progressDarkColor);
			p1.add(progressDark, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Light Border"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			progressLight = new SBField(Theme.progressLightColor);
			p1.add(progressLight, gc);
			
			// Text
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Text Forecolor"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			progressSelectFore = new SBField(Theme.progressSelectForeColor);
			p1.add(progressSelectFore, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Text Backcolor"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			progressSelectBack = new SBField(Theme.progressSelectBackColor);
			p1.add(progressSelectBack, gc);
			
			add(p1);
		}
		
		void init(boolean always) {
			if(inited && !always) return;
			
			progressField.update();
			progressTrack.update();
			progressBorder.update();
			progressDark.update();
			progressLight.update();
			progressSelectFore.update();
			progressSelectBack.update();

			inited = true;
		}
		
		void updateTheme() {}
	}
	
	class ComboCP extends JPanel {
		private JCheckBox paintFocus, rolloverEnabled;
		private JSpinner mTop, mLeft, mBottom, mRight;
		private boolean inited = false;
		
		ComboCP() {
			setupUI();
		}
		
		private void setupUI() {
			setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
			JPanel p1 = new JPanel(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.anchor = GridBagConstraints.WEST;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets = new Insets(2, 4, 0, 4);

			// Background
			p1.add(new JLabel("Background"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			comboBg = new SBField(Theme.comboBgColor, true);
			p1.add(comboBg, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Foreground"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			comboText = new SBField(Theme.comboTextColor, true);
			p1.add(comboText, gc);
			gc.gridy ++;
			
			p1.add(new JLabel("Selected Background"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			comboSelectedBg = new SBField(Theme.comboSelectedBgColor, true);
			p1.add(comboSelectedBg, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Selected Foreground"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			comboSelectedText = new SBField(Theme.comboSelectedTextColor, true);
			p1.add(comboSelectedText, gc);
			gc.gridy ++;
			
			// Border
			gc.gridx ++;
			gc.gridy = 0;
			gc.gridheight = 1;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Border Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			comboBorder = new SBField(Theme.comboBorderColor);
			p1.add(comboBorder, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Border Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			comboDark = new SBField(Theme.comboDarkColor, false, true);
			p1.add(comboDark, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Border Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			comboLight = new SBField(Theme.comboLightColor, false, true);
			p1.add(comboLight, gc);

			// Border Disabled
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Disabled Border"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			comboBorderDisabled = new SBField(Theme.comboBorderDisabledColor);
			p1.add(comboBorderDisabled, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			comboDisabledDark = new SBField(Theme.comboDarkDisabledColor, false, true);
			p1.add(comboDisabledDark, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			comboDisabledLight = new SBField(Theme.comboLightDisabledColor, false, true);
			p1.add(comboDisabledLight, gc);
			
			// Button
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 16, 0, 4);
			p1.add(new JLabel("Button Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 16, 0, 4);
			comboButt = new SBField(Theme.comboButtColor);
			p1.add(comboButt, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 16, 0, 4);
			p1.add(new JLabel("Rollover Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 16, 0, 4);
			comboButtRollover = new SBField(Theme.comboButtRolloverColor);
			p1.add(comboButtRollover, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 16, 0, 4);
			p1.add(new JLabel("Presssed Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 16, 0, 4);
			comboButtPressed = new SBField(Theme.comboButtPressedColor);
			p1.add(comboButtPressed, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 16, 0, 4);
			p1.add(new JLabel("Disabled Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 16, 0, 4);
			comboButtDisabled = new SBField(Theme.comboButtDisabledColor);
			p1.add(comboButtDisabled, gc);
			
			// Spread
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Spread Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			comboSpreadLight = new SpreadControl(
				Theme.comboSpreadLight, 20, CONTROLS_COMBO);
			p1.add(comboSpreadLight, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Spread Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			comboSpreadDark = new SpreadControl(
				Theme.comboSpreadDark, 20, CONTROLS_COMBO);
			p1.add(comboSpreadDark, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled S. Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			comboSpreadLightDisabled = new SpreadControl(
				Theme.comboSpreadLightDisabled, 20, CONTROLS_COMBO);
			p1.add(comboSpreadLightDisabled, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled S. Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			comboSpreadDarkDisabled = new SpreadControl(
				Theme.comboSpreadDarkDisabled, 20, CONTROLS_COMBO);
			p1.add(comboSpreadDarkDisabled, gc);
			
			// Button Border
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Button Border Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			comboButtBorder = new SBField(Theme.comboButtBorderColor);
			p1.add(comboButtBorder, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Border Dark Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			comboButtDark = new SBField(Theme.comboButtDarkColor, false, true);
			p1.add(comboButtDark, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Border Light Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			comboButtLight = new SBField(Theme.comboButtLightColor, false, true);
			p1.add(comboButtLight, gc);			
			
			// Border disabled
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Disabled Border"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			comboButtBorderDisabled = new SBField(Theme.comboButtBorderDisabledColor);
			p1.add(comboButtBorderDisabled, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			comboButtDarkDisabled = new SBField(Theme.comboButtDarkDisabledColor, false, true);
			p1.add(comboButtDarkDisabled, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			comboButtLightDisabled = new SBField(Theme.comboButtLightDisabledColor, false, true);
			p1.add(comboButtLightDisabled, gc);

			// Arrow
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Arrow Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			comboArrowField = new SBField(Theme.comboArrowColor);
			p1.add(comboArrowField, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled Arrow"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			comboArrowDisabled = new SBField(Theme.comboArrowDisabledColor);
			p1.add(comboArrowDisabled, gc);
			gc.gridy ++;
			
			// Flags
			gc.insets = new Insets(4, 8, 0, 4);
			gc.gridheight = 4;
			gc.gridwidth = 3;
			JPanel p2 = new JPanel(new GridLayout(2, 1, 0, 2));
			rolloverEnabled = new JCheckBox("Paint Rollover Border", true);
			rolloverEnabled.addActionListener(checkAction);
			p2.add(rolloverEnabled);
			paintFocus = new JCheckBox("Paint Focus");
			paintFocus.addActionListener(checkAction);
			p2.add(paintFocus);
			
			p1.add(p2, gc);
			
			// Insets
			gc.gridx ++;
			gc.gridy = 1;
			gc.insets = new Insets(8, 8, 0, 4);
			gc.gridheight = 4;
			gc.gridwidth = 1;
			p2 = new JPanel(new GridBagLayout());
			GridBagConstraints gc2 = new GridBagConstraints();
			gc2.anchor = GridBagConstraints.WEST;
			gc2.fill = GridBagConstraints.HORIZONTAL;
			gc2.gridx = 0;
			gc2.gridy = 1;
			gc2.insets = new Insets(0, 2, 0, 2);
			
			mLeft = new JSpinner(new SpinnerNumberModel(2, 2, 24, 1));
      		mLeft.addChangeListener(spinnerUpdateAction);
			p2.add(mLeft, gc2);
			
			gc2.gridx ++;
			gc2.gridy = 0;
			mTop = new JSpinner(new SpinnerNumberModel(2, 2, 8, 1));
      		mTop.addChangeListener(spinnerUpdateAction);
			p2.add(mTop, gc2);
			gc2.gridy ++;
			p2.add(new JLabel("Insets"), gc2);
			gc2.gridy ++;
			mBottom = new JSpinner(new SpinnerNumberModel(2, 2, 8, 1));
      		mBottom.addChangeListener(spinnerUpdateAction);
			p2.add(mBottom, gc2);
			
			gc2.gridx ++;
			gc2.gridy = 1;
			mRight = new JSpinner(new SpinnerNumberModel(2, 2, 24, 1));
      		mRight.addChangeListener(spinnerUpdateAction);
			p2.add(mRight, gc2);
			
			p1.add(p2, gc);
			
			add(p1);
		}
		
		void init(boolean always) {
			if(inited && !always) return;

			rolloverEnabled.setSelected(Theme.comboRollover[Theme.style]);
			paintFocus.setSelected(Theme.comboFocus[Theme.style]);
    		
    		comboBg.update();
    		comboText.update();
    		comboSelectedText.update();
    		comboArrowField.update();
    		comboSelectedBg.update();
    		comboBorder.update();
    		comboDark.update();
    		comboLight.update();
    		comboBorderDisabled.update();
    		comboDisabledDark.update();
    		comboDisabledLight.update();
    		comboButt.update();
    		comboButtRollover.update();
    		comboButtDisabled.update();
    		comboButtPressed.update();
    		comboButtBorder.update();
    		comboButtDark.update();
    		comboButtLight.update();
    		comboButtBorderDisabled.update();
    		comboButtDarkDisabled.update();
    		comboButtLightDisabled.update();
    		comboArrowField.update();
    		comboArrowDisabled.update();
    		comboSpreadDark.init();
			comboSpreadLight.init();
			comboSpreadDarkDisabled.init();
			comboSpreadLightDisabled.init();
    		
    		mTop.setValue(new Integer(Theme.comboInsets[Theme.style].top));
    		mLeft.setValue(new Integer(Theme.comboInsets[Theme.style].left));
    		mBottom.setValue(new Integer(Theme.comboInsets[Theme.style].bottom));
    		mRight.setValue(new Integer(Theme.comboInsets[Theme.style].right));

			inited = true;
		}
		
		void updateTheme() {
			if(!inited || resistUpdate) return;

			Theme.comboRollover[Theme.style] = rolloverEnabled.isSelected();
			Theme.comboFocus[Theme.style] = paintFocus.isSelected();
    		 
    		Theme.comboInsets[Theme.style] = new Insets(
    			((Integer)mTop.getValue()).intValue(),
    			((Integer)mLeft.getValue()).intValue(),
    			((Integer)mBottom.getValue()).intValue(),
    			((Integer)mRight.getValue()).intValue());
		}
	}
	
	class ButtonsCP  extends JPanel {
		
		private JCheckBox rolloverEnabled, focusEnabled, enterEnabled;
		private JCheckBox focusBorderEnabled, shiftTextEnabled;
		private JSpinner mTop, mLeft, mBottom, mRight;
		private JSpinner cTop, cLeft, cBottom, cRight;
		private JPanel cardPanel;
		private boolean inited = false;
		
		ButtonsCP() {
			setupUI();
		}
		
		private void setupUI() {
			setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
			JPanel p1 = new JPanel(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.anchor = GridBagConstraints.NORTHWEST;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets = new Insets(2, 4, 0, 4);

			p1.add(new JLabel("Normal Bg"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			buttonNormalBg = new SBField(Theme.buttonNormalColor, true);
			p1.add(buttonNormalBg, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Rollover Bg"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			buttonRolloverBg = new SBField(Theme.buttonRolloverBgColor, true);
			p1.add(buttonRolloverBg, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Presssed Bg"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			buttonPressedBg = new SBField(Theme.buttonPressedColor, true);
			p1.add(buttonPressedBg, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Disabled Bg"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			buttonDisabledBg = new SBField(Theme.buttonDisabledColor);
			p1.add(buttonDisabledBg, gc);
			
			// Spread
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Spread Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			buttonSpreadLight = new SpreadControl(
				Theme.buttonSpreadLight, 20, CONTROLS_BUTTON);
			p1.add(buttonSpreadLight, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Spread Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			buttonSpreadDark = new SpreadControl(
				Theme.buttonSpreadDark, 20, CONTROLS_BUTTON);
			p1.add(buttonSpreadDark, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled S. Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			buttonSpreadLightDisabled = new SpreadControl(
				Theme.buttonSpreadLightDisabled, 20, CONTROLS_BUTTON);
			p1.add(buttonSpreadLightDisabled, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled S. Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			buttonSpreadDarkDisabled = new SpreadControl(
				Theme.buttonSpreadDarkDisabled, 20, CONTROLS_BUTTON);
			p1.add(buttonSpreadDarkDisabled, gc);
			
			// toolbar button info
			gc.gridx ++;
			gc.gridy --;
			gc.gridwidth = 4;
			gc.gridheight = 2;
			gc.anchor = GridBagConstraints.CENTER;
			gc.fill = GridBagConstraints.NONE;
			JLabel info = new JLabel("<html>For toolbar buttons choose <b>'ToolBar'</b>");
			p1.add(info, gc);
			
			// border
			gc.gridx ++;
			gc.gridy = 0;
			gc.gridwidth = 1;
			gc.gridheight = 1;
			gc.anchor = GridBagConstraints.NORTHWEST;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Border Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			buttonBorder = new SBField(Theme.buttonBorderColor);
			p1.add(buttonBorder, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Dark Border"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			buttonDark = new SBField(Theme.buttonDarkColor, false, true);
			p1.add(buttonDark, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Light Border"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			buttonLight = new SBField(Theme.buttonLightColor, false, true);
			p1.add(buttonLight, gc);			

			// disabled border
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Disabled Border"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			buttonDisabledBorder = new SBField(Theme.buttonBorderDisabledColor);
			p1.add(buttonDisabledBorder, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			buttonDisabledDark = new SBField(Theme.buttonDarkDisabledColor, false, true);
			p1.add(buttonDisabledDark, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			buttonDisabledLight = new SBField(Theme.buttonLightDisabledColor, false, true);
			p1.add(buttonDisabledLight, gc);
			
			// disabled foreground
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Button Disabled Text"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			buttonDisabledFg = new SBField(Theme.buttonDisabledFgColor, true);
			p1.add(buttonDisabledFg, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("CheckBox Disabled T."), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			checkDisabledFg = new SBField(Theme.checkDisabledFgColor, true);
			p1.add(checkDisabledFg, gc);
			
			gc.gridy ++;
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("RadioButton Disabled T."), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			radioDisabledFg = new SBField(Theme.radioDisabledFgColor, true);
			p1.add(radioDisabledFg, gc);
			
			// default/rollover
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Default Button Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			buttonDefault = new SBField(Theme.buttonDefaultColor, true);
			p1.add(buttonDefault, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Rollover Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			buttonRollover = new SBField(Theme.buttonRolloverColor);
			p1.add(buttonRollover, gc);
			
			// Flags
			GridBagConstraints gc3 = new GridBagConstraints();
			gc3.anchor = GridBagConstraints.NORTHWEST;
			gc3.fill = GridBagConstraints.HORIZONTAL;
			gc3.gridx = 0;
			gc3.gridy = 0;
			JPanel p3 = new JPanel(new GridBagLayout());
			
			gc3.insets = new Insets(0, 0, 0, 4);
			rolloverEnabled = new JCheckBox("Paint Rollover Border", true);
			buttons = new Component[24];
			buttons[15] = rolloverEnabled;
			rolloverEnabled.addActionListener(checkAction);
			p3.add(rolloverEnabled, gc3);
			
			gc3.gridy ++;
			shiftTextEnabled = new JCheckBox("Shift Button Text", true);
			buttons[16] = shiftTextEnabled;
			shiftTextEnabled.addActionListener(checkAction);
			p3.add(shiftTextEnabled, gc3);
			
			gc3.gridy = 0;
			gc3.gridx = 1;
			focusEnabled = new JCheckBox("Paint Focus", true);
			buttons[17] = focusEnabled;
			focusEnabled.addActionListener(checkAction);
			p3.add(focusEnabled, gc3);
			
			gc3.gridy = 1;
			focusBorderEnabled = new JCheckBox("Paint Focus Border", true);
			buttons[18] = focusBorderEnabled;
			focusBorderEnabled.addActionListener(checkAction);
			p3.add(focusBorderEnabled, gc3);

			gc3.gridy = 2;
			gc3.gridx = 0;
			gc3.gridwidth = 2;
			enterEnabled = new JCheckBox("ENTER \"presses\" focused button");
			buttons[19] = enterEnabled;
			enterEnabled.addActionListener(new CheckUpdateAction());
			p3.add(enterEnabled, gc3);
			
			gc.gridy = 5;
			gc.gridheight = 4;
			gc.gridwidth = 3;
			gc.fill = GridBagConstraints.NONE;
			p1.add(p3, gc);

			// checkmark
			gc.gridx ++;
			gc.gridy = 0;
			gc.gridwidth = 1;
			gc.gridheight = 1;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Checkmark Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			buttonCheck = new SBField(Theme.buttonCheckColor);
			p1.add(buttonCheck, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Check Disabled"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			buttonCheckDisabled = new SBField(Theme.buttonCheckDisabledColor);
			p1.add(buttonCheckDisabled, gc);

			// Margin
			gc.gridx ++;
			gc.gridy = 0;
			gc.gridheight = 1;
			gc.gridwidth = 1;
			gc.insets = new Insets(0, 16, 0, 0);
			
			ButtonGroup group = new ButtonGroup();
			JRadioButton rb = new JRadioButton("Button Margin", true);
			buttons[20] = rb;
			group.add(rb);
			rb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					AbstractButton b = (AbstractButton)e.getSource();
					
					if(!b.isSelected()) return;
					
					((CardLayout)cardPanel.getLayout()).show(cardPanel, "buttonMargin");
				}
			});
			p1.add(rb, gc);
			
			gc.gridy ++;
			rb = new JRadioButton("CheckBox/RadioButton Margin");
			buttons[21] = rb;
			group.add(rb);
			rb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					AbstractButton b = (AbstractButton)e.getSource();
					
					if(!b.isSelected()) return;
					
					((CardLayout)cardPanel.getLayout()).show(cardPanel, "checkMargin");
				}
			});
			p1.add(rb, gc);

			gc.gridy ++;
			gc.insets = new Insets(4, 16, 0, 0);
			gc.gridheight = 5;
			
			cardPanel = new JPanel(new CardLayout());
			cardPanel.add(createButtonMarginPanel(), "buttonMargin");
			cardPanel.add(createCheckMarginPanel(), "checkMargin");
			
			p1.add(cardPanel, gc);
	
			add(p1);
		}
		
		void init(boolean always) {
			if(inited && !always) return;
			
			rolloverEnabled.setSelected(Theme.buttonRollover[Theme.style]);
			focusEnabled.setSelected(Theme.buttonFocus[Theme.style]);
			focusBorderEnabled.setSelected(Theme.buttonFocusBorder[Theme.style]);
			enterEnabled.setSelected(Theme.buttonEnter[Theme.style]);
			shiftTextEnabled.setSelected(Theme.shiftButtonText[Theme.style]);

			buttonNormalBg.update();
			buttonRolloverBg.update();
			buttonPressedBg.update();
			buttonDisabledBg.update();
			buttonBorder.update();
			buttonDark.update();
			buttonLight.update();
			buttonDisabledBorder.update();
			buttonDisabledDark.update();
			buttonDisabledLight.update();
			buttonDisabledFg.update();
			checkDisabledFg.update();
			radioDisabledFg.update();
			buttonRollover.update();
			buttonDefault.update();
			buttonCheck.update();
			buttonCheckDisabled.update();
			buttonSpreadDark.init();
			buttonSpreadLight.init();
			buttonSpreadDarkDisabled.init();
			buttonSpreadLightDisabled.init();

    		mTop.setValue(new Integer(Theme.buttonMarginTop[Theme.style]));
    		mLeft.setValue(new Integer(Theme.buttonMarginLeft[Theme.style]));
    		mBottom.setValue(new Integer(Theme.buttonMarginBottom[Theme.style]));
    		mRight.setValue(new Integer(Theme.buttonMarginRight[Theme.style]));
    		
    		cTop.setValue(new Integer(Theme.checkMarginTop[Theme.style]));
    		cLeft.setValue(new Integer(Theme.checkMarginLeft[Theme.style]));
    		cBottom.setValue(new Integer(Theme.checkMarginBottom[Theme.style]));
    		cRight.setValue(new Integer(Theme.checkMarginRight[Theme.style]));
					
			inited = true;
		}
		
		InsetsUIResource getButtonMargin() {
			return new InsetsUIResource(
				((Integer)mTop.getValue()).intValue(),
				((Integer)mLeft.getValue()).intValue(),
				((Integer)mBottom.getValue()).intValue(),
				((Integer)mRight.getValue()).intValue());
		}
		
		void updateTheme() {
			if(!inited || resistUpdate) return;
			
			Theme.buttonRollover[Theme.style] = rolloverEnabled.isSelected();
			Theme.buttonFocus[Theme.style] = focusEnabled.isSelected();
			Theme.buttonFocusBorder[Theme.style] = focusBorderEnabled.isSelected();
			Theme.buttonEnter[Theme.style] = enterEnabled.isSelected();
			Theme.shiftButtonText[Theme.style] = shiftTextEnabled.isSelected();

    		Theme.buttonMarginTop[Theme.style] = ((Integer)mTop.getValue()).intValue();
    		Theme.buttonMarginLeft[Theme.style] = ((Integer)mLeft.getValue()).intValue();
    		Theme.buttonMarginBottom[Theme.style] = ((Integer)mBottom.getValue()).intValue();
    		Theme.buttonMarginRight[Theme.style] = ((Integer)mRight.getValue()).intValue();
    		
    		Theme.checkMarginTop[Theme.style] = ((Integer)cTop.getValue()).intValue();
    		Theme.checkMarginLeft[Theme.style] = ((Integer)cLeft.getValue()).intValue();
    		Theme.checkMarginBottom[Theme.style] = ((Integer)cBottom.getValue()).intValue();
    		Theme.checkMarginRight[Theme.style] = ((Integer)cRight.getValue()).intValue();
		}
		
		private JPanel createButtonMarginPanel() {
			JPanel p2 = new JPanel(new GridBagLayout());
			GridBagConstraints gc2 = new GridBagConstraints();
			gc2.anchor = GridBagConstraints.NORTHWEST;
			gc2.fill = GridBagConstraints.HORIZONTAL;
			gc2.gridx = 0;
			gc2.gridy = 1;
			gc2.insets = new Insets(0, 0, 0, 0);
			
			mLeft = new JSpinner(new SpinnerNumberModel(16, 0, 99, 1));
      		mLeft.addChangeListener(spinnerUpdateAction);
			p2.add(mLeft, gc2);
			
			gc2.gridx ++;
			gc2.gridy = 0;
			mTop = new JSpinner(new SpinnerNumberModel(2, 0, 99, 1));
      		mTop.addChangeListener(spinnerUpdateAction);
			p2.add(mTop, gc2);
			gc2.gridy += 2;
			mBottom = new JSpinner(new SpinnerNumberModel(3, 0, 99, 1));
      		mBottom.addChangeListener(spinnerUpdateAction);
			p2.add(mBottom, gc2);
			
			gc2.gridx ++;
			gc2.gridy = 1;
			mRight = new JSpinner(new SpinnerNumberModel(16, 0, 99, 1));
      		mRight.addChangeListener(spinnerUpdateAction);
			p2.add(mRight, gc2);
			
			return p2;
		}
		
		private JPanel createCheckMarginPanel() {
			JPanel p2 = new JPanel(new GridBagLayout());
			GridBagConstraints gc2 = new GridBagConstraints();
			gc2.anchor = GridBagConstraints.NORTHWEST;
			gc2.fill = GridBagConstraints.HORIZONTAL;
			gc2.gridx = 0;
			gc2.gridy = 1;
			gc2.insets = new Insets(0, 0, 0, 0);
			
			cLeft = new JSpinner(new SpinnerNumberModel(16, 0, 99, 1));
      		cLeft.addChangeListener(spinnerUpdateAction);
			p2.add(cLeft, gc2);
			
			gc2.gridx ++;
			gc2.gridy = 0;
			cTop = new JSpinner(new SpinnerNumberModel(2, 0, 99, 1));
      		cTop.addChangeListener(spinnerUpdateAction);
			p2.add(cTop, gc2);
			gc2.gridy += 2;
			cBottom = new JSpinner(new SpinnerNumberModel(3, 0, 99, 1));
      		cBottom.addChangeListener(spinnerUpdateAction);
			p2.add(cBottom, gc2);
			
			gc2.gridx ++;
			gc2.gridy = 1;
			cRight = new JSpinner(new SpinnerNumberModel(16, 0, 99, 1));
      		cRight.addChangeListener(spinnerUpdateAction);
			p2.add(cRight, gc2);
			
			return p2;
		}
	}

	class FrameCP extends JPanel {

		private boolean inited = false;
		private CardLayout cardLayout;
		private JPanel cardPanel;
		
		FrameCP() {
			cardLayout = new CardLayout();

			setupUI();
		}
		
		private void setupUI() {
			setLayout(new BorderLayout());
			// Radios
			JPanel p1 = new JPanel(new GridLayout(5, 1, 0, 0));
			JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 8));
			ButtonGroup group = new ButtonGroup();
			JRadioButton radio = new JRadioButton("Frame", true);
			group.add(radio);
			radio.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cardLayout.show(cardPanel, "Frame");
				}
			});
			p1.add(radio);
			radio = new JRadioButton("Iconify/Maximize Buttons");
			group.add(radio);
			radio.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cardLayout.show(cardPanel, "FrameButtons");
				}
			});
			p1.add(radio);
			radio = new JRadioButton("Close Button");
			group.add(radio);
			radio.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cardLayout.show(cardPanel, "FrameCloseButton");
				}
			});
			p1.add(radio);
			
			p1.add(new JLabel(""));
			
			JButton b = new JButton("Activate/Deactivate Internal Frame");
			b.addActionListener(new DeactivateInternalFrameAction());
			p1.add(b);
			
			p2.add(p1);
			add(p2, BorderLayout.CENTER);
			
			// CardPanel
			cardPanel = new JPanel(cardLayout);
			cardPanel.add(new FrameFrameCP(), "Frame");
			cardPanel.add(new FrameButtonsCP(), "FrameButtons");
			cardPanel.add(new FrameCloseButtonCP(), "FrameCloseButton");
			cardLayout.layoutContainer(this);
			
			add(cardPanel, BorderLayout.WEST);
		}
		
		void init(boolean always) {
			if(inited && !always) return;

    		frameCaption.update();
    		frameCaptionDisabled.update();
    		frameBorder.update();
    		frameDark.update();
    		frameLight.update();
    		frameBorderDisabled.update();
    		frameDarkDisabled.update();
    		frameLightDisabled.update();
    		frameTitle.update();
    		frameTitleDisabled.update();
    		frameButt.update();
    		frameButtRollover.update();
    		frameButtPressed.update();
    		frameButtDisabled.update();
    		frameButtBorder.update();
    		frameButtDark.update();
    		frameButtLight.update();
    		frameButtBorderDisabled.update();
    		frameButtDarkDisabled.update();
    		frameButtLightDisabled.update();
    		frameButtSpreadDark.init();
			frameButtSpreadLight.init();
			frameButtSpreadDarkDisabled.init();
			frameButtSpreadLightDisabled.init();
    		frameButtClose.update();
    		frameButtCloseRollover.update();
    		frameButtClosePressed.update();
    		frameButtCloseDisabled.update();
    		frameButtCloseBorder.update();
    		frameButtCloseDark.update();
    		frameButtCloseLight.update();
    		frameButtCloseBorderDisabled.update();
    		frameButtCloseDarkDisabled.update();
    		frameButtCloseLightDisabled.update();
    		frameButtCloseSpreadDark.init();
			frameButtCloseSpreadLight.init();
			frameButtCloseSpreadDarkDisabled.init();
			frameButtCloseSpreadLightDisabled.init();
    		frameSymbol.update();
    		frameSymbolPressed.update();
    		frameSymbolDisabled.update();
    		frameSymbolDark.update();
    		frameSymbolLight.update();
    		frameSymbolClose.update();
    		frameSymbolClosePressed.update();
    		frameSymbolCloseDisabled.update();
    		frameSymbolCloseDark.update();
    		frameSymbolCloseLight.update();
    		frameSpreadDark.init();
    		frameSpreadLight.init();
    		frameSpreadDarkDisabled.init();
    		frameSpreadLightDisabled.init();
    		
			inited = true;
		}
		
		void updateTheme() {}
		
		class DeactivateInternalFrameAction implements ActionListener {
			
			public void actionPerformed(ActionEvent e) {
				try {
					internalFrame.setSelected(!internalFrame.isSelected());
				} catch (PropertyVetoException ignore) {}
			}
		}
	}
	
	class FrameFrameCP extends JPanel {
		
		FrameFrameCP() {
			setupUI();
		}
		
		private void setupUI() {    		
    		setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
			JPanel p1 = new JPanel(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.anchor = GridBagConstraints.NORTHWEST;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets = new Insets(2, 4, 0, 4);

			p1.add(new JLabel("Caption Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			frameCaption = new SBField(Theme.frameCaptionColor);
			p1.add(frameCaption, gc);
			gc.gridy ++;

			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Disabled Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			frameCaptionDisabled = new SBField(Theme.frameCaptionDisabledColor);
			p1.add(frameCaptionDisabled, gc);
			
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Spread Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameSpreadDark = new SpreadControl(
				Theme.frameSpreadDark, 10, CONTROLS_ACTIVE_FRAME_CAPTION);
			p1.add(frameSpreadDark, gc);			
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Spread Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameSpreadLight = new SpreadControl(
				Theme.frameSpreadLight, 10, CONTROLS_ACTIVE_FRAME_CAPTION);
			p1.add(frameSpreadLight, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("S. Dark Disabled"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameSpreadDarkDisabled = new SpreadControl(
				Theme.frameSpreadDarkDisabled, 10, CONTROLS_INACTIVE_FRAME_CAPTION);
			p1.add(frameSpreadDarkDisabled, gc);gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("S. Light Disabled"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameSpreadLightDisabled = new SpreadControl(
				Theme.frameSpreadLightDisabled, 10, CONTROLS_INACTIVE_FRAME_CAPTION);
			p1.add(frameSpreadLightDisabled, gc);
			
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Border Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameBorder = new SBField(Theme.frameBorderColor);
			p1.add(frameBorder, gc);
			gc.gridy ++;

			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Border Dark Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameDark = new SBField(Theme.frameDarkColor, false, true);
			p1.add(frameDark, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Border Light Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameLight = new SBField(Theme.frameLightColor);
			p1.add(frameLight, gc);
			gc.gridy += 2;
			
			gc.gridwidth = 3;
			gc.insets = new Insets(0, 8, 0, 4);
			
			if(decoratedFramesCheck == null) {
				decoratedFramesCheck = new JCheckBox(
					"Decorated Frame (experimental, not saved)");
				decoratedFramesCheck.addActionListener(new DecorateFrameAction());
			}
			
			p1.add(decoratedFramesCheck, gc);

			gc.gridx ++;
			gc.gridy = 0;
			gc.gridwidth = 1;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Disabled Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameBorderDisabled = new SBField(Theme.frameBorderDisabledColor);
			p1.add(frameBorderDisabled, gc);
			gc.gridy ++;

			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled Dark Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameDarkDisabled = new SBField(Theme.frameDarkDisabledColor, false, true);
			p1.add(frameDarkDisabled, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled Light Col"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameLightDisabled = new SBField(Theme.frameLightDisabledColor);
			p1.add(frameLightDisabled, gc);
			
			gc.gridx ++;
			gc.gridy = 0;
			gc.gridwidth = 1;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Title Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameTitle = new SBField(Theme.frameTitleColor);
			p1.add(frameTitle, gc);
			gc.gridy ++;

			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Title Disabled"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameTitleDisabled = new SBField(Theme.frameTitleDisabledColor);
			p1.add(frameTitleDisabled, gc);
			
			add(p1);
		}
	}
	
	class FrameButtonsCP extends JPanel {
		
		FrameButtonsCP() {
			setupUI();
		}
		
		private void setupUI() {
    		setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
			JPanel p1 = new JPanel(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.anchor = GridBagConstraints.NORTHWEST;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets = new Insets(2, 4, 0, 4);

			p1.add(new JLabel("Button Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			frameButt = new SBField(Theme.frameButtColor);
			p1.add(frameButt, gc);
			gc.gridy ++;

			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Rollover Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			frameButtRollover = new SBField(Theme.frameButtRolloverColor);
			p1.add(frameButtRollover, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Pressed Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			frameButtPressed = new SBField(Theme.frameButtPressedColor);
			p1.add(frameButtPressed, gc);			
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Disabled Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			frameButtDisabled = new SBField(Theme.frameButtDisabledColor);
			p1.add(frameButtDisabled, gc);

			// Spread
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Spread Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameButtSpreadLight = new SpreadControl(
				Theme.frameButtSpreadLight, 20, CONTROLS_WINDOW_BUTTON);
			p1.add(frameButtSpreadLight, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Spread Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameButtSpreadDark = new SpreadControl(
				Theme.frameButtSpreadDark, 20, CONTROLS_WINDOW_BUTTON);
			p1.add(frameButtSpreadDark, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled S. Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameButtSpreadLightDisabled = new SpreadControl(
				Theme.frameButtSpreadLightDisabled, 20, CONTROLS_WINDOW_BUTTON);
			p1.add(frameButtSpreadLightDisabled, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled S. Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameButtSpreadDarkDisabled = new SpreadControl(
				Theme.frameButtSpreadDarkDisabled, 20, CONTROLS_WINDOW_BUTTON);
			p1.add(frameButtSpreadDarkDisabled, gc);
			
			// Border
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Border Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameButtBorder = new SBField(Theme.frameButtBorderColor);
			p1.add(frameButtBorder, gc);
			gc.gridy ++;

			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Border Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameButtDark = new SBField(Theme.frameButtDarkColor);
			p1.add(frameButtDark, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Border Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameButtLight = new SBField(Theme.frameButtLightColor);
			p1.add(frameButtLight, gc);

			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Disabled Border"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameButtBorderDisabled = new SBField(Theme.frameButtBorderDisabledColor);
			p1.add(frameButtBorderDisabled, gc);
			gc.gridy ++;

			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameButtDarkDisabled = new SBField(Theme.frameButtDarkDisabledColor);
			p1.add(frameButtDarkDisabled, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameButtLightDisabled = new SBField(Theme.frameButtLightDisabledColor);
			p1.add(frameButtLightDisabled, gc);
			
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Symbol Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameSymbol = new SBField(Theme.frameSymbolColor);
			p1.add(frameSymbol, gc);
			gc.gridy ++;

			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Pressed Symbol"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameSymbolPressed = new SBField(Theme.frameSymbolPressedColor);
			p1.add(frameSymbolPressed, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled Symbol"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameSymbolDisabled = new SBField(Theme.frameSymbolDisabledColor);
			p1.add(frameSymbolDisabled, gc);
			
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Symbol Dark Border"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameSymbolDark = new SBField(Theme.frameSymbolDarkColor);
			p1.add(frameSymbolDark, gc);
			gc.gridy ++;

			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Symbol Light Border"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameSymbolLight = new SBField(Theme.frameSymbolLightColor);
			p1.add(frameSymbolLight, gc);
			
			add(p1);
		}
	}
	
	class FrameCloseButtonCP extends JPanel {
		
		FrameCloseButtonCP() {
			setupUI();
		}
		
		private void setupUI() {
			setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
			JPanel p1 = new JPanel(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.anchor = GridBagConstraints.NORTHWEST;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets = new Insets(2, 4, 0, 4);

			p1.add(new JLabel("Button Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			frameButtClose = new SBField(Theme.frameButtCloseColor);
			p1.add(frameButtClose, gc);
			gc.gridy ++;

			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Rollover Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			frameButtCloseRollover = new SBField(Theme.frameButtCloseRolloverColor);
			p1.add(frameButtCloseRollover, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Pressed Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			frameButtClosePressed = new SBField(Theme.frameButtClosePressedColor);
			p1.add(frameButtClosePressed, gc);			
			gc.gridy ++;
			
			gc.insets = new Insets(4, 4, 0, 4);
			p1.add(new JLabel("Disabled Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 4, 0, 4);
			frameButtCloseDisabled = new SBField(Theme.frameButtCloseDisabledColor);
			p1.add(frameButtCloseDisabled, gc);

			// Spread
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Spread Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameButtCloseSpreadLight = new SpreadControl(
				Theme.frameButtCloseSpreadLight, 20, CONTROLS_WINDOW_BUTTON);
			p1.add(frameButtCloseSpreadLight, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Spread Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameButtCloseSpreadDark = new SpreadControl(
				Theme.frameButtCloseSpreadDark, 20, CONTROLS_WINDOW_BUTTON);
			p1.add(frameButtCloseSpreadDark, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled S. Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameButtCloseSpreadLightDisabled = new SpreadControl(
				Theme.frameButtCloseSpreadLightDisabled, 20, CONTROLS_WINDOW_BUTTON);
			p1.add(frameButtCloseSpreadLightDisabled, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled S. Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameButtCloseSpreadDarkDisabled = new SpreadControl(
				Theme.frameButtCloseSpreadDarkDisabled, 20, CONTROLS_WINDOW_BUTTON);
			p1.add(frameButtCloseSpreadDarkDisabled, gc);
			
			// Border
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Border Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameButtCloseBorder = new SBField(Theme.frameButtCloseBorderColor);
			p1.add(frameButtCloseBorder, gc);
			gc.gridy ++;

			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Border Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameButtCloseDark = new SBField(Theme.frameButtCloseDarkColor);
			p1.add(frameButtCloseDark, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Border Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameButtCloseLight = new SBField(Theme.frameButtCloseLightColor);
			p1.add(frameButtCloseLight, gc);

			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Disabled Border"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameButtCloseBorderDisabled = new SBField(Theme.frameButtCloseBorderDisabledColor);
			p1.add(frameButtCloseBorderDisabled, gc);
			gc.gridy ++;

			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled Dark"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameButtCloseDarkDisabled = new SBField(Theme.frameButtCloseDarkDisabledColor);
			p1.add(frameButtCloseDarkDisabled, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled Light"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameButtCloseLightDisabled = new SBField(Theme.frameButtCloseLightDisabledColor);
			p1.add(frameButtCloseLightDisabled, gc);
			
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Symbol Color"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameSymbolClose = new SBField(Theme.frameSymbolCloseColor);
			p1.add(frameSymbolClose, gc);
			gc.gridy ++;

			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Pressed Symbol"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameSymbolClosePressed = new SBField(Theme.frameSymbolClosePressedColor);
			p1.add(frameSymbolClosePressed, gc);
			gc.gridy ++;
			
			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Disabled Symbol"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameSymbolCloseDisabled = new SBField(Theme.frameSymbolCloseDisabledColor);
			p1.add(frameSymbolCloseDisabled, gc);
			
			gc.gridx ++;
			gc.gridy = 0;
			gc.insets = new Insets(2, 8, 0, 4);
			p1.add(new JLabel("Symbol Dark Border"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameSymbolCloseDark = new SBField(Theme.frameSymbolCloseDarkColor);
			p1.add(frameSymbolCloseDark, gc);
			gc.gridy ++;

			gc.insets = new Insets(4, 8, 0, 4);
			p1.add(new JLabel("Symbol Light Border"), gc);
			gc.gridy ++;
			gc.insets = new Insets(1, 8, 0, 4);
			frameSymbolCloseLight = new SBField(Theme.frameSymbolCloseLightColor);
			p1.add(frameSymbolCloseLight, gc);
			
			add(p1);
		}
	}

	class SpreadControl extends JPanel implements FocusListener {
		
		private int controlMode = 0;
		private final Color activeColor = Color.WHITE;
		private final Color inactiveColor = new Color(207, 210, 217);
		private int max = 20;
		private Dimension size = new Dimension(18, 18);
		private Font font = new Font("sansserif", Font.BOLD, 12);
		private int[] spreadRef;
		private boolean hasFocus = false;
		private int spread;
		private int x1 = 7, x2, paintX, y = 7;
		
		SpreadControl(int[] spreadRef, int max, int controlMode) {
			this.spreadRef = spreadRef;
			this.max = max;
			this.controlMode = controlMode;
			
			addMouseListener(new MouseHandler());
			addMouseMotionListener(new MouseMotionHandler());
			addKeyListener(new ArrowKeyAction());
			addFocusListener(this);
			init();
		}
		
		public void update(int spread) {
			this.repaint();
			
			if(spread == this.spread) return;
			
			this.spread = spread;
			spreadRef[Theme.style] = spread;
			
			if(internalFrame == null) return;

			if(controlMode == CONTROLS_BUTTON) {
				repaintTargets(buttons);
			}
			else if(controlMode == CONTROLS_COMBO) {
				repaintTargets(combos);
			}
			else if(controlMode == CONTROLS_ACTIVE_FRAME_CAPTION) {
				if(decoratedFramesCheck.isSelected()) {
					// to be performant, we repaint title pane only
					Component[] cs =
						theFrame.getLayeredPane().getComponentsInLayer(
							JLayeredPane.FRAME_CONTENT_LAYER.intValue());
					
					for(int i = 0; i < cs.length; i++) {
						if(cs[i] instanceof TinyTitlePane) {
							cs[i].repaint();
							break;
						}
					}
				}
				
				repaintTargets(frames);
			}
			else if(controlMode == CONTROLS_INACTIVE_FRAME_CAPTION) {
				repaintTargets(frames);
			}
			else if(controlMode == CONTROLS_SCROLLBAR) {
				repaintTargets(scrollBars);
			}
			else if(controlMode == CONTROLS_SPINNER) {
				repaintTargets(spinners);
			}
			else if(controlMode == CONTROLS_WINDOW_BUTTON) {
				repaintTargets(windowButtons);
			}
		}
		
		public void init() {
			paintX = -1;
			update(spreadRef[Theme.style]);
		}
		
		public Dimension getPreferredSize() {
			return size;
		}
		
		public void paint(Graphics g) {
			if(hasFocus) {
				g.setColor(activeColor);
			}
			else {
				g.setColor(inactiveColor);
			}
			
			g.fillRect(1, 1, getWidth() - 2, getHeight() - 2);
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
			
			x2 = getWidth() - 24;
			// Track
			g.drawLine(x1, y - 3, x1, y + 3);
			g.drawLine(x2, y - 3, x2, y + 3);
			g.drawLine(x1, y, x2, y);
			
			// Thumb
			int x = paintX;
			if(x == -1) {
				x = spread * (x2 - x1) / max + x1;
			}
			g.drawLine(x, y + 2, x, y + 2);
			g.drawLine(x - 1, y + 3, x + 1, y + 3);
			g.drawLine(x - 2, y + 4, x + 2, y + 4);
			g.drawLine(x - 3, y + 5, x + 3, y + 5);
			g.drawLine(x - 4, y + 6, x + 4, y + 6);
			
			//Number
			g.setFont(font);
			FontMetrics fm = g.getFontMetrics();
			int xd = fm.stringWidth(String.valueOf(spread));
			g.drawString(String.valueOf(spread), getWidth() - xd - 3, getHeight() - 5);
		}

		public void focusGained(FocusEvent e) {
			hasFocus = true;
		}
		
		public void focusLost(FocusEvent e) {
			hasFocus = false;
			repaint(0);
		}
		
		class MouseHandler extends MouseAdapter {

			public void mousePressed(MouseEvent e) {
				if(SpreadControl.this.equals(frameSpreadDark) ||
					SpreadControl.this.equals(frameSpreadLight))
				{
					if(!internalFrame.isSelected()) {
						try {
							internalFrame.setSelected(true);
						} catch (PropertyVetoException ignore) {}
					}
				}
				else if(SpreadControl.this.equals(frameSpreadDarkDisabled) ||
					SpreadControl.this.equals(frameSpreadLightDisabled))
				{
					if(internalFrame.isSelected()) {
						try {
							internalFrame.setSelected(false);
						} catch (PropertyVetoException ignore) {}
					}
				}
				
				if(!hasFocus) {
					requestFocus();
					repaint(0);
				}
				else {
					int x = e.getX();
					if(x < x1) x = x1;
					if(x > x2) x = x2;
				
					int xd = (x - x1);
					paintX = x;
				
					update(xd * max / (x2 - x1));
				}
			}
			
			public void mouseReleased(MouseEvent e) {
				repaint(0);
				examplePanel.update(false);
			}
		}
		
		class MouseMotionHandler extends MouseMotionAdapter {

			public void mouseDragged(MouseEvent e) {
				int x = e.getX();
				
				if(x < x1) x = x1;
				if(x > x2) x = x2;
				
				int xd = (x - x1);
				paintX = x;
				
				update(xd * max / (x2 - x1));
			}
		}
		
		class ArrowKeyAction extends KeyAdapter implements ActionListener {
		
		private javax.swing.Timer keyTimer;
		private int step;
		
		ArrowKeyAction() {
			keyTimer = new javax.swing.Timer(20, this);
		}
		
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == 38) {	// up => increase
				if(spread == max) return;
				
				step = 1;

				changeVal();
				keyTimer.setInitialDelay(300);
				keyTimer.start();
			}
			else if(e.getKeyCode() == 40) {	// up => decrease
				if(spread == 0) return;
				
				step = -1;

				changeVal();
				keyTimer.setInitialDelay(300);
				keyTimer.start();
			}
		}
		
		public void keyReleased(KeyEvent e) {
			keyTimer.stop();
		}
		
		// the keyTimer action
		public void actionPerformed(ActionEvent e) {
			changeVal();
		}
		
		private void changeVal() {
			if(spread + step < 0 || spread + step > max) return;
			
			paintX = -1;
			update(spread + step);
		}
	}
	}
	
	class SizedPanel extends JPanel {
		
		private Dimension size;
		private Color grey = new Color(204, 204, 204);
		
		SizedPanel(int w, int h) {
			size = new Dimension(w, h);
			setBackground(Color.WHITE);
		}
		
		public Dimension getPreferredSize() {
			return size;
		}
		
		public void paint(Graphics g) {
			int w = getWidth(); int h = getHeight();
			int xOffset = 0;
			
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, w, h);
			
			g.setColor(grey);
			
			for(int y = 0; y < h; y += 8) {
				for(int x = 0; x < w; x += 16) {
					g.fillRect(x + xOffset, y, 8, 8);
				}
				
				if(xOffset == 0) xOffset = 8;
				else xOffset = 0;
			}
		}
		
		public void update(Graphics g) {
			paint(g);
		}
	}
	
	class ProgressAction implements ActionListener {
		
		private int value = 0;
		
		public void actionPerformed(ActionEvent e) {			
			value ++;
			
			if(value > 20) {
				value = 0;
				horzProgressBar.setIndeterminate(!horzProgressBar.isIndeterminate());
				vertProgressBar.setIndeterminate(!vertProgressBar.isIndeterminate());
			}
			
			horzProgressBar.setValue(value);
			vertProgressBar.setValue(value);
			
			int v = value % 25;
			
			if(v < 8) {
				horzProgressBar.setString("Fun");
				vertProgressBar.setString("Fun");
			}
			else if(v < 16) {
				horzProgressBar.setString("with");
				vertProgressBar.setString("with");
			}
			else {
				horzProgressBar.setString("Swing");
				vertProgressBar.setString("Swing");
			}
		}
	}
	
	class ProgressBarAction extends MouseAdapter {

		public void mousePressed(MouseEvent e) {
			if(progressTimer == null) {
				startProgressTimer();
			}
			else if(progressTimer.isRunning()) {
				stopProgressTimer();
			}
			else {
				startProgressTimer();
			}
		}
	}

	class ThemeFileFilter extends javax.swing.filechooser.FileFilter {
		
		public boolean accept(File pathname) {
			if(pathname.isDirectory()) return true;
			if(pathname.getName().endsWith(Theme.FILE_EXTENSION)) return true;
			
			return false;
		}
		
		public String getDescription() {
			return Theme.FILE_EXTENSION;
		}
	}
	
	class SelectThemeAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {				
			openTheme(((JMenuItem)e.getSource()).getText() + Theme.FILE_EXTENSION);
		}
	}
	
	class CheckedIcon extends JPanel implements ActionListener {
		
		private HSBField field;
		private JLabel iconLabel;
		private JCheckBox check;
		private Icon icon;
		
		CheckedIcon(boolean b, HSBField field, String ttt) {
			this.field = field;
			setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
			check = new JCheckBox("", b);
			check.addActionListener(this);
			add(check);
			iconLabel = new JLabel("");
			add(iconLabel);
			super.setToolTipText(ttt);
		}
		
		public void setIcon(Icon i) {
			icon = i;
			iconLabel.setIcon(icon);
		}
		
		public void setSelected(boolean b) {
			check.setSelected(b);
		}
		
		public boolean isSelected() {
			return check.isSelected();
		}
		
		public Icon getIcon() {
			return icon;
		}
		
		public void actionPerformed(ActionEvent e) {
			iconCP.colorizeIcon(field, check.isSelected());
			examplePanel.update(true);
		}
	}
	
	class DecorateFrameAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {				
			decorateFrame(((AbstractButton)e.getSource()).isSelected());
		}
	}
	
	class CheckAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {				
			examplePanel.update(false);
		}
	}
	
	class CheckUpdateAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {				
			examplePanel.update(true);
		}
	}
	
	class UpdateAction implements ChangeListener {

		public void stateChanged(ChangeEvent e) {
			examplePanel.update(true);
		}
	}

	class SpinnerUpdateAction implements ChangeListener {

		public void stateChanged(ChangeEvent e) {
			updateThemeButton.setEnabled(true);
		}
	}
	
	class PopupTrigger extends JPanel implements MouseListener {
		
		JLabel label;
		
		PopupTrigger() {
			setLayout(new FlowLayout(FlowLayout.CENTER, 4, 2));
			setBackground(Color.ORANGE);
			label = new JLabel("Popup trigger");
			label.setForeground(Color.BLUE);
			add(label);
			addMouseListener(this);
		}
		
		void updateColors() {
			label.setForeground(Color.BLUE);
		}

		// MouseListener implementation
		public void mousePressed(MouseEvent e) {
			if(thePopup != null && thePopup.isShowing()) return;
			
			thePopup = new JPopupMenu("Popup Menu");
			JMenuItem item = new JMenuItem("Popup item #1");
			thePopup.add(item);
			item = new JMenuItem("Popup item #2");
			thePopup.add(item);

			thePopup.addSeparator();
			
			item = new JMenuItem("Popup item #3");
			thePopup.add(item);
			item = new JMenuItem("Popup disabled item");
			item.setEnabled(false);
			thePopup.add(item);
			
			thePopup.show(e.getComponent(), 0, -thePopup.getPreferredSize().height - 1);
		}
		
		public void mouseClicked(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	}
	
	private class SmallTableModel extends AbstractTableModel {
		
		String[] columnNames = {
			"C1", "C2", "C3", "C4"
		};
		
		public int getRowCount() {
			return 50;
		}

		public int getColumnCount() {
			return columnNames.length;
		}
		
		public String getColumnName(int column) {
			return columnNames[column];
		}
		
		public Class getColumnClass(int column) {
			return Integer.class;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			return new Integer(rowIndex * getColumnCount() + columnIndex + 1);
		}
	}
	
	private class AboutDialog extends JDialog {
		
		AboutDialog() {
			super(theFrame, "About TinyLaF", true);
			
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			
			String msg = "<html>" +
				"TinyLaF v" + TinyLookAndFeel.VERSION_STRING +
				" (" + TinyLookAndFeel.DATE_STRING + ")" +
				"<br>Author: Hans Bickel" +
				"<br>TinyLaF Home: www.muntjak.de/hans/java/tinylaf/";
			
			getContentPane().setLayout(new BorderLayout());
			
			JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
			p.add(new JLabel(msg));
			getContentPane().add(p, BorderLayout.CENTER);
			
			p = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
			
			JButton b = new JButton("Copy Link");
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
					
					if(cb == null) {
						JOptionPane.showMessageDialog(AboutDialog.this,
							"System Clipboard not available.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
					}
					else {
						StringSelection ss = new StringSelection(
							"http://www.muntjak.de/hans/java/tinylaf/");
						cb.setContents(ss, ss);
					}
				}
			});
			p.add(b);
			
			b = new JButton("Close");
			getRootPane().setDefaultButton(b);
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					AboutDialog.this.dispose();
				}
			});
			p.add(b);
			
			getContentPane().add(p, BorderLayout.SOUTH);
			
			pack();
			
			Point loc = theFrame.getLocationOnScreen();
			loc.x += (theFrame.getWidth() - getWidth()) / 2;
			loc.y += (theFrame.getHeight() - getHeight()) / 2;
			
			setLocation(loc);
			setVisible(true);
		}
	}
}
