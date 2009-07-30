package de.juwimm.cms.search.res;

import org.compass.core.CompassSession;
import org.compass.core.Resource;
import org.springframework.beans.factory.annotation.Autowired;

public class DocumentResourceLocatorFactory {
	@Autowired
	private PDFResourceLocator pdfResourceLocator;
	@Autowired
	private RTFResourceLocator rtfResourceLocator;
	@Autowired
	private WordResourceLocator wordResourceLocator;
	 

	public Resource getResource(CompassSession session, de.juwimm.cms.model.DocumentHbm document) throws Exception {
		Resource resource = null;
		if (PDFResourceLocator.MIME_TYPE.equalsIgnoreCase(document.getMimeType())) {
			resource = pdfResourceLocator.getResource(session, document);
		} else if (WordResourceLocator.MIME_TYPE.equalsIgnoreCase(document.getMimeType())) {
			resource = wordResourceLocator.getResource(session, document);
		} else if (RTFResourceLocator.MIME_TYPE.equalsIgnoreCase(document.getMimeType())) {
			resource = rtfResourceLocator.getResource(session, document);
		}
		return resource;
	}
	
	public boolean isSupportedFileFormat(String mimeType) {
		return PDFResourceLocator.MIME_TYPE.equalsIgnoreCase(mimeType) || WordResourceLocator.MIME_TYPE.equalsIgnoreCase(mimeType) || RTFResourceLocator.MIME_TYPE.equalsIgnoreCase(mimeType);
	}
 
}
