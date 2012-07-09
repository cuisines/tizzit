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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.rtf.RTFEditorKit;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.tizzit.util.XercesHelper;

import de.juwimm.cms.model.DocumentHbmDao;

/**
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class RTFDocumentLocator {
	private static Logger log = Logger.getLogger(RTFDocumentLocator.class);
	public static final String MIME_TYPE = "application/rtf";
	@Autowired
	DocumentHbmDao documentHbmDao;

	public Document getDocument(de.juwimm.cms.model.DocumentHbm document) throws IOException {
		Document doc = new Document();
		InputStream bis = new ByteArrayInputStream(documentHbmDao.getDocumentContent(document.getDocumentId()));

		DefaultStyledDocument styledDoc = new DefaultStyledDocument();
		String contents = null;
		try {
			new RTFEditorKit().read(bis, styledDoc, 0);
			contents = styledDoc.getText(0, styledDoc.getLength());
		} catch (BadLocationException e) {
			log.warn("Error parsing rtf-doc: " + e.getMessage(), e);
			throw new IOException("Error parsing rtf-doc: " + e.getMessage());
		}
		doc.add(new Field("contents", contents, Field.Store.YES, Field.Index.ANALYZED));

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
		return doc;
	}

	public Document getResource(de.juwimm.cms.model.DocumentHbm document) throws IOException {
		Document resource = new Document();
		InputStream bis = new ByteArrayInputStream(documentHbmDao.getDocumentContent(document.getDocumentId()));
		resource = getContent(resource, bis);
		resource.add(new Field("documentId", document.getDocumentId().toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		resource.add(new Field("uid", document.getDocumentId().toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		String docName = document.getDocumentName();
		if (docName == null) docName = "";
		resource.add(new Field("documentName", docName, Field.Store.YES, Field.Index.ANALYZED));
		resource.add(new Field("title", docName, Field.Store.YES, Field.Index.ANALYZED));
		resource.add(new Field("unitId", document.getUnit().getUnitId().toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		resource.add(new Field("unitName", document.getUnit().getName(), Field.Store.YES, Field.Index.ANALYZED));
		resource.add(new Field("mimeType", document.getMimeType(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		resource.add(new Field("timeStamp", document.getTimeStamp().toString(), Field.Store.YES, Field.Index.NO));
		resource.add(new Field("siteId", document.getUnit().getSite().getSiteId().toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		return resource;
	}

	private Document getContent(Document resource, InputStream in) throws IOException {
		DefaultStyledDocument styledDoc = new DefaultStyledDocument();
		String contents = null;
		try {
			new RTFEditorKit().read(in, styledDoc, 0);
			contents = styledDoc.getText(0, styledDoc.getLength());
			in.close();
		} catch (BadLocationException e) {
			log.warn("Error parsing rtf-doc: " + e.getMessage(), e);
			throw new IOException("Error parsing rtf-doc: " + e.getMessage());
		}
		resource.add(new Field("contents", contents, Field.Store.YES, Field.Index.ANALYZED));
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
		resource.add(new Field("summary", summary, Field.Store.YES, Field.Index.NO));
		return resource;
	}

	public Document getExternalResource(String url, InputStream in) throws IOException {
		Document resource = new Document();
		resource.add(new Field("url", url, Field.Store.YES, Field.Index.NOT_ANALYZED));
		resource.add(new Field("uid", url, Field.Store.YES, Field.Index.NOT_ANALYZED));
		resource = getContent(resource, in);
		return resource;
	}

}
