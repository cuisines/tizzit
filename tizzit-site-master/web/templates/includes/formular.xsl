<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!--anfang formularfelder menu -->
	<!--anfang popup menu-->
	<xsl:template match="popupmenu" mode="format">
		<select name="popup">
			<xsl:apply-templates select="option" mode="option"/>
		</select>
	</xsl:template>

	<xsl:template match="option" mode="option">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<option>
					<xsl:value-of select="."/>
				</option>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!--ende popup menu-->

	<xsl:template match="textarea" mode="format">
		<textarea>
			<xsl:attribute name="name">
				<xsl:value-of select="@name"/>
			</xsl:attribute>
			<xsl:attribute name="rows">
				<xsl:value-of select="@rows"/>
			</xsl:attribute>
			<xsl:attribute name="cols">
				<xsl:value-of select="@cols"/>
			</xsl:attribute>
			<xsl:value-of select="."/>
		</textarea>
	</xsl:template>

	<xsl:template match="input" mode="format">
		<input>
			<xsl:attribute name="name">
				<xsl:value-of select="@name"/>
			</xsl:attribute>
			<xsl:attribute name="type">
				<xsl:value-of select="@type"/>
			</xsl:attribute>
			<xsl:attribute name="value">
				<xsl:value-of select="@value"/>
			</xsl:attribute>
			<xsl:value-of select="."/>
		</input>
	</xsl:template>

	<xsl:template match="inputchecked" mode="format">
		<input>
			<xsl:attribute name="checked">
				<xsl:value-of select="@checked"/>
			</xsl:attribute>
			<xsl:attribute name="name">
				<xsl:value-of select="@name"/>
			</xsl:attribute>
			<xsl:attribute name="type">
				<xsl:value-of select="@type"/>
			</xsl:attribute>
			<xsl:attribute name="value">
				<xsl:value-of select="@value"/>
			</xsl:attribute>
			<xsl:value-of select="."/>
		</input>
	</xsl:template>
	<!-- test check dev area -->

	<xsl:template match="inputtest" mode="format">
		<input>
			<xsl:attribute name="name">
				<xsl:value-of select="@name"/>
			</xsl:attribute>
			<xsl:attribute name="type">
				<xsl:value-of select="@type"/>
			</xsl:attribute>
			<xsl:attribute name="value">
				<xsl:value-of select="@value"/>
			</xsl:attribute>
			<xsl:value-of select="."/>
		</input>
	</xsl:template>
	<!--ende formularfelder menu -->
</xsl:stylesheet><!-- Stylus Studio meta-information - (c)1998-2001 eXcelon Corp.
<metaInformation>
<scenarios/><MapperInfo  srcSchemaPath="" srcSchemaRoot="" srcSchemaPathIsRelative="yes" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" />
</metaInformation>
-->
