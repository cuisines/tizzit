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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import de.juwimm.cms.model.EditionSliceHbm;
import de.juwimm.cms.model.EditionSliceHbmDao;

/**
 * This is the EditionSliceInputStream for reading the EditionSlices out of the Database.
 *
 * <p>Title: ConQuest</p>
 * <p>Description: Enterprise Content Management</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: JuwiMacMillan Group</p>
 * @author <a href="s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class EditionSliceInputStream extends InputStream {
	private static Logger log = Logger.getLogger(EditionSliceInputStream.class);
	private Integer editionId = null;
	private ByteArrayInputStream bin = new ByteArrayInputStream(new byte[0]);
	private int actualSliceNr = 0;
	private EditionSliceHbmDao eshd;

	public EditionSliceInputStream(Integer editionId, EditionSliceHbmDao eshd) {
		this.editionId = editionId;
		this.eshd = eshd;
	}

	public int read() throws IOException {
		int retVal = this.bin.read();
		if (retVal == -1) {
			try {
				EditionSliceHbm esl = eshd.findByEditionAndSlice(this.editionId.intValue(), ++this.actualSliceNr);
				this.bin = new ByteArrayInputStream(esl.getSliceData());
				retVal = this.bin.read();
			} catch (Exception exe) {
				// no more slice found
				if (log.isDebugEnabled()) log.debug("Error finding or reading data for Edition " + this.editionId + " and slice " + this.actualSliceNr + ": " + exe.getMessage());
			}
		}
		return retVal;
	}

	public void close() throws IOException {
		this.bin.close();
	}
}