<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cinclude="http://apache.org/cocoon/include/1.0">

    <xsl:param name="formURI" select="''"/>
    
    <!--cinclude Element schreiben -->
	<xsl:template match="includexml">
			<cinclude:include ignoreErrors="true" xmlns:cinclude="http://apache.org/cocoon/include/1.0">
				<xsl:attribute name="src">cocoon:/<xsl:value-of select="$formURI"/>.flow</xsl:attribute>
			</cinclude:include>
	</xsl:template>

	<!--durch cform Element durchschleusen -->
	<xsl:template match="cform">
	    <xsl:apply-templates />
	</xsl:template>
	
	<!--Alles andere matchen und wieder schreiben -->
	<xsl:template match="@*|node()" priority="-1">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>	
	
</xsl:stylesheet>
