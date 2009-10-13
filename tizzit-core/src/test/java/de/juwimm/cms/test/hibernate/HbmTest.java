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


public interface HbmTest{
	
	
	/**
	 * The implemented test cases compare an ejb service implementation
	 * with the new spring service implementation. The two necessary service
	 * objects should be instatiated within this method.
	 *
	 */
	void initializeServiceBeans();
	
	/**
	 * Some methods require special rights. Before testing these methods
	 * loginPrincipal can be called 
	 */
	Principal loginSystemUser();
	
	
	/**
	 * Login a "normal" user
	 */
	void loginUser( String username, String password );
	
	/**
	 * Get mapped EJB object from the spring context
	 * 
	 */
	Object getBean( String mappedName );
	
}
