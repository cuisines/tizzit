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
package de.juwimm.cms.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.tizzit.util.Base64;

import de.juwimm.cms.beans.BlobJdbcDao;
import de.juwimm.cms.vo.DocumentSlimValue;

/**
 * @see de.juwimm.cms.model.DocumentHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * , Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class DocumentHbmImpl extends DocumentHbm {
	private static final long serialVersionUID = 4804431489081379254L;
	private static Logger log = Logger.getLogger(DocumentHbmImpl.class);

	/**
	 * @see de.juwimm.cms.model.DocumentHbm#getSlimValue()
	 */
	@Override
	public DocumentSlimValue getSlimValue() {
		DocumentSlimValue vo = new DocumentSlimValue();
		vo.setDocumentId(this.getDocumentId());
		vo.setDocumentName(this.getDocumentName());
		vo.setMimeType(this.getMimeType());
		vo.setTimeStamp(this.getTimeStamp());
		vo.setUseCountLastVersion(this.getUseCountLastVersion());
		vo.setUseCountPublishVersion(this.getUseCountPublishVersion());
		return vo;
	}

	@Override
	public String toXml(int tabdepth, byte[] data) {
		StringBuffer sb = new StringBuffer();
		sb.append("<document id=\"");
		sb.append(this.getDocumentId());
		sb.append("\" mimeType=\"");
		sb.append(this.getMimeType());
		if (this.getUnit() == null) {
			sb.append("\" viewComponentId=\"");
			sb.append(this.getViewComponent().getViewComponentId());
		} else {
			sb.append("\" unitId=\"");
			sb.append(this.getUnit().getUnitId());
		}
		sb.append("\">\n");
//		ByteArrayInputStream inputStream = null;
//		try {
//			inputStream = (ByteArrayInputStream) this.getDocument().getBinaryStream();
//		} catch (SQLException e1) {
//			log.error(e1);
//		}
//		byte[] data = null;
//		try {
//			data = IOUtils.toByteArray(inputStream);
//		} catch (Exception e) {
//			log.error(e);
//		}
		if (data == null || data.length == 0) {
			sb.append("\t<file></file>\n");
		} else {
			sb.append("\t<file>").append(Base64.encodeBytes(data)).append("</file>\n");
		}

		sb.append("\t<name><![CDATA[" + this.getDocumentName() + "]]></name>\n");
		sb.append("</document>\n");
		return sb.toString();
	}

}
