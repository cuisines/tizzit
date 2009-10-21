importClass(java.lang.String);

cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");
cocoon.load("../../../tizzit-site-master/web/cforms/flow/pollJars.js");

function cmsratingXml(logprefix, ratingName, site, user, xmlStr_s){
    //logger
    var log = Packages.org.apache.log4j.Logger.getLogger(logprefix+".cmsrating_xml");
    
    var success = false;
    var cfh = new Packages.org.tizzit.core.classloading.ClassloadingHelper();
    try {
        var utl = cfh.getInstance("de.juwimm.poll.remote.PollServiceUtil"); 
    	var pollService = utl.getHome().create();
        var pollVO = cfh.getInstance("de.juwimm.poll.vo.PollValue");
        
        pollVO.setXmldata(xmlStr_s);
        pollVO.setDatum(new java.util.Date());
        pollVO.setUsername(user);
        pollVO.setName(ratingName);
        pollVO.setSite(site);
        
        var id = pollService.createPoll(pollVO);
        log.debug("Poll gespeichert mit id: "+id);
        success = true;        	    	
 	} catch (e) {
	    log.error("Konnte poll nicht in DB speichern: "+e);
	}
	return success;
}




