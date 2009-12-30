package de.juwimm.cms.beans.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import de.juwimm.cms.util.AbstractCrawlUrlStrategy.FilterCrawlUrlStrategy;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * 
 * @version $Id$
 */
public class CrawlUrlStrategyTest extends TestCase {

	FilterCrawlUrlStrategy filtersStrategy;
	List<String> positives;
	List<String> negatives;

	@Override
	protected void setUp() throws Exception {
		filtersStrategy = new FilterCrawlUrlStrategy();
	}

	public void test1() {
		filtersStrategy.setPositives(positives);
		filtersStrategy.setNegatives(negatives);
		Assert.assertTrue(filtersStrategy.isUrlValid("http://www.juwimm.net/"));
	}

	public void test2() {
		positives = new ArrayList<String>();
		negatives = new ArrayList<String>();
		filtersStrategy.setPositives(positives);
		filtersStrategy.setNegatives(negatives);
		Assert.assertTrue(filtersStrategy.isUrlValid("http://www.juwimm.net/"));
	}

	public void test3() {
		positives = new ArrayList<String>();
		negatives = new ArrayList<String>();

		positives.add("video");
		positives.add("contact");

		filtersStrategy.setPositives(positives);
		filtersStrategy.setNegatives(negatives);

		Assert.assertTrue(filtersStrategy.isUrlValid("http://www.juwimm.net/video"));
		Assert.assertTrue(filtersStrategy.isUrlValid("http://www.juwimm.net/video/contact"));
		Assert.assertTrue(filtersStrategy.isUrlValid("http://www.juwimm.net/contact"));
		Assert.assertFalse(filtersStrategy.isUrlValid("http://www.juwimm.net/"));
		Assert.assertTrue(filtersStrategy.isUrlValid("http://www.juwimm.net/contact/deutsch/Presse"));

		negatives.add("deutsch/Presse");

		Assert.assertTrue(filtersStrategy.isUrlValid("http://www.juwimm.net/video"));
		Assert.assertTrue(filtersStrategy.isUrlValid("http://www.juwimm.net/video/contact"));
		Assert.assertTrue(filtersStrategy.isUrlValid("http://www.juwimm.net/contact"));
		Assert.assertFalse(filtersStrategy.isUrlValid("http://www.juwimm.net/deutsch/Presse"));
		Assert.assertFalse(filtersStrategy.isUrlValid("http://www.juwimm.net/contact/deutsch/Presse"));

	}

	public void test4() {
		positives = new ArrayList<String>();
		negatives = new ArrayList<String>();

		negatives.add("video");
		negatives.add("contact");
		negatives.add("deutsch/Presse");

		filtersStrategy.setPositives(positives);
		filtersStrategy.setNegatives(negatives);

		Assert.assertFalse(filtersStrategy.isUrlValid("http://www.juwimm.net/video"));
		Assert.assertFalse(filtersStrategy.isUrlValid("http://www.juwimm.net/video/contact"));
		Assert.assertFalse(filtersStrategy.isUrlValid("http://www.juwimm.net/contact"));
		Assert.assertTrue(filtersStrategy.isUrlValid("http://www.juwimm.net/"));

	}
}
