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

import java.text.Collator;
import java.util.*;

/**
 * This listmodel can be used for sorted lists attending the current locale.
 * Unfortunately this model can't be used in a JComboBox. 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>* company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class SortingListModel extends AbstractPickListModel {
	// Define a SortedSet
	private TreeSet model;

	private static Comparator localeSensitiveStringComparator = new Comparator() {
		public int compare(Object o1, Object o2) {
			String str1 = o1.toString();
			String str2 = o2.toString();
			Collator collator = Collator.getInstance();
			int result = collator.compare(str1, str2);
			return result;
		}
	};

	public SortingListModel() {
		model = new TreeSet(localeSensitiveStringComparator);
	}

	// ListModel methods
	/** @see javax.swing.ListModel#getSize() */
	public int getSize() {
		return model.size();
	}

	/** @see javax.swing.ListModel#getElementAt(int) */
	public Object getElementAt(int index) {
		// Return the appropriate element
		return model.toArray()[index];
	}

	// Other methods
	/**
	 * Adds the specified {@code element} to this list model.
	 * 
	 * @param element
	 */
	public void addElement(Object element) {
		if (model.add(element)) {
			fireContentsChanged(this, 0, getSize());
		}
	}

	public void addAll(Object[] elements) {
		Collection c = Arrays.asList(elements);
		model.addAll(c);
		fireContentsChanged(this, 0, getSize());
	}

	public void clear() {
		model.clear();
		fireContentsChanged(this, 0, getSize());
	}

	public boolean contains(Object element) {
		return model.contains(element);
	}

	public Object firstElement() {
		// Return the appropriate element
		return model.first();
	}

	public Iterator iterator() {
		return model.iterator();
	}

	public Object lastElement() {
		// Return the appropriate element
		return model.last();
	}

	public boolean removeElement(Object element) {
		boolean removed = model.remove(element);
		if (removed) {
			fireContentsChanged(this, 0, getSize());
		}
		return removed;
	}
	
	public boolean removeElementAt(int position) {
		Object current = this.getElementAt(position);
		return (this.removeElement(current));
	}
	
	public void removeAllElements() {
		this.clear();
	}
	
	public void fireContentsChanged() {
		this.fireContentsChanged(this, 0, this.getSize());
	}
	
	public void setElementAt(Object obj, int index) {
		// nothing to do
	}

}