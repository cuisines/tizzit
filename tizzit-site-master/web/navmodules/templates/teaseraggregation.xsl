<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">
    
    <xsl:include href="variables.xsl"/>
    
    <xsl:variable name="teaserId" select="//viewcomponent/viewcomponent[template ='teaseraggregation']/@id"/>
    <xsl:variable name="teaserPath" select="//viewcomponent/viewcomponent[template ='teaseraggregation']/url"/>  
    
    <!-- Teaser Url mit Portangabe für Sandoz Static  -->
    <xsl:variable name="teaserUrl" select="concat('http://', $fullServerName,'/',$language,'/',$teaserPath,'/content.xhtml')"/>
    
    
    <xsl:template match="/" priority="1.5">
        
        <xsl:choose>
            <xsl:when test="//viewcomponent/template = 'teaseraggregation'">
                <xsl:variable name="teaseragg" select="document($teaserUrl)"/>
                <xsl:copy-of select="$teaseragg/*"/>
            </xsl:when>
            <xsl:otherwise><noteaseraggregation/></xsl:otherwise>
        </xsl:choose>     
        
    </xsl:template>
    
</xsl:stylesheet>
