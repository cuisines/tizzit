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
package de.juwimm.cms.search.compass;

import java.util.Properties;

import org.apache.log4j.Logger;

import de.juwimm.cms.beans.foreign.CqPropertiesBeanSpring;

/**
 * Helper to configure Compass with values from conquest.properties
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> , Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class CompassSettings extends Properties {
	private static final long serialVersionUID = 1834535436381958163L;
	private Logger log = Logger.getLogger(CompassSettings.class);
	private String hibernateDialect = null;
	private CqPropertiesBeanSpring cqPropertiesBeanSpring;

	public CompassSettings(String hibernateDialect, CqPropertiesBeanSpring cqPropertiesBeanSpring) {
		super();
		this.hibernateDialect = hibernateDialect;
		this.cqPropertiesBeanSpring = cqPropertiesBeanSpring;
		String luceneStore = getCqPropertiesBeanSpring().getSearch().getLuceneStore();
		if ("luceneFile".equalsIgnoreCase(luceneStore)) {
			String filePath = getCqPropertiesBeanSpring().getDatadir();
			filePath += "/" + "search";
			if(!filePath.startsWith("file://"))
			{
				filePath = "file://" + filePath;
			}
			log.info("using filesystem to store searchindex: " + filePath);
			super.put("compass.engine.connection", filePath);
			super.put("compass.engine.store.lockFactory.type", "nativefs");
		} else {
			String dataSource = getCqPropertiesBeanSpring().getSearch().getSearchDataSource();
			if (dataSource == null) dataSource = getCqPropertiesBeanSpring().getSearch().getXmlDatasource();
			log.info("using database to store searchindex: " + dataSource);
			super.put("compass.engine.store.jdbc.connection.provider.class", "org.compass.core.lucene.engine.store.jdbc.JndiDataSourceProvider");
			// FIXME why does this not work properly?
			// super.put("compass.engine.store.jdbc.managed", "true");
			// super.put("compass.transaction.commitBeforeCompletion", "true");
			super.put("compass.engine.connection", "jdbc://" + dataSource);
			String dialect = this.getJdbcDialect();
			log.info("Setting compass.engine.store.jdbc.dialect to: " + dialect);
			if (dialect != null) super.put("compass.engine.store.jdbc.dialect", dialect);
		}
		super.put("compass.transaction.factory", "org.compass.spring.transaction.SpringSyncTransactionFactory");
		super.put("compass.engine.analyzer.default.type", "org.apache.lucene.analysis.standard.StandardAnalyzer");
		super.put("compass.engine.analyzer.search.type", "org.apache.lucene.analysis.standard.StandardAnalyzer");
		super.put("compass.engine.highlighter.default.fragmenter.simple.size", "250");
		super.put("compass.engine.queryParser.default.defaultOperator", "AND");
		super.put("compass.engine.optimizer.schedule.period", "300");
		// super.put("compass.engine.analyzer.default.name", "German2");
		

	}

	public CqPropertiesBeanSpring getCqPropertiesBeanSpring() {
		return cqPropertiesBeanSpring;
	}

	public void setCqPropertiesBeanSpring(CqPropertiesBeanSpring cqPropertiesBeanSpring) {
		this.cqPropertiesBeanSpring = cqPropertiesBeanSpring;
	}

	private String getJdbcDialect() {
		if (this.hibernateDialect != null) {
			if (this.hibernateDialect.indexOf("Oracle") > 0) return "org.apache.lucene.store.jdbc.dialect.OracleDialect";
			if (this.hibernateDialect.indexOf("SQLServer") > 0) return "org.apache.lucene.store.jdbc.dialect.SQLServerDialect";
			if (this.hibernateDialect.indexOf("MySQL") > 0) return "org.apache.lucene.store.jdbc.dialect.MySQLDialect";
		}
		return null;
	}
}
