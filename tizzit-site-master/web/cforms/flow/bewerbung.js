function bewerbung() {
	var log = Packages.org.apache.log4j.Logger.getLogger("bewerbung");

	//Store Requeststring
	var reqStr = cocoon.parameters["requestURI"];
    //filetype "*.cleanxml" muss in der Sitemap gemached werden und ohne cicnlude des Formulars ausgefuehrt werden,     
    //damit die Parameter eingelesen werden koennen, ohne dass das Script erneut asgefuehrt wird (sonst Endlosschleife).
        
	reqStr = "http://"+cocoon.parameters["serverName"]+reqStr.substring(0,reqStr.lastIndexOf("/")+1)+"content.cleanxml";
	log.warn("req-str: "+reqStr);
	/*erwartete Parameter: 
		to
		bcc
		cc
		from
		subject
		formUri
		successUri
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

	if (!cc) var cc = false;
	if (!bcc) var bcc = false;

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
	//setUtf();
	log.error("requestEncoding: "+getRequestCharacterEncoding());
	form.showForm(templatePath);
	
	//Mail verschicken
	log.warn("Mail verschicken");
	var model = form.getModel();
    mailMessage = getMailMessage(model);
    
    //Get Uploaded
	var uploadBytes = getUploadBytes(form, "attachment");
	var filename = "";
	var mimetype = "";
    if (uploadBytes != null) {
	    filename = form.lookupWidget("attachment").getValue().getHeaders().get("filename");
	    filename = filename.substring(filename.lastIndexOf("\\")+1);
	    mimetype = form.lookupWidget("attachment").getValue().getHeaders().get("content-type");
	    log.error("header: "+form.lookupWidget("attachment").getValue().getHeaders().toString());
	    log.error("mimetype: "+mimetype);
	    log.error("uploadBytes: "+uploadBytes);
	}
        
    //sendMailText(subject,from,to,bcc,cc,mailMessage); 
	sendMultipartMailAtt(subject,from,to,bcc,cc, mailMessage, uploadBytes, filename, mimetype);
	log.warn("Mail ist verschickt");
	
//success 
	log.warn("show success Message");
    cocoon.sendPage(successUri);
	
	return;
}

function getUploadBytes(form, widgetname) {
  var mediaBytes = java.lang.reflect.Array.newInstance(java.lang.Byte.TYPE, 2048);
  var bos = new java.io.ByteArrayOutputStream(2048);
  var uploadWidget = form.lookupWidget(widgetname);

  if (uploadWidget.getValue() != null) {
    var stream = uploadWidget.getValue().getInputStream();
    var inByteStream = new java.io.BufferedInputStream(stream);
    
    while ((len = inByteStream.read(mediaBytes)) != -1) {
        bos.write(mediaBytes, 0, len);
    }
  } else {
      return null;
  }
  
  return bos.toByteArray();
}

function sendMultipartMailAtt(subject, from, to, cc, bcc, mailMessage, uploadBytes, filename, mimetype) {
        importPackage(Packages.javax.mail);
        importPackage(Packages.javax.naming);
        importPackage(Packages.javax.mail.internet);
        importPackage(Packages.javax.activation);
        importPackage(Packages.de.juwimm.util);
        importClass(java.lang.String);
       
        var ctx = new InitialContext();
        var mailSession = ctx.lookup("java:/conquestMail");

        //create new internet message object
        var msg = new MimeMessage(mailSession);

        msg.setSubject(from);//, "ISO-8859-1");
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

        mbp = new MimeBodyPart();
        mbp.setText(mailMessage.toString(), "iso-8859-1");
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