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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import de.juwimm.cms.model.EditionHbm;
import de.juwimm.cms.model.EditionSliceHbm;
import de.juwimm.cms.model.EditionSliceHbmDao;

/**
 * This is the EditionSliceOutputStream for use, if the actual outputStream should be written to the Database.
 *
 * <p>Title: Tizzit</p>
 * <p>Description: Enterprise Content Management</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: JuwiMacMillan Group</p>
 * @author <a href="s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class EditionSliceOutputStream extends OutputStream {
	private static Logger log = Logger.getLogger(EditionSliceOutputStream.class);
	private EditionHbm edition = null;
	private ByteArrayOutputStream out = null;
	private int sliceId = 0;
	private final EditionSliceHbmDao eshd;

	public EditionSliceOutputStream(EditionHbm edition, EditionSliceHbmDao eshd) {
		this.edition = edition;
		this.eshd = eshd;
		out = new ByteArrayOutputStream();
	}

	@Override
	public void close() throws IOException {
		if (log.isDebugEnabled()) log.debug("Close");
		fetterChecker(true);
		out.close();
	}

	@Override
	public void flush() throws IOException {
		out.flush();
	}

	@Override
	public void write(int b) {
		out.write(b);
		fetterChecker(false);
	}

	private void fetterChecker(boolean flush) {
		if (out.size() >= (1024 * 1024) || flush) {
			if (log.isDebugEnabled()) log.debug("Size: " + out.size());
			try {
				out.flush();
				byte[] meg = out.toByteArray();
				out.close();
				EditionSliceHbm esh = EditionSliceHbm.Factory.newInstance();
				esh.setEditionId(edition.getEditionId());
				esh.setSliceData(meg);
				esh.setSliceNr(++sliceId);
				eshd.create(esh);
				out = new ByteArrayOutputStream();
			} catch (Exception exe) {
				log.error("Error occured", exe);
			}
		}
	}

}