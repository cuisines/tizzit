package de.juwimm.cms.model;

/**
 * @author fzalum
 *
 */
public interface BlobJdbcDao{
	public byte[] getDocumentContent(Integer documentId);
}
