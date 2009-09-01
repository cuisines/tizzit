<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">
    
    <xsl:template match="/">
       <xsl:apply-templates />
    </xsl:template> 

    <xsl:template match="language">
       <xsl:apply-templates select="languageVersions"/>
    </xsl:template>       
   
    <xsl:template match="languageVersions">
        <modules>
            <ctmpl:module name="language" xmlns:ctmpl="http://www.conquest-cms.net/template">
                <div class="languageFlags">
                    <xsl:apply-templates select="language" mode="languageVersions"/>
                </div>
            </ctmpl:module>
        </modules>
    </xsl:template>
    
    <xsl:template match="language" mode="languageVersions">
       <span>
            <a>
                <xsl:attribute name="href">/<xsl:value-of select="langName"/>/<xsl:value-of select="langUrl"/>/page.html</xsl:attribute>
                <img>
                    <xsl:attribute name="src">
                        <xsl:text>/httpd/img/flag_</xsl:text>
                        <xsl:value-of select="langName"/>
                        <xsl:text>.gif</xsl:text>
                    </xsl:attribute>
                    <xsl:attribute name="border">
                        <xsl:text>0</xsl:text>
                    </xsl:attribute>
                </img>
            </a>
        </span>
    </xsl:template>
    
</xsl:stylesheet>
