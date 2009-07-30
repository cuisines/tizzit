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
package de.juwimm.swing;

import java.util.Arrays;
import java.util.Iterator;

import javax.swing.DefaultListModel;

/**
 * A manually sortable list model that can be used for {@code PickListData}.
 * 
 * @author <a href="mailto:christiane.hausleiter@juwimm.com">Christiane Hausleiter</a>
 *
 */
public class SortableListModel extends AbstractPickListModel {
	
	private DefaultListModel defaultListModel;
	
	public SortableListModel() {
		this.defaultListModel = new DefaultListModel();
	}

	/** @see de.juwimm.swing.AbstractPickListModel#addAll(java.lang.Object[]) */
	public void addAll(Object[] elements) {
		for (int i = 0; i < elements.length; i++) {
			this.defaultListModel.addElement(elements[i]);
		}
		fireContentsChanged(this, 0, getSize());
	}

	/** @see de.juwimm.swing.AbstractPickListModel#addElement(java.lang.Object) */
	public void addElement(Object obj) {
		this.defaultListModel.addElement(obj);
		fireContentsChanged(this, 0, getSize());
	}

	/** @see de.juwimm.swing.AbstractPickListModel#clear() */
	public void clear() {
		this.defaultListModel.clear();
		fireContentsChanged(this, 0, getSize());
	}

	/** @see de.juwimm.swing.AbstractPickListModel#contains(java.lang.Object) */
	public boolean contains(Object element) {
		return this.defaultListModel.contains(element);
	}

	/** @see de.juwimm.swing.AbstractPickListModel#fireContentsChanged() */
	public void fireContentsChanged() {
		fireContentsChanged(this, 0, getSize());
	}

	/** @see de.juwimm.swing.AbstractPickListModel#firstElement() */
	public Object firstElement() {
		return this.defaultListModel.firstElement();
	}

	/** @see de.juwimm.swing.AbstractPickListModel#iterator() */
	public Iterator iterator() {
		return Arrays.asList(this.defaultListModel.toArray()).iterator();
	}

	/** @see de.juwimm.swing.AbstractPickListModel#lastElement() */
	public Object lastElement() {
		return this.defaultListModel.lastElement();
	}

	/** @see de.juwimm.swing.AbstractPickListModel#removeAllElements() */
	public void removeAllElements() {
		this.defaultListModel.clear();
		fireContentsChanged(this, 0, getSize());
	}

	/** @see de.juwimm.swing.AbstractPickListModel#removeElement(java.lang.Object) */
	public boolean removeElement(Object obj) {
		int sizeBefore = getSize();
		this.defaultListModel.removeElement(obj);
		if (getSize() < sizeBefore) {
			fireContentsChanged(this, 0, getSize());
			return true;
		}
		return false;
	}

	/** @see de.juwimm.swing.AbstractPickListModel#removeElementAt(int) */
	public boolean removeElementAt(int position) {
		int sizeBefore = getSize();
		this.defaultListModel.removeElementAt(position);
		if (getSize() < sizeBefore) {
			fireContentsChanged(this, 0, getSize());
			return true;
		}
		return false;
	}

	/** @see de.juwimm.swing.AbstractPickListModel#setElementAt(java.lang.Object, int) */
	public void setElementAt(Object obj, int index) {
		if (index < 0) {
			this.defaultListModel.setElementAt(obj, 0);
		} else {
			if (index >= getSize()) {
				this.defaultListModel.setElementAt(obj, this.defaultListModel.getSize() - 1);
			} else {
				this.defaultListModel.setElementAt(obj, index);		
			}
		}
		fireContentsChanged(this, 0, getSize());
	}

	/** @see javax.swing.ListModel#getElementAt(int) */
	public Object getElementAt(int index) {
		if (index < 0) {
			return this.defaultListModel.get(0);
		} else if (index >= getSize()) {
			return this.defaultListModel.lastElement();
		}
		return this.defaultListModel.get(index);
	}

	/** @see javax.swing.ListModel#getSize() */
	public int getSize() {
		return this.defaultListModel.getSize();
	}

}
