function getJars(key) {
	var jars = new Object();
	jars["juwimm-registry"]= new Array(
		"juwimm-registry-remote-1.3.jar",
		"commons-beanutils-1.7.0.jar",
		"juwimm-registry-cocoon-1.3.jar"
	 );
	
	return jars[key];
}



