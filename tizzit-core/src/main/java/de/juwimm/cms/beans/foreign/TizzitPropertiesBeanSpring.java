/**
 * Copyright (c) 18.02.2009 Sascha-Matthias Kulawik <sascha-matthias.kulawik@juwimm.com>
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

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.tizzit.core.classloading.ExternalLibClassLoaderManager;

/**
 * @author <a href="mailto:sascha-matthias.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id: TizzitPropertiesBeanSpring.java 6 2009-07-30 14:05:05Z skulawik@gmail.com $
 * @since tizzitcms-core 18.02.2009
 */
public class TizzitPropertiesBeanSpring {
	private static final int tizzitPropertiesUCSversionNeeded = 30000;
	private final Logger log = Logger.getLogger(TizzitPropertiesBeanSpring.class);
	private String mandatorParent;
	private String cmsTemplatesPath;
	private String datadir;
	private String datasource;
	private String jnlpHost;
	private String jnlpPort;
	private boolean liveserver;
	private String statsDir;
	
	private String deployDir;
	private String deployUser;
	 
	private String deployPassword;


	private String mailDS;
	private int version;
	private String hibernateSqlDialect;
	private boolean hibernateShowSql;
	private String editionCronExpression;
	private String indexExternalSiteCronExpression;

	public String getIndexExternalSiteCronExpression() {
		return indexExternalSiteCronExpression;
	}

	public void setIndexExternalSiteCronExpression(String indexExternalSiteCronExpression) {
		this.indexExternalSiteCronExpression = indexExternalSiteCronExpression;
	}

	private ClientMailAppenderConfig clientMailAppenderConfig = new ClientMailAppenderConfig();
	private String updateDeployStatusCronExpression;

	public String getUpdateDeployStatusCronExpression() {
		return updateDeployStatusCronExpression;
	}

	public void setUpdateDeployStatusCronExpression(String updateDeployStatusCronExpression) {
		this.updateDeployStatusCronExpression = updateDeployStatusCronExpression;
	}

	public ClientMailAppenderConfig getClientMailAppenderConfig() {
		return clientMailAppenderConfig;
	}

	public void setClientMailAppenderConfig(ClientMailAppenderConfig mailAppenderConfig) {
		this.clientMailAppenderConfig = mailAppenderConfig;
	}

	public String getMailDS() {
		return mailDS;
	}

	public void setMailDS(String mailDS) {
		this.mailDS = mailDS;
		System.setProperty("tizzit.mailDS", mailDS);
	}


	public String getHibernateSqlDialect() {
		return hibernateSqlDialect;
	}

	public void setHibernateSqlDialect(String hibernateSqlDialect) {
		this.hibernateSqlDialect = hibernateSqlDialect;
	}

	public boolean isHibernateShowSql() {
		return hibernateShowSql;
	}

	public void setHibernateShowSql(boolean hibernateShowSql) {
		this.hibernateShowSql = hibernateShowSql;
	}

	private Logfile logfile = new Logfile();
	private Cocoon cocoon = new Cocoon();
	private Search search = new Search();
	private ExternalLib externalLib = new ExternalLib();

	public static class Search {
		private String luceneStore;
		private String searchDataSource;
		private String xmlDatasource;
		private String xmlDb;
		private String xindiceHost;
		private String xindicePort;
		private int maxClauseCount = 32768;
		private boolean indexerEnabled;
		private String indexerCronExpression;

		public boolean isIndexerEnabled() {
			return indexerEnabled;
		}

		public void setIndexerEnabled(boolean indexerEnabled) {
			this.indexerEnabled = indexerEnabled;
		}

		public String getIndexerCronExpression() {
			return indexerCronExpression;
		}

		public void setIndexerCronExpression(String indexerCronExpression) {
			this.indexerCronExpression = indexerCronExpression;
		}

		public int getMaxClauseCount() {
			return maxClauseCount;
		}

		public void setMaxClauseCount(int maxClauseCount) {
			this.maxClauseCount = maxClauseCount;
		}

		public String getLuceneStore() {
			return luceneStore;
		}

		public void setLuceneStore(String luceneStore) {
			this.luceneStore = luceneStore;
		}

		public String getSearchDataSource() {
			return searchDataSource;
		}

		public void setSearchDataSource(String searchDataSource) {
			this.searchDataSource = searchDataSource;
		}

		public String getXmlDatasource() {
			return xmlDatasource;
		}

		public void setXmlDatasource(String xmlDatasource) {
			this.xmlDatasource = xmlDatasource;
		}

		public String getXmlDb() {
			return xmlDb;
		}

		public void setXmlDb(String xmlDb) {
			this.xmlDb = xmlDb;
		}

		public String getXindiceHost() {
			return xindiceHost;
		}

		public void setXindiceHost(String xindiceHost) {
			this.xindiceHost = xindiceHost;
		}

		public String getXindicePort() {
			return xindicePort;
		}

