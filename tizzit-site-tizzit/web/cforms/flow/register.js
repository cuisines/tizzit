importClass(Packages.de.juwimm.cms.cocoon.helper.FlowscriptSpringHelper);
importClass(Packages.de.juwimm.cms.cocoon.support.FlowscriptUtils);
importClass(Packages.de.juwimm.cms.cocoon.support.ConquestMail);
importClass(java.lang.String);

cocoon.load("flow/include.js");

function register() {	
    var timestamp 				= new java.util.Date();
	var logprefix 				= "standard.register";
	var logname 				= "mail";
	var log 					= Packages.org.apache.log4j.Logger.getLogger(logname);
	var myCRegister 			= null;
    var requestParams 			= (new CRequestParamsBuilder(logprefix+".CRequestParamsBuilder")).getRequestParams(cocoon);
    var xercesHelper 			= Packages.org.tizzit.util.XercesHelper;
    var errorMsg 				= '';
    var value 					= '';
    var mandant 				= 'standard';
    var keyCode 				= '';
    var error 					= false;
    var type 					= '1';
    var userExists				= '';    
      
    //Formular laden und anzeigen ...
   	if (requestParams.get("memorize") == null) getDocument(true); //reset binding document
	var userForm = new Form("forms/register/userdata_model.xml");
	userForm.createBinding("cocoon:/register-userdata_model-binding");
	userForm.load(getDocument());
	var errorType = "";
	do {
		userForm.showForm("de/register-userdata_template-pipeline.jx",{errorType:errorType});
	   	saveDocument(userForm);
	   	var userModel = userForm.getModel();
	} while(error);
      
    value = getDocumentValue("logname");
    myCRegister = new CRegister(logprefix+".myCRegister");
    myCRegister.createRegisterService();
   	userExists = myCRegister.userExists(mandant, value);
    log.debug("does the user already exist? - "+userExists);

	if (! userExists){
		myCRegister.getUserVO(mandant, value, type);
		myCRegister.setEmail(getDocumentValue("email"));
	    myCRegister.setActive(false);
	    myCRegister.setLoginname(value);
	    myCRegister.setPassword(getDocumentValue("passwd"));
	    myCRegister.addExtraValue("name", getDocumentValue("lastname"));
	    myCRegister.addExtraValue("vorname", getDocumentValue("firstname"));
	    myCRegister.addExtraValue("email", getDocumentValue("email"));
	    myCRegister.addExtraValue("strasse", getDocumentValue("strasse"));
	    myCRegister.addExtraValue("stadt", getDocumentValue("stadt"));
	    myCRegister.addExtraValue("plz", getDocumentValue("plz"));
	    myCRegister.addExtraValue("land", getDocumentValue("land"));
	    myCRegister.addExtraValue("tel", getDocumentValue("tel"));
	    myCRegister.addExtraValue("fax", getDocumentValue("fax"));
	    myCRegister.addExtraValue("createDate", timestamp);
		var registered = myCRegister.registerUserVO(type);
		
		//Datensatz bzw. neuen User in die DB schreiben
	    log.debug("is the registration sucessful? - "+registered);
	    myCRegister.closeCRegister();
		myCRegister = null;
		
		if (registered = true) {
			var userName = getDocumentValue("logname");
			var cfh = new Packages.de.juwimm.cms.cocoon.CformHelper("register", getJars("juwimm-registry"));
			var utl = cfh.instanciateClass("de.juwimm.registry.remote.RegistryServiceUtil"); 
    		var registerService = utl.getHome().create();
	    	var userVo = registerService.getUserByMandantAndLoginname("standard", userName);
	        var active = userVo.isActive();
    		log.debug("user active? - "+active);
    		userVo.setActive(true);
         	registerService.updateUser(userVo);
         	log.debug("registering user... done!");
       		var active = userVo.isActive();
       		log.debug("user active? - "+active);  
       		//successUri
			cocoon.sendPage("register-register_success-pipeline.jx");
		} else {
			log.debug('konnte registrierung nicht freischalten');
			cocoon.sendPage("register-register_failed-pipeline.jx");
		}
	} else {
		if (userExists = true) {
			userExists = "Der Benuzer existiert bereits in der Datenbank!";
			var bizdata = { "userExists" : userExists };
			cocoon.sendPage("register-register_failed-pipeline.jx", bizdata);
		} else {
			userExists = "Unbekannt...";
			var bizdata = { "userExists" : userExists };
			cocoon.sendPage("register-register_failed-pipeline.jx", bizdata);
		}
	}
	return;
}