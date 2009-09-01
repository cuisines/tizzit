importClass(java.lang.String);
cocoon.load("flow/include.js");

function testCDocLogin() {
    var logprefix = "snet";	
    var log = Packages.org.apache.log4j.Logger.getLogger(logprefix+".testCDocLogin");
    var requestParams = (new CRequestParamsBuilder(logprefix+".CRequestParamsBuilder")).getRequestParams(cocoon);
    log.debug("DocLogin-Objekt erzeugen\n");
    var myCDocLogin = new CDoccheckLogin(logprefix+".myCDoccheckLogin");
    myCDocLogin.init();
    var success = myCDocLogin.createDocUser('snet', requestParams);
    log.debug("hats geklappt? "+success);
    myCDocLogin.endDocLogin();
    cocoon.sendPage('/loginpage/page.html');	
    return;
}