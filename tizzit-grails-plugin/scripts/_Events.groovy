
eventCreateWarStart = {warname, stagingDir ->
	def grailsHome = Ant.project.properties."environment.GRAILS_HOME"    
	println "Created $warname in $stagingDir $grailsHome"
	Ant.propertyfile(file:"${stagingDir}/WEB-INF/classes/application.properties") {
		entry(key: 'build.date', value: new Date())
	}
}
 