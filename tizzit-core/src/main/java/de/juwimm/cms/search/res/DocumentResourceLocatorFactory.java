package de.juwimm.cms.search.res;

import org.apache.lucene.document.Document;
import org.springframework.beans.factory.annotation.Autowired;

public class DocumentResourceLocatorFactory {
	@Autowired
	private PDFDocumentLocator pdfResourceLocator;
	@Autowired
	private RTFDocumentLocator rtfResourceLocator;
	@Autowired
	private WordDocumentLocator wordResourceLocator;
	 

	public Document getResource(de.juwimm.cms.model.DocumentHbm document) throws Exception {
		Document resource = null;
		if (PDFDocumentLocator.MIME_TYPE.equalsIgnoreCase(document.getMimeType())) {
			resource = pdfResourceLocator.getResource(document);
		} else if (WordDocumentLocator.MIME_TYPE.equalsIgnoreCase(document.getMimeType())) {
			resource = wordResourceLocator.getResource(document);
		} else if (RTFDocumentLocator.MIME_TYPE.equalsIgnoreCase(document.getMimeType())) {
			resource = rtfResourceLocator.getResource(document);
		}
		return resource;
	}
	
	public boolean isSupportedFileFormat(String mimeType) {
		return PDFDocumentLocator.MIME_TYPE.equalsIgnoreCase(mimeType) || WordDocumentLocator.MIME_TYPE.equalsIgnoreCase(mimeType) || RTFDocumentLocator.MIME_TYPE.equalsIgnoreCase(mimeType);
	}
 
}
