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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class TreeRenderer extends DefaultTreeCellRenderer {
	private JPanel panel;
	private JCheckBox box;

	public TreeRenderer() {
		panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		box = new JCheckBox();
		box.setBackground(Color.WHITE);
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		try {
			MyTreeNode n = (MyTreeNode) value;
			boolean s = n.getCheckBox();
			box.setSelected(s);
			box.setText(value.toString());
			panel.add(box, BorderLayout.CENTER);

			return panel;
		} catch (Exception ex) {
		}

		return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
	}

	public JPanel getPanel() {
		return panel;
	}

}