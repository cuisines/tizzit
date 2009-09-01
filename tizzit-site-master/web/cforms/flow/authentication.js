function getAuthentication(handler) {

    var logprefix = "sero.register";
	var log = Packages.org.apache.log4j.Logger.getLogger(logprefix+".Authentication");

	var userState = cocoon.session.getAttribute(Packages.org.apache.cocoon.webapps.authentication.components.DefaultAuthenticationManager.SESSION_ATTRIBUTE_USER_STATUS);
	var userName = "";
	try {
		var ctx = userState.getHandler(handler).getContext();
		userName = ctx.getXML("/authentication/ID").getFirstChild().getNodeValue();
	} catch(exe) {
		log.debug("Couldn't retrieve userName by getAuthentication()\n"+exe);
	}
    return userName;
}