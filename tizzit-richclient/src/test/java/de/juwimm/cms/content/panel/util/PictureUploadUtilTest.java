package de.juwimm.cms.content.panel.util;

import static org.junit.Assert.assertEquals;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import sun.awt.image.ToolkitImage;

@RunWith(PowerMockRunner.class)
public class PictureUploadUtilTest {

	@Test
	public void testGetBytesFromFile() throws IOException {
		File file=File.createTempFile("JUnit_", ".garbage");
		file.createNewFile();
		byte[] bs=PictureUploadUtil.getBytesFromFile(file);
		assertEquals(0, bs.length);
		FileOutputStream fos = new FileOutputStream(file);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		bos.write(new byte[]{0x11, 0x12});
		bos.flush();
		bos.close();
		fos.flush();
		fos.close();
		bs=PictureUploadUtil.getBytesFromFile(file);
		assertEquals(2, bs.length);
		file.delete();
	}
	
	@Ignore
	public void testManipulateImage(){
		Image image=new BufferedImage(500, 500, BufferedImage.TYPE_BYTE_BINARY);
		ByteArrayOutputStream stream=PictureUploadUtil.manipulateImage(image);
		assertEquals(4723, stream.size());
	}
}
