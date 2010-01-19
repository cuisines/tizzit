package de.juwimm.cms.remote.test;

import junit.framework.TestCase;

import org.junit.Ignore;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.context.SecurityContextImpl;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationToken;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
@Ignore
public class AbstractServiceTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		//Mocking the security context
		//Now AuthenticationHelper.getUserName will return "testUser"
		SecurityContextImpl secureContext = new SecurityContextImpl();
		Authentication token = new AnonymousAuthenticationToken("testUser", "testUser", new GrantedAuthority[] {new GrantedAuthorityImpl("testRole")});
		secureContext.setAuthentication(token);
		SecurityContextHolder.setContext(secureContext);
	}

}
