package de.juwimm.cms.test.hibernate;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;
import de.juwimm.cms.model.DocumentHbm;
import de.juwimm.cms.model.DocumentHbmImpl;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.SiteHbmImpl;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.model.UnitHbmImpl;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class DocumentDaoTest extends HbmTestImpl{
	
	public void initializeServiceBeans() {
		// I don't use it
		
	}
	
	public void insertDocument(DocumentHbm document){		
		getJdbcTemplate().update(String.format(
				"insert into document" +
				"(document_id,mime_type,time_stamp,document_name,use_count_last_version,use_count_publish_version,update_Search_Index,unit_id_fk) " +
				"values (%d,'%s',0,'%s',1,1,0,%d)",
				document.getDocumentId(),document.getMimeType(),document.getDocumentName(),document.getUnit().getUnitId()));
	}
	
	public void insertUnit(UnitHbm unit){
		getJdbcTemplate().update(String.format(
				"insert into unit (unit_id,name,last_Modified_date,site_id_fk) " +
				"values (%d,'%s',0,%d)",
				unit.getUnitId(),unit.getName(),unit.getSite().getSiteId()));
	}
	
	public void insertSite(SiteHbm site){
		getJdbcTemplate().update(String.format("insert into site " +
				"(site_id,site_name,site_short,mandator_dir,WYSIWYG_IMAGE_URL,HELP_URL,DCF_URL,PREVIEW_URL,PAGE_NAME_FULL,PAGE_NAME_CONTENT,PAGE_NAME_SEARCH,LAST_MODIFIED_DATE) values " +
				"(%d,'%s','%s','c:/mandatorDir','WYSIWYG_IMAGE_URL','HELP_URL','DCF_URL','PREVIEW_URL','page.html','content.html','search.html',0)", 
				site.getSiteId(),site.getName(),site.getName()));
	}
	
	public void init(){
		DocumentHbm documentInserted = new DocumentHbmImpl();
		UnitHbm unit = new UnitHbmImpl();		
		SiteHbm site = new SiteHbmImpl();
		
		site.setSiteId(1);
		site.setName("testSite");
		insertSite(site);
		
		unit.setUnitId(1);
		unit.setName("testUnit");
		unit.setSite(site);
		insertUnit(unit);
		
		documentInserted.setDocumentName("testDocument");
		documentInserted.setMimeType("txt");
		documentInserted.setDocumentId(1);
		documentInserted.setUnit(unit);
		insertDocument(documentInserted);
	}
		
	public void testLoad(){
		init();
		DocumentHbm document = getDocumentDao().load(1);	
		Assert.assertNotNull(document);
		Assert.assertEquals("testDocument",document.getDocumentName());
	}
	
	public void testFindAll(){
		init();
		Collection documents = getDocumentDao().findAll(1);	
		Assert.assertNotNull(documents);
		Assert.assertEquals(1,documents.size());
	}

}
