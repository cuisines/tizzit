package de.juwimm.cms.remote.test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.easymock.LogicalOperator;

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.Constants.ResourceUsageState;
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
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.model.UnitHbmImpl;
import de.juwimm.cms.model.ViewComponentHbm;
import de.juwimm.cms.model.ViewComponentHbmDao;
import de.juwimm.cms.model.ViewComponentHbmImpl;
import de.juwimm.cms.remote.ContentServiceSpringImpl;
import de.juwimm.cms.vo.DocumentSlimValue;
import de.juwimm.cms.vo.PictureSlimstValue;

/**
 * @author fzalum
 *
 */
public class ContentServiceTest extends TestCase {

	List<ContentVersionHbm> mockContentVersions1;
	List<ContentVersionHbm> mockContentVersions2;
	List<ContentVersionHbm> mockContentVersions3;
	List<ContentVersionHbm> mockContentVersions4;
	List<PictureHbm> mockPictures;
	List<DocumentHbm> mockDocuments;
	DocumentHbm documentToDelete;

	String contentWithDocumentTemplate = "<documents dcfname='de.juwimm.cms.content.modules.Documents' description='test' label='Dokumente'>" + "		<document displayType='block' documentName='something.pdf' src='#{documentId}'>test</document>" + "	</documents>";
	String contentWithPictureTemplate1 = "<picture dcfname='de.juwimm.cms.content.modules.Picture' description='#{imageId}' label='Bild'>" + "<image height='16' src='30' type='center' width='16'><legend/><filename>cms_16x16.gif</filename><alttext/></image>" + "</picture>";
	String contentWithPictureTemplate2 = "<picture dcfname='picture' label='Teaser Image (198 px Breite)'>	<image height='60' src='#{imageId}' type='center' width='82'>		<legend><![CDATA[]]></legend>		<filename><![CDATA[bda.gif]]></filename>		<alttext><![CDATA[]]></alttext>	</image></picture>";

	String contentTemplate = "<source>" + "<head/>" + "	<all>" + " #{contentDocument}" + " #{contentPicture}" + "	</all>" + "</source>";

	DocumentHbmDao documentDaoMock;
	PictureHbmDao pictureDaoMock;
	ContentVersionHbmDao contentVersionDaoMock;
	ContentServiceSpringImpl contentService;
	ViewComponentHbmDao viewComponentMock;

	ViewComponentHbm rootViewComponent;
	List<ViewComponentHbm> roots;

	/**
	 * 
	 * @param imageId
	 * @param documentId
	 * @return
	 */
	private String createNewContentXml1(Integer imageId, Integer documentId) {
		return createContentXml(contentWithPictureTemplate1, contentWithDocumentTemplate, imageId, documentId);
	}

	/**
	 * 
	 * @param imageId
	 * @param documentId
	 * @return
	 */
	private String createNewContentXml2(Integer imageId, Integer documentId) {
		return createContentXml(contentWithPictureTemplate2, contentWithDocumentTemplate, imageId, documentId);
	}

	private String createContentXml(String templateImage, String templateDocument, Integer imageId, Integer documentId) {
		return contentTemplate.replace("#{contentDocument}", (documentId != null ? templateDocument.replace("#{documentId}", documentId.toString()) : "")).replace("#{contentPicture}", (imageId != null ? templateImage.replace("#{imageId}", imageId.toString()) : ""));
	}

	/**
	 * 
	 * @param version
	 * @param imageId
	 * @param documentId
	 * @return
	 */
	private ContentVersionHbm createContentVersion1(String version, Integer imageId, Integer documentId) {
		ContentVersionHbm mockContentVersion = new ContentVersionHbmImpl();
		mockContentVersion.setText(createNewContentXml1(imageId, documentId));
		mockContentVersion.setVersion(version);
		return mockContentVersion;
	}

	/**
	 * 
	 * @param version
	 * @param imageId
	 * @param documentId
	 * @return
	 */
	private ContentVersionHbm createContentVersion2(String version, Integer imageId, Integer documentId) {
		ContentVersionHbm mockContentVersion = new ContentVersionHbmImpl();
		mockContentVersion.setText(createNewContentXml2(imageId, documentId));
		mockContentVersion.setVersion(version);
		return mockContentVersion;
	}

