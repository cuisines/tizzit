package org.tizzit.cocoon.generic.acting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.apache.commons.lang.StringUtils;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.log4j.Logger;
import org.tizzit.cocoon.generic.helper.MapHelper;

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
	private static Logger log = Logger.getLogger(MultiParamResourceExistsAction.class);

	private static final String DEFAULT_REPLACE_MODE = "full";
	private static final String DEFAULT_RESOURCE_NAME = "common";

	private static final String PREFIX_PARAM_NAME_CHECK_RESOURCES = "checkResource-";
	private static final String PREFIX_PARAM_NAME_DEFAULT_VALUES = "defaultValue-";
	private static final String PREFIX_PARAM_NAME_REPLACE_MODE_VALUES = "replaceMode-";

	private static final String PARAM_NAME_SET_REQUEST_ATTR = "setRequestAttributes";
	private static final String PARAM_NAME_DEFAULT_VALUE = "defaultValue";
	private static final String PARAM_NAME_REPLACE_MODE = "replaceMode";

	private static final Pattern pParamNameAndIndex = Pattern.compile("^" + PREFIX_PARAM_NAME_CHECK_RESOURCES + "(.*)\\-(\\d)$"); //Example: "checkResource-myParam-1"
	private Matcher m;

	private Parameters parameters = null;
	private Map< ? , ? > objectModel = null;

	private boolean hasMultiValueChecks = false;

	private Map<String, MultiParameterDataHolder> multiValueChecks;
	private List<String> keyPrefixList;

	private Map<String, String> sitemapParams;

	/**
	 * @see org.apache.cocoon.acting.Action#act(org.apache.cocoon.environment.Redirector, org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
	 */
	@SuppressWarnings("unchecked")
	public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters parameters) throws Exception {
		if (log.isDebugEnabled()) log.debug("act() -> begin");
		this.sitemapParams = new HashMap<String, String>();

		this.parameters = parameters;
		this.objectModel = objectModel;

		final String[] paramNames = this.parameters.getNames();
		for (int i = 0; i < paramNames.length; i++) {
			final String paramName = paramNames[i];
			if (paramName.startsWith(PREFIX_PARAM_NAME_CHECK_RESOURCES)) {
				if (this.multiValueChecks == null) {
					this.multiValueChecks = new HashMap<String, MultiParameterDataHolder>();
				}

				MultiParameterDataHolder dataHolder = new MultiParameterDataHolder();
				dataHolder.originName = paramName;

				try {
					dataHolder.identifier = paramName.split("^" + PREFIX_PARAM_NAME_CHECK_RESOURCES)[1];
				} catch (ArrayIndexOutOfBoundsException e) {
					log.error(PREFIX_PARAM_NAME_CHECK_RESOURCES + " has been without target! Please use \"" + PREFIX_PARAM_NAME_CHECK_RESOURCES + "yourParameterName\" as the name of a parameter!");
				}

				this.m = pParamNameAndIndex.matcher(paramName);

				if (m.matches()) {
					this.hasMultiValueChecks = true;
					dataHolder.multiValue = true;
					dataHolder.returnName = m.group(1);
					//dataHolder.value = m.group(1);

					//this.processMultiValue(paramName, mp);
					if (this.keyPrefixList == null) {
						this.keyPrefixList = new ArrayList<String>();
					}
					if (!this.keyPrefixList.contains(dataHolder.returnName)) {
						this.keyPrefixList.add(dataHolder.returnName);
					}
				} else {
					dataHolder.returnName = dataHolder.identifier;
				}
				dataHolder.value = this.parameters.getParameter(dataHolder.originName);

				if (m.matches()) {
					//add to map - this map will be checked later
					this.multiValueChecks.put(dataHolder.identifier, dataHolder);
				} else {
					boolean exists = this.resourceExists(resolver, dataHolder.value);
					if (exists) {
						this.sitemapParams.put(dataHolder.returnName, dataHolder.value);
						setRequestAttributes(dataHolder.returnName, dataHolder.value);
					} else {
						this.sitemapParams.put(dataHolder.returnName, this.getDefaultValue(dataHolder.returnName, dataHolder.value));
						setRequestAttributes(dataHolder.returnName, this.getDefaultValue(dataHolder.returnName, dataHolder.value));
					}
				}
			}
		}

		if (this.hasMultiValueChecks && this.keyPrefixList != null && this.keyPrefixList.size() > 0) {
			try {
				for (String keyPrefix : this.keyPrefixList) {
					int i = 0;
					boolean resourceExsists = false;
					String key = null;
					MultiParameterDataHolder dataHolder = null;
					do {
						if (this.multiValueChecks.containsKey(key)) {
							dataHolder = this.multiValueChecks.get(key);
							//this.context = EnvironmentHelper.getCurrentProcessor().getContext();

							resourceExsists = this.resourceExists(resolver, dataHolder.value);
							if (resourceExsists) {
								this.sitemapParams.put(dataHolder.returnName, dataHolder.value);
								setRequestAttributes(dataHolder.returnName, dataHolder.value);
							}
						}
						i++;
						key = keyPrefix + "-" + i;
					} while (this.multiValueChecks.containsKey(key) && !resourceExsists);

					if (!resourceExsists && dataHolder != null) {
						this.sitemapParams.put(dataHolder.returnName, this.getDefaultValue(dataHolder.returnName, dataHolder.value));
						setRequestAttributes(dataHolder.returnName, this.getDefaultValue(dataHolder.returnName, dataHolder.value));
					}
				}
			} catch (Exception e) {
				log.error("Error: ", e);
			}
		}

		if (log.isDebugEnabled()) {
			log.debug(MapHelper.mapToString(this.sitemapParams));
		}

		if (log.isDebugEnabled()) log.debug("act() -> end");
		return this.sitemapParams;
	}

	private boolean resourceExists(SourceResolver resolver, String resourceURI) {
		boolean retVal = false;

		resourceURI = getValidResourceURI(resourceURI);

		Source resource = null;
		try {
			resource = resolver.resolveURI(resourceURI);
			if (resource.exists()) {
				retVal = true;
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
		return retVal;
	}

	private String getValidResourceURI(String resourceURI) {
		if (log.isDebugEnabled()) log.debug("getValidResourceURI() -> begin");
		String retVal = resourceURI;

		String context = null;
		try {
			context = EnvironmentHelper.getCurrentProcessor().getContext();
		} catch (Exception exe) {
			if (log.isDebugEnabled()) log.debug("Could not get context!");
		}

		if (resourceURI.startsWith("/")) {
			if (StringUtils.isNotBlank(context)) {
				retVal = context + resourceURI;
			} else {
				retVal = resourceURI.replaceFirst("^/", "");
			}
		}

		if (log.isDebugEnabled()) log.debug("getValidResourceURI() -> end");
		return retVal;
	}

	private void setRequestAttributes(String attributeName, String resourceURI) {
		if (log.isDebugEnabled()) log.debug("setRequestAttributes() -> begin");
		try {
			if (this.parameters.isParameter(PARAM_NAME_SET_REQUEST_ATTR)) {
				if (Boolean.parseBoolean(this.parameters.getParameter(PARAM_NAME_SET_REQUEST_ATTR))) {
					ObjectModelHelper.getRequest(this.objectModel).setAttribute(attributeName, resourceURI);
					if (log.isDebugEnabled()) log.debug("Setting request attribute \"" + attributeName + "\" to \"" + resourceURI + "\". Use \"{request-attr:" + attributeName + "}\" to get the value!");
				}
			}
		} catch (Exception exe) {
			log.error("Error in setRequestAttributes(): ", exe);
		}
		if (log.isDebugEnabled()) log.debug("setRequestAttributes() -> end");
	}

	private String fullReplace(String name, String path) {
		if (log.isDebugEnabled()) log.debug("fullReplace() -> begin");
		String result = "";
		try {
			result = this.parameters.getParameter(PREFIX_PARAM_NAME_DEFAULT_VALUES + name);
		} catch (ParameterException e) {
			log.error("Error in fullReplace(): ", e);
		}
		if (log.isDebugEnabled()) {
			log.debug("Result: " + result);
		}
		if (log.isDebugEnabled()) log.debug("fullReplace() -> end");
		return result;
	}

	private String replaceFileName(String name, String path) {
		if (log.isDebugEnabled()) log.debug("replaceFileName() -> begin");
		String result = "";
		String replaceRegExp = "^.*\\.";
		String replaceSuffix = ".";
		try {
			//String[] path = this.parameters.getParameter(PREFIX_PARAM_NAME_CHECK_RESOURCES + name).split("/");
			String[] splittedPath = path.split("/");
			for (int j = 0; j < splittedPath.length; j++) {
				if (j + 1 == splittedPath.length) {
					if (this.parameters.isParameter(PREFIX_PARAM_NAME_DEFAULT_VALUES + name)) {
						result += splittedPath[j].replaceAll(replaceRegExp, this.parameters.getParameter(PREFIX_PARAM_NAME_DEFAULT_VALUES + name) + replaceSuffix);
					} else if (this.parameters.isParameter(PARAM_NAME_DEFAULT_VALUE)) {
						result += splittedPath[j].replaceAll(replaceRegExp, this.parameters.getParameter(PARAM_NAME_DEFAULT_VALUE) + replaceSuffix);
					} else {
						result += splittedPath[j].replaceAll(replaceRegExp, DEFAULT_RESOURCE_NAME + replaceSuffix);
					}
				} else {
					result += splittedPath[j] + "/";
				}
			}
		} catch (Exception e) {
			log.error("Error in replaceFileName(): ", e);
		}
		if (log.isDebugEnabled()) {
			log.debug("Result: " + result);
		}
		if (log.isDebugEnabled()) log.debug("replaceFileName() -> end");
		return result;
	}

	private String getDefaultValue(String name, String path) {
		if (log.isDebugEnabled()) log.debug("getDefaultValue() -> begin");
		String result = "";
		try {
			if (this.parameters.isParameter(PREFIX_PARAM_NAME_REPLACE_MODE_VALUES + name) && this.parameters.isParameter(PREFIX_PARAM_NAME_DEFAULT_VALUES + name)) {
				if (!this.parameters.getParameter(PREFIX_PARAM_NAME_REPLACE_MODE_VALUES + name).equalsIgnoreCase(DEFAULT_REPLACE_MODE)) {
					result = replaceFileName(name, path);
				} else {
					result = fullReplace(name, path);
				}
			} else if (this.parameters.isParameter(PARAM_NAME_REPLACE_MODE) && this.parameters.isParameter(PREFIX_PARAM_NAME_DEFAULT_VALUES + name)) {
				if (!this.parameters.getParameter(PARAM_NAME_REPLACE_MODE).equalsIgnoreCase(DEFAULT_REPLACE_MODE)) {
					result = replaceFileName(name, path);
				} else {
					result = fullReplace(name, path);
				}
			} else if (this.parameters.isParameter(PREFIX_PARAM_NAME_DEFAULT_VALUES + name)) {
				result = fullReplace(name, path);
			} else {
				if (log.isDebugEnabled()) log.debug("Parameter \"defaultValue-" + name + "\" not found. Replacing the filename with \"" + DEFAULT_RESOURCE_NAME + "\"!");
				result = replaceFileName(name, path);
			}
		} catch (Exception e) {
			log.error("Error in getDefaultValue(): ", e);
		}
		if (log.isDebugEnabled()) log.debug("getDefaultValue() -> end");
		return result;
	}

	private class MultiParameterDataHolder {
		String identifier;
		String returnName;
		String originName;
		String value;
		boolean multiValue = false;
	}
}