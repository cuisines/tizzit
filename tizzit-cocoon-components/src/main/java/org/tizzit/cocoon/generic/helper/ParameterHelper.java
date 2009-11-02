package org.tizzit.cocoon.generic.helper;

/**
 *
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 */
public class ParameterHelper {

	/**
	 *
	 * @param paramValue
	 * @return
	 */
	public static boolean booleanValue(String paramValue) {
		boolean retVal;
		if ("yes".equalsIgnoreCase(paramValue.trim())) {
			retVal = true;
		} else {
			retVal = new Boolean(paramValue).booleanValue();
		}
		return retVal;
	}
}