importClass(java.lang.String);

/*
cocoon.load("sendMailText.js");
cocoon.load("sendMultipartMail.js");
cocoon.load("sendMail.js");
*/

function mail() {                      

	var log = Packages.org.apache.log4j.Logger.getLogger("mail");
	log.warn("start form");

	//Store Requeststring
	var reqStr = cocoon.parameters["requestURI"];
    //filetype "*.cleanxml" muss in der Sitemap gemached werden und ohne cicnlude des Formulars ausgefuehrt werden,     
    //damit die Parameter eingelesen werden koennen, ohne dass das Script erneut asgefuehrt wird (sonst Endlosschleife).
        
	reqStr = "http://"+cocoon.parameters["serverName"]+reqStr.substring(0,reqStr.lastIndexOf("/")+1)+"content.cleanxml";
	log.warn("req-str: "+reqStr);
	
	/*erwartete Parameter: 
	|
	|____
							to
							bcc
							cc
							from
							subject
							
							formUri
							successUri
														_________
																							|
																							|
	
	*/
	
//Get XML-Document of given URL as DOM	
	var document = loadDocument(reqStr);
	var params   = document.getElementsByTagName("item");	
	paramsLength = params.getLength();
	log.warn("paramsLength ="+paramsLength);	
	for (i=0;i<paramsLength;i++){
	    paramName = params.item(i).getElementsByTagName("param-name").item(0).getChildNodes().item(0).getNodeValue();
        log.warn("paramName ="+paramName);
	    paramValue =params.item(i).getElementsByTagName("param-value").item(0).getChildNodes().item(0).getNodeValue();	    
        log.warn("paramValue ="+paramValue);
        eval("var "+paramName+" = paramValue");
	}

if (!cc){
    var cc = false;
}
if (!bcc){
    var bcc = false;
}
log.warn("bcc ="+bcc);	
	
//Pfad zum Form Model und Form Template
	modelPath = "http://"+cocoon.parameters["serverName"]+"/deutsch/"+formUri+"/formmodel.xml";	
	log.warn("modelPath ="+modelPath);	

//fuer das formtemplate verwendet cocoon immer automatisch das schema "cocoon:/", ein anderes ist nicht erlaubt	
	templatePath = "deutsch/"+formUri+"-conQuestForm";	
	log.warn("templatePath ="+templatePath);	

//generiertes Formmodel holen	
	var form = new Form(modelPath);

	
//generiertes Formtemplate holen und Form zeigen
	log.warn("showForm");	
	log.warn("templatePath: "+templatePath);
	setUtf();	
	form.showForm(templatePath);
	
//Mail verschicken
	log.warn("Mail verschicken");
	var model = form.getModel();
    mailMessage = getMailMessage(model);
    sendMailText(subject,from,to,bcc,cc,mailMessage); 
	log.warn("Mail ist verschickt");
	
//success 
	log.warn("show success Message");
    cocoon.sendPage(successUri);
	
	return;
}

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