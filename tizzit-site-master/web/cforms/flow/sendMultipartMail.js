function sendMultipartMailAtt(subject,sender,recipient,mailMessage, uploadBytes, filename, mimetype) {
        importPackage(Packages.javax.mail);
        importPackage(Packages.javax.naming);
        importPackage(Packages.javax.mail.internet);
        importPackage(Packages.javax.activation);
        importPackage(Packages.de.juwimm.util);
        importClass(java.lang.String);
       
        var ctx = new InitialContext();
        var mailSession = ctx.lookup("java:/paraMail");

        //create new internet message object
        var msg = new MimeMessage(mailSession);

        msg.setSubject(subject);//, "ISO-8859-1");
		msg.setFrom(new InternetAddress(sender));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

        mbp = new MimeBodyPart();
        mbp.setText(mailMessage.toString(), "ISO-8859-1");
        //create MimeMultipart with BodyPart
        mimemultipart = new MimeMultipart();
        mimemultipart.addBodyPart(mbp);

        if (uploadBytes != null) {
            var messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDisposition("attachment");
            messageBodyPart.setFileName(filename);
            messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(uploadBytes, mimetype)));

            mimemultipart.addBodyPart(messageBodyPart);
        }

        msg.setContent(mimemultipart);

        //send the message
        Transport.send(msg);   
}