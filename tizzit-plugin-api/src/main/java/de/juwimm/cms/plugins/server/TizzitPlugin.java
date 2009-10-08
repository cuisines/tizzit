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
package de.juwimm.cms.plugins.server;

import java.util.Date;

import org.xml.sax.ContentHandler;

/**
 * @author <a href="mailto:michael.meyer@juwimm.com">Michael Meyer</a><br/>
 *
 */
public interface TizzitPlugin extends ContentHandler {
	
	/**
	 * This method will be called if the plugin has got all its configurational
	 * stuff through the sax events.
	 * It depends on the plugin, if it is willing to react on this method call - 
	 * convinient method would be to fire up right now the sax events which
	 * are containing the xhtml output.
	 */
	public void processContent();
	
	/**
	 * Diese Methode soll verwendet werden, um das Plugin zu konfigurieren.
	 * 
	 * @param request Contains the HTTPRequest
	 * @param parent The parent-ContentHandler to submit the SAX-streamed output of the plugin
	 * @param uniquePageId The uniquePageId (internal: viewComponentId) of the webpage where this
	 * plugin has been called. Can be used to find out from which page this plugin has been called or
	 * to save into a database or to read out by this id from the database.
	 */
	public void configurePlugin(Request request, Response response, ContentHandler parent, Integer uniquePageId);
	
	/**
	 * Is necessary to find out if the page containing this plugin can be cached
	 * @return
	 */
	public boolean isCacheable();
	
	/**
	 * Will only be called if the actual plugin is cacheable. In this case the module should
	 * return a guilty modified date, not a null value.
	 * @return
	 */
	public Date getLastModifiedDate();

}
