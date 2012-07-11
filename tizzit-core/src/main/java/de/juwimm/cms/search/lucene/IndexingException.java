package de.juwimm.cms.search.lucene;

public class IndexingException extends RuntimeException {
	public IndexingException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
