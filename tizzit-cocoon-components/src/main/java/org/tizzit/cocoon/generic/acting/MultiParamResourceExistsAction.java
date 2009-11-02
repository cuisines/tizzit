package org.tizzit.cocoon.generic.acting;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.thread.SingleThreaded;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.internal.EnvironmentHelper;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.log4j.Logger;

/**
 *
 * <h5>Usage:</h5>
 * <p>
 * 	<pre>
 * &lt;map:act type="multiParamResourceExistsAction"&gt;
 *     &lt;map:parameter name="checkResource-theNameOfTheReturnParameter-1" value="layouts/html/fooBar.xml" /&gt;
 *     &lt;map:parameter name="checkResource-theNameOfTheReturnParameter-2" value="layouts/html/common.xml" /&gt;
 *     &lt;map:parameter name="checkResource-theNameOfTheReturnParameter-3" value="layouts/fooBar.xml" /&gt;
 *
 *     &lt;-- OPTIONAL parameters --&gt;
 *     &lt;map:parameter name="defaultValue-theNameOfTheReturnParameter1" value="myFileName" /&gt;
 *
 * &lt;/map:act&gt;
 * 	</pre>
 * </p>
 * <h5>Parameters</h5>
 * <ul>
 * 	<li><b>setRequestAttributes</b> - default: <i>false</i> - Text</li>
 * 	<li><b>defaultValue</b> - default: <i>common</i> - Text</li>
 * 	<li><b>replaceMode</b> - default: <i>fileName</i> - </li>
 * 	<li><b>checkResource-theNameOfTheReturnParameter</b> - Text</li>
 * 	<li><b>defaultValue-theNameOfTheReturnParameter</b> - Text</li>
 * 	<li><b>replaceMode-theNameOfTheReturnParameter</b> - Text</li>
 * </ul>
 *
 * This action can be configured to specify the value of the return parameters.
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * @version $Id$
 * @since tizzit-cocoon-components 02.11.2009
 */
public class MultiParamResourceExistsAction extends AbstractAction implements SingleThreaded {
	private static final String DEFAULT_REPLACE_MODE = "full";

	private static final String DEFAULT_RESOURCE_NAME = "common";
	private static Logger log = Logger.getLogger(MultiParamResourceExistsAction.class);
	private static final String PARAM_NAME_DEFAULT_VALUE = "defaultValue";

	private static final String PARAM_NAME_REPLACE_MODE = "replaceMode";
	private static final String PARAM_NAME_SET_REQUEST_ATTR = "setRequestAttributes";

	private static final String PREFIX_PARAM_NAME_CHECK_RESOURCES = "checkResource-";
	private static final String PREFIX_PARAM_NAME_DEFAULT_VALUES = "defaultValue-";

	private static final String PREFIX_PARAM_NAME_REPLACE_MODE_VALUES = "replaceMode-";

	private static final Pattern pParamNameAndIndex = Pattern.compile("^" + PREFIX_PARAM_NAME_CHECK_RESOURCES + "(.*)\\-(\\d)$"); //Example: "checkResource-myParam-1"

	private String checkResourceParamName = "";

	private String context;
	private String defaultValue;
	private Matcher m;

	private boolean multiFallBackMode = false;
	private HashMap<Integer, String[]> multiFallBackValues;
	private Source resource;

	private String resourceURI;
	private Map<String, String> sitemapParams;

	public MultiParamResourceExistsAction() {

	}

