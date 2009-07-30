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
package de.juwimm.cms.plugins.client.testcontainer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;

import org.apache.log4j.Logger;
import org.w3c.dom.*;

import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.modules.Module;
import de.juwimm.util.XercesHelper;

public final class ConquestPanel extends JPanel {
	
	private static Logger logger = Logger.getLogger(ConquestPanel.class);
	
	private String jarpath = null; 
	private String dcfpath = null;
	
	private JButton checkoutButton = new JButton("Checkout");
	private JButton checkinButton  = new JButton("Checkin");
		
	private Module plugin = null;
		
	private ShowContentFrame showContent = new ShowContentFrame();
		
	public ConquestPanel() {
		this.initialize();
		ContentManager manager = new ContentManager();
	}
	
	public void configurePlugin(String jarpath, String dcfpath) {
		this.jarpath = jarpath;
		this.dcfpath = dcfpath;
	}
	
	public boolean loadPlugin() {
		boolean load = false;
		try {
			File dcfFile = new File(this.dcfpath);
			if(dcfFile.exists()) {
				Document dcfDoc = XercesHelper.file2Dom(dcfFile);
				String pluginclass = this.getPluginClassname(dcfDoc);
				String[] jarList = this.getJarfiles(dcfDoc);				
				
				URL[] url = new URL[jarList.length];
				for(int i=0; i<url.length; i++) {
					File f = new File(jarList[i]);
					url[i] = f.toURL();
				}
				URLClassLoader cl = new URLClassLoader(url , this.getClass().getClassLoader());
				Class c = cl.loadClass(pluginclass);
				plugin = (Module) c.newInstance();
				plugin.setEnabled(false);
				JPanel pluginPanel = plugin.viewPanelUI();
				this.add(pluginPanel,BorderLayout.CENTER);
				load = true;
			} else {
				throw new Exception("FILE DOES NOT EXIST");
			}
		} catch (Exception ex) {
			logger.warn("CANNOT LOAD PLUGIN " + ex.getMessage());
		}
		return load;
	}
	
	private String getJarDir() {
		String pathSeparator = System.getProperty("file.separator");
		int index = this.jarpath.lastIndexOf(pathSeparator);
		String sub = this.jarpath.substring(0,index+1);
		
		return sub;
	}
	
/*	private String getFilename() {
		
		String pathSeparator = System.getProperty("file.separator");
		int index = this.dcfpath.lastIndexOf(pathSeparator);
		
		String filename = this.dcfpath.substring(index+1);
		
		return filename;
		
	}
	
	private String getNamespace() {
		
		String filename = this.getFilename();
				
		int pointIndex = filename.indexOf(".");
		
		StringBuffer buf = new StringBuffer(filename.substring(0,pointIndex));
		buf.append("Plugin");
		
		return buf.toString();
	} */
	
	private String getPluginClassname(Document dcfDoc) {
			
		String classname = "";		
		
		NodeList list = dcfDoc.getElementsByTagName("classname");
		for(int i=0; i<list.getLength(); i++) {
			Node tempNode = list.item(i);
			classname = tempNode.getFirstChild().getNodeValue();
		}		
			
		return classname;
	}
	
	private String[] getJarfiles(Document dcfDoc) {
		
		ArrayList<String> list = new ArrayList<String>();
		
		NodeList classpathList = dcfDoc.getElementsByTagName("classpath");
		
		for(int i=0; i<classpathList.getLength(); i++) {
			Node classpathNode = classpathList.item(i);
			NodeList jarList = classpathNode.getChildNodes();
			for(int x=0; x < jarList.getLength(); x++) {
				Node jarNode = jarList.item(x);
				if(jarNode.getNodeType() == Node.ELEMENT_NODE) {
					String val = jarNode.getFirstChild().getNodeValue();					
					list.add(val);
				}
			}
		}
		
		String[] jarArray = new String[list.size()];
					
		String jarDir = this.getJarDir();
		
		for(int i=0; i<list.size(); i++) {
			String jarFile = list.get(i);
			StringBuffer buf = new StringBuffer(jarDir);
			buf.append(jarFile);		
			jarArray[i] = buf.toString();
		}
		
		return jarArray;
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.setBorder(BasicBorders.getMenuBarBorder());
		this.add(buttonPanel,BorderLayout.NORTH);
		checkinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Node node = plugin.getProperties();
				String str = XercesHelper.node2string(node);
				if(!showContent.isVisible()) {
					showContent.setVisible(true);
				}
				showContent.setContent(str);
				plugin.setEnabled(false);
				/* checken ob der Content richtig ist */
		/*		plugin.setProperties(node);
				Node checkNode = plugin.getProperties();
				if(!node.equals(checkNode)) {
					JOptionPane.showMessageDialog(null,"The Content does not fit","Content Check",JOptionPane.OK_OPTION);
				} else {
					JOptionPane.showMessageDialog(null,"The Content is OK","Content Check",JOptionPane.OK_OPTION);
				}*/
			}	
		});
		checkoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				plugin.setEnabled(true);
			}	
		});
		buttonPanel.add(checkoutButton);
		buttonPanel.add(checkinButton);
	}
	
	class ShowContentFrame extends JFrame {
		
		JTextArea area = new JTextArea();
		
		public ShowContentFrame() {
			super();
			this.setSize(300,300);
			this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			this.getRootPane().setLayout(new BorderLayout());
		
			this.getRootPane().add(area,BorderLayout.CENTER);
		}
		
		public void setContent(String content) {
			area.setText(content);
		}
		
	}

}
