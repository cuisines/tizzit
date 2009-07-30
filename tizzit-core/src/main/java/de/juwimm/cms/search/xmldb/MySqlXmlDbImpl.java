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
package de.juwimm.cms.search.xmldb;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Uses cqXmlDatasource as datasource
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class MySqlXmlDbImpl extends AbstractXmlDbImpl {
	private static final String INSERT_STATEMENT = "INSERT INTO XML_SEARCH_DB (SITE_ID, VIEW_COMPONENT_ID, CONTENT, UNIT_ID, INFO_TEXT, TEXT, HASHCODE, CREATED) VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";

	@Override
	protected String getInsertStatementSql() {
		return MySqlXmlDbImpl.INSERT_STATEMENT;
	}

	@Override
	protected PreparedStatement getSearchXmlStatement(Integer siteId, String xpathQuery) throws SQLException {
		String select = "SELECT xdb.VIEW_COMPONENT_ID, xdb.UNIT_ID, xdb.INFO_TEXT, xdb.TEXT, xdb.CONTENT FROM XML_SEARCH_DB xdb WHERE xdb.SITE_ID = ? AND ExtractValue(xdb.CONTENT, ?) != ''"; 
		PreparedStatement pstmt = super.getConnection().prepareStatement(select);
		pstmt.setInt(1, siteId.intValue());
		pstmt.setString(2, xpathQuery);
		return pstmt;
	}

	@Override
	protected PreparedStatement getSearchXmlByUnitStatement(Integer unitId, Integer viewDocumentId, String xpathQuery) throws SQLException {
		String selectUnit = "SELECT xdb.VIEW_COMPONENT_ID, xdb.UNIT_ID, xdb.INFO_TEXT, xdb.TEXT, xdb.CONTENT FROM XML_SEARCH_DB xdb WHERE xdb.UNIT_ID = ? AND ExtractValue(xdb.CONTENT, ?) != ''";
		PreparedStatement pstmt = super.getConnection().prepareStatement(selectUnit);
		pstmt.setInt(1, unitId.intValue());
		pstmt.setString(2, xpathQuery);

		return pstmt;
	}

}