	/**
	 * @see org.apache.cocoon.acting.Action#act(org.apache.cocoon.environment.Redirector, org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
	 */
	@SuppressWarnings("unchecked")
	public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters parameters) throws Exception {
		if (log.isDebugEnabled()) log.debug("start act");

		this.sitemapParams = new HashMap<String, String>();

		final String[] paramNames = parameters.getNames();

		for (int i = 0; i < paramNames.length; i++) {
			final String paramName = paramNames[i];

			if (paramName.startsWith(PREFIX_PARAM_NAME_CHECK_RESOURCES)) {
				try {
					checkResourceParamName = paramName.split("^" + PREFIX_PARAM_NAME_CHECK_RESOURCES)[1];
				} catch (ArrayIndexOutOfBoundsException e) {
					log.error(PREFIX_PARAM_NAME_CHECK_RESOURCES + " has been without target! Please use \"" + PREFIX_PARAM_NAME_CHECK_RESOURCES + "yourParameterName\" as the name of a parameter!");
				}
				this.m = pParamNameAndIndex.matcher(paramName);

				if (m.matches()) {
					this.multiFallBackMode = true;
					if (this.multiFallBackValues == null) {
						this.multiFallBackValues = new HashMap<Integer, String[]>();
					}
					String[] tmp = {m.group(1), parameters.getParameter(paramName)};
					this.multiFallBackValues.put(Integer.valueOf(m.group(2)), tmp); // 1, {"myParamName", "pathA/pathB/myFile.xml"}
					tmp = null;
				} else {
					this.resourceURI = getValidResourceURI(parameters.getParameter(paramName));
					this.context = EnvironmentHelper.getCurrentProcessor().getContext();
					try {
						resource = resolver.resolveURI(this.resourceURI);
						if (resource.exists()) {
							sitemapParams.put(this.checkResourceParamName, this.resourceURI);
							setRequestAttributes(parameters, objectModel, this.resourceURI);
						} else {
							setDefaultValue(parameters);
							sitemapParams.put(this.checkResourceParamName, this.defaultValue);
							setRequestAttributes(parameters, objectModel, this.defaultValue);
							this.defaultValue = null;
						}
					} catch (SourceNotFoundException e) {
						if (log.isDebugEnabled()) log.debug("Havent found ressource \"" + this.resourceURI + "\" trying next!");
					} catch (Exception e) {
						log.error("Error in act(): ", e);
					} finally {
						if (resource != null) {
							resolver.release(resource);
						}
					}
					if (log.isDebugEnabled()) log.debug("----------"); //SPACER
				}
			}
		}

		if (multiFallBackMode) {
			int i = 0;
			boolean resourceExsists = false;
			try {
				do {
					if (multiFallBackValues.containsKey(Integer.valueOf(i))) {
						this.checkResourceParamName = multiFallBackValues.get(Integer.valueOf(i))[0];
						this.resourceURI = getValidResourceURI(multiFallBackValues.get(Integer.valueOf(i))[1]);
						this.context = EnvironmentHelper.getCurrentProcessor().getContext();
						this.resource = null;
						try {
							resource = resolver.resolveURI(resourceURI);
							if (resource.exists()) {
								sitemapParams.put(checkResourceParamName, resourceURI);
								setRequestAttributes(parameters, objectModel, resourceURI);
								resourceExsists = true;
							}
						} catch (SourceNotFoundException e) {
							if (log.isDebugEnabled()) log.debug("havent found ressource " + resourceURI + " trying next!");
						} catch (Exception e) {
							log.error("Error in act(): ", e);
						} finally {
							if (resource != null) {
								resolver.release(resource);
							}
						}
					}
					i++;
				} while (multiFallBackValues.containsKey(Integer.valueOf(i)) && !resourceExsists);
				if (!resourceExsists) {
					//TODO: Doku... der wird gesetzt, damit replaceFileName() diesen Parameter  nach jedem / splitten kann.
					parameters.setParameter(PREFIX_PARAM_NAME_CHECK_RESOURCES + checkResourceParamName, resourceURI);
					setDefaultValue(parameters);
					sitemapParams.put(checkResourceParamName, this.defaultValue);
					setRequestAttributes(parameters, objectModel, this.defaultValue);
					this.defaultValue = null;
				}
			} catch (Exception e) {
				log.error("Error: ", e);
			}

		}
		if (log.isDebugEnabled()) {
			log.debug("Return parameter(name=value): " + sitemapParams.toString());
			log.debug("end act");
		}
		return sitemapParams;
	}

	private String fullReplace(Parameters parameters) {
		if (log.isDebugEnabled()) log.debug("start fullReplace");
		String result = "";
		try {
			result = parameters.getParameter(PREFIX_PARAM_NAME_DEFAULT_VALUES + checkResourceParamName);
		} catch (ParameterException e) {
			log.error("Error in replaceFileName(): ", e);
		}
		if (log.isDebugEnabled()) {
			log.debug("Result: " + result);
			log.debug("end fullReplace");
		}
		return result;
	}

	private String getValidResourceURI(String resourceURI) {
		String retVal = resourceURI;
		if (resourceURI.startsWith("/")) {
			if (!context.equals("")) {
				retVal = context + resourceURI;
			} else {
				retVal = resourceURI.replaceFirst("^/", "");
			}
		}
		return retVal;
	}

	private String replaceFileName(Parameters parameters) {
		if (log.isDebugEnabled()) log.debug("start replaceFileName");
		String result = "";
		String replaceRegExp = "^.*\\.";
		String replaceSuffix = ".";
		try {
			String[] path = parameters.getParameter(PREFIX_PARAM_NAME_CHECK_RESOURCES + checkResourceParamName).split("/");
			for (int j = 0; j < path.length; j++) {
				if (j + 1 == path.length) {
					if (parameters.isParameter(PREFIX_PARAM_NAME_DEFAULT_VALUES + checkResourceParamName)) {
						result += path[j].replaceAll(replaceRegExp, parameters.getParameter(PREFIX_PARAM_NAME_DEFAULT_VALUES + checkResourceParamName) + replaceSuffix);
					} else if (parameters.isParameter(PARAM_NAME_DEFAULT_VALUE)) {
						result += path[j].replaceAll(replaceRegExp, parameters.getParameter(PARAM_NAME_DEFAULT_VALUE) + replaceSuffix);
					} else {
						result += path[j].replaceAll(replaceRegExp, DEFAULT_RESOURCE_NAME + replaceSuffix);
					}
				} else {
					result += path[j] + "/";
				}
			}
		} catch (Exception e) {
			log.error("Error in replaceFileName(): ", e);
		}
		if (log.isDebugEnabled()) {
			log.debug("Result: " + result);
			log.debug("end replaceFileName");
		}
		return result;
	}

	private void setDefaultValue(Parameters parameters) {
		if (log.isDebugEnabled()) log.debug("start setDefaultValue");
		String result = "";
		try {
			if (parameters.isParameter(PREFIX_PARAM_NAME_REPLACE_MODE_VALUES + checkResourceParamName) && parameters.isParameter(PREFIX_PARAM_NAME_DEFAULT_VALUES + checkResourceParamName)) {
				if (!parameters.getParameter(PREFIX_PARAM_NAME_REPLACE_MODE_VALUES + checkResourceParamName).equalsIgnoreCase(DEFAULT_REPLACE_MODE)) {
					result = replaceFileName(parameters);
				} else {
					result = fullReplace(parameters);
				}
			} else if (parameters.isParameter(PARAM_NAME_REPLACE_MODE) && parameters.isParameter(PREFIX_PARAM_NAME_DEFAULT_VALUES + checkResourceParamName)) {
				if (!parameters.getParameter(PARAM_NAME_REPLACE_MODE).equalsIgnoreCase(DEFAULT_REPLACE_MODE)) {
					result = replaceFileName(parameters);
				} else {
					result = fullReplace(parameters);
				}
			} else if (parameters.isParameter(PREFIX_PARAM_NAME_DEFAULT_VALUES + checkResourceParamName)) {
				result = fullReplace(parameters);
			} else {
				if (log.isDebugEnabled()) log.debug("Parameter \"defaultValue-" + checkResourceParamName + "\" not found. Replacing the filename with \"" + DEFAULT_RESOURCE_NAME + "\"!");
				result = replaceFileName(parameters);
			}
		} catch (Exception e) {
			log.error("Error in setDefaultValue(): ", e);
		}
		if (log.isDebugEnabled()) log.debug("end setDefaultValue");
		this.defaultValue = result;
	}

	@SuppressWarnings("unchecked")
	private void setRequestAttributes(Parameters parameters, Map objectModel, String resourceURI) {
		if (log.isDebugEnabled()) log.debug("start setRequestAttributes");
		try {
			if (parameters.isParameter(PARAM_NAME_SET_REQUEST_ATTR)) {
				if (Boolean.parseBoolean(parameters.getParameter(PARAM_NAME_SET_REQUEST_ATTR))) {
					ObjectModelHelper.getRequest(objectModel).setAttribute(checkResourceParamName, resourceURI);
					if (log.isDebugEnabled()) log.debug("Setting request attribute \"" + checkResourceParamName + "\" to \"" + resourceURI + "\". Use \"{request-attr:" + checkResourceParamName + "}\" to get the value!");
				}
			}
		} catch (Exception e) {
			log.error("Error in setRequestAttributes(): ", e);
		}
		if (log.isDebugEnabled()) log.debug("end setRequestAttributes");
	}
}