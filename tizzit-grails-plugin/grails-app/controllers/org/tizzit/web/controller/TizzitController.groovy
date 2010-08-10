package org.tizzit.web.controller

import org.dom4j.DocumentHelper
import groovy.xml.XmlUtil
import groovy.xml.StreamingMarkupBuilder

class TizzitController {
	def beforeInterceptor = [action: this.&tizzitAction]
	def tizzitRestClientService

	def tizzitAction() {
		log.debug "starting tizzitAction"

		flash.template = (params.template) ? params.template : "standard"

		if (!flash.tizzit) {
			flash.tizzit = new Expando()
			log.debug "fetching content from REST service"

			def resp = tizzitRestClientService.actionData("www.hsg-wennigsen-gehrden.de", params.tizzituri, params.safeguardUsername, params.safeguardPassword)
			def xml = new XmlSlurper().parseText(resp)
			flash.tizzit.viewComponentId = xml.params.viewComponentId
			flash.tizzit.template = xml.params.template ?: "standard"
			flash.tizzit.safeguard = xml.params.safeguard
			flash.tizzit.path = xml.params.path
			flash.tizzit.login = xml.params.login
			flash.tizzit.redirect = xml.params.redirect
			flash.tizzit.language = xml.params.language
			flash.tizzit.uri = params.tizzituri
			flash.tizzit.contentDom = DocumentHelper.parseText(resp)
			//flash.contentXml = new XmlSlurper().parseText(xml.content)
		}
	}

	def index = {
		log.debug "starting index"

		def fullUri = grailsAttributes.getTemplateUri("templates/$flash.tizzit.template", request)
		def resource = grailsAttributes.pagesTemplateEngine.getResourceForUri(fullUri)

		if (resource && resource.file && resource.exists()) {
			log.debug "gathered local template $flash.tizzit.template"
			render(template: "templates/$flash.tizzit.template")
		} else {
			log.debug "gathered plugin default template $flash.tizzit.template"
			render(template: "templates/$flash.tizzit.template", plugin: "tizzitWeb")
		}

		log.debug "finished index"
	}
}

/*
		sitemapParams.put(HostSelectorAction.HOST, host);

		// first check for some redirects for this host
		String redirectUrl = this.webServiceSpring.resolveRedirect(host, requestPath, new HashSet<String>());
		if (redirectUrl != null && !"".equalsIgnoreCase(redirectUrl)) {
			if (log.isDebugEnabled()) log.debug("found redirectUrl: " + redirectUrl);
			sitemapParams.put(HostSelectorAction.REDIRECT_URL, redirectUrl);
			return sitemapParams;
		}
		sitemapParams.put(HostSelectorAction.REDIRECT_URL, "0");

		String startPageUrl = this.webServiceSpring.getStartPage(host);
		if ("".equalsIgnoreCase(startPageUrl)) {
			startPageUrl = "0";
		}

		siteId = new Integer(par.getParameter("siteId"));
		String path = par.getParameter("path");
		String language = par.getParameter("language");
		String viewType = par.getParameter("viewType");
		Integer viewComponentId = null;
		viewComponentId = new Integer(par.getParameter("viewComponentId"));

*/