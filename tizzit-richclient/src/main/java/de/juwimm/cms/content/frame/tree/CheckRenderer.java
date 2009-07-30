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
package de.juwimm.cms.content.frame.tree;

import java.awt.*;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.TreeCellRenderer;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class CheckRenderer extends JPanel implements TreeCellRenderer {
	private JCheckBox check  = new JCheckBox();
	private TreeLabel label = new TreeLabel();

	public CheckRenderer() {
		setLayout(null);
		add(check);
		add(label);
		check.setBackground(Color.white);
		this.setBackground(Color.white);
	}

	public Rectangle getBounds() {
		return check.getBounds();
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		String stringValue = tree.convertValueToText(value, isSelected, expanded, leaf, row, hasFocus);
		if (!((CheckNode) value).isTmpCheckSel()) {
			setEnabled(tree.isEnabled());
			label.setSelected(isSelected);
			label.setFocus(hasFocus);
		}
		check.setSelected(((CheckNode) value).isCheckSelected());
		label.setFont(tree.getFont());
		label.setText(stringValue);

		if (isSelected) {
			label.setForeground(Color.WHITE);
		} else {
			label.setForeground(Color.BLACK);
		}

		String ttt = ((CheckNode) value).getToolTipText();
		this.setToolTipText(ttt);
		this.label.setToolTipText(ttt);
		this.check.setToolTipText(ttt);

		label.setIcon(((CheckNode) value).getIcon());
		return this;
	}

	public Dimension getPreferredSize() {
		Dimension dimCheck = check.getPreferredSize();
		Dimension dimLbl = label.getPreferredSize();

		return new Dimension(dimCheck.width + dimLbl.width, (dimCheck.height < dimLbl.height ? dimLbl.height
				: dimCheck.height));
	}

	public void doLayout() {
		Dimension dimCheck = check.getPreferredSize();
		Dimension dimLbl = label.getPreferredSize();
		int yCheck = 0;
		int yLabel = 0;

		if (dimCheck.height < dimLbl.height) {
			yCheck = (dimLbl.height - dimCheck.height) / 2;
		} else {
			yLabel = (dimCheck.height - dimLbl.height) / 2;
		}

		check.setLocation(0, yCheck);
		check.setBounds(0, yCheck, dimCheck.width, dimCheck.height);
		label.setLocation(dimCheck.width, yLabel);
		label.setBounds(dimCheck.width, yLabel, dimLbl.width, dimLbl.height);
	}

	public void setBackground(Color color) {
		if (color instanceof ColorUIResource) color = null;
		super.setBackground(color);
	}
	
	/**
	 * 
	 */
	private class TreeLabel extends JLabel {
		private boolean isSelected;
		private boolean hasFocus;

		public TreeLabel() {
			this.setBackground(Color.white);
		}

		public void setBackground(Color color) {
			if (color instanceof ColorUIResource) color = null;
			super.setBackground(color);
		}

		public void paint(Graphics g) {
			String str;
			if ((str = getText()) != null) {
				if (0 < str.length()) {
					if (isSelected) {
						g.setColor(UIManager.getColor("Tree.selectionBackground"));
					} else {
						g.setColor(Color.white);
					}
					Dimension d = getPreferredSize();
					int imageOffset = 0;
					Icon currentI = getIcon();
					if (currentI != null) {
						imageOffset = currentI.getIconWidth() + Math.max(0, getIconTextGap() - 1);
					}
					g.fillRect(imageOffset, 0, d.width - 1 - imageOffset, d.height);
					if (hasFocus) {
						g.setColor(UIManager.getColor("Tree.selectionBorderColor"));
						g.drawRect(imageOffset, 0, d.width - 1 - imageOffset, d.height - 1);
					}
				}
			}
			super.paint(g);
		}

		public Dimension getPreferredSize() {
			Dimension retDimension = super.getPreferredSize();
			if (retDimension != null) {
				retDimension = new Dimension(retDimension.width + 3, retDimension.height);
			}
			return retDimension;
		}

		void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
		}

		void setFocus(boolean hasFocus) {
			this.hasFocus = hasFocus;
		}
	}
}