<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:template name="substring-before-last">
        <xsl:param name="string"/>
        <xsl:param name="pattern"/>
        <xsl:choose>
            <xsl:when test="substring-before($string,$pattern) != '' or starts-with($string,$pattern)">
                <xsl:value-of select="substring-before($string,$pattern)"/>
                <xsl:value-of select="$pattern"/>
                <xsl:call-template name="substring-before-last">
                    <xsl:with-param name="string" select="substring-after($string,$pattern)"/>
                    <xsl:with-param name="pattern" select="$pattern"/>
                </xsl:call-template>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
</xsl:stylesheet>