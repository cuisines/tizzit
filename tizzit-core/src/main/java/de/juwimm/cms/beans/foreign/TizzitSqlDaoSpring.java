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
package de.juwimm.cms.beans.foreign;

import java.util.List;

import de.juwimm.cms.beans.foreign.support.MiniViewComponent;

public interface TizzitSqlDaoSpring {
	public List<MiniViewComponent> getViewComponentByParentId(Integer parentId);
 
	public void updateMvc(MiniViewComponent prev, MiniViewComponent next);
	
	public void updateFirstMvc(MiniViewComponent first);

	public void updateLastMvc(MiniViewComponent last);

	public void updateParentMvc(int parentId, MiniViewComponent first);

}
