/*
 * Copyright (c) 2002-2009 Juwi MacMillan Group GmbH (JuwiMM)
 * Bockhorn 1, 29664 Walsrode, Germany
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of JuwiMM
 * ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with JuwiMM.
 */
package org.tizzit.cocoon.generic.acting;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.acting.AbstractConfigurableAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Response;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.FastDateFormat;

/**
 * This action adds the <code>Last-Modified</code>, <code>Expires</code> and
 * <code>Cache-Control</code> HTTP headers to the response.
 *
 * <p>
 * This action will add the <code>Last-Modified</code> header to the response
 * with the time in which the request was executed, and an <code>Expires</code>
 * header at a specified time difference. Additionally, it will provide an
 * extra <code>Cache-Control</code> indicating the maximum age of the request
 * as a delta between the expiration and last modification dates.
 * </p>
 * <p>
 * This is useful (for example) when Cocoon is proxyied by a Web Server such
 * as Apache HTTPD running mod_cache, to indicate for each request how long
 * the output should be cached for.
 * </p>
 * <p>
 * To configure the difference between <code>Last-Modified</code> and
 * <code>Expires</code> this <code>Action</code> can be configured specifying
 * days, hours, minutes, and seconds in this way:
 * </p>
 * <pre>
 * &lt;map:action&gt;
 *   &lt;map:action name="xyz" src="de.juwimm.cocoon.components.acting.HttpCacheAction&gt;"
 *     &lt;days&gt;1&lt;/day&gt;
 *     &lt;hours&gt;2&lt;/hour&gt;
 *     &lt;minutes&gt;3&lt;/minute&gt;
 *     &lt;seconds&gt;4&lt;/second&gt;
 *   &lt;/map:actio&gt;
 * &lt;/map:action&gt;
 * </pre>
 * <p>
 * The configuration can be overridden specifying days, hours, minutes, and seconds in the following way.
 * The associated global setting will be used if one the parameter is missing.
 * </p>
 * <pre> &lt;map:act type="xyz"&gt;
 *   &lt;map:parameter name="days" value="1337" /&gt;
 *   &lt;map:parameter name="hours" value="1" /&gt;
 *   &lt;map:parameter name="minutes" value="33" /&gt;
 *   &lt;map:parameter name="seconds" value="7" /&gt;
 * &lt;/map:act&gt;</pre>
 * <p>
 * If the
 * </p>
 * <p>
 * Using this example configuration, the <code>Expires</code> header will
 * specify a date one day, two hours, three minutes and four seconds after
 * the time of the request (which will be in <code>Last-Modified</code>).
 * </p>
 * <p>
 * Note that if any of the parameters mentioned above is <b>zero</b> or
 * <b>less than zero</b> this action will modify the behaviour of the
 * resulting <code>Cache-Control</code> header to emit the keyword
 * <code>no-cache</code>.
 * </p>
 * <p>
 * This action will also return the three headers it added as sitemap
 * parameters called <code>last-modified</code>, <code>expires</code> and
 * <code>cache-control</code> (all lowercase).
 * </p>
 *
 * @cocoon.sitemap.component.documentation
 * This action adds the <code>Last-Modified</code>, <code>Expires</code> and
 * <code>Cache-Control</code> HTTP headers to the response.
 *
 * @author <a href="mailto:vgritsenko@apache.org">Vadim Gritsenko</a>
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since tizzit-cocoon-components 02.11.2009
 */
public class HttpCacheAction extends AbstractConfigurableAction implements ThreadSafe {

	private FastDateFormat formatter = null;
	int days = 0;
	int hours = 0;
	int minutes = 0;
	int seconds = 0;

	@Override
	public void configure(Configuration configuration) throws ConfigurationException {
		super.configure(configuration);

		// RFC-822 Date with a GMT based time zone
		this.formatter = FastDateFormat.getInstance("EEE, dd MMM yyyy kk:mm:ss zzz", DateUtils.UTC_TIME_ZONE);
		this.days = configuration.getChild("days").getValueAsInteger(0);
		this.hours = configuration.getChild("hours").getValueAsInteger(0);
		this.minutes = configuration.getChild("minutes").getValueAsInteger(0);
		this.seconds = configuration.getChild("seconds").getValueAsInteger(0);
	}

	@SuppressWarnings("unchecked")
	public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters parameters) throws Exception {

		this.overrideConfiguration(parameters);

		Response response = ObjectModelHelper.getResponse(objectModel);
		Calendar calendar = Calendar.getInstance(DateUtils.UTC_TIME_ZONE);
		Map values = new HashMap(3);

		/* Get the current time and output as the last modified header */
		String value = this.formatter.format(calendar);
		long maxage = calendar.getTime().getTime();
		response.setHeader("Last-Modified", value);
		values.put("last-modified", value);

		/* Advance the time as much as required */
		calendar.add(Calendar.DATE, this.days);
		calendar.add(Calendar.HOUR, this.hours);
		calendar.add(Calendar.MINUTE, this.minutes);
		calendar.add(Calendar.SECOND, this.seconds);

		/* Recalculate time and age to see what changed */
		maxage = calendar.getTime().getTime() - maxage;

		/* If we got more than one second everything is quite normal */
		if (maxage > 1000) {
			value = this.formatter.format(calendar);
			response.setHeader("Expires", value);
			values.put("expires", value);

			value = "max-age=" + Long.toString(maxage / 1000l);
			response.setHeader("Cache-Control", value);
			values.put("cache-control", value);

			/* If we got less than one second (even negatives) no cache */
		} else {
			/* We still hold the old value from Last-Modified here */
			response.setHeader("Expires", value);
			values.put("expires", value);

			response.setHeader("Cache-Control", "no-cache");
			values.put("cache-control", "no-cache");
		}

		/* Return the headers */
		return (Collections.unmodifiableMap(values));
	}

	private void overrideConfiguration(Parameters parameters) {
		this.days = parameters.getParameterAsInteger("days", this.days);
		this.hours = parameters.getParameterAsInteger("hours", this.hours);
		this.minutes = parameters.getParameterAsInteger("minutes", this.minutes);
		this.seconds = parameters.getParameterAsInteger("seconds", this.seconds);
	}
}