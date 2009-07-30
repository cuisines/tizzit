<html>
<body>
<script language="JavaScript">
	returnUrl = window.location.toString();
	returnUrl = returnUrl.substring(0, returnUrl.lastIndexOf("/"));
	returnUrl = returnUrl + "/admin.jnlp";
	document.write("<object classid=\"clsid:8AD9C840-044E-11D1-B3E9-00805F499D93\" codebase=\"http://java.sun.com/update/1.5.0/jinstall-1_5_0_06-windows-i586.cab#Version=5,0,60,5\" width=\"0\" height=\"0\">");
 document.write("<param name=\"type\" value=\"application/x-java-applet;version=1.5\">");
 document.write("<param name=\"code\" value=\"com.sun.java.Main\">");
 document.write("<param name=\"codebase\" value=\"/applet\">");
 document.write("<param name=\"archive\" value=\"download.jar\">");
 document.write("<param name=\"scriptable\" value=\"false\">");
 document.write("<param name=\"success\" value=\""+returnUrl+"\">");
 document.write("<param name=\"bgcolor\" value=\"ffffff\">");
/*	document.write("<PARAM NAME=\"app\" VALUE=\""+returnUrl+"\">");
	document.write("<PARAM NAME=\"back\" VALUE=\"true\">");*/
	document.write("<A HREF=\"http://java.sun.com/cgi-bin/javawebstart-platform.sh?\">Download Java Web Start</A></OBJECT>");
</script>
</body>
</html>
