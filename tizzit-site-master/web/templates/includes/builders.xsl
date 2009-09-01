<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:template name="hrefBuilder2">
		<xsl:param name="url"/>
		<xsl:param name="file"/>
		<xsl:value-of select="concat('/',$language,'/',$url,'/',$file)"/>
	</xsl:template>
	
</xsl:stylesheet>
