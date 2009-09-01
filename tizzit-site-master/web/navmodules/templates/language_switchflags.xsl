<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">
    
    <xsl:param name="language" select="'de'"/>

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
                    <xsl:apply-templates select="../../language" mode="languageVersions"/>
                </div>
            </ctmpl:module>
        </modules>
    </xsl:template>
    
    <xsl:template match="language" mode="languageVersions">
       <span>
            <a>
                <xsl:choose>
                    <xsl:when test="$language = 'de'">                    
                        <xsl:attribute name="href">/en/page.html</xsl:attribute>
                        Englisch
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:attribute name="href">/de/page.html</xsl:attribute>
                        German
                    </xsl:otherwise>
                </xsl:choose>                                
            </a>
        </span>
    </xsl:template>
    
</xsl:stylesheet>
