// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package de.juwimm.cms.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @see de.juwimm.cms.model.SequenceHbm
 */
public class SequenceHbmDaoImpl extends de.juwimm.cms.model.SequenceHbmDaoBase {
	private Log log = LogFactory.getLog(SequenceHbmDaoImpl.class);
	/**
	 * @see de.juwimm.cms.model.SequenceHbmDao#getNextSequenceNumber(java.lang.String)
	 */
	protected java.lang.Integer handleGetNextSequenceNumber(java.lang.String name) {
		SequenceHbm sequenceHbm = load(name);
		if(sequenceHbm == null) {
			log.info("Creating new sequence for " + name);
			sequenceHbm = SequenceHbm.Factory.newInstance();
			sequenceHbm.setIdx(1);
			sequenceHbm.setName(name);
			create(sequenceHbm);
		} 
		synchronized (sequenceHbm) {
			sequenceHbm.setIdx(sequenceHbm.getIdx() + 1);
			update(sequenceHbm);
			return sequenceHbm.getIdx();
		}
	}

}