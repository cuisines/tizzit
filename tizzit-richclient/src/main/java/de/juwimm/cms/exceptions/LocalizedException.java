package de.juwimm.cms.exceptions;

import static de.juwimm.cms.common.Constants.rb;

import org.apache.log4j.Logger;


/**
 * Class that throws internationalized messages
 * 
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class LocalizedException extends Exception {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(LocalizedException.class);
	private String key;
	
	public LocalizedException(String key,String message, Exception cause){
		super(message,cause);
		this.key = key;
	}
	
	public void logThrowException(){
		log.error("LocalizedException: " + (key!=null?getLocalizedMessage():getMessage()) + getCause()!= null?", "+getCause().getMessage():"");
	}
	
	@Override
	public String getLocalizedMessage() {
		return rb.getString(key);
	}
	
}
