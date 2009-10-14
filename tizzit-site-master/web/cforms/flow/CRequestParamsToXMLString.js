/* CRequestParamsToXMLString */

	

function CRequestParamsToXMLString(logname) {

	this.log = Packages.org.apache.log4j.Logger.getLogger(logname);
	this.helper = Packages.org.tizzit.util.XercesHelper;	
	this.rootNodeName = "root";
	//der firstChildNode ist ein optionales, kapselndes Element unter dem root und kann zur Differenzierung gesetzt werden 	
	this.firstChildNode = "";
	
	this.setRootNodeName = function(rootNodeName){
	    this.rootNodeName = rootNodeName;
	}
	this.setFirstChildNode = function(firstChildNode){
	    this.firstChildNode = firstChildNode;
	}
	
	this.getXMLString = function(cocoonObj) {
	    //leeres dom document holen
        var doc = this.helper.getNewDocument();
        //root Element anlegen
        var elm = doc.createElement(this.rootNodeName);
        doc.appendChild(elm);
        
        //ggf. zweites kapselndes Element        
        if (this.firstChildNode != "") {
		    elm = this.helper.createTextNode(elm,this.firstChildNode, "");
		}		
		//alle Parameter als Kindelemente anlegen
  		var key = "";
		var it = cocoonObj.request.getParameterNames();
		while(it.hasMoreElements()) {
			key = it.nextElement().toString();
			//"root" und "child" ueberspringen, da sie schon als Elementnamen geschrieben werden
		    if ((key=="root") || (key=="child")  || (key=="x")  || (key=="y")) continue;
		    this.helper.createTextNode(elm,key,cocoonObj.request.getParameter(key));
			if (this.log.isDebugEnabled()){
				this.log.debug(key+" = "+cocoonObj.request.getParameter(key));
				this.log.debug("key and nodename: "+key);
			}
		}		
		//dom als string
		var doc_str = this.helper.node2string(doc.getDocumentElement());
		log.debug("doc_str: \n "+doc_str);
		return doc_str;
	}	
}