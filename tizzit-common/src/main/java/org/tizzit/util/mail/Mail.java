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
package org.tizzit.util.mail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:christiane.hausleiter@juwimm.com">Christiane Hausleiter</a>
 *
 */
public class Mail {

	private static Log log = LogFactory.getLog(Mail.class);

	private MimeMessage message = null;
	private String encoding = "UTF-8";
	private String messageText = null;
	private ArrayList<MimeBodyPart> attachments = null;
	private Hashtable<String, String> tempFileNameMappings = null;

	/**
	 * The value constructor initializes the instance.
	 * 
	 * @param mailDS the datasource to use
	 */
	public Mail(String mailDS) {
		try {
			Session session = (Session) new InitialContext().lookup(mailDS);
			if (session == null) {
				throw new IllegalArgumentException("session could not be initialized with mailDS '" + mailDS + "'");
			}
			initializeMail(session);
		} catch (NamingException exception) {
			log.error(exception);
		}
	}
	
	/**
	 * For testing purposes: Value constructor that creates a mail and its session based
	 * on the specified properties.
	 * 
	 * @param testProperties {@link Properties} containing at least a valid entry for "mail.host"
	 */
	public Mail(Properties testProperties) {
		Session session = Session.getDefaultInstance(testProperties);
		if (session == null) {
			throw new IllegalArgumentException("session could not be initialized with specified properties");
		}
		initializeMail(session);	
	}
	
	private void initializeMail(Session session) {
		this.message = new MimeMessage(session);
		this.attachments = new ArrayList<MimeBodyPart>();
		this.tempFileNameMappings = new Hashtable<String, String>();
	}

	/**
	 * Changes the mail's encoding from default (UTF-8) to ISO-8859-1.
	 */
	public void setEncodingToISO() {
		this.encoding = "ISO-8859-1";
	}

	/**
	 * Sets the mail's sender.
	 * 
	 * @param from the sender
	 */
	public void setFrom(String from) {
		try {
			this.message.setFrom(new InternetAddress(from));
		} catch (MessagingException exception) {
			log.error(exception);
		}
	}
	
	/**
	 * Returns the mail's sender.
	 * 
	 * @return the mail's sender
	 */
	public String getFrom() {
		try {
			return this.message.getFrom()[0].toString();
		} catch (MessagingException exception) {
			log.error(exception);
			return "";
		}
	}

	/**
	 * Sets the mail's receiver(s).
	 * 
	 * @param to the mail's receiver(s)
	 */
	public void setTo(String[] to) {
		if (to != null) {
			try {
				for (int i = 0; i < to.length; i++) {
					this.message.addRecipient(RecipientType.TO, new InternetAddress(to[i]));
				}
			} catch (MessagingException exception) {
				log.error(exception);
			}
		}
	}
	
	/**
	 * Adds one receiver.
	 * 
	 * @param to another mail's receiver
	 */
	public void addTo(String to) {
		if (to != null) {
			try {
				this.message.addRecipient(RecipientType.TO, new InternetAddress(to));
			} catch (MessagingException exception) {
				log.error(exception);
			}
		}
	}
	
	public String[] getTo() {
		return getRecipients(RecipientType.TO);
	}

	/**
	 * Sets the mail's carbon copy receiver(s).
	 * 
	 * @param cc the mail's cc receiver(s)
	 */
	public void setCc(String[] cc) {
		if (cc != null) {
			try {
				for (int i = 0; i < cc.length; i++) {
					this.message.addRecipient(RecipientType.CC, new InternetAddress(cc[i]));
				}
			} catch (MessagingException exception) {
				log.error(exception);
			}
		}
	}
	
	/**
	 * Adds a carbon copy receiver.
	 * 
	 * @param cc a cc receiver
	 */
	public void addCc(String cc) {
		if (cc != null) {
			try {
				this.message.addRecipient(RecipientType.CC, new InternetAddress(cc));
			} catch (MessagingException exception) {
				log.error(exception);
			}
		}
	}
	
	/**
	 * Returns all carbon copy receivers.
	 * 
	 * @return the carbon copy receivers
	 */
	public String[] getCc() {
		return getRecipients(RecipientType.CC);
	}

