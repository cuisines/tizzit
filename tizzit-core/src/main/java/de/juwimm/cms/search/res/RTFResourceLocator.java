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
package de.juwimm.cms.search.res;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.rtf.RTFEditorKit;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.compass.core.CompassSession;
import org.compass.core.Resource;
import org.compass.core.ResourceFactory;

import de.juwimm.cms.search.beans.SearchengineService;
import de.juwimm.util.XercesHelper;

/**
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class RTFResourceLocator {
	private static Logger log = Logger.getLogger(RTFResourceLocator.class);
	public static final String MIME_TYPE = "application/rtf";

	public Document getDocument(de.juwimm.cms.model.DocumentHbm document) throws IOException {
		Document doc = new Document();
		try {
			InputStream bis = document.getDocument().getBinaryStream();

			DefaultStyledDocument styledDoc = new DefaultStyledDocument();
			String contents = null;
			try {
				new RTFEditorKit().read(bis, styledDoc, 0);
				contents = styledDoc.getText(0, styledDoc.getLength());
			} catch (BadLocationException e) {
				log.warn("Error parsing rtf-doc: " + e.getMessage(), e);
				throw new IOException("Error parsing rtf-doc: " + e.getMessage());
			}
			doc.add(new Field("contents", contents, Field.Store.COMPRESS, Field.Index.ANALYZED));

			doc.add(new Field("documentId", document.getDocumentId().toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field("uid", document.getDocumentId().toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
			String docName = document.getDocumentName();
			if (docName == null) docName = "";
			doc.add(new Field("documentName", docName, Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field("title", docName, Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field("unitId", document.getUnit().getUnitId().toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field("unitName", document.getUnit().getName(), Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field("mimeType", document.getMimeType(), Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field("timeStamp", document.getTimeStamp().toString(), Field.Store.YES, Field.Index.NO));
			int summarySize = Math.min(contents.length(), 500);
			String summary = contents.substring(0, summarySize);
			if (summary != null && summary.length() > 0) {
				try {
					summary = XercesHelper.html2nodeUTF8(summary);
				} catch (Exception e) {
					// ignore
				}
			}
			if (summary == null) summary = "";
			doc.add(new Field("summary", summary, Field.Store.YES, Field.Index.NO));
		} catch (SQLException e1) {
			log.error("Error parsing WordResourceLocator", e1);
		}
		return doc;
	}

	public Resource getResource(CompassSession session, de.juwimm.cms.model.DocumentHbm document) throws IOException {
		ResourceFactory resourceFactory = session.resourceFactory();
		Resource resource = resourceFactory.createResource("DocumentSearchValue");
		try {
			InputStream bis = document.getDocument().getBinaryStream();

			DefaultStyledDocument styledDoc = new DefaultStyledDocument();
			String contents = null;
			try {
				new RTFEditorKit().read(bis, styledDoc, 0);
				contents = styledDoc.getText(0, styledDoc.getLength());
			} catch (BadLocationException e) {
				log.warn("Error parsing rtf-doc: " + e.getMessage(), e);
				throw new IOException("Error parsing rtf-doc: " + e.getMessage());
			}
			resource.addProperty("contents", contents);
			int summarySize = Math.min(contents.length(), 500);
			String summary = contents.substring(0, summarySize);
			if (summary != null && summary.length() > 0) {
				try {
					summary = XercesHelper.html2nodeUTF8(summary);
				} catch (Exception e) {
					// ignore
				}
			}
			if (summary == null) summary = "";
			resource.addProperty("summary", summary);
			resource.addProperty("documentId", document.getDocumentId().toString());
			resource.addProperty("uid", document.getDocumentId().toString());
			resource.addProperty("siteId", document.getUnit().getSite().getSiteId().toString());
			String docName = document.getDocumentName();
			if (docName == null) docName = "";
			resource.addProperty("documentName", docName);
			resource.addProperty("title", docName);
			resource.addProperty("unitId", document.getUnit().getUnitId().toString());
			resource.addProperty("unitName", document.getUnit().getName());
			resource.addProperty("mimeType", document.getMimeType());
			resource.addProperty("timeStamp", document.getTimeStamp().toString());
		} catch (SQLException e1) {
			log.error("Error parsing WordResourceLocator", e1);
		}
		return resource;
	}

}
