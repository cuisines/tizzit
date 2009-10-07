package de.juwimm.cms.remote.test;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;

import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.model.ContentVersionHbm;
import de.juwimm.cms.model.ContentVersionHbmDao;
import de.juwimm.cms.model.ContentVersionHbmImpl;
import de.juwimm.cms.model.DocumentHbm;
import de.juwimm.cms.model.DocumentHbmDao;
import de.juwimm.cms.model.DocumentHbmImpl;
import de.juwimm.cms.model.PictureHbm;
import de.juwimm.cms.model.PictureHbmDao;
import de.juwimm.cms.model.PictureHbmImpl;
import de.juwimm.cms.model.ViewComponentHbm;
import de.juwimm.cms.model.ViewComponentHbmDao;
import de.juwimm.cms.model.ViewComponentHbmImpl;
import de.juwimm.cms.model.ViewDocumentHbm;
import de.juwimm.cms.remote.ContentServiceSpringImpl;
import de.juwimm.cms.vo.ContentVersionValue;
import de.juwimm.cms.vo.ViewComponentValue;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author fzalum
 *
 */
public class ContentServiceTest extends TestCase{
	
	List<ContentVersionHbm> mockContentVersions;
	List<PictureHbm> mockPictures;
	List<DocumentHbm> mockDocuments;
	
	
	String contentWithDocumentTemplate = 
		 "	<documents dcfname='de.juwimm.cms.content.modules.Documents' description='test' label='Dokumente'>"
		+"		<document displayType='block' documentName='something.pdf' src='#{documentId}'>test</document>"
		+"	</documents>";
	String contentWithPictureTemplate = "" +
			"<picture dcfname='de.juwimm.cms.content.modules.Picture' description='#{imageId}' label='Bild'>"+
			"<image height='16' src='30' type='center' width='16'><legend/><filename>cms_16x16.gif</filename><alttext/></image>"+
			"</picture>";
	String contentTemplate = "<source>"
		+"<head/>"
		+"	<all>"
		+" #{contentDocument}"
		+" #{contentPicture}"
		+"	</all>"		
		+"</source>";
	
	DocumentHbmDao documentDaoMock;
	PictureHbmDao pictureDaoMock;
	ContentVersionHbmDao contentVersionDaoMock;
	ContentServiceSpringImpl contentService;
	ViewComponentHbmDao viewComponentMock;
	
	ViewComponentHbm rootViewComponent;
	List<ViewComponentHbm> roots;
	
	
	
	
	
	@Override	
	protected void setUp() throws Exception {
		//contentVersion
		mockContentVersions = new ArrayList<ContentVersionHbm>();
		ContentVersionHbm mockContentVersion;
		mockContentVersion = new ContentVersionHbmImpl();
		mockContentVersion.setText(contentTemplate.replace("#{contentDocument}", 
				contentWithDocumentTemplate.replace("#{documentId}",Integer.toString(1))).
				replace("#{contentPicture}", contentWithPictureTemplate.replace("#{imageId}", Integer.toString(3))));
		mockContentVersions.add(mockContentVersion);
		mockContentVersion = new ContentVersionHbmImpl();
		mockContentVersion.setText(contentTemplate.replace("#{contentDocument}", 
				contentWithDocumentTemplate.replace("#{documentId}",Integer.toString(2))).replace("#{contentPicture}",""));
		mockContentVersions.add(mockContentVersion);
		mockContentVersion = new ContentVersionHbmImpl();
		mockContentVersion.setText(contentTemplate.replace("#{contentDocument}", "").replace("#{contentPicture}",
				contentWithPictureTemplate.replace("#{imageId}", Integer.toString(1))));
		mockContentVersions.add(mockContentVersion);
		
		//pictures
		mockPictures = new ArrayList<PictureHbm>();	
		mockPictures.add(createPictureHbm(1));
		mockPictures.add(createPictureHbm(2));
		//documents
		mockDocuments = new ArrayList<DocumentHbm>();
		mockDocuments.add(createDocumentHbm(1));
		mockDocuments.add(createDocumentHbm(2));
		mockDocuments.add(createDocumentHbm(3));
		
		contentService = new ContentServiceSpringImpl();
		
		viewComponentMock = EasyMock.createMock(ViewComponentHbmDao.class);
		contentService.setViewComponentHbmDao(viewComponentMock);
		
		contentVersionDaoMock  = EasyMock.createMock(ContentVersionHbmDao.class);
		contentService.setContentVersionHbmDao(contentVersionDaoMock);
		
		documentDaoMock = EasyMock.createMock(DocumentHbmDao.class);
		contentService.setDocumentHbmDao(documentDaoMock);
		
		pictureDaoMock = EasyMock.createMock(PictureHbmDao.class);
		contentService.setPictureHbmDao(pictureDaoMock);
		
		//create tree of view components
		rootViewComponent = createViewComponent(0);
		ViewComponentHbmImpl child1 = createViewComponent(1);
		ViewComponentHbmImpl child2 = createViewComponent(2);
		ViewComponentHbmImpl child3 = createViewComponent(3);
		ViewComponentHbmImpl child31 = createViewComponent(4);
		
		List<ViewComponentHbmImpl> children1 = new ArrayList<ViewComponentHbmImpl>();
		children1.add(child1);
		children1.add(child2);
		children1.add(child3);
		List<ViewComponentHbmImpl> children2 = new ArrayList<ViewComponentHbmImpl>();
		children2.add(child31);
		rootViewComponent.setChildren(children1);
		child3.setChildren(children2);
		roots = new ArrayList<ViewComponentHbm>();
		roots.add(rootViewComponent);
		
	}
	
	ViewComponentHbmImpl createViewComponent(Integer id){
		ViewComponentHbmImpl view = new ViewComponentHbmImpl();
		view.setViewComponentId(id);
		return view;
	}
	
	private PictureHbm createPictureHbm(Integer pictureId){
		PictureHbm picture = new PictureHbmImpl();
		picture.setPictureId(pictureId);
		picture.setHeight(1);
		picture.setWidth(1);
		return picture;
	}
	
	private DocumentHbm createDocumentHbm(Integer documentId){
		DocumentHbm document = new DocumentHbmImpl();
		document.setDocumentId(documentId);
		return document;
	}
	
	public void testGetUnusedResources4Unit(){
		
		EasyMock.expect(viewComponentMock.findRootViewComponents4Unit(EasyMock.anyInt())).andReturn(roots);		
		
		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(mockContentVersions);
		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(new ArrayList<ContentVersionHbm>()).times(4);
		
		
		EasyMock.expect(documentDaoMock.findAll(EasyMock.anyInt())).andReturn(mockDocuments);
		EasyMock.expect(pictureDaoMock.findAllPerUnit(EasyMock.anyInt())).andReturn(mockPictures);
		
		
		EasyMock.replay(viewComponentMock);
		EasyMock.replay(contentVersionDaoMock);
		EasyMock.replay(documentDaoMock);
		EasyMock.replay(pictureDaoMock);
		
		
		
		List result = null;
		try {
			result = contentService.getUnusedResources4Unit(1);
		} catch (UserException e) {
			Assert.assertFalse(true);
		}
		
		EasyMock.verify(contentVersionDaoMock);		
		EasyMock.verify(viewComponentMock);
		EasyMock.verify(documentDaoMock);
		EasyMock.verify(pictureDaoMock);
		
		Assert.assertEquals(2, result.size());
	}
}
