/**
 * Copyright (c) 2009 Juwi MacMillan Group GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.juwimm.cms.remote.test;

import junit.framework.TestCase;

import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.juwimm.cms.authorization.model.UserHbmDao;
import de.juwimm.cms.authorization.vo.UserLoginValue;
import de.juwimm.cms.authorization.vo.UserValue;
import de.juwimm.cms.components.vo.AddressValue;
import de.juwimm.cms.components.vo.PersonValue;
import de.juwimm.cms.remote.ClientServiceSpring;

public abstract class AbstractRemoteInterfaceTest extends TestCase {

    private ClassPathXmlApplicationContext ctx = null;
    private final String REMOTE = "applicationContext-import-remoteServices.xml";
    private ClientServiceSpring clientService = null;
    private static Logger log = Logger.getLogger(AbstractRemoteInterfaceTest.class);
    protected String testUserName = "a";
    protected String testPassword = "a";
    protected Integer testSiteId = 11;
    protected UserValue uv = null;

    @Override
    protected void setUp() throws Exception {
	BasicConfigurator.configure();
	Logger.getLogger("org.hibernate").setLevel(Level.DEBUG);
	Logger.getLogger("org.springframework").setLevel(Level.ERROR);
	String[] config = {REMOTE};
	ctx = new ClassPathXmlApplicationContext(config);
	UserLoginValue ulv = login();
	this.uv = ulv.getUser();
	log.debug("User logged in");
	super.setUp();
    }
    
    
    protected UserLoginValue login() {
	UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(testUserName, testPassword);
	SecurityContext context = SecurityContextHolder.getContext();
	context.setAuthentication(auth);
	UserLoginValue uv = getClientService().login(testUserName, testPassword, testSiteId);
	return uv;
    }


    protected ClientServiceSpring getClientService() {
	if (clientService == null) {
	    clientService = (ClientServiceSpring) ctx.getBean("clientServiceSpring");
	}
	return clientService;
    }
    
    protected UserValue createTestUser() {
	  String userName = "JuwiMMTestUser";
	  String passwd = "testPass";
	  String firstName = "firstName";
	  String lastName = "lastName";
	  String email = "email@email.com";
	  Integer unitId = 3227;
	  ClientServiceSpring cs = getClientService();
	  cs.createUser(userName, passwd, firstName, lastName, email, unitId);
	  UserValue uv = cs.getUserForId(userName);
	  return uv;
    }
    
    protected void deleteTestUser(String userId) {
	 ClientServiceSpring cs = getClientService();
	 cs.deleteUser(userId);
    }
    
    protected void removeTask(int taskId) {
	 ClientServiceSpring cs = getClientService();
	 cs.removeTask(taskId);
    }
    
    protected int createTestTask() {
	 ClientServiceSpring cs = getClientService();
	 String receiverId = "a";
	 String receiverRole = "a";
	 Integer unitId = 3227;
	 String comment = "Kommentar";
	 byte taskType = 1;
	 int taskId = cs.createTask(receiverId, receiverRole, unitId, comment, taskType);
	 return taskId;
    }
    
    protected PersonValue createTestPerson() {
	 PersonValue pv = new PersonValue();
	 pv.setCountryJob("countryJob");
	 pv.setExternalId("externalId");
	 pv.setFirstname("FirstName");
	 pv.setLastname("lastName");
	 pv.setImageId(9771);
	 pv.setUnitId(3227L);
	 pv.setDepartmentId(1L);
	 return pv;
    }
    
    protected String departmentName = "TestDepartment";
    protected Integer departmentUnitId = 3227;
    
    protected long createTestDepartment() {
	 ClientServiceSpring cs = getClientService();	 
	 long departmentId = cs.createDepartment(departmentName, departmentUnitId);
	 return departmentId;
    }
    
    protected String city = "Hannover";
    protected String country = "Deutschland";
    protected String email = "email@email.de";
    protected String fax = "0154786987";
    
    protected long createTestAddress() {
	ClientServiceSpring cs = getClientService();
	AddressValue av = new AddressValue();
	av.setCity(city);
	av.setCountry(country);
	av.setEmail(email);
	av.setFax(fax);
	long id = cs.createAddress(av);
	return id;
    }
    
    protected String talkTimes = "talktimesTest";
    protected String talkTimeType = "talkTimeTypeTest";
    
    protected long createTestTalktime() {
	ClientServiceSpring cs = getClientService();
	long id = cs.createTalkTime(talkTimes, talkTimeType);	
	return id;
    }
    
    protected void removeTalktime(long id) {
	ClientServiceSpring cs = getClientService();
	cs.removeTalktime(id);
    }
    
}
