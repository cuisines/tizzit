package de.juwimm.cms.search.lucene;

public enum IndexingMode {
	/**
	 * Only indexes live pages and thus only returns search from live pages
	 */
	LIVE,
	/**
	 * Only indexes the last version of a page ignoring the live status of a
	 * page
	 */
	WORK,
	/**
	 * Duplicates the index by indexing both the working last version of a page
	 * and the live version of a page if there is one. Search should be done by
	 * using the "isLive" parameter of indexed pages
	 */
	BOTH;
}
