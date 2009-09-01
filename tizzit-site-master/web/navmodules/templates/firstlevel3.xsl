<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="firstlevel1.xsl"/>
	<xsl:template match="navigation" priority="1">
		<xsl:if test="viewcomponent/viewcomponent[showType='3']">
			<xsl:apply-templates select="viewcomponent/viewcomponent[showType='3']"/>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>


