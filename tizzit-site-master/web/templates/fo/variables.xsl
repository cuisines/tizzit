<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:param name="newsNr" select="0"/>
	<xsl:param name="jobNr" select="0"/>
	<xsl:param name="currentDate" select="''"/>
	<xsl:param name="liveserver" select="''"/>
	<xsl:param name="serverName" select="''"/>
	<xsl:param name="serverPort" select="''"/>

	<xsl:variable name="day">
		<xsl:value-of select="number(substring($currentDate,1,2))"/>
	</xsl:variable>
	<xsl:variable name="month">
		<xsl:value-of select="number(substring($currentDate,4,2))"/>
	</xsl:variable>
	<xsl:variable name="year">
		<xsl:value-of select="number(substring($currentDate,7,4))"/>
	</xsl:variable>

    <xsl:variable name="staticImgUrl">
        <xsl:if test="$serverPort = '80'">http://</xsl:if>
        <xsl:if test="$serverPort = '443'">https://</xsl:if>
        <xsl:value-of select="$serverName" />/httpd/img/</xsl:variable>

    <xsl:variable name="ejbImgUrl">
        <xsl:if test="$serverPort = '80'">http://</xsl:if>
        <xsl:if test="$serverPort = '443'">https://</xsl:if>
        <xsl:value-of select="$serverName" />/img/</xsl:variable>
        
	<xsl:variable name="xmlRequestUrl">
		<xsl:if test="$serverPort = '80'">http://</xsl:if>
		<xsl:if test="$serverPort = '443'">https://</xsl:if>
		<xsl:value-of select="$serverName" />/</xsl:variable>

	<xsl:variable name="dpifactor" select="'1'"/>
        
</xsl:stylesheet>