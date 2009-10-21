
function CSMGRegister(logname) {
    this.log = Packages.org.apache.log4j.Logger.getLogger(logname+".CSMGRegister");
    
	this.type = 1;
	this.id = 0;	
	this.mandant = "";
	this.loginname = "";
	this.pin = 0;
	this.doccheckid = 0;
	
	this.cfh = null;
    this.utl = null;
    this.smgService = null;
	this.personVO = null; 
	this.personDOM;
	
	this.createSmgService = function(mandant){
	    if (this.log.isDebugEnabled()) {
			this.log.debug("start creating SmgService!");
		}
		this.mandant=mandant;
    	try {
    		this.cfh = new Packages.org.tizzit.core.classloading.ClassloadingHelper(); 
    		this.smgService = cfh.getInstance("de.juwimm.smg.cocoon.CformAccessor").getWebsiteService();
    		this.log.debug("SMG Service created\n");
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
	
	this.getPersonVO = function(value, type){
		//SMG unterst?tzt nur type 1, getUserByMandantAndLoginname
		type = 1;
	    this.type = type;
        this.log.debug("Value: "+value);
        this.log.debug("type: "+type);
        this.log.debug("typeof(type): "+typeof(type));	
    	try {
    	 switch(type){
    	  //Loginname
    	  case 1:
    	    this.loginname = value;
    	    this.log.debug("site und loginname: "+this.mandant +" "+ this.loginname);
    	    //this.log.debug("this.smgService.userExistsBySiteAndLoginname(this.mandant, this.loginname): "+this.smgService.userExistsBySiteAndLoginname(this.mandant, this.loginname));
        	
        	try {
        	    this.personVO = this.smgService.getUserBySiteAndLoginname(this.mandant, this.loginname);
        	    log.debug("user geholt: " + this.personVO);
            } catch(e) {
                log.debug("hier: " + e);
            }
        	this.log.debug("this.personVO.getFirstName(): "+this.personVO.getFirstName());
        	this.log.debug("this.personVO.getPersonId(): "+this.personVO.getPersonId());
        	this.log.debug("this.personVO.getSiteId(): "+this.personVO.getSiteId());
        	this.log.debug("this.personVO.getUserName(): "+this.personVO.getUserName());
        	break;
        	/*
          //Pin
          case 2:
            this.pin = value;
        	this.personVO = this.smgService.getUserByMandantAndPin(this.mandant, this.pin);
        	break;
          //DoccheckId
          case 3:
            this.doccheckid = value;
        	this.personVO = this.smgService.getUserByMandantAndDoccheckid(this.mandant, this.doccheckid);
        	break;
          //ID*/
          default:
          	this.log.error("falscher logintype, methoden nicht vorhanden");
            //this.id = Integer.parseInt(value);
            //this.personVO = this.smgService.getUserById(this.id);
            break;
            
    	 }
    	 this.log.debug("personVO: "+this.personVO);
    	 
    	 
    	 try {
            this.log.debug("is arzt: " + this.personVO.isArzt());
            this.log.debug("Institution" + this.personVO.getInstitution() );
        } catch(e) {
            log.error(e);
        }
    	 
    	 //this.updateLocalFields();
        }
        catch(e){
        	this.log.error("Fehler bei getPersonVO: "+e);
        }
        
    }
        
     /*    
    this.updateLocalFields = function(){
    		this.log.debug("update local Fields with new VO");
			this.id = this.personVO.getId() ;
			this.log.debug("user id "+this.id);
			this.mandant = this.personVO.getMandant();
			this.email = this.personVO.getEmail();
			this.doccheckid = this.personVO.getDoccheckid();
			this.pin = this.personVO.getPin();
			this.password=this.personVO.getPassword();
			this.keycode = this.personVO.getKeycode();
			this.loginname = this.personVO.getLoginname();
			this.active = this.personVO.isActive();
			this.lmsuser = this.personVO.isLmsuser();    	
    }
    * 
    * */
    
    this.registerpersonVO = function(type){
    	//SMG unterst?tzt nur type 1
    	type = 1;
        var success = false;
        this.type = type;
	    try {
    	 switch(type){
    	  //Loginname
    	  case "1":
        	success = this.smgService.registerUserByMandantAndLoginname(this.personVO);
        	break;
        	/*
          //Pin
          case "2":
        	success = this.smgService.registerUserByMandantAndPin(this.personVO);
        	break;
          //DoccheckId
          case "3":
        	success = this.smgService.registerUserByMandantAndDoccheckid(this.personVO);
        	break;
          //Default: Loginname
          default:
            success = this.smgService.registerUserByMandantAndLoginname(this.personVO);
            break;
            * */
    	 }
    	}
        catch(e){
        	this.log.error("Fehler beim Registrieren: "+e);
        }
        
        return success;
    }
    
    this.userExists = function(value, type){
    	//SMG unterst?tzt nur type 1
    	type = 1;
        this.mandant = mandant;
	    this.type = type;
	    this.value = value;
        var success = false;
        try {
    	 switch(type){
    	  //Loginname
    	  case "1":
        	this.loginname = value;
        	success = this.smgService.userExistsByMandantAndLoginname(this.mandant, this.loginname);
        	break;
        	/*
          //Pin
          case "2":
        	this.pin = value;
        	success = this.smgService.userExistsByMandantAndPin(this.mandant, this.pin);
        	break;
          //DoccheckId
          case "3":
        	this.doccheckid = value;
        	success = this.smgService.userExistsByMandantAndDoccheckid(this.mandant, this.doccheckid);
        	break;
          //Default: ID
          default:
            this.id = Integer.parseInt(value);
        	success = this.smgService.userExistsById(this.id);
            break;
             */
    	 }
    	}
        catch(e){
        	this.log.error("Fehler: "+e);
        }
        return success;
    }
    
    this.unregisterUser = function(){
        var success = false;
        this.smgService.unregisterUserById(this.id);
        return success;
    }
    
    this.registerUserToLms = function(){
    	try { 
	    	this.smgService.registerUserToLms(this.personVO); 
	        //this.personVO.setLmsuser(true);
        } catch (e) { this.log.error("Fehler bei Register User to LMS: "+e); }
    }
    
    
    this.save = function(){
        try {
        	if (this.personVO.isArzt()) {
        	    var resultVO = this.smgService.updateArzt(this.personVO);
            } else {
                var resultVO = this.smgService.updateEmployee(this.personVO);
            }
        	return resultVO;
        } catch(e) { 
        	this.log.error("Error while updating user data: "+e); 
        	throw(e);	
        }
    }
        
    this.getDoccheckid = function(){
           return this.doccheckid;
    } 
    
    this.setDoccheckid = function(doccheckid){
           //this.personVO.setDoccheckid(this.doccheckid);
    }
    
    this.getEmail = function(){
        return this.personVO.getEmail();
    } 
    
    this.setEmail = function(email){
           this.personVO.setEmail(email);
    }
    
    this.getExtravalues = function(){
           return this.personVO.getExtravalues();
    } 
        
    this.getId = function(){
    	this.id = this.personVO.getPersonId(); 
        return  this.id;
    } 
    
    this.setId = function(id){
           this.id = id;
           this.personVO.setPersonId(this.id);
    }
   
   
    this.getLoginname = function(){
           return this.personVO.getUserName();
    } 
    
    this.setLoginname = function(loginname){
           this.loginname = loginname;
           return this.personVO.setUserName(this.loginname);
    }
    
    this.getMandant = function(){
           return this.mandant;
    } 
    
    this.setMandant = function(mandant){
           this.mandant = mandant;
    }
    
    this.getPassword = function(){    	
           return this.personVO.getPassword();           
    } 
    
    this.setPassword = function(password){
    	this.password = password;	
        return this.personVO.setPassword(password);
    }
    
    this.getPin = function(){
    	   this.pin = this.personVO.getPin();
           return this.pin;
    } 
    
    this.setPin = function(pin){
           this.pin = pin;
           this.personVO.setPin(this.pin);
    }
    
    this.getXml = function(){
           var xml = this.personVO.getXml();
           return xml;
    } 
    /*
    this.setXML = function(xml){
           this.xml = xml;
           this.personVO.setXml(this.xml);
    }
    
    this.isActive = function(){
           return this.active;
    } 
    
    this.setActive = function(active){
           this.active = active;
           this.personVO.setActive(this.active);
    }
    
    this.isLmsuser = function(){
           this.lmsuser;
    } 
    */
    
    this.close = function(){
        this.smgService = null;
    }
    
    /*Alle Methoden aus der Klasse RegistryService*/
    /*
    this.activateUser = function(keycode){
    	this.keycode = keycode;
    	var success = false;
    	success = this.smgService.activateUser(this.personVO, this.keycode);
    	return success;
    }
    */
    this.addExtraValue = function(property, value){
    	try{	
    		this.personVO = this.smgService.addExtraValue(this.personVO, property, value);
    	} catch(e){
    		this.log.error("Fehler bei addExtraValue: "+e);
    	}
    	this.log.debug("Extravalue added: "+value);
    }
    
    this.addExtraValueToList = function(listName, value){
    	this.personVO = this.smgService.addExtraValueToList(this.personVO, listName, value);
    }
    
    this.changeExtraValue = function(property, value){
    	this.personVO = this.smgService.changeExtraValue(this.personVO, property, value);
    }
    
    this.changeExtraValueListItem = function(listName, oldValue, newValue){
    	this.personVO = this.smgService.changeExtraValueListItem(this.personVO, listName, oldValue, newValue);
    }
    
    this.getExtraValue = function(property){
    	var value = this.smgService.getExtraValue(this.personVO, property);
    	this.log.debug("Extravalue '"+property+"': "+value);
    	this.log.debug("this.personVO: "+this.personVO);
    	this.log.debug(this.smgService.getExtraValue(this.personVO, property));
    	return value;
    }
    
    this.getExtraValueList = function(listName){
    	var listName = new Object();
		listName[listName]= new Array();
		listName = this.smgService.getExtraValueList(this.personVO, listName);
		return listName;
    }
    
    this.removeAllExtraValues = function(){
    	this.personVO = this.smgService.removeAllExtraValues(this.personVO);
    }
    
    this.removeExtraValue = function(property){
    	this.personVO = this.smgService.removeExtraValue(this.personVO, property);
    }
    
    this.removeExtraValueList = function(listName){
    	this.personVO = this.smgService.removeExtraValueList(this.personVO, listName);
    }
    
    this.removeExtraValueListItem = function(listName, value){
    	this.personVO = this.smgService.removeExtraValueListItem(this.personVO, listName, value);
    }
    
    this.unregisterUserById = function(id){
    	this.id = id;
    	var success = false;
    	success = this.smgService.unregisterUserById(this.id);
    	return success;
    } 
    
    this.getPersonAsDOM = function(){
        
    	this.personDOM = this.smgService.getPersonAsDOM(this.personVO);
    	return this.personDOM;
    }
    
    this.setPersonFromDOM = function(personDOM){
    
        this.personDOM = personDOM;
        this.personVO = this.smgService.setPersonFromDOM(this.personDOM);
    	
    }
    
}