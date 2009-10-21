importClass(java.lang.String);

cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");

cocoon.load("../../../tizzit-site-master/web/cforms/flow/CHashMap.js");
cocoon.load("../../../tizzit-site-master/web/cforms/flow/CRequestParamsBuilder.js");
cocoon.load("../../../tizzit-site-master/web/cforms/flow/CRequestParamsToXMLString.js");

cocoon.load("../../../tizzit-site-master/web/cforms/flow/pollJars.js");



function cmsrating(logprefix, ratingName, site, user){

    var log = Packages.org.apache.log4j.Logger.getLogger(logprefix+".cmsrating");
    
    var requestParams = (new CRequestParamsBuilder(logprefix+".CRequestParamsBuilder")).getRequestParams(cocoon);
  
   //user ggfs. auf rootnodeName setzen
   if(user=="rootNodeName"){
       user = requestParams.get('root');
   }
   
	//getting xml string from request parameter
    var rpx = new CRequestParamsToXMLString(logprefix+".CRequestParamsToXMLString");
    if (requestParams.get('root')!= null){
        rpx.setRootNodeName(requestParams.get('root'));
    }
    if (requestParams.get('child')!= null){
        rpx.setFirstChildNode(requestParams.get('child'));
    }
    var xmlStr_s = rpx.getXMLString(cocoon);
        
    if (log.isDebugEnabled()) {
		log.debug("start form - rating of newsletter article");
	    log.debug("requestParams\n"+requestParams.toString());
	    log.debug("request XML String xmlStr_s \n"+xmlStr_s);
	}
    
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
            
            //TODO: Hier DB post    	    	
 	} catch (e) {
	    log.error("Konnte poll nicht in DB speichern: "+e);
	}
}




