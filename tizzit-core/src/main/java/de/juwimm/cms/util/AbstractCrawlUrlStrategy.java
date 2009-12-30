package de.juwimm.cms.util;

import java.util.List;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * Used by the @see WebCrawlerService to see if a link is valid. Has 2 implementation: Filter and Protocol 
 * @version $Id$
 */
public abstract class AbstractCrawlUrlStrategy {
	private List<String> positives;
	private List<String> negatives;

	public AbstractCrawlUrlStrategy() {

	}

	public AbstractCrawlUrlStrategy(List<String> poz, List<String> neg) {
		this.positives = poz;
		this.negatives = neg;
	}

	public List<String> getNegatives() {
		return negatives;
	}

	public void setNegatives(List<String> negatives) {
		this.negatives = negatives;
	}

	public List<String> getPositives() {
		return positives;
	}

	public void setPositives(List<String> positives) {
		this.positives = positives;
	}

	public abstract boolean match(String url, String pattern);

	/**
	 * Cases
	 * 1. Positive list with data - Negative list with data
	 * 2. Positive list with data - Negative list empty
	 * 3. Positive list empty 	  - Negative list with data
	 * 4. Positive list empty	  - Negative list empty
	 * 
	 * 1  - url must match positive strings and not match any of the negative strings
	 * 2  - url must match positive strings
	 * 3 - url must not match any string from negative strings
	 * 4 - url has no constrains at all  
	 * 
	 * for any changes write test in @see CrawlUrlStrategyTest
	 * @param url
	 * @return
	 */
	public boolean isUrlValid(String url) {
		boolean emptyPositives = positives == null || positives.size() == 0;
		boolean emptyNegatives = negatives == null || negatives.size() == 0;
		if (emptyPositives && emptyNegatives) {
			//no restrictions
			return true;
		}

		if (!emptyPositives) {
			return contains(url, positives) && !contains(url, negatives);
		} else {
			return !contains(url, negatives);
		}
	}

	private boolean contains(String url, List<String> patterns) {
		for (String pattern : patterns) {
			if (match(url, pattern)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Filter strings can be part of an url  
	 * @author fzalum
	 *
	 */
	public static class FilterCrawlUrlStrategy extends AbstractCrawlUrlStrategy {

		public FilterCrawlUrlStrategy() {

		}

		public FilterCrawlUrlStrategy(List<String> poz, List<String> neg) {
			super(poz, neg);

		}

		@Override
		public boolean match(String url, String pattern) {
			return url.contains(pattern);
		}

	}

	/**
	 * Protocol strings can be only prefixes of an url
	 * @author fzalum
	 *
	 */
	public static class ProtocolCrawlUrlStrategy extends AbstractCrawlUrlStrategy {

		public ProtocolCrawlUrlStrategy() {

		}

		public ProtocolCrawlUrlStrategy(List<String> poz, List<String> neg) {
			super(poz, neg);
		}

		@Override
		public boolean match(String url, String pattern) {
			return url.startsWith(pattern);
		}

	}
}
