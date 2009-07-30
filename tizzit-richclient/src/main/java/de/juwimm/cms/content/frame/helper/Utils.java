/**
 * Copyright (c) 2009 Juwi MacMillan Group GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.juwimm.cms.content.frame.helper;

import java.io.File;
import java.util.HashMap;

import javax.swing.ImageIcon;

import de.juwimm.cms.util.UIConstants;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */

public final class Utils {
	public static final HashMap<String, FileType> FILETYPES_SUPPORTED = new HashMap<String, FileType>(22);
    public static final String JPEG = "jpeg";
    public static final String JPG = "jpg";
    public static final String GIF = "gif";
    public static final String TIFF = "tiff";
    public static final String TIF = "tif";
    public static final String PNG = "png";
    public static final String SVG = "svg";
    public static final String EMF = "emf";
    public static final String WMF = "wmf";
    public static final String BMP = "bmp";
    public static final String DOC = "doc";
    public static final String PDF = "pdf";
    public static final String XLS = "xls";
    public static final String ZIP = "zip";
    public static final String PPT = "ppt";
    public static final String PPS = "pps";
    public static final String ODT = "odt";
    public static final String OTT = "ott";
    public static final String ODS = "ods";
    public static final String DOT = "dot";
    public static final String RTF = "rtf";
    public static final String AVI = "avi";
    public static final String MPG = "mpg";
    public static final String MPEG = "mpeg";
    public static final String MP3 = "mp3";
    public static final String SWF = "swf";
    public static final String WMA = "wma";
    public static final String WMV = "wmv";
    public static final String RM = "rm";
    public static final String MOV = "mov";
    public static final String EXE = "exe";
    public static final String CSV = "csv";
    public static final String XML = "xml";
    public static final String HLP = "hlp";
    public static final String TXT = "txt";

	static {
		FILETYPES_SUPPORTED.put(JPEG, new FileType(JPEG, "image/jpeg", "jpg.png"));
		FILETYPES_SUPPORTED.put(JPG, new FileType(JPG,   "image/jpeg", "jpg.png"));
		FILETYPES_SUPPORTED.put(GIF, new FileType(GIF,   "image/gif", "png.png"));
		FILETYPES_SUPPORTED.put(TIFF, new FileType(TIFF, "image/tif", "tif.png"));
		FILETYPES_SUPPORTED.put(TIF, new FileType(TIF,   "image/tif", "tif.png"));
		FILETYPES_SUPPORTED.put(BMP, new FileType(BMP,   "image/bitmap", "tif.png"));
		FILETYPES_SUPPORTED.put(PNG, new FileType(PNG,   "image/png", "png.png"));
		FILETYPES_SUPPORTED.put(SVG, new FileType(SVG,   "image/svg+xml", "xml.png"));
		FILETYPES_SUPPORTED.put(EMF, new FileType(EMF,   "image/x-emf", "emf.gif"));
		FILETYPES_SUPPORTED.put(WMF, new FileType(WMF,   "image/x-wmf", "emf.gif"));
		FILETYPES_SUPPORTED.put(DOC, new FileType(DOC,   "application/msword", "doc.png"));
		FILETYPES_SUPPORTED.put(DOT, new FileType(DOT,   "application/msword", "doc.png"));
		FILETYPES_SUPPORTED.put(RTF, new FileType(RTF,   "application/rtf", "doc.png"));
		FILETYPES_SUPPORTED.put(PDF, new FileType(PDF,   "application/pdf", "pdf.png"));
		FILETYPES_SUPPORTED.put(XLS, new FileType(XLS,   "application/excel", "xls.png"));
		FILETYPES_SUPPORTED.put(PPT, new FileType(PPT,   "application/powerpoint", "ppt.png"));
		FILETYPES_SUPPORTED.put(PPS, new FileType(PPS,   "application/powerpoint", "ppt.png"));
		FILETYPES_SUPPORTED.put(ZIP, new FileType(ZIP,   "application/zip", "zip.png"));
		FILETYPES_SUPPORTED.put(CSV, new FileType(CSV,   "text/csv", "ascii.png"));
		FILETYPES_SUPPORTED.put(XML, new FileType(XML,   "text/xml", "xml.png"));
		FILETYPES_SUPPORTED.put(AVI, new FileType(AVI,   "video/x-msvideo", "avi.png"));
		FILETYPES_SUPPORTED.put(MPG, new FileType(MPG,   "video/mpeg", "mpg.png"));
		FILETYPES_SUPPORTED.put(MPEG, new FileType(MPEG, "video/mpeg", "mpg.png"));
		FILETYPES_SUPPORTED.put(WMV, new FileType(WMV,   "video/x-msvideo", "avi.png"));
		FILETYPES_SUPPORTED.put(WMA, new FileType(WMA,   "audio/x-ms-wma", "mp3.gif"));
		FILETYPES_SUPPORTED.put(RM, new FileType(RM,     "audio/x-pn-realaudio", "mp3.gif"));
		FILETYPES_SUPPORTED.put(MP3, new FileType(MP3,   "audio/mpeg", "mp3.gif"));
		FILETYPES_SUPPORTED.put(SWF, new FileType(SWF,   "application/x-shockwave-flash", "flash.png"));
		FILETYPES_SUPPORTED.put(EXE, new FileType(EXE,   "application/octet-stream", "exe.gif"));
		FILETYPES_SUPPORTED.put(HLP, new FileType(HLP,   "application/winhlp", "help.png"));
		FILETYPES_SUPPORTED.put(TXT, new FileType(TXT,   "text/plain", "ascii.png"));
		FILETYPES_SUPPORTED.put(ODT, new FileType(ODT,   "application/vnd.oasis.opendocument.spreadsheet", "doc.png"));
		FILETYPES_SUPPORTED.put(OTT, new FileType(OTT,   "application/vnd.oasis.opendocument.text-template", "doc.png"));
		FILETYPES_SUPPORTED.put(ODS, new FileType(ODS,   "application/vnd.oasis.opendocument.spreadsheet", "xls.xls"));
	}

	private Utils() {
	}

	public static boolean containsFileType(String fileType) {
		return FILETYPES_SUPPORTED.containsKey(fileType.toLowerCase());
	}

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static String getExtension(String filename) {
        String ext = null;
        String s = filename;
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static String getMimeType4Extension(String fext) {
        return FILETYPES_SUPPORTED.get(fext).getMimeType();
    }

    public static ImageIcon getIcon4Extension(String fext) {
        String iconname = "";
		try {
			iconname = FILETYPES_SUPPORTED.get(fext).getImageName();
		} catch (Exception exep) {
			iconname = FILETYPES_SUPPORTED.get(EXE).getImageName();
		}
        return UIConstants.getDocumentsIcon(iconname);
    }

	public static FileType getFileType(String fext) {
		return FILETYPES_SUPPORTED.get(fext);
	}

	/**
	 * <p>Title: ConQuest</p>
	 * <p>Description: Enterprise Content Management</p>
	 * <p>Copyright: Copyright (c) 2004</p>
	 * InnerClass to represent a FileType.
	 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
	 * @version $Revision: 1.32 $
	 */
	public static class FileType {
		private String extension, mimeType, imageName;

		public FileType(String ftExtension, String ftMimeType, String ftImageName) {
			this.extension = ftExtension;
			this.mimeType = ftMimeType;
			this.imageName = ftImageName;
		}

		public String getExtension() {
			return extension;
		}

		public String getMimeType() {
			return mimeType;
		}

		public String getImageName() {
			return imageName;
		}
	}
}