		public void setXindicePort(String xindicePort) {
			this.xindicePort = xindicePort;
		}
	}

	/**
	 *
	 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
	 * company Juwi MacMillan Group GmbH, Walsrode, Germany
	 * @version $Id: TizzitPropertiesBeanSpring.java 369 2009-10-07 19:10:56Z eduard.siebert@online.de $
	 * @since tizzit-core 29.09.2009
	 */
	public static class ExternalLib {
		private String path;
		private boolean reloadingEnabled;

		/**
		 * @return the path
		 */
		public String getPath() {
			return path;
		}

		/**
		 * @param path the path to set
		 */
		public void setPath(String path) {
			this.path = path;
		}

		/**
		 * @return the reloadingEnabled
		 */
		public boolean isReloadingEnabled() {
			return reloadingEnabled;
		}

		/**
		 * @param reloadingEnabled the reloadingEnabled to set
		 */
		public void setReloadingEnabled(boolean reloadingEnabled) {
			this.reloadingEnabled = reloadingEnabled;
		}

		/**
		 * @see {@link ExternalLibClassLoaderManager#getFileList(String)}
		 */
		public List<File> getFileList() throws Exception {
			return ExternalLibClassLoaderManager.getFileList(this.path);
		}

		/**
		 * @see {@link ExternalLibClassLoaderManager#getURLList(String)}
		 */
		public List<URL> getURLList() throws Exception {
			return ExternalLibClassLoaderManager.getURLList(this.path);
		}
	}

	public static class Cocoon {
		private String janitorFreeMemoryRatio;
		private String janitorFreeMemory;
		private String janitorHeapSizeRatio;
		private String janitorHeapSize;
		private String cleanupThreadIntervalSecs;
		private String percentToFree;
		private boolean invokeGC;

		public String getJanitorFreeMemoryRatio() {
			return janitorFreeMemoryRatio;
		}

		public void setJanitorFreeMemoryRatio(String janitorFreeMemoryRatio) {
			this.janitorFreeMemoryRatio = janitorFreeMemoryRatio;
		}

		public String getJanitorHeapSizeRatio() {
			return janitorHeapSizeRatio;
		}

		public void setJanitorHeapSizeRatio(String janitorHeapSizeRatio) {
			this.janitorHeapSizeRatio = janitorHeapSizeRatio;
		}

		public String getCleanupThreadIntervalSecs() {
			return cleanupThreadIntervalSecs;
		}

		public void setCleanupThreadIntervalSecs(String cleanupThreadIntervalSecs) {
			this.cleanupThreadIntervalSecs = cleanupThreadIntervalSecs;
		}

		public String getPercentToFree() {
			return percentToFree;
		}

		public void setPercentToFree(String percentToFree) {
			this.percentToFree = percentToFree;
		}

		public boolean isInvokeGC() {
			return invokeGC;
		}

		public void setInvokeGC(boolean invokeGC) {
			this.invokeGC = invokeGC;
		}

		public String getJanitorFreeMemory() {
			return janitorFreeMemory;
		}

		public void setJanitorFreeMemory(String janitorFreeMemory) {
			this.janitorFreeMemory = janitorFreeMemory;
		}

		public String getJanitorHeapSize() {
			return janitorHeapSize;
		}

		public void setJanitorHeapSize(String janitorHeapSize) {
			this.janitorHeapSize = janitorHeapSize;
		}
	}

	public static class ClientMailAppenderConfig {
		public ClientMailAppenderConfig() {
		}

		private String SMTPHost;
		private String from;
		private String to;

		public String getSMTPHost() {
			return SMTPHost;
		}

		public void setSMTPHost(String host) {
			SMTPHost = host;
		}

		public String getFrom() {
			return from;
		}

		public void setFrom(String from) {
			this.from = from;
		}

		public String getTo() {
			return to;
		}

		public void setTo(String to) {
			this.to = to;
		}
	}
	public static class Logfile {
		private String logfileSource = "/tizzit/data/log/apache/access.log";
		private String logfileDestDir = "/tizzit/data/applications/cocoon.war/stats/";
		private boolean purgeLogfileDestDirOnExit = false;
		private boolean cutVHost = false;
		private boolean runWindows = false;
		private String prepareScriptName = "/tizzit/bin/prepare";
		private String scriptName = "/tizzit/bin/run_webalizer";
		private String statsDestDir = "/tizzit/data/applications/cocoon.war/stats/";
		private boolean listDomainsPerUnit = false;
		private boolean enabled = false;
		private String cronExpression;

		public String getCronExpression() {
			return cronExpression;
		}

		public void setCronExpression(String cronExpression) {
			this.cronExpression = cronExpression;
		}

		public String getLogfileSource() {
			return logfileSource;
		}

		public void setLogfileSource(String logfileSource) {
			this.logfileSource = logfileSource;
		}

