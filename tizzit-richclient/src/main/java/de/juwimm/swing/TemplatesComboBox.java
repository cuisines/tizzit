package de.juwimm.swing;

import java.awt.Component;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

public class TemplatesComboBox extends JComboBox {
	public TemplatesComboBox() {
		super();
		super.setRenderer(new TemplatesRenderer());
	}

	private Set categories = new HashSet();

	public void addItem(Object anObject) {
		super.addItem(anObject);
		DropDownHolder holder = (DropDownHolder) anObject;
		if (holder.getType().equals(DropDownHolder.ELEMENT_TYPE_CATEGORY)) {
			categories.add(getItemCount() - 1);
		}
	}

	@Override
	public void removeAllItems() {
		super.removeAllItems();
		categories = new HashSet();
	}

	@Override
	public void removeItemAt(final int anIndex) {
		super.removeItemAt(anIndex);
		categories.remove(anIndex);
	}

	@Override
	public void removeItem(final Object anObject) {
		for (int i = 0; i < getItemCount(); i++) {
			if (getItemAt(i) == anObject) {
				categories.remove(i);
			}
		}
		super.removeItem(anObject);
	}

	@Override
	public void setSelectedIndex(int index) {
		if (!categories.contains(index)) {
			super.setSelectedIndex(index);
		}
	}

	private class TemplatesRenderer extends BasicComboBoxRenderer {

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {

			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			if (categories.contains(index)) {
				setBackground(list.getBackground());
				setForeground(UIManager.getColor("Label.disabledForeground"));
			}
			setFont(list.getFont());
			setText((value == null) ? "" : value.toString());
			return this;
		}
	}
}
