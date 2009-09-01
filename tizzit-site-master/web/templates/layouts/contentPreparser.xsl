<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">
	
	<xsl:include href="../variables.xsl"/>
	
	<xsl:template match="@*|node()" priority="-2">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="contentresource" priority="1">
		<xsl:apply-templates mode="contentresource"/>
	</xsl:template>
	
	<xsl:template match="cached-include" mode="contentresource" priority="1">
		<cinclude:cached-include xmlns:cinclude="http://apache.org/cocoon/include/1.0">
			<xsl:attribute name="src">
				<xsl:value-of select="$preStringCinclude"/>contentresource/<xsl:value-of select="$viewComponentId"/>/<xsl:value-of select="$template"/>/<xsl:value-of select="$currentDate"/>/<xsl:value-of select="$language"/>/<xsl:value-of select="$url"/>/<xsl:value-of select="@src"/><xsl:value-of select="$requestQuery"/></xsl:attribute>
		</cinclude:cached-include>
	</xsl:template>
	
</xsl:stylesheet>