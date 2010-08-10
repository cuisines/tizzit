package org.tizzit.web.controller

import org.dom4j.DocumentHelper
import groovy.xml.XmlUtil
import groovy.xml.StreamingMarkupBuilder
import grails.converters.XML

class TizzitController {
	 //def beforeInterceptor = [action: this.&tizzitAction]
	def tizzitRestClientService

	def tizzitAction() {
		log.debug "starting tizzitAction"

		if (!params.tizzit) {
			params.tizzit = new Expando()
			log.debug "fetching content from REST service"
//http://localhost:8080/remote/action?host=www.hsg-wennigsen-gehrden.de&requestPath=de/UnsereMannschaften&safeguardUsername=null&safeguardPassword=null
			def resp = tizzitRestClientService.actionData("www.hsg-wennigsen-gehrden.de", params.tizzituri, params.safeguardUsername, params.safeguardPassword)
			def xml = new XmlSlurper().parseText(resp)
			params.tizzit.viewComponentId = xml.params.viewComponentId
			params.tizzit.template = xml.params.template ?: "standard"
			params.tizzit.safeguard = xml.params.safeguard
			params.tizzit.path = xml.params.path
			params.tizzit.login = xml.params.login
			params.tizzit.redirect = xml.params.redirect
			params.tizzit.language = xml.params.language
			params.tizzit.isLiveserver = xml.params.hostIsLiveserver
			params.tizzit.uri = params.tizzituri
			params.tizzit.contentDom = DocumentHelper.parseText(resp)
			params.tizzit.contentRaw = resp                              // todo flash scope!
			//flash.contentXml = new XmlSlurper().parseText(xml.content)
		}
	}

	def index = {
		log.debug "starting index"
		tizzitAction()
		def renderType = (params.renderType) ? params.renderType : "html"

		if (renderType == "html") {
			def fullUri = grailsAttributes.getTemplateUri("templates/$params.tizzit.template", request)
			def resource = grailsAttributes.pagesTemplateEngine.getResourceForUri(fullUri)

			if (resource && resource.file && resource.exists()) {
				log.debug "gathered local template $params.tizzit.template"
				render(template: "templates/$params.tizzit.template")
			} else {
				log.debug "gathered plugin default template $params.tizzit.template"
				render(template: "templates/$params.tizzit.template", plugin: "tizzitWeb")
			}
		} else if (renderType == "xml") {
			render(text: params.tizzit.contentRaw, contentType:"text/xml", encoding:"UTF-8")
		}
		log.debug "finished index"
	}


	def picture = {
		log.debug "starting picture"
		//http://localhost:8080/remote/picture/10153
		response.setHeader "Content-Disposition", "inline"
		response.setContentType "image/jpg"
		OutputStream out = response.getOutputStream()
		out <<  tizzitRestClientService.picture(params.id)
		out.close()
		out.flush()
	}

	def favicon = {
		render ""
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