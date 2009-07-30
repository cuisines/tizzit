package de.juwimm.cms.client.util;
import java.util.ArrayList;
import java.util.List;

import de.juwimm.cms.client.model.MandantenModel;
public class TestData {
	public static List<MandantenModel> getTableTestData(){
		List<MandantenModel> testList = new ArrayList<MandantenModel>();
		MandantenModel site = new MandantenModel(1,"TestShort","Test Name");
		testList.add(site);
		site = new MandantenModel(1,"TestShort2","Test2 Name");
		testList.add(site);
		site = new MandantenModel(1,"TestShort4","Test3 Name");
		testList.add(site);
		return testList;
	}
}