	/**
	 * Sets the mail's multiple blind carbon copy receiver(s).
	 * 
	 * @param bcc the mail's bcc receiver(s)
	 */
	public void setBcc(String[] bcc) {
		if (bcc != null) {
			try {
				for (int i = 0; i < bcc.length; i++) {
					this.message.addRecipient(RecipientType.BCC, new InternetAddress(bcc[i]));
				}
			} catch (MessagingException exception) {
				log.error(exception);
			}
		}
	}
	
	/**
	 * Adds a blind carbon copy receiver.
	 * 
	 * @param bcc a bcc receiver
	 */
	public void addBcc(String bcc) {
		if (bcc != null) {
			try {
				this.message.addRecipient(RecipientType.BCC, new InternetAddress(bcc));
			} catch (MessagingException exception) {
				log.error(exception);
			}
		}
	}
	
	/**
	 * Returns all blind carbon copy receiver(s).
	 * 
	 * @return the blind carbon copy receivers
	 */
	public String[] getBcc() {
		return getRecipients(RecipientType.BCC);
	}
	
	/**
	 * Sets the mail's subject.
	 * 
	 * @param subject the mail's subject
	 */
	public void setSubject(String subject) {
		try {
			this.message.setSubject(subject, this.encoding);
		} catch (MessagingException exception) {
			log.error(exception);
		}
	}
	
	/**
	 * Returns the mail's subject.
	 * 
	 * @return the mail's subject
	 */
	public String getSubject() {
		String result = "";
		try {
			result = this.message.getSubject();
		} catch (MessagingException exception) {
			log.error(exception);
		}
		return result;
	}

	/**
	 * Sets the mail's content. This may be plain text or HTML.
	 * If it is HTML, a plain text alternative (in case the receiver's client won't display HTML)
	 * may be specified when sending the mail. 
	 * 
	 * @see #sendHtmlMail(String)
	 * 
	 * @param bodyText the mail content to set
	 */
	public void setBody(String bodyText) {
		this.messageText = bodyText;
	}
	
	public void appendBody(String bodyText) {
		this.messageText = this.messageText + bodyText;
	}
	
	/**
	 * Returns a boolean indicating whether this message is valid and ready for being sent.
	 * 
	 * @return a boolean indicating whether the message is valid
	 */
	public boolean isMailSendable() {
		try {
			Address[] fromArray = this.message.getFrom();
			return (this.message.getSubject() != null && 
					getRecipients(RecipientType.TO).length > 0 &&
					fromArray != null &&
					fromArray.length > 0 &&
					fromArray[0] != null &&
					fromArray[0].toString().length() > 0);
		} catch (MessagingException exception) {
			log.error(exception);
			return false;
		}
		 
	}

