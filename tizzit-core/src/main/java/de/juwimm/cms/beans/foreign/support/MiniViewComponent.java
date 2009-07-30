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
/*
 * Created on 28.11.2005
 */
package de.juwimm.cms.beans.foreign.support;

/**
 * Helper-class for viewComponent holding the needed attributes for ordering a subtree
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id:MiniViewComponent.java 17629 2007-03-05 15:03:50Z kulawik $
 */
public class MiniViewComponent implements Comparable {
	private Integer vcId;
	private Integer prevId;
	private Integer nextId;
	private String text;

	/**
	 * @param vcId
	 * @param prevId
	 * @param nextId
	 */
	public MiniViewComponent(Integer vcId, Integer prevId, Integer nextId, String text) {
		super();
		this.nextId = nextId;
		this.prevId = prevId;
		this.vcId = vcId;
		this.text = text;
	}

	public Integer getNextId() {
		return nextId;
	}

	public Integer getPrevId() {
		return prevId;
	}

	public Integer getVcId() {
		return vcId;
	}

	public String getText() {
		return text;
	}

	public int compareTo(Object o) {
		if (o instanceof MiniViewComponent) {
			MiniViewComponent mvc = (MiniViewComponent) o;
			return this.getVcId().compareTo(mvc.getVcId());
		}
		return 0;
	}

}
