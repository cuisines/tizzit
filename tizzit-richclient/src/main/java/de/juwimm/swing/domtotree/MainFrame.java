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
package de.juwimm.swing.domtotree;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: JuwiMacMillanGroup</p>
 * @author Michael Meyer
 * @version 1.0
 */
public class MainFrame extends JFrame {
	private JPanel contentPane;
	private BorderLayout borderLayout1 = new BorderLayout();
	private TreeApplet treeapplet;

	//Construct the frame
	public MainFrame() {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			jbInit();
			myChanges();
		} catch (Exception e) {

		}
	}

	//Component initialization
	private void jbInit() throws Exception {
		contentPane = (JPanel) this.getContentPane();
		borderLayout1.setHgap(10);
		borderLayout1.setVgap(10);
		contentPane.setLayout(borderLayout1);
		this.setSize(new Dimension(400, 300));
		this.setTitle("Frame Title");
	}

	//Overridden so we can exit when window is closed
	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			System.exit(0);
		}
	}

	private void myChanges() {
		DocbookParser dp = new DocbookParser("standards_book.xml", "title", Color.white);
		if (dp.convertFileToDom()) {
			dp.setStandardTag("role", "standard");
			dp.start();
			dp.removeAllStandards();
			treeapplet = new TreeApplet();
			treeapplet.setTreeModel(dp.getTreeModel(), new Color(175, 175, 175), Color.white);
			this.getContentPane().add(treeapplet, BorderLayout.CENTER);
		}
	}
}