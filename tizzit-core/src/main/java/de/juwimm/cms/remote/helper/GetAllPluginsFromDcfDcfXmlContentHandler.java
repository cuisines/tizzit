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
package de.juwimm.cms.remote.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import de.juwimm.cms.common.http.HttpClientWrapper;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class GetAllPluginsFromDcfDcfXmlContentHandler extends DefaultHandler {
	private static Logger log = Logger.getLogger(GetAllPluginsFromDcfDcfXmlContentHandler.class);
	private List<String> alDcfs = new ArrayList<String>();
	private HttpClientWrapper httpClient = HttpClientWrapper.getInstance();
	private String dcfUrl = null;

	public GetAllPluginsFromDcfDcfXmlContentHandler(String dcfUrl) {
		this.dcfUrl = dcfUrl;
	}

	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		if (qName.equalsIgnoreCase("item")) {
			String filename = atts.getValue("filename");
			try {
				alDcfs.add(httpClient.getString(dcfUrl + filename + ".xml"));
			} catch (IOException exe) {
				log.warn("an unknown error occured fetching dcf " + dcfUrl + filename + ".xml");
			}
		}
	}

	public final List<String> getAlDcfs() {
		return this.alDcfs;
	}
}