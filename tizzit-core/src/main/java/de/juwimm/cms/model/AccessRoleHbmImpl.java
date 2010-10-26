// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package de.juwimm.cms.model;

import de.juwimm.cms.vo.AccessRoleValue;

/**
 * @see de.juwimm.cms.model.AccessRoleHbm
 */
public class AccessRoleHbmImpl extends de.juwimm.cms.model.AccessRoleHbm {
	/**
	 * The serial version UID of this class. Needed for serialization.
	 */
	private static final long serialVersionUID = 6264703685876723315L;

	/**
	 * @see de.juwimm.cms.model.AccessRoleHbm#getDao()
	 */
	@Override
	public AccessRoleValue getDao() {
		AccessRoleValue aRoleValue = new AccessRoleValue();
		aRoleValue.setRoleId(this.getRoleId());
		return aRoleValue;
	}

}