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

import java.util.Iterator;

import javax.swing.AbstractListModel;

/**
 * An abstract class extending {@link AbstractListModel}, providing services similar to {@code javax.swing.DefaultListModel} 
 * - but not all of them.
 * 
 * @author <a href="mailto:christiane.hausleiter@juwimm.com">Christiane Hausleiter</a>
 */
public abstract class AbstractPickListModel extends AbstractListModel {
	
	public abstract void addElement(Object obj);
	
	public abstract void addAll(Object[] elements);
	
	public abstract void clear();
	
	public abstract boolean contains(Object element);
	
	public abstract Object firstElement();
	
	public abstract Iterator iterator();
	
	public abstract Object lastElement();

	public abstract boolean removeElement(Object obj);

	public abstract boolean removeElementAt(int position);
	
	public abstract void removeAllElements();
	
	public abstract void fireContentsChanged();
	
	public abstract void setElementAt(Object obj, int index);
}
