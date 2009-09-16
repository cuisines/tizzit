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
package de.juwimm.cms.cocoon.transformation;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import javax.naming.*;
import javax.sql.DataSource;

import org.apache.avalon.excalibur.pool.Recyclable;
import org.apache.avalon.framework.configuration.*;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractTransformer;
import org.apache.log4j.Logger;
import org.tizzit.util.xml.SAXHelper;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import de.juwimm.cms.cocoon.helper.ConfigurationHelper;

/**
 * 
 * input-snippet:
 * <?xml version="1.0" encoding="UTF-8"?>
 * ...
 * <!-- getuserdata wird konfiguriert mittels <map:parameter name="tagname" value="getuserdata"/> -->
 * <getuserdata>
 * 	  <!-- Als Bindvariablentypen sind derzeit nur String, Integer, Double und Date (im Format dd.mm.yyyy) erlaubt. -->
 *    <!--<param type="java.lang.Integer" value="3000"/>-->
 *    <param type="java.lang.String" value="jens@jens.de"/>        
 * </getuserdata>
 * ...
 * 
 * output-snippet:
 * <getuserdata>
 * 	<item>
 *  	<column_name1>value</column_name1>
 *  	<column_name2>value</column_name2>
 *  	...
 *  </item>
 *  ...
 * </getuserdata>
 * 
 * sitemap.xml-snippets:
 * <map:transformers default="saxon">
 * 		...
 * 		<map:transformer name="sqlTransformer" src="de.juwimm.cms.cocoon.transformation.SQLTransformer">
 *			<dsJndiName>java:/SeronoDS</dsJndiName>			
 *		</map:transformer>
 *		...			
 * </map:transformers>
 * 
 * <map:pipeline type="noncaching">
 *		<map:match pattern="sqltransformer">
 *			<map:generate src="xml/sqltransformertest.xml"/>
 *				<map:transform type="sqltransformer">
 *					<map:parameter name="sql" value="SELECT * FROM xuser WHERE email = ?"/>
 *					<map:parameter name="tagname" value="getuserdata"/>
 *				</map:transform>
 *				<map:transform src="xsl/nothing.xsl">
 *					<!--<map:parameter name="use-request-parameters" value="true"/>-->
 *				</map:transform>
 *			<map:serialize type="xml"/>
 *		</map:match>
 *	</map:pipeline>
 *
 *	lifecycle (always pooled because it implements Recyclable):
 *	1.) configure
 *  2.) setup(...)
 *  3.) ...
 *  4.) recycle(...)
 *  5.) setup(...)
 *  6.) ...
 *  7.) recycle(...)
 *  ...
 * 
 * @author toerberj
 * Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class SQLTransformer extends AbstractTransformer implements Recyclable, Configurable {
	protected static Logger log = Logger.getLogger(SQLTransformer.class);
	private static final String SQL = "sql";
	private static final String TAGNAME = "tagname";
	private static final String PARAM = "param";
	private static final String TYPE = "type";
	private static final String VALUE = "value";

	private static final String ITEM_TAG = "item";

	private Configuration config = null;
	private String dsJndiName = "";
	private String sql = "";
	private String tagname = "";
	private boolean inTag = false;
	private List parameter = new ArrayList();

	private Connection getConnection() throws SQLException {
		Connection conn = null;
		DataSource ds = null;

		Context jndiCntx = null;
		try {
			jndiCntx = new InitialContext();

			ds = (DataSource) jndiCntx.lookup(this.dsJndiName);
			conn = ds.getConnection();
		} catch (NamingException ex) {
			log.error("getConnection(): ", ex);
		} finally {
			try {
				jndiCntx.close();
			} catch (NamingException ex) {
				log.error("getConnection(): ", ex);
			}
		}

		return conn;
	}

	/* (non-Javadoc)
	 * @see org.apache.cocoon.transformation.AbstractTransformer#setup(org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
	 */
	public void setup(SourceResolver arg0, Map arg1, String arg2, Parameters params) throws ProcessingException, SAXException, IOException {
		if (log.isDebugEnabled()) log.debug("setup(...) begin");

		try {
			this.sql = params.getParameter(SQLTransformer.SQL);
			this.tagname = params.getParameter(SQLTransformer.TAGNAME);
		} catch (ParameterException e) {
			throw new SAXException(SQLTransformer.SQL + "- and/or " + SQLTransformer.TAGNAME + "-Parameter not definied");
		}

		if (log.isDebugEnabled()) log.debug("setup(...) end");
	}

	/* (non-Javadoc)
	 * @see org.apache.cocoon.xml.AbstractXMLProducer#recycle()
	 */
	@Override
	public void recycle() {
		if (log.isDebugEnabled()) log.debug("recycle()");
	}

	/* (non-Javadoc)
	 * @see org.apache.avalon.framework.configuration.Configurable#configure(org.apache.avalon.framework.configuration.Configuration)
	 */
	public void configure(Configuration config) throws ConfigurationException {
		this.config = config;

		if (this.config != null) {
			this.dsJndiName = ConfigurationHelper.getDsJndiName(this.config);
		}
	}

	/**
	 * uri - The Namespace URI, or the empty string if the element has no Namespace URI or if Namespace processing is not being performed.
	 * loc - The local name (without prefix), or the empty string if Namespace processing is not being performed.
	 * qName - The raw XML 1.0 name (with prefix), or the empty string if raw names are not available.
	 * a - The attributes attached to the element. If there are no attributes, it shall be an empty Attributes object.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void startElement(String uri, String loc, String qName, Attributes a) throws SAXException {
		if (!this.inTag) {
			super.startElement(uri, loc, qName, a);
		}
		if (this.inTag) {
			if (qName.equals(SQLTransformer.PARAM)) {
				int indexType = a.getIndex(SQLTransformer.TYPE);
				int indexValue = a.getIndex(SQLTransformer.VALUE);
				String type = "";
				String value = "";

				if (indexType > -1) {
					type = a.getValue(indexType);
					if (indexValue > -1) {
						value = a.getValue(indexValue);
						if (type.equals("java.lang.String")) {
							this.parameter.add(value);
						} else if (type.equals("java.lang.Integer")) {
							this.parameter.add(Integer.valueOf(value));
						} else if (type.equals("java.lang.Double")) {
							this.parameter.add(Double.valueOf(value));
						} else if (type.equals("java.util.Date")) {
							SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
							Date date = null;

							try {
								date = sdf.parse(value);
								this.parameter.add(date);
							} catch (ParseException ex) {
								log.error("startElement(...): parsing Date-value ", ex);
							}
						} else {
							log.error("not supported type " + type + " for value " + value);
						}
					}
				}
			}
		}
		if (qName.equals(this.tagname)) {
			this.inTag = true;
			this.parameter = new ArrayList();
		}
	}

	/**
	 * uri - The Namespace URI, or the empty string if the element has no Namespace URI or if Namespace processing is not being performed.
	 * loc - The local name (without prefix), or the empty string if Namespace processing is not being performed.
	 * raw - The raw XML 1.0 name (with prefix), or the empty string if raw names are not available. 
	 */
	@Override
	public void endElement(String uri, String loc, String qName) throws SAXException {
		if (qName.equals(this.tagname)) {
			this.inTag = false;
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				conn = getConnection();
				pstmt = conn.prepareStatement(this.sql);

				for (int i = 1; i <= this.parameter.size(); i++) {
					String clzName = this.parameter.get(i - 1).getClass().getName();

					if (clzName.equals("java.lang.String")) {
						pstmt.setString(i, (String) this.parameter.get(i - 1));
					} else if (clzName.equals("java.lang.Integer")) {
						pstmt.setInt(i, ((Integer) this.parameter.get(i - 1)).intValue());
					} else if (clzName.equals("java.lang.Double")) {
						pstmt.setDouble(i, ((Double) this.parameter.get(i - 1)).doubleValue());
					} else if (clzName.equals("java.util.Date")) {
						java.sql.Date date = new java.sql.Date(((Date) this.parameter.get(i - 1)).getTime());

						pstmt.setDate(i, date);
					} else {
						log.error("not supported type for bind variable: " + clzName);
					}
				}
				rs = pstmt.executeQuery();
				if (rs != null) {
					ResultSetMetaData rsmeta = rs.getMetaData();
					int colCount = rsmeta.getColumnCount();
					String columnType = "";

					while (rs.next()) {
						this.contentHandler.startElement("", SQLTransformer.ITEM_TAG, SQLTransformer.ITEM_TAG, new AttributesImpl());
						for (int i = 1; i <= colCount; i++) {
							columnType = rsmeta.getColumnTypeName(i);
							if (!columnType.contains("BLOB")) {
								String s = rs.getString(i);

								if (rs.wasNull()) {
									SAXHelper.addElement(this.contentHandler, rsmeta.getColumnLabel(i).toLowerCase(), "");
								} else {
									if (columnType.contains("VARCHAR") || columnType.contains("TEXT") || columnType.equals("CLOB")) {
									}
									SAXHelper.addElement(this.contentHandler, rsmeta.getColumnLabel(i).toLowerCase(), s);
									if (columnType.contains("VARCHAR") || columnType.contains("TEXT") || columnType.equals("CLOB")) {
										// this.endCDATA();
									}
								}
							} else {
								log.info("ignoring column " + rsmeta.getColumnLabel(i) + " because of not supported type: " + columnType);
							}
						}
						this.contentHandler.endElement("", SQLTransformer.ITEM_TAG, SQLTransformer.ITEM_TAG);
					}
				}
			} catch (SQLException ex) {
				log.error("endElement(...): ", ex);
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException ex) {
					log.error("endElement(...): error during close ", ex);
				}
			}
		}
		if (!this.inTag) {
			super.endElement(uri, loc, qName);
		}
	}

}
