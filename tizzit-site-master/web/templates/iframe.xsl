<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  
  <xsl:param name="id" select="''"/>

	<xsl:template match="content" mode="include" priority="0.5">
		<xsl:apply-templates select="../iframe"/>		
	</xsl:template>
	
	<xsl:template match="iframe">
		<iframe height="100%" width="535" id="iframename" src="{iframeSrc}" name="{iframeName}" frameborder="0" scrolling="no" marginheight="1" marginwidth="0" style="padding-top: 1">
		</iframe>		
		<!-- Bitte auch iframe script einbinden!! -->
	</xsl:template>

	

</xsl:stylesheet>

