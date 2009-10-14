cocoon.load("../../../tizzit-site-master/web/cforms/flow/captcha.js");

function conQuestMail(logname) {                      

	logname = "mail."+logname;
	var log = Packages.org.apache.log4j.Logger.getLogger(logname);
	log.debug("start form");

	//Store Requeststring
	var vcId   = cocoon.parameters["viewComponentId"];
    //filetype "*.cleanxml" muss in der Sitemap gematched werden und ohne cicnlude des Formulars ausgefuehrt werden,     
    //damit die Parameter eingelesen werden koennen, ohne dass das Script erneut asgefuehrt wird (sonst Endlosschleife).
        
	//var reqStr = "http://"+cocoon.parameters["serverName"]+"/content-"+vcId+".cleanxml";
	//log.debug("req-str: "+reqStr);
	
	/*erwartete Parameter: 
	|
	|____
							to
							bcc    (optional)
							cc     (optional)
							from
							subject
							
							formUri
							successUri
							errorUri (optional)
							_________																							|
																					|
*/
		 
	//Parameter aus ConQuest in Hashmap
		
	var paramsBuilder = new CRequestParamsBuilder(logname+".CRequestParamsBuilder");
	var params = paramsBuilder.getRequestParams(cocoon);
	
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
	
	
//DEBUG Paramvalues fuer Aufruf per /cforms/mail_simple.flow	
    /*
	var	to ="@juwimm.com";	
	var	bcc ="@juwimm.com";	
	var	cc ="@juwimm.com";
	var	from ="@juwimm.com";
	var	subject ="Testformular";
	var	formUri ="Formulardefinitionen/CallBack";
	var	successUri ="mail-success-pipeline.jx";
	var	errorUri ="mail-success-pipeline.jx";
	*/
	
//Pfad zum Form Model und Form Template
	var modelPath = "http://"+cocoon.parameters['serverName']+"/"+params.get('language')+"/"+formUri+"/formmodel.xml";	
	log.debug("modelPath ="+modelPath);	

//fuer das formtemplate verwendet cocoon immer automatisch das schema "cocoon:/", ein anderes ist nicht erlaubt	
	var templatePath = params.get('language')+"/"+formUri+"-conQuestForm";	
	log.debug("templatePath ="+templatePath);
	
	//START - CAPTCHA Protection
	try{
		var formUri 			= params.get('formUri');
		var xercesHelper 		= Packages.org.tizzit.util.XercesHelper;
		var flowsriptUtils 		= new FlowscriptSpringHelper.getFlowscriptUtils(cocoon.context.getContext("/"));
		var domDocUrl 			= "http://"+cocoon.parameters['serverName']+"/"+params.get('language')+"/"+formUri+"/content.xml"; 
		var domDocUrlToString 	= domDocUrl.toString();  
		var domIdDocument 		= flowsriptUtils.getDomDocument(domDocUrlToString);
		var domString			= xercesHelper.getNodeValue(domIdDocument, "//captcha/count");
	} catch(e){ 
		log.error(e);
	}
	log.debug("1");	
	var session = cocoon.session;
	log.debug("2");
	  	session.setAttribute("captcha", passcode(domString));
	  	log.debug("3");
	  	session.setAttribute("count", domString)
	  	log.debug("4");
	  	log.debug("count= "+ domString);
	//END - CAPTCHA Protection

//generiertes Formmodel holen	
	try {
		var form = new Form(modelPath);
	} catch (e) {
		log.error(e);
	}
	
//generiertes Formtemplate holen und Form zeigen
	log.debug("showForm");	
	log.debug("templatePath: "+templatePath);
	setUtf();	
	form.showForm(templatePath);
	
//Formulardaten holen
	var model = form.getModel();
	log.debug("Model: "+model);
    var mailMessage = getMailMessage(model);  
	log.debug("mailMessage "+mailMessage);
	

//Mail verschicken	

	//var cMail = new CMail("java:/conquestMail", "CMail");
	var cMail = new FlowscriptSpringHelper.getConquestMail(cocoon.context.getContext("/"));
	cMail.addTo(params.get('to'));	
	/* Abfrage ob mehrere "to" vorhanden sind. Im CMS to, to2, to3, etc... */
	if (params.get('to2')) {
		var x = 2;
		var hasAnotherTo = true;
		while(hasAnotherTo) {
			cMail.addTo(params.get('to'+x));
			x++;
			if (!params.get('to'+x)) {
				hasAnotherTo = false;
			}	
		}
	}
	/* Abfrage ob mehrere "cc" vorhanden sind. Im CMS cc, cc2, cc3, etc... */
    if (params.get('cc')){cMail.addCc(params.get('cc'))};
    if (params.get('cc2')) {
		var y = 2;
		var hasAnotherCc = true;
		while(hasAnotherCc) {
			cMail.addCc(params.get('cc'+y));
			y++;
			if (!params.get('cc'+y)) {
				hasAnotherCc = false;
			}	
		}
	}
    /* Abfrage ob mehrere "bcc" vorhanden sind. Im CMS bcc, bcc2, bcc3, etc... */
	if (params.get('bcc')){cMail.addBcc(params.get('bcc'))};
	if (params.get('bcc2')) {
		var z = 2;
		var hasAnotherBcc = true;
		while(hasAnotherBcc) {
			cMail.addBcc(params.get('bcc'+z));
			z++;
			if (!params.get('bcc'+z)) {
				hasAnotherBcc = false;
			}	
		}
	}
	cMail.setFrom(params.get('from'));
	cMail.setSubject(params.get('subject'));
	cMail.setBody(mailMessage);	
	
//ggf. Attachment holen und anhaengen
	
	var doesFormHasAttachment = false;
   	var i = 1;
   	var attachmentName = "attachment_"+i.toString();
	if (form.lookupWidget(attachmentName)!= null && form.lookupWidget(attachmentName)!='null'){
		doesFormHasAttachment = true;
		log.debug("doesFormHasAttachment "+doesFormHasAttachment);
	}
	if (doesFormHasAttachment){
    	var attachment = new CAttachment("mail.CAttachment",form.lookupWidget(attachmentName));
    	var hasAnotherAttachment = true;
    	while (hasAnotherAttachment){
    		log.debug("another Attachment");
    		log.debug("AttachmentName "+attachmentName);
	    	attachment.uploadWidget = form.lookupWidget(attachmentName);	    	
	    	attachment.readWidget();    
	    	attachment.readBytes();    
	    	attachment.readFilename();    
	    	attachment.readMimetype();	 
	    	//add Attachment to mail
	    	if(attachment.hasData){
				try{
			  		cMail.addAttachmentFromInputStream(attachment.uploadWidget.getValue().getInputStream(),attachment.getFilename(),attachment.getMimetype()); 
			  		log.debug ("Attachment_"+i.toString()+" sucessfully added to mail");  
			    }catch(e){
			    	log.error("Attachment coudln't be attached. "+e);
			    }
			}  	
		    i++;	     	 
    		attachmentName = "attachment_"+i.toString();    	 		
			if (form.lookupWidget(attachmentName)== null || form.lookupWidget(attachmentName)=='null'){
				hasAnotherAttachment = false;    	
			}
	    }   	
    }	
	
	log.debug("cMail.isMailSendable() "+cMail.isMailSendable());
	var isSent = false;
	if (cMail.isMailSendable()){
		try{
			isSent = cMail.sendPlaintextMail();
		}catch(e){
			log.error("Error at sendPlaintextMail()"+e);
		}		
	}else{	
		log.error("mail is not sendable.");			  
	}

	if (isSent){
		//success 
		log.debug("Mail is sent");
    	log.debug("show success Message");
    	log.debug("successUri"+params.get('successUri'));
        cocoon.sendPage(params.get('successUri'));
	}else{
		//fail
		log.error("mail is not sent.\n"+
			    "\n"+
               "configuration was:\n"+
               "from: "+params.get('from')+"\n"+
               "to: "+params.get('to')+"\n"+
               "cc: "+params.get('cc')+"\n"+
               "bcc: "+params.get('bcc')+"\n"+
               "subject: "+params.get('subject'));		    
               //error 
               log.error("show error Message");
		       cocoon.sendPage(errorUri);
	}

	return;
}