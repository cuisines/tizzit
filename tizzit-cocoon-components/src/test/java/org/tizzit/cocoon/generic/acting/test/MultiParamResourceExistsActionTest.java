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
package org.tizzit.cocoon.generic.acting.test;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.SitemapComponentTestCase;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.components.source.SourceResolverAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.excalibur.source.SourceResolver;
import org.tizzit.cocoon.generic.acting.MultiParamResourceExistsAction;
import org.tizzit.cocoon.generic.helper.MapHelper;

// TODO: Class description
/**
 *
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since tizzit-cocoon-components 11.11.2009
 */
public class MultiParamResourceExistsActionTest extends SitemapComponentTestCase {
	private static final Log log = LogFactory.getLog(MultiParamResourceExistsActionTest.class);

	private AbstractAction action = null;
	private SourceResolver resolver = null;

	@Override
	public void setUp() throws Exception {
		super.setUp();

		this.action = (MultiParamResourceExistsAction) this.getBeanFactory().getBean("org.apache.cocoon.acting.Action/multiParamResourceExistsAction");

		//		Object a = this.lookup(CocoonSourceResolver.ROLE);

		try {
			this.resolver = (SourceResolver) this.lookup(SourceResolver.ROLE);
		} catch (Exception exe) {
			log.error(exe.getMessage(), exe);
			assertTrue(false);
		}

		assertNotNull(this.action);
		assertNotNull(this.resolver);
	}

	public void testSimpleCheckResource() throws Exception {
		if (log.isDebugEnabled()) log.debug("testSimpleCheckResource() -> begin");
		Parameters params = new Parameters();
		params.setParameter("checkResource-foo", "foo/bar.xml");

		Map< ? , ? > result = this.action.act(this.getRedirector(), new SourceResolverAdapter(this.resolver, this.getManager()), this.getObjectModel(), null, params);

		assertEquals("foo/common.xml", result.get("foo"));
		if (log.isDebugEnabled()) log.debug("testSimpleCheckResource() -> end");
	}

	public void testSimpleCheckResources() throws Exception {
		if (log.isDebugEnabled()) log.debug("testSimpleCheckResources() -> begin");
		Parameters params = new Parameters();
		params.setParameter("checkResource-foo", "foo.xml");
		params.setParameter("checkResource-bar", "bar.xml");
		params.setParameter("checkResource-hello", "hello.xml");
		params.setParameter("checkResource-world", "world.xml");
		params.setParameter("checkResource-anExistingFile", prepandPath("dummy.xml"));
		params.setParameter("checkResource-fooBar", "foo/bar.xml");

		Map< ? , ? > result = this.action.act(this.getRedirector(), new SourceResolverAdapter(this.resolver, this.getManager()), this.getObjectModel(), null, params);

		assertEquals("common.xml", result.get("foo"));
		assertEquals("common.xml", result.get("bar"));
		assertEquals("common.xml", result.get("hello"));
		assertEquals("common.xml", result.get("world"));
		assertEquals(prepandPath("dummy.xml"), result.get("anExistingFile"));
		assertEquals("foo/common.xml", result.get("fooBar"));

		if (log.isDebugEnabled()) log.debug("testSimpleCheckResources() -> end");
	}

	public void testSimpleCheckResourceWithDefaultValue() throws Exception {
		if (log.isDebugEnabled()) log.debug("testSimpleCheckResourceWithDefaultValue() -> begin");
		Parameters params = new Parameters();
		params.setParameter("checkResource-foo", "foo/bar.xml");
		params.setParameter("defaultValue-foo", "foo/myDefaultValue.xml");

		Map< ? , ? > result = this.action.act(this.getRedirector(), new SourceResolverAdapter(this.resolver, this.getManager()), this.getObjectModel(), null, params);

		log.info(MapHelper.mapToString(result));

		assertEquals("foo/myDefaultValue.xml", result.get("foo"));
		if (log.isDebugEnabled()) log.debug("testSimpleCheckResourceWithDefaultValue() -> end");
	}

	public void testComplexCheckResource() throws Exception {
		if (log.isDebugEnabled()) log.debug("testComplexCheckResource() -> begin");
		Parameters params = new Parameters();
		params.setParameter("checkResource-foo-1", "foo/bar.xml");
		params.setParameter("checkResource-foo-2", "foo/bar/hello.xml");
		params.setParameter("checkResource-foo-3", "foo/bar/hello/world.xml");

		Map< ? , ? > result = this.action.act(this.getRedirector(), new SourceResolverAdapter(this.resolver, this.getManager()), this.getObjectModel(), null, params);

		assertEquals("foo/bar/hello/common.xml", result.get("foo"));
		if (log.isDebugEnabled()) log.debug("testComplexCheckResource() -> end");
	}

	public void testComplexCheckResourceWithAvailableResource() throws Exception {
		if (log.isDebugEnabled()) log.debug("testComplexCheckResourceWithAvailableResource() -> begin");
		Parameters params = new Parameters();
		params.setParameter("checkResource-foo-1", "foo/bar.xml");
		params.setParameter("checkResource-foo-2", "foo/bar/hello.xml");
		params.setParameter("checkResource-foo-3", prepandPath("dummy.xml"));

		Map< ? , ? > result = this.action.act(this.getRedirector(), new SourceResolverAdapter(this.resolver, this.getManager()), this.getObjectModel(), null, params);

		assertEquals(prepandPath("dummy.xml"), result.get("foo"));
		if (log.isDebugEnabled()) log.debug("testComplexCheckResourceWithAvailableResource() -> end");
	}

	public void testComplexCheckResourceWithAvailableResource2() throws Exception {
		if (log.isDebugEnabled()) log.debug("testComplexCheckResourceWithAvailableResource() -> begin");
		Parameters params = new Parameters();
		params.setParameter("checkResource-foo-1", "foo/bar.xml");
		params.setParameter("checkResource-foo-2", "foo/bar/hello.xml");
		params.setParameter("checkResource-foo-3", prepandPath("dummy.xml"));

		params.setParameter("checkResource-bar-1", "foo/bar.xml");
		params.setParameter("checkResource-bar-2", "foo/bar/hello.xml");
		params.setParameter("checkResource-bar-3", prepandPath("dummy.xml"));

		Map< ? , ? > result = this.action.act(this.getRedirector(), new SourceResolverAdapter(this.resolver, this.getManager()), this.getObjectModel(), null, params);

		assertEquals(prepandPath("dummy.xml"), result.get("foo"));
		assertEquals(prepandPath("dummy.xml"), result.get("bar"));
		if (log.isDebugEnabled()) log.debug("testComplexCheckResourceWithAvailableResource() -> end");
	}

	//TODO: externalize method
	private String prepandPath(String resource) {
		String packagePath = this.getClass().getPackage().getName().replaceAll("\\.", "/");
		return "src/test/resources/" + packagePath + "/" + resource;
	}
}
