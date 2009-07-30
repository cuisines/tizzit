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
package de.juwimm.cms.cocoon.reading;

import java.io.IOException;
import java.io.Serializable;

import org.apache.avalon.excalibur.pool.Poolable;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.ResourceNotFoundException;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Response;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.TimeStampValidity;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * Some ways to get a file:<br/>
 * <br/>
 * Chance <b>one</b> to get a file is via request parameters:<br/>
 * id = id of file<br/>
 *<br/>
 * &lt;map:match pattern="img/ejbfile**"&gt;<br/>
 * &nbsp;&nbsp;&lt;map:read type="filereader" /&gt;<br/>
 * &lt;/map:match&gt;<br/>
 *<br/>
 * 
 * <b>Second</b> chance to get a file is via sitemap parameters:<br/>
 * Possible parameters are the same as above. An advantage<br/>
 * solving file reading that way is a better search engine<br/>
 * optimized way to get files on pages.<br/>
 *<br/>
 * &lt;a href="/file/123/my long description text goes here.pdf"&gt;Filename&lt;/a&gt;<br/>
 *<br/>
 * &lt;map:match pattern="file/&#x2a;/&#x2a;&#x2a;"&gt;<br/>
 * &nbsp;&nbsp;&lt;map:read type="imagereader"&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;map:parameter name="id" value="{1}" /&gt;<br/>
 * &nbsp;&nbsp;&lt;/map:read&gt;<br/>
 * &lt;/map:match&gt;<br/>
 *<br/>
 * @author <a href="mailto:kulawik@juwimm.com">Sascha Kulawik</a>
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * @author <a href="mailto:michael.frankfurter@juwimm.com">Michael Frankfurter</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class CmsFileReader extends CmsImageReader implements Poolable, CacheableProcessingComponent {
	private static Logger log = Logger.getLogger(CmsFileReader.class);

	@Override
	public void generate() throws IOException, SAXException, ProcessingException {
		try {
			if (log.isDebugEnabled()) log.debug("GENERATE");
			Response response = ObjectModelHelper.getResponse(objectModel);
			setRessource(getWebServiceSpring().getDocument(getPrimaryKey()));

			if (getRessource() == null) {
				if (log.isDebugEnabled()) log.debug("GENERATE NULL");
				throw new ResourceNotFoundException("The Blob is empty!");
			}
			/*
			 response.setHeader("Accept-Ranges", "bytes");
			 response.setHeader("Cache-Control", "no-cache");
			 response.setHeader("Pragma", "no-cache");
			 response.setDateHeader("Expires", 0);
			 */
			response.setHeader("Content-disposition", "inline;filename=" + getWebServiceSpring().getDocumentName(getPrimaryKey()));
			if (log.isDebugEnabled()) log.debug("Returning serialized object with size " + getRessource().length);
			out.write(getRessource());
			out.flush();
			if (log.isDebugEnabled()) log.debug("Fine generating");
		} catch (IOException ioe) {
			if (log.isDebugEnabled()) log.debug("Assuming client reset stream");
		} catch (Exception e) {
			throw new ResourceNotFoundException("DatabaseReader error:", e);
		}
	}

	@Override
	public String getMimeType() {
		String mimetype = "application/octet-stream";
		try {
			mimetype = getWebServiceSpring().getMimetype4Document(getPrimaryKey());
		} catch (Exception e) {
		}
		return mimetype;
	}

	@Override
	public Serializable getKey() {
		return getPrimaryKey();
	}

	@Override
	public SourceValidity getValidity() {
		setChgDate(0);
		try {
			setChgDate(getWebServiceSpring().getTimestamp4Document(getPrimaryKey()).longValue());
		} catch (Exception exe) {
		}
		if (getChgDate() != 0) { return new TimeStampValidity(getChgDate()); }
		return null;
	}

}
