

function sendMail(array, formWidget) {
	java.lang.System.out.println("sending mail");

	var mailMessage = new Packages.java.lang.StringBuffer();
	
	try {
		var auth = getAuthentication();
		mailMessage.append("USERNAME: " + auth.userName + "\n");
	} catch(exe) {
	}
	
	var cookies = cocoon.request.getCookies();
	for( var c=0; c<cookies.length; c++) {
		mailMessage.append("COOKIE: " + cookies[c].name + " " + cookies[c].value + "\n");
	}
	
	mailMessage.append("\n");
	
	for(var counter = 0; counter < array.length; counter++) {
		var name = array[counter];
		if(name != null) {
			var widgetValue = null;
			if( formWidget.lookupWidget(name) != null ) {
		 		widgetValue = formWidget.lookupWidget(name).getValue();
			} 
			mailMessage.append(name);
			mailMessage.append(" = ");
			if(widgetValue != null) {
				mailMessage.append(widgetValue);
			}
			mailMessage.append("\n");
		}
	}
	
	java.lang.System.out.println(mailMessage.toString());	
	
	importPackage(Packages.javax.mail);
	importPackage(Packages.javax.naming);
	importPackage(Packages.javax.mail.internet);
	
	var ctx = new InitialContext();
	var mailSession = ctx.lookup("java:/GistMail");

	//create new internet message object
	var msg = new MimeMessage(mailSession);

	msg.setSubject("Gist Register entry");
	msg.setRecipient(Message.RecipientType.TO, new InternetAddress("hato.nordeck@juwimm.com"));
	msg.setContent(mailMessage.toString(),"text/plain");

	//send the message
	Transport.send(msg); 	
}