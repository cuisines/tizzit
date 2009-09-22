package de.juwimm.cms.model;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public interface BlobJdbcDao{
	public byte[] getDocumentContent(Integer documentId);
}
