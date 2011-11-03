package de.juwimm.cms.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;

public class PdfUtils {
	private static Logger log = Logger.getLogger(PdfUtils.class);
	
	public static boolean isPassswordProtected(File file){
		boolean result=false;
		FileInputStream fis=null;
		try {
			fis = new FileInputStream(file);
			PDDocument pdfDocument = PDDocument.load(fis);
			result=pdfDocument.isEncrypted();
			if (pdfDocument != null) {
				pdfDocument.close();
			}
		} catch (FileNotFoundException e) {
			log.error("Cold not open PDF file to check for encryption",e);
		} catch (IOException e) {
			log.error("Cold not open PDF file to check for encryption",e);
		} finally {
			if(fis!=null)
			IOUtils.closeQuietly(fis);
			
		}
		return result;
	}

}
