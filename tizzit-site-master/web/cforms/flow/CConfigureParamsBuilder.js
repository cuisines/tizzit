/*CConfigureParamsBuilder*/
function CConfigureParamsBuilder(logname) {
	this.logprefix = logname;
	this.log = Packages.org.apache.log4j.Logger.getLogger(logname);
	
	this.getConfiguration = function(uri) {
		var result = new CHashMap();
		if (this.log.isDebugEnabled()) {
			this.log.debug("configRequestURI="+uri);
		}
		var aXMLHelper = new CXMLHelper(this.logprefix+".aXMLHelper");
		var document = aXMLHelper.loadDocumentFromUri(uri);
		if (document != null) {
			var params = document.getElementsByTagName("item");
			for (var i=0;i<params.getLength();i++){
				result.add(aXMLHelper.getNodeValue(params.item(i), "param-name").toString(), aXMLHelper.getNodeValue(params.item(i), "param-value").toString());
			}
			if (this.log.isDebugEnabled()) {
				this.log.debug("configParams retrieved from ConQuest\n"+result.toString());
			}
		} else {
			this.log.error("Error resolving configuration from uri "+uri);
			result = null;
		}

		return result;
	}
}
