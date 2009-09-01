function CRegister(logname) {
    this.log = Packages.org.apache.log4j.Logger.getLogger(logname+".CRegister");
    
	this.type = 0;
	this.id = 0;
	
	this.mandant = "";
	this.email = "";
	this.doccheckid = "";
	this.pin = "";
	this.password="";
	this.keycode = "";
	this.loginname = "";
	this.value = "";
	
	this.cfh = null;
    this.utl = null;
    this.registerService = null;
	this.userVO = null; 
	
	this.active = false;
	this.lmsuser = false; 
	
	this.createRegisterService = function(){
	    if (this.log.isDebugEnabled()) {
			this.log.debug("start creating RegisterService!");
		}
    	try {
    		this.cfh = new Packages.de.juwimm.cms.cocoon.CformHelper("register", getJars("juwimm-registry"));
    		this.utl = this.cfh.instanciateClass("de.juwimm.registry.remote.RegistryServiceUtil");
    		this.registerService = this.utl.getHome().create();
    		this.log.debug("RegisterService created\n");
    	} catch(e){
    		this.log.error("Fehler: "+e);
    	}
	}
	
	/*
	Parameter type:       _________	
							
	                        1: Loginname
							2: Pin
							3: DoccheckId
							Default: ID (oder Loginname)
							_________																							|
	*/
	
	this.getUserVO = function(mandant, value, type){
	    this.mandant = mandant;
	    this.type = type;
	    this.value = value;
        this.log.debug("Mandant: "+mandant);
        this.log.debug("Value: "+value);	
    	try {
    	 switch(type){
    	  //Loginname
    	  case "1":
    	    this.loginname = value;
        	this.userVO = this.registerService.getUserByMandantAndLoginname(this.mandant, this.loginname);
        	break;
          //Pin
          case "2":
            this.pin = value;
        	this.userVO = this.registerService.getUserByMandantAndPin(this.mandant, this.pin);
        	break;
          //DoccheckId
          case "3":
            this.doccheckid = value;
        	this.userVO = this.registerService.getUserByMandantAndDoccheckid(this.mandant, this.doccheckid);
        	break;
          //ID
          default:
            this.id = Integer.parseInt(value);
            this.userVO = this.registerService.getUserById(this.id);
            break;
    	 }
    	 this.log.debug("UserVO: "+this.userVO);
    	 this.updateLocalFields();
        }
        catch(e){
        	this.log.error("Fehler bei getUserVO: "+e);
        }
        
    }
         
    this.updateLocalFields = function(){
    		this.log.debug("update local Fields with new VO");
			this.id = this.userVO.getId() ;
			this.log.debug("user id "+this.id);
			this.mandant = this.userVO.getMandant();
			this.email = this.userVO.getEmail();
			this.doccheckid = this.userVO.getDoccheckid();
			this.pin = this.userVO.getPin();
			this.password=this.userVO.getPassword();
			this.keycode = this.userVO.getKeycode();
			this.loginname = this.userVO.getLoginname();
			this.active = this.userVO.isActive();
			this.lmsuser = this.userVO.isLmsuser();    	
    }
    
    this.registerUserVO = function(type){
        var success = false;
        this.type = type;
	    try {
    	 switch(type){
    	  //Loginname
    	  case "1":
        	success = this.registerService.registerUserByMandantAndLoginname(this.userVO);
        	break;
          //Pin
          case "2":
        	success = this.registerService.registerUserByMandantAndPin(this.userVO);
        	break;
          //DoccheckId
          case "3":
        	success = this.registerService.registerUserByMandantAndDoccheckid(this.userVO);
        	break;
          //Default: Loginname
          default:
            success = this.registerService.registerUserByMandantAndLoginname(this.userVO);
            break;
    	 }
    	}
        catch(e){
        	this.log.error("Fehler beim Registrieren: "+e);
        }
        
        return success;
    }
    
    this.userExists = function(mandant, value, type){
        this.mandant = mandant;
	    this.type = type;
	    this.value = value;
        var success = false;
        try {
    	 switch(type){
    	  //Loginname
    	  case "1":
        	this.loginname = value;
        	success = this.registerService.userExistsByMandantAndLoginname(mandant, this.loginname);
        	break;
          //Pin
          case "2":
        	this.pin = value;
        	success = this.registerService.userExistsByMandantAndPin(mandant, this.pin);
        	break;
          //DoccheckId
          case "3":
        	this.doccheckid = value;
        	success = this.registerService.userExistsByMandantAndDoccheckid(mandant, this.doccheckid);
        	break;
          //Default: ID
          default:
            this.id = Integer.parseInt(value);
        	success = this.registerService.userExistsById(this.id);
            break;
    	 }
    	}
        catch(e){
        	this.log.error("Fehler: "+e);
        }
        return success;
    }
    
    this.unregisterUser = function(){
        var success = false;
        this.registerService.unregisterUserById(this.id);
        return success;
    }
    
    this.registerUserToLms = function(){
    	try { 
            //this.cfh.retireBorderline();
	    	//this.cfh.reuseClassloader();
	    	//log.debug("reuseClassloader: " + this.cfh.reuseClassloader());
	    	//this.createRegisterService();
	    	//this.log.debug("Mandant: "+this.mandant);
	    	//this.log.debug("Value: "+this.value);
	    	//this.log.debug("Type: "+this.type);
	    	//this.getUserVO(this.mandant, this.value, this.type);
	    	//this.log.debug("RegisterUserToLMS: userVO = "+this.userVO);
	    	this.registerService.registerUserToLms(this.userVO); 
	        this.userVO.setLmsuser(true); 
	        //this.updateUserVO();
        } catch (e) { this.log.error("Fehler bei Register User to LMS: "+e); }
    }
    
    
    this.updateUserVO = function(){
    	var success = false;
        try{    
        	this.log.debug("this.userVO: "+this.registerService.updateUser(this.userVO));
        	success = this.registerService.updateUser(this.userVO);
        	this.log.debug("hat das update geklappt? "+success);
        } catch(e) { this.log.error("Error while updating user data: "+e); }
    }
        
    this.getDoccheckid = function(){
           return this.doccheckid;
    } 
    
    this.setDoccheckid = function(doccheckid){
           this.doccheckid = doccheckid;
           this.userVO.setDoccheckid(this.doccheckid);
    }
    
    this.getEmail = function(){
    	if(this.email == ""){
    		this.email = this.userVO.getEmail();
    	}
        return this.email;
    } 
    
    this.setEmail = function(email){
           this.email = email;
           this.userVO.setEmail(this.email);
    }
    
    this.getExtravalues = function(){
           return this.userVO.getExtravalues();
    } 
        
    this.getId = function(){
           return this.id;
    } 
    
    this.setId = function(id){
           this.id = id;
           this.userVO.setId(this.id);
    }
    
    this.getKeycode = function(){
           return this.keycode;
    } 
    
    this.setKeycode = function(keycode){
           this.keycode = keycode;
           this.userVO.setKeycode(this.keycode);
    }
    
    this.getLoginname = function(){
           return this.userVO.getLoginname();
    } 
    
    this.setLoginname = function(loginname){
           this.loginname = loginname;
           return this.userVO.setLoginname(this.loginname);
    }
    
    this.getMandant = function(){
           return this.userVO.getMandant();
    } 
    
    this.setMandant = function(mandant){
           this.mandant = mandant;
           return this.userVO.setMandant(this.mandant);
    }
    
    this.getPassword = function(){
           return this.password;
    } 
    
    this.setPassword = function(pw){
           this.password = pw;
           return this.userVO.setPassword(this.password);
    }
    
    this.getPin = function(){
           return this.userVO.getPin();
    } 
    
    this.setPin = function(pin){
           this.pin = pin;
           this.userVO.setPin(this.pin);
    }
    
    this.getXml = function(){
           var xml = this.userVO.getXml();
           return xml;
    } 
    
    this.setXML = function(xml){
           this.xml = xml;
           this.userVO.setXml(this.xml);
    }
    
    this.isActive = function(){
           return this.active;
    } 
    
    this.setActive = function(active){
           this.active = active;
           this.userVO.setActive(this.active);
    }
    
    this.isLmsuser = function(){
           this.lmsuser;
    } 
    
    
    this.closeCRegister = function(){
        this.registerService = null;
        this.cfh.retireBorderline();
        this.log.debug("cfh retired");
    }
    
    /*Alle Methoden aus der Klasse RegistryService*/
    
    this.activateUser = function(keycode){
    	this.keycode = keycode;
    	var success = false;
    	success = this.registerService.activateUser(this.userVO, this.keycode);
    	return success;
    }
    
    this.addExtraValue = function(property, value){
    	try{	
    		this.userVO = this.registerService.addExtraValue(this.userVO, property, value);
    	} catch(e){
    		this.log.error("Fehler bei addExtraValue: "+e);
    	}
    	this.log.debug("Extravalue added: "+value);
    }
    
    this.addExtraValueToList = function(listName, value){
    	this.userVO = this.registerService.addExtraValueToList(this.userVO, listName, value);
    }
    
    this.changeExtraValue = function(property, value){
    	this.userVO = this.registerService.changeExtraValue(this.userVO, property, value);
    }
    
    this.changeExtraValueListItem = function(listName, oldValue, newValue){
    	this.userVO = this.registerService.changeExtraValueListItem(this.userVO, listName, oldValue, newValue);
    }
    
    this.getExtraValue = function(property){
    	var value = this.registerService.getExtraValue(this.userVO, property);
    	this.log.debug("Extravalue '"+property+"': "+value);
    	this.log.debug("this.userVO: "+this.userVO);
    	this.log.debug(this.registerService.getExtraValue(this.userVO, property));
    	return value;
    }
    
    this.getExtraValueList = function(listName){
    	var listName = new Object();
		listName[listName]= new Array();
		listName = this.registerService.getExtraValueList(this.userVO, listName);
		return listName;
    }
    
    this.removeAllExtraValues = function(){
    	this.userVO = this.registerService.removeAllExtraValues(this.userVO);
    }
    
    this.removeExtraValue = function(property){
    	this.userVO = this.registerService.removeExtraValue(this.userVO, property);
    }
    
    this.removeExtraValueList = function(listName){
    	this.userVO = this.registerService.removeExtraValueList(this.userVO, listName);
    }
    
    this.removeExtraValueListItem = function(listName, value){
    	this.userVO = this.registerService.removeExtraValueListItem(this.userVO, listName, value);
    }
    
    this.unregisterUserById = function(id){
    	this.id = id;
    	var success = false;
    	success = this.registerService.unregisterUserById(this.id);
    	return success;
    }
}