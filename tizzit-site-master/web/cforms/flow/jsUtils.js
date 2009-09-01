// Create a new document, given the name of its root element

importClass(org.apache.xpath.XPathAPI);   
importClass(javax.xml.parsers.DocumentBuilderFactory);   
importClass(org.w3c.dom.Node);
importClass(org.w3c.dom.Element);
importClass(org.w3c.dom.NodeList);
importClass(org.apache.commons.httpclient.HttpClient);
importClass(org.apache.commons.httpclient.methods.PostMethod);

importClass(java.io.ByteArrayInputStream);
importClass(java.io.IOException);
importClass(java.io.InputStream);
importClass(java.io.UnsupportedEncodingException);

importClass(javax.xml.parsers.DocumentBuilder);
importClass(javax.xml.parsers.DocumentBuilderFactory);
importClass(javax.xml.parsers.ParserConfigurationException);

importClass(org.w3c.dom.Document);
importClass(org.xml.sax.SAXException);
importClass(org.xml.sax.SAXParseException);


function newDocument(root, attributeName, attributeVal) {
	
//	var result = Packages.javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
	var result = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
/* -=-=-=-=-=-=-=-=-=-=-=-=-=-=- 
    // Create a processing instruction targeted for xml.
    var node = result.createProcessingInstruction("xml", "version='1.0'");
    result.appendChild(node);
    node = null;

    // Create a comment for the document.
    node = result.createComment(
        "sample xml file created using XML DOM object.");
    result.appendChild(node);
    node=null;	
 -=-=-=-=-=-=-=-=-=-=-=-=-=-=- */
	result.appendChild(result.createElement(root));
	print("Add attributes to root..." + attributeName);
    if (attributeVal != null){
    	try	{
			print("creating attribute for root: " + attributeName);    	
			var root = result.getFirstChild();
			root.setAttribute(attributeName, attributeVal);    			

        } catch (error) {
            cocoon.log.error("Could not add attribute node to root: " + error);
        }
	    
    }

	return result;
}

function loadHttpDocument(method) {
			var log = Packages.org.apache.log4j.Logger.getLogger("loadHttpDocument");			
 		log.warn("start loadHttpDocument: ");	
		
			httpClient = new HttpClient();
			status = httpClient.executeMethod(method);
			responseBody = new String(method.getResponseBody());
			var document;
			
			try {
				bis = new ByteArrayInputStream(responseBody.getBytes("UTF-8"));
				factory = DocumentBuilderFactory.newInstance();
				builder = factory.newDocumentBuilder();
				document = builder.parse(bis);
			} 			
			catch (error){	log.warn("start loadHttpDocument: "+uri)}	
			method.releaseConnection();
			return document;
}				
		
		

function addNode(hrcyDocument, nodeParent, nodeName, nodeText, attributeName, attributeVal){
	var document = null;
	document = hrcyDocument;
	
	if(document == null){
		print("Dom doc is null!");
		return null;
	}

    var root = document.getFirstChild();

    print("creating newNode: " + nodeName);

    var newNode = document.createElement(nodeName);
    
    if(nodeText != null){
	    print("creating textnode: " + nodeText);
	    newNode.appendChild(document.createTextNode(nodeText));
	}
    
    if (attributeVal != null){
    	try	{
			print("creating attribute: " + attributeName);    	
    		newNode.setAttribute(attributeName, attributeVal);    		

        } catch (error) {
            cocoon.log.error("Could not add attribute node: " + error);
        }
	    
    }
    
    if(nodeParent == null){
    	print("Node parent is null - appending newNode to root");
	    root.appendChild(newNode);
	} else {
    	print("Node parent is NOT null - appending newNode to parent");
		nodeParent.appendChild(newNode);
	}
	return newNode;
}

