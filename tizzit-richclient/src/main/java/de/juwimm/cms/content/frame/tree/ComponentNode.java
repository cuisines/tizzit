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

import java.util.Hashtable;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import de.juwimm.cms.content.frame.helper.IsolatedAggregationHelper;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public abstract class ComponentNode extends CheckNode {
	private boolean isInit = false;
	private Hashtable hashClicks = new Hashtable();
	private IsolatedAggregationHelper aggHelper = null;

	public ComponentNode() {
	}
	
	public abstract boolean hasChildren();

	public void loadNodes() {
	}

	public void setInit() {
		isInit = true;
	}

	public boolean isInit() {
		return isInit;
	}

	public abstract void remove() throws Exception;

	public abstract long getId();

	/**
	 * Sets the {@link Hashtable} that tracks the comprised components that should be displayed.
	 * Each visible component is an entry with the component's name as key and an integer as value
	 * (1 = true). If at least one component was marked visible, 
	 * 
	 * @param hashClicks the Hashtable for this node
	 */
	public void setClicks(Hashtable hashClicks) {
		this.hashClicks = hashClicks;
		if (hasClicks()) setCheckSelected(true);
	}

	public Hashtable getClicks() {
		return this.getHashClicks();
	}

	/**
	 * Returns {@code true} if at least one comprised component is supposed to be visible, false otherwise.
	 * 
	 * @return a boolean indicating if the node is supposed to display at least one of it's components
	 */
	public boolean hasClicks() {
		if (this.getHashClicks() != null) {
			java.util.Iterator it = this.getHashClicks().values().iterator();
			while (it.hasNext()) {
				if (((Integer) it.next()).equals(new Integer(1))) {
					return true;
				}
			}
		}
		return false;
	}

	public abstract Node getXmlRepresentation(Document doc, Node node);

	public abstract String getDescription();

	/**
	 * @return Returns the hashClicks.
	 */
	protected Hashtable getHashClicks() {
		return hashClicks;
	}

	public IsolatedAggregationHelper getAggregationHelper() {
		return aggHelper;
	}
	
	public void setAggregationHelper(IsolatedAggregationHelper aggHelper) {
		this.aggHelper = aggHelper;
	}

}