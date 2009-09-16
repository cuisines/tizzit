<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">
    
    <xsl:template match="/"> 
        <xsl:apply-templates />
    </xsl:template> 
    
    <xsl:template match="language">
        <xsl:apply-templates select="languageVersions"/>
    </xsl:template>        
    
    <xsl:template match="languageVersions">
        <modules>
            <ctmpl:module name="language" xmlns:ctmpl="http://www.conquest-cms.net/template">
                <xsl:if test="language">
                    <div class="languageFlags">
                        <xsl:apply-templates select="language" mode="languageVersions"/>
                    </div>
                </xsl:if>
            </ctmpl:module>
        </modules>
    </xsl:template>
    
    <xsl:template match="language" mode="languageVersions">
        <span class="flag">
            <a>
                <xsl:attribute name="href">
                    <xsl:text>/</xsl:text>
                    <xsl:value-of select="langName"/>
                    <xsl:text>/</xsl:text>
                    <xsl:value-of select="langUrl"/>
                    <xsl:text>/page.html</xsl:text>
                </xsl:attribute>
                <img border="0">
                    <xsl:attribute name="src">
                        <xsl:text>/httpd/img/flags/</xsl:text>
                        <xsl:value-of select="langName"/>
                        <xsl:text>.gif</xsl:text>
                    </xsl:attribute>
                </img>
            </a>
        </span>
    </xsl:template>
    
</xsl:stylesheet>