function addAttribute(document,nodeParent, attributeName, attributeVal){
	//Add an attribute to the nominated nodeParent.
	if(document == null){
		print("Dom doc is null!");
		return null;
	}

    if (attributeVal != null){
    	try	{
			print("creating attribute: " + attributeName);
			if(nodeParent == null){
				var root = document.getFirstChild();
				root.setAttribute(attributeName, attributeVal);
			} else {    	
	    		nodeParent.setAttribute(attributeName, attributeVal); 
	    	}   		

        } catch (error) {
            cocoon.log.error("Could not add attribute node: " + error);
        }
	    
    }
	return nodeParent;
}
function getParentNodeFromDOM(hrcyDoc, nodeId){
	print("In getParentNodeFromDOM...");
//	var document = null;
//	document = hrcyDoc;
	
	var prntNode = null;
	
	
	if(hrcyDoc == null){
		print("Dom doc is null, return null.");
		return null;
/*		try{
			hrcyDoc = loadDocument("cocoon:/hrcy_xml/hrcy.xml");
		} catch (error) {
			print("Error loading xml document! " + error);
			return null;
		}
*/
	}
	print("got document, now getting requested node (" + nodeId + ")");
	
	if(nodeId == 0){
		print(" nodeId = 0, We need to get a reference to the parentest node of the document");
		prntNode = hrcyDoc.getFirstChild();
		return prntNode;
	}
	
	if (hrcyDoc.getElementById) { 
		//print("***** getElementById is C O O L ");
		//getElementById doesn't always return an element
		// if there's no dtd specifying the id attribute.
		prntNode = hrcyDoc.getElementById(nodeId+ "");
	} else {
		//We need to walk the tree and locate the node with the id
		prntNode = getNodeById(hrcyDoc, "hNode", nodeId + "");				
		return prntNode;
	}
	if(prntNode != null){
		print("returning parent node!");
		return prntNode;
	} else {
		print("Did not obtain reference to parent node...trying getNode()");
		//We'll walk the tree looking for the node in question...
		//print("Actually trying getNodeByIdXPATH");
		//prntNode = getNodeByIdXPATH(document, "hNode", nodeId + "")	;
	
		prntNode = getNodeById(hrcyDoc, "hNode", nodeId + "");				
		return prntNode;
	}
}
function checkChildNodesExists(domNode){
	//If we've already got the nodes in the document,
	//don't re-get them as they'll double up in the tree
	print("In checkChildNodesExists()~~~Node is of type: " + domNode.getNodeType());
//	if(domNode.getNodeType()==domNode.ELEMENT_NODE) print("We got an ELEMENT!~~~");
	if(domNode.hasChildNodes()){
		var childList = domNode.getChildNodes();
		print("Number of childnodes = " + childList.getLength());				
		return childList.getLength();
	} else {
		return 0;
	}
}

function getNodeByIdXPATH(document, sXpath) {

	//Return a node list that corresponds to the XPath
	// Serving suggestion: sXpath = "//hNode[@copyStatus='U']" to return all updated nodes etc
	try {
		print("getNodeByIdXPATH: " + sXpath);

		if(document == undefined){
			print("Document is undefined!");
			document = hrcyDoc;
		}
		var nl = null;
		nl = XPathAPI.selectNodeList(document, sXpath);
		print("Num of nodes = " + nl.length);
		return nl ;		
		
	} catch (error) {

		print("Error in getNodeByIdXPATH: " + error);
		return null;		
	}

}

function getNodesByAttribute(document, elmntName, attrName, attrValue) {
	/** elmntName = "hNode"
	 * attrName = the attribute we're trying to find. 
	 * attrValue = the atrribute's value we're trying to retrieve
	 * Returns the nodes whose attr = the value parm */
	print("In getNodesByAttribute: " + elmntName + "; Attribute Name = " + attrName);
	var node = null;
	
	//Get all parentNode or childNode elements
	var pnodes = document.getElementsByTagName(elmntName);

	var nodeLen = pnodes.getLength();
	print("Number of elements retrieved = " + nodeLen);
	if (nodeLen == 0) {
		//Node don't exist!
		return null;
	} else {
		//We've found some Node elements, 
		//see if the id attribute value = nodeId parm.
		for (var i = 0; i < nodeLen; i++) {
			node = pnodes.item(i);
			if (node != null) {
				if (node.hasAttributes()) {
					var attributes = node.getAttributes();
					var idAttribute = attributes.getNamedItem(attrName);
					if (idAttribute != null){
						if (idAttribute.getNodeValue().equals(attrValue)) {
							return node;
						}
					} else {
						return null;
					}
				}
			}
		}
		return null;
	}
}
	
