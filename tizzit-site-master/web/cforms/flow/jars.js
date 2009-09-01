
function getJars(key) {
	var jars = new Object();
	jars["juwimm-registry"]= new Array(
								"juwimm-registry-remote-1.3.jar",
								"commons-beanutils-1.7.0.jar",
								"juwimm-registry-cocoon-1.3.jar"
							 );
	jars["juwimm-smg"]= new Array(
								"juwimm-smg-cocoon-1.2.jar",
								"juwimm-smg-common-1.2.jar",
								"juwimm-smg-core-1.2-client.jar"
							 );
	
	return jars[key];
}

