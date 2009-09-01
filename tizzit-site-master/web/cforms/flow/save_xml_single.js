importClass(java.lang.String);

cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");
cocoon.load("../../../tizzit-site-master/web/cforms/flow/pollJars.js");

/*
Diese Funktion speichert ueber den poll service einen einzigen XML Eintrag 
zu einem bestimmten user,  name und site.
Existiert breits ein Eintrag für dieselben Parameter, so wird dieser ueberschrieben.
*/

function saveXmlSingle(logprefix, ratingName, site, user, xmlStr_s){
    //logger
    var log = Packages.org.apache.log4j.Logger.getLogger(logprefix+".saveXmlSingle");
    
    var success = false;
    //CformHelper
    var cfh = new Packages.de.juwimm.cms.cocoon.CformHelper("poll", pollJars);
    try {
        var utl = cfh.instanciateClass("de.juwimm.poll.remote.PollServiceUtil"); 
    	var pollService = utl.getHome().create();
        var pollVO = null;
        
        var polls = pollService.getPollsBySiteNameUser(site, ratingName, user);
        log.debug("polls "+polls);
        var polls_it = polls.iterator();
        log.debug("polls_it "+polls_it);
        //es existiert schon ein Eintrag mit diesen Parametern -> ueberschreiben
        if (polls_it.hasNext()){
            pollVO = polls_it.next();
            pollVO.setXmldata(xmlStr_s);
            pollVO.setDatum(new java.util.Date());
            pollVO.setUsername(user);
            pollVO.setName(ratingName);
            pollVO.setSite(site);            
            pollService.updatePoll(pollVO);
            log.debug("existing poll updated");
        }else{
        //es existiert noch kein Eintrag mit diesen Parametern -> neu anlegen
            pollVO = cfh.instanciateClass("de.juwimm.poll.vo.PollValue");            
            pollVO.setXmldata(xmlStr_s);
            pollVO.setDatum(new java.util.Date());
            pollVO.setUsername(user);
            pollVO.setName(ratingName);
            pollVO.setSite(site);            
            var id = pollService.createPoll(pollVO);
            log.debug("new poll saved with id: "+id);
        }
        success = true;        	    	
 	} catch (e) {
	    log.error("Konnte poll nicht in DB speichern: "+e);
	} finally {
	    cfh.retireBorderline();
	}
	return success;
}




