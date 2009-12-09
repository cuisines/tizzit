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
/*
 * Created on 26.10.2005
 */
package org.tizzit.util.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Helper-class offering static methods for sending emails
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class MailHelper {
	private static Session mailSession = null;
	private static Log log = LogFactory.getLog(MailHelper.class);

	static {
		Properties mailprops = new Properties();
		mailprops.put("mail.smtp.host", System.getProperty("mail.smtp.host"));
		mailSession = Session.getDefaultInstance(mailprops, null);
	}

	private MailHelper() {
	}

	/**
	 * Sends an email in text-format in iso-8859-1-encoding
	 * 
	 * @param subject the subject of the new mail
	 * @param message the content of the mail
	 * @param from the sender-address
	 * @param to the receiver-address
	 */
	public static void sendMail(String subject, String message, String from, String to) {
		sendMail(subject, message, from, to, null, null);
	}

	/**
	 * Sends an email in text-format in iso-8859-1-encoding
	 * 
	 * @param subject the subject of the new mail
	 * @param message the content of the mail
	 * @param from the sender-address
	 * @param to the receiver-address(es)
	 * @param cc the address of the receiver of a copy of this mail
	 * @param bcc the address of the receiver of a blind-copy of this mail
	 */
	public static void sendMail(String subject, String message, String from, String[] to, String cc, String bcc) {
		try {
			MimeMessage msg = new MimeMessage(mailSession);
			msg.setFrom(new InternetAddress(from));
			if (cc != null && !cc.equals("")) msg.setRecipients(Message.RecipientType.CC, cc);
			if (bcc != null && !bcc.equals("")) msg.setRecipients(Message.RecipientType.BCC, bcc);
			for (int i = to.length - 1; i >= 0; i--) {
				msg.addRecipients(Message.RecipientType.TO, to[i]);
			}
			msg.setSubject(subject, "iso-8859-1");
			msg.setSentDate(new Date());
			msg.setText(message, "iso-8859-1");
			Transport.send(msg);
		} catch (Exception e) {
			log.error("Error sending mail: " + e.getLocalizedMessage());
		}
	}

	/**
	 * Sends an email in text-format in iso-8859-1-encoding
	 * 
	 * @param subject the subject of the new mail
	 * @param message the content of the mail
	 * @param from the sender-address
	 * @param to the receiver-address
	 * @param cc the address of the receiver of a copy of this mail
	 * @param bcc the address of the receiver of a blind-copy of this mail
	 */
	public static void sendMail(String subject, String message, String from, String to, String cc, String bcc) {
		try {
			MimeMessage msg = new MimeMessage(mailSession);
			msg.setFrom(new InternetAddress(from));
			if (cc != null && !cc.equals("")) msg.setRecipients(Message.RecipientType.CC, cc);
			if (bcc != null && !bcc.equals("")) msg.setRecipients(Message.RecipientType.BCC, bcc);
			msg.addRecipients(Message.RecipientType.TO, to);
			msg.setSubject(subject, "iso-8859-1");
			msg.setSentDate(new Date());
			msg.setText(message, "iso-8859-1");
			Transport.send(msg);
		} catch (Exception e) {
			log.error("Error sending mail: " + e.getLocalizedMessage());
		}
	}

	/**
	 * Send an email in html-format in iso-8859-1-encoding
	 * 
	 * @param subject the subject of the new mail
	 * @param htmlMessage the content of the mail as html
	 * @param alternativeTextMessage the content of the mail as text
	 * @param from the sender-address
	 * @param to the receiver-address
	 * @param cc the address of the receiver of a copy of this mail
	 * @param bcc the address of the receiver of a blind-copy of this mail
	 */
	public static void sendHtmlMail(String subject, String htmlMessage, String alternativeTextMessage, String from, String to, String cc, String bcc) {
		try {
			MimeMessage msg = new MimeMessage(mailSession);
			msg.setFrom(new InternetAddress(from));
			if (cc != null && !cc.equals("")) msg.setRecipients(Message.RecipientType.CC, cc);
			if (bcc != null && !bcc.equals("")) msg.setRecipients(Message.RecipientType.BCC, bcc);
			msg.addRecipients(Message.RecipientType.TO, to);
			msg.setSubject(subject, "iso-8859-1");
			msg.setSentDate(new Date());

			MimeMultipart multiPart = new MimeMultipart();
			BodyPart bodyPart = new MimeBodyPart();

			bodyPart.setText(alternativeTextMessage);
			multiPart.addBodyPart(bodyPart);

			bodyPart = new MimeBodyPart();
			bodyPart.setContent(htmlMessage, "text/html");
			multiPart.addBodyPart(bodyPart);

			multiPart.setSubType("alternative");

			msg.setContent(multiPart);
			Transport.send(msg);
		} catch (Exception e) {
			log.error("Error sending html-mail: " + e.getLocalizedMessage());
		}
	}

	/**
	 * Send an email in html-format in iso-8859-1-encoding
	 * 
	 * @param subject the subject of the new mail
	 * @param htmlMessage the content of the mail as html
	 * @param alternativeTextMessage the content of the mail as text
	 * @param from the sender-address
	 * @param to the receiver-address
	 * @param cc the address of the receiver of a copy of this mail
	 * @param bcc the address of the receiver of a blind-copy of this mail
	 */
	public static void sendHtmlMail2(String subject, String htmlMessage, String alternativeTextMessage, String from, String to, String cc, String bcc) {
		try {
			MimeMessage msg = new MimeMessage(mailSession);
			msg.setFrom(new InternetAddress(from));
			if (cc != null && !cc.equals("")) msg.setRecipients(Message.RecipientType.CC, cc);
			if (bcc != null && !bcc.equals("")) msg.setRecipients(Message.RecipientType.BCC, bcc);
			msg.addRecipients(Message.RecipientType.TO, to);
			msg.setSubject(subject, "ISO8859_1");
			msg.setSentDate(new Date());

			MimeMultipart multiPart = new MimeMultipart("alternative");
			MimeBodyPart htmlPart = new MimeBodyPart();
			MimeBodyPart textPart = new MimeBodyPart();

			textPart.setText(alternativeTextMessage, "ISO8859_1");
			textPart.setHeader("MIME-Version", "1.0");
			//textPart.setHeader("Content-Type", textPart.getContentType());
			textPart.setHeader("Content-Type", "text/plain;charset=\"ISO-8859-1\"");

			htmlPart.setContent(htmlMessage, "text/html;charset=\"ISO8859_1\"");
			htmlPart.setHeader("MIME-Version", "1.0");
			htmlPart.setHeader("Content-Type", "text/html;charset=\"ISO-8859-1\"");
			//htmlPart.setHeader("Content-Type", htmlPart.getContentType());

			multiPart.addBodyPart(textPart);
			multiPart.addBodyPart(htmlPart);

			multiPart.setSubType("alternative");

			msg.setContent(multiPart);
			msg.setHeader("MIME-Version", "1.0");
			msg.setHeader("Content-Type", multiPart.getContentType());

			Transport.send(msg);
		} catch (Exception e) {
			log.error("Error sending html-mail: " + e.getLocalizedMessage());
		}
	}

	/**
	 * Send an email in html-format in iso-8859-1-encoding
	 * 
	 * @param subject the subject of the new mail
	 * @param htmlMessage the content of the mail as html
	 * @param alternativeTextMessage the content of the mail as text
	 * @param from the sender-address
	 * @param to the receiver-address
	 * @param cc the address of the receiver of a copy of this mail
	 * @param bcc the address of the receiver of a blind-copy of this mail
	 */
	public static void sendHtmlMail2(String subject, String htmlMessage, String alternativeTextMessage, String from, String[] to, String[] cc, String[] bcc) {
		try {
			MimeMessage msg = new MimeMessage(mailSession);
			msg.setFrom(new InternetAddress(from));

			if (cc != null && cc.length != 0) {
				for (int i = cc.length - 1; i >= 0; i--) {
					msg.addRecipients(Message.RecipientType.CC, cc[i]);
				}
			}
			if (bcc != null && bcc.length != 0) {
				for (int i = bcc.length - 1; i >= 0; i--) {
					msg.addRecipients(Message.RecipientType.BCC, bcc[i]);
				}
			}
			if (to != null && to.length != 0) {
				for (int i = to.length - 1; i >= 0; i--) {
					msg.addRecipients(Message.RecipientType.TO, to[i]);
				}
			}
			msg.setSubject(subject, "ISO8859_1");
			msg.setSentDate(new Date());

			MimeMultipart multiPart = new MimeMultipart("alternative");
			MimeBodyPart htmlPart = new MimeBodyPart();
			MimeBodyPart textPart = new MimeBodyPart();

			textPart.setText(alternativeTextMessage, "ISO8859_1");
			textPart.setHeader("MIME-Version", "1.0");
			//textPart.setHeader("Content-Type", textPart.getContentType());
			textPart.setHeader("Content-Type", "text/plain;charset=\"ISO-8859-1\"");

			htmlPart.setContent(htmlMessage, "text/html;charset=\"ISO8859_1\"");
			htmlPart.setHeader("MIME-Version", "1.0");
			htmlPart.setHeader("Content-Type", "text/html;charset=\"ISO-8859-1\"");
			//htmlPart.setHeader("Content-Type", htmlPart.getContentType());

			multiPart.addBodyPart(textPart);
			multiPart.addBodyPart(htmlPart);

			multiPart.setSubType("alternative");

			msg.setContent(multiPart);
			msg.setHeader("MIME-Version", "1.0");
			msg.setHeader("Content-Type", multiPart.getContentType());

			Transport.send(msg);
		} catch (Exception e) {
			log.error("Error sending html-mail: " + e.getLocalizedMessage());
		}
	}

}