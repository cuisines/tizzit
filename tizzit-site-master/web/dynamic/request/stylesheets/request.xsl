<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="/">
		<html>
			<head>
				<title>Request-Parameter</title>
				<!--<link rel="stylesheet" href="/httpd/css/main.css" type="text/css"/>-->
			</head>
			<body class="text">
				<xsl:apply-templates/>
			</body>
		</html>
	</xsl:template>

	<xsl:template match="requestparams">
		<table border="1" cellpadding="2" cellspacing="2">
			<xsl:apply-templates mode="table"/>
		</table>
	</xsl:template>

	<xsl:template match="@*|node()" priority="-2" mode="table">
		<tr>
			<td align="right"><xsl:value-of select="local-name(.)"/></td>
			<td align="left"><xsl:value-of select="text()"/></td>
		</tr>
	</xsl:template>

</xsl:stylesheet><!-- Stylus Studio meta-information - (c)1998-2001 eXcelon Corp.
<metaInformation>
<scenarios/><MapperInfo  srcSchemaPath="" srcSchemaRoot="" srcSchemaPathIsRelative="yes" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" />
</metaInformation>
-->
