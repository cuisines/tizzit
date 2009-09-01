<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template name="tree2flashvars">
        <xsl:param name="tree"/>
        
        <xsl:for-each select="$tree/params/param">
            <xsl:choose>
                <xsl:when test="position() = 1">
                    <xsl:value-of select="concat(@name,'=',@value)"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="concat('&amp;',@name,'=',@value)"/>                    
                </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>