package de.juwimm.cms.content.panel.util;

public interface DocumentPreviewComponent {
	
	/**
	 * Load into the component the byte[] contayning the content of the document.
	 * @param content
	 */
	public void setDocumentContent(byte[] content);
	
	/**
	 * This method shows the preview frame for the document loaded.
	 */
	public void displayComponent();
}
