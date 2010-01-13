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
package de.juwimm.cms.test.hibernate;

import java.security.Principal;
import java.util.Iterator;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.context.SecurityContextImpl;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationToken;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import de.juwimm.cms.authorization.SimpleCallbackHandler;

/**
 * Abstract test class inherited from
 * AbstractTransactionalDataSourceSpringContextTests which overrides the
 * getConfigLocations() method so that the derived test classes do not to
 * override the method every time
 * 
 * @see org.springframework.test.AbstractTransactionalDataSourceSpringContextTests
 * @author Jens
 * 
 */
public abstract class HbmTestImpl extends AbstractTransactionalDataSourceSpringContextTests implements HbmTest {

	private static Log log = LogFactory.getLog(HbmTestImpl.class);
	private static final String DATA_SOURCE = "classpath:beans-test.xml";
	private static final String APPLICATION_CONTEXT = "applicationContext.xml";
	private static final String TIZZIT_BEANS = "classpath:applicationContext-tizzitBeans-test.xml";
	private static final String TIZZIT_COMPASS = "classpath:applicationContext-compass-test.xml";
	private LoginContext loginContext = null;
	public static final String SYSTEM_USER = "system";

	public HbmTestImpl() {
		super();
		setAutowireMode(AUTOWIRE_BY_NAME);
	}

	/**
	 * Supply the spring configuration files
	 */
	@Override
	protected String[] getConfigLocations() {
		//String[] springConfig = { TEST_APPLICATION_CONTEXT, TEST_DATA_SOURCE };
		String[] springConfig = {DATA_SOURCE, APPLICATION_CONTEXT, TIZZIT_COMPASS, TIZZIT_BEANS};
		return springConfig;
	}

	/**
	 * Returns the used hibernate session factory
	 * 
	 * @return A a session factory object
	 */
	protected SessionFactory getSessionFactory() {
		return (SessionFactory) applicationContext.getBean("sessionFactory");
	}

	/**
	 * Returns a new hibernate session
	 * 
	 * @return A hibernate session
	 */
	protected Session getSession() {
		return getSessionFactory().openSession();
	}

	/**
	 * List all mapped classes using log4j 
	 * 
	 */
	protected void listMappedClasses() {
		String[] classes = getApplicationContext().getBeanDefinitionNames();
		logSeperator();
		for (int i = 0; i < classes.length; i++) {
			log.info(classes[i]);
		}
		logSeperator();
	}

	protected void logSeperator() {
		log.info("\n##############################\n");
	}

	public void loginUser(String username, String password) {
		Principal p = null;
		if (loginContext == null) {
			SimpleCallbackHandler simpleCallbackHandler = new SimpleCallbackHandler(username, password);
			try {
				loginContext = new LoginContext("juwimm-cms-security-domain", simpleCallbackHandler);
				loginContext.login();
				Subject s = loginContext.getSubject();
				Iterator it = s.getPrincipals().iterator();
				if (!s.getPrincipals().isEmpty()) {
					while (it.hasNext()) {
						p = (Principal) it.next();
						if (!p.getName().equalsIgnoreCase(SYSTEM_USER)) {
							org.andromda.spring.PrincipalStore.set(p);
							break;
						}
					}
				}
			} catch (LoginException e) {
				if (log.isErrorEnabled()) {
					log.error("Could not login: " + e.getMessage(), e);
				}
			}
		}
	}

	public Principal loginSystemUser() {
		Principal p = null;
		if (loginContext == null) {
			log.info("Setting principal...");
			//TODO login			
			//			System.setProperty( "java.security.auth.login.config", "C:\\svnroot\\juwimm-cms\\core\\src\\test\\jaas.conf" );
			String encoded = "e";
			SimpleCallbackHandler simpleCallbackHandler = new SimpleCallbackHandler(SYSTEM_USER, encoded);
			try {
				loginContext = new LoginContext("juwimm-cms-security-domain", simpleCallbackHandler);
				loginContext.login();
				Subject s = loginContext.getSubject();
				Iterator it = s.getPrincipals().iterator();
				if (!s.getPrincipals().isEmpty()) {
					p = (Principal) it.next();
					org.andromda.spring.PrincipalStore.set(p);
				}
			} catch (LoginException e) {

				if (log.isErrorEnabled()) {
					log.error("Could not login: " + e.getMessage(), e);
				}
			}
		}
		return p;
	}

	public Object getBean(String mappedName) {
		loginSystemUser();
		return applicationContext.getBean(mappedName);
	}

	public Subject getSubject() {
		if (loginContext != null) {
			return loginContext.getSubject();
		}
		return null;
	}

	public void mockAuthetication() {
		SecurityContextImpl secureContext = new SecurityContextImpl();
		Authentication token = new AnonymousAuthenticationToken("testUser", "testUser", new GrantedAuthority[] {new GrantedAuthorityImpl("testRole")});
		secureContext.setAuthentication(token);
		SecurityContextHolder.setContext(secureContext);
	}

}