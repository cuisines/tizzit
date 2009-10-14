importPackage(java.lang);
importPackage(java.util);
importPackage(java.text);
importClass(Packages.org.tizzit.util.XercesHelper);
importClass(org.jaxen.dom.DOMXPath);

function getDocument() {
	return getDocument(false);
}

function getDocument(forceNew) {
	var doc;
	doc = cocoon.session.getAttribute("flow-document");
	if(doc == undefined || forceNew == true) {
		doc = Packages.javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
	    var result = doc.createElement("result");
	    doc.appendChild(result);
    }
    cocoon.session.setAttribute("flow-document", doc);
    return doc;
}

function saveDocument(form) {
	var doc = getDocument();
	form.save(doc);
	cocoon.session.setAttribute("flow-document", doc);
}

function nodes2string(nodeiterator) {
    var node = null;
    var result = "";
    while(nodeiterator.hasNext()) {
        result += XercesHelper.node2string(nodeiterator.next());
    }
    return result;
}


function setDocumentValue(name, value) {
    var doc = getDocument();
    var node = XercesHelper.findNode(doc.getFirstChild(), name);
    if(node != undefined && node.getFirstChild() != undefined) {
    	node.getFirstChild().setNodeValue(value);
    } else {
    	XercesHelper.createTextNode(doc.getFirstChild(), name, value);
    }
    cocoon.session.setAttribute("flow-document", doc);
}

function getDocumentValue(name) {
	var doc = getDocument();
	return XercesHelper.getNodeValue(doc, "//" + name);
}