	/**
	 * Creates an attachment by reading the specified {@code inputStream}, 
	 * creating a temporary file for the retreived data, and attaching this file.
	 * 
	 * TODO Create a certain temp file directory for this operation, so that we securely may clean up this directory whenever we want to!
	 * TODO synchronization?!?
	 * 
	 * @param inputStream the {@link InputStream} to get the attachment from
	 * @param fileName the attachment name to use
	 * @param mimeType the mimeType of the data
	 */
	public void addAttachmentFromInputStream(InputStream inputStream, String fileName, String mimeType) {
		FileOutputStream fileOutputStream = null;
		try {
			String oldFileName = fileName;
			String suffix = null;
			int dotIndex = fileName.lastIndexOf('.');
			if (dotIndex >= 0) {
				suffix = fileName.substring(dotIndex);
				fileName = fileName.substring(0, dotIndex);
			}
			// File.createTempFile() throws IllegalArgumentException if tempFileName does not contain at least 3 characters
			while (fileName.length() < 3) {
				fileName = "0" + fileName;
			}
			File tempFile = File.createTempFile(fileName, suffix);
			// temp file will be deleted when VM exits... won't be enough on a server!
			tempFile.deleteOnExit();
			this.tempFileNameMappings.put(tempFile.getAbsolutePath(), oldFileName);
			fileOutputStream = new FileOutputStream(tempFile);
			byte[] buffer = new byte[512];
			for (int length = 0; (length = inputStream.read(buffer)) != -1; ) {
				fileOutputStream.write(buffer, 0, length);
			}
			this.addAttachmentFromFile(tempFile.getAbsolutePath());
		} catch (IOException exception) {
			log.error("Error creating attachment " + fileName + " from inputStream", exception);
		} finally {
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();	
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException exception) {
				// ignored
			}
		}
	}

	/**
	 * Creates an attachment containing the specified file.
	 * 
	 * @param fileNameWithPath the absolute file name
	 */
	public void addAttachmentFromFile(String fileNameWithPath) {
		try {
			File file = new File(fileNameWithPath);
			MimeBodyPart bodyPart = new MimeBodyPart();
			bodyPart.attachFile(file);
			if (this.tempFileNameMappings.containsKey(fileNameWithPath)) {
				bodyPart.setFileName(this.tempFileNameMappings.get(fileNameWithPath));
			}
			this.attachments.add(bodyPart);
		} catch (IOException exception) {
			log.error("Error opening file " + fileNameWithPath, exception);
		} catch (MessagingException exception) {
			log.error("Error creating attachment comprising file " + fileNameWithPath);
		}

	}

	/**
	 * Creates an attachment by opening a connection to the specified URL, 
	 * creating a temporary file for the retreived data and attaching this file.
	 * 
	 * TODO Check that the temp file is properly deleted after attaching it!
	 * TODO Create a certain temp file directory for this operation, so that we securely may clean up this directory whenever we want to!
	 * TODO synchronization?!?
	 * 
	 * @param httpUrl the URL to get the attachment from
	 */
	public void addAttachmentFromUrl(String httpUrl) {
		try {
			URL url = new URL(httpUrl);
			String fileName = url.getPath();
			int slashIndex = fileName.lastIndexOf("/");
			if (slashIndex >= 0) {
				fileName = fileName.substring(slashIndex + 1);
			}
			String oldFileName = fileName;
			String suffix = null;
			int dotIndex = fileName.lastIndexOf('.');
			if (dotIndex >= 0) {
				suffix = fileName.substring(dotIndex);
				fileName = fileName.substring(0, dotIndex);
			}
			// File.createTempFile() throws IllegalArgumentException if tempFileName does not contain at least 3 characters
			while (fileName.length() < 3) {
				fileName = "0" + fileName;
			}
			File tempFile = File.createTempFile(fileName, suffix);
			// temp file will be deleted when VM exits... won't be enough on a server and does not work on win32 anyway!
			tempFile.deleteOnExit();
			this.tempFileNameMappings.put(tempFile.getAbsolutePath(), oldFileName);
			InputStream inputStream = url.openStream();
			FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
			byte[] buffer = new byte[512];
			for (int length = 0; (length = inputStream.read(buffer)) != -1; ) {
				fileOutputStream.write(buffer, 0, length);
			}
			fileOutputStream.close();
			inputStream.close();
			this.addAttachmentFromFile(tempFile.getAbsolutePath());
		} catch (MalformedURLException exception) {
			log.error("The URL is invalid: " + httpUrl, exception);
		} catch (IOException exception) {
			log.error("Error opening the URL " + httpUrl, exception);
		}

	}

	public boolean sendPlaintextMail() {
		try {
			if (this.attachments.size() > 0) {
				MimeMultipart multiPart = new MimeMultipart("mixed");
				BodyPart plainTextBodyPart = new MimeBodyPart();
				plainTextBodyPart.setText(this.messageText);
				multiPart.addBodyPart(plainTextBodyPart);
				for (int i = 0; i < this.attachments.size(); i++) {
					multiPart.addBodyPart(this.attachments.get(i));
				}
				this.message.setContent(multiPart);
			} else {
				this.message.setText(this.messageText, this.encoding, "plain");
			}
			this.message.saveChanges();
			doSend();
		} catch (MessagingException exception) {
			log.error("Error sending plain text mail", exception);
			return false;
		}
		return true;
	}

	public boolean sendHtmlMail(String alternativePlaintextBody) {
		try {
			if (this.attachments.size() > 0) {
				MimeMultipart mainMultiPart = new MimeMultipart("mixed");
				if (alternativePlaintextBody != null) {
					MimeMultipart alternativeMultiPart = new MimeMultipart("alternative");
					MimeBodyPart plainTextBodyPart = new MimeBodyPart();
					MimeBodyPart htmlBodyPart = new MimeBodyPart();
					plainTextBodyPart.setText(alternativePlaintextBody, this.encoding, "plain");
					htmlBodyPart.setText(this.messageText, this.encoding, "html");
					alternativeMultiPart.addBodyPart(plainTextBodyPart);
					alternativeMultiPart.addBodyPart(htmlBodyPart);
					
					MimeBodyPart containerBodyPart = new MimeBodyPart();
					containerBodyPart.setContent(alternativeMultiPart);
					mainMultiPart.addBodyPart(containerBodyPart);
				} else { // without plain text alternative
					MimeBodyPart htmlBodyPart = new MimeBodyPart();
					htmlBodyPart.setText(this.messageText, this.encoding, "html");
					mainMultiPart.addBodyPart(htmlBodyPart);
				}
				for (int i = 0; i < this.attachments.size(); i++) {
					mainMultiPart.addBodyPart(this.attachments.get(i));
				}
				this.message.setContent(mainMultiPart);
			} 
			else { // no attachments
				if (alternativePlaintextBody != null) {
					MimeMultipart mainMultipart = new MimeMultipart("alternative");
					MimeBodyPart plainTextBodyPart = new MimeBodyPart();
					MimeBodyPart htmlBodyPart = new MimeBodyPart();
					plainTextBodyPart.setText(alternativePlaintextBody, this.encoding, "plain");
					htmlBodyPart.setText(this.messageText, this.encoding, "html");
					mainMultipart.addBodyPart(plainTextBodyPart);
					mainMultipart.addBodyPart(htmlBodyPart);
					this.message.setContent(mainMultipart);
				}
				else { // no alternative plain text neither -> no MimeMessage!
					this.message.setContent(this.messageText, "text/html");
				}
			}
			this.message.saveChanges();
			doSend();
		} catch (MessagingException exception) {
			log.error("Error sending HTML mail", exception);
			return false;
		}
		return true;
	}
	
	/**
	 * Allows adding display names for attachments from outside this class.
	 * 
	 * @param absoluteFileName the attachment's absolute file name
	 * @param displayName the name for the attachment
	 */
	public void addNameToFileNameMappings(String absoluteFileName, String displayName) {
		this.tempFileNameMappings.put(absoluteFileName, displayName);
	}
	
	/**
	 * Returns all recipients for the specified {@code RecipientType}.
	 * 
	 * @param type the {@link RecipientType}
	 * @return a string array containing all recipients for the specified type
	 */
	private String[] getRecipients(RecipientType type) {
		String[] result;
		try {
			Address[] addresses = this.message.getRecipients(type);
			result = new String[addresses.length];
			for (int i = 0; i < addresses.length; i++) {
				result[i] = addresses[i].toString();
			}
		} catch (MessagingException exception) {
			log.error(exception);
			result = new String[0];
		}
		return result;
	}
	
	/**
	 * Sends the message and cleans up all temp files that were created for the attachments.
	 * 
	 * @throws MessagingException
	 */
	private void doSend() throws MessagingException {
		this.message.setSentDate(new Date());
		Transport.send(this.message);
		
		if (this.attachments.size() > 0) {
			Enumeration<String> enumeration = this.tempFileNameMappings.keys();
			while (enumeration.hasMoreElements()) {
				File file = new File(enumeration.nextElement());
				if (file.exists()) {
					if (!file.delete()) {
						log.error("Temp file " + file.getAbsolutePath() + " could not be deleted!");
					}
				}
			}
			this.attachments.clear();
			this.tempFileNameMappings.clear();
		}
	}
	
	public void clearTempFiles() {
		if (this.attachments.size() > 0) {
			Enumeration<String> enumeration = this.tempFileNameMappings.keys();
			while (enumeration.hasMoreElements()) {
				File file = new File(enumeration.nextElement());
				if (file.exists()) {
					if (!file.delete()) {
						log.error("Temp file " + file.getAbsolutePath() + " could not be deleted!");
					}
				}
			}
			this.attachments.clear();
			this.tempFileNameMappings.clear();
		}
	}

}
