package org.tizzit.web.controller

class TizzitController {

	def index = {
		log.debug "starting index"
		def template = (params.template) ? params.template : "standard"

		def fullUri = grailsAttributes.getTemplateUri("templates/$template", request)
		def resource = grailsAttributes.pagesTemplateEngine.getResourceForUri(fullUri)

		if (resource && resource.file && resource.exists()) {
			log.debug "gathered local template"
			render(template: "templates/$template")
		} else {
			log.debug "gathered plugin default template"
			render(template: "templates/$template", plugin: "tizzitWeb")
		}
		log.debug "finished index"
	}
}
