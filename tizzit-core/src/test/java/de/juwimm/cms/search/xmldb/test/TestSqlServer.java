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
 * Created on 12.05.2005
 */
package de.juwimm.cms.search.xmldb.test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Ignore;

import de.juwimm.cms.search.vo.XmlSearchValue;

/**
 * Test for SQL-Server 2005
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
@Ignore
public final class TestSqlServer {
	private static Logger log = Logger.getLogger(TestSqlServer.class);
	private Connection connection = null;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS", Locale.GERMAN);

	private TestSqlServer() {
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		TestSqlServer main = new TestSqlServer();
		main.execute();
	}

	private void execute() {
		try {
			this.connection = this.connect();
			XmlSearchValue[] results = this.searchXml(1, "//content");
			if (results != null && results.length > 0) {
				for (int i = (results.length - 1); i >= 0; i--) {
					log.info(results[i].getViewComponentId().toString() + ": " + results[i].getText());
				}
			}
		} catch (Exception e) {
			log.error("Error: " + e.getMessage(), e);
		} finally {
			try {
				if (this.connection != null) {
					this.connection.close();
					this.connection = null;
				}
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
	}

	private Connection connect() {
		try {
			Properties properties = new Properties();
			properties.put("user", "cms");
			properties.put("password", "cms");
			String driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			String connectionUrl = "jdbc:sqlserver://192.168.30.129:1433;databaseName=cms";
			Class.forName(driverClass);
			Connection connection = DriverManager.getConnection(connectionUrl, properties);
			DatabaseMetaData dmd = connection.getMetaData();
			StringBuilder sb = new StringBuilder();
			sb.append("\nConnection Details");
			sb.append("\n==================");
			sb.append("\nConnection URL : " + dmd.getURL());
			sb.append("\nDB Name        : " + dmd.getDatabaseProductName());
			sb.append("\nDB Version     : " + dmd.getDatabaseMajorVersion() + "." + dmd.getDatabaseMinorVersion());
			sb.append("\nDB Version Name: " + dmd.getDatabaseProductVersion());
			sb.append("\nDriver Name    : " + dmd.getDriverName());
			sb.append("\nDriver Version : " + dmd.getDriverVersion());
			sb.append("\nUsername       : " + dmd.getUserName());
			sb.append("\n-------------------------------------");
			log.info(sb.toString());
			return connection;
		} catch (ClassNotFoundException e) {
			log.error("Class not found: " + e.getMessage());
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	public static String calcHMS(long timeInSeconds) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return (dateFormat.format(new java.util.Date(timeInSeconds)));
	}

	public synchronized XmlSearchValue[] searchXml(Integer siteId, String xpathQuery) {
		if (log.isDebugEnabled()) log.debug("searchXML(...) -> begin at " + sdf.format(new Date()));
		String select = "SELECT xdb.VIEW_COMPONENT_ID, xdb.UNIT_ID, xdb.INFO_TEXT, xdb.TEXT, xdb.CONTENT FROM XML_SEARCH_DB xdb WHERE xdb.SITE_ID = ? AND xdb.CONTENT.exist('" + xpathQuery + "') = 1";

		XmlSearchValue[] retArray = null;
		LinkedList<XmlSearchValue> tmpList = new LinkedList<XmlSearchValue>();

		PreparedStatement pstmt = null;

		try {
			pstmt = this.connection.prepareStatement(select);
			pstmt.setInt(1, siteId.intValue());
			ResultSet qResult = pstmt.executeQuery();

			while (qResult.next()) {
				XmlSearchValue newEntry = new XmlSearchValue();
				newEntry.setViewComponentId(qResult.getInt(1));
				newEntry.setUnitId(qResult.getInt(2));
				newEntry.setInfoText(qResult.getString(3));
				newEntry.setText(qResult.getString(4));
				newEntry.setContent(qResult.getString(5));

				tmpList.add(newEntry);
			}

			retArray = tmpList.toArray(new XmlSearchValue[0]);
		} catch (SQLException sqle) {
			log.error("searchXML(...) -> failed to excecute query for xpathQuery = \"" + xpathQuery + "\": " + sqle.getMessage());
		} catch (Exception e) {
			log.error("searchXML(...) -> unkown exception occured " + e.getMessage());
		} finally {
			try {
				if (pstmt != null) pstmt.close();
			} catch (SQLException e) {
				log.error("searchXML(...) -> error closing pstmt " + e.getMessage());
			}
		}

		if (log.isDebugEnabled()) log.debug("searchXML(...) -> end at " + sdf.format(new Date()));

		return retArray;
	}

}
