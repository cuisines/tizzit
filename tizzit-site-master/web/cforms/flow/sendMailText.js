function sendMailText(subject,from,to,bcc,cc,mailMessage) {
	

	java.lang.System.out.println("sending mail");

	importPackage(Packages.javax.mail);
	importPackage(Packages.javax.naming);
	importPackage(Packages.javax.mail.internet);
	importClass(java.lang.String);
	
	
	var ctx = new InitialContext();
	var mailSession = ctx.lookup("java:/conquestMail");

	//create new internet message object
	var msg = new MimeMessage(mailSession);
	msg.setSubject(subject);
	msg.setFrom(new InternetAddress(from));
	msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
	try{
        if (cc){
            msg.setRecipient(Message.RecipientType.CC, new InternetAddress(cc));
        }
        if (bcc){
            msg.setRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
        }
	}
	catch(e){	
    	log.warn("cc oder bcc wurde nicht gesetzt"+e);
	}
//create MimeBodyPart
 mbp = new MimeBodyPart();
 mbp.setText(mailMessage, "ISO-8859-1");
 
 //create MimeMultipart with BodyPart
 mimemultipart = new MimeMultipart();
 mimemultipart.addBodyPart(mbp);
 //set MultipartMessage
 msg.setContent(mimemultipart);
        
 //	msg.setContent(mailMessage.toString()+mailMessageIso+mailMessageUtf,"text/plain");
 //msg.setContent(mailMessage,"text/plain");
 //send the message
 Transport.send(msg); 	
}