		public String getLogfileDestDir() {
			return logfileDestDir;
		}

		public void setLogfileDestDir(String logfileDestDir) {
			this.logfileDestDir = logfileDestDir;
		}

		public boolean isPurgeLogfileDestDirOnExit() {
			return purgeLogfileDestDirOnExit;
		}

		public void setPurgeLogfileDestDirOnExit(boolean purgeLogfileDestDirOnExit) {
			this.purgeLogfileDestDirOnExit = purgeLogfileDestDirOnExit;
		}

		public boolean isCutVHost() {
			return cutVHost;
		}

		public void setCutVHost(boolean cutVHost) {
			this.cutVHost = cutVHost;
		}

		public boolean isRunWindows() {
			return runWindows;
		}

		public void setRunWindows(boolean runWindows) {
			this.runWindows = runWindows;
		}

		public String getPrepareScriptName() {
			return prepareScriptName;
		}

		public void setPrepareScriptName(String prepareScriptName) {
			this.prepareScriptName = prepareScriptName;
		}

		public String getScriptName() {
			return scriptName;
		}

		public void setScriptName(String scriptName) {
			this.scriptName = scriptName;
		}

		public String getStatsDestDir() {
			return statsDestDir;
		}

		public void setStatsDestDir(String statsDestDir) {
			this.statsDestDir = statsDestDir;
		}

		public boolean isListDomainsPerUnit() {
			return listDomainsPerUnit;
		}

		public void setListDomainsPerUnit(boolean listDomainsPerUnit) {
			this.listDomainsPerUnit = listDomainsPerUnit;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
	}

	public Logfile getLogfile() {
		return logfile;
	}

	public void setLogfile(Logfile logfile) {
		this.logfile = logfile;
	}

	public String getMandatorParent() {
		return mandatorParent;
	}

	public void setMandatorParent(String mandatorParent) {
		this.mandatorParent = mandatorParent;
	}

	public String getCmsTemplatesPath() {
		return cmsTemplatesPath;
	}

	public void setCmsTemplatesPath(String cmsTemplatesPath) {
		this.cmsTemplatesPath = cmsTemplatesPath;
	}

	public String getDatadir() {
		return datadir;
	}

	public void setDatadir(String datadir) {
		this.datadir = datadir;
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public String getJnlpHost() {
		return jnlpHost;
	}

	public void setJnlpHost(String jnlpHost) {
		this.jnlpHost = jnlpHost;
	}

	public String getJnlpPort() {
		return jnlpPort;
	}

	public void setJnlpPort(String jnlpPort) {
		this.jnlpPort = jnlpPort;
	}

	public boolean isLiveserver() {
		return liveserver;
	}

	public void setLiveserver(boolean liveserver) {
		this.liveserver = liveserver;
	}

	public String getStatsDir() {
		return statsDir;
	}

	public void setStatsDir(String statsDir) {
		this.statsDir = statsDir;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
		if (version < tizzitPropertiesUCSversionNeeded) {
			log.fatal("FAILURE! Please update your Tizzit Properties File to Version: " + tizzitPropertiesUCSversionNeeded);
			System.exit(-1);
		}
	}

	public Cocoon getCocoon() {
		return cocoon;
	}

	public void setCocoon(Cocoon cocoon) {
		this.cocoon = cocoon;
	}

	public Search getSearch() {
		return search;
	}

	public void setSearch(Search search) {
		this.search = search;
	}

	public void setEditionCronExpression(String editionCronExpression) {
		this.editionCronExpression = editionCronExpression;
	}

	public String getEditionCronExpression() {
		return editionCronExpression;
	}

	/**
	 * @return the externalLib
	 */
	public ExternalLib getExternalLib() {
		return externalLib;
	}

	/**
	 * @param externalLib the externalLib to set
	 */
	public void setExternalLib(ExternalLib externalLib) {
		this.externalLib = externalLib;
	}
	
	/**
	 	 * @return the deployDir
	 	 */
	 	public String getDeployDir() {
	 		return deployDir;
	 	}
	 
	 	/**
	 	 * @param deployDir the deployDir to set
	 	 */
	 	public void setDeployDir(String deployDir) {
	 		this.deployDir = deployDir;
	 	}
	 
	 	/**
	 	 * @return the deployUser
	 	 */
	 	public String getDeployUser() {
	 		return deployUser;
	 	}
	 
	 	/**
	 	 * @param deployUser the deployUser to set
	 	 */
	 	public void setDeployUser(String deployUser) {
	 		this.deployUser = deployUser;
	 	}
	 
	 	/**
	 	 * @return the deployPassword
	 	 */
	 	public String getDeployPassword() {
	 		return deployPassword;
	 	}
	 
	 	/**
	 	 * @param deployPassword the deployPassword to set
	 	 */
	 	public void setDeployPassword(String deployPassword) {
	 		this.deployPassword = deployPassword;
	 	}


}
