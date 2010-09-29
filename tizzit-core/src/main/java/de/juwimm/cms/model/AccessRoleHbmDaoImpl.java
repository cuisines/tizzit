// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package de.juwimm.cms.model;
/**
 * @see de.juwimm.cms.model.AccessRoleHbm
 */
public class AccessRoleHbmDaoImpl
    extends de.juwimm.cms.model.AccessRoleHbmDaoBase
{
    /**
     * @see de.juwimm.cms.model.AccessRoleHbmDao#toAccessRoleValue(de.juwimm.cms.model.AccessRoleHbm, de.juwimm.cms.vo.AccessRoleValue)
     */
    public void toAccessRoleValue(
        de.juwimm.cms.model.AccessRoleHbm source,
        de.juwimm.cms.vo.AccessRoleValue target)
    {
        // @todo verify behavior of toAccessRoleValue
        super.toAccessRoleValue(source, target);
        // WARNING! No conversion for target.viewComponents (can't convert source.getViewComponents():de.juwimm.cms.model.ViewComponentHbm to de.juwimm.cms.vo.ViewComponentValue[]
    }


    /**
     * @see de.juwimm.cms.model.AccessRoleHbmDao#toAccessRoleValue(de.juwimm.cms.model.AccessRoleHbm)
     */
    public de.juwimm.cms.vo.AccessRoleValue toAccessRoleValue(final de.juwimm.cms.model.AccessRoleHbm entity)
    {
        // @todo verify behavior of toAccessRoleValue
        return super.toAccessRoleValue(entity);
    }


    /**
     * Retrieves the entity object that is associated with the specified value object
     * from the object store. If no such entity object exists in the object store,
     * a new, blank entity is created
     */
    private de.juwimm.cms.model.AccessRoleHbm loadAccessRoleHbmFromAccessRoleValue(de.juwimm.cms.vo.AccessRoleValue accessRoleValue)
    {
        // @todo implement loadAccessRoleHbmFromAccessRoleValue
        throw new java.lang.UnsupportedOperationException("de.juwimm.cms.model.loadAccessRoleHbmFromAccessRoleValue(de.juwimm.cms.vo.AccessRoleValue) not yet implemented.");

        /* A typical implementation looks like this:
        de.juwimm.cms.model.AccessRoleHbm accessRoleHbm = this.load(accessRoleValue.getId());
        if (accessRoleHbm == null)
        {
            accessRoleHbm = de.juwimm.cms.model.AccessRoleHbm.Factory.newInstance();
        }
        return accessRoleHbm;
        */
    }

    
    /**
     * @see de.juwimm.cms.model.AccessRoleHbmDao#accessRoleValueToEntity(de.juwimm.cms.vo.AccessRoleValue)
     */
    public de.juwimm.cms.model.AccessRoleHbm accessRoleValueToEntity(de.juwimm.cms.vo.AccessRoleValue accessRoleValue)
    {
        // @todo verify behavior of accessRoleValueToEntity
        de.juwimm.cms.model.AccessRoleHbm entity = this.loadAccessRoleHbmFromAccessRoleValue(accessRoleValue);
        this.accessRoleValueToEntity(accessRoleValue, entity, true);
        return entity;
    }


    /**
     * @see de.juwimm.cms.model.AccessRoleHbmDao#accessRoleValueToEntity(de.juwimm.cms.vo.AccessRoleValue, de.juwimm.cms.model.AccessRoleHbm)
     */
    public void accessRoleValueToEntity(
        de.juwimm.cms.vo.AccessRoleValue source,
        de.juwimm.cms.model.AccessRoleHbm target,
        boolean copyIfNull)
    {
        // @todo verify behavior of accessRoleValueToEntity
        super.accessRoleValueToEntity(source, target, copyIfNull);
    }

}