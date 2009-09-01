
function testshow() {                           
	importClass(java.net.URLDecoder);
	importClass(java.net.URLEncoder);

	var log = Packages.org.apache.log4j.Logger.getLogger("testshow");
	log.warn("start form");

	//Store Requeststring
	var reqStr = cocoon.parameters["requestURI"];
    //filetype "*.cleanxml" muss in der Sitemap gemached werden und ohne cicnlude des Formulars ausgefuehrt werden,     
    //damit die Parameter eingelesen werden koennen, ohne dass das Script erneut asgefuehrt wird (sonst Endlosschleife).
        
	reqStr = "http://"+cocoon.parameters["serverName"]+reqStr.substring(0,reqStr.lastIndexOf("/")+1)+"content.cleanxml";
	log.warn("req-str: "+reqStr);
	
	/*erwartete Parameter: 
	|
	|____
							to
							bcc
							cc
							from
							subject
							
							formUri
														_________
																							|
																							|
	
	*/
	
	//Get XML-Document of given URL as DOM	
	var document = loadDocument(reqStr);
	var params   = document.getElementsByTagName("item");	
	paramsLength = params.getLength();
	log.warn("paramsLength ="+paramsLength);	
	for (i=0;i<paramsLength;i++){
	    paramName = params.item(i).getElementsByTagName("param-name").item(0).getChildNodes().item(0).getNodeValue();
        log.warn("paramName ="+paramName);
	    paramValue =params.item(i).getElementsByTagName("param-value").item(0).getChildNodes().item(0).getNodeValue();	    
        log.warn("paramValue ="+paramValue);
        eval("var "+paramName+" = paramValue");
	}

	//Pfad zum Form Model und Form Template
	modelPath = "http://"+cocoon.parameters["serverName"]+"/deutsch/"+formUri+"/formmodel.xml";	
	log.warn("modelPath ="+modelPath);	

//fuer das formtemplate verwendet cocoon immer automatisch das schema "cocoon:/", ein anderes ist nicht erlaubt	
	templatePath = "deutsch/"+formUri+"-conQuestForm";	
	log.warn("templatePath ="+templatePath);	

//generiertes Formmodel holen	
	var form = new Form(modelPath);
	log.warn("showForm");
	
//generiertes Formtemplate holen und Form zeigen	
	setUtf();	
	form.showForm(templatePath);

}
