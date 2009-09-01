<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template name="weekcode2short">
    <xsl:param name="weekcode"/>
    <xsl:choose>
        <xsl:when test="$weekcode = '0'"><xsl:text>Mo</xsl:text></xsl:when>
        <xsl:when test="$weekcode = '1'"><xsl:text>Di</xsl:text></xsl:when>
        <xsl:when test="$weekcode = '2'"><xsl:text>Mi</xsl:text></xsl:when>
        <xsl:when test="$weekcode = '3'"><xsl:text>Do</xsl:text></xsl:when>
        <xsl:when test="$weekcode = '4'"><xsl:text>Fr</xsl:text></xsl:when>
        <xsl:when test="$weekcode = '5'"><xsl:text>Sa</xsl:text></xsl:when>
        <xsl:when test="$weekcode = '6'"><xsl:text>So</xsl:text></xsl:when>
        <xsl:otherwise><xsl:text>N.A.</xsl:text></xsl:otherwise>
    </xsl:choose>
</xsl:template>
    
<xsl:template name="weekcode2long">
    <xsl:param name="weekcode"/>
    <xsl:choose>
        <xsl:when test="$weekcode = '0'"><xsl:text>Montag</xsl:text></xsl:when>
        <xsl:when test="$weekcode = '1'"><xsl:text>Dienstag</xsl:text></xsl:when>
        <xsl:when test="$weekcode = '2'"><xsl:text>Mittwoch</xsl:text></xsl:when>
        <xsl:when test="$weekcode = '3'"><xsl:text>Donnerstag</xsl:text></xsl:when>
        <xsl:when test="$weekcode = '4'"><xsl:text>Freitag</xsl:text></xsl:when>
        <xsl:when test="$weekcode = '5'"><xsl:text>Samstag</xsl:text></xsl:when>
        <xsl:when test="$weekcode = '6'"><xsl:text>Sonntag</xsl:text></xsl:when>
        <xsl:otherwise><xsl:text>N.A.</xsl:text></xsl:otherwise>
    </xsl:choose>
</xsl:template>
    
</xsl:stylesheet>