	@Override
	protected void setUp() throws Exception {
		//contentVersion
		mockContentVersions1 = new ArrayList<ContentVersionHbm>();
		mockContentVersions2 = new ArrayList<ContentVersionHbm>();
		mockContentVersions3 = new ArrayList<ContentVersionHbm>();
		mockContentVersions4 = new ArrayList<ContentVersionHbm>();

		mockContentVersions1.add(createContentVersion1("1", null, 1));
		mockContentVersions1.add(createContentVersion1("2", null, 2));
		mockContentVersions1.add(createContentVersion1("3", 11, 2));

		mockContentVersions2.add(createContentVersion2("1", null, null));
		mockContentVersions2.add(createContentVersion2(Constants.PUBLISH_VERSION, 13, null));

		mockContentVersions3.add(createContentVersion1("1", 11, 1));
		mockContentVersions3.add(createContentVersion1("2", null, 1));
		mockContentVersions3.add(createContentVersion1("3", 12, 1));

		mockContentVersions4.add(createContentVersion1("1", null, null));
		mockContentVersions4.add(createContentVersion1("2", null, null));
		mockContentVersions4.add(createContentVersion1(Constants.PUBLISH_VERSION, null, 3));

		//pictures
		mockPictures = new ArrayList<PictureHbm>();
		mockPictures.add(createPictureHbm(11));
		mockPictures.add(createPictureHbm(12));
		mockPictures.add(createPictureHbm(13));
		//documents
		mockDocuments = new ArrayList<DocumentHbm>();
		mockDocuments.add(createDocumentHbm(1));
		mockDocuments.add(createDocumentHbm(2));
		mockDocuments.add(createDocumentHbm(3));
		documentToDelete = createDocumentHbm(1);
		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(1);
		documentToDelete.setUnit(unit);

		contentService = new ContentServiceSpringImpl();

		viewComponentMock = EasyMock.createMock(ViewComponentHbmDao.class);
		contentService.setViewComponentHbmDao(viewComponentMock);

		contentVersionDaoMock = EasyMock.createMock(ContentVersionHbmDao.class);
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

	ViewComponentHbmImpl createViewComponent(Integer id) {
		ViewComponentHbmImpl view = new ViewComponentHbmImpl();
		view.setViewComponentId(id);
		return view;
	}

	private PictureHbm createPictureHbm(Integer pictureId) {
		return createPictureHbm(pictureId, pictureId);
	}

	private PictureHbm createPictureHbm(Integer pictureId, Integer unitId) {
		PictureHbm picture = new PictureHbmImpl();
		picture.setPictureId(pictureId);
		picture.setHeight(1);
		picture.setWidth(1);
		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(unitId);
		picture.setUnit(unit);
		return picture;
	}

	private DocumentHbm createDocumentHbm(Integer documentId) {
		DocumentHbm document = new DocumentHbmImpl();
		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(documentId);
		document.setDocumentId(documentId);
		document.setUnit(unit);
		return createDocumentHbm(documentId, documentId);
	}

	private DocumentHbm createDocumentHbm(Integer documentId, Integer unitId) {
		DocumentHbm document = new DocumentHbmImpl();
		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(unitId);
		document.setDocumentId(documentId);
		document.setUnit(unit);
		return document;
	}

	public void testGetUnusedResources4Unit1() {

		EasyMock.expect(viewComponentMock.findRootViewComponents4Unit(EasyMock.anyInt())).andReturn(roots);

		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(mockContentVersions1);
		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(mockContentVersions2);
		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(new ArrayList<ContentVersionHbm>()).times(3);

		EasyMock.expect(documentDaoMock.findAll(EasyMock.anyInt())).andReturn(mockDocuments);
		EasyMock.expect(pictureDaoMock.findAllPerUnit(EasyMock.anyInt())).andReturn(mockPictures);

		EasyMock.replay(viewComponentMock);
		EasyMock.replay(contentVersionDaoMock);
		EasyMock.replay(documentDaoMock);
		EasyMock.replay(pictureDaoMock);

		Map<Object, ResourceUsageState> result = null;
		try {
			result = contentService.getResources4Unit(1, true, false, true, false);
		} catch (UserException e) {
			Assert.assertFalse(true);
		}

		EasyMock.verify(contentVersionDaoMock);
		EasyMock.verify(viewComponentMock);
		EasyMock.verify(documentDaoMock);
		EasyMock.verify(pictureDaoMock);

		Assert.assertEquals(4, result.size());

		for (Entry<Object, ResourceUsageState> resource : result.entrySet()) {
			if (resource.getKey() instanceof DocumentSlimValue) {
				if (((DocumentSlimValue) resource.getKey()).getDocumentId().equals(1)) {
					if (resource.getValue() == ResourceUsageState.Used) {
						Assert.assertTrue(false);
					}
				}
				if (((DocumentSlimValue) resource.getKey()).getDocumentId().equals(2)) {
					if (resource.getValue() == ResourceUsageState.UsedInOlderVersions) {
						Assert.assertTrue(false);
					}
				}
			} else if (resource.getKey() instanceof PictureSlimstValue) {
				if (resource.getValue() != ResourceUsageState.Used) Assert.assertTrue(false);
			}
		}
	}

	public void testGetUnusedResources4Unit2() {

		EasyMock.expect(viewComponentMock.findRootViewComponents4Unit(EasyMock.anyInt())).andReturn(roots);

		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(mockContentVersions1);
		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(mockContentVersions2);
		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(new ArrayList<ContentVersionHbm>()).times(3);

		EasyMock.expect(documentDaoMock.findAll(EasyMock.anyInt())).andReturn(mockDocuments);
		EasyMock.expect(pictureDaoMock.findAllPerUnit(EasyMock.anyInt())).andReturn(mockPictures);

		EasyMock.replay(viewComponentMock);
		EasyMock.replay(contentVersionDaoMock);
		EasyMock.replay(documentDaoMock);
		EasyMock.replay(pictureDaoMock);

		Map<Object, ResourceUsageState> result = null;
		try {
			result = contentService.getResources4Unit(1, false, true, false, true);
		} catch (UserException e) {
			Assert.assertFalse(true);
		}

		EasyMock.verify(contentVersionDaoMock);
		EasyMock.verify(viewComponentMock);
		EasyMock.verify(documentDaoMock);
		EasyMock.verify(pictureDaoMock);

		Assert.assertEquals(2, result.size());

		for (Entry<Object, ResourceUsageState> resource : result.entrySet()) {
			if (resource.getKey() instanceof DocumentSlimValue) {
				if (((DocumentSlimValue) resource.getKey()).getDocumentId().equals(3)) {
					if (resource.getValue() != ResourceUsageState.Unsused) {
						Assert.assertTrue(false);
					}
				}
			} else if (resource.getKey() instanceof PictureSlimstValue) {
				if (((PictureSlimstValue) resource.getKey()).getPictureId().equals(12)) {
					if (resource.getValue() != ResourceUsageState.Unsused) {
						Assert.assertTrue(false);
					}
				}
			}
		}
	}

	public void testGetUnusedResources4Unit3() {

		EasyMock.expect(viewComponentMock.findRootViewComponents4Unit(EasyMock.anyInt())).andReturn(roots);

		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(mockContentVersions1);
		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(mockContentVersions2);
		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(new ArrayList<ContentVersionHbm>()).times(3);

		EasyMock.expect(documentDaoMock.findAll(EasyMock.anyInt())).andReturn(mockDocuments);
		EasyMock.expect(pictureDaoMock.findAllPerUnit(EasyMock.anyInt())).andReturn(mockPictures);

		EasyMock.replay(viewComponentMock);
		EasyMock.replay(contentVersionDaoMock);
		EasyMock.replay(documentDaoMock);
		EasyMock.replay(pictureDaoMock);

		Map<Object, ResourceUsageState> result = null;
		try {
			result = contentService.getResources4Unit(1, true, true, true, true);
		} catch (UserException e) {
			Assert.assertFalse(true);
		}

		EasyMock.verify(contentVersionDaoMock);
		EasyMock.verify(viewComponentMock);
		EasyMock.verify(documentDaoMock);
		EasyMock.verify(pictureDaoMock);

		Assert.assertEquals(6, result.size());
	}

	public void testGetUnusedResources4Unit4() {

		EasyMock.expect(viewComponentMock.findRootViewComponents4Unit(EasyMock.anyInt())).andReturn(roots);

		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(mockContentVersions1);
		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(mockContentVersions2);
		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(new ArrayList<ContentVersionHbm>()).times(3);

		EasyMock.expect(documentDaoMock.findAll(EasyMock.anyInt())).andReturn(mockDocuments);
		EasyMock.expect(pictureDaoMock.findAllPerUnit(EasyMock.anyInt())).andReturn(mockPictures);

		EasyMock.replay(viewComponentMock);
		EasyMock.replay(contentVersionDaoMock);
		EasyMock.replay(documentDaoMock);
		EasyMock.replay(pictureDaoMock);

		Map<Object, ResourceUsageState> result = null;
		try {
			result = contentService.getResources4Unit(1, true, true, false, false);
		} catch (UserException e) {
			Assert.assertFalse(true);
		}

		EasyMock.verify(contentVersionDaoMock);
		EasyMock.verify(viewComponentMock);
		EasyMock.verify(documentDaoMock);
		EasyMock.verify(pictureDaoMock);

		Assert.assertEquals(3, result.size());

		for (Entry<Object, ResourceUsageState> resource : result.entrySet()) {
			if (resource.getKey() instanceof DocumentSlimValue) {
				if (((DocumentSlimValue) resource.getKey()).getDocumentId().equals(1)) {
					if (resource.getValue() == ResourceUsageState.Used) {
						Assert.assertTrue(false);
					}
				}
				if (((DocumentSlimValue) resource.getKey()).getDocumentId().equals(2)) {
					if (resource.getValue() == ResourceUsageState.UsedInOlderVersions) {
						Assert.assertTrue(false);
					}
				}
				if (((DocumentSlimValue) resource.getKey()).getDocumentId().equals(2)) {
					if (resource.getValue() == ResourceUsageState.UsedInOlderVersions) {
						Assert.assertTrue(false);
					}
				}
			}
		}
	}

	public void testGetDocumentUsage1() {
		DocumentHbm mockDocument = createDocumentHbm(1);

		EasyMock.expect(viewComponentMock.findRootViewComponents4Unit(EasyMock.anyInt())).andReturn(roots);

		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(mockContentVersions1);
		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(mockContentVersions2);
		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(new ArrayList<ContentVersionHbm>()).times(3);

		EasyMock.expect(documentDaoMock.load(EasyMock.anyInt())).andReturn(mockDocument);

		EasyMock.replay(viewComponentMock);
		EasyMock.replay(contentVersionDaoMock);
		EasyMock.replay(documentDaoMock);

		Set result = null;
		try {
			result = contentService.getDocumentUsage(1);
		} catch (UserException e) {
			Assert.assertFalse(true);
		}

		EasyMock.verify(contentVersionDaoMock);
		EasyMock.verify(viewComponentMock);
		EasyMock.verify(documentDaoMock);

		Assert.assertEquals(1, result.size());

	}

	public void testGetDocumentUsage2() {
		DocumentHbm mockDocument = createDocumentHbm(1);

		EasyMock.expect(viewComponentMock.findRootViewComponents4Unit(EasyMock.anyInt())).andReturn(roots);

		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(mockContentVersions1);
		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(mockContentVersions2);
		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(new ArrayList<ContentVersionHbm>()).times(3);

		EasyMock.expect(documentDaoMock.load(EasyMock.anyInt())).andReturn(mockDocument);

		EasyMock.replay(viewComponentMock);
		EasyMock.replay(contentVersionDaoMock);
		EasyMock.replay(documentDaoMock);

		Set result = null;
		try {
			result = contentService.getDocumentUsage(3);
		} catch (UserException e) {
			Assert.assertFalse(true);
		}

		EasyMock.verify(contentVersionDaoMock);
		EasyMock.verify(viewComponentMock);
		EasyMock.verify(documentDaoMock);

		Assert.assertEquals(0, result.size());

	}

	public void testGetPictureUsage() {
		PictureHbm mockPicture = createPictureHbm(1);

		EasyMock.expect(viewComponentMock.findRootViewComponents4Unit(EasyMock.anyInt())).andReturn(roots);

		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(mockContentVersions1);
		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(mockContentVersions2);
		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(new ArrayList<ContentVersionHbm>()).times(3);

		EasyMock.expect(pictureDaoMock.load(EasyMock.anyInt())).andReturn(mockPicture);

		EasyMock.replay(viewComponentMock);
		EasyMock.replay(contentVersionDaoMock);
		EasyMock.replay(pictureDaoMock);

		Set result = null;
		try {
			result = contentService.getPictureUsage(11);
		} catch (UserException e) {
			Assert.assertFalse(true);
		}

		EasyMock.verify(contentVersionDaoMock);
		EasyMock.verify(viewComponentMock);
		EasyMock.verify(pictureDaoMock);

		Assert.assertEquals(1, result.size());

	}

	public void testRemoveResources() {
		Integer[] pictureIds = new Integer[] {11, 12};
		Integer[] documentIds = new Integer[] {1, 3};

		EasyMock.expect(viewComponentMock.findRootViewComponents4Unit(EasyMock.anyInt())).andReturn(roots);

		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(mockContentVersions3);
		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(mockContentVersions4);
		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(new ArrayList<ContentVersionHbm>()).times(3);

		EasyMock.expect(documentDaoMock.load(EasyMock.anyInt())).andReturn(createDocumentHbm(documentIds[0], 1));
		EasyMock.expect(documentDaoMock.load(EasyMock.anyInt())).andReturn(createDocumentHbm(documentIds[1], 1));

		EasyMock.expect(pictureDaoMock.load(EasyMock.anyInt())).andReturn(createPictureHbm(pictureIds[0], 1));
		EasyMock.expect(pictureDaoMock.load(EasyMock.anyInt())).andReturn(createPictureHbm(pictureIds[1], 1));

		EasyMock.replay(viewComponentMock);
		EasyMock.replay(contentVersionDaoMock);
		EasyMock.replay(documentDaoMock);
		EasyMock.replay(pictureDaoMock);
		boolean validationError = false;

		try {
			contentService.removeResources(pictureIds, documentIds, false);
		} catch (Exception e) {
			Assert.assertEquals("validation exception", e.getCause().getMessage());
			validationError = true;
		}

		EasyMock.verify(contentVersionDaoMock);
		EasyMock.verify(viewComponentMock);
		EasyMock.verify(documentDaoMock);
		EasyMock.verify(pictureDaoMock);

		Assert.assertTrue(validationError);
	}

	public void testRemoveResources2() {
		Integer[] pictureIds = new Integer[] {13};
		Integer[] documentIds = new Integer[] {2};

		EasyMock.expect(viewComponentMock.findRootViewComponents4Unit(EasyMock.anyInt())).andReturn(roots);

		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(mockContentVersions3);
		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(mockContentVersions4);
		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(new ArrayList<ContentVersionHbm>()).times(3);

		EasyMock.expect(documentDaoMock.load(EasyMock.anyInt())).andReturn(createDocumentHbm(documentIds[0], 1));
		EasyMock.expect(pictureDaoMock.load(EasyMock.anyInt())).andReturn(createPictureHbm(pictureIds[0], 1));

		pictureDaoMock.deletePictures((Integer[]) EasyMock.anyObject());
		documentDaoMock.deleteDocuments((Integer[]) EasyMock.anyObject());

		EasyMock.replay(viewComponentMock);
		EasyMock.replay(contentVersionDaoMock);
		EasyMock.replay(documentDaoMock);
		EasyMock.replay(pictureDaoMock);

		try {
			contentService.removeResources(pictureIds, documentIds, false);
		} catch (Exception e) {
			Assert.assertFalse(true);
		}

		EasyMock.verify(contentVersionDaoMock);
		EasyMock.verify(viewComponentMock);
		EasyMock.verify(documentDaoMock);
		EasyMock.verify(pictureDaoMock);

	}

	@SuppressWarnings("unchecked")
	public void testRemoveResources3() {
		Integer[] pictureIds = new Integer[] {11, 13};
		Integer[] documentIds = new Integer[] {2};

		EasyMock.expect(viewComponentMock.findRootViewComponents4Unit(EasyMock.anyInt())).andReturn(roots);

		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(mockContentVersions3);
		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(mockContentVersions4);
		EasyMock.expect(contentVersionDaoMock.findContentVersionsByViewComponent(EasyMock.anyInt())).andReturn(new ArrayList<ContentVersionHbm>()).times(3);

		EasyMock.expect(documentDaoMock.load(EasyMock.anyInt())).andReturn(createDocumentHbm(documentIds[0], 1));
		EasyMock.expect(pictureDaoMock.load(EasyMock.anyInt())).andReturn(createPictureHbm(pictureIds[0], 1));
		EasyMock.expect(pictureDaoMock.load(EasyMock.anyInt())).andReturn(createPictureHbm(pictureIds[1], 1));
		//check if remove method is called with 2 elements
		List mockDeleteContents = new ArrayList();
		mockDeleteContents.add(null);
		contentVersionDaoMock.remove(EasyMock.cmp(mockDeleteContents, new Comparator<List>() {
			public int compare(List o1, List o2) {

				return o1.size() == o2.size() ? 0 : -1;
			}

		}, LogicalOperator.EQUAL));

		pictureDaoMock.deletePictures((Integer[]) EasyMock.anyObject());
		documentDaoMock.deleteDocuments((Integer[]) EasyMock.anyObject());

		EasyMock.replay(viewComponentMock);
		EasyMock.replay(contentVersionDaoMock);
		EasyMock.replay(documentDaoMock);
		EasyMock.replay(pictureDaoMock);

		try {
			contentService.removeResources(pictureIds, documentIds, true);
		} catch (Exception e) {
			Assert.assertFalse(true);
		}

		EasyMock.verify(contentVersionDaoMock);
		EasyMock.verify(viewComponentMock);
		EasyMock.verify(documentDaoMock);
		EasyMock.verify(pictureDaoMock);

	}	

}
