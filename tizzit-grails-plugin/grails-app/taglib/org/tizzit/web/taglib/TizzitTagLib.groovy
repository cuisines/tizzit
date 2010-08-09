package org.tizzit.web.taglib

import javax.xml.transform.stream.StreamResult

import javax.xml.transform.TransformerFactory
import org.w3c.dom.Node
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerConfigurationException
import javax.xml.transform.Transformer
import javax.xml.transform.Result
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.Source
import org.dom4j.io.XMLWriter
import org.dom4j.io.OutputFormat
import org.dom4j.DocumentHelper
import org.dom4j.Element

class TizzitTagLib {
	def tizzitRestClientService
	def grailsApplication
	static namespace = "tizzit"

	def navigation = { attrs, body ->
		def depth = attrs.depth
		def since = attrs.since
		def template = (attrs.template) ? attrs.template : "navigation"
		def omitFirst = (attrs.omitFirst) ? attrs.omitFirst.toBoolean() : false
		def xml = tizzitRestClientService.navigationXml(depth, since, flash.tizzit.viewComponentId)
		if (omitFirst) {
			xml = xml.viewcomponent
		}
		try {
			out << render(template: "components/${template}", model: [navigation: xml, urlLinkName: "/$flash.tizzit.language"])
		} catch (e) {
			out << render(template: "components/${template}", model: [navigation: xml, urlLinkName: "/$flash.tizzit.language"], plugin: "tizzitWeb")
		}
	}

	def content = { attrs, body ->
		def node = (attrs.node) ? attrs.node : null
		def nodes = (attrs.nodes) ? attrs.nodes : null
		def omitFirst = (attrs.omitFirst) ? attrs.omitFirst.toBoolean() : false
		def template = (attrs.template) ? attrs.template : null
		def preparseModules = (attrs.preparseModules) ? attrs.preparseModules.toBoolean() : true

		def xmlDom = flash.tizzit.contentDom.clone()
		def xmlSlurp = flash.contentXml // TODO: Use slurp xml for xpath stuff
		/*GroovyShell gs = new GroovyShell()
		gs.xmlSlurp = xmlSlurp
		gs.evaluate("")*/

		if (node) {
			xmlDom = xmlDom.selectSingleNode(node)
		} else if (nodes) {
			log.debug "select multiple nodes $nodes"
			xmlDom = xmlDom.selectNodes(nodes)
		}

		if (preparseModules) {
			log.info "preparseModules for node $node nodes $nodes"
			xmlDom.selectNodes("//" + grailsApplication.config.tizzit.modules.list.join(' | //')).each { Element n ->
				def xp = new XmlParser().parseText(n.asXML())
				def i
				try {
					i = render(template: "modules/${n.name}", model: [node: xp], contentType: 'text/xmlDom')
				} catch (e) {
					i = render(template: "modules/${n.name}", model: [node: xp], contentType: 'text/xmlDom', plugin: "tizzitWeb")
				}
				def da = DocumentHelper.parseText(i.toString())
				replaceNode(n, da)
			}
		}

		if (template) {
			def model = [:]
			if (nodes) {
				model.nodes = xmlDom
			} else {
				def xp = new XmlParser().parseText(xmlDom.asXML())
				model.node = xp
			}
			try {
				out << render(template: "components/${template}", model: model, contentType: 'text/xmlDom')
			} catch (e) {
				out << render(template: "components/${template}", model: model, contentType: 'text/xmlDom', plugin: "tizzitWeb")
			}
		} else {
			out << getStringFromXml(xmlDom, omitFirst)
		}
	}

	def getStringFromXml(xml, omitFirst = false) {
		StringWriter sw = new StringWriter()
		OutputFormat format = OutputFormat.createCompactFormat()
		format.setXHTML true
		format.setSuppressDeclaration(true)
		def writer = new XMLWriter(sw, format)
		writer.write(xml)
		def s = sw.toString()

		if (omitFirst) {
			def r = s =~ /<\w+((\s+\w+(\s*=\s*(?:".*?"|'.*?'|[^'">\s]+))?)+\s*|\s*)>(.*)<\/\w+>$/
			s = r[0].last()
		}
		return s
	}

	def replaceNode(source, dest) {
		List contentOfParent = source.getParent().content();
		int index = contentOfParent.indexOf(source);
		contentOfParent.set(index, dest);
	}

}
