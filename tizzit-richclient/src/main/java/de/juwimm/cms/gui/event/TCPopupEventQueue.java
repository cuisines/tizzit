package de.juwimm.cms.gui.event;

import static de.juwimm.cms.common.Constants.rb;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

public class TCPopupEventQueue extends EventQueue {
	public JPopupMenu popup;
	JTable table;
	public BasicAction cut, copy, paste, selectAll;

	public TCPopupEventQueue() {
	}

	public void createPopupMenu(JTextComponent text) {
		cut = new CutAction(rb.getString("event.tcpopupevent.cut"), null);
		copy = new CopyAction(rb.getString("event.tcpopupevent.copy"), null);
		paste = new PasteAction(rb.getString("event.tcpopupevent.paste"), null);
		selectAll = new SelectAllAction(rb.getString("event.tcpopupevent.selectAll"), null);
		cut.setTextComponent(text);
		copy.setTextComponent(text);
		paste.setTextComponent(text);
		selectAll.setTextComponent(text);

		popup = new JPopupMenu();
		popup.add(cut);
		popup.add(copy);
		popup.add(paste);
		popup.addSeparator();
		popup.add(selectAll);
	}

	public void showPopup(Component parent, MouseEvent me) {
		popup.validate();
		popup.show(parent, me.getX(), me.getY());
	}

	protected void dispatchEvent(AWTEvent event) {
		super.dispatchEvent(event);
		if (!(event instanceof MouseEvent)) { return; }
		MouseEvent me = (MouseEvent) event;
		if (!me.isPopupTrigger()) { return; }
		if (!(me.getSource() instanceof Component)) { return; }
		Component comp = SwingUtilities.getDeepestComponentAt((Component) me.getSource(), me.getX(), me.getY());
		if (!(comp instanceof JTextComponent)) { return; }
		if (MenuSelectionManager.defaultManager().getSelectedPath().length > 0) { return; }
		createPopupMenu((JTextComponent) comp);
		showPopup((Component) me.getSource(), me);
	}
}

abstract class BasicAction extends AbstractAction {
	JTextComponent comp;

	public BasicAction(String text, Icon icon) {
		super(text, icon);
		putValue(Action.SHORT_DESCRIPTION, text);
	}

	public void setTextComponent(JTextComponent comp) {
		this.comp = comp;
	}

	public abstract void actionPerformed(ActionEvent e);
}

class CutAction extends BasicAction {
	public CutAction(String text, Icon icon) {
		super(text, icon);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl X"));
	}

	public void actionPerformed(ActionEvent e) {
		comp.cut();
	}

	public boolean isEnabled() {
		return comp != null && comp.isEditable() && comp.getSelectedText() != null;
	}
}

class CopyAction extends BasicAction {
	public CopyAction(String text, Icon icon) {
		super(text, icon);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl C"));
	}

	public void actionPerformed(ActionEvent e) {
		comp.copy();
	}

	public boolean isEnabled() {
		return comp != null && comp.getSelectedText() != null;
	}
}

class PasteAction extends BasicAction {
	public PasteAction(String text, Icon icon) {
		super(text, icon);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl V"));
	}

	public void actionPerformed(ActionEvent e) {
		comp.paste();
	}

	public boolean isEnabled() {
		Transferable content = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		return comp != null && comp.isEnabled() && comp.isEditable() && content.isDataFlavorSupported(DataFlavor.stringFlavor);
	}
}

class SelectAllAction extends BasicAction {
	public SelectAllAction(String text, Icon icon) {
		super(text, icon);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl A"));
	}

	public void actionPerformed(ActionEvent e) {
		comp.selectAll();
	}

	public boolean isEnabled() {
		return comp != null && comp.isEnabled() && comp.getText().length() > 0 && (comp.getSelectedText() == null || comp.getSelectedText().length() < comp.getText().length());
	}
}
