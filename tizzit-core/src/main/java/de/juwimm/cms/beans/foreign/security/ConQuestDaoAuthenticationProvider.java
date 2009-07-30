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
package de.juwimm.cms.beans.foreign.security;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.security.acl.Group;
import java.util.*;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.SpringSecurityException;
import org.springframework.security.context.HttpSessionContextIntegrationFilter;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.providers.AuthenticationProvider;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.providers.jaas.*;
import org.springframework.security.providers.jaas.event.JaasAuthenticationFailedEvent;
import org.springframework.security.providers.jaas.event.JaasAuthenticationSuccessEvent;
import org.springframework.security.ui.session.HttpSessionDestroyedEvent;
import org.springframework.util.Assert;

/**
 *
 * @author Ray Krueger
 * @version $Id$
 */
public class ConQuestDaoAuthenticationProvider implements AuthenticationProvider, ApplicationEventPublisherAware, InitializingBean, ApplicationListener, Serializable {
	private static final long serialVersionUID = -4744822348338254363L;
	private transient Log log = LogFactory.getLog(ConQuestDaoAuthenticationProvider.class);
	private transient LoginExceptionResolver loginExceptionResolver = new DefaultLoginExceptionResolver();
	private transient String loginContextName = "juwimm-cms-security-domain"; // TODO 09 Lateron this should be configurable
	private transient JaasAuthenticationCallbackHandler[] callbackHandlers = new JaasAuthenticationCallbackHandler[] {new org.springframework.security.providers.jaas.JaasNameCallbackHandler(), new org.springframework.security.providers.jaas.JaasPasswordCallbackHandler()};
	private transient ApplicationEventPublisher applicationEventPublisher;

	public void afterPropertiesSet() throws Exception {
		Assert.hasLength(loginContextName, "loginContextName must be set on " + getClass());
	}

	/**
	 * Attempts to login the user given the Authentication objects principal and credential
	 *
	 * @param auth The Authentication object to be authenticated.
	 *
	 * @return The authenticated Authentication object, with it's grantedAuthorities set.
	 *
	 * @throws AuthenticationException This implementation does not handle 'locked' or 'disabled' accounts. This method
	 *         only throws a AuthenticationServiceException, with the message of the LoginException that will be
	 *         thrown, should the loginContext.login() method fail.
	 */
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		if (auth instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken request = (UsernamePasswordAuthenticationToken) auth;

			try {
				//Create the LoginContext object, and pass our InternallCallbackHandler
				LoginContext loginContext = new LoginContext(loginContextName, new InternalCallbackHandler(auth));

				//Attempt to login the user, the LoginContext will call our InternalCallbackHandler at this point.
				loginContext.login();

				//create a set to hold the authorities, and add any that have already been applied.
				Set authorities = new HashSet();

				if (request.getAuthorities() != null) {
					authorities.addAll(Arrays.asList(request.getAuthorities()));
				}

				//get the subject principals and pass them to each of the AuthorityGranters
				Set principals = loginContext.getSubject().getPrincipals();

				authorities.add(new JaasGrantedAuthority("*", new AllPrincipal()));

				for (Iterator iterator = principals.iterator(); iterator.hasNext();) {
					Principal principal = (Principal) iterator.next();
					if (principal instanceof Group) {
						Group g = (Group) principal;
						if (g.members() != null) {
							Enumeration members = g.members();
							while (members.hasMoreElements()) {
								Principal object = (Principal) members.nextElement();
								authorities.add(new JaasGrantedAuthority(object.toString(), object));
							}
						} else {
							authorities.add(new JaasGrantedAuthority(g.toString(), g));
						}
					}
				}

				//Convert the authorities set back to an array and apply it to the token.
				JaasAuthenticationToken result = new JaasAuthenticationToken(request.getPrincipal(), request.getCredentials(), (GrantedAuthority[]) authorities.toArray(new GrantedAuthority[authorities.size()]), loginContext);

				//Publish the success event
				publishSuccessEvent(result);

				//we're done, return the token.
				return result;
			} catch (LoginException loginException) {
				SpringSecurityException ase = loginExceptionResolver.resolveException(loginException);

				publishFailureEvent(request, ase);
				throw ase;
			}
		}

