<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:param name="ejbimageid" select="'55'"/>
	<xsl:param name="clientCode" select="'tk'"/>


	<xsl:template match="dummy">
		<html>
			<head>
				<title>Bild</title>
			<script language="JavaScript" src="/jscript/scripts.js">
			</script>
			</head>
			<body MARGINWIDTH="0" MARGINHEIGHT="0" LEFTMARGIN="0" TOPMARGIN="0">
				<img hspace="0" vspace="0">
					<xsl:attribute name="src">/img/ejbimage/dummy.jpg?id=<xsl:value-of select="$ejbimageid"/>&amp;typ=s</xsl:attribute>
				</img>
			</body>
		</html>
	</xsl:template>

	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>
</xsl:stylesheet>
