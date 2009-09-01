<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:import href="master.xsl"/>

	<xsl:template match="navigation" priority="1">
		<xsl:if test="viewcomponent/viewcomponent[showType='2']">
					<xsl:apply-templates select="viewcomponent/viewcomponent[showType='2']"/>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
<!-- Stylus Studio meta-information - (c)1998-2001 eXcelon Corp.
<metaInformation>
<scenarios/><MapperInfo  srcSchemaPath="" srcSchemaRoot="" srcSchemaPathIsRelative="yes" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" />
</metaInformation>
-->
