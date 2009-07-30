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
package de.juwimm.cms.content.panel;

import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.*;

/**
 * An abstract implementation of the {@link TreePanel} interface.<br>
 * Two remaining methods must be implented by all extending classes:
 * {@link #setCheckHash(Hashtable)} and {@link #setFieldsEditable(boolean)}.
 * 
 * <p>Title: juwimm cms</p>
 * <p><b>Copyright: JuwiMacMillan Group GmbH (c) 2002</b></p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public abstract class AbstractTreePanel extends JPanel implements TreePanel {
	
	private EventListenerList changelistener = new EventListenerList();
	private ChangedDocumentListener docListener = new ChangedDocumentListener();
	private CheckActionListener checkListener = new CheckActionListener();
	private Hashtable checkhash = new Hashtable();

	/** @see de.juwimm.cms.content.panel.TreePanel#setFieldsEditable(boolean) */
	public abstract void setFieldsEditable(boolean editable);
	
	/** @see de.juwimm.cms.content.panel.TreePanel#updateCheckHash() */
	public abstract void updateCheckHash();

	/** @see de.juwimm.cms.content.panel.TreePanel#getCheckHash() */
	public Hashtable getCheckHash() {
		return this.checkhash;
	}

	/** @see de.juwimm.cms.content.panel.TreePanel#setCheckHash(java.util.Hashtable) */
	public void setCheckHash(Hashtable ch) {
		this.checkhash = ch;
		// TODO [CH] check if fireChangeListener() is still needed... a new Hashtable only means the visibilities have changed, not the content!		
		//fireChangeListener();
	}
	
	/** @see de.juwimm.cms.content.panel.TreePanel#setAllChecksEnabled(boolean) */
	public void setAllChecksEnabled(boolean enabled) {
		setAllElementsEnabled(getComponents(), enabled);
	}
	
	/** @see de.juwimm.cms.content.panel.TreePanel#addChangeListener(javax.swing.event.ChangeListener) */
	public void addChangeListener(ChangeListener cl) {
		changelistener.add(ChangeListener.class, cl);
	}

	/** @see de.juwimm.cms.content.panel.TreePanel#removeAllChangeListener() */
	public void removeAllChangeListener() {
		changelistener = new EventListenerList();
	}

	/**
	 * Returns the ChangedDocumentListener Object for using inside the Panel.
	 * 
	 * @return
	 */
	public ChangedDocumentListener getChangedDocumentListener() {
		return this.docListener;
	}

	/**
	 * Returns the {@link CheckActionListener} object that should be registered for all action events 
	 * on check boxes inside this panel.
	 * 
	 * @return the {@code CheckActionListener} object
	 */
	public CheckActionListener getCheckActionListener() {
		return this.checkListener;
	}

	/**
	 * Returns true if this Panel contains any Clicked Fields.
	 * @return
	 */
	public boolean hasClicks() {
		boolean ret = false;
		if (this.checkhash != null) {
			java.util.Iterator it = this.checkhash.values().iterator();
			while (it.hasNext()) {
				if (((Integer) it.next()).equals(new Integer(1))) {
					ret = true;
					break;
				}
			}
		}
		return ret;
	}



	/**
	 * Internal Helper-Procedure for setAllChecks
	 * @param comparr
	 * @param enabled
	 */
	private void setAllElementsEnabled(java.awt.Component[] comparr, boolean enabled) {
		if (comparr != null) {
			for (int i = 0; i < comparr.length; i++) {
				try {
					if (comparr[i] != null) {
						if ((comparr[i] instanceof javax.swing.JCheckBox)) {
							JComponent comp = (JComponent) comparr[i];
							comp.setEnabled(enabled);
							if (!enabled) {
								((javax.swing.JCheckBox) comp).setSelected(false);
							}
						} else {
							JComponent comp = (JComponent) comparr[i];
							setAllElementsEnabled(comp.getComponents(), enabled);
						}
					}
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * Returns a boolean value reflecting whether the Checkbox for the specified name
	 * should be checked or not.
	 * @param name the String used as key inside the {@code Hashtable}
	 * @return a boolean indicating whether the checkbox should be checked
	 */
	public boolean getCheckValueForName(String name) {
		if (this.checkhash.get(name) != null) {
			if (this.checkhash.get(name).equals(new Integer(1))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Fires the Event for calling all ChangeListener.
	 * FIXME [CH] die Listener für die Entscheidung: Datenbank-Speicherung notwendig?
	 */
	public void fireChangeListener() {
		ChangeEvent ce = new ChangeEvent(this);
		Object[] listeners = changelistener.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			((ChangeListener) listeners[i + 1]).stateChanged(ce);
		}
	}
	
	/**
	 * Observer for content changes. 
	 * Notifies the listener that a database update is needed.
	 * 
	 * @author <a href="mailto:christiane.hausleiter@juwimm.com">Christiane Hausleiter</a>
	 *
	 */
	public class ChangedDocumentListener implements DocumentListener {
		
		public void changedUpdate(DocumentEvent de) {
			fireChangeListener();
		}

		public void insertUpdate(DocumentEvent de) {
			fireChangeListener();
		}

		public void removeUpdate(DocumentEvent de) {
			fireChangeListener();
		}
	}


	/**
	 * An {@link ActionListener} implementation that listens for registering 
	 * checking and unchecking events on check boxes within this panel.
	 * 
	 * @author <a href="mailto:christiane.hausleiter@juwimm.com">Christiane Hausleiter</a>
	 *
	 */
	public class CheckActionListener implements ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent ae) {
			updateCheckHash();
		}
	}
}