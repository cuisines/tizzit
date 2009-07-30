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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * <p>Title: DomToTree</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: JuwiMacMillan</p>
 * @author Michael Meyer
 * @version 1.0
 */
public class ColorTreeRenderer extends DefaultTreeCellRenderer {
	private JPanel panel;
	private JLabel label;
	private Color click;

	/**
	 * Constructor.<p>
	 * This TreeRenderer will render your Nodes with two different colors.
	 *
	 * @param normal Color when the node is not selected
	 * @param click Color when the node is selected
	 */

	public ColorTreeRenderer(Color normal, Color click) {
		panel = new JPanel(new BorderLayout());
		label = new JLabel();
		label.setBackground(normal);
		panel.setBackground(normal);
		panel.add(label, BorderLayout.CENTER);
		this.click = click;
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {

		try {
			MyTreeNode n = (MyTreeNode) value;
			label.setText(value.toString());
			panel.add(label, BorderLayout.CENTER);
			panel.setBackground(n.getColor());
			label.setBackground(n.getColor());
			if (n.getStandard() && (!(n.equals(AktColorNode.getColorNode())))) {
				panel.setBackground(new Color(240, 240, 240));
			}
			return panel;
		} catch (Exception ex) {

		}

		return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
	}

}