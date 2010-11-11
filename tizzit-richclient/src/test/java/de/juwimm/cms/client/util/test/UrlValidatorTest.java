package de.juwimm.cms.client.util.test;

import static junit.framework.Assert.assertEquals;
import static org.powermock.api.easymock.PowerMock.createNiceMock;
import static org.powermock.api.easymock.PowerMock.createStrictMock;
import static org.powermock.api.easymock.PowerMock.expectLastCall;
import static org.powermock.api.easymock.PowerMock.expectStrictNew;
import static org.powermock.api.easymock.PowerMock.expectNew;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.util.UrlValidator;

@RunWith(PowerMockRunner.class)
@PrepareForTest( {URL.class, UrlValidator.class})
public class UrlValidatorTest {

	@Before
	public void setUp() {
		Constants.rb = ResourceBundle.getBundle("CMS", Constants.CMS_LOCALE);
	}

	@Test
	public void testValidateHappyFlow() throws Exception {
		String testLink = "igoogle.com";

		URL url = createStrictMock(URL.class);
		HttpURLConnection conn = createNiceMock(HttpURLConnection.class);

		expectStrictNew(URL.class, "http://" + testLink).andReturn(url);
		url.openConnection();
		expectLastCall().andReturn(conn);
		conn.setConnectTimeout(2000);
		expectLastCall();
		conn.setReadTimeout(5000);
		expectLastCall();
		conn.setInstanceFollowRedirects(true);
		expectLastCall();
		conn.setRequestProperty("User-agent", "crawler");
		expectLastCall();
		conn.connect();
		expectLastCall();
		conn.getResponseCode();
		expectLastCall().andReturn(200);
		conn.getURL();
		expectLastCall().andReturn(url);
		url.toExternalForm();
		expectLastCall().andReturn(testLink);

		replayAll();
		String actual = UrlValidator.validate(testLink);
		assertEquals(testLink, actual);
		verifyAll();
	}

	@Test
	public void testValidateInvalidUrl() throws Exception {
		String testLink = "cd+45";

		URL url = createStrictMock(URL.class);
		HttpURLConnection conn = createNiceMock(HttpURLConnection.class);

		expectStrictNew(URL.class, "http://" + testLink).andReturn(url);
		url.openConnection();
		expectLastCall().andThrow(new MalformedURLException());

		replayAll();
		String actual = UrlValidator.validate(testLink);
		assertEquals("Invalid URL", actual);
		verifyAll();
	}
}
