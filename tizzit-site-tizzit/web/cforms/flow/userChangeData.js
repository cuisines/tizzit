importClass(java.lang.String);
importClass(Packages.org.tizzit.util.XercesHelper);

cocoon.load("flow/include.js");

var count = 0;
var titel = '';

function userChangeData() {
    var timestamp 			= new java.util.Date();
	var registerService;
	var logprefix 			= "standard.userChangeData";
	var log 				= Packages.org.apache.log4j.Logger.getLogger("mail");
    var error 				= false;
    var errorMsg 			= "";
    var loginname 			= "";
    var myCRegister 		= null;
	var mandant 			= 'standard';
	var keyCode 			= '';
	var emergencyStop 		= false;
	var requestParams 		= (new CRequestParamsBuilder("mail.CRequestParamsBuilder")).getRequestParams(cocoon);
	var configParams 		= requestParams; 
	var cqUserName 			= requestParams.get("safeguardUsername");

	var userForm = new Form("forms/register/changeuserdata_model.xml");
	userForm.createBinding("cocoon:/register-changeuserdata_model-binding");

    var cfh = new Packages.de.juwimm.cms.cocoon.CformHelper("register", getJars("juwimm-registry"));
    var utl = cfh.instanciateClass("de.juwimm.registry.remote.RegistryServiceUtil"); 
   	registerService = utl.getHome().create();

    var userVo = null;
    getDocument(true);
    
    userVo = registerService.getUserByMandantAndLoginname(mandant, cqUserName);
    setDocumentValue("logname", userVo.getLoginname());
    setDocumentValue("email", userVo.getEmail());
    setDocumentValue("passwd", userVo.getPassword());
    setDocumentValue("confirmpasswd", userVo.getPassword());
    setDocumentValue("lastname", registerService.getExtraValue(userVo, "name"));
    setDocumentValue("firstname", registerService.getExtraValue(userVo, "vorname"));
    setDocumentValue("strasse", registerService.getExtraValue(userVo, "strasse"));
    setDocumentValue("plz", registerService.getExtraValue(userVo, "plz"));
    setDocumentValue("stadt", registerService.getExtraValue(userVo, "stadt"));	
    setDocumentValue("abbrobationsnummer", registerService.getExtraValue(userVo, "abbrobationsnummer"));	
    
    cfh.retireBorderline(); 
	var error = false;
	var false_str = new java.lang.String("false");
	var errorType = "";
    userForm.load(getDocument());

    do {	  
   		userForm.showForm("de/register-changeuserdata_template-pipeline.jx",{errorType:errorType});
	   	saveDocument(userForm);
	   	var userModel = userForm.getModel();	
	} while(error);
    
    cfh.reuseClassloader();
	userVo.setEmail(getDocumentValue("email"));
	userVo.setPassword(getDocumentValue("passwd"));
	userVo = registerService.changeExtraValue(userVo, "name", getDocumentValue("lastname"));
	userVo = registerService.changeExtraValue(userVo, "vorname", getDocumentValue("firstname"));
	userVo = registerService.changeExtraValue(userVo, "strasse", getDocumentValue("strasse"));
	userVo = registerService.changeExtraValue(userVo, "stadt", getDocumentValue("stadt"));
	userVo = registerService.changeExtraValue(userVo, "plz", getDocumentValue("plz"));
	userVo = registerService.changeExtraValue(userVo, "abbrobationsnummer", getDocumentValue("abbrobationsnummer"));
   	var test = false;
   	test = registerService.updateUser(userVo);
   	cfh.retireBorderline();
   	cfh.reuseClassloader();
   	userVo = registerService.getUserByMandantAndLoginname(mandant, cqUserName);
   	loginname = userVo.getLoginname();
   	test = false;
   	test = registerService.updateUser(userVo);

    if (registerService != null) registerService.remove();
    cfh.retireBorderline();    

	cocoon.sendPage("de/register-changeuserdata_completed-pipeline.jx");

	return;
}