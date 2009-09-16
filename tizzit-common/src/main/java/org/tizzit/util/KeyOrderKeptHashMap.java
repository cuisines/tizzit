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
package org.tizzit.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * @author Sabarinath Ayyappan
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $$Id$$
 */
public class KeyOrderKeptHashMap extends HashMap {
	//List to hold the keys so that the order of the key added is kept,
	//which HashMap does not maintain.
	private List internalOrder;

	public KeyOrderKeptHashMap() {
		super();
		internalOrder = new LinkedList();
	}

	/**
	 * Funtion to add the key and value to the HashMap and also add the
	 * key to the list to keep the order of in which it was added.
	 * @param key
	 * @param value
	 * @return
	 */
	public Object put(Object key, Object value) {
		internalOrder.add(key);
		return super.put(key, value);
	}

	/**
	 * Funtion to add Map, from which the key and value will
	 * be added to the HashMap and also add the key to the list
	 * @param key
	 * @param value
	 * @return
	 */
	public void putAll(Map otherMap) {
		internalOrder.addAll(otherMap.keySet());
		super.putAll(otherMap);
	}

	/**
	 * Funtion to remove the key and value from the HashMap and also
	 * remove the key to the list
	 * @param key
	 * @param value
	 * @return
	 */
	public Object remove(Object key) {
		internalOrder.remove(key);
		return super.remove(key);
	}

	/**
	 * Funtion to get the list which contains all the key, in the order in which
	 * it was added.
	 * @param key
	 * @param value
	 * @return
	 */
	public List getKeyOrder() {
		return internalOrder;
	}

	public void clear() {
		super.clear();
		this.internalOrder.clear();
	}

}