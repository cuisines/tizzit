package de.juwimm.cms.remote.test;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.authorization.model.UserHbmDao;
import de.juwimm.cms.authorization.model.UserHbmImpl;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.model.UnitHbmDao;
import de.juwimm.cms.model.UnitHbmImpl;
import de.juwimm.cms.remote.UnitServiceSpringImpl;
import de.juwimm.cms.vo.UnitValue;

public class UnitServiceTest extends TestCase {
	@Autowired
	private UnitHbmDao unitDaoMock;
	private UserHbmDao userHbmDaoMock;
	private UnitServiceSpringImpl unitService;

	@Override
	protected void setUp() throws Exception {
		unitService = new UnitServiceSpringImpl();
		unitDaoMock = EasyMock.createMock(UnitHbmDao.class);
		unitService.setUnitHbmDao(unitDaoMock);
		userHbmDaoMock = EasyMock.createMock(UserHbmDao.class);
		unitService.setUserHbmDao(userHbmDaoMock);
	}

	/**
	 * Test AddUnit2User
	 * expect: the given user to be added to the unit's user list
	 */
	public void testAddUnit2User() {
		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");
		user.setFirstName("testFirstName");
		user.setLastName("testLastName");

		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(1);
		unit.setName("testUnitName");

		UnitValue unitValue = new UnitValue();
		unitValue.setUnitId(1);
		unitValue.setName("testUnitName");

		try {
			EasyMock.expect(userHbmDaoMock.load(EasyMock.eq("testUser"))).andReturn(user);
			EasyMock.expect(unitDaoMock.load(EasyMock.eq(1))).andReturn(unit).times(2);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		EasyMock.replay(userHbmDaoMock);
		EasyMock.replay(unitDaoMock);

		try {
			unitService.addUser2Unit(unitValue, "testUser");
			unit = unitDaoMock.load(1);
			Assert.assertEquals(1, unit.getUsers().size());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(userHbmDaoMock);
		EasyMock.verify(unitDaoMock);
	}

	/**
	* Test RemoveUnitFromUser
	 * expect: the given user to be removed from
	 *		   the unit's user list
	 */
	public void testRemoveUserFromUnit() {
		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");
		user.setFirstName("testFirstName");
		user.setLastName("testLastName");

		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(1);
		unit.setName("testUnitName");
		unit.getUsers().add(user);

		UnitValue unitValue = new UnitValue();
		unitValue.setUnitId(1);
		unitValue.setName("testUnitName");

		try {
			EasyMock.expect(userHbmDaoMock.load(EasyMock.eq("testUser"))).andReturn(user);
			EasyMock.expect(unitDaoMock.load(EasyMock.eq(1))).andReturn(unit).times(2);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		EasyMock.replay(userHbmDaoMock);
		EasyMock.replay(unitDaoMock);

		try {
			unitService.removeUserFromUnit(unitValue, "testUser");
			unit = unitDaoMock.load(1);
			Assert.assertEquals(0, unit.getUsers().size());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}
}
