importClass(java.lang.String);

function CDoccheckLogin(logname) {
    this.logprefix = logname+".CDoccheckLogin";
	this.log = Packages.org.apache.log4j.Logger.getLogger(this.logprefix);
	
	this.uniquekey = ''; 
	this.geschlecht = '';
	this.mandant = '';
	this.beruf = '';
	
	this.myCRegister = null;
	this.requestParams = null;
	
    //Initialisierung
	this.init = function(){
		this.myCRegister = new CRegister(this.logprefix);
		this.myCRegister.createRegisterService();
		//hier die Fallunterscheidungen und ggfs. Daten??bernahme der DocCheck Daten
   		this.requestParams = (new CRequestParamsBuilder(this.logprefix+".CRequestParamsBuilder")).getRequestParams(cocoon);
	}
	
	this.createDocUser = function(mandant, requestParams){
		var success=false;
		this.mandant = mandant;
		this.requestParams = requestParams;
		this.uniquekey = this.requestParams.get("uniquekey");
		this.log.debug("Uniquekey angekommen: "+this.uniquekey);
		var exists = this.myCRegister.userExists(this.mandant, this.uniquekey, '3');
		//Benutzer bereits in der DB
		if(exists){
			this.log.debug("DocCheckBenutzer existiert");
			if(this.requestParams.get("dc_name")){
				//DocCheckBenutzer mit Datenübertragung
				this.docWithData();
				success = this.myCRegister.updateUserVO();
			}else{
				//DocCheckBenutzer ohne Datenübertragung
				this.docWithoutData();
				success = this.myCRegister.updateUserVO();
			}
		}
		//Neuer Benutzer	
		else{
			this.log.debug("Neuer DocCheckBenutzer");
			if(this.requestParams.get("dc_name")){
				//DocCheckBenutzer mit Datenübertragung
				this.docWithData();
				this.myCRegister.setActive(true);
    			this.myCRegister.registerUserToLms(); 
    			this.log.debug("Neuer Benutzer beim LMS registriert: "+success);
    			success = this.myCRegister.registerUserVO('3');
			}else{
				//DocCheckBenutzer ohne Datenübertragung
				this.docWithoutData();
				this.myCRegister.setActive(true);
    			this.myCRegister.registerUserToLms(); 
    			this.log.debug("Neuer Benutzer beim LMS registriert: "+success);
    			success = this.myCRegister.registerUserVO('3');
			}
			
		}
		return success;
	}
	
	//DocCheckBenutzer mit Datenübertragung
	this.docWithData = function(){
		this.myCRegister.getUserVO(this.mandant, this.uniquekey, '3');
		this.log.debug("DocCheckBenutzer mit Datenübertragung");
		//Geschlecht ermitteln
		if(this.requestParams.get('dc_anrede')=='Frau'){
    	 	this.geschlecht='f';
    	    this.log.debug("User ist weiblich");
    	} else if(this.requestParams.get('dc_anrede')=='Herr'){
    	    this.geschlecht='m';
    	    this.log.debug("User ist maennlich");
    	} else{}
    	//Beruf ermitteln
    	this.beruf = this.getProfession();
    	this.myCRegister.setEmail(this.requestParams.get("dc_email"));
    	this.myCRegister.setDoccheckid(this.uniquekey);
    	this.checkData("name", this.requestParams.get('dc_name'));
    	this.checkData("vorname", this.requestParams.get('dc_vorname'));
    	this.checkData("titel", this.requestParams.get('dc_titel'));
    	this.checkData("stadt", this.requestParams.get('dc_ort'));
    	this.checkData("land", this.requestParams.get('dc_land'));
    	this.checkData("plz", this.requestParams.get('dc_plz'));
    	this.checkData("strasse", this.requestParams.get('dc_strasse'));
    	this.checkData("createDate", this.timestamp);
    	this.checkData("geschlecht", this.geschlecht);
    	this.checkData("beruf", this.getProfession(this.requestParams.get('dc_beruf'))); 
	}
	
	//DocCheckBenutzer ohne Datenübertragung
	this.docWithoutData = function(){
		this.myCRegister.getUserVO(this.mandant, this.uniquekey, '3');
		this.myCRegister.setEmail('anne.milbert@juwimm.com');
    	this.myCRegister.setDoccheckid(this.uniquekey);
    	this.checkData("beruf", this.getProfession(this.requestParams.get('dc_beruf')));
		this.log.debug("DocCheckBenutzer ohne Datenübertragung");
		if(!this.myCRegister.isActive()){
			this.myCRegister.setActive(true);
		}
		if(!this.myCRegister.isLmsuser()){
			var success = this.myCRegister.registerUserToLms();
			this.log.debug("Benutzer beim LMS registriert: "+success);
		}
	}
	
	//Beruf ermitteln
	this.getProfession = function(){
		if(this.requestParams.get('dc_beruf')==2 || this.requestParams.get('dc_beruf')==1002)
    	   this.beruf = 'apo';
    	else if(this.requestParams.get('dc_beruf')==1 || this.requestParams.get('dc_beruf')==107)
    	   this.beruf = 'arzt';
    	else if(!(this.requestParams.get('dc_beruf'))||this.requestParams.get('dc_beruf')=='')
	       this.beruf = 'all';  
	    else
    	   this.beruf = 'sonstige';
    	return this.beruf;
	}
	
	this.checkData = function(element, value){
		var test = this.myCRegister.getExtraValue(element);
		if(test == ''){
			this.myCRegister.addExtraValue(element, value);
		}
	}
	
	this.endDocLogin = function(){
		this.myCRegister.closeCRegister();
	}
}