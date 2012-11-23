package de.juwimm.cms.remote.test;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

import org.junit.Ignore;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.authentication.AnonymousAuthenticationToken;

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
		Collection<GrantedAuthority> grantedAuthorities=new ArrayList<GrantedAuthority>();
		grantedAuthorities.add(new GrantedAuthorityImpl("testRole"));
		Authentication token = new AnonymousAuthenticationToken("testUser", "testUser", grantedAuthorities);
		secureContext.setAuthentication(token);
		SecurityContextHolder.setContext(secureContext);
	}

}
