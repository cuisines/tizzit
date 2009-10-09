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
package de.juwimm.cms.beans;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.tizzit.core.classloading.ClassloadingHelper;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import de.juwimm.cms.plugins.server.Request;
import de.juwimm.cms.plugins.server.Response;
import de.juwimm.cms.plugins.server.TizzitPlugin;

/**
 *
 * @author <a href="mailto:j2ee@juwimm.com">Sascha-Matthias Kulawik</a>
 * company Juwi|MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since tizzit-core 08.10.2009
 */
public class PluginManagement {
	private Logger log = Logger.getLogger(PluginManagement.class);
	private PluginConfig<String, String> pluginConfig = null;

	public PluginManagement() {
		this.pluginConfig = new PluginConfig<String, String>();
	}

	public TizzitPlugin getPlugin(String namespace) {
		TizzitPlugin plugin = null;
		String clazzName = this.pluginConfig.get(namespace);
		if (clazzName == null) {
			log.warn("Could not find Plugin for Namespace: " + namespace + "!");
			plugin = new NullPlugin();
		} else {
			try {
				plugin = (TizzitPlugin) ClassloadingHelper.getInstance(clazzName);
			} catch (Exception exe) {
				log.warn("Could not resolve TizzitPlugin with classname " + clazzName + " " + exe.getMessage());
				plugin = new NullPlugin();
			}
		}
		return plugin;
	}

	public class NullPlugin implements TizzitPlugin {
		ContentHandler parent = null;

		public void configurePlugin(Request request, Response response, ContentHandler parent, Integer uniquePageId) {
			this.parent = parent;
		}

		public Date getLastModifiedDate() {
			return new Date(0L);
		}

		public boolean isCacheable() {
			return false;
		}

		public void processContent() {
		}

		public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
			parent.characters(arg0, arg1, arg2);
		}

		public void endDocument() throws SAXException {
			parent.endDocument();
		}

		public void endElement(String arg0, String arg1, String arg2) throws SAXException {
			parent.endElement(arg0, arg1, arg2);
		}

		public void endPrefixMapping(String arg0) throws SAXException {
			parent.endPrefixMapping(arg0);
		}

		public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {
			parent.ignorableWhitespace(arg0, arg1, arg2);
		}

		public void processingInstruction(String arg0, String arg1) throws SAXException {
			parent.processingInstruction(arg0, arg1);
		}

		public void setDocumentLocator(Locator arg0) {
			parent.setDocumentLocator(arg0);
		}

		public void skippedEntity(String arg0) throws SAXException {
			parent.skippedEntity(arg0);
		}

		public void startDocument() throws SAXException {
			parent.startDocument();
		}

		public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {
			parent.startElement(arg0, arg1, arg2, arg3);
		}

		public void startPrefixMapping(String arg0, String arg1) throws SAXException {
			parent.startPrefixMapping(arg0, arg1);
		}
	}

	/**
	 *
	 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
	 * company Juwi MacMillan Group GmbH, Walsrode, Germany
	 * @version $Id$
	 * @since tizzit-core 09.10.2009
	 * @param <K>
	 * @param <V>
	 */
	public class PluginConfig<K, V> extends HashMap<String, String> {
		private static final long serialVersionUID = -2484247980813323117L;

		public PluginConfig() {
			super();
			this.parsePluginConfig();
		}

		private void parsePluginConfig() {
			if (log.isDebugEnabled()) log.debug("parsePluginConfig() -> begin");
			try {
				URL cp = this.getClass().getResource("/tizzit-plugins.conf");
				InputStream is = new FileInputStream(cp.getFile());
				InputStreamReader reader = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(reader);

				String line = br.readLine();
				int lineNumber = 1;
				while (line != null) {
					String formedLine = StringUtils.deleteWhitespace(line);
					if (!formedLine.startsWith("#")) {
						try {
							int sepIndex = formedLine.lastIndexOf("=");
							int commentIndex = formedLine.indexOf("#");
							if (sepIndex > -1) {
								String namespace = null;
								String clazzName = null;
								if (commentIndex > sepIndex) {
									// entries with comments  behind the equal char
									namespace = formedLine.substring(0, sepIndex);
									clazzName = formedLine.substring(sepIndex + 1, commentIndex);
								} else if (commentIndex == -1) {
									// entries without comments on line
									namespace = formedLine.substring(0, sepIndex);
									clazzName = formedLine.substring(sepIndex + 1);
								} else {
									log.warn(appendLineNumber("Ignoring entry: '" + line + "' because the comment char is before the equals sign!", lineNumber));
								}
								namespace = StringUtils.trimToNull(namespace);
								clazzName = StringUtils.trimToNull(clazzName);
								if (namespace != null && clazzName != null) {
									if (!this.containsKey(namespace)) {
										this.put(namespace, clazzName);
										log.info(appendLineNumber("Mapped plugin namespace '" + namespace + "' to class '" + clazzName + "'.", lineNumber));
									} else {
										log.warn(appendLineNumber("Ignoring plugin namespace '" + namespace + "' because it has already been added.", lineNumber));
									}
								} else {
									log.warn(appendLineNumber("Namespace and class MUST not be null! Ignoring...", lineNumber));
								}
							}
						} catch (Exception exe) {
							log.error(appendLineNumber("Could not parse line '" + formedLine + "': " + exe.getMessage(), lineNumber));
						}
					} else {
						if (log.isDebugEnabled()) log.debug(appendLineNumber("Ignoring entry: '" + line + "' because it is a comment!", lineNumber));
					}
					line = br.readLine();
					lineNumber++;
				}
			} catch (FileNotFoundException e) {
				log.error("Could not find tizzit-plugins.conf - no plugins will be available!");
			} catch (Exception e) {
				log.error("Could not find tizzit-plugins.conf - no plugins will be available!");
			}

			if (log.isDebugEnabled()) {
				StringBuffer resultMsg = new StringBuffer();
				resultMsg.append("\nResult {\n");
				resultMsg.append("\tSize: " + this.size() + "\n");
				if (this.size() > 1) {
					resultMsg.append("\tEntries:\n");
				} else {
					resultMsg.append("\tEntry:\n");
				}
				for (String key : this.keySet()) {
					resultMsg.append("\t\tNamespace: " + key + "\n");
					resultMsg.append("\t\tClass    : " + this.get(key) + "\n");
				}
				resultMsg.append("}");
				log.debug(resultMsg.toString());
			}

			if (log.isDebugEnabled()) log.debug("parsePluginConfig() -> end");
		}

		private String appendLineNumber(String message, int lineNumber) {
			return message + " [Line: " + lineNumber + "]";
		}
	}
}
