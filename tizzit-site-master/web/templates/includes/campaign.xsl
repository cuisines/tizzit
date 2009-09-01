<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="bildutext" mode="format">
		<font class="text">
			<xsl:value-of select="."/>
			<br/>
		</font>
	</xsl:template>

	<xsl:template match="bildu" mode="format">
		<font class="textb">
			<xsl:value-of select="."/>
			<br/>
		</font>
	</xsl:template>

	<xsl:template match="bildunterschrift" mode="format">
		<font class="bildunter">
			<xsl:value-of select="."/>
			<br/>
		</font>
	</xsl:template>

</xsl:stylesheet><!-- Stylus Studio meta-information - (c)1998-2001 eXcelon Corp.
<metaInformation>
<scenarios/><MapperInfo  srcSchemaPath="" srcSchemaRoot="" srcSchemaPathIsRelative="yes" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" />
</metaInformation>
-->
