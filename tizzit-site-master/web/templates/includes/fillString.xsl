<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template name="fillString">
        <xsl:param name="pattern"/>
        <xsl:param name="maxlength"/>
        <xsl:param name="actlength"/>
        <xsl:choose>
            <xsl:when test="$actlength &lt; $maxlength">
                <xsl:value-of select="$pattern"/>
                <xsl:call-template name="fillString">
                    <xsl:with-param name="pattern" select="$pattern"/>
                    <xsl:with-param name="maxlength" select="$maxlength"/>
                    <xsl:with-param name="actlength" select="$actlength + 1"/>
                </xsl:call-template>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
</xsl:stylesheet>