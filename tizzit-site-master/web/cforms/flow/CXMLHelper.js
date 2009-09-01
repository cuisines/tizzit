/* CXMLHelper */
function CXMLHelper(logname) {
	this.log = Packages.org.apache.log4j.Logger.getLogger(logname);
	this.helper = Packages.de.juwimm.util.XercesHelper;
	
	this.loadDocumentFromUri = function(uri) {
		if (this.log.isDebugEnabled()) {
			this.log.debug("resolving document from uri "+ uri);
		}
	
		try {
			var resolver = cocoon.getComponent( "org.apache.excalibur.source.SourceResolver");
			var source = resolver.resolveURI(uri);
			var document = Packages.org.apache.cocoon.components.source.SourceUtil.toDOM(source);
			resolver.release(source);
			cocoon.releaseComponent(resolver);
		} catch (e) {
			this.log.error("Error resolving document from uri "+ uri);
			this.log.error("Errormessage resolving document "+ e);
			document = null;
		}
		
		return document;
	}

	this.findNode = function(node, path) {
		return this.helper.findNode(node, path);
	}
	
	this.findNodes = function(node, path) {
		return this.helper.findNodes(node, path);
	}
	
	this.getNodeValue = function(node, path) {
		return this.helper.getNodeValue(node, path);
	}

	this.documentToString = function(document) {
		return this.helper.doc2String(document);
	}

	this.nodeToString = function(node) {
		return this.helper.node2string(node);
	}
}