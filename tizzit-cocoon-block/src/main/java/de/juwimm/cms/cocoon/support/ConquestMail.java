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
/**
 * 
 */
package de.juwimm.cms.cocoon.support;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.tizzit.util.mail.Mail;

import de.juwimm.cms.beans.WebServiceSpring;
import de.juwimm.cms.vo.PictureValue;

/**
 * @author <a href="mailto:christiane.hausleiter@juwimm.com">Christiane Hausleiter</a>
 *
 */
public class ConquestMail extends Mail {
	
	private static Logger log = Logger.getLogger(ConquestMail.class);
	private WebServiceSpring webSpringBean = null;
	
	/**
	 * The constructor initializes the instance.
	 */
	public ConquestMail() {
		super(System.getProperty("conquest.mailDS", "java:/conquestMail"));
	}
	
	public ConquestMail(Properties testProperties) {
		super(testProperties);
	}
	
	public void setWebSpringBean(WebServiceSpring webSpringBean) {
		this.webSpringBean = webSpringBean;
	}

	/**
	 * Creates the specified document as an attachment. 
	 * 
	 * @param documentId the document's ID
	 */
	public void addAttachmentFromDocument(Integer documentId) {
		try {
			byte[] documentContent = this.webSpringBean.getDocument(documentId);
			String documentName = this.webSpringBean.getDocumentName(documentId);
			String tempFileName = documentName;
			String suffix = null;
			int dotIndex = tempFileName.lastIndexOf('.');
			if (dotIndex >= 0) {
				suffix = tempFileName.substring(dotIndex);
				tempFileName = tempFileName.substring(0, dotIndex);
			}
			// File.createTempFile() throws IllegalArgumentException if tempFileName does not contain at least 3 characters
			while (tempFileName.length() < 3) {
				tempFileName = "0" + tempFileName;
			}
			File tempFile = File.createTempFile(tempFileName, suffix);
			// temp file will be deleted when VM exits... won't be enough on a server and does not work on win32 anyway!
			tempFile.deleteOnExit();
			addNameToFileNameMappings(tempFile.getAbsolutePath(), documentName);
			FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
			ByteArrayInputStream inputStream = new ByteArrayInputStream(documentContent);
			byte[] buffer = new byte[512];
			for (int length = 0; (length = inputStream.read(buffer)) != -1; ) {
				fileOutputStream.write(buffer, 0, length);
			}
			addAttachmentFromFile(tempFile.getAbsolutePath());
		} catch (Exception exception) {
			log.error("Error reading document " + documentId, exception);
		}  
	}
	
	/**
	 * Creates the specified picture as an attachment
	 * 
	 * @param pictureId
	 */
	public void addAttachmentFromPicture(Integer pictureId) {
		try {
			PictureValue pictureValue = this.webSpringBean.getPictureValue(pictureId);
			byte[] pictureContent = pictureValue.getPicture();
			String pictureName = pictureValue.getPictureName();
			String suffix = null;
			
			if (pictureName == null || pictureName.trim().length() == 0) {
				suffix = pictureValue.getMimeType();
				suffix = "." + suffix.substring(suffix.lastIndexOf("/") + 1);
				pictureName = "cq_pic_" + pictureId + suffix;
			}
			
			String tempFileName = pictureName;
			int dotIndex = tempFileName.lastIndexOf('.');
			if (dotIndex >= 0) {
				suffix = tempFileName.substring(dotIndex);
				tempFileName = tempFileName.substring(0, dotIndex);
			}
			// File.createTempFile() throws IllegalArgumentException if tempFileName does not contain at least 3 characters
			while (tempFileName.length() < 3) {
				tempFileName = "0" + tempFileName;
			}
			// resulting tempFileName will be generated from specified name + id
			File tempFile = File.createTempFile(tempFileName, suffix);
			// temp file will be deleted when VM exits... won't be enough on a server and does not work on win32 anyway!
			tempFile.deleteOnExit();
			addNameToFileNameMappings(tempFile.getAbsolutePath(), pictureName);
			FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
			ByteArrayInputStream inputStream = new ByteArrayInputStream(pictureContent);
			byte[] buffer = new byte[512];
			for (int length = 0; (length = inputStream.read(buffer)) != -1; ) {
				fileOutputStream.write(buffer, 0, length);
			}
			addAttachmentFromFile(tempFile.getAbsolutePath());
		} catch (Exception exception) {
			log.error("Error reading document " + pictureId, exception);
		}  
	}

	@Override
	public void finalize() {
		super.clearTempFiles();
	}

}
