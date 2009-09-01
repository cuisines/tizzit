<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


	<xsl:param name="requestQuery" select="''"/>
	<xsl:param name="language" select="'deutsch'"/>
	<xsl:param name="url" select="''"/>
	<xsl:param name="viewComponentId" select="''"/>
	<xsl:param name="print" select="0"/>
	<xsl:param name="anchor" select="''"/>
	<xsl:param name="liveserver" select="''"/>
	<xsl:param name="clientCode" select="''"/>
	<xsl:param name="template" select="''"/>
	<xsl:param name="currentDate" select="''"/>
	<xsl:param name="preStringCinclude" select="'cocoon:/'"/>
	<xsl:param name="safeguardlogedin" select="''"/>
	<xsl:param name="safeguardUsername" select="''"></xsl:param>	
		
		
	<xsl:param name="paramString">
		<xsl:choose>
			<xsl:when test="$requestQuery!=''">
				<xsl:value-of select="$requestQuery"/>
				<xsl:text>&amp;</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>?</xsl:text>
			</xsl:otherwise>
		</xsl:choose>	
		<!-- add here additional parameters to be passed to the modules -->
		<xsl:text>safeguardlogedin=</xsl:text>
		<xsl:value-of select="$safeguardlogedin"/>		
		<xsl:text>&amp;</xsl:text>		
		<xsl:text>safeguardUsername=</xsl:text>
		<xsl:value-of select="$safeguardUsername"/>		
	</xsl:param>
	
	


</xsl:stylesheet>