function getNodeById(document, elmntName, nodeId) {
	/** elmntName = "hNode"
	 * nodeId = the node id we're trying to find. Using this instead of name so
	 * you can have duplicate names if necessary.
	 * Returns the node whose id attr = the nodeId parm */
	print("In getNodeById: " + elmntName + "; nodeid = " + nodeId);
	var node = null;
	
	//Get all parentNode or childNode elements
	var pnodes = document.getElementsByTagName(elmntName);

	var nodeLen = pnodes.getLength();
	print("Number of elements retrieved = " + nodeLen);
	if (nodeLen == 0) {
		//Node don't exist!
		return null;
	} else {
		//We've found some Node elements, 
		//see if the id attribute value = nodeId parm.
		for (var i = 0; i < nodeLen; i++) {
			node = pnodes.item(i);
			if (node != null) {
				if (node.hasAttributes()) {
					var attributes = node.getAttributes();
					var idAttribute = attributes.getNamedItem("id");
					if (idAttribute != null){
						if (idAttribute.getNodeValue().equals(nodeId)) {
							return node;
						}
					} else {
						return null;
					}
				}
			}
		}
		return null;
	}
}

//geht leider nicht, Hato 24.10.04
function getNodeText2(domNode){
	if(domNode == null){
		return;
	}
	var nodeList = domNode.childNodes;
	print("Getting list of node's kids...");
	text="";
	for(var i=0; i < nodeList.getLength(); i++){
					var nodeKid = nodeList.item(i);
					if (nodeKid.nodeType==nodeKid.TEXT_NODE){
							print("We found a text node.");
							text += nodeKid.nodeValue;
					 	//if(nodeKid.nextSibling.nodeType!=nodeKid.TEXT_NODE) break;		
					} 
					else if (nodeKid.nodeType==nodeKid.ELEMENT_NODE){
							getNodeText2(nodeKid);
					}
	}				
	return text;
}


function getNodeText(domNode){
        if(domNode == null){
                return;
        }
        var nodeList = domNode.getChildNodes();
        print("Getting list of node's kids...");
        for(var i=0; i < nodeList.getLength(); i++){
                var nodeKid = nodeList.item(i);
                if (nodeKid.getNodeType()==nodeKid.TEXT_NODE){
                        print("We found a text node.");
                        return nodeKid.getNodeValue();
                } else {
                        return null;
                }
        }

}


function getNodeAttributeValue(domNode,attributeName){
	if(domNode == null){
		return null;
	}
	if (domNode.hasAttributes()) {
		var attributes = domNode.getAttributes();
		var idAttribute = attributes.getNamedItem(attributeName);
		if (idAttribute != null){
			return idAttribute.getNodeValue();
		} else {
			return null;
		}
	} else {
		return null;
	}
}

function getComponentChildByName(domNode, childName){

	/* 	For a given node, return a reference to a component child node
		with a given element name eg: hNode/components/component/authorityLink <- return the authorityLink child.
		We're assuming that the node has links already loaded if it has any.
	*/
	print("In getComponentChildByName(). Getting elements by tag name for '" + childName + "'");
	if((childName == null) || (childName == "")){
		print("In getComponentChildByName: childName is null! Returning...");
		return null;
	}
	
	if(domNode == null){
		print("In getComponentChildByName: domNode is null! Returning...");
		return null;
	}

	var nlist = domNode.getElementsByTagName(childName);
	if(nlist != null){
		print("Number of elements with tag '" + childName + "' = " + nlist.getLength());
		if(nlist.getLength() == 0){
			return null;
		} else {
			//There should only be one element
			var nodeKid = nlist.item(0);
			return nodeKid;
		}
	}
}


function updateDomNodeDesc(domNode, nodeDesc){

	var nodeList = domNode.getChildNodes();
	print("Getting list of node's kids...");
	for(var i=0; i < nodeList.getLength(); i++){
		var nodeKid = nodeList.item(i);
		if (nodeKid.getNodeType()==nodeKid.TEXT_NODE){
			print("We found a text node. Change it!");
			nodeKid.setNodeValue(nodeDesc);				
			return true;
		}
	}				
}

function setDomNodeAttribute(domNode, attribute,value){
	//More brutal method of adding an attribute to a node
	// instead of using addAttribute, which requires an element.
	if(domNode == null){
		return null;
	}
	
	if(domNode.getNodeType()==domNode.ELEMENT_NODE){
		domNode.setAttribute(attribute, value);
	} else {
		//node is not an element but a node
		// (probably because its been cloned)
		if (domNode.hasAttributes()) {
			var attributes = domNode.getAttributes();
			if (attributes != null || (attributes.getLength() > 0)) {
				for (var i = 0; i < attributes.getLength(); i++) {
					var attribute = attributes.item(i);
					if(attribute.getNodeName() == attribute){
						attribute.setNodeValue(value);
					}
				}
			}
		}		
		
	}		
	return true;
}

