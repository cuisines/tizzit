/* CPoll */
importClass(java.util.Date);
cocoon.load("../../../tizzit-site-master/web/cforms/flow/pollJars.js");

function CPoll(logprefix) {

    var logname = logprefix+".CPoll";
	this.log = Packages.org.apache.log4j.Logger.getLogger(logname);
    cocoon.load("../../../tizzit-site-master/web/cforms/flow/pollJars.js");

    this.createPollVO = function(){
      this.log.debug("createPollVO");
      var cfh = new Packages.org.tizzit.core.classloading.ClassloadingHelper();  
      var pollVO = cfh.getInstance("de.juwimm.poll.vo.PollValue");
      pollVO.setDatum(new java.util.Date());
	  return pollVO;
    }   
    
    this.pollVO = this.createPollVO();        
    this.polls_col = null;
    
    
    //error und response messages
    this.msg = "";
    this.error = "";
    this.code = 0;
    this.returnValue = null;
    this.success = false;    
    
    this.getStatus = function(){
        var status = {msg: this.msg, error: this.error, code: this.code, returnValue: this.returnValue, success: this.success};
        return status;
    }
    
    this.getPollVO = function(){
    	this.log.debug("getPollVO");
        return this.pollVO;    
    }        
	this.setId = function(id) {
		this.pollVO.setId(id);
	}
	this.setUsername = function(username) {
		this.pollVO.setUsername(username);
	}
	this.setXmldata = function(xmlStr_s) {
		this.pollVO.setXmldata(xmlStr_s);
	}
	this.setSite = function(site) {
		this.pollVO.setSite(site);
	}
	this.setDatum = function(datum) {
		this.pollVO.setDatum(datum);
	}
	this.setFreshDatum = function() {
		this.pollVO.setDatum(new java.util.Date());
	}
	this.setName = function(name) {
		this.pollVO.setName(name);
	}
	this.getId = function() {
		return this.pollVO.getId();
	}
	this.getUsername = function() {
		return this.pollVO.getUsername();
	}
	this.getXmldata = function() {
		return this.pollVO.getXmldata();
	}
	this.getSite = function() {
		return this.pollVO.getSite();
	}    
	this.getDatum = function() {
		return this.pollVO.getDatum();
	}    
	this.getName = function() {
		return this.pollVO.getName();
	}
    
   this.getPollByIdAndUpdateXmldata = function(id, xmldata){   
        log.debug("getPollByIdAndUpdateXmldata");
        var id_int = parseInt(id);
        log.debug("id_int: "+id_int);
         var cfh = new Packages.org.tizzit.core.classloading.ClassloadingHelper();
            try {
                var utl = cfh.getInstance("de.juwimm.poll.remote.PollServiceUtil"); 
                var pollService = utl.getHome().create();
                this.pollVO = pollService.getPollById(id_int);                
                log.debug("poll "+this.pollVO);
                this.pollVO.setXmldata(xmldata);                
                this.success = pollService.updatePoll(this.pollVO);
                this.returnValue = this.pollVO.getId();               
                this.error = "";           	    	
             } catch (e) {
                log.error("Konnte poll nicht aus DB holen. Ist die ID gesetzt?\n"+e);
                log.error("angefragte poll id: "+id_int);
                this.success = false;
                this.error = e;
            } 
            return this.success;    
    } 
    
    
    //einzlenen Poll nach id holen 
    this.getPollById = function(id){
        log.debug("getPollById");
        var id_int = parseInt(id);
        log.debug("id_int: "+id_int);
         var cfh = new Packages.org.tizzit.core.classloading.ClassloadingHelper();
            try {
                var utl = cfh.getInstance("de.juwimm.poll.remote.PollServiceUtil"); 
                var pollService = utl.getHome().create();
                this.pollVO = pollService.getPollById(id_int);                
                log.debug("poll "+this.pollVO);
                this.success = true; 
               // this.returnValue = this.pollVO.getId();               
                this.error = "";           	    	
             } catch (e) {
                log.error("Konnte poll nicht aus DB holen. Ist die ID gesetzt?\n"+e);
                log.error("angefragte poll id: "+id_int);
                this.success = false;
                this.error = e;
            } 
            return this.success;    
    }
    
    //einzlenen Poll nach Site, Namen und User holen 
    this.getPollBySiteNameUser = function(site, name, user){
		if(log.isDebugEnabled()) {
	    	log.debug("getPollsBySiteNameUser");
	    	log.debug("site: "+site);
	    	log.debug("name: "+name);
	    	log.debug("user: "+user);
	    	log.debug(""); //spacer
		} 
	    var cfh = new Packages.org.tizzit.core.classloading.ClassloadingHelper();
	    try {
	        var utl = cfh.getInstance("de.juwimm.poll.remote.PollServiceUtil"); 
	        var pollService = utl.getHome().create();
	        if(pollService.getPollsBySiteNameUser(site, name, user).iterator().hasNext()) {
	        	this.pollVO = pollService.getPollsBySiteNameUser(site, name, user).iterator().next();
	        }
		 	if(log.isDebugEnabled()) {           
        		log.debug("poll "+this.pollVO.getName());
	        }
	        this.success = true; 
	       // this.returnValue = this.pollVO.getId();               
	        this.error = "";           	    	
	     } catch (e) {
	        log.error("Konnte poll nicht aus DB holen. Ist die Site, der Name und der User gesetzt?\n"+e);
	        log.error("angefragte site: "+site);
	        log.error("angefragter name: "+name);
	        log.error("angefragter user: "+user);
	        this.success = false;
	        this.error = e;
	    } 
     	return this.success;    
    }
    
    //bestehenden poll aendern
     this.update = function(){    
        log.debug("update");
            try {
                var cfh = new Packages.org.tizzit.core.classloading.ClassloadingHelper();
                if (this.pollVO.getId == null || this.pollVO.getId == "" ){
                    throw("Keine ID f�r pollVO gesetzt. Es kann kein Poll upgedated werden");
                }  
                var utl = cfh.getInstance("de.juwimm.poll.remote.PollServiceUtil"); 
                var pollService = utl.getHome().create();
                var pollVO = cfh.getInstance("de.juwimm.poll.vo.PollValue");
                pollVO.setId(this.getId());
                pollVO.setName(this.getName());
                pollVO.setUsername(this.getUsername());
                pollVO.setDatum(this.getDatum());
                pollVO.setXmldata(this.getXmldata());
                pollVO.setSite(this.getSite());
                this.success = pollService.updatePoll(pollVO);
                if (this.success==false) throw ("updatePoll fehl geschlagen");
                this.error = "";                           	    	
             } catch (e) {
                log.error("Konnte poll nicht aus DB holen. Ist die ID gesetzt?\n"+e);
                log.error("angefragte poll id: "+this.id);
                this.success = false;
                this.error = e;
            } 
            return this.success;
    }
    
    //neuen poll speichern
    this.createPoll = function(){
           log.debug("createPoll");
           try {              
                var cfh = new Packages.org.tizzit.core.classloading.ClassloadingHelper();     
                var utl = cfh.getInstance("de.juwimm.poll.remote.PollServiceUtil"); 
                var pollService = utl.getHome().create();
                var pollVO = cfh.getInstance("de.juwimm.poll.vo.PollValue");
                pollVO.setId(this.getId());
                pollVO.setName(this.getName());
                pollVO.setUsername(this.getUsername());
                pollVO.setDatum(this.getDatum());
                pollVO.setXmldata(this.getXmldata());
                pollVO.setSite(this.getSite());
                this.pollVO = pollVO;
                this.returnValue = pollService.createPoll(pollVO);
                this.error = "";
                this.success = true;                           	    	
             } catch (e) {
                log.error("Konnte poll nicht aus DB holen. Ist die ID gesetzt?");
                log.error("angefragte poll id: "+this.id);
                this.success = false;
                this.error = e;
            } 
            return this.success;
    }   
    
    this.toString = function(){
        var result = "\n";
        result += "Site: "+this.getSite()+"\n";
        result += "Datum: "+this.getDatum()+"\n";
        result += "xmldata: "+this.getXmldata()+"\n";
        result += "name: "+this.getName()+"\n";
        result += "id: "+this.getId()+"\n";
        result += "username: "+this.getUsername()+"\n";
        result += "typeof: "+typeof(this.pollVO)+"\n";
        return result;
    }
    
    //TODO: alle getPolls... Methoden kapseln, die eine collection zurueckgeben 
}




