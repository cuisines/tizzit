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
import javax.swing.tree.DefaultTreeCellEditor;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class TreeEditor extends DefaultTreeCellEditor {
	private JCheckBox box = new JCheckBox();
	private JPanel panel = new JPanel();

	public TreeEditor(JTree tr, TreeRenderer r) {
		super(tr, r);

	}

	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded,
			boolean leaf, int row) {
		try {
			MyTreeNode node = (MyTreeNode) value;
			boolean sel = node.setCheckBox();
			box.setSelected(sel);
			box.setBackground(Color.LIGHT_GRAY);
			box.setText(value.toString());
			panel.add(box, BorderLayout.CENTER);
			panel.setBackground(Color.BLUE);
			return box;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		return super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);
	}
}