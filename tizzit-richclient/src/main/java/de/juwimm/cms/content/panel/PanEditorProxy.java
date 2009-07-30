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
/**
 * 
 */
package de.juwimm.cms.content.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusListener;
import java.awt.event.MouseListener;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * A proxy for the WYSIWYG editor that displays the HTML content until the user choses to
 * actually edit the content (by clicking inside this panel).
 * 
 * @author <a href="mailto:christiane.hausleiter@juwimm.com">Christiane Hausleiter</a>
 *
 */
public class PanEditorProxy extends JPanel {
	
	private JEditorPane editorPane = null;
	private JScrollPane scrollPane = null;
	
	public PanEditorProxy() {
		super(new BorderLayout());
		this.editorPane = new JEditorPane();
		this.editorPane.setEditable(false);
		this.scrollPane = new JScrollPane(this.editorPane);
		this.scrollPane.setDoubleBuffered(true);
		setPreferredSize(new Dimension(600, 400));
		this.add(this.scrollPane, BorderLayout.CENTER);
	}
	
	/** 
	 * Delegates the listener registration to the editor panel.
	 * 
	 * @see java.awt.Component#addMouseListener(java.awt.event.MouseListener) */
	public void addMouseListener(MouseListener ml) {
		this.editorPane.addMouseListener(ml);
	}
	
	/**
	 * Delegates the listener registration to the editor panel. 
	 * 
	 * @see java.awt.Component#addFocusListener(java.awt.event.FocusListener) */
	public void addFocusListener(FocusListener fl) {
		this.editorPane.addFocusListener(fl);
	}
	
	public void initialize(String text) {
		this.editorPane.setText(null);
		this.editorPane.setContentType("text/html; charset=UTF-8");
		this.editorPane.setEditable(false);
		this.editorPane.setText(text);
		this.editorPane.setCaretPosition(0);
	}
	
}
