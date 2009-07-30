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

import java.util.Comparator;

/**
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id:MiniViewComponentComparator.java 17629 2007-03-05 15:03:50Z kulawik $
 */
public class MiniViewComponentComparator implements Comparator {

	public int compare(MiniViewComponent mvc1, MiniViewComponent mvc2) {
		if (mvc1.getVcId() != null && mvc2.getVcId() != null) { return mvc1.getVcId().compareTo(mvc2.getVcId()); }
		return 0;
	}

	public int compare(Object o1, Object o2) {
		if ((o1 instanceof MiniViewComponent) && (o2 instanceof MiniViewComponent)) { return ((MiniViewComponent) o1).getVcId().compareTo(((MiniViewComponent) o2).getVcId()); }
		return 0;
	}

}