		return null;
	}

	public static class AllPrincipal implements Principal, Serializable {
		private static final long serialVersionUID = 1L;

		public String getName() {
			return "*";
		}
	}

	/**
	 * Returns the current JaasAuthenticationCallbackHandler array, or null if none are set.
	 *
	 * @return the JAASAuthenticationCallbackHandlers.
	 *
	 * @see #setCallbackHandlers(JaasAuthenticationCallbackHandler[])
	 */
	public JaasAuthenticationCallbackHandler[] getCallbackHandlers() {
		return callbackHandlers;
	}

	public String getLoginContextName() {
		return loginContextName;
	}

	public LoginExceptionResolver getLoginExceptionResolver() {
		return loginExceptionResolver;
	}

	public void onApplicationEvent(ApplicationEvent applicationEvent) {
		if (applicationEvent instanceof HttpSessionDestroyedEvent) {
			HttpSessionDestroyedEvent event = (HttpSessionDestroyedEvent) applicationEvent;
			handleLogout(event);
		}
	}

	protected void handleLogout(HttpSessionDestroyedEvent event) {
		SecurityContext context = (SecurityContext) event.getSession().getAttribute(HttpSessionContextIntegrationFilter.SPRING_SECURITY_CONTEXT_KEY);

		if (context == null) {
			log.debug("The destroyed session has no SecurityContext");

			return;
		}

		Authentication auth = context.getAuthentication();

		if ((auth != null) && (auth instanceof JaasAuthenticationToken)) {
			JaasAuthenticationToken token = (JaasAuthenticationToken) auth;

			try {
				LoginContext loginContext = token.getLoginContext();

				if (loginContext != null) {
					log.debug("Logging principal: [" + token.getPrincipal() + "] out of LoginContext");
					loginContext.logout();
				} else {
					log.debug("Cannot logout principal: [" + token.getPrincipal() + "] from LoginContext. " + "The LoginContext is unavailable");
				}
			} catch (LoginException e) {
				log.warn("Error error logging out of LoginContext", e);
			}
		}
	}

	/**
	 * Publishes the {@link JaasAuthenticationFailedEvent}. Can be overridden by subclasses for different
	 * functionality
	 *
	 * @param token The {@link UsernamePasswordAuthenticationToken} being processed
	 * @param ase The {@link AcegiSecurityException} that caused the failure
	 */
	protected void publishFailureEvent(UsernamePasswordAuthenticationToken token, SpringSecurityException ase) {
		applicationEventPublisher.publishEvent(new JaasAuthenticationFailedEvent(token, ase));
	}

	/**
	 * Publishes the {@link JaasAuthenticationSuccessEvent}. Can be overridden by subclasses for different
	 * functionality.
	 *
	 * @param token The {@link UsernamePasswordAuthenticationToken} being processed
	 */
	protected void publishSuccessEvent(UsernamePasswordAuthenticationToken token) {
		applicationEventPublisher.publishEvent(new JaasAuthenticationSuccessEvent(token));
	}

	/**
	 * Set the JAASAuthentcationCallbackHandler array to handle callback objects generated by the
	 * LoginContext.login method.
	 *
	 * @param callbackHandlers Array of JAASAuthenticationCallbackHandlers
	 */
	public void setCallbackHandlers(JaasAuthenticationCallbackHandler[] callbackHandlers) {
		this.callbackHandlers = callbackHandlers;
	}

	/**
	 * Set the loginContextName, this name is used as the index to the configuration specified in the
	 * loginConfig property.
	 *
	 * @param loginContextName
	 */
	public void setLoginContextName(String loginContextName) {
		this.loginContextName = loginContextName;
	}

	public void setLoginExceptionResolver(LoginExceptionResolver loginExceptionResolver) {
		this.loginExceptionResolver = loginExceptionResolver;
	}

	public boolean supports(Class aClass) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass);
	}

	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	protected ApplicationEventPublisher getApplicationEventPublisher() {
		return applicationEventPublisher;
	}

	//~ Inner Classes ==================================================================================================

	/**
	 * Wrapper class for JAASAuthenticationCallbackHandlers
	 */
	private class InternalCallbackHandler implements CallbackHandler, Serializable {
		private static final long serialVersionUID = -366939099927300938L;
		private transient Authentication authentication;

		public InternalCallbackHandler(Authentication authentication) {
			this.authentication = authentication;
		}

		public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
			for (int i = 0; i < callbackHandlers.length; i++) {
				JaasAuthenticationCallbackHandler handler = callbackHandlers[i];

				for (int j = 0; j < callbacks.length; j++) {
					Callback callback = callbacks[j];

					handler.handle(callback, authentication);
				}
			}
		}
	}
}
