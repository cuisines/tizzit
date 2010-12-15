package de.juwimm.cms.content.panel.util;

import static org.junit.Assert.*;
import static org.powermock.api.easymock.PowerMock.*;

import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PagePanel;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

@RunWith(PowerMockRunner.class)
@PrepareForTest( {PdfPreviewFrame.class})
public class PdfPreviewFrameTest {

	@Test
	public void testSetDocumentContent() throws Exception {
		PDDocument document = new PDDocument();
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		PDPage blankPage = new PDPage();
		document.addPage(blankPage);
		document.save(byteOutputStream);
		document.close();

		PdfPreviewFrame previewFrame = createPartialMockForAllMethodsExcept(PdfPreviewFrame.class, "setDocumentContent");
		previewFrame.setDocumentContent(byteOutputStream.toByteArray());
		Object object = Whitebox.getInternalState(previewFrame, "pdffile");
		assertNotNull(object);
		assertTrue(object instanceof PDFFile);
	}
	
	@Test
	public void testActionNext() throws Exception{
		PdfPreviewFrame previewFrame = createPartialMockForAllMethodsExcept(PdfPreviewFrame.class, "actionNext");
		PDFFile pdfFile=createNiceMock(PDFFile.class);
		Whitebox.setInternalState(previewFrame, "pdffile", pdfFile);
		int currentPage=0;
		Whitebox.setInternalState(previewFrame, "currentPage", currentPage);
		PDFPage pdfPage=createNiceMock(PDFPage.class);
		PagePanel pagePanel=createNiceMock(PagePanel.class);
		Whitebox.setInternalState(previewFrame, "pagePanel", pagePanel);
		ActionEvent actionEvent=createNiceMock(ActionEvent.class);
		
		pdfFile.getNumPages();
		expectLastCall().andReturn(2);
		
		pdfFile.getPage(1);
		expectLastCall().andReturn(pdfPage);
		pagePanel.showPage(pdfPage);
		expectLastCall();
		
		replayAll();
		
		Whitebox.invokeMethod(previewFrame, "actionNext", actionEvent);
		
		verifyAll();
		currentPage=Whitebox.getInternalState(previewFrame, "currentPage");
		assertEquals(1, currentPage);
	}

	@Test
	public void testActionPrevious() throws Exception{
		PdfPreviewFrame previewFrame = createPartialMockForAllMethodsExcept(PdfPreviewFrame.class, "actionPrevious");
		PDFFile pdfFile=createNiceMock(PDFFile.class);
		Whitebox.setInternalState(previewFrame, "pdffile", pdfFile);
		int currentPage=1;
		Whitebox.setInternalState(previewFrame, "currentPage", currentPage);
		PDFPage pdfPage=createNiceMock(PDFPage.class);
		PagePanel pagePanel=createNiceMock(PagePanel.class);
		Whitebox.setInternalState(previewFrame, "pagePanel", pagePanel);
		ActionEvent actionEvent=createNiceMock(ActionEvent.class);
		
		pdfFile.getPage(0);
		expectLastCall().andReturn(pdfPage);
		pagePanel.showPage(pdfPage);
		expectLastCall();
		
		replayAll();
		
		Whitebox.invokeMethod(previewFrame, "actionPrevious", actionEvent);
		
		verifyAll();
		currentPage=Whitebox.getInternalState(previewFrame, "currentPage");
		assertEquals(0, currentPage);
	}

}
