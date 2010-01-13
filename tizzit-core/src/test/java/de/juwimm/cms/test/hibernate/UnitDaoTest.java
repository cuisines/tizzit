package de.juwimm.cms.test.hibernate;

import java.util.ArrayList;
import java.util.Collection;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.authorization.model.UserHbmDao;
import de.juwimm.cms.authorization.model.UserHbmImpl;
import de.juwimm.cms.components.model.AddressHbm;
import de.juwimm.cms.components.model.AddressHbmImpl;
import de.juwimm.cms.components.model.DepartmentHbm;
import de.juwimm.cms.components.model.DepartmentHbmDao;
import de.juwimm.cms.components.model.DepartmentHbmImpl;
import de.juwimm.cms.components.model.PersonHbm;
import de.juwimm.cms.components.model.PersonHbmDao;
import de.juwimm.cms.components.model.PersonHbmImpl;
import de.juwimm.cms.components.model.TalktimeHbm;
import de.juwimm.cms.components.model.TalktimeHbmDao;
import de.juwimm.cms.components.model.TalktimeHbmImpl;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.SiteHbmImpl;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.model.UnitHbmDao;
import de.juwimm.cms.model.UnitHbmDaoImpl;
import de.juwimm.cms.model.UnitHbmImpl;
import de.juwimm.cms.vo.UnitValue;

public class UnitDaoTest extends HbmTestImpl {

	@Autowired
	UnitHbmDao unitDao;
	UserHbmDao userDao;
	TalktimeHbmDao talktimeDao;
	PersonHbmDao personDao;
	DepartmentHbmDao departmentDao;

	public void initializeServiceBeans() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		mockAuthetication();

		userDao = EasyMock.createMock(UserHbmDao.class);
		((UnitHbmDaoImpl) unitDao).setUserHbmDao(userDao);

		talktimeDao = EasyMock.createMock(TalktimeHbmDao.class);
		((UnitHbmDaoImpl) unitDao).setTalktimeHbmDao(talktimeDao);

		personDao = EasyMock.createMock(PersonHbmDao.class);
		((UnitHbmDaoImpl) unitDao).setPersonHbmDao(personDao);

		departmentDao = EasyMock.createMock(DepartmentHbmDao.class);
		((UnitHbmDaoImpl) unitDao).setDepartmentHbmDao(departmentDao);

	}

	/**
	 * Test Create
	 * expect: assign site, assign id 
	 */
	public void testCreate() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);

		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");
		user.setActiveSite(site);

		UnitHbm unit = new UnitHbmImpl();
		unit.setName("testUnit");

		try {
			EasyMock.expect(userDao.load(EasyMock.eq("testUser"))).andReturn(user);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(userDao);

		try {
			unit = unitDao.create(unit);
			Assert.assertNotNull(unit.getUnitId());
			Assert.assertNotNull(unit.getSite());
			Assert.assertNotNull(unit.getLastModifiedDate());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		EasyMock.verify(userDao);
	}

	/**
	 * Test GetDao
	 * expect: a UnitValue object with the properties set correctly.
	 */
	public void testGetDao() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);

		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(1);
		unit.setImageId(1);
		unit.setLogoId(1);
		unit.setName("testUnit");
		unit.setSite(site);

		AddressHbm address = new AddressHbmImpl();
		address.setAddressId(new Long(1));

		AddressHbm address1 = new AddressHbmImpl();
		address1.setAddressId(new Long(2));

		Collection<AddressHbm> addresses = new ArrayList<AddressHbm>();
		addresses.add(address);
		addresses.add(address1);

		unit.setAddresses(addresses);

		TalktimeHbm talktime = new TalktimeHbmImpl();
		talktime.setTalkTimeId(new Long(1));

		Collection<TalktimeHbm> talktimeCollection = new ArrayList<TalktimeHbm>();
		talktimeCollection.add(talktime);

		PersonHbm person = new PersonHbmImpl();
		person.setPersonId(new Long(1));

		Collection<PersonHbm> persons = new ArrayList<PersonHbm>();
		persons.add(person);

		DepartmentHbm department = new DepartmentHbmImpl();
		department.setDepartmentId(new Long(1));
		Collection<DepartmentHbm> departments = new ArrayList<DepartmentHbm>();
		departments.add(department);

		unit.setDepartments(departments);

		try {
			EasyMock.expect(talktimeDao.findByUnit(EasyMock.eq(1))).andReturn(talktimeCollection);
			EasyMock.expect(personDao.findByUnit(EasyMock.eq(1))).andReturn(persons);

		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(talktimeDao);
		EasyMock.replay(personDao);

		try {
			UnitValue unitValue = unitDao.getDao(unit);
			Assert.assertNotNull(unitValue);
			Assert.assertEquals(2, unitValue.getAddresses().length);
			Assert.assertEquals(1, unitValue.getTalkTimes().length);
			Assert.assertEquals(1, unitValue.getDepartments().length);
			Assert.assertTrue(unitValue.isHasChildren());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(talktimeDao);
		EasyMock.verify(personDao);
	}

	/**
	 *  Test ToXmlRecursive
	 *  expect: the unit doesn't have no persons,talktime,departments, or addresses
	 *  		the xml will contain the rest of the data assigned to unit
	 *  		
	 */
	public void testToXmlRecursive() {
		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(1);
		unit.setName("testUnit");
		unit.setImageId(1);
		unit.setLogoId(1);
		unit.setLastModifiedDate(0);

		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);
		site.setRootUnit(unit);

		unit.setSite(site);

		Collection<PersonHbm> persons = new ArrayList<PersonHbm>();
		Collection<TalktimeHbm> talktimes = new ArrayList<TalktimeHbm>();

		try {
			EasyMock.expect(talktimeDao.findByUnit(EasyMock.eq(1))).andReturn(talktimes);
			EasyMock.expect(personDao.findByUnit(EasyMock.eq(1))).andReturn(persons);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(talktimeDao);
		EasyMock.replay(personDao);

		try {
			String result = unitDao.toXmlRecursive(1, unit);
			String expected = "<unit id=\"1\" imageId=\"1\" logoId=\"1\" isRootUnit=\"true\"><![CDATA[testUnit]]>\n</unit>\n";
			Assert.assertNotNull(result);
			Assert.assertEquals(expected, result);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}
}
