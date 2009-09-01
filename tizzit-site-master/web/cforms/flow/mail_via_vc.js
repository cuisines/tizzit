importClass(java.lang.String);

function mail() {                      

	var log = Packages.org.apache.log4j.Logger.getLogger("mail");
	log.debug("start form");

	//Store Requeststring
	var vcId   = cocoon.parameters["viewComponentId"];
    //filetype "*.cleanxml" muss in der Sitemap gematched werden und ohne cicnlude des Formulars ausgefuehrt werden,     
    //damit die Parameter eingelesen werden koennen, ohne dass das Script erneut asgefuehrt wird (sonst Endlosschleife).
        
	var reqStr = "http://"+cocoon.parameters["serverName"]+"/content-"+vcId+".cleanxml";
	log.debug("req-str: "+reqStr);
	
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
	
	
//DEBUG Paramvalues fuer Aufruf per /cforms/mail_simple.flow	
    /*
	var	to ="hans-thomas.nordeck@juwimm.com";	
	var	bcc ="hans-thomas.nordeck@juwimm.com";	
	var	cc ="hans-thomas.nordeck@juwimm.com";
	var	from ="hans-thomas.nordeck@juwimm.com";
	var	subject ="Testformular";
	var	formUri ="Formulardefinitionen/CallBack";
	var	successUri ="mail-success-pipeline.jx";
	var	errorUri ="mail-success-pipeline.jx";
	*/
	
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
	log.debug("Model: "+model);
    var mailMessage = getMailMessage(model);  
	log.debug("mailMessage "+mailMessage);
	
//Mail verschicken	

	var cMail = new CMail("java:/conquestMail", "CMail");
	cMail.addTo(params.get('to'));	
    if (params.get('cc')){cMail.addCc(params.get('cc'))};
	if (params.get('bcc')){cMail.addBcc(params.get('bcc'))};
	cMail.setFrom(params.get('from'));
	cMail.setSubject(params.get('subject'));
	cMail.setBody(mailMessage);
	
	//cMail.setEncoding("ISO-8859-1");	
	var isSent = cMail.sendMail();
	log.debug("Mail ist verschickt: "+isSent );	
		
	if (isSent){
        //success 
    	log.debug("show success Message");
    	log.debug("successUri"+params.get('successUri'));
        cocoon.sendPage(params.get('successUri'));
	}else{
	    error 
    	log.debug("show error Message");
        cocoon.sendPage(errorUri);
		}
	return;
}