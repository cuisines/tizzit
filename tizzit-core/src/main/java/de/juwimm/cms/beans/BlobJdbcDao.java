package de.juwimm.cms.beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class BlobJdbcDao extends JdbcDaoSupport {
	
	@SuppressWarnings("unchecked")
	public byte[] getDocumentContent(Integer documentId) {		
		String selectQuery = "select document as document from document where document_Id = ?";
		List<byte[]> lst = getJdbcTemplate().query(selectQuery, new Object[] {documentId},new BlobRowMapper("document"));		
		return lst.get(0);		
	}
	
	private class BlobRowMapper implements RowMapper {		
		private String blobColumn;
		
		public BlobRowMapper(String columneName) {
			blobColumn = columneName;
		}

		public byte[] mapRow(ResultSet rs, int rowNum) throws SQLException {
			byte[] blob = rs.getBytes(blobColumn);
			return blob;
		}
	}

}
