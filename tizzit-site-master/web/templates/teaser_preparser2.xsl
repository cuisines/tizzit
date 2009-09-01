<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cinclude="http://apache.org/cocoon/include/1.0">

<!-- Dieser teaser preparser ist fuer content ressource matcher auf "/" und ohne "-"  -->

	<xsl:include href="variables.xsl"/>		
	<xsl:param name="siteId" select="''"/>


    <xsl:template match="source">
    	<source>
    		<xsl:apply-templates select="//item[internalLink/internalLink/@viewid]"/>
    	</source>
    </xsl:template>
	
	<xsl:template match="item" priority="1">
		<item>
			<cinclude:include ignoreErrors="true" xmlns:cinclude="http://apache.org/cocoon/include/1.0">
				<xsl:attribute name="src"><xsl:value-of select="$preStringCinclude"/>contentresource/<xsl:value-of select="internalLink/internalLink/@viewid"/>/teaser/<xsl:value-of select="$currentDate"/>/<xsl:value-of select="internalLink/internalLink/@language"/>/<xsl:value-of select="internalLink/internalLink/@url"/>/content.xml?<xsl:value-of select="$siteId"/></xsl:attribute>	
			</cinclude:include>
		</item>
	</xsl:template>

 <!-- ignoreErrors="true" -->
	<xsl:template match="@*|node()" priority="-1">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>

