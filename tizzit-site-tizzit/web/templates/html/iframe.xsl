<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
 
   <xsl:include href="common.xsl"/>
    
    <xsl:template match="content" mode="include-after">
        <xsl:apply-templates select="../iframe"/>
    </xsl:template>
    
    <xsl:template match="iframe">
        <iframe frameborder="0" align="left" style="padding:0px;margin:0px;">
            <xsl:attribute name="src"><xsl:value-of select="iframeSrc"/></xsl:attribute>
            <xsl:attribute name="width"><xsl:value-of select="iframeWidth"/></xsl:attribute>
            <xsl:attribute name="height"><xsl:value-of select="iframeHeight"/></xsl:attribute>
            <xsl:attribute name="name"><xsl:value-of select="iframeName"/></xsl:attribute>
        &#160;</iframe>
    </xsl:template>
    
</xsl:stylesheet>