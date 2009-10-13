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
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import de.juwimm.cms.authorization.SimpleCallbackHandler;
import de.juwimm.cms.authorization.model.GroupHbmDao;
import de.juwimm.cms.authorization.model.UserHbmDao;
import de.juwimm.cms.components.model.AddressHbmDao;
import de.juwimm.cms.components.model.DepartmentHbmDao;
import de.juwimm.cms.components.model.PersonHbmDao;
import de.juwimm.cms.components.model.TalktimeHbmDao;
import de.juwimm.cms.model.ContentHbmDao;
import de.juwimm.cms.model.ContentVersionHbmDao;
import de.juwimm.cms.model.DocumentHbmDao;
import de.juwimm.cms.model.EditionHbmDao;
import de.juwimm.cms.model.HostHbmDao;
import de.juwimm.cms.model.LockHbmDao;
import de.juwimm.cms.model.PictureHbmDao;
import de.juwimm.cms.model.SiteHbmDao;
import de.juwimm.cms.model.TaskHbmDao;
import de.juwimm.cms.model.UnitHbmDao;
import de.juwimm.cms.model.ViewComponentHbmDao;
import de.juwimm.cms.model.ViewDocumentHbmDao;
import de.juwimm.cms.model.SequenceHbmDao;
import de.juwimm.cms.safeguard.model.RealmJaasHbmDao;
import de.juwimm.cms.safeguard.model.RealmJdbcHbmDao;
import de.juwimm.cms.safeguard.model.RealmLdapHbmDao;
import de.juwimm.cms.safeguard.model.RealmSimplePwHbmDao;
import de.juwimm.cms.safeguard.model.RealmSimplePwUserHbmDao;

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

	private static Log log = LogFactory.getLog( HbmTestImpl.class );
	private static final String DATA_SOURCE = "classpath:beans-test.xml";
	private static final String APPLICATION_CONTEXT = "applicationContext.xml";
	private static final String TIZZIT_BEANS = "classpath:applicationContext-tizzitBeans-test.xml";
	private static final String TIZZIT_COMPASS = "classpath:applicationContext-compass-test.xml";
	private LoginContext loginContext = null;
	public static final String SYSTEM_USER = "system";
	private UserHbmDao userDao = null;
	private GroupHbmDao groupDao = null;
	private TaskHbmDao taskDao = null;
	private HostHbmDao hostDao = null;
	private SiteHbmDao siteDao = null;
	private ContentHbmDao contentDao = null;
	private ContentVersionHbmDao contentVersionDao = null;
	private ViewComponentHbmDao viewComponentDao = null;
	private DocumentHbmDao documentDao = null;
	private PictureHbmDao pictureDao = null;
	private UnitHbmDao unitDao = null;
	private ViewDocumentHbmDao viewDocumentDao = null;
	private EditionHbmDao editionDao = null;
	private PersonHbmDao personDao = null;
	private AddressHbmDao addressDao = null;
	private DepartmentHbmDao departmentDao = null;
	private TalktimeHbmDao talktimeDao = null;
	private RealmSimplePwHbmDao realmSimplePwDao = null;
	private RealmSimplePwUserHbmDao realmSimplePwUserDao = null;
	private RealmJaasHbmDao realmJaasDao = null;
	private RealmLdapHbmDao realmLdapDao = null;
	private RealmJdbcHbmDao realmJdbcDao = null;
	private LockHbmDao lockDao = null;
	private static String LOCK_DAO = "lockHbmDao";
	private static String REALM_JDBC_DAO = "realmJdbcHbmDao";
	private static String REALM_LDAP_DAO = "realmLdapHbmDao";
	private static String REALM_JAAS_DAO = "realmJaasHbmDao";
	private static String REALM_SIMPLE_PW_DAO = "realmSimplePwHbmDao";
	private static String REALM_SIMPLE_PW_USER_DAO = "realmSimplePwUserHbmDao";
	private static String USER_DAO = "userHbmDao";
	private static String GROUP_DAO = "groupHbmDao";
	private static String TASK_DAO = "taskHbmDao";
	private static String HOST_DAO = "hostHbmDao";
	private static String CONTENT_DAO = "contentHbmDao";
	private static String SITE_DAO = "siteHbmDao";
	private static String CONTENT_VERSION_DAO = "contentVersionHbmDao";
	private static String VIEWCOMPONENT_DAO = "viewComponentHbmDao";
	private static String DOCUMENT_DAO = "documentHbmDao";
	private static String PICTURE_DAO = "pictureHbmDao";
	private static String UNIT_DAO = "unitHbmDao";
	private static String VIEW_DOCUMENT_DAO = "viewDocumentHbmDao";
	private static String EDITION_DAO = "editonHbmDao";
	private static String PERSON_DAO = "personHbmDao";
	private static String ADDRESS_DAO = "addressHbmDao";
	private static String DEPARTMENT_DAO = "departmentHbmDao";
	private static String TALKTIME_DAO = "talktimeHbmDao";
	private static String SEQUENCE_DAO = "sequenceDao";	
	
	public HbmTestImpl() {
		super();		
		setAutowireMode( AUTOWIRE_BY_NAME );		
	}
	
	
	/**
	 * Supply the spring configuration files
	 */
	@Override
	protected String[] getConfigLocations() {
		//String[] springConfig = { TEST_APPLICATION_CONTEXT, TEST_DATA_SOURCE };
		String[] springConfig = {DATA_SOURCE, APPLICATION_CONTEXT,TIZZIT_COMPASS,TIZZIT_BEANS};
		return springConfig;
	}
	
	
	/**
	 * Returns the used hibernate session factory
	 * 
	 * @return A a session factory object
	 */
	protected SessionFactory getSessionFactory() {
		return (SessionFactory) applicationContext.getBean( "sessionFactory" );
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
		for( int i=0; i<classes.length; i++ ) {
			log.info( classes[i] );
		}
		logSeperator();
	}
	
	protected void logSeperator() {
		log.info(  "\n##############################\n" );
	}
	
	public void loginUser( String username, String password ) {
		Principal p = null;
		if( loginContext == null ) {			
			SimpleCallbackHandler simpleCallbackHandler = new SimpleCallbackHandler( username, password );
			try {
				loginContext = new LoginContext( "juwimm-cms-security-domain", simpleCallbackHandler );
				loginContext.login();
				Subject s = loginContext.getSubject();
				Iterator it = s.getPrincipals().iterator();
				if( !s.getPrincipals().isEmpty() ) {
					while( it.hasNext() ) {
						p = (Principal) it.next();
						if( !p.getName().equalsIgnoreCase( SYSTEM_USER ) ) { 
							org.andromda.spring.PrincipalStore.set( p );
							break;
						}
					}
				}
			} catch ( LoginException e ) {
				if( log.isErrorEnabled() ) {
					log.error( "Could not login: " + e.getMessage(), e );
				}
			}
		}		
	}
	
	
	
	public Principal loginSystemUser() {
		Principal p = null;
		if( loginContext == null ) {
			log.info( "Setting principal..." );
//			System.setProperty( "java.security.auth.login.config", "C:\\svnroot\\juwimm-cms\\core\\src\\test\\jaas.conf" );
			String encoded = "e";
			SimpleCallbackHandler simpleCallbackHandler = new SimpleCallbackHandler( SYSTEM_USER, encoded );
			try {
				loginContext = new LoginContext( "juwimm-cms-security-domain", simpleCallbackHandler );
				loginContext.login();
				Subject s = loginContext.getSubject();
				Iterator it = s.getPrincipals().iterator();
				if( !s.getPrincipals().isEmpty() ) {
					p = (Principal) it.next();
					org.andromda.spring.PrincipalStore.set( p );
				}
			} catch ( LoginException e ) {
				
				if( log.isErrorEnabled() ) {
					log.error( "Could not login: " + e.getMessage(), e );
				}
			}
		}
		return p;
	}
	
	public Object getBean( String mappedName ) {
		loginSystemUser();
		return applicationContext.getBean( mappedName );
	}
	
	public Subject getSubject() {
		if( loginContext != null ) {
			return loginContext.getSubject();
		}
		return null;
	}
	
	protected UserHbmDao getUserDao() {
		if( userDao == null ) {
			userDao = (UserHbmDao) getApplicationContext().getBean( USER_DAO );			
		}
		return userDao;
	}
	
	protected GroupHbmDao getGroupDao() {
		if( groupDao == null ) {
			groupDao = (GroupHbmDao) getApplicationContext().getBean( GROUP_DAO );
		}
		return groupDao;
	}
	
	protected TaskHbmDao getTaskDao() {
		if( taskDao == null ) {
			taskDao = (TaskHbmDao) getApplicationContext().getBean( TASK_DAO );
		}
		return taskDao;
	}
	
	protected HostHbmDao getHostDao() {
		if( hostDao == null ) {
			hostDao = (HostHbmDao) getApplicationContext().getBean( HOST_DAO );
		}
		return hostDao;
	}
	
	protected ViewComponentHbmDao getViewComponentDao() {
		if( viewComponentDao == null ) {
			viewComponentDao = (ViewComponentHbmDao) getApplicationContext().getBean( VIEWCOMPONENT_DAO );
		}
		return viewComponentDao;
	}
	
	protected SiteHbmDao getSiteDao() {
		if( siteDao == null ) {
			siteDao = (SiteHbmDao) getApplicationContext().getBean( SITE_DAO );
		}
		return siteDao;
	}
	
	protected ContentVersionHbmDao getContentVersionDao() {
		if( contentVersionDao == null ) {
			contentVersionDao = (ContentVersionHbmDao) getApplicationContext().getBean( CONTENT_VERSION_DAO );
		}
		return contentVersionDao;
	}
	
	protected ContentHbmDao getContentDao() {
		if( contentDao == null ) {
			contentDao = (ContentHbmDao) getApplicationContext().getBean( CONTENT_DAO );
		}
		return contentDao;
	}
	
	protected DocumentHbmDao getDocumentDao() {
		if( documentDao == null ) {
			documentDao = (DocumentHbmDao) getApplicationContext().getBean( DOCUMENT_DAO );
		}
		return documentDao;
	}
	
	protected PictureHbmDao getPictureDao() {
		if( pictureDao == null ) {
			pictureDao = (PictureHbmDao) getApplicationContext().getBean( PICTURE_DAO );
		}
		return pictureDao;
	}
	
	protected UnitHbmDao getUnitDao() {
		if( unitDao == null ) {
			unitDao = (UnitHbmDao) getApplicationContext().getBean( UNIT_DAO );
		}
		return unitDao;
	}
	
	protected ViewDocumentHbmDao getViewDocumentDao() {
		if( viewDocumentDao == null ) {
			viewDocumentDao = (ViewDocumentHbmDao) getApplicationContext().getBean( VIEW_DOCUMENT_DAO );
		}
		return viewDocumentDao;		
	}
	
	protected EditionHbmDao getEditionDao() {
		if( editionDao == null ) {
			editionDao = (EditionHbmDao) getApplicationContext().getBean( EDITION_DAO );
		}
		return editionDao;
	}
	
	protected SequenceHbmDao getSequenceGenerator() {
		SequenceHbmDao sequenceSession = (SequenceHbmDao) getApplicationContext().getBean( SEQUENCE_DAO );
		return sequenceSession;
	}
	
	protected PersonHbmDao getPersonDao() {
		if( personDao == null ) {
			personDao = (PersonHbmDao) getApplicationContext().getBean( PERSON_DAO );
		}
		return personDao;
	}
	
	protected AddressHbmDao getAddressDao() {
		if( addressDao == null ) {
			addressDao = (AddressHbmDao) getApplicationContext().getBean( ADDRESS_DAO );
		}
		return addressDao;
	}
	
	protected DepartmentHbmDao getDepartmentDao() {
		if( departmentDao == null ) {
			departmentDao = (DepartmentHbmDao) getApplicationContext().getBean( DEPARTMENT_DAO );
		}
		return departmentDao;
	}		
	
	protected TalktimeHbmDao getTalktimeDao() {
		if( talktimeDao == null ) {
			talktimeDao = (TalktimeHbmDao) getApplicationContext().getBean( TALKTIME_DAO );
		}
		return talktimeDao;
	}
	
	protected RealmSimplePwHbmDao getRealmSimplePwDao() {
		if( realmSimplePwDao == null ) {
			realmSimplePwDao = (RealmSimplePwHbmDao) getApplicationContext().getBean( REALM_SIMPLE_PW_DAO );
		}
		return realmSimplePwDao;
	}
	
	protected RealmSimplePwUserHbmDao getRealmSimplePwUserDao() {
		if( realmSimplePwUserDao == null ) {
			realmSimplePwUserDao = (RealmSimplePwUserHbmDao) getApplicationContext().getBean( REALM_SIMPLE_PW_USER_DAO );
		}
		return realmSimplePwUserDao;
	}
	
	protected RealmJaasHbmDao getRealmJaasDao() {
		if( realmJaasDao == null ) {
			realmJaasDao = (RealmJaasHbmDao) getApplicationContext().getBean( REALM_JAAS_DAO );
		}
		return realmJaasDao;
	}
	
	protected RealmLdapHbmDao getRealmLdapDao() {
		if( realmLdapDao == null ) {
			realmLdapDao = (RealmLdapHbmDao) getApplicationContext().getBean( REALM_LDAP_DAO );
		}
		return realmLdapDao;
	}
	
	protected RealmJdbcHbmDao getRealmJdbcDao() {
		if( realmJdbcDao == null ) {
			realmJdbcDao = (RealmJdbcHbmDao) getApplicationContext().getBean( REALM_JDBC_DAO );
		}
		return realmJdbcDao;		
	}
	
	protected LockHbmDao getLockDao() {
		if( lockDao == null ) {
			lockDao = (LockHbmDao) getApplicationContext().getBean( LOCK_DAO );
		}
		return lockDao;
	}
}