<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">

	<xsl:param name="defaultImage" select="''"/>
	<xsl:param name="defaultLogo" select="''"/>

	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="logoimage">
		<modules>
			<xsl:apply-templates select="navigation[1]"/>
		</modules>
	</xsl:template>

	<!--Unitimages als logo und image Modul definieren-->
	<xsl:template match="navigation">
		<ctmpl:attribute name="unitimage">
		    <xsl:attribute name="value">
				<xsl:choose>
					<xsl:when test="@unitImageId">
                        background-image: url(/cms/img/ejbimage?Typ=S&amp;id=<xsl:value-of select="@unitImageId"/>);
					</xsl:when>
					<xsl:otherwise>
                        background-image: url(<xsl:value-of select="$defaultImage"/>);
					</xsl:otherwise>
				</xsl:choose>
                background-repeat:no-repeat;
                background-position:right;
		    </xsl:attribute>
		</ctmpl:attribute>
	</xsl:template>
</xsl:stylesheet>