<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
<xsl:output method="text" indent="no" />

<xsl:template match="/">
    <xsl:apply-templates select="styles" mode="common"/>
    <xsl:apply-templates select="styles" mode="mandator"/>
</xsl:template>

<!--
  all elements inside should not reference any colors, just
  set width, height, paddings and margins - doesn't make
  sense for all templates!  
-->
<xsl:template match="styles" mode="common">
</xsl:template>

<!-- 
  nothing should be inside of this, should be overwritten by
  mandator
-->
<xsl:template match="styles" mode="mandator">
</xsl:template>
    
</xsl:stylesheet>