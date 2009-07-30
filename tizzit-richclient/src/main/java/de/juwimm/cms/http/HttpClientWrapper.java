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
package de.juwimm.cms.http;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.URIException;
import org.apache.log4j.Logger;

import de.juwimm.cms.common.HttpMessages;

/**
 * <p>
 * Title: ConQuest
 * </p>
 * <p>
 * Description: Enterprise Content Management
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Revision: 1.1 $
 */
public final class HttpClientWrapper extends de.juwimm.cms.common.http.HttpClientWrapper {
    private static Logger log = Logger.getLogger(HttpClientWrapper.class);
    private static HttpClientWrapper instance = null;

    public static HttpClientWrapper getInstance() {
	if (instance == null) {
	    instance = new HttpClientWrapper();
	}
	return instance;
    }

    private HttpClientWrapper() {
	super();
    }

    public void testAndConfigureConnection(String testUrlString) throws HttpException {
	testAndConfigureConnection(testUrlString, null, null);
    }

    /**
     * 
     * @param testUrlString
     *                destination
     * @param userName
     *                for authentication at testUrlString NOT for proxy
     * @param password
     *                for authentication at testUrlString NOT for proxy
     * @throws HttpException
     */
    public void testAndConfigureConnection(String testUrlString, String userName, String password) throws HttpException {
	URL testURL = null;
	try {
	    testURL = new URL(testUrlString);
	} catch (MalformedURLException exe1) {
	    throw new HttpException(HttpMessages.getString("HttpClientWrapper.testConnectionFailed", testUrlString, "\n"));
	}

	DlgUsernamePassword dlg = new DlgUsernamePassword();
	if ((getHttpProxyUser() == null || "".equalsIgnoreCase(getHttpProxyUser()))
		&& getHttpProxyPassword() == null || "".equalsIgnoreCase(getHttpProxyPassword())) {
	    dlg.getTxtUsername().setText(System.getProperty("user.name"));
	} else {
	    dlg.getTxtUsername().setText(getHttpProxyUser());
	}
	dlg.getTxtPassword().setText(getHttpProxyPassword());
	// dlg.getTxtNTDomain().setText(httpProxyNTDomain);

	// try no auth, base auth, ntlm auth with user giving username and
	// password until successful
	while (true) {
	    try {

		testAndConfigureConnectionTryInvoke(testURL, userName, password);
		// save password only if connect successful
		saveProperties(dlg.getCboSave().isSelected());
		break;
	    } catch (URIException exe) {
		// http-Error-Code: 407 = Proxy Authentication Required
		if (exe.getReasonCode() == 407) {
		    // ask user for user and password
		    dlg.setVisible(true);
		    if (dlg.isCanceled()) {
			throw new HttpException(HttpMessages.getString("HttpClientWrapper.noProxyWhereNeededExeption"));
		    }
		    setHttpProxyUser(dlg.getTxtUsername().getText());
		    setHttpProxyPassword(String.copyValueOf(dlg.getTxtPassword().getPassword()));
		    // httpProxyNTDomain = dlg.getTxtNTDomain().getText();
		} else {
		    throw new HttpException(HttpMessages.getString("HttpClientWrapper.testConnectionFailed", testURL.getHost(), exe.getMessage()));
		}
	    }
	}
	log.debug("finished test");
    }

    /**
     * tries first with no auth toherwise first with non-ntlm proxy if still
     * http-error-code 407 with ntlm-proxy
     * 
     * @param testURL
     * @param userName
     *                for testURL authentication NOT for proxy
     * @param password
     *                for testURL authentication NOT for proxy
     * @throws HttpException
     */
    private void testAndConfigureConnectionTryInvoke(URL testURL, String userName, String password) throws HttpException {
	HttpMethodBase method = null;
	try {
	    /*
	     * At first Im trying here with default settings, so no proxy auth,
	     * proxy if given and or proxy auth with username and password auth.
	     */
	    method = invoke(testURL, userName, password);
	} catch (URIException exe) {
	    if (exe.getReasonCode() == 407) {
		log.info("Proxy needs authorization to be configured");
		if (this.getHttpProxyUser() != null) {
		    try {
			/*
			 * Now I want to try the proxy with NTLM
			 * authentification. I can not figure out if I can use
			 * NTLM, so we're trying it.
			 */
			this.setUseNTproxy(true);
			method = invoke(testURL, null, null);
		    } catch (URIException exe2) {
			if (exe2.getReasonCode() == 407) {
			    /*
			     * Something went wrong - in general username /
			     * password pair does not match - in this case this
			     * could also be a wrong NT-DOMAIN.
			     */
			    this.setUseNTproxy(false);
			    throw exe2;
			}
			throw new HttpException(HttpMessages.getString("HttpClientWrapper.testConnectionFailed", testURL.getHost(), exe2.getMessage()));
		    }
		} else {
		    log.info("...but first you have to enter one");
		    this.setUseNTproxy(false);
		    throw exe;
		}
	    } else {
		throw new HttpException(HttpMessages.getString("HttpClientWrapper.testConnectionFailed", testURL.getHost(), exe.getMessage()));
	    }
	} finally {
	    if (method != null) {
		method.releaseConnection();
	    }
	}
    }
}