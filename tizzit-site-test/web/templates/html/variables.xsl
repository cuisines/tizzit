<?xml version='1.0'?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:param name="language" select="'en'"/>
	<xsl:param name="url" select="''"/>
	<xsl:param name="viewComponentId" select="''"/> 
	<xsl:param name="print" select="0"/>
	<xsl:param name="anchor" select="''"/>
	<xsl:param name="liveserver" select="''"/>
	<xsl:param name="clientCode" select="''"/>
	<xsl:param name="template" select="''"/>	
	<xsl:param name="host" select="''"/>
	<xsl:param name="serverName" select="''"/>
	<xsl:param name="serverPort" select="''"/>
	<xsl:param name="userAgentString" select="''"/>
	
	<xsl:variable name="userAgent">
		<xsl:choose>
			<xsl:when test="contains(translate($userAgentString, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'safari')">safari</xsl:when>
			<xsl:when test="contains(translate($userAgentString, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'opera')">opera</xsl:when>
			<xsl:when test="contains(translate($userAgentString, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'msie')">ie</xsl:when>
			<xsl:when test="contains(translate($userAgentString, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'netscape')">mozilla</xsl:when>
			<xsl:when test="contains(translate($userAgentString, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'mozilla')">mozilla</xsl:when>
			<xsl:when test="contains(translate($userAgentString, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'firefox')">mozilla</xsl:when>
			<xsl:otherwise>unknown</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>

</xsl:stylesheet>