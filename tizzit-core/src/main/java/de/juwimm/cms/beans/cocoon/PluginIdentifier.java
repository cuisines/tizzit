/**
 * Copyright (c) 2009 Juwi MacMillan Group GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.juwimm.cms.beans.cocoon;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PluginIdentifier {
	private Integer siteId = null;
	private String namespace = null;

	public PluginIdentifier(Integer siteId, String namespace) {
		this.siteId = siteId;
		this.namespace = namespace;
	}

	public String getNamespace() {
		return this.namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public Integer getSiteId() {
		return this.siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	@Override
	public boolean equals(Object obj) {
		PluginIdentifier piOther = (PluginIdentifier) obj;
		return piOther.getNamespace().equalsIgnoreCase(getNamespace()) && piOther.getSiteId().equals(getSiteId());
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + ((this.namespace != null) ? this.namespace.hashCode() : 0);
		result = 37 * result + ((this.siteId != null) ? this.siteId.hashCode() : 0);
		return result;
	}
}
