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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import de.juwimm.cms.authorization.SimpleCallbackHandler;
import de.juwimm.cms.model.ContentHbm;
import de.juwimm.cms.model.ContentVersionHbm;
import de.juwimm.cms.model.DocumentHbm;
import de.juwimm.cms.model.HostHbm;
import de.juwimm.cms.model.LockHbm;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.model.TaskHbm;
import de.juwimm.cms.model.ViewComponentHbm;
import de.juwimm.cms.model.ViewDocumentHbm;
import de.juwimm.cms.safeguard.model.Realm2viewComponentHbm;

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
		Collection<GrantedAuthority> grantedAuthorities=new ArrayList<GrantedAuthority>();
		grantedAuthorities.add(new GrantedAuthorityImpl("testRole"));
		Authentication token = new AnonymousAuthenticationToken("testUser", "testUser", grantedAuthorities);
		secureContext.setAuthentication(token);
		SecurityContextHolder.setContext(secureContext);
	}
	
	public void insertSite(SiteHbm site) {
		getJdbcTemplate().update(String.format("insert into site " + "(site_id,site_name,site_short,mandator_dir,WYSIWYG_IMAGE_URL,HELP_URL,DCF_URL,PREVIEW_URL_LIVE_SERVER,PREVIEW_URL_WORK_SERVER,PAGE_NAME_FULL,PAGE_NAME_CONTENT,PAGE_NAME_SEARCH,LAST_MODIFIED_DATE,UPDATE_SITE_INDEX, EXTERNAL_SITE_SEARCH) values " + "(%d,'%s','%s','c:/mandatorDir','WYSIWYG_IMAGE_URL','HELP_URL','DCF_URL','PREVIEW_URL_LIVE_SERVER','PREVIEW_URL_WORK_SERVER','page.html','content.html','search.html',0,0,0)", site.getSiteId(), site.getName(), site.getName()));
	}
	
	public void insertUnit(UnitHbm unit) {
		getJdbcTemplate().update(String.format("insert into unit (unit_id,name,last_Modified_date,site_id_fk, colour) " + "values (%d,'%s',0,%d,'testColour')", unit.getUnitId(), unit.getName(), unit.getSite().getSiteId()));
	}
	
	public void insertHost(HostHbm hostHbm) {
		getJdbcTemplate().update(String.format("insert into host " + "(host_name,site_id_fk,unit_id_fk,redirect_url,liveserver) values " + "('testHost',%d,%d,'%s',%b)", hostHbm.getSite().getSiteId(), hostHbm.getUnit().getUnitId(), hostHbm.getRedirectUrl(), hostHbm.isLiveserver()));
	}
	
	public void insertViewDocument(ViewDocumentHbm viewDocument) {
		getJdbcTemplate().update(String.format("insert into viewdocument (view_document_id,language,view_type,site_id_fk) " + "values (%d,'%s','%s',%d)", viewDocument.getViewDocumentId(), viewDocument.getLanguage(), viewDocument.getViewType(), viewDocument.getSite().getSiteId()));
	}
	
	public void insertUser(String userName, Integer activeSiteId) {
		getJdbcTemplate().update(String.format("insert into usr " + "(user_id,first_name,last_name,email,login_date,active_site_id_fk,masterRoot,passwd) values " + "('%s','%s','%s','%s',0,%d,1,'123')", userName, userName, userName, userName + "@juwimm.de", activeSiteId));
	}

	public void insertLock(LockHbm lock) {
		getJdbcTemplate().update(String.format("insert into locks (lock_id,create_date,owner_id_fk) " + "values (%d,'0','testUser')", lock.getLockId()));
	}

	public void insertViewComponent(ViewComponentHbm viewComponent) {
		getJdbcTemplate().update(String.format("insert into viewcomponent" + "(view_component_id,status,show_type,view_type,visible,search_indexed,xml_search_indexed,display_link_name,link_description,url_link_name,approved_link_name,online_start,online_stop,reference,online_state,deploy_command,meta_data,meta_description,view_document_id_fk,create_date,last_modified_date,display_settings,user_last_modified_date) " + "values (%d,0,3,1,1,1,1,'%s','%s','%s','%s',0,0,'%s',0,0,'%s','%s',%d,0,0,0,0)", viewComponent.getViewComponentId(), viewComponent.getDisplayLinkName(), viewComponent.getLinkDescription(), viewComponent.getUrlLinkName(), viewComponent.getApprovedLinkName(), viewComponent.getReference(), viewComponent.getMetaData(), viewComponent.getMetaDescription(), viewComponent.getViewDocument().getViewDocumentId()));
	}

	public void insertContent(ContentHbm content) {
		getJdbcTemplate().update(String.format("insert into content (content_id,status,template,UPDATE_SEARCH_INDEX) " + "values (%d,%d,'%s',%b)", content.getContentId(), content.getStatus(), content.getTemplate(), content.isUpdateSearchIndex()));
	}
	
	public void insertDocument(DocumentHbm document) {
		getJdbcTemplate().update(String.format("insert into document" + "(document_id,mime_type,time_stamp,document_name,use_count_last_version,use_count_publish_version,update_Search_Index,unit_id_fk,searchable) " + "values (%d,'%s',0,'%s',1,1,0,%d,%b)", document.getDocumentId(), document.getMimeType(), document.getDocumentName(), document.getUnit().getUnitId(), document.isSearchable()));
	}

	public void insertContentVersion(ContentVersionHbm contentVersion, Integer contentId) {
		getJdbcTemplate().update(String.format("insert into contentversion (content_version_id,version,heading,text,create_date,creator,content_id_fk) " + "values (%d,'%s','%s','%s',%d,'%s',%d)", contentVersion.getContentVersionId(), contentVersion.getVersion(), contentVersion.getHeading(), contentVersion.getText(), contentVersion.getCreateDate(), contentVersion.getCreator(), contentId));
	}
	
	public void insertContentVersion(ContentVersionHbm contentVersion) {
		getJdbcTemplate().update(String.format("insert into contentversion (content_version_id,version,heading,text,create_date,creator) " + "values (%d,'%s','%s','%s',%d,'%s')", contentVersion.getContentVersionId(), contentVersion.getVersion(), contentVersion.getHeading(), contentVersion.getText(), contentVersion.getCreateDate(), contentVersion.getCreator()));
	}

	public void insertRealm2viewComponent(Realm2viewComponentHbm realm) {
		getJdbcTemplate().update(String.format("insert into realm2view_component (realm2view_component_id,role_needed,view_component_id_fk,login_page_id_fk) " + "values (%d,'%s',1,1)", realm.getRealm2viewComponentId(), realm.getRoleNeeded()));
	}
	
	public void insertTask(TaskHbm task) {
		getJdbcTemplate().update(String.format("insert into task " + "(task_id,task_type,receiver_role,unit_id_fk,receiver,sender,user_comment,status,creation_date) values " + "(%d,1,'receiverRole',%d,'%s','%s','comment',1,1)", task.getTaskId(), task.getUnit().getUnitId(), task.getReceiver().getUserId(), task.getSender().getUserId()));
	}

}