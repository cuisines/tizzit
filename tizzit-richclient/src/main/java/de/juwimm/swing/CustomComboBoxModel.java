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
package de.juwimm.swing;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 * 
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class CustomComboBoxModel extends AbstractListModel implements ComboBoxModel, Serializable {
	private Vector objects;
	private Object selectedObject;
	private String methodName;

	/**
	 * Constructs a DefaultComboBoxModel object initialized with
	 * an array of objects.
	 *
	 * @param items  an array of Object objects
	 */
	public CustomComboBoxModel(final Object[] items, String methodName) {
		this.methodName = methodName;
		objects = new Vector();
		objects.ensureCapacity(items.length);

		int i, c;
		for (i = 0, c = items.length; i < c; i++) {
			try {
				objects.addElement(new DropDownHolder(items[i], (String) items[i].getClass().getMethod(methodName,
						new Class[0]).invoke(items[i], new Object[0])));
			} catch (Exception exe) {
			}
		}
		if (getSize() > 0) {
			selectedObject = getElementAt(0);
		}
	}

	public CustomComboBoxModel(final Object[] items, String methodName, String methodTwo) {
		this.methodName = methodName;
		objects = new Vector();
		objects.ensureCapacity(items.length);

		int i, c;
		for (i = 0, c = items.length; i < c; i++) {
			try {
				String toString = "";
				String one = (String) items[i].getClass().getMethod(methodName, new Class[0]).invoke(items[i],
						new Object[0]);
				String two = (String) items[i].getClass().getMethod(methodTwo, new Class[0]).invoke(items[i],
						new Object[0]);
				if (two.equals("")) {
					toString = one;
				} else {
					toString = one + ", " + two;
				}
				objects.addElement(new DropDownHolder(items[i], toString));
			} catch (Exception exe) {
			}
		}
		if (getSize() > 0) {
			selectedObject = getElementAt(0);
		}
	}

	public CustomComboBoxModel(Vector v, String methodName, String methodTwo) {
		this.methodName = methodName;
		Iterator it = v.iterator();
		objects = new Vector(v.size());
		while (it.hasNext()) {
			Object item = it.next();
			try {
				String toString = "";
				String one = (String) item.getClass().getMethod(methodName, new Class[0]).invoke(item, new Object[0]);
				String two = (String) item.getClass().getMethod(methodTwo, new Class[0]).invoke(item, new Object[0]);
				if (two.equals("")) {
					toString = one;
				} else {
					toString = one + ", " + two;
				}
				objects.addElement(new DropDownHolder(item, toString));
			} catch (Exception exe) {
			}
		}
		if (getSize() > 0) {
			selectedObject = getElementAt(0);
		}
	}

	public CustomComboBoxModel(Vector v, String methodName) {
		this.methodName = methodName;
		Iterator it = v.iterator();
		objects = new Vector(v.size());
		while (it.hasNext()) {
			Object item = it.next();
			try {
				objects.addElement(new DropDownHolder(item, (String) item.getClass()
						.getMethod(methodName, new Class[0]).invoke(item, new Object[0])));
			} catch (Exception exe) {
			}
		}
		if (getSize() > 0) {
			selectedObject = getElementAt(0);
		}
	}

	// implements javax.swing.ComboBoxModel
	/**
	 * Set the value of the selected item. The selected item may be null.
	 * <p>
	 * @param anObject The combo box value or null for no selection.
	 */
	public void setSelectedItem(Object anObject) {
		if ((selectedObject != null && !selectedObject.equals(anObject)) 
				|| selectedObject == null && anObject != null) {
			selectedObject = anObject;
			fireContentsChanged(this, -1, -1);
		}
	}

	// implements javax.swing.ComboBoxModel
	public Object getSelectedItem() {
		return selectedObject;
		// return ((DropDownHolder)selectedObject).getObject();
	}

	public Object getSelectedItemCustom() {
		return ((DropDownHolder) selectedObject).getObject();
	}

	// implements javax.swing.ListModel
	public int getSize() {
		return objects.size();
	}

	// implements javax.swing.ListModel
	public Object getElementAt(int index) {
		if (index >= 0 && index < objects.size()) {

			// return ((DropDownHolder)objects.elementAt(index)).getObject();
			return objects.elementAt(index);
		}
		return null;
	}

	/**
	 * Returns the index-position of the specified object in the list.
	 *
	 * @param anObject
	 * @return an int representing the index position, where 0 is
	 *         the first position
	 */
	public int getIndexOf(Object anObject) {
		return objects.indexOf(anObject);
	}

	/**
	 * Empties the list.
	 */
	public void removeAllElements() {
		if (objects.size() > 0) {
			int firstIndex = 0;
			int lastIndex = objects.size() - 1;
			objects.removeAllElements();
			selectedObject = null;
			fireIntervalRemoved(this, firstIndex, lastIndex);
		}
	}
}