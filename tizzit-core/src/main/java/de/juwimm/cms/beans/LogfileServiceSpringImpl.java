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
// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package de.juwimm.cms.beans;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.beans.foreign.TizzitPropertiesBeanSpring;
import de.juwimm.cms.beans.vo.LogfileValue;
import de.juwimm.cms.model.HostHbm;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.vo.HostValue;
import de.juwimm.cms.vo.SiteValue;
import de.juwimm.cms.vo.UnitSlimValue;

/**
 * @see de.juwimm.cms.beans.LogfileServiceSpring
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class LogfileServiceSpringImpl extends LogfileServiceSpringBase {
	private static Logger log = Logger.getLogger(LogfileServiceSpringImpl.class);
	private static final String NO_RUNNER = "null";
	private HashMap<String, Integer> pathCache = new HashMap<String, Integer>();
	private HashMap<String, Integer> siteCache = new HashMap<String, Integer>();
	private final HashMap<Integer, Collection<HostValue>> hostsCache = new HashMap<Integer, Collection<HostValue>>();
	private final LogfileValue logfileValue = new LogfileValue();
	private boolean processRunning = false;
	private TizzitPropertiesBeanSpring tizzitPropertiesBeanSpring = null;

	@Autowired
	private WebServiceSpring webServiceSpring;

	public TizzitPropertiesBeanSpring getTizzitPropertiesBeanSpring() {
		return tizzitPropertiesBeanSpring;
	}

	public void setTizzitPropertiesBeanSpring(TizzitPropertiesBeanSpring tizzitPropertiesBeanSpring) {
		this.tizzitPropertiesBeanSpring = tizzitPropertiesBeanSpring;
		de.juwimm.cms.beans.foreign.TizzitPropertiesBeanSpring.Logfile lfv = getTizzitPropertiesBeanSpring().getLogfile();
		this.logfileValue.setLogfileSource(lfv.getLogfileSource());
		this.logfileValue.setLogfileDestDir(lfv.getLogfileDestDir());
		this.logfileValue.setPurgeLogfileDestDirOnExit(lfv.isPurgeLogfileDestDirOnExit());
		this.logfileValue.setCutVHost(lfv.isCutVHost());
		this.logfileValue.setRunWindows(lfv.isRunWindows());
		this.logfileValue.setPrepareScriptName(lfv.getPrepareScriptName());
		this.logfileValue.setScriptName(lfv.getScriptName());
		this.logfileValue.setStatsDestDir(lfv.getStatsDestDir());
		this.logfileValue.setListDomainsPerUnit(lfv.isListDomainsPerUnit());
		this.logfileValue.setEnabled(lfv.isEnabled());
	}

	/**
	 * @see de.juwimm.cms.beans.LogfileServiceSpring#getLogfileValue()
	 */
	@Override
	protected LogfileValue handleGetLogfileValue() throws Exception {
		return this.logfileValue;
	}

	/**
	 * @see de.juwimm.cms.beans.LogfileServiceSpring#setLogfileValue(de.juwimm.cms.beans.vo.LogfileValue)
	 */
	@Override
	protected void handleSetLogfileValue(LogfileValue logfileValue) throws Exception {
		this.logfileValue.setLogfileSource(logfileValue.getLogfileSource());
		this.logfileValue.setLogfileDestDir(logfileValue.getLogfileDestDir());
		this.logfileValue.setPurgeLogfileDestDirOnExit(logfileValue.isPurgeLogfileDestDirOnExit());
		this.logfileValue.setCutVHost(logfileValue.isCutVHost());
		this.logfileValue.setRunWindows(logfileValue.isRunWindows());
		this.logfileValue.setPrepareScriptName(logfileValue.getPrepareScriptName());
		this.logfileValue.setScriptName(logfileValue.getScriptName());
		this.logfileValue.setStatsDestDir(logfileValue.getStatsDestDir());
		this.logfileValue.setListDomainsPerUnit(logfileValue.isListDomainsPerUnit());

		this.logfileValue.setEnabled(logfileValue.isEnabled());
	}

	/**
	 * @see de.juwimm.cms.beans.LogfileServiceSpring#startParsing()
	 */
	@Override
	protected void handleStartParsing() throws Exception {
		if (this.logfileValue.isEnabled()) {
			log.info("Starting Logfile Cronjob");
			if (this.processRunning) {
				log.info("Another process for parsing is already running, I quit");
				return;
			}
			// check script
			String postScriptName = this.logfileValue.getScriptName();
			if (postScriptName != null && !"".equalsIgnoreCase(postScriptName) && !NO_RUNNER.equalsIgnoreCase(postScriptName)) {
				File scriptFile = new File(postScriptName);
				if (!scriptFile.exists()) {
					log.warn("Script \"" + postScriptName + "\" not found, Logfile Cronjob is terminating!");
					return;
				}
			}
			this.processRunning = true;
			if ((this.logfileValue.getPrepareScriptName() == null) || ("".equalsIgnoreCase(this.logfileValue.getPrepareScriptName())) || NO_RUNNER.equalsIgnoreCase(this.logfileValue.getPrepareScriptName())) {
				log.info("No script is executed before parsing logfile");
			} else {
				File prepareScript = new File(this.logfileValue.getPrepareScriptName());
				if (prepareScript.exists()) {
					Process preparer = null;
					try {
						preparer = Runtime.getRuntime().exec(logfileValue.getPrepareScriptName());
					} catch (IOException e) {
						log.error(e.getMessage());
					}
					if (preparer != null) {
						int returnCode = 0;
						try {
							returnCode = preparer.waitFor();
							//this.wait(120000);
						} catch (InterruptedException e1) {
							log.error(e1.getMessage());
							try {
								preparer.destroy();
							} catch (Exception e2) {
							}
						}
						log.info("Script " + this.logfileValue.getPrepareScriptName() + " returned with code " + returnCode);
					} else {
						log.error("Could not run the script " + this.logfileValue.getPrepareScriptName());
					}
				} else {
					log.warn("Script \"" + this.logfileValue.getPrepareScriptName() + "\" not found!");
				}
			}
			BufferedReader br = null;
			HashMap<Integer, UnitFile> unitFileMap = null;
			HashMap<Integer, SiteFile> siteFileMap = null;
			FileWriter restFile = null;

			pathCache = new HashMap<String, Integer>();
			siteCache = new HashMap<String, Integer>();
			this.logfileValue.setLines(0);
			this.logfileValue.setCacheHit(0);
			this.logfileValue.setCacheMiss(0);
			this.logfileValue.setSiteCacheHit(0);
			this.logfileValue.setSiteCacheMiss(0);

			Collection units = null;
			Collection sites = null;
			Date start = new Date();
			try {
				if (this.logfileValue.getLogfileSource().endsWith(".gz")) {
					GZIPInputStream gzin = new GZIPInputStream(new FileInputStream(this.logfileValue.getLogfileSource()));
					InputStreamReader inStream = new InputStreamReader(gzin);
					br = new BufferedReader(inStream);
				} else {
					br = new BufferedReader(new FileReader(this.logfileValue.getLogfileSource()));
				}
				log.info("Preparing Units...");
				units = super.getViewServiceSpring().getAllUnits();
				Iterator it = units.iterator();
				unitFileMap = new HashMap<Integer, UnitFile>(units.size() + 1);
				while (it.hasNext()) {
					UnitHbm unit = (UnitHbm) it.next();
					try {
						SiteHbm site = unit.getSite();
						if (!hostsCache.containsKey(site.getSiteId())) {
							Collection hosts = site.getHost();
							hostsCache.put(site.getSiteId(), this.getHostValues(hosts));
						}
						Collection<HostValue> hosts = null;
						if (this.logfileValue.isListDomainsPerUnit()) {
							hosts = this.getHosts4Unit(unit.getUnitId(), hostsCache.get(site.getSiteId()));
						} else {
							hosts = hostsCache.get(site.getSiteId());
						}
						unitFileMap.put(unit.getUnitId(), new UnitFile(unit, hosts));
					} catch (Exception nsee) {
						log.error("unit " + unit.getUnitId() + " does not belong to any site! " + nsee.getMessage());
					}
				}
				log.info("Preparing Sites...");
				sites = webServiceSpring.getAllSites();
				it = sites.iterator();
				siteFileMap = new HashMap<Integer, SiteFile>(sites.size());
				while (it.hasNext()) {
					SiteValue site = (SiteValue) it.next();
					if (!hostsCache.containsKey(site.getSiteId())) {
						Collection hosts = getSiteHbmDao().load(site.getSiteId()).getHost();
						hostsCache.put(site.getSiteId(), this.getHostValues(hosts));
					}
					siteFileMap.put(site.getSiteId(), new SiteFile(site, hostsCache.get(site.getSiteId())));
				}
				log.info("Preparing Destination-Directories...");
				File destDir = new File(this.logfileValue.getLogfileDestDir());
				if (!destDir.exists()) destDir.mkdirs();

				File ff = new File(destDir, "not-assignable-access.log");
				ff.createNewFile();
				restFile = new FileWriter(ff, false);

				String line = null;
				Integer unitId = null;
				Integer siteId = null;
				String host = "";
				String viewLanguage = "";
				String path = "";
				start = new Date();
				log.info("Start Parsing...");
				while ((line = br.readLine()) != null) {
					unitId = null;
					siteId = null;
					host = "";
					viewLanguage = "";
					path = "";
					try {
						StringBuffer sbPath = new StringBuffer();
						StringTokenizer st = new StringTokenizer(line, " ");
						host = st.nextToken();
						st = new StringTokenizer(line, "\"");
						st.nextToken();
						st = new StringTokenizer(st.nextToken(), " ");
						st.nextToken();
						st = new StringTokenizer(st.nextToken(), "/");
						if (st.hasMoreTokens()) {
							viewLanguage = st.nextToken();
						}
						while (st.hasMoreTokens()) {
							sbPath.append("/").append(st.nextToken());
						}
						path = sbPath.toString();
						int portPosition = host.lastIndexOf(":");
						if (portPosition > 0) {
							host = host.substring(0, portPosition);
						}
						siteId = this.getSiteForHost(host);
					} catch (Exception exe) {
					}
					if (siteId == null) {
						restFile.write(line + "\n");
					} else {
						if (this.logfileValue.isCutVHost()) {
							line = this.cutVHost(line);
						}
						siteFileMap.get(siteId).write(line + "\n");
						unitId = this.getUnitForPath(siteId, viewLanguage, "browser", path);
						if (unitId != null) {
							unitFileMap.get(unitId).write(line + "\n");
						}
					}
					if (log.isDebugEnabled()) {
						log.debug("unitId " + unitId + " host " + host + " viewLanguage " + viewLanguage + " path " + path);
					}
					this.logfileValue.setLines(this.logfileValue.getLines() + 1);
					if (this.logfileValue.getLines() % 10000 == 0) {
						if (log.isDebugEnabled()) log.debug("Calling Garbage Collector...");
						System.gc();
					}
				}
			} catch (Exception exe) {
				log.error("Error occured", exe);
			} finally {
				System.gc();
				Date end = new Date();
				log.info("Processed " + this.logfileValue.getLines() + " Lines of Logfiles in " + this.calcHMS(end.getTime() - start.getTime()));
				try {
					br.close();
				} catch (Exception exi) {
				}
				try {
					restFile.close();
				} catch (Exception exi) {
				}
				Iterator it = unitFileMap.values().iterator();
				while (it.hasNext()) {
					try {
						((UnitFile) it.next()).close();
					} catch (Exception exi) {
					}
				}
				it = siteFileMap.values().iterator();
				while (it.hasNext()) {
					((SiteFile) it.next()).close();
				}
				this.processRunning = false;
			}
			Iterator it = unitFileMap.values().iterator();
			while (it.hasNext()) {
				UnitFile uf = ((UnitFile) it.next());
				try {
					uf.runRunner();
				} catch (Exception exe) {
					log.error("Error occured during Runner of " + uf.getUnit().getUnitId() + " (" + uf.getUnit().getName() + ") " + exe.getMessage());
				}
			}
			it = siteFileMap.values().iterator();
			while (it.hasNext()) {
				SiteFile sf = ((SiteFile) it.next());
				try {
					sf.runRunner();
				} catch (Exception exe) {
					log.error("Error occured during Runner of " + sf.getSite().getSiteId() + " (" + sf.getSite().getName() + ") " + exe.getMessage());
				}
			}
			if ((this.logfileValue.isPurgeLogfileDestDirOnExit()) && (!(NO_RUNNER.equalsIgnoreCase(this.logfileValue.getScriptName())))) {
				try {
					File logFileDir = new File(logfileValue.getLogfileDestDir());
					if (log.isDebugEnabled()) log.debug("Purging Dir: " + logFileDir.getPath());
					this.deleteDir(logFileDir);
				} catch (Exception exe) {
					log.warn("Could not clean Logfile Dir because of: " + exe.getMessage());
				}
			}
			log.info("Finished Logfile Cronjob");
			log.info("CacheHits: " + this.logfileValue.getCacheHit() + "\t\tCacheMiss: " + this.logfileValue.getCacheMiss());
			log.info("SiteCacheHits: " + this.logfileValue.getSiteCacheHit() + "\tSiteCacheMiss: " + this.logfileValue.getSiteCacheMiss());
			log.info("Number of Units: " + units.size() + "\tNumber of Sites: " + sites.size());
			this.processRunning = false;
		}
	}

	/**
	 * 
	 */
	public class UnitFile {
		private FileWriter file = null;
		private UnitSlimValue unit = null;
		private SiteValue site = null;
		private Collection<HostValue> hosts = null;
		private String fileName = "";

		public UnitFile(UnitHbm unit, Collection<HostValue> hosts) {
			this.unit = unit.getUnitSlimValue();
			this.site = unit.getSite().getSiteValue();
			this.hosts = hosts;
		}

		public void write(String line) {
			if (line != null && !line.equalsIgnoreCase("")) {
				if (this.file == null) {
					try {
						File fleDir = new File(logfileValue.getLogfileDestDir() + File.separator + File.separator + "site-" + this.site.getSiteId() + "-" + this.site.getShortName() + File.separator + this.unit.getUnitId());
						fleDir.mkdirs();
						File fle = new File(fleDir, "access.log");
						this.fileName = fle.getPath();
						if (log.isDebugEnabled()) log.debug("fileName " + this.fileName);
						fle.createNewFile();
						this.file = new FileWriter(fle, false);
					} catch (Exception exe) {
						log.error("Error occured", exe);
					}
				}
				try {
					this.file.write(line);
				} catch (Exception exe) {
					log.error("Error occured", exe);
				}
			}
		}

		public void close() {
			if (this.file != null) {
				try {
					this.file.close();
				} catch (Exception exe) {
				}
			}
		}

		public void runRunner() throws Exception {
			if (!this.fileName.equalsIgnoreCase("")) {
				if (!(NO_RUNNER.equalsIgnoreCase(logfileValue.getScriptName()))) {
					String siteName = site.getName().trim();
					String unitName = this.unit.getName().trim();
					Integer siteId = this.site.getSiteId();
					Integer unitId = this.unit.getUnitId();
					String defaultHostName = null;
					if (logfileValue.isListDomainsPerUnit()) {
						defaultHostName = getDefaultHost4Unit(this.unit.getUnitId(), this.hosts);
					} else {
						defaultHostName = getDefaultHostName(this.hosts);
					}
					if (defaultHostName.length() == 0) {
						// take first host of site
						defaultHostName = getDefaultHostName(hostsCache.get(siteId));
					}
					String hostNames = getHostsAsString(this.hosts);
					if (logfileValue.isRunWindows()) {
						// substitute all blanks by _
						siteName = siteName.replaceAll("[ ]", "_");
						unitName = unitName.replaceAll("[ ]", "_");
					}
					File statsDir = new File(logfileValue.getStatsDestDir() + File.separator + File.separator + site.getSiteId() + File.separator + this.unit.getUnitId());
					statsDir.mkdirs();
					String[] cmdArr = null;
					if (!logfileValue.isRunWindows()) {
						cmdArr = new String[11];
						cmdArr[0] = logfileValue.getScriptName();
						cmdArr[1] = this.fileName;
						cmdArr[2] = statsDir.getPath();
						cmdArr[3] = "\"" + unitName + "\"";
						cmdArr[4] = "\"" + siteName + "\"";
						cmdArr[5] = "\"" + siteId + "\"";
						cmdArr[6] = "\"" + unitId + "\"";
						cmdArr[7] = "\"" + defaultHostName + "\"";
						cmdArr[8] = "\"" + hostNames + "\"";
						cmdArr[9] = "\"\""; //ip's to ignore
						cmdArr[10] = "\"\""; //language stats are presented
					} else {
						cmdArr = new String[14];
						cmdArr[0] = logfileValue.getScriptName();
						cmdArr[1] = "-F";
						cmdArr[2] = "clf";
						cmdArr[3] = "-p";
						cmdArr[4] = "-n";
						cmdArr[5] = siteName.replaceAll("\\s", "");
						cmdArr[6] = "-o";
						cmdArr[7] = statsDir.getPath();
						cmdArr[8] = "-t";
						cmdArr[9] = "\"Statistik f\u00FCr " + unitName + " auf Server\"";
						cmdArr[10] = this.fileName;
						cmdArr[11] = ">>";
						cmdArr[12] = logfileValue.getScriptName().charAt(0) + ":\\tizzit\\data\\log\\webalizer\\webalizer.log";
						cmdArr[13] = "2>&1";
						writeCommand(stringArray2String(cmdArr));
					}
					if (log.isDebugEnabled()) {
						log.debug("Calling Runner with " + stringArray2String(cmdArr));
					}
					if (logfileValue.isRunWindows()) return;
					Process p = Runtime.getRuntime().exec(cmdArr);
					int runResult = 0;
					if (logfileValue.isPurgeLogfileDestDirOnExit()) {
						runResult = p.waitFor();
					}
					if (runResult != 0) {
						log.warn("Execution of \"" + stringArray2String(cmdArr) + "\" terminated with returncode " + runResult);
					}
				} else {
					log.info("No processing after parsing");
				}
			}
		}

		public UnitSlimValue getUnit() {
			return this.unit;
		}
	}

	/**
	 * 
	 */
	public class SiteFile {
		private FileWriter siteWriter = null;
		private SiteValue site = null;
		private Collection<HostValue> hosts = null;
		private String fileName = "";

		public SiteFile(SiteValue site, Collection<HostValue> hosts) {
			this.site = site;
			this.hosts = hosts;
		}

		public void write(String line) {
			if (line != null && !line.equalsIgnoreCase("")) {
				if (this.siteWriter == null) {
					try {
						File fleDir = new File(logfileValue.getLogfileDestDir() + File.separator + File.separator + "site-" + this.site.getSiteId() + "-" + this.site.getShortName());
						fleDir.mkdirs();
						File fle = new File(fleDir, "access.log");
						this.fileName = fle.getPath();
						if (log.isDebugEnabled()) log.debug("fileName " + this.fileName);
						fle.createNewFile();
						this.siteWriter = new FileWriter(fle, false);
					} catch (Exception exe) {
						log.error("Error occured", exe);
					}
				}
				try {
					this.siteWriter.write(line);
				} catch (Exception exe) {
					log.error("Error occured", exe);
				}
			}
		}

		public void close() {
			if (this.siteWriter != null) {
				try {
					this.siteWriter.close();
				} catch (Exception exe) {
				}
			}
		}

		public void runRunner() throws Exception {
			if (!this.fileName.equalsIgnoreCase("")) {
				if (!(NO_RUNNER.equalsIgnoreCase(logfileValue.getScriptName()))) {
					String siteName = this.site.getName().trim();
					Integer siteId = this.site.getSiteId();
					Integer rootUnitId = null;
					try {
						rootUnitId = getSiteHbmDao().load(siteId).getRootUnit().getUnitId();
					} catch (Exception e) {
						log.error("Error resolving root-unit for site \"" + siteName + "\": " + e.getMessage(), e);
					}
					String defaultHostName = getDefaultHostName(this.hosts);
					String hostNames = getHostsAsString(this.hosts);
					if (logfileValue.isRunWindows()) {
						// substitute all blanks by _
						siteName = siteName.replaceAll("[ ]", "_");
					}
					File statsDir = new File(logfileValue.getStatsDestDir() + File.separator + this.site.getSiteId());
					statsDir.mkdirs();
					String[] cmdArr = null;
					if (!logfileValue.isRunWindows()) {
						cmdArr = new String[11];
						cmdArr[0] = logfileValue.getScriptName();
						cmdArr[1] = this.fileName;
						cmdArr[2] = statsDir.getPath();
						cmdArr[3] = "\"" + siteName + "\"";
						cmdArr[4] = "\"" + siteName + "\"";
						cmdArr[5] = "\"" + siteId + "\"";
						cmdArr[6] = "\"" + (rootUnitId != null ? rootUnitId : "") + "\"";
						cmdArr[7] = "\"" + defaultHostName + "\"";
						cmdArr[8] = "\"" + hostNames + "\"";
						cmdArr[9] = "\"\""; //ip's to ignore
						cmdArr[10] = "\"\""; //language stats are presented
					} else {
						cmdArr = new String[14];
						cmdArr[0] = logfileValue.getScriptName();
						cmdArr[1] = "-F";
						cmdArr[2] = "clf";
						cmdArr[3] = "-p";
						cmdArr[4] = "-n";
						cmdArr[5] = siteName.replaceAll("\\s", "");
						cmdArr[6] = "-o";
						cmdArr[7] = statsDir.getPath();
						cmdArr[8] = "-t";
						cmdArr[9] = "\"Statistik f\u00FCr " + siteName + " auf Server\"";
						cmdArr[10] = fileName;
						cmdArr[11] = ">>";
						cmdArr[12] = logfileValue.getScriptName().charAt(0) + ":\\tizzit\\data\\log\\webalizer\\webalizer.log";
						cmdArr[13] = "2>&1";
						writeCommand(stringArray2String(cmdArr));
					}
					if (log.isDebugEnabled()) {
						log.debug("Calling Runner with " + stringArray2String(cmdArr));
					}
					if (logfileValue.isRunWindows()) return;
					Process p = Runtime.getRuntime().exec(cmdArr);
					int runResult = 0;
					if (logfileValue.isPurgeLogfileDestDirOnExit()) {
						runResult = p.waitFor();
					}
					if (runResult != 0) {
						log.warn("Execution of \"" + stringArray2String(cmdArr) + "\" terminated with returncode " + runResult);
					}
				} else {
					log.info("No processing after parsing");
				}
			}
		}

		public SiteValue getSite() {
			return this.site;
		}
	}

	private boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = this.deleteDir(new File(dir, children[i]));
				if (!success) { return false; }
			}
		}
		return dir.delete();
	}

	private Integer getUnitForPath(Integer siteId, String viewLanguage, String viewType, String path) {
		Integer unitId = pathCache.get(siteId + "2" + viewLanguage + "3" + viewType + "4" + path);
		if (unitId == null) {
			// first search
			this.logfileValue.setCacheMiss(this.logfileValue.getCacheMiss() + 1);
			// no language - no resolution
			if (viewLanguage != null && viewLanguage.length() > 0) {
				try {
					String lang = viewLanguage;
					if (lang != null && lang.length() > 49) lang = lang.substring(0, 49);
					unitId = super.getViewServiceSpring().getUnitForPath(siteId, lang, viewType, path);
				} catch (Exception exe) {
					log.error("Error occured", exe);
				}
			}
			if (unitId == null) {
				unitId = new Integer(0);
			}
			pathCache.put(siteId + "2" + viewLanguage + "3" + viewType + "4" + path, unitId);
			if (unitId.intValue() == 0) unitId = null;
		} else if (unitId.intValue() == 0) {
			// prior search without result
			this.logfileValue.setCacheHit(this.logfileValue.getCacheHit() + 1);
			unitId = null;
		} else {
			this.logfileValue.setCacheHit(this.logfileValue.getCacheHit() + 1);
		}
		return unitId;
	}

	private Integer getSiteForHost(String hostName) {
		Integer siteId = siteCache.get(hostName);
		if (siteId == null) {
			// first call for this hostName
			this.logfileValue.setSiteCacheMiss(this.logfileValue.getSiteCacheMiss() + 1);
			try {
				siteId = webServiceSpring.getSiteForHost(hostName);
				if ((siteId == null) && (hostName.startsWith("www."))) {
					if (log.isDebugEnabled()) log.debug("Trying host without www...");
					hostName = hostName.substring(4);
					siteId = webServiceSpring.getSiteForHost(hostName);
				}
				if ((siteId == null) && (!hostName.startsWith("www."))) {
					if (log.isDebugEnabled()) log.debug("Trying host with additional www...");
					hostName = "www." + hostName;
					siteId = webServiceSpring.getSiteForHost(hostName);
				}
				if (siteId == null) {
					siteCache.put(hostName, new Integer(0));
				} else {
					siteCache.put(hostName, siteId);
				}
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		} else if (siteId.intValue() == 0) {
			// no site found on prior search
			this.logfileValue.setSiteCacheHit(this.logfileValue.getSiteCacheHit() + 1);
			siteId = null;
		} else {
			// site found
			this.logfileValue.setSiteCacheHit(this.logfileValue.getSiteCacheHit() + 1);
		}
		return (siteId);
	}

	/*
	 * Method searches for the first blank and returns the rest of the string after the blank.
	 */
	public String cutVHost(String line) {
		return (line.substring(line.indexOf(32) + 1));
	}

	private String stringArray2String(String[] sa) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < sa.length; i++) {
			if (sb.length() > 0) sb.append(" ");
			sb.append(sa[i]);
		}
		return sb.toString();
	}

	private void writeCommand(String command) throws IOException {
		File statsDirFile = new File(this.logfileValue.getStatsDestDir() + File.separator + "runWebalizer.bat");
		BufferedWriter bw = new BufferedWriter(new FileWriter(statsDirFile, true));
		bw.write(command);
		bw.newLine();
		bw.flush();
		bw.close();
	}

	private String getHostsAsString(Collection hosts) {
		ArrayList<String> hostNames = new ArrayList<String>();

		Iterator it = hosts.iterator();
		while (it.hasNext()) {
			HostValue host = (HostValue) it.next();
			hostNames.add(host.getHostName());
		}

		return this.stringArray2String(hostNames.toArray(new String[0]));
	}

	/*
	 * should be replaced by a real default host per site,
	 * this is just a fake taking the first host from the list
	 */
	private String getDefaultHostName(Collection<HostValue> hosts) {
		String result = "";

		if (hosts.size() > 0) {
			Iterator<HostValue> it = hosts.iterator();
			if (it.hasNext()) {
				HostValue host = it.next();
				result = host.getHostName();
			}
		}

		return result;
	}

	private Collection<HostValue> getHostValues(Collection<HostHbm> hosts) {
		ArrayList<HostValue> hostValues = new ArrayList<HostValue>();
		Iterator iter = hosts.iterator();

		while (iter.hasNext()) {
			hostValues.add(((HostHbm) iter.next()).getHostValue());
		}

		return hostValues;
	}

	/**
	 * Method for filtering all Hosts for the UnitId given
	 * @param unitId the UnitId to filter the Hosts
	 * @param hosts4Site Collection of all Hosts for one Site
	 * @return a Collection containing only Hosts for the given UnitId
	 */
	private Collection<HostValue> getHosts4Unit(Integer unitId, Collection<HostValue> hosts4Site) {
		Collection<HostValue> hosts4Unit = new ArrayList<HostValue>();
		Iterator<HostValue> it = hosts4Site.iterator();
		while (it.hasNext()) {
			HostValue hostValue = it.next();
			if (hostValue.getUnitId() != null) {
				if (hostValue.getUnitId().intValue() == unitId.intValue()) {
					hosts4Unit.add(hostValue);
				}
			} else {
				// no unit, examine startPage
				if (hostValue.getStartPageId() != null) {
					try {
						Integer startPageUnitId = super.getViewServiceSpring().getUnit4ViewComponent(hostValue.getStartPageId());
						if (unitId.intValue() == startPageUnitId.intValue()) {
							hosts4Unit.add(hostValue);
						}
					} catch (Exception e) {
						log.warn("Error determining unit for host: " + e.getMessage(), e);
					}
				}
			}
		}
		return hosts4Unit;
	}

	private String getDefaultHost4Unit(Integer unitId, Collection<HostValue> hosts4Site) {
		String defaultHost = null;
		Iterator<HostValue> it = hosts4Site.iterator();
		while (it.hasNext()) {
			HostValue hostValue = it.next();
			if (hostValue.getUnitId() != null) {
				if (hostValue.getUnitId().intValue() == unitId.intValue()) {
					defaultHost = hostValue.getHostName();
					break;
				}
			}
		}
		if (defaultHost == null) {
			if (!hosts4Site.isEmpty()) {
				it = hosts4Site.iterator();
				if (it.hasNext()) defaultHost = it.next().getHostName();
			} else {
				// should not occur, site should have at least one host
				defaultHost = "";
			}
		}

		return defaultHost;
	}

	public String calcHMS(long timeInSeconds) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return (dateFormat.format(new java.util.Date(timeInSeconds)));
	}

}
