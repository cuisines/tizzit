package org.tizzit.web.service

import groovyx.net.http.HTTPBuilder
import groovy.xml.XmlUtil

class TizzitRestClientService {

	static transactional = true

	def navigationXml(depth, since) {
		def xml
		withHttp(uri: "http://localhost:8080") {
			def resp = get(path: "/remote/navigationxml/10086/$since/$depth/false")
			resp = "<root>$resp</root>"
			xml = new XmlParser().parseText(resp)
		}
		return xml
	}

	def contentText() {
		def xml
		withHttp(uri: "http://localhost:8080") {
			def resp = get(path: '/remote/content/10086/false')
			xml = new XmlParser().parse(resp)
			xml = XmlUtil.serialize(xml)
		}
		return xml
	}
}