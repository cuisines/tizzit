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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import oracle.xdb.XMLType;

import org.apache.log4j.Logger;
import org.jboss.resource.adapter.jdbc.WrappedConnection;

/**
 * Uses cqXmlDatasource as datasource
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id: OracleXmlDbImpl.java 29838 2009-03-13 10:49:08Z kulawik $
 */
public class OracleXmlDbImpl extends AbstractXmlDbImpl {
	private static Logger log = Logger.getLogger(OracleXmlDbImpl.class);
	private static final String INSERT_STATEMENT = "INSERT INTO XML_SEARCH_DB (SITE_ID, VIEW_COMPONENT_ID, CONTENT, UNIT_ID, INFO_TEXT, TEXT, HASHCODE, CREATED) VALUES (?, ?, ?, ?, ?, ?, ?, SYSDATE)";
	private static final String SELECT_STATEMENT = "SELECT xdb.VIEW_COMPONENT_ID, xdb.UNIT_ID, xdb.INFO_TEXT, xdb.TEXT, xdb.CONTENT.extract(?).getClobVal() FROM XML_SEARCH_DB xdb WHERE xdb.SITE_ID = ? AND xdb.CONTENT.existsNode(?) = 1";
	private static final String SELECT_STATEMENT_UNIT = "SELECT xdb.VIEW_COMPONENT_ID, xdb.UNIT_ID, xdb.INFO_TEXT, xdb.TEXT, xdb.CONTENT.extract(?).getClobVal() FROM XML_SEARCH_DB xdb WHERE xdb.UNIT_ID = ? AND xdb.CONTENT.existsNode(?) = 1";

	/**
	 * Override because of special behavior on inserting xml
	 * @param siteId
	 * @param viewComponentId
	 * @param contentText
	 * @param metaAttributes
	 * @return
	 */
	@Override
	protected synchronized boolean doInsert(Integer siteId, Integer viewComponentId, String contentText, Map<String, String> metaAttributes, String hashcode) {
		if (log.isDebugEnabled()) log.debug("doInsert(...) -> begin");
		boolean retVal = false;

		PreparedStatement pstmt = null;
		Connection connection = null;
		String infoText = null;
		String text = null;
		int unitId = 0;

		try {
			if (contentText == null || contentText.length() == 0) {
				// do not insert anything if content is empty
				return true;
			}
			if (log.isDebugEnabled()) {
				log.debug("doInsert(...) -> before parsing metaAttributes");
			}
			if (!("".equals(metaAttributes.get("unitId")))) {
				unitId = Integer.parseInt(metaAttributes.get("unitId"));
			}
			infoText = metaAttributes.get("infoText"); 
			text = metaAttributes.get("text");
			if (log.isDebugEnabled()) {
				log.debug("doInsert(...) -> after parsing metaAttributes");
			}

			connection = super.getConnection();
			pstmt = connection.prepareStatement(this.getInsertStatementSql());
			pstmt.setInt(1, siteId.intValue());
			pstmt.setInt(2, viewComponentId.intValue());
			// ClassCastException: org.jboss.resource.adapter.jdbc.WrappedConnection cannot be cast to oracle.jdbc.OracleConnection...
			XMLType xmlType = XMLType.createXML(((WrappedConnection) connection).getUnderlyingConnection(), contentText);
			// pstmt.setStringForClob(3, contentText);
			pstmt.setObject(3, xmlType);
			pstmt.setInt(4, unitId);
			pstmt.setString(5, infoText);
			pstmt.setString(6, text);
			pstmt.setString(7, hashcode);
			retVal = pstmt.execute();
			if (log.isDebugEnabled()) log.debug("doInsert(...) -> insert query executed");
		} catch (SQLException sqle) {
			log.error("doInsert(...) -> failed to excecute insert query for viewComponentId = " + viewComponentId + ", " + sqle.getMessage());
			if (log.isDebugEnabled()) log.debug("Content to insert: " + contentText);
		} catch (Exception e) {
			log.error("doInsert(...) -> unkown exception occured " + e.getMessage(), e);
		} finally {
			try {
				if (pstmt != null) pstmt.close();
			} catch (SQLException e) {
				log.error("doInsert(...) -> error closing pstmt " + e.getMessage());
			}
			super.releaseConnection();
		}

		if (log.isDebugEnabled()) log.debug("doInsert(...) -> end");
		return retVal;
	}

	@Override
	protected String getInsertStatementSql() {
		return OracleXmlDbImpl.INSERT_STATEMENT;
	}

	@Override
	protected PreparedStatement getSearchXmlStatement(Integer siteId, String xpathQuery) throws SQLException {
		PreparedStatement pstmt = super.getConnection().prepareStatement(OracleXmlDbImpl.SELECT_STATEMENT);
		pstmt.setString(1, xpathQuery);
		pstmt.setInt(2, siteId.intValue());
		pstmt.setString(3, xpathQuery);

		return pstmt;
	}

	@Override
	protected PreparedStatement getSearchXmlByUnitStatement(Integer unitId, Integer viewDocumentId, String xpathQuery) throws SQLException {
		PreparedStatement pstmt = super.getConnection().prepareStatement(OracleXmlDbImpl.SELECT_STATEMENT_UNIT);
		pstmt.setString(1, xpathQuery);
		pstmt.setInt(2, unitId.intValue());
		pstmt.setString(3, xpathQuery);

		return pstmt;
	}

}
