package de.juwimm.cms.util;

import java.net.HttpURLConnection;
import java.net.URL;
import static de.juwimm.cms.common.Constants.rb;

public class UrlValidator {
	public static String validate(final String urlLink) {
			int responseCode = -1;
			HttpURLConnection conn = null;
			try {
				URL url = null;
				if (!urlLink.startsWith("http")) {
					url = new URL("http://" + urlLink);
				} else {
					url = new URL(urlLink);
				}
				conn = (HttpURLConnection) url.openConnection();

				// Set up a request.
				conn.setConnectTimeout(2000); // 10 sec
				conn.setReadTimeout(5000); // 10 sec
				conn.setInstanceFollowRedirects(true);
				conn.setRequestProperty("User-agent", "crawler");

				// Send the request.
				conn.connect();

				// Get the response.
				responseCode = conn.getResponseCode();
			} catch (final Exception e) {
				if(conn!=null)conn.disconnect();
				return rb.getString("exception.invalidURL");
			}
			if (responseCode == 200) {
				String textUrl = conn != null ? conn.getURL().toExternalForm() : "";
				conn.disconnect();
				return textUrl;
			} else {
				conn.disconnect();
				return rb.getString("exception.invalidURL");
			}

	}
}
