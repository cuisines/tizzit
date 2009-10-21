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
    	try {
    		this.cfh = new Packages.de.juwimm.cms.cocoon.CformHelper("register", getJars("juwimm-registry"));
    		this.utl = cfh.instanciateClass("de.juwimm.registry.remote.RegistryServiceUtil");
    		
    		this.registerService = this.utl.getHome().create();
    	} catch(e){
    		this.log.error("Fehler beim registerService: "+e);
    		
    	}
    	
	}
	
	this.getUserVO = function(mandant, value, type){
	    this.mandant = mandant;
	    this.type = type;
	    this.value = value;
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
        }
        catch(e){
        	this.log.error("Fehler bei getUserVO: "+e);
        }
        
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
    
    this.userExists = function(mandant, value){
        this.mandant = mandant;
	    this.value = value;
        var success = false;
        try {
        	this.loginname = value;
        	success = this.registerService.userExistsByMandantAndLoginname(mandant, this.loginname);
        }
        catch(e){
        	this.log.error("Fehler2: "+e);
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
	    	this.registerService.registerUserToLms(this.userVO); 
	        this.userVO.setLmsuser(true); 
        } catch (e) {
        	this.log.error("Fehler bei Register User to LMS: "+e); 
        }
    }
    
    
    this.updateUserVO = function(){
    	var success = false;
        try{    
        	success = this.registerService.updateUser(this.userVO);
        } catch(e) { 
        	this.log.error("Error while updating user data: "+e); 
        }
    }
        
    this.getDoccheckid = function(){
           return this.doccheckid;
    } 
    
    this.setDoccheckid = function(doccheckid){
           this.doccheckid = doccheckid;
           this.userVO.setDoccheckid(this.doccheckid);
    }
    
    this.getEmail = function(){
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
           return this.pin;
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