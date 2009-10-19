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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

public class TestContainerFrame {

	private ConfigurePanel configurePanel = null;
	private JFrame fr = null;	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestContainerFrame();

	}
	
	public TestContainerFrame() {
		fr = new JFrame();
		fr.setSize(650,400);
		fr.getRootPane().setLayout(new BorderLayout());
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		configurePanel = new ConfigurePanel();
		fr.getRootPane().add(configurePanel,BorderLayout.CENTER);
		
		fr.setVisible(true);
	}
	
	private void loadPlugin(String jarpath, String dcfpath) {
		TizzitPanel tizzit = new TizzitPanel();
		tizzit.configurePlugin(jarpath,dcfpath);
		tizzit.loadPlugin();
		fr.getRootPane().remove(configurePanel);
		fr.getRootPane().add(tizzit,BorderLayout.CENTER);
		fr.getRootPane().updateUI();
	}
	
	class ConfigurePanel extends JPanel implements ActionListener {
		
		private JButton loadPluginButton = new JButton("Load");
		
		private JTextField filePath = null;
		private JTextField dcfPath = null;
		
		private boolean selectedFile = false;
		private boolean selectedJar  = false;
		
		public ConfigurePanel() {
			this.setLayout(new GridBagLayout());
			
			JLabel lab1 = new JLabel("Path to Jarfile");
			JLabel lab2 = new JLabel("Path to DCF");
			this.add(lab1,this.getGridBagConstraint(0,1));
			this.add(lab2,this.getGridBagConstraint(0,2));
			
			filePath = new JTextField();
			filePath.setColumns(40);
			dcfPath = new JTextField();
			dcfPath.setColumns(40);
			this.add(filePath,this.getGridBagConstraint(1,1));
			this.add(dcfPath,this.getGridBagConstraint(1,2));
			
			JButton fileButton = new JButton("Choose");
			fileButton.setActionCommand("0");
			fileButton.addActionListener(this);
			this.add(fileButton,this.getGridBagConstraint(2,1));
			
			JButton dcfButton = new JButton("Choose");
			dcfButton.setActionCommand("1");
			dcfButton.addActionListener(this);
			this.add(dcfButton,this.getGridBagConstraint(2,2));
			
			loadPluginButton.setEnabled(false);
			loadPluginButton.addActionListener(new ActionListener() {				
				public void actionPerformed(ActionEvent e) {
					loadPlugin(filePath.getText(),dcfPath.getText());
				}				
			});
			this.add(loadPluginButton,this.getGridBagConstraint(0,4));
			
		}	
		
		
		private GridBagConstraints getGridBagConstraint(final int x, final int y) {
			GridBagConstraints c = new GridBagConstraints();
			Insets insets = new Insets(5,5,5,5);
			c.gridx = x;
			c.gridy = y;
			c.insets = insets;
			c.anchor = GridBagConstraints.NORTH; 
			
			return c;
		}

		public void actionPerformed(ActionEvent arg0) {
			String command = arg0.getActionCommand();
			
			try {
				int sw = new Integer(command).intValue();
				JFileChooser choose = new JFileChooser();
				MyFileFilter filter = new MyFileFilter();
				if(sw == 0) {
					filter.setExtension("jar");
				} else {
					filter.setExtension("xml");
				}
				choose.setFileFilter(filter);
				int retVal = choose.showDialog(this,"OK");
				if(retVal == JFileChooser.APPROVE_OPTION) {
					File file = choose.getSelectedFile();
					choose.setVisible(false);
					String path = file.getAbsolutePath();
					if(sw == 0) {
						this.filePath.setText(path);
						this.selectedJar = true;
					} else {
						this.dcfPath.setText(path);
						this.selectedFile = true;
					}
				}
				
			} catch (Exception ex) {
				
			}
			
			if(selectedJar && selectedFile) {
				loadPluginButton.setEnabled(true);
			}
		}
		
		class MyFileFilter extends FileFilter {
			
			String extension = null;
			
			public void setExtension(String ext) {
				this.extension = ext.toLowerCase();
			}

			@Override
			public boolean accept(File arg0) {
				boolean accept = false;
				
				if(arg0.isDirectory()) {
					accept = true;
				} else if (arg0.isFile()) {
					String name = arg0.getName();
					int index = name.lastIndexOf(".");
					String sub = name.substring(index+1).toLowerCase();
					if(sub.equals(this.extension)) {
						accept = true;
					}
				}
				
				return accept;
			}

			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return null;
			}
			
		}
		
		
		
		
	}
	
	

}
