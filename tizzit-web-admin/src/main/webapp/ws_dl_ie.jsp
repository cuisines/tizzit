<html>
<body>
<script language="JavaScript">
	returnUrl = window.location.toString();
	returnUrl = returnUrl.substring(0, returnUrl.lastIndexOf("/"));
	returnUrl = returnUrl + "/juwimm-cms-client.jnlp";
	document.write("<object classid=\"clsid:5852F5ED-8BF4-11D4-A245-0080C6F74284\" codebase=\"http://java.sun.com/update/1.6.0/jinstall-6-windows-i586.cab#Version=6,0,0,0\" width=\"0\" height=\"0\">");
 document.write("<param name=\"type\" value=\"application/x-java-applet;version=1.5\">");
 document.write("<param name=\"code\" value=\"com.sun.java.Main\">");
 document.write("<param name=\"codebase\" value=\"/applet\">");
 document.write("<param name=\"archive\" value=\"download.jar\">");
 document.write("<param name=\"scriptable\" value=\"false\">");
 document.write("<param name=\"success\" value=\""+returnUrl+"\">");
 document.write("<param name=\"bgcolor\" value=\"ffffff\">");
/*	document.write("<PARAM NAME=\"app\" VALUE=\""+returnUrl+"\">");
	document.write("<PARAM NAME=\"back\" VALUE=\"true\">");*/
	document.write("<A HREF=\"http://www.java.com\">Download Java Web Start</A></OBJECT>");
</script>
</body>
</html>
