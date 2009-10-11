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
package org.tizzit.core.classloading;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

// TODO: Class description
/**
 * This servlet builds a classloading sandbox and runs another servlet inside
 * that sandbox. The purpose is to use the reloading classloader to load the
 *
 * <p>
 * This servlet propagates all initialisation parameters to the sandboxed
 * servlet, and requires the parameter <code>servlet-class</code>.
 * <ul>
 * <li><code>servlet-class</code> defines the sandboxed servlet class.</li>
 * </ul>
 *
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since tizzit-core 07.10.2009
 */
public class ExternalLibClassLoaderInjectServlet extends HttpServlet {
	private static Logger log = Logger.getLogger(ExternalLibClassLoaderInjectServlet.class);
	private static final long serialVersionUID = 8543646073528093241L;

	protected Servlet servlet;
	protected ServletContext context;

	/**
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.context = config.getServletContext();

		String servletName = config.getInitParameter("servlet-class");
		if (servletName == null) { throw new ServletException("ReloadingServlet: Init-Parameter 'servlet-class' is missing."); }

		// Create the servlet
		try {
			ClassLoader cl = ExternalLibClassLoaderManager.getClassLoader();
			Class< ? > servletClass = cl.loadClass(servletName);
			this.servlet = (Servlet) servletClass.newInstance();
		} catch (Exception e) {
			throw new ServletException("Cannot load servlet " + servletName, e);
		}

		// Always set the context classloader. JAXP uses it to find a
		// ParserFactory,
		// and thus fails if it's not set to the webapp classloader.
		final ClassLoader old = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(ExternalLibClassLoaderManager.getClassLoader());

			// Inlitialize the actual servlet
			this.servlet.init(this.getServletConfig());
		} finally {
			Thread.currentThread().setContextClassLoader(old);
		}

	}

	/**
	 * Service the request by delegating the call to the real servlet
	 */
	@Override
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		final ClassLoader old = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(ExternalLibClassLoaderManager.getClassLoader());

			this.servlet.service(request, response);
		} catch (Throwable t) {
			//log.error("Error in servlet", t); will be already logged by "cocoon.access" logger
		} finally {
			Thread.currentThread().setContextClassLoader(old);
		}
	}

	/**
	 * Destroy the actual servlet
	 */
	@Override
	public void destroy() {
		if (this.servlet != null) {
			final ClassLoader old = Thread.currentThread().getContextClassLoader();
			try {
				Thread.currentThread().setContextClassLoader(ExternalLibClassLoaderManager.getClassLoader());
				this.servlet.destroy();
			} finally {
				Thread.currentThread().setContextClassLoader(old);
			}
		}
		super.destroy();
	}
}