function setDomNodeElementText(domNode, elmntName, value){

	print("In setDomNodeElementText(). Changing " + elmntName + " text to '" + value + "'");
	print("Calling getComponentChildByName() for " + elmntName);
	changeNode = getComponentChildByName(domNode, elmntName);
	if(changeNode != null){
		print("Updating the value to " + value);
		updateDomNodeDesc(changeNode, value);
		return true;	
	}

	return false;
}

function moveNode(document, domNode){
	//Grafts node onto the end of the document
	var newNode= null;
	newNode = document.importNode(domNode,true);
	var root = document.getLastChild();
	root.appendChild(newNode);
}

function moveNodeToNode(document, domNodeTarget, domNodeToMove){
	//Grafts domNodeToMove onto domNodeTarget
	var newNode= null;
	newNode = document.importNode(domNodeToMove,true);
	domNodeTarget.appendChild(newNode);
}

function removeNodeFromDOM(document, domNode){
	if(domNode.parentNode == null){
		var root = document.getFirstChild();
		return root.removeChild(domNode);
		return true;
	} else {
		var prntNode = domNode.parentNode;
		return prntNode.removeChild(domNode);
	}
}


function loadOtherDocument(uri) {
    var parser = null;
    var source = null;
    var resolver = null;
    try {
        parser = cocoon.getComponent(Packages.org.apache.excalibur.xml.dom.DOMParser.ROLE);
        resolver = cocoon.getComponent(Packages.org.apache.cocoon.environment.SourceResolver.ROLE);
        source = resolver.resolveURI(uri);
        var is = new Packages.org.xml.sax.InputSource(source.getInputStream());
        is.setSystemId(source.getURI());
        return parser.parseDocument(is);
    } finally {
        if (source != null)
            resolver.release(source);
        cocoon.releaseComponent(parser);
        cocoon.releaseComponent(resolver);
    }
}
function saveDocument(document, uri) {
	
    var source = null;
    var resolver = null;
    var outputStream = null;
    try {
        resolver = cocoon.getComponent(Packages.org.apache.cocoon.environment.SourceResolver.ROLE);
        source = resolver.resolveURI(uri);

        var tf = Packages.javax.xml.transform.TransformerFactory.newInstance();

        if (source instanceof Packages.org.apache.excalibur.source.ModifiableSource
            && tf.getFeature(Packages.javax.xml.transform.sax.SAXTransformerFactory.FEATURE)) {

            outputStream = source.getOutputStream();
            var transformerHandler = tf.newTransformerHandler();
            var transformer = transformerHandler.getTransformer();
            transformer.setOutputProperty(Packages.javax.xml.transform.OutputKeys.INDENT, "true");
            transformer.setOutputProperty(Packages.javax.xml.transform.OutputKeys.METHOD, "xml");
            transformerHandler.setResult(new Packages.javax.xml.transform.stream.StreamResult(outputStream));

            var streamer = new Packages.org.apache.cocoon.xml.dom.DOMStreamer(transformerHandler);
            streamer.stream(document);
        } else {
            throw new Packages.org.apache.cocoon.ProcessingException("Cannot write to source " + uri);
        }
    } finally {
        if (source != null)
            resolver.release(source);
       		cocoon.releaseComponent(resolver);
        if (outputStream != null) {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (error) {
                cocoon.log.error("Could not flush/close outputstream: " + error);
            }
        }
    }
}

/*
==================================================================
LTrim(string) : Returns a copy of a string without leading spaces.
==================================================================
*/
function LTrim(str)
/*
   PURPOSE: Remove leading blanks from our string.
   IN: str - the string we want to LTrim
*/
{
   var whitespace = new String(" \t\n\r");

   var s = new String(str);

   if (whitespace.indexOf(s.charAt(0)) != -1) {
      // We have a string with leading blank(s)...

      var j=0, i = s.length;

      // Iterate from the far left of string until we
      // don't have any more whitespace...
      while (j < i && whitespace.indexOf(s.charAt(j)) != -1)
         j++;

      // Get the substring from the first non-whitespace
      // character to the end of the string...
      s = s.substring(j, i);
   }
   return s;
}

