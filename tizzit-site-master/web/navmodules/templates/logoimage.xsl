<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">

	<!--rootunit images werden als default images definiert-->
<!--	<xsl:param name="defaultImageId">123
		<xsl:value-of select="//navigation[1]/@unitImageId"/>
	</xsl:param>
	<xsl:param name="defaultLogoId">
		<xsl:value-of select="//navigation[2]/@unitLogoId"/>
	</xsl:param>
-->


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
		<ctmpl:module name="logo">
			<xsl:if test="@unitLogoId or $defaultLogo!=''">
				<div class="logo">
					<xsl:choose>
						<xsl:when test="@unitLogoId">
							<img border="0">
								<xsl:attribute name="alt">
									<xsl:text>Logo</xsl:text>
								</xsl:attribute>
								<xsl:attribute name="src">/img/ejbimage<xsl:choose>
									<xsl:when test="filename != ''">/<xsl:value-of select="filename"/></xsl:when>
									<xsl:otherwise><xsl:text>/dummy.jpg</xsl:text></xsl:otherwise>
								</xsl:choose>?id=<xsl:value-of select="@unitLogoId"/>&amp;typ=S</xsl:attribute>
							</img>
						</xsl:when>
						<xsl:otherwise>
							<img src="{$defaultLogo}" alt="Logo" border="0"/>
						</xsl:otherwise>
					</xsl:choose>
				</div>
			</xsl:if>
		</ctmpl:module> 
		<ctmpl:module name="image">
			<xsl:if test="@unitImageId or $defaultImage!=''">
				<div class="image">
					<xsl:choose>
						<xsl:when test="@unitImageId">
						    <img border="0">
						        <xsl:attribute name="alt">
						            <xsl:text>UnitImage</xsl:text>
						        </xsl:attribute>
						        <xsl:attribute name="src">/img/ejbimage<xsl:choose>
						            <xsl:when test="filename != ''">/<xsl:value-of select="filename"/></xsl:when>
						            <xsl:otherwise><xsl:text>/dummy.jpg</xsl:text></xsl:otherwise>
						        </xsl:choose>?id=<xsl:value-of select="@unitImageId"/>&amp;typ=S</xsl:attribute>
						    </img>
						</xsl:when>
						<xsl:otherwise>
							<img src="{$defaultImage}" alt="UnitImage" border="0"/>
						</xsl:otherwise>
					</xsl:choose>
				</div>
			</xsl:if>
		</ctmpl:module>
	</xsl:template>
</xsl:stylesheet><!-- Stylus Studio meta-information - (c)1998-2004. Sonic Software Corporation. All rights reserved.
<metaInformation>
<scenarios/><MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>
</metaInformation>
-->