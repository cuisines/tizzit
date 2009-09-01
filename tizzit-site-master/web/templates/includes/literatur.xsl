<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


	<xsl:template match="literaturangabe" mode="format">
		<xsl:apply-templates select="literatur-aufzaehlung" mode="literatur-aufzaehlung"/>
		<xsl:apply-templates select="literatur-autor" mode="literatur-autor"/>
		<xsl:apply-templates select="literatur-titel" mode="literatur-titel"/>
		<xsl:apply-templates select="literatur-veroeffentlicht-in" mode="literatur-veroeffentlicht-in"/>
		<xsl:apply-templates select="literatur-link" mode="literatur-link"/>
		<xsl:apply-templates select="literatur-sonstiges" mode="literatur-sontiges"/>
	</xsl:template>

	<xsl:template match="literatur-aufzaehlung" mode="literatur-aufzaehlung">
		<xsl:if test="not(boolean(.=''))">
		<font class="literaturangabe">
			<b>
				<xsl:apply-templates mode="format"/>&#160;
			</b>
		</font>
		</xsl:if>
	</xsl:template>

	<xsl:template match="literatur-autor" mode="literatur-autor">
		<xsl:if test="not(boolean(.=''))">
		<font class="literaturangabe">
			<xsl:apply-templates mode="format"/>:&#160;
		</font>
		</xsl:if>
	</xsl:template>

	<xsl:template match="literatur-titel" mode="literatur-titel">
		<xsl:if test="not(boolean(.=''))">
		<font class="literaturangabe">
			<b>
				<xsl:apply-templates mode="format"/>,&#160;
			</b>
		</font>
		</xsl:if>
	</xsl:template>

	<xsl:template match="literatur-veroeffentlicht-in" mode="literatur-veroeffentlicht-in">
		<xsl:if test="not(boolean(.=''))">
		<font class="literaturangabe">
		In:&#160;<xsl:apply-templates mode="format"/>.&#160;
		</font>
		</xsl:if>
	</xsl:template>

	<xsl:template match="literatur-link" mode="literatur-link">
			<xsl:if test="not(boolean(.=''))">
		<a>
			<xsl:attribute name="href">
				<xsl:value-of select="@url"/>
			</xsl:attribute>
			<xsl:attribute name="id">
				<xsl:value-of select="@id"/>
			</xsl:attribute>
			<xsl:attribute name="target">
				<xsl:value-of select="@target"/>
			</xsl:attribute>
			<xsl:attribute name="alt">
				<xsl:value-of select="@alt"/>
			</xsl:attribute>
			<xsl:attribute name="name">
				<xsl:value-of select="@name"/>
			</xsl:attribute>
			<xsl:apply-templates mode="format"/>
		</a>,&#160;
		</xsl:if>
	</xsl:template>

	<xsl:template match="literatur-sonstiges" mode="literatur-sonstiges">
		<xsl:if test="not(boolean(.=''))">
		<font class="literaturangabe">
			<xsl:apply-templates mode="format"/>
		</font>,&#160;
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
<!-- Stylus Studio meta-information - (c)1998-2001 eXcelon Corp.
<metaInformation>
<scenarios/><MapperInfo  srcSchemaPath="" srcSchemaRoot="" srcSchemaPathIsRelative="yes" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" />
</metaInformation>
-->
