importClass(java.lang.String);

cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");
cocoon.load("../../../tizzit-site-master/web/cforms/flow/pollJars.js");

function cmsratingXml(logprefix, ratingName, site, user, xmlStr_s){
    //logger
    var log = Packages.org.apache.log4j.Logger.getLogger(logprefix+".cmsrating_xml");
    
    var success = false;
    //CformHelper
    var cfh = new Packages.de.juwimm.cms.cocoon.CformHelper("poll", pollJars);
    try {
        var utl = cfh.instanciateClass("de.juwimm.poll.remote.PollServiceUtil"); 
    	var pollService = utl.getHome().create();
        var pollVO = cfh.instanciateClass("de.juwimm.poll.vo.PollValue");
        
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
	} finally {
	    cfh.retireBorderline();
	}
	return success;
}




