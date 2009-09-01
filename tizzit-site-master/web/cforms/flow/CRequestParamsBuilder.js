/* CRequestParamsBuilder */
function CRequestParamsBuilder(logname) {
	this.log = Packages.org.apache.log4j.Logger.getLogger(logname);
	
	this.getRequestParams = function(cocoonObj) {
		var result = new CHashMap();
		var key = "";
		
		for(var i in cocoonObj.parameters) {
			result.add(i.toString(), cocoonObj.parameters[i]);
			if (this.log.isDebugEnabled()){
			    this.log.debug(i.toString() + "=" +cocoonObj.parameters[i]);
			}
		}
		
		var it = cocoonObj.request.getParameterNames();
		while(it.hasMoreElements()) {
			key = it.nextElement().toString();
			result.add(key, cocoonObj.request.getParameter(key));
			if (this.log.isDebugEnabled())
				this.log.debug(key+"="+cocoonObj.request.getParameter(key));
				this.log.debug("key: "+key);
		}
		
		return result;
	}	
}