/**
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

import java.io.Serializable;
import java.util.Collection;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @author <a href="mailto:irbouh@gmail.com">Omar Irbouh</a>
 * @author <a href="mailto:daniel.murygin@carano.de">Daniel Murygin</a>
 * @author <a href="mailto:j2ee@juwimm.com">Sascha-Matthias Kulawik</a>
 * @since 2004.10.07
 */
public class MethodCacheInterceptor implements MethodInterceptor, InitializingBean {
	private static final Log log = LogFactory.getLog(MethodCacheInterceptor.class);

	private CacheManager cacheManager;
	private String defaultCacheName;

	public String getDefaultCacheName() {
		return defaultCacheName;
	}

	public void setDefaultCacheName(String defaultCacheName) {
		this.defaultCacheName = defaultCacheName;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	/**
	 * Checks if required attributes are provided.
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(cacheManager, "A cache-manager is required. Use Manager(CacheManager) to provide one.");
	}

	/**
	 * main method caches method result if method is configured for caching method results must be serializable
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		String targetName = invocation.getThis().getClass().getName();
		String methodName = invocation.getMethod().getName();
		Object[] arguments = invocation.getArguments();
		String invocationClassName = invocation.getThis().getClass().getName();
		Object result = null;

		Cache cache = getCache(invocation);
		Element element = null;
		String cacheKey = getCacheKey(targetName, methodName, arguments);
		if (cache != null) {
			element = cache.get(cacheKey);
		} else if (log.isInfoEnabled()) {
			log.info("Cache not found in cache manager for class: " + invocationClassName);
		}
		if (element == null) {
			// call target/sub-interceptor
			if (log.isTraceEnabled()) {
				log.trace("Calling intercepted method of class: " + invocationClassName);
			}
			result = invocation.proceed();

			boolean isEmptyCollection = false;
			if (result != null && result instanceof Collection && ((Collection) result).isEmpty()) {
				isEmptyCollection = true;
			}

			if (cache != null && result != null && !isEmptyCollection) {
				// cache method result
				if (log.isDebugEnabled()) {
					log.debug("Caching result in cache: " + cache.getName() + ", using key: " + cacheKey);
				}
				element = new Element(cacheKey, (Serializable) result);
				cache.put(element);
			}
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Result found in cache: " + cache.getName() + ", using key: " + cacheKey);
			}
			result = element.getObjectValue();
		}
		return result;
	}

	/**
	 * creates cache name: targetName.methodName(argument1-class-name,argument2-class-name...)
	 * if argumentsClasses is null or empty name is: targetName.methodName()
	 */
	private Cache getCache(MethodInvocation invocation) {
		String methodName = invocation.getMethod().getName();
		Class< ? >[] argumentsClasses = invocation.getMethod().getParameterTypes();

		Class< ? > clazz = invocation.getThis().getClass();
		String name = getCacheName(clazz, methodName, argumentsClasses);
		if (log.isTraceEnabled()) {
			log.trace("Looking for cache with name: " + name);
		}
		Cache cache = getCacheManager().getCache(name);
		// get default
		if(cache == null) {
			cache = getCacheManager().getCache(defaultCacheName);
		}
		if (log.isTraceEnabled() && cache != null) {
			log.trace("Cache found: " + cache.getName());
		}
		return cache;
	}

	private String getCacheName(Class clazz, String methodName, Class< ? >[] argumentsClasses) {
		StringBuffer sb = new StringBuffer();
		sb.append(clazz.getName()).append(".").append(methodName).append("(");
		if ((argumentsClasses != null) && (argumentsClasses.length != 0)) {
			for (int i = 0; i < argumentsClasses.length; i++) {
				sb.append(argumentsClasses[i].getName());
				if ((i + 1) < argumentsClasses.length) {
					sb.append(",");
				}
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
 	/**
	 * creates cache key: targetName.methodName.argument0.argument1...
	 */
	private String getCacheKey(String targetName, String methodName, Object[] arguments) {
		StringBuffer sb = new StringBuffer();
		sb.append(targetName).append(".").append(methodName);
		if ((arguments != null) && (arguments.length != 0)) {
			for (int i = 0; i < arguments.length; i++) {
				sb.append(".").append(arguments[i]);
			}
		}
		return sb.toString();
	}
 
}