importClass(java.lang.String);

cocoon.load("flow/include.js");

function testCRegister() {
    var logprefix = "snet";	
    var log = Packages.org.apache.log4j.Logger.getLogger(logprefix+".testCRegister");
    log.debug("vor Register-Objekt erzeugen\n");
    var myCRegister = new CRegister(logprefix+".myCRegister");
    
    myCRegister.createRegisterService();
    
    var success = myCRegister.userExists('snet', '99997','2');
    log.debug("Success: "+success);
    
    myCRegister.getUserVO('snet', '99997', '2');
    
    myCRegister.setEmail('anne.milbert@juwimm.com');
    log.debug("Email: "+myCRegister.getEmail());
    myCRegister.setPassword('test');
    log.debug("PW: "+myCRegister.getPassword());
    myCRegister.setActive(true);
    log.debug("Active: "+myCRegister.isActive());
    
    
    myCRegister.addExtraValue("beruf", "Arzt");
    var beruf = myCRegister.getExtraValue("beruf");
    log.debug("Beruf: "+beruf);
    myCRegister.removeExtraValue("beruf");
    beruf = myCRegister.getExtraValue("beruf");
    log.debug("Beruf: "+beruf);
    
    var testme = myCRegister.registerUserToLms();
    log.debug("Beim LMS registriert: "+testme);
    var pin = myCRegister.getPin();
    log.debug("Pin: "+pin);
    
    var registered = myCRegister.registerUserVO('2');
    log.debug("registered: "+registered);
    
    var test = false;
    test = myCRegister.updateUserVO();
    log.debug("Userdata updated? "+test);
    
    myCRegister.closeCRegister();
    
    cocoon.sendPage('/loginpage/page.html');	
    
    return;
}