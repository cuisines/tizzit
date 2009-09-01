<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template"
xmlns:cinclude="http://apache.org/cocoon/include/1.0">
	
	<!--	
		<xsl:param name="viewComponentId" select="''"/>	
		<xsl:param name="language" select="''"/>
		<xsl:param name="liveserver" select="''"/>
	-->
	
	<!-- 
		Habe diese drei variablen auskommentiert, da sie redundant in 
		tizzit-site-master/../variables.xsl sind. Falls es in manchen Mandanten zu Problemen 
		kommt, bitte in Mandant variables.xsl hinzufügen. Wenn sie hier drin stehen, fliegen 
		bei den standard Mandanten Fehler "duplicate variables declaration..",Hato, 26.1.2008
	-->
	
	<xsl:template match="iteration" priority="3"/>

	<xsl:template match="params" mode="format" priority="3"/>
	
	<xsl:template match="includexml" mode="format" priority="-0.5">
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
					<!-- This entry is to add specific flowscript params in the mandants -->
					<xsl:apply-templates select="." mode="custom-params"/>
					<xsl:apply-templates select="../params/item" mode="item2param"/>
				</xsl:attribute>	
			</cinclude:include>
		</div>	    
	</xsl:template>
	
	<xsl:template match="item" mode="item2param"  priority="-0.5">		
		<xsl:text>&amp;</xsl:text>
		<xsl:value-of select="param-name"/>
		<xsl:text>=</xsl:text>
		<xsl:value-of select="param-value"/>		
	</xsl:template>
	
	<xsl:template match="includexml" mode="custom-params" priority="-1"/>	
		
</xsl:stylesheet>