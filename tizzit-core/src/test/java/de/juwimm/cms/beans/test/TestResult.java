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
package de.juwimm.cms.beans.test;

import org.apache.log4j.Logger;
import org.junit.Ignore;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
@Ignore
public class TestResult {
	private static Logger log = Logger.getLogger(TestResult.class);
	private static int testResultAdderInt = 0;
	private static int testResultAdderInt2 = 0;

	public int testResultAdder() {
		log.info("called testResultAdder");
		return testResultAdderInt++;
	}

	public int testResultAdder2(String variable) {
		log.info("called testResultAdder2");
		return testResultAdderInt2++;
	}
}
