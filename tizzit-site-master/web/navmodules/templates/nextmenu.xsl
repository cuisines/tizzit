<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">
    
    <xsl:include href="master.xsl"/>
    
    <xsl:template match="navigation" priority="1.5">
            <xsl:apply-templates select="viewcomponent"/>
    </xsl:template>
    
    <xsl:template match="viewcomponent" priority="1.1">        
        <xsl:choose>
            <xsl:when test="visible = 'true'">
                <a>
                    <xsl:call-template name="href"/>   
                    <xsl:text>next</xsl:text>
                </a>
            </xsl:when>
            <xsl:otherwise>
                &#160;
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>