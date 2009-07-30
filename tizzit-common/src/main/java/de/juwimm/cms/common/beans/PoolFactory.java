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
package de.juwimm.cms.common.beans;

import org.springframework.aop.target.CommonsPoolTargetSource;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
@ManagedResource
public class PoolFactory implements FactoryBean, ApplicationContextAware {
	private ApplicationContext applicationContext;
	private CommonsPoolTargetSource pool;

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void setPool(CommonsPoolTargetSource pool) {
		this.pool = pool;
	}

	public Object getObject() throws Exception {
		return getPool().getTarget();
	}

	public Class getObjectType() {
		return getPool().getTargetClass();
	}

	private CommonsPoolTargetSource getPool() {
		return pool;
	}

	public boolean isSingleton() {
		return false;
	}

	@ManagedAttribute(description = "Active count of pooled plugincaches")
	public int getActiveCount() {
		return getPool().getActiveCount();
	}

}