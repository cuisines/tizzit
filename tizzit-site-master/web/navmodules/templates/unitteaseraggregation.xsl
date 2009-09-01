<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">
    
    <xsl:include href="variables.xsl"/>
    
    <xsl:variable name="teaserId" select="//viewcomponent/viewcomponent[template ='unitteaseraggregation']/@id"/>
    <xsl:variable name="teaserPath" select="//viewcomponent/viewcomponent[template ='unitteaseraggregation']/url"/>  
      
    <!-- Teaser Url mit Portangabe für Sandoz Static  -->
    <xsl:variable name="teaserUrl" select="concat('http://', $fullServerName,'/',$language,'/',$teaserPath,'/content.xhtml')"/>
           
    <xsl:template match="/" priority="1">
        
        <xsl:choose>
            <xsl:when test="//viewcomponent/template = 'unitteaseraggregation'">
            <xsl:variable name="teaseragg" select="document($teaserUrl)"/>
            <xsl:copy-of select="$teaseragg/*"/>
            </xsl:when>
            <xsl:otherwise><nounitteaseraggregation/></xsl:otherwise>
        </xsl:choose>     
           
    </xsl:template>

    <!-- for debugging just change priority to 1.5 !!!-->
    <xsl:template match="/" priority="0.5">
        <test>        
            <xsl:if test="//viewcomponent/template = 'unitteaseraggregation'">
                <unitteaseraggregation-exists/>                
                <teaserPath><xsl:value-of select="$teaserPath"/></teaserPath>
                <teaserid><xsl:value-of select="$teaserId"/></teaserid>
                <teaserurl><xsl:value-of select="$teaserUrl"/></teaserurl>
                <!--                
                <xsl:variable name="teaseragg" select="document($teaserUrl)"/>
                <xsl:copy-of select="$teaseragg/*"/>
                -->
            </xsl:if>            
        </test>
    </xsl:template>
    
</xsl:stylesheet>
