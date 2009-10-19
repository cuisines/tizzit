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
package de.juwimm.cms.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.vo.ContentVersionValue;

/**
 * <p>Title: Tizzit</p>
 * <p>Description: Enterprise Content Management</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Revision: 1.32 $
 */
public final class DbHelper {
	private static Logger log = Logger.getLogger(DbHelper.class);
	public static final String DRIVER = "org.hsqldb.jdbcDriver";
	public static final String PROTOCOL = "jdbc:hsqldb:";
	private final String DB_NAME = "cacheDB24_";
	private Connection conn = null;
	private static Statement statement = null;
	private PreparedStatement ps = null;

	/* Constructor */
	public DbHelper() {
		log.info("Local caching database in = " + Constants.DB_PATH);
		try {
			Class.forName(DRIVER).newInstance();
			/* Now "DbHelper" will create for every different Server a different cache DB.
			 In german called "Mandantenfähig". Therefor we will add here only the customername after the
			 serverURL. */
			if (conn == null || statement == null) {
				this.startDB();
			}
		} catch (Exception exe) {
			if (exe.getMessage().startsWith("The database is already in use by another process")) {
				JOptionPane.showMessageDialog(UIConstants.getMainFrame(), Constants.rb.getString("exception.programAlreadyStarted"), Constants.rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			} else {
				log.error("Local caching panic! Have to brute force delete all caching data", exe);
				autoEmptyCache();
			}
		}
	}

	public boolean clearMyCache() {
		boolean ret = false;
		try {
			this.shutdownDB();
			ret = true;
		} catch (Exception exe) {
			log.error("Error on deleting cacheDB: " + exe.getMessage());
			ret = false;
		} finally {
			try {
				this.startDB();
			} catch (Exception exee) {
				log.error("Error on connecting to cacheDB: " + exee.getMessage());
			}
		}
		return ret;
	}

	/**
	 * This method is called, when serious exceptions occur on inserting data into database or creating tables.<br/>
	 * It deletes the cacheDB and recreates all necessary tables and reestablishes the connection.
	 */
	public void autoEmptyCache() {
		if (this.clearMyCache()) {
			log.warn("CacheDB was succesfully cleaned, see log for more details");
		} else {
			log.error("Error on cleaning cacheDB, see log for more details");
		}
	}

	/* If tables are not available, create them */
	public void createTables() {
		try {
			/* Check to see whether Cachepicture table is available, to know whether is initialized */
			statement.execute("select picture_id from CACHEPICTURE");
		} catch (Exception ex) {
			try {
				statement.execute("CREATE TABLE CACHEPICTURE (picture_id INT, thumbnail VARBINARY, picture VARBINARY, " + "mime_type VARCHAR (30), time_stamp DATE, unit_id_fk INT)");
				statement.execute("create table cachedocument (document_id int, document_name varchar, document varbinary, time_stamp date)");
			} catch (Exception exe) {
				log.error("Error create table CACHEPICTURE", exe);
				this.autoEmptyCache();
			}
		}
		try {
			statement.execute("SELECT dcfname FROM sessioncache_dcf");
			/* sessioncache tables are rebuilded every time the client will be started */
			statement.execute("DELETE FROM sessioncache_dcf");
		} catch (Exception ex) {
			try {
				statement.execute("CREATE TABLE sessioncache_dcf (dcfname VARCHAR_IGNORECASE(50), dcf LONGVARCHAR)");
			} catch (Exception exe) {
				log.error("Error create table sessioncache_dcf", exe);
				this.autoEmptyCache();
			}
		}
		try {
			statement.execute("SELECT content_id FROM cache_templatename");
		} catch (Exception ex) {
			try {
				statement.execute("CREATE TABLE cache_templatename (content_id INT, templatename VARCHAR_IGNORECASE(50))");
			} catch (Exception exe) {
				log.error("Error create table cache_templatename", exe);
				this.autoEmptyCache();
			}
		}
		try {
			statement.execute("SELECT contentVersion_id FROM cache_contentVersion");
		} catch (Exception ex) {
			try {
				statement.execute("CREATE TABLE cache_contentVersion (contentVersion_id INT, content_id INT, contentVersionDao OBJECT)");
			} catch (Exception exe) {
				log.error("Error create table cache_contentVersion", exe);
				this.autoEmptyCache();
			}
		}

	}

	public void close() {
		try {
			ps.close();
		} catch (Exception e) {
			log.error("Error on closing connection to cacheDB: " + e.getMessage());
		}
		try {
			statement.close();
		} catch (Exception e) {
			log.error("Error on closing connection to cacheDB: " + e.getMessage());
		}
		try {
			conn.close();
		} catch (Exception e) {
			log.error("Error on closing connection to cacheDB: " + e.getMessage());
		}
	}

	public boolean checkContentVersionsForContentAviable(int contentId) {
		boolean ret = false;
		try {
			ps = conn.prepareStatement("SELECT count(*) FROM cache_contentVersion where content_id = ?");
			ps.setInt(1, contentId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				if (rs.getInt(1) > 0) ret = true;
			}
			ps.close();
		} catch (Exception exe) {
			log.error("Error checkContentVersionsForContentAvailable", exe);
			try {
				ps.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		return ret;
	}

	public ContentVersionValue getContentVersion(int contentVersionId) {
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT contentVersionDao FROM cache_contentVersion WHERE contentVersion_id = ?");
			ps.setInt(1, contentVersionId);
			rs = ps.executeQuery();
			return (rs.next()) ? (ContentVersionValue) rs.getObject(1) : null;
		} catch (Exception ex) {
			log.error(ex.getMessage());
			try {
				rs.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			try {
				ps.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		return null;
	}

	public void deleteContentVersions(int contentId) {
		try {
			ps = conn.prepareStatement("DELETE FROM cache_contentVersion WHERE content_id = ?");
			ps.setInt(1, contentId);
			ps.executeUpdate();
		} catch (Exception exe) {
			log.error("Error deleteContentVersions " + contentId, exe);
			try {
				ps.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
	}

	public void setContentVersion(int contentVersionId, int contentId, ContentVersionValue contentVersionDao) {
		try {
			ps = conn.prepareStatement("INSERT INTO cache_contentVersion (contentVersion_id, content_id, contentVersionDao) values (?,?,?)");
			ps.setInt(1, contentVersionId);
			ps.setInt(2, contentId);
			ps.setObject(3, contentVersionDao);
			if (ps.executeUpdate() != 1) { throw new Exception("Failed to add ContentVersion to database"); }
		} catch (Exception exe) {
			log.error("Error setContentVersion " + contentVersionId, exe);
			try {
				ps.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			this.autoEmptyCache();
		}
	}

	public String getDCF(String dcfname) {
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT dcf FROM sessioncache_dcf WHERE dcfname = ?");
			ps.setString(1, dcfname);
			rs = ps.executeQuery();
			return (rs.next()) ? rs.getString(1) : null;
		} catch (Exception ex) {
			log.error(ex.getMessage());
			try {
				rs.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			try {
				ps.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		return null;
	}

	public void setDCF(String dcfname, String dcftext) {
		try {
			ps = conn.prepareStatement("INSERT INTO sessioncache_dcf (dcfname, dcf) values (?,?)");
			ps.setString(1, dcfname);
			ps.setString(2, dcftext);
			if (ps.executeUpdate() != 1) { throw new Exception("Failed to add DCF to database"); }
		} catch (Exception exe) {
			log.error("Error setDCF " + dcfname, exe);
			try {
				ps.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			this.autoEmptyCache();
		}
	}

	public String getTemplateName(int viewComponentId) {
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT templatename FROM cache_templatename WHERE content_id = ?");
			ps.setInt(1, viewComponentId);
			rs = ps.executeQuery();
			return (rs.next()) ? rs.getString(1) : null;
		} catch (Exception ex) {
			log.error(ex.getMessage());
			try {
				rs.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			try {
				ps.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		return null;
	}

	public void setTemplateName(int viewComponentId, String templateName) {
		try {
			ps = conn.prepareStatement("DELETE FROM cache_templatename WHERE content_id = ?");
			ps.setInt(1, viewComponentId);
			ps.execute();
			ps = conn.prepareStatement("INSERT INTO cache_templatename (content_id, templatename) values (?,?)");
			ps.setInt(1, viewComponentId);
			ps.setString(2, templateName);
			if (ps.executeUpdate() != 1) { throw new Exception("Failed to add TemplateName to database"); }
		} catch (Exception exe) {
			log.error("Error setTemplateName " + viewComponentId + " " + templateName, exe);
			try {
				ps.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			this.autoEmptyCache();
		}
	}

	/* Return true when need to download picture from server side */
	public boolean checkPicture(int pictureId) {
		try {
			ps = conn.prepareStatement("select count(picture_id) from cachepicture where picture_id = ?");
			ps.setInt(1, pictureId);
			ResultSet rs = ps.executeQuery();
			return (rs.getInt(1) == 0) ? true : false;
		} catch (Exception exe) {
			log.error("Error checkPicture " + pictureId, exe);
			try {
				ps.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		return true;
	}

	/* Get picture from local caching database */
	public byte[] getPicture(int pictureId) {
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("select picture_id, thumbnail from cachepicture where picture_id = ?");
			ps.setInt(1, pictureId);
			rs = ps.executeQuery();
			return (rs.next()) ? rs.getBytes(2) : null;
		} catch (Exception ex) {
			log.error(ex.getMessage());
			try {
				rs.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			try {
				ps.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		return null;
	}

	public void setPicture(int pictureId, byte[] byteArray) {
		try {
			ps = conn.prepareStatement("insert into cachepicture (picture_id, thumbnail) values (?,?)");
			ps.setInt(1, pictureId);
			ps.setBytes(2, byteArray);
			if (ps.executeUpdate() != 1) { throw new Exception("Failed to add Book to database"); }
		} catch (Exception exe) {
			log.error("Error setPicture " + pictureId, exe);
			try {
				ps.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			this.autoEmptyCache();
		}
	}

	/* Return true when need to download picture from server side */
	public boolean checkThumbnail(int pictureId) {
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("select count(*) from cachepicture where picture_id = ?");
			ps.setInt(1, pictureId);
			rs = ps.executeQuery();
			if (rs.next()) { return (rs.getInt(1) == 1) ? false : true; }
			throw new Exception("");
		} catch (Exception exe) {
			log.error("Error checkThumbnail " + pictureId, exe);
			try {
				rs.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			try {
				ps.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			return true;
		}
	}

	/* Return true when need to download document name from server side */
	public boolean checkDocumentName(int documentId) {
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("select count(*) from cachedocument where document_id = ?");
			ps.setInt(1, documentId);
			rs = ps.executeQuery();
			if (rs.next()) { return (rs.getInt(1) == 1) ? false : true; }
			throw new Exception("");
		} catch (Exception exe) {
			log.error("Error checkDocumentName " + documentId, exe);
			try {
				rs.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			try {
				ps.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			return true;
		}
	}

	/* Get document name from local caching database */
	public String getDocumentName(int documentId) {
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("select document_id, document_name from cachedocument where document_id = ?");
			ps.setInt(1, documentId);
			rs = ps.executeQuery();
			return (rs.next()) ? rs.getString(2) : null;
		} catch (Exception ex) {
			log.error(ex.getMessage());
			try {
				rs.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			try {
				ps.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			return null;
		}

	}

	public void setDocumentName(int documentId, String docName) {
		try {
			ps = conn.prepareStatement("insert into cachedocument (document_id, document_name) values (?,?)");
			ps.setInt(1, documentId);
			ps.setString(2, docName);
			if (ps.executeUpdate() != 1) { throw new Exception("Failed to add document to cache database"); }
		} catch (Exception exe) {
			log.error("Error setDocumentName " + documentId, exe);
			try {
				ps.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			this.autoEmptyCache();
		}
	}

	public void shutdown() {
		log.info("Deleting local caching database in = " + Constants.DB_PATH);
		try {
			this.shutdownDB();
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
	}

	private void shutdownDB() throws Exception {
		statement.execute("DELETE FROM sessioncache_dcf");
		statement.execute("SHUTDOWN COMPACT");
		statement.close();
		ps.close();
		conn.close();
		File fle = new File(Constants.DB_PATH + DB_NAME + Constants.SERVER_HOST + ".data");
		fle.delete();
		fle = new File(Constants.DB_PATH + DB_NAME + Constants.SERVER_HOST + ".properties");
		fle.delete();
		fle = new File(Constants.DB_PATH + DB_NAME + Constants.SERVER_HOST + ".script");
		fle.delete();
		fle = new File(Constants.DB_PATH + DB_NAME + Constants.SERVER_HOST + ".log");
		fle.delete();
	}

	private void startDB() throws Exception {
		conn = DriverManager.getConnection(PROTOCOL + Constants.DB_PATH + DB_NAME + Constants.SERVER_HOST, "sa", "");
		conn.setAutoCommit(true);
		statement = conn.createStatement();
		createTables();
	}

}
