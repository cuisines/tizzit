<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


    <xsl:param name="language" select="'de'"/>
    <xsl:param name="url" select="''"/>
    <xsl:param name="serverName" select="''"/>
    <xsl:param name="serverPort" select="''"/>
	<xsl:param name="viewComponentId" select="''"/>
	<xsl:param name="print" select="0"/>
	<xsl:param name="anchor" select="''"/>
	<xsl:param name="liveserver" select="''"/>
	<xsl:param name="clientCode" select="''"/>
	<xsl:param name="template" select="''"/>
    <xsl:param name="safeguardlogedin" select="''"/>
    
    <!-- Only for BIG 15.08.07 -->
    <xsl:param name="thistargetgroup" select="normalize-space(//targetgroup//targetgroup)"/>
    
    <xsl:param name="targetgroup">
        <xsl:choose>
            <xsl:when test="$thistargetgroup='pat'">pat</xsl:when><!-- Only for BIG 15.08.07 -->
            <xsl:when test="$safeguardlogedin='true'">pro</xsl:when>
            <xsl:otherwise>pub</xsl:otherwise>
        </xsl:choose>
    </xsl:param>

    <xsl:param name="fullServerName">
        <xsl:value-of select="$serverName"/>
        <xsl:choose>
            <xsl:when test="$serverPort!=''">
                <xsl:text>:</xsl:text>
                <xsl:value-of select="$serverPort"/>
            </xsl:when>
        </xsl:choose>
    </xsl:param>


</xsl:stylesheet>
