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
import java.util.Map;

import org.apache.avalon.excalibur.pool.Poolable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.ResourceNotFoundException;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.reading.AbstractReader;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.TimeStampValidity;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import de.juwimm.cms.beans.WebServiceSpring;
import de.juwimm.cms.cocoon.helper.CocoonSpringHelper;

/**
 * 
 * Some ways to get an image:<br/>
 * <br/>
 * Chance <b>one</b> to get an image is via request parameters:<br/>
 * id = id of image<br/>
 * typ = (s)tandard for images as it is stored<br/>
 *     = (t)humb image - scaled image<br/>
 *     = (p)review image<br/>
 *<br/>
 * &lt;map:match pattern="img/ejbimage**"&gt;<br/>
 * &nbsp;&nbsp;&lt;map:read type="imagereader" /&gt;<br/>
 * &lt;/map:match&gt;<br/>
 *<br/>
 * 
 * <b>Second</b> chance to get an image is via sitemap parameters:<br/>
 * Possible parameters are the same as above. An advantage<br/>
 * solving image reading that way is a better search engine<br/>
 * optimized way to get images on pages.<br/>
 *<br/>
 * &lt;img src="/img/123-t.jpg" alt="" title="" /&gt;<br/>
 *<br/>
 * &lt;map:match pattern="img/&#x2a;-&#x2a;.&#x2a;"&gt;<br/>
 * &nbsp;&nbsp;&lt;map:read type="imagereader"&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;map:parameter name="id" value="{1}" /&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;map:parameter name="typ" value="{2}" /&gt;<br/>
 * &nbsp;&nbsp;&lt;/map:read&gt;<br/>
 * &lt;/map:match&gt;<br/>
 *<br/>
 * and/or<br/>
 * <br/>
 * &lt;img src="/img/123.jpg" alt="" title="" /&gt;<br/> 
 *<br/>
 * &lt;map:match pattern="img/&#x2a;.&#x2a;"&gt;<br/>
 * &nbsp;&nbsp;&lt;map:read type="imagereader"&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;map:parameter name="id" value="{1}" /&gt;<br/>
 * &nbsp;&nbsp;&lt;/map:read&gt;<br/>
 * &lt;/map:match&gt;<br/>
 * <br/>
 * It is also possible to filter the possibly placed description
 * to get even better search engine optimization<br/>
 *<br/>
 * &lt;img src="/img/123-t/my long description text goes here.jpg" alt="" title="" /&gt;<br/>
 *<br/>
 * &lt;map:match pattern="img/&#x2a;-&#x2a;/&#x2a;&#x2a;"&gt;<br/>
 * &nbsp;&nbsp;&lt;map:read type="imagereader"&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;map:parameter name="id" value="{1}" /&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;map:parameter name="typ" value="{2}" /&gt;<br/>
 * &nbsp;&nbsp;&lt;/map:read&gt;<br/>
 * &lt;/map:match&gt;<br/>
 *<br/>
 * &lt;img src="/img/123/t/my long description text goes here.jpg" alt="" title="" /&gt;<br/>
 *<br/>
 * &lt;map:match pattern="img/&#x2a;/&#x2a;/&#x2a;&#x2a;"&gt;<br/>
 * &nbsp;&nbsp;&lt;map:read type="imagereader"&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;map:parameter name="id" value="{1}" /&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;map:parameter name="typ" value="{2}" /&gt;<br/>
 * &nbsp;&nbsp;&lt;/map:read&gt;<br/>
 * &lt;/map:match&gt;<br/>
 *<br/>
 * &lt;img src="/img/123/my long description text goes here.jpg" alt="" title="" /&gt;<br/>
 *<br/>
 * &lt;map:match pattern="img/&#x2a;/&#x2a;&#x2a;"&gt;<br/>
 * &nbsp;&nbsp;&lt;map:read type="imagereader"&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;map:parameter name="id" value="{1}" /&gt;<br/>
 * &nbsp;&nbsp;&lt;/map:read&gt;<br/>
 * &lt;/map:match&gt;<br/>
 * <br/>
 * @author <a href="mailto:kulawik@juwimm.com">Sascha Kulawik</a>
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * @author <a href="mailto:michael.frankfurter@juwimm.com">Michael Frankfurter</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class CmsImageReader extends AbstractReader implements Poolable, CacheableProcessingComponent {
	private static Logger log = Logger.getLogger(CmsImageReader.class);
	private long chgDate = 0;
	private byte[] ressource = null;
	private WebServiceSpring webSpringBean = null;
	private Integer primaryKey;
	private String strBez = "";

	public void supersetup(SourceResolver resolver, Map objectModel, String src, Parameters par) throws ProcessingException, SAXException, IOException {
		super.setup(resolver, objectModel, src, par);
	}

	@Override
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) throws ProcessingException, SAXException, IOException {
		supersetup(resolver, objectModel, src, par);
		try {
			try {
				webSpringBean = (WebServiceSpring) CocoonSpringHelper.getBean(objectModel, CocoonSpringHelper.WEB_SERVICE_SPRING);
			} catch (Exception exf) {
				log.error("Could not load webservicespringbean!", exf);
			}
			Request request = ObjectModelHelper.getRequest(objectModel);

			if (par.isParameter("id")) {
				primaryKey = new Integer(par.getParameterAsInteger("id"));
				strBez = (par.isParameter("typ")) ? par.getParameter("typ") : null;
			} else {
				primaryKey = new Integer(request.getParameter("id"));
				try {
					strBez = request.getParameter("typ");
				} catch (Exception e) {
					strBez = "s";
				}
			}
			if (strBez == null || strBez.equals("")) strBez = "s";
		} catch (Exception e) {
			throw new ResourceNotFoundException("error:", e);
		}
		if (log.isDebugEnabled()) log.debug("Fine setup");
	}

	public void generate() throws java.io.IOException, org.xml.sax.SAXException, org.apache.cocoon.ProcessingException {
		try {
			//Response response = ObjectModelHelper.getResponse(objectModel);

			if (strBez.equals("t")) {
				this.ressource = getWebServiceSpring().getThumbnail(this.primaryKey);
			} else if (strBez.equals("p")) {
				this.ressource = getWebServiceSpring().getPreview(this.primaryKey);
			} else {
				this.ressource = getWebServiceSpring().getPicture(this.primaryKey);
			}

			if (this.ressource == null) { throw new ResourceNotFoundException("The Blob is empty!"); }
			//response.setHeader("Content-disposition", "inline;filename=" + this.filename);
			out.write(ressource);
			out.flush();
		} catch (IOException ioe) {
			if (log.isDebugEnabled()) log.debug("Assuming client reset stream");
		} catch (Exception e) {
			throw new ResourceNotFoundException("DatabaseReader error:", e);
		}
		if (log.isDebugEnabled()) log.debug("Fine generating");
	}

	public Serializable getKey() {
		if (strBez.equals("t")) { return "t" + this.primaryKey; }
		if (strBez.equals("p")) { return "p" + this.primaryKey; }
		return "s" + this.primaryKey;
	}

	public SourceValidity getValidity() {
		chgDate = 0;
		try {
			chgDate = getWebServiceSpring().getTimestamp4Picture(this.primaryKey).longValue();
		} catch (Exception exe) {
		}
		if (chgDate != 0) { return new TimeStampValidity(chgDate); }
		return null;
	}

	protected WebServiceSpring getWebServiceSpring() {
		return webSpringBean;
	}

	@Override
	public String getMimeType() {
		String mimetype = "image/jpeg";
		try {
			mimetype = getWebServiceSpring().getMimetype4Picture(this.primaryKey);
		} catch (Exception e) {
		}
		return mimetype;
	}

	/**
	 * @return Returns the ressource.
	 */
	protected byte[] getRessource() {
		return this.ressource;
	}

	/**
	 * @param ressource The ressource to set.
	 */
	protected void setRessource(byte[] ressource) {
		this.ressource = ressource;
	}

	/**
	 * @return Returns the chgDate.
	 */
	protected long getChgDate() {
		return this.chgDate;
	}

	/**
	 * @param chgDate The chgDate to set.
	 */
	protected void setChgDate(long chgDate) {
		this.chgDate = chgDate;
	}

	/**
	 * @return Returns the primaryKey.
	 */
	protected Integer getPrimaryKey() {
		return this.primaryKey;
	}

	/**
	 * @param primaryKey The primaryKey to set.
	 */
	protected void setPrimaryKey(Integer primaryKey) {
		this.primaryKey = primaryKey;
	}

}
