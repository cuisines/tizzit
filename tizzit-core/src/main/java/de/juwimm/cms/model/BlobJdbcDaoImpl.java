package de.juwimm.cms.model;

import java.io.IOException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.hibernate.lob.SerializableBlob;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import de.juwimm.cms.beans.foreign.support.MiniViewComponent;

/**
 * @author fzalum
 *
 */
public class BlobJdbcDaoImpl extends JdbcDaoSupport implements BlobJdbcDao {
	
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
