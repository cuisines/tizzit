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
package de.juwimm.cms.util;

import javax.swing.ImageIcon;

/**
 * <p>Title: Tizzit</p>
 * <p>Description: Enterprise Content Management</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class SiteParameter implements Parameter {
	private final String id;
	private final String name;
	private final ParameterType[] parameterTypes;
	private ImageIcon imgi = null;

	public SiteParameter(String id, String name, ParameterType[] parameterTypes) {
		this.id = id;
		this.name = name;
		this.parameterTypes = parameterTypes;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int[] getParameterTypes() {
		int[] retArr = new int[parameterTypes.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			retArr[i] = parameterTypes[i].getParameterType();
		}
		return retArr;
	}

	public Object getDefaultValue(int parameterType) {
		for (int i = 0; i < parameterTypes.length; i++) {
			if (parameterTypes[i].getParameterType() == parameterType) { return parameterTypes[i].getValue(); }
		}
		return null;
	}

	public ImageIcon getIcon() {
		String imgname = "/images/parameter/" + getId() + ".png";
		if (imgi == null) {
			try {
				imgi = new ImageIcon(SiteParameter.class.getResource(imgname));
			} catch (Exception ex) {
				// can't find this Image
				imgname = "/images/parameter/defaultParameter.png";
				imgi = new ImageIcon(SiteParameter.class.getResource(imgname));
			}
		}
		return imgi;
	}

	/**
	 * <p>Title: Tizzit</p>
	 * <p>Description: Enterprise Content Management</p>
	 * <p>Copyright: Copyright (c) 2004</p>
	 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
	 * @version $Id$
	 */
	public static class ParameterType {
		private final int paramType;
		private Object value;

		public ParameterType(int paramType, Object defaultValue) {
			this.paramType = paramType;
			this.value = defaultValue;
		}

		public int getParameterType() {
			return paramType;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}
	}

}
