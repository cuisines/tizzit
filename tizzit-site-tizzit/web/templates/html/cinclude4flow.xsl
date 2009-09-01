<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template"
	xmlns:cinclude="http://apache.org/cocoon/include/1.0">
	
	<xsl:include href="standard.xsl"/>	
	<xsl:include href="../../../../tizzit-site-master/web/templates/cinclude4flow.xsl"/>	
	
	<xsl:param name="safeguardUsername" select="''"/>
	
	<xsl:template match="includexml" mode="format" priority="2">
		<div class="form">
			<cinclude:include ignoreErrors="true" xmlns:cinclude="http://apache.org/cocoon/include/1.0">
				<xsl:attribute name="src">
					<xsl:value-of select="."/>
					<xsl:text>?viewComponentId=</xsl:text>
					<xsl:value-of select="$viewComponentId"/>
					<xsl:text>&amp;</xsl:text>
					<xsl:text>language=</xsl:text>
					<xsl:value-of select="$language"/>
					<xsl:text>&amp;</xsl:text>
					<xsl:text>liveserver=</xsl:text>
					<xsl:value-of select="$liveserver"/>
					<xsl:text>&amp;</xsl:text>
					<xsl:text>safeguardUsername=</xsl:text>
					<xsl:value-of select="$safeguardUsername"/>
					<!-- This entry is to add specific flowscript params in the mandants -->
					<xsl:apply-templates select="." mode="custom-params"/>
					<xsl:apply-templates select="../params/item" mode="item2param"/>
				</xsl:attribute>	
			</cinclude:include>
		</div>	    
	</xsl:template>
	
</xsl:stylesheet>