cocoon.load("../../../tizzit-site-master/web/cforms/flow/save_xml_single.js");
cocoon.load("../../../tizzit-site-master/web/cforms/flow/CHashMap.js");
cocoon.load("../../../tizzit-site-master/web/cforms/flow/CRequestParamsBuilder.js");

/*
Diese Funktion kann alle Werte des poll service als values entgegennehmen und als poll speichern.
Wenn bestimmte Werte nicht gesetzt werden, so werden default Werte verwendet.
*/


function pollSingle(logprefix){
    var log = Packages.org.apache.log4j.Logger.getLogger(logprefix+".cmspoll");
    //default values  
    var pollParams = new Array();
    pollParams.ratingName = "poll";
    pollParams.site = "pw-con";
    pollParams.user = "pw-con";
    pollParams.xmlStr_s = "<empty/>";    
    
    //request parameter lesen 
    var requestParams = (new CRequestParamsBuilder(logprefix+".CRequestParamsBuilder")).getRequestParams(cocoon);
  
   //default values ueberschreiben, wenn sie als request param uebergeben wurden
   for (var i in pollParams){   
       if(requestParams.get(i)!=null){
           pollParams[i] = requestParams.get(i);
       }
    }
    //poll in DB schreiben mit pollservice
    var success = saveXmlSingle(logprefix, pollParams.ratingName, pollParams.site, pollParams.user, pollParams.xmlStr_s);
    
    return success;    
}

