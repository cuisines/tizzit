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
import org.dom4j.tree.DefaultElement

class TizzitTagLib {
	def tizzitRestClientService
	def grailsApplication
	static namespace = "tizzit"

	def navigation = { attrs, body ->
		def depth = attrs.depth
		def since = attrs.since
		def template = (attrs.template) ? attrs.template : "navigation"
		def omitFirst = (attrs.omitFirst) ? attrs.omitFirst.toBoolean() : false
		def xml = tizzitRestClientService.navigationXml(depth, since)
		if (omitFirst) {
			xml = xml.viewcomponent
		}
		try {
			out << render(template: "components/${template}", model: [navigation: xml, urlLinkName: "/de"])
		} catch (e) {
			out << render(template: "components/${template}", model: [navigation: xml, urlLinkName: "/de"], plugin: "tizzitWeb")
		}
	}

	public static String xmlToString(Node node) {
		try {
			Source source = new DOMSource(node);
			StringWriter stringWriter = new StringWriter();
			Result result = new StreamResult(stringWriter);
			TransformerFactory factory = TransformerFactory.newInstance();

			Transformer transformer = factory.newTransformer();
			transformer.transform(source, result);
			return stringWriter.getBuffer().toString();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	def content = { attrs, body ->
		DefaultElement.metaClass.methodMissing = {
			String name, def args ->
			println name
		}

		def node = (attrs.node) ? attrs.node : null
		def nodes = (attrs.nodes) ? attrs.nodes : null
		def omitFirst = (attrs.omitFirst) ? attrs.omitFirst.toBoolean() : false
		def template = (attrs.template) ? attrs.template : null
		def preparseModules = (attrs.preparseModules) ? attrs.preparseModules.toBoolean() : true

		def xml
		if (flash.content) {
			xml = flash.content.clone()
		} else {
			log.debug "fetching content from REST service"
			def resp = tizzitRestClientService.contentText()
			xml = DocumentHelper.parseText(resp)
			flash.content = xml.clone()
		}

		if (preparseModules) {
			log.info "preparseModules for node $node nodes $nodes"
			xml.selectNodes("//" + grailsApplication.config.tizzit.modules.list.join(' | //')).each { Element n ->
				def xp = new XmlParser().parseText(n.asXML())
				def i
				try {
					i = render(template: "modules/${n.name}", model: [node: xp], contentType: 'text/xml')
				} catch (e) {
					i = render(template: "modules/${n.name}", model: [node: xp], contentType: 'text/xml', plugin: "tizzitWeb")
				}
				def da = DocumentHelper.parseText(i.toString())
				replaceNode(n, da)
			}
		}

		if (node) {
			xml = xml.selectSingleNode(node)
		} else if (nodes) {
			log.debug "select multiple nodes $nodes"
			xml = xml.selectNodes(nodes)
		}

		if (template) {
			def model = [:]
			if (nodes) {
				model.nodes = xml
			} else {
				def xp = new XmlParser().parseText(xml.asXML())
				model.node = xp
			}

			try {
				out << render(template: "components/${template}", model: model, contentType: 'text/xml')
			} catch (e) {
				out << render(template: "components/${template}", model: model, contentType: 'text/xml', plugin: "tizzitWeb")
			}
		} else {
			out << getStringFromXml(xml, omitFirst)
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
