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
package de.juwimm.cms;

import static de.juwimm.cms.client.beans.Application.*;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.*;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.w3c.dom.Document;

import com.Ostermiller.util.Browser;

import de.juwimm.cms.client.beans.Application;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.exceptions.UserHasNoUnitsException;
import de.juwimm.cms.gui.*;
import de.juwimm.cms.gui.LookAndFeel;
import de.juwimm.cms.gui.admin.PanAdministrationAdmin;
import de.juwimm.cms.gui.admin.PanAdministrationRoot;
import de.juwimm.cms.gui.controls.UnloadablePanel;
import de.juwimm.cms.gui.event.ExitEvent;
import de.juwimm.cms.gui.event.MyWindowListener;
import de.juwimm.cms.gui.views.PanContentView;
import de.juwimm.cms.gui.views.PanInitView;
import de.juwimm.cms.http.HttpClientWrapper;
import de.juwimm.cms.http.ProxyHelper;
import de.juwimm.cms.util.ActionHub;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.util.XercesHelper;

/**
 * <b>ConQuest Enterprise Content Management</b><br/>
 * This is the Main-Class of the Swing-Client Software.
 * <p>Copyright: Copyright (c) 2002, 2003</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class Main extends JFrame implements ActionListener {
	private static Logger log = null;

	private Communication comm;
	private PanMenubar panMenubar;
	private PanToolbar panToolbar;
	private PanRibbon panRibbon;
	private PanStatusbar panStatusbar;
	private PanLogin panLogin;
	private PanAdministrationAdmin panAdmin;
	private PanAdministrationRoot panRoot;
	private PanTool panTool;
	private ResourceBundle rb;
	private UnloadablePanel activePanel = null;
	private final static String LOG4J_PROPERTIES_ARGUMENT ="clientMailAppenderProperties"; 
	
	public static void main(String[] argv) {
		for (int i = 0; i < argv.length; i++) {
			argv[i] = argv[i].trim();
		}
		new Main(argv);
	}

	public void logSys(String stri) {
		System.out.println(stri);
	}

	public Main(String[] argv) {
		// Bugfix [CH], use the "old" Java 5 RepaintManager (no-arg constructor creates one) for current thread group
		// instead of setting the system property "swing.bufferPerWindow" to false (does not work with JavaWebStart)
		RepaintManager.setCurrentManager(new RepaintManager());

		System.setProperty("swing.aatext","true"); 
		try {
			InputStream in = this.getClass().getResourceAsStream("/pom.xml");
			String pom = IOUtils.toString(in);
			Document doc = XercesHelper.string2Dom(pom);
			
			String version =  XercesHelper.getNodeValue(doc, "//conquest.version");
			System.setProperty("conquest.version", version);
			
			Constants.CMS_VERSION = "V " + version;
			
			logSys("Starting Tizzit Version " + Constants.CMS_VERSION);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//SplashShell splash = new SplashShell();
		FrmVersion splash = new FrmVersion();
		int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int frameHeight = 300;
		int frameWidth = 450;
		splash.setLocation((screenWidth / 2) - (frameWidth / 2), (screenHeight / 2) - (frameHeight / 2));		
		splash.setIconImage(new ImageIcon(getClass().getResource("/images/cms_16x16.gif")).getImage());		
		splash.setSize(frameWidth, frameHeight);
		splash.setVisible(true);

		String host = "";
		if (argv.length >= 2 && argv[0].equals("URL_HOST")) {
			try {
				URL url = new URL(argv[1]);
				host = url.getHost();
				Constants.SERVER_SSL = url.getProtocol().equalsIgnoreCase("https");
				if(Constants.SERVER_SSL) {
					JOptionPane.showMessageDialog(null, "Fehler beim Erstellen der SSL Verbindung!\nBitte wenden Sie sich an den Tizzit Support.", "Tizzit", JOptionPane.ERROR_MESSAGE);
					System.exit(-1);
				}
				Constants.SERVER_PORT = (url.getPort() == -1) ? ((Constants.SERVER_SSL) ? 443 : 80) : url.getPort();
			} catch (Exception exe) {
			    log.error(exe);
			}
		} else if (argv.length == 1) {
			host = argv[0];
		} else {
			return;
		}
		if ("".equalsIgnoreCase(host)) { return; }

		logSys("CONNECTING HOST " + host + " " + argv[1] + " with SSL " + Constants.SERVER_SSL);
		Constants.SERVER_HOST = host;
		Constants.SVG_CACHE = Constants.DB_PATH + "svgcache_" + Constants.SERVER_HOST + System.getProperty("file.separator");
		UIConstants.setMainFrame(this);

		logSys("Setting SAX/DOM XML Parser to Apache Xerces");
		System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
		System.setProperty("javax.xml.parsers.DocumentBuilder", "org.apache.xerces.jaxp.DocumentBuilderImpl"); // needed?
		System.setProperty("javax.xml.parsers.SAXParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
		System.setProperty("javax.xml.parsers.SAXParser", "org.apache.xerces.jaxp.SAXParserImpl"); //needed?
		System.setProperty("org.xml.sax.driver", "org.apache.xerces.parsers.SAXParser"); //needed?
		
		initLog4J(host,argv);
		
		String testUrl = ((Constants.SERVER_SSL) ? "https://" : "http://") + Constants.SERVER_HOST + ":" + Constants.SERVER_PORT + "/admin/juwimm-cms-client.jnlp";
		try {
			URI desturi = new URI(testUrl);
			ProxyHelper helper = new ProxyHelper();
			helper.init(desturi, false);
	
		} catch (IllegalArgumentException ex) {
			logSys("could not initialize the proxy settings for host " + host + ": " + ex);
		} catch (URISyntaxException ex) {
			logSys("could not initialize the proxy settings because URI from host " + host + ": " + ex);
			// log.error("could not initialize the proxy settings because URI from host " + host + " : ", ex);
		}		

		try {
			UIManager.getLookAndFeelDefaults().put("ClassLoader", getClass().getClassLoader());
			LookAndFeel.switchTo(LookAndFeel.determineLookAndFeel());
		} catch (Exception exe) {
			log.error("Can't switch to Default LookAndFeel");
		}

		splash.setStatusInfo("Invoking Bean Framework...");
		Application.initializeContext();

		splash.setStatusInfo("Getting Locale Settings...");
		Constants.rb = ResourceBundle.getBundle("CMS", Constants.CMS_LOCALE);
		
		splash.setStatusInfo(Constants.rb.getString("splash.checkingSSL"));
		HttpClientWrapper httpClientWrapper = HttpClientWrapper.getInstance();
		
		try {
			httpClientWrapper.testAndConfigureConnection(testUrl);
		} catch (HttpException exe) {
			JOptionPane.showMessageDialog(null, exe.getMessage(), Constants.rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
		
		
		splash.setStatusInfo(Constants.rb.getString("splash.configBrowserSettings"));
		Browser.init(); // only needs to be called once.
		splash.setStatusInfo(Constants.rb.getString("splash.gettingTemplates"));
		comm = ((Communication) getBean(Beans.COMMUNICATION));		
		splash.setStatusInfo(Constants.rb.getString("splash.locadingLocalCachingDatabase"));
		comm.getDbHelper(); // check if there is already a programm running
		ActionHub.addActionListener(this);
		rb = Constants.rb;

		splash.setStatusInfo(Constants.rb.getString("splash.initUI"));
		try {
			this.getContentPane().setLayout(new BorderLayout());
			panLogin = new PanLogin();
			setCenterPanel(panLogin);
			Constants.CMS_CLIENT_VIEW = Constants.CLIENT_VIEW_LOGIN;
			screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
			screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
			frameWidth = Constants.CMS_SCREEN_WIDTH;
			frameHeight = Constants.CMS_SCREEN_HEIGHT;
			this.setSize(frameWidth, frameHeight);
			this.setLocationRelativeTo(null);
			this.setIconImage(UIConstants.CMS.getImage());
			this.setTitle(rb.getString("dialog.title") + " " + Constants.CMS_VERSION);
			this.addWindowListener(new MyWindowListener());
			this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

			if (screenHeight <= frameHeight && screenWidth <= frameWidth) {
				this.setExtendedState(MAXIMIZED_BOTH);
			}

			this.setVisible(true);
			// splash.disposeMe(true);
			splash.dispose();

			// unimportant stuff
			UIConstants.loadImages();

		} catch (Exception exe) {
			log.error("Tizzit will exit", exe);
			JOptionPane.showMessageDialog(this, exe.getMessage() + "\nCMS will exit.", "CMS", JOptionPane.ERROR_MESSAGE);
		} finally {
			//splash.disposeMe();
			splash.dispose();
		}
	}
	
	private void initLog4J(String host,String[] arguments){
		Properties prop = new Properties();
		String mailAppenderLog = initMailAppender(prop,arguments);
		String loggerLevel = "INFO";
		
		if ("true".equalsIgnoreCase(System.getProperty("debug"))) {
			loggerLevel = "DEBUG";
		}
		String log4jRootLogger = prop.getProperty("log4j.rootLogger");
		if(log4jRootLogger == null || log4jRootLogger.isEmpty()){
			prop.setProperty("log4j.rootLogger", "ERROR, STDOUT, CMSLOG");
		}else{
			prop.setProperty("log4j.rootLogger", "ERROR, STDOUT, CMSLOG, "+log4jRootLogger);
		}

		prop.setProperty("log4j.category.org.apache", "WARN");
		prop.setProperty("log4j.category.httpclient.wire", "WARN");
		prop.setProperty("log4j.category.de.juwimm", loggerLevel);
		prop.setProperty("log4j.category.org.apache.commons.httpclient", "ERROR");
		
		prop.setProperty("log4j.appender.STDOUT", "org.apache.log4j.ConsoleAppender");
		prop.setProperty("log4j.appender.STDOUT.layout", "org.apache.log4j.PatternLayout");
		prop.setProperty("log4j.appender.STDOUT.layout.ConversionPattern", "%d %-5p [%-16t] %c{1} - %m%n");

		prop.setProperty("log4j.appender.CMSLOG", "org.apache.log4j.RollingFileAppender");
		prop.setProperty("log4j.appender.CMSLOG.File", System.getProperty("user.home") + "/conquest_cms.log");
		prop.setProperty("log4j.appender.CMSLOG.MaxFileSize", "1024KB");
		prop.setProperty("log4j.appender.CMSLOG.MaxBackupIndex", "1");
		prop.setProperty("log4j.appender.CMSLOG.layout", "org.apache.log4j.PatternLayout");
		prop.setProperty("log4j.appender.CMSLOG.layout.ConversionPattern", "%d %-5p [%-16t] %c{1} - %m%n");
				
		PropertyConfigurator.configure(prop);
		log = Logger.getLogger(Main.class);
		log.debug(mailAppenderLog);
	}

	/**
	 * Transfer properties from arguments string to property object 
	 * @param prop will contain mail appender log properties
	 * @param arguments 
	 * @return message to be print in log 
	 */
	private String initMailAppender(Properties prop, String[] arguments) {
		if(arguments == null || arguments.length == 0){
			return "no mail appender log properties specified";
		}
		String log4jArguments = null;
		for(String argument:arguments){
			if(argument.contains(LOG4J_PROPERTIES_ARGUMENT)){
				log4jArguments = argument.replace(LOG4J_PROPERTIES_ARGUMENT+"=", "");
				log4jArguments = log4jArguments.replace("\"","");
				log4jArguments = log4jArguments.replace(";", "\n");
				break;
			}
		}
		
		if(log4jArguments == null || log4jArguments.isEmpty()){
			return "no mail appender log properties specified from server";
		}
		
		//loads all the log4j properties into Properties object
		//log4jArguments has format "v1=k1\nv2=k2\n ..."
		InputStream is = new ByteArrayInputStream(log4jArguments.getBytes());
		try {
			prop.load(is);			
		} catch (IOException e) {
			return "can not load mail appender log properties";
		}
		if(prop.entrySet().size() > 0){			
			prop.setProperty("log4j.appender.email", "org.apache.log4j.net.SMTPAppender");
			prop.setProperty("log4j.appender.email.bufferSize", "3");
			prop.setProperty("log4j.appender.email.layout", "org.apache.log4j.SimpleLayout");
			prop.setProperty("log4j.appender.email.subject", "CQ Richclient Errors");
			prop.setProperty("log4j.rootLogger", "email");
			return "mail appender log properties loaded";
		}else{
			return "no mail appender log properties specified from server";
		}
		
	}

	private void setCenterPanel(UnloadablePanel pan) {
		JPanel jpan = (JPanel) pan;
		if (pan != activePanel) {
			try {
				activePanel.unload();
			} catch (Exception exe) {
			}
			try {
				this.getContentPane().remove((JPanel) activePanel);
			} catch (Exception e) {
			}
			activePanel = pan;
			this.getContentPane().add(jpan, BorderLayout.CENTER);
		}
	}

	private void showAdminPanel() throws Exception {
		Constants.CMS_CLIENT_VIEW = Constants.CLIENT_VIEW_ADMIN;
		panRibbon.setView(false);
		panMenubar.setView(false);

		if (comm.isUserInRole(UserRights.SITE_ROOT)) {
			try {
				if (panRoot == null) {
					panRoot = new PanAdministrationRoot();
				}
				panRoot.reload();
			} catch (UserHasNoUnitsException ex) {
				JOptionPane.showMessageDialog(UIConstants.getMainFrame(), ex.getMessage(), rb.getString("msgbox.title.loginFailed"), JOptionPane.ERROR_MESSAGE);
				ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Constants.ACTION_LOGOFF));
				return;
			}
			setCenterPanel(panRoot);
		} else {
			try {
				if (panAdmin == null) {
					panAdmin = new PanAdministrationAdmin();
				}
				panAdmin.reload();
			} catch (UserHasNoUnitsException ex) {
				JOptionPane.showMessageDialog(UIConstants.getMainFrame(), ex.getMessage(), rb.getString("msgbox.title.loginFailed"), JOptionPane.ERROR_MESSAGE);
				ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Constants.ACTION_LOGOFF));
				return;
			}
			setCenterPanel(panAdmin);
		}
	}

	private void showToolPanel() throws Exception {
		if (Constants.CMS_CLIENT_VIEW != Constants.CLIENT_VIEW_CONTENT) {
			panRibbon.setView(true);
			panMenubar.setView(true);
			try {
				panTool = PanTool.getInstance();
				panTool.reload();
			} catch (UserHasNoUnitsException ex) {
				JOptionPane.showMessageDialog(UIConstants.getMainFrame(), ex.getMessage(), rb.getString("msgbox.title.loginFailed"), JOptionPane.ERROR_MESSAGE);
				ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Constants.ACTION_LOGOFF));
				return;
			} catch (NullPointerException npe) {
				JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("exception.SiteIsEmpty"), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
			}
			setCenterPanel(panTool);
		}
	}

	private void showTaskPanel() throws Exception {
		if (Constants.CMS_CLIENT_VIEW != Constants.CLIENT_VIEW_TASK) {
			panTool = PanTool.getInstance();
			setCenterPanel(panTool);
		}
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try {
			if (action.equals(Constants.ACTION_EXIT)) {
				if (ActionHub.fireExitPerformed(new ExitEvent())) {
					comm.getDbHelper().shutdown();
					System.exit(0);
				}
			} else if (action.equals(Constants.ACTION_LOGIN)) {
				Constants.CMS_CLIENT_VIEW = -1;
				setCenterPanel(PanInitView.getInstance());

				if (panMenubar != null) {
					ActionHub.removeActionListener(panMenubar);
				}
				panMenubar = new PanMenubar();
				panMenubar.addActionListener(comm);
				ActionHub.addActionListener(panMenubar);
//				/this.setJMenuBar(panMenubar);

				if (panToolbar == null) {
					panToolbar = new PanToolbar(comm);
					panStatusbar = new PanStatusbar();
					UIConstants.setStatusLine(panStatusbar);
					ActionHub.addActionListener(panToolbar);
					ActionHub.addActionListener(panStatusbar);
				}
				
				if(panRibbon == null){
					this.setLayout(new BorderLayout());
					panRibbon = new PanRibbon(this,comm);
					ActionHub.addActionListener(panRibbon);
				}

				ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Constants.ACTION_VIEW_EDITOR));

				//this.getJMenuBar().setVisible(true);				
				//this.getContentPane().add(panToolbar, BorderLayout.NORTH);								
				this.getContentPane().add(panStatusbar, BorderLayout.SOUTH);
				
				this.getRootPane().setDoubleBuffered(true);
				this.getRootPane().validate();
				this.getRootPane().repaint();
			} else if (action.equals(Constants.ACTION_LOGOFF)) {
				if (ActionHub.fireExitPerformed(new ExitEvent())) {
					comm.setLoggedIn(false);
					Application.initializeContext();
					panLogin = new PanLogin();
					panRoot = null;
					panAdmin = null;
					this.getContentPane().removeAll();
					//this.getJMenuBar().setVisible(false);
					comm.getDbHelper().autoEmptyCache();
					PanContentView.getInstance().unloadAll();
					setCenterPanel(panLogin);
					Constants.CMS_CLIENT_VIEW = Constants.CLIENT_VIEW_LOGIN;
					this.validate();
					this.getContentPane().validate();
					this.getContentPane().repaint();
					this.repaint();
					panLogin.init();
				}
			} else if (action.equals(Constants.ACTION_VIEW_EDITOR)) {
				showToolPanel();
				panTool = PanTool.getInstance();
				Constants.CMS_CLIENT_VIEW = Constants.CLIENT_VIEW_CONTENT;
			} else if (action.equals(Constants.ACTION_VIEW_ADMIN) || action.equals(Constants.ACTION_VIEW_ROOT)) {
				Constants.CMS_CLIENT_VIEW = Constants.CLIENT_VIEW_ADMIN;
				showAdminPanel();
			} else if (action.equals(Constants.ACTION_CHANGE_PASSWORD)) {
				showChangePasswordDialog();
			} else if (action.equals(Constants.ACTION_SHOW_TASK)) {
				showTaskPanel();
				Constants.CMS_CLIENT_VIEW = Constants.CLIENT_VIEW_TASK;
			} else if (action.equals(Constants.ACTION_SHOW_CONTENT)) {
				Constants.CMS_CLIENT_VIEW = Constants.CLIENT_VIEW_CONTENT;
			}
		} catch (Exception exe) {
			log.error("Error in actionPerformed", exe);
		} finally {
			this.setCursor(Cursor.getDefaultCursor());
		}
	}

	private void showChangePasswordDialog() {
		PasswordDialog dialog = new PasswordDialog(comm.getUser().getUserName());
		dialog.setVisible(true);
	}
	
	public static void showProxies() {
		StringBuffer text = new StringBuffer();
		final String crlf = System.getProperty("line.separator");
		text.append("http.proxyUser=" + System.getProperty("http.proxyUser")).append(crlf);
		text.append("http.proxySet=" + System.getProperty("http.proxySet")).append(crlf);
		text.append("http.proxyHost=" + System.getProperty("http.proxyHost")).append(crlf);
		text.append("http.proxyPort=" + System.getProperty("http.proxyPort")).append(crlf);
		
		text.append("https.proxySet=" + System.getProperty("https.proxySet")).append(crlf);
		text.append("https.proxyHost=" + System.getProperty("https.proxyHost")).append(crlf);
		text.append("https.proxyPort=" + System.getProperty("https.proxyPort")).append(crlf);
		
		text.append("proxySet=" + System.getProperty("proxySet")).append(crlf);
		text.append("proxyHost=" + System.getProperty("proxyHost")).append(crlf);
		text.append("proxyPort=" + System.getProperty("proxyPort")).append(crlf);
		log.info(text.toString());
		JOptionPane.showMessageDialog(null, text.toString(), "Proxy-Info", JOptionPane.INFORMATION_MESSAGE);
	}

}