/*
==================================================================
RTrim(string) : Returns a copy of a string without trailing spaces.
==================================================================
*/
function RTrim(str)
/*
   PURPOSE: Remove trailing blanks from our string.
   IN: str - the string we want to RTrim

*/
{
   // We don't want to trip JUST spaces, but also tabs,
   // line feeds, etc.  Add anything else you want to
   // "trim" here in Whitespace
   var whitespace = new String(" \t\n\r");

   var s = new String(str);

   if (whitespace.indexOf(s.charAt(s.length-1)) != -1) {
      // We have a string with trailing blank(s)...

      var i = s.length - 1;       // Get length of string

      // Iterate from the far right of string until we
      // don't have any more whitespace...
      while (i >= 0 && whitespace.indexOf(s.charAt(i)) != -1)
         i--;


      // Get the substring from the front of the string to
      // where the last non-whitespace character is...
      s = s.substring(0, i+1);
   }

   return s;
}

/*
=============================================================
Trim(string) : Returns a copy of a string without leading or trailing spaces
=============================================================
*/
function Trim(str)
/*
   PURPOSE: Remove trailing and leading blanks from our string.
   IN: str - the string we want to Trim

   RETVAL: A Trimmed string!
*/
{
   return RTrim(LTrim(str));
}
function Replace(str, chrReplace, chrInsert){
	var regExp = chrReplace ;
	print("Replacing " + str + ": " + chrReplace + " with " + chrInsert);
	print("RegExp = " + regExp);
	return str.replace(regExp , chrInsert);
}

function getJdbcClassName(){
	var dbInfo = loadDocument("cocoon:/xml/db_connections.xml");	
	print("Loading document: db_connections.xml");
	//get the root node (in this case, it is ROOTNODE)
	var docRoot = dbInfo.getDocumentElement();
	//get the first "TAG1" element
	var connElmt = docRoot.getElementsByTagName("connection").item(0);
	
	var jdbcClassElmt = connElmt.getElementsByTagName("class_forname").item(0);
	var jdbcClassName = jdbcClassElmt.getFirstChild().getNodeValue();
	cocoon.session.setAttribute("jdbcClassName",jdbcClassName);		
	print("class = " + jdbcClassName);
	return jdbcClassName;
	
}
function getDbUrl(){
	var dbInfo = loadDocument("cocoon:/xml/db_connections.xml");	
	print("Loading document: db_connections.xml");
	//get the root node (in this case, it is ROOTNODE)
	var docRoot = dbInfo.getDocumentElement();
	//get the first 'connection' element
	var connElmt = docRoot.getElementsByTagName("connection").item(0);
	
	var dbUrlElmt = connElmt.getElementsByTagName("db_url").item(0);
	var dbUrl = dbUrlElmt.getFirstChild().getNodeValue();
	cocoon.session.setAttribute("dbUrl",dbUrl);
	print("dbUrl = " + dbUrl);
	return dbUrl;
	
}
function getRandom(){

	var random_num = (Math.round((Math.random()*60000)+1))
	return random_num;
	
}

function getDbUserName(psUserName){
	//If we pass in a username, simply add them to the session attribute
	//otherwise get the username from the connection xml.
	var dbUser = "";
	if (psUserName == null){	
		var dbInfo = loadDocument("cocoon:/xml/db_connections.xml");	
		print("Loading document: db_connections.xml");
		//get the root node (in this case, it is ROOTNODE)
		var docRoot = dbInfo.getDocumentElement();
		//get the first 'connection' element
		var connElmt = docRoot.getElementsByTagName("connection").item(0);		
		var dbUserElmt = connElmt.getElementsByTagName("username").item(0);
		dbUser = dbUserElmt.getFirstChild().getNodeValue();
	} else {
		dbUser = psUserName;
	}
	cocoon.session.setAttribute("dbUser", dbUser);
	print("dbUser = " + dbUser);
	return dbUser;
}

function getDbPassWord(psPassWord){
	var dbPWrd = "";
	if(psPassWord == null){
		var dbInfo = loadDocument("cocoon:/xml/db_connections.xml");	
		print("Loading document: db_connections.xml");
		//get the root node (in this case, it is ROOTNODE)
		var docRoot = dbInfo.getDocumentElement();
		//get the first 'connection' element
		var connElmt = docRoot.getElementsByTagName("connection").item(0);	
		var dbUserElmt = connElmt.getElementsByTagName("password").item(0);	
		dbPWrd = dbUserElmt.getFirstChild().getNodeValue();
	} else {
		dbPWrd = psPassWord;
	}
	cocoon.session.setAttribute("dbPWrd",dbPWrd);		
	print("dbPWrd = " + dbPWrd);
	return dbPWrd;
}
