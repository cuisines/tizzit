importClass(java.lang.String);

function multipartMail() {                      

	var log = Packages.org.apache.log4j.Logger.getLogger("mail_multipart");
	
	log.debug("start form");

	//Store Requeststring
	var vcId   = cocoon.parameters["viewComponentId"];
    //filetype "*.cleanxml" muss in der Sitemap gematched werden und ohne cicnlude des Formulars ausgefuehrt werden,     
    //damit die Parameter eingelesen werden koennen, ohne dass das Script erneut asgefuehrt wird (sonst Endlosschleife).
        
	var reqStr = "http://"+cocoon.parameters["serverName"]+"/content-"+vcId+".cleanxml";
	log.debug("req-str: "+reqStr);
		
	//Parameter aus ConQuest in Hashmap
		
	var paramsBuilder = new CConfigureParamsBuilder("CConfigureParamsBuilder");
	var params = paramsBuilder.getConfiguration(reqStr);
	
	log.debug("params ="+params);	
	//Pfad auf Formular
	var formUri = params.get('formUri');
	//URI auf Errorseite 
	var errorUri = "";
	if (params.get('errorUri')){
	    errorUri = params.get('errorUri');
	}else{
	    errorUri = "mail-error-cmspipeline.jx";
	}
			
//Pfad zum Form Model und Form Template
	var modelPath = "http://"+cocoon.parameters['serverName']+"/deutsch/"+formUri+"/formmodel.xml";	
	log.debug("modelPath ="+modelPath);	

//fuer das formtemplate verwendet cocoon immer automatisch das schema "cocoon:/", ein anderes ist nicht erlaubt	
	var templatePath = "deutsch/"+formUri+"-conQuestForm";	
	log.debug("templatePath ="+templatePath);	

//generiertes Formmodel holen	
	var form = new Form(modelPath);
	
//generiertes Formtemplate holen und Form zeigen
	log.debug("showForm");	
	log.debug("templatePath: "+templatePath);
	setUtf();	
	form.showForm(templatePath);
	
//Formulardaten holen
	var model = form.getModel();
    var mailMessage = getMailMessage(model);
	//log.debug("mailMessage "+mailMessage);
    
//Attachment holen
    var attachment = new CAttachment("CAttachment",form.lookupWidget("attachment"));
    attachment.readWidget();	
	
//Mail verschicken	
	var cMail = new CMPMail("java:/conquestMail", "CMPMail");
	cMail.addTo(params.get('to'));	
	
    log.debug("Mail senden an: "+params.get('to'));
    
    if (params.get('cc')){cMail.addCc(params.get('cc'))};
	if (params.get('bcc')){cMail.addBcc(params.get('bcc'))};
	cMail.setFrom(params.get('from'));

    log.debug("Mail gesendet von: "+params.get('from'));
	
	cMail.setSubject(params.get('subject'));
	cMail.setBody(mailMessage);
	
	//cMail.setEncoding("ISO-8859-1");
   
	if(attachment.hasData){
	  cMail.setAttachment(attachment);  
	}	
	log.debug("Nachricht verschicken.");
	var isSent = cMail.sendMail();
	log.debug("Mail ist verschickt: "+isSent );
		
	if (isSent){
        //success 
    	log.debug("show success Message");
    	log.debug("successUri: "+params.get('successUri'));
        cocoon.sendPage(params.get('successUri'));
	}else{
	    //error 
    	log.debug("show error Message");
        cocoon.sendPage(errorUri);
	}
		
	return;
}