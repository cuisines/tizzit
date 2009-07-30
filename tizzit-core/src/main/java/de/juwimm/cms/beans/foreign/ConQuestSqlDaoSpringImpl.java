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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import de.juwimm.cms.beans.foreign.support.MiniViewComponent;

public class ConQuestSqlDaoSpringImpl extends JdbcDaoSupport implements ConQuestSqlDaoSpring {
	private Logger log = Logger.getLogger(ConQuestSqlDaoSpringImpl.class);

	public List<MiniViewComponent> getViewComponentByParentId(Integer parentId) {
		log.info("TreeRepair: getViewComponentByParentId " + parentId);
		String selectQuery = "SELECT VIEW_COMPONENT_ID, PREV_NODE_ID_FK, NEXT_NODE_ID_FK, URL_LINK_NAME FROM VIEWCOMPONENT WHERE PARENT_ID_FK = ? ORDER BY VIEW_COMPONENT_ID";
		List<MiniViewComponent> lst = getJdbcTemplate().query(selectQuery, new Object[] {parentId}, new MiniViewComponentRowMapper());
		return lst;
	}

	private class MiniViewComponentRowMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			int vcId = rs.getInt("VIEW_COMPONENT_ID");
			int prevId = rs.getInt("PREV_NODE_ID_FK");
			int nextId = rs.getInt("NEXT_NODE_ID_FK");
			String text = rs.getString("URL_LINK_NAME");
			MiniViewComponent vc = new MiniViewComponent(vcId, prevId, nextId, text);
			return vc;
		}
	}

	public void updateMvc(MiniViewComponent prev, MiniViewComponent next) {
		log.debug("TreeRepair: update prev " + prev.getVcId() + " next " + next.getVcId());
		String updateNextQuery = "UPDATE VIEWCOMPONENT SET PREV_NODE_ID_FK = ? WHERE VIEW_COMPONENT_ID = ?";
		getJdbcTemplate().update(updateNextQuery, new Object[] {prev.getVcId(), next.getVcId()});

		String updatePrevQuery = "UPDATE VIEWCOMPONENT SET NEXT_NODE_ID_FK = ? WHERE VIEW_COMPONENT_ID = ?";
		getJdbcTemplate().update(updatePrevQuery, new Object[] {next.getVcId(), prev.getVcId()});
	}

	public void updateFirstMvc(MiniViewComponent first) {
		log.debug("TreeRepair: update first " + first.getText());
		String updateQuery = "UPDATE VIEWCOMPONENT SET PREV_NODE_ID_FK = NULL WHERE VIEW_COMPONENT_ID = ?";
		getJdbcTemplate().update(updateQuery, new Object[] {first.getVcId()});
	}

	public void updateLastMvc(MiniViewComponent last) {
		log.debug("TreeRepair: update last " + last.getText());
		String updateQuery = "UPDATE VIEWCOMPONENT SET NEXT_NODE_ID_FK = NULL WHERE VIEW_COMPONENT_ID = ?";
		getJdbcTemplate().update(updateQuery, new Object[] {last.getVcId()});
	}

	public void updateParentMvc(int parentId, MiniViewComponent first) {
		log.debug("TreeRepair: update parent " + parentId);
		String updateQuery = "UPDATE VIEWCOMPONENT SET FIRST_CHILD_ID_FK = ? WHERE VIEW_COMPONENT_ID = ?";
		getJdbcTemplate().update(updateQuery, new Object[] {first.getVcId(), parentId});
	}

}
